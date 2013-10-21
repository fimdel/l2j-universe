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

import org.apache.commons.lang3.ArrayUtils;

public class _10318_DecayingDarkness extends Quest implements ScriptFile
{
	private static final int NPC_LYDIA = 32892;
	private static final int ITEM_CURSE_RESIDUE = 17733;
	private static final int[] MOB_ANCIENT_HEROES =
	{
		18978,
		18979,
		18980,
		18981,
		18982,
		18983
	};
	
	public _10318_DecayingDarkness()
	{
		super(PARTY_ONE);
		addStartNpc(NPC_LYDIA);
		addKillId(MOB_ANCIENT_HEROES);
		addQuestItem(ITEM_CURSE_RESIDUE);
		addLevelCheck(95, 99);
		addQuestCompletedCheck(_10317_OrbisWitch.class);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		if (st == null)
		{
			return "noquest";
		}
		if (event.equalsIgnoreCase("32892-07.htm"))
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
		if (st == null)
		{
			return htmltext;
		}
		Player player = st.getPlayer();
		QuestState previous = player.getQuestState(_10317_OrbisWitch.class);
		if (npc.getNpcId() == NPC_LYDIA)
		{
			if ((previous == null) || (!previous.isCompleted()) || (player.getLevel() < 95))
			{
				st.exitCurrentQuest(true);
				return "32892-02.htm";
			}
			switch (st.getState())
			{
				case COMPLETED:
					htmltext = "32892-03.htm";
					break;
				case CREATED:
					htmltext = "32892-01.htm";
					break;
				case STARTED:
					if (st.getCond() == 1)
					{
						if (st.getQuestItemsCount(ITEM_CURSE_RESIDUE) != 0)
						{
							htmltext = "32892-08.htm";
						}
						else
						{
							htmltext = "32892-09.htm";
						}
					}
					else
					{
						if ((st.getCond() != 2) || (st.getQuestItemsCount(ITEM_CURSE_RESIDUE) < 8))
						{
							break;
						}
						htmltext = "32892-10.htm";
						st.addExpAndSp(79260650, 36253450);
						st.giveItems(ADENA_ID, 5427900, true);
						st.playSound(SOUND_FINISH);
						st.exitCurrentQuest(false);
					}
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		if ((st.getCond() == 1) && (ArrayUtils.contains(MOB_ANCIENT_HEROES, npc.getNpcId())))
		{
			if (st.rollAndGive(ITEM_CURSE_RESIDUE, 1, 1, 8, 100))
			{
				st.playSound(SOUND_MIDDLE);
				st.setCond(2);
			}
		}
		return null;
	}
	
	@Override
	public boolean isVisible(Player player)
	{
		QuestState qs = player.getQuestState(_10318_DecayingDarkness.class);
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
