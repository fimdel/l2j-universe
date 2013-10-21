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
package npc.model;

import lineage2.gameserver.listener.zone.OnZoneEnterLeaveListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.EventTrigger;
import lineage2.gameserver.network.serverpackets.ExStartScenePlayer;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.ReflectionUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class OddGlobeInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field instancedZoneId. (value is 151)
	 */
	private static final int instancedZoneId = 151;
	
	/**
	 * Constructor for OddGlobeInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public OddGlobeInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	/**
	 * Method onBypassFeedback.
	 * @param player Player
	 * @param command String
	 */
	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if (!canBypassCheck(player, this))
		{
			return;
		}
		if (command.equalsIgnoreCase("monastery_enter"))
		{
			Reflection r = player.getActiveReflection();
			if (r != null)
			{
				if (player.canReenterInstance(instancedZoneId))
				{
					player.teleToLocation(r.getTeleportLoc(), r);
				}
			}
			else if (player.canEnterInstance(instancedZoneId))
			{
				Reflection newfew = ReflectionUtils.enterReflection(player, instancedZoneId);
				ZoneListener zoneL = new ZoneListener();
				newfew.getZone("[ssq_holy_burial_ground]").addListener(zoneL);
				ZoneListener2 zoneL2 = new ZoneListener2();
				newfew.getZone("[ssq_holy_seal]").addListener(zoneL2);
			}
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
	
	/**
	 * @author Mobius
	 */
	public class ZoneListener implements OnZoneEnterLeaveListener
	{
		/**
		 * Field done.
		 */
		private boolean done = false;
		
		/**
		 * Method onZoneEnter.
		 * @param zone Zone
		 * @param cha Creature
		 * @see lineage2.gameserver.listener.zone.OnZoneEnterLeaveListener#onZoneEnter(Zone, Creature)
		 */
		@Override
		public void onZoneEnter(Zone zone, Creature cha)
		{
			Player player = cha.getPlayer();
			if ((player == null) || !cha.isPlayer() || done)
			{
				return;
			}
			done = true;
			player.showQuestMovie(ExStartScenePlayer.SCENE_SSQ2_HOLY_BURIAL_GROUND_OPENING);
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
	
	/**
	 * @author Mobius
	 */
	public class ZoneListener2 implements OnZoneEnterLeaveListener
	{
		/**
		 * Field done.
		 */
		private boolean done = false;
		
		/**
		 * Method onZoneEnter.
		 * @param zone Zone
		 * @param cha Creature
		 * @see lineage2.gameserver.listener.zone.OnZoneEnterLeaveListener#onZoneEnter(Zone, Creature)
		 */
		@Override
		public void onZoneEnter(Zone zone, Creature cha)
		{
			Player player = cha.getPlayer();
			if ((player == null) || !cha.isPlayer())
			{
				return;
			}
			player.broadcastPacket(new EventTrigger(21100100, true));
			if (!done)
			{
				done = true;
				player.showQuestMovie(ExStartScenePlayer.SCENE_SSQ2_SOLINA_TOMB_OPENING);
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
