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
package lineage2.gameserver.network.clientpackets;

import lineage2.gameserver.Config;
import lineage2.gameserver.network.serverpackets.Ex2ndPasswordAck;
import lineage2.gameserver.utils.SecondaryPasswordAuth;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestEx2ndPasswordReq extends L2GameClientPacket
{
	/**
	 * Field _changePass.
	 */
	private int _changePass;
	/**
	 * Field _password.
	 */
	private String _password;
	/**
	 * Field _newPassword.
	 */
	private String _newPassword;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_changePass = readC();
		_password = readS();
		if (_changePass == 2)
		{
			_newPassword = readS();
		}
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		if (!Config.SECOND_AUTH_ENABLED)
		{
			return;
		}
		SecondaryPasswordAuth spa = getClient().getSecondaryAuth();
		boolean exVal = false;
		if ((_changePass == 0) && !spa.passwordExist())
		{
			exVal = spa.savePassword(_password);
		}
		else if ((_changePass == 2) && spa.passwordExist())
		{
			exVal = spa.changePassword(_password, _newPassword);
		}
		if (exVal)
		{
			getClient().sendPacket(new Ex2ndPasswordAck(Ex2ndPasswordAck.SUCCESS));
		}
	}
}
