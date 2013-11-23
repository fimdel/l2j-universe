/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.skills.effects;

import l2p.gameserver.ai.CtrlIntention;
import l2p.gameserver.model.Effect;
import l2p.gameserver.model.instances.MonsterInstance;
import l2p.gameserver.stats.Env;

public final class i_delete_hate_of_me extends Effect {
    public i_delete_hate_of_me(Env env, EffectTemplate template) {
        super(env, template);
    }

    @Override
    public boolean checkCondition() {
        return getEffected().isMonster();
    }

    @Override
    public void onStart() {
        MonsterInstance monster = (MonsterInstance) getEffected();
        monster.getAggroList().remove(getEffector(), true);
        monster.getAI().setIntention(CtrlIntention.AI_INTENTION_ACTIVE);
    }

    @Override
    public boolean isHidden() {
        return true;
    }

    @Override
    public boolean onActionTime() {
        return false;
    }
}