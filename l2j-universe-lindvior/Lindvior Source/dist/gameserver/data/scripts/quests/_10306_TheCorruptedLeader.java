/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package quests;

import l2p.commons.util.Rnd;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.model.quest.Quest;
import l2p.gameserver.model.quest.QuestState;
import l2p.gameserver.scripts.ScriptFile;

public class _10306_TheCorruptedLeader extends Quest implements ScriptFile {
    private static final int NPC_NAOMI_KASHERON = 32896;
    private static final int MOB_KIMERIAN = 32896;
    private static final int[] CRYSTALS =
            {
                    9552,
                    9553,
                    9554,
                    9555,
                    9556,
                    9557
            };

    public _10306_TheCorruptedLeader() {
        super(false);
        addStartNpc(NPC_NAOMI_KASHERON);
        addKillId(MOB_KIMERIAN);
        addQuestCompletedCheck(_10305_TheEndlessFutileEfforts.class);
        addLevelCheck(90, 99);
    }

    @Override
    public String onEvent(String event, QuestState st, NpcInstance npc) {
        if (st == null) {
            return event;
        }
        if (event.equalsIgnoreCase("32896-05.htm")) {
            st.setCond(1);
            st.setState(STARTED);
            st.playSound(SOUND_ACCEPT);
        } else if (event.equalsIgnoreCase("32896-08.htm")) {
            st.playSound(SOUND_FINISH);
            st.addExpAndSp(9479594, 4104484);
            st.giveItems(CRYSTALS[Rnd.get(CRYSTALS.length)], 1);
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
        QuestState prevst = player.getQuestState(_10305_TheEndlessFutileEfforts.class);
        if (npc.getNpcId() == NPC_NAOMI_KASHERON) {
            switch (st.getState()) {
                case COMPLETED:
                    htmltext = "32896-02.htm";
                    break;
                case CREATED:
                    if (player.getLevel() >= 90) {
                        if ((prevst != null) && (prevst.isCompleted())) {
                            htmltext = "32896-01.htm";
                        } else {
                            st.exitCurrentQuest(true);
                            htmltext = "32896-03.htm";
                        }
                    } else {
                        st.exitCurrentQuest(true);
                        htmltext = "32896-03.htm";
                    }
                    break;
                case STARTED:
                    if (st.getCond() == 1) {
                        htmltext = "32896-06.htm";
                    } else {
                        if (st.getCond() != 2) {
                            break;
                        }
                        htmltext = "32896-07.htm";
                    }
            }
        }
        return htmltext;
    }

    @Override
    public boolean isVisible(Player player) {
        QuestState qs = player.getQuestState(_10306_TheCorruptedLeader.class);
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
