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

import java.util.ArrayList;
import java.util.List;

import lineage2.commons.math.SafeMath;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.instancemanager.WorldStatisticsManager;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.model.items.TradeItem;
import lineage2.gameserver.model.worldstatistics.CategoryType;
import lineage2.gameserver.network.serverpackets.components.CustomMessage;
import lineage2.gameserver.utils.Log;
import lineage2.gameserver.utils.TradeHelper;

import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestPrivateStoreBuy extends L2GameClientPacket
{
	/**
	 * Field _sellerId.
	 */
	private int _sellerId;
	/**
	 * Field _count.
	 */
	private int _count;
	/**
	 * Field _items.
	 */
	private int[] _items;
	/**
	 * Field _itemQ.
	 */
	private long[] _itemQ;
	/**
	 * Field _itemP.
	 */
	private long[] _itemP;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_sellerId = readD();
		_count = readD();
		if (((_count * 20) > _buf.remaining()) || (_count > Short.MAX_VALUE) || (_count < 1))
		{
			_count = 0;
			return;
		}
		_items = new int[_count];
		_itemQ = new long[_count];
		_itemP = new long[_count];
		for (int i = 0; i < _count; i++)
		{
			_items[i] = readD();
			_itemQ[i] = readQ();
			_itemP[i] = readQ();
			if ((_itemQ[i] < 1) || (_itemP[i] < 1) || (ArrayUtils.indexOf(_items, _items[i]) < i))
			{
				_count = 0;
				break;
			}
		}
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		Player buyer = getClient().getActiveChar();
		if ((buyer == null) || (_count == 0))
		{
			return;
		}
		if (buyer.isActionsDisabled())
		{
			buyer.sendActionFailed();
			return;
		}
		if (buyer.isInStoreMode())
		{
			buyer.sendPacket(Msg.WHILE_OPERATING_A_PRIVATE_STORE_OR_WORKSHOP_YOU_CANNOT_DISCARD_DESTROY_OR_TRADE_AN_ITEM);
			return;
		}
		if (buyer.isInTrade())
		{
			buyer.sendActionFailed();
			return;
		}
		if (buyer.isFishing())
		{
			buyer.sendPacket(Msg.YOU_CANNOT_DO_ANYTHING_ELSE_WHILE_FISHING);
			return;
		}
		if (!buyer.getPlayerAccess().UseTrade)
		{
			buyer.sendPacket(Msg.THIS_ACCOUNT_CANOT_USE_PRIVATE_STORES);
			return;
		}
		Player seller = (Player) buyer.getVisibleObject(_sellerId);
		if ((seller == null) || ((seller.getPrivateStoreType() != Player.STORE_PRIVATE_SELL) && (seller.getPrivateStoreType() != Player.STORE_PRIVATE_SELL_PACKAGE)) || !seller.isInRangeZ(buyer, Creature.INTERACTION_DISTANCE))
		{
			buyer.sendPacket(Msg.THE_ATTEMPT_TO_TRADE_HAS_FAILED);
			buyer.sendActionFailed();
			return;
		}
		List<TradeItem> sellList = seller.getSellList();
		if (sellList.isEmpty())
		{
			buyer.sendPacket(Msg.THE_ATTEMPT_TO_TRADE_HAS_FAILED);
			buyer.sendActionFailed();
			return;
		}
		List<TradeItem> buyList = new ArrayList<>();
		long totalCost = 0;
		int slots = 0;
		long weight = 0;
		buyer.getInventory().writeLock();
		seller.getInventory().writeLock();
		try
		{
			loop:
			for (int i = 0; i < _count; i++)
			{
				int objectId = _items[i];
				long count = _itemQ[i];
				long price = _itemP[i];
				TradeItem bi = null;
				for (TradeItem si : sellList)
				{
					if (si.getObjectId() == objectId)
					{
						if (si.getOwnersPrice() == price)
						{
							if (count > si.getCount())
							{
								break loop;
							}
							ItemInstance item = seller.getInventory().getItemByObjectId(objectId);
							if ((item == null) || (item.getCount() < count) || !item.canBeTraded(seller))
							{
								break loop;
							}
							totalCost = SafeMath.addAndCheck(totalCost, SafeMath.mulAndCheck(count, price));
							weight = SafeMath.addAndCheck(weight, SafeMath.mulAndCheck(count, item.getTemplate().getWeight()));
							if (!item.isStackable() || (buyer.getInventory().getItemByItemId(item.getItemId()) == null))
							{
								slots++;
							}
							bi = new TradeItem();
							bi.setObjectId(objectId);
							bi.setItemId(item.getItemId());
							bi.setCount(count);
							bi.setOwnersPrice(price);
							buyList.add(bi);
							break;
						}
					}
				}
			}
		}
		catch (ArithmeticException ae)
		{
			buyList.clear();
			sendPacket(Msg.YOU_HAVE_EXCEEDED_THE_QUANTITY_THAT_CAN_BE_INPUTTED);
			return;
		}
		finally
		{
			try
			{
				if ((buyList.size() != _count) || ((seller.getPrivateStoreType() == Player.STORE_PRIVATE_SELL_PACKAGE) && (buyList.size() != sellList.size())))
				{
					buyer.sendPacket(Msg.THE_ATTEMPT_TO_TRADE_HAS_FAILED);
					buyer.sendActionFailed();
					return;
				}
				if (!buyer.getInventory().validateWeight(weight))
				{
					buyer.sendPacket(Msg.YOU_HAVE_EXCEEDED_THE_WEIGHT_LIMIT);
					buyer.sendActionFailed();
					return;
				}
				if (!buyer.getInventory().validateCapacity(slots))
				{
					buyer.sendPacket(Msg.YOUR_INVENTORY_IS_FULL);
					buyer.sendActionFailed();
					return;
				}
				if (!buyer.reduceAdena(totalCost))
				{
					buyer.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
					buyer.sendActionFailed();
					return;
				}
				ItemInstance item;
				for (TradeItem bi : buyList)
				{
					item = seller.getInventory().removeItemByObjectId(bi.getObjectId(), bi.getCount());
					for (TradeItem si : sellList)
					{
						if (si.getObjectId() == bi.getObjectId())
						{
							si.setCount(si.getCount() - bi.getCount());
							if (si.getCount() < 1L)
							{
								sellList.remove(si);
							}
							break;
						}
					}
					Log.LogItem(seller, Log.PrivateStoreSell, item);
					Log.LogItem(buyer, Log.PrivateStoreBuy, item);
					buyer.getInventory().addItem(item);
					TradeHelper.purchaseItem(buyer, seller, bi);
				}
				WorldStatisticsManager.getInstance().updateStat(seller, CategoryType.PRIVATE_SELL_COUNT, buyList.size());

				long tax = TradeHelper.getTax(seller, totalCost);
				if (tax > 0)
				{
					totalCost -= tax;
					seller.sendMessage(new CustomMessage("trade.HavePaidTax", seller).addNumber(tax));
				}
				seller.addAdena(totalCost);
				seller.saveTradeList();
			}
			finally
			{
				seller.getInventory().writeUnlock();
				buyer.getInventory().writeUnlock();
			}
		}
		if (sellList.isEmpty())
		{
			TradeHelper.cancelStore(seller);
		}
		seller.sendChanges();
		buyer.sendChanges();
		buyer.sendActionFailed();
	}
}
