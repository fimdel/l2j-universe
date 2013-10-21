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

import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.instancemanager.naia.NaiaTowerManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.MagicSkillUse;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.templates.npc.NpcTemplate;
import ai.hellbound.NaiaLock;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class NaiaControllerInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor for NaiaControllerInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public NaiaControllerInstance(int objectId, NpcTemplate template)
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
		if (command.startsWith("tryenter"))
		{
			if (NaiaLock.isEntranceActive())
			{
				if (!player.isInParty())
				{
					player.sendPacket(Msg.YOU_ARE_NOT_CURRENTLY_IN_A_PARTY_SO_YOU_CANNOT_ENTER);
					return;
				}
				if (!player.getParty().isLeader(player))
				{
					player.sendPacket(Msg.ONLY_A_PARTY_LEADER_CAN_TRY_TO_ENTER);
					return;
				}
				for (Player member : player.getParty().getPartyMembers())
				{
					if (member.getLevel() < 80)
					{
						player.sendPacket(new SystemMessage(SystemMessage.C1S_LEVEL_REQUIREMENT_IS_NOT_SUFFICIENT_AND_CANNOT_BE_ENTERED).addName(member));
						return;
					}
					if (!member.isInRange(this, 500))
					{
						player.sendPacket(new SystemMessage(SystemMessage.C1_IS_IN_A_LOCATION_WHICH_CANNOT_BE_ENTERED_THEREFORE_IT_CANNOT_BE_PROCESSED).addName(member));
						return;
					}
				}
				NaiaTowerManager.startNaiaTower(player);
				broadcastPacket(new MagicSkillUse(this, this, 5527, 1, 0, 0));
				doDie(null);
			}
			else
			{
				broadcastPacket(new MagicSkillUse(this, this, 5527, 1, 0, 0));
				doDie(null);
			}
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
}
