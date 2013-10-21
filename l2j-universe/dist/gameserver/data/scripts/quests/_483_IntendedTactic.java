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

import org.apache.commons.lang3.ArrayUtils;

public class _483_IntendedTactic extends Quest implements ScriptFile
{
	private static final int CON1 = 33357;
	private static final int CON2 = 17736;
	private static final int CON3 = 17737;
	private static final int CON4 = 17624;
	private static final int[] CON5 =
	{
		23069,
		23070,
		23073,
		23071,
		23072,
		23074,
		23075
	};
	private static final int[] CON6 =
	{
		25811,
		25812,
		25815,
		25809
	};
	
	public _483_IntendedTactic()
	{
		super(false);
		addStartNpc(CON1);
		addTalkId(CON1);
		addKillId(CON5);
		addKillId(CON6);
		addQuestItem(CON3, CON2);
		addLevelCheck(48, 99);
	}
	
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
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if (event.equalsIgnoreCase("33357-08.htm"))
		{
			st.setState(STARTED);
			st.setCond(1);
			st.playSound(SOUND_ACCEPT);
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		Player player = st.getPlayer();
		String htmltext = "noquest";
		int npcId = npc.getNpcId();
		int cond = st.getCond();
		if (npcId == CON1)
		{
			if (st.getState() == CREATED)
			{
				if (player.getLevel() >= 48)
				{
					htmltext = "33357-01.htm";
				}
				else
				{
					htmltext = "33357-02.htm";
					st.exitCurrentQuest(true);
				}
			}
			if (st.getState() == STARTED)
			{
				if (cond == 1)
				{
					htmltext = "33357-09.htm";
				}
				else
				{
					if (cond == 2)
					{
						if ((st.getQuestItemsCount(CON2) >= 10) && (st.getQuestItemsCount(CON3) >= 1))
						{
							htmltext = "33357-12.htm";
							st.addExpAndSp(1500000, 1250000);
							st.giveItems(CON4, 1);
							st.exitCurrentQuest(false);
						}
						else
						{
							if (st.getQuestItemsCount(CON2) >= 10)
							{
								htmltext = "33357-11.htm";
								st.addExpAndSp(1500000, 1250000);
								st.exitCurrentQuest(false);
							}
						}
					}
				}
			}
			if (st.getState() == COMPLETED)
			{
				htmltext = "33357-03.htm";
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		npc.getNpcId();
		int cond = st.getCond();
		if (cond == 1)
		{
			if ((ArrayUtils.contains(CON5, npc.getNpcId())) && (Rnd.chance(25)))
			{
				st.giveItems(CON2, 1);
				st.playSound("SOUND_ITEMGET");
				if (st.getQuestItemsCount(CON2) >= 10L)
				{
					st.setCond(2);
					st.playSound("SOUND_MIDDLE");
				}
			}
			else if (ArrayUtils.contains(CON6, npc.getNpcId()))
			{
				if (st.getQuestItemsCount(CON3) <= 0)
				{
					st.giveItems(CON3, 1);
					st.playSound("SOUND_ITEMGET");
				}
			}
		}
		else if (cond == 2)
		{
			if (ArrayUtils.contains(CON6, npc.getNpcId()))
			{
				if (st.getQuestItemsCount(CON3) <= 0)
				{
					st.giveItems(CON3, 1);
					st.playSound("SOUND_ITEMGET");
				}
			}
		}
		return null;
	}
}
