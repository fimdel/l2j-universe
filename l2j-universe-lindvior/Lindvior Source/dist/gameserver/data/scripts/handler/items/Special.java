/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package handler.items;

import bosses.AntharasManager;
import bosses.ValakasManager;
import l2p.commons.util.Rnd;
import l2p.gameserver.cache.Msg;
import l2p.gameserver.instancemanager.QuestManager;
import l2p.gameserver.instancemanager.ReflectionManager;
import l2p.gameserver.model.GameObject;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.Zone.ZoneType;
import l2p.gameserver.model.instances.DoorInstance;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.model.items.ItemInstance;
import l2p.gameserver.model.quest.Quest;
import l2p.gameserver.model.quest.QuestState;
import l2p.gameserver.network.serverpackets.NpcHtmlMessage;
import l2p.gameserver.network.serverpackets.SystemMessage;
import l2p.gameserver.network.serverpackets.TutorialShowHtml;
import l2p.gameserver.network.serverpackets.components.CustomMessage;
import l2p.gameserver.network.serverpackets.components.SystemMsg;
import l2p.gameserver.scripts.Functions;
import l2p.gameserver.tables.SkillTable;
import l2p.gameserver.utils.Location;
import org.apache.commons.lang3.ArrayUtils;
import quests.*;

public class Special extends SimpleItemHandler {
    private static final int[] ITEM_IDS = new int[]{
            8060,
            8556,
            13853,
            13808,
            13809,
            14835,
            15537,
            10632,
            21899,
            21900,
            21901,
            21902,
            21903,
            21904,
            17268,
            35702,
            21106, 21107,
            34033};


    @Override
    public int[] getItemIds() {
        return ITEM_IDS;
    }

    @Override
    protected boolean useItemImpl(Player player, ItemInstance item, boolean ctrl) {
        int itemId = item.getItemId();

        switch (itemId) {
            //Key of Enigma
            case 8060:
                return use8060(player, ctrl);
            //Dewdrop of Destruction
            case 8556:
                return use8556(player, ctrl);
            //DestroyedDarknessFragmentPowder -> DestroyedLightFragmentPowder
            case 13853:
                return use13853(player, ctrl);
            //Holy Water for SSQ 2nd quest
            case 13808:
                return use13808(player, ctrl);
            //Court Mag Staff for SSQ 2nd quest
            case 13809:
                return use13809(player, ctrl);
            case 14835:
                return use14835(player, ctrl);
            //Strongbox of Promise
            case 15537:
                return use15537(player, ctrl);
            //Wondrous Cubic
            case 10632:
                return use10632(player, ctrl);
            case 21899:
                return use21899(player, ctrl);
            case 21900:
                return use21900(player, ctrl);
            case 21901:
                return use21901(player, ctrl);
            case 21902:
                return use21902(player, ctrl);
            case 21903:
                return use21903(player, ctrl);
            case 21904:
                return use21904(player, ctrl);
            //Antharas Blood Crystal
            case 17268:
                return use17268(player, ctrl);
            case 17619: //cruma quest
                return use17619(player, ctrl);
            case 17604: //megameld quest
                return use17604(player, ctrl);
            case 21106:
                return use21106(player, ctrl);
            case 21107:
                return use21107(player, ctrl);
            case 34033:
                return use34033(player, ctrl);
            //Top Quality Petra
            case 35702:
                return use35702(player, ctrl);
            default:
                return false;
        }
    }

    //Key of Enigma
    private boolean use8060(Player player, boolean ctrl) {
        if (Functions.removeItem(player, 8058, 1) == 1) {
            Functions.addItem(player, 8059, 1);
            return true;
        }
        return false;
    }

    private boolean use21106(Player player, boolean ctrl) {
        int chance = Rnd.get(1000);

        if (chance < 350)
            Functions.addItem(player, 10633, 1);
        else if (chance < 200)
            Functions.addItem(player, 10634, 1);
        else if (chance < 100)
            Functions.addItem(player, 10635, 1);
        else if (chance < 80)
            Functions.addItem(player, 10636, 1);
        else if (chance < 20)
            Functions.addItem(player, 10637, 1);
        else if (chance < 140)
            Functions.addItem(player, 10642, 1);
        else if (chance < 70)
            Functions.addItem(player, 10643, 1);
        else if (chance < 25)
            Functions.addItem(player, 10644, 1);
        else if (chance < 10)
            Functions.addItem(player, 10645, 1);
        else if (chance <= 5) {
            Functions.addItem(player, 10646, 1);
        }
        Functions.removeItem(player, 21106, 1);
        return true;
    }

    private boolean use21107(Player player, boolean ctrl) {
        int chance = Rnd.get(1000);

        if (chance < 550)
            Functions.addItem(player, 8728, 1);
        else if (chance < 470)
            Functions.addItem(player, 8729, 1);
        else if (chance < 420)
            Functions.addItem(player, 8730, 1);
        else if (chance < 400)
            Functions.addItem(player, 8731, 1);
        else if (chance < 300)
            Functions.addItem(player, 8732, 1);
        else if (chance < 140)
            Functions.addItem(player, 9573, 1);
        else if (chance < 70)
            Functions.addItem(player, 10483, 1);
        else if (chance < 25) {
            Functions.addItem(player, 14166, 1);
        } else if (chance < 550)
            Functions.addItem(player, 8738, 1);
        else if (chance <= 470)
            Functions.addItem(player, 8739, 1);
        else if (chance <= 420)
            Functions.addItem(player, 8740, 1);
        else if (chance <= 400)
            Functions.addItem(player, 8741, 1);
        else if (chance <= 300)
            Functions.addItem(player, 8742, 1);
        else if (chance <= 140)
            Functions.addItem(player, 9574, 1);
        else if (chance <= 70)
            Functions.addItem(player, 10484, 1);
        else if (chance <= 25) {
            Functions.addItem(player, 14167, 1);
        } else if (chance <= 550)
            Functions.addItem(player, 8748, 1);
        else if (chance <= 470)
            Functions.addItem(player, 8749, 1);
        else if (chance <= 420)
            Functions.addItem(player, 8750, 1);
        else if (chance <= 400)
            Functions.addItem(player, 8751, 1);
        else if (chance <= 300)
            Functions.addItem(player, 8752, 1);
        else if (chance <= 140)
            Functions.addItem(player, 9575, 1);
        else if (chance <= 70)
            Functions.addItem(player, 10485, 1);
        else if (chance <= 25) {
            Functions.addItem(player, 14168, 1);
        } else if (chance <= 550)
            Functions.addItem(player, 8758, 1);
        else if (chance <= 470)
            Functions.addItem(player, 8759, 1);
        else if (chance <= 420)
            Functions.addItem(player, 8760, 1);
        else if (chance <= 400)
            Functions.addItem(player, 8761, 1);
        else if (chance <= 300)
            Functions.addItem(player, 8762, 1);
        else if (chance <= 140)
            Functions.addItem(player, 9576, 1);
        else if (chance <= 70)
            Functions.addItem(player, 10486, 1);
        else if (chance <= 25) {
            Functions.addItem(player, 14169, 1);
        }
        Functions.removeItem(player, 21107, 1);
        return true;
    }

    //Dewdrop of Destruction
    private boolean use8556(Player player, boolean ctrl) {
        int[] npcs = {29048, 29049};

        GameObject t = player.getTarget();
        if (t == null || !t.isNpc() || !ArrayUtils.contains(npcs, ((NpcInstance) t).getNpcId())) {
            player.sendPacket(new SystemMessage(SystemMessage.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addItemName(8556));
            return false;
        }
        if (player.getDistance(t) > 200) {
            player.sendPacket(new SystemMessage(SystemMessage.YOUR_TARGET_IS_OUT_OF_RANGE));
            return false;
        }

        useItem(player, 8556, 1);
        ((NpcInstance) t).doDie(player);
        return true;
    }

    //DestroyedDarknessFragmentPowder -> DestroyedLightFragmentPowde
    private boolean use13853(Player player, boolean ctrl) {
        if (!player.isInZone(ZoneType.mother_tree)) {
            player.sendPacket(Msg.THERE_WAS_NOTHING_FOUND_INSIDE_OF_THAT);
            return false;
        }
        useItem(player, 13853, 1);
        Functions.addItem(player, 13854, 1);
        return true;
    }

    //Holy Water for SSQ 2nd quest
    private boolean use13808(Player player, boolean ctrl) {
        int[] allowedDoors = {17240101, 17240105, 17240109};

        GameObject target = player.getTarget();
        if (player.getDistance(target) > 150)
            return false;
        if (target != null && target.isDoor()) {
            int _door = ((DoorInstance) target).getDoorId();
            if (ArrayUtils.contains(allowedDoors, _door))
                player.getReflection().openDoor(_door);
            else {
                player.sendPacket(SystemMsg.THAT_IS_AN_INCORRECT_TARGET);
                return false;
            }
        } else {
            player.sendPacket(Msg.INVALID_TARGET);
            return false;
        }
        return true;
    }

    //Court Mag Staff for SSQ 2nd quest
    private boolean use13809(Player player, boolean ctrl) {
        int[] allowedDoors = {17240103, 17240107};

        GameObject target = player.getTarget();
        if (target != null && target.isDoor()) {
            int _door = ((DoorInstance) target).getDoorId();
            if (ArrayUtils.contains(allowedDoors, _door)) {
                useItem(player, 13809, 1);
                player.getReflection().openDoor(_door);
                return false;
            } else {
                player.sendPacket(SystemMsg.THAT_IS_AN_INCORRECT_TARGET);
                return false;
            }
        } else {
            player.sendPacket(Msg.INVALID_TARGET);
            return false;
        }
    }

    private boolean use14835(Player player, boolean ctrl) {
        player.checkAllowAction_CB();
        if (player.isActionsDisabled() || player.isInOlympiadMode() || player.isInZone(ZoneType.no_escape)) {
            player.sendPacket(new SystemMessage(SystemMessage.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addItemName(14835));
            return false;
        }

        useItem(player, 14835, 1);
        //Stakato nest entrance
        player.teleToLocation(89464, -44712, -2167, ReflectionManager.DEFAULT);
        return true;
    }

    //Strongbox of Promise
    private boolean use15537(Player player, boolean ctrl) {
        QuestState qs = player.getQuestState(_464_Oath.class);
        if (player.getLevel() >= 82 && qs == null) {
            useItem(player, 15537, 1);
            Functions.addItem(player, 15538, 1);
            Quest q = QuestManager.getQuest(464);
            QuestState st = player.getQuestState(q.getClass());
            st = q.newQuestState(player, Quest.CREATED);
            st.setState(Quest.STARTED);
            st.setCond(1);
        } else {
            player.sendMessage(new CustomMessage("Quest._464_Oath.QuestCannotBeTaken", player));
            return false;
        }
        return true;
    }

    //Wondrous Cubic
    private boolean use10632(Player player, boolean ctrl) {
        int chance = Rnd.get(1000000);

        if (chance < 350000) // Rough Blue Cubic Piece            35%
            Functions.addItem(player, 10633, 1);
        else if (chance < 550000) // Rough Yellow Cubic Piece     20%
            Functions.addItem(player, 10634, 1);
        else if (chance < 650000) // Rough Green Cubic Piece      10%
            Functions.addItem(player, 10635, 1);
        else if (chance < 730000) // Rough Red Cubic Piece        8%
            Functions.addItem(player, 10636, 1);
        else if (chance < 750000) // Rough White Cubic Piece      2%
            Functions.addItem(player, 10637, 1);

        else if (chance < 890000) // Fine Blue Cubic Piece        14%
            Functions.addItem(player, 10642, 1);
        else if (chance < 960000) // Fine Yellow Cubic Piece      7%
            Functions.addItem(player, 10643, 1);
        else if (chance < 985000) // Fine Green Cubic Piece       2.5%
            Functions.addItem(player, 10644, 1);
        else if (chance < 995000) // Fine Red Cubic Piece         1%
            Functions.addItem(player, 10645, 1);
        else if (chance <= 1000000) // Fine White Cubic Piece     0.5%
            Functions.addItem(player, 10646, 1);

        return true;
    }

    //Totem
    private boolean use21899(Player player, boolean ctrl) {
        if (!player.isInZone(AntharasManager.getZone()) && !player.isInZone(ValakasManager.getZone())) {
            player.sendPacket(new SystemMessage(SystemMessage.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addItemName(21899));
            return false;
        }
        Functions.spawn(Location.findPointToStay(player.getLoc(), 50, 100, player.getGeoIndex()), 143);
        return true;
    }

    //Totem
    private boolean use21900(Player player, boolean ctrl) {
        if (!player.isInZone(AntharasManager.getZone()) && !player.isInZone(ValakasManager.getZone())) {
            player.sendPacket(new SystemMessage(SystemMessage.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addItemName(21900));
            return false;
        }
        Functions.spawn(Location.findPointToStay(player.getLoc(), 50, 100, player.getGeoIndex()), 144);
        return true;
    }

    //Totem
    private boolean use21901(Player player, boolean ctrl) {
        if (!player.isInZone(AntharasManager.getZone()) && !player.isInZone(ValakasManager.getZone())) {
            player.sendPacket(new SystemMessage(SystemMessage.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addItemName(21901));
            return false;
        }
        Functions.spawn(Location.findPointToStay(player.getLoc(), 50, 100, player.getGeoIndex()), 145);
        return true;
    }

    //Totem
    private boolean use21902(Player player, boolean ctrl) {
        if (!player.isInZone(AntharasManager.getZone()) && !player.isInZone(ValakasManager.getZone())) {
            player.sendPacket(new SystemMessage(SystemMessage.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addItemName(21902));
            return false;
        }
        Functions.spawn(Location.findPointToStay(player.getLoc(), 50, 100, player.getGeoIndex()), 146);
        return true;
    }

    // Refined Red Dragon Blood
    private boolean use21903(Player player, boolean ctrl) {
        if (!player.isInZone(AntharasManager.getZone()) && !player.isInZone(ValakasManager.getZone())) {
            player.sendPacket(new SystemMessage(SystemMessage.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addItemName(21903));
            return false;
        }
        player.doCast(SkillTable.getInstance().getInfo(22298, 1), player, false);
        Functions.removeItem(player, 21903, 1);
        return true;
    }

    // Refined Blue Dragon Blood
    private boolean use21904(Player player, boolean ctrl) {
        if (!player.isInZone(AntharasManager.getZone()) && !player.isInZone(ValakasManager.getZone())) {
            player.sendPacket(new SystemMessage(SystemMessage.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addItemName(21904));
            return false;
        }
        player.doCast(SkillTable.getInstance().getInfo(22299, 1), player, false);
        Functions.removeItem(player, 21904, 1);
        return true;
    }

    // Antharas Blood Crystal
    private boolean use17268(Player player, boolean ctrl) {
        if (!player.isInZone(AntharasManager.getZone())) {
            player.sendPacket(new SystemMessage(SystemMessage.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addItemName(17268));
            return false;
        }
        player.doCast(SkillTable.getInstance().getInfo(9179, 1), player, false);
        Functions.removeItem(player, 17268, 1);
        return true;
    }

    private boolean use17619(Player player, boolean ctrl) {
        //TODO[Mazaffaka] Define zone in cruma tower we can use this scroll only there!!
        QuestState qs = player.getQuestState(_10352_LegacyOfCrumaTower.class);
        QuestState qs2 = player.getQuestState(_480_AnotherLegacyOfCrumaTower.class);
        if (player.getVar("MechanismSpawn") != null || qs == null || qs.getCond() > 4) {
            if (qs2 == null || qs2.getCond() > 4 || player.getVar("MechanismSpawn") != null) {
                player.sendPacket(new SystemMessage(SystemMessage.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addItemName(17619));
                return false;
            }
        }

        Functions.removeItem(player, 17619, 1);
        NpcInstance npc = Functions.spawn(Location.findPointToStay(player.getLoc(), 50, 100, player.getGeoIndex()), 17619);
        player.setVar("MechanismSpawn", "1", 120000);
        if (qs != null && !qs.isCompleted()) {
            Quest q = QuestManager.getQuest(10352);
            player.processQuestEvent(q.getName(), "advanceCond3", null);
        }
        if (qs2 != null && !qs2.isCompleted()) {
            Quest q2 = QuestManager.getQuest(480);
            player.processQuestEvent(q2.getName(), "advanceCond3", null);
        }
        Functions.executeTask("handler.items.Special", "despawnNpc", new Object[]{npc, player}, 120000);
        return true;
    }

    private boolean use17604(Player player, boolean ctrl) {
        //TODO[Mazaffaka] Define zone in cruma tower we can use this scroll only there!!
        QuestState qs = player.getQuestState(_10301_ShadowOfTerrorBlackishRedFog.class);
        GameObject target = player.getTarget();
        if (target == null || !target.isNpc()) {
            player.sendPacket(new SystemMessage(SystemMessage.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addItemName(17604));
            return false;
        }

        NpcInstance _target = (NpcInstance) target;
        if (_target.getNpcId() != 33489) {
            player.sendPacket(new SystemMessage(SystemMessage.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addItemName(17604));
            return false;
        }

        if (qs == null || qs.getCond() != 2 || player.getVar("CrystalsSpawn") != null) {
            player.sendPacket(new SystemMessage(SystemMessage.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addItemName(17604));
            return false;
        }

        Functions.removeItem(player, 17604, 1);
        NpcInstance npc = Functions.spawn(Location.findPointToStay(player.getLoc(), 50, 100, player.getGeoIndex()), 32938);
        NpcInstance npc2 = Functions.spawn(Location.findPointToStay(player.getLoc(), 50, 100, player.getGeoIndex()), 32938);
        player.setVar("CrystalsSpawn", "1", 120000);
        Functions.executeTask("handler.items.Special", "despawnNpc", new Object[]{npc, player}, 120000);
        Functions.executeTask("handler.items.Special", "despawnNpc", new Object[]{npc2, player}, 120000);
        return true;
    }

    //Top Quality Petra
    private boolean use35702(Player player, boolean ctrl) {
        QuestState qs = player.getQuestState(_756_TopQualityPetra.class);
        if (player.getLevel() >= 97 && qs == null) {
            Quest q = QuestManager.getQuest(756);
            QuestState st = player.getQuestState(q.getClass());
            if (st.isNowAvailableByTime()) {
                st = q.newQuestState(player, Quest.CREATED);
                st.giveItems(35703, 1);
                st.setState(Quest.STARTED);
                st.setCond(1);
                st.showTutorialHTML("petra_of_zahaq_q0756_03.htm", TutorialShowHtml.TYPE_HTML);
            } else {
                st.showTutorialHTML("petra_of_zahaq_q0756_01.htm", TutorialShowHtml.TYPE_HTML);
                return false;
            }
        } else {
            qs.showTutorialHTML("petra_of_zahaq_q0756_02.htm", TutorialShowHtml.TYPE_HTML);
            return false;
        }
        return true;
    }

    public static void despawnNpc(NpcInstance npc, Player player) {
        if (npc != null)
            npc.deleteMe();
        if (player != null)
            player.unsetVar("CrystalsSpawn");
    }

    private boolean use34033(Player player, boolean ctrl) {
        QuestState qs = player.getQuestState(_10304_ForForgottenHeroes.class);
        NpcHtmlMessage msg = new NpcHtmlMessage(5);
        if (player.getLevel() >= 90 && qs == null) {
            Quest q = QuestManager.getQuest(10304);
            QuestState st = player.getQuestState(q.getClass());
            st = q.newQuestState(player, Quest.CREATED);
            st.setState(Quest.STARTED);
            st.setCond(1);
            useItem(player, 34033, 1);
            Functions.addItem(player, 17618, 1);
            msg.setFile("quests/_10304_ForTheForgottenHeroes/2.htm");
            player.sendPacket(msg);
        } else {
            msg.setFile("quests/_10304_ForTheForgottenHeroes/4.htm");
            player.sendPacket(msg);
            return false;
        }
        return true;
    }

    private static long useItem(Player player, int itemId, long count) {
        player.sendPacket(new SystemMessage(SystemMessage.YOU_USE_S1).addItemName(itemId));
        return Functions.removeItem(player, itemId, count);

    }
}
