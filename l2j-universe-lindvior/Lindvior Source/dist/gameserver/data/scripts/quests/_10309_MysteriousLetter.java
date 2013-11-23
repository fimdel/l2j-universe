/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package quests;

import l2p.gameserver.model.Player;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.model.quest.Quest;
import l2p.gameserver.model.quest.QuestState;
import l2p.gameserver.scripts.ScriptFile;

public class _10309_MysteriousLetter extends Quest implements ScriptFile {
    private static final int Alisha = 31303;
    private static final int advent = 33463;
    private static final int waizard = 31522;
    private static final int tifaren = 31334;
    private static final int letter = 19493;

    @Override
    public void onLoad() {
    }

    @Override
    public void onReload() {
    }

    @Override
    public void onShutdown() {
    }

    public _10309_MysteriousLetter() {
        super(false);
        addStartNpc(advent);
        addTalkId(Alisha);
        addTalkId(advent);
        addTalkId(waizard);
        addTalkId(tifaren);
        addLevelCheck(65, 69);
    }

    @Override
    public String onEvent(String event, QuestState st, NpcInstance npc) {
        String htmltext = event;
        if (event.equalsIgnoreCase("quest_ac")) {
            st.setState(STARTED);
            st.setCond(1);
            st.playSound(SOUND_ACCEPT);
            htmltext = "0-3.htm";
        } else if (event.equalsIgnoreCase("3-2.htm")) {
            st.getPlayer().addExpAndSp(4952910, 4894920);
            st.giveItems(57, 276000);
            st.takeAllItems(letter);
            st.exitCurrentQuest(false);
            st.playSound(SOUND_FINISH);
        } else if (event.equalsIgnoreCase("2-2.htm")) {
            st.setCond(3);
            st.takeAllItems(letter);
            st.playSound(SOUND_MIDDLE);
            st.giveItems(letter, 1, false);
        }
        return htmltext;
    }

    @Override
    public String onTalk(NpcInstance npc, QuestState st) {
        int cond = st.getCond();
        int npcId = npc.getNpcId();
        Player player = st.getPlayer();
        player.getClassId().getId();
        String htmltext = "noquest";
        if (npcId == advent) {
            if (st.isCompleted()) {
                htmltext = "0-c.htm";
            } else if ((cond == 0) && isAvailableFor(st.getPlayer())) {
                htmltext = "0-1.htm";
            } else if (cond == 1) {
                htmltext = "0-3.htm";
            } else {
                htmltext = TODO_FIND_HTML;
            }
        } else if (npcId == Alisha) {
            if (st.isCompleted()) {
                htmltext = "1-c.htm";
            } else if (cond == 0) {
                htmltext = TODO_FIND_HTML;
            } else if (cond == 1) {
                htmltext = "1-1.htm";
                st.giveItems(letter, 1, false);
                st.setCond(2);
            } else if (cond == 2) {
                htmltext = "1-2.htm";
            }
        } else if (npcId == tifaren) {
            if (st.isCompleted()) {
                htmltext = "2-c.htm";
            } else if (cond == 0) {
                htmltext = TODO_FIND_HTML;
            } else if (cond == 2) {
                htmltext = "2-1.htm";
            } else if (cond == 3) {
                htmltext = "2-3.htm";
            }
        } else if (npcId == waizard) {
            if (st.isCompleted()) {
                htmltext = "3-c.htm";
            } else if (cond == 0) {
                htmltext = TODO_FIND_HTML;
            } else if ((cond == 1) || (cond == 2)) {
                htmltext = TODO_FIND_HTML;
            } else if (cond == 3) {
                htmltext = "3-1.htm";
            }
        }
        return htmltext;
    }
}
