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

import java.util.List;

import lineage2.commons.math.SafeMath;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Request;
import lineage2.gameserver.model.Request.L2RequestType;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.model.items.TradeItem;
import lineage2.gameserver.network.serverpackets.SendTradeDone;
import lineage2.gameserver.network.serverpackets.TradeOtherAdd;
import lineage2.gameserver.network.serverpackets.TradeOwnAdd;
import lineage2.gameserver.network.serverpackets.TradeUpdate;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AddTradeItem extends L2GameClientPacket
{
	/**
	 * Field _tradeId.
	 */
	@SuppressWarnings("unused")
	private int _tradeId;
	/**
	 * Field _objectId.
	 */
	private int _objectId;
	/**
	 * Field _amount.
	 */
	private long _amount;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_tradeId = readD();
		_objectId = readD();
		_amount = readQ();
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		Player parthner1 = getClient().getActiveChar();
		if ((parthner1 == null) || (_amount < 1))
		{
			return;
		}
		Request request = parthner1.getRequest();
		if ((request == null) || !request.isTypeOf(L2RequestType.TRADE))
		{
			parthner1.sendActionFailed();
			return;
		}
		if (!request.isInProgress())
		{
			request.cancel();
			parthner1.sendPacket(SendTradeDone.FAIL);
			parthner1.sendActionFailed();
			return;
		}
		if (parthner1.isOutOfControl())
		{
			request.cancel();
			parthner1.sendPacket(SendTradeDone.FAIL);
			parthner1.sendActionFailed();
			return;
		}
		Player parthner2 = request.getOtherPlayer(parthner1);
		if (parthner2 == null)
		{
			request.cancel();
			parthner1.sendPacket(SendTradeDone.FAIL);
			parthner1.sendPacket(Msg.THAT_PLAYER_IS_NOT_ONLINE);
			parthner1.sendActionFailed();
			return;
		}
		if (parthner2.getRequest() != request)
		{
			request.cancel();
			parthner1.sendPacket(SendTradeDone.FAIL);
			parthner1.sendActionFailed();
			return;
		}
		if (request.isConfirmed(parthner1) || request.isConfirmed(parthner2))
		{
			parthner1.sendPacket(SystemMsg.YOU_MAY_NO_LONGER_ADJUST_ITEMS_IN_THE_TRADE_BECAUSE_THE_TRADE_HAS_BEEN_CONFIRMED);
			parthner1.sendActionFailed();
			return;
		}
		ItemInstance item = parthner1.getInventory().getItemByObjectId(_objectId);
		if ((item == null) || !item.canBeTraded(parthner1))
		{
			parthner1.sendPacket(SystemMsg.THIS_ITEM_CANNOT_BE_TRADED_OR_SOLD);
			return;
		}
		long count = Math.min(_amount, item.getCount());
		List<TradeItem> tradeList = parthner1.getTradeList();
		TradeItem tradeItem = null;
		try
		{
			for (TradeItem ti : parthner1.getTradeList())
			{
				if (ti.getObjectId() == _objectId)
				{
					count = SafeMath.addAndCheck(count, ti.getCount());
					count = Math.min(count, item.getCount());
					ti.setCount(count);
					tradeItem = ti;
					break;
				}
			}
		}
		catch (ArithmeticException ae)
		{
			parthner1.sendPacket(SystemMsg.INCORRECT_ITEM_COUNT);
			return;
		}
		if (tradeItem == null)
		{
			tradeItem = new TradeItem(item);
			tradeItem.setCount(count);
			tradeList.add(tradeItem);
		}
		parthner1.sendPacket(new TradeOwnAdd(tradeItem, tradeItem.getCount()), new TradeUpdate(tradeItem, item.getCount() - tradeItem.getCount()));
		parthner2.sendPacket(new TradeOtherAdd(tradeItem, tradeItem.getCount()));
	}
}
