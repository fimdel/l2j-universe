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

public class _696_ConquertheHallofErosion extends Quest implements ScriptFile
{
	private static final int TEPIOS = 32603;
	private static final int Cohemenes = 25634;
	private static final int MARK_OF_KEUCEREUS_STAGE_1 = 13691;
	private static final int MARK_OF_KEUCEREUS_STAGE_2 = 13692;
	
	public _696_ConquertheHallofErosion()
	{
		super(PARTY_ALL);
		addStartNpc(TEPIOS);
		addKillId(Cohemenes);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		if (event.equalsIgnoreCase("tepios_q696_3.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int npcId = npc.getNpcId();
		Player player = st.getPlayer();
		int cond = st.getCond();
		if (npcId == TEPIOS)
		{
			if (cond == 0)
			{
				if (player.getLevel() >= 75)
				{
					if ((st.getQuestItemsCount(MARK_OF_KEUCEREUS_STAGE_1) > 0) || (st.getQuestItemsCount(MARK_OF_KEUCEREUS_STAGE_2) > 0))
					{
						htmltext = "tepios_q696_1.htm";
					}
					else
					{
						htmltext = "tepios_q696_6.htm";
						st.exitCurrentQuest(true);
					}
				}
				else
				{
					htmltext = "tepios_q696_0.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if (cond == 1)
			{
				if (st.getInt("cohemenesDone") != 0)
				{
					if (st.getQuestItemsCount(MARK_OF_KEUCEREUS_STAGE_2) < 1)
					{
						st.takeAllItems(MARK_OF_KEUCEREUS_STAGE_1);
						st.giveItems(MARK_OF_KEUCEREUS_STAGE_2, 1);
					}
					htmltext = "tepios_q696_5.htm";
					st.playSound(SOUND_FINISH);
					st.exitCurrentQuest(true);
				}
				else
				{
					htmltext = "tepios_q696_1a.htm";
				}
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		if (npc.getNpcId() == Cohemenes)
		{
			st.set("cohemenesDone", 1);
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
