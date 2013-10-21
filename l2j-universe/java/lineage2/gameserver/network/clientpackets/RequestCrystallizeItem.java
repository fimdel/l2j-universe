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

import lineage2.commons.util.Rnd;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.items.CrystallizationItem;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.model.items.etcitems.CrystallizationManager;
import lineage2.gameserver.templates.item.ItemTemplate.Grade;
import lineage2.gameserver.utils.ItemFunctions;
import lineage2.gameserver.utils.Log;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestCrystallizeItem extends L2GameClientPacket
{
	/**
	 * Field _objectId.
	 */
	private int _objectId;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_objectId = readD();
		readQ();
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if (activeChar == null)
		{
			return;
		}
		if (activeChar.isActionsDisabled())
		{
			activeChar.sendActionFailed();
			return;
		}
		if (activeChar.isInStoreMode())
		{
			activeChar.sendPacket(Msg.WHILE_OPERATING_A_PRIVATE_STORE_OR_WORKSHOP_YOU_CANNOT_DISCARD_DESTROY_OR_TRADE_AN_ITEM);
			return;
		}
		if (activeChar.isInTrade())
		{
			activeChar.sendActionFailed();
			return;
		}
		ItemInstance item = activeChar.getInventory().getItemByObjectId(_objectId);
		if (item == null)
		{
			activeChar.sendActionFailed();
			return;
		}
		if (item.isHeroWeapon())
		{
			activeChar.sendPacket(Msg.HERO_WEAPONS_CANNOT_BE_DESTROYED);
			return;
		}
		if (!item.canBeCrystallized(activeChar))
		{
			activeChar.sendActionFailed();
			return;
		}
		if (activeChar.isInStoreMode())
		{
			activeChar.sendPacket(Msg.WHILE_OPERATING_A_PRIVATE_STORE_OR_WORKSHOP_YOU_CANNOT_DISCARD_DESTROY_OR_TRADE_AN_ITEM);
			return;
		}
		if (activeChar.isFishing())
		{
			activeChar.sendPacket(Msg.YOU_CANNOT_DO_THAT_WHILE_FISHING);
			return;
		}
		if (activeChar.isInTrade())
		{
			activeChar.sendActionFailed();
			return;
		}
		int crystalAmount = item.getTemplate().getCrystalCount();
		int crystalId = item.getTemplate().getCrystalType().cry;
		int level = activeChar.getSkillLevel(Skill.SKILL_CRYSTALLIZE);
		boolean canCrystallize = true;
		Grade itemGrade = item.getTemplate().getItemGrade();
		switch (itemGrade)
		{
			case D:
			{
				if (level < 1)
				{
					canCrystallize = false;
				}
				break;
			}
			case C:
			{
				if (level < 2)
				{
					canCrystallize = false;
				}
				break;
			}
			case B:
			{
				if (level < 3)
				{
					canCrystallize = false;
				}
				break;
			}
			case A:
			{
				if (level < 4)
				{
					canCrystallize = false;
				}
				break;
			}
			case S:
			{
				if (level < 5)
				{
					canCrystallize = false;
				}
				break;
			}
			case S80:
			{
				if (level < 5)
				{
					canCrystallize = false;
				}
				break;
			}
			case S84:
			{
				if (level < 5)
				{
					canCrystallize = false;
				}
				break;
			}
			case R:
			{
				if (level < 6)
				{
					canCrystallize = false;
				}
				break;
			}
			case R95:
			{
				if (level < 6)
				{
					canCrystallize = false;
				}
				break;
			}
			case R99:
			{
				if (level < 6)
				{
					canCrystallize = false;
				}
				break;
			}
			default:
				break;
		}
		if (!canCrystallize)
		{
			activeChar.sendPacket(Msg.CANNOT_CRYSTALLIZE_CRYSTALLIZATION_SKILL_LEVEL_TOO_LOW);
			activeChar.sendActionFailed();
			return;
		}
		if (((item.getTemplate().getItemGrade() == Grade.R) || (item.getTemplate().getItemGrade() == Grade.R95) || (item.getTemplate().getItemGrade() == Grade.R99)) && (CrystallizationManager.isItemExistInTable(item)))
		{
			for (CrystallizationItem itemD : CrystallizationManager.getProductsForItem(item))
			{
				if (Rnd.chance(itemD.getChance()))
				{
					ItemFunctions.addItem(activeChar, itemD.getItemId(), itemD.getCount(), true);
				}
			}
		}
		Log.LogItem(activeChar, Log.Crystalize, item);
		if (!activeChar.getInventory().destroyItemByObjectId(_objectId, 1L))
		{
			activeChar.sendActionFailed();
			return;
		}
		activeChar.sendPacket(Msg.THE_ITEM_HAS_BEEN_SUCCESSFULLY_CRYSTALLIZED);
		ItemFunctions.addItem(activeChar, crystalId, crystalAmount, true);
		activeChar.sendChanges();
	}
}
