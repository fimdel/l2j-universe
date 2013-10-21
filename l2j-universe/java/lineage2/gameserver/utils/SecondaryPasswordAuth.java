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
package lineage2.gameserver.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

import lineage2.gameserver.Config;
import lineage2.gameserver.network.GameClient;
import lineage2.gameserver.network.loginservercon.LoginServerCommunication;
import lineage2.gameserver.network.loginservercon.gspackets.Player2ndAuthSetAttempts;
import lineage2.gameserver.network.loginservercon.gspackets.Player2ndAuthSetBanTime;
import lineage2.gameserver.network.loginservercon.gspackets.Player2ndAuthSetPassword;
import lineage2.gameserver.network.serverpackets.Ex2ndPasswordAck;
import lineage2.gameserver.network.serverpackets.Ex2ndPasswordCheck;
import lineage2.gameserver.network.serverpackets.Ex2ndPasswordVerify;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class SecondaryPasswordAuth
{
	/**
	 * Field _log.
	 */
	private final Logger _log = Logger.getLogger(SecondaryPasswordAuth.class.getName());
	/**
	 * Field _activeClient.
	 */
	private final GameClient _activeClient;
	/**
	 * Field _password.
	 */
	private String _password;
	/**
	 * Field _wrongAttempts.
	 */
	private int _wrongAttempts;
	/**
	 * Field _authed.
	 */
	private boolean _authed;
	/**
	 * Field _unBanTime.
	 */
	private long _unBanTime;
	
	/**
	 * Constructor for SecondaryPasswordAuth.
	 * @param activeClient GameClient
	 * @param pwd String
	 * @param wrongAttempts int
	 * @param unbanTime long
	 */
	public SecondaryPasswordAuth(GameClient activeClient, String pwd, int wrongAttempts, long unbanTime)
	{
		_activeClient = activeClient;
		_password = pwd;
		_wrongAttempts = wrongAttempts;
		_authed = false;
		_unBanTime = unbanTime;
	}
	
	/**
	 * Method setPassword.
	 * @param password String
	 */
	public void setPassword(String password)
	{
		_password = password;
	}
	
	/**
	 * Method setUnbanTime.
	 * @param unbanTime long
	 */
	public void setUnbanTime(long unbanTime)
	{
		_unBanTime = unbanTime;
	}
	
	/**
	 * Method setWrongAttempts.
	 * @param wrongAttempts int
	 */
	public void setWrongAttempts(int wrongAttempts)
	{
		_wrongAttempts = wrongAttempts;
	}
	
	/**
	 * Method savePassword.
	 * @param password String
	 * @return boolean
	 */
	public boolean savePassword(String password)
	{
		if (passwordExist())
		{
			_log.warning("[SecondaryPasswordAuth]" + _activeClient.getLogin() + " forced savePassword");
			_activeClient.closeNow(false);
			return false;
		}
		if (!validatePassword(password))
		{
			_activeClient.sendPacket(new Ex2ndPasswordAck(Ex2ndPasswordAck.WRONG_PATTERN));
			return false;
		}
		password = cryptPassword(password);
		LoginServerCommunication.getInstance().sendPacket(new Player2ndAuthSetPassword(_activeClient.getLogin(), password));
		_password = password;
		return true;
	}
	
	/**
	 * Method insertWrongAttempt.
	 * @param attempts int
	 * @return boolean
	 */
	public boolean insertWrongAttempt(int attempts)
	{
		LoginServerCommunication.getInstance().sendPacket(new Player2ndAuthSetAttempts(_activeClient.getLogin(), attempts));
		return true;
	}
	
	/**
	 * Method changePassword.
	 * @param oldPassword String
	 * @param newPassword String
	 * @return boolean
	 */
	public boolean changePassword(String oldPassword, String newPassword)
	{
		if (!passwordExist())
		{
			_log.warning("[SecondaryPasswordAuth]" + _activeClient.getLogin() + " forced changePassword");
			_activeClient.closeNow(false);
			return false;
		}
		if (!checkPassword(oldPassword, true))
		{
			return false;
		}
		if (!validatePassword(newPassword))
		{
			_activeClient.sendPacket(new Ex2ndPasswordAck(Ex2ndPasswordAck.WRONG_PATTERN));
			return false;
		}
		newPassword = cryptPassword(newPassword);
		LoginServerCommunication.getInstance().sendPacket(new Player2ndAuthSetPassword(_activeClient.getLogin(), newPassword));
		_password = newPassword;
		_authed = false;
		return true;
	}
	
	/**
	 * Method checkPassword.
	 * @param password String
	 * @param skipAuth boolean
	 * @return boolean
	 */
	public boolean checkPassword(String password, boolean skipAuth)
	{
		password = cryptPassword(password);
		if (!password.equals(_password))
		{
			_wrongAttempts++;
			if (_wrongAttempts < Config.SECOND_AUTH_MAX_ATTEMPTS)
			{
				_activeClient.sendPacket(new Ex2ndPasswordVerify(Ex2ndPasswordVerify.PASSWORD_WRONG, Config.SECOND_AUTH_MAX_ATTEMPTS - _wrongAttempts));
				insertWrongAttempt(_wrongAttempts);
			}
			else
			{
				insertBanTime(Config.SECOND_AUTH_BAN_TIME);
				_log.warning(_activeClient.getLogin() + " - (" + _activeClient.getIpAddr() + ") has inputted the wrong password " + _wrongAttempts + " times in row.");
				insertWrongAttempt(0);
				_activeClient.sendPacket(new Ex2ndPasswordVerify(Ex2ndPasswordVerify.PASSWORD_BAN, Config.SECOND_AUTH_MAX_ATTEMPTS));
			}
			return false;
		}
		if (!skipAuth)
		{
			_authed = true;
			_activeClient.sendPacket(new Ex2ndPasswordVerify(Ex2ndPasswordVerify.PASSWORD_OK, _wrongAttempts));
		}
		insertWrongAttempt(0);
		return true;
	}
	
	/**
	 * Method passwordExist.
	 * @return boolean
	 */
	public boolean passwordExist()
	{
		return ((_password != null) && !_password.isEmpty());
	}
	
	/**
	 * Method insertBanTime.
	 * @param banTime int
	 */
	public void insertBanTime(int banTime)
	{
		LoginServerCommunication.getInstance().sendPacket(new Player2ndAuthSetBanTime(_activeClient.getLogin(), banTime));
		_unBanTime = System.currentTimeMillis() + (banTime * 60000);
	}
	
	/**
	 * Method openDialog.
	 */
	public void openDialog()
	{
		if (System.currentTimeMillis() <= _unBanTime)
		{
			_activeClient.sendPacket(new Ex2ndPasswordCheck(Ex2ndPasswordCheck.PASSWORD_PROMPT));
		}
		else if (passwordExist())
		{
			_activeClient.sendPacket(new Ex2ndPasswordCheck(Ex2ndPasswordCheck.PASSWORD_PROMPT));
		}
		else
		{
			_activeClient.sendPacket(new Ex2ndPasswordCheck(Ex2ndPasswordCheck.PASSWORD_NEW));
		}
	}
	
	/**
	 * Method isAuthed.
	 * @return boolean
	 */
	public boolean isAuthed()
	{
		return _authed;
	}
	
	/**
	 * Method cryptPassword.
	 * @param password String
	 * @return String
	 */
	private String cryptPassword(String password)
	{
		try
		{
			MessageDigest md = MessageDigest.getInstance("SHA");
			byte[] raw = password.getBytes("UTF-8");
			byte[] hash = md.digest(raw);
			return Base64.encode(hash);
		}
		catch (NoSuchAlgorithmException e)
		{
			_log.severe("[SecondaryPasswordAuth]Unsupported Algorythm");
		}
		catch (UnsupportedEncodingException e)
		{
			_log.severe("[SecondaryPasswordAuth]Unsupported Encoding");
		}
		return null;
	}
	
	/**
	 * Method validatePassword.
	 * @param password String
	 * @return boolean
	 */
	private boolean validatePassword(String password)
	{
		if (!Util.isNumber(password))
		{
			return false;
		}
		if ((password.length() < 6) || (password.length() > 8))
		{
			return false;
		}
		_wrongAttempts = 0;
		return true;
	}
}
