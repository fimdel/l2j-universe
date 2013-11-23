package l2p.gameserver.skills.effects;

import l2p.gameserver.model.Effect;
import l2p.gameserver.network.serverpackets.FlyToLocation;
import l2p.gameserver.stats.Env;
import l2p.gameserver.utils.Location;

public class EffectTargetToMe extends Effect {
    public EffectTargetToMe(Env env, EffectTemplate template) {
        super(env, template);
    }

    @Override
    public void onStart() {
        super.onStart();
        Location flyLoc = _effected.getFlyLocation(getEffector(), getSkill());
        if (flyLoc != null) {
            _effected.setLoc(flyLoc);
            _effected.broadcastPacket(new FlyToLocation(_effected, flyLoc, getSkill().getFlyType(), 0));
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
