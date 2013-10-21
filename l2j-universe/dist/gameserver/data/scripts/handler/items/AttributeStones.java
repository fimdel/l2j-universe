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

import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.model.Playable;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.model.items.etcitems.AttributeStoneManager;
import lineage2.gameserver.network.serverpackets.ExChooseInventoryAttributeItem;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AttributeStones extends ScriptItemHandler
{
	/**
	 * Method useItem.
	 * @param playable Playable
	 * @param item ItemInstance
	 * @param ctrl boolean
	 * @return boolean * @see lineage2.gameserver.handler.items.IItemHandler#useItem(Playable, ItemInstance, boolean)
	 */
	@Override
	public boolean useItem(Playable playable, ItemInstance item, boolean ctrl)
	{
		if ((playable == null) || !playable.isPlayer())
		{
			return false;
		}
		final Player player = (Player) playable;
		if (player.getPrivateStoreType() != Player.STORE_PRIVATE_NONE)
		{
			player.sendPacket(Msg.YOU_CANNOT_ADD_ELEMENTAL_POWER_WHILE_OPERATING_A_PRIVATE_STORE_OR_PRIVATE_WORKSHOP);
			return false;
		}
		if (player.getEnchantScroll() != null)
		{
			return false;
		}
		player.setEnchantScroll(item);
		player.sendPacket(Msg.PLEASE_SELECT_ITEM_TO_ADD_ELEMENTAL_POWER);
		player.sendPacket(new ExChooseInventoryAttributeItem(player, item));
		return true;
	}
	
	/**
	 * Method getItemIds.
	 * @return int[] * @see lineage2.gameserver.handler.items.IItemHandler#getItemIds()
	 */
	@Override
	public final int[] getItemIds()
	{
		return AttributeStoneManager.getAttributeStoneIds();
	}
}
