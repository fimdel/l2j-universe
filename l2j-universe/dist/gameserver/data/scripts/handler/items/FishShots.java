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
import lineage2.gameserver.network.serverpackets.ExAutoSoulShot;
import lineage2.gameserver.network.serverpackets.MagicSkillUse;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.templates.item.WeaponTemplate;
import lineage2.gameserver.templates.item.WeaponTemplate.WeaponType;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class FishShots extends ScriptItemHandler
{
	/**
	 * Field _itemIds.
	 */
	private static final int[] _itemIds =
	{
		6535,
		6536,
		6537,
		6538,
		6539,
		6540
	};
	/**
	 * Field _skillIds.
	 */
	private static final int[] _skillIds =
	{
		2181,
		2182,
		2183,
		2184,
		2185,
		2186
	};
	
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
		final int FishshotId = item.getItemId();
		boolean isAutoSoulShot = false;
		if (player.getAutoSoulShot().contains(FishshotId))
		{
			isAutoSoulShot = true;
		}
		final ItemInstance weaponInst = player.getActiveWeaponInstance();
		final WeaponTemplate weaponItem = player.getActiveWeaponItem();
		if ((weaponInst == null) || (weaponItem.getItemType() != WeaponType.ROD))
		{
			if (!isAutoSoulShot)
			{
				player.sendPacket(Msg.CANNOT_USE_SOULSHOTS);
			}
			return false;
		}
		if (weaponInst.getChargedFishshot())
		{
			return false;
		}
		if (item.getCount() < 1)
		{
			if (isAutoSoulShot)
			{
				player.removeAutoSoulShot(FishshotId);
				player.sendPacket(new ExAutoSoulShot(FishshotId, false), new SystemMessage(SystemMessage.THE_AUTOMATIC_USE_OF_S1_WILL_NOW_BE_CANCELLED).addString(item.getName()));
				return false;
			}
			player.sendPacket(Msg.NOT_ENOUGH_SPIRITSHOTS);
			return false;
		}
		final int grade = weaponItem.getCrystalType().externalOrdinal;
		if (((grade == 0) && (FishshotId != 6535)) || ((grade == 1) && (FishshotId != 6536)) || ((grade == 2) && (FishshotId != 6537)) || ((grade == 3) && (FishshotId != 6538)) || ((grade == 4) && (FishshotId != 6539)) || ((grade == 5) && (FishshotId != 6540)))
		{
			if (isAutoSoulShot)
			{
				return false;
			}
			player.sendPacket(Msg.THIS_FISHING_SHOT_IS_NOT_FIT_FOR_THE_FISHING_POLE_CRYSTAL);
			return false;
		}
		if (player.getInventory().destroyItem(item, 1L))
		{
			weaponInst.setChargedFishshot(true);
			player.sendPacket(Msg.POWER_OF_MANA_ENABLED);
			player.broadcastPacket(new MagicSkillUse(player, player, _skillIds[grade], 1, 0, 0));
		}
		return true;
	}
	
	/**
	 * Method getItemIds.
	 * @return int[] * @see lineage2.gameserver.handler.items.IItemHandler#getItemIds()
	 */
	@Override
	public int[] getItemIds()
	{
		return _itemIds;
	}
}
