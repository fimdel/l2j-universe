package l2p.gameserver.ai;

import l2p.gameserver.model.AggroList.AggroInfo;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.instances.MonsterInstance;
import l2p.gameserver.model.instances.NpcInstance;

public class Guard extends Fighter {
    public Guard(NpcInstance actor) {
        super(actor);
    }

    @Override
    public boolean canAttackCharacter(Creature target) {
        NpcInstance actor = getActor();
        if (getIntention() == CtrlIntention.AI_INTENTION_ATTACK) {
            AggroInfo ai = actor.getAggroList().get(target);
            return ai != null && ai.hate > 0;
        }
        return target.isMonster() || target.isPlayable();
    }

    @Override
    public boolean checkAggression(Creature target) {
        NpcInstance actor = getActor();
        if (getIntention() != CtrlIntention.AI_INTENTION_ACTIVE || !isGlobalAggro())
            return false;

        if (target.isPlayable()) {
            if (target.getKarma() >= 0 || (actor.getParameter("evilGuard", false) && target.getPvpFlag() > 0))
                return false;
        }
        if (target.isNpc()) {
            if (((NpcInstance) target).isInFaction(actor))
                return false;
            if (target.isMonster()) {
                if (!((MonsterInstance) target).isAggressive())
                    return false;
            }
        }

        return super.checkAggression(target);
    }

    @Override
    public int getMaxAttackTimeout() {
        return 0;
    }

    @Override
    protected boolean randomWalk() {
        return false;
    }
}