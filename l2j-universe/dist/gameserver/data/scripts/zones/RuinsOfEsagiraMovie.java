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

import lineage2.gameserver.listener.zone.OnZoneEnterLeaveListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.network.serverpackets.components.SceneMovie;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.ReflectionUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RuinsOfEsagiraMovie implements ScriptFile
{
	/**
	 * Field ZONE_NAME. (value is ""[roe_presentation_video]"")
	 */
	private static final String ZONE_NAME = "[roe_presentation_video]";
	/**
	 * Field _zoneListener.
	 */
	private static ZoneListener _zoneListener;
	
	/**
	 * Method onLoad.
	 * @see lineage2.gameserver.scripts.ScriptFile#onLoad()
	 */
	@Override
	public void onLoad()
	{
		init();
	}
	
	/**
	 * Method onReload.
	 * @see lineage2.gameserver.scripts.ScriptFile#onReload()
	 */
	@Override
	public void onReload()
	{
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
	 * Method init.
	 */
	private void init()
	{
		_zoneListener = new ZoneListener();
		Zone zone = ReflectionUtils.getZone(ZONE_NAME);
		if (zone != null)
		{
			zone.addListener(_zoneListener);
		}
	}
	
	/**
	 * @author Mobius
	 */
	public class ZoneListener implements OnZoneEnterLeaveListener
	{
		/**
		 * Method onZoneEnter.
		 * @param zone Zone
		 * @param cha Creature
		 * @see lineage2.gameserver.listener.zone.OnZoneEnterLeaveListener#onZoneEnter(Zone, Creature)
		 */
		@Override
		public void onZoneEnter(Zone zone, Creature cha)
		{
			if (cha.isPlayer())
			{
				Player player = cha.getPlayer();
				if (!player.getVarB("@roe_present_video"))
				{
					player.showQuestMovie(SceneMovie.si_illusion_03_que);
					player.setVar("@roe_present_video", "true", -1);
				}
			}
		}
		
		/**
		 * Method onZoneLeave.
		 * @param zone Zone
		 * @param cha Creature
		 * @see lineage2.gameserver.listener.zone.OnZoneEnterLeaveListener#onZoneLeave(Zone, Creature)
		 */
		@Override
		public void onZoneLeave(Zone zone, Creature cha)
		{
		}
	}
}
