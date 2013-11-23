/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets.company.party;

import l2p.gameserver.network.serverpackets.L2GameServerPacket;

public class ExPartyMemberRenamed extends L2GameServerPacket {
    @Override
    protected void writeImpl() {
        writeEx(0xA7);
        // TODO ddd
    }
}