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

import lineage2.loginserver.accounts.SecondaryPasswordAuth;
import lineage2.loginserver.gameservercon.ReceivablePacket;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Player2ndAuthSetAttempts extends ReceivablePacket
{
	/**
	 * Field _account.
	 */
	private String _account;
	/**
	 * Field _attempts.
	 */
	private int _attempts;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_account = readS();
		_attempts = readD();
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		SecondaryPasswordAuth.setLoginAttempts(_account, _attempts);
	}
}
