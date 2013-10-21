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
package lineage2.gameserver.instancemanager.commission;

import java.util.Collection;

import lineage2.commons.dao.JdbcEntityState;
import lineage2.gameserver.model.items.ItemContainer;
import lineage2.gameserver.model.items.ItemInstance;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class CommissionItemContainer extends ItemContainer
{
	/**
	 * Method getItemLocation.
	 * @return ItemInstance.ItemLocation
	 */
	public ItemInstance.ItemLocation getItemLocation()
	{
		return ItemInstance.ItemLocation.COMMISSION;
	}
	
	/**
	 * Method onAddItem.
	 * @param item ItemInstance
	 */
	@Override
	protected void onAddItem(ItemInstance item)
	{
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
		writeLock();
		try
		{
			Collection<ItemInstance> items = _itemsDAO.getItemsByLoc(getItemLocation());
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
