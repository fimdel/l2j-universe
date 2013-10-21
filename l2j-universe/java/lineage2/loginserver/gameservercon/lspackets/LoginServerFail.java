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

import lineage2.loginserver.gameservercon.SendablePacket;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class LoginServerFail extends SendablePacket
{
	/**
	 * Field REASON_IP_BANNED. (value is 1)
	 */
	public static final int REASON_IP_BANNED = 1;
	/**
	 * Field REASON_IP_RESERVED. (value is 2)
	 */
	public static final int REASON_IP_RESERVED = 2;
	/**
	 * Field REASON_WRONG_HEXID. (value is 3)
	 */
	public static final int REASON_WRONG_HEXID = 3;
	/**
	 * Field REASON_ID_RESERVED. (value is 4)
	 */
	public static final int REASON_ID_RESERVED = 4;
	/**
	 * Field REASON_NO_FREE_ID. (value is 5)
	 */
	public static final int REASON_NO_FREE_ID = 5;
	/**
	 * Field NOT_AUTHED. (value is 6)
	 */
	public static final int NOT_AUTHED = 6;
	/**
	 * Field REASON_ALREADY_LOGGED_IN. (value is 7)
	 */
	public static final int REASON_ALREADY_LOGGED_IN = 7;
	/**
	 * Field reason.
	 */
	private final int reason;
	
	/**
	 * Constructor for LoginServerFail.
	 * @param reason int
	 */
	public LoginServerFail(int reason)
	{
		this.reason = reason;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected void writeImpl()
	{
		writeC(0x01);
		writeC(reason);
	}
}
