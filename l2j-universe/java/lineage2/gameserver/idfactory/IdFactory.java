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
package lineage2.gameserver.idfactory;

import gnu.trove.list.array.TIntArrayList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import lineage2.commons.dbutils.DbUtils;
import lineage2.gameserver.database.DatabaseFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public abstract class IdFactory
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(IdFactory.class);
	/**
	 * Field EXTRACT_OBJ_ID_TABLES.
	 */
	public static final String[][] EXTRACT_OBJ_ID_TABLES =
	{
		{
			"characters",
			"obj_id"
		},
		{
			"items",
			"object_id"
		},
		{
			"clan_data",
			"clan_id"
		},
		{
			"ally_data",
			"ally_id"
		},
		{
			"pets",
			"objId"
		},
		{
			"servitors",
			"objId"
		},
		{
			"couples",
			"id"
		}
	};
	/**
	 * Field FIRST_OID.
	 */
	public static final int FIRST_OID = 0x10000000;
	/**
	 * Field LAST_OID.
	 */
	public static final int LAST_OID = 0x7FFFFFFF;
	/**
	 * Field FREE_OBJECT_ID_SIZE.
	 */
	public static final int FREE_OBJECT_ID_SIZE = LAST_OID - FIRST_OID;
	/**
	 * Field _instance.
	 */
	protected static final IdFactory _instance = new BitSetIDFactory();
	
	/**
	 * Method getInstance.
	 * @return IdFactory
	 */
	public static final IdFactory getInstance()
	{
		return _instance;
	}
	
	/**
	 * Field initialized.
	 */
	protected boolean initialized;
	/**
	 * Field releasedCount.
	 */
	protected long releasedCount = 0;
	
	/**
	 * Constructor for IdFactory.
	 */
	protected IdFactory()
	{
		resetOnlineStatus();
	}
	
	/**
	 * Method resetOnlineStatus.
	 */
	private void resetOnlineStatus()
	{
		Connection con = null;
		Statement st = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			st = con.createStatement();
			st.executeUpdate("UPDATE characters SET online = 0");
			_log.info("IdFactory: Clear characters online status.");
		}
		catch (SQLException e)
		{
			_log.error("", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, st);
		}
	}
	
	/**
	 * Method extractUsedObjectIDTable.
	 * @return int[] * @throws SQLException
	 */
	protected int[] extractUsedObjectIDTable() throws SQLException
	{
		TIntArrayList objectIds = new TIntArrayList();
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			st = con.createStatement();
			for (String[] table : EXTRACT_OBJ_ID_TABLES)
			{
				rs = st.executeQuery("SELECT " + table[1] + " FROM " + table[0]);
				int size = objectIds.size();
				while (rs.next())
				{
					objectIds.add(rs.getInt(1));
				}
				DbUtils.close(rs);
				size = objectIds.size() - size;
				if (size > 0)
				{
					_log.info("IdFactory: Extracted " + size + " used id's from " + table[0]);
				}
			}
		}
		finally
		{
			DbUtils.closeQuietly(con, st, rs);
		}
		int[] extracted = objectIds.toArray();
		Arrays.sort(extracted);
		_log.info("IdFactory: Extracted total " + extracted.length + " used id's.");
		return extracted;
	}
	
	/**
	 * Method isInitialized.
	 * @return boolean
	 */
	public boolean isInitialized()
	{
		return initialized;
	}
	
	/**
	 * Method getNextId.
	 * @return int
	 */
	public abstract int getNextId();
	
	/**
	 * Method releaseId.
	 * @param id int
	 */
	public void releaseId(int id)
	{
		releasedCount++;
	}
	
	/**
	 * Method getReleasedCount.
	 * @return long
	 */
	public long getReleasedCount()
	{
		return releasedCount;
	}
	
	/**
	 * Method size.
	 * @return int
	 */
	public abstract int size();
}
