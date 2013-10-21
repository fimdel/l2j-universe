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
package lineage2.gameserver.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import lineage2.commons.dbutils.DbUtils;
import lineage2.gameserver.database.DatabaseFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class CastleDoorUpgradeDAO
{
	/**
	 * Field _instance.
	 */
	private static final CastleDoorUpgradeDAO _instance = new CastleDoorUpgradeDAO();
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(CastleDoorUpgradeDAO.class);
	/**
	 * Field SELECT_SQL_QUERY. (value is ""SELECT hp FROM castle_door_upgrade WHERE door_id=?"")
	 */
	public static final String SELECT_SQL_QUERY = "SELECT hp FROM castle_door_upgrade WHERE door_id=?";
	/**
	 * Field REPLACE_SQL_QUERY. (value is ""REPLACE INTO castle_door_upgrade (door_id, hp) VALUES (?,?)"")
	 */
	public static final String REPLACE_SQL_QUERY = "REPLACE INTO castle_door_upgrade (door_id, hp) VALUES (?,?)";
	/**
	 * Field DELETE_SQL_QUERY. (value is ""DELETE FROM castle_door_upgrade WHERE door_id=?"")
	 */
	public static final String DELETE_SQL_QUERY = "DELETE FROM castle_door_upgrade WHERE door_id=?";
	
	/**
	 * Method getInstance.
	 * @return CastleDoorUpgradeDAO
	 */
	public static CastleDoorUpgradeDAO getInstance()
	{
		return _instance;
	}
	
	/**
	 * Method load.
	 * @param doorId int
	 * @return int
	 */
	public int load(int doorId)
	{
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(SELECT_SQL_QUERY);
			statement.setInt(1, doorId);
			rset = statement.executeQuery();
			if (rset.next())
			{
				return rset.getInt("hp");
			}
		}
		catch (Exception e)
		{
			_log.error("CastleDoorUpgradeDAO:load(int): " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
		return 0;
	}
	
	/**
	 * Method insert.
	 * @param uId int
	 * @param val int
	 */
	public void insert(int uId, int val)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(REPLACE_SQL_QUERY);
			statement.setInt(1, uId);
			statement.setInt(2, val);
			statement.execute();
		}
		catch (Exception e)
		{
			_log.error("CastleDoorUpgradeDAO:insert(int, int): " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
	
	/**
	 * Method delete.
	 * @param uId int
	 */
	public void delete(int uId)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(DELETE_SQL_QUERY);
			statement.setInt(1, uId);
			statement.execute();
		}
		catch (Exception e)
		{
			_log.error("CastleDoorUpgradeDAO:delete(int): " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
}
