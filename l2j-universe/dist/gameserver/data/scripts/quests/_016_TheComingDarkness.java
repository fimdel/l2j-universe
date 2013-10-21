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

public class _016_TheComingDarkness extends Quest implements ScriptFile
{
	public final int HIERARCH = 31517;
	public final int[][] ALTAR_LIST =
	{
		{
			31512,
			1
		},
		{
			31513,
			2
		},
		{
			31514,
			3
		},
		{
			31515,
			4
		},
		{
			31516,
			5
		}
	};
	public final int CRYSTAL_OF_SEAL = 7167;
	
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
	
	public _016_TheComingDarkness()
	{
		super(false);
		addStartNpc(HIERARCH);
		for (int[] element : ALTAR_LIST)
		{
			addTalkId(element[0]);
		}
		addQuestItem(CRYSTAL_OF_SEAL);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if (event.equalsIgnoreCase("31517-02.htm"))
		{
			st.setState(STARTED);
			st.setCond(1);
			st.giveItems(CRYSTAL_OF_SEAL, 5);
			st.playSound(SOUND_ACCEPT);
		}
		for (int[] element : ALTAR_LIST)
		{
			if (event.equalsIgnoreCase(String.valueOf(element[0]) + "-02.htm"))
			{
				st.takeItems(CRYSTAL_OF_SEAL, 1);
				st.setCond(Integer.valueOf(element[1] + 1));
				st.playSound(SOUND_MIDDLE);
			}
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int npcId = npc.getNpcId();
		int cond = st.getCond();
		if (npcId == 31517)
		{
			if (cond < 1)
			{
				if (st.getPlayer().getLevel() < 61)
				{
					htmltext = "31517-00.htm";
					st.exitCurrentQuest(true);
				}
				else
				{
					htmltext = "31517-01.htm";
				}
			}
			else if ((cond > 0) && (cond < 6) && (st.getQuestItemsCount(CRYSTAL_OF_SEAL) > 0))
			{
				htmltext = "31517-02r.htm";
			}
			else if ((cond > 0) && (cond < 6) && (st.getQuestItemsCount(CRYSTAL_OF_SEAL) < 1))
			{
				htmltext = "31517-proeb.htm";
				st.exitCurrentQuest(false);
			}
			else if ((cond > 5) && (st.getQuestItemsCount(CRYSTAL_OF_SEAL) < 1))
			{
				htmltext = "31517-03.htm";
				st.addExpAndSp(1795524, 1679808);
				st.playSound(SOUND_FINISH);
				st.exitCurrentQuest(false);
			}
		}
		for (int[] element : ALTAR_LIST)
		{
			if (npcId == element[0])
			{
				if (cond == element[1])
				{
					if (st.getQuestItemsCount(CRYSTAL_OF_SEAL) > 0)
					{
						htmltext = String.valueOf(element[0]) + "-01.htm";
					}
					else
					{
						htmltext = String.valueOf(element[0]) + "-03.htm";
					}
				}
				else if (cond == (element[1] + 1))
				{
					htmltext = String.valueOf(element[0]) + "-04.htm";
				}
			}
		}
		return htmltext;
	}
}
