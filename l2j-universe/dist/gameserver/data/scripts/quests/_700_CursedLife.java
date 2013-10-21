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

public class _700_CursedLife extends Quest implements ScriptFile
{
	private static int Orbyu = 32560;
	private static int SwallowedSkull = 13872;
	private static int SwallowedSternum = 13873;
	private static int SwallowedBones = 13874;
	private static int MutantBird1 = 22602;
	private static int MutantBird2 = 22603;
	private static int DraHawk1 = 22604;
	private static int DraHawk2 = 22605;
	private static int Rok = 25624;
	private static int _skullprice = 50000;
	private static int _sternumprice = 5000;
	private static int _bonesprice = 500;
	
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
	
	public _700_CursedLife()
	{
		super(false);
		addStartNpc(Orbyu);
		addTalkId(Orbyu);
		addKillId(MutantBird1, MutantBird2, DraHawk1, DraHawk2);
		addQuestItem(SwallowedSkull, SwallowedSternum, SwallowedBones);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		int cond = st.getCond();
		String htmltext = event;
		if (event.equals("orbyu_q700_2.htm") && (cond == 0))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equals("ex_bones") && (cond == 1))
		{
			if ((st.getQuestItemsCount(SwallowedSkull) >= 1) || (st.getQuestItemsCount(SwallowedSternum) >= 1) || (st.getQuestItemsCount(SwallowedBones) >= 1))
			{
				long _adenatogive = (st.getQuestItemsCount(SwallowedSkull) * _skullprice) + (st.getQuestItemsCount(SwallowedSternum) * _sternumprice) + (st.getQuestItemsCount(SwallowedBones) * _bonesprice);
				st.giveItems(ADENA_ID, _adenatogive);
				if (st.getQuestItemsCount(SwallowedSkull) >= 1)
				{
					st.takeItems(SwallowedSkull, -1);
				}
				if (st.getQuestItemsCount(SwallowedSternum) >= 1)
				{
					st.takeItems(SwallowedSternum, -1);
				}
				if (st.getQuestItemsCount(SwallowedBones) >= 1)
				{
					st.takeItems(SwallowedBones, -1);
				}
				htmltext = "orbyu_q700_4.htm";
			}
			else
			{
				htmltext = "orbyu_q700_3a.htm";
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
		if (npcId == Orbyu)
		{
			if (cond == 0)
			{
				if ((st.getPlayer().getLevel() >= 75) && (GoodDayToFly != null) && GoodDayToFly.isCompleted())
				{
					htmltext = "orbyu_q700_1.htm";
				}
				else
				{
					htmltext = "orbyu_q700_0.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if (cond == 1)
			{
				if ((st.getQuestItemsCount(SwallowedSkull) >= 1) || (st.getQuestItemsCount(SwallowedSternum) >= 1) || (st.getQuestItemsCount(SwallowedBones) >= 1))
				{
					htmltext = "orbyu_q700_3.htm";
				}
				else
				{
					htmltext = "orbyu_q700_3a.htm";
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
		if (cond == 1)
		{
			if ((npcId == MutantBird1) || (npcId == MutantBird2) || (npcId == DraHawk1) || (npcId == DraHawk2))
			{
				st.giveItems(SwallowedBones, 1);
				st.playSound(SOUND_ITEMGET);
				if (Rnd.chance(20))
				{
					st.giveItems(SwallowedSkull, 1);
				}
				else if (Rnd.chance(20))
				{
					st.giveItems(SwallowedSternum, 1);
				}
			}
			else if (npcId == Rok)
			{
				st.giveItems(SwallowedSternum, 50);
				st.giveItems(SwallowedSkull, 30);
				st.giveItems(SwallowedBones, 100);
				st.playSound(SOUND_ITEMGET);
			}
		}
		return null;
	}
}
