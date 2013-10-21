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

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class _190_LostDream extends Quest implements ScriptFile
{
	private static final int Kusto = 30512;
	private static final int Lorain = 30673;
	private static final int Nikola = 30621;
	private static final int Juris = 30113;
	
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
	
	public _190_LostDream()
	{
		super(false);
		addTalkId(Kusto, Nikola, Lorain, Juris);
		addFirstTalkId(Kusto);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if (event.equalsIgnoreCase("head_blacksmith_kusto_q0190_03.htm"))
		{
			st.playSound(SOUND_ACCEPT);
			st.setCond(1);
		}
		else if (event.equalsIgnoreCase("head_blacksmith_kusto_q0190_06.htm"))
		{
			st.playSound(SOUND_MIDDLE);
			st.setCond(3);
		}
		else if (event.equalsIgnoreCase("juria_q0190_03.htm"))
		{
			st.setCond(2);
			st.playSound(SOUND_MIDDLE);
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int npcId = npc.getNpcId();
		int cond = st.getCond();
		if (st.getState() == STARTED)
		{
			if (npcId == Kusto)
			{
				if (cond == 0)
				{
					if (st.getPlayer().getLevel() < 42)
					{
						htmltext = "head_blacksmith_kusto_q0190_02.htm";
					}
					else
					{
						htmltext = "head_blacksmith_kusto_q0190_01.htm";
					}
				}
				else if (cond == 1)
				{
					htmltext = "head_blacksmith_kusto_q0190_04.htm";
				}
				else if (cond == 2)
				{
					htmltext = "head_blacksmith_kusto_q0190_05.htm";
				}
				else if (cond == 3)
				{
					htmltext = "head_blacksmith_kusto_q0190_07.htm";
				}
				else if (cond == 5)
				{
					htmltext = "head_blacksmith_kusto_q0190_08.htm";
					st.giveItems(ADENA_ID, 127224);
					st.exitCurrentQuest(false);
					st.playSound(SOUND_FINISH);
				}
			}
			else if (npcId == Juris)
			{
				if (cond == 1)
				{
					htmltext = "juria_q0190_01.htm";
				}
				else if (cond == 2)
				{
					htmltext = "juria_q0190_04.htm";
				}
			}
			else if (npcId == Lorain)
			{
				if (cond == 3)
				{
					htmltext = "researcher_lorain_q0190_01.htm";
					st.playSound(SOUND_MIDDLE);
					st.setCond(4);
				}
				else if (cond == 4)
				{
					htmltext = "researcher_lorain_q0190_02.htm";
				}
			}
			else if (npcId == Nikola)
			{
				if (cond == 4)
				{
					htmltext = "maestro_nikola_q0190_01.htm";
					st.playSound(SOUND_MIDDLE);
					st.setCond(5);
				}
				else if (cond == 5)
				{
					htmltext = "maestro_nikola_q0190_02.htm";
				}
			}
		}
		return htmltext;
	}
	
	@Override
	public String onFirstTalk(NpcInstance npc, Player player)
	{
		QuestState qs = player.getQuestState(_187_NikolasHeart.class);
		if ((qs != null) && qs.isCompleted() && (player.getQuestState(getClass()) == null))
		{
			newQuestState(player, STARTED);
		}
		return "";
	}
}
