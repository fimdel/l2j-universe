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
package lineage2.commons.threading;

import java.util.Queue;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public abstract class FIFORunnableQueue<T extends Runnable> implements Runnable
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
	 * Field _state.
	 */
	private int _state = NONE;
	/**
	 * Field _queue.
	 */
	private final Queue<T> _queue;
	
	/**
	 * Constructor for FIFORunnableQueue.
	 * @param queue Queue<T>
	 */
	public FIFORunnableQueue(Queue<T> queue)
	{
		_queue = queue;
	}
	
	/**
	 * Method execute.
	 * @param t T
	 */
	public void execute(T t)
	{
		_queue.add(t);
		synchronized (this)
		{
			if (_state != NONE)
			{
				return;
			}
			_state = QUEUED;
		}
		execute();
	}
	
	/**
	 * Method execute.
	 */
	protected abstract void execute();
	
	/**
	 * Method clear.
	 */
	public void clear()
	{
		_queue.clear();
	}
	
	/**
	 * Method run.
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run()
	{
		synchronized (this)
		{
			if (_state == RUNNING)
			{
				return;
			}
			_state = RUNNING;
		}
		try
		{
			for (;;)
			{
				final Runnable t = _queue.poll();
				if (t == null)
				{
					break;
				}
				t.run();
			}
		}
		finally
		{
			synchronized (this)
			{
				_state = NONE;
			}
		}
	}
}
