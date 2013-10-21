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

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestSaveInventoryOrder extends L2GameClientPacket
{
	/**
	 * Field _items.
	 */
	int[][] _items;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		int size = readD();
		if (size > 125)
		{
			size = 125;
		}
		if (((size * 8) > _buf.remaining()) || (size < 1))
		{
			_items = null;
			return;
		}
		_items = new int[size][2];
		for (int i = 0; i < size; i++)
		{
			_items[i][0] = readD();
			_items[i][1] = readD();
		}
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		if (_items == null)
		{
			return;
		}
		Player activeChar = getClient().getActiveChar();
		if (activeChar == null)
		{
			return;
		}
		activeChar.getInventory().sort(_items);
	}
}
