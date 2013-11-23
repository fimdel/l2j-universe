/*
 * Copyright Mazaffaka Project (c) 2012.
 */

package quests;

import l2p.gameserver.model.Player;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.model.quest.Quest;
import l2p.gameserver.model.quest.QuestState;
import l2p.gameserver.scripts.ScriptFile;

/**
 * Created with IntelliJ IDEA.
 * User: Darvin
 * Date: 21.11.12
 * Time: 18:51
 */
public class _469_SuspiciousGardener extends Quest implements ScriptFile {
    private static final int NPC_HORPINA = 33031;
    private static final int MOB_APHERUS_WATCHMAN = 22964;
    private static final int REWARD_CERTIFICATE_OF_LIFE = 30385;

    public _469_SuspiciousGardener() {
        super(false);

        addStartNpc(NPC_HORPINA);
        addKillId(MOB_APHERUS_WATCHMAN);
        addKillNpcWithLog(1, "Watchman", 30, MOB_APHERUS_WATCHMAN);
        addLevelCheck(90, 99);
    }

    @Override
    public String onEvent(String event, QuestState st, NpcInstance npc) {
        if (st == null) {
            return "noquest";
        }

        if (event.equalsIgnoreCase("33031-03.htm")) {
            st.startQuest();
        } else if (event.equalsIgnoreCase("33031-06.htm")) {
            st.giveItems(REWARD_CERTIFICATE_OF_LIFE, 6);
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

        if (npc.getNpcId() == NPC_HORPINA) {
            switch (st.getState()) {
                case DELAYED:
                    htmltext = "33031-04.htm";
                    break;
                case CREATED:
                    if (player.getLevel() >= 90) {
                        htmltext = "33031-01.htm";
                    } else {
                        htmltext = getLowLevelMsg(90);

                        st.exitCurrentQuest(true);
                    }

                    break;
                case STARTED:
                    if (st.getCond() == 1) {
                        htmltext = "33031-03.htm";
                    } else {
                        if (st.getCond() != 2) {
                            break;
                        }

                        htmltext = "33031-05.htm";
                    }
            }
        }

        return htmltext;
    }

    @Override
    public String onKill(NpcInstance npc, QuestState st) {
        if ((npc == null) || (st == null)) {
            return null;
        }

        if (updateKill(npc, st)) {
            st.setCond(2);
            st.unset("Watchman");
        }

        return null;
    }

    public boolean isVisible(Player player) {
        QuestState qs = player.getQuestState(_469_SuspiciousGardener.class);

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
