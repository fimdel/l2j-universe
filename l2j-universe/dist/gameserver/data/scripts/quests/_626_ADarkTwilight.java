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

import lineage2.commons.util.Rnd;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class _626_ADarkTwilight extends Quest implements ScriptFile
{
	private static final int Hierarch = 31517;
	private static int BloodOfSaint = 7169;
	
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
	
	public _626_ADarkTwilight()
	{
		super(true);
		addStartNpc(Hierarch);
		for (int npcId = 21520; npcId <= 21542; npcId++)
		{
			addKillId(npcId);
		}
		addQuestItem(BloodOfSaint);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if (event.equalsIgnoreCase("dark_presbyter_q0626_0104.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("dark_presbyter_q0626_0201.htm"))
		{
			if (st.getQuestItemsCount(BloodOfSaint) < 300)
			{
				htmltext = "dark_presbyter_q0626_0203.htm";
			}
		}
		else if (event.equalsIgnoreCase("rew_exp"))
		{
			st.takeItems(BloodOfSaint, -1);
			st.addExpAndSp(162773, 12500);
			htmltext = "dark_presbyter_q0626_0202.htm";
			st.exitCurrentQuest(true);
		}
		else if (event.equalsIgnoreCase("rew_adena"))
		{
			st.takeItems(BloodOfSaint, -1);
			st.giveItems(ADENA_ID, 100000, true);
			htmltext = "dark_presbyter_q0626_0202.htm";
			st.exitCurrentQuest(true);
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int cond = st.getCond();
		int npcId = npc.getNpcId();
		if (npcId == Hierarch)
		{
			if (cond == 0)
			{
				if (st.getPlayer().getLevel() < 60)
				{
					htmltext = "dark_presbyter_q0626_0103.htm";
					st.exitCurrentQuest(true);
				}
				else
				{
					htmltext = "dark_presbyter_q0626_0101.htm";
				}
			}
			else if (cond == 1)
			{
				htmltext = "dark_presbyter_q0626_0106.htm";
			}
			else if (cond == 2)
			{
				htmltext = "dark_presbyter_q0626_0105.htm";
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		if ((st.getCond() == 1) && Rnd.chance(70))
		{
			st.giveItems(BloodOfSaint, 1);
			if (st.getQuestItemsCount(BloodOfSaint) == 300)
			{
				st.setCond(2);
			}
		}
		return null;
	}
}
