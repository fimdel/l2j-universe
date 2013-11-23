package l2p.gameserver.network.serverpackets;

import l2p.gameserver.model.CommandChannel;
import l2p.gameserver.model.Party;
import l2p.gameserver.model.Player;

import java.util.ArrayList;
import java.util.List;

public class ExMultiPartyCommandChannelInfo extends L2GameServerPacket {
    private String ChannelLeaderName;
    private int MemberCount;
    private List<ChannelPartyInfo> parties;

    public ExMultiPartyCommandChannelInfo(CommandChannel channel) {
        ChannelLeaderName = channel.getChannelLeader().getName();
        MemberCount = channel.getMemberCount();

        parties = new ArrayList<ChannelPartyInfo>();
        for (Party party : channel.getParties()) {
            Player leader = party.getPartyLeader();
            if (leader != null)
                parties.add(new ChannelPartyInfo(leader.getName(), leader.getObjectId(), party.getMemberCount()));
        }
    }

    @Override
    protected void writeImpl() {
        writeEx(0x31);
        writeS(ChannelLeaderName); // имя лидера CC
        writeD(0); // Looting type?
        writeD(MemberCount); // общее число человек в СС
        writeD(parties.size()); // общее число партий в СС

        for (ChannelPartyInfo party : parties) {
            writeS(party.Leader_name); // имя лидера партии
            writeD(party.Leader_obj_id); // ObjId пати лидера
            writeD(party.MemberCount); // количество мемберов в пати
        }
    }

    static class ChannelPartyInfo {
        public String Leader_name;
        public int Leader_obj_id, MemberCount;

        public ChannelPartyInfo(String _Leader_name, int _Leader_obj_id, int _MemberCount) {
            Leader_name = _Leader_name;
            Leader_obj_id = _Leader_obj_id;
            MemberCount = _MemberCount;
        }
    }
}