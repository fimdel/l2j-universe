/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.stats.triggers;

import l2p.commons.lang.ArrayUtils;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Skill;
import l2p.gameserver.stats.Env;
import l2p.gameserver.stats.conditions.Condition;

/**
 * @author VISTALL
 * @date 15:03/22.01.2011
 */
public class TriggerInfo extends Skill.AddedSkill {
    private final TriggerType _type;
    private final double _chance;
    private Condition[] _conditions = Condition.EMPTY_ARRAY;

    public TriggerInfo(int id, int level, TriggerType type, double chance) {
        super(id, level);
        _type = type;
        _chance = chance;
    }

    public TriggerInfo(int id, TriggerType type, double chance) {
        this(id, -1, type, chance);
    }

    public final void addCondition(Condition c) {
        _conditions = ArrayUtils.add(_conditions, c);
    }

    public boolean checkCondition(Creature actor, Creature target, Creature aimTarget, Skill owner, double damage) {
        // Скилл проверяется и кастуется на aimTarget
        if (getSkill().checkTarget(actor, aimTarget, aimTarget, false, false) != null) {
            return false;
        }

        Env env = new Env();
        env.character = actor;
        env.skill = owner;
        env.target = target; // В условии проверяется реальная цель.
        env.value = damage;

        for (Condition c : _conditions) {
            if (!c.test(env)) {
                return false;
            }
        }
        return true;
    }

    public TriggerType getType() {
        return _type;
    }

    public double getChance() {
        return _chance;
    }

    @Override
    public Skill getSkill() {
        if (level >= 1) {
            return super.getSkill();
        }
        return null;
    }
}
