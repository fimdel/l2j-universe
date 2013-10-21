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
import lineage2.gameserver.network.serverpackets.TutorialShowHtml;
import lineage2.gameserver.scripts.ScriptFile;

public class _10320_LetsGototheCentralSquare extends Quest implements ScriptFile
{
	private static final int teodor = 32975;
	private static final int panteleon = 32972;
	
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
	
	public _10320_LetsGototheCentralSquare()
	{
		super(false);
		addStartNpc(panteleon);
		addTalkId(panteleon);
		addTalkId(teodor);
		addLevelCheck(1, 20);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if (event.equalsIgnoreCase("quest_ac"))
		{
			st.setState(STARTED);
			st.setCond(1);
			st.playSound(SOUND_ACCEPT);
			st.showTutorialHTML(TutorialShowHtml.QT_001, TutorialShowHtml.TYPE_WINDOW);
			htmltext = "0-3.htm";
		}
		if (event.equalsIgnoreCase("qet_rev"))
		{
			htmltext = "1-2.htm";
			st.getPlayer().addExpAndSp(30, 100);
			st.giveItems(57, 3000);
			st.exitCurrentQuest(false);
			st.playSound(SOUND_FINISH);
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		int cond = st.getCond();
		int npcId = npc.getNpcId();
		String htmltext = "noquest";
		if (npcId == panteleon)
		{
			if (st.isCompleted())
			{
				htmltext = "0-c.htm";
			}
			else if ((cond == 0) && isAvailableFor(st.getPlayer()))
			{
				htmltext = "0-1.htm";
			}
			else if (cond == 1)
			{
				htmltext = "0-4.htm";
			}
		}
		else if (npcId == teodor)
		{
			if (st.isCompleted())
			{
				htmltext = "1-c.htm";
			}
			else if (cond == 0)
			{
				htmltext = "1-t.htm";
			}
			else if (cond == 1)
			{
				htmltext = "1-1.htm";
			}
		}
		return htmltext;
	}
}
