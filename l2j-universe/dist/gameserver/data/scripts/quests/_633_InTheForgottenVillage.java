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

import java.util.HashMap;
import java.util.Map;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class _633_InTheForgottenVillage extends Quest implements ScriptFile
{
	private static int MINA = 31388;
	private static int RIB_BONE = 7544;
	private static int Z_LIVER = 7545;
	private static Map<Integer, Double> DAMOBS = new HashMap<>();
	private static Map<Integer, Double> UNDEADS = new HashMap<>();
	
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
	
	public _633_InTheForgottenVillage()
	{
		super(true);
		DAMOBS.put(21557, 32.8);
		DAMOBS.put(21558, 32.8);
		DAMOBS.put(21559, 33.7);
		DAMOBS.put(21560, 33.7);
		DAMOBS.put(21563, 34.2);
		DAMOBS.put(21564, 34.8);
		DAMOBS.put(21565, 35.1);
		DAMOBS.put(21566, 35.9);
		DAMOBS.put(21567, 35.9);
		DAMOBS.put(21572, 36.5);
		DAMOBS.put(21574, 38.3);
		DAMOBS.put(21575, 38.3);
		DAMOBS.put(21580, 38.5);
		DAMOBS.put(21581, 39.5);
		DAMOBS.put(21583, 39.7);
		DAMOBS.put(21584, 40.1);
		UNDEADS.put(21553, 34.7);
		UNDEADS.put(21554, 34.7);
		UNDEADS.put(21561, 45.0);
		UNDEADS.put(21578, 50.1);
		UNDEADS.put(21596, 35.9);
		UNDEADS.put(21597, 37.0);
		UNDEADS.put(21598, 44.1);
		UNDEADS.put(21599, 39.5);
		UNDEADS.put(21600, 40.8);
		UNDEADS.put(21601, 41.1);
		addStartNpc(MINA);
		addQuestItem(RIB_BONE);
		for (int i : UNDEADS.keySet())
		{
			addKillId(i);
		}
		for (int i : DAMOBS.keySet())
		{
			addKillId(i);
		}
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if (event.equalsIgnoreCase("quest_accept"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
			htmltext = "day_mina_q0633_0104.htm";
		}
		if (event.equalsIgnoreCase("633_4"))
		{
			st.takeItems(RIB_BONE, -1);
			st.playSound(SOUND_FINISH);
			htmltext = "day_mina_q0633_0204.htm";
			st.exitCurrentQuest(true);
		}
		else if (event.equalsIgnoreCase("633_1"))
		{
			htmltext = "day_mina_q0633_0201.htm";
		}
		else if (event.equalsIgnoreCase("633_3"))
		{
			if (st.getCond() == 2)
			{
				if (st.getQuestItemsCount(RIB_BONE) >= 200)
				{
					st.takeItems(RIB_BONE, -1);
					st.giveItems(ADENA_ID, 25000);
					st.addExpAndSp(305235, 0);
					st.playSound(SOUND_FINISH);
					st.setCond(1);
					htmltext = "day_mina_q0633_0202.htm";
				}
				else
				{
					htmltext = "day_mina_q0633_0203.htm";
				}
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
		int id = st.getState();
		if (npcId == MINA)
		{
			if (id == CREATED)
			{
				if (st.getPlayer().getLevel() >= 65)
				{
					htmltext = "day_mina_q0633_0101.htm";
				}
				else
				{
					htmltext = "day_mina_q0633_0103.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if (cond == 1)
			{
				htmltext = "day_mina_q0633_0106.htm";
			}
			else if (cond == 2)
			{
				htmltext = "day_mina_q0633_0105.htm";
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		int npcId = npc.getNpcId();
		if (UNDEADS.containsKey(npcId))
		{
			st.rollAndGive(Z_LIVER, 1, UNDEADS.get(npcId));
		}
		else if (DAMOBS.containsKey(npcId))
		{
			long count = st.getQuestItemsCount(RIB_BONE);
			if ((count < 200) && Rnd.chance(DAMOBS.get(npcId)))
			{
				st.giveItems(RIB_BONE, 1);
				if (count >= 199)
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
