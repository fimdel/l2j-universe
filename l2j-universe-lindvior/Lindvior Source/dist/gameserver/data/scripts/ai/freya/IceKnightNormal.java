package ai.freya;

import l2p.commons.threading.RunnableImpl;
import l2p.gameserver.ThreadPoolManager;
import l2p.gameserver.ai.CtrlEvent;
import l2p.gameserver.ai.Fighter;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.entity.Reflection;
import l2p.gameserver.model.instances.NpcInstance;

import java.util.concurrent.ScheduledFuture;

/**
 * @author pchayka
 */

public class IceKnightNormal extends Fighter {
    private boolean iced;
    private ScheduledFuture<?> task;

    public IceKnightNormal(NpcInstance actor) {
        super(actor);
        MAX_PURSUE_RANGE = 6000;
    }

    @Override
    protected void onEvtSpawn() {
        super.onEvtSpawn();
        NpcInstance actor = getActor();
        iced = true;
        actor.setNpcState(1);
        actor.block();
        Reflection r = actor.getReflection();
        if (r != null && r.getPlayers() != null)
            for (Player p : r.getPlayers())
                this.notifyEvent(CtrlEvent.EVT_AGGRESSION, p, 300);

        task = ThreadPoolManager.getInstance().schedule(new ReleaseFromIce(), 6000L);
    }

    @Override
    protected void onEvtAttacked(Creature attacker, int damage) {
        NpcInstance actor = getActor();

        if (iced) {
            iced = false;
            if (task != null)
                task.cancel(false);
            actor.unblock();
            actor.setNpcState(2);
        }
        super.onEvtAttacked(attacker, damage);
    }

    private class ReleaseFromIce extends RunnableImpl {
        @Override
        public void runImpl() {
            if (iced) {
                iced = false;
                getActor().setNpcState(2);
                getActor().unblock();
            }
        }
    }

    @Override
    protected void teleportHome() {
    }
}