package l2p.gameserver.network.serverpackets;

public class ExResponseFreeServer extends L2GameServerPacket {
    @Override
    protected void writeImpl() {
        writeEx449(0x77);
        // just trigger
    }
}