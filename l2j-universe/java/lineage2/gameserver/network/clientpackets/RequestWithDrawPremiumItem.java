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
import lineage2.gameserver.data.xml.holder.ItemHolder;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.PremiumItem;
import lineage2.gameserver.network.serverpackets.ExGetPremiumItemList;
import lineage2.gameserver.network.serverpackets.SystemMessage2;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class RequestWithDrawPremiumItem extends L2GameClientPacket
{
	/**
	 * Field _itemNum.
	 */
	private int _itemNum;
	/**
	 * Field _charId.
	 */
	private int _charId;
	/**
	 * Field _itemcount.
	 */
	private long _itemcount;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_itemNum = readD();
		_charId = readD();
		_itemcount = readQ();
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		final Player activeChar = getClient().getActiveChar();
		if (activeChar == null)
		{
			return;
		}
		if (_itemcount <= 0)
		{
			return;
		}
		if (activeChar.getObjectId() != _charId)
		{
			return;
		}
		if (activeChar.getPremiumItemList().isEmpty())
		{
			return;
		}
		if ((activeChar.getWeightPenalty() >= 3) || ((activeChar.getInventoryLimit() * 0.8) <= activeChar.getInventory().getSize()))
		{
			activeChar.sendPacket(Msg.YOU_CANNOT_RECEIVE_THE_VITAMIN_ITEM_BECAUSE_YOU_HAVE_EXCEED_YOUR_INVENTORY_WEIGHT_QUANTITY_LIMIT);
			return;
		}
		if (activeChar.isProcessingRequest())
		{
			activeChar.sendPacket(Msg.YOU_CANNOT_RECEIVE_A_VITAMIN_ITEM_DURING_AN_EXCHANGE);
			return;
		}
		PremiumItem _item = activeChar.getPremiumItemList().get(_itemNum);
		if (_item == null)
		{
			return;
		}
		boolean stackable = ItemHolder.getInstance().getTemplate(_item.getItemId()).isStackable();
		if (_item.getCount() < _itemcount)
		{
			return;
		}
		if (!stackable)
		{
			for (int i = 0; i < _itemcount; i++)
			{
				addItem(activeChar, _item.getItemId(), 1);
			}
		}
		else
		{
			addItem(activeChar, _item.getItemId(), _itemcount);
		}
		if (_itemcount < _item.getCount())
		{
			activeChar.getPremiumItemList().get(_itemNum).updateCount(_item.getCount() - _itemcount);
			activeChar.updatePremiumItem(_itemNum, _item.getCount() - _itemcount);
		}
		else
		{
			activeChar.getPremiumItemList().remove(_itemNum);
			activeChar.deletePremiumItem(_itemNum);
		}
		if (activeChar.getPremiumItemList().isEmpty())
		{
			activeChar.sendPacket(Msg.THERE_ARE_NO_MORE_VITAMIN_ITEMS_TO_BE_FOUND);
		}
		else
		{
			activeChar.sendPacket(new ExGetPremiumItemList(activeChar));
		}
	}
	
	/**
	 * Method addItem.
	 * @param player Player
	 * @param itemId int
	 * @param count long
	 */
	private void addItem(Player player, int itemId, long count)
	{
		player.getInventory().addItem(itemId, count);
		player.sendPacket(SystemMessage2.obtainItems(itemId, count, 0));
	}
}
