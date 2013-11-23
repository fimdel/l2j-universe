package ai.dragonvalley;

import l2p.gameserver.ai.Fighter;
import l2p.gameserver.model.instances.NpcInstance;

/**
 * @author pchayka
 */
public class DragonRaid extends Fighter {
    private long lastAttackTime = 0;

    public DragonRaid(NpcInstance actor) {
        super(actor);
    }

    @Override
    protected void onEvtSpawn() {
        super.onEvtSpawn();
        lastAttackTime = System.currentTimeMillis();
    }

    @Override
    protected boolean thinkActive() {
        super.thinkActive();
        if (lastAttackTime != 0 && lastAttackTime + 30 * 60 * 1000L < System.currentTimeMillis())
            getActor().deleteMe();
        return true;
    }
}