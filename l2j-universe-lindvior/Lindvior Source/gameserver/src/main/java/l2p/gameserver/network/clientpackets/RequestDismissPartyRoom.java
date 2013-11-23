package l2p.gameserver.network.clientpackets;

import l2p.gameserver.model.Player;
import l2p.gameserver.model.matching.MatchingRoom;

/**
 * Format: (ch) dd
 */
public class RequestDismissPartyRoom extends L2GameClientPacket {
    private int _roomId;

    @Override
    protected void readImpl() {
        _roomId = readD(); //room id
    }

    @Override
    protected void runImpl() {
        Player player = getClient().getActiveChar();
        if (player == null)
            return;

        MatchingRoom room = player.getMatchingRoom();
        if (room.getId() != _roomId || room.getType() != MatchingRoom.PARTY_MATCHING)
            return;

        if (room.getLeader() != player)
            return;

        room.disband();
    }
}