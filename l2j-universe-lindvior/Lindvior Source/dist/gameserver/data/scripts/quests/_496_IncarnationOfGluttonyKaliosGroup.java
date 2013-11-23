/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package quests;

import l2p.gameserver.model.Player;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.model.quest.Quest;
import l2p.gameserver.model.quest.QuestState;
import l2p.gameserver.scripts.ScriptFile;

/**
 * TODO: offlike EN HTMLs
 * Daily quest
 */
public class _496_IncarnationOfGluttonyKaliosGroup extends Quest implements ScriptFile {
    private static final int NPC_KARTIA_RESEARCHER = 33647;
    private static final int MOB_KALIOS = 25884;
    private static final int REWARD_DIMENSION_KEEPERS_GOLDEN_BOX = 34929;

    public _496_IncarnationOfGluttonyKaliosGroup() {
        super(PARTY_ALL);

        addStartNpc(NPC_KARTIA_RESEARCHER);
        addKillNpcWithLog(1, "KaliosGroup", 1, MOB_KALIOS);
        addLevelCheck(95, 99);
    }

    @Override
    public String onEvent(String event, QuestState st, NpcInstance npc) {
        if (st == null) {
            return "noquest";
        }

        if (event.equalsIgnoreCase("33647-04.htm")) {
            st.startQuest();
        } else if (event.equalsIgnoreCase("33647-07.htm")) {
            st.unset("cond");
            st.giveItems(REWARD_DIMENSION_KEEPERS_GOLDEN_BOX, 1);
            st.playSound(SOUND_FINISH);
            st.exitCurrentQuest(this);
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

        if (npc.getNpcId() == NPC_KARTIA_RESEARCHER) {
            if (player.getLevel() < 95) {
                st.exitCurrentQuest(true);
                return "33647-01a.htm";
            }

            switch (st.getState()) {
                case CREATED:
                    htmltext = "33647-01.htm";
                    break;
                case STARTED:
                    if (st.getCond() == 1) {
                        htmltext = "33647-05.htm";
                    } else {
                        if (st.getCond() != 2) {
                            break;
                        }

                        htmltext = "33647-06.htm";
                    }

                    break;
                case DELAYED:
                    htmltext = "33647-08.htm";
            }
        }

        return htmltext;
    }

    @Override
    public String onKill(NpcInstance npc, QuestState st) {
        if ((npc == null) || (st == null) || (st.getCond() != 1)) {
            return null;
        }

        if (updateKill(npc, st)) {
            st.playSound(SOUND_MIDDLE);
            st.setCond(2);
            st.unset("KaliosGroup");
        }

        return null;
    }

    @Override
    public boolean isVisible(Player player) {
        QuestState qs = player.getQuestState(_496_IncarnationOfGluttonyKaliosGroup.class);

        return ((qs == null) && isAvailableFor(player)) || ((qs != null) && qs.isNowAvailable());
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
