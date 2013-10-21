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
package lineage2.gameserver.handler.usercommands.impl;

import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.handler.usercommands.IUserCommandHandler;
import lineage2.gameserver.model.Party;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.CustomMessage;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class PartyInfo implements IUserCommandHandler
{
	/**
	 * Field COMMAND_IDS.
	 */
	private static final int[] COMMAND_IDS =
	{
		81
	};
	
	/**
	 * Method useUserCommand.
	 * @param id int
	 * @param activeChar Player
	 * @return boolean * @see lineage2.gameserver.handler.usercommands.IUserCommandHandler#useUserCommand(int, Player)
	 */
	@Override
	public boolean useUserCommand(int id, Player activeChar)
	{
		if (id != COMMAND_IDS[0])
		{
			return false;
		}
		Party playerParty = activeChar.getParty();
		if (!activeChar.isInParty())
		{
			return false;
		}
		Player partyLeader = playerParty.getPartyLeader();
		if (partyLeader == null)
		{
			return false;
		}
		int memberCount = playerParty.getMemberCount();
		int lootDistribution = playerParty.getLootDistribution();
		activeChar.sendPacket(Msg._PARTY_INFORMATION_);
		switch (lootDistribution)
		{
			case Party.ITEM_LOOTER:
				activeChar.sendPacket(Msg.LOOTING_METHOD_FINDERS_KEEPERS);
				break;
			case Party.ITEM_ORDER:
				activeChar.sendPacket(Msg.LOOTING_METHOD_BY_TURN);
				break;
			case Party.ITEM_ORDER_SPOIL:
				activeChar.sendPacket(Msg.LOOTING_METHOD_BY_TURN_INCLUDING_SPOIL);
				break;
			case Party.ITEM_RANDOM:
				activeChar.sendPacket(Msg.LOOTING_METHOD_RANDOM);
				break;
			case Party.ITEM_RANDOM_SPOIL:
				activeChar.sendPacket(Msg.LOOTING_METHOD_RANDOM_INCLUDING_SPOIL);
				break;
		}
		activeChar.sendPacket(new SystemMessage(SystemMessage.PARTY_LEADER_S1).addString(partyLeader.getName()));
		activeChar.sendMessage(new CustomMessage("scripts.commands.user.PartyInfo.Members", activeChar).addNumber(memberCount));
		activeChar.sendPacket(Msg.__DASHES__);
		return true;
	}
	
	/**
	 * Method getUserCommandList.
	 * @return int[] * @see lineage2.gameserver.handler.usercommands.IUserCommandHandler#getUserCommandList()
	 */
	@Override
	public final int[] getUserCommandList()
	{
		return COMMAND_IDS;
	}
}
