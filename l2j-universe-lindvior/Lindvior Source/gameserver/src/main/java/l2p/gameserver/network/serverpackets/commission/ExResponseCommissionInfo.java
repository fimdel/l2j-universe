/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets.commission;

import l2p.gameserver.model.items.ItemInstance;
import l2p.gameserver.network.serverpackets.L2GameServerPacket;

/**
 * @author : Darvin
 */
public class ExResponseCommissionInfo extends L2GameServerPacket {
    private int response;
    private ItemInstance _item;

    public ExResponseCommissionInfo(ItemInstance item, int response) {
        this.response = response;
        _item = item;
    }

    protected void writeImpl() {
        writeEx449(0xF3);
        writeD(response);
        if (response == 1) {
            writeD(_item.getObjectId());
            writeQ(_item.getReferencePrice());
            writeQ(_item.getCount());
            writeD(0);
            writeD(0); // unknown, always 0
        }
    }
}
