package l2p.gameserver.skills.skillclasses;

import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Skill;
import l2p.gameserver.stats.Formulas;
import l2p.gameserver.stats.Formulas.AttackInfo;
import l2p.gameserver.templates.StatsSet;

import java.util.List;

public class ChargeSoul extends Skill {
    private int _numSouls;

    public ChargeSoul(StatsSet set) {
        super(set);
        _numSouls = set.getInteger("numSouls", getLevel());
    }

    @Override
    public void useSkill(Creature activeChar, List<Creature> targets) {
        if (!activeChar.isPlayer())
            return;

        boolean ss = activeChar.getChargedSoulShot() && isSSPossible();
        if (ss && getTargetType() != SkillTargetType.TARGET_SELF)
            activeChar.unChargeShots(false);

        Creature realTarget;
        boolean reflected;

        for (Creature target : targets)
            if (target != null) {
                if (target.isDead())
                    continue;

                reflected = target != activeChar && target.checkReflectSkill(activeChar, this);
                realTarget = reflected ? activeChar : target;

                if (getPower() > 0) // Если == 0 значит скилл "отключен"
                {
                    AttackInfo info = Formulas.calcPhysDam(activeChar, realTarget, this, false, false, ss, false);

                    if (info.lethal_dmg > 0)
                        realTarget.reduceCurrentHp(info.lethal_dmg, info.reflectableDamage, activeChar, this, true, true, false, false, false, false, false);

                    realTarget.reduceCurrentHp(info.damage, info.reflectableDamage, activeChar, this, true, true, false, true, false, false, true);
                    if (!reflected)
                        realTarget.doCounterAttack(this, activeChar, false);
                }

                if (realTarget.isPlayable() || realTarget.isMonster())
                    activeChar.setConsumedSouls(activeChar.getConsumedSouls() + _numSouls, null);

                getEffects(activeChar, target, getActivateRate() > 0, false, reflected);
            }

        if (isSSPossible())
            activeChar.unChargeShots(isMagic());
    }
}
