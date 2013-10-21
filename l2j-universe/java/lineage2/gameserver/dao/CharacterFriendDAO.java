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
import lineage2.gameserver.model.actor.instances.player.Friend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class CharacterFriendDAO
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(CharacterFriendDAO.class);
	/**
	 * Field _instance.
	 */
	private static final CharacterFriendDAO _instance = new CharacterFriendDAO();
	
	/**
	 * Method getInstance.
	 * @return CharacterFriendDAO
	 */
	public static CharacterFriendDAO getInstance()
	{
		return _instance;
	}
	
	/**
	 * Method select.
	 * @param owner Player
	 * @return Map<Integer,Friend>
	 */
	public Map<Integer, Friend> select(Player owner)
	{
		Map<Integer, Friend> map = new HashMap<>();
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT f.friend_id, c.char_name, s.class_id, s.level FROM character_friends f LEFT JOIN characters c ON f.friend_id = c.obj_Id LEFT JOIN character_subclasses s ON ( f.friend_id = s.char_obj_id AND s.active =1 ) WHERE f.char_id = ?");
			statement.setInt(1, owner.getObjectId());
			rset = statement.executeQuery();
			while (rset.next())
			{
				int objectId = rset.getInt("f.friend_id");
				String name = rset.getString("c.char_name");
				int classId = rset.getInt("s.class_id");
				int level = rset.getInt("s.level");
				map.put(objectId, new Friend(objectId, name, level, classId));
			}
		}
		catch (Exception e)
		{
			_log.error("CharacterFriendDAO.load(L2Player): " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
		return map;
	}
	
	/**
	 * Method insert.
	 * @param owner Player
	 * @param friend Player
	 */
	public void insert(Player owner, Player friend)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("INSERT INTO character_friends (char_id,friend_id) VALUES(?,?)");
			statement.setInt(1, owner.getObjectId());
			statement.setInt(2, friend.getObjectId());
			statement.execute();
		}
		catch (Exception e)
		{
			_log.warn(owner.getFriendList() + " could not add friend objectid: " + friend.getObjectId(), e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
	
	/**
	 * Method delete.
	 * @param owner Player
	 * @param friend int
	 */
	public void delete(Player owner, int friend)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("DELETE FROM character_friends WHERE (char_id=? AND friend_id=?) OR (char_id=? AND friend_id=?)");
			statement.setInt(1, owner.getObjectId());
			statement.setInt(2, friend);
			statement.setInt(3, friend);
			statement.setInt(4, owner.getObjectId());
			statement.execute();
		}
		catch (Exception e)
		{
			_log.warn("FriendList: could not delete friend objectId: " + friend + " ownerId: " + owner.getObjectId(), e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
}
