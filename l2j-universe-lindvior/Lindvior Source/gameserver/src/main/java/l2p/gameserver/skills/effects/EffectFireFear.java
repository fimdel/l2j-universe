/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.skills.effects;

import l2p.gameserver.ai.CtrlIntention;
import l2p.gameserver.cache.Msg;
import l2p.gameserver.model.Effect;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.entity.events.impl.SiegeEvent;
import l2p.gameserver.model.instances.SummonInstance;
import l2p.gameserver.network.serverpackets.components.SystemMsg;
import l2p.gameserver.stats.Env;

public final class EffectFireFear extends Effect {
    public static final double FEAR_RANGE = 900;

    public EffectFireFear(Env env, EffectTemplate template) {
        super(env, template);
    }

    @Override
    public boolean checkCondition() {
        if (_effected.isFearImmune()) {
            getEffector().sendPacket(SystemMsg.THAT_IS_AN_INCORRECT_TARGET);
            return false;
        }

        // Fear нельзя наложить на осадных саммонов
        Player player = _effected.getPlayer();
        if (player != null) {
            SiegeEvent<?, ?> siegeEvent = player.getEvent(SiegeEvent.class);
            if (_effected.isServitor() && (siegeEvent != null) && siegeEvent.containsSiegeSummon((SummonInstance) _effected)) {
                getEffector().sendPacket(SystemMsg.THAT_IS_AN_INCORRECT_TARGET);
                return false;
            }
        }

        if (_effected.isInZonePeace()) {
            getEffector().sendPacket(Msg.YOU_MAY_NOT_ATTACK_IN_A_PEACEFUL_ZONE);
            return false;
        }

        return super.checkCondition();
    }

    @Override
    public void onStart() {
        super.onStart();

        if (!_effected.startFear()) {
            _effected.abortAttack(true, true);
            _effected.abortCast(true, true);
            _effected.stopMove();
        }
    }

    @Override
    public void onExit() {
        super.onExit();
        _effected.stopFear();
        _effected.getAI().setIntention(CtrlIntention.AI_INTENTION_ACTIVE);
    }

    @Override
    public boolean onActionTime() {
        return true;
    }
}