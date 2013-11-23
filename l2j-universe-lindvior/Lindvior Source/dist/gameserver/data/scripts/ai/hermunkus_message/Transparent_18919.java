package ai.hermunkus_message;

import l2p.gameserver.ai.DefaultAI;
import l2p.gameserver.model.Skill;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.tables.SkillTable;

public class Transparent_18919 extends DefaultAI {
    private static final int SKILL_ID = 14649;

    public Transparent_18919(NpcInstance actor) {
        super(actor);
    }

    @Override
    protected void onEvtSpawn() {
        super.onEvtSpawn();
        addTimer(1, 100);
    }

    @Override
    protected void onEvtTimer(int timerId, Object arg1, Object arg2) {
        if (timerId == 1) {
            Skill skill = SkillTable.getInstance().getInfo(SKILL_ID, 1);
            addTaskBuff(getActor(), skill);
            doTask();
        }
    }

    @Override
    protected void onEvtFinishCasting(int skill_id, boolean success) {
        if (skill_id == SKILL_ID) {
            getActor().deleteMe();
        }
    }
}
