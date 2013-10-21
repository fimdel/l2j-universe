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
package bosses;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

import lineage2.commons.dbutils.DbUtils;
import lineage2.gameserver.database.DatabaseFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class EpicBossState
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(EpicBossState.class);
	
	/**
	 * @author Mobius
	 */
	public static enum State
	{
		/**
		 * Field NOTSPAWN.
		 */
		NOTSPAWN,
		/**
		 * Field ALIVE.
		 */
		ALIVE,
		/**
		 * Field DEAD.
		 */
		DEAD,
		/**
		 * Field INTERVAL.
		 */
		INTERVAL
	}
	
	/**
	 * Field _bossId.
	 */
	private int _bossId;
	/**
	 * Field _respawnDate.
	 */
	private long _respawnDate;
	/**
	 * Field _state.
	 */
	private State _state;
	
	/**
	 * Method getBossId.
	 * @return int
	 */
	public int getBossId()
	{
		return _bossId;
	}
	
	/**
	 * Method setBossId.
	 * @param newId int
	 */
	public void setBossId(int newId)
	{
		_bossId = newId;
	}
	
	/**
	 * Method getState.
	 * @return State
	 */
	public State getState()
	{
		return _state;
	}
	
	/**
	 * Method setState.
	 * @param newState State
	 */
	public void setState(State newState)
	{
		_state = newState;
	}
	
	/**
	 * Method getRespawnDate.
	 * @return long
	 */
	public long getRespawnDate()
	{
		return _respawnDate;
	}
	
	/**
	 * Method setRespawnDate.
	 * @param interval long
	 */
	public void setRespawnDate(long interval)
	{
		_respawnDate = interval + System.currentTimeMillis();
	}
	
	/**
	 * Constructor for EpicBossState.
	 * @param bossId int
	 */
	public EpicBossState(int bossId)
	{
		this(bossId, true);
	}
	
	/**
	 * Constructor for EpicBossState.
	 * @param bossId int
	 * @param isDoLoad boolean
	 */
	public EpicBossState(int bossId, boolean isDoLoad)
	{
		_bossId = bossId;
		if (isDoLoad)
		{
			load();
		}
	}
	
	/**
	 * Method load.
	 */
	public void load()
	{
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT * FROM epic_boss_spawn WHERE bossId = ? LIMIT 1");
			statement.setInt(1, _bossId);
			rset = statement.executeQuery();
			if (rset.next())
			{
				_respawnDate = rset.getLong("respawnDate") * 1000L;
				if ((_respawnDate - System.currentTimeMillis()) <= 0)
				{
					_state = State.NOTSPAWN;
				}
				else
				{
					final int tempState = rset.getInt("state");
					if (tempState == State.NOTSPAWN.ordinal())
					{
						_state = State.NOTSPAWN;
					}
					else if (tempState == State.INTERVAL.ordinal())
					{
						_state = State.INTERVAL;
					}
					else if (tempState == State.ALIVE.ordinal())
					{
						_state = State.ALIVE;
					}
					else if (tempState == State.DEAD.ordinal())
					{
						_state = State.DEAD;
					}
					else
					{
						_state = State.NOTSPAWN;
					}
				}
			}
		}
		catch (Exception e)
		{
			_log.error("", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
	}
	
	/**
	 * Method save.
	 */
	public void save()
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("REPLACE INTO epic_boss_spawn (bossId,respawnDate,state) VALUES(?,?,?)");
			statement.setInt(1, _bossId);
			statement.setInt(2, (int) (_respawnDate / 1000));
			statement.setInt(3, _state.ordinal());
			statement.execute();
		}
		catch (Exception e)
		{
			_log.error("", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
	
	/**
	 * Method update.
	 */
	public void update()
	{
		Connection con = null;
		Statement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.createStatement();
			statement.executeUpdate("UPDATE epic_boss_spawn SET respawnDate=" + (_respawnDate / 1000) + ", state=" + _state.ordinal() + " WHERE bossId=" + _bossId);
			final Date dt = new Date(_respawnDate);
			_log.info("update EpicBossState: ID:" + _bossId + ", RespawnDate:" + dt + ", State:" + _state.toString());
		}
		catch (Exception e)
		{
			_log.error("Exception on update EpicBossState: ID " + _bossId + ", RespawnDate:" + (_respawnDate / 1000) + ", State:" + _state.toString(), e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
	
	/**
	 * Method setNextRespawnDate.
	 * @param newRespawnDate long
	 */
	public void setNextRespawnDate(long newRespawnDate)
	{
		_respawnDate = newRespawnDate;
	}
	
	/**
	 * Method getInterval.
	 * @return long
	 */
	public long getInterval()
	{
		final long interval = _respawnDate - System.currentTimeMillis();
		return (interval > 0) ? interval : 0;
	}
}
