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

import lineage2.gameserver.model.base.Race;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class _10366_RuinsStatusUpdate extends Quest implements ScriptFile
{
	private static final int revian = 32147;
	private static final int tuk = 32150;
	private static final int prana = 32153;
	private static final int devon = 32160;
	private static final int moka = 32157;
	private static final int valpor = 32146;
	private static final int sebion = 32978;
	
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
	
	public _10366_RuinsStatusUpdate()
	{
		super(false);
		addStartNpc(sebion);
		addTalkId(revian);
		addTalkId(tuk);
		addTalkId(prana);
		addTalkId(devon);
		addTalkId(moka);
		addTalkId(valpor);
		addTalkId(sebion);
		addLevelCheck(16, 25);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if (event.equalsIgnoreCase("qet_rev"))
		{
			st.getPlayer().addExpAndSp(150000, 30000);
			st.giveItems(57, 75000);
			st.exitCurrentQuest(false);
			st.playSound(SOUND_FINISH);
			{
				if (st.getPlayer().getRace() == Race.human)
				{
					htmltext = "1-5h.htm";
				}
				else if (st.getPlayer().getRace() == Race.elf)
				{
					htmltext = "1-5e.htm";
				}
				else if (st.getPlayer().getRace() == Race.darkelf)
				{
					htmltext = "1-5de.htm";
				}
				else if (st.getPlayer().getRace() == Race.dwarf)
				{
					htmltext = "1-5d.htm";
				}
				else if (st.getPlayer().getRace() == Race.kamael)
				{
					htmltext = "1-5k.htm";
				}
				else if (st.getPlayer().getRace() == Race.orc)
				{
					htmltext = "1-5o.htm";
				}
			}
		}
		else if (event.equalsIgnoreCase("quest_ac"))
		{
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
			if (st.getPlayer().getRace() == Race.human)
			{
				st.setCond(2);
				htmltext = "0-4h.htm";
			}
			else if (st.getPlayer().getRace() == Race.elf)
			{
				st.setCond(3);
				htmltext = "0-4e.htm";
			}
			else if (st.getPlayer().getRace() == Race.darkelf)
			{
				st.setCond(4);
				htmltext = "0-4de.htm";
			}
			else if (st.getPlayer().getRace() == Race.dwarf)
			{
				st.setCond(6);
				htmltext = "0-4d.htm";
			}
			else if (st.getPlayer().getRace() == Race.kamael)
			{
				st.setCond(7);
				htmltext = "0-4k.htm";
			}
			else if (st.getPlayer().getRace() == Race.orc)
			{
				st.setCond(5);
				htmltext = "0-4o.htm";
			}
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		int cond = st.getCond();
		int npcId = npc.getNpcId();
		String htmltext = "noquest";
		if (npcId == sebion)
		{
			if (st.isCompleted())
			{
				htmltext = TODO_FIND_HTML;
			}
			else if ((cond == 0) && isAvailableFor(st.getPlayer()))
			{
				htmltext = "start.htm";
			}
			else if ((cond == 2) || (cond == 3) || (cond == 4) || (cond == 5) || (cond == 6) || (cond == 7))
			{
				if (st.getPlayer().getRace() == Race.human)
				{
					htmltext = "0-5h.htm";
				}
				else if (st.getPlayer().getRace() == Race.elf)
				{
					htmltext = "0-5e.htm";
				}
				else if (st.getPlayer().getRace() == Race.darkelf)
				{
					htmltext = "0-5de.htm";
				}
				else if (st.getPlayer().getRace() == Race.dwarf)
				{
					htmltext = "0-5d.htm";
				}
				else if (st.getPlayer().getRace() == Race.kamael)
				{
					htmltext = "0-5k.htm";
				}
				else if (st.getPlayer().getRace() == Race.orc)
				{
					htmltext = "0-5o.htm";
				}
			}
			else
			{
				htmltext = "0-nc.htm";
			}
		}
		else if (npcId == devon)
		{
			if (st.isCompleted())
			{
				htmltext = "1-5dec.htm";
			}
			else if (st.getPlayer().getRace() == Race.darkelf)
			{
				if (cond == 0)
				{
					htmltext = TODO_FIND_HTML;
				}
				else if (cond == 4)
				{
					htmltext = "1-3de.htm";
				}
			}
			else
			{
				htmltext = "1-2de.htm";
			}
		}
		else if (npcId == revian)
		{
			if (st.isCompleted())
			{
				htmltext = "1-5ec.htm";
			}
			else if (st.getPlayer().getRace() == Race.elf)
			{
				if (cond == 0)
				{
					htmltext = TODO_FIND_HTML;
				}
				else if (cond == 3)
				{
					htmltext = "1-3e.htm";
				}
			}
			else
			{
				htmltext = "1-2e.htm";
			}
		}
		else if (npcId == prana)
		{
			if (st.isCompleted())
			{
				htmltext = "1-5hc.htm";
			}
			else if (st.getPlayer().getRace() == Race.human)
			{
				if (cond == 0)
				{
					htmltext = TODO_FIND_HTML;
				}
				else if (cond == 2)
				{
					htmltext = "1-3h.htm";
				}
			}
			else
			{
				htmltext = "1-2h.htm";
			}
		}
		else if (npcId == valpor)
		{
			if (st.isCompleted())
			{
				htmltext = "1-5kc.htm";
			}
			else if (st.getPlayer().getRace() == Race.kamael)
			{
				if (cond == 0)
				{
					htmltext = TODO_FIND_HTML;
				}
				else if (cond == 7)
				{
					htmltext = "1-3k.htm";
				}
			}
			else
			{
				htmltext = "1-2k.htm";
			}
		}
		else if (npcId == moka)
		{
			if (st.isCompleted())
			{
				htmltext = "1-5dc.htm";
			}
			else if (st.getPlayer().getRace() == Race.dwarf)
			{
				if (cond == 0)
				{
					htmltext = TODO_FIND_HTML;
				}
				else if (cond == 6)
				{
					htmltext = "1-3d.htm";
				}
			}
			else
			{
				htmltext = "1-2d.htm";
			}
		}
		else if (npcId == tuk)
		{
			if (st.isCompleted())
			{
				htmltext = "1-5oc.htm";
			}
			else if (st.getPlayer().getRace() == Race.orc)
			{
				if (cond == 0)
				{
					htmltext = TODO_FIND_HTML;
				}
				else if (cond == 5)
				{
					htmltext = "1-3o.htm";
				}
			}
			else
			{
				htmltext = "1-2o.htm";
			}
		}
		return htmltext;
	}
}
