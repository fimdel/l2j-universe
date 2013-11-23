package l2p.gameserver.network.clientpackets;

import l2p.gameserver.model.Player;

/**
 * Created with IntelliJ IDEA.
 * User: Darvin
 * Date: 01.07.12
 * Time: 14:39
 */
public class RequestDeletePartySubstitute extends L2GameClientPacket {

    @Override
    protected void readImpl() {
    }

    @Override
    protected void runImpl() {
        Player player = getClient().getActiveChar();

        if (player == null)
            return;

        System.out.println("RequestDeletePartySubstitute");
    }
}
