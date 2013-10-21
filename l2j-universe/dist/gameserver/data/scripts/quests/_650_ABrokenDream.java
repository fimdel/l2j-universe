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

public class _650_ABrokenDream extends Quest implements ScriptFile
{
	private static final int RailroadEngineer = 32054;
	private static final int ForgottenCrewman = 22027;
	private static final int VagabondOfTheRuins = 22028;
	private static final int RemnantsOfOldDwarvesDreams = 8514;
	
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
	
	public _650_ABrokenDream()
	{
		super(false);
		addStartNpc(RailroadEngineer);
		addKillId(ForgottenCrewman);
		addKillId(VagabondOfTheRuins);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if (event.equalsIgnoreCase("quest_accept"))
		{
			htmltext = "ghost_of_railroadman_q0650_0103.htm";
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
			st.setCond(1);
		}
		else if (event.equalsIgnoreCase("650_4"))
		{
			htmltext = "ghost_of_railroadman_q0650_0205.htm";
			st.playSound(SOUND_FINISH);
			st.exitCurrentQuest(true);
			st.unset("cond");
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		int cond = st.getCond();
		String htmltext = "noquest";
		if (cond == 0)
		{
			QuestState OceanOfDistantStar = st.getPlayer().getQuestState(_117_OceanOfDistantStar.class);
			if (OceanOfDistantStar != null)
			{
				if (OceanOfDistantStar.isCompleted())
				{
					if (st.getPlayer().getLevel() < 39)
					{
						st.exitCurrentQuest(true);
						htmltext = "ghost_of_railroadman_q0650_0102.htm";
					}
					else
					{
						htmltext = "ghost_of_railroadman_q0650_0101.htm";
					}
				}
				else
				{
					htmltext = "ghost_of_railroadman_q0650_0104.htm";
					st.exitCurrentQuest(true);
				}
			}
			else
			{
				htmltext = "ghost_of_railroadman_q0650_0104.htm";
				st.exitCurrentQuest(true);
			}
		}
		else if (cond == 1)
		{
			htmltext = "ghost_of_railroadman_q0650_0202.htm";
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		st.rollAndGive(RemnantsOfOldDwarvesDreams, 1, 1, 68);
		return null;
	}
}
