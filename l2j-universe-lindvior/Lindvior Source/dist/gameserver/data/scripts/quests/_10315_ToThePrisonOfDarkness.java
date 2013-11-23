/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package quests;

import l2p.gameserver.model.Player;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.model.quest.Quest;
import l2p.gameserver.model.quest.QuestState;
import l2p.gameserver.scripts.ScriptFile;

public class _10315_ToThePrisonOfDarkness extends Quest implements ScriptFile {
    private static final int NPC_SLAKI = 32893;
    private static final int NPC_OPERA = 32946;

    public _10315_ToThePrisonOfDarkness() {
        super(false);
        addStartNpc(NPC_SLAKI);
        addTalkId(NPC_OPERA);
        addLevelCheck(90, 99);
        addQuestCompletedCheck(_10306_TheCorruptedLeader.class);
        addQuestCompletedCheck(_10311_PeacefulDaysAreOver.class);
    }

    @Override
    public String onEvent(String event, QuestState st, NpcInstance npc) {
        if (st == null) {
            return "noquest";
        }
        if (event.equalsIgnoreCase("32893-06.htm")) {
            st.setCond(1);
            st.setState(STARTED);
            st.playSound(SOUND_ACCEPT);
        } else if (event.equalsIgnoreCase("32946-05.htm")) {
            st.playSound(SOUND_FINISH);
            st.addExpAndSp(4038093, 1708398);
            st.giveItems(57, 279513, true);
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
        Player player = st.getPlayer();
        QuestState previous = player.getQuestState(_10306_TheCorruptedLeader.class);
        QuestState previous2 = player.getQuestState(_10311_PeacefulDaysAreOver.class);
        if (npc.getNpcId() == NPC_SLAKI) {
            if ((previous == null) || (!previous.isCompleted()) || (previous2 == null) || (!previous2.isCompleted()) || (player.getLevel() < 90)) {
                st.exitCurrentQuest(true);
                return "32893-03.htm";
            }
            switch (st.getState()) {
                case COMPLETED:
                    htmltext = "32893-04.htm";
                    break;
                case CREATED:
                    htmltext = "32893-01.htm";
                    break;
                case STARTED:
                    if (st.getCond() != 1) {
                        break;
                    }
                    htmltext = "32893-07.htm";
            }
        } else if (npc.getNpcId() == NPC_OPERA) {
            if (st.isStarted()) {
                if (st.getCond() == 1) {
                    htmltext = "32946-01.htm";
                }
            } else if (st.isCompleted()) {
                htmltext = "32946-03.htm";
            } else {
                htmltext = "32946-02.htm";
            }
        }
        return htmltext;
    }

    @Override
    public boolean isVisible(Player player) {
        QuestState qs = player.getQuestState(_10315_ToThePrisonOfDarkness.class);
        return ((qs == null) && isAvailableFor(player)) || ((qs != null) && qs.isNowAvailableByTime());
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
