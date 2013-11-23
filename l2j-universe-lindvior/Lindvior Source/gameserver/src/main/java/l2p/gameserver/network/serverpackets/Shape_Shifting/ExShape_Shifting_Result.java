/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets.Shape_Shifting;

import l2p.gameserver.network.serverpackets.L2GameServerPacket;

public class ExShape_Shifting_Result extends L2GameServerPacket {
    public static L2GameServerPacket FAIL = new ExShape_Shifting_Result(0x00, 0, 0);
    public static int SUCCESS_RESULT = 0x01;

    private final int _result;
    private final int _targetItemId;
    private final int _extractItemId;

    public ExShape_Shifting_Result(int result, int targetItemId, int extractItemId) {
        _result = result;
        _targetItemId = targetItemId;
        _extractItemId = extractItemId;
    }

    @Override
    protected void writeImpl() {
        writeEx(0x132);
        writeD(_result); //Result
        writeD(_targetItemId);
        writeD(_extractItemId);
    }
}
