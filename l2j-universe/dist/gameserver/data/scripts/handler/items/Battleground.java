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
import lineage2.gameserver.model.Zone.ZoneType;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.network.serverpackets.MagicSkillUse;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.tables.SkillTable;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Battleground extends SimpleItemHandler
{
	/**
	 * Field ITEM_IDS.
	 */
	private static final int[] ITEM_IDS = new int[]
	{
		10143,
		10144,
		10145,
		10146,
		10147,
		10148,
		10411
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
		if (!player.isInZone(ZoneType.SIEGE))
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
			case 10143:
				for (int skill : new int[]
				{
					2379,
					2380,
					2381,
					2382,
					2383
				})
				{
					player.broadcastPacket(new MagicSkillUse(player, player, skill, 1, 0, 0));
					player.altOnMagicUseTimer(player, SkillTable.getInstance().getInfo(skill, 1));
				}
				break;
			case 10144:
				for (int skill : new int[]
				{
					2379,
					2380,
					2381,
					2384,
					2385
				})
				{
					player.broadcastPacket(new MagicSkillUse(player, player, skill, 1, 0, 0));
					player.altOnMagicUseTimer(player, SkillTable.getInstance().getInfo(skill, 1));
				}
				break;
			case 10145:
				for (int skill : new int[]
				{
					2379,
					2380,
					2381,
					2384,
					2386
				})
				{
					player.broadcastPacket(new MagicSkillUse(player, player, skill, 1, 0, 0));
					player.altOnMagicUseTimer(player, SkillTable.getInstance().getInfo(skill, 1));
				}
				break;
			case 10146:
				for (int skill : new int[]
				{
					2379,
					2380,
					2381,
					2388,
					2383
				})
				{
					player.broadcastPacket(new MagicSkillUse(player, player, skill, 1, 0, 0));
					player.altOnMagicUseTimer(player, SkillTable.getInstance().getInfo(skill, 1));
				}
				break;
			case 10147:
				for (int skill : new int[]
				{
					2379,
					2380,
					2381,
					2389,
					2383
				})
				{
					player.broadcastPacket(new MagicSkillUse(player, player, skill, 1, 0, 0));
					player.altOnMagicUseTimer(player, SkillTable.getInstance().getInfo(skill, 1));
				}
				break;
			case 10148:
				for (int skill : new int[]
				{
					2390,
					2391
				})
				{
					player.broadcastPacket(new MagicSkillUse(player, player, skill, 1, 0, 0));
					player.altOnMagicUseTimer(player, SkillTable.getInstance().getInfo(skill, 1));
				}
				break;
			case 10411:
				for (int skill : new int[]
				{
					2499
				})
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
