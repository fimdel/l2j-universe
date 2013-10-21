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

public class _308_ReedFieldMaintenance extends Quest implements ScriptFile
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
	
	private static final int Katensa = 32646;
	private static final int MucrokianHide = 14871;
	private static final int AwakenMucrokianHide = 14872;
	private static final int MucrokianFanatic = 22650;
	private static final int MucrokianAscetic = 22651;
	private static final int MucrokianSavior = 22652;
	private static final int MucrokianPreacher = 22653;
	private static final int ContaminatedMucrokian = 22654;
	private static final int ChangedMucrokian = 22655;
	private static final int[] MoiraiRecipes =
	{
		15777,
		15780,
		15783,
		15786,
		15789,
		15790,
		15812,
		15813,
		15814
	};
	private static final int[] Moiraimaterials =
	{
		15647,
		15650,
		15653,
		15656,
		15659,
		15692,
		15772,
		15773,
		15774
	};
	
	public _308_ReedFieldMaintenance()
	{
		super(false);
		addStartNpc(Katensa);
		addQuestItem(MucrokianHide, AwakenMucrokianHide);
		addKillId(MucrokianFanatic, MucrokianAscetic, MucrokianSavior, MucrokianPreacher, ContaminatedMucrokian, ChangedMucrokian);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if (event.equalsIgnoreCase("32646-04.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
		}
		else if (event.equalsIgnoreCase("32646-11.htm"))
		{
			st.exitCurrentQuest(true);
		}
		else if (event.equalsIgnoreCase("moirairec"))
		{
			if (st.getQuestItemsCount(MucrokianHide) >= 180)
			{
				st.takeItems(MucrokianHide, 180);
				st.giveItems(MoiraiRecipes[Rnd.get(MoiraiRecipes.length - 1)], 1);
				return null;
			}
			htmltext = "32646-16.htm";
		}
		else if (event.equalsIgnoreCase("moiraimat"))
		{
			if (st.getQuestItemsCount(MucrokianHide) >= 100)
			{
				st.takeItems(MucrokianHide, 100);
				st.giveItems(Moiraimaterials[Rnd.get(Moiraimaterials.length - 1)], 1);
				return null;
			}
			htmltext = "32646-16.htm";
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int npcId = npc.getNpcId();
		int id = st.getState();
		int cond = st.getCond();
		if (npcId == Katensa)
		{
			if (id == CREATED)
			{
				QuestState qs1 = st.getPlayer().getQuestState(_309_ForAGoodCause.class);
				if ((qs1 != null) && qs1.isStarted())
				{
					return "32646-15.htm";
				}
				if (st.getPlayer().getLevel() < 82)
				{
					return "32646-00.htm";
				}
				return "32646-01.htm";
			}
			else if (cond == 1)
			{
				long awaken = st.takeAllItems(AwakenMucrokianHide);
				if (awaken > 0)
				{
					st.giveItems(MucrokianHide, awaken * 2);
				}
				if (st.getQuestItemsCount(MucrokianHide) == 0)
				{
					return "32646-05.htm";
				}
				else if (!st.getPlayer().isQuestCompleted(_238_SuccessFailureOfBusiness.class))
				{
					return "32646-a1.htm";
				}
				else
				{
					return "32646-a2.htm";
				}
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		st.rollAndGive(npc.getNpcId() == ChangedMucrokian ? AwakenMucrokianHide : MucrokianHide, 1, 60);
		return null;
	}
}
