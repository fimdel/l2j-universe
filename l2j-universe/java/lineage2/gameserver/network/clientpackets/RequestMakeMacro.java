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

import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.actor.instances.player.Macro;
import lineage2.gameserver.model.actor.instances.player.Macro.L2MacroCmd;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestMakeMacro extends L2GameClientPacket
{
	/**
	 * Field _macro.
	 */
	private Macro _macro;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		int _id = readD();
		String _name = readS(32);
		String _desc = readS(64);
		String _acronym = readS(4);
		int _icon = readC();
		int _count = readC();
		if (_count > 12)
		{
			_count = 12;
		}
		L2MacroCmd[] commands = new L2MacroCmd[_count];
		for (int i = 0; i < _count; i++)
		{
			int entry = readC();
			int type = readC();
			int d1 = readD();
			int d2 = readC();
			String command = readS().replace(";", "").replace(",", "");
			commands[i] = new L2MacroCmd(entry, type, d1, d2, command);
		}
		_macro = new Macro(_id, _icon, _name, _desc, _acronym, commands);
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
		if (activeChar.getMacroses().getAllMacroses().length > 48)
		{
			activeChar.sendPacket(Msg.YOU_MAY_CREATE_UP_TO_48_MACROS);
			return;
		}
		if (_macro.name.length() == 0)
		{
			activeChar.sendPacket(Msg.ENTER_THE_NAME_OF_THE_MACRO);
			return;
		}
		if (_macro.descr.length() > 32)
		{
			activeChar.sendPacket(Msg.MACRO_DESCRIPTIONS_MAY_CONTAIN_UP_TO_32_CHARACTERS);
			return;
		}
		activeChar.registerMacro(_macro);
	}
}
