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

import java.util.HashMap;
import java.util.Map;

import lineage2.gameserver.instancemanager.QuestManager;
import lineage2.gameserver.listener.actor.OnCurrentHpDamageListener;
import lineage2.gameserver.listener.actor.player.OnPlayerEnterListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.actor.listener.CharListenerList;
import lineage2.gameserver.model.base.Race;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.TutorialCloseHtml;
import lineage2.gameserver.network.serverpackets.TutorialShowHtml;
import lineage2.gameserver.scripts.ScriptFile;

public class _255_Tutorial extends Quest implements ScriptFile, OnPlayerEnterListener
{
	public final String[][] QMCa =
	{
		{
			"0",
			"tutorial_fighter017.htm",
			"-83165",
			"242711",
			"-3720"
		},
		{
			"10",
			"tutorial_mage017.htm",
			"-85247",
			"244718",
			"-3720"
		},
		{
			"18",
			"tutorial_fighter017.htm",
			"45610",
			"52206",
			"-2792"
		},
		{
			"25",
			"tutorial_mage017.htm",
			"45610",
			"52206",
			"-2792"
		},
		{
			"31",
			"tutorial_fighter017.htm",
			"10344",
			"14445",
			"-4242"
		},
		{
			"38",
			"tutorial_mage017.htm",
			"10344",
			"14445",
			"-4242"
		},
		{
			"44",
			"tutorial_fighter017.htm",
			"-46324",
			"-114384",
			"-200"
		},
		{
			"49",
			"tutorial_fighter017.htm",
			"-46305",
			"-112763",
			"-200"
		},
		{
			"53",
			"tutorial_fighter017.htm",
			"115447",
			"-182672",
			"-1440"
		},
		{
			"123",
			"tutorial_fighter017.htm",
			"-118132",
			"42788",
			"723"
		},
		{
			"124",
			"tutorial_fighter017.htm",
			"-118132",
			"42788",
			"723"
		}
	};
	public final Map<Integer, String> QMCb = new HashMap<>();
	public final Map<Integer, String> QMCc = new HashMap<>();
	public final Map<Integer, String> TCLa = new HashMap<>();
	public final Map<Integer, String> TCLb = new HashMap<>();
	public final Map<Integer, String> TCLc = new HashMap<>();
	static TutorialShowListener _tutorialShowListener;
	
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
	
	public _255_Tutorial()
	{
		super(false);
		CharListenerList.addGlobal(this);
		_tutorialShowListener = new TutorialShowListener();
		QMCb.put(0, "tutorial_human009.htm");
		QMCb.put(10, "tutorial_human009.htm");
		QMCb.put(18, "tutorial_elf009.htm");
		QMCb.put(25, "tutorial_elf009.htm");
		QMCb.put(31, "tutorial_delf009.htm");
		QMCb.put(38, "tutorial_delf009.htm");
		QMCb.put(44, "tutorial_orc009.htm");
		QMCb.put(49, "tutorial_orc009.htm");
		QMCb.put(53, "tutorial_dwarven009.htm");
		QMCb.put(123, "tutorial_kamael009.htm");
		QMCb.put(124, "tutorial_kamael009.htm");
		QMCc.put(0, "tutorial_1st_ct_human.htm");
		QMCc.put(1, "tutorial_1st_ct_elf.htm");
		QMCc.put(2, "tutorial_1st_ct_dark_elf.htm");
		QMCc.put(3, "tutorial_1st_ct_orc.htm");
		QMCc.put(4, "tutorial_1st_ct_dwarf.htm");
		QMCc.put(5, "tutorial_1st_ct_kamael.htm");
		TCLa.put(1, "tutorial_22w.htm");
		TCLa.put(4, "tutorial_22.htm");
		TCLa.put(7, "tutorial_22b.htm");
		TCLa.put(11, "tutorial_22c.htm");
		TCLa.put(15, "tutorial_22d.htm");
		TCLa.put(19, "tutorial_22e.htm");
		TCLa.put(22, "tutorial_22f.htm");
		TCLa.put(26, "tutorial_22g.htm");
		TCLa.put(29, "tutorial_22h.htm");
		TCLa.put(32, "tutorial_22n.htm");
		TCLa.put(35, "tutorial_22o.htm");
		TCLa.put(39, "tutorial_22p.htm");
		TCLa.put(42, "tutorial_22q.htm");
		TCLa.put(45, "tutorial_22i.htm");
		TCLa.put(47, "tutorial_22j.htm");
		TCLa.put(50, "tutorial_22k.htm");
		TCLa.put(54, "tutorial_22l.htm");
		TCLa.put(56, "tutorial_22m.htm");
		TCLb.put(4, "tutorial_22aa.htm");
		TCLb.put(7, "tutorial_22ba.htm");
		TCLb.put(11, "tutorial_22ca.htm");
		TCLb.put(15, "tutorial_22da.htm");
		TCLb.put(19, "tutorial_22ea.htm");
		TCLb.put(22, "tutorial_22fa.htm");
		TCLb.put(26, "tutorial_22ga.htm");
		TCLb.put(32, "tutorial_22na.htm");
		TCLb.put(35, "tutorial_22oa.htm");
		TCLb.put(39, "tutorial_22pa.htm");
		TCLb.put(50, "tutorial_22ka.htm");
		TCLc.put(4, "tutorial_22ab.htm");
		TCLc.put(7, "tutorial_22bb.htm");
		TCLc.put(11, "tutorial_22cb.htm");
		TCLc.put(15, "tutorial_22db.htm");
		TCLc.put(19, "tutorial_22eb.htm");
		TCLc.put(22, "tutorial_22fb.htm");
		TCLc.put(26, "tutorial_22gb.htm");
		TCLc.put(32, "tutorial_22nb.htm");
		TCLc.put(35, "tutorial_22ob.htm");
		TCLc.put(39, "tutorial_22pb.htm");
		TCLc.put(50, "tutorial_22kb.htm");
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		Player player = st.getPlayer();
		if (player == null)
		{
			return null;
		}
		String html = "";
		int classId = player.getActiveClassId();
		int Ex = st.getInt("Ex");
		if (event.startsWith("UC"))
		{
			int level = player.getLevel();
			if ((level < 6) && (st.getInt("onlyone") == 0))
			{
				int uc = st.getInt("ucMemo");
				if (uc == 0)
				{
					st.set("ucMemo", "0");
					st.startQuestTimer("QT", 10000);
					st.set("Ex", "-2");
				}
				else if (uc == 1)
				{
					st.showQuestionMark(1);
					st.playTutorialVoice("tutorial_voice_006");
					st.playSound(SOUND_TUTORIAL);
				}
				else if (uc == 2)
				{
					if (Ex == 2)
					{
						st.showQuestionMark(3);
						st.playSound(SOUND_TUTORIAL);
					}
					else if (st.getQuestItemsCount(6353) > 0)
					{
						st.showQuestionMark(5);
						st.playSound(SOUND_TUTORIAL);
					}
				}
				else if (uc == 3)
				{
					st.showQuestionMark(12);
					st.playSound(SOUND_TUTORIAL);
					st.onTutorialClientEvent(0);
				}
			}
			else if ((level >= 18) && (player.getClassLevel() == 0))
			{
				st.showQuestionMark(35);
				st.playSound(SOUND_TUTORIAL);
			}
			else if ((level == 20) && (player.getQuestState("_10276_MutatedKaneusGludio") == null))
			{
				st.showQuestionMark(36);
				st.playSound(SOUND_TUTORIAL);
			}
			else if ((level == 28) && (player.getQuestState("_10277_MutatedKaneusDion") == null))
			{
				st.showQuestionMark(36);
				st.playSound(SOUND_TUTORIAL);
			}
			else if ((level == 28) && (player.getQuestState("_10278_MutatedKaneusHeine") == null))
			{
				st.showQuestionMark(36);
				st.playSound(SOUND_TUTORIAL);
			}
			else if ((level == 28) && (player.getQuestState("_10279_MutatedKaneusOren") == null))
			{
				st.showQuestionMark(36);
				st.playSound(SOUND_TUTORIAL);
			}
			else if ((level == 28) && (player.getQuestState("_10280_MutatedKaneusSchuttgart") == null))
			{
				st.showQuestionMark(36);
				st.playSound(SOUND_TUTORIAL);
			}
			else if ((level == 28) && (player.getQuestState("_10281_MutatedKaneusRune") == null))
			{
				st.showQuestionMark(36);
				st.playSound(SOUND_TUTORIAL);
			}
			else if ((level == 79) && (player.getQuestState("_192_SevenSignSeriesOfDoubt") == null))
			{
				st.showQuestionMark(36);
				st.playSound(SOUND_TUTORIAL);
			}
		}
		else if (event.startsWith("QT"))
		{
			if (Ex == -2)
			{
				html = "tutorial_00.htm";
				st.set("Ex", "-3");
				st.cancelQuestTimer("QT");
				st.startQuestTimer("QT", 30000);
			}
			else if (Ex == -3)
			{
				st.playTutorialVoice("tutorial_voice_002");
				st.set("Ex", "0");
			}
			else if (Ex == -4)
			{
				st.playTutorialVoice("tutorial_voice_008");
				st.set("Ex", "-5");
			}
		}
		else if (event.startsWith("TE"))
		{
			st.cancelQuestTimer("TE");
			int event_id = 0;
			if (!event.equalsIgnoreCase("TE"))
			{
				event_id = Integer.valueOf(event.substring(2));
			}
			if (event_id == 0)
			{
				player.sendPacket(TutorialCloseHtml.STATIC);
			}
			else if (event_id == 1)
			{
				player.sendPacket(TutorialCloseHtml.STATIC);
				st.playTutorialVoice("tutorial_voice_006");
				st.showQuestionMark(1);
				st.playSound(SOUND_TUTORIAL);
				st.startQuestTimer("QT", 30000);
				st.set("Ex", "-4");
			}
			else if (event_id == 2)
			{
				st.playTutorialVoice("tutorial_voice_003");
				html = "tutorial_02.htm";
				st.onTutorialClientEvent(1);
				st.set("Ex", "-5");
			}
			else if (event_id == 3)
			{
				html = "tutorial_03.htm";
				st.onTutorialClientEvent(2);
			}
			else if (event_id == 5)
			{
				html = "tutorial_05.htm";
				st.onTutorialClientEvent(8);
			}
			else if (event_id == 7)
			{
				html = "tutorial_100.htm";
				st.onTutorialClientEvent(0);
			}
			else if (event_id == 8)
			{
				html = "tutorial_101.htm";
				st.onTutorialClientEvent(0);
			}
			else if (event_id == 10)
			{
				html = "tutorial_103.htm";
				st.onTutorialClientEvent(0);
			}
			else if (event_id == 12)
			{
				player.sendPacket(TutorialCloseHtml.STATIC);
			}
			else if ((event_id == 23) && TCLb.containsKey(classId))
			{
				html = TCLb.get(classId);
			}
			else if ((event_id == 24) && TCLc.containsKey(classId))
			{
				html = TCLc.get(classId);
			}
			else if (event_id == 25)
			{
				html = "tutorial_22cc.htm";
			}
			else if ((event_id == 26) && TCLa.containsKey(classId))
			{
				html = TCLa.get(classId);
			}
			else if (event_id == 27)
			{
				html = "tutorial_29.htm";
			}
			else if (event_id == 28)
			{
				html = "tutorial_28.htm";
			}
		}
		else if (event.startsWith("CE"))
		{
			int event_id = Integer.valueOf(event.substring(2));
			if ((event_id == 1) && (player.getLevel() < 6))
			{
				st.playTutorialVoice("tutorial_voice_004");
				html = "tutorial_03.htm";
				st.playSound(SOUND_TUTORIAL);
				st.onTutorialClientEvent(2);
			}
			else if ((event_id == 2) && (player.getLevel() < 6))
			{
				st.playTutorialVoice("tutorial_voice_005");
				html = "tutorial_05.htm";
				st.playSound(SOUND_TUTORIAL);
				st.onTutorialClientEvent(8);
			}
			else if ((event_id == 8) && (player.getLevel() < 6))
			{
				html = "tutorial_01.htm";
				st.playSound(SOUND_TUTORIAL);
				st.playTutorialVoice("ItemSound.quest_tutorial");
				st.set("ucMemo", "1");
				st.set("Ex", "-5");
			}
			else if ((event_id == 30) && (player.getLevel() < 10) && (st.getInt("Die") == 0))
			{
				st.playTutorialVoice("tutorial_voice_016");
				st.playSound(SOUND_TUTORIAL);
				st.set("Die", "1");
				st.showQuestionMark(8);
				st.onTutorialClientEvent(0);
			}
			else if ((event_id == 800000) && (player.getLevel() < 6) && (st.getInt("sit") == 0))
			{
				st.playTutorialVoice("tutorial_voice_018");
				st.playSound(SOUND_TUTORIAL);
				st.set("sit", "1");
				st.onTutorialClientEvent(0);
				html = "tutorial_21z.htm";
			}
			else if (event_id == 40)
			{
				if (player.getLevel() == 5)
				{
					if (((st.getInt("lvl") < 5) && !player.getClassId().isMage()) || (classId == 49))
					{
						st.playTutorialVoice("tutorial_voice_014");
						st.showQuestionMark(9);
						st.playSound(SOUND_TUTORIAL);
						st.set("lvl", "5");
					}
				}
				if (player.getLevel() == 6)
				{
					if ((st.getInt("lvl") < 6) && (player.getClassLevel() == 0))
					{
						st.playTutorialVoice("tutorial_voice_020");
						st.playSound(SOUND_TUTORIAL);
						st.showQuestionMark(24);
						st.set("lvl", "6");
					}
				}
				else if (player.getLevel() == 7)
				{
					if ((st.getInt("lvl") < 7) && player.getClassId().isMage() && (classId != 49) && (player.getClassLevel() == 0))
					{
						st.playTutorialVoice("tutorial_voice_019");
						st.playSound(SOUND_TUTORIAL);
						st.set("lvl", "7");
						st.showQuestionMark(11);
					}
				}
				else if (player.getLevel() == 15)
				{
					if (st.getInt("lvl") < 15)
					{
						st.playSound(SOUND_TUTORIAL);
						st.set("lvl", "15");
						st.showQuestionMark(33);
					}
				}
				else if (player.getLevel() >= 18)
				{
					if ((st.getInt("lvl") < 18) && (player.getClassLevel() == 0))
					{
						st.playSound(SOUND_TUTORIAL);
						st.set("lvl", "18");
						st.showQuestionMark(35);
					}
				}
				else if (player.getLevel() == 20)
				{
					if (st.getInt("lvl") < 20)
					{
						st.playSound(SOUND_TUTORIAL);
						st.set("lvl", "20");
						st.showQuestionMark(36);
					}
				}
				else if (player.getLevel() == 28)
				{
					if (st.getInt("lvl") < 28)
					{
						st.playSound(SOUND_TUTORIAL);
						st.set("lvl", "28");
						st.showQuestionMark(36);
					}
				}
				else if (player.getLevel() == 35)
				{
					if ((st.getInt("lvl") < 35) && (player.getRace() != Race.kamael) && (player.getClassLevel() == 1))
					{
						switch (classId)
						{
							case 1:
							case 4:
							case 7:
							case 11:
							case 15:
							case 19:
							case 22:
							case 26:
							case 29:
							case 32:
							case 35:
							case 39:
							case 42:
							case 45:
							case 47:
							case 50:
							case 54:
							case 56:
								st.playSound(SOUND_TUTORIAL);
								st.set("lvl", "35");
								st.showQuestionMark(34);
						}
					}
				}
				else if (player.getLevel() == 38)
				{
					if (st.getInt("lvl") < 38)
					{
						st.playSound(SOUND_TUTORIAL);
						st.set("lvl", "38");
						st.showQuestionMark(36);
					}
				}
				else if (player.getLevel() == 48)
				{
					if (st.getInt("lvl") < 48)
					{
						st.playSound(SOUND_TUTORIAL);
						st.set("lvl", "48");
						st.showQuestionMark(36);
					}
				}
				else if (player.getLevel() == 58)
				{
					if (st.getInt("lvl") < 58)
					{
						st.playSound(SOUND_TUTORIAL);
						st.set("lvl", "58");
						st.showQuestionMark(36);
					}
				}
				else if (player.getLevel() == 68)
				{
					if (st.getInt("lvl") < 68)
					{
						st.playSound(SOUND_TUTORIAL);
						st.set("lvl", "68");
						st.showQuestionMark(36);
					}
				}
				else if (player.getLevel() == 79)
				{
					if (st.getInt("lvl") < 79)
					{
						st.playSound(SOUND_TUTORIAL);
						st.set("lvl", "79");
						st.showQuestionMark(79);
					}
				}
			}
			else if ((event_id == 45) && (player.getLevel() < 10) && (st.getInt("HP") == 0))
			{
				st.playTutorialVoice("tutorial_voice_017");
				st.playSound(SOUND_TUTORIAL);
				st.set("HP", "1");
				st.showQuestionMark(10);
				st.onTutorialClientEvent(800000);
			}
			else if ((event_id == 57) && (player.getLevel() < 6) && (st.getInt("Adena") == 0))
			{
				st.playTutorialVoice("tutorial_voice_012");
				st.playSound(SOUND_TUTORIAL);
				st.set("Adena", "1");
				st.showQuestionMark(23);
			}
			else if ((event_id == 6353) && (player.getLevel() < 6) && (st.getInt("Gemstone") == 0))
			{
				st.playTutorialVoice("tutorial_voice_013");
				st.playSound(SOUND_TUTORIAL);
				st.set("Gemstone", "1");
				st.showQuestionMark(5);
			}
			else if ((event_id == 1048576) && (player.getLevel() < 6))
			{
				st.showQuestionMark(5);
				st.playTutorialVoice("tutorial_voice_013");
				st.playSound(SOUND_TUTORIAL);
			}
		}
		else if (event.startsWith("QM"))
		{
			int MarkId = Integer.valueOf(event.substring(2));
			if (MarkId == 1)
			{
				st.set("Ex", "-5");
				html = "tutorial_01.htm";
			}
			else if (MarkId == 3)
			{
				html = "tutorial_09.htm";
				st.onTutorialClientEvent(1048576);
			}
			else if (MarkId == 5)
			{
				html = "tutorial_11.htm";
			}
			else if (MarkId == 7)
			{
				html = "tutorial_15.htm";
				st.set("ucMemo", "3");
			}
			else if (MarkId == 8)
			{
				html = "tutorial_18.htm";
			}
			else if (MarkId == 9)
			{
				int x = 0;
				int y = 0;
				int z = 0;
				for (String[] element : QMCa)
				{
					if (classId == Integer.valueOf(element[0]))
					{
						html = element[1];
						x = Integer.valueOf(element[2]);
						y = Integer.valueOf(element[3]);
						z = Integer.valueOf(element[4]);
					}
				}
				if (x != 0)
				{
					st.addRadar(x, y, z);
				}
			}
			else if (MarkId == 10)
			{
				html = "tutorial_19.htm";
			}
			else if (MarkId == 11)
			{
				int x = 0;
				int y = 0;
				int z = 0;
				for (String[] element : QMCa)
				{
					if (classId == Integer.valueOf(element[0]))
					{
						html = element[1];
						x = Integer.valueOf(element[2]);
						y = Integer.valueOf(element[3]);
						z = Integer.valueOf(element[4]);
					}
				}
				if (x != 0)
				{
					st.addRadar(x, y, z);
				}
			}
			else if (MarkId == 12)
			{
				html = "tutorial_15.htm";
				st.set("ucMemo", "4");
			}
			else if (MarkId == 12)
			{
				html = "tutorial_30.htm";
			}
			else if (MarkId == 23)
			{
				html = "tutorial_24.htm";
			}
			else if ((MarkId == 24) && QMCb.containsKey(classId))
			{
				html = QMCb.get(classId);
			}
			else if (MarkId == 26)
			{
				if (player.getClassId().isMage() && (classId != 49))
				{
					html = "tutorial_newbie004b.htm";
				}
				else
				{
					html = "tutorial_newbie004a.htm";
				}
			}
			else if (MarkId == 33)
			{
				html = "tutorial_27.htm";
			}
			else if (MarkId == 34)
			{
				html = "tutorial_28.htm";
			}
			else if ((MarkId == 35) && QMCc.containsKey(player.getRace().ordinal()))
			{
				html = QMCc.get(player.getRace().ordinal());
			}
			else if (MarkId == 36)
			{
				int lvl = player.getLevel();
				if (lvl == 20)
				{
					html = "tutorial_kama_20.htm";
				}
				else if (lvl == 28)
				{
					html = "tutorial_kama_28.htm";
				}
				else if (lvl == 38)
				{
					html = "tutorial_kama_38.htm";
				}
				else if (lvl == 48)
				{
					html = "tutorial_kama_48.htm";
				}
				else if (lvl == 58)
				{
					html = "tutorial_kama_58.htm";
				}
				else if (lvl == 68)
				{
					html = "tutorial_kama_68.htm";
				}
				else if (lvl == 79)
				{
					html = "tutorial_epic_quest.htm";
				}
			}
		}
		if (html.isEmpty())
		{
			return null;
		}
		st.showTutorialHTML(html, TutorialShowHtml.TYPE_HTML);
		return null;
	}
	
	@Override
	public void onPlayerEnter(Player player)
	{
		if (player.getLevel() < 6)
		{
			player.addListener(_tutorialShowListener);
		}
	}
	
	public class TutorialShowListener implements OnCurrentHpDamageListener
	{
		@Override
		public void onCurrentHpDamage(Creature actor, double damage, Creature attacker, Skill skill)
		{
			Player player = actor.getPlayer();
			if (player.getCurrentHpPercents() < 25)
			{
				player.removeListener(_tutorialShowListener);
				Quest q = QuestManager.getQuest(255);
				if (q != null)
				{
					player.processQuestEvent(q.getName(), "CE45", null);
				}
			}
			else if (player.getLevel() > 5)
			{
				player.removeListener(_tutorialShowListener);
			}
		}
	}
	
	public boolean isVisible()
	{
		return false;
	}
}
