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

import javax.crypto.Cipher;

import lineage2.loginserver.Config;
import lineage2.loginserver.GameServerManager;
import lineage2.loginserver.IpBanManager;
import lineage2.loginserver.L2LoginClient;
import lineage2.loginserver.L2LoginClient.LoginClientState;
import lineage2.loginserver.accounts.Account;
import lineage2.loginserver.accounts.SessionManager;
import lineage2.loginserver.accounts.SessionManager.Session;
import lineage2.loginserver.crypt.PasswordHash;
import lineage2.loginserver.gameservercon.GameServer;
import lineage2.loginserver.gameservercon.lspackets.GetAccountInfo;
import lineage2.loginserver.serverpackets.LoginFail.LoginFailReason;
import lineage2.loginserver.serverpackets.LoginOk;
import lineage2.loginserver.utils.Log;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestAuthLogin extends L2LoginClientPacket
{
	/**
	 * Field _raw.
	 */
	private final byte[] _raw = new byte[128];
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		readB(_raw);
		readD();
		readD();
		readD();
		readD();
		readD();
		readD();
		readH();
		readC();
	}
	
	/**
	 * Method runImpl.
	 * @throws Exception
	 */
	@Override
	protected void runImpl() throws Exception
	{
		L2LoginClient client = getClient();
		byte[] decrypted;
		try
		{
			Cipher rsaCipher = Cipher.getInstance("RSA/ECB/nopadding");
			rsaCipher.init(Cipher.DECRYPT_MODE, client.getRSAPrivateKey());
			decrypted = rsaCipher.doFinal(_raw, 0x00, 0x80);
		}
		catch (Exception e)
		{
			client.closeNow(true);
			return;
		}
		String user = new String(decrypted, 0x5E, 14).trim();
		user = user.toLowerCase();
		String password = new String(decrypted, 0x6C, 16).trim();
		int currentTime = (int) (System.currentTimeMillis() / 1000L);
		Account account = new Account(user);
		account.restore();
		String passwordHash = Config.DEFAULT_CRYPT.encrypt(password);
		if (account.getPasswordHash() == null)
		{
			if (Config.AUTO_CREATE_ACCOUNTS && user.matches(Config.ANAME_TEMPLATE) && password.matches(Config.APASSWD_TEMPLATE))
			{
				account.setAllowedIP("");
				account.setPasswordHash(passwordHash);
				account.save();
			}
			else
			{
				client.close(LoginFailReason.REASON_USER_OR_PASS_WRONG);
				return;
			}
		}
		boolean passwordCorrect = account.getPasswordHash().equals(passwordHash);
		if (!passwordCorrect)
		{
			for (PasswordHash c : Config.LEGACY_CRYPT)
			{
				if (c.compare(password, account.getPasswordHash()))
				{
					passwordCorrect = true;
					account.setPasswordHash(passwordHash);
					break;
				}
			}
		}
		if (!IpBanManager.getInstance().tryLogin(client.getIpAddress(), passwordCorrect))
		{
			client.closeNow(false);
			return;
		}
		if (!passwordCorrect)
		{
			client.close(LoginFailReason.REASON_USER_OR_PASS_WRONG);
			return;
		}
		if (account.getAccessLevel() < 0)
		{
			client.close(LoginFailReason.REASON_ACCESS_FAILED);
			return;
		}
		if (account.getBanExpire() > currentTime)
		{
			client.close(LoginFailReason.REASON_ACCESS_FAILED);
			return;
		}
		if (!account.isAllowedIP(client.getIpAddress()))
		{
			client.close(LoginFailReason.REASON_ATTEMPTED_RESTRICTED_IP);
			return;
		}
		for (GameServer gs : GameServerManager.getInstance().getGameServers())
		{
			if ((gs.getProtocol() >= 2) && gs.isAuthed())
			{
				gs.sendPacket(new GetAccountInfo(user));
			}
		}
		account.setLastAccess(currentTime);
		account.setLastIP(client.getIpAddress());
		Log.LogAccount(account);
		Session session = SessionManager.getInstance().openSession(account);
		client.setAuthed(true);
		client.setLogin(user);
		client.setAccount(account);
		client.setSessionKey(session.getSessionKey());
		client.setState(LoginClientState.AUTHED);
		client.sendPacket(new LoginOk(client.getSessionKey()));
	}
}
