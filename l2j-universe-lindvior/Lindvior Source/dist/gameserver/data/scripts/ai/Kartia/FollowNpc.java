/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package ai.Kartia;

import l2p.commons.threading.RunnableImpl;
import l2p.commons.util.Rnd;
import l2p.gameserver.Config;
import l2p.gameserver.ThreadPoolManager;
import l2p.gameserver.ai.CtrlIntention;
import l2p.gameserver.ai.DefaultAI;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.instances.MonsterInstance;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.utils.Location;

import java.util.concurrent.ScheduledFuture;

public class FollowNpc extends DefaultAI {
    /**
     * Field _thinking.
     */
    private boolean _thinking = false;
    /**
     * Field _followTask.
     */
    ScheduledFuture<?> _followTask;

    /**
     * Constructor for FollowNpc.
     *
     * @param actor NpcInstance
     */
    public FollowNpc(NpcInstance actor) {
        super(actor);
    }

    /**
     * Method randomWalk.
     *
     * @return boolean
     */
    @Override
    protected boolean randomWalk() {
        if (getActor() instanceof MonsterInstance) {
            return true;
        }
        return false;
    }

    /**
     * Method onEvtThink.
     */
    @Override
    protected void onEvtThink() {
        final NpcInstance actor = getActor();
        if (_thinking || actor.isActionsDisabled() || actor.isAfraid() || actor.isDead() || actor.isMovementDisabled()) {
            return;
        }
        _thinking = true;
        try {
            if (!Config.BLOCK_ACTIVE_TASKS && ((getIntention() == CtrlIntention.AI_INTENTION_ACTIVE) || (getIntention() == CtrlIntention.AI_INTENTION_IDLE))) {
                thinkActive();
            } else if (getIntention() == CtrlIntention.AI_INTENTION_FOLLOW) {
                thinkFollow();
            }
        } catch (Exception e) {
            _log.error("", e);
        } finally {
            _thinking = false;
        }
    }

    /**
     * Method thinkFollow.
     */
    protected void thinkFollow() {
        final NpcInstance actor = getActor();
        final Creature target = actor.getFollowTarget();
        if ((target == null) || target.isAlikeDead() || (actor.getDistance(target) > 4000) || actor.isMovementDisabled()) {
            clientActionFailed();
            return;
        }
        if (actor.isFollow && (actor.getFollowTarget().equals(target))) {
            clientActionFailed();
            return;
        }
        if (actor.isInRange(target, Config.FOLLOW_RANGE + 20)) {
            clientActionFailed();
        }
        if (_followTask != null) {
            _followTask.cancel(false);
            _followTask = null;
        }
        _followTask = ThreadPoolManager.getInstance().schedule(new ThinkFollow(), 250L);
    }

    /**
     * @author Mobius
     */
    protected class ThinkFollow extends RunnableImpl {
        /**
         * Method getActor.
         *
         * @return NpcInstance
         */
        public NpcInstance getActor() {
            return FollowNpc.this.getActor();
        }

        /**
         * Method runImpl.
         */
        @Override
        public void runImpl() {
            final NpcInstance actor = getActor();
            if (actor == null) {
                return;
            }
            final Creature target = actor.getFollowTarget();
            if ((target == null) || target.isAlikeDead() || (actor.getDistance(target) > 4000)) {
                setIntention(CtrlIntention.AI_INTENTION_ACTIVE);
                return;
            }
            if (!actor.isInRange(target, Config.FOLLOW_RANGE + 20) && (!actor.isFollow || (!actor.getFollowTarget().equals(target)))) {
                final Location loc = new Location(target.getX() + Rnd.get(-60, 60), target.getY() + Rnd.get(-60, 60), target.getZ());
                actor.followToCharacter(loc, target, Config.FOLLOW_RANGE, false);
            }
            _followTask = ThreadPoolManager.getInstance().schedule(this, 250L);
        }
    }
}
