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
package lineage2.loginserver.gameservercon.gspackets;

import lineage2.loginserver.GameServerManager;
import lineage2.loginserver.gameservercon.GameServer;
import lineage2.loginserver.gameservercon.ReceivablePacket;
import lineage2.loginserver.gameservercon.lspackets.AuthResponse;
import lineage2.loginserver.gameservercon.lspackets.LoginServerFail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AuthRequest extends ReceivablePacket
{
	/**
	 * Field _log.
	 */
	private final static Logger _log = LoggerFactory.getLogger(AuthRequest.class);
	/**
	 * Field _protocolVersion.
	 */
	private int _protocolVersion;
	/**
	 * Field requestId.
	 */
	private int requestId;
	/**
	 * Field acceptAlternateID.
	 */
	private boolean acceptAlternateID;
	/**
	 * Field externalIp.
	 */
	private String externalIp;
	/**
	 * Field internalIp.
	 */
	private String internalIp;
	/**
	 * Field maxOnline.
	 */
	private int maxOnline;
	/**
	 * Field _serverType.
	 */
	private int _serverType;
	/**
	 * Field _ageLimit.
	 */
	private int _ageLimit;
	/**
	 * Field _gmOnly.
	 */
	private boolean _gmOnly;
	/**
	 * Field _brackets.
	 */
	private boolean _brackets;
	/**
	 * Field _pvp.
	 */
	private boolean _pvp;
	/**
	 * Field ports.
	 */
	private int[] ports;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_protocolVersion = readD();
		requestId = readC();
		acceptAlternateID = readC() == 1;
		_serverType = readD();
		_ageLimit = readD();
		_gmOnly = readC() == 1;
		_brackets = readC() == 1;
		_pvp = readC() == 1;
		externalIp = readS();
		internalIp = readS();
		ports = new int[readH()];
		for (int i = 0; i < ports.length; i++)
		{
			ports[i] = readH();
		}
		maxOnline = readD();
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		_log.info("Trying to register gameserver: " + requestId + " [" + getGameServer().getConnection().getIpAddress() + "]");
		int failReason = 0;
		GameServer gs = getGameServer();
		if (GameServerManager.getInstance().registerGameServer(requestId, gs))
		{
			gs.setPorts(ports);
			gs.setExternalHost(externalIp);
			gs.setInternalHost(internalIp);
			gs.setMaxPlayers(maxOnline);
			gs.setPvp(_pvp);
			gs.setServerType(_serverType);
			gs.setShowingBrackets(_brackets);
			gs.setGmOnly(_gmOnly);
			gs.setAgeLimit(_ageLimit);
			gs.setProtocol(_protocolVersion);
			gs.setAuthed(true);
			gs.getConnection().startPingTask();
		}
		else if (acceptAlternateID)
		{
			if (GameServerManager.getInstance().registerGameServer(gs = getGameServer()))
			{
				gs.setPorts(ports);
				gs.setExternalHost(externalIp);
				gs.setInternalHost(internalIp);
				gs.setMaxPlayers(maxOnline);
				gs.setPvp(_pvp);
				gs.setServerType(_serverType);
				gs.setShowingBrackets(_brackets);
				gs.setGmOnly(_gmOnly);
				gs.setAgeLimit(_ageLimit);
				gs.setProtocol(_protocolVersion);
				gs.setAuthed(true);
				gs.getConnection().startPingTask();
			}
			else
			{
				failReason = LoginServerFail.REASON_NO_FREE_ID;
			}
		}
		else
		{
			failReason = LoginServerFail.REASON_ID_RESERVED;
		}
		if (failReason != 0)
		{
			_log.info("Gameserver registration failed.");
			sendPacket(new LoginServerFail(failReason));
			return;
		}
		_log.info("Gameserver registration successful.");
		sendPacket(new AuthResponse(gs));
	}
}
