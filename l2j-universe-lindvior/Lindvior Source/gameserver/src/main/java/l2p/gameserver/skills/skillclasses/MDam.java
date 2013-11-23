package l2p.gameserver.skills.skillclasses;

import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Skill;
import l2p.gameserver.stats.Formulas;
import l2p.gameserver.templates.StatsSet;

import java.util.List;

public class MDam extends Skill {
    public MDam(StatsSet set) {
        super(set);
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
