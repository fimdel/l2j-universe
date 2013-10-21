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

public class _606_WarwithVarkaSilenos extends Quest implements ScriptFile
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
	
	private static final int KADUN_ZU_KETRA = 31370;
	private static final int VARKAS_MANE = 7233;
	private static final int VARKAS_MANE_DROP_CHANCE = 80;
	private static final int HORN_OF_BUFFALO = 7186;
	private static final int[] VARKA_NPC_LIST = new int[20];
	
	public _606_WarwithVarkaSilenos()
	{
		super(true);
		addStartNpc(KADUN_ZU_KETRA);
		VARKA_NPC_LIST[0] = 21350;
		VARKA_NPC_LIST[1] = 21351;
		VARKA_NPC_LIST[2] = 21353;
		VARKA_NPC_LIST[3] = 21354;
		VARKA_NPC_LIST[4] = 21355;
		VARKA_NPC_LIST[5] = 21357;
		VARKA_NPC_LIST[6] = 21358;
		VARKA_NPC_LIST[7] = 21360;
		VARKA_NPC_LIST[8] = 21361;
		VARKA_NPC_LIST[9] = 21362;
		VARKA_NPC_LIST[10] = 21364;
		VARKA_NPC_LIST[11] = 21365;
		VARKA_NPC_LIST[12] = 21366;
		VARKA_NPC_LIST[13] = 21368;
		VARKA_NPC_LIST[14] = 21369;
		VARKA_NPC_LIST[15] = 21370;
		VARKA_NPC_LIST[16] = 21371;
		VARKA_NPC_LIST[17] = 21372;
		VARKA_NPC_LIST[18] = 21373;
		VARKA_NPC_LIST[19] = 21374;
		addKillId(VARKA_NPC_LIST);
		addQuestItem(VARKAS_MANE);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if (event.equals("quest_accept"))
		{
			htmltext = "elder_kadun_zu_ketra_q0606_0104.htm";
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equals("606_3"))
		{
			long ec = st.getQuestItemsCount(VARKAS_MANE) / 5;
			if (ec > 0)
			{
				htmltext = "elder_kadun_zu_ketra_q0606_0202.htm";
				st.takeItems(VARKAS_MANE, ec * 5);
				st.giveItems(HORN_OF_BUFFALO, ec);
			}
			else
			{
				htmltext = "elder_kadun_zu_ketra_q0606_0203.htm";
			}
		}
		else if (event.equals("606_4"))
		{
			htmltext = "elder_kadun_zu_ketra_q0606_0204.htm";
			st.takeItems(VARKAS_MANE, -1);
			st.playSound(SOUND_FINISH);
			st.exitCurrentQuest(true);
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int cond = st.getCond();
		if (cond == 0)
		{
			if (st.getPlayer().getLevel() >= 74)
			{
				htmltext = "elder_kadun_zu_ketra_q0606_0101.htm";
			}
			else
			{
				htmltext = "elder_kadun_zu_ketra_q0606_0103.htm";
				st.exitCurrentQuest(true);
			}
		}
		else if ((cond == 1) && (st.getQuestItemsCount(VARKAS_MANE) == 0))
		{
			htmltext = "elder_kadun_zu_ketra_q0606_0106.htm";
		}
		else if ((cond == 1) && (st.getQuestItemsCount(VARKAS_MANE) > 0))
		{
			htmltext = "elder_kadun_zu_ketra_q0606_0105.htm";
		}
		return htmltext;
	}
	
	public boolean isVarkaNpc(int npc)
	{
		for (int i : VARKA_NPC_LIST)
		{
			if (npc == i)
			{
				return true;
			}
		}
		return false;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		if (isVarkaNpc(npc.getNpcId()) && (st.getCond() == 1))
		{
			st.rollAndGive(VARKAS_MANE, 1, VARKAS_MANE_DROP_CHANCE);
		}
		return null;
	}
}
