package ai.TI;

import l2p.gameserver.ai.CtrlIntention;
import l2p.gameserver.ai.Fighter;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.instances.NpcInstance;

/**
 * @author KilRoy
 *         Guard use, for attacking Maneken.
 */
public class GuardManeken extends Fighter {
    private int Manaken = 33023;

    public GuardManeken(NpcInstance actor) {
        super(actor);
        AI_TASK_ACTIVE_DELAY = 5000;
    }

    @Override
    public boolean checkAggression(Creature target) {
        NpcInstance actor = getActor();

        if (target.getNpcId() != Manaken)
            return false;

        if (getIntention() != CtrlIntention.AI_INTENTION_ATTACK) {
            actor.getAggroList().addDamageHate(target, 0, 1);
            setIntention(CtrlIntention.AI_INTENTION_ATTACK, target);
        }

        return super.checkAggression(target);
    }

    @Override
    protected boolean randomWalk() {
        return false;
    }
}
