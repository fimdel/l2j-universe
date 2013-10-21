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
import java.util.ArrayList;
import java.util.List;

import lineage2.commons.dbutils.DbUtils;
import lineage2.gameserver.database.DatabaseFactory;
import lineage2.gameserver.model.entity.residence.Residence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class SiegePlayerDAO
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(SiegePlayerDAO.class);
	/**
	 * Field _instance.
	 */
	private static final SiegePlayerDAO _instance = new SiegePlayerDAO();
	/**
	 * Field INSERT_SQL_QUERY. (value is ""INSERT INTO siege_players(residence_id, object_id, clan_id) VALUES (?,?,?)"")
	 */
	public static final String INSERT_SQL_QUERY = "INSERT INTO siege_players(residence_id, object_id, clan_id) VALUES (?,?,?)";
	/**
	 * Field DELETE_SQL_QUERY. (value is ""DELETE FROM siege_players WHERE residence_id=? AND object_id=? AND clan_id=?"")
	 */
	public static final String DELETE_SQL_QUERY = "DELETE FROM siege_players WHERE residence_id=? AND object_id=? AND clan_id=?";
	/**
	 * Field DELETE_SQL_QUERY2. (value is ""DELETE FROM siege_players WHERE residence_id=?"")
	 */
	public static final String DELETE_SQL_QUERY2 = "DELETE FROM siege_players WHERE residence_id=?";
	/**
	 * Field SELECT_SQL_QUERY. (value is ""SELECT object_id FROM siege_players WHERE residence_id=? AND clan_id=?"")
	 */
	public static final String SELECT_SQL_QUERY = "SELECT object_id FROM siege_players WHERE residence_id=? AND clan_id=?";
	
	/**
	 * Method getInstance.
	 * @return SiegePlayerDAO
	 */
	public static SiegePlayerDAO getInstance()
	{
		return _instance;
	}
	
	/**
	 * Method select.
	 * @param residence Residence
	 * @param clanId int
	 * @return List<Integer>
	 */
	public List<Integer> select(Residence residence, int clanId)
	{
		List<Integer> set = new ArrayList<>();
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(SELECT_SQL_QUERY);
			statement.setInt(1, residence.getId());
			statement.setInt(2, clanId);
			rset = statement.executeQuery();
			while (rset.next())
			{
				set.add(rset.getInt("object_id"));
			}
		}
		catch (Exception e)
		{
			_log.error("SiegePlayerDAO.select(Residence, int): " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
		return set;
	}
	
	/**
	 * Method insert.
	 * @param residence Residence
	 * @param clanId int
	 * @param playerId int
	 */
	public void insert(Residence residence, int clanId, int playerId)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(INSERT_SQL_QUERY);
			statement.setInt(1, residence.getId());
			statement.setInt(2, playerId);
			statement.setInt(3, clanId);
			statement.execute();
		}
		catch (Exception e)
		{
			_log.error("SiegePlayerDAO.insert(Residence, int, int): " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
	
	/**
	 * Method delete.
	 * @param residence Residence
	 * @param clanId int
	 * @param playerId int
	 */
	public void delete(Residence residence, int clanId, int playerId)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(DELETE_SQL_QUERY);
			statement.setInt(1, residence.getId());
			statement.setInt(2, playerId);
			statement.setInt(3, clanId);
			statement.execute();
		}
		catch (Exception e)
		{
			_log.error("SiegePlayerDAO.delete(Residence, int, int): " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
	
	/**
	 * Method delete.
	 * @param residence Residence
	 */
	public void delete(Residence residence)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(DELETE_SQL_QUERY2);
			statement.setInt(1, residence.getId());
			statement.execute();
		}
		catch (Exception e)
		{
			_log.error("SiegePlayerDAO.delete(Residence): " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
}
