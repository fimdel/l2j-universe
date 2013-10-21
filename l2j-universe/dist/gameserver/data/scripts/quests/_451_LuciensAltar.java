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

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

import org.apache.commons.lang3.ArrayUtils;

public class _451_LuciensAltar extends Quest implements ScriptFile
{
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
	
	private static int DAICHIR = 30537;
	private static int REPLENISHED_BEAD = 14877;
	private static int DISCHARGED_BEAD = 14878;
	private static int ALTAR_1 = 32706;
	private static int ALTAR_2 = 32707;
	private static int ALTAR_3 = 32708;
	private static int ALTAR_4 = 32709;
	private static int ALTAR_5 = 32710;
	private static int[] ALTARS = new int[]
	{
		ALTAR_1,
		ALTAR_2,
		ALTAR_3,
		ALTAR_4,
		ALTAR_5
	};
	
	public _451_LuciensAltar()
	{
		super(false);
		addStartNpc(DAICHIR);
		addTalkId(ALTARS);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		if (event.equalsIgnoreCase("30537-03.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.giveItems(REPLENISHED_BEAD, 5);
			st.playSound(SOUND_ACCEPT);
		}
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int npcId = npc.getNpcId();
		int cond = st.getCond();
		Player player = st.getPlayer();
		if (npcId == DAICHIR)
		{
			if (cond == 0)
			{
				if (player.getLevel() < 80)
				{
					htmltext = "30537-00.htm";
					st.exitCurrentQuest(true);
				}
				else if (!canEnter(player))
				{
					htmltext = "30537-06.htm";
					st.exitCurrentQuest(true);
				}
				else
				{
					htmltext = "30537-01.htm";
				}
			}
			else if (cond == 1)
			{
				htmltext = "30537-04.htm";
			}
			else if (cond == 2)
			{
				htmltext = "30537-05.htm";
				st.addExpAndSp(13773960, 16232820);
				st.giveItems(ADENA_ID, 742800);
				st.takeItems(DISCHARGED_BEAD, -1);
				st.exitCurrentQuest(true);
				st.playSound(SOUND_FINISH);
				st.getPlayer().setVar(getName(), String.valueOf(System.currentTimeMillis()), -1);
			}
		}
		else if ((cond == 1) && ArrayUtils.contains(ALTARS, npcId))
		{
			if ((npcId == ALTAR_1) && (st.getInt("Altar1") < 1))
			{
				htmltext = "recharge.htm";
				onAltarCheck(st);
				st.set("Altar1", 1);
			}
			else if ((npcId == ALTAR_2) && (st.getInt("Altar2") < 1))
			{
				htmltext = "recharge.htm";
				onAltarCheck(st);
				st.set("Altar2", 1);
			}
			else if ((npcId == ALTAR_3) && (st.getInt("Altar3") < 1))
			{
				htmltext = "recharge.htm";
				onAltarCheck(st);
				st.set("Altar3", 1);
			}
			else if ((npcId == ALTAR_4) && (st.getInt("Altar4") < 1))
			{
				htmltext = "recharge.htm";
				onAltarCheck(st);
				st.set("Altar4", 1);
			}
			else if ((npcId == ALTAR_5) && (st.getInt("Altar5") < 1))
			{
				htmltext = "recharge.htm";
				onAltarCheck(st);
				st.set("Altar5", 1);
			}
			else
			{
				htmltext = "findother.htm";
			}
		}
		return htmltext;
	}
	
	private void onAltarCheck(QuestState st)
	{
		st.takeItems(REPLENISHED_BEAD, 1);
		st.giveItems(DISCHARGED_BEAD, 1);
		st.playSound(SOUND_ITEMGET);
		if (st.getQuestItemsCount(DISCHARGED_BEAD) >= 5)
		{
			st.setCond(2);
			st.playSound(SOUND_MIDDLE);
		}
	}
	
	private boolean canEnter(Player player)
	{
		if (player.isGM())
		{
			return true;
		}
		String var = player.getVar(getName());
		if (var == null)
		{
			return true;
		}
		return (Long.parseLong(var) - System.currentTimeMillis()) > (24 * 60 * 60 * 1000);
	}
}
