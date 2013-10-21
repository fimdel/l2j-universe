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

public class _10291_FireDragonDestroyer extends Quest implements ScriptFile
{
	private static final int Klein = 31540;
	private static final int PoorNecklace = 15524;
	private static final int ValorNecklace = 15525;
	private static final int Valakas = 29028;
	
	public _10291_FireDragonDestroyer()
	{
		super(PARTY_ALL);
		addStartNpc(Klein);
		addQuestItem(PoorNecklace, ValorNecklace);
		addKillId(Valakas);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if (event.equalsIgnoreCase("klein_q10291_04.htm"))
		{
			st.setState(STARTED);
			st.setCond(1);
			st.playSound(SOUND_ACCEPT);
			st.giveItems(PoorNecklace, 1);
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int npcId = npc.getNpcId();
		int cond = st.getCond();
		if (npcId == Klein)
		{
			if (cond == 0)
			{
				if ((st.getPlayer().getLevel() >= 83) && (st.getQuestItemsCount(7267) >= 1))
				{
					htmltext = "klein_q10291_01.htm";
				}
				else if (st.getQuestItemsCount(7267) < 1)
				{
					htmltext = "klein_q10291_00a.htm";
				}
				else
				{
					htmltext = "klein_q10291_00.htm";
				}
			}
			else if (cond == 1)
			{
				htmltext = "klein_q10291_05.htm";
			}
			else if (cond == 2)
			{
				if (st.getQuestItemsCount(ValorNecklace) >= 1)
				{
					htmltext = "klein_q10291_07.htm";
					st.takeAllItems(ValorNecklace);
					st.giveItems(8567, 1);
					st.giveItems(ADENA_ID, 126549);
					st.addExpAndSp(717291, 77397);
					st.playSound(SOUND_FINISH);
					st.setState(COMPLETED);
					st.exitCurrentQuest(false);
				}
				else
				{
					htmltext = "klein_q10291_06.htm";
				}
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		int npcId = npc.getNpcId();
		int cond = st.getCond();
		if ((cond == 1) && (npcId == Valakas))
		{
			st.takeAllItems(PoorNecklace);
			st.giveItems(ValorNecklace, 1);
			st.setCond(2);
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
