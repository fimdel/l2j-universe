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
package zones;

import java.util.List;
import java.util.concurrent.ScheduledFuture;

import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.network.serverpackets.ExNotifyFlyMoveStart;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.ReflectionUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class JumpZone implements ScriptFile
{
	/**
	 * Field zoneTask.
	 */
	private static ScheduledFuture<?> zoneTask;
	/**
	 * Field jumpZones.
	 */
	static List<Zone> jumpZones;
	
	/**
	 * Method onLoad.
	 * @see lineage2.gameserver.scripts.ScriptFile#onLoad()
	 */
	@Override
	public void onLoad()
	{
		jumpZones = ReflectionUtils.getZonesByType(Zone.ZoneType.JUMPING);
		zoneTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new ZoneTask(), 1000L, 1000L);
	}
	
	/**
	 * Method onReload.
	 * @see lineage2.gameserver.scripts.ScriptFile#onReload()
	 */
	@Override
	public void onReload()
	{
		zoneTask.cancel(true);
		jumpZones.clear();
	}
	
	/**
	 * Method onShutdown.
	 * @see lineage2.gameserver.scripts.ScriptFile#onShutdown()
	 */
	@Override
	public void onShutdown()
	{
	}
	
	/**
	 * @author Mobius
	 */
	private class ZoneTask implements Runnable
	{
		/**
		 * Constructor for ZoneTask.
		 */
		public ZoneTask()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method run.
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run()
		{
			for (Zone zone : jumpZones)
			{
				for (Player player : zone.getInsidePlayers())
				{
					if (player.isAwaking())
					{
						player.sendPacket(ExNotifyFlyMoveStart.STATIC);
					}
				}
			}
		}
	}
}
