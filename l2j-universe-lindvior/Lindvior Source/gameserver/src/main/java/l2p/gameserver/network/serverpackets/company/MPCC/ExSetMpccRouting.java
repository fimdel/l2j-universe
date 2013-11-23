/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets.company.MPCC;

import l2p.gameserver.network.serverpackets.L2GameServerPacket;

public class ExSetMpccRouting extends L2GameServerPacket {
    @Override
    protected void writeImpl() {
        writeEx(0x37);
        // TODO d
    }
}