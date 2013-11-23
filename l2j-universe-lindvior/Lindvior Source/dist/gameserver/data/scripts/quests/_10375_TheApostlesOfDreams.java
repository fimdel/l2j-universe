/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package quests;

import gnu.trove.TIntIntHashMap;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.base.ClassLevel;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.model.quest.Quest;
import l2p.gameserver.model.quest.QuestState;
import l2p.gameserver.network.serverpackets.ExQuestNpcLogList;

/**
 * Created with IntelliJ IDEA.
 * User: Darvin
 * Date: 18.09.12
 * Time: 3:32
 */
public class _10375_TheApostlesOfDreams extends Quest {
    private static final int Зения = 32140;
    private static final int КошмарСмерти = 23191;
    private static final int КошмарТьмы = 23192;
    private static final int КошмарБезумия = 23197;
    private static final int КошмарБезмолвия = 23198;

    public _10375_TheApostlesOfDreams() {
        super(false);
        addStartNpc(Зения);
        addTalkId(Зения);
        addKillId(КошмарСмерти, КошмарТьмы, КошмарБезумия, КошмарБезмолвия);
    }

    @Override
    public String onEvent(String event, QuestState st, NpcInstance npc) {
        int i = -1;
        switch (event.hashCode()) {
            case -1733495816:
                if (!event.equals("32140-06.htm")) break;
                i = 0;
                break;
            case -1730725253:
                if (!event.equals("32140-09.htm")) break;
                i = 1;
        }
        switch (i) {
            case 0:
                st.startQuest();
                break;
            case 1:
                st.setCond(3);
                st.playSound("ItemSound.quest_middle");
        }

        return event;
    }

    @Override
    public String onTalk(NpcInstance npc, QuestState st) {
        String htmltext = getNoQuestMsg(st.getPlayer());

        if (npc.getNpcId() == Зения) {
            switch (st.getState()) {
                case COMPLETED:
                    htmltext = "32140-05.htm";
                    break;
                case CREATED:
                    if (st.getPlayer().getLevel() >= 80) {
                        QuestState prevst = st.getPlayer().getQuestState("10374_SourceOfNightmares");
                        if ((!st.getPlayer().isAwaking()) && (st.getPlayer().getClassId().level() == ClassLevel.THIRD.ordinal()) && (prevst != null) && (prevst.isCompleted())) {
                            htmltext = "32140-01.htm";
                        } else {
                            htmltext = "32140-03.htm";
                            st.exitQuest(true);
                        }
                    } else {
                        htmltext = "32140-04.htm";
                    }
                    break;
                case STARTED:
                    switch (st.getCond()) {
                        case 1:
                            htmltext = "32140-07.htm";
                            break;
                        case 2:
                            htmltext = "32140-08.htm";
                            break;
                        case 3:
                            htmltext = "32140-10.htm";
                            break;
                        case 4:
                            htmltext = "32140-11.htm";
                            st.giveAdena(498700);
                            st.addExpAndSp(24782300, 28102300);
                            st.playSound("ItemSound.quest_finish");
                            st.exitQuest(false);
                    }
            }

        }

        return htmltext;
    }

    @Override
    public String onKill(NpcInstance npc, QuestState st) {

        if (st.getCond() == 1) {
            int _1 = st.getInt("_1");
            int _2 = st.getInt("_2");

            TIntIntHashMap moblist = new TIntIntHashMap();

            if ((npc.getNpcId() == КошмарСмерти) && (_1 < 5)) {
                _1++;
                st.set("_1", String.valueOf(_1));
            } else if ((npc.getNpcId() == КошмарТьмы) && (_2 < 5)) {
                _2++;
                st.set("_2", String.valueOf(_2));
            }
            if (_1 + _2 >= 10) {
                st.setCond(2);
                st.playSound("ItemSound.quest_middle");
            }
            moblist.put(1023191, _1);
            moblist.put(1023192, _2);
            st.getPlayer().sendPacket(new ExQuestNpcLogList(10375, moblist));
        } else if (st.getCond() == 3) {
            int _3 = st.getInt("_3");
            int _4 = st.getInt("_4");

            TIntIntHashMap moblist = new TIntIntHashMap();

            if ((npc.getNpcId() == КошмарБезумия) && (_3 < 5)) {
                _3++;
                st.set("_3", String.valueOf(_3));
            } else if ((npc.getNpcId() == КошмарБезмолвия) && (_4 < 5)) {
                _4++;
                st.set("_4", String.valueOf(_4));
            }
            if (_3 + _4 >= 10) {
                st.setCond(4);
                st.playSound("ItemSound.quest_middle");
            }
            moblist.put(1023197, _3);
            moblist.put(1023198, _4);
            st.getPlayer().sendPacket(new ExQuestNpcLogList(10375, moblist));
        }
        return null;
    }

    @Override
    public boolean canBeStarted(Player player) {
        QuestState prevst = player.getQuestState("10374_SourceOfNightmares");

        return (prevst != null) && (prevst.isCompleted()) && (player.getLevel() >= 80) && (!player.isAwaking()) && (player.getClassId().level() == ClassLevel.THIRD.ordinal());
    }
}
