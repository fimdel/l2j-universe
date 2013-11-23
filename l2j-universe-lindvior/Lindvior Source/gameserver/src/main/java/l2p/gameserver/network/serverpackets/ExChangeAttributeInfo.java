/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package l2p.gameserver.network.serverpackets;

import l2p.gameserver.model.base.Element;
import l2p.gameserver.model.items.ItemInstance;

public class ExChangeAttributeInfo extends L2GameServerPacket {
    private int _crystalItemId;
    private int _attributes;
    private int _itemObjId;

    public ExChangeAttributeInfo(int crystalItemId, ItemInstance item) {
        // _itemObjId = item.getObjectId();
        _crystalItemId = crystalItemId;
        _attributes = 0;
        for (Element e : Element.VALUES) {
            if (e == item.getAttackElement())
                continue;
            _attributes |= e.getId();
        }
    }

    protected void writeImpl() {
        writeEx(0x119);
        writeD(_crystalItemId);//unk??
        writeD(_attributes);
        writeD(_itemObjId);//unk??
    }
}