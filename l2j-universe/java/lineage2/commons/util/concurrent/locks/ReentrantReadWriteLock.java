/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package lineage2.commons.util.concurrent.locks;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ReentrantReadWriteLock
{
	/**
	 * Field stateUpdater.
	 */
	private static final AtomicIntegerFieldUpdater<ReentrantReadWriteLock> stateUpdater = AtomicIntegerFieldUpdater.newUpdater(ReentrantReadWriteLock.class, "state");
	/**
	 * Field SHARED_SHIFT. (value is 16)
	 */
	static final int SHARED_SHIFT = 16;
	/**
	 * Field SHARED_UNIT.
	 */
	static final int SHARED_UNIT = (1 << SHARED_SHIFT);
	/**
	 * Field MAX_COUNT.
	 */
	static final int MAX_COUNT = (1 << SHARED_SHIFT) - 1;
	/**
	 * Field EXCLUSIVE_MASK.
	 */
	static final int EXCLUSIVE_MASK = (1 << SHARED_SHIFT) - 1;
	
	/**
	 * Method sharedCount.
	 * @param c int
	 * @return int
	 */
	static int sharedCount(int c)
	{
		return c >>> SHARED_SHIFT;
	}
	
	/**
	 * Method exclusiveCount.
	 * @param c int
	 * @return int
	 */
	static int exclusiveCount(int c)
	{
		return c & EXCLUSIVE_MASK;
	}
	
	/**
	 * @author Mobius
	 */
	static final class HoldCounter
	{
		/**
		 * Field count.
		 */
		int count;
		/**
		 * Field tid.
		 */
		final long tid = Thread.currentThread().getId();
		
		/**
		 * Method tryDecrement.
		 * @return int
		 */
		int tryDecrement()
		{
			int c = count;
			if (c > 0)
			{
				count = c - 1;
			}
			return c;
		}
	}
	
	/**
	 * @author Mobius
	 */
	static final class ThreadLocalHoldCounter extends ThreadLocal<HoldCounter>
	{
		/**
		 * Method initialValue.
		 * @return HoldCounter
		 */
		@Override
		public HoldCounter initialValue()
		{
			return new HoldCounter();
		}
	}
	
	/**
	 * Field readHolds.
	 */
	transient ThreadLocalHoldCounter readHolds;
	/**
	 * Field cachedHoldCounter.
	 */
	transient HoldCounter cachedHoldCounter;
	/**
	 * Field owner.
	 */
	private Thread owner;
	/**
	 * Field state.
	 */
	private volatile int state;
	
	/**
	 * Constructor for ReentrantReadWriteLock.
	 */
	public ReentrantReadWriteLock()
	{
		readHolds = new ThreadLocalHoldCounter();
		setState(0);
	}
	
	/**
	 * Method getState.
	 * @return int
	 */
	private final int getState()
	{
		return state;
	}
	
	/**
	 * Method setState.
	 * @param newState int
	 */
	private void setState(int newState)
	{
		state = newState;
	}
	
	/**
	 * Method compareAndSetState.
	 * @param expect int
	 * @param update int
	 * @return boolean
	 */
	private boolean compareAndSetState(int expect, int update)
	{
		return stateUpdater.compareAndSet(this, expect, update);
	}
	
	/**
	 * Method getExclusiveOwnerThread.
	 * @return Thread
	 */
	private Thread getExclusiveOwnerThread()
	{
		return owner;
	}
	
	/**
	 * Method setExclusiveOwnerThread.
	 * @param thread Thread
	 */
	private void setExclusiveOwnerThread(Thread thread)
	{
		owner = thread;
	}
	
	/**
	 * Method writeLock.
	 */
	public void writeLock()
	{
		Thread current = Thread.currentThread();
		for (;;)
		{
			int c = getState();
			int w = exclusiveCount(c);
			if (c != 0)
			{
				if ((w == 0) || (current != getExclusiveOwnerThread()))
				{
					continue;
				}
				if ((w + exclusiveCount(1)) > MAX_COUNT)
				{
					throw new Error("Maximum lock count exceeded");
				}
			}
			if (compareAndSetState(c, c + 1))
			{
				setExclusiveOwnerThread(current);
				return;
			}
		}
	}
	
	/**
	 * Method tryWriteLock.
	 * @return boolean
	 */
	public boolean tryWriteLock()
	{
		Thread current = Thread.currentThread();
		int c = getState();
		if (c != 0)
		{
			int w = exclusiveCount(c);
			if ((w == 0) || (current != getExclusiveOwnerThread()))
			{
				return false;
			}
			if (w == MAX_COUNT)
			{
				throw new Error("Maximum lock count exceeded");
			}
		}
		if (!compareAndSetState(c, c + 1))
		{
			return false;
		}
		setExclusiveOwnerThread(current);
		return true;
	}
	
	/**
	 * Method tryReadLock.
	 * @return boolean
	 */
	final boolean tryReadLock()
	{
		Thread current = Thread.currentThread();
		int c = getState();
		int w = exclusiveCount(c);
		if ((w != 0) && (getExclusiveOwnerThread() != current))
		{
			return false;
		}
		if (sharedCount(c) == MAX_COUNT)
		{
			throw new Error("Maximum lock count exceeded");
		}
		if (compareAndSetState(c, c + SHARED_UNIT))
		{
			HoldCounter rh = cachedHoldCounter;
			if ((rh == null) || (rh.tid != current.getId()))
			{
				cachedHoldCounter = rh = readHolds.get();
			}
			rh.count++;
			return true;
		}
		return false;
	}
	
	/**
	 * Method readLock.
	 */
	public void readLock()
	{
		Thread current = Thread.currentThread();
		HoldCounter rh = cachedHoldCounter;
		if ((rh == null) || (rh.tid != current.getId()))
		{
			rh = readHolds.get();
		}
		for (;;)
		{
			int c = getState();
			int w = exclusiveCount(c);
			if ((w != 0) && (getExclusiveOwnerThread() != current))
			{
				continue;
			}
			if (sharedCount(c) == MAX_COUNT)
			{
				throw new Error("Maximum lock count exceeded");
			}
			if (compareAndSetState(c, c + SHARED_UNIT))
			{
				cachedHoldCounter = rh;
				rh.count++;
				return;
			}
		}
	}
	
	/**
	 * Method writeUnlock.
	 */
	public void writeUnlock()
	{
		int nextc = getState() - 1;
		if (Thread.currentThread() != getExclusiveOwnerThread())
		{
			throw new IllegalMonitorStateException();
		}
		if (exclusiveCount(nextc) == 0)
		{
			setExclusiveOwnerThread(null);
			setState(nextc);
			return;
		}
		setState(nextc);
		return;
	}
	
	/**
	 * Method readUnlock.
	 */
	public void readUnlock()
	{
		HoldCounter rh = cachedHoldCounter;
		Thread current = Thread.currentThread();
		if ((rh == null) || (rh.tid != current.getId()))
		{
			rh = readHolds.get();
		}
		if (rh.tryDecrement() <= 0)
		{
			throw new IllegalMonitorStateException();
		}
		for (;;)
		{
			int c = getState();
			int nextc = c - SHARED_UNIT;
			if (compareAndSetState(c, nextc))
			{
				return;
			}
		}
	}
}
