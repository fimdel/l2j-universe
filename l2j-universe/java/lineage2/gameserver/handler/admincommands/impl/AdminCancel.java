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
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.World;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AdminCancel implements IAdminCommandHandler
{
	/**
	 * @author Mobius
	 */
	private static enum Commands
	{
		/**
		 * Field admin_cancel.
		 */
		admin_cancel
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
		if (!activeChar.getPlayerAccess().CanEditChar)
		{
			return false;
		}
		switch (command)
		{
			case admin_cancel:
				handleCancel(activeChar, wordList.length > 1 ? wordList[1] : null);
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
	
	/**
	 * Method handleCancel.
	 * @param activeChar Player
	 * @param targetName String
	 */
	private void handleCancel(Player activeChar, String targetName)
	{
		GameObject obj = activeChar.getTarget();
		if (targetName != null)
		{
			Player plyr = World.getPlayer(targetName);
			if (plyr != null)
			{
				obj = plyr;
			}
			else
			{
				try
				{
					int radius = Math.max(Integer.parseInt(targetName), 100);
					for (Creature character : activeChar.getAroundCharacters(radius, 200))
					{
						character.getEffectList().stopAllEffects();
					}
					activeChar.sendMessage("Apply Cancel within " + radius + " unit radius.");
					return;
				}
				catch (NumberFormatException e)
				{
					activeChar.sendMessage("Enter valid player name or radius");
					return;
				}
			}
		}
		if (obj == null)
		{
			obj = activeChar;
		}
		if (obj.isCreature())
		{
			((Creature) obj).getEffectList().stopAllEffects();
		}
		else
		{
			activeChar.sendPacket(Msg.INVALID_TARGET);
		}
	}
}
