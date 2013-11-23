/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package quests;

import gnu.trove.map.hash.TIntIntHashMap;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.model.quest.Quest;
import l2p.gameserver.model.quest.QuestState;
import l2p.gameserver.scripts.ScriptFile;
import org.apache.commons.lang3.ArrayUtils;

public class _10305_TheEndlessFutileEfforts extends Quest implements ScriptFile {
    private static final int CON1 = 32895;
    private static final int[] CON2 =
            {
                    32919,
                    32920
            };

    public _10305_TheEndlessFutileEfforts() {
        super(false);
        addStartNpc(CON1);
        addTalkId(CON1);
        addKillId(CON2);
        addLevelCheck(90, 99);
    }

    @Override
    public String onEvent(String event, QuestState st, NpcInstance npc) {
        if (st == null) {
            return null;
        }
        if (event.equalsIgnoreCase("32895-05.htm")) {
            st.getQuest();
        }
        return event;
    }

    @Override
    public String onTalk(NpcInstance npc, QuestState st) {
        String htmltext = "noquest";
        if (st == null) {
            return htmltext;
        }
        QuestState previous = st.getPlayer().getQuestState("10302_TheShadowOfAnxiety");
        if (npc.getNpcId() == 32895) {
            if ((previous == null) || (!previous.isCompleted()) || (st.getPlayer().getLevel() < 90)) {
                st.exitCurrentQuest(true);
                return "32895-00.htm";
            }
            switch (st.getState()) {
                case 0:
                    htmltext = "32895-01.htm";
                    break;
                case 1:
                    switch (st.getCond()) {
                        case 1:
                            htmltext = "32895-06.htm";
                            break;
                        case 2:
                            htmltext = "32895-07.htm";
                            st.addExpAndSp(34971975, 15142200);
                            st.getPlayer().addAdena(1007735, true);
                            st.playSound("ItemSound.quest_finish");
                            st.exitCurrentQuest(false);
                    }
                    break;
                case 2:
                    htmltext = "32895-08.htm";
            }
        }
        return htmltext;
    }

    @Override
    public String onKill(NpcInstance npc, QuestState st) {
        if ((npc == null) || (st == null)) {
            return null;
        }
        if (ArrayUtils.contains(CON2, npc.getNpcId())) {
            TIntIntHashMap moblist = new TIntIntHashMap();
            int ONE = st.getInt("one");
            ONE++;
            st.set("one", String.valueOf(ONE));
            moblist.put(32919, ONE);
            if (ONE >= 5) {
                st.setCond(2);
                st.playSound("ItemSound.quest_middle");
            }
        }
        return null;
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
