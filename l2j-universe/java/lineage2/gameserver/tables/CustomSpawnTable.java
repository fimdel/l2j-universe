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
package lineage2.gameserver.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import lineage2.commons.dbutils.DbUtils;
import lineage2.gameserver.Config;
import lineage2.gameserver.data.xml.holder.SpawnHolder;
import lineage2.gameserver.database.DatabaseFactory;
import lineage2.gameserver.model.SimpleSpawner;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.templates.StatsSet;
import lineage2.gameserver.templates.spawn.PeriodOfDay;
import lineage2.gameserver.templates.spawn.SpawnNpcInfo;
import lineage2.gameserver.templates.spawn.SpawnTemplate;
import lineage2.gameserver.utils.Location;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class CustomSpawnTable
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(CustomSpawnTable.class);
	/**
	 * Field _instance.
	 */
	private static CustomSpawnTable _instance;
	
	/**
	 * Method getInstance.
	 * @return CustomSpawnTable
	 */
	public static CustomSpawnTable getInstance()
	{
		if (_instance == null)
		{
			new CustomSpawnTable();
		}
		return _instance;
	}
	
	/**
	 * Constructor for CustomSpawnTable.
	 */
	private CustomSpawnTable()
	{
		_instance = this;
		if (Config.LOAD_GM_SPAWN_CUSTOM)
		{
			fillCustomSpawnTable();
		}
		fillSpawnTable();
	}
	
	/**
	 * Method fillSpawnTable.
	 */
	private void fillSpawnTable()
	{
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT * FROM spawnlist ORDER by npc_templateid");
			rset = statement.executeQuery();
			SpawnTemplate template;
			while (rset.next())
			{
				int count = rset.getInt("count");
				int delay = rset.getInt("respawn_delay");
				int delay_rnd = rset.getInt("respawn_delay_rnd");
				int npcId = rset.getInt("npc_templateid");
				int x = rset.getInt("locx");
				int y = rset.getInt("locy");
				int z = rset.getInt("locz");
				int h = rset.getInt("heading");
				template = new SpawnTemplate(PeriodOfDay.NONE, count, delay, delay_rnd);
				template.addNpc(new SpawnNpcInfo(npcId, 1, StatsSet.EMPTY));
				template.addSpawnRange(new Location(x, y, z, h));
				SpawnHolder.getInstance().addSpawn(PeriodOfDay.NONE.name(), template);
			}
		}
		catch (Exception e1)
		{
			_log.warn("spawn couldnt be initialized:" + e1);
			e1.printStackTrace();
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
	}
	
	/**
	 * Method fillCustomSpawnTable.
	 */
	private void fillCustomSpawnTable()
	{
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT * FROM custom_spawnlist ORDER by npc_templateid");
			rset = statement.executeQuery();
			SpawnTemplate template;
			while (rset.next())
			{
				int count = rset.getInt("count");
				int delay = rset.getInt("respawn_delay");
				int delay_rnd = rset.getInt("respawn_delay_rnd");
				int npcId = rset.getInt("npc_templateid");
				int x = rset.getInt("locx");
				int y = rset.getInt("locy");
				int z = rset.getInt("locz");
				int h = rset.getInt("heading");
				template = new SpawnTemplate(PeriodOfDay.NONE, count, delay, delay_rnd);
				template.addNpc(new SpawnNpcInfo(npcId, 1, StatsSet.EMPTY));
				template.addSpawnRange(new Location(x, y, z, h));
				SpawnHolder.getInstance().addSpawn(PeriodOfDay.NONE.name(), template);
			}
		}
		catch (Exception e1)
		{
			_log.warn("custom_spawnlist couldnt be initialized:" + e1);
			e1.printStackTrace();
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
			_log.info("CustomSpawn: NPC Loading completed");
		}
	}
	
	/**
	 * Method addNewSpawn.
	 * @param spawn SimpleSpawner
	 */
	public void addNewSpawn(SimpleSpawner spawn)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("INSERT INTO `custom_spawnlist` (location,count,npc_templateid,locx,locy,locz,heading,respawn_delay) values(?,?,?,?,?,?,?,?)");
			statement.setString(1, spawn.getLoc().toString());
			statement.setInt(2, spawn.getAmount());
			statement.setInt(3, spawn.getCurrentNpcId());
			statement.setInt(4, spawn.getLocx());
			statement.setInt(5, spawn.getLocy());
			statement.setInt(6, spawn.getLocz());
			statement.setInt(7, spawn.getHeading());
			statement.setInt(8, spawn.getRespawnDelay());
			statement.execute();
		}
		catch (Exception e1)
		{
			_log.warn("spawn couldnt be stored in db:" + e1);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
	
	/**
	 * Method deleteSpawn.
	 * @param spawn NpcInstance
	 */
	public void deleteSpawn(NpcInstance spawn)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("DELETE FROM custom_spawnlist WHERE locx=? AND locy=? AND locz=? AND npc_templateid=? AND heading=?");
			statement.setInt(1, spawn.getLoc().getX());
			statement.setInt(2, spawn.getLoc().getY());
			statement.setInt(3, spawn.getLoc().getZ());
			statement.setInt(4, spawn.getNpcId());
			statement.setInt(5, spawn.getHeading());
			statement.execute();
		}
		catch (Exception e1)
		{
			_log.warn("spawn couldnt be deleted in db:" + e1);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
}
