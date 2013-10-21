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
import lineage2.gameserver.model.ManageBbsBuffer;
import lineage2.gameserver.model.ManageBbsBuffer.SBufferScheme;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class CommunityBufferDAO {
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory
			.getLogger(CommunityBufferDAO.class);
	/**
	 * Field _instance.
	 */
	private static final CommunityBufferDAO _instance = new CommunityBufferDAO();
	/**
	 * Field SELECT_SQL_QUERY. (value is ""SELECT * FROM bbs_skillsave"")
	 */
	public static final String SELECT_SQL_QUERY = "SELECT * FROM bbs_skillsave";
	/**
	 * Field DELETE_SQL_QUERY. (value is ""DELETE FROM bbs_skillsave WHERE
	 * charId=? AND schameid=?"")
	 */
	public static final String DELETE_SQL_QUERY = "DELETE FROM bbs_skillsave WHERE charId=? AND schameid=?";
	/**
	 * Field INSERT_SQL_QUERY. (value is ""INSERT INTO bbs_skillsave
	 * (charId,schameid,name,skills) VALUES(?,?,?,?)"")
	 */
	public static final String INSERT_SQL_QUERY = "INSERT INTO bbs_skillsave (charId,schameid,name,skills) VALUES(?,?,?,?)";

	/**
	 * Method getInstance.
	 * 
	 * @return CommunityBufferDAO
	 */
	public static CommunityBufferDAO getInstance() {
		return _instance;
	}

	/**
	 * Method select.
	 */
	public void select() {
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try {
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(SELECT_SQL_QUERY);
			rset = statement.executeQuery();
			while (rset.next()) {
				SBufferScheme scheme = new SBufferScheme();
				scheme.id = rset.getInt("schameid");
				scheme.obj_id = rset.getInt("charId");
				scheme.name = rset.getString("name");
				scheme.skills_id = ManageBbsBuffer.StringToInt(rset
						.getString("skills"));
				ManageBbsBuffer.getInstance();
				ManageBbsBuffer.getSchemeList().add(scheme);
			}
		} catch (Exception e) {
			_log.error("CommunityBufferDAO.select():" + e, e);
		} finally {
			DbUtils.closeQuietly(con, statement, rset);
		}
	}

	/**
	 * Method delete.
	 * 
	 * @param scheme
	 *            SBufferScheme
	 */
	public void delete(SBufferScheme scheme) {
		Connection con = null;
		PreparedStatement statement = null;
		try {
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(DELETE_SQL_QUERY);
			statement.setInt(1, scheme.obj_id);
			statement.setInt(2, scheme.id);
			statement.execute();
			ManageBbsBuffer.getInstance();
			ManageBbsBuffer.getSchemeList().remove(scheme);
		} catch (Exception e) {
		} finally {
			DbUtils.closeQuietly(con, statement);
		}
	}

	/**
	 * Method insert.
	 * 
	 * @param scheme
	 *            SBufferScheme
	 */
	public void insert(SBufferScheme scheme)
	{
		Connection con = null;
		PreparedStatement stmt = null;
		scheme.id = ManageBbsBuffer.getAutoIncrement(1);
		String buff_list = ManageBbsBuffer.IntToString(scheme.skills_id);
		try
		{
			if(buff_list != "" || !buff_list.isEmpty()){
			con = DatabaseFactory.getInstance().getConnection();
			stmt = con.prepareStatement(INSERT_SQL_QUERY);
			stmt.setInt(1, scheme.obj_id);
			stmt.setInt(2, scheme.id);
			stmt.setString(3, scheme.name);
			stmt.setString(4, buff_list);
			stmt.execute();
			ManageBbsBuffer.getInstance();
			ManageBbsBuffer.getSchemeList().add(scheme);
			}
			
		}
		catch (Exception e)
		{
		}
		finally
		{
			DbUtils.closeQuietly(con, stmt);
		}
	}
}
