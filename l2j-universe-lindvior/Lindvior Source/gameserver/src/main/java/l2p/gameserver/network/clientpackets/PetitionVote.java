package l2p.gameserver.network.clientpackets;

/**
 * format: ddS
 */
public class PetitionVote extends L2GameClientPacket {
    @SuppressWarnings("unused")
    private int _type, _unk1;
    @SuppressWarnings("unused")
    private String _petitionText;

    @Override
    protected void runImpl() {
    }

    @Override
    protected void readImpl() {
        _type = readD();
        _unk1 = readD(); // possible always zero
        _petitionText = readS(4096);
        // not done
    }
}
