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
package lineage2.commons.net.nio.impl;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
@SuppressWarnings("rawtypes")
public class MMOExecutableQueue<T extends MMOClient> implements Queue<ReceivablePacket<T>>, Runnable
{
	/**
	 * Field NONE. (value is 0)
	 */
	private static final int NONE = 0;
	/**
	 * Field QUEUED. (value is 1)
	 */
	private static final int QUEUED = 1;
	/**
	 * Field RUNNING. (value is 2)
	 */
	private static final int RUNNING = 2;
	/**
	 * Field _executor.
	 */
	private final IMMOExecutor<T> _executor;
	/**
	 * Field _queue.
	 */
	private final Queue<ReceivablePacket<T>> _queue;
	/**
	 * Field _state.
	 */
	private final AtomicInteger _state = new AtomicInteger(NONE);
	
	/**
	 * Constructor for MMOExecutableQueue.
	 * @param executor IMMOExecutor<T>
	 */
	public MMOExecutableQueue(IMMOExecutor<T> executor)
	{
		_executor = executor;
		_queue = new ArrayDeque<>();
	}
	
	/**
	 * Method run.
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run()
	{
		while (_state.compareAndSet(QUEUED, RUNNING))
		{
			try
			{
				for (;;)
				{
					final Runnable t = poll();
					if (t == null)
					{
						break;
					}
					t.run();
				}
			}
			finally
			{
				_state.compareAndSet(RUNNING, NONE);
			}
		}
	}
	
	/**
	 * Method size.
	 * @return int * @see java.util.Collection#size()
	 */
	@Override
	public int size()
	{
		return _queue.size();
	}
	
	/**
	 * Method isEmpty.
	 * @return boolean * @see java.util.Collection#isEmpty()
	 */
	@Override
	public boolean isEmpty()
	{
		return _queue.isEmpty();
	}
	
	/**
	 * Method contains.
	 * @param o Object
	 * @return boolean * @see java.util.Collection#contains(Object)
	 */
	@Override
	public boolean contains(Object o)
	{
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Method iterator.
	 * @return Iterator<ReceivablePacket<T>> * @see java.util.Collection#iterator()
	 */
	@Override
	public Iterator<ReceivablePacket<T>> iterator()
	{
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Method toArray.
	 * @return Object[] * @see java.util.Collection#toArray()
	 */
	@Override
	public Object[] toArray()
	{
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Method toArray.
	 * @param a E[]
	 * @return E[]
	 */
	@Override
	public <E> E[] toArray(E[] a)
	{
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Method remove.
	 * @param o Object
	 * @return boolean * @see java.util.Collection#remove(Object)
	 */
	@Override
	public boolean remove(Object o)
	{
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Method containsAll.
	 * @param c Collection<?>
	 * @return boolean * @see java.util.Collection#containsAll(Collection<?>)
	 */
	@Override
	public boolean containsAll(Collection<?> c)
	{
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Method addAll.
	 * @param c Collection<? extends ReceivablePacket<T>>
	 * @return boolean * @see java.util.Collection#addAll(Collection<? extends ReceivablePacket<T>>)
	 */
	@Override
	public boolean addAll(Collection<? extends ReceivablePacket<T>> c)
	{
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Method removeAll.
	 * @param c Collection<?>
	 * @return boolean * @see java.util.Collection#removeAll(Collection<?>)
	 */
	@Override
	public boolean removeAll(Collection<?> c)
	{
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Method retainAll.
	 * @param c Collection<?>
	 * @return boolean * @see java.util.Collection#retainAll(Collection<?>)
	 */
	@Override
	public boolean retainAll(Collection<?> c)
	{
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Method clear.
	 * @see java.util.Collection#clear()
	 */
	@Override
	public void clear()
	{
		synchronized (_queue)
		{
			_queue.clear();
		}
	}
	
	/**
	 * Method add.
	 * @param e ReceivablePacket<T>
	 * @return boolean
	 */
	@Override
	public boolean add(ReceivablePacket<T> e)
	{
		synchronized (_queue)
		{
			if (!_queue.add(e))
			{
				return false;
			}
		}
		if (_state.getAndSet(QUEUED) == NONE)
		{
			_executor.execute(this);
		}
		return true;
	}
	
	/**
	 * Method offer.
	 * @param e ReceivablePacket<T>
	 * @return boolean
	 */
	@Override
	public boolean offer(ReceivablePacket<T> e)
	{
		synchronized (_queue)
		{
			return _queue.offer(e);
		}
	}
	
	/**
	 * Method remove.
	 * @return ReceivablePacket<T> * @see java.util.Queue#remove()
	 */
	@Override
	public ReceivablePacket<T> remove()
	{
		synchronized (_queue)
		{
			return _queue.remove();
		}
	}
	
	/**
	 * Method poll.
	 * @return ReceivablePacket<T> * @see java.util.Queue#poll()
	 */
	@Override
	public ReceivablePacket<T> poll()
	{
		synchronized (_queue)
		{
			return _queue.poll();
		}
	}
	
	/**
	 * Method element.
	 * @return ReceivablePacket<T> * @see java.util.Queue#element()
	 */
	@Override
	public ReceivablePacket<T> element()
	{
		synchronized (_queue)
		{
			return _queue.element();
		}
	}
	
	/**
	 * Method peek.
	 * @return ReceivablePacket<T> * @see java.util.Queue#peek()
	 */
	@Override
	public ReceivablePacket<T> peek()
	{
		synchronized (_queue)
		{
			return _queue.peek();
		}
	}
}
