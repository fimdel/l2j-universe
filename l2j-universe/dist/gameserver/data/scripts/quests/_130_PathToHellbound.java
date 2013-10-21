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

import lineage2.gameserver.instancemanager.HellboundManager;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class _130_PathToHellbound extends Quest implements ScriptFile
{
	private static int CASIAN = 30612;
	private static int GALATE = 32292;
	private static int CASIAN_BLUE_CRY = 12823;
	
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
	
	public _130_PathToHellbound()
	{
		super(false);
		addStartNpc(CASIAN);
		addTalkId(GALATE);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		int cond = st.getCond();
		String htmltext = event;
		if (event.equals("sage_kasian_q0130_05.htm") && (cond == 0))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		if (event.equals("galate_q0130_03.htm") && (cond == 1))
		{
			st.setCond(2);
			st.playSound(SOUND_MIDDLE);
		}
		if (event.equals("sage_kasian_q0130_08.htm") && (cond == 2))
		{
			st.setCond(3);
			st.playSound(SOUND_MIDDLE);
			st.giveItems(CASIAN_BLUE_CRY, 1);
		}
		if (event.equals("galate_q0130_07.htm") && (cond == 3))
		{
			st.playSound(SOUND_FINISH);
			st.takeItems(CASIAN_BLUE_CRY, -1);
			if (HellboundManager.getHellboundLevel() == 0)
			{
				HellboundManager.getInstance().openHellbound();
			}
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
		if (npcId == CASIAN)
		{
			if (cond == 0)
			{
				if (st.getPlayer().getLevel() >= 78)
				{
					htmltext = "sage_kasian_q0130_01.htm";
				}
				else
				{
					htmltext = "sage_kasian_q0130_02.htm";
					st.exitCurrentQuest(true);
				}
			}
			if (cond == 2)
			{
				htmltext = "sage_kasian_q0130_07.htm";
			}
		}
		else if (id == STARTED)
		{
			if (npcId == GALATE)
			{
				if (cond == 1)
				{
					htmltext = "galate_q0130_01.htm";
				}
				if (cond == 3)
				{
					htmltext = "galate_q0130_05.htm";
				}
			}
		}
		return htmltext;
	}
}
