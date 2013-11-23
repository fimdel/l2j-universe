/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets;

import gnu.trove.TIntArrayList;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.items.ItemInstance;
import l2p.gameserver.model.items.etcitems.AttributeStoneInfo;
import l2p.gameserver.model.items.etcitems.AttributeStoneManager;
import l2p.gameserver.templates.item.type.ItemGrade;

public class ExChooseInventoryAttributeItem extends L2GameServerPacket {
    private TIntArrayList _attributableItems;
    private int _itemId;
    private int _stoneLvl;
    private int[] _att;

    public ExChooseInventoryAttributeItem(Player player, ItemInstance item) {
        _attributableItems = new TIntArrayList();
        ItemInstance[] items = player.getInventory().getItems();
        for (ItemInstance _item : items)
            if (_item.getCrystalType() != ItemGrade.NONE && (_item.isArmor() || _item.isWeapon()))
                _attributableItems.add(_item.getObjectId());

        _itemId = item.getItemId();
        _att = new int[6];

        AttributeStoneInfo asi = AttributeStoneManager.getStoneInfo(_itemId);
        _att[asi.getElement().getId()] = 1;
        _stoneLvl = asi.getStoneLevel();
    }

    @Override
    protected final void writeImpl() {
        writeEx(0x63);
        writeD(_itemId);
        for (int i : _att)
            writeD(i);
        writeD(_stoneLvl); // max enchant lvl
        writeD(_attributableItems.size()); // equipable items count
        for (int itemObjId : _attributableItems.toNativeArray())
            writeD(itemObjId); // itemObjId
    }
}