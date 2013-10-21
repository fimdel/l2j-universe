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
package lineage2.gameserver.network.serverpackets;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ExWaitWaitingSubStituteInfo extends L2GameServerPacket
{
	/**
	 * Field turnOn.
	 */
	boolean turnOn;
	
	/**
	 * Constructor for ExWaitWaitingSubStituteInfo.
	 * @param _turnOn boolean
	 */
	public ExWaitWaitingSubStituteInfo(boolean _turnOn)
	{
		turnOn = _turnOn;
	}
	
	public static final int WAITING_CANCEL = 0;
	public static final int WAITING_OK = 1;

	private int _code;

	public ExWaitWaitingSubStituteInfo(int code)
	{
		_code = code;
	}

/**
	 * Method writeImpl.
	 */
	@Override
	protected void writeImpl()
	{
        writeEx(0x104);
		writeD(turnOn ? 0x01 : 0x00);
		writeD(0x00);
		writeD(_code);
	}

}
















