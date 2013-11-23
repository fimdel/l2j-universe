package l2p.gameserver.dao;

import l2p.commons.dbutils.DbUtils;
import l2p.gameserver.database.DatabaseFactory;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.actor.instances.player.Friend;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

/**
 * @author VISTALL
 * @date 23:27/22.03.2011
 */
public class CharacterFriendDAO {
    private static final Logger _log = LoggerFactory.getLogger(CharacterFriendDAO.class);

    private static final CharacterFriendDAO _instance = new CharacterFriendDAO();

    public static CharacterFriendDAO getInstance() {
        return _instance;
    }

    public Map<Integer, Friend> select(Player owner) {
        Map<Integer, Friend> map = new HashMap<Integer, Friend>();
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet rset = null;
        try {
            con = DatabaseFactory.getInstance().getConnection();
            statement = con.prepareStatement("SELECT f.friend_id, c.char_name, s.class_id, s.level FROM character_friends f LEFT JOIN characters c ON f.friend_id = c.obj_Id LEFT JOIN character_subclasses s ON ( f.friend_id = s.char_obj_id AND s.active =1 ) WHERE f.char_id = ?");
            statement.setInt(1, owner.getObjectId());
            rset = statement.executeQuery();
            while (rset.next()) {
                int objectId = rset.getInt("f.friend_id");
                String name = rset.getString("c.char_name");
                int classId = rset.getInt("s.class_id");
                int level = rset.getInt("s.level");

                map.put(objectId, new Friend(objectId, name, level, classId));
            }
        } catch (Exception e) {
            _log.error("CharacterFriendDAO.load(L2Player): " + e, e);
        } finally {
            DbUtils.closeQuietly(con, statement, rset);
        }
        return map;
    }

    public void insert(Player owner, Player friend) {
        Connection con = null;
        PreparedStatement statement = null;
        try {
            con = DatabaseFactory.getInstance().getConnection();
            statement = con.prepareStatement("INSERT INTO character_friends (char_id,friend_id) VALUES(?,?)");
            statement.setInt(1, owner.getObjectId());
            statement.setInt(2, friend.getObjectId());
            statement.execute();
        } catch (Exception e) {
            _log.warn(owner.getFriendList() + " could not add friend objectid: " + friend.getObjectId(), e);
        } finally {
            DbUtils.closeQuietly(con, statement);
        }
    }

    public void delete(Player owner, int friend) {
        Connection con = null;
        PreparedStatement statement = null;
        try {
            con = DatabaseFactory.getInstance().getConnection();
            statement = con.prepareStatement("DELETE FROM character_friends WHERE (char_id=? AND friend_id=?) OR (char_id=? AND friend_id=?)");
            statement.setInt(1, owner.getObjectId());
            statement.setInt(2, friend);
            statement.setInt(3, friend);
            statement.setInt(4, owner.getObjectId());
            statement.execute();
        } catch (Exception e) {
            _log.warn("FriendList: could not delete friend objectId: " + friend + " ownerId: " + owner.getObjectId(), e);
        } finally {
            DbUtils.closeQuietly(con, statement);
        }
    }

}
