package l2p.gameserver.network.serverpackets;

@Deprecated
public class TradePressOwnOk extends L2GameServerPacket {
    @Override
    protected final void writeImpl() {
        writeC(0x53);
    }
}