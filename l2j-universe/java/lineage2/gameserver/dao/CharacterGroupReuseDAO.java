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
import lineage2.gameserver.skills.TimeStamp;
import lineage2.gameserver.utils.SqlBatch;

import org.napile.primitive.maps.IntObjectMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class CharacterGroupReuseDAO
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(CharacterGroupReuseDAO.class);
	/**
	 * Field _instance.
	 */
	private static CharacterGroupReuseDAO _instance = new CharacterGroupReuseDAO();
	/**
	 * Field DELETE_SQL_QUERY. (value is ""DELETE FROM character_group_reuse WHERE object_id=?"")
	 */
	public static final String DELETE_SQL_QUERY = "DELETE FROM character_group_reuse WHERE object_id=?";
	/**
	 * Field SELECT_SQL_QUERY. (value is ""SELECT * FROM character_group_reuse WHERE object_id=?"")
	 */
	public static final String SELECT_SQL_QUERY = "SELECT * FROM character_group_reuse WHERE object_id=?";
	/**
	 * Field INSERT_SQL_QUERY. (value is ""REPLACE INTO `character_group_reuse` (`object_id`,`reuse_group`,`item_id`,`end_time`,`reuse`) VALUES"")
	 */
	public static final String INSERT_SQL_QUERY = "REPLACE INTO `character_group_reuse` (`object_id`,`reuse_group`,`item_id`,`end_time`,`reuse`) VALUES";
	
	/**
	 * Method getInstance.
	 * @return CharacterGroupReuseDAO
	 */
	public static CharacterGroupReuseDAO getInstance()
	{
		return _instance;
	}
	
	/**
	 * Method select.
	 * @param player Player
	 */
	public void select(Player player)
	{
		long curTime = System.currentTimeMillis();
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
				int group = rset.getInt("reuse_group");
				int item_id = rset.getInt("item_id");
				long endTime = rset.getLong("end_time");
				long reuse = rset.getLong("reuse");
				if ((endTime - curTime) > 500)
				{
					TimeStamp stamp = new TimeStamp(item_id, endTime, reuse);
					player.addSharedGroupReuse(group, stamp);
				}
			}
			DbUtils.close(statement);
			statement = con.prepareStatement(DELETE_SQL_QUERY);
			statement.setInt(1, player.getObjectId());
			statement.execute();
		}
		catch (Exception e)
		{
			_log.error("CharacterGroupReuseDAO.select(L2Player):", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
	}
	
	/**
	 * Method insert.
	 * @param player Player
	 */
	public void insert(Player player)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(DELETE_SQL_QUERY);
			statement.setInt(1, player.getObjectId());
			statement.execute();
			if (player.getSharedGroupReuses().isEmpty())
			{
				return;
			}
			SqlBatch b = new SqlBatch(INSERT_SQL_QUERY);
			synchronized (player.getSharedGroupReuses())
			{
				for (IntObjectMap.Entry<TimeStamp> entry : player.getSharedGroupReuses())
				{
					int group = entry.getKey();
					TimeStamp timeStamp = entry.getValue();
					if (timeStamp.hasNotPassed())
					{
						StringBuilder sb = new StringBuilder("(");
						sb.append(player.getObjectId()).append(',');
						sb.append(group).append(',');
						sb.append(timeStamp.getId()).append(',');
						sb.append(timeStamp.getEndTime()).append(',');
						sb.append(timeStamp.getReuseBasic()).append(')');
						b.write(sb.toString());
					}
				}
			}
			if (!b.isEmpty())
			{
				statement.executeUpdate(b.close());
			}
		}
		catch (final Exception e)
		{
			_log.error("CharacterGroupReuseDAO.insert(L2Player):", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
}
