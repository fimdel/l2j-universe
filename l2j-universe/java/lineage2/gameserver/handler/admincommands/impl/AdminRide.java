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
import lineage2.gameserver.tables.PetDataTable;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AdminRide implements IAdminCommandHandler
{
	/**
	 * @author Mobius
	 */
	private static enum Commands
	{
		/**
		 * Field admin_ride.
		 */
		admin_ride,
		/**
		 * Field admin_ride_wyvern.
		 */
		admin_ride_wyvern,
		/**
		 * Field admin_ride_strider.
		 */
		admin_ride_strider,
		/**
		 * Field admin_unride.
		 */
		admin_unride,
		/**
		 * Field admin_wr.
		 */
		admin_wr,
		/**
		 * Field admin_sr.
		 */
		admin_sr,
		/**
		 * Field admin_ur.
		 */
		admin_ur
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
		if (!activeChar.getPlayerAccess().Rider)
		{
			return false;
		}
		switch (command)
		{
			case admin_ride:
				if (activeChar.isMounted() || (activeChar.getSummonList().getPet() != null))
				{
					activeChar.sendMessage("Already Have a Pet or Mounted.");
					return false;
				}
				if (wordList.length != 2)
				{
					activeChar.sendMessage("Incorrect id.");
					return false;
				}
				activeChar.setMount(Integer.parseInt(wordList[1]), 0, 85);
				break;
			case admin_ride_wyvern:
			case admin_wr:
				if (activeChar.isMounted() || (activeChar.getSummonList().getPet() != null))
				{
					activeChar.sendMessage("Already Have a Pet or Mounted.");
					return false;
				}
				activeChar.setMount(PetDataTable.WYVERN_ID, 0, 85);
				break;
			case admin_ride_strider:
			case admin_sr:
				if (activeChar.isMounted() || (activeChar.getSummonList().getPet() != null))
				{
					activeChar.sendMessage("Already Have a Pet or Mounted.");
					return false;
				}
				activeChar.setMount(PetDataTable.STRIDER_WIND_ID, 0, 85);
				break;
			case admin_unride:
			case admin_ur:
				activeChar.setMount(0, 0, 0);
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
