/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package quests;

import l2p.commons.util.Rnd;
import l2p.gameserver.ai.CtrlIntention;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.Skill;
import l2p.gameserver.model.entity.Reflection;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.model.quest.Quest;
import l2p.gameserver.model.quest.QuestState;
import l2p.gameserver.network.serverpackets.ExShowScreenMessage;
import l2p.gameserver.network.serverpackets.ExShowScreenMessage.ScreenMessageAlign;
import l2p.gameserver.network.serverpackets.components.ChatType;
import l2p.gameserver.network.serverpackets.components.NpcString;
import l2p.gameserver.scripts.Functions;
import l2p.gameserver.scripts.ScriptFile;
import l2p.gameserver.utils.Location;
import l2p.gameserver.utils.NpcUtils;
import l2p.gameserver.utils.ReflectionUtils;

import java.util.List;

public class _10327_BookOfGiants extends Quest implements ScriptFile {
    private static final int panteleon = 32972;
    private static final int table = 33126;
    private static final int assassin = 23121;
    private static final int tairen = 33004;
    private static final int book = 17575;
    private NpcInstance Tairen = null;
    private NpcInstance Assassin1 = null;
    private NpcInstance Assassin2 = null;
    private int killedassasin = 0;
    private static final int INSTANCE_ID = 182;
    private int bookDeskObjectId = 0;
    private boolean bookTaken = false;

    @Override
    public void onLoad() {
    }

    @Override
    public void onReload() {
    }

    @Override
    public void onShutdown() {
    }

    public _10327_BookOfGiants() {
        super(false);
        addStartNpc(panteleon);
        addTalkId(panteleon);
        addFirstTalkId(table);
        addQuestItem(book);
        addSkillUseId(assassin);
        addFirstTalkId(tairen);
        addKillId(assassin);
        addAttackId(assassin);

        addLevelCheck(1, 20);
        addQuestCompletedCheck(_10326_RespectYourElders.class);
    }

    @Override
    public String onSkillUse(NpcInstance npc, Skill skill, QuestState st) {

        Functions.npcSayToPlayer(Tairen, st.getPlayer(), NpcString.ENOUGH_OF_THIS_COME_AT_ME, ChatType.NPC_SAY);
        if (npc.getNpcId() == assassin) {
            if (Tairen != null) {
                Tairen.setRunning();
                Tairen.getAggroList().addDamageHate(npc, 0, 10000);
                Tairen.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK);
                if (npc != null & npc == Assassin1)
                    Assassin1.deleteMe();
                if (npc != null & npc == Assassin2)
                    Assassin2.deleteMe();
                ++killedassasin;
                if (killedassasin >= 2) {
                    st.setCond(3);
                    st.cancelQuestTimer("attak");
                    st.playSound(SOUND_MIDDLE);
                    killedassasin = 0;
                }
            }
        }
        return null;
    }

    private void enterInstance(Player player) {
        Reflection reflection = player.getActiveReflection();
        if (reflection != null) {
            if (player.canReenterInstance(INSTANCE_ID))
                player.teleToLocation(reflection.getTeleportLoc(), reflection);
        } else if (player.canEnterInstance(INSTANCE_ID))
            ReflectionUtils.enterReflection(player, INSTANCE_ID);

        List<NpcInstance> desks = player.getActiveReflection().getAllByNpcId(table, true);
        double seed = Math.random();
        int counter = 0;

        for (NpcInstance desk : desks) {
            if ((seed <= 0.25 && counter == 0) ||
                    (seed > 0.25 && seed <= 0.5 && counter == 1) ||
                    (seed > 0.5 && seed <= 0.75 && counter == 2) ||
                    (seed > 0.75 && counter == 3))
                bookDeskObjectId = desk.getObjectId();
            ++counter;
        }

        if (bookDeskObjectId == 0 && desks.size() > 0)
            bookDeskObjectId = desks.get(0).getObjectId();
    }

    @Override
    public String onEvent(String event, QuestState st, NpcInstance npc) {
        String htmltext = event;
        Player player = st.getPlayer();

        if (event.equalsIgnoreCase("quest_ac")) {
            st.setState(STARTED);
            st.setCond(1);
            st.playSound(SOUND_ACCEPT);
            htmltext = "0-3.htm";
        }
        if (event.equalsIgnoreCase("enter_instance")) {
            enterInstance(st.getPlayer());
            st.playSound(SOUND_MIDDLE);
            bookTaken = false;
            Tairen = st.getPlayer().getActiveReflection().getAllByNpcId(tairen, true).get(0);
            if (Tairen != null)
                Tairen.setRunning();
            return null;
        }
        if (event.equalsIgnoreCase("qet_rev")) {
            player.sendPacket(new ExShowScreenMessage(NpcString.ACCESSORIES_HAVE_BEEN_ADDED_TO_YOUR_INVENTORY, 4500, ScreenMessageAlign.TOP_CENTER));
            htmltext = "0-5.htm";
            st.getPlayer().addExpAndSp(7800, 3500);
            st.giveItems(57, 16000);
            st.giveItems(112, 2);
            st.exitCurrentQuest(false);
            st.playSound(SOUND_FINISH);
        }

        if (event.equalsIgnoreCase("attak")) {
            htmltext = "";
            st.startQuestTimer("attak", 5000);
            if (Tairen != null) {
                Tairen.moveToLocation(st.getPlayer().getLoc(), Rnd.get(0, 100), true);
                if (Rnd.chance(33))
                    Functions.npcSayToPlayer(Tairen, st.getPlayer(), NpcString.LOOKS_LIKE_ONLY_SKILL_BASED_ATTACKS_DAMAGE_THEM, ChatType.NPC_SAY);
                if (Rnd.chance(33))
                    Functions.npcSayToPlayer(Tairen, st.getPlayer(), NpcString.YOUR_NORMAL_ATTACKS_ARENT_WORKING, ChatType.NPC_SAY);
                if (Rnd.chance(33))
                    Functions.npcSayToPlayer(Tairen, st.getPlayer(), NpcString.USE_YOUR_SKILL_ATTACKS_AGAINST_THEM, ChatType.NPC_SAY);
            }
        }

        if (event.equalsIgnoreCase("spawnas")) {
            htmltext = "";

            Assassin1 = NpcUtils.spawnSingle(assassin, new Location(-114815, 244966, -7976, 0), player.getActiveReflection());
            Assassin2 = NpcUtils.spawnSingle(assassin, new Location(-114554, 244954, -7976, 0), player.getActiveReflection());

            Functions.npcSayToPlayer(Assassin1, st.getPlayer(), NpcString.FINALLY_I_THOUGHT_I_WAS_GOING_TO_DIE_WAITING, ChatType.NPC_SAY);

            Assassin1.getAggroList().addDamageHate(st.getPlayer(), 0, 10000);
            Assassin1.setAggressionTarget(player);
            Assassin2.getAggroList().addDamageHate(st.getPlayer(), 0, 10000);
            Assassin2.setAggressionTarget(player);
        }
        return htmltext;
    }

    @Override
    public String onTalk(NpcInstance npc, QuestState st) {
        int cond = st.getCond();
        int npcId = npc.getNpcId();
        String htmltext = "noquest";

        if (npcId == panteleon) {
            if (st.isCompleted())
                htmltext = "0-c.htm";
            else if (cond == 0 && isAvailableFor(st.getPlayer()))
                htmltext = "start.htm";
            else if (cond == 1)
                htmltext = "0-3.htm";
            else if (cond == 3 && st.getQuestItemsCount(book) >= 1)
                htmltext = "0-4.htm";
            else if (cond == 2) {
                htmltext = "0-3.htm";
                st.setCond(1);
                st.takeAllItems(book);
            } else
                htmltext = "0-nc.htm";
        }
        return htmltext;
    }

    @Override
    public String onFirstTalk(NpcInstance npc, Player player) {
        String htmltext = "noquest";
        QuestState st = player.getQuestState(getClass());

        int npcId = npc.getNpcId();

        if (npcId == table) {
            if (st == null)
                return htmltext;
            else if (npc.getObjectId() == bookDeskObjectId && !bookTaken) {
                bookTaken = true;
                player.sendPacket(new ExShowScreenMessage(NpcString.WATCH_OUT_YOU_ARE_BEING_ATTACKED, 4500, ScreenMessageAlign.TOP_CENTER));
                htmltext = "2-2.htm";
                st.takeAllItems(book);
                st.giveItems(book, 1, false);
                st.setCond(2);
                st.startQuestTimer("attak", 5000);
                st.startQuestTimer("spawnas", 50);
            } else
                htmltext = "2-1.htm";
        }
        if (npcId == tairen) {
            if (st == null || st.getCond() == 0)
                return "";
            if (st.getCond() == 1)
                htmltext = "3-1.htm";
            else if (st.getCond() == 2)
                htmltext = "3-2.htm";
            else if (st.getCond() == 3)
                htmltext = "3-3.htm";
        }
        return htmltext;
    }
}