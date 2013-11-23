/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package quests;

import l2p.gameserver.model.Player;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.model.quest.Quest;
import l2p.gameserver.model.quest.QuestState;
import l2p.gameserver.scripts.ScriptFile;

public class _10317_OrbisWitch extends Quest implements ScriptFile {
    private static final int NPC_OPERA = 32946;
    private static final int NPC_LYDIA = 32892;

    public _10317_OrbisWitch() {
        super(PARTY_NONE);
        addStartNpc(NPC_OPERA);
        addTalkId(NPC_LYDIA);
        addLevelCheck(95, 99);
        addQuestCompletedCheck(_10316_UndecayingMemoryOfThePast.class);
    }

    @Override
    public String onEvent(String event, QuestState st, NpcInstance npc) {
        if (st == null) {
            return "noquest";
        }
        if (event.equalsIgnoreCase("32946-08.htm")) {
            st.setCond(1);
            st.setState(STARTED);
            st.playSound(SOUND_ACCEPT);
        } else if (event.equalsIgnoreCase("32892-02.htm")) {
            st.playSound(SOUND_FINISH);
            st.addExpAndSp(74128050, 3319695);
            st.giveItems(ADENA_ID, 506760, true);
            st.exitCurrentQuest(false);
        }
        return event;
    }

    @Override
    public String onTalk(NpcInstance npc, QuestState st) {
        String htmltext = "noquest";
        if (st == null) {
            return htmltext;
        }
        Player player = st.getPlayer();
        QuestState previous = player.getQuestState(_10316_UndecayingMemoryOfThePast.class);
        if (npc.getNpcId() == NPC_OPERA) {
            if ((previous == null) || (!previous.isCompleted()) || (player.getLevel() < 95)) {
                st.exitCurrentQuest(true);
                return "32946-03.htm";
            }
            switch (st.getState()) {
                case COMPLETED:
                    htmltext = "32946-02.htm";
                    break;
                case CREATED:
                    htmltext = "32946-01.htm";
                    break;
                case STARTED:
                    if (st.getCond() != 1) {
                        break;
                    }
                    htmltext = "32946-09.htm";
            }
        } else if (npc.getNpcId() == NPC_LYDIA) {
            if (st.isStarted()) {
                if (st.getCond() == 1) {
                    htmltext = "32892-01.htm";
                }
            } else if (st.isCompleted()) {
                htmltext = "32892-03.htm";
            }
        }
        return htmltext;
    }

    @Override
    public boolean isVisible(Player player) {
        QuestState qs = player.getQuestState(_10317_OrbisWitch.class);
        return ((qs == null) && isAvailableFor(player)) || ((qs != null) && qs.isNowAvailableByTime());
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
