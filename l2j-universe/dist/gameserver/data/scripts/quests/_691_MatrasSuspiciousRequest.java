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

public class _691_MatrasSuspiciousRequest extends Quest implements ScriptFile
{
	private final static int MATRAS = 32245;
	private final static int LABYRINTH_CAPTAIN = 22368;
	private final static int RED_STONE = 10372;
	private final static int RED_STONES_COUNT = 744;
	private final static int DYNASTIC_ESSENCE_II = 10413;
	
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
	
	public _691_MatrasSuspiciousRequest()
	{
		super(true);
		addStartNpc(MATRAS);
		addQuestItem(RED_STONE);
		addKillId(new int[]
		{
			22363,
			22364,
			22365,
			22366,
			22367,
			LABYRINTH_CAPTAIN,
			22369,
			22370,
			22371,
			22372
		});
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		if (event.equalsIgnoreCase("32245-03.htm"))
		{
			qs.setCond(1);
			qs.setState(STARTED);
			qs.playSound(SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("32245-05.htm"))
		{
			qs.takeItems(RED_STONE, RED_STONES_COUNT);
			qs.giveItems(DYNASTIC_ESSENCE_II, 1, false);
			qs.playSound(SOUND_FINISH);
			qs.exitCurrentQuest(true);
		}
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int cond = st.getCond();
		if (cond == 0)
		{
			if (st.getPlayer().getLevel() >= 76)
			{
				htmltext = "32245-01.htm";
			}
			else
			{
				htmltext = "32245-00.htm";
			}
			st.exitCurrentQuest(true);
		}
		else if (st.getQuestItemsCount(RED_STONE) < RED_STONES_COUNT)
		{
			htmltext = "32245-03.htm";
		}
		else
		{
			htmltext = "32245-04.htm";
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		st.rollAndGive(RED_STONE, 1, 1, RED_STONES_COUNT, npc.getNpcId() == LABYRINTH_CAPTAIN ? 50 : 30);
		return null;
	}
}
