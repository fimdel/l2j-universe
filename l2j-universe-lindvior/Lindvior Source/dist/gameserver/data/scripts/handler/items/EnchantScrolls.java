package handler.items;

import l2p.gameserver.model.Playable;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.items.ItemInstance;
import l2p.gameserver.model.items.etcitems.EnchantScrollManager;
import l2p.gameserver.network.serverpackets.ChooseInventoryItem;

public class EnchantScrolls extends ScriptItemHandler {
    @Override
    public boolean useItem(Playable playable, ItemInstance item, boolean ctrl) {
        if (playable == null || !playable.isPlayer())
            return false;
        Player player = (Player) playable;

        if (player.getEnchantScroll() != null)
            return false;

        player.setEnchantScroll(item);
        player.sendPacket(new ChooseInventoryItem(item.getItemId()));
        return true;
    }

    @Override
    public final int[] getItemIds() {
        return EnchantScrollManager.getEnchantScrollIds();
    }
}