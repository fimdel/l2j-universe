package l2p.gameserver.skills.effects;

import l2p.gameserver.model.Effect;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.stats.Env;

public class EffectEnervation extends Effect {
    public EffectEnervation(Env env, EffectTemplate template) {
        super(env, template);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (_effected.isNpc())
            ((NpcInstance) _effected).setParameter("DebuffIntention", 0.5);
    }

    @Override
    public boolean onActionTime() {
        return false;
    }

    @Override
    public void onExit() {
        super.onExit();
        if (_effected.isNpc())
            ((NpcInstance) _effected).setParameter("DebuffIntention", 1.);
    }
}