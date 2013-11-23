package l2p.gameserver.skills.skillclasses;

import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Skill;
import l2p.gameserver.templates.StatsSet;

import java.util.List;

public class CPDam extends Skill {
    public CPDam(StatsSet set) {
        super(set);
    }

    @Override
    public void useSkill(Creature activeChar, List<Creature> targets) {
        boolean ss = activeChar.getChargedSoulShot() && isSSPossible();
        if (ss)
            activeChar.unChargeShots(false);

        Creature realTarget;
        boolean reflected;

        for (Creature target : targets)
            if (target != null) {
                if (target.isDead())
                    continue;

                target.doCounterAttack(this, activeChar, false);

                reflected = target.checkReflectSkill(activeChar, this);
                realTarget = reflected ? activeChar : target;

                if (realTarget.isCurrentCpZero())
                    continue;

                double damage = _power * realTarget.getCurrentCp();

                if (damage < 1)
                    damage = 1;

                realTarget.reduceCurrentHp(damage, 0, activeChar, this, true, true, false, true, false, false, true);

                getEffects(activeChar, target, getActivateRate() > 0, false, reflected);
            }
    }
}
