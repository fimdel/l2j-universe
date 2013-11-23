/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets.company.pledge;

import l2p.gameserver.network.serverpackets.L2GameServerPacket;

public class PledgeShowMemberListDeleteAll extends L2GameServerPacket {
    public static final L2GameServerPacket STATIC = new PledgeShowMemberListDeleteAll();

    @Override
    protected final void writeImpl() {
        writeC(0x88);
    }
}