/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package ai.custom;

import l2p.gameserver.ai.DefaultAI;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.instances.NpcInstance;

public class CorpsRuin extends DefaultAI {
    public CorpsRuin(NpcInstance actor) {
        super(actor);
    }

    protected void onEvtDead(Creature killer) {
        return;
    }
}
