/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets.company.MPCC;

import l2p.gameserver.instancemanager.MatchingRoomManager;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.matching.MatchingRoom;
import l2p.gameserver.network.serverpackets.L2GameServerPacket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author VISTALL
 */
public class ExMpccRoomMember extends L2GameServerPacket {
    private int _type;
    private List<MpccRoomMemberInfo> _members = Collections.emptyList();

    public ExMpccRoomMember(MatchingRoom room, Player player) {
        _type = room.getMemberType(player);
        _members = new ArrayList<MpccRoomMemberInfo>(room.getPlayers().size());

        for (Player member : room.getPlayers())
            _members.add(new MpccRoomMemberInfo(member, room.getMemberType(member)));
    }

    @Override
    public void writeImpl() {
        writeEx(0xA0);
        writeD(_type);
        writeD(_members.size());
        for (MpccRoomMemberInfo member : _members) {
            writeD(member.objectId);
            writeS(member.name);
            writeD(member.level);
            writeD(member.classId);
            writeD(member.location);
            writeD(member.memberType);
        }
    }

    static class MpccRoomMemberInfo {
        public final int objectId;
        public final int classId;
        public final int level;
        public final int location;
        public final int memberType;
        public final String name;

        public MpccRoomMemberInfo(Player member, int type) {
            this.objectId = member.getObjectId();
            this.name = member.getName();
            this.classId = member.getClassId().ordinal();
            this.level = member.getLevel();
            this.location = MatchingRoomManager.getInstance().getLocation(member);
            this.memberType = type;
        }
    }
}