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
package lineage2.gameserver.network.loginservercon.lspackets;

import lineage2.gameserver.network.loginservercon.LoginServerCommunication;
import lineage2.gameserver.network.loginservercon.ReceivablePacket;
import lineage2.gameserver.network.loginservercon.gspackets.OnlineStatus;
import lineage2.gameserver.network.loginservercon.gspackets.PlayerInGame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AuthResponse extends ReceivablePacket
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(AuthResponse.class);
	/**
	 * Field _serverId.
	 */
	private int _serverId;
	/**
	 * Field _serverName.
	 */
	private String _serverName;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_serverId = readC();
		_serverName = readS();
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		_log.info("Registered on loginserver as " + _serverId + " [" + _serverName + "]");
		sendPacket(new OnlineStatus(true));
		String[] accounts = LoginServerCommunication.getInstance().getAccounts();
		for (String account : accounts)
		{
			sendPacket(new PlayerInGame(account));
		}
	}
}
