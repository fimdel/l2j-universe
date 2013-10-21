package quests;

import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class _497_IncarnationOfGreedZellakaSolo extends Quest implements ScriptFile
{
	public static final int KARTIA_RESEARCH = 33647;
	public static final int CHALAKA = 19253;

	public _497_IncarnationOfGreedZellakaSolo()
	{
		super(true);
		addStartNpc(KARTIA_RESEARCH);
		addKillId(CHALAKA);
		addLevelCheck(85, 89);
	}

	public void onShutdown()
	{
	}

	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		if (event.equalsIgnoreCase("33647-4.htm"))
		{
			st.setCond(1);
			st.setState(2);
			st.playSound("ItemSound.quest_accept");
		}
		if (event.equalsIgnoreCase("33647-8.htm"))
		{
			st.giveItems(34930, 1L);
			st.unset("cond");
			st.playSound("ItemSound.quest_finish");
			st.exitCurrentQuest(this);
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
					return "33647-5.htm";
				return "33647.htm";
			}
			if (state == 2)
			{
				if (cond == 1)
					return "33647-6.htm";
				if (cond == 2)
				{
					return "33647-7.htm";
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