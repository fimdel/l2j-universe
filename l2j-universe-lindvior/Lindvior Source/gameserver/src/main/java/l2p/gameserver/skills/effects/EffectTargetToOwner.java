package l2p.gameserver.skills.effects;

import l2p.gameserver.model.Effect;
import l2p.gameserver.model.instances.SymbolInstance;
import l2p.gameserver.network.serverpackets.FlyToLocation;
import l2p.gameserver.stats.Env;
import l2p.gameserver.utils.Location;

/**
 * Created with IntelliJ IDEA.
 * User: Darvin
 * Date: 30.06.12
 * Time: 9:37
 */
public class EffectTargetToOwner extends Effect {
    public EffectTargetToOwner(Env env, EffectTemplate template) {
        super(env, template);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (((SymbolInstance) getEffector()).getOwner().getObjectId() != _effected.getObjectId()) {
            Location flyLoc = _effected.getFlyLocation(((SymbolInstance) getEffector()).getOwner(), getSkill());
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
