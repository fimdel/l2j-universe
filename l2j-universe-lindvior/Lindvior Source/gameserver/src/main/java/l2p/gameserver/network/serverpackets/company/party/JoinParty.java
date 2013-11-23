/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets.company.party;

import l2p.gameserver.network.serverpackets.L2GameServerPacket;

public class JoinParty extends L2GameServerPacket {
    public static final L2GameServerPacket SUCCESS = new JoinParty(1);
    public static final L2GameServerPacket FAIL = new JoinParty(0);

    private int _response;

    public JoinParty(int response) {
        _response = response;
    }

    @Override
    protected final void writeImpl() {
        writeC(0x3A);
        writeD(_response);
    }
}