package ai.isle_of_prayer;

import l2p.commons.threading.RunnableImpl;
import l2p.commons.util.Rnd;
import l2p.gameserver.ThreadPoolManager;
import l2p.gameserver.ai.Fighter;
import l2p.gameserver.data.xml.holder.NpcHolder;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.SimpleSpawner;
import l2p.gameserver.model.Skill;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.model.items.ItemInstance;
import l2p.gameserver.stats.Stats;
import l2p.gameserver.stats.funcs.FuncTemplate;
import l2p.gameserver.utils.ItemFunctions;
import l2p.gameserver.utils.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

/**
 * @author Diamond
 * @corrected n0nam3
 */
public class FafurionKindred extends Fighter {
    private static final int DETRACTOR1 = 22270;
    private static final int DETRACTOR2 = 22271;

    private static final int Spirit_of_the_Lake = 2368;

    private static final int Water_Dragon_Scale = 9691;
    private static final int Water_Dragon_Claw = 9700;

    ScheduledFuture<?> poisonTask;
    ScheduledFuture<?> despawnTask;

    List<SimpleSpawner> spawns = new ArrayList<SimpleSpawner>();

    private static final FuncTemplate ft = new FuncTemplate(null, "Mul", Stats.HEAL_EFFECTIVNESS, 0x90, 0);

    public FafurionKindred(NpcInstance actor) {
        super(actor);
        actor.addStatFunc(ft.getFunc(this));
    }

    @Override
    protected void onEvtSpawn() {
        super.onEvtSpawn();

        spawns.clear();

        ThreadPoolManager.getInstance().schedule(new SpawnTask(DETRACTOR1), 500);
        ThreadPoolManager.getInstance().schedule(new SpawnTask(DETRACTOR2), 500);
        ThreadPoolManager.getInstance().schedule(new SpawnTask(DETRACTOR1), 500);
        ThreadPoolManager.getInstance().schedule(new SpawnTask(DETRACTOR2), 500);

        poisonTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new PoisonTask(), 3000, 3000);
        despawnTask = ThreadPoolManager.getInstance().schedule(new DeSpawnTask(), 300000);
    }

    @Override
    protected void onEvtDead(Creature killer) {
        cleanUp();

        super.onEvtDead(killer);
    }

    @Override
    protected void onEvtSeeSpell(Skill skill, Creature caster) {
        NpcInstance actor = getActor();
        if (actor.isDead() || skill == null)
            return;
        // Лечим
        if (skill.getId() == Spirit_of_the_Lake)
            actor.setCurrentHp(actor.getCurrentHp() + 3000, false);
        actor.getAggroList().remove(caster, true);
    }

    @Override
    protected boolean randomWalk() {
        return false;
    }

    private void cleanUp() {
        if (poisonTask != null) {
            poisonTask.cancel(false);
            poisonTask = null;
        }
        if (despawnTask != null) {
            despawnTask.cancel(false);
            despawnTask = null;
        }

        for (SimpleSpawner spawn : spawns)
            spawn.deleteAll();
        spawns.clear();
    }

    private class SpawnTask extends RunnableImpl {
        private final int _id;

        public SpawnTask(int id) {
            _id = id;
        }

        @Override
        public void runImpl() {
            NpcInstance actor = getActor();
            SimpleSpawner sp = new SimpleSpawner(NpcHolder.getInstance().getTemplate(_id));
            sp.setLoc(Location.findPointToStay(actor, 100, 120));
            sp.setRespawnDelay(30, 40);
            sp.doSpawn(true);
            spawns.add(sp);
        }
    }

    private class PoisonTask extends RunnableImpl {
        @Override
        public void runImpl() {
            NpcInstance actor = getActor();
            actor.reduceCurrentHp(500, 0, actor, null, true, false, true, false, false, false, false); // Травим дракошу ядом
        }
    }

    private class DeSpawnTask extends RunnableImpl {
        @Override
        public void runImpl() {
            NpcInstance actor = getActor();

            // Если продержались 5 минут, то выдаем награду, и деспавним
            dropItem(actor, Water_Dragon_Scale, Rnd.get(1, 2));
            if (Rnd.chance(36))
                dropItem(actor, Water_Dragon_Claw, Rnd.get(1, 3));

            cleanUp();
            actor.deleteMe();
        }
    }

    private void dropItem(NpcInstance actor, int id, int count) {
        ItemInstance item = ItemFunctions.createItem(id);
        item.setCount(count);
        item.dropToTheGround(actor, Location.findPointToStay(actor, 100));
    }
}