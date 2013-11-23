/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package l2p.gameserver.network.serverpackets.Beautyshop;

import l2p.gameserver.model.Player;
import l2p.gameserver.network.serverpackets.L2GameServerPacket;

public class ExResponseBeautyRegistResetPacket extends L2GameServerPacket {

    private long _adena_count, _item_count;
    private int _check;  // 1 = reset/ 0 =regist

    public ExResponseBeautyRegistResetPacket(Player pl, int check) {
        _adena_count = pl.getInventory().getAdena();
        _item_count = pl.getInventory().getAdena2();
        _check = check;
    }

    @Override
    protected void writeImpl() {
        writeEx(0x140);
        writeQ(_adena_count);
        writeQ(_item_count);
        writeD(0);
        writeD(1);
        writeD(1);
    }
}
