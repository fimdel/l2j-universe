/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package handler.items;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.model.Playable;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.network.serverpackets.ExAutoSoulShot;
import lineage2.gameserver.network.serverpackets.MagicSkillUse;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.stats.Stats;
import lineage2.gameserver.templates.item.WeaponTemplate;
import lineage2.gameserver.templates.item.WeaponTemplate.WeaponType;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class SoulShots extends ScriptItemHandler
{
	/**
	 * Field _itemIds.
	 */
	private static final int[] _itemIds =
	{
		5789,
		1835,
		1463,
		1464,
		1465,
		1466,
		1467,
		13037,
		13045,
		13055,
		22082,
		22083,
		22084,
		22085,
		22086,
		17754,
		33780
	};
	/**
	 * Field _skillIds.
	 */
	private static final int[] _skillIds =
	{
		2039,
		2150,
		2151,
		2152,
		2153,
		2154,
		9193
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
		final WeaponTemplate weaponItem = player.getActiveWeaponItem();
		final ItemInstance weaponInst = player.getActiveWeaponInstance();
		final int SoulshotId = item.getItemId();
		boolean isAutoSoulShot = false;
		if (player.getAutoSoulShot().contains(SoulshotId))
		{
			isAutoSoulShot = true;
		}
		if (weaponInst == null)
		{
			if (!isAutoSoulShot)
			{
				player.sendPacket(Msg.CANNOT_USE_SOULSHOTS);
			}
			return false;
		}
		if (weaponInst.getChargedSoulshot() != ItemInstance.CHARGED_NONE)
		{
			return false;
		}
		final int grade = weaponItem.getCrystalType().externalOrdinal;
		int soulShotConsumption = weaponItem.getSoulShotCount();
		if (soulShotConsumption == 0)
		{
			if (isAutoSoulShot)
			{
				player.removeAutoSoulShot(SoulshotId);
				player.sendPacket(new ExAutoSoulShot(SoulshotId, false), new SystemMessage(SystemMessage.THE_AUTOMATIC_USE_OF_S1_WILL_NOW_BE_CANCELLED).addItemName(SoulshotId));
				return false;
			}
			player.sendPacket(Msg.CANNOT_USE_SOULSHOTS);
			return false;
		}
		if (((grade == 0) && (SoulshotId != 5789) && (SoulshotId != 1835)) || ((grade == 1) && (SoulshotId != 1463) && (SoulshotId != 22082) && (SoulshotId != 13037)) || ((grade == 2) && (SoulshotId != 1464) && (SoulshotId != 22083) && (SoulshotId != 13045)) || ((grade == 3) && (SoulshotId != 1465) && (SoulshotId != 22084)) || ((grade == 4) && (SoulshotId != 1466) && (SoulshotId != 22085) && (SoulshotId != 13055)) || ((grade == 5) && (SoulshotId != 1467) && (SoulshotId != 22086)) || ((grade == 6) && (SoulshotId != 17754) && (SoulshotId != 33780)))
		{
			if (isAutoSoulShot)
			{
				return false;
			}
			player.sendPacket(Msg.SOULSHOT_DOES_NOT_MATCH_WEAPON_GRADE);
			return false;
		}
		if ((weaponItem.getItemType() == WeaponType.BOW) || (weaponItem.getItemType() == WeaponType.CROSSBOW))
		{
			final int newSS = (int) player.calcStat(Stats.SS_USE_BOW, soulShotConsumption, null, null);
			if ((newSS < soulShotConsumption) && Rnd.chance(player.calcStat(Stats.SS_USE_BOW_CHANCE, soulShotConsumption, null, null)))
			{
				soulShotConsumption = newSS;
			}
		}
		if (!player.getInventory().destroyItem(item, soulShotConsumption))
		{
			player.sendPacket(Msg.NOT_ENOUGH_SOULSHOTS);
			return false;
		}
		weaponInst.setChargedSoulshot(ItemInstance.CHARGED_SOULSHOT);
		player.sendPacket(Msg.POWER_OF_THE_SPIRITS_ENABLED);
		player.broadcastPacket(new MagicSkillUse(player, player, _skillIds[grade], 1, 0, 0));
		return true;
	}
	
	/**
	 * Method getItemIds.
	 * @return int[] * @see lineage2.gameserver.handler.items.IItemHandler#getItemIds()
	 */
	@Override
	public final int[] getItemIds()
	{
		return _itemIds;
	}
}
