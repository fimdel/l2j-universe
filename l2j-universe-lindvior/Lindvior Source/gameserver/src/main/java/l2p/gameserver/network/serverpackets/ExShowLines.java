package l2p.gameserver.network.serverpackets;

public class ExShowLines extends L2GameServerPacket {
    @Override
    protected void writeImpl() {
        writeEx449(0xA5);
        // TODO hdcc cx[ddd]
    }
}