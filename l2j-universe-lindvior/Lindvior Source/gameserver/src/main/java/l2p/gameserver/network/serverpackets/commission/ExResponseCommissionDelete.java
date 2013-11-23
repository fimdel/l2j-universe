/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets.commission;

import l2p.gameserver.network.serverpackets.L2GameServerPacket;

/**
 * Created with IntelliJ IDEA.
 * User: Darvin
 * Date: 09.06.12
 * Time: 22:10
 */
public class ExResponseCommissionDelete extends L2GameServerPacket {
    protected void writeImpl() {
        writeEx449(0xF5);
        writeD(0x00);
        writeD(0x00);
        writeQ(0x00);
    }
}
