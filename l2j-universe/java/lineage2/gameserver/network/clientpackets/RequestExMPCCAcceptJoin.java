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
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Request;
import lineage2.gameserver.model.Request.L2RequestType;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.SystemMessage2;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestExMPCCAcceptJoin extends L2GameClientPacket
{
	/**
	 * Field _unk. Field _response.
	 */
	@SuppressWarnings("unused")
	private int _response, _unk;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_response = _buf.hasRemaining() ? readD() : 0;
		_unk = _buf.hasRemaining() ? readD() : 0;
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
		Request request = activeChar.getRequest();
		if ((request == null) || !request.isTypeOf(L2RequestType.CHANNEL))
		{
			return;
		}
		if (!request.isInProgress())
		{
			request.cancel();
			activeChar.sendActionFailed();
			return;
		}
		if (activeChar.isOutOfControl())
		{
			request.cancel();
			activeChar.sendActionFailed();
			return;
		}
		Player requestor = request.getRequestor();
		if (requestor == null)
		{
			request.cancel();
			activeChar.sendPacket(Msg.THAT_PLAYER_IS_NOT_ONLINE);
			activeChar.sendActionFailed();
			return;
		}
		if (requestor.getRequest() != request)
		{
			request.cancel();
			activeChar.sendActionFailed();
			return;
		}
		if (_response == 0)
		{
			request.cancel();
			requestor.sendPacket(new SystemMessage(SystemMessage.S1_HAS_DECLINED_THE_CHANNEL_INVITATION).addString(activeChar.getName()));
			return;
		}
		if (!requestor.isInParty() || !activeChar.isInParty() || activeChar.getParty().isInCommandChannel())
		{
			request.cancel();
			requestor.sendPacket(Msg.NO_USER_HAS_BEEN_INVITED_TO_THE_COMMAND_CHANNEL);
			return;
		}
		if (activeChar.isTeleporting())
		{
			request.cancel();
			activeChar.sendPacket(Msg.YOU_CANNOT_JOIN_A_COMMAND_CHANNEL_WHILE_TELEPORTING);
			requestor.sendPacket(Msg.NO_USER_HAS_BEEN_INVITED_TO_THE_COMMAND_CHANNEL);
			return;
		}
		try
		{
			if (requestor.getParty().isInCommandChannel())
			{
				requestor.getParty().getCommandChannel().addParty(activeChar.getParty());
			}
			else if (CommandChannel.checkAuthority(requestor))
			{
				boolean haveSkill = requestor.getSkillLevel(CommandChannel.CLAN_IMPERIUM_ID) > 0;
				boolean haveItem = false;
				if (!haveSkill)
				{
					haveItem = requestor.getInventory().destroyItemByItemId(CommandChannel.STRATEGY_GUIDE_ID, 1);
					if (haveItem)
					{
						requestor.sendPacket(SystemMessage2.removeItems(CommandChannel.STRATEGY_GUIDE_ID, 1));
					}
				}
				if (!haveSkill && !haveItem)
				{
					return;
				}
				CommandChannel channel = new CommandChannel(requestor);
				requestor.sendPacket(Msg.THE_COMMAND_CHANNEL_HAS_BEEN_FORMED);
				channel.addParty(activeChar.getParty());
			}
		}
		finally
		{
			request.done();
		}
	}
}
