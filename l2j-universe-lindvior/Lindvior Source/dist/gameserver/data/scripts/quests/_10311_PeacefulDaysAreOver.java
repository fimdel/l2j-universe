/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package quests;

import l2p.gameserver.model.Player;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.model.quest.Quest;
import l2p.gameserver.model.quest.QuestState;
import l2p.gameserver.scripts.ScriptFile;

public class _10311_PeacefulDaysAreOver extends Quest implements ScriptFile {
    private static final int NPC_SLAKI = 32893;
    private static final int NPC_SELINA = 33032;

    public _10311_PeacefulDaysAreOver() {
        super(PARTY_NONE);
        addStartNpc(NPC_SELINA);
        addTalkId(NPC_SLAKI);
        addQuestCompletedCheck(_10312_AbandonedGodsCreature.class);
    }

    @Override
    public String onEvent(String event, QuestState st, NpcInstance npc) {
        if (st == null) {
            return "noquest";
        }
        if (event.equalsIgnoreCase("33031-06.htm")) {
            st.setCond(1);
            st.setState(STARTED);
            st.playSound(SOUND_ACCEPT);
        } else if (event.equalsIgnoreCase("32893-05.htm")) {
            st.addExpAndSp(7168395, 3140085);
            st.giveItems(57, 489220, true);
            st.playSound(SOUND_FINISH);
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
        npc.getNpcId();
        Player player = st.getPlayer();
        QuestState previous = player.getQuestState(_10312_AbandonedGodsCreature.class);
        if (npc.getNpcId() == NPC_SELINA) {
            if ((previous == null) || (!previous.isCompleted()) || (player.getLevel() < 90)) {
                st.exitCurrentQuest(true);
                return "33032-03.htm";
            }
            switch (st.getState()) {
                case COMPLETED:
                    htmltext = "33032-02.htm";
                    break;
                case CREATED:
                    htmltext = "33032-01.htm";
                    break;
                case STARTED:
                    if (st.getCond() != 1) {
                        break;
                    }
                    htmltext = "33032-07.htm";
            }
        } else if (npc.getNpcId() == NPC_SLAKI) {
            if (st.isStarted()) {
                if (st.getCond() == 1) {
                    htmltext = "32893-01.htm";
                }
            }
        }
        return htmltext;
    }

    @Override
    public boolean isVisible(Player player) {
        QuestState qs = player.getQuestState(_10311_PeacefulDaysAreOver.class);
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
