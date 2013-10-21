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

public class _181_DevilsStrikeBackAdventOfBalok extends Quest implements ScriptFile
{
	private static final int CON1 = 33044;
	private static final int CON2 = 29218;
	private static final int CON3 = 17592;
	private static final int CON4 = 17527;
	private static final int CON5 = 17526;
	private static final int CON6 = 34861;
	
	public _181_DevilsStrikeBackAdventOfBalok()
	{
		super(false);
		addStartNpc(CON1);
		addTalkId(CON1);
		addKillId(CON2);
		addQuestItem(CON3);
		addLevelCheck(97, 99);
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
		if (event.equalsIgnoreCase("33044-06.htm"))
		{
			st.setState(STARTED);
			st.setCond(1);
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("reward"))
		{
			st.addExpAndSp(886750000, 414855000);
			st.giveItems(57, 37128000L);
			st.playSound("SOUND_FINISH");
			st.exitCurrentQuest(false);
			int rnd = Rnd.get(2);
			switch (rnd)
			{
				case 0:
					st.giveItems(CON5, 2);
					return "33044-09.htm";
				case 1:
					st.giveItems(CON4, 2);
					return "33044-10.htm";
				case 2:
					st.giveItems(CON6, 2);
					return "33044-11.htm";
			}
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
				if (player.getLevel() < 97)
				{
					htmltext = "33044-02.htm";
					st.exitCurrentQuest(true);
				}
				else
				{
					htmltext = "33044-01.htm";
				}
			}
			if (st.getState() == STARTED)
			{
				if (cond == 1)
				{
					htmltext = "33044-07.htm";
				}
				else
				{
					if (cond == 2)
					{
						htmltext = "33044-08.htm";
					}
				}
			}
			if (st.getState() == COMPLETED)
			{
				htmltext = "33044-03.htm";
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		int npcId = npc.getNpcId();
		int cond = st.getCond();
		Player player = st.getPlayer();
		if (cond == 1)
		{
			if (npcId == CON2)
			{
				if (player.getParty() == null)
				{
					st.setCond(2);
					st.giveItems(CON3, 1);
					st.playSound(SOUND_MIDDLE);
				}
				else
				{
					for (Player pmember : player.getParty().getPartyMembers())
					{
						QuestState pst = pmember.getQuestState("_181_DevilsStrikeBackAdventOfBalok");
						if ((pst != null) && (pst.getCond() == 1))
						{
							pst.setCond(2);
							pst.giveItems(CON3, 1);
							pst.playSound(SOUND_MIDDLE);
						}
					}
				}
			}
		}
		return null;
	}
}
