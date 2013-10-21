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
package lineage2.gameserver.cache;

import gnu.trove.map.hash.TIntIntHashMap;
import gnu.trove.map.hash.TIntObjectHashMap;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import lineage2.commons.dbutils.DbUtils;
import lineage2.gameserver.database.DatabaseFactory;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class CrestCache
{
	/**
	 * Field ALLY_CREST_SIZE. (value is 192)
	 */
	public static final int ALLY_CREST_SIZE = 192;
	/**
	 * Field CREST_SIZE. (value is 256)
	 */
	public static final int CREST_SIZE = 256;
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(CrestCache.class);
	/**
	 * Field _instance.
	 */
	private final static CrestCache _instance = new CrestCache();
	
	/**
	 * Method getInstance.
	 * @return CrestCache
	 */
	public final static CrestCache getInstance()
	{
		return _instance;
	}
	
	/**
	 * Field _pledgeCrestId.
	 */
	private final TIntIntHashMap _pledgeCrestId = new TIntIntHashMap();
	/**
	 * Field _pledgeCrestLargeId.
	 */
	private final TIntIntHashMap _pledgeCrestLargeId = new TIntIntHashMap();
	/**
	 * Field _allyCrestId.
	 */
	private final TIntIntHashMap _allyCrestId = new TIntIntHashMap();
	/**
	 * Field _pledgeCrest.
	 */
	private final TIntObjectHashMap<byte[]> _pledgeCrest = new TIntObjectHashMap<>();
	/**
	 * Field _pledgeCrestLarge.
	 */
	private final TIntObjectHashMap<byte[]> _pledgeCrestLarge = new TIntObjectHashMap<>();
	/**
	 * Field _allyCrest.
	 */
	private final TIntObjectHashMap<byte[]> _allyCrest = new TIntObjectHashMap<>();
	/**
	 * Field lock.
	 */
	private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	/**
	 * Field readLock.
	 */
	private final Lock readLock = lock.readLock();
	/**
	 * Field writeLock.
	 */
	private final Lock writeLock = lock.writeLock();

	public byte[] crestLargeTmp = null;
	
	/**
	 * Constructor for CrestCache.
	 */
	private CrestCache()
	{
		load();
	}
	
	/**
	 * Method load.
	 */
	public void load()
	{
		int count = 0;
		int pledgeId, crestId;
		byte[] crest;
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT clan_id, crest FROM clan_data WHERE crest IS NOT NULL");
			rset = statement.executeQuery();
			while (rset.next())
			{
				count++;
				pledgeId = rset.getInt("clan_id");
				crest = rset.getBytes("crest");
				crestId = getCrestId(pledgeId, crest);
				_pledgeCrestId.put(pledgeId, crestId);
				_pledgeCrest.put(crestId, crest);
			}
			DbUtils.close(statement, rset);
			statement = con.prepareStatement("SELECT clan_id, largecrest FROM clan_data WHERE largecrest IS NOT NULL");
			rset = statement.executeQuery();
			while (rset.next())
			{
				count++;
				pledgeId = rset.getInt("clan_id");
				crest = rset.getBytes("largecrest");
				crestId = getCrestId(pledgeId, crest);
				_pledgeCrestLargeId.put(pledgeId, crestId);
				get_pledgeCrestLarge().put(crestId, crest);
			}
			DbUtils.close(statement, rset);
			statement = con.prepareStatement("SELECT ally_id, crest FROM ally_data WHERE crest IS NOT NULL");
			rset = statement.executeQuery();
			while (rset.next())
			{
				count++;
				pledgeId = rset.getInt("ally_id");
				crest = rset.getBytes("crest");
				crestId = getCrestId(pledgeId, crest);
				_allyCrestId.put(pledgeId, crestId);
				_allyCrest.put(crestId, crest);
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
		_log.info("CrestCache: Loaded " + count + " crests");
	}
	
	/**
	 * Method getCrestId.
	 * @param pledgeId int
	 * @param crest byte[]
	 * @return int
	 */
	private static int getCrestId(int pledgeId, byte[] crest)
	{
		return Math.abs(new HashCodeBuilder(15, 87).append(pledgeId).append(crest).toHashCode());
	}
	
	/**
	 * Method getPledgeCrest.
	 * @param crestId int
	 * @return byte[]
	 */
	public byte[] getPledgeCrest(int crestId)
	{
		byte[] crest = null;
		readLock.lock();
		try
		{
			crest = _pledgeCrest.get(crestId);
		}
		finally
		{
			readLock.unlock();
		}
		return crest;
	}
	
	/**
	 * Method getPledgeCrestLarge.
	 * @param crestId int
	 * @return byte[]
	 */
	public byte[] getPledgeCrestLarge(int crestId)
	{
		byte[] crest = null;
		readLock.lock();
		try
		{
			crest = get_pledgeCrestLarge().get(crestId);
		}
		finally
		{
			readLock.unlock();
		}
		return crest;
	}
	
	/**
	 * Method getAllyCrest.
	 * @param crestId int
	 * @return byte[]
	 */
	public byte[] getAllyCrest(int crestId)
	{
		byte[] crest = null;
		readLock.lock();
		try
		{
			crest = _allyCrest.get(crestId);
		}
		finally
		{
			readLock.unlock();
		}
		return crest;
	}
	
	/**
	 * Method getPledgeCrestId.
	 * @param pledgeId int
	 * @return int
	 */
	public int getPledgeCrestId(int pledgeId)
	{
		int crestId = 0;
		readLock.lock();
		try
		{
			crestId = _pledgeCrestId.get(pledgeId);
		}
		finally
		{
			readLock.unlock();
		}
		return crestId;
	}
	
	/**
	 * Method getPledgeCrestLargeId.
	 * @param pledgeId int
	 * @return int
	 */
	public int getPledgeCrestLargeId(int pledgeId)
	{
		int crestId = 0;
		readLock.lock();
		try
		{
			crestId = _pledgeCrestLargeId.get(pledgeId);
		}
		finally
		{
			readLock.unlock();
		}
		return crestId;
	}
	
	/**
	 * Method getAllyCrestId.
	 * @param pledgeId int
	 * @return int
	 */
	public int getAllyCrestId(int pledgeId)
	{
		int crestId = 0;
		readLock.lock();
		try
		{
			crestId = _allyCrestId.get(pledgeId);
		}
		finally
		{
			readLock.unlock();
		}
		return crestId;
	}
	
	/**
	 * Method removePledgeCrest.
	 * @param pledgeId int
	 */
	public void removePledgeCrest(int pledgeId)
	{
		writeLock.lock();
		try
		{
			_pledgeCrest.remove(_pledgeCrestId.remove(pledgeId));
		}
		finally
		{
			writeLock.unlock();
		}
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("UPDATE clan_data SET crest=? WHERE clan_id=?");
			statement.setNull(1, Types.VARBINARY);
			statement.setInt(2, pledgeId);
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
	 * Method removePledgeCrestLarge.
	 * @param pledgeId int
	 */
	public void removePledgeCrestLarge(int pledgeId)
	{
		writeLock.lock();
		try
		{
			get_pledgeCrestLarge().remove(_pledgeCrestLargeId.remove(pledgeId));
		}
		finally
		{
			writeLock.unlock();
		}
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("UPDATE clan_data SET largecrest=? WHERE clan_id=?");
			statement.setNull(1, Types.VARBINARY);
			statement.setInt(2, pledgeId);
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
	 * Method removeAllyCrest.
	 * @param pledgeId int
	 */
	public void removeAllyCrest(int pledgeId)
	{
		writeLock.lock();
		try
		{
			_allyCrest.remove(_allyCrestId.remove(pledgeId));
		}
		finally
		{
			writeLock.unlock();
		}
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("UPDATE ally_data SET crest=? WHERE ally_id=?");
			statement.setNull(1, Types.VARBINARY);
			statement.setInt(2, pledgeId);
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
	 * Method savePledgeCrest.
	 * @param pledgeId int
	 * @param crest byte[]
	 * @return int
	 */
	public int savePledgeCrest(int pledgeId, byte[] crest)
	{
		int crestId = getCrestId(pledgeId, crest);
		writeLock.lock();
		try
		{
			_pledgeCrestId.put(pledgeId, crestId);
			_pledgeCrest.put(crestId, crest);
		}
		finally
		{
			writeLock.unlock();
		}
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("UPDATE clan_data SET crest=? WHERE clan_id=?");
			statement.setBytes(1, crest);
			statement.setInt(2, pledgeId);
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
		return crestId;
	}
	
	/**
	 * Method savePledgeCrestLarge.
	 * @param pledgeId int
	 * @param crest byte[]
	 * @return int
	 */
	public int savePledgeCrestLarge(int pledgeId, byte[] crest)
	{
		int crestId = getCrestId(pledgeId, crest);
		writeLock.lock();
		try
		{
			_pledgeCrestLargeId.put(pledgeId, crestId);
			get_pledgeCrestLarge().put(crestId, crest);
		}
		finally
		{
			writeLock.unlock();
		}
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("UPDATE clan_data SET largecrest=? WHERE clan_id=?");
			statement.setBytes(1, crest);
			statement.setInt(2, pledgeId);
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
		return crestId;
	}
	
	/**
	 * Method saveAllyCrest.
	 * @param pledgeId int
	 * @param crest byte[]
	 * @return int
	 */
	public int saveAllyCrest(int pledgeId, byte[] crest)
	{
		int crestId = getCrestId(pledgeId, crest);
		writeLock.lock();
		try
		{
			_allyCrestId.put(pledgeId, crestId);
			_allyCrest.put(crestId, crest);
		}
		finally
		{
			writeLock.unlock();
		}
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("UPDATE ally_data SET crest=? WHERE ally_id=?");
			statement.setBytes(1, crest);
			statement.setInt(2, pledgeId);
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
		return crestId;
	}

	public TIntObjectHashMap<byte[]> get_pledgeCrestLarge()
	{
		return _pledgeCrestLarge;
	}
}
