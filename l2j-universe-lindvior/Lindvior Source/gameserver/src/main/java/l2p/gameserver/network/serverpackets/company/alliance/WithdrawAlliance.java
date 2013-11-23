/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets.company.alliance;

import l2p.gameserver.network.serverpackets.L2GameServerPacket;

public class WithdrawAlliance extends L2GameServerPacket {

    @Override
    protected final void writeImpl() {
        writeC(0xAB);
        //TODO dSddddS dd
    }
}