package l2p.gameserver.skills.effects;

import l2p.gameserver.Config;
import l2p.gameserver.geodata.GeoEngine;
import l2p.gameserver.model.Effect;
import l2p.gameserver.model.entity.Reflection;
import l2p.gameserver.network.serverpackets.FlyToLocation;
import l2p.gameserver.network.serverpackets.ValidateLocation;
import l2p.gameserver.stats.Env;
import l2p.gameserver.utils.Location;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Darvin
 * Date: 22.09.12
 * Time: 3:06
 */
public class EffectEnemyCharge extends Effect {

    static final Logger _log = LogManager.getLogger(EffectEnemyCharge.class);
    private Reflection r;

    public EffectEnemyCharge(Env env, EffectTemplate template) {
        super(env, template);
    }

    public void onStart() {
        int curX = getEffector().getX();
        int curY = getEffector().getY();
        int curZ = getEffector().getZ();
        double dx = getEffected().getX() - curX;
        double dy = getEffected().getY() - curY;
        double dz = getEffected().getZ() - curZ;
        double distance = Math.sqrt(dx * dx + dy * dy);
        if (distance > 2000.0D) {
            _log.log(Level.INFO, "EnemyCharge was going to use invalid coordinates for characters, getEffector: " + curX + "," + curY + " and getEffected: " + getEffected().getX() + "," + getEffected().getY());
            return;
        }
        int offset = Math.max((int) distance - getSkill().getFlyRadius(), 30);

        offset = (int) (offset - Math.abs(dz));
        if (offset < 5) {
            offset = 5;
        }

        if ((distance < 1.0D) || (distance - offset <= 0.0D)) {
            return;
        }

        double sin = dy / distance;
        double cos = dx / distance;
        int index = r.getGeoIndex();
        int _x = (curX + (int) ((distance - offset) * cos));
        int _y = (curY + (int) ((distance - offset) * sin));
        int _z = getEffected().getZ();

        if (Config.ALLOW_GEODATA) {
            Location destiny = GeoEngine.moveCheck(getEffector().getX(), getEffector().getY(), getEffector().getZ(), _x, _y, index);
            _x = destiny.getX();
            _y = destiny.getY();
        }
        getEffector().broadcastPacket(new FlyToLocation(getEffector(), _x, _y, _z, FlyToLocation.FlyType.CHARGE, getSkill().getFlySpeed()));

        getEffector().setXYZ(_x, _y, _z);
        getEffector().broadcastPacket(new ValidateLocation(getEffector()));
    }

    @Override
    protected boolean onActionTime() {
        return false;

    }
}
