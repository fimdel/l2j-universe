package l2p.gameserver.network.clientpackets;

import l2p.gameserver.model.Player;
import l2p.gameserver.model.entity.events.impl.KrateisCubeEvent;

/**
 * @author VISTALL
 */
public class RequestExStartShowCrataeCubeRank extends L2GameClientPacket {
    @Override
    protected void readImpl() {
        //
    }

    @Override
    protected void runImpl() {
        Player player = getClient().getActiveChar();
        if (player == null)
            return;

        KrateisCubeEvent cubeEvent = player.getEvent(KrateisCubeEvent.class);
        if (cubeEvent == null)
            return;

        cubeEvent.showRank(player);
    }
}