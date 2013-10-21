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

import org.apache.commons.lang3.ArrayUtils;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class _474_WaitingForTheSummer extends Quest implements ScriptFile
{
	//npc
	private static final int GUIDE = 33463;
	private static final int VISHOTSKY = 31981;

	//q items
	private static final int BUFFALO_MEAT = 19490;
	private static final int URSUS_MEAT = 19491;
	private static final int YETI_MIAT = 19492;

	//mobs
	private static final int[] BUFFALO = {22093, 22094};
	private static final int[] URSUS = {22095, 22096};
	private static final int[] YETI = {22097, 22098};

	@Override
	public void onLoad()
	{}
	
	@Override
	public void onReload()
	{}
	
	@Override
	public void onShutdown()
	{}
	
	public _474_WaitingForTheSummer()
	{
		super(false);
		addStartNpc(GUIDE);
		addTalkId(VISHOTSKY);
		addKillId(BUFFALO);
		addKillId(URSUS);
		addKillId(YETI);
		addQuestItem(BUFFALO_MEAT, YETI_MIAT, URSUS_MEAT);
		addLevelCheck(60, 64);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		st.getPlayer();
		if(event.equalsIgnoreCase("33463-3.htm"))
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
		st.getPlayer();
		int npcId = npc.getNpcId();
		int state = st.getState();
		int cond = st.getCond();
		if(npcId == GUIDE)
		{
			if(state == 1)
			{
				if(!st.isNowAvailableByTime())
				{
					return "33463-comp.htm";
				}
				return "33463.htm";
			}
			if(state == 2)
			{
				if(cond == 1)
					return "33463-4.htm";
					
			}
		}
		if(npcId == VISHOTSKY && state == 2)
		{
			if(cond == 1)
				return "31981-1.htm";
			if(cond == 2)
			{
				st.giveItems(57,194000);
				st.addExpAndSp(1879400, 1782000);
				st.takeItems(BUFFALO_MEAT, -1);
				st.takeItems(URSUS_MEAT, -1);
				st.takeItems(YETI_MIAT, -1);
				st.unset("cond");
				st.playSound(SOUND_FINISH);
				st.exitCurrentQuest(this);		
				return "31981.htm"; //no further html do here
			}	
		}		
		return "noquest";
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		int npcId = npc.getNpcId();
		int cond = st.getCond();
		if ((cond == 1) && (Rnd.chance(50)))
		{
			if (ArrayUtils.contains(YETI, npcId))
			{
				if (st.getQuestItemsCount(YETI_MIAT) < 30)
				{
					st.giveItems(YETI_MIAT, 1);
					st.playSound("ItemSound.quest_itemget");
				}
			}
			else if (ArrayUtils.contains(URSUS, npcId))
			{
				if (st.getQuestItemsCount(URSUS_MEAT) < 30)
				{
					st.giveItems(URSUS_MEAT, 1);
					st.playSound("ItemSound.quest_itemget");
				}
			}
			else if (ArrayUtils.contains(BUFFALO, npcId))
			{
				if (st.getQuestItemsCount(BUFFALO_MEAT) < 30)
				{
					st.giveItems(BUFFALO_MEAT, 1);
					st.playSound("ItemSound.quest_itemget");
				}
			}
			if ((st.getQuestItemsCount(YETI_MIAT) >= 30) && (st.getQuestItemsCount(URSUS_MEAT) >= 30) && (st.getQuestItemsCount(BUFFALO_MEAT) >= 30))
			{
				st.setCond(2);
				st.playSound("ItemSound.quest_middle");
			}
		}
		return null;
	}
	
	public boolean isDailyQuest()
	{
		return true;
	}
}
