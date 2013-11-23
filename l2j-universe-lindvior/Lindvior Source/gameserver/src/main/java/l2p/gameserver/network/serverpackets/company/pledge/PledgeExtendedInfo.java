/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets.company.pledge;

import l2p.gameserver.network.serverpackets.L2GameServerPacket;

public class PledgeExtendedInfo extends L2GameServerPacket {

    @Override
    protected final void writeImpl() {
        writeC(0x8A);
        //TODO SddSddddddddSd
    }
}
