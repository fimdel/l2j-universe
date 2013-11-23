package l2p.gameserver.network.serverpackets;

public class ExGet24HzSessionID extends L2GameServerPacket {
    protected void writeImpl() {
        writeEx449(0x108);
        //TODO: [Bonux]
    }
}