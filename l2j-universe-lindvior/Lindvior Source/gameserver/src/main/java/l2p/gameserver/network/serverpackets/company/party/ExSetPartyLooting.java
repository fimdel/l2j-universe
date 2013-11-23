/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets.company.party;

import l2p.gameserver.network.serverpackets.L2GameServerPacket;

public class ExSetPartyLooting extends L2GameServerPacket {
    private int _result;
    private int _mode;

    public ExSetPartyLooting(int result, int mode) {
        _result = result;
        _mode = mode;
    }

    @Override
    protected void writeImpl() {
        writeEx(0xC1);
        writeD(_result);
        writeD(_mode);
    }
}
