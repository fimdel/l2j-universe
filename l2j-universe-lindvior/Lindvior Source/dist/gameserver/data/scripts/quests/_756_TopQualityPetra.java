/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package quests;

import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.model.quest.Quest;
import l2p.gameserver.model.quest.QuestState;
import l2p.gameserver.scripts.ScriptFile;

/**
 * @author KilRoy & Mangol
 * @name 756 - Top Quality Petra
 * @category Daily quest. Party
 * @see http://l2wiki.com/Top_Quality_Petra
 */
public class _756_TopQualityPetra extends Quest implements ScriptFile {
    private int AKU_MARK = 34910;
    private int TOP_QUALITY_PETRA = 35703;

    private int AKU = 33671;

    public _756_TopQualityPetra() {
        super(false);
        addTalkId(AKU);
        addQuestItem(TOP_QUALITY_PETRA);

        addLevelCheck(97, 99);
    }

    @Override
    public String onEvent(String event, QuestState st, NpcInstance npc) {
        String htmltext = event;

        if (event.equalsIgnoreCase("sofa_aku_q0756_02.htm")) {
            st.takeAllItems(TOP_QUALITY_PETRA);
            st.getPlayer().addExpAndSp(570676680, 261024840);
            st.giveItems(AKU_MARK, 1);
            st.exitCurrentQuest(this);
            st.playSound(SOUND_FINISH);
        }
        return htmltext;
    }

    @Override
    public String onTalk(NpcInstance npc, QuestState st) {
        int cond = st.getCond();
        int npcId = npc.getNpcId();
        String htmltext = "noquest";

        if (npcId == AKU) {
            if (st.isCreated() && cond == 1)
                htmltext = "sofa_aku_q0756_01.htm";
            else
                htmltext = "sofa_aku_q0756_03.htm";
        }
        return htmltext;
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