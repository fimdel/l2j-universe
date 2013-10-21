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
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.Util;

public class _509_TheClansPrestige extends Quest implements ScriptFile
{
	private static final int GRAND_MAGISTER_VALDIS = 31331;
	private static final int DAIMONS_EYES = 8489;
	private static final int HESTIAS_FAIRY_STONE = 8490;
	private static final int NUCLEUS_OF_LESSER_GOLEM = 8491;
	private static final int FALSTONS_FANG = 8492;
	private static final int DAIMON_THE_WHITE_EYED = 25290;
	private static final int HESTIA_GUARDIAN_DEITY = 25293;
	private static final int PLAGUE_GOLEM = 25523;
	private static final int DEMONS_AGENT_FALSTON = 25322;
	private static final int[][] REWARDS_LIST =
	{
		{
			0,
			0
		},
		{
			DAIMON_THE_WHITE_EYED,
			DAIMONS_EYES,
			1378
		},
		{
			HESTIA_GUARDIAN_DEITY,
			HESTIAS_FAIRY_STONE,
			1378
		},
		{
			PLAGUE_GOLEM,
			NUCLEUS_OF_LESSER_GOLEM,
			1070
		},
		{
			DEMONS_AGENT_FALSTON,
			FALSTONS_FANG,
			782
		}
	};
	private static final int[][] RADAR =
	{
		{
			0,
			0,
			0
		},
		{
			186304,
			-43744,
			-3193
		},
		{
			134672,
			-115600,
			-1216
		},
		{
			168641,
			-60417,
			-3888
		},
		{
			93296,
			-75104,
			-1824
		}
	};
	
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
	
	public _509_TheClansPrestige()
	{
		super(PARTY_ALL);
		addStartNpc(GRAND_MAGISTER_VALDIS);
		for (int[] i : REWARDS_LIST)
		{
			if (i[0] > 0)
			{
				addKillId(i[0]);
			}
			if (i[1] > 0)
			{
				addQuestItem(i[1]);
			}
		}
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		int cond = st.getCond();
		String htmltext = event;
		if (event.equalsIgnoreCase("31331-0.htm") && (cond == 0))
		{
			st.setCond(1);
			st.setState(STARTED);
		}
		else if (Util.isNumber(event))
		{
			int evt = Integer.parseInt(event);
			st.set("raid", event);
			htmltext = "31331-" + event + ".htm";
			int x = RADAR[evt][0];
			int y = RADAR[evt][1];
			int z = RADAR[evt][2];
			if ((x + y + z) > 0)
			{
				st.addRadar(x, y, z);
			}
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("31331-6.htm"))
		{
			st.playSound(SOUND_FINISH);
			st.exitCurrentQuest(true);
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		Clan clan = st.getPlayer().getClan();
		if (clan == null)
		{
			st.exitCurrentQuest(true);
			htmltext = "31331-0a.htm";
		}
		else if (clan.getLeader().getPlayer() != st.getPlayer())
		{
			st.exitCurrentQuest(true);
			htmltext = "31331-0a.htm";
		}
		else if (clan.getLevel() < 6)
		{
			st.exitCurrentQuest(true);
			htmltext = "31331-0b.htm";
		}
		else
		{
			int cond = st.getCond();
			int raid = st.getInt("raid");
			int id = st.getState();
			if ((id == CREATED) && (cond == 0))
			{
				htmltext = "31331-0c.htm";
			}
			else if ((id == STARTED) && (cond == 1))
			{
				int item = REWARDS_LIST[raid][1];
				long count = st.getQuestItemsCount(item);
				if (count == 0)
				{
					htmltext = "31331-" + raid + "a.htm";
				}
				else if (count == 1)
				{
					htmltext = "31331-" + raid + "b.htm";
					int increasedPoints = clan.incReputation(REWARDS_LIST[raid][2], true, "_509_TheClansPrestige");
					st.getPlayer().sendPacket(new SystemMessage(SystemMessage.YOU_HAVE_SUCCESSFULLY_COMPLETED_A_CLAN_QUEST_S1_POINTS_HAVE_BEEN_ADDED_TO_YOUR_CLAN_REPUTATION_SCORE).addNumber(increasedPoints));
					st.takeItems(item, 1);
				}
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		QuestState id = null;
		Clan clan = st.getPlayer().getClan();
		if (clan == null)
		{
			return null;
		}
		Player clan_leader = clan.getLeader().getPlayer();
		if (clan_leader == null)
		{
			return null;
		}
		if (clan_leader.equals(st.getPlayer()) || (clan_leader.getDistance(npc) <= 1600))
		{
			id = clan_leader.getQuestState(getName());
		}
		if (id == null)
		{
			return null;
		}
		if ((st.getCond() == 1) && (st.getState() == STARTED))
		{
			int raid = REWARDS_LIST[st.getInt("raid")][0];
			int item = REWARDS_LIST[st.getInt("raid")][1];
			int npcId = npc.getNpcId();
			if ((npcId == raid) && (st.getQuestItemsCount(item) == 0))
			{
				st.giveItems(item, 1);
				st.playSound(SOUND_MIDDLE);
			}
		}
		return null;
	}
}
