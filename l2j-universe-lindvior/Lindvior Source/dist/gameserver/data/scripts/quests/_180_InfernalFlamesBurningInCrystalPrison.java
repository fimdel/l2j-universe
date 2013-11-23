/*
 * Copyright Mazaffaka Project (c) 2012.
 */

package quests;

import l2p.gameserver.model.Player;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.model.quest.Quest;
import l2p.gameserver.model.quest.QuestState;
import l2p.gameserver.scripts.ScriptFile;

/**
 * Created with IntelliJ IDEA.
 * User: Darvin
 * Date: 21.11.12
 * Time: 18:36
 */
public class _180_InfernalFlamesBurningInCrystalPrison extends Quest implements ScriptFile {
    private static final int NPC_FIOREN = 33044;
    private static final int MOB_BAYLOR = 29213;
    private static final int ITEM_BELETH_MARK = 17591;
    private static final int REWARD_ENCHANT_ARMOR_R = 17527;

    public _180_InfernalFlamesBurningInCrystalPrison() {
        super(PARTY_ALL);

        addStartNpc(NPC_FIOREN);
        addKillId(MOB_BAYLOR);
        addQuestItem(ITEM_BELETH_MARK);
        addLevelCheck(97, 99);
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
                    if (player.getLevel() < 97) {
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

                        qs.addExpAndSp(14000000, 6400000);
                        qs.giveItems(REWARD_ENCHANT_ARMOR_R, 1);
                        qs.playSound("ItemSound.quest_finish");
                        qs.exitCurrentQuest(false);
                    }

                    break;
                case COMPLETED:
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
        }

        return htmltext;
    }

    @Override
    public String onKill(NpcInstance npc, QuestState qs) {
        if ((npc == null) || (qs == null)) {
            return null;
        }

        if (qs.getCond() == 1) {
            if (npc.getNpcId() == MOB_BAYLOR) {
                qs.setCond(2);
                qs.giveItems(ITEM_BELETH_MARK, 1);
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
