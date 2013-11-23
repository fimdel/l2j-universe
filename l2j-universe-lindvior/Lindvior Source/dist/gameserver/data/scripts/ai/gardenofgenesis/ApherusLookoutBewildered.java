/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package ai.gardenofgenesis;

import l2p.commons.util.Rnd;
import l2p.gameserver.ai.Fighter;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.utils.Location;
import l2p.gameserver.utils.NpcUtils;

public class ApherusLookoutBewildered extends Fighter {
    public ApherusLookoutBewildered(NpcInstance actor) {
        super(actor);
    }

    @Override
    protected void onEvtDead(Creature killer) {
        super.onEvtDead(killer);
        NpcInstance actor = getActor();
        if (actor != null && killer != null && actor != killer) {
            NpcUtils.spawnSingle(19002, new Location(killer.getX() - Rnd.get(40), killer.getY() - Rnd.get(40), killer.getZ(), 0));
            NpcUtils.spawnSingle(19001, new Location(killer.getX() - Rnd.get(40), killer.getY() - Rnd.get(40), killer.getZ(), 0));
            NpcUtils.spawnSingle(19002, new Location(killer.getX() - Rnd.get(40), killer.getY() - Rnd.get(40), killer.getZ(), 0));
        }
    }
}