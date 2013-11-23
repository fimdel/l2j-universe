/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets.company.pledge;

import l2p.gameserver.network.serverpackets.L2GameServerPacket;

public class AskJoinPledge extends L2GameServerPacket {
    private int _requestorId;
    private String _pledgeName;

    public AskJoinPledge(int requestorId, String pledgeName) {
        _requestorId = requestorId;
        _pledgeName = pledgeName;
    }

    @Override
    protected final void writeImpl() {
        writeC(0x2c);
        writeD(_requestorId);
        writeS(_pledgeName);
    }
}