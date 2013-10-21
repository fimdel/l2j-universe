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

import lineage2.loginserver.SessionKey;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class PlayOk extends L2LoginServerPacket
{
	/**
	 * Field _playOk2. Field _playOk1.
	 */
	private final int _playOk1, _playOk2;
	
	/**
	 * Constructor for PlayOk.
	 * @param sessionKey SessionKey
	 */
	public PlayOk(SessionKey sessionKey)
	{
		_playOk1 = sessionKey.playOkID1;
		_playOk2 = sessionKey.playOkID2;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected void writeImpl()
	{
		writeC(0x07);
		writeD(_playOk1);
		writeD(_playOk2);
	}
}
