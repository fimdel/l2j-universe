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
package lineage2.gameserver.taskmanager;

import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.threading.SteppingRunnableQueueManager;
import lineage2.gameserver.ThreadPoolManager;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RegenTaskManager extends SteppingRunnableQueueManager
{
	/**
	 * Field _instance.
	 */
	private static final RegenTaskManager _instance = new RegenTaskManager();
	
	/**
	 * Method getInstance.
	 * @return RegenTaskManager
	 */
	public static final RegenTaskManager getInstance()
	{
		return _instance;
	}
	
	/**
	 * Constructor for RegenTaskManager.
	 */
	private RegenTaskManager()
	{
		super(1000L);
		ThreadPoolManager.getInstance().scheduleAtFixedRate(this, 1000L, 1000L);
		ThreadPoolManager.getInstance().scheduleAtFixedRate(new RunnableImpl()
		{
			@Override
			public void runImpl()
			{
				RegenTaskManager.this.purge();
			}
		}, 10000L, 10000L);
	}
}
