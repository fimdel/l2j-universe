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
 * @name 754 - Assisting the Rebel Forces
 * @category Daily quest. Party
 * @see http://l2wiki.com/Assisting_the_Rebel_Forces
 */
public class _754_AssistingTheRebelForces extends Quest implements ScriptFile {
    private int REBEL_SUPPLY_BOX = 35549;
    private int MARK_OF_RESISTANCE = 34909;

    private int SIZRAK = 33669;
    private int COMMUNICATION = 33676;
    private int KUNDA_GUARDIAN = 23224;
    private int KUNDA_BERSERKER = 23225;
    private int KUNDA_EXECUTOR = 23226;

    private static final String KUNDA_GUARDIAN_KILL = "guardian";
    private static final String KUNDA_BERSERKER_KILL = "berserker";
    private static final String KUNDA_EXECUTOR_KILL = "executor";

    public _754_AssistingTheRebelForces() {
        super(2);
        addStartNpc(SIZRAK);
        addTalkId(SIZRAK);
        addTalkId(COMMUNICATION);
        addKillNpcWithLog(1, KUNDA_GUARDIAN_KILL, 5, KUNDA_GUARDIAN);
        addKillNpcWithLog(1, KUNDA_BERSERKER_KILL, 5, KUNDA_BERSERKER);
        addKillNpcWithLog(1, KUNDA_EXECUTOR_KILL, 5, KUNDA_EXECUTOR);

        addLevelCheck(97, 99);
    }

    @Override
    public String onEvent(String event, QuestState st, NpcInstance npc) {
        String htmltext = event;

        if (event.equalsIgnoreCase("quest_accpted")) {
            st.setState(STARTED);
            st.setCond(1);
            st.playSound(SOUND_ACCEPT);
            htmltext = "sofa_sizraku_q0754_04.htm";
        }
        return htmltext;
    }

    @Override
    public String onTalk(NpcInstance npc, QuestState st) {
        int cond = st.getCond();
        int npcId = npc.getNpcId();
        String htmltext = "noquest";

        if (npcId == SIZRAK) {
            if (st.isCreated() && !st.isNowAvailableByTime())
                htmltext = "sofa_sizraku_q0754_06.htm";
            else if (cond == 0 && isAvailableFor(st.getPlayer()))
                htmltext = "sofa_sizraku_q0754_01.htm";
            else if (cond == 1)
                htmltext = "sofa_sizraku_q0754_07.htm";
            else if (cond == 2) {
                st.getPlayer().addExpAndSp(570676680, 261024840);
                st.giveItems(REBEL_SUPPLY_BOX, 1);
                st.giveItems(MARK_OF_RESISTANCE, 1);
                st.exitCurrentQuest(this);
                st.playSound(SOUND_FINISH);
                htmltext = "sofa_sizraku_q0754_08.htm";
            } else
                htmltext = "sofa_sizraku_q0754_05.htm";
        }
        if (npcId == COMMUNICATION) {
            if (cond == 2) {
                st.getPlayer().addExpAndSp(570676680, 261024840);
                st.giveItems(REBEL_SUPPLY_BOX, 1);
                st.giveItems(MARK_OF_RESISTANCE, 1);
                st.exitCurrentQuest(this);
                st.playSound(SOUND_FINISH);
                htmltext = "sofa_sizraku_q0754_08.htm";
            }
        }
        return htmltext;
    }

    @Override
    public String onKill(NpcInstance npc, QuestState st) {
        boolean doneKill = updateKill(npc, st);

        if (doneKill) {
            st.unset(KUNDA_GUARDIAN_KILL);
            st.unset(KUNDA_BERSERKER_KILL);
            st.unset(KUNDA_EXECUTOR_KILL);
            st.playSound(SOUND_MIDDLE);
            st.setCond(2);
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