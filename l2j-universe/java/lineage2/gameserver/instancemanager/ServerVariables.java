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

import lineage2.commons.dbutils.DbUtils;
import lineage2.gameserver.database.DatabaseFactory;
import lineage2.gameserver.templates.StatsSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ServerVariables
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(ServerVariables.class);
	/**
	 * Field server_vars.
	 */
	private static StatsSet server_vars = null;
	
	/**
	 * Method getVars.
	 * @return StatsSet
	 */
	private static StatsSet getVars()
	{
		if (server_vars == null)
		{
			server_vars = new StatsSet();
			LoadFromDB();
		}
		return server_vars;
	}
	
	/**
	 * Method LoadFromDB.
	 */
	private static void LoadFromDB()
	{
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT * FROM server_variables");
			rs = statement.executeQuery();
			while (rs.next())
			{
				server_vars.set(rs.getString("name"), rs.getString("value"));
			}
		}
		catch (Exception e)
		{
			_log.error("", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rs);
		}
	}
	
	/**
	 * Method SaveToDB.
	 * @param name String
	 */
	private static void SaveToDB(String name)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			String value = getVars().getString(name, "");
			if (value.isEmpty())
			{
				statement = con.prepareStatement("DELETE FROM server_variables WHERE name = ?");
				statement.setString(1, name);
				statement.execute();
			}
			else
			{
				statement = con.prepareStatement("REPLACE INTO server_variables (name, value) VALUES (?,?)");
				statement.setString(1, name);
				statement.setString(2, value);
				statement.execute();
			}
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
	 * Method getBool.
	 * @param name String
	 * @return boolean
	 */
	public static boolean getBool(String name)
	{
		return getVars().getBool(name);
	}
	
	/**
	 * Method getBool.
	 * @param name String
	 * @param defult boolean
	 * @return boolean
	 */
	public static boolean getBool(String name, boolean defult)
	{
		return getVars().getBool(name, defult);
	}
	
	/**
	 * Method getInt.
	 * @param name String
	 * @return int
	 */
	public static int getInt(String name)
	{
		return getVars().getInteger(name);
	}
	
	/**
	 * Method getInt.
	 * @param name String
	 * @param defult int
	 * @return int
	 */
	public static int getInt(String name, int defult)
	{
		return getVars().getInteger(name, defult);
	}
	
	/**
	 * Method getLong.
	 * @param name String
	 * @return long
	 */
	public static long getLong(String name)
	{
		return getVars().getLong(name);
	}
	
	/**
	 * Method getLong.
	 * @param name String
	 * @param defult long
	 * @return long
	 */
	public static long getLong(String name, long defult)
	{
		return getVars().getLong(name, defult);
	}
	
	/**
	 * Method getFloat.
	 * @param name String
	 * @return double
	 */
	public static double getFloat(String name)
	{
		return getVars().getDouble(name);
	}
	
	/**
	 * Method getFloat.
	 * @param name String
	 * @param defult double
	 * @return double
	 */
	public static double getFloat(String name, double defult)
	{
		return getVars().getDouble(name, defult);
	}
	
	/**
	 * Method getString.
	 * @param name String
	 * @return String
	 */
	public static String getString(String name)
	{
		return getVars().getString(name);
	}
	
	/**
	 * Method getString.
	 * @param name String
	 * @param defult String
	 * @return String
	 */
	public static String getString(String name, String defult)
	{
		return getVars().getString(name, defult);
	}
	
	/**
	 * Method set.
	 * @param name String
	 * @param value boolean
	 */
	public static void set(String name, boolean value)
	{
		getVars().set(name, value);
		SaveToDB(name);
	}
	
	/**
	 * Method set.
	 * @param name String
	 * @param value int
	 */
	public static void set(String name, int value)
	{
		getVars().set(name, value);
		SaveToDB(name);
	}
	
	/**
	 * Method set.
	 * @param name String
	 * @param value long
	 */
	public static void set(String name, long value)
	{
		getVars().set(name, value);
		SaveToDB(name);
	}
	
	/**
	 * Method set.
	 * @param name String
	 * @param value double
	 */
	public static void set(String name, double value)
	{
		getVars().set(name, value);
		SaveToDB(name);
	}
	
	/**
	 * Method set.
	 * @param name String
	 * @param value String
	 */
	public static void set(String name, String value)
	{
		getVars().set(name, value);
		SaveToDB(name);
	}
	
	/**
	 * Method unset.
	 * @param name String
	 */
	public static void unset(String name)
	{
		getVars().unset(name);
		SaveToDB(name);
	}
}
