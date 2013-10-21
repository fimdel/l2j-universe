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
package lineage2.gameserver.handler.admincommands.impl;

import lineage2.gameserver.handler.admincommands.IAdminCommandHandler;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.World;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AdminIP implements IAdminCommandHandler
{
	/**
	 * @author Mobius
	 */
	private enum Commands
	{
		/**
		 * Field admin_charip.
		 */
		admin_charip
	}
	
	/**
	 * Method useAdminCommand.
	 * @param comm Enum<?>
	 * @param wordList String[]
	 * @param fullString String
	 * @param activeChar Player
	 * @return boolean * @see lineage2.gameserver.handler.admincommands.IAdminCommandHandler#useAdminCommand(Enum<?>, String[], String, Player)
	 */
	@Override
	public boolean useAdminCommand(Enum<?> comm, String[] wordList, String fullString, Player activeChar)
	{
		Commands command = (Commands) comm;
		if (!activeChar.getPlayerAccess().CanBan)
		{
			return false;
		}
		switch (command)
		{
			case admin_charip:
				if (wordList.length != 2)
				{
					activeChar.sendMessage("Command syntax: //charip <char_name>");
					activeChar.sendMessage(" Gets character's IP.");
					break;
				}
				Player pl = World.getPlayer(wordList[1]);
				if (pl == null)
				{
					activeChar.sendMessage("Character " + wordList[1] + " not found.");
					break;
				}
				String ip_adr = pl.getIP();
				if (ip_adr.equalsIgnoreCase("<not connected>"))
				{
					activeChar.sendMessage("Character " + wordList[1] + " not found.");
					break;
				}
				activeChar.sendMessage("Character's IP: " + ip_adr);
				break;
		}
		return true;
	}
	
	/**
	 * Method getAdminCommandEnum.
	 * @return Enum[] * @see lineage2.gameserver.handler.admincommands.IAdminCommandHandler#getAdminCommandEnum()
	 */
	@Override
	public Enum<?>[] getAdminCommandEnum()
	{
		return Commands.values();
	}
}
