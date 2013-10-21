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
public class AdminHeal implements IAdminCommandHandler
{
	/**
	 * @author Mobius
	 */
	private static enum Commands
	{
		/**
		 * Field admin_heal.
		 */
		admin_heal
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
		if (!activeChar.getPlayerAccess().Heal)
		{
			return false;
		}
		switch (command)
		{
			case admin_heal:
				if (wordList.length == 1)
				{
					handleRes(activeChar);
				}
				else
				{
					handleRes(activeChar, wordList[1]);
				}
				break;
		}
		return true;
	}
	
	/**
	 * Method getAdminCommandEnum.
	 * @return Enum[] * @see lineage2.gameserver.handler.admincommands.IAdminCommandHandler#getAdminCommandEnum()
	 */
	@Override
	public Commands[] getAdminCommandEnum()
	{
		return Commands.values();
	}
	
	/**
	 * Method handleRes.
	 * @param activeChar Player
	 */
	private void handleRes(Player activeChar)
	{
		handleRes(activeChar, null);
	}
	
	/**
	 * Method handleRes.
	 * @param activeChar Player
	 * @param player String
	 */
	private void handleRes(Player activeChar, String player)
	{
		GameObject obj = activeChar.getTarget();
		if (player != null)
		{
			Player plyr = World.getPlayer(player);
			if (plyr != null)
			{
				obj = plyr;
			}
			else
			{
				int radius = Math.max(Integer.parseInt(player), 100);
				for (Creature character : activeChar.getAroundCharacters(radius, 200))
				{
					character.setCurrentHpMp(character.getMaxHp(), character.getMaxMp());
					if (character.isPlayer())
					{
						character.setCurrentCp(character.getMaxCp());
					}
				}
				activeChar.sendMessage("Healed within " + radius + " unit radius.");
				return;
			}
		}
		if (obj == null)
		{
			obj = activeChar;
		}
		if (obj instanceof Creature)
		{
			Creature target = (Creature) obj;
			target.setCurrentHpMp(target.getMaxHp(), target.getMaxMp());
			if (target.isPlayer())
			{
				target.setCurrentCp(target.getMaxCp());
			}
		}
		else
		{
			activeChar.sendPacket(Msg.INVALID_TARGET);
		}
	}
}
