package l2p.gameserver.listener.actor;

import l2p.gameserver.listener.CharListener;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Skill;

public interface OnMagicHitListener extends CharListener {
    public void onMagicHit(Creature actor, Skill skill, Creature caster);
}
