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

import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class _143_FallenAngelRequestOfDusk extends Quest implements ScriptFile
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
	
	private final static int NATOOLS = 30894;
	private final static int TOBIAS = 30297;
	private final static int CASIAN = 30612;
	private final static int ROCK = 32368;
	private final static int ANGEL = 32369;
	private final static int MonsterAngel = 27338;
	private final static int SEALED_PATH = 10354;
	private final static int PATH = 10355;
	private final static int EMPTY_CRYSTAL = 10356;
	private final static int MEDICINE = 10357;
	private final static int MESSAGE = 10358;
	
	public _143_FallenAngelRequestOfDusk()
	{
		super(false);
		addTalkId(NATOOLS, TOBIAS, CASIAN, ROCK, ANGEL);
		addQuestItem(SEALED_PATH, PATH, EMPTY_CRYSTAL, MEDICINE, MESSAGE);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if (event.equalsIgnoreCase("start"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
			htmltext = "warehouse_chief_natools_q0143_01.htm";
		}
		else if (event.equalsIgnoreCase("warehouse_chief_natools_q0143_04.htm"))
		{
			st.setCond(2);
			st.setState(STARTED);
			st.playSound(SOUND_MIDDLE);
			st.giveItems(SEALED_PATH, 1);
		}
		else if (event.equalsIgnoreCase("master_tobias_q0143_05.htm"))
		{
			st.setCond(3);
			st.setState(STARTED);
			st.unset("talk");
			st.playSound(SOUND_MIDDLE);
			st.giveItems(PATH, 1);
			st.giveItems(EMPTY_CRYSTAL, 1);
		}
		else if (event.equalsIgnoreCase("sage_kasian_q0143_09.htm"))
		{
			st.setCond(4);
			st.setState(STARTED);
			st.unset("talk");
			st.giveItems(MEDICINE, 1);
			st.playSound(SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("stained_rock_q0143_05.htm"))
		{
			if (GameObjectsStorage.getByNpcId(MonsterAngel) != null)
			{
				htmltext = "stained_rock_q0143_03.htm";
			}
			else if (GameObjectsStorage.getByNpcId(ANGEL) != null)
			{
				htmltext = "stained_rock_q0143_04.htm";
			}
			else
			{
				st.addSpawn(ANGEL, 180000);
				st.playSound(SOUND_MIDDLE);
			}
		}
		else if (event.equalsIgnoreCase("q_fallen_angel_npc_q0143_14.htm"))
		{
			st.setCond(5);
			st.setState(STARTED);
			st.unset("talk");
			st.takeItems(EMPTY_CRYSTAL, -1);
			st.giveItems(MESSAGE, 1);
			st.playSound(SOUND_MIDDLE);
			NpcInstance n = GameObjectsStorage.getByNpcId(ANGEL);
			if (n != null)
			{
				n.deleteMe();
			}
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int cond = st.getCond();
		int npcId = npc.getNpcId();
		if (npcId == NATOOLS)
		{
			if ((cond == 1) || (st.isStarted() && (cond == 0)))
			{
				htmltext = "warehouse_chief_natools_q0143_01.htm";
			}
			else if (cond == 2)
			{
				htmltext = "warehouse_chief_natools_q0143_05.htm";
			}
		}
		else if (npcId == TOBIAS)
		{
			if (cond == 2)
			{
				if (st.getInt("talk") == 1)
				{
					htmltext = "master_tobias_q0143_03.htm";
				}
				else
				{
					htmltext = "master_tobias_q0143_02.htm";
					st.takeItems(SEALED_PATH, -1);
					st.set("talk", "1");
				}
			}
			else if (cond == 3)
			{
				htmltext = "master_tobias_q0143_06.htm";
			}
			else if (cond == 5)
			{
				htmltext = "master_tobias_q0143_07.htm";
				st.playSound(SOUND_FINISH);
				st.giveItems(ADENA_ID, 89046);
				st.exitCurrentQuest(false);
			}
		}
		else if (npcId == CASIAN)
		{
			if (cond == 3)
			{
				if (st.getInt("talk") == 1)
				{
					htmltext = "sage_kasian_q0143_03.htm";
				}
				else
				{
					htmltext = "sage_kasian_q0143_02.htm";
					st.takeItems(PATH, -1);
					st.set("talk", "1");
				}
			}
			else if (cond == 4)
			{
				htmltext = "sage_kasian_q0143_09.htm";
			}
		}
		else if (npcId == ROCK)
		{
			if (cond <= 3)
			{
				htmltext = "stained_rock_q0143_01.htm";
			}
			else if (cond == 4)
			{
				htmltext = "stained_rock_q0143_02.htm";
			}
			else
			{
				htmltext = "stained_rock_q0143_06.htm";
			}
		}
		else if (npcId == ANGEL)
		{
			if (cond == 4)
			{
				if (st.getInt("talk") == 1)
				{
					htmltext = "q_fallen_angel_npc_q0143_04.htm";
				}
				else
				{
					htmltext = "q_fallen_angel_npc_q0143_03.htm";
					st.takeItems(MEDICINE, -1);
					st.set("talk", "1");
				}
			}
		}
		else if (cond == 5)
		{
			htmltext = "q_fallen_angel_npc_q0143_14.htm";
		}
		return htmltext;
	}
}
