package l2p.gameserver.network.serverpackets;

public class ExNotifyBirthDay extends L2GameServerPacket {
    public static final L2GameServerPacket STATIC = new ExNotifyBirthDay();

    @Override
    protected void writeImpl() {
        writeEx449(0x8F);
        writeD(0); // Actor OID
    }
}