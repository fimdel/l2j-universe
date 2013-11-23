/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets.CuriousHouse;

import l2p.gameserver.network.serverpackets.L2GameServerPacket;

public class ExCuriousHouseObserveMode extends L2GameServerPacket {
    @Override
    protected void writeImpl() {
        writeEx(0x12C);

        writeH(0);
    }

    @Override
    public String getType() {
        return "[S] FE:12C ExCuriousHouseObserveMode";
    }
}
