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

import java.util.List;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.Config;
import lineage2.gameserver.ai.CtrlIntention;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class _454_CompletelyLost extends Quest implements ScriptFile
{
	private static final int WoundedSoldier = 32738;
	private static final int Ermian = 32736;
	private static final int[][] rewards =
	{
		{
			15792,
			1
		},
		{
			15798,
			1
		},
		{
			15795,
			1
		},
		{
			15801,
			1
		},
		{
			15808,
			1
		},
		{
			15804,
			1
		},
		{
			15809,
			1
		},
		{
			15810,
			1
		},
		{
			15811,
			1
		},
		{
			15660,
			3
		},
		{
			15666,
			3
		},
		{
			15663,
			3
		},
		{
			15667,
			3
		},
		{
			15669,
			3
		},
		{
			15668,
			3
		},
		{
			15769,
			3
		},
		{
			15770,
			3
		},
		{
			15771,
			3
		},
		{
			15805,
			1
		},
		{
			15796,
			1
		},
		{
			15793,
			1
		},
		{
			15799,
			1
		},
		{
			15802,
			1
		},
		{
			15809,
			1
		},
		{
			15810,
			1
		},
		{
			15811,
			1
		},
		{
			15672,
			3
		},
		{
			15664,
			3
		},
		{
			15661,
			3
		},
		{
			15670,
			3
		},
		{
			15671,
			3
		},
		{
			15769,
			3
		},
		{
			15770,
			3
		},
		{
			15771,
			3
		},
		{
			15800,
			1
		},
		{
			15803,
			1
		},
		{
			15806,
			1
		},
		{
			15807,
			1
		},
		{
			15797,
			1
		},
		{
			15794,
			1
		},
		{
			15809,
			1
		},
		{
			15810,
			1
		},
		{
			15811,
			1
		},
		{
			15673,
			3
		},
		{
			15674,
			3
		},
		{
			15675,
			3
		},
		{
			15691,
			3
		},
		{
			15665,
			3
		},
		{
			15662,
			3
		},
		{
			15769,
			3
		},
		{
			15770,
			3
		},
		{
			15771,
			3
		}
	};
	
	public _454_CompletelyLost()
	{
		super(PARTY_ALL);
		addStartNpc(WoundedSoldier);
		addTalkId(Ermian);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		if (event.equalsIgnoreCase("wounded_soldier_q454_02.htm"))
		{
			st.setState(STARTED);
			st.setCond(1);
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("wounded_soldier_q454_03.htm"))
		{
			if (seeSoldier(npc, st.getPlayer()) == null)
			{
				npc.setFollowTarget(st.getPlayer());
				npc.getAI().setIntention(CtrlIntention.AI_INTENTION_FOLLOW, st.getPlayer(), Config.FOLLOW_RANGE);
			}
		}
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		if (npc.getNpcId() == WoundedSoldier)
		{
			switch (st.getState())
			{
				case CREATED:
					if (st.isNowAvailableByTime())
					{
						if (st.getPlayer().getLevel() >= 84)
						{
							htmltext = "wounded_soldier_q454_01.htm";
						}
						else
						{
							htmltext = "wounded_soldier_q454_00.htm";
							st.exitCurrentQuest(true);
						}
					}
					else
					{
						htmltext = "wounded_soldier_q454_00a.htm";
					}
					break;
				case STARTED:
					if (st.getCond() == 1)
					{
						htmltext = "wounded_soldier_q454_04.htm";
						if (seeSoldier(npc, st.getPlayer()) == null)
						{
							npc.setFollowTarget(st.getPlayer());
							npc.getAI().setIntention(CtrlIntention.AI_INTENTION_FOLLOW, st.getPlayer(), Config.FOLLOW_RANGE);
						}
					}
					break;
			}
		}
		else if (npc.getNpcId() == Ermian)
		{
			if (st.getCond() == 1)
			{
				if (seeSoldier(npc, st.getPlayer()) != null)
				{
					htmltext = "ermian_q454_01.htm";
					NpcInstance soldier = seeSoldier(npc, st.getPlayer());
					soldier.doDie(null);
					soldier.endDecayTask();
					giveReward(st);
					st.setState(COMPLETED);
					st.playSound(SOUND_FINISH);
					st.exitCurrentQuest(this);
				}
				else
				{
					htmltext = "ermian_q454_02.htm";
				}
			}
		}
		return htmltext;
	}
	
	private NpcInstance seeSoldier(NpcInstance npc, Player player)
	{
		List<NpcInstance> around = npc.getAroundNpc(Config.FOLLOW_RANGE * 2, 300);
		if ((around != null) && !around.isEmpty())
		{
			for (NpcInstance n : around)
			{
				if ((n.getNpcId() == WoundedSoldier) && (n.getFollowTarget() != null))
				{
					if (n.getFollowTarget().getObjectId() == player.getObjectId())
					{
						return n;
					}
				}
			}
		}
		return null;
	}
	
	private void giveReward(QuestState st)
	{
		int row = Rnd.get(rewards.length);
		int id = rewards[row][0];
		int count = rewards[row][1];
		st.giveItems(id, count);
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
