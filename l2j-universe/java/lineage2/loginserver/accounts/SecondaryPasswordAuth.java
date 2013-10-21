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
package lineage2.loginserver.accounts;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import lineage2.commons.dbutils.DbUtils;
import lineage2.loginserver.database.L2DatabaseFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class SecondaryPasswordAuth
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = Logger.getLogger(SecondaryPasswordAuth.class.getName());
	/**
	 * Field SELECT_PARAMS. (value is ""SELECT account_password, wrongAttempts, banTime FROM account_2ndAuth WHERE account_name=?"")
	 */
	private static final String SELECT_PARAMS = "SELECT account_password, wrongAttempts, banTime FROM account_2ndAuth WHERE account_name=?";
	/**
	 * Field INSERT_PASSWORD. (value is ""INSERT INTO account_2ndAuth VALUES (?, ?, 0, 0) ON DUPLICATE KEY UPDATE account_password=?"")
	 */
	private static final String INSERT_PASSWORD = "INSERT INTO account_2ndAuth VALUES (?, ?, 0, 0) ON DUPLICATE KEY UPDATE account_password=?";
	/**
	 * Field UPDATE_WA. (value is ""UPDATE account_2ndAuth SET wrongAttempts=? WHERE account_name=?"")
	 */
	private static final String UPDATE_WA = "UPDATE account_2ndAuth SET wrongAttempts=? WHERE account_name=?";
	/**
	 * Field UPDATE_BT. (value is ""UPDATE account_2ndAuth SET banTime=? WHERE account_name=?"")
	 */
	private static final String UPDATE_BT = "UPDATE account_2ndAuth SET banTime=? WHERE account_name=?";
	
	/**
	 * Method getBanTime.
	 * @param login String
	 * @return long
	 */
	public static long getBanTime(String login)
	{
		long _unBanTime = 0;
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = L2DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(SELECT_PARAMS);
			statement.setString(1, login);
			for (ResultSet rs = statement.executeQuery(); rs.next();)
			{
				_unBanTime = rs.getLong("banTime");
			}
		}
		catch (Exception e)
		{
			_log.log(Level.SEVERE, "Error while reading bantime from base.", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
		return _unBanTime;
	}
	
	/**
	 * Method setBanTime.
	 * @param login String
	 * @param banTime int
	 */
	public static void setBanTime(String login, int banTime)
	{
		long _unBanTime = System.currentTimeMillis() + (banTime * 60000);
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = L2DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(UPDATE_BT);
			statement.setLong(1, _unBanTime);
			statement.setString(2, login);
			statement.execute();
		}
		catch (Exception e)
		{
			_log.log(Level.SEVERE, "Error while writing unban time.", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
	
	/**
	 * Method getLoginAttempts.
	 * @param login String
	 * @return int
	 */
	public static int getLoginAttempts(String login)
	{
		int _loginAttempts = 0;
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = L2DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(SELECT_PARAMS);
			statement.setString(1, login);
			for (ResultSet rs = statement.executeQuery(); rs.next();)
			{
				_loginAttempts = rs.getInt("wrongAttempts");
			}
		}
		catch (Exception e)
		{
			_log.log(Level.SEVERE, "Error while reading login attempts from base.", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
		return _loginAttempts;
	}
	
	/**
	 * Method setLoginAttempts.
	 * @param login String
	 * @param enterAttempts int
	 */
	public static void setLoginAttempts(String login, int enterAttempts)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = L2DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(UPDATE_WA);
			statement.setInt(1, enterAttempts);
			statement.setString(2, login);
			statement.execute();
		}
		catch (Exception e)
		{
			_log.log(Level.SEVERE, "Error while writing wrong attempts.", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
	
	/**
	 * Method getPassword.
	 * @param login String
	 * @return String
	 */
	public static String getPassword(String login)
	{
		String _password = null;
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = L2DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(SELECT_PARAMS);
			statement.setString(1, login);
			for (ResultSet rs = statement.executeQuery(); rs.next();)
			{
				_password = rs.getString("account_password");
			}
		}
		catch (Exception e)
		{
			_log.log(Level.SEVERE, "Error while reading password from base.", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
		return _password;
	}
	
	/**
	 * Method setPassword.
	 * @param login String
	 * @param password String
	 */
	public static void setPassword(String login, String password)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = L2DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(INSERT_PASSWORD);
			statement.setString(1, login);
			statement.setString(2, password);
			statement.setString(3, password);
			statement.execute();
		}
		catch (Exception e)
		{
			_log.log(Level.SEVERE, "Error while writing password.", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
}
