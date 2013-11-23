/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package quests;

import l2p.gameserver.instancemanager.QuestManager;
import l2p.gameserver.listener.actor.OnCurrentHpDamageListener;
import l2p.gameserver.listener.actor.player.OnLevelChangeListener;
import l2p.gameserver.listener.actor.player.OnPlayerEnterListener;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.Skill;
import l2p.gameserver.model.actor.listener.CharListenerList;
import l2p.gameserver.model.base.ClassId;
import l2p.gameserver.model.base.ClassLevel;
import l2p.gameserver.model.base.Race;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.model.quest.Quest;
import l2p.gameserver.model.quest.QuestState;
import l2p.gameserver.network.serverpackets.ExCallToChangeClass;
import l2p.gameserver.network.serverpackets.ExShowScreenMessage;
import l2p.gameserver.network.serverpackets.TutorialCloseHtml;
import l2p.gameserver.network.serverpackets.TutorialShowHtml;
import l2p.gameserver.network.serverpackets.components.NpcString;
import l2p.gameserver.scripts.ScriptFile;

import java.util.HashMap;
import java.util.Map;


public class _255_Tutorial extends Quest implements ScriptFile, OnPlayerEnterListener, OnLevelChangeListener {
    // table for Question Mark Clicked (9 & 11) learning skills [raceId, html, x, y, z]
    public final String[][] QMCa = {
            {"0", "tutorial_fighter017.htm", "-83165", "242711", "-3720"},
            {"10", "tutorial_mage017.htm", "-85247", "244718", "-3720"},
            {"18", "tutorial_fighter017.htm", "45610", "52206", "-2792"},
            {"25", "tutorial_mage017.htm", "45610", "52206", "-2792"},
            {"31", "tutorial_fighter017.htm", "10344", "14445", "-4242"},
            {"38", "tutorial_mage017.htm", "10344", "14445", "-4242"},
            {"44", "tutorial_fighter017.htm", "-46324", "-114384", "-200"},
            {"49", "tutorial_fighter017.htm", "-46305", "-112763", "-200"},
            {"53", "tutorial_fighter017.htm", "115447", "-182672", "-1440"},
            {"123", "tutorial_fighter017.htm", "-118132", "42788", "723"},
            {"124", "tutorial_fighter017.htm", "-118132", "42788", "723"}};

    // table for Question Mark Clicked (24) newbie lvl [raceId, html]
    public final Map<Integer, String> QMCb = new HashMap<Integer, String>();

    // table for Question Mark Clicked (35) 1st class transfer [raceId, html]
    public final Map<Integer, String> QMCc = new HashMap<Integer, String>();

    // table for Question Mark Clicked (101) 3st class transfer [raceId, html]
    public final Map<Integer, String> QMCd = new HashMap<Integer, String>();

    // table for Tutorial Close Link (26) 2nd class transfer [raceId, html]
    public final Map<Integer, String> TCLa = new HashMap<Integer, String>();

    // table for Tutorial Close Link (23) 2nd class transfer [raceId, html]
    public final Map<Integer, String> TCLb = new HashMap<Integer, String>();

    // table for Tutorial Close Link (24) 2nd class transfer [raceId, html]
    public final Map<Integer, String> TCLc = new HashMap<Integer, String>();

    private static TutorialShowListener _tutorialShowListener;

    @Override
    public void onLoad() {
    }

    @Override
    public void onReload() {
    }

    @Override
    public void onShutdown() {
    }

    public _255_Tutorial() {
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

        QMCd.put(0, "tutorial_q10341.htm");
        QMCd.put(1, "tutorial_q10342.htm");
        QMCd.put(2, "tutorial_q10343.htm");
        QMCd.put(3, "tutorial_q10344.htm");
        QMCd.put(4, "tutorial_q10345.htm");
        QMCd.put(5, "tutorial_q10346.htm");

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
    public String onEvent(String event, QuestState st, NpcInstance npc) {
        Player player = st.getPlayer();
        if (player == null)
            return null;

        String html = "";

        int classId = player.getActiveClassId();
        int Ex = st.getInt("Ex");

        // Вход в мир
        if (event.startsWith("UC")) {
            int level = player.getLevel();
            if (level < 6 && st.getInt("onlyone") == 0) {
                int uc = st.getInt("ucMemo");
                if (uc == 0) {
                    st.set("ucMemo", "0");
                    st.startQuestTimer("QT", 10000);
                    st.set("Ex", "-2");
                } else if (uc == 1) {
                    st.showQuestionMark(1);
                    st.playTutorialVoice("tutorial_voice_006");
                    st.playSound(SOUND_TUTORIAL);
                } else if (uc == 2) {
                    if (Ex == 2) {
                        st.showQuestionMark(3);
                        st.playSound(SOUND_TUTORIAL);
                    } else if (st.getQuestItemsCount(6353) > 0) {
                        st.showQuestionMark(5);
                        st.playSound(SOUND_TUTORIAL);
                    }
                } else if (uc == 3) {
                    st.showQuestionMark(12);
                    st.playSound(SOUND_TUTORIAL);
                    st.onTutorialClientEvent(0);
                }
            } else if (level >= 18 && player.getClassLevel() == 0) {
                st.showQuestionMark(35);
                st.playSound(SOUND_TUTORIAL);
            } else if (level == 20 && player.getQuestState("_10276_MutatedKaneusGludio") == null) {
                st.showQuestionMark(36);
                st.playSound(SOUND_TUTORIAL);
            } else if (level == 28 && player.getQuestState("_10277_MutatedKaneusDion") == null) {
                st.showQuestionMark(36);
                st.playSound(SOUND_TUTORIAL);
            } else if (level == 28 && player.getQuestState("_10278_MutatedKaneusHeine") == null) {
                st.showQuestionMark(36);
                st.playSound(SOUND_TUTORIAL);
            } else if (level == 28 && player.getQuestState("_10279_MutatedKaneusOren") == null) {
                st.showQuestionMark(36);
                st.playSound(SOUND_TUTORIAL);
            } else if (level == 28 && player.getQuestState("_10280_MutatedKaneusSchuttgart") == null) {
                st.showQuestionMark(36);
                st.playSound(SOUND_TUTORIAL);
            } else if (level == 28 && player.getQuestState("_10281_MutatedKaneusRune") == null) {
                st.showQuestionMark(36);
                st.playSound(SOUND_TUTORIAL);
            } /*else if (level >= 76 && player.getQuestState("_10341_DayOfDestinyHumansFate") == null && player.getClassLevel() == 2 && player.getRace() == Race.human) {
                st.showQuestionMark(101);
                st.playSound(SOUND_TUTORIAL);
            } else if (level >= 76 && player.getQuestState("_10342_DayOfDestinyElvenFate") == null && player.getClassLevel() == 2 && player.getRace() == Race.elf) {
                st.showQuestionMark(101);
                st.playSound(SOUND_TUTORIAL);
            } else if (level >= 76 && player.getQuestState("_10343_DayOfDestinyDarkElfsFate") == null && player.getClassLevel() == 2 && player.getRace() == Race.darkelf) {
                st.showQuestionMark(101);
                st.playSound(SOUND_TUTORIAL);
            } else if (level >= 76 && player.getQuestState("_10344_DayOfDestinyOrcsFate") == null && player.getClassLevel() == 2 && player.getRace() == Race.orc) {
                st.showQuestionMark(101);
                st.playSound(SOUND_TUTORIAL);
            } else if (level >= 76 && player.getQuestState("_10345_DayOfDestinyDwarfsFate") == null && player.getClassLevel() == 2 && player.getRace() == Race.dwarf) {
                st.showQuestionMark(101);
                st.playSound(SOUND_TUTORIAL);
            } else if (level >= 76 && player.getQuestState("_10346_DayOfDestinyKamaelFate") == null && player.getClassLevel() == 2 && player.getRace() == Race.kamael) {
                st.showQuestionMark(101);
                st.playSound(SOUND_TUTORIAL);
            }*/ else if (level == 79 && player.getQuestState("_192_SevenSignSeriesOfDoubt") == null) {
                st.showQuestionMark(36);
                st.playSound(SOUND_TUTORIAL);
            }
        }

        // Обработка таймера QT
        else if (event.startsWith("QT")) {
            if (Ex == -2) {
                html = "tutorial_00.htm";
                st.set("Ex", "-3");
                st.cancelQuestTimer("QT");
                st.startQuestTimer("QT", 30000);
            } else if (Ex == -3) {
                st.playTutorialVoice("tutorial_voice_002");
                st.set("Ex", "0");
            } else if (Ex == -4) {
                st.playTutorialVoice("tutorial_voice_008");
                st.set("Ex", "-5");
            }
        }

        // Tutorial close
        else if (event.startsWith("TE")) {
            st.cancelQuestTimer("TE");
            int event_id = 0;
            if (!event.equalsIgnoreCase("TE"))
                event_id = Integer.valueOf(event.substring(2));
            if (event_id == 0)
                player.sendPacket(TutorialCloseHtml.STATIC);
            else if (event_id == 1) {
                player.sendPacket(TutorialCloseHtml.STATIC);
                st.playTutorialVoice("tutorial_voice_006");
                st.showQuestionMark(1);
                st.playSound(SOUND_TUTORIAL);
                st.startQuestTimer("QT", 30000);
                st.set("Ex", "-4");
            } else if (event_id == 2) {
                st.playTutorialVoice("tutorial_voice_003");
                html = "tutorial_02.htm";
                st.onTutorialClientEvent(1);
                st.set("Ex", "-5");
            } else if (event_id == 3) {
                html = "tutorial_03.htm";
                st.onTutorialClientEvent(2);
            } else if (event_id == 5) {
                html = "tutorial_05.htm";
                st.onTutorialClientEvent(8);
            } else if (event_id == 7) {
                html = "tutorial_100.htm";
                st.onTutorialClientEvent(0);
            } else if (event_id == 8) {
                html = "tutorial_101.htm";
                st.onTutorialClientEvent(0);
            } else if (event_id == 10) {
                html = "tutorial_103.htm";
                st.onTutorialClientEvent(0);
            } else if (event_id == 12)
                player.sendPacket(TutorialCloseHtml.STATIC);
            else if (event_id == 23 && TCLb.containsKey(classId))
                html = TCLb.get(classId);
            else if (event_id == 24 && TCLc.containsKey(classId))
                html = TCLc.get(classId);
            else if (event_id == 25)
                html = "tutorial_22cc.htm";
            else if (event_id == 26 && TCLa.containsKey(classId))
                html = TCLa.get(classId);
            else if (event_id == 27)
                html = "tutorial_29.htm";
            else if (event_id == 28)
                html = "tutorial_28.htm";
        }

        // Client Event
        else if (event.startsWith("CE")) {
            int event_id = Integer.valueOf(event.substring(2));
            if (event_id == 1 && player.getLevel() < 6) {
                st.playTutorialVoice("tutorial_voice_004");
                html = "tutorial_03.htm";
                st.playSound(SOUND_TUTORIAL);
                st.onTutorialClientEvent(2);
            } else if (event_id == 2 && player.getLevel() < 6) {
                st.playTutorialVoice("tutorial_voice_005");
                html = "tutorial_05.htm";
                st.playSound(SOUND_TUTORIAL);
                st.onTutorialClientEvent(8);
            } else if (event_id == 8 && player.getLevel() < 6) {
                html = "tutorial_01.htm";
                st.playSound(SOUND_TUTORIAL);
                st.playTutorialVoice("ItemSound.quest_tutorial");
                st.set("ucMemo", "1");
                st.set("Ex", "-5");
            } else if (event_id == 30 && player.getLevel() < 10 && st.getInt("Die") == 0) {
                st.playTutorialVoice("tutorial_voice_016");
                st.playSound(SOUND_TUTORIAL);
                st.set("Die", "1");
                st.showQuestionMark(8);
                st.onTutorialClientEvent(0);
            } else if (event_id == 800000 && player.getLevel() < 6 && st.getInt("sit") == 0) {
                st.playTutorialVoice("tutorial_voice_018");
                st.playSound(SOUND_TUTORIAL);
                st.set("sit", "1");
                st.onTutorialClientEvent(0);
                html = "tutorial_21z.htm";
            } else if (event_id == 40) {
                if (player.getLevel() == 5)
                    if (st.getInt("lvl") < 5 && !player.getClassId().isMage() || classId == 49) {
                        st.playTutorialVoice("tutorial_voice_014");
                        st.showQuestionMark(9);
                        st.playSound(SOUND_TUTORIAL);
                        st.set("lvl", "5");
                    }
                if (player.getLevel() == 6) {
                    if (st.getInt("lvl") < 6 && player.getClassLevel() == 0) {
                        st.playTutorialVoice("tutorial_voice_020");
                        st.playSound(SOUND_TUTORIAL);
                        st.showQuestionMark(24);
                        st.set("lvl", "6");
                    }
                } else if (player.getLevel() == 7) {
                    if (st.getInt("lvl") < 7 && player.getClassId().isMage() && classId != 49 && player.getClassLevel() == 0) {
                        st.playTutorialVoice("tutorial_voice_019");
                        st.playSound(SOUND_TUTORIAL);
                        st.set("lvl", "7");
                        st.showQuestionMark(11);
                    }
                } else if (player.getLevel() == 15) {
                    if (st.getInt("lvl") < 15) {
                        // st.playTutorialVoice("tutorial_voice_???");
                        st.playSound(SOUND_TUTORIAL);
                        st.set("lvl", "15");
                        st.showQuestionMark(33);
                    }
                } else if (player.getLevel() >= 18) {
                    if (st.getInt("lvl") < 18 && player.getClassLevel() == 0) {
                        // st.playTutorialVoice("tutorial_voice_???");
                        st.playSound(SOUND_TUTORIAL);
                        st.set("lvl", "18");
                        st.showQuestionMark(35);
                    }
                } else if (player.getLevel() == 20) {
                    if (st.getInt("lvl") < 20) {
                        st.playSound(SOUND_TUTORIAL);
                        st.set("lvl", "20");
                        st.showQuestionMark(36);
                    }
                } else if (player.getLevel() == 28) {
                    if (st.getInt("lvl") < 28) {
                        st.playSound(SOUND_TUTORIAL);
                        st.set("lvl", "28");
                        st.showQuestionMark(36);
                    }
                } else if (player.getLevel() == 35) {
                    if (st.getInt("lvl") < 35 && player.getRace() != Race.kamael && player.getClassLevel() == 0)
                        switch (classId) {
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
                                // st.playTutorialVoice("tutorial_voice_???");
                                st.playSound(SOUND_TUTORIAL);
                                st.set("lvl", "35");
                                st.showQuestionMark(34);
                        }
                } else if (player.getLevel() == 38) {
                    if (st.getInt("lvl") < 38) {
                        st.playSound(SOUND_TUTORIAL);
                        st.set("lvl", "38");
                        st.showQuestionMark(36);
                    }
                } else if (player.getLevel() == 40) {
                    if (st.getInt("lvl") < 40) {
                        st.playSound(SOUND_TUTORIAL);
                        st.set("lvl", "40");
                        st.getPlayer().sendPacket(new TutorialShowHtml(TutorialShowHtml.GUIDE_40_50, TutorialShowHtml.TYPE_WINDOW));
                    }
                } else if (player.getLevel() == 48) {
                    if (st.getInt("lvl") < 48) {
                        st.playSound(SOUND_TUTORIAL);
                        st.set("lvl", "48");
                        st.showQuestionMark(36);
                    }
                } else if (player.getLevel() == 50) {
                    if (st.getInt("lvl") < 50) {
                        st.playSound(SOUND_TUTORIAL);
                        st.set("lvl", "50");
                        st.getPlayer().sendPacket(new TutorialShowHtml(TutorialShowHtml.GUIDE_50_55, TutorialShowHtml.TYPE_WINDOW));
                    }
                } else if (player.getLevel() == 55) {
                    if (st.getInt("lvl") < 55) {
                        st.playSound(SOUND_TUTORIAL);
                        st.set("lvl", "55");
                        st.getPlayer().sendPacket(new TutorialShowHtml(TutorialShowHtml.GUIDE_55_60, TutorialShowHtml.TYPE_WINDOW));
                    }
                } else if (player.getLevel() == 58) {
                    if (st.getInt("lvl") < 58) {
                        st.playSound(SOUND_TUTORIAL);
                        st.set("lvl", "58");
                        st.showQuestionMark(36);
                    }
                } else if (player.getLevel() == 60) {
                    if (st.getInt("lvl") < 60) {
                        st.playSound(SOUND_TUTORIAL);
                        st.set("lvl", "60");
                        st.getPlayer().sendPacket(new TutorialShowHtml(TutorialShowHtml.GUIDE_60_65, TutorialShowHtml.TYPE_WINDOW));
                    }
                } else if (player.getLevel() == 65) {
                    if (st.getInt("lvl") < 65) {
                        st.playSound(SOUND_TUTORIAL);
                        st.set("lvl", "65");
                        st.getPlayer().sendPacket(new TutorialShowHtml(TutorialShowHtml.GUIDE_65_70, TutorialShowHtml.TYPE_WINDOW));
                    }
                } else if (player.getLevel() == 68) {
                    if (st.getInt("lvl") < 68) {
                        st.playSound(SOUND_TUTORIAL);
                        st.set("lvl", "68");
                        st.showQuestionMark(36);
                    }
                } else if (player.getLevel() == 70) {
                    if (st.getInt("lvl") < 70) {
                        st.playSound(SOUND_TUTORIAL);
                        st.set("lvl", "70");
                        st.getPlayer().sendPacket(new TutorialShowHtml(TutorialShowHtml.GUIDE_70_75, TutorialShowHtml.TYPE_WINDOW));
                    }
                } else if (player.getLevel() == 75) {
                    if (st.getInt("lvl") < 75) {
                        st.playSound(SOUND_TUTORIAL);
                        st.set("lvl", "75");
                        st.getPlayer().sendPacket(new TutorialShowHtml(TutorialShowHtml.GUIDE_75_80, TutorialShowHtml.TYPE_WINDOW));
                    }
                } else if (player.getLevel() == 76) {
                    if (st.getInt("lvl") < 76 && player.getClassLevel() == 2) {
                        st.playSound(SOUND_TUTORIAL);
                        st.set("lvl", "76");
                        st.showQuestionMark(101);
                    }
                } else if (player.getLevel() == 79) {
                    if (st.getInt("lvl") < 79) {
                        st.playSound(SOUND_TUTORIAL);
                        st.set("lvl", "79");
                        st.showQuestionMark(79);
                    }
                } else if (player.getLevel() == 80) {
                    if (st.getInt("lvl") < 80) {
                        st.playSound(SOUND_TUTORIAL);
                        st.set("lvl", "80");
                        st.getPlayer().sendPacket(new TutorialShowHtml(TutorialShowHtml.GUIDE_80_85, TutorialShowHtml.TYPE_WINDOW));
                    }
                } else if (player.getLevel() == 90) {
                    if (st.getInt("lvl") < 90 && player.getClassLevel() == 4) {
                        st.playSound(SOUND_TUTORIAL);
                        st.set("lvl", "90");
                        st.getPlayer().sendPacket(new TutorialShowHtml(TutorialShowHtml.AWAKING90_95, TutorialShowHtml.TYPE_WINDOW));
                    }
                } else if (player.getLevel() == 95) {
                    if (st.getInt("lvl") < 95 && player.getClassLevel() == 4) {
                        st.playSound(SOUND_TUTORIAL);
                        st.set("lvl", "95");
                        st.getPlayer().sendPacket(new TutorialShowHtml(TutorialShowHtml.AWAKING95_99, TutorialShowHtml.TYPE_WINDOW));
                    }
                }
            } else if (event_id == 45 && player.getLevel() < 10 && st.getInt("HP") == 0) {
                st.playTutorialVoice("tutorial_voice_017");
                st.playSound(SOUND_TUTORIAL);
                st.set("HP", "1");
                st.showQuestionMark(10);
                st.onTutorialClientEvent(800000);
            } else if (event_id == 57 && player.getLevel() < 6 && st.getInt("Adena") == 0) {
                st.playTutorialVoice("tutorial_voice_012");
                st.playSound(SOUND_TUTORIAL);
                st.set("Adena", "1");
                st.showQuestionMark(23);
            } else if (event_id == 6353 && player.getLevel() < 6 && st.getInt("Gemstone") == 0) {
                st.playTutorialVoice("tutorial_voice_013");
                st.playSound(SOUND_TUTORIAL);
                st.set("Gemstone", "1");
                st.showQuestionMark(5);
            }
            // TODO оно нужно?
            else if (event_id == 1048576 && player.getLevel() < 6) {
                st.showQuestionMark(5);
                st.playTutorialVoice("tutorial_voice_013");
                st.playSound(SOUND_TUTORIAL);
            }
        }

        // Question mark clicked
        else if (event.startsWith("QM")) {
            int MarkId = Integer.valueOf(event.substring(2));
            if (MarkId == 1) {
                st.set("Ex", "-5");
                html = "tutorial_01.htm";
            } else if (MarkId == 3) {
                html = "tutorial_09.htm";
                // TODO оно нужно?
                st.onTutorialClientEvent(1048576);
            } else if (MarkId == 5)
                html = "tutorial_11.htm";
            else if (MarkId == 7) {
                html = "tutorial_15.htm";
                st.set("ucMemo", "3");
            } else if (MarkId == 8)
                html = "tutorial_18.htm";
            else if (MarkId == 9) {
                int x = 0;
                int y = 0;
                int z = 0;
                for (String[] element : QMCa)
                    if (classId == Integer.valueOf(element[0])) {
                        html = element[1];
                        x = Integer.valueOf(element[2]);
                        y = Integer.valueOf(element[3]);
                        z = Integer.valueOf(element[4]);
                    }
                if (x != 0)
                    st.addRadar(x, y, z);
            } else if (MarkId == 10)
                html = "tutorial_19.htm";
            else if (MarkId == 11) {
                int x = 0;
                int y = 0;
                int z = 0;
                for (String[] element : QMCa)
                    if (classId == Integer.valueOf(element[0])) {
                        html = element[1];
                        x = Integer.valueOf(element[2]);
                        y = Integer.valueOf(element[3]);
                        z = Integer.valueOf(element[4]);
                    }
                if (x != 0)
                    st.addRadar(x, y, z);
            } else if (MarkId == 12) {
                html = "tutorial_15.htm";
                st.set("ucMemo", "4");
            } else if (MarkId == 12)
                html = "tutorial_30.htm";
            else if (MarkId == 23)
                html = "tutorial_24.htm";
            else if (MarkId == 24 && QMCb.containsKey(classId))
                html = QMCb.get(classId);
            else if (MarkId == 26) {
                if (player.getClassId().isMage() && classId != 49)
                    html = "tutorial_newbie004b.htm";
                else
                    html = "tutorial_newbie004a.htm";
            } else if (MarkId == 33)
                html = "tutorial_27.htm";
            else if (MarkId == 34)
                html = "tutorial_28.htm";
            else if (MarkId == 35 && QMCc.containsKey(player.getRace().ordinal()))
                html = QMCc.get(player.getRace().ordinal());
            else if (MarkId == 36) {
                int lvl = player.getLevel();
                if (lvl == 20)
                    html = "tutorial_kama_20.htm";
                else if (lvl == 28)
                    html = "tutorial_kama_28.htm";
                else if (lvl == 38)
                    html = "tutorial_kama_38.htm";
                else if (lvl == 48)
                    html = "tutorial_kama_48.htm";
                else if (lvl == 58)
                    html = "tutorial_kama_58.htm";
                else if (lvl == 68)
                    html = "tutorial_kama_68.htm";
                else if (lvl == 79)
                    html = "tutorial_epic_quest.htm";
            } else if (MarkId == 101 && QMCd.containsKey(player.getRace().ordinal())) {
                html = QMCd.get(player.getRace().ordinal());
            }
        }

        if (html.isEmpty())
            return null;
        st.showTutorialHTML(html, TutorialShowHtml.TYPE_HTML);
        return null;
    }

    @Override
    public void onPlayerEnter(Player player) {
        if (player.getLevel() < 6)
            player.addListener(_tutorialShowListener);
        else
            checkHermenkusMsg(player);
    }

    @Override
    public void onLevelChange(Player player, int oldLvl, int newLvl) {
        checkHermenkusMsg(player);
    }

    public class TutorialShowListener implements OnCurrentHpDamageListener {
        @Override
        public void onCurrentHpDamage(Creature actor, double damage, Creature attacker, Skill skill) {
            Player player = actor.getPlayer();
            if (player.getCurrentHpPercents() < 25) {
                player.removeListener(_tutorialShowListener);
                Quest q = QuestManager.getQuest(255);
                if (q != null)
                    player.processQuestEvent(q.getName(), "CE45", null);
            } else if (player.getLevel() > 5)
                player.removeListener(_tutorialShowListener);
        }
    }

    public boolean isVisible() {
        return false;
    }

    private static void checkHermenkusMsg(Player player) {
        // Сообщение от гермункуса.
        if (player.getLevel() >= 85 && player.isBaseClassActive() && player.getClassId().isOfLevel(ClassLevel.THIRD)) {
            int classId = 0;
            for (ClassId c : ClassId.VALUES) {
                if (c.isOfLevel(ClassLevel.AWAKED) && c.childOf(player.getClassId())) {
                    classId = c.getId();
                    break;
                }
            }
            if (!player.getVarB("GermunkusUSM")) {
                player.sendPacket(new ExCallToChangeClass(classId, false));
                player.sendPacket(new ExShowScreenMessage(NpcString.FREE_THE_GIANT_FROM_HIS_IMPRISONMENT_AND_AWAKEN_YOUR_TRUE_POWER, 10000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true));
            }
        }
    }
}