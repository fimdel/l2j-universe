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
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.PetInstance;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.model.items.PcInventory;
import lineage2.gameserver.model.items.PetInventory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestGiveItemToPet extends L2GameClientPacket
{
	/**
	 * Field _objectId.
	 */
	private int _objectId;
	/**
	 * Field _amount.
	 */
	private long _amount;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_objectId = readD();
		_amount = readQ();
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if ((activeChar == null) || (_amount < 1))
		{
			return;
		}
		PetInstance pet = activeChar.getSummonList().getPet();
		if (pet == null)
		{
			activeChar.sendActionFailed();
			return;
		}
		if (activeChar.isOutOfControl())
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
		if (pet.isDead())
		{
			sendPacket(Msg.CANNOT_GIVE_ITEMS_TO_A_DEAD_PET);
			return;
		}
		if (_objectId == pet.getControlItemObjId())
		{
			activeChar.sendActionFailed();
			return;
		}
		if (activeChar.isInStoreMode())
		{
			activeChar.sendActionFailed();
			return;
		}
		PetInventory petInventory = pet.getInventory();
		PcInventory playerInventory = activeChar.getInventory();
		ItemInstance item = playerInventory.getItemByObjectId(_objectId);
		if ((item == null) || (item.getCount() < _amount) || !item.canBeDropped(activeChar, false))
		{
			activeChar.sendActionFailed();
			return;
		}
		int slots = 0;
		long weight = item.getTemplate().getWeight() * _amount;
		if (!item.getTemplate().isStackable() || (pet.getInventory().getItemByItemId(item.getItemId()) == null))
		{
			slots = 1;
		}
		if (!pet.getInventory().validateWeight(weight))
		{
			sendPacket(Msg.EXCEEDED_PET_INVENTORYS_WEIGHT_LIMIT);
			return;
		}
		if (!pet.getInventory().validateCapacity(slots))
		{
			sendPacket(Msg.DUE_TO_THE_VOLUME_LIMIT_OF_THE_PETS_INVENTORY_NO_MORE_ITEMS_CAN_BE_PLACED_THERE);
			return;
		}
		petInventory.addItem(playerInventory.removeItemByObjectId(_objectId, _amount));
		pet.sendChanges();
		activeChar.sendChanges();
	}
}
