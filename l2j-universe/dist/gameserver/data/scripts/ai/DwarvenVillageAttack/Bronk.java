package ai.DwarvenVillageAttack;

import instances.MemoryOfDisaster;

import java.util.List;

import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.scripts.Functions;

public class Bronk extends Dwarvs
{
	private static final int TOROCCO_ID = 19198;

	public Bronk(NpcInstance actor)
	{
		super(actor);
	}

	@Override
	protected void onEvtScriptEvent(String event, Object arg1, Object arg2)
	{
		if(event.equalsIgnoreCase("BRONK_1"))
			addTimer(1, 1600);
		else if(event.equalsIgnoreCase("BRONK_2"))
			addTimer(2, 1600);
	}

	@Override
	protected void onEvtTimer(int timerId, Object arg1, Object arg2)
	{
		super.onEvtTimer(timerId, arg1, arg2);

		Reflection r = getActor().getReflection();
		if(!(r instanceof MemoryOfDisaster))
			return;
		MemoryOfDisaster ad = (MemoryOfDisaster) r;

		switch(timerId)
		{
			case 1:
				Functions.npcSayInRange(getActor(), 1500, NpcString.MM_IM_SEE);
				List<NpcInstance> list = r.getAllByNpcId(TOROCCO_ID, true);
				if(list.size() > 0)
				{
					NpcInstance torocco = list.get(0);
					torocco.getAI().notifyEvent(CtrlEvent.EVT_SCRIPT_EVENT, "TOROCCO_1");
				}
				break;
			case 2:
				Functions.npcSayInRange(getActor(), 1500, NpcString.THANK_YOU_FOR_THE_REPORT_ROGIN);
				addTimer(3, 1600);
				break;
			case 3:
				Functions.npcSayInRange(getActor(), 1500, NpcString.SOLDIERS_WERE_FIGHTING_A_BATTLE_THAT_CANT_BE_WON);
				addTimer(4, 1600);
				break;
			case 4:
				Functions.npcSayInRange(getActor(), 1500, NpcString.BUT_WE_HAVE_TO_DEFEND_OUR_VILLAGE_SO_WERE_FIGHTING);
				addTimer(5, 1600);
				break;
			case 5:
				Functions.npcSayInRange(getActor(), 1500, NpcString.FOR_THE_FINE_WINES_AND_TREASURES_OF_ADEN);
				addTimer(6, 1600);
				break;
			case 6:
				Functions.npcSayInRange(getActor(), 1500, NpcString.IM_PROUD_OF_EVERY_ONE_OF);
				addTimer(7, 1600);
				addTimer(7, 2400);
				addTimer(7, 3600);

				addTimer(8, 2000);
				break;
			case 7:
				broadCastScriptEvent("SHOUT_ALL_1", 500);
				break;
			case 8:
				ad.spawnTentacles();
				addTimer(9, 8000);
				break;
			case 9:
				Functions.npcSayInRange(getActor(), 1500, NpcString.UGH_IFI_SEE_YOU_IN_THE_SPIRIT_WORLD_FIRST_ROUND_IS_ON_ME);
				addTimer(11, 1000);
				addTimer(10, 1600);
				addTimer(10, 2400);
				addTimer(10, 3600);
				break;
			case 10:
				broadCastScriptEvent("SHOUT_ALL_2", 500);
				break;
			case 11:
				getActor().doDie(getActor());
				break;
		}
	}

	@Override
	protected boolean canAttackCharacter(Creature target)
	{
		return false;
	}

	@Override
	public boolean checkAggression(Creature target)
	{
		return false;
	}
}
