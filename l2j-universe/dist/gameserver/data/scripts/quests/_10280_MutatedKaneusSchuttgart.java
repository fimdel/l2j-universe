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

public class _10280_MutatedKaneusSchuttgart extends Quest implements ScriptFile
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
	
	private static final int Vishotsky = 31981;
	private static final int Atraxia = 31972;
	private static final int VenomousStorace = 18571;
	private static final int KelBilette = 18573;
	private static final int Tissue1 = 13838;
	private static final int Tissue2 = 13839;
	
	public _10280_MutatedKaneusSchuttgart()
	{
		super(true);
		addStartNpc(Vishotsky);
		addTalkId(Atraxia);
		addKillId(VenomousStorace, KelBilette);
		addQuestItem(Tissue1, Tissue2);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if (event.equalsIgnoreCase("31981-03.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("31972-02.htm"))
		{
			st.giveItems(57, 420000);
			st.addExpAndSp(2060000, 1840000);
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
			if (npcId == Vishotsky)
			{
				htmltext = "31981-0a.htm";
			}
		}
		else if ((id == CREATED) && (npcId == Vishotsky))
		{
			if (st.getPlayer().getLevel() >= 58)
			{
				htmltext = "31981-01.htm";
			}
			else
			{
				htmltext = "31981-00.htm";
			}
		}
		else if (npcId == Vishotsky)
		{
			if (cond == 1)
			{
				htmltext = "31981-04.htm";
			}
			else if (cond == 2)
			{
				htmltext = "31981-05.htm";
			}
		}
		else if (npcId == Atraxia)
		{
			if (cond == 1)
			{
				htmltext = "31972-01a.htm";
			}
			else if (cond == 2)
			{
				htmltext = "31972-01.htm";
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
