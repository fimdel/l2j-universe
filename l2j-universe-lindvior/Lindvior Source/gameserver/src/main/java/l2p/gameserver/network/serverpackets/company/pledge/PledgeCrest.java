/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets.company.pledge;

import l2p.gameserver.network.serverpackets.L2GameServerPacket;

public class PledgeCrest extends L2GameServerPacket {
    private int _crestId;
    private int _crestSize;
    private byte[] _data;

    public PledgeCrest(int crestId, byte[] data) {
        _crestId = crestId;
        _data = data;
        _crestSize = _data.length;
    }

    @Override
    protected final void writeImpl() {
        writeC(0x6a);
        writeD(_crestId);
        writeD(_crestSize);
        writeB(_data);
    }
}