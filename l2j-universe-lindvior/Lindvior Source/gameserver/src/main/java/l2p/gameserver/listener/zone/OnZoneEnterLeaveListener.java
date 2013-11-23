package l2p.gameserver.listener.zone;

import l2p.commons.listener.Listener;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Zone;

public interface OnZoneEnterLeaveListener extends Listener<Zone> {
    public void onZoneEnter(Zone zone, Creature actor);

    public void onZoneLeave(Zone zone, Creature actor);
}
