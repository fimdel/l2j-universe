/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets.CuriousHouse;

import l2p.gameserver.network.serverpackets.L2GameServerPacket;

public class ExCuriousHouseRemainTime extends L2GameServerPacket {
    public void ExCuriousHouseRemainTime() {
    }

    @Override
    protected void writeImpl() {
        writeEx(0x129);

        writeD(360);
    }

    @Override
    public String getType() {
        return "[S] FE:129 ExCuriousHouseRemainTime";
    }
}
