package l2p.gameserver.network.serverpackets;

public class ExRaidCharSelected extends L2GameServerPacket {
    @Override
    protected void writeImpl() {
        writeEx449(0xB5);
        // just a trigger
    }
}