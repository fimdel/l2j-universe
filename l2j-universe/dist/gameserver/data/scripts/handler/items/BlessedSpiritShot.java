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

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class BlessedSpiritShot extends ScriptItemHandler
{
	/**
	 * Field _itemIds.
	 */
	private static final int[] _itemIds =
	{
		3947,
		3948,
		3949,
		3950,
		3951,
		3952,
		22072,
		22073,
		22074,
		22075,
		22076,
		19442,
		33794
	};
	/**
	 * Field _skillIds.
	 */
	private static final int[] _skillIds =
	{
		2061,
		2160,
		2161,
		2162,
		2163,
		2164,
		9195
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
		final ItemInstance weaponInst = player.getActiveWeaponInstance();
		final WeaponTemplate weaponItem = player.getActiveWeaponItem();
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
				player.sendPacket(Msg.CANNOT_USE_SPIRITSHOTS);
			}
			return false;
		}
		if (weaponInst.getChargedSpiritshot() == ItemInstance.CHARGED_BLESSED_SPIRITSHOT)
		{
			return false;
		}
		final int spiritshotId = item.getItemId();
		final int grade = weaponItem.getCrystalType().externalOrdinal;
		final int blessedsoulSpiritConsumption = weaponItem.getSpiritShotCount();
		if (blessedsoulSpiritConsumption == 0)
		{
			if (isAutoSoulShot)
			{
				player.removeAutoSoulShot(SoulshotId);
				player.sendPacket(new ExAutoSoulShot(SoulshotId, false), new SystemMessage(SystemMessage.THE_AUTOMATIC_USE_OF_S1_WILL_NOW_BE_CANCELLED).addItemName(spiritshotId));
				return false;
			}
			player.sendPacket(Msg.CANNOT_USE_SPIRITSHOTS);
			return false;
		}
		if (((grade == 0) && (spiritshotId != 3947)) || ((grade == 1) && (spiritshotId != 3948) && (spiritshotId != 22072)) || ((grade == 2) && (spiritshotId != 3949) && (spiritshotId != 22073)) || ((grade == 3) && (spiritshotId != 3950) && (spiritshotId != 22074)) || ((grade == 4) && (spiritshotId != 3951) && (spiritshotId != 22075)) || ((grade == 5) && (spiritshotId != 3952) && (spiritshotId != 22076)) || ((grade == 6) && (spiritshotId != 19442)))
		{
			if (isAutoSoulShot)
			{
				return false;
			}
			player.sendPacket(Msg.SPIRITSHOT_DOES_NOT_MATCH_WEAPON_GRADE);
			return false;
		}
		if (!player.getInventory().destroyItem(item, blessedsoulSpiritConsumption))
		{
			if (isAutoSoulShot)
			{
				player.removeAutoSoulShot(SoulshotId);
				player.sendPacket(new ExAutoSoulShot(SoulshotId, false), new SystemMessage(SystemMessage.THE_AUTOMATIC_USE_OF_S1_WILL_NOW_BE_CANCELLED).addItemName(spiritshotId));
				return false;
			}
			player.sendPacket(Msg.NOT_ENOUGH_SPIRITSHOTS);
			return false;
		}
		weaponInst.setChargedSpiritshot(ItemInstance.CHARGED_BLESSED_SPIRITSHOT);
		player.sendPacket(Msg.POWER_OF_MANA_ENABLED);
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
