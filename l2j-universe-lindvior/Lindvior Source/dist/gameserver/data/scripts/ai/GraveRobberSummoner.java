/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package ai;

import l2p.commons.util.Rnd;
import l2p.gameserver.ai.Mystic;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.instances.MonsterInstance;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.stats.Env;
import l2p.gameserver.stats.Stats;
import l2p.gameserver.stats.funcs.Func;
import l2p.gameserver.templates.npc.MinionData;

/**
 * При спавне саммонят случайную охрану. Защита прямо пропорциональна количеству охранников.
 */
public class GraveRobberSummoner extends Mystic {
    private static final int[] Servitors =
            {
                    22683, 22684, 22685, 22686
            };

    private int _lastMinionCount = 1;

    private class FuncMulMinionCount extends Func {
        public FuncMulMinionCount(Stats stat, int order, Object owner) {
            super(stat, order, owner);
        }

        @Override
        public void calc(Env env) {
            env.value *= _lastMinionCount;
        }
    }

    public GraveRobberSummoner(NpcInstance actor) {
        super(actor);

        actor.addStatFunc(new FuncMulMinionCount(Stats.MAGIC_DEFENCE, 0x30, actor));
        actor.addStatFunc(new FuncMulMinionCount(Stats.PHYSICAL_DEFENCE, 0x30, actor));
    }

    @Override
    protected void onEvtSpawn() {
        super.onEvtSpawn();

        NpcInstance actor = getActor();
        actor.getMinionList().addMinion(new MinionData(Servitors[Rnd.get(Servitors.length)], Rnd.get(2)));
        _lastMinionCount = Math.max(actor.getMinionList().getAliveMinions().size(), 1);
    }

    @Override
    protected void onEvtAttacked(Creature attacker, int damage) {
        MonsterInstance actor = (MonsterInstance) getActor();
        if (actor.isDead()) {
            return;
        }
        _lastMinionCount = Math.max(actor.getMinionList().getAliveMinions().size(), 1);
        super.onEvtAttacked(attacker, damage);
    }

    @Override
    protected void onEvtDead(Creature killer) {
        NpcInstance actor = getActor();
        actor.getMinionList().deleteMinions();
        super.onEvtDead(killer);
    }
}