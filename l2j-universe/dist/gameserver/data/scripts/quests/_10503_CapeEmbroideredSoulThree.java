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
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class _10503_CapeEmbroideredSoulThree extends Quest implements ScriptFile
{
	private static final int OLF_ADAMS = 32612;
	private static final int FRINTEZZA = 29047;
	private static final int SOUL_FRINTEZZA = 21724;
	private static final int CLOAK_FRINTEZZA = 21721;
	
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
	
	public _10503_CapeEmbroideredSoulThree()
	{
		super(PARTY_ALL);
		addStartNpc(OLF_ADAMS);
		addTalkId(OLF_ADAMS);
		addKillId(FRINTEZZA);
		addQuestItem(SOUL_FRINTEZZA);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		if (event.equalsIgnoreCase("olf_adams_q10503_02.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int cond = st.getCond();
		if (cond == 0)
		{
			if (st.getPlayer().getLevel() >= 80)
			{
				htmltext = "olf_adams_q10503_01.htm";
			}
			else
			{
				htmltext = "olf_adams_q10503_00.htm";
				st.exitCurrentQuest(true);
			}
		}
		else if (cond == 1)
		{
			htmltext = "olf_adams_q10503_03.htm";
		}
		else if (cond == 2)
		{
			if (st.getQuestItemsCount(SOUL_FRINTEZZA) < 20)
			{
				st.setCond(1);
				htmltext = "olf_adams_q10503_03.htm";
			}
			else
			{
				st.takeItems(SOUL_FRINTEZZA, -1);
				st.giveItems(CLOAK_FRINTEZZA, 1);
				st.playSound(SOUND_FINISH);
				htmltext = "olf_adams_q10503_04.htm";
				st.exitCurrentQuest(false);
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		int npcId = npc.getNpcId();
		int cond = st.getCond();
		if ((cond == 1) && (npcId == FRINTEZZA))
		{
			if (st.getQuestItemsCount(SOUL_FRINTEZZA) < 20)
			{
				st.giveItems(SOUL_FRINTEZZA, Rnd.get(1, 3), false);
			}
			if (st.getQuestItemsCount(SOUL_FRINTEZZA) >= 20)
			{
				st.setCond(2);
				st.playSound(SOUND_MIDDLE);
			}
		}
		return null;
	}
}
