/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package instances;

import l2p.commons.threading.RunnableImpl;
import l2p.gameserver.ThreadPoolManager;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.entity.Reflection;
import l2p.gameserver.network.serverpackets.SystemMessage2;
import l2p.gameserver.network.serverpackets.components.SystemMsg;
import l2p.gameserver.utils.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Bergamo extends Reflection {
    private static final Logger _log = LoggerFactory.getLogger(Bergamo.class);
    private static final int Bergamo = 33692;

    @Override
    public void onPlayerEnter(Player player) {
        super.onPlayerEnter(player);
        player.sendPacket(new SystemMessage2(SystemMsg.THIS_DUNGEON_WILL_EXPIRE_IN_S1_MINUTES).addInteger(3));
        startCollapseTimer(3 * 60 * 1000L);
        ThreadPoolManager.getInstance().schedule(new BergamoSpawn(this), 1);
        ThreadPoolManager.getInstance().schedule(new BergamoExit(this), 3 * 60 * 1000L);
        setReenterTime(System.currentTimeMillis());
    }

    public class BergamoSpawn extends RunnableImpl {
        Reflection _r;

        public BergamoSpawn(Reflection r) {
            _r = r;
        }

        @Override
        public void runImpl() {
            _r.addSpawnWithoutRespawn(Bergamo, new Location(75432, -213416, -3738, 32767), 0);
            _r.addSpawnWithoutRespawn(33693, new Location(75063, -213640, -3738, 3242), 0);
            _r.addSpawnWithoutRespawn(33694, new Location(74888, -213512, -3738, 32767), 0);
            _r.addSpawnWithoutRespawn(33695, new Location(75000, -213352, -3738, 32767), 0);
            _r.addSpawnWithoutRespawn(19284, new Location(74760, -213816 - 3738, 42114), 0);
            _r.addSpawnWithoutRespawn(19285, new Location(74808, -213864, -3738, 57343), 0);
            _r.addSpawnWithoutRespawn(19285, new Location(74696, -213864, -3737, 32767), 0);
            _r.addSpawnWithoutRespawn(19285, new Location(74696, -213752, -3737, 16383), 0);
            _r.addSpawnWithoutRespawn(19284, new Location(74616, -213336, -3738, 15516), 0);
            _r.addSpawnWithoutRespawn(19285, new Location(74584, -213368, -3738, 40959), 0);
            _r.addSpawnWithoutRespawn(19285, new Location(74648, -213400, -3738, 60699), 0);
            _r.addSpawnWithoutRespawn(19285, new Location(74600, -213256, -3738, 19739), 0);
            _r.addSpawnWithoutRespawn(19284, new Location(74904, -213096, -3738, 2555), 0);
            _r.addSpawnWithoutRespawn(19285, new Location(74840, -213128, -3738, 37604), 0);
            _r.addSpawnWithoutRespawn(19285, new Location(74872, -213048, -3738, 12415), 0);
            _r.addSpawnWithoutRespawn(19285, new Location(75000, -213032, -3738, 1297), 0);
            _r.addSpawnWithoutRespawn(19284, new Location(75080, -213832, -3738, 62980), 0);
            _r.addSpawnWithoutRespawn(19285, new Location(74984, -213800, -3738, 29412), 0);
            _r.addSpawnWithoutRespawn(19285, new Location(75048, -213896, -3738, 55285), 0);
            _r.addSpawnWithoutRespawn(19285, new Location(75112, -213784, -3738, 10969), 0);
        }
    }

    public class BergamoExit extends RunnableImpl {
        Reflection _r;

        public BergamoExit(Reflection r) {
            _r = r;
        }

        @Override
        public void runImpl() {
            for (Player p : getPlayers()) {
                if (p.getVar("BerOpen") != null) {
                    p.unsetVar("BerOpen");
                }
            }
        }
    }
}
