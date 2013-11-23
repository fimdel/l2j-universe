package l2p.gameserver.listener.actor;

import l2p.gameserver.listener.CharListener;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Skill;

public interface OnCurrentHpDamageListener extends CharListener {
    public void onCurrentHpDamage(Creature actor, double damage, Creature attacker, Skill skill);
}
