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
 * Time: 4:55
 */
public class _483_IntendedTactic extends Quest implements ScriptFile {
    private static final int Энде = 33357;
    private static final int КровьВерности = 17736;
    private static final int КровьИстины = 17736;
    private static final int СимволДерзости = 17624;
    private static final int[] МонстрыВерности = {23069, 23070, 23073, 23071, 23072, 23074, 23075};
    private static final int[] МонстрыИстины = {25811, 25812, 25815, 25809};

    @Override
    public void onLoad() {
    }

    @Override
    public void onReload() {
    }

    @Override
    public void onShutdown() {
    }

    public _483_IntendedTactic() {
        super(false);
        addStartNpc(Энде);
        addTalkId(Энде);
        addKillId(МонстрыВерности);
        addKillId(МонстрыИстины);
        addQuestItem(КровьВерности, КровьИстины);
    }

    @Override
    public String onEvent(String event, QuestState st, NpcInstance npc) {
        if (event.equalsIgnoreCase("33357-08.htm")) {
            st.playSound(Quest.SOUND_ACCEPT);
            st.setState(Quest.STARTED);
            st.setCond(1);
        }
        return event;
    }

    @Override
    public String onTalk(NpcInstance npc, QuestState st) {
        String htmltext = "noquest";

        if (npc.getNpcId() == 33357) {
            switch (st.getState()) {
                case CREATED:
                    if (st.getPlayer().getLevel() >= 48) {
                        htmltext = "33357-01.htm";
                    } else {
                        htmltext = "33357-02.htm";
                        st.exitCurrentQuest(true);
                    }
                    break;
                case STARTED:
                    if (st.getCond() == 1) {
                        htmltext = "33357-09.htm";
                    } else {
                        if (st.getCond() != 2)
                            break;
                        if ((st.getQuestItemsCount(17736) >= 10L) && (st.getQuestItemsCount(17736) >= 1L)) {
                            htmltext = "33357-12.htm";
                            st.addExpAndSp(1500000, 1250000);
                            st.giveItems(СимволДерзости, 1);
                            st.exitCurrentQuest(false);
                        } else {
                            if (st.getQuestItemsCount(17736) < 10L)
                                break;
                            htmltext = "33357-11.htm";
                            st.addExpAndSp(1500000, 1250000);
                            st.exitCurrentQuest(false);
                        }
                    }
                    break;
                case COMPLETED:
                    htmltext = "33357-03.htm";
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
            if ((ArrayUtils.contains(МонстрыВерности, npc.getNpcId())) && (Rnd.chance(25))) {
                st.giveItems(17736, 1);
                st.playSound("ItemSound.quest_itemget");
                if (st.getQuestItemsCount(17736) >= 10L) {
                    st.setCond(2);
                    st.playSound("ItemSound.quest_middle");
                }
            } else if (ArrayUtils.contains(МонстрыИстины, npc.getNpcId())) {
                if (!st.hasQuestItems(17736)) {
                    st.giveItems(17736, 1);
                    st.playSound("ItemSound.quest_itemget");
                }
            }
        } else if (st.getCond() == 2) {
            if (ArrayUtils.contains(МонстрыИстины, npc.getNpcId())) {
                if (!st.hasQuestItems(17736)) {
                    st.giveItems(17736, 1);
                    st.playSound("ItemSound.quest_itemget");
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
        return player.getLevel() >= 48;
    }
}
