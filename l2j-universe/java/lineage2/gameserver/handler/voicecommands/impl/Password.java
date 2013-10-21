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
package lineage2.gameserver.handler.voicecommands.impl;

import lineage2.gameserver.data.htm.HtmCache;
import lineage2.gameserver.handler.voicecommands.IVoicedCommandHandler;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.loginservercon.LoginServerCommunication;
import lineage2.gameserver.network.loginservercon.gspackets.ChangePassword;
import lineage2.gameserver.network.serverpackets.components.CustomMessage;
import lineage2.gameserver.scripts.Functions;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Password extends Functions implements IVoicedCommandHandler
{
	/**
	 * Field _commandList.
	 */
	private final String[] _commandList = new String[]
	{
		"password",
		"check"
	};
	
	/**
	 * Method useVoicedCommand.
	 * @param command String
	 * @param activeChar Player
	 * @param args String
	 * @return boolean * @see lineage2.gameserver.handler.voicecommands.IVoicedCommandHandler#useVoicedCommand(String, Player, String)
	 */
	@Override
	public boolean useVoicedCommand(String command, Player activeChar, String args)
	{
		command = command.intern();
		if (command.equalsIgnoreCase("password"))
		{
			return password(command, activeChar, args);
		}
		if (command.equalsIgnoreCase("check"))
		{
			return check(command, activeChar, args);
		}
		return false;
	}
	
	/**
	 * Method password.
	 * @param command String
	 * @param activeChar Player
	 * @param target String
	 * @return boolean
	 */
	private boolean password(String command, Player activeChar, String target)
	{
		if (command.equals("password"))
		{
			String dialog = HtmCache.getInstance().getNotNull("command/password.html", activeChar);
			show(dialog, activeChar);
			return true;
		}
		return true;
	}
	
	/**
	 * Method getVoicedCommandList.
	 * @return String[] * @see lineage2.gameserver.handler.voicecommands.IVoicedCommandHandler#getVoicedCommandList()
	 */
	@Override
	public String[] getVoicedCommandList()
	{
		return _commandList;
	}
	
	/**
	 * Method check.
	 * @param command String
	 * @param activeChar Player
	 * @param target String
	 * @return boolean
	 */
	private boolean check(String command, Player activeChar, String target)
	{
		String[] parts = target.split(" ");
		if (parts.length != 3)
		{
			show(new CustomMessage("scripts.commands.user.password.IncorrectValues", activeChar), activeChar);
			return false;
		}
		if (!parts[1].equals(parts[2]))
		{
			show(new CustomMessage("scripts.commands.user.password.IncorrectConfirmation", activeChar), activeChar);
			return false;
		}
		if (parts[1].equals(parts[0]))
		{
			show(new CustomMessage("scripts.commands.user.password.NewPassIsOldPass", activeChar), activeChar);
			return false;
		}
		if ((parts[1].length() < 5) || (parts[1].length() > 20))
		{
			show(new CustomMessage("scripts.commands.user.password.IncorrectSize", activeChar), activeChar);
			return false;
		}
		LoginServerCommunication.getInstance().sendPacket(new ChangePassword(activeChar.getAccountName(), parts[0], parts[1], "null"));
		show(new CustomMessage("scripts.commands.user.password.ResultTrue", activeChar), activeChar);
		return true;
	}
}
