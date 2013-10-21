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
import lineage2.gameserver.instancemanager.HellboundManager;
import lineage2.gameserver.model.Player;

import org.apache.commons.lang3.math.NumberUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AdminHellbound implements IAdminCommandHandler
{
	/**
	 * @author Mobius
	 */
	private static enum Commands
	{
		/**
		 * Field admin_hbadd.
		 */
		admin_hbadd,
		/**
		 * Field admin_hbsub.
		 */
		admin_hbsub,
		/**
		 * Field admin_hbset.
		 */
		admin_hbset,
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
		if (!activeChar.getPlayerAccess().Menu)
		{
			return false;
		}
		switch (command)
		{
			case admin_hbadd:
				HellboundManager.addConfidence(Long.parseLong(wordList[1]));
				activeChar.sendMessage("Added " + NumberUtils.toInt(wordList[1], 1) + " to Hellbound confidence");
				activeChar.sendMessage("Hellbound confidence is now " + HellboundManager.getConfidence());
				break;
			case admin_hbsub:
				HellboundManager.reduceConfidence(Long.parseLong(wordList[1]));
				activeChar.sendMessage("Reduced confidence by " + NumberUtils.toInt(wordList[1], 1));
				activeChar.sendMessage("Hellbound confidence is now " + HellboundManager.getConfidence());
				break;
			case admin_hbset:
				HellboundManager.setConfidence(Long.parseLong(wordList[1]));
				activeChar.sendMessage("Hellbound confidence is now " + HellboundManager.getConfidence());
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
