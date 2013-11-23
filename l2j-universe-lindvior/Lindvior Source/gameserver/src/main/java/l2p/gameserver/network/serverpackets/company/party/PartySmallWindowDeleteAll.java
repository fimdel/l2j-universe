/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets.company.party;

import l2p.gameserver.network.serverpackets.L2GameServerPacket;

public class PartySmallWindowDeleteAll extends L2GameServerPacket {
    public static final L2GameServerPacket STATIC = new PartySmallWindowDeleteAll();

    @Override
    protected final void writeImpl() {
        writeC(0x50);
    }
}