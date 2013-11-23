/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets.commission;

import l2p.gameserver.network.serverpackets.L2GameServerPacket;

/**
 * @author : Darvin
 */
public class ExResponseCommissionBuyItem extends L2GameServerPacket {
    public static final ExResponseCommissionBuyItem FAILED = new ExResponseCommissionBuyItem(0);

    private int _code;
    private int _itemId;
    private long _count;

    public ExResponseCommissionBuyItem(int code) {
        _code = code;
    }

    public ExResponseCommissionBuyItem(int code, int itemId, long count) {
        this._code = code;
        _itemId = itemId;
        _count = count;
    }

    @Override
    protected void writeImpl() {
        writeEx449(0xF8);
        writeD(_code);
        if (_code == 0)
            return;
        writeD(0x00); //unk, maybe item object Id
        writeD(_itemId);
        writeQ(_count);

    }
}
