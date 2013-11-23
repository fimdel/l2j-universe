package l2p.gameserver.stats.conditions;

import l2p.gameserver.stats.Env;
import l2p.gameserver.utils.PositionUtils;

public class ConditionTargetDirection extends Condition {
    private final PositionUtils.TargetDirection _dir;

    public ConditionTargetDirection(PositionUtils.TargetDirection direction) {
        _dir = direction;
    }

    @Override
    protected boolean testImpl(Env env) {
        return PositionUtils.getDirectionTo(env.target, env.character) == _dir;
    }
}
