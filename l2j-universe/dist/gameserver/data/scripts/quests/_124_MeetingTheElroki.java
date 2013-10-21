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

public class _124_MeetingTheElroki extends Quest implements ScriptFile
{
	public final int Marquez = 32113;
	public final int Mushika = 32114;
	public final int Asamah = 32115;
	public final int Karakawei = 32117;
	public final int Mantarasa = 32118;
	public final int Mushika_egg = 8778;
	
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
	
	public _124_MeetingTheElroki()
	{
		super(false);
		addStartNpc(Marquez);
		addTalkId(Mushika);
		addTalkId(Asamah);
		addTalkId(Karakawei);
		addTalkId(Mantarasa);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		int cond = st.getCond();
		String htmltext = event;
		if (event.equals("marquez_q0124_03.htm"))
		{
			st.setState(STARTED);
		}
		if (event.equals("marquez_q0124_04.htm") && (cond == 0))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		if (event.equals("marquez_q0124_06.htm") && (cond == 1))
		{
			st.setCond(2);
			st.playSound(SOUND_ITEMGET);
		}
		if (event.equals("mushika_q0124_03.htm") && (cond == 2))
		{
			st.setCond(3);
			st.playSound(SOUND_ITEMGET);
		}
		if (event.equals("asama_q0124_06.htm") && (cond == 3))
		{
			st.setCond(4);
			st.playSound(SOUND_ITEMGET);
		}
		if (event.equals("shaman_caracawe_q0124_03.htm") && (cond == 4))
		{
			st.set("id", "1");
		}
		if (event.equals("shaman_caracawe_q0124_05.htm") && (cond == 4))
		{
			st.setCond(5);
			st.playSound(SOUND_ITEMGET);
		}
		if (event.equals("egg_of_mantarasa_q0124_02.htm") && (cond == 5))
		{
			st.giveItems(Mushika_egg, 1);
			st.setCond(6);
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
		if (npcId == Marquez)
		{
			if (cond == 0)
			{
				if (st.getPlayer().getLevel() < 75)
				{
					htmltext = "marquez_q0124_02.htm";
					st.exitCurrentQuest(true);
				}
				else
				{
					htmltext = "marquez_q0124_01.htm";
				}
			}
			else if (cond == 1)
			{
				htmltext = "marquez_q0124_04.htm";
			}
			else if (cond == 2)
			{
				htmltext = "marquez_q0124_07.htm";
			}
		}
		else if ((npcId == Mushika) && (cond == 2))
		{
			htmltext = "mushika_q0124_01.htm";
		}
		else if (npcId == Asamah)
		{
			if (cond == 3)
			{
				htmltext = "asama_q0124_03.htm";
			}
			else if (cond == 6)
			{
				htmltext = "asama_q0124_08.htm";
				st.takeItems(Mushika_egg, 1);
				st.addExpAndSp(1109665, 1229015);
				st.giveItems(ADENA_ID, 236510);
				st.playSound(SOUND_FINISH);
				st.setState(COMPLETED);
				st.exitCurrentQuest(false);
			}
		}
		else if (npcId == Karakawei)
		{
			if (cond == 4)
			{
				htmltext = "shaman_caracawe_q0124_01.htm";
				if (st.getInt("id") == 1)
				{
					htmltext = "shaman_caracawe_q0124_03.htm";
				}
				else if (cond == 5)
				{
					htmltext = "shaman_caracawe_q0124_07.htm";
				}
			}
		}
		else if ((npcId == Mantarasa) && (cond == 5))
		{
			htmltext = "egg_of_mantarasa_q0124_01.htm";
		}
		return htmltext;
	}
}
