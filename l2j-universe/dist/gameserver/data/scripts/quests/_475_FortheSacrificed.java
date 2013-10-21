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

import org.apache.commons.lang3.ArrayUtils;

public class _475_FortheSacrificed extends Quest implements ScriptFile
{
	private static final int chance = 80;
	private static final int ashsw = 19495;
	private static final int[] mobstohunt =
	{
		20676,
		20677,
		21108,
		21109,
		21110,
		21111,
		21112,
		21113,
		21114,
		21115,
		21116
	};
	private static final int advent = 33463;
	private static final int ross = 30858;
	
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
	
	public _475_FortheSacrificed()
	{
		super(PARTY_ONE);
		addStartNpc(advent);
		addTalkId(advent);
		addTalkId(ross);
		addKillId(mobstohunt);
		addQuestItem(ashsw);
		addLevelCheck(65, 69);
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
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		int cond = st.getCond();
		int npcId = npc.getNpcId();
		String htmltext = "noquest";
		if (npcId == advent)
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
					htmltext = TODO_FIND_HTML;
				}
			}
			else if ((cond == 1) || (cond == 2))
			{
				htmltext = "0-5.htm";
			}
		}
		else if (npcId == ross)
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
					htmltext = TODO_FIND_HTML;
				}
			}
			else if (cond == 1)
			{
				htmltext = TODO_FIND_HTML;
			}
			else if (cond == 2)
			{
				htmltext = "1-1.htm";
				st.takeAllItems(ashsw);
				st.exitCurrentQuest(this);
				st.playSound(SOUND_FINISH);
				st.getPlayer().addExpAndSp(3904500, 2813550);
				st.giveItems(57, 118500);
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		int npcId = npc.getNpcId();
		if ((st.getCond() == 1) && ArrayUtils.contains(mobstohunt, npcId) && (st.getQuestItemsCount(ashsw) < 30))
		{
			st.rollAndGive(ashsw, 1, chance);
			st.playSound(SOUND_ITEMGET);
		}
		if (st.getQuestItemsCount(ashsw) >= 30)
		{
			st.setCond(2);
			st.playSound(SOUND_MIDDLE);
		}
		return null;
	}
}
