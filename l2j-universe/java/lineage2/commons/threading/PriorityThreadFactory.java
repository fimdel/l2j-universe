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

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class PriorityThreadFactory implements ThreadFactory
{
	/**
	 * Field _prio.
	 */
	private final int _prio;
	/**
	 * Field _name.
	 */
	private final String _name;
	/**
	 * Field _threadNumber.
	 */
	private final AtomicInteger _threadNumber = new AtomicInteger(1);
	/**
	 * Field _group.
	 */
	private final ThreadGroup _group;
	
	/**
	 * Constructor for PriorityThreadFactory.
	 * @param name String
	 * @param prio int
	 */
	public PriorityThreadFactory(String name, int prio)
	{
		_prio = prio;
		_name = name;
		_group = new ThreadGroup(_name);
	}
	
	/**
	 * Method newThread.
	 * @param r Runnable
	 * @return Thread * @see java.util.concurrent.ThreadFactory#newThread(Runnable)
	 */
	@Override
	public Thread newThread(Runnable r)
	{
		Thread t = new Thread(_group, r);
		t.setName(_name + "-" + _threadNumber.getAndIncrement());
		t.setPriority(_prio);
		return t;
	}
	
	/**
	 * Method getGroup.
	 * @return ThreadGroup
	 */
	public ThreadGroup getGroup()
	{
		return _group;
	}
}
