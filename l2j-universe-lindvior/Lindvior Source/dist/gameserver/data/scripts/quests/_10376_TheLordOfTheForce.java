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
 * Time: 3:41
 */
public class _10376_TheLordOfTheForce extends Quest {
    private static final int Зения = 32140;
    private static final int Кеске = 32139;
    private static final int Агнес = 31588;
    private static final int Андрей = 31292;
    private static final int Владивейн = 27481;
    private static final int ПлохаяМагическаяЗаколка = 32700;

    public _10376_TheLordOfTheForce() {
        super(false);
        addStartNpc(Зения);
        addTalkId(Зения, Кеске, Агнес, Андрей);
        addKillId(Владивейн);
    }

    @Override
    public String onEvent(String event, QuestState st, NpcInstance npc) {
        if (event.equals("32140-06.htm")) {
            st.startQuest();

        } else if (event.equals("32139-03.htm")) {
            st.setCond(2);
            st.playSound("ItemSound.quest_middle");

        } else if (event.equals("enterInstance")) {
            st.setCond(3);
            st.playSound("ItemSound.quest_middle");

            addSpawn(27481, st.getPlayer().getLoc(), 0, 0);
            //     ((PlayableAI)mob).canAttackCharacter(st.getPlayer());
            return null;
        } else if (event.equals("32139-08.htm")) {
            st.setCond(5);
            st.playSound("ItemSound.quest_middle");

        } else if (event.equals("teleport_goddard")) {
            st.getPlayer().teleToLocation(149597, -57249, -2976);
            return null;
        } else if (event.equals("31588-03.htm")) {
            st.setCond(6);
            st.playSound("ItemSound.quest_middle");

        } else if (event.equals("31292-03.htm")) {
            st.addExpAndSp(121297500, 48433200);
            st.giveItem(ПлохаяМагическаяЗаколка);
            st.playSound("ItemSound.quest_finish");
            st.exitQuest(false);
        }

        return event;
    }

    @Override
    public String onTalk(NpcInstance npc, QuestState st) {
        String htmltext = getNoQuestMsg(st.getPlayer());

        if (npc.getNpcId() == 32140) {
            switch (st.getState()) {
                case COMPLETED:
                    htmltext = "32140-05.htm";
                    break;
                case CREATED:
                    if (st.getPlayer().getLevel() >= 80) {
                        QuestState prevst = st.getPlayer().getQuestState("10375_TheApostlesOfDreams");
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
                    htmltext = "32140-07.htm";
            }

        } else if (npc.getNpcId() == 32139) {
            if (st.isStarted()) {
                switch (st.getCond()) {
                    case 1:
                        htmltext = "32139-02.htm";
                        break;
                    case 2:
                    case 3:
                        htmltext = "32139-03.htm";
                        break;
                    case 4:
                        htmltext = "32139-04.htm";
                        break;
                    case 5:
                        htmltext = "32139-08.htm";
                }
            }

        } else if (npc.getNpcId() == 31588) {
            if (st.isStarted()) {
                if (st.getCond() == 5)
                    htmltext = "31588-01.htm";
                else if (st.getCond() == 6)
                    htmltext = "31588-03.htm";
            }
        } else if (npc.getNpcId() == 31292) {
            if (st.isStarted()) {
                if (st.getCond() == 6)
                    htmltext = "31292-01.htm";
            }
        }
        return htmltext;
    }

    @Override
    public String onKill(NpcInstance npc, QuestState st) {
        if (st.getCond() == 3) {
            if (npc.getNpcId() == 27481) {
                TIntIntHashMap moblist = new TIntIntHashMap();
                moblist.put(1027481, 1);
                st.setCond(4);
                st.playSound("ItemSound.quest_middle");
                st.getPlayer().sendPacket(new ExQuestNpcLogList(10375, moblist));
            }
        }
        return null;
    }

    @Override
    public boolean canBeStarted(Player player) {
        QuestState prevst = player.getQuestState("10375_TheApostlesOfDreams");

        return (prevst != null) && (prevst.isCompleted()) && (player.getLevel() >= 80) && (!player.isAwaking()) && (player.getClassId().level() == ClassLevel.THIRD.ordinal());
    }
}
