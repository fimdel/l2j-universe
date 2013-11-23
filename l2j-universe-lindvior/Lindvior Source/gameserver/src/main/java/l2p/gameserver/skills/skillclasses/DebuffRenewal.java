/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.skills.skillclasses;

import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Effect;
import l2p.gameserver.model.Skill;
import l2p.gameserver.skills.EffectType;
import l2p.gameserver.templates.StatsSet;

import java.util.List;

public class DebuffRenewal extends Skill {
    public DebuffRenewal(StatsSet set) {
        super(set);
    }

    @Override
    public void useSkill(Creature activeChar, List<Creature> targets) {
        for (Creature target : targets) {
            if (target != null) {
                renewEffect(target, EffectType.Paralyze);

                renewEffect(target, EffectType.Root);

                renewEffect(target, EffectType.Mute);
                renewEffect(target, EffectType.MuteAll);
                renewEffect(target, EffectType.MuteAttack);
                renewEffect(target, EffectType.MutePhisycal);

                renewEffect(target, EffectType.Sleep);

                renewEffect(target, EffectType.Stun);

                renewEffect(target, EffectType.Fear);

                renewEffect(target, EffectType.Petrification);

                renewEffect(target, EffectType.Disarm);

                renewEffect(target, EffectType.Mutation);

                getEffects(activeChar, target, getActivateRate() > 0, false);
                target.updateEffectIcons();
            }
        }

        if (isSSPossible())
            activeChar.unChargeShots(isMagic());
    }

    private void renewEffect(Creature target, EffectType type) {
        List<Effect> effects = (List<Effect>) target.getEffectList().getEffectByType(type);
        if (effects != null) {
            for (Effect effect : effects)
                effect.restart();
        }
    }
}