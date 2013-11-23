/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.skills.effects;

import l2p.gameserver.model.Effect;
import l2p.gameserver.stats.Env;

import java.util.List;

public class i_dispel_all extends Effect {
    public i_dispel_all(Env env, EffectTemplate template) {
        super(env, template);
    }

    @Override
    public void onStart() {
        super.onStart();

        List<Effect> effects = getEffected().getEffectList().getAllEffects();
        for (Effect effect : effects) {
            if (effect.getSkill().isCancelable()) {
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