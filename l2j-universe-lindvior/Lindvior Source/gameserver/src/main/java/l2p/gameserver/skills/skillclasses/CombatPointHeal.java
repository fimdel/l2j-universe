package l2p.gameserver.skills.skillclasses;

import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Skill;
import l2p.gameserver.network.serverpackets.SystemMessage;
import l2p.gameserver.stats.Stats;
import l2p.gameserver.templates.StatsSet;

import java.util.List;

public class CombatPointHeal extends Skill {
    private final boolean _ignoreCpEff;

    public CombatPointHeal(StatsSet set) {
        super(set);
        _ignoreCpEff = set.getBool("ignoreCpEff", false);
    }

    @Override
    public void useSkill(Creature activeChar, List<Creature> targets) {
        for (Creature target : targets)
            if (target != null) {
                if (target.isDead() || target.isHealBlocked())
                    continue;
                double maxNewCp = _power * (!_ignoreCpEff ? target.calcStat(Stats.CPHEAL_EFFECTIVNESS, 100., activeChar, this) : 100.) / 100.;
                double addToCp = Math.max(0, Math.min(maxNewCp, target.calcStat(Stats.CP_LIMIT, null, null) * target.getMaxCp() / 100. - target.getCurrentCp()));
                if (addToCp > 0)
                    target.setCurrentCp(addToCp + target.getCurrentCp());
                target.sendPacket(new SystemMessage(SystemMessage.S1_CPS_WILL_BE_RESTORED).addNumber((long) addToCp));
                getEffects(activeChar, target, getActivateRate() > 0, false);
            }
        if (isSSPossible())
            activeChar.unChargeShots(isMagic());
    }
}
