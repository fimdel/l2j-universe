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
import lineage2.gameserver.model.items.PcInventory;
import lineage2.gameserver.model.items.Warehouse;
import lineage2.gameserver.model.items.Warehouse.WarehouseType;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.templates.item.ItemTemplate;
import lineage2.gameserver.utils.Log;

import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class SendWareHouseDepositList extends L2GameClientPacket
{
	/**
	 * Field _WAREHOUSE_FEE. (value is 30)
	 */
	private static final long _WAREHOUSE_FEE = 30;
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
		Player activeChar = getClient().getActiveChar();
		if ((activeChar == null) || (_count == 0))
		{
			return;
		}
		if (!activeChar.getPlayerAccess().UseWarehouse)
		{
			activeChar.sendActionFailed();
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
		NpcInstance whkeeper = activeChar.getLastNpc();
		if ((whkeeper == null) || !activeChar.isInRangeZ(whkeeper, Creature.INTERACTION_DISTANCE))
		{
			activeChar.sendPacket(Msg.WAREHOUSE_IS_TOO_FAR);
			return;
		}
		PcInventory inventory = activeChar.getInventory();
		boolean privatewh = activeChar.getUsingWarehouseType() != WarehouseType.CLAN;
		Warehouse warehouse;
		if (privatewh)
		{
			warehouse = activeChar.getWarehouse();
		}
		else
		{
			warehouse = activeChar.getClan().getWarehouse();
		}
		inventory.writeLock();
		warehouse.writeLock();
		try
		{
			int slotsleft = 0;
			long adenaDeposit = 0;
			if (privatewh)
			{
				slotsleft = activeChar.getWarehouseLimit() - warehouse.getSize();
			}
			else
			{
				slotsleft = (activeChar.getClan().getWhBonus() + Config.WAREHOUSE_SLOTS_CLAN) - warehouse.getSize();
			}
			int items = 0;
			for (int i = 0; i < _count; i++)
			{
				ItemInstance item = inventory.getItemByObjectId(_items[i]);
				if ((item == null) || (item.getCount() < _itemQ[i]) || !item.canBeStored(activeChar, privatewh))
				{
					_items[i] = 0;
					_itemQ[i] = 0L;
					continue;
				}
				if (!item.isStackable() || (warehouse.getItemByItemId(item.getItemId()) == null))
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
				activeChar.sendPacket(Msg.YOUR_WAREHOUSE_IS_FULL);
			}
			if (items == 0)
			{
				activeChar.sendPacket(SystemMsg.INCORRECT_ITEM_COUNT);
				return;
			}
			long fee = SafeMath.mulAndCheck(items, _WAREHOUSE_FEE);
			if ((fee + adenaDeposit) > activeChar.getAdena())
			{
				activeChar.sendPacket(Msg.YOU_LACK_THE_FUNDS_NEEDED_TO_PAY_FOR_THIS_TRANSACTION);
				return;
			}
			if (!activeChar.reduceAdena(fee, true))
			{
				sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
				return;
			}
			for (int i = 0; i < _count; i++)
			{
				if (_items[i] == 0)
				{
					continue;
				}
				ItemInstance item = inventory.removeItemByObjectId(_items[i], _itemQ[i]);
				Log.LogItem(activeChar, privatewh ? Log.WarehouseDeposit : Log.ClanWarehouseDeposit, item);
				warehouse.addItem(item);
			}
		}
		catch (ArithmeticException ae)
		{
			sendPacket(Msg.YOU_HAVE_EXCEEDED_THE_QUANTITY_THAT_CAN_BE_INPUTTED);
			return;
		}
		finally
		{
			warehouse.writeUnlock();
			inventory.writeUnlock();
		}
		activeChar.sendChanges();
		activeChar.sendPacket(Msg.THE_TRANSACTION_IS_COMPLETE);
	}
}
