package l2p.gameserver.network.serverpackets;

public class ExShowTerritory extends L2GameServerPacket {
    @Override
    protected void writeImpl() {
        writeEx449(0x89);
        // TODO ddd[dd]
    }
}