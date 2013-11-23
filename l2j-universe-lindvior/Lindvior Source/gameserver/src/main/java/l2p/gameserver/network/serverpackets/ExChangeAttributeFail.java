/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package l2p.gameserver.network.serverpackets;

public class ExChangeAttributeFail extends L2GameServerPacket {
    public static final L2GameServerPacket STATIC = new ExChangeAttributeFail();

    public ExChangeAttributeFail() {
        //
    }

    protected void writeImpl() {
        writeEx(0x11B);
    }
}