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

import lineage2.gameserver.Config;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.instances.PetInstance;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.utils.ItemFunctions;

import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestPetUseItem extends L2GameClientPacket
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
			activeChar.sendPacket(Msg.YOU_CANNOT_DO_THAT_WHILE_FISHING);
			return;
		}
		activeChar.setActive();
		PetInstance pet = activeChar.getSummonList().getPet();
		if (pet == null)
		{
			return;
		}
		ItemInstance item = pet.getInventory().getItemByObjectId(_objectId);
		if ((item == null) || (item.getCount() < 1))
		{
			return;
		}
		if (activeChar.isAlikeDead() || pet.isDead() || pet.isOutOfControl())
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addItemName(item.getItemId()));
			return;
		}
		if (pet.tryFeedItem(item))
		{
			return;
		}
		if (ArrayUtils.contains(Config.ALT_ALLOWED_PET_POTIONS, item.getItemId()))
		{
			Skill[] skills = item.getTemplate().getAttachedSkills();
			if (skills.length > 0)
			{
				for (Skill skill : skills)
				{
					Creature aimingTarget = skill.getAimingTarget(pet, pet.getTarget());
					if (skill.checkCondition(pet, aimingTarget, false, false, true))
					{
						pet.getAI().Cast(skill, aimingTarget, false, false);
					}
				}
			}
			return;
		}
		SystemMessage sm = ItemFunctions.checkIfCanEquip(pet, item);
		if (sm == null)
		{
			if (item.isEquipped())
			{
				pet.getInventory().unEquipItem(item);
			}
			else
			{
				pet.getInventory().equipItem(item);
			}
			pet.broadcastCharInfo();
			return;
		}
		activeChar.sendPacket(sm);
	}
}
