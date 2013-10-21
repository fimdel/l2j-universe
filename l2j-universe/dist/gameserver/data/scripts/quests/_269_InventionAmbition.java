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

public class _269_InventionAmbition extends Quest implements ScriptFile
{
	public final int INVENTOR_MARU = 32486;
	public final int[] MOBS =
	{
		21124,
		21125,
		21126,
		21127,
		21128,
		21129,
		21130,
		21131,
	};
	public final int ENERGY_ORES = 10866;
	
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
	
	public _269_InventionAmbition()
	{
		super(false);
		addStartNpc(INVENTOR_MARU);
		addKillId(MOBS);
		addQuestItem(ENERGY_ORES);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if (event.equals("inventor_maru_q0269_04.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equals("inventor_maru_q0269_07.htm"))
		{
			st.exitCurrentQuest(true);
			st.playSound(SOUND_FINISH);
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		long count = st.getQuestItemsCount(ENERGY_ORES);
		if (st.getState() == CREATED)
		{
			if (st.getPlayer().getLevel() < 18)
			{
				htmltext = "inventor_maru_q0269_02.htm";
				st.exitCurrentQuest(true);
			}
			else
			{
				htmltext = "inventor_maru_q0269_01.htm";
			}
		}
		else if (count > 0)
		{
			st.giveItems(ADENA_ID, (count * 50) + (2044 * (count / 20)), true);
			st.takeItems(ENERGY_ORES, -1);
			htmltext = "inventor_maru_q0269_06.htm";
		}
		else
		{
			htmltext = "inventor_maru_q0269_05.htm";
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		if (st.getState() != STARTED)
		{
			return null;
		}
		if (Rnd.chance(60))
		{
			st.giveItems(ENERGY_ORES, 1, false);
			st.playSound(SOUND_ITEMGET);
		}
		return null;
	}
}
