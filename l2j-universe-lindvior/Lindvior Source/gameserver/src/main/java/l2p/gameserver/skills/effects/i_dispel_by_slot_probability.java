/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.skills.effects;

import l2p.commons.util.Rnd;
import l2p.gameserver.model.Effect;
import l2p.gameserver.stats.Env;

import java.util.List;

public class i_dispel_by_slot_probability extends Effect {
    private final String _abnormalType;
    private final int _dispelChance;

    public i_dispel_by_slot_probability(Env env, EffectTemplate template) {
        super(env, template);

        _abnormalType = template.getParam().getString("abnormal_type");
        if (_abnormalType.equalsIgnoreCase("null") || _abnormalType.equalsIgnoreCase("none"))
            _dispelChance = 0;
        else
            _dispelChance = template.getParam().getInteger("dispel_chance", 100);
    }

    @Override
    public void onStart() {
        super.onStart();

        if (_dispelChance == 0) {
            return;
        }
        List<Effect> effects = getEffected().getEffectList().getEffectsByAbnormalType(_abnormalType);
        for (Effect effect : effects) {
            if (effect.getSkill().isCancelable()) {
                if (Rnd.chance(_dispelChance))
                    effect.exit();
            }
        }
    }

    @Override
    public void onExit() {
        super.onExit();
    }

    @Override
    public boolean onActionTime() {
        return false;
    }
}