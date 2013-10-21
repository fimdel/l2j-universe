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

import lineage2.gameserver.data.BoatHolder;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.boat.Boat;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestMoveToLocationInShuttle extends L2GameClientPacket
{
	/**
	 * Field _pos.
	 */
	private final Location _pos = new Location();
	/**
	 * Field _originPos.
	 */
	private final Location _originPos = new Location();
	/**
	 * Field _shuttleId.
	 */
	private int _shuttleId;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_shuttleId = readD();
		_pos.x = readD();
		_pos.y = readD();
		_pos.z = readD();
		_originPos.x = readD();
		_originPos.y = readD();
		_originPos.z = readD();
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		Player player = getClient().getActiveChar();
		if (player == null)
		{
			return;
		}
		Boat boat = BoatHolder.getInstance().getBoat(_shuttleId);
		if (boat == null)
		{
			player.sendActionFailed();
			return;
		}
		boat.moveInBoat(player, _originPos, _pos);
	}
}
