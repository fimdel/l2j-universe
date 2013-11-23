/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.handler.items;

import l2p.gameserver.model.Playable;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.items.ItemInstance;
import l2p.gameserver.utils.Location;
import l2p.gameserver.utils.Log;
import org.apache.commons.lang3.ArrayUtils;

/**
 * Mother class of all itemHandlers.<BR><BR>
 * an IItemHandler implementation has to be stateless
 */
public interface IItemHandler {
    public static final IItemHandler NULL = new IItemHandler() {
        @Override
        public boolean useItem(Playable playable, ItemInstance item, boolean ctrl) {
            return false;
        }

        @Override
        public void dropItem(Player player, ItemInstance item, long count, Location loc) {
            if (item.isEquipped()) {
                player.getInventory().unEquipItem(item);
                player.sendUserInfo(true);
            }

            item = player.getInventory().removeItemByObjectId(item.getObjectId(), count);
            if (item == null) {
                player.sendActionFailed();
                return;
            }

            Log.LogItem(player, Log.Drop, item);

            item.dropToTheGround(player, loc);
            player.disableDrop(1000);

            player.sendChanges();
        }

        @Override
        public boolean pickupItem(Playable playable, ItemInstance item) {
            return true;
        }

        @Override
        public int[] getItemIds() {
            return ArrayUtils.EMPTY_INT_ARRAY;
        }
    };

    /**
     * Launch task associated to the item.
     *
     * @param playable
     * @param item     : L2ItemInstance designating the item to use
     * @param ctrl
     */
    public boolean useItem(Playable playable, ItemInstance item, boolean ctrl);

    /**
     * Check can drop or not
     *
     * @param player
     * @param item
     * @param count
     * @param loc    @return can drop
     */
    public void dropItem(Player player, ItemInstance item, long count, Location loc);

    /**
     * Check if can pick up item
     *
     * @param playable
     * @param item
     * @return
     */
    public boolean pickupItem(Playable playable, ItemInstance item);

    /**
     * Returns the list of item IDs corresponding to the type of item.<BR><BR>
     * <B><I>Use :</I></U><BR>
     * This method is called at initialization to register all the item IDs automatically
     *
     * @return int[] designating all itemIds for a type of item.
     */
    public int[] getItemIds();
}
