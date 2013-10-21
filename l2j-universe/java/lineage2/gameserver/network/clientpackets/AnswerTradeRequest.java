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

import java.util.concurrent.CopyOnWriteArrayList;

import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Request;
import lineage2.gameserver.model.Request.L2RequestType;
import lineage2.gameserver.model.items.TradeItem;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.network.serverpackets.TradeStart;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AnswerTradeRequest extends L2GameClientPacket
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
		if ((request == null) || !request.isTypeOf(L2RequestType.TRADE_REQUEST))
		{
			activeChar.sendActionFailed();
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
		if (_response == 0)
		{
			request.cancel();
			requestor.sendPacket(new SystemMessage2(SystemMsg.C1_HAS_DENIED_YOUR_REQUEST_TO_TRADE).addString(activeChar.getName()));
			return;
		}
		if (!activeChar.isInRangeZ(requestor, Creature.INTERACTION_DISTANCE))
		{
			request.cancel();
			activeChar.sendPacket(SystemMsg.YOUR_TARGET_IS_OUT_OF_RANGE);
			return;
		}
		if (requestor.isActionsDisabled())
		{
			request.cancel();
			activeChar.sendPacket(new SystemMessage2(SystemMsg.C1_IS_ON_ANOTHER_TASK).addString(requestor.getName()));
			activeChar.sendActionFailed();
			return;
		}
		try
		{
			new Request(L2RequestType.TRADE, activeChar, requestor);
			requestor.setTradeList(new CopyOnWriteArrayList<TradeItem>());
			requestor.sendPacket(new SystemMessage2(SystemMsg.YOU_BEGIN_TRADING_WITH_C1).addString(activeChar.getName()), new TradeStart(requestor, activeChar));
			activeChar.setTradeList(new CopyOnWriteArrayList<TradeItem>());
			activeChar.sendPacket(new SystemMessage2(SystemMsg.YOU_BEGIN_TRADING_WITH_C1).addString(requestor.getName()), new TradeStart(activeChar, requestor));
		}
		finally
		{
			request.done();
		}
	}
}
