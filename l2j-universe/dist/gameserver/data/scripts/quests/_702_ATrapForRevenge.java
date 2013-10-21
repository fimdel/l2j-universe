/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package quests;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class _702_ATrapForRevenge extends Quest implements ScriptFile
{
	private static int PLENOS = 32563;
	private static int TENIUS = 32555;
	private static int DRAKES_FLESH = 13877;
	private static int LEONARD = 9628;
	private static int ADAMANTINE = 9629;
	private static int ORICHALCUM = 9630;
	private static int DRAK = 22612;
	private static int MUTATED_DRAKE_WING = 22611;
	
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
	
	public _702_ATrapForRevenge()
	{
		super(true);
		addStartNpc(PLENOS);
		addTalkId(PLENOS);
		addTalkId(TENIUS);
		addKillId(DRAK);
		addKillId(MUTATED_DRAKE_WING);
		addQuestItem(DRAKES_FLESH);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		int cond = st.getCond();
		String htmltext = event;
		if (event.equals("take") && (cond == 0))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
			htmltext = "plenos_q702_2.htm";
		}
		else if (event.equals("took_mission") && (cond == 1))
		{
			st.setCond(2);
			htmltext = "tenius_q702_3.htm";
			st.playSound(SOUND_MIDDLE);
		}
		else if (event.equals("hand_over") && (cond == 2))
		{
			int rand = Rnd.get(1, 3);
			htmltext = "tenius_q702_6.htm";
			st.takeItems(DRAKES_FLESH, -1);
			if (rand == 1)
			{
				st.giveItems(LEONARD, 3);
			}
			else if (rand == 2)
			{
				st.giveItems(ADAMANTINE, 3);
			}
			else if (rand == 3)
			{
				st.giveItems(ORICHALCUM, 3);
			}
			st.giveItems(ADENA_ID, 157200);
			st.playSound(SOUND_FINISH);
			st.exitCurrentQuest(true);
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int npcId = npc.getNpcId();
		int cond = st.getCond();
		QuestState GoodDayToFly = st.getPlayer().getQuestState(_10273_GoodDayToFly.class);
		if (npcId == PLENOS)
		{
			if (cond == 0)
			{
				if (st.getPlayer().getLevel() >= 78)
				{
					if ((GoodDayToFly != null) && GoodDayToFly.isCompleted())
					{
						htmltext = "plenos_q702_1.htm";
					}
					else
					{
						htmltext = "plenos_q702_1a.htm";
					}
				}
				else
				{
					htmltext = "plenos_q702_1b.htm";
					st.exitCurrentQuest(true);
				}
			}
			else
			{
				htmltext = "plenos_q702_1c.htm";
			}
		}
		else if (npcId == TENIUS)
		{
			if (cond == 1)
			{
				htmltext = "tenius_q702_1.htm";
			}
			else if ((cond == 2) && (st.getQuestItemsCount(DRAKES_FLESH) < 100))
			{
				htmltext = "tenius_q702_4.htm";
			}
			else if ((cond == 2) && (st.getQuestItemsCount(DRAKES_FLESH) >= 100))
			{
				htmltext = "tenius_q702_5.htm";
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		int npcId = npc.getNpcId();
		int cond = st.getCond();
		if ((cond == 2) && ((npcId == DRAK) || (npcId == MUTATED_DRAKE_WING)) && (st.getQuestItemsCount(DRAKES_FLESH) <= 100))
		{
			st.giveItems(DRAKES_FLESH, 1);
			st.playSound(SOUND_ITEMGET);
		}
		return null;
	}
}
