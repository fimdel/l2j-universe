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

public class _10267_JourneyToGracia extends Quest implements ScriptFile
{
	private final static int Orven = 30857;
	private final static int Keucereus = 32548;
	private final static int Papiku = 32564;
	private final static int Letter = 13810;
	
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
	
	public _10267_JourneyToGracia()
	{
		super(false);
		addStartNpc(Orven);
		addTalkId(Keucereus);
		addTalkId(Papiku);
		addQuestItem(Letter);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		if (event.equalsIgnoreCase("30857-06.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
			st.giveItems(Letter, 1);
		}
		else if (event.equalsIgnoreCase("32564-02.htm"))
		{
			st.setCond(2);
			st.playSound(SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("32548-02.htm"))
		{
			st.giveItems(ADENA_ID, 1135000);
			st.takeItems(Letter, -1);
			st.addExpAndSp(5326400, 6000000);
			st.unset("cond");
			st.exitCurrentQuest(false);
			st.playSound(SOUND_FINISH);
		}
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int id = st.getState();
		int npcId = npc.getNpcId();
		int cond = st.getCond();
		if (id == COMPLETED)
		{
			if (npcId == Keucereus)
			{
				htmltext = "32548-03.htm";
			}
			else if (npcId == Orven)
			{
				htmltext = "30857-0a.htm";
			}
		}
		else if (id == CREATED)
		{
			if (npcId == Orven)
			{
				if (st.getPlayer().getLevel() < 75)
				{
					htmltext = "30857-00.htm";
				}
				else
				{
					htmltext = "30857-01.htm";
				}
			}
		}
		else if (id == STARTED)
		{
			if (npcId == Orven)
			{
				htmltext = "30857-07.htm";
			}
			else if (npcId == Papiku)
			{
				if (cond == 1)
				{
					htmltext = "32564-01.htm";
				}
				else
				{
					htmltext = "32564-03.htm";
				}
			}
			else if ((npcId == Keucereus) && (cond == 2))
			{
				htmltext = "32548-01.htm";
			}
		}
		return htmltext;
	}
}
