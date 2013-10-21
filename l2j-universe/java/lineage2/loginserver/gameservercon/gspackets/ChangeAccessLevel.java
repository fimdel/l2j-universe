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

import lineage2.loginserver.accounts.Account;
import lineage2.loginserver.gameservercon.ReceivablePacket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ChangeAccessLevel extends ReceivablePacket
{
	/**
	 * Field _log.
	 */
	public static final Logger _log = LoggerFactory.getLogger(ChangeAccessLevel.class);
	/**
	 * Field account.
	 */
	private String account;
	/**
	 * Field level.
	 */
	private int level;
	/**
	 * Field banExpire.
	 */
	private int banExpire;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		account = readS();
		level = readD();
		banExpire = readD();
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		Account acc = new Account(account);
		acc.restore();
		acc.setAccessLevel(level);
		acc.setBanExpire(banExpire);
		acc.update();
	}
}
