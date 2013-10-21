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

public class _607_ProveYourCourage extends Quest implements ScriptFile
{
	private final static int KADUN_ZU_KETRA = 31370;
	private final static int VARKAS_HERO_SHADITH = 25309;
	private final static int HEAD_OF_SHADITH = 7235;
	private final static int TOTEM_OF_VALOR = 7219;
	@SuppressWarnings("unused")
	private final static int MARK_OF_KETRA_ALLIANCE1 = 7211;
	@SuppressWarnings("unused")
	private final static int MARK_OF_KETRA_ALLIANCE2 = 7212;
	private final static int MARK_OF_KETRA_ALLIANCE3 = 7213;
	private final static int MARK_OF_KETRA_ALLIANCE4 = 7214;
	private final static int MARK_OF_KETRA_ALLIANCE5 = 7215;
	
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
	
	public _607_ProveYourCourage()
	{
		super(true);
		addStartNpc(KADUN_ZU_KETRA);
		addKillId(VARKAS_HERO_SHADITH);
		addQuestItem(HEAD_OF_SHADITH);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if (event.equals("quest_accept"))
		{
			htmltext = "elder_kadun_zu_ketra_q0607_0104.htm";
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equals("607_3"))
		{
			if (st.getQuestItemsCount(HEAD_OF_SHADITH) >= 1)
			{
				htmltext = "elder_kadun_zu_ketra_q0607_0201.htm";
				st.takeItems(HEAD_OF_SHADITH, -1);
				st.giveItems(TOTEM_OF_VALOR, 1);
				st.addExpAndSp(0, 10000);
				st.unset("cond");
				st.playSound(SOUND_FINISH);
				st.exitCurrentQuest(true);
			}
			else
			{
				htmltext = "elder_kadun_zu_ketra_q0607_0106.htm";
			}
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
			if (st.getPlayer().getLevel() >= 75)
			{
				if ((st.getQuestItemsCount(MARK_OF_KETRA_ALLIANCE3) == 1) || (st.getQuestItemsCount(MARK_OF_KETRA_ALLIANCE4) == 1) || (st.getQuestItemsCount(MARK_OF_KETRA_ALLIANCE5) == 1))
				{
					htmltext = "elder_kadun_zu_ketra_q0607_0101.htm";
				}
				else
				{
					htmltext = "elder_kadun_zu_ketra_q0607_0102.htm";
					st.exitCurrentQuest(true);
				}
			}
			else
			{
				htmltext = "elder_kadun_zu_ketra_q0607_0103.htm";
				st.exitCurrentQuest(true);
			}
		}
		else if ((cond == 1) && (st.getQuestItemsCount(HEAD_OF_SHADITH) == 0))
		{
			htmltext = "elder_kadun_zu_ketra_q0607_0106.htm";
		}
		else if ((cond == 2) && (st.getQuestItemsCount(HEAD_OF_SHADITH) >= 1))
		{
			htmltext = "elder_kadun_zu_ketra_q0607_0105.htm";
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		int npcId = npc.getNpcId();
		if ((npcId == VARKAS_HERO_SHADITH) && (st.getCond() == 1))
		{
			st.giveItems(HEAD_OF_SHADITH, 1);
			st.setCond(2);
			st.playSound(SOUND_ITEMGET);
		}
		return null;
	}
}
