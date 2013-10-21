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

public class _122_OminousNews extends Quest implements ScriptFile
{
	int MOIRA = 31979;
	int KARUDA = 32017;
	
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
	
	public _122_OminousNews()
	{
		super(false);
		addStartNpc(MOIRA);
		addTalkId(KARUDA);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		int cond = st.getCond();
		htmltext = event;
		if (htmltext.equalsIgnoreCase("seer_moirase_q0122_0104.htm") && (cond == 0))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		else if (htmltext.equalsIgnoreCase("karuda_q0122_0201.htm"))
		{
			if (cond == 1)
			{
				st.giveItems(ADENA_ID, 8923);
				st.addExpAndSp(45151, 2310);
				st.playSound(SOUND_FINISH);
				st.exitCurrentQuest(false);
			}
			else
			{
				htmltext = "noquest";
			}
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		int npcId = npc.getNpcId();
		String htmltext = "noquest";
		int cond = st.getCond();
		if (npcId == MOIRA)
		{
			if (cond == 0)
			{
				if (st.getPlayer().getLevel() >= 20)
				{
					htmltext = "seer_moirase_q0122_0101.htm";
				}
				else
				{
					htmltext = "seer_moirase_q0122_0103.htm";
					st.exitCurrentQuest(true);
				}
			}
			else
			{
				htmltext = "seer_moirase_q0122_0104.htm";
			}
		}
		else if ((npcId == KARUDA) && (cond == 1))
		{
			htmltext = "karuda_q0122_0101.htm";
		}
		return htmltext;
	}
}
