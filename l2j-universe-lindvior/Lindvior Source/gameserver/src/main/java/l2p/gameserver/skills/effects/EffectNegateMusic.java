package l2p.gameserver.skills.effects;

import l2p.gameserver.model.Effect;
import l2p.gameserver.stats.Env;

public class EffectNegateMusic extends Effect {
    public EffectNegateMusic(Env env, EffectTemplate template) {
        super(env, template);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onExit() {
        super.onExit();
    }

    @Override
    public boolean onActionTime() {
        for (Effect e : _effected.getEffectList().getAllEffects())
            if (e.getSkill().isMusic())
                e.exit();
        return false;
    }
}