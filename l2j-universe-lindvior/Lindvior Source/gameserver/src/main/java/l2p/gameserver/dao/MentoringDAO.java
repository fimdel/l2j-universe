/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.dao;

import l2p.commons.dbutils.DbUtils;
import l2p.gameserver.database.DatabaseFactory;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.actor.instances.player.Mentee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Darvin
 * Date: 02.07.12
 * Time: 0:36
 */
public class MentoringDAO {
    private static final Logger _log = LoggerFactory.getLogger(MentoringDAO.class);

    private static final MentoringDAO _instance = new MentoringDAO();
    private static final String menteeList = "SELECT m.mentor AS charid, c.char_name, s.class_id, s.level FROM character_mentoring m LEFT JOIN characters c ON m.mentor = c.obj_Id LEFT JOIN character_subclasses s ON ( m.mentor = s.char_obj_id AND s.active =1 ) WHERE m.mentee = ?";
    private static final String mentorList = "SELECT m.mentee AS charid, c.char_name, s.class_id, s.level FROM character_mentoring m LEFT JOIN characters c ON m.mentee = c.obj_Id LEFT JOIN character_subclasses s ON ( m.mentee = s.char_obj_id AND s.active =1 ) WHERE m.mentor = ?";
    private static final String INSERT_CHAR_MENTORING = "INSERT INTO character_mentoring (mentor,mentee) VALUES(?,?)";
    private static final String DELETE_CHAR_MENTORING = "DELETE FROM character_mentoring WHERE mentor=? AND mentee=?";

    public static MentoringDAO getInstance() {
        return _instance;
    }

    public void delete(int mentor, int mentee) {
        Connection con = null;
        PreparedStatement statement = null;
        try {
            con = DatabaseFactory.getInstance().getConnection();

            statement = con.prepareStatement("DELETE FROM character_mentoring WHERE mentor=? AND mentee=?");
            statement.setInt(1, mentor);
            statement.setInt(2, mentee);
            statement.execute();
        } catch (Exception e) {
            _log.warn("MenteeList: could not delete mentee objectId: " + mentee + " mentorId: " + mentor, e);
        } finally {
            DbUtils.closeQuietly(con, statement);
        }
    }

    public void insert(Player mentor, Player mentee) {
        Connection con = null;
        PreparedStatement statement = null;
        try {
            con = DatabaseFactory.getInstance().getConnection();

            statement = con.prepareStatement("INSERT INTO character_mentoring (mentor,mentee) VALUES(?,?)");
            statement.setInt(1, mentor.getObjectId());
            statement.setInt(2, mentee.getObjectId());
            statement.execute();
        } catch (Exception e) {
            _log.warn(mentor.getMenteeList() + " could not add mentee objectid: " + mentee.getObjectId(), e);
        } finally {
            DbUtils.closeQuietly(con, statement);
        }
    }

    public List selectMenteeList(Player listOwner) {
        List listMetees = new ArrayList();
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet rset = null;
        try {
            con = DatabaseFactory.getInstance().getConnection();
            int clid = listOwner.getClassId().getId();
            statement = con.prepareStatement(clid > 138 ? "SELECT m.mentee AS charid, c.char_name, s.class_id, s.level FROM character_mentoring m LEFT JOIN characters c ON m.mentee = c.obj_Id LEFT JOIN character_subclasses s ON ( m.mentee = s.char_obj_id AND s.active =1 ) WHERE m.mentor = ?" : "SELECT m.mentor AS charid, c.char_name, s.class_id, s.level FROM character_mentoring m LEFT JOIN characters c ON m.mentor = c.obj_Id LEFT JOIN character_subclasses s ON ( m.mentor = s.char_obj_id AND s.active =1 ) WHERE m.mentee = ?");
            statement.setInt(1, listOwner.getObjectId());
            rset = statement.executeQuery();
            while (rset.next())
                listMetees.add(new Mentee(rset));
        } catch (Exception e) {
            _log.error("MentoringDAO.load(L2Player): " + e, e);
        } finally {
            DbUtils.closeQuietly(con, statement, rset);
        }

        return listMetees;
    }
}
