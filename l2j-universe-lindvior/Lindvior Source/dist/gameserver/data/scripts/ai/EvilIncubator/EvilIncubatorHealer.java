/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package ai.EvilIncubator;

import instances.EvilIncubator;
import l2p.commons.threading.RunnableImpl;
import l2p.commons.util.Rnd;
import l2p.gameserver.Config;
import l2p.gameserver.ThreadPoolManager;
import l2p.gameserver.ai.CtrlIntention;
import l2p.gameserver.ai.Fighter;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.Skill;
import l2p.gameserver.model.entity.Reflection;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.tables.SkillTable;
import l2p.gameserver.utils.Location;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

/**
 * @author KilRoy
 */
public class EvilIncubatorHealer extends Fighter {
    private boolean _thinking = false;
    private final Skill recharge = SkillTable.getInstance().getInfo(6728, 1);
    private final Skill heal = SkillTable.getInstance().getInfo(6724, 1);

    private ScheduledFuture<?> _followTask;

    public EvilIncubatorHealer(NpcInstance actor) {
        super(actor);
    }

    @Override
    protected boolean thinkActive() {
        NpcInstance actor = getActor();
        Creature following = actor.getFollowTarget();

        if (following == null || !actor.isFollow) {
            Player master = getMaster();
            if (master != null) {
                actor.setFollowTarget(master);
                actor.setRunning();
                actor.getAI().setIntention(CtrlIntention.AI_INTENTION_FOLLOW, master, Config.FOLLOW_RANGE);
            }
        }

        super.thinkActive();
        return false;
    }

    @Override
    protected void onEvtThink() {
        NpcInstance actor = getActor();
        if (_thinking || actor.isActionsDisabled() || actor.isAfraid() || actor.isDead() || actor.isMovementDisabled())
            return;

        _thinking = true;
        try {
            if (!Config.BLOCK_ACTIVE_TASKS && (getIntention() == CtrlIntention.AI_INTENTION_ACTIVE || getIntention() == CtrlIntention.AI_INTENTION_IDLE))
                thinkActive();
            else if (getIntention() == CtrlIntention.AI_INTENTION_FOLLOW)
                thinkFollow();
        } catch (Exception e) {
            _log.error("", e);
        } finally {
            _thinking = false;
        }
    }

    private Player getMaster() {
        if (!getActor().getReflection().getPlayers().isEmpty())
            return getActor().getReflection().getPlayers().get(0);
        return null;
    }

    protected void thinkFollow() {
        NpcInstance actor = getActor();

        Creature target = actor.getFollowTarget();

        if (target == null || target.isAlikeDead() || actor.getDistance(target) > 4000 || actor.isMovementDisabled()) {
            actor.getAI().setIntention(CtrlIntention.AI_INTENTION_ACTIVE);
            return;
        }

        if (actor.isFollow && actor.getFollowTarget() == target) {
            clientActionFailed();
            return;
        }

        if (actor.isInRange(target, Config.FOLLOW_RANGE + 20))
            clientActionFailed();

        if (_followTask != null) {
            _followTask.cancel(false);
            _followTask = null;
        }

        _followTask = ThreadPoolManager.getInstance().schedule(new ThinkFollow(), 250L);

        Reflection ref = actor.getReflection();

        if (ref != null) {
            Map<Skill, Integer> d_skill = new HashMap<Skill, Integer>();
            double distance = actor.getDistance(target);
            if (ref.getInstancedZoneId() == 185) {
                if (target.getCurrentHpPercents() < 70)
                    addDesiredSkill(d_skill, target, distance, heal);
                if (target.getCurrentMpPercents() < 50)
                    addDesiredSkill(d_skill, target, distance, recharge);

                Skill r_skill = selectTopSkill(d_skill);
                chooseTaskAndTargets(r_skill, target, distance);
                doTask();
            }
        }
    }

    protected class ThinkFollow extends RunnableImpl {
        @Override
        public void runImpl() {
            NpcInstance actor = getActor();
            if (actor == null)
                return;

            EvilIncubator instance = (EvilIncubator) actor.getReflection();

            Creature target = actor.getFollowTarget();

            if (target == null || actor.getDistance(target) > 4000 || instance.getInstanceStage() < 1) {
                setIntention(CtrlIntention.AI_INTENTION_ACTIVE);
                return;
            }

            if (!actor.isInRange(target, Config.FOLLOW_RANGE + 20) && (!actor.isFollow || actor.getFollowTarget() != target)) {
                Location loc = new Location(target.getX() + Rnd.get(-60, 60), target.getY() + Rnd.get(-60, 60), target.getZ());
                actor.followToCharacter(loc, target, Config.FOLLOW_RANGE, false);
            }
            _followTask = ThreadPoolManager.getInstance().schedule(this, 250L);
        }
    }

    @Override
    protected boolean randomWalk() {
        return false;
    }

    @Override
    public void addTaskAttack(Creature target) {
    }
}