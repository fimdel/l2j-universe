package l2p.gameserver.network.clientpackets;

public class RequestChangeBookMarkSlot extends L2GameClientPacket {
    @SuppressWarnings("unused")
    private int slot_old, slot_new;

    @Override
    protected void readImpl() {
        slot_old = readD();
        slot_new = readD();
    }

    @Override
    protected void runImpl() {
        //TODO not implemented
    }
}
