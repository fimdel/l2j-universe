package ai.harnak_4pf;

import l2p.gameserver.ai.DefaultAI;
import l2p.gameserver.instancemanager.ReflectionManager;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.network.serverpackets.ExStartScenePlayer;

public class Hermuncus extends DefaultAI {
    private final boolean LAST_SPAWN;

    public Hermuncus(NpcInstance actor) {
        super(actor);
        LAST_SPAWN = actor.getParameter("lastSpawn", false);
    }

    @Override
    protected void onEvtSpawn() {
        super.onEvtSpawn();
        if (!LAST_SPAWN)
            getActor().setNpcState(1);
    }

    @Override
    protected void onEvtMenuSelected(Player player, int ask, int reply) {
        if (ask == 10338 && reply == 2) {
            player.teleToLocation(-114962, 226564, -2864, ReflectionManager.DEFAULT);
            player.showQuestMovie(ExStartScenePlayer.SCENE_AWAKENING_VIEW);
        }
    }
}
