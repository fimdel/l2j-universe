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
package lineage2.commons.dbutils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class DbUtils
{
	/**
	 * Method close.
	 * @param conn Connection
	 * @throws SQLException
	 */
	public static void close(Connection conn) throws SQLException
	{
		if (conn != null)
		{
			conn.close();
		}
	}
	
	/**
	 * Method close.
	 * @param rs ResultSet
	 * @throws SQLException
	 */
	public static void close(ResultSet rs) throws SQLException
	{
		if (rs != null)
		{
			rs.close();
		}
	}
	
	/**
	 * Method close.
	 * @param stmt Statement
	 * @throws SQLException
	 */
	public static void close(Statement stmt) throws SQLException
	{
		if (stmt != null)
		{
			stmt.close();
		}
	}
	
	/**
	 * Method close.
	 * @param stmt Statement
	 * @param rs ResultSet
	 * @throws SQLException
	 */
	public static void close(Statement stmt, ResultSet rs) throws SQLException
	{
		close(stmt);
		close(rs);
	}
	
	/**
	 * Method closeQuietly.
	 * @param conn Connection
	 */
	public static void closeQuietly(Connection conn)
	{
		try
		{
			close(conn);
		}
		catch (SQLException e)
		{
		}
	}
	
	/**
	 * Method closeQuietly.
	 * @param conn Connection
	 * @param stmt Statement
	 */
	public static void closeQuietly(Connection conn, Statement stmt)
	{
		try
		{
			closeQuietly(stmt);
		}
		finally
		{
			closeQuietly(conn);
		}
	}
	
	/**
	 * Method closeQuietly.
	 * @param stmt Statement
	 * @param rs ResultSet
	 */
	public static void closeQuietly(Statement stmt, ResultSet rs)
	{
		try
		{
			closeQuietly(stmt);
		}
		finally
		{
			closeQuietly(rs);
		}
	}
	
	/**
	 * Method closeQuietly.
	 * @param conn Connection
	 * @param stmt Statement
	 * @param rs ResultSet
	 */
	public static void closeQuietly(Connection conn, Statement stmt, ResultSet rs)
	{
		try
		{
			closeQuietly(rs);
		}
		finally
		{
			try
			{
				closeQuietly(stmt);
			}
			finally
			{
				closeQuietly(conn);
			}
		}
	}
	
	/**
	 * Method closeQuietly.
	 * @param rs ResultSet
	 */
	public static void closeQuietly(ResultSet rs)
	{
		try
		{
			close(rs);
		}
		catch (SQLException e)
		{
		}
	}
	
	/**
	 * Method closeQuietly.
	 * @param stmt Statement
	 */
	public static void closeQuietly(Statement stmt)
	{
		try
		{
			close(stmt);
		}
		catch (SQLException e)
		{
		}
	}
}
