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

public class _146_TheZeroHour extends Quest implements ScriptFile
{
	private static int KAHMAN = 31554;
	private static int STAKATO_QUEENS_FANG = 14859;
	private static int KAHMANS_SUPPLY_BOX = 14849;
	private static int QUEEN_SHYEED_ID = 25671;
	
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
	
	public _146_TheZeroHour()
	{
		super(true);
		addStartNpc(KAHMAN);
		addTalkId(KAHMAN);
		addKillId(QUEEN_SHYEED_ID);
		addQuestItem(STAKATO_QUEENS_FANG);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		int cond = st.getCond();
		String htmltext = event;
		if (event.equals("merc_kahmun_q0146_0103.htm") && (cond == 0))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		if (event.equals("reward") && (cond == 2))
		{
			htmltext = "merc_kahmun_q0146_0107.htm";
			st.takeItems(STAKATO_QUEENS_FANG, -1);
			st.giveItems(KAHMANS_SUPPLY_BOX, 1);
			st.addExpAndSp(2850000, 3315000);
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
		QuestState InSearchOfTheNest = st.getPlayer().getQuestState(_109_InSearchOfTheNest.class);
		if (npcId == KAHMAN)
		{
			if (cond == 0)
			{
				if (st.getPlayer().getLevel() >= 81)
				{
					if ((InSearchOfTheNest != null) && InSearchOfTheNest.isCompleted())
					{
						htmltext = "merc_kahmun_q0146_0101.htm";
					}
					else
					{
						htmltext = "merc_kahmun_q0146_0104.htm";
					}
				}
				else
				{
					htmltext = "merc_kahmun_q0146_0102.htm";
				}
			}
			else if ((cond == 1) && (st.getQuestItemsCount(STAKATO_QUEENS_FANG) < 1))
			{
				htmltext = "merc_kahmun_q0146_0105.htm";
			}
			else if (cond == 2)
			{
				htmltext = "merc_kahmun_q0146_0106.htm";
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		if (st.getState() == STARTED)
		{
			st.setCond(2);
			st.giveItems(STAKATO_QUEENS_FANG, 1);
		}
		return null;
	}
}
