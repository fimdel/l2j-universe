/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets.CuriousHouse;

import l2p.gameserver.network.serverpackets.L2GameServerPacket;

//пропадает почти весь интерфейс и пооявляется кнопка отказ
//связан с пакетом RequestLeaveCuriousHouse
public class ExCuriousHouseLeave extends L2GameServerPacket {
    @Override
    protected void writeImpl() {
        writeEx(0x126);
    }

    @Override
    public String getType() {
        return "[S] FE:126 ExCuriousHouseLeave";
    }
}
