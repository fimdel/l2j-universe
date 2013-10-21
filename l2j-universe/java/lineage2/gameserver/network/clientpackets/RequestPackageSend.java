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
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.model.items.PcFreight;
import lineage2.gameserver.model.items.PcInventory;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.templates.item.ItemTemplate;
import lineage2.gameserver.utils.Log;

import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestPackageSend extends L2GameClientPacket
{
	/**
	 * Field _FREIGHT_FEE. (value is 1000)
	 */
	private static final long _FREIGHT_FEE = 1000;
	/**
	 * Field _objectId.
	 */
	private int _objectId;
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
		_objectId = readD();
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
			if ((_itemQ[i] < 1) || (ArrayUtils.indexOf(_items, _items[i]) < i))
			{
				_count = 0;
				return;
			}
		}
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		Player player = getClient().getActiveChar();
		if ((player == null) || (_count == 0))
		{
			return;
		}
		if (!player.getPlayerAccess().UseWarehouse)
		{
			player.sendActionFailed();
			return;
		}
		if (player.isActionsDisabled())
		{
			player.sendActionFailed();
			return;
		}
		if (player.isInStoreMode())
		{
			player.sendPacket(SystemMsg.WHILE_OPERATING_A_PRIVATE_STORE_OR_WORKSHOP_YOU_CANNOT_DISCARD_DESTROY_OR_TRADE_AN_ITEM);
			return;
		}
		if (player.isInTrade())
		{
			player.sendActionFailed();
			return;
		}
		NpcInstance whkeeper = player.getLastNpc();
		if ((whkeeper == null) || !player.isInRangeZ(whkeeper, Creature.INTERACTION_DISTANCE))
		{
			return;
		}
		if (!player.getAccountChars().containsKey(_objectId))
		{
			return;
		}
		PcInventory inventory = player.getInventory();
		PcFreight freight = new PcFreight(_objectId);
		freight.restore();
		inventory.writeLock();
		freight.writeLock();
		try
		{
			int slotsleft = 0;
			long adenaDeposit = 0;
			slotsleft = Config.FREIGHT_SLOTS - freight.getSize();
			int items = 0;
			for (int i = 0; i < _count; i++)
			{
				ItemInstance item = inventory.getItemByObjectId(_items[i]);
				if ((item == null) || (item.getCount() < _itemQ[i]) || !item.getTemplate().isFreightable())
				{
					_items[i] = 0;
					_itemQ[i] = 0L;
					continue;
				}
				if (!item.isStackable() || (freight.getItemByItemId(item.getItemId()) == null))
				{
					if (slotsleft <= 0)
					{
						_items[i] = 0;
						_itemQ[i] = 0L;
						continue;
					}
					slotsleft--;
				}
				if (item.getItemId() == ItemTemplate.ITEM_ID_ADENA)
				{
					adenaDeposit = _itemQ[i];
				}
				items++;
			}
			if (slotsleft <= 0)
			{
				player.sendPacket(SystemMsg.YOU_HAVE_EXCEEDED_THE_QUANTITY_THAT_CAN_BE_INPUTTED);
			}
			if (items == 0)
			{
				player.sendPacket(SystemMsg.INCORRECT_ITEM_COUNT);
				return;
			}
			long fee = SafeMath.mulAndCheck(items, _FREIGHT_FEE);
			if ((fee + adenaDeposit) > player.getAdena())
			{
				player.sendPacket(SystemMsg.YOU_LACK_THE_FUNDS_NEEDED_TO_PAY_FOR_THIS_TRANSACTION);
				return;
			}
			if (!player.reduceAdena(fee, true))
			{
				player.sendPacket(SystemMsg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
				return;
			}
			for (int i = 0; i < _count; i++)
			{
				if (_items[i] == 0)
				{
					continue;
				}
				ItemInstance item = inventory.removeItemByObjectId(_items[i], _itemQ[i]);
				Log.LogItem(player, Log.FreightDeposit, item);
				freight.addItem(item);
			}
		}
		catch (ArithmeticException ae)
		{
			player.sendPacket(SystemMsg.YOU_HAVE_EXCEEDED_THE_QUANTITY_THAT_CAN_BE_INPUTTED);
			return;
		}
		finally
		{
			freight.writeUnlock();
			inventory.writeUnlock();
		}
		player.sendChanges();
		player.sendPacket(SystemMsg.THE_TRANSACTION_IS_COMPLETE);
	}
}
