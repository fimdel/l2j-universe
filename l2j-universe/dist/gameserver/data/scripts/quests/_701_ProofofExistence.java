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

public class _701_ProofofExistence extends Quest implements ScriptFile
{
	private static int Artius = 32559;
	private static int DeadmansRemains = 13875;
	private static int BansheeQueensEye = 13876;
	private static int Enira = 25625;
	private static int FloatingSkull1 = 22606;
	private static int FloatingSkull2 = 22607;
	private static int FloatingZombie1 = 22608;
	private static int FloatingZombie2 = 22609;
	
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
	
	public _701_ProofofExistence()
	{
		super(false);
		addStartNpc(Artius);
		addTalkId(Artius);
		addKillId(Enira, FloatingSkull1, FloatingSkull2, FloatingZombie1, FloatingZombie2);
		addQuestItem(DeadmansRemains, BansheeQueensEye);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		int cond = st.getCond();
		String htmltext = event;
		if (event.equals("artius_q701_2.htm") && (cond == 0))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equals("ex_mons") && (cond == 1))
		{
			if (st.getQuestItemsCount(DeadmansRemains) >= 1)
			{
				st.giveItems(ADENA_ID, st.getQuestItemsCount(DeadmansRemains) * 2500);
				st.takeItems(DeadmansRemains, -1);
				htmltext = "artius_q701_4.htm";
			}
			else
			{
				htmltext = "artius_q701_3a.htm";
			}
		}
		else if (event.equals("ex_boss") && (cond == 1))
		{
			if (st.getQuestItemsCount(BansheeQueensEye) >= 1)
			{
				st.giveItems(ADENA_ID, st.getQuestItemsCount(BansheeQueensEye) * 1000000);
				st.takeItems(BansheeQueensEye, -1);
				htmltext = "artius_q701_4.htm";
			}
			else
			{
				htmltext = "artius_q701_3a.htm";
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
		QuestState GoodDayToFly = st.getPlayer().getQuestState(_10273_GoodDayToFly.class);
		if (npcId == Artius)
		{
			if (cond == 0)
			{
				if ((st.getPlayer().getLevel() >= 78) && (GoodDayToFly != null) && GoodDayToFly.isCompleted())
				{
					htmltext = "artius_q701_1.htm";
				}
				else
				{
					htmltext = "artius_q701_0.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if (cond == 1)
			{
				if ((st.getQuestItemsCount(DeadmansRemains) >= 1) || (st.getQuestItemsCount(BansheeQueensEye) >= 1))
				{
					htmltext = "artius_q701_3.htm";
				}
				else
				{
					htmltext = "artius_q701_3a.htm";
				}
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		int npcId = npc.getNpcId();
		int cond = st.getCond();
		if ((cond == 1) && (npcId != Enira))
		{
			st.giveItems(DeadmansRemains, 1);
			st.playSound(SOUND_ITEMGET);
		}
		else if ((cond == 1) && (npcId == Enira))
		{
			st.giveItems(BansheeQueensEye, 1);
			st.playSound(SOUND_ITEMGET);
		}
		return null;
	}
}
