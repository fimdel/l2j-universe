package l2p.gameserver.skills.effects;

import l2p.gameserver.model.Effect;
import l2p.gameserver.network.serverpackets.FlyToLocation;
import l2p.gameserver.network.serverpackets.FlyToLocation.FlyType;
import l2p.gameserver.network.serverpackets.ValidateLocation;
import l2p.gameserver.stats.Env;
import l2p.gameserver.utils.Location;

public class EffectKnockDown extends Effect {
    private int _x, _y, _z;

    public EffectKnockDown(Env env, EffectTemplate template) {
        super(env, template);
    }

    @Override
    public void onStart() {
        super.onStart();

        Location playerLoc = _effected.getLoc();
        Location tagetLoc = getEffector().getLoc();

        double distance = playerLoc.distance(tagetLoc);

        if (distance > 2000 || distance < 1) {
            return;
        }

        double dx = tagetLoc.x - playerLoc.x;
        double dy = tagetLoc.y - playerLoc.y;
        double dz = tagetLoc.z - playerLoc.z;

        int offset = Math.min((int) distance + getSkill().getFlyRadius(), 1400);
        offset = (int) (offset + Math.abs(dz));

        if (offset < 5) {
            offset = 5;
        }

        double sin = dy / distance;
        double cos = dx / distance;

        _x = (tagetLoc.x - (int) (offset * cos));
        _y = (tagetLoc.y - (int) (offset * sin));
        _z = tagetLoc.z;

        _effected.startKnockDown();
        _effected.broadcastPacket(new FlyToLocation(_effected, new Location(_x, _y, _z), FlyType.PUSH_DOWN_HORIZONTAL, getSkill().getFlySpeed()));
        _effected.setXYZ(this._x, this._y, this._z);
        _effected.broadcastPacket(new ValidateLocation(_effected));
    }

    @Override
    public void onExit() {
        super.onExit();
        _effected.setXYZ(_x, _y, _z);
        _effected.broadcastPacket(new ValidateLocation(_effected));
        _effected.stopKnockDown();
    }

    @Override
    public boolean onActionTime() {
        return false;
    }

}
