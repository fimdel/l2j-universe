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
import lineage2.gameserver.model.base.Experience;
import lineage2.gameserver.model.instances.PetInstance;
import lineage2.gameserver.tables.PetDataTable;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AdminLevel implements IAdminCommandHandler
{
	/**
	 * @author Mobius
	 */
	private static enum Commands
	{
		/**
		 * Field admin_add_level.
		 */
		admin_add_level,
		/**
		 * Field admin_addLevel.
		 */
		admin_addLevel,
		/**
		 * Field admin_set_level.
		 */
		admin_set_level,
		/**
		 * Field admin_setLevel.
		 */
		admin_setLevel,
	}
	
	/**
	 * Method setLevel.
	 * @param activeChar Player
	 * @param target GameObject
	 * @param level int
	 */
	private void setLevel(Player activeChar, GameObject target, int level)
	{
		if ((target == null) || !(target.isPlayer() || target.isPet()))
		{
			activeChar.sendPacket(Msg.INVALID_TARGET);
			return;
		}
		if ((level < 1) || (level > Experience.getMaxLevel()))
		{
			activeChar.sendMessage("You must specify level 1 - " + Experience.getMaxLevel());
			return;
		}
		if (target.isPlayer())
		{
			Long exp_add = Experience.LEVEL[level] - ((Player) target).getExp();
			((Player) target).addExpAndSp(exp_add, 0);
			return;
		}
		if (target.isPet())
		{
			Long exp_add = PetDataTable.getInstance().getInfo(((PetInstance) target).getNpcId(), level).getExp() - ((PetInstance) target).getExp();
			((PetInstance) target).addExpAndSp(exp_add, 0);
		}
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
		GameObject target = activeChar.getTarget();
		if ((target == null) || !(target.isPlayer() || target.isPet()))
		{
			activeChar.sendPacket(Msg.INVALID_TARGET);
			return false;
		}
		int level;
		switch (command)
		{
			case admin_add_level:
			case admin_addLevel:
				if (wordList.length < 2)
				{
					activeChar.sendMessage("USAGE: //addLevel level");
					return false;
				}
				try
				{
					level = Integer.parseInt(wordList[1]);
				}
				catch (NumberFormatException e)
				{
					activeChar.sendMessage("You must specify level");
					return false;
				}
				setLevel(activeChar, target, level + ((Creature) target).getLevel());
				break;
			case admin_set_level:
			case admin_setLevel:
				if (wordList.length < 2)
				{
					activeChar.sendMessage("USAGE: //setlevel level");
					return false;
				}
				try
				{
					level = Integer.parseInt(wordList[1]);
				}
				catch (NumberFormatException e)
				{
					activeChar.sendMessage("You must specify level");
					return false;
				}
				setLevel(activeChar, target, level);
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
