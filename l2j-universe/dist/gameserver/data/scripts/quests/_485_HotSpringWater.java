package quests;

import org.apache.commons.lang3.ArrayUtils;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class _485_HotSpringWater extends Quest implements ScriptFile
{
	//npc
	public static final int GUIDE = 33463;
	public static final int VALDEMOR = 30844;
	
	//mobs
	private final static int[] Mobs = { 21314, 21315, 21316, 21317, 21318, 21319, 21320, 21321, 21322, 21323 };
	
	//q items
	public static final int WATER = 19497;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _485_HotSpringWater()
	{
		super(true);
		addStartNpc(GUIDE);
		addTalkId(VALDEMOR);
		addKillId(Mobs);
		addQuestItem(WATER);
		addLevelCheck(70, 74);
	}

	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		st.getPlayer();
		if(event.equalsIgnoreCase("33463-3.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		return event;
	}

	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		st.getPlayer();
		int npcId = npc.getNpcId();
		int state = st.getState();
		int cond = st.getCond();
		if(npcId == GUIDE)
		{
			if(state == 1)
			{
				if(!st.isNowAvailableByTime())
					return "33463-comp.htm";
				return "33463.htm";
			}
			else if(state == 2)
			{
				if(cond == 1)
					return "33463-4.htm";
				if(cond == 2)
					return "33463-5.htm";
			}
			
		}
		if(npcId == VALDEMOR && state == 2)
		{
			if(cond == 1)
				return "30844-1.htm";
			else if(cond == 2)
			{
				st.addExpAndSp(9483000, 9470430);
				st.takeItems(WATER, -1);
				st.giveItems(57, 371745);
				st.unset("cond");
				st.playSound(SOUND_FINISH);
				st.exitCurrentQuest(this);			
				return "30844.htm"; //no further html do here
			}	
		}		
		return "noquest";
	}
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		int cond = st.getCond();
		if(cond != 1 || npc == null)
			return null;
		if(ArrayUtils.contains(Mobs, npc.getNpcId()) && Rnd.chance(50))
		{
			st.giveItems(WATER, 1);
			st.playSound(SOUND_MIDDLE);
		}	
		if(st.getQuestItemsCount(WATER) >= 40)
			st.setCond(2);
			
		return null;
	}
}