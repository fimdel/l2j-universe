package ai;

import l2p.commons.util.Rnd;
import l2p.gameserver.ai.Ranger;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.scripts.Functions;

/**
 * AI для Karul Bugbear ID: 20600
 *
 * @author Diamond
 */
public class KarulBugbear extends Ranger {
    private boolean _firstTimeAttacked = true;

    public KarulBugbear(NpcInstance actor) {
        super(actor);
    }

    @Override
    protected void onEvtSpawn() {
        _firstTimeAttacked = true;
        super.onEvtSpawn();
    }

    @Override
    protected void onEvtAttacked(Creature attacker, int damage) {
        NpcInstance actor = getActor();
        if (_firstTimeAttacked) {
            _firstTimeAttacked = false;
            if (Rnd.chance(25))
                Functions.npcSay(actor, "Your rear is practically unguarded!");
        } else if (Rnd.chance(10))
            Functions.npcSay(actor, "Watch your back!");
        super.onEvtAttacked(attacker, damage);
    }
}