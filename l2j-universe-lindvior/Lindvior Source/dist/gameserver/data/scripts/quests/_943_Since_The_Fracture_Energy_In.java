/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package quests;

import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.model.quest.Quest;
import l2p.gameserver.model.quest.QuestState;
import l2p.gameserver.scripts.ScriptFile;
import org.apache.commons.lang3.ArrayUtils;

/**
 * @author cruel
 */

public class _943_Since_The_Fracture_Energy_In extends Quest implements ScriptFile {
    private static final int Talisman_Supervisor = 33715;
    private static final int Energy_Of_Destruction = 35562;
    private static final int[] RaidBoss = {29195, 29194, 29213, 29218, 25825, 25779, 25867, 25868};

    public _943_Since_The_Fracture_Energy_In() {
        super(PARTY_ALL);
        addStartNpc(Talisman_Supervisor);
        addKillId(RaidBoss);
    }

    @Override
    public String onEvent(String event, QuestState st, NpcInstance npc) {
        String htmltext = event;
        if (event.equalsIgnoreCase("npc3.htm")) {
            st.setState(STARTED);
            st.setCond(1);
            st.playSound(SOUND_ACCEPT);
        } else if (event.equalsIgnoreCase("npc5.htm")) {
            st.giveItems(Energy_Of_Destruction, 1);
            st.takeItems(35668, 1);
            st.setState(COMPLETED);
            st.playSound(SOUND_FINISH);
            st.exitCurrentQuest(this);
        }

        return htmltext;
    }

    @Override
    public String onTalk(NpcInstance npc, QuestState st) {
        String htmltext = "noquest";
        int cond = st.getCond();
        if (npc.getNpcId() == Talisman_Supervisor)
            switch (st.getState()) {
                case CREATED:
                    if (st.getPlayer().getLevel() >= 90) {
                        if (st.isNowAvailableByTime())
                            htmltext = "npc1.htm";
                        else
                            htmltext = "npc0.htm";
                    } else {
                        htmltext = "no_level.htm";
                        st.exitCurrentQuest(true);
                    }
                    break;
                case STARTED:
                    if (cond == 1)
                        htmltext = "npc3.htm";
                    else if (cond == 2)
                        htmltext = "npc4.htm";
                    break;
            }

        return htmltext;
    }

    @Override
    public String onKill(NpcInstance npc, QuestState st) {
        int cond = st.getCond();
        if (cond == 1)
            if (ArrayUtils.contains(RaidBoss, npc.getNpcId())) {
                st.setCond(2);
                st.giveItems(35668, 1);
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