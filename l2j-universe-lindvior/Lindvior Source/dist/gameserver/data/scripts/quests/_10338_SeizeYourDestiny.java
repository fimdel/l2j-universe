/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package quests;

import instances.HarnakUndergroundRuins;
import instances.MemoryOfDisaster;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.SubClass;
import l2p.gameserver.model.entity.Reflection;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.model.quest.Quest;
import l2p.gameserver.model.quest.QuestState;
import l2p.gameserver.network.serverpackets.ExShowScreenMessage;
import l2p.gameserver.network.serverpackets.components.NpcString;
import l2p.gameserver.scripts.ScriptFile;
import l2p.gameserver.utils.ReflectionUtils;

/**
 * Created by IntelliJ IDEA.
 * User: Darvin
 * Date: 25.01.12
 * Time: 0:07
 * last update 05.01.2013
 */
public class _10338_SeizeYourDestiny extends Quest implements ScriptFile {
    private static final int CELLPHINE_ID = 33477;
    private static final int HADEL_ID = 33344;
    private static final int INSTANCE_ID = 195;
    private static final int MEMORY_OF_DISASTER_ID = 200;
    private static final int HARNAK_ID = 27445;
    private static final int HERMUNKUS_ID = 33340;
    private static final int SCROLL_OF_AFTERLIFE = 17600;

    public _10338_SeizeYourDestiny() {
        super(false);
        addStartNpc(CELLPHINE_ID);
        addTalkId(HADEL_ID);
        addTalkId(HERMUNKUS_ID);
        addFirstTalkId(HERMUNKUS_ID);
        addKillId(HARNAK_ID);

        addLevelCheck(85);
        addClassLevelCheck(3);
    }

    @Override
    public String onTalk(NpcInstance npc, QuestState qs) {
        if (qs == null)
            return null;

        Player player = qs.getPlayer();
        if (player == null)
            return null;

        String htmltext = "noquest";
        int cond = qs.getCond();
        switch (npc.getNpcId()) {
            case CELLPHINE_ID:
                switch (cond) {
                    case 0:
                        return "start.htm";
                    case 1:
                        return "0-3.htm";
                }
                break;
            case HADEL_ID:
                switch (cond) {
                    case 1:
                        return "1-1.htm";
                    case 2:
                    case 3:
                        return "1-5.htm";
                }
                break;
            case HERMUNKUS_ID:
                switch (cond) {
                    case 3:
                        return "2-2.htm";
                }
                break;
        }
        return htmltext;
    }

    @Override
    public String onEvent(String event, QuestState st, NpcInstance npc) {
        String htmltext = event;
        if (st == null)
            return htmltext;

        Player player = st.getPlayer();
        if (player == null)
            return htmltext;

        if (event.equalsIgnoreCase("MemoryOfDisaster")) {
            Reflection r = player.getActiveReflection();
            if (r != null) {
                if (player.canReenterInstance(MEMORY_OF_DISASTER_ID))
                    player.teleToLocation(r.getTeleportLoc(), r);
            } else if (player.canEnterInstance(MEMORY_OF_DISASTER_ID)) {

                ReflectionUtils.enterReflection(player, new MemoryOfDisaster(player), MEMORY_OF_DISASTER_ID);
            }
            return null;
        }

        switch (npc.getNpcId()) {
            case CELLPHINE_ID:
                if (event.equalsIgnoreCase("quest_ac")) {
                    st.setState(STARTED);
                    st.setCond(1);
                    st.playSound(SOUND_ACCEPT);
                    htmltext = "0-2.htm";
                }
                break;
            case HADEL_ID:
                if (event.equalsIgnoreCase("1-5.htm")) {
                    st.setCond(2);
                    st.playSound(SOUND_MIDDLE);
                } else if (event.equalsIgnoreCase("EnterInstance")) {
                    Reflection r = player.getActiveReflection();
                    if (r != null) {
                        if (player.canReenterInstance(INSTANCE_ID))
                            player.teleToLocation(r.getTeleportLoc(), r);
                    } else if (player.canEnterInstance(INSTANCE_ID)) {
                        if (st.getCond() < 3)
                            ReflectionUtils.enterReflection(player, new HarnakUndergroundRuins(1), INSTANCE_ID);
                        else
                            ReflectionUtils.enterReflection(player, new HarnakUndergroundRuins(2), INSTANCE_ID);
                    }
                    htmltext = null;
                }
                break;
            case HERMUNKUS_ID:
                if (event.equalsIgnoreCase("accept_scroll")) {
                    player.sendPacket(new ExShowScreenMessage(NpcString.YOU_MAY_USE_SCROLL_OF_AFTERLIFE_FROM_HERMUNCUS_TO_AWAKEN, 10000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true));
                    st.playSound(SOUND_FINISH);
                    st.giveItems(SCROLL_OF_AFTERLIFE, 1);
                    st.exitCurrentQuest(false);
                    checkDualConditions(st, player);
                    htmltext = "2-3.htm";
                }
                break;
        }
        return htmltext;
    }

    @Override
    public String onKill(NpcInstance npc, QuestState qs) {
        if (npc.getNpcId() == HARNAK_ID) {
            qs.setCond(3);
            qs.playSound(SOUND_MIDDLE);
        }
        return null;
    }

    @Override
    public String onFirstTalk(NpcInstance npc, Player player) {
        QuestState st = player.getQuestState(getClass());
        if (npc.getNpcId() == HERMUNKUS_ID) {
            if (npc.getNpcState() == 1)
                return null;
            else if (st.getCond() == 3)
                return "2-1.htm";
            else
                return "2-3.htm";
        }
        return null;
    }

    private static void checkDualConditions(QuestState st, Player player) {
        SubClass sub = player.getActiveSubClass();
        if (sub == null)
            player.setVar(st.getQuest().getName(), "1", -1);
        else if (!sub.isDouble())
            player.setVar(st.getQuest().getName(), "1", -1);
        else if (sub.isDouble())
            player.unsetVar(st.getQuest().getName());
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
