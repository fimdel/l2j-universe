/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.skills.effects;

import l2p.gameserver.model.Effect;
import l2p.gameserver.model.Player;
import l2p.gameserver.stats.Env;

public class EffectVitality extends Effect {

    public EffectVitality(Env env, EffectTemplate template) {
        super(env, template);
    }

    @Override
    public boolean checkCondition() {
        if (!_effected.isPlayer())
            return false;

        Player p = _effected.getPlayer();

        if (p.getVitality().getItems() == 0)
            return false;

        p.getVitality().decItems();

        return super.checkCondition();
    }

    @Override
    public boolean onActionTime() {
        return true;
    }

}
