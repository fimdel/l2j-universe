package handler.items;

import l2p.gameserver.model.Player;
import l2p.gameserver.model.items.ItemInstance;
import l2p.gameserver.network.serverpackets.SystemMessage2;
import l2p.gameserver.network.serverpackets.components.SystemMsg;

/**
 * Created by IntelliJ IDEA.
 * User: Darvin
 * Date: 24.02.12
 * Time: 19:30
 */
public class TeleportBookmark extends SimpleItemHandler {
    private static final int[] ITEM_IDS = new int[]{13015};

    @Override
    public int[] getItemIds() {
        return ITEM_IDS;
    }

    @Override
    protected boolean useItemImpl(Player player, ItemInstance item, boolean ctrl) {
        if (player == null || item == null || !(player instanceof Player))
            return false;

        if (player.bookmarks.getCapacity() >= 30) {
            player.sendPacket(SystemMsg.YOUR_NUMBER_OF_MY_TELEPORTS_SLOTS_HAS_REACHED_ITS_MAXIMUM_LIMIT);
            return false;
        } else {
            player.getInventory().destroyItem(item, 1);
            player.sendPacket(new SystemMessage2(SystemMsg.S1_HAS_DISAPPEARED).addItemName(item.getItemId()));
            player.bookmarks.setCapacity(player.bookmarks.getCapacity() + 3);
            player.sendPacket(SystemMsg.THE_NUMBER_OF_MY_TELEPORTS_SLOTS_HAS_BEEN_INCREASED);
            return true;
        }
    }
}

