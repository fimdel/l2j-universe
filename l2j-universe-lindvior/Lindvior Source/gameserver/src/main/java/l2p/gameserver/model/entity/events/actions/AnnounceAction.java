package l2p.gameserver.model.entity.events.actions;

import l2p.gameserver.model.entity.events.EventAction;
import l2p.gameserver.model.entity.events.GlobalEvent;

/**
 * @author VISTALL
 * @date 11:12/11.03.2011
 */
public class AnnounceAction implements EventAction {
    private int _id;

    public AnnounceAction(int id) {
        _id = id;
    }

    @Override
    public void call(GlobalEvent event) {
        event.announce(_id);
    }
}
