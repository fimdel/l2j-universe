/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package ai;

import l2p.gameserver.ai.DefaultAI;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.instances.NpcInstance;

import java.util.ArrayList;
import java.util.List;

/**
 * При спавне саммонят случайную охрану.
 * private static final int Mob1 = 23012;
 * private static final int Mob2 = 23010;
 * private static final int Mob3 = 23011;
 */
public class CrystalHallWaveAI extends DefaultAI {
    private static final int MOBS_Delay = 10 * 1000;
    private long spawnTime = 10 * 1000;

    private static final int[] MOBS = {23010,
            23011,
            23012
    };

    private List<NpcInstance> _npcs = new ArrayList<NpcInstance>();

    public CrystalHallWaveAI(NpcInstance actor) {
        super(actor);
        actor.startImmobilized();
    }

    @Override
    protected void onEvtDead(Creature killer) {
        spawnTime = 0;
        _npcs.clear();
        super.onEvtDead(killer);
    }

    @Override
    protected boolean thinkActive() {
        NpcInstance actor = getActor();
        if (spawnTime + MOBS_Delay < System.currentTimeMillis()) {
            if (_npcs.size() < 10)
                for (int id : MOBS) {
                    NpcInstance mob = actor.getReflection().addSpawnWithoutRespawn(id, actor.getLoc(), 300);
                    _npcs.add(mob);
                }
            spawnTime = System.currentTimeMillis();
            return true;
        }
        return true;
    }

    @Override
    protected void onEvtAttacked(Creature attacker, int damage) {
    }

    @Override
    protected void onEvtAggression(Creature target, int aggro) {
    }
}
