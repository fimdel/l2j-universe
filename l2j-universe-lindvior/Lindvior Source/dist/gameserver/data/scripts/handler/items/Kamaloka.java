package handler.items;

import l2p.gameserver.model.Player;
import l2p.gameserver.model.items.ItemInstance;
import l2p.gameserver.network.serverpackets.SystemMessage;

public class Kamaloka extends SimpleItemHandler {
    private static final int[] ITEM_IDS = new int[]{13010, 13297, 20026, 13011, 13298, 20027, 13012, 13299, 20028};

    @Override
    public int[] getItemIds() {
        return ITEM_IDS;
    }

    @Override
    protected boolean useItemImpl(Player player, ItemInstance item, boolean ctrl) {
        int itemId = item.getItemId();

        switch (itemId) {
            case 13010:
            case 13297:
            case 20026:
                useItem(player, item, 1);
                player.removeInstanceReusesByGroupId(1);
                break;
            case 13011:
            case 13298:
            case 20027:
                useItem(player, item, 1);
                player.removeInstanceReusesByGroupId(2);
                break;
            case 13012:
            case 13299:
            case 20028:
                useItem(player, item, 1);
                player.removeInstanceReusesByGroupId(3);
                break;
        }
        player.sendPacket(new SystemMessage(SystemMessage.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addItemName(itemId));
        return false;
    }
}
