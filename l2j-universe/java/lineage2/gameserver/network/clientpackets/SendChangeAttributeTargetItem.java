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
import lineage2.gameserver.network.serverpackets.ExChangeAttributeInfo;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class SendChangeAttributeTargetItem extends L2GameClientPacket
{
	/**
	 * Field _crystalItemId.
	 */
	public int _crystalItemId;
	/**
	 * Field _itemObjId.
	 */
	public int _itemObjId;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_crystalItemId = readD();
		_itemObjId = readD();
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
		ItemInstance item = activeChar.getInventory().getItemByObjectId(_itemObjId);
		if ((item == null) || !item.isWeapon())
		{
			activeChar.sendActionFailed();
			return;
		}
		activeChar.sendPacket(new ExChangeAttributeInfo(_crystalItemId, item.getItemId()));
	}
}
