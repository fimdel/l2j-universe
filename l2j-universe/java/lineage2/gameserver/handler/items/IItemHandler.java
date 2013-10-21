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
package lineage2.gameserver.handler.items;

import lineage2.gameserver.model.Playable;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.Log;

import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public interface IItemHandler
{
	/**
	 * Field NULL.
	 */
	public static final IItemHandler NULL = new IItemHandler()
	{
		@Override
		public boolean useItem(Playable playable, ItemInstance item, boolean ctrl)
		{
			return false;
		}
		
		@Override
		public void dropItem(Player player, ItemInstance item, long count, Location loc)
		{
			if (item.isEquipped())
			{
				player.getInventory().unEquipItem(item);
				player.sendUserInfo();
			}
			item = player.getInventory().removeItemByObjectId(item.getObjectId(), count);
			if (item == null)
			{
				player.sendActionFailed();
				return;
			}
			Log.LogItem(player, Log.Drop, item);
			item.dropToTheGround(player, loc);
			player.disableDrop(1000);
			player.sendChanges();
		}
		
		@Override
		public boolean pickupItem(Playable playable, ItemInstance item)
		{
			return true;
		}
		
		@Override
		public int[] getItemIds()
		{
			return ArrayUtils.EMPTY_INT_ARRAY;
		}
	};
	
	/**
	 * Method useItem.
	 * @param playable Playable
	 * @param item ItemInstance
	 * @param ctrl boolean
	 * @return boolean
	 */
	public boolean useItem(Playable playable, ItemInstance item, boolean ctrl);
	
	/**
	 * Method dropItem.
	 * @param player Player
	 * @param item ItemInstance
	 * @param count long
	 * @param loc Location
	 */
	public void dropItem(Player player, ItemInstance item, long count, Location loc);
	
	/**
	 * Method pickupItem.
	 * @param playable Playable
	 * @param item ItemInstance
	 * @return boolean
	 */
	public boolean pickupItem(Playable playable, ItemInstance item);
	
	/**
	 * Method getItemIds.
	 * @return int[]
	 */
	public int[] getItemIds();
}
