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

public class _011_SecretMeetingWithKetraOrcs extends Quest implements ScriptFile
{
	int CADMON = 31296;
	int LEON = 31256;
	int WAHKAN = 31371;
	int MUNITIONS_BOX = 7231;
	
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
	
	public _011_SecretMeetingWithKetraOrcs()
	{
		super(false);
		addStartNpc(CADMON);
		addTalkId(LEON);
		addTalkId(WAHKAN);
		addQuestItem(MUNITIONS_BOX);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if (event.equalsIgnoreCase("guard_cadmon_q0011_0104.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("trader_leon_q0011_0201.htm"))
		{
			st.giveItems(MUNITIONS_BOX, 1);
			st.setCond(2);
			st.playSound(SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("herald_wakan_q0011_0301.htm"))
		{
			st.takeItems(MUNITIONS_BOX, 1);
			st.addExpAndSp(82045, 6047);
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
		if (npcId == CADMON)
		{
			if (cond == 0)
			{
				if (st.getPlayer().getLevel() >= 74)
				{
					htmltext = "guard_cadmon_q0011_0101.htm";
				}
				else
				{
					htmltext = "guard_cadmon_q0011_0103.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if (cond == 1)
			{
				htmltext = "guard_cadmon_q0011_0105.htm";
			}
		}
		else if (npcId == LEON)
		{
			if (cond == 1)
			{
				htmltext = "trader_leon_q0011_0101.htm";
			}
			else if (cond == 2)
			{
				htmltext = "trader_leon_q0011_0202.htm";
			}
		}
		else if (npcId == WAHKAN)
		{
			if ((cond == 2) && (st.getQuestItemsCount(MUNITIONS_BOX) > 0))
			{
				htmltext = "herald_wakan_q0011_0201.htm";
			}
		}
		return htmltext;
	}
}
