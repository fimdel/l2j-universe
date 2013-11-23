package l2p.gameserver.skills.effects;

import l2p.gameserver.model.Effect;
import l2p.gameserver.stats.Env;

public class EffectCharmOfCourage extends Effect {
    public EffectCharmOfCourage(Env env, EffectTemplate template) {
        super(env, template);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (_effected.isPlayer())
            _effected.getPlayer().setCharmOfCourage(true);
    }

    @Override
    public void onExit() {
        super.onExit();
        _effected.getPlayer().setCharmOfCourage(false);
    }

    @Override
    public boolean onActionTime() {
        return false;
    }
}