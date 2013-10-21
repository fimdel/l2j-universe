package ai.DwarvenVillageAttack;

import instances.MemoryOfDisaster;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.scripts.Functions;

public class DarkElves extends DefaultAI
{
	private static NpcString[] TEXT = {
			NpcString.GAH_SHILEN_WHY_MUST_YOU_MAKE_US_SUFFER,
			NpcString.SHILEN_ABANDONED_US_IT_IS_OUR_TIME_TO_DIE,
			NpcString.WITH_OUR_SACRIFICE_WILL_WE_FULLFILL_THE_PROPHECY,
			NpcString.BLOODY_RAIN_PLAGUE_DEATH_SHE_IS_NEAR,
			NpcString.ARHHHH,
			NpcString.WE_OFFER_OUR_BLOOD_AS_A_SACRIFICE_SHILEN_SEE_US,
			NpcString.WILL_DARK_ELVES_BE_FORGOTTEN_AFTER_WHAT_WE_HAVE_DONE,
			NpcString.UNBELIEVERS_RUN_DEATH_WILL_FOLLOW_YOU,
			NpcString.I_CURSE_OUR_BLOOD_I_DESPISE_WHAT_WE_ARE_SHILEN };

	public DarkElves(NpcInstance actor)
	{
		super(actor);
	}

	@Override
	protected void onEvtScriptEvent(String event, Object arg1, Object arg2)
	{
		if(event.equalsIgnoreCase("START_DIE"))
			addTimer(1, 3000);
	}

	@Override
	protected void onEvtTimer(int timerId, Object arg1, Object arg2)
	{
		if(timerId == 1)
		{
			if(Rnd.chance(50))
				Functions.npcSayInRange(getActor(), 1000, TEXT[Rnd.get(TEXT.length)]);
			Reflection r = getActor().getReflection();
			if(r instanceof MemoryOfDisaster)
				((MemoryOfDisaster) r).dieNextElf();
			getActor().doDie(getActor());
		}
	}
}
