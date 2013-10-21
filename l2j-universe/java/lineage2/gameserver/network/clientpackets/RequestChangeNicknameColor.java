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

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestChangeNicknameColor extends L2GameClientPacket
{
	/**
	 * Field COLORS.
	 */
	private static final int COLORS[] =
	{
		0x9393FF,
		0x7C49FC,
		0x97F8FC,
		0xFA9AEE,
		0xFF5D93,
		0x00FCA0,
		0xA0A601,
		0x7898AF,
		0x486295,
		0x999999
	};
	/**
	 * Field _itemObjectId. Field _colorNum.
	 */
	private int _colorNum, _itemObjectId;
	/**
	 * Field _title.
	 */
	private String _title;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_colorNum = readD();
		_title = readS();
		_itemObjectId = readD();
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
		int storedId = activeChar.getVarInt("NameColorItemOID");
		activeChar.unsetVar("NameColorItemOID");
		if (storedId != _itemObjectId)
		{
			return;
		}
		if ((_colorNum < 0) || (_colorNum >= COLORS.length))
		{
			return;
		}
		ItemInstance item = activeChar.getInventory().getItemByObjectId(_itemObjectId);
		if (item == null)
		{
			return;
		}
		if (activeChar.consumeItem(item.getItemId(), 1))
		{
			activeChar.setTitleColor(COLORS[_colorNum]);
			activeChar.setTitle(_title);
			activeChar.broadcastUserInfo();
		}
	}
}
