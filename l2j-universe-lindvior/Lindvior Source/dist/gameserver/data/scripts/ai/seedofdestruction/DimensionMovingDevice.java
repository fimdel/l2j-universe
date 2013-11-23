package ai.seedofdestruction;

import l2p.gameserver.ai.DefaultAI;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.instances.NpcInstance;

import java.util.ArrayList;
import java.util.List;

/**
 * AI Dimension Moving Device в Seed of Destruction:
 * <p/>
 * Из ловушки спаунятся мобы с задержкой в 3 сек через 5 сек после спауна в следующей последовательности:
 * Dragon Steed Troop Commander
 * White Dragon Leader
 * Dragon Steed Troop Healer (not off-like)
 * Dragon Steed Troop Magic Leader
 * Dragon Steed Troop Javelin Thrower
 *
 * @author pchayka
 */
public class DimensionMovingDevice extends DefaultAI {
    private static final int MOBS_WAVE_DELAY = 120 * 1000; // 2 мин между волнами мобов
    private long spawnTime = 0;

    private static final int[] MOBS = {22538, // Dragon Steed Troop Commander
            22540, // White Dragon Leader
            22547, // Dragon Steed Troop Healer
            22542, // Dragon Steed Troop Magic Leader
            22548 // Dragon Steed Troop Javelin Thrower
    };

    private List<NpcInstance> _npcs = new ArrayList<NpcInstance>();

    public DimensionMovingDevice(NpcInstance actor) {
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
        if (spawnTime + MOBS_WAVE_DELAY < System.currentTimeMillis()) {
            if (_npcs.size() < 100)
                for (int id : MOBS) {
                    NpcInstance mob = actor.getReflection().addSpawnWithoutRespawn(id, actor.getLoc(), 0);
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