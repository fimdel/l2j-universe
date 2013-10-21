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

public class _623_TheFinestFood extends Quest implements ScriptFile
{
	public final int JEREMY = 31521;
	public static final int HOT_SPRINGS_BUFFALO = 21315;
	public static final int HOT_SPRINGS_FLAVA = 21316;
	public static final int HOT_SPRINGS_ANTELOPE = 21318;
	public static final int LEAF_OF_FLAVA = 7199;
	public static final int BUFFALO_MEAT = 7200;
	public static final int ANTELOPE_HORN = 7201;
	
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
	
	public _623_TheFinestFood()
	{
		super(true);
		addStartNpc(JEREMY);
		addTalkId(JEREMY);
		addKillId(HOT_SPRINGS_BUFFALO);
		addKillId(HOT_SPRINGS_FLAVA);
		addKillId(HOT_SPRINGS_ANTELOPE);
		addQuestItem(BUFFALO_MEAT);
		addQuestItem(LEAF_OF_FLAVA);
		addQuestItem(ANTELOPE_HORN);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if (event.equalsIgnoreCase("quest_accept"))
		{
			htmltext = "jeremy_q0623_0104.htm";
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("623_3"))
		{
			htmltext = "jeremy_q0623_0201.htm";
			st.takeItems(LEAF_OF_FLAVA, -1);
			st.takeItems(BUFFALO_MEAT, -1);
			st.takeItems(ANTELOPE_HORN, -1);
			st.giveItems(ADENA_ID, 73000);
			st.addExpAndSp(230000, 18250);
			st.playSound(SOUND_FINISH);
			st.exitCurrentQuest(true);
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int npcId = npc.getNpcId();
		int id = st.getState();
		if (id == CREATED)
		{
			st.setCond(0);
		}
		if (summ(st) >= 300)
		{
			st.setCond(2);
		}
		int cond = st.getCond();
		if (npcId == JEREMY)
		{
			if (cond == 0)
			{
				if (st.getPlayer().getLevel() >= 71)
				{
					htmltext = "jeremy_q0623_0101.htm";
				}
				else
				{
					htmltext = "jeremy_q0623_0103.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if ((cond == 1) && (summ(st) < 300))
			{
				htmltext = "jeremy_q0623_0106.htm";
			}
			else if ((cond == 2) && (summ(st) >= 300))
			{
				htmltext = "jeremy_q0623_0105.htm";
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		int cond = st.getCond();
		int npcId = npc.getNpcId();
		if (cond == 1)
		{
			if (npcId == HOT_SPRINGS_BUFFALO)
			{
				if (st.getQuestItemsCount(BUFFALO_MEAT) < 100)
				{
					st.giveItems(BUFFALO_MEAT, 1);
					if (st.getQuestItemsCount(BUFFALO_MEAT) == 100)
					{
						if (summ(st) >= 300)
						{
							st.setCond(2);
						}
						st.playSound(SOUND_MIDDLE);
					}
					else
					{
						st.playSound(SOUND_ITEMGET);
					}
				}
			}
			else if (npcId == HOT_SPRINGS_FLAVA)
			{
				if (st.getQuestItemsCount(LEAF_OF_FLAVA) < 100)
				{
					st.giveItems(LEAF_OF_FLAVA, 1);
					if (st.getQuestItemsCount(LEAF_OF_FLAVA) == 100)
					{
						if (summ(st) >= 300)
						{
							st.setCond(2);
						}
						st.playSound(SOUND_MIDDLE);
					}
					else
					{
						st.playSound(SOUND_ITEMGET);
					}
				}
			}
			else if (npcId == HOT_SPRINGS_ANTELOPE)
			{
				if (st.getQuestItemsCount(ANTELOPE_HORN) < 100)
				{
					st.giveItems(ANTELOPE_HORN, 1);
					if (st.getQuestItemsCount(ANTELOPE_HORN) == 100)
					{
						if (summ(st) >= 300)
						{
							st.setCond(2);
						}
						st.playSound(SOUND_MIDDLE);
					}
					else
					{
						st.playSound(SOUND_ITEMGET);
					}
				}
			}
		}
		return null;
	}
	
	private long summ(QuestState st)
	{
		return st.getQuestItemsCount(LEAF_OF_FLAVA) + st.getQuestItemsCount(BUFFALO_MEAT) + st.getQuestItemsCount(ANTELOPE_HORN);
	}
}
