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

import java.util.List;
import java.util.StringTokenizer;

import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class MoonlightTombstoneInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field KEY_ID. (value is 9714)
	 */
	private static final int KEY_ID = 9714;
	/**
	 * Field COLLAPSE_TIME. (value is 5)
	 */
	private final static long COLLAPSE_TIME = 5;
	/**
	 * Field _activated.
	 */
	private boolean _activated = false;
	
	/**
	 * Constructor for MoonlightTombstoneInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public MoonlightTombstoneInstance(int objectId, NpcTemplate template)
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
		StringTokenizer st = new StringTokenizer(command);
		if (st.nextToken().equals("insertKey"))
		{
			if (player.getParty() == null)
			{
				player.sendPacket(Msg.YOU_ARE_NOT_CURRENTLY_IN_A_PARTY_SO_YOU_CANNOT_ENTER);
				return;
			}
			if (!player.getParty().isLeader(player))
			{
				player.sendPacket(Msg.ONLY_A_PARTY_LEADER_CAN_TRY_TO_ENTER);
				return;
			}
			List<Player> partyMembers = player.getParty().getPartyMembers();
			for (Player partyMember : partyMembers)
			{
				if (!isInRange(partyMember, INTERACTION_DISTANCE * 2))
				{
					Functions.show("default/32343-3.htm", player, this);
					return;
				}
			}
			if (_activated)
			{
				Functions.show("default/32343-1.htm", player, this);
				return;
			}
			if (Functions.getItemCount(player, KEY_ID) > 0)
			{
				Functions.removeItem(player, KEY_ID, 1);
				player.getReflection().startCollapseTimer(COLLAPSE_TIME * 60 * 1000L);
				_activated = true;
				broadcastPacketToOthers(new SystemMessage(SystemMessage.THIS_DUNGEON_WILL_EXPIRE_IN_S1_MINUTES).addNumber(COLLAPSE_TIME));
				player.getReflection().setCoreLoc(player.getReflection().getReturnLoc());
				player.getReflection().setReturnLoc(new Location(16280, 283448, -9704));
				Functions.show("default/32343-1.htm", player, this);
				return;
			}
			Functions.show("default/32343-2.htm", player, this);
			return;
		}
		super.onBypassFeedback(player, command);
	}
}
