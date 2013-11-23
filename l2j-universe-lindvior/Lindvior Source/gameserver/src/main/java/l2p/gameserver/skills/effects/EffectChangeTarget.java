/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.skills.effects;

import l2p.commons.util.Rnd;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Effect;
import l2p.gameserver.stats.Env;

import java.util.List;

/**
 * @author ALF
 * @date 06.11.2012
 */
public class EffectChangeTarget extends Effect {
    private final int radius;
    private final int height;

    public EffectChangeTarget(Env env, EffectTemplate template) {
        super(env, template);
        radius = template.getParam().getInteger("radius");
        height = template.getParam().getInteger("height");
    }

    @Override
    public boolean checkCondition() {
        return Rnd.chance(_template.chance(100));
    }

    @Override
    public void onStart() {
        List<Creature> _around = getEffected().getAroundCharacters(radius, height);

        if (_around.isEmpty()) {
            return;
        }

        _around.remove(getEffector());

        if (_around.isEmpty()) {
            return;
        }

        Creature target = _around.get(Rnd.get(_around.size()));

        getEffected().setTarget(target);

    }

    @Override
    public boolean onActionTime() {
        return false;
    }

    @Override
    public boolean isHidden() {
        return true;
    }

}
