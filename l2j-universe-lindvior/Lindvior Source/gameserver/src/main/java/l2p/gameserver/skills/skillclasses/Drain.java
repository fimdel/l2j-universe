package l2p.gameserver.skills.skillclasses;

import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Skill;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.stats.Formulas;
import l2p.gameserver.stats.Formulas.AttackInfo;
import l2p.gameserver.stats.Stats;
import l2p.gameserver.templates.StatsSet;

import java.util.List;

public class Drain extends Skill {
    private double _absorbAbs;

    public Drain(StatsSet set) {
        super(set);
        _absorbAbs = set.getDouble("absorbAbs", 0.f);
    }

    @Override
    public void useSkill(Creature activeChar, List<Creature> targets) {
        int sps = isSSPossible() ? activeChar.getChargedSpiritShot() : 0;
        boolean ss = isSSPossible() && activeChar.getChargedSoulShot();
        Creature realTarget;
        boolean reflected;
        final boolean corpseSkill = _targetType == SkillTargetType.TARGET_CORPSE;

        for (Creature target : targets)
            if (target != null) {
                reflected = !corpseSkill && target.checkReflectSkill(activeChar, this);
                realTarget = reflected ? activeChar : target;

                if (getPower() > 0 || _absorbAbs > 0) // Если == 0 значит скилл "отключен"
                {
                    if (realTarget.isDead() && !corpseSkill)
                        continue;

                    double hp = 0.;
                    double targetHp = realTarget.getCurrentHp();

                    if (!corpseSkill) {
                        double damage, reflectableDamage = 0;
                        if (isMagic()) {
                            AttackInfo info = Formulas.calcMagicDam(activeChar, realTarget, this, sps);
                            damage = info.damage;
                            reflectableDamage = info.reflectableDamage;
                        } else {
                            AttackInfo info = Formulas.calcPhysDam(activeChar, realTarget, this, false, false, ss, false);
                            damage = info.damage;
                            reflectableDamage = info.reflectableDamage;

                            if (info.lethal_dmg > 0)
                                realTarget.reduceCurrentHp(info.lethal_dmg, reflectableDamage, activeChar, this, true, true, false, false, false, false, false);
                        }
                        double targetCP = realTarget.getCurrentCp();

                        // Нельзя восстанавливать HP из CP
                        if (damage > targetCP || !realTarget.isPlayer())
                            hp = (damage - targetCP) * _absorbPart;

                        realTarget.reduceCurrentHp(damage, reflectableDamage, activeChar, this, true, true, false, true, false, false, true);
                        if (!reflected)
                            realTarget.doCounterAttack(this, activeChar, false);
                    }

                    if (_absorbAbs == 0 && _absorbPart == 0)
                        continue;

                    hp += _absorbAbs;

                    // Нельзя восстановить больше hp, чем есть у цели.
                    if (hp > targetHp && !corpseSkill)
                        hp = targetHp;

                    double addToHp = Math.max(0, Math.min(hp, activeChar.calcStat(Stats.HP_LIMIT, null, null) * activeChar.getMaxHp() / 100. - activeChar.getCurrentHp()));

                    if (addToHp > 0 && !activeChar.isHealBlocked())
                        activeChar.setCurrentHp(activeChar.getCurrentHp() + addToHp, false);

                    if (realTarget.isDead() && corpseSkill && realTarget.isNpc()) {
                        activeChar.getAI().setAttackTarget(null);
                        ((NpcInstance) realTarget).endDecayTask();
                    }
                }

                getEffects(activeChar, target, getActivateRate() > 0, false, reflected);
            }

        if (isMagic() ? sps != 0 : ss)
            activeChar.unChargeShots(isMagic());
    }
}
