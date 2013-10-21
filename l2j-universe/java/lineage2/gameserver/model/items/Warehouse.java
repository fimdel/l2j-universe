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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import lineage2.commons.dao.JdbcEntityState;
import lineage2.gameserver.dao.ItemsDAO;
import lineage2.gameserver.model.items.ItemInstance.ItemLocation;
import lineage2.gameserver.templates.item.ItemTemplate;
import lineage2.gameserver.templates.item.ItemTemplate.ItemClass;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public abstract class Warehouse extends ItemContainer
{
	/**
	 * Field _itemsDAO.
	 */
	private static final ItemsDAO _itemsDAO = ItemsDAO.getInstance();
	
	/**
	 * @author Mobius
	 */
	public static enum WarehouseType
	{
		/**
		 * Field NONE.
		 */
		NONE,
		/**
		 * Field PRIVATE.
		 */
		PRIVATE,
		/**
		 * Field CLAN.
		 */
		CLAN,
		/**
		 * Field CASTLE.
		 */
		CASTLE,
		/**
		 * Field FREIGHT.
		 */
		FREIGHT
	}
	
	/**
	 * @author Mobius
	 */
	public static class ItemClassComparator implements Comparator<ItemInstance>
	{
		/**
		 * Field instance.
		 */
		private static final Comparator<ItemInstance> instance = new ItemClassComparator();
		
		/**
		 * Method getInstance.
		 * @return Comparator<ItemInstance>
		 */
		public static Comparator<ItemInstance> getInstance()
		{
			return instance;
		}
		
		/**
		 * Method compare.
		 * @param o1 ItemInstance
		 * @param o2 ItemInstance
		 * @return int
		 */
		@Override
		public int compare(ItemInstance o1, ItemInstance o2)
		{
			if ((o1 == null) || (o2 == null))
			{
				return 0;
			}
			int diff = o1.getItemClass().ordinal() - o2.getItemClass().ordinal();
			if (diff == 0)
			{
				diff = o1.getCrystalType().ordinal() - o2.getCrystalType().ordinal();
			}
			if (diff == 0)
			{
				diff = o1.getItemId() - o2.getItemId();
			}
			if (diff == 0)
			{
				diff = o1.getEnchantLevel() - o2.getEnchantLevel();
			}
			return diff;
		}
	}
	
	/**
	 * Field _ownerId.
	 */
	protected final int _ownerId;
	
	/**
	 * Constructor for Warehouse.
	 * @param ownerId int
	 */
	protected Warehouse(int ownerId)
	{
		_ownerId = ownerId;
	}
	
	/**
	 * Method getOwnerId.
	 * @return int
	 */
	public int getOwnerId()
	{
		return _ownerId;
	}
	
	/**
	 * Method getItemLocation.
	 * @return ItemLocation
	 */
	public abstract ItemLocation getItemLocation();
	
	/**
	 * Method getItems.
	 * @param itemClass ItemClass
	 * @return ItemInstance[]
	 */
	public ItemInstance[] getItems(ItemClass itemClass)
	{
		List<ItemInstance> result = new ArrayList<>();
		readLock();
		try
		{
			ItemInstance item;
			for (int i = 0; i < _items.size(); i++)
			{
				item = _items.get(i);
				if (((itemClass == null) || (itemClass == ItemClass.ALL)) || (item.getItemClass() == itemClass))
				{
					result.add(item);
				}
			}
		}
		finally
		{
			readUnlock();
		}
		return result.toArray(new ItemInstance[result.size()]);
	}
	
	/**
	 * Method getCountOfAdena.
	 * @return long
	 */
	public long getCountOfAdena()
	{
		return getCountOf(ItemTemplate.ITEM_ID_ADENA);
	}
	
	/**
	 * Method onAddItem.
	 * @param item ItemInstance
	 */
	@Override
	protected void onAddItem(ItemInstance item)
	{
		item.setOwnerId(getOwnerId());
		item.setLocation(getItemLocation());
		item.setLocData(0);
		if (item.getJdbcState().isSavable())
		{
			item.save();
		}
		else
		{
			item.setJdbcState(JdbcEntityState.UPDATED);
			item.update();
		}
	}
	
	/**
	 * Method onModifyItem.
	 * @param item ItemInstance
	 */
	@Override
	protected void onModifyItem(ItemInstance item)
	{
		item.setJdbcState(JdbcEntityState.UPDATED);
		item.update();
	}
	
	/**
	 * Method onRemoveItem.
	 * @param item ItemInstance
	 */
	@Override
	protected void onRemoveItem(ItemInstance item)
	{
		item.setLocData(-1);
	}
	
	/**
	 * Method onDestroyItem.
	 * @param item ItemInstance
	 */
	@Override
	protected void onDestroyItem(ItemInstance item)
	{
		item.setCount(0L);
		item.delete();
	}
	
	/**
	 * Method restore.
	 */
	public void restore()
	{
		final int ownerId = getOwnerId();
		writeLock();
		try
		{
			Collection<ItemInstance> items = _itemsDAO.getItemsByOwnerIdAndLoc(ownerId, getItemLocation());
			for (ItemInstance item : items)
			{
				_items.add(item);
			}
		}
		finally
		{
			writeUnlock();
		}
	}
}
