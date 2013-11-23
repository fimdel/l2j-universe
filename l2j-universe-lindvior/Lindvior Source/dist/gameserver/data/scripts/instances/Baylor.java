/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package instances;

import l2p.commons.threading.RunnableImpl;
import l2p.gameserver.ThreadPoolManager;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.entity.Reflection;
import l2p.gameserver.utils.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Baylor extends Reflection {
    private static final Logger _log = LoggerFactory.getLogger(Baylor.class);
    private static final int Baylor = 29213;
    private static final int Golem1 = 23123;
    private static final int Golem2 = 23123;
    private static final int Golem3 = 23123;
    private static final int Golem4 = 23123;
    //private static final int Door =
    private Location Golem1Loc = new Location(152648, 142968, -12762);
    private Location Golem2Loc = new Location(152664, 141160, -12762);
    private Location Golem3Loc = new Location(154488, 141160, -12762);
    private Location Golem4Loc = new Location(154488, 143000, -12762);
    private Location spawn1 = new Location(153256, 142056, -12762, 0);
    private Location spawn2 = new Location(153912, 142088, -12762, 32767);
    private static final long BeforeDelay = 60 * 1000L;
    private static final long BeforeDelayVDO = 47 * 1000L;

    @Override
    public void onPlayerEnter(Player player) {
        super.onPlayerEnter(player);
        //ThreadPoolManager.getInstance().schedule(new FirstStage(), BeforeDelayVDO);
        ThreadPoolManager.getInstance().schedule(new BaylorSpawn(this), BeforeDelay);
    }
    //ExStartScenePlayer.SCENE_BALROG_OPENING
    //player.showQuestMovie(SceneMovie.si_barlog_opening);
    //private class FirstStage extends RunnableImpl
    //{
    //@Override
    //public void runImpl() throws Exception
    //{

    //closeDoor(Door);

    //for(Player player : getPlayers())
    //player.showQuestMovie(ExStartScenePlayer.SCENE_SC_NOBLE_OPENING);
    //_log.info("43 Video");

    //}
    //}
    public class BaylorSpawn extends RunnableImpl {
        Reflection _r;

        public BaylorSpawn(Reflection r) {
            _r = r;
        }

        @Override
        public void runImpl() {
            Location Loc1 = Golem1Loc;
            Location Loc2 = Golem2Loc;
            Location Loc3 = Golem3Loc;
            Location Loc4 = Golem4Loc;
            Location Loc = spawn1;
            Location Loc5 = spawn2;
            _r.addSpawnWithoutRespawn(Baylor, Loc, 0);
            _r.addSpawnWithoutRespawn(Baylor, Loc5, 0);
            _r.addSpawnWithoutRespawn(Golem1, Loc1, 0);
            _r.addSpawnWithoutRespawn(Golem2, Loc2, 0);
            _r.addSpawnWithoutRespawn(Golem3, Loc3, 0);
            _r.addSpawnWithoutRespawn(Golem4, Loc4, 0);
        }
    }
}