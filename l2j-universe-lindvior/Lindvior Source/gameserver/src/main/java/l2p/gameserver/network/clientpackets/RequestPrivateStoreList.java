package l2p.gameserver.network.clientpackets;

public class RequestPrivateStoreList extends L2GameClientPacket {
    @SuppressWarnings("unused")
    private int unk;

    /**
     * format: d
     */
    @Override
    protected void readImpl() {
        unk = readD();
    }

    @Override
    protected void runImpl() {
        //TODO not implemented
    }
}
