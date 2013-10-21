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

import lineage2.gameserver.instancemanager.SoIManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class _695_DefendtheHallofSuffering extends Quest implements ScriptFile
{
	private static final int TEPIOS = 32603;
	
	public _695_DefendtheHallofSuffering()
	{
		super(PARTY_ALL);
		addStartNpc(TEPIOS);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if (event.equalsIgnoreCase("tepios_q695_3.htm"))
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
				if ((player.getLevel() >= 75) && (player.getLevel() <= 82))
				{
					if (SoIManager.getCurrentStage() == 4)
					{
						htmltext = "tepios_q695_1.htm";
					}
					else
					{
						htmltext = "tepios_q695_0a.htm";
						st.exitCurrentQuest(true);
					}
				}
				else
				{
					htmltext = "tepios_q695_0.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if (cond == 1)
			{
				htmltext = "tepios_q695_4.htm";
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
