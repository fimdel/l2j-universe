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

public class _10316_UndecayingMemoryOfThePast extends Quest implements ScriptFile
{
	private static final int NPC_OPERA = 32946;
	private static final int MOB_SPEZION = 25779;
	private static final int REWARD_GIANTS_WARMISHT_HOLDER = 19305;
	private static final int REWARD_GIANTS_REONIRS_MOLD = 19306;
	private static final int REWARD_GIANTS_ARCSMITH_ANVIL = 19307;
	private static final int REWARD_GIANTS_WARSMITH_MOLD = 19308;
	private static final int REWARD_ENCHANT_ARMOR_R = 17527;
	private static final int REWARD_HARDENER_POUNCH_R = 34861;
	
	public _10316_UndecayingMemoryOfThePast()
	{
		super(PARTY_ALL);
		addStartNpc(NPC_OPERA);
		addKillId(MOB_SPEZION);
		addLevelCheck(90, 99);
		addQuestCompletedCheck(_10315_ToThePrisonOfDarkness.class);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		if (st == null)
		{
			return "noquest";
		}
		if (event.equalsIgnoreCase("32946-06.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("32946-12.htm"))
		{
			st.addExpAndSp(54093924, 23947602);
			st.giveItems(REWARD_GIANTS_WARMISHT_HOLDER, 1);
			st.giveItems(REWARD_GIANTS_REONIRS_MOLD, 1);
			st.giveItems(REWARD_GIANTS_ARCSMITH_ANVIL, 1);
			st.giveItems(REWARD_GIANTS_WARSMITH_MOLD, 1);
			st.playSound(SOUND_FINISH);
			st.exitCurrentQuest(false);
		}
		else if (event.equalsIgnoreCase("32946-12.htm"))
		{
			st.addExpAndSp(54093924, 23947602);
			st.giveItems(REWARD_ENCHANT_ARMOR_R, 2);
			st.playSound(SOUND_FINISH);
			st.exitCurrentQuest(false);
		}
		else if (event.equalsIgnoreCase("32946-12.htm"))
		{
			st.addExpAndSp(54093924, 23947602);
			st.giveItems(REWARD_HARDENER_POUNCH_R, 2);
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
		QuestState previous = player.getQuestState(_10315_ToThePrisonOfDarkness.class);
		if (npc.getNpcId() == NPC_OPERA)
		{
			if ((previous == null) || (!previous.isCompleted()))
			{
				if (player.getLevel() < 90)
				{
					st.exitCurrentQuest(true);
					return "32946-02.htm";
				}
			}
			else
			{
				st.exitCurrentQuest(true);
				return "32946-09.htm";
			}
			switch (st.getState())
			{
				case COMPLETED:
					htmltext = "32946-10.htm";
					break;
				case CREATED:
					htmltext = "32946-01.htm";
					break;
				case STARTED:
					if (st.getCond() == 1)
					{
						htmltext = "32946-07.htm";
					}
					else
					{
						if (st.getCond() != 2)
						{
							break;
						}
						htmltext = "32946-08.htm";
					}
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		if ((st.getCond() == 1) && (npc.getNpcId() == MOB_SPEZION))
		{
			st.playSound(SOUND_MIDDLE);
			st.setCond(2);
		}
		return null;
	}
	
	@Override
	public boolean isVisible(Player player)
	{
		QuestState qs = player.getQuestState(_10315_ToThePrisonOfDarkness.class);
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
