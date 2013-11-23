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
 * Time: 3:25
 */
public class _10374_SourceOfNightmares extends Quest {
    private static final int Андрей = 31292;
    private static final int Агнес = 31588;
    private static final int Зения = 32140;
    private static final int ВоительМиражей = 23186;
    private static final int ВоинМиражей = 23187;
    private static final int СтрелокМиражей = 23188;
    private static final int ШаманМиражей = 23189;
    private static final int МученикМиражей = 23190;

    public _10374_SourceOfNightmares() {
        super(false);
        addStartNpc(Андрей);
        addTalkId(Андрей, Агнес, Зения);
        addKillId(ВоительМиражей, ВоинМиражей, СтрелокМиражей, ШаманМиражей, МученикМиражей);
    }

    @Override
    public String onEvent(String event, QuestState st, NpcInstance npc) {
        if (event.equals("31292-07.htm")) {
            st.startQuest();

        } else if (event.equals("31588-02.htm")) {
            st.setCond(2);
            st.playSound("ItemSound.quest_middle");

        } else if (event.equals("32140-02.htm")) {
            st.setCond(3);
            st.playSound("ItemSound.quest_middle");
        }

        return event;
    }

    @Override
    public String onTalk(NpcInstance npc, QuestState st) {
        String htmltext = getNoQuestMsg(st.getPlayer());

        if (npc.getNpcId() == Андрей) {
            switch (st.getState()) {
                case COMPLETED:
                    htmltext = "31292-06.htm";
                    break;
                case CREATED:
                    if (st.getPlayer().getLevel() >= 80) {
                        if ((!st.getPlayer().isAwaking()) && (st.getPlayer().getClassId().level() == ClassLevel.THIRD.ordinal())) {
                            htmltext = "31292-01.htm";
                        } else {
                            htmltext = "31292-04.htm";
                            st.exitQuest(true);
                        }
                    } else
                        htmltext = "31292-05.htm";
                    break;
                case STARTED:
                    if (st.getCond() != 1) break;
                    htmltext = "31292-08.htm";
            }

        } else if (npc.getNpcId() == Агнес) {
            if (st.isStarted()) {
                if (st.getCond() == 1)
                    htmltext = "31588-01.htm";
                else if (st.getCond() == 2)
                    htmltext = "31588-03.htm";
            }
        } else if (npc.getNpcId() == Зения) {
            if (st.isStarted()) {
                switch (st.getCond()) {
                    case 2:
                        htmltext = "32140-01.htm";
                        break;
                    case 3:
                        htmltext = "32140-03.htm";
                        break;
                    case 4:
                        htmltext = "32140-04.htm";
                        st.addExpAndSp(23747100, 27618200);
                        st.giveAdena(500560);
                        st.playSound("ItemSound.quest_finish");
                        st.exitQuest(false);
                }
            }
        }

        return htmltext;
    }

    @Override
    public String onKill(NpcInstance npc, QuestState st) {
        if (st.getCond() == 3) {
            int _1 = st.getInt("_1");
            int _2 = st.getInt("_2");
            int _3 = st.getInt("_3");
            int _4 = st.getInt("_4");
            int _5 = st.getInt("_5");

            TIntIntHashMap moblist = new TIntIntHashMap();

            if ((npc.getNpcId() == ВоительМиражей) && (_1 < 15)) {
                _1++;
                st.set("_1", String.valueOf(_1));
            } else if ((npc.getNpcId() == ВоинМиражей) && (_2 < 10)) {
                _2++;
                st.set("_2", String.valueOf(_2));
            } else if ((npc.getNpcId() == СтрелокМиражей) && (_3 < 5)) {
                _3++;
                st.set("_3", String.valueOf(_3));
            } else if ((npc.getNpcId() == ШаманМиражей) && (_4 < 5)) {
                _4++;
                st.set("_4", String.valueOf(_4));
            } else if ((npc.getNpcId() == МученикМиражей) && (_5 < 5)) {
                _5++;
                st.set("_5", String.valueOf(_5));
            }
            if (_1 + _2 + _3 + _4 + _5 >= 40) {
                st.setCond(4);
                st.playSound("ItemSound.quest_middle");
            }
            moblist.put(1023186, _1);
            moblist.put(1023187, _2);
            moblist.put(1023188, _3);
            moblist.put(1023189, _4);
            moblist.put(1023190, _5);
            st.getPlayer().sendPacket(new ExQuestNpcLogList(10374, moblist));
        }
        return null;
    }

    @Override
    public boolean canBeStarted(Player player) {
        return player.getLevel() >= 80 && !player.isAwaking() && player.getClassId().level() == ClassLevel.THIRD.ordinal();
    }
}
