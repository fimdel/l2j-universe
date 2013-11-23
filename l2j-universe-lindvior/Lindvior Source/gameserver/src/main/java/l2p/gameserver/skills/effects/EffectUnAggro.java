package l2p.gameserver.skills.effects;

import l2p.gameserver.model.Effect;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.stats.Env;

public class EffectUnAggro extends Effect {
    public EffectUnAggro(Env env, EffectTemplate template) {
        super(env, template);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (_effected.isNpc())
            ((NpcInstance) _effected).setUnAggred(true);
    }

    @Override
    public void onExit() {
        super.onExit();
        if (_effected.isNpc())
            ((NpcInstance) _effected).setUnAggred(false);
    }

    @Override
    public boolean onActionTime() {
        return false;
    }
}