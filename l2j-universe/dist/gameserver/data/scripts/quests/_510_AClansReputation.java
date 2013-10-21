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

public class _510_AClansReputation extends Quest implements ScriptFile
{
	private static final int VALDIS = 31331;
	private static final int CLAW = 8767;
	private static final int CLAN_POINTS_REWARD = 30;
	
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
	
	public _510_AClansReputation()
	{
		super(PARTY_ALL);
		addStartNpc(VALDIS);
		for (int npc = 22215; npc <= 22217; npc++)
		{
			addKillId(npc);
		}
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		int cond = st.getCond();
		String htmltext = event;
		if (event.equals("31331-3.htm"))
		{
			if (cond == 0)
			{
				st.setCond(1);
				st.setState(STARTED);
			}
		}
		else if (event.equals("31331-6.htm"))
		{
			st.playSound(SOUND_FINISH);
			st.exitCurrentQuest(true);
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "<html><body>You are either not on a quest that involves this NPC, or you don't meet this NPC's minimum quest requirements.</body></html>";
		Player player = st.getPlayer();
		Clan clan = player.getClan();
		if ((player.getClan() == null) || !player.isClanLeader())
		{
			st.exitCurrentQuest(true);
			htmltext = "31331-0.htm";
		}
		else if (player.getClan().getLevel() < 5)
		{
			st.exitCurrentQuest(true);
			htmltext = "31331-0.htm";
		}
		else
		{
			int cond = st.getCond();
			int id = st.getState();
			if ((id == CREATED) && (cond == 0))
			{
				htmltext = "31331-1.htm";
			}
			else if ((id == STARTED) && (cond == 1))
			{
				long count = st.getQuestItemsCount(CLAW);
				if (count == 0)
				{
					htmltext = "31331-4.htm";
				}
				else if (count >= 1)
				{
					htmltext = "31331-7.htm";
					st.takeItems(CLAW, -1);
					int pointsCount = CLAN_POINTS_REWARD * (int) count;
					if (count > 10)
					{
						pointsCount += (count % 10) * 118;
					}
					int increasedPoints = clan.incReputation(pointsCount, true, "_510_AClansReputation");
					player.sendPacket(new SystemMessage(SystemMessage.YOU_HAVE_SUCCESSFULLY_COMPLETED_A_CLAN_QUEST_S1_POINTS_HAVE_BEEN_ADDED_TO_YOUR_CLAN_REPUTATION_SCORE).addNumber(increasedPoints));
				}
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		if (!st.getPlayer().isClanLeader())
		{
			st.exitCurrentQuest(true);
		}
		else if (st.getState() == STARTED)
		{
			int npcId = npc.getNpcId();
			if ((npcId >= 22215) && (npcId <= 22218))
			{
				st.giveItems(CLAW, 1);
				st.playSound(SOUND_ITEMGET);
			}
		}
		return null;
	}
}
