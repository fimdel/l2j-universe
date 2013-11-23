/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package quests;

import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.model.quest.Quest;
import l2p.gameserver.model.quest.QuestState;
import l2p.gameserver.scripts.ScriptFile;

public class _10368_RebellionofMonsters extends Quest implements ScriptFile {

    private static final int fred = 33179;

    private static final int jaga = 23024;
    private static final int jagav = 23025;
    private static final int incect = 23099;
    private static final int incectl = 23100;

    private static final String jaga_item = "jaga";
    private static final String jagav_item = "jagav";
    private static final String incect_item = "incect";
    private static final String incectl_item = "incectl";

    @Override
    public void onLoad() {
    }

    @Override
    public void onReload() {
    }

    @Override
    public void onShutdown() {
    }

    public _10368_RebellionofMonsters() {
        super(false);
        addStartNpc(fred);
        addTalkId(fred);
        addKillNpcWithLog(1, jaga_item, 10, jaga);
        addKillNpcWithLog(1, jagav_item, 15, jagav);
        addKillNpcWithLog(1, incect_item, 15, incect);
        addKillNpcWithLog(1, incectl_item, 20, incectl);

        addLevelCheck(34, 40);

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
        return htmltext;
    }

    @Override
    public String onTalk(NpcInstance npc, QuestState st) {
        int cond = st.getCond();
        int npcId = npc.getNpcId();
        String htmltext = "noquest";

        if (npcId == fred)
            if (st.isCompleted())
                htmltext = "0-c.htm";
            else if (cond == 0 && isAvailableFor(st.getPlayer()))
                htmltext = "0-1.htm";
            else if (cond == 1)
                htmltext = "0-4.htm";
            else if (cond == 2) {
                htmltext = "0-5.htm";
                st.getPlayer().addExpAndSp(550000, 150000);
                st.giveItems(57, 99000);
                st.exitCurrentQuest(false);
                st.playSound(SOUND_FINISH);
            }

        return htmltext;
    }

    @Override
    public String onKill(NpcInstance npc, QuestState st) {
        boolean doneKill = updateKill(npc, st);

        if (doneKill) {
            st.unset(jaga_item);
            st.unset(jagav_item);
            st.unset(incect_item);
            st.unset(incectl_item);
            st.setCond(2);
        }
        return null;
    }
}