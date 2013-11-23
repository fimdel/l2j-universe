/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets.CuriousHouse;

import l2p.gameserver.network.serverpackets.L2GameServerPacket;

public class ExCuriousHouseState extends L2GameServerPacket {
    public void ExCuriousHouseState() {
    }

    @Override
    protected void writeImpl() {
        writeEx(0x125);

        writeD(1);
    }

    @Override
    public String getType() {
        return "[S] FE:125 ExCuriousHouseState";
    }
}
