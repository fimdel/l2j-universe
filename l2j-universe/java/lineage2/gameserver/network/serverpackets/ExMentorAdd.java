package lineage2.gameserver.network.serverpackets;

import lineage2.gameserver.model.Player;

public class ExMentorAdd extends L2GameServerPacket {
    private String _newMentorName;
    private int _newMentorClassId, _newMentorLvl;

    public ExMentorAdd(Player newMentor) {
        _newMentorName = newMentor.getName();
        _newMentorClassId = newMentor.getClassId().getId();
        _newMentorLvl = newMentor.getLevel();
    }

    @Override
    protected final void writeImpl() {
        writeEx(0x122);
        writeS(_newMentorName);
        writeD(_newMentorClassId);
        writeD(_newMentorLvl);
    }
}
