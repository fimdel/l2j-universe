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
public class ChangePassword extends SendablePacket
{
	/**
	 * Field account.
	 */
	private final String account;
	/**
	 * Field oldPass.
	 */
	private final String oldPass;
	/**
	 * Field newPass.
	 */
	private final String newPass;
	/**
	 * Field hwid.
	 */
	private final String hwid;
	
	/**
	 * Constructor for ChangePassword.
	 * @param account String
	 * @param oldPass String
	 * @param newPass String
	 * @param hwid String
	 */
	public ChangePassword(String account, String oldPass, String newPass, String hwid)
	{
		this.account = account;
		this.oldPass = oldPass;
		this.newPass = newPass;
		this.hwid = hwid;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected void writeImpl()
	{
		writeC(0x08);
		writeS(account);
		writeS(oldPass);
		writeS(newPass);
		writeS(hwid);
	}
}
