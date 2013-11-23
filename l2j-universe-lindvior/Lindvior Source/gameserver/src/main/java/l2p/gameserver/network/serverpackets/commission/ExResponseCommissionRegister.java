/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets.commission;

import l2p.gameserver.network.serverpackets.L2GameServerPacket;

public class ExResponseCommissionRegister extends L2GameServerPacket {
    protected void writeImpl() {
        writeEx449(0xF4);
        writeD(0x00);
        writeD(0x00);
        writeQ(0x00);
    }
}
