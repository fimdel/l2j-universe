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

import lineage2.commons.dao.JdbcEntityState;
import lineage2.gameserver.model.Player;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class PcRefund extends ItemContainer
{
	/**
	 * Constructor for PcRefund.
	 * @param player Player
	 */
	public PcRefund(Player player)
	{
	}
	
	/**
	 * Method onAddItem.
	 * @param item ItemInstance
	 */
	@Override
	protected void onAddItem(ItemInstance item)
	{
		item.setLocation(ItemInstance.ItemLocation.VOID);
		if (item.getJdbcState().isPersisted())
		{
			item.setJdbcState(JdbcEntityState.UPDATED);
			item.update();
		}
		if (_items.size() > 12)
		{
			destroyItem(_items.remove(0));
		}
	}
	
	/**
	 * Method onModifyItem.
	 * @param item ItemInstance
	 */
	@Override
	protected void onModifyItem(ItemInstance item)
	{
	}
	
	/**
	 * Method onRemoveItem.
	 * @param item ItemInstance
	 */
	@Override
	protected void onRemoveItem(ItemInstance item)
	{
	}
	
	/**
	 * Method onDestroyItem.
	 * @param item ItemInstance
	 */
	@Override
	protected void onDestroyItem(ItemInstance item)
	{
		item.setCount(0);
		item.delete();
	}
	
	/**
	 * Method clear.
	 */
	@Override
	public void clear()
	{
		writeLock();
		try
		{
			_itemsDAO.delete(_items);
			_items.clear();
		}
		finally
		{
			writeUnlock();
		}
	}
}
