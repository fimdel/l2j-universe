package l2p.gameserver.stats.conditions;

import l2p.gameserver.model.Player;
import l2p.gameserver.stats.Env;

public class ConditionPlayerMaxPK extends Condition {
    private final int _pk;

    public ConditionPlayerMaxPK(int pk) {
        _pk = pk;
    }

    @Override
    protected boolean testImpl(Env env) {
        if (env.character.isPlayer())
            return ((Player) env.character).getPkKills() <= _pk;
        return false;
    }
}