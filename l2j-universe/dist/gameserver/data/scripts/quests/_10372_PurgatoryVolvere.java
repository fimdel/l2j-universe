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

public class _10372_PurgatoryVolvere extends Quest implements ScriptFile
{
	private static final int andreig = 31292;
	private static final int gerkenshtein = 33648;
	private static final int Essence = 34766;
	private static final int[] classesav =
	{
		88,
		89,
		90,
		91,
		92,
		93,
		94,
		95,
		96,
		97,
		98,
		99,
		100,
		101,
		102,
		103,
		104,
		105,
		106,
		107,
		108,
		109,
		110,
		111,
		112,
		113,
		114,
		115,
		116,
		117,
		118,
		136,
		135,
		134,
		132,
		133
	};
	private static final int Bloody = 23185;
	private static final int chance = 10;
	
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
	
	public _10372_PurgatoryVolvere()
	{
		super(false);
		addStartNpc(gerkenshtein);
		addTalkId(gerkenshtein);
		addTalkId(andreig);
		addKillId(Bloody);
		addQuestItem(Essence);
		addLevelCheck(76, 81);
		addQuestCompletedCheck(_10371_GraspThyPower.class);
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
		else if (event.equalsIgnoreCase("firec"))
		{
			htmltext = "1-4.htm";
			st.getPlayer().addExpAndSp(23009000, 26440100);
			st.giveItems(9552, 1);
			st.takeAllItems(Essence);
			st.exitCurrentQuest(false);
			st.playSound(SOUND_FINISH);
		}
		else if (event.equalsIgnoreCase("waterc"))
		{
			htmltext = "1-4.htm";
			st.getPlayer().addExpAndSp(23009000, 26440100);
			st.giveItems(9553, 1);
			st.takeAllItems(Essence);
			st.exitCurrentQuest(false);
			st.playSound(SOUND_FINISH);
		}
		else if (event.equalsIgnoreCase("windc"))
		{
			htmltext = "1-4.htm";
			st.getPlayer().addExpAndSp(23009000, 26440100);
			st.giveItems(9555, 1);
			st.takeAllItems(Essence);
			st.exitCurrentQuest(false);
			st.playSound(SOUND_FINISH);
		}
		else if (event.equalsIgnoreCase("earth"))
		{
			htmltext = "1-4.htm";
			st.getPlayer().addExpAndSp(23009000, 26440100);
			st.giveItems(9554, 1);
			st.takeAllItems(Essence);
			st.exitCurrentQuest(false);
			st.playSound(SOUND_FINISH);
		}
		else if (event.equalsIgnoreCase("darkc"))
		{
			htmltext = "1-4.htm";
			st.getPlayer().addExpAndSp(23009000, 26440100);
			st.giveItems(9556, 1);
			st.takeAllItems(Essence);
			st.exitCurrentQuest(false);
			st.playSound(SOUND_FINISH);
		}
		else if (event.equalsIgnoreCase("holyc"))
		{
			htmltext = "1-4.htm";
			st.getPlayer().addExpAndSp(23009000, 26440100);
			st.giveItems(9557, 1);
			st.takeAllItems(Essence);
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
		Player player = st.getPlayer();
		int classid = player.getClassId().getId();
		String htmltext = "noquest";
		if (npcId == gerkenshtein)
		{
			if (st.isCompleted())
			{
				htmltext = "0-c.htm";
			}
			else if (cond == 0)
			{
				if (isAvailableFor(st.getPlayer()) && ArrayUtils.contains(classesav, classid))
				{
					htmltext = "start.htm";
				}
				else
				{
					htmltext = TODO_FIND_HTML;
				}
			}
			else if (cond == 1)
			{
				htmltext = "0-5.htm";
			}
			else if (cond == 2)
			{
				htmltext = "0-6.htm";
				st.setCond(3);
				st.giveItems(34767, 1, false);
				st.takeAllItems(Essence);
				st.playSound(SOUND_MIDDLE);
			}
			else if (cond == 3)
			{
				htmltext = "0-3.htm";
			}
		}
		else if (npcId == andreig)
		{
			if (st.isCompleted())
			{
				htmltext = "1-4.htm";
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
				htmltext = "1-1.htm";
				st.takeAllItems(34767);
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		int npcId = npc.getNpcId();
		if ((st.getCond() == 1) && (npcId == Bloody) && (st.getQuestItemsCount(Essence) < 10))
		{
			st.rollAndGive(Essence, 1, chance);
			st.playSound(SOUND_ITEMGET);
		}
		if (st.getQuestItemsCount(Essence) >= 10)
		{
			st.setCond(2);
			st.playSound(SOUND_MIDDLE);
		}
		return null;
	}
}
