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
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

import org.apache.commons.lang3.ArrayUtils;

public class _489_InThisQuietPlace extends Quest implements ScriptFile
{
	private static final int chance = 50;
	private static final int TraceofEvil = 19501;
	private static final int[] mobstohunt =
	{
		21646,
		21647,
		21648,
		21649,
		21650,
		21651
	};
	private static final int bastian = 31280;
	private static final int Adventurequid = 33463;
	
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
	
	public _489_InThisQuietPlace()
	{
		super(false);
		addStartNpc(Adventurequid);
		addTalkId(Adventurequid);
		addTalkId(bastian);
		addKillId(mobstohunt);
		addQuestItem(TraceofEvil);
		addLevelCheck(75, 79);
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
			htmltext = "0-4.htm";
		}
		if (event.equalsIgnoreCase("qet_rev"))
		{
			htmltext = "1-3.htm";
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		int cond = st.getCond();
		int npcId = npc.getNpcId();
		String htmltext = "noquest";
		if (npcId == Adventurequid)
		{
			if (cond == 0)
			{
				if (isAvailableFor(st.getPlayer()))
				{
					if (st.isNowAvailableByTime())
					{
						htmltext = "0-1.htm";
					}
					else
					{
						htmltext = "0-c.htm";
					}
				}
				else
				{
					htmltext = "0-nc.htm";
				}
			}
			else if ((cond == 1) || (cond == 2))
			{
				htmltext = "0-5.htm";
			}
		}
		else if (npcId == bastian)
		{
			if (cond == 0)
			{
				if (isAvailableFor(st.getPlayer()))
				{
					if (st.isNowAvailableByTime())
					{
						htmltext = TODO_FIND_HTML;
					}
					else
					{
						htmltext = "1-c.htm";
					}
				}
				else
				{
					htmltext = "1-nc.htm";
				}
			}
			else if (cond == 1)
			{
				htmltext = TODO_FIND_HTML;
			}
			else if (cond == 2)
			{
				htmltext = "1-1.htm";
				st.getPlayer().addExpAndSp(19890000, 22602330);
				st.giveItems(57, 426045);
				st.takeAllItems(TraceofEvil);
				st.exitCurrentQuest(this);
				st.playSound(SOUND_FINISH);
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		int npcId = npc.getNpcId();
		if ((st.getCond() == 1) && ArrayUtils.contains(mobstohunt, npcId) && (st.getQuestItemsCount(TraceofEvil) < 77))
		{
			st.rollAndGive(TraceofEvil, 1, chance);
			st.playSound(SOUND_ITEMGET);
		}
		if (st.getQuestItemsCount(TraceofEvil) >= 77)
		{
			st.setCond(2);
			st.playSound(SOUND_MIDDLE);
		}
		return null;
	}
	
	@Override
	public boolean isVisible(Player player)
	{
		QuestState qs = player.getQuestState(_489_InThisQuietPlace.class);
		return ((qs == null) && isAvailableFor(player)) || ((qs != null) && qs.isNowAvailableByTime());
	}
}
