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

import org.apache.commons.lang3.ArrayUtils;

public class _10359_SakumsTrace extends Quest implements ScriptFile
{
	private static final int guild = 31795;
	private static final int fred = 33179;
	private static final int reins = 30288;
	private static final int raimon = 30289;
	private static final int tobias = 30297;
	private static final int Drikus = 30505;
	private static final int mendius = 30504;
	private static final int gershfin = 32196;
	private static final int elinia = 30155;
	private static final int ershandel = 30158;
	private static final int frag = 17586;
	private static final int[] huntl =
	{
		20067,
		20070,
		20072
	};
	private static final int[] hunth =
	{
		23097,
		23098,
		23026,
		20192
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
	
	public _10359_SakumsTrace()
	{
		super(false);
		addStartNpc(guild);
		addTalkId(fred);
		addTalkId(reins);
		addTalkId(raimon);
		addTalkId(tobias);
		addTalkId(Drikus);
		addTalkId(mendius);
		addTalkId(gershfin);
		addTalkId(elinia);
		addTalkId(ershandel);
		addTalkId(guild);
		addKillId(huntl);
		addKillId(hunth);
		addQuestItem(frag);
		addLevelCheck(34, 40);
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
			st.getPlayer().addExpAndSp(670000, 220000);
			st.giveItems(57, 108000);
			st.exitCurrentQuest(false);
			st.playSound(SOUND_FINISH);
			if (st.getPlayer().getRace() == Race.human)
			{
				if (st.getPlayer().isMageClass())
				{
					htmltext = "2-3re.htm";
				}
				else
				{
					htmltext = "2-3r.htm";
				}
			}
			else if (st.getPlayer().getRace() == Race.elf)
			{
				if (st.getPlayer().isMageClass())
				{
					htmltext = "2-3e.htm";
				}
				else
				{
					htmltext = "2-3ew.htm";
				}
			}
			else if (st.getPlayer().getRace() == Race.darkelf)
			{
				htmltext = "2-3t.htm";
			}
			else if (st.getPlayer().getRace() == Race.orc)
			{
				htmltext = "2-3d.htm";
			}
			else if (st.getPlayer().getRace() == Race.dwarf)
			{
				htmltext = "2-3m.htm";
			}
			else if (st.getPlayer().getRace() == Race.kamael)
			{
				htmltext = "2-3g.htm";
			}
		}
		if (event.equalsIgnoreCase("1-3.htm"))
		{
			st.setCond(2);
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
		else if (npcId == fred)
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
			else if (cond == 2)
			{
				htmltext = "1-4.htm";
			}
			else if (cond == 3)
			{
				if (st.getPlayer().getRace() == Race.human)
				{
					if (st.getPlayer().isMageClass())
					{
						htmltext = "1-5re.htm";
						st.setCond(4);
						addTalkId(raimon);
					}
					else
					{
						htmltext = "1-5r.htm";
						st.setCond(5);
						addTalkId(reins);
					}
				}
				else if (st.getPlayer().getRace() == Race.elf)
				{
					if (st.getPlayer().isMageClass())
					{
						htmltext = "1-5e.htm";
						st.setCond(11);
						addTalkId(elinia);
					}
					else
					{
						htmltext = "1-5ew.htm";
						st.setCond(10);
						addTalkId(ershandel);
					}
				}
				else if (st.getPlayer().getRace() == Race.darkelf)
				{
					htmltext = "1-5t.htm";
					st.setCond(6);
					addTalkId(tobias);
				}
				else if (st.getPlayer().getRace() == Race.orc)
				{
					htmltext = "1-5d.htm";
					st.setCond(7);
					addTalkId(Drikus);
				}
				else if (st.getPlayer().getRace() == Race.dwarf)
				{
					htmltext = "1-5m.htm";
					st.setCond(8);
					addTalkId(mendius);
				}
				else if (st.getPlayer().getRace() == Race.kamael)
				{
					htmltext = "1-5g.htm";
					st.setCond(9);
					addTalkId(gershfin);
				}
			}
		}
		else if ((npcId == raimon) && (st.getPlayer().getRace() == Race.human) && st.getPlayer().isMageClass())
		{
			if (st.isCompleted())
			{
				htmltext = "2re-c.htm";
			}
			else if (cond == 0)
			{
				htmltext = TODO_FIND_HTML;
			}
			else if (cond == 4)
			{
				htmltext = "2-1re.htm";
			}
		}
		else if ((npcId == reins) && (st.getPlayer().getRace() == Race.human) && !st.getPlayer().isMageClass())
		{
			if (st.isCompleted())
			{
				htmltext = "2r-c.htm";
			}
			else if (cond == 0)
			{
				htmltext = TODO_FIND_HTML;
			}
			else if (cond == 5)
			{
				htmltext = "2-1r.htm";
			}
		}
		else if ((npcId == tobias) && (st.getPlayer().getRace() == Race.darkelf))
		{
			if (st.isCompleted())
			{
				htmltext = "2t-c.htm";
			}
			else if (cond == 0)
			{
				htmltext = TODO_FIND_HTML;
			}
			else if (cond == 6)
			{
				htmltext = "2-1t.htm";
			}
		}
		else if ((npcId == Drikus) && (st.getPlayer().getRace() == Race.orc))
		{
			if (st.isCompleted())
			{
				htmltext = "2d-c.htm";
			}
			else if (cond == 0)
			{
				htmltext = TODO_FIND_HTML;
			}
			else if (cond == 7)
			{
				htmltext = "2-1d.htm";
			}
		}
		else if ((npcId == gershfin) && (st.getPlayer().getRace() == Race.kamael))
		{
			if (st.isCompleted())
			{
				htmltext = "2g-c.htm";
			}
			else if (cond == 0)
			{
				htmltext = TODO_FIND_HTML;
			}
			else if (cond == 9)
			{
				htmltext = "2-1g.htm";
			}
		}
		else if ((npcId == elinia) && (st.getPlayer().getRace() == Race.elf) && !st.getPlayer().isMageClass())
		{
			if (st.isCompleted())
			{
				htmltext = "2ew-c.htm";
			}
			else if (cond == 0)
			{
				htmltext = TODO_FIND_HTML;
			}
			else if (cond == 10)
			{
				htmltext = "2-1e.htm";
			}
		}
		else if ((npcId == ershandel) && (st.getPlayer().getRace() == Race.elf) && st.getPlayer().isMageClass())
		{
			if (st.isCompleted())
			{
				htmltext = "2e-c.htm";
			}
			else if (cond == 0)
			{
				htmltext = TODO_FIND_HTML;
			}
			else if (cond == 11)
			{
				htmltext = "2-1ew.htm";
			}
		}
		else if ((npcId == mendius) && (st.getPlayer().getRace() == Race.dwarf))
		{
			if (st.isCompleted())
			{
				htmltext = "2m-c.htm";
			}
			else if (cond == 0)
			{
				htmltext = TODO_FIND_HTML;
			}
			else if (cond == 8)
			{
				htmltext = "2-1m.htm";
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		int cond = st.getCond();
		int npcId = npc.getNpcId();
		if ((cond == 2) && (st.getQuestItemsCount(frag) < 20))
		{
			if (ArrayUtils.contains(huntl, npcId))
			{
				st.rollAndGive(frag, 1, 15);
			}
			else if (ArrayUtils.contains(hunth, npcId))
			{
				st.rollAndGive(frag, 1, 35);
			}
		}
		if (st.getQuestItemsCount(frag) >= 20)
		{
			st.setCond(3);
		}
		return null;
	}
}
