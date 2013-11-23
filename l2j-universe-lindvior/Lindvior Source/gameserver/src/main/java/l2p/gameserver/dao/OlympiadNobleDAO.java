/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.dao;

import l2p.commons.dbutils.DbUtils;
import l2p.gameserver.database.DatabaseFactory;
import l2p.gameserver.model.entity.olympiad.Olympiad;
import l2p.gameserver.templates.StatsSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * @author VISTALL @date 20:00/02.05.2011
 */
public class OlympiadNobleDAO {
    private static final Logger _log = LoggerFactory.getLogger(OlympiadNobleDAO.class);
    private static final OlympiadNobleDAO _instance = new OlympiadNobleDAO();
    public static final String SELECT_SQL_QUERY = "SELECT char_id, characters.char_name as char_name, class_id, olympiad_points, olympiad_points_past, olympiad_points_past_static, competitions_done, competitions_loose, competitions_win, game_count,olympiad_participant FROM olympiad_nobles LEFT JOIN characters ON characters.obj_Id = olympiad_nobles.char_id";
    public static final String REPLACE_SQL_QUERY = "REPLACE INTO `olympiad_nobles` (`char_id`, `class_id`, `olympiad_points`, `olympiad_points_past`, `olympiad_points_past_static`, `competitions_done`, `competitions_win`, `competitions_loose`, `game_count`, `olympiad_participant`) VALUES (?,?,?,?,?,?,?,?,?,?)";
    public static final String OLYMPIAD_GET_HEROS = "SELECT `char_id`, characters.char_name AS char_name FROM `olympiad_nobles` LEFT JOIN characters ON char_id=characters.obj_Id WHERE `class_id` = ? AND `competitions_done` >= ? AND `competitions_win` > 0 ORDER BY `olympiad_points` DESC, `competitions_win` DESC, `competitions_done` DESC";
    public static final String GET_EACH_CLASS_LEADER = "SELECT characters.char_name AS char_name FROM `olympiad_nobles` LEFT JOIN characters ON char_id=characters.obj_Id WHERE `class_id` = ? AND `olympiad_points_past_static` != 0 ORDER BY `olympiad_points_past_static` DESC LIMIT 10";
    public static final String GET_ALL_CLASSIFIED_NOBLESS = "SELECT `char_id` FROM `olympiad_nobles` ORDER BY olympiad_points_past_static DESC";
    public static final String OLYMPIAD_CALCULATE_LAST_PERIOD = "UPDATE `olympiad_nobles` SET `olympiad_points_past` = `olympiad_points`, `olympiad_points_past_static` = `olympiad_points` WHERE `competitions_done` >= ?";
    public static final String OLYMPIAD_CLEANUP_NOBLES = "UPDATE `olympiad_nobles` SET `competitions_done` = 0, `competitions_win` = 0, `competitions_loose` = 0, `game_count`=0";
    public static final String OLYMPIAD_CLEANUP_NOBLE_POINTS = "UPDATE `olympiad_nobles` SET `olympiad_points` = 10 WHERE `olympiad_participant`=1";

    public static OlympiadNobleDAO getInstance() {
        return _instance;
    }

    public void select() {
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet rset = null;

        try {
            con = DatabaseFactory.getInstance().getConnection();
            statement = con.prepareStatement(SELECT_SQL_QUERY);
            rset = statement.executeQuery();

            while (rset.next()) {
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
                statDat.set(Olympiad.GAME_COUNT, rset.getInt(Olympiad.GAME_COUNT));
                statDat.set(Olympiad.OLYMPIAD_PARTICIPANT, rset.getBoolean(Olympiad.OLYMPIAD_PARTICIPANT));
                Olympiad._nobles.put(charId, statDat);
            }
        } catch (Exception e) {
            _log.error("OlympiadNobleDAO: select():", e);
        } finally {
            DbUtils.closeQuietly(con, statement, rset);
        }
    }

    public void replace(int nobleId) {
        Connection con = null;
        PreparedStatement statement = null;

        try {
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
            statement.setInt(9, nobleInfo.getInteger(Olympiad.GAME_COUNT));
            statement.setBoolean(10, nobleInfo.getBool(Olympiad.OLYMPIAD_PARTICIPANT));
            statement.execute();
        } catch (Exception e) {
            _log.error("OlympiadNobleDAO: replace(int): " + nobleId, e);
        } finally {
            DbUtils.closeQuietly(con, statement);
        }
    }
}
