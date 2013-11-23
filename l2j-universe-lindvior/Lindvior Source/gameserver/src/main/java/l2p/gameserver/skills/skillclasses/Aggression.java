package l2p.gameserver.skills.skillclasses;

import l2p.gameserver.ai.CtrlEvent;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Skill;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.templates.StatsSet;

import java.util.List;

public class Aggression extends Skill {
    private final boolean _unaggring;
    private final boolean _silent;

    public Aggression(StatsSet set) {
        super(set);
        _unaggring = set.getBool("unaggroing", false);
        _silent = set.getBool("silent", false);
    }

    @Override
    public void useSkill(Creature activeChar, List<Creature> targets) {
        int effect = _effectPoint;

        if (isSSPossible() && (activeChar.getChargedSoulShot() || activeChar.getChargedSpiritShot() > 0))
            effect *= 2;

        for (Creature target : targets)
            if (target != null) {
                if (!target.isAutoAttackable(activeChar))
                    continue;
                if (target.isNpc())
                    if (_unaggring) {
                        if (target.isNpc() && activeChar.isPlayable())
                            ((NpcInstance) target).getAggroList().addDamageHate(activeChar, 0, -effect);
                    } else {
                        target.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, activeChar, effect);
                        if (!_silent)
                            target.getAI().notifyEvent(CtrlEvent.EVT_ATTACKED, activeChar, 0);
                    }
                else if (target.isPlayable() && !target.isDebuffImmune())
                    target.setTarget(activeChar);
                getEffects(activeChar, target, getActivateRate() > 0, false);
            }

        if (isSSPossible())
            activeChar.unChargeShots(isMagic());
    }
}
