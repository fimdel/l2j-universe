package l2p.gameserver.network.serverpackets;

public class ExRaidReserveResult extends L2GameServerPacket {
    @Override
    protected void writeImpl() {
        writeEx449(0xB6);
        // TODO dx[dddd]
    }
}