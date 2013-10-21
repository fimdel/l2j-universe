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
package lineage2.loginserver.gameservercon.lspackets;

import lineage2.loginserver.SessionKey;
import lineage2.loginserver.accounts.Account;
import lineage2.loginserver.accounts.SecondaryPasswordAuth;
import lineage2.loginserver.accounts.SessionManager.Session;
import lineage2.loginserver.gameservercon.SendablePacket;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class PlayerAuthResponse extends SendablePacket
{
	/**
	 * Field login.
	 */
	private final String login;
	/**
	 * Field authed.
	 */
	private final boolean authed;
	/**
	 * Field playOkID1.
	 */
	private int playOkID1;
	/**
	 * Field playOkID2.
	 */
	private int playOkID2;
	/**
	 * Field loginOkID1.
	 */
	private int loginOkID1;
	/**
	 * Field loginOkID2.
	 */
	private int loginOkID2;
	/**
	 * Field bonus.
	 */
	private double bonus;
	/**
	 * Field bonusExpire.
	 */
	private int bonusExpire;
	/**
	 * Field _2ndPassword.
	 */
	private String _2ndPassword;
	/**
	 * Field _2ndWrongAttempts.
	 */
	private int _2ndWrongAttempts;
	/**
	 * Field _2ndUnbanTime.
	 */
	private long _2ndUnbanTime;
	
	/**
	 * Constructor for PlayerAuthResponse.
	 * @param session Session
	 * @param authed boolean
	 */
	public PlayerAuthResponse(Session session, boolean authed)
	{
		Account account = session.getAccount();
		login = account.getLogin();
		this.authed = authed;
		if (authed)
		{
			SessionKey skey = session.getSessionKey();
			playOkID1 = skey.playOkID1;
			playOkID2 = skey.playOkID2;
			loginOkID1 = skey.loginOkID1;
			loginOkID2 = skey.loginOkID2;
			bonus = account.getBonus();
			bonusExpire = account.getBonusExpire();
			_2ndPassword = SecondaryPasswordAuth.getPassword(login);
			_2ndWrongAttempts = SecondaryPasswordAuth.getLoginAttempts(login);
			_2ndUnbanTime = SecondaryPasswordAuth.getBanTime(login);
		}
	}
	
	/**
	 * Constructor for PlayerAuthResponse.
	 * @param account String
	 */
	public PlayerAuthResponse(String account)
	{
		login = account;
		authed = false;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected void writeImpl()
	{
		writeC(0x02);
		writeS(login);
		writeC(authed ? 1 : 0);
		if (authed)
		{
			writeD(playOkID1);
			writeD(playOkID2);
			writeD(loginOkID1);
			writeD(loginOkID2);
			writeF(bonus);
			writeD(bonusExpire);
			writeS(_2ndPassword);
			writeD(_2ndWrongAttempts);
			writeQ(_2ndUnbanTime);
		}
	}
}
