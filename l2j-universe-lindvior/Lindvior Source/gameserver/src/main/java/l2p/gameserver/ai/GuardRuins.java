package l2p.gameserver.ai;


import l2p.gameserver.model.Creature;
import l2p.gameserver.model.instances.NpcInstance;

import java.util.List;


public class GuardRuins extends Fighter {
    public GuardRuins(NpcInstance actor) {
        super(actor);
    }

    @Override
    protected boolean thinkActive() {
        NpcInstance actor = getActor();
        if (actor.isDead())
            return false;

        List<NpcInstance> around = actor.getAroundNpc(500, 300);
        if (around != null && !around.isEmpty())
            for (NpcInstance npc : around)
                if (npc.isMonster() && npc.getNpcId() != 19153 && npc.getNpcId() != 19152)
                    actor.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, npc, 300);
        return true;
    }

    @Override
    protected void onEvtAttacked(Creature attacker, int damage) {
        if (attacker == null || attacker.isPlayable())
            return;

        super.onEvtAttacked(attacker, damage);
    }
}
