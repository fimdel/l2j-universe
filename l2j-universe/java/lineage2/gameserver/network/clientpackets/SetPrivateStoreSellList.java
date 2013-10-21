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

import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.model.items.TradeItem;
import lineage2.gameserver.network.serverpackets.PrivateStoreManageListSell;
import lineage2.gameserver.network.serverpackets.PrivateStoreMsgSell;
import lineage2.gameserver.templates.item.ItemTemplate;
import lineage2.gameserver.utils.TradeHelper;

import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class SetPrivateStoreSellList extends L2GameClientPacket
{
	/**
	 * Field _count.
	 */
	private int _count;
	/**
	 * Field _package.
	 */
	private boolean _package;
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
		_package = readD() == 1;
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
			if ((_itemQ[i] < 1) || (_itemP[i] < 0) || (ArrayUtils.indexOf(_items, _items[i]) < i))
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
		Player seller = getClient().getActiveChar();
		if ((seller == null) || (_count == 0))
		{
			return;
		}
		if (!TradeHelper.checksIfCanOpenStore(seller, _package ? Player.STORE_PRIVATE_SELL_PACKAGE : Player.STORE_PRIVATE_SELL))
		{
			seller.sendActionFailed();
			return;
		}
		TradeItem temp;
		List<TradeItem> sellList = new CopyOnWriteArrayList<>();
		seller.getInventory().writeLock();
		try
		{
			for (int i = 0; i < _count; i++)
			{
				int objectId = _items[i];
				long count = _itemQ[i];
				long price = _itemP[i];
				ItemInstance item = seller.getInventory().getItemByObjectId(objectId);
				if ((item == null) || (item.getCount() < count) || !item.canBeTraded(seller) || (item.getItemId() == ItemTemplate.ITEM_ID_ADENA))
				{
					continue;
				}
				temp = new TradeItem(item);
				temp.setCount(count);
				temp.setOwnersPrice(price);
				sellList.add(temp);
			}
		}
		finally
		{
			seller.getInventory().writeUnlock();
		}
		if (sellList.size() > seller.getTradeLimit())
		{
			seller.sendPacket(Msg.YOU_HAVE_EXCEEDED_THE_QUANTITY_THAT_CAN_BE_INPUTTED);
			seller.sendPacket(new PrivateStoreManageListSell(seller, _package));
			return;
		}
		if (!sellList.isEmpty())
		{
			seller.setSellList(_package, sellList);
			seller.saveTradeList();
			seller.setPrivateStoreType(_package ? Player.STORE_PRIVATE_SELL_PACKAGE : Player.STORE_PRIVATE_SELL);
			seller.broadcastPacket(new PrivateStoreMsgSell(seller));
			seller.sitDown(null);
			seller.broadcastCharInfo();
		}
		seller.sendActionFailed();
	}
}
