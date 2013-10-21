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

import java.util.StringTokenizer;

import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.handler.admincommands.IAdminCommandHandler;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.World;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.utils.AdminFunctions;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
@SuppressWarnings("unused")
public class AdminMenu implements IAdminCommandHandler
{
	/**
	 * @author Mobius
	 */
	private static enum Commands
	{
		/**
		 * Field admin_char_manage.
		 */
		admin_char_manage,
		/**
		 * Field admin_teleport_character_to_menu.
		 */
		admin_teleport_character_to_menu,
		/**
		 * Field admin_recall_char_menu.
		 */
		admin_recall_char_menu,
		/**
		 * Field admin_goto_char_menu.
		 */
		admin_goto_char_menu,
		/**
		 * Field admin_kick_menu.
		 */
		admin_kick_menu,
		/**
		 * Field admin_kill_menu.
		 */
		admin_kill_menu,
		/**
		 * Field admin_ban_menu.
		 */
		admin_ban_menu,
		/**
		 * Field admin_unban_menu.
		 */
		admin_unban_menu
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
		if (fullString.startsWith("admin_teleport_character_to_menu"))
		{
			String[] data = fullString.split(" ");
			if (data.length == 5)
			{
				String playerName = data[1];
				Player player = World.getPlayer(playerName);
				if (player != null)
				{
					teleportCharacter(player, new Location(Integer.parseInt(data[2]), Integer.parseInt(data[3]), Integer.parseInt(data[4])), activeChar);
				}
			}
		}
		else if (fullString.startsWith("admin_recall_char_menu"))
		{
			try
			{
				String targetName = fullString.substring(23);
				Player player = World.getPlayer(targetName);
				teleportCharacter(player, activeChar.getLoc(), activeChar);
			}
			catch (StringIndexOutOfBoundsException e)
			{
			}
		}
		else if (fullString.startsWith("admin_goto_char_menu"))
		{
			try
			{
				String targetName = fullString.substring(21);
				Player player = World.getPlayer(targetName);
				teleportToCharacter(activeChar, player);
			}
			catch (StringIndexOutOfBoundsException e)
			{
			}
		}
		else if (fullString.equals("admin_kill_menu"))
		{
			GameObject obj = activeChar.getTarget();
			StringTokenizer st = new StringTokenizer(fullString);
			if (st.countTokens() > 1)
			{
				st.nextToken();
				String player = st.nextToken();
				Player plyr = World.getPlayer(player);
				if (plyr == null)
				{
					activeChar.sendMessage("Player " + player + " not found in game.");
				}
				obj = plyr;
			}
			if ((obj != null) && obj.isCreature())
			{
				Creature target = (Creature) obj;
				target.reduceCurrentHp(target.getMaxHp() + 1, 0, activeChar, null, true, true, true, false, false, false, true);
			}
			else
			{
				activeChar.sendPacket(Msg.INVALID_TARGET);
			}
		}
		else if (fullString.startsWith("admin_kick_menu"))
		{
			StringTokenizer st = new StringTokenizer(fullString);
			if (st.countTokens() > 1)
			{
				st.nextToken();
				String player = st.nextToken();
				if (AdminFunctions.kick(player, "kick"))
				{
					activeChar.sendMessage("Player kicked.");
				}
			}
		}
		activeChar.sendPacket(new NpcHtmlMessage(5).setFile("admin/charmanage.htm"));
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
	 * Method teleportCharacter.
	 * @param player Player
	 * @param loc Location
	 * @param activeChar Player
	 */
	private void teleportCharacter(Player player, Location loc, Player activeChar)
	{
		if (player != null)
		{
			player.sendMessage("Admin is teleporting you.");
			player.teleToLocation(loc);
		}
	}
	
	/**
	 * Method teleportToCharacter.
	 * @param activeChar Player
	 * @param target GameObject
	 */
	private void teleportToCharacter(Player activeChar, GameObject target)
	{
		Player player;
		if ((target != null) && target.isPlayer())
		{
			player = (Player) target;
		}
		else
		{
			activeChar.sendPacket(Msg.INVALID_TARGET);
			return;
		}
		if (player.getObjectId() == activeChar.getObjectId())
		{
			activeChar.sendMessage("You cannot self teleport.");
		}
		else
		{
			activeChar.teleToLocation(player.getLoc());
			activeChar.sendMessage("You have teleported to character " + player.getName() + ".");
		}
	}
}
