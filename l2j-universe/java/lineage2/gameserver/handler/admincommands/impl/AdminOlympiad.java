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

import java.util.ArrayList;
import java.util.List;

import lineage2.gameserver.Announcements;
import lineage2.gameserver.Config;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.handler.admincommands.IAdminCommandHandler;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.entity.Hero;
import lineage2.gameserver.model.entity.olympiad.Olympiad;
import lineage2.gameserver.model.entity.olympiad.OlympiadDatabase;
import lineage2.gameserver.model.entity.olympiad.OlympiadManager;
import lineage2.gameserver.templates.StatsSet;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AdminOlympiad implements IAdminCommandHandler
{
	/**
	 * @author Mobius
	 */
	private static enum Commands
	{
		/**
		 * Field admin_oly_save.
		 */
		admin_oly_save,
		/**
		 * Field admin_add_oly_points.
		 */
		admin_add_oly_points,
		/**
		 * Field admin_oly_start.
		 */
		admin_oly_start,
		/**
		 * Field admin_add_hero.
		 */
		admin_add_hero,
		/**
		 * Field admin_oly_stop.
		 */
		admin_oly_stop
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
		switch (command)
		{
			case admin_oly_save:
			{
				if (!Config.ENABLE_OLYMPIAD)
				{
					return false;
				}
				try
				{
					OlympiadDatabase.save();
				}
				catch (Exception e)
				{
				}
				activeChar.sendMessage("olympaid data saved.");
				break;
			}
			case admin_add_oly_points:
			{
				if (wordList.length < 3)
				{
					activeChar.sendMessage("Command syntax: //add_oly_points <char_name> <point_to_add>");
					activeChar.sendMessage("This command can be applied only for online players.");
					return false;
				}
				Player player = World.getPlayer(wordList[1]);
				if (player == null)
				{
					activeChar.sendMessage("Character " + wordList[1] + " not found in game.");
					return false;
				}
				int pointToAdd;
				try
				{
					pointToAdd = Integer.parseInt(wordList[2]);
				}
				catch (NumberFormatException e)
				{
					activeChar.sendMessage("Please specify integer value for olympiad points.");
					return false;
				}
				int curPoints = Olympiad.getNoblePoints(player.getObjectId());
				Olympiad.manualSetNoblePoints(player.getObjectId(), curPoints + pointToAdd);
				int newPoints = Olympiad.getNoblePoints(player.getObjectId());
				activeChar.sendMessage("Added " + pointToAdd + " points to character " + player.getName());
				activeChar.sendMessage("Old points: " + curPoints + ", new points: " + newPoints);
				break;
			}
			case admin_oly_start:
			{
				Olympiad._manager = new OlympiadManager();
				Olympiad._inCompPeriod = true;
				new Thread(Olympiad._manager).start();
				Announcements.getInstance().announceToAll(Msg.THE_OLYMPIAD_GAME_HAS_STARTED);
				break;
			}
			case admin_oly_stop:
			{
				Olympiad._inCompPeriod = false;
				Announcements.getInstance().announceToAll(Msg.THE_OLYMPIAD_GAME_HAS_ENDED);
				try
				{
					OlympiadDatabase.save();
				}
				catch (Exception e)
				{
				}
				break;
			}
			case admin_add_hero:
			{
				if (wordList.length < 2)
				{
					activeChar.sendMessage("Command syntax: //add_hero <char_name>");
					activeChar.sendMessage("This command can be applied only for online players.");
					return false;
				}
				Player player = World.getPlayer(wordList[1]);
				if (player == null)
				{
					activeChar.sendMessage("Character " + wordList[1] + " not found in game.");
					return false;
				}
				StatsSet hero = new StatsSet();
				hero.set(Olympiad.CLASS_ID, player.getBaseClassId());
				hero.set(Olympiad.CHAR_ID, player.getObjectId());
				hero.set(Olympiad.CHAR_NAME, player.getName());
				List<StatsSet> heroesToBe = new ArrayList<>();
				heroesToBe.add(hero);
				Hero.getInstance().computeNewHeroes(heroesToBe);
				activeChar.sendMessage("Hero status added to player " + player.getName());
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
