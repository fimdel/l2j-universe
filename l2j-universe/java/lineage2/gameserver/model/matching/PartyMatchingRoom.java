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

import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.serverpackets.ExClosePartyRoom;
import lineage2.gameserver.network.serverpackets.ExPartyRoomMember;
import lineage2.gameserver.network.serverpackets.L2GameServerPacket;
import lineage2.gameserver.network.serverpackets.PartyRoomInfo;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class PartyMatchingRoom extends MatchingRoom
{
	/**
	 * Constructor for PartyMatchingRoom.
	 * @param leader Player
	 * @param minLevel int
	 * @param maxLevel int
	 * @param maxMemberSize int
	 * @param lootType int
	 * @param topic String
	 */
	public PartyMatchingRoom(Player leader, int minLevel, int maxLevel, int maxMemberSize, int lootType, String topic)
	{
		super(leader, minLevel, maxLevel, maxMemberSize, lootType, topic);
		
		leader.broadcastCharInfo();
	}
	
	/**
	 * Method notValidMessage.
	 * @return SystemMsg
	 */
	@Override
	public SystemMsg notValidMessage()
	{
		return SystemMsg.YOU_DO_NOT_MEET_THE_REQUIREMENTS_TO_ENTER_THAT_PARTY_ROOM;
	}
	
	/**
	 * Method enterMessage.
	 * @return SystemMsg
	 */
	@Override
	public SystemMsg enterMessage()
	{
		return SystemMsg.C1_HAS_ENTERED_THE_PARTY_ROOM;
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
		if (toOthers)
		{
			return kick ? SystemMsg.C1_HAS_BEEN_KICKED_FROM_THE_PARTY_ROOM : SystemMsg.C1_HAS_LEFT_THE_PARTY_ROOM;
		}
		return kick ? SystemMsg.YOU_HAVE_BEEN_OUSTED_FROM_THE_PARTY_ROOM : SystemMsg.YOU_HAVE_EXITED_THE_PARTY_ROOM;
	}
	
	/**
	 * Method closeRoomMessage.
	 * @return SystemMsg
	 */
	@Override
	public SystemMsg closeRoomMessage()
	{
		return SystemMsg.THE_PARTY_ROOM_HAS_BEEN_DISBANDED;
	}
	
	/**
	 * Method closeRoomPacket.
	 * @return L2GameServerPacket
	 */
	@Override
	public L2GameServerPacket closeRoomPacket()
	{
		return ExClosePartyRoom.STATIC;
	}
	
	/**
	 * Method infoRoomPacket.
	 * @return L2GameServerPacket
	 */
	@Override
	public L2GameServerPacket infoRoomPacket()
	{
		return new PartyRoomInfo(this);
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
		return membersPacket($member);
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
		return membersPacket($member);
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
		return membersPacket($member);
	}
	
	/**
	 * Method membersPacket.
	 * @param active Player
	 * @return L2GameServerPacket
	 */
	@Override
	public L2GameServerPacket membersPacket(Player active)
	{
		return new ExPartyRoomMember(this, active);
	}
	
	/**
	 * Method getType.
	 * @return int
	 */
	@Override
	public int getType()
	{
		return PARTY_MATCHING;
	}
	
	/**
	 * Method getMemberType.
	 * @param member Player
	 * @return int
	 */
	@Override
	public int getMemberType(Player member)
	{
		return member.equals(_leader) ? ROOM_MASTER : (member.getParty() != null) && (_leader.getParty() == member.getParty()) ? PARTY_MEMBER : WAIT_PLAYER;
	}
}
