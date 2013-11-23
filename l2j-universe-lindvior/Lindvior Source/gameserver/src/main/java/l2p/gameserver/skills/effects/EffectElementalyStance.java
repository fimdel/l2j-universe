/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.skills.effects;

import l2p.gameserver.model.Effect;
import l2p.gameserver.skills.EffectType;
import l2p.gameserver.stats.Env;

/**
 * Created with IntelliJ IDEA. User: Darvin Date: 24.11.12 Time: 0:06
 */
public class EffectElementalyStance extends Effect {
    public EffectElementalyStance(Env env, EffectTemplate template) {
        super(env, template);
    }

    @Override
    protected void onStart() {
        onActionTime();
        super.onStart();
    }

    /*
      * На всякий случай
      */
    @Override
    protected boolean onActionTime() {
        if (_effected.getEffectList().getEffectByType(EffectType.DoubleCasting) == null) {
            Effect efa = _effected.getEffectList().getEffectByType(EffectType.ElementalyStance);

            if ((efa != null) && (efa.getSkill() != getSkill())) {
                efa.exit();
            }
        }

        return true;
    }
}