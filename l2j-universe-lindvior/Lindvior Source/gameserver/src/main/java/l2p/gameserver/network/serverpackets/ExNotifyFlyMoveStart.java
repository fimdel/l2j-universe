package l2p.gameserver.network.serverpackets;

public final class ExNotifyFlyMoveStart extends L2GameServerPacket {
    public static final L2GameServerPacket STATIC = new ExNotifyFlyMoveStart();

    public ExNotifyFlyMoveStart() {
        //trigger
    }

    @Override
    protected void writeImpl() {
        writeEx449(0x114);
    }
}