/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package l2p.gameserver.network.serverpackets.Beautyshop;

import l2p.gameserver.network.serverpackets.L2GameServerPacket;

public class ExShowBeautyMenuPacket extends L2GameServerPacket {

    private final int _result;

    public ExShowBeautyMenuPacket(int result) {
        _result = result;
    }

    @Override
    protected void writeImpl() {
        writeEx(0x13E);
        writeD(_result);          // если 0х00 то открывается салон красоты, если 0х01 то открывается меню сбороса стиля
    }
}
