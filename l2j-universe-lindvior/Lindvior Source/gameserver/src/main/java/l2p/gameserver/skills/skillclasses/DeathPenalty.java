package l2p.gameserver.skills.skillclasses;

import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.Skill;
import l2p.gameserver.templates.StatsSet;

import java.util.List;

public class DeathPenalty extends Skill {
    public DeathPenalty(StatsSet set) {
        super(set);
    }

    @Override
    public boolean checkCondition(final Creature activeChar, final Creature target, boolean forceUse, boolean dontMove, boolean first) {
        // Chaotic characters can't use scrolls of recovery
        //   if (activeChar.getKarma() < 0 && !Config.ALT_DEATH_PENALTY_C5_CHAOTIC_RECOVERY) {
        // GOD свитки не работают, снизить штраф можно у спец нпс
        activeChar.sendActionFailed();
        return false;
        //   }

        //    return super.checkCondition(activeChar, target, forceUse, dontMove, first);
    }

    @Override
    public void useSkill(Creature activeChar, List<Creature> targets) {
        for (Creature target : targets)
            if (target != null) {
                if (!target.isPlayer())
                    continue;
                ((Player) target).getDeathPenalty().reduceLevel();
            }
    }
}
