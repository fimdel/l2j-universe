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

import lineage2.gameserver.cache.CrestCache;
import lineage2.gameserver.network.serverpackets.AllianceCrest;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestAllyCrest extends L2GameClientPacket
{
	/**
	 * Field _crestId.
	 */
	private int _crestId;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_crestId = readD();
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		if (_crestId == 0)
		{
			return;
		}
		byte[] data = CrestCache.getInstance().getAllyCrest(_crestId);
		if (data != null)
		{
			AllianceCrest ac = new AllianceCrest(_crestId, data);
			sendPacket(ac);
		}
	}
}
