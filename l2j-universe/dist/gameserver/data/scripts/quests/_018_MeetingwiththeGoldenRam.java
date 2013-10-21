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

public class _018_MeetingwiththeGoldenRam extends Quest implements ScriptFile
{
	private static final int SUPPLY_BOX = 7245;
	
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
	
	public _018_MeetingwiththeGoldenRam()
	{
		super(false);
		addStartNpc(31314);
		addTalkId(31314);
		addTalkId(31315);
		addTalkId(31555);
		addQuestItem(SUPPLY_BOX);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if (event.equals("warehouse_chief_donal_q0018_0104.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equals("freighter_daisy_q0018_0201.htm"))
		{
			st.setCond(2);
			st.giveItems(SUPPLY_BOX, 1);
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equals("supplier_abercrombie_q0018_0301.htm"))
		{
			st.takeItems(SUPPLY_BOX, -1);
			st.addExpAndSp(330194, 326328);
			st.giveItems(ADENA_ID, 68686);
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
		if (npcId == 31314)
		{
			if (cond == 0)
			{
				if (st.getPlayer().getLevel() >= 66)
				{
					htmltext = "warehouse_chief_donal_q0018_0101.htm";
				}
				else
				{
					htmltext = "warehouse_chief_donal_q0018_0103.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if (cond == 1)
			{
				htmltext = "warehouse_chief_donal_q0018_0105.htm";
			}
		}
		else if (npcId == 31315)
		{
			if (cond == 1)
			{
				htmltext = "freighter_daisy_q0018_0101.htm";
			}
			else if (cond == 2)
			{
				htmltext = "freighter_daisy_q0018_0202.htm";
			}
		}
		else if (npcId == 31555)
		{
			if ((cond == 2) && (st.getQuestItemsCount(SUPPLY_BOX) == 1))
			{
				htmltext = "supplier_abercrombie_q0018_0201.htm";
			}
		}
		return htmltext;
	}
}
