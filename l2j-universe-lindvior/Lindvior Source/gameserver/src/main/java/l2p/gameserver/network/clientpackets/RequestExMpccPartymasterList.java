/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.clientpackets;

import l2p.gameserver.model.Player;
import l2p.gameserver.model.matching.MatchingRoom;
import l2p.gameserver.network.serverpackets.company.MPCC.ExMpccPartymasterList;

import java.util.HashSet;
import java.util.Set;

/**
 * @author VISTALL
 */
public class RequestExMpccPartymasterList extends L2GameClientPacket {
    @Override
    protected void readImpl() {
        //
    }

    @Override
    protected void runImpl() {
        Player player = getClient().getActiveChar();
        if (player == null)
            return;

        MatchingRoom room = player.getMatchingRoom();
        if (room == null || room.getType() != MatchingRoom.CC_MATCHING)
            return;

        Set<String> set = new HashSet<String>();
        for (Player $member : room.getPlayers())
            if ($member.getParty() != null)
                set.add($member.getParty().getPartyLeader().getName());

        player.sendPacket(new ExMpccPartymasterList(set));
    }
}