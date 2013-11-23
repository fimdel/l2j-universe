/*
 * Copyright Mazaffaka Project (c) 2012.
 */

package quests;

import l2p.gameserver.model.Player;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.model.quest.Quest;
import l2p.gameserver.model.quest.QuestState;
import l2p.gameserver.scripts.ScriptFile;
import org.apache.commons.lang3.ArrayUtils;

/**
 * Created with IntelliJ IDEA.
 * User: Darvin
 * Date: 21.11.12
 * Time: 18:52
 */
public class _470_DivinityProtector extends Quest implements ScriptFile {
    private static final int NPC_ADVENTURER_HELPER = 33463;
    private static final int NPC_AGRIPEL = 31348;
    private static final int ITEM_REMNANT_ASH = 19489;
    private static final int[] MONSTERS = {21520, 21521, 21522, 21523, 21524, 21525, 21526, 21527, 21528, 21542, 21543, 21529, 21530, 21541, 21532, 21533, 21534, 21535, 21536, 21545, 21546, 21537, 21538, 21539, 21540, 21544};

    public _470_DivinityProtector() {
        super(false);

        addStartNpc(NPC_ADVENTURER_HELPER);
        addTalkId(NPC_AGRIPEL);
        addKillId(MONSTERS);
        addQuestItem(ITEM_REMNANT_ASH);
        addLevelCheck(60, 64);
    }

    @Override
    public String onEvent(String event, QuestState st, NpcInstance npc) {
        if (st == null) {
            return "noquest";
        }

        if (event.equalsIgnoreCase("33463-04.htm")) {
            st.startQuest();
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

        if (npc.getNpcId() == NPC_ADVENTURER_HELPER) {
            if (player.getLevel() < 60) {
                st.exitCurrentQuest(true);
                return getLowLevelMsg(60);
            }

            if (player.getLevel() > 64) {
                st.exitCurrentQuest(true);
                return getHighLevelMsg(64);
            }

            switch (st.getState()) {
                case CREATED:
                    htmltext = "33463-01.htm";
                    break;
                case STARTED:
                    if (st.getCond() == 1) {
                        htmltext = "33463-05.htm";
                    } else {
                        if (st.getCond() != 2) {
                            break;
                        }

                        htmltext = "33463-06.htm";

                        st.addExpAndSp(1879400, 1782000);
                        st.giveItems(57, 194000, true);    // Учитываем рейты
                        st.playSound(SOUND_FINISH);
                        st.exitCurrentQuest(this);
                    }

                    break;
                case DELAYED:
                    htmltext = "daily";
            }
        } else if (npc.getNpcId() == NPC_AGRIPEL) {
            if (st.isStarted()) {
                if (st.getCond() == 2) {
                    htmltext = "31348-01.htm";

                    st.exitCurrentQuest(false);
                } else {
                    htmltext = "31348-02.htm";
                }
            } else if (st.getState() == DELAYED) {
                htmltext = "31348-03.htm";
            }
        }

        return htmltext;
    }

    @Override
    public String onKill(NpcInstance npc, QuestState st) {
        if ((npc == null) || (st == null)) {
            return null;
        }

        if (ArrayUtils.contains(MONSTERS, npc.getNpcId()) && (st.getCond() == 1)) {
            // Максимум 3 итема
            if (st.rollAndGive(ITEM_REMNANT_ASH, 1, 3, 20, 50)) {
                st.setCond(2);
                st.playSound(SOUND_MIDDLE);
            }
        }

        return null;
    }

    public boolean isVisible(Player player) {
        QuestState qs = player.getQuestState(_470_DivinityProtector.class);

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
