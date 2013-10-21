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

import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.interfaces.RSAPrivateKey;

import lineage2.commons.net.nio.impl.MMOClient;
import lineage2.commons.net.nio.impl.MMOConnection;
import lineage2.loginserver.accounts.Account;
import lineage2.loginserver.crypt.LoginCrypt;
import lineage2.loginserver.crypt.ScrambledKeyPair;
import lineage2.loginserver.serverpackets.AccountKicked;
import lineage2.loginserver.serverpackets.AccountKicked.AccountKickedReason;
import lineage2.loginserver.serverpackets.L2LoginServerPacket;
import lineage2.loginserver.serverpackets.LoginFail;
import lineage2.loginserver.serverpackets.LoginFail.LoginFailReason;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class L2LoginClient extends MMOClient<MMOConnection<L2LoginClient>>
{
	/**
	 * Field _log.
	 */
	private final static Logger _log = LoggerFactory.getLogger(L2LoginClient.class);
	
	/**
	 * @author Mobius
	 */
	public static enum LoginClientState
	{
		/**
		 * Field CONNECTED.
		 */
		CONNECTED,
		/**
		 * Field AUTHED_GG.
		 */
		AUTHED_GG,
		/**
		 * Field AUTHED.
		 */
		AUTHED,
		/**
		 * Field DISCONNECTED.
		 */
		DISCONNECTED
	}
	
	/**
	 * Field _state.
	 */
	private LoginClientState _state;
	/**
	 * Field _loginCrypt.
	 */
	private LoginCrypt _loginCrypt;
	/**
	 * Field _scrambledPair.
	 */
	private ScrambledKeyPair _scrambledPair;
	/**
	 * Field _blowfishKey.
	 */
	private byte[] _blowfishKey;
	/**
	 * Field _login.
	 */
	private String _login;
	/**
	 * Field _skey.
	 */
	private SessionKey _skey;
	/**
	 * Field _account.
	 */
	private Account _account;
	/**
	 * Field _ipAddr.
	 */
	private final String _ipAddr;
	/**
	 * Field _sessionId.
	 */
	private int _sessionId;
	
	/**
	 * Constructor for L2LoginClient.
	 * @param con MMOConnection<L2LoginClient>
	 */
	public L2LoginClient(MMOConnection<L2LoginClient> con)
	{
		super(con);
		_state = LoginClientState.CONNECTED;
		_scrambledPair = Config.getScrambledRSAKeyPair();
		_blowfishKey = Config.getBlowfishKey();
		_loginCrypt = new LoginCrypt();
		_loginCrypt.setKey(_blowfishKey);
		_sessionId = con.hashCode();
		_ipAddr = getConnection().getSocket().getInetAddress().getHostAddress();
	}
	
	/**
	 * Method decrypt.
	 * @param buf ByteBuffer
	 * @param size int
	 * @return boolean
	 */
	@Override
	public boolean decrypt(ByteBuffer buf, int size)
	{
		boolean ret;
		try
		{
			ret = _loginCrypt.decrypt(buf.array(), buf.position(), size);
		}
		catch (IOException e)
		{
			_log.error("", e);
			closeNow(true);
			return false;
		}
		if (!ret)
		{
			closeNow(true);
		}
		return ret;
	}
	
	/**
	 * Method encrypt.
	 * @param buf ByteBuffer
	 * @param size int
	 * @return boolean
	 */
	@Override
	public boolean encrypt(ByteBuffer buf, int size)
	{
		final int offset = buf.position();
		try
		{
			size = _loginCrypt.encrypt(buf.array(), offset, size);
		}
		catch (IOException e)
		{
			_log.error("", e);
			return false;
		}
		buf.position(offset + size);
		return true;
	}
	
	/**
	 * Method getState.
	 * @return LoginClientState
	 */
	public LoginClientState getState()
	{
		return _state;
	}
	
	/**
	 * Method setState.
	 * @param state LoginClientState
	 */
	public void setState(LoginClientState state)
	{
		_state = state;
	}
	
	/**
	 * Method getBlowfishKey.
	 * @return byte[]
	 */
	public byte[] getBlowfishKey()
	{
		return _blowfishKey;
	}
	
	/**
	 * Method getScrambledModulus.
	 * @return byte[]
	 */
	public byte[] getScrambledModulus()
	{
		return _scrambledPair.getScrambledModulus();
	}
	
	/**
	 * Method getRSAPrivateKey.
	 * @return RSAPrivateKey
	 */
	public RSAPrivateKey getRSAPrivateKey()
	{
		return (RSAPrivateKey) _scrambledPair.getKeyPair().getPrivate();
	}
	
	/**
	 * Method getLogin.
	 * @return String
	 */
	public String getLogin()
	{
		return _login;
	}
	
	/**
	 * Method setLogin.
	 * @param login String
	 */
	public void setLogin(String login)
	{
		_login = login;
	}
	
	/**
	 * Method getAccount.
	 * @return Account
	 */
	public Account getAccount()
	{
		return _account;
	}
	
	/**
	 * Method setAccount.
	 * @param account Account
	 */
	public void setAccount(Account account)
	{
		_account = account;
	}
	
	/**
	 * Method getSessionKey.
	 * @return SessionKey
	 */
	public SessionKey getSessionKey()
	{
		return _skey;
	}
	
	/**
	 * Method setSessionKey.
	 * @param skey SessionKey
	 */
	public void setSessionKey(SessionKey skey)
	{
		_skey = skey;
	}
	
	/**
	 * Method setSessionId.
	 * @param val int
	 */
	public void setSessionId(int val)
	{
		_sessionId = val;
	}
	
	/**
	 * Method getSessionId.
	 * @return int
	 */
	public int getSessionId()
	{
		return _sessionId;
	}
	
	/**
	 * Method sendPacket.
	 * @param lsp L2LoginServerPacket
	 */
	public void sendPacket(L2LoginServerPacket lsp)
	{
		if (isConnected())
		{
			getConnection().sendPacket(lsp);
		}
	}
	
	/**
	 * Method close.
	 * @param reason LoginFailReason
	 */
	public void close(LoginFailReason reason)
	{
		if (isConnected())
		{
			getConnection().close(new LoginFail(reason));
		}
	}
	
	/**
	 * Method close.
	 * @param reason AccountKickedReason
	 */
	public void close(AccountKickedReason reason)
	{
		if (isConnected())
		{
			getConnection().close(new AccountKicked(reason));
		}
	}
	
	/**
	 * Method close.
	 * @param lsp L2LoginServerPacket
	 */
	public void close(L2LoginServerPacket lsp)
	{
		if (isConnected())
		{
			getConnection().close(lsp);
		}
	}
	
	/**
	 * Method onDisconnection.
	 */
	@Override
	public void onDisconnection()
	{
		_state = LoginClientState.DISCONNECTED;
		_skey = null;
		_loginCrypt = null;
		_scrambledPair = null;
		_blowfishKey = null;
	}
	
	/**
	 * Method toString.
	 * @return String
	 */
	@Override
	public String toString()
	{
		switch (_state)
		{
			case AUTHED:
				return "[ Account : " + getLogin() + " IP: " + getIpAddress() + "]";
			default:
				return "[ State : " + getState() + " IP: " + getIpAddress() + "]";
		}
	}
	
	/**
	 * Method getIpAddress.
	 * @return String
	 */
	public String getIpAddress()
	{
		return _ipAddr;
	}
	
	/**
	 * Method onForcedDisconnection.
	 */
	@Override
	protected void onForcedDisconnection()
	{
	}
}
