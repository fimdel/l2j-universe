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

public class _376_GiantsExploration1 extends Quest implements ScriptFile
{
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
	
	private static final int DROP_RATE = 3;
	private static final int DROP_RATE_BOOK = 1;
	private static final int ANCIENT_PARCHMENT = 14841;
	private static final int DICT1 = 5891;
	private static final int DICT2 = 5892;
	private static final int MST_BK = 5890;
	private static final int[][] EXCHANGE =
	{
		{
			5937,
			5938,
			5939,
			5940,
			5941
		},
		{
			5346,
			5354
		},
		{
			5932,
			5933,
			5934,
			5935,
			5936
		},
		{
			5332,
			5334
		},
		{
			5922,
			5923,
			5924,
			5925,
			5926
		},
		{
			5416,
			5418
		},
		{
			5927,
			5928,
			5929,
			5930,
			5931
		},
		{
			5424,
			5340
		}
	};
	private static final int HR_SOBLING = 31147;
	private static final int WF_CLIFF = 30182;
	private static final int[] MOBS =
	{
		22670,
		22671,
		22672,
		22673,
		22674,
		22675,
		22676,
		22677,
	};
	
	public _376_GiantsExploration1()
	{
		super(true);
		addStartNpc(HR_SOBLING);
		addTalkId(WF_CLIFF);
		addKillId(MOBS);
		addQuestItem(DICT1, MST_BK);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		int cond = st.getCond();
		if (event.equalsIgnoreCase("yes"))
		{
			htmltext = "Starting.htm";
			st.setState(STARTED);
			st.setCond(1);
			st.giveItems(DICT1, 1);
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("no"))
		{
			htmltext = "ext_msg.htm";
			st.playSound(SOUND_FINISH);
			st.exitCurrentQuest(true);
		}
		else if (event.equalsIgnoreCase("show"))
		{
			htmltext = "no_items.htm";
			for (int i = 0; i < EXCHANGE.length; i = i + 2)
			{
				long count = Long.MAX_VALUE;
				for (int j : EXCHANGE[i])
				{
					count = Math.min(count, st.getQuestItemsCount(j));
				}
				if (count >= 1)
				{
					htmltext = "tnx4items.htm";
					for (int j : EXCHANGE[i])
					{
						st.takeItems(j, count);
					}
					for (int l = 0; l < count; l++)
					{
						int item = EXCHANGE[i + 1][Rnd.get(EXCHANGE[i + 1].length)];
						st.giveItems(item, 1);
					}
				}
			}
		}
		else if (event.equalsIgnoreCase("myst"))
		{
			if (st.getQuestItemsCount(MST_BK) > 0)
			{
				if (cond == 1)
				{
					st.setState(STARTED);
					st.setCond(2);
					htmltext = "go_part2.htm";
				}
				else if (cond == 2)
				{
					htmltext = "gogogo_2.htm";
				}
			}
			else
			{
				htmltext = "no_part2.htm";
			}
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int npcId = npc.getNpcId();
		int id = st.getState();
		int cond = st.getCond();
		if (npcId == HR_SOBLING)
		{
			if (id == CREATED)
			{
				if (st.getPlayer().getLevel() < 75)
				{
					st.exitCurrentQuest(true);
					htmltext = "error_1.htm";
				}
				else
				{
					htmltext = "start.htm";
				}
			}
			else if (id == STARTED)
			{
				if (st.getQuestItemsCount(ANCIENT_PARCHMENT) != 0)
				{
					htmltext = "checkout2.htm";
				}
				else
				{
					htmltext = "checkout.htm";
				}
			}
		}
		else if (npcId == WF_CLIFF)
		{
			if ((cond == 2) & (st.getQuestItemsCount(MST_BK) > 0))
			{
				htmltext = "ok_part2.htm";
				st.takeItems(MST_BK, -1);
				st.giveItems(DICT2, 1);
				st.setCond(3);
				st.playSound(SOUND_MIDDLE);
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		int cond = st.getCond();
		if (cond > 0)
		{
			st.rollAndGive(ANCIENT_PARCHMENT, 1, 1, DROP_RATE);
			if (cond == 1)
			{
				st.rollAndGive(MST_BK, 1, 1, 1, DROP_RATE_BOOK);
			}
		}
		return null;
	}
}
