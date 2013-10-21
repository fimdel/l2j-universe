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
import java.util.concurrent.CopyOnWriteArrayList;

import lineage2.commons.math.SafeMath;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.data.xml.holder.ItemHolder;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.items.TradeItem;
import lineage2.gameserver.network.serverpackets.PrivateStoreManageListBuy;
import lineage2.gameserver.network.serverpackets.PrivateStoreMsgBuy;
import lineage2.gameserver.network.serverpackets.components.CustomMessage;
import lineage2.gameserver.templates.item.ItemTemplate;
import lineage2.gameserver.utils.TradeHelper;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class SetPrivateStoreBuyList extends L2GameClientPacket
{
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
		_count = readD();
		if (((_count * 40) > _buf.remaining()) || (_count > Short.MAX_VALUE) || (_count < 1))
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
			readD();
			_itemQ[i] = readQ();
			_itemP[i] = readQ();
			if ((_itemQ[i] < 1) || (_itemP[i] < 1))
			{
				_count = 0;
				break;
			}
			readH();
			readH();
			readD();
			readD();
			readD();
			readD();
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
		if (!TradeHelper.checksIfCanOpenStore(buyer, Player.STORE_PRIVATE_BUY))
		{
			buyer.sendActionFailed();
			return;
		}
		List<TradeItem> buyList = new CopyOnWriteArrayList<>();
		long totalCost = 0;
		try
		{
			loop:
			for (int i = 0; i < _count; i++)
			{
				int itemId = _items[i];
				long count = _itemQ[i];
				long price = _itemP[i];
				ItemTemplate item = ItemHolder.getInstance().getTemplate(itemId);
				if ((item == null) || (itemId == ItemTemplate.ITEM_ID_ADENA))
				{
					continue;
				}
				if ((item.getReferencePrice() / 2) > price)
				{
					buyer.sendMessage(new CustomMessage("lineage2.gameserver.clientpackets.SetPrivateStoreBuyList.TooLowPrice", buyer).addItemName(item).addNumber(item.getReferencePrice() / 2));
					continue;
				}
				if (item.isStackable())
				{
					for (TradeItem bi : buyList)
					{
						if (bi.getItemId() == itemId)
						{
							bi.setOwnersPrice(price);
							bi.setCount(bi.getCount() + count);
							totalCost = SafeMath.addAndCheck(totalCost, SafeMath.mulAndCheck(count, price));
							continue loop;
						}
					}
				}
				TradeItem bi = new TradeItem();
				bi.setItemId(itemId);
				bi.setCount(count);
				bi.setOwnersPrice(price);
				totalCost = SafeMath.addAndCheck(totalCost, SafeMath.mulAndCheck(count, price));
				buyList.add(bi);
			}
		}
		catch (ArithmeticException ae)
		{
			sendPacket(Msg.YOU_HAVE_EXCEEDED_THE_QUANTITY_THAT_CAN_BE_INPUTTED);
			return;
		}
		if (buyList.size() > buyer.getTradeLimit())
		{
			buyer.sendPacket(Msg.YOU_HAVE_EXCEEDED_THE_QUANTITY_THAT_CAN_BE_INPUTTED);
			buyer.sendPacket(new PrivateStoreManageListBuy(buyer));
			return;
		}
		if (totalCost > buyer.getAdena())
		{
			buyer.sendPacket(Msg.THE_PURCHASE_PRICE_IS_HIGHER_THAN_THE_AMOUNT_OF_MONEY_THAT_YOU_HAVE_AND_SO_YOU_CANNOT_OPEN_A_PERSONAL_STORE);
			buyer.sendPacket(new PrivateStoreManageListBuy(buyer));
			return;
		}
		if (!buyList.isEmpty())
		{
			buyer.setBuyList(buyList);
			buyer.saveTradeList();
			buyer.setPrivateStoreType(Player.STORE_PRIVATE_BUY);
			buyer.broadcastPacket(new PrivateStoreMsgBuy(buyer));
			buyer.sitDown(null);
			buyer.broadcastCharInfo();
		}
		buyer.sendActionFailed();
	}
}
