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
import lineage2.gameserver.dao.CharacterDAO;
import lineage2.gameserver.network.serverpackets.ExIsCharNameCreatable;
import lineage2.gameserver.utils.Util;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestCharacterNameCreatable extends L2GameClientPacket
{
	/**
	 * Field _C__D0_B0_REQUESTCHARACTERNAMECREATABLE. (value is ""[C] D0:B0 RequestCharacterNameCreatable"")
	 */
	private static final String _C__D0_B0_REQUESTCHARACTERNAMECREATABLE = "[C] D0:B0 RequestCharacterNameCreatable";
	/**
	 * Field _nickname.
	 */
	private String _nickname;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_nickname = readS();
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		if (CharacterDAO.getInstance().accountCharNumber(getClient().getLogin()) >= 8)
		{
			sendPacket(new ExIsCharNameCreatable(ExIsCharNameCreatable.REASON_TOO_MANY_CHARACTERS));
			return;
		}
		if (!Util.isMatchingRegexp(_nickname, Config.CNAME_TEMPLATE))
		{
			sendPacket(new ExIsCharNameCreatable(ExIsCharNameCreatable.REASON_16_ENG_CHARS));
			return;
		}
		if (CharacterDAO.getInstance().getObjectIdByName(_nickname) > 0)
		{
			sendPacket(new ExIsCharNameCreatable(ExIsCharNameCreatable.REASON_NAME_ALREADY_EXISTS));
			return;
		}
		sendPacket(new ExIsCharNameCreatable(ExIsCharNameCreatable.REASON_CREATION_OK));
	}
	
	/**
	 * Method getType.
	 * @return String
	 */
	@Override
	public String getType()
	{
		return _C__D0_B0_REQUESTCHARACTERNAMECREATABLE;
	}
}
