/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package l2p.gameserver.network.serverpackets;

/**
 * Format: (chd)
 */
public class ExCubeGameRequestReady extends L2GameServerPacket {
    @Override
    protected void writeImpl() {
        writeEx(0x98);
        writeD(0x04);
    }
}