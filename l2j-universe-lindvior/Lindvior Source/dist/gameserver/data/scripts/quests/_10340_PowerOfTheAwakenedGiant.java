package quests;

import l2p.gameserver.model.Player;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.model.quest.Quest;
import l2p.gameserver.model.quest.QuestState;

/**
 * Created with IntelliJ IDEA.
 * User: Darvin
 * Date: 18.09.12
 * Time: 0:17
 */
public class _10340_PowerOfTheAwakenedGiant extends Quest {
    private static final int Себион = 32978;
    private static final int Пантеон = 32972;
    private static final int ПризракХарнака = 25772;

    public _10340_PowerOfTheAwakenedGiant() {
        super(false);
        addStartNpc(Себион);
        addTalkId(Себион, Пантеон);
        addKillId(ПризракХарнака);
    }

    @Override
    public String onEvent(String event, QuestState st, NpcInstance npc) {
        if (event.equals("32978-04.htm")) {
            st.startQuest();

        } else if (event.equals("32972-02.htm")) {
            st.playSound("ItemSound.quest_finish");
            st.addExpAndSp(235645257, 97634343);
            st.giveItem(19508);
            st.exitQuest(false);

        } else if (event.equals("32972-03.htm")) {
            st.playSound("ItemSound.quest_finish");
            st.addExpAndSp(235645257, 97634343);
            st.giveItem(19510);
            st.exitQuest(false);

        } else if (event.equals("32972-04.htm")) {
            st.playSound("ItemSound.quest_finish");
            st.addExpAndSp(235645257, 97634343);
            st.giveItem(19509);
            st.exitQuest(false);
        }

        return event;
    }

    @Override
    public String onTalk(NpcInstance npc, QuestState st) {
        String htmltext = getNoQuestMsg(st.getPlayer());

        if (npc.getNpcId() == Себион) {
            switch (st.getState()) {
                case COMPLETED:
                    htmltext = "32978-06.htm";
                    break;
                case CREATED:
                    if (st.getPlayer().getLevel() < 85) {
                        htmltext = "32978-05.htm";
                    } else {
                        htmltext = "32978-01.htm";
                        st.exitQuest(true);
                    }
                    break;
                case STARTED:
                    switch (st.getCond()) {
                        case 1:
                            htmltext = "32978-04.htm";
                            break;
                        case 2:
                            htmltext = "32978-07.htm";
                            st.playSound("ItemSound.quest_middle");
                            st.setCond(3);
                            break;
                        case 3:
                            htmltext = "32978-08.htm";
                    }

            }

        } else if (npc.getNpcId() == Пантеон) {
            if (st.isStarted() && st.getCond() == 3) {
                htmltext = "32972-01.htm";
            }
        }
        return htmltext;
    }

    @Override
    public String onKill(NpcInstance npc, QuestState st) {
        if (npc.getNpcId() == ПризракХарнака && st.getCond() == 1) {
            st.setCond(2);
            st.playSound("ItemSound.quest_middle");
        }
        return null;
    }

    @Override
    public boolean canBeStarted(Player player) {
        return player.getLevel() >= 85;
    }
}
