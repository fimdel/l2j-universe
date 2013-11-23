/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package l2p.gameserver.network.serverpackets.Beautyshop;

import l2p.gameserver.model.Player;
import l2p.gameserver.network.serverpackets.L2GameServerPacket;

import java.util.Collections;
import java.util.List;

public class ExResponseBeautyListPacket extends L2GameServerPacket {

    private long _adena_count, _item_count;
    private int _check;

    private int[] face = new int[]{10001, 10002, 10003, 10004, 10005, 10006, 10007, 10008, 10009, 10010};
    private int[] face2 = new int[]{99999959, 99999982, 99999948, 99999936, 99999992, 99999987, 99999962, 99999972, 99999936, 99999927};
    private List<int[]> _reqs = Collections.emptyList();

    public ExResponseBeautyListPacket(Player pl, int check) {
        _adena_count = pl.getInventory().getAdena();
        _item_count = pl.getInventory().getAdena2();
        _check = check;
    }

    @Override
    protected void writeImpl() {
        writeEx(0x13F);
        writeQ(_adena_count);
        writeQ(_item_count);
        writeD(_check);
        if (_check == 0) {
            writeD(10);

            writeD(10001);
            writeD(99999959);

            writeD(10002);
            writeD(99999982);

            writeD(10003);
            writeD(99999948);

            writeD(10004);
            writeD(99999936);

            writeD(10005);
            writeD(99999992);

            writeD(10006);
            writeD(99999987);

            writeD(10007);
            writeD(99999962);

            writeD(10008);
            writeD(99999972);

            writeD(10009);
            writeD(99999936);

            writeD(10010);
            writeD(99999927);
        }
        if (_check == 1) {
            writeD(5);

            writeD(20001);
            writeD(99999982);

            writeD(20002);
            writeD(99999970);

            writeD(20003);
            writeD(99999981);

            writeD(20004);
            writeD(99999937);

            writeD(20005);
            writeD(99999956);
        }
    }
}