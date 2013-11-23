package l2p.gameserver.network.serverpackets;

public class ExNotifyPremiumItem extends L2GameServerPacket {
    public static final L2GameServerPacket STATIC = new ExNotifyPremiumItem();

    @Override
    protected void writeImpl() {
        writeEx449(0x85);
    }
}