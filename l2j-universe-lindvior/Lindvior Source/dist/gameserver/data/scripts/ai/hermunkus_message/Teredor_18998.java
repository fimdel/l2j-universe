package ai.hermunkus_message;

import instances.MemoryOfDisaster;
import l2p.gameserver.ai.DefaultAI;
import l2p.gameserver.model.Skill;
import l2p.gameserver.model.entity.Reflection;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.tables.SkillTable;


public class Teredor_18998 extends DefaultAI {
    private static final int SKILL_ID = 16021;

    public Teredor_18998(NpcInstance actor) {
        super(actor);
    }

    @Override
    protected void onEvtSpawn() {
        super.onEvtSpawn();
        addTimer(1, 2500);
    }

    @Override
    protected void onEvtTimer(int timer_id, Object arg1, Object arg2) {
        super.onEvtTimer(timer_id, arg1, arg2);
        switch (timer_id) {
            case 1:
                Skill sk = SkillTable.getInstance().getInfo(SKILL_ID, 1);
                addTaskBuff(getActor(), sk);
                doTask();
                break;
            case 2:
                Reflection r = getActor().getReflection();
                if (r instanceof MemoryOfDisaster)
                    ((MemoryOfDisaster) r).spawnWyrm();
                getActor().deleteMe();
                break;
        }
    }

    @Override
    protected void onEvtFinishCasting(int skill_id, boolean success) {
        if (skill_id == SKILL_ID) {
            addTimer(2, 2000);
        }
    }
}
