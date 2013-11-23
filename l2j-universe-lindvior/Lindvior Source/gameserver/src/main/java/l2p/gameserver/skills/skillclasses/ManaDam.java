/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.skills.skillclasses;

import l2p.commons.util.Rnd;
import l2p.gameserver.cache.Msg;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Skill;
import l2p.gameserver.network.serverpackets.SystemMessage;
import l2p.gameserver.stats.Formulas;
import l2p.gameserver.stats.Stats;
import l2p.gameserver.templates.StatsSet;

import java.util.List;

public class ManaDam extends Skill {
    public ManaDam(StatsSet set) {
        super(set);
    }

    @Override
    public void useSkill(Creature activeChar, List<Creature> targets) {
        int sps = 0;
        if (isSSPossible())
            sps = activeChar.getChargedSpiritShot();

        for (Creature target : targets)
            if (target != null) {
                if (target.isDead())
                    continue;

                int magicLevel = getMagicLevel() == 0 ? activeChar.getLevel() : getMagicLevel();
                int landRate = Rnd.get(30, 100);
                landRate *= target.getLevel();
                landRate /= magicLevel;

                if (Rnd.chance(landRate)) {
                    double mAtk = activeChar.getMAtk(target, this);
                    if (sps == 2)
                        mAtk *= 4;
                    else if (sps == 1)
                        mAtk *= 2;

                    double mDef = target.getMDef(activeChar, this);
                    if (mDef < 1.)
                        mDef = 1.;

                    double damage = Math.sqrt(mAtk) * this.getPower() * (target.getMaxMp() / 97) / mDef;

                    boolean Mcrit = Formulas.calcMCrit(activeChar.getMagicCriticalRate(target, this));
                    if (Mcrit) {
                        activeChar.sendPacket(Msg.MAGIC_CRITICAL_HIT);
                        damage *= activeChar.calcStat(Stats.MAGIC_CRIT_DAMAGE, activeChar.isPlayable() && target.isPlayable() ? 2.5 : 3., target, this);
                    }
                    target.reduceCurrentMp(damage, activeChar);
                } else {
                    SystemMessage msg = new SystemMessage(SystemMessage.C1_RESISTED_C2S_MAGIC).addName(target).addName(activeChar);
                    activeChar.sendPacket(msg);
                    target.sendPacket(msg);
                    target.reduceCurrentHp(1., 0, activeChar, this, true, true, false, true, false, false, true);
                }

                getEffects(activeChar, target, getActivateRate() > 0, false);
            }

        if (isSSPossible())
            activeChar.unChargeShots(isMagic());
    }
}
