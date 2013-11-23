package ai.primeval_isle;

import l2p.gameserver.ai.DefaultAI;
import l2p.gameserver.model.Skill;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.tables.SkillTable;

/**
 * @author VISTALL
 * @date 4:57/16.06.2011
 */
public class SprigantStun extends DefaultAI {
    private final Skill SKILL = SkillTable.getInstance().getInfo(5085, 1);
    private long _waitTime;

    private static final int TICK_IN_MILISECONDS = 15000;

    public SprigantStun(NpcInstance actor) {
        super(actor);
    }

    @Override
    protected boolean thinkActive() {
        if (System.currentTimeMillis() > _waitTime) {
            NpcInstance actor = getActor();

            actor.doCast(SKILL, actor, false);

            _waitTime = System.currentTimeMillis() + TICK_IN_MILISECONDS;
            return true;
        }

        return false;
    }
}
