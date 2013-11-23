package l2p.gameserver.skills.skillclasses;

import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Skill;
import l2p.gameserver.network.serverpackets.SystemMessage;
import l2p.gameserver.stats.Stats;
import l2p.gameserver.templates.StatsSet;

import java.util.List;

public class ManaHealPercent extends Skill {
    private final boolean _ignoreMpEff;

    public ManaHealPercent(StatsSet set) {
        super(set);
        _ignoreMpEff = set.getBool("ignoreMpEff", true);
    }

    @Override
    public void useSkill(Creature activeChar, List<Creature> targets) {
        for (Creature target : targets)
            if (target != null) {
                if (target.isDead() || target.isHealBlocked())
                    continue;

                getEffects(activeChar, target, getActivateRate() > 0, false);

                double mp = _power * target.getMaxMp() / 100.;
                double newMp = mp * (!_ignoreMpEff ? target.calcStat(Stats.MANAHEAL_EFFECTIVNESS, 100., activeChar, this) : 100.) / 100.;
                double addToMp = Math.max(0, Math.min(newMp, target.calcStat(Stats.MP_LIMIT, null, null) * target.getMaxMp() / 100. - target.getCurrentMp()));

                if (addToMp > 0)
                    target.setCurrentMp(target.getCurrentMp() + addToMp);
                if (target.isPlayer())
                    if (activeChar != target)
                        target.sendPacket(new SystemMessage(SystemMessage.XS2S_MP_HAS_BEEN_RESTORED_BY_S1).addString(activeChar.getName()).addNumber(Math.round(addToMp)));
                    else
                        activeChar.sendPacket(new SystemMessage(SystemMessage.S1_MPS_HAVE_BEEN_RESTORED).addNumber(Math.round(addToMp)));
            }

        if (isSSPossible())
            activeChar.unChargeShots(isMagic());
    }
}
