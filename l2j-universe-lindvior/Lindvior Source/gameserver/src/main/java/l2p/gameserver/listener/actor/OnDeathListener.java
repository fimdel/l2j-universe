package l2p.gameserver.listener.actor;

import l2p.gameserver.listener.CharListener;
import l2p.gameserver.model.Creature;

public interface OnDeathListener extends CharListener {
    public void onDeath(Creature actor, Creature killer);
}
