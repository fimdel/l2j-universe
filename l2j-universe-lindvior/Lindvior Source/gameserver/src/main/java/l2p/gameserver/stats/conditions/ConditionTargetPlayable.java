package l2p.gameserver.stats.conditions;

import l2p.gameserver.model.Creature;
import l2p.gameserver.stats.Env;

public class ConditionTargetPlayable extends Condition {
    private final boolean _flag;

    public ConditionTargetPlayable(boolean flag) {
        _flag = flag;
    }

    @Override
    protected boolean testImpl(Env env) {
        Creature target = env.target;
        return target != null && target.isPlayable() == _flag;
    }
}
