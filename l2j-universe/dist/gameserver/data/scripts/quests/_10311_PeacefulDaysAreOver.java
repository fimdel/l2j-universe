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

public class _10311_PeacefulDaysAreOver extends Quest implements ScriptFile
{
	private static final int NPC_SLAKI = 32893;
	private static final int NPC_SELINA = 33032;
	
	public _10311_PeacefulDaysAreOver()
	{
		super(PARTY_NONE);
		addStartNpc(NPC_SELINA);
		addTalkId(NPC_SLAKI);
		addQuestCompletedCheck(_10312_AbandonedGodsCreature.class);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		if (st == null)
		{
			return "noquest";
		}
		if (event.equalsIgnoreCase("33031-06.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("32893-05.htm"))
		{
			st.addExpAndSp(7168395, 3140085);
			st.giveItems(57, 489220, true);
			st.playSound(SOUND_FINISH);
			st.exitCurrentQuest(false);
		}
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		if (st == null)
		{
			return htmltext;
		}
		npc.getNpcId();
		Player player = st.getPlayer();
		QuestState previous = player.getQuestState(_10312_AbandonedGodsCreature.class);
		if (npc.getNpcId() == NPC_SELINA)
		{
			if ((previous == null) || (!previous.isCompleted()) || (player.getLevel() < 90))
			{
				st.exitCurrentQuest(true);
				return "33032-03.htm";
			}
			switch (st.getState())
			{
				case COMPLETED:
					htmltext = "33032-02.htm";
					break;
				case CREATED:
					htmltext = "33032-01.htm";
					break;
				case STARTED:
					if (st.getCond() != 1)
					{
						break;
					}
					htmltext = "33032-07.htm";
			}
		}
		else if (npc.getNpcId() == NPC_SLAKI)
		{
			if (st.isStarted())
			{
				if (st.getCond() == 1)
				{
					htmltext = "32893-01.htm";
				}
			}
		}
		return htmltext;
	}
	
	@Override
	public boolean isVisible(Player player)
	{
		QuestState qs = player.getQuestState(_10311_PeacefulDaysAreOver.class);
		return ((qs == null) && isAvailableFor(player)) || ((qs != null) && qs.isNowAvailableByTime());
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
}
