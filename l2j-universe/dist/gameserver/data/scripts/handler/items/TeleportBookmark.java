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
package handler.items;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class TeleportBookmark extends SimpleItemHandler
{
	/**
	 * Field ITEM_IDS.
	 */
	private static final int[] ITEM_IDS = new int[]
	{
		13015
	};
	
	/**
	 * Method getItemIds.
	 * @return int[] * @see lineage2.gameserver.handler.items.IItemHandler#getItemIds()
	 */
	@Override
	public int[] getItemIds()
	{
		return ITEM_IDS;
	}
	
	/**
	 * Method useItemImpl.
	 * @param player Player
	 * @param item ItemInstance
	 * @param ctrl boolean
	 * @return boolean
	 */
	@Override
	protected boolean useItemImpl(Player player, ItemInstance item, boolean ctrl)
	{
		if ((player == null) || (item == null))
		{
			return false;
		}
		if (player.bookmarks.getCapacity() >= 30)
		{
			player.sendPacket(SystemMsg.YOUR_NUMBER_OF_MY_TELEPORTS_SLOTS_HAS_REACHED_ITS_MAXIMUM_LIMIT);
			return false;
		}
		player.getInventory().destroyItem(item, 1);
		player.sendPacket(new SystemMessage2(SystemMsg.S1_HAS_DISAPPEARED).addItemName(item.getItemId()));
		player.bookmarks.setCapacity(player.bookmarks.getCapacity() + 3);
		player.sendPacket(SystemMsg.THE_NUMBER_OF_MY_TELEPORTS_SLOTS_HAS_BEEN_INCREASED);
		return true;
	}
}
