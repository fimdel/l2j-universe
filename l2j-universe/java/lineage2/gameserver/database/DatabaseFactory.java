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
package lineage2.gameserver.database;

import java.sql.Connection;
import java.sql.SQLException;

import lineage2.commons.dbcp.BasicDataSource;
import lineage2.gameserver.Config;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class DatabaseFactory extends BasicDataSource
{
	/**
	 * Field _instance.
	 */
	private static DatabaseFactory _instance = new DatabaseFactory();
	
	/**
	 * Method getInstance.
	 * @return DatabaseFactory
	 */
	public static DatabaseFactory getInstance()
	{
		return _instance;
	}
	
	/**
	 * Constructor for DatabaseFactory.
	 */
	public DatabaseFactory()
	{
		super(Config.DATABASE_DRIVER, Config.DATABASE_URL, Config.DATABASE_LOGIN, Config.DATABASE_PASSWORD, Config.DATABASE_MAX_CONNECTIONS, Config.DATABASE_MAX_CONNECTIONS, Config.DATABASE_MAX_IDLE_TIMEOUT, Config.DATABASE_IDLE_TEST_PERIOD, false);
	}
	
	/**
	 * Constructor for DatabaseFactory.
	 * @param driver String
	 * @param url String
	 * @param login String
	 * @param pass String
	 * @param maxconn int
	 * @param maxIdle int
	 * @param idleTime int
	 * @param idleTest int
	 * @param prepared boolean
	 */
	public DatabaseFactory(String driver, String url, String login, String pass, int maxconn, int maxIdle, int idleTime, int idleTest, boolean prepared)
	{
		super(driver, url, login, pass, maxconn, maxIdle, idleTime, idleTest, prepared);
	}
	
	/**
	 * Method getConnection.
	 * @return Connection * @throws SQLException * @see javax.sql.DataSource#getConnection()
	 */
	@Override
	public Connection getConnection() throws SQLException
	{
		return getConnection(null);
	}
}
