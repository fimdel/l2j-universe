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
package lineage2.gameserver.model.matching;

import lineage2.gameserver.model.CommandChannel;
import lineage2.gameserver.model.Party;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.serverpackets.ExDissmissMpccRoom;
import lineage2.gameserver.network.serverpackets.ExManageMpccRoomMember;
import lineage2.gameserver.network.serverpackets.ExMpccRoomInfo;
import lineage2.gameserver.network.serverpackets.ExMpccRoomMember;
import lineage2.gameserver.network.serverpackets.L2GameServerPacket;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class CCMatchingRoom extends MatchingRoom
{
	/**
	 * Constructor for CCMatchingRoom.
	 * @param leader Player
	 * @param minLevel int
	 * @param maxLevel int
	 * @param maxMemberSize int
	 * @param lootType int
	 * @param topic String
	 */
	public CCMatchingRoom(Player leader, int minLevel, int maxLevel, int maxMemberSize, int lootType, String topic)
	{
		super(leader, minLevel, maxLevel, maxMemberSize, lootType, topic);
		
		leader.sendPacket(SystemMsg.THE_COMMAND_CHANNEL_MATCHING_ROOM_WAS_CREATED);
	}
	
	/**
	 * Method notValidMessage.
	 * @return SystemMsg
	 */
	@Override
	public SystemMsg notValidMessage()
	{
		return SystemMsg.YOU_CANNOT_ENTER_THE_COMMAND_CHANNEL_MATCHING_ROOM_BECAUSE_YOU_DO_NOT_MEET_THE_REQUIREMENTS;
	}
	
	/**
	 * Method enterMessage.
	 * @return SystemMsg
	 */
	@Override
	public SystemMsg enterMessage()
	{
		return SystemMsg.C1_ENTERED_THE_COMMAND_CHANNEL_MATCHING_ROOM;
	}
	
	/**
	 * Method exitMessage.
	 * @param toOthers boolean
	 * @param kick boolean
	 * @return SystemMsg
	 */
	@Override
	public SystemMsg exitMessage(boolean toOthers, boolean kick)
	{
		if (!toOthers)
		{
			return kick ? SystemMsg.YOU_WERE_EXPELLED_FROM_THE_COMMAND_CHANNEL_MATCHING_ROOM : SystemMsg.YOU_EXITED_FROM_THE_COMMAND_CHANNEL_MATCHING_ROOM;
		}
		return null;
	}
	
	/**
	 * Method closeRoomMessage.
	 * @return SystemMsg
	 */
	@Override
	public SystemMsg closeRoomMessage()
	{
		return SystemMsg.THE_COMMAND_CHANNEL_MATCHING_ROOM_WAS_CANCELLED;
	}
	
	/**
	 * Method closeRoomPacket.
	 * @return L2GameServerPacket
	 */
	@Override
	public L2GameServerPacket closeRoomPacket()
	{
		return ExDissmissMpccRoom.STATIC;
	}
	
	/**
	 * Method infoRoomPacket.
	 * @return L2GameServerPacket
	 */
	@Override
	public L2GameServerPacket infoRoomPacket()
	{
		return new ExMpccRoomInfo(this);
	}
	
	/**
	 * Method addMemberPacket.
	 * @param $member Player
	 * @param active Player
	 * @return L2GameServerPacket
	 */
	@Override
	public L2GameServerPacket addMemberPacket(Player $member, Player active)
	{
		return new ExManageMpccRoomMember(ExManageMpccRoomMember.ADD_MEMBER, this, active);
	}
	
	/**
	 * Method removeMemberPacket.
	 * @param $member Player
	 * @param active Player
	 * @return L2GameServerPacket
	 */
	@Override
	public L2GameServerPacket removeMemberPacket(Player $member, Player active)
	{
		return new ExManageMpccRoomMember(ExManageMpccRoomMember.REMOVE_MEMBER, this, active);
	}
	
	/**
	 * Method updateMemberPacket.
	 * @param $member Player
	 * @param active Player
	 * @return L2GameServerPacket
	 */
	@Override
	public L2GameServerPacket updateMemberPacket(Player $member, Player active)
	{
		return new ExManageMpccRoomMember(ExManageMpccRoomMember.UPDATE_MEMBER, this, active);
	}
	
	/**
	 * Method membersPacket.
	 * @param active Player
	 * @return L2GameServerPacket
	 */
	@Override
	public L2GameServerPacket membersPacket(Player active)
	{
		return new ExMpccRoomMember(this, active);
	}
	
	/**
	 * Method getType.
	 * @return int
	 */
	@Override
	public int getType()
	{
		return CC_MATCHING;
	}
	
	/**
	 * Method disband.
	 */
	@Override
	public void disband()
	{
		Party party = _leader.getParty();
		if (party != null)
		{
			CommandChannel commandChannel = party.getCommandChannel();
			if (commandChannel != null)
			{
				commandChannel.setMatchingRoom(null);
			}
		}
		
		super.disband();
	}
	
	/**
	 * Method getMemberType.
	 * @param member Player
	 * @return int
	 */
	@Override
	public int getMemberType(Player member)
	{
		Party party = _leader.getParty();
		CommandChannel commandChannel = party.getCommandChannel();
		if (member == _leader)
		{
			return MatchingRoom.UNION_LEADER;
		}
		else if (member.getParty() == null)
		{
			return MatchingRoom.WAIT_NORMAL;
		}
		else if ((member.getParty() == party) || commandChannel.getParties().contains(member.getParty()))
		{
			return MatchingRoom.UNION_PARTY;
		}
		else if (member.getParty() != null)
		{
			return MatchingRoom.WAIT_PARTY;
		}
		else
		{
			return MatchingRoom.WAIT_NORMAL;
		}
	}
}
