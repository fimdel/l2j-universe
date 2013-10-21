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
package lineage2.gameserver.instancemanager.games;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import lineage2.commons.dbutils.DbUtils;
import lineage2.gameserver.Config;
import lineage2.gameserver.database.DatabaseFactory;
import lineage2.gameserver.model.Player;

import org.napile.primitive.comparators.IntComparator;
import org.napile.primitive.maps.IntObjectMap;
import org.napile.primitive.maps.impl.CTreeIntObjectMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class MiniGameScoreManager
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(MiniGameScoreManager.class);
	/**
	 * Field _scores.
	 */
	private final IntObjectMap<Set<String>> _scores = new CTreeIntObjectMap<>(new IntComparator()
	{
		@Override
		public int compare(int o1, int o2)
		{
			return o2 - o1;
		}
	});
	/**
	 * Field _instance.
	 */
	private static MiniGameScoreManager _instance = new MiniGameScoreManager();
	
	/**
	 * Method getInstance.
	 * @return MiniGameScoreManager
	 */
	public static MiniGameScoreManager getInstance()
	{
		return _instance;
	}
	
	/**
	 * Constructor for MiniGameScoreManager.
	 */
	private MiniGameScoreManager()
	{
		if (Config.EX_JAPAN_MINIGAME)
		{
			load();
		}
	}
	
	/**
	 * Method load.
	 */
	private void load()
	{
		Connection con = null;
		Statement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.createStatement();
			rset = statement.executeQuery("SELECT characters.char_name AS name, character_minigame_score.score AS score FROM characters, character_minigame_score WHERE characters.obj_Id=character_minigame_score.object_id");
			while (rset.next())
			{
				String name = rset.getString("name");
				int score = rset.getInt("score");
				addScore(name, score);
			}
		}
		catch (SQLException e)
		{
			_log.info("Exception: " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
	}
	
	/**
	 * Method insertScore.
	 * @param player Player
	 * @param score int
	 */
	public void insertScore(Player player, int score)
	{
		if (addScore(player.getName(), score))
		{
			Connection con = null;
			PreparedStatement statement = null;
			try
			{
				con = DatabaseFactory.getInstance().getConnection();
				statement = con.prepareStatement("INSERT INTO character_minigame_score(object_id, score) VALUES (?, ?)");
				statement.setInt(1, player.getObjectId());
				statement.setInt(2, score);
				statement.execute();
			}
			catch (final Exception e)
			{
				_log.info("Exception: " + e, e);
			}
			finally
			{
				DbUtils.closeQuietly(con, statement);
			}
		}
	}
	
	/**
	 * Method addScore.
	 * @param name String
	 * @param score int
	 * @return boolean
	 */
	public boolean addScore(String name, int score)
	{
		Set<String> set = _scores.get(score);
		if (set == null)
		{
			_scores.put(score, (set = new CopyOnWriteArraySet<>()));
		}
		return set.add(name);
	}
	
	/**
	 * Method getScores.
	 * @return IntObjectMap<Set<String>>
	 */
	public IntObjectMap<Set<String>> getScores()
	{
		return _scores;
	}
}
