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
public class Cocktails extends SimpleItemHandler
{
	/**
	 * Field ITEM_IDS.
	 */
	private static final int[] ITEM_IDS = new int[]
	{
		10178,
		15356,
		20393,
		10179,
		15357,
		20394,
		14739,
		32316,
		33862,
		33766
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
	 * Field bless_list.
	 */
	private static final int[] bless_list =
	{
		11517,
		11520,
		11519,
		11522,
		11521,
	};
	/**
	 * Field sweet_list.
	 */
	private static final int[] sweet_list =
	{
		2404,
		2405,
		2406,
		2407,
		2408,
		2409,
		2410,
		2411,
		2412,
		2413,
	};
	/**
	 * Field fresh_list.
	 */
	private static final int[] fresh_list =
	{
		2414,
		2411,
		2415,
		2405,
		2406,
		2416,
		2417,
		2418,
		2419,
	};
	/**
	 * Field milk_list.
	 */
	private static final int[] milk_list =
	{
		2873,
		2874,
		2875,
		2876,
		2877,
		2878,
		2879,
		2885,
		2886,
		2887,
		2888,
		2889,
		2890,
	};
	
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
			case 10178:
			case 15356:
			case 20393:
				for (int skill : sweet_list)
				{
					player.broadcastPacket(new MagicSkillUse(player, player, skill, 1, 0, 0));
					player.altOnMagicUseTimer(player, SkillTable.getInstance().getInfo(skill, 1));
				}
				break;
			case 10179:
			case 15357:
			case 20394:
				for (int skill : fresh_list)
				{
					player.broadcastPacket(new MagicSkillUse(player, player, skill, 1, 0, 0));
					player.altOnMagicUseTimer(player, SkillTable.getInstance().getInfo(skill, 1));
				}
				break;
			case 32316:
			case 33862:
			case 33766:
				for (int skill : bless_list)
				{
					player.broadcastPacket(new MagicSkillUse(player, player, skill, 1, 0, 0));
					player.altOnMagicUseTimer(player, SkillTable.getInstance().getInfo(skill, 1));
				}
				break;
			case 14739:
				player.broadcastPacket(new MagicSkillUse(player, player, 2873, 1, 0, 0));
				player.altOnMagicUseTimer(player, SkillTable.getInstance().getInfo(2891, 6));
				for (int skill : milk_list)
				{
					player.broadcastPacket(new MagicSkillUse(player, player, skill, 1, 0, 0));
					player.altOnMagicUseTimer(player, SkillTable.getInstance().getInfo(skill, 1));
				}
				break;
			default:
				return false;
		}
		return true;
	}
}
