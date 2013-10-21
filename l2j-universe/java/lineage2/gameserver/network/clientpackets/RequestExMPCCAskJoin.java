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
package lineage2.gameserver.network.clientpackets;

import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.model.CommandChannel;
import lineage2.gameserver.model.Party;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Request;
import lineage2.gameserver.model.Request.L2RequestType;
import lineage2.gameserver.model.World;
import lineage2.gameserver.network.serverpackets.ExAskJoinMPCC;
import lineage2.gameserver.network.serverpackets.SystemMessage;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestExMPCCAskJoin extends L2GameClientPacket
{
	/**
	 * Field _name.
	 */
	private String _name;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_name = readS(16);
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if (activeChar == null)
		{
			return;
		}
		if (activeChar.isOutOfControl())
		{
			activeChar.sendActionFailed();
			return;
		}
		if (activeChar.isProcessingRequest())
		{
			activeChar.sendPacket(Msg.WAITING_FOR_ANOTHER_REPLY);
			return;
		}
		if (!activeChar.isInParty())
		{
			activeChar.sendPacket(Msg.YOU_DO_NOT_HAVE_AUTHORITY_TO_INVITE_SOMEONE_TO_THE_COMMAND_CHANNEL);
			return;
		}
		Player target = World.getPlayer(_name);
		if (target == null)
		{
			activeChar.sendPacket(Msg.THAT_PLAYER_IS_NOT_CURRENTLY_ONLINE);
			return;
		}
		if ((activeChar == target) || !target.isInParty() || (activeChar.getParty() == target.getParty()))
		{
			activeChar.sendPacket(Msg.YOU_HAVE_INVITED_WRONG_TARGET);
			return;
		}
		if (target.isInParty() && !target.getParty().isLeader(target))
		{
			target = target.getParty().getPartyLeader();
		}
		if (target == null)
		{
			activeChar.sendPacket(Msg.THAT_PLAYER_IS_NOT_CURRENTLY_ONLINE);
			return;
		}
		if (target.getParty().isInCommandChannel())
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.S1_PARTY_IS_ALREADY_A_MEMBER_OF_THE_COMMAND_CHANNEL).addString(target.getName()));
			return;
		}
		if (target.isBusy())
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.S1_IS_BUSY_PLEASE_TRY_AGAIN_LATER).addString(target.getName()));
			return;
		}
		Party activeParty = activeChar.getParty();
		if (activeParty.isInCommandChannel())
		{
			if (activeParty.getCommandChannel().getChannelLeader() != activeChar)
			{
				activeChar.sendPacket(Msg.YOU_DO_NOT_HAVE_AUTHORITY_TO_INVITE_SOMEONE_TO_THE_COMMAND_CHANNEL);
				return;
			}
			sendInvite(activeChar, target);
		}
		else if (CommandChannel.checkAuthority(activeChar))
		{
			sendInvite(activeChar, target);
		}
	}
	
	/**
	 * Method sendInvite.
	 * @param requestor Player
	 * @param target Player
	 */
	private void sendInvite(Player requestor, Player target)
	{
		new Request(L2RequestType.CHANNEL, requestor, target).setTimeout(10000L);
		target.sendPacket(new ExAskJoinMPCC(requestor.getName()));
		requestor.sendMessage("You invited " + target.getName() + " to your Command Channel.");
	}
}
