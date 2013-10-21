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

public class _10337_SakumsImpact extends Quest implements ScriptFile
{
	private static final int guild = 31795;
	private static final int silvana = 33178;
	private static final int lef = 33510;
	private static final int bes = 20506;
	private static final int skelet = 23022;
	private static final int batt = 27458;
	private static final int sc_bat = 20411;
	private static final int ruin_bat = 20505;
	private static final String bes_item = "bes";
	private static final String bat_item = "bat";
	private static final String skelet_item = "skelet";
	
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
	
	public _10337_SakumsImpact()
	{
		super(false);
		addStartNpc(guild);
		addTalkId(silvana);
		addTalkId(guild);
		addTalkId(lef);
		addKillNpcWithLog(2, bes_item, 20, bes);
		addKillNpcWithLog(2, bat_item, 25, batt, sc_bat, ruin_bat);
		addKillNpcWithLog(2, skelet_item, 15, skelet);
		addLevelCheck(28, 40);
		addQuestCompletedCheck(_10336_DividedSakumKanilov.class);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if (event.equalsIgnoreCase("quest_ac"))
		{
			st.setState(STARTED);
			st.setCond(1);
			st.playSound(SOUND_ACCEPT);
			htmltext = "0-3.htm";
		}
		if (event.equalsIgnoreCase("qet_rev"))
		{
			htmltext = "2-2.htm";
			st.getPlayer().addExpAndSp(470000, 160000);
			st.giveItems(57, 103000);
			st.exitCurrentQuest(false);
			st.playSound(SOUND_FINISH);
		}
		if (event.equalsIgnoreCase("1-3.htm"))
		{
			htmltext = "1-3.htm";
			st.setCond(2);
			st.playSound(SOUND_MIDDLE);
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		int cond = st.getCond();
		int npcId = npc.getNpcId();
		String htmltext = "noquest";
		if (npcId == guild)
		{
			if (st.isCompleted())
			{
				htmltext = TODO_FIND_HTML;
			}
			else if ((cond == 0) && isAvailableFor(st.getPlayer()))
			{
				htmltext = "0-1.htm";
			}
			else if ((cond == 1) || (cond == 2) || (cond == 3))
			{
				htmltext = "0-4.htm";
			}
		}
		else if (npcId == silvana)
		{
			if (st.isCompleted())
			{
				htmltext = TODO_FIND_HTML;
			}
			else if (cond == 0)
			{
				htmltext = TODO_FIND_HTML;
			}
			else if (cond == 1)
			{
				htmltext = "1-1.htm";
			}
			else if ((cond == 2) || (cond == 3))
			{
				htmltext = "1-4.htm";
			}
		}
		else if (npcId == lef)
		{
			if (st.isCompleted())
			{
				htmltext = TODO_FIND_HTML;
			}
			else if (cond == 0)
			{
				htmltext = TODO_FIND_HTML;
			}
			else if (cond == 1)
			{
				htmltext = TODO_FIND_HTML;
			}
			else if (cond == 2)
			{
				htmltext = TODO_FIND_HTML;
			}
			else if (cond == 3)
			{
				htmltext = "2-1.htm";
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		boolean doneKill = updateKill(npc, st);
		if (doneKill)
		{
			st.unset(bes_item);
			st.unset(skelet_item);
			st.unset(bat_item);
			st.setCond(3);
		}
		return null;
	}
}
