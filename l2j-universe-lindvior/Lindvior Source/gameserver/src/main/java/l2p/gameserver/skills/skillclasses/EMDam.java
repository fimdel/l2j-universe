package l2p.gameserver.skills.skillclasses;

import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Skill;
import l2p.gameserver.stats.Formulas;
import l2p.gameserver.templates.StatsSet;

import java.util.List;

/**
 * @author ALF
 *         По сути - это тип для скилов, которые зависят от Ауры Элементов.
 */
public class EMDam extends Skill {
    private static final int[] TRUE_ELEMENTS = {11007, 11008, 11009, 11010};

    public EMDam(StatsSet set) {
        super(set);
    }

    @Override
    public boolean checkCondition(Creature activeChar, Creature target, boolean forceUse, boolean dontMove, boolean first) {
        if (!super.checkCondition(activeChar, target, forceUse, dontMove, first))
            return false;

        if (!activeChar.getEffectList().containEffectFromSkills(TRUE_ELEMENTS)) {
            if (activeChar.isPlayer())
                activeChar.sendMessage("Необходимо активировать одну с аур...");
            return false;
        }
        return true;
    }

    @Override
    public void useSkill(Creature activeChar, List<Creature> targets) {
        int sps = isSSPossible() ? isMagic() ? activeChar.getChargedSpiritShot() : activeChar.getChargedSoulShot() ? 2 : 0 : 0;

        Creature realTarget;
        boolean reflected;

        for (Creature target : targets)
            if (target != null) {
                if (target.isDead())
                    continue;
                reflected = target.checkReflectSkill(activeChar, this);
                realTarget = reflected ? activeChar : target;

                Formulas.AttackInfo info = Formulas.calcMagicDam(activeChar, realTarget, this, sps);
                if (info.damage >= 1)
                    realTarget.reduceCurrentHp(info.damage, info.reflectableDamage, activeChar, this, true, true, false, true, false, false, true);

                getEffects(activeChar, target, getActivateRate() > 0, false, reflected);
            }

        if (isSuicideAttack())
            activeChar.doDie(null);
        else if (isSSPossible())
            activeChar.unChargeShots(isMagic());
    }
}
