package ai.DwarvenVillageAttack;

import instances.MemoryOfDisaster;

import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.tables.SkillTable;

public class EarthWyrmTrasken extends DefaultAI
{
	private static final int RHAND_ID = 15280;
	private static final int ENRAGED_SKILL_ID = 14505;
	private static final int BODY_STRIKE_SKILL_ID_1 = 14337;
	private static final int BODY_STRIKE_SKILL_ID_2 = 14338;

	public EarthWyrmTrasken(NpcInstance actor)
	{
		super(actor);
	}

	@Override
	protected void onEvtSpawn()
	{
		super.onEvtSpawn();
		getActor().setRHandId(RHAND_ID);
		addTimer(1, 50);
	}

	@Override
	protected void onEvtTimer(int timerId, Object arg1, Object arg2)
	{
		super.onEvtTimer(timerId, arg1, arg2);
		Skill sk;
		switch(timerId)
		{
			case 1:
				sk = SkillTable.getInstance().getInfo(ENRAGED_SKILL_ID, 1);
				addTaskBuff(getActor(), sk);
				doTask();
				break;
			case 2:
				sk = SkillTable.getInstance().getInfo(BODY_STRIKE_SKILL_ID_1, 1);
				addTaskBuff(getActor(), sk);
				doTask();
				break;
			case 3:
				sk = SkillTable.getInstance().getInfo(BODY_STRIKE_SKILL_ID_2, 1);
				addTaskBuff(getActor(), sk);
				doTask();
				break;
		}
	}

	@Override
	protected void onEvtFinishCasting(int skill_id, boolean success)
	{
		if(skill_id == ENRAGED_SKILL_ID)
		{
			Reflection r = getActor().getReflection();
			if(r instanceof MemoryOfDisaster)
				((MemoryOfDisaster) r).startFinalScene();
			addTimer(2, 50);
		}
		else if(skill_id == BODY_STRIKE_SKILL_ID_1)
			addTimer(3, 50);
	}
}
