/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets.commission;

import l2p.gameserver.network.serverpackets.L2GameServerPacket;

/**
 * @author Bonux
 */
public class ExShowCommission extends L2GameServerPacket {
    public ExShowCommission() {
        //
    }

    @Override
    protected final void writeImpl() {
        writeEx449(0xF1);
        writeD(0x01); // ??Open??
    }
}