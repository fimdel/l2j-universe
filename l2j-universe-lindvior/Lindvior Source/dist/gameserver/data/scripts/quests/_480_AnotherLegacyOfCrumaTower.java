/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package quests;


import l2p.gameserver.instancemanager.ReflectionManager;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.model.quest.Quest;
import l2p.gameserver.model.quest.QuestState;
import l2p.gameserver.scripts.ScriptFile;
import l2p.gameserver.utils.Location;
import l2p.gameserver.utils.ReflectionUtils;
import services.SupportMagic;

public class _480_AnotherLegacyOfCrumaTower extends Quest implements ScriptFile {
    //npc
    public static final int LILEJ = 33155;
    public static final int LINKENS = 33163;
    public static final int STRANGE_MECHANIC_CR = 33158;

    public static final int MARTES_NPC = 33292; //martes NPC
    public static final int MARTES_RB = 25829; //martes RB

    //items
    public static final int TRESURE_TOOL = 17619;
    public static final int MARTES_CORE = 17728;

    @Override
    public void onLoad() {
    }

    @Override
    public void onReload() {
    }

    @Override
    public void onShutdown() {
    }

    public _480_AnotherLegacyOfCrumaTower() {
        super(true);
        addStartNpc(LILEJ);
        addTalkId(LINKENS);
        addTalkId(MARTES_NPC);
        addKillId(MARTES_RB);
        addQuestItem(TRESURE_TOOL);
        addQuestItem(MARTES_CORE);

        addLevelCheck(38);
        addQuestCompletedCheck(_10352_LegacyOfCrumaTower.class);
    }

    @Override
    public String onEvent(String event, QuestState st, NpcInstance npc) {
        Player player = st.getPlayer();
        String htmltext = event;
        if (event.equalsIgnoreCase("33155-9.htm")) {
            SupportMagic.doSupportMagic(npc, player, false);
        }

        if (event.equalsIgnoreCase("33155-10.htm")) {
            SupportMagic.doSupportMagic(npc, player, true);
        }

        if (event.equalsIgnoreCase("advanceCond3")) {
            if (st.getCond() != 3)
                st.setCond(3);
            return null;
        }

        if (event.equalsIgnoreCase("teleportCruma")) {
            st.setCond(1);
            st.setState(STARTED);
            st.playSound(SOUND_ACCEPT);
            player.teleToLocation(17192, 114173, -3439);
            return null;
        }
        if (event.equalsIgnoreCase("33163-8.htm")) {
            if (st.getQuestItemsCount(TRESURE_TOOL) == 0) {
                st.giveItems(TRESURE_TOOL, 30);
                st.setCond(2);
            } else
                return "33163-12.htm";
        }

        if (event.equalsIgnoreCase("EnterInstance")) {
            if (player.getParty() == null) {
                player.sendMessage("You cannot enter without party!"); //pts message?
                return null;
            } else {
                for (Player member : player.getParty().getPartyMembers()) {
                    QuestState qs = member.getQuestState(_480_AnotherLegacyOfCrumaTower.class);
                    if (qs == null || qs.getCond() != 3) {
                    } //nothing as I've seen everybody can enter this instance
                    else if (qs.getCond() == 3)
                        qs.setCond(4);
                }
                ReflectionUtils.enterReflection(player, 198);
                return null;
            }
        }
        if (event.equalsIgnoreCase("LeaveInstance")) {
            player.teleToLocation(17192, 114173, -3439, ReflectionManager.DEFAULT);
            return null;
        }

        return event;
    }

    @Override
    public String onTalk(NpcInstance npc, QuestState st) {
        Player player = st.getPlayer();
        int npcId = npc.getNpcId();
        int state = st.getState();
        int cond = st.getCond();
        if (state == 1) {
            if (player.getLevel() < 38)
                return "33155-lvl.htm";
            if (!st.isNowAvailable())
                return "33155-comp.htm";
        }
        if (npcId == LILEJ) {
            if (cond < 5) {
                return "33155.htm";
            }
        }
        if (npcId == LINKENS) {
            if (cond == 1)
                return "33163.htm";
            if (cond == 2)
                return "33163-5.htm";
            if (cond == 5) {
                if (st.getQuestItemsCount(MARTES_CORE) == 0)
                    return "33163-14.htm";
                else if (st.getQuestItemsCount(MARTES_CORE) != 0) {
                    st.takeItems(MARTES_CORE, -1);
                    st.takeItems(TRESURE_TOOL, -1);
                    st.addExpAndSp(240000, 156000);
                    st.unset("cond");
                    st.playSound(SOUND_FINISH);
                    st.exitCurrentQuest(this);
                    return "33163-15.htm";
                }

            }
        }
        if (npcId == MARTES_NPC) {
            if (cond == 3)
                return "33292.htm";
            if (cond == 5)
                return "33292-1.htm";
        }
        return "noquest";
    }

    @Override
    public String onKill(NpcInstance npc, QuestState st) {
        Player player = st.getPlayer();

        if (player.getParty() == null) {
            st.setCond(5);
        } else {
            for (Player member : player.getParty().getPartyMembers()) {
                QuestState qs = member.getQuestState(_480_AnotherLegacyOfCrumaTower.class);
                if (qs == null || qs.getCond() != 4)
                    continue;
                qs.setCond(5);
            }
        }
        st.getPlayer().getReflection().addSpawnWithoutRespawn(MARTES_NPC, Location.findPointToStay(st.getPlayer(), 50, 100), st.getPlayer().getGeoIndex());
        return null;
    }
}