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
public class SetAccountInfo extends SendablePacket
{
	/**
	 * Field _account.
	 */
	private final String _account;
	/**
	 * Field _size.
	 */
	private final int _size;
	/**
	 * Field _deleteChars.
	 */
	private final int[] _deleteChars;
	
	/**
	 * Constructor for SetAccountInfo.
	 * @param account String
	 * @param size int
	 * @param deleteChars int[]
	 */
	public SetAccountInfo(String account, int size, int[] deleteChars)
	{
		_account = account;
		_size = size;
		_deleteChars = deleteChars;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected void writeImpl()
	{
		writeC(0x05);
		writeS(_account);
		writeC(_size);
		writeD(_deleteChars.length);
		for (int i : _deleteChars)
		{
			writeD(i);
		}
	}
}
