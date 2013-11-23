/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets.company.party;

import l2p.gameserver.network.serverpackets.L2GameServerPacket;

public class ExAskModifyPartyLooting extends L2GameServerPacket {
    private String _requestor;
    private int _mode;

    public ExAskModifyPartyLooting(String name, int mode) {
        _requestor = name;
        _mode = mode;
    }

    @Override
    protected void writeImpl() {
        writeEx449(0xBF);
        writeS(_requestor);
        writeD(_mode);
    }
}
