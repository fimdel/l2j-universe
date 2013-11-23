package quests;

import l2p.gameserver.model.Player;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.model.quest.Quest;
import l2p.gameserver.model.quest.QuestState;
import l2p.gameserver.scripts.ScriptFile;

public class _481_ShadowHelper extends Quest implements ScriptFile {
    //npc
    public static final int RINOBERG = 33302;

    public static final String A_LIST = "a_list";

    @Override
    public void onLoad() {
    }

    @Override
    public void onReload() {
    }

    @Override
    public void onShutdown() {
    }

    public _481_ShadowHelper() {
        super(true);
        addStartNpc(RINOBERG);
        addTalkId(RINOBERG);

        addKillNpcWithLog(1, A_LIST, 20, 20213, 20214, 20215, 20216, 21035, 21039, 20217, 20751, 21036, 20218, 20219, 20220, 21037, 29012, 20754, 20222, 21040, 21038, 20753, 29007);

        addLevelCheck(38);
    }

    @Override
    public String onEvent(String event, QuestState st, NpcInstance npc) {
        Player player = st.getPlayer();
        String htmltext = event;
        if (event.equalsIgnoreCase("33302-6.htm")) {
            st.setCond(1);
            st.setState(STARTED);
            st.playSound(SOUND_ACCEPT);
        }
        if (event.equalsIgnoreCase("33302-10.htm")) {
            st.unset("cond");
            st.addExpAndSp(240000, 156000);
            st.playSound(SOUND_FINISH);
            st.exitCurrentQuest(this);
        }
        return event;
    }

    @Override
    public String onTalk(NpcInstance npc, QuestState st) {
        Player player = st.getPlayer();
        int npcId = npc.getNpcId();
        int state = st.getState();
        int cond = st.getCond();
        if (npcId == RINOBERG) {
            if (state == 1) {
                if (player.getLevel() < 38)
                    return "33302-lvl.htm";
                if (!st.isNowAvailable())
                    return "33302-comp.htm";
                return "33302.htm";
            }
            if (state == 2) {
                if (cond == 1)
                    return "33302-8.htm";

                if (cond == 2)
                    return "33302-9.htm";
            }
        }
        return "noquest";
    }

    @Override
    public String onKill(NpcInstance npc, QuestState st) {
        int cond = st.getCond();
        if (cond != 1)
            return null;
        boolean doneKill = updateKill(npc, st);
        if (doneKill) {
            st.unset(A_LIST);
            st.setCond(2);
        }
        return null;
    }
}