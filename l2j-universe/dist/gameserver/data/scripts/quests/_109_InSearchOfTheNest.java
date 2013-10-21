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

public class _109_InSearchOfTheNest extends Quest implements ScriptFile
{
	private static final int PIERCE = 31553;
	private static final int CORPSE = 32015;
	private static final int KAHMAN = 31554;
	private static final int MEMO = 8083;
	private static final int GOLDEN_BADGE_RECRUIT = 7246;
	private static final int GOLDEN_BADGE_SOLDIER = 7247;
	
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
	
	public _109_InSearchOfTheNest()
	{
		super(false);
		addStartNpc(PIERCE);
		addTalkId(CORPSE);
		addTalkId(KAHMAN);
		addQuestItem(MEMO);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		int cond = st.getCond();
		if (event.equalsIgnoreCase("Memo") && (cond == 1))
		{
			st.giveItems(MEMO, 1);
			st.setCond(2);
			st.playSound(SOUND_ITEMGET);
			htmltext = "You've find something...";
		}
		else if (event.equalsIgnoreCase("merc_cap_peace_q0109_0301.htm") && (cond == 2))
		{
			st.takeItems(MEMO, -1);
			st.setCond(3);
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		int npcId = npc.getNpcId();
		int id = st.getState();
		if (id == COMPLETED)
		{
			return "completed";
		}
		int cond = st.getCond();
		String htmltext = "noquest";
		if (id == CREATED)
		{
			if ((st.getPlayer().getLevel() >= 66) && (npcId == PIERCE) && ((st.getQuestItemsCount(GOLDEN_BADGE_RECRUIT) > 0) || (st.getQuestItemsCount(GOLDEN_BADGE_SOLDIER) > 0)))
			{
				st.setState(STARTED);
				st.playSound(SOUND_ACCEPT);
				st.setCond(1);
				htmltext = "merc_cap_peace_q0109_0105.htm";
			}
			else
			{
				htmltext = "merc_cap_peace_q0109_0103.htm";
				st.exitCurrentQuest(true);
			}
		}
		else if (id == STARTED)
		{
			if (npcId == CORPSE)
			{
				if (cond == 1)
				{
					htmltext = "corpse_of_scout_q0109_0101.htm";
				}
				else if (cond == 2)
				{
					htmltext = "corpse_of_scout_q0109_0203.htm";
				}
			}
			else if (npcId == PIERCE)
			{
				if (cond == 1)
				{
					htmltext = "merc_cap_peace_q0109_0304.htm";
				}
				else if (cond == 2)
				{
					htmltext = "merc_cap_peace_q0109_0201.htm";
				}
				else if (cond == 3)
				{
					htmltext = "merc_cap_peace_q0109_0303.htm";
				}
			}
			else if ((npcId == KAHMAN) && (cond == 3))
			{
				htmltext = "merc_kahmun_q0109_0401.htm";
				st.addExpAndSp(8550000, 9950000);
				st.giveItems(ADENA_ID, 1800000);
				st.exitCurrentQuest(false);
				st.playSound(SOUND_FINISH);
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
