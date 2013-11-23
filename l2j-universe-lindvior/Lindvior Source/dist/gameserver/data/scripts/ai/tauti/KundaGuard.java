package ai.tauti;

import l2p.commons.util.Rnd;
import l2p.gameserver.ai.CtrlEvent;
import l2p.gameserver.ai.Fighter;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.instances.NpcInstance;

import java.util.List;

/**
 * @author KilRoy
 */
public class KundaGuard extends Fighter {
    public KundaGuard(NpcInstance actor) {
        super(actor);
    }

    @Override
    protected boolean thinkActive() {
        NpcInstance actor = getActor();
        if (actor.isDead())
            return false;

        List<NpcInstance> around = actor.getAroundNpc(500, 300);
        if (around != null && !around.isEmpty() && Rnd.chance(40))
            for (NpcInstance npc : around)
                if (npc.getNpcId() == 33679 || npc.getNpcId() == 33680)
                    actor.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, npc, 300);
        return true;
    }

    @Override
    protected void onEvtAttacked(Creature attacker, int damage) {
        if (attacker == null)
            return;

        super.onEvtAttacked(attacker, damage);
    }

    @Override
    public int getMaxAttackTimeout() {
        return 2000;
    }

    @Override
    protected boolean maybeMoveToHome() {
        return false;
    }

    @Override
    protected boolean randomWalk() {
        return false;
    }
}