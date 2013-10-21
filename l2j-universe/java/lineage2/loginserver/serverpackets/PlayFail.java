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
package lineage2.loginserver.serverpackets;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class PlayFail extends L2LoginServerPacket
{
	/**
	 * Field REASON_SYSTEM_ERROR. (value is 1)
	 */
	public static final int REASON_SYSTEM_ERROR = 1;
	/**
	 * Field REASON_ACCESS_FAILED_1. (value is 2)
	 */
	public static final int REASON_ACCESS_FAILED_1 = 2;
	/**
	 * Field REASON_ACCOUNT_INFO_INCORRECT. (value is 3)
	 */
	public static final int REASON_ACCOUNT_INFO_INCORRECT = 3;
	/**
	 * Field REASON_PASSWORD_INCORRECT_1. (value is 4)
	 */
	public static final int REASON_PASSWORD_INCORRECT_1 = 4;
	/**
	 * Field REASON_PASSWORD_INCORRECT_2. (value is 5)
	 */
	public static final int REASON_PASSWORD_INCORRECT_2 = 5;
	/**
	 * Field REASON_NO_REASON. (value is 6)
	 */
	public static final int REASON_NO_REASON = 6;
	/**
	 * Field REASON_SYS_ERROR. (value is 7)
	 */
	public static final int REASON_SYS_ERROR = 7;
	/**
	 * Field REASON_ACCESS_FAILED_2. (value is 8)
	 */
	public static final int REASON_ACCESS_FAILED_2 = 8;
	/**
	 * Field REASON_HIGH_SERVER_TRAFFIC. (value is 9)
	 */
	public static final int REASON_HIGH_SERVER_TRAFFIC = 9;
	/**
	 * Field REASON_MIN_AGE. (value is 10)
	 */
	public static final int REASON_MIN_AGE = 10;
	/**
	 * Field _reason.
	 */
	private final int _reason;
	
	/**
	 * Constructor for PlayFail.
	 * @param reason int
	 */
	public PlayFail(int reason)
	{
		_reason = reason;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected void writeImpl()
	{
		writeC(0x06);
		writeC(_reason);
	}
}
