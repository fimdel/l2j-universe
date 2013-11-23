/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.ai;

import l2p.gameserver.model.Creature;
import l2p.gameserver.model.instances.NpcInstance;

public class NpcFighter extends Fighter {
    public NpcFighter(NpcInstance actor) {
        super(actor);
    }

    @Override
    protected void onEvtAttacked(Creature attacker, int damage) {
    }
}
