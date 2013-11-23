/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package quests;

import l2p.commons.util.Rnd;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.model.quest.Quest;
import l2p.gameserver.model.quest.QuestState;
import l2p.gameserver.scripts.ScriptFile;

public class _353_PowerOfDarkness extends Quest implements ScriptFile {
    //NPCs
    private static int GALMAN = 31044;
    //Mobs
    private static int Malruk_Succubus = 20283;
    private static int Malruk_Succubus_Turen = 20284;
    private static int Malruk_Succubus2 = 20244;
    private static int Malruk_Succubus_Turen2 = 20245;
    //Items
    private static int STONE = 5862;
    //Chances
    private static int STONE_CHANCE = 50;

    public _353_PowerOfDarkness() {
        super(false);
        addStartNpc(GALMAN);
        addKillId(Malruk_Succubus);
        addKillId(Malruk_Succubus_Turen);
        addKillId(Malruk_Succubus2);
        addKillId(Malruk_Succubus_Turen2);
        addQuestItem(STONE);
    }

    @Override
    public String onEvent(String event, QuestState st, NpcInstance npc) {
        int _state = st.getState();
        if (event.equalsIgnoreCase("31044-04.htm") && _state == CREATED) {
            st.setState(STARTED);
            st.set("cond", "1");
            st.playSound(SOUND_ACCEPT);
        } else if (event.equalsIgnoreCase("31044-08.htm") && _state == STARTED) {
            st.playSound(SOUND_FINISH);
            st.exitCurrentQuest(true);
        }
        return event;
    }

    @Override
    public String onTalk(NpcInstance npc, QuestState st) {
        String htmltext = "noquest";
        if (npc.getNpcId() != GALMAN)
            return htmltext;

        if (st.getState() == CREATED) {
            if (st.getPlayer().getLevel() >= 55) {
                htmltext = "31044-02.htm";
                st.set("cond", "0");
            } else {
                htmltext = "31044-01.htm";
                st.exitCurrentQuest(true);
            }
        } else {
            long stone_count = st.getQuestItemsCount(STONE);
            if (stone_count > 0) {
                htmltext = "31044-06.htm";
                st.takeItems(STONE, -1);
                st.giveItems(ADENA_ID, 2500 + 230 * stone_count);
                st.playSound(SOUND_MIDDLE);
            } else
                htmltext = "31044-05.htm";
        }
        return htmltext;
    }

    @Override
    public String onKill(NpcInstance npc, QuestState qs) {
        if (qs.getState() != STARTED)
            return null;

        if (Rnd.chance(STONE_CHANCE)) {
            qs.giveItems(STONE, 1);
            qs.playSound(SOUND_ITEMGET);
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