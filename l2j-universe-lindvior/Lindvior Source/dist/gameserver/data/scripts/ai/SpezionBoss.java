/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package ai;

import l2p.commons.threading.RunnableImpl;
import l2p.gameserver.ThreadPoolManager;
import l2p.gameserver.ai.CtrlEvent;
import l2p.gameserver.ai.Fighter;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.Skill;
import l2p.gameserver.model.entity.Reflection;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.tables.SkillTable;

import java.util.concurrent.ScheduledFuture;

/**
 * @author cruel
 */
public class SpezionBoss extends Fighter {
    private ScheduledFuture<?> DeadTask;

    public SpezionBoss(NpcInstance actor) {
        super(actor);
    }

    @Override
    public boolean isGlobalAI() {
        return true;
    }

    @Override
    protected void onEvtSpawn() {
        NpcInstance actor = getActor();
        DeadTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new SpawnMinion(), 1000, 30000);
        Reflection r = actor.getReflection();
        for (Player p : r.getPlayers())
            notifyEvent(CtrlEvent.EVT_AGGRESSION, p, 2);
        super.onEvtSpawn();
        Skill fp = SkillTable.getInstance().getInfo(14190, 1);
        fp.getEffects(actor, actor, false, false);
    }

    @Override
    protected void onEvtDead(Creature killer) {
        if (DeadTask != null)
            DeadTask.cancel(true);

        super.onEvtDead(killer);
    }

    public class SpawnMinion extends RunnableImpl {
        @Override
        public void runImpl() {
            NpcInstance actor = getActor();
            NpcInstance minion = actor.getReflection().addSpawnWithoutRespawn(25780, actor.getLoc(), 250);
            for (Player p : actor.getReflection().getPlayers())
                minion.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, p, 2);
        }
    }
}
