/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package ai.TI;

import l2p.gameserver.ThreadPoolManager;
import l2p.gameserver.ai.CtrlEvent;
import l2p.gameserver.ai.Fighter;
import l2p.gameserver.model.World;
import l2p.gameserver.model.instances.NpcInstance;

public class ScarecrowArena extends Fighter {
    private NpcInstance target = null;

    public ScarecrowArena(NpcInstance actor) {
        super(actor);
    }

    @Override
    protected void onEvtSpawn() {
        ThreadPoolManager.getInstance().schedule(new AggressionTask(), 1000L);
    }

    @Override
    public boolean isGlobalAI() {
        return true;
    }

    private class AggressionTask
            implements Runnable {
        private AggressionTask() {
        }

        @Override
        public void run() {
            NpcInstance actor = getActor();
            if (target == null) {
                for (NpcInstance npc : World.getAroundNpc(actor, 300, 100)) {
                    if (npc.getNpcId() == 33201) {
                        if (target == null || actor.getDistance3D(npc) < actor.getDistance3D(target)) {
                            target = npc;
                        }
                    }
                }
            }

            if (target != null)
                target.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, actor, 5000);
            else
                ThreadPoolManager.getInstance().schedule(new AggressionTask(), 1000L);
        }
    }
}
