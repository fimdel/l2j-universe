package l2p.gameserver.stats.conditions;

import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Effect;
import l2p.gameserver.stats.Env;

import java.util.List;

public final class ConditionTargetHasBuffId extends Condition {
    private final int _id;
    private final int _level;

    public ConditionTargetHasBuffId(int id, int level) {
        _id = id;
        _level = level;
    }

    @Override
    protected boolean testImpl(Env env) {
        Creature target = env.target;
        if (target == null)
            return false;
        if (_level == -1)
            return target.getEffectList().getEffectsBySkillId(_id) != null;
        List<Effect> el = target.getEffectList().getEffectsBySkillId(_id);
        if (el == null)
            return false;
        for (Effect effect : el)
            if (effect != null && effect.getSkill().getLevel() >= _level)
                return true;
        return false;
    }
}
