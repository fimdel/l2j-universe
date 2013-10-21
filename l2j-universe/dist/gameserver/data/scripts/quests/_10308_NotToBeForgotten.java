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

public class _10308_NotToBeForgotten extends Quest implements ScriptFile
{
	private static final int NPC_ADVENTURER_HELPER = 33463;
	private static final int NPC_KURTIZ = 30870;
	private static final int[] MONSTERS =
	{
		20679,
		20680,
		21017,
		21019,
		21020,
		21022,
		21258,
		21259,
		21018,
		21021
	};
	private static final int ITEM_LEGACY_CORE = 19487;
	private static final int DROP_CHANCE = 60;
	
	public _10308_NotToBeForgotten()
	{
		super(false);
		addStartNpc(NPC_ADVENTURER_HELPER);
		addTalkId(NPC_KURTIZ);
		addKillId(MONSTERS);
		addQuestItem(ITEM_LEGACY_CORE);
		addLevelCheck(55, 59);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		if (st == null)
		{
			return "noquest";
		}
		if (event.equalsIgnoreCase("33463-04.htm"))
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
		if (npc.getNpcId() == NPC_ADVENTURER_HELPER)
		{
			if ((player.getLevel() < 55) || (player.getLevel() > 59))
			{
				htmltext = "33463-00.htm";
			}
			else if (st.isCreated())
			{
				htmltext = "33463-01.htm";
			}
			else if ((st.isStarted()) && (st.getCond() == 1))
			{
				htmltext = "33463-05.htm";
			}
			else if (st.isCompleted())
			{
				htmltext = "completed";
			}
		}
		else if (npc.getNpcId() == NPC_KURTIZ)
		{
			if (st.isCompleted())
			{
				htmltext = "30870-03.htm";
			}
			else if ((st.isStarted()) && (st.getCond() == 1))
			{
				htmltext = "30870-01.htm";
			}
			else if ((st.isStarted()) && (st.getCond() == 2))
			{
				st.takeItems(ITEM_LEGACY_CORE, -1);
				st.addExpAndSp(2322445, 1968325);
				st.giveItems(57, 376704, true);
				st.unset("cond");
				st.exitCurrentQuest(false);
				htmltext = "30870-02.htm";
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
		if (ArrayUtils.contains(MONSTERS, npc.getNpcId()) && (st.getCond() == 1))
		{
			if (st.rollAndGive(ITEM_LEGACY_CORE, 1, 3, 40, DROP_CHANCE))
			{
				st.setCond(2);
				st.playSound(SOUND_MIDDLE);
			}
		}
		return null;
	}
	
	@Override
	public boolean isVisible(Player player)
	{
		QuestState qs = player.getQuestState(_10308_NotToBeForgotten.class);
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
