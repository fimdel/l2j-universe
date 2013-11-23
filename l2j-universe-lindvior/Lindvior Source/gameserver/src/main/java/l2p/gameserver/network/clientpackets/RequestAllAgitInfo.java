package l2p.gameserver.network.clientpackets;

import l2p.gameserver.network.serverpackets.ExShowAgitInfo;

public class RequestAllAgitInfo extends L2GameClientPacket {
    @Override
    protected void readImpl() {
    }

    @Override
    protected void runImpl() {
        getClient().getActiveChar().sendPacket(new ExShowAgitInfo());
    }
}