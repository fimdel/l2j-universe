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
package handler.usercommands;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.residence.Castle;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class SiegeStatus extends ScriptUserCommand
{
	/**
	 * Field COMMANDS.
	 */
	public static final int[] COMMANDS =
	{
		99
	};
	
	/**
	 * Method useUserCommand.
	 * @param id int
	 * @param player Player
	 * @return boolean * @see lineage2.gameserver.handler.usercommands.IUserCommandHandler#useUserCommand(int, Player)
	 */
	@Override
	public boolean useUserCommand(int id, Player player)
	{
		if (!player.isClanLeader())
		{
			player.sendPacket(SystemMsg.ONLY_THE_CLAN_LEADER_MAY_ISSUE_COMMANDS);
			return false;
		}
		Castle castle = player.getCastle();
		if (castle == null)
		{
			return false;
		}
		if (castle.getSiegeEvent().isInProgress())
		{
			if (!player.isNoble())
			{
				player.sendPacket(SystemMsg.ONLY_A_CLAN_LEADER_THAT_IS_A_NOBLESSE_CAN_VIEW_THE_SIEGE_WAR_STATUS_WINDOW_DURING_A_SIEGE_WAR);
				return false;
			}
		}
		NpcHtmlMessage msg = new NpcHtmlMessage(5);
		msg.setFile("siege_status.htm");
		msg.replace("%name%", player.getName());
		msg.replace("%kills%", String.valueOf(0));
		msg.replace("%deaths%", String.valueOf(0));
		msg.replace("%type%", String.valueOf(0));
		player.sendPacket(msg);
		return true;
	}
	
	/**
	 * Method getUserCommandList.
	 * @return int[] * @see lineage2.gameserver.handler.usercommands.IUserCommandHandler#getUserCommandList()
	 */
	@Override
	public int[] getUserCommandList()
	{
		return COMMANDS;
	}
}
