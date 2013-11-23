/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package l2p.gameserver.network.serverpackets;

public class ExDuelEnemyRelation extends L2GameServerPacket {
    @Override
    protected void writeImpl() {
        writeEx(0x5A);
        // just trigger
    }
}