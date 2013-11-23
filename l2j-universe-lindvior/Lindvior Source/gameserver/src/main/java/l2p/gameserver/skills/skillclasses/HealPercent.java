package l2p.gameserver.skills.skillclasses;

import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Skill;
import l2p.gameserver.network.serverpackets.SystemMessage;
import l2p.gameserver.stats.Stats;
import l2p.gameserver.templates.StatsSet;

import java.util.List;

public class HealPercent extends Skill {
    private final boolean _ignoreHpEff;

    public HealPercent(StatsSet set) {
        super(set);
        _ignoreHpEff = set.getBool("ignoreHpEff", true);
    }

    @Override
    public boolean checkCondition(Creature activeChar, Creature target, boolean forceUse, boolean dontMove, boolean first) {
        if (activeChar.isPlayable() && target.isMonster())
            return false;
        return super.checkCondition(activeChar, target, forceUse, dontMove, first);
    }

    @Override
    public void useSkill(Creature activeChar, List<Creature> targets) {
        for (Creature target : targets)
            if (target != null) {
                if (target.isHealBlocked())
                    continue;

                getEffects(activeChar, target, getActivateRate() > 0, false);

                double hp = _power * target.getMaxHp() / 100.;
                double newHp = hp * (!_ignoreHpEff ? target.calcStat(Stats.HEAL_EFFECTIVNESS, 100., activeChar, this) : 100.) / 100.;
                double addToHp = Math.max(0, Math.min(newHp, target.calcStat(Stats.HP_LIMIT, null, null) * target.getMaxHp() / 100. - target.getCurrentHp()));

                if (addToHp > 0)
                    target.setCurrentHp(addToHp + target.getCurrentHp(), false);
                if (target.isPlayer())
                    if (activeChar != target)
                        target.sendPacket(new SystemMessage(SystemMessage.XS2S_HP_HAS_BEEN_RESTORED_BY_S1).addString(activeChar.getName()).addNumber(Math.round(addToHp)));
                    else
                        activeChar.sendPacket(new SystemMessage(SystemMessage.S1_HPS_HAVE_BEEN_RESTORED).addNumber(Math.round(addToHp)));
            }

        if (isSSPossible())
            activeChar.unChargeShots(isMagic());
    }
}
