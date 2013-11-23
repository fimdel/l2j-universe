/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package quests;

import l2p.commons.util.Rnd;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.model.quest.Quest;
import l2p.gameserver.model.quest.QuestState;
import l2p.gameserver.scripts.ScriptFile;

public class _10353_CertificationOfValue extends Quest implements ScriptFile {
    private static final int NPC_RIEYI1 = 33155;
    private static final int NPC_RIEYI2 = 33406;
    private static final int NPC_KYUORI = 33358;
    private static final int CHANCE_DROP = 10;
    private static final int REWARD_TOKEN_OF_INSOLENCE_TOWER = 17624;

    public _10353_CertificationOfValue() {
        super(PARTY_ONE);

        addStartNpc(NPC_RIEYI1, NPC_RIEYI2);
        addTalkId(NPC_KYUORI);

        for (int i = 23044; i <= 23068; i++) {
            addKillNpcWithLog(2, "MobsCount", 10, i);
        }

        for (int i = 23101; i <= 23112; i++) {
            addKillNpcWithLog(2, "MobsCount", 10, i);
        }

        addLevelCheck(48, 99);
        //addQuestCompletedCheck(_10337_SakumImpact.class);
    }

    @Override
    public String onTalk(NpcInstance npc, QuestState qs) {
        String htmltext = "noquest";

        if (qs == null) {
            return htmltext;
        }

        Player player = qs.getPlayer();

        if (npc.getNpcId() == NPC_RIEYI1) {
            switch (qs.getState()) {
                case CREATED:
                    if (player.getLevel() >= 48) {
                        htmltext = "33155-01.htm";
                    } else {
                        htmltext = "33155-02.htm";

                        qs.exitCurrentQuest(true);
                    }

                    break;
                case STARTED:
                    if (qs.getCond() != 1) {
                        break;
                    }

                    htmltext = "33155-08.htm";
                    break;
                case COMPLETED:
                    htmltext = "33155-03.htm";
            }
        } else if (npc.getNpcId() == NPC_RIEYI2) {
            switch (qs.getState()) {
                case CREATED:
                    if (player.getLevel() >= 48) {
                        htmltext = "33406-01.htm";
                    } else {
                        htmltext = "33406-02.htm";

                        qs.exitCurrentQuest(true);
                    }

                    break;
                case STARTED:
                    if (qs.getCond() != 1) {
                        break;
                    }

                    htmltext = "33406-08.htm";
                    break;
                case COMPLETED:
                    htmltext = "33406-03.htm";
            }
        } else if (npc.getNpcId() == NPC_KYUORI) {
            if (qs.isStarted()) {
                if (qs.getCond() == 1) {
                    htmltext = "33358-01.htm";
                } else if (qs.getCond() == 2) {
                    htmltext = "33358-05.htm";
                } else if (qs.getCond() == 3) {
                    htmltext = "33358-07.htm";

                    qs.addExpAndSp(3000000, 2500000);
                    qs.giveItems(REWARD_TOKEN_OF_INSOLENCE_TOWER, 1);
                    qs.exitCurrentQuest(false);
                }
            } else if (qs.isCompleted()) {
                htmltext = "33358-03.htm";
            }
        }

        return htmltext;
    }

    @Override
    public String onEvent(String event, QuestState st, NpcInstance npc) {
        if ((event.equalsIgnoreCase("33155-08.htm")) || (event.equalsIgnoreCase("33406-08.htm"))) {
            st.setState(STARTED);
            st.setCond(1);
            st.playSound(SOUND_ACCEPT);
        } else if (event.equalsIgnoreCase("33358-04.htm")) {
            st.setCond(2);
            st.playSound("ItemSound.quest_middle");
        }

        return event;
    }

    @Override
    public String onKill(NpcInstance npc, QuestState qs) {
        if (qs.getCond() != 2) {
            return null;
        }

        if (Rnd.chance(CHANCE_DROP)) {
            if (updateKill(npc, qs)) {
                qs.unset("MobsCount");
                qs.setCond(3);
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
