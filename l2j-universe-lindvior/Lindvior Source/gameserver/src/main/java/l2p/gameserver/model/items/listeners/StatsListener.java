package l2p.gameserver.model.items.listeners;

import l2p.gameserver.listener.inventory.OnEquipListener;
import l2p.gameserver.model.Playable;
import l2p.gameserver.model.items.ItemInstance;
import l2p.gameserver.stats.funcs.Func;

public final class StatsListener implements OnEquipListener {
    private static final StatsListener _instance = new StatsListener();

    public static StatsListener getInstance() {
        return _instance;
    }

    @Override
    public void onUnequip(int slot, ItemInstance item, Playable actor) {
        actor.removeStatsOwner(item);
        actor.updateStats();
    }

    @Override
    public void onEquip(int slot, ItemInstance item, Playable actor) {
        Func[] funcs = item.getStatFuncs();
        actor.addStatFuncs(funcs);
        actor.updateStats();
    }
}