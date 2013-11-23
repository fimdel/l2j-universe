package l2p.gameserver.skills.effects;

import l2p.gameserver.model.Effect;
import l2p.gameserver.stats.Env;

/**
 * Иммобилизует и парализует на время действия.
 *
 * @author Diamond
 * @date 24.07.2007
 * @time 5:32:46
 */
public final class EffectMeditation extends Effect {
    public EffectMeditation(Env env, EffectTemplate template) {
        super(env, template);
    }

    @Override
    public void onStart() {
        super.onStart();
        _effected.startParalyzed();
        _effected.setMeditated(true);
    }

    @Override
    public void onExit() {
        super.onExit();
        _effected.stopParalyzed();
        _effected.setMeditated(false);
    }

    @Override
    public boolean onActionTime() {
        return false;
    }
}