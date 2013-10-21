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

import quests._10301_ShadowOfTerrorBlackishRedFog;
import lineage2.gameserver.instancemanager.QuestManager;
import lineage2.gameserver.listener.zone.OnZoneEnterLeaveListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.components.SceneMovie;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.ReflectionUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AngelWaterfall implements ScriptFile
{
	private static ZoneListener _zoneListener;
	private static String[] zones = {"[25_20_telzone_to_magmeld]"};
	
	private void init()
	{
		_zoneListener = new ZoneListener();
		for (String s : zones)
		{
			Zone zone = ReflectionUtils.getZone(s);
			zone.addListener(_zoneListener);
		}
	}
	
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
			if (zone == null)
			{
				return;
			}
			if (cha == null)
			{
				return;
			}
			if (!cha.isPlayer())
			{
				return;
			}
			Player player = cha.getPlayer();
			
			QuestState qs = player.getQuestState(_10301_ShadowOfTerrorBlackishRedFog.class);
			if(qs != null && qs.getCond() == 3 && player.getVar("instance10301") == null)
			{
				Quest q = QuestManager.getQuest(10301);
				player.processQuestEvent(q.getName(), "enterInstance", null);
				//player.setVar("instance10301", "true", -1);
				return;
			}
			if (!player.getVarB("@25_20_telzone_to_magmeld"))
			{
				player.showQuestMovie(SceneMovie.si_arkan_enter);
				player.setVar("@25_20_telzone_to_magmeld", "true", -1);
			}
			cha.teleToLocation(Location.parseLoc(zone.getParams().getString("tele")));
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
