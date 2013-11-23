/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets.components;

import l2p.gameserver.network.serverpackets.SystemMessage;

public class SystemMessageId {
    public static final SystemMessage YOU_HAVE_BEEN_DISCONNECTED_FROM_THE_SERVER = new SystemMessage(0);

    public static final SystemMessage THE_SERVER_WILL_BE_COMING_DOWN_IN_S1_SECONDS__PLEASE_FIND_A_SAFE_PLACE_TO_LOG_OUT = new SystemMessage(1);

    public static final SystemMessage S1_DOES_NOT_EXIST = new SystemMessage(2);

    public static final SystemMessage S1_IS_NOT_CURRENTLY_LOGGED_IN = new SystemMessage(3);

    public static final SystemMessage YOU_CANNOT_ASK_YOURSELF_TO_APPLY_TO_A_CLAN = new SystemMessage(4);

    public static final SystemMessage S1_ALREADY_EXISTS = new SystemMessage(5);

    public static final SystemMessage S1_DOES_NOT_EXIST_2 = new SystemMessage(6);

    public static final SystemMessage YOU_ARE_ALREADY_A_MEMBER_OF_S1 = new SystemMessage(7);

    public static final SystemMessage YOU_ARE_WORKING_WITH_ANOTHER_CLAN = new SystemMessage(8);

    public static final SystemMessage S1_IS_NOT_A_CLAN_LEADER = new SystemMessage(9);

    public static final SystemMessage S1_IS_WORKING_WITH_ANOTHER_CLAN = new SystemMessage(10);

    public static final SystemMessage THERE_ARE_NO_APPLICANTS_FOR_THIS_CLAN = new SystemMessage(11);

    public static final SystemMessage APPLICANT_INFORMATION_IS_INCORRECT = new SystemMessage(12);

    public static final SystemMessage UNABLE_TO_DISPERSE_YOUR_CLAN_HAS_REQUESTED_TO_PARTICIPATE_IN_A_CASTLE_SIEGE = new SystemMessage(13);

    public static final SystemMessage UNABLE_TO_DISPERSE_YOUR_CLAN_OWNS_ONE_OR_MORE_CASTLES_OR_HIDEOUTS = new SystemMessage(14);

    public static final SystemMessage YOU_ARE_IN_SIEGE = new SystemMessage(15);

    public static final SystemMessage CASTLE_SIEGE_HAS_BEGUN = new SystemMessage(17);

    public static final SystemMessage YOU_ARE_NOT_IN_SIEGE = new SystemMessage(16);

    public static final SystemMessage THERE_IS_A_NEW_LORD_OF_THE_CASTLE = new SystemMessage(19);

    public static final SystemMessage THE_CASTLE_SIEGE_HAS_ENDED = new SystemMessage(18);

    public static final SystemMessage THE_GATE_IS_BEING_DESTROYED = new SystemMessage(21);

    public static final SystemMessage THE_GATE_IS_BEING_OPENED = new SystemMessage(20);

    public static final SystemMessage NOT_ENOUGH_HP = new SystemMessage(23);

    public static final SystemMessage YOUR_TARGET_IS_OUT_OF_RANGE = new SystemMessage(22);

    public static final SystemMessage REJUVENATING_HP = new SystemMessage(25);

    public static final SystemMessage NOT_ENOUGH_MP = new SystemMessage(24);

    public static final SystemMessage YOUR_CASTING_HAS_BEEN_INTERRUPTED = new SystemMessage(27);

    public static final SystemMessage REJUVENATING_MP = new SystemMessage(26);

    public static final SystemMessage YOU_HAVE_OBTAINED_S2_S1 = new SystemMessage(29);

    public static final SystemMessage YOU_HAVE_OBTAINED_S1_ADENA = new SystemMessage(28);

    public static final SystemMessage YOU_CANNOT_MOVE_WHILE_SITTING = new SystemMessage(31);

    public static final SystemMessage YOU_HAVE_OBTAINED_S1 = new SystemMessage(30);

    public static final SystemMessage WELCOME_TO_THE_WORLD_OF_LINEAGE_II = new SystemMessage(34);

    public static final SystemMessage YOU_HIT_FOR_S1_DAMAGE = new SystemMessage(35);

    public static final SystemMessage YOU_ARE_NOT_CAPABLE_OF_COMBAT_MOVE_TO_THE_NEAREST_RESTART_POINT = new SystemMessage(32);

    public static final SystemMessage YOU_CANNOT_MOVE_WHILE_CASTING = new SystemMessage(33);

    public static final SystemMessage THE_TGS2002_EVENT_BEGINS = new SystemMessage(38);

    public static final SystemMessage THE_TGS2002_EVENT_IS_OVER_THANK_YOU_VERY_MUCH = new SystemMessage(39);

    public static final SystemMessage C1_HIT_YOU_FOR_S2_DAMAGE = new SystemMessage(36);

    public static final SystemMessage C1_HIT_YOU_FOR_S2_DAMAGE_2 = new SystemMessage(37);

    public static final SystemMessage YOU_HAVE_AVOIDED_C1S_ATTACK = new SystemMessage(42);

    public static final SystemMessage YOU_HAVE_MISSED = new SystemMessage(43);

    public static final SystemMessage THIS_IS_THE_TGS_DEMO_THE_CHARACTER_WILL_IMMEDIATELY_BE_RESTORED = new SystemMessage(40);

    public static final SystemMessage YOU_CAREFULLY_NOCK_AN_ARROW = new SystemMessage(41);

    public static final SystemMessage YOU_USE_S1 = new SystemMessage(46);

    public static final SystemMessage YOU_BEGIN_TO_USE_AN_S1 = new SystemMessage(47);

    public static final SystemMessage CRITICAL_HIT = new SystemMessage(44);

    public static final SystemMessage YOU_HAVE_EARNED_S1_EXPERIENCE = new SystemMessage(45);

    public static final SystemMessage YOU_CANNOT_USE_THIS_ON_YOURSELF = new SystemMessage(51);

    public static final SystemMessage YOUR_TARGET_CANNOT_BE_FOUND = new SystemMessage(50);

    public static final SystemMessage YOU_HAVE_EQUIPPED_YOUR_S1 = new SystemMessage(49);

    public static final SystemMessage S1_IS_NOT_AVAILABLE_AT_THIS_TIME_BEING_PREPARED_FOR_REUSE = new SystemMessage(48);

    public static final SystemMessage YOU_HAVE_FAILED_TO_PICK_UP_S1_ADENA = new SystemMessage(55);

    public static final SystemMessage YOU_HAVE_EARNED_S1 = new SystemMessage(54);

    public static final SystemMessage YOU_HAVE_EARNED_S2_S1S = new SystemMessage(53);

    public static final SystemMessage YOU_HAVE_EARNED_S1_ADENA = new SystemMessage(52);

    public static final SystemMessage YOU_HAVE_FAILED_TO_EARN_S1 = new SystemMessage(59);

    public static final SystemMessage YOU_HAVE_FAILED_TO_EARN_S1_ADENA = new SystemMessage(58);

    public static final SystemMessage YOU_HAVE_FAILED_TO_PICK_UP_S2_S1S = new SystemMessage(57);

    public static final SystemMessage YOU_HAVE_FAILED_TO_PICK_UP_S1 = new SystemMessage(56);

    public static final SystemMessage _S1_S2_HAS_BEEN_SUCCESSFULLY_ENCHANTED = new SystemMessage(63);

    public static final SystemMessage S1_HAS_BEEN_SUCCESSFULLY_ENCHANTED = new SystemMessage(62);

    public static final SystemMessage NOTHING_HAPPENED = new SystemMessage(61);

    public static final SystemMessage YOU_HAVE_FAILED_TO_EARN_S2_S1S = new SystemMessage(60);

    public static final SystemMessage WOULD_YOU_LIKE_TO_WITHDRAW_FROM_THE_S1_CLAN_IF_YOU_LEAVE_YOU_WILL_HAVE_TO_WAIT_AT_LEAST_A_DAY_BEFORE_JOINING_ANOTHER_CLAN = new SystemMessage(68);

    public static final SystemMessage WOULD_YOU_LIKE_TO_DISMISS_S1_FROM_THE_CLAN_IF_YOU_DO_SO_YOU_WILL_HAVE_TO_WAIT_AT_LEAST_A_DAY_BEFORE_ACCEPTING_A_NEW_MEMBER = new SystemMessage(69);

    public static final SystemMessage DO_YOU_WISH_TO_DISPERSE_THE_CLAN_S1 = new SystemMessage(70);

    public static final SystemMessage HOW_MANY_S1_S_DO_YOU_WANT_TO_DISCARD = new SystemMessage(71);

    public static final SystemMessage THE_ENCHANTMENT_HAS_FAILED_YOUR_S1_HAS_BEEN_CRYSTALLIZED = new SystemMessage(64);

    public static final SystemMessage THE_ENCHANTMENT_HAS_FAILED_YOUR__S1_S2_HAS_BEEN_CRYSTALLIZED = new SystemMessage(65);

    public static final SystemMessage C1_IS_INVITING_YOU_TO_JOIN_A_PARTY_DO_YOU_ACCEPT = new SystemMessage(66);

    public static final SystemMessage S1_HAS_INVITED_YOU_TO_THE_JOIN_THE_CLAN_S2_DO_YOU_WISH_TO_JOIN = new SystemMessage(67);

    public static final SystemMessage INCORRECT_PASSWORD = new SystemMessage(76);

    public static final SystemMessage YOU_CANNOT_CREATE_ANOTHER_CHARACTER_PLEASE_DELETE_THE_EXISTING_CHARACTER_AND_TRY_AGAIN = new SystemMessage(77);

    public static final SystemMessage DO_YOU_WISH_TO_DELETE_S1 = new SystemMessage(78);

    public static final SystemMessage THIS_NAME_ALREADY_EXISTS = new SystemMessage(79);

    public static final SystemMessage HOW_MANY_S1_S_DO_YOU_WANT_TO_MOVE = new SystemMessage(72);

    public static final SystemMessage HOW_MANY_S1_S_DO_YOU_WANT_TO_DESTROY = new SystemMessage(73);

    public static final SystemMessage DO_YOU_WISH_TO_DESTROY_YOUR_S1 = new SystemMessage(74);

    public static final SystemMessage ID_DOES_NOT_EXIST = new SystemMessage(75);

    public static final SystemMessage YOU_MAY_NOT_ATTACK_THIS_TARGET_IN_A_PEACEFUL_ZONE = new SystemMessage(85);

    public static final SystemMessage YOU_MAY_NOT_ATTACK_IN_A_PEACEFUL_ZONE = new SystemMessage(84);

    public static final SystemMessage PLEASE_ENTER_YOUR_PASSWORD = new SystemMessage(87);

    public static final SystemMessage PLEASE_ENTER_YOUR_ID = new SystemMessage(86);

    public static final SystemMessage PLEASE_SELECT_YOUR_RACE = new SystemMessage(81);

    public static final SystemMessage YOUR_TITLE_CANNOT_EXCEED_16_CHARACTERS_IN_LENGTH_PLEASE_TRY_AGAIN = new SystemMessage(80);

    public static final SystemMessage PLEASE_SELECT_YOUR_GENDER = new SystemMessage(83);

    public static final SystemMessage PLEASE_SELECT_YOUR_OCCUPATION = new SystemMessage(82);

    public static final SystemMessage YOU_DO_NOT_HAVE_ENOUGH_SP_FOR_THIS = new SystemMessage(93);

    public static final SystemMessage S1_HAS_WORN_OFF = new SystemMessage(92);

    public static final SystemMessage YOU_HAVE_EARNED_S1_EXPERIENCE_AND_S2_SP = new SystemMessage(95);

    public static final SystemMessage COPYRIGHT_NCSOFT_CORPORATION_ALL_RIGHTS_RESERVED = new SystemMessage(94);

    public static final SystemMessage YOUR_PROTOCOL_VERSION_IS_DIFFERENT_PLEASE_CONTINUE = new SystemMessage(89);

    public static final SystemMessage YOUR_PROTOCOL_VERSION_IS_DIFFERENT_PLEASE_RESTART_YOUR_CLIENT_AND_RUN_A_FULL_CHECK = new SystemMessage(88);

    public static final SystemMessage PLEASE_SELECT_YOUR_HAIRSTYLE = new SystemMessage(91);

    public static final SystemMessage YOU_ARE_UNABLE_TO_CONNECT_TO_THE_SERVER = new SystemMessage(90);

    public static final SystemMessage YOU_CANNOT_RESTART_WHILE_IN_COMBAT = new SystemMessage(102);

    public static final SystemMessage THIS_ID_IS_CURRENTLY_LOGGED_IN = new SystemMessage(103);

    public static final SystemMessage S1_REQUESTS_A_TRADE_DO_YOU_WANT_TO_TRADE = new SystemMessage(100);

    public static final SystemMessage YOU_CANNOT_EXIT_WHILE_IN_COMBAT = new SystemMessage(101);

    public static final SystemMessage THIS_ITEM_CANNOT_BE_DISCARDED = new SystemMessage(98);

    public static final SystemMessage THIS_ITEM_CANNOT_BE_TRADED_OR_SOLD = new SystemMessage(99);

    public static final SystemMessage YOUR_LEVEL_HAS_INCREASED = new SystemMessage(96);

    public static final SystemMessage THIS_ITEM_CANNOT_BE_MOVED = new SystemMessage(97);

    public static final SystemMessage S1_S2S_EFFECT_CAN_BE_FELT = new SystemMessage(110);

    public static final SystemMessage YOUR_SHIELD_DEFENSE_HAS_SUCCEEDED = new SystemMessage(111);

    public static final SystemMessage S1_HAS_LEFT_THE_PARTY = new SystemMessage(108);

    public static final SystemMessage INVALID_TARGET = new SystemMessage(109);

    public static final SystemMessage YOU_HAVE_JOINED_S1S_PARTY = new SystemMessage(106);

    public static final SystemMessage S1_HAS_JOINED_THE_PARTY = new SystemMessage(107);

    public static final SystemMessage YOU_MAY_NOT_EQUIP_ITEMS_WHILE_CASTING_OR_PERFORMING_A_SKILL = new SystemMessage(104);

    public static final SystemMessage YOU_HAVE_INVITED_C1_TO_JOIN_YOUR_PARTY = new SystemMessage(105);

    public static final SystemMessage C1_HAS_DENIED_YOUR_REQUEST_TO_TRADE = new SystemMessage(119);

    public static final SystemMessage YOU_HAVE_REQUESTED_A_TRADE_WITH_C1 = new SystemMessage(118);

    public static final SystemMessage YOU_HAVE_LEFT_THE_PEACEFUL_ZONE = new SystemMessage(117);

    public static final SystemMessage YOU_HAVE_ENTERED_A_PEACEFUL_ZONE = new SystemMessage(116);

    public static final SystemMessage YOU_HAVE_LEFT_THE_SHADOW_OF_THE_MOTHER_TREE = new SystemMessage(115);

    public static final SystemMessage YOU_HAVE_ENTERED_THE_SHADOW_OF_THE_MOTHER_TREE = new SystemMessage(114);

    public static final SystemMessage S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS = new SystemMessage(113);

    public static final SystemMessage YOU_HAVE_RUN_OUT_OF_ARROWS = new SystemMessage(112);

    public static final SystemMessage YOU_HAVE_BEEN_DISCONNECTED_FROM_THE_SERVER_PLEASE_LOGIN_AGAIN = new SystemMessage(127);

    public static final SystemMessage DO_YOU_WISH_TO_EXIT_TO_THE_CHARACTER_SELECT_SCREEN = new SystemMessage(126);

    public static final SystemMessage DO_YOU_WISH_TO_EXIT_THE_GAME = new SystemMessage(125);

    public static final SystemMessage C1_HAS_CANCELLED_THE_TRADE = new SystemMessage(124);

    public static final SystemMessage YOUR_TRADE_IS_SUCCESSFUL = new SystemMessage(123);

    public static final SystemMessage YOU_MAY_NO_LONGER_ADJUST_ITEMS_IN_THE_TRADE_BECAUSE_THE_TRADE_HAS_BEEN_CONFIRMED = new SystemMessage(122);

    public static final SystemMessage C1_HAS_CONFIRMED_THE_TRADE = new SystemMessage(121);

    public static final SystemMessage YOU_BEGIN_TRADING_WITH_C1 = new SystemMessage(120);

    public static final SystemMessage THERE_ARE_NO_MORE_ITEMS_IN_THE_SHORTCUT = new SystemMessage(137);

    public static final SystemMessage YOU_HAVE_NOT_REPLIED_TO_C1S_INVITATION_THE_OFFER_HAS_BEEN_CANCELLED = new SystemMessage(136);

    public static final SystemMessage C1_HAS_RESISTED_YOUR_S2 = new SystemMessage(139);

    public static final SystemMessage DESIGNATE_SHORTCUT = new SystemMessage(138);

    public static final SystemMessage ONCE_THE_TRADE_IS_CONFIRMED_THE_ITEM_CANNOT_BE_MOVED_AGAIN = new SystemMessage(141);

    public static final SystemMessage YOUR_SKILL_WAS_REMOVED_DUE_TO_A_LACK_OF_MP = new SystemMessage(140);

    public static final SystemMessage C1_IS_ALREADY_TRADING_WITH_ANOTHER_PERSON_PLEASE_TRY_AGAIN_LATER = new SystemMessage(143);

    public static final SystemMessage YOU_ARE_ALREADY_TRADING_WITH_SOMEONE = new SystemMessage(142);

    public static final SystemMessage YOUR_INVENTORY_IS_FULL = new SystemMessage(129);

    public static final SystemMessage YOUR_CHARACTER_CREATION_HAS_FAILED = new SystemMessage(128);

    public static final SystemMessage S1_HAS_LOGGED_IN = new SystemMessage(131);

    public static final SystemMessage YOUR_WAREHOUSE_IS_FULL = new SystemMessage(130);

    public static final SystemMessage S1_HAS_BEEN_REMOVED_FROM_YOUR_FRIEND_LIST = new SystemMessage(133);

    public static final SystemMessage S1_HAS_BEEN_ADDED_TO_YOUR_FRIEND_LIST = new SystemMessage(132);

    public static final SystemMessage S1_DID_NOT_REPLY_TO_YOUR_INVITATION_PARTY_INVITATION_HAS_BEEN_CANCELLED = new SystemMessage(135);

    public static final SystemMessage PLEASE_CHECK_YOUR_FRIEND_LIST_AGAIN = new SystemMessage(134);

    public static final SystemMessage YOU_HAVE_INVITED_WRONG_TARGET = new SystemMessage(152);

    public static final SystemMessage S1_IS_BUSY_PLEASE_TRY_AGAIN_LATER = new SystemMessage(153);

    public static final SystemMessage ONLY_THE_LEADER_CAN_GIVE_OUT_INVITATIONS = new SystemMessage(154);

    public static final SystemMessage PARTY_IS_FULL = new SystemMessage(155);

    public static final SystemMessage DRAIN_WAS_ONLY_HALF_SUCCESSFUL = new SystemMessage(156);

    public static final SystemMessage YOU_RESISTED_S1S_DRAIN = new SystemMessage(157);

    public static final SystemMessage ATTACK_FAILED = new SystemMessage(158);

    public static final SystemMessage RESISTED_AGAINST_S1S_MAGIC = new SystemMessage(159);

    public static final SystemMessage THAT_IS_THE_INCORRECT_TARGET = new SystemMessage(144);

    public static final SystemMessage THAT_PLAYER_IS_NOT_ONLINE = new SystemMessage(145);

    public static final SystemMessage CHATTING_IS_NOW_PERMITTED = new SystemMessage(146);

    public static final SystemMessage CHATTING_IS_CURRENTLY_PROHIBITED = new SystemMessage(147);

    public static final SystemMessage YOU_CANNOT_USE_QUEST_ITEMS = new SystemMessage(148);

    public static final SystemMessage YOU_CANNOT_PICK_UP_OR_USE_ITEMS_WHILE_TRADING = new SystemMessage(149);

    public static final SystemMessage YOU_CANNOT_DISCARD_OR_DESTROY_AN_ITEM_WHILE_TRADING_AT_A_PRIVATE_STORE = new SystemMessage(150);

    public static final SystemMessage THAT_IS_TOO_FAR_FROM_YOU_TO_DISCARD = new SystemMessage(151);

    public static final SystemMessage S1_IS_NOT_ON_YOUR_FRIEND_LIST = new SystemMessage(171);

    public static final SystemMessage THE_USER_WHO_REQUESTED_TO_BECOME_FRIENDS_IS_NOT_FOUND_IN_THE_GAME = new SystemMessage(170);

    public static final SystemMessage ACCEPT_FRIENDSHIP_0_1__1_TO_ACCEPT_0_TO_DENY = new SystemMessage(169);

    public static final SystemMessage S1_HAS_REQUESTED_TO_BECOME_FRIENDS = new SystemMessage(168);

    public static final SystemMessage HP_WAS_FULLY_RECOVERED_AND_SKILL_WAS_REMOVED = new SystemMessage(175);

    public static final SystemMessage THE_PERSONS_INVENTORY_IS_FULL = new SystemMessage(174);

    public static final SystemMessage YOU_LACK_THE_FUNDS_NEEDED_TO_PAY_FOR_THIS_TRANSACTION_2 = new SystemMessage(173);

    public static final SystemMessage YOU_LACK_THE_FUNDS_NEEDED_TO_PAY_FOR_THIS_TRANSACTION = new SystemMessage(172);

    public static final SystemMessage YOU_CANNOT_DESTROY_IT_BECAUSE_THE_NUMBER_IS_INCORRECT = new SystemMessage(163);

    public static final SystemMessage WAREHOUSE_IS_TOO_FAR = new SystemMessage(162);

    public static final SystemMessage THAT_PLAYER_IS_NOT_CURRENTLY_ONLINE = new SystemMessage(161);

    public static final SystemMessage S1_IS_A_MEMBER_OF_ANOTHER_PARTY_AND_CANNOT_BE_INVITED = new SystemMessage(160);

    public static final SystemMessage S1_IS_ALREADY_ON_YOUR_FRIEND_LIST = new SystemMessage(167);

    public static final SystemMessage FRIEND_LIST_IS_NOT_READY_YET_PLEASE_REGISTER_AGAIN_LATER = new SystemMessage(166);

    public static final SystemMessage YOU_CANNOT_ADD_YOURSELF_TO_YOUR_OWN_FRIEND_LIST = new SystemMessage(165);

    public static final SystemMessage WAITING_FOR_ANOTHER_REPLY = new SystemMessage(164);

    public static final SystemMessage SELECT_USER_TO_INVITE_TO_YOUR_CLAN = new SystemMessage(186);

    public static final SystemMessage SELECT_USER_TO_EXPEL = new SystemMessage(187);

    public static final SystemMessage PLEASE_TRY_AGAIN_LATER = new SystemMessage(184);

    public static final SystemMessage SELECT_USER_TO_INVITE_TO_YOUR_PARTY = new SystemMessage(185);

    public static final SystemMessage YOU_HAVE_FAILED_TO_CREATE_A_CLAN = new SystemMessage(190);

    public static final SystemMessage CLAN_MEMBER_S1_HAS_BEEN_EXPELLED = new SystemMessage(191);

    public static final SystemMessage CREATE_CLAN_NAME = new SystemMessage(188);

    public static final SystemMessage CLAN_HAS_BEEN_CREATED = new SystemMessage(189);

    public static final SystemMessage MESSAGE_ACCEPTANCE_MODE = new SystemMessage(178);

    public static final SystemMessage YOU_CANNOT_DISCARD_THOSE_ITEMS_HERE = new SystemMessage(179);

    public static final SystemMessage THE_PERSON_IS_IN_A_MESSAGE_REFUSAL_MODE = new SystemMessage(176);

    public static final SystemMessage MESSAGE_REFUSAL_MODE = new SystemMessage(177);

    public static final SystemMessage DO_YOU_WANT_TO_QUIT_THE_CURRENT_QUEST = new SystemMessage(182);

    public static final SystemMessage THERE_ARE_TOO_MANY_USERS_ON_THE_SERVER_PLEASE_TRY_AGAIN_LATER = new SystemMessage(183);

    public static final SystemMessage YOU_HAVE_S1_DAY_S_LEFT_UNTIL_DELETION_DO_YOU_WANT_TO_CANCEL_DELETION = new SystemMessage(180);

    public static final SystemMessage CANNOT_SEE_TARGET = new SystemMessage(181);

    public static final SystemMessage INCORRECT_CHARACTER_NAME_PLEASE_ASK_THE_GM = new SystemMessage(205);

    public static final SystemMessage INCORRECT_NAME_PLEASE_TRY_AGAIN = new SystemMessage(204);

    public static final SystemMessage S2_OF_THE_S1_CLAN_REQUESTS_DECLARATION_OF_WAR_DO_YOU_ACCEPT = new SystemMessage(207);

    public static final SystemMessage ENTER_NAME_OF_CLAN_TO_DECLARE_WAR_ON = new SystemMessage(206);

    public static final SystemMessage S1_WAS_EXPELLED_FROM_THE_PARTY = new SystemMessage(201);

    public static final SystemMessage YOU_HAVE_WITHDRAWN_FROM_THE_PARTY = new SystemMessage(200);

    public static final SystemMessage THE_PARTY_HAS_DISPERSED = new SystemMessage(203);

    public static final SystemMessage YOU_HAVE_BEEN_EXPELLED_FROM_THE_PARTY = new SystemMessage(202);

    public static final SystemMessage WITHDRAWN_FROM_THE_CLAN = new SystemMessage(197);

    public static final SystemMessage S1_REFUSED_TO_JOIN_THE_CLAN = new SystemMessage(196);

    public static final SystemMessage YOU_HAVE_RECENTLY_BEEN_DISMISSED_FROM_A_CLAN_YOU_ARE_NOT_ALLOWED_TO_JOIN_ANOTHER_CLAN_FOR_24_HOURS = new SystemMessage(199);

    public static final SystemMessage YOU_HAVE_FAILED_TO_WITHDRAW_FROM_THE_S1_CLAN = new SystemMessage(198);

    public static final SystemMessage CLAN_HAS_DISPERSED = new SystemMessage(193);

    public static final SystemMessage YOU_HAVE_FAILED_TO_EXPEL_S1_FROM_THE_CLAN = new SystemMessage(192);

    public static final SystemMessage ENTERED_THE_CLAN = new SystemMessage(195);

    public static final SystemMessage YOU_HAVE_FAILED_TO_DISPERSE_THE_CLAN = new SystemMessage(194);

    public static final SystemMessage YOU_HAVE_S1_MINUTES_LEFT_UNTIL_THE_CLAN_WAR_ENDS = new SystemMessage(220);

    public static final SystemMessage THE_TIME_LIMIT_FOR_THE_CLAN_WAR_IS_UPWAR_WITH_THE_S1_CLAN_IS_OVER = new SystemMessage(221);

    public static final SystemMessage S1_HAS_JOINED_THE_CLAN = new SystemMessage(222);

    public static final SystemMessage S1_HAS_WITHDRAWN_FROM_THE_CLAN = new SystemMessage(223);

    public static final SystemMessage WAR_WITH_THE_S1_CLAN_HAS_ENDED = new SystemMessage(216);

    public static final SystemMessage YOU_HAVE_WON_THE_WAR_OVER_THE_S1_CLAN = new SystemMessage(217);

    public static final SystemMessage YOU_HAVE_SURRENDERED_TO_THE_S1_CLAN = new SystemMessage(218);

    public static final SystemMessage YOUR_CLAN_LEADER_HAS_DIEDYOU_HAVE_BEEN_DEFEATED_BY_THE_S1_CLAN = new SystemMessage(219);

    public static final SystemMessage YOU_ARE_NOT_A_CLAN_MEMBER = new SystemMessage(212);

    public static final SystemMessage NOT_WORKING_PLEASE_TRY_AGAIN_LATER = new SystemMessage(213);

    public static final SystemMessage TITLE_HAS_CHANGED = new SystemMessage(214);

    public static final SystemMessage WAR_WITH_THE_S1_CLAN_HAS_BEGUN = new SystemMessage(215);

    public static final SystemMessage PLEASE_INCLUDE_FILE_TYPE_WHEN_ENTERING_FILE_PATH = new SystemMessage(208);

    public static final SystemMessage THE_SIZE_OF_THE_IMAGE_FILE_IS_DIFFERENT_PLEASE_ADJUST_TO_16_12 = new SystemMessage(209);

    public static final SystemMessage CANNOT_FIND_FILE_PLEASE_ENTER_PRECISE_PATH = new SystemMessage(210);

    public static final SystemMessage CAN_ONLY_REGISTER_16_12_SIZED_BMP_FILES_OF_256_COLORS = new SystemMessage(211);

    public static final SystemMessage THE_CLAN_LEADER_CANNOT_WITHDRAW = new SystemMessage(239);

    public static final SystemMessage NOT_JOINED_IN_ANY_CLAN = new SystemMessage(238);

    public static final SystemMessage CANNOT_FIND_CLAN_LEADER = new SystemMessage(237);

    public static final SystemMessage ONLY_THE_CLAN_LEADER_IS_ENABLED = new SystemMessage(236);

    public static final SystemMessage YOU_CANNOT_TRANSFER_YOUR_RIGHTS = new SystemMessage(235);

    public static final SystemMessage THE_TARGET_MUST_BE_A_CLAN_MEMBER = new SystemMessage(234);

    public static final SystemMessage THE_ACADEMY_ROYAL_GUARD_ORDER_OF_KNIGHTS_IS_FULL_AND_CANNOT_ACCEPT_NEW_MEMBERS_AT_THIS_TIME = new SystemMessage(233);

    public static final SystemMessage AFTER_LEAVING_OR_HAVING_BEEN_DISMISSED_FROM_A_CLAN_YOU_MUST_WAIT_AT_LEAST_A_DAY_BEFORE_JOINING_ANOTHER_CLAN = new SystemMessage(232);

    public static final SystemMessage AFTER_A_CLAN_MEMBER_IS_DISMISSED_FROM_A_CLAN_THE_CLAN_MUST_WAIT_AT_LEAST_A_DAY_BEFORE_ACCEPTING_A_NEW_MEMBER = new SystemMessage(231);

    public static final SystemMessage YOU_MUST_WAIT_10_DAYS_BEFORE_CREATING_A_NEW_CLAN = new SystemMessage(230);

    public static final SystemMessage YOU_ARE_NOT_QUALIFIED_TO_CREATE_A_CLAN = new SystemMessage(229);

    public static final SystemMessage REQUEST_TO_END_WAR_HAS_BEEN_DENIED = new SystemMessage(228);

    public static final SystemMessage CLAN_WAR_HAS_BEEN_REFUSED_BECAUSE_YOU_DID_NOT_RESPOND_TO_S1_CLANS_WAR_PROCLAMATION = new SystemMessage(227);

    public static final SystemMessage THE_S1_CLAN_DID_NOT_RESPOND_WAR_PROCLAMATION_HAS_BEEN_REFUSED = new SystemMessage(226);

    public static final SystemMessage YOU_DIDNT_RESPOND_TO_S1S_INVITATION_JOINING_HAS_BEEN_CANCELLED = new SystemMessage(225);

    public static final SystemMessage S1_DID_NOT_RESPOND_INVITATION_TO_THE_CLAN_HAS_BEEN_CANCELLED = new SystemMessage(224);

    public static final SystemMessage CLAN_LEADER_CANNOT_SURRENDER_PERSONALLY = new SystemMessage(254);

    public static final SystemMessage THE_S1_CLAN_HAS_REQUESTED_TO_END_WAR_DO_YOU_AGREE = new SystemMessage(255);

    public static final SystemMessage ENTER_THE_NAME_OF_CLAN_TO_SURRENDER_TO = new SystemMessage(252);

    public static final SystemMessage ENTER_THE_NAME_OF_CLAN_TO_REQUEST_END_OF_WAR = new SystemMessage(253);

    public static final SystemMessage YOU_HAVE_PERSONALLY_SURRENDERED_TO_THE_S1_CLAN_YOU_ARE_LEAVING_THE_CLAN_WAR = new SystemMessage(250);

    public static final SystemMessage YOU_CANNOT_PROCLAIM_WAR_YOU_ARE_AT_WAR_WITH_ANOTHER_CLAN = new SystemMessage(251);

    public static final SystemMessage YOU_CANNOT_PROCLAIM_WAR_THE_S1_CLAN_DOES_NOT_HAVE_ENOUGH_MEMBERS = new SystemMessage(248);

    public static final SystemMessage DO_YOU_WISH_TO_SURRENDER_TO_THE_S1_CLAN = new SystemMessage(249);

    public static final SystemMessage THE_OTHER_CLAN_IS_CURRENTLY_AT_WAR = new SystemMessage(246);

    public static final SystemMessage YOU_HAVE_ALREADY_BEEN_AT_WAR_WITH_THE_S1_CLAN_5_DAYS_MUST_PASS_BEFORE_YOU_CAN_PROCLAIM_WAR_AGAIN = new SystemMessage(247);

    public static final SystemMessage UNQUALIFIED_TO_REQUEST_DECLARATION_OF_CLAN_WAR = new SystemMessage(244);

    public static final SystemMessage _5_DAYS_HAS_NOT_PASSED_SINCE_YOU_WERE_REFUSED_WAR_DO_YOU_WANT_TO_CONTINUE = new SystemMessage(245);

    public static final SystemMessage SELECT_TARGET = new SystemMessage(242);

    public static final SystemMessage CANNOT_PROCLAIM_WAR_ON_ALLIED_CLANS = new SystemMessage(243);

    public static final SystemMessage CURRENTLY_INVOLVED_IN_CLAN_WAR = new SystemMessage(240);

    public static final SystemMessage LEADER_OF_THE_S1_CLAN_IS_NOT_LOGGED_IN = new SystemMessage(241);

    public static final SystemMessage CLAN_HAS_FAILED_TO_INCREASE_SKILL_LEVEL = new SystemMessage(275);

    public static final SystemMessage CLANS_SKILL_LEVEL_HAS_INCREASED = new SystemMessage(274);

    public static final SystemMessage PROCLAMATION_OF_CLAN_WAR_IS_ONLY_POSSIBLE_WHEN_CLANS_SKILL_LEVELS_ARE_ABOVE_3 = new SystemMessage(273);

    public static final SystemMessage CLAN_CREST_REGISTRATION_IS_ONLY_POSSIBLE_WHEN_CLANS_SKILL_LEVELS_ARE_ABOVE_3 = new SystemMessage(272);

    public static final SystemMessage YOU_DO_NOT_HAVE_ENOUGH_ADENA = new SystemMessage(279);

    public static final SystemMessage YOU_DO_NOT_HAVE_ENOUGH_SP_TO_LEARN_SKILLS = new SystemMessage(278);

    public static final SystemMessage YOU_HAVE_EARNED_S1_2 = new SystemMessage(277);

    public static final SystemMessage YOU_DO_NOT_HAVE_ENOUGH_ITEMS_TO_LEARN_SKILLS = new SystemMessage(276);

    public static final SystemMessage YOU_HAVE_ENTERED_A_COMBAT_ZONE = new SystemMessage(283);

    public static final SystemMessage YOU_HAVE_NOT_DEPOSITED_ANY_ITEMS_IN_YOUR_WAREHOUSE = new SystemMessage(282);

    public static final SystemMessage YOU_DO_NOT_HAVE_ENOUGH_CUSTODY_FEES = new SystemMessage(281);

    public static final SystemMessage YOU_DO_NOT_HAVE_ANY_ITEMS_TO_SELL = new SystemMessage(280);

    public static final SystemMessage THE_OPPONENT_CLAN_HAS_BEGUN_TO_ENGRAVE_THE_RULER = new SystemMessage(287);

    public static final SystemMessage YOUR_BASE_IS_BEING_ATTACKED = new SystemMessage(286);

    public static final SystemMessage CLAN_S1_HAS_SUCCEEDED_IN_ENGRAVING_THE_RULER = new SystemMessage(285);

    public static final SystemMessage YOU_HAVE_LEFT_A_COMBAT_ZONE = new SystemMessage(284);

    public static final SystemMessage NOT_INVOLVED_IN_CLAN_WAR = new SystemMessage(258);

    public static final SystemMessage SELECT_CLAN_MEMBERS_FROM_LIST = new SystemMessage(259);

    public static final SystemMessage ENTER_NAME = new SystemMessage(256);

    public static final SystemMessage DO_YOU_PROPOSE_TO_THE_S1_CLAN_TO_END_THE_WAR = new SystemMessage(257);

    public static final SystemMessage CLAN_NAMES_LENGTH_IS_INCORRECT = new SystemMessage(262);

    public static final SystemMessage DISPERSION_HAS_ALREADY_BEEN_REQUESTED = new SystemMessage(263);

    public static final SystemMessage FAME_LEVEL_HAS_DECREASED_5_DAYS_HAVE_NOT_PASSED_SINCE_YOU_WERE_REFUSED_WAR = new SystemMessage(260);

    public static final SystemMessage CLAN_NAME_IS_INCORRECT = new SystemMessage(261);

    public static final SystemMessage YOU_CANNOT_DISSOLVE_A_CLAN_WHILE_OWNING_A_CLAN_HALL_OR_CASTLE = new SystemMessage(266);

    public static final SystemMessage NO_REQUESTS_FOR_DISPERSION = new SystemMessage(267);

    public static final SystemMessage YOU_CANNOT_DISSOLVE_A_CLAN_WHILE_ENGAGED_IN_A_WAR = new SystemMessage(264);

    public static final SystemMessage YOU_CANNOT_DISSOLVE_A_CLAN_DURING_A_SIEGE_OR_WHILE_PROTECTING_A_CASTLE = new SystemMessage(265);

    public static final SystemMessage YOU_HAVE_ALREADY_SURRENDERED = new SystemMessage(270);

    public static final SystemMessage TITLE_ENDOWMENT_IS_ONLY_POSSIBLE_WHEN_CLANS_SKILL_LEVELS_ARE_ABOVE_3 = new SystemMessage(271);

    public static final SystemMessage PLAYER_ALREADY_BELONGS_TO_A_CLAN = new SystemMessage(268);

    public static final SystemMessage YOU_CANNOT_EXPEL_YOURSELF = new SystemMessage(269);

    public static final SystemMessage THE_PLAYER_DECLINED_TO_JOIN_YOUR_PARTY = new SystemMessage(305);

    public static final SystemMessage CLAN_MEMBER_S1_HAS_LOGGED_INTO_GAME = new SystemMessage(304);

    public static final SystemMessage YOU_HAVE_FAILED_TO_TRADE_WITH_THE_WAREHOUSE = new SystemMessage(307);

    public static final SystemMessage YOU_HAVE_FAILED_TO_DELETE_THE_CHARACTER = new SystemMessage(306);

    public static final SystemMessage SUCCEEDED_IN_EXPELLING_A_CLAN_MEMBER = new SystemMessage(309);

    public static final SystemMessage FAILED_TO_JOIN_THE_CLAN = new SystemMessage(308);

    public static final SystemMessage CLAN_WAR_HAS_BEEN_ACCEPTED = new SystemMessage(311);

    public static final SystemMessage FAILED_TO_EXPEL_A_CLAN_MEMBER = new SystemMessage(310);

    public static final SystemMessage THE_CEASE_WAR_REQUEST_HAS_BEEN_ACCEPTED = new SystemMessage(313);

    public static final SystemMessage CLAN_WAR_HAS_BEEN_REFUSED = new SystemMessage(312);

    public static final SystemMessage FAILED_TO_PERSONALLY_SURRENDER = new SystemMessage(315);

    public static final SystemMessage FAILED_TO_SURRENDER = new SystemMessage(314);

    public static final SystemMessage FAILED_TO_EXPEL_A_PARTY_MEMBER = new SystemMessage(317);

    public static final SystemMessage FAILED_TO_WITHDRAW_FROM_THE_PARTY = new SystemMessage(316);

    public static final SystemMessage YOU_ARE_UNABLE_TO_UNLOCK_THE_DOOR = new SystemMessage(319);

    public static final SystemMessage FAILED_TO_DISPERSE_THE_PARTY = new SystemMessage(318);

    public static final SystemMessage THE_CASTLE_GATE_HAS_BEEN_BROKEN_DOWN = new SystemMessage(288);

    public static final SystemMessage SINCE_A_HEADQUARTERS_ALREADY_EXISTS_YOU_CANNOT_BUILD_ANOTHER_ONE = new SystemMessage(289);

    public static final SystemMessage YOU_CANNOT_SET_UP_A_BASE_HERE = new SystemMessage(290);

    public static final SystemMessage CLAN_S1_IS_VICTORIOUS_OVER_S2S_CASTLE_SIEGE = new SystemMessage(291);

    public static final SystemMessage S1_HAS_ANNOUNCED_THE_CASTLE_SIEGE_TIME = new SystemMessage(292);

    public static final SystemMessage THE_REGISTRATION_TERM_FOR_S1_HAS_ENDED = new SystemMessage(293);

    public static final SystemMessage YOU_CANNOT_SUMMON_A_BASE_BECAUSE_YOU_ARE_NOT_IN_BATTLE = new SystemMessage(294);

    public static final SystemMessage S1S_SIEGE_WAS_CANCELED_BECAUSE_THERE_WERE_NO_CLANS_THAT_PARTICIPATED = new SystemMessage(295);

    public static final SystemMessage YOU_RECEIVED_S1_DAMAGE_FROM_TAKING_A_HIGH_FALL = new SystemMessage(296);

    public static final SystemMessage YOU_RECEIVED_S1_DAMAGE_BECAUSE_YOU_WERE_UNABLE_TO_BREATHE = new SystemMessage(297);

    public static final SystemMessage YOU_HAVE_DROPPED_S1 = new SystemMessage(298);

    public static final SystemMessage S1_HAS_OBTAINED_S3_S2 = new SystemMessage(299);

    public static final SystemMessage S1_HAS_OBTAINED_S2 = new SystemMessage(300);

    public static final SystemMessage S2_S1_HAS_DISAPPEARED = new SystemMessage(301);

    public static final SystemMessage S1_HAS_DISAPPEARED = new SystemMessage(302);

    public static final SystemMessage SELECT_ITEM_TO_ENCHANT = new SystemMessage(303);

    public static final SystemMessage SWEEPER_FAILED_TARGET_NOT_SPOILED = new SystemMessage(343);

    public static final SystemMessage POWER_OF_THE_SPIRITS_ENABLED = new SystemMessage(342);

    public static final SystemMessage NOT_ENOUGH_MATERIALS = new SystemMessage(341);

    public static final SystemMessage PRIVATE_STORE_UNDER_WAY = new SystemMessage(340);

    public static final SystemMessage CANNOT_USE_SOULSHOTS = new SystemMessage(339);

    public static final SystemMessage NOT_ENOUGH_SOULSHOTS = new SystemMessage(338);

    public static final SystemMessage SOULSHOT_DOES_NOT_MATCH_WEAPON_GRADE = new SystemMessage(337);

    public static final SystemMessage S1_IS_CRYSTALLIZED_DO_YOU_WANT_TO_CONTINUE = new SystemMessage(336);

    public static final SystemMessage INCORRECT_ITEM_COUNT = new SystemMessage(351);

    public static final SystemMessage ITEM_OUT_OF_STOCK = new SystemMessage(350);

    public static final SystemMessage PRIVATE_STORE_ALREADY_CLOSED = new SystemMessage(349);

    public static final SystemMessage INCORRECT_ITEM_PRICE = new SystemMessage(348);

    public static final SystemMessage INCORRECT_ITEM_COUNT_0 = new SystemMessage(347);

    public static final SystemMessage CHAT_DISABLED = new SystemMessage(346);

    public static final SystemMessage CHAT_ENABLED = new SystemMessage(345);

    public static final SystemMessage POWER_OF_THE_SPIRITS_DISABLED = new SystemMessage(344);

    public static final SystemMessage SELECT_TARGET_FROM_LIST = new SystemMessage(326);

    public static final SystemMessage YOU_CANNOT_EXCEED_80_CHARACTERS = new SystemMessage(327);

    public static final SystemMessage YOUR_FORCE_HAS_REACHED_MAXIMUM_CAPACITY_ = new SystemMessage(324);

    public static final SystemMessage THE_CORPSE_HAS_ALREADY_DISAPPEARED = new SystemMessage(325);

    public static final SystemMessage PLEASE_DECIDE_ON_THE_SALES_PRICE = new SystemMessage(322);

    public static final SystemMessage YOUR_FORCE_HAS_INCREASED_TO_S1_LEVEL = new SystemMessage(323);

    public static final SystemMessage YOU_HAVE_FAILED_TO_UNLOCK_THE_DOOR = new SystemMessage(320);

    public static final SystemMessage IT_IS_NOT_LOCKED = new SystemMessage(321);

    public static final SystemMessage PLEASE_ENTER_STORE_MESSAGE = new SystemMessage(334);

    public static final SystemMessage S1_IS_ABORTED = new SystemMessage(335);

    public static final SystemMessage DO_YOU_WANT_TO_BE_RESTORED = new SystemMessage(332);

    public static final SystemMessage YOU_HAVE_RECEIVED_S1_DAMAGE_BY_CORES_BARRIER = new SystemMessage(333);

    public static final SystemMessage A_ONE_LINE_RESPONSE_MAY_NOT_EXCEED_128_CHARACTERS = new SystemMessage(330);

    public static final SystemMessage YOU_HAVE_ACQUIRED_S1_SP = new SystemMessage(331);

    public static final SystemMessage PLEASE_INPUT_TITLE_USING_LESS_THAN_128_CHARACTERS = new SystemMessage(328);

    public static final SystemMessage PLEASE_INPUT_CONTENTS_USING_LESS_THAN_3000_CHARACTERS = new SystemMessage(329);

    public static final SystemMessage DESTROY__S1_S2_DO_YOU_WISH_TO_CONTINUE = new SystemMessage(373);

    public static final SystemMessage FAILED_TO_EARN_S1 = new SystemMessage(372);

    public static final SystemMessage DROPPED__S1_S2 = new SystemMessage(375);

    public static final SystemMessage CRYSTALLIZE__S1_S2_DO_YOU_WISH_TO_CONTINUE = new SystemMessage(374);

    public static final SystemMessage YOU_HAVE_OBTAINED__S1S2 = new SystemMessage(369);

    public static final SystemMessage EQUIPPED__S1_S2 = new SystemMessage(368);

    public static final SystemMessage ACQUIRED__S1_S2 = new SystemMessage(371);

    public static final SystemMessage FAILED_TO_PICK_UP_S1 = new SystemMessage(370);

    public static final SystemMessage CANNOT_CONNECT_TO_PETITION_SERVER = new SystemMessage(381);

    public static final SystemMessage S1_PURCHASED_S3_S2_S = new SystemMessage(380);

    public static final SystemMessage REQUEST_CONFIRMED_TO_END_CONSULTATION_AT_PETITION_SERVER = new SystemMessage(383);

    public static final SystemMessage CURRENTLY_THERE_ARE_NO_USERS_THAT_HAVE_CHECKED_OUT_A_GM_ID = new SystemMessage(382);

    public static final SystemMessage _S1_S2_DISAPPEARED = new SystemMessage(377);

    public static final SystemMessage S1_HAS_OBTAINED__S2S3 = new SystemMessage(376);

    public static final SystemMessage S1_PURCHASED__S2_S3 = new SystemMessage(379);

    public static final SystemMessage S1_PURCHASED_S2 = new SystemMessage(378);

    public static final SystemMessage REJECT_RESURRECTION = new SystemMessage(356);

    public static final SystemMessage ALREADY_SPOILED = new SystemMessage(357);

    public static final SystemMessage S1_HOUR_S_UNTIL_CASTLE_SIEGE_CONCLUSION = new SystemMessage(358);

    public static final SystemMessage S1_MINUTE_S_UNTIL_CASTLE_SIEGE_CONCLUSION = new SystemMessage(359);

    public static final SystemMessage INCORRECT_ITEM = new SystemMessage(352);

    public static final SystemMessage CANNOT_PURCHASE = new SystemMessage(353);

    public static final SystemMessage CANCEL_ENCHANT = new SystemMessage(354);

    public static final SystemMessage INAPPROPRIATE_ENCHANT_CONDITIONS = new SystemMessage(355);

    public static final SystemMessage ENTER_USERS_NAME_TO_SEARCH = new SystemMessage(364);

    public static final SystemMessage ARE_YOU_SURE = new SystemMessage(365);

    public static final SystemMessage SELECT_HAIR_COLOR = new SystemMessage(366);

    public static final SystemMessage CANNOT_REMOVE_CLAN_CHARACTER = new SystemMessage(367);

    public static final SystemMessage CASTLE_SIEGE_S1_SECOND_S_LEFT = new SystemMessage(360);

    public static final SystemMessage OVER_HIT = new SystemMessage(361);

    public static final SystemMessage ACQUIRED_S1_BONUS_EXPERIENCE_THROUGH_OVER_HIT = new SystemMessage(362);

    public static final SystemMessage CHAT_AVAILABLE_TIME_S1_MINUTE = new SystemMessage(363);

    public static final SystemMessage REGISTRATION_PERIOD = new SystemMessage(410);

    public static final SystemMessage REGISTRATION_TIME_S1_S2_S3 = new SystemMessage(411);

    public static final SystemMessage SET_PERIOD = new SystemMessage(408);

    public static final SystemMessage SET_TIME_S1_S2_S3 = new SystemMessage(409);

    public static final SystemMessage STANDBY = new SystemMessage(414);

    public static final SystemMessage UNDER_SIEGE = new SystemMessage(415);

    public static final SystemMessage BATTLE_BEGINS_IN_S1_S2_S4 = new SystemMessage(412);

    public static final SystemMessage BATTLE_ENDS_IN_S1_S2_S5 = new SystemMessage(413);

    public static final SystemMessage YOU_MAY_NOT_GET_ON_BOARD_WITHOUT_A_PASS = new SystemMessage(402);

    public static final SystemMessage YOU_HAVE_EXCEEDED_YOUR_POCKET_MONEY_LIMIT = new SystemMessage(403);

    public static final SystemMessage DISCARD_S1_DO_YOU_WISH_TO_CONTINUE = new SystemMessage(400);

    public static final SystemMessage TOO_MANY_QUESTS_IN_PROGRESS = new SystemMessage(401);

    public static final SystemMessage PETITION_APPLICATION_ACCEPTED = new SystemMessage(406);

    public static final SystemMessage PETITION_UNDER_PROCESS = new SystemMessage(407);

    public static final SystemMessage CREATE_ITEM_LEVEL_IS_TOO_LOW_TO_REGISTER_THIS_RECIPE = new SystemMessage(404);

    public static final SystemMessage THE_TOTAL_PRICE_OF_THE_PRODUCT_IS_TOO_HIGH = new SystemMessage(405);

    public static final SystemMessage ENDING_PETITION_CONSULTATION_WITH_S1 = new SystemMessage(395);

    public static final SystemMessage PETITION_CONSULTATION_WITH_S1_UNDER_WAY = new SystemMessage(394);

    public static final SystemMessage FAILED_TO_CANCEL_PETITION_PLEASE_TRY_AGAIN_LATER = new SystemMessage(393);

    public static final SystemMessage UNDER_PETITION_ADVICE = new SystemMessage(392);

    public static final SystemMessage SYSTEM_ERROR = new SystemMessage(399);

    public static final SystemMessage YOU_HAVE_NO_MORE_TIME_LEFT_ON_YOUR_ACCOUNT = new SystemMessage(398);

    public static final SystemMessage NOT_A_PAID_ACCOUNT = new SystemMessage(397);

    public static final SystemMessage PLEASE_LOGIN_AFTER_CHANGING_YOUR_TEMPORARY_PASSWORD = new SystemMessage(396);

    public static final SystemMessage ENDING_PETITION_CONSULTATION = new SystemMessage(387);

    public static final SystemMessage PETITION_REQUESTS_MUST_BE_OVER_FIVE_CHARACTERS = new SystemMessage(386);

    public static final SystemMessage REQUEST_CONFIRMED_TO_BEGIN_CONSULTATION_AT_PETITION_SERVER = new SystemMessage(385);

    public static final SystemMessage THE_CLIENT_IS_NOT_LOGGED_ONTO_THE_GAME_SERVER = new SystemMessage(384);

    public static final SystemMessage RECEIPT_NO_S1_PETITION_CANCELLED = new SystemMessage(391);

    public static final SystemMessage ALREADY_APPLIED_FOR_PETITION = new SystemMessage(390);

    public static final SystemMessage PETITION_APPLICATION_ACCEPTED_RECEIPT_NO_IS_S1 = new SystemMessage(389);

    public static final SystemMessage NOT_UNDER_PETITION_CONSULTATION = new SystemMessage(388);

    public static final SystemMessage BECAUSE_YOU_ARE_REGISTERED_AS_A_MINOR_YOUR_ACCOUNT_HAS_BEEN_SUSPENDED_AT_THE_REQUEST_OF_YOUR = new SystemMessage(440);

    public static final SystemMessage PER_OUR_COMPANYS_USER_AGREEMENT_THE_USE_OF_THIS_ACCOUNT_HAS_BEEN_SUSPENDED_IF_YOU_HAVE_ANY = new SystemMessage(441);

    public static final SystemMessage YOUR_ACCOUNT_HAS_BEEN_SUSPENDED_UNDER_CHAPTER_2_SECTION_7_OF_THE_LINEAGE_II_SERVICE_USE = new SystemMessage(442);

    public static final SystemMessage THE_IDENTITY_OF_THIS_ACCOUNT_HAS_NOT_BEEN_VEEN_VERIFIED_THEREFORE_LINEAGE_II_SERVICE_FOR_THIS = new SystemMessage(443);

    public static final SystemMessage SINCE_WE_HAVE_RECEIVED_A_WITHDRAWAL_REQUEST_FROM_THE_HOLDER_OF_THIS_ACCOUNT_ACCESS_TO_ALL = new SystemMessage(444);

    public static final SystemMessage REFERENCE_NUMBER_REGARDING_MEMBERSHIP_WITHDRAWAL_REQUEST__S1 = new SystemMessage(445);

    public static final SystemMessage FOR_MORE_INFORMATION_PLEASE_VISIT_THE_SUPPORT_CENTER_ON_THE_PLAYNC_WEBSITE_HTTP___WWWPLAYNCCOM = new SystemMessage(446);

    public static final SystemMessage SYSMSG_ID447 = new SystemMessage(447);

    public static final SystemMessage YOUR_ACCOUNT_HAS_BEEN_SUSPENDED_FOR_7_DAYS_RETROACTIVE_TO_THE_DAY_OF_DISCLOSURE_UNDER_CHAPTER_3 = new SystemMessage(432);

    public static final SystemMessage YOUR_ACCOUNT_HAS_BEEN_SUSPENDED_UNDER_CHAPTER_3_SECTION_14_OF_THE_LINEAGE_II_SERVICE_USE = new SystemMessage(433);

    public static final SystemMessage YOUR_ACCOUNT_HAS_BEEN_SUSPENDED_UNDER_CHAPTER_3_SECTION_14_OF_THE_LINEAGE_II_SERVICE_USE_1 = new SystemMessage(434);

    public static final SystemMessage YOUR_ACCOUNT_HAS_BEEN_SUSPENDED_UNDER_CHAPTER_3_SECTION_14_OF_THE_LINEAGE_II_SERVICE_USE_2 = new SystemMessage(435);

    public static final SystemMessage YOUR_ACCOUNT_HAS_BEEN_SUSPENDED_UNDER_CHAPTER_3_SECTION_14_OF_THE_LINEAGE_II_SERVICE_USE_3 = new SystemMessage(436);

    public static final SystemMessage YOUR_ACCOUNT_HAS_BEEN_SUSPENDED_UNDER_CHAPTER_3_SECTION_14_OF_THE_LINEAGE_II_SERVICE_USE_4 = new SystemMessage(437);

    public static final SystemMessage YOUR_ACCOUNT_HAS_BEEN_SUSPENDED_UNDER_CHAPTER_3_SECTION_14_OF_THE_LINEAGE_II_SERVICE_USE_5 = new SystemMessage(438);

    public static final SystemMessage IN_ACCORDANCE_WITH_THE_COMPANYS_USER_AGREEMENT_AND_OPERATIONAL_POLICY_THIS_ACCOUNT_HAS_BEEN = new SystemMessage(439);

    public static final SystemMessage YOUR_CREATE_ITEM_LEVEL_IS_TOO_LOW = new SystemMessage(425);

    public static final SystemMessage DOES_NOT_FIT_STRENGTHENING_CONDITIONS_OF_THE_SCROLL = new SystemMessage(424);

    public static final SystemMessage PLEASE_CONTACT_US = new SystemMessage(427);

    public static final SystemMessage YOUR_ACCOUNT_HAS_BEEN_REPORTED_FOR_INTENTIONALLY_NOT_PAYING_THE_CYBER_CAFE_FEES = new SystemMessage(426);

    public static final SystemMessage IN_ACCORDANCE_WITH_COMPANY_POLICY_YOUR_ACCOUNT_HAS_BEEN_SUSPENDED_DUE_TO_FALSELY_REPORTING_A = new SystemMessage(429);

    public static final SystemMessage IN_ACCORDANCE_WITH_COMPANY_POLICY_YOUR_ACCOUNT_HAS_BEEN_SUSPENDED_DUE_TO_SUSPICION_OF_ILLEGAL = new SystemMessage(428);

    public static final SystemMessage YOUR_ACCOUNT_HAS_BEEN_SUSPENDED_DUE_TO_VIOLATING_THE_EULA_ROC_AND_OR_USER_AGREEMENT_CHAPTER_4 = new SystemMessage(431);

    public static final SystemMessage __DOESNT_NEED_TO_TRANSLATE = new SystemMessage(430);

    public static final SystemMessage S1__HAS_BEEN_DISARMED = new SystemMessage(417);

    public static final SystemMessage CANNOT_BE_EXCHANGED = new SystemMessage(416);

    public static final SystemMessage S1_MINUTE_S_OF_DESIGNATED_USAGE_TIME_LEFT = new SystemMessage(419);

    public static final SystemMessage THERE_IS_A_SIGNIFICANT_DIFFERENCE_BETWEEN_THE_ITEMS_PRICE_AND_ITS_STANDARD_PRICE_PLEASE_CHECK_AGAIN = new SystemMessage(418);

    public static final SystemMessage ANOTHER_PERSON_HAS_LOGGED_IN_WITH_THE_SAME_ACCOUNT = new SystemMessage(421);

    public static final SystemMessage TIME_EXPIRED = new SystemMessage(420);

    public static final SystemMessage THE_SCROLL_OF_ENCHANT_HAS_BEEN_CANCELED = new SystemMessage(423);

    public static final SystemMessage YOU_HAVE_EXCEEDED_THE_WEIGHT_LIMIT = new SystemMessage(422);

    public static final SystemMessage NO_RESPONSE_YOUR_ENTRANCE_TO_THE_ALLIANCE_HAS_BEEN_CANCELLED = new SystemMessage(478);

    public static final SystemMessage S1_HAS_JOINED_AS_A_FRIEND = new SystemMessage(479);

    public static final SystemMessage INCORRECT_IMAGE_SIZE_PLEASE_ADJUST_TO_8X12 = new SystemMessage(476);

    public static final SystemMessage NO_RESPONSE_INVITATION_TO_JOIN_AN_ALLIANCE_HAS_BEEN_CANCELLED = new SystemMessage(477);

    public static final SystemMessage THE_FOLLOWING_CLAN_DOES_NOT_EXIST = new SystemMessage(474);

    public static final SystemMessage DIFFERENT_ALLIANCE_1 = new SystemMessage(475);

    public static final SystemMessage YOU_CANNOT_EXPEL_YOURSELF_FROM_THE_CLAN = new SystemMessage(472);

    public static final SystemMessage DIFFERENT_ALLIANCE = new SystemMessage(473);

    public static final SystemMessage ONLY_THE_CLAN_LEADER_MAY_APPLY_FOR_WITHDRAWAL_FROM_THE_ALLIANCE = new SystemMessage(470);

    public static final SystemMessage ALLIANCE_LEADERS_CANNOT_WITHDRAW = new SystemMessage(471);

    public static final SystemMessage A_CLAN_THAT_HAS_WITHDRAWN_OR_BEEN_EXPELLED_CANNOT_ENTER_INTO_AN_ALLIANCE_WITHIN_ONE_DAY_OF_WITHDRAWAL_OR_EXPULSION = new SystemMessage(468);

    public static final SystemMessage YOU_MAY_NOT_ALLY_WITH_A_CLAN_YOU_ARE_AT_BATTLE_WITH = new SystemMessage(469);

    public static final SystemMessage YOU_HAVE_EXCEEDED_THE_LIMIT = new SystemMessage(466);

    public static final SystemMessage YOU_MAY_NOT_ACCEPT_ANY_CLAN_WITHIN_A_DAY_AFTER_EXPELLING_ANOTHER_CLAN = new SystemMessage(467);

    public static final SystemMessage FEATURE_AVAILABLE_TO_ALLIANCE_LEADERS_ONLY = new SystemMessage(464);

    public static final SystemMessage YOU_ARE_NOT_CURRENTLY_ALLIED_WITH_ANY_CLANS = new SystemMessage(465);

    public static final SystemMessage SYSMSG_ID463 = new SystemMessage(463);

    public static final SystemMessage PLEASE_TRY_AGAIN_LATER_1 = new SystemMessage(462);

    public static final SystemMessage ACCESS_FAILED = new SystemMessage(461);

    public static final SystemMessage TO_REACTIVATE_YOUR_ACCOUNT = new SystemMessage(460);

    public static final SystemMessage PLEASE_VISIT_THE_OFFICIAL_LINEAGE_II_WEBSITE_AT_HTTP__WWWLINEAGE2COM = new SystemMessage(459);

    public static final SystemMessage YOUR_USAGE_TERM_HAS_EXPIRED = new SystemMessage(458);

    public static final SystemMessage SERVER_UNDER_MAINTENANCE_PLEASE_TRY_AGAIN_LATER = new SystemMessage(457);

    public static final SystemMessage LINEAGE_II_GAME_SERVICES_MAY_BE_USED_BY_INDIVIDUALS_15_YEARS_OF_AGE_OR_OLDER_EXCEPT_FOR_PVP_SERVERS = new SystemMessage(456);

    public static final SystemMessage THE_ACCOUNT_IS_ALREADY_IN_USE_ACCESS_DENIED = new SystemMessage(455);

    public static final SystemMessage FOR_MORE_DETAILS_PLEASE_CONTACT_OUR_CUSTOMER_SERVICE_CENTER_AT_HTTP__SUPPORTPLAYNCCOM = new SystemMessage(454);

    public static final SystemMessage YOUR_ACCOUNT_INFORMATION_IS_INCORRECT = new SystemMessage(453);

    public static final SystemMessage PLEASE_CONFIRM_YOUR_ACCOUNT_INFORMATION_AND_TRY_LOGGING_IN_AGAIN = new SystemMessage(452);

    public static final SystemMessage THE_PASSWORD_YOU_HAVE_ENTERED_IS_INCORRECT = new SystemMessage(451);

    public static final SystemMessage CONFIRM_YOUR_ACCOUNT_INFORMATION_AND_LOG_IN_AGAIN_LATER = new SystemMessage(450);

    public static final SystemMessage PASSWORD_DOES_NOT_MATCH_THIS_ACCOUNT = new SystemMessage(449);

    public static final SystemMessage SYSTEM_ERROR_PLEASE_LOG_IN_AGAIN_LATER = new SystemMessage(448);

    public static final SystemMessage THIS_ALLIANCE_NAME_ALREADY_EXISTS = new SystemMessage(508);

    public static final SystemMessage CANNOT_ACCEPT_CLAN_ALLY_IS_REGISTERED_AS_AN_ENEMY_DURING_SIEGE_BATTLE = new SystemMessage(509);

    public static final SystemMessage YOU_HAVE_INVITED_SOMEONE_TO_YOUR_ALLIANCE = new SystemMessage(510);

    public static final SystemMessage SELECT_USER_TO_INVITE = new SystemMessage(511);

    public static final SystemMessage ONLY_CLAN_LEADERS_MAY_CREATE_ALLIANCES = new SystemMessage(504);

    public static final SystemMessage YOU_CANNOT_CREATE_A_NEW_ALLIANCE_WITHIN_1_DAY_AFTER_DISSOLUTION = new SystemMessage(505);

    public static final SystemMessage INCORRECT_ALLIANCE_NAME = new SystemMessage(506);

    public static final SystemMessage INCORRECT_LENGTH_FOR_AN_ALLIANCE_NAME = new SystemMessage(507);

    public static final SystemMessage __DASHES__ = new SystemMessage(500);

    public static final SystemMessage SYSMSG_ID501 = new SystemMessage(501);

    public static final SystemMessage YOU_ALREADY_BELONG_TO_ANOTHER_ALLIANCE = new SystemMessage(502);

    public static final SystemMessage S1_FRIEND_HAS_LOGGED_IN = new SystemMessage(503);

    public static final SystemMessage _CLAN_INFORMATION_ = new SystemMessage(496);

    public static final SystemMessage CLAN_NAME_S1 = new SystemMessage(497);

    public static final SystemMessage CLAN_LEADER_S1 = new SystemMessage(498);

    public static final SystemMessage CLAN_LEVEL_S1 = new SystemMessage(499);

    public static final SystemMessage CONNECTION_S1_TOTAL_S2 = new SystemMessage(493);

    public static final SystemMessage ALLIANCE_NAME_S1 = new SystemMessage(492);

    public static final SystemMessage AFFILIATED_CLANS_TOTAL_S1_CLAN_S = new SystemMessage(495);

    public static final SystemMessage ALLIANCE_LEADER_S2_OF_S1 = new SystemMessage(494);

    public static final SystemMessage S1_CURRENTLY_OFFLINE = new SystemMessage(489);

    public static final SystemMessage S1_CURRENTLY_ONLINE = new SystemMessage(488);

    public static final SystemMessage _ALLIANCE_INFORMATION_ = new SystemMessage(491);

    public static final SystemMessage __EQUALS__ = new SystemMessage(490);

    public static final SystemMessage NO_NEW_FRIEND_INVITATIONS_FROM_OTHER_USERS = new SystemMessage(485);

    public static final SystemMessage ALREADY_REGISTERED_ON_THE_FRIENDS_LIST = new SystemMessage(484);

    public static final SystemMessage _FRIENDS_LIST_ = new SystemMessage(487);

    public static final SystemMessage THE_FOLLOWING_USER_IS_NOT_IN_YOUR_FRIENDS_LIST = new SystemMessage(486);

    public static final SystemMessage S1__HAS_BEEN_DELETED_FROM_YOUR_FRIENDS_LIST = new SystemMessage(481);

    public static final SystemMessage PLEASE_CHECK_YOUR_FRIENDS_LIST = new SystemMessage(480);

    public static final SystemMessage FRIEND_LIST_IS_NOT_READY_YET_PLEASE_TRY_AGAIN_LATER = new SystemMessage(483);

    public static final SystemMessage YOU_CANNOT_ADD_YOURSELF_TO_YOUR_OWN_FRIEND_LIST_1 = new SystemMessage(482);

    public static final SystemMessage YOU_CANNOT_CREATE_AN_ALLIANCE_DURING_THE_TERM_OF_DISSOLUTION_POSTPONEMENT = new SystemMessage(550);

    public static final SystemMessage YOU_CANNOT_RAISE_YOUR_CLAN_LEVEL_DURING_THE_TERM_OF_DISPERSION_POSTPONEMENT = new SystemMessage(551);

    public static final SystemMessage YOUR_PETS_NAME_CAN_BE_UP_TO_8_CHARACTERS = new SystemMessage(548);

    public static final SystemMessage TO_CREATE_AN_ALLIANCE_YOUR_CLAN_MUST_BE_LEVEL_5_OR_HIGHER = new SystemMessage(549);

    public static final SystemMessage EXCEEDED_PET_INVENTORYS_WEIGHT_LIMIT = new SystemMessage(546);

    public static final SystemMessage SUMMON_A_PET = new SystemMessage(547);

    public static final SystemMessage ITEM_NOT_AVAILABLE_FOR_PETS = new SystemMessage(544);

    public static final SystemMessage DUE_TO_THE_VOLUME_LIMIT_OF_THE_PETS_INVENTORY_NO_MORE_ITEMS_CAN_BE_PLACED_THERE = new SystemMessage(545);

    public static final SystemMessage THE_PET_HAS_BEEN_SUMMONED_AND_CANNOT_BE_LET_GO = new SystemMessage(558);

    public static final SystemMessage PURCHASED_S2_FROM_S1 = new SystemMessage(559);

    public static final SystemMessage YOU_CANNOT_MOVE_IN_THIS_STATE = new SystemMessage(556);

    public static final SystemMessage THE_PET_HAS_BEEN_SUMMONED_AND_CANNOT_BE_DELETED = new SystemMessage(557);

    public static final SystemMessage YOU_CANNOT_DISPERSE_THE_CLANS_IN_YOUR_ALLIANCE = new SystemMessage(554);

    public static final SystemMessage YOU_CANNOT_MOVE_YOUR_ITEM_WEIGHT_IS_TOO_GREAT = new SystemMessage(555);

    public static final SystemMessage DURING_THE_GRACE_PERIOD_FOR_DISSOLVING_A_CLAN_REGISTRATION_OR_DELETION_OF_A_CLANS_CREST_IS_NOT_ALLOWED = new SystemMessage(552);

    public static final SystemMessage THE_OPPOSING_CLAN_HAS_APPLIED_FOR_DISPERSION = new SystemMessage(553);

    public static final SystemMessage FEAR_FAILED = new SystemMessage(567);

    public static final SystemMessage CONFUSION_FAILED = new SystemMessage(566);

    public static final SystemMessage NOT_ENOUGH_LUCK = new SystemMessage(565);

    public static final SystemMessage FAILED_TO_CHANGE_ATTACK_TARGET = new SystemMessage(564);

    public static final SystemMessage FAILED_TO_DISABLE_ATTACK_TARGET = new SystemMessage(563);

    public static final SystemMessage CANNOT_CRYSTALLIZE_CRYSTALLIZATION_SKILL_LEVEL_TOO_LOW = new SystemMessage(562);

    public static final SystemMessage PURCHASED_S3_S2_S_FROM_S1_ = new SystemMessage(561);

    public static final SystemMessage PURCHASED_S2_S3_FROM_S1 = new SystemMessage(560);

    public static final SystemMessage HOW_MUCH_ADENA_DO_YOU_WISH_TO_TRANSFER_TO_YOUR_PET = new SystemMessage(575);

    public static final SystemMessage PETS_AND_SERVITORS_ARE_NOT_AVAILABLE_AT_THIS_TIME = new SystemMessage(574);

    public static final SystemMessage DO_YOU_WISH_TO_JOIN_S1S_PARTY_ITEM_DISTRIBUTION_RANDOM = new SystemMessage(573);

    public static final SystemMessage DO_YOU_WISH_TO_JOIN_S1S_PARTY_ITEM_DISTRIBUTION_FINDERS_KEEPERS = new SystemMessage(572);

    public static final SystemMessage HOW_MANY__S1__S_DO_YOU_WANT_TO_PURCHASE = new SystemMessage(571);

    public static final SystemMessage HOW_MANY__S1__S_DO_YOU_WISH_TO_PURCHASE = new SystemMessage(570);

    public static final SystemMessage CAUTION_THE_ITEM_PRICE_GREATLY_DIFFERS_FROM_THE_SHOPS_STANDARD_PRICE_DO_YOU_WISH_TO_CONTINUE = new SystemMessage(569);

    public static final SystemMessage CUBIC_SUMMONING_FAILED = new SystemMessage(568);

    public static final SystemMessage S1_HAS_INVITED_YOU_AS_A_FRIEND = new SystemMessage(516);

    public static final SystemMessage YOU_HAVE_ACCEPTED_THE_ALLIANCE = new SystemMessage(517);

    public static final SystemMessage YOU_HAVE_FAILED_TO_INVITE_A_CLAN_INTO_THE_ALLIANCE = new SystemMessage(518);

    public static final SystemMessage YOU_HAVE_WITHDRAWN_FROM_THE_ALLIANCE = new SystemMessage(519);

    public static final SystemMessage DO_YOU_REALLY_WISH_TO_WITHDRAW_FROM_THE_ALLIANCE = new SystemMessage(512);

    public static final SystemMessage ENTER_THE_NAME_OF_THE_CLAN_YOU_WISH_TO_EXPEL = new SystemMessage(513);

    public static final SystemMessage DO_YOU_REALLY_WISH_TO_DISSOLVE_THE_ALLIANCE = new SystemMessage(514);

    public static final SystemMessage ENTER_FILE_NAME_FOR_THE_ALLIANCE_CREST = new SystemMessage(515);

    public static final SystemMessage YOU_HAVE_FAILED_TO_DISSOLVE_THE_ALLIANCE = new SystemMessage(524);

    public static final SystemMessage YOU_HAVE_SUCCEEDED_IN_INVITING_A_FRIEND = new SystemMessage(525);

    public static final SystemMessage YOU_HAVE_FAILED_TO_INVITE_A_FRIEND = new SystemMessage(526);

    public static final SystemMessage S2_THE_LEADER_OF_S1_HAS_REQUESTED_AN_ALLIANCE = new SystemMessage(527);

    public static final SystemMessage YOU_HAVE_FAILED_TO_WITHDRAW_FROM_THE_ALLIANCE = new SystemMessage(520);

    public static final SystemMessage YOU_HAVE_SUCCEEDED_IN_EXPELLING_A_CLAN = new SystemMessage(521);

    public static final SystemMessage YOU_HAVE_FAILED_TO_EXPEL_A_CLAN = new SystemMessage(522);

    public static final SystemMessage THE_ALLIANCE_HAS_BEEN_DISSOLVED = new SystemMessage(523);

    public static final SystemMessage POWER_OF_MANA_ENABLED = new SystemMessage(533);

    public static final SystemMessage CANNOT_USE_SPIRITSHOTS = new SystemMessage(532);

    public static final SystemMessage NAME_PET = new SystemMessage(535);

    public static final SystemMessage POWER_OF_MANA_DISABLED = new SystemMessage(534);

    public static final SystemMessage YOU_MAY_ONLY_REGISTER_8X12_BMP_FILES_WITH_256_COLORS = new SystemMessage(529);

    public static final SystemMessage FILE_NOT_FOUND = new SystemMessage(528);

    public static final SystemMessage NOT_ENOUGH_SPIRITSHOTS = new SystemMessage(531);

    public static final SystemMessage SPIRITSHOT_DOES_NOT_MATCH_WEAPON_GRADE = new SystemMessage(530);

    public static final SystemMessage YOU_CANNOT_DELETE_A_CLAN_MEMBER_WITHDRAW_FROM_THE_CLAN_AND_TRY_AGAIN = new SystemMessage(541);

    public static final SystemMessage CLAN_LEADERS_CANNOT_BE_DELETED_DISSOLVE_THE_CLAN_AND_TRY_AGAIN = new SystemMessage(540);

    public static final SystemMessage YOU_ALREADY_HAVE_A_PET = new SystemMessage(543);

    public static final SystemMessage NPC_SERVER_NOT_OPERATING_PETS_CANNOT_BE_SUMMONED = new SystemMessage(542);

    public static final SystemMessage HOW_MUCH_WILL_YOU_TRANSFER = new SystemMessage(537);

    public static final SystemMessage HOW_MUCH_ADENA_DO_YOU_WISH_TO_TRANSFER_TO_YOUR_INVENTORY = new SystemMessage(536);

    public static final SystemMessage EXPERIENCE_HAS_DECREASED_BY_S1 = new SystemMessage(539);

    public static final SystemMessage SP_HAS_DECREASED_BY_S1 = new SystemMessage(538);

    public static final SystemMessage YOUR_SKILL_HAS_BEEN_CANCELED_DUE_TO_LACK_OF_HP = new SystemMessage(610);

    public static final SystemMessage YOU_HAVE_SUCCEEDED_IN_CONFUSING_THE_ENEMY = new SystemMessage(611);

    public static final SystemMessage S1_HAS_OBTAINED_3_S2_S_BY_USING_SWEEPER = new SystemMessage(608);

    public static final SystemMessage S1_HAS_OBTAINED_S2_BY_USING_SWEEPER = new SystemMessage(609);

    public static final SystemMessage C1___C2 = new SystemMessage(614);

    public static final SystemMessage YOU_HAVE_FAILED_TO_REGISTER_THE_USER_TO_YOUR_IGNORE_LIST = new SystemMessage(615);

    public static final SystemMessage THE_SPOIL_CONDITION_HAS_BEEN_ACTIVATED = new SystemMessage(612);

    public static final SystemMessage _IGNORE_LIST_ = new SystemMessage(613);

    public static final SystemMessage S1_HAS_BEEN_REMOVED_FROM_YOUR_IGNORE_LIST = new SystemMessage(618);

    public static final SystemMessage S1__HAS_PLACED_YOU_ON_HIS_HER_IGNORE_LIST = new SystemMessage(619);

    public static final SystemMessage YOU_HAVE_FAILED_TO_DELETE_THE_CHARACTER_FROM_IGNORE_LIST = new SystemMessage(616);

    public static final SystemMessage S1_HAS_BEEN_ADDED_TO_YOUR_IGNORE_LIST = new SystemMessage(617);

    public static final SystemMessage YOU_MAY_NOT_MAKE_A_DECLARATION_OF_WAR_DURING_AN_ALLIANCE_BATTLE = new SystemMessage(622);

    public static final SystemMessage YOUR_OPPONENT_HAS_EXCEEDED_THE_NUMBER_OF_SIMULTANEOUS_ALLIANCE_BATTLES_ALLOWED = new SystemMessage(623);

    public static final SystemMessage S1__HAS_PLACED_YOU_ON_HIS_HER_IGNORE_LIST_1 = new SystemMessage(620);

    public static final SystemMessage THIS_SERVER_IS_RESERVED_FOR_PLAYERS_IN_KOREA__TO_USE_LINEAGE_II_GAME_SERVICES_PLEASE_CONNECT_TO_THE_SERVER_IN_YOUR_REGION = new SystemMessage(621);

    public static final SystemMessage CLAN_BATTLE_HAS_BEEN_REFUSED_BECAUSE_YOU_DID_NOT_RESPOND_TO_S1_CLANS_WAR_PROCLAMATION = new SystemMessage(627);

    public static final SystemMessage THE_S1_CLAN_DID_NOT_RESPOND__WAR_PROCLAMATION_HAS_BEEN_REFUSED = new SystemMessage(626);

    public static final SystemMessage YOUR_REQUEST_FOR_ALLIANCE_BATTLE_TRUCE_HAS_BEEN_DENIED = new SystemMessage(625);

    public static final SystemMessage S1_CLAN_LEADER_IS_NOT_CURRENTLY_CONNECTED_TO_THE_GAME_SERVER = new SystemMessage(624);

    public static final SystemMessage WAR_WITH_THE_S1_CLAN_IS_OVER = new SystemMessage(631);

    public static final SystemMessage WAR_WITH_THE_S1_CLAN_HAS_BEGUN_1 = new SystemMessage(630);

    public static final SystemMessage YOUR_OPPONENT_HAS_EXCEEDED_THE_NUMBER_OF_SIMULTANEOUS_ALLIANCE_BATTLES_ALLOWED_1 = new SystemMessage(629);

    public static final SystemMessage YOU_HAVE_ALREADY_BEEN_AT_WAR_WITH_THE_S1_CLAN_5_DAYS_MUST_PASS_BEFORE_YOU_CAN_DECLARE_WAR_AGAIN = new SystemMessage(628);

    public static final SystemMessage THE_TIME_LIMIT_FOR_THE_CLAN_WAR_HAS_BEEN_EXCEEDED_WAR_WITH_THE_S1_CLAN_IS_OVER = new SystemMessage(635);

    public static final SystemMessage YOUR_ALLIANCE_LEADER_HAS_BEEN_SLAIN_YOU_HAVE_BEEN_DEFEATED_BY_THE_S1_CLAN = new SystemMessage(634);

    public static final SystemMessage YOU_HAVE_SURRENDERED_TO_THE_S1_CLAN_1 = new SystemMessage(633);

    public static final SystemMessage YOU_HAVE_WON_THE_WAR_OVER_THE_S1_CLAN_1 = new SystemMessage(632);

    public static final SystemMessage YOUR_APPLICATION_HAS_BEEN_DENIED_BECAUSE_YOU_HAVE_ALREADY_SUBMITTED_A_REQUEST_FOR_ANOTHER_SIEGE_BATTLE = new SystemMessage(639);

    public static final SystemMessage YOU_HAVE_ALREADY_REQUESTED_A_SIEGE_BATTLE = new SystemMessage(638);

    public static final SystemMessage A_CLAN_ALLY_HAS_REGISTERED_ITSELF_TO_THE_OPPONENT = new SystemMessage(637);

    public static final SystemMessage YOU_ARE_NOT_INVOLVED_IN_A_CLAN_WAR_1 = new SystemMessage(636);

    public static final SystemMessage HOW_MUCH_DO_YOU_WISH_TO_TRANSFER = new SystemMessage(576);

    public static final SystemMessage YOU_CANNOT_SUMMON_DURING_A_TRADE_OR_WHILE_USING_THE_PRIVATE_SHOPS = new SystemMessage(577);

    public static final SystemMessage YOU_CANNOT_SUMMON_DURING_COMBAT = new SystemMessage(578);

    public static final SystemMessage A_PET_CANNOT_BE_SENT_BACK_DURING_BATTLE = new SystemMessage(579);

    public static final SystemMessage YOU_MAY_NOT_USE_MULTIPLE_PETS_OR_SERVITORS_AT_THE_SAME_TIME = new SystemMessage(580);

    public static final SystemMessage THERE_IS_A_SPACE_IN_THE_NAME = new SystemMessage(581);

    public static final SystemMessage INAPPROPRIATE_CHARACTER_NAME = new SystemMessage(582);

    public static final SystemMessage NAME_INCLUDES_FORBIDDEN_WORDS = new SystemMessage(583);

    public static final SystemMessage ALREADY_IN_USE_BY_ANOTHER_PET = new SystemMessage(584);

    public static final SystemMessage PLEASE_DECIDE_ON_THE_PRICE = new SystemMessage(585);

    public static final SystemMessage PET_ITEMS_CANNOT_BE_REGISTERED_AS_SHORTCUTS = new SystemMessage(586);

    public static final SystemMessage IRREGULAR_SYSTEM_SPEED = new SystemMessage(587);

    public static final SystemMessage PET_INVENTORY_IS_FULL = new SystemMessage(588);

    public static final SystemMessage A_DEAD_PET_CANNOT_BE_SENT_BACK = new SystemMessage(589);

    public static final SystemMessage CANNOT_GIVE_ITEMS_TO_A_DEAD_PET = new SystemMessage(590);

    public static final SystemMessage AN_INVALID_CHARACTER_IS_INCLUDED_IN_THE_PETS_NAME = new SystemMessage(591);

    public static final SystemMessage YOUR_PET_HAS_LEFT_DUE_TO_UNBEARABLE_HUNGER = new SystemMessage(593);

    public static final SystemMessage DO_YOU_WISH_TO_DISMISS_YOUR_PET_DISMISSING_YOUR_PET_WILL_CAUSE_THE_PET_NECKLACE_TO_DISAPPEAR = new SystemMessage(592);

    public static final SystemMessage YOUR_PET_IS_VERY_HUNGRY = new SystemMessage(595);

    public static final SystemMessage YOU_CANNOT_RESTORE_HUNGRY_PETS = new SystemMessage(594);

    public static final SystemMessage YOUR_PET_IS_VERY_HUNGRY_PLEASE_BE_CAREFUL = new SystemMessage(597);

    public static final SystemMessage YOUR_PET_ATE_A_LITTLE_BUT_IS_STILL_HUNGRY = new SystemMessage(596);

    public static final SystemMessage THE_GM_HAS_AN_IMPORTANT_NOTICE_CHAT_IS_TEMPORARILY_ABORTED = new SystemMessage(599);

    public static final SystemMessage YOU_CANNOT_CHAT_WHILE_YOU_ARE_INVISIBLE = new SystemMessage(598);

    public static final SystemMessage THERE_ARE_S1_PETITIONS_PENDING = new SystemMessage(601);

    public static final SystemMessage YOU_CANNOT_EQUIP_A_PET_ITEM = new SystemMessage(600);

    public static final SystemMessage THE_PETITION_SYSTEM_IS_CURRENTLY_UNAVAILABLE_PLEASE_TRY_AGAIN_LATER = new SystemMessage(602);

    public static final SystemMessage THAT_ITEM_CANNOT_BE_DISCARDED_OR_EXCHANGED = new SystemMessage(603);

    public static final SystemMessage YOU_MAY_NOT_CALL_FORTH_A_PET_OR_SUMMONED_CREATURE_FROM_THIS_LOCATION = new SystemMessage(604);

    public static final SystemMessage YOU_MAY_REGISTER_UP_TO_64_PEOPLE_ON_YOUR_LIST = new SystemMessage(605);

    public static final SystemMessage YOU_DO_NOT_HAVE_ANY_FURTHER_SKILLS_TO_LEARN__COME_BACK_WHEN_YOU_HAVE_REACHED_LEVEL_S1 = new SystemMessage(607);

    public static final SystemMessage YOU_CANNOT_BE_REGISTERED_BECAUSE_THE_OTHER_PERSON_HAS_ALREADY_REGISTERED_64_PEOPLE_ON_HIS_HER_LIST = new SystemMessage(606);

    public static final SystemMessage YOU_CANNOT_MOVE_IN_A_FROZEN_STATE_PLEASE_WAIT_A_MOMENT = new SystemMessage(687);

    public static final SystemMessage YOU_HAVE_RECEIVED_S1_DAMAGE_FROM_THE_FIRE_OF_MAGIC = new SystemMessage(686);

    public static final SystemMessage YOU_CANNOT_APPLY_FOR_CLAN_WAR_WITH_A_CLAN_THAT_BELONGS_TO_THE_SAME_ALLIANCE = new SystemMessage(685);

    public static final SystemMessage YOU_CANNOT_POSITION_MERCENARIES_DURING_A_SIEGE = new SystemMessage(684);

    public static final SystemMessage THERE_ARE_NO_PRIORITY_RIGHTS_ON_A_SWEEPER = new SystemMessage(683);

    public static final SystemMessage YOU_ARE_MOVING_TO_ANOTHER_VILLAGE_DO_YOU_WANT_TO_CONTINUE = new SystemMessage(682);

    public static final SystemMessage THE_CLAN_DOES_NOT_OWN_A_CLAN_HALL = new SystemMessage(681);

    public static final SystemMessage YOU_CANNOT_PARTICIPATE_IN_AN_AUCTION = new SystemMessage(680);

    public static final SystemMessage YOU_HAVE_CANCELED_YOUR_BID = new SystemMessage(679);

    public static final SystemMessage YOU_HAVE_SUBMITTED_A_BID_IN_THE_AUCTION_OF_S1 = new SystemMessage(678);

    public static final SystemMessage YOUR_BID_PRICE_MUST_BE_HIGHER_THAN_THE_MINIMUM_PRICE_THAT_CAN_BE_BID = new SystemMessage(677);

    public static final SystemMessage SINCE_YOU_HAVE_ALREADY_SUBMITTED_A_BID_YOU_ARE_NOT_ALLOWED_TO_PARTICIPATE_IN_ANOTHER_AUCTION_AT_THIS_TIME = new SystemMessage(676);

    public static final SystemMessage THERE_ARE_NO_CLAN_HALLS_UP_FOR_AUCTION = new SystemMessage(675);

    public static final SystemMessage IT_HAS_NOT_YET_BEEN_SEVEN_DAYS_SINCE_CANCELING_AN_AUCTION = new SystemMessage(674);

    public static final SystemMessage ONLY_A_CLAN_LEADER_WHOSE_CLAN_IS_OF_LEVEL_2_OR_HIGHER_IS_ALLOWED_TO_PARTICIPATE_IN_A_CLAN_HALL_AUCTION = new SystemMessage(673);

    public static final SystemMessage S1_ADENA_DISAPPEARED = new SystemMessage(672);

    public static final SystemMessage THERE_ARE_NOT_ANY_GMS_THAT_ARE_PROVIDING_CUSTOMER_SERVICE_CURRENTLY = new SystemMessage(702);

    public static final SystemMessage _GM_LIST_ = new SystemMessage(703);

    public static final SystemMessage THE_PURCHASE_IS_COMPLETE = new SystemMessage(700);

    public static final SystemMessage YOU_DO_NOT_HAVE_ENOUGH_REQUIRED_ITEMS = new SystemMessage(701);

    public static final SystemMessage THE_PRICE_IS_DIFFERENT_THAN_THE_SAME_ITEM_ON_THE_SALES_LIST = new SystemMessage(698);

    public static final SystemMessage CURRENTLY_NOT_PURCHASING = new SystemMessage(699);

    public static final SystemMessage YOUR_ACCOUNT_IS_RESTRICTED_FOR_NOT_PAYING_YOUR_PC_ROOM_USAGE_FEES = new SystemMessage(696);

    public static final SystemMessage THE_ITEM_ENCHANT_VALUE_IS_STRANGE = new SystemMessage(697);

    public static final SystemMessage NO_PACKAGES_HAVE_ARRIVED = new SystemMessage(694);

    public static final SystemMessage YOU_CANNOT_SET_THE_NAME_OF_THE_PET = new SystemMessage(695);

    public static final SystemMessage THE_OTHER_PARTY_IS_FROZEN_PLEASE_WAIT_A_MOMENT = new SystemMessage(692);

    public static final SystemMessage THE_PACKAGE_THAT_ARRIVED_IS_IN_ANOTHER_WAREHOUSE = new SystemMessage(693);

    public static final SystemMessage YOU_CANNOT_REGISTER_ON_THE_ATTACKING_SIDE_BECAUSE_YOU_ARE_PART_OF_AN_ALLIANCE_WITH_THE_CLAN_THAT_OWNS_THE_CASTLE = new SystemMessage(690);

    public static final SystemMessage S1_CLAN_IS_ALREADY_A_MEMBER_OF_S2_ALLIANCE = new SystemMessage(691);

    public static final SystemMessage THE_CLAN_THAT_OWNS_THE_CASTLE_IS_AUTOMATICALLY_REGISTERED_ON_THE_DEFENDING_SIDE = new SystemMessage(688);

    public static final SystemMessage A_CLAN_THAT_OWNS_A_CASTLE_CANNOT_PARTICIPATE_IN_ANOTHER_SIEGE = new SystemMessage(689);

    public static final SystemMessage YOU_DO_NOT_HAVE_THE_AUTHORITY_TO_POSITION_MERCENARIES = new SystemMessage(653);

    public static final SystemMessage THE_TARGET_OF_THE_SUMMONED_MONSTER_IS_WRONG = new SystemMessage(652);

    public static final SystemMessage MERCENARIES_CANNOT_BE_POSITIONED_HERE = new SystemMessage(655);

    public static final SystemMessage YOU_DO_NOT_HAVE_THE_AUTHORITY_TO_CANCEL_MERCENARY_POSITIONING = new SystemMessage(654);

    public static final SystemMessage NO_MORE_REGISTRATIONS_MAY_BE_ACCEPTED_FOR_THE_DEFENDER_SIDE = new SystemMessage(649);

    public static final SystemMessage NO_MORE_REGISTRATIONS_MAY_BE_ACCEPTED_FOR_THE_ATTACKER_SIDE = new SystemMessage(648);

    public static final SystemMessage PLACE_S1_IN_THE_CURRENT_LOCATION_AND_DIRECTION_DO_YOU_WISH_TO_CONTINUE = new SystemMessage(651);

    public static final SystemMessage YOU_MAY_NOT_SUMMON_FROM_YOUR_CURRENT_LOCATION = new SystemMessage(650);

    public static final SystemMessage ONLY_CLANS_WITH_LEVEL_4_AND_HIGHER_MAY_REGISTER_FOR_A_CASTLE_SIEGE = new SystemMessage(645);

    public static final SystemMessage YOU_ARE_NOT_YET_REGISTERED_FOR_THE_CASTLE_SIEGE = new SystemMessage(644);

    public static final SystemMessage YOU_DO_NOT_HAVE_THE_AUTHORITY_TO_MODIFY_THE_SIEGE_TIME = new SystemMessage(647);

    public static final SystemMessage YOU_DO_NOT_HAVE_THE_AUTHORITY_TO_MODIFY_THE_CASTLE_DEFENDER_LIST = new SystemMessage(646);

    public static final SystemMessage YOU_HAVE_FAILED_TO_APPROVE_CASTLE_DEFENSE_AID = new SystemMessage(641);

    public static final SystemMessage YOU_HAVE_FAILED_TO_REFUSE_CASTLE_DEFENSE_AID = new SystemMessage(640);

    public static final SystemMessage YOU_HAVE_ALREADY_REGISTERED_TO_THE_DEFENDER_SIDE_AND_MUST_CANCEL_YOUR_REGISTRATION_BEFORE_SUBMITTING_YOUR_REQUEST = new SystemMessage(643);

    public static final SystemMessage YOU_ARE_ALREADY_REGISTERED_TO_THE_ATTACKER_SIDE_AND_MUST_CANCEL_YOUR_REGISTRATION_BEFORE_SUBMITTING_YOUR_REQUEST = new SystemMessage(642);

    public static final SystemMessage YOU_ARE_REGISTERING_ON_THE_DEFENDING_SIDE_OF_THE_S1_SIEGE_DO_YOU_WANT_TO_CONTINUE = new SystemMessage(668);

    public static final SystemMessage YOU_ARE_CANCELING_YOUR_APPLICATION_TO_PARTICIPATE_IN_THE_S1_SIEGE_BATTLE_DO_YOU_WANT_TO_CONTINUE = new SystemMessage(669);

    public static final SystemMessage YOU_ARE_REFUSING_THE_REGISTRATION_OF_S1_CLAN_ON_THE_DEFENDING_SIDE_DO_YOU_WANT_TO_CONTINUE = new SystemMessage(670);

    public static final SystemMessage YOU_ARE_AGREEING_TO_THE_REGISTRATION_OF_S1_CLAN_ON_THE_DEFENDING_SIDE_DO_YOU_WANT_TO_CONTINUE = new SystemMessage(671);

    public static final SystemMessage PLEASE_CHOOSE_A_PERSON_TO_RECEIVE = new SystemMessage(664);

    public static final SystemMessage S2_OF_S1_ALLIANCE_IS_APPLYING_FOR_ALLIANCE_WAR_DO_YOU_WANT_TO_ACCEPT_THE_CHALLENGE = new SystemMessage(665);

    public static final SystemMessage A_REQUEST_FOR_CEASEFIRE_HAS_BEEN_RECEIVED_FROM_S1_ALLIANCE_DO_YOU_AGREE = new SystemMessage(666);

    public static final SystemMessage YOU_ARE_REGISTERING_ON_THE_ATTACKING_SIDE_OF_THE_S1_SIEGE_DO_YOU_WANT_TO_CONTINUE = new SystemMessage(667);

    public static final SystemMessage THIS_IS_NOT_THE_TIME_FOR_SIEGE_REGISTRATION_AND_SO_REGISTRATION_AND_CANCELLATION_CANNOT_BE_DONE = new SystemMessage(660);

    public static final SystemMessage IT_IS_A_CHARACTER_THAT_CANNOT_BE_SPOILED = new SystemMessage(661);

    public static final SystemMessage THE_OTHER_PLAYER_IS_REJECTING_FRIEND_INVITATIONS = new SystemMessage(662);

    public static final SystemMessage THE_SIEGE_TIME_HAS_BEEN_DECLARED_FOR_S_IT_IS_NOT_POSSIBLE_TO_CHANGE_THE_TIME_AFTER_A_SIEGE_TIME_HAS_BEEN_DECLARED_DO_YOU_WANT_TO_CONTINUE = new SystemMessage(663);

    public static final SystemMessage THIS_MERCENARY_CANNOT_BE_POSITIONED_ANYMORE = new SystemMessage(656);

    public static final SystemMessage POSITIONING_CANNOT_BE_DONE_HERE_BECAUSE_THE_DISTANCE_BETWEEN_MERCENARIES_IS_TOO_SHORT = new SystemMessage(657);

    public static final SystemMessage THIS_IS_NOT_A_MERCENARY_OF_A_CASTLE_THAT_YOU_OWN_AND_SO_YOU_CANNOT_CANCEL_ITS_POSITIONING = new SystemMessage(658);

    public static final SystemMessage THIS_IS_NOT_THE_TIME_FOR_SIEGE_REGISTRATION_AND_SO_REGISTRATIONS_CANNOT_BE_ACCEPTED_OR_REJECTED = new SystemMessage(659);

    public static final SystemMessage IF_YOU_NEED_HELP_PLEASE_USE_11_INQUIRY_ON_THE_OFFICIAL_WEB_SITE = new SystemMessage(747);

    public static final SystemMessage IT_IS_NOT_CURRENTLY_A_PETITION = new SystemMessage(746);

    public static final SystemMessage YOU_ARE_CURRENTLY_NOT_IN_A_PETITION_CHAT = new SystemMessage(745);

    public static final SystemMessage YOU_FAILED_AT_REMOVING_S1_FROM_THE_PETITION_CHAT_THE_ERROR_CODE_IS_S2 = new SystemMessage(744);

    public static final SystemMessage AS_THERE_IS_A_CONFLICT_IN_THE_SIEGE_RELATIONSHIP_WITH_A_CLAN_IN_THE_ALLIANCE_YOU_CANNOT_INVITE_THAT_CLAN_TO_THE_ALLIANCE = new SystemMessage(751);

    public static final SystemMessage THERE_ARE_NO_OTHER_SKILLS_TO_LEARN = new SystemMessage(750);

    public static final SystemMessage THE_EFFECT_OF_S1_HAS_BEEN_REMOVED = new SystemMessage(749);

    public static final SystemMessage THE_DISTANCE_IS_TOO_FAR_AND_SO_THE_CASTING_HAS_BEEN_STOPPED = new SystemMessage(748);

    public static final SystemMessage YOU_FAILED_AT_CANCELING_A_PETITION_ON_BEHALF_OF_S1_THE_ERROR_CODE_IS_S2 = new SystemMessage(739);

    public static final SystemMessage YOU_HAVE_NOT_SUBMITTED_A_PETITION = new SystemMessage(738);

    public static final SystemMessage YOU_FAILED_AT_SUBMITTING_A_PETITION_ON_BEHALF_OF_S1 = new SystemMessage(737);

    public static final SystemMessage THE_PETITION_WAS_CANCELED_YOU_MAY_SUBMIT_S1_MORE_PETITIONS_TODAY = new SystemMessage(736);

    public static final SystemMessage S1_LEFT_THE_PETITION_CHAT = new SystemMessage(743);

    public static final SystemMessage YOU_FAILED_AT_ADDING_S1_TO_THE_PETITION_CHAT_THE_ERROR_CODE_IS_S2 = new SystemMessage(742);

    public static final SystemMessage YOU_FAILED_AT_ADDING_S1_TO_THE_PETITION_CHAT_A_PETITION_HAS_ALREADY_BEEN_SUBMITTED = new SystemMessage(741);

    public static final SystemMessage S1_PARTICIPATED_IN_A_PETITION_CHAT_AT_THE_REQUEST_OF_THE_GM = new SystemMessage(740);

    public static final SystemMessage S1_ROLLED_S2_AND_S3S_EYE_CAME_OUT = new SystemMessage(762);

    public static final SystemMessage YOU_FAILED_AT_SENDING_THE_PACKAGE_BECAUSE_YOU_ARE_TOO_FAR_FROM_THE_WAREHOUSE = new SystemMessage(763);

    public static final SystemMessage S1_CANNOT_JOIN_THE_CLAN_BECAUSE_ONE_DAY_HAS_NOT_YET_PASSED_SINCE_HE_SHE_LEFT_ANOTHER_CLAN = new SystemMessage(760);

    public static final SystemMessage S1_CLAN_CANNOT_JOIN_THE_ALLIANCE_BECAUSE_ONE_DAY_HAS_NOT_YET_PASSED_SINCE_IT_LEFT_ANOTHER_ALLIANCE = new SystemMessage(761);

    public static final SystemMessage THERE_IS_A_GAMEGUARD_INITIALIZATION_ERROR_PLEASE_TRY_RUNNING_IT_AGAIN_AFTER_REBOOTING = new SystemMessage(766);

    public static final SystemMessage THE_GAMEGUARD_FILE_IS_DAMAGED__PLEASE_REINSTALL_GAMEGUARD = new SystemMessage(767);

    public static final SystemMessage YOU_HAVE_BEEN_PLAYING_FOR_AN_EXTENDED_PERIOD_OF_TIME_PLEASE_CONSIDER_TAKING_A_BREAK = new SystemMessage(764);

    public static final SystemMessage GAMEGUARD_IS_ALREADY_RUNNING_PLEASE_TRY_RUNNING_IT_AGAIN_AFTER_REBOOTING = new SystemMessage(765);

    public static final SystemMessage THERE_ARE_S1_HOURS_AND_S2_MINUTES_LEFT_IN_THIS_WEEKS_USAGE_TIME = new SystemMessage(754);

    public static final SystemMessage THERE_ARE_S1_MINUTES_LEFT_IN_THIS_WEEKS_USAGE_TIME = new SystemMessage(755);

    public static final SystemMessage THAT_NAME_CANNOT_BE_USED = new SystemMessage(752);

    public static final SystemMessage YOU_CANNOT_POSITION_MERCENARIES_HERE = new SystemMessage(753);

    public static final SystemMessage THERE_ARE_S1_MINUTES_LEFT_IN_THIS_WEEKS_PLAY_TIME = new SystemMessage(758);

    public static final SystemMessage THERE_ARE_S1_MINUTES_LEFT_IN_THIS_WEEKS_PLAY_TIME_1 = new SystemMessage(759);

    public static final SystemMessage THIS_WEEKS_USAGE_TIME_HAS_FINISHED = new SystemMessage(756);

    public static final SystemMessage THERE_ARE_S1_HOURS_AND_S2_MINUTES_LEFT_IN_THE_FIXED_USE_TIME = new SystemMessage(757);

    public static final SystemMessage S1_S2_S3_S4S5 = new SystemMessage(713);

    public static final SystemMessage THE_SIEGE_OF_S1_HAS_FINISHED = new SystemMessage(712);

    public static final SystemMessage THE_TRAP_DEVICE_HAS_STOPPED = new SystemMessage(715);

    public static final SystemMessage A_TRAP_DEVICE_HAS_TRIPPED = new SystemMessage(714);

    public static final SystemMessage THE_GUARDIAN_TOWER_HAS_BEEN_DESTROYED_AND_RESURRECTION_IS_NOT_POSSIBLE = new SystemMessage(717);

    public static final SystemMessage IF_A_BASE_CAMP_DOES_NOT_EXIST_RESURRECTION_IS_NOT_POSSIBLE = new SystemMessage(716);

    public static final SystemMessage YOU_FAILED_AT_ITEM_MIXING = new SystemMessage(719);

    public static final SystemMessage THE_CASTLE_GATES_CANNOT_BE_OPENED_AND_CLOSED_DURING_A_SIEGE = new SystemMessage(718);

    public static final SystemMessage YOU_CANNOT_EXCLUDE_YOURSELF = new SystemMessage(705);

    public static final SystemMessage GM_S1 = new SystemMessage(704);

    public static final SystemMessage YOU_CANNOT_TELEPORT_TO_A_VILLAGE_THAT_IS_IN_A_SIEGE = new SystemMessage(707);

    public static final SystemMessage YOU_CAN_ONLY_REGISTER_UP_TO_64_NAMES_ON_YOUR_EXCLUDE_LIST = new SystemMessage(706);

    public static final SystemMessage YOU_DO_NOT_HAVE_THE_RIGHT_TO_USE_THE_CLAN_WAREHOUSE = new SystemMessage(709);

    public static final SystemMessage YOU_DO_NOT_HAVE_THE_RIGHT_TO_USE_THE_CASTLE_WAREHOUSE = new SystemMessage(708);

    public static final SystemMessage THE_SIEGE_OF_S1_HAS_STARTED = new SystemMessage(711);

    public static final SystemMessage ONLY_CLANS_OF_CLAN_LEVEL_1_OR_HIGHER_CAN_USE_A_CLAN_WAREHOUSE = new SystemMessage(710);

    public static final SystemMessage YOU_CANNOT_APPLY_FOR_DISSOLUTION_AGAIN_WITHIN_SEVEN_DAYS_AFTER_A_PREVIOUS_APPLICATION_FOR_DISSOLUTION = new SystemMessage(728);

    public static final SystemMessage THAT_ITEM_CANNOT_BE_DISCARDED = new SystemMessage(729);

    public static final SystemMessage YOU_HAVE_SUBMITTED_S1_PETITIONS_YOU_MAY_SUBMIT_S2_MORE_PETITIONS_TODAY = new SystemMessage(730);

    public static final SystemMessage A_PETITION_HAS_BEEN_RECEIVED_BY_THE_GM_ON_BEHALF_OF_S1_IT_IS_PETITION_S2 = new SystemMessage(731);

    public static final SystemMessage S1_HAS_RECEIVED_A_REQUEST_FOR_A_CONSULTATION_WITH_THE_GM = new SystemMessage(732);

    public static final SystemMessage WE_HAVE_RECEIVED_S1_PETITIONS_FROM_YOU_TODAY_AND_THAT_IS_THE_MAXIMUM_THAT_YOU_CAN_SUBMIT_IN_ONE_DAY_YOU_CANNOT_SUBMIT_ANY_MORE_PETITIONS = new SystemMessage(733);

    public static final SystemMessage YOU_FAILED_AT_SUBMITTING_A_PETITION_ON_BEHALF_OF_SOMEONE_ELSE_S1_ALREADY_SUBMITTED_A_PETITION = new SystemMessage(734);

    public static final SystemMessage YOU_FAILED_AT_SUBMITTING_A_PETITION_ON_BEHALF_OF_S1_THE_ERROR_IS_S2 = new SystemMessage(735);

    public static final SystemMessage THE_PURCHASE_PRICE_IS_HIGHER_THAN_THE_AMOUNT_OF_MONEY_THAT_YOU_HAVE_AND_SO_YOU_CANNOT_OPEN_A_PERSONAL_STORE = new SystemMessage(720);

    public static final SystemMessage YOU_CANNOT_CREATE_AN_ALLIANCE_WHILE_PARTICIPATING_IN_A_SIEGE = new SystemMessage(721);

    public static final SystemMessage YOU_CANNOT_DISSOLVE_AN_ALLIANCE_WHILE_AN_AFFILIATED_CLAN_IS_PARTICIPATING_IN_A_SIEGE_BATTLE = new SystemMessage(722);

    public static final SystemMessage THE_OPPOSING_CLAN_IS_PARTICIPATING_IN_A_SIEGE_BATTLE = new SystemMessage(723);

    public static final SystemMessage YOU_CANNOT_LEAVE_WHILE_PARTICIPATING_IN_A_SIEGE_BATTLE = new SystemMessage(724);

    public static final SystemMessage YOU_CANNOT_BANISH_A_CLAN_FROM_AN_ALLIANCE_WHILE_THE_CLAN_IS_PARTICIPATING_IN_A_SIEGE = new SystemMessage(725);

    public static final SystemMessage THE_FROZEN_CONDITION_HAS_STARTED_PLEASE_WAIT_A_MOMENT = new SystemMessage(726);

    public static final SystemMessage THE_FROZEN_CONDITION_WAS_REMOVED = new SystemMessage(727);

    public static final SystemMessage THE_S1TH_MONSTER_RACE_WILL_BEGIN_IN_30_SECONDS = new SystemMessage(821);

    public static final SystemMessage THE_S2TH_MONSTER_RACE_WILL_BEGIN_IN_S1_MINUTES = new SystemMessage(820);

    public static final SystemMessage THE_RACE_WILL_BEGIN_IN_S1_SECONDS = new SystemMessage(823);

    public static final SystemMessage THE_S1TH_MONSTER_RACE_IS_ABOUT_TO_BEGIN_COUNTDOWN_IN_FIVE_SECONDS = new SystemMessage(822);

    public static final SystemMessage WE_ARE_NOW_SELLING_TICKETS_FOR_THE_S1TH_MONSTER_RACE = new SystemMessage(817);

    public static final SystemMessage TICKETS_ARE_NOW_AVAILABLE_FOR_THE_S1TH_MONSTER_RACE = new SystemMessage(816);

    public static final SystemMessage TICKETS_SALES_ARE_CLOSED_FOR_THE_S1TH_MONSTER_RACE_ODDS_ARE_POSTED = new SystemMessage(819);

    public static final SystemMessage TICKET_SALES_FOR_THE_MONSTER_RACE_WILL_CEASE_IN_S1_MINUTE_S = new SystemMessage(818);

    public static final SystemMessage YOU_CANNOT_RECOMMEND_YOURSELF = new SystemMessage(829);

    public static final SystemMessage ARE_YOU_SURE_YOU_WISH_TO_DELETE_THE_S1_MACRO = new SystemMessage(828);

    public static final SystemMessage YOU_HAVE_BEEN_RECOMMENDED_BY_C1 = new SystemMessage(831);

    public static final SystemMessage YOU_HAVE_RECOMMENDED_C1_YOU_HAVE_S2_RECOMMENDATIONS_LEFT = new SystemMessage(830);

    public static final SystemMessage MONSTER_RACE_S1_IS_FINISHED = new SystemMessage(825);

    public static final SystemMessage THEYRE_OFF = new SystemMessage(824);

    public static final SystemMessage YOU_MAY_NOT_IMPOSE_A_BLOCK_ON_A_GM = new SystemMessage(827);

    public static final SystemMessage FIRST_PRIZE_GOES_TO_THE_PLAYER_IN_LANE_S1_SECOND_PRIZE_GOES_TO_THE_PLAYER_IN_LANE_S2 = new SystemMessage(826);

    public static final SystemMessage SELECT_A_TYPE = new SystemMessage(804);

    public static final SystemMessage PETITIONS_ARE_NOT_BEING_ACCEPTED_AT_THIS_TIME_YOU_MAY_SUBMIT_YOUR_PETITION_AFTER_S1_AM_PM_1 = new SystemMessage(805);

    public static final SystemMessage IF_YOU_ARE_TRAPPED_TRY_TYPING__UNSTUCK = new SystemMessage(806);

    public static final SystemMessage THIS_TERRAIN_IS_UNNAVIGABLE_PREPARE_FOR_TRANSPORT_TO_THE_NEAREST_VILLAGE = new SystemMessage(807);

    public static final SystemMessage YOU_ARE_TOO_LATE_THE_REGISTRATION_PERIOD_IS_OVER = new SystemMessage(800);

    public static final SystemMessage REGISTRATION_FOR_THE_CLAN_HALL_SIEGE_IS_CLOSED = new SystemMessage(801);

    public static final SystemMessage PETITIONS_ARE_NOT_BEING_ACCEPTED_AT_THIS_TIME_YOU_MAY_SUBMIT_YOUR_PETITION_AFTER_S1_AM_PM = new SystemMessage(802);

    public static final SystemMessage ENTER_THE_SPECIFICS_OF_YOUR_PETITION = new SystemMessage(803);

    public static final SystemMessage THE_SECRET_TRAP_HAS_INFLICTED_S1_DAMAGE_ON_YOU = new SystemMessage(812);

    public static final SystemMessage YOU_HAVE_BEEN_POISONED_BY_A_SECRET_TRAP = new SystemMessage(813);

    public static final SystemMessage YOUR_SPEED_HAS_BEEN_DECREASED_BY_A_SECRET_TRAP = new SystemMessage(814);

    public static final SystemMessage THE_TRYOUTS_ARE_ABOUT_TO_BEGIN_LINE_UP = new SystemMessage(815);

    public static final SystemMessage YOU_ARE_STUCK_YOU_MAY_SUBMIT_A_PETITION_BY_TYPING__GM = new SystemMessage(808);

    public static final SystemMessage YOU_ARE_STUCK_YOU_WILL_BE_TRANSPORTED_TO_THE_NEAREST_VILLAGE_IN_FIVE_MINUTES = new SystemMessage(809);

    public static final SystemMessage INVALID_MACRO_REFER_TO_THE_HELP_FILE_FOR_INSTRUCTIONS = new SystemMessage(810);

    public static final SystemMessage YOU_WILL_BE_MOVED_TO_S1_DO_YOU_WISH_TO_CONTINUE = new SystemMessage(811);

    public static final SystemMessage THE_FINAL_MATCH_IS_ABOUT_TO_BEGIN_LINE_UP = new SystemMessage(791);

    public static final SystemMessage THE_FINALS_HAVE_BEGUN = new SystemMessage(790);

    public static final SystemMessage THE_TRYOUTS_HAVE_BEGUN = new SystemMessage(789);

    public static final SystemMessage THE_FINALS_ARE_FINISHED = new SystemMessage(788);

    public static final SystemMessage THE_TRYOUTS_ARE_FINISHED = new SystemMessage(787);

    public static final SystemMessage INCORRECT_SYNTAX = new SystemMessage(786);

    public static final SystemMessage THE_RESULTS_OF_LOTTERY_NUMBER_S1_HAVE_NOT_YET_BEEN_PUBLISHED = new SystemMessage(785);

    public static final SystemMessage TICKETS_FOR_THE_CURRENT_LOTTERY_ARE_NO_LONGER_AVAILABLE = new SystemMessage(784);

    public static final SystemMessage THE_OBSERVATION_TIME_HAS_EXPIRED = new SystemMessage(799);

    public static final SystemMessage ITEM_REGISTRATION_IS_IRREVERSIBLE_DO_YOU_WISH_TO_CONTINUE = new SystemMessage(798);

    public static final SystemMessage YOU_MAY_CREATE_UP_TO_48_MACROS = new SystemMessage(797);

    public static final SystemMessage YOUR_REMAINING_OBSERVATION_TIME_IS_S1_MINUTES = new SystemMessage(796);

    public static final SystemMessage ONLY_CLAN_LEADERS_ARE_AUTHORIZED_TO_SET_RIGHTS = new SystemMessage(795);

    public static final SystemMessage YOU_ARE_NOT_AUTHORIZED_TO_DO_THAT = new SystemMessage(794);

    public static final SystemMessage THE_SIEGE_OF_THE_CLAN_HALL_HAS_BEGUN = new SystemMessage(793);

    public static final SystemMessage THE_SIEGE_OF_THE_CLAN_HALL_IS_FINISHED = new SystemMessage(792);

    public static final SystemMessage SINCE_THIS_IS_A_PEACE_ZONE_PLAY_TIME_DOES_NOT_GET_EXPENDED_HERE = new SystemMessage(774);

    public static final SystemMessage FROM_HERE_ON_PLAY_TIME_WILL_BE_EXPENDED = new SystemMessage(775);

    public static final SystemMessage THERE_WAS_A_PROBLEM_WHEN_RUNNING_GAMEGUARD = new SystemMessage(772);

    public static final SystemMessage THE_GAME_OR_GAMEGUARD_FILES_ARE_DAMAGED = new SystemMessage(773);

    public static final SystemMessage THE_GAMEGUARD_UPDATE_WAS_CANCELED_PLEASE_CHECK_YOUR_NETWORK_CONNECTION_STATUS_OR_FIREWALL = new SystemMessage(770);

    public static final SystemMessage THE_GAMEGUARD_UPDATE_WAS_CANCELED_PLEASE_TRY_RUNNING_IT_AGAIN_AFTER_DOING_A_VIRUS_SCAN_OR_CHANGING_THE_SETTINGS_IN_YOUR_PC_MANAGEMENT_PROGRAM = new SystemMessage(771);

    public static final SystemMessage A_WINDOWS_SYSTEM_FILE_IS_DAMAGED_PLEASE_REINSTALL_INTERNET_EXPLORER = new SystemMessage(768);

    public static final SystemMessage A_HACKING_TOOL_HAS_BEEN_DISCOVERED_PLEASE_TRY_PLAYING_AGAIN_AFTER_CLOSING_UNNECESSARY_PROGRAMS = new SystemMessage(769);

    public static final SystemMessage YOU_MAY_NOT_OBSERVE_A_SUMMONED_CREATURE = new SystemMessage(782);

    public static final SystemMessage LOTTERY_TICKET_SALES_HAVE_BEEN_TEMPORARILY_SUSPENDED = new SystemMessage(783);

    public static final SystemMessage OBSERVATION_IS_ONLY_POSSIBLE_DURING_A_SIEGE = new SystemMessage(780);

    public static final SystemMessage OBSERVERS_CANNOT_PARTICIPATE = new SystemMessage(781);

    public static final SystemMessage YOU_MAY_NOT_LOG_OUT_FROM_THIS_LOCATION = new SystemMessage(778);

    public static final SystemMessage YOU_MAY_NOT_RESTART_IN_THIS_LOCATION = new SystemMessage(779);

    public static final SystemMessage THE_CLAN_HALL_WHICH_WAS_PUT_UP_FOR_AUCTION_HAS_BEEN_AWARDED_TO_S1_CLAN = new SystemMessage(776);

    public static final SystemMessage THE_CLAN_HALL_WHICH_HAD_BEEN_PUT_UP_FOR_AUCTION_WAS_NOT_SOLD_AND_THEREFORE_HAS_BEEN_RELISTED = new SystemMessage(777);

    public static final SystemMessage THERE_IS_A_DISCREPANCY_ON_THE_INVOICE = new SystemMessage(881);

    public static final SystemMessage THE_TRANSACTION_IS_COMPLETE = new SystemMessage(880);

    public static final SystemMessage SEED_INFORMATION_IS_INCORRECT = new SystemMessage(883);

    public static final SystemMessage SEED_QUANTITY_IS_INCORRECT = new SystemMessage(882);

    public static final SystemMessage THE_NUMBER_OF_CROPS_IS_INCORRECT = new SystemMessage(885);

    public static final SystemMessage THE_MANOR_INFORMATION_HAS_BEEN_UPDATED = new SystemMessage(884);

    public static final SystemMessage THE_TYPE_IS_INCORRECT = new SystemMessage(887);

    public static final SystemMessage THE_CROPS_ARE_PRICED_INCORRECTLY = new SystemMessage(886);

    public static final SystemMessage THE_SEED_WAS_SUCCESSFULLY_SOWN = new SystemMessage(889);

    public static final SystemMessage NO_CROPS_CAN_BE_PURCHASED_AT_THIS_TIME = new SystemMessage(888);

    public static final SystemMessage YOU_ARE_NOT_AUTHORIZED_TO_HARVEST = new SystemMessage(891);

    public static final SystemMessage THE_SEED_WAS_NOT_SOWN = new SystemMessage(890);

    public static final SystemMessage THE_HARVEST_FAILED_BECAUSE_THE_SEED_WAS_NOT_SOWN = new SystemMessage(893);

    public static final SystemMessage THE_HARVEST_HAS_FAILED = new SystemMessage(892);

    public static final SystemMessage NO_RECIPES_HAVE_BEEN_REGISTERED = new SystemMessage(895);

    public static final SystemMessage UP_TO_S1_RECIPES_CAN_BE_REGISTERED = new SystemMessage(894);

    public static final SystemMessage YOU_FEEL_THE_ENERGY_OF_WATER = new SystemMessage(864);

    public static final SystemMessage YOU_FEEL_THE_ENERGY_OF_WIND = new SystemMessage(865);

    public static final SystemMessage YOU_MAY_NO_LONGER_GATHER_ENERGY = new SystemMessage(866);

    public static final SystemMessage THE_ENERGY_IS_DEPLETED = new SystemMessage(867);

    public static final SystemMessage THE_ENERGY_OF_FIRE_HAS_BEEN_DELIVERED = new SystemMessage(868);

    public static final SystemMessage THE_ENERGY_OF_WATER_HAS_BEEN_DELIVERED = new SystemMessage(869);

    public static final SystemMessage THE_ENERGY_OF_WIND_HAS_BEEN_DELIVERED = new SystemMessage(870);

    public static final SystemMessage THE_SEED_HAS_BEEN_SOWN = new SystemMessage(871);

    public static final SystemMessage THIS_SEED_MAY_NOT_BE_SOWN_HERE = new SystemMessage(872);

    public static final SystemMessage THAT_CHARACTER_DOES_NOT_EXIST = new SystemMessage(873);

    public static final SystemMessage THE_CAPACITY_OF_THE_WAREHOUSE_HAS_BEEN_EXCEEDED = new SystemMessage(874);

    public static final SystemMessage TRANSPORT_OF_CARGO_HAS_BEEN_CANCELED = new SystemMessage(875);

    public static final SystemMessage CARGO_WAS_NOT_DELIVERED = new SystemMessage(876);

    public static final SystemMessage THE_SYMBOL_HAS_BEEN_ADDED = new SystemMessage(877);

    public static final SystemMessage THE_SYMBOL_HAS_BEEN_DELETED = new SystemMessage(878);

    public static final SystemMessage THE_MANOR_SYSTEM_IS_CURRENTLY_UNDER_MAINTENANCE = new SystemMessage(879);

    public static final SystemMessage S1_HAS_BEEN_ADDED = new SystemMessage(851);

    public static final SystemMessage S1_ALREADY_EXISTS_1 = new SystemMessage(850);

    public static final SystemMessage S1_CANNOT_BE_FOUND = new SystemMessage(849);

    public static final SystemMessage S1_HAS_BEEN_DELETED = new SystemMessage(848);

    public static final SystemMessage S1_CLAN_HAS_DEFEATED_S2 = new SystemMessage(855);

    public static final SystemMessage YOU_LACK_S2_OF_S1 = new SystemMessage(854);

    public static final SystemMessage YOU_MAY_NOT_ALTER_YOUR_RECIPE_BOOK_WHILE_ENGAGED_IN_MANUFACTURING = new SystemMessage(853);

    public static final SystemMessage THE_RECIPE_IS_INCORRECT = new SystemMessage(852);

    public static final SystemMessage PLEASE_REGISTER_A_RECIPE = new SystemMessage(859);

    public static final SystemMessage THE_PRELIMINARY_MATCH_OF_S1_HAS_ENDED_IN_A_DRAW = new SystemMessage(858);

    public static final SystemMessage S1_CLAN_HAS_WON_IN_THE_PRELIMINARY_MATCH_OF_S2 = new SystemMessage(857);

    public static final SystemMessage THE_SIEGE_OF_S1_HAS_ENDED_IN_A_DRAW = new SystemMessage(856);

    public static final SystemMessage YOU_FEEL_THE_ENERGY_OF_FIRE = new SystemMessage(863);

    public static final SystemMessage ODDS_ARE_NOT_POSTED_UNTIL_TICKET_SALES_HAVE_CLOSED = new SystemMessage(862);

    public static final SystemMessage YOU_HAVE_EXCEEDED_THE_MAXIMUM_NUMBER_OF_MEMOS = new SystemMessage(861);

    public static final SystemMessage YOU_MAY_NOT_BUILD_YOUR_HEADQUARTERS_IN_CLOSE_PROXIMITY_TO_ANOTHER_HEADQUARTERS = new SystemMessage(860);

    public static final SystemMessage S1_HAS_ROLLED_S2 = new SystemMessage(834);

    public static final SystemMessage YOU_MAY_NOT_THROW_THE_DICE_AT_THIS_TIMETRY_AGAIN_LATER = new SystemMessage(835);

    public static final SystemMessage THAT_CHARACTER_HAS_ALREADY_BEEN_RECOMMENDED = new SystemMessage(832);

    public static final SystemMessage YOU_ARE_NOT_AUTHORIZED_TO_MAKE_FURTHER_RECOMMENDATIONS_AT_THIS_TIME_YOU_WILL_RECEIVE_MORE_RECOMMENDATION_CREDITS_EACH_DAY_AT_1_PM = new SystemMessage(833);

    public static final SystemMessage ENTER_THE_NAME_OF_THE_MACRO = new SystemMessage(838);

    public static final SystemMessage THAT_NAME_IS_ALREADY_ASSIGNED_TO_ANOTHER_MACRO = new SystemMessage(839);

    public static final SystemMessage THE_INVENTORY_IS_FULL_NO_FURTHER_QUEST_ITEMS_MAY_BE_DEPOSITED_AT_THIS_TIME = new SystemMessage(836);

    public static final SystemMessage MACRO_DESCRIPTIONS_MAY_CONTAIN_UP_TO_32_CHARACTERS = new SystemMessage(837);

    public static final SystemMessage YOU_ARE_NOT_AUTHORIZED_TO_REGISTER_A_RECIPE = new SystemMessage(842);

    public static final SystemMessage THE_SIEGE_OF_S1_IS_FINISHED = new SystemMessage(843);

    public static final SystemMessage THAT_RECIPE_IS_ALREADY_REGISTERED = new SystemMessage(840);

    public static final SystemMessage NO_FURTHER_RECIPES_MAY_BE_REGISTERED = new SystemMessage(841);

    public static final SystemMessage THE_SIEGE_OF_S1_HAS_BEEN_CANCELED_DUE_TO_LACK_OF_INTEREST = new SystemMessage(846);

    public static final SystemMessage A_CLAN_THAT_OWNS_A_CLAN_HALL_MAY_NOT_PARTICIPATE_IN_A_CLAN_HALL_SIEGE = new SystemMessage(847);

    public static final SystemMessage THE_SIEGE_TO_CONQUER_S1_HAS_BEGUN = new SystemMessage(844);

    public static final SystemMessage THE_DEADLINE_TO_REGISTER_FOR_THE_SIEGE_OF_S1_HAS_PASSED = new SystemMessage(845);

    public static final SystemMessage RESTART_AT_THE_COLISEUM = new SystemMessage(956);

    public static final SystemMessage RESTART_AT_HEINE = new SystemMessage(957);

    public static final SystemMessage ITEMS_CANNOT_BE_DISCARDED_OR_DESTROYED_WHILE_OPERATING_A_PRIVATE_STORE_OR_WORKSHOP = new SystemMessage(958);

    public static final SystemMessage S1_S2_MANUFACTURING_SUCCESS = new SystemMessage(959);

    public static final SystemMessage RESTART_AT_DWARVEN_VILLAGE = new SystemMessage(952);

    public static final SystemMessage RESTART_AT_THE_TOWN_OF_OREN = new SystemMessage(953);

    public static final SystemMessage RESTART_AT_HUNTERS_VILLAGE = new SystemMessage(954);

    public static final SystemMessage RESTART_AT_ADEN_CASTLE_TOWN = new SystemMessage(955);

    public static final SystemMessage RESTART_AT_FLORAN_VILLAGE = new SystemMessage(948);

    public static final SystemMessage RESTART_AT_GIRAN_CASTLE_TOWN = new SystemMessage(949);

    public static final SystemMessage RESTART_AT_GIRAN_HARBOR = new SystemMessage(950);

    public static final SystemMessage RESTART_AT_ORC_VILLAGE = new SystemMessage(951);

    public static final SystemMessage RESTART_AT_THE_NEUTRAL_ZONE = new SystemMessage(944);

    public static final SystemMessage RESTART_AT_ELVEN_VILLAGE = new SystemMessage(945);

    public static final SystemMessage RESTART_AT_DARK_ELVEN_VILLAGE = new SystemMessage(946);

    public static final SystemMessage RESTART_AT_DION_CASTLE_TOWN = new SystemMessage(947);

    public static final SystemMessage RESTART_AT_TALKING_ISLAND_VILLAGE = new SystemMessage(941);

    public static final SystemMessage S1_IS_BLOCKING_EVERYTHING = new SystemMessage(940);

    public static final SystemMessage RESTART_AT_GLUDIN_CASTLE_TOWN = new SystemMessage(943);

    public static final SystemMessage RESTART_AT_GLUDIN_VILLAGE = new SystemMessage(942);

    public static final SystemMessage CURRENTLY_PREPARING_FOR_PRIVATE_WORKSHOP = new SystemMessage(937);

    public static final SystemMessage USE_S1 = new SystemMessage(936);

    public static final SystemMessage YOU_CANNOT_EXCHANGE_WHILE_BLOCKING_EVERYTHING = new SystemMessage(939);

    public static final SystemMessage THE_COMMUNITY_SERVER_IS_CURRENTLY_OFFLINE = new SystemMessage(938);

    public static final SystemMessage THE_SEED_PRICING_GREATLY_DIFFERS_FROM_STANDARD_SEED_PRICES = new SystemMessage(933);

    public static final SystemMessage YOU_CANNOT_CHAT_LOCALLY_WHILE_OBSERVING = new SystemMessage(932);

    public static final SystemMessage THE_AMOUNT_IS_NOT_SUFFICIENT_AND_SO_THE_MANOR_IS_NOT_IN_OPERATION = new SystemMessage(935);

    public static final SystemMessage IT_IS_A_DELETED_RECIPE = new SystemMessage(934);

    public static final SystemMessage NO_COMPENSATION_WAS_GIVEN_FOR_THE_FARM_PRODUCTS = new SystemMessage(929);

    public static final SystemMessage THE_CURRENT_TIME_IS_S1S2_IN_THE_NIGHT = new SystemMessage(928);

    public static final SystemMessage THE_WINNING_LOTTERY_TICKET_NUMBER_HAS_NOT_YET_BEEN_ANNOUNCED = new SystemMessage(931);

    public static final SystemMessage LOTTERY_TICKETS_ARE_NOT_CURRENTLY_BEING_SOLD = new SystemMessage(930);

    public static final SystemMessage CURRENT_LOCATION__S1_S2_S3_NEAR_HEINE = new SystemMessage(926);

    public static final SystemMessage THE_CURRENT_TIME_IS_S1S2_IN_THE_DAY = new SystemMessage(927);

    public static final SystemMessage CURRENT_LOCATION__S1_S2_S3_NEAR_ADEN_CASTLE_TOWN = new SystemMessage(924);

    public static final SystemMessage CURRENT_LOCATION__S1_S2_S3_NEAR_THE_COLISEUM = new SystemMessage(925);

    public static final SystemMessage CURRENT_LOCATION__S1_S2_S3_NEAR_THE_TOWN_OF_OREN = new SystemMessage(922);

    public static final SystemMessage CURRENT_LOCATION__S1_S2_S3_NEAR_HUNTERS_VILLAGE = new SystemMessage(923);

    public static final SystemMessage CURRENT_LOCATION__S1_S2_S3_NEAR_ORC_VILLAGE = new SystemMessage(920);

    public static final SystemMessage CURRENT_LOCATION__S1_S2_S3_NEAR_DWARVEN_VILLAGE = new SystemMessage(921);

    public static final SystemMessage CURRENT_LOCATION__S1_S2_S3_NEAR_GIRAN_CASTLE_TOWN = new SystemMessage(918);

    public static final SystemMessage CURRENT_LOCATION__S1_S2_S3_NEAR_GIRAN_HARBOR = new SystemMessage(919);

    public static final SystemMessage CURRENT_LOCATION__S1_S2_S3_NEAR_DION_CASTLE_TOWN = new SystemMessage(916);

    public static final SystemMessage CURRENT_LOCATION__S1_S2_S3_NEAR_FLORAN_VILLAGE = new SystemMessage(917);

    public static final SystemMessage CURRENT_LOCATION__S1_S2_S3_NEAR_ELVEN_VILLAGE = new SystemMessage(914);

    public static final SystemMessage CURRENT_LOCATION__S1_S2_S3_NEAR_DARK_ELVEN_VILLAGE = new SystemMessage(915);

    public static final SystemMessage CURRENT_LOCATION__S1_S2_S3_NEAR_GLUDIO_CASTLE_TOWN = new SystemMessage(912);

    public static final SystemMessage CURRENT_LOCATION__S1_S2_S3_NEAR_THE_NEUTRAL_ZONE = new SystemMessage(913);

    public static final SystemMessage CURRENT_LOCATION__S1_S2_S3_NEAR_GLUDIN_VILLAGE = new SystemMessage(911);

    public static final SystemMessage CURRENT_LOCATION__S1_S2_S3_NEAR_TALKING_ISLAND_VILLAGE = new SystemMessage(910);

    public static final SystemMessage THE_CURRENT_SCORE_FOR_THE_DWARVEN_RACE_IS_S1 = new SystemMessage(909);

    public static final SystemMessage THE_CURRENT_SCORE_FOR_THE_ORC_RACE_IS_S1 = new SystemMessage(908);

    public static final SystemMessage THE_CURRENT_SCORE_FOR_THE_DARK_ELVEN_RACE_IS_S1 = new SystemMessage(907);

    public static final SystemMessage THE_CURRENT_SCORE_FOR_THE_ELVEN_RACE_IS_S1 = new SystemMessage(906);

    public static final SystemMessage THE_CURRENT_SCORE_FOR_THE_HUMAN_RACE_IS_S1 = new SystemMessage(905);

    public static final SystemMessage ITEMS_CANNOT_BE_DISCARDED_WHILE_IN_PRIVATE_STORE_STATUS = new SystemMessage(904);

    public static final SystemMessage YOU_MAY_NOT_SUBMIT_A_PETITION_WHILE_FROZEN_BE_PATIENT = new SystemMessage(903);

    public static final SystemMessage THE_NUMBER_OF_ITEMS_IS_INCORRECT = new SystemMessage(902);

    public static final SystemMessage THE_SYMBOL_INFORMATION_CANNOT_BE_FOUND = new SystemMessage(901);

    public static final SystemMessage NO_SLOT_EXISTS_TO_DRAW_THE_SYMBOL = new SystemMessage(900);

    public static final SystemMessage THE_SYMBOL_CANNOT_BE_DRAWN = new SystemMessage(899);

    public static final SystemMessage ONLY_CHARACTERS_OF_LEVEL_10_OR_ABOVE_ARE_AUTHORIZED_TO_MAKE_RECOMMENDATIONS = new SystemMessage(898);

    public static final SystemMessage THE_FEE_TO_CREATE_THE_ITEM_IS_INCORRECT = new SystemMessage(897);

    public static final SystemMessage QUEST_RECIPES_CAN_NOT_BE_REGISTERED = new SystemMessage(896);

    public static final SystemMessage THE_PET_RECEIVED_DAMAGE_OF_S2_CAUSED_BY_S1 = new SystemMessage(1016);

    public static final SystemMessage PETS_CRITICAL_HIT = new SystemMessage(1017);

    public static final SystemMessage THE_PET_USES_S1 = new SystemMessage(1018);

    public static final SystemMessage YOUR_PET_USES_S1 = new SystemMessage(1019);

    public static final SystemMessage THE_PET_GAVE_S1 = new SystemMessage(1020);

    public static final SystemMessage THE_PET_GAVE_S2_S1_S = new SystemMessage(1021);

    public static final SystemMessage THE_PET_GAVE__S1_S2 = new SystemMessage(1022);

    public static final SystemMessage THE_PET_GAVE_S1_ADENA = new SystemMessage(1023);

    public static final SystemMessage A_HUNGRY_STRIDER_CANNOT_BE_MOUNTED_OR_DISMOUNTED = new SystemMessage(1008);

    public static final SystemMessage A_STRIDER_CANNOT_BE_RIDDEN_WHEN_DEAD = new SystemMessage(1009);

    public static final SystemMessage A_DEAD_PET_CANNOT_BE_RIDDEN = new SystemMessage(1010);

    public static final SystemMessage A_STRIDER_IN_BATTLE_CANNOT_BE_RIDDEN = new SystemMessage(1011);

    public static final SystemMessage A_STRIDER_CANNOT_BE_RIDDEN_WHILE_IN_BATTLE = new SystemMessage(1012);

    public static final SystemMessage A_STRIDER_CAN_BE_RIDDEN_ONLY_WHEN_STANDING = new SystemMessage(1013);

    public static final SystemMessage THE_PET_ACQUIRED_EXPERIENCE_POINTS_OF_S1 = new SystemMessage(1014);

    public static final SystemMessage THE_PET_GAVE_DAMAGE_OF_S1 = new SystemMessage(1015);

    public static final SystemMessage INNADRIL_PLEASURE_BOAT_IS_LEAVING_SOON = new SystemMessage(1001);

    public static final SystemMessage THE_INNADRIL_PLEASURE_BOAT_WILL_LEAVE_IN_ONE_MINUTE = new SystemMessage(1000);

    public static final SystemMessage CANNOT_PROCESS_A_MONSTER_RACE_TICKET = new SystemMessage(1003);

    public static final SystemMessage INNADRIL_PLEASURE_BOAT_IS_LEAVING = new SystemMessage(1002);

    public static final SystemMessage THERE_IS_NOT_ENOUGH_ADENA_IN_THE_CLAN_HALL_WAREHOUSE = new SystemMessage(1005);

    public static final SystemMessage YOU_HAVE_REGISTERED_FOR_A_CLAN_HALL_AUCTION = new SystemMessage(1004);

    public static final SystemMessage THE_PRELIMINARY_MATCH_REGISTRATION_OF_S1_HAS_FINISHED = new SystemMessage(1007);

    public static final SystemMessage YOU_HAVE_BID_IN_A_CLAN_HALL_AUCTION = new SystemMessage(1006);

    public static final SystemMessage WILL_LEAVE_FOR_GIRAN_HARBOR_AFTER_ANCHORING_FOR_TEN_MINUTES = new SystemMessage(993);

    public static final SystemMessage ARRIVED_AT_GIRAN_HARBOR = new SystemMessage(992);

    public static final SystemMessage WILL_LEAVE_FOR_GIRAN_HARBOR_IN_ONE_MINUTE = new SystemMessage(995);

    public static final SystemMessage WILL_LEAVE_FOR_GIRAN_HARBOR_IN_FIVE_MINUTES = new SystemMessage(994);

    public static final SystemMessage LEAVING_FOR_GIRAN_HARBOR = new SystemMessage(997);

    public static final SystemMessage LEAVING_SOON_FOR_GIRAN_HARBOR = new SystemMessage(996);

    public static final SystemMessage THE_INNADRIL_PLEASURE_BOAT_WILL_LEAVE_IN_FIVE_MINUTES = new SystemMessage(999);

    public static final SystemMessage THE_INNADRIL_PLEASURE_BOAT_HAS_ARRIVED_IT_WILL_ANCHOR_FOR_TEN_MINUTES = new SystemMessage(998);

    public static final SystemMessage ARRIVED_AT_GLUDIN_HARBOR = new SystemMessage(986);

    public static final SystemMessage WILL_LEAVE_FOR_TALKING_ISLAND_HARBOR_AFTER_ANCHORING_FOR_TEN_MINUTES = new SystemMessage(987);

    public static final SystemMessage LEAVING_SOON_FOR_GLUDIN_HARBOR = new SystemMessage(984);

    public static final SystemMessage LEAVING_FOR_GLUDIN_HARBOR = new SystemMessage(985);

    public static final SystemMessage LEAVING_SOON_FOR_TALKING_ISLAND_HARBOR = new SystemMessage(990);

    public static final SystemMessage LEAVING_FOR_TALKING_ISLAND_HARBOR = new SystemMessage(991);

    public static final SystemMessage WILL_LEAVE_FOR_TALKING_ISLAND_HARBOR_IN_FIVE_MINUTES = new SystemMessage(988);

    public static final SystemMessage WILL_LEAVE_FOR_TALKING_ISLAND_HARBOR_IN_ONE_MINUTE = new SystemMessage(989);

    public static final SystemMessage THE_SOUL_CRYSTAL_IS_REFUSING_TO_ABSORB_A_SOUL = new SystemMessage(978);

    public static final SystemMessage ARRIVED_AT_TALKING_ISLAND_HARBOR = new SystemMessage(979);

    public static final SystemMessage THE_SOUL_CRYSTAL_BROKE_BECAUSE_IT_WAS_NOT_ABLE_TO_ENDURE_THE_SOUL_ENERGY = new SystemMessage(976);

    public static final SystemMessage THE_SOUL_CRYSTALS_CAUSED_RESONATION_AND_FAILED_AT_ABSORBING_A_SOUL = new SystemMessage(977);

    public static final SystemMessage WILL_LEAVE_FOR_GLUDIN_HARBOR_IN_ONE_MINUTE = new SystemMessage(982);

    public static final SystemMessage THOSE_WISHING_TO_RIDE_SHOULD_MAKE_HASTE_TO_GET_ON = new SystemMessage(983);

    public static final SystemMessage WILL_LEAVE_FOR_GLUDIN_HARBOR_AFTER_ANCHORING_FOR_TEN_MINUTES = new SystemMessage(980);

    public static final SystemMessage WILL_LEAVE_FOR_GLUDIN_HARBOR_IN_FIVE_MINUTES = new SystemMessage(981);

    public static final SystemMessage PETITIONS_CANNOT_EXCEED_255_CHARACTERS = new SystemMessage(971);

    public static final SystemMessage S2S_MP_HAS_BEEN_DRAINED_BY_S1 = new SystemMessage(970);

    public static final SystemMessage DO_YOU_ACCEPT_THE_PARTY_INVITATION_FROM_S1_ITEM_DISTRIBUTION_BY_TURN_INCLUDING_SPOIL = new SystemMessage(969);

    public static final SystemMessage DO_YOU_ACCEPT_THE_PARTY_INVITATION_FROM_S1_ITEM_DISTRIBUTION_BY_TURN = new SystemMessage(968);

    public static final SystemMessage THE_SOUL_CRYSTAL_WAS_NOT_ABLE_TO_ABSORB_A_SOUL = new SystemMessage(975);

    public static final SystemMessage THE_SOUL_CRYSTAL_SUCCEEDED_IN_ABSORBING_A_SOUL = new SystemMessage(974);

    public static final SystemMessage PLEASE_INPUT_NO_MORE_THAN_THE_NUMBER_YOU_HAVE = new SystemMessage(973);

    public static final SystemMessage PETS_CANNOT_USE_THIS_ITEM = new SystemMessage(972);

    public static final SystemMessage PLEASE_DETERMINE_THE_MANUFACTURING_PRICE = new SystemMessage(963);

    public static final SystemMessage YOU_ARE_NO_LONGER_BLOCKING_EVERYTHING = new SystemMessage(962);

    public static final SystemMessage YOU_ARE_NOW_BLOCKING_EVERYTHING = new SystemMessage(961);

    public static final SystemMessage S1_MANUFACTURING_FAILURE = new SystemMessage(960);

    public static final SystemMessage DO_YOU_ACCEPT_THE_PARTY_INVITATION_FROM_S1_ITEM_DISTRIBUTION_RANDOM_INCLUDING_SPOIL = new SystemMessage(967);

    public static final SystemMessage CHATTING_IS_CURRENTLY_PROHIBITED_IF_YOU_TRY_TO_CHAT_BEFORE_THE_PROHIBITION_IS_REMOVED_THE_PROHIBITION_TIME_WILL_BECOME_EVEN_LONGER = new SystemMessage(966);

    public static final SystemMessage THE_CHATTING_PROHIBITION_HAS_BEEN_REMOVED = new SystemMessage(965);

    public static final SystemMessage CHATTING_IS_PROHIBITED_FOR_ABOUT_ONE_MINUTE = new SystemMessage(964);

    public static final SystemMessage XDO_YOU_WISH_TO_USE_THE_HAIR_STYLE_CHANGE_POTION_X_TYPE_G_IT_IS_PERMANENT = new SystemMessage(1100);

    public static final SystemMessage THE_FACELIFTING_POTION__TYPE_B_IS_BEING_USED = new SystemMessage(1101);

    public static final SystemMessage THE_FACELIFTING_POTION__TYPE_C_IS_BEING_USED = new SystemMessage(1102);

    public static final SystemMessage THE_DYE_POTION__TYPE_B_IS_BEING_USED = new SystemMessage(1103);

    public static final SystemMessage XDO_YOU_WISH_TO_USE_THE_HAIR_STYLE_CHANGE_POTION_X_TYPE_C_IT_IS_PERMANENT = new SystemMessage(1096);

    public static final SystemMessage XDO_YOU_WISH_TO_USE_THE_HAIR_STYLE_CHANGE_POTION_X_TYPE_D_IT_IS_PERMANENT = new SystemMessage(1097);

    public static final SystemMessage XDO_YOU_WISH_TO_USE_THE_HAIR_STYLE_CHANGE_POTION_X_TYPE_E_IT_IS_PERMANENT = new SystemMessage(1098);

    public static final SystemMessage XDO_YOU_WISH_TO_USE_THE_HAIR_STYLE_CHANGE_POTION_X_TYPE_F_IT_IS_PERMANENT = new SystemMessage(1099);

    public static final SystemMessage XDO_YOU_WISH_TO_USE_THE_DYE_POTION_X_TYPE_B_IT_IS_PERMANENT = new SystemMessage(1092);

    public static final SystemMessage XDO_YOU_WISH_TO_USE_THE_DYE_POTION_X_TYPE_C_IT_IS_PERMANENT = new SystemMessage(1093);

    public static final SystemMessage XDO_YOU_WISH_TO_USE_THE_DYE_POTION_X_TYPE_D_IT_IS_PERMANENT = new SystemMessage(1094);

    public static final SystemMessage XDO_YOU_WISH_TO_USE_THE_HAIR_STYLE_CHANGE_POTION_X_TYPE_B_IT_IS_PERMANENT = new SystemMessage(1095);

    public static final SystemMessage YOUR_HAIR_STYLE_HAS_BEEN_CHANGED = new SystemMessage(1088);

    public static final SystemMessage S1_HAS_OBTAINED_A_FIRST_ANNIVERSARY_COMMEMORATIVE_ITEM = new SystemMessage(1089);

    public static final SystemMessage DO_YOU_WISH_TO_USE_THE_FACELIFTING_POTION_X_TYPE_B_IT_IS_PERMANENT = new SystemMessage(1090);

    public static final SystemMessage XDO_YOU_WISH_TO_USE_THE_FACELIFTING_POTION_X_TYPE_C_IT_IS_PERMANENT = new SystemMessage(1091);

    public static final SystemMessage A_CLAN_MEMBER_MAY_NOT_BE_DISMISSED_DURING_COMBAT = new SystemMessage(1117);

    public static final SystemMessage ONE_CANNOT_LEAVE_ONES_CLAN_DURING_COMBAT = new SystemMessage(1116);

    public static final SystemMessage QUEST_WAS_AUTOMATICALLY_CANCELED_WHEN_YOU_ATTEMPTED_TO_SETTLE_THE_ACCOUNTS_OF_YOUR_QUEST_WHILE_YOUR_INVENTORY_EXCEEDED_80_PERCENT_OF_CAPACITY = new SystemMessage(1119);

    public static final SystemMessage PROGRESS_IN_A_QUEST_IS_POSSIBLE_ONLY_WHEN_YOUR_INVENTORYS_WEIGHT_AND_VOLUME_ARE_LESS_THAN_80_PERCENT_OF_CAPACITY = new SystemMessage(1118);

    public static final SystemMessage THE_PRIZE_AMOUNT_FOR_LUCKY_LOTTERY__S1__IS_S2_ADENA_THERE_WAS_NO_FIRST_PRIZE_WINNER_IN_THIS_DRAWING_THEREFORE_THE_JACKPOT_WILL_BE_ADDED_TO_THE_NEXT_DRAWING = new SystemMessage(1113);

    public static final SystemMessage THE_PRIZE_AMOUNT_FOR_THE_WINNER_OF_LOTTERY__S1__IS_S2_ADENA_WE_HAVE_S3_FIRST_PRIZE_WINNERS = new SystemMessage(1112);

    public static final SystemMessage INDIVIDUALS_MAY_NOT_SURRENDER_DURING_COMBAT = new SystemMessage(1115);

    public static final SystemMessage YOUR_CLAN_MAY_NOT_REGISTER_TO_PARTICIPATE_IN_A_SIEGE_WHILE_UNDER_A_GRACE_PERIOD_OF_THE_CLANS_DISSOLUTION = new SystemMessage(1114);

    public static final SystemMessage THE_HAIR_STYLE_CHANGE_POTION__TYPE_E_IS_BEING_USED = new SystemMessage(1109);

    public static final SystemMessage THE_HAIR_STYLE_CHANGE_POTION__TYPE_D_IS_BEING_USED = new SystemMessage(1108);

    public static final SystemMessage THE_HAIR_STYLE_CHANGE_POTION__TYPE_G_IS_BEING_USED = new SystemMessage(1111);

    public static final SystemMessage THE_HAIR_STYLE_CHANGE_POTION__TYPE_F_IS_BEING_USED = new SystemMessage(1110);

    public static final SystemMessage THE_DYE_POTION__TYPE_D_IS_BEING_USED = new SystemMessage(1105);

    public static final SystemMessage THE_DYE_POTION__TYPE_C_IS_BEING_USED = new SystemMessage(1104);

    public static final SystemMessage THE_HAIR_STYLE_CHANGE_POTION__TYPE_C_IS_BEING_USED = new SystemMessage(1107);

    public static final SystemMessage THE_HAIR_STYLE_CHANGE_POTION__TYPE_B_IS_BEING_USED = new SystemMessage(1106);

    public static final SystemMessage SINCE_HP_HAS_INCREASED_THE_EFFECT_OF_S1_WILL_DISAPPEAR = new SystemMessage(1134);

    public static final SystemMessage WHILE_YOU_ARE_ENGAGED_IN_COMBAT_YOU_CANNOT_OPERATE_A_PRIVATE_STORE_OR_PRIVATE_WORKSHOP = new SystemMessage(1135);

    public static final SystemMessage IT_IS_DAWN_AND_THE_EFFECT_OF_S1_WILL_NOW_DISAPPEAR = new SystemMessage(1132);

    public static final SystemMessage SINCE_HP_HAS_DECREASED_THE_EFFECT_OF_S1_CAN_BE_FELT = new SystemMessage(1133);

    public static final SystemMessage YOU_HAVE_GIVEN_S1_DAMAGE_TO_YOUR_TARGET_AND_S2_DAMAGE_TO_THE_SERVITOR = new SystemMessage(1130);

    public static final SystemMessage IT_IS_NOW_MIDNIGHT_AND_THE_EFFECT_OF_S1_CAN_BE_FELT = new SystemMessage(1131);

    public static final SystemMessage A_PRIVATE_STORE_MAY_NOT_BE_OPENED_WHILE_USING_A_SKILL = new SystemMessage(1128);

    public static final SystemMessage THIS_IS_NOT_ALLOWED_WHILE_USING_A_FERRY = new SystemMessage(1129);

    public static final SystemMessage YOU_MAY_NOT_ENTER_A_NEGATIVE_NUMBER = new SystemMessage(1126);

    public static final SystemMessage THE_REWARD_MUST_BE_LESS_THAN_10_TIMES_THE_STANDARD_PRICE = new SystemMessage(1127);

    public static final SystemMessage A_RECIPE_BOOK_MAY_NOT_BE_USED_WHILE_USING_A_SKILL = new SystemMessage(1124);

    public static final SystemMessage AN_ITEM_MAY_NOT_BE_CREATED_WHILE_ENGAGED_IN_TRADING = new SystemMessage(1125);

    public static final SystemMessage THERE_IS_NO_CANDIDATE = new SystemMessage(1122);

    public static final SystemMessage WEIGHT_AND_VOLUME_LIMIT_HAS_BEEN_EXCEEDED_THAT_SKILL_IS_CURRENTLY_UNAVAILABLE = new SystemMessage(1123);

    public static final SystemMessage YOU_ARE_STILL_IN_THE_CLAN = new SystemMessage(1120);

    public static final SystemMessage YOU_DO_NOT_HAVE_THE_RIGHT_TO_VOTE = new SystemMessage(1121);

    public static final SystemMessage S2_IS_SOLD_TO_S1_AT_THE_PRICE_OF_S3_ADENA = new SystemMessage(1151);

    public static final SystemMessage S1_HAS_FAILED_TO_CREATE_S2_AT_THE_PRICE_OF_S3_ADENA = new SystemMessage(1150);

    public static final SystemMessage THE_ATTEMPT_TO_CREATE_S2_FOR_S1_AT_THE_PRICE_OF_S3_ADENA_HAS_FAILED = new SystemMessage(1149);

    public static final SystemMessage S1_CREATED_S2_S3_AT_THE_PRICE_OF_S4_ADENA = new SystemMessage(1148);

    public static final SystemMessage S2_S3_HAVE_BEEN_CREATED_FOR_S1_AT_THE_PRICE_OF_S4_ADENA = new SystemMessage(1147);

    public static final SystemMessage S1_CREATED_S2_AFTER_RECEIVING_S3_ADENA = new SystemMessage(1146);

    public static final SystemMessage S2_HAS_BEEN_CREATED_FOR_S1_AFTER_THE_PAYMENT_OF_S3_ADENA_IS_RECEIVED = new SystemMessage(1145);

    public static final SystemMessage CURRENTLY_YOU_DONT_HAVE_ANYBODY_TO_CHAT_WITH_IN_THE_GAME = new SystemMessage(1144);

    public static final SystemMessage SINCE_YOU_DO_NOT_HAVE_ENOUGH_ITEMS_TO_MAINTAIN_THE_SERVITORS_STAY_THE_SERVITOR_WILL_DISAPPEAR = new SystemMessage(1143);

    public static final SystemMessage SINCE_S1_ALREADY_EXISTS_NEARBY_YOU_CANNOT_SUMMON_IT_AGAIN = new SystemMessage(1142);

    public static final SystemMessage WOULD_YOU_LIKE_TO_CLOSE_THE_GATE = new SystemMessage(1141);

    public static final SystemMessage WOULD_YOU_LIKE_TO_OPEN_THE_GATE = new SystemMessage(1140);

    public static final SystemMessage THE_WEIGHT_AND_VOLUME_LIMIT_OF_INVENTORY_MUST_NOT_BE_EXCEEDED = new SystemMessage(1139);

    public static final SystemMessage S1_HARVESTED_S2_S = new SystemMessage(1138);

    public static final SystemMessage S1_HARVESTED_S3_S2_S = new SystemMessage(1137);

    public static final SystemMessage SINCE_THERE_WAS_AN_ACCOUNT_THAT_USED_THIS_IP_AND_ATTEMPTED_TO_LOG_IN_ILLEGALLY_THIS_ACCOUNT_IS_NOT_ALLOWED_TO_CONNECT_TO_THE_GAME_SERVER_FOR_S1_MINUTES_PLEASE_USE_ANOTHER_GAME_SERVER = new SystemMessage(1136);

    public static final SystemMessage LOOTING_METHOD_RANDOM = new SystemMessage(1032);

    public static final SystemMessage LOOTING_METHOD_RANDOM_INCLUDING_SPOIL = new SystemMessage(1033);

    public static final SystemMessage LOOTING_METHOD_BY_TURN = new SystemMessage(1034);

    public static final SystemMessage LOOTING_METHOD_BY_TURN_INCLUDING_SPOIL = new SystemMessage(1035);

    public static final SystemMessage YOU_HAVE_EXCEEDED_THE_QUANTITY_THAT_CAN_BE_INPUTTED = new SystemMessage(1036);

    public static final SystemMessage S1_MANUFACTURED_S2 = new SystemMessage(1037);

    public static final SystemMessage S1_MANUFACTURED_S3_S2_S = new SystemMessage(1038);

    public static final SystemMessage ITEMS_LEFT_AT_THE_CLAN_HALL_WAREHOUSE_CAN_ONLY_BE_RETRIEVED_BY_THE_CLAN_LEADER_DO_YOU_WANT_TO_CONTINUE = new SystemMessage(1039);

    public static final SystemMessage THE_PET_PUT_ON_S1 = new SystemMessage(1024);

    public static final SystemMessage THE_PET_TOOK_OFF_S1 = new SystemMessage(1025);

    public static final SystemMessage THE_SUMMONED_MONSTER_GAVE_DAMAGE_OF_S1 = new SystemMessage(1026);

    public static final SystemMessage THE_SUMMONED_MONSTER_RECEIVED_DAMAGE_OF_S2_CAUSED_BY_S1 = new SystemMessage(1027);

    public static final SystemMessage SUMMONED_MONSTERS_CRITICAL_HIT = new SystemMessage(1028);

    public static final SystemMessage A_SUMMONED_MONSTER_USES_S1 = new SystemMessage(1029);

    public static final SystemMessage _PARTY_INFORMATION_ = new SystemMessage(1030);

    public static final SystemMessage LOOTING_METHOD_FINDERS_KEEPERS = new SystemMessage(1031);

    public static final SystemMessage IT_IS_NOT_POSSIBLE_TO_MAKE_INVITATIONS_FOR_ORGANIZING_PARTIES_IN_STATE_OF_OVERALL_BLOCKING = new SystemMessage(1049);

    public static final SystemMessage WHISPERING_IS_NOT_POSSIBLE_IN_STATE_OF_OVERALL_BLOCKING = new SystemMessage(1048);

    public static final SystemMessage PAYMENT_FOR_YOUR_CLAN_HALL_HAS_NOT_BEEN_MADE_PLEASE_MAKE_PAYMENT_TO_YOUR_CLAN_WAREHOUSE_BY_S1_TOMORROW = new SystemMessage(1051);

    public static final SystemMessage THERE_ARE_NO_COMMUNITIES_IN_MY_CLAN_CLAN_COMMUNITIES_ARE_ALLOWED_FOR_CLANS_WITH_SKILL_LEVELS_OF_2_AND_HIGHER = new SystemMessage(1050);

    public static final SystemMessage IT_IS_IMPOSSIBLE_TO_BE_RESSURECTED_IN_BATTLEFIELDS_WHERE_SIEGE_WARS_ARE_IN_PROCESS = new SystemMessage(1053);

    public static final SystemMessage THE_CLAN_HALL_FEE_IS_ONE_WEEK_OVERDUE_THEREFORE_THE_CLAN_HALL_OWNERSHIP_HAS_BEEN_REVOKED = new SystemMessage(1052);

    public static final SystemMessage YOU_HAVE_LEFT_THE_LAND_WHICH_HAS_MYSTERIOUS_POWERS = new SystemMessage(1055);

    public static final SystemMessage YOU_HAVE_ENTERED_A_LAND_WITH_MYSTERIOUS_POWERS = new SystemMessage(1054);

    public static final SystemMessage THE_NEXT_SEED_PURCHASE_PRICE_IS_S1_ADENA = new SystemMessage(1041);

    public static final SystemMessage PACKAGES_SENT_CAN_ONLY_BE_RETRIEVED_AT_THIS_WAREHOUSE_DO_YOU_WANT_TO_CONTINUE = new SystemMessage(1040);

    public static final SystemMessage AT_THE_CURRENT_TIME_THE__UNSTUCK_COMMAND_CANNOT_BE_USED_PLEASE_SEND_IN_A_PETITION = new SystemMessage(1043);

    public static final SystemMessage THE_NEXT_FARM_GOODS_PURCHASE_PRICE_IS_S1_ADENA = new SystemMessage(1042);

    public static final SystemMessage NOT_CURRENTLY_PREPARING_FOR_A_MONSTER_RACE = new SystemMessage(1045);

    public static final SystemMessage MONSTER_RACE_PAYOUT_INFORMATION_IS_NOT_AVAILABLE_WHILE_TICKETS_ARE_BEING_SOLD = new SystemMessage(1044);

    public static final SystemMessage WE_DID_NOT_SUCCEED_IN_PRODUCING_S1_ITEM = new SystemMessage(1047);

    public static final SystemMessage MONSTER_RACE_TICKETS_ARE_NO_LONGER_AVAILABLE = new SystemMessage(1046);

    public static final SystemMessage S1_HPS_HAVE_BEEN_RESTORED = new SystemMessage(1066);

    public static final SystemMessage XS2S_HP_HAS_BEEN_RESTORED_BY_S1 = new SystemMessage(1067);

    public static final SystemMessage EQUIPMENT_OF__S1_S2_HAS_BEEN_REMOVED = new SystemMessage(1064);

    public static final SystemMessage WHILE_OPERATING_A_PRIVATE_STORE_OR_WORKSHOP_YOU_CANNOT_DISCARD_DESTROY_OR_TRADE_AN_ITEM = new SystemMessage(1065);

    public static final SystemMessage XYOU_DO_NOT_HAVE_XREADX_PERMISSION = new SystemMessage(1070);

    public static final SystemMessage XYOU_DO_NOT_HAVE_XWRITEX_PERMISSION = new SystemMessage(1071);

    public static final SystemMessage S1_MPS_HAVE_BEEN_RESTORED = new SystemMessage(1068);

    public static final SystemMessage XS2S_MP_HAS_BEEN_RESTORED_BY_S1 = new SystemMessage(1069);

    public static final SystemMessage THE_SALES_AMOUNT_OF_SEEDS_IS_S1_ADENA = new SystemMessage(1058);

    public static final SystemMessage THE_REMAINING_PURCHASING_AMOUNT_IS_S1_ADENA = new SystemMessage(1059);

    public static final SystemMessage YOU_HAVE_EXCEEDED_THE_CASTLES_STORAGE_LIMIT_OF_ADENA = new SystemMessage(1056);

    public static final SystemMessage THIS_COMMAND_CAN_ONLY_BE_USED_IN_THE_RELAX_SERVER = new SystemMessage(1057);

    public static final SystemMessage WRITING_SOMETHING_NEW_IS_POSSIBLE_AFTER_LEVEL_10 = new SystemMessage(1062);

    public static final SystemMessage PETITION_SERVICE_IS_NOT_AVAILABEL_FOR_S1_TO_S2_IN_CASE_OF_BEING_TRAPPED_IN_TERRITORY_WHERE_YOU_ARE_UNABLE_TO_MOVE_PLEASE_USE_THE__UNSTUCK_COMMAND = new SystemMessage(1063);

    public static final SystemMessage THE_REMAINDER_AFTER_SELLING_THE_SEEDS_IS_S1 = new SystemMessage(1060);

    public static final SystemMessage THE_RECIPE_CANNOT_BE_REGISTERED__YOU_DO_NOT_HAVE_THE_ABILITY_TO_CREATE_ITEMS = new SystemMessage(1061);

    public static final SystemMessage THE_FACELIFTING_POTION__TYPE_A_IS_BEING_USED = new SystemMessage(1083);

    public static final SystemMessage DO_YOU_WISH_TO_USE_THE_HAIR_STYLE_CHANGE_POTION_X_TYPE_A_IT_IS_PERMANENT = new SystemMessage(1082);

    public static final SystemMessage DO_YOU_WISH_TO_USE_THE_DYE_POTION_X_TYPE_A_IT_IS_PERMANENT = new SystemMessage(1081);

    public static final SystemMessage DO_YOU_WISH_TO_USE_THE_FACELIFTING_POTION_X_TYPE_A_IT_IS_PERMANENT = new SystemMessage(1080);

    public static final SystemMessage YOUR_HAIR_COLOR_HAS_BEEN_CHANGED = new SystemMessage(1087);

    public static final SystemMessage YOUR_FACIAL_APPEARANCE_HAS_BEEN_CHANGED = new SystemMessage(1086);

    public static final SystemMessage THE_HAIR_STYLE_CHANGE_POTION__TYPE_A_IS_BEING_USED = new SystemMessage(1085);

    public static final SystemMessage THE_DYE_POTION__TYPE_A_IS_BEING_USED = new SystemMessage(1084);

    public static final SystemMessage THE_SECOND_BID_AMOUNT_MUST_BE_HIGHER_THAN_THE_ORIGINAL = new SystemMessage(1075);

    public static final SystemMessage YOU_DO_NOT_MEET_THE_AGE_REQUIREMENT_TO_PURCHASE_A_MONSTER_RACE_TICKET = new SystemMessage(1074);

    public static final SystemMessage YOU_HAVE_OBTAINED_A_TICKET_FOR_THE_MONSTER_RACE_S1__DOUBLE = new SystemMessage(1073);

    public static final SystemMessage YOU_HAVE_OBTAINED_A_TICKET_FOR_THE_MONSTER_RACE_S1__SINGLE = new SystemMessage(1072);

    public static final SystemMessage THE_TARGET_IS_CURRENTLY_BANNED_FROM_CHATTING = new SystemMessage(1079);

    public static final SystemMessage WHEN_A_USERS_KEYBOARD_INPUT_EXCEEDS_A_CERTAIN_CUMULATIVE_SCORE_A_CHAT_BAN_WILL_BE_APPLIED_THIS_IS_DONE_TO_DISCOURAGE_SPAMMING_PLEASE_AVOID_POSTING_THE_SAME_MESSAGE_MULTIPLE_TIMES_DURING_A_SHORT_PERIOD = new SystemMessage(1078);

    public static final SystemMessage A_GAMEGUARD_EXECUTION_ERROR_HAS_OCCURRED_PLEASE_SEND_THE_ERL_FILE_S_LOCATED_IN_THE_GAMEGUARD_FOLDER_TO_GAME = new SystemMessage(1077);

    public static final SystemMessage THE_GAME_CANNOT_BE_TERMINATED = new SystemMessage(1076);

    public static final SystemMessage DO_YOU_REALLY_WISH_TO_RETURN_IT = new SystemMessage(1221);

    public static final SystemMessage ARE_YOU_SURE_YOU_WISH_TO_SUMMON_IT = new SystemMessage(1220);

    public static final SystemMessage WE_DEPART_FOR_TALKING_ISLAND_IN_FIVE_MINUTES = new SystemMessage(1223);

    public static final SystemMessage CURRENT_LOCATION_S1_S2_S3_GM_CONSULTATION_SERVICE = new SystemMessage(1222);

    public static final SystemMessage SEVEN_SIGNS_THE_REVOLUTIONARIES_OF_DUSK_HAVE_OBTAINED_THE_SEAL_OF_STRIFE = new SystemMessage(1217);

    public static final SystemMessage SEVEN_SIGNS_THE_REVOLUTIONARIES_OF_DUSK_HAVE_OBTAINED_THE_SEAL_OF_GNOSIS = new SystemMessage(1216);

    public static final SystemMessage SEVEN_SIGNS_THE_SEAL_VALIDATION_PERIOD_HAS_ENDED = new SystemMessage(1219);

    public static final SystemMessage SEVEN_SIGNS_THE_SEAL_VALIDATION_PERIOD_HAS_BEGUN = new SystemMessage(1218);

    public static final SystemMessage NO_MORE_MESSAGES_MAY_BE_SENT_AT_THIS_TIME_EACH_ACCOUNT_IS_ALLOWED_10_MESSAGES_PER_DAY = new SystemMessage(1229);

    public static final SystemMessage S1_HAS_BLOCKED_YOU_YOU_CANNOT_SEND_MAIL_TO_S1_ = new SystemMessage(1228);

    public static final SystemMessage YOUVE_SENT_MAIL = new SystemMessage(1231);

    public static final SystemMessage YOU_ARE_LIMITED_TO_FIVE_RECIPIENTS_AT_A_TIME = new SystemMessage(1230);

    public static final SystemMessage ALL_ABOARD_FOR_TALKING_ISLAND = new SystemMessage(1225);

    public static final SystemMessage WE_DEPART_FOR_TALKING_ISLAND_IN_ONE_MINUTE = new SystemMessage(1224);

    public static final SystemMessage YOU_HAVE_S1_UNREAD_MESSAGES = new SystemMessage(1227);

    public static final SystemMessage WE_ARE_NOW_LEAVING_FOR_TALKING_ISLAND = new SystemMessage(1226);

    public static final SystemMessage PLEASE_ENTER_SECURITY_CARD_NUMBER = new SystemMessage(1236);

    public static final SystemMessage PLEASE_ENTER_THE_CARD_NUMBER_FOR_NUMBER_S1 = new SystemMessage(1237);

    public static final SystemMessage YOUR_TEMPORARY_MAILBOX_IS_FULL_NO_MORE_MAIL_CAN_BE_STORED_10_MESSAGE_LIMIT = new SystemMessage(1238);

    public static final SystemMessage LOADING_OF_THE_KEYBOARD_SECURITY_MODULE_HAS_FAILED_PLEASE_EXIT_THE_GAME_AND_RELOAD = new SystemMessage(1239);

    public static final SystemMessage THE_MESSAGE_WAS_NOT_SENT = new SystemMessage(1232);

    public static final SystemMessage YOUVE_GOT_MAIL = new SystemMessage(1233);

    public static final SystemMessage THE_MAIL_HAS_BEEN_STORED_IN_YOUR_TEMPORARY_MAILBOX = new SystemMessage(1234);

    public static final SystemMessage DO_YOU_WISH_TO_DELETE_ALL_YOUR_FRIENDS = new SystemMessage(1235);

    public static final SystemMessage USERS_WHO_HAVE_NOT_VERIFIED_THEIR_AGE_CANNOT_LOG_IN_BETWEEN_1000_PM_AND_600_AM_LOGGING_OFF = new SystemMessage(1244);

    public static final SystemMessage YOU_WILL_BE_LOGGED_OUT_IN_S1_MINUTES = new SystemMessage(1245);

    public static final SystemMessage S1_DIED_AND_HAS_DROPPED_S2_ADENA = new SystemMessage(1246);

    public static final SystemMessage THE_CORPSE_IS_TOO_OLD_THE_SKILL_CANNOT_BE_USED = new SystemMessage(1247);

    public static final SystemMessage SEVEN_SIGNS_THE_REVOLUTIONARIES_OF_DUSK_HAVE_WON = new SystemMessage(1240);

    public static final SystemMessage SEVEN_SIGNS_THE_LORDS_OF_DAWN_HAVE_WON = new SystemMessage(1241);

    public static final SystemMessage USERS_WHO_HAVE_NOT_VERIFIED_THEIR_AGE_CANNOT_LOG_IN_BETWEEN_1000_PM_AND_600_AM = new SystemMessage(1242);

    public static final SystemMessage THE_SECURITY_CARD_NUMBER_IS_INVALID = new SystemMessage(1243);

    public static final SystemMessage GM_CONSULTATION_HAS_BEGUN = new SystemMessage(1255);

    public static final SystemMessage THANK_YOU_FOR_SUBMITTING_FEEDBACK = new SystemMessage(1254);

    public static final SystemMessage ARE_YOU_SURE_YOU_WANT_TO_SURRENDER_EXP_PENALTY_WILL_BE_THE_SAME_AS_DEATH_AND_YOU_WILL_NOT_BE_ALLOWED_TO_PARTICIPATE_IN_CLAN_WAR = new SystemMessage(1253);

    public static final SystemMessage ARE_YOU_SURE_YOU_WANT_TO_SURRENDER_EXP_PENALTY_WILL_BE_THE_SAME_AS_DEATH = new SystemMessage(1252);

    public static final SystemMessage ARE_YOU_SURE_YOU_WANT_TO_DISMISS_THE_ALLIANCE_IF_YOU_USE_THE__ALLYDISMISS_COMMAND_YOU_WILL_NOT_BE_ABLE_TO_ACCEPT_ANOTHER_CLAN_TO_YOUR_ALLIANCE_FOR_ONE_DAY = new SystemMessage(1251);

    public static final SystemMessage DO_YOU_REALLY_WANT_TO_SURRENDER_IF_YOU_SURRENDER_DURING_AN_ALLIANCE_WAR_YOUR_EXP_WILL_DROP_AS_MUCH_AS_WHEN_YOUR_CHARACTER_DIES_ONCE = new SystemMessage(1250);

    public static final SystemMessage YOU_MAY_ONLY_RIDE_A_WYVERN_WHILE_YOURE_RIDING_A_STRIDER = new SystemMessage(1249);

    public static final SystemMessage YOU_ARE_OUT_OF_FEED_MOUNT_STATUS_CANCELED = new SystemMessage(1248);

    public static final SystemMessage SEVEN_SIGNS_THIS_IS_THE_SEAL_VALIDATION_PERIOD_A_NEW_QUEST_EVENT_PERIOD_BEGINS_NEXT_MONDAY = new SystemMessage(1263);

    public static final SystemMessage SEVEN_SIGNS_QUEST_EVENT_HAS_ENDED_RESULTS_ARE_BEING_TALLIED = new SystemMessage(1262);

    public static final SystemMessage SEVEN_SIGNS_THE_QUEST_EVENT_PERIOD_HAS_BEGUN_SPEAK_WITH_A_PRIEST_OF_DAWN_OR_DUSK_PRIESTESS_IF_YOU_WISH_TO_PARTICIPATE_IN_THE_EVENT = new SystemMessage(1261);

    public static final SystemMessage SEVEN_SIGNS_PREPARATIONS_HAVE_BEGUN_FOR_THE_NEXT_QUEST_EVENT = new SystemMessage(1260);

    public static final SystemMessage _ALLIANCE_TARGET_ = new SystemMessage(1259);

    public static final SystemMessage S1_HAS_BEEN_CRYSTALLIZED = new SystemMessage(1258);

    public static final SystemMessage THE_SPECIAL_SKILL_OF_A_SERVITOR_OR_PET_CANNOT_BE_REGISTERED_AS_A_MACRO = new SystemMessage(1257);

    public static final SystemMessage PLEASE_WRITE_THE_NAME_AFTER_THE_COMMAND = new SystemMessage(1256);

    public static final SystemMessage THE_TRANSFER_OF_SUB_CLASS_HAS_BEEN_COMPLETED = new SystemMessage(1270);

    public static final SystemMessage DO_YOU_WISH_TO_PARTICIPATE_UNTIL_THE_NEXT_SEAL_VALIDATION_PERIOD_YOU_ARE_A_MEMBER_OF_THE_LORDS_OF_DAWN = new SystemMessage(1271);

    public static final SystemMessage DO_YOU_WISH_TO_ADD_S1_CLASS_AS_YOUR_SUB_CLASS = new SystemMessage(1268);

    public static final SystemMessage THE_NEW_SUB_CLASS_HAS_BEEN_ADDED = new SystemMessage(1269);

    public static final SystemMessage THE_EXCHANGE_HAS_ENDED = new SystemMessage(1266);

    public static final SystemMessage YOUR_CONTRIBUTION_SCORE_IS_INCREASED_BY_S1 = new SystemMessage(1267);

    public static final SystemMessage THIS_SOUL_STONE_CANNOT_CURRENTLY_ABSORB_SOULS_ABSORPTION_HAS_FAILED = new SystemMessage(1264);

    public static final SystemMessage YOU_CANT_ABSORB_SOULS_WITHOUT_A_SOUL_STONE = new SystemMessage(1265);

    public static final SystemMessage THE_NPC_SERVER_IS_NOT_OPERATING = new SystemMessage(1278);

    public static final SystemMessage CONTRIBUTION_LEVEL_HAS_EXCEEDED_THE_LIMIT_YOU_MAY_NOT_CONTINUE = new SystemMessage(1279);

    public static final SystemMessage YOUVE_CHOSEN_TO_FIGHT_FOR_THE_SEAL_OF_GNOSIS_DURING_THIS_QUEST_EVENT_PERIOD = new SystemMessage(1276);

    public static final SystemMessage YOUVE_CHOSEN_TO_FIGHT_FOR_THE_SEAL_OF_STRIFE_DURING_THIS_QUEST_EVENT_PERIOD = new SystemMessage(1277);

    public static final SystemMessage YOU_WILL_PARTICIPATE_IN_THE_SEVEN_SIGNS_AS_A_MEMBER_OF_THE_REVOLUTIONARIES_OF_DUSK = new SystemMessage(1274);

    public static final SystemMessage YOUVE_CHOSEN_TO_FIGHT_FOR_THE_SEAL_OF_AVARICE_DURING_THIS_QUEST_EVENT_PERIOD = new SystemMessage(1275);

    public static final SystemMessage DO_YOU_WISH_TO_PARTICIPATE_UNTIL_THE_NEXT_SEAL_VALIDATION_PERIOD_YOU_ARE_A_MEMBER_OF_THE_REVOLUTIONARIES_OF_DUSK = new SystemMessage(1272);

    public static final SystemMessage YOU_WILL_PARTICIPATE_IN_THE_SEVEN_SIGNS_AS_A_MEMBER_OF_THE_LORDS_OF_DAWN = new SystemMessage(1273);

    public static final SystemMessage S2_HAS_BEEN_PURCHASED_FROM_S1_AT_THE_PRICE_OF_S3_ADENA = new SystemMessage(1153);

    public static final SystemMessage S2_S3_HAVE_BEEN_SOLD_TO_S1_FOR_S4_ADENA = new SystemMessage(1152);

    public static final SystemMessage _S2S3_HAS_BEEN_SOLD_TO_S1_AT_THE_PRICE_OF_S4_ADENA = new SystemMessage(1155);

    public static final SystemMessage S3_S2_HAS_BEEN_PURCHASED_FROM_S1_FOR_S4_ADENA = new SystemMessage(1154);

    public static final SystemMessage TRYING_ON_STATE_LASTS_FOR_ONLY_5_SECONDS_WHEN_A_CHARACTERS_STATE_CHANGES_IT_CAN_BE_CANCELLED = new SystemMessage(1157);

    public static final SystemMessage _S2S3_HAS_BEEN_PURCHASED_FROM_S1_AT_THE_PRICE_OF_S4_ADENA = new SystemMessage(1156);

    public static final SystemMessage THE_FERRY_FROM_TALKING_ISLAND_WILL_ARRIVE_AT_GLUDIN_HARBOR_IN_APPROXIMATELY_10_MINUTES = new SystemMessage(1159);

    public static final SystemMessage YOU_CANNOT_GET_DOWN_FROM_A_PLACE_THAT_IS_TOO_HIGH = new SystemMessage(1158);

    public static final SystemMessage THE_FERRY_FROM_TALKING_ISLAND_WILL_BE_ARRIVING_AT_GLUDIN_HARBOR_IN_APPROXIMATELY_1_MINUTE = new SystemMessage(1161);

    public static final SystemMessage THE_FERRY_FROM_TALKING_ISLAND_WILL_BE_ARRIVING_AT_GLUDIN_HARBOR_IN_APPROXIMATELY_5_MINUTES = new SystemMessage(1160);

    public static final SystemMessage THE_FERRY_FROM_GIRAN_HARBOR_WILL_BE_ARRIVING_AT_TALKING_ISLAND_IN_APPROXIMATELY_10_MINUTES = new SystemMessage(1163);

    public static final SystemMessage THE_FERRY_FROM_GIRAN_HARBOR_WILL_BE_ARRIVING_AT_TALKING_ISLAND_IN_APPROXIMATELY_15_MINUTES = new SystemMessage(1162);

    public static final SystemMessage THE_FERRY_FROM_GIRAN_HARBOR_WILL_BE_ARRIVING_AT_TALKING_ISLAND_IN_APPROXIMATELY_1_MINUTE = new SystemMessage(1165);

    public static final SystemMessage THE_FERRY_FROM_GIRAN_HARBOR_WILL_BE_ARRIVING_AT_TALKING_ISLAND_IN_APPROXIMATELY_5_MINUTES = new SystemMessage(1164);

    public static final SystemMessage THE_FERRY_FROM_TALKING_ISLAND_WILL_BE_ARRIVING_AT_GIRAN_HARBOR_IN_APPROXIMATELY_15_MINUTES = new SystemMessage(1167);

    public static final SystemMessage THE_FERRY_FROM_TALKING_ISLAND_WILL_BE_ARRIVING_AT_GIRAN_HARBOR_IN_APPROXIMATELY_20_MINUTES = new SystemMessage(1166);

    public static final SystemMessage THE_FERRY_FROM_TALKING_ISLAND_WILL_BE_ARRIVING_AT_GIRAN_HARBOR_IN_APPROXIMATELY_10_MINUTES = new SystemMessage(1168);

    public static final SystemMessage THE_FERRY_FROM_TALKING_ISLAND_WILL_BE_ARRIVING_AT_GIRAN_HARBOR_IN_APPROXIMATELY_5_MINUTES = new SystemMessage(1169);

    public static final SystemMessage THE_FERRY_FROM_TALKING_ISLAND_WILL_BE_ARRIVING_AT_GIRAN_HARBOR_IN_APPROXIMATELY_1_MINUTE = new SystemMessage(1170);

    public static final SystemMessage THE_INNADRIL_PLEASURE_BOAT_WILL_ARRIVE_IN_APPROXIMATELY_20_MINUTES = new SystemMessage(1171);

    public static final SystemMessage THE_INNADRIL_PLEASURE_BOAT_WILL_ARRIVE_IN_APPROXIMATELY_15_MINUTES = new SystemMessage(1172);

    public static final SystemMessage THE_INNADRIL_PLEASURE_BOAT_WILL_ARRIVE_IN_APPROXIMATELY_10_MINUTES = new SystemMessage(1173);

    public static final SystemMessage THE_INNADRIL_PLEASURE_BOAT_WILL_ARRIVE_IN_APPROXIMATELY_5_MINUTES = new SystemMessage(1174);

    public static final SystemMessage THE_INNADRIL_PLEASURE_BOAT_WILL_ARRIVE_IN_APPROXIMATELY_1_MINUTE = new SystemMessage(1175);

    public static final SystemMessage THIS_IS_A_QUEST_EVENT_PERIOD = new SystemMessage(1176);

    public static final SystemMessage THIS_IS_THE_SEAL_VALIDATION_PERIOD = new SystemMessage(1177);

    public static final SystemMessage THIS_SEAL_PERMITS_THE_GROUP_THAT_HOLDS_IT_TO_EXCLUSIVELY_ENTER_THE_DUNGEON_OPENED_BY_THE_SEAL_OF_AVARICE_DURING_THE_SEAL_VALIDATION_PERIOD__IT_ALSO_PERMITS_TRADING_WITH_THE_MERCHANT_OF_MAMMON_WHO_APPEARS_IN_SPECIAL_DUNGEONS_AND_PERMITS_MEETINGS_WITH_ANAKIM_OR_LILITH_IN_THE_DISCIPLES_NECROPOLIS = new SystemMessage(1178);

    public static final SystemMessage THIS_SEAL_PERMITS_THE_GROUP_THAT_HOLDS_IT_TO_ENTER_THE_DUNGEON_OPENED_BY_THE_SEAL_OF_GNOSIS_USE_THE_TELEPORTATION_SERVICE_OFFERED_BY_THE_PRIEST_IN_THE_VILLAGE_AND_DO_BUSINESS_WITH_THE_MERCHANT_OF_MAMMON_THE_ORATOR_OF_REVELATIONS_APPEARS_AND_CASTS_GOOD_MAGIC_ON_THE_WINNERS_AND_THE_PREACHER_OF_DOOM_APPEARS_AND_CASTS_BAD_MAGIC_ON_THE_LOSERS = new SystemMessage(1179);

    public static final SystemMessage DURING_THE_SEAL_VALIDATION_PERIOD_THE_COSTS_OF_CASTLE_DEFENSE_MERCENARIES_AND_RENOVATIONS_BASIC_P_DEF_OF_CASTLE_GATES_AND_CASTLE_WALLS_AND_MAXIMUM_TAX_RATES_WILL_ALL_CHANGE_TO_FAVOR_THE_GROUP_OF_FIGHTERS_THAT_POSSESSES_THIS_SEAL = new SystemMessage(1180);

    public static final SystemMessage DO_YOU_REALLY_WISH_TO_CHANGE_THE_TITLE = new SystemMessage(1181);

    public static final SystemMessage DO_YOU_REALLY_WISH_TO_DELETE_THE_CLAN_CREST = new SystemMessage(1182);

    public static final SystemMessage THIS_IS_THE_INITIAL_PERIOD = new SystemMessage(1183);

    public static final SystemMessage IF_YOU_HAVE_LOST_YOUR_ACCOUNT_INFORMATION_PLEASE_VISIT_THE_OFFICIAL_LINEAGE_II_SUPPORT_WEBSITE_AT_HTTP__SUPPORTPLAYNCCOM = new SystemMessage(1187);

    public static final SystemMessage TO_CREATE_A_NEW_ACCOUNT_PLEASE_VISIT_THE_PLAYNC_WEBSITE_HTTP___WWWPLAYNCCOM_US_SUPPORT = new SystemMessage(1186);

    public static final SystemMessage DAYS_LEFT_UNTIL_DELETION = new SystemMessage(1185);

    public static final SystemMessage THIS_IS_A_PERIOD_OF_CALCULATIING_STATISTICS_IN_THE_SERVER = new SystemMessage(1184);

    public static final SystemMessage THE_FERRY_FROM_GLUDIN_HARBOR_WILL_BE_ARRIVING_AT_TALKING_ISLAND_IN_APPROXIMATELY_10_MINUTES = new SystemMessage(1191);

    public static final SystemMessage THE_TEMPORARY_ALLIANCE_OF_THE_CASTLE_ATTACKER_TEAM_HAS_BEEN_DISSOLVED = new SystemMessage(1190);

    public static final SystemMessage THE_TEMPORARY_ALLIANCE_OF_THE_CASTLE_ATTACKER_TEAM_IS_IN_EFFECT_IT_WILL_BE_DISSOLVED_WHEN_THE_CASTLE_LORD_IS_REPLACED = new SystemMessage(1189);

    public static final SystemMessage YOUR_SELECTED_TARGET_CAN_NO_LONGER_RECEIVE_A_RECOMMENDATION = new SystemMessage(1188);

    public static final SystemMessage THIS_MERCENARY_CANNOT_BE_ASSIGNED_TO_A_POSITION_BY_USING_THE_SEAL_OF_STRIFE = new SystemMessage(1195);

    public static final SystemMessage A_MERCENARY_CAN_BE_ASSIGNED_TO_A_POSITION_FROM_THE_BEGINNING_OF_THE_SEAL_VALIDATION_PERIOD_UNTIL_THE_TIME_WHEN_A_SIEGE_STARTS = new SystemMessage(1194);

    public static final SystemMessage THE_FERRY_FROM_GLUDIN_HARBOR_WILL_BE_ARRIVING_AT_TALKING_ISLAND_IN_APPROXIMATELY_1_MINUTE = new SystemMessage(1193);

    public static final SystemMessage THE_FERRY_FROM_GLUDIN_HARBOR_WILL_BE_ARRIVING_AT_TALKING_ISLAND_IN_APPROXIMATELY_5_MINUTES = new SystemMessage(1192);

    public static final SystemMessage _CLAN_WAR_TARGET_ = new SystemMessage(1199);

    public static final SystemMessage THE_ITEM_HAS_BEEN_SUCCESSFULLY_CRYSTALLIZED = new SystemMessage(1198);

    public static final SystemMessage SUMMONING_A_SERVITOR_COSTS_S2_S1 = new SystemMessage(1197);

    public static final SystemMessage YOUR_FORCE_HAS_REACHED_MAXIMUM_CAPACITY = new SystemMessage(1196);

    public static final SystemMessage S1_NO_ALLIANCE_EXISTS = new SystemMessage(1202);

    public static final SystemMessage THERE_IS_NO_CLAN_WAR_IN_PROGRESS = new SystemMessage(1203);

    public static final SystemMessage S1_S2_ALLIANCE = new SystemMessage(1200);

    public static final SystemMessage PLEASE_SELECT_THE_QUEST_YOU_WISH_TO_QUIT = new SystemMessage(1201);

    public static final SystemMessage MEMO_BOX_IS_FULL_100_MEMO_MAXIMUM = new SystemMessage(1206);

    public static final SystemMessage PLEASE_MAKE_AN_ENTRY_IN_THE_FIELD = new SystemMessage(1207);

    public static final SystemMessage THE_SCREENSHOT_HAS_BEEN_SAVED_S1_S2XS3 = new SystemMessage(1204);

    public static final SystemMessage MAILBOX_IS_FULL100_MESSAGE_MAXIMUM = new SystemMessage(1205);

    public static final SystemMessage SEVEN_SIGNS_THE_QUEST_EVENT_PERIOD_HAS_BEGUN_VISIT_A_PRIEST_OF_DAWN_OR_DUSK_TO_PARTICIPATE_IN_THE_EVENT = new SystemMessage(1210);

    public static final SystemMessage SEVEN_SIGNS_THE_QUEST_EVENT_PERIOD_HAS_ENDED_THE_NEXT_QUEST_EVENT_WILL_START_IN_ONE_WEEK = new SystemMessage(1211);

    public static final SystemMessage S1_DIED_AND_DROPPED_S3_S2 = new SystemMessage(1208);

    public static final SystemMessage CONGRATULATIONS_YOUR_RAID_WAS_SUCCESSFUL = new SystemMessage(1209);

    public static final SystemMessage SEVEN_SIGNS_THE_LORDS_OF_DAWN_HAVE_OBTAINED_THE_SEAL_OF_STRIFE = new SystemMessage(1214);

    public static final SystemMessage SEVEN_SIGNS_THE_REVOLUTIONARIES_OF_DUSK_HAVE_OBTAINED_THE_SEAL_OF_AVARICE = new SystemMessage(1215);

    public static final SystemMessage SEVEN_SIGNS_THE_LORDS_OF_DAWN_HAVE_OBTAINED_THE_SEAL_OF_AVARICE = new SystemMessage(1212);

    public static final SystemMessage SEVEN_SIGNS_THE_LORDS_OF_DAWN_HAVE_OBTAINED_THE_SEAL_OF_GNOSIS = new SystemMessage(1213);

    public static final SystemMessage S1_HAS_PICKED_UP__S2_ADENA_THAT_WAS_DROPPED_BY_A_RAID_BOSS = new SystemMessage(1375);

    public static final SystemMessage S1_HAS_PICKED_UP_S3_S2_S_THAT_WAS_DROPPED_BY_A_RAID_BOSS = new SystemMessage(1374);

    public static final SystemMessage S1_HAS_PICKED_UP_S2_THAT_WAS_DROPPED_BY_A_RAID_BOSS = new SystemMessage(1373);

    public static final SystemMessage YOUR_PUNISHMENT_WILL_CONTINUE_FOR_S1_MINUTES = new SystemMessage(1372);

    public static final SystemMessage IT_HAS_BEEN_DETERMINED_THAT_YOURE_NOT_ENGAGED_IN_NORMAL_GAMEPLAY_AND_A_RESTRICTION_HAS_BEEN_IMPOSED_UPON_YOU_YOU_MAY_NOT_MOVE_FOR_S1_MINUTES = new SystemMessage(1371);

    public static final SystemMessage YOU_CANNOT_SEND_MAIL_TO_A_GM_SUCH_AS_S1 = new SystemMessage(1370);

    public static final SystemMessage YOUVE_EXCEEDED_THE_MAXIMUM = new SystemMessage(1369);

    public static final SystemMessage THOSE_ITEMS_MAY_NOT_BE_TRIED_ON_SIMULTANEOUSLY = new SystemMessage(1368);

    public static final SystemMessage ONLY_MEMBERS_OF_THE_GROUP_ARE_ALLOWED_TO_ADD_RECORDS = new SystemMessage(1367);

    public static final SystemMessage YOU_CAN_DELETE_A_GROUP_ONLY_WHEN_YOU_DO_NOT_HAVE_ANY_CONTACT_IN_THAT_GROUP__IN_ORDER_TO_DELETE_A_GROUP_FIRST_TRANSFER_YOUR_CONTACT_S_IN_THAT_GROUP_TO_ANOTHER_GROUP = new SystemMessage(1366);

    public static final SystemMessage S2_OF_S1_CLAN_HAS_SURRENDERED_AS_AN_INDIVIDUAL = new SystemMessage(1365);

    public static final SystemMessage THE_REQUEST_FOR_AN_ALLIANCE_WAR_HAS_BEEN_REJECTED = new SystemMessage(1364);

    public static final SystemMessage YOUR_REQUEST_TO_PARTICIPATE_IN_THE_ALLIANCE_WAR_HAS_BEEN_DENIED = new SystemMessage(1363);

    public static final SystemMessage PLEASE_SELECT_THE_CONTACT_YOU_WISH_TO_DELETE__IF_YOU_WOULD_LIKE_TO_DELETE_A_GROUP_CLICK_THE_BUTTON_NEXT_TO_MY_STATUS_AND_THEN_USE_THE_OPTIONS_MENU = new SystemMessage(1362);

    public static final SystemMessage DUE_TO_A_SYSTEM_ERROR_YOU_HAVE_BEEN_LOGGED_OUT_OF_THE_NET_MESSENGER_SERVICE = new SystemMessage(1361);

    public static final SystemMessage YOU_HAVE_RECEIVED_A_MESSAGE_FROM_S1 = new SystemMessage(1360);

    public static final SystemMessage YOU_ARE_BEING_LOGGED_OUT = new SystemMessage(1358);

    public static final SystemMessage S1_HAS_LOGGED_IN_1 = new SystemMessage(1359);

    public static final SystemMessage THE_CONTACT_YOU_CHOSE_TO_CHAT_WITH_IS_NOT_CURRENTLY_LOGGED_IN = new SystemMessage(1356);

    public static final SystemMessage YOU_HAVE_BEEN_BLOCKED_FROM_THE_CONTACT_YOU_SELECTED = new SystemMessage(1357);

    public static final SystemMessage YOU_ARE_NOT_ALLOWED_TO_CHAT_WITH_YOUR_CONTACT_WHILE_YOU_ARE_BLOCKED_FROM_CHATTING = new SystemMessage(1354);

    public static final SystemMessage THE_CONTACT_YOU_CHOSE_TO_CHAT_WITH_IS_CURRENTLY_BLOCKED_FROM_CHATTING = new SystemMessage(1355);

    public static final SystemMessage YOU_HAVE_BEEN_ADDED_TO_THE_CONTACT_LIST_OF_S1_S2 = new SystemMessage(1352);

    public static final SystemMessage YOU_CAN_SET_THE_OPTION_TO_SHOW_YOUR_STATUS_AS_ALWAYS_BEING_OFF_LINE_TO_ALL_OF_YOUR_CONTACTS = new SystemMessage(1353);

    public static final SystemMessage THE_STATUS_WILL_BE_CHANGED_TO_INDICATE__OFF_LINE__ALL_THE_CHAT_WINDOWS_CURRENTLY_OPENED_WILL_BE = new SystemMessage(1350);

    public static final SystemMessage AFTER_SELECTING_THE_CONTACT_YOU_WANT_TO_DELETE_CLICK_THE_DELETE_BUTTON = new SystemMessage(1351);

    public static final SystemMessage S1_HAS_ENTERED_THE_CHAT_ROOM = new SystemMessage(1348);

    public static final SystemMessage S1_HAS_LEFT_THE_CHAT_ROOM = new SystemMessage(1349);

    public static final SystemMessage YOU_ARE_CURRENTLY_ENTERING_A_CHAT_MESSAGE = new SystemMessage(1346);

    public static final SystemMessage THE_LINEAGE_II_MESSENGER_COULD_NOT_CARRY_OUT_THE_TASK_YOU_REQUESTED = new SystemMessage(1347);

    public static final SystemMessage THE_SERVICE_YOU_REQUESTED_COULD_NOT_BE_LOCATED_AND_THEREFORE_YOUR_ATTEMPT_TO_LOG_INTO_THE_NET_MESSENGER_SERVICE_HAS_FAILED_PLEASE_VERIFY_THAT_YOU_ARE_CURRENTLY_CONNECTED_TO_THE_INTERNET = new SystemMessage(1344);

    public static final SystemMessage AFTER_SELECTING_A_CONTACT_NAME_CLICK_ON_THE_OK_BUTTON = new SystemMessage(1345);

    public static final SystemMessage S1_CPS_WILL_BE_RESTORED = new SystemMessage(1405);

    public static final SystemMessage THE_OWNER_OF_THE_PRIVATE_MANUFACTURING_STORE_HAS_CHANGED_THE_PRICE_FOR_CREATING_THIS_ITEM__PLEASE_CHECK_THE_NEW_PRICE_BEFORE_TRYING_AGAIN = new SystemMessage(1404);

    public static final SystemMessage YOU_ARE_USING_A_COMPUTER_THAT_DOES_NOT_ALLOW_YOU_TO_LOG_IN_WITH_TWO_ACCOUNTS_AT_THE_SAME_TIME = new SystemMessage(1407);

    public static final SystemMessage S1_WILL_RESTORE_S2S_CP = new SystemMessage(1406);

    public static final SystemMessage YOU_CANNOT_TRANSFER_RIGHTS_TO_YOURSELF = new SystemMessage(1401);

    public static final SystemMessage PLEASE_SELECT_THE_PERSON_YOU_WISH_TO_MAKE_THE_PARTY_LEADER = new SystemMessage(1400);

    public static final SystemMessage YOU_HAVE_FAILED_TO_TRANSFER_THE_PARTY_LEADER_RIGHTS = new SystemMessage(1403);

    public static final SystemMessage YOU_CAN_TRANSFER_RIGHTS_ONLY_TO_ANOTHER_PARTY_MEMBER = new SystemMessage(1402);

    public static final SystemMessage THE_LEADER_OF_THE_PARTY_ROOM_HAS_CHANGED = new SystemMessage(1397);

    public static final SystemMessage THE_LIST_OF_PARTY_ROOMS_CAN_BE_VIEWED_BY_A_PERSON_WHO_HAS_NOT_JOINED_A_PARTY_OR_WHO_IS_A_PARTY_LEADER = new SystemMessage(1396);

    public static final SystemMessage ONLY_A_PARTY_LEADER_CAN_TRANSFER_ONES_RIGHTS_TO_ANOTHER_PLAYER = new SystemMessage(1399);

    public static final SystemMessage WE_ARE_RECRUITING_PARTY_MEMBERS = new SystemMessage(1398);

    public static final SystemMessage YOU_HAVE_BEEN_OUSTED_FROM_THE_PARTY_ROOM = new SystemMessage(1393);

    public static final SystemMessage S1_HAS_LEFT_THE_PARTY_ROOM = new SystemMessage(1392);

    public static final SystemMessage THE_PARTY_ROOM_HAS_BEEN_DISBANDED = new SystemMessage(1395);

    public static final SystemMessage S1_HAS_BEEN_OUSTED_FROM_THE_PARTY_ROOM = new SystemMessage(1394);

    public static final SystemMessage A_PARTY_ROOM_HAS_BEEN_CREATED = new SystemMessage(1388);

    public static final SystemMessage THE_PARTY_ROOMS_INFORMATION_HAS_BEEN_REVISED = new SystemMessage(1389);

    public static final SystemMessage YOU_ARE_NOT_ALLOWED_TO_ENTER_THE_PARTY_ROOM = new SystemMessage(1390);

    public static final SystemMessage YOU_HAVE_EXITED_FROM_THE_PARTY_ROOM = new SystemMessage(1391);

    public static final SystemMessage S1_HAS_BECOME_A_PARTY_LEADER = new SystemMessage(1384);

    public static final SystemMessage YOU_ARE_NOT_ALLOWED_TO_DISMOUNT_AT_THIS_LOCATION = new SystemMessage(1385);

    public static final SystemMessage HOLD_STATE_HAS_BEEN_LIFTED = new SystemMessage(1386);

    public static final SystemMessage PLEASE_SELECT_THE_ITEM_YOU_WOULD_LIKE_TO_TRY_ON = new SystemMessage(1387);

    public static final SystemMessage YOU_CANT_SUMMON_A_S1_WHILE_ON_THE_BATTLEGROUND = new SystemMessage(1380);

    public static final SystemMessage THE_PARTY_LEADER_HAS_OBTAINED_S2_OF_S1 = new SystemMessage(1381);

    public static final SystemMessage ARE_YOU_SURE_YOU_WANT_TO_CHOOSE_THIS_WEAPON_TO_FULFILL_THE_QUEST_YOU_MUST_BRING_THE_CHOSEN_WEAPON = new SystemMessage(1382);

    public static final SystemMessage ARE_YOU_SURE_YOU_WANT_TO_EXCHANGE = new SystemMessage(1383);

    public static final SystemMessage S1_HAS_PICKED_UP_S2_THAT_WAS_DROPPED_BY_ANOTHER_CHARACTER = new SystemMessage(1376);

    public static final SystemMessage S1_HAS_PICKED_UP_S3_S2_S_THAT_WAS_DROPPED_BY_ANOTHER_CHARACTER = new SystemMessage(1377);

    public static final SystemMessage S1_HAS_PICKED_UP__S3S2_THAT_WAS_DROPPED_BY_ANOTHER_CHARACTER = new SystemMessage(1378);

    public static final SystemMessage S1_HAS_OBTAINED_S2_ADENA = new SystemMessage(1379);

    public static final SystemMessage ACCOUNTS_MAY_ONLY_BE_SETTLED_DURING_THE_SEAL_VALIDATION_PERIOD = new SystemMessage(1307);

    public static final SystemMessage TRYING_ON_MODE_HAS_ENDED = new SystemMessage(1306);

    public static final SystemMessage YOU_MAY_GIVE_SOMEONE_ELSE_A_SEAL_STONE_FOR_SAFEKEEPING_ONLY_DURING_A_QUEST_EVENT_PERIOD = new SystemMessage(1305);

    public static final SystemMessage DUE_TO_THE_INFLUENCE_OF_THE_SEAL_OF_STRIFE_ALL_DEFENSIVE_REGISTRATION_HAS_BEEN_CANCELED_EXCEPT_BY_ALLIANCES_OF_CASTLE_OWNING_CLANS = new SystemMessage(1304);

    public static final SystemMessage PREVIOUS_VERSIONS_OF_MSN_MESSENGER_ONLY_PROVIDE_THE_BASIC_FEATURES_TO_CHAT_IN_THE_GAME_ADD_DELETE_CONTACTS_AND_OTHER_OPTIONS_ARENT_AVAILABLE = new SystemMessage(1311);

    public static final SystemMessage FOR_FULL_FUNCTIONALITY_THE_LATEST_VERSION_OF_MSN_MESSENGER_CLIENT_MUST_BE_INSTALLED_ON_THE_USERS_COMPUTER = new SystemMessage(1310);

    public static final SystemMessage THIS_OPTION_REQUIRES_THAT_THE_LATEST_VERSION_OF_MSN_MESSENGER_CLIENT_BE_INSTALLED_ON_YOUR_COMPUTER = new SystemMessage(1309);

    public static final SystemMessage CONGRATULATIONS_YOU_HAVE_TRANSFERRED_TO_A_NEW_CLASS = new SystemMessage(1308);

    public static final SystemMessage S1S_CASTING_HAS_BEEN_INTERRUPTED = new SystemMessage(1299);

    public static final SystemMessage EXITING_THE_MONSTER_RACE_TRACK = new SystemMessage(1298);

    public static final SystemMessage A_PRIVATE_WORKSHOP_MAY_NOT_BE_OPENED_IN_THIS_AREA = new SystemMessage(1297);

    public static final SystemMessage A_PRIVATE_STORE_MAY_NOT_BE_OPENED_IN_THIS_AREA = new SystemMessage(1296);

    public static final SystemMessage USED_ONLY_DURING_A_QUEST_EVENT_PERIOD = new SystemMessage(1303);

    public static final SystemMessage CAN_BE_USED_ONLY_BY_THE_REVOLUTIONARIES_OF_DUSK = new SystemMessage(1302);

    public static final SystemMessage CAN_BE_USED_ONLY_BY_THE_LORDS_OF_DAWN = new SystemMessage(1301);

    public static final SystemMessage TRYING_ON_MODE_CANCELED = new SystemMessage(1300);

    public static final SystemMessage ALTHOUGH_THE_SEAL_WAS_NOT_OWNED_SINCE_35_PERCENT_OR_MORE_PEOPLE_HAVE_VOTED = new SystemMessage(1290);

    public static final SystemMessage ALTHOUGH_THE_SEAL_WAS_OWNED_DURING_THE_PREVIOUS_PERIOD_BECAUSE_LESS_THAN_10_PERCENT_OF_PEOPLE_HAVE_VOTED = new SystemMessage(1291);

    public static final SystemMessage IF_TRENDS_CONTINUE_S1_WILL_WIN_AND_THE_SEAL_WILL_BELONG_TO = new SystemMessage(1288);

    public static final SystemMessage SINCE_THE_SEAL_WAS_OWNED_DURING_THE_PREVIOUS_PERIOD_AND_10_PERCENT_OR_MORE_PEOPLE_HAVE_VOTED = new SystemMessage(1289);

    public static final SystemMessage SINCE_THE_COMPETITION_HAS_ENDED_IN_A_TIE_THE_SEAL_WILL_NOT_BE_AWARDED = new SystemMessage(1294);

    public static final SystemMessage SUB_CLASSES_MAY_NOT_BE_CREATED_OR_CHANGED_WHILE_A_SKILL_IS_IN_USE = new SystemMessage(1295);

    public static final SystemMessage SINCE_THE_SEAL_WAS_NOT_OWNED_DURING_THE_PREVIOUS_PERIOD_AND_SINCE_LESS_THAN_35_PERCENT_OF_PEOPLE_HAVE_VOTED = new SystemMessage(1292);

    public static final SystemMessage IF_CURRENT_TRENDS_CONTINUE_IT_WILL_END_IN_A_TIE = new SystemMessage(1293);

    public static final SystemMessage YOUR_KARMA_HAS_BEEN_CHANGED_TO_S1 = new SystemMessage(1282);

    public static final SystemMessage THE_MINIMUM_FRAME_OPTION_HAS_BEEN_ACTIVATED = new SystemMessage(1283);

    public static final SystemMessage MAGIC_CRITICAL_HIT = new SystemMessage(1280);

    public static final SystemMessage YOUR_EXCELLENT_SHIELD_DEFENSE_WAS_A_SUCCESS = new SystemMessage(1281);

    public static final SystemMessage UNTIL_NEXT_MONDAY_AT_120_AM = new SystemMessage(1286);

    public static final SystemMessage UNTIL_TODAY_AT_120_AM = new SystemMessage(1287);

    public static final SystemMessage THE_MINIMUM_FRAME_OPTION_HAS_BEEN_DEACTIVATED = new SystemMessage(1284);

    public static final SystemMessage NO_INVENTORY_EXISTS_YOU_CANNOT_PURCHASE_AN_ITEM = new SystemMessage(1285);

    public static final SystemMessage REPLAY_MODE_WILL_BE_TERMINATED_DO_YOU_WISH_TO_CONTINUE = new SystemMessage(1337);

    public static final SystemMessage THE_REPLAY_FILE_HAS_BEEN_CORRUPTED_PLEASE_CHECK_THE_S1S2_FILE = new SystemMessage(1336);

    public static final SystemMessage ONCE_A_MACRO_IS_ASSIGNED_TO_A_SHORTCUT_IT_CANNOT_BE_RUN_AS_A_MACRO_AGAIN = new SystemMessage(1339);

    public static final SystemMessage YOU_HAVE_EXCEEDED_THE_QUANTITY_THAT_CAN_BE_TRANSFERRED_AT_ONE_TIME = new SystemMessage(1338);

    public static final SystemMessage THE_NAME_OR_E_MAIL_ADDRESS_YOU_ENTERED_IS_INCORRECT = new SystemMessage(1341);

    public static final SystemMessage THIS_SERVER_CANNOT_BE_ACCESSED_BY_THE_COUPON_YOU_ARE_USING = new SystemMessage(1340);

    public static final SystemMessage THE_PASSWORD_OR_E_MAIL_ADDRESS_YOU_ENTERED_IS_INCORRECT__YOUR_ATTEMPT_TO_LOG_INTO_NET_MESSENGER_SERVICE_HAS_FAILED = new SystemMessage(1343);

    public static final SystemMessage YOU_ARE_ALREADY_LOGGED_IN = new SystemMessage(1342);

    public static final SystemMessage YOU_ARE_CURRENTLY_BANNED_FROM_ACTIVITIES_RELATED_TO_THE_PRIVATE_STORE_AND_PRIVATE_WORKSHOP = new SystemMessage(1329);

    public static final SystemMessage MEMBERS_OF_THE_REVOLUTIONARIES_OF_DUSK_WILL_NOT_BE_RESURRECTED = new SystemMessage(1328);

    public static final SystemMessage ACTIVITIES_RELATED_TO_THE_PRIVATE_STORE_AND_PRIVATE_WORKSHOP_ARE_NOW_PERMITTED = new SystemMessage(1331);

    public static final SystemMessage NO_PRIVATE_STORE_OR_PRIVATE_WORKSHOP_MAY_BE_OPENED_FOR_S1_MINUTES = new SystemMessage(1330);

    public static final SystemMessage REPLAY_FILE_ISNT_ACCESSIBLE_VERIFY_THAT_REPLAYINI_FILE_EXISTS = new SystemMessage(1333);

    public static final SystemMessage ITEMS_MAY_NOT_BE_USED_AFTER_YOUR_CHARACTER_OR_PET_DIES = new SystemMessage(1332);

    public static final SystemMessage THE_ATTEMPT_TO_STORE_THE_NEW_CAMERA_DATA_HAS_FAILED = new SystemMessage(1335);

    public static final SystemMessage THE_NEW_CAMERA_DATA_HAS_BEEN_STORED = new SystemMessage(1334);

    public static final SystemMessage AFTER_SELECTING_THE_GROUP_YOU_WISH_TO_MOVE_YOUR_CONTACT_TO_PRESS_THE_OK_BUTTON = new SystemMessage(1320);

    public static final SystemMessage ENTER_THE_NAME_OF_THE_GROUP_YOU_WISH_TO_ADD = new SystemMessage(1321);

    public static final SystemMessage SELECT_THE_GROUP_AND_ENTER_THE_NEW_NAME = new SystemMessage(1322);

    public static final SystemMessage SELECT_THE_GROUP_YOU_WISH_TO_DELETE_AND_CLICK_THE_OK_BUTTON = new SystemMessage(1323);

    public static final SystemMessage SIGNING_IN = new SystemMessage(1324);

    public static final SystemMessage YOUVE_LOGGED_INTO_ANOTHER_COMPUTER_AND_BEEN_LOGGED_OUT_OF_THE_NET_MESSENGER_SERVICE_ON_THIS_COMPUTER = new SystemMessage(1325);

    public static final SystemMessage S1_ = new SystemMessage(1326);

    public static final SystemMessage THE_FOLLOWING_MESSAGE_COULD_NOT_BE_DELIVERED = new SystemMessage(1327);

    public static final SystemMessage THE_LATEST_VERSION_OF_MSN_MESSENGER_MAY_BE_OBTAINED_FROM_THE_MSN_WEB_SITE_ = new SystemMessage(1312);

    public static final SystemMessage S1_TO_BETTER_SERVE_OUR_CUSTOMERS_ALL_CHAT_HISTORIES_ARE_STORED_AND_MAINTAINED_BY_NCSOFT_IF_YOU_DO_NOT_AGREE_TO_HAVE_YOUR_CHAT_RECORDS_STORED_CLOSE_THE_CHAT_WINDOW_NOW_FOR_MORE_INFORMATION_REGARDING_THIS_ISSUE_PLEASE_VISIT_OUR_HOME_PAGE_AT_WWWNCSOFTCOM = new SystemMessage(1313);

    public static final SystemMessage PLEASE_ENTER_THE_PASSPORT_ID_OF_THE_PERSON_YOU_WISH_TO_ADD_TO_YOUR_CONTACT_LIST = new SystemMessage(1314);

    public static final SystemMessage DELETING_A_CONTACT_WILL_REMOVE_THAT_CONTACT_FROM_MSN_MESSENGER_AS_WELL_THE_CONTACT_CAN_STILL_CHECK_YOUR_ONLINE_STATUS_AND_WILL_NOT_BE_BLOCKED_FROM_SENDING_YOU_A_MESSAGE = new SystemMessage(1315);

    public static final SystemMessage THE_CONTACT_WILL_BE_DELETED_AND_BLOCKED_FROM_YOUR_CONTACT_LIST = new SystemMessage(1316);

    public static final SystemMessage WOULD_YOU_LIKE_TO_DELETE_THIS_CONTACT = new SystemMessage(1317);

    public static final SystemMessage PLEASE_SELECT_THE_CONTACT_YOU_WANT_TO_BLOCK_OR_UNBLOCK = new SystemMessage(1318);

    public static final SystemMessage PLEASE_SELECT_THE_NAME_OF_THE_CONTACT_YOU_WISH_TO_CHANGE_TO_ANOTHER_GROUP = new SystemMessage(1319);

    public static final SystemMessage THE_GAME_HAS_BEEN_CANCELLED_BECAUSE_THE_OTHER_PARTY_DOES_NOT_MEET_THE_REQUIREMENTS_FOR_JOINING_THE_GAME = new SystemMessage(1494);

    public static final SystemMessage THE_GAME_WILL_START_IN_S1_SECOND_S = new SystemMessage(1495);

    public static final SystemMessage YOU_WILL_ENTER_THE_OLYMPIAD_STADIUM_IN_S1_SECOND_S = new SystemMessage(1492);

    public static final SystemMessage THE_GAME_HAS_BEEN_CANCELLED_BECAUSE_THE_OTHER_PARTY_ENDS_THE_GAME = new SystemMessage(1493);

    public static final SystemMessage TRADED_S2_OF_CROP_S1 = new SystemMessage(1490);

    public static final SystemMessage FAILED_IN_TRADING_S2_OF_CROP_S1 = new SystemMessage(1491);

    public static final SystemMessage THE_FERRY_FROM_TALKING_ISLAND_TO_GIRAN_HARBOR_HAS_BEEN_DELAYED = new SystemMessage(1488);

    public static final SystemMessage INNADRIL_CRUISE_SERVICE_HAS_BEEN_DELAYED = new SystemMessage(1489);

    public static final SystemMessage YOU_HAVE_ALREADY_BEEN_REGISTERED_IN_A_WAITING_LIST_OF_AN_EVENT = new SystemMessage(1502);

    public static final SystemMessage YOU_HAVE_BEEN_REGISTERED_IN_A_WAITING_LIST_OF_CLASSIFIED_GAMES = new SystemMessage(1503);

    public static final SystemMessage YOU_CANT_JOIN_THE_OLYMPIAD_WITH_A_SUB_JOB_CHARACTER = new SystemMessage(1500);

    public static final SystemMessage ONLY_NOBLESS_CAN_PARTICIPATE_IN_THE_OLYMPIAD = new SystemMessage(1501);

    public static final SystemMessage THE_GAME_ENDED_IN_A_TIE = new SystemMessage(1498);

    public static final SystemMessage YOU_WILL_GO_BACK_TO_THE_VILLAGE_IN_S1_SECOND_S = new SystemMessage(1499);

    public static final SystemMessage STARTS_THE_GAME = new SystemMessage(1496);

    public static final SystemMessage S1_HAS_WON_THE_GAME = new SystemMessage(1497);

    public static final SystemMessage THIS_FISHING_SHOT_IS_NOT_FIT_FOR_THE_FISHING_POLE_CRYSTAL = new SystemMessage(1479);

    public static final SystemMessage YOU_CAN_REGISTER_ONLY_256_COLOR_BMP_FILES_WITH_A_SIZE_OF_64X64 = new SystemMessage(1478);

    public static final SystemMessage PET_HAS_DROPPED_S2_OF_S1 = new SystemMessage(1477);

    public static final SystemMessage PET_HAS_DROPPED__S1S2 = new SystemMessage(1476);

    public static final SystemMessage PET_HAS_DROPPED_S1 = new SystemMessage(1475);

    public static final SystemMessage S1_IS_NOT_AVAILABLE = new SystemMessage(1474);

    public static final SystemMessage S1_IS_NOT_SUFFICIENT = new SystemMessage(1473);

    public static final SystemMessage YOU_CANT_MAKE_AN_ATTACK_WITH_A_FISHING_POLE = new SystemMessage(1472);

    public static final SystemMessage THE_FERRY_FROM_GIRAN_HARBOR_TO_TALKING_ISLAND_HAS_BEEN_DELAYED = new SystemMessage(1487);

    public static final SystemMessage THE_FERRY_FROM_GLUDIN_HARBOR_TO_TALKING_ISLAND_HAS_BEEN_DELAYED = new SystemMessage(1486);

    public static final SystemMessage THE_FERRY_FROM_TALKING_ISLAND_TO_GLUDIN_HARBOR_HAS_BEEN_DELAYED = new SystemMessage(1485);

    public static final SystemMessage DO_YOU_WANT_TO_USE_THE_HEROES_WEAPON_THAT_YOU_CHOSE = new SystemMessage(1484);

    public static final SystemMessage DO_YOU_WANT_TO_BECOME_A_HERO_NOW = new SystemMessage(1483);

    public static final SystemMessage YOU_HAVE_BEEN_SELECTED_FOR_CLASSIFIED_GAME_DO_YOU_WANT_TO_JOIN = new SystemMessage(1482);

    public static final SystemMessage YOU_HAVE_BEEN_SELECTED_FOR_NO_CLASS_GAME_DO_YOU_WANT_TO_JOIN = new SystemMessage(1481);

    public static final SystemMessage DO_YOU_WANT_TO_CANCEL_YOUR_APPLICATION_FOR_JOINING_THE_GRAND_OLYMPIAD = new SystemMessage(1480);

    public static final SystemMessage PET_OF_S1_GAINED_S2 = new SystemMessage(1524);

    public static final SystemMessage PET_OF_S1_GAINED_S3_OF_S2 = new SystemMessage(1525);

    public static final SystemMessage PET_OF_S1_GAINED__S2S3 = new SystemMessage(1526);

    public static final SystemMessage PET_TOOK_S1_BECAUSE_HE_WAS_HUNGRY = new SystemMessage(1527);

    public static final SystemMessage SERVITOR_PASSED_AWAY = new SystemMessage(1520);

    public static final SystemMessage SERVITOR_DISAPPEASR_BECAUSE_THE_SUMMONING_TIME_IS_OVER = new SystemMessage(1521);

    public static final SystemMessage THE_CORPSE_DISAPPEARED_BECAUSE_MUCH_TIME_PASSED_AFTER_PET_DIED = new SystemMessage(1522);

    public static final SystemMessage BECAUSE_PET_OR_SERVITOR_MAY_BE_DROWNED_WHILE_THE_BOAT_MOVES_PLEASE_RELEASE_THE_SUMMON_BEFORE_DEPARTURE = new SystemMessage(1523);

    public static final SystemMessage ENTER_THE_NAME_OF_CLAN_AGAINST_WHICH_YOU_WANT_TO_STOP_THE_WAR = new SystemMessage(1532);

    public static final SystemMessage ATTENTION_S1_PICKED_UP_S2 = new SystemMessage(1533);

    public static final SystemMessage ATTENTION_S1_PICKED_UP__S2_S3 = new SystemMessage(1534);

    public static final SystemMessage ATTENTION_S1_PET_PICKED_UP_S2 = new SystemMessage(1535);

    public static final SystemMessage A_FORCIBLE_PETITION_FROM_GM_HAS_BEEN_RECEIVED = new SystemMessage(1528);

    public static final SystemMessage S1_HAS_INVITED_YOU_TO_THE_COMMAND_CHANNEL_DO_YOU_WANT_TO_JOIN = new SystemMessage(1529);

    public static final SystemMessage SELECT_A_TARGET_OR_ENTER_THE_NAME = new SystemMessage(1530);

    public static final SystemMessage ENTER_THE_NAME_OF_CLAN_AGAINST_WHICH_YOU_WANT_TO_MAKE_AN_ATTACK = new SystemMessage(1531);

    public static final SystemMessage THIS_SKILL_IS_NOT_AVAILABLE_FOR_THE_OLYMPIAD_EVENT = new SystemMessage(1509);

    public static final SystemMessage THIS_ITEM_IS_NOT_AVAILABLE_FOR_THE_OLYMPIAD_EVENT = new SystemMessage(1508);

    public static final SystemMessage WHILE_A_PET_IS_ATTEMPTING_TO_RESURRECT_IT_CANNOT_HELP_IN_RESURRECTING_ITS_MASTER = new SystemMessage(1511);

    public static final SystemMessage S1_IS_MAKING_AN_ATTEMPT_AT_RESURRECTION_WITH_$S2_EXPERIENCE_POINTS_DO_YOU_WANT_TO_CONTINUE_WITH_THIS_RESURRECTION = new SystemMessage(1510);

    public static final SystemMessage YOU_HAVE_BEEN_DELETED_FROM_THE_WAITING_LIST_OF_A_GAME = new SystemMessage(1505);

    public static final SystemMessage YOU_HAVE_BEEN_REGISTERED_IN_A_WAITING_LIST_OF_NO_CLASS_GAMES = new SystemMessage(1504);

    public static final SystemMessage THIS_ITEM_CANT_BE_EQUIPPED_FOR_THE_OLYMPIAD_EVENT = new SystemMessage(1507);

    public static final SystemMessage YOU_HAVE_NOT_BEEN_REGISTERED_IN_A_WAITING_LIST_OF_A_GAME = new SystemMessage(1506);

    public static final SystemMessage FAILED_IN_BLESSED_ENCHANT_THE_ENCHANT_VALUE_OF_THE_ITEM_BECAME_0 = new SystemMessage(1517);

    public static final SystemMessage THE_TARGET_IS_UNAVAILABLE_FOR_SEEDING = new SystemMessage(1516);

    public static final SystemMessage THE_PET_HAS_BEEN_KILLED_IF_YOU_DO_NOT_RESURRECT_IT_WITHIN_24_HOURS_THE_PETS_BODY_WILL_DISAPPEAR_ALONG_WITH_ALL_THE_PETS_ITEMS = new SystemMessage(1519);

    public static final SystemMessage YOU_DO_NOT_MEET_THE_REQUIRED_CONDITION_TO_EQUIP_THAT_ITEM = new SystemMessage(1518);

    public static final SystemMessage BETTER_RESURRECTION_HAS_BEEN_ALREADY_PROPOSED = new SystemMessage(1513);

    public static final SystemMessage WHILE_A_PETS_MASTER_IS_ATTEMPTING_TO_RESURRECT_THE_PET_CANNOT_BE_RESURRECTED_AT_THE_SAME_TIME = new SystemMessage(1512);

    public static final SystemMessage SINCE_THE_MASTER_WAS_IN_THE_PROCESS_OF_BEING_RESURRECTED_THE_ATTEMPT_TO_RESURRECT_THE_PET_HAS_BEEN_CANCELLED = new SystemMessage(1515);

    public static final SystemMessage SINCE_THE_PET_WAS_IN_THE_PROCESS_OF_BEING_RESURRECTED_THE_ATTEMPT_TO_RESURRECT_ITS_MASTER_HAS_BEEN_CANCELLED = new SystemMessage(1514);

    public static final SystemMessage RACE_SETUP_FILE_ERROR__EXPSKILLCNT_IS_NOT_SPECIFIED = new SystemMessage(1426);

    public static final SystemMessage RACE_SETUP_FILE_ERROR__EXPSKILLIDS1_IS_NOT_SPECIFIED = new SystemMessage(1427);

    public static final SystemMessage RACE_SETUP_FILE_ERROR__BUFFLVS1_IS_NOT_SPECIFIED = new SystemMessage(1424);

    public static final SystemMessage RACE_SETUP_FILE_ERROR__DEFAULTALLOW_IS_NOT_SPECIFIED = new SystemMessage(1425);

    public static final SystemMessage RACE_SETUP_FILE_ERROR__TELEPORTDELAY_IS_NOT_SPECIFIED = new SystemMessage(1430);

    public static final SystemMessage THE_RACE_WILL_BE_STOPPED_TEMPORARILY = new SystemMessage(1431);

    public static final SystemMessage RACE_SETUP_FILE_ERROR__EXPITEMCNT_IS_NOT_SPECIFIED = new SystemMessage(1428);

    public static final SystemMessage RACE_SETUP_FILE_ERROR__EXPITEMIDS1_IS_NOT_SPECIFIED = new SystemMessage(1429);

    public static final SystemMessage THE_AUTOMATIC_USE_OF_S1_WILL_NOW_BE_CANCELLED = new SystemMessage(1434);

    public static final SystemMessage DUE_TO_INSUFFICIENT_S1_THE_AUTOMATIC_USE_FUNCTION_HAS_BEEN_CANCELLED = new SystemMessage(1435);

    public static final SystemMessage YOUR_OPPONENT_IS_CURRENTLY_IN_A_PETRIFIED_STATE = new SystemMessage(1432);

    public static final SystemMessage THE_USE_OF_S1_WILL_NOW_BE_AUTOMATED = new SystemMessage(1433);

    public static final SystemMessage THERE_IS_NO_SKILL_THAT_ENABLES_ENCHANT = new SystemMessage(1438);

    public static final SystemMessage ITEMS_REQUIRED_FOR_SKILL_ENCHANT_ARE_INSUFFICIENT = new SystemMessage(1439);

    public static final SystemMessage DUE_TO_INSUFFICIENT_S1_THE_AUTOMATIC_USE_FUNCTION_CANNOT_BE_ACTIVATED = new SystemMessage(1436);

    public static final SystemMessage PLAYERS_ARE_NO_LONGER_ALLOWED_TO_PLACE_DICE_DICE_CANNOT_BE_PURCHASED_FROM_A_VILLAGE_STORE_ANY_MORE_HOWEVER_YOU_CAN_STILL_SELL_THEM_TO_A_STORE_IN_A_VILLAGE = new SystemMessage(1437);

    public static final SystemMessage THE_NUMBER_OF_YOUR_PREPAID_RESERVATIONS_HAS_CHANGED = new SystemMessage(1411);

    public static final SystemMessage YOUR_PREPAID_USAGE_TIME_HAS_EXPIRED_YOU_DO_NOT_HAVE_ANY_MORE_PREPAID_RESERVATIONS_LEFT = new SystemMessage(1410);

    public static final SystemMessage YOUR_PREPAID_USAGE_TIME_HAS_EXPIRED_YOUR_NEW_PREPAID_RESERVATION_WILL_BE_USED_THE_REMAINING_USAGE_TIME_IS_S1_HOURS_AND_S2_MINUTES = new SystemMessage(1409);

    public static final SystemMessage YOUR_PREPAID_REMAINING_USAGE_TIME_IS_S1_HOURS_AND_S2_MINUTES__YOU_HAVE_S3_PAID_RESERVATIONS_LEFT = new SystemMessage(1408);

    public static final SystemMessage THE_COMMAND_FILE_IS_NOT_SET = new SystemMessage(1415);

    public static final SystemMessage THE_WIDTH_AND_LENGTH_SHOULD_BE_100_OR_MORE_GRIDS_AND_LESS_THAN_5000_GRIDS_RESPECTIVELY = new SystemMessage(1414);

    public static final SystemMessage SINCE_YOU_DO_NOT_MEET_THE_REQUIREMENTS_YOU_ARE_NOT_ALLOWED_TO_ENTER_THE_PARTY_ROOM = new SystemMessage(1413);

    public static final SystemMessage YOUR_PREPAID_USAGE_TIME_HAS_S1_MINUTES_LEFT = new SystemMessage(1412);

    public static final SystemMessage THE_NAME_OF_TEAM_2_HAS_NOT_YET_BEEN_CHOSEN = new SystemMessage(1419);

    public static final SystemMessage THE_NAME_OF_TEAM_1_HAS_NOT_YET_BEEN_CHOSEN = new SystemMessage(1418);

    public static final SystemMessage THE_PARTY_REPRESENTATIVE_OF_TEAM_2_HAS_NOT_BEEN_SELECTED = new SystemMessage(1417);

    public static final SystemMessage THE_PARTY_REPRESENTATIVE_OF_TEAM_1_HAS_NOT_BEEN_SELECTED = new SystemMessage(1416);

    public static final SystemMessage RACE_SETUP_FILE_ERROR__BUFFIDS1_IS_NOT_SPECIFIED = new SystemMessage(1423);

    public static final SystemMessage RACE_SETUP_FILE_ERROR__BUFFCNT_IS_NOT_SPECIFIED = new SystemMessage(1422);

    public static final SystemMessage THE_RACE_SETUP_FILE_HAS_NOT_BEEN_DESIGNATED = new SystemMessage(1421);

    public static final SystemMessage THE_NAME_OF_TEAM_1_AND_THE_NAME_OF_TEAM_2_ARE_IDENTICAL = new SystemMessage(1420);

    public static final SystemMessage YOU_CANT_FISH_WHILE_YOU_ARE_ON_BOARD = new SystemMessage(1456);

    public static final SystemMessage YOU_CANT_FISH_HERE = new SystemMessage(1457);

    public static final SystemMessage CANCELS_FISHING = new SystemMessage(1458);

    public static final SystemMessage NOT_ENOUGH_BAIT = new SystemMessage(1459);

    public static final SystemMessage ENDS_FISHING = new SystemMessage(1460);

    public static final SystemMessage STARTS_FISHING = new SystemMessage(1461);

    public static final SystemMessage PUMPING_SKILL_IS_AVAILABLE_ONLY_WHILE_FISHING = new SystemMessage(1462);

    public static final SystemMessage REELING_SKILL_IS_AVAILABLE_ONLY_WHILE_FISHING = new SystemMessage(1463);

    public static final SystemMessage FISH_HAS_RESISTED = new SystemMessage(1464);

    public static final SystemMessage PUMPING_IS_SUCCESSFUL_DAMAGE_S1 = new SystemMessage(1465);

    public static final SystemMessage PUMPING_FAILED_DAMAGE_S1 = new SystemMessage(1466);

    public static final SystemMessage REELING_IS_SUCCESSFUL_DAMAGE_S1 = new SystemMessage(1467);

    public static final SystemMessage REELING_FAILED_DAMAGE_S1 = new SystemMessage(1468);

    public static final SystemMessage SUCCEEDED_IN_FISHING = new SystemMessage(1469);

    public static final SystemMessage YOU_CANNOT_DO_THAT_WHILE_FISHING = new SystemMessage(1470);

    public static final SystemMessage YOU_CANNOT_DO_ANYTHING_ELSE_WHILE_FISHING = new SystemMessage(1471);

    public static final SystemMessage FAILED_IN_ENCHANTING_SKILL_S1 = new SystemMessage(1441);

    public static final SystemMessage SUCCEEDED_IN_ENCHANTING_SKILL_S1 = new SystemMessage(1440);

    public static final SystemMessage SP_REQUIRED_FOR_SKILL_ENCHANT_IS_INSUFFICIENT = new SystemMessage(1443);

    public static final SystemMessage REMAINING_TIME_S1_SECOND = new SystemMessage(1442);

    public static final SystemMessage YOUR_PREVIOUS_SUB_CLASS_WILL_BE_DELETED_AND_YOUR_NEW_SUB_CLASS_WILL_START_AT_LEVEL_40__DO_YOU_WISH_TO_PROCEED = new SystemMessage(1445);

    public static final SystemMessage EXP_REQUIRED_FOR_SKILL_ENCHANT_IS_INSUFFICIENT = new SystemMessage(1444);

    public static final SystemMessage OTHER_SKILLS_ARE_NOT_AVAILABLE_WHILE_FISHING = new SystemMessage(1447);

    public static final SystemMessage THE_FERRY_FROM_S1_TO_S2_HAS_BEEN_DELAYED = new SystemMessage(1446);

    public static final SystemMessage SUCCEEDED_IN_GETTING_A_BITE = new SystemMessage(1449);

    public static final SystemMessage ONLY_FISHING_SKILLS_ARE_AVAILABLE = new SystemMessage(1448);

    public static final SystemMessage THE_FISH_GOT_AWAY = new SystemMessage(1451);

    public static final SystemMessage TIME_IS_UP_SO_THAT_FISH_GOT_AWAY = new SystemMessage(1450);

    public static final SystemMessage FISHING_POLES_ARE_NOT_INSTALLED = new SystemMessage(1453);

    public static final SystemMessage BAITS_HAVE_BEEN_LOST_BECAUSE_THE_FISH_GOT_AWAY = new SystemMessage(1452);

    public static final SystemMessage YOU_CANT_FISH_IN_WATER = new SystemMessage(1455);

    public static final SystemMessage BAITS_ARE_NOT_PUT_ON_A_HOOK = new SystemMessage(1454);

    public static final SystemMessage THE_OLYMPIAD_GAME_HAS_ENDED = new SystemMessage(1642);

    public static final SystemMessage CURRENT_LOCATION_S1_S2_S3_DIMENSION_GAP = new SystemMessage(1643);

    public static final SystemMessage OLYMPIAD_PERIOD_S1_HAS_ENDED = new SystemMessage(1640);

    public static final SystemMessage THE_OLYMPIAD_GAME_HAS_STARTED = new SystemMessage(1641);

    public static final SystemMessage NONE_1646 = new SystemMessage(1646);

    public static final SystemMessage NONE_1647 = new SystemMessage(1647);

    public static final SystemMessage NONE_1644 = new SystemMessage(1644);

    public static final SystemMessage NONE_1645 = new SystemMessage(1645);

    public static final SystemMessage THE_FERRY_FROM_GLUDIN_HARBOR_WILL_BE_ARRIVING_AT_RUNE_HARBOR_IN_APPROXIMATELY_15_MINUTES = new SystemMessage(1634);

    public static final SystemMessage THE_FERRY_FROM_GLUDIN_HARBOR_WILL_BE_ARRIVING_AT_RUNE_HARBOR_IN_APPROXIMATELY_10_MINUTES = new SystemMessage(1635);

    public static final SystemMessage THE_FERRY_FROM_RUNE_HARBOR_WILL_BE_ARRIVING_AT_GLUDIN_HARBOR_IN_APPROXIMATELY_5_MINUTES = new SystemMessage(1632);

    public static final SystemMessage THE_FERRY_FROM_RUNE_HARBOR_WILL_BE_ARRIVING_AT_GLUDIN_HARBOR_IN_APPROXIMATELY_1_MINUTE = new SystemMessage(1633);

    public static final SystemMessage YOU_CANNOT_FISH_WHILE_USING_A_RECIPE_BOOK_PRIVATE_MANUFACTURE_OR_PRIVATE_STORE = new SystemMessage(1638);

    public static final SystemMessage OLYMPIAD_PERIOD_S1_HAS_STARTED = new SystemMessage(1639);

    public static final SystemMessage THE_FERRY_FROM_GLUDIN_HARBOR_WILL_BE_ARRIVING_AT_RUNE_HARBOR_IN_APPROXIMATELY_5_MINUTES = new SystemMessage(1636);

    public static final SystemMessage THE_FERRY_FROM_GLUDIN_HARBOR_WILL_BE_ARRIVING_AT_RUNE_HARBOR_IN_APPROXIMATELY_1_MINUTE = new SystemMessage(1637);

    public static final SystemMessage CURRENT_LOCATION_S1_S2_S3_CEMETERY_OF_THE_EMPIRE = new SystemMessage(1659);

    public static final SystemMessage C1_HAS_LOST_S2_POINTS_IN_THE_GRAND_OLYMPIAD_GAMES = new SystemMessage(1658);

    public static final SystemMessage C1_HAS_EARNED_S2_POINTS_IN_THE_GRAND_OLYMPIAD_GAMES = new SystemMessage(1657);

    public static final SystemMessage YOU_HAVE_SUCCESSFULLY_TRADED_THE_ITEM_WITH_THE_NPC = new SystemMessage(1656);

    public static final SystemMessage THE_CLANS_EMBLEM_WAS_SUCCESSFULLY_REGISTERED__ONLY_A_CLAN_THAT_OWNS_A_CLAN_HALL_OR_A_CASTLE_CAN_GET_THEIR_EMBLEM_DISPLAYED_ON_CLAN_RELATED_ITEMS = new SystemMessage(1663);

    public static final SystemMessage IF_YOU_FISH_IN_ONE_SPOT_FOR_A_LONG_TIME_THE_SUCCESS_RATE_OF_A_FISH_TAKING_THE_BAIT_BECOMES_LOWER__PLEASE_MOVE_TO_ANOTHER_PLACE_AND_CONTINUE_YOUR_FISHING_THERE = new SystemMessage(1662);

    public static final SystemMessage S1_HAS_OBTAINED_S3_S2S = new SystemMessage(1661);

    public static final SystemMessage THE_CHANNEL_WAS_OPENED_BY_S1 = new SystemMessage(1660);

    public static final SystemMessage THE_OLYMPIAD_GAME_IS_NOT_CURRENTLY_IN_PROGRESS = new SystemMessage(1651);

    public static final SystemMessage DUE_TO_A_LARGE_NUMBER_OF_USERS_CURRENTLY_ACCESSING_OUR_SERVER_YOUR_LOGIN_ATTEMPT_HAS_FAILED_PLEASE_WAIT_A_LITTLE_WHILE_AND_ATTEMPT_TO_LOG_IN_AGAIN = new SystemMessage(1650);

    public static final SystemMessage PLAY_TIME_IS_NOW_ACCUMULATING = new SystemMessage(1649);

    public static final SystemMessage NONE_1648 = new SystemMessage(1648);

    public static final SystemMessage YOU_HAVE_CAUGHT_A_MONSTER = new SystemMessage(1655);

    public static final SystemMessage THE_ATTEMPT_TO_RECORD_THE_REPLAY_FILE_HAS_FAILED = new SystemMessage(1654);

    public static final SystemMessage THE_REPLAY_FILE_HAS_BEEN_STORED_SUCCESSFULLY_S1 = new SystemMessage(1653);

    public static final SystemMessage THE_VIDEO_RECORDING_OF_THE_REPLAY_WILL_NOW_BEGIN = new SystemMessage(1652);

    public static final SystemMessage PRODUCT_PURCHASE_ERROR___THE_PRODUCT_IS_NOT_RIGHT = new SystemMessage(6008);

    public static final SystemMessage YOU_CANT_BUY_ANOTHER_CASTLE_SINCE_ADENA_IS_NOT_SUFFICIENT = new SystemMessage(1608);

    public static final SystemMessage PRODUCT_PURCHASE_ERROR___THE_ITEM_WITHIN_THE_PRODUCT_IS_NOT_RIGHT = new SystemMessage(6009);

    public static final SystemMessage THE_DECLARATION_OF_WAR_HAS_BEEN_ALREADY_MADE_TO_THE_CLAN = new SystemMessage(1609);

    public static final SystemMessage THE_MASTER_ACCOUNT_OF_YOUR_ACCOUNT_HAS_BEEN_RESTRICTED = new SystemMessage(6010);

    public static final SystemMessage FOOL_YOU_CANNOT_DECLARE_WAR_AGAINST_YOUR_OWN_CLAN = new SystemMessage(1610);

    public static final SystemMessage YOU_ACQUIRED_S1_EXP_AND_S2_SP_AS_A_REWARD_YOU_RECEIVE_S3_MORE_EXP = new SystemMessage(6011);

    public static final SystemMessage PARTY_LEADER_S1 = new SystemMessage(1611);

    public static final SystemMessage A_BLESSING_THAT_INCREASES_EXP_BY_1_2 = new SystemMessage(6012);

    public static final SystemMessage _WAR_LIST_ = new SystemMessage(1612);

    public static final SystemMessage IT_IS_NOT_A_BLESSING_PERIOD_WHEN_YOU_REACH_TODAY_S_TARGET_YOU_CAN_RECEIVE_S1 = new SystemMessage(6013);

    public static final SystemMessage THERE_IS_NO_CLAN_LISTED_ON_WAR_LIST = new SystemMessage(1613);

    public static final SystemMessage IT_IS_EVA_S_BLESSING_PERIOD_S1_WILL_BE_EFFECTIVE_UNTIL_S2 = new SystemMessage(6014);

    public static final SystemMessage YOU_ARE_PARTICIPATING_IN_THE_CHANNEL_WHICH_HAS_BEEN_ALREADY_OPENED = new SystemMessage(1614);

    public static final SystemMessage IT_IS_EVA_S_BLESSING_PERIOD_UNTIL_S1_JACK_SAGE_CAN_GIFT_YOU_WITH_S2 = new SystemMessage(6015);

    public static final SystemMessage THE_NUMBER_OF_REMAINING_PARTIES_IS_S1_UNTIL_A_CHANNEL_IS_ACTIVATED = new SystemMessage(1615);

    public static final SystemMessage TOMORROWS_ITEMS_WILL_ALL_BE_SET_TO_0__DO_YOU_WISH_TO_CONTINUE = new SystemMessage(1600);

    public static final SystemMessage THE_ITEM_HAS_BEEN_SUCCESSFULLY_PURCHASED = new SystemMessage(6001);

    public static final SystemMessage TOMORROWS_ITEMS_WILL_ALL_BE_SET_TO_THE_SAME_VALUE_AS_TODAYS_ITEMS__DO_YOU_WISH_TO_CONTINUE = new SystemMessage(1601);

    public static final SystemMessage THE_ITEM_HAS_FAILED_TO_BE_PURCHASED = new SystemMessage(6002);

    public static final SystemMessage ONLY_A_PARTY_LEADER_CAN_ACCESS_THE_COMMAND_CHANNEL = new SystemMessage(1602);

    public static final SystemMessage THE_ITEM_YOU_SELECTED_CANNOT_BE_PURCHASED_UNFORTUNATELY_THE_SALE_PERIOD_ENDED = new SystemMessage(6003);

    public static final SystemMessage ONLY_CHANNEL_OPENER_CAN_GIVE_ALL_COMMAND = new SystemMessage(1603);

    public static final SystemMessage ENCHANT_FAILED_THE_ENCHANT_SKILL_FOR_THE_CORRESPONDING_ITEM_WILL_BE_EXACTLY_RETAINED = new SystemMessage(6004);

    public static final SystemMessage WHILE_DRESSED_IN_FORMAL_WEAR_YOU_CANT_USE_ITEMS_THAT_REQUIRE_ALL_SKILLS_AND_CASTING_OPERATIONS = new SystemMessage(1604);

    public static final SystemMessage GAME_POINTS_ARE_NOT_ENOUGH = new SystemMessage(6005);

    public static final SystemMessage _HERE_YOU_CAN_BUY_ONLY_SEEDS_OF_S1_MANOR = new SystemMessage(1605);

    public static final SystemMessage THE_ITEM_CANNOT_BE_RECEIVED_BECAUSE_THE_INVENTORY_WEIGHT_QUANTITY_LIMIT_HAS_BEEN_EXCEEDED = new SystemMessage(6006);

    public static final SystemMessage YOU_HAVE_COMPLETED_THE_QUEST_FOR_3RD_OCCUPATION_CHANGE_AND_MOVED_TO_ANOTHER_CLASS_CONGRATULATIONS = new SystemMessage(1606);

    public static final SystemMessage PRODUCT_PURCHASE_ERROR___THE_USER_STATE_IS_NOT_RIGHT = new SystemMessage(6007);

    public static final SystemMessage S1_ADENA_HAS_BEEN_PAID_FOR_PURCHASING_FEES = new SystemMessage(1607);

    public static final SystemMessage WILL_LEAVE_FOR_RUNE_HARBOR_AFTER_ANCHORING_FOR_TEN_MINUTES = new SystemMessage(1625);

    public static final SystemMessage WE_ARE_NOW_DEPARTING_FOR_GLUDIN_HARBOR__HOLD_ON_AND_ENJOY_THE_RIDE = new SystemMessage(1624);

    public static final SystemMessage WILL_LEAVE_FOR_RUNE_HARBOR_IN_ONE_MINUTE = new SystemMessage(1627);

    public static final SystemMessage WILL_LEAVE_FOR_RUNE_HARBOR_IN_FIVE_MINUTES = new SystemMessage(1626);

    public static final SystemMessage LEAVING_FOR_RUNE_HARBOR = new SystemMessage(1629);

    public static final SystemMessage LEAVING_SOON_FOR_RUNE_HARBOR = new SystemMessage(1628);

    public static final SystemMessage THE_FERRY_FROM_RUNE_HARBOR_WILL_BE_ARRIVING_AT_GLUDIN_HARBOR_IN_APPROXIMATELY_10_MINUTES = new SystemMessage(1631);

    public static final SystemMessage THE_FERRY_FROM_RUNE_HARBOR_WILL_BE_ARRIVING_AT_GLUDIN_HARBOR_IN_APPROXIMATELY_15_MINUTES = new SystemMessage(1630);

    public static final SystemMessage YOU_DO_NOT_HAVE_AUTHORITY_TO_USE_THE_COMMAND_CHANNEL = new SystemMessage(1617);

    public static final SystemMessage THE_COMMAND_CHANNEL_HAS_BEEN_ACTIVATED = new SystemMessage(1616);

    public static final SystemMessage THE_FERRY_FROM_GLUDIN_HARBOR_TO_RUNE_HARBOR_HAS_BEEN_DELAYED = new SystemMessage(1619);

    public static final SystemMessage THE_FERRY_FROM_RUNE_HARBOR_TO_GLUDIN_HARBOR_HAS_BEEN_DELAYED = new SystemMessage(1618);

    public static final SystemMessage DEPARTURE_FOR_GLUDIN_HARBOR_WILL_TAKE_PLACE_IN_FIVE_MINUTES = new SystemMessage(1621);

    public static final SystemMessage ARRIVED_AT_RUNE_HARBOR = new SystemMessage(1620);

    public static final SystemMessage MAKE_HASTE__WE_WILL_BE_DEPARTING_FOR_GLUDIN_HARBOR_SHORTLY = new SystemMessage(1623);

    public static final SystemMessage DEPARTURE_FOR_GLUDIN_HARBOR_WILL_TAKE_PLACE_IN_ONE_MINUTE = new SystemMessage(1622);

    public static final SystemMessage YOU_HAVE_PARTICIPATED_IN_THE_COMMAND_CHANNEL = new SystemMessage(1582);

    public static final SystemMessage YOU_WERE_DISMISSED_FROM_THE_COMMAND_CHANNEL = new SystemMessage(1583);

    public static final SystemMessage THE_COMMAND_CHANNEL_HAS_BEEN_FORMED = new SystemMessage(1580);

    public static final SystemMessage THE_COMMAND_CHANNEL_HAS_BEEN_DISBANDED = new SystemMessage(1581);

    public static final SystemMessage ITEMS_ARE_NOT_AVAILABLE_FOR_A_PRIVATE_STORE_OR_PRIVATE_MANUFACTURE = new SystemMessage(1578);

    public static final SystemMessage S1_PET_GAINED_S2_ADENA = new SystemMessage(1579);

    public static final SystemMessage PET_USES_THE_POWER_OF_SPIRIT = new SystemMessage(1576);

    public static final SystemMessage SERVITOR_USES_THE_POWER_OF_SPIRIT = new SystemMessage(1577);

    public static final SystemMessage THERE_IS_NO_UNDER_ATTACK_CLAN = new SystemMessage(1574);

    public static final SystemMessage COMMAND_CHANNELS_CAN_ONLY_BE_FORMED_BY_A_PARTY_LEADER_WHO_IS_ALSO_THE_LEADER_OF_A_LEVEL_5_CLAN = new SystemMessage(1575);

    public static final SystemMessage _UNDER_ATTACK_LIST_ = new SystemMessage(1572);

    public static final SystemMessage THERE_IS_NO_ATTACK_CLAN = new SystemMessage(1573);

    public static final SystemMessage A_DECLARATION_OF_WAR_AGAINST_MORE_THAN_30_CLANS_CANT_BE_MADE_AT_THE_SAME_TIME = new SystemMessage(1570);

    public static final SystemMessage _ATTACK_LIST_ = new SystemMessage(1571);

    public static final SystemMessage THE_TARGET_FOR_DECLARATION_IS_WRONG = new SystemMessage(1568);

    public static final SystemMessage A_DECLARATION_OF_CLAN_WAR_AGAINST_AN_ALLIED_CLAN_CANT_BE_MADE = new SystemMessage(1569);

    public static final SystemMessage WATCHING_IS_IMPOSSIBLE_DURING_COMBAT = new SystemMessage(1599);

    public static final SystemMessage WHEN_PET_OR_SERVITOR_IS_DEAD_SOULSHOTS_OR_SPIRITSHOTS_FOR_PET_OR_SERVITOR_ARE_NOT_AVAILABLE = new SystemMessage(1598);

    public static final SystemMessage S1_HAS_FAILED = new SystemMessage(1597);

    public static final SystemMessage HIT_BY_S1 = new SystemMessage(1596);

    public static final SystemMessage S1_HAS_SUCCEEDED = new SystemMessage(1595);

    public static final SystemMessage S1_PARTY_IS_ALREADY_A_MEMBER_OF_THE_COMMAND_CHANNEL = new SystemMessage(1594);

    public static final SystemMessage YOU_DO_NOT_HAVE_AUTHORITY_TO_INVITE_SOMEONE_TO_THE_COMMAND_CHANNEL = new SystemMessage(1593);

    public static final SystemMessage YOU_CANT_OPEN_COMMAND_CHANNELS_ANY_MORE = new SystemMessage(1592);

    public static final SystemMessage NO_USER_HAS_BEEN_INVITED_TO_THE_COMMAND_CHANNEL = new SystemMessage(1591);

    public static final SystemMessage _COMMAND_CHANNEL_INFO_TOTAL_PARTIES_S1_ = new SystemMessage(1590);

    public static final SystemMessage COMMAND_CHANNEL_AUTHORITY_HAS_BEEN_TRANSFERRED_TO_S1 = new SystemMessage(1589);

    public static final SystemMessage THE_COMMAND_CHANNEL_IS_ACTIVATED_ONLY_IF_AT_LEAST_FIVE_PARTIES_PARTICIPATE_IN = new SystemMessage(1588);

    public static final SystemMessage S1_PARTY_HAS_LEFT_THE_COMMAND_CHANNEL = new SystemMessage(1587);

    public static final SystemMessage YOU_HAVE_QUIT_THE_COMMAND_CHANNEL = new SystemMessage(1586);

    public static final SystemMessage THE_COMMAND_CHANNEL_HAS_BEEN_DEACTIVATED = new SystemMessage(1585);

    public static final SystemMessage S1_PARTY_HAS_BEEN_DISMISSED_FROM_THE_COMMAND_CHANNEL = new SystemMessage(1584);

    public static final SystemMessage CARGO_HAS_ARRIVED_AT_FLORAN_VILLAGE = new SystemMessage(1548);

    public static final SystemMessage CARGO_HAS_ARRIVED_AT_GLUDIN_VILLAGE = new SystemMessage(1549);

    public static final SystemMessage CARGO_HAS_ARRIVED_AT_GLUDIO_CASTLE_TOWN = new SystemMessage(1550);

    public static final SystemMessage CARGO_HAS_ARRIVED_AT_GIRAN_CASTLE_TOWN = new SystemMessage(1551);

    public static final SystemMessage CARGO_HAS_ARRIVED_AT_ADEN_CASTLE_TOWN = new SystemMessage(1544);

    public static final SystemMessage CARGO_HAS_ARRIVED_AT_OREN_CASTLE_TOWN = new SystemMessage(1545);

    public static final SystemMessage CARGO_HAS_ARRIVED_AT_HUNTERS_VILLAGE = new SystemMessage(1546);

    public static final SystemMessage CARGO_HAS_ARRIVED_AT_DION_CASTLE_TOWN = new SystemMessage(1547);

    public static final SystemMessage CARGO_HAS_ARRIVED_AT_DARK_ELVEN_VILLAGE = new SystemMessage(1540);

    public static final SystemMessage CARGO_HAS_ARRIVED_AT_ELVEN_VILLAGE = new SystemMessage(1541);

    public static final SystemMessage CARGO_HAS_ARRIVED_AT_ORC_VILLAGE = new SystemMessage(1542);

    public static final SystemMessage CARGO_HAS_ARRIVED_AT_DWARVEN_VILLAGE = new SystemMessage(1543);

    public static final SystemMessage ATTENTION_S1_PET_PICKED_UP__S2_S3 = new SystemMessage(1536);

    public static final SystemMessage CURRENT_LOCATION__S1_S2_S3_NEAR_RUNE_VILLAGE = new SystemMessage(1537);

    public static final SystemMessage CURRENT_LOCATION__S1_S2_S3_NEAR_GODDARD_CASTLE_TOWN = new SystemMessage(1538);

    public static final SystemMessage CARGO_HAS_ARRIVED_AT_TALKING_ISLAND_VILLAGE = new SystemMessage(1539);

    public static final SystemMessage THE_DECLARATION_OF_WAR_CANT_BE_MADE_BECAUSE_THE_CLAN_DOES_NOT_EXIST_OR_ACT_FOR_A_LONG_PERIOD = new SystemMessage(1565);

    public static final SystemMessage A_CLAN_WAR_CAN_BE_DECLARED_ONLY_IF_THE_CLAN_IS_LEVEL_THREE_OR_ABOVE_AND_THE_NUMBER_OF_CLAN_MEMBERS_IS_FIFTEEN_OR_GREATER = new SystemMessage(1564);

    public static final SystemMessage THE_WAR_AGAINST_S1_CLAN_HAS_BEEN_STOPPED = new SystemMessage(1567);

    public static final SystemMessage S1_CLAN_HAS_STOPPED_THE_WAR = new SystemMessage(1566);

    public static final SystemMessage S1_CLAN_HAS_DECLARED_CLAN_WAR = new SystemMessage(1561);

    public static final SystemMessage THE_QUANTITY_OF_CROP_SHOULD_BE_MORE_THAN_S1_AND_LESS_THAN_S2_ = new SystemMessage(1560);

    public static final SystemMessage S1_CLAN_CANT_MAKE_A_DECLARATION_OF_CLAN_WAR_SINCE_IT_HASNT_REACHED_THE_CLAN_LEVEL_OR_DOESNT_HAVE_ENOUGH_CLAN_MEMBERS = new SystemMessage(1563);

    public static final SystemMessage CLAN_WAR_HAS_BEEN_DECLARED_AGAINST_S1_CLAN_IF_YOU_ARE_KILLED_DURING_THE_CLAN_WAR_BY_MEMBERS_OF_THE_OPPOSING_CLAN_THE_EXPERIENCE_PENALTY_WILL_BE_REDUCED_TO_1_4_OF_NORMAL = new SystemMessage(1562);

    public static final SystemMessage SEED_PRICE_SHOULD_BE_MORE_THAN_S1_AND_LESS_THAN_S2 = new SystemMessage(1557);

    public static final SystemMessage NOTICE_HAS_BEEN_SAVED = new SystemMessage(1556);

    public static final SystemMessage CROP_PRICE_SHOULD_BE_MORE_THAN_S1_AND_LESS_THAN_S2 = new SystemMessage(1559);

    public static final SystemMessage THE_QUANTITY_OF_SEED_SHOULD_BE_MORE_THAN_S1_AND_LESS_THAN_S2 = new SystemMessage(1558);

    public static final SystemMessage CARGO_HAS_ARRIVED_AT_RUNE_VILLAGE = new SystemMessage(1553);

    public static final SystemMessage CARGO_HAS_ARRIVED_AT_HEINE = new SystemMessage(1552);

    public static final SystemMessage DO_YOU_WANT_TO_CANCEL_CHARACTER_DELETION = new SystemMessage(1555);

    public static final SystemMessage CARGO_HAS_ARRIVED_AT_GODDARD_CASTLE_TOWN = new SystemMessage(1554);

    public static final SystemMessage S2_CLAN_MEMBER_S1S_APPRENTICE_HAS_BEEN_REMOVED = new SystemMessage(1763);

    public static final SystemMessage YOU_DO_NOT_HAVE_THE_RIGHT_TO_DISMISS_AN_APPRENTICE = new SystemMessage(1762);

    public static final SystemMessage CLAN_MEMBER_S1S_PRIVILEGE_LEVEL_HAS_BEEN_CHANGED_TO_S2 = new SystemMessage(1761);

    public static final SystemMessage CLAN_MEMBER_S1S_TITLE_HAS_BEEN_CHANGED_TO_S2 = new SystemMessage(1760);

    public static final SystemMessage AN_APPLICATION_TO_JOIN_THE_CLAN_ACADEMY_HAS_BEEN_SENT_TO_S1 = new SystemMessage(1767);

    public static final SystemMessage AN_APPLICATION_TO_JOIN_THE_CLAN_HAS_BEEN_SENT_TO_S1_IN_S2 = new SystemMessage(1766);

    public static final SystemMessage AS_A_GRADUATE_OF_THE_CLAN_ACADEMY_YOU_CAN_NO_LONGER_WEAR_THIS_ITEM = new SystemMessage(1765);

    public static final SystemMessage THIS_ITEM_CAN_ONLY_BE_WORN_BY_A_MEMBER_OF_THE_CLAN_ACADEMY = new SystemMessage(1764);

    public static final SystemMessage NOW_THAT_YOUR_CLAN_LEVEL_IS_ABOVE_LEVEL_5_IT_CAN_ACCUMULATE_CLAN_REPUTATION_POINTS = new SystemMessage(1771);

    public static final SystemMessage THE_CLAN_REPUTATION_SCORE_HAS_DROPPED_BELOW_0_THE_CLAN_MAY_FACE_CERTAIN_PENALTIES_AS_A_RESULT = new SystemMessage(1770);

    public static final SystemMessage C1_HAS_SENT_YOU_AN_INVITATION_TO_JOIN_THE_S3_ORDER_OF_KNIGHTS_UNDER_THE_S2_CLAN_WOULD_YOU_LIKE = new SystemMessage(1769);

    public static final SystemMessage C1_HAS_INVITED_YOU_TO_JOIN_THE_CLAN_ACADEMY_OF_S2_CLAN_WOULD_YOU_LIKE_TO_JOIN = new SystemMessage(1768);

    public static final SystemMessage CLAN_MEMBER_S1_WAS_AN_ACTIVE_MEMBER_OF_THE_HIGHEST_RANKED_PARTY_IN_THE_FESTIVAL_OF_DARKNESS_S2_POINTS_HAVE_BEEN_ADDED_TO_YOUR_CLAN_REPUTATION_SCORE = new SystemMessage(1775);

    public static final SystemMessage YOUR_CLAN_NEWLY_ACQUIRED_CONTESTED_CLAN_HALL_HAS_ADDED_S1_POINTS_TO_YOUR_CLAN_REPUTATION_SCORE = new SystemMessage(1774);

    public static final SystemMessage SINCE_YOUR_CLAN_EMERGED_VICTORIOUS_FROM_THE_SIEGE_S1_POINTS_HAVE_BEEN_ADDED_TO_YOUR_CLAN_REPUTATION_SCORE = new SystemMessage(1773);

    public static final SystemMessage SINCE_YOUR_CLAN_WAS_DEFEATED_IN_A_SIEGE_S1_POINTS_HAVE_BEEN_DEDUCTED_FROM_YOUR_CLAN_REPUTATION_SCORE_AND_GIVEN_TO_THE_OPPOSING_CLAN = new SystemMessage(1772);

    public static final SystemMessage AN_OPPOSING_CLAN_HAS_CAPTURED_YOUR_CLAN_CONTESTED_CLAN_HALL_S1_POINTS_HAVE_BEEN_DEDUCTED_FROM_YOUR_CLAN_REPUTATION_SCORE = new SystemMessage(1778);

    public static final SystemMessage AFTER_LOSING_THE_CONTESTED_CLAN_HALL_300_POINTS_HAVE_BEEN_DEDUCTED_FROM_YOUR_CLAN_REPUTATION_SCORE = new SystemMessage(1779);

    public static final SystemMessage CLAN_MEMBER_S1_WAS_NAMED_A_HERO_2S_POINTS_HAVE_BEEN_ADDED_TO_YOUR_CLAN_REPUTATION_SCORE = new SystemMessage(1776);

    public static final SystemMessage YOU_HAVE_SUCCESSFULLY_COMPLETED_A_CLAN_QUEST_S1_POINTS_HAVE_BEEN_ADDED_TO_YOUR_CLAN_REPUTATION_SCORE = new SystemMessage(1777);

    public static final SystemMessage YOUR_CLAN_MEMBER_S1_WAS_KILLED_S2_POINTS_HAVE_BEEN_DEDUCTED_FROM_YOUR_CLAN_REPUTATION_SCORE_AND_ADDED_TO_YOUR_OPPONENT_CLAN_REPUTATION_SCORE = new SystemMessage(1782);

    public static final SystemMessage FOR_KILLING_AN_OPPOSING_CLAN_MEMBER_S1_POINTS_HAVE_BEEN_DEDUCTED_FROM_YOUR_OPPONENTS_CLAN_REPUTATION_SCORE = new SystemMessage(1783);

    public static final SystemMessage YOUR_CLAN_HAS_CAPTURED_YOUR_OPPONENT_CONTESTED_CLAN_HALL_S1_POINTS_HAVE_BEEN_DEDUCTED_FROM_YOUR_OPPONENT_CLAN_REPUTATION_SCORE = new SystemMessage(1780);

    public static final SystemMessage YOUR_CLAN_HAS_ADDED_1S_POINTS_TO_ITS_CLAN_REPUTATION_SCORE = new SystemMessage(1781);

    public static final SystemMessage YOUR_CLAN_HAS_FAILED_TO_DEFEND_THE_CASTLE_S1_POINTS_HAVE_BEEN_DEDUCTED_FROM_YOUR_CLAN_REPUTATION_SCORE = new SystemMessage(1786);

    public static final SystemMessage S1_POINTS_HAVE_BEEN_DEDUCTED_FROM_THE_CLAN_REPUTATION_SCORE = new SystemMessage(1787);

    public static final SystemMessage YOUR_CLAN_HAS_FAILED_TO_DEFEND_THE_CASTLE_S1_POINTS_HAVE_BEEN_DEDUCTED_FROM_YOUR_CLAN_REPUTATION_SCORE_AND_ADDED_TO_YOUR_OPPONENTS = new SystemMessage(1784);

    public static final SystemMessage THE_CLAN_YOU_BELONG_TO_HAS_BEEN_INITIALIZED_S1_POINTS_HAVE_BEEN_DEDUCTED_FROM_YOUR_CLAN_REPUTATION_SCORE = new SystemMessage(1785);

    public static final SystemMessage THE_CONDITIONS_NECESSARY_TO_INCREASE_THE_CLAN_LEVEL_HAVE_NOT_BEEN_MET = new SystemMessage(1790);

    public static final SystemMessage THE_CONDITIONS_NECESSARY_TO_CREATE_A_MILITARY_UNIT_HAVE_NOT_BEEN_MET = new SystemMessage(1791);

    public static final SystemMessage THE_CLAN_SKILL_S1_HAS_BEEN_ADDED = new SystemMessage(1788);

    public static final SystemMessage SINCE_THE_CLAN_REPUTATION_SCORE_HAS_DROPPED_TO_0_OR_LOWER_YOUR_CLAN_SKILLS_WILL_BE_DE_ACTIVATED = new SystemMessage(1789);

    public static final SystemMessage YOU_CANNOT_JOIN_A_COMMAND_CHANNEL_WHILE_TELEPORTING = new SystemMessage(1729);

    public static final SystemMessage THE_RECIPIENT_OF_YOUR_INVITATION_DID_NOT_ACCEPT_THE_PARTY_MATCHING_INVITATION = new SystemMessage(1728);

    public static final SystemMessage ONLY_THE_CLAN_LEADER_CAN_CREATE_A_CLAN_ACADEMY = new SystemMessage(1731);

    public static final SystemMessage TO_ESTABLISH_A_CLAN_ACADEMY_YOUR_CLAN_MUST_BE_LEVEL_5_OR_HIGHER = new SystemMessage(1730);

    public static final SystemMessage YOU_DO_NOT_HAVE_ENOUGH_ADENA_TO_CREATE_A_CLAN_ACADEMY = new SystemMessage(1733);

    public static final SystemMessage TO_CREATE_A_CLAN_ACADEMY_A_BLOOD_MARK_IS_NEEDED = new SystemMessage(1732);

    public static final SystemMessage S1_DOES_NOT_MEET_THE_REQUIREMENTS_TO_JOIN_A_CLAN_ACADEMY = new SystemMessage(1735);

    public static final SystemMessage TO_JOIN_A_CLAN_ACADEMY_CHARACTERS_MUST_BE_LEVEL_40_OR_BELOW_NOT_BELONG_ANOTHER_CLAN_AND_NOT_YET_COMPLETED_THEIR_2ND_CLASS_TRANSFER = new SystemMessage(1734);

    public static final SystemMessage YOUR_CLAN_HAS_NOT_ESTABLISHED_A_CLAN_ACADEMY_BUT_IS_ELIGIBLE_TO_DO_SO = new SystemMessage(1737);

    public static final SystemMessage THE_CLAN_ACADEMY_HAS_REACHED_ITS_MAXIMUM_ENROLLMENT = new SystemMessage(1736);

    public static final SystemMessage WOULD_YOU_LIKE_TO_CREATE_A_CLAN_ACADEMY = new SystemMessage(1739);

    public static final SystemMessage YOUR_CLAN_HAS_ALREADY_ESTABLISHED_A_CLAN_ACADEMY = new SystemMessage(1738);

    public static final SystemMessage CONGRATULATIONS_THE_S1S_CLAN_ACADEMY_HAS_BEEN_CREATED = new SystemMessage(1741);

    public static final SystemMessage PLEASE_ENTER_THE_NAME_OF_THE_CLAN_ACADEMY = new SystemMessage(1740);

    public static final SystemMessage TO_OPEN_A_CLAN_ACADEMY_THE_LEADER_OF_A_LEVEL_5_CLAN_OR_ABOVE_MUST_PAY_XX_PROOFS_OF_BLOOD_OR_A_CERTAIN_AMOUNT_OF_ADENA = new SystemMessage(1743);

    public static final SystemMessage A_MESSAGE_INVITING_S1_TO_JOIN_THE_CLAN_ACADEMY_IS_BEING_SENT = new SystemMessage(1742);

    public static final SystemMessage THERE_WAS_NO_RESPONSE_TO_YOUR_INVITATION_TO_JOIN_THE_CLAN_ACADEMY_SO_THE_INVITATION_HAS_BEEN_RESCINDED = new SystemMessage(1744);

    public static final SystemMessage THE_RECIPIENT_OF_YOUR_INVITATION_TO_JOIN_THE_CLAN_ACADEMY_HAS_DECLINED = new SystemMessage(1745);

    public static final SystemMessage YOU_HAVE_ALREADY_JOINED_A_CLAN_ACADEMY = new SystemMessage(1746);

    public static final SystemMessage S1_HAS_SENT_YOU_AN_INVITATION_TO_JOIN_THE_CLAN_ACADEMY_BELONGING_TO_THE_S2_CLAN_DO_YOU_ACCEPT = new SystemMessage(1747);

    public static final SystemMessage CLAN_ACADEMY_MEMBER_S1_HAS_SUCCESSFULLY_COMPLETED_THE_2ND_CLASS_TRANSFER_AND_OBTAINED_S2_CLAN_REPUTATION_POINTS = new SystemMessage(1748);

    public static final SystemMessage CONGRATULATIONS_YOU_WILL_NOW_GRADUATE_FROM_THE_CLAN_ACADEMY_AND_LEAVE_YOUR_CURRENT_CLAN_AS_A_GRADUATE_OF_THE_ACADEMY_YOU_CAN_IMMEDIATELY_JOIN_A_CLAN_AS_A_REGULAR_MEMBER_WITHOUT_BEING_SUBJECT_TO_ANY_PENALTIES = new SystemMessage(1749);

    public static final SystemMessage C1_DOES_NOT_MEET_THE_PARTICIPATION_REQUIREMENTS_THE_OWNER_OF_S2_CANNOT_PARTICIPATE_IN_THE = new SystemMessage(1750);

    public static final SystemMessage THE_GRAND_MASTER_HAS_GIVEN_YOU_A_COMMEMORATIVE_ITEM = new SystemMessage(1751);

    public static final SystemMessage SINCE_THE_CLAN_HAS_RECEIVED_A_GRADUATE_OF_THE_CLAN_ACADEMY_IT_HAS_EARNED_S1_POINTS_TOWARD_ITS_REPUTATION_SCORE = new SystemMessage(1752);

    public static final SystemMessage THE_CLAN_LEADER_HAS_DECREED_THAT_THAT_PARTICULAR_PRIVILEGE_CANNOT_BE_GRANTED_TO_A_CLAN_ACADEMY = new SystemMessage(1753);

    public static final SystemMessage THAT_PRIVILEGE_CANNOT_BE_GRANTED_TO_A_CLAN_ACADEMY_MEMBER = new SystemMessage(1754);

    public static final SystemMessage S2_HAS_BEEN_DESIGNATED_AS_THE_APPRENTICE_OF_CLAN_MEMBER_S1 = new SystemMessage(1755);

    public static final SystemMessage S1_YOUR_CLAN_ACADEMYS_APPRENTICE_HAS_LOGGED_IN = new SystemMessage(1756);

    public static final SystemMessage S1_YOUR_CLAN_ACADEMYS_APPRENTICE_HAS_LOGGED_OUT = new SystemMessage(1757);

    public static final SystemMessage S1_YOUR_CLAN_ACADEMYS_SPONSOR_HAS_LOGGED_IN = new SystemMessage(1758);

    public static final SystemMessage S1_YOUR_CLAN_ACADEMYS_SPONSOR_HAS_LOGGED_OUT = new SystemMessage(1759);

    public static final SystemMessage THE_USER_WHO_CONDUCTED_A_SEARCH_A_MOMENT_AGO_HAS_BEEN_CONFIRMED_TO_BE_A_NONBOT_USER = new SystemMessage(1703);

    public static final SystemMessage THE_USER_WHO_CONDUCTED_A_SEARCH_A_MOMENT_AGO_HAS_BEEN_CONFIRMED_TO_BE_A_BOT_USER = new SystemMessage(1702);

    public static final SystemMessage YOU_DONT_HAVE_ENOUGH_SOULSHOTS_NEEDED_FOR_A_PET_SERVITOR = new SystemMessage(1701);

    public static final SystemMessage BELOW_S1_POINTS = new SystemMessage(6036);

    public static final SystemMessage YOU_DONT_HAVE_ENOUGH_SPIRITSHOTS_NEEDED_FOR_A_PET_SERVITOR = new SystemMessage(1700);

    public static final SystemMessage WHEN_THERE_ARE_MANY_PLAYERS_WITH_THE_SAME_SCORE_THEY_APPEAR_IN_THE_ORDER_IN_WHICH_THEY_WERE = new SystemMessage(6035);

    public static final SystemMessage YOU_CANNOT_DISMISS_A_PARTY_MEMBER_BY_FORCE = new SystemMessage(1699);

    public static final SystemMessage S1_HAVE_HAS_BEEN_INITIALIZED = new SystemMessage(6034);

    public static final SystemMessage SINCE_YOUR_CUMULATIVE_ACCESS_TIME_HAS_EXCEEDED_S1_YOU_NO_LONGER_HAVE_EXP_OR_ITEM_DROP_PRIVILEGE__PLEASE_TERMINATE_YOUR_SESSION_AND_TAKE_A_BREAK = new SystemMessage(1698);

    public static final SystemMessage ONLY_THE_TOP_S1_APPEAR_IN_THE_RANKING_AND_ONLY_THE_TOP_S2_ARE_RECORDED_IN_MY_BEST_RANKING = new SystemMessage(6033);

    public static final SystemMessage SINCE_YOUR_CUMULATIVE_ACCESS_TIME_HAS_EXCEEDED_S1_YOUR_EXP_AND_ITEM_DROP_RATE_WERE_REDUCED_BY_HALF_PLEASE_TERMINATE_YOUR_SESSION_AND_TAKE_A_BREAK = new SystemMessage(1697);

    public static final SystemMessage SANTA_IS_OUT_DELIVERING_THE_GIFTS_MERRY_CHRISTMAS = new SystemMessage(6032);

    public static final SystemMessage IF_THE_ACCUMULATED_ONLINE_ACCESS_TIME_IS_S1_OR_MORE_A_PENALTY_WILL_BE_IMPOSED__PLEASE_TERMINATE_YOUR_SESSION_AND_TAKE_A_BREAK = new SystemMessage(1696);

    public static final SystemMessage PC_BANG_POINTS_USE_PERIOD_HAS_EXPIRED = new SystemMessage(1711);

    public static final SystemMessage YOU_ARE_SHORT_OF_ACCUMULATED_POINTS = new SystemMessage(1710);

    public static final SystemMessage YOU_ARE_USING_S1_POINT = new SystemMessage(1709);

    public static final SystemMessage DOUBLE_POINTS_YOU_AQUIRED_S1_PC_BANG_POINT = new SystemMessage(1708);

    public static final SystemMessage YOU_ACQUIRED_S1_PC_BANG_POINT = new SystemMessage(1707);

    public static final SystemMessage PC_BANG_POINTS_USE_PERIOD_POINTS_USE_PERIOD_LEFT_S1_HOUR = new SystemMessage(1706);

    public static final SystemMessage PC_BANG_POINTS_ACQUISITION_PERIOD_PONTS_ACQUISITION_PERIOD_LEFT_S1_HOUR = new SystemMessage(1705);

    public static final SystemMessage PLEASE_CLOSE_THE_SETUP_WINDOW_FOR_A_PRIVATE_MANUFACTURING_STORE_OR_THE_SETUP_WINDOW_FOR_A_PRIVATE_STORE_AND_TRY_AGAIN = new SystemMessage(1704);

    public static final SystemMessage S1_RECEIVED_S4_S3_AS_REWARD_FOR_S2_CONSECUTIVE_WINS = new SystemMessage(6022);

    public static final SystemMessage GENERAL_FIELD = new SystemMessage(1718);

    public static final SystemMessage WORLD__S1_CONSECUTIVE_WINS_S2_PPL = new SystemMessage(6023);

    public static final SystemMessage SEVEN_SIGNS_ZONE___N__ALTHOUGH_A_CHARACTER_S_LEVEL_MAY_INCREASE_WHILE_IN_THIS_AREA_HP_AND_MP___N = new SystemMessage(1719);

    public static final SystemMessage YOU_CANNOT_BUY_THE_ITEM_AT_THIS_HOUR = new SystemMessage(6020);

    public static final SystemMessage ALTERED_ZONE = new SystemMessage(1716);

    public static final SystemMessage S1_REACHED_S2_CONSECUTIVE_WINS_IN_JACK_GAME = new SystemMessage(6021);

    public static final SystemMessage SIEGE_WAR_ZONE___N__A_SIEGE_IS_CURRENTLY_IN_PROGRESS_IN_THIS_AREA____N_IF_A_CHARACTER_DIES_IN = new SystemMessage(1717);

    public static final SystemMessage EVA_S_BLESSING_STAGE_S1_HAS_ENDED = new SystemMessage(6018);

    public static final SystemMessage CURRENT_LOCATION__S1_S2_S3_NEAR_THE_TOWN_OF_SCHUTTGART = new SystemMessage(1714);

    public static final SystemMessage YOU_CANNOT_BUY_THE_ITEM_ON_THIS_DAY_OF_THE_WEEK = new SystemMessage(6019);

    public static final SystemMessage THIS_IS_A_PEACEFUL_ZONE__N__PVP_IS_NOT_ALLOWED_IN_THIS_AREA = new SystemMessage(1715);

    public static final SystemMessage PROGRESS__EVENT_STAGE_S1 = new SystemMessage(6016);

    public static final SystemMessage THE_PC_BANG_POINTS_ACCUMULATION_PERIOD_HAS_EXPIRED = new SystemMessage(1712);

    public static final SystemMessage EVA_S_BLESSING_STAGE_S1_HAS_BEGUN = new SystemMessage(6017);

    public static final SystemMessage THE_MATCH_MAY_BE_DELAYED_DUE_TO_NOT_ENOUGH_COMBATANTS = new SystemMessage(1713);

    public static final SystemMessage SANTA_HAS_STARTED_DELIVERING_THE_CHRISTMAS_GIFTS_TO_ADEN = new SystemMessage(6030);

    public static final SystemMessage S1_HAS_DISAPPEARED_BECAUSE_ITS_TIME_PERIOD_HAS_EXPIRED = new SystemMessage(1726);

    public static final SystemMessage SANTA_HAS_COMPLETED_THE_DELIVERIES_SEE_YOU_IN_1_HOUR = new SystemMessage(6031);

    public static final SystemMessage C1_HAS_INVITED_YOU_TO_A_PARTY_ROOM_DO_YOU_ACCEPT = new SystemMessage(1727);

    public static final SystemMessage NO_RECORD_OVER_10_CONSECUTIVE_WINS = new SystemMessage(6028);

    public static final SystemMessage A_SERVITOR_WHOM_IS_ENGAGED_IN_BATTLE_CANNOT_BE_DE_ACTIVATED = new SystemMessage(1724);

    public static final SystemMessage PLEASE_HELP_RAISE_REINDEER_FOR_SANTA_S_CHRISTMAS_DELIVERY = new SystemMessage(6029);

    public static final SystemMessage YOU_HAVE_EARNED_S1_RAID_POINTS = new SystemMessage(1725);

    public static final SystemMessage MY_RECORD__UNDER_4_CONSECUTIVE_WINS = new SystemMessage(6026);

    public static final SystemMessage PLEASE_ENTER_THE_NAME_OF_THE_ITEM_YOU_WISH_TO_SEARCH_FOR = new SystemMessage(1722);

    public static final SystemMessage IT_S_HALLOWEEN_EVENT_PERIOD = new SystemMessage(6027);

    public static final SystemMessage PLEASE_TAKE_A_MOMENT_TO_PROVIDE_FEEDBACK_ABOUT_THE_PETITION_SERVICE = new SystemMessage(1723);

    public static final SystemMessage MY_RECORD__S1_CONSECUTIVE_WINS = new SystemMessage(6024);

    public static final SystemMessage ___ = new SystemMessage(1720);

    public static final SystemMessage WORLD__UNDER_4_CONSECUTIVE_WINS = new SystemMessage(6025);

    public static final SystemMessage COMBAT_ZONE = new SystemMessage(1721);

    public static final SystemMessage THERE_WAS_NOTHING_FOUND_INSIDE_OF_THAT = new SystemMessage(1669);

    public static final SystemMessage YOUR_LETHAL_STRIKE_WAS_SUCCESSFUL = new SystemMessage(1668);

    public static final SystemMessage YOUR_REELING_WAS_SUCCESSFUL_MASTERY_PENALTY_S1 = new SystemMessage(1671);

    public static final SystemMessage SINCE_THE_SKILL_LEVEL_OF_REELING_PUMPING_IS_HIGHER_THAN_THE_LEVEL_OF_YOUR_FISHING_MASTERY_A_PENALTY_OF_S1_WILL_BE_APPLIED = new SystemMessage(1670);

    public static final SystemMessage SINCE_THE_FISH_IS_EXHAUSTED_THE_FLOAT_IS_MOVING_ONLY_SLIGHTLY = new SystemMessage(1665);

    public static final SystemMessage BECAUSE_THE_FISH_IS_RESISTING_THE_FLOAT_IS_BOBBING_UP_AND_DOWN_A_LOT = new SystemMessage(1664);

    public static final SystemMessage LETHAL_STRIKE = new SystemMessage(1667);

    public static final SystemMessage YOU_HAVE_OBTAINED__S1_S2 = new SystemMessage(1666);

    public static final SystemMessage A_CEASE_FIRE_DURING_A_CLAN_WAR_CAN_NOT_BE_CALLED_WHILE_MEMBERS_OF_YOUR_CLAN_ARE_ENGAGED_IN_BATTLE = new SystemMessage(1677);

    public static final SystemMessage SINCE_A_SERVITOR_OR_A_PET_DOES_NOT_EXIST_AUTOMATIC_USE_IS_NOT_APPLICABLE = new SystemMessage(1676);

    public static final SystemMessage ONLY_THE_CREATOR_OF_A_CHANNEL_CAN_ISSUE_A_GLOBAL_COMMAND = new SystemMessage(1679);

    public static final SystemMessage YOU_HAVE_NOT_DECLARED_A_CLAN_WAR_TO_S1_CLAN = new SystemMessage(1678);

    public static final SystemMessage THE_CURRENT_FOR_THIS_OLYMPIAD_IS_S1_WINS_S2_DEFEATS_S3_YOU_HAVE_EARNED_S4_OLYMPIAD_POINTS = new SystemMessage(1673);

    public static final SystemMessage YOUR_PUMPING_WAS_SUCCESSFUL_MASTERY_PENALTY_S1 = new SystemMessage(1672);

    public static final SystemMessage A_MANOR_CANNOT_BE_SET_UP_BETWEEN_6_AM_AND_8_PM = new SystemMessage(1675);

    public static final SystemMessage THIS_COMMAND_CAN_ONLY_BE_USED_BY_A_NOBLESSE = new SystemMessage(1674);

    public static final SystemMessage WHILE_A_CLAN_IS_BEING_DISSOLVED_IT_IS_IMPOSSIBLE_TO_DECLARE_A_CLAN_WAR_AGAINST_IT = new SystemMessage(1684);

    public static final SystemMessage IF_YOUR_PK_COUNT_IS_1_OR_MORE_YOU_ARE_NOT_ALLOWED_TO_WEAR_THIS_ITEM = new SystemMessage(1685);

    public static final SystemMessage THE_CASTLE_WALL_HAS_SUSTAINED_DAMAGE = new SystemMessage(1686);

    public static final SystemMessage THIS_AREA_CANNOT_BE_ENTERED_WHILE_MOUNTED_ATOP_OF_A_WYVERN_YOU_WILL_BE_DISMOUNTED_FROM_YOUR_WYVERN_IF_YOU_DO_NOT_LEAVE = new SystemMessage(1687);

    public static final SystemMessage S1_HAS_DECLINED_THE_CHANNEL_INVITATION = new SystemMessage(1680);

    public static final SystemMessage SINCE_S1_DID_NOT_RESPOND_YOUR_CHANNEL_INVITATION_HAS_FAILED = new SystemMessage(1681);

    public static final SystemMessage ONLY_THE_CREATOR_OF_A_CHANNEL_CAN_USE_THE_CHANNEL_DISMISS_COMMAND = new SystemMessage(1682);

    public static final SystemMessage ONLY_A_PARTY_LEADER_CAN_CHOOSE_THE_OPTION_TO_LEAVE_A_CHANNEL = new SystemMessage(1683);

    public static final SystemMessage SINCE_YOU_HAVE_CHANGED_YOUR_CLASS_INTO_A_SUB_JOB_YOU_CANNOT_PARTICIPATE_IN_THE_OLYMPIAD = new SystemMessage(1692);

    public static final SystemMessage WHILE_YOU_ARE_ON_THE_WAITING_LIST_YOU_ARE_NOT_ALLOWED_TO_WATCH_THE_GAME = new SystemMessage(1693);

    public static final SystemMessage ONLY_A_CLAN_LEADER_THAT_IS_A_NOBLESSE_CAN_VIEW_THE_SIEGE_WAR_STATUS_WINDOW_DURING_A_SIEGE_WAR = new SystemMessage(1694);

    public static final SystemMessage IT_CAN_BE_USED_ONLY_WHILE_A_SIEGE_WAR_IS_TAKING_PLACE = new SystemMessage(1695);

    public static final SystemMessage YOU_CANNOT_PRACTICE_ENCHANTING_WHILE_OPERATING_A_PRIVATE_STORE_OR_PRIVATE_MANUFACTURING_WORKSHOP = new SystemMessage(1688);

    public static final SystemMessage YOU_ARE_ALREADY_ON_THE_WAITING_LIST_TO_PARTICIPATE_IN_THE_GAME_FOR_YOUR_CLASS = new SystemMessage(1689);

    public static final SystemMessage YOU_ARE_ALREADY_ON_THE_WAITING_LIST_FOR_ALL_CLASSES_WAITING_TO_PARTICIPATE_IN_THE_GAME = new SystemMessage(1690);

    public static final SystemMessage SINCE_80_PERCENT_OR_MORE_OF_YOUR_INVENTORY_SLOTS_ARE_FULL_YOU_CANNOT_PARTICIPATE_IN_THE_OLYMPIAD = new SystemMessage(1691);

    public static final SystemMessage THE_REMAINING_RECYCLE_TIME_FOR_S1_IS_S2_MINUTES = new SystemMessage(1913);

    public static final SystemMessage ONLY_THOSE_REQUESTING_NCOTP_SHOULD_MAKE_AN_ENTRY_INTO_THIS_FIELD = new SystemMessage(1912);

    public static final SystemMessage THE_GAME_WILL_END_IN_S1_SECONDS_2 = new SystemMessage(1915);

    public static final SystemMessage THE_REMAINING_RECYCLE_TIME_FOR_S1_IS_S2_SECONDS = new SystemMessage(1914);

    public static final SystemMessage THE_DEATH_PENALTY_HAS_BEEN_LIFTED = new SystemMessage(1917);

    public static final SystemMessage THE_LEVEL_S1_DEATH_PENALTY_WILL_BE_ASSESSED = new SystemMessage(1916);

    public static final SystemMessage THE_GRAND_OLYMPIAD_REGISTRATION_PERIOD_HAS_ENDED = new SystemMessage(1919);

    public static final SystemMessage THE_PET_IS_TOO_HIGH_LEVEL_TO_CONTROL = new SystemMessage(1918);

    public static final SystemMessage S2_OF_S1_WILL_BE_REPLACED_WITH_S4_OF_S3 = new SystemMessage(1905);

    public static final SystemMessage A_SUB_CLASS_MAY_NOT_BE_CREATED_OR_CHANGED_WHILE_A_SERVITOR_OR_PET_IS_SUMMONED = new SystemMessage(1904);

    public static final SystemMessage SELECT_THE_THE_CHARACTER_WHO_WILL_REPLACE_THE_CURRENT_CHARACTER = new SystemMessage(1907);

    public static final SystemMessage SELECT_THE_COMBAT_UNIT_YOU_WISH_TO_TRANSFER_TO = new SystemMessage(1906);

    public static final SystemMessage LIST_OF_CLAN_ACADEMY_GRADUATES_DURING_THE_PAST_WEEK = new SystemMessage(1909);

    public static final SystemMessage S1_IS_IN_A_STATE_WHICH_PREVENTS_SUMMONING = new SystemMessage(1908);

    public static final SystemMessage YOU_CANNOT_SUMMON_PLAYERS_WHO_ARE_CURRENTLY_PARTICIPATING_IN_THE_GRAND_OLYMPIAD = new SystemMessage(1911);

    public static final SystemMessage GRADUATES = new SystemMessage(1910);

    public static final SystemMessage S1_HAS_ALREADY_BEEN_SUMMONED = new SystemMessage(1896);

    public static final SystemMessage S1_IS_REQUIRED_FOR_SUMMONING = new SystemMessage(1897);

    public static final SystemMessage S1_IS_CURRENTLY_TRADING_OR_OPERATING_A_PRIVATE_STORE_AND_CANNOT_BE_SUMMONED = new SystemMessage(1898);

    public static final SystemMessage YOUR_TARGET_IS_IN_AN_AREA_WHICH_BLOCKS_SUMMONING = new SystemMessage(1899);

    public static final SystemMessage S1_HAS_ENTERED_THE_PARTY_ROOM = new SystemMessage(1900);

    public static final SystemMessage S1_HAS_INVITED_YOU_TO_ENTER_THE_PARTY_ROOM = new SystemMessage(1901);

    public static final SystemMessage INCOMPATIBLE_ITEM_GRADE_THIS_ITEM_CANNOT_BE_USED = new SystemMessage(1902);

    public static final SystemMessage REQUESTED_NCOTP = new SystemMessage(1903);

    public static final SystemMessage INVALID_SERIAL_NUMBER_YOUR_ATTEMPT_TO_ENTER_THE_NUMBER_HAS_FAILED_5_TIMES_PLEASE_TRY_AGAIN_IN_4_HOURS = new SystemMessage(1888);

    public static final SystemMessage CONGRATULATIONS_YOU_HAVE_RECEIVED_S1 = new SystemMessage(1889);

    public static final SystemMessage SINCE_YOU_HAVE_ALREADY_USED_THIS_COUPON_YOU_MAY_NOT_USE_THIS_SERIAL_NUMBER = new SystemMessage(1890);

    public static final SystemMessage YOU_MAY_NOT_USE_ITEMS_IN_A_PRIVATE_STORE_OR_PRIVATE_WORK_SHOP = new SystemMessage(1891);

    public static final SystemMessage THE_REPLAY_FILE_FOR_THE_PREVIOUS_VERSION_CANNOT_BE_PLAYED = new SystemMessage(1892);

    public static final SystemMessage THIS_FILE_CANNOT_BE_REPLAYED = new SystemMessage(1893);

    public static final SystemMessage A_SUB_CLASS_CANNOT_BE_CREATED_OR_CHANGED_WHILE_YOU_ARE_OVER_YOUR_WEIGHT_LIMIT = new SystemMessage(1894);

    public static final SystemMessage S1_IS_IN_AN_AREA_WHICH_BLOCKS_SUMMONING = new SystemMessage(1895);

    public static final SystemMessage THERE_ARE_NO_OFFERINGS_I_OWN_OR_I_MADE_A_BID_FOR = new SystemMessage(1883);

    public static final SystemMessage CHARACTERS_CANNOT_BE_CREATED_FROM_THIS_SERVER = new SystemMessage(1882);

    public static final SystemMessage THE_PRELIMINARY_MATCH_WILL_BEGIN_IN_S1_SECONDS_PREPARE_YOURSELF = new SystemMessage(1881);

    public static final SystemMessage IN_S1_SECONDS_YOU_WILL_BE_TELEPORTED_OUTSIDE_OF_THE_GAME_ARENA = new SystemMessage(1880);

    public static final SystemMessage INVALID_SERIAL_NUMBER_YOUR_ATTEMPT_TO_ENTER_THE_NUMBER_HAS_FAILED_S1_TIMES_YOU_WILL_BE_ALLOWED_TO_MAKE_S2_MORE_ATTEMPTS = new SystemMessage(1887);

    public static final SystemMessage THIS_SERIAL_NUMBER_HAS_ALREADY_BEEN_USED = new SystemMessage(1886);

    public static final SystemMessage THIS_SERIAL_NUMBER_CANNOT_BE_ENTERED_PLEASE_TRY_AGAIN_IN_S1_MINUTES = new SystemMessage(1885);

    public static final SystemMessage ENTER_THE_PC_ROOM_COUPON_SERIAL_NUMBER = new SystemMessage(1884);

    public static final SystemMessage THERE_ARE_S1_MINUTES_LEFT_IN_THE_FIXED_USE_TIME_FOR_THIS_PC_CARD = new SystemMessage(1875);

    public static final SystemMessage THERE_ARE_S1_MINUTES_LEFT_FOR_THIS_INDIVIDUAL_USER = new SystemMessage(1874);

    public static final SystemMessage THERE_ARE_S1_HOURS_AND_S2_MINUTES_LEFT_IN_THE_FIXED_USE_TIME_FOR_THIS_PC_CARD = new SystemMessage(1873);

    public static final SystemMessage YOU_HAVE_S1_HOURS_AND_S2_MINUTES_LEFT = new SystemMessage(1872);

    public static final SystemMessage IN_S1_MINUTES_YOU_WILL_BE_TELEPORTED_OUTSIDE_OF_THE_GAME_ARENA = new SystemMessage(1879);

    public static final SystemMessage THE_GAME_WILL_END_IN_S1_SECONDS = new SystemMessage(1878);

    public static final SystemMessage THE_GAME_WILL_END_IN_S1_MINUTES = new SystemMessage(1877);

    public static final SystemMessage DO_YOU_WANT_TO_LEAVE_S1_CLAN = new SystemMessage(1876);

    public static final SystemMessage MP_WAS_REDUCED_BY_S1 = new SystemMessage(1866);

    public static final SystemMessage YOUR_OPPONENTS_MP_WAS_REDUCED_BY_S1 = new SystemMessage(1867);

    public static final SystemMessage THE_PET_SERVITOR_IS_UNRESPONSIVE_AND_WILL_NOT_OBEY_ANY_ORDERS = new SystemMessage(1864);

    public static final SystemMessage THE_PET_SERVITOR_IS_CURRENTLY_IN_A_STATE_OF_DISTRESS = new SystemMessage(1865);

    public static final SystemMessage A_COMMAND_CHANNEL_WITH_THE_ITEM_LOOTING_PRIVILEGE_ALREADY_EXISTS = new SystemMessage(1870);

    public static final SystemMessage DO_YOU_WANT_TO_DISMISS_S1_FROM_THE_CLAN = new SystemMessage(1871);

    public static final SystemMessage YOU_CANNOT_EXCHANGE_AN_ITEM_WHILE_IT_IS_BEING_USED = new SystemMessage(1868);

    public static final SystemMessage S1_HAS_GRANTED_THE_CHANNELS_MASTER_PARTY_THE_PRIVILEGE_OF_ITEM_LOOTING = new SystemMessage(1869);

    public static final SystemMessage YOU_CANNOT_PARTICIPATE_IN_THE_OLYMPIAD_WHILE_DEAD = new SystemMessage(1858);

    public static final SystemMessage YOU_HAVE_EXCEEDED_THE_QUANTITY_THAT_CAN_BE_MOVED_AT_ONE_TIME = new SystemMessage(1859);

    public static final SystemMessage SINCE_YOUR_OPPONENT_IS_NOW_THE_OWNER_OF_S1_THE_OLYMPIAD_HAS_BEEN_CANCELLED = new SystemMessage(1856);

    public static final SystemMessage SINCE_YOU_NOW_OWN_S1_YOU_CANNOT_PARTICIPATE_IN_THE_OLYMPIAD = new SystemMessage(1857);

    public static final SystemMessage THE_CLAN_SKILL_WILL_BE_ACTIVATED_BECAUSE_THE_CLANS_REPUTATION_SCORE_HAS_REACHED_TO_0_OR_HIGHER = new SystemMessage(1862);

    public static final SystemMessage S1_PURCHASED_A_CLAN_ITEM_REDUCING_THE_CLAN_REPUTATION_BY_S2_POINTS = new SystemMessage(1863);

    public static final SystemMessage THE_CLAN_REPUTATION_SCORE_IS_TOO_LOW = new SystemMessage(1860);

    public static final SystemMessage THE_CLANS_CREST_HAS_BEEN_DELETED = new SystemMessage(1861);

    public static final SystemMessage QUANTITY_ITEMS_OF_THE_SAME_TYPE_CANNOT_BE_EXCHANGED_AT_THE_SAME_TIME = new SystemMessage(1853);

    public static final SystemMessage THE_ATTEMPT_TO_ACQUIRE_THE_SKILL_HAS_FAILED_BECAUSE_OF_AN_INSUFFICIENT_CLAN_REPUTATION_SCORE = new SystemMessage(1852);

    public static final SystemMessage ANOTHER_MILITARY_UNIT_IS_ALREADY_USING_THAT_NAME_PLEASE_ENTER_A_DIFFERENT_NAME = new SystemMessage(1855);

    public static final SystemMessage THE_ITEM_WAS_CONVERTED_SUCCESSFULLY = new SystemMessage(1854);

    public static final SystemMessage ALL_OF_S1_WILL_BE_DISCARDED_WOULD_YOU_LIKE_TO_CONTINUE = new SystemMessage(1849);

    public static final SystemMessage BECAUSE_OF_THE_SIZE_OF_FISH_CAUGHT_YOU_WILL_BE_REGISTERED_IN_THE_RANKING = new SystemMessage(1848);

    public static final SystemMessage THE_CAPTAIN_OF_THE_ROYAL_GUARD_CANNOT_BE_APPOINTED = new SystemMessage(1851);

    public static final SystemMessage THE_CAPTAIN_OF_THE_ORDER_OF_KNIGHTS_CANNOT_BE_APPOINTED = new SystemMessage(1850);

    public static final SystemMessage HERO_WEAPONS_CANNOT_BE_DESTROYED = new SystemMessage(1845);

    public static final SystemMessage S1_IS_DEAD_AT_THE_MOMENT_AND_CANNOT_BE_SUMMONED = new SystemMessage(1844);

    public static final SystemMessage YOU_CAUGHT_A_FISH_S1_IN_LENGTH = new SystemMessage(1847);

    public static final SystemMessage YOU_ARE_TOO_FAR_AWAY_FROM_THE_FENRIR_TO_MOUNT_IT = new SystemMessage(1846);

    public static final SystemMessage THIS_CLAN_HALL_WAR_HAS_BEEN_CANCELLED__NOT_ENOUGH_CLANS_HAVE_REGISTERED = new SystemMessage(1841);

    public static final SystemMessage _3_IF_YOU_ENTER_THE_INCORRECT_SERIAL_NUMBER_MORE_THAN_5_TIMES__N___YOU_MAY_USE_IT_AGAIN_AFTER_A = new SystemMessage(1840);

    public static final SystemMessage S1_IS_ENGAGED_IN_COMBAT_AND_CANNOT_BE_SUMMONED = new SystemMessage(1843);

    public static final SystemMessage S1_WISHES_TO_SUMMON_YOU_FROM_S2_DO_YOU_ACCEPT = new SystemMessage(1842);

    public static final SystemMessage YOU_CANNOT_JOIN_A_CLAN_ACADEMY_BECAUSE_YOU_HAVE_SUCCESSFULLY_COMPLETED_YOUR_2ND_CLASS_TRANSFER = new SystemMessage(1836);

    public static final SystemMessage C1_HAS_SENT_YOU_AN_INVITATION_TO_JOIN_THE_S3_ROYAL_GUARD_UNDER_THE_S2_CLAN_WOULD_YOU_LIKE_TO = new SystemMessage(1837);

    public static final SystemMessage _1_THE_COUPON_CAN_BE_USED_ONCE_PER_CHARACTER = new SystemMessage(1838);

    public static final SystemMessage _2_A_USED_SERIAL_NUMBER_MAY_NOT_BE_USED_AGAIN = new SystemMessage(1839);

    public static final SystemMessage ONLY_A_ROOM_LEADER_MAY_INVITE_OTHERS_TO_A_PARTY_ROOM = new SystemMessage(1832);

    public static final SystemMessage ALL_OF_S1_WILL_BE_DROPPED_WOULD_YOU_LIKE_TO_CONTINUE = new SystemMessage(1833);

    public static final SystemMessage THE_PARTY_ROOM_IS_FULL_NO_MORE_CHARACTERS_CAN_BE_INVITED_IN = new SystemMessage(1834);

    public static final SystemMessage S1_IS_FULL_AND_CANNOT_ACCEPT_ADDITIONAL_CLAN_MEMBERS_AT_THIS_TIME = new SystemMessage(1835);

    public static final SystemMessage IN_S1_SECONDS_THE_GAME_WILL_BEGIN = new SystemMessage(1828);

    public static final SystemMessage THE_COMMAND_CHANNEL_IS_FULL = new SystemMessage(1829);

    public static final SystemMessage C1_IS_NOT_ALLOWED_TO_USE_THE_PARTY_ROOM_INVITE_COMMAND_PLEASE_UPDATE_THE_WAITING_LIST = new SystemMessage(1830);

    public static final SystemMessage C1_DOES_NOT_MEET_THE_CONDITIONS_OF_THE_PARTY_ROOM_PLEASE_UPDATE_THE_WAITING_LIST = new SystemMessage(1831);

    public static final SystemMessage YOU_HAVE_BEEN_REGISTERED_FOR_A_CLAN_HALL_WAR__PLEASE_MOVE_TO_THE_LEFT_SIDE_OF_THE_CLAN_HALL_S = new SystemMessage(1824);

    public static final SystemMessage YOU_HAVE_FAILED_IN_YOUR_ATTEMPT_TO_REGISTER_FOR_THE_CLAN_HALL_WAR_PLEASE_TRY_AGAIN = new SystemMessage(1825);

    public static final SystemMessage IN_S1_MINUTES_THE_GAME_WILL_BEGIN_ALL_PLAYERS_MUST_HURRY_AND_MOVE_TO_THE_LEFT_SIDE_OF_THE_CLAN = new SystemMessage(1826);

    public static final SystemMessage IN_S1_MINUTES_THE_GAME_WILL_BEGIN_ALL_PLAYERS_PLEASE_ENTER_THE_ARENA_NOW = new SystemMessage(1827);

    public static final SystemMessage THE_REGISTRATION_PERIOD_FOR_A_CLAN_HALL_WAR_HAS_ENDED = new SystemMessage(1823);

    public static final SystemMessage S1_HAS_BEEN_SEALED = new SystemMessage(1822);

    public static final SystemMessage S2_S_EVIL_PRESENCE_IS_FELT_IN_S1 = new SystemMessage(1821);

    public static final SystemMessage S1_IS_CURRENTLY_ASLEEP = new SystemMessage(1820);

    public static final SystemMessage AN_EVIL_IS_PULSATING_FROM_S2_IN_S1 = new SystemMessage(1819);

    public static final SystemMessage S1_HAS_DISAPPEARED_CW = new SystemMessage(1818);

    public static final SystemMessage S2_OWNER_HAS_LOGGED_INTO_THE_S1_REGION = new SystemMessage(1817);

    public static final SystemMessage THE_OWNER_OF_S2_HAS_APPEARED_IN_THE_S1_REGION = new SystemMessage(1816);

    public static final SystemMessage S2_WAS_DROPPED_IN_THE_S1_REGION = new SystemMessage(1815);

    public static final SystemMessage S2_MINUTE_OF_USAGE_TIME_ARE_LEFT_FOR_S1 = new SystemMessage(1814);

    public static final SystemMessage THERE_IS_S1_HOUR_AND_S2_MINUTE_LEFT_OF_THE_FIXED_USAGE_TIME = new SystemMessage(1813);

    public static final SystemMessage SINCE_THE_REFUSE_INVITATION_STATE_IS_CURRENTLY_ACTIVATED_NO_INVITATION_CAN_BE_MADE = new SystemMessage(1812);

    public static final SystemMessage THE_REFUSE_INVITATION_STATE_HAS_BEEN_REMOVED = new SystemMessage(1811);

    public static final SystemMessage THE_REFUSE_INVITATION_STATE_HAS_BEEN_ACTIVATED = new SystemMessage(1810);

    public static final SystemMessage ACCOUNT_OWNER_MUST_BE_VERIFIED_IN_ORDER_TO_USE_THIS_ACCOUNT_AGAIN = new SystemMessage(1809);

    public static final SystemMessage THIS_ACCOUNT_HAS_BEEN_PERMANENTLY_BANNED_2 = new SystemMessage(1808);

    public static final SystemMessage THIS_ACCOUNT_HAS_BEEN_PERMANENTLY_BANNED_1 = new SystemMessage(1806);

    public static final SystemMessage THIS_ACCOUNT_HAS_BEEN_SUSPENDED_FOR_30_DAYS_2 = new SystemMessage(1807);

    public static final SystemMessage THIS_ACCOUNT_HAS_BEEN_SUSPENDED_FOR_7_DAYS = new SystemMessage(1804);

    public static final SystemMessage THIS_ACCOUNT_HAS_BEEN_SUSPENDED_FOR_30_DAYS_1 = new SystemMessage(1805);

    public static final SystemMessage THE_ATTEMPT_TO_TRADE_HAS_FAILED = new SystemMessage(1802);

    public static final SystemMessage YOU_CANNOT_REGISTER_FOR_A_MATCH = new SystemMessage(1803);

    public static final SystemMessage THE_USER_NAME_S1_HAS_A_HISTORY_OF_USING_THIRD_PARTY_PROGRAMS = new SystemMessage(1800);

    public static final SystemMessage THE_ATTEMPT_TO_SELL_HAS_FAILED = new SystemMessage(1801);

    public static final SystemMessage CLAN_LORD_PRIVILEGES_HAVE_BEEN_TRANSFERRED_TO_C1 = new SystemMessage(1798);

    public static final SystemMessage CURRENTLY_UNDER_INVESTIGATION_PLEASE_WAIT = new SystemMessage(1799);

    public static final SystemMessage FOR_KOREA_ONLY = new SystemMessage(1796);

    public static final SystemMessage C1_HAS_BEEN_PROMOTED_TO_S2 = new SystemMessage(1797);

    public static final SystemMessage THE_KNIGHTS_OF_S1_HAVE_BEEN_CREATED = new SystemMessage(1794);

    public static final SystemMessage THE_ROYAL_GUARD_OF_S1_HAVE_BEEN_CREATED = new SystemMessage(1795);

    public static final SystemMessage PLEASE_ASSIGN_A_MANAGER_FOR_YOUR_NEW_ORDER_OF_KNIGHTS = new SystemMessage(1792);

    public static final SystemMessage S1_HAS_BEEN_SELECTED_AS_THE_CAPTAIN_OF_S2 = new SystemMessage(1793);

    public static final SystemMessage IT_IS_NOT_THE_RIGHT_TIME_FOR_PURCHASING_THE_ITEM = new SystemMessage(2032);

    public static final SystemMessage A_SUB_CLASS_CANNOT_BE_CREATED_OR_CHANGED_BECAUSE_YOU_HAVE_EXCEEDED_YOUR_INVENTORY_LIMIT = new SystemMessage(2033);

    public static final SystemMessage THERE_ARE_S1_HOURSS_AND_S2_MINUTES_REMAINING_UNTIL_THE_TIME_WHEN_THE_ITEM_CAN_BE_PURCHASED = new SystemMessage(2034);

    public static final SystemMessage THERE_ARE_S1_MINUTES_REMAINING_UNTIL_THE_TIME_WHEN_THE_ITEM_CAN_BE_PURCHASED = new SystemMessage(2035);

    public static final SystemMessage UNABLE_TO_INVITE_BECAUSE_THE_PARTY_IS_LOCKED = new SystemMessage(2036);

    public static final SystemMessage UNABLE_TO_CREATE_CHARACTER_YOU_ARE_UNABLE_TO_CREATE_A_NEW_CHARACTER_ON_THE_SELECTED_SERVER_A = new SystemMessage(2037);

    public static final SystemMessage SOME_LINEAGE_II_FEATURES_HAVE_BEEN_LIMITED_FOR_FREE_TRIALS_TRIAL_ACCOUNTS_ARENT_ALLOWED_TO_DROP = new SystemMessage(2038);

    public static final SystemMessage THIS_ACCOUNT_CANOT_TRADE_ITEMS = new SystemMessage(2039);

    public static final SystemMessage CANNOT_TRADE_ITEMS_WITH_THE_TARGETED_USER = new SystemMessage(2040);

    public static final SystemMessage SOME_LINEAGE_II_FEATURES_HAVE_BEEN_LIMITED_FOR_FREE_TRIALS_TRIAL_ACCOUNTS_ARENT_ALLOWED_TO = new SystemMessage(2041);

    public static final SystemMessage THIS_ACCOUNT_HAS_BEEN_SUSPENDED_FOR_NON_PAYMENT_BASED_ON_THE_CELL_PHONE_PAYMENT_AGREEMENT__N = new SystemMessage(2042);

    public static final SystemMessage YOU_HAVE_EXCEEDED_YOUR_INVENTORY_VOLUME_LIMIT_AND_MAY_NOT_TAKE_THIS_QUEST_ITEM_PLEASE_MAKE_ROOM = new SystemMessage(2043);

    public static final SystemMessage SOME_LINEAGE_II_FEATURES_HAVE_BEEN_LIMITED_FOR_FREE_TRIALS_TRIAL_ACCOUNTS_ARENT_ALLOWED_TO_SET = new SystemMessage(2044);

    public static final SystemMessage SOME_LINEAGE_II_FEATURES_HAVE_BEEN_LIMITED_FOR_FREE_TRIALS_TRIAL_ACCOUNTS_ARENT_ALLOWED_TO_USE = new SystemMessage(2045);

    public static final SystemMessage THIS_ACCOUNT_CANOT_USE_PRIVATE_STORES = new SystemMessage(2046);

    public static final SystemMessage SOME_LINEAGE_II_FEATURES_HAVE_BEEN_LIMITED_FOR_FREE_TRIALS_TRIAL_ACCOUNTS_ARENT_ALLOWED_TO_ = new SystemMessage(2047);

    public static final SystemMessage S1_CANNOT_DUEL_BECAUSE_S1_IS_CURRENTLY_ENGAGED_IN_A_PRIVATE_STORE_OR_MANUFACTURE = new SystemMessage(2017);

    public static final SystemMessage A_SKILL_IS_READY_TO_BE_USED_AGAIN_BUT_ITS_RE_USE_COUNTER_TIME_HAS_INCREASED = new SystemMessage(2016);

    public static final SystemMessage S1_CANNOT_DUEL_BECAUSE_S1S_HP_OR_MP_IS_BELOW_50_PERCENT = new SystemMessage(2019);

    public static final SystemMessage S1_CANNOT_DUEL_BECAUSE_S1_IS_CURRENTLY_FISHING = new SystemMessage(2018);

    public static final SystemMessage S1_CANNOT_DUEL_BECAUSE_S1_IS_CURRENTLY_ENGAGED_IN_BATTLE = new SystemMessage(2021);

    public static final SystemMessage S1_CANNOT_MAKE_A_CHALLANGE_TO_A_DUEL_BECAUSE_S1_IS_CURRENTLY_IN_A_DUEL_PROHIBITED_AREA = new SystemMessage(2020);

    public static final SystemMessage S1_CANNOT_DUEL_BECAUSE_S1_IS_IN_A_CHAOTIC_STATE = new SystemMessage(2023);

    public static final SystemMessage S1_CANNOT_DUEL_BECAUSE_S1_IS_ALREADY_ENGAGED_IN_A_DUEL = new SystemMessage(2022);

    public static final SystemMessage S1_CANNOT_DUEL_BECAUSE_S1_IS_PARTICIPATING_IN_A_CLAN_HALL_WAR = new SystemMessage(2025);

    public static final SystemMessage S1_CANNOT_DUEL_BECAUSE_S1_IS_PARTICIPATING_IN_THE_OLYMPIAD = new SystemMessage(2024);

    public static final SystemMessage S1_CANNOT_DUEL_BECAUSE_S1_IS_CURRENTLY_RIDING_A_BOAT_WYVERN_OR_STRIDER = new SystemMessage(2027);

    public static final SystemMessage S1_CANNOT_DUEL_BECAUSE_S1_IS_PARTICIPATING_IN_A_SIEGE_WAR = new SystemMessage(2026);

    public static final SystemMessage C1_IS_CURRENTLY_TELEPORTING_AND_CANNOT_PARTICIPATE_IN_THE_OLYMPIAD = new SystemMessage(2029);

    public static final SystemMessage S1_CANNOT_RECEIVE_A_DUEL_CHALLENGE_BECAUSE_S1_IS_TOO_FAR_AWAY = new SystemMessage(2028);

    public static final SystemMessage PLEASE_WAIT_A_MOMENT = new SystemMessage(2031);

    public static final SystemMessage YOU_ARE_CURRENTLY_LOGGING_IN = new SystemMessage(2030);

    public static final SystemMessage TRAP_FAILED = new SystemMessage(2002);

    public static final SystemMessage YOU_OBTAINED_AN_ORDINARY_MATERIAL = new SystemMessage(2003);

    public static final SystemMessage YOU_HAVE_AVOIDED_C1_S_ATTACK = new SystemMessage(2000);

    public static final SystemMessage AUGMENTATION_FAILED_DUE_TO_INAPPROPRIATE_CONDITIONS = new SystemMessage(2001);

    public static final SystemMessage YOU_OBTAINED_THE_ONLY_MATERIAL_OF_THIS_KIND = new SystemMessage(2006);

    public static final SystemMessage PLEASE_ENTER_THE_RECIPIENT_S_NAME = new SystemMessage(2007);

    public static final SystemMessage YOU_OBTAINED_A_RARE_MATERIAL = new SystemMessage(2004);

    public static final SystemMessage YOU_OBTAINED_A_UNIQUE_MATERIAL = new SystemMessage(2005);

    public static final SystemMessage S2_S1 = new SystemMessage(2010);

    public static final SystemMessage THE_AUGMENTED_ITEM_CANNOT_BE_DISCARDED = new SystemMessage(2011);

    public static final SystemMessage PLEASE_ENTER_THE_TEXT = new SystemMessage(2008);

    public static final SystemMessage YOU_CANNOT_EXCEED_1500_CHARACTERS = new SystemMessage(2009);

    public static final SystemMessage YOU_CANNOT_PROCEED_BECAUSE_THE_MANOR_CANNOT_ACCEPT_ANY_MORE_CROPS__ALL_CROPS_HAVE_BEEN_RETURNED = new SystemMessage(2014);

    public static final SystemMessage A_SKILL_IS_READY_TO_BE_USED_AGAIN = new SystemMessage(2015);

    public static final SystemMessage S1_HAS_BEEN_ACTIVATED = new SystemMessage(2012);

    public static final SystemMessage YOUR_SEED_OR_REMAINING_PURCHASE_AMOUNT_IS_INADEQUATE = new SystemMessage(2013);

    public static final SystemMessage S1_1 = new SystemMessage(1987);

    public static final SystemMessage S1_S_OWNER_S2 = new SystemMessage(1986);

    public static final SystemMessage S1_S_DROP_AREA_S2 = new SystemMessage(1985);

    public static final SystemMessage PRESS_THE_AUGMENT_BUTTON_TO_BEGIN = new SystemMessage(1984);

    public static final SystemMessage THE_FERRY_WILL_LEAVE_FOR_PRIMEVAL_ISLE_AFTER_ANCHORING_FOR_THREE_MINUTES = new SystemMessage(1991);

    public static final SystemMessage THE_FERRY_IS_NOW_DEPARTING_PRIMEVAL_ISLE_FOR_RUNE_HARBOR = new SystemMessage(1990);

    public static final SystemMessage THE_FERRY_WILL_LEAVE_FOR_RUNE_HARBOR_AFTER_ANCHORING_FOR_THREE_MINUTES = new SystemMessage(1989);

    public static final SystemMessage THE_FERRY_HAS_ARRIVED_AT_PRIMEVAL_ISLE = new SystemMessage(1988);

    public static final SystemMessage S1_CHANNEL_FILTERING_OPTION = new SystemMessage(1995);

    public static final SystemMessage THE_FERRY_FROM_RUNE_HARBOR_TO_PRIMEVAL_ISLE_HAS_BEEN_DELAYED = new SystemMessage(1994);

    public static final SystemMessage THE_FERRY_FROM_PRIMEVAL_ISLE_TO_RUNE_HARBOR_HAS_BEEN_DELAYED = new SystemMessage(1993);

    public static final SystemMessage THE_FERRY_IS_NOW_DEPARTING_RUNE_HARBOR_FOR_PRIMEVAL_ISLE = new SystemMessage(1992);

    public static final SystemMessage C1_DODGES_THE_ATTACK = new SystemMessage(1999);

    public static final SystemMessage YOU_COUNTERED_C1S_ATTACK = new SystemMessage(1998);

    public static final SystemMessage C1S_IS_PERFORMING_A_COUNTERATTACK = new SystemMessage(1997);

    public static final SystemMessage THE_ATTACK_HAS_BEEN_BLOCKED = new SystemMessage(1996);

    public static final SystemMessage YOU_CANNOT_AUGMENT_ITEMS_WHILE_A_PRIVATE_STORE_OR_PRIVATE_WORKSHOP_IS_IN_OPERATION = new SystemMessage(1972);

    public static final SystemMessage YOU_CANNOT_AUGMENT_ITEMS_WHILE_FROZEN = new SystemMessage(1973);

    public static final SystemMessage YOU_CANNOT_AUGMENT_ITEMS_WHILE_DEAD = new SystemMessage(1974);

    public static final SystemMessage YOU_CANNOT_AUGMENT_ITEMS_WHILE_ENGAGED_IN_TRADE_ACTIVITIES = new SystemMessage(1975);

    public static final SystemMessage S1_S_OWNER = new SystemMessage(1968);

    public static final SystemMessage AREA_WHERE_S1_APPEARS = new SystemMessage(1969);

    public static final SystemMessage ONCE_AN_ITEM_IS_AUGMENTED_IT_CANNOT_BE_AUGMENTED_AGAIN = new SystemMessage(1970);

    public static final SystemMessage THE_LEVEL_OF_THE_HARDENER_IS_TOO_HIGH_TO_BE_USED = new SystemMessage(1971);

    public static final SystemMessage S1S_REMAINING_MANA_IS_NOW_5 = new SystemMessage(1980);

    public static final SystemMessage S1S_REMAINING_MANA_IS_NOW_1_IT_WILL_DISAPPEAR_SOON = new SystemMessage(1981);

    public static final SystemMessage S1S_REMAINING_MANA_IS_NOW_0_AND_THE_ITEM_HAS_DISAPPEARED = new SystemMessage(1982);

    public static final SystemMessage S1 = new SystemMessage(1983);

    public static final SystemMessage YOU_CANNOT_AUGMENT_ITEMS_WHILE_PARALYZED = new SystemMessage(1976);

    public static final SystemMessage YOU_CANNOT_AUGMENT_ITEMS_WHILE_FISHING = new SystemMessage(1977);

    public static final SystemMessage YOU_CANNOT_AUGMENT_ITEMS_WHILE_SITTING_DOWN = new SystemMessage(1978);

    public static final SystemMessage S1S_REMAINING_MANA_IS_NOW_10 = new SystemMessage(1979);

    public static final SystemMessage SELECT_THE_ITEM_TO_BE_AUGMENTED = new SystemMessage(1957);

    public static final SystemMessage SINCE_S1S_PARTY_WITHDREW_FROM_THE_DUEL_S1S_PARTY_HAS_WON = new SystemMessage(1956);

    public static final SystemMessage REQUIRES_S1_S2 = new SystemMessage(1959);

    public static final SystemMessage SELECT_THE_CATALYST_FOR_AUGMENTATION = new SystemMessage(1958);

    public static final SystemMessage SINCE_C1_WAS_DISQUALIFIED_S2_HAS_WON = new SystemMessage(1953);

    public static final SystemMessage THE_DUEL_HAS_ENDED_IN_A_TIE = new SystemMessage(1952);

    public static final SystemMessage SINCE_S1_WITHDREW_FROM_THE_DUEL_S2_HAS_WON = new SystemMessage(1955);

    public static final SystemMessage SINCE_C1_S_PARTY_WAS_DISQUALIFIED_S2_S_PARTY_HAS_WON = new SystemMessage(1954);

    public static final SystemMessage AUGMENTATION_HAS_BEEN_SUCCESSFULLY_REMOVED_FROM_YOUR_S1 = new SystemMessage(1965);

    public static final SystemMessage AUGMENTATION_REMOVAL_CAN_ONLY_BE_DONE_ON_AN_AUGMENTED_ITEM = new SystemMessage(1964);

    public static final SystemMessage THE_GATE_IS_FIRMLY_LOCKED_PLEASE_TRY_AGAIN_LATER = new SystemMessage(1967);

    public static final SystemMessage ONLY_THE_CLAN_LEADER_MAY_ISSUE_COMMANDS = new SystemMessage(1966);

    public static final SystemMessage GEMSTONE_QUANTITY_IS_INCORRECT = new SystemMessage(1961);

    public static final SystemMessage THIS_IS_NOT_A_SUITABLE_ITEM = new SystemMessage(1960);

    public static final SystemMessage SELECT_THE_ITEM_FROM_WHICH_YOU_WISH_TO_REMOVE_AUGMENTATION = new SystemMessage(1963);

    public static final SystemMessage THE_ITEM_WAS_SUCCESSFULLY_AUGMENTED = new SystemMessage(1962);

    public static final SystemMessage THE_OPPOSING_PARTY_IS_CURRENTLY_UNABLE_TO_ACCEPT_A_CHALLENGE_TO_A_DUEL = new SystemMessage(1942);

    public static final SystemMessage THE_OPPOSING_PARTY_IS_CURRENTLY_NOT_IN_A_SUITABLE_LOCATION_FOR_A_DUEL = new SystemMessage(1943);

    public static final SystemMessage YOU_ARE_UNABLE_TO_REQUEST_A_DUEL_AT_THIS_TIME = new SystemMessage(1940);

    public static final SystemMessage THIS_IS_NOT_A_SUITABLE_PLACE_TO_CHALLENGE_ANYONE_OR_PARTY_TO_A_DUEL = new SystemMessage(1941);

    public static final SystemMessage S1_HAS_CHALLENGED_YOU_TO_A_DUEL = new SystemMessage(1938);

    public static final SystemMessage S1S_PARTY_HAS_CHALLENGED_YOUR_PARTY_TO_A_DUEL = new SystemMessage(1939);

    public static final SystemMessage THE_OPPOSING_PARTY_HAS_DECLINED_YOUR_CHALLENGE_TO_A_DUEL = new SystemMessage(1936);

    public static final SystemMessage SINCE_THE_PERSON_YOU_CHALLENGED_IS_NOT_CURRENTLY_IN_A_PARTY_THEY_CANNOT_DUEL_AGAINST_YOUR_PARTY = new SystemMessage(1937);

    public static final SystemMessage S1_HAS_WON_THE_DUEL = new SystemMessage(1950);

    public static final SystemMessage S1S_PARTY_HAS_WON_THE_DUEL = new SystemMessage(1951);

    public static final SystemMessage THE_DUEL_WILL_BEGIN_IN_S1_SECONDS_1 = new SystemMessage(1948);

    public static final SystemMessage LET_THE_DUEL_BEGIN = new SystemMessage(1949);

    public static final SystemMessage C1_HAS_CHALLENGED_YOU_TO_A_DUEL_WILL_YOU_ACCEPT = new SystemMessage(1946);

    public static final SystemMessage C1_S_PARTY_HAS_CHALLENGED_YOUR_PARTY_TO_A_DUEL_WILL_YOU_ACCEPT = new SystemMessage(1947);

    public static final SystemMessage IN_A_MOMENT_YOU_WILL_BE_TRANSPORTED_TO_THE_SITE_WHERE_THE_DUEL_WILL_TAKE_PLACE = new SystemMessage(1944);

    public static final SystemMessage THE_DUEL_WILL_BEGIN_IN_S1_SECONDS = new SystemMessage(1945);

    public static final SystemMessage S1_HAS_BEEN_CHALLENGED_TO_A_DUEL = new SystemMessage(1927);

    public static final SystemMessage THERE_IS_NO_OPPONENT_TO_RECEIVE_YOUR_CHALLENGE_FOR_A_DUEL = new SystemMessage(1926);

    public static final SystemMessage DUE_TO_THE_AFFECTS_OF_THE_SEAL_OF_STRIFE_IT_IS_NOT_POSSIBLE_TO_SUMMON_AT_THIS_TIME = new SystemMessage(1925);

    public static final SystemMessage CURRENT_LOCATION__S1_S2_S3_NEAR_PRIMEVAL_ISLE = new SystemMessage(1924);

    public static final SystemMessage COURT_MAGICIAN__THE_PORTAL_HAS_BEEN_CREATED = new SystemMessage(1923);

    public static final SystemMessage BECAUSE_S1_FAILED_TO_KILL_FOR_ONE_FULL_DAY_IT_HAS_EXPIRED = new SystemMessage(1922);

    public static final SystemMessage S2_HOURS_AND_S3_MINUTES_HAVE_PASSED_SINCE_S1_HAS_KILLED = new SystemMessage(1921);

    public static final SystemMessage YOUR_ACCOUNT_IS_CURRENTLY_INACTIVE_BECAUSE_YOU_HAVE_NOT_LOGGED_INTO_THE_GAME_FOR_SOME_TIME_YOU = new SystemMessage(1920);

    public static final SystemMessage C1_HAS_DECLINED_YOUR_CHALLENGE_TO_A_PARTY_DUEL = new SystemMessage(1935);

    public static final SystemMessage S1_HAS_ACCEPTED_YOUR_CHALLENGE_TO_DUEL_AGAINST_THEIR_PARTY_THE_DUEL_WILL_BEGIN_IN_A_FEW_MOMENTS = new SystemMessage(1934);

    public static final SystemMessage YOU_HAVE_ACCEPTED_S1S_CHALLENGE_TO_A_PARTY_DUEL_THE_DUEL_WILL_BEGIN_IN_A_FEW_MOMENTS = new SystemMessage(1933);

    public static final SystemMessage C1_HAS_DECLINED_YOUR_CHALLENGE_TO_A_DUEL = new SystemMessage(1932);

    public static final SystemMessage S1_HAS_DECLINED_YOUR_CHALLENGE_TO_A_DUEL = new SystemMessage(1931);

    public static final SystemMessage YOU_HAVE_ACCEPTED_S1S_CHALLENGE_TO_A_DUEL_THE_DUEL_WILL_BEGIN_IN_A_FEW_MOMENTS = new SystemMessage(1930);

    public static final SystemMessage S1_HAS_ACCEPTED_YOUR_CHALLENGE_TO_A_DUEL_THE_DUEL_WILL_BEGIN_IN_A_FEW_MOMENTS = new SystemMessage(1929);

    public static final SystemMessage S1S_PARTY_HAS_BEEN_CHALLENGED_TO_A_DUEL = new SystemMessage(1928);

    public static final SystemMessage THIS_INSTANCE_ZONE_WILL_BE_TERMINATED_BECAUSE_THE_NPC_SERVER_IS_UNAVAILABLE_YOU_WILL_BE_FORCIBLY = new SystemMessage(2200);

    public static final SystemMessage S1YEARS_S2MONTHS_S3DAYS = new SystemMessage(2201);

    public static final SystemMessage S1HOURS_S2MINUTES_S3_SECONDS = new SystemMessage(2202);

    public static final SystemMessage S1_M_S2_D = new SystemMessage(2203);

    public static final SystemMessage S1HOURS = new SystemMessage(2204);

    public static final SystemMessage YOU_HAVE_ENTERED_AN_AREA_WHERE_THE_MINI_MAP_CANNOT_BE_USED_THE_MINI_MAP_WILL_BE_CLOSED = new SystemMessage(2205);

    public static final SystemMessage YOU_HAVE_ENTERED_AN_AREA_WHERE_THE_MINI_MAP_CAN_BE_USED = new SystemMessage(2206);

    public static final SystemMessage THIS_IS_AN_AREA_WHERE_YOU_CANNOT_USE_THE_MINI_MAP_THE_MINI_MAP_WILL_NOT_BE_OPENED = new SystemMessage(2207);

    public static final SystemMessage YOU_HAVE_BID_ON_AN_ITEM_AUCTION = new SystemMessage(2192);

    public static final SystemMessage ITS_TOO_FAR_FROM_THE_NPC_TO_WORK = new SystemMessage(2193);

    public static final SystemMessage CURRENT_POLYMORPH_FORM_CANNOT_BE_APPLIED_WITH_CORRESPONDING_EFFECTS = new SystemMessage(2194);

    public static final SystemMessage THERE_IS_NOT_ENOUGHT_SOUL = new SystemMessage(2195);

    public static final SystemMessage NO_OWNED_CLAN = new SystemMessage(2196);

    public static final SystemMessage OWNED_BY_CLAN_S1 = new SystemMessage(2197);

    public static final SystemMessage YOU_HAVE_THE_HIGHEST_BID_IN_AN_ITEM_AUCTION = new SystemMessage(2198);

    public static final SystemMessage YOU_CANNOT_ENTER_THIS_INSTANCE_ZONE_WHILE_THE_NPC_SERVER_IS_UNAVAILABLE = new SystemMessage(2199);

    public static final SystemMessage ONLY_A_PARTY_LEADER_CAN_TRY_TO_ENTER = new SystemMessage(2185);

    public static final SystemMessage S1_CLAN_IS_VICTORIOUS_IN_THE_FORTRESS_BATLE_OF_S2 = new SystemMessage(2184);

    public static final SystemMessage THE_TARGET_IS_LOCATED_WHERE_YOU_CANNOT_CHARGE = new SystemMessage(2187);

    public static final SystemMessage SOUL_CANNOT_BE_ABSORBED_ANY_MORE = new SystemMessage(2186);

    public static final SystemMessage CURRENT_LOCATION__S1_S2_S3_NEAR_KAMAEL_VILLAGE = new SystemMessage(2189);

    public static final SystemMessage ANOTHER_ENCHANTMENT_IS_IN_PROGRESS_PLEASE_COMPLETE_PREVIOUS_TASK_AND_TRY_AGAIN = new SystemMessage(2188);

    public static final SystemMessage TO_APPLY_SELECTED_OPTIONS_THE_GAME_NEEDS_TO_BE_RELOADED_IF_YOU_DON_T_APPLY_NOW_IT_WILL_BE = new SystemMessage(2191);

    public static final SystemMessage CURRENT_LOCATION__S1_S2_S3_NEAR_SOUTH_OF_WASTELANDS_CAP = new SystemMessage(2190);

    public static final SystemMessage _S1S2_S_ELEMENTAL_POWER_HAS_BEEN_REMOVED = new SystemMessage(2177);

    public static final SystemMessage S1_S_ELEMENTAL_POWER_HAS_BEEN_REMOVED = new SystemMessage(2176);

    public static final SystemMessage YOU_HAVE_THE_HIGHEST_BID_SUBMITTED_IN_A_GIRAN_CASTLE_AUCTION = new SystemMessage(2179);

    public static final SystemMessage YOU_FAILED_TO_REMOVE_THE_ELEMENTAL_POWER = new SystemMessage(2178);

    public static final SystemMessage YOU_HAVE_HIGHEST_THE_BID_SUBMITTED_IN_A_RUNE_CASTLE_AUCTION = new SystemMessage(2181);

    public static final SystemMessage YOU_HAVE_THE_HIGHEST_BID_SUBMITTED_IN_AN_ADEN_CASTLE_AUCTION = new SystemMessage(2180);

    public static final SystemMessage THE_FORTRESS_BATTLE_OF_S1_HAS_FINISHED = new SystemMessage(2183);

    public static final SystemMessage YOU_CANNOT_POLYMORPH_WHILE_RIDING_A_BOAT = new SystemMessage(2182);

    public static final SystemMessage WILL_YOU_USE_THE_SELECTED_KAMAEL_RACE_ONLY_HERO_WEAPON = new SystemMessage(2234);

    public static final SystemMessage THE_INSTANCE_ZONE_IN_USE_HAS_BEEN_DELETED_AND_CANNOT_BE_ACCESSED = new SystemMessage(2235);

    public static final SystemMessage S1_WILL_BE_CRYSTALIZED_BEFORE_DESTRUCTION_WILL_YOU_CONTINUE = new SystemMessage(2232);

    public static final SystemMessage SIEGE_REGISTRATION_IS_NOT_POSSIBLE_DUE_TO_A_CONTRACT_WITH_A_HIGHER_CASTLE = new SystemMessage(2233);

    public static final SystemMessage YOU_HAVE_PARTICIPATED_IN_THE_SIEGE_OF_S1_THIS_SIEGE_WILL_CONTINUE_FOR_2_HOURS = new SystemMessage(2238);

    public static final SystemMessage THE_SIEGE_OF_S1_IN_WHICH_YOU_ARE_PARTICIPATING_HAS_FINISHED = new SystemMessage(2239);

    public static final SystemMessage S1_MINUTES_LEFT_FOR_WYVERN_RIDING = new SystemMessage(2236);

    public static final SystemMessage S1_SECONDS_LEFT_FOR_WYVERN_RIDING = new SystemMessage(2237);

    public static final SystemMessage NOT_ENOUGH_BOLTS = new SystemMessage(2226);

    public static final SystemMessage IT_IS_NOT_POSSIBLE_TO_REGISTER_FOR_THE_CASTLE_SIEGE_SIDE_OR_CASTLE_SIEGE_OF_A_HIGHER_CASTLE_IN = new SystemMessage(2227);

    public static final SystemMessage CROSSBOW_IS_PREPARING_TO_FIRE = new SystemMessage(2224);

    public static final SystemMessage THERE_ARE_NO_OTHER_SKILLS_TO_LEARN_PLEASE_COME_BACK_AFTER_S1ND_CLASS_CHANGE = new SystemMessage(2225);

    public static final SystemMessage S1_WILL_BE_AVAILABLE_FOR_RE_USE_AFTER_S2_HOURS_S3_MINUTES = new SystemMessage(2230);

    public static final SystemMessage THE_SUPPLY_ITEMS_HAVE_NOT_NOT_BEEN_PROVIDED_BECAUSE_THE_HIGHER_CASTLE_IN_CONTRACT_DOESN_T_HAVE = new SystemMessage(2231);

    public static final SystemMessage INSTANCE_ZONE_TIME_LIMIT = new SystemMessage(2228);

    public static final SystemMessage THERE_IS_NO_INSTANCE_ZONE_UNDER_A_TIME_LIMIT = new SystemMessage(2229);

    public static final SystemMessage THIS_LOWER_CLAN_SKILL_HAS_ALREADY_BEEN_ACQUIRED = new SystemMessage(2219);

    public static final SystemMessage THIS_IS_A_MAIN_CLASS_SKILL_ONLY = new SystemMessage(2218);

    public static final SystemMessage THE_BALLISTA_HAS_BEEN_SUCCESSFULLY_DESTROYED_AND_THE_CLAN_S_REPUTATION_WILL_BE_INCREASED = new SystemMessage(2217);

    public static final SystemMessage THE_CPU_DRIVER_IS_NOT_UP_TO_DATE_PLEASE_INSTALL_AN_UP_TO_DATE_CPU_DRIVER = new SystemMessage(2216);

    public static final SystemMessage IT_WILL_COST_200000_ADENA_FOR_A_FORTRESS_GATE_ENHANCEMENT_WILL_YOU_ENHANCE_IT = new SystemMessage(2223);

    public static final SystemMessage IT_WILL_COST_150000_ADENA_TO_PLACE_SCOUTS_WILL_YOU_PLACE_THEM = new SystemMessage(2222);

    public static final SystemMessage WILL_YOU_ACTIVATE_THE_SELECTED_FUNCTIONS = new SystemMessage(2221);

    public static final SystemMessage THE_PREVIOUS_LEVEL_SKILL_HAS_NOT_BEEN_LEARNED = new SystemMessage(2220);

    public static final SystemMessage YOU_MUST_LEARN_A_GOOD_DEED_SKILL_BEFORE_YOU_CAN_ACQUIRE_NEW_SKILLS = new SystemMessage(2211);

    public static final SystemMessage IT_WILL_RETURN_TO_AN_UNENCHANTED_CONDITION = new SystemMessage(2210);

    public static final SystemMessage THIS_IS_AN_AREA_WHERE_RADAR_CANNOT_BE_USED = new SystemMessage(2209);

    public static final SystemMessage YOU_DO_NOT_MEET_THE_SKILL_LEVEL_REQUIREMENTS = new SystemMessage(2208);

    public static final SystemMessage S1P_DEF = new SystemMessage(2215);

    public static final SystemMessage A_NEW_CHARACTER_WILL_BE_CREATED_WITH_THE_CURRENT_SETTINGS_CONTINUE = new SystemMessage(2214);

    public static final SystemMessage YOU_CANNOT_BOARD_A_SHIP_WHILE_YOU_ARE_POLYMORPHED = new SystemMessage(2213);

    public static final SystemMessage YOU_HAVE_NOT_COMPLETED_THE_NECESSARY_QUEST_FOR_SKILL_ACQUISITION = new SystemMessage(2212);

    public static final SystemMessage C1S_ATTACK_FAILED = new SystemMessage(2268);

    public static final SystemMessage C1_RESISTED_C2S_MAGIC = new SystemMessage(2269);

    public static final SystemMessage C1_HAS_RECEIVED_DAMAGE_FROM_S2_THROUGH_THE_FIRE_OF_MAGIC = new SystemMessage(2270);

    public static final SystemMessage C1_WEAKLY_RESISTED_C2S_MAGIC = new SystemMessage(2271);

    public static final SystemMessage C1_HAS_EVADED_C2S_ATTACK = new SystemMessage(2264);

    public static final SystemMessage C1S_ATTACK_WENT_ASTRAY = new SystemMessage(2265);

    public static final SystemMessage C1_HAD_A_CRITICAL_HIT = new SystemMessage(2266);

    public static final SystemMessage C1_RESISTED_C2S_DRAIN = new SystemMessage(2267);

    public static final SystemMessage THE_PET_CAN_RUN_AWAY_IF_THE_HUNGER_GAUGE_IS_BELOW_10 = new SystemMessage(2260);

    public static final SystemMessage C1_HAS_GIVEN_C2_DAMAGE_OF_S3 = new SystemMessage(2261);

    public static final SystemMessage C1_HAS_RECEIVED_DAMAGE_OF_S3_FROM_C2 = new SystemMessage(2262);

    public static final SystemMessage C1_HAS_RECEIVED_DAMAGE_OF_S3_THROUGH_C2 = new SystemMessage(2263);

    public static final SystemMessage YOU_CANNOT_BOARD_AN_AIRSHIP_WHILE_HOLDING_A_FLAG = new SystemMessage(2256);

    public static final SystemMessage YOU_CANNOT_BOARD_AN_AIRSHIP_WHILE_A_PET_OR_A_SERVITOR_IS_SUMMONED = new SystemMessage(2257);

    public static final SystemMessage YOU_HAVE_ALREADY_BOARDED_ANOTHER_AIRSHIP = new SystemMessage(2258);

    public static final SystemMessage CURRENT_LOCATION__S1_S2_S3_NEAR_FANTASY_ISLE = new SystemMessage(2259);

    public static final SystemMessage YOU_CANNOT_BOARD_AN_AIRSHIP_WHILE_SITTING = new SystemMessage(2253);

    public static final SystemMessage YOU_CANNOT_BOARD_AN_AIRSHIP_WHILE_IN_A_DUEL = new SystemMessage(2252);

    public static final SystemMessage YOU_CANNOT_BOARD_AN_AIRSHIP_WHILE_A_CURSED_WEAPON_IS_EQUPPED = new SystemMessage(2255);

    public static final SystemMessage YOU_CANNOT_BOARD_AN_AIRSHIP_WHILE_SKILL_CASTING = new SystemMessage(2254);

    public static final SystemMessage YOU_CANNOT_BOARD_AN_AIRSHIP_WHILE_DEAD = new SystemMessage(2249);

    public static final SystemMessage YOU_CANNOT_BOARD_AN_AIRSHIP_WHILE_PETRIFIED = new SystemMessage(2248);

    public static final SystemMessage YOU_CANNOT_BOARD_AN_AIRSHIP_WHILE_IN_BATTLE = new SystemMessage(2251);

    public static final SystemMessage YOU_CANNOT_BOARD_AN_AIRSHIP_WHILE_FISHING = new SystemMessage(2250);

    public static final SystemMessage S1_SECONDS_REMAINING = new SystemMessage(2245);

    public static final SystemMessage S1_MINUTES_REMAINING = new SystemMessage(2244);

    public static final SystemMessage YOU_CANNOT_BOARD_AN_AIRSHIP_WHILE_TRANSFORMED = new SystemMessage(2247);

    public static final SystemMessage THE_CONTEST_WILL_BEGIN_IN_S1_MINUTES = new SystemMessage(2246);

    public static final SystemMessage YOU_CANNOT_APPLY_FOR_A_CLAN_LORD_TRANSACTION_IF_YOUR_CLAN_HAS_REGISTERED_FOR_THE_TEAM_BATTLE = new SystemMessage(2241);

    public static final SystemMessage YOU_CANNOT_REGISTER_FOR_THE_TEAM_BATTLE_CLAN_HALL_WAR_WHEN_YOUR_CLAN_LORD_IS_ON_THE_WAITING_LIST = new SystemMessage(2240);

    public static final SystemMessage WHEN_A_CLAN_LORD_OCCUPYING_THE_BANDIT_STRONGHOLD_OR_WILD_BEAST_RESERVE_CLAN_HALL_IS_IN_DANGER = new SystemMessage(2243);

    public static final SystemMessage CLAN_MEMBERS_CANNOT_LEAVE_OR_BE_EXPELLED_WHEN_THEY_ARE_REGISTERED_FOR_THE_TEAM_BATTLE_CLAN_HALL = new SystemMessage(2242);

    public static final SystemMessage YOUR_VITAMIN_ITEM_HAS_ARRIVED_VISIT_THE_VITAMIN_MANAGER_IN_ANY_VILLAGE_TO_OBTAIN_IT = new SystemMessage(2302);

    public static final SystemMessage THERE_ARE_S2_SECONDS_REMAINING_IN_S1S_REUSE_TIME = new SystemMessage(2303);

    public static final SystemMessage FAILED_TO_LOAD_KEYBOARD_SECURITY_MODULE_FOR_EFFECTIVE_GAMING_FUNCTIONALITY_WHEN_THE_GAME_IS_OVER = new SystemMessage(2300);

    public static final SystemMessage CURRENT_LOCATION__STEEL_CITADEL_RESISTANCE = new SystemMessage(2301);

    public static final SystemMessage THE_COLOR_OF_THE_BADGE_OR_INSIGNIA_THAT_YOU_WANT_TO_REGISTER_DOES_NOT_MEET_THE_STANDARD = new SystemMessage(2298);

    public static final SystemMessage THE_FILE_FORMAT_OF_THE_BADGE_OR_INSIGNIA_THAT_YOU_WANT_TO_REGISTER_DOES_NOT_MEET_THE_STANDARD = new SystemMessage(2299);

    public static final SystemMessage YOU_HAVE_GAINED_VITALITY_POINTS = new SystemMessage(2296);

    public static final SystemMessage ROUND_S1 = new SystemMessage(2297);

    public static final SystemMessage THE_WIDTH_OF_THE_UPLOADED_BADGE_OR_INSIGNIA_DOES_NOT_MEET_THE_STANDARD_REQUIREMENTS = new SystemMessage(2294);

    public static final SystemMessage THE_LENGTH_OF_THE_UPLOADED_BADGE_OR_INSIGNIA_DOES_NOT_MEET_THE_STANDARD_REQUIREMENTS = new SystemMessage(2295);

    public static final SystemMessage AGATHION_SKILLS_CAN_BE_USED_ONLY_WHEN_AGATHION_IS_SUMMONED = new SystemMessage(2292);

    public static final SystemMessage CURRENT_LOCATION__S1_S2_S3_INSIDE_THE_STEEL_CITADEL = new SystemMessage(2293);

    public static final SystemMessage YOU_CANNOT_ASSIGN_SHORTCUT_KEYS_BEFORE_YOU_LOG_IN = new SystemMessage(2290);

    public static final SystemMessage YOU_CAN_OPERATE_THE_MACHINE_WHEN_YOU_PARTICIPATE_IN_THE_PARTY = new SystemMessage(2291);

    public static final SystemMessage RESURRECTION_WILL_OCCUR_IN_S1_SECONDS = new SystemMessage(2288);

    public static final SystemMessage THE_MATCH_BETWEEN_THE_PARTIES_IS_NOT_AVAILABLE_BECAUSE_ONE_OF_THE_PARTY_MEMBERS_IS_BEING = new SystemMessage(2289);

    public static final SystemMessage THERE_IS_NO_SPACE_TO_WEAR_S1 = new SystemMessage(2287);

    public static final SystemMessage YOU_CANNOT_WEAR_S1_BECAUSE_YOU_ARE_NOT_WEARING_THE_BRACELET = new SystemMessage(2286);

    public static final SystemMessage THIS_SKILL_CANNOT_REMOVE_THIS_TRAP = new SystemMessage(2285);

    public static final SystemMessage YOU_HAVE_OBTAINED_ALL_THE_POINTS_YOU_CAN_GET_TODAY_IN_A_PLACE_OTHER_THAN_INTERNET_CAF = new SystemMessage(2284);

    public static final SystemMessage YOU_CANNOT_TRANSFORM_WHILE_SITTING = new SystemMessage(2283);

    public static final SystemMessage LEAVE_FANTASY_ISLE = new SystemMessage(2282);

    public static final SystemMessage C1_HIT_YOU_FOR_S3_DAMAGE_AND_HIT_YOUR_SERVITOR_FOR_S4 = new SystemMessage(2281);

    public static final SystemMessage DAMAGE_IS_DECREASED_BECAUSE_C1_RESISTED_AGAINST_C2S_MAGIC = new SystemMessage(2280);

    public static final SystemMessage YOU_CAN_NO_LONGER_ADD_A_QUEST_TO_THE_QUEST_ALERTS = new SystemMessage(2279);

    public static final SystemMessage REMAINING_TIME_S1_S2 = new SystemMessage(2278);

    public static final SystemMessage PARTY_OF_S1 = new SystemMessage(2277);

    public static final SystemMessage THE_REBEL_ARMY_RECAPTURED_THE_FORTRESS = new SystemMessage(2276);

    public static final SystemMessage YOU_ARE_IN_AN_AREA_WHERE_YOU_CANNOT_CANCEL_PET_SUMMONING = new SystemMessage(2275);

    public static final SystemMessage YOU_ENTERED_AN_AREA_WHERE_YOU_CANNOT_THROW_AWAY_ITEMS = new SystemMessage(2274);

    public static final SystemMessage THIS_SKILL_CANNOT_BE_LEARNED_WHILE_IN_THE_SUB_CLASS_STATE_PLEASE_TRY_AGAIN_AFTER_CHANGING_TO_THE = new SystemMessage(2273);

    public static final SystemMessage THE_KEY_YOU_ASSIGNED_AS_A_SHORTCUT_KEY_IS_NOT_AVAILABLE_IN_THE_REGULAR_CHATTING_MODE = new SystemMessage(2272);

    public static final SystemMessage THAT_ITEM_CANNOT_BE_TAKEN_OFF = new SystemMessage(2065);

    public static final SystemMessage YOU_CANNOT_POLYMORPH_WHILE_UNDER_THE_EFFECT_OF_A_SPECIAL_SKILL = new SystemMessage(2064);

    public static final SystemMessage THAT_WEAPON_CANNOT_USE_ANY_OTHER_SKILL_EXCEPT_THE_WEAPON_S_SKILL = new SystemMessage(2067);

    public static final SystemMessage THAT_WEAPON_CANNOT_PERFORM_ANY_ATTACKS = new SystemMessage(2066);

    public static final SystemMessage Untrain_of_enchant_skill_was_successful_Current_level_of_enchant_skill_S1_has_been_decreased_by_1 = new SystemMessage(2069);

    public static final SystemMessage YOU_DO_NOT_HAVE_ALL_OF_THE_ITEMS_NEEDED_TO_UNTRAIN_THE_ENCHANT_SKILL = new SystemMessage(2068);

    public static final SystemMessage You_do_not_have_all_of_the_items_needed_to_enchant_skill_route_change = new SystemMessage(2071);

    public static final SystemMessage Untrain_of_enchant_skill_was_successful_Current_level_of_enchant_skill_S1_became_0_and_enchant_skill_will_be_initialized = new SystemMessage(2070);

    public static final SystemMessage Enchant_skill_route_change_was_successful_Lv_of_enchant_skill_S1_will_remain = new SystemMessage(2073);

    public static final SystemMessage Enchant_skill_route_change_was_successful_Lv_of_enchant_skill_S1_has_been_decreased_by_S2 = new SystemMessage(2072);

    public static final SystemMessage IT_IS_NOT_AN_AUCTION_PERIOD = new SystemMessage(2075);

    public static final SystemMessage Skill_enchant_failed_Current_level_of_enchant_skill_S1_will_remain_unchanged = new SystemMessage(2074);

    public static final SystemMessage YOUR_BID_MUST_BE_HIGHER_THAN_THE_CURRENT_HIGHEST_BID = new SystemMessage(2077);

    public static final SystemMessage YOUR_BID_CANNOT_EXCEED_100_BILLION = new SystemMessage(2076);

    public static final SystemMessage YOU_CURRENTLY_HAVE_THE_HIGHEST_BID = new SystemMessage(2079);

    public static final SystemMessage YOU_DO_NOT_HAVE_ENOUGH_ADENA_FOR_THIS_BID = new SystemMessage(2078);

    public static final SystemMessage THE_SHORTCUT_IN_USE_CONFLICTS_WITH_S1_DO_YOU_WISH_TO_RESET_THE_CONFLICTING_SHORTCUTS_AND_USE_THE = new SystemMessage(2048);

    public static final SystemMessage THE_SHORTCUT_WILL_BE_APPLIED_AND_SAVED_IN_THE_SERVER_WILL_YOU_CONTINUE = new SystemMessage(2049);

    public static final SystemMessage S1_CLAN_IS_TRYING_TO_DISPLAY_A_FLAG = new SystemMessage(2050);

    public static final SystemMessage YOU_MUST_ACCEPT_THE_USER_AGREEMENT_BEFORE_THIS_ACCOUNT_CAN_ACCESS_LINEAGE_II__N_PLEASE_TRY_AGAIN = new SystemMessage(2051);

    public static final SystemMessage A_GUARDIAN_S_CONSENT_IS_REQUIRED_BEFORE_THIS_ACCOUNT_CAN_BE_USED_TO_PLAY_LINEAGE_II__NPLEASE_TRY = new SystemMessage(2052);

    public static final SystemMessage THIS_ACCOUNT_HAS_DECLINED_THE_USER_AGREEMENT_OR_IS_PENDING_A_WITHDRAWL_REQUEST___NPLEASE_TRY = new SystemMessage(2053);

    public static final SystemMessage THIS_ACCOUNT_HAS_BEEN_SUSPENDED___NFOR_MORE_INFORMATION_PLEASE_CALL_THE_CUSTOMER_S_CENTER_TEL = new SystemMessage(2054);

    public static final SystemMessage YOUR_ACCOUNT_HAS_BEEN_SUSPENDED_FROM_ALL_GAME_SERVICES__NFOR_MORE_INFORMATION_PLEASE_VISIT_THE = new SystemMessage(2055);

    public static final SystemMessage YOUR_ACCOUNT_HAS_BEEN_CONVERTED_TO_AN_INTEGRATED_ACCOUNT_AND_IS_UNABLE_TO_BE_ACCESSED___NPLEASE = new SystemMessage(2056);

    public static final SystemMessage YOU_HAVE_BLOCKED_C1 = new SystemMessage(2057);

    public static final SystemMessage YOU_ALREADY_POLYMORPHED_AND_CANNOT_POLYMORPH_AGAIN = new SystemMessage(2058);

    public static final SystemMessage THE_NEARBLY_AREA_IS_TOO_NARROW_FOR_YOU_TO_POLYMORPH_PLEASE_MOVE_TO_ANOTHER_AREA_AND_TRY_TO_POLYMORPH_AGAIN = new SystemMessage(2059);

    public static final SystemMessage YOU_CANNOT_POLYMORPH_INTO_THE_DESIRED_FORM_IN_WATER = new SystemMessage(2060);

    public static final SystemMessage YOU_ARE_STILL_UNDER_TRANSFORM_PENALTY_AND_CANNOT_BE_POLYMORPHED = new SystemMessage(2061);

    public static final SystemMessage YOU_CANNOT_POLYMORPH_WHEN_YOU_HAVE_SUMMONED_A_SERVITOR_PET = new SystemMessage(2062);

    public static final SystemMessage YOU_CANNOT_POLYMORPH_WHILE_RIDING_A_PET = new SystemMessage(2063);

    public static final SystemMessage C1S_ITEM_REQUIREMENT_IS_NOT_SUFFICIENT_AND_CANNOT_BE_ENTERED = new SystemMessage(2099);

    public static final SystemMessage C1S_QUEST_REQUIREMENT_IS_NOT_SUFFICIENT_AND_CANNOT_BE_ENTERED = new SystemMessage(2098);

    public static final SystemMessage C1S_LEVEL_REQUIREMENT_IS_NOT_SUFFICIENT_AND_CANNOT_BE_ENTERED = new SystemMessage(2097);

    public static final SystemMessage C1_IS_IN_A_LOCATION_WHICH_CANNOT_BE_ENTERED_THEREFORE_IT_CANNOT_BE_PROCESSED = new SystemMessage(2096);

    public static final SystemMessage YOU_CANNOT_ENTER_BECAUSE_YOU_ARE_NOT_IN_A_CURRENT_COMMAND_CHANNEL = new SystemMessage(2103);

    public static final SystemMessage YOU_CANNOT_ENTER_DUE_TO_THE_PARTY_HAVING_EXCEEDED_THE_LIMIT = new SystemMessage(2102);

    public static final SystemMessage YOU_ARE_NOT_CURRENTLY_IN_A_PARTY_SO_YOU_CANNOT_ENTER = new SystemMessage(2101);

    public static final SystemMessage C1_MAY_NOT_RE_ENTER_YET = new SystemMessage(2100);

    public static final SystemMessage THIS_INSTANCE_ZONE_WILL_BE_TERMINATED_IN_S1_MINUTES_YOU_WILL_BE_FORCED_OUT_OF_THE_DANGEON_THEN_TIME_EXPIRES = new SystemMessage(2107);

    public static final SystemMessage THIS_DUNGEON_WILL_EXPIRE_IN_S1_MINUTES = new SystemMessage(2106);

    public static final SystemMessage YOU_HAVE_ENTERED_ANOTHER_INSTANCE_ZONE_THEREFORE_YOU_CANNOT_ENTER_CORRESPONDING_DUNGEON = new SystemMessage(2105);

    public static final SystemMessage THE_MAXIMUM_NUMBER_OF_INSTANCE_ZONES_HAS_BEEN_EXCEEDED_YOU_CANNOT_ENTER = new SystemMessage(2104);

    public static final SystemMessage ENTER_A_SHORTCUT_TO_ASSIGN = new SystemMessage(2111);

    public static final SystemMessage THIS_CHARACTER_NAME_ALREADY_EXISTS_OR_IS_AN_INVALID_NAME_PLEASE_ENTER_A_NEW_NAME = new SystemMessage(2110);

    public static final SystemMessage THE_SERVER_HAS_BEEN_INTEGRATED_AND_YOUR_CHARACTER_S1_HAS_BEEN_OVERLAPPED_WITH_ANOTHER_NAME = new SystemMessage(2109);

    public static final SystemMessage YOUR_ACCOUNT_HAS_BEEN_SUSPENDED_FOR_10_DAYS_FOR_USE_OF_ILLEGAL_SOFTWARE_AND_MAY_BE_PERMANENTLY = new SystemMessage(2108);

    public static final SystemMessage YOU_HAVE_EXCEEDED_THE_TOTAL_AMOUT_OF_ADENA_ALLOWED_IN_INVENTORY = new SystemMessage(2082);

    public static final SystemMessage THE_AUCTION_HAS_BEGUN = new SystemMessage(2083);

    public static final SystemMessage YOU_HAVE_BEEN_OUTBID = new SystemMessage(2080);

    public static final SystemMessage THERE_ARE_NO_FUNDS_PRESENTLY_DUE_TO_YOU = new SystemMessage(2081);

    public static final SystemMessage SEARCH_ON_USER_C2_FOR_THIRD_PARTY_PROGRAM_USE_WILL_BE_COMPLETED_IN_S1_MINUTES = new SystemMessage(2086);

    public static final SystemMessage A_FORTRESS_IS_UNDER_ATTACK = new SystemMessage(2087);

    public static final SystemMessage ENEMY_BLOOD_PLEDGES_HAVE_INTRUDED_INTO_THE_FORTRESS = new SystemMessage(2084);

    public static final SystemMessage SHOUT_AND_TRADE_CHATING_CANNOT_BE_USED_SHILE_POSSESSING_A_CURSED_WEAPON = new SystemMessage(2085);

    public static final SystemMessage THE_FORTRESS_BATTLE_S1_HAS_BEGAN = new SystemMessage(2090);

    public static final SystemMessage YOUR_ACCOUNT_CAN_ONLY_BE_USED_AFTER_CHANGING_YOUR_PASSWORD_AND_QUIZ___N_SERVICES_WILL_BE = new SystemMessage(2091);

    public static final SystemMessage S1_MINUTE_UNTIL_THE_FORTRESS_BATTLE_STARTS = new SystemMessage(2088);

    public static final SystemMessage S1_SECOND_UNTIL_THE_FORTRESS_BATTLE_STARTS = new SystemMessage(2089);

    public static final SystemMessage ANOTHER_USER_IS_PURCHASING_PLEASE_TRY_AGAIN_LATER = new SystemMessage(2094);

    public static final SystemMessage SOME_LINEAGE_II_FEATURES_HAVE_BEEN_LIMITED_FOR_FREE_TRIALS_TRIAL_ACCOUNTS_HAVE_LIMITED_CHATTING = new SystemMessage(2095);

    public static final SystemMessage YOU_CANNOT_BID_DUE_TO_A_PASSED_IN_PRICE = new SystemMessage(2092);

    public static final SystemMessage THE_BID_AMOUNT_WAS_S1_ADENA_WOULD_YOU_LIKE_TO_RETRIEVE_THE_BID_AMOUNT = new SystemMessage(2093);

    public static final SystemMessage YOU_CANNOT_BOOKMARK_THIS_LOCATION_BECAUSE_YOU_DO_NOT_HAVE_A_MY_TELEPORT_FLAG = new SystemMessage(6501);

    public static final SystemMessage YOU_HAVE_ENTERED_AN_ADULTS_ONLY_SEVER = new SystemMessage(2133);

    public static final SystemMessage YOU_HAVE_ENTERED_A_COMMON_SEVER = new SystemMessage(2132);

    public static final SystemMessage THE_EVIL_THOMAS_D_TURKEY_HAS_APPEARED_PLEASE_SAVE_SANTA = new SystemMessage(6503);

    public static final SystemMessage BECAUSE_OF_YOUR_FATIGUE_LEVEL_THIS_IS_NOT_ALLOWED = new SystemMessage(2135);

    public static final SystemMessage MY_TELEPORT_FLAG__S1 = new SystemMessage(6502);

    public static final SystemMessage YOU_HAVE_ENTERED_A_SERVER_FOR_JUVENILES = new SystemMessage(2134);

    public static final SystemMessage THE_AUGMENTED_ITEM_CANNOT_BE_CONVERTED_PLEASE_CONVERT_AFTER_THE_AUGMENTATION_HAS_BEEN_REMOVED = new SystemMessage(2129);

    public static final SystemMessage YOUR_ACCOUNT_HAS_BEEN_SUSPENDED_FROM_ALL_GAME_SERVICES_FOR_USING_THE_GAME_FOR_COMMERCIAL = new SystemMessage(2128);

    public static final SystemMessage YOU_HAVE_BID_THE_HIGHEST_PRICE_AND_HAVE_WON_THE_ITEM_THE_ITEM_CAN_BE_FOUND_IN_YOUR_PERSONAL = new SystemMessage(2131);

    public static final SystemMessage YOU_CANNOT_CONVERT_THIS_ITEM = new SystemMessage(2130);

    public static final SystemMessage YOU_SLIGHTLY_RESISTED_C1_S_MAGIC = new SystemMessage(2141);

    public static final SystemMessage C1_S_SERVITOR = new SystemMessage(2140);

    public static final SystemMessage YOU_CANNOT_ADD_ELEMENTAL_POWER_WHILE_OPERATING_A_PRIVATE_STORE_OR_PRIVATE_WORKSHOP = new SystemMessage(2143);

    public static final SystemMessage YOU_CANNOT_EXPEL_C1_BECAUSE_C1_IS_NOT_A_PARTY_MEMBER = new SystemMessage(2142);

    public static final SystemMessage YOU_DID_NOT_RESCUE_SANTA_AND_THOMAS_D_TURKEY_HAS_DISAPPEARED = new SystemMessage(6505);

    public static final SystemMessage YOU_ARE_ABOUT_TO_BID_S1_ITEM_WITH_S2_ADENA_WILL_YOU_CONTINUE = new SystemMessage(2137);

    public static final SystemMessage YOU_WON_THE_BATTLE_AGAINST_THOMAS_D_TURKEY_SANTA_HAS_BEEN_RESCUED = new SystemMessage(6504);

    public static final SystemMessage A_CLAN_NAME_CHANGE_APPLICATION_HAS_BEEN_SUBMITTED = new SystemMessage(2136);

    public static final SystemMessage YOU_FEEL_REFRESHED_EVERYTHING_APPEARS_CLEAR = new SystemMessage(6507);

    public static final SystemMessage C1_S_PET = new SystemMessage(2139);

    public static final SystemMessage ALTHOUGH_YOU_CAN_T_BE_CERTAIN_THE_AIR_SEEMS_LADEN_WITH_THE_SCENT_OF_FRESHLY_BAKED_BREAD = new SystemMessage(6506);

    public static final SystemMessage PLEASE_ENTER_A_BID_PRICE = new SystemMessage(2138);

    public static final SystemMessage YOUR_ACCOUNT_HAS_BEEN_SUSPENDED_FOR_ABUSING_A_FREE_NCCOIN_FOR_MORE_INFORMATION_PLEASE_VISIT_THE = new SystemMessage(2116);

    public static final SystemMessage YOUR_ACCOUNT_HAS_BEEN_SUSPENDED_FOR_USING_ANOTHER_PERSON_S_IDENTIFICATION_IF_YOU_WERE_NOT = new SystemMessage(2117);

    public static final SystemMessage YOUR_ACCOUNT_HAS_BEEN_SUSPENDED_FOR_MISAPPROPRIATING_PAYMENT_UNDER_ANOTHER_PLAYER_S_ACCOUNT_FOR = new SystemMessage(2118);

    public static final SystemMessage YOUR_ACCOUNT_HAS_BEEN_SUSPENDED_FROM_ALL_GAME_SERVICES_AFTER_BEING_DETECTED_WITH_DEALING_AN = new SystemMessage(2119);

    public static final SystemMessage SUB_KEY_CAN_BE_CTRL_ALT_SHIFT_AND_YOU_MAY_ENTER_TWO_SUB_KEYS_AT_A_TIME___N_EXAMPLE__CTRL___ALT__ = new SystemMessage(2112);

    public static final SystemMessage CTRL_ALT_SHIFT_KEYS_MAY_BE_USED_AS_SUB_KEY_IN_EXPANDED_SUB_KEY_MODE_AND_ONLY_ALT_MAY_BE_USED_AS = new SystemMessage(2113);

    public static final SystemMessage FORCED_ATTACK_AND_STAND_IN_PLACE_ATTACKS_ASSIGNED_PREVIOUSLY_TO_CTRL_AND_SHIFT_WILL_BE_CHANGED = new SystemMessage(2114);

    public static final SystemMessage YOUR_ACCOUNT_HAS_BEEN_SUSPENDED_FOR_ABUSING_A_BUG_RELATED_TO_THE_NCCOIN_FOR_MORE_INFORMATION = new SystemMessage(2115);

    public static final SystemMessage THE_SERVER_HAS_BEEN_INTEGRATED_AND_YOUR_CLAN_NAME_S1_HAS_BEEN_OVERLAPPED_WITH_ANOTHER_NAME = new SystemMessage(2124);

    public static final SystemMessage THE_NAME_ALREADY_EXISTS_OR_IS_AN_INVALID_NAME_PLEASE_ENTER_THE_CLAN_NAME_TO_BE_CHANGED = new SystemMessage(2125);

    public static final SystemMessage YOUR_ACCOUNT_HAS_BEEN_SUSPENDED_FOR_REGULARLY_POSTING_ILLEGAL_MESSAGES_FOR_MORE_INFORMATION = new SystemMessage(2126);

    public static final SystemMessage YOUR_ACCOUNT_HAS_BEEN_SUSPENDED_AFTER_BEING_DETECTED_WITH_AN_ILLEGAL_MESSAGE_FOR_MORE = new SystemMessage(2127);

    public static final SystemMessage YOUR_ACCOUNT_HAS_BEEN_SUSPENDED_FOR_10_DAYS_FOR_USING_ILLEGAL_SOFTWARE_YOUR_ACCOUNT_MAY_BE = new SystemMessage(2120);

    public static final SystemMessage YOUR_ACCOUNT_HAS_BEEN_SUSPENDED_FROM_ALL_GAME_SERVICES_FOR_USE_OF_ILLEGAL_SOFTWARE_FOR_MORE = new SystemMessage(2121);

    public static final SystemMessage YOUR_ACCOUNT_HAS_BEEN_SUSPENDED_FROM_ALL_GAME_SERVICES_FOR_USE_OF_ILLEGAL_SOFTWARE_FOR_MORE_ = new SystemMessage(2122);

    public static final SystemMessage YOUR_ACCOUNT_HAS_BEEN_SUSPENDED_AT_YOUR_OWN_REQUEST_FOR_MORE_INFORMATION_PLEASE_VISIT_THE = new SystemMessage(2123);

    public static final SystemMessage A_MALICIOUS_SKILL_CANNOT_BE_USED_IN_A_PEACE_ZONE = new SystemMessage(2167);

    public static final SystemMessage ALL_BARRACKS_ARE_OCCUPIED = new SystemMessage(2166);

    public static final SystemMessage THE_BARRACKS_FUNCTION_HAS_BEEN_RESTORED = new SystemMessage(2165);

    public static final SystemMessage THE_BARRACKS_HAVE_BEEN_SEIZED = new SystemMessage(2164);

    public static final SystemMessage SOUL_CANNOT_BE_INCREASED_ANY_MORE = new SystemMessage(2163);

    public static final SystemMessage YOUR_SOUL_HAS_INCREASED_BY_S1_SO_IT_IS_NOW_AT_S2 = new SystemMessage(2162);

    public static final SystemMessage THERE_IS_NOT_ENOUGH_SPACE_TO_MOVE_THE_SKILL_CANNOT_BE_USED = new SystemMessage(2161);

    public static final SystemMessage BIDDER_EXISTS__AUCTION_TIME_HAS_BEEN_EXTENDED_BY_3_MINUTES = new SystemMessage(2160);

    public static final SystemMessage PARTY_DUEL_CANNOT_BE_INITIATED_DUEL_TO_A_POLYMORPHED_PARTY_MEMBER = new SystemMessage(2175);

    public static final SystemMessage C1_CANNOT_DUEL_BECAUSE_C1_IS_CURRENTLY_POLYMORPHED = new SystemMessage(2174);

    public static final SystemMessage S1_S_AUCTION_HAS_ENDED = new SystemMessage(2173);

    public static final SystemMessage TRADE_S1_S2_S_AUCTION_HAS_ENDED = new SystemMessage(2172);

    public static final SystemMessage THIS_ITEM_CANNOT_BE_CRYSTALIZED = new SystemMessage(2171);

    public static final SystemMessage A_MALICIOUS_SKILL_CANNOT_BE_USED_WHEN_AN_OPPONENT_IS_IN_THE_PEACE_ZONE = new SystemMessage(2170);

    public static final SystemMessage YOUR_CLAN_HAS_BEEN_REGISTERED_TO_S1_FORTRESS_BATTLE = new SystemMessage(2169);

    public static final SystemMessage C1_HAS_ACQUIRED_THE_FLAG = new SystemMessage(2168);

    public static final SystemMessage ANOTHER_ELEMENTAL_POWER_HAS_ALREADY_BEEN_ADDED_THIS_ELEMENTAL_POWER_CANNOT_BE_ADDED = new SystemMessage(2150);

    public static final SystemMessage YOUR_OPPONENT_HAS_RESISTANCE_TO_MAGIC_THE_DAMAGE_WAS_DECREASED = new SystemMessage(2151);

    public static final SystemMessage S3_ELEMENTAL_POWER_HAS_BEEN_ADDED_SUCCESSFULLY_TO__S1S2 = new SystemMessage(2148);

    public static final SystemMessage YOU_HAVE_FAILED_TO_ADD_ELEMENTAL_POWER = new SystemMessage(2149);

    public static final SystemMessage ELEMENTAL_POWER_ENCHANCER_USAGE_REQUIREMENT_IS_NOT_SUFFICIENT = new SystemMessage(2146);

    public static final SystemMessage S2_ELEMENTAL_POWER_HAS_BEEN_ADDED_SUCCESSFULLY_TO_S1 = new SystemMessage(2147);

    public static final SystemMessage PLEASE_SELECT_ITEM_TO_ADD_ELEMENTAL_POWER = new SystemMessage(2144);

    public static final SystemMessage ELEMENTAL_POWER_ENCHANCER_USAGE_HAS_BEEN_CANCELLED = new SystemMessage(2145);

    public static final SystemMessage FORCE_ATTACK_IS_IMPOSSIBLE_AGAINST_A_TEMPORARY_ALLIED_MEMBER_DURING_A_SIEGE = new SystemMessage(2158);

    public static final SystemMessage BIDDER_EXISTS__THE_AUCTION_TIME_HAS_BEEN_EXTENDED_BY_5_MINUTES = new SystemMessage(2159);

    public static final SystemMessage THERE_ARE_NOT_ENOUGH_NECESSARY_ITEMS_TO_USE_THE_SKILL = new SystemMessage(2156);

    public static final SystemMessage BID_WILL_BE_ATTEMPTED_WITH_S1_ADENA = new SystemMessage(2157);

    public static final SystemMessage THE_TARGET_IS_NOT_A_FLAGPOLE_SO_A_FLAG_CANNOT_BE_DISPLAYED = new SystemMessage(2154);

    public static final SystemMessage A_FLAG_IS_ALREADY_BEING_DISPLAYED_ANOTHER_FLAG_CANNOT_BE_DISPLAYED = new SystemMessage(2155);

    public static final SystemMessage THE_ASSIGNED_SHORTCUT_WILL_BE_DELETED_AND_THE_INITIAL_SHORTCUT_SETTING_RESTORED_WILL_YOU = new SystemMessage(2152);

    public static final SystemMessage YOU_ARE_CURRENTLY_LOGGED_INTO_10_OF_YOUR_ACCOUNTS_AND_CAN_NO_LONGER_ACCESS_YOUR_OTHER_ACCOUNTS = new SystemMessage(2153);

    public static final SystemMessage FLAMES_FILLED_WITH_THE_WRATH_OF_VALAKAS_ARE_ENGULFING_YOU = new SystemMessage(2443);

    public static final SystemMessage THE_REQUEST_CANNOT_BE_MADE_BECAUSE_THE_REQUIREMENTS_HAVE_NOT_BEEN_MET_TO_PARTICIPATE_IN_A_TEAM = new SystemMessage(2442);

    public static final SystemMessage ONLY_A_PARTY_LEADER_CAN_REQUEST_A_TEAM_MATCH = new SystemMessage(2441);

    public static final SystemMessage C1_IS_ALREADY_REGISTERED_ON_THE_WAITING_LIST_FOR_THE_NON_CLASS_LIMITED_MATCH_EVENT = new SystemMessage(2440);

    public static final SystemMessage FIVE_YEARS_HAVE_PASSED_SINCE_THIS_CHARACTERS_CREATION = new SystemMessage(2447);

    public static final SystemMessage THE_BATTLEFIELD_CHANNEL_HAS_BEEN_DEACTIVATED = new SystemMessage(2446);

    public static final SystemMessage THE_BATTLEFIELD_CHANNEL_HAS_BEEN_ACTIVATED = new SystemMessage(2445);

    public static final SystemMessage FLAMES_FILLED_WITH_THE_AUTHORITY_OF_VALAKAS_ARE_BINDING_YOUR_MIND = new SystemMessage(2444);

    public static final SystemMessage THE_THIRD_STRONGHOLD_S_COMPRESSOR_HAS_BEEN_DESTROYED = new SystemMessage(2435);

    public static final SystemMessage THE_SECOND_STRONGHOLD_S_COMPRESSOR_HAS_BEEN_DESTROYED = new SystemMessage(2434);

    public static final SystemMessage THE_FIRST_STRONGHOLD_S_COMPRESSOR_HAS_BEEN_DESTROYED = new SystemMessage(2433);

    public static final SystemMessage THE_CENTRAL_STRONGHOLD_S_COMPRESSOR_HAS_BEEN_DESTROYED = new SystemMessage(2432);

    public static final SystemMessage THE_THIRD_STRONGHOLD_S_COMPRESSOR_IS_WORKING = new SystemMessage(2439);

    public static final SystemMessage THE_SECOND_STRONGHOLD_S_COMPRESSOR_IS_WORKING = new SystemMessage(2438);

    public static final SystemMessage THE_FIRST_STRONGHOLD_S_COMPRESSOR_IS_WORKING = new SystemMessage(2437);

    public static final SystemMessage THE_CENTRAL_STRONGHOLD_S_COMPRESSOR_IS_WORKING = new SystemMessage(2436);

    public static final SystemMessage THE_AIRSHIP_OWNED_BY_THE_CLAN_IS_ALREADY_BEING_USED_BY_ANOTHER_CLAN_MEMBER = new SystemMessage(2458);

    public static final SystemMessage THE_AIRSHIP_SUMMON_LICENSE_HAS_ALREADY_BEEN_ACQUIRED = new SystemMessage(2459);

    public static final SystemMessage IN_ORDER_TO_ACQUIRE_AN_AIRSHIP_THE_CLAN_S_LEVEL_MUST_BE_LEVEL_5_OR_HIGHER = new SystemMessage(2456);

    public static final SystemMessage AN_AIRSHIP_CANNOT_BE_SUMMONED_BECAUSE_EITHER_YOU_HAVE_NOT_REGISTERED_YOUR_AIRSHIP_LICENSE_OR_THE = new SystemMessage(2457);

    public static final SystemMessage THE_AIRSHIP_CANNOT_BE_SUMMONED_BECAUSE_YOU_DON_T_HAVE_ENOUGH_S1 = new SystemMessage(2462);

    public static final SystemMessage THE_AIRSHIP_S_FUEL_EP_WILL_SOON_RUN_OUT = new SystemMessage(2463);

    public static final SystemMessage THE_CLAN_OWNED_AIRSHIP_ALREADY_EXISTS = new SystemMessage(2460);

    public static final SystemMessage THE_AIRSHIP_OWNED_BY_THE_CLAN_CAN_ONLY_BE_PURCHASED_BY_THE_CLAN_LORD = new SystemMessage(2461);

    public static final SystemMessage C1S_CHARACTER_BIRTHDAY_IS_S3S4S2 = new SystemMessage(2450);

    public static final SystemMessage THE_CLOAK_EQUIP_HAS_BEEN_REMOVED_BECAUSE_THE_ARMOR_SET_EQUIP_HAS_BEEN_REMOVED = new SystemMessage(2451);

    public static final SystemMessage YOUR_BIRTHDAY_GIFT_HAS_ARRIVED_YOU_CAN_OBTAIN_IT_FROM_THE_GATEKEEPER_IN_ANY_VILLAGE = new SystemMessage(2448);

    public static final SystemMessage THERE_ARE_S1_DAYS_UNTIL_YOUR_CHARACTERS_BIRTHDAY_ON_THAT_DAY_YOU_CAN_OBTAIN_A_SPECIAL_GIFT_FROM_THE_GATEKEEPER_IN_ANY_VILLAGE = new SystemMessage(2449);

    public static final SystemMessage KRESNIK_CLASS_AIRSHIP = new SystemMessage(2454);

    public static final SystemMessage THE_AIRSHIP_MUST_BE_SUMMONED_IN_ORDER_FOR_YOU_TO_BOARD = new SystemMessage(2455);

    public static final SystemMessage THE_INVENTORY_IS_FULL_SO_IT_CANNOT_BE_EQUIPPED_OR_REMOVED_ON_THE_BELT = new SystemMessage(2452);

    public static final SystemMessage THE_CLOAK_CANNOT_BE_EQUIPPED_BECAUSE_A_NECESSARY_ITEM_IS_NOT_EQUIPPED = new SystemMessage(2453);

    public static final SystemMessage YOU_HAVE_BEEN_REPORTED_AS_AN_ILLEGAL_PROGRAM_USER_AND_YOUR_CHATTING_WILL_BE_BLOCKED_FOR_10 = new SystemMessage(2473);

    public static final SystemMessage THIS_CHARACTER_CANNOT_MAKE_A_REPORT_BECAUSE_ANOTHER_CHARACTER_FROM_THIS_ACCOUNT_HAS_ALREADY_DONE = new SystemMessage(2472);

    public static final SystemMessage YOU_HAVE_BEEN_REPORTED_AS_AN_ILLEGAL_PROGRAM_USER_AND_YOUR_PARTY_PARTICIPATION_WILL_BE_BLOCKED_120 = new SystemMessage(2475);

    public static final SystemMessage YOU_HAVE_BEEN_REPORTED_AS_AN_ILLEGAL_PROGRAM_USER_AND_YOUR_PARTY_PARTICIPATION_WILL_BE_BLOCKED_60 = new SystemMessage(2474);

    public static final SystemMessage YOU_HAVE_BEEN_REPORTED_AS_AN_ILLEGAL_PROGRAM_USER_AND_YOUR_ACTIONS_WILL_BE_RESTRICTED_FOR_120 = new SystemMessage(2477);

    public static final SystemMessage YOU_HAVE_BEEN_REPORTED_AS_AN_ILLEGAL_PROGRAM_USER_AND_YOUR_PARTY_PARTICIPATION_WILL_BE_BLOCKED_180 = new SystemMessage(2476);

    public static final SystemMessage YOU_HAVE_BEEN_REPORTED_AS_AN_ILLEGAL_PROGRAM_USER_AND_YOUR_ACTIONS_WILL_BE_RESTRICTED_FOR_180_1 = new SystemMessage(2479);

    public static final SystemMessage YOU_HAVE_BEEN_REPORTED_AS_AN_ILLEGAL_PROGRAM_USER_AND_YOUR_ACTIONS_WILL_BE_RESTRICTED_FOR_180 = new SystemMessage(2478);

    public static final SystemMessage YOU_HAVE_SELECTED_A_NON_CLASS_LIMITED_TEAM_MATCH_DO_YOU_WISH_TO_PARTICIPATE = new SystemMessage(2465);

    public static final SystemMessage THE_AIRSHIP_S_FUEL_EP_HAS_RUN_OUT_THE_AIRSHIP_S_SPEED_WILL_BE_GREATLY_DECREASED_IN_THIS = new SystemMessage(2464);

    public static final SystemMessage DO_YOU_WISH_TO_BEGIN_THE_GAME_NOW = new SystemMessage(2467);

    public static final SystemMessage A_PET_ON_AUXILIARY_MODE_CANNOT_USE_SKILLS = new SystemMessage(2466);

    public static final SystemMessage YOU_HAVE_USED_ALL_AVAILABLE_POINTS_POINTS_ARE_RESET_EVERYDAY_AT_NOON = new SystemMessage(2469);

    public static final SystemMessage YOU_HAVE_USED_A_REPORT_POINT_ON_C1_YOU_HAVE_S2_POINTS_REMAINING_ON_THIS_ACCOUNT = new SystemMessage(2468);

    public static final SystemMessage THIS_CHARACTER_CANNOT_MAKE_A_REPORT_THE_TARGET_HAS_ALREADY_BEEN_REPORTED_BY_EITHER_YOUR_CLAN_OR = new SystemMessage(2471);

    public static final SystemMessage THIS_CHARACTER_CANNOT_MAKE_A_REPORT_YOU_CANNOT_MAKE_A_REPORT_WHILE_LOCATED_INSIDE_A_PEACE_ZONE = new SystemMessage(2470);

    public static final SystemMessage YOU_CANNOT_ENTER_AERIAL_CLEFT_BECAUSE_YOU_ARE_NOT_AT_THE_RIGHT_LEVEL_ENTRY_IS_POSSIBLE_ONLY = new SystemMessage(2488);

    public static final SystemMessage YOU_MUST_TARGET_A_CONTROL_DEVICE_IN_ORDER_TO_PERFORM_THIS_ACTION = new SystemMessage(2489);

    public static final SystemMessage YOU_CANNOT_PERFORM_THIS_ACTION_BECAUSE_YOU_ARE_TOO_FAR_AWAY_FROM_THE_CONTROL_DEVICE = new SystemMessage(2490);

    public static final SystemMessage YOUR_SHIP_CANNOT_TELEPORT_BECAUSE_IT_DOES_NOT_HAVE_ENOUGH_FUEL_FOR_THE_TRIP = new SystemMessage(2491);

    public static final SystemMessage THE_AIRSHIP_HAS_BEEN_SUMMONED_IT_WILL_AUTOMATICALLY_DEPART_IN_S_MINUTES = new SystemMessage(2492);

    public static final SystemMessage ENTER_CHAT_MODE_IS_AUTOMATICALLY_ENABLED_WHEN_YOU_ARE_IN_A_FLYING_TRANSFORMATION_STATE = new SystemMessage(2493);

    public static final SystemMessage ENTER_CHAT_MODE_IS_AUTOMATICALLY_ENABLED_WHEN_YOU_ARE_IN_AIRSHIP_CONTROL_STATE = new SystemMessage(2494);

    public static final SystemMessage W_GO_FORWARD_S_STOP_A_TURN_LEFT_D_TURN_RIGHT_E_INCREASE_ALTITUDE_AND_Q_DECREASE_ALTITUDE = new SystemMessage(2495);

    public static final SystemMessage YOU_HAVE_BEEN_REPORTED_AS_AN_ILLEGAL_PROGRAM_USER_AND_MOVING_WILL_BE_BLOCKED_FOR_120_MINUTES = new SystemMessage(2480);

    public static final SystemMessage C1_HAS_BEEN_REPORTED_AS_AN_ILLEGAL_PROGRAM_USER_AND_HAS_BEEN_INVESTIGATED = new SystemMessage(2481);

    public static final SystemMessage C1_HAS_BEEN_REPORTED_AS_AN_ILLEGAL_PROGRAM_USER_AND_CANNOT_JOIN_A_PARTY = new SystemMessage(2482);

    public static final SystemMessage YOU_HAVE_BEEN_REPORTED_AS_AN_ILLEGAL_PROGRAM_USER_AND_CHATTING_IS_NOT_ALLOWED = new SystemMessage(2483);

    public static final SystemMessage YOU_HAVE_BEEN_REPORTED_AS_AN_ILLEGAL_PROGRAM_USER_AND_PARTICIPATING_IN_A_PARTY_IS_NOT_ALLOWED = new SystemMessage(2484);

    public static final SystemMessage YOU_HAVE_BEEN_REPORTED_AS_AN_ILLEGAL_PROGRAM_USER_AND_YOUR_ACTIVITIES_ARE_ONLY_ALLOWED_WITHIN = new SystemMessage(2485);

    public static final SystemMessage YOU_HAVE_BEEN_BLOCKED_DUE_TO_VERIFICATION_THAT_YOU_ARE_USING_A_THIRD_PARTY_PROGRAM_SUBSEQUENT = new SystemMessage(2486);

    public static final SystemMessage YOU_HAVE_BEEN_REPORTED_AS_AN_ILLEGAL_PROGRAM_USER_AND_YOUR_CONNECTION_HAS_BEEN_ENDED_PLEASE = new SystemMessage(2487);

    public static final SystemMessage ACTIVATES_OR_DEACTIVATES_MINIMUM_FRAME_FUNCTION = new SystemMessage(2511);

    public static final SystemMessage THE_BUFF_IN_THE_PARTY_WINDOW_IS_TOGGLED_BUFF_FOR_ONE_INPUT_DEBUFF_FOR_TWO_INPUTS_A_SONG_AND = new SystemMessage(2510);

    public static final SystemMessage OPENS_THE_GM_PETITION_WINDOW = new SystemMessage(2509);

    public static final SystemMessage OPENS_THE_GM_MANAGER_WINDOW = new SystemMessage(2508);

    public static final SystemMessage CLOSES_ALL_OPEN_WINDOWS = new SystemMessage(2507);

    public static final SystemMessage TEMPORARILY_HIDES_ALL_OPEN_WINDOWS = new SystemMessage(2506);

    public static final SystemMessage OPENS_OR_CLOSES_THE_INVENTORY_WINDOW = new SystemMessage(2505);

    public static final SystemMessage FOCUS_WILL_BE_MOVED_TO_CHAT_WINDOW = new SystemMessage(2504);

    public static final SystemMessage THE_CURRENTLY_SELECTED_TARGET_WILL_BE_CANCELLED = new SystemMessage(2503);

    public static final SystemMessage YOU_WILL_BE_MOVED_TO_THE_NEXT_CHATTING_CHANNEL_TAB = new SystemMessage(2502);

    public static final SystemMessage YOU_WILL_BE_MOVED_TO_THE_PREVIOUS_CHATTING_CHANNEL_TAB = new SystemMessage(2501);

    public static final SystemMessage THE_COLLECTION_HAS_SUCCEEDED = new SystemMessage(2500);

    public static final SystemMessage YOU_CANNOT_COLLECT_BECAUSE_SOMEONE_ELSE_IS_ALREADY_COLLECTING = new SystemMessage(2499);

    public static final SystemMessage DURING_THE_AIRSHIP_CONTROL_STATE_YOU_CAN_ALSO_CHANGE_ALTITUDE_USING_THE_BUTTON_AT_THE_CENTER_OF = new SystemMessage(2498);

    public static final SystemMessage TO_CLOSE_THE_CURRENTLY_OPEN_TIP_PLEASE_CANCEL_THE_CHECKED_BOX__SYSTEM_TUTORIAL__IN_OPTIONS = new SystemMessage(2497);

    public static final SystemMessage IF_YOU_CLICK_ON_A_SKILL_DESIGNATED_ON_YOUR_SHORTCUT_BAR_THAT_SLOT_IS_ACTIVATED_ONCE_ACTIVATED = new SystemMessage(2496);

    public static final SystemMessage ASSIGN_2ND_SLOT_SHORTCUT_IN_THE_1ST_SHORTCUT_EXPANDED_WINDOW_COMBINATION_OF_CTRL_AND_SHIFT = new SystemMessage(2526);

    public static final SystemMessage ASSIGN_3RD_SLOT_SHORTCUT_IN_THE_1ST_SHORTCUT_EXPANDED_WINDOW_COMBINATION_OF_CTRL_AND_SHIFT = new SystemMessage(2527);

    public static final SystemMessage ASSIGN_12TH_SLOT_SHORTCUT_IN_THE_SHORTCUT_BASE_WINDOW_COMBINATION_OF_CTRL_AND_SHIFT_CANNOT_BE = new SystemMessage(2524);

    public static final SystemMessage ASSIGN_1ST_SLOT_SHORTCUT_IN_THE_1ST_SHORTCUT_EXPANDED_WINDOW_COMBINATION_OF_CTRL_AND_SHIFT = new SystemMessage(2525);

    public static final SystemMessage ASSIGN_10TH_SLOT_SHORTCUT_IN_THE_SHORTCUT_BASE_WINDOW_COMBINATION_OF_CTRL_AND_SHIFT_CANNOT_BE = new SystemMessage(2522);

    public static final SystemMessage ASSIGN_11TH_SLOT_SHORTCUT_IN_THE_SHORTCUT_BASE_WINDOW_COMBINATION_OF_CTRL_AND_SHIFT_CANNOT_BE = new SystemMessage(2523);

    public static final SystemMessage ASSIGN_8TH_SLOT_SHORTCUT_IN_THE_SHORTCUT_BASE_WINDOW_COMBINATION_OF_CTRL_AND_SHIFT_CANNOT_BE = new SystemMessage(2520);

    public static final SystemMessage ASSIGN_9TH_SLOT_SHORTCUT_IN_THE_SHORTCUT_BASE_WINDOW_COMBINATION_OF_CTRL_AND_SHIFT_CANNOT_BE = new SystemMessage(2521);

    public static final SystemMessage ASSIGN_6TH_SLOT_SHORTCUT_IN_THE_SHORTCUT_BASE_WINDOW_COMBINATION_OF_CTRL_AND_SHIFT_CANNOT_BE = new SystemMessage(2518);

    public static final SystemMessage ASSIGN_7TH_SLOT_SHORTCUT_IN_THE_SHORTCUT_BASE_WINDOW_COMBINATION_OF_CTRL_AND_SHIFT_CANNOT_BE = new SystemMessage(2519);

    public static final SystemMessage ASSIGN_4TH_SLOT_SHORTCUT_IN_THE_SHORTCUT_BASE_WINDOW_COMBINATION_OF_CTRL_AND_SHIFT_CANNOT_BE = new SystemMessage(2516);

    public static final SystemMessage ASSIGN_5TH_SLOT_SHORTCUT_IN_THE_SHORTCUT_BASE_WINDOW_COMBINATION_OF_CTRL_AND_SHIFT_CANNOT_BE = new SystemMessage(2517);

    public static final SystemMessage ASSIGN_2ND_SLOT_SHORTCUT_IN_THE_SHORTCUT_BASE_WINDOW_COMBINATION_OF_CTRL_AND_SHIFT_CANNOT_BE = new SystemMessage(2514);

    public static final SystemMessage ASSIGN_3RD_SLOT_SHORTCUT_IN_THE_SHORTCUT_BASE_WINDOW_COMBINATION_OF_CTRL_AND_SHIFT_CANNOT_BE = new SystemMessage(2515);

    public static final SystemMessage RUNS_OR_CLOSES_THE_MSN_MESSENGER_WINDOW = new SystemMessage(2512);

    public static final SystemMessage ASSIGN_1ST_SLOT_SHORTCUT_IN_THE_SHORTCUT_BASE_WINDOW_COMBINATION_OF_CTRL_AND_SHIFT_CANNOT_BE = new SystemMessage(2513);

    public static final SystemMessage ASSIGN_5TH_SLOT_SHORTCUT_IN_THE_2ND_SHORTCUT_EXPANDED_WINDOW_COMBINATION_OF_CTRL_AND_SHIFT = new SystemMessage(2541);

    public static final SystemMessage ASSIGN_4TH_SLOT_SHORTCUT_IN_THE_2ND_SHORTCUT_EXPANDED_WINDOW_COMBINATION_OF_CTRL_AND_SHIFT = new SystemMessage(2540);

    public static final SystemMessage ASSIGN_7TH_SLOT_SHORTCUT_IN_THE_2ND_SHORTCUT_EXPANDED_WINDOW_COMBINATION_OF_CTRL_AND_SHIFT = new SystemMessage(2543);

    public static final SystemMessage ASSIGN_6TH_SLOT_SHORTCUT_IN_THE_2ND_SHORTCUT_EXPANDED_WINDOW_COMBINATION_OF_CTRL_AND_SHIFT = new SystemMessage(2542);

    public static final SystemMessage ASSIGN_1ST_SLOT_SHORTCUT_IN_THE_2ND_SHORTCUT_EXPANDED_WINDOW_COMBINATION_OF_CTRL_AND_SHIFT = new SystemMessage(2537);

    public static final SystemMessage ASSIGN_12TH_SLOT_SHORTCUT_IN_THE_1ST_SHORTCUT_EXPANDED_WINDOW_COMBINATION_OF_CTRL_AND_SHIFT = new SystemMessage(2536);

    public static final SystemMessage ASSIGN_3RD_SLOT_SHORTCUT_IN_THE_2ND_SHORTCUT_EXPANDED_WINDOW_COMBINATION_OF_CTRL_AND_SHIFT = new SystemMessage(2539);

    public static final SystemMessage ASSIGN_2ND_SLOT_SHORTCUT_IN_THE_2ND_SHORTCUT_EXPANDED_WINDOW_COMBINATION_OF_CTRL_AND_SHIFT = new SystemMessage(2538);

    public static final SystemMessage ASSIGN_9TH_SLOT_SHORTCUT_IN_THE_1ST_SHORTCUT_EXPANDED_WINDOW_COMBINATION_OF_CTRL_AND_SHIFT = new SystemMessage(2533);

    public static final SystemMessage ASSIGN_8TH_SLOT_SHORTCUT_IN_THE_1ST_SHORTCUT_EXPANDED_WINDOW_COMBINATION_OF_CTRL_AND_SHIFT = new SystemMessage(2532);

    public static final SystemMessage ASSIGN_11TH_SLOT_SHORTCUT_IN_THE_1ST_SHORTCUT_EXPANDED_WINDOW_COMBINATION_OF_CTRL_AND_SHIFT = new SystemMessage(2535);

    public static final SystemMessage ASSIGN_10TH_SLOT_SHORTCUT_IN_THE_1ST_SHORTCUT_EXPANDED_WINDOW_COMBINATION_OF_CTRL_AND_SHIFT = new SystemMessage(2534);

    public static final SystemMessage ASSIGN_5TH_SLOT_SHORTCUT_IN_THE_1ST_SHORTCUT_EXPANDED_WINDOW_COMBINATION_OF_CTRL_AND_SHIFT = new SystemMessage(2529);

    public static final SystemMessage ASSIGN_4TH_SLOT_SHORTCUT_IN_THE_1ST_SHORTCUT_EXPANDED_WINDOW_COMBINATION_OF_CTRL_AND_SHIFT = new SystemMessage(2528);

    public static final SystemMessage ASSIGN_7TH_SLOT_SHORTCUT_IN_THE_1ST_SHORTCUT_EXPANDED_WINDOW_COMBINATION_OF_CTRL_AND_SHIFT = new SystemMessage(2531);

    public static final SystemMessage ASSIGN_6TH_SLOT_SHORTCUT_IN_THE_1ST_SHORTCUT_EXPANDED_WINDOW_COMBINATION_OF_CTRL_AND_SHIFT = new SystemMessage(2530);

    public static final SystemMessage MOVE_THE_SHORTCUT_PAGE_IN_THE_SHORTCUT_BASE_WINDOW_TO_PAGE_8 = new SystemMessage(2556);

    public static final SystemMessage MOVE_THE_SHORTCUT_PAGE_IN_THE_SHORTCUT_BASE_WINDOW_TO_PAGE_9 = new SystemMessage(2557);

    public static final SystemMessage MOVE_THE_SHORTCUT_PAGE_IN_THE_SHORTCUT_BASE_WINDOW_TO_PAGE_10 = new SystemMessage(2558);

    public static final SystemMessage OPENS_AND_CLOSES_THE_ACTION_WINDOW_EXECUTING_CHARACTER_ACTIONS_AND_GAME_COMMANDS = new SystemMessage(2559);

    public static final SystemMessage MOVE_THE_SHORTCUT_PAGE_IN_THE_SHORTCUT_BASE_WINDOW_TO_PAGE_4 = new SystemMessage(2552);

    public static final SystemMessage MOVE_THE_SHORTCUT_PAGE_IN_THE_SHORTCUT_BASE_WINDOW_TO_PAGE_5 = new SystemMessage(2553);

    public static final SystemMessage MOVE_THE_SHORTCUT_PAGE_IN_THE_SHORTCUT_BASE_WINDOW_TO_PAGE_6 = new SystemMessage(2554);

    public static final SystemMessage MOVE_THE_SHORTCUT_PAGE_IN_THE_SHORTCUT_BASE_WINDOW_TO_PAGE_7 = new SystemMessage(2555);

    public static final SystemMessage ASSIGN_12TH_SLOT_SHORTCUT_IN_THE_2ND_SHORTCUT_EXPANDED_WINDOW_COMBINATION_OF_CTRL_AND_SHIFT = new SystemMessage(2548);

    public static final SystemMessage MOVE_THE_SHORTCUT_PAGE_IN_THE_SHORTCUT_BASE_WINDOW_TO_PAGE_1 = new SystemMessage(2549);

    public static final SystemMessage MOVE_THE_SHORTCUT_PAGE_IN_THE_SHORTCUT_BASE_WINDOW_TO_PAGE_2 = new SystemMessage(2550);

    public static final SystemMessage MOVE_THE_SHORTCUT_PAGE_IN_THE_SHORTCUT_BASE_WINDOW_TO_PAGE_3 = new SystemMessage(2551);

    public static final SystemMessage ASSIGN_8TH_SLOT_SHORTCUT_IN_THE_2ND_SHORTCUT_EXPANDED_WINDOW_COMBINATION_OF_CTRL_AND_SHIFT = new SystemMessage(2544);

    public static final SystemMessage ASSIGN_9TH_SLOT_SHORTCUT_IN_THE_2ND_SHORTCUT_EXPANDED_WINDOW_COMBINATION_OF_CTRL_AND_SHIFT = new SystemMessage(2545);

    public static final SystemMessage ASSIGN_10TH_SLOT_SHORTCUT_IN_THE_2ND_SHORTCUT_EXPANDED_WINDOW_COMBINATION_OF_CTRL_AND_SHIFT = new SystemMessage(2546);

    public static final SystemMessage ASSIGN_11TH_SLOT_SHORTCUT_IN_THE_2ND_SHORTCUT_EXPANDED_WINDOW_COMBINATION_OF_CTRL_AND_SHIFT = new SystemMessage(2547);

    public static final SystemMessage RESURRECTION_IS_POSSIBLE_BECAUSE_OF_THE_COURAGE_CHARM_S_EFFECT_WOULD_YOU_LIKE_TO_RESURRECT_NOW = new SystemMessage(2306);

    public static final SystemMessage THE_TARGET_IS_RECEIVING_THE_COURAGE_CHARM_S_EFFECT = new SystemMessage(2307);

    public static final SystemMessage THERE_ARE_S2_MINUTES_S3_SECONDS_REMAINING_IN_S1S_REUSE_TIME = new SystemMessage(2304);

    public static final SystemMessage THERE_ARE_S2_HOURS_S3_MINUTES_AND_S4_SECONDS_REMAINING_IN_S1S_REUSE_TIME = new SystemMessage(2305);

    public static final SystemMessage REMAINING_TIME__S1_MINUTES = new SystemMessage(2310);

    public static final SystemMessage YOU_DO_NOT_HAVE_A_SERVITOR = new SystemMessage(2311);

    public static final SystemMessage REMAINING_TIME__S1_DAYS = new SystemMessage(2308);

    public static final SystemMessage REMAINING_TIME__S1_HOURS = new SystemMessage(2309);

    public static final SystemMessage YOUR_VITALITY_IS_AT_MAXIMUM = new SystemMessage(2314);

    public static final SystemMessage VITALITY_HAS_INCREASED = new SystemMessage(2315);

    public static final SystemMessage YOU_DO_NOT_HAVE_A_PET = new SystemMessage(2312);

    public static final SystemMessage THE_VITAMIN_ITEM_HAS_ARRIVED = new SystemMessage(2313);

    public static final SystemMessage ONLY_AN_ENHANCED_SKILL_CAN_BE_CANCELLED = new SystemMessage(2318);

    public static final SystemMessage YOU_HAVE_ACQUIRED_S1_REPUTATION_SCORE = new SystemMessage(2319);

    public static final SystemMessage VITALITY_HAS_DECREASED = new SystemMessage(2316);

    public static final SystemMessage VITALITY_IS_FULLY_EXHAUSTED = new SystemMessage(2317);

    public static final SystemMessage CURRENT_LOCATION__INSIDE_RIM_KAMALOKA = new SystemMessage(2323);

    public static final SystemMessage CURRENT_LOCATION__INSIDE_NIA_KAMALOKA = new SystemMessage(2322);

    public static final SystemMessage CURRENT_LOCATION__INSIDE_KAMALOKA = new SystemMessage(2321);

    public static final SystemMessage MASTERWORK_POSSIBLE = new SystemMessage(2320);

    public static final SystemMessage NOT_ENOUGH_FAME_POINTS = new SystemMessage(2327);

    public static final SystemMessage ACQUIRED_50_CLAN_FAME_POINTS = new SystemMessage(2326);

    public static final SystemMessage ANOTHER_TELEPORT_IS_TAKING_PLACE_PLEASE_TRY_AGAIN_ONCE_THE_TELEPORT_IN_PROCESS_ENDS = new SystemMessage(2325);

    public static final SystemMessage C1_YOU_CANNOT_ENTER_BECAUSE_YOU_HAVE_INSUFFICIENT_PC_CAFE_POINTS = new SystemMessage(2324);

    public static final SystemMessage RARE_S1 = new SystemMessage(2331);

    public static final SystemMessage __EXPERIENCE_POINTS_BOOSTED_BY_S1 = new SystemMessage(2330);

    public static final SystemMessage VITALITY_LEVEL_S1_S2 = new SystemMessage(2329);

    public static final SystemMessage CLANS_OF_LEVEL_4_OR_ABOVE_CAN_REGISTER_FOR_HIDEAWAY_WARS_FOR_DEVASTATED_CASTLE_AND_FORTRESS_OF = new SystemMessage(2328);

    public static final SystemMessage THERE_ARE_NO_MORE_VITAMIN_ITEMS_TO_BE_FOUND = new SystemMessage(2335);

    public static final SystemMessage SCORE_THAT_SHOWS_A_PLAYER_S_INDIVIDUAL_FAME_FAME_CAN_BE_OBTAINED_BY_PARTICIPATING_IN_A_TERRITORY = new SystemMessage(2334);

    public static final SystemMessage YOU_CANNOT_RECEIVE_THE_VITAMIN_ITEM_BECAUSE_YOU_HAVE_EXCEED_YOUR_INVENTORY_WEIGHT_QUANTITY_LIMIT = new SystemMessage(2333);

    public static final SystemMessage SUPPLY_S1 = new SystemMessage(2332);

    public static final SystemMessage HALF_KILL = new SystemMessage(2336);

    public static final SystemMessage CP_DISAPPEARS_WHEN_HIT_WITH_A_HALF_KILL_SKILL = new SystemMessage(2337);

    public static final SystemMessage IF_IT_S_A_DRAW_THE_PLAYER_WHO_FIRST_ENTERED_IS_FIRST = new SystemMessage(2338);

    public static final SystemMessage PLEASE_PLACE_THE_ITEM_TO_BE_ENCHANTED = new SystemMessage(2339);

    public static final SystemMessage PLEASE_PLACE_THE_ITEM_FOR_RATE_INCREASE = new SystemMessage(2340);

    public static final SystemMessage THE_ENCHANT_WILL_BEGIN_ONCE_YOU_PRESS_THE_START_BUTTON_BELOW = new SystemMessage(2341);

    public static final SystemMessage SUCCESS_THE_ITEM_IS_NOW_A_S1 = new SystemMessage(2342);

    public static final SystemMessage FAILED_YOU_HAVE_OBTAINED_S2_OF_S1 = new SystemMessage(2343);

    public static final SystemMessage YOU_HAVE_BEEN_KILLED_BY_AN_ATTACK_FROM_C1 = new SystemMessage(2344);

    public static final SystemMessage YOU_HAVE_ATTACKED_AND_KILLED_C1 = new SystemMessage(2345);

    public static final SystemMessage YOUR_ACCOUNT_IS_TEMPORARILY_LIMITED_BECAUSE_YOUR_GAME_USE_GOAL_IS_PRESUMED_TO_BE_EMBEZZLEMENT_OF = new SystemMessage(2346);

    public static final SystemMessage S1_SECONDS_TO_GAME_END = new SystemMessage(2347);

    public static final SystemMessage YOU_CANNOT_USE_MY_TELEPORTS_DURING_A_BATTLE = new SystemMessage(2348);

    public static final SystemMessage YOU_CANNOT_USE_MY_TELEPORTS_WHILE_PARTICIPATING_A_LARGE_SCALE_BATTLE_SUCH_AS_A_CASTLE_SIEGE = new SystemMessage(2349);

    public static final SystemMessage YOU_CANNOT_USE_MY_TELEPORTS_DURING_A_DUEL = new SystemMessage(2350);

    public static final SystemMessage YOU_CANNOT_USE_MY_TELEPORTS_WHILE_FLYING = new SystemMessage(2351);

    public static final SystemMessage YOU_CANNOT_USE_MY_TELEPORTS_WHILE_YOU_ARE_IN_A_FLINT_OR_PARALYZED_STATE = new SystemMessage(2353);

    public static final SystemMessage YOU_CANNOT_USE_MY_TELEPORTS_WHILE_PARTICIPATING_IN_AN_OLYMPIAD_MATCH = new SystemMessage(2352);

    public static final SystemMessage YOU_CANNOT_USE_MY_TELEPORTS_IN_THIS_AREA = new SystemMessage(2355);

    public static final SystemMessage YOU_CANNOT_USE_MY_TELEPORTS_WHILE_YOU_ARE_DEAD = new SystemMessage(2354);

    public static final SystemMessage YOU_CANNOT_USE_MY_TELEPORTS_IN_AN_INSTANT_ZONE = new SystemMessage(2357);

    public static final SystemMessage YOU_CANNOT_USE_MY_TELEPORTS_UNDERWATER = new SystemMessage(2356);

    public static final SystemMessage YOU_CANNOT_TELEPORT_BECAUSE_YOU_DO_NOT_HAVE_A_TELEPORT_ITEM = new SystemMessage(2359);

    public static final SystemMessage YOU_HAVE_NO_SPACE_TO_SAVE_THE_TELEPORT_LOCATION = new SystemMessage(2358);

    public static final SystemMessage CURRENT_LOCATION__S1 = new SystemMessage(2361);

    public static final SystemMessage MY_TELEPORTS_SPELLBK__S1 = new SystemMessage(2360);

    public static final SystemMessage YOUR_ACCOUNT_HAS_BEEN_CONFIRMED_AS_USING_ANOTHER_PERSON_S_NAME_ALL_GAME_SERVICES_HAVE_BEEN = new SystemMessage(2363);

    public static final SystemMessage THE_SAVED_TELEPORT_LOCATION_WILL_BE_DELETED_DO_YOU_WISH_TO_CONTINUE = new SystemMessage(2362);

    public static final SystemMessage THE_DESIGNATED_ITEM_HAS_EXPIRED_AFTER_ITS_S1_PERIOD = new SystemMessage(2365);

    public static final SystemMessage THE_ITEM_HAS_EXPIRED_AFTER_ITS_S1_PERIOD = new SystemMessage(2364);

    public static final SystemMessage S1_S_BLESSING_HAS_RECOVERED_HP_BY_S2 = new SystemMessage(2367);

    public static final SystemMessage THE_LIMITED_TIME_ITEM_HAS_BEEN_DELETED = new SystemMessage(2366);

    public static final SystemMessage END_MATCH = new SystemMessage(2374);

    public static final SystemMessage THE_HUNTING_HELPER_PET_CANNOT_BE_RETURNED_BECAUSE_THERE_IS_NOT_MUCH_TIME_REMAINING_UNTIL_IT = new SystemMessage(2375);

    public static final SystemMessage THERE_IS_NOT_MUCH_TIME_REMAINING_UNTIL_THE_HUNTING_HELPER_PET_LEAVES = new SystemMessage(2372);

    public static final SystemMessage THE_HUNTING_HELPER_PET_IS_NOW_LEAVING = new SystemMessage(2373);

    public static final SystemMessage RESURRECTION_WILL_TAKE_PLACE_IN_THE_WAITING_ROOM_AFTER_S1_SECONDS = new SystemMessage(2370);

    public static final SystemMessage C1_WAS_REPORTED_AS_A_BOT = new SystemMessage(2371);

    public static final SystemMessage S1_S_BLESSING_HAS_RECOVERED_MP_BY_S2 = new SystemMessage(2368);

    public static final SystemMessage S1_S_BLESSING_HAS_FULLY_RECOVERED_HP_AND_MP = new SystemMessage(2369);

    public static final SystemMessage YOU_CANNOT_REPORT_THIS_PERSON_AGAIN_AT_THIS_TIME___CLAN = new SystemMessage(2382);

    public static final SystemMessage YOU_CANNOT_REPORT_THIS_PERSON_AGAIN_AT_THIS_TIME___IP = new SystemMessage(2383);

    public static final SystemMessage YOU_CANNOT_REPORT_THIS_PERSON_AGAIN_AT_THIS_TIME___CHARACTER = new SystemMessage(2380);

    public static final SystemMessage YOU_CANNOT_REPORT_THIS_PERSON_AGAIN_AT_THIS_TIME___ACCOUNT = new SystemMessage(2381);

    public static final SystemMessage YOU_CANNOT_REPORT_WHEN_A_CLAN_WAR_HAS_BEEN_DECLARED = new SystemMessage(2378);

    public static final SystemMessage YOU_CANNOT_REPORT_A_CHARACTER_WHO_HAS_NOT_ACQUIRED_ANY_EXP_AFTER_CONNECTING = new SystemMessage(2379);

    public static final SystemMessage YOU_CANNOT_RECEIVE_A_VITAMIN_ITEM_DURING_AN_EXCHANGE = new SystemMessage(2376);

    public static final SystemMessage YOU_CANNOT_REPORT_A_CHARACTER_WHO_IS_IN_A_PEACE_ZONE_OR_A_BATTLEFIELD = new SystemMessage(2377);

    public static final SystemMessage YOU_HAVE_USED_THE_FEATHER_OF_BLESSING_TO_RESURRECT = new SystemMessage(2391);

    public static final SystemMessage YOUR_NUMBER_OF_MY_TELEPORTS_SLOTS_HAS_REACHED_ITS_MAXIMUM_LIMIT = new SystemMessage(2390);

    public static final SystemMessage THE_MAXIMUM_ACCUMULATION_ALLOWED_OF_PC_CAFE_POINTS_HAS_BEEN_EXCEEDED_YOU_CAN_NO_LONGER_ACQUIRE = new SystemMessage(2389);

    public static final SystemMessage A_PARTY_CANNOT_BE_FORMED_IN_THIS_AREA = new SystemMessage(2388);

    public static final SystemMessage REGISTRATION_OF_THE_SUPPORT_ENHANCEMENT_SPELLBOOK_HAS_FAILED = new SystemMessage(2387);

    public static final SystemMessage THIS_ITEM_DOES_NOT_MEET_THE_REQUIREMENTS_FOR_THE_SUPPORT_ENHANCEMENT_SPELLBOOK = new SystemMessage(2386);

    public static final SystemMessage THIS_IS_AN_INCORRECT_SUPPORT_ENHANCEMENT_SPELLBOOK = new SystemMessage(2385);

    public static final SystemMessage THIS_ITEM_DOES_NOT_MEET_THE_REQUIREMENTS_FOR_THE_ENHANCEMENT_SPELLBOOK = new SystemMessage(2384);

    public static final SystemMessage S1_S_OWNERSHIP_EXPIRES_IN_S2_MINUTES = new SystemMessage(2399);

    public static final SystemMessage YOU_HAVE_NO_OPEN_MY_TELEPORTS_SLOTS = new SystemMessage(2398);

    public static final SystemMessage PLEASE_USE_A_MY_TELEPORT_SCROLL = new SystemMessage(2397);

    public static final SystemMessage THAT_PET_SERVITOR_SKILL_CANNOT_BE_USED_BECAUSE_IT_IS_RECHARGING = new SystemMessage(2396);

    public static final SystemMessage THAT_SKILL_CANNOT_BE_USED_BECAUSE_YOUR_PET_SERVITOR_LACKS_SUFFICIENT_HP = new SystemMessage(2395);

    public static final SystemMessage THAT_SKILL_CANNOT_BE_USED_BECAUSE_YOUR_PET_SERVITOR_LACKS_SUFFICIENT_MP = new SystemMessage(2394);

    public static final SystemMessage YOU_HAVE_ACQUIRED_S1_PC_CAFE_POINTS = new SystemMessage(2393);

    public static final SystemMessage THE_VITAMIN_ITEM_CANNOT_BE_LOCATED_BECAUSE_OF_A_TEMPORARY_CONNECTION_ERROR = new SystemMessage(2392);

    public static final SystemMessage TERRITORY_WAR_BEGINS_IN_5_MINUTES = new SystemMessage(2404);

    public static final SystemMessage TERRITORY_WAR_BEGINS_IN_1_MINUTE = new SystemMessage(2405);

    public static final SystemMessage S1_S_TERRITORY_WAR_HAS_BEGUN = new SystemMessage(2406);

    public static final SystemMessage S1_S_TERRITORY_WAR_HAS_ENDED = new SystemMessage(2407);

    public static final SystemMessage INSTANT_ZONE_CURRENTLY_IN_USE__S1 = new SystemMessage(2400);

    public static final SystemMessage CLAN_LORD_C2_WHO_LEADS_CLAN_S1_HAS_BEEN_DECLARED_THE_LORD_OF_THE_S3_TERRITORY = new SystemMessage(2401);

    public static final SystemMessage THE_TERRITORY_WAR_REQUEST_PERIOD_HAS_ENDED = new SystemMessage(2402);

    public static final SystemMessage TERRITORY_WAR_BEGINS_IN_10_MINUTES = new SystemMessage(2403);

    public static final SystemMessage THE_BIRTHDAY_GIFT_HAS_BEEN_DELIVERED_VISIT_THE_VITAMIN_MANAGER_IN_ANY_VILLAGE_TO_OBTAIN_IT = new SystemMessage(2412);

    public static final SystemMessage YOU_ARE_REGISTERING_AS_A_RESERVE_ON_THE_AERIAL_CLEFT_RED_TEAM_S_BATTLEFIELD_DO_YOU_WISH_TO = new SystemMessage(2413);

    public static final SystemMessage YOU_ARE_REGISTERING_AS_A_RESERVE_ON_THE_AERIAL_CLEFT_BLUE_TEAM_S_BATTLEFIELD_DO_YOU_WISH_TO = new SystemMessage(2414);

    public static final SystemMessage YOU_HAVE_REGISTERED_AS_A_RESERVE_ON_THE_AERIAL_CLEFT_RED_TEAM_S_BATTLEFIELD_WHEN_IN_BATTLE_THE = new SystemMessage(2415);

    public static final SystemMessage YOU_HAVE_REGISTERED_ON_THE_WAITING_LIST_FOR_THE_NON_CLASS_LIMITED_TEAM_MATCH_EVENT = new SystemMessage(2408);

    public static final SystemMessage THE_NUMBER_OF_MY_TELEPORTS_SLOTS_HAS_BEEN_INCREASED = new SystemMessage(2409);

    public static final SystemMessage YOU_CANNOT_USE_MY_TELEPORTS_TO_REACH_THIS_AREA = new SystemMessage(2410);

    public static final SystemMessage C1_HAS_ISSUED_A_PARTY_INVITATION_WHICH_YOU_AUTOMATICALLY_REJECTED_TO_RECEIVE_PARTY_INVITATIONS = new SystemMessage(2411);

    public static final SystemMessage THE_BATTLEFIELD_CLOSES_IN_10_SECONDS = new SystemMessage(2421);

    public static final SystemMessage THE_BATTLEFIELD_CLOSES_IN_1_MINUTE = new SystemMessage(2420);

    public static final SystemMessage EP_CAN_BE_REFILLED_BY_USING_A_S1_WHILE_SAILING_ON_AN_AIRSHIP = new SystemMessage(2423);

    public static final SystemMessage EP_OR_ENERGY_POINTS_REFERS_TO_FUEL = new SystemMessage(2422);

    public static final SystemMessage YOU_ARE_CANCELING_THE_AERIAL_CLEFT_BATTLEFIELD_REGISTRATION_DO_YOU_WISH_TO_CONTINUE = new SystemMessage(2417);

    public static final SystemMessage YOU_HAVE_REGISTERED_AS_A_RESERVE_ON_THE_AERIAL_CLEFT_BLUE_TEAM_S_BATTLEFIELD_WHEN_IN_BATTLE_THE = new SystemMessage(2416);

    public static final SystemMessage THE_AERIAL_CLEFT_BATTLEFIELD_HAS_BEEN_ACTIVATED_FLIGHT_TRANSFORMATION_WILL_BE_POSSIBLE_IN = new SystemMessage(2419);

    public static final SystemMessage THE_AERIAL_CLEFT_BATTLEFIELD_REGISTRATION_HAS_BEEN_CANCELED = new SystemMessage(2418);

    public static final SystemMessage C1_HAS_BEEN_DESIGNATED_AS_THE_TARGET = new SystemMessage(2429);

    public static final SystemMessage THE_BLUE_TEAM_IS_VICTORIOUS = new SystemMessage(2428);

    public static final SystemMessage C2_HAS_FALLEN_THE_BLUE_TEAM_S_POINTS_HAVE_INCREASED = new SystemMessage(2431);

    public static final SystemMessage C1_HAS_FALLEN_THE_RED_TEAM_S_POINTS_HAVE_INCREASED = new SystemMessage(2430);

    public static final SystemMessage THE_AERIAL_CLEFT_BATTLEFIELD_HAS_BEEN_CLOSED = new SystemMessage(2425);

    public static final SystemMessage THE_COLLECTION_HAS_FAILED = new SystemMessage(2424);

    public static final SystemMessage THE_RED_TEAM_IS_VICTORIOUS = new SystemMessage(2427);

    public static final SystemMessage C1_HAS_BEEN_EXPELLED_FROM_THE_TEAM = new SystemMessage(2426);

    public static final SystemMessage THE_S1_WARD_HAS_BEEN_DESTROYED_C2_NOW_HAS_THE_TERRITORY_WARD = new SystemMessage(2750);

    public static final SystemMessage THE_CHARACTER_THAT_ACQUIRED_S1_WARD_HAS_BEEN_KILLED = new SystemMessage(2751);

    public static final SystemMessage YOU_HAVE_BEEN_REPORTED_AS_AN_ILLEGAL_PROGRAM_USER_AND_CANNOT_REPORT_OTHER_USERS = new SystemMessage(2748);

    public static final SystemMessage YOU_HAVE_REACHED_YOUR_CRYSTALLIZATION_LIMIT_AND_CANNOT_CRYSTALLIZE_ANY_MORE = new SystemMessage(2749);

    public static final SystemMessage USE_THE__DEPART__ACTION_TO_MAKE_THE_AIRSHIP_DEPART = new SystemMessage(2746);

    public static final SystemMessage AIRSHIP_TELEPORT_IS_POSSIBLE_THROUGH_THE__DEPART__ACTION_AND_IN_THAT_CASE_FUEL_EP_IS_CONSUMED = new SystemMessage(2747);

    public static final SystemMessage IF_YOU_PRESS_THE__CONTROL_CANCEL__ACTION_BUTTON_YOU_CAN_EXIT_THE_CONTROL_STATE_AT_ANY_TIME = new SystemMessage(2744);

    public static final SystemMessage THE__MOUNT_CANCEL__ACTION_BUTTON_ALLOWS_YOU_TO_DISMOUNT_BEFORE_THE_AIRSHIP_DEPARTS = new SystemMessage(2745);

    public static final SystemMessage ANY_CHARACTER_RIDING_THE_AIRSHIP_CAN_CONTROL_IT = new SystemMessage(2742);

    public static final SystemMessage IF_YOU_RESTART_WHILE_ON_AN_AIRSHIP_YOU_WILL_RETURN_TO_THE_DEPARTURE_LOCATION = new SystemMessage(2743);

    public static final SystemMessage THIS_ACTION_IS_PROHIBITED_WHILE_CONTROLLING = new SystemMessage(2740);

    public static final SystemMessage YOU_CAN_CONTROL_THE_AIRSHIP_BY_TARGETING_THE_AIRSHIP_S_CONTROL_KEY_AND_PRESSING_THE__CONTROL_ = new SystemMessage(2741);

    public static final SystemMessage YOU_CANNOT_CONTROL_THE_TARGET_WHILE_HOLDING_A_FLAG = new SystemMessage(2738);

    public static final SystemMessage YOU_CANNOT_CONTROL_BECAUSE_YOU_DO_NOT_MEET_THE_REQUIREMENTS = new SystemMessage(2739);

    public static final SystemMessage YOU_CANNOT_CONTROL_THE_TARGET_WHILE_USING_A_SKILL = new SystemMessage(2736);

    public static final SystemMessage YOU_CANNOT_CONTROL_THE_TARGET_WHILE_A_CURSED_WEAPON_IS_EQUIPPED = new SystemMessage(2737);

    public static final SystemMessage YOU_CANNOT_CONTROL_THE_TARGET_WHILE_IN_A_SITTING_POSITION = new SystemMessage(2735);

    public static final SystemMessage YOU_CANNOT_CONTROL_THE_TARGET_WHILE_IN_A_DUEL = new SystemMessage(2734);

    public static final SystemMessage YOU_CANNOT_CONTROL_THE_TARGET_WHILE_IN_A_BATTLE = new SystemMessage(2733);

    public static final SystemMessage YOU_CANNOT_CONTROL_THE_TARGET_WHILE_FISHING = new SystemMessage(2732);

    public static final SystemMessage YOU_CANNOT_CONTROL_THE_TARGET_WHEN_YOU_ARE_DEAD = new SystemMessage(2731);

    public static final SystemMessage YOU_CANNOT_CONTROL_THE_TARGET_WHILE_YOU_ARE_PETRIFIED = new SystemMessage(2730);

    public static final SystemMessage YOU_CANNOT_CONTROL_THE_TARGET_WHILE_TRANSFORMED = new SystemMessage(2729);

    public static final SystemMessage THIS_ACTION_IS_PROHIBITED_WHILE_MOUNTED = new SystemMessage(2728);

    public static final SystemMessage YOU_CANNOT_MOUNT_BECAUSE_YOU_DO_NOT_MEET_THE_REQUIREMENTS = new SystemMessage(2727);

    public static final SystemMessage YOU_CANNOT_PURCHASE_THE_AIRSHIP_BECAUSE_YOU_DO_NOT_MEET_THE_REQUIREMENTS = new SystemMessage(2726);

    public static final SystemMessage YOU_CANNOT_SUMMON_THE_AIRSHIP_BECAUSE_YOU_DO_NOT_MEET_THE_REQUIREMENTS = new SystemMessage(2725);

    public static final SystemMessage THE_AIRSHIP_CANNOT_BE_PURCHASED_BECAUSE_YOU_DONT_HAVE_ENOUGH_S1 = new SystemMessage(2724);

    public static final SystemMessage THE_AIRSHIP_CANNOT_BE_SUMMONED_BECAUSE_YOU_DONT_HAVE_ENOUGH_S1 = new SystemMessage(2723);

    public static final SystemMessage ANOTHER_AIRSHIP_HAS_ALREADY_BEEN_SUMMONED_AT_THE_WHARF_PLEASE_TRY_AGAIN_LATER = new SystemMessage(2722);

    public static final SystemMessage BOARDING_OR_CANCELLATION_OF_BOARDING_ON_AIRSHIPS_IS_NOT_ALLOWED_IN_THE_CURRENT_AREA = new SystemMessage(2721);

    public static final SystemMessage INSTANT_ZONE_FROM_HERE__S1_S_ENTRY_HAS_BEEN_RESTRICTED_YOU_CAN_CHECK_THE_NEXT_ENTRY_POSSIBLE = new SystemMessage(2720);

    public static final SystemMessage CURRENT_LOCATION__S1_S2_S3_INSIDE_AERIAL_CLEFT = new SystemMessage(2716);

    public static final SystemMessage THE_AIRSHIP_WILL_LAND_AT_THE_WHARF_SHORTLY = new SystemMessage(2717);

    public static final SystemMessage THE_SKILL_CANNOT_BE_USED_BECAUSE_THE_TARGET_S_LOCATION_IS_TOO_HIGH_OR_LOW = new SystemMessage(2718);

    public static final SystemMessage ONLY_NON_COMPRESSED_256_COLOR_BMP_BITMAP_FILES_CAN_BE_REGISTERED = new SystemMessage(2719);

    public static final SystemMessage CURRENT_LOCATION__S1_S2_S3_OUTSIDE_THE_SEED_OF_INFINITY = new SystemMessage(2712);

    public static final SystemMessage ______________________________________________________ = new SystemMessage(2713);

    public static final SystemMessage ______________________________________________________________________ = new SystemMessage(2714);

    public static final SystemMessage AIRSHIPS_CANNOT_BE_BOARDED_IN_THE_CURRENT_AREA = new SystemMessage(2715);

    public static final SystemMessage YOU_CANNOT_REGISTER_WHILE_POSSESSING_A_CURSED_WEAPON = new SystemMessage(2708);

    public static final SystemMessage APPLICANTS_FOR_THE_OLYMPIAD_UNDERGROUND_COLISEUM_OR_KRATEI_S_CUBE_MATCHES_CANNOT_REGISTER = new SystemMessage(2709);

    public static final SystemMessage CURRENT_LOCATION__S1_S2_S3_NEAR_THE_KEUCEREUS_CLAN_ASSOCIATION_LOCATION = new SystemMessage(2710);

    public static final SystemMessage CURRENT_LOCATION__S1_S2_S3_INSIDE_THE_SEED_OF_INFINITY = new SystemMessage(2711);

    public static final SystemMessage YOU_CANNOT_REGISTER_BECAUSE_CAPACITY_HAS_BEEN_EXCEEDED = new SystemMessage(2704);

    public static final SystemMessage THE_MATCH_WAITING_TIME_WAS_EXTENDED_BY_1_MINUTE = new SystemMessage(2705);

    public static final SystemMessage YOU_CANNOT_ENTER_BECAUSE_YOU_DO_NOT_MEET_THE_REQUIREMENTS = new SystemMessage(2706);

    public static final SystemMessage YOU_CANNOT_MAKE_ANOTHER_REQUEST_FOR_10_SECONDS_AFTER_CANCELLING_A_MATCH_REGISTRATION = new SystemMessage(2707);

    public static final SystemMessage THE_MATCH_IS_BEING_PREPARED_PLEASE_TRY_AGAIN_LATER = new SystemMessage(2701);

    public static final SystemMessage THE_TEAM_WAS_ADJUSTED_BECAUSE_THE_POPULATION_RATIO_WAS_NOT_CORRECT = new SystemMessage(2703);

    public static final SystemMessage YOU_WERE_EXCLUDED_FROM_THE_MATCH_BECAUSE_THE_REGISTRATION_COUNT_WAS_NOT_CORRECT = new SystemMessage(2702);

    public static final SystemMessage NO_TRANSLATION_REQUIRED_2810 = new SystemMessage(2810);

    public static final SystemMessage NO_TRANSLATION_REQUIRED_2811 = new SystemMessage(2811);

    public static final SystemMessage NO_TRANSLATION_REQUIRED_2808 = new SystemMessage(2808);

    public static final SystemMessage NO_TRANSLATION_REQUIRED_2809 = new SystemMessage(2809);

    public static final SystemMessage NO_TRANSLATION_REQUIRED_2814 = new SystemMessage(2814);

    public static final SystemMessage NO_TRANSLATION_REQUIRED_2815 = new SystemMessage(2815);

    public static final SystemMessage NO_TRANSLATION_REQUIRED_2812 = new SystemMessage(2812);

    public static final SystemMessage NO_TRANSLATION_REQUIRED_2813 = new SystemMessage(2813);

    public static final SystemMessage NO_TRANSLATION_REQUIRED_2802 = new SystemMessage(2802);

    public static final SystemMessage NO_TRANSLATION_REQUIRED_2803 = new SystemMessage(2803);

    public static final SystemMessage NO_TRANSLATION_REQUIRED_2800 = new SystemMessage(2800);

    public static final SystemMessage NO_TRANSLATION_REQUIRED_2801 = new SystemMessage(2801);

    public static final SystemMessage NO_TRANSLATION_REQUIRED_2806 = new SystemMessage(2806);

    public static final SystemMessage NO_TRANSLATION_REQUIRED_2807 = new SystemMessage(2807);

    public static final SystemMessage NO_TRANSLATION_REQUIRED_2804 = new SystemMessage(2804);

    public static final SystemMessage NO_TRANSLATION_REQUIRED_2805 = new SystemMessage(2805);

    public static final SystemMessage YOU_VE_ALREADY_REQUESTED_A_TERRITORY_WAR_IN_ANOTHER_TERRITORY_ELSEWHERE = new SystemMessage(2795);

    public static final SystemMessage THE_TERRITORY_WAR_CHANNEL_AND_FUNCTIONS_WILL_NOW_BE_DEACTIVATED = new SystemMessage(2794);

    public static final SystemMessage THE_MINIMUM_NUMBER_S1_OF_PEOPLE_TO_ENTER_INSTANT_ZONE_IS_NOT_MET_AND_ENTRY_IS_NOT_AVAILABLE = new SystemMessage(2793);

    public static final SystemMessage _50_CLAN_REPUTATION_POINTS_WILL_BE_AWARDED_DO_YOU_WISH_TO_CONTINUE = new SystemMessage(2792);

    public static final SystemMessage THE_TERRITORY_WAR_WILL_END_IN_S1_MINUTES = new SystemMessage(2799);

    public static final SystemMessage THE_TERRITORY_WAR_WILL_END_IN_S1_HOURS = new SystemMessage(2798);

    public static final SystemMessage IT_IS_NOT_A_TERRITORY_WAR_REGISTRATION_PERIOD_SO_A_REQUEST_CANNOT_BE_MADE_AT_THIS_TIME = new SystemMessage(2797);

    public static final SystemMessage THE_CLAN_WHO_OWNS_THE_TERRITORY_CANNOT_PARTICIPATE_IN_THE_TERRITORY_WAR_AS_MERCENARIES = new SystemMessage(2796);

    public static final SystemMessage C1_HAS_DRAINED_YOU_OF_S2_HP = new SystemMessage(2787);

    public static final SystemMessage START_ACTION_IS_AVAILABLE_ONLY_WHEN_CONTROLLING_THE_AIRSHIP = new SystemMessage(2786);

    public static final SystemMessage IT_S_A_LARGE_SCALED_AIRSHIP_FOR_TRANSPORTATIONS_AND_BATTLES_AND_CAN_BE_OWNED_BY_THE_UNIT_OF_CLAN = new SystemMessage(2785);

    public static final SystemMessage THE_BATTLEFIELD_HAS_BEEN_CLOSED_THE_MATCH_HAS_ENDED_IN_A_TIE_BECAUSE_THE_MATCH_LASTED_FOR_S1 = new SystemMessage(2784);

    public static final SystemMessage CLAN_PARTICIPATION_REQUEST_IS_CANCELLED_IN_S1_TERRITORY = new SystemMessage(2791);

    public static final SystemMessage CLAN_PARTICIPATION_IS_REQUESTED_IN_S1_TERRITORY = new SystemMessage(2790);

    public static final SystemMessage MERCENARY_PARTICIPATION_REQUEST_IS_CANCELLED_IN_S1_TERRITORY = new SystemMessage(2789);

    public static final SystemMessage MERCENARY_PARTICIPATION_IS_REQUESTED_IN_S1_TERRITORY = new SystemMessage(2788);

    public static final SystemMessage THE_EFFECT_OF_TERRITORY_WARD_IS_DISAPPEARING = new SystemMessage(2776);

    public static final SystemMessage THE_AIRSHIP_SUMMON_LICENSE_HAS_BEEN_ENTERED_YOUR_CLAN_CAN_NOW_SUMMON_THE_AIRSHIP = new SystemMessage(2777);

    public static final SystemMessage YOU_CANNOT_TELEPORT_WHILE_IN_POSSESSION_OF_A_WARD = new SystemMessage(2778);

    public static final SystemMessage FURTHER_INCREASE_IN_ALTITUDE_IS_NOT_ALLOWED = new SystemMessage(2779);

    public static final SystemMessage FURTHER_DECREASE_IN_ALTITUDE_IS_NOT_ALLOWED = new SystemMessage(2780);

    public static final SystemMessage NUMBER_OF_UNITS__S1 = new SystemMessage(2781);

    public static final SystemMessage NUMBER_OF_PEOPLE__S1 = new SystemMessage(2782);

    public static final SystemMessage NO_ONE_IS_LEFT_FROM_THE_OPPOSING_TEAM_THUS_VICTORY_IS_YOURS = new SystemMessage(2783);

    public static final SystemMessage SEED_OF_INFINITY_CONQUEST_COMPLETE = new SystemMessage(2768);

    public static final SystemMessage SEED_OF_INFINITY_STAGE_1_DEFENSE_IN_PROGRESS = new SystemMessage(2769);

    public static final SystemMessage SEED_OF_INFINITY_STAGE_2_DEFENSE_IN_PROGRESS = new SystemMessage(2770);

    public static final SystemMessage SEED_OF_DESTRUCTION_ATTACK_IN_PROGRESS = new SystemMessage(2771);

    public static final SystemMessage SEED_OF_DESTRUCTION_CONQUEST_COMPLETE = new SystemMessage(2772);

    public static final SystemMessage SEED_OF_DESTRUCTION_DEFENSE_IN_PROGRESS = new SystemMessage(2773);

    public static final SystemMessage YOU_CAN_MAKE_ANOTHER_REPORT_IN_S1_MINUTES_YOU_HAVE_S2_POINTS_REMAINING_ON_THIS_ACCOUNT = new SystemMessage(2774);

    public static final SystemMessage THE_MATCH_CANNOT_TAKE_PLACE_BECAUSE_A_PARTY_MEMBER_IS_IN_THE_PROCESS_OF_BOARDING = new SystemMessage(2775);

    public static final SystemMessage YOU_MUST_TARGET_THE_ONE_YOU_WISH_TO_CONTROL = new SystemMessage(2761);

    public static final SystemMessage WHEN_ACTIONS_ARE_PROHIBITED_YOU_CANNOT_CONTROL_THE_TARGET = new SystemMessage(2760);

    public static final SystemMessage YOU_CANNOT_ENTER_THE_BATTLEFIELD_WHILE_IN_A_PARTY_STATE = new SystemMessage(2763);

    public static final SystemMessage YOU_CANNOT_CONTROL_BECAUSE_YOU_ARE_TOO_FAR = new SystemMessage(2762);

    public static final SystemMessage ONLY_THE_ALLIANCE_CHANNEL_LEADER_CAN_ATTEMPT_ENTRY = new SystemMessage(2765);

    public static final SystemMessage YOU_CANNOT_ENTER_BECAUSE_THE_CORRESPONDING_ALLIANCE_CHANNEL_S_MAXIMUM_NUMBER_OF_ENTRANTS_HAS = new SystemMessage(2764);

    public static final SystemMessage SEED_OF_INFINITY_STAGE_2_ATTACK_IN_PROGRESS = new SystemMessage(2767);

    public static final SystemMessage SEED_OF_INFINITY_STAGE_1_ATTACK_IN_PROGRESS = new SystemMessage(2766);

    public static final SystemMessage A_POWERFUL_ATTACK_IS_PROHIBITED_WHEN_ALLIED_TROOPS_ARE_THE_TARGET = new SystemMessage(2753);

    public static final SystemMessage THE_WAR_FOR_S1_HAS_BEEN_DECLARED = new SystemMessage(2752);

    public static final SystemMessage C1_HAS_BEEN_DESIGNATED_AS_CAT = new SystemMessage(2755);

    public static final SystemMessage PVP_MATCHES_SUCH_AS_OLYMPIAD_UNDERGROUND_COLISEUM_AERIAL_CLEFT_KRATEI_S_CUBE_AND_HANDY_S_BLOCK = new SystemMessage(2754);

    public static final SystemMessage THE_TARGET_IS_MOVING_SO_YOU_HAVE_FAILED_TO_MOUNT = new SystemMessage(2757);

    public static final SystemMessage ANOTHER_PLAYER_IS_PROBABLY_CONTROLLING_THE_TARGET = new SystemMessage(2756);

    public static final SystemMessage WHEN_ACTIONS_ARE_PROHIBITED_YOU_CANNOT_MOUNT_A_MOUNTABLE = new SystemMessage(2759);

    public static final SystemMessage YOU_CANNOT_CONTROL_THE_TARGET_WHILE_A_PET_OR_SERVITOR_IS_SUMMONED = new SystemMessage(2758);

    public static final SystemMessage NO_TRANSLATION_REQUIRED_2598 = new SystemMessage(2598);

    public static final SystemMessage NO_TRANSLATION_REQUIRED_2599 = new SystemMessage(2599);

    public static final SystemMessage NO_TRANSLATION_REQUIRED_2596 = new SystemMessage(2596);

    public static final SystemMessage NO_TRANSLATION_REQUIRED_2597 = new SystemMessage(2597);

    public static final SystemMessage RETREAT_BY_A_SET_DISTANCE_THE_VIEWING_POINT_OF_MY_CHARACTER_OR_MOUNTABLE = new SystemMessage(2594);

    public static final SystemMessage RESET_THE_VIEWING_POINT_OF_MY_CHARACTER_OR_MOUNTABLE = new SystemMessage(2595);

    public static final SystemMessage QUICKLY_SWITCH_THE_CONTENT_OF_THE_EXPANDED_SHORTCUT_WINDOW = new SystemMessage(2592);

    public static final SystemMessage ADVANCE_BY_A_SET_DISTANCE_THE_VIEWING_POINT_OF_MY_CHARACTER_OR_MOUNTABLE = new SystemMessage(2593);

    public static final SystemMessage INITIALIZE_USER_INTERFACE_LOCATION_TO_A_DEFAULT_LOCATION = new SystemMessage(2581);

    public static final SystemMessage EXPANDS_EACH_SHORTCUT_WINDOW = new SystemMessage(2580);

    public static final SystemMessage SPIN_MY_CHARACTER_OR_MOUNTABLE_TO_THE_RIGHT = new SystemMessage(2583);

    public static final SystemMessage SPIN_MY_CHARACTER_OR_MOUNTABLE_TO_THE_LEFT = new SystemMessage(2582);

    public static final SystemMessage DO_NOT_SHOW_DROP_ITEMS_DROPPED_IN_THE_WORLD_GAME_PERFORMANCE_SPEED_CAN_BE_ENHANCED_BY_USING_THIS = new SystemMessage(2577);

    public static final SystemMessage OPENS_AND_CLOSES_THE_SYSTEM_MENU_WINDOW_ENABLES_DETAILED_MENU_SELECTION = new SystemMessage(2576);

    public static final SystemMessage TURNS_OFF_ALL_GAME_SOUNDS = new SystemMessage(2579);

    public static final SystemMessage A_KEY_TO_AUTOMATICALLY_SEND_WHISPERS_TO_A_TARGETED_CHARACTER = new SystemMessage(2578);

    public static final SystemMessage QUICKLY_SPIN_IN_ALL_DIRECTIONS_THE_VIEWING_POINT_OF_MY_CHARACTER_OR_MOUNTABLE = new SystemMessage(2589);

    public static final SystemMessage ENLARGE_THE_VIEWING_POINT_OF_MY_CHARACTER_OR_MOUNTABLE = new SystemMessage(2588);

    public static final SystemMessage OPENS_THE_GM_PETITION_WINDOW_ = new SystemMessage(2591);

    public static final SystemMessage OPENS_THE_GM_MANAGER_WINDOW_ = new SystemMessage(2590);

    public static final SystemMessage SPIN_MY_CHARACTER_OR_MOUNTABLE_TO_THE_REAR = new SystemMessage(2585);

    public static final SystemMessage SPIN_MY_CHARACTER_OR_MOUNTABLE_FORWARD = new SystemMessage(2584);

    public static final SystemMessage REDUCE_THE_VIEWING_POINT_OF_MY_CHARACTER_OR_MOUNTABLE = new SystemMessage(2587);

    public static final SystemMessage CONTINUE_MOVING_IN_THE_PRESENT_DIRECTION = new SystemMessage(2586);

    public static final SystemMessage OPENS_AND_CLOSES_THE_STATUS_WINDOW_SHOWING_THE_DETAILED_STATUS_OF_A_CHARACTER_THAT_YOU_CREATED = new SystemMessage(2564);

    public static final SystemMessage OPENS_AND_CLOSES_THE_HELP_WINDOW = new SystemMessage(2565);

    public static final SystemMessage OPENS_OR_CLOSES_THE_INVENTORY_WINDOW_ = new SystemMessage(2566);

    public static final SystemMessage OPENS_AND_CLOSES_THE_MACRO_WINDOW_FOR_MACRO_SETTINGS = new SystemMessage(2567);

    public static final SystemMessage OPENS_AND_CLOSES_THE_GAME_BULLETIN_BOARD = new SystemMessage(2560);

    public static final SystemMessage OPENS_AND_CLOSES_THE_CALCULATOR = new SystemMessage(2561);

    public static final SystemMessage HIDES_OR_SHOWS_THE_CHAT_WINDOW_THE_WINDOW_ALWAYS_SHOWS_BY_DEFAULT = new SystemMessage(2562);

    public static final SystemMessage OPENS_AND_CLOSES_THE_CLAN_WINDOW_CONFIRMING_INFORMATION_OF_THE_INCLUDED_CLAN_AND_PERFORMS_THE = new SystemMessage(2563);

    public static final SystemMessage OPEN_AND_CLOSE_THE_PARTY_MATCHING_WINDOW_USEFUL_IN_ORGANIZING_A_PARTY_BY_HELPING_TO_EASILY_FIND = new SystemMessage(2572);

    public static final SystemMessage OPEN_AND_CLOSE_THE_QUEST_JOURNAL_DISPLAYING_THE_PROGRESS_OF_QUESTS = new SystemMessage(2573);

    public static final SystemMessage HIDES_OR_RE_OPENS_THE_RADAR_MAP_WHICH_ALWAYS_APPEARS_BY_DEFAULT = new SystemMessage(2574);

    public static final SystemMessage HIDE_OR_SHOW_THE_STATUS_WINDOW_THE_WINDOW_WILL_SHOW_BY_DEFAULT = new SystemMessage(2575);

    public static final SystemMessage OPENS_AND_CLOSES_THE_SKILL_WINDOW_DISPLAYING_THE_LIST_OF_SKILLS_THAT_YOU_CAN_USE = new SystemMessage(2568);

    public static final SystemMessage HIDES_OR_SHOWS_THE_MENU_WINDOW_THE_WINDOW_SHOWS_BY_DEFAULT = new SystemMessage(2569);

    public static final SystemMessage OPENS_AND_CLOSES_THE_MINI_MAP_SHOWING_DETAILED_INFORMATION_ABOUT_THE_GAME_WORLD = new SystemMessage(2570);

    public static final SystemMessage OPENS_AND_CLOSES_THE_OPTION_WINDOW = new SystemMessage(2571);

    public static final SystemMessage VITAMIN_ITEM_S1_IS_BEING_USED = new SystemMessage(2989);

    public static final SystemMessage YOU_COULD_NOT_CANCEL_RECEIPT_BECAUSE_YOUR_INVENTORY_IS_FULL = new SystemMessage(2988);

    public static final SystemMessage TRUE_INPUT_MUST_BE_ENTERED_BY_SOMEONE_OVER_15_YEARS_OLD = new SystemMessage(2991);

    public static final SystemMessage _2_UNITS_OF_VITAMIN_ITEM_S1_WAS_CONSUMED = new SystemMessage(2990);

    public static final SystemMessage YOU_CANNOT_CANCEL_DURING_AN_ITEM_ENHANCEMENT_OR_ATTRIBUTE_ENHANCEMENT = new SystemMessage(2985);

    public static final SystemMessage YOU_CANNOT_CANCEL_BECAUSE_THE_PRIVATE_SHOP_OR_WORKSHOP_IS_IN_PROGRESS = new SystemMessage(2984);

    public static final SystemMessage PLEASE_SET_THE_AMOUNT_OF_ADENA_TO_RECEIVE = new SystemMessage(2987);

    public static final SystemMessage PLEASE_SET_THE_AMOUNT_OF_ADENA_TO_SEND = new SystemMessage(2986);

    public static final SystemMessage YOU_COULD_NOT_RECEIVE_BECAUSE_YOUR_INVENTORY_IS_FULL = new SystemMessage(2981);

    public static final SystemMessage YOU_CANNOT_RECEIVE_BECAUSE_YOU_DON_T_HAVE_ENOUGH_ADENA = new SystemMessage(2980);

    public static final SystemMessage YOU_CANNOT_CANCEL_DURING_AN_EXCHANGE = new SystemMessage(2983);

    public static final SystemMessage YOU_CANNOT_CANCEL_IN_A_NON_PEACE_ZONE_LOCATION = new SystemMessage(2982);

    public static final SystemMessage YOU_CANNOT_RECEIVE_DURING_AN_EXCHANGE = new SystemMessage(2977);

    public static final SystemMessage YOU_CANNOT_RECEIVE_IN_A_NON_PEACE_ZONE_LOCATION = new SystemMessage(2976);

    public static final SystemMessage YOU_CANNOT_RECEIVE_DURING_AN_ITEM_ENHANCEMENT_OR_ATTRIBUTE_ENHANCEMENT = new SystemMessage(2979);

    public static final SystemMessage YOU_CANNOT_RECEIVE_BECAUSE_THE_PRIVATE_SHOP_OR_WORKSHOP_IS_IN_PROGRESS = new SystemMessage(2978);

    public static final SystemMessage I_M_SORRY_TO_GIVE_YOU_A_SATISFACTORY_RESPONSE__N__NIF_YOU_SEND_YOUR_COMMENTS_REGARDING_THE = new SystemMessage(3004);

    public static final SystemMessage THIS_SKILL_CANNOT_BE_ENHANCED = new SystemMessage(3005);

    public static final SystemMessage NEWLY_USED_PC_CAFE_S1_POINTS_WERE_WITHDRAWN = new SystemMessage(3006);

    public static final SystemMessage SHYEED_S_ROAR_FILLED_WITH_WRATH_RINGS_THROUGHOUT_THE_STAKATO_NEST = new SystemMessage(3007);

    public static final SystemMessage THE_COMMAND_CHANNEL_MATCHING_ROOM_WAS_CREATED = new SystemMessage(3000);

    public static final SystemMessage THE_COMMAND_CHANNEL_MATCHING_ROOM_INFORMATION_WAS_EDITED = new SystemMessage(3001);

    public static final SystemMessage WHEN_THE_RECIPIENT_DOESN_T_EXIST_OR_THE_CHARACTER_HAS_BEEN_DELETED_SENDING_MAIL_IS_NOT_POSSIBLE = new SystemMessage(3002);

    public static final SystemMessage C1_ENTERED_THE_COMMAND_CHANNEL_MATCHING_ROOM = new SystemMessage(3003);

    public static final SystemMessage YOU_CANNOT_ENTER_THE_COMMAND_CHANNEL_MATCHING_ROOM_BECAUSE_YOU_DO_NOT_MEET_THE_REQUIREMENTS = new SystemMessage(2996);

    public static final SystemMessage YOU_EXITED_FROM_THE_COMMAND_CHANNEL_MATCHING_ROOM = new SystemMessage(2997);

    public static final SystemMessage YOU_WERE_EXPELLED_FROM_THE_COMMAND_CHANNEL_MATCHING_ROOM = new SystemMessage(2998);

    public static final SystemMessage THE_COMMAND_CHANNEL_AFFILIATED_PARTY_S_PARTY_MEMBER_CANNOT_USE_THE_MATCHING_SCREEN = new SystemMessage(2999);

    public static final SystemMessage PLEASE_CHOOSE_THE_2ND_STAGE_TYPE = new SystemMessage(2992);

    public static final SystemMessage WHEN_AN_COMMAND_CHANNEL_LEADER_GOES_OUT_OF_THE_COMMAND_CHANNEL_MATCHING_ROOM_THE_CURRENTLY_OPEN = new SystemMessage(2993);

    public static final SystemMessage THE_COMMAND_CHANNEL_MATCHING_ROOM_WAS_CANCELLED = new SystemMessage(2994);

    public static final SystemMessage THIS_COMMAND_CHANNEL_MATCHING_ROOM_IS_ALREADY_CANCELLED = new SystemMessage(2995);

    public static final SystemMessage YOUR_ACCOUNT_HAS_BEEN_TEMPORARILY_RESTRICTED_BECAUSE_IT_IS_SUSPECTED_OF_BEING_USED_ABNORMALLY = new SystemMessage(2959);

    public static final SystemMessage AN_AGATHION_HAS_ALREADY_BEEN_SUMMONED = new SystemMessage(2958);

    public static final SystemMessage CHARACTERS_WITH_A_FEBRUARY_29_CREATION_DATE_WILL_RECEIVE_A_GIFT_ON_FEBRUARY_28 = new SystemMessage(2957);

    public static final SystemMessage A_USER_PARTICIPATING_IN_THE_OLYMPIAD_CANNOT_WITNESS_THE_BATTLE = new SystemMessage(2956);

    public static final SystemMessage THE_TERRITORY_WAR_EXCLUSIVE_DISGUISE_AND_TRANSFORMATION_CAN_BE_USED_20_MINUTES_BEFORE_THE_START = new SystemMessage(2955);

    public static final SystemMessage THE_SECOND_GIFT_S_REMAINING_RESUPPLY_TIME_IS_S1_SECONDS_IF_YOU_RESUMMON_THE_AGATHION_AT_THE_GIFT = new SystemMessage(2947);

    public static final SystemMessage THE_SECOND_GIFT_S_REMAINING_RESUPPLY_TIME_IS_S1_MINUTES_S2_SECONDS_IF_YOU_RESUMMON_THE_AGATHION = new SystemMessage(2946);

    public static final SystemMessage THE_SECOND_GIFT_S_REMAINING_RESUPPLY_TIME_IS_S1_HOURS_S2_MINUTES_S3_SECONDS__IF_YOU_RESUMMON_THE = new SystemMessage(2945);

    public static final SystemMessage THE_FIRST_GIFT_S_REMAINING_RESUPPLY_TIME_IS_S1_SECONDS__IF_YOU_RESUMMON_THE_AGATHION_AT_THE_GIFT = new SystemMessage(2944);

    public static final SystemMessage THE_ITEM_THAT_YOU_RE_TRYING_TO_SEND_CANNOT_BE_FORWARDED_BECAUSE_IT_ISN_T_PROPER = new SystemMessage(2974);

    public static final SystemMessage YOU_CANNOT_FORWARD_BECAUSE_YOU_DON_T_HAVE_ENOUGH_ADENA = new SystemMessage(2975);

    public static final SystemMessage YOU_CANNOT_FORWARD_BECAUSE_THE_PRIVATE_SHOP_OR_WORKSHOP_IS_IN_PROGRESS = new SystemMessage(2972);

    public static final SystemMessage YOU_CANNOT_FORWARD_DURING_AN_ITEM_ENHANCEMENT_OR_ATTRIBUTE_ENHANCEMENT = new SystemMessage(2973);

    public static final SystemMessage YOU_CANNOT_FORWARD_IN_A_NON_PEACE_ZONE_LOCATION = new SystemMessage(2970);

    public static final SystemMessage YOU_CANNOT_FORWARD_DURING_AN_EXCHANGE = new SystemMessage(2971);

    public static final SystemMessage THE_MAIL_LIMIT_240_HAS_BEEN_EXCEEDED_AND_THIS_CANNOT_BE_FORWARDED = new SystemMessage(2968);

    public static final SystemMessage THE_PREVIOUS_MAIL_WAS_FORWARDED_LESS_THAN_1_MINUTE_AGO_AND_THIS_CANNOT_BE_FORWARDED = new SystemMessage(2969);

    public static final SystemMessage IT_S_A_PAYMENT_REQUEST_TRANSACTION_PLEASE_ATTACH_THE_ITEM = new SystemMessage(2966);

    public static final SystemMessage YOU_ARE_ATTEMPTING_TO_SEND_MAIL_DO_YOU_WISH_TO_PROCEED = new SystemMessage(2967);

    public static final SystemMessage BEING_APPOINTED_AS_A_NOBLESSE_WILL_CANCEL_ALL_RELATED_QUESTS_DO_YOU_WISH_TO_CONTINUE = new SystemMessage(2964);

    public static final SystemMessage YOU_CANNOT_PURCHASE_AND_RE_PURCHASE_THE_SAME_TYPE_OF_ITEM_AT_THE_SAME_TIME = new SystemMessage(2965);

    public static final SystemMessage THIS_ITEM_CANNOT_BE_USED_IN_THE_CURRENT_TRANSFORMATION_STATTE = new SystemMessage(2962);

    public static final SystemMessage THE_OPPONENT_HAS_NOT_EQUIPPED_S1_SO_S2_CANNOT_BE_USED = new SystemMessage(2963);

    public static final SystemMessage THE_ITEM_S1_IS_REQUIRED = new SystemMessage(2960);

    public static final SystemMessage _2_UNITS_OF_THE_ITEM_S1_IS_REQUIRED = new SystemMessage(2961);

    public static final SystemMessage A_NEW_LEADER_EMERGED_LILITH_WHO_SEEKED_TO_SUMMON_SHILEN_FROM_THE_AFTERLIFE = new SystemMessage(3049);

    public static final SystemMessage BUT_IN_THE_END_SOME_SURVIVORS_MANAGED_TO_HIDE_IN_UNDERGROUND_CATACOMBS = new SystemMessage(3048);

    public static final SystemMessage NOW_IN_THE_MIDST_OF_IMPENDING_WAR_THE_MERCHANT_OF_MAMMON_STRUCK_A_DEAL = new SystemMessage(3051);

    public static final SystemMessage AND_TO_REBUILD_THE_LILIM_ARMY_WITHIN_THE_EIGHT_NECROPOLISES = new SystemMessage(3050);

    public static final SystemMessage AND_RIGHT_NOW_THE_DOCUMENT_WE_RE_LOOKING_FOR_IS_THAT_CONTRACT = new SystemMessage(3053);

    public static final SystemMessage HE_SUPPLIES_SHUNAIMAN_WITH_WAR_FUNDS_IN_EXCHANGE_FOR_PROTECTION = new SystemMessage(3052);

    public static final SystemMessage IT_S_THE_SEAL_DEVICES_I_NEED_YOU_TO_DESTROY_THEM_WHILE_I_DISTRACT_LILITH = new SystemMessage(3055);

    public static final SystemMessage FINALLY_YOU_RE_HERE_I_M_ANAKIM_I_NEED_YOUR_HELP = new SystemMessage(3054);

    public static final SystemMessage AROUND_FIFTEEN_HUNDRED_YEARS_AGO_THE_LANDS_WERE_RIDDLED_WITH_HERETICS = new SystemMessage(3041);

    public static final SystemMessage BY_USING_THE_COURT_MAGICIAN_S_MAGIC_STAFF_OPEN_THE_DOOR_ON_WHICH_THE_MAGICIAN_S_BARRIER_IS = new SystemMessage(3040);

    public static final SystemMessage BUT_A_MIRACLE_HAPPENED_AT_THE_ENTHRONEMENT_OF_SHUNAIMAN_THE_FIRST_EMPEROR = new SystemMessage(3043);

    public static final SystemMessage WORSHIPPERS_OF_SHILEN_THE_GODDESS_OF_DEATH = new SystemMessage(3042);

    public static final SystemMessage SURROUNDED_BY_SACRED_FLAMES_AND_THREE_PAIRS_OF_WINGS = new SystemMessage(3045);

    public static final SystemMessage ANAKIM_AN_ANGEL_OF_EINHASAD_CAME_DOWN_FROM_THE_SKIES = new SystemMessage(3044);

    public static final SystemMessage THE_EMPEROR_S_ARMY_LED_BY_ANAKIM_ATTACKED__SHILEN_S_PEOPLE__RELENTLESSLY = new SystemMessage(3047);

    public static final SystemMessage THUS_EMPOWERED_THE_EMPEROR_LAUNCHED_A_WAR_AGAINST__SHILEN_S_PEOPLE_ = new SystemMessage(3046);

    public static final SystemMessage THERE_IS_AN_UNREAD_MAIL = new SystemMessage(3064);

    public static final SystemMessage CURRENT_LOCATION__INSIDE_THE_CHAMBER_OF_DELUSION = new SystemMessage(3065);

    public static final SystemMessage YOU_CANNOT_USE_THE_MAIL_FUNCTION_OUTSIDE_THE_PEACE_ZONE = new SystemMessage(3066);

    public static final SystemMessage S1_CANCELED_THE_SENT_MAIL = new SystemMessage(3067);

    public static final SystemMessage THE_MAIL_WAS_RETURNED_DUE_TO_THE_EXCEEDED_WAITING_TIME = new SystemMessage(3068);

    public static final SystemMessage DO_YOU_REALLY_WANT_TO_CANCEL_THE_TRANSACTION = new SystemMessage(3069);

    public static final SystemMessage SKILL_NOT_AVAILABLE_TO_BE_ENHANCED_CHECK_SKILL_S_LV_AND_CURRENT_PC_STATUS = new SystemMessage(3070);

    public static final SystemMessage DO_YOU_REALLY_WANT_TO_RESET_1000000010_MILLION_ADENA_WILL_BE_CONSUMED = new SystemMessage(3071);

    public static final SystemMessage PLEASE_HURRY_I_DON_T_HAVE_MUCH_TIME_LEFT = new SystemMessage(3056);

    public static final SystemMessage FOR_EINHASAD = new SystemMessage(3057);

    public static final SystemMessage EMBRYO = new SystemMessage(3058);

    public static final SystemMessage S1_DID_NOT_RECEIVE_IT_DURING_THE_WAITING_TIME_SO_IT_WAS_RETURNED_AUTOMATICALLY_TNTLS = new SystemMessage(3059);

    public static final SystemMessage THE_SEALING_DEVICE_GLITTERS_AND_MOVES_ACTIVATION_COMPLETE_NORMALLY = new SystemMessage(3060);

    public static final SystemMessage THERE_COMES_A_SOUND_OF_OPENING_THE_HEAVY_DOOR_FROM_SOMEWHERE = new SystemMessage(3061);

    public static final SystemMessage DO_YOU_WANT_TO_PAY_S1_ADENA = new SystemMessage(3062);

    public static final SystemMessage DO_YOU_REALLY_WANT_TO_FORWARD = new SystemMessage(3063);

    public static final SystemMessage YOU_CANNOT_SEND_A_MAIL_TO_YOURSELF = new SystemMessage(3019);

    public static final SystemMessage YOU_CANNOT_USE_SKILL_ENHANCING_SYSTEM_FUNCTIONS_FOR_THE_SKILLS_CURRENTLY_NOT_ACQUIRED = new SystemMessage(3018);

    public static final SystemMessage YOU_CANNOT_USE_ANY_SKILL_ENHANCING_SYSTEM_UNDER_YOUR_STATUS_CHECK_OUT_THE_PC_S_CURRENT_STATUS = new SystemMessage(3017);

    public static final SystemMessage ITEM_SELECTION_IS_POSSIBLE_UP_TO_8 = new SystemMessage(3016);

    public static final SystemMessage I_CAN_FEEL_THAT_THE_ENERGY_BEING_FLOWN_IN_THE_KASHA_S_EYE_IS_GETTING_STRONGER_RAPIDLY = new SystemMessage(3023);

    public static final SystemMessage THE_KASHA_S_EYE_GIVES_YOU_A_STRANGE_FEELING = new SystemMessage(3022);

    public static final SystemMessage STAND_BY_FOR_THE_GAME_TO_BEGIN = new SystemMessage(3021);

    public static final SystemMessage WHEN_NOT_ENTERING_THE_AMOUNT_FOR_THE_PAYMENT_REQUEST_YOU_CANNOT_SEND_ANY_MAIL = new SystemMessage(3020);

    public static final SystemMessage MAIL_SUCCESSFULLY_CANCELLED = new SystemMessage(3011);

    public static final SystemMessage MAIL_SUCCESSFULLY_RETURNED = new SystemMessage(3010);

    public static final SystemMessage MAIL_SUCCESSFULLY_SENT = new SystemMessage(3009);

    public static final SystemMessage THE_MAIL_HAS_ARRIVED = new SystemMessage(3008);

    public static final SystemMessage PLEASE_SELECT_THE_MAIL_TO_BE_DELETED = new SystemMessage(3015);

    public static final SystemMessage DO_YOU_WISH_TO_ERASE_THE_SELECTED_MAIL = new SystemMessage(3014);

    public static final SystemMessage C1_HAS_SUCCESSFULY_ENCHANTED_A__S2_S3 = new SystemMessage(3013);

    public static final SystemMessage MAIL_SUCCESSFULLY_RECEIVED = new SystemMessage(3012);

    public static final SystemMessage THE_DOOR_IN_FRONT_OF_US_IS_THE_ENTRANCE_TO_THE_DAWN_S_DOCUMENT_STORAGE_APPROACH_TO_THE_CODE = new SystemMessage(3034);

    public static final SystemMessage MY_POWER_S_WEAKENING_PLEASE_ACTIVATE_THE_SEALING_DEVICE_POSSESSED_BY_LILITH_S_MAGICAL_CURSE = new SystemMessage(3035);

    public static final SystemMessage IN_ORDER_TO_HELP_ANAKIM_ACTIVATE_THE_SEALING_DEVICE_OF_THE_EMPEROR_WHO_IS_POSSESED_BY_THE_EVIL = new SystemMessage(3032);

    public static final SystemMessage BY_USING_THE_INVISIBLE_SKILL_SNEAK_INTO_THE_DAWN_S_DOCUMENT_STORAGE = new SystemMessage(3033);

    public static final SystemMessage FEMALE_GUARDS_NOTICE_THE_DISGUISES_FROM_FAR_AWAY_BETTER_THAN_THE_MALE_GUARDS_DO_SO_BEWARE = new SystemMessage(3038);

    public static final SystemMessage BY_USING_THE_HOLY_WATER_OF_EINHASAD_OPEN_THE_DOOR_POSSESSED_BY_THE_CURSE_OF_FLAMES = new SystemMessage(3039);

    public static final SystemMessage YOU_SUCH_A_FOOL_THE_VICTORY_OVER_THIS_WAR_BELONGS_TO_SHILIEN = new SystemMessage(3036);

    public static final SystemMessage MALE_GUARDS_CAN_DETECT_THE_CONCEALMENT_BUT_THE_FEMALE_GUARDS_CANNOT = new SystemMessage(3037);

    public static final SystemMessage YOU_CANNOT_USE_THE_SKILL_ENHANCING_FUNCTION_ON_THIS_LEVEL_YOU_CAN_USE_THE_CORRESPONDING_FUNCTION = new SystemMessage(3026);

    public static final SystemMessage YOU_CANNOT_USE_THE_SKILL_ENHANCING_FUNCTION_IN_THIS_CLASS_YOU_CAN_USE_CORRESPONDING_FUNCTION = new SystemMessage(3027);

    public static final SystemMessage KASHA_S_EYE_PITCHES_AND_TOSSES_LIKE_IT_S_ABOUT_TO_EXPLODE = new SystemMessage(3024);

    public static final SystemMessage S2_HAS_MADE_A_PAYMENT_OF_S1_ADENA_PER_YOUR_PAYMENT_REQUEST_MAIL = new SystemMessage(3025);

    public static final SystemMessage YOU_CANNOT_CANCEL_SENT_MAIL_SINCE_THE_RECIPIENT_RECEIVED_IT = new SystemMessage(3030);

    public static final SystemMessage BY_USING_THE_SKILL_OF_EINHASAD_S_HOLY_SWORD_DEFEAT_THE_EVIL_LILIMS = new SystemMessage(3031);

    public static final SystemMessage YOU_CANNOT_USE_THE_SKILL_ENHANCING_FUNCTION_IN_THIS_CLASS_YOU_CAN_USE_THE_SKILL_ENHANCING = new SystemMessage(3028);

    public static final SystemMessage S1_RETURNED_THE_MAIL = new SystemMessage(3029);

    public static final SystemMessage STOP_ALL_ACTIONS_OF_MY_CONTROLLED_MOUNTABLE = new SystemMessage(2852);

    public static final SystemMessage IF_YOU_JOIN_THE_CLAN_ACADEMY_YOU_CAN_BECOME_A_CLAN_MEMBER_AND_LEARN_THE_GAME_SYSTEM_UNTIL_YOU = new SystemMessage(2853);

    public static final SystemMessage IF_YOU_BECOME_LEVEL_40_THE_SECOND_CLASS_CHANGE_IS_AVAILABLE_IF_YOU_COMPLETE_THE_SECOND_CLASS = new SystemMessage(2854);

    public static final SystemMessage YOU_CAN_SEE_THE_GAME_HELP = new SystemMessage(2855);

    public static final SystemMessage NO_TRANSLATION_REQUIRED_2848 = new SystemMessage(2848);

    public static final SystemMessage NO_TRANSLATION_REQUIRED_2849 = new SystemMessage(2849);

    public static final SystemMessage NO_TRANSLATION_REQUIRED_2850 = new SystemMessage(2850);

    public static final SystemMessage STOP_ALL_ACTIONS_OF_MY_CHARACTER = new SystemMessage(2851);

    public static final SystemMessage THIS_DISPLAYS_A_LIST_OF_MY_CHARACTER_S_SKILL_AND_MAGIC_ABILITIES = new SystemMessage(2860);

    public static final SystemMessage THIS_CONFIRMS_MY_CHARACTER_S_CLAN_INFORMATION_AND_MANAGES_THE_CLAN = new SystemMessage(2861);

    public static final SystemMessage THIS_DISPLAYS_ALL_THE_ACTIONS_THAT_MY_CHARACTER_CAN_TAKE = new SystemMessage(2862);

    public static final SystemMessage THIS_DISPLAYS_THE_LIST_OF_ALL_THE_QUESTS_THAT_MY_CHARACTER_IS_UNDERTAKING_THE_QUEST_PROGRESS = new SystemMessage(2863);

    public static final SystemMessage YOU_CAN_ASK_A_QUESTION_ABOUT_YOUR_GAME_PROGRESS_TO_A_GM = new SystemMessage(2856);

    public static final SystemMessage YOU_CAN_SELECT_SEVERAL_OPTIONS_RELATED_TO_THE_GAME_INCLUDING_GRAPHIC_SETTINGS_AND_SOUND_SETTINGS = new SystemMessage(2857);

    public static final SystemMessage YOU_ARE_RESTARTING_THE_GAME_AS_ANOTHER_CHARACTER = new SystemMessage(2858);

    public static final SystemMessage YOU_ARE_QUITTING_THE_GAME_CLIENT_AND_LOGGING_OUT_FROM_THE_SERVER = new SystemMessage(2859);

    public static final SystemMessage YOU_CAN_CLOSE_OR_OPEN_SPECIFIC_MESSAGES_FROM_THE_CHAT_SCREEN_YOU_CAN_CLOSE_OR_OPEN_THE_SYSTEM = new SystemMessage(2869);

    public static final SystemMessage IF_YOU_CLICK_THE_CHAT_TAB_YOU_CAN_SELECT_AND_VIEW_THE_MESSAGES_OF_YOUR_DESIRED_GROUP_THE_TABS = new SystemMessage(2868);

    public static final SystemMessage YOU_CAN_USE_THE_PARTY_MATCHING_FUNCTION_THAT_ALLOWS_YOU_TO_EASILY_FORM_PARTIES_WITH_OTHER = new SystemMessage(2871);

    public static final SystemMessage YOU_CAN_LOGIN_ONTO_MSN_MESSENGER_YOU_CAN_ALSO_CHAT_WITH_REGISTERED_FRIENDS_WITHIN_THE_GAME = new SystemMessage(2870);

    public static final SystemMessage THIS_OPENS_AN_INVENTORY_WINDOW_WHERE_I_CAN_CHECK_THE_LIST_OF_ALL_MY_CHARACTER_S_ITEMS = new SystemMessage(2865);

    public static final SystemMessage THIS_DISPLAYS_MY_CHARACTER_S_DETAILED_STATUS_INFORMATION_I_CAN_EASILY_CONFIRM_WHEN_AN_ITEM_IS = new SystemMessage(2864);

    public static final SystemMessage CLICK_HERE_TO_SEE_A_GAME_SYSTEM_MENU_THAT_CONTAINS_VARIOUS_FUNCTIONS_OF_THE_GAME_YOU_CAN_CHECK = new SystemMessage(2867);

    public static final SystemMessage I_CAN_SEE_INFORMATION_ABOUT_MY_LOCATION_BY_OPENING_A_MINI_MAP_WINDOW_AND_I_CAN_CHECK_CURRENT = new SystemMessage(2866);

    public static final SystemMessage _IF_YOU_BECOME_LEVEL_40_THE_SECOND_CLASS_CHANGE_IS_AVAILABLE_IF_YOU_COMPLETE_THE_SECOND_CLASS = new SystemMessage(2876);

    public static final SystemMessage THIS_ENLARGES_THE_RAIDER_S_VIEW_AND_MARKS_A_MORE_DETAILED_TOPOGRAPHY = new SystemMessage(2873);

    public static final SystemMessage YOU_CAN_INSTALL_VARIOUS_RAID_OPTIONS_SUCH_AS_MONSTER_OR_PARTY_MEMBER_MARK_AND_RAID_FIX = new SystemMessage(2872);

    public static final SystemMessage _IF_YOU_JOIN_THE_CLAN_ACADEMY_YOU_CAN_BECOME_A_CLAN_MEMBER_AND_LEARN_THE_GAME_SYSTEM_UNTIL_YOU = new SystemMessage(2875);

    public static final SystemMessage THIS_DIMINISHES_THE_RAIDER_S_VIEW_AND_MARKS_A_WIDER_TOPOGRAPHY = new SystemMessage(2874);

    public static final SystemMessage DESIGNATE_A_SHORTCUT_KEY_FOR_THE_FLYING_TRANSFORMED_OBJECT_EXCLUSIVE_USE_SHORTCUT_WINDOW_S_NO_7 = new SystemMessage(2822);

    public static final SystemMessage DESIGNATE_A_SHORTCUT_KEY_FOR_THE_FLYING_TRANSFORMED_OBJECT_EXCLUSIVE_USE_SHORTCUT_WINDOW_S_NO_8 = new SystemMessage(2823);

    public static final SystemMessage DESIGNATE_A_SHORTCUT_KEY_FOR_THE_FLYING_TRANSFORMED_OBJECT_EXCLUSIVE_USE_SHORTCUT_WINDOW_S_NO_5 = new SystemMessage(2820);

    public static final SystemMessage DESIGNATE_A_SHORTCUT_KEY_FOR_THE_FLYING_TRANSFORMED_OBJECT_EXCLUSIVE_USE_SHORTCUT_WINDOW_S_NO_6 = new SystemMessage(2821);

    public static final SystemMessage DESIGNATE_A_SHORTCUT_KEY_FOR_THE_FLYING_TRANSFORMED_OBJECT_EXCLUSIVE_USE_SHORTCUT_WINDOW_S_NO_3 = new SystemMessage(2818);

    public static final SystemMessage DESIGNATE_A_SHORTCUT_KEY_FOR_THE_FLYING_TRANSFORMED_OBJECT_EXCLUSIVE_USE_SHORTCUT_WINDOW_S_NO_4 = new SystemMessage(2819);

    public static final SystemMessage DESIGNATE_A_SHORTCUT_KEY_FOR_THE_FLYING_TRANSFORMED_OBJECT_EXCLUSIVE_USE_SHORTCUT_WINDOW_S_NO_1 = new SystemMessage(2816);

    public static final SystemMessage DESIGNATE_A_SHORTCUT_KEY_FOR_THE_FLYING_TRANSFORMED_OBJECT_EXCLUSIVE_USE_SHORTCUT_WINDOW_S_NO_2 = new SystemMessage(2817);

    public static final SystemMessage DESIGNATE_A_SHORTCUT_KEY_FOR_THE_MOUNTABLE_EXCLUSIVE_USE_SHORTCUT_WINDOW_S_NO_3_SLOT_THE_CTRL = new SystemMessage(2830);

    public static final SystemMessage DESIGNATE_A_SHORTCUT_KEY_FOR_THE_MOUNTABLE_EXCLUSIVE_USE_SHORTCUT_WINDOW_S_NO_4_SLOT_THE_CTRL = new SystemMessage(2831);

    public static final SystemMessage DESIGNATE_A_SHORTCUT_KEY_FOR_THE_MOUNTABLE_EXCLUSIVE_USE_SHORTCUT_WINDOW_S_NO_1_SLOT_THE_CTRL = new SystemMessage(2828);

    public static final SystemMessage DESIGNATE_A_SHORTCUT_KEY_FOR_THE_MOUNTABLE_EXCLUSIVE_USE_SHORTCUT_WINDOW_S_NO_2_SLOT_THE_CTRL = new SystemMessage(2829);

    public static final SystemMessage DESIGNATE_A_SHORTCUT_KEY_FOR_THE_FLYING_TRANSFORMED_OBJECT_EXCLUSIVE_USE_SHORTCUT_WINDOW_S_NO_11 = new SystemMessage(2826);

    public static final SystemMessage DESIGNATE_A_SHORTCUT_KEY_FOR_THE_FLYING_TRANSFORMED_OBJECT_EXCLUSIVE_USE_SHORTCUT_WINDOW_S_NO_12 = new SystemMessage(2827);

    public static final SystemMessage DESIGNATE_A_SHORTCUT_KEY_FOR_THE_FLYING_TRANSFORMED_OBJECT_EXCLUSIVE_USE_SHORTCUT_WINDOW_S_NO_9 = new SystemMessage(2824);

    public static final SystemMessage DESIGNATE_A_SHORTCUT_KEY_FOR_THE_FLYING_TRANSFORMED_OBJECT_EXCLUSIVE_USE_SHORTCUT_WINDOW_S_NO_10 = new SystemMessage(2825);

    public static final SystemMessage DESIGNATE_A_SHORTCUT_KEY_FOR_THE_MOUNTABLE_EXCLUSIVE_USE_SHORTCUT_WINDOW_S_NO_12_SLOT_THE_CTRL = new SystemMessage(2839);

    public static final SystemMessage DESIGNATE_A_SHORTCUT_KEY_FOR_THE_MOUNTABLE_EXCLUSIVE_USE_SHORTCUT_WINDOW_S_NO_11_SLOT_THE_CTRL = new SystemMessage(2838);

    public static final SystemMessage DESIGNATE_A_SHORTCUT_KEY_FOR_THE_MOUNTABLE_EXCLUSIVE_USE_SHORTCUT_WINDOW_S_NO_10_SLOT_THE_CTRL = new SystemMessage(2837);

    public static final SystemMessage DESIGNATE_A_SHORTCUT_KEY_FOR_THE_MOUNTABLE_EXCLUSIVE_USE_SHORTCUT_WINDOW_S_NO_9_SLOT_THE_CTRL = new SystemMessage(2836);

    public static final SystemMessage DESIGNATE_A_SHORTCUT_KEY_FOR_THE_MOUNTABLE_EXCLUSIVE_USE_SHORTCUT_WINDOW_S_NO_8_SLOT_THE_CTRL = new SystemMessage(2835);

    public static final SystemMessage DESIGNATE_A_SHORTCUT_KEY_FOR_THE_MOUNTABLE_EXCLUSIVE_USE_SHORTCUT_WINDOW_S_NO_7_SLOT_THE_CTRL = new SystemMessage(2834);

    public static final SystemMessage DESIGNATE_A_SHORTCUT_KEY_FOR_THE_MOUNTABLE_EXCLUSIVE_USE_SHORTCUT_WINDOW_S_NO_6_SLOT_THE_CTRL = new SystemMessage(2833);

    public static final SystemMessage DESIGNATE_A_SHORTCUT_KEY_FOR_THE_MOUNTABLE_EXCLUSIVE_USE_SHORTCUT_WINDOW_S_NO_5_SLOT_THE_CTRL = new SystemMessage(2832);

    public static final SystemMessage NO_TRANSLATION_REQUIRED_2847 = new SystemMessage(2847);

    public static final SystemMessage NO_TRANSLATION_REQUIRED_2846 = new SystemMessage(2846);

    public static final SystemMessage AUTOMATICALLY_SEND_FORWARD_MY_CHARACTER_OR_MOUNTABLE = new SystemMessage(2845);

    public static final SystemMessage LOWER_THE_CONTROLLED_MOUNTABLE_TO_THE_BOTTOM = new SystemMessage(2844);

    public static final SystemMessage RAISE_THE_CONTROLLED_MOUNTABLE_TO_THE_TOP = new SystemMessage(2843);

    public static final SystemMessage LOWER_MY_CHARACTER_TO_THE_BOTTOM = new SystemMessage(2842);

    public static final SystemMessage RAISE_MY_CHARACTER_TO_THE_TOP = new SystemMessage(2841);

    public static final SystemMessage EXECUTE_THE_DESIGNATED_SHORTCUT_S_ACTION_SKILL_MACRO = new SystemMessage(2840);

    public static final SystemMessage YOU_VE_INVITED_C1_TO_JOIN_YOUR_CLAN = new SystemMessage(2912);

    public static final SystemMessage CLAN_S1_HAS_SUCCEEDED_IN_CAPTURING_S2_S_TERRITORY_WARD = new SystemMessage(2913);

    public static final SystemMessage THE_TERRITORY_WAR_WILL_BEGIN_IN_20_MINUTES_TERRITORY_RELATED_FUNCTIONS_IE__BATTLEFIELD_CHANNEL = new SystemMessage(2914);

    public static final SystemMessage THIS_CLAN_MEMBER_CANNOT_WITHDRAW_OR_BE_EXPELLED_WHILE_PARTICIPATING_IN_A_TERRITORY_WAR = new SystemMessage(2915);

    public static final SystemMessage PARTICIPATING_IN_S1_TERRITORY_WAR = new SystemMessage(2916);

    public static final SystemMessage NOT_PARTICIPATING_IN_A_TERRITORY_WAR = new SystemMessage(2917);

    public static final SystemMessage ONLY_CHARACTERS_WHO_ARE_LEVEL_40_OR_ABOVE_WHO_HAVE_COMPLETED_THEIR_SECOND_CLASS_TRANSFER_CAN = new SystemMessage(2918);

    public static final SystemMessage WHILE_DISGUISED_YOU_CANNOT_OPERATE_A_PRIVATE_OR_MANUFACTURE_STORE = new SystemMessage(2919);

    public static final SystemMessage NO_MORE_AIRSHIPS_CAN_BE_SUMMONED_AS_THE_MAXIMUM_AIRSHIP_LIMIT_HAS_BEEN_MET = new SystemMessage(2920);

    public static final SystemMessage YOU_CANNOT_BOARD_THE_AIRSHIP_BECAUSE_THE_MAXIMUM_NUMBER_FOR_OCCUPANTS_IS_MET = new SystemMessage(2921);

    public static final SystemMessage BLOCK_CHECKER_WILL_END_IN_5_SECONDS = new SystemMessage(2922);

    public static final SystemMessage BLOCK_CHECKER_WILL_END_IN_4_SECONDS = new SystemMessage(2923);

    public static final SystemMessage YOU_CANNOT_ENTER_A_SEED_WHILE_IN_A_FLYING_TRANSFORMATION_STATE = new SystemMessage(2924);

    public static final SystemMessage BLOCK_CHECKER_WILL_END_IN_3_SECONDS = new SystemMessage(2925);

    public static final SystemMessage BLOCK_CHECKER_WILL_END_IN_2_SECONDS = new SystemMessage(2926);

    public static final SystemMessage BLOCK_CHECKER_WILL_END_IN_1_SECOND = new SystemMessage(2927);

    public static final SystemMessage YOUR_REQUEST_CANNOT_BE_PROCESSED_BECAUSE_THERE_S_NO_ENOUGH_AVAILABLE_MEMORY_ON_YOUR_GRAPHIC_CARD = new SystemMessage(2929);

    public static final SystemMessage THE_C1_TEAM_HAS_WON = new SystemMessage(2928);

    public static final SystemMessage THE_SYSTEM_FILE_MAY_HAVE_BEEN_DAMAGED_AFTER_ENDING_THE_GAME_PLEASE_CHECK_THE_FILE_USING_THE = new SystemMessage(2931);

    public static final SystemMessage A_GRAPHIC_CARD_INTERNAL_ERROR_HAS_OCCURRED_PLEASE_INSTALL_THE_LATEST_VERSION_OF_THE_GRAPHIC_CARD = new SystemMessage(2930);

    public static final SystemMessage THOMAS_D_TURKEY_HAS_APPEARED_PLEASE_SAVE_SANTA = new SystemMessage(2933);

    public static final SystemMessage S1_ADENA = new SystemMessage(2932);

    public static final SystemMessage YOU_FAILED_TO_RESCUE_SANTA_AND_THOMAS_D_TURKEY_HAS_DISAPPEARED = new SystemMessage(2935);

    public static final SystemMessage YOU_HAVE_DEFEATED_THOMAS_D_TURKEY_AND_RESCUED_SANTA = new SystemMessage(2934);

    public static final SystemMessage A_TERRITORY_OWNING_CLAN_MEMBER_CANNOT_USE_A_DISGUISE_SCROLL = new SystemMessage(2937);

    public static final SystemMessage THE_DISGUISE_SCROLL_CANNOT_BE_USED_BECAUSE_IT_IS_MEANT_FOR_USE_IN_A_DIFFERENT_TERRITORY = new SystemMessage(2936);

    public static final SystemMessage A_DISGUISE_CANNOT_BE_USED_WHEN_YOU_ARE_IN_A_CHAOTIC_STATE = new SystemMessage(2939);

    public static final SystemMessage THE_DISGUISE_SCROLL_CANNOT_BE_USED_WHILE_YOU_ARE_ENGAGED_IN_A_PRIVATE_STORE_OR_MANUFACTURE = new SystemMessage(2938);

    public static final SystemMessage THE_REQUEST_CANNOT_BE_COMPLETED_BECAUSE_THE_REQUIREMENTS_ARE_NOT_MET_IN_ORDER_TO_PARTICIPATE_IN = new SystemMessage(2941);

    public static final SystemMessage YOU_CAN_INCREASE_THE_CHANCE_TO_ENCHANT_THE_ITEM_PRESS_THE_START_BUTTON_BELOW_TO_BEGIN = new SystemMessage(2940);

    public static final SystemMessage THE_FIRST_GIFT_S_REMAINING_RESUPPLY_TIME_IS_S1_MINUTES_S2_SECONDS_IF_YOU_RESUMMON_THE_AGATHION = new SystemMessage(2943);

    public static final SystemMessage THE_FIRST_GIFT_S_REMAINING_RESUPPLY_TIME_IS_S1_HOURS_S2_MINUTES_S3_SECONDS__IF_YOU_RESUMMON_THE = new SystemMessage(2942);

    public static final SystemMessage TERRITORY_WAR_HAS_BEGUN = new SystemMessage(2903);

    public static final SystemMessage YOU_VE_ACQUIRED_THE_WARD_MOVE_QUICKLY_TO_YOUR_FORCES__OUTPOST = new SystemMessage(2902);

    public static final SystemMessage YOU_CANNOT_FORCE_ATTACK_A_MEMBER_OF_THE_SAME_TERRITORY = new SystemMessage(2901);

    public static final SystemMessage S1_SECONDS_TO_THE_END_OF_TERRITORY_WAR = new SystemMessage(2900);

    public static final SystemMessage YOU_HAVE_ENTERED_A_POTENTIALLY_HOSTILE_ENVIRONMENT_SO_THE_AIRSHIP_S_SPEED_HAS_BEEN_GREATLY = new SystemMessage(2907);

    public static final SystemMessage ALTITUDE_CANNOT_BE_INCREASED_ANY_FURTHER = new SystemMessage(2906);

    public static final SystemMessage ALTITUDE_CANNOT_BE_DECREASED_ANY_FURTHER = new SystemMessage(2905);

    public static final SystemMessage TERRITORY_WAR_HAS_ENDED = new SystemMessage(2904);

    public static final SystemMessage YOU_VE_REQUESTED_C1_TO_BE_ON_YOUR_FRIENDS_LIST = new SystemMessage(2911);

    public static final SystemMessage YOU_HAVE_ENTERED_AN_INCORRECT_COMMAND = new SystemMessage(2910);

    public static final SystemMessage A_SERVITOR_OR_PET_CANNOT_BE_SUMMONED_WHILE_ON_AN_AIRSHIP = new SystemMessage(2909);

    public static final SystemMessage AS_YOU_ARE_LEAVING_THE_HOSTILE_ENVIRONMENT_THE_AIRSHIP_S_SPEED_HAS_BEEN_RETURNED_TO_NORMAL = new SystemMessage(2908);

    public static final SystemMessage YOU_OBTAINED_S1_RECOMMENDS = new SystemMessage(3207);

    public static final SystemMessage YOU_HAVE_EARNED_S1_B_S2_EXP_AND_S3_B_S4_SP = new SystemMessage(3259);

    public static final SystemMessage THERE_ARE_S2_SECONDS_REMAINING_FOR_S1S_REUSE_TIME = new SystemMessage(3263);

    public static final SystemMessage THERE_ARE_S2_MINUTES_S3_SECONDS_REMAINING_FOR_S1S_REUSE_TIME = new SystemMessage(3264);

    public static final SystemMessage THERE_ARE_S2_HOURS_S3_MINUTES_S4_SECONDS_REMAINING_FOR_S1S_REUSE_TIME = new SystemMessage(3265);

    public static final SystemMessage C1_IS_SET_TO_REFUSE_COUPLE_ACTIONS = new SystemMessage(3164);

    public static final SystemMessage YOU_ASK_FOR_COUPLE_ACTION_C1 = new SystemMessage(3150);

    public static final SystemMessage C1_ACCEPTED_COUPLE_ACTION = new SystemMessage(3151);

    public static final SystemMessage PARTY_LOOT_CHANGE_CANCELLED = new SystemMessage(3137);

    public static final SystemMessage PARTY_LOOT_CHANGED_S1 = new SystemMessage(3138);

    public static final SystemMessage COUPLE_ACTION_CANNOT_C1_TARGET_IS_DEAD = new SystemMessage(3139);

    public static final SystemMessage CURRENT_LOCATION__S1_S2_S3_INSIDE_SEED_OF_ANNIHILATION = new SystemMessage(3170);

    public static final SystemMessage YOU_HAVE_S1_MINUTES_AND_S2_SECONDS_LEFT_IN_THE_PROOF_OF_SPACE_AND_TIME_IF_AGATHION_IS_SUMMONED = new SystemMessage(3089);

    public static final SystemMessage YOU_HAVE_S1_HOURS_S2_MINUTES_AND_S3_SECONDS_LEFT_IN_THE_PROOF_OF_SPACE_AND_TIME_IF_AGATHION_IS = new SystemMessage(3088);

    public static final SystemMessage YOU_CANNOT_DELETE_CHARACTERS_ON_THIS_SERVER_RIGHT_NOW = new SystemMessage(3091);

    public static final SystemMessage YOU_HAVE_S1_SECONDS_LEFT_IN_THE_PROOF_OF_SPACE_AND_TIME_IF_AGATHION_IS_SUMMONED_WITHIN_THIS_TIME = new SystemMessage(3090);

    public static final SystemMessage YOU_HAVE_EXCEEDED_THE_CORRECT_CALCULATION_RANGE_PLEASE_ENTER_AGAIN = new SystemMessage(3093);

    public static final SystemMessage TRANSACTION_COMPLETED = new SystemMessage(3092);

    public static final SystemMessage A_USER_CURRENTLY_PARTICIPATING_IN_THE_OLYMPIAD_CANNOT_SEND_PARTY_AND_FRIEND_INVITATIONS = new SystemMessage(3094);

    public static final SystemMessage YOU_CANNOT_RESET_THE_SKILL_LINK_BECAUSE_THERE_IS_NOT_ENOUGH_ADENA = new SystemMessage(3080);

    public static final SystemMessage YOU_CANNOT_RECEIVE_IT_BECAUSE_YOU_ARE_UNDER_THE_CONDITION_THAT_THE_OPPONENT_CANNOT_ACQUIRE_ANY = new SystemMessage(3081);

    public static final SystemMessage YOU_CANNOT_SEND_MAILS_TO_ANY_CHARACTER_THAT_HAS_BLOCKED_YOU = new SystemMessage(3082);

    public static final SystemMessage IN_THE_PROCESS_OF_WORKING_ON_THE_PREVIOUS_CLAN_DECLARATION_RETREAT_PLEASE_TRY_AGAIN_LATER = new SystemMessage(3083);

    public static final SystemMessage CURRENTLY_WE_ARE_IN_THE_PROCESS_OF_CHOOSING_A_HERO_PLEASE_TRY_AGAIN_LATER = new SystemMessage(3084);

    public static final SystemMessage YOU_CAN_SUMMON_THE_PET_YOU_ARE_TRYING_TO_SUMMON_NOW_ONLY_WHEN_YOU_OWN_AN_AGIT = new SystemMessage(3085);

    public static final SystemMessage WOULD_YOU_LIKE_TO_GIVE_S2_S1 = new SystemMessage(3086);

    public static final SystemMessage THIS_MAIL_IS_BEING_SENT_WITH_A_PAYMENT_REQUEST_WOULD_YOU_LIKE_TO_CONTINUE = new SystemMessage(3087);

    public static final SystemMessage S1_ACQUIRED_THE_ATTACHED_ITEM_TO_YOUR_MAIL = new SystemMessage(3072);

    public static final SystemMessage YOU_HAVE_ACQUIRED_S2_S1 = new SystemMessage(3073);

    public static final SystemMessage THE_ALLOWED_LENGTH_FOR_RECIPIENT_EXCEEDED = new SystemMessage(3074);

    public static final SystemMessage THE_ALLOWED_LENGTH_FOR_A_TITLE_EXCEEDED = new SystemMessage(3075);

    public static final SystemMessage _THE_ALLOWED_LENGTH_FOR_A_TITLE_EXCEEDED = new SystemMessage(3076);

    public static final SystemMessage THE_MAIL_LIMIT_240_OF_THE_OPPONENT_S_CHARACTER_HAS_BEEN_EXCEEDED_AND_THIS_CANNOT_BE_FORWARDED = new SystemMessage(3077);

    public static final SystemMessage YOU_RE_MAKING_A_REQUEST_FOR_PAYMENT_DO_YOU_WANT_TO_PROCEED = new SystemMessage(3078);

    public static final SystemMessage THERE_ARE_ITEMS_IN_THE_PET_INVENTORY_SO_YOU_CANNOT_REGISTER_AS_AN_INDIVIDUAL_STORE_OR_DROP_ITEMS = new SystemMessage(3079);

    public static final SystemMessage COUPLE_ACTION_CANNOT_C1_TARGET_IS_IN_VEHICLE_MOUNT_OTHER = new SystemMessage(3131);

    public static final SystemMessage COUPLE_ACTION_CANNOT_C1_TARGET_IS_IN_SIEGE = new SystemMessage(3130);

    public static final SystemMessage COUPLE_ACTION_CANNOT_C1_TARGET_IS_IN_HIDEOUT_SIEGE = new SystemMessage(3129);

    public static final SystemMessage COUPLE_ACTION_CANNOT_C1_TARGET_IS_IN_OLYMPIAD = new SystemMessage(3128);

    public static final SystemMessage REQUESTING_APPROVAL_CHANGE_PARTY_LOOT_S1 = new SystemMessage(3135);

    public static final SystemMessage COUPLE_ACTION_CANNOT_C1_TARGET_IS_IN_TRANSFORM = new SystemMessage(3133);

    public static final SystemMessage COUPLE_ACTION_CANNOT_C1_TARGET_IS_TELEPORTING = new SystemMessage(3132);

    public static final SystemMessage COUPLE_ACTION_CANNOT_C1_TARGET_IN_PRIVATE_STORE = new SystemMessage(3123);

    public static final SystemMessage COUPLE_ACTION_WAS_CANCELED = new SystemMessage(3121);

    public static final SystemMessage REQUEST_CANNOT_COMPLETED_BECAUSE_TARGET_DOES_NOT_MEET_LOCATION_REQUIREMENTS = new SystemMessage(3120);

    public static final SystemMessage COUPLE_ACTION_CANNOT_C1_TARGET_IS_CURSED_WEAPON_EQUIPED = new SystemMessage(3127);

    public static final SystemMessage COUPLE_ACTION_CANNOT_C1_TARGET_IN_ANOTHER_COUPLE_ACTION = new SystemMessage(3126);

    public static final SystemMessage COUPLE_ACTION_CANNOT_C1_TARGET_IS_IN_COMBAT = new SystemMessage(3125);

    public static final SystemMessage COUPLE_ACTION_CANNOT_C1_TARGET_IS_FISHING = new SystemMessage(3124);

    public static final SystemMessage YOU_CANCEL_FOR_COUPLE_ACTION = new SystemMessage(3119);

    public static final SystemMessage YOU_ARE_PROTECTED_AGGRESSIVE_MONSTERS = new SystemMessage(3108);

    public static final SystemMessage YOU_CANNOT_REGISTER_ITEM_IN_PRIVATE_STORE_OR_PRIVATE_WORKSHOP = new SystemMessage(3478);

    public static final SystemMessage YOU_CANNOT_REGISTER_PURCHASE_OR_CANCEL_ITEM_DURING_ENCHANTMENT_AUGMENTING_OR_ATTRIBUTE_ENHANCEMENT = new SystemMessage(3479);

    public static final SystemMessage YOU_CANNOT_REGISTER_ITEM_DURING_EXCHANGE = new SystemMessage(3477);

    public static final SystemMessage THE_NUMBER_OF_ALLOWED_ADENA_IS_EXCEEDED = new SystemMessage(3482);

    public static final SystemMessage THE_NUMBER_OF_ALLOWED_PIECES_IS_EXCEEDED = new SystemMessage(3483);

    public static final SystemMessage ITEMS_THAT_CANNOT_BE_EXCHANGEDDROPPEDUSE_A_PRIVATE_STORE_OR_THAT_ARE_FOR_A_LIMITED_PERIODAUGMENTING_CANNOT_BE_REGISTERED = new SystemMessage(3480);

    public static final SystemMessage IF_WEIGHT_IS_80_PERCENTS_OR_MORE_AND_INVENTORY_NUMBER_IS_90_PERCENTS_PURCHASE_AND_CANCELLATION_IS_NOT_POSSIBLE = new SystemMessage(3481);

    public static final SystemMessage THE_COMMISSION_ITEM_HAS_BEEN_SUCCESSFULLY_PURCHASED = new SystemMessage(3486);

    public static final SystemMessage THE_ITEM_HAS_BEEN_SUCCESSFULLY_REGISTERED = new SystemMessage(3484);

    public static final SystemMessage CANCELLATION_OF_SALE_FOR_THE_ITEM_IS_SUCCESSFUL = new SystemMessage(3485);

    public static final SystemMessage STOPPED_SEARCHING_THE_PARTY = new SystemMessage(3453);

    public static final SystemMessage YOU_ARE_REGISTERED_ON_THE_WAITING_LIST = new SystemMessage(3452);

    public static final SystemMessage CURRENTLY_THERE_ARE_NO_REGISTERED_ITEMS = new SystemMessage(3369);

    public static final SystemMessage ITEM_PURCHASE_IS_NOT_AVAILABLE_BECAUSE_THE_CORRESPONDING_ITEM_DOES_NOT_EXIST = new SystemMessage(3370);

    public static final SystemMessage YOU_CANNOT_CHANGE_CLASS_BECAUSE_OF_IDENTIFY_CRISIS = new SystemMessage(3574);

    public static final SystemMessage REGISTRATION_IS_NOT_AVAILABLE_BECAUSE_THE_CORRESPONDING_ITEM_DOES_NOT_EXIST = new SystemMessage(3361);

    public static final SystemMessage NOT_ENOUGH_ADENA_FOR_REGISTER_THIS_ITEM = new SystemMessage(3364);

    public static final SystemMessage FAILED_TO_REGISTER_ITEM = new SystemMessage(3365);

    public static final SystemMessage YOU_CAN_NOT_CHANGE_CLASS_IN_TRANSFORMATION = new SystemMessage(3677);

    public static final SystemMessage THIS_TERRITORY_CAN_NOT_CHANGE_CLASS = new SystemMessage(3684);

    public static final SystemMessage BONUS_ENERGY_AVAILABLE_POINTS_OF_ENERGY_RECOVERED_EVERY_WEDNESDAY_AT_6_30_ENERGY_POTION_YOU_CAN_USE_A_MAXIMUM_OF_$_s1_ONE_A_WEEK_TO_SUPPLEMENT_TO_MAINTAIN_OR_RESTORE_VITAL_ENERGY = new SystemMessage(6068);

    public static final SystemMessage YOU_CANNOT_MODIFY_AS_YOU_DO_NOT_HAVE_ENOUGH_ADENA = new SystemMessage(6099);

    public static final SystemMessage YOU_HAVE_SPENT_S1_ON_A_SUCCESSFUL_APPEARANCE_MODIFICATION = new SystemMessage(6100);

    public static final SystemMessage ITEM_GRADES_DO_NOT_MATCH = new SystemMessage(6101);

    public static final SystemMessage YOU_CANNOT_MODIFY_OR_RESTORE_NOGRADE_ITEMS = new SystemMessage(6103);

    public static final SystemMessage WEAPONS_ONLY = new SystemMessage(6104);

    public static final SystemMessage ARMOR_ONLY = new SystemMessage(6105);

    public static final SystemMessage YOU_CANNOT_EXTRACT_FROM_A_MODIFIED_ITEM = new SystemMessage(6106);

    public static final SystemMessage YOU_CANNOT_EXTRACT_FROM_ITEMS_THAT_ARE_HIGHERGRADE_THAN_ITEMS_TO_BE_MODIFIED = new SystemMessage(6102);

    public static final SystemMessage THIS_ITEM_DOES_NOT_MEET_REQUIREMENTS = new SystemMessage(6094);

    public static final SystemMessage YOU_CANNOT_AWAKEN_DUE_TO_WEIGHT_LIMITS_PLEASE_TRY_AWAKEN_AGAIN_AFTER_INCREASING_THE_ALLOWED_WEIGHT_BY_ORGANIZING_THE_INVENTORY = new SystemMessage(3652);

    public static final SystemMessage YOU_CANNOT_AWAKEN_WHILE_YOURE_TRANSFORMED_OR_RIDING = new SystemMessage(3655);

    public static final SystemMessage YOU_CANNOT_MOVE_WHILE_IN_A_CHAOTIC_STATE = new SystemMessage(3404);

    public static final SystemMessage YOU_CANNOT_MOVE_WHILE_DEAD = new SystemMessage(3392);

    public static final SystemMessage YOU_CANNOT_MOVE_DURING_COMBAT = new SystemMessage(3393);

    public static final SystemMessage SUBCLASS_S1_HAS_BEEN_UPGRADED_TO_DUAL_CLASS_S2_CONGRATULATION = new SystemMessage(3279);
}
