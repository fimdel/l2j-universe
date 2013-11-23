package l2p.gameserver.network.clientpackets;

public class RequestSEKCustom extends L2GameClientPacket {
    @SuppressWarnings("unused")
    private int SlotNum, Direction;

    /**
     * format: dd
     */
    @Override
    protected void readImpl() {
        SlotNum = readD();
        Direction = readD();
    }

    @Override
    protected void runImpl() {
        //TODO not implemented
    }
}
