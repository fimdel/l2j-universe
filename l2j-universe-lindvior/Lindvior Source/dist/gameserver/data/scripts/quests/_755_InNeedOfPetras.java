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
 * @author KilRoy & Mangol
 * @name 755 - In Need of Petras
 * @category Daily quest. Party
 * @see http://l2wiki.com/In_Need_of_Petras
 */
public class _755_InNeedOfPetras extends Quest implements ScriptFile {
    private int ENERGY_OF_DESTRUCTION = 35562;
    private int AKU_SUPPLY_BOX = 35550;
    private int PETRA = 34959;

    private int AKU = 33671;
    private int COMMUNICATION = 33676;
    private int[] COUCH = {23233, 23234, 23237, 23219, 23217, 23218, 23231, 23232, 23213, 23214, 23227, 23228};

    public _755_InNeedOfPetras() {
        super(2);
        addStartNpc(AKU);
        addTalkId(AKU);
        addTalkId(COMMUNICATION);
        addKillId(COUCH);
        addQuestItem(PETRA);

        addLevelCheck(97, 99);
    }

    @Override
    public String onEvent(String event, QuestState st, NpcInstance npc) {
        String htmltext = event;

        if (event.equalsIgnoreCase("quest_accpted")) {
            st.setState(STARTED);
            st.setCond(1);
            st.playSound(SOUND_ACCEPT);
            htmltext = "sofa_aku_q0755_04.htm";
        }
        return htmltext;
    }

    @Override
    public String onTalk(NpcInstance npc, QuestState st) {
        int cond = st.getCond();
        int npcId = npc.getNpcId();
        String htmltext = "noquest";

        if (npcId == AKU) {
            if (st.isCreated() && !st.isNowAvailableByTime())
                htmltext = "sofa_aku_q0755_06.htm";
            else if (cond == 0 && isAvailableFor(st.getPlayer()))
                htmltext = "sofa_aku_q0755_01.htm";
            else if (cond == 1)
                htmltext = "sofa_aku_q0755_07.htm";
            else if (cond == 2) {
                st.takeAllItems(PETRA);
                st.getPlayer().addExpAndSp(570676680, 261024840);
                st.giveItems(ENERGY_OF_DESTRUCTION, 1);
                st.giveItems(AKU_SUPPLY_BOX, 1);
                st.exitCurrentQuest(this);
                st.playSound(SOUND_FINISH);
                htmltext = "sofa_aku_q0755_08.htm";
            } else
                htmltext = "sofa_aku_q0755_05.htm";
        }
        if (npcId == COMMUNICATION) {
            if (cond == 2) {
                st.takeAllItems(PETRA);
                st.getPlayer().addExpAndSp(570676680, 261024840);
                st.giveItems(ENERGY_OF_DESTRUCTION, 1);
                st.giveItems(AKU_SUPPLY_BOX, 1);
                st.exitCurrentQuest(this);
                st.playSound(SOUND_FINISH);
                htmltext = "sofa_aku_q0755_08.htm";
            }
        }
        return htmltext;
    }

    @Override
    public String onKill(NpcInstance npc, QuestState st) {
        int npcId = npc.getNpcId();
        int cond = st.getCond();
        if (cond == 1 && ArrayUtils.contains(COUCH, npcId) & st.getQuestItemsCount(PETRA) <= 50) {
            st.rollAndGive(PETRA, 1, 18);
            if (st.getQuestItemsCount(PETRA) == 50) {
                st.setCond(2);
                st.playSound(SOUND_MIDDLE);
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