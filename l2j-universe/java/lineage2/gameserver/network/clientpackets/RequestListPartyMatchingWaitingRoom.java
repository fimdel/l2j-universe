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
import lineage2.gameserver.network.serverpackets.ExListPartyMatchingWaitingRoom;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestListPartyMatchingWaitingRoom extends L2GameClientPacket
{
	/**
	 * Field _classes. Field _page. Field _maxLevel. Field _minLevel.
	 */
	private int _minLevel, _maxLevel, _page, _classes[];
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_page = readD();
		_minLevel = readD();
		_maxLevel = readD();
		int size = readD();
		if ((size > Byte.MAX_VALUE) || (size < 0))
		{
			size = 0;
		}
		_classes = new int[size];
		for (int i = 0; i < size; i++)
		{
			_classes[i] = readD();
		}
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
		activeChar.sendPacket(new ExListPartyMatchingWaitingRoom(activeChar, _minLevel, _maxLevel, _page, _classes));
	}
}
