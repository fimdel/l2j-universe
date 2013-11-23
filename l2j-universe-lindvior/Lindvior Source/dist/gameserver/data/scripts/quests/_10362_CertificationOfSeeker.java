/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package quests;

import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.model.quest.Quest;
import l2p.gameserver.model.quest.QuestState;
import l2p.gameserver.scripts.ScriptFile;

public class _10362_CertificationOfSeeker extends Quest implements ScriptFile {
    private static final int chesha = 33449;
    private static final int nagel = 33450;
    private static final int husk = 22991;
    private static final int stalker = 22992;
    private static final String stalker_item = "husk_stalker";
    private static final String husk_item = "husk";

    @Override
    public void onLoad() {
    }

    @Override
    public void onReload() {
    }

    @Override
    public void onShutdown() {
    }

    public _10362_CertificationOfSeeker() {
        super(false);
        addStartNpc(chesha);
        addTalkId(chesha);
        addTalkId(nagel);
        addKillNpcWithLog(1, stalker_item, 10, stalker);
        addKillNpcWithLog(1, husk_item, 5, husk);

        addLevelCheck(1, 20);
        addQuestCompletedCheck(_10361_RolesOfSeeker.class);

    }

    @Override
    public String onEvent(String event, QuestState st, NpcInstance npc) {
        String htmltext = event;

        if (event.equalsIgnoreCase("quest_ac")) {
            st.setState(STARTED);
            st.setCond(1);
            st.playSound(SOUND_ACCEPT);
            htmltext = "0-4.htm";
        }

        if (event.equalsIgnoreCase("qet_rev")) {
            htmltext = "1-3.htm";
            st.getPlayer().addExpAndSp(50000, 7000);
            st.giveItems(57, 43000);
            st.giveItems(1060, 50);
            st.giveItems(49, 1);
            st.exitCurrentQuest(false);
            st.playSound(SOUND_FINISH);
        }
        return htmltext;
    }

    @Override
    public String onTalk(NpcInstance npc, QuestState st) {
        int cond = st.getCond();
        int npcId = npc.getNpcId();
        String htmltext = "noquest";

        if (npcId == chesha)
            if (st.isCompleted())
                htmltext = "0-c.htm";
            else if (cond == 0 && isAvailableFor(st.getPlayer()))
                htmltext = "start.htm";
            else if (cond == 1)
                htmltext = "0-4.htm";
            else if (cond == 2) {
                htmltext = "0-5.htm";
                st.setCond(3);
            } else if (cond == 3)
                htmltext = "0-6.htm";
            else
                htmltext = "0-nc.htm";
        if (npcId == nagel)
            if (st.isCompleted())
                htmltext = "1-c.htm";
            else if (cond == 0)
                htmltext = TODO_FIND_HTML;
            else if (cond == 3)
                htmltext = "1-1.htm";
        return htmltext;
    }

    @Override
    public String onKill(NpcInstance npc, QuestState st) {
        boolean doneKill = updateKill(npc, st);
        if (doneKill) {
            st.unset(stalker_item);
            st.unset(husk_item);
            st.setCond(2);
        }
        return null;
    }
}