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
import lineage2.commons.util.Rnd;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.model.Player;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AutoSaveManager extends SteppingRunnableQueueManager
{
	/**
	 * Field _instance.
	 */
	private static final AutoSaveManager _instance = new AutoSaveManager();
	
	/**
	 * Method getInstance.
	 * @return AutoSaveManager
	 */
	public static final AutoSaveManager getInstance()
	{
		return _instance;
	}
	
	/**
	 * Constructor for AutoSaveManager.
	 */
	private AutoSaveManager()
	{
		super(10000L);
		ThreadPoolManager.getInstance().scheduleAtFixedRate(this, 10000L, 10000L);
		ThreadPoolManager.getInstance().scheduleAtFixedRate(new RunnableImpl()
		{
			@Override
			public void runImpl()
			{
				AutoSaveManager.this.purge();
			}
		}, 60000L, 60000L);
	}
	
	/**
	 * Method addAutoSaveTask.
	 * @param player Player
	 * @return Future<?>
	 */
	public Future<?> addAutoSaveTask(final Player player)
	{
		long delay = Rnd.get(180, 360) * 1000L;
		return scheduleAtFixedRate(new RunnableImpl()
		{
			@Override
			public void runImpl()
			{
				if (!player.isOnline())
				{
					return;
				}
				player.store(true);
			}
		}, delay, delay);
	}
}
