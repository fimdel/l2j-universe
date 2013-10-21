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

import java.util.HashMap;
import java.util.Map;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.data.xml.holder.MultiSellHolder;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class _382_KailsMagicCoin extends Quest implements ScriptFile
{
	private static int ROYAL_MEMBERSHIP = 5898;
	private static int VERGARA = 30687;
	private static final Map<Integer, int[]> MOBS = new HashMap<>();
	static
	{
		MOBS.put(21017, new int[]
		{
			5961
		});
		MOBS.put(21019, new int[]
		{
			5962
		});
		MOBS.put(21020, new int[]
		{
			5963
		});
		MOBS.put(21022, new int[]
		{
			5961,
			5962,
			5963
		});
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
	
	public _382_KailsMagicCoin()
	{
		super(false);
		addStartNpc(VERGARA);
		for (int mobId : MOBS.keySet())
		{
			addKillId(mobId);
		}
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if (event.equalsIgnoreCase("head_blacksmith_vergara_q0382_03.htm"))
		{
			if ((st.getPlayer().getLevel() >= 55) && (st.getQuestItemsCount(ROYAL_MEMBERSHIP) > 0))
			{
				st.setCond(1);
				st.setState(STARTED);
				st.playSound(SOUND_ACCEPT);
			}
			else
			{
				htmltext = "head_blacksmith_vergara_q0382_01.htm";
				st.exitCurrentQuest(true);
			}
		}
		else if (event.equalsIgnoreCase("list"))
		{
			MultiSellHolder.getInstance().SeparateAndSend(382, st.getPlayer(), 0);
			htmltext = null;
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int cond = st.getCond();
		if ((st.getQuestItemsCount(ROYAL_MEMBERSHIP) == 0) || (st.getPlayer().getLevel() < 55))
		{
			htmltext = "head_blacksmith_vergara_q0382_01.htm";
			st.exitCurrentQuest(true);
		}
		else if (cond == 0)
		{
			htmltext = "head_blacksmith_vergara_q0382_02.htm";
		}
		else
		{
			htmltext = "head_blacksmith_vergara_q0382_04.htm";
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		if ((st.getState() != STARTED) || (st.getQuestItemsCount(ROYAL_MEMBERSHIP) == 0))
		{
			return null;
		}
		int[] droplist = MOBS.get(npc.getNpcId());
		st.rollAndGive(droplist[Rnd.get(droplist.length)], 1, 10);
		return null;
	}
}
