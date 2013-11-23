/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets.Shape_Shifting;

import l2p.gameserver.network.serverpackets.L2GameServerPacket;

public class ExPut_Shape_Shifting_Target_Item_Result extends L2GameServerPacket {
    public static L2GameServerPacket FAIL = new ExPut_Shape_Shifting_Target_Item_Result(0, 0L);

    public static int SUCCESS_RESULT = 1;
    private final int _result;
    private final long _price;

    public ExPut_Shape_Shifting_Target_Item_Result(int resultId, long price) {
        this._result = resultId;
        this._price = price;
    }

    @Override
    protected void writeImpl() {
        writeEx(0x130);
        writeD(_result);
        writeQ(_price);
    }
}