/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package quests;

import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.model.quest.Quest;
import l2p.gameserver.model.quest.QuestState;
import l2p.gameserver.scripts.Functions;
import l2p.gameserver.scripts.ScriptFile;
import l2p.gameserver.utils.Location;

public class _10365_SeekerEscort extends Quest implements ScriptFile {

    private static final int dep = 33453;
    private static final int sebian = 32978;
    private static final int bloodhound = 32988;

    @Override
    public void onLoad() {
    }

    @Override
    public void onReload() {
    }

    @Override
    public void onShutdown() {
    }

    public _10365_SeekerEscort() {
        super(false);
        addStartNpc(dep);
        addTalkId(sebian);

        addLevelCheck(16, 25);
        addQuestCompletedCheck(_10364_ObligationsOfSeeker.class);
    }

    @Override
    public String onEvent(String event, QuestState st, NpcInstance npc) {
        String htmltext = event;

        if (event.equalsIgnoreCase("quest_accept")) {
            htmltext = "dep_q10365_3.htm";
            st.setState(STARTED);
            st.playSound(SOUND_ACCEPT);
            st.setCond(1);

            NpcInstance guide = Functions.spawn(new Location(-110628, 238359, -2920), bloodhound);
            guide.setFollowTarget(st.getPlayer());
        } else if (st.getCond() == 2 && event.equalsIgnoreCase("quest_done")) {
            htmltext = "sebion_q10365_2.htm";
            st.getPlayer().addExpAndSp(120000, 20000);
            st.giveItems(ADENA_ID, 65000);
            st.exitCurrentQuest(false);
            st.playSound(SOUND_FINISH);
        }

        return htmltext;
    }

    @Override
    public String onTalk(NpcInstance npc, QuestState st) {
        String htmltext = "noquest";
        int npcId = npc.getNpcId();
        int cond = st.getCond();

        switch (npcId) {
            case dep:
                if (st.isCompleted())
                    htmltext = "dep_q10365_0.htm";
                else if (cond == 0)
                    htmltext = "dep_q10365_1.htm";
                else if (!isAvailableFor(st.getPlayer()))
                    htmltext = "dep_q10365_5.htm";
                else
                    htmltext = "dep_q10365_4.htm";
                break;
            case sebian:
                if (st.isCompleted())
                    htmltext = "sebion_q10365_0.htm";
                if (cond == 0)
                    htmltext = "sebion_q10365_3.htm";
                if (cond == 2)
                    htmltext = "sebion_q10365_1.htm";
                break;
        }
        return htmltext;
    }

    public void bloodhoundEscorted(QuestState st) {
        st.setCond(2);
    }
}