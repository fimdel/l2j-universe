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

import lineage2.gameserver.Config;
import lineage2.gameserver.handler.voicecommands.IVoicedCommandHandler;
import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.tables.FakePlayersTable;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Online extends Functions implements IVoicedCommandHandler
{
	/**
	 * Field _commandList.
	 */
	private final String[] _commandList =
	{
		"online"
	};
	
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
	 * Method useVoicedCommand.
	 * @param command String
	 * @param activeChar Player
	 * @param target String
	 * @return boolean * @see lineage2.gameserver.handler.voicecommands.IVoicedCommandHandler#useVoicedCommand(String, Player, String)
	 */
	@Override
	public boolean useVoicedCommand(String command, Player activeChar, String target)
	{
		if (!Config.ALLOW_TOTAL_ONLINE)
		{
			return false;
		}
		if (command.equals("online"))
		{
			int i = 0;
			int j = 0;
			for (Player player : GameObjectsStorage.getAllPlayersForIterate())
			{
				i++;
				if (player.isInOfflineMode())
				{
					j++;
				}
			}
			i += FakePlayersTable.getFakePlayersCount();
			activeChar.sendMessage("There are " + i + " online players.");
			activeChar.sendMessage("Of them " + j + " are offline shops.");
			return true;
		}
		return false;
	}
}
