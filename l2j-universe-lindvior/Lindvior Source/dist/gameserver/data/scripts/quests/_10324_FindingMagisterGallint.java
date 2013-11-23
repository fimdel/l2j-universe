/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package quests;

import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.model.quest.Quest;
import l2p.gameserver.model.quest.QuestState;
import l2p.gameserver.network.serverpackets.TutorialShowHtml;
import l2p.gameserver.scripts.ScriptFile;

public class _10324_FindingMagisterGallint extends Quest implements ScriptFile {
    private static final int shenon = 32974;
    private static final int galint = 32980;

    @Override
    public void onLoad() {
    }

    @Override
    public void onReload() {
    }

    @Override
    public void onShutdown() {
    }

    public _10324_FindingMagisterGallint() {
        super(false);
        addStartNpc(shenon);
        addTalkId(shenon);
        addTalkId(galint);

        addLevelCheck(1, 20);
        addQuestCompletedCheck(_10323_GoingIntoRealWar.class);
    }

    @Override
    public String onEvent(String event, QuestState st, NpcInstance npc) {
        String htmltext = event;

        if (event.equalsIgnoreCase("quest_ac")) {
            st.setState(STARTED);
            st.setCond(1);
            st.playSound(SOUND_ACCEPT);
            htmltext = "0-3.htm";
        }

        if (event.equalsIgnoreCase("qet_rev")) {
            htmltext = "1-2.htm";
            st.showTutorialHTML(TutorialShowHtml.QT_004, TutorialShowHtml.TYPE_WINDOW);
            st.getPlayer().addExpAndSp(1700, 2000);
            st.giveItems(57, 11000);
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

        if (npcId == shenon) {
            if (st.isCompleted())
                htmltext = TODO_FIND_HTML;
            else if (cond == 0 && isAvailableFor(st.getPlayer()))
                htmltext = "start.htm";
            else if (cond == 1)
                htmltext = "0-4.htm";
            else
                htmltext = "0-nc.htm";
        } else if (npcId == galint)
            if (st.isCompleted())
                htmltext = "1-c.htm";
            else if (cond == 0)
                htmltext = "1-nc.htm";
            else if (cond == 1)
                htmltext = "1-1.htm";
        return htmltext;
    }
}
