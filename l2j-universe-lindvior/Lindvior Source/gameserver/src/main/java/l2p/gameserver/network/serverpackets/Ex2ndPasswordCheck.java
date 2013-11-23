/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets;

/**
 * Format (ch)dd
 * d: window type
 * d: ban user (1)
 */
public class Ex2ndPasswordCheck extends L2GameServerPacket {
    public static final int PASSWORD_NEW = 0x00;
    public static final int PASSWORD_PROMPT = 0x01;
    public static final int PASSWORD_OK = 0x02;

    private int _windowType;

    public Ex2ndPasswordCheck(int windowType) {
        _windowType = windowType;
    }

    @Override
    protected void writeImpl() {
        writeEx(0x10A);
        writeD(_windowType);
        writeD(0x00);
    }
}
