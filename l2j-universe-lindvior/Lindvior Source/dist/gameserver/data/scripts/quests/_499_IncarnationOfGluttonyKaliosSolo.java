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
 * TODO:offlike EN HTMLs
 * Daily quest
 */
public class _499_IncarnationOfGluttonyKaliosSolo extends Quest implements ScriptFile {
    private static final int NPC_KARTIA_RESEARCHER = 33647;
    private static final int MOB_KALIOS = 25884;
    private static final int REWARD_DIMENSION_TRAVELERS_GOLDEN_BOX = 34932;

    public _499_IncarnationOfGluttonyKaliosSolo() {
        super(PARTY_NONE);

        addStartNpc(NPC_KARTIA_RESEARCHER);
        addKillNpcWithLog(1, "KaliosSolo", 1, MOB_KALIOS);
        addLevelCheck(95, 99);
    }

    @Override
    public String onEvent(String event, QuestState st, NpcInstance npc) {
        if (st == null) {
            return "noquest";
        }

        if (event.equalsIgnoreCase("33647-07.htm")) {
            st.startQuest();
        } else if (event.equalsIgnoreCase("33647-10.htm")) {
            st.unset("cond");
            st.giveItems(REWARD_DIMENSION_TRAVELERS_GOLDEN_BOX, 1);
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
            if ((player.getLevel() < 85) || (player.getLevel() > 90)) {
                st.exitCurrentQuest(true);
                return "33647-02.htm";
            }

            switch (st.getState()) {
                case CREATED:
                    htmltext = "33647-01.htm";
                    break;
                case STARTED:
                    if (st.getCond() == 1) {
                        htmltext = "33647-08.htm";
                    } else {
                        if (st.getCond() != 2) {
                            break;
                        }

                        htmltext = "33647-09.htm";
                    }

                    break;
                case DELAYED:
                    htmltext = "33647-03.htm";
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
            st.unset("KaliosSolo");
        }

        return null;
    }

    @Override
    public boolean isVisible(Player player) {
        QuestState qs = player.getQuestState(_498_IncarnationOfJealousyPellineSolo.class);

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
