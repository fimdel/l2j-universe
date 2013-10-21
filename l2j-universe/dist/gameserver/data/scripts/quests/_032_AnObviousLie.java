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

import lineage2.commons.util.Rnd;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class _032_AnObviousLie extends Quest implements ScriptFile
{
	int MAXIMILIAN = 30120;
	int GENTLER = 30094;
	int MIKI_THE_CAT = 31706;
	int ALLIGATOR = 20135;
	int CHANCE_FOR_DROP = 30;
	int MAP = 7165;
	int MEDICINAL_HERB = 7166;
	int SPIRIT_ORES = 3031;
	int THREAD = 1868;
	int SUEDE = 1866;
	int RACCOON_EAR = 7680;
	int CAT_EAR = 6843;
	int RABBIT_EAR = 7683;
	
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
	
	public _032_AnObviousLie()
	{
		super(false);
		addStartNpc(MAXIMILIAN);
		addTalkId(MAXIMILIAN);
		addTalkId(GENTLER);
		addTalkId(MIKI_THE_CAT);
		addKillId(ALLIGATOR);
		addQuestItem(MEDICINAL_HERB);
		addQuestItem(MAP);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if (event.equals("30120-1.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equals("30094-1.htm"))
		{
			st.giveItems(MAP, 1);
			st.setCond(2);
		}
		else if (event.equals("31706-1.htm"))
		{
			st.takeItems(MAP, 1);
			st.setCond(3);
		}
		else if (event.equals("30094-4.htm"))
		{
			if (st.getQuestItemsCount(MEDICINAL_HERB) > 19)
			{
				st.takeItems(MEDICINAL_HERB, 20);
				st.setCond(5);
			}
			else
			{
				htmltext = "You don't have enough materials";
				st.setCond(3);
			}
		}
		else if (event.equals("30094-7.htm"))
		{
			if (st.getQuestItemsCount(SPIRIT_ORES) >= 500)
			{
				st.takeItems(SPIRIT_ORES, 500);
				st.setCond(6);
			}
			else
			{
				htmltext = "You don't have enough materials";
			}
		}
		else if (event.equals("31706-4.htm"))
		{
			st.setCond(7);
		}
		else if (event.equals("30094-10.htm"))
		{
			st.setCond(8);
		}
		else if (event.equals("30094-13.htm"))
		{
			if ((st.getQuestItemsCount(THREAD) < 1000) || (st.getQuestItemsCount(SUEDE) < 500))
			{
				htmltext = "You don't have enough materials";
			}
		}
		else if (event.equalsIgnoreCase("cat") || event.equalsIgnoreCase("racoon") || event.equalsIgnoreCase("rabbit"))
		{
			if ((st.getCond() == 8) && (st.getQuestItemsCount(THREAD) >= 1000) && (st.getQuestItemsCount(SUEDE) >= 500))
			{
				st.takeItems(THREAD, 1000);
				st.takeItems(SUEDE, 500);
				if (event.equalsIgnoreCase("cat"))
				{
					st.giveItems(CAT_EAR, 1);
				}
				else if (event.equalsIgnoreCase("racoon"))
				{
					st.giveItems(RACCOON_EAR, 1);
				}
				else if (event.equalsIgnoreCase("rabbit"))
				{
					st.giveItems(RABBIT_EAR, 1);
				}
				st.unset("cond");
				st.playSound(SOUND_FINISH);
				htmltext = "30094-14.htm";
				st.exitCurrentQuest(false);
			}
			else
			{
				htmltext = "You don't have enough materials";
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
		if (npcId == MAXIMILIAN)
		{
			if (cond == 0)
			{
				if (st.getPlayer().getLevel() >= 45)
				{
					htmltext = "30120-0.htm";
				}
				else
				{
					htmltext = "30120-0a.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if (cond == 1)
			{
				htmltext = "30120-2.htm";
			}
		}
		if (npcId == GENTLER)
		{
			if (cond == 1)
			{
				htmltext = "30094-0.htm";
			}
			else if (cond == 2)
			{
				htmltext = "30094-2.htm";
			}
			else if (cond == 3)
			{
				htmltext = "30094-forgot.htm";
			}
			else if (cond == 4)
			{
				htmltext = "30094-3.htm";
			}
			else if ((cond == 5) && (st.getQuestItemsCount(SPIRIT_ORES) < 500))
			{
				htmltext = "30094-5.htm";
			}
			else if ((cond == 5) && (st.getQuestItemsCount(SPIRIT_ORES) >= 500))
			{
				htmltext = "30094-6.htm";
			}
			else if (cond == 6)
			{
				htmltext = "30094-8.htm";
			}
			else if (cond == 7)
			{
				htmltext = "30094-9.htm";
			}
			else if ((cond == 8) && ((st.getQuestItemsCount(THREAD) < 1000) || (st.getQuestItemsCount(SUEDE) < 500)))
			{
				htmltext = "30094-11.htm";
			}
			else if ((cond == 8) && (st.getQuestItemsCount(THREAD) >= 1000) && (st.getQuestItemsCount(SUEDE) >= 500))
			{
				htmltext = "30094-12.htm";
			}
		}
		if (npcId == MIKI_THE_CAT)
		{
			if (cond == 2)
			{
				htmltext = "31706-0.htm";
			}
			else if (cond == 3)
			{
				htmltext = "31706-2.htm";
			}
			else if (cond == 6)
			{
				htmltext = "31706-3.htm";
			}
			else if (cond == 7)
			{
				htmltext = "31706-5.htm";
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		long count = st.getQuestItemsCount(MEDICINAL_HERB);
		if (Rnd.chance(CHANCE_FOR_DROP) && (st.getCond() == 3))
		{
			if (count < 20)
			{
				st.giveItems(MEDICINAL_HERB, 1);
				if (count == 19)
				{
					st.playSound(SOUND_MIDDLE);
					st.setCond(4);
				}
				else
				{
					st.playSound(SOUND_ITEMGET);
				}
			}
		}
		return null;
	}
}
