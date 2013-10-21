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
package lineage2.loginserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import lineage2.commons.dbutils.DbUtils;
import lineage2.loginserver.database.L2DatabaseFactory;
import lineage2.loginserver.gameservercon.GameServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class GameServerManager
{
	/**
	 * Field _log.
	 */
	private static Logger _log = LoggerFactory.getLogger(GameServerManager.class);
	/**
	 * Field _instance.
	 */
	private static final GameServerManager _instance = new GameServerManager();
	
	/**
	 * Method getInstance.
	 * @return GameServerManager
	 */
	public static final GameServerManager getInstance()
	{
		return _instance;
	}
	
	/**
	 * Field _gameServers.
	 */
	private final Map<Integer, GameServer> _gameServers = new TreeMap<>();
	/**
	 * Field _lock.
	 */
	private final ReadWriteLock _lock = new ReentrantReadWriteLock();
	/**
	 * Field _readLock.
	 */
	private final Lock _readLock = _lock.readLock();
	/**
	 * Field _writeLock.
	 */
	private final Lock _writeLock = _lock.writeLock();
	
	/**
	 * Constructor for GameServerManager.
	 */
	public GameServerManager()
	{
		load();
		_log.info("Loaded " + _gameServers.size() + " registered GameServer(s).");
	}
	
	/**
	 * Method load.
	 */
	private void load()
	{
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = L2DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT server_id FROM gameservers");
			rset = statement.executeQuery();
			int id;
			while (rset.next())
			{
				id = rset.getInt("server_id");
				GameServer gs = new GameServer(id);
				_gameServers.put(id, gs);
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
	 * Method getGameServers.
	 * @return GameServer[]
	 */
	public GameServer[] getGameServers()
	{
		_readLock.lock();
		try
		{
			return _gameServers.values().toArray(new GameServer[_gameServers.size()]);
		}
		finally
		{
			_readLock.unlock();
		}
	}
	
	/**
	 * Method getGameServerById.
	 * @param id int
	 * @return GameServer
	 */
	public GameServer getGameServerById(int id)
	{
		_readLock.lock();
		try
		{
			return _gameServers.get(id);
		}
		finally
		{
			_readLock.unlock();
		}
	}
	
	/**
	 * Method registerGameServer.
	 * @param gs GameServer
	 * @return boolean
	 */
	public boolean registerGameServer(GameServer gs)
	{
		if (!Config.ACCEPT_NEW_GAMESERVER)
		{
			return false;
		}
		_writeLock.lock();
		try
		{
			int id = 1;
			while (id++ > 0)
			{
				GameServer pgs = _gameServers.get(id);
				if ((pgs == null) || !pgs.isAuthed())
				{
					_gameServers.put(id, gs);
					gs.setId(id);
					return true;
				}
			}
		}
		finally
		{
			_writeLock.unlock();
		}
		return false;
	}
	
	/**
	 * Method registerGameServer.
	 * @param id int
	 * @param gs GameServer
	 * @return boolean
	 */
	public boolean registerGameServer(int id, GameServer gs)
	{
		_writeLock.lock();
		try
		{
			GameServer pgs = _gameServers.get(id);
			if (!Config.ACCEPT_NEW_GAMESERVER && (pgs == null))
			{
				return false;
			}
			if ((pgs == null) || !pgs.isAuthed())
			{
				_gameServers.put(id, gs);
				gs.setId(id);
				return true;
			}
		}
		finally
		{
			_writeLock.unlock();
		}
		return false;
	}
}
