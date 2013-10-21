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

public class _019_GoToThePastureland extends Quest implements ScriptFile
{
	int VLADIMIR = 31302;
	int TUNATUN = 31537;
	int BEAST_MEAT = 7547;
	
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
	
	public _019_GoToThePastureland()
	{
		super(false);
		addStartNpc(VLADIMIR);
		addTalkId(VLADIMIR);
		addTalkId(TUNATUN);
		addQuestItem(BEAST_MEAT);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if (event.equals("trader_vladimir_q0019_0104.htm"))
		{
			st.giveItems(BEAST_MEAT, 1);
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		if (event.equals("beast_herder_tunatun_q0019_0201.htm"))
		{
			st.takeItems(BEAST_MEAT, -1);
			st.addExpAndSp(1456218, 1672008);
			st.giveItems(ADENA_ID, 299928);
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
		if (npcId == VLADIMIR)
		{
			if (cond == 0)
			{
				if (st.getPlayer().getLevel() >= 63)
				{
					htmltext = "trader_vladimir_q0019_0101.htm";
				}
				else
				{
					htmltext = "trader_vladimir_q0019_0103.htm";
					st.exitCurrentQuest(true);
				}
			}
			else
			{
				htmltext = "trader_vladimir_q0019_0105.htm";
			}
		}
		else if (npcId == TUNATUN)
		{
			if (st.getQuestItemsCount(BEAST_MEAT) >= 1)
			{
				htmltext = "beast_herder_tunatun_q0019_0101.htm";
			}
			else
			{
				htmltext = "beast_herder_tunatun_q0019_0202.htm";
				st.exitCurrentQuest(true);
			}
		}
		return htmltext;
	}
}
