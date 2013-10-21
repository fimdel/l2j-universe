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

public class _10340_RevivedPowerOfTheGiant extends Quest implements ScriptFile
{
	private static final int NPC_SEBIO = 32978;
	private static final int NPC_PANTHEON = 32972;
	private static final int MOB_HARNAKS_WRAITH = 25772;
	private static final int REWARD_YELLOW_FRAGMENT = 19508;
	private static final int REWARD_TEAL_FRAGMENT = 19509;
	private static final int REWARD_PURPLE_FRAGMENT = 19510;
	
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
	
	public _10340_RevivedPowerOfTheGiant()
	{
		super(PARTY_ALL);
		addStartNpc(NPC_SEBIO);
		addTalkId(NPC_PANTHEON);
		addKillId(MOB_HARNAKS_WRAITH);
		addLevelCheck(85, 99);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if (event.equalsIgnoreCase("32978-04.htm"))
		{
			st.setState(STARTED);
			st.setCond(1);
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("32972-02.htm"))
		{
			st.playSound("ItemSound.quest_finish");
			st.addExpAndSp(235645257, 97634343);
			st.giveItems(REWARD_YELLOW_FRAGMENT, 1);
			st.exitCurrentQuest(false);
		}
		else if (event.equalsIgnoreCase("32972-03.htm"))
		{
			st.playSound("ItemSound.quest_finish");
			st.addExpAndSp(235645257, 97634343);
			st.giveItems(REWARD_PURPLE_FRAGMENT, 1);
			st.exitCurrentQuest(false);
		}
		else if (event.equalsIgnoreCase("32972-04.htm"))
		{
			st.playSound("ItemSound.quest_finish");
			st.addExpAndSp(235645257, 97634343);
			st.giveItems(REWARD_TEAL_FRAGMENT, 1);
			st.exitCurrentQuest(false);
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		st.getCond();
		npc.getNpcId();
		Player player = st.getPlayer();
		String htmltext = "noquest";
		if (npc.getNpcId() == NPC_SEBIO)
		{
			switch (st.getState())
			{
				case COMPLETED:
					htmltext = "32978-06.htm";
					break;
				case CREATED:
					if (player.getLevel() < 85)
					{
						htmltext = "32978-05.htm";
					}
					else
					{
						htmltext = "32978-01.htm";
						st.exitCurrentQuest(true);
					}
					break;
				case STARTED:
					switch (st.getCond())
					{
						case 1:
							htmltext = "32978-04.htm";
							break;
						case 2:
							htmltext = "32978-07.htm";
							st.playSound("ItemSound.quest_middle");
							st.setCond(3);
							break;
						case 3:
							htmltext = "32978-08.htm";
					}
			}
		}
		else if (npc.getNpcId() == NPC_PANTHEON)
		{
			if ((st.isStarted()) && (st.getCond() == 3))
			{
				htmltext = "32972-01.htm";
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		if ((npc.getNpcId() == MOB_HARNAKS_WRAITH) && (st.getCond() == 1))
		{
			st.setCond(2);
			st.playSound("ItemSound.quest_middle");
		}
		return null;
	}
}
