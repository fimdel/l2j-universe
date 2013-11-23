package ai.residences;

import l2p.commons.collections.CollectionUtils;
import l2p.commons.util.Rnd;
import l2p.gameserver.Config;
import l2p.gameserver.ai.CtrlIntention;
import l2p.gameserver.ai.DefaultAI;
import l2p.gameserver.geodata.GeoEngine;
import l2p.gameserver.model.*;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.utils.Location;
import npc.model.residences.SiegeGuardInstance;

import java.util.List;

public abstract class SiegeGuard extends DefaultAI {
    public SiegeGuard(NpcInstance actor) {
        super(actor);
        MAX_PURSUE_RANGE = 1000;
    }

    @Override
    public SiegeGuardInstance getActor() {
        return (SiegeGuardInstance) super.getActor();
    }

    @Override
    public int getMaxPathfindFails() {
        return Integer.MAX_VALUE;
    }

    @Override
    public int getMaxAttackTimeout() {
        return 0;
    }

    @Override
    protected boolean randomWalk() {
        return false;
    }

    @Override
    protected boolean randomAnimation() {
        return false;
    }

    @Override
    public boolean canSeeInSilentMove(Playable target) {
        // Осадные гварды могут видеть игроков в режиме Silent Move с вероятностью 10%
        return !target.isSilentMoving() || Rnd.chance(10);
    }

    @Override
    protected boolean checkAggression(Creature target) {
        NpcInstance actor = getActor();
        if (getIntention() != CtrlIntention.AI_INTENTION_ACTIVE || !isGlobalAggro())
            return false;
        if (target.isAlikeDead() || target.isInvul())
            return false;

        if (target.isPlayable()) {
            if (!canSeeInSilentMove((Playable) target))
                return false;
            if (!canSeeInHide((Playable) target))
                return false;
            if (target.isPlayer() && ((Player) target).isGM() && target.isInvisible())
                return false;
            if (((Playable) target).getNonAggroTime() > System.currentTimeMillis())
                return false;
            if (target.isPlayer() && !target.getPlayer().isActive())
                return false;
            if (actor.isMonster() && target.isInZonePeace())
                return false;
        }

        AggroList.AggroInfo ai = actor.getAggroList().get(target);
        if (ai != null && ai.hate > 0) {
            if (!target.isInRangeZ(actor.getSpawnedLoc(), MAX_PURSUE_RANGE))
                return false;
        } else if (!target.isInRangeZ(actor.getSpawnedLoc(), 600))
            return false;

        if (!canAttackCharacter(target))
            return false;
        if (!GeoEngine.canSeeTarget(actor, target, false))
            return false;

        actor.getAggroList().addDamageHate(target, 0, 2);

        if (target.isServitor() || target.isPet())
            actor.getAggroList().addDamageHate(target.getPlayer(), 0, 1);

        startRunningTask(AI_TASK_ATTACK_DELAY);
        setIntention(CtrlIntention.AI_INTENTION_ATTACK, target);

        return true;
    }

    @Override
    protected boolean isGlobalAggro() {
        return true;
    }

    @Override
    protected void onEvtAggression(Creature target, int aggro) {
        SiegeGuardInstance actor = getActor();
        if (actor.isDead())
            return;
        if (target == null || !actor.isAutoAttackable(target))
            return;
        super.onEvtAggression(target, aggro);
    }

    @Override
    protected boolean thinkActive() {
        NpcInstance actor = getActor();
        if (actor.isActionsDisabled())
            return true;

        if (_def_think) {
            if (doTask())
                clearTasks();
            return true;
        }

        long now = System.currentTimeMillis();
        if (now - _checkAggroTimestamp > Config.AGGRO_CHECK_INTERVAL) {
            _checkAggroTimestamp = now;

            List<Creature> chars = World.getAroundCharacters(actor);
            CollectionUtils.eqSort(chars, _nearestTargetComparator);
            for (Creature cha : chars)
                if (checkAggression(cha))
                    return true;
        }

        Location sloc = actor.getSpawnedLoc();
        // Проверка на расстояние до точки спауна
        if (!actor.isInRange(sloc, 250)) {
            teleportHome();
            return true;
        }

        return false;
    }

    @Override
    protected Creature prepareTarget() {
        SiegeGuardInstance actor = getActor();
        if (actor.isDead())
            return null;

        // Новая цель исходя из агрессивности
        List<Creature> hateList = actor.getAggroList().getHateList();
        Creature hated = null;
        for (Creature cha : hateList) {
            //Не подходит, очищаем хейт
            if (!checkTarget(cha, MAX_PURSUE_RANGE)) {
                actor.getAggroList().remove(cha, true);
                continue;
            }
            hated = cha;
            break;
        }

        if (hated != null) {
            setAttackTarget(hated);
            return hated;
        }

        return null;
    }

    @Override
    protected boolean canAttackCharacter(Creature target) {
        return getActor().isAutoAttackable(target);
    }
}