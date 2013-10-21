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

import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class _113_StatusOfTheBeaconTower extends Quest implements ScriptFile
{
	private static final int MOIRA = 31979;
	private static final int TORRANT = 32016;
	private static final int BOX = 8086;
	
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
	
	public _113_StatusOfTheBeaconTower()
	{
		super(false);
		addStartNpc(MOIRA);
		addTalkId(TORRANT);
		addQuestItem(BOX);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if (event.equalsIgnoreCase("seer_moirase_q0113_0104.htm"))
		{
			st.setCond(1);
			st.giveItems(BOX, 1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("torant_q0113_0201.htm"))
		{
			st.giveItems(ADENA_ID, 247600);
			st.addExpAndSp(1147830, 1352735);
			st.takeItems(BOX, 1);
			st.playSound(SOUND_FINISH);
			st.exitCurrentQuest(false);
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int npcId = npc.getNpcId();
		int id = st.getState();
		int cond = st.getCond();
		if (id == COMPLETED)
		{
			htmltext = "completed";
		}
		else if (npcId == MOIRA)
		{
			if (id == CREATED)
			{
				if (st.getPlayer().getLevel() >= 40)
				{
					htmltext = "seer_moirase_q0113_0101.htm";
				}
				else
				{
					htmltext = "seer_moirase_q0113_0103.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if (cond == 1)
			{
				htmltext = "seer_moirase_q0113_0105.htm";
			}
		}
		else if ((npcId == TORRANT) && (st.getQuestItemsCount(BOX) == 1))
		{
			htmltext = "torant_q0113_0101.htm";
		}
		return htmltext;
	}
}
