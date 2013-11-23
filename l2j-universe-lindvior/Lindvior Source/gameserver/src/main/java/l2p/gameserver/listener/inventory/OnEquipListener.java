package l2p.gameserver.listener.inventory;

import l2p.commons.listener.Listener;
import l2p.gameserver.model.Playable;
import l2p.gameserver.model.items.ItemInstance;

public interface OnEquipListener extends Listener<Playable> {
    public void onEquip(int slot, ItemInstance item, Playable actor);

    public void onUnequip(int slot, ItemInstance item, Playable actor);
}
