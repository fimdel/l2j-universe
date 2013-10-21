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

public class _014_WhereaboutsoftheArchaeologist extends Quest implements ScriptFile
{
	private static final int LETTER_TO_ARCHAEOLOGIST = 7253;
	
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
	
	public _014_WhereaboutsoftheArchaeologist()
	{
		super(false);
		addStartNpc(31263);
		addTalkId(31538);
		addQuestItem(LETTER_TO_ARCHAEOLOGIST);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if (event.equalsIgnoreCase("trader_liesel_q0014_0104.htm"))
		{
			st.setCond(1);
			st.giveItems(LETTER_TO_ARCHAEOLOGIST, 1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("explorer_ghost_a_q0014_0201.htm"))
		{
			st.takeItems(LETTER_TO_ARCHAEOLOGIST, -1);
			st.addExpAndSp(853088, 933748);
			st.giveItems(ADENA_ID, 181320);
			st.playSound(SOUND_FINISH);
			st.exitCurrentQuest(false);
			return "explorer_ghost_a_q0014_0201.htm";
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int npcId = npc.getNpcId();
		int cond = st.getCond();
		if (npcId == 31263)
		{
			if (cond == 0)
			{
				if (st.getPlayer().getLevel() >= 74)
				{
					htmltext = "trader_liesel_q0014_0101.htm";
				}
				else
				{
					htmltext = "trader_liesel_q0014_0103.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if (cond == 1)
			{
				htmltext = "trader_liesel_q0014_0104.htm";
			}
		}
		else if (npcId == 31538)
		{
			if ((cond == 1) && (st.getQuestItemsCount(LETTER_TO_ARCHAEOLOGIST) == 1))
			{
				htmltext = "explorer_ghost_a_q0014_0101.htm";
			}
		}
		return htmltext;
	}
}
