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

import java.util.concurrent.Future;

import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.threading.SteppingRunnableQueueManager;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.model.Creature;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class DecayTaskManager extends SteppingRunnableQueueManager
{
	/**
	 * Field _instance.
	 */
	private static final DecayTaskManager _instance = new DecayTaskManager();
	
	/**
	 * Method getInstance.
	 * @return DecayTaskManager
	 */
	public static final DecayTaskManager getInstance()
	{
		return _instance;
	}
	
	/**
	 * Constructor for DecayTaskManager.
	 */
	private DecayTaskManager()
	{
		super(500L);
		ThreadPoolManager.getInstance().scheduleAtFixedRate(this, 500L, 500L);
		ThreadPoolManager.getInstance().scheduleAtFixedRate(new RunnableImpl()
		{
			@Override
			public void runImpl()
			{
				DecayTaskManager.this.purge();
			}
		}, 60000L, 60000L);
	}
	
	/**
	 * Method addDecayTask.
	 * @param actor Creature
	 * @param delay long
	 * @return Future<?>
	 */
	public Future<?> addDecayTask(final Creature actor, long delay)
	{
		return schedule(new RunnableImpl()
		{
			@Override
			public void runImpl()
			{
				actor.doDecay();
			}
		}, delay);
	}
}
