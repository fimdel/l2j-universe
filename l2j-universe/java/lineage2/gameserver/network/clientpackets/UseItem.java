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
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.network.serverpackets.ExUseSharedGroupItem;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.skills.TimeStamp;
import lineage2.gameserver.tables.PetDataTable;

/**
 */
public class UseItem extends L2GameClientPacket
{
	/**
	 * Field _objectId.
	 */
	private int _objectId;
	/**
	 * Field _ctrlPressed.
	 */
	private boolean _ctrlPressed;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_objectId = readD();
		_ctrlPressed = readD() == 1;
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
		activeChar.setActive();
		ItemInstance item = activeChar.getInventory().getItemByObjectId(_objectId);
		if (item == null)
		{
			activeChar.sendActionFailed();
			return;
		}
		int itemId = item.getItemId();
		if (activeChar.isInStoreMode())
		{
			if (PetDataTable.isPetControlItem(item))
			{
				activeChar.sendPacket(SystemMsg.YOU_CANNOT_SUMMON_DURING_A_TRADE_OR_WHILE_USING_A_PRIVATE_STORE);
			}
			else
			{
				activeChar.sendPacket(SystemMsg.YOU_MAY_NOT_USE_ITEMS_IN_A_PRIVATE_STORE_OR_PRIVATE_WORK_SHOP);
			}
			return;
		}
		if (activeChar.isFishing() && ((itemId < 6535) || (itemId > 6540)))
		{
			activeChar.sendPacket(SystemMsg.YOU_CANNOT_DO_THAT_WHILE_FISHING_2);
			return;
		}
		if (activeChar.isSharedGroupDisabled(item.getTemplate().getReuseGroup()))
		{
			activeChar.sendReuseMessage(item);
			return;
		}
		if (!item.getTemplate().testCondition(activeChar, item))
		{
			return;
		}
		if (activeChar.getInventory().isLockedItem(item))
		{
			return;
		}
		if (item.getTemplate().isForPet())
		{
			activeChar.sendPacket(SystemMsg.YOU_MAY_NOT_EQUIP_A_PET_ITEM);
			return;
		}
		if (Config.ALT_IMPROVED_PETS_LIMITED_USE && activeChar.isMageClass() && (item.getItemId() == 10311))
		{
			activeChar.sendPacket(new SystemMessage2(SystemMsg.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addItemName(itemId));
			return;
		}
		if (Config.ALT_IMPROVED_PETS_LIMITED_USE && !activeChar.isMageClass() && (item.getItemId() == 10313))
		{
			activeChar.sendPacket(new SystemMessage2(SystemMsg.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addItemName(itemId));
			return;
		}
		if (activeChar.isOutOfControl())
		{
			activeChar.sendActionFailed();
			return;
		}
		int[] IDENTIFY_CRISIS =
		{
			1570
		};
		if (activeChar.getEffectList().containEffectFromSkills(IDENTIFY_CRISIS))
		{
			activeChar.sendPacket(SystemMsg.YOU_CAN_NOT_CHANGE_CLASS_DUE_TO_DISRUPTION_OF_THE_IDENTIFICATION);
			activeChar.sendActionFailed();
			return;
		}
		boolean success = item.getTemplate().getHandler().useItem(activeChar, item, _ctrlPressed);
		if (success)
		{
			long nextTimeUse = item.getTemplate().getReuseType().next(item);
			if (nextTimeUse > System.currentTimeMillis())
			{
				TimeStamp timeStamp = new TimeStamp(item.getItemId(), nextTimeUse, item.getTemplate().getReuseDelay());
				activeChar.addSharedGroupReuse(item.getTemplate().getReuseGroup(), timeStamp);
				if (item.getTemplate().getReuseDelay() > 0)
				{
					activeChar.sendPacket(new ExUseSharedGroupItem(item.getTemplate().getDisplayReuseGroup(), timeStamp));
				}
			}
		}
		activeChar.getListeners().onUseItem(item); // DynamicQuest
	}
}
