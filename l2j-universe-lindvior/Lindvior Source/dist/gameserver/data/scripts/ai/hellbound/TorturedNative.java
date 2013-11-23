package ai.hellbound;

import l2p.commons.util.Rnd;
import l2p.gameserver.ai.Fighter;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.scripts.Functions;

/**
 * AI Tortured Native в городе-инстанте на Hellbound<br>
 * - периодически кричат
 *
 * @author SYS
 */
public class TorturedNative extends Fighter {
    public TorturedNative(NpcInstance actor) {
        super(actor);
    }

    @Override
    protected boolean thinkActive() {
        NpcInstance actor = getActor();
        if (actor.isDead())
            return true;

        if (Rnd.chance(1))
            if (Rnd.chance(10))
                Functions.npcSay(actor, "Eeeek... I feel sick... yow...!");
            else
                Functions.npcSay(actor, "It... will... kill... everyone...!");

        return super.thinkActive();
    }
}