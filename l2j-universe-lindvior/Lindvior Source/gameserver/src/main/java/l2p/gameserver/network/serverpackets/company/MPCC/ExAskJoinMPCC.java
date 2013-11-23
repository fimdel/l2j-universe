/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets.company.MPCC;

import l2p.gameserver.network.serverpackets.L2GameServerPacket;

/**
 * Asks the player to join a Command Channel
 */
public class ExAskJoinMPCC extends L2GameServerPacket {
    private String _requestorName;

    /**
     * @param requestorName Name of CCLeader
     */
    public ExAskJoinMPCC(String requestorName) {
        _requestorName = requestorName;
    }

    @Override
    protected void writeImpl() {
        writeEx(0x1a);
        writeS(_requestorName); // лидер CC
        writeD(0x00);
    }
}