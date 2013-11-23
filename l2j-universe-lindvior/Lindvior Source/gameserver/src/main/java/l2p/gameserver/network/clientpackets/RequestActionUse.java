/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package l2p.gameserver.network.clientpackets;

import l2p.commons.threading.RunnableImpl;
import l2p.gameserver.Config;
import l2p.gameserver.ThreadPoolManager;
import l2p.gameserver.ai.CtrlIntention;
import l2p.gameserver.cache.Msg;
import l2p.gameserver.geodata.GeoEngine;
import l2p.gameserver.instancemanager.PartySubstituteManager;
import l2p.gameserver.instancemanager.TacticalSignManager;
import l2p.gameserver.model.*;
import l2p.gameserver.model.entity.boat.ClanAirShip;
import l2p.gameserver.model.instances.PetBabyInstance;
import l2p.gameserver.model.instances.StaticObjectInstance;
import l2p.gameserver.model.instances.residences.SiegeFlagInstance;
import l2p.gameserver.model.quest.QuestState;
import l2p.gameserver.network.serverpackets.*;
import l2p.gameserver.network.serverpackets.components.SystemMsg;
import l2p.gameserver.tables.PetDataTable;
import l2p.gameserver.tables.PetSkillsTable;
import l2p.gameserver.tables.SkillTable;
import l2p.gameserver.utils.TradeHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * packet type id 0x56
 * format:		cddc
 */
public class RequestActionUse extends L2GameClientPacket {
    private static final int PLAYER_ACTION = 0;
    private static final int PET_ACTION = 1;
    private static final int SERVITOR_ACTION = 2;
    private static final int SERVITOR_GROUP_ACTION = 3;
    private static final int SOCIAL_ACTION = 4;
    private static final int COUPLE_ACTION = 5;

    private static final Logger _log = LoggerFactory.getLogger(RequestActionUse.class);

    private int _actionId;
    private boolean _ctrlPressed;
    private boolean _shiftPressed;

    /* type:
      * 0 - action

      // value > 0 = summon skill
      * 1 - Pet Action
      * 2 - Servitor Action
      * 3 - Servitor Group Action


      * 4 - social
      * 5 - couple social
      *
      * transform:
      * 0 для любых разрешено
      * 1 разрешено для некоторых
      * 2 запрещено для всех
      */
    public static enum Action {
        // Действия персонажей
        ACTION0(0, PLAYER_ACTION, 0, 1), // Сесть/встать
        ACTION1(1, PLAYER_ACTION, 0, 0), // Изменить тип передвижения, шаг/бег
        ACTION7(7, PLAYER_ACTION, 0, 1), // Next Target
        ACTION10(10, PLAYER_ACTION, 0, 1), // Запрос на создание приватного магазина продажи
        ACTION28(28, PLAYER_ACTION, 0, 1), // Запрос на создание приватного магазина покупки
        ACTION37(37, PLAYER_ACTION, 0, 1), // Создание магазина Common Craft
        ACTION38(38, PLAYER_ACTION, 0, 1), // Mount
        ACTION51(51, PLAYER_ACTION, 0, 1), // Создание магазина Dwarven Craft
        ACTION61(61, PLAYER_ACTION, 0, 1), // Запрос на создание приватного магазина продажи (Package)
        ACTION96(96, PLAYER_ACTION, 0, 1), // Quit Party Command Channel?
        ACTION97(97, PLAYER_ACTION, 0, 1), // Request Party Command Channel Info?

        ACTION67(67, PLAYER_ACTION, 0, 1), // Steer. Allows you to control the Airship.
        ACTION68(68, PLAYER_ACTION, 0, 1), // Cancel Control. Relinquishes control of the Airship.
        ACTION69(69, PLAYER_ACTION, 0, 1), // Destination Map. Choose from pre-designated locations.
        ACTION70(70, PLAYER_ACTION, 0, 1), // Exit Airship. Disembarks from the Airship.

        // Действия петов
        ACTION15(15, PET_ACTION, 0, 0), // Pet Change Move Type
        ACTION16(16, PET_ACTION, 0, 0), // Атака петом
        ACTION17(17, PET_ACTION, 0, 0), // Отмена действия у пета
        // Action 18 - Pet: Pickup item
        ACTION19(19, PET_ACTION, 0, 0), // Отзыв пета
        ACTION54(54, PET_ACTION, 0, 0), // Передвинуть пета к цели

        // Действия саммонов
        ACTION21(21, SERVITOR_ACTION, 0, 0), // Servitor Change Move Type
        ACTION22(22, SERVITOR_ACTION, 0, 0), // Атака саммоном
        ACTION23(23, SERVITOR_ACTION, 0, 0), // Отмена действия у саммона
        ACTION52(52, SERVITOR_ACTION, 0, 0), // Отзыв саммона
        ACTION53(53, SERVITOR_ACTION, 0, 0), // Передвинуть саммона к цели

        // Действия нескольких саммонов

        ACTION1100(1100, SERVITOR_GROUP_ACTION, 0, 0), // Движение к цели группы питомцев
        ACTION1101(1101, SERVITOR_GROUP_ACTION, 0, 0), // Отмена действий у группы питомцев
        ACTION1102(1102, SERVITOR_GROUP_ACTION, 0, 0), // Отзыв группы питомцев


        // Действия петов со скиллами
        ACTION32(32, SERVITOR_ACTION, 4230, 0), // Wild Hog Cannon - Mode Change
        ACTION36(36, SERVITOR_ACTION, 4259, 0), // Soulless - Toxic Smoke
        ACTION39(39, SERVITOR_ACTION, 4138, 0), // Soulless - Parasite Burst
        ACTION41(41, SERVITOR_ACTION, 4230, 0), // Wild Hog Cannon - Attack
        ACTION42(42, SERVITOR_ACTION, 4378, 0), // Kai the Cat - Self Damage Shield
        ACTION43(43, SERVITOR_ACTION, 4137, 0), // Unicorn Merrow - Hydro Screw
        ACTION44(44, SERVITOR_ACTION, 4139, 0), // Big Boom - Boom Attack
        ACTION45(45, SERVITOR_ACTION, 4025, 0), // Unicorn Boxer - Master Recharge
        ACTION46(46, SERVITOR_ACTION, 4261, 0), // Mew the Cat - Mega Storm Strike
        ACTION47(47, SERVITOR_ACTION, 4260, 0), // Silhouette - Steal Blood
        ACTION48(48, SERVITOR_ACTION, 4068, 0), // Mechanic Golem - Mech. Cannon
        ACTION1000(1000, SERVITOR_ACTION, 4079, 0), // Siege Golem - Siege Hammer
        //ACTION1001(1001, 2, , 0), // Sin Eater - Ultimate Bombastic Buster
        ACTION1003(1003, PET_ACTION, 4710, 0), // Wind Hatchling/Strider - Wild Stun
        ACTION1004(1004, PET_ACTION, 4711, 0), // Wind Hatchling/Strider - Wild Defense
        ACTION1005(1005, PET_ACTION, 4712, 0), // Star Hatchling/Strider - Bright Burst
        ACTION1006(1006, PET_ACTION, 4713, 0), // Star Hatchling/Strider - Bright Heal
        ACTION1007(1007, SERVITOR_ACTION, 4699, 0), // Cat Queen - Blessing of Queen
        ACTION1008(1008, SERVITOR_ACTION, 4700, 0), // Cat Queen - Gift of Queen
        ACTION1009(1009, SERVITOR_ACTION, 4701, 0), // Cat Queen - Cure of Queen
        ACTION1010(1010, SERVITOR_ACTION, 4702, 0), // Unicorn Seraphim - Blessing of Seraphim
        ACTION1011(1011, SERVITOR_ACTION, 4703, 0), // Unicorn Seraphim - Gift of Seraphim
        ACTION1012(1012, SERVITOR_ACTION, 4704, 0), // Unicorn Seraphim - Cure of Seraphim
        ACTION1013(1013, SERVITOR_ACTION, 4705, 0), // Nightshade - Curse of Shade
        ACTION1014(1014, SERVITOR_ACTION, 4706, 0), // Nightshade - Mass Curse of Shade
        ACTION1015(1015, SERVITOR_ACTION, 4707, 0), // Nightshade - Shade Sacrifice
        ACTION1016(1016, SERVITOR_ACTION, 4709, 0), // Cursed Man - Cursed Blow
        ACTION1017(1017, SERVITOR_ACTION, 4708, 0), // Cursed Man - Cursed Strike/Stun
        ACTION1031(1031, SERVITOR_ACTION, 5135, 0), // Feline King - Slash
        ACTION1032(1032, SERVITOR_ACTION, 5136, 0), // Feline King - Spin Slash
        ACTION1033(1033, SERVITOR_ACTION, 5137, 0), // Feline King - Hold of King
        ACTION1034(1034, SERVITOR_ACTION, 5138, 0), // Magnus the Unicorn - Whiplash
        ACTION1035(1035, SERVITOR_ACTION, 5139, 0), // Magnus the Unicorn - Tridal Wave
        ACTION1036(1036, SERVITOR_ACTION, 5142, 0), // Spectral Lord - Corpse Kaboom
        ACTION1037(1037, SERVITOR_ACTION, 5141, 0), // Spectral Lord - Dicing Death
        ACTION1038(1038, SERVITOR_ACTION, 5140, 0), // Spectral Lord - Force Curse
        ACTION1039(1039, SERVITOR_ACTION, 5110, 0), // Swoop Cannon - Cannon Fodder
        ACTION1040(1040, SERVITOR_ACTION, 5111, 0), // Swoop Cannon - Big Bang
        ACTION1041(1041, PET_ACTION, 5442, 0), // Great Wolf - 5442 - Bite Attack
        ACTION1042(1042, PET_ACTION, 5444, 0), // Great Wolf - 5444 - Moul
        ACTION1043(1043, PET_ACTION, 5443, 0), // Great Wolf - 5443 - Cry of the Wolf
        ACTION1044(1044, PET_ACTION, 5445, 0), // Great Wolf - 5445 - Awakening 70
        ACTION1045(1045, PET_ACTION, 5584, 0), // Wolf Howl
        ACTION1046(1046, PET_ACTION, 5585, 0), // Strider - Roar // TODO скилл не отображается даже на 85 уровне, вероятно нужно корректировать поле type в PetInfo для страйдеров
        ACTION1047(1047, SERVITOR_ACTION, 5580, 0), // Divine Beast - Bite
        ACTION1048(1048, SERVITOR_ACTION, 5581, 0), // Divine Beast - Stun Attack
        ACTION1049(1049, SERVITOR_ACTION, 5582, 0), // Divine Beast - Fire Breath
        ACTION1050(1050, SERVITOR_ACTION, 5583, 0), // Divine Beast - Roar
        ACTION1051(1051, SERVITOR_ACTION, 5638, 0), // Feline Queen - Bless The Body
        ACTION1052(1052, SERVITOR_ACTION, 5639, 0), // Feline Queen - Bless The Soul
        ACTION1053(1053, SERVITOR_ACTION, 5640, 0), // Feline Queen - Haste
        ACTION1054(1054, SERVITOR_ACTION, 5643, 0), // Unicorn Seraphim - Acumen
        ACTION1055(1055, SERVITOR_ACTION, 5647, 0), // Unicorn Seraphim - Clarity
        ACTION1056(1056, SERVITOR_ACTION, 5648, 0), // Unicorn Seraphim - Empower
        ACTION1057(1057, SERVITOR_ACTION, 5646, 0), // Unicorn Seraphim - Wild Magic
        ACTION1058(1058, SERVITOR_ACTION, 5652, 0), // Nightshade - Death Whisper
        ACTION1059(1059, SERVITOR_ACTION, 5653, 0), // Nightshade - Focus
        ACTION1060(1060, SERVITOR_ACTION, 5654, 0), // Nightshade - Guidance
        ACTION1061(1061, PET_ACTION, 5745, 0), // (Wild Beast Fighter, White Weasel) Death Blow - Awakens a hidden ability to inflict a powerful attack on the enemy. Requires application of the Awakening skill.
        ACTION1062(1062, PET_ACTION, 5746, 0), // (Wild Beast Fighter) Double Attack - Rapidly attacks the enemy twice.
        ACTION1063(1063, PET_ACTION, 5747, 0), // (Wild Beast Fighter) Spin Attack - Inflicts shock and damage to the enemy at the same time with a powerful spin attack.
        ACTION1064(1064, PET_ACTION, 5748, 0), // (Wild Beast Fighter) Meteor Shower - Attacks nearby enemies with a doll heap attack.
        ACTION1065(1065, PET_ACTION, 5753, 0), // (Fox Shaman, Wild Beast Fighter, White Weasel, Fairy Princess) Awakening - Awakens a hidden ability.
        ACTION1066(1066, PET_ACTION, 5749, 0), // (Fox Shaman, Spirit Shaman) Thunder Bolt - Attacks the enemy with the power of thunder.
        ACTION1067(1067, PET_ACTION, 5750, 0), // (Fox Shaman, Spirit Shaman) Flash - Inflicts a swift magic attack upon contacted enemies nearby.
        ACTION1068(1068, PET_ACTION, 5751, 0), // (Fox Shaman, Spirit Shaman) Lightning Wave - Attacks nearby enemies with the power of lightning.
        ACTION1069(1069, PET_ACTION, 5752, 0), // (Fox Shaman, Fairy Princess) Flare - Awakens a hidden ability to inflict a powerful attack on the enemy. Requires application of the Awakening skill.
        ACTION1070(1070, PET_ACTION, 5771, 0), // (White Weasel, Fairy Princess, Improved Baby Buffalo, Improved Baby Kookaburra, Improved Baby Cougar) Buff Control - Controls to prevent a buff upon the master. Lasts for 5 minutes.
        ACTION1071(1071, PET_ACTION, 5761, 0), // (Tigress) Power Striker - Powerfully attacks the target.
        ACTION1072(1072, PET_ACTION, 6046, 0), // (Toy Knight) Piercing attack
        ACTION1073(1073, PET_ACTION, 6047, 0), // (Toy Knight) Whirlwind
        ACTION1074(1074, PET_ACTION, 6048, 0), // (Toy Knight) Lance Smash
        ACTION1075(1075, PET_ACTION, 6049, 0), // (Toy Knight) Battle Cry
        ACTION1076(1076, PET_ACTION, 6050, 0), // (Turtle Ascetic) Power Smash
        ACTION1077(1077, PET_ACTION, 6051, 0), // (Turtle Ascetic) Energy Burst
        ACTION1078(1078, PET_ACTION, 6052, 0), // (Turtle Ascetic) Shockwave
        ACTION1079(1079, PET_ACTION, 6053, 0), // (Turtle Ascetic) Howl
        ACTION1080(1080, SERVITOR_ACTION, 6041, 0), // Phoenix Rush
        ACTION1081(1081, SERVITOR_ACTION, 6042, 0), // Phoenix Cleanse
        ACTION1082(1082, SERVITOR_ACTION, 6043, 0), // Phoenix Flame Feather
        ACTION1083(1083, SERVITOR_ACTION, 6044, 0), // Phoenix Flame Beak
        ACTION1084(1084, SERVITOR_ACTION, 6054, 0), // (Spirit Shaman, Toy Knight, Turtle Ascetic) Switch State - Toggles you between Attack and Support modes.
        ACTION1086(1086, SERVITOR_ACTION, 6094, 0), // Panther Cancel
        ACTION1087(1087, SERVITOR_ACTION, 6095, 0), // Panther Dark Claw
        ACTION1088(1088, SERVITOR_ACTION, 6096, 0), // Panther Fatal Claw
        ACTION1089(1089, PET_ACTION, 6199, 0), // (Deinonychus) Tail Strike
        ACTION1090(1090, SERVITOR_ACTION, 6205, 0), // (Guardian's Strider) Strider Bite
        ACTION1091(1091, SERVITOR_ACTION, 6206, 0), // (Guardian's Strider) Strider Fear
        ACTION1092(1092, SERVITOR_ACTION, 6207, 0), // (Guardian's Strider) Strider Dash
        ACTION1093(1093, SERVITOR_ACTION, 6618, 0),
        ACTION1094(1094, SERVITOR_ACTION, 6681, 0),
        ACTION1095(1095, SERVITOR_ACTION, 6619, 0),
        ACTION1096(1096, SERVITOR_ACTION, 6682, 0),
        ACTION1097(1097, SERVITOR_ACTION, 6683, 0),
        ACTION1098(1098, SERVITOR_ACTION, 6684, 0),
        ACTION1099(1099, SERVITOR_GROUP_ACTION, 0, 0),

        ACTION5000(5000, PET_ACTION, 23155, 0), // Baby Rudolph - Reindeer Scratch
        ACTION5001(5001, PET_ACTION, 23167, 0),
        ACTION5002(5002, PET_ACTION, 23168, 0),
        ACTION5003(5003, PET_ACTION, 5749, 0),
        ACTION5004(5004, PET_ACTION, 5750, 0),
        ACTION5005(5005, PET_ACTION, 5751, 0),
        ACTION5006(5006, PET_ACTION, 5771, 0),
        ACTION5007(5007, PET_ACTION, 6046, 0),
        ACTION5008(5008, PET_ACTION, 6047, 0),
        ACTION5009(5009, PET_ACTION, 6048, 0),
        ACTION5010(5010, PET_ACTION, 6049, 0),
        ACTION5011(5011, PET_ACTION, 6050, 0),
        ACTION5012(5012, PET_ACTION, 6051, 0),
        ACTION5013(5013, PET_ACTION, 6052, 0),
        ACTION5014(5014, PET_ACTION, 6053, 0),
        ACTION5015(5015, PET_ACTION, 6054, 0),
        ACTION5016(5016, PET_ACTION, 0, 0),

        ACTION1103(1103, PET_ACTION, 0, 0), // Пассивность. Пет не реагирует на атаки врагов
        ACTION1104(1104, PET_ACTION, 0, 0), // Защита. Пет отвечает, если атакован он или хозяин

        ACTION1106(1106, SERVITOR_GROUP_ACTION, 11278, 0),// Bear Claw
        ACTION1107(1107, SERVITOR_GROUP_ACTION, 11279, 0),// Bear Tumbling

        ACTION1108(1108, SERVITOR_GROUP_ACTION, 11280, 0),// Cougar Bite
        ACTION1109(1109, SERVITOR_GROUP_ACTION, 11281, 0),// Cougar Pounce

        ACTION1110(1110, SERVITOR_GROUP_ACTION, 11282, 0),// Reaper Touch
        ACTION1111(1111, SERVITOR_GROUP_ACTION, 11283, 0),// Reaper Power

        ACTION1113(1113, SERVITOR_ACTION, 10051, 0),// Lion's Roar
        ACTION1114(1114, SERVITOR_ACTION, 10052, 0),// Lion's Claw
        ACTION1115(1115, SERVITOR_ACTION, 10053, 0),// Lion's Dash
        ACTION1116(1116, SERVITOR_ACTION, 10054, 0),// Lion's Red Flame

        ACTION1117(1117, SERVITOR_ACTION, 10794, 0), // Полет Ястреба
        ACTION1118(1118, SERVITOR_ACTION, 10795, 0), // Очищение Ястреба
        ACTION1119(1119, SERVITOR_ACTION, 10796, 0), // Охрана Ястреба
        ACTION1120(1120, SERVITOR_ACTION, 10797, 0), // Стальное Перо
        ACTION1121(1121, SERVITOR_ACTION, 10798, 0), // Стальные Когти
        ACTION1122(1122, 1, 0, 0),// Blessing of Life
        ACTION1123(1123, 1, 0, 0),// Siege Punch

        // Социальные действия
        ACTION12(12, SOCIAL_ACTION, SocialAction.GREETING, 2),
        ACTION13(13, SOCIAL_ACTION, SocialAction.VICTORY, 2),
        ACTION14(14, SOCIAL_ACTION, SocialAction.ADVANCE, 2),
        ACTION24(24, SOCIAL_ACTION, SocialAction.YES, 2),
        ACTION25(25, SOCIAL_ACTION, SocialAction.NO, 2),
        ACTION26(26, SOCIAL_ACTION, SocialAction.BOW, 2),
        ACTION29(29, SOCIAL_ACTION, SocialAction.UNAWARE, 2),
        ACTION30(30, SOCIAL_ACTION, SocialAction.WAITING, 2),
        ACTION31(31, SOCIAL_ACTION, SocialAction.LAUGH, 2),
        ACTION33(33, SOCIAL_ACTION, SocialAction.APPLAUD, 2),
        ACTION34(34, SOCIAL_ACTION, SocialAction.DANCE, 2),
        ACTION35(35, SOCIAL_ACTION, SocialAction.SORROW, 2),
        ACTION62(62, SOCIAL_ACTION, SocialAction.CHARM, 2),
        ACTION66(66, SOCIAL_ACTION, SocialAction.SHYNESS, 2),
        ACTION87(87, SOCIAL_ACTION, SocialAction.Tauty1, 2),   //TAUTI
        ACTION88(88, SOCIAL_ACTION, SocialAction.Tauty2, 2),   //TAUTI
        ACTION89(89, SOCIAL_ACTION, SocialAction.lv_1, 2),   //Lindvior

        // Парные социальные действия
        ACTION71(71, COUPLE_ACTION, SocialAction.COUPLE_BOW, 2),
        ACTION72(72, COUPLE_ACTION, SocialAction.COUPLE_HIGH_FIVE, 2),
        ACTION73(73, COUPLE_ACTION, SocialAction.COUPLE_DANCE, 2),

        /* NOT USED BY SERVER
        ACTION74(74, 0, 0, 1),
        ACTION75(75, 0, 0, 1),
        ACTION76(76, 0, 0, 1),
        ACTION77(77, 0, 0, 1),
          */

        // 8 Tactical Signs
        ACTION78(78, PLAYER_ACTION, 0, 1),
        ACTION79(79, PLAYER_ACTION, 0, 1),
        ACTION80(80, PLAYER_ACTION, 0, 1),
        ACTION81(81, PLAYER_ACTION, 0, 1),
        ACTION82(82, PLAYER_ACTION, 0, 1),
        ACTION83(83, PLAYER_ACTION, 0, 1),
        ACTION84(84, PLAYER_ACTION, 0, 1),
        ACTION85(85, PLAYER_ACTION, 0, 1),

        ACTION86(86, PLAYER_ACTION, 0, 1); // Start/Stop Automatic Party Search

        public int id;
        public int type; // type == 2 скилы питомцев. type == 5 - действия саммона (одного) type == 6 - действия нескольких саммонов
        public int value;
        public int transform;

        private Action(int id, int type, int value, int transform) {
            this.id = id;
            this.type = type;
            this.value = value;
            this.transform = transform;
        }

        public static Action find(int id) {
            for (Action action : Action.values())
                if (action.id == id)
                    return action;
            return null;
        }
    }

    @Override
    protected void readImpl() {
        _actionId = readD();
        _ctrlPressed = readD() == 1;
        _shiftPressed = readC() == 1;
    }

    @Override
    protected void runImpl() {
        Player activeChar = getClient().getActiveChar();
        if (activeChar == null)
            return;

        Action action = Action.find(_actionId);
        if (action == null) {
            _log.warn("unhandled action type " + _actionId + " by player " + activeChar.getName());
            activeChar.sendActionFailed();
            return;
        }

        final boolean usePet = action.type == PET_ACTION;
        final boolean useServitor = action.type == SERVITOR_ACTION;
        final boolean useServitorGroup = action.type == SERVITOR_GROUP_ACTION;

        final GameObject target = activeChar.getTarget();

        final Summon pet = activeChar.getSummonList().getPet();
        final Summon servitor = activeChar.getSummonList().getFirstServitor();
        final List<Summon> servitors = activeChar.getSummonList().getServitors();

        // dont do anything if player is dead or confused
        if (!usePet && !useServitor && !useServitorGroup && (activeChar.isOutOfControl() || activeChar.isActionsDisabled()) && !(activeChar.isFakeDeath() && _actionId == 0)) {
            activeChar.sendActionFailed();
            return;
        }

        if (activeChar.getTransformation() != 0 && action.transform > 0) // TODO разрешить для некоторых трансформ
        {
            activeChar.sendActionFailed();
            return;
        }


        // Действия петов и саммонов
        switch (action.type) {
            case PLAYER_ACTION: // Действия игроков
                switch (action.id) {
                    case 0: // Сесть/встать
                        // На страйдере нельзя садиться
                        if (activeChar.isMounted()) {
                            activeChar.sendActionFailed();
                            break;
                        }

                        if (activeChar.isFakeDeath()) {
                            activeChar.breakFakeDeath();
                            activeChar.updateEffectIcons();
                            break;
                        }

                        if (!activeChar.isSitting()) {
                            if (target != null && target instanceof StaticObjectInstance && ((StaticObjectInstance) target).getType() == 1 && activeChar.getDistance3D(target) <= Creature.INTERACTION_DISTANCE)
                                activeChar.sitDown((StaticObjectInstance) target);
                            else
                                activeChar.sitDown(null);
                        } else
                            activeChar.standUp();

                        break;
                    case 1: // Изменить тип передвижения, шаг/бег
                        if (activeChar.isRunning())
                            activeChar.setWalking();
                        else
                            activeChar.setRunning();
                        break;
                    case 10: // Запрос на создание приватного магазина продажи
                    case 61: // Запрос на создание приватного магазина продажи (Package)
                    {
                        if (activeChar.getSittingTask()) {
                            activeChar.sendActionFailed();
                            return;
                        }
                        if (activeChar.isInStoreMode()) {
                            activeChar.setPrivateStoreType(Player.STORE_PRIVATE_NONE);
                            activeChar.standUp();
                            activeChar.broadcastCharInfo();
                        } else if (!TradeHelper.checksIfCanOpenStore(activeChar, _actionId == 61 ? Player.STORE_PRIVATE_SELL_PACKAGE : Player.STORE_PRIVATE_SELL)) {
                            activeChar.sendActionFailed();
                            return;
                        }
                        activeChar.sendPacket(new PrivateStoreManageListSell(activeChar, _actionId == 61));
                        break;
                    }
                    case 28: // Запрос на создание приватного магазина покупки
                    {
                        if (activeChar.getSittingTask()) {
                            activeChar.sendActionFailed();
                            return;
                        }
                        if (activeChar.isInStoreMode()) {
                            activeChar.setPrivateStoreType(Player.STORE_PRIVATE_NONE);
                            activeChar.standUp();
                            activeChar.broadcastCharInfo();
                        } else if (!TradeHelper.checksIfCanOpenStore(activeChar, Player.STORE_PRIVATE_BUY)) {
                            activeChar.sendActionFailed();
                            return;
                        }
                        activeChar.sendPacket(new PrivateStoreManageListBuy(activeChar));
                        break;
                    }
                    case 51: // Создание магазина Dwarven Craft
                    {
                        if (activeChar.getSittingTask()) {
                            activeChar.sendActionFailed();
                            return;
                        }
                        if (activeChar.isInStoreMode()) {
                            activeChar.setPrivateStoreType(Player.STORE_PRIVATE_NONE);
                            activeChar.standUp();
                            activeChar.broadcastCharInfo();
                        } else if (!TradeHelper.checksIfCanOpenStore(activeChar, Player.STORE_PRIVATE_MANUFACTURE)) {
                            activeChar.sendActionFailed();
                            return;
                        }
                        activeChar.sendPacket(new RecipeShopManageList(activeChar, true));
                        break;
                    }
                    case 38: // Mount
                        if (activeChar.getTransformation() != 0)
                            activeChar.sendPacket(SystemMsg.YOU_CANNOT_BOARD_BECAUSE_YOU_DO_NOT_MEET_THE_REQUIREMENTS);
                        else if (activeChar.isMounted()) {
                            if (activeChar.isFlying() && !activeChar.checkLandingState()) // Виверна
                            {
                                activeChar.sendPacket(Msg.YOU_ARE_NOT_ALLOWED_TO_DISMOUNT_AT_THIS_LOCATION, ActionFail.STATIC);
                                activeChar.sendActionFailed();
                                return;
                            }
                            activeChar.setMount(0, 0, 0);
                        } else if (activeChar.isMounted() || activeChar.isInBoat())
                            activeChar.sendPacket(Msg.YOU_CANNOT_MOUNT_BECAUSE_YOU_DO_NOT_MEET_THE_REQUIREMENTS);
                        else if (activeChar.isDead())
                            activeChar.sendPacket(Msg.YOU_CANNOT_MOUNT_BECAUSE_YOU_DO_NOT_MEET_THE_REQUIREMENTS);
                        else if (activeChar.isInDuel())
                            activeChar.sendPacket(Msg.YOU_CANNOT_MOUNT_BECAUSE_YOU_DO_NOT_MEET_THE_REQUIREMENTS);
                        else if (activeChar.isFishing())
                            activeChar.sendPacket(Msg.YOU_CANNOT_MOUNT_BECAUSE_YOU_DO_NOT_MEET_THE_REQUIREMENTS);
                        else if (activeChar.isSitting())
                            activeChar.sendPacket(Msg.YOU_CANNOT_MOUNT_BECAUSE_YOU_DO_NOT_MEET_THE_REQUIREMENTS);
                        else if (activeChar.isCursedWeaponEquipped())
                            activeChar.sendPacket(Msg.YOU_CANNOT_MOUNT_BECAUSE_YOU_DO_NOT_MEET_THE_REQUIREMENTS);
                        else if (activeChar.getActiveWeaponFlagAttachment() != null)
                            activeChar.sendPacket(Msg.YOU_CANNOT_MOUNT_BECAUSE_YOU_DO_NOT_MEET_THE_REQUIREMENTS);
                        else if (activeChar.isCastingNow())
                            activeChar.sendPacket(Msg.YOU_CANNOT_MOUNT_BECAUSE_YOU_DO_NOT_MEET_THE_REQUIREMENTS);
                        else if (activeChar.isParalyzed())
                            activeChar.sendPacket(Msg.YOU_CANNOT_MOUNT_BECAUSE_YOU_DO_NOT_MEET_THE_REQUIREMENTS);
                        else if (pet == null || activeChar.isInCombat() || pet.isInCombat())
                            activeChar.sendPacket(Msg.YOU_CANNOT_MOUNT_BECAUSE_YOU_DO_NOT_MEET_THE_REQUIREMENTS);
                        else if (pet.isDead())
                            activeChar.sendPacket(Msg.A_DEAD_PET_CANNOT_BE_RIDDEN);
                        else if (pet != null && pet.isMountable()) {
                            activeChar.getEffectList().stopEffect(Skill.SKILL_EVENT_TIMER);
                            activeChar.setMount(pet.getTemplate().npcId, pet.getObjectId(), pet.getLevel());
                            activeChar.getSummonList().unsummonPet();
                        }
                        break;
                    case 37: // Создание магазина Common Craft
                    {
                        if (activeChar.getSittingTask()) {
                            activeChar.sendActionFailed();
                            return;
                        }
                        if (activeChar.isInStoreMode()) {
                            activeChar.setPrivateStoreType(Player.STORE_PRIVATE_NONE);
                            activeChar.standUp();
                            activeChar.broadcastCharInfo();
                        } else if (!TradeHelper.checksIfCanOpenStore(activeChar, Player.STORE_PRIVATE_MANUFACTURE)) {
                            activeChar.sendActionFailed();
                            return;
                        }
                        activeChar.sendPacket(new RecipeShopManageList(activeChar, false));
                        break;
                    }
                    case 67: // Steer. Allows you to control the Airship.
                        if (activeChar.isInBoat() && activeChar.getBoat().isClanAirShip() && !activeChar.getBoat().isMoving) {
                            ClanAirShip boat = (ClanAirShip) activeChar.getBoat();
                            if (boat.getDriver() == null)
                                boat.setDriver(activeChar);
                            else
                                activeChar.sendPacket(SystemMsg.ANOTHER_PLAYER_IS_PROBABLY_CONTROLLING_THE_TARGET);
                        }
                        break;
                    case 68: // Cancel Control. Relinquishes control of the Airship.
                        if (activeChar.isClanAirShipDriver()) {
                            ClanAirShip boat = (ClanAirShip) activeChar.getBoat();
                            boat.setDriver(null);
                            activeChar.broadcastCharInfo();
                        }
                        break;
                    case 69: // Destination Map. Choose from pre-designated locations.
                        if (activeChar.isClanAirShipDriver() && activeChar.getBoat().isDocked())
                            activeChar.sendPacket(new ExAirShipTeleportList((ClanAirShip) activeChar.getBoat()));
                        break;
                    case 70: // Exit Airship. Disembarks from the Airship.
                        if (activeChar.isInBoat() && activeChar.getBoat().isAirShip() && activeChar.getBoat().isDocked())
                            activeChar.getBoat().oustPlayer(activeChar, activeChar.getBoat().getReturnLoc(), true);
                        break;
                    case 78:
                        TacticalSignManager.setTacticalSign(activeChar, target, 1);
                        break;
                    case 79:
                        TacticalSignManager.setTacticalSign(activeChar, target, 2);
                        break;
                    case 80:
                        TacticalSignManager.setTacticalSign(activeChar, target, 3);
                        break;
                    case 81:
                        TacticalSignManager.setTacticalSign(activeChar, target, 4);
                        break;
                    case 82:
                        TacticalSignManager.getTargetOnTacticalSign(activeChar, 1);
                        break;
                    case 83:
                        TacticalSignManager.getTargetOnTacticalSign(activeChar, 2);
                        break;
                    case 84:
                        TacticalSignManager.getTargetOnTacticalSign(activeChar, 3);
                        break;
                    case 85:
                        TacticalSignManager.getTargetOnTacticalSign(activeChar, 4);
                        break;
                    case 86:
                        if (PartySubstituteManager.getInstance().isPlayerToParty(activeChar))
                            PartySubstituteManager.getInstance().removePlayerFromParty(activeChar);
                        PartySubstituteManager.getInstance().addPlayerToParty(activeChar);
                        break;
                    case 96: // Quit Party Command Channel?
                        _log.info("96 Accessed");
                        break;
                    case 97: // Request Party Command Channel Info?
                        _log.info("97 Accessed");
                        break;
                }
                break;
            case PET_ACTION:
                if (pet == null || pet.isOutOfControl()) {
                    activeChar.sendActionFailed();
                    return;
                }
                if (pet.isDepressed()) {
                    activeChar.sendPacket(SystemMsg.YOUR_PETSERVITOR_IS_UNRESPONSIVE_AND_WILL_NOT_OBEY_ANY_ORDERS);
                    return;
                }

                switch (action.id) {
                    case 15: // Follow для петов
                        pet.setFollowMode(!pet.isFollowMode());
                        break;
                    case 16: // Атака петом
                        pet.setFollowMode(!pet.isFollowMode());
                        if (target == null || !target.isCreature() || pet == target || pet.isDead()) {
                            activeChar.sendActionFailed();
                            return;
                        }

                        if (activeChar.isInOlympiadMode() && !activeChar.isOlympiadCompStart()) {
                            activeChar.sendActionFailed();
                            return;
                        }

                        // Sin Eater
                        if (pet.getTemplate().getNpcId() == PetDataTable.SIN_EATER_ID) {
                            activeChar.sendActionFailed();
                            return;
                        }

                        if (!_ctrlPressed && target.isCreature() && !((Creature) target).isAutoAttackable(pet)) {
                            activeChar.sendActionFailed();
                            return;
                        }


                        if (_ctrlPressed && !target.isAttackable(pet)) {
                            activeChar.sendPacket(SystemMsg.INVALID_TARGET);
                            activeChar.sendActionFailed();
                            return;
                        }

                        if (!target.isMonster() && (pet.isInZonePeace() || target.isCreature() && ((Creature) target).isInZonePeace())) {
                            activeChar.sendPacket(SystemMsg.YOU_MAY_NOT_ATTACK_THIS_TARGET_IN_A_PEACEFUL_ZONE);
                            activeChar.sendActionFailed();
                            return;
                        }

                        if (activeChar.getLevel() + 20 <= pet.getLevel()) {
                            activeChar.sendPacket(SystemMsg.YOUR_PET_IS_TOO_HIGH_LEVEL_TO_CONTROL);
                            activeChar.sendActionFailed();
                            return;
                        }

                        pet.getAI().Attack(target, _ctrlPressed, _shiftPressed);
                        break;
                    case 17: // Отмена действия у петов
                        pet.getAI().setIntention(CtrlIntention.AI_INTENTION_ACTIVE);
                        break;
                    case 19: // Отзыв пета
                        if (pet.isDead()) {
                            activeChar.sendPacket(SystemMsg.DEAD_PETS_CANNOT_BE_RETURNED_TO_THEIR_SUMMONING_ITEM, ActionFail.STATIC);
                            return;
                        }

                        if (pet.isInCombat()) {
                            activeChar.sendPacket(SystemMsg.A_PET_CANNOT_BE_UNSUMMONED_DURING_BATTLE, ActionFail.STATIC);
                            return;
                        }

                        if (!PetDataTable.isVitaminPet(pet.getNpcId()) && pet.isPet() && pet.getCurrentFed() < 0.55 * pet.getMaxFed()) {
                            activeChar.sendPacket(SystemMsg.YOU_MAY_NOT_RESTORE_A_HUNGRY_PET, ActionFail.STATIC);
                            return;
                        }
                        activeChar.getSummonList().unsummonPet();
                        break;
                    case 54: // Передвинуть пета к цели
                        if (target != null && pet != target && !pet.isMovementDisabled()) {
                            pet.setFollowMode(false);
                            pet.moveToLocation(target.getLoc(), 100, true);
                        }
                        break;
                    case 1070:
                        if (pet instanceof PetBabyInstance)
                            ((PetBabyInstance) pet).triggerBuff();
                        break;
                }
                if (action.value > 0) {
                    UseSkill(action.value, pet);
                    activeChar.sendActionFailed();
                    return;
                }
                break;
            case SERVITOR_ACTION:
                if (servitor == null) {
                    activeChar.sendActionFailed();
                    return;
                }
                switch (action.id) {
                    case 21: // Follow для саммонов
                        servitor.setFollowMode(!servitor.isFollowMode());
                        break;
                    case 22: // Атака саммонами
                        if (target == null || !target.isCreature()) {
                            activeChar.sendActionFailed();
                            return;
                        }

                        if (servitor == target || servitor.isDead()) {
                            activeChar.sendActionFailed();
                            return;
                        }

                        if (activeChar.isInOlympiadMode() && !activeChar.isOlympiadCompStart()) {
                            activeChar.sendActionFailed();
                            return;
                        }

                        if (_ctrlPressed) {
                            if (!target.isAttackable(servitor)) {
                                activeChar.sendPacket(SystemMsg.INVALID_TARGET);
                                activeChar.sendActionFailed();
                                return;
                            }
                        } else if (!_ctrlPressed && target.isCreature()) {
                            if (!((Creature) target).isAutoAttackable(servitor)) {
                                activeChar.sendActionFailed();
                                return;
                            }
                        }


                        if (!target.isMonster() && (target.isCreature() && ((Creature) target).isInZonePeace())) {
                            if (servitor.isInZonePeace()) {
                                activeChar.sendPacket(SystemMsg.YOU_MAY_NOT_ATTACK_THIS_TARGET_IN_A_PEACEFUL_ZONE);
                                activeChar.sendActionFailed();
                                return;
                            }
                        }
                        servitor.setFollowMode(!servitor.isFollowMode());
                        servitor.getAI().Attack(target, _ctrlPressed, _shiftPressed);
                        break;
                    case 23: // Отмена действия у саммонов
                        servitor.getAI().setIntention(CtrlIntention.AI_INTENTION_ACTIVE);
                        break;
                    case 52: // Отзыв саммона
                        if (servitor.isInCombat()) {
                            activeChar.sendPacket(SystemMsg.A_PET_CANNOT_BE_UNSUMMONED_DURING_BATTLE);
                            activeChar.sendActionFailed();
                        } else {
                            servitor.saveEffects();
                            activeChar.getSummonList().unsummonAllServitors();
                        }
                        break;
                    case 53: // Передвинуть пета к цели
                        if (target != null && servitor != target && !servitor.isMovementDisabled()) {
                            servitor.setFollowMode(false);
                            servitor.moveToLocation(target.getLoc(), 100, true);
                        }
                        break;
                    case 1000:
                        if (target != null && !target.isDoor()) {
                            activeChar.sendActionFailed();
                            return;
                        }
                        break;
                    case 1039:
                    case 1040:
                        if (target.isDoor() || target instanceof SiegeFlagInstance) {
                            activeChar.sendActionFailed();
                            return;
                        }
                        break;
                }
                if (action.value > 0) {
                    UseSkill(action.value, servitor);
                    activeChar.sendActionFailed();
                    return;
                }
            case SERVITOR_GROUP_ACTION:
                if (servitors.isEmpty()) {
                    activeChar.sendActionFailed();
                    return;
                }
                switch (action.id) {
                    case 1099: // Атака группой питомцев
                        if (target == null || !target.isCreature()) {
                            activeChar.sendActionFailed();
                            return;
                        }

                        if (activeChar.isInOlympiadMode() && !activeChar.isOlympiadCompStart()) {
                            activeChar.sendActionFailed();
                            return;
                        }

                        if (servitors.contains(target)) {
                            activeChar.sendActionFailed();
                            return;
                        }

                        for (Summon summon : servitors) {
                            if (_ctrlPressed) {
                                if (!target.isAttackable(summon)) {
                                    activeChar.sendPacket(SystemMsg.INVALID_TARGET);
                                    activeChar.sendActionFailed();
                                    return;
                                }
                            } else if (!_ctrlPressed && target.isCreature()) {
                                if (!((Creature) target).isAutoAttackable(summon)) {
                                    activeChar.sendActionFailed();
                                    return;
                                }
                            }

                            if (!target.isMonster() && (target.isCreature() && ((Creature) target).isInZonePeace())) {
                                if (summon.isInZonePeace()) {
                                    activeChar.sendPacket(SystemMsg.YOU_MAY_NOT_ATTACK_THIS_TARGET_IN_A_PEACEFUL_ZONE);
                                    activeChar.sendActionFailed();
                                    return;
                                }
                            }
                        }

                        for (Summon summon : servitors) {
                            if (!summon.isDead()) {
                                summon.setFollowMode(!summon.isFollowMode());
                                summon.getAI().Attack(target, _ctrlPressed, _shiftPressed);
                            }
                        }
                        break;
                    case 1100: // Движение группы питомцев к цели
                        if (target != null && !servitors.contains(target)) {
                            for (Summon summon : servitors) {
                                if (!summon.isMovementDisabled()) {
                                    summon.setFollowMode(false);
                                    summon.moveToLocation(target.getLoc(), 100, true);
                                }
                            }
                        }
                        break;
                    case 1101: // Отмена действий у группы питомцев
                        for (Summon summon : servitors)
                            summon.getAI().setIntention(CtrlIntention.AI_INTENTION_ACTIVE);
                        break;
                    case 1102: // Отзыв петов (групповых)
                        if (activeChar.getSummonList().isInCombat()) {
                            activeChar.sendPacket(SystemMsg.A_PET_CANNOT_BE_UNSUMMONED_DURING_BATTLE);
                            activeChar.sendActionFailed();
                        } else {
                            activeChar.getSummonList().unsummonAllServitors();
                        }
                        break;
                }

                if (action.value > 0) {
                    UseSkill(action.value, servitors.toArray(new Summon[servitors.size()]));
                    activeChar.sendActionFailed();
                    return;
                }
                break;
            case SOCIAL_ACTION:  // Социальные действия
                if (activeChar.isOutOfControl() || activeChar.getTransformation() != 0 || activeChar.isActionsDisabled() || activeChar.isSitting() || activeChar.getPrivateStoreType() != Player.STORE_PRIVATE_NONE || activeChar.isProcessingRequest()) {
                    activeChar.sendActionFailed();
                    return;
                }
                if (activeChar.isFishing()) {
                    activeChar.sendPacket(SystemMsg.YOU_CANNOT_DO_THAT_WHILE_FISHING_2);
                    activeChar.sendActionFailed();
                    return;
                }
                activeChar.broadcastPacket(new SocialAction(activeChar.getObjectId(), action.value));
                for (QuestState state : activeChar.getAllQuestsStates()) {
                    state.getQuest().notifySocialActionUse(state, action.value);
                }
                if (Config.ALT_SOCIAL_ACTION_REUSE) {
                    ThreadPoolManager.getInstance().schedule(new SocialTask(activeChar), 2600);
                    activeChar.startParalyzed();
                }
                break;
            case COUPLE_ACTION: // Парные социальные действия
                if (activeChar.isOutOfControl() || activeChar.isActionsDisabled() || activeChar.isSitting()) {
                    activeChar.sendActionFailed();
                    return;
                }
                if (target == null || !target.isPlayer()) {
                    activeChar.sendActionFailed();
                    return;
                }
                final Player pcTarget = target.getPlayer();
                if (pcTarget.isProcessingRequest() && pcTarget.getRequest().isTypeOf(Request.L2RequestType.COUPLE_ACTION)) {
                    activeChar.sendPacket(new SystemMessage2(SystemMsg.C1_IS_ALREADY_PARTICIPATING_IN_A_COUPLE_ACTION_AND_CANNOT_BE_REQUESTED_FOR_ANOTHER_COUPLE_ACTION).addName(pcTarget));
                    return;
                }
                if (pcTarget.isProcessingRequest()) {
                    activeChar.sendPacket(new SystemMessage2(SystemMsg.C1_IS_ON_ANOTHER_TASK).addName(pcTarget));
                    return;
                }
                if (!activeChar.isInRange(pcTarget, 300) || activeChar.isInRange(pcTarget, 25) || activeChar.getTargetId() == activeChar.getObjectId() || !GeoEngine.canSeeTarget(activeChar, pcTarget, false)) {
                    activeChar.sendPacket(SystemMsg.THE_REQUEST_CANNOT_BE_COMPLETED_BECAUSE_THE_TARGET_DOES_NOT_MEET_LOCATION_REQUIREMENTS);
                    return;
                }
                if (!activeChar.checkCoupleAction(pcTarget)) {
                    activeChar.sendActionFailed();
                    return;
                }

                new Request(Request.L2RequestType.COUPLE_ACTION, activeChar, pcTarget).setTimeout(10000L);
                activeChar.sendPacket(new SystemMessage2(SystemMsg.YOU_HAVE_REQUESTED_A_COUPLE_ACTION_WITH_C1).addName(pcTarget));
                pcTarget.sendPacket(new ExAskCoupleAction(activeChar.getObjectId(), action.value));

                if (Config.ALT_SOCIAL_ACTION_REUSE) {
                    ThreadPoolManager.getInstance().schedule(new SocialTask(activeChar), 2600);
                    activeChar.startParalyzed();
                }
                break;
        }
        activeChar.sendActionFailed();
    }

    private void UseSkill(int skillId, Summon... casters) {
        Player activeChar = getClient().getActiveChar();

        if (casters.length == 0)
            return;

        for (Summon summon : casters) {
            if (summon.isPet() && activeChar.getLevel() + 20 <= summon.getLevel()) {
                activeChar.sendPacket(SystemMsg.YOUR_PET_IS_TOO_HIGH_LEVEL_TO_CONTROL);
                continue;
            }
            int skillLevel = PetSkillsTable.getInstance().getAvailableLevel(summon, skillId);

            Skill skill = SkillTable.getInstance().getInfo(skillId, skillLevel);
            if (skill == null)
                continue;

            Creature aimingTarget = skill.getAimingTarget(summon, activeChar.getTarget());
            if (skill.checkCondition(summon, aimingTarget, _ctrlPressed, _shiftPressed, true))
                summon.getAI().Cast(skill, aimingTarget, _ctrlPressed, _shiftPressed);
        }
    }

    static class SocialTask extends RunnableImpl {
        Player _player;

        SocialTask(Player player) {
            _player = player;
        }

        @Override
        public void runImpl() throws Exception {
            _player.stopParalyzed();
        }
    }
}
