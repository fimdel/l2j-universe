package l2p.gameserver.network.clientpackets;

import l2p.gameserver.model.Player;

/**
 * Created with IntelliJ IDEA.
 * User: Darvin
 * Date: 09.06.12
 * Time: 19:30
 */
public class RequestFirstPlayStart extends L2GameClientPacket {
    @Override
    protected void readImpl() {
        //
    }

    @Override
    protected void runImpl() {
        Player player = getClient().getActiveChar();
        if (player == null)
            return;
    }
}
