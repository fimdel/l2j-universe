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

import org.apache.commons.lang3.math.NumberUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AdminKill implements IAdminCommandHandler
{
	/**
	 * @author Mobius
	 */
	private static enum Commands
	{
		/**
		 * Field admin_kill.
		 */
		admin_kill,
		/**
		 * Field admin_damage.
		 */
		admin_damage,
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
		if (!activeChar.getPlayerAccess().CanEditNPC)
		{
			return false;
		}
		switch (command)
		{
			case admin_kill:
				if (wordList.length == 1)
				{
					handleKill(activeChar);
				}
				else
				{
					handleKill(activeChar, wordList[1]);
				}
				break;
			case admin_damage:
				handleDamage(activeChar, NumberUtils.toInt(wordList[1], 1));
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
	 * Method handleKill.
	 * @param activeChar Player
	 */
	private void handleKill(Player activeChar)
	{
		handleKill(activeChar, null);
	}
	
	/**
	 * Method handleKill.
	 * @param activeChar Player
	 * @param player String
	 */
	private void handleKill(Player activeChar, String player)
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
					if (!character.isDoor())
					{
						character.doDie(activeChar);
					}
				}
				activeChar.sendMessage("Killed within " + radius + " unit radius.");
				return;
			}
		}
		if ((obj != null) && obj.isCreature())
		{
			Creature target = (Creature) obj;
			target.doDie(activeChar);
		}
		else
		{
			activeChar.sendPacket(Msg.INVALID_TARGET);
		}
	}
	
	/**
	 * Method handleDamage.
	 * @param activeChar Player
	 * @param damage int
	 */
	private void handleDamage(Player activeChar, int damage)
	{
		GameObject obj = activeChar.getTarget();
		if (obj == null)
		{
			activeChar.sendPacket(Msg.SELECT_TARGET);
			return;
		}
		if (!obj.isCreature())
		{
			activeChar.sendPacket(Msg.INVALID_TARGET);
			return;
		}
		Creature cha = (Creature) obj;
		cha.reduceCurrentHp(damage, 0, activeChar, null, true, true, false, false, false, false, true);
		activeChar.sendMessage("You gave " + damage + " damage to " + cha.getName() + ".");
	}
}
