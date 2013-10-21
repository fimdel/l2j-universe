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

public class _119_LastImperialPrince extends Quest implements ScriptFile
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
	
	private static final int SPIRIT = 31453;
	private static final int DEVORIN = 32009;
	private static final int BROOCH = 7262;
	private static final int AMOUNT = 407970;
	
	public _119_LastImperialPrince()
	{
		super(false);
		addStartNpc(SPIRIT);
		addTalkId(DEVORIN);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if (event.equalsIgnoreCase("31453-4.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("32009-2.htm"))
		{
			if (st.getQuestItemsCount(BROOCH) < 1)
			{
				htmltext = "noquest";
				st.exitCurrentQuest(true);
			}
		}
		else if (event.equalsIgnoreCase("32009-3.htm"))
		{
			st.setCond(2);
			st.playSound(SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("31453-7.htm"))
		{
			st.giveItems(ADENA_ID, AMOUNT, true);
			st.addExpAndSp(1919448, 2100933);
			st.playSound(SOUND_FINISH);
			st.exitCurrentQuest(false);
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int npcId = npc.getNpcId();
		int cond = st.getCond();
		if (st.getPlayer().getLevel() < 74)
		{
			htmltext = "<html><body>Quest for characters level 74 and above.</body></html>";
			st.exitCurrentQuest(true);
			return htmltext;
		}
		else if (st.getQuestItemsCount(BROOCH) < 1)
		{
			htmltext = "noquest";
			st.exitCurrentQuest(true);
			return htmltext;
		}
		if (npcId == SPIRIT)
		{
			if (cond == 0)
			{
				return "31453-1.htm";
			}
			else if (cond == 2)
			{
				return "31453-5.htm";
			}
			else
			{
				return "noquest";
			}
		}
		else if ((npcId == DEVORIN) && (cond == 1))
		{
			htmltext = "32009-1.htm";
		}
		return htmltext;
	}
}
