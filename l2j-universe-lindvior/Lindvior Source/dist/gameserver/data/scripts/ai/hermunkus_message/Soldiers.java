package ai.hermunkus_message;

import l2p.gameserver.ai.CtrlIntention;
import l2p.gameserver.ai.Fighter;
import l2p.gameserver.model.AggroList;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.instances.NpcInstance;
import org.apache.commons.lang3.ArrayUtils;

public class Soldiers extends Fighter {
    private static final int[] ATTACK_IDS = {19171, 19172};

    public Soldiers(NpcInstance actor) {
        super(actor);
        AI_TASK_ATTACK_DELAY = 10;
    }

    @Override
    public int getMaxAttackTimeout() {
        return 0;
    }

    @Override
    protected boolean canAttackCharacter(Creature target) {
        NpcInstance actor = getActor();
        if (getIntention() == CtrlIntention.AI_INTENTION_ATTACK) {
            AggroList.AggroInfo ai = actor.getAggroList().get(target);
            return ai != null && ai.hate > 0;
        }
        return ArrayUtils.contains(ATTACK_IDS, target.getNpcId());
    }

    @Override
    public boolean checkAggression(Creature target) {
        if (getIntention() != CtrlIntention.AI_INTENTION_ACTIVE || !isGlobalAggro())
            return false;

        if (target.isNpc() && !ArrayUtils.contains(ATTACK_IDS, target.getNpcId()))
            return false;

        return super.checkAggression(target);
    }

}
