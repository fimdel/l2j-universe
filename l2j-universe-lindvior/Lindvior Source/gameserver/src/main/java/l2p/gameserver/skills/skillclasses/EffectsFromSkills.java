package l2p.gameserver.skills.skillclasses;

import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Skill;
import l2p.gameserver.templates.StatsSet;

import java.util.List;

public class EffectsFromSkills extends Skill {
    public EffectsFromSkills(StatsSet set) {
        super(set);
    }

    @Override
    public void useSkill(Creature activeChar, List<Creature> targets) {
        for (Creature target : targets)
            if (target != null)
                for (AddedSkill as : getAddedSkills())
                    as.getSkill().getEffects(activeChar, target, false, false);
    }
}
