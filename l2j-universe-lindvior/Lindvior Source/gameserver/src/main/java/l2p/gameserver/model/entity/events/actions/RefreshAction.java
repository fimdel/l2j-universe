package l2p.gameserver.model.entity.events.actions;

import l2p.gameserver.model.entity.events.EventAction;
import l2p.gameserver.model.entity.events.GlobalEvent;

/**
 * @author VISTALL
 * @date 23:45/09.03.2011
 */
public class RefreshAction implements EventAction {
    private final String _name;

    public RefreshAction(String name) {
        _name = name;
    }

    @Override
    public void call(GlobalEvent event) {
        event.refreshAction(_name);
    }
}
