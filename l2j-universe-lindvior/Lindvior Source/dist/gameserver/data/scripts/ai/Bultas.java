/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package ai;

import l2p.gameserver.ai.CtrlEvent;
import l2p.gameserver.ai.Fighter;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.instances.NpcInstance;

import java.util.List;

public class Bultas extends Fighter {
    public Bultas(NpcInstance actor) {
        super(actor);
    }

    @Override
    protected boolean thinkActive() {
        NpcInstance actor = getActor();
        if (actor.isDead())
            return false;

        List<NpcInstance> around = actor.getAroundNpc(2000, 1000);
        if (around != null && !around.isEmpty())
            for (NpcInstance npc : around)
                if (npc.getNpcId() != 19128 && npc.getNpcId() != 19129 && npc.getNpcId() != 19130 && npc.getNpcId() != 19131 && npc.getNpcId() != 19132 && npc.getNpcId() != 19133 && npc.getNpcId() != 19134 && npc.getNpcId() != 19135 && npc.getNpcId() != 19136 && npc.getNpcId() != 19137 && npc.getNpcId() != 19138 && npc.getNpcId() != 19139 && npc.getNpcId() != 19140)
                    actor.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, npc, 3);
        return true;
    }

    @Override
    protected void onEvtAttacked(Creature attacker, int damage) {
        if (attacker == null || attacker.isPlayable())
            return;

        super.onEvtAttacked(attacker, damage);
    }
}
