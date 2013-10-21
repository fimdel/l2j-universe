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

import org.apache.commons.lang3.ArrayUtils;

public class _252_GoodSmell extends Quest implements ScriptFile
{
	private static final int GuardStan = 30200;
	private static final int[] SelMahums =
	{
		22786,
		22787,
		22788
	};
	private static final int SelChef = 18908;
	private static final int SelMahumDiary = 15500;
	private static final int SelMahumCookbookPage = 15501;
	
	public _252_GoodSmell()
	{
		super(false);
		addStartNpc(GuardStan);
		addKillId(SelMahums[0], SelMahums[1], SelMahums[2], SelChef);
		addQuestItem(SelMahumDiary, SelMahumCookbookPage);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if (event.equalsIgnoreCase("stan_q252_03.htm"))
		{
			st.setState(STARTED);
			st.setCond(1);
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("stan_q252_06.htm"))
		{
			st.takeAllItems(SelMahumDiary, SelMahumCookbookPage);
			st.setState(COMPLETED);
			st.giveItems(57, 147656);
			st.addExpAndSp(716238, 78324);
			st.playSound(SOUND_FINISH);
			st.exitCurrentQuest(false);
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int cond = st.getCond();
		if (npc.getNpcId() == GuardStan)
		{
			if (cond == 0)
			{
				if (st.getPlayer().getLevel() >= 82)
				{
					htmltext = "stan_q252_01.htm";
				}
				else
				{
					htmltext = "stan_q252_00.htm";
				}
			}
			else if (cond == 1)
			{
				htmltext = "stan_q252_04.htm";
			}
			else if (cond == 2)
			{
				htmltext = "stan_q252_05.htm";
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		int cond = st.getCond();
		if (cond == 1)
		{
			if ((st.getQuestItemsCount(SelMahumDiary) < 10) && ArrayUtils.contains(SelMahums, npc.getNpcId()))
			{
				st.rollAndGive(SelMahumDiary, 1, 15);
			}
			if ((st.getQuestItemsCount(SelMahumCookbookPage) < 5) && (npc.getNpcId() == SelChef))
			{
				st.rollAndGive(SelMahumCookbookPage, 1, 10);
			}
			if ((st.getQuestItemsCount(SelMahumDiary) >= 10) && (st.getQuestItemsCount(SelMahumCookbookPage) >= 5))
			{
				st.setCond(2);
			}
		}
		return null;
	}
	
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
}
