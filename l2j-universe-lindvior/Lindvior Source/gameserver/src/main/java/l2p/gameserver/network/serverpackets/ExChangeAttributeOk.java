/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package l2p.gameserver.network.serverpackets;

public class ExChangeAttributeOk extends L2GameServerPacket {
    public static final L2GameServerPacket STATIC = new ExChangeAttributeOk();

    public ExChangeAttributeOk() {
        //
    }

    protected void writeImpl() {
        writeEx(0x11A);
    }
}