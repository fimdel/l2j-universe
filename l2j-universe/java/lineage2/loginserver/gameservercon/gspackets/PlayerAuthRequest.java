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

import lineage2.loginserver.SessionKey;
import lineage2.loginserver.accounts.SessionManager;
import lineage2.loginserver.accounts.SessionManager.Session;
import lineage2.loginserver.gameservercon.ReceivablePacket;
import lineage2.loginserver.gameservercon.lspackets.PlayerAuthResponse;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class PlayerAuthRequest extends ReceivablePacket
{
	/**
	 * Field account.
	 */
	private String account;
	/**
	 * Field playOkId1.
	 */
	private int playOkId1;
	/**
	 * Field playOkId2.
	 */
	private int playOkId2;
	/**
	 * Field loginOkId1.
	 */
	private int loginOkId1;
	/**
	 * Field loginOkId2.
	 */
	private int loginOkId2;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		account = readS();
		playOkId1 = readD();
		playOkId2 = readD();
		loginOkId1 = readD();
		loginOkId2 = readD();
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		SessionKey skey = new SessionKey(loginOkId1, loginOkId2, playOkId1, playOkId2);
		Session session = SessionManager.getInstance().closeSession(skey);
		if ((session == null) || !session.getAccount().getLogin().equals(account))
		{
			sendPacket(new PlayerAuthResponse(account));
			return;
		}
		sendPacket(new PlayerAuthResponse(session, session.getSessionKey().equals(skey)));
	}
}
