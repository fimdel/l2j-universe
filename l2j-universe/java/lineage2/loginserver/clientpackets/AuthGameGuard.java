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

import lineage2.loginserver.L2LoginClient;
import lineage2.loginserver.L2LoginClient.LoginClientState;
import lineage2.loginserver.serverpackets.GGAuth;
import lineage2.loginserver.serverpackets.LoginFail;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AuthGameGuard extends L2LoginClientPacket
{
	/**
	 * Field _sessionId.
	 */
	private int _sessionId;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_sessionId = readD();
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		L2LoginClient client = getClient();
		if ((_sessionId == 0) || (_sessionId == client.getSessionId()))
		{
			client.setState(LoginClientState.AUTHED_GG);
			client.sendPacket(new GGAuth(client.getSessionId()));
		}
		else
		{
			client.close(LoginFail.LoginFailReason.REASON_ACCESS_FAILED);
		}
	}
}
