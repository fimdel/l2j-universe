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

import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.handler.admincommands.IAdminCommandHandler;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.World;
import lineage2.gameserver.network.serverpackets.components.CustomMessage;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AdminDisconnect implements IAdminCommandHandler
{
	/**
	 * @author Mobius
	 */
	private static enum Commands
	{
		/**
		 * Field admin_disconnect.
		 */
		admin_disconnect,
		/**
		 * Field admin_kick.
		 */
		admin_kick
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
		if (!activeChar.getPlayerAccess().CanKick)
		{
			return false;
		}
		switch (command)
		{
			case admin_disconnect:
			case admin_kick:
				final Player player;
				if (wordList.length == 1)
				{
					GameObject target = activeChar.getTarget();
					if (target == null)
					{
						activeChar.sendMessage("Select character or specify player name.");
						break;
					}
					if (!target.isPlayer())
					{
						activeChar.sendPacket(Msg.INVALID_TARGET);
						break;
					}
					player = (Player) target;
				}
				else
				{
					player = World.getPlayer(wordList[1]);
					if (player == null)
					{
						activeChar.sendMessage("Character " + wordList[1] + " not found in game.");
						break;
					}
				}
				if (player.getObjectId() == activeChar.getObjectId())
				{
					activeChar.sendMessage("You can't logout your character.");
					break;
				}
				activeChar.sendMessage("Character " + player.getName() + " disconnected from server.");
				if (player.isInOfflineMode())
				{
					player.setOfflineMode(false);
					player.kick();
					return true;
				}
				player.sendMessage(new CustomMessage("admincommandhandlers.AdminDisconnect.YoureKickedByGM", player));
				player.sendPacket(Msg.YOU_HAVE_BEEN_DISCONNECTED_FROM_THE_SERVER_PLEASE_LOGIN_AGAIN);
				ThreadPoolManager.getInstance().schedule(new RunnableImpl()
				{
					@Override
					public void runImpl()
					{
						player.kick();
					}
				}, 500);
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
