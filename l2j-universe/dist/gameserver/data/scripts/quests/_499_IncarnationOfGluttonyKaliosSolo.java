package quests;

import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class _499_IncarnationOfGluttonyKaliosSolo extends Quest implements ScriptFile
{
	public static final int KARTIA_RESEARCH = 33647;
	public static final int KALIOS = 19255;
	
	public _499_IncarnationOfGluttonyKaliosSolo()
	{
		super(true);
		addStartNpc(KARTIA_RESEARCH);
		addKillId(KALIOS);
		addLevelCheck(95, 99);
	}

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		if(event.equalsIgnoreCase("33647-4.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		if(event.equalsIgnoreCase("33647-8.htm"))
		{		
			st.giveItems(34932, 1);
			st.unset("cond");
			st.playSound(SOUND_FINISH);
			st.exitCurrentQuest(this);
		}	
		return event;
	}

	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		int npcId = npc.getNpcId();
		int state = st.getState();
		int cond = st.getCond();
		if(npcId == KARTIA_RESEARCH)
		{
			if(state == 1)
			{			
				if (!isAvailableFor(st.getPlayer()) || !st.isNowAvailableByTime())
					return "33647-5.htm";			
				return "33647.htm";
			}
			if(state == 2)
			{
				if(cond == 1)
					return "33647-6.htm";
				if(cond == 2)
				{
			
					return "33647-7.htm";
				}					
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
		st.setCond(2);
		return null;
	}	
}