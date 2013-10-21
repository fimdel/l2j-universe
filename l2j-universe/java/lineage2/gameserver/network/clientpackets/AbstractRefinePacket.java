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
package lineage2.gameserver.network.clientpackets;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.model.items.etcitems.LifeStoneGrade;
import lineage2.gameserver.model.items.etcitems.LifeStoneInfo;
import lineage2.gameserver.model.items.etcitems.LifeStoneManager;
import lineage2.gameserver.templates.item.ItemTemplate.Grade;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public abstract class AbstractRefinePacket extends L2GameClientPacket
{
	/**
	 * Field GEMSTONE_D. (value is 2130)
	 */
	protected static final int GEMSTONE_D = 2130;
	/**
	 * Field GEMSTONE_C. (value is 2131)
	 */
	protected static final int GEMSTONE_C = 2131;
	/**
	 * Field GEMSTONE_B. (value is 2132)
	 */
	protected static final int GEMSTONE_B = 2132;
	/**
	 * Field GEMSTONE_A. (value is 2133)
	 */
	protected static final int GEMSTONE_A = 2133;
	
	/**
	 * Method isValid.
	 * @param player Player
	 * @param item ItemInstance
	 * @param refinerItem ItemInstance
	 * @param gemStones ItemInstance
	 * @return boolean
	 */
	protected static final boolean isValid(Player player, ItemInstance item, ItemInstance refinerItem, ItemInstance gemStones)
	{
		if (!isValid(player, item, refinerItem))
		{
			return false;
		}
		if (gemStones.getOwnerId() != player.getObjectId())
		{
			return false;
		}
		if (gemStones.getLocation() != ItemInstance.ItemLocation.INVENTORY)
		{
			return false;
		}
		final Grade grade = item.getTemplate().getItemGrade();
		LifeStoneInfo ls = LifeStoneManager.getStoneInfo(refinerItem.getItemId());
		if (getGemStoneId(grade) != gemStones.getItemId())
		{
			return false;
		}
		if (getGemStoneCount(ls.getGrade(), grade) > gemStones.getCount())
		{
			return false;
		}
		return true;
	}
	
	/**
	 * Method isValid.
	 * @param player Player
	 * @param item ItemInstance
	 * @param refinerItem ItemInstance
	 * @return boolean
	 */
	protected static final boolean isValid(Player player, ItemInstance item, ItemInstance refinerItem)
	{
		if (!isValid(player, item))
		{
			return false;
		}
		if (refinerItem.getLocation() != ItemInstance.ItemLocation.INVENTORY)
		{
			return false;
		}
		LifeStoneInfo ls = LifeStoneManager.getStoneInfo(refinerItem.getItemId());
		if (player.getLevel() < ls.getLevel())
		{
			return false;
		}
		if (!item.canBeAugmented(player, ls.getGrade()))
		{
			return false;
		}
		return true;
	}
	
	/**
	 * Method isValid.
	 * @param player Player
	 * @param item ItemInstance
	 * @return boolean
	 */
	protected static final boolean isValid(Player player, ItemInstance item)
	{
		if (!isValid(player))
		{
			return false;
		}
		switch (item.getLocation())
		{
			case INVENTORY:
			case PAPERDOLL:
				break;
			default:
				return false;
		}
		return true;
	}
	
	/**
	 * Method isValid.
	 * @param player Player
	 * @return boolean
	 */
	protected static final boolean isValid(Player player)
	{
		if (player.isActionsDisabled())
		{
			return false;
		}
		if (player.isInStoreMode())
		{
			return false;
		}
		if (player.isInTrade())
		{
			return false;
		}
		if (player.getLevel() < 46)
		{
			return false;
		}
		return true;
	}
	
	/**
	 * Method getGemStoneId.
	 * @param grade Grade
	 * @return int
	 */
	protected static final int getGemStoneId(Grade grade)
	{
		switch (grade)
		{
			case C:
			case B:
				return GEMSTONE_D;
			case A:
			case S:
				return GEMSTONE_C;
			case S80:
			case S84:
				return GEMSTONE_B;
			case R:
			case R95:
			case R99:
				return GEMSTONE_A;
			default:
				return 0;
		}
	}
	
	/**
	 * Method getGemStoneCount.
	 * @param lsGrade LifeStoneGrade
	 * @param itemGrade Grade
	 * @return int
	 */
	protected static final int getGemStoneCount(LifeStoneGrade lsGrade, Grade itemGrade)
	{
		switch (lsGrade)
		{
			case ACCESSORY:
			{
				switch (itemGrade)
				{
					case C:
						return 200;
					case B:
						return 300;
					case A:
						return 200;
					case S:
						return 250;
					case S80:
						return 360;
					case S84:
						return 480;
					case R:
						return 26;
					case R95:
						return 90;
					case R99:
						return 236;
					default:
						return 0;
				}
			}
			case UNDERWEAR:
			{
				return 26;
			}
			default:
			{
				switch (itemGrade)
				{
					case C:
						return 20;
					case B:
						return 30;
					case A:
						return 20;
					case S:
						return 25;
					case S80:
						return 36;
					case S84:
						return 48;
					case R:
						return 13;
					case R95:
						return 15;
					case R99:
						return 118;
					default:
						return 0;
				}
			}
		}
	}
}
