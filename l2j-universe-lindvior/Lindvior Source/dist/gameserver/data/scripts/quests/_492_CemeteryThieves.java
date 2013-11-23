/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package quests;

import l2p.commons.util.Rnd;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.base.ClassLevel;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.model.quest.Quest;
import l2p.gameserver.model.quest.QuestState;
import org.apache.commons.lang3.ArrayUtils;

public class _492_CemeteryThieves extends Quest {
    private static final int Зения = 32140;
    private static final int[] Монстры = {23193, 23194, 23195, 23196};
    private static final int РеликвииДревнейИмперии = 34769;

    public _492_CemeteryThieves() {
        super(false);
        addStartNpc(Зения);
        addTalkId(Зения);
        addKillId(Монстры);
        addQuestItem(РеликвииДревнейИмперии);
    }

    @Override
    public String onEvent(String event, QuestState st, NpcInstance npc) {
        if (event.equalsIgnoreCase("32140-06.htm")) {
            st.startQuest();
        }
        return event;
    }

    @Override
    public String onTalk(NpcInstance npc, QuestState st) {
        String htmltext = "noquest";

        if (npc.getNpcId() == 32140) {
            switch (st.getState()) {
                case COMPLETED:
                    htmltext = "32140-05.htm";
                    break;
                case CREATED:
                    if (st.getPlayer().getLevel() >= 80) {
                        if ((!st.getPlayer().isAwaking()) && (st.getPlayer().getClassId().level() == ClassLevel.THIRD.ordinal())) {
                            htmltext = "32140-01.htm";
                        } else {
                            htmltext = "32140-02.htm";
                            st.exitCurrentQuest(true);
                        }
                    } else
                        htmltext = "32140-03.htm";
                    break;
                case STARTED:
                    if (st.getCond() == 1) {
                        htmltext = "32140-07.htm";
                    } else {
                        if ((st.getCond() != 2) || (st.getQuestItemsCount(34769) < 50L))
                            break;
                        htmltext = "32140-08.htm";
                        st.addExpAndSp(25000000, 28500000);
                        st.playSound("ItemSound.quest_finish");
                        st.exitCurrentQuest(false);
                    }
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
            if ((Rnd.chance(50)) && (ArrayUtils.contains(Монстры, npc.getNpcId()))) {
                if (st.getQuestItemsCount(34769) < 50L) {
                    st.giveItems(34769, 1);
                    if (st.getQuestItemsCount(34769) >= 50L) {
                        st.setCond(2);
                        st.playSound("ItemSound.quest_middle");
                    }
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
        return (player.getLevel() >= 80) && (!player.isAwaking()) && (player.getClassId().level() == ClassLevel.THIRD.ordinal());
    }
}
