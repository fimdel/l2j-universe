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

import lineage2.gameserver.instancemanager.MatchingRoomManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.matching.MatchingRoom;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestPartyMatchDetail extends L2GameClientPacket
{
	/**
	 * Field _roomId.
	 */
	private int _roomId;
	/**
	 * Field _locations.
	 */
	private int _locations;
	/**
	 * Field _level.
	 */
	private int _level;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_roomId = readD();
		_locations = readD();
		_level = readD();
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
		if (player.getMatchingRoom() != null)
		{
			return;
		}
		if (_roomId > 0)
		{
			MatchingRoom room = MatchingRoomManager.getInstance().getMatchingRoom(MatchingRoom.PARTY_MATCHING, _roomId);
			if (room == null)
			{
				return;
			}
			room.addMember(player);
		}
		else
		{
			for (MatchingRoom room : MatchingRoomManager.getInstance().getMatchingRooms(MatchingRoom.PARTY_MATCHING, _locations, _level == 1, player))
			{
				if (room.addMember(player))
				{
					break;
				}
			}
		}
	}
}
