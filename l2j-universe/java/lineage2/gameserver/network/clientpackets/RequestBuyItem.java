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
import lineage2.gameserver.Config;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.data.xml.holder.BuyListHolder;
import lineage2.gameserver.data.xml.holder.BuyListHolder.NpcTradeList;
import lineage2.gameserver.instancemanager.ReflectionManager;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.residence.Castle;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.items.TradeItem;
import lineage2.gameserver.network.serverpackets.ExBuySellList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestBuyItem extends L2GameClientPacket
{
	/**
	 * Field _log.
	 */
	@SuppressWarnings("unused")
	private static final Logger _log = LoggerFactory.getLogger(RequestBuyItem.class);
	/**
	 * Field _listId.
	 */
	private int _listId;
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
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_listId = readD();
		_count = readD();
		if (((_count * 12) > _buf.remaining()) || (_count > Short.MAX_VALUE) || (_count < 1))
		{
			_count = 0;
			return;
		}
		_items = new int[_count];
		_itemQ = new long[_count];
		for (int i = 0; i < _count; i++)
		{
			_items[i] = readD();
			_itemQ[i] = readQ();
			if (_itemQ[i] < 1)
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
		Player activeChar = getClient().getActiveChar();
		if ((activeChar == null) || (_count == 0))
		{
			return;
		}
		if (activeChar.getBuyListId() != _listId)
		{
			return;
		}
		if (activeChar.isActionsDisabled())
		{
			activeChar.sendActionFailed();
			return;
		}
		if (activeChar.isInStoreMode())
		{
			activeChar.sendPacket(Msg.WHILE_OPERATING_A_PRIVATE_STORE_OR_WORKSHOP_YOU_CANNOT_DISCARD_DESTROY_OR_TRADE_AN_ITEM);
			return;
		}
		if (activeChar.isInTrade())
		{
			activeChar.sendActionFailed();
			return;
		}
		if (activeChar.isFishing())
		{
			activeChar.sendPacket(Msg.YOU_CANNOT_DO_THAT_WHILE_FISHING);
			return;
		}
		if (!Config.ALT_GAME_KARMA_PLAYER_CAN_SHOP && (activeChar.isChaotic()) && !activeChar.isGM())
		{
			activeChar.sendActionFailed();
			return;
		}
		NpcInstance merchant = activeChar.getLastNpc();
		boolean isValidMerchant = (merchant != null) && merchant.isMerchantNpc();
		if (!activeChar.isGM() && ((merchant == null) || !isValidMerchant || !activeChar.isInRange(merchant, Creature.INTERACTION_DISTANCE)))
		{
			activeChar.sendActionFailed();
			return;
		}
		NpcTradeList list = BuyListHolder.getInstance().getBuyList(_listId);
		if (list == null)
		{
			activeChar.sendActionFailed();
			return;
		}
		int slots = 0;
		long weight = 0;
		long totalPrice = 0;
		long tax = 0;
		double taxRate = 0;
		Castle castle = null;
		if (merchant != null)
		{
			castle = merchant.getCastle(activeChar);
			if (castle != null)
			{
				taxRate = castle.getTaxRate();
			}
		}
		List<TradeItem> buyList = new ArrayList<TradeItem>(_count);
		List<TradeItem> tradeList = list.getItems();
		try
		{
			loop:
			for (int i = 0; i < _count; i++)
			{
				int itemId = _items[i];
				long count = _itemQ[i];
				long price = 0;
				for (TradeItem ti : tradeList)
				{
					if (ti.getItemId() == itemId)
					{
						if (ti.isCountLimited() && (ti.getCurrentValue() < count))
						{
							continue loop;
						}
						price = ti.getOwnersPrice();
					}
				}
				if ((price == 0) && (!activeChar.isGM() || !activeChar.getPlayerAccess().UseGMShop))
				{
					activeChar.sendActionFailed();
					return;
				}
				totalPrice = SafeMath.addAndCheck(totalPrice, SafeMath.mulAndCheck(count, price));
				TradeItem ti = new TradeItem();
				ti.setItemId(itemId);
				ti.setCount(count);
				ti.setOwnersPrice(price);
				weight = SafeMath.addAndCheck(weight, SafeMath.mulAndCheck(count, ti.getItem().getWeight()));
				if (!ti.getItem().isStackable() || (activeChar.getInventory().getItemByItemId(itemId) == null))
				{
					slots++;
				}
				buyList.add(ti);
			}
			tax = (long) (totalPrice * taxRate);
			totalPrice = SafeMath.addAndCheck(totalPrice, tax);
			if (!activeChar.getInventory().validateWeight(weight))
			{
				sendPacket(Msg.YOU_HAVE_EXCEEDED_THE_WEIGHT_LIMIT);
				return;
			}
			if (!activeChar.getInventory().validateCapacity(slots))
			{
				sendPacket(Msg.YOUR_INVENTORY_IS_FULL);
				return;
			}
			if (!activeChar.reduceAdena(totalPrice))
			{
				activeChar.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
				return;
			}
			for (TradeItem ti : buyList)
			{
				activeChar.getInventory().addItem(ti.getItemId(), ti.getCount());
			}
			list.updateItems(buyList);
			if (castle != null)
			{
				if ((tax > 0) && (castle.getOwnerId() > 0) && (activeChar.getReflection() == ReflectionManager.DEFAULT))
				{
					castle.addToTreasury(tax, true, false);
				}
			}
		}
		catch (ArithmeticException ae)
		{
			sendPacket(Msg.YOU_HAVE_EXCEEDED_THE_QUANTITY_THAT_CAN_BE_INPUTTED);
			return;
		}
		sendPacket(new ExBuySellList.SellRefundList(activeChar, true));
		activeChar.sendChanges();
	}
}
