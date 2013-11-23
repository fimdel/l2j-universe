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
public class _494_IncarnationOfGreedZellakaGroup extends Quest implements ScriptFile {
    private static final int NPC_KARTIA_RESEARCHER = 33647;
    private static final int MOB_ZELAKA = 25882;
    private static final int REWARD_DIMENSION_KEEPER_BLUE_BOX = 34927;

    public _494_IncarnationOfGreedZellakaGroup() {
        super(PARTY_ALL);

        addStartNpc(NPC_KARTIA_RESEARCHER);
        addKillId(MOB_ZELAKA);
        addKillNpcWithLog(1, "Zelaka", 1, MOB_ZELAKA);
        addLevelCheck(85, 90);
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
            st.giveItems(REWARD_DIMENSION_KEEPER_BLUE_BOX, 1);
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
                return "33647-01a.htm";
            }

            switch (st.getState()) {
                case DELAYED:
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
                case COMPLETED:
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
            st.unset("Zelaka");
        }

        return null;
    }

    @Override
    public boolean isVisible(Player player) {
        QuestState qs = player.getQuestState(_494_IncarnationOfGreedZellakaGroup.class);

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
