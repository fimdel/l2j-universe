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
import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.handler.admincommands.IAdminCommandHandler;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.Player;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AdminPolymorph implements IAdminCommandHandler
{
	/**
	 * @author Mobius
	 */
	private static enum Commands
	{
		/**
		 * Field admin_polyself.
		 */
		admin_polyself,
		/**
		 * Field admin_polymorph.
		 */
		admin_polymorph,
		/**
		 * Field admin_poly.
		 */
		admin_poly,
		/**
		 * Field admin_unpolyself.
		 */
		admin_unpolyself,
		/**
		 * Field admin_unpolymorph.
		 */
		admin_unpolymorph,
		/**
		 * Field admin_unpoly.
		 */
		admin_unpoly
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
	@SuppressWarnings("fallthrough")
	public boolean useAdminCommand(Enum<?> comm, String[] wordList, String fullString, Player activeChar)
	{
		Commands command = (Commands) comm;
		if (!activeChar.getPlayerAccess().CanPolymorph)
		{
			return false;
		}
		GameObject target = activeChar.getTarget();
		switch (command)
		{
			case admin_polyself:
				target = activeChar;
			case admin_polymorph:
			case admin_poly:
				if ((target == null) || !target.isPlayer())
				{
					activeChar.sendPacket(Msg.INVALID_TARGET);
					return false;
				}
				try
				{
					int id = Integer.parseInt(wordList[1]);
					if (NpcHolder.getInstance().getTemplate(id) != null)
					{
						((Player) target).setPolyId(id);
						((Player) target).broadcastCharInfo();
					}
				}
				catch (Exception e)
				{
					activeChar.sendMessage("USAGE: //poly id [type:npc|item]");
					return false;
				}
				break;
			case admin_unpolyself:
				target = activeChar;
			case admin_unpolymorph:
			case admin_unpoly:
				if ((target == null) || !target.isPlayer())
				{
					activeChar.sendPacket(Msg.INVALID_TARGET);
					return false;
				}
				((Player) target).setPolyId(0);
				((Player) target).broadcastCharInfo();
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
