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

public class _10312_AbandonedGodsCreature extends Quest implements ScriptFile
{
	private static final int NPC_HORPINA = 33031;
	private static final int REWARD_GIANTS_WARSMITH_HOLDER = 19305;
	private static final int REWARD_GIANTS_REORIN_MOLD = 19306;
	private static final int REWARD_GIANTS_ARCSMITH_ANVIL = 19307;
	private static final int REWARD_GIANTS_WARSMITH_MOLD = 19308;
	private static final int REWARD_ENCHANT_ARMOR_AR = 17527;
	private static final int REWARD_HARDENER_POUNCH_R = 34861;
	private static final int MOB_APHROS = 25866;
	
	public _10312_AbandonedGodsCreature()
	{
		super(PARTY_ALL);
		addStartNpc(NPC_HORPINA);
		addKillId(MOB_APHROS);
		addKillNpcWithLog(1, "Arphos", 1, MOB_APHROS);
		addQuestCompletedCheck(_10310_CreationOfTwistedSpiral.class);
		addLevelCheck(90, 99);
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
		else if (event.equalsIgnoreCase("33031-10.htm"))
		{
			st.addExpAndSp(46847289, 20739487);
			st.giveItems(REWARD_GIANTS_WARSMITH_HOLDER, 1);
			st.giveItems(REWARD_GIANTS_REORIN_MOLD, 1);
			st.giveItems(REWARD_GIANTS_ARCSMITH_ANVIL, 1);
			st.giveItems(REWARD_GIANTS_WARSMITH_MOLD, 1);
			st.playSound(SOUND_FINISH);
			st.exitCurrentQuest(false);
		}
		else if (event.equalsIgnoreCase("33031-11.htm"))
		{
			st.addExpAndSp(46847289, 20739487);
			st.giveItems(REWARD_ENCHANT_ARMOR_AR, 2);
			st.playSound(SOUND_FINISH);
			st.exitCurrentQuest(false);
		}
		else if (event.equalsIgnoreCase("33031-12.htm"))
		{
			st.addExpAndSp(46847289, 20739487);
			st.giveItems(REWARD_HARDENER_POUNCH_R, 1);
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
		Player player = st.getPlayer();
		QuestState previous = player.getQuestState(_10310_CreationOfTwistedSpiral.class);
		if (npc.getNpcId() == 33031)
		{
			if ((previous == null) || (!previous.isCompleted()) || (player.getLevel() < 90))
			{
				st.exitCurrentQuest(true);
				return "33031-03.htm";
			}
			switch (st.getState())
			{
				case COMPLETED:
					htmltext = "33031-02.htm";
					break;
				case CREATED:
					htmltext = "33031-01.htm";
					break;
				case STARTED:
					if (st.getCond() == 1)
					{
						htmltext = "33031-07.htm";
					}
					else
					{
						if (st.getCond() != 2)
						{
							break;
						}
						htmltext = "33031-09.htm";
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
			st.setCond(2);
			st.playSound(SOUND_MIDDLE);
			st.unset("Arphos");
		}
		return null;
	}
	
	@Override
	public boolean isVisible(Player player)
	{
		QuestState qs = player.getQuestState(_10312_AbandonedGodsCreature.class);
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
