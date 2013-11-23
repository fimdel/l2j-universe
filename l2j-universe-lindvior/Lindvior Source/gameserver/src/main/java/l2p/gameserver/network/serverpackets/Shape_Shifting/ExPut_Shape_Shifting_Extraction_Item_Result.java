/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets.Shape_Shifting;

import l2p.gameserver.network.serverpackets.L2GameServerPacket;

public class ExPut_Shape_Shifting_Extraction_Item_Result extends L2GameServerPacket {

    public static L2GameServerPacket FAIL = new ExPut_Shape_Shifting_Extraction_Item_Result(0);
    public static L2GameServerPacket SUCCESS = new ExPut_Shape_Shifting_Extraction_Item_Result(1);
    private final int _result;

    public ExPut_Shape_Shifting_Extraction_Item_Result(int result) {
        _result = result;
    }

    @Override
    protected void writeImpl() {
        writeEx(0x131);
        writeD(_result);
    }
}
