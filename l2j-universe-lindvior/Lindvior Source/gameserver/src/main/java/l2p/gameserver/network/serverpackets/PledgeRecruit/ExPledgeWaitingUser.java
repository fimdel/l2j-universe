/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets.PledgeRecruit;

import l2p.gameserver.network.serverpackets.L2GameServerPacket;

public class ExPledgeWaitingUser extends L2GameServerPacket {
    @Override
    protected void writeImpl() {
        writeEx(0x14F);
        writeD(0);
        writeS("");
    }
}