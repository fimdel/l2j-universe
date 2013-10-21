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
import lineage2.gameserver.network.serverpackets.components.SystemMsg;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestExManageMpccRoom extends L2GameClientPacket
{
	/**
	 * Field _id.
	 */
	private int _id;
	/**
	 * Field _memberSize.
	 */
	private int _memberSize;
	/**
	 * Field _minLevel.
	 */
	private int _minLevel;
	/**
	 * Field _maxLevel.
	 */
	private int _maxLevel;
	/**
	 * Field _topic.
	 */
	private String _topic;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_id = readD();
		_memberSize = readD();
		_minLevel = readD();
		_maxLevel = readD();
		readD();
		_topic = readS();
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
		if ((room == null) || (room.getId() != _id) || (room.getType() != MatchingRoom.CC_MATCHING))
		{
			return;
		}
		if (room.getLeader() != player)
		{
			return;
		}
		room.setTopic(_topic);
		room.setMaxMemberSize(_memberSize);
		room.setMinLevel(_minLevel);
		room.setMaxLevel(_maxLevel);
		room.broadCast(room.infoRoomPacket());
		player.sendPacket(SystemMsg.THE_COMMAND_CHANNEL_MATCHING_ROOM_INFORMATION_WAS_EDITED);
	}
}
