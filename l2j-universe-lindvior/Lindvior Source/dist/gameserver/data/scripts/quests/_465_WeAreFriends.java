/*
 * Copyright Mazaffaka Project (c) 2012.
 */

package quests;

import l2p.commons.util.Rnd;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.Skill;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.model.quest.Quest;
import l2p.gameserver.model.quest.QuestState;
import l2p.gameserver.network.serverpackets.SocialAction;
import l2p.gameserver.scripts.ScriptFile;

/**
 * Created with IntelliJ IDEA.
 * User: Darvin
 * Date: 21.11.12
 * Time: 18:42
 */
public class _465_WeAreFriends extends Quest implements ScriptFile {
    private static final int NPC_FAIRY = 32921;
    private static final int NPC_FAIRY_FROM_COCCONE = 32923;
    private static final int MOB_LITTLE_COCCONE = 32919;
    private static final int ITEM_GRATITUDE_SIGN = 17377;
    private static final int REWARD_FOREST_FAIRY_HORN = 17378;
    private static final int REWARD_PROOF_OF_PROMISES = 30384;

    public _465_WeAreFriends() {
        super(false);

        addStartNpc(NPC_FAIRY);
        addKillId(NPC_FAIRY_FROM_COCCONE);
        addQuestItem(ITEM_GRATITUDE_SIGN);
        addAttackId(MOB_LITTLE_COCCONE);
        addSkillUseId(MOB_LITTLE_COCCONE);
        addLevelCheck(90, 99);
    }

    @Override
    public String onEvent(String event, QuestState st, NpcInstance npc) {
        if (event.equalsIgnoreCase("32921-02.htm")) {
            st.setState(STARTED);
            st.setCond(1);
            st.playSound(SOUND_ACCEPT);
        } else if (event.equals("32921-05.htm")) {
            st.giveItems(REWARD_FOREST_FAIRY_HORN, 1);
            st.giveItems(REWARD_PROOF_OF_PROMISES, 3);
            st.playSound("ItemSound.quest_finish");
            st.exitCurrentQuest(this);
        } else if (event.equalsIgnoreCase("32923-01.htm")) {
            st.playSound("ItemSound.quest_itemget");
            st.giveItems(ITEM_GRATITUDE_SIGN, 1);

            if (st.getQuestItemsCount(ITEM_GRATITUDE_SIGN) >= 2) {
                st.playSound("ItemSound.quest_middle");
                st.setCond(2);
            }

            npc.broadcastPacket(new SocialAction(npc.getObjectId(), 2));
            npc.deleteMe();
        }

        return event;
    }

    @Override
    public String onTalk(NpcInstance npc, QuestState st) {
        int npcId = npc.getNpcId();
        Player player = st.getPlayer();
        String htmltext = "noquest";

        if (npcId == NPC_FAIRY) {
            switch (st.getState()) {
                case DELAYED:
                    htmltext = "32921-noday.htm";
                    break;
                case CREATED:
                    if (player.getLevel() >= 90) {
                        htmltext = "32921-00.htm";
                    } else {
                        htmltext = "32921-nolvl.htm";

                        st.exitCurrentQuest(true);
                    }

                    break;
                case STARTED:
                    if (st.getCond() == 1) {
                        htmltext = "32921-03.htm";
                    } else {
                        if (st.getCond() != 2) {
                            break;
                        }

                        htmltext = "32921-04.htm";
                    }

                    break;
            }
        } else if (npc.getNpcId() == NPC_FAIRY_FROM_COCCONE) {
            if (st.isStarted()) {
                htmltext = "32923-00.htm";
            }
        }

        return htmltext;
    }

    @Override
    public String onAttack(NpcInstance npc, QuestState qs) {
        if (Rnd.chance(5) && (npc.getNpcId() == MOB_LITTLE_COCCONE)) {
            addSpawn(NPC_FAIRY_FROM_COCCONE, npc.getLoc(), 0, 30000);
        }

        return null;
    }

    public String onSkillUse(NpcInstance npc, Skill skill, QuestState qs) {
        if (skill.getId() == 12002) {
            if (Rnd.chance(10) && (npc.getNpcId() == 32919)) {
                addSpawn(NPC_FAIRY_FROM_COCCONE, npc.getLoc(), 0, 30000);
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
