package quests;


import l2p.gameserver.model.Player;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.model.quest.Quest;
import l2p.gameserver.model.quest.QuestState;
import l2p.gameserver.scripts.ScriptFile;
import services.SupportMagic;

public class _482_RecertificationOfValue extends Quest implements ScriptFile {
    //npc
    public static final int LILEJ = 33155;
    public static final int KUORI = 33358;

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

    public _482_RecertificationOfValue() {
        super(true);
        addStartNpc(LILEJ);
        addTalkId(KUORI);

        addLevelCheck(48);
        addKillNpcWithLog(2, A_LIST, 10, 23044, 23045, 23046, 23047, 23048, 23049, 23050, 23051, 23052, 23053, 23054, 23055, 23056, 23057, 23058, 23059, 23060, 23061, 23062, 23063, 23064, 23065, 23066, 23067, 23068, 23102, 23103, 23104, 23105, 23106, 23107, 23108, 23109, 23110, 23111, 23112);
        addQuestCompletedCheck(_10353_CertificationOfValue.class);
    }

    @Override
    public String onEvent(String event, QuestState st, NpcInstance npc) {
        Player player = st.getPlayer();
        String htmltext = event;
        if (event.equalsIgnoreCase("SupportPlayer")) {
            SupportMagic.doSupportMagic(npc, player, false);
            return "33155-6.htm";
        } else if (event.equalsIgnoreCase("SupportPet")) {
            SupportMagic.doSupportMagic(npc, player, true);
            return "33155-6.htm";
        } else if (event.equalsIgnoreCase("Goto")) {
            st.setCond(1);
            st.setState(STARTED);
            st.playSound(SOUND_ACCEPT);
            player.teleToLocation(119656, 16072, -5120);
            return null;
        } else if (event.equalsIgnoreCase("33358-3.htm")) {
            st.setCond(2);
        }
        return event;
    }

    @Override
    public String onTalk(NpcInstance npc, QuestState st) {
        Player player = st.getPlayer();
        int npcId = npc.getNpcId();
        int state = st.getState();
        int cond = st.getCond();

        if (state == 1) {
            if (player.getLevel() < 48)
                return "33155-lvl.htm";
            if (!st.isNowAvailable())
                return "33155-comp.htm";
        }

        if (npcId == LILEJ) {
            if (cond == 0)
                return "33155.htm";
            if (cond == 1)
                return "33155-11.htm";
        }
        if (npcId == KUORI) {
            if (cond == 1)
                return "33358.htm";
            if (cond == 2)
                return "33358-5.htm";
            if (cond == 3) {
                st.addExpAndSp(1500000, 1250000);
                st.giveItems(17624, 1);
                st.unset("cond");
                st.playSound(SOUND_FINISH);
                st.exitCurrentQuest(this);
                return "33358-6.htm";
            }
        }
        return "noquest";
    }

    @Override
    public String onKill(NpcInstance npc, QuestState st) {
        int cond = st.getCond();
        if (cond != 2)
            return null;
        boolean doneKill = updateKill(npc, st);
        if (doneKill) {
            st.unset(A_LIST);
            st.setCond(3);
        }
        return null;
    }
}