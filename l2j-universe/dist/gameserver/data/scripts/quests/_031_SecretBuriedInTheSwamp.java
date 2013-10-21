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

public class _031_SecretBuriedInTheSwamp extends Quest implements ScriptFile
{
	int ABERCROMBIE = 31555;
	int FORGOTTEN_MONUMENT_1 = 31661;
	int FORGOTTEN_MONUMENT_2 = 31662;
	int FORGOTTEN_MONUMENT_3 = 31663;
	int FORGOTTEN_MONUMENT_4 = 31664;
	int CORPSE_OF_DWARF = 31665;
	int KRORINS_JOURNAL = 7252;
	
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
	
	public _031_SecretBuriedInTheSwamp()
	{
		super(false);
		addStartNpc(ABERCROMBIE);
		for (int i = 31661; i <= 31665; i++)
		{
			addTalkId(i);
		}
		addQuestItem(KRORINS_JOURNAL);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		int cond = st.getCond();
		String htmltext = event;
		if (event.equals("31555-1.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equals("31665-1.htm") && (cond == 1))
		{
			st.setCond(2);
			st.playSound(SOUND_ITEMGET);
			st.giveItems(KRORINS_JOURNAL, 1);
		}
		else if (event.equals("31555-4.htm") && (cond == 2))
		{
			st.setCond(3);
		}
		else if (event.equals("31661-1.htm") && (cond == 3))
		{
			st.setCond(4);
		}
		else if (event.equals("31662-1.htm") && (cond == 4))
		{
			st.setCond(5);
		}
		else if (event.equals("31663-1.htm") && (cond == 5))
		{
			st.setCond(6);
		}
		else if (event.equals("31664-1.htm") && (cond == 6))
		{
			st.setCond(7);
			st.playSound(SOUND_MIDDLE);
		}
		else if (event.equals("31555-7.htm") && (cond == 7))
		{
			st.takeItems(KRORINS_JOURNAL, -1);
			st.addExpAndSp(1650970, 1631640);
			st.giveItems(ADENA_ID, 343430);
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
		if (npcId == ABERCROMBIE)
		{
			if (cond == 0)
			{
				if (st.getPlayer().getLevel() >= 66)
				{
					htmltext = "31555-0.htm";
				}
				else
				{
					htmltext = "31555-0a.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if (cond == 1)
			{
				htmltext = "31555-2.htm";
			}
			else if (cond == 2)
			{
				htmltext = "31555-3.htm";
			}
			else if (cond == 3)
			{
				htmltext = "31555-5.htm";
			}
			else if (cond == 7)
			{
				htmltext = "31555-6.htm";
			}
		}
		else if (npcId == CORPSE_OF_DWARF)
		{
			if (cond == 1)
			{
				htmltext = "31665-0.htm";
			}
			else if (cond == 2)
			{
				htmltext = "31665-2.htm";
			}
		}
		else if (npcId == FORGOTTEN_MONUMENT_1)
		{
			if (cond == 3)
			{
				htmltext = "31661-0.htm";
			}
			else if (cond > 3)
			{
				htmltext = "31661-2.htm";
			}
		}
		else if (npcId == FORGOTTEN_MONUMENT_2)
		{
			if (cond == 4)
			{
				htmltext = "31662-0.htm";
			}
			else if (cond > 4)
			{
				htmltext = "31662-2.htm";
			}
		}
		else if (npcId == FORGOTTEN_MONUMENT_3)
		{
			if (cond == 5)
			{
				htmltext = "31663-0.htm";
			}
			else if (cond > 5)
			{
				htmltext = "31663-2.htm";
			}
		}
		else if (npcId == FORGOTTEN_MONUMENT_4)
		{
			if (cond == 6)
			{
				htmltext = "31664-0.htm";
			}
			else if (cond > 6)
			{
				htmltext = "31664-2.htm";
			}
		}
		return htmltext;
	}
}
