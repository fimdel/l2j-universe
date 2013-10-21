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

public class _617_GatherTheFlames extends Quest implements ScriptFile
{
	private final static int VULCAN = 31539;
	private final static int HILDA = 31271;
	private final static int TORCH = 7264;
	private final static int[][] DROPLIST =
	{
		{
			22634,
			58
		},
		{
			22635,
			58
		},
		{
			22636,
			58
		},
		{
			22637,
			58
		},
		{
			22638,
			58
		},
		{
			22639,
			61
		},
		{
			22640,
			61
		},
		{
			22641,
			61
		},
		{
			22642,
			61
		},
		{
			22643,
			61
		},
		{
			22644,
			61
		},
		{
			22645,
			63
		},
		{
			22646,
			63
		},
		{
			22647,
			63
		},
		{
			22648,
			66
		},
		{
			22649,
			66
		},
		{
			18799,
			66
		},
		{
			18800,
			66
		},
		{
			18801,
			66
		},
		{
			18802,
			74
		},
		{
			18803,
			74
		}
	};
	public static final int[] Recipes =
	{
		6881,
		6883,
		6885,
		6887,
		7580,
		6891,
		6893,
		6895,
		6897,
		6899
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
	
	public _617_GatherTheFlames()
	{
		super(true);
		addStartNpc(VULCAN);
		addStartNpc(HILDA);
		for (int[] element : DROPLIST)
		{
			addKillId(element[0]);
		}
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if (event.equalsIgnoreCase("warsmith_vulcan_q0617_03.htm"))
		{
			if (st.getPlayer().getLevel() < 74)
			{
				return "warsmith_vulcan_q0617_02.htm";
			}
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
			st.setCond(1);
		}
		else if (event.equalsIgnoreCase("blacksmith_hilda_q0617_03.htm"))
		{
			if (st.getPlayer().getLevel() < 74)
			{
				return "blacksmith_hilda_q0617_02.htm";
			}
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
			st.setCond(1);
		}
		else if (event.equalsIgnoreCase("warsmith_vulcan_q0617_08.htm"))
		{
			st.playSound(SOUND_FINISH);
			st.takeItems(TORCH, -1);
			st.exitCurrentQuest(true);
		}
		else if (event.equalsIgnoreCase("warsmith_vulcan_q0617_07.htm"))
		{
			if (st.getQuestItemsCount(TORCH) < 1000)
			{
				return "warsmith_vulcan_q0617_05.htm";
			}
			st.takeItems(TORCH, 1000);
			st.giveItems(Recipes[Rnd.get(Recipes.length)], 1);
			st.playSound(SOUND_MIDDLE);
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int npcId = npc.getNpcId();
		int cond = st.getCond();
		if (npcId == VULCAN)
		{
			if (cond == 0)
			{
				if (st.getPlayer().getLevel() < 74)
				{
					htmltext = "warsmith_vulcan_q0617_02.htm";
					st.exitCurrentQuest(true);
				}
				else
				{
					htmltext = "warsmith_vulcan_q0617_01.htm";
				}
			}
			else
			{
				htmltext = st.getQuestItemsCount(TORCH) < 1000 ? "warsmith_vulcan_q0617_05.htm" : "warsmith_vulcan_q0617_04.htm";
			}
		}
		else if (npcId == HILDA)
		{
			if (cond < 1)
			{
				htmltext = st.getPlayer().getLevel() < 74 ? "blacksmith_hilda_q0617_02.htm" : "blacksmith_hilda_q0617_01.htm";
			}
			else
			{
				htmltext = "blacksmith_hilda_q0617_04.htm";
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		for (int[] element : DROPLIST)
		{
			if (npc.getNpcId() == element[0])
			{
				st.rollAndGive(TORCH, 1, element[1]);
				return null;
			}
		}
		return null;
	}
}
