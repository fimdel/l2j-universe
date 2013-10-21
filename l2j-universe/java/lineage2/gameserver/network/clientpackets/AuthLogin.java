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
package lineage2.gameserver.network.clientpackets;

import lineage2.gameserver.Shutdown;
import lineage2.gameserver.network.GameClient;
import lineage2.gameserver.network.loginservercon.LoginServerCommunication;
import lineage2.gameserver.network.loginservercon.SessionKey;
import lineage2.gameserver.network.loginservercon.gspackets.PlayerAuthRequest;
import lineage2.gameserver.network.serverpackets.LoginFail;
import lineage2.gameserver.network.serverpackets.ServerClose;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AuthLogin extends L2GameClientPacket
{
	/**
	 * Field _loginName.
	 */
	private String _loginName;
	/**
	 * Field _playKey1.
	 */
	private int _playKey1;
	/**
	 * Field _playKey2.
	 */
	private int _playKey2;
	/**
	 * Field _loginKey1.
	 */
	private int _loginKey1;
	/**
	 * Field _loginKey2.
	 */
	private int _loginKey2;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_loginName = readS(32).toLowerCase();
		_playKey2 = readD();
		_playKey1 = readD();
		_loginKey1 = readD();
		_loginKey2 = readD();
		readD();
		readD();
		readD();
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		GameClient client = getClient();
		SessionKey key = new SessionKey(_loginKey1, _loginKey2, _playKey1, _playKey2);
		client.setSessionId(key);
		client.setLoginName(_loginName);
		if ((Shutdown.getInstance().getMode() != Shutdown.NONE) && (Shutdown.getInstance().getSeconds() <= 15))
		{
			client.closeNow(false);
		}
		else
		{
			if (LoginServerCommunication.getInstance().isShutdown())
			{
				client.close(new LoginFail(LoginFail.SYSTEM_ERROR_LOGIN_LATER));
				return;
			}
			GameClient oldClient = LoginServerCommunication.getInstance().addWaitingClient(client);
			if (oldClient != null)
			{
				oldClient.close(ServerClose.STATIC);
			}
			LoginServerCommunication.getInstance().sendPacket(new PlayerAuthRequest(client));
		}
	}
}
