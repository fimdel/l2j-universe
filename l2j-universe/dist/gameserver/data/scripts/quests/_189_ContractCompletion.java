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

public class _189_ContractCompletion extends Quest implements ScriptFile
{
	private static final int Kusto = 30512;
	private static final int Lorain = 30673;
	private static final int Luka = 30621;
	private static final int Shegfield = 30068;
	private static final int Metal = 10370;
	
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
	
	public _189_ContractCompletion()
	{
		super(false);
		addTalkId(Kusto, Luka, Lorain, Shegfield);
		addFirstTalkId(Luka);
		addQuestItem(Metal);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if (event.equalsIgnoreCase("blueprint_seller_luka_q0189_03.htm"))
		{
			st.playSound(SOUND_ACCEPT);
			st.setCond(1);
			st.giveItems(Metal, 1);
		}
		else if (event.equalsIgnoreCase("researcher_lorain_q0189_02.htm"))
		{
			st.playSound(SOUND_MIDDLE);
			st.setCond(2);
			st.takeItems(Metal, -1);
		}
		else if (event.equalsIgnoreCase("shegfield_q0189_03.htm"))
		{
			st.setCond(3);
			st.playSound(SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("head_blacksmith_kusto_q0189_02.htm"))
		{
			st.giveItems(ADENA_ID, 141360);
			st.exitCurrentQuest(false);
			st.playSound(SOUND_FINISH);
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
			if (npcId == Luka)
			{
				if (cond == 0)
				{
					if (st.getPlayer().getLevel() < 42)
					{
						htmltext = "blueprint_seller_luka_q0189_02.htm";
					}
					else
					{
						htmltext = "blueprint_seller_luka_q0189_01.htm";
					}
				}
				else if (cond == 1)
				{
					htmltext = "blueprint_seller_luka_q0189_04.htm";
				}
			}
			else if (npcId == Lorain)
			{
				if (cond == 1)
				{
					htmltext = "researcher_lorain_q0189_01.htm";
				}
				else if (cond == 2)
				{
					htmltext = "researcher_lorain_q0189_03.htm";
				}
				else if (cond == 3)
				{
					htmltext = "researcher_lorain_q0189_04.htm";
					st.setCond(4);
					st.playSound(SOUND_MIDDLE);
				}
				else if (cond == 4)
				{
					htmltext = "researcher_lorain_q0189_05.htm";
				}
			}
			else if (npcId == Shegfield)
			{
				if (cond == 2)
				{
					htmltext = "shegfield_q0189_01.htm";
				}
				else if (cond == 3)
				{
					htmltext = "shegfield_q0189_04.htm";
				}
			}
			else if (npcId == Kusto)
			{
				if (cond == 4)
				{
					htmltext = "head_blacksmith_kusto_q0189_01.htm";
				}
			}
		}
		return htmltext;
	}
	
	@Override
	public String onFirstTalk(NpcInstance npc, Player player)
	{
		QuestState qs = player.getQuestState(_186_ContractExecution.class);
		if ((qs != null) && qs.isCompleted() && (player.getQuestState(getClass()) == null))
		{
			newQuestState(player, STARTED);
		}
		return "";
	}
}
