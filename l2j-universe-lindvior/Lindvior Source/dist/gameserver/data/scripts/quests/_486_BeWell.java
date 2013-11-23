package quests;

import l2p.commons.util.Rnd;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.model.quest.Quest;
import l2p.gameserver.model.quest.QuestState;
import l2p.gameserver.scripts.ScriptFile;
import org.apache.commons.lang3.ArrayUtils;

/**
 * Created with IntelliJ IDEA.
 * User: Darvin
 * Date: 02.09.12
 * Time: 3:55
 */
public class _486_BeWell extends Quest implements ScriptFile {
    private static final int Аберкромби = 31555;
    private static final int Гид = 33463;
    private static final int[] Монстры = {21508, 21509, 21510, 21511, 21512, 21513, 21514, 21515, 21516, 21517, 21518, 21519};
    private static final int ПанцирьСтакато = 19498;

    @Override
    public void onLoad() {
    }

    @Override
    public void onReload() {
    }

    @Override
    public void onShutdown() {
    }

    public _486_BeWell() {
        super(false);
        addStartNpc(Гид);
        addTalkId(Гид, Аберкромби);
        addKillId(Монстры);
        addQuestItem(ПанцирьСтакато);
    }

    @Override
    public String onEvent(String event, QuestState st, NpcInstance npc) {
        if (event.equalsIgnoreCase("33463-04.htm")) {
            st.playSound(Quest.SOUND_ACCEPT);
            st.setState(Quest.STARTED);
            st.setCond(1);
        }
        return event;
    }

    @Override
    public String onTalk(NpcInstance npc, QuestState st) {
        String htmltext = "noquest";

        if (npc.getNpcId() == 33463) {
            switch (st.getState()) {
                case CREATED:
                    if (st.getPlayer().getLevel() < 70) {
                        htmltext = getLowLevelMsg(70);
                        st.exitCurrentQuest(true);
                    } else if (st.getPlayer().getLevel() > 74) {
                        htmltext = getHighLevelMsg(74);
                        st.exitCurrentQuest(true);
                    } else {
                        htmltext = "33463-01.htm";
                    }
                    break;
                case STARTED:
                    if (st.getCond() == 1) {
                        htmltext = "33463-05.htm";
                    } else {
                        if (st.getCond() != 2)
                            break;
                        htmltext = "33463-06.htm";
                    }
                    break;
                case COMPLETED:
                    htmltext = getAlreadyCompletedMsg(st.getPlayer());
            }

        } else if (npc.getNpcId() == 31555) {
            if (st.isStarted()) {
                if (st.getCond() == 2) {
                    htmltext = "31155-01.htm";
                    st.addExpAndSp(9009000, 8997060);
                    st.giveAdena(353160);
                    st.exitCurrentQuest(false);
                } else {
                    htmltext = "31155-02.htm";
                }
            } else if (st.isCompleted()) {
                htmltext = "31155-03.htm";
            }
        }
        return htmltext;
    }

    @Override
    public String onKill(NpcInstance npc, QuestState st) {
        if ((npc == null) || (st == null)) {
            return null;
        }
        if (st.getCond() == 1) {
            if ((ArrayUtils.contains(Монстры, npc.getNpcId())) && (Rnd.chance(90))) {
                st.giveItems(19498, 1);
                st.playSound("ItemSound.quest_itemget");
                if (st.getQuestItemsCount(19498) >= 80L) {
                    st.setCond(2);
                    st.playSound("ItemSound.quest_middle");
                }
            }
        }
        return null;
    }

    @Override
    public boolean isDailyQuest() {
        return true;
    }

    @Override
    public boolean canBeStarted(Player player) {
        return (player.getLevel() >= 70) && (player.getLevel() <= 74);
    }
}
