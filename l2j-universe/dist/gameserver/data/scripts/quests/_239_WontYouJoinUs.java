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

import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class _239_WontYouJoinUs extends Quest implements ScriptFile
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
	
	private static final int Athenia = 32643;
	private static final int WasteLandfillMachine = 18805;
	private static final int Suppressor = 22656;
	private static final int Exterminator = 22657;
	private static final int CertificateOfSupport = 14866;
	private static final int DestroyedMachinePiece = 14869;
	private static final int EnchantedGolemFragment = 14870;
	
	public _239_WontYouJoinUs()
	{
		super(false);
		addStartNpc(Athenia);
		addKillId(WasteLandfillMachine, Suppressor, Exterminator);
		addQuestItem(DestroyedMachinePiece, EnchantedGolemFragment);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if (event.equalsIgnoreCase("32643-03.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
		}
		if (event.equalsIgnoreCase("32643-07.htm"))
		{
			st.takeAllItems(DestroyedMachinePiece);
			st.setCond(3);
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
		if (npcId == Athenia)
		{
			if (id == CREATED)
			{
				if ((st.getPlayer().getLevel() < 82) || !st.getPlayer().isQuestCompleted(_237_WindsOfChange.class))
				{
					return "32643-00.htm";
				}
				if (st.getQuestItemsCount(CertificateOfSupport) == 0)
				{
					return "32643-12.htm";
				}
				return "32643-01.htm";
			}
			else if (id == COMPLETED)
			{
				return "32643-11.htm";
			}
			else if (cond == 1)
			{
				return "32643-04.htm";
			}
			else if (cond == 2)
			{
				return "32643-06.htm";
			}
			else if (cond == 3)
			{
				return "32643-08.htm";
			}
			else if (cond == 4)
			{
				st.takeAllItems(CertificateOfSupport);
				st.takeAllItems(EnchantedGolemFragment);
				st.giveItems(ADENA_ID, 4498920);
				st.addExpAndSp(21843270, 25080120);
				st.setState(COMPLETED);
				st.exitCurrentQuest(false);
				return "32643-10.htm";
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		int cond = st.getCond();
		if ((cond == 1) && (npc.getNpcId() == WasteLandfillMachine))
		{
			st.giveItems(DestroyedMachinePiece, 1);
			if (st.getQuestItemsCount(DestroyedMachinePiece) >= 10)
			{
				st.setCond(2);
			}
		}
		else if ((cond == 3) && ((npc.getNpcId() == Suppressor) || (npc.getNpcId() == Exterminator)))
		{
			st.giveItems(EnchantedGolemFragment, 1);
			if (st.getQuestItemsCount(EnchantedGolemFragment) >= 20)
			{
				st.setCond(4);
			}
		}
		return null;
	}
}
