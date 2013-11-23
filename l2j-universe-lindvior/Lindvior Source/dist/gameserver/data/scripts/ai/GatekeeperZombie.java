package ai;

import l2p.gameserver.ai.CtrlIntention;
import l2p.gameserver.ai.Mystic;
import l2p.gameserver.geodata.GeoEngine;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Playable;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.scripts.Functions;

/**
 * AI охраны входа в Pagan Temple.<br>
 * <li>кидаются на всех игроков, у которых в кармане нету предмета 8064 или 8067
 * <li>не умеют ходить
 *
 * @author SYS
 */
public class GatekeeperZombie extends Mystic {
    public GatekeeperZombie(NpcInstance actor) {
        super(actor);
        actor.startImmobilized();
    }

    @Override
    public boolean checkAggression(Creature target) {
        NpcInstance actor = getActor();
        if (actor.isDead())
            return false;
        if (getIntention() != CtrlIntention.AI_INTENTION_ACTIVE || !isGlobalAggro())
            return false;
        if (target.isAlikeDead() || !target.isPlayable())
            return false;
        if (!target.isInRangeZ(actor.getSpawnedLoc(), actor.getAggroRange()))
            return false;
        if (Functions.getItemCount((Playable) target, 8067) != 0 || Functions.getItemCount((Playable) target, 8064) != 0)
            return false;
        if (!GeoEngine.canSeeTarget(actor, target, false))
            return false;

        if (getIntention() != CtrlIntention.AI_INTENTION_ATTACK) {
            actor.getAggroList().addDamageHate(target, 0, 1);
            setIntention(CtrlIntention.AI_INTENTION_ATTACK, target);
        }

        return true;
    }

    @Override
    protected boolean randomWalk() {
        return false;
    }
}