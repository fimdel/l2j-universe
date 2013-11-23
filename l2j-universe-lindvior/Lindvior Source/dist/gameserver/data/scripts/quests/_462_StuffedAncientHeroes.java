package quests;


import l2p.gameserver.data.htm.HtmCache;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.model.quest.Quest;
import l2p.gameserver.model.quest.QuestState;
import l2p.gameserver.scripts.ScriptFile;
import org.apache.commons.lang3.ArrayUtils;

public class _462_StuffedAncientHeroes extends Quest implements ScriptFile {
    private final int[] Bosses = {25760, 25761, 25762, 25763, 25764, 25766, 25767, 25768, 25769, 25770};

    @Override
    public void onLoad() {
    }

    @Override
    public void onReload() {
    }

    @Override
    public void onShutdown() {
    }

    public _462_StuffedAncientHeroes() {
        super(true);
        addStartNpc(32892); // Tipia

        addLevelCheck(95);
        addQuestCompletedCheck(_10317_OrbisWitch.class); // to replace for witch quest
    }

    @Override
    public String onEvent(String event, QuestState st, NpcInstance npc) {
        Player player = st.getPlayer();
        String htmltext = event;
        if (event.equalsIgnoreCase("32892-6.htm")) {
            st.setCond(1);
            st.setState(STARTED);
            st.playSound(SOUND_ACCEPT);
        } else if (event.equalsIgnoreCase("reward")) {
            st.giveItems(30386, 3);
            st.unset("cond");
            st.unset("1bk");
            st.unset("2bk");
            st.set("1bk", "0");
            st.set("2bk", "0");
            st.playSound(SOUND_FINISH);
            st.exitCurrentQuest(this);
            return "32892-11.htm";
        }
        return event;
    }

    @Override
    public String onTalk(NpcInstance npc, QuestState st) {
        Player player = st.getPlayer();
        int npcId = npc.getNpcId();
        int state = st.getState();
        int cond = st.getCond();
        if (npcId == 32892) {
            if (state == 1) {
                if (player.getLevel() < 95)
                    return "32892-lvl.htm";
                if (!st.isNowAvailable())
                    return "32892-comp.htm";
                if (st.getPlayer().getLevel() < 95)
                    return "32892-lvl.htm";
                String htmltext = HtmCache.getInstance().getNotNull("quests/_462_StuffedAncientHeroes/32892.htm", st.getPlayer());
                htmltext.replace("%name%", player.getName());
                return htmltext;
            }
            if (state == 2) {
                if (cond == 1)
                    return "32892-7.htm";

                if (cond == 3) {
                    return "32892-8.htm";
                }
                if (cond == 2) {
                    return "reward";
                }
            }
        }
        return "noquest";
    }

    @Override
    public String onKill(NpcInstance npc, QuestState st) {
        int cond = st.getCond();
        if (cond != 1 || cond != 3)
            return null;
        if (npc != null && ArrayUtils.contains(Bosses, npc.getNpcId())) {
            if (st.getInt("1bk") == 1) {
                st.set("2bk", "1");
                st.setCond(2);
            } else {
                st.set("1bk", "1");
                st.setCond(3);
            }
        }
        return null;
    }
}