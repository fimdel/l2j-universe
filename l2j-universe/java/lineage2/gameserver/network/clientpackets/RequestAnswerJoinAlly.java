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
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Request;
import lineage2.gameserver.model.Request.L2RequestType;
import lineage2.gameserver.model.pledge.Alliance;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestAnswerJoinAlly extends L2GameClientPacket
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
		_response = _buf.remaining() >= 4 ? readD() : 0;
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
		if ((request == null) || !request.isTypeOf(L2RequestType.ALLY))
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
		if (requestor.getAlliance() == null)
		{
			request.cancel();
			activeChar.sendActionFailed();
			return;
		}
		if (_response == 0)
		{
			request.cancel();
			requestor.sendPacket(Msg.YOU_HAVE_FAILED_TO_INVITE_A_CLAN_INTO_THE_ALLIANCE);
			return;
		}
		try
		{
			Alliance ally = requestor.getAlliance();
			activeChar.sendPacket(Msg.YOU_HAVE_ACCEPTED_THE_ALLIANCE);
			activeChar.getClan().setAllyId(requestor.getAllyId());
			activeChar.getClan().updateClanInDB();
			ally.addAllyMember(activeChar.getClan(), true);
			ally.broadcastAllyStatus();
		}
		finally
		{
			request.done();
		}
	}
}
