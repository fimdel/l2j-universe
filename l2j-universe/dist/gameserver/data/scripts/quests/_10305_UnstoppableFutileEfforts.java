package quests;

import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class _10305_UnstoppableFutileEfforts extends Quest implements ScriptFile
{
	public static final String A_LIST = "a_list";
	//npc
	private static final int NOETI = 32895;
	

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _10305_UnstoppableFutileEfforts()
	{
		super(false);
		addStartNpc(NOETI);
		addTalkId(NOETI);

		addLevelCheck(90,99);
		addQuestCompletedCheck(_10302_UnsettlingShadowAndRumors.class);
		addKillNpcWithLog(1, A_LIST, 5, 22866,22882,22890,22898,22890,22874,22870,22886,22910,22902,22894,22878);
	}

	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		st.getPlayer();
		if(event.equalsIgnoreCase("32895-6.htm"))
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
		
		if(state == COMPLETED)
			return "32895-comp.htm";

		if(st.getPlayer().getLevel() < 90)
			return "32895-lvl.htm";		
		QuestState qs = st.getPlayer().getQuestState(_10302_UnsettlingShadowAndRumors.class);
		if(qs == null || !qs.isCompleted())
			return "32895-lvl.htm";	
			
		if(npcId == NOETI)
		{
			if(cond == 0)
				return "32895.htm";
			else if(cond == 1)
				return "32895-7.htm";	
			else if(cond == 2)
			{
				st.addExpAndSp(34971975, 12142200);
				st.giveItems(57, 1007735);
				st.playSound(SOUND_FINISH);
				st.exitCurrentQuest(false);			
				return "32895-8.htm";
			}	
		}
		return "noquest";
	}

	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if(qs.getCond() != 1)
			return null;

		if(updateKill(npc, qs))
		{
			qs.unset(A_LIST);
			qs.setCond(2);
		}

		return null;
	}	
}