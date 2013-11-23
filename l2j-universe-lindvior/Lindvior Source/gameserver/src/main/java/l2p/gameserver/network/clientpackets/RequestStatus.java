package l2p.gameserver.network.clientpackets;

import l2p.gameserver.network.serverpackets.SendStatus;

public final class RequestStatus extends L2GameClientPacket {
    @Override
    protected void readImpl() {
    }

    @Override
    protected void runImpl() {
        getClient().close(new SendStatus());
    }
}