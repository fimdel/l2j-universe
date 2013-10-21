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

import java.util.StringTokenizer;

import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class _254_LegendaryTales extends Quest implements ScriptFile
{
	private static final int Gilmore = 30754;
	private static final int LargeBone = 17249;
	private static final int[] raids =
	{
		25718,
		25719,
		25720,
		25721,
		25722,
		25723,
		25724
	};
	
	public _254_LegendaryTales()
	{
		super(PARTY_ALL);
		addStartNpc(Gilmore);
		addKillId(raids);
		addQuestItem(LargeBone);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if (event.equalsIgnoreCase("gilmore_q254_05.htm"))
		{
			st.setState(STARTED);
			st.setCond(1);
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.startsWith("gilmore_q254_09.htm"))
		{
			st.takeAllItems(LargeBone);
			StringTokenizer tokenizer = new StringTokenizer(event);
			tokenizer.nextToken();
			switch (Integer.parseInt(tokenizer.nextToken()))
			{
				case 1:
					st.giveItems(13467, 1);
					break;
				case 2:
					st.giveItems(13462, 1);
					break;
				case 3:
					st.giveItems(13464, 1);
					break;
				case 4:
					st.giveItems(13461, 1);
					break;
				case 5:
					st.giveItems(13465, 1);
					break;
				case 6:
					st.giveItems(13463, 1);
					break;
				case 7:
					st.giveItems(13460, 1);
					break;
				case 8:
					st.giveItems(13466, 1);
					break;
				case 9:
					st.giveItems(13459, 1);
					break;
				case 10:
					st.giveItems(13457, 1);
					break;
				case 11:
					st.giveItems(13458, 1);
					break;
				default:
					break;
			}
			st.playSound(SOUND_FINISH);
			st.setState(COMPLETED);
			st.exitCurrentQuest(false);
			htmltext = "gilmore_q254_09.htm";
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int cond = st.getCond();
		if (npc.getNpcId() == Gilmore)
		{
			if (cond == 0)
			{
				if (st.getPlayer().getLevel() >= 80)
				{
					htmltext = "gilmore_q254_01.htm";
				}
				else
				{
					htmltext = "gilmore_q254_00.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if (cond == 1)
			{
				htmltext = "gilmore_q254_06.htm";
			}
			else if (cond == 2)
			{
				htmltext = "gilmore_q254_07.htm";
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		int cond = st.getCond();
		if (cond == 1)
		{
			int mask = 1;
			int var = npc.getNpcId();
			for (int raid : raids)
			{
				if (raid == var)
				{
					break;
				}
				mask = mask << 1;
			}
			var = st.getInt("RaidsKilled");
			if ((var & mask) == 0)
			{
				var |= mask;
				st.set("RaidsKilled", var);
				st.giveItems(LargeBone, 1);
				if (st.getQuestItemsCount(LargeBone) >= 7)
				{
					st.setCond(2);
				}
			}
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
