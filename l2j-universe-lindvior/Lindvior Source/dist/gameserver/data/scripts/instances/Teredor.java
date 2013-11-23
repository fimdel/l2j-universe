package instances;

import l2p.gameserver.model.entity.Reflection;
import l2p.gameserver.utils.Location;

/**
 * Класс контролирует инстанс Teredor
 * User: Darvin
 * Date: 16.10.12
 * Time: 22:59
 */
public class Teredor extends Reflection {
    private static int teredor = 25785;
    private Location teredorCoords = new Location(176160, -185200, -3800);

    @Override
    protected void onCreate() {
        super.onCreate();
        this.addSpawnWithoutRespawn(teredor, teredorCoords, 0);
    }

}
