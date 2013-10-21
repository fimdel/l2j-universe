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

public class _618_IntoTheFlame extends Quest implements ScriptFile
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
	
	private static final int KLEIN = 31540;
	private static final int HILDA = 31271;
	private static final int VACUALITE_ORE = 7265;
	private static final int VACUALITE = 7266;
	private static final int FLOATING_STONE = 7267;
	private static final int CHANCE_FOR_QUEST_ITEMS = 50;
	
	public _618_IntoTheFlame()
	{
		super(false);
		addStartNpc(KLEIN);
		addTalkId(HILDA);
		addKillId(21274, 21275, 21276, 21278);
		addKillId(21282, 21283, 21284, 21286);
		addKillId(21290, 21291, 21292, 21294);
		addQuestItem(VACUALITE_ORE);
		addQuestItem(VACUALITE);
		addQuestItem(FLOATING_STONE);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		int cond = st.getCond();
		if (event.equalsIgnoreCase("watcher_valakas_klein_q0618_0104.htm") && (cond == 0))
		{
			st.setState(STARTED);
			st.setCond(1);
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("watcher_valakas_klein_q0618_0401.htm"))
		{
			if ((st.getQuestItemsCount(VACUALITE) > 0) && (cond == 4))
			{
				st.playSound(SOUND_FINISH);
				st.exitCurrentQuest(true);
				st.giveItems(FLOATING_STONE, 1);
			}
			else
			{
				htmltext = "watcher_valakas_klein_q0618_0104.htm";
			}
		}
		else if (event.equalsIgnoreCase("blacksmith_hilda_q0618_0201.htm") && (cond == 1))
		{
			st.setCond(2);
			st.playSound(SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("blacksmith_hilda_q0618_0301.htm"))
		{
			if ((cond == 3) && (st.getQuestItemsCount(VACUALITE_ORE) == 50))
			{
				st.takeItems(VACUALITE_ORE, -1);
				st.giveItems(VACUALITE, 1);
				st.setCond(4);
				st.playSound(SOUND_MIDDLE);
			}
			else
			{
				htmltext = "blacksmith_hilda_q0618_0203.htm";
			}
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int npcId = npc.getNpcId();
		int cond = st.getCond();
		if (npcId == KLEIN)
		{
			if (cond == 0)
			{
				if (st.getPlayer().getLevel() < 60)
				{
					htmltext = "watcher_valakas_klein_q0618_0103.htm";
					st.exitCurrentQuest(true);
				}
				else
				{
					htmltext = "watcher_valakas_klein_q0618_0101.htm";
				}
			}
			else if ((cond == 4) && (st.getQuestItemsCount(VACUALITE) > 0))
			{
				htmltext = "watcher_valakas_klein_q0618_0301.htm";
			}
			else
			{
				htmltext = "watcher_valakas_klein_q0618_0104.htm";
			}
		}
		else if (npcId == HILDA)
		{
			if (cond == 1)
			{
				htmltext = "blacksmith_hilda_q0618_0101.htm";
			}
			else if ((cond == 3) && (st.getQuestItemsCount(VACUALITE_ORE) >= 50))
			{
				htmltext = "blacksmith_hilda_q0618_0202.htm";
			}
			else if (cond == 4)
			{
				htmltext = "blacksmith_hilda_q0618_0303.htm";
			}
			else
			{
				htmltext = "blacksmith_hilda_q0618_0203.htm";
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		long count = st.getQuestItemsCount(VACUALITE_ORE);
		if (Rnd.chance(CHANCE_FOR_QUEST_ITEMS) && (count < 50))
		{
			st.giveItems(VACUALITE_ORE, 1);
			if (count == 49)
			{
				st.setCond(3);
				st.playSound(SOUND_MIDDLE);
			}
			else
			{
				st.playSound(SOUND_ITEMGET);
			}
		}
		return null;
	}
}
