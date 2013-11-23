/*
 * Copyright Mazaffaka Project (c) 2012.
 */

package quests;

import l2p.commons.util.Rnd;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.model.quest.Quest;
import l2p.gameserver.model.quest.QuestState;
import l2p.gameserver.scripts.ScriptFile;

/**
 * Created with IntelliJ IDEA.
 * User: Darvin
 * Date: 21.11.12
 * Time: 18:37
 */
public class _181_DevilsStrikeBackAdventOfBalok extends Quest implements ScriptFile {
    private static final int NPC_FIOREN = 33044;
    private static final int MOB_BALOK = 29218;
    private static final int ITEM_BELETH_CONTRACT = 17592;
    private static final int REWARD_ENCHANT_ARMOR_R = 17527;
    private static final int REWARD_ENCHANT_WEAPON_R = 17526;
    private static final int REWARD_HARDENER_POUNCH_R = 34861;

    public _181_DevilsStrikeBackAdventOfBalok() {
        super(PARTY_ALL);

        addStartNpc(NPC_FIOREN);
        addKillId(MOB_BALOK);
        addQuestItem(ITEM_BELETH_CONTRACT);
        addLevelCheck(97, 99);
        addQuestCompletedCheck(_180_InfernalFlamesBurningInCrystalPrison.class);
    }

    @Override
    public String onTalk(NpcInstance npc, QuestState qs) {
        String htmltext = "noquest";

        if (qs == null) {
            return htmltext;
        }

        Player player = qs.getPlayer();

        if (npc.getNpcId() == NPC_FIOREN) {
            switch (qs.getState()) {
                case CREATED:
                    QuestState prevst = player.getQuestState(_180_InfernalFlamesBurningInCrystalPrison.class);

                    if ((player.getLevel() < 97) || (prevst == null) || (!prevst.isCompleted())) {
                        htmltext = "33044-02.htm";

                        qs.exitCurrentQuest(true);
                    } else {
                        htmltext = "33044-01.htm";
                    }

                    break;
                case STARTED:
                    if (qs.getCond() == 1) {
                        htmltext = "33044-07.htm";
                    } else {
                        if (qs.getCond() != 2) {
                            break;
                        }

                        htmltext = "33044-08.htm";
                    }

                    break;
                case DELAYED:
                    htmltext = "33044-03.htm";
            }
        }

        return htmltext;
    }

    @Override
    public String onEvent(String event, QuestState st, NpcInstance npc) {
        String htmltext = event;

        if (st == null) {
            return htmltext;
        }

        if (event.equalsIgnoreCase("33044-06.htm")) {
            st.setState(STARTED);
            st.setCond(1);
            st.playSound(SOUND_ACCEPT);
        } else if (event.equalsIgnoreCase("reward")) {
            st.addExpAndSp(886750000, 414855000);
            st.giveItems(57, 37128000, true);    // Учитываем рейты
            st.playSound("ItemSound.quest_finish");
            st.exitCurrentQuest(false);

            int rnd = Rnd.get(2);

            switch (rnd) {
                case 0:
                    st.giveItems(REWARD_ENCHANT_WEAPON_R, 2);
                    return "33044-09.htm";
                case 1:
                    st.giveItems(REWARD_ENCHANT_ARMOR_R, 2);
                    return "33044-10.htm";
                case 2:
                    st.giveItems(REWARD_HARDENER_POUNCH_R, 2);
                    return "33044-11.htm";
            }
        }

        return htmltext;
    }

    @Override
    public String onKill(NpcInstance npc, QuestState qs) {
        if ((npc == null) || (qs == null)) {
            return null;
        }

        if (qs.getCond() == 1) {
            if (npc.getNpcId() == MOB_BALOK) {
                qs.setCond(2);
                qs.giveItems(ITEM_BELETH_CONTRACT, 1);
                qs.playSound("ItemSound.quest_middle");
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
