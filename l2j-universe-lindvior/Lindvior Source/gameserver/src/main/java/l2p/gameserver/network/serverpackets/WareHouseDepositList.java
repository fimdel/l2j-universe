package l2p.gameserver.network.serverpackets;

import l2p.commons.lang.ArrayUtils;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.items.ItemInfo;
import l2p.gameserver.model.items.ItemInstance;
import l2p.gameserver.model.items.Warehouse.ItemClassComparator;
import l2p.gameserver.model.items.Warehouse.WarehouseType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ALF
 * @data 10.02.2012
 */
public class WareHouseDepositList extends L2GameServerPacket {
    private int _whtype;
    private long _adena;
    private List<ItemInfo> _itemList;
    private int _depositedItemsCount;

    public WareHouseDepositList(Player cha, WarehouseType whtype) {
        _whtype = whtype.ordinal();
        _adena = cha.getAdena();

        ItemInstance[] items = cha.getInventory().getItems();
        ArrayUtils.eqSort(items, ItemClassComparator.getInstance());
        _itemList = new ArrayList<ItemInfo>(items.length);
        for (ItemInstance item : items)
            if (item.canBeStored(cha, _whtype == 1))
                _itemList.add(new ItemInfo(item));

        switch (whtype) {
            case PRIVATE:
                _depositedItemsCount = cha.getWarehouse().getSize();
                break;
            case FREIGHT:
                _depositedItemsCount = cha.getFreight().getSize();
                break;
            case CLAN:
            case CASTLE:
                _depositedItemsCount = cha.getClan().getWarehouse().getSize();
                break;
            default:
                _depositedItemsCount = 0;
        }
    }

    @Override
    protected final void writeImpl() {
        writeC(0x41);
        writeH(_whtype);
        writeQ(_adena);
        writeH(_depositedItemsCount); //Количество вещей которые уже есть в банке.
        writeD(0);//TODO [Darvin]
        //for (size)
        //	writeD(ItemId);
        //
        writeH(_itemList.size());
        for (ItemInfo item : _itemList) {
            writeItemInfo(item);
            writeD(item.getObjectId());
        }
    }
}