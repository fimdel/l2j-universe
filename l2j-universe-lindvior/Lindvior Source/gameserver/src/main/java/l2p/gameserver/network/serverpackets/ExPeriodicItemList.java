package l2p.gameserver.network.serverpackets;

public class ExPeriodicItemList extends L2GameServerPacket {
    @Override
    protected void writeImpl() {
        writeEx449(0x87);
        writeD(0); // count of dd
    }
}