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
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class _139_ShadowFoxPart1 extends Quest implements ScriptFile
{
	private final static int MIA = 30896;
	private final static int FRAGMENT = 10345;
	private final static int CHEST = 10346;
	private final static int TasabaLizardman1 = 20784;
	private final static int TasabaLizardman2 = 21639;
	private final static int TasabaLizardmanShaman1 = 20785;
	private final static int TasabaLizardmanShaman2 = 21640;
	
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
	
	public _139_ShadowFoxPart1()
	{
		super(false);
		addFirstTalkId(MIA);
		addTalkId(MIA);
		addQuestItem(FRAGMENT, CHEST);
		addKillId(TasabaLizardman1, TasabaLizardman2, TasabaLizardmanShaman1, TasabaLizardmanShaman2);
	}
	
	@Override
	public String onFirstTalk(NpcInstance npc, Player player)
	{
		QuestState qs = player.getQuestState(_138_TempleChampionPart2.class);
		if ((qs != null) && qs.isCompleted() && (player.getQuestState(getClass()) == null))
		{
			newQuestState(player, STARTED);
		}
		return "";
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		if (event.equalsIgnoreCase("30896-03.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("30896-11.htm"))
		{
			st.setCond(2);
			st.setState(STARTED);
			st.playSound(SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("30896-14.htm"))
		{
			st.takeItems(FRAGMENT, -1);
			st.takeItems(CHEST, -1);
			st.set("talk", "1");
		}
		else if (event.equalsIgnoreCase("30896-16.htm"))
		{
			st.playSound(SOUND_FINISH);
			st.giveItems(ADENA_ID, 14050);
			st.exitCurrentQuest(false);
		}
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int cond = st.getCond();
		int npcId = npc.getNpcId();
		if (npcId == MIA)
		{
			if (cond == 0)
			{
				if (st.getPlayer().getLevel() >= 37)
				{
					htmltext = "30896-01.htm";
				}
				else
				{
					htmltext = "30896-00.htm";
				}
			}
			else if (cond == 1)
			{
				htmltext = "30896-03.htm";
			}
			else if (cond == 2)
			{
				if ((st.getQuestItemsCount(FRAGMENT) >= 10) && (st.getQuestItemsCount(CHEST) >= 1))
				{
					htmltext = "30896-13.htm";
				}
				else if (st.getInt("talk") == 1)
				{
					htmltext = "30896-14.htm";
				}
				else
				{
					htmltext = "30896-12.htm";
				}
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		int cond = st.getCond();
		if (cond == 2)
		{
			st.giveItems(FRAGMENT, 1);
			st.playSound(SOUND_ITEMGET);
			if (Rnd.chance(10))
			{
				st.giveItems(CHEST, 1);
			}
		}
		return null;
	}
}
