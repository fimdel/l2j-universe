/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets.company.pledge;

import l2p.gameserver.network.serverpackets.L2GameServerPacket;

public class PledgeShowMemberListDelete extends L2GameServerPacket {
    private String _player;

    public PledgeShowMemberListDelete(String playerName) {
        _player = playerName;
    }

    @Override
    protected final void writeImpl() {
        writeC(0x5d);
        writeS(_player);
    }
}