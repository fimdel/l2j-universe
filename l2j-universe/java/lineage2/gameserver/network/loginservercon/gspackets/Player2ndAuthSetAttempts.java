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
package lineage2.gameserver.network.loginservercon.gspackets;

import lineage2.gameserver.network.loginservercon.SendablePacket;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Player2ndAuthSetAttempts extends SendablePacket
{
	/**
	 * Field _login.
	 */
	private final String _login;
	/**
	 * Field _attempts.
	 */
	private final int _attempts;
	
	/**
	 * Constructor for Player2ndAuthSetAttempts.
	 * @param login String
	 * @param attempts int
	 */
	public Player2ndAuthSetAttempts(String login, int attempts)
	{
		_login = login;
		_attempts = attempts;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected void writeImpl()
	{
		writeC(0x16);
		writeS(_login);
		writeD(_attempts);
	}
}
