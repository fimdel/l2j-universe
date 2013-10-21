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
import java.util.HashMap;
import java.util.Map;

import lineage2.commons.dbutils.DbUtils;
import lineage2.gameserver.database.DatabaseFactory;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.actor.instances.player.MenteeMentor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class MentoringDAO
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(MentoringDAO.class);
	/**
	 * Field _instance.
	 */
	private static final MentoringDAO _instance = new MentoringDAO();
	/**
	 * Field menteeList. (value is ""SELECT m.mentor AS charid, c.char_name, s.class_id, s.level FROM character_mentoring m LEFT JOIN characters c ON m.mentor = c.obj_Id LEFT JOIN character_subclasses s ON ( m.mentor = s.char_obj_id AND s.active =1 ) WHERE m.mentee = ?"")
	 */
	private static final String menteeList = "SELECT m.mentor AS charid, c.char_name, s.class_id, s.level FROM character_mentoring m LEFT JOIN characters c ON m.mentor = c.obj_Id LEFT JOIN character_subclasses s ON ( m.mentor = s.char_obj_id AND s.active =1 ) WHERE m.mentee = ?";
	/**
	 * Field mentorList. (value is ""SELECT m.mentee AS charid, c.char_name, s.class_id, s.level FROM character_mentoring m LEFT JOIN characters c ON m.mentee = c.obj_Id LEFT JOIN character_subclasses s ON ( m.mentee = s.char_obj_id AND s.active =1 ) WHERE m.mentor = ?"")
	 */
	private static final String mentorList = "SELECT m.mentee AS charid, c.char_name, s.class_id, s.level FROM character_mentoring m LEFT JOIN characters c ON m.mentee = c.obj_Id LEFT JOIN character_subclasses s ON ( m.mentee = s.char_obj_id AND s.active =1 ) WHERE m.mentor = ?";
	
	/**
	 * Method getInstance.
	 * @return MentoringDAO
	 */
	public static MentoringDAO getInstance()
	{
		return _instance;
	}
	
	/**
	 * Method selectMenteeList.
	 * @param listOwner Player
	 * @return Map<Integer,Mentee>
	 */
	public Map<Integer, MenteeMentor> selectMenteeMentorList(Player listOwner)
	{
		Map<Integer, MenteeMentor> map = new HashMap<>();
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			int clid = listOwner.getClassId().getId();
			statement = con.prepareStatement(clid > 138 ? mentorList : menteeList);
			statement.setInt(1, listOwner.getObjectId());
			rset = statement.executeQuery();
			while (rset.next())
			{
				int objectId = rset.getInt("charid");
				String name = rset.getString("char_name");
				int classId = rset.getInt("class_id");
				int level = rset.getInt("level");
				map.put(objectId, new MenteeMentor(objectId, name, classId, level, classId > 138));
			}
		}
		catch (Exception e)
		{
			_log.error("MentoringDAO.load(L2Player): " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
		return map;
	}
	
	/**
	 * Method insert.
	 * @param mentor Player
	 * @param mentee Player
	 */
	public void insert(Player mentor, Player mentee)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("INSERT INTO character_mentoring (mentor,mentee) VALUES(?,?)");
			statement.setInt(1, mentor.getObjectId());
			statement.setInt(2, mentee.getObjectId());
			statement.execute();
		}
		catch (Exception e)
		{
			_log.warn(mentor.getMenteeMentorList() + " could not add mentee objectid: " + mentee.getObjectId(), e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
	
	/**
	 * Method delete.
	 * @param mentor int
	 * @param mentee int
	 */
	public void delete(int mentor, int mentee)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("DELETE FROM character_mentoring WHERE mentor=? AND mentee=?");
			statement.setInt(1, mentor);
			statement.setInt(2, mentee);
			statement.execute();
		}
		catch (Exception e)
		{
			_log.warn("MenteeList: could not delete mentee objectId: " + mentee + " mentorId: " + mentor, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
}
