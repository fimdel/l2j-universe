/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package ai;

import instances.SpezionNormal;
import l2p.gameserver.ai.Mystic;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.entity.Reflection;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.tables.SkillTable;

/**
 * @author cruel
 */
public class SpezionMonster extends Mystic {

    public SpezionMonster(NpcInstance actor) {
        super(actor);
    }

    @Override
    protected void onEvtDead(Creature killer) {
        NpcInstance actor = getActor();
        if (actor.getNpcId() == 22985) {
            Reflection r = actor.getReflection();
            if (r instanceof SpezionNormal) {
                SpezionNormal spezion = (SpezionNormal) r;
                spezion.openGate(26190004);
            }
        }

        super.onEvtDead(killer);
    }

    @Override
    protected void thinkAttack() {
        NpcInstance actor = getActor();
        Creature randomHated = actor.getAggroList().getRandomHated();
        if (randomHated != null && actor.getNpcId() == 22971 || actor.getNpcId() == 22972)
            actor.doCast(SkillTable.getInstance().getInfo(14139, 1), randomHated, true);
        super.thinkAttack();
    }
}
