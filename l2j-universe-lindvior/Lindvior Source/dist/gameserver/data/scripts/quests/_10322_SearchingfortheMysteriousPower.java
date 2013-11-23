/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package quests;

import l2p.gameserver.model.Player;
import l2p.gameserver.model.base.Race;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.model.quest.Quest;
import l2p.gameserver.model.quest.QuestState;
import l2p.gameserver.network.serverpackets.TutorialShowHtml;
import l2p.gameserver.network.serverpackets.components.ChatType;
import l2p.gameserver.network.serverpackets.components.NpcString;
import l2p.gameserver.scripts.Functions;
import l2p.gameserver.scripts.ScriptFile;
import l2p.gameserver.tables.SkillTable;
import l2p.gameserver.utils.Location;
import l2p.gameserver.utils.NpcUtils;

public class _10322_SearchingfortheMysteriousPower extends Quest implements ScriptFile {
    private static final int evan = 33464;
    private static final int shenon = 32974;
    private static final int helper = 32981;
    private static final int solder = 33014;
    private static final int[] SOLDER_START_POINT = {-111430, 255720, -1440};
    private static final int crow = 27457;
    private NpcInstance solderg = null;

    @Override
    public void onLoad() {
    }

    @Override
    public void onReload() {
    }

    @Override
    public void onShutdown() {
    }

    public _10322_SearchingfortheMysteriousPower() {
        super(false);
        addStartNpc(shenon);
        addTalkId(shenon);
        addTalkId(helper);
        addTalkId(evan);
        addKillId(crow);
        addKillId(solder);

        addLevelCheck(1, 20);
        addQuestCompletedCheck(_10321_QualificationsOfTheSeeker.class);
    }

    private void spawnsolder(QuestState st) {
        solderg = NpcUtils.spawnSingle(solder, Location.findPointToStay(SOLDER_START_POINT[0], SOLDER_START_POINT[1], SOLDER_START_POINT[2], 50, 100, st.getPlayer().getGeoIndex()));
        solderg.setFollowTarget(st.getPlayer());
        Functions.npcSay(solderg, NpcString.S1_COME_WITH_ME_I_WILL_LEAD_YOU_TO_IBANE, ChatType.NPC_SAY, 800, st.getPlayer().getName());
    }

    private void despawnsolder() {
        if (solderg != null)
            solderg.deleteMe();
    }

    @Override
    public String onEvent(String event, QuestState st, NpcInstance npc) {
        String htmltext = event;
        Player player = st.getPlayer();

        if (event.equalsIgnoreCase("quest_ac")) {
            st.setState(STARTED);
            st.setCond(1);
            spawnsolder(st);
            st.playSound(SOUND_ACCEPT);
            htmltext = "0-3.htm";
        }

        if (event.equalsIgnoreCase("bufs")) {
            SkillTable.getInstance().getInfo(4322, 1).getEffects(player, player, false, false);
            SkillTable.getInstance().getInfo(4323, 1).getEffects(player, player, false, false);
            SkillTable.getInstance().getInfo(5637, 1).getEffects(player, player, false, false);
            if (!player.isMageClass() || player.getTemplate().getRace() == Race.orc) {
                SkillTable.getInstance().getInfo(4324, 1).getEffects(player, player, false, false);
                SkillTable.getInstance().getInfo(4327, 1).getEffects(player, player, false, false);
                SkillTable.getInstance().getInfo(4325, 1).getEffects(player, player, false, false);
                SkillTable.getInstance().getInfo(4326, 1).getEffects(player, player, false, false);
            } else {
                SkillTable.getInstance().getInfo(4328, 1).getEffects(player, player, false, false);
                SkillTable.getInstance().getInfo(4329, 1).getEffects(player, player, false, false);
                SkillTable.getInstance().getInfo(4330, 1).getEffects(player, player, false, false);
                SkillTable.getInstance().getInfo(4331, 1).getEffects(player, player, false, false);
            }
            st.setCond(5);
            st.playSound(SOUND_MIDDLE);
            htmltext = "2-2.htm";
        }
        return htmltext;
    }

    @Override
    public String onTalk(NpcInstance npc, QuestState st) {
        int cond = st.getCond();
        int npcId = npc.getNpcId();
        String htmltext = "noquest";

        if (npcId == shenon) {
            if (st.isCompleted())
                htmltext = "0-c.htm";
            else if (cond == 0 && isAvailableFor(st.getPlayer()))
                htmltext = "start.htm";
            else if (cond >= 1)
                htmltext = "0-4.htm";
            else
                htmltext = "noqu.htm";
        } else if (npcId == evan) {
            if (st.isCompleted())
                htmltext = "1-c.htm";
            else if (cond == 0)
                htmltext = TODO_FIND_HTML;
            else if (cond == 1) {
                htmltext = "1-1.htm";
                despawnsolder();
                st.setCond(2);
                st.playSound(SOUND_MIDDLE);
            } else if (cond == 2)
                htmltext = "1-3.htm";
            else if (cond == 3) {
                htmltext = "1-2.htm";
                st.setCond(4);
                st.playSound(SOUND_MIDDLE);
            } else if (cond == 4)
                htmltext = "1-4.htm";
            else if (cond == 5)
                htmltext = "1-6.htm";
            else if (cond == 6) {
                htmltext = "1-5.htm";
                Functions.npcSayToPlayer(npc, st.getPlayer(), NpcString.WEAPONS_HAVE_BEEN_ADDED_TO_YOUR_INVENTORY, ChatType.NPC_SAY);
                st.getPlayer().addExpAndSp(300, 800);
                st.giveItems(57, 7000);
                st.giveItems(17, 500);
                st.giveItems(7816, 1);
                st.giveItems(7817, 1);
                st.giveItems(7818, 1);
                st.giveItems(7819, 1);
                st.giveItems(7820, 1);
                st.giveItems(7821, 1);
                st.giveItems(1060, 50);
                st.exitCurrentQuest(false);
                st.playSound(SOUND_FINISH);
            }

        } else if (npcId == helper)
            if (st.isCompleted() || cond == 0 || cond == 1 || cond == 2 || cond == 3 || cond == 6)
                htmltext = "2-nc.htm";
            else if (cond == 4) {
                htmltext = "2-1.htm";
                st.showTutorialHTML(TutorialShowHtml.QT_002, TutorialShowHtml.TYPE_WINDOW);
            } else if (cond == 5)
                htmltext = "2-3.htm";

        return htmltext;
    }

    @Override
    public String onKill(NpcInstance npc, QuestState st) {
        int npcId = npc.getNpcId();

        if (st.getCond() == 2 && npcId == crow) {
            st.playSound(SOUND_MIDDLE);
            st.setCond(3);
        } else if (st.getCond() == 5 && npcId == crow) {
            st.setCond(6);
            st.playSound(SOUND_MIDDLE);
        }

        return null;
    }
}