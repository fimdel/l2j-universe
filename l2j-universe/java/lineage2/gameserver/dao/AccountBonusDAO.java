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
package lineage2.gameserver.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import lineage2.commons.dbutils.DbUtils;
import lineage2.gameserver.database.DatabaseFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AccountBonusDAO
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(AccountBonusDAO.class);
	/**
	 * Field _instance.
	 */
	private static final AccountBonusDAO _instance = new AccountBonusDAO();
	/**
	 * Field SELECT_SQL_QUERY. (value is ""SELECT bonus, bonus_expire FROM account_bonus WHERE account=?"")
	 */
	public static final String SELECT_SQL_QUERY = "SELECT bonus, bonus_expire FROM account_bonus WHERE account=?";
	/**
	 * Field DELETE_SQL_QUERY. (value is ""DELETE FROM account_bonus WHERE account=?"")
	 */
	public static final String DELETE_SQL_QUERY = "DELETE FROM account_bonus WHERE account=?";
	/**
	 * Field INSERT_SQL_QUERY. (value is ""REPLACE INTO account_bonus(account, bonus, bonus_expire) VALUES (?,?,?)"")
	 */
	public static final String INSERT_SQL_QUERY = "REPLACE INTO account_bonus(account, bonus, bonus_expire) VALUES (?,?,?)";
	
	/**
	 * Method getInstance.
	 * @return AccountBonusDAO
	 */
	public static AccountBonusDAO getInstance()
	{
		return _instance;
	}
	
	/**
	 * Method select.
	 * @param account String
	 * @return double[]
	 */
	public double[] select(String account)
	{
		double bonus = 1.;
		int time = 0;
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(SELECT_SQL_QUERY);
			statement.setString(1, account);
			rset = statement.executeQuery();
			if (rset.next())
			{
				bonus = rset.getDouble("bonus");
				time = rset.getInt("bonus_expire");
			}
		}
		catch (Exception e)
		{
			_log.info("AccountBonusDAO.select(String): " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
		return new double[]
		{
			bonus,
			time
		};
	}
	
	/**
	 * Method delete.
	 * @param account String
	 */
	public void delete(String account)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(DELETE_SQL_QUERY);
			statement.setString(1, account);
			statement.execute();
		}
		catch (Exception e)
		{
			_log.info("AccountBonusDAO.delete(String): " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
	
	/**
	 * Method insert.
	 * @param account String
	 * @param bonus double
	 * @param endTime int
	 */
	public void insert(String account, double bonus, int endTime)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(INSERT_SQL_QUERY);
			statement.setString(1, account);
			statement.setDouble(2, bonus);
			statement.setInt(3, endTime);
			statement.execute();
		}
		catch (Exception e)
		{
			_log.info("AccountBonusDAO.insert(String, double, int): " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
}
