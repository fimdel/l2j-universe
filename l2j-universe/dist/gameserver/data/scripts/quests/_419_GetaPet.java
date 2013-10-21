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

public class _419_GetaPet extends Quest implements ScriptFile
{
	private static final int PET_MANAGER_MARTIN = 30731;
	private static final int GK_BELLA = 30256;
	private static final int MC_ELLIE = 30091;
	private static final int GD_METTY = 30072;
	private static final int SPIDER_H1 = 22994;
	private static final int SPIDER_H2 = 22993;
	private static final int SPIDER_H3 = 22995;
	private static final int SPIDER_LE1 = 20460;
	private static final int SPIDER_LE2 = 20308;
	private static final int SPIDER_LE3 = 20466;
	private static final int SPIDER_DE1 = 20025;
	private static final int SPIDER_DE2 = 20105;
	private static final int SPIDER_DE3 = 20034;
	private static final int SPIDER_O1 = 20474;
	private static final int SPIDER_O2 = 20476;
	private static final int SPIDER_O3 = 20478;
	private static final int SPIDER_D1 = 20403;
	private static final int SPIDER_D2 = 20508;
	private static final int SPIDER_K1 = 22244;
	private static final int REQUIRED_SPIDER_LEGS = 50;
	private static final int ANIMAL_LOVERS_LIST1 = 3417;
	private static final int ANIMAL_SLAYER_LIST1 = 3418;
	private static final int ANIMAL_SLAYER_LIST2 = 3419;
	private static final int ANIMAL_SLAYER_LIST3 = 3420;
	private static final int ANIMAL_SLAYER_LIST4 = 3421;
	private static final int ANIMAL_SLAYER_LIST5 = 3422;
	private static final int ANIMAL_SLAYER_LIST6 = 10164;
	private static final int SPIDER_LEG1 = 3423;
	private static final int SPIDER_LEG2 = 3424;
	private static final int SPIDER_LEG3 = 3425;
	private static final int SPIDER_LEG4 = 3426;
	private static final int SPIDER_LEG5 = 3427;
	private static final int SPIDER_LEG6 = 10165;
	private static final int WOLF_COLLAR = 2375;
	@SuppressWarnings("unused")
	private static final int DROP_CHANCE_BUGBEAR_BLOOD_ID = 25;
	@SuppressWarnings("unused")
	private static final int DROP_CHANCE_FORBIDDEN_LOVE_SCROLL_ID = 3;
	@SuppressWarnings("unused")
	private static final int DROP_CHANCE_NECKLACE_OF_GRACE_ID = 4;
	@SuppressWarnings("unused")
	private static final int DROP_CHANCE_GOLD_BAR_ID = 10;
	private static final int[][] DROPLIST_COND =
	{
		{
			1,
			0,
			SPIDER_H1,
			ANIMAL_SLAYER_LIST1,
			SPIDER_LEG1,
			50,
			100,
			1
		},
		{
			1,
			0,
			SPIDER_H2,
			ANIMAL_SLAYER_LIST1,
			SPIDER_LEG1,
			50,
			100,
			1
		},
		{
			1,
			0,
			SPIDER_H3,
			ANIMAL_SLAYER_LIST1,
			SPIDER_LEG1,
			50,
			100,
			1
		},
		{
			1,
			0,
			SPIDER_LE1,
			ANIMAL_SLAYER_LIST2,
			SPIDER_LEG2,
			50,
			100,
			1
		},
		{
			1,
			0,
			SPIDER_LE2,
			ANIMAL_SLAYER_LIST2,
			SPIDER_LEG2,
			50,
			100,
			1
		},
		{
			1,
			0,
			SPIDER_LE3,
			ANIMAL_SLAYER_LIST2,
			SPIDER_LEG2,
			50,
			100,
			1
		},
		{
			1,
			0,
			SPIDER_DE1,
			ANIMAL_SLAYER_LIST3,
			SPIDER_LEG3,
			50,
			100,
			1
		},
		{
			1,
			0,
			SPIDER_DE2,
			ANIMAL_SLAYER_LIST3,
			SPIDER_LEG3,
			50,
			100,
			1
		},
		{
			1,
			0,
			SPIDER_DE3,
			ANIMAL_SLAYER_LIST3,
			SPIDER_LEG3,
			50,
			100,
			1
		},
		{
			1,
			0,
			SPIDER_O1,
			ANIMAL_SLAYER_LIST4,
			SPIDER_LEG4,
			50,
			100,
			1
		},
		{
			1,
			0,
			SPIDER_O2,
			ANIMAL_SLAYER_LIST4,
			SPIDER_LEG4,
			50,
			100,
			1
		},
		{
			1,
			0,
			SPIDER_O3,
			ANIMAL_SLAYER_LIST4,
			SPIDER_LEG4,
			50,
			100,
			1
		},
		{
			1,
			0,
			SPIDER_D1,
			ANIMAL_SLAYER_LIST5,
			SPIDER_LEG5,
			50,
			100,
			1
		},
		{
			1,
			0,
			SPIDER_D2,
			ANIMAL_SLAYER_LIST5,
			SPIDER_LEG5,
			50,
			100,
			1
		},
		{
			1,
			0,
			SPIDER_K1,
			ANIMAL_SLAYER_LIST6,
			SPIDER_LEG6,
			50,
			100,
			1
		}
	};
	
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
	
	public _419_GetaPet()
	{
		super(false);
		addStartNpc(PET_MANAGER_MARTIN);
		addTalkId(PET_MANAGER_MARTIN);
		addTalkId(GK_BELLA);
		addTalkId(MC_ELLIE);
		addTalkId(GD_METTY);
		addQuestItem(ANIMAL_LOVERS_LIST1);
		addQuestItem(ANIMAL_SLAYER_LIST2);
		addQuestItem(ANIMAL_SLAYER_LIST3);
		addQuestItem(ANIMAL_SLAYER_LIST4);
		addQuestItem(ANIMAL_SLAYER_LIST5);
		addQuestItem(ANIMAL_SLAYER_LIST6);
		addQuestItem(SPIDER_LEG1);
		addQuestItem(SPIDER_LEG2);
		addQuestItem(SPIDER_LEG3);
		addQuestItem(SPIDER_LEG4);
		addQuestItem(SPIDER_LEG5);
		addQuestItem(SPIDER_LEG6);
		for (int[] element : DROPLIST_COND)
		{
			addKillId(element[2]);
		}
	}
	
	public long getCount_proof(QuestState st)
	{
		long counts = 0;
		switch (st.getPlayer().getRace())
		{
			case human:
				counts = st.getQuestItemsCount(SPIDER_LEG1);
				break;
			case elf:
				counts = st.getQuestItemsCount(SPIDER_LEG2);
				break;
			case darkelf:
				counts = st.getQuestItemsCount(SPIDER_LEG3);
				break;
			case orc:
				counts = st.getQuestItemsCount(SPIDER_LEG4);
				break;
			case dwarf:
				counts = st.getQuestItemsCount(SPIDER_LEG5);
				break;
			case kamael:
				counts = st.getQuestItemsCount(SPIDER_LEG6);
		}
		return counts;
	}
	
	public String check_questions(QuestState st)
	{
		String htmltext = "";
		int answers = st.getInt("answers");
		int question = st.getInt("question");
		if (question > 0)
		{
			htmltext = "419_q" + String.valueOf(question) + ".htm";
		}
		else if (answers < 10)
		{
			String[] ANS = st.get("quiz").toString().split(" ");
			int GetQuestion = Rnd.get(ANS.length);
			String index = ANS[GetQuestion];
			st.set("question", index);
			String quiz = "";
			if ((GetQuestion + 1) == ANS.length)
			{
				for (int i = 0; i < (ANS.length - 2); i++)
				{
					quiz = quiz + ANS[i] + " ";
				}
				quiz = quiz + ANS[ANS.length - 2];
			}
			else
			{
				for (int i = 0; i < (ANS.length - 1); i++)
				{
					if (i != GetQuestion)
					{
						quiz = quiz + ANS[i] + " ";
					}
				}
				quiz = quiz + ANS[ANS.length - 1];
			}
			st.set("quiz", quiz);
			htmltext = "419_q" + index + ".htm";
		}
		else
		{
			st.giveItems(WOLF_COLLAR, 1);
			st.playSound(SOUND_FINISH);
			htmltext = "Completed.htm";
			st.exitCurrentQuest(true);
		}
		return htmltext;
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		int StateId = st.getInt("id");
		if (event.equalsIgnoreCase("details"))
		{
			htmltext = "419_confirm.htm";
		}
		else if (event.equalsIgnoreCase("agree"))
		{
			st.setState(STARTED);
			st.setCond(1);
			switch (st.getPlayer().getRace())
			{
				case human:
					st.giveItems(ANIMAL_SLAYER_LIST1, 1);
					st.addRadarWithMap(-111768, 233016, -3180);
					st.addRadar(-111768, 233016, -3180);
					htmltext = "419_slay_0.htm";
					break;
				case elf:
					st.giveItems(ANIMAL_SLAYER_LIST2, 1);
					htmltext = "419_slay_1.htm";
					break;
				case darkelf:
					st.giveItems(ANIMAL_SLAYER_LIST3, 1);
					htmltext = "419_slay_2.htm";
					break;
				case orc:
					st.giveItems(ANIMAL_SLAYER_LIST4, 1);
					htmltext = "419_slay_3.htm";
					break;
				case dwarf:
					st.giveItems(ANIMAL_SLAYER_LIST5, 1);
					htmltext = "419_slay_4.htm";
					break;
				case kamael:
					st.giveItems(ANIMAL_SLAYER_LIST6, 1);
					htmltext = "419_slay_5.htm";
			}
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("disagree"))
		{
			htmltext = "419_cancelled.htm";
			st.exitCurrentQuest(true);
		}
		else if (StateId == 1)
		{
			if (event.equalsIgnoreCase("talk"))
			{
				htmltext = "419_talk.htm";
			}
			else if (event.equalsIgnoreCase("talk1"))
			{
				htmltext = "419_bella_2.htm";
			}
			else if (event.equalsIgnoreCase("talk2"))
			{
				st.set("progress", String.valueOf(st.getInt("progress") | 1));
				htmltext = "419_bella_3.htm";
			}
			else if (event.equalsIgnoreCase("talk3"))
			{
				st.set("progress", String.valueOf(st.getInt("progress") | 2));
				htmltext = "419_ellie_2.htm";
			}
			else if (event.equalsIgnoreCase("talk4"))
			{
				st.set("progress", String.valueOf(st.getInt("progress") | 4));
				htmltext = "419_metty_2.htm";
			}
		}
		else if (StateId == 2)
		{
			if (event.equalsIgnoreCase("tryme"))
			{
				htmltext = check_questions(st);
			}
			else if (event.equalsIgnoreCase("wrong"))
			{
				st.set("id", "1");
				st.set("progress", "0");
				st.unset("quiz");
				st.unset("answers");
				st.unset("question");
				st.giveItems(ANIMAL_LOVERS_LIST1, 1);
				htmltext = "419_failed.htm";
			}
			else if (event.equalsIgnoreCase("right"))
			{
				st.set("answers", String.valueOf(st.getInt("answers") + 1));
				st.set("question", "0");
				htmltext = check_questions(st);
			}
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		int npcId = npc.getNpcId();
		String htmltext = "noquest";
		int StateId = st.getInt("id");
		int cond = st.getCond();
		if (cond == 0)
		{
			if (npcId == PET_MANAGER_MARTIN)
			{
				if (st.getPlayer().getLevel() < 15)
				{
					htmltext = "419_low_level.htm";
					st.exitCurrentQuest(true);
				}
				else
				{
					htmltext = "Start.htm";
				}
			}
		}
		else if (cond == 1)
		{
			if (npcId == PET_MANAGER_MARTIN)
			{
				if (StateId == 0)
				{
					long counts = getCount_proof(st);
					if (counts == 0)
					{
						htmltext = "419_no_slay.htm";
					}
					else if (counts < REQUIRED_SPIDER_LEGS)
					{
						htmltext = "419_pending_slay.htm";
					}
					else
					{
						switch (st.getPlayer().getRace())
						{
							case human:
								st.takeItems(ANIMAL_SLAYER_LIST1, -1);
								st.takeItems(SPIDER_LEG1, -1);
								break;
							case elf:
								st.takeItems(ANIMAL_SLAYER_LIST2, -1);
								st.takeItems(SPIDER_LEG2, -1);
								break;
							case darkelf:
								st.takeItems(ANIMAL_SLAYER_LIST3, -1);
								st.takeItems(SPIDER_LEG3, -1);
								break;
							case orc:
								st.takeItems(ANIMAL_SLAYER_LIST4, -1);
								st.takeItems(SPIDER_LEG4, -1);
								break;
							case dwarf:
								st.takeItems(ANIMAL_SLAYER_LIST5, -1);
								st.takeItems(SPIDER_LEG5, -1);
								break;
							case kamael:
								st.takeItems(ANIMAL_SLAYER_LIST6, -1);
								st.takeItems(SPIDER_LEG6, -1);
						}
						st.set("id", "1");
						st.giveItems(ANIMAL_LOVERS_LIST1, 1);
						htmltext = "Slayed.htm";
					}
				}
				else if (StateId == 1)
				{
					if (st.getInt("progress") == 7)
					{
						st.takeItems(ANIMAL_LOVERS_LIST1, -1);
						st.set("quiz", "1 2 3 4 5 6 7 8 9 10 11 12 13 14 15");
						st.set("answers", "0");
						st.set("id", "2");
						htmltext = "Talked.htm";
					}
					else
					{
						htmltext = "419_pending_talk.htm";
					}
				}
				else if (StateId == 2)
				{
					htmltext = "Talked.htm";
				}
			}
			else if (StateId == 1)
			{
				if (npcId == GK_BELLA)
				{
					htmltext = "419_bella_1.htm";
				}
				else if (npcId == MC_ELLIE)
				{
					htmltext = "419_ellie_1.htm";
				}
				else if (npcId == GD_METTY)
				{
					htmltext = "419_metty_1.htm";
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
		for (int[] element : DROPLIST_COND)
		{
			if ((cond == element[0]) && (npcId == element[2]))
			{
				if ((element[3] == 0) || (st.getQuestItemsCount(element[3]) > 0))
				{
					if (element[5] == 0)
					{
						st.rollAndGive(element[4], element[7], element[6]);
					}
					else if (st.rollAndGive(element[4], element[7], element[7], element[5], element[6]))
					{
						if ((element[1] != cond) && (element[1] != 0))
						{
							st.setCond(Integer.valueOf(element[1]));
							st.setState(STARTED);
						}
					}
				}
			}
		}
		return null;
	}
}
