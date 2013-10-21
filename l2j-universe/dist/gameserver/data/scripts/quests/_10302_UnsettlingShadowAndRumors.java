package quests;

import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class _10302_UnsettlingShadowAndRumors extends Quest implements ScriptFile
{
	//npc
	private static final int KANIBYS = 32898;
	private static final int ISHAEL = 32894;
	
	private static final int KES = 32901;
	private static final int KEY = 32903;
	private static final int KIK = 32902;
	

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _10302_UnsettlingShadowAndRumors()
	{
		super(false);
		addStartNpc(KANIBYS);
		addTalkId(KANIBYS);
		addTalkId(ISHAEL);
		
		addTalkId(KES);
		addTalkId(KEY);
		addTalkId(KIK);

		addLevelCheck(90,99);
		addQuestCompletedCheck(_10301_ShadowOfTerrorBlackishRedFog.class);
	}

	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		st.getPlayer();
		if(event.equalsIgnoreCase("32898-4.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		
		if(event.equalsIgnoreCase("32898-8.htm"))
		{		
			st.addExpAndSp(6728850, 755280);
			st.giveItems(57, 2177190);
			st.giveItems(34033, 1);
			st.playSound(SOUND_FINISH);
			st.exitCurrentQuest(false);
		}		

		if(event.equalsIgnoreCase("32894-1.htm"))
		{
			st.playSound(SOUND_MIDDLE);
			st.setCond(2);
		}	

		if(event.equalsIgnoreCase("32901-1.htm"))
		{
			st.playSound(SOUND_MIDDLE);
			st.setCond(3);
		}	

		if(event.equalsIgnoreCase("32903-1.htm"))
		{
			st.playSound(SOUND_MIDDLE);
			st.setCond(4);
		}	
		
		if(event.equalsIgnoreCase("32902-1.htm"))
		{
			st.playSound(SOUND_MIDDLE);
			st.setCond(5);
		}		

		if(event.equalsIgnoreCase("32894-5.htm"))
		{
			st.playSound(SOUND_MIDDLE);
			st.setCond(6);
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
		
		if(state == COMPLETED)
			return "32898-comp.htm";

		if(st.getPlayer().getLevel() < 90)
			return "32898-lvl.htm";		
		QuestState qs = st.getPlayer().getQuestState(_10301_ShadowOfTerrorBlackishRedFog.class);
		if(qs == null || !qs.isCompleted())
			return "32898-lvl.htm";	
			
		if(npcId == KANIBYS)
		{
			if(cond == 0)
				return "32898.htm";
			else if(cond >= 1 && cond < 6)
				return "32898-5.htm";	
			else if(cond == 6)
				return "32898-6.htm";
		}
		else if(npcId == ISHAEL)
		{
			if(cond == 1)
				return "32894.htm";
			else if(cond >= 2 && cond < 5)
				return "32894-2.htm";
			else if(cond == 5)
				return "32894-3.htm";
			else if(cond == 6)
				return "32894-6.htm";
		}
		else if(npcId == KES)
		{
			if(cond == 2)
				return "32901.htm";
			else
				return "32901-2.htm";
		}

		else if(npcId == KEY)
		{
			if(cond == 3)
				return "32903.htm";
			else
				return "32903-2.htm";
		}

		else if(npcId == KIK)
		{
			if(cond == 4)
				return "32902.htm";
			else
				return "32902-2.htm";
		}		
		return "noquest";
	}
}