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
import lineage2.gameserver.model.pledge.Alliance;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestSetAllyCrest extends L2GameClientPacket
{
	/**
	 * Field _length.
	 */
	private int _length;
	/**
	 * Field _data.
	 */
	private byte[] _data;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_length = readD();
		if ((_length == CrestCache.ALLY_CREST_SIZE) && (_length == _buf.remaining()))
		{
			_data = new byte[_length];
			readB(_data);
		}
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
		Alliance ally = activeChar.getAlliance();
		if ((ally != null) && activeChar.isAllyLeader())
		{
			int crestId = 0;
			if (_data != null)
			{
				crestId = CrestCache.getInstance().saveAllyCrest(ally.getAllyId(), _data);
			}
			else if (ally.hasAllyCrest())
			{
				CrestCache.getInstance().removeAllyCrest(ally.getAllyId());
			}
			ally.setAllyCrestId(crestId);
			ally.broadcastAllyStatus();
		}
	}
}
