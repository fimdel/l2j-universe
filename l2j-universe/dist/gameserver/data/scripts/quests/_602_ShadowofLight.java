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

public class _602_ShadowofLight extends Quest implements ScriptFile
{
	private static final int ARGOS = 31683;
	private static final int EYE_OF_DARKNESS = 7189;
	private static final int[][] REWARDS =
	{
		{
			6699,
			40000,
			120000,
			20000,
			1,
			19
		},
		{
			6698,
			60000,
			110000,
			15000,
			20,
			39
		},
		{
			6700,
			40000,
			150000,
			10000,
			40,
			49
		},
		{
			0,
			100000,
			140000,
			11250,
			50,
			100
		}
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
	
	public _602_ShadowofLight()
	{
		super(true);
		addStartNpc(ARGOS);
		addKillId(21299);
		addKillId(21304);
		addQuestItem(EYE_OF_DARKNESS);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if (event.equalsIgnoreCase("eye_of_argos_q0602_0104.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("eye_of_argos_q0602_0201.htm"))
		{
			st.takeItems(EYE_OF_DARKNESS, -1);
			int random = Rnd.get(100) + 1;
			for (int[] element : REWARDS)
			{
				if ((element[4] <= random) && (random <= element[5]))
				{
					st.giveItems(ADENA_ID, element[1], true);
					st.addExpAndSp(element[2], element[3]);
					if (element[0] != 0)
					{
						st.giveItems(element[0], 3, true);
					}
				}
			}
			st.playSound(SOUND_FINISH);
			st.exitCurrentQuest(true);
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		int npcId = npc.getNpcId();
		String htmltext = "noquest";
		int id = st.getState();
		int cond = 0;
		if (id != CREATED)
		{
			cond = st.getCond();
		}
		if (npcId == ARGOS)
		{
			if (cond == 0)
			{
				if (st.getPlayer().getLevel() < 68)
				{
					htmltext = "eye_of_argos_q0602_0103.htm";
					st.exitCurrentQuest(true);
				}
				else
				{
					htmltext = "eye_of_argos_q0602_0101.htm";
				}
			}
			else if (cond == 1)
			{
				htmltext = "eye_of_argos_q0602_0106.htm";
			}
			else if ((cond == 2) && (st.getQuestItemsCount(EYE_OF_DARKNESS) == 100))
			{
				htmltext = "eye_of_argos_q0602_0105.htm";
			}
			else
			{
				htmltext = "eye_of_argos_q0602_0106.htm";
				st.setCond(1);
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		if (st.getCond() == 1)
		{
			long count = st.getQuestItemsCount(EYE_OF_DARKNESS);
			if ((count < 100) && Rnd.chance(npc.getNpcId() == 21299 ? 35 : 40))
			{
				st.giveItems(EYE_OF_DARKNESS, 1);
				if (count == 99)
				{
					st.setCond(2);
					st.playSound(SOUND_MIDDLE);
				}
				else
				{
					st.playSound(SOUND_ITEMGET);
				}
			}
		}
		return null;
	}
}
