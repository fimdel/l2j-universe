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

import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class _10270_BirthOfTheSeed extends Quest implements ScriptFile
{
	private static int PLENOS = 32563;
	private static int ARTIUS = 32559;
	private static int LELIKIA = 32567;
	private static int GINBY = 32566;
	private static int Yehan_Klodekus_Badge = 13868;
	private static int Yehan_Klanikus_Badge = 13869;
	private static int Lich_Crystal = 13870;
	private static int Yehan_Klodekus = 25665;
	private static int Yehan_Klanikus = 25666;
	private static int Cohemenes = 25634;
	
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
	
	public _10270_BirthOfTheSeed()
	{
		super(true);
		addStartNpc(PLENOS);
		addTalkId(PLENOS);
		addTalkId(ARTIUS);
		addTalkId(LELIKIA);
		addTalkId(GINBY);
		addKillId(Yehan_Klodekus);
		addKillId(Yehan_Klanikus);
		addKillId(Cohemenes);
		addQuestItem(Yehan_Klodekus_Badge);
		addQuestItem(Yehan_Klanikus_Badge);
		addQuestItem(Lich_Crystal);
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
			htmltext = "plenos_q10270_2.htm";
		}
		else if (event.equals("took_mission") && (cond == 1))
		{
			st.setCond(2);
			htmltext = "artius_q10270_3.htm";
			st.playSound(SOUND_MIDDLE);
		}
		else if (event.equals("hand_over") && (cond == 2))
		{
			st.takeItems(Yehan_Klodekus_Badge, -1);
			st.takeItems(Yehan_Klanikus_Badge, -1);
			st.takeItems(Lich_Crystal, -1);
			htmltext = "artius_q10270_6.htm";
		}
		else if (event.equals("artius_q10270_7.htm") && (cond == 2))
		{
			st.setCond(3);
			st.playSound(SOUND_MIDDLE);
		}
		else if (event.equals("lelika") && (cond == 3))
		{
			st.setCond(4);
			htmltext = "artius_q10270_9.htm";
			st.playSound(SOUND_MIDDLE);
		}
		else if (event.equals("lelikia_q10270_2.htm") && (cond == 4))
		{
			st.setCond(5);
			st.playSound(SOUND_MIDDLE);
		}
		else if (event.equals("reward") && (cond == 5))
		{
			htmltext = "artius_q10270_11.htm";
			st.giveItems(ADENA_ID, 236510);
			st.addExpAndSp(1109665, 1229015);
			st.playSound(SOUND_FINISH);
			st.exitCurrentQuest(false);
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int npcId = npc.getNpcId();
		int cond = st.getCond();
		if (npcId == PLENOS)
		{
			if (cond == 0)
			{
				if (st.getPlayer().getLevel() >= 75)
				{
					htmltext = "plenos_q10270_1.htm";
				}
				else
				{
					htmltext = "plenos_q10270_1a.htm";
					st.exitCurrentQuest(true);
				}
			}
		}
		else if (npcId == ARTIUS)
		{
			if (cond == 1)
			{
				htmltext = "artius_q10270_1.htm";
			}
			else if ((cond == 2) && ((st.getQuestItemsCount(Yehan_Klodekus_Badge) == 0) || (st.getQuestItemsCount(Yehan_Klanikus_Badge) == 0) || (st.getQuestItemsCount(Lich_Crystal) == 0)))
			{
				htmltext = "artius_q10270_4.htm";
			}
			else if ((cond == 2) && (st.getQuestItemsCount(Yehan_Klodekus_Badge) == 1) && (st.getQuestItemsCount(Yehan_Klanikus_Badge) == 1) && (st.getQuestItemsCount(Lich_Crystal) == 1))
			{
				htmltext = "artius_q10270_5.htm";
			}
			else if (cond == 3)
			{
				htmltext = "artius_q10270_8.htm";
			}
			else if (cond == 5)
			{
				htmltext = "artius_q10270_10.htm";
			}
		}
		else if (npcId == GINBY)
		{
			if (cond == 4)
			{
				htmltext = "ginby_q10270_1.htm";
			}
		}
		else if (npcId == LELIKIA)
		{
			if (cond == 4)
			{
				htmltext = "lelikia_q10270_1.htm";
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		int npcId = npc.getNpcId();
		int cond = st.getCond();
		if (cond == 2)
		{
			if ((npcId == Yehan_Klodekus) && (st.getQuestItemsCount(Yehan_Klodekus_Badge) < 1))
			{
				st.giveItems(Yehan_Klodekus_Badge, 1);
				st.playSound(SOUND_ITEMGET);
			}
			else if ((npcId == Yehan_Klanikus) && (st.getQuestItemsCount(Yehan_Klanikus_Badge) < 1))
			{
				st.giveItems(Yehan_Klanikus_Badge, 1);
				st.playSound(SOUND_ITEMGET);
			}
			else if ((npcId == Cohemenes) && (st.getQuestItemsCount(Lich_Crystal) < 1))
			{
				st.giveItems(Lich_Crystal, 1);
				st.playSound(SOUND_ITEMGET);
			}
		}
		return null;
	}
}
