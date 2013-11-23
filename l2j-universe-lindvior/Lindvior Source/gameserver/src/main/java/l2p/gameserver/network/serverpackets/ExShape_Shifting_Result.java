/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets;

public class ExShape_Shifting_Result extends L2GameServerPacket {
    public static ExShape_Shifting_Result CANCEL = new ExShape_Shifting_Result();
    private int _result;
    private int _targetId = 0;
    private int _supportId = 0;

    public ExShape_Shifting_Result() {
        _result = 0;
    }

    public ExShape_Shifting_Result(int targetId, int supportId) {
        _result = 1;
        _targetId = targetId;
        _supportId = supportId;
    }

    protected void writeImpl() {
        writeEx(0x131);

        writeD(_result);

        writeD(_targetId);
        writeD(_supportId);
    }
}
