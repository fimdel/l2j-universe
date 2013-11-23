/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package ai.custom;

import l2p.gameserver.ai.Fighter;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.entity.Reflection;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.model.instances.ReflectionBossInstance;
import l2p.gameserver.stats.Stats;
import l2p.gameserver.stats.funcs.FuncSet;

/**
 * @author pchayka
 */
public class LabyrinthLostWatcher extends Fighter {

    public LabyrinthLostWatcher(NpcInstance actor) {
        super(actor);
    }

    @Override
    protected void onEvtDead(Creature killer) {
        NpcInstance actor = getActor();
        Reflection r = actor.getReflection();
        if (!r.isDefault()) {
            if (checkMates(actor.getNpcId())) {
                if (findLostCaptain() != null) {
                    findLostCaptain().addStatFunc(new FuncSet(Stats.PHYSICAL_DEFENCE, 0x30, this, findLostCaptain().getTemplate().getBasePDef() * 0.66));
                }
            }
        }
        super.onEvtDead(killer);
    }

    private boolean checkMates(int id) {
        for (NpcInstance n : getActor().getReflection().getNpcs()) {
            if ((n.getNpcId() == id) && !n.isDead()) {
                return false;
            }
        }
        return true;
    }

    private NpcInstance findLostCaptain() {
        for (NpcInstance n : getActor().getReflection().getNpcs()) {
            if (n instanceof ReflectionBossInstance) {
                return n;
            }
        }
        return null;
    }
}