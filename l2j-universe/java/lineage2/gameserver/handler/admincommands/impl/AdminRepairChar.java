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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import lineage2.commons.dbutils.DbUtils;
import lineage2.gameserver.database.DatabaseFactory;
import lineage2.gameserver.handler.admincommands.IAdminCommandHandler;
import lineage2.gameserver.model.Player;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
@SuppressWarnings("unused")
public class AdminRepairChar implements IAdminCommandHandler
{
	/**
	 * @author Mobius
	 */
	private static enum Commands
	{
		/**
		 * Field admin_restore.
		 */
		admin_restore,
		/**
		 * Field admin_repair.
		 */
		admin_repair
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
		if ((activeChar.getPlayerAccess() == null) || !activeChar.getPlayerAccess().CanEditChar)
		{
			return false;
		}
		if (wordList.length != 2)
		{
			return false;
		}
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("UPDATE characters SET x=-84318, y=244579, z=-3730 WHERE char_name=?");
			statement.setString(1, wordList[1]);
			statement.execute();
			DbUtils.close(statement);
			statement = con.prepareStatement("SELECT obj_id FROM characters where char_name=?");
			statement.setString(1, wordList[1]);
			rset = statement.executeQuery();
			int objId = 0;
			if (rset.next())
			{
				objId = rset.getInt(1);
			}
			DbUtils.close(statement, rset);
			if (objId == 0)
			{
				return false;
			}
			statement = con.prepareStatement("DELETE FROM character_shortcuts WHERE object_id=?");
			statement.setInt(1, objId);
			statement.execute();
			DbUtils.close(statement);
			statement = con.prepareStatement("UPDATE items SET loc='INVENTORY' WHERE owner_id=? AND loc!='WAREHOUSE'");
			statement.setInt(1, objId);
			statement.execute();
			DbUtils.close(statement);
			statement = con.prepareStatement("DELETE FROM character_variables WHERE obj_id=? AND `type`='user-var' AND `name`='reflection' LIMIT 1");
			statement.setInt(1, objId);
			statement.execute();
			DbUtils.close(statement);
		}
		catch (Exception e)
		{
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
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
