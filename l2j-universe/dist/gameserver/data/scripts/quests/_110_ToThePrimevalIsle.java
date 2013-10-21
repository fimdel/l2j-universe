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

public class _110_ToThePrimevalIsle extends Quest implements ScriptFile
{
	int ANTON = 31338;
	int MARQUEZ = 32113;
	int ANCIENT_BOOK = 8777;
	
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
	
	public _110_ToThePrimevalIsle()
	{
		super(false);
		addStartNpc(ANTON);
		addTalkId(ANTON);
		addTalkId(MARQUEZ);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if (event.equals("1"))
		{
			htmltext = "scroll_seller_anton_q0110_05.htm";
			st.setCond(1);
			st.giveItems(ANCIENT_BOOK, 1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equals("2") && (st.getQuestItemsCount(ANCIENT_BOOK) > 0))
		{
			htmltext = "marquez_q0110_05.htm";
			st.playSound(SOUND_FINISH);
			st.giveItems(ADENA_ID, 189208);
			st.addExpAndSp(887732, 983212);
			st.takeItems(ANCIENT_BOOK, -1);
			st.exitCurrentQuest(false);
		}
		else if (event.equals("3"))
		{
			htmltext = "marquez_q0110_06.htm";
			st.exitCurrentQuest(true);
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int npcId = npc.getNpcId();
		int id = st.getState();
		int cond = st.getCond();
		if (id == CREATED)
		{
			if (st.getPlayer().getLevel() >= 75)
			{
				htmltext = "scroll_seller_anton_q0110_01.htm";
			}
			else
			{
				st.exitCurrentQuest(true);
				htmltext = "scroll_seller_anton_q0110_02.htm";
			}
		}
		else if (npcId == ANTON)
		{
			if (cond == 1)
			{
				htmltext = "scroll_seller_anton_q0110_07.htm";
			}
		}
		else if (id == STARTED)
		{
			if ((npcId == MARQUEZ) && (cond == 1))
			{
				if (st.getQuestItemsCount(ANCIENT_BOOK) == 0)
				{
					htmltext = "marquez_q0110_07.htm";
				}
				else
				{
					htmltext = "marquez_q0110_01.htm";
				}
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		return null;
	}
}
