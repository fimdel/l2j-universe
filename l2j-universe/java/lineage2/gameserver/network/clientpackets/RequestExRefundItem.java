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
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.network.serverpackets.ExBuySellList;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.utils.Log;

import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestExRefundItem extends L2GameClientPacket
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
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_listId = readD();
		_count = readD();
		if (((_count * 4) > _buf.remaining()) || (_count > Short.MAX_VALUE) || (_count < 1))
		{
			_count = 0;
			return;
		}
		_items = new int[_count];
		for (int i = 0; i < _count; i++)
		{
			_items[i] = readD();
			if (ArrayUtils.indexOf(_items, _items[i]) < i)
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
		NpcInstance npc = activeChar.getLastNpc();
		boolean isValidMerchant = (npc != null) && npc.isMerchantNpc();
		if (!activeChar.isGM() && ((npc == null) || !isValidMerchant || !activeChar.isInRange(npc, Creature.INTERACTION_DISTANCE)))
		{
			activeChar.sendActionFailed();
			return;
		}
		activeChar.getInventory().writeLock();
		try
		{
			int slots = 0;
			long weight = 0;
			long totalPrice = 0;
			List<ItemInstance> refundList = new ArrayList<>();
			for (int objId : _items)
			{
				ItemInstance item = activeChar.getRefund().getItemByObjectId(objId);
				if (item == null)
				{
					continue;
				}
				totalPrice = SafeMath.addAndCheck(totalPrice, SafeMath.mulAndCheck(item.getCount(), item.getReferencePrice()) / 2);
				weight = SafeMath.addAndCheck(weight, SafeMath.mulAndCheck(item.getCount(), item.getTemplate().getWeight()));
				if (!item.isStackable() || (activeChar.getInventory().getItemByItemId(item.getItemId()) == null))
				{
					slots++;
				}
				refundList.add(item);
			}
			if (refundList.isEmpty())
			{
				activeChar.sendPacket(SystemMsg.INCORRECT_ITEM_COUNT);
				activeChar.sendActionFailed();
				return;
			}
			if (!activeChar.getInventory().validateWeight(weight))
			{
				sendPacket(Msg.YOU_HAVE_EXCEEDED_THE_WEIGHT_LIMIT);
				activeChar.sendActionFailed();
				return;
			}
			if (!activeChar.getInventory().validateCapacity(slots))
			{
				sendPacket(Msg.YOUR_INVENTORY_IS_FULL);
				activeChar.sendActionFailed();
				return;
			}
			if (!activeChar.reduceAdena(totalPrice))
			{
				activeChar.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
				activeChar.sendActionFailed();
				return;
			}
			for (ItemInstance item : refundList)
			{
				ItemInstance refund = activeChar.getRefund().removeItem(item);
				Log.LogItem(activeChar, Log.RefundReturn, refund);
				activeChar.getInventory().addItem(refund);
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
