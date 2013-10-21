package quests;

import org.apache.commons.lang3.ArrayUtils;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class _467_TheOppressorAndTheOppressed extends Quest implements ScriptFile
{
	//npc
	public static final int GUIDE = 33463;
	public static final int DASMOND = 30855;
	
	//mobs
	private final int[] Mobs = {20650, 20648, 20647, 20649};
	
	//q items
	public static final int CLEAR_CORE = 19488;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _467_TheOppressorAndTheOppressed()
	{
		super(true);
		addStartNpc(GUIDE);
		addTalkId(DASMOND);
		addKillId(Mobs);
		addQuestItem(CLEAR_CORE);
		addLevelCheck(60, 64);
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
				{
					return "33463-comp.htm";
				}
				return "33463.htm";
			}
			if(state == 2)
			{
				if(cond == 1)
					return "33463-4.htm";
					
			}
		}
		if(npcId == DASMOND && state == 2)
		{
			if(cond == 1)
				return "30855-1.htm";
			if(cond == 2)
			{
				st.giveItems(57,194000);
				st.addExpAndSp(1879400, 1782000);
				st.takeItems(CLEAR_CORE, -1);
				st.unset("cond");
				st.playSound(SOUND_FINISH);
				st.exitCurrentQuest(this);		
				return "30855.htm"; //no further html do here
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
			st.giveItems(CLEAR_CORE, 1);
		}
		if(st.getQuestItemsCount(CLEAR_CORE) >= 30)
			st.setCond(2);
			
		return null;
	}
}