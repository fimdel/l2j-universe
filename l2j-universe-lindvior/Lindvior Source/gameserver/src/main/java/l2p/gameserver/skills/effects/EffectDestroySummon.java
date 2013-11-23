package l2p.gameserver.skills.effects;

import l2p.gameserver.model.Effect;
import l2p.gameserver.model.Summon;
import l2p.gameserver.stats.Env;

public final class EffectDestroySummon extends Effect {
    public EffectDestroySummon(Env env, EffectTemplate template) {
        super(env, template);
    }

    @Override
    public boolean checkCondition() {
        if (!_effected.isServitor())
            return false;
        return super.checkCondition();
    }

    @Override
    public void onStart() {
        super.onStart();
        ((Summon) _effected).unSummon();
    }

    @Override
    public boolean onActionTime() {
        // just stop this effect
        return false;
    }
}