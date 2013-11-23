package l2p.gameserver.stats.conditions;

import l2p.gameserver.stats.Env;

public class ConditionPlayerOlympiad extends Condition {
    private final boolean _value;

    public ConditionPlayerOlympiad(boolean v) {
        _value = v;
    }

    @Override
    protected boolean testImpl(Env env) {
        return env.character.isInOlympiadMode() == _value;
    }
}