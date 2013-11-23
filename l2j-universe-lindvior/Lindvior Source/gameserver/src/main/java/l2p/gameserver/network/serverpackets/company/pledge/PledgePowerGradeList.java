/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets.company.pledge;

import l2p.gameserver.model.pledge.RankPrivs;
import l2p.gameserver.network.serverpackets.L2GameServerPacket;

public class PledgePowerGradeList extends L2GameServerPacket {
    private RankPrivs[] _privs;

    public PledgePowerGradeList(RankPrivs[] privs) {
        _privs = privs;
    }

    @Override
    protected final void writeImpl() {
        writeEx(0x3d);
        writeD(_privs.length);
        for (RankPrivs element : _privs) {
            writeD(element.getRank());
            writeD(element.getParty());
        }
    }
}
