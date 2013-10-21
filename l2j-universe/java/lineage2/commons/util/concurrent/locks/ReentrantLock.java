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
public class ReentrantLock
{
	/**
	 * Field stateUpdater.
	 */
	private static final AtomicIntegerFieldUpdater<ReentrantLock> stateUpdater = AtomicIntegerFieldUpdater.newUpdater(ReentrantLock.class, "state");
	/**
	 * Field owner.
	 */
	private Thread owner;
	/**
	 * Field state.
	 */
	private volatile int state;
	
	/**
	 * Constructor for ReentrantLock.
	 */
	public ReentrantLock()
	{
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
	 * Method lock.
	 */
	public void lock()
	{
		if (compareAndSetState(0, 1))
		{
			setExclusiveOwnerThread(Thread.currentThread());
		}
		else
		{
			for (;;)
			{
				if (tryLock())
				{
					break;
				}
			}
		}
	}
	
	/**
	 * Method tryLock.
	 * @return boolean
	 */
	public boolean tryLock()
	{
		final Thread current = Thread.currentThread();
		int c = getState();
		if (c == 0)
		{
			if (compareAndSetState(0, 1))
			{
				setExclusiveOwnerThread(current);
				return true;
			}
		}
		else if (current == getExclusiveOwnerThread())
		{
			int nextc = c + 1;
			if (nextc < 0)
			{
				throw new Error("Maximum lock count exceeded");
			}
			setState(nextc);
			return true;
		}
		return false;
	}
	
	/**
	 * Method unlock.
	 * @return boolean
	 */
	public boolean unlock()
	{
		int c = getState() - 1;
		if (Thread.currentThread() != getExclusiveOwnerThread())
		{
			throw new IllegalMonitorStateException();
		}
		boolean free = false;
		if (c == 0)
		{
			free = true;
			setExclusiveOwnerThread(null);
		}
		setState(c);
		return free;
	}
}
