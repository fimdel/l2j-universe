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
import lineage2.gameserver.network.serverpackets.SystemMessage;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Kamaloka extends SimpleItemHandler
{
	/**
	 * Field ITEM_IDS.
	 */
	private static final int[] ITEM_IDS = new int[]
	{
		13010,
		13297,
		20026,
		13011,
		13298,
		20027,
		13012,
		13299,
		20028
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
		final int itemId = item.getItemId();
		switch (itemId)
		{
			case 13010:
			case 13297:
			case 20026:
				useItem(player, item, 1);
				player.removeInstanceReusesByGroupId(1);
				break;
			case 13011:
			case 13298:
			case 20027:
				useItem(player, item, 1);
				player.removeInstanceReusesByGroupId(2);
				break;
			case 13012:
			case 13299:
			case 20028:
				useItem(player, item, 1);
				player.removeInstanceReusesByGroupId(3);
				break;
		}
		player.sendPacket(new SystemMessage(SystemMessage.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addItemName(itemId));
		return false;
	}
}
