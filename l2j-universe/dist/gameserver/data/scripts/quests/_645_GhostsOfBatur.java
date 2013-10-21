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

public class _645_GhostsOfBatur extends Quest implements ScriptFile
{
	private static final int Karuda = 32017;
	private static final int CursedBurialItems = 14861;
	private static final int[] MOBS =
	{
		22703,
		22704,
		22705,
		22706,
		22707
	};
	
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
	
	public _645_GhostsOfBatur()
	{
		super(true);
		addStartNpc(Karuda);
		for (int i : MOBS)
		{
			addKillId(i);
		}
		addQuestItem(CursedBurialItems);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if (event.equalsIgnoreCase("karuda_q0645_0103.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int cond = st.getCond();
		if (cond == 0)
		{
			if (st.getPlayer().getLevel() < 61)
			{
				htmltext = "karuda_q0645_0102.htm";
				st.exitCurrentQuest(true);
			}
			else
			{
				htmltext = "karuda_q0645_0101.htm";
			}
		}
		else
		{
			if (cond == 2)
			{
				st.setCond(1);
			}
			if (st.getQuestItemsCount(CursedBurialItems) == 0)
			{
				htmltext = "karuda_q0645_0106.htm";
			}
			else
			{
				htmltext = "karuda_q0645_0105.htm";
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		if (st.getCond() > 0)
		{
			if (Rnd.chance(5))
			{
				st.giveItems(CursedBurialItems, 1, true);
			}
		}
		return null;
	}
}
