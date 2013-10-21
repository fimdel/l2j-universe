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

import gnu.trove.map.hash.TIntIntHashMap;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class _10304_ForForgottenHeroes extends Quest implements ScriptFile
{
	private static final int CON1 = 32894;
	private static final int CON2 = 25837;
	private static final int CON3 = 25838;
	private static final int CON4 = 25839;
	private static final int CON5 = 25840;
	private static final int CON6 = 25841;
	private static final int CON7 = 25843;
	private static final int CON8 = 25846;
	private static final int CON9 = 25824;
	@SuppressWarnings("unused")
	private static final int CON10 = 34033;
	private static final int CON11 = 34034;
	private static final int CON12 = 33467;
	private static final int CON13 = 33466;
	private static final int CON14 = 32779;
	
	public _10304_ForForgottenHeroes()
	{
		super(false);
		addStartNpc(CON1);
		addTalkId(CON1);
		addKillId(CON2, CON3, CON4, CON5, CON6, CON7, CON8, CON9);
		addQuestItem(CON11);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		if (st == null)
		{
			return event;
		}
		if (event.equalsIgnoreCase("32894-01.htm"))
		{
			st.takeItems(34034, -1L);
			st.setCond(2);
		}
		else
		{
			if (event.equalsIgnoreCase("enter"))
			{
				if ((st.getPlayer().isInParty()) && (st.getPlayer().getParty().getMemberCount() == 7))
				{
					return "32894-enter.htm";
				}
				return "32894-no7.htm";
			}
			if (event.equalsIgnoreCase("32894-04.htm"))
			{
				st.unset("one");
				st.unset("two");
				st.unset("three");
				st.unset("four");
				st.unset("five");
				st.unset("six");
				st.unset("seven");
				st.unset("eight");
				st.addExpAndSp(15197798, 6502166);
				st.getPlayer().addAdena(47085998, true);
				st.giveItems(CON12, 1);
				st.giveItems(CON13, 1);
				st.giveItems(CON14, 1);
				st.playSound("ItemSound.quest_finish");
				st.exitCurrentQuest(false);
			}
		}
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		QuestState prevst = st.getPlayer().getQuestState("10302_TheShadowOfAnxiety");
		if (npc.getNpcId() == 32894)
		{
			switch (st.getState())
			{
				case 2:
					htmltext = "32894-completed.htm";
					break;
				case 1:
					switch (st.getCond())
					{
						case 1:
							if (st.getPlayer().getLevel() >= 90)
							{
								if ((prevst != null) && (prevst.isCompleted()))
								{
									htmltext = "32894-00.htm";
								}
								else
								{
									htmltext = "32894-noprev.htm";
									st.exitCurrentQuest(true);
								}
							}
							else
							{
								htmltext = "32894-nolvl.htm";
								st.exitCurrentQuest(true);
							}
							break;
						case 2:
							htmltext = "32894-01a.htm";
							break;
						case 3:
						case 4:
						case 5:
						case 6:
						case 7:
						case 8:
							htmltext = "32894-02.htm";
							break;
						case 9:
							htmltext = "32894-03.htm";
					}
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		TIntIntHashMap moblist = new TIntIntHashMap();
		if ((npc == null) || (st == null))
		{
			return null;
		}
		int cond = st.getCond();
		if ((cond >= 2) && (cond < 9))
		{
			int ONE = st.getInt("one");
			int TWO = st.getInt("two");
			int THREE = st.getInt("three");
			int FOUR = st.getInt("four");
			int FIVE = st.getInt("five");
			int SIX = st.getInt("six");
			int SEVEN = st.getInt("seven");
			int EIGHT = st.getInt("eight");
			if ((npc.getNpcId() == 25837) && (cond == 2))
			{
				ONE++;
				st.set("one", String.valueOf(ONE));
				st.setCond(3);
				st.playSound("ItemSound.quest_middle");
			}
			else if ((npc.getNpcId() == 25840) && (cond == 3))
			{
				TWO++;
				st.set("two", String.valueOf(TWO));
				st.setCond(4);
				st.playSound("ItemSound.quest_middle");
			}
			else if ((npc.getNpcId() == 25843) && (cond == 4))
			{
				THREE++;
				st.set("three", String.valueOf(THREE));
				st.setCond(5);
				st.playSound("ItemSound.quest_middle");
			}
			else if ((npc.getNpcId() == 25841) && (cond == 5))
			{
				FOUR++;
				st.set("four", String.valueOf(FOUR));
				st.setCond(6);
				st.playSound("ItemSound.quest_middle");
			}
			if (cond == 6)
			{
				if (npc.getNpcId() == 25838)
				{
					FIVE++;
					st.set("five", String.valueOf(FIVE));
				}
				else if (npc.getNpcId() == 25839)
				{
					SIX++;
					st.set("six", String.valueOf(SIX));
				}
				if ((FIVE > 0) && (SIX > 0))
				{
					st.setCond(7);
					st.playSound("ItemSound.quest_middle");
				}
			}
			else if ((npc.getNpcId() == 25846) && (cond == 7))
			{
				SEVEN++;
				st.set("seven", String.valueOf(SEVEN));
				st.setCond(8);
				st.playSound("ItemSound.quest_middle");
			}
			else if ((npc.getNpcId() == 25824) && (cond == 8))
			{
				EIGHT++;
				st.set("eight", String.valueOf(EIGHT));
				st.setCond(9);
				st.playSound("ItemSound.quest_middle");
			}
			moblist.put(25837, ONE);
			moblist.put(25840, TWO);
			moblist.put(25845, THREE);
			moblist.put(25841, FOUR);
			moblist.put(25838, FIVE);
			moblist.put(25839, SIX);
			moblist.put(25846, SEVEN);
			moblist.put(25825, EIGHT);
		}
		return null;
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
