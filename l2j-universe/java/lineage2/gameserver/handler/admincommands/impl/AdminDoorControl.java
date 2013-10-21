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

import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.handler.admincommands.IAdminCommandHandler;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.instances.DoorInstance;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AdminDoorControl implements IAdminCommandHandler
{
	/**
	 * @author Mobius
	 */
	private static enum Commands
	{
		/**
		 * Field admin_open.
		 */
		admin_open,
		/**
		 * Field admin_close.
		 */
		admin_close,
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
		if (!activeChar.getPlayerAccess().Door)
		{
			return false;
		}
		GameObject target;
		switch (command)
		{
			case admin_open:
				if (wordList.length > 1)
				{
					target = World.getAroundObjectById(activeChar, Integer.parseInt(wordList[1]));
				}
				else
				{
					target = activeChar.getTarget();
				}
				if ((target != null) && target.isDoor())
				{
					((DoorInstance) target).openMe();
				}
				else
				{
					activeChar.sendPacket(Msg.INVALID_TARGET);
				}
				break;
			case admin_close:
				if (wordList.length > 1)
				{
					target = World.getAroundObjectById(activeChar, Integer.parseInt(wordList[1]));
				}
				else
				{
					target = activeChar.getTarget();
				}
				if ((target != null) && target.isDoor())
				{
					((DoorInstance) target).closeMe();
				}
				else
				{
					activeChar.sendPacket(Msg.INVALID_TARGET);
				}
				break;
		}
		return true;
	}
	
	/**
	 * Method getAdminCommandEnum.
	 * @return Enum<?>[] * @see lineage2.gameserver.handler.admincommands.IAdminCommandHandler#getAdminCommandEnum()
	 */
	@Override
	public Enum<?>[] getAdminCommandEnum()
	{
		return Commands.values();
	}
}
