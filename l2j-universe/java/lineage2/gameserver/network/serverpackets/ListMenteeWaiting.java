package lineage2.gameserver.network.serverpackets;

public class ListMenteeWaiting extends L2GameServerPacket {
    @Override
    protected void writeImpl() {
        writeEx(0x122);
    }
}
