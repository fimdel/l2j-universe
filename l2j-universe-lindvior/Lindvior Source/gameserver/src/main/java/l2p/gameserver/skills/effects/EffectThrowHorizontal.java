package l2p.gameserver.skills.effects;

import l2p.gameserver.model.Effect;
import l2p.gameserver.network.serverpackets.FlyToLocation;
import l2p.gameserver.network.serverpackets.FlyToLocation.FlyType;
import l2p.gameserver.network.serverpackets.ValidateLocation;
import l2p.gameserver.stats.Env;
import l2p.gameserver.utils.Location;

public class EffectThrowHorizontal extends Effect {
    private Location location;

    public EffectThrowHorizontal(Env env, EffectTemplate template) {
        super(env, template);
    }

    @Override
    public void onStart() {
        super.onStart();

        if (!_effected.isPlayer())
            return;

        location = new Location(getEffector().getX(), getEffector().getY(), getEffector().getZ());

        getEffector().broadcastPacket(new FlyToLocation(getEffected(), location, FlyType.THROW_HORIZONTAL, getSkill().getFlySpeed()));
        getEffected().setLoc(location);
        getEffected().broadcastPacket(new ValidateLocation(getEffected()));
    }

    @Override
    public boolean onActionTime() {
        return false;
    }

}