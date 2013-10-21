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
import lineage2.gameserver.instancemanager.SoIManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class _697_DefendtheHallofErosion extends Quest implements ScriptFile
{
	private static final int TEPIOS = 32603;
	private static final int VesperNobleEnhanceStone = 14052;
	
	public _697_DefendtheHallofErosion()
	{
		super(PARTY_ALL);
		addStartNpc(TEPIOS);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		st.getPlayer();
		String htmltext = event;
		if (event.equalsIgnoreCase("tepios_q697_3.htm"))
		{
			st.setState(STARTED);
			st.setCond(1);
			st.playSound(SOUND_ACCEPT);
		}
		return htmltext;
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
				if (player.getLevel() < 75)
				{
					st.exitCurrentQuest(true);
					return "tepios_q697_0.htm";
				}
				if (SoIManager.getCurrentStage() != 4)
				{
					st.exitCurrentQuest(true);
					return "tepios_q697_0a.htm";
				}
				htmltext = "tepios_q697_1.htm";
			}
			else if ((cond == 1) && (st.getInt("defenceDone") == 0))
			{
				htmltext = "tepios_q697_4.htm";
			}
			else if ((cond == 1) && (st.getInt("defenceDone") != 0))
			{
				st.giveItems(VesperNobleEnhanceStone, Rnd.get(12, 20));
				htmltext = "tepios_q697_5.htm";
				st.playSound(SOUND_FINISH);
				st.unset("defenceDone");
				st.exitCurrentQuest(true);
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
