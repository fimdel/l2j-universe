/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package l2p.gameserver.network.serverpackets;

/**
 * Format: (chd)
 */
public class ExCubeGameCloseUI extends L2GameServerPacket {
    int _seconds;

    public ExCubeGameCloseUI() {
    }

    @Override
    protected void writeImpl() {
        writeEx(0x98);
        writeD(0xffffffff);
    }
}