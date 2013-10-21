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

public class _10335_RequesttoFindSakum extends Quest implements ScriptFile
{
	private static final int batis = 30332;
	private static final int kalesin = 33177;
	private static final int jena = 33509;
	private static final int sledopyt = 20035;
	private static final int strelec = 20051;
	private static final int skelet = 20054;
	private static final int zombie = 20026;
	private static final String sledopyt_item = "sledopyt";
	private static final String strelec_item = "strelec";
	private static final String skelet_item = "skelet";
	private static final String zombie_item = "zombie";
	
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
	
	public _10335_RequesttoFindSakum()
	{
		super(false);
		addStartNpc(batis);
		addTalkId(batis);
		addTalkId(kalesin);
		addTalkId(jena);
		addKillNpcWithLog(2, sledopyt_item, 10, sledopyt);
		addKillNpcWithLog(2, strelec_item, 10, strelec);
		addKillNpcWithLog(2, skelet_item, 15, skelet);
		addKillNpcWithLog(2, zombie_item, 15, zombie);
		addLevelCheck(23, 40);
		addQuestCompletedCheck(_10334_WindmillHillStatusReport.class);
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
		else if (event.equalsIgnoreCase("qet_rev"))
		{
			htmltext = "2-3.htm";
			st.getPlayer().addExpAndSp(250000, 100000);
			st.giveItems(57, 90000);
			st.exitCurrentQuest(false);
			st.playSound(SOUND_FINISH);
		}
		else if (event.equalsIgnoreCase("1-2.htm"))
		{
			st.setCond(2);
			htmltext = "1-2.htm";
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
		if (npcId == batis)
		{
			if (st.isCompleted())
			{
				htmltext = "0-c.htm";
			}
			else if ((cond == 0) && isAvailableFor(st.getPlayer()))
			{
				htmltext = "0-1.htm";
			}
			else if ((cond == 1) || (cond == 2) || (cond == 3))
			{
				htmltext = "0-3.htm";
			}
			else
			{
				htmltext = "0-nc.htm";
			}
		}
		else if (npcId == kalesin)
		{
			if (st.isCompleted())
			{
				htmltext = "1-c.htm";
			}
			else if (cond == 0)
			{
				htmltext = TODO_FIND_HTML;
			}
			else if (cond == 1)
			{
				htmltext = "1-1.htm";
			}
			else if (cond == 2)
			{
				htmltext = "1-3.htm";
			}
		}
		else if (npcId == jena)
		{
			if (st.isCompleted())
			{
				htmltext = "2-c.htm";
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
			st.unset(sledopyt_item);
			st.unset(strelec_item);
			st.unset(skelet_item);
			st.unset(zombie_item);
			st.setCond(3);
		}
		return null;
	}
}
