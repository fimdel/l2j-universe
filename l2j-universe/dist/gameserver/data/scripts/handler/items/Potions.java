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
import lineage2.gameserver.network.serverpackets.MagicSkillUse;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.tables.SkillTable;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Potions extends SimpleItemHandler
{
	/**
	 * Field ITEM_IDS.
	 */
	private static final int[] ITEM_IDS = new int[]
	{
		7906,
		7907,
		7908,
		7909,
		7910,
		7911,
		14612
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
		if (player.isInOlympiadMode())
		{
			player.sendPacket(new SystemMessage(SystemMessage.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addItemName(itemId));
			return false;
		}
		if (!useItem(player, item, 1))
		{
			return false;
		}
		switch (itemId)
		{
			case 7906:
				player.broadcastPacket(new MagicSkillUse(player, player, 2248, 1, 0, 0));
				player.altOnMagicUseTimer(player, SkillTable.getInstance().getInfo(2248, 1));
				break;
			case 7907:
				player.broadcastPacket(new MagicSkillUse(player, player, 2249, 1, 0, 0));
				player.altOnMagicUseTimer(player, SkillTable.getInstance().getInfo(2249, 1));
				break;
			case 7908:
				player.broadcastPacket(new MagicSkillUse(player, player, 2250, 1, 0, 0));
				player.altOnMagicUseTimer(player, SkillTable.getInstance().getInfo(2250, 1));
				break;
			case 7909:
				player.broadcastPacket(new MagicSkillUse(player, player, 2251, 1, 0, 0));
				player.altOnMagicUseTimer(player, SkillTable.getInstance().getInfo(2251, 1));
				break;
			case 7910:
				player.broadcastPacket(new MagicSkillUse(player, player, 2252, 1, 0, 0));
				player.altOnMagicUseTimer(player, SkillTable.getInstance().getInfo(2252, 1));
				break;
			case 7911:
				player.broadcastPacket(new MagicSkillUse(player, player, 2253, 1, 0, 0));
				player.altOnMagicUseTimer(player, SkillTable.getInstance().getInfo(2253, 1));
				break;
			case 14612:
				player.broadcastPacket(new MagicSkillUse(player, player, 23017, 1, 0, 0));
				player.altOnMagicUseTimer(player, SkillTable.getInstance().getInfo(23017, 1));
				break;
			default:
				return false;
		}
		return true;
	}
}
