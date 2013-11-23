/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package quests;

import l2p.gameserver.model.Player;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.model.quest.Quest;
import l2p.gameserver.model.quest.QuestState;
import l2p.gameserver.scripts.ScriptFile;

public class _10749_MysteriousSuggestion2 extends Quest implements ScriptFile {

    private static final int ТаинственныйЛакей = 33685;
    private static final int СвидетельствоУчастия2 = 35551;
    private static final int ТаинственныйЗнак = 34900;

    public _10749_MysteriousSuggestion2() {
        super(false);
        addStartNpc(ТаинственныйЛакей);
        addTalkId(ТаинственныйЛакей);
        addQuestItem(СвидетельствоУчастия2);
    }

    @Override
    public String onEvent(String event, QuestState st, NpcInstance npc) {
        if (event.equals("quest_accept") && !st.isCompleted()) {
            st.startQuest();
            return "grankain_lumiere_q10749_03.htm";
        }
        if (event.equals("slushat")) {
            return "grankain_lumiere_q10749_02.htm";
        }
        return null;
    }

    @Override
    public String onTalk(NpcInstance npc, QuestState st) {
        String htmltext = getNoQuestMsg(st.getPlayer());

        if (st == null) {
            return htmltext;
        }

        if ((st.isNowAvailable()) && (st.isCompleted())) {
            st.setState(0);
        }

        if (npc.getNpcId() == ТаинственныйЛакей) {
            switch (st.getState()) {
                case 0:
                    if (st.getPlayer().getLevel() < 76 || st.getPlayer().getClan() == null || st.getPlayer().getClan().getLevel() < 3) {
                        st.exitQuest(true);
                        return "grankain_lumiere_q10749_04.htm";
                    }

                    return "grankain_lumiere_q10749_01.htm";
                case 1:
                    if (st.getCond() == 1) {
                        return "grankain_lumiere_q10749_06.htm";
                    }
                    if (st.getCond() == 2) {
                        st.giveItems(ТаинственныйЗнак, 2);
                        st.playSound(SOUND_FINISH);
                        st.exitQuest(true);
                        return "grankain_lumiere_q10749_07.htm";
                    }
                    break;
                case 2:
                    return "grankain_lumiere_q10749_05.htm";
            }
        }

        return htmltext;
    }

    @Override
    public void onHaosBattleEnd(Player player, boolean isWinner) {
        if (player != null) {
            QuestState st = player.getQuestState(getName());
            if (st != null && st.isStarted()) {
                if (st.getQuestItemsCount(СвидетельствоУчастия2) < 5) {
                    st.giveItem(СвидетельствоУчастия2);
                    st.playSound(SOUND_ITEMGET);
                    if (st.getQuestItemsCount(СвидетельствоУчастия2) == 5) {
                        st.setCond(2);
                        st.playSound(SOUND_MIDDLE);
                    }
                }
            }
        }
    }

    @Override
    public boolean canBeStarted(Player player) {
        if (player.getLevel() >= 76 && player.getClan() != null && player.getClan().getLevel() > 3) {
            return true;
        }

        return false;
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
