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
public class BonusRequest extends SendablePacket
{
	/**
	 * Field account.
	 */
	private final String account;
	/**
	 * Field bonus.
	 */
	private final double bonus;
	/**
	 * Field bonusExpire.
	 */
	private final int bonusExpire;
	
	/**
	 * Constructor for BonusRequest.
	 * @param account String
	 * @param bonus double
	 * @param bonusExpire int
	 */
	public BonusRequest(String account, double bonus, int bonusExpire)
	{
		this.account = account;
		this.bonus = bonus;
		this.bonusExpire = bonusExpire;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected void writeImpl()
	{
		writeC(0x10);
		writeS(account);
		writeF(bonus);
		writeD(bonusExpire);
	}
}
