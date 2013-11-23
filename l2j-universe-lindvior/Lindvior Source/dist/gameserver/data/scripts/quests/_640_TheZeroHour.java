package quests;

import l2p.gameserver.model.Player;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.model.quest.Quest;
import l2p.gameserver.model.quest.QuestState;
import l2p.gameserver.scripts.ScriptFile;

public class _640_TheZeroHour extends Quest implements ScriptFile {
    //npc
    public static final int KAHMAN = 31554;

    public static final int QUEEN_FANG = 14859;

    public static final int QUEEN = 25671; //queen sheed

    @Override
    public void onLoad() {
    }

    @Override
    public void onReload() {
    }

    @Override
    public void onShutdown() {
    }

    public _640_TheZeroHour() {
        super(true);
        addStartNpc(KAHMAN);

        addQuestCompletedCheck(_109_InSearchOfTheNest.class);
        addQuestItem(QUEEN_FANG);
        addKillId(QUEEN);
        addLevelCheck(81);
    }

    @Override
    public String onEvent(String event, QuestState st, NpcInstance npc) {
        Player player = st.getPlayer();
        String htmltext = event;
        if (event.equalsIgnoreCase("31554-4.htm")) {
            st.setCond(1);
            st.setState(STARTED);
            st.playSound(SOUND_ACCEPT);
        }
        if (event.equalsIgnoreCase("32892-10.htm")) {
            if (st.getQuestItemsCount(QUEEN_FANG) <= 0)
                return "32892-8.htm";
            st.giveItems(14849, 1); //treasure chest
            st.addExpAndSp(2850000, 3315000);
            st.playSound(SOUND_FINISH);
            st.exitCurrentQuest(false);
            return "32892-7.htm";
        }
        return event;
    }

    @Override
    public String onTalk(NpcInstance npc, QuestState st) {
        Player player = st.getPlayer();
        int npcId = npc.getNpcId();
        int state = st.getState();
        int cond = st.getCond();
        if (state == COMPLETED)
            return "31554-comp.htm";
        if (player.getLevel() < 81)
            return "31554-lvl.htm";
        if (npcId == KAHMAN) {
            if (cond == 0) {
                return "31554.htm";
            }
            if (cond == 1)
                return "31554-6.htm";
        }
        return "noquest";
    }

    @Override
    public String onKill(NpcInstance npc, QuestState st) {
        int npcId = npc.getNpcId();
        if (npcId == QUEEN && st.getQuestItemsCount(QUEEN_FANG) == 0)
            st.giveItems(QUEEN_FANG, 1);
        return null;
    }
}