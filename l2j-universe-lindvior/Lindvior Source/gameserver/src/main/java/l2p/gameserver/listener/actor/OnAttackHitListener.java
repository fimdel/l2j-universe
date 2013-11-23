package l2p.gameserver.listener.actor;

import l2p.gameserver.listener.CharListener;
import l2p.gameserver.model.Creature;

public interface OnAttackHitListener extends CharListener {
    public void onAttackHit(Creature actor, Creature attacker);
}
