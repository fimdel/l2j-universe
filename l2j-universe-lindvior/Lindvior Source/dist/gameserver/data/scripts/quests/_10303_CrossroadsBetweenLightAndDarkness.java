/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package quests;

import l2p.commons.util.Rnd;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.model.quest.Quest;
import l2p.gameserver.model.quest.QuestState;
import l2p.gameserver.scripts.ScriptFile;

public class _10303_CrossroadsBetweenLightAndDarkness extends Quest implements ScriptFile {
    private static final int CON1 = 32909;
    private static final int CON2 = 33343;
    private static final int CON3 = 17747;
    private static final int[] CON4 =
            {
                    13505,
                    16108,
                    16102,
                    16105
            };
    private static final int[] CON5 =
            {
                    16101,
                    16100,
                    16099,
                    16098
            };

    public _10303_CrossroadsBetweenLightAndDarkness() {
        super(false);
        addTalkId(CON1, CON2);
        addQuestItem(CON3);
    }

    @Override
    public String onEvent(String event, QuestState st, NpcInstance npc) {
        if (st == null) {
            return event;
        }
        if (event.equalsIgnoreCase("32909-05.htm")) {
            st.dropItem(npc, CON4[Rnd.get(CON4.length)], Rnd.get(1, 100));
            st.addExpAndSp(6730155, 2847330);
            st.getPlayer().addAdena(465855);
            st.playSound("ItemSound.quest_finish");
            st.exitCurrentQuest(false);
        } else if (event.equalsIgnoreCase("33343-05.htm")) {
            st.dropItem(npc, CON5[Rnd.get(CON5.length)], Rnd.get(1, 100));
            st.addExpAndSp(6730155, 2847330);
            st.getPlayer().addAdena(465855);
            st.playSound("ItemSound.quest_finish");
            st.exitCurrentQuest(false);
        }
        return event;
    }

    @Override
    public String onTalk(NpcInstance npc, QuestState st) {
        String htmltext = "noquest";
        if (st == null) {
            return htmltext;
        }
        if (npc.getNpcId() == CON1) {
            switch (st.getState()) {
                case 2:
                    htmltext = "32909-02.htm";
                    break;
                case 1:
                    switch (st.getCond()) {
                        case 1:
                            if (st.getPlayer().getLevel() < 90) {
                                htmltext = "32909-03.htm";
                            } else {
                                htmltext = "32909-01.htm";
                            }
                    }
            }
        } else if (npc.getNpcId() == CON2) {
            switch (st.getState()) {
                case 2:
                    htmltext = "33343-02.htm";
                    break;
                case 1:
                    switch (st.getCond()) {
                        case 1:
                            if (st.getPlayer().getLevel() < 90) {
                                htmltext = "33343-03.htm";
                            } else {
                                htmltext = "33343-01.htm";
                            }
                    }
            }
        }
        return htmltext;
    }

    @Override
    public void onLoad() {
    }

    @Override
    public void onReload() {
    }

    @Override
    public void onShutdown() {
    }
}
