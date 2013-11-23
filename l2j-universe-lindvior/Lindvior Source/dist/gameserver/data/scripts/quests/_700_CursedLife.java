package quests;

import l2p.commons.util.Rnd;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.model.quest.Quest;
import l2p.gameserver.model.quest.QuestState;
import l2p.gameserver.scripts.ScriptFile;

public class _700_CursedLife extends Quest implements ScriptFile {
    // NPC's
    private static int wharf_soldier_orbiu = 32560;

    // ITEMS
    private static int q_swallowed_skull = 13872;
    private static int q_swallowed_rib = 13873;
    private static int q_swallowed_bones = 13874;

    // MOB's
    private static int mutant_hawk_1lv = 22602;
    private static int mutant_hawk_2lv = 22603;
    private static int drahawk_1lv = 22604;
    private static int drahawk_2lv = 22605;
    private static int rok = 25624;

    @Override
    public void onLoad() {
    }

    @Override
    public void onReload() {
    }

    @Override
    public void onShutdown() {
    }

    public _700_CursedLife() {
        super(false);
        addStartNpc(wharf_soldier_orbiu);
        addTalkId(wharf_soldier_orbiu);
        addKillId(mutant_hawk_1lv, mutant_hawk_2lv, drahawk_1lv, drahawk_2lv, rok);
        addQuestItem(q_swallowed_skull, q_swallowed_rib, q_swallowed_bones);
    }

    @Override
    public String onEvent(String event, QuestState st, NpcInstance npc) {
        int cond = st.getCond();
        String htmltext = event;

        if (event.equals("quest_accept") && cond == 0) {
            st.setCond(1);
            st.setState(STARTED);
            st.playSound(SOUND_ACCEPT);
            htmltext = "wharf_soldier_orbiu_q0700_05.htm";
        } else if (event.equals("reply_4") && cond == 1) {
            st.playSound(SOUND_FINISH);
            st.exitCurrentQuest(true);
            htmltext = "wharf_soldier_orbiu_q0700_10.htm";
        }
        return htmltext;
    }

    @Override
    public String onTalk(NpcInstance npc, QuestState st) {
        String htmltext = "noquest";
        int npcId = npc.getNpcId();
        int cond = st.getCond();
        QuestState GoodDayToFly = st.getPlayer().getQuestState(_10273_GoodDayToFly.class);
        long _adenatogive = st.getQuestItemsCount(q_swallowed_skull) * 50000 + st.getQuestItemsCount(q_swallowed_rib) * 5000 + st.getQuestItemsCount(q_swallowed_bones) * 500;

        if (npcId == wharf_soldier_orbiu)
            if (cond == 0) {
                if (st.getPlayer().getLevel() >= 75 && GoodDayToFly != null && GoodDayToFly.isCompleted())
                    htmltext = "wharf_soldier_orbiu_q0700_01.htm";
                else {
                    htmltext = "wharf_soldier_orbiu_q0700_03.htm";
                    st.exitCurrentQuest(true);
                }
            } else if (cond == 1)
                if (st.getQuestItemsCount(q_swallowed_skull) < 1 && st.getQuestItemsCount(q_swallowed_rib) < 1 && st.getQuestItemsCount(q_swallowed_bones) < 1)
                    htmltext = "wharf_soldier_orbiu_q0700_06.htm";
                else if (cond == 1 && st.getQuestItemsCount(q_swallowed_skull) + st.getQuestItemsCount(q_swallowed_rib) + st.getQuestItemsCount(q_swallowed_bones) >= 1 && st.getQuestItemsCount(q_swallowed_skull) + st.getQuestItemsCount(q_swallowed_rib) + st.getQuestItemsCount(q_swallowed_bones) < 10) {
                    st.giveItems(ADENA_ID, _adenatogive);
                    st.takeItems(q_swallowed_skull, -1);
                    st.takeItems(q_swallowed_rib, -1);
                    st.takeItems(q_swallowed_bones, -1);
                    htmltext = "wharf_soldier_orbiu_q0700_07.htm";
                }
        return htmltext;
    }

    @Override
    public String onKill(NpcInstance npc, QuestState st) {
        int npcId = npc.getNpcId();
        int cond = st.getCond();
        if (cond == 1)
            if (npcId == mutant_hawk_1lv) {
                int i0 = Rnd.get(1000);
                if (i0 < 15) {
                    st.giveItems(q_swallowed_skull, 1);
                    st.playSound(SOUND_ITEMGET);
                } else if (i0 < 139) {
                    st.giveItems(q_swallowed_rib, 1);
                    st.playSound(SOUND_ITEMGET);
                } else if (i0 < 965) {
                    st.giveItems(q_swallowed_bones, 1);
                    st.playSound(SOUND_ITEMGET);
                }
            } else if (npcId == mutant_hawk_2lv) {
                int i1 = Rnd.get(1000);
                if (i1 < 15) {
                    st.giveItems(q_swallowed_skull, 1);
                    st.playSound(SOUND_ITEMGET);
                } else if (i1 < 143) {
                    st.giveItems(q_swallowed_rib, 1);
                    st.playSound(SOUND_ITEMGET);
                } else if (i1 < 999) {
                    st.giveItems(q_swallowed_bones, 1);
                    st.playSound(SOUND_ITEMGET);
                }
            } else if (npcId == drahawk_1lv) {
                int i2 = Rnd.get(1000);
                if (i2 < 5) {
                    st.giveItems(q_swallowed_skull, 1);
                    st.playSound(SOUND_ITEMGET);
                } else if (i2 < 94) {
                    st.giveItems(q_swallowed_rib, 1);
                    st.playSound(SOUND_ITEMGET);
                } else if (i2 < 994) {
                    st.giveItems(q_swallowed_bones, 1);
                    st.playSound(SOUND_ITEMGET);
                }
            } else if (npcId == drahawk_2lv) {
                int i3 = Rnd.get(1000);
                if (i3 < 5) {
                    st.giveItems(q_swallowed_skull, 1);
                    st.playSound(SOUND_ITEMGET);
                } else if (i3 < 99) {
                    st.giveItems(q_swallowed_rib, 1);
                    st.playSound(SOUND_ITEMGET);
                } else if (i3 < 993) {
                    st.giveItems(q_swallowed_bones, 1);
                    st.playSound(SOUND_ITEMGET);
                }
            } else if (npcId == rok) {
                int i4 = Rnd.get(1000);
                int i5 = Rnd.get(1000);
                int i6 = Rnd.get(1000);
                if (i4 < 700) {
                    st.giveItems(q_swallowed_skull, 1);
                    st.playSound(SOUND_ITEMGET);
                } else if (i4 < 885) {
                    st.giveItems(q_swallowed_skull, 2);
                    st.playSound(SOUND_ITEMGET);
                } else if (i4 < 949) {
                    st.giveItems(q_swallowed_skull, 3);
                    st.playSound(SOUND_ITEMGET);
                } else if (i4 < 966) {
                    st.giveItems(q_swallowed_skull, Rnd.get(5) + 4);
                    st.playSound(SOUND_ITEMGET);
                } else if (i4 < 985) {
                    st.giveItems(q_swallowed_skull, Rnd.get(9) + 4);
                    st.playSound(SOUND_ITEMGET);
                } else if (i4 < 993) {
                    st.giveItems(q_swallowed_skull, Rnd.get(7) + 13);
                    st.playSound(SOUND_ITEMGET);
                } else if (i4 < 997) {
                    st.giveItems(q_swallowed_skull, Rnd.get(15) + 9);
                    st.playSound(SOUND_ITEMGET);
                } else if (i4 < 999) {
                    st.giveItems(q_swallowed_skull, Rnd.get(23) + 53);
                    st.playSound(SOUND_ITEMGET);
                } else if (i4 < 1000) {
                    st.giveItems(q_swallowed_skull, Rnd.get(49) + 76);
                    st.playSound(SOUND_ITEMGET);
                }

                if (i5 < 520) {
                    st.giveItems(q_swallowed_rib, 1);
                    st.playSound(SOUND_ITEMGET);
                } else if (i5 < 771) {
                    st.giveItems(q_swallowed_rib, 2);
                    st.playSound(SOUND_ITEMGET);
                } else if (i5 < 836) {
                    st.giveItems(q_swallowed_rib, 3);
                    st.playSound(SOUND_ITEMGET);
                } else if (i5 < 985) {
                    st.giveItems(q_swallowed_rib, Rnd.get(2) + 4);
                    st.playSound(SOUND_ITEMGET);
                } else if (i5 < 995) {
                    st.giveItems(q_swallowed_rib, Rnd.get(4) + 5);
                    st.playSound(SOUND_ITEMGET);
                } else if (i5 < 1000) {
                    st.giveItems(q_swallowed_rib, Rnd.get(8) + 6);
                    st.playSound(SOUND_ITEMGET);
                }

                if (i6 < 185) {
                    st.giveItems(q_swallowed_bones, Rnd.get(2) + 1);
                    st.playSound(SOUND_ITEMGET);
                } else if (i6 < 370) {
                    st.giveItems(q_swallowed_bones, Rnd.get(6) + 2);
                    st.playSound(SOUND_ITEMGET);
                } else if (i6 < 570) {
                    st.giveItems(q_swallowed_bones, Rnd.get(6) + 7);
                    st.playSound(SOUND_ITEMGET);
                } else if (i6 < 850) {
                    st.giveItems(q_swallowed_bones, Rnd.get(6) + 12);
                    st.playSound(SOUND_ITEMGET);
                } else if (i6 < 1000) {
                    st.giveItems(q_swallowed_bones, Rnd.get(2) + 17);
                    st.playSound(SOUND_ITEMGET);
                }
            }
        return null;
    }
}