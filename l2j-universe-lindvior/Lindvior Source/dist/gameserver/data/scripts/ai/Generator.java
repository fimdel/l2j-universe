package ai;

import l2p.gameserver.ai.DefaultAI;
import l2p.gameserver.model.instances.NpcInstance;

public class Generator extends DefaultAI {
    public Generator(NpcInstance actor) {
        super(actor);
        actor.startImmobilized();
    }

    @Override
    protected void onEvtSpawn() {
        super.onEvtSpawn();
    }

    @Override
    public boolean isGlobalAI() {
        return true;
    }
}