package ai.hermunkus_message;

import l2p.gameserver.ai.CtrlIntention;
import l2p.gameserver.ai.Fighter;
import l2p.gameserver.model.AggroList;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.instances.NpcInstance;
import org.apache.commons.lang3.ArrayUtils;


public class Tentacle_19171 extends Fighter {
    private static final int[] ATTACK_IDS = {
            19191, 19192, 19193, 19196, 19197,
            19198, 19199, 19200, 19201, 19202,
            19203, 19204, 19205, 19206, 19207,
            19208, 19209, 19210, 19211, 19212,
            19213, 19214, 19215
    };

    public Tentacle_19171(NpcInstance actor) {
        super(actor);
        AI_TASK_ATTACK_DELAY = 10;
    }

    @Override
    protected boolean canAttackCharacter(Creature target) {
        NpcInstance actor = getActor();
        if (getIntention() == CtrlIntention.AI_INTENTION_ATTACK) {
            AggroList.AggroInfo ai = actor.getAggroList().get(target);
            return ai != null && ai.hate > 0;
        }
        return target.isPlayable() || ArrayUtils.contains(ATTACK_IDS, target.getNpcId());
    }

    @Override
    public boolean checkAggression(Creature target) {
        if (getIntention() != CtrlIntention.AI_INTENTION_ACTIVE || !isGlobalAggro())
            return false;

        if (target.isNpc() && !ArrayUtils.contains(ATTACK_IDS, target.getNpcId()))
            return false;

        return super.checkAggression(target);
    }

    @Override
    protected void onEvtDead(Creature killer) {
        super.onEvtDead(killer);
        if (getActor().getParameter("notifyDie", false)) {
            broadCastScriptEvent("TENTACLE_DIE", 600);
        }
    }
}
