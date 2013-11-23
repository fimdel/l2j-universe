/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets.CuriousHouse;

import l2p.gameserver.network.serverpackets.L2GameServerPacket;

//пир отправке этого пакета на экране появляется иконка получения письма
public class ExCuriousHouseEnter extends L2GameServerPacket {
    public void ExCuriousHouseEnter() {
    }

    @Override
    protected void writeImpl() {
        writeEx(0x125);
    }

    @Override
    public String getType() {
        return "[S] FE:125 ExCuriousHouseEnter";
    }
}
