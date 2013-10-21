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
import lineage2.gameserver.model.Playable;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.instances.NpcInstance;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
@SuppressWarnings("unused")
public class AdminRes implements IAdminCommandHandler
{
	/**
	 * @author Mobius
	 */
	private static enum Commands
	{
		/**
		 * Field admin_res.
		 */
		admin_res
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
		if (!activeChar.getPlayerAccess().Res)
		{
			return false;
		}
		if (fullString.startsWith("admin_res "))
		{
			handleRes(activeChar, wordList[1]);
		}
		if (fullString.equals("admin_res"))
		{
			handleRes(activeChar);
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
				try
				{
					int radius = Math.max(Integer.parseInt(player), 100);
					for (Creature character : activeChar.getAroundCharacters(radius, radius))
					{
						handleRes(character);
					}
					activeChar.sendMessage("Resurrected within " + radius + " unit radius.");
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
		if (obj instanceof Creature)
		{
			handleRes((Creature) obj);
		}
		else
		{
			activeChar.sendPacket(Msg.INVALID_TARGET);
		}
	}
	
	/**
	 * Method handleRes.
	 * @param target Creature
	 */
	private void handleRes(Creature target)
	{
		if (!target.isDead())
		{
			return;
		}
		if (target.isPlayable())
		{
			if (target.isPlayer())
			{
				((Player) target).doRevive(100.);
			}
			else
			{
				((Playable) target).doRevive();
			}
		}
		else if (target.isNpc())
		{
			((NpcInstance) target).stopDecay();
		}
		target.setCurrentHpMp(target.getMaxHp(), target.getMaxMp(), true);
		target.setCurrentCp(target.getMaxCp());
	}
}
