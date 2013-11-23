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
public class _477_Awakened extends Quest implements ScriptFile {
    private static final int Гид = 33463;
    private static final int Джастин = 31282;
    private static final int КровавыеСлезы = 19496;
    private static final int[] Монстры = {21294, 21295, 21296, 21297, 21298, 21299, 21300, 21301, 21302, 21303, 21304, 21305, 21307, 21312, 21313};

    @Override
    public void onLoad() {
    }

    @Override
    public void onReload() {
    }

    @Override
    public void onShutdown() {
    }

    public _477_Awakened() {
        super(false);
        addStartNpc(Гид);
        addTalkId(Гид, Джастин);
        addKillId(Монстры);
        addQuestItem(КровавыеСлезы);
    }

    @Override
    public String onEvent(String event, QuestState st, NpcInstance npc) {
        if (!event.equalsIgnoreCase("33463-04.htm")) {
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
            if (st.getPlayer().getLevel() < 70) {
                st.exitCurrentQuest(true);
                return getLowLevelMsg(70);
            }
            if (st.getPlayer().getLevel() > 74) {
                st.exitCurrentQuest(true);
                return getHighLevelMsg(74);
            }
            switch (st.getState()) {
                case COMPLETED:
                    htmltext = getAlreadyCompletedMsg(st.getPlayer());
                    break;
                case CREATED:
                    htmltext = "33463-01.htm";
                    break;
                case STARTED:
                    if (st.getCond() == 1)
                        htmltext = "33463-05.htm";
                    else {
                        htmltext = "33463-06.htm";
                    }
            }
        } else if (npc.getNpcId() == 31282) {
            if (st.isStarted()) {
                if (st.getCond() == 1) {
                    htmltext = "31282-02.htm";
                } else if (st.getCond() == 2) {
                    htmltext = "31282-01.htm";
                    st.addExpAndSp(8534700, 8523390);
                    st.giveAdena(334560);
                    st.exitCurrentQuest(false);
                }
            } else if (st.isCompleted()) {
                htmltext = "31282-03.htm";
            }
        }
        return htmltext;
    }

    @Override
    public String onKill(NpcInstance npc, QuestState st) {

        if ((npc == null) || (st == null)) {
            return null;
        }
        if ((st.getCond() == 1) && (Rnd.chance(50))) {
            if (ArrayUtils.contains(Монстры, npc.getNpcId())) {
                st.giveItems(19496, 1);
                st.playSound("ItemSound.quest_itemget");
                if (st.getQuestItemsCount(19496) >= 45L) {
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
