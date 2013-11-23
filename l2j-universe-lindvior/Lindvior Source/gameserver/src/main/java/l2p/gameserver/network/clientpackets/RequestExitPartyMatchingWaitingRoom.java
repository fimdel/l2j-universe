package l2p.gameserver.network.clientpackets;

import l2p.gameserver.instancemanager.MatchingRoomManager;
import l2p.gameserver.model.Player;

/**
 * Format: (ch)
 */
public class RequestExitPartyMatchingWaitingRoom extends L2GameClientPacket {
    @Override
    protected void readImpl() {
    }

    @Override
    protected void runImpl() {
        Player player = getClient().getActiveChar();
        if (player == null)
            return;

        MatchingRoomManager.getInstance().removeFromWaitingList(player);
    }
}