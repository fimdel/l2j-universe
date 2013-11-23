package l2p.gameserver.skills.effects;

import l2p.gameserver.ai.CtrlEvent;
import l2p.gameserver.model.Effect;
import l2p.gameserver.stats.Env;

public class EffectHate extends Effect {
    public EffectHate(Env env, EffectTemplate template) {
        super(env, template);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (_effected.isNpc() && _effected.isMonster())
            _effected.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, _effector, _template._value);
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