/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.skills.effects;

import l2p.gameserver.model.Effect;
import l2p.gameserver.stats.Env;

public final class EffectDeathImmunity extends Effect {

    public EffectDeathImmunity(Env env, EffectTemplate template) {
        super(env, template);
    }

    @Override
    public boolean checkCondition() {
        return super.checkCondition();
    }

    @Override
    public void onStart() {
        getEffected().startDeathImmunity();
    }

    @Override
    public void onExit() {
        getEffected().stopDeathImmunity();
    }

    @Override
    public boolean onActionTime() {
        return false;
    }
}
