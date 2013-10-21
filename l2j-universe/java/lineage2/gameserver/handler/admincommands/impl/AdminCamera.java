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
import lineage2.gameserver.model.base.InvisibleType;
import lineage2.gameserver.network.serverpackets.CameraMode;
import lineage2.gameserver.network.serverpackets.SpecialCamera;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AdminCamera implements IAdminCommandHandler
{
	/**
	 * @author Mobius
	 */
	private static enum Commands
	{
		/**
		 * Field admin_freelook.
		 */
		admin_freelook,
		/**
		 * Field admin_cinematic.
		 */
		admin_cinematic
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
			case admin_freelook:
			{
				if (fullString.length() > 15)
				{
					fullString = fullString.substring(15);
				}
				else
				{
					activeChar.sendMessage("Usage: //freelook 1 or //freelook 0");
					return false;
				}
				int mode = Integer.parseInt(fullString);
				if (mode == 1)
				{
					activeChar.setInvisibleType(InvisibleType.NORMAL);
					activeChar.setIsInvul(true);
					activeChar.setNoChannel(-1);
					activeChar.setFlying(true);
				}
				else
				{
					activeChar.setInvisibleType(InvisibleType.NONE);
					activeChar.setIsInvul(false);
					activeChar.setNoChannel(0);
					activeChar.setFlying(false);
				}
				activeChar.sendPacket(new CameraMode(mode));
				break;
			}
			case admin_cinematic:
			{
				int id = Integer.parseInt(wordList[1]);
				int dist = Integer.parseInt(wordList[2]);
				int yaw = Integer.parseInt(wordList[3]);
				int pitch = Integer.parseInt(wordList[4]);
				int time = Integer.parseInt(wordList[5]);
				int duration = Integer.parseInt(wordList[6]);
				activeChar.sendPacket(new SpecialCamera(id, dist, yaw, pitch, time, duration));
				break;
			}
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
