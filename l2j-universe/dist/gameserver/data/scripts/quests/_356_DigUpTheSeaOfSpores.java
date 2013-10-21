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

public class _356_DigUpTheSeaOfSpores extends Quest implements ScriptFile
{
	private static final int GAUEN = 30717;
	private static final int SPORE_ZOMBIE = 20562;
	private static final int ROTTING_TREE = 20558;
	private static final int CARNIVORE_SPORE = 5865;
	private static final int HERBIBOROUS_SPORE = 5866;
	
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
	
	public _356_DigUpTheSeaOfSpores()
	{
		super(false);
		addStartNpc(GAUEN);
		addKillId(SPORE_ZOMBIE);
		addKillId(ROTTING_TREE);
		addQuestItem(CARNIVORE_SPORE);
		addQuestItem(HERBIBOROUS_SPORE);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		long carn = st.getQuestItemsCount(CARNIVORE_SPORE);
		long herb = st.getQuestItemsCount(HERBIBOROUS_SPORE);
		if (event.equalsIgnoreCase("magister_gauen_q0356_06.htm"))
		{
			if (st.getPlayer().getLevel() >= 43)
			{
				st.setCond(1);
				st.setState(STARTED);
				st.playSound(SOUND_ACCEPT);
			}
			else
			{
				htmltext = "magister_gauen_q0356_01.htm";
				st.exitCurrentQuest(true);
			}
		}
		else if ((event.equalsIgnoreCase("magister_gauen_q0356_20.htm") || event.equalsIgnoreCase("magister_gauen_q0356_17.htm")) && (carn >= 50) && (herb >= 50))
		{
			st.takeItems(CARNIVORE_SPORE, -1);
			st.takeItems(HERBIBOROUS_SPORE, -1);
			st.playSound(SOUND_FINISH);
			st.exitCurrentQuest(true);
			if (event.equalsIgnoreCase("magister_gauen_q0356_17.htm"))
			{
				st.giveItems(ADENA_ID, 44000);
			}
			else
			{
				st.addExpAndSp(36000, 2600);
			}
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
			htmltext = "magister_gauen_q0356_02.htm";
		}
		else if (cond != 3)
		{
			htmltext = "magister_gauen_q0356_07.htm";
		}
		else if (cond == 3)
		{
			htmltext = "magister_gauen_q0356_10.htm";
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		int npcId = npc.getNpcId();
		long carn = st.getQuestItemsCount(CARNIVORE_SPORE);
		long herb = st.getQuestItemsCount(HERBIBOROUS_SPORE);
		if (npcId == SPORE_ZOMBIE)
		{
			if (carn < 50)
			{
				st.giveItems(CARNIVORE_SPORE, 1);
				if (carn == 49)
				{
					st.playSound(SOUND_MIDDLE);
					if (herb >= 50)
					{
						st.setCond(3);
						st.setState(STARTED);
					}
				}
				else
				{
					st.playSound(SOUND_ITEMGET);
				}
			}
		}
		else if (npcId == ROTTING_TREE)
		{
			if (herb < 50)
			{
				st.giveItems(HERBIBOROUS_SPORE, 1);
				if (herb == 49)
				{
					st.playSound(SOUND_MIDDLE);
					if (carn >= 50)
					{
						st.setCond(3);
						st.setState(STARTED);
					}
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
