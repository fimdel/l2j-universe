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
public final class AccountKicked extends L2LoginServerPacket
{
	/**
	 * @author Mobius
	 */
	public static enum AccountKickedReason
	{
		/**
		 * Field REASON_FALSE_DATA_STEALER_REPORT.
		 */
		REASON_FALSE_DATA_STEALER_REPORT(0x00),
		/**
		 * Field REASON_DATA_STEALER.
		 */
		REASON_DATA_STEALER(0x01),
		/**
		 * Field REASON_SOUSPICION_DATA_STEALER.
		 */
		REASON_SOUSPICION_DATA_STEALER(0x03),
		/**
		 * Field REASON_NON_PAYEMENT_CELL_PHONE.
		 */
		REASON_NON_PAYEMENT_CELL_PHONE(0x04),
		/**
		 * Field REASON_30_DAYS_SUSPENDED_CASH.
		 */
		REASON_30_DAYS_SUSPENDED_CASH(0x08),
		/**
		 * Field REASON_PERMANENTLY_SUSPENDED_CASH.
		 */
		REASON_PERMANENTLY_SUSPENDED_CASH(0x10),
		/**
		 * Field REASON_PERMANENTLY_BANNED.
		 */
		REASON_PERMANENTLY_BANNED(0x20),
		/**
		 * Field REASON_ACCOUNT_MUST_BE_VERIFIED.
		 */
		REASON_ACCOUNT_MUST_BE_VERIFIED(0x40);
		/**
		 * Field _code.
		 */
		private final int _code;
		
		/**
		 * Constructor for AccountKickedReason.
		 * @param code int
		 */
		AccountKickedReason(int code)
		{
			_code = code;
		}
		
		/**
		 * Method getCode.
		 * @return int
		 */
		public final int getCode()
		{
			return _code;
		}
	}
	
	/**
	 * Field reason.
	 */
	private final int reason;
	
	/**
	 * Constructor for AccountKicked.
	 * @param reason AccountKickedReason
	 */
	public AccountKicked(AccountKickedReason reason)
	{
		this.reason = reason.getCode();
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected void writeImpl()
	{
		writeC(0x02);
		writeD(reason);
	}
}
