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

public class _453_NotStrongEnough extends Quest implements ScriptFile
{
	private static final int Klemis = 32734;
	public static final String A_MOBS = "a_mobs";
	public static final String B_MOBS = "b_mobs";
	public static final String C_MOBS = "c_mobs";
	public static final String E_MOBS = "e_mobs";
	private static final int[] Rewards =
	{
		//Requiem weapon
		18103,
		18104,
		18105,
		18106,
		18107,
		18108,
		18109,
		18110,
		18111,
		18112,
		18113,
		//Apocalypse weapon
		18137,
		18138,
		18139,
		18140,
		18141,
		18142,
		18143,
		18144,
		18145,
		18146,
		18147,
		//Enchant Scrolls
		17526,
		17527,
		//Attribute
		9546,
		9547,
		9548,
		9549,
		9550,
		9551,
		4343,
		4342,
		4345,
		4344,
		4346,
		4347
	};
	
	public _453_NotStrongEnough()
	{
		super(true);
		addStartNpc(Klemis);
		addKillNpcWithLog(2, A_MOBS, 15, 22746, 22750);
		addKillNpcWithLog(2, B_MOBS, 15, 22747, 22751);
		addKillNpcWithLog(2, C_MOBS, 15, 22748, 22752);
		addKillNpcWithLog(2, E_MOBS, 15, 22749, 22753);
		addKillNpcWithLog(3, A_MOBS, 20, 22754, 22757);
		addKillNpcWithLog(3, B_MOBS, 20, 22755, 22758);
		addKillNpcWithLog(3, C_MOBS, 20, 22756, 22759);
		addKillNpcWithLog(4, A_MOBS, 20, 22760, 22763);
		addKillNpcWithLog(4, B_MOBS, 20, 22761, 22764);
		addKillNpcWithLog(4, C_MOBS, 20, 22762, 22765);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if (event.equalsIgnoreCase("klemis_q453_03.htm"))
		{
			st.setState(STARTED);
			st.setCond(1);
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("bistakon"))
		{
			htmltext = "klemis_q453_05.htm";
			st.setCond(2);
		}
		else if (event.equalsIgnoreCase("reptilicon"))
		{
			htmltext = "klemis_q453_06.htm";
			st.setCond(3);
		}
		else if (event.equalsIgnoreCase("cokrakon"))
		{
			htmltext = "klemis_q453_07.htm";
			st.setCond(4);
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int npcId = npc.getNpcId();
		int cond = st.getCond();
		if (npcId == Klemis)
		{
			switch (st.getState())
			{
				case CREATED:
				{
					QuestState qs = st.getPlayer().getQuestState(_10282_ToTheSeedOfAnnihilation.class);
					if ((st.getPlayer().getLevel() >= 84) && (qs != null) && qs.isCompleted())
					{
						if (st.isNowAvailableByTime())
						{
							htmltext = "klemis_q453_01.htm";
						}
						else
						{
							htmltext = "klemis_q453_00a.htm";
						}
					}
					else
					{
						htmltext = "klemis_q453_00.htm";
					}
					break;
				}
				case STARTED:
				{
					if (cond == 1)
					{
						htmltext = "klemis_q453_03.htm";
					}
					else if (cond == 2)
					{
						htmltext = "klemis_q453_09.htm";
					}
					else if (cond == 3)
					{
						htmltext = "klemis_q453_10.htm";
					}
					else if (cond == 4)
					{
						htmltext = "klemis_q453_11.htm";
					}
					else if (cond == 5)
					{
						htmltext = "klemis_q453_12.htm";
						st.giveItems(Rewards[Rnd.get(Rewards.length)], 1, false);
						st.setState(COMPLETED);
						st.playSound(SOUND_FINISH);
						st.exitCurrentQuest(this);
					}
					break;
				}
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		boolean doneKill = updateKill(npc, st);
		if (doneKill)
		{
			st.unset(A_MOBS);
			st.unset(B_MOBS);
			st.unset(C_MOBS);
			st.unset(E_MOBS);
			st.setCond(5);
		}
		return null;
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
}
