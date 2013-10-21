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

import java.util.ArrayList;
import java.util.List;

import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.items.CrystallizationItem;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.model.items.etcitems.CrystallizationManager;
import lineage2.gameserver.network.serverpackets.ExGetCrystalizingEstimation;
import lineage2.gameserver.templates.item.ItemTemplate.Grade;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestCrystallizeEstimate extends L2GameClientPacket
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
		Player player = getClient().getActiveChar();
		if (player == null)
		{
			return;
		}
		if (player.isActionsDisabled())
		{
			player.sendActionFailed();
			return;
		}
		if (player.isInStoreMode())
		{
			player.sendPacket(Msg.WHILE_OPERATING_A_PRIVATE_STORE_OR_WORKSHOP_YOU_CANNOT_DISCARD_DESTROY_OR_TRADE_AN_ITEM);
			return;
		}
		if (player.isInTrade())
		{
			player.sendActionFailed();
			return;
		}
		ItemInstance item = player.getInventory().getItemByObjectId(_objectId);
		if (item == null)
		{
			player.sendActionFailed();
			return;
		}
		if (item.isHeroWeapon())
		{
			player.sendPacket(Msg.HERO_WEAPONS_CANNOT_BE_DESTROYED);
			return;
		}
		if (!item.canBeCrystallized(player))
		{
			player.sendActionFailed();
			return;
		}
		if (player.isInStoreMode())
		{
			player.sendPacket(Msg.WHILE_OPERATING_A_PRIVATE_STORE_OR_WORKSHOP_YOU_CANNOT_DISCARD_DESTROY_OR_TRADE_AN_ITEM);
			return;
		}
		if (player.isFishing())
		{
			player.sendPacket(Msg.YOU_CANNOT_DO_THAT_WHILE_FISHING);
			return;
		}
		if (player.isInTrade())
		{
			player.sendActionFailed();
			return;
		}
		int crystalAmount = item.getTemplate().getCrystalCount();
		int crystalId = item.getTemplate().getCrystalType().cry;
		int level = player.getSkillLevel(Skill.SKILL_CRYSTALLIZE);
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
			player.sendPacket(Msg.CANNOT_CRYSTALLIZE_CRYSTALLIZATION_SKILL_LEVEL_TOO_LOW);
			player.sendActionFailed();
			return;
		}
		List<CrystallizationItem> temp = new ArrayList<>();
		temp.add(new CrystallizationItem(crystalId, crystalAmount, 100.0D));
		if (((item.getTemplate().getItemGrade() == Grade.R) || (item.getTemplate().getItemGrade() == Grade.R95) || (item.getTemplate().getItemGrade() == Grade.R99)) && (CrystallizationManager.isItemExistInTable(item)))
		{
			temp.addAll(CrystallizationManager.getProductsForItem(item));
		}
		player.sendPacket(new ExGetCrystalizingEstimation(temp));
	}
}
