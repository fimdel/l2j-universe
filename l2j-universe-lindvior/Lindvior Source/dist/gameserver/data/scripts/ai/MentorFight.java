package ai;

import l2p.commons.util.Rnd;
import l2p.gameserver.ai.DefaultAI;
import l2p.gameserver.model.World;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.network.serverpackets.SocialAction;

public class MentorFight extends DefaultAI {
    private int[] lastAction = {5, 8, 9, 10};
    private int action;
    private int lastactionId = 0;

    public MentorFight(NpcInstance actor) {
        super(actor);
        AI_TASK_ACTIVE_DELAY = 2500;
    }

    @Override
    public boolean isGlobalAI() {
        return true;
    }

    @Override
    protected boolean thinkActive() {
        NpcInstance actor = getActor();
        if (actor == null)
            return true;

        if (lastactionId == 0) {
            action = lastAction[Rnd.get(0, 3)];
            actor.broadcastPacket(new SocialAction(actor.getObjectId(), action));
            lastactionId = 1;
        } else if (lastactionId == 1) {
            actor.broadcastPacket(new SocialAction(actor.getObjectId(), 6));
            lastactionId = 2;
        } else if (lastactionId == 2) {
            for (NpcInstance target : World.getAroundNpc(actor, 500, 200))
                if (target != null && target.getNpcId() == 33018)
                    target.broadcastPacket(new SocialAction(target.getObjectId(), action));
            lastactionId = 3;
        } else if (lastactionId == 3)
            lastactionId = 0;

        return true;
    }

    @Override
    protected boolean randomWalk() {
        return false;
    }
}