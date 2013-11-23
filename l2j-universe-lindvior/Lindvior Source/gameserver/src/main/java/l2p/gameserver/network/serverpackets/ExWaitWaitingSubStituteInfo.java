package l2p.gameserver.network.serverpackets;

public class ExWaitWaitingSubStituteInfo extends L2GameServerPacket {

    public static final int WAITING_CANCEL = 0;
    public static final int WAITING_OK = 1;

    private int _open;

    public ExWaitWaitingSubStituteInfo(int open) {
        _open = open;
    }

    protected void writeImpl() {
        writeEx449(0x103);
        writeD(_open);
    }
}