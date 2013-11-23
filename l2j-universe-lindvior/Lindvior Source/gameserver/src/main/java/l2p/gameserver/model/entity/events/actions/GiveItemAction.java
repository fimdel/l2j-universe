package l2p.gameserver.model.entity.events.actions;

import l2p.gameserver.model.Player;
import l2p.gameserver.model.entity.events.EventAction;
import l2p.gameserver.model.entity.events.GlobalEvent;

/**
 * @author VISTALL
 * @date 12:53/09.05.2011
 */
public class GiveItemAction implements EventAction {
    private int _itemId;
    private long _count;

    public GiveItemAction(int itemId, long count) {
        _itemId = itemId;
        _count = count;
    }

    @Override
    public void call(GlobalEvent event) {
        for (Player player : event.itemObtainPlayers())
            event.giveItem(player, _itemId, _count);
    }
}
