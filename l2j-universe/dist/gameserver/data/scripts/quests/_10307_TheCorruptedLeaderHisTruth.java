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

public class _10307_TheCorruptedLeaderHisTruth extends Quest implements ScriptFile
{
	private static final int NPC_NAOMI_KASHERON = 32896;
	private static final int NPC_MIMILEAD = 32895;
	private static final int[] MOB_KIMERIAN =
	{
		25745,
		25747
	};
	private static final int REWARD_ENCHANT_ARMOR_R = 17527;
	
	public _10307_TheCorruptedLeaderHisTruth()
	{
		super(false);
		addStartNpc(NPC_NAOMI_KASHERON);
		addTalkId(NPC_MIMILEAD);
		addKillId(MOB_KIMERIAN);
		addQuestCompletedCheck(_10306_TheCorruptedLeader.class);
		addLevelCheck(90, 99);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		if (st == null)
		{
			return "noquest";
		}
		if (event.equalsIgnoreCase("32896-05.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("32896-08.htm"))
		{
			st.setCond(3);
			st.playSound(SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("32896-08.htm"))
		{
			st.playSound(SOUND_FINISH);
			st.addExpAndSp(11779522, 5275253);
			st.giveItems(REWARD_ENCHANT_ARMOR_R, 1);
			st.exitCurrentQuest(false);
		}
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		if (st == null)
		{
			return htmltext;
		}
		Player player = st.getPlayer();
		QuestState prevst = player.getQuestState(_10306_TheCorruptedLeader.class);
		if (npc.getNpcId() == NPC_NAOMI_KASHERON)
		{
			switch (st.getState())
			{
				case COMPLETED:
					htmltext = "32896-02.htm";
					break;
				case CREATED:
					if (player.getLevel() >= 90)
					{
						if ((prevst != null) && (prevst.isCompleted()))
						{
							htmltext = "32896-01.htm";
						}
						else
						{
							st.exitCurrentQuest(true);
							htmltext = "32896-03.htm";
						}
					}
					else
					{
						st.exitCurrentQuest(true);
						htmltext = "32896-03.htm";
					}
					break;
				case STARTED:
					if (st.getCond() == 1)
					{
						htmltext = "32896-05.htm";
					}
					else
					{
						if (st.getCond() != 2)
						{
							break;
						}
						htmltext = "32896-06.htm";
					}
			}
		}
		else if (npc.getNpcId() == NPC_MIMILEAD)
		{
			if (st.isStarted())
			{
				if (st.getCond() == 3)
				{
					htmltext = "32895-01.htm";
				}
			}
			else if (st.isCompleted())
			{
				htmltext = "32895-05.htm";
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		if ((npc == null) || (st == null) || (st.getCond() != 1))
		{
			return null;
		}
		if (ArrayUtils.contains(MOB_KIMERIAN, npc.getNpcId()))
		{
			if (st.getCond() == 1)
			{
				st.playSound(SOUND_MIDDLE);
				st.setCond(2);
			}
		}
		return null;
	}
	
	@Override
	public boolean isVisible(Player player)
	{
		QuestState qs = player.getQuestState(_10307_TheCorruptedLeaderHisTruth.class);
		return ((qs == null) && isAvailableFor(player)) || ((qs != null) && qs.isNowAvailableByTime());
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
