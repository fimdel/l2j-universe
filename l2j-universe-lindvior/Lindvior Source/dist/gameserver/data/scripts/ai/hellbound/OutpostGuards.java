package ai.hellbound;

import l2p.gameserver.ai.Fighter;
import l2p.gameserver.model.instances.NpcInstance;

public class OutpostGuards extends Fighter {
    public OutpostGuards(NpcInstance actor) {
        super(actor);
        actor.startImmobilized();
    }
}