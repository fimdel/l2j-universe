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
package lineage2.loginserver.gameservercon;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import lineage2.loginserver.Config;

import org.apache.log4j.Logger;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class GameServer
{
	/**
	 * Field _log.
	 */
	@SuppressWarnings("unused")
	private static final Logger _log = Logger.getLogger(GameServer.class);
	/**
	 * Field _id.
	 */
	private int _id;
	/**
	 * Field _externalHost. Field _internalHost.
	 */
	private String _internalHost, _externalHost;
	/**
	 * Field _ports.
	 */
	private int[] _ports = new int[]
	{
		7777
	};
	/**
	 * Field _serverType.
	 */
	private int _serverType;
	/**
	 * Field _ageLimit.
	 */
	private int _ageLimit;
	/**
	 * Field _protocol.
	 */
	private int _protocol;
	/**
	 * Field _isOnline.
	 */
	private boolean _isOnline;
	/**
	 * Field _isPvp.
	 */
	private boolean _isPvp;
	/**
	 * Field _isShowingBrackets.
	 */
	private boolean _isShowingBrackets;
	/**
	 * Field _isGmOnly.
	 */
	private boolean _isGmOnly;
	/**
	 * Field _maxPlayers.
	 */
	private int _maxPlayers;
	/**
	 * Field _conn.
	 */
	private GameServerConnection _conn;
	/**
	 * Field _isAuthed.
	 */
	private boolean _isAuthed;
	/**
	 * Field _port.
	 */
	private int _port;
	/**
	 * Field _accounts.
	 */
	private final Set<String> _accounts = new CopyOnWriteArraySet<>();
	
	/**
	 * Constructor for GameServer.
	 * @param conn GameServerConnection
	 */
	public GameServer(GameServerConnection conn)
	{
		_conn = conn;
	}
	
	/**
	 * Constructor for GameServer.
	 * @param id int
	 */
	public GameServer(int id)
	{
		_id = id;
	}
	
	/**
	 * Method setId.
	 * @param id int
	 */
	public void setId(int id)
	{
		_id = id;
	}
	
	/**
	 * Method getId.
	 * @return int
	 */
	public int getId()
	{
		return _id;
	}
	
	/**
	 * Method setAuthed.
	 * @param isAuthed boolean
	 */
	public void setAuthed(boolean isAuthed)
	{
		_isAuthed = isAuthed;
	}
	
	/**
	 * Method isAuthed.
	 * @return boolean
	 */
	public boolean isAuthed()
	{
		return _isAuthed;
	}
	
	/**
	 * Method setConnection.
	 * @param conn GameServerConnection
	 */
	public void setConnection(GameServerConnection conn)
	{
		_conn = conn;
	}
	
	/**
	 * Method getConnection.
	 * @return GameServerConnection
	 */
	public GameServerConnection getConnection()
	{
		return _conn;
	}
	
	/**
	 * Method getInternalHost.
	 * @return InetAddress * @throws UnknownHostException
	 */
	public InetAddress getInternalHost() throws UnknownHostException
	{
		return InetAddress.getByName(_internalHost);
	}
	
	/**
	 * Method setInternalHost.
	 * @param internalHost String
	 */
	public void setInternalHost(String internalHost)
	{
		if (internalHost.equals("*"))
		{
			internalHost = getConnection().getIpAddress();
		}
		_internalHost = internalHost;
	}
	
	/**
	 * Method setExternalHost.
	 * @param externalHost String
	 */
	public void setExternalHost(String externalHost)
	{
		if (externalHost.equals("*"))
		{
			externalHost = getConnection().getIpAddress();
		}
		_externalHost = externalHost;
	}
	
	/**
	 * Method getExternalHost.
	 * @return InetAddress * @throws UnknownHostException
	 */
	public InetAddress getExternalHost() throws UnknownHostException
	{
		return InetAddress.getByName(_externalHost);
	}
	
	/**
	 * Method getPort.
	 * @return int
	 */
	public int getPort()
	{
		return _ports[_port++ & (_ports.length - 1)];
	}
	
	/**
	 * Method setPorts.
	 * @param ports int[]
	 */
	public void setPorts(int[] ports)
	{
		_ports = ports;
	}
	
	/**
	 * Method setMaxPlayers.
	 * @param maxPlayers int
	 */
	public void setMaxPlayers(int maxPlayers)
	{
		_maxPlayers = maxPlayers;
	}
	
	/**
	 * Method getMaxPlayers.
	 * @return int
	 */
	public int getMaxPlayers()
	{
		return _maxPlayers;
	}
	
	/**
	 * Method getOnline.
	 * @return int
	 */
	public int getOnline()
	{
		return _accounts.size();
	}
	
	/**
	 * Method getAccounts.
	 * @return Set<String>
	 */
	public Set<String> getAccounts()
	{
		return _accounts;
	}
	
	/**
	 * Method addAccount.
	 * @param account String
	 */
	public void addAccount(String account)
	{
		_accounts.add(account);
	}
	
	/**
	 * Method removeAccount.
	 * @param account String
	 */
	public void removeAccount(String account)
	{
		_accounts.remove(account);
	}
	
	/**
	 * Method setDown.
	 */
	public void setDown()
	{
		setAuthed(false);
		setConnection(null);
		setOnline(false);
		_accounts.clear();
	}
	
	/**
	 * Method getName.
	 * @return String
	 */
	public String getName()
	{
		return Config.SERVER_NAMES.get(getId());
	}
	
	/**
	 * Method sendPacket.
	 * @param packet SendablePacket
	 */
	public void sendPacket(SendablePacket packet)
	{
		GameServerConnection conn = getConnection();
		if (conn != null)
		{
			conn.sendPacket(packet);
		}
	}
	
	/**
	 * Method getServerType.
	 * @return int
	 */
	public int getServerType()
	{
		return _serverType;
	}
	
	/**
	 * Method isOnline.
	 * @return boolean
	 */
	public boolean isOnline()
	{
		return _isOnline;
	}
	
	/**
	 * Method setOnline.
	 * @param online boolean
	 */
	public void setOnline(boolean online)
	{
		_isOnline = online;
	}
	
	/**
	 * Method setServerType.
	 * @param serverType int
	 */
	public void setServerType(int serverType)
	{
		_serverType = serverType;
	}
	
	/**
	 * Method isPvp.
	 * @return boolean
	 */
	public boolean isPvp()
	{
		return _isPvp;
	}
	
	/**
	 * Method setPvp.
	 * @param pvp boolean
	 */
	public void setPvp(boolean pvp)
	{
		_isPvp = pvp;
	}
	
	/**
	 * Method isShowingBrackets.
	 * @return boolean
	 */
	public boolean isShowingBrackets()
	{
		return _isShowingBrackets;
	}
	
	/**
	 * Method setShowingBrackets.
	 * @param showingBrackets boolean
	 */
	public void setShowingBrackets(boolean showingBrackets)
	{
		_isShowingBrackets = showingBrackets;
	}
	
	/**
	 * Method isGmOnly.
	 * @return boolean
	 */
	public boolean isGmOnly()
	{
		return _isGmOnly;
	}
	
	/**
	 * Method setGmOnly.
	 * @param gmOnly boolean
	 */
	public void setGmOnly(boolean gmOnly)
	{
		_isGmOnly = gmOnly;
	}
	
	/**
	 * Method getAgeLimit.
	 * @return int
	 */
	public int getAgeLimit()
	{
		return _ageLimit;
	}
	
	/**
	 * Method setAgeLimit.
	 * @param ageLimit int
	 */
	public void setAgeLimit(int ageLimit)
	{
		_ageLimit = ageLimit;
	}
	
	/**
	 * Method getProtocol.
	 * @return int
	 */
	public int getProtocol()
	{
		return _protocol;
	}
	
	/**
	 * Method setProtocol.
	 * @param protocol int
	 */
	public void setProtocol(int protocol)
	{
		_protocol = protocol;
	}
}
