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
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lineage2.commons.dbutils.DbUtils;
import lineage2.gameserver.database.DatabaseFactory;
import lineage2.gameserver.model.entity.olympiad.OlympiadHistory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class OlympiadHistoryDAO
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(OlympiadHistoryDAO.class);
	/**
	 * Field _instance.
	 */
	private static final OlympiadHistoryDAO _instance = new OlympiadHistoryDAO();
	/**
	 * Field SELECT_SQL_QUERY. (value is ""SELECT * FROM olympiad_history ORDER BY game_start_time"")
	 */
	public static final String SELECT_SQL_QUERY = "SELECT * FROM olympiad_history ORDER BY game_start_time";
	/**
	 * Field DELETE_SQL_QUERY. (value is ""DELETE FROM olympiad_history WHERE old=1"")
	 */
	public static final String DELETE_SQL_QUERY = "DELETE FROM olympiad_history WHERE old=1";
	/**
	 * Field UPDATE_SQL_QUERY. (value is ""UPDATE olympiad_history SET old=1"")
	 */
	public static final String UPDATE_SQL_QUERY = "UPDATE olympiad_history SET old=1";
	/**
	 * Field INSERT_SQL_QUERY. (value is ""INSERT INTO olympiad_history(object_id_1, object_id_2, class_id_1, class_id_2, name_1, name_2, game_start_time, game_time, game_status, game_type, old) VALUES (?,?,?,?,?,?,?,?,?,?,?)"")
	 */
	public static final String INSERT_SQL_QUERY = "INSERT INTO olympiad_history(object_id_1, object_id_2, class_id_1, class_id_2, name_1, name_2, game_start_time, game_time, game_status, game_type, old) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
	
	/**
	 * Method getInstance.
	 * @return OlympiadHistoryDAO
	 */
	public static OlympiadHistoryDAO getInstance()
	{
		return _instance;
	}
	
	/**
	 * Method select.
	 * @return Map<Boolean,List<OlympiadHistory>>
	 */
	public Map<Boolean, List<OlympiadHistory>> select()
	{
		Map<Boolean, List<OlympiadHistory>> map = null;
		Connection con = null;
		Statement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.createStatement();
			rset = statement.executeQuery(SELECT_SQL_QUERY);
			map = new HashMap<>(2);
			map.put(Boolean.TRUE, new ArrayList<OlympiadHistory>());
			map.put(Boolean.FALSE, new ArrayList<OlympiadHistory>());
			while (rset.next())
			{
				int objectId1 = rset.getInt("object_id_1");
				int objectId2 = rset.getInt("object_id_2");
				int classId1 = rset.getInt("class_id_1");
				int classId2 = rset.getInt("class_id_2");
				String name1 = rset.getString("name_1");
				String name2 = rset.getString("name_2");
				boolean old = rset.getBoolean("old");
				OlympiadHistory history = new OlympiadHistory(objectId1, objectId2, classId1, classId2, name1, name2, rset.getLong("game_start_time"), rset.getInt("game_time"), rset.getInt("game_status"), rset.getInt("game_type"));
				map.get(old).add(history);
			}
		}
		catch (Exception e)
		{
			map = Collections.emptyMap();
			_log.error("OlympiadHistoryDAO: select(): " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
		return map;
	}
	
	/**
	 * Method insert.
	 * @param history OlympiadHistory
	 */
	public void insert(OlympiadHistory history)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(INSERT_SQL_QUERY);
			statement.setInt(1, history.getObjectId1());
			statement.setInt(2, history.getObjectId2());
			statement.setInt(3, history.getClassId1());
			statement.setInt(4, history.getClassId2());
			statement.setString(5, history.getName1());
			statement.setString(6, history.getName2());
			statement.setLong(7, history.getGameStartTime());
			statement.setInt(8, history.getGameTime());
			statement.setInt(9, history.getGameStatus());
			statement.setInt(10, history.getGameType());
			statement.setInt(11, 0);
			statement.execute();
		}
		catch (Exception e)
		{
			_log.error("OlympiadHistoryDAO: insert(OlympiadHistory): " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
	
	/**
	 * Method switchData.
	 */
	public void switchData()
	{
		Connection con = null;
		Statement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.createStatement();
			statement.execute(DELETE_SQL_QUERY);
			DbUtils.close(statement);
			statement = con.createStatement();
			statement.execute(UPDATE_SQL_QUERY);
		}
		catch (Exception e)
		{
			_log.error("OlympiadHistoryDAO: select(): " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
}
