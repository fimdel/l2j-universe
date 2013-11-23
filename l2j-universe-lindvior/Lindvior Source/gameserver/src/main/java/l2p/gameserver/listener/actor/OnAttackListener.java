package l2p.gameserver.listener.actor;

import l2p.gameserver.listener.CharListener;
import l2p.gameserver.model.Creature;

public interface OnAttackListener extends CharListener {
    public void onAttack(Creature actor, Creature target);
}
