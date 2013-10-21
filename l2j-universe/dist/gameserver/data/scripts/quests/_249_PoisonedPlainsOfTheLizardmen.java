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

public class _249_PoisonedPlainsOfTheLizardmen extends Quest implements ScriptFile
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
	
	private static final int MOUEN = 30196;
	private static final int JOHNNY = 32744;
	
	public _249_PoisonedPlainsOfTheLizardmen()
	{
		super(false);
		addStartNpc(MOUEN);
		addTalkId(MOUEN);
		addTalkId(JOHNNY);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if (npc.getNpcId() == MOUEN)
		{
			if (event.equalsIgnoreCase("30196-03.htm"))
			{
				st.setState(STARTED);
				st.setCond(1);
				st.playSound(SOUND_ACCEPT);
			}
		}
		else if ((npc.getNpcId() == JOHNNY) && event.equalsIgnoreCase("32744-03.htm"))
		{
			st.unset("cond");
			st.giveItems(57, 83056);
			st.addExpAndSp(477496, 58743);
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
		if (npcId == MOUEN)
		{
			switch (st.getState())
			{
				case CREATED:
					if (st.getPlayer().getLevel() >= 82)
					{
						htmltext = "30196-01.htm";
					}
					else
					{
						htmltext = "30196-00.htm";
					}
					break;
				case STARTED:
					if (cond == 1)
					{
						htmltext = "30196-04.htm";
					}
					break;
				case COMPLETED:
					htmltext = "30196-05.htm";
					break;
			}
		}
		else if (npcId == JOHNNY)
		{
			if (cond == 1)
			{
				htmltext = "32744-01.htm";
			}
			else if (st.isCompleted())
			{
				htmltext = "32744-04.htm";
			}
		}
		return htmltext;
	}
}
