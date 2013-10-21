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

public class _10276_MutatedKaneusGludio extends Quest implements ScriptFile
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
	
	private static final int Bathis = 30332;
	private static final int Rohmer = 30344;
	private static final int TomlanKamos = 18554;
	private static final int OlAriosh = 18555;
	private static final int Tissue1 = 13830;
	private static final int Tissue2 = 13831;
	
	public _10276_MutatedKaneusGludio()
	{
		super(true);
		addStartNpc(Bathis);
		addTalkId(Rohmer);
		addKillId(TomlanKamos, OlAriosh);
		addQuestItem(Tissue1, Tissue2);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if (event.equalsIgnoreCase("30332-03.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("30344-02.htm"))
		{
			st.giveItems(57, 17000);
			st.addExpAndSp(70000, 32000);
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
			if (npcId == Bathis)
			{
				htmltext = "30332-0a.htm";
			}
		}
		else if ((id == CREATED) && (npcId == Bathis))
		{
			if (st.getPlayer().getLevel() >= 18)
			{
				htmltext = "30332-01.htm";
			}
			else
			{
				htmltext = "30332-00.htm";
			}
		}
		else if (npcId == Bathis)
		{
			if (cond == 1)
			{
				htmltext = "30332-04.htm";
			}
			else if (cond == 2)
			{
				htmltext = "30332-05.htm";
			}
		}
		else if (npcId == Rohmer)
		{
			if (cond == 1)
			{
				htmltext = "30344-01a.htm";
			}
			else if (cond == 2)
			{
				htmltext = "30344-01.htm";
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
