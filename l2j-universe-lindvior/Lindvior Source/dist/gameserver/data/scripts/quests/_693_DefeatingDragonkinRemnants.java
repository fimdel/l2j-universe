/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package quests;

import l2p.commons.util.Rnd;
import l2p.gameserver.model.Party;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.model.quest.Quest;
import l2p.gameserver.model.quest.QuestState;
import l2p.gameserver.scripts.ScriptFile;

public class _693_DefeatingDragonkinRemnants extends Quest implements ScriptFile {

    private static final int EDRIC = 32527;
    private static final int REWARD = 14638;

    public _693_DefeatingDragonkinRemnants() {
        super(false);
        addStartNpc(EDRIC);
        addTalkId(EDRIC);
        addLevelCheck(85);
    }

    @Override
    public String onEvent(String event, QuestState st, NpcInstance npc) {

        if (st == null) {
            return event;
        }
        if (npc.getNpcId() == EDRIC) {
            if (event.equalsIgnoreCase("32527-05.htm")) {
                st.isStarted();
                st.unset("timeDiff");
            }
        }
        return event;
    }

    @Override
    public String onTalk(NpcInstance npc, QuestState st) {
        String htmltext = "noquest";
        if (st == null) {
            return htmltext;
        }
        if (npc.getNpcId() == EDRIC) {
            if (st.getPlayer().getLevel() < 75) {
                htmltext = "32527-00.htm";
            } else if (st.isCreated()) {
                htmltext = "32527-01.htm";
            } else if (st.getPlayer().isGM()) {
                st.isStarted();
                htmltext = "32527-10.html";
            } else if (st.getCond() == 1) {
                Party party = st.getPlayer().getParty();
                if (st.getInt("timeDiff") > 0) {
                    if (giveReward(st, st.getInt("timeDiff")))
                        htmltext = "32527-reward.html";
                    else {
                        htmltext = "32527-noreward.html";
                    }

                    st.unset("timeDiff");
                    st.unset("cond");
                    st.playSound(SOUND_MIDDLE);
                } else if (party == null) {
                    htmltext = "32527-noparty.html";
                } else if (!party.getPartyLeader().equals(st.getPlayer())) {
                    htmltext = "32527-noleader.html";
                } else {
                    for (Player pm : party.getPartyMembers()) {
                        QuestState state = pm.getQuestState(getName());
                        if ((state == null) || (state.getCond() != 1))
                            return htmltext = "32527-noquest.html";
                    }
                    htmltext = "32527-10.html";
                }
            }
        }
        return htmltext;
    }

    private boolean giveReward(QuestState st, int finishDiff) {
        if (Rnd.get(100) < 60) {
            if (finishDiff == 0)
                return false;
            if (finishDiff < 5)
                st.giveItems(REWARD, 1L);
            else if (finishDiff < 10)
                st.giveItems(REWARD, 1L);
            else if (finishDiff < 15)
                st.giveItems(REWARD, 1L);
            else if (finishDiff < 20)
                st.giveItems(REWARD, 1L);
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

