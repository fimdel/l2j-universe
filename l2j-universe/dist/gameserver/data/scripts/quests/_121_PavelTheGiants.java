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

public class _121_PavelTheGiants extends Quest implements ScriptFile
{
	private static int NEWYEAR = 31961;
	private static int YUMI = 32041;
	
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
	
	public _121_PavelTheGiants()
	{
		super(false);
		addStartNpc(NEWYEAR);
		addTalkId(NEWYEAR, YUMI);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		if (event.equals("collecter_yumi_q0121_0201.htm"))
		{
			st.playSound(SOUND_FINISH);
			st.addExpAndSp(1959460, 2039940);
			st.exitCurrentQuest(false);
		}
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int npcId = npc.getNpcId();
		int id = st.getState();
		int cond = st.getCond();
		if ((id == CREATED) && (npcId == NEWYEAR))
		{
			if (st.getPlayer().getLevel() >= 70)
			{
				htmltext = "head_blacksmith_newyear_q0121_0101.htm";
				st.setCond(1);
				st.setState(STARTED);
				st.playSound(SOUND_ACCEPT);
			}
			else
			{
				htmltext = "head_blacksmith_newyear_q0121_0103.htm";
				st.exitCurrentQuest(false);
			}
		}
		else if (id == STARTED)
		{
			if ((npcId == YUMI) && (cond == 1))
			{
				htmltext = "collecter_yumi_q0121_0101.htm";
			}
			else
			{
				htmltext = "head_blacksmith_newyear_q0121_0105.htm";
			}
		}
		return htmltext;
	}
}
