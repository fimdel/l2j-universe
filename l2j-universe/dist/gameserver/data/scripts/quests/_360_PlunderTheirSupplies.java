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

public class _360_PlunderTheirSupplies extends Quest implements ScriptFile
{
	private static final int COLEMAN = 30873;
	private static final int TAIK_SEEKER = 20666;
	private static final int TAIK_LEADER = 20669;
	private static final int SUPPLY_ITEM = 5872;
	private static final int SUSPICIOUS_DOCUMENT = 5871;
	private static final int RECIPE_OF_SUPPLY = 5870;
	private static final int ITEM_DROP_SEEKER = 50;
	private static final int ITEM_DROP_LEADER = 65;
	private static final int DOCUMENT_DROP = 5;
	
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
	
	public _360_PlunderTheirSupplies()
	{
		super(false);
		addStartNpc(COLEMAN);
		addKillId(TAIK_SEEKER);
		addKillId(TAIK_LEADER);
		addQuestItem(SUPPLY_ITEM);
		addQuestItem(SUSPICIOUS_DOCUMENT);
		addQuestItem(RECIPE_OF_SUPPLY);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if (event.equalsIgnoreCase("guard_coleman_q0360_04.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("guard_coleman_q0360_10.htm"))
		{
			st.playSound(SOUND_FINISH);
			st.exitCurrentQuest(true);
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int id = st.getState();
		long docs = st.getQuestItemsCount(RECIPE_OF_SUPPLY);
		long supplies = st.getQuestItemsCount(SUPPLY_ITEM);
		if (id != STARTED)
		{
			if (st.getPlayer().getLevel() >= 52)
			{
				htmltext = "guard_coleman_q0360_02.htm";
			}
			else
			{
				htmltext = "guard_coleman_q0360_01.htm";
			}
		}
		else if ((docs > 0) || (supplies > 0))
		{
			long reward = 6000 + (supplies * 100) + (docs * 6000);
			st.takeItems(SUPPLY_ITEM, -1);
			st.takeItems(RECIPE_OF_SUPPLY, -1);
			st.giveItems(ADENA_ID, reward);
			htmltext = "guard_coleman_q0360_08.htm";
		}
		else
		{
			htmltext = "guard_coleman_q0360_05.htm";
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		int npcId = npc.getNpcId();
		if (((npcId == TAIK_SEEKER) && Rnd.chance(ITEM_DROP_SEEKER)) || ((npcId == TAIK_LEADER) && Rnd.chance(ITEM_DROP_LEADER)))
		{
			st.giveItems(SUPPLY_ITEM, 1);
			st.playSound(SOUND_ITEMGET);
		}
		if (Rnd.chance(DOCUMENT_DROP))
		{
			if (st.getQuestItemsCount(SUSPICIOUS_DOCUMENT) < 4)
			{
				st.giveItems(SUSPICIOUS_DOCUMENT, 1);
			}
			else
			{
				st.takeItems(SUSPICIOUS_DOCUMENT, -1);
				st.giveItems(RECIPE_OF_SUPPLY, 1);
			}
			st.playSound(SOUND_ITEMGET);
		}
		return null;
	}
}
