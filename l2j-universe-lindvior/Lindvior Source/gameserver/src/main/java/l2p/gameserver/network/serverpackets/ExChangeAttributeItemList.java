/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package l2p.gameserver.network.serverpackets;

import l2p.gameserver.model.items.ItemInfo;

public class ExChangeAttributeItemList extends L2GameServerPacket {
    private ItemInfo[] _itemsList;
    private int _itemId;

    public ExChangeAttributeItemList(int itemId, ItemInfo[] itemsList) {
        _itemId = itemId;
        _itemsList = itemsList;
    }

    protected void writeImpl() {
        writeEx(0x118);
        writeD(_itemId);
        writeD(_itemsList.length); //size
        for (ItemInfo item : _itemsList) {
            writeItemInfo(item);
        }
    }
}