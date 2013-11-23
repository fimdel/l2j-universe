package l2p.gameserver.network.serverpackets;

import l2p.gameserver.model.Player;
import l2p.gameserver.model.base.Element;
import l2p.gameserver.model.items.ItemInstance;
import l2p.gameserver.templates.item.ItemTemplate;

import java.util.ArrayList;
import java.util.List;


/**
 * @author SYS
 */
public class ExShowBaseAttributeCancelWindow extends L2GameServerPacket {
    private final List<ItemInstance> _items = new ArrayList<ItemInstance>();

    public ExShowBaseAttributeCancelWindow(Player activeChar) {
        for (ItemInstance item : activeChar.getInventory().getItems()) {
            if (item.getAttributeElement() == Element.NONE || !item.canBeEnchanted() || getAttributeRemovePrice(item) == 0)
                continue;
            _items.add(item);
        }
    }

    @Override
    protected final void writeImpl() {
        writeEx449(0x74);
        writeD(_items.size());
        for (ItemInstance item : _items) {
            writeD(item.getObjectId());
            writeQ(getAttributeRemovePrice(item));
        }
    }

    public static long getAttributeRemovePrice(ItemInstance item) {
        switch (item.getCrystalType()) {
            case S:
                return item.getTemplate().getType2() == ItemTemplate.TYPE2_WEAPON ? 50000 : 40000;
            case S80:
                return item.getTemplate().getType2() == ItemTemplate.TYPE2_WEAPON ? 100000 : 80000;
            case R:
                return item.getTemplate().getType2() == ItemTemplate.TYPE2_WEAPON ? 250000 : 240000;
            case R95:
                return item.getTemplate().getType2() == ItemTemplate.TYPE2_WEAPON ? 300000 : 280000;
            case R99:
                return item.getTemplate().getType2() == ItemTemplate.TYPE2_WEAPON ? 350000 : 320000;
            default:
                break;
        }
        return 0;
    }
}
