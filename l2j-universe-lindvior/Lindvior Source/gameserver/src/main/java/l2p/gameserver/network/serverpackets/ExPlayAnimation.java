package l2p.gameserver.network.serverpackets;

public class ExPlayAnimation extends L2GameServerPacket {
    @Override
    protected void writeImpl() {
        writeEx449(0x5A);
        // TODO dcdS
    }
}