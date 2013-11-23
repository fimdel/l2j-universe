/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package quests;

import l2p.commons.lang.ArrayUtils;
import l2p.commons.util.Rnd;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.SubClass;
import l2p.gameserver.model.base.SubClassType;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.model.quest.Quest;
import l2p.gameserver.model.quest.QuestState;
import l2p.gameserver.network.serverpackets.ExSubjobInfo;
import l2p.gameserver.network.serverpackets.SystemMessage2;
import l2p.gameserver.network.serverpackets.components.SystemMsg;
import l2p.gameserver.scripts.ScriptFile;

public class _177_StartTheNewDestiny extends Quest implements ScriptFile {
    private static final int Hadel = 33344;
    private static final int Ishuma = 32615;
    private static final int questitem_01 = 17718; //Руки Поверженного Гиганта
    private static final int questitem_02 = 17719; //Ноги Поверженного Гиганта
    private static final int questitem_03 = 17720; //Часть Руки Поверженного Гиганта
    private static final int questitem_04 = 17721; //Часть Ноги Поверженного Гиганта
    private static final int[] MOBS_1 = {21549, 21550, 21547, 21548, 21587}; // Лес Неупокоенных
    private static final int[] MOBS_2 = {22257, 22258, 22259, 22260}; // Кристальный Остров

    public _177_StartTheNewDestiny() {
        super(false);
        addStartNpc(Hadel);
        addTalkId(Hadel);
        addTalkId(Ishuma);
        addQuestItem(questitem_01, questitem_02, questitem_03, questitem_04);
        addKillId(MOBS_1);
        addKillId(MOBS_2);
    }

    @Override
    public String onEvent(String event, QuestState st, NpcInstance npc) {
        String htmltext = event;
        if (event.equalsIgnoreCase("33344_03.htm")) {
            st.setState(STARTED);
            st.setCond(1);
            st.playSound(SOUND_ACCEPT);
            st.set("subClassId", String.valueOf(st.getPlayer().getActiveClassId()));
        } else if (event.equalsIgnoreCase("33344_07.htm")) {
            st.takeAllItems(questitem_03);
            st.setCond(4);
        } else if (event.equalsIgnoreCase("33344_10.htm")) {
            st.giveItems(questitem_03, 10);
            st.setCond(7);
        } else if (event.equalsIgnoreCase("32615_03.htm")) {
            st.takeAllItems(questitem_03);
            st.takeAllItems(questitem_04);
            st.setCond(8);
        } else if (event.equalsIgnoreCase("33344_13.htm")) {
            st.takeAllItems(questitem_01);
            st.takeAllItems(questitem_02);
        } else if (event.equalsIgnoreCase("33344_16.htm") || event.equalsIgnoreCase("33344_17.htm") || event.equalsIgnoreCase("33344_18.htm")) {
            Player player = st.getPlayer();
            if (player.isBaseClassActive())
                return "no_subclass.htm";

            if (st.getInt("subClassId") != player.getActiveClassId())
                return "no_subclass.htm";

            SubClass sub = player.getActiveSubClass();
            if (sub == null)
                return "Error! Active Subclass is null!";

            if (sub.isDouble())
                return "Error! You already have double-class!"; // TODO: Заменить на более адекватное сообщение в .htm

            sub.setType(SubClassType.DOUBLE_SUBCLASS);
            player.sendPacket(new ExSubjobInfo(player, true));

            int classId = sub.getClassId();
            player.sendPacket(new SystemMessage2(SystemMsg.SUBCLASS_S1_HAS_BEEN_UPGRADED_TO_DUEL_CLASS_S2_CONGRATULATIONS).addClassName(classId).addClassName(classId));

            if (event.equalsIgnoreCase("33344_16.htm")) // Красный СА
                st.giveItems(10480, 1);
            else if (event.equalsIgnoreCase("33344_17.htm")) // Синий СА
                st.giveItems(10481, 1);
            else if (event.equalsIgnoreCase("33344_18.htm")) // Зеленый СА
                st.giveItems(10482, 1);

            st.giveItems(18168, 1);
            st.addExpAndSp(175739575, 2886300);
            st.playSound(SOUND_FINISH);
            st.exitCurrentQuest(false);
        }
        return htmltext;
    }

    @Override
    public String onTalk(NpcInstance npc, QuestState st) {
        Player player = st.getPlayer();
        if (player.isBaseClassActive())
            return "no_subclass.htm";

        String htmltext = "noquest";
        int npcId = npc.getNpcId();
        int cond = st.getCond();
        if (npcId == Hadel) {
            if (cond == 0) {
                if (player.getLevel() >= 80)
                    htmltext = "33344_01.htm";
                else
                    htmltext = "33344_00.htm";
            } else if (cond == 1)
                htmltext = "33344_04.htm";
            else if (cond == 2)
                htmltext = "33344_04.htm";
            else if (cond == 3) {
                if (st.getQuestItemsCount(questitem_03) >= 10)
                    htmltext = "33344_05.htm";
            } else if (cond == 4)
                htmltext = "33344_08.htm";
            else if (cond == 5)
                htmltext = "33344_08.htm";
            else if (cond == 6) {
                if (st.getQuestItemsCount(questitem_04) >= 10)
                    htmltext = "33344_09.htm";
            } else if (cond == 7)
                htmltext = "33344_11.htm";
            else if (cond == 9) {
                if (st.getQuestItemsCount(questitem_01) >= 2 && st.getQuestItemsCount(questitem_02) >= 2)
                    htmltext = "33344_12.htm";
                else
                    htmltext = "33344_14.htm";
            } else
                htmltext = "no_subclass.htm";
        } else if (npcId == Ishuma) {
            if (cond == 7) {
                if (st.getQuestItemsCount(questitem_03) >= 10 && st.getQuestItemsCount(questitem_04) >= 10) {
                    htmltext = "32615_01.htm";
                    st.setCond(8);
                }
            } else if (cond == 8) {
                st.giveItems(questitem_01, 2);
                st.giveItems(questitem_02, 2);
                htmltext = "32615_04.htm";
                st.setCond(9);
            } else if (cond == 9)
                htmltext = "32615_05.htm";
        } else
            htmltext = "no_subclass.htm";
        return htmltext;
    }

    @Override
    public String onKill(NpcInstance npc, QuestState st) {
        int npcId = npc.getNpcId();
        int cond = st.getCond();

        if (cond == 1 && ArrayUtils.contains(MOBS_1, npcId) && Rnd.chance(30)) {
            if (st.getQuestItemsCount(questitem_03) < 1) {
                st.giveItems(questitem_03, 1);
                st.playSound(SOUND_ITEMGET);
            } else if (st.getQuestItemsCount(questitem_03) >= 1) {
                st.playSound(SOUND_MIDDLE);
                st.setCond(2);
            }
        } else if (cond == 2 && ArrayUtils.contains(MOBS_1, npcId) && Rnd.chance(30)) {
            if (st.getQuestItemsCount(questitem_03) < 10) {
                st.giveItems(questitem_03, 1);
                st.playSound(SOUND_ITEMGET);
            }
            if (st.getQuestItemsCount(questitem_03) >= 10) {
                st.playSound(SOUND_MIDDLE);
                st.setCond(3);
            }
        } else if (cond == 4 && ArrayUtils.contains(MOBS_2, npcId) && Rnd.chance(30)) {
            if (st.getQuestItemsCount(questitem_04) < 1) {
                st.giveItems(questitem_04, 1);
                st.playSound(SOUND_ITEMGET);
            } else if (st.getQuestItemsCount(questitem_04) >= 1) {
                st.playSound(SOUND_MIDDLE);
                st.setCond(5);
            }
        } else if (cond == 5 && ArrayUtils.contains(MOBS_2, npcId) && Rnd.chance(30)) {
            if (st.getQuestItemsCount(questitem_04) < 10) {
                st.giveItems(questitem_04, 1);
                st.playSound(SOUND_ITEMGET);
            }
            if (st.getQuestItemsCount(questitem_04) >= 10) {
                st.playSound(SOUND_MIDDLE);
                st.setCond(6);
            }
        }
        return null;
    }

    @Override
    public void onLoad() {
    }

    @Override
    public void onReload() {
    }

    @Override
    public void onShutdown() {
    }
}
