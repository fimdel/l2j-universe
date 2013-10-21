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

public class _033_MakeAPairOfDressShoes extends Quest implements ScriptFile
{
	int LEATHER = 1882;
	int THREAD = 1868;
	int DRESS_SHOES_BOX = 7113;
	
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
	
	public _033_MakeAPairOfDressShoes()
	{
		super(false);
		addStartNpc(30838);
		addTalkId(30838);
		addTalkId(30838);
		addTalkId(30164);
		addTalkId(31520);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if (event.equals("30838-1.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equals("31520-1.htm"))
		{
			st.setCond(2);
		}
		else if (event.equals("30838-3.htm"))
		{
			st.setCond(3);
		}
		else if (event.equals("30838-5.htm"))
		{
			if ((st.getQuestItemsCount(LEATHER) >= 200) && (st.getQuestItemsCount(THREAD) >= 600) && (st.getQuestItemsCount(ADENA_ID) >= 200000))
			{
				st.takeItems(LEATHER, 200);
				st.takeItems(THREAD, 600);
				st.takeItems(ADENA_ID, 200000);
				st.setCond(4);
			}
			else
			{
				htmltext = "You don't have enough materials";
			}
		}
		else if (event.equals("30164-1.htm"))
		{
			if (st.getQuestItemsCount(ADENA_ID) >= 300000)
			{
				st.takeItems(ADENA_ID, 300000);
				st.setCond(5);
			}
			else
			{
				htmltext = "30164-havent.htm";
			}
		}
		else if (event.equals("30838-7.htm"))
		{
			st.giveItems(DRESS_SHOES_BOX, 1);
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
		int cond = st.getCond();
		if (npcId == 30838)
		{
			if ((cond == 0) && (st.getQuestItemsCount(DRESS_SHOES_BOX) == 0))
			{
				if (st.getPlayer().getLevel() >= 60)
				{
					QuestState fwear = st.getPlayer().getQuestState(_037_PleaseMakeMeFormalWear.class);
					if ((fwear != null) && (fwear.getCond() == 7))
					{
						htmltext = "30838-0.htm";
					}
					else
					{
						st.exitCurrentQuest(true);
					}
				}
				else
				{
					htmltext = "30838-00.htm";
				}
			}
			else if (cond == 1)
			{
				htmltext = "30838-1.htm";
			}
			else if (cond == 2)
			{
				htmltext = "30838-2.htm";
			}
			else if ((cond == 3) && (st.getQuestItemsCount(LEATHER) >= 200) && (st.getQuestItemsCount(THREAD) >= 600) && (st.getQuestItemsCount(ADENA_ID) >= 200000))
			{
				htmltext = "30838-4.htm";
			}
			else if ((cond == 3) && ((st.getQuestItemsCount(LEATHER) < 200) || (st.getQuestItemsCount(THREAD) < 600) || (st.getQuestItemsCount(ADENA_ID) < 200000)))
			{
				htmltext = "30838-4r.htm";
			}
			else if (cond == 4)
			{
				htmltext = "30838-5r.htm";
			}
			else if (cond == 5)
			{
				htmltext = "30838-6.htm";
			}
		}
		else if (npcId == 31520)
		{
			if (cond == 1)
			{
				htmltext = "31520-0.htm";
			}
			else if (cond == 2)
			{
				htmltext = "31520-1r.htm";
			}
		}
		else if (npcId == 30164)
		{
			if (cond == 4)
			{
				htmltext = "30164-0.htm";
			}
			else if (cond == 5)
			{
				htmltext = "30164-2.htm";
			}
		}
		return htmltext;
	}
}
