/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package quests;

import l2p.gameserver.model.Party;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.model.quest.Quest;
import l2p.gameserver.model.quest.QuestState;
import l2p.gameserver.scripts.ScriptFile;

/**
 * Created with IntelliJ IDEA.
 * User: Darvin
 * Date: 06.08.12
 * Time: 23:35
 */
public class _150_ExtremeChallengePrimalMotherResurrected extends Quest implements ScriptFile {

    private static final int _Rumiese = 33293;
    private static final int _IstinaHard = 29196;
    private static final int _topShilensMark = 17590;
    private static final int _IstinaSoul = 34883;

    public _150_ExtremeChallengePrimalMotherResurrected() {
        super(false);
        addStartNpc(_Rumiese);
        addTalkId(_Rumiese);
        addKillId(_IstinaHard);
        addQuestItem(_topShilensMark);
    }

    @Override
    public String onEvent(String event, QuestState st, NpcInstance npc) {
        String htmltext = event;
        if (event.equalsIgnoreCase("33293-06.htm")) {
            st.setCond(1);
            st.setState(STARTED);
            st.playSound(SOUND_ACCEPT);
        }
        return htmltext;
    }

    @Override
    public String onTalk(NpcInstance npc, QuestState st) {
        Player player = st.getPlayer();
        String htmlText = NO_QUEST_DIALOG;
        int cond = st.getCond();
        int id = st.getState();

        if (cond == 0) {
            if (player.getLevel() < 97) {
                st.exitCurrentQuest(true);
                htmlText = "33293-02";
            }
            QuestState Rumiese = player.getQuestState(_149_PrimalMotherIstina.class);
            if (id == CREATED && Rumiese != null && Rumiese.getState() != COMPLETED) {
                st.exitCurrentQuest(true);
                htmlText = "33293-02.htm";
            } else
                htmlText = "33293-01.htm";
        }

        if (cond == 1) {
            htmlText = "33293-07.htm";
        } else if (cond == 2 || (st.getQuestItemsCount(_topShilensMark) >= 1)) {
            htmlText = "33293-08.htm";
            st.giveItems(_IstinaSoul, 1);
            st.setState(COMPLETED);
            st.playSound(SOUND_FINISH);
            st.exitCurrentQuest(false);
        }
        return htmlText;
    }

    @Override
    public String onKill(NpcInstance npc, QuestState st) {
        int cond = st.getCond();
        Party party = st.getPlayer().getParty();
        if (cond == 1) {
            if (npc.getNpcId() == _IstinaHard)
                if (party == null) {
                    st.setCond(2);
                    st.giveItems(_topShilensMark, 1);
                    st.playSound(SOUND_MIDDLE);
                } else {
                    for (Player pmember : party.getPartyMembers()) {
                        QuestState pst = pmember.getQuestState(_149_PrimalMotherIstina.class);
                        if (pst != null && pst.getCond() == 1) {
                            pst.setCond(2);
                            pst.giveItems(_topShilensMark, 1);
                            pst.playSound("SOUND_MIDDLE");
                        }
                    }

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
