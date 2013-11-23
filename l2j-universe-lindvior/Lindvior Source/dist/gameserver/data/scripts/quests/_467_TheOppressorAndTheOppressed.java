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
 * Time: 18:46
 */
public class _467_TheOppressorAndTheOppressed extends Quest implements ScriptFile {
    private static final int NPC_ADVENTURER_HELPER = 33463;
    private static final int NPC_DESMOND = 30855;
    private static final int ITEM_PURE_LEGACY_CORE = 19488;
    private static final int[] MONSTERS = {20650, 20648, 20647, 20649};

    public _467_TheOppressorAndTheOppressed() {
        super(false);

        addStartNpc(NPC_ADVENTURER_HELPER);
        addTalkId(NPC_DESMOND);
        addKillId(MONSTERS);
        addQuestItem(ITEM_PURE_LEGACY_CORE);
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
                    switch (st.getCond()) {
                        case 1:
                            htmltext = "33463-05.htm";
                            break;
                        case 2:
                            htmltext = "33463-06.htm";
                            break;
                    }

                    break;
                case DELAYED:
                    htmltext = "33463-07.htm";
            }
        } else if (npc.getNpcId() == NPC_DESMOND) {
            if (st.isStarted()) {
                if (st.getCond() == 2) {
                    htmltext = "30855-01.htm";

                    st.addExpAndSp(1879400, 1782000);
                    st.giveItems(57, 194000, true);    // Учитываем рейты
                    st.playSound("ItemSound.quest_finish");
                    st.exitCurrentQuest(this);
                }
            } else if (st.getState() == DELAYED) {
                htmltext = "30855-03.htm";
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
            // Максимум 2 итема
            if (st.rollAndGive(ITEM_PURE_LEGACY_CORE, 1, 2, 30, 25)) {
                st.setCond(2);
                st.playSound(SOUND_MIDDLE);
            }
        }

        return null;
    }

    public boolean isVisible(Player player) {
        QuestState qs = player.getQuestState(_467_TheOppressorAndTheOppressed.class);

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
