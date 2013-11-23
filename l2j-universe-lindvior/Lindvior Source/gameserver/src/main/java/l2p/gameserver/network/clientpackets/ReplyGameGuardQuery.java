package l2p.gameserver.network.clientpackets;

public class ReplyGameGuardQuery extends L2GameClientPacket {
    // Format: cdddd

    public byte[] _data = new byte[84];

    @Override
    protected void readImpl() {
        l2p.gameserver.ccpGuard.Protection.doReadReplyGameGuard(getClient(), _buf, _data);
    }

    @Override
    protected void runImpl() {
        if (getClient() != null) {
            l2p.gameserver.ccpGuard.Protection.doReplyGameGuard(getClient(), _data);
        }
    }
}
