/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets.PledgeRecruit;

import l2p.gameserver.network.serverpackets.L2GameServerPacket;

public class ExPledgeRecruitBoardDetail extends L2GameServerPacket {
    @Override
    protected void writeImpl() {
        writeEx(0x14C);
        writeD(0);
        writeD(0);
        writeS("");
        writeS("");
    }
}
