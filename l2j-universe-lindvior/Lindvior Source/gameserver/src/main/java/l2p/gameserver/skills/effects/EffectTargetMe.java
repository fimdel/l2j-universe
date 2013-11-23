/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.skills.effects;

import l2p.gameserver.model.Effect;
import l2p.gameserver.model.Playable;
import l2p.gameserver.network.serverpackets.MyTargetSelected;
import l2p.gameserver.stats.Env;

public class EffectTargetMe extends Effect {
    public EffectTargetMe(Env env, EffectTemplate template) {
        super(env, template);
    }

    @Override
    public void onStart() {
        if (getEffected() instanceof Playable) {
            if (getEffected().getTarget() != getEffector()) {
                if (getEffected().isPlayer())
                    getEffected().sendPacket(new MyTargetSelected(getEffected().getPlayer(), getEffector()));
                getEffected().setTarget(getEffector());
            }
        }
        getEffected().setLockedTarget(true);
        super.onStart();
    }

    @Override
    public void onExit() {
        if (getEffected() instanceof Playable)
            (getEffected()).setLockedTarget(false);
        super.onExit();
    }

    @Override
    public boolean onActionTime() {
        return false;
    }
}
