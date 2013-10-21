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
package lineage2.gameserver.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.atomic.AtomicLong;

import lineage2.commons.dbutils.DbUtils;
import lineage2.gameserver.database.DatabaseFactory;
import lineage2.gameserver.instancemanager.ServerVariables;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class GameStats
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(GameStats.class);
	/**
	 * Field _updatePlayerBase.
	 */
	private static AtomicLong _updatePlayerBase = new AtomicLong(0L);
	/**
	 * Field _playerEnterGameCounter.
	 */
	private static AtomicLong _playerEnterGameCounter = new AtomicLong(0L);
	/**
	 * Field _taxSum.
	 */
	private static AtomicLong _taxSum = new AtomicLong(0L);
	/**
	 * Field _taxLastUpdate.
	 */
	private static long _taxLastUpdate;
	/**
	 * Field _rouletteSum.
	 */
	private static AtomicLong _rouletteSum = new AtomicLong(0L);
	/**
	 * Field _rouletteLastUpdate.
	 */
	private static long _rouletteLastUpdate;
	/**
	 * Field _adenaSum.
	 */
	private static AtomicLong _adenaSum = new AtomicLong(0L);
	static
	{
		_taxSum.set(ServerVariables.getLong("taxsum", 0));
		_rouletteSum.set(ServerVariables.getLong("rouletteSum", 0));
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT (SELECT SUM(count) FROM items WHERE item_id=57) + (SELECT SUM(treasury) FROM castle) AS `count`");
			rset = statement.executeQuery();
			if (rset.next())
			{
				_adenaSum.addAndGet(rset.getLong("count"));
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
	 * Method increaseUpdatePlayerBase.
	 */
	public static void increaseUpdatePlayerBase()
	{
		_updatePlayerBase.incrementAndGet();
	}
	
	/**
	 * Method getUpdatePlayerBase.
	 * @return long
	 */
	public static long getUpdatePlayerBase()
	{
		return _updatePlayerBase.get();
	}
	
	/**
	 * Method incrementPlayerEnterGame.
	 */
	public static void incrementPlayerEnterGame()
	{
		_playerEnterGameCounter.incrementAndGet();
	}
	
	/**
	 * Method getPlayerEnterGame.
	 * @return long
	 */
	public static long getPlayerEnterGame()
	{
		return _playerEnterGameCounter.get();
	}
	
	/**
	 * Method addTax.
	 * @param sum long
	 */
	public static void addTax(long sum)
	{
		long taxSum = _taxSum.addAndGet(sum);
		if ((System.currentTimeMillis() - _taxLastUpdate) < 10000)
		{
			return;
		}
		_taxLastUpdate = System.currentTimeMillis();
		ServerVariables.set("taxsum", taxSum);
	}
	
	/**
	 * Method addRoulette.
	 * @param sum long
	 */
	public static void addRoulette(long sum)
	{
		long rouletteSum = _rouletteSum.addAndGet(sum);
		if ((System.currentTimeMillis() - _rouletteLastUpdate) < 10000)
		{
			return;
		}
		_rouletteLastUpdate = System.currentTimeMillis();
		ServerVariables.set("rouletteSum", rouletteSum);
	}
	
	/**
	 * Method getTaxSum.
	 * @return long
	 */
	public static long getTaxSum()
	{
		return _taxSum.get();
	}
	
	/**
	 * Method getRouletteSum.
	 * @return long
	 */
	public static long getRouletteSum()
	{
		return _rouletteSum.get();
	}
	
	/**
	 * Method addAdena.
	 * @param sum long
	 */
	public static void addAdena(long sum)
	{
		_adenaSum.addAndGet(sum);
	}
	
	/**
	 * Method getAdena.
	 * @return long
	 */
	public static long getAdena()
	{
		return _adenaSum.get();
	}
}
