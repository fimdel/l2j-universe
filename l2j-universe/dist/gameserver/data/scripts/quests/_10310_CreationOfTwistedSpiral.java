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

public class _10310_CreationOfTwistedSpiral extends Quest implements ScriptFile
{
	private static final int NPC_SELINA = 33032;
	private static final int NPC_HORPINA = 33031;
	private static final int MOB_GARDEN_SENTRY = 22947;
	private static final int MOB_GARDEN_SCOUT = 22948;
	private static final int MOB_GARDEN_COMMANDER = 22949;
	private static final int MOB_OUTDOOR_GARDENER = 22950;
	private static final int MOB_GARDEN_DESTROYER = 22951;
	
	public _10310_CreationOfTwistedSpiral()
	{
		super(PARTY_ONE);
		addStartNpc(NPC_SELINA);
		addTalkId(NPC_HORPINA);
		addKillNpcWithLog(2, "Sentry", 10, MOB_GARDEN_SENTRY);
		addKillNpcWithLog(2, "Scount", 10, MOB_GARDEN_SCOUT);
		addKillNpcWithLog(2, "Commander", 10, MOB_GARDEN_COMMANDER);
		addKillNpcWithLog(2, "Gardener", 10, MOB_OUTDOOR_GARDENER);
		addKillNpcWithLog(2, "Destroyer", 10, MOB_GARDEN_DESTROYER);
		addLevelCheck(90, 99);
		addQuestCompletedCheck(_10302_UnsettlingShadowAndRumors.class);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		if (st == null)
		{
			return "noquest";
		}
		if (event.equalsIgnoreCase("33032-06.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("33031-03.htm"))
		{
			st.setCond(2);
			st.playSound(SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("33031-05.htm"))
		{
			st.addExpAndSp(50178765, 21980595);
			st.giveItems(57, 3424540, true);
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
		int npcId = npc.getNpcId();
		Player player = st.getPlayer();
		QuestState previous = player.getQuestState(_10302_UnsettlingShadowAndRumors.class);
		if (npcId == NPC_SELINA)
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
					htmltext = "33032-07.htm";
			}
		}
		else if (npcId == NPC_HORPINA)
		{
			switch (st.getState())
			{
				case COMPLETED:
					htmltext = "completed";
					break;
				case STARTED:
					if (st.getCond() == 1)
					{
						htmltext = "33031-01.htm";
					}
					else if (st.getCond() == 2)
					{
						htmltext = "33031-03.htm";
					}
					else
					{
						if (st.getCond() != 3)
						{
							break;
						}
						htmltext = "33031-04.htm";
					}
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		if ((npc == null) || (st == null))
		{
			return null;
		}
		if (updateKill(npc, st))
		{
			st.setCond(3);
			st.playSound(SOUND_MIDDLE);
			st.unset("Sentry");
			st.unset("Scount");
			st.unset("Commander");
			st.unset("Gardener");
			st.unset("Destroyer");
		}
		return null;
	}
	
	@Override
	public boolean isVisible(Player player)
	{
		QuestState qs = player.getQuestState(_10310_CreationOfTwistedSpiral.class);
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
