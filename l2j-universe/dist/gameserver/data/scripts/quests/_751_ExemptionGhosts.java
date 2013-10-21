package quests;

import lineage2.gameserver.model.Party;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

import org.apache.commons.lang3.ArrayUtils;

public class _751_ExemptionGhosts extends Quest implements ScriptFile
{
	private static final int Roderik = 30631;
	private static final int Deadmans_Flesh = 34971;
	private static final int[] Mobs = {23199, 23201, 23202, 23200, 23203, 23204, 23205, 23206, 23207, 23208, 23209, 23242, 23243, 23244, 23245};
	private int Scaldisect = 23212;
	private static final String SCALDISECT_KILL = "Scaldisect";

	public _751_ExemptionGhosts()
	{
		super(PARTY_ALL);
		addStartNpc(Roderik);
		addKillId(Mobs);
		addKillId(Scaldisect);
		addQuestItem(Deadmans_Flesh);
		addKillNpcWithLog(1, SCALDISECT_KILL, 1, Scaldisect);
	}

	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if(event.equalsIgnoreCase("30631-3.htm"))
		{
			st.setState(STARTED);
			st.setCond(1);
			st.playSound(SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("30631-5.htm"))
		{
			st.getPlayer().addExpAndSp(600000000, 0);
			st.takeItems(Deadmans_Flesh, 40);
			st.unset(SCALDISECT_KILL);
			st.setState(COMPLETED);
			st.playSound(SOUND_FINISH);
			st.exitCurrentQuest(this);
		}
		return htmltext;
	}

	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int cond = st.getCond();
		if(npc.getNpcId() == Roderik)
		{
			switch(st.getState())
			{
				case CREATED:
					if(st.getPlayer().getLevel() >= 95)
					{
						if(st.isNowAvailable())
						{
							htmltext = "30631.htm";
						}
						else
						{
							htmltext = "30631-0.htm";
						}
					}
					else
					{
						htmltext = "lvl.htm";
						st.exitCurrentQuest(true);
					}
					break;
				case STARTED:
					if(cond == 1)
					{
						htmltext = "30631-3.htm";
					}
					else if(cond == 2)
					{
						htmltext = "30631-4.htm";
					}
					break;
			}
		}

		return htmltext;
	}

	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		int cond = st.getCond();
		boolean doneKill = updateKill(npc, st);
		if(cond == 1)
		{
			if(ArrayUtils.contains(Mobs, npc.getNpcId()))
			{
				Party party = st.getPlayer().getParty();
				if(party != null)
				{
					for(Player member : party.getPartyMembers())
					{
						QuestState qs = member.getQuestState(getClass());
						if(qs != null && qs.isStarted())
						{
							if(st.getQuestItemsCount(Deadmans_Flesh) < 40)
							{
								qs.giveItems(Deadmans_Flesh, 1);
								qs.playSound(SOUND_ITEMGET);
								if(doneKill && st.getQuestItemsCount(Deadmans_Flesh) == 40)
								{
									st.setCond(2);
								}
							}
						}
					}
				}
				else
				{
					if(st.getQuestItemsCount(Deadmans_Flesh) < 50)
					{
						st.giveItems(Deadmans_Flesh, 1);
						st.playSound(SOUND_ITEMGET);
						if(doneKill && st.getQuestItemsCount(Deadmans_Flesh) == 40)
						{
							st.setCond(2);
						}
					}
				}
			}
			if(npc.getNpcId() == Scaldisect)
			{
				Party party = st.getPlayer().getParty();
				if(party != null)
				{
					for(Player member : party.getPartyMembers())
					{
						QuestState qs = member.getQuestState(getClass());
						if(qs != null && qs.isStarted())
						{
							updateKill(npc, st);
							if(st.getQuestItemsCount(Deadmans_Flesh) == 40)
							{
								st.setCond(2);
							}
						}
					}
				}
				else
				{
					updateKill(npc, st);
					if(st.getQuestItemsCount(Deadmans_Flesh) == 40)
					{
						st.setCond(2);
					}
				}
			}
		}
		return null;
	}

	@Override
	public void onLoad()
	{
	}

	@Override
	public void onReload()
	{
	}

	@Override
	public void onShutdown()
	{
	}
}