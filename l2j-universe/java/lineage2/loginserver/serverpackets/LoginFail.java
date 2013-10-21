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
public final class LoginFail extends L2LoginServerPacket
{
	/**
	 * @author Mobius
	 */
	public static enum LoginFailReason
	{
		/**
		 * Field REASON_NO_MESSAGE.
		 */
		REASON_NO_MESSAGE(0),
		/**
		 * Field REASON_SYSTEM_ERROR.
		 */
		REASON_SYSTEM_ERROR(1),
		/**
		 * Field REASON_PASS_WRONG.
		 */
		REASON_PASS_WRONG(2),
		/**
		 * Field REASON_USER_OR_PASS_WRONG.
		 */
		REASON_USER_OR_PASS_WRONG(3),
		/**
		 * Field REASON_ACCESS_FAILED_TRYA1.
		 */
		REASON_ACCESS_FAILED_TRYA1(4),
		/**
		 * Field REASON_ACCOUNT_INFO_INCORR.
		 */
		REASON_ACCOUNT_INFO_INCORR(5),
		/**
		 * Field REASON_ACCESS_FAILED_TRYA2.
		 */
		REASON_ACCESS_FAILED_TRYA2(6),
		/**
		 * Field REASON_ACCOUNT_IN_USE.
		 */
		REASON_ACCOUNT_IN_USE(7),
		/**
		 * Field REASON_MIN_AGE.
		 */
		REASON_MIN_AGE(12),
		/**
		 * Field REASON_SERVER_MAINTENANCE.
		 */
		REASON_SERVER_MAINTENANCE(16),
		/**
		 * Field REASON_CHANGE_TEMP_PASS.
		 */
		REASON_CHANGE_TEMP_PASS(17),
		/**
		 * Field REASON_USAGE_TEMP_EXPIRED.
		 */
		REASON_USAGE_TEMP_EXPIRED(18),
		/**
		 * Field REASON_TIME_LEFT_EXPIRED.
		 */
		REASON_TIME_LEFT_EXPIRED(19),
		/**
		 * Field REASON_SYS_ERR.
		 */
		REASON_SYS_ERR(20),
		/**
		 * Field REASON_ACCESS_FAILED.
		 */
		REASON_ACCESS_FAILED(21),
		/**
		 * Field REASON_ATTEMPTED_RESTRICTED_IP.
		 */
		REASON_ATTEMPTED_RESTRICTED_IP(22),
		/**
		 * Field REASON_WEEK_USAGE_TIME_END.
		 */
		REASON_WEEK_USAGE_TIME_END(30),
		/**
		 * Field REASON_SECURITY_CARD_NUMB_I.
		 */
		REASON_SECURITY_CARD_NUMB_I(31),
		/**
		 * Field REASON_VERIFY_AGE.
		 */
		REASON_VERIFY_AGE(32),
		/**
		 * Field REASON_CANNOT_ACC_COUPON.
		 */
		REASON_CANNOT_ACC_COUPON(33),
		/**
		 * Field REASON_DUAL_BOX.
		 */
		REASON_DUAL_BOX(35),
		/**
		 * Field REASON_ACCOUNT_INACTIVE.
		 */
		REASON_ACCOUNT_INACTIVE(36),
		/**
		 * Field REASON_USER_AGREEMENT_DIS.
		 */
		REASON_USER_AGREEMENT_DIS(37),
		/**
		 * Field REASON_GUARDIAN_CONSENT_REQ.
		 */
		REASON_GUARDIAN_CONSENT_REQ(38),
		/**
		 * Field REASON_USER_AGREEMENT_DEC.
		 */
		REASON_USER_AGREEMENT_DEC(39),
		/**
		 * Field REASON_ACCOUNT_SUSPENDED.
		 */
		REASON_ACCOUNT_SUSPENDED(40),
		/**
		 * Field REASON_CHANGE_PASS_AND_QUIZ.
		 */
		REASON_CHANGE_PASS_AND_QUIZ(41),
		/**
		 * Field REASON_LOGGED_INTO_10_ACCS.
		 */
		REASON_LOGGED_INTO_10_ACCS(42);
		/**
		 * Field _code.
		 */
		private final int _code;
		
		/**
		 * Constructor for LoginFailReason.
		 * @param code int
		 */
		LoginFailReason(int code)
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
	 * Field reason_code.
	 */
	private final int reason_code;
	
	/**
	 * Constructor for LoginFail.
	 * @param reason LoginFailReason
	 */
	public LoginFail(LoginFailReason reason)
	{
		reason_code = reason.getCode();
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected void writeImpl()
	{
		writeC(0x01);
		writeD(reason_code);
	}
}
