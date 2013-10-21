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

public class _10279_MutatedKaneusOren extends Quest implements ScriptFile
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
	
	private static final int Mouen = 30196;
	private static final int Rovia = 30189;
	private static final int KaimAbigore = 18566;
	private static final int KnightMontagnar = 18568;
	private static final int Tissue1 = 13836;
	private static final int Tissue2 = 13837;
	
	public _10279_MutatedKaneusOren()
	{
		super(true);
		addStartNpc(Mouen);
		addTalkId(Rovia);
		addKillId(KaimAbigore, KnightMontagnar);
		addQuestItem(Tissue1, Tissue2);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if (event.equalsIgnoreCase("30196-03.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("30189-02.htm"))
		{
			st.giveItems(ADENA_ID, 200000);
			st.addExpAndSp(1000000, 770000);
			st.exitCurrentQuest(false);
			st.playSound(SOUND_FINISH);
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int id = st.getState();
		int cond = st.getCond();
		int npcId = npc.getNpcId();
		if (id == COMPLETED)
		{
			if (npcId == Mouen)
			{
				htmltext = "30196-0a.htm";
			}
		}
		else if ((id == CREATED) && (npcId == Mouen))
		{
			if (st.getPlayer().getLevel() >= 48)
			{
				htmltext = "30196-01.htm";
			}
			else
			{
				htmltext = "30196-00.htm";
			}
		}
		else if (npcId == Mouen)
		{
			if (cond == 1)
			{
				htmltext = "30196-04.htm";
			}
			else if (cond == 2)
			{
				htmltext = "30196-05.htm";
			}
		}
		else if (npcId == Rovia)
		{
			if (cond == 1)
			{
				htmltext = "30189-01a.htm";
			}
			else if (cond == 2)
			{
				htmltext = "30189-01.htm";
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		if ((st.getState() == STARTED) && (st.getCond() == 1))
		{
			st.giveItems(Tissue1, 1);
			st.giveItems(Tissue2, 1);
			st.setCond(2);
			st.playSound(SOUND_MIDDLE);
		}
		return null;
	}
}
