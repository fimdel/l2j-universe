/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets.company.MPCC;

import l2p.gameserver.network.serverpackets.L2GameServerPacket;

/**
 * Opens the CommandChannel Information window
 */
public class ExMPCCOpen extends L2GameServerPacket {
    public static final L2GameServerPacket STATIC = new ExMPCCOpen();

    @Override
    protected void writeImpl() {
        writeEx(0x12);
    }
}