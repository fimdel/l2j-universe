/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.skills.effects;

import l2p.gameserver.model.Effect;
import l2p.gameserver.stats.Env;

/**
 * @author ALF
 * @date 02.08.2012
 */
public class EffectTargetLock extends Effect {

    public EffectTargetLock(Env env, EffectTemplate template) {
        super(env, template);
    }

    @Override
    public void onStart() {
        super.onStart();
        _effected.setLockedTarget(true);

    }

    @Override
    public void onExit() {
        super.onExit();
        _effected.setLockedTarget(false);

    }

    @Override
    public boolean onActionTime() {
        return false;
    }

}
