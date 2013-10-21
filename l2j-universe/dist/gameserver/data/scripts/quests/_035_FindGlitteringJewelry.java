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

public class _035_FindGlitteringJewelry extends Quest implements ScriptFile
{
	int ROUGH_JEWEL = 7162;
	int ORIHARUKON = 1893;
	int SILVER_NUGGET = 1873;
	int THONS = 4044;
	int JEWEL_BOX = 7077;
	
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
	
	public _035_FindGlitteringJewelry()
	{
		super(false);
		addStartNpc(30091);
		addTalkId(30091);
		addTalkId(30879);
		addKillId(20135);
		addQuestItem(ROUGH_JEWEL);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		int cond = st.getCond();
		if (event.equals("30091-1.htm") && (cond == 0))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equals("30879-1.htm") && (cond == 1))
		{
			st.setCond(2);
		}
		else if (event.equals("30091-3.htm") && (cond == 3))
		{
			if (st.getQuestItemsCount(ROUGH_JEWEL) == 10)
			{
				st.takeItems(ROUGH_JEWEL, -1);
				st.setCond(4);
			}
			else
			{
				htmltext = "30091-hvnore.htm";
			}
		}
		else if (event.equals("30091-5.htm") && (cond == 4))
		{
			if ((st.getQuestItemsCount(ORIHARUKON) >= 5) && (st.getQuestItemsCount(SILVER_NUGGET) >= 500) && (st.getQuestItemsCount(THONS) >= 150))
			{
				st.takeItems(ORIHARUKON, 5);
				st.takeItems(SILVER_NUGGET, 500);
				st.takeItems(THONS, 150);
				st.giveItems(JEWEL_BOX, 1);
				st.playSound(SOUND_FINISH);
				st.exitCurrentQuest(true);
			}
			else
			{
				htmltext = "30091-hvnmat-bug.htm";
			}
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int npcId = npc.getNpcId();
		int cond = st.getCond();
		if (npcId == 30091)
		{
			if ((cond == 0) && (st.getQuestItemsCount(JEWEL_BOX) == 0))
			{
				if (st.getPlayer().getLevel() >= 60)
				{
					QuestState fwear = st.getPlayer().getQuestState(_037_PleaseMakeMeFormalWear.class);
					if ((fwear != null) && (fwear.getCond() == 6))
					{
						htmltext = "30091-0.htm";
					}
					else
					{
						st.exitCurrentQuest(true);
					}
				}
				else
				{
					htmltext = "30091-6.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if (cond == 1)
			{
				htmltext = "30091-1r.htm";
			}
			else if (cond == 2)
			{
				htmltext = "30091-1r2.htm";
			}
			else if ((cond == 3) && (st.getQuestItemsCount(ROUGH_JEWEL) == 10))
			{
				htmltext = "30091-2.htm";
			}
			else if ((cond == 4) && ((st.getQuestItemsCount(ORIHARUKON) < 5) || (st.getQuestItemsCount(SILVER_NUGGET) < 500) || (st.getQuestItemsCount(THONS) < 150)))
			{
				htmltext = "30091-hvnmat.htm";
			}
			else if ((cond == 4) && (st.getQuestItemsCount(ORIHARUKON) >= 5) && (st.getQuestItemsCount(SILVER_NUGGET) >= 500) && (st.getQuestItemsCount(THONS) >= 150))
			{
				htmltext = "30091-4.htm";
			}
		}
		else if (npcId == 30879)
		{
			if (cond == 1)
			{
				htmltext = "30879-0.htm";
			}
			else if (cond == 2)
			{
				htmltext = "30879-1r.htm";
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		long count = st.getQuestItemsCount(ROUGH_JEWEL);
		if (count < 10)
		{
			st.giveItems(ROUGH_JEWEL, 1);
			if (st.getQuestItemsCount(ROUGH_JEWEL) == 10)
			{
				st.playSound(SOUND_MIDDLE);
				st.setCond(3);
			}
			else
			{
				st.playSound(SOUND_ITEMGET);
			}
		}
		return null;
	}
}
