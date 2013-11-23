package l2p.gameserver.network.serverpackets;

import l2p.gameserver.model.Party;
import l2p.gameserver.model.Player;

/**
 * Created with IntelliJ IDEA.
 * User: Darvin
 * Date: 01.07.12
 * Time: 11:57
 * запрос выбранному чару на вступление в пати
 */
/*public class ExRegistWaitingSubstituteOk extends L2GameServerPacket {
    int classId;
    Player player;

    public ExRegistWaitingSubstituteOk(int cId, Player _player) {
        classId = cId;
        player = _player.getParty().getPartyLeader();
    }

    @Override
    protected void writeImpl() {
        writeC(0xFE);
        writeH(0x104);
        writeC(0x00); // TODO: Unknown
        writeC(0x00); // TODO: Unknown
        writeD(0x00);// чёт с регионом связано
        writeC(0x00); // TODO: Unknown
        writeC(0x00); // TODO: Unknown
        writeD(0x00); // TODO: Unknown
        writeD(0x01);
        writeD(classId);
    }
}*/
public class ExRegistWaitingSubstituteOk extends L2GameServerPacket {
    private int x, y, z;
    private int classId;

    public ExRegistWaitingSubstituteOk(Party _party, Player _player) {
        Player leader = _party.getPartyLeader();
        x = leader.getX();
        y = leader.getY();
        z = leader.getZ();
        classId = _player.getClassId().getId();
    }

    @Override
    protected void writeImpl() {
        writeEx(0x104);
        writeD(x); // x
        writeD(y); // y
        writeD(z); // z
        writeD(0x00); // unk
        writeD(classId); // ClassId
        writeD(0x00); // unk1
        writeD(0x00); // unk2
    }
}
