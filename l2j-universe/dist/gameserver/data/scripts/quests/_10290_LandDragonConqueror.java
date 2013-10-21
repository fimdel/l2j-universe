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

public class _10290_LandDragonConqueror extends Quest implements ScriptFile
{
	private static final int Theodric = 30755;
	private static final int ShabbyNecklace = 15522;
	private static final int MiracleNecklace = 15523;
	private static final int UltimateAntharas = 29068;
	
	public _10290_LandDragonConqueror()
	{
		super(PARTY_ALL);
		addStartNpc(Theodric);
		addQuestItem(ShabbyNecklace, MiracleNecklace);
		addKillId(UltimateAntharas);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if (event.equalsIgnoreCase("theodric_q10290_04.htm"))
		{
			st.setState(STARTED);
			st.setCond(1);
			st.playSound(SOUND_ACCEPT);
			st.giveItems(ShabbyNecklace, 1);
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int npcId = npc.getNpcId();
		int cond = st.getCond();
		if (npcId == Theodric)
		{
			if (cond == 0)
			{
				if ((st.getPlayer().getLevel() >= 83) && (st.getQuestItemsCount(3865) >= 1))
				{
					htmltext = "theodric_q10290_01.htm";
				}
				else if (st.getQuestItemsCount(3865) < 1)
				{
					htmltext = "theodric_q10290_00a.htm";
				}
				else
				{
					htmltext = "theodric_q10290_00.htm";
				}
			}
			else if (cond == 1)
			{
				htmltext = "theodric_q10290_05.htm";
			}
			else if (cond == 2)
			{
				if (st.getQuestItemsCount(MiracleNecklace) >= 1)
				{
					htmltext = "theodric_q10290_07.htm";
					st.takeAllItems(MiracleNecklace);
					st.giveItems(8568, 1);
					st.giveItems(ADENA_ID, 131236);
					st.addExpAndSp(702557, 76334);
					st.playSound(SOUND_FINISH);
					st.setState(COMPLETED);
					st.exitCurrentQuest(false);
				}
				else
				{
					htmltext = "theodric_q10290_06.htm";
				}
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		int npcId = npc.getNpcId();
		int cond = st.getCond();
		if ((cond == 1) && (npcId == UltimateAntharas))
		{
			st.takeAllItems(ShabbyNecklace);
			st.giveItems(MiracleNecklace, 1);
			st.setCond(2);
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
