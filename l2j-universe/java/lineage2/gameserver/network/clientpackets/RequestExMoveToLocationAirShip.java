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

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.boat.ClanAirShip;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestExMoveToLocationAirShip extends L2GameClientPacket
{
	/**
	 * Field _moveType.
	 */
	private int _moveType;
	/**
	 * Field _param2. Field _param1.
	 */
	private int _param1, _param2;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_moveType = readD();
		switch (_moveType)
		{
			case 4:
				_param1 = readD() + 1;
				break;
			case 0:
				_param1 = readD();
				_param2 = readD();
				break;
			case 2:
				readD();
				readD();
				break;
			case 3:
				readD();
				readD();
				break;
		}
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		Player player = getClient().getActiveChar();
		if ((player == null) || (player.getBoat() == null) || !player.getBoat().isClanAirShip())
		{
			return;
		}
		ClanAirShip airship = (ClanAirShip) player.getBoat();
		if (airship.getDriver() == player)
		{
			switch (_moveType)
			{
				case 4:
					airship.addTeleportPoint(player, _param1);
					break;
				case 0:
					if (!airship.isCustomMove())
					{
						break;
					}
					airship.moveToLocation(airship.getLoc().setX(_param1).setY(_param2), 0, false);
					break;
				case 2:
					if (!airship.isCustomMove())
					{
						break;
					}
					airship.moveToLocation(airship.getLoc().changeZ(100), 0, false);
					break;
				case 3:
					if (!airship.isCustomMove())
					{
						break;
					}
					airship.moveToLocation(airship.getLoc().changeZ(-100), 0, false);
					break;
			}
		}
	}
}
