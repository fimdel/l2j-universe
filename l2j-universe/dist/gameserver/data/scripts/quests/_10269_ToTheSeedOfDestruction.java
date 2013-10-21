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

public class _10269_ToTheSeedOfDestruction extends Quest implements ScriptFile
{
	private final static int Keucereus = 32548;
	private final static int Allenos = 32526;
	private final static int Introduction = 13812;
	
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
	
	public _10269_ToTheSeedOfDestruction()
	{
		super(false);
		addStartNpc(Keucereus);
		addTalkId(Allenos);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		if (event.equalsIgnoreCase("32548-05.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
			st.giveItems(Introduction, 1);
		}
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int id = st.getState();
		int npcId = npc.getNpcId();
		if (id == COMPLETED)
		{
			if (npcId == Allenos)
			{
				htmltext = "32526-02.htm";
			}
			else
			{
				htmltext = "32548-0a.htm";
			}
		}
		else if ((id == CREATED) && (npcId == Keucereus))
		{
			if (st.getPlayer().getLevel() < 75)
			{
				htmltext = "32548-00.htm";
			}
			else
			{
				htmltext = "32548-01.htm";
			}
		}
		else if ((id == STARTED) && (npcId == Keucereus))
		{
			htmltext = "32548-06.htm";
		}
		else if ((id == STARTED) && (npcId == Allenos))
		{
			htmltext = "32526-01.htm";
			st.giveItems(ADENA_ID, 710000);
			st.addExpAndSp(6660000, 7375000);
			st.exitCurrentQuest(false);
			st.playSound(SOUND_FINISH);
		}
		return htmltext;
	}
}
