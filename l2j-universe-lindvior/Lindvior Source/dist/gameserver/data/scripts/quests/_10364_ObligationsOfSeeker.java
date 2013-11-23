/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package quests;

import l2p.commons.util.Rnd;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.model.quest.Quest;
import l2p.gameserver.model.quest.QuestState;
import l2p.gameserver.scripts.ScriptFile;

public class _10364_ObligationsOfSeeker extends Quest implements ScriptFile {
    private static final int celin = 33451;
    private static final int avian = 22994;
    private static final int warper = 22996;
    private static final int dep = 33453;
    private static final int papper = 17578;
    private static final int walter = 33452;

    @Override
    public void onLoad() {
    }

    @Override
    public void onReload() {
    }

    @Override
    public void onShutdown() {
    }

    public _10364_ObligationsOfSeeker() {
        super(false);
        addStartNpc(celin);
        addTalkId(celin);
        addTalkId(dep);
        addTalkId(walter);
        addKillId(warper, avian);
        addQuestItem(papper);
        addLevelCheck(1, 20);
        addQuestCompletedCheck(_10363_RequestOfSeeker.class);
    }

    @Override
    public String onEvent(String event, QuestState st, NpcInstance npc) {
        String htmltext = event;

        if (event.equalsIgnoreCase("quest_ac")) {
            st.setState(STARTED);
            st.setCond(1);
            st.playSound(SOUND_ACCEPT);
            htmltext = "0-3.htm";
        }

        if (event.equalsIgnoreCase("papper")) {
            st.setCond(2);
            st.playSound(SOUND_MIDDLE);
            htmltext = "0-3.htm";
        }

        if (event.equalsIgnoreCase("qet_rev")) {
            st.takeAllItems(papper);
            htmltext = "2-4.htm";
            st.getPlayer().addExpAndSp(95000, 10000);
            st.giveItems(57, 55000);
            st.giveItems(1060, 50);
            st.giveItems(37, 1);
            st.exitCurrentQuest(false);
            st.playSound(SOUND_FINISH);
        }
        return htmltext;
    }

    @Override
    public String onTalk(NpcInstance npc, QuestState st) {
        int cond = st.getCond();
        int npcId = npc.getNpcId();
        String htmltext = "noquest";

        if (npcId == celin) {
            if (st.isCompleted())
                htmltext = "0-c.htm";
            else if (cond == 0 && isAvailableFor(st.getPlayer()))
                htmltext = "start.htm";
            else if (cond == 1)
                htmltext = "0-4.htm";
            else
                htmltext = "0-nc.htm";
        } else if (npcId == walter) {
            if (st.isCompleted())
                htmltext = "1-c.htm";
            else if (cond == 1)
                htmltext = "1-1.htm";
            else if (cond == 2)
                htmltext = "1-5.htm";
            else if (cond == 3)
                htmltext = "1-6.htm";
            else
                htmltext = "1-5.htm";
        } else if (npcId == dep)
            if (st.isCompleted())
                htmltext = "2-c.htm";
            else if (cond == 0)
                htmltext = "2-nc.htm";
            else if (cond == 1)
                htmltext = TODO_FIND_HTML;
            else if (cond == 2)
                htmltext = TODO_FIND_HTML;
            else if (cond == 3)
                htmltext = "2-1.htm";
        return htmltext;
    }

    @Override
    public String onKill(NpcInstance npc, QuestState st) {
        int npcId = npc.getNpcId();

        if (Rnd.chance(35) && st.getCond() == 2 && st.getQuestItemsCount(papper) < 5 && npcId == warper || npcId == avian) {
            st.giveItems(papper, 1);
            st.playSound(SOUND_ITEMGET);
        }

        if (st.getQuestItemsCount(papper) >= 5) {
            st.setCond(3);
            st.playSound(SOUND_MIDDLE);
        }
        return null;
    }
}