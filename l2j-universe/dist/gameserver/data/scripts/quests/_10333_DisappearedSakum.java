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

public class _10333_DisappearedSakum extends Quest implements ScriptFile
{
	private static final int shnain = 33508;
	private static final int bent = 33176;
	private static final int batis = 30332;
	private static final int lizard = 20030;
	private static final int vooko = 20017;
	private static final int spider = 23094;
	private static final int bigspider = 20038;
	private static final int arach = 20050;
	private static final String vooko_item = "vooko";
	private static final String lizard_item = "lizard";
	private static final int mark = 17583;
	
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
	
	public _10333_DisappearedSakum()
	{
		super(false);
		addStartNpc(batis);
		addTalkId(batis);
		addTalkId(bent);
		addTalkId(shnain);
		addKillNpcWithLog(2, vooko_item, 5, vooko);
		addKillNpcWithLog(2, lizard_item, 7, lizard);
		addKillId(spider, bigspider, arach);
		addQuestItem(mark);
		addLevelCheck(18, 40);
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
			htmltext = "0-5.htm";
		}
		else if (event.equalsIgnoreCase("qet_rev"))
		{
			htmltext = "2-3.htm";
			st.getPlayer().addExpAndSp(130000, 50000);
			st.giveItems(57, 80000);
			st.takeAllItems(mark);
			st.exitCurrentQuest(false);
			st.playSound(SOUND_FINISH);
		}
		else if (event.equalsIgnoreCase("1-3.htm"))
		{
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
		if (npcId == batis)
		{
			if (st.isCompleted())
			{
				htmltext = "0-c.htm";
			}
			else if ((cond == 0) && isAvailableFor(st.getPlayer()))
			{
				htmltext = "start.htm";
			}
			else if ((cond == 1) || (cond == 2) || (cond == 3))
			{
				htmltext = "0-6.htm";
			}
			else
			{
				htmltext = "0-nc.htm";
			}
		}
		else if (npcId == bent)
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
			else if ((cond == 2) || (cond == 3))
			{
				htmltext = "1-3.htm";
			}
		}
		else if (npcId == shnain)
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
		if (((npc.getNpcId() == spider) || (npc.getNpcId() == bigspider) || (npc.getNpcId() == arach)) && (st.getQuestItemsCount(mark) < 5) && (st.getCond() == 2))
		{
			st.giveItems(mark, 1, false);
		}
		if (doneKill && (st.getQuestItemsCount(mark) >= 5))
		{
			st.unset(vooko_item);
			st.unset(lizard_item);
			st.setCond(3);
			st.playSound(SOUND_MIDDLE);
		}
		return null;
	}
}
