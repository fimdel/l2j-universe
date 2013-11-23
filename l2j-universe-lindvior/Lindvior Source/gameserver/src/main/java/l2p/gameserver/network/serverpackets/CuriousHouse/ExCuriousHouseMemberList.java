/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets.CuriousHouse;

import l2p.gameserver.network.serverpackets.L2GameServerPacket;

public class ExCuriousHouseMemberList extends L2GameServerPacket {
    public void ExCuriousHouseMemberList() {
    }

    @Override
    protected void writeImpl() {
        writeEx(0x127);

        writeD(0);
        writeD(3);

        writeD(3);

        writeD(268492063);
        writeD(1);
        writeD(222);
        writeD(16164);
        writeD(333);
        writeD(16164);
    }

    @Override
    public String getType() {
        return "[S] FE:127 ExCuriousHouseMemberList";
    }
}
