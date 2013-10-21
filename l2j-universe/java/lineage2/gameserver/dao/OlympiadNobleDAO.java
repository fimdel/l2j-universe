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
import lineage2.gameserver.model.entity.olympiad.Olympiad;
import lineage2.gameserver.templates.StatsSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class OlympiadNobleDAO
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(OlympiadNobleDAO.class);
	/**
	 * Field _instance.
	 */
	private static final OlympiadNobleDAO _instance = new OlympiadNobleDAO();
	/**
	 * Field SELECT_SQL_QUERY. (value is ""SELECT char_id, characters.char_name as char_name, class_id, olympiad_points, olympiad_points_past, olympiad_points_past_static, competitions_done, competitions_loose, competitions_win, game_classes_count, game_noclasses_count FROM olympiad_nobles LEFT JOIN
	 * characters ON characters.obj_Id = olympiad_nobles.char_id"")
	 */
	public static final String SELECT_SQL_QUERY = "SELECT char_id, characters.char_name as char_name, class_id, olympiad_points, olympiad_points_past, olympiad_points_past_static, competitions_done, competitions_loose, competitions_win, game_classes_count, game_noclasses_count FROM olympiad_nobles LEFT JOIN characters ON characters.obj_Id = olympiad_nobles.char_id";
	/**
	 * Field REPLACE_SQL_QUERY. (value is ""REPLACE INTO `olympiad_nobles` (`char_id`, `class_id`, `olympiad_points`, `olympiad_points_past`, `olympiad_points_past_static`, `competitions_done`, `competitions_win`, `competitions_loose`, game_classes_count, game_noclasses_count) VALUES
	 * (?,?,?,?,?,?,?,?,?,?)"")
	 */
	public static final String REPLACE_SQL_QUERY = "REPLACE INTO `olympiad_nobles` (`char_id`, `class_id`, `olympiad_points`, `olympiad_points_past`, `olympiad_points_past_static`, `competitions_done`, `competitions_win`, `competitions_loose`, game_classes_count, game_noclasses_count) VALUES (?,?,?,?,?,?,?,?,?,?)";
	/**
	 * Field OLYMPIAD_GET_HEROS. (value is ""SELECT `char_id`, characters.char_name AS char_name FROM `olympiad_nobles` LEFT JOIN characters ON char_id=characters.obj_Id WHERE `class_id` = ? AND `competitions_done` >= ? AND `competitions_win` > 0 ORDER BY `olympiad_points` DESC, `competitions_win`
	 * DESC, `competitions_done` DESC"")
	 */
	public static final String OLYMPIAD_GET_HEROS = "SELECT `char_id`, characters.char_name AS char_name FROM `olympiad_nobles` LEFT JOIN characters ON char_id=characters.obj_Id WHERE `class_id` = ? AND `competitions_done` >= ? AND `competitions_win` > 0 ORDER BY `olympiad_points` DESC, `competitions_win` DESC, `competitions_done` DESC";
	/**
	 * Field GET_EACH_CLASS_LEADER. (value is ""SELECT characters.char_name AS char_name FROM `olympiad_nobles` LEFT JOIN characters ON char_id=characters.obj_Id WHERE `class_id` = ? AND `olympiad_points_past_static` != 0 ORDER BY `olympiad_points_past_static` DESC LIMIT 10"")
	 */
	public static final String GET_EACH_CLASS_LEADER = "SELECT characters.char_name AS char_name FROM `olympiad_nobles` LEFT JOIN characters ON char_id=characters.obj_Id WHERE `class_id` = ? AND `olympiad_points_past_static` != 0 ORDER BY `olympiad_points_past_static` DESC LIMIT 10";
	/**
	 * Field GET_ALL_CLASSIFIED_NOBLESS. (value is ""SELECT `char_id` FROM `olympiad_nobles` ORDER BY olympiad_points_past_static DESC"")
	 */
	public static final String GET_ALL_CLASSIFIED_NOBLESS = "SELECT `char_id` FROM `olympiad_nobles` ORDER BY olympiad_points_past_static DESC";
	/**
	 * Field OLYMPIAD_CALCULATE_LAST_PERIOD. (value is ""UPDATE `olympiad_nobles` SET `olympiad_points_past` = `olympiad_points`, `olympiad_points_past_static` = `olympiad_points` WHERE `competitions_done` >= ?"")
	 */
	public static final String OLYMPIAD_CALCULATE_LAST_PERIOD = "UPDATE `olympiad_nobles` SET `olympiad_points_past` = `olympiad_points`, `olympiad_points_past_static` = `olympiad_points` WHERE `competitions_done` >= ?";
	/**
	 * Field OLYMPIAD_CLEANUP_NOBLES. (value is ""UPDATE `olympiad_nobles` SET `olympiad_points` = ?, `competitions_done` = 0, `competitions_win` = 0, `competitions_loose` = 0, game_classes_count=0, game_noclasses_count=0"")
	 */
	public static final String OLYMPIAD_CLEANUP_NOBLES = "UPDATE `olympiad_nobles` SET `olympiad_points` = ?, `competitions_done` = 0, `competitions_win` = 0, `competitions_loose` = 0, game_classes_count=0, game_noclasses_count=0";
	
	/**
	 * Method getInstance.
	 * @return OlympiadNobleDAO
	 */
	public static OlympiadNobleDAO getInstance()
	{
		return _instance;
	}
	
	/**
	 * Method select.
	 */
	public void select()
	{
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(SELECT_SQL_QUERY);
			rset = statement.executeQuery();
			while (rset.next())
			{
				int classId = rset.getInt(Olympiad.CLASS_ID);
				StatsSet statDat = new StatsSet();
				int charId = rset.getInt(Olympiad.CHAR_ID);
				statDat.set(Olympiad.CLASS_ID, classId);
				statDat.set(Olympiad.CHAR_NAME, rset.getString(Olympiad.CHAR_NAME));
				statDat.set(Olympiad.POINTS, rset.getInt(Olympiad.POINTS));
				statDat.set(Olympiad.POINTS_PAST, rset.getInt(Olympiad.POINTS_PAST));
				statDat.set(Olympiad.POINTS_PAST_STATIC, rset.getInt(Olympiad.POINTS_PAST_STATIC));
				statDat.set(Olympiad.COMP_DONE, rset.getInt(Olympiad.COMP_DONE));
				statDat.set(Olympiad.COMP_WIN, rset.getInt(Olympiad.COMP_WIN));
				statDat.set(Olympiad.COMP_LOOSE, rset.getInt(Olympiad.COMP_LOOSE));
				statDat.set(Olympiad.GAME_CLASSES_COUNT, rset.getInt(Olympiad.GAME_CLASSES_COUNT));
				statDat.set(Olympiad.GAME_NOCLASSES_COUNT, rset.getInt(Olympiad.GAME_NOCLASSES_COUNT));
				Olympiad._nobles.put(charId, statDat);
			}
		}
		catch (Exception e)
		{
			_log.error("OlympiadNobleDAO: select():", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
	}
	
	/**
	 * Method replace.
	 * @param nobleId int
	 */
	public void replace(int nobleId)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			StatsSet nobleInfo = Olympiad._nobles.get(nobleId);
			statement = con.prepareStatement(REPLACE_SQL_QUERY);
			statement.setInt(1, nobleId);
			statement.setInt(2, nobleInfo.getInteger(Olympiad.CLASS_ID));
			statement.setInt(3, nobleInfo.getInteger(Olympiad.POINTS));
			statement.setInt(4, nobleInfo.getInteger(Olympiad.POINTS_PAST));
			statement.setInt(5, nobleInfo.getInteger(Olympiad.POINTS_PAST_STATIC));
			statement.setInt(6, nobleInfo.getInteger(Olympiad.COMP_DONE));
			statement.setInt(7, nobleInfo.getInteger(Olympiad.COMP_WIN));
			statement.setInt(8, nobleInfo.getInteger(Olympiad.COMP_LOOSE));
			statement.setInt(9, nobleInfo.getInteger(Olympiad.GAME_CLASSES_COUNT));
			statement.setInt(10, nobleInfo.getInteger(Olympiad.GAME_NOCLASSES_COUNT));
			statement.execute();
		}
		catch (Exception e)
		{
			_log.error("OlympiadNobleDAO: replace(int): " + nobleId, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
}
