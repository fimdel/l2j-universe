/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package l2p.gameserver.network.serverpackets;

public class ExTacticalSign extends L2GameServerPacket {
    public static final int STAR = 1;
    public static final int HEART = 2;
    public static final int MOON = 3;
    public static final int CROSS = 4;

    private int _targetId;
    private int _signId;

    public ExTacticalSign(int target, int sign) {
        _targetId = target;
        _signId = sign;
    }

    @Override
    protected final void writeImpl() {
        writeEx(0x100);
        writeD(_targetId);
        writeD(_signId);
    }
}
