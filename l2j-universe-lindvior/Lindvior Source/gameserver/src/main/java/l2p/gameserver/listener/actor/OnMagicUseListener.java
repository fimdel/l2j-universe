package l2p.gameserver.listener.actor;

import l2p.gameserver.listener.CharListener;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Skill;

public interface OnMagicUseListener extends CharListener {
    public void onMagicUse(Creature actor, Skill skill, Creature target, boolean alt);
}
