package l2p.gameserver.network.serverpackets;

public class ExTutorialList extends L2GameServerPacket {
    @Override
    protected void writeImpl() {
        writeEx449(0x6B);
        writeS("");
        writeD(0x00);
        writeD(0x00);
        writeD(0x00);
    }
}
