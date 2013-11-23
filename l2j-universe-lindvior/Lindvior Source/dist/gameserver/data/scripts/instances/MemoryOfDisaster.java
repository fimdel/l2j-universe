/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package instances;

import l2p.commons.threading.RunnableImpl;
import l2p.commons.util.Rnd;
import l2p.gameserver.ThreadPoolManager;
import l2p.gameserver.ai.CtrlEvent;
import l2p.gameserver.instancemanager.ReflectionManager;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.entity.Reflection;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.model.quest.QuestState;
import l2p.gameserver.network.serverpackets.EventTrigger;
import l2p.gameserver.network.serverpackets.ExShowScreenMessage;
import l2p.gameserver.network.serverpackets.components.NpcString;
import l2p.gameserver.network.serverpackets.components.SceneMovie;
import l2p.gameserver.utils.Location;

import java.util.ArrayList;
import java.util.List;

public class MemoryOfDisaster extends Reflection {
    private static final int ROGIN_ID = 19193;
    private static final int TENTACLE_ID = 19171;
    private static final int TRASNPERENT_TEREDOR_1_ID = 18998;
    private static final int EARTH_WYRM_ID = 19217;
    private static final int TRANSPARENT = 18919;
    private static final int[] ELVES = {33536, 33538, 33540, 33542, 33544, 33546};

    private boolean _introShowed = false;

    public MemoryOfDisaster(Player player) {
        setReturnLoc(player.getLoc());
    }

    @Override
    public void onPlayerEnter(final Player player) {
        if (!_introShowed) {
            _introShowed = true;
            for (Player p : getPlayers()) {
                p.startScenePlayer(SceneMovie.sc_awakening_opening);
                p.sendPacket(new EventTrigger(23120700, true));
            }
            ThreadPoolManager.getInstance().schedule(new TeleportTask(), 29500L);
        }
        super.onPlayerEnter(player);
    }

    @Override
    public void onPlayerExit(Player player) {
        player.setVar("GermunkusUSM", "true", -1);
        player.sendPacket(new EventTrigger(23120700, false));
        super.onPlayerExit(player);
    }

    public void spawnTentacles() {
        addSpawnWithoutRespawn(TENTACLE_ID, new Location(116596, -183176, -1608, 31175), 0).setParameter("notifyDie", true);
        addSpawnWithoutRespawn(TENTACLE_ID, new Location(116542, -183126, -1600, 52580), 0).setParameter("notifyDie", true);
        addSpawnWithoutRespawn(TENTACLE_ID, new Location(116542, -183189, -1608, 9413), 0).setParameter("notifyDie", true);
    }

    public void spawnTransparentTeredor() {
        addSpawnWithoutRespawn(TRASNPERENT_TEREDOR_1_ID, new Location(116511, -178729, -1176, 43905), 0);
    }

    public void spawnWyrm() {
        addSpawnWithoutRespawn(TRANSPARENT, new Location(116511, -178729, -1176, 50366), 0);
        addSpawnWithoutRespawn(EARTH_WYRM_ID, new Location(116511, -178729, -1176, 50366), 0);
    }

    public void startFinalScene() {
        for (Player player : getPlayers()) {
            player.startScenePlayer(SceneMovie.sc_awakening_opening_c);
        }
        ThreadPoolManager.getInstance().schedule(new Scene1(), 32900);
    }

    public void dieNextElf() {
        List<NpcInstance> elves = new ArrayList<NpcInstance>();
        for (int id : ELVES) {
            elves.addAll(getAllByNpcId(id, true));
        }

        if (!elves.isEmpty()) {
            elves.get(Rnd.get(elves.size())).getAI().notifyEvent(CtrlEvent.EVT_SCRIPT_EVENT, "START_DIE", "empty", "empty");
        }
    }

    private class TeleportTask extends RunnableImpl {
        @Override
        public void runImpl() throws Exception {
            for (Player player : getPlayers()) {
                player.teleToLocation(new Location(116063, -183167, -1488, 64960));
                player.sendPacket(new EventTrigger(23120700, true));
            }
            ThreadPoolManager.getInstance().schedule(new ScreenMessageTask(NpcString.WATCH_THE_DWARVEN_VILLAGE_LAST_STAND), 2000);
            ThreadPoolManager.getInstance().schedule(new SpawnRoginTask(), 7000);
        }
    }

    private class ScreenMessageTask extends RunnableImpl {
        private NpcString msg;

        public ScreenMessageTask(NpcString msg) {
            this.msg = msg;
        }

        @Override
        public void runImpl() throws Exception {
            for (Player player : getPlayers()) {
                player.sendPacket(new ExShowScreenMessage(msg, 5000, ExShowScreenMessage.ScreenMessageAlign.MIDDLE_CENTER, true));
            }
        }
    }

    private class SpawnRoginTask extends RunnableImpl {
        @Override
        public void runImpl() throws Exception {
            addSpawnWithRespawn(ROGIN_ID, new Location(115731, -183054, -1472, 3170), 0, 0);
        }
    }

    private class Scene1 extends RunnableImpl {
        @Override
        public void runImpl() throws Exception {
            for (Player player : getPlayers()) {
                player.teleToLocation(new Location(10400, 17092, -4584, 44839));
                player.startScenePlayer(SceneMovie.sc_awakening_opening_d);
            }
            dieNextElf();
            dieNextElf();
            dieNextElf();
            dieNextElf();
            ThreadPoolManager.getInstance().schedule(new Scene2(), 83000);
        }
    }

    private class Scene2 extends RunnableImpl {
        @Override
        public void runImpl() throws Exception {
            for (Player player : getPlayers()) {
                player.startScenePlayer(SceneMovie.sc_awakening_opening_e);
            }
            ThreadPoolManager.getInstance().schedule(new Scene3(), 30000);
        }
    }

    private class Scene3 extends RunnableImpl {
        @Override
        public void runImpl() throws Exception {
            for (Player player : getPlayers()) {
                player.startScenePlayer(SceneMovie.sc_awakening_opening_f);
            }
            ThreadPoolManager.getInstance().schedule(new EndInstanceTask(), 37500);
        }
    }

    private class EndInstanceTask extends RunnableImpl {
        @Override
        public void runImpl() throws Exception {
            for (Player player : getPlayers()) {
                player.teleToLocation(getReturnLoc(), ReflectionManager.DEFAULT);
                ThreadPoolManager.getInstance().schedule(new ShowHtmlTask(player), 1500);
            }
        }
    }

    private class ShowHtmlTask extends RunnableImpl {
        private Player player;

        public ShowHtmlTask(Player player) {
            this.player = player;
        }

        @Override
        public void runImpl() throws Exception {
            if (player == null)
                return;

            QuestState st = player.getQuestState("_255_Tutorial");
            if (st != null) {
                st.showTutorialHTML("hermunkus_call.htm");
            }
        }
    }
}
