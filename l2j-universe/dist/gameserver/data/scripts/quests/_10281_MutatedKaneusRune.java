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

public class _10281_MutatedKaneusRune extends Quest implements ScriptFile
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
	
	private static final int Mathias = 31340;
	private static final int Kayan = 31335;
	private static final int WhiteAllosce = 18577;
	private static final int Tissue = 13840;
	
	public _10281_MutatedKaneusRune()
	{
		super(true);
		addStartNpc(Mathias);
		addTalkId(Kayan);
		addKillId(WhiteAllosce);
		addQuestItem(Tissue);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if (event.equalsIgnoreCase("31340-03.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("31335-02.htm"))
		{
			st.giveItems(57, 720000);
			st.addExpAndSp(3500000, 3500000);
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
			if (npcId == Mathias)
			{
				htmltext = "31340-0a.htm";
			}
		}
		else if ((id == CREATED) && (npcId == Mathias))
		{
			if (st.getPlayer().getLevel() >= 68)
			{
				htmltext = "31340-01.htm";
			}
			else
			{
				htmltext = "31340-00.htm";
			}
		}
		else if (npcId == Mathias)
		{
			if (cond == 1)
			{
				htmltext = "31340-04.htm";
			}
			else if (cond == 2)
			{
				htmltext = "31340-05.htm";
			}
		}
		else if (npcId == Kayan)
		{
			if (cond == 1)
			{
				htmltext = "31335-01a.htm";
			}
			else if (cond == 2)
			{
				htmltext = "31335-01.htm";
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		if ((st.getState() == STARTED) && (st.getCond() == 1))
		{
			st.giveItems(Tissue, 1);
			st.setCond(2);
			st.playSound(SOUND_MIDDLE);
		}
		return null;
	}
}
