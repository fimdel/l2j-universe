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
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.NpcUtils;

public class _10365_SeekerEscort extends Quest implements ScriptFile
{
	private static final int dep = 33453;
	private static final int sebian = 32978;
	private static final int seeker = 32988;
	private NpcInstance seek = null;
	private static final int[] SOLDER_START_POINT =
	{
		-110616,
		238376,
		-2950
	};
	
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
	
	public _10365_SeekerEscort()
	{
		super(false);
		addStartNpc(dep);
		addTalkId(dep);
		addTalkId(sebian);
		addLevelCheck(16, 25);
		addQuestCompletedCheck(_10364_ObligationsOfSeeker.class);
	}
	
	private void spawnseeker(QuestState st)
	{
		seek = NpcUtils.spawnSingle(seeker, Location.findPointToStay(SOLDER_START_POINT[0], SOLDER_START_POINT[1], SOLDER_START_POINT[2], 50, 100, st.getPlayer().getGeoIndex()));
		seek.setFollowTarget(st.getPlayer());
		st.set("seeksp", 1);
		st.set("zone", 1);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if (event.equalsIgnoreCase("quest_ac"))
		{
			st.setState(STARTED);
			st.setCond(2); // Autocomplete tempfix {normal is setCond(1)}
			st.playSound(SOUND_ACCEPT);
			htmltext = "0-3.htm";
			spawnseeker(st);
		}
		if (event.equalsIgnoreCase("king"))
		{
			htmltext = "";
			spawnseeker(st);
		}
		if (event.equalsIgnoreCase("qet_rev")) // Russian emu QuestEvent. DO NOT EDIT THIS LINE!
		{
			htmltext = "1-2.htm";
			st.getPlayer().addExpAndSp(120000, 20000);
			st.giveItems(57, 65000);
			st.exitCurrentQuest(false);
			st.playSound(SOUND_FINISH);
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		int cond = st.getCond();
		int npcId = npc.getNpcId();
		String htmltext = "noquest";
		int seeksp = st.getInt("seeksp");
		if (npcId == dep)
		{
			if (st.isCompleted())
			{
				htmltext = "0-c.htm";
			}
			else if ((cond == 0) && isAvailableFor(st.getPlayer()))
			{
				htmltext = "start.htm";
			}
			else if ((cond == 1) && (seeksp == 0))
			{
				htmltext = "0-5.htm";
			}
			else if (cond == 1)
			{
				htmltext = "0-4.htm";
			}
			else
			{
				htmltext = "0-nc.htm";
			}
		}
		else if (npcId == sebian)
		{
			if (st.isCompleted())
			{
				htmltext = "1-c.htm";
			}
			else if (cond == 0)
			{
				htmltext = "1-nc.htm";
			}
			else if (cond == 1)
			{
				htmltext = TODO_FIND_HTML;
			}
			else if (cond == 2)
			{
				htmltext = "1-1.htm";
			}
		}
		return htmltext;
	}
}
