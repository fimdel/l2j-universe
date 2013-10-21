package lineage2.gameserver.network.serverpackets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lineage2.commons.lang.ArrayUtils;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.items.ItemInfo;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.model.items.Warehouse.ItemClassComparator;
import lineage2.gameserver.model.items.Warehouse.WarehouseType;
import lineage2.gameserver.templates.item.ItemTemplate.ItemClass;

/**
 * @author ALF
 * @data 10.02.2012
 */
public class WareHouseWithdrawList extends L2GameServerPacket
{
    private long _adena;
    private List<ItemInfo> _itemList = new ArrayList<ItemInfo>();
    private int _type;
    private int _inventoryUsedSlots;

    public WareHouseWithdrawList(Player player, WarehouseType type, ItemClass clss)
    {
        _adena = player.getAdena();
        _type = type.ordinal();

        ItemInstance[] items;
        switch (type)
        {
            case PRIVATE:
                items = player.getWarehouse().getItems(clss);
                break;
            case FREIGHT:
                items = player.getFreight().getItems(clss);
                break;
            case CLAN:
            case CASTLE:
                items = player.getClan().getWarehouse().getItems(clss);
                break;
            default:
                _itemList = Collections.emptyList();
                return;
        }

        _itemList = new ArrayList<ItemInfo>(items.length);
        ArrayUtils.eqSort(items, ItemClassComparator.getInstance());
        for (ItemInstance item : items)
        {
            _itemList.add(new ItemInfo(item));
        }
        _inventoryUsedSlots = player.getInventory().getSize();
    }

    @Override
    protected final void writeImpl()
    {
        writeC(0x42);
        writeH(_type);
        writeQ(_adena);
        writeH(_itemList.size());
        writeH(1);
        writeD(0);
        writeD(_inventoryUsedSlots);
        for (ItemInfo item : _itemList)
        {
            writeItemInfo(item);
            writeD(item.getObjectId());
            writeD(0x00);
            writeD(0x00);
        }
    }
}