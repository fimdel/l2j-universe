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

import lineage2.commons.math.SafeMath;
import lineage2.gameserver.Config;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.network.serverpackets.ExBuySellList;
import lineage2.gameserver.utils.Log;

import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestSellItem extends L2GameClientPacket
{
	/**
	 * Field _listId.
	 */
	@SuppressWarnings("unused")
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
		if (((_count * 16) > _buf.remaining()) || (_count > Short.MAX_VALUE) || (_count < 1))
		{
			_count = 0;
			return;
		}
		_items = new int[_count];
		_itemQ = new long[_count];
		for (int i = 0; i < _count; i++)
		{
			_items[i] = readD();
			readD();
			_itemQ[i] = readQ();
			if ((_itemQ[i] < 1) || (ArrayUtils.indexOf(_items, _items[i]) < i))
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
		if (!Config.ALT_GAME_KARMA_PLAYER_CAN_SHOP && (activeChar.getKarma() < 0) && !activeChar.isGM())
		{
			activeChar.sendActionFailed();
			return;
		}
		NpcInstance merchant = activeChar.getLastNpc();
		boolean isValidMerchant = (merchant != null) && merchant.isMerchantNpc();
		if (!activeChar.isGM() && !activeChar.isBBSUse() && ((merchant == null) || !isValidMerchant || !activeChar.isInRange(merchant, Creature.INTERACTION_DISTANCE)))
		{
			activeChar.sendActionFailed();
			return;
		}
		activeChar.getInventory().writeLock();
		try
		{
			for (int i = 0; i < _count; i++)
			{
				int objectId = _items[i];
				long count = _itemQ[i];
				ItemInstance item = activeChar.getInventory().getItemByObjectId(objectId);
				if ((item == null) || (item.getCount() < count) || !item.canBeSold(activeChar))
				{
					continue;
				}
				long price = SafeMath.mulAndCheck(item.getReferencePrice(), count) / 2;
				ItemInstance refund = activeChar.getInventory().removeItemByObjectId(objectId, count);
				Log.LogItem(activeChar, Log.RefundSell, refund);
				activeChar.addAdena(price);
				activeChar.getRefund().addItem(refund);
				if (activeChar.isBBSUse())
				{
					activeChar.setIsBBSUse(false);
				}
			}
		}
		catch (ArithmeticException ae)
		{
			sendPacket(Msg.YOU_HAVE_EXCEEDED_THE_QUANTITY_THAT_CAN_BE_INPUTTED);
			return;
		}
		finally
		{
			activeChar.getInventory().writeUnlock();
		}
		activeChar.sendPacket(new ExBuySellList.SellRefundList(activeChar, true));
		activeChar.sendChanges();
	}
}
