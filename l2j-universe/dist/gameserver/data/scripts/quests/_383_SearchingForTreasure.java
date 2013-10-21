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

import java.util.ArrayList;
import java.util.List;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class _383_SearchingForTreasure extends Quest implements ScriptFile
{
	private static final int PIRATES_TREASURE_MAP = 5915;
	private static final int SHARK = 20314;
	private static final int ESPEN = 30890;
	private static final int PIRATES_CHEST = 31148;
	
	private class rewardInfo
	{
		public int id, count, chance;
		
		public rewardInfo(int _id, int _count, int _chance)
		{
			id = _id;
			count = _count;
			chance = _chance;
		}
	}
	
	private static List<rewardInfo> rewards = new ArrayList<>();
	
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
	
	public _383_SearchingForTreasure()
	{
		super(false);
		addStartNpc(ESPEN);
		addTalkId(PIRATES_CHEST);
		addQuestItem(PIRATES_TREASURE_MAP);
		rewards.add(new rewardInfo(952, 1, 8));
		rewards.add(new rewardInfo(956, 1, 15));
		rewards.add(new rewardInfo(1337, 1, 130));
		rewards.add(new rewardInfo(1338, 2, 150));
		rewards.add(new rewardInfo(2450, 1, 2));
		rewards.add(new rewardInfo(2451, 1, 2));
		rewards.add(new rewardInfo(3452, 1, 140));
		rewards.add(new rewardInfo(3455, 1, 120));
		rewards.add(new rewardInfo(4408, 1, 220));
		rewards.add(new rewardInfo(4409, 1, 220));
		rewards.add(new rewardInfo(4418, 1, 220));
		rewards.add(new rewardInfo(4419, 1, 220));
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if (event.equalsIgnoreCase("30890-03.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
		}
		else if (event.equalsIgnoreCase("30890-07.htm"))
		{
			if (st.getQuestItemsCount(PIRATES_TREASURE_MAP) > 0)
			{
				st.setCond(2);
				st.takeItems(PIRATES_TREASURE_MAP, 1);
				st.addSpawn(PIRATES_CHEST, 106583, 197747, -4209, 900000);
				st.addSpawn(SHARK, 106570, 197740, -4209, 900000);
				st.addSpawn(SHARK, 106580, 197747, -4209, 900000);
				st.addSpawn(SHARK, 106590, 197743, -4209, 900000);
				st.playSound(SOUND_ACCEPT);
			}
			else
			{
				htmltext = "You don't have required items";
				st.exitCurrentQuest(true);
			}
		}
		else if (event.equalsIgnoreCase("30890-02b.htm"))
		{
			if (st.getQuestItemsCount(PIRATES_TREASURE_MAP) > 0)
			{
				st.giveItems(ADENA_ID, 1000);
				st.playSound("ItemSound.quest_finish");
			}
			else
			{
				htmltext = "You don't have required items";
			}
			st.exitCurrentQuest(true);
		}
		else if (event.equalsIgnoreCase("31148-02.htm"))
		{
			if (st.getQuestItemsCount(1661) > 0)
			{
				st.takeItems(1661, 1);
				st.giveItems(ADENA_ID, 500 + (Rnd.get(5) * 300));
				int count = 0;
				while (count < 1)
				{
					for (rewardInfo reward : rewards)
					{
						int id = reward.id;
						int qty = reward.count;
						int chance = reward.chance;
						if ((Rnd.get(1000) < chance) && (count < 2))
						{
							st.giveItems(id, Rnd.get(qty) + 1);
							count += 1;
						}
						if (count < 2)
						{
							for (int i = 4481; i <= 4505; i++)
							{
								if ((Rnd.get(500) == 1) && (count < 2))
								{
									st.giveItems(i, 1);
									count += 1;
								}
							}
						}
					}
				}
				st.playSound("ItemSound.quest_finish");
				st.exitCurrentQuest(true);
			}
			else
			{
				htmltext = "31148-03.htm";
			}
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int npcId = npc.getNpcId();
		if (st.getState() == CREATED)
		{
			if (st.getPlayer().getLevel() >= 42)
			{
				if (st.getQuestItemsCount(PIRATES_TREASURE_MAP) > 0)
				{
					htmltext = "30890-01.htm";
				}
				else
				{
					htmltext = "30890-00.htm";
					st.exitCurrentQuest(true);
				}
			}
			else
			{
				htmltext = "30890-01a.htm";
				st.exitCurrentQuest(true);
			}
		}
		else if (npcId == ESPEN)
		{
			htmltext = "30890-03a.htm";
		}
		else if ((npcId == PIRATES_CHEST) && (st.getCond() == 2) && (st.getState() == STARTED))
		{
			htmltext = "31148-01.htm";
		}
		return htmltext;
	}
}
