package ai;

import l2p.commons.util.Rnd;
import l2p.gameserver.ai.CtrlIntention;
import l2p.gameserver.ai.Fighter;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.scripts.Functions;

/**
 * AI монахов в Monastery of Silence<br>
 * - агрятся на чаров с оружием в руках
 * - перед тем как броситься в атаку кричат
 *
 * @author SYS
 */
public class MoSMonk extends Fighter {
    public MoSMonk(NpcInstance actor) {
        super(actor);
    }

    @Override
    protected void onIntentionAttack(Creature target) {
        if (getIntention() == CtrlIntention.AI_INTENTION_ACTIVE && Rnd.chance(20))
            Functions.npcSayCustomMessage(getActor(), "scripts.ai.MoSMonk.onIntentionAttack");
        super.onIntentionAttack(target);
    }

    @Override
    public boolean checkAggression(Creature target) {
        if (target.getActiveWeaponInstance() == null)
            return false;
        return super.checkAggression(target);
    }
}