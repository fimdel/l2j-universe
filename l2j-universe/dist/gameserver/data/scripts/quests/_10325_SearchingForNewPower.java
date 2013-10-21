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

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.base.Race;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class _10325_SearchingForNewPower extends Quest implements ScriptFile
{
	private static final int galint = 32980;
	private final static int talbot = 32156;
	private final static int cindet = 32148;
	private final static int black = 32161;
	private final static int herz = 32151;
	private final static int kinkaid = 32159;
	private final static int xonia = 32144;
	
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
	
	public _10325_SearchingForNewPower()
	{
		super(false);
		addStartNpc(galint);
		addTalkId(galint);
		addTalkId(talbot);
		addTalkId(cindet);
		addTalkId(black);
		addTalkId(kinkaid);
		addTalkId(xonia);
		addTalkId(herz);
		addLevelCheck(1, 20);
		addQuestCompletedCheck(_10324_FindingMagisterGallint.class);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if (event.equalsIgnoreCase("0-2.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		if (event.equalsIgnoreCase("quest_ac"))
		{
			if (st.getPlayer().getRace() == Race.human)
			{
				st.playSound(SOUND_MIDDLE);
				st.setCond(2);
				htmltext = "0-3h.htm";
			}
			if (st.getPlayer().getRace() == Race.elf)
			{
				st.playSound(SOUND_MIDDLE);
				st.setCond(3);
				htmltext = "0-3e.htm";
			}
			if (st.getPlayer().getRace() == Race.darkelf)
			{
				st.playSound(SOUND_MIDDLE);
				st.setCond(4);
				htmltext = "0-3de.htm";
			}
			if (st.getPlayer().getRace() == Race.dwarf)
			{
				st.playSound(SOUND_MIDDLE);
				st.setCond(6);
				htmltext = "0-3d.htm";
			}
			if (st.getPlayer().getRace() == Race.kamael)
			{
				st.playSound(SOUND_MIDDLE);
				st.setCond(7);
				htmltext = "0-3k.htm";
			}
			if (st.getPlayer().getRace() == Race.orc)
			{
				st.playSound(SOUND_MIDDLE);
				st.setCond(5);
				htmltext = "0-3o.htm";
			}
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		Player player = st.getPlayer();
		int cond = st.getCond();
		int npcId = npc.getNpcId();
		String htmltext = "noquest";
		if (npcId == galint)
		{
			if (st.isCompleted())
			{
				htmltext = "0-c.htm";
			}
			else if ((cond == 0) && isAvailableFor(st.getPlayer()))
			{
				htmltext = "start.htm";
			}
			else if ((cond == 2) || (cond == 3) || (cond == 4) || (cond == 5) || (cond == 6) || (cond == 7))
			{
				htmltext = "0-4.htm";
			}
			else if (cond == 1)
			{
				htmltext = "0-2.htm";
			}
			else if ((cond == 8) || (cond == 9) || (cond == 10) || (cond == 11) || (cond == 12) || (cond == 13))
			{
				htmltext = "0-5.htm";
				st.getPlayer().addExpAndSp(3254, 2400);
				st.giveItems(57, 12000);
				st.exitCurrentQuest(false);
				st.playSound(SOUND_FINISH);
				if (!player.isMageClass() || (player.getTemplate().getRace() == Race.orc))
				{
					st.giveItems(5789, 1000);
				}
				else
				{
					st.giveItems(5790, 1000);
				}
			}
			else
			{
				htmltext = "0-nc.htm";
			}
		}
		else if (npcId == black)
		{
			if (st.getPlayer().getRace() == Race.darkelf)
			{
				if (cond == 4)
				{
					st.setCond(10);
					st.playSound(SOUND_MIDDLE);
					htmltext = "1-2de.htm";
				}
				if (cond == 10)
				{
					htmltext = "1-4de.htm";
				}
			}
			else
			{
				htmltext = "1-3de.htm";
			}
		}
		else if (npcId == cindet)
		{
			if (st.getPlayer().getRace() == Race.elf)
			{
				if (cond == 3)
				{
					st.setCond(9);
					st.playSound(SOUND_MIDDLE);
					htmltext = "1-2e.htm";
				}
				if (cond == 9)
				{
					htmltext = "1-4e.htm";
				}
			}
			else
			{
				htmltext = "1-3e.htm";
			}
		}
		else if (npcId == talbot)
		{
			if (st.getPlayer().getRace() == Race.human)
			{
				if (cond == 2)
				{
					st.setCond(8);
					st.playSound(SOUND_MIDDLE);
					htmltext = "1-2h.htm";
				}
				if (cond == 8)
				{
					htmltext = "1-4h.htm";
				}
			}
			else
			{
				htmltext = "1-3h.htm";
			}
		}
		else if (npcId == xonia)
		{
			if (st.getPlayer().getRace() == Race.kamael)
			{
				if (cond == 7)
				{
					st.setCond(13);
					st.playSound(SOUND_MIDDLE);
					htmltext = "1-2k.htm";
				}
				if (cond == 13)
				{
					htmltext = "1-4k.htm";
				}
			}
			else
			{
				htmltext = "1-3k.htm";
			}
		}
		else if (npcId == kinkaid)
		{
			if (st.getPlayer().getRace() == Race.dwarf)
			{
				if (cond == 6)
				{
					st.setCond(12);
					st.playSound(SOUND_MIDDLE);
					htmltext = "1-2d.htm";
				}
				if (cond == 12)
				{
					htmltext = "1-4d.htm";
				}
			}
			else
			{
				htmltext = "1-3d.htm";
			}
		}
		else if (npcId == herz)
		{
			if (st.getPlayer().getRace() == Race.orc)
			{
				if (cond == 5)
				{
					st.setCond(11);
					st.playSound(SOUND_MIDDLE);
					htmltext = "1-2o.htm";
				}
				if (cond == 11)
				{
					htmltext = "1-4o.htm";
				}
			}
			else
			{
				htmltext = "1-3o.htm";
			}
		}
		return htmltext;
	}
}
