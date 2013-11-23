package l2p.gameserver.skills.effects;

import l2p.gameserver.model.Effect;
import l2p.gameserver.stats.Env;

public class EffectMuteAttack extends Effect {
    public EffectMuteAttack(Env env, EffectTemplate template) {
        super(env, template);
    }

    @Override
    public void onStart() {
        super.onStart();

        if (!_effected.startAMuted()) {
            _effected.abortCast(true, true);
            _effected.abortAttack(true, true);
        }
    }

    @Override
    public void onExit() {
        super.onExit();
        _effected.stopAMuted();
    }

    @Override
    public boolean onActionTime() {
        return false;
    }
}