package l2p.gameserver.skills.skillclasses;

import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Skill;
import l2p.gameserver.model.Summon;
import l2p.gameserver.network.serverpackets.SystemMessage;
import l2p.gameserver.stats.Formulas;
import l2p.gameserver.templates.StatsSet;

import java.util.List;

public class DestroySummon extends Skill {
    public DestroySummon(StatsSet set) {
        super(set);
    }

    @Override
    public void useSkill(Creature activeChar, List<Creature> targets) {
        for (Creature target : targets)
            if (target != null) {

                if (getActivateRate() > 0 && !Formulas.calcSkillSuccess(activeChar, target, this, getActivateRate())) {
                    activeChar.sendPacket(new SystemMessage(SystemMessage.C1_HAS_RESISTED_YOUR_S2).addString(target.getName()).addSkillName(getId(), getLevel()));
                    continue;
                }

                if (target.isServitor()) {
                    ((Summon) target).saveEffects();
                    ((Summon) target).unSummon();
                    getEffects(activeChar, target, getActivateRate() > 0, false);
                }
            }

        if (isSSPossible())
            activeChar.unChargeShots(isMagic());
    }
}
