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
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.model.items.PcInventory;
import lineage2.gameserver.network.serverpackets.ExChangeAttributeOk;
import lineage2.gameserver.network.serverpackets.InventoryUpdate;
import lineage2.gameserver.utils.ItemFunctions;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestChangeAttributeItem extends L2GameClientPacket
{
	/**
	 * Field _consumeItemId.
	 */
	public int _consumeItemId;
	/**
	 * Field _itemObjId.
	 */
	public int _itemObjId;
	/**
	 * Field _newElementId.
	 */
	public int _newElementId;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_consumeItemId = readD();
		_itemObjId = readD();
		_newElementId = readD();
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		Player activeChar = (getClient()).getActiveChar();
		if (activeChar == null)
		{
			return;
		}
		PcInventory inventory = activeChar.getInventory();
		ItemInstance _item = inventory.getItemByObjectId(_itemObjId);
		ItemFunctions.removeItem(activeChar, _consumeItemId, 1, true);
		boolean equipped = _item.isEquipped();
		if (equipped)
		{
			activeChar.getInventory().isRefresh = true;
			activeChar.getInventory().unEquipItem(_item);
		}
		Element oldElement = _item.getAttackElement();
		int elementVal = _item.getAttributeElementValue(oldElement, false);
		_item.setAttributeElement(oldElement, 0);
		Element newElement = Element.VALUES[_newElementId];
		_item.setAttributeElement(newElement, _item.getAttributeElementValue(newElement, false) + elementVal);
		_item.setJdbcState(JdbcEntityState.UPDATED);
		_item.update();
		if (equipped)
		{
			activeChar.getInventory().equipItem(_item);
			activeChar.getInventory().isRefresh = false;
		}
		activeChar.sendPacket(new InventoryUpdate().addModifiedItem(_item));
		activeChar.sendPacket(new ExChangeAttributeOk());
	}
}
