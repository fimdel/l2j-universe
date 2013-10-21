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

import java.io.File;
import java.net.InetAddress;
import java.net.ServerSocket;

import lineage2.commons.net.nio.impl.SelectorConfig;
import lineage2.commons.net.nio.impl.SelectorThread;
import lineage2.loginserver.database.L2DatabaseFactory;
import lineage2.loginserver.gameservercon.GameServerCommunication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class LoginServer
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(LoginServer.class);
	/**
	 * Field authServer.
	 */
	private static LoginServer authServer;
	/**
	 * Field _gameServerListener.
	 */
	private final GameServerCommunication _gameServerListener;
	
	/**
	 * Method getInstance.
	 * @return LoginServer
	 */
	public static LoginServer getInstance()
	{
		return authServer;
	}
	
	/**
	 * Constructor for LoginServer.
	 * @throws Throwable
	 */
	public LoginServer() throws Throwable
	{
		Config.initCrypt();
		GameServerManager.getInstance();
		L2LoginPacketHandler loginPacketHandler = new L2LoginPacketHandler();
		SelectorHelper sh = new SelectorHelper();
		SelectorConfig sc = new SelectorConfig();
		SelectorThread<L2LoginClient> _selectorThread = new SelectorThread<>(sc, loginPacketHandler, sh, sh, sh);
		_gameServerListener = GameServerCommunication.getInstance();
		_gameServerListener.openServerSocket(Config.GAME_SERVER_LOGIN_HOST.equals("*") ? null : InetAddress.getByName(Config.GAME_SERVER_LOGIN_HOST), Config.GAME_SERVER_LOGIN_PORT);
		_gameServerListener.start();
		_log.info("Listening for gameservers on " + Config.GAME_SERVER_LOGIN_HOST + ":" + Config.GAME_SERVER_LOGIN_PORT);
		_selectorThread.openServerSocket(Config.LOGIN_HOST.equals("*") ? null : InetAddress.getByName(Config.LOGIN_HOST), Config.PORT_LOGIN);
		_selectorThread.start();
		_log.info("Listening for clients on " + Config.LOGIN_HOST + ":" + Config.PORT_LOGIN);
	}
	
	/**
	 * Method getGameServerListener.
	 * @return GameServerCommunication
	 */
	public GameServerCommunication getGameServerListener()
	{
		return _gameServerListener;
	}
	
	/**
	 * Method checkFreePorts.
	 * @throws Throwable
	 */
	public static void checkFreePorts() throws Throwable
	{
		ServerSocket ss = null;
		try
		{
			if (Config.LOGIN_HOST.equalsIgnoreCase("*"))
			{
				ss = new ServerSocket(Config.PORT_LOGIN);
			}
			else
			{
				ss = new ServerSocket(Config.PORT_LOGIN, 50, InetAddress.getByName(Config.LOGIN_HOST));
			}
		}
		finally
		{
			if (ss != null)
			{
				try
				{
					ss.close();
				}
				catch (Exception ignored)
				{
				}
			}
		}
	}
	
	/**
	 * Method main.
	 * @param args String[]
	 * @throws Throwable
	 */
	public static void main(String[] args) throws Throwable
	{
		new File("./log/").mkdir();
		Config.load();
		checkFreePorts();
		Class.forName(Config.DATABASE_DRIVER).newInstance();
		L2DatabaseFactory.getInstance().getConnection().close();
		authServer = new LoginServer();
	}
}
