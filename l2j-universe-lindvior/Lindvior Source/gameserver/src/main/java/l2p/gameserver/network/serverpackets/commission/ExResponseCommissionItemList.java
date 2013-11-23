/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets.commission;

import l2p.gameserver.model.items.TradeItem;
import l2p.gameserver.network.serverpackets.L2GameServerPacket;

import java.util.List;

/**
 * @author : Darvin
 */
public class ExResponseCommissionItemList extends L2GameServerPacket {
    private List<TradeItem> items;

    public ExResponseCommissionItemList(List<TradeItem> items) {
        this.items = items;
    }

    @Override
    protected void writeImpl() {
        writeEx449(0xF2);
        writeD(items.size());// itemsSize
        for (TradeItem item : items) {
            writeItemInfo(item);
        }
    }
}