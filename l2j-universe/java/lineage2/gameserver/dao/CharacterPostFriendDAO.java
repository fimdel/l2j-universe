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
import lineage2.gameserver.model.Player;

import org.napile.primitive.maps.IntObjectMap;
import org.napile.primitive.maps.impl.CHashIntObjectMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class CharacterPostFriendDAO
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(CharacterPostFriendDAO.class);
	/**
	 * Field _instance.
	 */
	private static final CharacterPostFriendDAO _instance = new CharacterPostFriendDAO();
	/**
	 * Field SELECT_SQL_QUERY. (value is ""SELECT pf.post_friend, c.char_name FROM character_post_friends pf LEFT JOIN characters c ON pf.post_friend = c.obj_Id WHERE pf.object_id = ?"")
	 */
	private static final String SELECT_SQL_QUERY = "SELECT pf.post_friend, c.char_name FROM character_post_friends pf LEFT JOIN characters c ON pf.post_friend = c.obj_Id WHERE pf.object_id = ?";
	/**
	 * Field INSERT_SQL_QUERY. (value is ""INSERT INTO character_post_friends(object_id, post_friend) VALUES (?,?)"")
	 */
	private static final String INSERT_SQL_QUERY = "INSERT INTO character_post_friends(object_id, post_friend) VALUES (?,?)";
	/**
	 * Field DELETE_SQL_QUERY. (value is ""DELETE FROM character_post_friends WHERE object_id=? AND post_friend=?"")
	 */
	private static final String DELETE_SQL_QUERY = "DELETE FROM character_post_friends WHERE object_id=? AND post_friend=?";
	
	/**
	 * Method getInstance.
	 * @return CharacterPostFriendDAO
	 */
	public static CharacterPostFriendDAO getInstance()
	{
		return _instance;
	}
	
	/**
	 * Method select.
	 * @param player Player
	 * @return IntObjectMap<String>
	 */
	public IntObjectMap<String> select(Player player)
	{
		IntObjectMap<String> set = new CHashIntObjectMap<>();
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(SELECT_SQL_QUERY);
			statement.setInt(1, player.getObjectId());
			rset = statement.executeQuery();
			while (rset.next())
			{
				set.put(rset.getInt(1), rset.getString(2));
			}
		}
		catch (Exception e)
		{
			_log.error("CharacterPostFriendDAO.load(L2Player): "+ player.getObjectId() + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
		return set;
	}
	
	/**
	 * Method insert.
	 * @param player Player
	 * @param val int
	 */
	public void insert(Player player, int val)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(INSERT_SQL_QUERY);
			statement.setInt(1, player.getObjectId());
			statement.setInt(2, val);
			statement.execute();
		}
		catch (Exception e)
		{
			_log.error("CharacterPostFriendDAO.insert(L2Player, int): " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
	
	/**
	 * Method delete.
	 * @param player Player
	 * @param val int
	 */
	public void delete(Player player, int val)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(DELETE_SQL_QUERY);
			statement.setInt(1, player.getObjectId());
			statement.setInt(2, val);
			statement.execute();
		}
		catch (Exception e)
		{
			_log.error("CharacterPostFriendDAO.delete(L2Player, int): " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
}
