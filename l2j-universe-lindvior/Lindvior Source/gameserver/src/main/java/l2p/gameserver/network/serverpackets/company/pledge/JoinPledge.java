/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets.company.pledge;

import l2p.gameserver.network.serverpackets.L2GameServerPacket;

public class JoinPledge extends L2GameServerPacket {
    private int _pledgeId;

    public JoinPledge(int pledgeId) {
        _pledgeId = pledgeId;
    }

    @Override
    protected final void writeImpl() {
        writeC(0x2d);

        writeD(_pledgeId);
    }
}