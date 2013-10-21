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
package lineage2.loginserver.clientpackets;

import lineage2.loginserver.GameServerManager;
import lineage2.loginserver.L2LoginClient;
import lineage2.loginserver.SessionKey;
import lineage2.loginserver.accounts.Account;
import lineage2.loginserver.gameservercon.GameServer;
import lineage2.loginserver.serverpackets.LoginFail.LoginFailReason;
import lineage2.loginserver.serverpackets.PlayOk;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestServerLogin extends L2LoginClientPacket
{
	/**
	 * Field _loginOkID1.
	 */
	private int _loginOkID1;
	/**
	 * Field _loginOkID2.
	 */
	private int _loginOkID2;
	/**
	 * Field _serverId.
	 */
	private int _serverId;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_loginOkID1 = readD();
		_loginOkID2 = readD();
		_serverId = readC();
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		L2LoginClient client = getClient();
		SessionKey skey = client.getSessionKey();
		if ((skey == null) || !skey.checkLoginPair(_loginOkID1, _loginOkID2))
		{
			client.close(LoginFailReason.REASON_ACCESS_FAILED);
			return;
		}
		Account account = client.getAccount();
		GameServer gs = GameServerManager.getInstance().getGameServerById(_serverId);
		if ((gs == null) || !gs.isAuthed() || (gs.isGmOnly() && (account.getAccessLevel() < 100)) || ((gs.getOnline() >= gs.getMaxPlayers()) && (account.getAccessLevel() < 50)))
		{
			client.close(LoginFailReason.REASON_ACCESS_FAILED);
			return;
		}
		account.setLastServer(_serverId);
		account.update();
		client.close(new PlayOk(skey));
	}
}
