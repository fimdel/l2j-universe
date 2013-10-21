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

public class _654_JourneytoaSettlement extends Quest implements ScriptFile
{
	private static final int NamelessSpirit = 31453;
	private static final int CanyonAntelope = 21294;
	private static final int CanyonAntelopeSlave = 21295;
	private static final int AntelopeSkin = 8072;
	private static final int FrintezzasMagicForceFieldRemovalScroll = 8073;
	
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
	
	public _654_JourneytoaSettlement()
	{
		super(true);
		addStartNpc(NamelessSpirit);
		addKillId(CanyonAntelope, CanyonAntelopeSlave);
		addQuestItem(AntelopeSkin);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		if (event.equalsIgnoreCase("printessa_spirit_q0654_03.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		if (event.equalsIgnoreCase("printessa_spirit_q0654_04.htm"))
		{
			st.setCond(2);
		}
		if (event.equalsIgnoreCase("printessa_spirit_q0654_07.htm"))
		{
			st.giveItems(FrintezzasMagicForceFieldRemovalScroll, 1);
			st.playSound(SOUND_FINISH);
			st.exitCurrentQuest(true);
		}
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		QuestState q = st.getPlayer().getQuestState(_119_LastImperialPrince.class);
		if (q == null)
		{
			return htmltext;
		}
		if (st.getPlayer().getLevel() < 74)
		{
			htmltext = "printessa_spirit_q0654_02.htm";
			st.exitCurrentQuest(true);
			return htmltext;
		}
		else if (!q.isCompleted())
		{
			htmltext = "noquest";
			st.exitCurrentQuest(true);
			return htmltext;
		}
		int cond = st.getCond();
		if (npc.getNpcId() == NamelessSpirit)
		{
			if (cond == 0)
			{
				return "printessa_spirit_q0654_01.htm";
			}
			if (cond == 1)
			{
				return "printessa_spirit_q0654_03.htm";
			}
			if (cond == 3)
			{
				return "printessa_spirit_q0654_06.htm";
			}
		}
		else
		{
			htmltext = "noquest";
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		if ((st.getCond() == 2) && Rnd.chance(5))
		{
			st.setCond(3);
			st.giveItems(AntelopeSkin, 1);
			st.playSound(SOUND_MIDDLE);
		}
		return null;
	}
}
