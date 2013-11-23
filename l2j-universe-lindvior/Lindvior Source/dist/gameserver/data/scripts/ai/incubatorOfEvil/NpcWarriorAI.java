/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package ai.incubatorOfEvil;

import l2p.commons.util.Rnd;
import l2p.gameserver.ai.CtrlEvent;
import l2p.gameserver.ai.Fighter;
import l2p.gameserver.geodata.GeoEngine;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.network.serverpackets.components.NpcString;
import l2p.gameserver.scripts.Functions;

import java.util.List;

/**
 * @author Mazaffaka
 */
public class NpcWarriorAI extends Fighter {
    private NpcInstance target = null;

    public NpcWarriorAI(NpcInstance actor) {
        super(actor);
    }

    @Override
    public boolean isGlobalAI() {
        return false;
    }

    @Override
    protected void onEvtSpawn() {
        startAttack();
    }

    @Override
    protected boolean thinkActive() {
        NpcInstance actor = getActor();

        if (Rnd.chance(3)) {
            switch (actor.getNpcId()) {
                case 33172:
                    Functions.npcSay(actor, NpcString.THE_ONLY_GOOD_SHILEN_CREATURE_IS_A_DEAD_ONE);
                    break;
                case 33170:
                    Functions.npcSay(actor, NpcString.GET_BEHIND_ME_GET_BEHIND_ME);
                    break;
                default:
                    break;
            }
        }
        return startAttack();
    }

    private boolean startAttack() {
        NpcInstance actor = getActor();
        if (target == null) {
            List<NpcInstance> around = actor.getAroundNpc(3000, 150);
            if (around != null && !around.isEmpty()) {
                for (NpcInstance npc : around) {
                    if (checkTarget(npc)) {
                        if (target == null || actor.getDistance3D(npc) < actor.getDistance3D(target))
                            target = npc;
                    }
                }
            }
        }

        if (target != null && !actor.isAttackingNow() && !actor.isCastingNow() && !target.isDead() && GeoEngine.canSeeTarget(actor, target, false) && target.isVisible()) {
            actor.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, target, 1);
            return true;
        }

        if (target != null && (!target.isVisible() || target.isDead() || !GeoEngine.canSeeTarget(actor, target, false))) {
            target = null;
            return false;
        }

        return false;
    }

    private boolean checkTarget(NpcInstance target) {
        if (target == null)
            return false;
        int _id = target.getNpcId();

        if (_id == 33170 || _id == 33171 || _id == 33172 || _id == 33173 || _id == 33174 || _id == 33414 || _id == 33415 || _id == 33416)
            return false;

        return true;
    }
}