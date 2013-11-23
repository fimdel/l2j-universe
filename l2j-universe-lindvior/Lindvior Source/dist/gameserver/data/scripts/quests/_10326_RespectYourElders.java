/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package quests;

import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.model.quest.Quest;
import l2p.gameserver.model.quest.QuestState;
import l2p.gameserver.network.serverpackets.components.ChatType;
import l2p.gameserver.network.serverpackets.components.NpcString;
import l2p.gameserver.scripts.Functions;
import l2p.gameserver.scripts.ScriptFile;
import l2p.gameserver.utils.Location;
import l2p.gameserver.utils.NpcUtils;

public class _10326_RespectYourElders extends Quest implements ScriptFile {
    private static final int panteleon = 32972;
    private static final int galint = 32980;
    private static final int handemonkey = 32971;
    private NpcInstance handemonkeyg = null;

    private static final int[] SOLDER_START_POINT = {-116696, 255560, -1453};

    @Override
    public void onLoad() {
    }

    @Override
    public void onReload() {
    }

    @Override
    public void onShutdown() {
    }

    public _10326_RespectYourElders() {
        super(false);
        addStartNpc(galint);
        addTalkId(panteleon);

        addLevelCheck(1, 20);
        addQuestCompletedCheck(_10325_SearchingForNewPower.class);
    }

    private void spawnsolder(QuestState st) {
        handemonkeyg = NpcUtils.spawnSingle(handemonkey, Location.findPointToStay(SOLDER_START_POINT[0], SOLDER_START_POINT[1], SOLDER_START_POINT[2], 50, 100, st.getPlayer().getGeoIndex()));
        handemonkeyg.setFollowTarget(st.getPlayer());
        Functions.npcSay(handemonkeyg, NpcString.COME_ON_CREEK, ChatType.NPC_SAY, 800, st.getPlayer().getName());
    }

    private void despawnsolder() {
        if (handemonkeyg != null)
            handemonkeyg.deleteMe();
    }

    @Override
    public String onEvent(String event, QuestState st, NpcInstance npc) {
        String htmltext = event;

        if (event.equalsIgnoreCase("quest_ac")) {
            st.setState(STARTED);
            st.setCond(1);
            st.playSound(SOUND_ACCEPT);
            spawnsolder(st);
            htmltext = "0-3.htm";
        }

        if (event.equalsIgnoreCase("qet_rev")) {
            htmltext = "1-2.htm";
            st.getPlayer().addExpAndSp(5300, 2800);
            st.giveItems(57, 14000);
            st.exitCurrentQuest(false);
            st.playSound(SOUND_FINISH);
        }
        return htmltext;
    }

    @Override
    public String onTalk(NpcInstance npc, QuestState st) {
        int cond = st.getCond();
        int npcId = npc.getNpcId();
        String htmltext = "noquest";

        if (npcId == galint) {
            if (st.isCompleted())
                htmltext = "0-nc.htm";
            else if (cond == 0 && isAvailableFor(st.getPlayer()))
                htmltext = "start.htm";
            else if (cond == 1)
                htmltext = "0-4.htm";
            else
                htmltext = "0-nc.htm";
        } else if (npcId == panteleon)
            if (st.isCompleted())
                htmltext = "1-c.htm";
            else if (cond == 0)
                htmltext = TODO_FIND_HTML;
            else if (cond == 1) {
                despawnsolder();
                htmltext = "1-1.htm";
            }
        return htmltext;
    }
}
