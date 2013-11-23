/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets.commission;

import l2p.gameserver.network.serverpackets.L2GameServerPacket;

public class ExPutCommissionResultForVariationMake extends L2GameServerPacket {
    private int _gemstoneObjId, _unk1, _unk3;
    private long _gemstoneCount, _unk2;

    public ExPutCommissionResultForVariationMake(int gemstoneObjId, long count) {
        _gemstoneObjId = gemstoneObjId;
        _unk1 = 1;
        _gemstoneCount = count;
        _unk2 = 1;
        _unk3 = 1;
    }

    @Override
    protected void writeImpl() {
        writeEx449(0x55);
        writeD(_gemstoneObjId);
        writeD(_unk1);
        writeQ(_gemstoneCount);
        writeQ(_unk2);
        writeD(_unk3);
    }
}