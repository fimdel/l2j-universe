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
import lineage2.commons.util.Rnd;
import lineage2.gameserver.Config;
import lineage2.gameserver.ThreadPoolManager;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class EffectTaskManager extends SteppingRunnableQueueManager
{
	/**
	 * Field TICK. (value is 250)
	 */
	private final static long TICK = 250L;
	/**
	 * Field _instances.
	 */
	private final static EffectTaskManager[] _instances = new EffectTaskManager[Config.EFFECT_TASK_MANAGER_COUNT];
	static
	{
		for (int i = 0; i < _instances.length; i++)
		{
			_instances[i] = new EffectTaskManager();
		}
	}
	/**
	 * Field randomizer.
	 */
	private static int randomizer = 0;
	
	/**
	 * Method getInstance.
	 * @return EffectTaskManager
	 */
	public final static EffectTaskManager getInstance()
	{
		return _instances[randomizer++ & (_instances.length - 1)];
	}
	
	/**
	 * Constructor for EffectTaskManager.
	 */
	private EffectTaskManager()
	{
		super(TICK);
		ThreadPoolManager.getInstance().scheduleAtFixedRate(this, Rnd.get(TICK), TICK);
		ThreadPoolManager.getInstance().scheduleAtFixedRate(new RunnableImpl()
		{
			@Override
			public void runImpl()
			{
				EffectTaskManager.this.purge();
			}
		}, 30000L, 30000L);
	}
	
	/**
	 * Method getStats.
	 * @param num int
	 * @return CharSequence
	 */
	public CharSequence getStats(int num)
	{
		return _instances[num].getStats();
	}
}
