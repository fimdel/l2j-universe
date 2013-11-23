package ai;

import l2p.commons.util.Rnd;
import l2p.gameserver.ai.Fighter;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.scripts.Functions;

public class FrightenedOrc extends Fighter {
    private boolean _sayOnAttack;

    public FrightenedOrc(NpcInstance actor) {
        super(actor);
    }

    @Override
    protected void onEvtSpawn() {
        _sayOnAttack = true;
        super.onEvtSpawn();
    }

    @Override
    protected void onEvtAttacked(Creature attacker, int damage) {
        NpcInstance actor = getActor();
        if (attacker != null && Rnd.chance(10) && _sayOnAttack) {
            Functions.npcSay(actor, "Don't kill me! If you show mercy I will pay you 10000 adena!");
            _sayOnAttack = false;
        }

        super.onEvtAttacked(attacker, damage);
    }

}