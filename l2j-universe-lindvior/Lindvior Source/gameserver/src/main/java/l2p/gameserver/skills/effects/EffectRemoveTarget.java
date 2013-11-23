package l2p.gameserver.skills.effects;

import l2p.commons.util.Rnd;
import l2p.gameserver.ai.CtrlIntention;
import l2p.gameserver.ai.DefaultAI;
import l2p.gameserver.model.Effect;
import l2p.gameserver.stats.Env;

/**
 * @author nonam3
 * @date 08/01/2011 17:37
 */
public final class EffectRemoveTarget extends Effect {
    private boolean _doStopTarget;

    public EffectRemoveTarget(Env env, EffectTemplate template) {
        super(env, template);
        _doStopTarget = template.getParam().getBool("doStopTarget", false);
    }

    @Override
    public boolean checkCondition() {
        return Rnd.chance(_template.chance(100));
    }

    @Override
    public void onStart() {
        if (getEffected().getAI() instanceof DefaultAI)
            ((DefaultAI) getEffected().getAI()).setGlobalAggro(System.currentTimeMillis() + 3000L);

        getEffected().setTarget(null);
        if (_doStopTarget)
            getEffected().stopMove();
        getEffected().abortAttack(true, true);
        getEffected().abortCast(true, true);
        getEffected().getAI().setIntention(CtrlIntention.AI_INTENTION_ACTIVE, getEffector());
    }

    @Override
    public boolean isHidden() {
        return true;
    }

    @Override
    public boolean onActionTime() {
        return false;
    }
}