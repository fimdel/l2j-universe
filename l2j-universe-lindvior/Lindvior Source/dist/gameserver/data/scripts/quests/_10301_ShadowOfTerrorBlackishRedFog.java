/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package quests;

import l2p.gameserver.model.Skill;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.model.quest.Quest;
import l2p.gameserver.model.quest.QuestState;
import l2p.gameserver.network.serverpackets.ExShowScreenMessage;
import l2p.gameserver.network.serverpackets.components.NpcString;
import l2p.gameserver.scripts.ScriptFile;

public class _10301_ShadowOfTerrorBlackishRedFog extends Quest implements ScriptFile {
    private static final int LETTER_LADA_QUEST = 17819;
    private static final int CRISTALL = 17604;
    private static final int CAPTURED_WISP_ID = 17588;
    private static final int AGATHION_BRACELET_ID = 17380;
    private static final int LADA_ID = 33100;
    private static final int SLASKI_ID = 32893;
    private static final int FOREST = 33489;
    private static final int SUMMON_SPIRIT = 32938;
    private static final int CRISTALL_SOUND = 12011;

    @Override
    public void onLoad() {
    }

    @Override
    public void onReload() {
    }

    @Override
    public void onShutdown() {
    }

    public _10301_ShadowOfTerrorBlackishRedFog() {
        super(false);
        addStartNpc(LADA_ID);
        addTalkId(LADA_ID, SLASKI_ID);
        addSkillUseId(FOREST);
        addKillId(SUMMON_SPIRIT);
        addQuestItem(CRISTALL, CAPTURED_WISP_ID);
        addLevelCheck(90, 99);
    }

    @Override
    public String onSkillUse(NpcInstance npc, Skill skill, QuestState st) {
        st.getPlayer();
        int npcId = npc.getNpcId();
        if ((npcId == FOREST) && (skill.getId() == CRISTALL_SOUND)) {
            st.addSpawn(SUMMON_SPIRIT, npc.getX(), npc.getY(), npc.getZ(), npc.getHeading(), 100, 120000);
        }
        return null;
    }

    @Override
    public String onEvent(String event, QuestState st, NpcInstance npc) {
        if (event.equals("33100-03.htm")) {
            st.setState(STARTED);
            st.setCond(2);
            st.playSound(SOUND_ACCEPT);
            st.giveItems(CRISTALL, 10);
        } else if (event.equals("33100-05.htm")) {
            st.playSound(SOUND_MIDDLE);
            st.giveItems(CRISTALL, 5);
        } else if (event.equals("32893-02.htm")) {
            st.playSound(SOUND_MIDDLE);
            st.takeItems(CAPTURED_WISP_ID, -1);
        } else if (event.equals("32893-04.htm")) {
            st.addExpAndSp(26920620, 11389320);
            st.getPlayer().addAdena(1863420);
            st.giveItems(AGATHION_BRACELET_ID, 1);
            st.playSound(SOUND_FINISH);
            st.exitCurrentQuest(false);
        }
        return event;
    }

    @Override
    public String onTalk(NpcInstance npc, QuestState st) {
        String htmltext = "noquest";
        int id = st.getState();
        int cond = st.getCond();
        if (npc.getNpcId() == LADA_ID) {
            if (id == CREATED) {
                if (isAvailableFor(st.getPlayer())) {
                    htmltext = "33100-00.htm";
                } else {
                    htmltext = "33100-nolvl.htm";
                    st.exitCurrentQuest(true);
                }
            } else if (id == STARTED) {
                if (cond == 1) {
                    htmltext = "33100-00.htm";
                    st.takeItems(LETTER_LADA_QUEST, -1);
                } else if (cond == 2) {
                    htmltext = "33100-04.htm";
                } else {
                    if (cond == 3) {
                        htmltext = "33100-06.htm";
                    }
                }
            } else if (id == COMPLETED) {
                htmltext = "33100-07.htm";
            }
        } else if (npc.getNpcId() == SLASKI_ID) {
            if (id == STARTED) {
                if (cond == 3) {
                    htmltext = "32893-00.htm";
                }
            } else if (id == COMPLETED) {
                htmltext = "32893-05.htm";
            }
        }
        return htmltext;
    }

    @Override
    public String onKill(NpcInstance npc, QuestState st) {
        if ((npc.getNpcId() == SUMMON_SPIRIT)) ;// && (st.getCond() == 2))
        {
            st.getPlayer().sendPacket(new ExShowScreenMessage(NpcString.They_managed_to_do_everything_on_the_first_try, 4500, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, false));
            st.takeItems(CRISTALL, -1);
            st.giveItems(CAPTURED_WISP_ID, 1);
            st.setCond(3);
        }
        return null;
    }
}
