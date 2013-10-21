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
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Request;
import lineage2.gameserver.model.Request.L2RequestType;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.model.items.TradeItem;
import lineage2.gameserver.network.serverpackets.SendTradeDone;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.TradePressOtherOk;
import lineage2.gameserver.utils.Log;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class TradeDone extends L2GameClientPacket
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
		Player parthner1 = getClient().getActiveChar();
		if (parthner1 == null)
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
		if (_response == 0)
		{
			request.cancel();
			parthner1.sendPacket(SendTradeDone.FAIL);
			parthner2.sendPacket(SendTradeDone.FAIL, new SystemMessage(SystemMessage.C1_HAS_CANCELLED_THE_TRADE).addString(parthner1.getName()));
			return;
		}
		if (!parthner1.isInRangeZ(parthner2, Creature.INTERACTION_DISTANCE))
		{
			parthner1.sendPacket(Msg.YOUR_TARGET_IS_OUT_OF_RANGE);
			return;
		}
		request.confirm(parthner1);
		parthner2.sendPacket(new SystemMessage(SystemMessage.C1_HAS_CONFIRMED_THE_TRADE).addString(parthner1.getName()), TradePressOtherOk.STATIC);
		if (!request.isConfirmed(parthner2))
		{
			parthner1.sendActionFailed();
			return;
		}
		List<TradeItem> tradeList1 = parthner1.getTradeList();
		List<TradeItem> tradeList2 = parthner2.getTradeList();
		int slots = 0;
		long weight = 0;
		boolean success = false;
		parthner1.getInventory().writeLock();
		parthner2.getInventory().writeLock();
		try
		{
			slots = 0;
			weight = 0;
			for (TradeItem ti : tradeList1)
			{
				ItemInstance item = parthner1.getInventory().getItemByObjectId(ti.getObjectId());
				if ((item == null) || (item.getCount() < ti.getCount()) || !item.canBeTraded(parthner1))
				{
					return;
				}
				weight = SafeMath.addAndCheck(weight, SafeMath.mulAndCheck(ti.getCount(), ti.getItem().getWeight()));
				if (!ti.getItem().isStackable() || (parthner2.getInventory().getItemByItemId(ti.getItemId()) == null))
				{
					slots++;
				}
			}
			if (!parthner2.getInventory().validateWeight(weight))
			{
				parthner2.sendPacket(Msg.YOU_HAVE_EXCEEDED_THE_WEIGHT_LIMIT);
				return;
			}
			if (!parthner2.getInventory().validateCapacity(slots))
			{
				parthner2.sendPacket(Msg.YOUR_INVENTORY_IS_FULL);
				return;
			}
			slots = 0;
			weight = 0;
			for (TradeItem ti : tradeList2)
			{
				ItemInstance item = parthner2.getInventory().getItemByObjectId(ti.getObjectId());
				if ((item == null) || (item.getCount() < ti.getCount()) || !item.canBeTraded(parthner2))
				{
					return;
				}
				weight = SafeMath.addAndCheck(weight, SafeMath.mulAndCheck(ti.getCount(), ti.getItem().getWeight()));
				if (!ti.getItem().isStackable() || (parthner1.getInventory().getItemByItemId(ti.getItemId()) == null))
				{
					slots++;
				}
			}
			if (!parthner1.getInventory().validateWeight(weight))
			{
				parthner1.sendPacket(Msg.YOU_HAVE_EXCEEDED_THE_WEIGHT_LIMIT);
				return;
			}
			if (!parthner1.getInventory().validateCapacity(slots))
			{
				parthner1.sendPacket(Msg.YOUR_INVENTORY_IS_FULL);
				return;
			}
			for (TradeItem ti : tradeList1)
			{
				ItemInstance item = parthner1.getInventory().removeItemByObjectId(ti.getObjectId(), ti.getCount());
				Log.LogItem(parthner1, Log.TradeSell, item);
				Log.LogItem(parthner2, Log.TradeBuy, item);
				parthner2.getInventory().addItem(item);
			}
			for (TradeItem ti : tradeList2)
			{
				ItemInstance item = parthner2.getInventory().removeItemByObjectId(ti.getObjectId(), ti.getCount());
				Log.LogItem(parthner2, Log.TradeSell, item);
				Log.LogItem(parthner1, Log.TradeBuy, item);
				parthner1.getInventory().addItem(item);
			}
			parthner1.sendPacket(Msg.YOUR_TRADE_IS_SUCCESSFUL);
			parthner2.sendPacket(Msg.YOUR_TRADE_IS_SUCCESSFUL);
			success = true;
		}
		finally
		{
			parthner2.getInventory().writeUnlock();
			parthner1.getInventory().writeUnlock();
			request.done();
			parthner1.sendPacket(success ? SendTradeDone.SUCCESS : SendTradeDone.FAIL);
			parthner2.sendPacket(success ? SendTradeDone.SUCCESS : SendTradeDone.FAIL);
		}
	}
}
