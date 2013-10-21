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

public class _10268_ToTheSeedOfInfinity extends Quest implements ScriptFile
{
	private final static int Keucereus = 32548;
	private final static int Tepios = 32603;
	private final static int Introduction = 13811;
	
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
	
	public _10268_ToTheSeedOfInfinity()
	{
		super(false);
		addStartNpc(Keucereus);
		addTalkId(Tepios);
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
		switch (id)
		{
			case CREATED:
				if (npcId == Keucereus)
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
				break;
			case STARTED:
				if (npcId == Keucereus)
				{
					htmltext = "32548-06.htm";
				}
				else if (npcId == Tepios)
				{
					htmltext = "32530-01.htm";
					st.giveItems(ADENA_ID, 425500);
					st.addExpAndSp(4000000, 4425000);
					st.exitCurrentQuest(false);
					st.playSound(SOUND_FINISH);
				}
				break;
		}
		return htmltext;
	}
}
