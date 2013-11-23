package l2p.gameserver.network.clientpackets;

public class RequestExChangeName extends L2GameClientPacket {
    @SuppressWarnings("unused")
    @Override
    protected void readImpl() {
        int unk1 = readD();
        String name = readS();
        int unk2 = readD();
    }

    @Override
    protected void runImpl() {
        //TODO not implemented
    }
}
