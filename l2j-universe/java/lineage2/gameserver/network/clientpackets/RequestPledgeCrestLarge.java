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
import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.serverpackets.ExPledgeCrestLarge;

public class RequestPledgeCrestLarge extends L2GameClientPacket
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
		Player activeChar = getClient().getActiveChar();
		if (activeChar == null)
		{
			return;
		}
		if (_crestId == 0)
		{
			return;
		}
		byte[] data = CrestCache.getInstance().getPledgeCrestLarge(_crestId);
		if (data != null)
		{
			for (int i = 0; i <= 4; i++)
			{
				byte[] dest1 = new byte[14336];
				byte[] dest2 = new byte[8320];
				if (i<4)
				{
					System.arraycopy(data, (14336*i), dest1, 0, 14336);
					ExPledgeCrestLarge pcl = new ExPledgeCrestLarge(_crestId, dest1, i);
					sendPacket(pcl);
				}
				else
				{
					System.arraycopy(data, (14336*i), dest2, 0, 8320);
					ExPledgeCrestLarge pcl = new ExPledgeCrestLarge(_crestId, dest2, i);
					sendPacket(pcl);
				}
			}
		}
	}
}
