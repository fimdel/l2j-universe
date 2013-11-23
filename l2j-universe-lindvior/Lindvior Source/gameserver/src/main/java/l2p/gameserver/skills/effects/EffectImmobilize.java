package l2p.gameserver.skills.effects;

import l2p.gameserver.model.Effect;
import l2p.gameserver.stats.Env;

public final class EffectImmobilize extends Effect {
    public EffectImmobilize(Env env, EffectTemplate template) {
        super(env, template);
    }

    @Override
    public void onStart() {
        super.onStart();
        _effected.startImmobilized();
        _effected.stopMove();
    }

    @Override
    public void onExit() {
        super.onExit();
        _effected.stopImmobilized();
    }

    @Override
    public boolean onActionTime() {
        return false;
    }
}
