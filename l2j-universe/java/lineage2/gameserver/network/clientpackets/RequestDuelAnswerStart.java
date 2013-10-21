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

import lineage2.gameserver.data.xml.holder.EventHolder;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Request;
import lineage2.gameserver.model.Request.L2RequestType;
import lineage2.gameserver.model.entity.events.EventType;
import lineage2.gameserver.model.entity.events.impl.DuelEvent;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestDuelAnswerStart extends L2GameClientPacket
{
	/**
	 * Field _response.
	 */
	private int _response;
	/**
	 * Field _duelType.
	 */
	private int _duelType;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_duelType = readD();
		readD();
		_response = readD();
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
		if ((request == null) || !request.isTypeOf(L2RequestType.DUEL))
		{
			return;
		}
		if (!request.isInProgress())
		{
			request.cancel();
			activeChar.sendActionFailed();
			return;
		}
		if (activeChar.isActionsDisabled())
		{
			request.cancel();
			activeChar.sendActionFailed();
			return;
		}
		Player requestor = request.getRequestor();
		if (requestor == null)
		{
			request.cancel();
			activeChar.sendPacket(SystemMsg.THAT_PLAYER_IS_NOT_ONLINE);
			activeChar.sendActionFailed();
			return;
		}
		if (requestor.getRequest() != request)
		{
			request.cancel();
			activeChar.sendActionFailed();
			return;
		}
		if (_duelType != request.getInteger("duelType"))
		{
			request.cancel();
			activeChar.sendActionFailed();
			return;
		}
		DuelEvent duelEvent = EventHolder.getInstance().getEvent(EventType.PVP_EVENT, _duelType);
		switch (_response)
		{
			case 0:
				request.cancel();
				if (_duelType == 1)
				{
					requestor.sendPacket(SystemMsg.THE_OPPOSING_PARTY_HAS_DECLINED_YOUR_CHALLENGE_TO_A_DUEL);
				}
				else
				{
					requestor.sendPacket(new SystemMessage2(SystemMsg.C1_HAS_DECLINED_YOUR_CHALLENGE_TO_A_PARTY_DUEL).addName(activeChar));
				}
				break;
			case -1:
				request.cancel();
				requestor.sendPacket(new SystemMessage2(SystemMsg.C1_IS_SET_TO_REFUSE_DUEL_REQUESTS_AND_CANNOT_RECEIVE_A_DUEL_REQUEST).addName(activeChar));
				break;
			case 1:
				if (!duelEvent.canDuel(requestor, activeChar, false))
				{
					request.cancel();
					return;
				}
				SystemMessage2 msg1,
				msg2;
				if (_duelType == 1)
				{
					msg1 = new SystemMessage2(SystemMsg.YOU_HAVE_ACCEPTED_C1S_CHALLENGE_TO_A_PARTY_DUEL);
					msg2 = new SystemMessage2(SystemMsg.S1_HAS_ACCEPTED_YOUR_CHALLENGE_TO_DUEL_AGAINST_THEIR_PARTY);
				}
				else
				{
					msg1 = new SystemMessage2(SystemMsg.YOU_HAVE_ACCEPTED_C1S_CHALLENGE_A_DUEL);
					msg2 = new SystemMessage2(SystemMsg.C1_HAS_ACCEPTED_YOUR_CHALLENGE_TO_A_DUEL);
				}
				activeChar.sendPacket(msg1.addName(requestor));
				requestor.sendPacket(msg2.addName(activeChar));
				try
				{
					duelEvent.createDuel(requestor, activeChar);
				}
				finally
				{
					request.done();
				}
				break;
		}
	}
}
