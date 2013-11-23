/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets.CuriousHouse;

import l2p.gameserver.network.serverpackets.L2GameServerPacket;

//открывается   окошко и написано ничья, кароче лисп победителя
public class ExCuriousHouseObserveList extends L2GameServerPacket {
    @Override
    protected void writeImpl() {
        writeEx(0x12B);

        writeD(0);

        for (; ; ) {
            writeD(0);
            writeS("");

            writeH(0);

            writeD(0);
        }
    }

    @Override
    public String getType() {
        return "[S] FE:12B ExCuriousHouseObserveList";
    }
}
