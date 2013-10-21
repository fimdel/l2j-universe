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
import lineage2.gameserver.model.items.etcitems.EnchantScrollInfo;
import lineage2.gameserver.model.items.etcitems.EnchantScrollTarget;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public abstract class AbstractEnchantPacket extends L2GameClientPacket
{
	/**
	 * Method isValidPlayer.
	 * @param player Player
	 * @return boolean
	 */
	protected boolean isValidPlayer(Player player)
	{
		if (player.isActionsDisabled())
		{
			return false;
		}
		if (player.isInTrade())
		{
			return false;
		}
		if (player.isInStoreMode())
		{
			return false;
		}
		return true;
	}
	
	/**
	 * Method checkItem.
	 * @param item ItemInstance
	 * @param esi EnchantScrollInfo
	 * @return boolean
	 */
	protected boolean checkItem(ItemInstance item, EnchantScrollInfo esi)
	{
		if (item.isStackable())
		{
			return false;
		}
		if (item.getTemplate().getItemGrade().externalOrdinal != esi.getGrade().externalOrdinal)
		{
			return false;
		}
		if (item.isArmor() && (esi.getTarget() != EnchantScrollTarget.ARMOR))
		{
			return false;
		}
		if (item.isWeapon() && (esi.getTarget() != EnchantScrollTarget.WEAPON))
		{
			return false;
		}
		if (!item.canBeEnchanted())
		{
			return false;
		}
		if ((item.getLocation() != ItemInstance.ItemLocation.INVENTORY) && (item.getLocation() != ItemInstance.ItemLocation.PAPERDOLL))
		{
			return false;
		}
		return true;
	}
}
