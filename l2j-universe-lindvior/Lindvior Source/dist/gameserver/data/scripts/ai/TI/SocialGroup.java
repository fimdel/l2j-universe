package ai.TI;

import l2p.commons.lang.ArrayUtils;
import l2p.commons.util.Rnd;
import l2p.gameserver.ThreadPoolManager;
import l2p.gameserver.ai.Fighter;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.network.serverpackets.SocialAction;

import java.util.List;

public class SocialGroup extends Fighter {
    //33007
    private static final int[] groups = {33018, 33000};
    private long _wait_timeout = 0;

    public SocialGroup(NpcInstance actor) {
        super(actor);
        AI_TASK_ACTIVE_DELAY = 1000;
    }

    @Override
    public boolean thinkActive() {
        NpcInstance actor = getActor();

        if (System.currentTimeMillis() > _wait_timeout) {
            _wait_timeout = System.currentTimeMillis() + Rnd.get(10, 30) * 1000L;
            List<NpcInstance> around = actor.getAroundNpc(700, 100);
            if (around != null && !around.isEmpty()) {
                actor.broadcastPacket(new SocialAction(actor.getObjectId(), 3));
                for (final NpcInstance group : around)
                    if (ArrayUtils.contains(groups, group.getNpcId()))
                        ThreadPoolManager.getInstance().schedule(new Runnable() {
                            @Override
                            public void run() {
                                group.broadcastPacket(new SocialAction(group.getObjectId(), 3));
                            }
                        }, 2000);
            }
        }
        return false;
    }
}