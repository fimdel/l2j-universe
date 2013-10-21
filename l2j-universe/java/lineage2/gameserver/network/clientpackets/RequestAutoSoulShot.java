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

import lineage2.gameserver.handler.items.IItemHandler;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.network.serverpackets.ExAutoSoulShot;
import lineage2.gameserver.network.serverpackets.SystemMessage;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestAutoSoulShot extends L2GameClientPacket
{
	/**
	 * Field _itemId.
	 */
	private int _itemId;
	/**
	 * Field _type.
	 */
	private boolean _type;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_itemId = readD();
		_type = readD() == 1;
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
		if ((activeChar.getPrivateStoreType() != Player.STORE_PRIVATE_NONE) || activeChar.isDead())
		{
			return;
		}
		ItemInstance item = activeChar.getInventory().getItemByItemId(_itemId);
		if (item == null)
		{
			return;
		}
		if (_type)
		{
			activeChar.addAutoSoulShot(_itemId);
			activeChar.sendPacket(new ExAutoSoulShot(_itemId, true));
			activeChar.sendPacket(new SystemMessage(SystemMessage.THE_USE_OF_S1_WILL_NOW_BE_AUTOMATED).addString(item.getName()));
			IItemHandler handler = item.getTemplate().getHandler();
			handler.useItem(activeChar, item, false);
			return;
		}
		activeChar.removeAutoSoulShot(_itemId);
		activeChar.sendPacket(new ExAutoSoulShot(_itemId, false));
		activeChar.sendPacket(new SystemMessage(SystemMessage.THE_AUTOMATIC_USE_OF_S1_WILL_NOW_BE_CANCELLED).addString(item.getName()));
	}
}
