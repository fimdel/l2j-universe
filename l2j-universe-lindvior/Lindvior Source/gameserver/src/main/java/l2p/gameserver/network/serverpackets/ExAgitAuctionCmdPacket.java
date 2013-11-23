/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets;

public class ExAgitAuctionCmdPacket extends L2GameServerPacket {
    //0xfe:0xd1 ExAgitAuctionCmdPacket

    @Override
    protected void writeImpl() {
        writeEx(0xD2);
    }
}
