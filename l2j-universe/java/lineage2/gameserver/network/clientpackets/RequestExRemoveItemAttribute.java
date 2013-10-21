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
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.base.Element;
import lineage2.gameserver.model.items.ItemAttributes;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.model.items.PcInventory;
import lineage2.gameserver.network.serverpackets.ActionFail;
import lineage2.gameserver.network.serverpackets.ExBaseAttributeCancelResult;
import lineage2.gameserver.network.serverpackets.ExShowBaseAttributeCancelWindow;
import lineage2.gameserver.network.serverpackets.InventoryUpdate;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestExRemoveItemAttribute extends L2GameClientPacket
{
	/**
	 * Field _objectId.
	 */
	private int _objectId;
	/**
	 * Field _attributeId.
	 */
	private int _attributeId;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_objectId = readD();
		_attributeId = readD();
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
		if (activeChar.isActionsDisabled() || activeChar.isInStoreMode() || activeChar.isInTrade())
		{
			activeChar.sendActionFailed();
			return;
		}
		PcInventory inventory = activeChar.getInventory();
		ItemInstance itemToUnnchant = inventory.getItemByObjectId(_objectId);
		if (itemToUnnchant == null)
		{
			activeChar.sendActionFailed();
			return;
		}
		ItemAttributes set = itemToUnnchant.getAttributes();
		Element element = Element.getElementById(_attributeId);
		if ((element == Element.NONE) || (set.getValue(element) <= 0))
		{
			activeChar.sendPacket(new ExBaseAttributeCancelResult(false, itemToUnnchant, element), ActionFail.STATIC);
			return;
		}
		if (!activeChar.reduceAdena(ExShowBaseAttributeCancelWindow.getAttributeRemovePrice(itemToUnnchant), true))
		{
			activeChar.sendPacket(new ExBaseAttributeCancelResult(false, itemToUnnchant, element), SystemMsg.YOU_DO_NOT_HAVE_ENOUGH_ADENA, ActionFail.STATIC);
			return;
		}
		boolean equipped = itemToUnnchant.isEquipped();
		if (equipped)
		{
			activeChar.getInventory().unEquipItem(itemToUnnchant);
		}
		itemToUnnchant.setAttributeElement(element, 0);
		itemToUnnchant.setJdbcState(JdbcEntityState.UPDATED);
		itemToUnnchant.update();
		if (equipped)
		{
			activeChar.getInventory().equipItem(itemToUnnchant);
		}
		activeChar.sendPacket(new InventoryUpdate().addModifiedItem(itemToUnnchant));
		activeChar.sendPacket(new ExBaseAttributeCancelResult(true, itemToUnnchant, element));
		activeChar.updateStats();
	}
}
