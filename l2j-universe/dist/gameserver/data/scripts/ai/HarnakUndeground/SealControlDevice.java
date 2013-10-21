package ai.HarnakUndeground;

import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.scripts.Functions;
import instances.HarnakUndergroundRuins;

public class SealControlDevice extends DefaultAI
{
	private final boolean SHOUT;

	public SealControlDevice(NpcInstance actor)
	{
		super(actor);
		SHOUT = getActor().getParameter("shout", false);
	}

	@Override
	protected void onEvtSpawn()
	{
		super.onEvtSpawn();
		if(SHOUT)
			addTimer(1, 1);
	}

	@Override
	protected void onEvtMenuSelected(Player player, int ask, int reply)
	{
		if(ask == 10338 && reply == 1)
		{
			if(getActor().getNpcState() != 2)
				getActor().setNpcState(2);
			broadCastScriptEvent("SEAL_ACTIVATED", 3000);
		}
	}

	@Override
	protected void onEvtScriptEvent(String event, Object arg1, Object arg2)
	{
		if(event.equalsIgnoreCase("FAIL_INSTANCE"))
			getActor().deleteMe();
	}

	@Override
	protected void onEvtTimer(int timerId, Object arg1, Object arg2)
	{
		super.onEvtTimer(timerId, arg1, arg2);

		if(!isActive())
			return;
		Reflection r = getActor().getReflection();
		if(!(r instanceof HarnakUndergroundRuins))
			return;
		switch(timerId)
		{
			case 1:
				Functions.npcSayInRange(getActor(), 1500, NpcString.DISABLE_DEVICE_WILL_GO_OUT_OF_CONTROL_IN_1_MINUTE);
				addTimer(2, 10000);
				break;
			case 2:
				Functions.npcSayInRange(getActor(), 1500, NpcString._50_SECONDS_ARE_REMAINING);
				addTimer(3, 10000);
				break;
			case 3:
				Functions.npcSayInRange(getActor(), 1500, NpcString._40_SECONDS_ARE_REMAINING);
				addTimer(4, 10000);
				break;
			case 4:
				Functions.npcSayInRange(getActor(), 1500, NpcString._30_SECONDS_ARE_REMAINING);
				addTimer(5, 10000);
				break;
			case 5:
				Functions.npcSayInRange(getActor(), 1500, NpcString._20_SECONDS_ARE_REMAINING);
				addTimer(6, 10000);
				break;
			case 6:
				Functions.npcSayInRange(getActor(), 1500, NpcString._10_SECONDS_ARE_REMAINING);
				addTimer(7, 5000);
				break;
			case 7:
				Functions.npcSayInRange(getActor(), 1500, NpcString._5_SECONDS);
				addTimer(8, 1000);
				break;
			case 8:
				Functions.npcSayInRange(getActor(), 1500, NpcString._4_SECONDS);
				addTimer(9, 1000);
				break;
			case 9:
				Functions.npcSayInRange(getActor(), 1500, NpcString._3_SECONDS);
				addTimer(10, 1000);
				break;
			case 10:
				Functions.npcSayInRange(getActor(), 1500, NpcString._2_SECONDS);
				addTimer(11, 1000);
				break;
			case 11:
				Functions.npcSayInRange(getActor(), 1500, NpcString._1_SECOND);
				break;
		}
	}
	
	@Override
	protected boolean randomWalk()
	{
		return false;
	}
}
