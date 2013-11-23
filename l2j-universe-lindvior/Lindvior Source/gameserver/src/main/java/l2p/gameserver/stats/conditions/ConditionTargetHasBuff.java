package l2p.gameserver.stats.conditions;

import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Effect;
import l2p.gameserver.skills.EffectType;
import l2p.gameserver.stats.Env;

public final class ConditionTargetHasBuff extends Condition {
    private final EffectType _effectType;
    private final int _level;

    public ConditionTargetHasBuff(EffectType effectType, int level) {
        _effectType = effectType;
        _level = level;
    }

    @Override
    protected boolean testImpl(Env env) {
        Creature target = env.target;
        if (target == null)
            return false;
        Effect effect = target.getEffectList().getEffectByType(_effectType);
        if (effect == null)
            return false;
        if (_level == -1 || effect.getSkill().getLevel() >= _level)
            return true;
        return false;
    }
}
