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

import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.templates.item.ItemTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
@Deprecated
public class RequestUnEquipItem extends L2GameClientPacket
{
	/**
	 * Field _slot.
	 */
	private int _slot;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_slot = readD();
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
		if (activeChar.isFishing())
		{
			activeChar.sendPacket(Msg.YOU_CANNOT_DO_ANYTHING_ELSE_WHILE_FISHING);
			return;
		}
		if (((_slot == ItemTemplate.SLOT_R_HAND) || (_slot == ItemTemplate.SLOT_L_HAND) || (_slot == ItemTemplate.SLOT_LR_HAND)) && (activeChar.isCursedWeaponEquipped() || (activeChar.getActiveWeaponFlagAttachment() != null)))
		{
			return;
		}
		if (_slot == ItemTemplate.SLOT_R_HAND)
		{
			ItemInstance weapon = activeChar.getActiveWeaponInstance();
			if (weapon == null)
			{
				return;
			}
			activeChar.abortAttack(true, true);
			activeChar.abortCast(true, true);
			activeChar.sendDisarmMessage(weapon);
		}
		activeChar.getInventory().unEquipItemInBodySlot(_slot);
	}
}
