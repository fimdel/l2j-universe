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
package lineage2.gameserver.model.items;

import java.util.Collection;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.PetInstance;
import lineage2.gameserver.model.items.ItemInstance.ItemLocation;
import lineage2.gameserver.network.serverpackets.PetInventoryUpdate;
import lineage2.gameserver.utils.ItemFunctions;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class PetInventory extends Inventory
{
	/**
	 * Field _actor.
	 */
	private final PetInstance _actor;
	
	/**
	 * Constructor for PetInventory.
	 * @param actor PetInstance
	 */
	public PetInventory(PetInstance actor)
	{
		super(actor.getPlayer().getObjectId());
		_actor = actor;
	}
	
	/**
	 * Method getActor.
	 * @return PetInstance
	 */
	@Override
	public PetInstance getActor()
	{
		return _actor;
	}
	
	/**
	 * Method getOwner.
	 * @return Player
	 */
	public Player getOwner()
	{
		return _actor.getPlayer();
	}
	
	/**
	 * Method getBaseLocation.
	 * @return ItemLocation
	 */
	@Override
	protected ItemLocation getBaseLocation()
	{
		return ItemLocation.PET_INVENTORY;
	}
	
	/**
	 * Method getEquipLocation.
	 * @return ItemLocation
	 */
	@Override
	protected ItemLocation getEquipLocation()
	{
		return ItemLocation.PET_PAPERDOLL;
	}
	
	/**
	 * Method onRefreshWeight.
	 */
	@Override
	protected void onRefreshWeight()
	{
		getActor().sendPetInfo();
	}
	
	/**
	 * Method sendAddItem.
	 * @param item ItemInstance
	 */
	@Override
	protected void sendAddItem(ItemInstance item)
	{
		getOwner().sendPacket(new PetInventoryUpdate().addNewItem(item));
	}
	
	/**
	 * Method sendModifyItem.
	 * @param item ItemInstance
	 */
	@Override
	protected void sendModifyItem(ItemInstance item)
	{
		getOwner().sendPacket(new PetInventoryUpdate().addModifiedItem(item));
	}
	
	/**
	 * Method sendRemoveItem.
	 * @param item ItemInstance
	 */
	@Override
	protected void sendRemoveItem(ItemInstance item)
	{
		getOwner().sendPacket(new PetInventoryUpdate().addRemovedItem(item));
	}
	
	/**
	 * Method restore.
	 */
	@Override
	public void restore()
	{
		final int ownerId = getOwnerId();
		writeLock();
		try
		{
			Collection<ItemInstance> items = _itemsDAO.getItemsByOwnerIdAndLoc(ownerId, getBaseLocation());
			for (ItemInstance item : items)
			{
				_items.add(item);
				onRestoreItem(item);
			}
			items = _itemsDAO.getItemsByOwnerIdAndLoc(ownerId, getEquipLocation());
			for (ItemInstance item : items)
			{
				_items.add(item);
				onRestoreItem(item);
				if (ItemFunctions.checkIfCanEquip(getActor(), item) == null)
				{
					setPaperdollItem(item.getEquipSlot(), item);
				}
			}
		}
		finally
		{
			writeUnlock();
		}
		refreshWeight();
	}
	
	/**
	 * Method store.
	 */
	@Override
	public void store()
	{
		writeLock();
		try
		{
			_itemsDAO.update(_items);
		}
		finally
		{
			writeUnlock();
		}
	}
	
	/**
	 * Method validateItems.
	 */
	public void validateItems()
	{
		for (ItemInstance item : _paperdoll)
		{
			if ((item != null) && ((ItemFunctions.checkIfCanEquip(getActor(), item) != null) || !item.getTemplate().testCondition(getActor(), item)))
			{
				unEquipItem(item);
			}
		}
	}
}
