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

import lineage2.commons.dao.JdbcEntityState;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.base.Element;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.model.items.PcInventory;
import lineage2.gameserver.model.items.etcitems.AttributeStoneInfo;
import lineage2.gameserver.model.items.etcitems.AttributeStoneManager;
import lineage2.gameserver.network.serverpackets.ActionFail;
import lineage2.gameserver.network.serverpackets.ExAttributeEnchantResult;
import lineage2.gameserver.network.serverpackets.InventoryUpdate;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.templates.item.ItemTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestEnchantItemAttribute extends L2GameClientPacket
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
		if (_objectId == -1)
		{
			activeChar.setEnchantScroll(null);
			activeChar.sendPacket(Msg.ELEMENTAL_POWER_ENCHANCER_USAGE_HAS_BEEN_CANCELLED);
			return;
		}
		if (activeChar.isActionsDisabled())
		{
			activeChar.sendActionFailed();
			return;
		}
		if (activeChar.isInStoreMode())
		{
			activeChar.sendPacket(Msg.YOU_CANNOT_ADD_ELEMENTAL_POWER_WHILE_OPERATING_A_PRIVATE_STORE_OR_PRIVATE_WORKSHOP, ActionFail.STATIC);
			return;
		}
		if (activeChar.isInTrade())
		{
			activeChar.sendActionFailed();
			return;
		}
		PcInventory inventory = activeChar.getInventory();
		ItemInstance itemToEnchant = inventory.getItemByObjectId(_objectId);
		ItemInstance stone = activeChar.getEnchantScroll();
		activeChar.setEnchantScroll(null);
		if ((itemToEnchant == null) || (stone == null))
		{
			activeChar.sendActionFailed();
			return;
		}
		ItemTemplate item = itemToEnchant.getTemplate();
		if (!itemToEnchant.canBeEnchanted() || (item.getCrystalType().cry < ItemTemplate.CRYSTAL_S))
		{
			activeChar.sendPacket(Msg.INAPPROPRIATE_ENCHANT_CONDITIONS, ActionFail.STATIC);
			return;
		}
		if ((itemToEnchant.getLocation() != ItemInstance.ItemLocation.INVENTORY) && (itemToEnchant.getLocation() != ItemInstance.ItemLocation.PAPERDOLL))
		{
			activeChar.sendPacket(Msg.INAPPROPRIATE_ENCHANT_CONDITIONS, ActionFail.STATIC);
			return;
		}
		if (itemToEnchant.isStackable() || ((stone = inventory.getItemByObjectId(stone.getObjectId())) == null))
		{
			activeChar.sendActionFailed();
			return;
		}
		AttributeStoneInfo asi = AttributeStoneManager.getStoneInfo(stone.getItemId());
		if (asi == null)
		{
			return;
		}
		Element element = itemToEnchant.isArmor() ? Element.getReverseElement(asi.getElement()) : asi.getElement();
		if (itemToEnchant.isArmor())
		{
			if (itemToEnchant.getAttributeElementValue(Element.getReverseElement(element), false) != 0)
			{
				activeChar.sendPacket(Msg.ANOTHER_ELEMENTAL_POWER_HAS_ALREADY_BEEN_ADDED_THIS_ELEMENTAL_POWER_CANNOT_BE_ADDED, ActionFail.STATIC);
				return;
			}
		}
		else if (itemToEnchant.isWeapon())
		{
			if ((itemToEnchant.getAttributeElement() != Element.NONE) && (itemToEnchant.getAttributeElement() != element))
			{
				activeChar.sendPacket(Msg.ANOTHER_ELEMENTAL_POWER_HAS_ALREADY_BEEN_ADDED_THIS_ELEMENTAL_POWER_CANNOT_BE_ADDED, ActionFail.STATIC);
				return;
			}
		}
		else
		{
			activeChar.sendPacket(Msg.INAPPROPRIATE_ENCHANT_CONDITIONS, ActionFail.STATIC);
			return;
		}
		if (item.isUnderwear() || item.isCloak() || item.isBracelet() || item.isBelt() || !item.isAttributable())
		{
			activeChar.sendPacket(Msg.INAPPROPRIATE_ENCHANT_CONDITIONS, ActionFail.STATIC);
			return;
		}
		int maxValue = itemToEnchant.isWeapon() ? asi.getMaxWeapon() : asi.getMaxArmor();
		int minValue = itemToEnchant.isWeapon() ? asi.getMinWeapon() : asi.getMinArmor();
		int currentValue = itemToEnchant.getAttributeElementValue(element, false);
		if ((currentValue >= maxValue) || (currentValue < minValue))
		{
			activeChar.sendPacket(Msg.ELEMENTAL_POWER_ENCHANCER_USAGE_HAS_BEEN_CANCELLED, ActionFail.STATIC);
			return;
		}
		if (itemToEnchant.getOwnerId() != activeChar.getObjectId())
		{
			activeChar.sendPacket(Msg.INAPPROPRIATE_ENCHANT_CONDITIONS, ActionFail.STATIC);
			return;
		}
		if (!inventory.destroyItem(stone, 1L))
		{
			activeChar.sendActionFailed();
			return;
		}
		if (Rnd.chance(asi.getChance()))
		{
			if (itemToEnchant.getEnchantLevel() == 0)
			{
				SystemMessage sm = new SystemMessage(SystemMessage.S2_ELEMENTAL_POWER_HAS_BEEN_ADDED_SUCCESSFULLY_TO_S1);
				sm.addItemName(itemToEnchant.getItemId());
				sm.addItemName(stone.getItemId());
				activeChar.sendPacket(sm);
			}
			else
			{
				SystemMessage sm = new SystemMessage(SystemMessage.S3_ELEMENTAL_POWER_HAS_BEEN_ADDED_SUCCESSFULLY_TO__S1S2);
				sm.addNumber(itemToEnchant.getEnchantLevel());
				sm.addItemName(itemToEnchant.getItemId());
				sm.addItemName(stone.getItemId());
				activeChar.sendPacket(sm);
			}
			int value = itemToEnchant.isWeapon() ? asi.getIncWeapon() : asi.getIncArmor();
			if ((itemToEnchant.getAttributeElementValue(element, false) == 0) && itemToEnchant.isWeapon())
			{
				value = 20;
			}
			boolean equipped = itemToEnchant.isEquipped();
			if (equipped)
			{
				activeChar.getInventory().isRefresh = true;
				activeChar.getInventory().unEquipItem(itemToEnchant);
			}
			itemToEnchant.setAttributeElement(element, itemToEnchant.getAttributeElementValue(element, false) + value);
			itemToEnchant.setJdbcState(JdbcEntityState.UPDATED);
			itemToEnchant.update();
			if (equipped)
			{
				activeChar.getInventory().equipItem(itemToEnchant);
				activeChar.getInventory().isRefresh = false;
			}
			activeChar.sendPacket(new InventoryUpdate().addModifiedItem(itemToEnchant));
			activeChar.sendPacket(new ExAttributeEnchantResult(value));
		}
		else
		{
			activeChar.sendPacket(Msg.YOU_HAVE_FAILED_TO_ADD_ELEMENTAL_POWER);
		}
		activeChar.setEnchantScroll(null);
		activeChar.updateStats();
	}
}
