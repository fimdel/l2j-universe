package l2p.gameserver.skills.skillclasses;

import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Skill;
import l2p.gameserver.templates.StatsSet;

import java.util.List;

public class Disablers extends Skill {
    private final boolean _skillInterrupt;

    public Disablers(StatsSet set) {
        super(set);
        _skillInterrupt = set.getBool("skillInterrupt", false);
    }

    @Override
    public void useSkill(Creature activeChar, List<Creature> targets) {
        Creature realTarget;
        boolean reflected;

        for (Creature target : targets)
            if (target != null) {
                reflected = target.checkReflectSkill(activeChar, this);
                realTarget = reflected ? activeChar : target;

                if (_skillInterrupt) {
                    if (realTarget.getCastingSkill() != null && !realTarget.getCastingSkill().isMagic() && !realTarget.isRaid())
                        realTarget.abortCast(false, true);
                    if (!realTarget.isRaid())
                        realTarget.abortAttack(true, true);
                }

                getEffects(activeChar, target, getActivateRate() > 0, false, reflected);
            }

        if (isSSPossible())
            activeChar.unChargeShots(isMagic());
    }
}
