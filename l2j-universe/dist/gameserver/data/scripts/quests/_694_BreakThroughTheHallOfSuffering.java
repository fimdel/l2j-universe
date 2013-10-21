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

public class _694_BreakThroughTheHallOfSuffering extends Quest implements ScriptFile
{
	private static final int TEPIOS = 32603;
	private static final int MARK_OF_KEUCEREUS_STAGE_1 = 13691;
	private static final int MARK_OF_KEUCEREUS_STAGE_2 = 13692;
	
	public _694_BreakThroughTheHallOfSuffering()
	{
		super(PARTY_ALL);
		addStartNpc(TEPIOS);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		if (event.equalsIgnoreCase("32603-04.htm"))
		{
			qs.setCond(1);
			qs.setState(STARTED);
			qs.playSound(SOUND_ACCEPT);
		}
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int npcId = npc.getNpcId();
		int cond = st.getCond();
		Player player = st.getPlayer();
		if (npcId == TEPIOS)
		{
			if (cond == 0)
			{
				if ((player.getLevel() < 75) || (player.getLevel() > 82))
				{
					if ((st.getQuestItemsCount(MARK_OF_KEUCEREUS_STAGE_1) == 0) && (st.getQuestItemsCount(MARK_OF_KEUCEREUS_STAGE_2) == 0) && (player.getLevel() > 82))
					{
						st.giveItems(MARK_OF_KEUCEREUS_STAGE_1, 1);
					}
					st.exitCurrentQuest(true);
					htmltext = "32603-00.htm";
				}
				else
				{
					htmltext = "32603-01.htm";
				}
			}
			else if (cond == 1)
			{
				htmltext = "32603-05.htm";
			}
		}
		return htmltext;
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
