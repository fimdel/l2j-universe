/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package quests;

import instances.EvilIncubator;
import l2p.gameserver.cache.Msg;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.base.ProfessionRewards;
import l2p.gameserver.model.base.Race;
import l2p.gameserver.model.entity.Reflection;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.model.quest.Quest;
import l2p.gameserver.model.quest.QuestState;
import l2p.gameserver.scripts.ScriptFile;
import l2p.gameserver.utils.ReflectionUtils;
import org.apache.commons.lang3.ArrayUtils;

/**
 * @name 10345 - Day of Destiny Dwarfs Fate
 * @category Class quest. One
 */
public class _10345_DayOfDestinyDwarfsFate extends Quest implements ScriptFile {
    private static final int FERRIS = 30847;
    private static final int QUARTERMASTER = 33407;
    private static final int ADEN_VANGUARD_MEMBER = 33165;
    private static final int ADOLPH = 33170;
    private static final int ADOLPH_HELPERS1 = 33171;
    private static final int ADOLPH_HELPERS2 = 33172;
    private static final int ADOLPH_HELPERS3 = 33173;
    private static final int ADOLPH_HELPERS4 = 33174;
    private static final int[] ADEN_VANGUARD_CORPSE = {33166, 33167, 33168, 33169};

    private static final int VANGUARD_SOLDERS_DOG_TAGS = 17748;
    private static final int ADEN_HEROS_TREASURE_BOX_RED = 33771;
    private static final int ADEN_HEROS_TREASURE_BOX_BLUE = 33772;
    private static final int ADEN_HEROS_TREASURE_BOX_GREEN = 33773;
    private static final int SCROLL_OF_ESCAPE = 736;
    private static final int[] CRY_OF_FATE = {
            17484,
            17485,
            17486,
            17487,
            17488,
            17489,
            17490,
            17491,
            17492,
            17493,
            17494,
            17495,
            17496,
            17497,
            17498,
            17499,
            17500,
            17501,
            17502,
            17503,
            17504,
            17505,
            17506,
            17507,
            17508,
            17509,
            17510,
            17511,
            17512,
            17513,
            17514,
            17515,
            17516,
            17517};

    private static final int INSTANCE_ID = 185;

    @Override
    public void onLoad() {
    }

    @Override
    public void onReload() {
    }

    @Override
    public void onShutdown() {
    }

    public _10345_DayOfDestinyDwarfsFate() {
        super(false);
        addStartNpc(FERRIS);
        addTalkId(FERRIS, QUARTERMASTER, ADEN_VANGUARD_MEMBER, ADOLPH);
        addTalkId(ADEN_VANGUARD_CORPSE);
        addTalkId(ADOLPH_HELPERS1, ADOLPH_HELPERS2, ADOLPH_HELPERS3, ADOLPH_HELPERS4);

        addQuestItem(VANGUARD_SOLDERS_DOG_TAGS);
        addQuestItem(CRY_OF_FATE);

        addLevelCheck(76, 99);
        addClassLevelCheck(2);
    }

    @Override
    public String onEvent(String event, QuestState st, NpcInstance npc) {
        String htmltext = event;
        Player player = st.getPlayer();
        EvilIncubator instance = (EvilIncubator) st.getPlayer().getActiveReflection();

        if (event.equalsIgnoreCase("quest_accept")) {
            st.setState(STARTED);
            st.setCond(1);
            st.playSound(SOUND_ACCEPT);
            htmltext = "30847-07.htm";
        }
        if (event.equalsIgnoreCase("quest_next")) {
            st.setCond(2);
            st.playSound(SOUND_MIDDLE);
            htmltext = "33407-01.htm";
        }
        if (event.equalsIgnoreCase("quest_next1")) {
            st.setCond(4);
            st.playSound(SOUND_MIDDLE);
            st.takeAllItems(VANGUARD_SOLDERS_DOG_TAGS);
            htmltext = "33407-04.htm";
        }
        if (event.equalsIgnoreCase("corpse_search")) {
            if (st.get(String.valueOf(npc.getNpcId())) != null)
                htmltext = "corpse-02.htm";
            else {
                st.set(String.valueOf(npc.getNpcId()), 1);
                st.playSound(SOUND_ITEMGET);
                st.giveItems(VANGUARD_SOLDERS_DOG_TAGS, 1);
                htmltext = "corpse-01.htm";
                if (st.getQuestItemsCount(VANGUARD_SOLDERS_DOG_TAGS) == 4) {
                    st.setCond(3);
                    st.playSound(SOUND_MIDDLE);
                    htmltext = "corpse-03.htm";
                }
            }
        }
        if (event.equalsIgnoreCase("enter_instance")) {
            st.playSound(SOUND_MIDDLE);
            Reflection reflection = player.getActiveReflection();
            if (reflection != null) {
                if (player.canReenterInstance(INSTANCE_ID))
                    player.teleToLocation(reflection.getTeleportLoc(), reflection);
            } else if (player.canEnterInstance(INSTANCE_ID)) {
                ReflectionUtils.enterReflection(player, new EvilIncubator(st), INSTANCE_ID);
            }
            htmltext = "33165-01.htm";
        }
        if (event.equalsIgnoreCase("quest_select_helper")) {
            st.setCond(6);
            st.playSound(SOUND_MIDDLE);
            htmltext = "33170-01.htm";
        }
        if (event.equalsIgnoreCase("select_helper")) {
            if (st.get("10341Guard1") == null) {
                st.set("10341Guard1", npc.getNpcId());
                instance.setHelperId(npc.getNpcId());
                instance.deleteSelectedHelper(npc.getNpcId());
                htmltext = null;
            } else if (st.get("10341Guard2") == null) {
                st.setCond(7);
                st.playSound(SOUND_MIDDLE);
                instance.deleteSelectedHelper(npc.getNpcId());
                instance.setHelperId(npc.getNpcId());
                instance.deleteNotSelectedHelper();
                htmltext = null;
            }
        }
        if (event.equalsIgnoreCase("start_instance")) {
            st.setCond(8);
            st.playSound(SOUND_MIDDLE);
            instance.startFirstWave(player);
            htmltext = null;
        }
        if (event.equalsIgnoreCase("take_item")) {
            st.giveItems(ProfessionRewards.getThirdClassForId(st.getPlayer().getActiveClassId()) + 17396, 1);
            st.setCond(10);
            st.playSound(SOUND_MIDDLE);
            instance.sendInstanceState(2);
            htmltext = "33170-04.htm";
        }
        if (event.equalsIgnoreCase("start_second_part")) {
            st.setCond(11);
            st.playSound(SOUND_MIDDLE);
            instance.startSecondWave();
            htmltext = null;
        }
        if (event.equalsIgnoreCase("red")) {
            st.playSound(SOUND_FINISH);
            st.addExpAndSp(2050000, 0);
            st.takeAllItems(CRY_OF_FATE);
            st.giveItems(ADEN_HEROS_TREASURE_BOX_RED, 1);

            int newClass = ProfessionRewards.getThirdClassForId(player.getClassId().getId());
            player.setClassId(newClass, false);
            player.sendPacket(Msg.CONGRATULATIONS_YOU_HAVE_TRANSFERRED_TO_A_NEW_CLASS);

            player.broadcastCharInfo();
            st.exitCurrentQuest(false);
            return "30847-11.htm";
        }
        if (event.equalsIgnoreCase("blue")) {
            st.playSound(SOUND_FINISH);
            st.addExpAndSp(2050000, 0);
            st.takeAllItems(CRY_OF_FATE);
            st.giveItems(ADEN_HEROS_TREASURE_BOX_BLUE, 1);

            int newClass = ProfessionRewards.getThirdClassForId(player.getClassId().getId());
            player.setClassId(newClass, false);
            player.sendPacket(Msg.CONGRATULATIONS_YOU_HAVE_TRANSFERRED_TO_A_NEW_CLASS);

            player.broadcastCharInfo();
            st.exitCurrentQuest(false);
            return "30847-11.htm";
        }
        if (event.equalsIgnoreCase("green")) {
            st.playSound(SOUND_FINISH);
            st.addExpAndSp(2050000, 0);
            st.takeAllItems(CRY_OF_FATE);
            st.giveItems(ADEN_HEROS_TREASURE_BOX_GREEN, 1);

            int newClass = ProfessionRewards.getThirdClassForId(player.getClassId().getId());
            player.setClassId(newClass, false);
            player.sendPacket(Msg.CONGRATULATIONS_YOU_HAVE_TRANSFERRED_TO_A_NEW_CLASS);

            player.broadcastCharInfo();
            st.exitCurrentQuest(false);
            return "30847-11.htm";
        }
        return htmltext;
    }

    @Override
    public String onTalk(NpcInstance npc, QuestState st) {
        int cond = st.getCond();
        int npcId = npc.getNpcId();
        String htmltext = "noquest";
        Player player = st.getPlayer();
        Race race = player.getRace();

        if (npcId == FERRIS) {
            if (st.isCompleted())
                htmltext = "30847-03.htm";
            else if (cond == 0 && race != Race.dwarf)
                htmltext = "30847-02.htm";
            else if (!isAvailableFor(player))
                htmltext = "30847-10.htm";
            else if (cond == 0)
                htmltext = "30847-01.htm";
            else if (cond >= 1 && cond < 13)
                htmltext = "30847-08.htm";
            else if (cond == 13)
                htmltext = "30847-09.htm";
            else
                htmltext = "30847-08.htm";
        }
        if (npcId == QUARTERMASTER) {
            switch (cond) {
                case 1:
                    htmltext = "33407-00.htm";
                    break;
                case 2:
                    htmltext = "33407-02.htm";
                    break;
                case 3:
                    htmltext = "33407-03.htm";
                    break;
                case 4:
                    htmltext = "33407-04.htm";
                    break;
                default:
                    htmltext = "33407-05.htm";
            }
        }
        if (ArrayUtils.contains(ADEN_VANGUARD_CORPSE, npcId)) {
            if (cond == 2)
                htmltext = "corpse.htm";
            else if (cond == 3)
                htmltext = "corpse-03.htm";
            else
                htmltext = "";
        }
        if (npcId == ADEN_VANGUARD_MEMBER) {
            if (cond >= 4 && cond < 13)
                htmltext = "33165-00.htm";
        }
        if (npcId == ADOLPH) {
            switch (cond) {
                case 5:
                    htmltext = "33170-00.htm";
                    break;
                case 6:
                    htmltext = "33170-01.htm";
                    break;
                case 7:
                    htmltext = "33170-02.htm";
                    break;
                case 9:
                    htmltext = "33170-03.htm";
                    break;
                case 10:
                    htmltext = "33170-05.htm";
                    break;
                case 12:
                    st.setCond(13);
                    st.giveItems(SCROLL_OF_ESCAPE, 1);
                    st.playSound(SOUND_MIDDLE);
                    htmltext = "33170-06.htm";
                    break;
                default:
                    htmltext = "33170-battle.htm";
            }
        }
        if (npcId == ADOLPH_HELPERS1) {
            switch (cond) {
                case 5:
                    htmltext = "33171-no.htm";
                    break;
                case 6:
                    htmltext = "33171-00.htm";
                    break;
                case 7:
                case 9:
                case 10:
                case 12:
                    htmltext = "33171-no.htm";
                    break;
                default:
                    htmltext = "33171-battle.htm";
            }
        }
        if (npcId == ADOLPH_HELPERS2) {
            switch (cond) {
                case 5:
                    htmltext = "33172-no.htm";
                    break;
                case 6:
                    htmltext = "33172-00.htm";
                    break;
                case 7:
                case 9:
                case 10:
                case 12:
                    htmltext = "33172-no.htm";
                    break;
                default:
                    htmltext = "33172-battle.htm";
            }
        }
        if (npcId == ADOLPH_HELPERS3) {
            switch (cond) {
                case 5:
                    htmltext = "33173-no.htm";
                    break;
                case 6:
                    htmltext = "33173-00.htm";
                    break;
                case 7:
                case 9:
                case 10:
                case 12:
                    htmltext = "33173-no.htm";
                    break;
                default:
                    htmltext = "33173-battle.htm";
            }
        }
        if (npcId == ADOLPH_HELPERS4) {
            switch (cond) {
                case 5:
                    htmltext = "33174-no.htm";
                    break;
                case 6:
                    htmltext = "33174-00.htm";
                    break;
                case 7:
                case 9:
                case 10:
                case 12:
                    htmltext = "33174-no.htm";
                    break;
                default:
                    htmltext = "33174-battle.htm";
            }
        }
        return htmltext;
    }
}