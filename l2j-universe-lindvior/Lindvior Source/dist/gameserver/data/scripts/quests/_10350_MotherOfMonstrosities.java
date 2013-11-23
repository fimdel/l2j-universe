/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package quests;

import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.model.quest.Quest;
import l2p.gameserver.model.quest.QuestState;
import l2p.gameserver.scripts.ScriptFile;

/*
 **@author coldy
 * TODO: offlike EN htmls
 */
public class _10350_MotherOfMonstrosities extends Quest implements ScriptFile {
    private static final int NPC_CHICHIRIN = 30539;
    private static final int NPC_TRASKEN_BODY = 33159;
    private static final int ITEM_WYRM_PART = 17734;

    @Override
    public void onLoad() {
    }

    @Override
    public void onReload() {
    }

    @Override
    public void onShutdown() {
    }

    public _10350_MotherOfMonstrosities() {
        super(PARTY_NONE);

        addStartNpc(NPC_CHICHIRIN);
        addTalkId(NPC_TRASKEN_BODY);
        addQuestItem(ITEM_WYRM_PART);
        addLevelCheck(40, 74);
    }

    @Override
    public String onEvent(String event, QuestState st, NpcInstance npc) {
        String htmltext = event;

        if (event.equalsIgnoreCase("take_item")) {
            st.giveItems(ITEM_WYRM_PART, 1);
            st.setCond(2);
            st.playSound("ItemSound.quest_middle");
        } else if (event.equalsIgnoreCase("30539-08.htm")) {
            st.setState(STARTED);
            st.setCond(1);
            st.playSound(SOUND_ACCEPT);
        }

        return htmltext;
    }

    @Override
    public String onTalk(NpcInstance npc, QuestState st) {
        int cond = st.getCond();
        int npcId = npc.getNpcId();
        String htmltext = "noquest";

        if (npc.getNpcId() == NPC_CHICHIRIN) {
            if (st.getPlayer().getLevel() < 40) {
                st.exitCurrentQuest(true);
                return "30539-02.htm";
            }

            if (st.getPlayer().getLevel() > 75) {
                st.exitCurrentQuest(true);
                return "30539-05.htm";
            }

            switch (st.getState()) {
                case COMPLETED:
                    htmltext = "30539-03.htm";
                    break;
                case CREATED:
                    htmltext = "30539-01.htm";
                    break;
                case STARTED:
                    if (st.getCond() == 1) {
                        htmltext = "30539-09.htm";
                    } else {
                        if (st.getCond() != 2) {
                            break;
                        }

                        htmltext = "30539-10.htm";

                        st.addExpAndSp(200454, 135933);
                        st.giveItems(57, 40299, true);
                        st.playSound("ItemSound.quest_finish");
                        st.exitCurrentQuest(false);
                    }
            }
        } else if (npc.getNpcId() == NPC_TRASKEN_BODY) {
            if (st.isStarted()) {
                if (st.getCond() == 1) {
                    htmltext = "33159-01.htm";
                } else if (st.getCond() == 2) {
                    htmltext = "33159-03.htm";
                }
            }
        }

        return htmltext;
    }
}
