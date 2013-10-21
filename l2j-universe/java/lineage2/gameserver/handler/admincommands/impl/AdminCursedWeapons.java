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
import lineage2.gameserver.instancemanager.CursedWeaponsManager;
import lineage2.gameserver.model.CursedWeapon;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.utils.ItemFunctions;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AdminCursedWeapons implements IAdminCommandHandler
{
	/**
	 * @author Mobius
	 */
	private static enum Commands
	{
		/**
		 * Field admin_cw_info.
		 */
		admin_cw_info,
		/**
		 * Field admin_cw_remove.
		 */
		admin_cw_remove,
		/**
		 * Field admin_cw_goto.
		 */
		admin_cw_goto,
		/**
		 * Field admin_cw_reload.
		 */
		admin_cw_reload,
		/**
		 * Field admin_cw_add.
		 */
		admin_cw_add,
		/**
		 * Field admin_cw_drop.
		 */
		admin_cw_drop
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
		CursedWeaponsManager cwm = CursedWeaponsManager.getInstance();
		CursedWeapon cw = null;
		switch (command)
		{
			case admin_cw_remove:
			case admin_cw_goto:
			case admin_cw_add:
			case admin_cw_drop:
				if (wordList.length < 2)
				{
					activeChar.sendMessage("Вы не указали id");
					return false;
				}
				for (CursedWeapon cwp : CursedWeaponsManager.getInstance().getCursedWeapons())
				{
					if (cwp.getName().toLowerCase().contains(wordList[1].toLowerCase()))
					{
						cw = cwp;
					}
				}
				if (cw == null)
				{
					activeChar.sendMessage("�?еизве�?тный id");
					return false;
				}
				break;
			default:
				break;
		}
		switch (command)
		{
			case admin_cw_info:
				activeChar.sendMessage("======= Cursed Weapons: =======");
				for (CursedWeapon c : cwm.getCursedWeapons())
				{
					activeChar.sendMessage("> " + c.getName() + " (" + c.getItemId() + ")");
					if (c.isActivated())
					{
						Player pl = c.getPlayer();
						activeChar.sendMessage("  Player holding: " + pl.getName());
						activeChar.sendMessage("  Player karma: " + c.getPlayerKarma());
						activeChar.sendMessage("  Time Remaining: " + (c.getTimeLeft() / 60000) + " min.");
						activeChar.sendMessage("  Kills : " + c.getNbKills());
					}
					else if (c.isDropped())
					{
						activeChar.sendMessage("  Lying on the ground.");
						activeChar.sendMessage("  Time Remaining: " + (c.getTimeLeft() / 60000) + " min.");
						activeChar.sendMessage("  Kills : " + c.getNbKills());
					}
					else
					{
						activeChar.sendMessage("  Don't exist in the world.");
					}
				}
				break;
			case admin_cw_reload:
				activeChar.sendMessage("Cursed weapons can't be reloaded.");
				break;
			case admin_cw_remove:
				if (cw == null)
				{
					return false;
				}
				CursedWeaponsManager.getInstance().endOfLife(cw);
				break;
			case admin_cw_goto:
				if (cw == null)
				{
					return false;
				}
				activeChar.teleToLocation(cw.getLoc());
				break;
			case admin_cw_add:
				if (cw == null)
				{
					return false;
				}
				if (cw.isActive())
				{
					activeChar.sendMessage("This cursed weapon is already active.");
				}
				else
				{
					GameObject target = activeChar.getTarget();
					if ((target != null) && target.isPlayer() && !((Player) target).isInOlympiadMode())
					{
						Player player = (Player) target;
						ItemInstance item = ItemFunctions.createItem(cw.getItemId());
						cwm.activate(player, player.getInventory().addItem(item));
						cwm.showUsageTime(player, cw);
					}
				}
				break;
			case admin_cw_drop:
				if (cw == null)
				{
					return false;
				}
				if (cw.isActive())
				{
					activeChar.sendMessage("This cursed weapon is already active.");
				}
				else
				{
					GameObject target = activeChar.getTarget();
					if ((target != null) && target.isPlayer() && !((Player) target).isInOlympiadMode())
					{
						Player player = (Player) target;
						cw.create(null, player);
					}
				}
				break;
		}
		/**
		 * Method getAdminCommandEnum.
		 * @return Enum[]
		 * @see lineage2.gameserver.handler.admincommands.IAdminCommandHandler#getAdminCommandEnum()
		 */
		
		/**
		 * Method getAdminCommandEnum.
		 * @return Enum[]
		 * @see lineage2.gameserver.handler.admincommands.IAdminCommandHandler#getAdminCommandEnum()
		 */
		return true;
	}
	
	@Override
	public Enum<?>[] getAdminCommandEnum()
	{
		return Commands.values();
	}
}
