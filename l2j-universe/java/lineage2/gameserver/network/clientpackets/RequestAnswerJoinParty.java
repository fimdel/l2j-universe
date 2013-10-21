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

import lineage2.gameserver.model.Party;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Request;
import lineage2.gameserver.model.Request.L2RequestType;
import lineage2.gameserver.network.serverpackets.ActionFail;
import lineage2.gameserver.network.serverpackets.JoinParty;
import lineage2.gameserver.network.serverpackets.components.IStaticPacket;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestAnswerJoinParty extends L2GameClientPacket
{
	/**
	 * Field _response.
	 */
	private int _response;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		if (_buf.hasRemaining())
		{
			_response = readD();
		}
		else
		{
			_response = 0;
		}
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
		if ((request == null) || !request.isTypeOf(L2RequestType.PARTY))
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
		if (_response <= 0)
		{
			request.cancel();
			requestor.sendPacket(JoinParty.FAIL);
			return;
		}
		if (activeChar.isInOlympiadMode())
		{
			request.cancel();
			activeChar.sendPacket(SystemMsg.A_PARTY_CANNOT_BE_FORMED_IN_THIS_AREA);
			requestor.sendPacket(JoinParty.FAIL);
			return;
		}
		if (requestor.isInOlympiadMode())
		{
			request.cancel();
			requestor.sendPacket(JoinParty.FAIL);
			return;
		}
		Party party = requestor.getParty();
		if ((party != null) && (party.getMemberCount() >= Party.MAX_SIZE))
		{
			request.cancel();
			activeChar.sendPacket(SystemMsg.THE_PARTY_IS_FULL);
			requestor.sendPacket(SystemMsg.THE_PARTY_IS_FULL);
			requestor.sendPacket(JoinParty.FAIL);
			return;
		}
		IStaticPacket problem = activeChar.canJoinParty(requestor);
		if (problem != null)
		{
			request.cancel();
			activeChar.sendPacket(problem, ActionFail.STATIC);
			requestor.sendPacket(JoinParty.FAIL);
			return;
		}
		if (party == null)
		{
			int itemDistribution = request.getInteger("itemDistribution");
			requestor.setParty(party = new Party(requestor, itemDistribution));
		}
		try
		{
			activeChar.joinParty(party);
			requestor.sendPacket(JoinParty.SUCCESS);
		}
		finally
		{
			request.done();
		}
	}
}
