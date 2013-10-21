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
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.base.Experience;
import lineage2.gameserver.network.serverpackets.RadarControl;
import lineage2.gameserver.network.serverpackets.components.CustomMessage;
import lineage2.gameserver.scripts.Functions;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Help extends Functions implements IVoicedCommandHandler
{
	/**
	 * Field _commandList.
	 */
	private final String[] _commandList = new String[]
	{
		"help",
		"exp",
		"whereis"
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
		if (command.equalsIgnoreCase("help"))
		{
			return help(command, activeChar, args);
		}
		if (command.equalsIgnoreCase("whereis"))
		{
			return whereis(command, activeChar, args);
		}
		if (command.equalsIgnoreCase("exp"))
		{
			return exp(command, activeChar, args);
		}
		return false;
	}
	
	/**
	 * Method exp.
	 * @param command String
	 * @param activeChar Player
	 * @param args String
	 * @return boolean
	 */
	private boolean exp(String command, Player activeChar, String args)
	{
		if (activeChar.getLevel() >= (activeChar.isBaseClassActive() ? Experience.getMaxSubLevel() : Experience.getMaxLevel()))
		{
			activeChar.sendMessage(new CustomMessage("voicedcommandhandlers.Help.MaxLevel", activeChar));
		}
		else
		{
			long exp = Experience.LEVEL[activeChar.getLevel() + 1] - activeChar.getExp();
			activeChar.sendMessage(new CustomMessage("voicedcommandhandlers.Help.ExpLeft", activeChar).addNumber(exp));
		}
		return true;
	}
	
	/**
	 * Method whereis.
	 * @param command String
	 * @param activeChar Player
	 * @param args String
	 * @return boolean
	 */
	private boolean whereis(String command, Player activeChar, String args)
	{
		Player friend = World.getPlayer(args);
		if (friend == null)
		{
			return false;
		}
		if ((friend.getParty() == activeChar.getParty()) || (friend.getClan() == activeChar.getClan()))
		{
			RadarControl rc = new RadarControl(0, 1, friend.getLoc());
			activeChar.sendPacket(rc);
			return true;
		}
		return false;
	}
	
	/**
	 * Method help.
	 * @param command String
	 * @param activeChar Player
	 * @param args String
	 * @return boolean
	 */
	private boolean help(String command, Player activeChar, String args)
	{
		String dialog = HtmCache.getInstance().getNotNull("command/help.htm", activeChar);
		show(dialog, activeChar);
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
}
