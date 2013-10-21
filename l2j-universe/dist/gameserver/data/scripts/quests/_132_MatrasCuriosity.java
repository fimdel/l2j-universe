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

public class _132_MatrasCuriosity extends Quest implements ScriptFile
{
	private static final int Matras = 32245;
	private static final int Ranku = 25542;
	private static final int Demon_Prince = 25540;
	private static final int Rankus_Blueprint = 9800;
	private static final int Demon_Princes_Blueprint = 9801;
	private static final int Rough_Ore_of_Fire = 10521;
	private static final int Rough_Ore_of_Water = 10522;
	private static final int Rough_Ore_of_Earth = 10523;
	private static final int Rough_Ore_of_Wind = 10524;
	private static final int Rough_Ore_of_Darkness = 10525;
	private static final int Rough_Ore_of_Divinity = 10526;
	
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
	
	public _132_MatrasCuriosity()
	{
		super(PARTY_ALL);
		addStartNpc(Matras);
		addKillId(Ranku);
		addKillId(Demon_Prince);
		addQuestItem(new int[]
		{
			Rankus_Blueprint,
			Demon_Princes_Blueprint
		});
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if (event.equalsIgnoreCase("32245-02.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
			String is_given = st.getPlayer().getVar("q132_Rough_Ore_is_given");
			if (is_given != null)
			{
				htmltext = "32245-02a.htm";
			}
			else
			{
				st.giveItems(Rough_Ore_of_Fire, 1, false);
				st.giveItems(Rough_Ore_of_Water, 1, false);
				st.giveItems(Rough_Ore_of_Earth, 1, false);
				st.giveItems(Rough_Ore_of_Wind, 1, false);
				st.giveItems(Rough_Ore_of_Darkness, 1, false);
				st.giveItems(Rough_Ore_of_Divinity, 1, false);
				st.getPlayer().setVar("q132_Rough_Ore_is_given", "1", -1);
			}
		}
		else if (event.equalsIgnoreCase("32245-04.htm"))
		{
			st.setCond(3);
			st.setState(STARTED);
			st.startQuestTimer("talk_timer", 10000);
		}
		else if (event.equalsIgnoreCase("talk_timer"))
		{
			htmltext = "Matras wishes to talk to you.";
		}
		else if (event.equalsIgnoreCase("get_reward"))
		{
			st.playSound(SOUND_FINISH);
			st.addExpAndSp(5388330, 6048600);
			st.giveItems(ADENA_ID, 575545);
			st.exitCurrentQuest(false);
			return null;
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int npcId = npc.getNpcId();
		int cond = st.getCond();
		if (npcId == Matras)
		{
			if ((cond < 1) && (st.getPlayer().getLevel() >= 78))
			{
				htmltext = "32245-01.htm";
			}
			else if (cond == 1)
			{
				htmltext = "32245-02a.htm";
			}
			else if ((cond == 2) && (st.getQuestItemsCount(Rankus_Blueprint) > 0) && (st.getQuestItemsCount(Demon_Princes_Blueprint) > 0))
			{
				htmltext = "32245-03.htm";
			}
			else if (cond == 3)
			{
				if (st.isRunningQuestTimer("talk_timer"))
				{
					htmltext = "32245-04.htm";
				}
				else
				{
					htmltext = "32245-04a.htm";
				}
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		if (st.getCond() == 1)
		{
			if ((npc.getNpcId() == Ranku) && (st.getQuestItemsCount(Rankus_Blueprint) < 1))
			{
				st.playSound(SOUND_ITEMGET);
				st.playSound(SOUND_MIDDLE);
				st.giveItems(Rankus_Blueprint, 1, false);
			}
			if ((npc.getNpcId() == Demon_Prince) && (st.getQuestItemsCount(Demon_Princes_Blueprint) < 1))
			{
				st.playSound(SOUND_ITEMGET);
				st.playSound(SOUND_MIDDLE);
				st.giveItems(Demon_Princes_Blueprint, 1, false);
			}
			if ((st.getQuestItemsCount(Rankus_Blueprint) > 0) && (st.getQuestItemsCount(Demon_Princes_Blueprint) > 0))
			{
				st.setCond(2);
				st.setState(STARTED);
			}
		}
		return null;
	}
}
