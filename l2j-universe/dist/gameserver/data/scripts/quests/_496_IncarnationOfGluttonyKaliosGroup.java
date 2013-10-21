package quests;

import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class _496_IncarnationOfGluttonyKaliosGroup extends Quest implements ScriptFile
{
	public static final int KARTIA_RESEARCH = 33647;
	public static final int KALIOS = 25884;

	public _496_IncarnationOfGluttonyKaliosGroup()
	{
		super(true);
		addStartNpc(KARTIA_RESEARCH);
		addKillId(KALIOS);
		addLevelCheck(95, 99);
	}

	public void onShutdown()
	{
	}

	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		if (event.equalsIgnoreCase("33647-2.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		return event;
	}

	public String onTalk(NpcInstance npc, QuestState st)
	{
		int npcId = npc.getNpcId();
		int state = st.getState();
		int cond = st.getCond();
		if (npcId == KARTIA_RESEARCH)
		{
			if (state == 1)
			{
				if (!isAvailableFor(st.getPlayer()) || !st.isNowAvailableByTime())
					return "33647-3.htm";
				return "33647.htm";
			}
			if (state == 2)
			{
				if (cond == 1)
					return "33647-2.htm";
				if (cond == 2)
				{
					st.giveItems(34929, 1L);
					st.unset("cond");
					st.playSound("ItemSound.quest_finish");
					st.exitCurrentQuest(this);
					return "33647-4.htm";
				}
			}
		}
		return "noquest";
	}

	public void onLoad()
	{
	}

	public void onReload()
	{
	} 

	public String onKill(NpcInstance npc, QuestState st)
	{
		int cond = st.getCond();
		if ((cond != 1) || (npc == null))
			return null;
		st.setCond(2);
		return null;
	}
}
