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

public class _279_TargetOfOpportunity extends Quest implements ScriptFile
{
	private static final int Jerian = 32302;
	private static final int CosmicScout = 22373;
	private static final int CosmicWatcher = 22374;
	private static final int CosmicPriest = 22375;
	private static final int CosmicLord = 22376;
	private static final int SealComponentsPart1 = 15517;
	private static final int SealComponentsPart2 = 15518;
	private static final int SealComponentsPart3 = 15519;
	private static final int SealComponentsPart4 = 15520;
	
	public _279_TargetOfOpportunity()
	{
		super(PARTY_ALL);
		addStartNpc(Jerian);
		addKillId(CosmicScout, CosmicWatcher, CosmicPriest, CosmicLord);
		addQuestItem(SealComponentsPart1, SealComponentsPart2, SealComponentsPart3, SealComponentsPart4);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if (event.equalsIgnoreCase("jerian_q279_04.htm"))
		{
			st.setState(STARTED);
			st.setCond(1);
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("jerian_q279_07.htm"))
		{
			st.takeAllItems(SealComponentsPart1, SealComponentsPart2, SealComponentsPart3, SealComponentsPart4);
			st.giveItems(15515, 1);
			st.giveItems(15516, 1);
			st.playSound(SOUND_FINISH);
			st.exitCurrentQuest(true);
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int npcId = npc.getNpcId();
		int cond = st.getCond();
		if (npcId == Jerian)
		{
			if (cond == 0)
			{
				if (st.getPlayer().getLevel() >= 82)
				{
					htmltext = "jerian_q279_01.htm";
				}
				else
				{
					htmltext = "jerian_q279_00.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if (cond == 1)
			{
				htmltext = "jerian_q279_05.htm";
			}
			else if (cond == 2)
			{
				htmltext = "jerian_q279_06.htm";
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		int npcId = npc.getNpcId();
		int cond = st.getCond();
		if (cond == 1)
		{
			if ((npcId == CosmicScout) && (st.getQuestItemsCount(SealComponentsPart1) < 1) && Rnd.chance(15))
			{
				st.giveItems(SealComponentsPart1, 1);
			}
			else if ((npcId == CosmicWatcher) && (st.getQuestItemsCount(SealComponentsPart2) < 1) && Rnd.chance(15))
			{
				st.giveItems(SealComponentsPart2, 1);
			}
			else if ((npcId == CosmicPriest) && (st.getQuestItemsCount(SealComponentsPart3) < 1) && Rnd.chance(15))
			{
				st.giveItems(SealComponentsPart3, 1);
			}
			else if ((npcId == CosmicLord) && (st.getQuestItemsCount(SealComponentsPart4) < 1) && Rnd.chance(15))
			{
				st.giveItems(SealComponentsPart4, 1);
			}
			if ((st.getQuestItemsCount(SealComponentsPart1) >= 1) && (st.getQuestItemsCount(SealComponentsPart2) >= 1) && (st.getQuestItemsCount(SealComponentsPart3) >= 1) && (st.getQuestItemsCount(SealComponentsPart4) >= 1))
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
