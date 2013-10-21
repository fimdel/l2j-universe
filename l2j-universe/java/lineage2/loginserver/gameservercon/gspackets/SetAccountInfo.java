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

import lineage2.loginserver.accounts.SessionManager;
import lineage2.loginserver.gameservercon.GameServer;
import lineage2.loginserver.gameservercon.ReceivablePacket;

import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class SetAccountInfo extends ReceivablePacket
{
	/**
	 * Field _account.
	 */
	private String _account;
	/**
	 * Field _size.
	 */
	private int _size;
	/**
	 * Field _deleteChars.
	 */
	private int[] _deleteChars;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_account = readS();
		_size = readC();
		int size = readD();
		if ((size > 7) || (size <= 0))
		{
			_deleteChars = ArrayUtils.EMPTY_INT_ARRAY;
		}
		else
		{
			_deleteChars = new int[size];
			for (int i = 0; i < _deleteChars.length; i++)
			{
				_deleteChars[i] = readD();
			}
		}
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		GameServer gs = getGameServer();
		if (gs.isAuthed())
		{
			SessionManager.Session session = SessionManager.getInstance().getSessionByName(_account);
			if (session == null)
			{
				return;
			}
			session.getAccount().addAccountInfo(gs.getId(), _size, _deleteChars);
		}
	}
}
