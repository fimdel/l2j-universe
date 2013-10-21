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
package lineage2.gameserver.instancemanager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import lineage2.commons.dbutils.DbUtils;
import lineage2.gameserver.Config;
import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.database.DatabaseFactory;
import lineage2.gameserver.database.mysql;
import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Spawner;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.instances.RaidBossInstance;
import lineage2.gameserver.model.instances.ReflectionBossInstance;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.tables.ClanTable;
import lineage2.gameserver.tables.GmListTable;
import lineage2.gameserver.templates.StatsSet;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.SqlBatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RaidBossSpawnManager
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(RaidBossSpawnManager.class);
	/**
	 * Field _instance.
	 */
	private static RaidBossSpawnManager _instance;
	/**
	 * Field _spawntable.
	 */
	protected static Map<Integer, Spawner> _spawntable = new ConcurrentHashMap<>();
	/**
	 * Field _storedInfo.
	 */
	protected static Map<Integer, StatsSet> _storedInfo;
	/**
	 * Field _points.
	 */
	protected static Map<Integer, Map<Integer, Integer>> _points;
	
	/**
	 * @author Mobius
	 */
	public static enum Status
	{
		/**
		 * Field ALIVE.
		 */
		ALIVE,
		/**
		 * Field DEAD.
		 */
		DEAD,
		/**
		 * Field UNDEFINED.
		 */
		UNDEFINED
	}
	
	/**
	 * Constructor for RaidBossSpawnManager.
	 */
	private RaidBossSpawnManager()
	{
		_instance = this;
		if (!Config.DONTLOADSPAWN)
		{
			reloadBosses();
		}
	}
	
	/**
	 * Method reloadBosses.
	 */
	public void reloadBosses()
	{
		loadStatus();
		restorePointsTable();
		calculateRanking();
	}
	
	/**
	 * Method cleanUp.
	 */
	public void cleanUp()
	{
		updateAllStatusDb();
		updatePointsDb();
		_storedInfo.clear();
		_spawntable.clear();
		_points.clear();
	}
	
	/**
	 * Method getInstance.
	 * @return RaidBossSpawnManager
	 */
	public static RaidBossSpawnManager getInstance()
	{
		if (_instance == null)
		{
			new RaidBossSpawnManager();
		}
		return _instance;
	}
	
	/**
	 * Method loadStatus.
	 */
	private void loadStatus()
	{
		_storedInfo = new ConcurrentHashMap<>();
		Connection con = null;
		Statement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			rset = con.createStatement().executeQuery("SELECT * FROM `raidboss_status`");
			while (rset.next())
			{
				int id = rset.getInt("id");
				StatsSet info = new StatsSet();
				info.set("current_hp", rset.getDouble("current_hp"));
				info.set("current_mp", rset.getDouble("current_mp"));
				info.set("respawn_delay", rset.getInt("respawn_delay"));
				_storedInfo.put(id, info);
			}
		}
		catch (Exception e)
		{
			_log.warn("RaidBossSpawnManager: Couldnt load raidboss statuses");
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
		_log.info("RaidBossSpawnManager: Loaded " + _storedInfo.size() + " Statuses");
	}
	
	/**
	 * Method updateAllStatusDb.
	 */
	private void updateAllStatusDb()
	{
		for (int id : _storedInfo.keySet())
		{
			updateStatusDb(id);
		}
	}
	
	/**
	 * Method updateStatusDb.
	 * @param id int
	 */
	private void updateStatusDb(int id)
	{
		Spawner spawner = _spawntable.get(id);
		if (spawner == null)
		{
			return;
		}
		StatsSet info = _storedInfo.get(id);
		if (info == null)
		{
			_storedInfo.put(id, info = new StatsSet());
		}
		NpcInstance raidboss = spawner.getFirstSpawned();
		if (raidboss instanceof ReflectionBossInstance)
		{
			return;
		}
		if (raidboss != null)
		{
			info.set("current_hp", raidboss.getCurrentHp());
			info.set("current_mp", raidboss.getCurrentMp());
			info.set("respawn_delay", 0);
		}
		else
		{
			info.set("current_hp", 0);
			info.set("current_mp", 0);
			info.set("respawn_delay", spawner.getRespawnTime());
		}
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("REPLACE INTO `raidboss_status` (id, current_hp, current_mp, respawn_delay) VALUES (?,?,?,?)");
			statement.setInt(1, id);
			statement.setDouble(2, info.getDouble("current_hp"));
			statement.setDouble(3, info.getDouble("current_mp"));
			statement.setInt(4, info.getInteger("respawn_delay", 0));
			statement.execute();
		}
		catch (SQLException e)
		{
			_log.warn("RaidBossSpawnManager: Couldnt update raidboss_status table");
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
	
	/**
	 * Method addNewSpawn.
	 * @param npcId int
	 * @param spawnDat Spawner
	 */
	public void addNewSpawn(int npcId, Spawner spawnDat)
	{
		if (_spawntable.containsKey(npcId))
		{
			return;
		}
		_spawntable.put(npcId, spawnDat);
		StatsSet info = _storedInfo.get(npcId);
		if (info != null)
		{
			spawnDat.setRespawnTime(info.getInteger("respawn_delay", 0));
		}
	}
	
	/**
	 * Method onBossSpawned.
	 * @param raidboss RaidBossInstance
	 */
	public void onBossSpawned(RaidBossInstance raidboss)
	{
		int bossId = raidboss.getNpcId();
		if (!_spawntable.containsKey(bossId))
		{
			return;
		}
		StatsSet info = _storedInfo.get(bossId);
		if ((info != null) && (info.getDouble("current_hp") > 1))
		{
			raidboss.setCurrentHp(info.getDouble("current_hp"), false);
			raidboss.setCurrentMp(info.getDouble("current_mp"));
		}
		GmListTable.broadcastMessageToGMs("Spawning RaidBoss " + raidboss.getName());
	}
	
	/**
	 * Method onBossDespawned.
	 * @param raidboss RaidBossInstance
	 */
	public void onBossDespawned(RaidBossInstance raidboss)
	{
		updateStatusDb(raidboss.getNpcId());
	}
	
	/**
	 * Method getRaidBossStatusId.
	 * @param bossId int
	 * @return Status
	 */
	public Status getRaidBossStatusId(int bossId)
	{
		Spawner spawner = _spawntable.get(bossId);
		if (spawner == null)
		{
			return Status.UNDEFINED;
		}
		NpcInstance npc = spawner.getFirstSpawned();
		return npc == null ? Status.DEAD : Status.ALIVE;
	}
	
	/**
	 * Method isDefined.
	 * @param bossId int
	 * @return boolean
	 */
	public boolean isDefined(int bossId)
	{
		return _spawntable.containsKey(bossId);
	}
	
	/**
	 * Method getSpawnTable.
	 * @return Map<Integer,Spawner>
	 */
	public Map<Integer, Spawner> getSpawnTable()
	{
		return _spawntable;
	}
	
	/**
	 * Field KEY_RANK.
	 */
	public static final Integer KEY_RANK = new Integer(-1);
	/**
	 * Field KEY_TOTAL_POINTS.
	 */
	public static final Integer KEY_TOTAL_POINTS = new Integer(0);
	/**
	 * Field pointsLock.
	 */
	private final Lock pointsLock = new ReentrantLock();
	
	/**
	 * Method restorePointsTable.
	 */
	private void restorePointsTable()
	{
		pointsLock.lock();
		_points = new ConcurrentHashMap<>();
		Connection con = null;
		Statement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.createStatement();
			rset = statement.executeQuery("SELECT owner_id, boss_id, points FROM `raidboss_points` ORDER BY owner_id ASC");
			int currentOwner = 0;
			Map<Integer, Integer> score = null;
			while (rset.next())
			{
				if (currentOwner != rset.getInt("owner_id"))
				{
					currentOwner = rset.getInt("owner_id");
					score = new HashMap<>();
					_points.put(currentOwner, score);
				}
				assert score != null;
				int bossId = rset.getInt("boss_id");
				NpcTemplate template = NpcHolder.getInstance().getTemplate(bossId);
				if ((bossId != KEY_RANK) && (bossId != KEY_TOTAL_POINTS) && (template != null) && (template.rewardRp > 0))
				{
					score.put(bossId, rset.getInt("points"));
				}
			}
		}
		catch (Exception e)
		{
			_log.warn("RaidBossSpawnManager: Couldnt load raidboss points");
			_log.error("", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
		pointsLock.unlock();
	}
	
	/**
	 * Method updatePointsDb.
	 */
	public void updatePointsDb()
	{
		pointsLock.lock();
		if (!mysql.set("DELETE FROM `raidboss_points`"))
		{
			_log.warn("RaidBossSpawnManager: Couldnt empty raidboss_points table");
		}
		if (_points.isEmpty())
		{
			pointsLock.unlock();
			return;
		}
		Connection con = null;
		Statement statement = null;
		StringBuilder sb;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.createStatement();
			SqlBatch b = new SqlBatch("INSERT INTO `raidboss_points` (owner_id, boss_id, points) VALUES");
			for (Map.Entry<Integer, Map<Integer, Integer>> pointEntry : _points.entrySet())
			{
				Map<Integer, Integer> tmpPoint = pointEntry.getValue();
				if ((tmpPoint == null) || tmpPoint.isEmpty())
				{
					continue;
				}
				for (Map.Entry<Integer, Integer> pointListEntry : tmpPoint.entrySet())
				{
					if (KEY_RANK.equals(pointListEntry.getKey()) || KEY_TOTAL_POINTS.equals(pointListEntry.getKey()) || (pointListEntry.getValue() == null) || (pointListEntry.getValue() == 0))
					{
						continue;
					}
					sb = new StringBuilder("(");
					sb.append(pointEntry.getKey()).append(',');
					sb.append(pointListEntry.getKey()).append(',');
					sb.append(pointListEntry.getValue()).append(')');
					b.write(sb.toString());
				}
			}
			if (!b.isEmpty())
			{
				statement.executeUpdate(b.close());
			}
		}
		catch (SQLException e)
		{
			_log.warn("RaidBossSpawnManager: Couldnt update raidboss_points table");
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
		pointsLock.unlock();
	}
	
	/**
	 * Method addPoints.
	 * @param ownerId int
	 * @param bossId int
	 * @param points int
	 */
	public void addPoints(int ownerId, int bossId, int points)
	{
		if ((points <= 0) || (ownerId <= 0) || (bossId <= 0))
		{
			return;
		}
		pointsLock.lock();
		Map<Integer, Integer> pointsTable = _points.get(ownerId);
		if (pointsTable == null)
		{
			pointsTable = new HashMap<>();
			_points.put(ownerId, pointsTable);
		}
		if (pointsTable.isEmpty())
		{
			pointsTable.put(bossId, points);
		}
		else
		{
			Integer currentPoins = pointsTable.get(bossId);
			pointsTable.put(bossId, currentPoins == null ? points : currentPoins + points);
		}
		pointsLock.unlock();
	}
	
	/**
	 * Method calculateRanking.
	 * @return TreeMap<Integer,Integer>
	 */
	public TreeMap<Integer, Integer> calculateRanking()
	{
		TreeMap<Integer, Integer> tmpRanking = new TreeMap<>();
		pointsLock.lock();
		for (Map.Entry<Integer, Map<Integer, Integer>> point : _points.entrySet())
		{
			Map<Integer, Integer> tmpPoint = point.getValue();
			tmpPoint.remove(KEY_RANK);
			tmpPoint.remove(KEY_TOTAL_POINTS);
			int totalPoints = 0;
			for (Entry<Integer, Integer> e : tmpPoint.entrySet())
			{
				totalPoints += e.getValue();
			}
			if (totalPoints != 0)
			{
				tmpPoint.put(KEY_TOTAL_POINTS, totalPoints);
				tmpRanking.put(totalPoints, point.getKey());
			}
		}
		int ranking = 1;
		for (Entry<Integer, Integer> entry : tmpRanking.descendingMap().entrySet())
		{
			Map<Integer, Integer> tmpPoint = _points.get(entry.getValue());
			tmpPoint.put(KEY_RANK, ranking);
			ranking++;
		}
		pointsLock.unlock();
		return tmpRanking;
	}
	
	/**
	 * Method distributeRewards.
	 */
	public void distributeRewards()
	{
		pointsLock.lock();
		TreeMap<Integer, Integer> ranking = calculateRanking();
		Iterator<Integer> e = ranking.descendingMap().values().iterator();
		int counter = 1;
		while (e.hasNext() && (counter <= 100))
		{
			int reward = 0;
			int playerId = e.next();
			if (counter == 1)
			{
				reward = 2500;
			}
			else if (counter == 2)
			{
				reward = 1800;
			}
			else if (counter == 3)
			{
				reward = 1400;
			}
			else if (counter == 4)
			{
				reward = 1200;
			}
			else if (counter == 5)
			{
				reward = 900;
			}
			else if (counter == 6)
			{
				reward = 700;
			}
			else if (counter == 7)
			{
				reward = 600;
			}
			else if (counter == 8)
			{
				reward = 400;
			}
			else if (counter == 9)
			{
				reward = 300;
			}
			else if (counter == 10)
			{
				reward = 200;
			}
			else if (counter <= 50)
			{
				reward = 50;
			}
			else if (counter <= 100)
			{
				reward = 25;
			}
			Player player = GameObjectsStorage.getPlayer(playerId);
			Clan clan = null;
			if (player != null)
			{
				clan = player.getClan();
			}
			else
			{
				clan = ClanTable.getInstance().getClan(mysql.simple_get_int("clanid", "characters", "obj_Id=" + playerId));
			}
			if (clan != null)
			{
				clan.incReputation(reward, true, "RaidPoints");
			}
			counter++;
		}
		_points.clear();
		updatePointsDb();
		pointsLock.unlock();
	}
	
	/**
	 * Method getPoints.
	 * @return Map<Integer,Map<Integer,Integer>>
	 */
	public Map<Integer, Map<Integer, Integer>> getPoints()
	{
		return _points;
	}
	
	/**
	 * Method getPointsForOwnerId.
	 * @param ownerId int
	 * @return Map<Integer,Integer>
	 */
	public Map<Integer, Integer> getPointsForOwnerId(int ownerId)
	{
		return _points.get(ownerId);
	}
}
