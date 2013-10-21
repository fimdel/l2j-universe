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
import lineage2.gameserver.model.matching.MatchingRoom;
import lineage2.gameserver.model.matching.PartyMatchingRoom;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestPartyMatchList extends L2GameClientPacket
{
	/**
	 * Field _lootDist.
	 */
	private int _lootDist;
	/**
	 * Field _maxMembers.
	 */
	private int _maxMembers;
	/**
	 * Field _minLevel.
	 */
	private int _minLevel;
	/**
	 * Field _maxLevel.
	 */
	private int _maxLevel;
	/**
	 * Field _roomId.
	 */
	private int _roomId;
	/**
	 * Field _roomTitle.
	 */
	private String _roomTitle;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_roomId = readD();
		_maxMembers = readD();
		_minLevel = readD();
		_maxLevel = readD();
		_lootDist = readD();
		_roomTitle = readS(64);
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
		MatchingRoom room = player.getMatchingRoom();
		if (room == null)
		{
			new PartyMatchingRoom(player, _minLevel, _maxLevel, _maxMembers, _lootDist, _roomTitle);
		}
		else if ((room.getId() == _roomId) && (room.getType() == MatchingRoom.PARTY_MATCHING) && (room.getLeader() == player))
		{
			room.setMinLevel(_minLevel);
			room.setMaxLevel(_maxLevel);
			room.setMaxMemberSize(_maxMembers);
			room.setTopic(_roomTitle);
			room.setLootType(_lootDist);
			room.broadCast(room.infoRoomPacket());
		}
	}
}
