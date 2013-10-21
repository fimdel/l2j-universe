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
package lineage2.commons.dbcp;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.GenericKeyedObjectPool;
import org.apache.commons.pool.impl.GenericKeyedObjectPoolFactory;
import org.apache.commons.pool.impl.GenericObjectPool;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class BasicDataSource implements DataSource
{
	/**
	 * Field _source.
	 */
	private final PoolingDataSource _source;
	/**
	 * Field _connectionPool.
	 */
	private final ObjectPool<?> _connectionPool;
	
	/**
	 * Constructor for BasicDataSource.
	 * @param driver String
	 * @param connectURI String
	 * @param uname String
	 * @param passwd String
	 * @param maxActive int
	 * @param maxIdle int
	 * @param idleTimeOut int
	 * @param idleTestPeriod int
	 * @param poolPreparedStatements boolean
	 */
	public BasicDataSource(String driver, String connectURI, String uname, String passwd, int maxActive, int maxIdle, int idleTimeOut, int idleTestPeriod, boolean poolPreparedStatements)
	{
		GenericObjectPool<?> connectionPool = new GenericObjectPool<>(null);
		connectionPool.setMaxActive(maxActive);
		connectionPool.setMaxIdle(maxIdle);
		connectionPool.setMinIdle(1);
		connectionPool.setMaxWait(-1L);
		connectionPool.setWhenExhaustedAction(GenericObjectPool.WHEN_EXHAUSTED_GROW);
		connectionPool.setTestOnBorrow(false);
		connectionPool.setTestWhileIdle(true);
		connectionPool.setTimeBetweenEvictionRunsMillis(idleTestPeriod * 1000L);
		connectionPool.setNumTestsPerEvictionRun(maxActive);
		connectionPool.setMinEvictableIdleTimeMillis(idleTimeOut * 1000L);
		GenericKeyedObjectPoolFactory<?, ?> statementPoolFactory = null;
		if (poolPreparedStatements)
		{
			statementPoolFactory = new GenericKeyedObjectPoolFactory<>(null, -1, GenericObjectPool.WHEN_EXHAUSTED_FAIL, 0L, 1, GenericKeyedObjectPool.DEFAULT_MAX_TOTAL);
		}
		Properties connectionProperties = new Properties();
		connectionProperties.put("user", uname);
		connectionProperties.put("password", passwd);
		ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(connectURI, connectionProperties);
		@SuppressWarnings("unused")
		PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(connectionFactory, connectionPool, statementPoolFactory, "SELECT 1", false, true);
		PoolingDataSource dataSource = new PoolingDataSource(connectionPool);
		_connectionPool = connectionPool;
		_source = dataSource;
	}
	
	/**
	 * Method getConnection.
	 * @param con Connection
	 * @return Connection * @throws SQLException
	 */
	public Connection getConnection(Connection con) throws SQLException
	{
		return (con == null) || con.isClosed() ? _source.getConnection() : con;
	}
	
	/**
	 * Method getBusyConnectionCount.
	 * @return int
	 */
	public int getBusyConnectionCount()
	{
		return _connectionPool.getNumActive();
	}
	
	/**
	 * Method getIdleConnectionCount.
	 * @return int
	 */
	public int getIdleConnectionCount()
	{
		return _connectionPool.getNumIdle();
	}
	
	/**
	 * Method shutdown.
	 * @throws Exception
	 */
	public void shutdown() throws Exception
	{
		_connectionPool.close();
	}
	
	/**
	 * Method getLogWriter.
	 * @return PrintWriter * @see javax.sql.CommonDataSource#getLogWriter()
	 */
	@Override
	public PrintWriter getLogWriter()
	{
		return _source.getLogWriter();
	}
	
	/**
	 * Method setLogWriter.
	 * @param out PrintWriter
	 * @see javax.sql.CommonDataSource#setLogWriter(PrintWriter)
	 */
	@Override
	public void setLogWriter(PrintWriter out)
	{
		_source.setLogWriter(out);
	}
	
	/**
	 * Method setLoginTimeout.
	 * @param seconds int
	 * @see javax.sql.CommonDataSource#setLoginTimeout(int)
	 */
	@Override
	public void setLoginTimeout(int seconds)
	{
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Method getLoginTimeout.
	 * @return int * @see javax.sql.CommonDataSource#getLoginTimeout()
	 */
	@Override
	public int getLoginTimeout()
	{
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Method getParentLogger.
	 * @return Logger * @see javax.sql.CommonDataSource#getParentLogger()
	 */
	@Override
	public Logger getParentLogger()
	{
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Method unwrap.
	 * @param iface Class<T>
	 * @return T * @see java.sql.Wrapper#unwrap(Class<T>)
	 */
	@Override
	public <T> T unwrap(Class<T> iface)
	{
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Method isWrapperFor.
	 * @param iface Class<?>
	 * @return boolean * @see java.sql.Wrapper#isWrapperFor(Class<?>)
	 */
	@Override
	public boolean isWrapperFor(Class<?> iface)
	{
		return false;
	}
	
	/**
	 * Method getConnection.
	 * @return Connection * @throws SQLException * @see javax.sql.DataSource#getConnection()
	 */
	@Override
	public Connection getConnection() throws SQLException
	{
		return _source.getConnection();
	}
	
	/**
	 * Method getConnection.
	 * @param username String
	 * @param password String
	 * @return Connection * @see javax.sql.DataSource#getConnection(String, String)
	 */
	@Override
	public Connection getConnection(String username, String password)
	{
		throw new UnsupportedOperationException();
	}
}
