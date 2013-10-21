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
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.utils.Log;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class SendWareHouseWithDrawList extends L2GameClientPacket
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(SendWareHouseWithDrawList.class);
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
		if ((whkeeper == null) || !activeChar.isInRange(whkeeper, Creature.INTERACTION_DISTANCE))
		{
			activeChar.sendPacket(Msg.WAREHOUSE_IS_TOO_FAR);
			return;
		}
		Warehouse warehouse = null;
		String logType = null;
		if (activeChar.getUsingWarehouseType() == WarehouseType.PRIVATE)
		{
			warehouse = activeChar.getWarehouse();
			logType = Log.WarehouseWithdraw;
		}
		else if (activeChar.getUsingWarehouseType() == WarehouseType.CLAN)
		{
			logType = Log.ClanWarehouseWithdraw;
			boolean canWithdrawCWH = false;
			if (activeChar.getClan() != null)
			{
				if (((activeChar.getClanPrivileges() & Clan.CP_CL_WAREHOUSE_SEARCH) == Clan.CP_CL_WAREHOUSE_SEARCH) && (Config.ALT_ALLOW_OTHERS_WITHDRAW_FROM_CLAN_WAREHOUSE || activeChar.isClanLeader() || activeChar.getVarB("canWhWithdraw")))
				{
					canWithdrawCWH = true;
				}
			}
			if (!canWithdrawCWH)
			{
				return;
			}
			warehouse = activeChar.getClan().getWarehouse();
		}
		else if (activeChar.getUsingWarehouseType() == WarehouseType.FREIGHT)
		{
			warehouse = activeChar.getFreight();
			logType = Log.FreightWithdraw;
		}
		else
		{
			_log.warn("Error retrieving a warehouse object for char " + activeChar.getName() + " - using warehouse type: " + activeChar.getUsingWarehouseType());
			return;
		}
		PcInventory inventory = activeChar.getInventory();
		inventory.writeLock();
		warehouse.writeLock();
		try
		{
			long weight = 0;
			int slots = 0;
			for (int i = 0; i < _count; i++)
			{
				ItemInstance item = warehouse.getItemByObjectId(_items[i]);
				if ((item == null) || (item.getCount() < _itemQ[i]))
				{
					activeChar.sendPacket(SystemMsg.INCORRECT_ITEM_COUNT);
					return;
				}
				weight = SafeMath.addAndCheck(weight, SafeMath.mulAndCheck(item.getTemplate().getWeight(), _itemQ[i]));
				if (!item.isStackable() || (inventory.getItemByItemId(item.getItemId()) == null))
				{
					slots++;
				}
			}
			if (!activeChar.getInventory().validateCapacity(slots))
			{
				activeChar.sendPacket(Msg.YOUR_INVENTORY_IS_FULL);
				return;
			}
			if (!activeChar.getInventory().validateWeight(weight))
			{
				activeChar.sendPacket(Msg.YOU_HAVE_EXCEEDED_THE_WEIGHT_LIMIT);
				return;
			}
			for (int i = 0; i < _count; i++)
			{
				ItemInstance item = warehouse.removeItemByObjectId(_items[i], _itemQ[i]);
				Log.LogItem(activeChar, logType, item);
				activeChar.getInventory().addItem(item);
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
