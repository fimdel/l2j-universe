package lineage2.gameserver.network.serverpackets;

import org.apache.commons.lang3.StringUtils;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.DoorInstance;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.utils.Location;

import java.util.ArrayList;
import java.util.List;

public class SystemMessage extends L2GameServerPacket {
    // d d (d S/d d/d dd)
    // |--------------> 0 - String 1-number 2-textref npcname (1000000-1002655)
    // 3-textref itemname 4-textref skills 5-??
    private static final int TYPE_DAMAGE = 16;
    private static final int TYPE_CLASS_NAME = 15;
    private static final int TYPE_SYSTEM_STRING = 13;
    private static final int TYPE_PLAYER_NAME = 12;
    private static final int TYPE_DOOR_NAME = 11;
    private static final int TYPE_INSTANCE_NAME = 10;
    private static final int TYPE_ELEMENT_NAME = 9;
    private static final int TYPE_UNKNOWN_8 = 8;
    private static final int TYPE_ZONE_NAME = 7;
    private static final int TYPE_LONG = 6;
    private static final int TYPE_CASTLE_NAME = 5;
    private static final int TYPE_SKILL_NAME = 4;
    private static final int TYPE_ITEM_NAME = 3;
    private static final int TYPE_NPC_NAME = 2;
    private static final int TYPE_NUMBER = 1;
    private static final int TYPE_TEXT = 0;
    private int _messageId;
    private List<Arg> args = new ArrayList<Arg>();

    public static final int YOU_HAVE_BEEN_DISCONNECTED_FROM_THE_SERVER = 0;
    public static final int THE_SERVER_WILL_BE_COMING_DOWN_IN_S1_SECONDS__PLEASE_FIND_A_SAFE_PLACE_TO_LOG_OUT = 1;
    public static final int S1_DOES_NOT_EXIST = 2;
    public static final int S1_IS_NOT_CURRENTLY_LOGGED_IN = 3;
    public static final int YOU_CANNOT_ASK_YOURSELF_TO_APPLY_TO_A_CLAN = 4;
    public static final int S1_ALREADY_EXISTS = 5;
    public static final int S1_DOES_NOT_EXIST_2 = 6;
    public static final int YOU_ARE_ALREADY_A_MEMBER_OF_S1 = 7;
    public static final int YOU_ARE_WORKING_WITH_ANOTHER_CLAN = 8;
    public static final int S1_IS_NOT_A_CLAN_LEADER = 9;
    public static final int S1_IS_WORKING_WITH_ANOTHER_CLAN = 10;
    public static final int THERE_ARE_NO_APPLICANTS_FOR_THIS_CLAN = 11;
    public static final int APPLICANT_INFORMATION_IS_INCORRECT = 12;
    public static final int UNABLE_TO_DISPERSE_YOUR_CLAN_HAS_REQUESTED_TO_PARTICIPATE_IN_A_CASTLE_SIEGE = 13;
    public static final int UNABLE_TO_DISPERSE_YOUR_CLAN_OWNS_ONE_OR_MORE_CASTLES_OR_HIDEOUTS = 14;
    public static final int YOU_ARE_IN_SIEGE = 15;
    public static final int YOU_ARE_NOT_IN_SIEGE = 16;
    public static final int CASTLE_SIEGE_HAS_BEGUN = 17;
    public static final int THE_CASTLE_SIEGE_HAS_ENDED = 18;
    public static final int THERE_IS_A_NEW_LORD_OF_THE_CASTLE = 19;
    public static final int THE_GATE_IS_BEING_OPENED = 20;
    public static final int THE_GATE_IS_BEING_DESTROYED = 21;
    public static final int YOUR_TARGET_IS_OUT_OF_RANGE = 22;
    public static final int NOT_ENOUGH_HP = 23;
    public static final int NOT_ENOUGH_MP = 24;
    public static final int REJUVENATING_HP = 25;
    public static final int REJUVENATING_MP = 26;
    public static final int YOUR_CASTING_HAS_BEEN_INTERRUPTED = 27;
    public static final int YOU_HAVE_OBTAINED_S1_ADENA = 28;
    public static final int YOU_HAVE_OBTAINED_S2_S1 = 29;
    public static final int YOU_HAVE_OBTAINED_S1 = 30;
    public static final int YOU_CANNOT_MOVE_WHILE_SITTING = 31;
    public static final int YOU_ARE_NOT_CAPABLE_OF_COMBAT_MOVE_TO_THE_NEAREST_RESTART_POINT = 32;
    public static final int YOU_CANNOT_MOVE_WHILE_CASTING = 33; // Ð�ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾
    public static final int WELCOME_TO_THE_WORLD_OF_LINEAGE_II = 34; // Ð”Ð¾Ð±Ñ€Ð¾
    @Deprecated
    public static final int YOU_HIT_FOR_S1_DAMAGE = 35; // Ð·Ð°Ð¼ÐµÐ½ÐµÐ½Ð¾
    @Deprecated
    public static final int C1_HIT_YOU_FOR_S2_DAMAGE = 36; // Ð·Ð°Ð¼ÐµÐ½ÐµÐ½Ð¾
    public static final int C1_HIT_YOU_FOR_S2_DAMAGE_2 = 37; // $c1 Ð½Ð°Ð½Ð¾Ñ�Ð¸Ñ‚ Ð’Ð°Ð¼
    public static final int THE_TGS2002_EVENT_BEGINS = 38; // Ð�Ð°Ñ‡Ð¸Ð½Ð°ÐµÑ‚Ñ�Ñ� Ð¸Ð²ÐµÐ½Ñ‚
    public static final int THE_TGS2002_EVENT_IS_OVER_THANK_YOU_VERY_MUCH = 39; // Ð˜Ð²ÐµÐ½Ñ‚
    public static final int THIS_IS_THE_TGS_DEMO_THE_CHARACTER_WILL_IMMEDIATELY_BE_RESTORED = 40; // Ð˜Ð·-Ð·Ð°
    public static final int YOU_CAREFULLY_NOCK_AN_ARROW = 41; // Ð’Ñ‹ Ð½Ð°Ñ‚Ñ�Ð³Ð¸Ð²Ð°ÐµÑ‚Ðµ
    @Deprecated
    public static final int YOU_HAVE_AVOIDED_C1S_ATTACK = 42; // Ð·Ð°Ð¼ÐµÐ½ÐµÐ½Ð¾
    @Deprecated
    public static final int YOU_HAVE_MISSED = 43; // Ð·Ð°Ð¼ÐµÐ½ÐµÐ½Ð¾
    @Deprecated
    public static final int CRITICAL_HIT = 44; // Ð·Ð°Ð¼ÐµÐ½ÐµÐ½Ð¾ C1_HAD_A_CRITICAL_HIT
    public static final int YOU_HAVE_EARNED_S1_EXPERIENCE = 45; // ÐŸÐ¾Ð»ÑƒÑ‡ÐµÐ½Ð¾: $s1
    public static final int YOU_USE_S1 = 46;
    public static final int YOU_BEGIN_TO_USE_AN_S1 = 47; // Ð˜Ñ�Ð¿Ð¾Ð»ÑŒÐ·ÑƒÐµÑ‚Ñ�Ñ�: $s1.
    @Deprecated
    public static final int S1_IS_NOT_AVAILABLE_AT_THIS_TIME_BEING_PREPARED_FOR_REUSE = 48; // Ð·Ð°Ð¼ÐµÐ½ÐµÐ½Ð¾
    public static final int YOU_HAVE_EQUIPPED_YOUR_S1 = 49; // Ð�Ð°Ð´ÐµÑ‚Ð¾: $s1.
    public static final int YOUR_TARGET_CANNOT_BE_FOUND = 50; // Ð�ÐµÑ‚ Ñ†ÐµÐ»Ð¸.
    public static final int YOU_CANNOT_USE_THIS_ON_YOURSELF = 51; // Ð’Ñ‹ Ð½Ðµ
    public static final int YOU_HAVE_EARNED_S1_ADENA = 52; // ÐŸÐ¾Ð»ÑƒÑ‡ÐµÐ½Ð¾: $s1
    public static final int YOU_HAVE_EARNED_S2_S1S = 53; // ÐŸÑ€Ð¸Ñ�Ð²Ð¾ÐµÐ½Ð¾: $s1 ($s2
    public static final int YOU_HAVE_EARNED_S1 = 54; // ÐŸÑ€Ð¸Ñ�Ð²Ð¾ÐµÐ½Ð¾: $s1.
    public static final int YOU_HAVE_FAILED_TO_PICK_UP_S1_ADENA = 55; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    public static final int YOU_HAVE_FAILED_TO_PICK_UP_S1 = 56; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    public static final int YOU_HAVE_FAILED_TO_PICK_UP_S2_S1S = 57; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    public static final int YOU_HAVE_FAILED_TO_EARN_S1_ADENA = 58; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    public static final int YOU_HAVE_FAILED_TO_EARN_S1 = 59; // Ð�ÐµÐ»ÑŒÐ·Ñ� Ð¿Ð¾Ð»ÑƒÑ‡Ð¸Ñ‚ÑŒ:
    public static final int YOU_HAVE_FAILED_TO_EARN_S2_S1S = 60; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    public static final int NOTHING_HAPPENED = 61; // Ð�Ð¸Ñ‡ÐµÐ³Ð¾ Ð½Ðµ Ð¿Ñ€Ð¾Ð¸Ð·Ð¾ÑˆÐ»Ð¾.
    public static final int S1_HAS_BEEN_SUCCESSFULLY_ENCHANTED = 62; // $s1:
    public static final int _S1_S2_HAS_BEEN_SUCCESSFULLY_ENCHANTED = 63; // +$s1
    // $s2:
    // ÑƒÐ»ÑƒÑ‡ÑˆÐµÐ½Ð¸Ðµ
    // ÑƒÐ´Ð°Ð»Ð¾Ñ�ÑŒ.
    public static final int THE_ENCHANTMENT_HAS_FAILED_YOUR_S1_HAS_BEEN_CRYSTALLIZED = 64; // $s1:
    // Ð¿Ñ€Ð¾Ð¸Ð·Ð¾ÑˆÐ»Ð°
    // ÐºÑ€Ð¸Ñ�Ñ‚Ð°Ð»Ð»Ð¸Ð·Ð°Ñ†Ð¸Ñ�.
    public static final int THE_ENCHANTMENT_HAS_FAILED_YOUR__S1_S2_HAS_BEEN_CRYSTALLIZED = 65; // +$s1
    // $s2:
    // Ð¿Ñ€Ð¾Ð¸Ð·Ð¾ÑˆÐ»Ð°
    // ÐºÑ€Ð¸Ñ�Ñ‚Ð°Ð»Ð»Ð¸Ð·Ð°Ñ†Ð¸Ñ�.
    public static final int C1_IS_INVITING_YOU_TO_JOIN_A_PARTY_DO_YOU_ACCEPT = 66; // $c1
    // Ð¿Ñ€Ð¸Ð³Ð»Ð°ÑˆÐ°ÐµÑ‚
    // Ð’Ð°Ñ�
    // Ð²
    // Ð³Ñ€ÑƒÐ¿Ð¿Ñƒ.
    // Ð’Ñ‹
    // Ñ�Ð¾Ð³Ð»Ð°Ñ�Ð½Ñ‹?
    public static final int S1_HAS_INVITED_YOU_TO_THE_JOIN_THE_CLAN_S2_DO_YOU_WISH_TO_JOIN = 67; // $s1
    // Ð¿Ñ€Ð¸Ð³Ð»Ð°ÑˆÐ°ÐµÑ‚
    // Ð’Ð°Ñ�
    // Ð²
    // ÐºÐ»Ð°Ð½
    // $s2.
    // Ð’Ñ‹
    // Ñ�Ð¾Ð³Ð»Ð°Ñ�Ð½Ñ‹?
    public static final int WOULD_YOU_LIKE_TO_WITHDRAW_FROM_THE_S1_CLAN_IF_YOU_LEAVE_YOU_WILL_HAVE_TO_WAIT_AT_LEAST_A_DAY_BEFORE_JOINING_ANOTHER_CLAN = 68; // Ð’Ñ‹Ð¹Ñ‚Ð¸
    // Ð¸Ð·
    // ÐºÐ»Ð°Ð½Ð°
    // $s1?
    // ÐŸÐ¾Ñ�Ð»Ðµ
    // Ñ�Ñ‚Ð¾Ð³Ð¾
    // Ð½ÐµÐ»ÑŒÐ·Ñ�
    // Ð²Ñ�Ñ‚ÑƒÐ¿Ð¸Ñ‚ÑŒ
    // Ð²
    // Ð´Ñ€ÑƒÐ³Ð¾Ð¹
    // ÐºÐ»Ð°Ð½
    // Ð²
    // Ñ‚ÐµÑ‡ÐµÐ½Ð¸Ðµ
    // 24
    // Ñ‡.
    public static final int WOULD_YOU_LIKE_TO_DISMISS_S1_FROM_THE_CLAN_IF_YOU_DO_SO_YOU_WILL_HAVE_TO_WAIT_AT_LEAST_A_DAY_BEFORE_ACCEPTING_A_NEW_MEMBER = 69; // Ð˜Ñ�ÐºÐ»ÑŽÑ‡Ð¸Ñ‚ÑŒ
    // Ð¸Ð·
    // ÐºÐ»Ð°Ð½Ð°
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°
    // $s1?
    // ÐŸÐ¾Ñ�Ð»Ðµ
    // Ñ�Ñ‚Ð¾Ð³Ð¾
    // Ð½ÐµÐ»ÑŒÐ·Ñ�
    // Ð¿Ñ€Ð¸Ð½Ð¸Ð¼Ð°Ñ‚ÑŒ
    // Ð²
    // ÐºÐ»Ð°Ð½
    // Ð´Ñ€ÑƒÐ³Ð¸Ñ…
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶ÐµÐ¹
    // Ð²
    // Ñ‚ÐµÑ‡ÐµÐ½Ð¸Ðµ
    // 24
    // Ñ‡.
    public static final int DO_YOU_WISH_TO_DISPERSE_THE_CLAN_S1 = 70; // Ð Ð°Ñ�Ð¿ÑƒÑ�Ñ‚Ð¸Ñ‚ÑŒ
    // ÐºÐ»Ð°Ð½
    // $s1?
    public static final int HOW_MANY_S1_S_DO_YOU_WANT_TO_DISCARD = 71; // $s1:
    // Ñ�ÐºÐ¾Ð»ÑŒÐºÐ¾
    // ÑˆÑ‚.
    // Ð²Ñ‹ÐºÐ¸Ð½ÑƒÑ‚ÑŒ?
    public static final int HOW_MANY_S1_S_DO_YOU_WANT_TO_MOVE = 72; // $s1:
    // Ñ�ÐºÐ¾Ð»ÑŒÐºÐ¾
    // ÑˆÑ‚.
    // Ð¿ÐµÑ€ÐµÐ¼ÐµÑ�Ñ‚Ð¸Ñ‚ÑŒ?
    public static final int HOW_MANY_S1_S_DO_YOU_WANT_TO_DESTROY = 73; // $s1:
    // Ñ�ÐºÐ¾Ð»ÑŒÐºÐ¾
    // ÑˆÑ‚.
    // ÑƒÐ½Ð¸Ñ‡Ñ‚Ð¾Ð¶Ð¸Ñ‚ÑŒ?
    public static final int DO_YOU_WISH_TO_DESTROY_YOUR_S1 = 74; // $s1 -
    // ÑƒÐ½Ð¸Ñ‡Ñ‚Ð¾Ð¶Ð¸Ñ‚ÑŒ?
    public static final int ID_DOES_NOT_EXIST = 75; // Ð›Ð¾Ð³Ð¸Ð½ Ð½Ðµ Ñ�ÑƒÑ‰ÐµÑ�Ñ‚Ð²ÑƒÐµÑ‚.
    public static final int INCORRECT_PASSWORD = 76; // Ð�ÐµÐ¿Ñ€Ð°Ð²Ð¸Ð»ÑŒÐ½Ñ‹Ð¹ Ð¿Ð°Ñ€Ð¾Ð»ÑŒ.
    public static final int YOU_CANNOT_CREATE_ANOTHER_CHARACTER_PLEASE_DELETE_THE_EXISTING_CHARACTER_AND_TRY_AGAIN = 77; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ñ�Ð¾Ð·Ð´Ð°Ñ‚ÑŒ
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°.
    // Ð£Ð´Ð°Ð»Ð¸Ñ‚Ðµ
    // ÑƒÐ¶Ðµ
    // Ñ�ÑƒÑ‰ÐµÑ�Ñ‚Ð²ÑƒÑŽÑ‰ÐµÐ³Ð¾
    // Ð¸
    // Ð¿Ð¾Ð²Ñ‚Ð¾Ñ€Ð¸Ñ‚Ðµ
    // Ð¿Ð¾Ð¿Ñ‹Ñ‚ÐºÑƒ.
    public static final int DO_YOU_WISH_TO_DELETE_S1 = 78; // Ð£Ð´Ð°Ð»Ð¸Ñ‚ÑŒ Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°
    // $s1?
    public static final int THIS_NAME_ALREADY_EXISTS = 79; // Ð¢Ð°ÐºÐ¾Ðµ Ð¸Ð¼Ñ� ÑƒÐ¶Ðµ
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·ÑƒÐµÑ‚Ñ�Ñ�.
    public static final int YOUR_TITLE_CANNOT_EXCEED_16_CHARACTERS_IN_LENGTH_PLEASE_TRY_AGAIN = 80; // Ð’Ð²ÐµÐ´Ð¸Ñ‚Ðµ
    // Ð¸Ð¼Ñ�
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°
    // (Ð¼Ð°ÐºÑ�Ð¸Ð¼ÑƒÐ¼
    // 16
    // Ñ�Ð¸Ð¼Ð²Ð¾Ð»Ð¾Ð²).
    public static final int PLEASE_SELECT_YOUR_RACE = 81; // Ð’Ñ‹Ð±ÐµÑ€Ð¸Ñ‚Ðµ Ñ€Ð°Ñ�Ñƒ.
    public static final int PLEASE_SELECT_YOUR_OCCUPATION = 82; // Ð’Ñ‹Ð±ÐµÑ€Ð¸Ñ‚Ðµ
    // Ð¿Ñ€Ð¾Ñ„ÐµÑ�Ñ�Ð¸ÑŽ.
    public static final int PLEASE_SELECT_YOUR_GENDER = 83; // Ð’Ñ‹Ð±ÐµÑ€Ð¸Ñ‚Ðµ Ð¿Ð¾Ð».
    public static final int YOU_MAY_NOT_ATTACK_IN_A_PEACEFUL_ZONE = 84; // Ð’Ñ‹ Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð°Ñ‚Ð°ÐºÐ¾Ð²Ð°Ñ‚ÑŒ
    // Ð²
    // Ð¼Ð¸Ñ€Ð½Ð¾Ð¹
    // Ð·Ð¾Ð½Ðµ.
    public static final int YOU_MAY_NOT_ATTACK_THIS_TARGET_IN_A_PEACEFUL_ZONE = 85; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð°Ñ‚Ð°ÐºÐ¾Ð²Ð°Ñ‚ÑŒ
    // Ñ†ÐµÐ»ÑŒ
    // Ð²
    // Ð¼Ð¸Ñ€Ð½Ð¾Ð¹
    // Ð·Ð¾Ð½Ðµ.
    public static final int PLEASE_ENTER_YOUR_ID = 86; // Ð’Ð²ÐµÐ´Ð¸Ñ‚Ðµ Ð»Ð¾Ð³Ð¸Ð½.
    public static final int PLEASE_ENTER_YOUR_PASSWORD = 87; // Ð’Ð²ÐµÐ´Ð¸Ñ‚Ðµ Ð¿Ð°Ñ€Ð¾Ð»ÑŒ.
    public static final int YOUR_PROTOCOL_VERSION_IS_DIFFERENT_PLEASE_RESTART_YOUR_CLIENT_AND_RUN_A_FULL_CHECK = 88; // Ð”Ñ€ÑƒÐ³Ð°Ñ�
    // Ð²ÐµÑ€Ñ�Ð¸Ñ�
    // Ð¿Ñ€Ð¾Ñ‚Ð¾ÐºÐ¾Ð»Ð°.
    // ÐŸÐ¾Ð¶Ð°Ð»ÑƒÐ¹Ñ�Ñ‚Ð°,
    // Ð·Ð°Ð²ÐµÑ€ÑˆÐ¸Ñ‚Ðµ
    // Ð¿Ñ€Ð¾Ð³Ñ€Ð°Ð¼Ð¼Ñƒ.
    public static final int YOUR_PROTOCOL_VERSION_IS_DIFFERENT_PLEASE_CONTINUE = 89; // Ð”Ñ€ÑƒÐ³Ð°Ñ�
    // Ð²ÐµÑ€Ñ�Ð¸Ñ�
    // Ð¿Ñ€Ð¾Ñ‚Ð¾ÐºÐ¾Ð»Ð°.
    // ÐŸÐ¾Ð¶Ð°Ð»ÑƒÐ¹Ñ�Ñ‚Ð°,
    // Ð¿Ñ€Ð¾Ð´Ð¾Ð»Ð¶Ð°Ð¹Ñ‚Ðµ.
    public static final int YOU_ARE_UNABLE_TO_CONNECT_TO_THE_SERVER = 90; // Ð�ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾
    // Ñ�Ð¾ÐµÐ´Ð¸Ð½Ð¸Ñ‚ÑŒÑ�Ñ�
    // Ñ�
    // Ñ�ÐµÑ€Ð²ÐµÑ€Ð¾Ð¼.
    public static final int PLEASE_SELECT_YOUR_HAIRSTYLE = 91; // Ð’Ñ‹Ð±ÐµÑ€Ð¸Ñ‚Ðµ
    // Ð¿Ñ€Ð¸Ñ‡ÐµÑ�ÐºÑƒ.
    public static final int S1_HAS_WORN_OFF = 92; // Ð—Ð°ÐºÐ¾Ð½Ñ‡Ð¸Ð»Ñ�Ñ� Ñ�Ñ„Ñ„ÐµÐºÑ‚: $s1.
    public static final int YOU_DO_NOT_HAVE_ENOUGH_SP_FOR_THIS = 93; // Ð�ÐµÐ´Ð¾Ñ�Ñ‚Ð°Ñ‚Ð¾Ñ‡Ð½Ð¾
    // SP.
    public static final int COPYRIGHT_NCSOFT_CORPORATION_ALL_RIGHTS_RESERVED = 94; // 2008
    // Copyright
    // NCsoft
    // Corporation.
    // All
    // Rights
    // Reserved.
    public static final int YOU_HAVE_EARNED_S1_EXPERIENCE_AND_S2_SP = 95; // ÐŸÐ¾Ð»ÑƒÑ‡ÐµÐ½Ð¾:
    // $s1
    // Ð¾Ð¿Ñ‹Ñ‚Ð°
    // Ð¸
    // $s2
    // SP.
    public static final int YOUR_LEVEL_HAS_INCREASED = 96; // Ð£Ñ€Ð¾Ð²ÐµÐ½ÑŒ ÑƒÐ²ÐµÐ»Ð¸Ñ‡ÐµÐ½!
    public static final int THIS_ITEM_CANNOT_BE_MOVED = 97; // Ð�ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾
    // Ð¿ÐµÑ€ÐµÐ¼ÐµÑ�Ñ‚Ð¸Ñ‚ÑŒ
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚.
    public static final int THIS_ITEM_CANNOT_BE_DISCARDED = 98; // Ð�ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾
    // Ð²Ñ‹Ð±Ñ€Ð¾Ñ�Ð¸Ñ‚ÑŒ
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚.
    public static final int THIS_ITEM_CANNOT_BE_TRADED_OR_SOLD = 99; // Ð�ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾
    // Ð¿ÐµÑ€ÐµÐ´Ð°Ñ‚ÑŒ/Ð¿Ñ€Ð¾Ð´Ð°Ñ‚ÑŒ
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚.
    public static final int S1_REQUESTS_A_TRADE_DO_YOU_WANT_TO_TRADE = 100; // $c1
    // Ð¿Ñ€ÐµÐ´Ð»Ð°Ð³Ð°ÐµÑ‚
    // Ñ�Ð´ÐµÐ»ÐºÑƒ.
    // ÐŸÑ€Ð¸Ð½Ñ�Ñ‚ÑŒ?
    public static final int YOU_CANNOT_EXIT_WHILE_IN_COMBAT = 101; // Ð’Ð¾ Ð²Ñ€ÐµÐ¼Ñ�
    // Ð±Ð¾Ñ� Ð½ÐµÐ»ÑŒÐ·Ñ�
    // Ð²Ñ‹Ð¹Ñ‚Ð¸ Ð¸Ð·
    // Ð¸Ð³Ñ€Ñ‹.
    public static final int YOU_CANNOT_RESTART_WHILE_IN_COMBAT = 102; // Ð’Ð¾
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ð±Ð¾Ñ�
    // Ð½ÐµÐ»ÑŒÐ·Ñ�
    // Ð¿ÐµÑ€ÐµÐ·Ð°Ð¿ÑƒÑ�Ñ‚Ð¸Ñ‚ÑŒ
    // Ð¸Ð³Ñ€Ñƒ.
    public static final int THIS_ID_IS_CURRENTLY_LOGGED_IN = 103; // Ð˜Ð³Ñ€Ð¾Ðº Ñ�
    // Ð´Ð°Ð½Ð½Ñ‹Ð¼
    // Ð¸Ð¼ÐµÐ½ÐµÐ¼ ÑƒÐ¶Ðµ
    // Ð² Ð¸Ð³Ñ€Ðµ.
    public static final int YOU_MAY_NOT_EQUIP_ITEMS_WHILE_CASTING_OR_PERFORMING_A_SKILL = 104; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ð½Ð°Ð´ÐµÐ²Ð°Ñ‚ÑŒ
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ñ‹
    // Ð²Ð¾
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ð½Ð¸Ñ�
    // ÑƒÐ¼ÐµÐ½Ð¸Ð¹.
    public static final int YOU_HAVE_INVITED_C1_TO_JOIN_YOUR_PARTY = 105; // $c1
    // Ð¿Ñ€Ð¸Ð³Ð»Ð°ÑˆÐ°ÐµÑ‚
    // Ð’Ð°Ñ�
    // Ð²
    // Ð³Ñ€ÑƒÐ¿Ð¿Ñƒ.
    public static final int YOU_HAVE_JOINED_S1S_PARTY = 106; // Ð’Ñ‹
    // Ð¿Ñ€Ð¸Ñ�Ð¾ÐµÐ´Ð¸Ð½Ð¸Ð»Ð¸Ñ�ÑŒ Ðº
    // Ð³Ñ€ÑƒÐ¿Ð¿Ðµ.
    public static final int S1_HAS_JOINED_THE_PARTY = 107; // $c1 Ð¿Ñ€Ð¸Ñ�Ð¾ÐµÐ´Ð¸Ð½Ñ�ÐµÑ‚Ñ�Ñ�
    // Ðº Ð³Ñ€ÑƒÐ¿Ð¿Ðµ.
    public static final int S1_HAS_LEFT_THE_PARTY = 108; // $c1 Ð²Ñ‹Ñ…Ð¾Ð´Ð¸Ñ‚ Ð¸Ð·
    // Ð³Ñ€ÑƒÐ¿Ð¿Ñ‹.
    public static final int INVALID_TARGET = 109; // Ð�ÐµÐ²ÐµÑ€Ð½Ð°Ñ� Ñ†ÐµÐ»ÑŒ.
    public static final int S1_S2S_EFFECT_CAN_BE_FELT = 110; // Ð’Ñ‹ Ð¾Ñ‰ÑƒÑ‰Ð°ÐµÑ‚Ðµ
    // Ñ�Ñ„Ñ„ÐµÐºÑ‚: $s1.
    public static final int YOUR_SHIELD_DEFENSE_HAS_SUCCEEDED = 111; // Ð’Ñ‹
    // ÑƒÐ´Ð°Ñ‡Ð½Ð¾
    // Ð±Ð»Ð¾ÐºÐ¸Ñ€Ð¾Ð²Ð°Ð»Ð¸
    // ÑƒÐ´Ð°Ñ€.
    public static final int YOU_HAVE_RUN_OUT_OF_ARROWS = 112; // Ð¡Ñ‚Ñ€ÐµÐ»Ñ‹
    // Ð·Ð°ÐºÐ¾Ð½Ñ‡Ð¸Ð»Ð¸Ñ�ÑŒ.
    public static final int S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS = 113; // $s1:
    // Ð½ÐµÐ»ÑŒÐ·Ñ�
    // Ñ�ÐµÐ¹Ñ‡Ð°Ñ�
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒ.
    public static final int YOU_HAVE_ENTERED_THE_SHADOW_OF_THE_MOTHER_TREE = 114; // Ð’Ñ‹
    // Ð·Ð°ÑˆÐ»Ð¸
    // Ð²
    // Ð¢ÐµÐ½ÑŒ
    // Ð”Ñ€ÐµÐ²Ð°
    // Ð–Ð¸Ð·Ð½Ð¸.
    public static final int YOU_HAVE_LEFT_THE_SHADOW_OF_THE_MOTHER_TREE = 115; // Ð’Ñ‹
    // Ð²Ñ‹ÑˆÐ»Ð¸
    // Ð¸Ð·
    // Ð¢ÐµÐ½Ð¸
    // Ð”Ñ€ÐµÐ²Ð°
    // Ð–Ð¸Ð·Ð½Ð¸.
    public static final int YOU_HAVE_ENTERED_A_PEACEFUL_ZONE = 116; // Ð’Ñ‹ Ð·Ð°ÑˆÐ»Ð¸
    // Ð² Ð¼Ð¸Ñ€Ð½ÑƒÑŽ
    // Ð·Ð¾Ð½Ñƒ.
    public static final int YOU_HAVE_LEFT_THE_PEACEFUL_ZONE = 117; // Ð’Ñ‹ Ð²Ñ‹ÑˆÐ»Ð¸
    // Ð¸Ð· Ð¼Ð¸Ñ€Ð½Ð¾Ð¹
    // Ð·Ð¾Ð½Ñ‹.
    public static final int YOU_HAVE_REQUESTED_A_TRADE_WITH_C1 = 118; // Ð’Ñ‹
    // Ð¿Ñ€ÐµÐ´Ð»Ð¾Ð¶Ð¸Ð»Ð¸
    // Ñ�Ð´ÐµÐ»ÐºÑƒ
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ñƒ
    // $c1.
    public static final int C1_HAS_DENIED_YOUR_REQUEST_TO_TRADE = 119; // $c1
    // Ð¾Ñ‚ÐºÐ°Ð·Ñ‹Ð²Ð°ÐµÑ‚Ñ�Ñ�
    // Ð¾Ñ‚
    // Ñ�Ð´ÐµÐ»ÐºÐ¸.
    public static final int YOU_BEGIN_TRADING_WITH_C1 = 120; // $c1: Ð¾Ð±Ð¼ÐµÐ½
    // Ð½Ð°Ñ‡Ð°Ð»Ñ�Ñ�.
    public static final int C1_HAS_CONFIRMED_THE_TRADE = 121; // $c1
    // Ð¿Ð¾Ð´Ñ‚Ð²ÐµÑ€Ð¶Ð´Ð°ÐµÑ‚
    // Ñ�Ð´ÐµÐ»ÐºÑƒ.
    public static final int YOU_MAY_NO_LONGER_ADJUST_ITEMS_IN_THE_TRADE_BECAUSE_THE_TRADE_HAS_BEEN_CONFIRMED = 122; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ð¸Ð·Ð¼ÐµÐ½Ñ�Ñ‚ÑŒ
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ñ‹,
    // ÐµÑ�Ð»Ð¸
    // Ñ�Ð´ÐµÐ»ÐºÐ°
    // Ð¿Ð¾Ð´Ñ‚Ð²ÐµÑ€Ð¶Ð´ÐµÐ½Ð°.
    public static final int YOUR_TRADE_IS_SUCCESSFUL = 123; // ÐžÐ±Ð¼ÐµÐ½ ÑƒÑ�Ð¿ÐµÑˆÐ½Ð¾
    // Ð·Ð°Ð²ÐµÑ€ÑˆÐµÐ½.
    public static final int C1_HAS_CANCELLED_THE_TRADE = 124; // $c1 Ð¾Ñ‚Ð¼ÐµÐ½Ñ�ÐµÑ‚
    // Ñ�Ð´ÐµÐ»ÐºÑƒ.
    public static final int DO_YOU_WISH_TO_EXIT_THE_GAME = 125; // Ð—Ð°Ð²ÐµÑ€ÑˆÐ¸Ñ‚ÑŒ
    // Ð¸Ð³Ñ€Ñƒ?
    public static final int DO_YOU_WISH_TO_EXIT_TO_THE_CHARACTER_SELECT_SCREEN = 126; // Ð’Ñ‹Ð¹Ñ‚Ð¸
    // Ð²
    // Ð¼ÐµÐ½ÑŽ
    // Ð²Ñ‹Ð±Ð¾Ñ€Ð°
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°?
    public static final int YOU_HAVE_BEEN_DISCONNECTED_FROM_THE_SERVER_PLEASE_LOGIN_AGAIN = 127; // Ð¡Ð²Ñ�Ð·ÑŒ
    // Ñ�
    // Ñ�ÐµÑ€Ð²ÐµÑ€Ð¾Ð¼
    // Ð¿Ñ€ÐµÑ€Ð²Ð°Ð»Ð°Ñ�ÑŒ.
    // ÐŸÐ¾Ð¶Ð°Ð»ÑƒÐ¹Ñ�Ñ‚Ð°,
    // Ð·Ð°Ð¹Ð´Ð¸Ñ‚Ðµ
    // Ð²
    // Ð¸Ð³Ñ€Ñƒ
    // Ð¿Ð¾Ð·Ð¶Ðµ.
    public static final int YOUR_CHARACTER_CREATION_HAS_FAILED = 128; // Ð�Ðµ
    // ÑƒÐ´Ð°Ð»Ð¾Ñ�ÑŒ
    // Ñ�Ð¾Ð·Ð´Ð°Ñ‚ÑŒ
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°.
    public static final int YOUR_INVENTORY_IS_FULL = 129; // Ð’Ñ�Ðµ Ñ�Ñ‡ÐµÐ¹ÐºÐ¸ Ð²
    // Ð¸Ð½Ð²ÐµÐ½Ñ‚Ð°Ñ€Ðµ Ð·Ð°Ð½Ñ�Ñ‚Ñ‹.
    public static final int YOUR_WAREHOUSE_IS_FULL = 130; // Ð’Ñ�Ðµ Ñ�Ñ‡ÐµÐ¹ÐºÐ¸ Ð²
    // Ñ…Ñ€Ð°Ð½Ð¸Ð»Ð¸Ñ‰Ðµ Ð·Ð°Ð½Ñ�Ñ‚Ñ‹.
    public static final int S1_HAS_LOGGED_IN = 131; // $s1 Ð·Ð°Ñ…Ð¾Ð´Ð¸Ñ‚ Ð² Ð¸Ð³Ñ€Ñƒ.
    public static final int S1_HAS_BEEN_ADDED_TO_YOUR_FRIEND_LIST = 132; // $s1
    // Ð´Ð¾Ð±Ð°Ð²Ð»Ñ�ÐµÑ‚Ñ�Ñ�
    // Ð²
    // Ñ�Ð¿Ð¸Ñ�Ð¾Ðº
    // Ð´Ñ€ÑƒÐ·ÐµÐ¹.
    public static final int S1_HAS_BEEN_REMOVED_FROM_YOUR_FRIEND_LIST = 133; // $s1
    // ÑƒÐ´Ð°Ð»Ñ�ÐµÑ‚Ñ�Ñ�
    // Ð¸Ð·
    // Ñ�Ð¿Ð¸Ñ�ÐºÐ°
    // Ð´Ñ€ÑƒÐ·ÐµÐ¹.
    public static final int PLEASE_CHECK_YOUR_FRIEND_LIST_AGAIN = 134; // ÐŸÐ¾Ð¶Ð°Ð»ÑƒÐ¹Ñ�Ñ‚Ð°,
    // Ð¿Ñ€Ð¾Ð²ÐµÑ€ÑŒÑ‚Ðµ
    // Ñ�Ð¿Ð¸Ñ�Ð¾Ðº
    // Ð´Ñ€ÑƒÐ·ÐµÐ¹.
    public static final int S1_DID_NOT_REPLY_TO_YOUR_INVITATION_PARTY_INVITATION_HAS_BEEN_CANCELLED = 135; // $c1
    // Ð½Ðµ
    // Ð¾Ñ‚Ð²ÐµÑ‡Ð°ÐµÑ‚
    // Ð½Ð°
    // Ð’Ð°ÑˆÐµ
    // Ð¿Ñ€Ð¸Ð³Ð»Ð°ÑˆÐµÐ½Ð¸Ðµ.
    public static final int YOU_HAVE_NOT_REPLIED_TO_C1S_INVITATION_THE_OFFER_HAS_BEEN_CANCELLED = 136; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¾Ñ‚Ð²ÐµÑ‚Ð¸Ð»Ð¸
    // Ð½Ð°
    // Ð¿Ñ€Ð¸Ð³Ð»Ð°ÑˆÐµÐ½Ð¸Ðµ
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°
    // $c1.
    public static final int THERE_ARE_NO_MORE_ITEMS_IN_THE_SHORTCUT = 137; // Ð‘Ð¾Ð»ÑŒÑˆÐµ
    // Ð½ÐµÑ‚
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ð¾Ð²,
    // Ð¿Ñ€Ð¸Ð²Ñ�Ð·Ð°Ð½Ð½Ñ‹Ñ…
    // Ðº
    // Ñ�Ñ€Ð»Ñ‹ÐºÑƒ.
    public static final int DESIGNATE_SHORTCUT = 138; // Ð�Ðµ ÑƒÐ´Ð°Ð»Ð¾Ñ�ÑŒ Ð½Ð°Ð·Ð½Ð°Ñ‡Ð¸Ñ‚ÑŒ
    // Ñ�Ñ€Ð»Ñ‹Ðº.
    public static final int C1_HAS_RESISTED_YOUR_S2 = 139; // Ð£Ð¼ÐµÐ½Ð¸Ðµ $s2 Ð½Ðµ
    // Ð¿Ð¾Ð´ÐµÐ¹Ñ�Ñ‚Ð²Ð¾Ð²Ð°Ð»Ð¾ Ð½Ð°
    // Ñ†ÐµÐ»ÑŒ $c1.
    public static final int YOUR_SKILL_WAS_REMOVED_DUE_TO_A_LACK_OF_MP = 140; // Ð�ÐµÐ´Ð¾Ñ�Ñ‚Ð°Ñ‚Ð¾Ñ‡Ð½Ð¾
    // MP,
    // Ð´ÐµÐ¹Ñ�Ñ‚Ð²Ð¸Ðµ
    // ÑƒÐ¼ÐµÐ½Ð¸Ñ�
    // Ð¿Ñ€ÐµÐºÑ€Ð°Ñ‰ÐµÐ½Ð¾.
    public static final int ONCE_THE_TRADE_IS_CONFIRMED_THE_ITEM_CANNOT_BE_MOVED_AGAIN = 141; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ð¸Ð·Ð¼ÐµÐ½Ñ�Ñ‚ÑŒ
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ñ‹,
    // ÐµÑ�Ð»Ð¸
    // Ñ�Ð´ÐµÐ»ÐºÐ°
    // Ð¿Ð¾Ð´Ñ‚Ð²ÐµÑ€Ð¶Ð´ÐµÐ½Ð°.
    public static final int YOU_ARE_ALREADY_TRADING_WITH_SOMEONE = 142; // Ð’Ñ‹
    // ÑƒÐ¶Ðµ
    // Ð¾Ð±Ð¼ÐµÐ½Ð¸Ð²Ð°ÐµÑ‚ÐµÑ�ÑŒ
    // Ñ�
    // Ð´Ñ€ÑƒÐ³Ð¸Ð¼
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶ÐµÐ¼.
    public static final int C1_IS_ALREADY_TRADING_WITH_ANOTHER_PERSON_PLEASE_TRY_AGAIN_LATER = 143; // $c1
    // Ð¾Ð±Ð¼ÐµÐ½Ð¸Ð²Ð°ÐµÑ‚Ñ�Ñ�
    // Ñ�
    // Ð´Ñ€ÑƒÐ³Ð¸Ð¼
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶ÐµÐ¼.
    public static final int THAT_IS_THE_INCORRECT_TARGET = 144; // Ð�ÐµÐ¿Ñ€Ð°Ð²Ð¸Ð»ÑŒÐ½Ð°Ñ�
    // Ñ†ÐµÐ»ÑŒ.
    public static final int THAT_PLAYER_IS_NOT_ONLINE = 145; // Ð­Ñ‚Ð¾Ð³Ð¾ Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°
    // Ð½ÐµÑ‚ Ð² Ð¸Ð³Ñ€Ðµ.
    public static final int CHATTING_IS_NOW_PERMITTED = 146; // Ð‘Ð»Ð¾ÐºÐ¸Ñ€Ð¾Ð²ÐºÐ° Ñ‡Ð°Ñ‚Ð°
    // Ñ�Ð½Ñ�Ñ‚Ð°.
    public static final int CHATTING_IS_CURRENTLY_PROHIBITED = 147; // Ð§Ð°Ñ‚
    // Ð·Ð°Ð±Ð»Ð¾ÐºÐ¸Ñ€Ð¾Ð²Ð°Ð½.
    public static final int YOU_CANNOT_USE_QUEST_ITEMS = 148; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒ
    // ÐºÐ²ÐµÑ�Ñ‚Ð¾Ð²Ñ‹Ðµ
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ñ‹.
    public static final int YOU_CANNOT_PICK_UP_OR_USE_ITEMS_WHILE_TRADING = 149; // Ð’Ð¾
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ñ�Ð´ÐµÐ»ÐºÐ¸
    // Ð½ÐµÐ»ÑŒÐ·Ñ�
    // Ð¿Ð¾Ð´Ð½Ð¸Ð¼Ð°Ñ‚ÑŒ/Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒ
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ñ‹.
    public static final int YOU_CANNOT_DISCARD_OR_DESTROY_AN_ITEM_WHILE_TRADING_AT_A_PRIVATE_STORE = 150; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ð²Ñ‹Ð±Ñ€Ð¾Ñ�Ð¸Ñ‚ÑŒ/ÑƒÐ½Ð¸Ñ‡Ñ‚Ð¾Ð¶Ð¸Ñ‚ÑŒ
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ñ‹
    // Ð²Ð¾
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ñ�Ð´ÐµÐ»ÐºÐ¸.
    public static final int THAT_IS_TOO_FAR_FROM_YOU_TO_DISCARD = 151; // Ð¡Ð»Ð¸ÑˆÐºÐ¾Ð¼
    // Ð´Ð°Ð»ÐµÐºÐ¾,
    // Ð½ÐµÐ»ÑŒÐ·Ñ�
    // Ð²Ñ‹Ð±Ñ€Ð¾Ñ�Ð¸Ñ‚ÑŒ
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚.
    public static final int YOU_HAVE_INVITED_WRONG_TARGET = 152; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ð¿Ñ€Ð¸Ð³Ð»Ð°Ñ�Ð¸Ñ‚ÑŒ
    // Ð²Ñ‹Ð±Ñ€Ð°Ð½Ð½ÑƒÑŽ
    // Ñ†ÐµÐ»ÑŒ.
    public static final int S1_IS_BUSY_PLEASE_TRY_AGAIN_LATER = 153; // $c1
    // Ð·Ð°Ð½Ð¸Ð¼Ð°ÐµÑ‚Ñ�Ñ�
    // Ñ‡ÐµÐ¼-Ñ‚Ð¾
    // Ð´Ñ€ÑƒÐ³Ð¸Ð¼.
    public static final int ONLY_THE_LEADER_CAN_GIVE_OUT_INVITATIONS = 154; // ÐŸÑ€Ð¸Ð³Ð»Ð°ÑˆÐ°Ñ‚ÑŒ
    // Ð²
    // Ð³Ñ€ÑƒÐ¿Ð¿Ñƒ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ñ‚Ð¾Ð»ÑŒÐºÐ¾
    // ÐµÐµ
    // Ð»Ð¸Ð´ÐµÑ€.
    public static final int PARTY_IS_FULL = 155; // Ð“Ñ€ÑƒÐ¿Ð¿Ð° Ð·Ð°Ð¿Ð¾Ð»Ð½ÐµÐ½Ð°.
    public static final int DRAIN_WAS_ONLY_HALF_SUCCESSFUL = 156; // ÐŸÐ¾Ð³Ð»Ð¾Ñ‰ÐµÐ½Ð¸Ðµ
    // ÑƒÐ´Ð°Ñ‡Ð½Ð¾
    // Ñ‚Ð¾Ð»ÑŒÐºÐ¾ Ð½Ð°
    // 50%.
    @Deprecated
    public static final int YOU_RESISTED_S1S_DRAIN = 157; // Ð·Ð°Ð¼ÐµÐ½ÐµÐ½Ð¾
    // C1_RESISTED_C2S_DRAIN
    // = 2267
    @Deprecated
    public static final int ATTACK_FAILED = 158; // Ð·Ð°Ð¼ÐµÐ½ÐµÐ½Ð¾ C1S_ATTACK_FAILED =
    // 2268
    @Deprecated
    public static final int RESISTED_AGAINST_S1S_MAGIC = 159; // Ð·Ð°Ð¼ÐµÐ½ÐµÐ½Ð¾
    // C1_RESISTED_C2S_MAGIC
    // = 2269
    public static final int S1_IS_A_MEMBER_OF_ANOTHER_PARTY_AND_CANNOT_BE_INVITED = 160; // $c1
    // ÑƒÐ¶Ðµ
    // Ñ�Ð¾Ñ�Ñ‚Ð¾Ð¸Ñ‚
    // Ð²
    // Ð´Ñ€ÑƒÐ³Ð¾Ð¹
    // Ð³Ñ€ÑƒÐ¿Ð¿Ðµ.
    public static final int THAT_PLAYER_IS_NOT_CURRENTLY_ONLINE = 161; // ÐŸÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°
    // Ð½ÐµÑ‚ Ð²
    // Ð¸Ð³Ñ€Ðµ.
    public static final int WAREHOUSE_IS_TOO_FAR = 162; // Ð¥Ñ€Ð°Ð½Ð¸Ð»Ð¸Ñ‰Ðµ Ð½Ð°Ñ…Ð¾Ð´Ð¸Ñ‚Ñ�Ñ�
    // Ñ�Ð»Ð¸ÑˆÐºÐ¾Ð¼ Ð´Ð°Ð»ÐµÐºÐ¾.
    public static final int YOU_CANNOT_DESTROY_IT_BECAUSE_THE_NUMBER_IS_INCORRECT = 163; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // ÑƒÐ½Ð¸Ñ‡Ñ‚Ð¾Ð¶Ð¸Ñ‚ÑŒ
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚,
    // Ð·Ð°Ð´Ð°Ð½Ð¾
    // Ð½ÐµÐ²ÐµÑ€Ð½Ð¾Ðµ
    // ÐºÐ¾Ð»Ð¸Ñ‡ÐµÑ�Ñ‚Ð²Ð¾.
    public static final int WAITING_FOR_ANOTHER_REPLY = 164; // ÐžÐ¶Ð¸Ð´Ð°Ð½Ð¸Ðµ Ð¾Ñ‚Ð²ÐµÑ‚Ð°.
    public static final int YOU_CANNOT_ADD_YOURSELF_TO_YOUR_OWN_FRIEND_LIST = 165; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ð´Ð¾Ð±Ð°Ð²Ð¸Ñ‚ÑŒ
    // Ñ�ÐµÐ±Ñ�
    // Ð²
    // Ñ�Ð¿Ð¸Ñ�Ð¾Ðº
    // Ð´Ñ€ÑƒÐ·ÐµÐ¹.
    public static final int FRIEND_LIST_IS_NOT_READY_YET_PLEASE_REGISTER_AGAIN_LATER = 166; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ñ�Ð¾Ð·Ð´Ð°Ñ‚ÑŒ
    // Ñ�Ð¿Ð¸Ñ�Ð¾Ðº
    // Ð´Ñ€ÑƒÐ·ÐµÐ¹.
    // ÐŸÐ¾Ð¿Ñ€Ð¾Ð±ÑƒÐ¹Ñ‚Ðµ
    // Ð¿Ð¾Ð·Ð¶Ðµ.
    public static final int S1_IS_ALREADY_ON_YOUR_FRIEND_LIST = 167; // $c1 ÑƒÐ¶Ðµ
    // Ð½Ð°Ñ…Ð¾Ð´Ð¸Ñ‚Ñ�Ñ�
    // Ð² Ñ�Ð¿Ð¸Ñ�ÐºÐµ
    // Ð´Ñ€ÑƒÐ·ÐµÐ¹.
    public static final int S1_HAS_REQUESTED_TO_BECOME_FRIENDS = 168; // $c1
    // Ñ…Ð¾Ñ‡ÐµÑ‚
    // Ð´Ð¾Ð±Ð°Ð²Ð¸Ñ‚ÑŒ
    // Ð’Ð°Ñ� Ð²
    // Ñ�Ð¿Ð¸Ñ�Ð¾Ðº
    // Ð´Ñ€ÑƒÐ·ÐµÐ¹.
    public static final int ACCEPT_FRIENDSHIP_0_1__1_TO_ACCEPT_0_TO_DENY = 169; // ÐŸÑ€Ð¸Ð½Ñ�Ñ‚ÑŒ
    // Ð´Ñ€ÑƒÐ¶Ð±Ñƒ?
    // 0/1
    // (1
    // -
    // Ð´Ð°,
    // 0
    // -
    // Ð½ÐµÑ‚)
    public static final int THE_USER_WHO_REQUESTED_TO_BECOME_FRIENDS_IS_NOT_FOUND_IN_THE_GAME = 170; // ÐŸÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°,
    // ÐºÐ¾Ñ‚Ð¾Ñ€Ð¾Ð³Ð¾
    // Ð’Ñ‹
    // Ñ…Ð¾Ñ‚Ð¸Ñ‚Ðµ
    // Ð´Ð¾Ð±Ð°Ð²Ð¸Ñ‚ÑŒ
    // Ð²
    // Ð´Ñ€ÑƒÐ·ÑŒÑ�,
    // Ð½ÐµÑ‚
    // Ð²
    // Ð¸Ð³Ñ€Ðµ.
    public static final int S1_IS_NOT_ON_YOUR_FRIEND_LIST = 171; // $c1 Ð½Ðµ
    // Ð²Ñ…Ð¾Ð´Ð¸Ñ‚ Ð² Ð’Ð°Ñˆ
    // Ñ�Ð¿Ð¸Ñ�Ð¾Ðº
    // Ð´Ñ€ÑƒÐ·ÐµÐ¹.
    public static final int YOU_LACK_THE_FUNDS_NEEDED_TO_PAY_FOR_THIS_TRANSACTION = 172; // Ð�ÐµÐ´Ð¾Ñ�Ñ‚Ð°Ñ‚Ð¾Ñ‡Ð½Ð¾
    // Ð´ÐµÐ½ÐµÐ³
    // Ð´Ð»Ñ�
    // Ð¾Ð¿Ð»Ð°Ñ‚Ñ‹
    // Ñ…Ñ€Ð°Ð½Ð¸Ð»Ð¸Ñ‰Ð°.
    public static final int YOU_LACK_THE_FUNDS_NEEDED_TO_PAY_FOR_THIS_TRANSACTION_2 = 173; // Ð�ÐµÐ´Ð¾Ñ�Ñ‚Ð°Ñ‚Ð¾Ñ‡Ð½Ð¾
    // Ð´ÐµÐ½ÐµÐ³
    // Ð´Ð»Ñ�
    // Ð¾Ð¿Ð»Ð°Ñ‚Ñ‹
    // ÑƒÑ�Ð»ÑƒÐ³Ð¸.
    public static final int THE_PERSONS_INVENTORY_IS_FULL = 174; // Ð£ Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°
    // Ð² Ð¸Ð½Ð²ÐµÐ½Ñ‚Ð°Ñ€Ðµ
    // Ð½ÐµÑ‚ Ð¼ÐµÑ�Ñ‚Ð°.
    public static final int HP_WAS_FULLY_RECOVERED_AND_SKILL_WAS_REMOVED = 175; // HP
    // Ð¿Ð¾Ð»Ð½Ð¾Ñ�Ñ‚ÑŒÑŽ
    // Ð²Ð¾Ñ�Ñ�Ñ‚Ð°Ð½Ð¾Ð²Ð¸Ð»Ð¸Ñ�ÑŒ,
    // Ð¸
    // ÑƒÐ¼ÐµÐ½Ð¸Ðµ
    // Ð´ÐµÐ°ÐºÑ‚Ð¸Ð²Ð¸Ñ€Ð¾Ð²Ð°Ð»Ð¾Ñ�ÑŒ.
    public static final int THE_PERSON_IS_IN_A_MESSAGE_REFUSAL_MODE = 176; // Ð˜Ð³Ñ€Ð¾Ðº
    // Ð·Ð°Ð±Ð»Ð¾ÐºÐ¸Ñ€Ð¾Ð²Ð°Ð»
    // Ð¿Ñ€Ð¸Ð½Ñ�Ñ‚Ð¸Ðµ
    // Ð»Ð¸Ñ‡Ð½Ñ‹Ñ…
    // Ñ�Ð¾Ð¾Ð±Ñ‰ÐµÐ½Ð¸Ð¹.
    public static final int MESSAGE_REFUSAL_MODE = 177; // ÐŸÑ€Ð¸Ð½Ñ�Ñ‚Ð¸Ðµ Ñ�Ð¾Ð¾Ð±Ñ‰ÐµÐ½Ð¸Ð¹
    // Ð·Ð°Ð±Ð»Ð¾ÐºÐ¸Ñ€Ð¾Ð²Ð°Ð½Ð¾.
    public static final int MESSAGE_ACCEPTANCE_MODE = 178; // ÐŸÑ€Ð¸Ð½Ñ�Ñ‚Ð¸Ðµ Ñ�Ð¾Ð¾Ð±Ñ‰ÐµÐ½Ð¸Ð¹
    // Ñ€Ð°Ð·Ð±Ð»Ð¾ÐºÐ¸Ñ€Ð¾Ð²Ð°Ð½Ð¾.
    public static final int YOU_CANNOT_DISCARD_THOSE_ITEMS_HERE = 179; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ð²Ñ‹Ð±Ñ€Ð¾Ñ�Ð¸Ñ‚ÑŒ
    // Ð·Ð´ÐµÑ�ÑŒ
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚.
    public static final int YOU_HAVE_S1_DAY_S_LEFT_UNTIL_DELETION_DO_YOU_WANT_TO_CANCEL_DELETION = 180; // Ð”Ð½ÐµÐ¹
    // Ð´Ð¾
    // ÑƒÐ´Ð°Ð»ÐµÐ½Ð¸Ñ�:
    // $s1.
    // ÐžÑ‚Ð¼ÐµÐ½Ð¸Ñ‚ÑŒ
    // ÑƒÐ´Ð°Ð»ÐµÐ½Ð¸Ðµ?
    public static final int CANNOT_SEE_TARGET = 181; // Ð¦ÐµÐ»Ð¸ Ð½Ðµ Ð²Ð¸Ð´Ð½Ð¾.
    public static final int DO_YOU_WANT_TO_QUIT_THE_CURRENT_QUEST = 182; // ÐžÑ‚Ð¼ÐµÐ½Ð¸Ñ‚ÑŒ
    // Ñ‚ÐµÐºÑƒÑ‰Ð¸Ð¹
    // ÐºÐ²ÐµÑ�Ñ‚?
    public static final int THERE_ARE_TOO_MANY_USERS_ON_THE_SERVER_PLEASE_TRY_AGAIN_LATER = 183; // Ð�Ð°
    // Ñ�ÐµÑ€Ð²ÐµÑ€Ðµ
    // Ñ�Ð»Ð¸ÑˆÐºÐ¾Ð¼
    // Ð¼Ð½Ð¾Ð³Ð¾
    // Ð¸Ð³Ñ€Ð¾ÐºÐ¾Ð².
    // ÐŸÐ¾Ð¿Ñ€Ð¾Ð±ÑƒÐ¹Ñ‚Ðµ
    // Ð·Ð°Ð¹Ñ‚Ð¸
    // Ð¿Ð¾Ð·Ð¶Ðµ.
    public static final int PLEASE_TRY_AGAIN_LATER = 184; // ÐŸÐ¾Ð¿Ñ€Ð¾Ð±ÑƒÐ¹Ñ‚Ðµ Ð·Ð°Ð¹Ñ‚Ð¸
    // Ð¿Ð¾Ð·Ð¶Ðµ.
    public static final int SELECT_USER_TO_INVITE_TO_YOUR_PARTY = 185; // Ð’Ñ‹Ð±ÐµÑ€Ð¸Ñ‚Ðµ
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°,
    // ÐºÐ¾Ñ‚Ð¾Ñ€Ð¾Ð³Ð¾
    // Ñ…Ð¾Ñ‚Ð¸Ñ‚Ðµ
    // Ð¿Ñ€Ð¸Ð³Ð»Ð°Ñ�Ð¸Ñ‚ÑŒ
    // Ð²
    // Ð³Ñ€ÑƒÐ¿Ð¿Ñƒ.
    public static final int SELECT_USER_TO_INVITE_TO_YOUR_CLAN = 186; // Ð’Ñ‹Ð±ÐµÑ€Ð¸Ñ‚Ðµ
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°,
    // ÐºÐ¾Ñ‚Ð¾Ñ€Ð¾Ð³Ð¾
    // Ñ…Ð¾Ñ‚Ð¸Ñ‚Ðµ
    // Ð¿Ñ€Ð¸Ð³Ð»Ð°Ñ�Ð¸Ñ‚ÑŒ
    // Ð² ÐºÐ»Ð°Ð½.
    public static final int SELECT_USER_TO_EXPEL = 187; // Ð’Ñ‹Ð±ÐµÑ€Ð¸Ñ‚Ðµ Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°,
    // ÐºÐ¾Ñ‚Ð¾Ñ€Ð¾Ð³Ð¾ Ñ…Ð¾Ñ‚Ð¸Ñ‚Ðµ
    // Ð¸Ñ�ÐºÐ»ÑŽÑ‡Ð¸Ñ‚ÑŒ.
    public static final int CREATE_CLAN_NAME = 188; // Ð’Ð²ÐµÐ´Ð¸Ñ‚Ðµ Ð½Ð°Ð·Ð²Ð°Ð½Ð¸Ðµ ÐºÐ»Ð°Ð½Ð°.
    public static final int CLAN_HAS_BEEN_CREATED = 189; // ÐšÐ»Ð°Ð½ Ñ�Ð¾Ð·Ð´Ð°Ð½.
    public static final int YOU_HAVE_FAILED_TO_CREATE_A_CLAN = 190; // Ð�Ðµ
    // ÑƒÐ´Ð°Ð»Ð¾Ñ�ÑŒ
    // Ñ�Ð¾Ð·Ð´Ð°Ñ‚ÑŒ
    // ÐºÐ»Ð°Ð½.
    public static final int CLAN_MEMBER_S1_HAS_BEEN_EXPELLED = 191; // $s1
    // Ð¸Ñ�ÐºÐ»ÑŽÑ‡Ð°ÐµÑ‚Ñ�Ñ�
    // Ð¸Ð· ÐºÐ»Ð°Ð½Ð°.
    public static final int YOU_HAVE_FAILED_TO_EXPEL_S1_FROM_THE_CLAN = 192; // $s1:
    // Ð¸Ñ�ÐºÐ»ÑŽÑ‡ÐµÐ½Ð¸Ðµ
    // Ð½Ðµ
    // ÑƒÐ´Ð°Ð»Ð¾Ñ�ÑŒ.
    public static final int CLAN_HAS_DISPERSED = 193; // ÐšÐ»Ð°Ð½ Ñ€Ð°Ñ�Ñ„Ð¾Ñ€Ð¼Ð¸Ñ€Ð¾Ð²Ð°Ð½.
    public static final int YOU_HAVE_FAILED_TO_DISPERSE_THE_CLAN = 194; // Ð�Ðµ
    // ÑƒÐ´Ð°Ð»Ð¾Ñ�ÑŒ
    // Ñ€Ð°Ñ�Ñ„Ð¾Ñ€Ð¼Ð¸Ñ€Ð¾Ð²Ð°Ñ‚ÑŒ
    // ÐºÐ»Ð°Ð½.
    public static final int ENTERED_THE_CLAN = 195; // Ð’Ñ‹ Ð²Ñ�Ñ‚ÑƒÐ¿Ð¸Ð»Ð¸ Ð² ÐºÐ»Ð°Ð½.
    public static final int S1_REFUSED_TO_JOIN_THE_CLAN = 196; // $s1
    // Ð¾Ñ‚ÐºÐ°Ð·Ñ‹Ð²Ð°ÐµÑ‚Ñ�Ñ�
    // Ð²Ñ�Ñ‚ÑƒÐ¿Ð¸Ñ‚ÑŒ Ð²
    // ÐºÐ»Ð°Ð½.
    public static final int WITHDRAWN_FROM_THE_CLAN = 197; // Ð’Ñ‹ Ð¿Ð¾ÐºÐ¸Ð½ÑƒÐ»Ð¸ ÐºÐ»Ð°Ð½.
    public static final int YOU_HAVE_FAILED_TO_WITHDRAW_FROM_THE_S1_CLAN = 198; // Ð�Ðµ
    // ÑƒÐ´Ð°Ð»Ð¾Ñ�ÑŒ
    // Ð¿Ð¾ÐºÐ¸Ð½ÑƒÑ‚ÑŒ
    // ÐºÐ»Ð°Ð½
    // $s1.
    public static final int YOU_HAVE_RECENTLY_BEEN_DISMISSED_FROM_A_CLAN_YOU_ARE_NOT_ALLOWED_TO_JOIN_ANOTHER_CLAN_FOR_24_HOURS = 199; // Ð’Ñ‹
    // Ð¸Ñ�ÐºÐ»ÑŽÑ‡ÐµÐ½Ñ‹
    // Ð¸Ð·
    // ÐºÐ»Ð°Ð½Ð°
    // Ð¸
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð²Ñ�Ñ‚ÑƒÐ¿Ð¸Ñ‚ÑŒ
    // Ð²
    // Ð´Ñ€ÑƒÐ³Ð¾Ð¹
    // Ñ‚Ð¾Ð»ÑŒÐºÐ¾
    // Ñ‡ÐµÑ€ÐµÐ·
    // 24
    // Ñ‡.
    public static final int YOU_HAVE_WITHDRAWN_FROM_THE_PARTY = 200; // Ð’Ñ‹ Ð²Ñ‹ÑˆÐ»Ð¸
    // Ð¸Ð·
    // Ð³Ñ€ÑƒÐ¿Ð¿Ñ‹.
    public static final int S1_WAS_EXPELLED_FROM_THE_PARTY = 201; // $c1
    // Ð¸Ñ�ÐºÐ»ÑŽÑ‡Ð°ÐµÑ‚Ñ�Ñ�
    // Ð¸Ð· Ð³Ñ€ÑƒÐ¿Ð¿Ñ‹.
    public static final int YOU_HAVE_BEEN_EXPELLED_FROM_THE_PARTY = 202; // Ð’Ð°Ñ�
    // Ð¸Ñ�ÐºÐ»ÑŽÑ‡Ð¸Ð»Ð¸
    // Ð¸Ð·
    // Ð³Ñ€ÑƒÐ¿Ð¿Ñ‹.
    public static final int THE_PARTY_HAS_DISPERSED = 203; // Ð“Ñ€ÑƒÐ¿Ð¿Ð°
    // Ñ€Ð°Ñ�Ñ„Ð¾Ñ€Ð¼Ð¸Ñ€Ð¾Ð²Ð°Ð½Ð°.
    public static final int INCORRECT_NAME_PLEASE_TRY_AGAIN = 204; // Ð�ÐµÐ¿Ñ€Ð°Ð²Ð¸Ð»ÑŒÐ½Ð¾Ðµ
    // Ð¸Ð¼Ñ�.
    // ÐŸÐ¾Ð¿Ñ€Ð¾Ð±ÑƒÐ¹Ñ‚Ðµ
    // ÐµÑ‰Ðµ Ñ€Ð°Ð·.
    public static final int INCORRECT_CHARACTER_NAME_PLEASE_ASK_THE_GM = 205; // Ð�ÐµÐ¿Ñ€Ð°Ð²Ð¸Ð»ÑŒÐ½Ð¾Ðµ
    // Ð¸Ð¼Ñ�
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°.
    // ÐŸÐ¾Ð¿Ñ€Ð¾Ð±ÑƒÐ¹Ñ‚Ðµ
    // ÐµÑ‰Ðµ
    // Ñ€Ð°Ð·.
    public static final int ENTER_NAME_OF_CLAN_TO_DECLARE_WAR_ON = 206; // Ð’Ð²ÐµÐ´Ð¸Ñ‚Ðµ
    // Ð½Ð°Ð·Ð²Ð°Ð½Ð¸Ðµ
    // ÐºÐ»Ð°Ð½Ð°,
    // ÐºÐ¾Ñ‚Ð¾Ñ€Ð¾Ð¼Ñƒ
    // Ñ…Ð¾Ñ‚Ð¸Ñ‚Ðµ
    // Ð¾Ð±ÑŠÑ�Ð²Ð¸Ñ‚ÑŒ
    // Ð²Ð¾Ð¹Ð½Ñƒ.
    public static final int S2_OF_THE_S1_CLAN_REQUESTS_DECLARATION_OF_WAR_DO_YOU_ACCEPT = 207; // $s2
    // Ð¸Ð·
    // ÐºÐ»Ð°Ð½Ð°
    // $s1
    // Ð¾Ð±ÑŠÑ�Ð²Ð»Ñ�ÐµÑ‚
    // Ð’Ð°Ð¼
    // Ð²Ð¾Ð¹Ð½Ñƒ.
    // Ð¡Ð¾Ð³Ð»Ð°Ñ�Ð¸Ñ‚ÑŒÑ�Ñ�?
    public static final int PLEASE_INCLUDE_FILE_TYPE_WHEN_ENTERING_FILE_PATH = 208; // Ð£ÐºÐ°Ð¶Ð¸Ñ‚Ðµ
    // Ñ€Ð°Ñ�ÑˆÐ¸Ñ€ÐµÐ½Ð¸Ðµ
    // Ñ„Ð°Ð¹Ð»Ð°
    // Ð¸
    // Ð¿ÑƒÑ‚ÑŒ
    // Ðº
    // Ð½ÐµÐ¼Ñƒ.
    public static final int THE_SIZE_OF_THE_IMAGE_FILE_IS_DIFFERENT_PLEASE_ADJUST_TO_16_12 = 209; // Ð�ÐµÐ²ÐµÑ€Ð½Ñ‹Ð¹
    // Ñ€Ð°Ð·Ð¼ÐµÑ€
    // Ñ€Ð¸Ñ�ÑƒÐ½ÐºÐ°.
    // ÐŸÐ¾Ð¶Ð°Ð»ÑƒÐ¹Ñ�Ñ‚Ð°,
    // Ð¿Ð¾Ð¼ÐµÐ½Ñ�Ð¹Ñ‚Ðµ
    // Ñ€Ð°Ð·Ð¼ÐµÑ€
    // Ð½Ð°
    // 16*12.
    public static final int CANNOT_FIND_FILE_PLEASE_ENTER_PRECISE_PATH = 210; // Ð¤Ð°Ð¹Ð»
    // Ð½Ðµ
    // Ð½Ð°Ð¹Ð´ÐµÐ½.
    // ÐŸÐ¾Ð¶Ð°Ð»ÑƒÐ¹Ñ�Ñ‚Ð°,
    // Ð²Ð²ÐµÐ´Ð¸Ñ‚Ðµ
    // Ð·Ð°Ð½Ð¾Ð²Ð¾
    // Ñ‚Ð¾Ñ‡Ð½Ñ‹Ð¹
    // Ð¿ÑƒÑ‚ÑŒ
    // Ðº
    // Ð½ÐµÐ¼Ñƒ.
    public static final int CAN_ONLY_REGISTER_16_12_SIZED_BMP_FILES_OF_256_COLORS = 211; // Ð¤Ð°Ð¹Ð»
    // -
    // Ñ„Ð¾Ñ€Ð¼Ð°Ñ‚
    // bmp,
    // 256
    // Ñ†Ð²ÐµÑ‚Ð¾Ð²,
    // Ñ€Ð°Ð·Ð¼ÐµÑ€
    // 16*12.
    public static final int YOU_ARE_NOT_A_CLAN_MEMBER = 212; // Ð’Ñ‹ Ð½Ðµ Ñ�Ð¾Ñ�Ñ‚Ð¾Ð¸Ñ‚Ðµ Ð²
    // ÐºÐ»Ð°Ð½Ðµ.
    public static final int NOT_WORKING_PLEASE_TRY_AGAIN_LATER = 213; // Ð�ÐµÐ´Ð¾Ñ�Ñ‚ÑƒÐ¿Ð½Ð¾.
    // ÐŸÐ¾Ð¶Ð°Ð»ÑƒÐ¹Ñ�Ñ‚Ð°,
    // Ð¿Ð¾Ð¿Ñ€Ð¾Ð±ÑƒÐ¹Ñ‚Ðµ
    // Ð¿Ð¾Ð·Ð¶Ðµ.
    public static final int TITLE_HAS_CHANGED = 214; // Ð¢Ð¸Ñ‚ÑƒÐ» Ð¸Ð·Ð¼ÐµÐ½ÐµÐ½.
    public static final int WAR_WITH_THE_S1_CLAN_HAS_BEGUN = 215; // Ð�Ð°Ñ‡Ð°Ð»Ð°Ñ�ÑŒ
    // Ð²Ð¾Ð¹Ð½Ð° Ñ�
    // ÐºÐ»Ð°Ð½Ð¾Ð¼ $s1.
    public static final int WAR_WITH_THE_S1_CLAN_HAS_ENDED = 216; // Ð’Ð¾Ð¹Ð½Ð° Ñ�
    // ÐºÐ»Ð°Ð½Ð¾Ð¼ $s1
    // Ð·Ð°ÐºÐ¾Ð½Ñ‡Ð¸Ð»Ð°Ñ�ÑŒ.
    public static final int YOU_HAVE_WON_THE_WAR_OVER_THE_S1_CLAN = 217; // Ð’Ñ‹
    // Ð²Ñ‹Ð¸Ð³Ñ€Ð°Ð»Ð¸
    // Ð²Ð¾Ð¹Ð½Ñƒ
    // Ñ�
    // ÐºÐ»Ð°Ð½Ð¾Ð¼
    // $s1!
    public static final int YOU_HAVE_SURRENDERED_TO_THE_S1_CLAN = 218; // Ð’Ñ‹
    // ÐºÐ°Ð¿Ð¸Ñ‚ÑƒÐ»Ð¸Ñ€Ð¾Ð²Ð°Ð»Ð¸
    // Ð²
    // Ð²Ð¾Ð¹Ð½Ðµ
    // Ñ�
    // ÐºÐ»Ð°Ð½Ð¾Ð¼
    // $s1.
    public static final int YOUR_CLAN_LEADER_HAS_DIEDYOU_HAVE_BEEN_DEFEATED_BY_THE_S1_CLAN = 219; // Ð“Ð»Ð°Ð²Ð°
    // ÐºÐ»Ð°Ð½Ð°
    // ÑƒÐ±Ð¸Ñ‚.
    // Ð’Ñ‹
    // Ð¿Ñ€Ð¾Ð¸Ð³Ñ€Ð°Ð»Ð¸
    // Ð²Ð¾Ð¹Ð½Ñƒ
    // Ñ�
    // ÐºÐ»Ð°Ð½Ð¾Ð¼
    // $s1.
    public static final int YOU_HAVE_S1_MINUTES_LEFT_UNTIL_THE_CLAN_WAR_ENDS = 220; // Ð”Ð¾
    // Ð¾ÐºÐ¾Ð½Ñ‡Ð°Ð½Ð¸Ñ�
    // Ð²Ð¾Ð¹Ð½Ñ‹:
    // $s1
    // Ð¼Ð¸Ð½.
    public static final int THE_TIME_LIMIT_FOR_THE_CLAN_WAR_IS_UPWAR_WITH_THE_S1_CLAN_IS_OVER = 221; // Ð’Ñ€ÐµÐ¼Ñ�
    // Ð²Ð¾Ð¹Ð½Ñ‹
    // Ð¸Ñ�Ñ‚ÐµÐºÐ»Ð¾.
    // Ð’Ð¾Ð¹Ð½Ð°
    // Ð·Ð°ÐºÐ¾Ð½Ñ‡Ð¸Ð»Ð°Ñ�ÑŒ.
    public static final int S1_HAS_JOINED_THE_CLAN = 222; // $s1 Ð²Ñ�Ñ‚ÑƒÐ¿Ð°ÐµÑ‚ Ð²
    // ÐºÐ»Ð°Ð½.
    public static final int S1_HAS_WITHDRAWN_FROM_THE_CLAN = 223; // $s1 Ð²Ñ‹Ñ…Ð¾Ð´Ð¸Ñ‚
    // Ð¸Ð· ÐºÐ»Ð°Ð½Ð°.
    public static final int S1_DID_NOT_RESPOND_INVITATION_TO_THE_CLAN_HAS_BEEN_CANCELLED = 224; // $s1
    // Ð½Ðµ
    // Ð¾Ñ‚Ð²ÐµÑ‡Ð°ÐµÑ‚.
    // ÐŸÑ€Ð¸Ð³Ð»Ð°ÑˆÐµÐ½Ð¸Ðµ
    // Ð½Ð°
    // Ð²Ñ�Ñ‚ÑƒÐ¿Ð»ÐµÐ½Ð¸Ðµ
    // Ð²
    // ÐºÐ»Ð°Ð½
    // Ð¾Ñ‚Ð¼ÐµÐ½ÐµÐ½Ð¾.
    public static final int YOU_DIDNT_RESPOND_TO_S1S_INVITATION_JOINING_HAS_BEEN_CANCELLED = 225; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¾Ñ‚Ð²ÐµÑ‚Ð¸Ð»Ð¸
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ñƒ
    // $s1.
    // ÐŸÑ€Ð¸Ð³Ð»Ð°ÑˆÐµÐ½Ð¸Ðµ
    // Ð½Ð°
    // Ð²Ñ�Ñ‚ÑƒÐ¿Ð»ÐµÐ½Ð¸Ðµ
    // Ð²
    // ÐºÐ»Ð°Ð½
    // Ð¾Ñ‚Ð¼ÐµÐ½ÐµÐ½Ð¾.
    public static final int THE_S1_CLAN_DID_NOT_RESPOND_WAR_PROCLAMATION_HAS_BEEN_REFUSED = 226; // ÐšÐ»Ð°Ð½
    // $s1
    // Ð½Ðµ
    // Ð¾Ñ‚Ð²ÐµÑ‚Ð¸Ð».
    // ÐžÐ±ÑŠÑ�Ð²Ð»ÐµÐ½Ð¸Ðµ
    // Ð²Ð¾Ð¹Ð½Ñ‹
    // Ð¾Ñ‚Ð¼ÐµÐ½ÐµÐ½Ð¾.
    public static final int CLAN_WAR_HAS_BEEN_REFUSED_BECAUSE_YOU_DID_NOT_RESPOND_TO_S1_CLANS_WAR_PROCLAMATION = 227; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¾Ñ‚Ð²ÐµÑ‚Ð¸Ð»Ð¸
    // ÐºÐ»Ð°Ð½Ñƒ
    // $s1.
    // Ð’Ð¾Ð¹Ð½Ð°
    // Ð½Ðµ
    // Ð±Ñ‹Ð»Ð°
    // Ð¾Ð±ÑŠÑ�Ð²Ð»ÐµÐ½Ð°.
    public static final int REQUEST_TO_END_WAR_HAS_BEEN_DENIED = 228; // ÐŸÑ€ÐµÐ´Ð»Ð¾Ð¶ÐµÐ½Ð¸Ðµ
    // Ð·Ð°Ð²ÐµÑ€ÑˆÐ¸Ñ‚ÑŒ
    // Ð²Ð¾Ð¹Ð½Ñƒ
    // Ð¾Ñ‚ÐºÐ»Ð¾Ð½ÐµÐ½Ð¾.
    public static final int YOU_ARE_NOT_QUALIFIED_TO_CREATE_A_CLAN = 229; // ÐŸÐ¾ÐºÐ°
    // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ñ�Ð¾Ð·Ð´Ð°Ñ‚ÑŒ
    // ÐºÐ»Ð°Ð½.
    public static final int YOU_MUST_WAIT_10_DAYS_BEFORE_CREATING_A_NEW_CLAN = 230; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ñ�Ð¾Ð·Ð´Ð°Ñ‚ÑŒ
    // Ð½Ð¾Ð²Ñ‹Ð¹
    // ÐºÐ»Ð°Ð½
    // Ð²
    // Ñ‚ÐµÑ‡ÐµÐ½Ð¸Ðµ
    // 10
    // Ð´Ð½ÐµÐ¹
    // Ð¿Ð¾Ñ�Ð»Ðµ
    // Ñ€Ð°Ñ�Ñ„Ð¾Ñ€Ð¼Ð¸Ñ€Ð¾Ð²Ð°Ð½Ð¸Ñ�
    // Ð¿Ñ€ÐµÐ´Ñ‹Ð´ÑƒÑ‰ÐµÐ³Ð¾.
    public static final int AFTER_A_CLAN_MEMBER_IS_DISMISSED_FROM_A_CLAN_THE_CLAN_MUST_WAIT_AT_LEAST_A_DAY_BEFORE_ACCEPTING_A_NEW_MEMBER = 231; // ÐŸÐ¾Ñ�Ð»Ðµ
    // Ð¸Ñ�ÐºÐ»ÑŽÑ‡ÐµÐ½Ð¸Ñ�
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°
    // Ð¸Ð·
    // ÐºÐ»Ð°Ð½Ð°
    // Ð½ÑƒÐ¶Ð½Ð¾
    // Ð¿Ð¾Ð´Ð¾Ð¶Ð´Ð°Ñ‚ÑŒ
    // 24
    // Ñ‡,
    // Ð¿ÐµÑ€ÐµÐ´
    // Ñ‚ÐµÐ¼
    // ÐºÐ°Ðº
    // Ð¿Ñ€Ð¸Ð½Ñ�Ñ‚ÑŒ
    // Ð´Ñ€ÑƒÐ³Ð¾Ð³Ð¾.
    public static final int AFTER_LEAVING_OR_HAVING_BEEN_DISMISSED_FROM_A_CLAN_YOU_MUST_WAIT_AT_LEAST_A_DAY_BEFORE_JOINING_ANOTHER_CLAN = 232; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ð²Ñ�Ñ‚ÑƒÐ¿Ð¸Ñ‚ÑŒ
    // Ð²
    // Ð½Ð¾Ð²Ñ‹Ð¹
    // ÐºÐ»Ð°Ð½
    // Ð²
    // Ñ‚ÐµÑ‡ÐµÐ½Ð¸Ðµ
    // 24
    // Ñ‡
    // Ð¿Ð¾Ñ�Ð»Ðµ
    // Ð²Ñ‹Ñ…Ð¾Ð´Ð°
    // Ð¸Ð·
    // Ð¿Ñ€ÐµÐ´Ñ‹Ð´ÑƒÑ‰ÐµÐ³Ð¾.
    public static final int THE_ACADEMY_ROYAL_GUARD_ORDER_OF_KNIGHTS_IS_FULL_AND_CANNOT_ACCEPT_NEW_MEMBERS_AT_THIS_TIME = 233; // ÐšÐ»Ð°Ð½
    // Ð¿ÐµÑ€ÐµÐ¿Ð¾Ð»Ð½ÐµÐ½.
    public static final int THE_TARGET_MUST_BE_A_CLAN_MEMBER = 234; // Ð¦ÐµÐ»ÑŒ
    // Ð´Ð¾Ð»Ð¶Ð½Ð°
    // Ñ�Ð¾Ñ�Ñ‚Ð¾Ñ�Ñ‚ÑŒ
    // Ð² ÐºÐ»Ð°Ð½Ðµ.
    public static final int YOU_CANNOT_TRANSFER_YOUR_RIGHTS = 235; // Ð’Ñ‹ Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ñ€Ð°Ñ�Ð¿Ð¾Ñ€Ñ�Ð¶Ð°Ñ‚ÑŒÑ�Ñ�
    // Ð¿Ñ€Ð°Ð²Ð°Ð¼Ð¸.
    public static final int ONLY_THE_CLAN_LEADER_IS_ENABLED = 236; // Ð­Ñ‚Ð¾ Ð¼Ð¾Ð¶ÐµÑ‚
    // Ñ�Ð´ÐµÐ»Ð°Ñ‚ÑŒ
    // Ñ‚Ð¾Ð»ÑŒÐºÐ¾
    // Ð³Ð»Ð°Ð²Ð°
    // ÐºÐ»Ð°Ð½Ð°.
    public static final int CANNOT_FIND_CLAN_LEADER = 237; // Ð�ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾ Ð½Ð°Ð¹Ñ‚Ð¸
    // Ð³Ð»Ð°Ð²Ñƒ ÐºÐ»Ð°Ð½Ð°.
    public static final int NOT_JOINED_IN_ANY_CLAN = 238; // Ð�Ðµ Ð² ÐºÐ»Ð°Ð½Ðµ.
    public static final int THE_CLAN_LEADER_CANNOT_WITHDRAW = 239; // Ð“Ð»Ð°Ð²Ð°
    // ÐºÐ»Ð°Ð½Ð° Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð¿Ð¾ÐºÐ¸Ð½ÑƒÑ‚ÑŒ
    // ÐºÐ»Ð°Ð½.
    public static final int CURRENTLY_INVOLVED_IN_CLAN_WAR = 240; // Ð¡ÐµÐ¹Ñ‡Ð°Ñ� Ð¸Ð´ÐµÑ‚
    // Ð²Ð¾Ð¹Ð½Ð°
    // ÐºÐ»Ð°Ð½Ð¾Ð².
    public static final int LEADER_OF_THE_S1_CLAN_IS_NOT_LOGGED_IN = 241; // Ð“Ð»Ð°Ð²Ñ‹
    // ÐºÐ»Ð°Ð½Ð°
    // $s1
    // Ð½ÐµÑ‚
    // Ð²
    // Ð¸Ð³Ñ€Ðµ.
    public static final int SELECT_TARGET = 242; // Ð’Ñ‹Ð±ÐµÑ€Ð¸Ñ‚Ðµ Ñ†ÐµÐ»ÑŒ.
    public static final int CANNOT_PROCLAIM_WAR_ON_ALLIED_CLANS = 243; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ð¾Ð±ÑŠÑ�Ð²Ð¸Ñ‚ÑŒ
    // Ð²Ð¾Ð¹Ð½Ñƒ
    // Ñ�Ð¾ÑŽÐ·Ð½Ð¾Ð¼Ñƒ
    // ÐºÐ»Ð°Ð½Ñƒ.
    public static final int UNQUALIFIED_TO_REQUEST_DECLARATION_OF_CLAN_WAR = 244; // Ð£
    // Ð’Ð°Ñ�
    // Ð½ÐµÑ‚
    // Ð¿Ñ€Ð°Ð²
    // Ð½Ð°
    // Ð¾Ð±ÑŠÑ�Ð²Ð»ÐµÐ½Ð¸Ðµ
    // Ð²Ð¾Ð¹Ð½Ñ‹.
    public static final int _5_DAYS_HAS_NOT_PASSED_SINCE_YOU_WERE_REFUSED_WAR_DO_YOU_WANT_TO_CONTINUE = 245; // Ð¡
    // Ð¼Ð¾Ð¼ÐµÐ½Ñ‚Ð°
    // Ð¾Ñ‚ÐºÐ°Ð·Ð°
    // Ð¾Ñ‚
    // Ð²Ð¾Ð¹Ð½Ñ‹
    // Ð½Ðµ
    // Ð¿Ñ€Ð¾ÑˆÐ»Ð¾
    // 5
    // Ð´Ð½ÐµÐ¹.
    // ÐŸÑ€Ð¾Ð´Ð¾Ð»Ð¶Ð¸Ñ‚ÑŒ?
    public static final int THE_OTHER_CLAN_IS_CURRENTLY_AT_WAR = 246; // Ð­Ñ‚Ð¾Ñ‚
    // ÐºÐ»Ð°Ð½
    // ÑƒÐ¶Ðµ
    // Ð²Ð¾ÑŽÐµÑ‚.
    public static final int YOU_HAVE_ALREADY_BEEN_AT_WAR_WITH_THE_S1_CLAN_5_DAYS_MUST_PASS_BEFORE_YOU_CAN_PROCLAIM_WAR_AGAIN = 247; // Ð’Ñ‹
    // ÑƒÐ¶Ðµ
    // Ð²Ð¾ÐµÐ²Ð°Ð»Ð¸
    // Ñ�
    // ÐºÐ»Ð°Ð½Ð¾Ð¼
    // $s1.
    // ÐŸÐµÑ€ÐµÐ´
    // Ð¿Ð¾Ð²Ñ‚Ð¾Ñ€Ð½Ñ‹Ð¼
    // Ð¾Ð±ÑŠÑ�Ð²Ð»ÐµÐ½Ð¸ÐµÐ¼
    // Ð²Ð¾Ð¹Ð½Ñ‹
    // Ð´Ð¾Ð»Ð¶Ð½Ð¾
    // Ð¿Ñ€Ð¾Ð¹Ñ‚Ð¸
    // 5
    // Ð´Ð½ÐµÐ¹.
    public static final int YOU_CANNOT_PROCLAIM_WAR_THE_S1_CLAN_DOES_NOT_HAVE_ENOUGH_MEMBERS = 248; // Ð’
    // ÐºÐ»Ð°Ð½Ðµ
    // $s1
    // Ñ�Ð»Ð¸ÑˆÐºÐ¾Ð¼
    // Ð¼Ð°Ð»Ð¾
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶ÐµÐ¹.
    // Ð�ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾
    // Ð¾Ð±ÑŠÑ�Ð²Ð¸Ñ‚ÑŒ
    // Ð²Ð¾Ð¹Ð½Ñƒ.
    public static final int DO_YOU_WISH_TO_SURRENDER_TO_THE_S1_CLAN = 249; // ÐšÐ°Ð¿Ð¸Ñ‚ÑƒÐ»Ð¸Ñ€Ð¾Ð²Ð°Ñ‚ÑŒ
    // Ð²
    // Ð²Ð¾Ð¹Ð½Ðµ
    // Ñ�
    // ÐºÐ»Ð°Ð½Ð¾Ð¼
    // $s1?
    public static final int YOU_HAVE_PERSONALLY_SURRENDERED_TO_THE_S1_CLAN_YOU_ARE_LEAVING_THE_CLAN_WAR = 250; // Ð’Ñ‹
    // Ñ�Ð´Ð°Ð»Ð¸Ñ�ÑŒ
    // ÐºÐ»Ð°Ð½Ñƒ
    // $s1.
    // Ð’Ð¾Ð¹Ð½Ð°
    // Ð·Ð°ÐºÐ¾Ð½Ñ‡ÐµÐ½Ð°.
    public static final int YOU_CANNOT_PROCLAIM_WAR_YOU_ARE_AT_WAR_WITH_ANOTHER_CLAN = 251; // Ð’Ñ‹
    // ÑƒÐ¶Ðµ
    // Ð²
    // Ñ�Ð¾Ñ�Ñ‚Ð¾Ñ�Ð½Ð¸Ð¸
    // Ð²Ð¾Ð¹Ð½Ñ‹.
    // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ð¾Ð±ÑŠÑ�Ð²Ð¸Ñ‚ÑŒ
    // Ð²Ð¾Ð¹Ð½Ñƒ
    // Ð´Ñ€ÑƒÐ³Ð¾Ð¼Ñƒ
    // ÐºÐ»Ð°Ð½Ñƒ.
    public static final int ENTER_THE_NAME_OF_CLAN_TO_SURRENDER_TO = 252; // Ð’Ð²ÐµÐ´Ð¸Ñ‚Ðµ
    // Ð½Ð°Ð·Ð²Ð°Ð½Ð¸Ðµ
    // ÐºÐ»Ð°Ð½Ð°,
    // ÐºÐ¾Ñ‚Ð¾Ñ€Ð¾Ð¼Ñƒ
    // Ñ…Ð¾Ñ‚Ð¸Ñ‚Ðµ
    // Ñ�Ð´Ð°Ñ‚ÑŒÑ�Ñ�.
    public static final int ENTER_THE_NAME_OF_CLAN_TO_REQUEST_END_OF_WAR = 253; // Ð’Ð²ÐµÐ´Ð¸Ñ‚Ðµ
    // Ð½Ð°Ð·Ð²Ð°Ð½Ð¸Ðµ
    // ÐºÐ»Ð°Ð½Ð°,
    // ÐºÐ¾Ñ‚Ð¾Ñ€Ð¾Ð¼Ñƒ
    // Ð¿Ñ€ÐµÐ´Ð»Ð°Ð³Ð°ÐµÑ‚Ðµ
    // Ð¼Ð¸Ñ€.
    public static final int CLAN_LEADER_CANNOT_SURRENDER_PERSONALLY = 254; // Ð“Ð»Ð°Ð²Ð°
    // ÐºÐ»Ð°Ð½Ð°
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ñ�Ð´Ð°Ñ‚ÑŒÑ�Ñ�.
    public static final int THE_S1_CLAN_HAS_REQUESTED_TO_END_WAR_DO_YOU_AGREE = 255; // ÐšÐ»Ð°Ð½
    // $s1
    // Ð¿Ñ€ÐµÐ´Ð»Ð°Ð³Ð°ÐµÑ‚
    // Ð¼Ð¸Ñ€.
    // Ð¡Ð¾Ð³Ð»Ð°Ñ�Ð¸Ñ‚ÑŒÑ�Ñ�?
    public static final int ENTER_NAME = 256; // Ð’Ð²ÐµÐ´Ð¸Ñ‚Ðµ Ñ‚Ð¸Ñ‚ÑƒÐ».
    public static final int DO_YOU_PROPOSE_TO_THE_S1_CLAN_TO_END_THE_WAR = 257; // ÐŸÑ€ÐµÐ´Ð»Ð¾Ð¶Ð¸Ñ‚ÑŒ
    // Ð¼Ð¸Ñ€
    // ÐºÐ»Ð°Ð½Ñƒ
    // $s1?
    public static final int NOT_INVOLVED_IN_CLAN_WAR = 258; // Ð’Ñ‹ Ð½Ðµ ÑƒÑ‡Ð°Ñ�Ñ‚Ð²ÑƒÐµÑ‚Ðµ
    // Ð² Ð²Ð¾Ð¹Ð½Ðµ.
    public static final int SELECT_CLAN_MEMBERS_FROM_LIST = 259; // Ð’Ñ‹Ð±ÐµÑ€Ð¸Ñ‚Ðµ
    // Ñ‡Ð»ÐµÐ½Ð° ÐºÐ»Ð°Ð½Ð°
    // Ð¸Ð· Ñ�Ð¿Ð¸Ñ�ÐºÐ°.
    public static final int FAME_LEVEL_HAS_DECREASED_5_DAYS_HAVE_NOT_PASSED_SINCE_YOU_WERE_REFUSED_WAR = 260; // ÐŸÐ¾Ñ�Ð»Ðµ
    // Ð¾ÐºÐ¾Ð½Ñ‡Ð°Ð½Ð¸Ñ�
    // Ð²Ð¾Ð¹Ð½Ñ‹
    // ÐºÐ»Ð°Ð½Ð¾Ð²
    // Ð½Ðµ
    // Ð¿Ñ€Ð¾ÑˆÐ»Ð¾
    // 5
    // Ð´Ð½ÐµÐ¹,
    // Ñ€ÐµÐ¿ÑƒÑ‚Ð°Ñ†Ð¸Ñ�
    // ÐºÐ»Ð°Ð½Ð°
    // Ð¿Ð¾Ð½Ð¸Ð·Ð¸Ð»Ð°Ñ�ÑŒ.
    public static final int CLAN_NAME_IS_INCORRECT = 261; // Ð�ÐµÐ²ÐµÑ€Ð½Ð¾Ðµ Ð½Ð°Ð·Ð²Ð°Ð½Ð¸Ðµ
    // ÐºÐ»Ð°Ð½Ð°.
    public static final int CLAN_NAMES_LENGTH_IS_INCORRECT = 262; // Ð�ÐµÐ²ÐµÑ€Ð½Ð°Ñ�
    // Ð´Ð»Ð¸Ð½Ð°
    // Ð½Ð°Ð·Ð²Ð°Ð½Ð¸Ñ�
    // ÐºÐ»Ð°Ð½Ð°.
    public static final int DISPERSION_HAS_ALREADY_BEEN_REQUESTED = 263; // Ð’Ñ‹
    // ÑƒÐ¶Ðµ
    // Ð¿Ð¾Ð´Ð°Ð»Ð¸
    // Ð·Ð°Ñ�Ð²ÐºÑƒ
    // Ð½Ð°
    // Ñ€Ð°Ñ�Ñ„Ð¾Ñ€Ð¼Ð¸Ñ€Ð¾Ð²Ð°Ð½Ð¸Ðµ
    // ÐºÐ»Ð°Ð½Ð°.
    public static final int YOU_CANNOT_DISSOLVE_A_CLAN_WHILE_ENGAGED_IN_A_WAR = 264; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ñ€Ð°Ñ�Ñ„Ð¾Ñ€Ð¼Ð¸Ñ€Ð¾Ð²Ð°Ñ‚ÑŒ
    // ÐºÐ»Ð°Ð½
    // Ð²Ð¾
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ð²Ð¾Ð¹Ð½Ñ‹.
    public static final int YOU_CANNOT_DISSOLVE_A_CLAN_DURING_A_SIEGE_OR_WHILE_PROTECTING_A_CASTLE = 265; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ñ€Ð°Ñ�Ñ„Ð¾Ñ€Ð¼Ð¸Ñ€Ð¾Ð²Ð°Ñ‚ÑŒ
    // ÐºÐ»Ð°Ð½
    // Ð²Ð¾
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ð¾Ñ�Ð°Ð´Ñ‹.
    public static final int YOU_CANNOT_DISSOLVE_A_CLAN_WHILE_OWNING_A_CLAN_HALL_OR_CASTLE = 266; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ñ€Ð°Ñ�Ñ„Ð¾Ñ€Ð¼Ð¸Ñ€Ð¾Ð²Ð°Ñ‚ÑŒ
    // ÐºÐ»Ð°Ð½,
    // Ð²Ð»Ð°Ð´ÐµÑŽÑ‰Ð¸Ð¹
    // Ñ…Ð¾Ð»Ð»Ð¾Ð¼/Ð·Ð°Ð¼ÐºÐ¾Ð¼.
    public static final int NO_REQUESTS_FOR_DISPERSION = 267; // Ð�ÐµÑ‚ Ð·Ð°Ñ�Ð²ÐºÐ¸ Ð½Ð°
    // Ñ€Ð°Ñ�Ñ„Ð¾Ñ€Ð¼Ð¸Ñ€Ð¾Ð²Ð°Ð½Ð¸Ðµ
    // ÐºÐ»Ð°Ð½Ð°.
    public static final int PLAYER_ALREADY_BELONGS_TO_A_CLAN = 268; // ÐŸÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶
    // ÑƒÐ¶Ðµ
    // Ñ�Ð¾Ñ�Ñ‚Ð¾Ð¸Ñ‚ Ð²
    // ÐºÐ»Ð°Ð½Ðµ.
    public static final int YOU_CANNOT_EXPEL_YOURSELF = 269; // Ð’Ñ‹ Ð½Ðµ Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¸Ñ�ÐºÐ»ÑŽÑ‡Ð¸Ñ‚ÑŒ Ñ�ÐµÐ±Ñ�.
    public static final int YOU_HAVE_ALREADY_SURRENDERED = 270; // Ð’Ñ‹ ÑƒÐ¶Ðµ
    // ÐºÐ°Ð¿Ð¸Ñ‚ÑƒÐ»Ð¸Ñ€Ð¾Ð²Ð°Ð»Ð¸.
    public static final int TITLE_ENDOWMENT_IS_ONLY_POSSIBLE_WHEN_CLANS_SKILL_LEVELS_ARE_ABOVE_3 = 271; // Ð˜Ð·Ð¼ÐµÐ½ÐµÐ½Ð¸Ðµ
    // Ñ‚Ð¸Ñ‚ÑƒÐ»Ð¾Ð²
    // Ð²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾
    // Ð¿Ñ€Ð¸
    // ÑƒÑ€Ð¾Ð²Ð½Ðµ
    // ÐºÐ»Ð°Ð½Ð°
    // 3
    // Ð¸
    // Ð²Ñ‹ÑˆÐµ.
    public static final int CLAN_CREST_REGISTRATION_IS_ONLY_POSSIBLE_WHEN_CLANS_SKILL_LEVELS_ARE_ABOVE_3 = 272; // Ð£Ñ�Ñ‚Ð°Ð½Ð¾Ð²ÐºÐ°
    // Ñ�Ð¼Ð±Ð»ÐµÐ¼Ñ‹
    // Ð´Ð¾Ñ�Ñ‚ÑƒÐ¿Ð½Ð°
    // Ð¿Ñ€Ð¸
    // ÑƒÑ€Ð¾Ð²Ð½Ðµ
    // ÐºÐ»Ð°Ð½Ð°
    // 3
    // Ð¸
    // Ð²Ñ‹ÑˆÐµ.
    public static final int PROCLAMATION_OF_CLAN_WAR_IS_ONLY_POSSIBLE_WHEN_CLANS_SKILL_LEVELS_ARE_ABOVE_3 = 273; // ÐžÐ±ÑŠÑ�Ð²Ð»ÐµÐ½Ð¸Ðµ
    // Ð²Ð¾Ð¹Ð½Ñ‹
    // Ð²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾
    // Ð¿Ñ€Ð¸
    // ÑƒÑ€Ð¾Ð²Ð½Ðµ
    // ÐºÐ»Ð°Ð½Ð°
    // 3
    // Ð¸
    // Ð²Ñ‹ÑˆÐµ.
    public static final int CLANS_SKILL_LEVEL_HAS_INCREASED = 274; // Ð£Ñ€Ð¾Ð²ÐµÐ½ÑŒ
    // ÐºÐ»Ð°Ð½Ð°
    // ÑƒÐ²ÐµÐ»Ð¸Ñ‡ÐµÐ½.
    public static final int CLAN_HAS_FAILED_TO_INCREASE_SKILL_LEVEL = 275; // Ð�Ðµ
    // ÑƒÐ´Ð°Ð»Ð¾Ñ�ÑŒ
    // ÑƒÐ²ÐµÐ»Ð¸Ñ‡Ð¸Ñ‚ÑŒ
    // ÑƒÑ€Ð¾Ð²ÐµÐ½ÑŒ
    // ÑƒÐ¼ÐµÐ½Ð¸Ð¹
    // ÐºÐ»Ð°Ð½Ð°.
    public static final int YOU_DO_NOT_HAVE_ENOUGH_ITEMS_TO_LEARN_SKILLS = 276; // Ð�Ðµ
    // Ñ…Ð²Ð°Ñ‚Ð°ÐµÑ‚
    // Ð¼Ð°Ñ‚ÐµÑ€Ð¸Ð°Ð»Ð¾Ð²,
    // Ñ‡Ñ‚Ð¾Ð±Ñ‹
    // Ð²Ñ‹ÑƒÑ‡Ð¸Ñ‚ÑŒ
    // ÑƒÐ¼ÐµÐ½Ð¸Ðµ.
    public static final int YOU_HAVE_EARNED_S1_2 = 277; // Ð’Ñ‹ÑƒÑ‡ÐµÐ½Ð¾ ÑƒÐ¼ÐµÐ½Ð¸Ðµ: $s1.
    public static final int YOU_DO_NOT_HAVE_ENOUGH_SP_TO_LEARN_SKILLS = 278; // Ð�ÐµÐ´Ð¾Ñ�Ñ‚Ð°Ñ‚Ð¾Ñ‡Ð½Ð¾
    // SP
    // Ð´Ð»Ñ�
    // Ð¸Ð·ÑƒÑ‡ÐµÐ½Ð¸Ñ�
    // ÑƒÐ¼ÐµÐ½Ð¸Ñ�.
    public static final int YOU_DO_NOT_HAVE_ENOUGH_ADENA = 279; // Ð�ÐµÐ´Ð¾Ñ�Ñ‚Ð°Ñ‚Ð¾Ñ‡Ð½Ð¾
    // Ð°Ð´ÐµÐ½.
    public static final int YOU_DO_NOT_HAVE_ANY_ITEMS_TO_SELL = 280; // Ð�ÐµÑ‚
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ð¾Ð²
    // Ð½Ð°
    // Ð¿Ñ€Ð¾Ð´Ð°Ð¶Ñƒ.
    public static final int YOU_DO_NOT_HAVE_ENOUGH_CUSTODY_FEES = 281; // Ð�ÐµÐ´Ð¾Ñ�Ñ‚Ð°Ñ‚Ð¾Ñ‡Ð½Ð¾
    // Ð´ÐµÐ½ÐµÐ³
    // Ð´Ð»Ñ�
    // Ð¾Ð¿Ð»Ð°Ñ‚Ñ‹
    // Ñ…Ñ€Ð°Ð½Ð¸Ð»Ð¸Ñ‰Ð°.
    public static final int YOU_HAVE_NOT_DEPOSITED_ANY_ITEMS_IN_YOUR_WAREHOUSE = 282; // Ð’
    // Ñ…Ñ€Ð°Ð½Ð¸Ð»Ð¸Ñ‰Ðµ
    // Ð½ÐµÑ‚
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ð¾Ð².
    public static final int YOU_HAVE_ENTERED_A_COMBAT_ZONE = 283; // Ð’Ñ‹ Ð·Ð°ÑˆÐ»Ð¸ Ð²
    // Ð±Ð¾ÐµÐ²ÑƒÑŽ
    // Ð·Ð¾Ð½Ñƒ.
    public static final int YOU_HAVE_LEFT_A_COMBAT_ZONE = 284; // Ð’Ñ‹ Ð²Ñ‹ÑˆÐ»Ð¸ Ð¸Ð·
    // Ð±Ð¾ÐµÐ²Ð¾Ð¹ Ð·Ð¾Ð½Ñ‹.
    public static final int CLAN_S1_HAS_SUCCEEDED_IN_ENGRAVING_THE_RULER = 285; // ÐŸÐ¾Ð±ÐµÐ´Ð¸Ð»
    // ÐºÐ»Ð°Ð½
    // $s1!
    public static final int YOUR_BASE_IS_BEING_ATTACKED = 286; // Ð’Ð°ÑˆÐ° Ð±Ð°Ð·Ð°
    // Ð°Ñ‚Ð°ÐºÐ¾Ð²Ð°Ð½Ð°.
    public static final int THE_OPPONENT_CLAN_HAS_BEGUN_TO_ENGRAVE_THE_RULER = 287; // ÐŸÑ€Ð¾Ñ‚Ð¸Ð²Ð½Ð¸ÐºÐ¸
    // Ð¿Ñ‹Ñ‚Ð°ÑŽÑ‚Ñ�Ñ�
    // Ð·Ð°Ñ…Ð²Ð°Ñ‚Ð¸Ñ‚ÑŒ
    // Ð’Ð°ÑˆÑƒ
    // Ð±Ð°Ð·Ñƒ.
    public static final int THE_CASTLE_GATE_HAS_BEEN_BROKEN_DOWN = 288; // Ð’Ñ€Ð°Ñ‚Ð°
    // Ð·Ð°Ð¼ÐºÐ°
    // Ñ€Ð°Ð·Ñ€ÑƒÑˆÐµÐ½Ñ‹.
    public static final int SINCE_A_HEADQUARTERS_ALREADY_EXISTS_YOU_CANNOT_BUILD_ANOTHER_ONE = 289; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ð¿Ð¾Ñ�Ñ‚Ð°Ð²Ð¸Ñ‚ÑŒ
    // ÐµÑ‰Ðµ
    // Ð¾Ð´Ð½Ñƒ
    // Ð±Ð°Ð·Ñƒ.
    public static final int YOU_CANNOT_SET_UP_A_BASE_HERE = 290; // Ð�ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾
    // Ð¿Ð¾Ñ�Ñ‚Ð°Ð²Ð¸Ñ‚ÑŒ
    // Ð±Ð°Ð·Ñƒ Ð·Ð´ÐµÑ�ÑŒ.
    public static final int CLAN_S1_IS_VICTORIOUS_OVER_S2S_CASTLE_SIEGE = 291; // ÐšÐ»Ð°Ð½
    // $s1
    // Ð·Ð°Ð²Ð¾ÐµÐ²Ð°Ð»
    // $s2!
    public static final int S1_HAS_ANNOUNCED_THE_CASTLE_SIEGE_TIME = 292; // $s1
    // Ð¾Ð±ÑŠÑ�Ð²Ð»Ñ�ÐµÑ‚
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ð¾Ñ�Ð°Ð´Ñ‹
    // Ð·Ð°Ð¼ÐºÐ°.
    public static final int THE_REGISTRATION_TERM_FOR_S1_HAS_ENDED = 293; // Ð ÐµÐ³Ð¸Ñ�Ñ‚Ñ€Ð°Ñ†Ð¸Ñ�
    // Ð½Ð°
    // Ð¾Ñ�Ð°Ð´Ñƒ
    // Ð·Ð°Ð¼ÐºÐ°
    // $s1
    // Ð·Ð°Ð²ÐµÑ€ÑˆÐµÐ½Ð°.
    public static final int YOU_CANNOT_SUMMON_A_BASE_BECAUSE_YOU_ARE_NOT_IN_BATTLE = 294; // Ð’Ð°Ñˆ
    // ÐºÐ»Ð°Ð½
    // Ð½Ðµ
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð²ÑƒÐµÑ‚
    // Ð²
    // Ð¾Ñ�Ð°Ð´Ðµ.
    // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ñ�Ð¾Ð·Ð´Ð°Ñ‚ÑŒ
    // Ð±Ð°Ð·Ñƒ.
    public static final int S1S_SIEGE_WAS_CANCELED_BECAUSE_THERE_WERE_NO_CLANS_THAT_PARTICIPATED = 295; // ÐžÑ�Ð°Ð´Ð°
    // Ð·Ð°Ð¼ÐºÐ°
    // $s1
    // Ð¾Ñ‚Ð¼ÐµÐ½ÐµÐ½Ð°
    // Ð¸Ð·-Ð·Ð°
    // Ð¾Ñ‚Ñ�ÑƒÑ‚Ñ�Ñ‚Ð²Ð¸Ñ�
    // Ð·Ð°Ñ�Ð²Ð¾Ðº.
    public static final int YOU_RECEIVED_S1_DAMAGE_FROM_TAKING_A_HIGH_FALL = 296; // ÐŸÐ¾Ð»ÑƒÑ‡ÐµÐ½Ð¾
    // $s1
    // ÑƒÑ€Ð¾Ð½Ð°
    // Ð¸Ð·-Ð·Ð°
    // Ð¿Ð°Ð´ÐµÐ½Ð¸Ñ�
    // Ñ�
    // Ð²Ñ‹Ñ�Ð¾Ñ‚Ñ‹.
    public static final int YOU_RECEIVED_S1_DAMAGE_BECAUSE_YOU_WERE_UNABLE_TO_BREATHE = 297; // ÐŸÐ¾Ð»ÑƒÑ‡ÐµÐ½Ð¾
    // $s1
    // ÑƒÑ€Ð¾Ð½Ð°
    // Ð¾Ñ‚
    // ÑƒÐ´ÑƒÑˆÑŒÑ�.
    public static final int YOU_HAVE_DROPPED_S1 = 298; // Ð’Ñ‹ Ð²Ñ‹Ñ€Ð¾Ð½Ð¸Ð»Ð¸: $s1.
    public static final int S1_HAS_OBTAINED_S3_S2 = 299; // $c1 Ð¿Ð¾Ð»ÑƒÑ‡Ð°ÐµÑ‚: $s2
    // ($s3 ÑˆÑ‚.)
    public static final int S1_HAS_OBTAINED_S2 = 300; // $c1 Ð¿Ð¾Ð»ÑƒÑ‡Ð°ÐµÑ‚: $s2.
    public static final int S2_S1_HAS_DISAPPEARED = 301; // Ð˜Ñ�Ñ‡ÐµÐ·Ð»Ð¾: $s1 ($s2
    // ÑˆÑ‚.)
    public static final int S1_HAS_DISAPPEARED = 302; // Ð˜Ñ�Ñ‡ÐµÐ·Ð»Ð¾: $s1.
    public static final int SELECT_ITEM_TO_ENCHANT = 303; // Ð’Ñ‹Ð±ÐµÑ€Ð¸Ñ‚Ðµ Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚
    // Ð´Ð»Ñ� ÑƒÐ»ÑƒÑ‡ÑˆÐµÐ½Ð¸Ñ�.
    public static final int CLAN_MEMBER_S1_HAS_LOGGED_INTO_GAME = 304; // Ð§Ð»ÐµÐ½
    // ÐºÐ»Ð°Ð½Ð°
    // $s1
    // Ð·Ð°ÑˆÐµÐ»
    // Ð²
    // Ð¸Ð³Ñ€Ñƒ.
    public static final int THE_PLAYER_DECLINED_TO_JOIN_YOUR_PARTY = 305; // ÐŸÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶
    // Ð¾Ñ‚ÐºÐ°Ð·Ð°Ð»Ñ�Ñ�
    // Ð¿Ñ€Ð¸Ñ�Ð¾ÐµÐ´Ð¸Ð½Ð¸Ñ‚ÑŒÑ�Ñ�
    // Ðº
    // Ð³Ñ€ÑƒÐ¿Ð¿Ðµ.
    public static final int YOU_HAVE_FAILED_TO_DELETE_THE_CHARACTER = 306; // Ð£Ð´Ð°Ð»ÐµÐ½Ð¸Ðµ
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°
    // Ð½Ðµ
    // ÑƒÐ´Ð°Ð»Ð¾Ñ�ÑŒ.
    public static final int YOU_HAVE_FAILED_TO_TRADE_WITH_THE_WAREHOUSE = 307; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ñ‚Ð¾Ñ€Ð³Ð¾Ð²Ð°Ñ‚ÑŒ
    // Ñ�Ð¾
    // Ð¡Ð¼Ð¾Ñ‚Ñ€Ð¸Ñ‚ÐµÐ»ÐµÐ¼
    // Ð¡ÐºÐ»Ð°Ð´Ð°.
    public static final int FAILED_TO_JOIN_THE_CLAN = 308; // ÐŸÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶ Ð¾Ñ‚ÐºÐ°Ð·Ð°Ð»Ñ�Ñ�
    // Ð¿Ñ€Ð¸Ñ�Ð¾ÐµÐ´Ð¸Ð½Ð¸Ñ‚ÑŒÑ�Ñ� Ðº
    // ÐºÐ»Ð°Ð½Ñƒ.
    public static final int SUCCEEDED_IN_EXPELLING_A_CLAN_MEMBER = 309; // Ð˜Ñ�ÐºÐ»ÑŽÑ‡ÐµÐ½Ð¸Ðµ
    // Ñ‡Ð»ÐµÐ½Ð°
    // ÐºÐ»Ð°Ð½Ð°
    // Ð¿Ñ€Ð¾ÑˆÐ»Ð¾
    // ÑƒÑ�Ð¿ÐµÑˆÐ½Ð¾.
    public static final int FAILED_TO_EXPEL_A_CLAN_MEMBER = 310; // Ð˜Ñ�ÐºÐ»ÑŽÑ‡ÐµÐ½Ð¸Ðµ
    // Ñ‡Ð»ÐµÐ½Ð° ÐºÐ»Ð°Ð½Ð°
    // Ð½Ðµ ÑƒÐ´Ð°Ð»Ð¾Ñ�ÑŒ.
    public static final int CLAN_WAR_HAS_BEEN_ACCEPTED = 311; // ÐŸÑ€ÐµÐ´Ð»Ð¾Ð¶ÐµÐ½Ð¸Ðµ
    // Ð²Ð¾Ð¹Ð½Ñ‹ Ð¼ÐµÐ¶Ð´Ñƒ
    // ÐºÐ»Ð°Ð½Ð°Ð¼Ð¸
    // Ð¿Ñ€Ð¸Ð½Ñ�Ñ‚Ð¾.
    public static final int CLAN_WAR_HAS_BEEN_REFUSED = 312; // ÐŸÑ€ÐµÐ´Ð»Ð¾Ð¶ÐµÐ½Ð¸Ðµ
    // Ð²Ð¾Ð¹Ð½Ñ‹ Ð¼ÐµÐ¶Ð´Ñƒ
    // ÐºÐ»Ð°Ð½Ð°Ð¼Ð¸
    // Ð¾Ñ‚ÐºÐ»Ð¾Ð½ÐµÐ½Ð¾.
    public static final int THE_CEASE_WAR_REQUEST_HAS_BEEN_ACCEPTED = 313; // ÐŸÑ€ÐµÐ´Ð»Ð¾Ð¶ÐµÐ½Ð¸Ðµ
    // Ð¾
    // Ð·Ð°Ð²ÐµÑ€ÑˆÐµÐ½Ð¸Ð¸
    // Ð²Ð¾Ð¹Ð½Ñ‹
    // Ð¿Ñ€Ð¸Ð½Ñ�Ñ‚Ð¾.
    public static final int FAILED_TO_SURRENDER = 314; // ÐšÐ°Ð¿Ð¸Ñ‚ÑƒÐ»Ñ�Ñ†Ð¸Ñ� Ð½Ðµ
    // ÑƒÐ´Ð°Ð»Ð°Ñ�ÑŒ.
    public static final int FAILED_TO_PERSONALLY_SURRENDER = 315; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ð²Ñ‹Ð¹Ñ‚Ð¸ Ð¸Ð·
    // Ð²Ð¾Ð¹Ð½Ñ‹.
    public static final int FAILED_TO_WITHDRAW_FROM_THE_PARTY = 316; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ð²Ñ‹Ð¹Ñ‚Ð¸ Ð¸Ð·
    // Ð³Ñ€ÑƒÐ¿Ð¿Ñ‹.
    public static final int FAILED_TO_EXPEL_A_PARTY_MEMBER = 317; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ð¸Ñ�ÐºÐ»ÑŽÑ‡Ð¸Ñ‚ÑŒ
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°
    // Ð¸Ð· Ð³Ñ€ÑƒÐ¿Ð¿Ñ‹.
    public static final int FAILED_TO_DISPERSE_THE_PARTY = 318; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ñ€Ð°Ñ�Ñ„Ð¾Ñ€Ð¼Ð¸Ñ€Ð¾Ð²Ð°Ñ‚ÑŒ
    // Ð³Ñ€ÑƒÐ¿Ð¿Ñƒ.
    public static final int YOU_ARE_UNABLE_TO_UNLOCK_THE_DOOR = 319; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ð¾Ñ‚ÐºÑ€Ñ‹Ñ‚ÑŒ
    // Ð´Ð²ÐµÑ€ÑŒ.
    public static final int YOU_HAVE_FAILED_TO_UNLOCK_THE_DOOR = 320; // Ð’Ð°Ð¼ Ð½Ðµ
    // ÑƒÐ´Ð°Ð»Ð¾Ñ�ÑŒ
    // Ð¾Ñ‚ÐºÑ€Ñ‹Ñ‚ÑŒ
    // Ð´Ð²ÐµÑ€ÑŒ.
    public static final int IT_IS_NOT_LOCKED = 321; // Ð�Ðµ Ð·Ð°Ð¿ÐµÑ€Ñ‚Ð¾.
    public static final int PLEASE_DECIDE_ON_THE_SALES_PRICE = 322; // Ð£ÐºÐ°Ð¶Ð¸Ñ‚Ðµ
    // Ñ†ÐµÐ½Ñƒ
    // Ð¿Ñ€Ð¾Ð´Ð°Ð¶Ð¸.
    public static final int YOUR_FORCE_HAS_INCREASED_TO_S1_LEVEL = 323; // Ð’Ð°ÑˆÐ°
    // Ñ�Ð¸Ð»Ð°
    // ÑƒÐ²ÐµÐ»Ð¸Ñ‡ÐµÐ½Ð°
    // Ð´Ð¾
    // $s1
    // ÑƒÑ€Ð¾Ð²Ð½Ñ�.
    public static final int YOUR_FORCE_HAS_REACHED_MAXIMUM_CAPACITY_ = 324; // Ð’Ð°ÑˆÐ°
    // Ñ�Ð¸Ð»Ð°
    // Ð´Ð¾Ñ�Ñ‚Ð¸Ð³Ð»Ð°
    // Ð¼Ð°ÐºÑ�Ð¸Ð¼Ð°Ð»ÑŒÐ½Ð¾Ð³Ð¾
    // ÑƒÑ€Ð¾Ð²Ð½Ñ�.
    public static final int THE_CORPSE_HAS_ALREADY_DISAPPEARED = 325; // Ð¢Ñ€ÑƒÐ¿
    // ÑƒÐ¶Ðµ
    // Ð¸Ñ�Ñ‡ÐµÐ·.
    public static final int SELECT_TARGET_FROM_LIST = 326; // Ð’Ñ‹Ð±ÐµÑ€Ð¸Ñ‚Ðµ Ñ†ÐµÐ»ÑŒ Ð¸Ð·
    // Ñ�Ð¿Ð¸Ñ�ÐºÐ°.
    public static final int YOU_CANNOT_EXCEED_80_CHARACTERS = 327; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ð²Ð²ÐµÑ�Ñ‚Ð¸
    // Ð±Ð¾Ð»ÑŒÑˆÐµ 80
    // Ñ�Ð¸Ð¼Ð²Ð¾Ð»Ð¾Ð².
    public static final int PLEASE_INPUT_TITLE_USING_LESS_THAN_128_CHARACTERS = 328; // ÐœÐ°ÐºÑ�Ð¸Ð¼Ð°Ð»ÑŒÐ½Ð°Ñ�
    // Ð´Ð»Ð¸Ð½Ð°
    // Ð·Ð°Ð³Ð¾Ð»Ð¾Ð²ÐºÐ°
    // -
    // 128
    // Ñ�Ð¸Ð¼Ð²Ð¾Ð»Ð¾Ð².
    public static final int PLEASE_INPUT_CONTENTS_USING_LESS_THAN_3000_CHARACTERS = 329; // ÐœÐ°ÐºÑ�Ð¸Ð¼Ð°Ð»ÑŒÐ½Ñ‹Ð¹
    // Ð¾Ð±ÑŠÐµÐ¼
    // Ñ‚ÐµÐºÑ�Ñ‚Ð°
    // -
    // 3000
    // Ñ�Ð¸Ð¼Ð²Ð¾Ð»Ð¾Ð².
    public static final int A_ONE_LINE_RESPONSE_MAY_NOT_EXCEED_128_CHARACTERS = 330; // ÐœÐ°ÐºÑ�Ð¸Ð¼Ð°Ð»ÑŒÐ½Ð°Ñ�
    // Ð´Ð»Ð¸Ð½Ð°
    // Ð¾Ñ‚Ð²ÐµÑ‚Ð°
    // -
    // 128
    // Ñ�Ð¸Ð¼Ð²Ð¾Ð»Ð¾Ð².
    public static final int YOU_HAVE_ACQUIRED_S1_SP = 331; // ÐŸÐ¾Ð»ÑƒÑ‡ÐµÐ½Ð¾: $s1 SP.
    public static final int DO_YOU_WANT_TO_BE_RESTORED = 332; // Ð¥Ð¾Ñ‚Ð¸Ñ‚Ðµ
    // Ð²Ð¾Ð·Ñ€Ð¾Ð´Ð¸Ñ‚ÑŒÑ�Ñ�?
    public static final int YOU_HAVE_RECEIVED_S1_DAMAGE_BY_CORES_BARRIER = 333; // ÐŸÐ¾Ð»ÑƒÑ‡ÐµÐ½Ð¾
    // $s1
    // ÑƒÑ€Ð¾Ð½Ð°
    // Ð¾Ñ‚
    // Ð±Ð°Ñ€ÑŒÐµÑ€Ð°
    // Ð¯Ð´Ñ€Ð°.
    public static final int PLEASE_ENTER_STORE_MESSAGE = 334; // Ð’Ð²ÐµÐ´Ð¸Ñ‚Ðµ
    // Ñ�Ð¾Ð¾Ð±Ñ‰ÐµÐ½Ð¸Ðµ.
    public static final int S1_IS_ABORTED = 335; // $s1: Ð¾Ñ‚Ð¼ÐµÐ½ÐµÐ½Ð¾.
    public static final int S1_IS_CRYSTALLIZED_DO_YOU_WANT_TO_CONTINUE = 336; // $s1:
    // Ð¿Ñ€Ð¾Ð´Ð¾Ð»Ð¶Ð¸Ñ‚ÑŒ
    // ÐºÑ€Ð¸Ñ�Ñ‚Ð°Ð»Ð»Ð¸Ð·Ð°Ñ†Ð¸ÑŽ?
    public static final int SOULSHOT_DOES_NOT_MATCH_WEAPON_GRADE = 337; // Ð—Ð°Ñ€Ñ�Ð´
    // Ð”ÑƒÑˆÐ¸
    // Ð½Ðµ
    // Ñ�Ð¾Ð¾Ñ‚Ð²ÐµÑ‚Ñ�Ñ‚Ð²ÑƒÐµÑ‚
    // Ñ€Ð°Ð½Ð³Ñƒ
    // Ð¾Ñ€ÑƒÐ¶Ð¸Ñ�.
    public static final int NOT_ENOUGH_SOULSHOTS = 338; // Ð�ÐµÐ´Ð¾Ñ�Ñ‚Ð°Ñ‚Ð¾Ñ‡Ð½Ð¾ Ð—Ð°Ñ€Ñ�Ð´Ð¾Ð²
    // Ð”ÑƒÑˆÐ¸.
    public static final int CANNOT_USE_SOULSHOTS = 339; // Ð�ÐµÐ»ÑŒÐ·Ñ� Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒ
    // Ð—Ð°Ñ€Ñ�Ð´ Ð”ÑƒÑˆÐ¸.
    public static final int PRIVATE_STORE_UNDER_WAY = 340; // Ð›Ð¸Ñ‡Ð½Ð°Ñ� Ð»Ð°Ð²ÐºÐ°
    // Ð¾Ñ‚ÐºÑ€Ñ‹Ñ‚Ð°.
    public static final int NOT_ENOUGH_MATERIALS = 341; // Ð�ÐµÐ´Ð¾Ñ�Ñ‚Ð°Ñ‚Ð¾Ñ‡Ð½Ð¾
    // Ð¼Ð°Ñ‚ÐµÑ€Ð¸Ð°Ð»Ð¾Ð².
    public static final int POWER_OF_THE_SPIRITS_ENABLED = 342; // Ð’Ð°ÑˆÐµ Ð¾Ñ€ÑƒÐ¶Ð¸Ðµ
    // Ð½Ð°Ð¿Ð¾Ð»Ð½ÐµÐ½Ð¾
    // Ñ�Ð¸Ð»Ð¾Ð¹.
    public static final int SWEEPER_FAILED_TARGET_NOT_SPOILED = 343; // Ð¦ÐµÐ»ÑŒ Ð½Ðµ
    // Ð¾Ñ†ÐµÐ½ÐµÐ½Ð°.
    // Ð�Ðµ
    // ÑƒÐ´Ð°Ð»Ð¾Ñ�ÑŒ
    // Ð½Ð¸Ñ‡ÐµÐ³Ð¾
    // Ð¿Ñ€Ð¸Ñ�Ð²Ð¾Ð¸Ñ‚ÑŒ.
    public static final int POWER_OF_THE_SPIRITS_DISABLED = 344; // Ð¡Ð¸Ð»Ð° Ð´ÑƒÑ…Ð¾Ð²
    // Ð½ÐµÐ´Ð¾Ñ�Ñ‚ÑƒÐ¿Ð½Ð°.
    public static final int CHAT_ENABLED = 345; // Ð§Ð°Ñ‚ Ð²ÐºÐ»ÑŽÑ‡ÐµÐ½.
    public static final int CHAT_DISABLED = 346; // Ð§Ð°Ñ‚ Ð¾Ñ‚ÐºÐ»ÑŽÑ‡ÐµÐ½.
    // 347 ÐºÐ°ÐºÐ¾Ð³Ð¾-Ñ‚Ð¾ Ñ…Ñ€ÐµÐ½Ð° Ð½Ðµ Ð¾Ñ‚Ð¾Ð±Ñ€Ð°Ð¶Ð°ÐµÑ‚Ñ�Ñ� Ð² ÐºÐ»Ð¸ÐµÐ½Ñ‚Ðµ, 351 Ð²Ð¼ÐµÑ�Ñ‚Ð¾ Ð½ÐµÐ³Ð¾
    @Deprecated
    public static final int INCORRECT_ITEM_COUNT_0 = 347; // Ð�ÐµÐ²ÐµÑ€Ð½Ð¾Ðµ ÐºÐ¾Ð»Ð¸Ñ‡ÐµÑ�Ñ‚Ð²Ð¾
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ð¾Ð².
    public static final int INCORRECT_ITEM_PRICE = 348; // Ð�ÐµÐ²ÐµÑ€Ð½Ð°Ñ� Ñ†ÐµÐ½Ð°
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ð°.
    public static final int PRIVATE_STORE_ALREADY_CLOSED = 349; // Ð›Ð¸Ñ‡Ð½Ð°Ñ� Ð»Ð°Ð²ÐºÐ°
    // Ð·Ð°ÐºÑ€Ñ‹Ñ‚Ð°.
    public static final int ITEM_OUT_OF_STOCK = 350; // ÐŸÑ€ÐµÐ´Ð¼ÐµÑ‚ Ð¿Ñ€Ð¾Ð´Ð°Ð½.
    public static final int INCORRECT_ITEM_COUNT = 351; // Ð�ÐµÐ²ÐµÑ€Ð½Ð¾Ðµ ÐºÐ¾Ð»Ð¸Ñ‡ÐµÑ�Ñ‚Ð²Ð¾
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ð°.
    public static final int INCORRECT_ITEM = 352; // Ð�ÐµÐ²ÐµÑ€Ð½Ñ‹Ð¹ Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚.
    public static final int CANNOT_PURCHASE = 353; // Ð�ÐµÐ»ÑŒÐ·Ñ� ÐºÑƒÐ¿Ð¸Ñ‚ÑŒ.
    public static final int CANCEL_ENCHANT = 354; // Ð£Ð»ÑƒÑ‡ÑˆÐµÐ½Ð¸Ðµ Ð¾Ñ‚Ð¼ÐµÐ½ÐµÐ½Ð¾.
    public static final int INAPPROPRIATE_ENCHANT_CONDITIONS = 355; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // ÑƒÐ»ÑƒÑ‡ÑˆÐ¸Ñ‚ÑŒ
    // Ð² Ñ�Ñ‚Ð¸Ñ…
    // ÑƒÑ�Ð»Ð¾Ð²Ð¸Ñ�Ñ….
    public static final int REJECT_RESURRECTION = 356; // Ð’Ð¾Ñ�ÐºÑ€ÐµÑˆÐµÐ½Ð¸Ðµ Ð¾Ñ‚Ð¼ÐµÐ½ÐµÐ½Ð¾.
    public static final int ALREADY_SPOILED = 357; // Ð¦ÐµÐ»ÑŒ ÑƒÐ¶Ðµ Ð¾Ñ†ÐµÐ½ÐµÐ½Ð°.
    public static final int S1_HOUR_S_UNTIL_CASTLE_SIEGE_CONCLUSION = 358; // Ð”Ð¾
    // Ð¾ÐºÐ¾Ð½Ñ‡Ð°Ð½Ð¸Ñ�
    // Ð¾Ñ�Ð°Ð´Ñ‹
    // Ð·Ð°Ð¼ÐºÐ°:
    // $s1
    // Ñ‡.
    public static final int S1_MINUTE_S_UNTIL_CASTLE_SIEGE_CONCLUSION = 359; // Ð”Ð¾
    // Ð¾ÐºÐ¾Ð½Ñ‡Ð°Ð½Ð¸Ñ�
    // Ð¾Ñ�Ð°Ð´Ñ‹
    // Ð·Ð°Ð¼ÐºÐ°:
    // $s1
    // Ð¼Ð¸Ð½.
    public static final int CASTLE_SIEGE_S1_SECOND_S_LEFT = 360; // Ð”Ð¾ Ð¾ÐºÐ¾Ð½Ñ‡Ð°Ð½Ð¸Ñ�
    // Ð¾Ñ�Ð°Ð´Ñ‹ Ð·Ð°Ð¼ÐºÐ°:
    // $s1 Ñ�ÐµÐº.
    public static final int OVER_HIT = 361; // Ð¡Ð²ÐµÑ€Ñ…ÑƒÐ´Ð°Ñ€!
    public static final int ACQUIRED_S1_BONUS_EXPERIENCE_THROUGH_OVER_HIT = 362; // ÐŸÐ¾Ð»ÑƒÑ‡ÐµÐ½Ð¾
    // Ð´Ð¾Ð¿Ð¾Ð»Ð½Ð¸Ñ‚ÐµÐ»ÑŒÐ½Ð¾
    // $s1
    // Ð¾Ð¿Ñ‹Ñ‚Ð°
    // Ð·Ð°
    // Ñ�Ð²ÐµÑ€Ñ…ÑƒÐ´Ð°Ñ€.
    public static final int CHAT_AVAILABLE_TIME_S1_MINUTE = 363; // Ð”Ð¾
    // Ñ€Ð°Ð·Ð±Ð»Ð¾ÐºÐ¸Ñ€Ð¾Ð²ÐºÐ¸
    // Ñ‡Ð°Ñ‚Ð°: $s1
    // Ð¼Ð¸Ð½.
    public static final int ENTER_USERS_NAME_TO_SEARCH = 364; // Ð’Ð²ÐµÐ´Ð¸Ñ‚Ðµ Ð¸Ð¼Ñ�
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð° Ð´Ð»Ñ�
    // Ð¿Ð¾Ð¸Ñ�ÐºÐ°.
    public static final int ARE_YOU_SURE = 365; // Ð’Ñ‹ ÑƒÐ²ÐµÑ€ÐµÐ½Ñ‹?
    public static final int SELECT_HAIR_COLOR = 366; // Ð’Ñ‹Ð±ÐµÑ€Ð¸Ñ‚Ðµ Ñ†Ð²ÐµÑ‚ Ð²Ð¾Ð»Ð¾Ñ�.
    public static final int CANNOT_REMOVE_CLAN_CHARACTER = 367; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // ÑƒÐ´Ð°Ð»Ð¸Ñ‚ÑŒ
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°,
    // Ñ�Ð¾Ñ�Ñ‚Ð¾Ñ�Ñ‰ÐµÐ³Ð¾ Ð²
    // ÐºÐ»Ð°Ð½Ðµ.
    public static final int EQUIPPED__S1_S2 = 368; // +$s1 $s2: Ð½Ð°Ð´ÐµÑ‚Ð¾.
    public static final int YOU_HAVE_OBTAINED__S1S2 = 369; // ÐŸÐ¾Ð»ÑƒÑ‡ÐµÐ½Ð¾: +$s1
    // $s2.
    @Deprecated
    public static final int FAILED_TO_PICK_UP_S1 = 370; // Ð�ÐµÐ»ÑŒÐ·Ñ� Ð¿Ð¾Ð´Ð½Ñ�Ñ‚ÑŒ: +$s1
    // $s2.
    public static final int ACQUIRED__S1_S2 = 371; // ÐŸÐ¾Ð»ÑƒÑ‡ÐµÐ½Ð¾: +$s1 $s2.
    @Deprecated
    public static final int FAILED_TO_EARN_S1 = 372; // Ð�ÐµÐ»ÑŒÐ·Ñ� Ð¿Ð¾Ð»ÑƒÑ‡Ð¸Ñ‚ÑŒ: +$s1
    // $s2.
    public static final int DESTROY__S1_S2_DO_YOU_WISH_TO_CONTINUE = 373; // +$s1
    // $s2
    // -
    // ÑƒÐ½Ð¸Ñ‡Ñ‚Ð¾Ð¶Ð¸Ñ‚ÑŒ?
    public static final int CRYSTALLIZE__S1_S2_DO_YOU_WISH_TO_CONTINUE = 374; // +$s1
    // $s2
    // -
    // ÐºÑ€Ð¸Ñ�Ñ‚Ð°Ð»Ð»Ð¸Ð·Ð¸Ñ€Ð¾Ð²Ð°Ñ‚ÑŒ?
    public static final int DROPPED__S1_S2 = 375; // Ð’Ñ‹ ÑƒÑ€Ð¾Ð½Ð¸Ð»Ð¸: +$s1 $s2.
    public static final int S1_HAS_OBTAINED__S2S3 = 376; // $c1 Ð¿Ð¾Ð»ÑƒÑ‡Ð°ÐµÑ‚: +$s2
    // $s3.
    public static final int _S1_S2_DISAPPEARED = 377; // +$s1 $s2 Ð¸Ñ�Ñ‡ÐµÐ·Ð°ÐµÑ‚.
    public static final int S1_PURCHASED_S2 = 378; // $c1 Ð¿Ð¾ÐºÑƒÐ¿Ð°ÐµÑ‚: $s2.
    public static final int S1_PURCHASED__S2_S3 = 379; // $c1 Ð¿Ð¾ÐºÑƒÐ¿Ð°ÐµÑ‚: +$s2
    // $s3.
    public static final int S1_PURCHASED_S3_S2_S = 380; // $c1 Ð¿Ð¾ÐºÑƒÐ¿Ð°ÐµÑ‚: $s2
    // ($s3 ÑˆÑ‚.).
    public static final int CANNOT_CONNECT_TO_PETITION_SERVER = 381; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ð¿Ð¾Ð´ÐºÐ»ÑŽÑ‡Ð¸Ñ‚ÑŒÑ�Ñ�
    // Ðº
    // Ñ�ÐµÑ€Ð²ÐµÑ€Ñƒ
    // Ð¿ÐµÑ‚Ð¸Ñ†Ð¸Ð¹.
    public static final int CURRENTLY_THERE_ARE_NO_USERS_THAT_HAVE_CHECKED_OUT_A_GM_ID = 382; // Ð’
    // Ð´Ð°Ð½Ð½Ñ‹Ð¹
    // Ð¼Ð¾Ð¼ÐµÐ½Ñ‚
    // Ð½Ð°
    // Ð¸Ð³Ñ€Ðµ
    // Ð½ÐµÑ‚
    // Ð˜Ð³Ñ€Ð¾Ð²Ñ‹Ñ…
    // Ð¼Ð°Ñ�Ñ‚ÐµÑ€Ð¾Ð².
    public static final int REQUEST_CONFIRMED_TO_END_CONSULTATION_AT_PETITION_SERVER = 383; // Ð—Ð°Ð¿Ñ€Ð¾Ñ�
    // Ð¾
    // Ð·Ð°Ð²ÐµÑ€ÑˆÐµÐ½Ð¸Ð¸
    // ÐºÐ¾Ð½Ñ�ÑƒÐ»ÑŒÑ‚Ð°Ñ†Ð¸Ð¸
    // Ð½Ð°
    // Ñ�ÐµÑ€Ð²ÐµÑ€Ðµ
    // Ð¿ÐµÑ‚Ð¸Ñ†Ð¸Ð¹
    // Ð¿Ð¾Ð´Ñ‚Ð²ÐµÑ€Ð¶Ð´ÐµÐ½.
    public static final int THE_CLIENT_IS_NOT_LOGGED_ONTO_THE_GAME_SERVER = 384; // ÐšÐ»Ð¸ÐµÐ½Ñ‚Ð°
    // Ð½ÐµÑ‚
    // Ð²
    // Ð¸Ð³Ñ€Ðµ.
    public static final int REQUEST_CONFIRMED_TO_BEGIN_CONSULTATION_AT_PETITION_SERVER = 385; // Ð—Ð°Ð¿Ñ€Ð¾Ñ�
    // Ð¾
    // Ð½Ð°Ñ‡Ð°Ð»Ðµ
    // ÐºÐ¾Ð½Ñ�ÑƒÐ»ÑŒÑ‚Ð°Ñ†Ð¸Ð¸
    // Ð½Ð°
    // Ñ�ÐµÑ€Ð²ÐµÑ€
    // Ð¾Ð±Ñ€Ð°Ñ‰ÐµÐ½Ð¸Ð¹
    // Ð¿Ð¾Ð´Ñ‚Ð²ÐµÑ€Ð¶Ð´ÐµÐ½.
    public static final int PETITION_REQUESTS_MUST_BE_OVER_FIVE_CHARACTERS = 386; // ÐœÐ¸Ð½Ð¸Ð¼Ð°Ð»ÑŒÐ½Ñ‹Ð¹
    // Ñ€Ð°Ð·Ð¼ÐµÑ€
    // Ñ�Ð¾Ð¾Ð±Ñ‰ÐµÐ½Ð¸Ñ�
    // -
    // 6
    // Ñ�Ð¸Ð¼Ð²Ð¾Ð»Ð¾Ð².
    public static final int ENDING_PETITION_CONSULTATION = 387; // Ð¡Ð»ÑƒÐ¶Ð±Ð°
    // Ð¿Ð¾Ð´Ð´ÐµÑ€Ð¶ÐºÐ¸
    // Ð¾Ñ‚Ð²ÐµÑ‚Ð¸Ð»Ð° Ð½Ð°
    // Ð’Ð°ÑˆÑƒ Ð·Ð°Ñ�Ð²ÐºÑƒ.
    // \\nÐŸÐ¾Ð¶Ð°Ð»ÑƒÐ¹Ñ�Ñ‚Ð°,
    // Ð¿Ñ€Ð¾ÐºÐ¾Ð¼Ð¼ÐµÐ½Ñ‚Ð¸Ñ€ÑƒÐ¹Ñ‚Ðµ
    // ÐºÐ°Ñ‡ÐµÑ�Ñ‚Ð²Ð¾
    // Ð¾Ð±Ñ�Ð»ÑƒÐ¶Ð¸Ð²Ð°Ð½Ð¸Ñ�.
    public static final int NOT_UNDER_PETITION_CONSULTATION = 388; // Ð�Ð° Ð´Ð°Ð½Ð½Ñ‹Ð¹
    // Ð¼Ð¾Ð¼ÐµÐ½Ñ‚ Ð½Ðµ
    // Ð²ÐµÐ´ÐµÑ‚Ñ�Ñ�
    // ÐºÐ¾Ð½Ñ�ÑƒÐ»ÑŒÑ‚Ð°Ñ†Ð¸Ð¹.
    public static final int PETITION_APPLICATION_ACCEPTED_RECEIPT_NO_IS_S1 = 389; // Ð’Ð°ÑˆÐ°
    // Ð·Ð°Ñ�Ð²ÐºÐ°
    // Ð¿Ñ€Ð¸Ð½Ñ�Ñ‚Ð°.
    // \\n
    // -
    // Ð�Ð¾Ð¼ÐµÑ€
    // Ð·Ð°Ñ�Ð²ÐºÐ¸:
    // $s1.
    public static final int ALREADY_APPLIED_FOR_PETITION = 390; // Ð’Ñ‹ Ð½Ðµ Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¿Ð¾Ð´Ð°Ñ‚ÑŒ Ð±Ð¾Ð»ÑŒÑˆÐµ
    // 1 Ð·Ð°Ñ�Ð²ÐºÐ¸.
    public static final int RECEIPT_NO_S1_PETITION_CANCELLED = 391; // Ð—Ð°Ñ�Ð²ÐºÐ°
    // â„–$s1
    // Ð¾Ñ‚Ð¼ÐµÐ½ÐµÐ½Ð°.
    public static final int UNDER_PETITION_ADVICE = 392; // Ð�Ð° Ð´Ð°Ð½Ð½Ñ‹Ð¹ Ð¼Ð¾Ð¼ÐµÐ½Ñ‚
    // Ð²ÐµÐ´ÐµÑ‚Ñ�Ñ�
    // ÐºÐ¾Ð½Ñ�ÑƒÐ»ÑŒÑ‚Ð°Ñ†Ð¸Ñ�.
    public static final int FAILED_TO_CANCEL_PETITION_PLEASE_TRY_AGAIN_LATER = 393; // Ð�Ðµ
    // ÑƒÐ´Ð°Ð»Ð¾Ñ�ÑŒ
    // Ð¾Ñ‚Ð¼ÐµÐ½Ð¸Ñ‚ÑŒ
    // Ð·Ð°Ñ�Ð²ÐºÑƒ.
    // ÐŸÐ¾Ð¶Ð°Ð»ÑƒÐ¹Ñ�Ñ‚Ð°,
    // Ð¿Ð¾Ð¿Ñ€Ð¾Ð±ÑƒÐ¹Ñ‚Ðµ
    // Ð¿Ð¾Ð·Ð¶Ðµ.
    public static final int PETITION_CONSULTATION_WITH_S1_UNDER_WAY = 394; // Ð‘ÐµÑ�ÐµÐ´Ð°
    // Ñ�
    // Ð¸Ð³Ñ€Ð¾ÐºÐ¾Ð¼
    // $c1
    // Ð½Ð°Ñ‡Ð°Ð»Ð°Ñ�ÑŒ.
    public static final int ENDING_PETITION_CONSULTATION_WITH_S1 = 395; // Ð‘ÐµÑ�ÐµÐ´Ð°
    // Ñ�
    // Ð¸Ð³Ñ€Ð¾ÐºÐ¾Ð¼
    // $c1
    // Ð·Ð°ÐºÐ¾Ð½Ñ‡Ð¸Ð»Ð°Ñ�ÑŒ.
    public static final int PLEASE_LOGIN_AFTER_CHANGING_YOUR_TEMPORARY_PASSWORD = 396; // Ð˜Ð·Ð¼ÐµÐ½Ð¸Ñ‚Ðµ
    // Ð²Ñ€ÐµÐ¼ÐµÐ½Ð½Ñ‹Ð¹
    // Ð¿Ð°Ñ€Ð¾Ð»ÑŒ
    // Ð½Ð°
    // Ñ�Ð°Ð¹Ñ‚Ðµ
    // Ð¸
    // Ð·Ð°Ð¹Ð´Ð¸Ñ‚Ðµ
    // Ð²
    // Ð¸Ð³Ñ€Ñƒ.
    public static final int NOT_A_PAID_ACCOUNT = 397; // Ð�ÐºÐºÐ°ÑƒÐ½Ñ‚ Ð½Ðµ Ð¾Ð¿Ð»Ð°Ñ‡ÐµÐ½.
    public static final int YOU_HAVE_NO_MORE_TIME_LEFT_ON_YOUR_ACCOUNT = 398; // Ð˜Ñ�Ñ‚ÐµÐºÐ»Ð¾
    // Ð¾Ð¿Ð»Ð°Ñ‡ÐµÐ½Ð½Ð¾Ðµ
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ð¸Ð³Ñ€Ñ‹
    // Ð½Ð°
    // Ð’Ð°ÑˆÐµÐ¼
    // Ð°ÐºÐºÐ°ÑƒÐ½Ñ‚Ðµ.
    public static final int SYSTEM_ERROR = 399; // ÐžÑˆÐ¸Ð±ÐºÐ° Ñ�Ð¸Ñ�Ñ‚ÐµÐ¼Ñ‹.
    public static final int DISCARD_S1_DO_YOU_WISH_TO_CONTINUE = 400; // $s1 -
    // Ð²Ñ‹Ð±Ñ€Ð¾Ñ�Ð¸Ñ‚ÑŒ?
    public static final int TOO_MANY_QUESTS_IN_PROGRESS = 401; // Ð£ Ð’Ð°Ñ� Ñ�Ð»Ð¸ÑˆÐºÐ¾Ð¼
    // Ð¼Ð½Ð¾Ð³Ð¾ ÐºÐ²ÐµÑ�Ñ‚Ð¾Ð².
    public static final int YOU_MAY_NOT_GET_ON_BOARD_WITHOUT_A_PASS = 402; // Ð‘ÐµÐ·Ð±Ð¸Ð»ÐµÑ‚Ð½Ñ‹Ð¹
    // Ð¿Ñ€Ð¾ÐµÐ·Ð´
    // Ð·Ð°Ð¿Ñ€ÐµÑ‰ÐµÐ½.
    public static final int YOU_HAVE_EXCEEDED_YOUR_POCKET_MONEY_LIMIT = 403; // ÐŸÑ€ÐµÐ²Ñ‹ÑˆÐµÐ½
    // Ð»Ð¸Ð¼Ð¸Ñ‚
    // Ð°Ð´ÐµÐ½.
    public static final int CREATE_ITEM_LEVEL_IS_TOO_LOW_TO_REGISTER_THIS_RECIPE = 404; // Ð£Ñ€Ð¾Ð²ÐµÐ½ÑŒ
    // ÑƒÐ¼ÐµÐ½Ð¸Ñ�
    // Â«Ð¡Ð¾Ð·Ð´Ð°Ñ‚ÑŒ
    // ÐŸÑ€ÐµÐ´Ð¼ÐµÑ‚Â»
    // Ñ�Ð»Ð¸ÑˆÐºÐ¾Ð¼
    // Ð½Ð¸Ð·Ð¾Ðº
    // Ð´Ð»Ñ�
    // Ñ€ÐµÐ³Ð¸Ñ�Ñ‚Ñ€Ð°Ñ†Ð¸Ð¸
    // Ñ€ÐµÑ†ÐµÐ¿Ñ‚Ð°.
    public static final int THE_TOTAL_PRICE_OF_THE_PRODUCT_IS_TOO_HIGH = 405; // ÐžÐ±Ñ‰Ð°Ñ�
    // Ñ�Ñ‚Ð¾Ð¸Ð¼Ð¾Ñ�Ñ‚ÑŒ
    // Ñ�Ð»Ð¸ÑˆÐºÐ¾Ð¼
    // Ð²Ñ‹Ñ�Ð¾ÐºÐ°.
    public static final int PETITION_APPLICATION_ACCEPTED = 406; // Ð—Ð°Ñ�Ð²ÐºÐ°
    // Ð¿Ñ€Ð¸Ð½Ñ�Ñ‚Ð°.
    public static final int PETITION_UNDER_PROCESS = 407; // ÐŸÐµÑ‚Ð¸Ñ†Ð¸Ñ� Ð½Ð°Ñ…Ð¾Ð´Ð¸Ñ‚Ñ�Ñ� Ð²
    // Ñ�Ñ‚Ð°Ð´Ð¸Ð¸ Ð¿Ñ€Ð¾Ð²ÐµÑ€ÐºÐ¸.
    public static final int SET_PERIOD = 408; // Ð£Ñ�Ñ‚Ð°Ð½Ð¾Ð²ÐºÐ° Ð¾Ñ�Ð°Ð´Ñ‹
    public static final int SET_TIME_S1_S2_S3 = 409; // Ð’Ñ‹Ð±Ð¾Ñ€ Ð²Ñ€ÐµÐ¼ÐµÐ½Ð¸: $s1 Ñ‡ $s2
    // Ð¼Ð¸Ð½ $s3 Ñ�ÐµÐº.
    public static final int REGISTRATION_PERIOD = 410; // Ð ÐµÐ³Ð¸Ñ�Ñ‚Ñ€Ð°Ñ†Ð¸Ñ� Ð¾Ñ�Ð°Ð´Ñ‹
    public static final int REGISTRATION_TIME_S1_S2_S3 = 411; // Ð’Ñ€ÐµÐ¼Ñ�
    // Ñ€ÐµÐ³Ð¸Ñ�Ñ‚Ñ€Ð°Ñ†Ð¸Ð¸:
    // $s1 Ñ‡ $s2 Ð¼Ð¸Ð½
    // $s3 Ñ�ÐµÐº.
    public static final int BATTLE_BEGINS_IN_S1_S2_S4 = 412; // Ð�Ð°Ñ‡Ð°Ð»Ð¾ Ð¾Ñ�Ð°Ð´Ñ‹:
    // $s1 Ñ‡ $s2 Ð¼Ð¸Ð½
    // $s4 Ñ�ÐµÐº.
    public static final int BATTLE_ENDS_IN_S1_S2_S5 = 413; // ÐžÐºÐ¾Ð½Ñ‡Ð°Ð½Ð¸Ðµ Ð¾Ñ�Ð°Ð´Ñ‹:
    // $s1 Ñ‡ $s2 Ð¼Ð¸Ð½ $s5
    // Ñ�ÐµÐº.
    public static final int STANDBY = 414; // ÐžÐ¶Ð¸Ð´Ð°Ð½Ð¸Ðµ Ð¾Ñ�Ð°Ð´Ñ‹
    public static final int UNDER_SIEGE = 415; // Ð’ Ð¿Ñ€Ð¾Ñ†ÐµÑ�Ñ�Ðµ Ð¾Ñ�Ð°Ð´Ñ‹
    public static final int CANNOT_BE_EXCHANGED = 416; // Ð�ÐµÐ»ÑŒÐ·Ñ� Ð¾Ð±Ð¼ÐµÐ½Ñ�Ñ‚ÑŒ.
    public static final int S1__HAS_BEEN_DISARMED = 417; // Ð¡Ð½Ñ�Ñ‚Ð¾: $s1.
    public static final int THERE_IS_A_SIGNIFICANT_DIFFERENCE_BETWEEN_THE_ITEMS_PRICE_AND_ITS_STANDARD_PRICE_PLEASE_CHECK_AGAIN = 418; // Ð¦ÐµÐ½Ð°
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ð°
    // Ñ�Ð»Ð¸ÑˆÐºÐ¾Ð¼
    // Ð²Ñ‹Ñ�Ð¾ÐºÐ°
    // Ð¿Ð¾
    // Ñ�Ñ€Ð°Ð²Ð½ÐµÐ½Ð¸ÑŽ
    // Ñ�Ð¾
    // Ñ�Ñ‚Ð°Ð½Ð´Ð°Ñ€Ñ‚Ð½Ð¾Ð¹.
    // ÐŸÐ¾Ð¶Ð°Ð»ÑƒÐ¹Ñ�Ñ‚Ð°,
    // Ð¸Ð·Ð¼ÐµÐ½Ð¸Ñ‚Ðµ
    // Ñ†ÐµÐ½Ñƒ.
    public static final int S1_MINUTE_S_OF_DESIGNATED_USAGE_TIME_LEFT = 419; // Ð”Ð¾
    // Ð¾ÐºÐ¾Ð½Ñ‡Ð°Ð½Ð¸Ñ�
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ð½Ð¸Ñ�:
    // $s1
    // Ð¼Ð¸Ð½.
    public static final int TIME_EXPIRED = 420; // Ð’Ñ€ÐµÐ¼Ñ� Ð¸Ñ�Ñ‚ÐµÐºÐ»Ð¾.
    public static final int ANOTHER_PERSON_HAS_LOGGED_IN_WITH_THE_SAME_ACCOUNT = 421; // Ð­Ñ‚Ð¾Ñ‚
    // Ð°ÐºÐºÐ°ÑƒÐ½Ñ‚
    // ÑƒÐ¶Ðµ
    // Ð²
    // Ð¸Ð³Ñ€Ðµ.
    public static final int YOU_HAVE_EXCEEDED_THE_WEIGHT_LIMIT = 422; // ÐŸÑ€ÐµÐ²Ñ‹ÑˆÐµÐ½
    // Ð»Ð¸Ð¼Ð¸Ñ‚
    // Ð²ÐµÑ�Ð°.
    public static final int THE_SCROLL_OF_ENCHANT_HAS_BEEN_CANCELED = 423; // Ð’Ñ‹
    // Ð¾Ñ‚Ð¼ÐµÐ½Ð¸Ð»Ð¸
    // Ð¿Ñ€Ð¾Ñ†ÐµÑ�Ñ�
    // ÑƒÐ»ÑƒÑ‡ÑˆÐµÐ½Ð¸Ñ�.
    public static final int DOES_NOT_FIT_STRENGTHENING_CONDITIONS_OF_THE_SCROLL = 424; // Ð�ÐµÑ�Ð¾Ð¾Ñ‚Ð²ÐµÑ‚Ñ�Ñ‚Ð²Ð¸Ðµ
    // ÑƒÑ�Ð»Ð¾Ð²Ð¸Ð¹
    // Ñ�Ð²Ð¸Ñ‚ÐºÐ°
    // ÑƒÐ»ÑƒÑ‡ÑˆÐµÐ½Ð¸Ñ�.
    public static final int YOUR_CREATE_ITEM_LEVEL_IS_TOO_LOW = 425; // Ð£Ñ€Ð¾Ð²ÐµÐ½ÑŒ
    // ÑƒÐ¼ÐµÐ½Ð¸Ñ�
    // Â«Ð¡Ð¾Ð·Ð´Ð°Ñ‚ÑŒ
    // ÐŸÑ€ÐµÐ´Ð¼ÐµÑ‚Â»
    // Ñ�Ð»Ð¸ÑˆÐºÐ¾Ð¼
    // Ð½Ð¸Ð·Ð¾Ðº
    // Ð´Ð»Ñ�
    // Ñ€ÐµÐ³Ð¸Ñ�Ñ‚Ñ€Ð°Ñ†Ð¸Ð¸
    // Ñ€ÐµÑ†ÐµÐ¿Ñ‚Ð°.
    public static final int YOUR_ACCOUNT_HAS_BEEN_REPORTED_FOR_INTENTIONALLY_NOT_PAYING_THE_CYBER_CAFE_FEES = 426; // ÐžÑ‚
    // Ð°Ð´Ð¼Ð¸Ð½Ð¸Ñ�Ñ‚Ñ€Ð°Ñ†Ð¸Ð¸
    // ÐºÐ¾Ð¼Ð¿ÑŒÑŽÑ‚ÐµÑ€Ð½Ð¾Ð³Ð¾
    // ÐºÐ»ÑƒÐ±Ð°
    // Ð¿Ð¾Ñ�Ñ‚ÑƒÐ¿Ð¸Ð»Ð°
    // Ð¶Ð°Ð»Ð¾Ð±Ð°,
    // Ñ‡Ñ‚Ð¾
    // Ð’Ð°ÑˆÐ°
    // ÑƒÑ‡ÐµÑ‚Ð½Ð°Ñ�
    // Ð·Ð°Ð¿Ð¸Ñ�ÑŒ
    // Ð±Ñ‹Ð»Ð°
    // Ð·Ð°Ð¼ÐµÑ‡ÐµÐ½Ð°
    // Ð½Ð°
    // ÐºÐ¾Ð¼Ð¿ÑŒÑŽÑ‚ÐµÑ€Ðµ
    // Ñ�
    // Ð½ÐµÐ¾Ð¿Ð»Ð°Ñ‡ÐµÐ½Ð½Ñ‹Ð¼
    // Ð²Ñ€ÐµÐ¼ÐµÐ½ÐµÐ¼.
    public static final int PLEASE_CONTACT_US = 427; // ÐŸÐ¾Ð¶Ð°Ð»ÑƒÐ¹Ñ�Ñ‚Ð°, Ñ�Ð²Ñ�Ð¶Ð¸Ñ‚ÐµÑ�ÑŒ Ñ�Ð¾
    // Ñ�Ð»ÑƒÐ¶Ð±Ð¾Ð¹ Ð¿Ð¾Ð´Ð´ÐµÑ€Ð¶ÐºÐ¸.
    public static final int IN_ACCORDANCE_WITH_COMPANY_POLICY_YOUR_ACCOUNT_HAS_BEEN_SUSPENDED_DUE_TO_SUSPICION_OF_ILLEGAL = 428; // Ð’
    // Ñ�Ð¾Ð¾Ñ‚Ð²ÐµÑ‚Ñ�Ñ‚Ð²Ð¸Ð¸
    // Ñ�
    // Ð¿Ð¾Ð»Ð¸Ñ‚Ð¸ÐºÐ¾Ð¹
    // ÐºÐ¾Ð¼Ð¿Ð°Ð½Ð¸Ð¸,
    // Ð’Ð°Ñˆ
    // Ð°ÐºÐºÐ°ÑƒÐ½Ñ‚
    // Ð±Ñ‹Ð»
    // Ð·Ð°Ð±Ð»Ð¾ÐºÐ¸Ñ€Ð¾Ð²Ð°Ð½
    // Ð·Ð°
    // Ð¿Ð¾Ð´Ð¾Ð·Ñ€ÐµÐ½Ð¸Ðµ
    // Ð²
    // Ð½ÐµÐ»ÐµÐ³Ð°Ð»ÑŒÐ½Ð¾Ð¹
    // Ð´ÐµÑ�Ñ‚ÐµÐ»ÑŒÐ½Ð¾Ñ�Ñ‚Ð¸/Ð¿Ñ€Ð¸Ñ�Ð²Ð¾ÐµÐ½Ð¸Ð¸
    // Ñ‡ÑƒÐ¶Ð¸Ñ…
    // Ð´Ð°Ð½Ð½Ñ‹Ñ….
    // Ð•Ñ�Ð»Ð¸
    // Ð’Ñ‹
    // Ð½Ðµ
    // Ñ�Ñ‡Ð¸Ñ‚Ð°ÐµÑ‚Ðµ
    // Ñ�ÐµÐ±Ñ�
    // Ð²Ð¸Ð½Ð¾Ð²Ð½Ñ‹Ð¼,
    // Ð¿Ð¾Ñ�ÐµÑ‚Ð¸Ñ‚Ðµ
    // Ð½Ð°Ñˆ
    // Ñ�Ð°Ð¹Ñ‚
    // Ð¸
    // Ð¾Ð±Ñ€Ð°Ñ‚Ð¸Ñ‚ÐµÑ�ÑŒ
    // Ð²
    // Ñ�Ð»ÑƒÐ¶Ð±Ñƒ
    // Ð¿Ð¾Ð´Ð´ÐµÑ€Ð¶ÐºÐ¸
    // Ð´Ð»Ñ�
    // Ð¿Ð¾Ð´Ð°Ñ‡Ð¸
    // Ð°Ð¿ÐµÐ»Ð»Ñ�Ñ†Ð¸Ð¸.
    public static final int IN_ACCORDANCE_WITH_COMPANY_POLICY_YOUR_ACCOUNT_HAS_BEEN_SUSPENDED_DUE_TO_FALSELY_REPORTING_A = 429; // Ð’
    // Ñ�Ð¾Ð¾Ñ‚Ð²ÐµÑ‚Ñ�Ñ‚Ð²Ð¸Ð¸
    // Ñ�
    // Ð¿Ð¾Ð»Ð¸Ñ‚Ð¸ÐºÐ¾Ð¹
    // ÐºÐ¾Ð¼Ð¿Ð°Ð½Ð¸Ð¸,
    // Ð’Ð°Ñˆ
    // Ð°ÐºÐºÐ°ÑƒÐ½Ñ‚
    // Ð±Ñ‹Ð»
    // Ð·Ð°Ð±Ð»Ð¾ÐºÐ¸Ñ€Ð¾Ð²Ð°Ð½
    // Ð·Ð°
    // Ð»Ð¾Ð¶Ð½Ñ‹Ð¹
    // Ð´Ð¾ÐºÐ»Ð°Ð´
    // Ð¾
    // Ð¿Ñ€Ð¸Ñ�Ð²Ð¾ÐµÐ½Ð¸Ð¸
    // Ð°ÐºÐºÐ°ÑƒÐ½Ñ‚Ð°.
    // Ð¢Ð°ÐºÐ¸Ðµ
    // Ð´Ð¾ÐºÐ»Ð°Ð´Ñ‹
    // Ð¼Ð¾Ð³ÑƒÑ‚
    // Ð½Ð°Ð²Ñ€ÐµÐ´Ð¸Ñ‚ÑŒ
    // Ñ‡ÐµÑ�Ñ‚Ð½Ñ‹Ð¼
    // Ð¸Ð³Ñ€Ð¾ÐºÐ°Ð¼.
    // Ð—Ð°
    // Ð´Ð¾Ð¿Ð¾Ð»Ð½Ð¸Ñ‚ÐµÐ»ÑŒÐ½Ð¾Ð¹
    // Ð¸Ð½Ñ„Ð¾Ñ€Ð¼Ð°Ñ†Ð¸ÐµÐ¹
    // Ð¾Ð±Ñ€Ð°Ñ‚Ð¸Ñ‚ÐµÑ�ÑŒ
    // Ð²
    // Ñ�Ð»ÑƒÐ¶Ð±Ñƒ
    // Ð¿Ð¾Ð´Ð´ÐµÑ€Ð¶ÐºÐ¸
    // Ð½Ð°
    // Ñ�Ð°Ð¹Ñ‚Ðµ.
    public static final int __DOESNT_NEED_TO_TRANSLATE = 430; // (Ð¿ÐµÑ€ÐµÐ²Ð¾Ð´ Ð½Ðµ
    // Ð½ÑƒÐ¶ÐµÐ½)
    public static final int YOUR_ACCOUNT_HAS_BEEN_SUSPENDED_DUE_TO_VIOLATING_THE_EULA_ROC_AND_OR_USER_AGREEMENT_CHAPTER_4 = 431; // Ð’Ð°Ñˆ
    // Ð°ÐºÐºÐ°ÑƒÐ½Ñ‚
    // Ð±Ñ‹Ð»
    // Ð·Ð°Ð±Ð»Ð¾ÐºÐ¸Ñ€Ð¾Ð²Ð°Ð½
    // Ð·Ð°
    // Ð½Ð°Ñ€ÑƒÑˆÐµÐ½Ð¸Ðµ
    // ÐŸÐ¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÐµÐ»ÑŒÑ�ÐºÐ¾Ð³Ð¾
    // Ð¡Ð¾Ð³Ð»Ð°ÑˆÐµÐ½Ð¸Ñ�.
    // Ð’
    // Ñ�Ð»ÑƒÑ‡Ð°Ðµ
    // Ð½ÐµÑ�Ð¾Ð±Ð»ÑŽÐ´ÐµÐ½Ð¸Ñ�
    // ÑƒÑ�Ð»Ð¾Ð²Ð¸Ð¹
    // Ñ�Ð¾Ð³Ð»Ð°ÑˆÐµÐ½Ð¸Ñ�
    // Ñ�Ð¾
    // Ñ�Ñ‚Ð¾Ñ€Ð¾Ð½Ñ‹
    // Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÐµÐ»Ñ�
    // ÐºÐ¾Ð¼Ð¿Ð°Ð½Ð¸Ñ�
    // Ð¸Ð¼ÐµÐµÑ‚
    // Ð¿Ñ€Ð°Ð²Ð¾
    // Ð·Ð°Ð±Ð»Ð¾ÐºÐ¸Ñ€Ð¾Ð²Ð°Ñ‚ÑŒ
    // ÐµÐ³Ð¾
    // ÑƒÑ‡ÐµÑ‚Ð½ÑƒÑŽ
    // Ð·Ð°Ð¿Ð¸Ñ�ÑŒ.
    // Ð—Ð°
    // Ð´Ð¾Ð¿Ð¾Ð»Ð½Ð¸Ñ‚ÐµÐ»ÑŒÐ½Ð¾Ð¹
    // Ð¸Ð½Ñ„Ð¾Ñ€Ð¼Ð°Ñ†Ð¸ÐµÐ¹
    // Ð¾Ð±Ñ€Ð°Ñ‚Ð¸Ñ‚ÐµÑ�ÑŒ
    // Ð²
    // Ñ�Ð»ÑƒÐ¶Ð±Ñƒ
    // Ð¿Ð¾Ð´Ð´ÐµÑ€Ð¶ÐºÐ¸
    // Ð½Ð°
    // Ð½Ð°ÑˆÐµÐ¼
    // Ñ�Ð°Ð¹Ñ‚Ðµ.
    public static final int YOUR_ACCOUNT_HAS_BEEN_SUSPENDED_FOR_7_DAYS_RETROACTIVE_TO_THE_DAY_OF_DISCLOSURE_UNDER_CHAPTER_3 = 432; // Ð’Ð°Ñˆ
    // Ð°ÐºÐºÐ°ÑƒÐ½Ñ‚
    // Ð±Ñ‹Ð»
    // Ð·Ð°Ð±Ð»Ð¾ÐºÐ¸Ñ€Ð¾Ð²Ð°Ð½
    // Ð½Ð°
    // 7
    // Ð´Ð½ÐµÐ¹
    // (Ñ�Ð¾
    // Ð´Ð½Ñ�
    // Ð¾Ð³Ð»Ð°ÑˆÐµÐ½Ð¸Ñ�)
    // Ð²
    // Ñ�Ð¾Ð¾Ñ‚Ð²ÐµÑ‚Ñ�Ñ‚Ð²Ð¸Ð¸
    // Ñ�
    // ÐŸÐ¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÐµÐ»ÑŒÑ�ÐºÐ¸Ð¼
    // Ð¡Ð¾Ð³Ð»Ð°ÑˆÐµÐ½Ð¸ÐµÐ¼
    // Ð·Ð°
    // Ð¿Ñ€Ð¾Ð²ÐµÐ´ÐµÐ½Ð¸Ðµ
    // Ð´ÐµÐ½ÐµÐ¶Ð½Ñ‹Ñ…
    // Ð¾Ð¿ÐµÑ€Ð°Ñ†Ð¸Ð¹
    // (Ð¸Ð»Ð¸
    // Ð¸Ñ…
    // Ð¿Ð¾Ð¿Ñ‹Ñ‚ÐºÑƒ)
    // Ñ�
    // Ð¸Ð³Ñ€Ð¾Ð²Ñ‹Ð¼Ð¸
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ð°Ð¼Ð¸,
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°Ð¼Ð¸
    // (Ð°ÐºÐºÐ°ÑƒÐ½Ñ‚Ð°Ð¼Ð¸)
    // Ð¸Ð»Ð¸
    // Ð¸Ð³Ñ€Ð¾Ð²Ð¾Ð¹
    // Ð²Ð°Ð»ÑŽÑ‚Ð¾Ð¹.
    // Ð§ÐµÑ€ÐµÐ·
    // 7
    // Ð´Ð½ÐµÐ¹
    // Ð’Ð°Ñˆ
    // Ð°ÐºÐºÐ°ÑƒÐ½Ñ‚
    // Ð°Ð²Ñ‚Ð¾Ð¼Ð°Ñ‚Ð¸Ñ‡ÐµÑ�ÐºÐ¸
    // Ñ€Ð°Ð·Ð±Ð»Ð¾ÐºÐ¸Ñ€ÑƒÐµÑ‚Ñ�Ñ�.
    // Ð—Ð°
    // Ð´Ð¾Ð¿Ð¾Ð»Ð½Ð¸Ñ‚ÐµÐ»ÑŒÐ½Ð¾Ð¹
    // Ð¸Ð½Ñ„Ð¾Ñ€Ð¼Ð°Ñ†Ð¸ÐµÐ¹
    // Ð¾Ð±Ñ€Ð°Ñ‚Ð¸Ñ‚ÐµÑ�ÑŒ
    // Ð²
    // Ñ�Ð»ÑƒÐ¶Ð±Ñƒ
    // Ð¿Ð¾Ð´Ð´ÐµÑ€Ð¶ÐºÐ¸
    // Ð½Ð°
    // Ð½Ð°ÑˆÐµÐ¼
    // Ñ�Ð°Ð¹Ñ‚Ðµ.
    public static final int YOUR_ACCOUNT_HAS_BEEN_SUSPENDED_UNDER_CHAPTER_3_SECTION_14_OF_THE_LINEAGE_II_SERVICE_USE = 433; // Ð’Ð°Ñˆ
    // Ð°ÐºÐºÐ°ÑƒÐ½Ñ‚
    // Ð±Ñ‹Ð»
    // Ð·Ð°Ð±Ð»Ð¾ÐºÐ¸Ñ€Ð¾Ð²Ð°Ð½
    // Ð²
    // Ñ�Ð¾Ð¾Ñ‚Ð²ÐµÑ‚Ñ�Ñ‚Ð²Ð¸Ð¸
    // Ñ�
    // ÐŸÐ¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÐµÐ»ÑŒÑ�ÐºÐ¸Ð¼
    // Ð¡Ð¾Ð³Ð»Ð°ÑˆÐµÐ½Ð¸ÐµÐ¼
    // Ð·Ð°
    // Ð¾Ð±Ð¼ÐµÐ½
    // (Ð¸Ð»Ð¸
    // Ð¿Ð¾Ð¿Ñ‹Ñ‚ÐºÑƒ
    // Ð¾Ð±Ð¼ÐµÐ½Ð°)
    // Ð¸Ð³Ñ€Ð¾Ð²Ñ‹Ñ…
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ð¾Ð²/Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶ÐµÐ¹
    // Ð½Ð°
    // Ð½Ð°Ð»Ð¸Ñ‡Ð½Ñ‹Ðµ
    // Ð´ÐµÐ½ÑŒÐ³Ð¸
    // Ð¸Ð»Ð¸
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ñ‹
    // Ð¸Ð·
    // Ð´Ñ€ÑƒÐ³Ð¾Ð¹
    // Ð¸Ð³Ñ€Ñ‹.
    // Ð—Ð°
    // Ð´Ð¾Ð¿Ð¾Ð»Ð½Ð¸Ñ‚ÐµÐ»ÑŒÐ½Ð¾Ð¹
    // Ð¸Ð½Ñ„Ð¾Ñ€Ð¼Ð°Ñ†Ð¸ÐµÐ¹
    // Ð¾Ð±Ñ€Ð°Ñ‚Ð¸Ñ‚ÐµÑ�ÑŒ
    // Ð²
    // Ñ�Ð»ÑƒÐ¶Ð±Ñƒ
    // Ð¿Ð¾Ð´Ð´ÐµÑ€Ð¶ÐºÐ¸
    // Ð½Ð°
    // Ñ�Ð°Ð¹Ñ‚Ðµ.
    public static final int YOUR_ACCOUNT_HAS_BEEN_SUSPENDED_UNDER_CHAPTER_3_SECTION_14_OF_THE_LINEAGE_II_SERVICE_USE_1 = 434; // Ð’Ð°Ñˆ
    // Ð°ÐºÐºÐ°ÑƒÐ½Ñ‚
    // Ð±Ñ‹Ð»
    // Ð·Ð°Ð±Ð»Ð¾ÐºÐ¸Ñ€Ð¾Ð²Ð°Ð½
    // Ð²
    // Ñ�Ð¾Ð¾Ñ‚Ð²ÐµÑ‚Ñ�Ñ‚Ð²Ð¸Ð¸
    // Ñ�
    // ÐŸÐ¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÐµÐ»ÑŒÑ�ÐºÐ¸Ð¼
    // Ð¡Ð¾Ð³Ð»Ð°ÑˆÐµÐ½Ð¸ÐµÐ¼
    // Ð·Ð°
    // Ð½ÐµÐ¿Ñ€Ð¸Ñ�Ñ‚Ð¾Ð¹Ð½Ð¾Ðµ
    // Ð¿Ð¾Ð²ÐµÐ´ÐµÐ½Ð¸Ðµ
    // Ð¸Ð»Ð¸
    // Ð¼Ð¾ÑˆÐµÐ½Ð½Ð¸Ñ‡ÐµÑ�Ñ‚Ð²Ð¾.
    // Ð—Ð°
    // Ð´Ð¾Ð¿Ð¾Ð»Ð½Ð¸Ñ‚ÐµÐ»ÑŒÐ½Ð¾Ð¹
    // Ð¸Ð½Ñ„Ð¾Ñ€Ð¼Ð°Ñ†Ð¸ÐµÐ¹
    // Ð¾Ð±Ñ€Ð°Ñ‚Ð¸Ñ‚ÐµÑ�ÑŒ
    // Ð²
    // Ñ�Ð»ÑƒÐ¶Ð±Ñƒ
    // Ð¿Ð¾Ð´Ð´ÐµÑ€Ð¶ÐºÐ¸
    // Ð½Ð°
    // Ð½Ð°ÑˆÐµÐ¼
    // Ñ�Ð°Ð¹Ñ‚Ðµ.
    public static final int YOUR_ACCOUNT_HAS_BEEN_SUSPENDED_UNDER_CHAPTER_3_SECTION_14_OF_THE_LINEAGE_II_SERVICE_USE_2 = 435; // Ð’Ð°Ñˆ
    // Ð°ÐºÐºÐ°ÑƒÐ½Ñ‚
    // Ð±Ñ‹Ð»
    // Ð·Ð°Ð±Ð»Ð¾ÐºÐ¸Ñ€Ð¾Ð²Ð°Ð½
    // Ð²
    // Ñ�Ð¾Ð¾Ñ‚Ð²ÐµÑ‚Ñ�Ñ‚Ð²Ð¸Ð¸
    // Ñ�
    // ÐŸÐ¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÐµÐ»ÑŒÑ�ÐºÐ¸Ð¼
    // Ð¡Ð¾Ð³Ð»Ð°ÑˆÐµÐ½Ð¸ÐµÐ¼
    // Ð·Ð°
    // Ð½ÐµÐ¿Ñ€Ð¸Ñ�Ñ‚Ð¾Ð¹Ð½Ð¾Ðµ
    // Ð¿Ð¾Ð²ÐµÐ´ÐµÐ½Ð¸Ðµ.
    // Ð—Ð°
    // Ð´Ð¾Ð¿Ð¾Ð»Ð½Ð¸Ñ‚ÐµÐ»ÑŒÐ½Ð¾Ð¹
    // Ð¸Ð½Ñ„Ð¾Ñ€Ð¼Ð°Ñ†Ð¸ÐµÐ¹
    // Ð¾Ð±Ñ€Ð°Ñ‚Ð¸Ñ‚ÐµÑ�ÑŒ
    // Ð²
    // Ñ�Ð»ÑƒÐ¶Ð±Ñƒ
    // Ð¿Ð¾Ð´Ð´ÐµÑ€Ð¶ÐºÐ¸
    // Ð½Ð°
    // Ð½Ð°ÑˆÐµÐ¼
    // Ñ�Ð°Ð¹Ñ‚Ðµ.
    public static final int YOUR_ACCOUNT_HAS_BEEN_SUSPENDED_UNDER_CHAPTER_3_SECTION_14_OF_THE_LINEAGE_II_SERVICE_USE_3 = 436; // Ð’Ð°Ñˆ
    // Ð°ÐºÐºÐ°ÑƒÐ½Ñ‚
    // Ð±Ñ‹Ð»
    // Ð·Ð°Ð±Ð»Ð¾ÐºÐ¸Ñ€Ð¾Ð²Ð°Ð½
    // Ð²
    // Ñ�Ð¾Ð¾Ñ‚Ð²ÐµÑ‚Ñ�Ñ‚Ð²Ð¸Ð¸
    // ÐŸÐ¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÐµÐ»ÑŒÑ�ÐºÐ¸Ð¼
    // Ð¡Ð¾Ð³Ð»Ð°ÑˆÐµÐ½Ð¸ÐµÐ¼
    // Ð·Ð°
    // Ð·Ð»Ð¾ÑƒÐ¿Ð¾Ñ‚Ñ€ÐµÐ±Ð»ÐµÐ½Ð¸Ðµ
    // Ð¸Ð³Ñ€Ð¾Ð²Ð¾Ð¹
    // Ñ�Ð¸Ñ�Ñ‚ÐµÐ¼Ð¾Ð¹
    // Ð¸
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ð½Ð¸Ðµ
    // Ð¾ÑˆÐ¸Ð±Ð¾Ðº
    // Ð¸Ð³Ñ€Ñ‹
    // Ñ�
    // Ñ†ÐµÐ»ÑŒÑŽ
    // Ð¿Ð¾Ð»ÑƒÑ‡ÐµÐ½Ð¸Ñ�
    // Ð²Ñ‹Ð³Ð¾Ð´Ñ‹.
    // Ð˜Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ð½Ð¸Ðµ
    // Ð¾ÑˆÐ¸Ð±Ð¾Ðº
    // Ð½Ð°Ñ€ÑƒÑˆÐ°ÐµÑ‚
    // Ð¸Ð³Ñ€Ð¾Ð²Ð¾Ð¹
    // Ð±Ð°Ð»Ð°Ð½Ñ�.
    // Ð—Ð°
    // Ð´Ð¾Ð¿Ð¾Ð»Ð½Ð¸Ñ‚ÐµÐ»ÑŒÐ½Ð¾Ð¹
    // Ð¸Ð½Ñ„Ð¾Ñ€Ð¼Ð°Ñ†Ð¸ÐµÐ¹
    // Ð¾Ð±Ñ€Ð°Ñ‚Ð¸Ñ‚ÐµÑ�ÑŒ
    // Ð²
    // Ñ�Ð»ÑƒÐ¶Ð±Ñƒ
    // Ð¿Ð¾Ð´Ð´ÐµÑ€Ð¶ÐºÐ¸
    // Ð½Ð°
    // Ð½Ð°ÑˆÐµÐ¼
    // Ñ�Ð°Ð¹Ñ‚Ðµ.
    public static final int YOUR_ACCOUNT_HAS_BEEN_SUSPENDED_UNDER_CHAPTER_3_SECTION_14_OF_THE_LINEAGE_II_SERVICE_USE_4 = 437; // Ð’Ð°Ñˆ
    // Ð°ÐºÐºÐ°ÑƒÐ½Ñ‚
    // Ð±Ñ‹Ð»
    // Ð·Ð°Ð±Ð»Ð¾ÐºÐ¸Ñ€Ð¾Ð²Ð°Ð½
    // Ð²
    // Ñ�Ð¾Ð¾Ñ‚Ð²ÐµÑ‚Ñ�Ñ‚Ð²Ð¸Ð¸
    // Ñ�
    // ÐŸÐ¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÐµÐ»ÑŒÑ�ÐºÐ¸Ð¼
    // Ð¡Ð¾Ð³Ð»Ð°ÑˆÐµÐ½Ð¸ÐµÐ¼
    // Ð·Ð°
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ð½Ð¸Ðµ
    // Ð½ÐµÐ»ÐµÐ³Ð°Ð»ÑŒÐ½Ð¾Ð³Ð¾
    // Ð¿Ñ€Ð¾Ð³Ñ€Ð°Ð¼Ð¼Ð½Ð¾Ð³Ð¾
    // Ð¾Ð±ÐµÑ�Ð¿ÐµÑ‡ÐµÐ½Ð¸Ñ�.
    // Ð—Ð°
    // Ð´Ð¾Ð¿Ð¾Ð»Ð½Ð¸Ñ‚ÐµÐ»ÑŒÐ½Ð¾Ð¹
    // Ð¸Ð½Ñ„Ð¾Ñ€Ð¼Ð°Ñ†Ð¸ÐµÐ¹
    // Ð¾Ð±Ñ€Ð°Ñ‚Ð¸Ñ‚ÐµÑ�ÑŒ
    // Ð²
    // Ñ�Ð»ÑƒÐ¶Ð±Ñƒ
    // Ð¿Ð¾Ð´Ð´ÐµÑ€Ð¶ÐºÐ¸
    // Ð½Ð°
    // Ð½Ð°ÑˆÐµÐ¼
    // Ñ�Ð°Ð¹Ñ‚Ðµ.
    public static final int YOUR_ACCOUNT_HAS_BEEN_SUSPENDED_UNDER_CHAPTER_3_SECTION_14_OF_THE_LINEAGE_II_SERVICE_USE_5 = 438; // Ð’Ð°Ñˆ
    // Ð°ÐºÐºÐ°ÑƒÐ½Ñ‚
    // Ð±Ñ‹Ð»
    // Ð·Ð°Ð±Ð»Ð¾ÐºÐ¸Ñ€Ð¾Ð²Ð°Ð½
    // Ð²
    // Ñ�Ð¾Ð¾Ñ‚Ð²ÐµÑ‚Ñ�Ñ‚Ð²Ð¸Ð¸
    // Ñ�
    // ÐŸÐ¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÐµÐ»ÑŒÑ�ÐºÐ¸Ð¼
    // Ð¡Ð¾Ð³Ð»Ð°ÑˆÐµÐ½Ð¸ÐµÐ¼
    // Ð·Ð°
    // Ð¿Ð¾Ð¿Ñ‹Ñ‚ÐºÑƒ
    // Ð²Ñ‹Ð´Ð°Ñ‚ÑŒ
    // Ñ�ÐµÐ±Ñ�
    // Ð·Ð°
    // Ð¿Ñ€ÐµÐ´Ñ�Ñ‚Ð°Ð²Ð¸Ñ‚ÐµÐ»Ñ�
    // Ñ�Ð»ÑƒÐ¶Ð±Ñ‹
    // Ð¿Ð¾Ð´Ð´ÐµÑ€Ð¶ÐºÐ¸
    // Ð¸Ð»Ð¸
    // ÑˆÑ‚Ð°Ñ‚Ð½Ð¾Ð³Ð¾
    // Ñ�Ð¾Ñ‚Ñ€ÑƒÐ´Ð½Ð¸ÐºÐ°
    // ÐºÐ¾Ð¼Ð¿Ð°Ð½Ð¸Ð¸.
    // Ð—Ð°
    // Ð´Ð¾Ð¿Ð¾Ð»Ð½Ð¸Ñ‚ÐµÐ»ÑŒÐ½Ð¾Ð¹
    // Ð¸Ð½Ñ„Ð¾Ñ€Ð¼Ð°Ñ†Ð¸ÐµÐ¹
    // Ð¾Ð±Ñ€Ð°Ñ‚Ð¸Ñ‚ÐµÑ�ÑŒ
    // Ð²
    // Ñ�Ð»ÑƒÐ¶Ð±Ñƒ
    // Ð¿Ð¾Ð´Ð´ÐµÑ€Ð¶ÐºÐ¸
    // Ð½Ð°
    // Ð½Ð°ÑˆÐµÐ¼
    // Ñ�Ð°Ð¹Ñ‚Ðµ.
    public static final int IN_ACCORDANCE_WITH_THE_COMPANYS_USER_AGREEMENT_AND_OPERATIONAL_POLICY_THIS_ACCOUNT_HAS_BEEN = 439; // Ð’
    // Ñ�Ð¾Ð¾Ñ‚Ð²ÐµÑ‚Ñ�Ñ‚Ð²Ð¸Ð¸
    // Ñ�
    // ÐŸÐ¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÐµÐ»ÑŒÑ�ÐºÐ¸Ð¼
    // Ð¡Ð¾Ð³Ð»Ð°ÑˆÐµÐ½Ð¸ÐµÐ¼
    // Ð¸
    // Ð¿Ð¾Ð»Ð¸Ñ‚Ð¸ÐºÐ¾Ð¹
    // ÐºÐ¾Ð¼Ð¿Ð°Ð½Ð¸Ð¸
    // Ñ�Ñ‚Ð¾Ñ‚
    // Ð°ÐºÐºÐ°ÑƒÐ½Ñ‚
    // Ð·Ð°Ð±Ð»Ð¾ÐºÐ¸Ñ€Ð¾Ð²Ð°Ð½
    // Ð¿Ð¾
    // Ð¿Ñ€Ð¾Ñ�ÑŒÐ±Ðµ
    // Ð²Ð»Ð°Ð´ÐµÐ»ÑŒÑ†Ð°.
    // Ð•Ñ�Ð»Ð¸
    // Ñƒ
    // Ð’Ð°Ñ�
    // ÐµÑ�Ñ‚ÑŒ
    // Ð²Ð¾Ð¿Ñ€Ð¾Ñ�Ñ‹
    // Ð¿Ð¾
    // Ð¿Ð¾Ð²Ð¾Ð´Ñƒ
    // Ð°ÐºÐºÐ°ÑƒÐ½Ñ‚Ð°,
    // Ð¾Ð±Ñ€Ð°Ñ‚Ð¸Ñ‚ÐµÑ�ÑŒ
    // Ð²
    // Ñ�Ð»ÑƒÐ¶Ð±Ñƒ
    // Ð¿Ð¾Ð´Ð´ÐµÑ€Ð¶ÐºÐ¸
    // Ð½Ð°
    // Ð½Ð°ÑˆÐµÐ¼
    // Ñ�Ð°Ð¹Ñ‚Ðµ.
    public static final int BECAUSE_YOU_ARE_REGISTERED_AS_A_MINOR_YOUR_ACCOUNT_HAS_BEEN_SUSPENDED_AT_THE_REQUEST_OF_YOUR = 440; // Ð’Ñ‹
    // Ð·Ð°Ñ€ÐµÐ³Ð¸Ñ�Ñ‚Ñ€Ð¸Ñ€Ð¾Ð²Ð°Ð½Ñ‹
    // ÐºÐ°Ðº
    // Ð½ÐµÑ�Ð¾Ð²ÐµÑ€ÑˆÐµÐ½Ð½Ð¾Ð»ÐµÑ‚Ð½Ð¸Ð¹,
    // Ð¸
    // Ð’Ð°Ñˆ
    // Ð°ÐºÐºÐ°ÑƒÐ½Ñ‚
    // Ð±Ñ‹Ð»
    // Ð·Ð°Ð±Ð»Ð¾ÐºÐ¸Ñ€Ð¾Ð²Ð°Ð½
    // Ð¿Ð¾
    // Ð¿Ñ€Ð¾Ñ�ÑŒÐ±Ðµ
    // Ð²Ð°ÑˆÐ¸Ñ…
    // Ñ€Ð¾Ð´Ð¸Ñ‚ÐµÐ»ÐµÐ¹
    // Ð¸Ð»Ð¸
    // Ð¾Ð¿ÐµÐºÑƒÐ½Ð¾Ð².
    // Ð—Ð°
    // Ð´Ð¾Ð¿Ð¾Ð»Ð½Ð¸Ñ‚ÐµÐ»ÑŒÐ½Ð¾Ð¹
    // Ð¸Ð½Ñ„Ð¾Ñ€Ð¼Ð°Ñ†Ð¸ÐµÐ¹
    // Ð¾Ð±Ñ€Ð°Ñ‚Ð¸Ñ‚ÐµÑ�ÑŒ
    // Ð²
    // Ñ�Ð»ÑƒÐ¶Ð±Ñƒ
    // Ð¿Ð¾Ð´Ð´ÐµÑ€Ð¶ÐºÐ¸
    // Ð½Ð°
    // Ð½Ð°ÑˆÐµÐ¼
    // Ñ�Ð°Ð¹Ñ‚Ðµ.
    public static final int PER_OUR_COMPANYS_USER_AGREEMENT_THE_USE_OF_THIS_ACCOUNT_HAS_BEEN_SUSPENDED_IF_YOU_HAVE_ANY = 441; // Ð’
    // Ñ�Ð¾Ð¾Ñ‚Ð²ÐµÑ‚Ñ�Ñ‚Ð²Ð¸Ð¸
    // Ñ�
    // ÐŸÐ¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÐµÐ»ÑŒÑ�ÐºÐ¸Ð¼
    // Ð¡Ð¾Ð³Ð»Ð°ÑˆÐµÐ½Ð¸ÐµÐ¼
    // Ð´Ð°Ð½Ð½Ñ‹Ð¹
    // Ð°ÐºÐºÐ°ÑƒÐ½Ñ‚
    // Ð±Ñ‹Ð»
    // Ð·Ð°Ð±Ð»Ð¾ÐºÐ¸Ñ€Ð¾Ð²Ð°Ð½.
    // Ð•Ñ�Ð»Ð¸
    // Ñƒ
    // Ð’Ð°Ñ�
    // ÐµÑ�Ñ‚ÑŒ
    // Ð²Ð¾Ð¿Ñ€Ð¾Ñ�Ñ‹
    // ÐºÐ°Ñ�Ð°Ñ‚ÐµÐ»ÑŒÐ½Ð¾
    // Ñ�Ñ‚Ð¾Ð³Ð¾
    // Ð°ÐºÐºÐ°ÑƒÐ½Ñ‚Ð°,
    // Ð¾Ð±Ñ€Ð°Ñ‚Ð¸Ñ‚ÐµÑ�ÑŒ
    // Ð²
    // Ñ�Ð»ÑƒÐ¶Ð±Ñƒ
    // Ð¿Ð¾Ð´Ð´ÐµÑ€Ð¶ÐºÐ¸
    // Ð½Ð°
    // Ð½Ð°ÑˆÐµÐ¼
    // Ñ�Ð°Ð¹Ñ‚Ðµ.
    public static final int YOUR_ACCOUNT_HAS_BEEN_SUSPENDED_UNDER_CHAPTER_2_SECTION_7_OF_THE_LINEAGE_II_SERVICE_USE = 442; // Ð’Ð°Ñˆ
    // Ð°ÐºÐºÐ°ÑƒÐ½Ñ‚
    // Ð±Ñ‹Ð»
    // Ð·Ð°Ð±Ð»Ð¾ÐºÐ¸Ñ€Ð¾Ð²Ð°Ð½
    // Ð²
    // Ñ�Ð¾Ð¾Ñ‚Ð²ÐµÑ‚Ñ�Ñ‚Ð²Ð¸Ð¸
    // Ñ�
    // ÐŸÐ¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÐµÐ»ÑŒÑ�ÐºÐ¸Ð¼
    // Ð¡Ð¾Ð³Ð»Ð°ÑˆÐµÐ½Ð¸ÐµÐ¼
    // Ð·Ð°
    // Ð¿Ñ€Ð¸Ñ�Ð²Ð¾ÐµÐ½Ð¸Ðµ
    // Ð¿Ð»Ð°Ñ‚Ñ‹
    // Ð´Ñ€ÑƒÐ³Ð¾Ð³Ð¾
    // Ð°ÐºÐºÐ°ÑƒÐ½Ñ‚Ð°.
    // Ð—Ð°
    // Ð´Ð¾Ð¿Ð¾Ð»Ð½Ð¸Ñ‚ÐµÐ»ÑŒÐ½Ð¾Ð¹
    // Ð¸Ð½Ñ„Ð¾Ñ€Ð¼Ð°Ñ†Ð¸ÐµÐ¹
    // Ð¾Ð±Ñ€Ð°Ñ‚Ð¸Ñ‚ÐµÑ�ÑŒ
    // Ð²
    // Ñ�Ð»ÑƒÐ¶Ð±Ñƒ
    // Ð¿Ð¾Ð´Ð´ÐµÑ€Ð¶ÐºÐ¸
    // Ð½Ð°
    // Ð½Ð°ÑˆÐµÐ¼
    // Ñ�Ð°Ð¹Ñ‚Ðµ.
    public static final int THE_IDENTITY_OF_THIS_ACCOUNT_HAS_NOT_BEEN_VEEN_VERIFIED_THEREFORE_LINEAGE_II_SERVICE_FOR_THIS = 443; // ÐŸÐ¾Ð´Ð»Ð¸Ð½Ð½Ð¾Ñ�Ñ‚ÑŒ
    // Ð’Ð°ÑˆÐµÐ³Ð¾
    // Ð°ÐºÐºÐ°ÑƒÐ½Ñ‚Ð°
    // Ð½Ðµ
    // Ð±Ñ‹Ð»Ð°
    // Ð¿Ð¾Ð´Ñ‚Ð²ÐµÑ€Ð¶Ð´ÐµÐ½Ð°,
    // Ð¿Ð¾Ñ‚Ð¾Ð¼Ñƒ
    // ÐµÐ³Ð¾
    // Ð´ÐµÐ¹Ñ�Ñ‚Ð²Ð¸Ðµ
    // Ñ�ÐµÐ¹Ñ‡Ð°Ñ�
    // Ð¿Ñ€Ð¸Ð¾Ñ�Ñ‚Ð°Ð½Ð¾Ð²Ð»ÐµÐ½Ð¾.
    // Ð”Ð»Ñ�
    // Ð¿Ð¾Ð´Ñ‚Ð²ÐµÑ€Ð¶Ð´ÐµÐ½Ð¸Ñ�
    // Ð¿Ð¾Ð´Ð»Ð¸Ð½Ð½Ð¾Ñ�Ñ‚Ð¸,
    // Ð¿Ð¾Ð¶Ð°Ð»ÑƒÐ¹Ñ�Ñ‚Ð°,
    // Ð²Ñ‹ÑˆÐ»Ð¸Ñ‚Ðµ
    // Ð½Ð°Ð¼
    // Ð´Ð°Ð½Ð½Ñ‹Ðµ
    // Ð°ÐºÐºÐ°ÑƒÐ½Ñ‚Ð°,
    // Ð’Ð°ÑˆÐ¸
    // Ð»Ð¸Ñ‡Ð½Ñ‹Ðµ
    // Ð´Ð°Ð½Ð½Ñ‹Ðµ,
    // ÐºÐ¾Ð¿Ð¸ÑŽ
    // Ð´Ð¾ÐºÑƒÐ¼ÐµÐ½Ñ‚Ð°,
    // ÑƒÐ´Ð¾Ñ�Ñ‚Ð¾Ð²ÐµÑ€Ñ�ÑŽÑ‰ÐµÐ³Ð¾
    // Ð’Ð°ÑˆÑƒ
    // Ð»Ð¸Ñ‡Ð½Ð¾Ñ�Ñ‚ÑŒ,
    // Ð¸
    // ÐºÐ¾Ð½Ñ‚Ð°ÐºÑ‚Ð½ÑƒÑŽ
    // Ð¸Ð½Ñ„Ð¾Ñ€Ð¼Ð°Ñ†Ð¸ÑŽ.
    // Ð—Ð°
    // Ð´Ð¾Ð¿Ð¾Ð»Ð½Ð¸Ñ‚ÐµÐ»ÑŒÐ½Ñ‹Ð¼Ð¸
    // Ñ�Ð²ÐµÐ´ÐµÐ½Ð¸Ñ�Ð¼Ð¸
    // Ð¾Ð±Ñ€Ð°Ñ‚Ð¸Ñ‚ÐµÑ�ÑŒ
    // Ð²
    // Ñ�Ð»ÑƒÐ¶Ð±Ñƒ
    // Ð¿Ð¾Ð´Ð´ÐµÑ€Ð¶ÐºÐ¸
    // Ð½Ð°
    // Ð½Ð°ÑˆÐµÐ¼
    // Ñ�Ð°Ð¹Ñ‚Ðµ.
    public static final int SINCE_WE_HAVE_RECEIVED_A_WITHDRAWAL_REQUEST_FROM_THE_HOLDER_OF_THIS_ACCOUNT_ACCESS_TO_ALL = 444; // ÐŸÐ¾Ð»ÑƒÑ‡ÐµÐ½
    // Ð·Ð°Ð¿Ñ€Ð¾Ñ�
    // Ð½Ð°
    // Ð±Ð»Ð¾ÐºÐ¸Ñ€Ð¾Ð²ÐºÑƒ
    // Ð°ÐºÐºÐ°ÑƒÐ½Ñ‚Ð°
    // Ð¾Ñ‚
    // ÐµÐ³Ð¾
    // Ð²Ð»Ð°Ð´ÐµÐ»ÑŒÑ†Ð°,
    // Ð°ÐºÐºÐ°ÑƒÐ½Ñ‚
    // Ð°Ð²Ñ‚Ð¾Ð¼Ð°Ñ‚Ð¸Ñ‡ÐµÑ�ÐºÐ¸
    // Ð±Ð»Ð¾ÐºÐ¸Ñ€ÑƒÐµÑ‚Ñ�Ñ�.
    public static final int REFERENCE_NUMBER_REGARDING_MEMBERSHIP_WITHDRAWAL_REQUEST__S1 = 445; // (ÐŸÐ¾Ð´Ð°Ñ‚ÑŒ
    // Ð·Ð°Ñ�Ð²ÐºÑƒ
    // Ð½Ð°
    // Ñ‡Ð»ÐµÐ½Ñ�Ñ‚Ð²Ð¾?
    // Ð�Ð¾Ð¼ÐµÑ€
    // ÐºÐ²Ð¸Ñ‚Ð°Ð½Ñ†Ð¸Ð¸:
    // $s1)
    public static final int FOR_MORE_INFORMATION_PLEASE_VISIT_THE_SUPPORT_CENTER_ON_THE_PLAYNC_WEBSITE_HTTP___WWWPLAYNCCOM = 446; // Ð”Ð¾Ð¿Ð¾Ð»Ð½Ð¸Ñ‚ÐµÐ»ÑŒÐ½ÑƒÑŽ
    // Ð¸Ð½Ñ„Ð¾Ñ€Ð¼Ð°Ñ†Ð¸ÑŽ
    // Ð¼Ð¾Ð¶Ð½Ð¾
    // Ð¿Ð¾Ð»ÑƒÑ‡Ð¸Ñ‚ÑŒ
    // Ð²
    // Ñ�Ð»ÑƒÐ¶Ð±Ðµ
    // Ð¿Ð¾Ð´Ð´ÐµÑ€Ð¶ÐºÐ¸
    // Ð½Ð°
    // Ð½Ð°ÑˆÐµÐ¼
    // Ñ�Ð°Ð¹Ñ‚Ðµ.
    public static final int SYSMSG_ID447 = 447; // .
    public static final int SYSTEM_ERROR_PLEASE_LOG_IN_AGAIN_LATER = 448; // ÐžÑˆÐ¸Ð±ÐºÐ°
    // Ñ�Ð¸Ñ�Ñ‚ÐµÐ¼Ñ‹.
    // ÐŸÐ¾Ð¶Ð°Ð»ÑƒÐ¹Ñ�Ñ‚Ð°,
    // Ð·Ð°Ð¹Ð´Ð¸Ñ‚Ðµ
    // Ð²
    // Ð¸Ð³Ñ€Ñƒ
    // Ð¿Ð¾Ð·Ð¶Ðµ.
    public static final int PASSWORD_DOES_NOT_MATCH_THIS_ACCOUNT = 449; // Ð’Ñ‹
    // Ð²Ð²ÐµÐ»Ð¸
    // Ð½ÐµÐ²ÐµÑ€Ð½Ñ‹Ð¹
    // Ð»Ð¾Ð³Ð¸Ð½
    // Ð¸Ð»Ð¸
    // Ð¿Ð°Ñ€Ð¾Ð»ÑŒ.
    public static final int CONFIRM_YOUR_ACCOUNT_INFORMATION_AND_LOG_IN_AGAIN_LATER = 450; // ÐŸÐ¾Ð´Ñ‚Ð²ÐµÑ€Ð´Ð¸Ñ‚Ðµ
    // Ð¸Ð½Ñ„Ð¾Ñ€Ð¼Ð°Ñ†Ð¸ÑŽ
    // Ð¸
    // Ð·Ð°Ð¹Ð´Ð¸Ñ‚Ðµ
    // Ð²
    // Ð¸Ð³Ñ€Ñƒ.
    public static final int THE_PASSWORD_YOU_HAVE_ENTERED_IS_INCORRECT = 451; // Ð’Ñ‹
    // Ð²Ð²ÐµÐ»Ð¸
    // Ð½ÐµÐ²ÐµÑ€Ð½Ñ‹Ð¹
    // Ð»Ð¾Ð³Ð¸Ð½
    // Ð¸Ð»Ð¸
    // Ð¿Ð°Ñ€Ð¾Ð»ÑŒ.
    public static final int PLEASE_CONFIRM_YOUR_ACCOUNT_INFORMATION_AND_TRY_LOGGING_IN_AGAIN = 452; // ÐŸÐ¾Ð´Ñ‚Ð²ÐµÑ€Ð´Ð¸Ñ‚Ðµ
    // Ð¸Ð½Ñ„Ð¾Ñ€Ð¼Ð°Ñ†Ð¸ÑŽ
    // Ð¸
    // Ð·Ð°Ð¹Ð´Ð¸Ñ‚Ðµ
    // Ð²
    // Ð¸Ð³Ñ€Ñƒ.
    public static final int YOUR_ACCOUNT_INFORMATION_IS_INCORRECT = 453; // Ð�ÐµÐ²ÐµÑ€Ð½Ð°Ñ�
    // Ð¸Ð½Ñ„Ð¾Ñ€Ð¼Ð°Ñ†Ð¸Ñ�.
    public static final int FOR_MORE_DETAILS_PLEASE_CONTACT_OUR_CUSTOMER_SERVICE_CENTER_AT_HTTP__SUPPORTPLAYNCCOM = 454; // Ð”Ð»Ñ�
    // Ð¿Ð¾Ð»ÑƒÑ‡ÐµÐ½Ð¸Ñ�
    // Ð´Ð¾Ð¿Ð¾Ð»Ð½Ð¸Ñ‚ÐµÐ»ÑŒÐ½Ð¾Ð¹
    // Ð¸Ð½Ñ„Ð¾Ñ€Ð¼Ð°Ñ†Ð¸Ð¸
    // Ð¾Ð±Ñ€Ð°Ñ‚Ð¸Ñ‚ÐµÑ�ÑŒ
    // Ð²
    // Ñ�Ð»ÑƒÐ¶Ð±Ñƒ
    // Ð¿Ð¾Ð´Ð´ÐµÑ€Ð¶ÐºÐ¸
    // Ð½Ð°
    // Ð½Ð°ÑˆÐµÐ¼
    // Ñ�Ð°Ð¹Ñ‚Ðµ.
    public static final int THE_ACCOUNT_IS_ALREADY_IN_USE_ACCESS_DENIED = 455; // Ð¢Ð°ÐºÐ¾Ð¹
    // Ð°ÐºÐºÐ°ÑƒÐ½Ñ‚
    // ÑƒÐ¶Ðµ
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·ÑƒÐµÑ‚Ñ�Ñ�.
    // Ð’Ð¾Ð¹Ñ‚Ð¸
    // Ð½ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾.
    public static final int LINEAGE_II_GAME_SERVICES_MAY_BE_USED_BY_INDIVIDUALS_15_YEARS_OF_AGE_OR_OLDER_EXCEPT_FOR_PVP_SERVERS = 456; // Ð’
    // Lineage
    // II
    // Ð¼Ð¾Ð³ÑƒÑ‚
    // Ð¸Ð³Ñ€Ð°Ñ‚ÑŒ
    // Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÐµÐ»Ð¸,
    // ÐºÐ¾Ñ‚Ð¾Ñ€Ñ‹Ð¼
    // Ð½Ðµ
    // Ð¼ÐµÐ½ÐµÐµ
    // 15
    // Ð»ÐµÑ‚,
    // Ð½Ð°
    // PvP
    // Ñ�ÐµÑ€Ð²ÐµÑ€Ð°Ñ…
    // â€“
    // Ð½Ðµ
    // Ð¼ÐµÐ½ÐµÐµ
    // 18
    // Ð»ÐµÑ‚.
    public static final int SERVER_UNDER_MAINTENANCE_PLEASE_TRY_AGAIN_LATER = 457; // Ð’
    // Ð´Ð°Ð½Ð½Ñ‹Ð¹
    // Ð¼Ð¾Ð¼ÐµÐ½Ñ‚
    // Ð½Ð°
    // Ñ�ÐµÑ€Ð²ÐµÑ€Ðµ
    // Ð¿Ñ€Ð¾Ñ…Ð¾Ð´Ñ�Ñ‚
    // Ð¿Ñ€Ð¾Ñ„Ð¸Ð»Ð°ÐºÑ‚Ð¸Ñ‡ÐµÑ�ÐºÐ¸Ðµ
    // Ñ€Ð°Ð±Ð¾Ñ‚Ñ‹.
    // ÐŸÐ¾Ð¿Ñ€Ð¾Ð±ÑƒÐ¹Ñ‚Ðµ
    // Ð·Ð°Ð¹Ñ‚Ð¸
    // Ð¿Ð¾Ð·Ð¶Ðµ.
    public static final int YOUR_USAGE_TERM_HAS_EXPIRED = 458; // Ð’Ñ€ÐµÐ¼Ñ� Ð¸Ð³Ñ€Ñ‹
    // Ð·Ð°ÐºÐ¾Ð½Ñ‡Ð¸Ð»Ð¾Ñ�ÑŒ.
    public static final int PLEASE_VISIT_THE_OFFICIAL_LINEAGE_II_WEBSITE_AT_HTTP__WWWLINEAGE2COM = 459; // Ð§Ñ‚Ð¾Ð±Ñ‹
    // Ð¿Ñ€Ð¾Ð´Ð¾Ð»Ð¶Ð¸Ñ‚ÑŒ
    // Ð¸Ð³Ñ€Ñƒ,
    // Ð¿Ñ€Ð¸Ð¾Ð±Ñ€ÐµÑ‚Ð¸Ñ‚Ðµ
    // LineageII
    public static final int TO_REACTIVATE_YOUR_ACCOUNT = 460; // Ð² Ð½Ð°ÑˆÐµÐ¼ Ñ�Ð°Ð¹Ñ‚Ðµ
    // Ð¸Ð»Ð¸ Ð² Ð¼Ð°Ð³Ð°Ð·Ð¸Ð½Ðµ.
    public static final int ACCESS_FAILED = 461; // Ð�Ðµ ÑƒÐ´Ð°Ð»Ð¾Ñ�ÑŒ Ñ�Ð¾ÐµÐ´Ð¸Ð½Ð¸Ñ‚ÑŒÑ�Ñ� Ñ�
    // Ñ�ÐµÑ€Ð²ÐµÑ€Ð¾Ð¼.
    @Deprecated
    public static final int PLEASE_TRY_AGAIN_LATER_1 = 462; // ÐŸÐ¾Ð¶Ð°Ð»ÑƒÐ¹Ñ�Ñ‚Ð°,
    // Ð¿Ð¾Ð²Ñ‚Ð¾Ñ€Ð¸Ñ‚Ðµ Ð¿Ð¾Ð¿Ñ‹Ñ‚ÐºÑƒ
    // Ð¿Ð¾Ð·Ð¶Ðµ.
    public static final int SYSMSG_ID463 = 463; // .
    public static final int FEATURE_AVAILABLE_TO_ALLIANCE_LEADERS_ONLY = 464; // Ð­Ñ‚Ð¾
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð´ÐµÐ»Ð°Ñ‚ÑŒ
    // Ñ‚Ð¾Ð»ÑŒÐºÐ¾
    // Ð³Ð»Ð°Ð²Ð°
    // Ð°Ð»ÑŒÑ�Ð½Ñ�Ð°.
    public static final int YOU_ARE_NOT_CURRENTLY_ALLIED_WITH_ANY_CLANS = 465; // Ð’Ñ‹
    // Ð½Ðµ
    // Ñ�Ð¾Ñ�Ñ‚Ð¾Ð¸Ñ‚Ðµ
    // Ð²
    // Ð°Ð»ÑŒÑ�Ð½Ñ�Ðµ.
    public static final int YOU_HAVE_EXCEEDED_THE_LIMIT = 466; // Ð�ÐµÐ»ÑŒÐ·Ñ� Ð¿Ñ€Ð¸Ð½Ñ�Ñ‚ÑŒ
    // Ð½Ð¾Ð²Ñ‹Ð¹ ÐºÐ»Ð°Ð½.
    public static final int YOU_MAY_NOT_ACCEPT_ANY_CLAN_WITHIN_A_DAY_AFTER_EXPELLING_ANOTHER_CLAN = 467; // ÐŸÐ¾Ñ�Ð»Ðµ
    // Ð¸Ñ�ÐºÐ»ÑŽÑ‡ÐµÐ½Ð¸Ñ�
    // ÐºÐ»Ð°Ð½Ð°
    // Ð¸Ð·
    // Ð°Ð»ÑŒÑ�Ð½Ñ�Ð°
    // Ð’Ñ‹
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¿Ñ€Ð¸Ð½Ñ�Ñ‚ÑŒ
    // Ð½Ð¾Ð²Ñ‹Ð¹
    // ÐºÐ»Ð°Ð½
    // Ñ‚Ð¾Ð»ÑŒÐºÐ¾
    // Ñ‡ÐµÑ€ÐµÐ·
    // 24
    // Ñ‡Ð°Ñ�Ð°.
    public static final int A_CLAN_THAT_HAS_WITHDRAWN_OR_BEEN_EXPELLED_CANNOT_ENTER_INTO_AN_ALLIANCE_WITHIN_ONE_DAY_OF_WITHDRAWAL_OR_EXPULSION = 468; // Ð˜Ñ�ÐºÐ»ÑŽÑ‡ÐµÐ½Ð½Ñ‹Ð¹
    // Ð¸Ð·
    // Ð°Ð»ÑŒÑ�Ð½Ñ�Ð°
    // Ð¸Ð»Ð¸
    // Ð¿Ð¾ÐºÐ¸Ð½ÑƒÐ²ÑˆÐ¸Ð¹
    // ÐµÐ³Ð¾
    // ÐºÐ»Ð°Ð½
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð²Ñ�Ñ‚ÑƒÐ¿Ð¸Ñ‚ÑŒ
    // Ð²
    // Ð½Ð¾Ð²Ñ‹Ð¹
    // Ð²
    // Ñ‚ÐµÑ‡ÐµÐ½Ð¸Ðµ
    // 24
    // Ñ‡.
    public static final int YOU_MAY_NOT_ALLY_WITH_A_CLAN_YOU_ARE_AT_BATTLE_WITH = 469; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ñ�Ð¾Ð·Ð´Ð°Ñ‚ÑŒ
    // Ð°Ð»ÑŒÑ�Ð½Ñ�
    // Ñ�
    // ÐºÐ»Ð°Ð½Ð¾Ð¼,
    // Ñ�
    // ÐºÐ¾Ñ‚Ð¾Ñ€Ñ‹Ð¼
    // Ð’Ñ‹
    // Ð²ÐµÐ´ÐµÑ‚Ðµ
    // Ð²Ð¾Ð¹Ð½Ñƒ.
    public static final int ONLY_THE_CLAN_LEADER_MAY_APPLY_FOR_WITHDRAWAL_FROM_THE_ALLIANCE = 470; // Ð¢Ð¾Ð»ÑŒÐºÐ¾
    // Ð³Ð»Ð°Ð²Ð°
    // ÐºÐ»Ð°Ð½Ð°
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð²Ñ‹Ð²ÐµÑ�Ñ‚Ð¸
    // ÐºÐ»Ð°Ð½
    // Ð¸Ð·
    // Ð°Ð»ÑŒÑ�Ð½Ñ�Ð°.
    public static final int ALLIANCE_LEADERS_CANNOT_WITHDRAW = 471; // Ð“Ð»Ð°Ð²Ð°
    // Ð°Ð»ÑŒÑ�Ð½Ñ�Ð°
    // Ð½Ðµ Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð¿Ð¾ÐºÐ¸Ð½ÑƒÑ‚ÑŒ
    // Ð°Ð»ÑŒÑ�Ð½Ñ�.
    public static final int YOU_CANNOT_EXPEL_YOURSELF_FROM_THE_CLAN = 472; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¸Ñ�ÐºÐ»ÑŽÑ‡Ð¸Ñ‚ÑŒ
    // Ñ�ÐµÐ±Ñ�
    // Ð¸Ð·
    // ÐºÐ»Ð°Ð½Ð°.
    public static final int DIFFERENT_ALLIANCE = 473; // ÐšÐ»Ð°Ð½ Ñ�Ð¾Ñ�Ñ‚Ð¾Ð¸Ñ‚ Ð² Ð´Ñ€ÑƒÐ³Ð¾Ð¼
    // Ð°Ð»ÑŒÑ�Ð½Ñ�Ðµ.
    public static final int THE_FOLLOWING_CLAN_DOES_NOT_EXIST = 474; // ÐšÐ»Ð°Ð½Ð° Ð½Ðµ
    // Ñ�ÑƒÑ‰ÐµÑ�Ñ‚Ð²ÑƒÐµÑ‚.
    @Deprecated
    public static final int DIFFERENT_ALLIANCE_1 = 475; // ÐšÐ»Ð°Ð½ Ñ�Ð¾Ñ�Ñ‚Ð¾Ð¸Ñ‚ Ð² Ð´Ñ€ÑƒÐ³Ð¾Ð¼
    // Ð°Ð»ÑŒÑ�Ð½Ñ�Ðµ.
    public static final int INCORRECT_IMAGE_SIZE_PLEASE_ADJUST_TO_8X12 = 476; // Ð�ÐµÐ²ÐµÑ€Ð½Ñ‹Ð¹
    // Ñ€Ð°Ð·Ð¼ÐµÑ€
    // Ñ€Ð¸Ñ�ÑƒÐ½ÐºÐ°.
    // ÐŸÐ¾Ð¶Ð°Ð»ÑƒÐ¹Ñ�Ñ‚Ð°,
    // Ð¿Ð¾Ð¼ÐµÐ½Ñ�Ð¹Ñ‚Ðµ
    // Ñ€Ð°Ð·Ð¼ÐµÑ€
    // Ð½Ð°
    // 8*12.
    public static final int NO_RESPONSE_INVITATION_TO_JOIN_AN_ALLIANCE_HAS_BEEN_CANCELLED = 477; // Ð�ÐµÑ‚
    // Ð¾Ñ‚Ð²ÐµÑ‚Ð°.
    // ÐŸÑ€Ð¸Ð³Ð»Ð°ÑˆÐµÐ½Ð¸Ðµ
    // Ð½Ð°
    // Ð²Ñ�Ñ‚ÑƒÐ¿Ð»ÐµÐ½Ð¸Ðµ
    // Ð²
    // Ð°Ð»ÑŒÑ�Ð½Ñ�
    // Ð¾Ñ‚Ð¼ÐµÐ½ÐµÐ½Ð¾.
    public static final int NO_RESPONSE_YOUR_ENTRANCE_TO_THE_ALLIANCE_HAS_BEEN_CANCELLED = 478; // Ð�ÐµÑ‚
    // Ð¾Ñ‚Ð²ÐµÑ‚Ð°.
    // Ð’Ð°ÑˆÐµ
    // Ð²Ñ�Ñ‚ÑƒÐ¿Ð»ÐµÐ½Ð¸Ðµ
    // Ð²
    // Ð°Ð»ÑŒÑ�Ð½Ñ�
    // Ð¾Ñ‚Ð¼ÐµÐ½ÐµÐ½Ð¾.
    public static final int S1_HAS_JOINED_AS_A_FRIEND = 479; // $s1 Ð´Ð¾Ð±Ð°Ð²Ð»Ñ�ÐµÑ‚Ñ�Ñ�
    // Ð² Ñ�Ð¿Ð¸Ñ�Ð¾Ðº Ð´Ñ€ÑƒÐ·ÐµÐ¹.
    public static final int PLEASE_CHECK_YOUR_FRIENDS_LIST = 480; // ÐŸÑ€Ð¾Ð²ÐµÑ€ÑŒÑ‚Ðµ
    // Ñ�Ð¿Ð¸Ñ�Ð¾Ðº
    // Ð´Ñ€ÑƒÐ·ÐµÐ¹.
    public static final int S1__HAS_BEEN_DELETED_FROM_YOUR_FRIENDS_LIST = 481; // $s1
    // ÑƒÐ´Ð°Ð»Ñ�ÐµÑ‚Ñ�Ñ�
    // Ð¸Ð·
    // Ñ�Ð¿Ð¸Ñ�ÐºÐ°
    // Ð´Ñ€ÑƒÐ·ÐµÐ¹.
    @Deprecated
    public static final int YOU_CANNOT_ADD_YOURSELF_TO_YOUR_OWN_FRIEND_LIST_1 = 482; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ð´Ð¾Ð±Ð°Ð²Ð¸Ñ‚ÑŒ
    // Ñ�ÐµÐ±Ñ�
    // Ð²
    // Ñ�Ð¿Ð¸Ñ�Ð¾Ðº
    // Ð´Ñ€ÑƒÐ·ÐµÐ¹.
    public static final int FRIEND_LIST_IS_NOT_READY_YET_PLEASE_TRY_AGAIN_LATER = 483; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ñ�Ð¾Ð·Ð´Ð°Ñ‚ÑŒ
    // Ñ�Ð¿Ð¸Ñ�Ð¾Ðº
    // Ð´Ñ€ÑƒÐ·ÐµÐ¹.
    // ÐŸÐ¾Ð¿Ñ€Ð¾Ð±ÑƒÐ¹Ñ‚Ðµ
    // Ð¿Ð¾Ð·Ð¶Ðµ.
    public static final int ALREADY_REGISTERED_ON_THE_FRIENDS_LIST = 484; // Ð£Ð¶Ðµ
    // Ð²
    // Ñ�Ð¿Ð¸Ñ�ÐºÐµ
    // Ð´Ñ€ÑƒÐ·ÐµÐ¹.
    public static final int NO_NEW_FRIEND_INVITATIONS_FROM_OTHER_USERS = 485; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ð¿Ñ€Ð¸Ð½Ñ�Ñ‚ÑŒ
    // Ð½Ð¾Ð²Ñ‹Ñ…
    // Ð´Ñ€ÑƒÐ·ÐµÐ¹.
    public static final int THE_FOLLOWING_USER_IS_NOT_IN_YOUR_FRIENDS_LIST = 486; // Ð�ÐµÑ‚
    // Ð²
    // Ñ�Ð¿Ð¸Ñ�ÐºÐµ
    // Ð’Ð°ÑˆÐ¸Ñ…
    // Ð´Ñ€ÑƒÐ·ÐµÐ¹.
    public static final int _FRIENDS_LIST_ = 487; // ======<FRIENDS_LIST>======
    public static final int S1_CURRENTLY_ONLINE = 488; // $s1 (Ð’ Ñ�ÐµÑ‚Ð¸)
    public static final int S1_CURRENTLY_OFFLINE = 489; // $s1 (Ð�Ðµ Ð² Ñ�ÐµÑ‚Ð¸)
    public static final int __EQUALS__ = 490; // ========================
    public static final int _ALLIANCE_INFORMATION_ = 491; // =======<ALLIANCE_INFORMATION>=======
    public static final int ALLIANCE_NAME_S1 = 492; // Ð�Ð°Ð·Ð²Ð°Ð½Ð¸Ðµ Ð°Ð»ÑŒÑ�Ð½Ñ�Ð° : $s1
    public static final int CONNECTION_S1_TOTAL_S2 = 493; // Ð’ Ñ�ÐµÑ‚Ð¸: $s1/Ð’Ñ�ÐµÐ³Ð¾:
    // $s2
    public static final int ALLIANCE_LEADER_S2_OF_S1 = 494; // Ð“Ð»Ð°Ð²Ð° Ð°Ð»ÑŒÑ�Ð½Ñ�Ð°:
    // $s2, ÐºÐ»Ð°Ð½ $s1
    public static final int AFFILIATED_CLANS_TOTAL_S1_CLAN_S = 495; // ÐšÐ»Ð°Ð½Ð¾Ð² Ð²
    // Ð°Ð»ÑŒÑ�Ð½Ñ�Ðµ:
    // $s1
    public static final int _CLAN_INFORMATION_ = 496; // =====<CLAN_INFORMATION>=====
    public static final int CLAN_NAME_S1 = 497; // Ð�Ð°Ð·Ð²Ð°Ð½Ð¸Ðµ ÐºÐ»Ð°Ð½Ð°: $s1
    public static final int CLAN_LEADER_S1 = 498; // Ð“Ð»Ð°Ð²Ð° ÐºÐ»Ð°Ð½Ð°: $s1
    public static final int CLAN_LEVEL_S1 = 499; // Ð£Ñ€Ð¾Ð²ÐµÐ½ÑŒ ÐºÐ»Ð°Ð½Ð°: $s1
    public static final int __DASHES__ = 500; // ------------------------
    public static final int SYSMSG_ID501 = 501; // ========================
    public static final int YOU_ALREADY_BELONG_TO_ANOTHER_ALLIANCE = 502; // Ð’Ñ‹
    // ÑƒÐ¶Ðµ
    // Ñ�Ð¾Ñ�Ñ‚Ð¾Ð¸Ñ‚Ðµ
    // Ð²
    // Ð°Ð»ÑŒÑ�Ð½Ñ�Ðµ.
    public static final int S1_FRIEND_HAS_LOGGED_IN = 503; // Ð’Ð°Ñˆ Ð´Ñ€ÑƒÐ³ $s1 Ð²Ð¾ÑˆÐµÐ»
    // Ð² Ð¸Ð³Ñ€Ñƒ.
    public static final int ONLY_CLAN_LEADERS_MAY_CREATE_ALLIANCES = 504; // Ð¢Ð¾Ð»ÑŒÐºÐ¾
    // Ð³Ð»Ð°Ð²Ñ‹
    // ÐºÐ»Ð°Ð½Ð¾Ð²
    // Ð¼Ð¾Ð³ÑƒÑ‚
    // Ñ�Ð¾Ð·Ð´Ð°Ñ‚ÑŒ
    // Ð°Ð»ÑŒÑ�Ð½Ñ�.
    public static final int YOU_CANNOT_CREATE_A_NEW_ALLIANCE_WITHIN_1_DAY_AFTER_DISSOLUTION = 505; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ñ�Ð¾Ð·Ð´Ð°Ñ‚ÑŒ
    // Ð°Ð»ÑŒÑ�Ð½Ñ�
    // Ð²
    // Ñ‚ÐµÑ‡ÐµÐ½Ð¸Ðµ
    // 24
    // Ñ‡
    // Ð¿Ð¾Ñ�Ð»Ðµ
    // Ñ€Ð¾Ñ�Ð¿ÑƒÑ�ÐºÐ°
    // Ð¿Ñ€ÐµÐ´Ñ‹Ð´ÑƒÑ‰ÐµÐ³Ð¾.
    public static final int INCORRECT_ALLIANCE_NAME = 506; // Ð�ÐµÐ²ÐµÑ€Ð½Ð¾Ðµ Ð½Ð°Ð·Ð²Ð°Ð½Ð¸Ðµ
    // Ð°Ð»ÑŒÑ�Ð½Ñ�Ð°.
    public static final int INCORRECT_LENGTH_FOR_AN_ALLIANCE_NAME = 507; // Ð�ÐµÐ²ÐµÑ€Ð½Ð°Ñ�
    // Ð´Ð»Ð¸Ð½Ð°
    // Ð½Ð°Ð·Ð²Ð°Ð½Ð¸Ñ�
    // Ð°Ð»ÑŒÑ�Ð½Ñ�Ð°.
    public static final int THIS_ALLIANCE_NAME_ALREADY_EXISTS = 508; // Ð�Ð»ÑŒÑ�Ð½Ñ� Ñ�
    // Ñ‚Ð°ÐºÐ¸Ð¼
    // Ð½Ð°Ð·Ð²Ð°Ð½Ð¸ÐµÐ¼
    // ÑƒÐ¶Ðµ
    // Ñ�ÑƒÑ‰ÐµÑ�Ñ‚Ð²ÑƒÐµÑ‚.
    public static final int CANNOT_ACCEPT_CLAN_ALLY_IS_REGISTERED_AS_AN_ENEMY_DURING_SIEGE_BATTLE = 509; // Ð—Ð°Ñ�Ð²ÐºÐ°
    // Ð¾Ñ‚ÐºÐ»Ð¾Ð½ÐµÐ½Ð°.
    // Ð¡Ð¾ÑŽÐ·Ð½Ñ‹Ð¹
    // ÐºÐ»Ð°Ð½
    // Ð·Ð°Ñ€ÐµÐ³Ð¸Ñ�Ñ‚Ñ€Ð¸Ñ€Ð¾Ð²Ð°Ð½
    // Ð²
    // Ð¾Ñ�Ð°Ð´Ðµ
    // Ð’Ð°ÑˆÐµÐ³Ð¾
    // Ð·Ð°Ð¼ÐºÐ°.
    public static final int YOU_HAVE_INVITED_SOMEONE_TO_YOUR_ALLIANCE = 510; // Ð’Ñ‹
    // Ð¿Ñ€Ð¸Ð³Ð»Ð°Ñ�Ð¸Ð»Ð¸
    // Ð²
    // Ð°Ð»ÑŒÑ�Ð½Ñ�.
    public static final int SELECT_USER_TO_INVITE = 511; // Ð’Ñ‹Ð±ÐµÑ€Ð¸Ñ‚Ðµ, ÐºÐ¾Ð³Ð¾
    // Ñ…Ð¾Ñ‚Ð¸Ñ‚Ðµ Ð¿Ñ€Ð¸Ð³Ð»Ð°Ñ�Ð¸Ñ‚ÑŒ.
    public static final int DO_YOU_REALLY_WISH_TO_WITHDRAW_FROM_THE_ALLIANCE = 512; // Ð’Ñ‹Ð¹Ñ‚Ð¸
    // Ð¸Ð·
    // Ð°Ð»ÑŒÑ�Ð½Ñ�Ð°?
    // ÐŸÐ¾Ñ�Ð»Ðµ
    // Ñ�Ñ‚Ð¾Ð³Ð¾
    // Ð½ÐµÐ»ÑŒÐ·Ñ�
    // Ð²Ñ�Ñ‚ÑƒÐ¿Ð¸Ñ‚ÑŒ
    // Ð²
    // Ð´Ñ€ÑƒÐ³Ð¾Ð¹
    // Ð²
    // Ñ‚ÐµÑ‡ÐµÐ½Ð¸Ðµ
    // 24
    // Ñ‡.
    public static final int ENTER_THE_NAME_OF_THE_CLAN_YOU_WISH_TO_EXPEL = 513; // Ð’Ð²ÐµÐ´Ð¸Ñ‚Ðµ
    // Ð½Ð°Ð·Ð²Ð°Ð½Ð¸Ðµ
    // ÐºÐ»Ð°Ð½Ð°,
    // ÐºÐ¾Ñ‚Ð¾Ñ€Ñ‹Ð¹
    // Ñ…Ð¾Ñ‚Ð¸Ñ‚Ðµ
    // Ð¸Ñ�ÐºÐ»ÑŽÑ‡Ð¸Ñ‚ÑŒ
    // Ð¸Ð·
    // Ð°Ð»ÑŒÑ�Ð½Ñ�Ð°.
    public static final int DO_YOU_REALLY_WISH_TO_DISSOLVE_THE_ALLIANCE = 514; // Ð Ð°Ñ�Ð¿ÑƒÑ�Ñ‚Ð¸Ñ‚ÑŒ
    // Ð°Ð»ÑŒÑ�Ð½Ñ�?
    // ÐŸÐ¾Ñ�Ð»Ðµ
    // Ñ�Ñ‚Ð¾Ð³Ð¾
    // Ð½ÐµÐ»ÑŒÐ·Ñ�
    // Ñ�Ð¾Ð·Ð´Ð°Ñ‚ÑŒ
    // Ð´Ñ€ÑƒÐ³Ð¾Ð¹
    // Ð²
    // Ñ‚ÐµÑ‡ÐµÐ½Ð¸Ðµ
    // 24
    // Ñ‡.
    public static final int ENTER_FILE_NAME_FOR_THE_ALLIANCE_CREST = 515; // Ð’Ð²ÐµÐ´Ð¸Ñ‚Ðµ
    // Ð½Ð°Ð·Ð²Ð°Ð½Ð¸Ðµ
    // Ñ„Ð°Ð¹Ð»Ð°,
    // ÐºÐ¾Ñ‚Ð¾Ñ€Ñ‹Ð¹
    // Ñ…Ð¾Ñ‚Ð¸Ñ‚Ðµ
    // Ð·Ð°Ñ€ÐµÐ³Ð¸Ñ�Ñ‚Ñ€Ð¸Ñ€Ð¾Ð²Ð°Ñ‚ÑŒ
    // ÐºÐ°Ðº
    // Ñ�Ð¼Ð±Ð»ÐµÐ¼Ñƒ
    // Ð°Ð»ÑŒÑ�Ð½Ñ�Ð°.
    public static final int S1_HAS_INVITED_YOU_AS_A_FRIEND = 516; // $s1 Ñ…Ð¾Ñ‡ÐµÑ‚
    // Ð´Ð¾Ð±Ð°Ð²Ð¸Ñ‚ÑŒ
    // Ð’Ð°Ñ� Ð²
    // Ð´Ñ€ÑƒÐ·ÑŒÑ�.
    public static final int YOU_HAVE_ACCEPTED_THE_ALLIANCE = 517; // Ð’Ñ‹ Ð²Ð¾ÑˆÐ»Ð¸ Ð²
    // Ñ�Ð¾Ñ�Ñ‚Ð°Ð²
    // Ð°Ð»ÑŒÑ�Ð½Ñ�Ð°.
    public static final int YOU_HAVE_FAILED_TO_INVITE_A_CLAN_INTO_THE_ALLIANCE = 518; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ð¿Ñ€Ð¸Ð³Ð»Ð°Ñ�Ð¸Ñ‚ÑŒ
    // ÐºÐ»Ð°Ð½
    // Ð²
    // Ð°Ð»ÑŒÑ�Ð½Ñ�.
    public static final int YOU_HAVE_WITHDRAWN_FROM_THE_ALLIANCE = 519; // Ð’Ñ‹
    // Ð²Ñ‹ÑˆÐ»Ð¸
    // Ð¸Ð·
    // Ð°Ð»ÑŒÑ�Ð½Ñ�Ð°.
    public static final int YOU_HAVE_FAILED_TO_WITHDRAW_FROM_THE_ALLIANCE = 520; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ð²Ñ‹Ð¹Ñ‚Ð¸
    // Ð¸Ð·
    // Ð°Ð»ÑŒÑ�Ð½Ñ�Ð°.
    public static final int YOU_HAVE_SUCCEEDED_IN_EXPELLING_A_CLAN = 521; // Ð’Ñ‹
    // Ð¸Ñ�ÐºÐ»ÑŽÑ‡Ð¸Ð»Ð¸
    // ÐºÐ»Ð°Ð½
    // Ð¸Ð·
    // Ð°Ð»ÑŒÑ�Ð½Ñ�Ð°.
    public static final int YOU_HAVE_FAILED_TO_EXPEL_A_CLAN = 522; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ð¸Ñ�ÐºÐ»ÑŽÑ‡Ð¸Ñ‚ÑŒ
    // ÐºÐ»Ð°Ð½ Ð¸Ð·
    // Ð°Ð»ÑŒÑ�Ð½Ñ�Ð°.
    public static final int THE_ALLIANCE_HAS_BEEN_DISSOLVED = 523; // Ð�Ð»ÑŒÑ�Ð½Ñ�
    // Ñ€Ð°Ñ�Ð¿ÑƒÑ‰ÐµÐ½.
    public static final int YOU_HAVE_FAILED_TO_DISSOLVE_THE_ALLIANCE = 524; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ñ€Ð°Ñ�Ð¿ÑƒÑ�Ñ‚Ð¸Ñ‚ÑŒ
    // Ð°Ð»ÑŒÑ�Ð½Ñ�.
    public static final int YOU_HAVE_SUCCEEDED_IN_INVITING_A_FRIEND = 525; // Ð’Ñ‹
    // Ð´Ð¾Ð±Ð°Ð²Ð¸Ð»Ð¸
    // Ð½Ð¾Ð²Ð¾Ð³Ð¾
    // Ð´Ñ€ÑƒÐ³Ð°.
    public static final int YOU_HAVE_FAILED_TO_INVITE_A_FRIEND = 526; // Ð’Ñ‹ Ð½Ðµ
    // Ñ�Ð¼Ð¾Ð³Ð»Ð¸
    // Ð´Ð¾Ð±Ð°Ð²Ð¸Ñ‚ÑŒ
    // Ð½Ð¾Ð²Ð¾Ð³Ð¾
    // Ð´Ñ€ÑƒÐ³Ð°.
    public static final int S2_THE_LEADER_OF_S1_HAS_REQUESTED_AN_ALLIANCE = 527; // $s2,
    // Ð»Ð¸Ð´ÐµÑ€
    // ÐºÐ»Ð°Ð½Ð°
    // $s1,
    // Ð¿Ñ€ÐµÐ´Ð»Ð°Ð³Ð°ÐµÑ‚
    // Ð’Ð°Ð¼
    // Ð°Ð»ÑŒÑ�Ð½Ñ�.
    public static final int FILE_NOT_FOUND = 528; // Ð�ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾ Ð½Ð°Ð¹Ñ‚Ð¸ Ñ„Ð°Ð¹Ð».
    public static final int YOU_MAY_ONLY_REGISTER_8X12_BMP_FILES_WITH_256_COLORS = 529; // Ð¤Ð°Ð¹Ð»:
    // Ñ„Ð¾Ñ€Ð¼Ð°Ñ‚
    // .bmp,
    // 256
    // Ñ†Ð²ÐµÑ‚Ð¾Ð²,
    // Ñ€Ð°Ð·Ð¼ÐµÑ€
    // 8*12.
    public static final int SPIRITSHOT_DOES_NOT_MATCH_WEAPON_GRADE = 530; // Ð—Ð°Ñ€Ñ�Ð´
    // Ð”ÑƒÑ…Ð°
    // Ð½Ðµ
    // Ñ�Ð¾Ð¾Ñ‚Ð²ÐµÑ‚Ñ�Ñ‚Ð²ÑƒÐµÑ‚
    // Ñ€Ð°Ð½Ð³Ñƒ
    // Ð¾Ñ€ÑƒÐ¶Ð¸ÑŽ.
    public static final int NOT_ENOUGH_SPIRITSHOTS = 531; // Ð�ÐµÐ´Ð¾Ñ�Ñ‚Ð°Ñ‚Ð¾Ñ‡Ð½Ð¾
    // Ð—Ð°Ñ€Ñ�Ð´Ð¾Ð² Ð”ÑƒÑ…Ð°.
    public static final int CANNOT_USE_SPIRITSHOTS = 532; // Ð�ÐµÐ»ÑŒÐ·Ñ� Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒ
    // Ð—Ð°Ñ€Ñ�Ð´ Ð”ÑƒÑ…Ð°.
    public static final int POWER_OF_MANA_ENABLED = 533; // ÐœÐ°Ð³Ð¸Ñ� Ð½Ð°Ð¿Ð¾Ð»Ð½Ñ�ÐµÑ‚ Ð’Ð°ÑˆÐµ
    // Ð¾Ñ€ÑƒÐ¶Ð¸Ðµ.
    public static final int POWER_OF_MANA_DISABLED = 534; // Ð”ÐµÐ¹Ñ�Ñ‚Ð²Ð¸Ðµ Ð—Ð°Ñ€Ñ�Ð´Ð°
    // Ð”ÑƒÑ…Ð° Ð·Ð°ÐºÐ¾Ð½Ñ‡Ð¸Ð»Ð¾Ñ�ÑŒ.
    public static final int NAME_PET = 535; // Ð’Ð²ÐµÐ´Ð¸Ñ‚Ðµ Ð¸Ð¼Ñ� Ð¿Ð¸Ñ‚Ð¾Ð¼Ñ†Ð°.
    public static final int HOW_MUCH_ADENA_DO_YOU_WISH_TO_TRANSFER_TO_YOUR_INVENTORY = 536; // Ð¡ÐºÐ¾Ð»ÑŒÐºÐ¾
    // Ð°Ð´ÐµÐ½
    // Ð¿ÐµÑ€ÐµÐ¼ÐµÑ�Ñ‚Ð¸Ñ‚ÑŒ
    // Ð²
    // Ð¸Ð½Ð²ÐµÐ½Ñ‚Ð°Ñ€ÑŒ?
    public static final int HOW_MUCH_WILL_YOU_TRANSFER = 537; // Ð¡ÐºÐ¾Ð»ÑŒÐºÐ¾
    // Ð¿ÐµÑ€ÐµÐ¼ÐµÑ�Ñ‚Ð¸Ñ‚ÑŒ?
    public static final int SP_HAS_DECREASED_BY_S1 = 538; // SP Ñ�Ð½Ð¸Ð¶ÐµÐ½Ñ‹ Ð½Ð° $s1.
    public static final int EXPERIENCE_HAS_DECREASED_BY_S1 = 539; // ÐžÐ¿Ñ‹Ñ‚ Ñ�Ð½Ð¸Ð¶ÐµÐ½
    // Ð½Ð° $s1.
    public static final int CLAN_LEADERS_CANNOT_BE_DELETED_DISSOLVE_THE_CLAN_AND_TRY_AGAIN = 540; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // ÑƒÐ´Ð°Ð»Ð¸Ñ‚ÑŒ
    // Ð»Ð¸Ð´ÐµÑ€Ð°
    // ÐºÐ»Ð°Ð½Ð°.
    // Ð Ð°Ñ�Ð¿ÑƒÑ�Ñ‚Ð¸Ñ‚Ðµ
    // ÐºÐ»Ð°Ð½
    // Ð¸
    // Ð¿Ð¾Ð¿Ñ€Ð¾Ð±ÑƒÐ¹Ñ‚Ðµ
    // Ñ�Ð½Ð¾Ð²Ð°.
    public static final int YOU_CANNOT_DELETE_A_CLAN_MEMBER_WITHDRAW_FROM_THE_CLAN_AND_TRY_AGAIN = 541; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // ÑƒÐ´Ð°Ð»Ð¸Ñ‚ÑŒ
    // Ñ‡Ð»ÐµÐ½Ð°
    // ÐºÐ»Ð°Ð½Ð°.
    // Ð’Ñ‹Ð¹Ð´Ð¸Ñ‚Ðµ
    // Ð¸Ð·
    // ÐºÐ»Ð°Ð½Ð°
    // Ð¸
    // Ð¿Ð¾Ð¿Ñ€Ð¾Ð±ÑƒÐ¹Ñ‚Ðµ
    // Ñ�Ð½Ð¾Ð²Ð°.
    public static final int NPC_SERVER_NOT_OPERATING_PETS_CANNOT_BE_SUMMONED = 542; // Ð¡ÐµÑ€Ð²ÐµÑ€
    // NPC
    // Ð¾Ñ‚ÐºÐ»ÑŽÑ‡ÐµÐ½.
    // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ð¿Ñ€Ð¸Ð·Ð²Ð°Ñ‚ÑŒ
    // Ð¿Ð¸Ñ‚Ð¾Ð¼Ñ†Ð°.
    public static final int YOU_ALREADY_HAVE_A_PET = 543; // Ð£ Ð’Ð°Ñ� ÑƒÐ¶Ðµ ÐµÑ�Ñ‚ÑŒ
    // Ð¿Ð¸Ñ‚Ð¾Ð¼ÐµÑ†.
    public static final int ITEM_NOT_AVAILABLE_FOR_PETS = 544; // ÐŸÐ¸Ñ‚Ð¾Ð¼ÐµÑ† Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒ
    // Ñ�Ñ‚Ð¾Ñ‚ Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚.
    public static final int DUE_TO_THE_VOLUME_LIMIT_OF_THE_PETS_INVENTORY_NO_MORE_ITEMS_CAN_BE_PLACED_THERE = 545; // Ð˜Ð½Ð²ÐµÐ½Ñ‚Ð°Ñ€ÑŒ
    // Ð¿Ð¸Ñ‚Ð¾Ð¼Ñ†Ð°
    // Ð¿ÐµÑ€ÐµÐ¿Ð¾Ð»Ð½ÐµÐ½.
    // Ð£Ð´Ð°Ð»Ð¸Ñ‚Ðµ
    // Ñ‡Ñ‚Ð¾-Ð½Ð¸Ð±ÑƒÐ´ÑŒ
    // Ð¸
    // Ð¿Ð¾Ð²Ñ‚Ð¾Ñ€Ð¸Ñ‚Ðµ
    // Ð¿Ð¾Ð¿Ñ‹Ñ‚ÐºÑƒ.
    public static final int EXCEEDED_PET_INVENTORYS_WEIGHT_LIMIT = 546; // Ð˜Ð½Ð²ÐµÐ½Ñ‚Ð°Ñ€ÑŒ
    // Ð¿Ð¸Ñ‚Ð¾Ð¼Ñ†Ð°
    // Ð¿ÐµÑ€ÐµÐ¿Ð¾Ð»Ð½ÐµÐ½.
    public static final int SUMMON_A_PET = 547; // ÐŸÑ€Ð¸Ð·Ñ‹Ð² Ð¿Ð¸Ñ‚Ð¾Ð¼Ñ†Ð°â€¦
    public static final int YOUR_PETS_NAME_CAN_BE_UP_TO_8_CHARACTERS = 548; // ÐœÐ°ÐºÑ�Ð¸Ð¼Ð°Ð»ÑŒÐ½Ð°Ñ�
    // Ð´Ð»Ð¸Ð½Ð°
    // Ð¸Ð¼ÐµÐ½Ð¸
    // Ð¿Ð¸Ñ‚Ð¾Ð¼Ñ†Ð°
    // -
    // 8
    // Ñ�Ð¸Ð¼Ð²Ð¾Ð»Ð¾Ð².
    public static final int TO_CREATE_AN_ALLIANCE_YOUR_CLAN_MUST_BE_LEVEL_5_OR_HIGHER = 549; // Ð¢Ð¾Ð»ÑŒÐºÐ¾
    // ÐºÐ»Ð°Ð½
    // 5-Ð³Ð¾
    // ÑƒÑ€Ð¾Ð²Ð½Ñ�
    // Ð¸Ð»Ð¸
    // Ð²Ñ‹ÑˆÐµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ñ�Ð¾Ð·Ð´Ð°Ñ‚ÑŒ
    // Ð°Ð»ÑŒÑ�Ð½Ñ�.
    public static final int YOU_CANNOT_CREATE_AN_ALLIANCE_DURING_THE_TERM_OF_DISSOLUTION_POSTPONEMENT = 550; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ñ�Ð¾Ð·Ð´Ð°Ñ‚ÑŒ
    // Ð°Ð»ÑŒÑ�Ð½Ñ�
    // Ñ�Ñ€Ð°Ð·Ñƒ
    // Ð¶Ðµ
    // Ð¿Ð¾Ñ�Ð»Ðµ
    // Ñ€Ð¾Ñ�Ð¿ÑƒÑ�ÐºÐ°
    // Ð¿Ñ€ÐµÐ´Ñ‹Ð´ÑƒÑ‰ÐµÐ³Ð¾.
    public static final int YOU_CANNOT_RAISE_YOUR_CLAN_LEVEL_DURING_THE_TERM_OF_DISPERSION_POSTPONEMENT = 551; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ð¿Ð¾Ð²Ñ‹Ñ�Ð¸Ñ‚ÑŒ
    // ÑƒÑ€Ð¾Ð²ÐµÐ½ÑŒ
    // ÐºÐ»Ð°Ð½Ð°
    // Ð¿Ð¾Ñ�Ð»Ðµ
    // Ð¾Ð±ÑŠÑ�Ð²Ð»ÐµÐ½Ð¸Ñ�
    // Ð¾
    // ÐµÐ³Ð¾
    // Ñ€Ð¾Ñ�Ð¿ÑƒÑ�ÐºÐµ.
    public static final int DURING_THE_GRACE_PERIOD_FOR_DISSOLVING_A_CLAN_REGISTRATION_OR_DELETION_OF_A_CLANS_CREST_IS_NOT_ALLOWED = 552; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ð¸Ð·Ð¼ÐµÐ½Ð¸Ñ‚ÑŒ/ÑƒÐ´Ð°Ð»Ð¸Ñ‚ÑŒ
    // Ñ�Ð¼Ð±Ð»ÐµÐ¼Ñƒ
    // ÐºÐ»Ð°Ð½Ð°
    // Ð²Ð¾
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ñ€Ð¾Ñ�Ð¿ÑƒÑ�ÐºÐ°
    // ÐºÐ»Ð°Ð½Ð°.
    public static final int THE_OPPOSING_CLAN_HAS_APPLIED_FOR_DISPERSION = 553; // Ð’Ñ‹Ð±Ñ€Ð°Ð½Ð½Ñ‹Ð¹
    // ÐºÐ»Ð°Ð½
    // Ð½Ð°Ñ…Ð¾Ð´Ð¸Ñ‚Ñ�Ñ�
    // Ð²
    // Ñ�Ð¾Ñ�Ñ‚Ð¾Ñ�Ð½Ð¸Ð¸
    // Ñ€Ð¾Ñ�Ð¿ÑƒÑ�ÐºÐ°.
    public static final int YOU_CANNOT_DISPERSE_THE_CLANS_IN_YOUR_ALLIANCE = 554; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ñ€Ð°Ñ�Ñ„Ð¾Ñ€Ð¼Ð¸Ñ€Ð¾Ð²Ð°Ñ‚ÑŒ
    // ÐºÐ»Ð°Ð½,
    // Ñ�Ð¾Ñ�Ñ‚Ð¾Ñ�Ñ‰Ð¸Ð¹
    // Ð²
    // Ð°Ð»ÑŒÑ�Ð½Ñ�Ðµ.
    public static final int YOU_CANNOT_MOVE_YOUR_ITEM_WEIGHT_IS_TOO_GREAT = 555; // Ð’Ñ‹
    // Ð¿ÐµÑ€ÐµÐ³Ñ€ÑƒÐ¶ÐµÐ½Ñ‹
    // Ð¸
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¿ÐµÑ€ÐµÐ´Ð²Ð¸Ð³Ð°Ñ‚ÑŒÑ�Ñ�.
    public static final int YOU_CANNOT_MOVE_IN_THIS_STATE = 556; // Ð’Ñ‹ Ð½Ðµ Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¿ÐµÑ€ÐµÐ´Ð²Ð¸Ð³Ð°Ñ‚ÑŒÑ�Ñ�
    // Ð² Ñ‚Ð°ÐºÐ¾Ð¼
    // Ñ�Ð¾Ñ�Ñ‚Ð¾Ñ�Ð½Ð¸Ð¸.
    public static final int THE_PET_HAS_BEEN_SUMMONED_AND_CANNOT_BE_DELETED = 557; // ÐŸÐ¸Ñ‚Ð¾Ð¼ÐµÑ†
    // Ð¿Ñ€Ð¸Ð·Ð²Ð°Ð½
    // Ð¸
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // ÑƒÐ´Ð°Ð»ÐµÐ½.
    public static final int THE_PET_HAS_BEEN_SUMMONED_AND_CANNOT_BE_LET_GO = 558; // ÐŸÐ¸Ñ‚Ð¾Ð¼ÐµÑ†
    // Ð¿Ñ€Ð¸Ð·Ð²Ð°Ð½
    // Ð¸
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð¾Ñ‚Ð¿ÑƒÑ‰ÐµÐ½.
    public static final int PURCHASED_S2_FROM_S1 = 559; // Ð’Ñ‹ ÐºÑƒÐ¿Ð¸Ð»Ð¸ Ñƒ Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°
    // $c1: $s2.
    public static final int PURCHASED_S2_S3_FROM_S1 = 560; // Ð’Ñ‹ ÐºÑƒÐ¿Ð¸Ð»Ð¸ Ñƒ
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð° $c1:
    // +$s2 $s3.
    public static final int PURCHASED_S3_S2_S_FROM_S1_ = 561; // Ð’Ñ‹ ÐºÑƒÐ¿Ð¸Ð»Ð¸ Ñƒ
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð° $c1:
    // $s2 ($s3 ÑˆÑ‚.)
    public static final int CANNOT_CRYSTALLIZE_CRYSTALLIZATION_SKILL_LEVEL_TOO_LOW = 562; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ñ€Ð°Ð·Ð±Ð¸Ñ‚ÑŒ
    // Ð½Ð°
    // ÐºÑ€Ð¸Ñ�Ñ‚Ð°Ð»Ð»Ñ‹
    // Ñ�Ñ‚Ð¾Ñ‚
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚.
    // Ð£Ñ€Ð¾Ð²ÐµÐ½ÑŒ
    // Ð’Ð°ÑˆÐµÐ³Ð¾
    // ÑƒÐ¼ÐµÐ½Ð¸Ñ�
    // Ñ�Ð»Ð¸ÑˆÐºÐ¾Ð¼
    // Ð½Ð¸Ð·Ð¾Ðº.
    public static final int FAILED_TO_DISABLE_ATTACK_TARGET = 563; // Ð�ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾
    // Ñ�Ð±Ñ€Ð¾Ñ�Ð¸Ñ‚ÑŒ
    // Ñ†ÐµÐ»ÑŒ
    // Ð°Ñ‚Ð°ÐºÐ¸.
    public static final int FAILED_TO_CHANGE_ATTACK_TARGET = 564; // Ð�ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾
    // Ñ�Ð¼ÐµÐ½Ð¸Ñ‚ÑŒ
    // Ñ†ÐµÐ»ÑŒ Ð°Ñ‚Ð°ÐºÐ¸.
    public static final int NOT_ENOUGH_LUCK = 565; // Ð£ Ð’Ð°Ñ� Ð½Ðµ Ñ…Ð²Ð°Ñ‚Ð°ÐµÑ‚ ÑƒÐ´Ð°Ñ‡Ð¸.
    public static final int CONFUSION_FAILED = 566; // Ð’Ð°ÑˆÐµ Ð·Ð°ÐºÐ»Ð¸Ð½Ð°Ð½Ð¸Ðµ Ð½Ðµ
    // Ð¿Ð¾Ð´ÐµÐ¹Ñ�Ñ‚Ð²Ð¾Ð²Ð°Ð»Ð¾.
    public static final int FEAR_FAILED = 567; // Ð’Ð°ÑˆÐµ Ð·Ð°ÐºÐ»Ð¸Ð½Ð°Ð½Ð¸Ðµ Ñ�Ñ‚Ñ€Ð°Ñ…Ð° Ð½Ðµ
    // Ð¿Ð¾Ð´ÐµÐ¹Ñ�Ñ‚Ð²Ð¾Ð²Ð°Ð»Ð¾.
    public static final int CUBIC_SUMMONING_FAILED = 568; // Ð�Ðµ ÑƒÐ´Ð°Ð»Ð¾Ñ�ÑŒ Ð¿Ñ€Ð¸Ð·Ð²Ð°Ñ‚ÑŒ
    // ÐšÑƒÐ±.
    public static final int CAUTION_THE_ITEM_PRICE_GREATLY_DIFFERS_FROM_THE_SHOPS_STANDARD_PRICE_DO_YOU_WISH_TO_CONTINUE = 569; // Ð’Ð½Ð¸Ð¼Ð°Ð½Ð¸Ðµ
    // -
    // Ñ†ÐµÐ½Ð°
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ð°
    // Ñ�Ð¸Ð»ÑŒÐ½Ð¾
    // Ð¾Ñ‚Ð»Ð¸Ñ‡Ð°ÐµÑ‚Ñ�Ñ�
    // Ð¾Ñ‚
    // Ñ†ÐµÐ½Ñ‹
    // Ð²
    // Ð¼Ð°Ð³Ð°Ð·Ð¸Ð½Ðµ.
    // ÐŸÑ€Ð¾Ð´Ð¾Ð»Ð¶Ð¸Ñ‚ÑŒ?
    public static final int HOW_MANY__S1__S_DO_YOU_WISH_TO_PURCHASE = 570; // $s1:
    // Ñ�ÐºÐ¾Ð»ÑŒÐºÐ¾
    // ÐºÑƒÐ¿Ð¸Ñ‚ÑŒ?
    public static final int HOW_MANY__S1__S_DO_YOU_WANT_TO_PURCHASE = 571; // $s1:
    // Ñ�ÐºÐ¾Ð»ÑŒÐºÐ¾
    // ÑƒÐ´Ð°Ð»Ð¸Ñ‚ÑŒ
    // Ð¸Ð·
    // Ñ�Ð¿Ð¸Ñ�ÐºÐ°
    // Ð¿Ð¾ÐºÑƒÐ¿Ð¾Ðº?
    public static final int DO_YOU_WISH_TO_JOIN_S1S_PARTY_ITEM_DISTRIBUTION_FINDERS_KEEPERS = 572; // Ð’Ñ�Ñ‚ÑƒÐ¿Ð¸Ñ‚ÑŒ
    // Ð²
    // Ð³Ñ€ÑƒÐ¿Ð¿Ñƒ
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°
    // $c1?
    // (Ñ€Ð°Ñ�Ð¿Ñ€ÐµÐ´ÐµÐ»ÐµÐ½Ð¸Ðµ
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ð¾Ð²:
    // Ð�Ð°ÑˆÐµÐ´ÑˆÐµÐ¼Ñƒ)
    public static final int DO_YOU_WISH_TO_JOIN_S1S_PARTY_ITEM_DISTRIBUTION_RANDOM = 573; // Ð’Ñ�Ñ‚ÑƒÐ¿Ð¸Ñ‚ÑŒ
    // Ð²
    // Ð³Ñ€ÑƒÐ¿Ð¿Ñƒ
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°
    // $c1?
    // (Ñ€Ð°Ñ�Ð¿Ñ€ÐµÐ´ÐµÐ»ÐµÐ½Ð¸Ðµ
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ð¾Ð²:
    // Ð¡Ð»ÑƒÑ‡Ð°Ð¹Ð½Ð¾)
    public static final int PETS_AND_SERVITORS_ARE_NOT_AVAILABLE_AT_THIS_TIME = 574; // Ð’
    // Ð´Ð°Ð½Ð½Ñ‹Ð¹
    // Ð¼Ð¾Ð¼ÐµÐ½Ñ‚
    // Ð¿Ñ€Ð¸Ð·Ñ‹Ð²
    // Ð¿Ð¸Ñ‚Ð¾Ð¼Ñ†Ð°
    // Ð¸
    // Ð´Ñ€ÑƒÐ³Ð¸Ñ…
    // Ñ�ÑƒÑ‰ÐµÑ�Ñ‚Ð²
    // Ð½ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶ÐµÐ½.
    public static final int HOW_MUCH_ADENA_DO_YOU_WISH_TO_TRANSFER_TO_YOUR_PET = 575; // Ð¡ÐºÐ¾Ð»ÑŒÐºÐ¾
    // Ð°Ð´ÐµÐ½
    // Ð¿ÐµÑ€ÐµÐ´Ð°Ñ‚ÑŒ
    // Ð¿Ð¸Ñ‚Ð¾Ð¼Ñ†Ñƒ?
    public static final int HOW_MUCH_DO_YOU_WISH_TO_TRANSFER = 576; // Ð¡ÐºÐ¾Ð»ÑŒÐºÐ¾
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ð¾Ð²
    // Ð¿ÐµÑ€ÐµÐ¼ÐµÑ�Ñ‚Ð¸Ñ‚ÑŒ?
    public static final int YOU_CANNOT_SUMMON_DURING_A_TRADE_OR_WHILE_USING_THE_PRIVATE_SHOPS = 577; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ð¿Ñ€Ð¸Ð·Ñ‹Ð²Ð°Ñ‚ÑŒ
    // Ð²Ð¾
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ð¾Ð±Ð¼ÐµÐ½Ð°
    // Ð¸Ð»Ð¸
    // Ñ‚Ð¾Ñ€Ð³Ð¾Ð²Ð»Ð¸.
    public static final int YOU_CANNOT_SUMMON_DURING_COMBAT = 578; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ð¿Ñ€Ð¸Ð·Ñ‹Ð²Ð°Ñ‚ÑŒ
    // Ð²Ð¾ Ð²Ñ€ÐµÐ¼Ñ�
    // Ð±Ð¸Ñ‚Ð²Ñ‹.
    public static final int A_PET_CANNOT_BE_SENT_BACK_DURING_BATTLE = 579; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ð¾Ñ‚Ð¾Ð·Ð²Ð°Ñ‚ÑŒ
    // Ð¿Ð¸Ñ‚Ð¾Ð¼Ñ†Ð°
    // Ð²Ð¾
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ð±Ð¸Ñ‚Ð²Ñ‹.
    public static final int YOU_MAY_NOT_USE_MULTIPLE_PETS_OR_SERVITORS_AT_THE_SAME_TIME = 580; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ð¿Ñ€Ð¸Ð·Ð²Ð°Ñ‚ÑŒ
    // Ð±Ð¾Ð»ÐµÐµ
    // Ð¾Ð´Ð½Ð¾Ð³Ð¾
    // Ð¿Ð¸Ñ‚Ð¾Ð¼Ñ†Ð°
    // Ð·Ð°
    // Ñ€Ð°Ð·.
    public static final int THERE_IS_A_SPACE_IN_THE_NAME = 581; // Ð’ Ð¸Ð¼ÐµÐ½Ð¸ ÐµÑ�Ñ‚ÑŒ
    // Ð¿Ñ€Ð¾Ð±ÐµÐ».
    public static final int INAPPROPRIATE_CHARACTER_NAME = 582; // Ð—Ð°Ð¿Ñ€ÐµÑ‰ÐµÐ½Ð½Ð¾Ðµ
    // Ð¸Ð¼Ñ�
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°.
    public static final int NAME_INCLUDES_FORBIDDEN_WORDS = 583; // Ð’ Ð¸Ð¼ÐµÐ½Ð¸
    // Ñ�Ð¾Ð´ÐµÑ€Ð¶Ð¸Ñ‚Ñ�Ñ�
    // Ð·Ð°Ð¿Ñ€ÐµÑ‰ÐµÐ½Ð½Ð¾Ðµ
    // Ñ�Ð»Ð¾Ð²Ð¾.
    public static final int ALREADY_IN_USE_BY_ANOTHER_PET = 584; // ÐŸÐ¸Ñ‚Ð¾Ð¼ÐµÑ† Ñ�
    // Ñ‚Ð°ÐºÐ¸Ð¼ Ð¸Ð¼ÐµÐ½ÐµÐ¼
    // ÑƒÐ¶Ðµ
    // Ñ�ÑƒÑ‰ÐµÑ�Ñ‚Ð²ÑƒÐµÑ‚.
    public static final int PLEASE_DECIDE_ON_THE_PRICE = 585; // Ð£Ñ�Ñ‚Ð°Ð½Ð¾Ð²Ð¸Ñ‚Ðµ Ñ†ÐµÐ½Ñƒ
    // Ð¿Ð¾ÐºÑƒÐ¿ÐºÐ¸.
    public static final int PET_ITEMS_CANNOT_BE_REGISTERED_AS_SHORTCUTS = 586; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ð¿Ð¾Ð¼ÐµÑ�Ñ‚Ð¸Ñ‚ÑŒ
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ñ‹
    // Ð¿Ð¸Ñ‚Ð¾Ð¼Ñ†Ð°
    // Ð²
    // Ñ�Ñ‡ÐµÐ¹ÐºÐ¸
    // Ð±Ñ‹Ñ�Ñ‚Ñ€Ð¾Ð³Ð¾
    // Ð´Ð¾Ñ�Ñ‚ÑƒÐ¿Ð°.
    public static final int IRREGULAR_SYSTEM_SPEED = 587; // Ð¡ÐºÐ¾Ñ€Ð¾Ñ�Ñ‚ÑŒ Ð’Ð°ÑˆÐµÐ¹
    // Ñ�Ð¸Ñ�Ñ‚ÐµÐ¼Ñ‹ Ð½Ð¸Ð·ÐºÐ°.
    public static final int PET_INVENTORY_IS_FULL = 588; // Ð˜Ð½Ð²ÐµÐ½Ñ‚Ð°Ñ€ÑŒ Ð¿Ð¸Ñ‚Ð¾Ð¼Ñ†Ð°
    // Ð¿Ð¾Ð»Ð¾Ð½.
    public static final int A_DEAD_PET_CANNOT_BE_SENT_BACK = 589; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ð¿Ñ€Ð¸Ð·Ð²Ð°Ñ‚ÑŒ
    // Ð¼ÐµÑ€Ñ‚Ð²Ð¾Ð³Ð¾
    // Ð¿Ð¸Ñ‚Ð¾Ð¼Ñ†Ð°.
    public static final int CANNOT_GIVE_ITEMS_TO_A_DEAD_PET = 590; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ð¿ÐµÑ€ÐµÐ´Ð°Ñ‚ÑŒ
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚
    // Ð¼ÐµÑ€Ñ‚Ð²Ð¾Ð¼Ñƒ
    // Ð¿Ð¸Ñ‚Ð¾Ð¼Ñ†Ñƒ.
    public static final int AN_INVALID_CHARACTER_IS_INCLUDED_IN_THE_PETS_NAME = 591; // Ð˜Ð¼Ñ�
    // Ð¿Ð¸Ñ‚Ð¾Ð¼Ñ†Ð°
    // Ñ�Ð¾Ð´ÐµÑ€Ð¶Ð¸Ñ‚
    // Ð·Ð°Ð¿Ñ€ÐµÑ‰ÐµÐ½Ð½Ñ‹Ð¹
    // Ñ�Ð¸Ð¼Ð²Ð¾Ð».
    public static final int DO_YOU_WISH_TO_DISMISS_YOUR_PET_DISMISSING_YOUR_PET_WILL_CAUSE_THE_PET_NECKLACE_TO_DISAPPEAR = 592; // Ð£Ð½Ð¸Ñ‡Ñ‚Ð¾Ð¶Ð¸Ñ‚ÑŒ
    // Ð¿Ð¸Ñ‚Ð¾Ð¼Ñ†Ð°?
    // ÐŸÑ€Ð¸
    // ÑƒÐ½Ð¸Ñ‡Ñ‚Ð¾Ð¶ÐµÐ½Ð¸Ð¸
    // Ð¿Ñ€Ð¾Ð¿Ð°Ð´ÐµÑ‚
    // Ð¾ÑˆÐµÐ¹Ð½Ð¸Ðº
    // Ð¿Ð¸Ñ‚Ð¾Ð¼Ñ†Ð°.
    public static final int YOUR_PET_HAS_LEFT_DUE_TO_UNBEARABLE_HUNGER = 593; // ÐŸÐ¸Ñ‚Ð¾Ð¼ÐµÑ†
    // Ñ�Ð±ÐµÐ¶Ð°Ð»,
    // Ñ‚Ð°Ðº
    // Ð¸
    // Ð½Ðµ
    // Ð´Ð¾Ð¶Ð´Ð°Ð²ÑˆÐ¸Ñ�ÑŒ
    // Ð¾Ñ‚
    // Ð’Ð°Ñ�
    // ÐµÐ´Ñ‹.
    public static final int YOU_CANNOT_RESTORE_HUNGRY_PETS = 594; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ð²ÐµÑ€Ð½ÑƒÑ‚ÑŒ
    // Ð³Ð¾Ð»Ð¾Ð´Ð½Ð¾Ð³Ð¾
    // Ð¿Ð¸Ñ‚Ð¾Ð¼Ñ†Ð°.
    public static final int YOUR_PET_IS_VERY_HUNGRY = 595; // ÐŸÐ¸Ñ‚Ð¾Ð¼ÐµÑ† Ð¾Ñ‡ÐµÐ½ÑŒ
    // Ð³Ð¾Ð»Ð¾Ð´ÐµÐ½.
    public static final int YOUR_PET_ATE_A_LITTLE_BUT_IS_STILL_HUNGRY = 596; // ÐŸÐ¸Ñ‚Ð¾Ð¼ÐµÑ†
    // Ð½ÐµÐ¼Ð½Ð¾Ð³Ð¾
    // Ð¿Ð¾ÐµÐ»,
    // Ð½Ð¾
    // Ð²Ñ�Ðµ
    // Ñ€Ð°Ð²Ð½Ð¾
    // Ð³Ð¾Ð»Ð¾Ð´ÐµÐ½.
    public static final int YOUR_PET_IS_VERY_HUNGRY_PLEASE_BE_CAREFUL = 597; // ÐŸÐ¸Ñ‚Ð¾Ð¼ÐµÑ†
    // Ð¾Ñ‡ÐµÐ½ÑŒ
    // Ð³Ð¾Ð»Ð¾Ð´ÐµÐ½,
    // Ð±ÑƒÐ´ÑŒÑ‚Ðµ
    // Ð¾Ñ�Ñ‚Ð¾Ñ€Ð¾Ð¶Ð½Ñ‹.
    public static final int YOU_CANNOT_CHAT_WHILE_YOU_ARE_INVISIBLE = 598; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒÑ�Ñ�
    // Ñ‡Ð°Ñ‚Ð¾Ð¼
    // Ð²
    // Ñ€ÐµÐ¶Ð¸Ð¼Ðµ
    // Ð½ÐµÐ²Ð¸Ð´Ð¸Ð¼Ð¾Ñ�Ñ‚Ð¸.
    public static final int THE_GM_HAS_AN_IMPORTANT_NOTICE_CHAT_IS_TEMPORARILY_ABORTED = 599; // Ð’Ð½Ð¸Ð¼Ð°Ð½Ð¸Ðµ!
    // Ð’Ð°Ð¶Ð½Ð¾Ðµ
    // Ð¾Ð±ÑŠÑ�Ð²Ð»ÐµÐ½Ð¸Ðµ
    // Ñ�Ð»ÑƒÐ¶Ð±Ñ‹
    // Ð¿Ð¾Ð´Ð´ÐµÑ€Ð¶ÐºÐ¸!
    // Ð�ÐµÐºÐ¾Ñ‚Ð¾Ñ€Ð¾Ðµ
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ñ‡Ð°Ñ‚
    // Ð±ÑƒÐ´ÐµÑ‚
    // Ð½ÐµÐ´Ð¾Ñ�Ñ‚ÑƒÐ¿ÐµÐ½.
    public static final int YOU_CANNOT_EQUIP_A_PET_ITEM = 600; // Ð�ÐµÐ»ÑŒÐ·Ñ� Ð½Ð°Ð´ÐµÑ‚ÑŒ
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚
    // Ð¿Ð¸Ñ‚Ð¾Ð¼Ñ†Ð°.
    public static final int THERE_ARE_S1_PETITIONS_PENDING = 601; // ÐšÐ¾Ð»Ð¸Ñ‡ÐµÑ�Ñ‚Ð²Ð¾
    // Ð½ÐµÑ€Ð°Ñ�Ñ�Ð¼Ð¾Ñ‚Ñ€ÐµÐ½Ð½Ñ‹Ñ…
    // Ð¿ÐµÑ‚Ð¸Ñ†Ð¸Ð¹:
    // $S1.
    public static final int THE_PETITION_SYSTEM_IS_CURRENTLY_UNAVAILABLE_PLEASE_TRY_AGAIN_LATER = 602; // Ð¡Ð¸Ñ�Ñ‚ÐµÐ¼Ð°
    // Ð¿ÐµÑ‚Ð¸Ñ†Ð¸Ð¹
    // Ð½ÐµÐ´Ð¾Ñ�Ñ‚ÑƒÐ¿Ð½Ð°.
    // ÐŸÐ¾Ð¿Ñ€Ð¾Ð±ÑƒÐ¹Ñ‚Ðµ
    // Ð²Ð¾Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒÑ�Ñ�
    // ÐµÑŽ
    // Ð¿Ð¾Ð·Ð¶Ðµ.
    public static final int THAT_ITEM_CANNOT_BE_DISCARDED_OR_EXCHANGED = 603; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ð²Ñ‹Ð±Ñ€Ð¾Ñ�Ð¸Ñ‚ÑŒ
    // Ð¸Ð»Ð¸
    // Ð¾Ð±Ð¼ÐµÐ½Ñ�Ñ‚ÑŒ
    // Ð´Ð°Ð½Ð½Ñ‹Ð¹
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚.
    public static final int YOU_MAY_NOT_CALL_FORTH_A_PET_OR_SUMMONED_CREATURE_FROM_THIS_LOCATION = 604; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ð¿Ñ€Ð¸Ð·Ð²Ð°Ñ‚ÑŒ
    // Ð¿Ð¸Ñ‚Ð¾Ð¼Ñ†Ð°
    // Ð¸Ð»Ð¸
    // Ð´Ñ€ÑƒÐ³Ð¸Ñ…
    // Ñ�ÑƒÑ‰ÐµÑ�Ñ‚Ð²
    // Ð²
    // Ñ�Ñ‚Ð¾Ð¼
    // Ð¼ÐµÑ�Ñ‚Ðµ.
    public static final int YOU_MAY_REGISTER_UP_TO_64_PEOPLE_ON_YOUR_LIST = 605; // Ð’
    // Ñ�Ð¿Ð¸Ñ�Ð¾Ðº
    // Ð¼Ð¾Ð¶Ð½Ð¾
    // Ð²Ð½ÐµÑ�Ñ‚Ð¸
    // Ð´Ð¾
    // 64
    // Ñ‡ÐµÐ»Ð¾Ð²ÐµÐº.
    public static final int YOU_CANNOT_BE_REGISTERED_BECAUSE_THE_OTHER_PERSON_HAS_ALREADY_REGISTERED_64_PEOPLE_ON_HIS_HER_LIST = 606; // Ð¡Ð¿Ð¸Ñ�Ð¾Ðº
    // Ð´Ð°Ð½Ð½Ð¾Ð³Ð¾
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°
    // Ð¿ÐµÑ€ÐµÐ¿Ð¾Ð»Ð½ÐµÐ½.
    public static final int YOU_DO_NOT_HAVE_ANY_FURTHER_SKILLS_TO_LEARN__COME_BACK_WHEN_YOU_HAVE_REACHED_LEVEL_S1 = 607; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¸Ð·ÑƒÑ‡Ð°Ñ‚ÑŒ
    // ÑƒÐ¼ÐµÐ½Ð¸Ñ�.
    // Ð’Ð¾Ð·Ð²Ñ€Ð°Ñ‰Ð°Ð¹Ñ‚ÐµÑ�ÑŒ,
    // ÐºÐ¾Ð³Ð´Ð°
    // Ð´Ð¾Ñ�Ñ‚Ð¸Ð³Ð½ÐµÑ‚Ðµ
    // $s1-Ð³Ð¾
    // ÑƒÑ€Ð¾Ð²Ð½Ñ�.
    public static final int S1_HAS_OBTAINED_3_S2_S_BY_USING_SWEEPER = 608; // $c1
    // Ð¿Ð¾Ð»ÑƒÑ‡Ð°ÐµÑ‚:
    // $s2
    // ($s3
    // ÑˆÑ‚.)
    // Ñ�
    // Ð¿Ð¾Ð¼Ð¾Ñ‰ÑŒÑŽ
    // ÑƒÐ¼ÐµÐ½Ð¸Ñ�
    // Â«ÐŸÑ€Ð¸Ñ�Ð²Ð¾Ð¸Ñ‚ÑŒÂ».
    public static final int S1_HAS_OBTAINED_S2_BY_USING_SWEEPER = 609; // $c1
    // Ð¿Ð¾Ð»ÑƒÑ‡Ð°ÐµÑ‚:
    // $s2 Ñ�
    // Ð¿Ð¾Ð¼Ð¾Ñ‰ÑŒÑŽ
    // ÑƒÐ¼ÐµÐ½Ð¸Ñ�
    // Â«ÐŸÑ€Ð¸Ñ�Ð²Ð¾Ð¸Ñ‚ÑŒÂ».
    public static final int YOUR_SKILL_HAS_BEEN_CANCELED_DUE_TO_LACK_OF_HP = 610; // Ð�ÐµÐ´Ð¾Ñ�Ñ‚Ð°Ñ‚Ð¾Ñ‡Ð½Ð¾
    // HP.
    // Ð£Ð¼ÐµÐ½Ð¸Ðµ
    // Ð¾Ñ‚Ð¼ÐµÐ½ÐµÐ½Ð¾.
    public static final int YOU_HAVE_SUCCEEDED_IN_CONFUSING_THE_ENEMY = 611; // Ð’Ñ‹
    // ÑƒÑ�Ð¿ÐµÑˆÐ½Ð¾
    // Ð²Ð²ÐµÐ»Ð¸
    // Ð¿Ñ€Ð¾Ñ‚Ð¸Ð²Ð½Ð¸ÐºÐ°
    // Ð²
    // Ð·Ð°Ð¼ÐµÑˆÐ°Ñ‚ÐµÐ»ÑŒÑ�Ñ‚Ð²Ð¾.
    public static final int THE_SPOIL_CONDITION_HAS_BEEN_ACTIVATED = 612; // Ð�ÐºÑ‚Ð¸Ð²Ð¸Ñ€Ð¾Ð²Ð°Ð½Ð¾
    // ÑƒÐ¼ÐµÐ½Ð¸Ðµ
    // Â«SpoilÂ».
    public static final int _IGNORE_LIST_ = 613; // ======<IGNORE_LIST>======
    public static final int C1___C2 = 614; // $c1 : $c2
    public static final int S1 = 1983; // $s1
    public static final int YOU_HAVE_FAILED_TO_REGISTER_THE_USER_TO_YOUR_IGNORE_LIST = 615; // ÐžÑˆÐ¸Ð±ÐºÐ°
    // Ð¿Ñ€Ð¸
    // Ð´Ð¾Ð±Ð°Ð²Ð»ÐµÐ½Ð¸Ð¸
    // Ð²
    // Ð¸Ð³Ð½Ð¾Ñ€-Ð»Ð¸Ñ�Ñ‚.
    public static final int YOU_HAVE_FAILED_TO_DELETE_THE_CHARACTER_FROM_IGNORE_LIST = 616; // ÐžÑˆÐ¸Ð±ÐºÐ°
    // Ð¿Ñ€Ð¸
    // ÑƒÐ´Ð°Ð»ÐµÐ½Ð¸Ð¸
    // Ð¸Ð·
    // Ð¸Ð³Ð½Ð¾Ñ€-Ð»Ð¸Ñ�Ñ‚Ð°.
    public static final int S1_HAS_BEEN_ADDED_TO_YOUR_IGNORE_LIST = 617; // $s1
    // Ð´Ð¾Ð±Ð°Ð²Ð»Ñ�ÐµÑ‚Ñ�Ñ�
    // Ð²
    // Ð¸Ð³Ð½Ð¾Ñ€-Ð»Ð¸Ñ�Ñ‚.
    public static final int S1_HAS_BEEN_REMOVED_FROM_YOUR_IGNORE_LIST = 618; // $s1
    // ÑƒÐ´Ð°Ð»Ñ�ÐµÑ‚Ñ�Ñ�
    // Ð¸Ð·
    // Ð¸Ð³Ð½Ð¾Ñ€-Ð»Ð¸Ñ�Ñ‚Ð°.
    public static final int S1__HAS_PLACED_YOU_ON_HIS_HER_IGNORE_LIST = 619; // $s1
    // Ð´Ð¾Ð±Ð°Ð²Ð»Ñ�ÐµÑ‚
    // Ð’Ð°Ñ�
    // Ð²
    // Ð¸Ð³Ð½Ð¾Ñ€-Ð»Ð¸Ñ�Ñ‚.
    public static final int S1__HAS_PLACED_YOU_ON_HIS_HER_IGNORE_LIST_1 = 620; // $s1
    // Ð´Ð¾Ð±Ð°Ð²Ð»Ñ�ÐµÑ‚
    // Ð’Ð°Ñ�
    // Ð²
    // Ð¸Ð³Ð½Ð¾Ñ€-Ð»Ð¸Ñ�Ñ‚.
    public static final int THIS_SERVER_IS_RESERVED_FOR_PLAYERS_IN_KOREA__TO_USE_LINEAGE_II_GAME_SERVICES_PLEASE_CONNECT_TO_THE_SERVER_IN_YOUR_REGION = 621; // Ð’Ñ‹
    // Ð·Ð°Ñ…Ð¾Ð´Ð¸Ñ‚Ðµ
    // Ð²
    // Ð¸Ð³Ñ€Ñƒ
    // Ñ�
    // Ð·Ð°Ð¿Ñ€ÐµÑ‰ÐµÐ½Ð½Ð¾Ð³Ð¾
    // IP.
    public static final int YOU_MAY_NOT_MAKE_A_DECLARATION_OF_WAR_DURING_AN_ALLIANCE_BATTLE = 622; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ð¿Ð¾Ñ�Ð»Ð°Ñ‚ÑŒ
    // Ñ�Ð¾Ð¾Ð±Ñ‰ÐµÐ½Ð¸Ðµ
    // Ð¾Ð±
    // Ð¾Ð±ÑŠÑ�Ð²Ð»ÐµÐ½Ð¸Ð¸
    // Ð²Ð¾Ð¹Ð½Ñ‹
    // Ð²Ð¾
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ð²Ð¾Ð¹Ð½Ñ‹
    // Ð¼ÐµÐ¶Ð´Ñƒ
    // ÐºÐ»Ð°Ð½Ð°Ð¼Ð¸.
    public static final int YOUR_OPPONENT_HAS_EXCEEDED_THE_NUMBER_OF_SIMULTANEOUS_ALLIANCE_BATTLES_ALLOWED = 623; // ÐŸÑ€Ð¾Ñ‚Ð¸Ð²Ð½Ð¸Ðº
    // Ð²Ð¾ÑŽÐµÑ‚
    // Ñ�
    // Ð¼Ð°ÐºÑ�Ð¸Ð¼Ð°Ð»ÑŒÐ½Ð¾
    // Ð´Ð¾Ð¿ÑƒÑ‰ÐµÐ½Ð½Ñ‹Ð¼
    // ÐºÐ¾Ð»Ð¸Ñ‡ÐµÑ�Ñ‚Ð²Ð¾Ð¼
    // ÐºÐ»Ð°Ð½Ð¾Ð².
    public static final int S1_CLAN_LEADER_IS_NOT_CURRENTLY_CONNECTED_TO_THE_GAME_SERVER = 624; // Ð›Ð¸Ð´ÐµÑ€
    // ÐºÐ»Ð°Ð½Ð°
    // $s1
    // Ñ�ÐµÐ¹Ñ‡Ð°Ñ�
    // Ð½Ðµ
    // Ð²
    // Ð¸Ð³Ñ€Ðµ.
    public static final int YOUR_REQUEST_FOR_ALLIANCE_BATTLE_TRUCE_HAS_BEEN_DENIED = 625; // ÐŸÐ¾Ñ�Ñ‚ÑƒÐ¿Ð¸Ð»
    // Ð¾Ñ‚ÐºÐ°Ð·
    // Ð¾Ñ‚
    // Ð·Ð°Ð²ÐµÑ€ÑˆÐµÐ½Ð¸Ñ�
    // Ð²Ð¾Ð¹Ð½Ñ‹
    // Ð¼ÐµÐ¶Ð´Ñƒ
    // ÐºÐ»Ð°Ð½Ð°Ð¼Ð¸.
    public static final int THE_S1_CLAN_DID_NOT_RESPOND__WAR_PROCLAMATION_HAS_BEEN_REFUSED = 626; // ÐšÐ»Ð°Ð½
    // $s1
    // Ð½Ðµ
    // Ð¾Ñ‚Ð²ÐµÑ‚Ð¸Ð»
    // Ð½Ð°
    // Ð¾Ð±ÑŠÑ�Ð²Ð»ÐµÐ½Ð¸Ðµ
    // Ð²Ð¾Ð¹Ð½Ñ‹.
    public static final int CLAN_BATTLE_HAS_BEEN_REFUSED_BECAUSE_YOU_DID_NOT_RESPOND_TO_S1_CLANS_WAR_PROCLAMATION = 627; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¾Ñ‚Ð²ÐµÑ‚Ð¸Ð»Ð¸
    // ÐºÐ»Ð°Ð½Ñƒ
    // $s1
    // Ð½Ð°
    // Ñ�Ð¾Ð¾Ð±Ñ‰ÐµÐ½Ð¸Ðµ
    // Ð¾Ð±
    // Ð¾Ð±ÑŠÑ�Ð²Ð»ÐµÐ½Ð¸Ð¸
    // Ð²Ð¾Ð¹Ð½Ñ‹.
    public static final int YOU_HAVE_ALREADY_BEEN_AT_WAR_WITH_THE_S1_CLAN_5_DAYS_MUST_PASS_BEFORE_YOU_CAN_DECLARE_WAR_AGAIN = 628; // Ð’Ñ‹
    // ÑƒÐ¶Ðµ
    // Ð²Ð¾ÐµÐ²Ð°Ð»Ð¸
    // Ñ�
    // ÐºÐ»Ð°Ð½Ð¾Ð¼
    // $s1.
    // ÐŸÐ¾Ð²Ñ‚Ð¾Ñ€Ð½Ð¾
    // Ð¾Ð±ÑŠÑ�Ð²Ð¸Ñ‚ÑŒ
    // Ð²Ð¾Ð¹Ð½Ñƒ
    // Ð¼Ð¾Ð¶Ð½Ð¾
    // Ñ‡ÐµÑ€ÐµÐ·
    // 5
    // Ð´Ð½ÐµÐ¹
    // Ð¿Ð¾Ñ�Ð»Ðµ
    // Ð¾ÐºÐ¾Ð½Ñ‡Ð°Ð½Ð¸Ñ�
    // Ð¿Ñ€ÐµÐ´Ñ‹Ð´ÑƒÑ‰ÐµÐ¹.
    public static final int YOUR_OPPONENT_HAS_EXCEEDED_THE_NUMBER_OF_SIMULTANEOUS_ALLIANCE_BATTLES_ALLOWED_1 = 629; // ÐŸÑ€Ð¾Ñ‚Ð¸Ð²Ð½Ð¸Ðº
    // ÑƒÐ¶Ðµ
    // Ð½Ð°Ñ…Ð¾Ð´Ð¸Ñ‚Ñ�Ñ�
    // Ð²
    // Ñ�Ð¾Ñ�Ñ‚Ð¾Ñ�Ð½Ð¸Ð¸
    // Ð²Ð¾Ð¹Ð½Ñ‹
    // Ñ�
    // Ð¼Ð°ÐºÑ�Ð¸Ð¼Ð°Ð»ÑŒÐ½Ñ‹Ð¼
    // ÐºÐ¾Ð»Ð¸Ñ‡ÐµÑ�Ñ‚Ð²Ð¾Ð¼
    // ÐºÐ»Ð°Ð½Ð¾Ð².
    public static final int WAR_WITH_THE_S1_CLAN_HAS_BEGUN_1 = 630; // Ð’Ð¾Ð¹Ð½Ð° Ñ�
    // ÐºÐ»Ð°Ð½Ð¾Ð¼
    // $s1
    // Ð½Ð°Ñ‡Ð°Ð»Ð°Ñ�ÑŒ.
    public static final int WAR_WITH_THE_S1_CLAN_IS_OVER = 631; // Ð’Ð¾Ð¹Ð½Ð° Ñ�
    // ÐºÐ»Ð°Ð½Ð¾Ð¼ $s1
    // Ð·Ð°Ð²ÐµÑ€ÑˆÐµÐ½Ð°.
    public static final int YOU_HAVE_WON_THE_WAR_OVER_THE_S1_CLAN_1 = 632; // Ð’Ñ‹
    // Ð¿Ð¾Ð±ÐµÐ´Ð¸Ð»Ð¸
    // Ð²
    // Ð²Ð¾Ð¹Ð½Ðµ
    // Ñ�
    // ÐºÐ»Ð°Ð½Ð¾Ð¼
    // $s1.
    public static final int YOU_HAVE_SURRENDERED_TO_THE_S1_CLAN_1 = 633; // Ð’Ñ‹
    // Ð¿Ñ€Ð¾Ð¸Ð³Ñ€Ð°Ð»Ð¸
    // Ð²
    // Ð²Ð¾Ð¹Ð½Ðµ
    // Ñ�
    // ÐºÐ»Ð°Ð½Ð¾Ð¼
    // $s1.
    public static final int YOUR_ALLIANCE_LEADER_HAS_BEEN_SLAIN_YOU_HAVE_BEEN_DEFEATED_BY_THE_S1_CLAN = 634; // Ð›Ð¸Ð´ÐµÑ€
    // ÐºÐ»Ð°Ð½Ð°
    // Ð¿Ð¾Ð³Ð¸Ð±.
    // Ð’Ñ‹
    // Ð¿Ñ€Ð¾Ð¸Ð³Ñ€Ð°Ð»Ð¸
    // ÐºÐ»Ð°Ð½Ñƒ
    // $s1.
    public static final int THE_TIME_LIMIT_FOR_THE_CLAN_WAR_HAS_BEEN_EXCEEDED_WAR_WITH_THE_S1_CLAN_IS_OVER = 635; // Ð’Ñ€ÐµÐ¼Ñ�
    // Ð²Ð¾Ð¹Ð½Ñ‹
    // ÐºÐ»Ð°Ð½Ð¾Ð²
    // Ð¸Ñ�Ñ‚ÐµÐºÐ»Ð¾.
    // Ð’Ð¾Ð¹Ð½Ð°
    // Ñ�
    // ÐºÐ»Ð°Ð½Ð¾Ð¼
    // $s1
    // Ð·Ð°Ð²ÐµÑ€ÑˆÐµÐ½Ð°.
    public static final int YOU_ARE_NOT_INVOLVED_IN_A_CLAN_WAR_1 = 636; // Ð’Ñ‹ Ð½Ðµ
    // Ð²Ð¾Ð²Ð»ÐµÑ‡ÐµÐ½Ñ‹
    // Ð²
    // Ð²Ð¾Ð¹Ð½Ñƒ
    // ÐºÐ»Ð°Ð½Ð¾Ð².
    public static final int A_CLAN_ALLY_HAS_REGISTERED_ITSELF_TO_THE_OPPONENT = 637; // ÐšÐ»Ð°Ð½,
    // Ð²Ñ…Ð¾Ð´Ñ�Ñ‰Ð¸Ð¹
    // Ð²
    // Ñ�Ð¾Ñ�Ñ‚Ð°Ð²
    // Ð’Ð°ÑˆÐµÐ³Ð¾
    // Ð°Ð»ÑŒÑ�Ð½Ñ�Ð°,
    // Ð·Ð°Ñ€ÐµÐ³Ð¸Ñ�Ñ‚Ñ€Ð¸Ñ€Ð¾Ð²Ð°Ð»Ñ�Ñ�
    // Ð’Ð°ÑˆÐ¸Ð¼
    // Ð¾Ð¿Ð¿Ð¾Ð½ÐµÐ½Ñ‚Ð¾Ð¼.
    public static final int YOU_HAVE_ALREADY_REQUESTED_A_SIEGE_BATTLE = 638; // Ð’Ñ‹
    // ÑƒÐ¶Ðµ
    // Ð¿Ð¾Ð´Ð°Ð»Ð¸
    // Ð·Ð°Ñ�Ð²ÐºÑƒ
    // Ð½Ð°
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð¸Ðµ
    // Ð²
    // Ð¾Ñ�Ð°Ð´Ðµ
    // ÐºÑ€ÐµÐ¿Ð¾Ñ�Ñ‚Ð¸.
    public static final int YOUR_APPLICATION_HAS_BEEN_DENIED_BECAUSE_YOU_HAVE_ALREADY_SUBMITTED_A_REQUEST_FOR_ANOTHER_SIEGE_BATTLE = 639; // Ð’Ñ‹
    // ÑƒÐ¶Ðµ
    // Ð¿Ð¾Ð´Ð°Ð»Ð¸
    // Ð·Ð°Ñ�Ð²ÐºÑƒ
    // Ð½Ð°
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð¸Ðµ
    // Ð²
    // Ð´Ñ€ÑƒÐ³Ð¾Ð¹
    // Ð¾Ñ�Ð°Ð´Ðµ
    // ÐºÑ€ÐµÐ¿Ð¾Ñ�Ñ‚Ð¸.
    // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ð¿Ð¾Ð´Ð°Ñ‚ÑŒ
    // Ð¿Ð¾Ð²Ñ‚Ð¾Ñ€Ð½ÑƒÑŽ
    // Ð·Ð°Ñ�Ð²ÐºÑƒ.
    public static final int YOU_HAVE_FAILED_TO_REFUSE_CASTLE_DEFENSE_AID = 640; // Ð’Ñ‹
    // Ð½Ðµ
    // Ñ�Ð¼Ð¾Ð³Ð»Ð¸
    // Ð¾Ñ‚ÐºÐ°Ð·Ð°Ñ‚ÑŒÑ�Ñ�
    // Ð¾Ñ‚
    // Ð·Ð°Ñ‰Ð¸Ñ‚Ñ‹
    // ÐºÑ€ÐµÐ¿Ð¾Ñ�Ñ‚Ð¸.
    public static final int YOU_HAVE_FAILED_TO_APPROVE_CASTLE_DEFENSE_AID = 641; // Ð’Ñ‹
    // Ð½Ðµ
    // Ñ�Ð¼Ð¾Ð³Ð»Ð¸
    // Ð¿Ð¾Ð´Ñ‚Ð²ÐµÑ€Ð´Ð¸Ñ‚ÑŒ
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð¸Ðµ
    // Ð²
    // Ð·Ð°Ñ‰Ð¸Ñ‚Ðµ
    // ÐºÑ€ÐµÐ¿Ð¾Ñ�Ñ‚Ð¸.
    public static final int YOU_ARE_ALREADY_REGISTERED_TO_THE_ATTACKER_SIDE_AND_MUST_CANCEL_YOUR_REGISTRATION_BEFORE_SUBMITTING_YOUR_REQUEST = 642; // Ð’Ñ‹
    // Ð¿Ð¾Ð´Ð°Ð»Ð¸
    // Ð·Ð°Ñ�Ð²ÐºÑƒ
    // Ð½Ð°
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð¸Ðµ
    // Ð²
    // Ð°Ñ‚Ð°ÐºÐµ
    // ÐºÑ€ÐµÐ¿Ð¾Ñ�Ñ‚Ð¸.
    // Ð�ÐµÐ¾Ð±Ñ…Ð¾Ð´Ð¸Ð¼Ð¾
    // Ð¾Ñ‚Ð¼ÐµÐ½Ð¸Ñ‚ÑŒ
    // ÐµÐµ
    // Ð¸
    // Ð¿Ð¾Ð²Ñ‚Ð¾Ñ€Ð¸Ñ‚ÑŒ
    // Ð¿Ð¾Ð¿Ñ‹Ñ‚ÐºÑƒ.
    public static final int YOU_HAVE_ALREADY_REGISTERED_TO_THE_DEFENDER_SIDE_AND_MUST_CANCEL_YOUR_REGISTRATION_BEFORE_SUBMITTING_YOUR_REQUEST = 643; // Ð’Ñ‹
    // Ð¿Ð¾Ð´Ð°Ð»Ð¸
    // Ð·Ð°Ñ�Ð²ÐºÑƒ
    // Ð½Ð°
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð¸Ðµ
    // Ð²
    // Ð¾Ð±Ð¾Ñ€Ð¾Ð½Ðµ
    // ÐºÑ€ÐµÐ¿Ð¾Ñ�Ñ‚Ð¸.
    // Ð�ÐµÐ¾Ð±Ñ…Ð¾Ð´Ð¸Ð¼Ð¾
    // Ð¾Ñ‚Ð¼ÐµÐ½Ð¸Ñ‚ÑŒ
    // ÐµÐµ
    // Ð¸
    // Ð¿Ð¾Ð²Ñ‚Ð¾Ñ€Ð¸Ñ‚ÑŒ
    // Ð¿Ð¾Ð¿Ñ‹Ñ‚ÐºÑƒ.
    public static final int YOU_ARE_NOT_YET_REGISTERED_FOR_THE_CASTLE_SIEGE = 644; // Ð’Ñ‹
    // ÐµÑ‰Ðµ
    // Ð½Ðµ
    // Ð¿Ð¾Ð´Ð°Ð»Ð¸
    // Ð·Ð°Ñ�Ð²ÐºÑƒ
    // Ð½Ð°
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð¸Ðµ
    // Ð²
    // Ð¾Ñ�Ð°Ð´Ðµ
    // ÐºÑ€ÐµÐ¿Ð¾Ñ�Ñ‚Ð¸.
    public static final int ONLY_CLANS_WITH_LEVEL_4_AND_HIGHER_MAY_REGISTER_FOR_A_CASTLE_SIEGE = 645; // ÐŸÐ¾Ð´Ð°Ñ‚ÑŒ
    // Ð·Ð°Ñ�Ð²ÐºÑƒ
    // Ð½Ð°
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð¸Ðµ
    // Ð²
    // Ð¾Ñ�Ð°Ð´Ðµ
    // ÐºÑ€ÐµÐ¿Ð¾Ñ�Ñ‚Ð¸
    // Ð¼Ð¾Ð³ÑƒÑ‚
    // Ñ‚Ð¾Ð»ÑŒÐºÐ¾
    // ÐºÐ»Ð°Ð½Ñ‹,
    // Ð´Ð¾Ñ�Ñ‚Ð¸Ð³ÑˆÐ¸Ðµ
    // 5-Ð³Ð¾
    // ÑƒÑ€Ð¾Ð²Ð½Ñ�.
    public static final int YOU_DO_NOT_HAVE_THE_AUTHORITY_TO_MODIFY_THE_CASTLE_DEFENDER_LIST = 646; // Ð£
    // Ð’Ð°Ñ�
    // Ð½ÐµÑ‚
    // Ð¿Ñ€Ð°Ð²Ð°
    // Ð²Ð½Ð¾Ñ�Ð¸Ñ‚ÑŒ
    // Ð¸Ð·Ð¼ÐµÐ½ÐµÐ½Ð¸Ñ�
    // Ð²
    // Ñ�Ð¿Ð¸Ñ�Ð¾Ðº
    // Ð·Ð°Ñ‰Ð¸Ñ‚Ð½Ð¸ÐºÐ¾Ð²
    // ÐºÑ€ÐµÐ¿Ð¾Ñ�Ñ‚Ð¸.
    public static final int YOU_DO_NOT_HAVE_THE_AUTHORITY_TO_MODIFY_THE_SIEGE_TIME = 647; // Ð£
    // Ð’Ð°Ñ�
    // Ð½ÐµÑ‚
    // Ð¿Ñ€Ð°Ð²Ð°
    // Ð²Ñ‹Ð±Ñ€Ð°Ñ‚ÑŒ
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ð½Ð°Ñ‡Ð°Ð»Ð°
    // Ð°Ñ‚Ð°ÐºÐ¸
    // Ð½Ð°
    // ÐºÑ€ÐµÐ¿Ð¾Ñ�Ñ‚ÑŒ.
    public static final int NO_MORE_REGISTRATIONS_MAY_BE_ACCEPTED_FOR_THE_ATTACKER_SIDE = 648; // Ð—Ð°Ñ�Ð²ÐºÐ¸
    // Ð½Ð°
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð¸Ðµ
    // Ð²
    // Ð°Ñ‚Ð°ÐºÐµ
    // ÐºÑ€ÐµÐ¿Ð¾Ñ�Ñ‚Ð¸
    // Ð±Ð¾Ð»ÑŒÑˆÐµ
    // Ð½Ðµ
    // Ð¿Ñ€Ð¸Ð½Ð¸Ð¼Ð°ÑŽÑ‚Ñ�Ñ�.
    public static final int NO_MORE_REGISTRATIONS_MAY_BE_ACCEPTED_FOR_THE_DEFENDER_SIDE = 649; // Ð—Ð°Ñ�Ð²ÐºÐ¸
    // Ð½Ð°
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð¸Ðµ
    // Ð²
    // Ð¾Ð±Ð¾Ñ€Ð¾Ð½Ðµ
    // ÐºÑ€ÐµÐ¿Ð¾Ñ�Ñ‚Ð¸
    // Ð±Ð¾Ð»ÑŒÑˆÐµ
    // Ð½Ðµ
    // Ð¿Ñ€Ð¸Ð½Ð¸Ð¼Ð°ÑŽÑ‚Ñ�Ñ�.
    public static final int YOU_MAY_NOT_SUMMON_FROM_YOUR_CURRENT_LOCATION = 650; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ð¿Ñ€Ð¸Ð·Ñ‹Ð²Ð°Ñ‚ÑŒ
    // Ð²
    // Ð´Ð°Ð½Ð½Ð¾Ð¼
    // Ð¼ÐµÑ�Ñ‚Ðµ.
    public static final int PLACE_S1_IN_THE_CURRENT_LOCATION_AND_DIRECTION_DO_YOU_WISH_TO_CONTINUE = 651; // ÐŸÐ¾Ð¼ÐµÑ�Ñ‚Ð¸Ñ‚ÑŒ
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚
    // $s1
    // Ð²
    // Ð´Ð°Ð½Ð½Ð¾Ð¼
    // Ð¼ÐµÑ�Ñ‚Ðµ?
    public static final int THE_TARGET_OF_THE_SUMMONED_MONSTER_IS_WRONG = 652; // Ð¦ÐµÐ»ÑŒ
    // Ð²Ñ‹Ð·Ð²Ð°Ð½Ð½Ð¾Ð³Ð¾
    // Ñ�ÑƒÑ‰ÐµÑ�Ñ‚Ð²Ð°
    // Ð½ÐµÐ²ÐµÑ€Ð½Ð°.
    public static final int YOU_DO_NOT_HAVE_THE_AUTHORITY_TO_POSITION_MERCENARIES = 653; // Ð£
    // Ð’Ð°Ñ�
    // Ð½ÐµÑ‚
    // Ð¿Ñ€Ð°Ð²Ð°
    // ÑƒÑ�Ñ‚Ð°Ð½Ð°Ð²Ð»Ð¸Ð²Ð°Ñ‚ÑŒ
    // Ð½Ð°ÐµÐ¼Ð½Ð¸ÐºÐ¾Ð².
    public static final int YOU_DO_NOT_HAVE_THE_AUTHORITY_TO_CANCEL_MERCENARY_POSITIONING = 654; // Ð£
    // Ð’Ð°Ñ�
    // Ð½ÐµÑ‚
    // Ð¿Ñ€Ð°Ð²Ð°
    // Ð¸Ð·Ð¼ÐµÐ½Ð¸Ñ‚ÑŒ
    // Ñ€Ð°Ñ�Ð¿Ð¾Ð»Ð¾Ð¶ÐµÐ½Ð¸Ðµ
    // Ð½Ð°ÐµÐ¼Ð½Ð¸ÐºÐ¾Ð².
    public static final int MERCENARIES_CANNOT_BE_POSITIONED_HERE = 655; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // ÑƒÑ�Ñ‚Ð°Ð½Ð¾Ð²Ð¸Ñ‚ÑŒ
    // Ð½Ð°ÐµÐ¼Ð½Ð¸ÐºÐ°
    // Ð²
    // Ñ�Ñ‚Ð¾Ð¼
    // Ð¼ÐµÑ�Ñ‚Ðµ.
    public static final int THIS_MERCENARY_CANNOT_BE_POSITIONED_ANYMORE = 656; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // ÑƒÑ�Ñ‚Ð°Ð½Ð¾Ð²Ð¸Ñ‚ÑŒ
    // Ñ�Ñ‚Ð¾Ð³Ð¾
    // Ð½Ð°ÐµÐ¼Ð½Ð¸ÐºÐ°.
    public static final int POSITIONING_CANNOT_BE_DONE_HERE_BECAUSE_THE_DISTANCE_BETWEEN_MERCENARIES_IS_TOO_SHORT = 657; // Ð Ð°Ñ�Ñ�Ñ‚Ð¾Ñ�Ð½Ð¸Ðµ
    // Ð¼ÐµÐ¶Ð´Ñƒ
    // Ð½Ð°ÐµÐ¼Ð½Ð¸ÐºÐ°Ð¼Ð¸
    // Ñ�Ð»Ð¸ÑˆÐºÐ¾Ð¼
    // Ð¼Ð°Ð»Ð¾.
    public static final int THIS_IS_NOT_A_MERCENARY_OF_A_CASTLE_THAT_YOU_OWN_AND_SO_YOU_CANNOT_CANCEL_ITS_POSITIONING = 658; // Ð­Ñ‚Ð¾Ñ‚
    // Ð½Ð°ÐµÐ¼Ð½Ð¸Ðº
    // Ð½Ðµ
    // Ð¿Ñ€Ð¸Ð½Ð°Ð´Ð»ÐµÐ¶Ð¸Ñ‚
    // Ð’Ð°ÑˆÐµÐ¼Ñƒ
    // Ð·Ð°Ð¼ÐºÑƒ,
    // Ð¿Ð¾Ñ�Ñ‚Ð¾Ð¼Ñƒ
    // Ð½ÐµÐ»ÑŒÐ·Ñ�
    // Ð¸Ð·Ð¼ÐµÐ½Ð¸Ñ‚ÑŒ
    // ÐµÐ³Ð¾
    // Ð¼ÐµÑ�Ñ‚Ð¾Ð¿Ð¾Ð»Ð¾Ð¶ÐµÐ½Ð¸Ðµ.
    public static final int THIS_IS_NOT_THE_TIME_FOR_SIEGE_REGISTRATION_AND_SO_REGISTRATIONS_CANNOT_BE_ACCEPTED_OR_REJECTED = 659; // Ð¡ÐµÐ¹Ñ‡Ð°Ñ�
    // Ð½Ðµ
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ð´Ð»Ñ�
    // Ñ€ÐµÐ³Ð¸Ñ�Ñ‚Ñ€Ð°Ñ†Ð¸Ð¸
    // Ð½Ð°
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð¸Ðµ
    // Ð²
    // Ð¾Ñ�Ð°Ð´Ðµ
    // ÐºÑ€ÐµÐ¿Ð¾Ñ�Ñ‚Ð¸,
    // Ð¿Ð¾Ñ�Ñ‚Ð¾Ð¼Ñƒ
    // Ð¾Ð´Ð¾Ð±Ñ€Ð¸Ñ‚ÑŒ
    // Ð¸Ð»Ð¸
    // Ð¾Ñ‚Ð¼ÐµÐ½Ð¸Ñ‚ÑŒ
    // Ð·Ð°Ñ�Ð²ÐºÑƒ
    // Ð½ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾.
    public static final int THIS_IS_NOT_THE_TIME_FOR_SIEGE_REGISTRATION_AND_SO_REGISTRATION_AND_CANCELLATION_CANNOT_BE_DONE = 660; // Ð¡ÐµÐ¹Ñ‡Ð°Ñ�
    // Ð½Ðµ
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ð´Ð»Ñ�
    // Ñ€ÐµÐ³Ð¸Ñ�Ñ‚Ñ€Ð°Ñ†Ð¸Ð¸
    // Ð½Ð°
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð¸Ðµ
    // Ð²
    // Ð¾Ñ�Ð°Ð´Ðµ
    // ÐºÑ€ÐµÐ¿Ð¾Ñ�Ñ‚Ð¸,
    // Ð¿Ð¾Ñ�Ñ‚Ð¾Ð¼Ñƒ
    // Ð¿Ð¾Ð´Ð°Ñ‚ÑŒ
    // Ð¸Ð»Ð¸
    // Ð¾Ñ‚Ð¼ÐµÐ½Ð¸Ñ‚ÑŒ
    // Ð·Ð°Ñ�Ð²ÐºÑƒ
    // Ð½ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾.
    public static final int IT_IS_A_CHARACTER_THAT_CANNOT_BE_SPOILED = 661; // Ð­Ñ‚Ð¾Ñ‚
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð¾Ñ†ÐµÐ½ÐµÐ½.
    public static final int THE_OTHER_PLAYER_IS_REJECTING_FRIEND_INVITATIONS = 662; // Ð­Ñ‚Ð¾Ñ‚
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶
    // Ð¾Ñ‚ÐºÐ°Ð·Ð°Ð»Ñ�Ñ�
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð’Ð°ÑˆÐ¸Ð¼
    // Ð´Ñ€ÑƒÐ³Ð¾Ð¼.
    public static final int THE_SIEGE_TIME_HAS_BEEN_DECLARED_FOR_S_IT_IS_NOT_POSSIBLE_TO_CHANGE_THE_TIME_AFTER_A_SIEGE_TIME_HAS_BEEN_DECLARED_DO_YOU_WANT_TO_CONTINUE = 663; // Ð’Ñ€ÐµÐ¼Ñ�
    // Ð½Ð°Ñ‡Ð°Ð»Ð°
    // Ð¾Ñ�Ð°Ð´Ñ‹:
    // $s2.
    // ÐŸÐ¾Ñ�Ð»Ðµ
    // Ñ�Ñ‚Ð¾Ð³Ð¾
    // Ð²Ð½ÐµÑ�Ñ‚Ð¸
    // ÐºÐ°ÐºÐ¸Ðµ-Ð»Ð¸Ð±Ð¾
    // Ð¸Ð·Ð¼ÐµÐ½ÐµÐ½Ð¸Ñ�
    // Ð±ÑƒÐ´ÐµÑ‚
    // Ð½ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾.
    // ÐŸÑ€Ð¾Ð´Ð¾Ð»Ð¶Ð¸Ñ‚ÑŒ?
    public static final int PLEASE_CHOOSE_A_PERSON_TO_RECEIVE = 664; // Ð’Ñ‹Ð±ÐµÑ€Ð¸Ñ‚Ðµ
    // Ð¿Ð¾Ð»ÑƒÑ‡Ð°Ñ‚ÐµÐ»Ñ�.
    public static final int S2_OF_S1_ALLIANCE_IS_APPLYING_FOR_ALLIANCE_WAR_DO_YOU_WANT_TO_ACCEPT_THE_CHALLENGE = 665; // $s2,
    // Ð¿Ñ€ÐµÐ´Ñ�Ñ‚Ð°Ð²Ð¸Ñ‚ÐµÐ»ÑŒ
    // Ð°Ð»ÑŒÑ�Ð½Ñ�Ð°
    // $s1,
    // Ð¿Ñ€ÐµÐ´Ð»Ð°Ð³Ð°ÐµÑ‚
    // Ð²Ð¾Ð¹Ð½Ñƒ.
    // Ð¡Ð¾Ð³Ð»Ð°Ñ�Ð¸Ñ‚ÑŒÑ�Ñ�?
    public static final int A_REQUEST_FOR_CEASEFIRE_HAS_BEEN_RECEIVED_FROM_S1_ALLIANCE_DO_YOU_AGREE = 666; // Ð�Ð»ÑŒÑ�Ð½Ñ�
    // $s1
    // Ð¿Ñ€Ð¾Ñ�Ð¸Ñ‚
    // Ð¾
    // Ð·Ð°Ð²ÐµÑ€ÑˆÐµÐ½Ð¸Ð¸
    // Ð²Ð¾Ð¹Ð½Ñ‹.
    // Ð’Ñ‹
    // Ñ�Ð¾Ð³Ð»Ð°Ñ�Ð½Ñ‹?
    public static final int YOU_ARE_REGISTERING_ON_THE_ATTACKING_SIDE_OF_THE_S1_SIEGE_DO_YOU_WANT_TO_CONTINUE = 667; // Ð’Ð°Ñ�
    // Ð´Ð¾Ð±Ð°Ð²Ð»Ñ�ÑŽÑ‚
    // Ð²
    // Ñ�Ð¿Ð¸Ñ�Ð¾Ðº
    // Ð°Ñ‚Ð°ÐºÑƒÑŽÑ‰Ð¸Ñ…
    // Ð²
    // Ð¾Ñ�Ð°Ð´Ðµ
    // ÐºÑ€ÐµÐ¿Ð¾Ñ�Ñ‚Ð¸
    // $s1.
    // ÐŸÑ€Ð¾Ð´Ð¾Ð»Ð¶Ð¸Ñ‚ÑŒ?
    public static final int YOU_ARE_REGISTERING_ON_THE_DEFENDING_SIDE_OF_THE_S1_SIEGE_DO_YOU_WANT_TO_CONTINUE = 668; // Ð’Ð°Ñ�
    // Ð´Ð¾Ð±Ð°Ð²Ð»Ñ�ÑŽÑ‚
    // Ð²
    // Ñ�Ð¿Ð¸Ñ�Ð¾Ðº
    // Ð·Ð°Ñ‰Ð¸Ñ‚Ð½Ð¸ÐºÐ¾Ð²
    // Ð²
    // Ð¾Ñ�Ð°Ð´Ðµ
    // ÐºÑ€ÐµÐ¿Ð¾Ñ�Ñ‚Ð¸
    // $s1.
    // ÐŸÑ€Ð¾Ð´Ð¾Ð»Ð¶Ð¸Ñ‚ÑŒ?
    public static final int YOU_ARE_CANCELING_YOUR_APPLICATION_TO_PARTICIPATE_IN_THE_S1_SIEGE_BATTLE_DO_YOU_WANT_TO_CONTINUE = 669; // Ð—Ð°Ñ�Ð²ÐºÐ°
    // Ð½Ð°
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð¸Ðµ
    // Ð²
    // Ð¾Ñ�Ð°Ð´Ðµ
    // $s1
    // Ð±ÑƒÐ´ÐµÑ‚
    // Ð¾Ñ‚Ð¼ÐµÐ½ÐµÐ½Ð°.
    // ÐŸÑ€Ð¾Ð´Ð¾Ð»Ð¶Ð¸Ñ‚ÑŒ?
    public static final int YOU_ARE_REFUSING_THE_REGISTRATION_OF_S1_CLAN_ON_THE_DEFENDING_SIDE_DO_YOU_WANT_TO_CONTINUE = 670; // Ð—Ð°Ñ�Ð²ÐºÐ°
    // Ð½Ð°
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð¸Ðµ
    // Ð²
    // Ð·Ð°Ñ‰Ð¸Ñ‚Ðµ
    // ÐºÑ€ÐµÐ¿Ð¾Ñ�Ñ‚Ð¸
    // ÐºÐ»Ð°Ð½Ð°
    // $s1
    // Ð±ÑƒÐ´ÐµÑ‚
    // Ð¾Ñ‚ÐºÐ»Ð¾Ð½ÐµÐ½Ð°.
    // ÐŸÑ€Ð¾Ð´Ð¾Ð»Ð¶Ð¸Ñ‚ÑŒ?
    public static final int YOU_ARE_AGREEING_TO_THE_REGISTRATION_OF_S1_CLAN_ON_THE_DEFENDING_SIDE_DO_YOU_WANT_TO_CONTINUE = 671; // Ð—Ð°Ñ�Ð²ÐºÐ°
    // Ð½Ð°
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð¸Ðµ
    // Ð²
    // Ð·Ð°Ñ‰Ð¸Ñ‚Ðµ
    // ÐºÑ€ÐµÐ¿Ð¾Ñ�Ñ‚Ð¸
    // ÐºÐ»Ð°Ð½Ð°
    // $s1
    // Ð±ÑƒÐ´ÐµÑ‚
    // Ð¾Ð´Ð¾Ð±Ñ€ÐµÐ½Ð°.
    // ÐŸÑ€Ð¾Ð´Ð¾Ð»Ð¶Ð¸Ñ‚ÑŒ?
    public static final int S1_ADENA_DISAPPEARED = 672; // Ð˜Ñ�Ñ‡ÐµÐ·Ð»Ð¾: $s1 Ð°Ð´ÐµÐ½.
    public static final int YOU_ARE_MOVING_TO_ANOTHER_VILLAGE_DO_YOU_WANT_TO_CONTINUE = 682; // ÐŸÐµÑ€ÐµÐ¼ÐµÑ‰ÐµÐ½Ð¸Ðµ
    // Ð²
    // Ð´Ñ€ÑƒÐ³ÑƒÑŽ
    // Ð´ÐµÑ€ÐµÐ²Ð½ÑŽ.
    // ÐŸÑ€Ð¾Ð´Ð¾Ð»Ð¶Ð¸Ñ‚ÑŒ?
    public static final int THERE_ARE_NO_PRIORITY_RIGHTS_ON_A_SWEEPER = 683; // Ð£
    // Ð’Ð°Ñ�
    // Ð½ÐµÑ‚
    // Ð¿Ñ€Ð°Ð²
    // Ð½Ð°
    // Ð¿Ð¾Ð»ÑƒÑ‡ÐµÐ½Ð¸Ðµ
    // Ñ�Ñ‚Ð¾Ð³Ð¾
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ð°.
    public static final int YOU_CANNOT_POSITION_MERCENARIES_DURING_A_SIEGE = 684; // Ð’Ð¾
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ð¾Ñ�Ð°Ð´Ñ‹
    // ÐºÑ€ÐµÐ¿Ð¾Ñ�Ñ‚Ð¸
    // ÑƒÑ�Ñ‚Ð°Ð½Ð¾Ð²Ð¸Ñ‚ÑŒ
    // Ð½Ð°ÐµÐ¼Ð½Ð¸ÐºÐ¾Ð²
    // Ð½ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾.
    public static final int YOU_CANNOT_APPLY_FOR_CLAN_WAR_WITH_A_CLAN_THAT_BELONGS_TO_THE_SAME_ALLIANCE = 685; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¾Ð±ÑŠÑ�Ð²Ð¸Ñ‚ÑŒ
    // Ð²Ð¾Ð¹Ð½Ñƒ
    // ÐºÐ»Ð°Ð½Ñƒ,
    // Ð²Ñ…Ð¾Ð´Ñ�Ñ‰ÐµÐ¼Ñƒ
    // Ð²
    // Ð’Ð°Ñˆ
    // Ð°Ð»ÑŒÑ�Ð½Ñ�.
    public static final int YOU_HAVE_RECEIVED_S1_DAMAGE_FROM_THE_FIRE_OF_MAGIC = 686; // Ð’Ñ‹
    // Ð¿Ð¾Ð»ÑƒÑ‡Ð¸Ð»Ð¸
    // $s1
    // ÑƒÑ€Ð¾Ð½Ð°
    // Ð¾Ñ‚
    // Ð¼Ð°Ð³Ð¸Ð¸.
    public static final int YOU_CANNOT_MOVE_IN_A_FROZEN_STATE_PLEASE_WAIT_A_MOMENT = 687; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¿ÐµÑ€ÐµÐ´Ð²Ð¸Ð³Ð°Ñ‚ÑŒÑ�Ñ�
    // Ð²
    // Ð·Ð°Ð¼ÐµÑ€Ð·ÑˆÐµÐ¼
    // Ñ�Ð¾Ñ�Ñ‚Ð¾Ñ�Ð½Ð¸Ð¸.
    // ÐŸÐ¾Ð´Ð¾Ð¶Ð´Ð¸Ñ‚Ðµ
    // Ð½ÐµÐ¼Ð½Ð¾Ð³Ð¾.
    public static final int THE_CLAN_THAT_OWNS_THE_CASTLE_IS_AUTOMATICALLY_REGISTERED_ON_THE_DEFENDING_SIDE = 688; // ÐšÐ»Ð°Ð½,
    // Ð²Ð»Ð°Ð´ÐµÑŽÑ‰Ð¸Ð¹
    // Ð·Ð°Ð¼ÐºÐ¾Ð¼,
    // Ð°Ð²Ñ‚Ð¾Ð¼Ð°Ñ‚Ð¸Ñ‡ÐµÑ�ÐºÐ¸
    // Ð·Ð°Ð½Ð¾Ñ�Ð¸Ñ‚Ñ�Ñ�
    // Ð²
    // Ñ�Ð¿Ð¸Ñ�Ð¾Ðº
    // Ð·Ð°Ñ‰Ð¸Ñ‚Ð½Ð¸ÐºÐ¾Ð².
    public static final int A_CLAN_THAT_OWNS_A_CASTLE_CANNOT_PARTICIPATE_IN_ANOTHER_SIEGE = 689; // ÐšÐ»Ð°Ð½,
    // Ð²Ð»Ð°Ð´ÐµÑŽÑ‰Ð¸Ð¹
    // Ð·Ð°Ð¼ÐºÐ¾Ð¼,
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð²Ð¾Ð²Ð°Ñ‚ÑŒ
    // Ð²
    // Ð´Ñ€ÑƒÐ³Ð¸Ñ…
    // Ð¾Ñ�Ð°Ð´Ð°Ñ….
    public static final int YOU_CANNOT_REGISTER_ON_THE_ATTACKING_SIDE_BECAUSE_YOU_ARE_PART_OF_AN_ALLIANCE_WITH_THE_CLAN_THAT_OWNS_THE_CASTLE = 690; // Ð¢Ð°Ðº
    // ÐºÐ°Ðº
    // Ð’Ñ‹
    // Ñ�Ð¾Ñ�Ñ‚Ð¾Ð¸Ñ‚Ðµ
    // Ð²
    // Ð¾Ð´Ð½Ð¾Ð¼
    // Ð°Ð»ÑŒÑ�Ð½Ñ�Ðµ
    // Ñ�
    // ÐºÐ»Ð°Ð½Ð¾Ð¼,
    // Ð²Ð»Ð°Ð´ÐµÑŽÑ‰Ð¸Ð¼
    // ÐºÑ€ÐµÐ¿Ð¾Ñ�Ñ‚ÑŒÑŽ,
    // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð²Ð¾Ð¹Ñ‚Ð¸
    // Ð²
    // Ñ�Ð¾Ñ�Ñ‚Ð°Ð²
    // Ð¾Ñ�Ð°Ð¶Ð´Ð°ÑŽÑ‰Ð¸Ñ…
    // Ð·Ð°Ð¼Ð¾Ðº.
    public static final int S1_CLAN_IS_ALREADY_A_MEMBER_OF_S2_ALLIANCE = 691; // ÐšÐ»Ð°Ð½
    // $s1
    // ÑƒÐ¶Ðµ
    // Ñ�Ð¾Ñ�Ñ‚Ð¾Ð¸Ñ‚
    // Ð²
    // Ð°Ð»ÑŒÑ�Ð½Ñ�Ðµ
    // $s2.
    public static final int THE_OTHER_PARTY_IS_FROZEN_PLEASE_WAIT_A_MOMENT = 692; // Ð’Ð°Ñˆ
    // Ñ�Ð¾Ñ€Ð°Ñ‚Ð½Ð¸Ðº
    // Ð·Ð°Ð¼Ð¾Ñ€Ð¾Ð¶ÐµÐ½.
    // ÐŸÐ¾Ð´Ð¾Ð¶Ð´Ð¸Ñ‚Ðµ
    // Ð½ÐµÐ¼Ð½Ð¾Ð³Ð¾.
    public static final int THE_PACKAGE_THAT_ARRIVED_IS_IN_ANOTHER_WAREHOUSE = 693; // ÐŸÑ€Ð¸Ð±Ñ‹Ð²ÑˆÐ°Ñ�
    // Ð¿Ð¾Ñ�Ñ‹Ð»ÐºÐ°
    // Ð½Ð°Ñ…Ð¾Ð´Ð¸Ñ‚Ñ�Ñ�
    // Ð²
    // Ð´Ñ€ÑƒÐ³Ð¾Ð¼
    // Ñ…Ñ€Ð°Ð½Ð¸Ð»Ð¸Ñ‰Ðµ.
    public static final int NO_PACKAGES_HAVE_ARRIVED = 694; // ÐŸÐ¾Ñ�Ñ‹Ð»Ð¾Ðº Ð½Ðµ Ð±Ñ‹Ð»Ð¾.
    public static final int YOU_CANNOT_SET_THE_NAME_OF_THE_PET = 695; // Ð’Ñ‹ Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð´Ð°Ñ‚ÑŒ
    // Ð¸Ð¼Ñ�
    // Ð¿Ð¸Ñ‚Ð¾Ð¼Ñ†Ñƒ.
    public static final int YOUR_ACCOUNT_IS_RESTRICTED_FOR_NOT_PAYING_YOUR_PC_ROOM_USAGE_FEES = 696; // Ð’Ð°Ñˆ
    // Ð°ÐºÐºÐ°ÑƒÐ½Ñ‚
    // Ð±Ñ‹Ð»
    // Ð·Ð°Ð±Ð»Ð¾ÐºÐ¸Ñ€Ð¾Ð²Ð°Ð½
    // Ð·Ð°
    // Ð½ÐµÑƒÐ¿Ð»Ð°Ñ‚Ñƒ.
    public static final int THE_ITEM_ENCHANT_VALUE_IS_STRANGE = 697; // Ð§Ð¸Ñ�Ð»Ð¾
    // ÑƒÐ»ÑƒÑ‡ÑˆÐµÐ½Ð¸Ð¹
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ð°
    // Ð½ÐµÐ²ÐµÑ€Ð½Ð¾.
    public static final int THE_PRICE_IS_DIFFERENT_THAN_THE_SAME_ITEM_ON_THE_SALES_LIST = 698; // Ð¦ÐµÐ½Ð°
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ð°
    // Ð¾Ñ‚Ð»Ð¸Ñ‡Ð°ÐµÑ‚Ñ�Ñ�
    // Ð¾Ñ‚
    // Ñ‚Ð°ÐºÐ¾Ð³Ð¾
    // Ð¶Ðµ
    // Ð²
    // Ñ�Ð¿Ð¸Ñ�ÐºÐµ
    // Ð¿Ñ€Ð¾Ð´Ð°Ð¶.
    public static final int CURRENTLY_NOT_PURCHASING = 699; // Ð’ Ð´Ð°Ð½Ð½Ñ‹Ð¹ Ð¼Ð¾Ð¼ÐµÐ½Ñ‚
    // Ð¿Ð¾ÐºÑƒÐ¿ÐºÐ° Ð½Ðµ
    // Ð¿Ñ€Ð¾Ð¸Ð·Ð²Ð¾Ð´Ð¸Ñ‚Ñ�Ñ�.
    public static final int THE_PURCHASE_IS_COMPLETE = 700; // ÐŸÐ¾ÐºÑƒÐ¿ÐºÐ°
    // Ð·Ð°Ð²ÐµÑ€ÑˆÐµÐ½Ð°.
    public static final int YOU_DO_NOT_HAVE_ENOUGH_REQUIRED_ITEMS = 701; // Ð�ÐµÐ´Ð¾Ñ�Ñ‚Ð°Ñ‚Ð¾Ñ‡Ð½Ð¾
    // Ð½ÐµÐ¾Ð±Ñ…Ð¾Ð´Ð¸Ð¼Ñ‹Ñ…
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ð¾Ð².
    public static final int THERE_ARE_NOT_ANY_GMS_THAT_ARE_PROVIDING_CUSTOMER_SERVICE_CURRENTLY = 702; // Ð¡ÐµÐ¹Ñ‡Ð°Ñ�
    // Ð²
    // Ð¸Ð³Ñ€Ðµ
    // Ð½ÐµÑ‚
    // Ð˜Ð³Ñ€Ð¾Ð²Ð¾Ð³Ð¾
    // Ð¼Ð°Ñ�Ñ‚ÐµÑ€Ð°.
    public static final int _GM_LIST_ = 703; // ======<GM_LIST>======
    public static final int GM_S1 = 704; // Ð˜Ð³Ñ€Ð¾Ð²Ð¾Ð¹ Ð¼Ð°Ñ�Ñ‚ÐµÑ€: $c1
    public static final int YOU_CANNOT_EXCLUDE_YOURSELF = 705; // Ð’Ñ‹ Ð½Ðµ Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð·Ð°Ð±Ð»Ð¾ÐºÐ¸Ñ€Ð¾Ð²Ð°Ñ‚ÑŒ
    // Ñ�ÐµÐ±Ñ�.
    public static final int YOU_CAN_ONLY_REGISTER_UP_TO_64_NAMES_ON_YOUR_EXCLUDE_LIST = 706; // Ð’
    // Ñ�Ð¿Ð¸Ñ�Ð¾Ðº
    // Ð·Ð°Ð±Ð»Ð¾ÐºÐ¸Ñ€Ð¾Ð²Ð°Ð½Ð½Ñ‹Ñ…
    // Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÐµÐ»ÐµÐ¹
    // Ð¼Ð¾Ð¶Ð½Ð¾
    // Ð´Ð¾Ð±Ð°Ð²Ð¸Ñ‚ÑŒ
    // Ð¼Ð°ÐºÑ�Ð¸Ð¼ÑƒÐ¼
    // 64
    // Ñ‡ÐµÐ»Ð¾Ð²ÐµÐºÐ°.
    public static final int YOU_CANNOT_TELEPORT_TO_A_VILLAGE_THAT_IS_IN_A_SIEGE = 707; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ð¿ÐµÑ€ÐµÐ¼ÐµÑ�Ñ‚Ð¸Ñ‚ÑŒÑ�Ñ�
    // Ð²
    // Ð¾Ñ�Ð°Ð¶Ð´Ð°ÐµÐ¼ÑƒÑŽ
    // Ð´ÐµÑ€ÐµÐ²Ð½ÑŽ.
    public static final int YOU_DO_NOT_HAVE_THE_RIGHT_TO_USE_THE_CASTLE_WAREHOUSE = 708; // Ð£
    // Ð’Ð°Ñ�
    // Ð½ÐµÑ‚
    // Ð¿Ñ€Ð°Ð²Ð°
    // Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒÑ�Ñ�
    // Ñ…Ñ€Ð°Ð½Ð¸Ð»Ð¸Ñ‰ÐµÐ¼
    // Ð·Ð°Ð¼ÐºÐ°.
    public static final int YOU_DO_NOT_HAVE_THE_RIGHT_TO_USE_THE_CLAN_WAREHOUSE = 709; // Ð£
    // Ð’Ð°Ñ�
    // Ð½ÐµÑ‚
    // Ð¿Ñ€Ð°Ð²Ð°
    // Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒÑ�Ñ�
    // Ñ…Ñ€Ð°Ð½Ð¸Ð»Ð¸Ñ‰ÐµÐ¼
    // ÐºÐ»Ð°Ð½Ð°.
    public static final int ONLY_CLANS_OF_CLAN_LEVEL_1_OR_HIGHER_CAN_USE_A_CLAN_WAREHOUSE = 710; // Ð¥Ñ€Ð°Ð½Ð¸Ð»Ð¸Ñ‰ÐµÐ¼
    // ÐºÐ»Ð°Ð½Ð°
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒÑ�Ñ�
    // Ñ‚Ð¾Ð»ÑŒÐºÐ¾
    // ÐºÐ»Ð°Ð½,
    // ÑƒÑ€Ð¾Ð²ÐµÐ½ÑŒ
    // ÐºÐ¾Ñ‚Ð¾Ñ€Ð¾Ð³Ð¾
    // Ð¿Ñ€ÐµÐ²Ñ‹ÑˆÐ°ÐµÑ‚
    // 1.
    public static final int THE_SIEGE_OF_S1_HAS_STARTED = 711; // $s1: Ð¾Ñ�Ð°Ð´Ð°
    // Ð½Ð°Ñ‡Ð°Ð»Ð°Ñ�ÑŒ.
    public static final int THE_SIEGE_OF_S1_HAS_FINISHED = 712; // $s1: Ð¾Ñ�Ð°Ð´Ð°
    // Ð·Ð°Ð²ÐµÑ€ÑˆÐµÐ½Ð°.
    public static final int S1_S2_S3_S4S5 = 713; // $s1/$s2/$s3 $s4:$s5
    public static final int A_TRAP_DEVICE_HAS_TRIPPED = 714; // Ð›Ð¾Ð²ÑƒÑˆÐºÐ°
    // Ð°ÐºÑ‚Ð¸Ð²Ð¸Ñ€Ð¾Ð²Ð°Ð½Ð°.
    public static final int THE_TRAP_DEVICE_HAS_STOPPED = 715; // Ð”ÐµÐ¹Ñ�Ñ‚Ð²Ð¸Ðµ
    // Ð»Ð¾Ð²ÑƒÑˆÐºÐ¸ Ð±Ñ‹Ð»Ð¾
    // Ð¿Ñ€Ð¸Ð¾Ñ�Ñ‚Ð°Ð½Ð¾Ð²Ð»ÐµÐ½Ð¾.
    public static final int IF_A_BASE_CAMP_DOES_NOT_EXIST_RESURRECTION_IS_NOT_POSSIBLE = 716; // Ð’Ð¾Ñ�ÐºÑ€ÐµÑˆÐµÐ½Ð¸Ðµ
    // Ð½ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾,
    // ÐµÑ�Ð»Ð¸
    // Ð¾Ñ‚Ñ�ÑƒÑ‚Ñ�Ñ‚Ð²ÑƒÐµÑ‚
    // Ð±Ð°Ð·Ð°.
    public static final int THE_GUARDIAN_TOWER_HAS_BEEN_DESTROYED_AND_RESURRECTION_IS_NOT_POSSIBLE = 717; // Ð’Ð¾Ñ�ÐºÑ€ÐµÑˆÐµÐ½Ð¸Ðµ
    // Ð½ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾,
    // Ñ‚Ð°Ðº
    // ÐºÐ°Ðº
    // Ð·Ð°Ñ‰Ð¸Ñ‚Ð½Ð°Ñ�
    // Ð±Ð°ÑˆÐ½Ñ�
    // Ð±Ñ‹Ð»Ð°
    // Ñ€Ð°Ð·Ñ€ÑƒÑˆÐµÐ½Ð°.
    public static final int THE_CASTLE_GATES_CANNOT_BE_OPENED_AND_CLOSED_DURING_A_SIEGE = 718; // Ð’Ð¾
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ð¾Ñ�Ð°Ð´Ñ‹
    // Ð·Ð°Ð¼ÐºÐ°
    // Ð¾Ñ‚ÐºÑ€Ñ‹Ñ‚ÑŒ
    // Ð¸Ð»Ð¸
    // Ð·Ð°ÐºÑ€Ñ‹Ñ‚ÑŒ
    // Ð²Ñ€Ð°Ñ‚Ð°
    // Ð½ÐµÐ»ÑŒÐ·Ñ�.
    public static final int YOU_FAILED_AT_ITEM_MIXING = 719; // Ð£ Ð’Ð°Ñ� Ð½Ðµ
    // Ð¿Ð¾Ð»ÑƒÑ‡Ð¸Ð»Ð¾Ñ�ÑŒ
    // ÑƒÑ�Ð¸Ð»Ð¸Ñ‚ÑŒ Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚.
    public static final int THE_PURCHASE_PRICE_IS_HIGHER_THAN_THE_AMOUNT_OF_MONEY_THAT_YOU_HAVE_AND_SO_YOU_CANNOT_OPEN_A_PERSONAL_STORE = 720; // Ð¦ÐµÐ½Ð°
    // Ð¿Ð¾ÐºÑƒÐ¿ÐºÐ¸
    // Ð¿Ñ€ÐµÐ²Ñ‹ÑˆÐ°ÐµÑ‚
    // Ð’Ð°Ñˆ
    // ÐºÐ°Ð¿Ð¸Ñ‚Ð°Ð»,
    // Ð¿Ð¾Ñ�Ñ‚Ð¾Ð¼Ñƒ
    // Ð½Ð°Ñ‡Ð°Ñ‚ÑŒ
    // Ñ‚Ð¾Ñ€Ð³Ð¾Ð²Ð»ÑŽ
    // Ð½ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾.
    public static final int YOU_CANNOT_CREATE_AN_ALLIANCE_WHILE_PARTICIPATING_IN_A_SIEGE = 721; // Ð’Ð¾
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ð¾Ñ�Ð°Ð´Ñ‹
    // Ð·Ð°Ð¼ÐºÐ°
    // Ñ�Ð¾Ð·Ð´Ð°Ñ‚ÑŒ
    // Ð°Ð»ÑŒÑ�Ð½Ñ�
    // Ð½ÐµÐ»ÑŒÐ·Ñ�.
    public static final int YOU_CANNOT_DISSOLVE_AN_ALLIANCE_WHILE_AN_AFFILIATED_CLAN_IS_PARTICIPATING_IN_A_SIEGE_BATTLE = 722; // ÐžÐ´Ð¸Ð½
    // Ð¸Ð·
    // ÐºÐ»Ð°Ð½Ð¾Ð²,
    // Ð²Ñ…Ð¾Ð´Ñ�Ñ‰Ð¸Ñ…
    // Ð²
    // Ð°Ð»ÑŒÑ�Ð½Ñ�,
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð²ÑƒÐµÑ‚
    // Ð²
    // Ð¾Ñ�Ð°Ð´Ðµ
    // Ð·Ð°Ð¼ÐºÐ°,
    // Ð¿Ð¾Ñ�Ñ‚Ð¾Ð¼Ñƒ
    // Ñ€Ð°Ñ�Ð¿ÑƒÑ�Ñ‚Ð¸Ñ‚ÑŒ
    // Ð°Ð»ÑŒÑ�Ð½Ñ�
    // Ð½ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾.
    public static final int THE_OPPOSING_CLAN_IS_PARTICIPATING_IN_A_SIEGE_BATTLE = 723; // Ð”Ð°Ð½Ð½Ñ‹Ð¹
    // ÐºÐ»Ð°Ð½
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð²ÑƒÐµÑ‚
    // Ð²
    // Ð¾Ñ�Ð°Ð´Ðµ
    // Ð·Ð°Ð¼ÐºÐ°.
    public static final int YOU_CANNOT_LEAVE_WHILE_PARTICIPATING_IN_A_SIEGE_BATTLE = 724; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // ÑƒÐ¹Ñ‚Ð¸
    // Ð²Ð¾
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ð¾Ñ�Ð°Ð´Ñ‹
    // Ð·Ð°Ð¼ÐºÐ°.
    public static final int YOU_CANNOT_BANISH_A_CLAN_FROM_AN_ALLIANCE_WHILE_THE_CLAN_IS_PARTICIPATING_IN_A_SIEGE = 725; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ð¸Ð·Ð³Ð½Ð°Ñ‚ÑŒ
    // Ð¸Ð·
    // Ð°Ð»ÑŒÑ�Ð½Ñ�Ð°
    // ÐºÐ»Ð°Ð½,
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð²ÑƒÑŽÑ‰Ð¸Ð¹
    // Ð²
    // Ð¾Ñ�Ð°Ð´Ðµ
    // Ð·Ð°Ð¼ÐºÐ°.
    public static final int THE_FROZEN_CONDITION_HAS_STARTED_PLEASE_WAIT_A_MOMENT = 726; // Ð¡Ð¾Ñ�Ñ‚Ð¾Ñ�Ð½Ð¸Ðµ
    // Ð·Ð°Ð¼Ð¾Ñ€Ð¾Ð·ÐºÐ¸.
    // ÐŸÐ¾Ð´Ð¾Ð¶Ð´Ð¸Ñ‚Ðµ
    // Ð½ÐµÐ¼Ð½Ð¾Ð³Ð¾.
    public static final int THE_FROZEN_CONDITION_WAS_REMOVED = 727; // Ð—Ð°Ð¼Ð¾Ñ€Ð¾Ð¶ÐµÐ½Ð½Ð¾Ðµ
    // Ñ�Ð¾Ñ�Ñ‚Ð¾Ñ�Ð½Ð¸Ðµ
    // Ð·Ð°ÐºÐ¾Ð½Ñ‡Ð¸Ð»Ð¾Ñ�ÑŒ.
    public static final int YOU_CANNOT_APPLY_FOR_DISSOLUTION_AGAIN_WITHIN_SEVEN_DAYS_AFTER_A_PREVIOUS_APPLICATION_FOR_DISSOLUTION = 728; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ð¿Ð¾Ð´Ð°Ñ‚ÑŒ
    // Ð·Ð°Ñ�Ð²ÐºÑƒ
    // Ð½Ð°
    // Ñ€Ð¾Ñ�Ð¿ÑƒÑ�Ðº
    // Ð²
    // Ñ‚ÐµÑ‡ÐµÐ½Ð¸Ðµ
    // 7
    // Ð´Ð½ÐµÐ¹
    // Ð¿Ð¾Ñ�Ð»Ðµ
    // Ð¿Ñ€ÐµÐ´Ñ‹Ð´ÑƒÑ‰ÐµÐ¹.
    public static final int THAT_ITEM_CANNOT_BE_DISCARDED = 729; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ð²Ñ‹Ð±Ñ€Ð¾Ñ�Ð¸Ñ‚ÑŒ
    // Ð´Ð°Ð½Ð½Ñ‹Ð¹
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚.
    public static final int YOU_HAVE_SUBMITTED_S1_PETITIONS_YOU_MAY_SUBMIT_S2_MORE_PETITIONS_TODAY = 730; // ÐšÐ¾Ð»Ð¸Ñ‡ÐµÑ�Ñ‚Ð²Ð¾
    // Ð¾Ñ‚Ð¿Ñ€Ð°Ð²Ð»ÐµÐ½Ð½Ñ‹Ñ…
    // Ð¿ÐµÑ‚Ð¸Ñ†Ð¸Ð¹:
    // $s1.
    // \\n
    // -
    // ÐšÐ¾Ð»Ð¸Ñ‡ÐµÑ�Ñ‚Ð²Ð¾
    // Ð´Ð¾Ñ�Ñ‚ÑƒÐ¿Ð½Ñ‹Ñ…
    // Ñ�ÐµÐ³Ð¾Ð´Ð½Ñ�
    // Ð¿ÐµÑ‚Ð¸Ñ†Ð¸Ð¹:
    // $s2.
    public static final int A_PETITION_HAS_BEEN_RECEIVED_BY_THE_GM_ON_BEHALF_OF_S1_IT_IS_PETITION_S2 = 731; // ÐŸÐ¾Ð»ÑƒÑ‡ÐµÐ½
    // Ð¾Ñ‚Ð²ÐµÑ‚
    // Ð¾Ñ‚
    // Ñ�Ð»ÑƒÐ¶Ð±Ñ‹
    // Ð¿Ð¾Ð´Ð´ÐµÑ€Ð¶ÐºÐ¸
    // Ð½Ð°
    // Ð¸Ð¼Ñ�
    // $c1.
    // ÐŸÐµÑ‚Ð¸Ñ†Ð¸Ñ�
    // $s2.
    public static final int S1_HAS_RECEIVED_A_REQUEST_FOR_A_CONSULTATION_WITH_THE_GM = 732; // Ð˜Ð³Ñ€Ð¾Ð²Ð¾Ð¹
    // Ð¼Ð°Ñ�Ñ‚ÐµÑ€
    // Ð¿Ñ€Ð¾Ñ�Ð¸Ñ‚
    // Ð¾
    // Ð¿Ñ€Ð¸Ð²Ð°Ñ‚Ð½Ð¾Ð¹
    // Ð±ÐµÑ�ÐµÐ´Ðµ
    // $c1.
    public static final int WE_HAVE_RECEIVED_S1_PETITIONS_FROM_YOU_TODAY_AND_THAT_IS_THE_MAXIMUM_THAT_YOU_CAN_SUBMIT_IN_ONE_DAY_YOU_CANNOT_SUBMIT_ANY_MORE_PETITIONS = 733; // Ð¡ÐµÐ³Ð¾Ð´Ð½Ñ�
    // Ð’Ñ‹
    // Ð¾Ñ‚Ð¿Ñ€Ð°Ð²Ð¸Ð»Ð¸
    // Ð¼Ð°ÐºÑ�Ð¸Ð¼Ð°Ð»ÑŒÐ½Ð¾Ðµ
    // ÐºÐ¾Ð»Ð¸Ñ‡ÐµÑ�Ñ‚Ð²Ð¾
    // Ð¿ÐµÑ‚Ð¸Ñ†Ð¸Ð¹.
    // Ð‘Ð¾Ð»ÑŒÑˆÐµ
    // Ð¿ÐµÑ‚Ð¸Ñ†Ð¸Ð¹
    // Ð¾Ñ‚Ð¿Ñ€Ð°Ð²Ð¸Ñ‚ÑŒ
    // Ð½ÐµÐ»ÑŒÐ·Ñ�.
    public static final int YOU_FAILED_AT_SUBMITTING_A_PETITION_ON_BEHALF_OF_SOMEONE_ELSE_S1_ALREADY_SUBMITTED_A_PETITION = 734; // Ð�Ðµ
    // ÑƒÐ´Ð°Ð»Ð¾Ñ�ÑŒ
    // Ð¿Ð¾Ð´Ð°Ñ‚ÑŒ
    // Ð¿ÐµÑ‚Ð¸Ñ†Ð¸ÑŽ
    // Ð·Ð°
    // ÐºÐ¾Ð³Ð¾-Ñ‚Ð¾
    // Ð´Ñ€ÑƒÐ³Ð¾Ð³Ð¾.
    // $c1
    // ÑƒÐ¶Ðµ
    // Ð¾Ñ‚Ð¿Ñ€Ð°Ð²Ð¸Ð»
    // Ð¿ÐµÑ‚Ð¸Ñ†Ð¸ÑŽ.
    public static final int YOU_FAILED_AT_SUBMITTING_A_PETITION_ON_BEHALF_OF_S1_THE_ERROR_IS_S2 = 735; // Ð�Ðµ
    // ÑƒÐ´Ð°Ð»Ð¾Ñ�ÑŒ
    // Ð¿Ð¾Ð´Ð°Ñ‚ÑŒ
    // Ð¿ÐµÑ‚Ð¸Ñ†Ð¸ÑŽ
    // Ð·Ð°
    // Ð¸Ð³Ñ€Ð¾ÐºÐ°
    // $c1.
    // Ð�Ð¾Ð¼ÐµÑ€
    // Ð¾ÑˆÐ¸Ð±ÐºÐ¸:
    // $s2.
    public static final int THE_PETITION_WAS_CANCELED_YOU_MAY_SUBMIT_S1_MORE_PETITIONS_TODAY = 736; // Ð—Ð°Ñ�Ð²ÐºÐ°
    // Ð¾Ñ‚Ð¾Ð·Ð²Ð°Ð½Ð°.
    // Ð’Ñ‹
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¾Ð±Ñ€Ð°Ñ‚Ð¸Ñ‚ÑŒÑ�Ñ�
    // Ñ�ÐµÐ³Ð¾Ð´Ð½Ñ�
    // ÐµÑ‰Ðµ
    // $s1
    // Ñ€Ð°Ð·.
    public static final int YOU_FAILED_AT_SUBMITTING_A_PETITION_ON_BEHALF_OF_S1 = 737; // Ð’Ñ‹
    // Ð¾Ñ‚Ð¼ÐµÐ½Ð¸Ð»Ð¸
    // Ð¿Ð¾Ð´Ð°Ñ‡Ñƒ
    // Ð¿ÐµÑ‚Ð¸Ñ†Ð¸Ð¸
    // Ð·Ð°
    // Ð¸Ð³Ñ€Ð¾ÐºÐ°
    // $c1.
    public static final int YOU_HAVE_NOT_SUBMITTED_A_PETITION = 738; // Ð’Ñ‹ Ð½Ðµ
    // Ð¿Ð¾Ð´Ð°Ð»Ð¸
    // Ð¿ÐµÑ‚Ð¸Ñ†Ð¸ÑŽ.
    public static final int YOU_FAILED_AT_CANCELING_A_PETITION_ON_BEHALF_OF_S1_THE_ERROR_CODE_IS_S2 = 739; // Ð�Ðµ
    // ÑƒÐ´Ð°Ð»Ð¾Ñ�ÑŒ
    // Ð¾Ñ‚Ð¼ÐµÐ½Ð¸Ñ‚ÑŒ
    // Ð¿Ð¾Ð´Ð°Ñ‡Ñƒ
    // Ð¿ÐµÑ‚Ð¸Ñ†Ð¸Ð¸
    // Ð·Ð°
    // Ð¸Ð³Ñ€Ð¾ÐºÐ°
    // $c1.
    // ÐšÐ¾Ð´
    // Ð¾ÑˆÐ¸Ð±ÐºÐ¸:
    // $s2.
    public static final int S1_PARTICIPATED_IN_A_PETITION_CHAT_AT_THE_REQUEST_OF_THE_GM = 740; // $c1
    // Ð¿Ñ€Ð¸Ñ�Ð¾ÐµÐ´Ð¸Ð½Ð¸Ð»Ñ�Ñ�
    // Ðº
    // Ñ‡Ð°Ñ‚Ñƒ
    // Ð¿ÐµÑ‚Ð¸Ñ†Ð¸Ð¹
    // Ð¿Ð¾
    // Ð¿Ñ€Ð¾Ñ�ÑŒÐ±Ðµ
    // Ñ�Ð»ÑƒÐ¶Ð±Ñ‹
    // Ð¿Ð¾Ð´Ð´ÐµÑ€Ð¶ÐºÐ¸.
    public static final int YOU_FAILED_AT_ADDING_S1_TO_THE_PETITION_CHAT_A_PETITION_HAS_ALREADY_BEEN_SUBMITTED = 741; // Ð�Ðµ
    // ÑƒÐ´Ð°Ð»Ð¾Ñ�ÑŒ
    // Ð´Ð¾Ð±Ð°Ð²Ð¸Ñ‚ÑŒ
    // Ð¸Ð³Ñ€Ð¾ÐºÐ°
    // $c1
    // Ð²
    // Ñ‡Ð°Ñ‚
    // Ð¿ÐµÑ‚Ð¸Ñ†Ð¸Ð¹.
    // ÐŸÐµÑ‚Ð¸Ñ†Ð¸Ñ�
    // ÑƒÐ¶Ðµ
    // Ð¿Ð¾Ð´Ð°Ð½Ð°
    public static final int YOU_FAILED_AT_ADDING_S1_TO_THE_PETITION_CHAT_THE_ERROR_CODE_IS_S2 = 742; // Ð�Ðµ
    // ÑƒÐ´Ð°Ð»Ð¾Ñ�ÑŒ
    // Ð´Ð¾Ð±Ð°Ð²Ð¸Ñ‚ÑŒ
    // Ð¸Ð³Ñ€Ð¾ÐºÐ°
    // $c1
    // Ð²
    // Ñ‡Ð°Ñ‚
    // Ð¿ÐµÑ‚Ð¸Ñ†Ð¸Ð¹.
    // ÐšÐ¾Ð´
    // Ð¾ÑˆÐ¸Ð±ÐºÐ¸:
    // $s2.
    public static final int S1_LEFT_THE_PETITION_CHAT = 743; // $c1 Ð²Ñ‹Ñ…Ð¾Ð´Ð¸Ñ‚ Ð¸Ð·
    // Ñ‡Ð°Ñ‚Ð° Ð¿ÐµÑ‚Ð¸Ñ†Ð¸Ð¹.
    public static final int YOU_FAILED_AT_REMOVING_S1_FROM_THE_PETITION_CHAT_THE_ERROR_CODE_IS_S2 = 744; // Ð�Ðµ
    // ÑƒÐ´Ð°Ð»Ð¾Ñ�ÑŒ
    // ÑƒÐ´Ð°Ð»Ð¸Ñ‚ÑŒ
    // Ð¸Ð³Ñ€Ð¾ÐºÐ°
    // $s1
    // Ð¸Ð·
    // Ñ‡Ð°Ñ‚Ð°
    // Ð¿ÐµÑ‚Ð¸Ñ†Ð¸Ð¹.
    // ÐšÐ¾Ð´
    // Ð¾ÑˆÐ¸Ð±ÐºÐ¸:
    // $s2.
    public static final int YOU_ARE_CURRENTLY_NOT_IN_A_PETITION_CHAT = 745; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð½Ð°Ñ…Ð¾Ð´Ð¸Ñ‚ÐµÑ�ÑŒ
    // Ð²
    // Ñ‡Ð°Ñ‚Ðµ
    // Ð¿ÐµÑ‚Ð¸Ñ†Ð¸Ð¹.
    public static final int IT_IS_NOT_CURRENTLY_A_PETITION = 746; // Ð­Ñ‚Ð¾ Ð½Ðµ
    // Ð¿ÐµÑ‚Ð¸Ñ†Ð¸Ñ�.
    public static final int IF_YOU_NEED_HELP_PLEASE_USE_11_INQUIRY_ON_THE_OFFICIAL_WEB_SITE = 747; // Ð•Ñ�Ð»Ð¸
    // Ð’Ð°Ð¼
    // Ð½ÐµÐ¾Ð±Ñ…Ð¾Ð´Ð¸Ð¼Ð°
    // Ð¿Ð¾Ð¼Ð¾Ñ‰ÑŒ,
    // Ð¾Ð±Ñ€Ð°Ñ‚Ð¸Ñ‚ÐµÑ�ÑŒ
    // Ð²
    // Ñ�Ð»ÑƒÐ¶Ð±Ñƒ
    // Ð¿Ð¾Ð´Ð´ÐµÑ€Ð¶ÐºÐ¸
    // Ð½Ð°
    // Ð½Ð°ÑˆÐµÐ¼
    // Ñ�Ð°Ð¹Ñ‚Ðµ.
    public static final int THE_DISTANCE_IS_TOO_FAR_AND_SO_THE_CASTING_HAS_BEEN_STOPPED = 748; // Ð”Ð¸Ñ�Ñ‚Ð°Ð½Ñ†Ð¸Ñ�
    // Ñ�Ð»Ð¸ÑˆÐºÐ¾Ð¼
    // Ð²ÐµÐ»Ð¸ÐºÐ°,
    // Ð¿Ð¾Ñ�Ñ‚Ð¾Ð¼Ñƒ
    // Ð·Ð°ÐºÐ»Ð¸Ð½Ð°Ð½Ð¸Ðµ
    // Ð¾Ñ‚Ð¼ÐµÐ½ÐµÐ½Ð¾.
    public static final int THE_EFFECT_OF_S1_HAS_BEEN_REMOVED = 749; // $s1:
    // Ñ�Ñ„Ñ„ÐµÐºÑ‚
    // Ð¾Ñ‚Ð¼ÐµÐ½ÐµÐ½.
    public static final int THERE_ARE_NO_OTHER_SKILLS_TO_LEARN = 750; // Ð¡ÐµÐ¹Ñ‡Ð°Ñ�
    // Ð’Ñ‹ Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¸Ð·ÑƒÑ‡Ð°Ñ‚ÑŒ
    // Ð½Ð¾Ð²Ñ‹Ðµ
    // ÑƒÐ¼ÐµÐ½Ð¸Ñ�.
    public static final int AS_THERE_IS_A_CONFLICT_IN_THE_SIEGE_RELATIONSHIP_WITH_A_CLAN_IN_THE_ALLIANCE_YOU_CANNOT_INVITE_THAT_CLAN_TO_THE_ALLIANCE = 751; // ÐšÐ»Ð°Ð½
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð²ÑƒÐµÑ‚
    // Ð²
    // Ð¾Ñ�Ð°Ð´Ðµ
    // Ð·Ð°Ð¼ÐºÐ°
    // Ð½Ð°
    // Ð²Ñ€Ð°Ð¶ÐµÑ�ÐºÐ¾Ð¹
    // Ñ�Ñ‚Ð¾Ñ€Ð¾Ð½Ðµ.
    // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ð¿Ñ€Ð¸Ð³Ð»Ð°Ñ�Ð¸Ñ‚ÑŒ
    // Ð²
    // Ñ�Ð¾Ñ�Ñ‚Ð°Ð²
    // Ð°Ð»ÑŒÑ�Ð½Ñ�Ð°.
    public static final int THAT_NAME_CANNOT_BE_USED = 752; // Ð”Ð°Ð½Ð½Ð¾Ðµ Ð¸Ð¼Ñ�
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒ
    // Ð½ÐµÐ»ÑŒÐ·Ñ�.
    public static final int YOU_CANNOT_POSITION_MERCENARIES_HERE = 753; // Ð£Ñ�Ñ‚Ð°Ð½Ð¾Ð²Ð¸Ñ‚ÑŒ
    // Ð½Ð°ÐµÐ¼Ð½Ð¸ÐºÐ°
    // Ð²
    // Ñ�Ñ‚Ð¾Ð¼
    // Ð¼ÐµÑ�Ñ‚Ðµ
    // Ð½ÐµÐ»ÑŒÐ·Ñ�.
    public static final int THERE_ARE_S1_HOURS_AND_S2_MINUTES_LEFT_IN_THIS_WEEKS_USAGE_TIME = 754; // ÐžÑ�Ñ‚Ð°Ð»Ð¾Ñ�ÑŒ
    // $s1
    // Ñ‡
    // $s2
    // Ð¼Ð¸Ð½
    // Ð½Ð°
    // Ñ�Ñ‚Ð¾Ð¹
    // Ð½ÐµÐ´ÐµÐ»Ðµ.
    public static final int THERE_ARE_S1_MINUTES_LEFT_IN_THIS_WEEKS_USAGE_TIME = 755; // ÐžÑ�Ñ‚Ð°Ð»Ð¾Ñ�ÑŒ
    // $s2
    // Ð¼Ð¸Ð½
    // Ð½Ð°
    // Ñ�Ñ‚Ð¾Ð¹
    // Ð½ÐµÐ´ÐµÐ»Ðµ.
    public static final int THIS_WEEKS_USAGE_TIME_HAS_FINISHED = 756; // Ð’Ñ€ÐµÐ¼Ñ�
    // Ð½Ð° Ñ�Ñ‚Ð¾Ð¹
    // Ð½ÐµÐ´ÐµÐ»Ðµ
    // Ð·Ð°ÐºÐ¾Ð½Ñ‡Ð¸Ð»Ð¾Ñ�ÑŒ.
    public static final int THERE_ARE_S1_HOURS_AND_S2_MINUTES_LEFT_IN_THE_FIXED_USE_TIME = 757; // ÐžÑ�Ñ‚Ð°Ð»Ð¾Ñ�ÑŒ:
    // $s1
    // Ñ‡
    // $s2
    // Ð¼Ð¸Ð½.
    public static final int THERE_ARE_S1_MINUTES_LEFT_IN_THIS_WEEKS_PLAY_TIME = 758; // Ð’Ñ‹
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¸Ð³Ñ€Ð°Ñ‚ÑŒ
    // $s1
    // Ñ‡
    // $s2
    // Ð¼Ð¸Ð½
    // Ð´Ð¾
    // ÐºÐ¾Ð½Ñ†Ð°
    // Ñ�Ñ‚Ð¾Ð¹
    // Ð½ÐµÐ´ÐµÐ»Ð¸.
    public static final int THERE_ARE_S1_MINUTES_LEFT_IN_THIS_WEEKS_PLAY_TIME_1 = 759; // Ð’Ñ‹
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¸Ð³Ñ€Ð°Ñ‚ÑŒ
    // $s2
    // Ð¼Ð¸Ð½
    // Ð´Ð¾
    // ÐºÐ¾Ð½Ñ†Ð°
    // Ñ�Ñ‚Ð¾Ð¹
    // Ð½ÐµÐ´ÐµÐ»Ð¸.
    public static final int S1_CANNOT_JOIN_THE_CLAN_BECAUSE_ONE_DAY_HAS_NOT_YET_PASSED_SINCE_HE_SHE_LEFT_ANOTHER_CLAN = 760; // $c1
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð²Ñ�Ñ‚ÑƒÐ¿Ð¸Ñ‚ÑŒ
    // Ð²
    // ÐºÐ»Ð°Ð½.
    // Ð�Ðµ
    // Ð¿Ñ€Ð¾ÑˆÐ»Ð¾
    // 24
    // Ñ‡
    // Ñ�
    // Ð¼Ð¾Ð¼ÐµÐ½Ñ‚Ð°
    // Ð²Ñ‹Ñ…Ð¾Ð´Ð°
    // Ð¸Ð·
    // Ð¿Ñ€ÐµÐ´Ñ‹Ð´ÑƒÑ‰ÐµÐ³Ð¾.
    public static final int S1_CLAN_CANNOT_JOIN_THE_ALLIANCE_BECAUSE_ONE_DAY_HAS_NOT_YET_PASSED_SINCE_IT_LEFT_ANOTHER_ALLIANCE = 761; // ÐšÐ»Ð°Ð½
    // $s1
    // Ð²Ñ‹ÑˆÐµÐ»
    // Ð¸Ð·
    // Ð°Ð»ÑŒÑ�Ð½Ñ�Ð°
    // Ð¼ÐµÐ½ÐµÐµ
    // 24
    // Ñ‡
    // Ð½Ð°Ð·Ð°Ð´
    // Ð¸
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð²Ð¾Ð¹Ñ‚Ð¸
    // Ð²
    // Ñ�Ð¾Ñ�Ñ‚Ð°Ð²
    // Ð°Ð»ÑŒÑ�Ð½Ñ�Ð°
    // Ð²Ð½Ð¾Ð²ÑŒ.
    public static final int S1_ROLLED_S2_AND_S3S_EYE_CAME_OUT = 762; // $c1
    // Ð±Ñ€Ð¾Ñ�Ð°ÐµÑ‚
    // $s2.
    // Ð’Ñ‹Ð¿Ð°Ð´Ð°ÐµÑ‚:
    // $s3.
    public static final int YOU_FAILED_AT_SENDING_THE_PACKAGE_BECAUSE_YOU_ARE_TOO_FAR_FROM_THE_WAREHOUSE = 763; // Ð Ð°Ñ�Ñ�Ñ‚Ð¾Ñ�Ð½Ð¸Ðµ
    // Ð´Ð¾
    // Ñ…Ñ€Ð°Ð½Ð¸Ð»Ð¸Ñ‰Ð°
    // Ñ�Ð»Ð¸ÑˆÐºÐ¾Ð¼
    // Ð²ÐµÐ»Ð¸ÐºÐ¾.
    // ÐžÑ‚Ð¿Ñ€Ð°Ð²Ð¸Ñ‚ÑŒ
    // Ð¿Ð¾Ñ�Ñ‹Ð»ÐºÑƒ
    // Ð½ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾.
    public static final int YOU_HAVE_BEEN_PLAYING_FOR_AN_EXTENDED_PERIOD_OF_TIME_PLEASE_CONSIDER_TAKING_A_BREAK = 764; // Ð’Ñ‹
    // Ð¸Ð³Ñ€Ð°ÐµÑ‚Ðµ
    // ÑƒÐ¶Ðµ
    // Ð´Ð¾Ð²Ð¾Ð»ÑŒÐ½Ð¾
    // Ð´Ð¾Ð»Ð³Ð¾.
    // Ð¡Ð¾Ð²ÐµÑ‚ÑƒÐµÐ¼
    // Ð’Ð°Ð¼
    // Ð¾Ñ‚Ð´Ð¾Ñ…Ð½ÑƒÑ‚ÑŒ.
    public static final int GAMEGUARD_IS_ALREADY_RUNNING_PLEASE_TRY_RUNNING_IT_AGAIN_AFTER_REBOOTING = 765; // Ð�ÐºÑ‚Ð¸Ð²Ð¸Ñ€Ð¾Ð²Ð°Ð½Ð°
    // Ð¿Ñ€Ð¾Ð³Ñ€Ð°Ð¼Ð¼Ð°
    // GameGuard.
    // ÐŸÐµÑ€ÐµÐ·Ð°Ð³Ñ€ÑƒÐ·Ð¸Ñ‚ÐµÑ�ÑŒ
    // Ð¸
    // Ð¿Ð¾Ð²Ñ‚Ð¾Ñ€Ð¸Ñ‚Ðµ
    // Ð¿Ð¾Ð¿Ñ‹Ñ‚ÐºÑƒ.
    public static final int THERE_IS_A_GAMEGUARD_INITIALIZATION_ERROR_PLEASE_TRY_RUNNING_IT_AGAIN_AFTER_REBOOTING = 766; // ÐžÑˆÐ¸Ð±ÐºÐ°
    // Ð¾Ð±Ð½Ð¾Ð²Ð»ÐµÐ½Ð¸Ñ�
    // Ð¿Ñ€Ð¾Ð³Ñ€Ð°Ð¼Ð¼Ñ‹
    // GameGuard.
    // ÐŸÐµÑ€ÐµÐ·Ð°Ð³Ñ€ÑƒÐ·Ð¸Ñ‚Ðµ
    // ÐºÐ¾Ð¼Ð¿ÑŒÑŽÑ‚ÐµÑ€
    // Ð¸
    // Ð¿Ð¾Ð²Ñ‚Ð¾Ñ€Ð¸Ñ‚Ðµ
    // Ð¿Ð¾Ð¿Ñ‹Ñ‚ÐºÑƒ.
    public static final int THE_GAMEGUARD_FILE_IS_DAMAGED__PLEASE_REINSTALL_GAMEGUARD = 767; // ÐŸÐ¾Ð²Ñ€ÐµÐ¶Ð´ÐµÐ½
    // Ñ„Ð°Ð¹Ð»
    // Ð¿Ñ€Ð¾Ð³Ñ€Ð°Ð¼Ð¼Ñ‹
    // GameGuard.
    // ÐŸÐµÑ€ÐµÑƒÑ�Ñ‚Ð°Ð½Ð¾Ð²Ð¸Ñ‚Ðµ
    // Ð¿Ñ€Ð¾Ð³Ñ€Ð°Ð¼Ð¼Ñƒ
    // Ð·Ð°Ñ‰Ð¸Ñ‚Ñ‹.
    public static final int A_WINDOWS_SYSTEM_FILE_IS_DAMAGED_PLEASE_REINSTALL_INTERNET_EXPLORER = 768; // ÐŸÐ¾Ð²Ñ€ÐµÐ¶Ð´ÐµÐ½
    // Ñ�Ð¸Ñ�Ñ‚ÐµÐ¼Ð½Ñ‹Ð¹
    // Ñ„Ð°Ð¹Ð»
    // Windows.
    // ÐŸÐµÑ€ÐµÑƒÑ�Ñ‚Ð°Ð½Ð¾Ð²Ð¸Ñ‚Ðµ
    // Internet
    // Explorer.
    public static final int A_HACKING_TOOL_HAS_BEEN_DISCOVERED_PLEASE_TRY_PLAYING_AGAIN_AFTER_CLOSING_UNNECESSARY_PROGRAMS = 769; // ÐžÐ±Ð½Ð°Ñ€ÑƒÐ¶ÐµÐ½
    // Ð²Ð·Ð»Ð¾Ð¼.
    // Ð’Ñ‹ÐºÐ»ÑŽÑ‡Ð¸Ñ‚Ðµ
    // Ð½ÐµÐ½ÑƒÐ¶Ð½Ñ‹Ðµ
    // Ð¿Ñ€Ð¾Ð³Ñ€Ð°Ð¼Ð¼Ñ‹
    // Ð¸
    // Ð¿Ð¾Ð²Ñ‚Ð¾Ñ€Ð¸Ñ‚Ðµ
    // Ð¿Ð¾Ð¿Ñ‹Ñ‚ÐºÑƒ.
    public static final int THE_GAMEGUARD_UPDATE_WAS_CANCELED_PLEASE_CHECK_YOUR_NETWORK_CONNECTION_STATUS_OR_FIREWALL = 770; // ÐžÐ±Ð½Ð¾Ð²Ð»ÐµÐ½Ð¸Ðµ
    // Ð¿Ñ€Ð¾Ð³Ñ€Ð°Ð¼Ð¼Ñ‹
    // GameGuard
    // Ð¾Ñ‚Ð¼ÐµÐ½ÐµÐ½Ð¾.
    // ÐŸÑ€Ð¾Ð²ÐµÑ€ÑŒÑ‚Ðµ
    // Ð½Ð°Ñ�Ñ‚Ñ€Ð¾Ð¹ÐºÐ¸
    // Ñ�ÐµÑ‚Ð¸
    // Ð¸
    // Firewall.
    public static final int THE_GAMEGUARD_UPDATE_WAS_CANCELED_PLEASE_TRY_RUNNING_IT_AGAIN_AFTER_DOING_A_VIRUS_SCAN_OR_CHANGING_THE_SETTINGS_IN_YOUR_PC_MANAGEMENT_PROGRAM = 771; // ÐžÐ±Ð½Ð¾Ð²Ð»ÐµÐ½Ð¸Ðµ
    // Ð¿Ñ€Ð¾Ð³Ñ€Ð°Ð¼Ð¼Ñ‹
    // GameGuard
    // Ð¾Ñ‚Ð¼ÐµÐ½ÐµÐ½Ð¾.
    // ÐŸÑ€Ð¾Ð²ÐµÑ€ÑŒÑ‚Ðµ
    // ÐºÐ¾Ð¼Ð¿ÑŒÑŽÑ‚ÐµÑ€
    // Ð½Ð°
    // Ð½Ð°Ð»Ð¸Ñ‡Ð¸Ðµ
    // Ð²Ð¸Ñ€ÑƒÑ�Ð°
    // Ð¸Ð»Ð¸
    // Ð¸Ð·Ð¼ÐµÐ½Ð¸Ñ‚Ðµ
    // Ð½Ð°Ñ�Ñ‚Ñ€Ð¾Ð¹ÐºÐ¸
    // ÑƒÐ¿Ñ€Ð°Ð²Ð»ÐµÐ½Ð¸Ñ�
    // Ð¿Ñ€Ð¾Ð³Ñ€Ð°Ð¼Ð¼Ð°Ð¼Ð¸
    // Ð¸
    // Ð¿Ð¾Ð²Ñ‚Ð¾Ñ€Ð¸Ñ‚Ðµ
    // Ð¿Ð¾Ð¿Ñ‹Ñ‚ÐºÑƒ.
    public static final int THERE_WAS_A_PROBLEM_WHEN_RUNNING_GAMEGUARD = 772; // ÐžÐ±Ð½Ð°Ñ€ÑƒÐ¶ÐµÐ½Ð°
    // Ð¾ÑˆÐ¸Ð±ÐºÐ°
    // Ð²Ð¾
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ð¿Ð¾Ð´ÐºÐ»ÑŽÑ‡ÐµÐ½Ð¸Ñ�
    // GameGuard.
    public static final int THE_GAME_OR_GAMEGUARD_FILES_ARE_DAMAGED = 773; // ÐŸÐ¾Ð²Ñ€ÐµÐ¶Ð´ÐµÐ½
    // Ñ„Ð°Ð¹Ð»
    // Ð¸Ð³Ñ€Ñ‹
    // Ð¸Ð»Ð¸
    // GameGuard.
    public static final int SINCE_THIS_IS_A_PEACE_ZONE_PLAY_TIME_DOES_NOT_GET_EXPENDED_HERE = 774; // Ð’Ñ€ÐµÐ¼Ñ�
    // Ð¸Ð³Ñ€Ñ‹
    // Ð±Ð¾Ð»ÐµÐµ
    // Ð½Ðµ
    // Ñ€Ð°Ñ�Ñ…Ð¾Ð´ÑƒÐµÑ‚Ñ�Ñ�.
    public static final int FROM_HERE_ON_PLAY_TIME_WILL_BE_EXPENDED = 775; // Ð¡
    // Ñ�Ñ‚Ð¾Ð³Ð¾
    // Ð¼Ð¾Ð¼ÐµÐ½Ñ‚Ð°
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ð¸Ð³Ñ€Ñ‹
    // Ð±ÑƒÐ´ÐµÑ‚
    // Ñ€Ð°Ñ�Ñ…Ð¾Ð´Ð¾Ð²Ð°Ñ‚ÑŒÑ�Ñ�.
    public static final int YOU_MAY_NOT_LOG_OUT_FROM_THIS_LOCATION = 778; // Ð—Ð´ÐµÑ�ÑŒ
    // Ð½ÐµÐ»ÑŒÐ·Ñ�
    // Ð²Ñ‹Ð¹Ñ‚Ð¸
    // Ð¸Ð·
    // Ð¸Ð³Ñ€Ñ‹.
    public static final int YOU_MAY_NOT_RESTART_IN_THIS_LOCATION = 779; // Ð—Ð´ÐµÑ�ÑŒ
    // Ð½ÐµÐ»ÑŒÐ·Ñ�
    // Ð¿ÐµÑ€ÐµÐ·Ð°Ð¿ÑƒÑ�Ñ‚Ð¸Ñ‚ÑŒ
    // Ð¸Ð³Ñ€Ñƒ.
    public static final int OBSERVATION_IS_ONLY_POSSIBLE_DURING_A_SIEGE = 780; // Ð ÐµÐ¶Ð¸Ð¼
    // Ð·Ñ€Ð¸Ñ‚ÐµÐ»Ñ�
    // Ð²Ð¾Ð·Ð¼Ð¾Ð¶ÐµÐ½
    // Ñ‚Ð¾Ð»ÑŒÐºÐ¾
    // Ð²Ð¾
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ð¾Ñ�Ð°Ð´Ñ‹.
    public static final int OBSERVERS_CANNOT_PARTICIPATE = 781; // Ð­Ñ‚Ð° Ñ„ÑƒÐ½ÐºÑ†Ð¸Ñ�
    // Ð½ÐµÐ´Ð¾Ñ�Ñ‚ÑƒÐ¿Ð½Ð° Ð²
    // Ñ€ÐµÐ¶Ð¸Ð¼Ðµ
    // Ð·Ñ€Ð¸Ñ‚ÐµÐ»Ñ�.
    public static final int YOU_MAY_NOT_OBSERVE_A_SUMMONED_CREATURE = 782; // Ð•Ñ�Ð»Ð¸
    // Ð’Ñ‹
    // Ð¿Ñ€Ð¸Ð·Ð²Ð°Ð»Ð¸
    // Ð¿Ð¸Ñ‚Ð¾Ð¼Ñ†Ð°
    // Ð¸Ð»Ð¸
    // Ð´Ñ€ÑƒÐ³Ð¾Ðµ
    // Ñ�ÑƒÑ‰ÐµÑ�Ñ‚Ð²Ð¾,
    // Ð²Ð¾Ð¹Ñ‚Ð¸
    // Ð²
    // Ñ€ÐµÐ¶Ð¸Ð¼
    // Ð·Ñ€Ð¸Ñ‚ÐµÐ»Ñ�
    // Ð½ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾.
    public static final int LOTTERY_TICKET_SALES_HAVE_BEEN_TEMPORARILY_SUSPENDED = 783; // Ð’
    // Ð´Ð°Ð½Ð½Ñ‹Ð¹
    // Ð¼Ð¾Ð¼ÐµÐ½Ñ‚
    // Ð¿Ñ€Ð¾Ð´Ð°Ð¶Ð°
    // Ð»Ð¾Ñ‚ÐµÑ€ÐµÐ¹Ð½Ñ‹Ñ…
    // Ð±Ð¸Ð»ÐµÑ‚Ð¾Ð²
    // Ð¿Ñ€Ð¸Ð¾Ñ�Ñ‚Ð°Ð½Ð¾Ð²Ð»ÐµÐ½Ð°.
    public static final int TICKETS_FOR_THE_CURRENT_LOTTERY_ARE_NO_LONGER_AVAILABLE = 784; // ÐŸÑ€Ð¾Ð´Ð°Ð¶Ð°
    // Ð»Ð¾Ñ‚ÐµÑ€ÐµÐ¹Ð½Ñ‹Ñ…
    // Ð±Ð¸Ð»ÐµÑ‚Ð¾Ð²
    // Ð·Ð°Ð²ÐµÑ€ÑˆÐµÐ½Ð°.
    public static final int THE_RESULTS_OF_LOTTERY_NUMBER_S1_HAVE_NOT_YET_BEEN_PUBLISHED = 785; // Ð•Ñ‰Ðµ
    // Ð½Ðµ
    // Ð¿Ð¾Ð»ÑƒÑ‡ÐµÐ½Ñ‹
    // Ñ€ÐµÐ·ÑƒÐ»ÑŒÑ‚Ð°Ñ‚Ñ‹
    // Ð»Ð¾Ñ‚ÐµÑ€ÐµÐ¸
    // â„–$s1.
    public static final int INCORRECT_SYNTAX = 786; // Ð˜Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ð½Ð¾ Ð½ÐµÐ²ÐµÑ€Ð½Ð¾Ðµ
    // Ñ�Ð»Ð¾Ð²Ð¾.
    public static final int THE_TRYOUTS_ARE_FINISHED = 787; // ÐžÑ‚Ð±Ð¾Ñ€Ð¾Ñ‡Ð½Ñ‹Ð¹ Ñ‚ÑƒÑ€
    // Ð·Ð°Ð²ÐµÑ€ÑˆÐµÐ½.
    public static final int THE_FINALS_ARE_FINISHED = 788; // Ð¤Ð¸Ð½Ð°Ð» Ð·Ð°Ð²ÐµÑ€ÑˆÐµÐ½.
    public static final int THE_TRYOUTS_HAVE_BEGUN = 789; // ÐžÑ‚Ð±Ð¾Ñ€Ð¾Ñ‡Ð½Ñ‹Ð¹ Ñ‚ÑƒÑ€
    // Ð½Ð°Ñ‡Ð°Ð»Ñ�Ñ�.
    public static final int THE_FINALS_HAVE_BEGUN = 790; // Ð¤Ð¸Ð½Ð°Ð» Ð½Ð°Ñ‡Ð°Ð»Ñ�Ñ�.
    public static final int THE_FINAL_MATCH_IS_ABOUT_TO_BEGIN_LINE_UP = 791; // Ð¤Ð¸Ð½Ð°Ð»
    // Ñ�ÐºÐ¾Ñ€Ð¾
    // Ð½Ð°Ñ‡Ð½ÐµÑ‚Ñ�Ñ�.
    public static final int THE_SIEGE_OF_THE_CLAN_HALL_IS_FINISHED = 792; // Ð‘Ð¸Ñ‚Ð²Ð°
    // Ð·Ð°
    // Ñ…Ð¾Ð»Ð»
    // ÐºÐ»Ð°Ð½Ð°
    // Ð·Ð°Ð²ÐµÑ€ÑˆÐµÐ½Ð°.
    public static final int THE_SIEGE_OF_THE_CLAN_HALL_HAS_BEGUN = 793; // Ð‘Ð¸Ñ‚Ð²Ð°
    // Ð·Ð°
    // Ñ…Ð¾Ð»Ð»
    // ÐºÐ»Ð°Ð½Ð°
    // Ð½Ð°Ñ‡Ð°Ð»Ð°Ñ�ÑŒ.
    public static final int YOU_ARE_NOT_AUTHORIZED_TO_DO_THAT = 794; // Ð£ Ð’Ð°Ñ�
    // Ð½ÐµÑ‚
    // Ð¿Ñ€Ð°Ð²,
    // Ñ‡Ñ‚Ð¾Ð±Ñ‹
    // Ñ�Ð´ÐµÐ»Ð°Ñ‚ÑŒ
    // Ñ�Ñ‚Ð¾.
    public static final int ONLY_CLAN_LEADERS_ARE_AUTHORIZED_TO_SET_RIGHTS = 795; // ÐŸÑ€Ð°Ð²Ð°
    // Ñ€Ð°Ñ�Ð¿Ñ€ÐµÐ´ÐµÐ»Ñ�ÑŽÑ‚Ñ�Ñ�
    // Ð»Ð¸Ð´ÐµÑ€Ð¾Ð¼
    // ÐºÐ»Ð°Ð½Ð°.
    public static final int YOUR_REMAINING_OBSERVATION_TIME_IS_S1_MINUTES = 796; // ÐžÑ�Ñ‚Ð°Ð»Ð¾Ñ�ÑŒ
    // Ð²
    // Ñ€ÐµÐ¶Ð¸Ð¼Ðµ
    // Ð·Ñ€Ð¸Ñ‚ÐµÐ»Ñ�:
    // $s1
    // Ð¼Ð¸Ð½.
    public static final int YOU_MAY_CREATE_UP_TO_48_MACROS = 797; // ÐœÐ¾Ð¶Ð½Ð¾
    // Ñ�Ð¾Ð·Ð´Ð°Ñ‚ÑŒ 48
    // Ð¼Ð°ÐºÑ€Ð¾Ñ�Ð¾Ð².
    public static final int ITEM_REGISTRATION_IS_IRREVERSIBLE_DO_YOU_WISH_TO_CONTINUE = 798; // Ð ÐµÐ³Ð¸Ñ�Ñ‚Ñ€Ð°Ñ†Ð¸ÑŽ
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ð°
    // Ð½ÐµÐ»ÑŒÐ·Ñ�
    // Ð¾Ñ‚Ð¼ÐµÐ½Ð¸Ñ‚ÑŒ.
    // ÐŸÑ€Ð¾Ð´Ð¾Ð»Ð¶Ð¸Ñ‚ÑŒ?
    public static final int THE_OBSERVATION_TIME_HAS_EXPIRED = 799; // Ð’Ñ€ÐµÐ¼Ñ�
    // Ñ€ÐµÐ¶Ð¸Ð¼Ð°
    // Ð·Ñ€Ð¸Ñ‚ÐµÐ»Ñ�
    // Ð¸Ñ�Ñ‚ÐµÐºÐ»Ð¾.
    public static final int YOU_ARE_TOO_LATE_THE_REGISTRATION_PERIOD_IS_OVER = 800; // Ð’Ñ€ÐµÐ¼Ñ�
    // Ð¿Ð¾Ð´Ð°Ñ‡Ð¸
    // Ð·Ð°Ñ�Ð²ÐºÐ¸
    // Ð½Ð°
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð¸Ðµ
    // Ð²
    // Ð±Ð¸Ñ‚Ð²Ðµ
    // Ð·Ð°
    // Ñ…Ð¾Ð»Ð»
    // ÐºÐ»Ð°Ð½Ð°
    // Ð¸Ñ�Ñ‚ÐµÐºÐ»Ð¾,
    // Ñ€ÐµÐ³Ð¸Ñ�Ñ‚Ñ€Ð°Ñ†Ð¸Ñ�
    // Ð½ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð°.
    public static final int REGISTRATION_FOR_THE_CLAN_HALL_SIEGE_IS_CLOSED = 801; // Ð—Ð°Ñ�Ð²ÐºÐ¸
    // Ð½Ð°
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð¸Ðµ
    // Ð²
    // Ð±Ð¸Ñ‚Ð²Ðµ
    // Ð·Ð°
    // Ñ…Ð¾Ð»Ð»
    // ÐºÐ»Ð°Ð½Ð°
    // Ð±Ð¾Ð»ÑŒÑˆÐµ
    // Ð½Ðµ
    // Ð¿Ñ€Ð¸Ð½Ð¸Ð¼Ð°ÑŽÑ‚Ñ�Ñ�.
    public static final int PETITIONS_ARE_NOT_BEING_ACCEPTED_AT_THIS_TIME_YOU_MAY_SUBMIT_YOUR_PETITION_AFTER_S1_AM_PM = 802; // Ð�ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾
    // Ð¾Ñ‚ÐºÑ€Ñ‹Ñ‚ÑŒ
    // ÐµÑ‰Ðµ
    // Ð¾Ð´Ð½Ð¾
    // Ð¾ÐºÐ½Ð¾
    // Ð·Ñ€Ð¸Ñ‚ÐµÐ»Ñ�.
    // Ð—Ð°ÐºÑ€Ð¾Ð¹Ñ‚Ðµ
    // ÑƒÐ¶Ðµ
    // Ð¾Ñ‚ÐºÑ€Ñ‹Ñ‚Ð¾Ðµ
    // Ð¾ÐºÐ½Ð¾
    // Ð¸
    // Ð¿Ð¾Ð²Ñ‚Ð¾Ñ€Ð¸Ñ‚Ðµ
    // Ð¿Ð¾Ð¿Ñ‹Ñ‚ÐºÑƒ.
    public static final int ENTER_THE_SPECIFICS_OF_YOUR_PETITION = 803; // Ð’Ð²ÐµÐ´Ð¸Ñ‚Ðµ
    // Ñ�Ð¾Ð´ÐµÑ€Ð¶Ð°Ð½Ð¸Ðµ
    // Ð¿ÐµÑ‚Ð¸Ñ†Ð¸Ð¸.
    public static final int SELECT_A_TYPE = 804; // Ð’Ñ‹Ð±ÐµÑ€Ð¸Ñ‚Ðµ Ñ‚Ð¸Ð¿.
    public static final int PETITIONS_ARE_NOT_BEING_ACCEPTED_AT_THIS_TIME_YOU_MAY_SUBMIT_YOUR_PETITION_AFTER_S1_AM_PM_1 = 805; // Ð�Ð°
    // Ð´Ð°Ð½Ð½Ñ‹Ð¹
    // Ð¼Ð¾Ð¼ÐµÐ½Ñ‚
    // Ð¿ÐµÑ‚Ð¸Ñ†Ð¸Ñ�
    // ÐµÑ‰Ðµ
    // Ð½Ðµ
    // Ð±Ñ‹Ð»Ð°
    // Ð¿Ñ€Ð¸Ð½Ñ�Ñ‚Ð°.
    // ÐŸÐ¾Ð¿Ñ€Ð¾Ð±ÑƒÐ¹Ñ‚Ðµ
    // Ð¿Ð¾Ð²Ñ‚Ð¾Ñ€Ð¸Ñ‚ÑŒ
    // Ð¿Ð¾Ð¿Ñ‹Ñ‚ÐºÑƒ
    // Ñ‡ÐµÑ€ÐµÐ·
    // $s1
    // Ñ‡.
    public static final int IF_YOU_ARE_TRAPPED_TRY_TYPING__UNSTUCK = 806; // Ð•Ñ�Ð»Ð¸
    // Ð²Ñ‹
    // Ð·Ð°Ñ�Ñ‚Ñ€Ñ�Ð½ÐµÑ‚Ðµ
    // Ð²
    // Ñ‚ÐµÐºÑ�Ñ‚ÑƒÑ€Ð°Ñ…,
    // Ð²Ð²ÐµÐ´Ð¸Ñ‚Ðµ
    // Ð²
    // Ñ�Ñ‚Ñ€Ð¾ÐºÑƒ
    // Ð²Ð²Ð¾Ð´Ð°
    // ÐºÐ¾Ð¼Ð°Ð½Ð´Ñƒ
    // "/unstuck"
    public static final int THIS_TERRAIN_IS_UNNAVIGABLE_PREPARE_FOR_TRANSPORT_TO_THE_NEAREST_VILLAGE = 807; // Ð’Ñ‹
    // Ð½Ð°Ñ…Ð¾Ð´Ð¸Ñ‚ÐµÑ�ÑŒ
    // Ð²
    // Ð·Ð¾Ð½Ðµ,
    // Ð³Ð´Ðµ
    // Ð¿ÐµÑ€ÐµÐ¼ÐµÑ‰ÐµÐ½Ð¸Ðµ
    // Ð½ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾,
    // Ð¼Ñ‹
    // Ð¿ÐµÑ€ÐµÐ¼ÐµÑ�Ñ‚Ð¸Ð¼
    // Ð’Ð°Ñ�
    // Ð²
    // Ð±Ð»Ð¸Ð¶Ð°Ð¹ÑˆÐ¸Ð¹
    // Ð³Ð¾Ñ€Ð¾Ð´.
    public static final int YOU_ARE_STUCK_YOU_MAY_SUBMIT_A_PETITION_BY_TYPING__GM = 808; // Ð’Ñ‹
    // Ð·Ð°Ñ�Ñ‚Ñ€Ñ�Ð»Ð¸.
    // Ð’Ð°Ð¼
    // Ð½ÐµÐ¾Ð±Ñ…Ð¾Ð´Ð¸Ð¼Ð¾
    // Ð¾Ñ‚Ð¿Ñ€Ð°Ð²Ð¸Ñ‚ÑŒ
    // Ð¿ÐµÑ‚Ð¸Ñ†Ð¸ÑŽ
    // Ñ�
    // Ð¿Ð¾Ð¼Ð¾Ñ‰ÑŒÑŽ
    // ÐºÐ¾Ð¼Ð°Ð½Ð´Ñ‹
    // "/gm".
    public static final int YOU_ARE_STUCK_YOU_WILL_BE_TRANSPORTED_TO_THE_NEAREST_VILLAGE_IN_FIVE_MINUTES = 809; // Ð’Ñ‹
    // Ð·Ð°Ñ�Ñ‚Ñ€Ñ�Ð»Ð¸.
    // Ð§ÐµÑ€ÐµÐ·
    // 5
    // Ð¼Ð¸Ð½
    // Ð’Ñ‹
    // Ð±ÑƒÐ´ÐµÑ‚Ðµ
    // Ð¿ÐµÑ€ÐµÐ¼ÐµÑ‰ÐµÐ½Ñ‹
    // Ð²
    // Ð±Ð»Ð¸Ð¶Ð°Ð¹ÑˆÐ¸Ð¹
    // Ð³Ð¾Ñ€Ð¾Ð´.
    public static final int INVALID_MACRO_REFER_TO_THE_HELP_FILE_FOR_INSTRUCTIONS = 810; // Ð�ÐµÐ²ÐµÑ€Ð½Ñ‹Ð¹
    // Ð¼Ð°ÐºÑ€Ð¾Ñ�.
    // ÐžÐ±Ñ€Ð°Ñ‚Ð¸Ñ‚ÐµÑ�ÑŒ
    // Ðº
    // Ñ€ÑƒÐºÐ¾Ð²Ð¾Ð´Ñ�Ñ‚Ð²Ñƒ
    // Ð¿Ð¾
    // Ð¼Ð°ÐºÑ€Ð¾Ñ�Ð°Ð¼.
    public static final int YOU_WILL_BE_MOVED_TO_S1_DO_YOU_WISH_TO_CONTINUE = 811; // Ð’Ñ‹
    // Ð¿ÐµÑ€ÐµÐ¼ÐµÑ‰Ð°ÐµÑ‚ÐµÑ�ÑŒ
    // Ð²
    // Ð»Ð¾ÐºÐ°Ñ†Ð¸ÑŽ
    // ($s1).
    // ÐŸÑ€Ð¾Ð´Ð¾Ð»Ð¶Ð¸Ñ‚ÑŒ?
    public static final int THE_SECRET_TRAP_HAS_INFLICTED_S1_DAMAGE_ON_YOU = 812; // Ð’Ñ‹
    // Ð¿Ð¾Ð¿Ð°Ð»Ð¸
    // Ð²
    // Ð»Ð¾Ð²ÑƒÑˆÐºÑƒ
    // Ð¸
    // Ð¿Ð¾Ð»ÑƒÑ‡Ð¸Ð»Ð¸
    // $s1
    // ÑƒÑ€Ð¾Ð½Ð°.
    public static final int YOU_HAVE_BEEN_POISONED_BY_A_SECRET_TRAP = 813; // Ð’Ñ‹
    // Ð¿Ð¾Ð¿Ð°Ð»Ð¸Ñ�ÑŒ
    // Ð²
    // Ð»Ð¾Ð²ÑƒÑˆÐºÑƒ
    // Ð¸
    // Ð±Ñ‹Ð»Ð¸
    // Ð¾Ñ‚Ñ€Ð°Ð²Ð»ÐµÐ½Ñ‹.
    public static final int YOUR_SPEED_HAS_BEEN_DECREASED_BY_A_SECRET_TRAP = 814; // Ð’Ñ‹
    // Ð¿Ð¾Ð¿Ð°Ð»Ð¸Ñ�ÑŒ
    // Ð²
    // Ð»Ð¾Ð²ÑƒÑˆÐºÑƒ,
    // Ð¸
    // Ð’Ð°ÑˆÐ°
    // Ñ�ÐºÐ¾Ñ€Ð¾Ñ�Ñ‚ÑŒ
    // Ð±Ñ‹Ð»Ð°
    // Ñ�Ð½Ð¸Ð¶ÐµÐ½Ð°.
    public static final int THE_TRYOUTS_ARE_ABOUT_TO_BEGIN_LINE_UP = 815; // ÐžÑ‚Ð±Ð¾Ñ€Ð¾Ñ‡Ð½Ñ‹Ð¹
    // Ñ‚ÑƒÑ€
    // Ñ�ÐµÐ¹Ñ‡Ð°Ñ�
    // Ð½Ð°Ñ‡Ð½ÐµÑ‚Ñ�Ñ�.
    // ÐŸÑ€Ð¸Ð³Ð¾Ñ‚Ð¾Ð²ÑŒÑ‚ÐµÑ�ÑŒ.
    public static final int TICKETS_ARE_NOW_AVAILABLE_FOR_THE_S1TH_MONSTER_RACE = 816; // Ð¡ÐºÐ¾Ñ€Ð¾
    // Ð½Ð°Ñ‡Ð½ÐµÑ‚Ñ�Ñ�
    // Ð³Ð¾Ð½ÐºÐ°
    // Ð¼Ð¾Ð½Ñ�Ñ‚Ñ€Ð¾Ð²
    // $s1.
    // ÐšÑƒÐ¿Ð¸Ñ‚Ðµ
    // Ð±Ð¸Ð»ÐµÑ‚Ñ‹.
    public static final int WE_ARE_NOW_SELLING_TICKETS_FOR_THE_S1TH_MONSTER_RACE = 817; // ÐŸÑ€Ð¾Ð´Ð°ÑŽÑ‚Ñ�Ñ�
    // Ð±Ð¸Ð»ÐµÑ‚Ñ‹
    // Ð½Ð°
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð¸Ðµ
    // Ð²
    // Ð³Ð¾Ð½ÐºÐµ
    // Ð¼Ð¾Ð½Ñ�Ñ‚Ñ€Ð¾Ð²
    // $s1.
    public static final int TICKET_SALES_FOR_THE_MONSTER_RACE_WILL_CEASE_IN_S1_MINUTE_S = 818; // ÐŸÑ€Ð¾Ð´Ð°Ð¶Ð°
    // Ð±Ð¸Ð»ÐµÑ‚Ð¾Ð²
    // Ð½Ð°
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð¸Ðµ
    // Ð²
    // Ð³Ð¾Ð½ÐºÐµ
    // Ð¼Ð¾Ð½Ñ�Ñ‚Ñ€Ð¾Ð²
    // $s1
    // Ð·Ð°Ð²ÐµÑ€ÑˆÐµÐ½Ð°.
    public static final int TICKETS_SALES_ARE_CLOSED_FOR_THE_S1TH_MONSTER_RACE_ODDS_ARE_POSTED = 819; // ÐŸÑ€Ð¾Ð´Ð°Ð¶Ð°
    // Ð±Ð¸Ð»ÐµÑ‚Ð¾Ð²
    // Ð½Ð°
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð¸Ðµ
    // Ð²
    // Ð³Ð¾Ð½ÐºÐµ
    // Ð¼Ð¾Ð½Ñ�Ñ‚Ñ€Ð¾Ð²
    // $s1
    // Ð·Ð°Ð²ÐµÑ€ÑˆÐµÐ½Ð°.
    // Ð’Ñ‹
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¿Ñ€Ð¾Ñ�Ð¼Ð¾Ñ‚Ñ€ÐµÑ‚ÑŒ
    // Ñ�ÑƒÐ¼Ð¼Ñƒ
    // Ð²Ñ‹Ð¸Ð³Ñ€Ñ‹ÑˆÐ°.
    public static final int THE_S2TH_MONSTER_RACE_WILL_BEGIN_IN_S1_MINUTES = 820; // Ð§ÐµÑ€ÐµÐ·
    // $s1
    // Ð¼Ð¸Ð½
    // Ð½Ð°Ñ‡Ð½ÐµÑ‚Ñ�Ñ�
    // Ð³Ð¾Ð½ÐºÐ°
    // Ð¼Ð¾Ð½Ñ�Ñ‚Ñ€Ð¾Ð²
    // $s2.
    public static final int THE_S1TH_MONSTER_RACE_WILL_BEGIN_IN_30_SECONDS = 821; // Ð§ÐµÑ€ÐµÐ·
    // 30
    // Ñ�ÐµÐº
    // Ð½Ð°Ñ‡Ð½ÐµÑ‚Ñ�Ñ�
    // Ð³Ð¾Ð½ÐºÐ°
    // Ð¼Ð¾Ð½Ñ�Ñ‚Ñ€Ð¾Ð²
    // $s2.
    public static final int THE_S1TH_MONSTER_RACE_IS_ABOUT_TO_BEGIN_COUNTDOWN_IN_FIVE_SECONDS = 822; // Ð“Ð¾Ð½ÐºÐ°
    // Ð¼Ð¾Ð½Ñ�Ñ‚Ñ€Ð¾Ð²
    // $s1
    // Ñ�ÐºÐ¾Ñ€Ð¾
    // Ð½Ð°Ñ‡Ð½ÐµÑ‚Ñ�Ñ�.
    // Ð§ÐµÑ€ÐµÐ·
    // 5
    // Ñ�ÐµÐº
    // Ð½Ð°Ñ‡Ð½ÐµÑ‚Ñ�Ñ�
    // Ð¾Ñ‚Ñ�Ñ‡ÐµÑ‚.
    public static final int THE_RACE_WILL_BEGIN_IN_S1_SECONDS = 823; // Ð”Ð¾
    // Ð½Ð°Ñ‡Ð°Ð»Ð°
    // $s1 Ñ�ÐµÐº!
    public static final int THEYRE_OFF = 824; // Ð¡Ñ‚Ð°Ñ€Ñ‚! Ð“Ð¾Ð½ÐºÐ° Ð½Ð°Ñ‡Ð°Ð»Ð°Ñ�ÑŒ!
    public static final int MONSTER_RACE_S1_IS_FINISHED = 825; // Ð“Ð¾Ð½ÐºÐ° Ð¼Ð¾Ð½Ñ�Ñ‚Ñ€Ð¾Ð²
    // $s1 Ð·Ð°Ð²ÐµÑ€ÑˆÐµÐ½Ð°.
    public static final int FIRST_PRIZE_GOES_TO_THE_PLAYER_IN_LANE_S1_SECOND_PRIZE_GOES_TO_THE_PLAYER_IN_LANE_S2 = 826; // ÐŸÐ¾Ð±ÐµÐ´Ð¸Ð»
    // Ð¼Ð¾Ð½Ñ�Ñ‚Ñ€
    // Ð½Ð°
    // Ð´Ð¾Ñ€Ð¾Ð¶ÐºÐµ
    // $s1!
    // II
    // Ð¼ÐµÑ�Ñ‚Ð¾
    // -
    // Ð·Ð°
    // Ð´Ð¾Ñ€Ð¾Ð¶ÐºÐ¾Ð¹
    // $s2.
    public static final int YOU_MAY_NOT_IMPOSE_A_BLOCK_ON_A_GM = 827; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ð·Ð°Ð±Ð»Ð¾ÐºÐ¸Ñ€Ð¾Ð²Ð°Ñ‚ÑŒ
    // GM.
    public static final int ARE_YOU_SURE_YOU_WISH_TO_DELETE_THE_S1_MACRO = 828; // ÐœÐ°ÐºÑ€Ð¾Ñ�
    // $s1
    // Ð±ÑƒÐ´ÐµÑ‚
    // ÑƒÐ´Ð°Ð»ÐµÐ½.
    // ÐŸÑ€Ð¾Ð´Ð¾Ð»Ð¶Ð¸Ñ‚ÑŒ?
    public static final int S1_HAS_ROLLED_S2 = 834; // Ð�Ð° Ð±Ñ€Ð¾ÑˆÐµÐ½Ð½Ñ‹Ñ… Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶ÐµÐ¼
    // $c1 ÐºÑƒÐ±Ð¸ÐºÐ°Ñ… Ð²Ñ‹Ð¿Ð°Ð»Ð¾ $s2.
    public static final int YOU_MAY_NOT_THROW_THE_DICE_AT_THIS_TIMETRY_AGAIN_LATER = 835; // Ð¡ÐµÐ¹Ñ‡Ð°Ñ�
    // Ð±Ñ€Ð¾Ñ�Ð¸Ñ‚ÑŒ
    // ÐºÑƒÐ±Ð¸ÐºÐ¸
    // Ð½ÐµÐ»ÑŒÐ·Ñ�.
    // ÐŸÐ¾Ð¿Ñ€Ð¾Ð±ÑƒÐ¹Ñ‚Ðµ
    // Ð¿Ð¾Ð·Ð¶Ðµ.
    public static final int THE_INVENTORY_IS_FULL_NO_FURTHER_QUEST_ITEMS_MAY_BE_DEPOSITED_AT_THIS_TIME = 836; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð²Ð·Ñ�Ñ‚ÑŒ
    // Ñ�Ñ‚Ð¾Ñ‚
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚,
    // Ñ‚Ð°Ðº
    // ÐºÐ°Ðº
    // Ð’Ð°Ñˆ
    // Ð¸Ð½Ð²ÐµÐ½Ñ‚Ð°Ñ€ÑŒ
    // Ð¿ÐµÑ€ÐµÐ¿Ð¾Ð»Ð½ÐµÐ½.
    public static final int MACRO_DESCRIPTIONS_MAY_CONTAIN_UP_TO_32_CHARACTERS = 837; // ÐžÐ¿Ð¸Ñ�Ð°Ð½Ð¸Ðµ
    // Ð¼Ð°ÐºÑ€Ð¾Ñ�Ð°
    // Ð½Ðµ
    // Ð´Ð¾Ð»Ð¶Ð½Ð¾
    // Ð¿Ñ€ÐµÐ²Ñ‹ÑˆÐ°Ñ‚ÑŒ
    // 32
    // Ñ�Ð¸Ð¼Ð²Ð¾Ð»Ð°.
    public static final int ENTER_THE_NAME_OF_THE_MACRO = 838; // Ð’Ð²ÐµÐ´Ð¸Ñ‚Ðµ Ð¸Ð¼Ñ�
    // Ð¼Ð°ÐºÑ€Ð¾Ñ�Ð°.
    public static final int THAT_NAME_IS_ALREADY_ASSIGNED_TO_ANOTHER_MACRO = 839; // ÐœÐ°ÐºÑ€Ð¾Ñ�
    // Ñ�
    // Ñ‚Ð°ÐºÐ¸Ð¼
    // Ð¸Ð¼ÐµÐ½ÐµÐ¼
    // ÑƒÐ¶Ðµ
    // Ñ�ÑƒÑ‰ÐµÑ�Ñ‚Ð²ÑƒÐµÑ‚.
    public static final int THAT_RECIPE_IS_ALREADY_REGISTERED = 840; // Ð¢Ð°ÐºÐ¾Ð¹
    // Ñ€ÐµÑ†ÐµÐ¿Ñ‚
    // ÑƒÐ¶Ðµ
    // Ñ�ÑƒÑ‰ÐµÑ�Ñ‚Ð²ÑƒÐµÑ‚.
    public static final int NO_FURTHER_RECIPES_MAY_BE_REGISTERED = 841; // Ð—Ð°Ñ€ÐµÐ³Ð¸Ñ�Ñ‚Ñ€Ð¸Ñ€Ð¾Ð²Ð°Ñ‚ÑŒ
    // Ñ€ÐµÑ†ÐµÐ¿Ñ‚
    // Ð±Ð¾Ð»ÑŒÑˆÐµ
    // Ð½ÐµÐ»ÑŒÐ·Ñ�.
    public static final int YOU_ARE_NOT_AUTHORIZED_TO_REGISTER_A_RECIPE = 842; // Ð£Ñ€Ð¾Ð²ÐµÐ½ÑŒ
    // ÑƒÐ¼ÐµÐ½Ð¸Ñ�
    // Ñ�Ð¾Ð·Ð´Ð°Ð½Ð¸Ñ�
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ð°
    // Ñ�Ð»Ð¸ÑˆÐºÐ¾Ð¼
    // Ð½Ð¸Ð·Ð¾Ðº,
    // Ñ‡Ñ‚Ð¾Ð±Ñ‹
    // Ð·Ð°Ñ€ÐµÐ³Ð¸Ñ�Ñ‚Ñ€Ð¸Ñ€Ð¾Ð²Ð°Ñ‚ÑŒ
    // Ð’Ð°Ñˆ
    // Ñ€ÐµÑ†ÐµÐ¿Ñ‚.
    public static final int THE_SIEGE_OF_S1_IS_FINISHED = 843; // ÐžÑ�Ð°Ð´Ð° $s1
    // Ð·Ð°Ð²ÐµÑ€ÑˆÐµÐ½Ð°.
    public static final int THE_SIEGE_TO_CONQUER_S1_HAS_BEGUN = 844; // ÐžÑ�Ð°Ð´Ð°
    // $s1
    // Ð½Ð°Ñ‡Ð°Ð»Ð°Ñ�ÑŒ.
    public static final int THE_DEADLINE_TO_REGISTER_FOR_THE_SIEGE_OF_S1_HAS_PASSED = 845; // Ð’Ñ€ÐµÐ¼Ñ�
    // Ñ€ÐµÐ³Ð¸Ñ�Ñ‚Ñ€Ð°Ñ†Ð¸Ð¸
    // Ð½Ð°
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð¸Ðµ
    // Ð²
    // Ð¾Ñ�Ð°Ð´Ðµ
    // $s1
    // Ð¸Ñ�Ñ‚ÐµÐºÐ»Ð¾.
    public static final int THE_SIEGE_OF_S1_HAS_BEEN_CANCELED_DUE_TO_LACK_OF_INTEREST = 846; // Ð–ÐµÐ»Ð°ÑŽÑ‰Ð¸Ñ…
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð²Ð¾Ð²Ð°Ñ‚ÑŒ
    // Ð½ÐµÑ‚,
    // Ð¾Ñ�Ð°Ð´Ð°
    // $s1
    // Ð¾Ñ‚Ð¼ÐµÐ½ÐµÐ½Ð°.
    public static final int A_CLAN_THAT_OWNS_A_CLAN_HALL_MAY_NOT_PARTICIPATE_IN_A_CLAN_HALL_SIEGE = 847; // ÐšÐ»Ð°Ð½,
    // Ð¾Ð±Ð»Ð°Ð´Ð°ÑŽÑ‰Ð¸Ð¹
    // Ñ…Ð¾Ð»Ð»Ð¾Ð¼,
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð²Ð¾Ð²Ð°Ñ‚ÑŒ
    // Ð²
    // Ð±Ð¸Ñ‚Ð²Ðµ
    // Ð·Ð°
    // Ñ…Ð¾Ð»Ð»
    // ÐºÐ»Ð°Ð½Ð°.
    public static final int S1_HAS_BEEN_DELETED = 848; // $s1 ÑƒÐ´Ð°Ð»ÐµÐ½.
    public static final int S1_CANNOT_BE_FOUND = 849; // $s1 Ð½Ðµ Ð½Ð°Ð¹Ð´ÐµÐ½.
    public static final int S1_ALREADY_EXISTS_1 = 850; // $s1 ÑƒÐ¶Ðµ Ñ�ÑƒÑ‰ÐµÑ�Ñ‚Ð²ÑƒÐµÑ‚.
    public static final int S1_HAS_BEEN_ADDED = 851; // $s1 Ð´Ð¾Ð±Ð°Ð²Ð»ÐµÐ½.
    public static final int THE_RECIPE_IS_INCORRECT = 852; // Ð¡Ð¾Ð´ÐµÑ€Ð¶Ð°Ð½Ð¸Ðµ Ñ€ÐµÑ†ÐµÐ¿Ñ‚Ð°
    // Ð½ÐµÐ²ÐµÑ€Ð½Ð¾.
    public static final int YOU_MAY_NOT_ALTER_YOUR_RECIPE_BOOK_WHILE_ENGAGED_IN_MANUFACTURING = 853; // Ð’
    // Ñ€ÐµÐ¶Ð¸Ð¼Ðµ
    // Ð»Ð¸Ñ‡Ð½Ð¾Ð¹
    // Ð¼Ð°Ñ�Ñ‚ÐµÑ€Ñ�ÐºÐ¾Ð¹
    // ÑƒÐ¿Ñ€Ð°Ð²Ð»Ñ�Ñ‚ÑŒ
    // ÐºÐ½Ð¸Ð³Ð¾Ð¹
    // Ñ€ÐµÑ†ÐµÐ¿Ñ‚Ð¾Ð²
    // Ð½ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾.
    public static final int YOU_LACK_S2_OF_S1 = 854; // Ð�Ðµ Ñ…Ð²Ð°Ñ‚Ð°ÐµÑ‚ Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ð° $s1
    // ($s2 ÑˆÑ‚.)
    public static final int S1_CLAN_HAS_DEFEATED_S2 = 855; // ÐšÐ»Ð°Ð½ $s1 Ð¿Ð¾Ð±ÐµÐ´Ð¸Ð» Ð²
    // Ð±Ð¸Ñ‚Ð²Ðµ Ð·Ð° Ñ…Ð¾Ð»Ð»
    // ÐºÐ»Ð°Ð½Ð° $s2.
    public static final int THE_SIEGE_OF_S1_HAS_ENDED_IN_A_DRAW = 856; // Ð‘Ð¸Ñ‚Ð²Ð°
    // Ð·Ð°
    // Ñ…Ð¾Ð»Ð»
    // ÐºÐ»Ð°Ð½Ð°
    // $s1
    // Ð·Ð°ÐºÐ¾Ð½Ñ‡Ð¸Ð»Ð°Ñ�ÑŒ
    // Ð²
    // Ð½Ð¸Ñ‡ÑŒÑŽ.
    public static final int S1_CLAN_HAS_WON_IN_THE_PRELIMINARY_MATCH_OF_S2 = 857; // ÐšÐ»Ð°Ð½
    // $s1
    // Ð¿Ð¾Ð±ÐµÐ´Ð¸Ð»
    // Ð²
    // Ð¾Ñ‚Ð±Ð¾Ñ€Ð¾Ñ‡Ð½Ð¾Ð¼
    // Ñ‚ÑƒÑ€Ðµ
    // $s2.
    public static final int THE_PRELIMINARY_MATCH_OF_S1_HAS_ENDED_IN_A_DRAW = 858; // ÐžÑ‚Ð±Ð¾Ñ€Ð¾Ñ‡Ð½Ñ‹Ð¹
    // Ñ‚ÑƒÑ€
    // $s1
    // Ð·Ð°ÐºÐ¾Ð½Ñ‡Ð¸Ð»Ñ�Ñ�
    // Ð²
    // Ð½Ð¸Ñ‡ÑŒÑŽ.
    public static final int PLEASE_REGISTER_A_RECIPE = 859; // Ð—Ð°Ñ€ÐµÐ³Ð¸Ñ�Ñ‚Ñ€Ð¸Ñ€ÑƒÐ¹Ñ‚Ðµ
    // Ñ€ÐµÑ†ÐµÐ¿Ñ‚.
    public static final int YOU_MAY_NOT_BUILD_YOUR_HEADQUARTERS_IN_CLOSE_PROXIMITY_TO_ANOTHER_HEADQUARTERS = 860; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // ÑƒÑ�Ñ‚Ð°Ð½Ð¾Ð²Ð¸Ñ‚ÑŒ
    // ÑˆÑ‚Ð°Ð±
    // Ð²
    // Ñ�Ñ‚Ð¾Ð¼
    // Ð¼ÐµÑ�Ñ‚Ðµ,
    // Ð´Ð¸Ñ�Ñ‚Ð°Ð½Ñ†Ð¸Ñ�
    // Ð´Ð¾
    // Ð´Ñ€ÑƒÐ³Ð¾Ð³Ð¾
    // ÑˆÑ‚Ð°Ð±Ð°
    // Ñ�Ð»Ð¸ÑˆÐºÐ¾Ð¼
    // Ð¼Ð°Ð»Ð°.
    public static final int YOU_HAVE_EXCEEDED_THE_MAXIMUM_NUMBER_OF_MEMOS = 861; // Ð’Ñ‹
    // Ð¿Ñ€ÐµÐ²Ñ‹Ñ�Ð¸Ð»Ð¸
    // Ð¼Ð°ÐºÑ�Ð¸Ð¼Ð°Ð»ÑŒÐ½Ð¾Ðµ
    // ÐºÐ¾Ð»Ð¸Ñ‡ÐµÑ�Ñ‚Ð²Ð¾
    // Ð¿Ð°Ð¼Ñ�Ñ‚Ð¾Ðº.
    public static final int ODDS_ARE_NOT_POSTED_UNTIL_TICKET_SALES_HAVE_CLOSED = 862; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¿Ñ€Ð¾Ñ�Ð¼Ð¾Ñ‚Ñ€ÐµÑ‚ÑŒ
    // Ñ�ÑƒÐ¼Ð¼Ñƒ
    // Ð¿Ñ€Ð¸Ð·Ð°,
    // Ñ‚Ð°Ðº
    // ÐºÐ°Ðº
    // Ð¿Ñ€Ð¾Ð´Ð°Ð¶Ð°
    // Ð±Ð¸Ð»ÐµÑ‚Ð¾Ð²
    // ÐµÑ‰Ðµ
    // Ð½Ðµ
    // Ð·Ð°Ð²ÐµÑ€ÑˆÐµÐ½Ð°.
    public static final int YOU_FEEL_THE_ENERGY_OF_FIRE = 863; // Ð’Ñ‹ Ñ‡ÑƒÐ²Ñ�Ñ‚Ð²ÑƒÐµÑ‚Ðµ
    // Ñ�Ð¸Ð»Ñƒ ÐžÐ³Ð½Ñ�.
    public static final int YOU_FEEL_THE_ENERGY_OF_WATER = 864; // Ð’Ñ‹ Ñ‡ÑƒÐ²Ñ�Ñ‚Ð²ÑƒÐµÑ‚Ðµ
    // Ñ�Ð¸Ð»Ñƒ Ð’Ð¾Ð´Ñ‹.
    public static final int YOU_FEEL_THE_ENERGY_OF_WIND = 865; // Ð’Ñ‹ Ñ‡ÑƒÐ²Ñ�Ñ‚Ð²ÑƒÐµÑ‚Ðµ
    // Ñ�Ð¸Ð»Ñƒ Ð’ÐµÑ‚Ñ€Ð°.
    public static final int YOU_MAY_NO_LONGER_GATHER_ENERGY = 866; // Ð’Ñ‹ Ð±Ð¾Ð»ÑŒÑˆÐµ
    // Ð½Ðµ Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¿Ð¾Ð»ÑƒÑ‡Ð°Ñ‚ÑŒ
    // Ñ�Ð¸Ð»Ñƒ.
    public static final int THE_ENERGY_IS_DEPLETED = 867; // Ð¡Ð¸Ð»Ð° Ð¿Ñ€Ð¾Ð¿Ð°Ð»Ð°.
    public static final int THE_ENERGY_OF_FIRE_HAS_BEEN_DELIVERED = 868; // Ð‘Ñ‹Ð»Ð°
    // Ð¿Ð¾Ð»ÑƒÑ‡ÐµÐ½Ð°
    // Ñ�Ð¸Ð»Ð°
    // ÐžÐ³Ð½Ñ�.
    public static final int THE_ENERGY_OF_WATER_HAS_BEEN_DELIVERED = 869; // Ð‘Ñ‹Ð»Ð°
    // Ð¿Ð¾Ð»ÑƒÑ‡ÐµÐ½Ð°
    // Ñ�Ð¸Ð»Ð°
    // Ð’Ð¾Ð´Ñ‹.
    public static final int THE_ENERGY_OF_WIND_HAS_BEEN_DELIVERED = 870; // Ð‘Ñ‹Ð»Ð°
    // Ð¿Ð¾Ð»ÑƒÑ‡ÐµÐ½Ð°
    // Ñ�Ð¸Ð»Ð°
    // Ð’ÐµÑ‚Ñ€Ð°.
    public static final int THE_SEED_HAS_BEEN_SOWN = 871; // Ð¡ÐµÐ¼Ñ� Ð±Ñ‹Ð»Ð¾ Ð¿Ð¾Ñ�ÐµÑ�Ð½Ð¾.
    public static final int THIS_SEED_MAY_NOT_BE_SOWN_HERE = 872; // Ð­Ñ‚Ð¾ Ñ�ÐµÐ¼Ñ�
    // Ð½ÐµÐ»ÑŒÐ·Ñ�
    // Ð¿Ð¾Ñ�ÐµÑ�Ñ‚ÑŒ
    // Ð·Ð´ÐµÑ�ÑŒ.
    public static final int THAT_CHARACTER_DOES_NOT_EXIST = 873; // Ð¢Ð°ÐºÐ¾Ð³Ð¾
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð° Ð½Ðµ
    // Ñ�ÑƒÑ‰ÐµÑ�Ñ‚Ð²ÑƒÐµÑ‚.
    public static final int THE_CAPACITY_OF_THE_WAREHOUSE_HAS_BEEN_EXCEEDED = 874; // Ð¥Ñ€Ð°Ð½Ð¸Ð»Ð¸Ñ‰Ðµ
    // Ð²Ñ‹Ð±Ñ€Ð°Ð½Ð½Ð¾Ð³Ð¾
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°
    // Ð¿ÐµÑ€ÐµÐ¿Ð¾Ð»Ð½ÐµÐ½Ð¾.
    public static final int TRANSPORT_OF_CARGO_HAS_BEEN_CANCELED = 875; // ÐŸÐ¾Ñ�Ñ‹Ð»ÐºÐ°
    // Ð¾Ñ‚Ð¼ÐµÐ½ÐµÐ½Ð°.
    public static final int CARGO_WAS_NOT_DELIVERED = 876; // ÐžÑˆÐ¸Ð±ÐºÐ° Ð¿Ñ€Ð¸
    // Ð¾Ñ‚Ð¿Ñ€Ð°Ð²ÐºÐµ Ð¿Ð¾Ñ�Ñ‹Ð»ÐºÐ¸.
    public static final int THE_SYMBOL_HAS_BEEN_ADDED = 877; // Ð¡Ð¸Ð¼Ð²Ð¾Ð» Ð´Ð¾Ð±Ð°Ð²Ð»ÐµÐ½.
    public static final int THE_SYMBOL_HAS_BEEN_DELETED = 878; // Ð¡Ð¸Ð¼Ð²Ð¾Ð» ÑƒÐ´Ð°Ð»ÐµÐ½.
    public static final int THE_MANOR_SYSTEM_IS_CURRENTLY_UNDER_MAINTENANCE = 879; // Ð¡ÐµÐ¹Ñ‡Ð°Ñ�
    // Ð¿Ñ€Ð¾Ñ…Ð¾Ð´Ð¸Ñ‚
    // Ð¿Ñ€Ð¾Ñ„Ð¸Ð»Ð°ÐºÑ‚Ð¸ÐºÐ°
    // Ñ�Ð¸Ñ�Ñ‚ÐµÐ¼Ñ‹
    // Ð²Ð»Ð°Ð´ÐµÐ½Ð¸Ð¹.
    public static final int THE_TRANSACTION_IS_COMPLETE = 880; // ÐŸÐµÑ€ÐµÐ´Ð°Ñ‡Ð°
    // Ð·Ð°Ð²ÐµÑ€ÑˆÐµÐ½Ð°.
    public static final int THERE_IS_A_DISCREPANCY_ON_THE_INVOICE = 881; // Ð�ÐµÐ²ÐµÑ€Ð½Ð°Ñ�
    // Ð¸Ð½Ñ„Ð¾Ñ€Ð¼Ð°Ñ†Ð¸Ñ�
    // Ð¾
    // Ð¿ÐµÑ€ÐµÐ´Ð°Ð½Ð½Ð¾Ð¼
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ðµ.
    public static final int SEED_QUANTITY_IS_INCORRECT = 882; // Ð�ÐµÐ²ÐµÑ€Ð½Ð¾Ðµ
    // ÐºÐ¾Ð»Ð¸Ñ‡ÐµÑ�Ñ‚Ð²Ð¾
    // Ñ�ÐµÐ¼Ñ�Ð½.
    public static final int SEED_INFORMATION_IS_INCORRECT = 883; // Ð�ÐµÐ²ÐµÑ€Ð½Ð°Ñ�
    // Ð¸Ð½Ñ„Ð¾Ñ€Ð¼Ð°Ñ†Ð¸Ñ� Ð¾
    // Ñ�ÐµÐ¼ÐµÐ½Ð¸.
    public static final int THE_MANOR_INFORMATION_HAS_BEEN_UPDATED = 884; // Ð˜Ð½Ñ„Ð¾Ñ€Ð¼Ð°Ñ†Ð¸Ñ�
    // Ð¾
    // Ð²Ð»Ð°Ð´ÐµÐ½Ð¸Ð¸
    // Ð¾Ð±Ð½Ð¾Ð²Ð»ÐµÐ½Ð°.
    public static final int THE_NUMBER_OF_CROPS_IS_INCORRECT = 885; // ÐšÐ¾Ð»Ð¸Ñ‡ÐµÑ�Ñ‚Ð²Ð¾
    // ÑƒÑ€Ð¾Ð¶Ð°Ñ�
    // Ð½ÐµÐ²ÐµÑ€Ð½Ð¾.
    public static final int THE_CROPS_ARE_PRICED_INCORRECTLY = 886; // Ð�ÐµÐ²ÐµÑ€Ð½Ð°Ñ�
    // Ð¸Ð½Ñ„Ð¾Ñ€Ð¼Ð°Ñ†Ð¸Ñ�
    // Ð¾ Ñ†ÐµÐ½Ðµ
    // ÑƒÑ€Ð¾Ð¶Ð°Ñ�.
    public static final int THE_TYPE_IS_INCORRECT = 887; // Ð�ÐµÐ²ÐµÑ€Ð½Ñ‹Ð¹ Ñ‚Ð¸Ð¿.
    public static final int NO_CROPS_CAN_BE_PURCHASED_AT_THIS_TIME = 888; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¿Ñ€Ð¸Ð¾Ð±Ñ€ÐµÑ�Ñ‚Ð¸
    // ÑƒÑ€Ð¾Ð¶Ð°Ð¹.
    public static final int THE_SEED_WAS_SUCCESSFULLY_SOWN = 889; // Ð’Ñ‹ ÑƒÑ�Ð¿ÐµÑˆÐ½Ð¾
    // Ð¿Ñ€Ð¾Ð²ÐµÐ»Ð¸
    // Ð¿Ð¾Ñ�ÐµÐ².
    public static final int THE_SEED_WAS_NOT_SOWN = 890; // Ð’Ñ‹ Ð½Ðµ Ñ�Ð¼Ð¾Ð³Ð»Ð¸
    // Ð¿Ñ€Ð¾Ð²ÐµÑ�Ñ‚Ð¸ Ð¿Ð¾Ñ�ÐµÐ².
    public static final int YOU_ARE_NOT_AUTHORIZED_TO_HARVEST = 891; // Ð’Ñ‹ Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ñ�Ð¾Ð±Ñ€Ð°Ñ‚ÑŒ
    // ÑƒÑ€Ð¾Ð¶Ð°Ð¹.
    public static final int THE_HARVEST_HAS_FAILED = 892; // Ð’Ñ‹ Ð½Ðµ Ñ�Ð¼Ð¾Ð³Ð»Ð¸
    // Ñ�Ð¾Ð±Ñ€Ð°Ñ‚ÑŒ ÑƒÑ€Ð¾Ð¶Ð°Ð¹.
    public static final int THE_HARVEST_FAILED_BECAUSE_THE_SEED_WAS_NOT_SOWN = 893; // Ð¡Ð±Ð¾Ñ€
    // ÑƒÑ€Ð¾Ð¶Ð°Ñ�
    // Ð½Ðµ
    // Ð¼Ð¾Ð³
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð¿Ñ€Ð¾Ð²ÐµÐ´ÐµÐ½,
    // Ñ‚Ð°Ðº
    // ÐºÐ°Ðº
    // Ð½Ðµ
    // Ð±Ñ‹Ð»Ð¾
    // Ð¿Ð¾Ñ�ÐµÐ²Ð°.
    public static final int UP_TO_S1_RECIPES_CAN_BE_REGISTERED = 894; // ÐœÐ¾Ð¶Ð½Ð¾
    // Ð·Ð°Ñ€ÐµÐ³Ð¸Ñ�Ñ‚Ñ€Ð¸Ñ€Ð¾Ð²Ð°Ñ‚ÑŒ
    // Ñ€ÐµÑ†ÐµÐ¿Ñ‚Ð¾Ð²:
    // $s1.
    public static final int NO_RECIPES_HAVE_BEEN_REGISTERED = 895; // Ð—Ð°Ñ€ÐµÐ³Ð¸Ñ�Ñ‚Ñ€Ð¸Ñ€Ð¾Ð²Ð°Ð½Ð½Ñ‹Ñ…
    // Ñ€ÐµÑ†ÐµÐ¿Ñ‚Ð¾Ð²
    // Ð½ÐµÑ‚.
    public static final int QUEST_RECIPES_CAN_NOT_BE_REGISTERED = 896; // Ð’Ñ‹ Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð·Ð°Ñ€ÐµÐ³Ð¸Ñ�Ñ‚Ñ€Ð¸Ñ€Ð¾Ð²Ð°Ñ‚ÑŒ
    // ÐºÐ²ÐµÑ�Ñ‚Ð¾Ð²Ñ‹Ð¹
    // Ñ€ÐµÑ†ÐµÐ¿Ñ‚.
    public static final int THE_FEE_TO_CREATE_THE_ITEM_IS_INCORRECT = 897; // Ð�ÐµÐ²ÐµÑ€Ð½Ð°Ñ�
    // ÐºÐ¾Ð¼Ð¸Ñ�Ñ�Ð¸Ñ�
    // Ð·Ð°
    // Ð¸Ð·Ð³Ð¾Ñ‚Ð¾Ð²Ð»ÐµÐ½Ð¸Ðµ
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ð°.
    public static final int THE_SYMBOL_CANNOT_BE_DRAWN = 899; // Ð’Ñ‹ Ð½Ðµ Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð½Ð°Ñ€Ð¸Ñ�Ð¾Ð²Ð°Ñ‚ÑŒ
    // Ñ�Ð¸Ð¼Ð²Ð¾Ð».
    public static final int NO_SLOT_EXISTS_TO_DRAW_THE_SYMBOL = 900; // Ð£ Ð’Ð°Ñ�
    // Ð½ÐµÑ‚
    // Ñ�Ñ‡ÐµÐ¹ÐºÐ¸
    // Ð´Ð»Ñ�
    // Ñ€Ð¸Ñ�Ð¾Ð²Ð°Ð½Ð¸Ñ�
    // Ñ�Ð¸Ð¼Ð²Ð¾Ð»Ð¾Ð².
    public static final int THE_SYMBOL_INFORMATION_CANNOT_BE_FOUND = 901; // Ð�ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾
    // Ð½Ð°Ð¹Ñ‚Ð¸
    // Ð¸Ð½Ñ„Ð¾Ñ€Ð¼Ð°Ñ†Ð¸ÑŽ
    // Ð¾
    // Ñ�Ð¸Ð¼Ð²Ð¾Ð»Ð°Ñ….
    public static final int THE_NUMBER_OF_ITEMS_IS_INCORRECT = 902; // ÐšÐ¾Ð»Ð¸Ñ‡ÐµÑ�Ñ‚Ð²Ð¾
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ð¾Ð²
    // Ð½ÐµÐ²ÐµÑ€Ð½Ð¾.
    public static final int YOU_MAY_NOT_SUBMIT_A_PETITION_WHILE_FROZEN_BE_PATIENT = 903; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¿Ð¾Ñ�Ð»Ð°Ñ‚ÑŒ
    // Ð¿ÐµÑ‚Ð¸Ñ†Ð¸ÑŽ
    // Ð²
    // Ð·Ð°Ð¼Ð¾Ñ€Ð¾Ð¶ÐµÐ½Ð½Ð¾Ð¼
    // Ñ�Ð¾Ñ�Ñ‚Ð¾Ñ�Ð½Ð¸Ð¸.
    // ÐŸÐ¾Ð´Ð¾Ð¶Ð´Ð¸Ñ‚Ðµ
    // Ð½ÐµÐ¼Ð½Ð¾Ð³Ð¾.
    public static final int ITEMS_CANNOT_BE_DISCARDED_WHILE_IN_PRIVATE_STORE_STATUS = 904; // Ð’
    // Ñ€ÐµÐ¶Ð¸Ð¼Ðµ
    // Ð»Ð¸Ñ‡Ð½Ð¾Ð¹
    // Ñ‚Ð¾Ñ€Ð³Ð¾Ð²Ð¾Ð¹
    // Ð»Ð°Ð²ÐºÐ¸
    // Ð²Ñ‹Ð±Ñ€Ð¾Ñ�Ð¸Ñ‚ÑŒ
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚
    // Ð½ÐµÐ»ÑŒÐ·Ñ�.
    public static final int THE_CURRENT_SCORE_FOR_THE_HUMAN_RACE_IS_S1 = 905; // Ð ÐµÐ·ÑƒÐ»ÑŒÑ‚Ð°Ñ‚
    // Ð›ÑŽÐ´ÐµÐ¹
    // Ð½Ð°
    // Ð´Ð°Ð½Ð½Ñ‹Ð¹
    // Ð¼Ð¾Ð¼ÐµÐ½Ñ‚
    // -
    // $s1.
    public static final int THE_CURRENT_SCORE_FOR_THE_ELVEN_RACE_IS_S1 = 906; // Ð ÐµÐ·ÑƒÐ»ÑŒÑ‚Ð°Ñ‚
    // Ð­Ð»ÑŒÑ„Ð¾Ð²
    // Ð½Ð°
    // Ð´Ð°Ð½Ð½Ñ‹Ð¹
    // Ð¼Ð¾Ð¼ÐµÐ½Ñ‚
    // -
    // $s1.
    public static final int THE_CURRENT_SCORE_FOR_THE_DARK_ELVEN_RACE_IS_S1 = 907; // Ð ÐµÐ·ÑƒÐ»ÑŒÑ‚Ð°Ñ‚
    // Ð¢ÐµÐ¼Ð½Ñ‹Ñ…
    // Ð­Ð»ÑŒÑ„Ð¾Ð²
    // Ð½Ð°
    // Ð´Ð°Ð½Ð½Ñ‹Ð¹
    // Ð¼Ð¾Ð¼ÐµÐ½Ñ‚
    // -
    // $s1.
    public static final int THE_CURRENT_SCORE_FOR_THE_ORC_RACE_IS_S1 = 908; // Ð ÐµÐ·ÑƒÐ»ÑŒÑ‚Ð°Ñ‚
    // ÐžÑ€ÐºÐ¾Ð²
    // Ð½Ð°
    // Ð´Ð°Ð½Ð½Ñ‹Ð¹
    // Ð¼Ð¾Ð¼ÐµÐ½Ñ‚
    // -
    // $s1.
    public static final int THE_CURRENT_SCORE_FOR_THE_DWARVEN_RACE_IS_S1 = 909; // Ð ÐµÐ·ÑƒÐ»ÑŒÑ‚Ð°Ñ‚
    // Ð“Ð½Ð¾Ð¼Ð¾Ð²
    // Ð½Ð°
    // Ð´Ð°Ð½Ð½Ñ‹Ð¹
    // Ð¼Ð¾Ð¼ÐµÐ½Ñ‚
    // -
    // $s1.
    public static final int CURRENT_LOCATION__S1_S2_S3_NEAR_TALKING_ISLAND_VILLAGE = 910; // Ð¢ÐµÐºÑƒÑ‰Ð°Ñ�
    // Ð»Ð¾ÐºÐ°Ñ†Ð¸Ñ�:
    // $s1,
    // $s2,
    // $s3
    // (Ð²Ð¾Ð·Ð»Ðµ
    // Ð”ÐµÑ€ÐµÐ²Ð½Ð¸
    // Ð“Ð¾Ð²Ð¾Ñ€Ñ�Ñ‰ÐµÐ³Ð¾
    // ÐžÑ�Ñ‚Ñ€Ð¾Ð²Ð°)
    public static final int CURRENT_LOCATION__S1_S2_S3_NEAR_GLUDIN_VILLAGE = 911; // Ð¢ÐµÐºÑƒÑ‰Ð°Ñ�
    // Ð»Ð¾ÐºÐ°Ñ†Ð¸Ñ�:
    // $s1,
    // $s2,
    // $s3
    // (Ð²Ð¾Ð·Ð»Ðµ
    // Ð“Ð»ÑƒÐ´Ð¸Ð½Ð°)
    public static final int CURRENT_LOCATION__S1_S2_S3_NEAR_GLUDIO_CASTLE_TOWN = 912; // Ð¢ÐµÐºÑƒÑ‰Ð°Ñ�
    // Ð»Ð¾ÐºÐ°Ñ†Ð¸Ñ�:
    // $s1,
    // $s2,
    // $s3
    // (Ð²Ð¾Ð·Ð»Ðµ
    // Ð“Ð»ÑƒÐ´Ð¸Ð¾)
    public static final int CURRENT_LOCATION__S1_S2_S3_NEAR_THE_NEUTRAL_ZONE = 913; // Ð¢ÐµÐºÑƒÑ‰Ð°Ñ�
    // Ð»Ð¾ÐºÐ°Ñ†Ð¸Ñ�::
    // $s1,
    // $s2,
    // $s3
    // (Ð²Ð¾Ð·Ð»Ðµ
    // Ð�ÐµÐ¹Ñ‚Ñ€Ð°Ð»ÑŒÐ½Ð¾Ð¹
    // Ð—Ð¾Ð½Ñ‹)
    public static final int CURRENT_LOCATION__S1_S2_S3_NEAR_ELVEN_VILLAGE = 914; // Ð¢ÐµÐºÑƒÑ‰Ð°Ñ�
    // Ð»Ð¾ÐºÐ°Ñ†Ð¸Ñ�:
    // $s1,
    // $s2,
    // $s3
    // (Ð²Ð¾Ð·Ð»Ðµ
    // Ð”ÐµÑ€ÐµÐ²Ð½Ð¸
    // Ð­Ð»ÑŒÑ„Ð¾Ð²)
    public static final int CURRENT_LOCATION__S1_S2_S3_NEAR_DARK_ELVEN_VILLAGE = 915; // Ð¢ÐµÐºÑƒÑ‰Ð°Ñ�
    // Ð»Ð¾ÐºÐ°Ñ†Ð¸Ñ�:
    // $s1,
    // $s2,
    // $s3
    // (Ð²Ð¾Ð·Ð»Ðµ
    // Ð”ÐµÑ€ÐµÐ²Ð½Ð¸
    // Ð¢ÐµÐ¼Ð½Ñ‹Ñ…
    // Ð­Ð»ÑŒÑ„Ð¾Ð²)
    public static final int CURRENT_LOCATION__S1_S2_S3_NEAR_DION_CASTLE_TOWN = 916; // Ð¢ÐµÐºÑƒÑ‰Ð°Ñ�
    // Ð»Ð¾ÐºÐ°Ñ†Ð¸Ñ�:
    // $s1,
    // $s2,
    // $s3
    // (Ð²Ð¾Ð·Ð»Ðµ
    // Ð”Ð¸Ð¾Ð½Ð°)
    public static final int CURRENT_LOCATION__S1_S2_S3_NEAR_FLORAN_VILLAGE = 917; // Ð¢ÐµÐºÑƒÑ‰Ð°Ñ�
    // Ð»Ð¾ÐºÐ°Ñ†Ð¸Ñ�:
    // $s1,
    // $s2,
    // $s3
    // (Ð²Ð¾Ð·Ð»Ðµ
    // Ð¤Ð»Ð¾Ñ€Ð°Ð½Ð°)
    public static final int CURRENT_LOCATION__S1_S2_S3_NEAR_GIRAN_CASTLE_TOWN = 918; // Ð¢ÐµÐºÑƒÑ‰Ð°Ñ�
    // Ð»Ð¾ÐºÐ°Ñ†Ð¸Ñ�:
    // $s1,
    // $s2,
    // $s3
    // (Ð²Ð¾Ð·Ð»Ðµ
    // Ð“Ð¸Ñ€Ð°Ð½Ð°)
    public static final int CURRENT_LOCATION__S1_S2_S3_NEAR_GIRAN_HARBOR = 919; // Ð¢ÐµÐºÑƒÑ‰Ð°Ñ�
    // Ð»Ð¾ÐºÐ°Ñ†Ð¸Ñ�:
    // $s1,
    // $s2,
    // $s3
    // (Ð²Ð¾Ð·Ð»Ðµ
    // Ð“Ð°Ð²Ð°Ð½Ð¸
    // Ð“Ð¸Ñ€Ð°Ð½Ð°)
    public static final int CURRENT_LOCATION__S1_S2_S3_NEAR_ORC_VILLAGE = 920; // Ð¢ÐµÐºÑƒÑ‰Ð°Ñ�
    // Ð»Ð¾ÐºÐ°Ñ†Ð¸Ñ�:
    // $s1,
    // $s2,
    // $s3
    // (Ð²Ð¾Ð·Ð»Ðµ
    // Ð”ÐµÑ€ÐµÐ²Ð½Ð¸
    // ÐžÑ€ÐºÐ¾Ð²)
    public static final int CURRENT_LOCATION__S1_S2_S3_NEAR_DWARVEN_VILLAGE = 921; // Ð¢ÐµÐºÑƒÑ‰Ð°Ñ�
    // Ð»Ð¾ÐºÐ°Ñ†Ð¸Ñ�:
    // $s1,
    // $s2,
    // $s3
    // (Ð²Ð¾Ð·Ð»Ðµ
    // Ð”ÐµÑ€ÐµÐ²Ð½Ð¸
    // Ð“Ð½Ð¾Ð¼Ð¾Ð²)
    public static final int CURRENT_LOCATION__S1_S2_S3_NEAR_THE_TOWN_OF_OREN = 922; // Ð¢ÐµÐºÑƒÑ‰Ð°Ñ�
    // Ð»Ð¾ÐºÐ°Ñ†Ð¸Ñ�:
    // $s1,
    // $s2,
    // $s3
    // (Ð²Ð¾Ð·Ð»Ðµ
    // ÐžÑ€ÐµÐ½Ð°)
    public static final int CURRENT_LOCATION__S1_S2_S3_NEAR_HUNTERS_VILLAGE = 923; // Ð¢ÐµÐºÑƒÑ‰Ð°Ñ�
    // Ð»Ð¾ÐºÐ°Ñ†Ð¸Ñ�:
    // $s1,
    // $s2,
    // $s3
    // (Ð²Ð¾Ð·Ð»Ðµ
    // Ð”ÐµÑ€ÐµÐ²Ð½Ð¸
    // ÐžÑ…Ð¾Ñ‚Ð½Ð¸ÐºÐ¾Ð²)
    public static final int CURRENT_LOCATION__S1_S2_S3_NEAR_ADEN_CASTLE_TOWN = 924; // Ð¢ÐµÐºÑƒÑ‰Ð°Ñ�
    // Ð»Ð¾ÐºÐ°Ñ†Ð¸Ñ�:
    // $s1,
    // $s2,
    // $s3
    // (Ð²Ð¾Ð·Ð»Ðµ
    // Ð�Ð´ÐµÐ½Ð°)
    public static final int CURRENT_LOCATION__S1_S2_S3_NEAR_THE_COLISEUM = 925; // Ð¢ÐµÐºÑƒÑ‰Ð°Ñ�
    // Ð»Ð¾ÐºÐ°Ñ†Ð¸Ñ�:
    // $s1,
    // $s2,
    // $s3
    // (Ð²Ð¾Ð·Ð»Ðµ
    // ÐšÐ¾Ð»Ð¸Ð·ÐµÑ�)
    public static final int CURRENT_LOCATION__S1_S2_S3_NEAR_HEINE = 926; // Ð¢ÐµÐºÑƒÑ‰Ð°Ñ�
    // Ð»Ð¾ÐºÐ°Ñ†Ð¸Ñ�:
    // $s1,
    // $s2,
    // $s3
    // (Ð²Ð¾Ð·Ð»Ðµ
    // Ð¥ÐµÐ¹Ð½Ð°)
    public static final int CURRENT_LOCATION__S1_S2_S3_NEAR_THE_TOWN_OF_SCHUTTGART = 1714; // Ð¢ÐµÐºÑƒÑ‰ÐµÐµ
    // Ð¼ÐµÑ�Ñ‚Ð¾Ð¿Ð¾Ð»Ð¾Ð¶ÐµÐ½Ð¸Ðµ:
    // $s1,
    // $s2,
    // $s3
    // (Ð¾ÐºÑ€ÐµÑ�Ñ‚Ð½Ð¾Ñ�Ñ‚Ð¸
    // Ð¨ÑƒÑ‚Ð³Ð°Ñ€Ñ‚Ð°)
    public static final int CURRENT_LOCATION__S1_S2_S3_NEAR_KAMAEL_VILLAGE = 2189; // Ð¢ÐµÐºÑƒÑ‰Ð°Ñ�
    // Ð»Ð¾ÐºÐ°Ñ†Ð¸Ñ�:
    // $s1,
    // $s2,
    // $s3
    // (Ð’Ð¾Ð·Ð»Ðµ
    // Ð”ÐµÑ€ÐµÐ²Ð½Ð¸
    // ÐšÐ°Ð¼Ð°Ñ�Ð»ÑŒ)
    public static final int CURRENT_LOCATION__S1_S2_S3_NEAR_PRIMEVAL_ISLE = 1924; // Ð¢ÐµÐºÑƒÑ‰Ð°Ñ�
    // Ð»Ð¾ÐºÐ°Ñ†Ð¸Ñ�:
    // $s1,
    // $s2,
    // $s3
    // (Ð²Ð¾Ð·Ð»Ðµ
    // ÐŸÐµÑ€Ð²Ð¾Ð±Ñ‹Ñ‚Ð½Ð¾Ð³Ð¾
    // ÐžÑ�Ñ‚Ñ€Ð¾Ð²Ð°).
    public static final int CURRENT_LOCATION__S1_S2_S3_NEAR_SOUTH_OF_WASTELANDS_CAP = 2190; // Ð¢ÐµÐºÑƒÑ‰Ð°Ñ�
    // Ð»Ð¾ÐºÐ°Ñ†Ð¸Ñ�:
    // $s1,
    // $s2,
    // $s3
    // (Ð’Ð¾Ð·Ð»Ðµ
    // Ð›Ð°Ð³ÐµÑ€Ñ�
    // ÐŸÑƒÑ�Ñ‚Ð¾ÑˆÐ¸)
    public static final int CURRENT_LOCATION__S1_S2_S3_NEAR_FANTASY_ISLE = 2259; // Ð¢ÐµÐºÑƒÑ‰Ð°Ñ�
    // Ð»Ð¾ÐºÐ°Ñ†Ð¸Ñ�:
    // $s1,
    // $s2,
    // $s3
    // (Ð²Ð¾Ð·Ð»Ðµ
    // ÐžÑ�Ñ‚Ñ€Ð¾Ð²Ð°
    // Ð“Ñ€ÐµÐ·)
    public static final int THE_CURRENT_TIME_IS_S1S2_IN_THE_DAY = 927; // Ð¢ÐµÐºÑƒÑ‰ÐµÐµ
    // Ð²Ñ€ÐµÐ¼Ñ�:
    // $s1 Ñ‡
    // $s2
    // Ð¼Ð¸Ð½
    // Ð´Ð½Ñ�.
    public static final int THE_CURRENT_TIME_IS_S1S2_IN_THE_NIGHT = 928; // Ð¢ÐµÐºÑƒÑ‰ÐµÐµ
    // Ð²Ñ€ÐµÐ¼Ñ�:
    // $s1
    // Ñ‡
    // $s2
    // Ð¼Ð¸Ð½
    // Ð½Ð¾Ñ‡Ð¸.
    public static final int NO_COMPENSATION_WAS_GIVEN_FOR_THE_FARM_PRODUCTS = 929; // ÐŸÐ¾Ð´Ð°Ñ€ÐºÐ°
    // Ð·Ð°
    // ÑƒÑ€Ð¾Ð¶Ð°Ð¹
    // Ð½Ðµ
    // Ð±ÑƒÐ´ÐµÑ‚.
    public static final int LOTTERY_TICKETS_ARE_NOT_CURRENTLY_BEING_SOLD = 930; // Ð’
    // Ð´Ð°Ð½Ð½Ñ‹Ð¹
    // Ð¼Ð¾Ð¼ÐµÐ½Ñ‚
    // Ð»Ð¾Ñ‚ÐµÑ€ÐµÐ¹Ð½Ñ‹Ðµ
    // Ð±Ð¸Ð»ÐµÑ‚Ñ‹
    // Ð½Ðµ
    // Ð¿Ñ€Ð¾Ð´Ð°ÑŽÑ‚Ñ�Ñ�.
    public static final int THE_WINNING_LOTTERY_TICKET_NUMBER_HAS_NOT_YET_BEEN_ANNOUNCED = 931; // Ð’Ñ‹Ð¸Ð³Ñ€Ñ‹ÑˆÐ½Ñ‹Ð¹
    // Ð»Ð¾Ñ‚ÐµÑ€ÐµÐ¹Ð½Ñ‹Ð¹
    // Ð±Ð¸Ð»ÐµÑ‚
    // ÐµÑ‰Ðµ
    // Ð½Ðµ
    // Ð¾Ð±ÑŠÑ�Ð²Ð»ÐµÐ½.
    public static final int YOU_CANNOT_CHAT_LOCALLY_WHILE_OBSERVING = 932; // Ð’
    // Ñ€ÐµÐ¶Ð¸Ð¼Ðµ
    // Ð·Ñ€Ð¸Ñ‚ÐµÐ»Ñ�
    // Ñ‡Ð°Ñ‚Ð¾Ð¼
    // Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒÑ�Ñ�
    // Ð½ÐµÐ»ÑŒÐ·Ñ�.
    public static final int THE_SEED_PRICING_GREATLY_DIFFERS_FROM_STANDARD_SEED_PRICES = 933; // Ð�ÐµÐ²ÐµÑ€Ð½Ð°Ñ�
    // Ð¸Ð½Ñ„Ð¾Ñ€Ð¼Ð°Ñ†Ð¸Ñ�
    // Ð¾
    // Ñ†ÐµÐ½Ðµ
    // Ñ�ÐµÐ¼Ñ�Ð½.
    public static final int IT_IS_A_DELETED_RECIPE = 934; // Ð£Ð´Ð°Ð»ÐµÐ½Ð½Ñ‹Ð¹ Ñ€ÐµÑ†ÐµÐ¿Ñ‚.
    public static final int THE_AMOUNT_IS_NOT_SUFFICIENT_AND_SO_THE_MANOR_IS_NOT_IN_OPERATION = 935; // Ð£
    // Ð’Ð°Ñ�
    // Ð½Ðµ
    // Ñ…Ð²Ð°Ñ‚Ð°ÐµÑ‚
    // Ð´ÐµÐ½ÐµÐ³
    // Ð´Ð»Ñ�
    // Ð°ÐºÑ‚Ð¸Ð²Ð°Ñ†Ð¸Ð¸
    // Ð²Ð»Ð°Ð´ÐµÐ½Ð¸Ñ�.
    public static final int USE_S1 = 936; // Ð˜Ñ�Ð¿Ð¾Ð»ÑŒÐ·ÑƒÐµÑ‚Ñ�Ñ�: $s1.
    public static final int CURRENTLY_PREPARING_FOR_PRIVATE_WORKSHOP = 937; // Ð˜Ð´ÐµÑ‚
    // Ð¿Ð¾Ð´Ð³Ð¾Ñ‚Ð¾Ð²ÐºÐ°
    // Ð»Ð¸Ñ‡Ð½Ð¾Ð³Ð¾
    // Ñ€Ð°Ð±Ð¾Ñ‡ÐµÐ³Ð¾
    // Ð¼ÐµÑ�Ñ‚Ð°.
    public static final int THE_COMMUNITY_SERVER_IS_CURRENTLY_OFFLINE = 938; // Ð¡ÐµÑ€Ð²ÐµÑ€
    // Ð¾Ð±Ñ‰ÐµÐ½Ð¸Ñ�
    // Ð½Ð°Ñ…Ð¾Ð´Ð¸Ñ‚Ñ�Ñ�
    // Ð½Ð°
    // Ð¿Ñ€Ð¾Ñ„Ð¸Ð»Ð°ÐºÑ‚Ð¸ÐºÐµ.
    public static final int YOU_CANNOT_EXCHANGE_WHILE_BLOCKING_EVERYTHING = 939; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¿Ñ€Ð¾Ð¸Ð·Ð²ÐµÑ�Ñ‚Ð¸
    // Ð¾Ð±Ð¼ÐµÐ½
    // Ð²
    // Ñ€ÐµÐ¶Ð¸Ð¼Ðµ
    // Ð¿Ð¾Ð»Ð½Ð¾Ð¹
    // Ð±Ð»Ð¾ÐºÐ¸Ñ€Ð¾Ð²ÐºÐ¸.
    public static final int S1_IS_BLOCKING_EVERYTHING = 940; // $s1 Ð½Ð°Ñ…Ð¾Ð´Ð¸Ñ‚Ñ�Ñ� Ð²
    // Ñ€ÐµÐ¶Ð¸Ð¼Ðµ Ð¿Ð¾Ð»Ð½Ð¾Ð¹
    // Ð±Ð»Ð¾ÐºÐ¸Ñ€Ð¾Ð²ÐºÐ¸.
    public static final int RESTART_AT_TALKING_ISLAND_VILLAGE = 941; // Ð’Ð¾Ð·Ð²Ñ€Ð°Ñ‰ÐµÐ½Ð¸Ðµ
    // Ð²
    // Ð”ÐµÑ€ÐµÐ²Ð½ÑŽ
    // Ð“Ð¾Ð²Ð¾Ñ€Ñ�Ñ‰ÐµÐ³Ð¾
    // ÐžÑ�Ñ‚Ñ€Ð¾Ð²Ð°
    public static final int RESTART_AT_GLUDIN_VILLAGE = 942; // Ð’Ð¾Ð·Ð²Ñ€Ð°Ñ‰ÐµÐ½Ð¸Ðµ Ð²
    // Ð“Ð»ÑƒÐ´Ð¸Ð½
    public static final int RESTART_AT_GLUDIN_CASTLE_TOWN = 943; // Ð’Ð¾Ð·Ð²Ñ€Ð°Ñ‰ÐµÐ½Ð¸Ðµ
    // Ð² Ð“Ð»ÑƒÐ´Ð¸Ð¾
    public static final int RESTART_AT_THE_NEUTRAL_ZONE = 944; // Ð’Ð¾Ð·Ð²Ñ€Ð°Ñ‰ÐµÐ½Ð¸Ðµ Ð²
    // Ð�ÐµÐ¹Ñ‚Ñ€Ð°Ð»ÑŒÐ½ÑƒÑŽ
    // Ð—Ð¾Ð½Ñƒ
    public static final int RESTART_AT_ELVEN_VILLAGE = 945; // Ð’Ð¾Ð·Ð²Ñ€Ð°Ñ‰ÐµÐ½Ð¸Ðµ Ð²
    // Ð­Ð»ÑŒÑ„Ð¸Ð¹Ñ�ÐºÑƒÑŽ
    // Ð”ÐµÑ€ÐµÐ²Ð½ÑŽ
    public static final int RESTART_AT_DARK_ELVEN_VILLAGE = 946; // Ð’Ð¾Ð·Ð²Ñ€Ð°Ñ‰ÐµÐ½Ð¸Ðµ
    // Ð² Ð”ÐµÑ€ÐµÐ²Ð½ÑŽ
    // Ð¢ÐµÐ¼Ð½Ñ‹Ñ…
    // Ð­Ð»ÑŒÑ„Ð¾Ð²
    public static final int RESTART_AT_DION_CASTLE_TOWN = 947; // Ð’Ð¾Ð·Ð²Ñ€Ð°Ñ‰ÐµÐ½Ð¸Ðµ Ð²
    // Ð”Ð¸Ð¾Ð½
    public static final int RESTART_AT_FLORAN_VILLAGE = 948; // Ð’Ð¾Ð·Ð²Ñ€Ð°Ñ‰ÐµÐ½Ð¸Ðµ Ð²Ð¾
    // Ð¤Ð»Ð¾Ñ€Ð°Ð½
    public static final int RESTART_AT_GIRAN_CASTLE_TOWN = 949; // Ð’Ð¾Ð·Ð²Ñ€Ð°Ñ‰ÐµÐ½Ð¸Ðµ Ð²
    // Ð“Ð¸Ñ€Ð°Ð½
    public static final int RESTART_AT_GIRAN_HARBOR = 950; // Ð’Ð¾Ð·Ð²Ñ€Ð°Ñ‰ÐµÐ½Ð¸Ðµ Ð²
    // Ð“Ð°Ð²Ð°Ð½ÑŒ Ð“Ð¸Ñ€Ð°Ð½Ð°
    public static final int RESTART_AT_ORC_VILLAGE = 951; // Ð’Ð¾Ð·Ð²Ñ€Ð°Ñ‰ÐµÐ½Ð¸Ðµ Ð²
    // Ð”ÐµÑ€ÐµÐ²Ð½ÑŽ ÐžÑ€ÐºÐ¾Ð²
    public static final int RESTART_AT_DWARVEN_VILLAGE = 952; // Ð’Ð¾Ð·Ð²Ñ€Ð°Ñ‰ÐµÐ½Ð¸Ðµ Ð²
    // Ð”ÐµÑ€ÐµÐ²Ð½ÑŽ Ð“Ð½Ð¾Ð¼Ð¾Ð²
    public static final int RESTART_AT_THE_TOWN_OF_OREN = 953; // Ð’Ð¾Ð·Ð²Ñ€Ð°Ñ‰ÐµÐ½Ð¸Ðµ Ð²
    // ÐžÑ€ÐµÐ½
    public static final int RESTART_AT_HUNTERS_VILLAGE = 954; // Ð’Ð¾Ð·Ð²Ñ€Ð°Ñ‰ÐµÐ½Ð¸Ðµ Ð²
    // Ð”ÐµÑ€ÐµÐ²Ð½ÑŽ
    // ÐžÑ…Ð¾Ñ‚Ð½Ð¸ÐºÐ¾Ð²
    public static final int RESTART_AT_ADEN_CASTLE_TOWN = 955; // Ð’Ð¾Ð·Ð²Ñ€Ð°Ñ‰ÐµÐ½Ð¸Ðµ Ð²
    // Ð�Ð´ÐµÐ½
    public static final int RESTART_AT_THE_COLISEUM = 956; // Ð’Ð¾Ð·Ð²Ñ€Ð°Ñ‰ÐµÐ½Ð¸Ðµ Ð²
    // ÐšÐ¾Ð»Ð¸Ð·ÐµÐ¹
    public static final int RESTART_AT_HEINE = 957; // Ð’Ð¾Ð·Ð²Ñ€Ð°Ñ‰ÐµÐ½Ð¸Ðµ Ð² Ð¥ÐµÐ¹Ð½
    public static final int ITEMS_CANNOT_BE_DISCARDED_OR_DESTROYED_WHILE_OPERATING_A_PRIVATE_STORE_OR_WORKSHOP = 958; // ÐŸÑ€ÐµÐ´Ð¼ÐµÑ‚
    // Ð½ÐµÐ»ÑŒÐ·Ñ�
    // Ð²Ñ‹Ð±Ñ€Ð¾Ñ�Ð¸Ñ‚ÑŒ
    // Ð¸Ð»Ð¸
    // ÑƒÐ½Ð¸Ñ‡Ñ‚Ð¾Ð¶Ð¸Ñ‚ÑŒ
    // Ð²
    // Ñ€ÐµÐ¶Ð¸Ð¼Ðµ
    // Ð»Ð¸Ñ‡Ð½Ð¾Ð¹
    // Ñ‚Ð¾Ñ€Ð³Ð¾Ð²Ð¾Ð¹
    // Ð»Ð°Ð²ÐºÐ¸
    // Ð¸Ð»Ð¸
    // Ð¼Ð°Ñ�Ñ‚ÐµÑ€Ñ�ÐºÐ¾Ð¹.
    public static final int S1_S2_MANUFACTURING_SUCCESS = 959; // $s1 (*$s2):
    // Ð¸Ð·Ð³Ð¾Ñ‚Ð¾Ð²Ð»ÐµÐ½Ð¾
    // ÑƒÐ´Ð°Ñ‡Ð½Ð¾.
    public static final int S1_MANUFACTURING_FAILURE = 960; // $s1: Ð½ÐµÑƒÐ´Ð°Ñ‡Ð°.
    public static final int YOU_ARE_NOW_BLOCKING_EVERYTHING = 961; // Ð’Ñ‹
    // Ð½Ð°Ñ…Ð¾Ð´Ð¸Ñ‚ÐµÑ�ÑŒ
    // Ð² Ñ€ÐµÐ¶Ð¸Ð¼Ðµ
    // Ð¿Ð¾Ð»Ð½Ð¾Ð¹
    // Ð±Ð»Ð¾ÐºÐ¸Ñ€Ð¾Ð²ÐºÐ¸.
    public static final int YOU_ARE_NO_LONGER_BLOCKING_EVERYTHING = 962; // Ð’Ñ‹
    // Ð²Ñ‹ÑˆÐ»Ð¸
    // Ð¸Ð·
    // Ñ€ÐµÐ¶Ð¸Ð¼Ð°
    // Ð¿Ð¾Ð»Ð½Ð¾Ð¹
    // Ð±Ð»Ð¾ÐºÐ¸Ñ€Ð¾Ð²ÐºÐ¸.
    public static final int PLEASE_DETERMINE_THE_MANUFACTURING_PRICE = 963; // Ð’Ñ‹Ð±ÐµÑ€Ð¸Ñ‚Ðµ
    // Ñ†ÐµÐ½Ñƒ
    // Ð¸Ð·Ð³Ð¾Ñ‚Ð¾Ð²Ð»ÐµÐ½Ð¸Ñ�.
    public static final int CHATTING_IS_PROHIBITED_FOR_ABOUT_ONE_MINUTE = 964; // Ð§Ð°Ñ‚
    // Ð·Ð°Ð±Ð»Ð¾ÐºÐ¸Ñ€Ð¾Ð²Ð°Ð½
    // Ð½Ð°
    // 1
    // Ð¼Ð¸Ð½.
    public static final int THE_CHATTING_PROHIBITION_HAS_BEEN_REMOVED = 965; // Ð‘Ð»Ð¾ÐºÐ¸Ñ€Ð¾Ð²ÐºÐ°
    // Ñ‡Ð°Ñ‚Ð°
    // Ñ�Ð½Ñ�Ñ‚Ð°.
    public static final int CHATTING_IS_CURRENTLY_PROHIBITED_IF_YOU_TRY_TO_CHAT_BEFORE_THE_PROHIBITION_IS_REMOVED_THE_PROHIBITION_TIME_WILL_BECOME_EVEN_LONGER = 966; // Ð‘Ð»Ð¾ÐºÐ¸Ñ€Ð¾Ð²ÐºÐ°
    // Ñ‡Ð°Ñ‚Ð°.
    // Ð’
    // Ñ�Ð»ÑƒÑ‡Ð°Ðµ
    // Ð¿Ð¾Ð¿Ñ‹Ñ‚ÐºÐ¸
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ð½Ð¸Ñ�
    // Ñ‡Ð°Ñ‚Ð°
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ð±Ð»Ð¾ÐºÐ¸Ñ€Ð¾Ð²ÐºÐ¸
    // Ð±ÑƒÐ´ÐµÑ‚
    // Ð¿Ñ€Ð¾Ð´Ð»ÐµÐ½Ð¾.
    public static final int DO_YOU_ACCEPT_THE_PARTY_INVITATION_FROM_S1_ITEM_DISTRIBUTION_RANDOM_INCLUDING_SPOIL = 967; // Ð’Ñ‹
    // Ñ�Ð¾Ð³Ð»Ð°Ñ�Ð½Ñ‹
    // Ð²Ñ�Ñ‚ÑƒÐ¿Ð¸Ñ‚ÑŒ
    // Ð²
    // Ð³Ñ€ÑƒÐ¿Ð¿Ñƒ
    // Ñ�
    // $c1?
    // (Ñ€Ð°Ñ�Ð¿Ñ€ÐµÐ´ÐµÐ»ÐµÐ½Ð¸Ðµ
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ð¾Ð²:
    // Ð¡Ð»ÑƒÑ‡Ð°Ð¹Ð½Ð¾+ÐŸÑ€Ð¸Ñ�Ð²Ð¾Ð¸Ñ‚ÑŒ)
    public static final int DO_YOU_ACCEPT_THE_PARTY_INVITATION_FROM_S1_ITEM_DISTRIBUTION_BY_TURN = 968; // Ð’Ñ‹
    // Ñ�Ð¾Ð³Ð»Ð°Ñ�Ð½Ñ‹
    // Ð²Ñ�Ñ‚ÑƒÐ¿Ð¸Ñ‚ÑŒ
    // Ð²
    // Ð³Ñ€ÑƒÐ¿Ð¿Ñƒ
    // Ñ�
    // $c1?
    // (Ñ€Ð°Ñ�Ð¿Ñ€ÐµÐ´ÐµÐ»ÐµÐ½Ð¸Ðµ
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ð¾Ð²:
    // ÐŸÐ¾
    // Ð¾Ñ‡ÐµÑ€ÐµÐ´Ð¸)
    public static final int DO_YOU_ACCEPT_THE_PARTY_INVITATION_FROM_S1_ITEM_DISTRIBUTION_BY_TURN_INCLUDING_SPOIL = 969; // Ð’Ñ‹
    // Ñ�Ð¾Ð³Ð»Ð°Ñ�Ð½Ñ‹
    // Ð²Ñ�Ñ‚ÑƒÐ¿Ð¸Ñ‚ÑŒ
    // Ð²
    // Ð³Ñ€ÑƒÐ¿Ð¿Ñƒ
    // Ñ�
    // $c1?
    // (Ñ€Ð°Ñ�Ð¿Ñ€ÐµÐ´ÐµÐ»ÐµÐ½Ð¸Ðµ
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ð¾Ð²:
    // ÐŸÐ¾
    // Ð¾Ñ‡ÐµÑ€ÐµÐ´Ð¸+ÐŸÑ€Ð¸Ñ�Ð²Ð¾Ð¸Ñ‚ÑŒ)
    public static final int S2S_MP_HAS_BEEN_DRAINED_BY_S1 = 970; // $s2 MP Ð±Ñ‹Ð»Ð¾
    // Ð¿Ð¾Ð³Ð»Ð¾Ñ‰ÐµÐ½Ð¾
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶ÐµÐ¼
    // $c1.
    public static final int PETITIONS_CANNOT_EXCEED_255_CHARACTERS = 971; // ÐžÐ±Ñ€Ð°Ñ‰ÐµÐ½Ð¸Ðµ
    // Ð½Ðµ
    // Ð´Ð¾Ð»Ð¶Ð½Ð¾
    // Ð¿Ñ€ÐµÐ²Ñ‹ÑˆÐ°Ñ‚ÑŒ
    // 255
    // Ñ�Ð¸Ð¼Ð²Ð¾Ð»Ð¾Ð².
    public static final int PETS_CANNOT_USE_THIS_ITEM = 972; // ÐŸÐ¸Ñ‚Ð¾Ð¼ÐµÑ† Ð½Ðµ Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð²Ð¾Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒÑ�Ñ�
    // Ñ�Ñ‚Ð¸Ð¼ Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ð¾Ð¼.
    public static final int PLEASE_INPUT_NO_MORE_THAN_THE_NUMBER_YOU_HAVE = 973; // Ð’Ð²ÐµÐ´Ð¸Ñ‚Ðµ
    // ÐºÐ¾Ð»Ð¸Ñ‡ÐµÑ�Ñ‚Ð²Ð¾,
    // Ð½Ðµ
    // Ð¿Ñ€ÐµÐ²Ñ‹ÑˆÐ°ÑŽÑ‰ÐµÐµ
    // Ñ‚Ð¾,
    // Ñ‡Ñ‚Ð¾
    // Ñƒ
    // Ð’Ð°Ñ�
    // ÐµÑ�Ñ‚ÑŒ.
    public static final int THE_SOUL_CRYSTAL_SUCCEEDED_IN_ABSORBING_A_SOUL = 974; // ÐšÑ€Ð¸Ñ�Ñ‚Ð°Ð»Ð»
    // Ð´ÑƒÑˆÐ¸
    // Ð¿Ð¾Ð³Ð»Ð¾Ñ‚Ð¸Ð»
    // Ð´ÑƒÑˆÑƒ.
    public static final int THE_SOUL_CRYSTAL_WAS_NOT_ABLE_TO_ABSORB_A_SOUL = 975; // ÐšÑ€Ð¸Ñ�Ñ‚Ð°Ð»Ð»
    // Ð´ÑƒÑˆÐ¸
    // Ð½Ðµ
    // Ñ�Ð¼Ð¾Ð³
    // Ð¿Ð¾Ð³Ð»Ð¾Ñ‚Ð¸Ñ‚ÑŒ
    // Ð´ÑƒÑˆÑƒ.
    public static final int THE_SOUL_CRYSTAL_BROKE_BECAUSE_IT_WAS_NOT_ABLE_TO_ENDURE_THE_SOUL_ENERGY = 976; // ÐšÑ€Ð¸Ñ�Ñ‚Ð°Ð»Ð»
    // Ð´ÑƒÑˆÐ¸
    // Ð¸Ñ�Ð¿Ð¾Ñ€Ñ‡ÐµÐ½,
    // Ñ‚Ð°Ðº
    // ÐºÐ°Ðº
    // Ð½Ðµ
    // Ñ�Ð¼Ð¾Ð³
    // Ñ�Ð´ÐµÑ€Ð¶Ð°Ñ‚ÑŒ
    // Ñ�Ð¸Ð»Ñƒ
    // Ð¿Ð¾Ð³Ð»Ð¾Ñ‰ÐµÐ½Ð½Ñ‹Ñ…
    // Ð¸Ð¼
    // Ð´ÑƒÑˆ.
    public static final int THE_SOUL_CRYSTALS_CAUSED_RESONATION_AND_FAILED_AT_ABSORBING_A_SOUL = 977; // ÐšÑ€Ð¸Ñ�Ñ‚Ð°Ð»Ð»
    // Ð´ÑƒÑˆÐ¸
    // Ð½Ðµ
    // Ñ�Ð¼Ð¾Ð³
    // Ð¿Ð¾Ð³Ð»Ð¾Ñ‚Ð¸Ñ‚ÑŒ
    // Ð´ÑƒÑˆÑƒ,
    // Ð¸Ð·-Ð·Ð°
    // Ñ‚Ð¾Ð³Ð¾
    // Ñ‡Ñ‚Ð¾
    // ÐºÑ€Ð¸Ñ�Ñ‚Ð°Ð»Ð»Ñ‹
    // Ð´ÑƒÑˆÐ¸
    // Ð²Ñ‹Ð·Ð²Ð°Ð»Ð¸
    // Ñ€ÐµÐ·Ð¾Ð½Ð°Ð½Ñ�.
    public static final int THE_SOUL_CRYSTAL_IS_REFUSING_TO_ABSORB_A_SOUL = 978; // ÐšÑ€Ð¸Ñ�Ñ‚Ð°Ð»Ð»
    // Ð´ÑƒÑˆÐ¸
    // Ð¾Ñ‚ÐºÐ°Ð·Ñ‹Ð²Ð°ÐµÑ‚Ñ�Ñ�
    // Ð¾Ñ‚
    // Ð¿Ð¾Ð³Ð»Ð¾Ñ‰ÐµÐ½Ð¸Ñ�
    // Ð´ÑƒÑˆÐ¸.
    public static final int ARRIVED_AT_TALKING_ISLAND_HARBOR = 979; // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ
    // Ð´Ð¾Ñ�Ñ‚Ð¸Ð³
    // Ð“Ð¾Ð²Ð¾Ñ€Ñ�Ñ‰ÐµÐ³Ð¾
    // ÐžÑ�Ñ‚Ñ€Ð¾Ð²Ð°.
    public static final int WILL_LEAVE_FOR_GLUDIN_HARBOR_AFTER_ANCHORING_FOR_TEN_MINUTES = 980; // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ
    // Ð¾Ñ‚Ð¿Ñ€Ð°Ð²Ð¸Ñ‚Ñ�Ñ�
    // Ð²
    // Ð³Ð°Ð²Ð°Ð½ÑŒ
    // Ð“Ð»ÑƒÐ´Ð¸Ð½Ð°
    // Ñ‡ÐµÑ€ÐµÐ·
    // 10
    // Ð¼Ð¸Ð½.
    public static final int WILL_LEAVE_FOR_GLUDIN_HARBOR_IN_FIVE_MINUTES = 981; // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ
    // Ð¾Ñ‚Ð¿Ñ€Ð°Ð²Ð¸Ñ‚Ñ�Ñ�
    // Ð²
    // Ð³Ð°Ð²Ð°Ð½ÑŒ
    // Ð“Ð»ÑƒÐ´Ð¸Ð½Ð°
    // Ñ‡ÐµÑ€ÐµÐ·
    // 5
    // Ð¼Ð¸Ð½.
    public static final int WILL_LEAVE_FOR_GLUDIN_HARBOR_IN_ONE_MINUTE = 982; // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ
    // Ð¾Ñ‚Ð¿Ñ€Ð°Ð²Ð¸Ñ‚Ñ�Ñ�
    // Ð²
    // Ð³Ð°Ð²Ð°Ð½ÑŒ
    // Ð“Ð»ÑƒÐ´Ð¸Ð½Ð°
    // Ñ‡ÐµÑ€ÐµÐ·
    // 1
    // Ð¼Ð¸Ð½.
    public static final int THOSE_WISHING_TO_RIDE_SHOULD_MAKE_HASTE_TO_GET_ON = 983; // Ð–ÐµÐ»Ð°ÑŽÑ‰Ð¸Ðµ
    // ÑƒÐµÑ…Ð°Ñ‚ÑŒ,
    // Ð¿Ð¾Ñ‚Ð¾Ñ€Ð¾Ð¿Ð¸Ñ‚ÐµÑ�ÑŒ.
    public static final int LEAVING_SOON_FOR_GLUDIN_HARBOR = 984; // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ
    // Ñ�ÐºÐ¾Ñ€Ð¾
    // Ð¾Ñ‚Ð¿Ñ€Ð°Ð²Ð»Ñ�ÐµÑ‚Ñ�Ñ�
    // Ð² Ð³Ð°Ð²Ð°Ð½ÑŒ
    // Ð“Ð»ÑƒÐ´Ð¸Ð½Ð°.
    public static final int LEAVING_FOR_GLUDIN_HARBOR = 985; // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ
    // Ð¾Ñ‚Ð¿Ñ€Ð°Ð²Ð»Ñ�ÐµÑ‚Ñ�Ñ� Ð²
    // Ð³Ð°Ð²Ð°Ð½ÑŒ Ð“Ð»ÑƒÐ´Ð¸Ð½Ð°.
    public static final int ARRIVED_AT_GLUDIN_HARBOR = 986; // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ Ð²Ð¾ÑˆÐµÐ» Ð²
    // Ð³Ð°Ð²Ð°Ð½ÑŒ Ð“Ð»ÑƒÐ´Ð¸Ð½Ð°.
    public static final int WILL_LEAVE_FOR_TALKING_ISLAND_HARBOR_AFTER_ANCHORING_FOR_TEN_MINUTES = 987; // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ
    // Ð¾Ñ‚Ð¿Ñ€Ð°Ð²Ð¸Ñ‚Ñ�Ñ�
    // Ð½Ð°
    // Ð“Ð¾Ð²Ð¾Ñ€Ñ�Ñ‰Ð¸Ð¹
    // ÐžÑ�Ñ‚Ñ€Ð¾Ð²
    // Ñ‡ÐµÑ€ÐµÐ·
    // 10
    // Ð¼Ð¸Ð½.
    public static final int WILL_LEAVE_FOR_TALKING_ISLAND_HARBOR_IN_FIVE_MINUTES = 988; // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ
    // Ð¾Ñ‚Ð¿Ñ€Ð°Ð²Ð¸Ñ‚Ñ�Ñ�
    // Ð½Ð°
    // Ð“Ð¾Ð²Ð¾Ñ€Ñ�Ñ‰Ð¸Ð¹
    // ÐžÑ�Ñ‚Ñ€Ð¾Ð²
    // Ñ‡ÐµÑ€ÐµÐ·
    // 5
    // Ð¼Ð¸Ð½.
    public static final int WILL_LEAVE_FOR_TALKING_ISLAND_HARBOR_IN_ONE_MINUTE = 989; // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ
    // Ð¾Ñ‚Ð¿Ñ€Ð°Ð²Ð¸Ñ‚Ñ�Ñ�
    // Ð½Ð°
    // Ð“Ð¾Ð²Ð¾Ñ€Ñ�Ñ‰Ð¸Ð¹
    // ÐžÑ�Ñ‚Ñ€Ð¾Ð²
    // Ñ‡ÐµÑ€ÐµÐ·
    // 1
    // Ð¼Ð¸Ð½.
    public static final int LEAVING_SOON_FOR_TALKING_ISLAND_HARBOR = 990; // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ
    // Ñ�ÐºÐ¾Ñ€Ð¾
    // Ð¾Ñ‚Ð±ÑƒÐ´ÐµÑ‚
    // Ðº
    // Ð“Ð¾Ð²Ð¾Ñ€Ñ�Ñ‰ÐµÐ¼Ñƒ
    // ÐžÑ�Ñ‚Ñ€Ð¾Ð²Ñƒ.
    public static final int LEAVING_FOR_TALKING_ISLAND_HARBOR = 991; // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ
    // Ð¾Ñ‚Ð¿Ñ€Ð°Ð²Ð»Ñ�ÐµÑ‚Ñ�Ñ�
    // Ðº
    // Ð“Ð¾Ð²Ð¾Ñ€Ñ�Ñ‰ÐµÐ¼Ñƒ
    // ÐžÑ�Ñ‚Ñ€Ð¾Ð²Ñƒ.
    public static final int ARRIVED_AT_GIRAN_HARBOR = 992; // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ Ð¿Ñ€Ð¸Ð±Ñ‹Ð» Ð²
    // Ð³Ð°Ð²Ð°Ð½ÑŒ Ð“Ð¸Ñ€Ð°Ð½Ð°.
    public static final int WILL_LEAVE_FOR_GIRAN_HARBOR_AFTER_ANCHORING_FOR_TEN_MINUTES = 993; // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ
    // Ð¿Ñ€Ð¾Ð´Ð¾Ð»Ð¶Ð¸Ñ‚
    // Ð¿ÑƒÑ‚ÑŒ
    // Ðº
    // Ð³Ð°Ð²Ð°Ð½Ð¸
    // Ð“Ð¸Ñ€Ð°Ð½Ð°
    // Ñ‡ÐµÑ€ÐµÐ·
    // 10
    // Ð¼Ð¸Ð½.
    public static final int WILL_LEAVE_FOR_GIRAN_HARBOR_IN_FIVE_MINUTES = 994; // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ
    // Ð¾Ñ‚Ð¿Ñ€Ð°Ð²Ð»Ñ�ÐµÑ‚Ñ�Ñ�
    // Ðº
    // Ð³Ð°Ð²Ð°Ð½Ð¸
    // Ð“Ð¸Ñ€Ð°Ð½Ð°
    // Ñ‡ÐµÑ€ÐµÐ·
    // 5
    // Ð¼Ð¸Ð½.
    public static final int WILL_LEAVE_FOR_GIRAN_HARBOR_IN_ONE_MINUTE = 995; // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ
    // Ð¾Ñ‚Ð¿Ñ€Ð°Ð²Ð»Ñ�ÐµÑ‚Ñ�Ñ�
    // Ðº
    // Ð³Ð°Ð²Ð°Ð½Ð¸
    // Ð“Ð¸Ñ€Ð°Ð½Ð°
    // Ñ‡ÐµÑ€ÐµÐ·
    // 1
    // Ð¼Ð¸Ð½.
    public static final int LEAVING_SOON_FOR_GIRAN_HARBOR = 996; // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ
    // Ñ�ÐºÐ¾Ñ€Ð¾
    // Ð¾Ñ‚Ð¿Ñ€Ð°Ð²Ð»Ñ�ÐµÑ‚Ñ�Ñ�
    // Ðº Ð³Ð°Ð²Ð°Ð½Ð¸
    // Ð“Ð¸Ñ€Ð°Ð½Ð°.
    public static final int LEAVING_FOR_GIRAN_HARBOR = 997; // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ
    // Ð¾Ñ‚Ð¿Ñ€Ð°Ð²Ð»Ñ�ÐµÑ‚Ñ�Ñ� Ðº
    // Ð³Ð°Ð²Ð°Ð½Ð¸ Ð“Ð¸Ñ€Ð°Ð½Ð°.
    public static final int THE_INNADRIL_PLEASURE_BOAT_HAS_ARRIVED_IT_WILL_ANCHOR_FOR_TEN_MINUTES = 998; // ÐŸÑ€Ð¸Ð±Ñ‹Ð»
    // ÐºÐ¾Ñ€Ð°Ð±Ð»ÑŒ
    // Ð¸Ð·
    // Ð˜Ð½Ð½Ð°Ð´Ñ€Ð¸Ð»Ð°.
    // Ð¡Ñ‚Ð¾Ñ�Ð½ÐºÐ°
    // 10
    // Ð¼Ð¸Ð½.
    public static final int THE_INNADRIL_PLEASURE_BOAT_WILL_LEAVE_IN_FIVE_MINUTES = 999; // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ
    // Ð²
    // Ð˜Ð½Ð½Ð°Ð´Ñ€Ð¸Ð»
    // Ð¾Ñ‚Ð±Ñ‹Ð²Ð°ÐµÑ‚
    // Ñ‡ÐµÑ€ÐµÐ·
    // 5
    // Ð¼Ð¸Ð½.
    public static final int THE_INNADRIL_PLEASURE_BOAT_WILL_LEAVE_IN_ONE_MINUTE = 1000; // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ
    // Ð²
    // Ð˜Ð½Ð½Ð°Ð´Ñ€Ð¸Ð»
    // Ð¾Ñ‚Ð±Ñ‹Ð²Ð°ÐµÑ‚
    // Ñ‡ÐµÑ€ÐµÐ·
    // 1
    // Ð¼Ð¸Ð½.
    public static final int INNADRIL_PLEASURE_BOAT_IS_LEAVING_SOON = 1001; // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ
    // Ð²
    // Ð˜Ð½Ð½Ð°Ð´Ñ€Ð¸Ð»
    // Ñ�ÐºÐ¾Ñ€Ð¾
    // Ð¾Ñ‚Ð±Ñ‹Ð²Ð°ÐµÑ‚.
    public static final int INNADRIL_PLEASURE_BOAT_IS_LEAVING = 1002; // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ
    // Ð²
    // Ð˜Ð½Ð½Ð°Ð´Ñ€Ð¸Ð»
    // Ð¾Ñ‚Ð¿Ñ€Ð°Ð²Ð»Ñ�ÐµÑ‚Ñ�Ñ�.
    public static final int CANNOT_PROCESS_A_MONSTER_RACE_TICKET = 1003; // ÐžÐ±Ñ€Ð°Ð±Ð¾Ñ‚Ð°Ñ‚ÑŒ
    // Ð±Ð¸Ð»ÐµÑ‚
    // Ð½Ð°
    // Ð³Ð¾Ð½ÐºÑƒ
    // Ð¼Ð¾Ð½Ñ�Ñ‚Ñ€Ð¾Ð²
    // Ð½ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾.
    public static final int THE_PRELIMINARY_MATCH_REGISTRATION_OF_S1_HAS_FINISHED = 1007; // Ð ÐµÐ³Ð¸Ñ�Ñ‚Ñ€Ð°Ñ†Ð¸Ñ�
    // Ð½Ð°
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð¸Ðµ
    // Ð²
    // Ð¾Ñ‚Ð±Ð¾Ñ€Ð¾Ñ‡Ð½Ð¾Ð¼
    // Ñ‚ÑƒÑ€Ðµ
    // $s1
    // Ð·Ð°Ð²ÐµÑ€ÑˆÐµÐ½Ð°.
    public static final int A_HUNGRY_STRIDER_CANNOT_BE_MOUNTED_OR_DISMOUNTED = 1008; // ÐžÑ�ÐµÐ´Ð»Ð°Ñ‚ÑŒ
    // Ð³Ð¾Ð»Ð¾Ð´Ð½Ð¾Ð³Ð¾
    // Ð¿Ð¸Ñ‚Ð¾Ð¼Ñ†Ð°
    // Ð½ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾.
    public static final int A_STRIDER_CANNOT_BE_RIDDEN_WHEN_DEAD = 1009; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¾Ñ�ÐµÐ´Ð»Ð°Ñ‚ÑŒ
    // Ð¿Ð¸Ñ‚Ð¾Ð¼Ñ†Ð°,
    // ÐµÑ�Ð»Ð¸
    // Ð¼ÐµÑ€Ñ‚Ð²Ñ‹.
    public static final int A_DEAD_PET_CANNOT_BE_RIDDEN = 1010; // Ð’Ñ‹ Ð½Ðµ Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¾Ñ�ÐµÐ´Ð»Ð°Ñ‚ÑŒ
    // Ð¼ÐµÑ€Ñ‚Ð²Ð¾Ð³Ð¾
    // Ð¿Ð¸Ñ‚Ð¾Ð¼Ñ†Ð°.
    public static final int A_STRIDER_IN_BATTLE_CANNOT_BE_RIDDEN = 1011; // Ð’Ð¾ÑŽÑŽÑ‰Ð¸Ð¹
    // Ð¿Ð¸Ñ‚Ð¾Ð¼ÐµÑ†
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð¾Ñ�ÐµÐ´Ð»Ð°Ð½.
    public static final int A_STRIDER_CANNOT_BE_RIDDEN_WHILE_IN_BATTLE = 1012; // ÐžÑ�ÐµÐ´Ð»Ð°Ñ‚ÑŒ
    // Ð¿Ð¸Ñ‚Ð¾Ð¼Ñ†Ð°
    // Ð²Ð¾
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ð±Ð¸Ñ‚Ð²Ñ‹
    // Ð½ÐµÐ»ÑŒÐ·Ñ�.
    public static final int A_STRIDER_CAN_BE_RIDDEN_ONLY_WHEN_STANDING = 1013; // Ð’Ñ‹
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¾Ñ�ÐµÐ´Ð»Ð°Ñ‚ÑŒ
    // Ð¿Ð¸Ñ‚Ð¾Ð¼Ñ†Ð°,
    // Ñ‚Ð¾Ð»ÑŒÐºÐ¾
    // ÐµÑ�Ð»Ð¸
    // Ñ�Ñ‚Ð¾Ð¸Ñ‚Ðµ.
    public static final int THE_PET_ACQUIRED_EXPERIENCE_POINTS_OF_S1 = 1014; // Ð’Ð°Ñˆ
    // Ð¿Ð¸Ñ‚Ð¾Ð¼ÐµÑ†
    // Ð¿Ð¾Ð»ÑƒÑ‡Ð¸Ð»
    // $s1
    // Ð¾Ð¿Ñ‹Ñ‚Ð°.
    public static final int THE_PET_GAVE_DAMAGE_OF_S1 = 1015; // Ð’Ð°Ñˆ Ð¿Ð¸Ñ‚Ð¾Ð¼ÐµÑ†
    // Ð½Ð°Ð½ÐµÑ� $s1
    // ÑƒÑ€Ð¾Ð½Ð°.
    public static final int THE_PET_RECEIVED_DAMAGE_OF_S2_CAUSED_BY_S1 = 1016; // $c1
    // Ð½Ð°Ð½Ð¾Ñ�Ð¸Ñ‚
    // Ð¿Ð¸Ñ‚Ð¾Ð¼Ñ†Ñƒ
    // $s2
    // ÑƒÑ€Ð¾Ð½Ð°.
    public static final int PETS_CRITICAL_HIT = 1017; // ÐšÑ€Ð¸Ñ‚Ð¸Ñ‡ÐµÑ�ÐºÐ¸Ð¹ ÑƒÐ´Ð°Ñ€
    // Ð¿Ð¸Ñ‚Ð¾Ð¼Ñ†Ð°!
    public static final int THE_PET_USES_S1 = 1018; // Ð’Ð°Ñˆ Ð¿Ð¸Ñ‚Ð¾Ð¼ÐµÑ† Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·ÑƒÐµÑ‚:
    // $s1.
    public static final int YOUR_PET_USES_S1 = 1019; // Ð’Ð°Ñˆ Ð¿Ð¸Ñ‚Ð¾Ð¼ÐµÑ† Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·ÑƒÐµÑ‚:
    // $s1.
    public static final int THE_PET_GAVE_S1 = 1020; // Ð’Ð°Ñˆ Ð¿Ð¸Ñ‚Ð¾Ð¼ÐµÑ† Ð¿Ð¾Ð´Ð¾Ð±Ñ€Ð°Ð»:
    // $s1.
    public static final int THE_PET_GAVE_S2_S1_S = 1021; // Ð’Ð°Ñˆ Ð¿Ð¸Ñ‚Ð¾Ð¼ÐµÑ† Ð¿Ð¾Ð´Ð¾Ð±Ñ€Ð°Ð»
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚: $s1 ($s2
    // ÑˆÑ‚.).
    public static final int THE_PET_GAVE__S1_S2 = 1022; // Ð’Ð°Ñˆ Ð¿Ð¸Ñ‚Ð¾Ð¼ÐµÑ† Ð¿Ð¾Ð´Ð¾Ð±Ñ€Ð°Ð»:
    // +$s1 $s2.
    public static final int THE_PET_GAVE_S1_ADENA = 1023; // Ð’Ð°Ñˆ Ð¿Ð¸Ñ‚Ð¾Ð¼ÐµÑ†
    // Ð¿Ð¾Ð´Ð¾Ð±Ñ€Ð°Ð»: $s1 Ð°Ð´ÐµÐ½.
    public static final int THE_PET_PUT_ON_S1 = 1024; // Ð’Ð°Ñˆ Ð¿Ð¸Ñ‚Ð¾Ð¼ÐµÑ† Ð½Ð°Ð´ÐµÐ»: $s1.
    public static final int THE_PET_TOOK_OFF_S1 = 1025; // Ð’Ð°Ñˆ Ð¿Ð¸Ñ‚Ð¾Ð¼ÐµÑ† Ñ�Ð½Ñ�Ð»:
    // $s1.
    public static final int THE_SUMMONED_MONSTER_GAVE_DAMAGE_OF_S1 = 1026; // Ð’Ð°Ñˆ
    // Ñ�Ð»ÑƒÐ³Ð°
    // Ð½Ð°Ð½Ð¾Ñ�Ð¸Ñ‚
    // $s1
    // ÑƒÑ€Ð¾Ð½Ð°.
    public static final int THE_SUMMONED_MONSTER_RECEIVED_DAMAGE_OF_S2_CAUSED_BY_S1 = 1027; // $c1
    // Ð½Ð°Ð½Ð¾Ñ�Ð¸Ñ‚
    // Ð’Ð°ÑˆÐµÐ¼Ñƒ
    // Ñ�Ð»ÑƒÐ³Ðµ
    // $s2
    // ÑƒÑ€Ð¾Ð½Ð°.
    public static final int SUMMONED_MONSTERS_CRITICAL_HIT = 1028; // ÐšÑ€Ð¸Ñ‚Ð¸Ñ‡ÐµÑ�ÐºÐ¸Ð¹
    // ÑƒÐ´Ð°Ñ€
    // Ð²Ñ‹Ð·Ð²Ð°Ð½Ð½Ð¾Ð³Ð¾
    // Ð¼Ð¾Ð½Ñ�Ñ‚Ñ€Ð°!
    public static final int A_SUMMONED_MONSTER_USES_S1 = 1029; // Ð’Ð°Ñˆ Ñ�Ð»ÑƒÐ³Ð°
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·ÑƒÐµÑ‚:
    // $s1.
    public static final int _PARTY_INFORMATION_ = 1030; // <Ð˜Ð½Ñ„Ð¾Ñ€Ð¼Ð°Ñ†Ð¸Ñ� Ð¾
    // Ð³Ñ€ÑƒÐ¿Ð¿Ðµ.>
    public static final int LOOTING_METHOD_FINDERS_KEEPERS = 1031; // ÐŸÐ¾Ð´Ð±Ð¾Ñ€
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ð¾Ð²:
    // Ð�Ð°ÑˆÐµÐ´ÑˆÐµÐ¼Ñƒ
    public static final int LOOTING_METHOD_RANDOM = 1032; // ÐŸÐ¾Ð´Ð±Ð¾Ñ€ Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ð¾Ð²:
    // Ð¡Ð»ÑƒÑ‡Ð°Ð¹Ð½Ð¾
    public static final int LOOTING_METHOD_RANDOM_INCLUDING_SPOIL = 1033; // ÐŸÐ¾Ð´Ð±Ð¾Ñ€
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ð¾Ð²:
    // Ð¡Ð»ÑƒÑ‡Ð°Ð¹Ð½Ð¾+ÐŸÑ€Ð¸Ñ�Ð²Ð¾Ð¸Ñ‚ÑŒ
    public static final int LOOTING_METHOD_BY_TURN = 1034; // ÐŸÐ¾Ð´Ð±Ð¾Ñ€ Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ð¾Ð²:
    // ÐŸÐ¾ Ð¾Ñ‡ÐµÑ€ÐµÐ´Ð¸
    public static final int LOOTING_METHOD_BY_TURN_INCLUDING_SPOIL = 1035; // ÐŸÐ¾Ð´Ð±Ð¾Ñ€
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ð¾Ð²:
    // ÐŸÐ¾
    // Ð¾Ñ‡ÐµÑ€ÐµÐ´Ð¸+ÐŸÑ€Ð¸Ñ�Ð²Ð¾Ð¸Ñ‚ÑŒ
    public static final int YOU_HAVE_EXCEEDED_THE_QUANTITY_THAT_CAN_BE_INPUTTED = 1036; // Ð’Ñ‹
    // Ð¿Ñ€ÐµÐ²Ñ‹Ñ�Ð¸Ð»Ð¸
    // Ð¼Ð°ÐºÑ�Ð¸Ð¼Ð°Ð»ÑŒÐ½Ð¾
    // Ð²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾Ðµ
    // ÐºÐ¾Ð»Ð¸Ñ‡ÐµÑ�Ñ‚Ð²Ð¾.
    public static final int S1_MANUFACTURED_S2 = 1037; // $c1 Ñ�Ð¾Ð·Ð´Ð°ÐµÑ‚: $s2.
    public static final int S1_MANUFACTURED_S3_S2_S = 1038; // $c1 Ñ�Ð¾Ð·Ð´Ð°ÐµÑ‚: $s2
    // ($s3 ÑˆÑ‚.).
    public static final int ITEMS_LEFT_AT_THE_CLAN_HALL_WAREHOUSE_CAN_ONLY_BE_RETRIEVED_BY_THE_CLAN_LEADER_DO_YOU_WANT_TO_CONTINUE = 1039; // ÐŸÑ€ÐµÐ´Ð¼ÐµÑ‚Ñ‹,
    // Ð½Ð°Ñ…Ð¾Ð´Ñ�Ñ‰Ð¸ÐµÑ�Ñ�
    // Ð²
    // Ñ…Ñ€Ð°Ð½Ð¸Ð»Ð¸Ñ‰Ðµ
    // ÐºÐ»Ð°Ð½Ð°,
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð·Ð°Ð±Ñ€Ð°Ñ‚ÑŒ
    // Ñ‚Ð¾Ð»ÑŒÐºÐ¾
    // Ð³Ð»Ð°Ð²Ð°
    // ÐºÐ»Ð°Ð½Ð°.
    // ÐŸÑ€Ð¾Ð´Ð¾Ð»Ð¶Ð¸Ñ‚ÑŒ?
    public static final int PACKAGES_SENT_CAN_ONLY_BE_RETRIEVED_AT_THIS_WAREHOUSE_DO_YOU_WANT_TO_CONTINUE = 1040; // ÐŸÐ¾Ñ�Ñ‹Ð»ÐºÑƒ
    // Ð¼Ð¾Ð¶Ð½Ð¾
    // Ð·Ð°Ð±Ñ€Ð°Ñ‚ÑŒ
    // Ñ�
    // Ð»ÑŽÐ±Ð¾Ð³Ð¾
    // Ñ�ÐºÐ»Ð°Ð´Ð°.
    // ÐŸÑ€Ð¾Ð´Ð¾Ð»Ð¶Ð¸Ñ‚ÑŒ?
    public static final int THE_NEXT_SEED_PURCHASE_PRICE_IS_S1_ADENA = 1041; // Ð¦ÐµÐ½Ð°
    // Ñ�ÐµÐ¼Ñ�Ð½
    // Ð½Ð°
    // Ð·Ð°Ð²Ñ‚Ñ€Ð°:
    // $s1
    // Ð°Ð´ÐµÐ½.
    public static final int THE_NEXT_FARM_GOODS_PURCHASE_PRICE_IS_S1_ADENA = 1042; // Ð¦ÐµÐ½Ð°
    // Ð¿Ð»Ð¾Ð´Ð¾Ð²
    // Ð½Ð°
    // Ð·Ð°Ð²Ñ‚Ñ€Ð°:
    // $s1
    // Ð°Ð´ÐµÐ½.
    public static final int AT_THE_CURRENT_TIME_THE__UNSTUCK_COMMAND_CANNOT_BE_USED_PLEASE_SEND_IN_A_PETITION = 1043; // Ð¡ÐµÐ¹Ñ‡Ð°Ñ�
    // Ð½ÐµÐ»ÑŒÐ·Ñ�
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒ
    // ÐºÐ¾Ð¼Ð°Ð½Ð´Ñƒ
    // "/unstuck".
    // ÐžÐ±Ñ€Ð°Ñ‚Ð¸Ñ‚ÐµÑ�ÑŒ
    // Ð²
    // Ñ�Ð»ÑƒÐ¶Ð±Ñƒ
    // Ð¿Ð¾Ð´Ð´ÐµÑ€Ð¶ÐºÐ¸.
    public static final int MONSTER_RACE_PAYOUT_INFORMATION_IS_NOT_AVAILABLE_WHILE_TICKETS_ARE_BEING_SOLD = 1044; // Ð’Ð¾
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ð¿Ñ€Ð¾Ð´Ð°Ð¶Ð¸
    // Ð±Ð¸Ð»ÐµÑ‚Ð¾Ð²
    // ÑƒÐ·Ð½Ð°Ñ‚ÑŒ
    // Ñ�ÑƒÐ¼Ð¼Ñƒ
    // Ñ�Ñ‚Ð°Ð²Ð¾Ðº
    // Ð½ÐµÐ»ÑŒÐ·Ñ�.
    public static final int NOT_CURRENTLY_PREPARING_FOR_A_MONSTER_RACE = 1045; // ÐŸÐ¾Ð´Ð³Ð¾Ñ‚Ð¾Ð²ÐºÐ°
    // Ð³Ð¾Ð½ÐºÐ¸
    // Ð¼Ð¾Ð½Ñ�Ñ‚Ñ€Ð¾Ð²
    // Ð½Ðµ
    // Ð¿Ñ€Ð¾Ð²Ð¾Ð´Ð¸Ñ‚Ñ�Ñ�.
    public static final int MONSTER_RACE_TICKETS_ARE_NO_LONGER_AVAILABLE = 1046; // ÐŸÑ€Ð¾Ð´Ð°Ð¶Ð°
    // Ð·Ð°Ð²ÐµÑ€ÑˆÐµÐ½Ð°,
    // Ð¿Ð¾ÐºÑƒÐ¿ÐºÐ°
    // Ð±Ð¸Ð»ÐµÑ‚Ð¾Ð²
    // Ð½Ð°
    // Ð³Ð¾Ð½ÐºÑƒ
    // Ð¼Ð¾Ð½Ñ�Ñ‚Ñ€Ð¾Ð²
    // Ð½ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð°.
    public static final int WE_DID_NOT_SUCCEED_IN_PRODUCING_S1_ITEM = 1047; // Ð˜Ð·Ð³Ð¾Ñ‚Ð¾Ð²Ð¸Ñ‚ÑŒ
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚
    // $s1
    // Ð½Ðµ
    // Ð¿Ð¾Ð»ÑƒÑ‡Ð¸Ð»Ð¾Ñ�ÑŒ.
    public static final int WHISPERING_IS_NOT_POSSIBLE_IN_STATE_OF_OVERALL_BLOCKING = 1048; // ÐŸÐ¾Ñ�Ð»Ð°Ñ‚ÑŒ
    // Ð»Ð¸Ñ‡Ð½Ð¾Ðµ
    // Ñ�Ð¾Ð¾Ð±Ñ‰ÐµÐ½Ð¸Ðµ
    // Ð²
    // Ñ€ÐµÐ¶Ð¸Ð¼Ðµ
    // Ð¿Ð¾Ð»Ð½Ð¾Ð¹
    // Ð±Ð»Ð¾ÐºÐ¸Ñ€Ð¾Ð²ÐºÐ¸
    // Ð½ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾.
    public static final int IT_IS_NOT_POSSIBLE_TO_MAKE_INVITATIONS_FOR_ORGANIZING_PARTIES_IN_STATE_OF_OVERALL_BLOCKING = 1049; // ÐŸÑ€Ð¸Ð³Ð»Ð°Ñ�Ð¸Ñ‚ÑŒ
    // Ð²
    // Ð³Ñ€ÑƒÐ¿Ð¿Ñƒ
    // Ð²
    // Ñ€ÐµÐ¶Ð¸Ð¼Ðµ
    // Ð¿Ð¾Ð»Ð½Ð¾Ð¹
    // Ð±Ð»Ð¾ÐºÐ¸Ñ€Ð¾Ð²ÐºÐ¸
    // Ð½ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾.
    public static final int THERE_ARE_NO_COMMUNITIES_IN_MY_CLAN_CLAN_COMMUNITIES_ARE_ALLOWED_FOR_CLANS_WITH_SKILL_LEVELS_OF_2_AND_HIGHER = 1050; // Ð’
    // ÐºÐ»Ð°Ð½Ðµ
    // Ð½ÐµÑ‚
    // Ñ„ÑƒÐ½ÐºÑ†Ð¸Ð¸
    // Ñ�Ð¾Ð¾Ð±Ñ‰ÐµÑ�Ñ‚Ð²Ð°.
    // ÐšÐ»Ð°Ð½Ð¾Ð²Ñ‹Ðµ
    // Ñ�Ð¾Ð¾Ð±Ñ‰ÐµÑ�Ñ‚Ð²Ð°
    // Ð´Ð¾Ñ�Ñ‚ÑƒÐ¿Ð½Ñ‹
    // Ñ‚Ð¾Ð»ÑŒÐºÐ¾
    // Ð´Ð»Ñ�
    // ÐºÐ»Ð°Ð½Ð¾Ð²
    // 2-Ð³Ð¾
    // ÑƒÑ€Ð¾Ð²Ð½Ñ�
    // Ð¸
    // Ð²Ñ‹ÑˆÐµ.
    public static final int PAYMENT_FOR_YOUR_CLAN_HALL_HAS_NOT_BEEN_MADE_PLEASE_MAKE_PAYMENT_TO_YOUR_CLAN_WAREHOUSE_BY_S1_TOMORROW = 1051; // Ð’Ñ‹
    // Ð½Ðµ
    // Ñ�Ð´Ð°Ð»Ð¸
    // Ð´ÐµÐ½ÑŒÐ³Ð¸
    // Ð·Ð°
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ð½Ð¸Ðµ
    // Ñ…Ð¾Ð»Ð»Ð°
    // ÐºÐ»Ð°Ð½Ð°.
    // ÐŸÑ€Ð¾Ñ�Ð¸Ð¼
    // Ð’Ð°Ñ�
    // Ð¿Ð¾Ð»Ð¾Ð¶Ð¸Ñ‚ÑŒ
    // Ð½ÐµÐ¾Ð±Ñ…Ð¾Ð´Ð¸Ð¼ÑƒÑŽ
    // Ñ�ÑƒÐ¼Ð¼Ñƒ
    // Ð²
    // Ñ…Ñ€Ð°Ð½Ð¸Ð»Ð¸Ñ‰Ðµ
    // ÐºÐ»Ð°Ð½Ð°
    // Ð´Ð¾
    // $s1
    // Ð·Ð°Ð²Ñ‚Ñ€Ð°ÑˆÐ½ÐµÐ³Ð¾
    // Ð´Ð½Ñ�.
    public static final int THE_CLAN_HALL_FEE_IS_ONE_WEEK_OVERDUE_THEREFORE_THE_CLAN_HALL_OWNERSHIP_HAS_BEEN_REVOKED = 1052; // Ð’Ñ‹
    // Ð¾Ð¿Ð¾Ð·Ð´Ð°Ð»Ð¸
    // Ñ�
    // Ð°Ñ€ÐµÐ½Ð´Ð¾Ð¹
    // Ñ…Ð¾Ð»Ð»Ð°
    // ÐºÐ»Ð°Ð½Ð°
    // Ð½Ð°
    // Ð½ÐµÐ´ÐµÐ»ÑŽ,
    // Ð²Ñ�Ð»ÐµÐ´Ñ�Ñ‚Ð²Ð¸Ðµ
    // Ñ‡ÐµÐ³Ð¾
    // Ð¿Ñ€Ð°Ð²Ð¾
    // Ð½Ð°
    // Ð²Ð»Ð°Ð´ÐµÐ½Ð¸Ðµ
    // Ñ…Ð¾Ð»Ð»Ð¾Ð¼
    // Ð¾Ñ‚Ð¼ÐµÐ½ÐµÐ½Ð¾.
    public static final int IT_IS_IMPOSSIBLE_TO_BE_RESSURECTED_IN_BATTLEFIELDS_WHERE_SIEGE_WARS_ARE_IN_PROCESS = 1053; // Ð’Ð¾
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ð¿Ñ€Ð¾Ð²ÐµÐ´ÐµÐ½Ð¸Ñ�
    // Ð¾Ñ�Ð°Ð´
    // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒÑ�Ñ�
    // Ñ„ÑƒÐ½ÐºÑ†Ð¸ÐµÐ¹
    // Ð²Ð¾Ñ�ÐºÑ€ÐµÑˆÐµÐ½Ð¸Ñ�.
    public static final int YOU_HAVE_ENTERED_A_LAND_WITH_MYSTERIOUS_POWERS = 1054; // Ð’Ñ‹
    // Ð¿Ð¾Ð¿Ð°Ð»Ð¸
    // Ð²
    // Ð¼Ð¸Ñ�Ñ‚Ð¸Ñ‡ÐµÑ�ÐºÐ¾Ðµ
    // Ð¼ÐµÑ�Ñ‚Ð¾.
    public static final int YOU_HAVE_LEFT_THE_LAND_WHICH_HAS_MYSTERIOUS_POWERS = 1055; // Ð’Ñ‹
    // ÑƒÑˆÐ»Ð¸
    // Ð¸Ð·
    // Ð¼Ð¸Ñ�Ñ‚Ð¸Ñ‡ÐµÑ�ÐºÐ¾Ð³Ð¾
    // Ð¼ÐµÑ�Ñ‚Ð°.
    public static final int YOU_HAVE_EXCEEDED_THE_CASTLES_STORAGE_LIMIT_OF_ADENA = 1056; // Ð’Ñ‹
    // Ð¿Ñ€ÐµÐ²Ñ‹Ñ�Ð¸Ð»Ð¸
    // Ð»Ð¸Ð¼Ð¸Ñ‚
    // Ð°Ð´ÐµÐ½,
    // ÐºÐ¾Ñ‚Ð¾Ñ€Ñ‹Ðµ
    // Ð¼Ð¾Ð¶Ð½Ð¾
    // Ð¿Ð¾Ð»Ð¾Ð¶Ð¸Ñ‚ÑŒ
    // Ð²
    // Ñ…Ñ€Ð°Ð½Ð¸Ð»Ð¸Ñ‰Ðµ
    // Ð·Ð°Ð¼ÐºÐ°.
    public static final int THIS_COMMAND_CAN_ONLY_BE_USED_IN_THE_RELAX_SERVER = 1057; // Ð”Ð°Ð½Ð½Ð°Ñ�
    // ÐºÐ¾Ð¼Ð°Ð½Ð´Ð°
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·ÑƒÐµÑ‚Ñ�Ñ�
    // Ñ‚Ð¾Ð»ÑŒÐºÐ¾
    // Ð½Ð°
    // Ñ�ÐµÑ€Ð²ÐµÑ€Ðµ
    // Ð´Ð»Ñ�
    // Ð¾Ñ‚Ð´Ñ‹Ñ…Ð°.
    public static final int THE_SALES_AMOUNT_OF_SEEDS_IS_S1_ADENA = 1058; // Ð¦ÐµÐ½Ð°
    // Ñ�ÐµÐ¼ÐµÐ½Ð¸:
    // $s1
    // Ð°Ð´ÐµÐ½.
    public static final int THE_REMAINING_PURCHASING_AMOUNT_IS_S1_ADENA = 1059; // Ð�Ð°
    // Ð·Ð°ÐºÑƒÐ¿ÐºÑƒ
    // Ñƒ
    // Ð’Ð°Ñ�
    // Ð¾Ñ�Ñ‚Ð°Ð»Ð¾Ñ�ÑŒ:
    // $s1
    // Ð°Ð´ÐµÐ½.
    public static final int THE_REMAINDER_AFTER_SELLING_THE_SEEDS_IS_S1 = 1060; // ÐŸÐ¾Ñ�Ð»Ðµ
    // Ð¿Ñ€Ð¾Ð´Ð°Ð¶Ð¸
    // Ñ�ÐµÐ¼Ñ�Ð½
    // Ñƒ
    // Ð’Ð°Ñ�
    // Ð¾Ñ�Ñ‚Ð°Ð»Ð¾Ñ�ÑŒ:
    // $s1
    // Ð°Ð´ÐµÐ½.
    public static final int THE_RECIPE_CANNOT_BE_REGISTERED__YOU_DO_NOT_HAVE_THE_ABILITY_TO_CREATE_ITEMS = 1061; // Ð£
    // Ð’Ð°Ñ�
    // Ð½ÐµÑ‚
    // ÑƒÐ¼ÐµÐ½Ð¸Ñ�
    // Ð¸Ð·Ð³Ð¾Ñ‚Ð°Ð²Ð»Ð¸Ð²Ð°Ñ‚ÑŒ
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ñ‹,
    // Ð·Ð°Ñ€ÐµÐ³Ð¸Ñ�Ñ‚Ñ€Ð¸Ñ€Ð¾Ð²Ð°Ñ‚ÑŒ
    // Ñ€ÐµÑ†ÐµÐ¿Ñ‚
    // Ð½Ðµ
    // Ð¿Ð¾Ð»ÑƒÑ‡Ð¸Ñ‚Ñ�Ñ�.
    public static final int WRITING_SOMETHING_NEW_IS_POSSIBLE_AFTER_LEVEL_10 = 1062; // Ð�Ð°Ð¿Ð¸Ñ�Ð°Ñ‚ÑŒ
    // Ñ�Ð¾Ð¾Ð±Ñ‰ÐµÐ½Ð¸Ðµ
    // Ð¼Ð¾Ð¶Ð½Ð¾,
    // Ñ‚Ð¾Ð»ÑŒÐºÐ¾
    // Ð´Ð¾Ñ�Ñ‚Ð¸Ð³Ð½ÑƒÐ²
    // 10-Ð³Ð¾
    // ÑƒÑ€Ð¾Ð²Ð½Ñ�.
    public static final int PETITION_SERVICE_IS_NOT_AVAILABEL_FOR_S1_TO_S2_IN_CASE_OF_BEING_TRAPPED_IN_TERRITORY_WHERE_YOU_ARE_UNABLE_TO_MOVE_PLEASE_USE_THE__UNSTUCK_COMMAND = 1063; // Ð’
    // Ð´Ð°Ð½Ð½Ñ‹Ð¹
    // Ð¼Ð¾Ð¼ÐµÐ½Ñ‚
    // Ð·Ð°Ñ�Ð²ÐºÑƒ
    // Ð¾Ñ‚Ð¿Ñ€Ð°Ð²Ð¸Ñ‚ÑŒ
    // Ð½ÐµÐ»ÑŒÐ·Ñ�.
    // Ð’
    // Ñ�Ð»ÑƒÑ‡Ð°Ðµ,
    // ÐµÑ�Ð»Ð¸
    // Ð’Ñ‹
    // Ð·Ð°Ñ�Ñ‚Ñ€Ñ�Ð»Ð¸
    // Ð²
    // Ñ‚ÐµÐºÑ�Ñ‚ÑƒÑ€Ð°Ñ…,
    // Ð²Ð¾Ñ�Ð¿Ð¾Ð»ÑŒÐ·ÑƒÐ¹Ñ‚ÐµÑ�ÑŒ
    // ÐºÐ¾Ð¼Ð°Ð½Ð´Ð¾Ð¹
    // "/unstuck".
    public static final int EQUIPMENT_OF__S1_S2_HAS_BEEN_REMOVED = 1064; // Ð’Ñ‹
    // Ñ�Ð½Ñ�Ð»Ð¸
    // Ñ�Ð½Ð°Ñ€Ñ�Ð¶ÐµÐ½Ð¸Ðµ
    // +$s1
    // $s2.
    public static final int WHILE_OPERATING_A_PRIVATE_STORE_OR_WORKSHOP_YOU_CANNOT_DISCARD_DESTROY_OR_TRADE_AN_ITEM = 1065; // Ð’
    // Ð»Ð¸Ñ‡Ð½Ð¾Ð¹
    // Ñ‚Ð¾Ñ€Ð³Ð¾Ð²Ð¾Ð¹
    // Ð»Ð°Ð²ÐºÐµ
    // Ð¸Ð»Ð¸
    // Ð¼Ð°Ñ�Ñ‚ÐµÑ€Ñ�ÐºÐ¾Ð¹
    // Ð¾Ð±Ð¼ÐµÐ½
    // Ð¸Ð»Ð¸
    // ÑƒÐ´Ð°Ð»ÐµÐ½Ð¸Ðµ
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ð°
    // Ð½ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾.
    public static final int S1_HPS_HAVE_BEEN_RESTORED = 1066; // Ð’Ð¾Ñ�Ñ�Ñ‚Ð°Ð½Ð¾Ð²Ð»ÐµÐ½Ð¾
    // $s1 Ð�Ð .
    public static final int XS2S_HP_HAS_BEEN_RESTORED_BY_S1 = 1067; // $c1
    // Ð²Ð¾Ñ�Ñ�Ñ‚Ð°Ð½Ð°Ð²Ð»Ð¸Ð²Ð°ÐµÑ‚
    // Ð’Ð°Ð¼ $s2
    // Ð�Ð .
    public static final int S1_MPS_HAVE_BEEN_RESTORED = 1068; // Ð’Ð¾Ñ�Ñ�Ñ‚Ð°Ð½Ð¾Ð²Ð»ÐµÐ½Ð¾
    // $s1 MP.
    public static final int XS2S_MP_HAS_BEEN_RESTORED_BY_S1 = 1069; // $c1
    // Ð²Ð¾Ñ�Ñ�Ñ‚Ð°Ð½Ð°Ð²Ð»Ð¸Ð²Ð°ÐµÑ‚
    // Ð’Ð°Ð¼ $s2
    // MP.
    public static final int XYOU_DO_NOT_HAVE_XREADX_PERMISSION = 1070; // Ð’Ð°Ð¼ Ð½Ðµ
    // Ð´Ð¾Ñ�Ñ‚ÑƒÐ¿Ð½Ð°
    // Ñ„ÑƒÐ½ÐºÑ†Ð¸Ñ�
    // "Ð¿Ñ€Ð¾Ñ‡Ð¸Ñ‚Ð°Ñ‚ÑŒ".
    public static final int XYOU_DO_NOT_HAVE_XWRITEX_PERMISSION = 1071; // Ð’Ð°Ð¼
    // Ð½Ðµ
    // Ð´Ð¾Ñ�Ñ‚ÑƒÐ¿Ð½Ð°
    // Ñ„ÑƒÐ½ÐºÑ†Ð¸Ñ�
    // "Ð½Ð°Ð¿Ð¸Ñ�Ð°Ñ‚ÑŒ".
    public static final int YOU_HAVE_OBTAINED_A_TICKET_FOR_THE_MONSTER_RACE_S1__SINGLE = 1072; // Ð’Ñ‹
    // Ð¿Ð¾Ð»ÑƒÑ‡Ð¸Ð»Ð¸
    // Ð¾Ð´Ð½Ð¾Ñ€Ð°Ð·Ð¾Ð²Ñ‹Ð¹
    // Ð±Ð¸Ð»ÐµÑ‚
    // Ð½Ð°
    // Ð³Ð¾Ð½ÐºÑƒ
    // Ð¼Ð¾Ð½Ñ�Ñ‚Ñ€Ð¾Ð²
    // $s1.
    public static final int YOU_HAVE_OBTAINED_A_TICKET_FOR_THE_MONSTER_RACE_S1__DOUBLE = 1073; // Ð’Ñ‹
    // Ð¿Ð¾Ð»ÑƒÑ‡Ð¸Ð»Ð¸
    // Ð¼Ð½Ð¾Ð³Ð¾Ñ€Ð°Ð·Ð¾Ð²Ñ‹Ð¹
    // Ð±Ð¸Ð»ÐµÑ‚
    // Ð½Ð°
    // Ð³Ð¾Ð½ÐºÑƒ
    // Ð¼Ð¾Ð½Ñ�Ñ‚Ñ€Ð¾Ð²
    // $s1.
    public static final int YOU_DO_NOT_MEET_THE_AGE_REQUIREMENT_TO_PURCHASE_A_MONSTER_RACE_TICKET = 1074; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¿Ñ€Ð¸Ð¾Ð±Ñ€ÐµÑ�Ñ‚Ð¸
    // Ð±Ð¸Ð»ÐµÑ‚
    // Ð½Ð°
    // Ð³Ð¾Ð½ÐºÐ¸
    // Ð¼Ð¾Ð½Ñ�Ñ‚Ñ€Ð¾Ð²
    // Ð¸Ð·-Ð·Ð°
    // Ð¾Ð³Ñ€Ð°Ð½Ð¸Ñ‡ÐµÐ½Ð¸Ñ�
    // Ð¿Ð¾
    // Ð²Ð¾Ð·Ñ€Ð°Ñ�Ñ‚Ñƒ.
    public static final int THE_GAME_CANNOT_BE_TERMINATED = 1076; // Ð�ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾
    // Ð²Ñ‹Ð¹Ñ‚Ð¸ Ð¸Ð·
    // Ð¸Ð³Ñ€Ñ‹.
    public static final int A_GAMEGUARD_EXECUTION_ERROR_HAS_OCCURRED_PLEASE_SEND_THE_ERL_FILE_S_LOCATED_IN_THE_GAMEGUARD_FOLDER_TO_GAME = 1077; // ÐžÑˆÐ¸Ð±ÐºÐ°
    // Ð¿Ñ€Ð¸
    // Ð°ÐºÑ‚Ð¸Ð²Ð°Ñ†Ð¸Ð¸
    // GameGuard.
    // ÐžÐ±Ñ€Ð°Ñ‚Ð¸Ñ‚ÐµÑ�ÑŒ
    // Ð²
    // Ñ�Ð»ÑƒÐ¶Ð±Ñƒ
    // Ð¿Ð¾Ð´Ð´ÐµÑ€Ð¶ÐºÐ¸
    // Ð½Ð°
    // Ð½Ð°ÑˆÐµÐ¼
    // Ñ�Ð°Ð¹Ñ‚Ðµ.
    public static final int WHEN_A_USERS_KEYBOARD_INPUT_EXCEEDS_A_CERTAIN_CUMULATIVE_SCORE_A_CHAT_BAN_WILL_BE_APPLIED_THIS_IS_DONE_TO_DISCOURAGE_SPAMMING_PLEASE_AVOID_POSTING_THE_SAME_MESSAGE_MULTIPLE_TIMES_DURING_A_SHORT_PERIOD = 1078; // ÐŸÑ€Ð¸
    // Ð¼Ð½Ð¾Ð³Ð¾ÐºÑ€Ð°Ñ‚Ð½Ð¾Ð¼
    // Ð¿Ð¾Ð²Ñ‚Ð¾Ñ€Ðµ
    // Ð¾Ð´Ð¸Ð½Ð°ÐºÐ¾Ð²Ñ‹Ñ…
    // Ñ„Ñ€Ð°Ð·
    // Ð²ÐºÐ»ÑŽÑ‡Ð¸Ñ‚Ñ�Ñ�
    // Ð±Ð»Ð¾ÐºÐ¸Ñ€Ð¾Ð²ÐºÐ°
    // Ñ‡Ð°Ñ‚Ð°.
    // Ð‘ÑƒÐ´ÑŒÑ‚Ðµ
    // Ð¾Ñ�Ñ‚Ð¾Ñ€Ð¾Ð¶Ð½Ñ‹
    // Ð¿Ñ€Ð¸
    // Ð¼Ð½Ð¾Ð³Ð¾ÐºÑ€Ð°Ñ‚Ð½Ð¾Ð¼
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ð½Ð¸Ð¸
    // Ð¾Ð´Ð½Ð¾Ð¹
    // Ð¸
    // Ñ‚Ð¾Ð¹
    // Ð¶Ðµ
    // Ñ„Ñ€Ð°Ð·Ñ‹.
    public static final int THE_TARGET_IS_CURRENTLY_BANNED_FROM_CHATTING = 1079; // Ð£
    // Ñ�Ð¾Ð±ÐµÑ�ÐµÐ´Ð½Ð¸ÐºÐ°
    // Ð°ÐºÑ‚Ð¸Ð²Ð¸Ñ€Ð¾Ð²Ð°Ð»Ð°Ñ�ÑŒ
    // Ð±Ð»Ð¾ÐºÐ¸Ñ€Ð¾Ð²ÐºÐ°
    // Ñ‡Ð°Ñ‚Ð°.
    public static final int DO_YOU_WISH_TO_USE_THE_FACELIFTING_POTION_X_TYPE_A_IT_IS_PERMANENT = 1080; // Ð’Ñ‹
    // Ñ…Ð¾Ñ‚Ð¸Ñ‚Ðµ
    // Ð²Ð¾Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒÑ�Ñ�
    // Ð·ÐµÐ»ÑŒÐµÐ¼
    // Ð¸Ð·Ð¼ÐµÐ½ÐµÐ½Ð¸Ñ�
    // Ð²Ð½ÐµÑˆÐ½Ð¾Ñ�Ñ‚Ð¸
    // -
    // Ñ‚Ð¸Ð¿
    // Ð�?
    // Ð­Ñ„Ñ„ÐµÐºÑ‚
    // Ð¾Ñ‚
    // Ñ�Ñ‚Ð¾Ð³Ð¾
    // Ð·ÐµÐ»ÑŒÑ�
    // Ð¾Ñ�Ñ‚Ð°Ð½ÐµÑ‚Ñ�Ñ�
    // Ð½Ð°Ð²Ñ�ÐµÐ³Ð´Ð°.
    public static final int DO_YOU_WISH_TO_USE_THE_DYE_POTION_X_TYPE_A_IT_IS_PERMANENT = 1081; // Ð’Ñ‹
    // Ñ…Ð¾Ñ‚Ð¸Ñ‚Ðµ
    // Ð²Ð¾Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒÑ�Ñ�
    // Ð·ÐµÐ»ÑŒÐµÐ¼
    // Ð¸Ð·Ð¼ÐµÐ½ÐµÐ½Ð¸Ñ�
    // Ñ†Ð²ÐµÑ‚Ð°
    // Ð²Ð¾Ð»Ð¾Ñ�
    // -
    // Ñ‚Ð¸Ð¿
    // Ð�?
    // Ð­Ñ„Ñ„ÐµÐºÑ‚
    // Ð¾Ñ‚
    // Ñ�Ñ‚Ð¾Ð³Ð¾
    // Ð·ÐµÐ»ÑŒÑ�
    // Ð¾Ñ�Ñ‚Ð°Ð½ÐµÑ‚Ñ�Ñ�
    // Ð½Ð°Ð²Ñ�ÐµÐ³Ð´Ð°.
    public static final int DO_YOU_WISH_TO_USE_THE_HAIR_STYLE_CHANGE_POTION_X_TYPE_A_IT_IS_PERMANENT = 1082; // Ð’Ñ‹
    // Ñ…Ð¾Ñ‚Ð¸Ñ‚Ðµ
    // Ð²Ð¾Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒÑ�Ñ�
    // Ð·ÐµÐ»ÑŒÐµÐ¼
    // Ð¸Ð·Ð¼ÐµÐ½ÐµÐ½Ð¸Ñ�
    // Ð¿Ñ€Ð¸Ñ‡ÐµÑ�ÐºÐ¸
    // -
    // Ñ‚Ð¸Ð¿
    // Ð�?
    // Ð­Ñ„Ñ„ÐµÐºÑ‚
    // Ð¾Ñ‚
    // Ñ�Ñ‚Ð¾Ð³Ð¾
    // Ð·ÐµÐ»ÑŒÑ�
    // Ð¾Ñ�Ñ‚Ð°Ð½ÐµÑ‚Ñ�Ñ�
    // Ð½Ð°Ð²Ñ�ÐµÐ³Ð´Ð°.
    public static final int THE_FACELIFTING_POTION__TYPE_A_IS_BEING_USED = 1083; // ÐŸÑ€Ð¸Ð¼ÐµÐ½ÐµÐ½Ð¾
    // Ð·ÐµÐ»ÑŒÐµ
    // Ð¸Ð·Ð¼ÐµÐ½ÐµÐ½Ð¸Ñ�
    // Ð²Ð½ÐµÑˆÐ½Ð¾Ñ�Ñ‚Ð¸
    // -
    // Ñ‚Ð¸Ð¿
    // Ð�.
    public static final int THE_DYE_POTION__TYPE_A_IS_BEING_USED = 1084; // ÐŸÑ€Ð¸Ð¼ÐµÐ½ÐµÐ½Ð¾
    // Ð·ÐµÐ»ÑŒÐµ
    // Ð¸Ð·Ð¼ÐµÐ½ÐµÐ½Ð¸Ñ�
    // Ñ†Ð²ÐµÑ‚Ð°
    // Ð²Ð¾Ð»Ð¾Ñ�
    // -
    // Ñ‚Ð¸Ð¿
    // Ð�.
    public static final int THE_HAIR_STYLE_CHANGE_POTION__TYPE_A_IS_BEING_USED = 1085; // ÐŸÑ€Ð¸Ð¼ÐµÐ½ÐµÐ½Ð¾
    // Ð·ÐµÐ»ÑŒÐµ
    // Ð¸Ð·Ð¼ÐµÐ½ÐµÐ½Ð¸Ñ�
    // Ð¿Ñ€Ð¸Ñ‡ÐµÑ�ÐºÐ¸
    // -
    // Ñ‚Ð¸Ð¿
    // Ð�.
    public static final int YOUR_FACIAL_APPEARANCE_HAS_BEEN_CHANGED = 1086; // Ð’Ð°ÑˆÐµ
    // Ð»Ð¸Ñ†Ð¾
    // Ð¸Ð·Ð¼ÐµÐ½Ð¸Ð»Ð¾Ñ�ÑŒ.
    public static final int YOUR_HAIR_COLOR_HAS_BEEN_CHANGED = 1087; // Ð¦Ð²ÐµÑ‚
    // Ð²Ð¾Ð»Ð¾Ñ�
    // Ð¸Ð·Ð¼ÐµÐ½ÐµÐ½.
    public static final int YOUR_HAIR_STYLE_HAS_BEEN_CHANGED = 1088; // ÐŸÑ€Ð¸Ñ‡ÐµÑ�ÐºÐ°
    // Ð¸Ð·Ð¼ÐµÐ½ÐµÐ½Ð°.
    public static final int S1_HAS_OBTAINED_A_FIRST_ANNIVERSARY_COMMEMORATIVE_ITEM = 1089; // ÐŸÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶
    // $c1
    // Ð¿Ð¾Ð»ÑƒÑ‡Ð¸Ð»
    // Ð¿Ð¾Ð´Ð°Ñ€Ð¾Ðº
    // Ð¿Ð¾
    // Ñ�Ð»ÑƒÑ‡Ð°ÑŽ
    // I
    // Ð³Ð¾Ð´Ð¾Ð²Ñ‰Ð¸Ð½Ñ‹
    // Ð²
    // Ð¸Ð³Ñ€Ðµ.
    public static final int DO_YOU_WISH_TO_USE_THE_FACELIFTING_POTION_X_TYPE_B_IT_IS_PERMANENT = 1090; // Ð’Ñ‹
    // Ñ…Ð¾Ñ‚Ð¸Ñ‚Ðµ
    // Ð²Ð¾Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒÑ�Ñ�
    // Ð·ÐµÐ»ÑŒÐµÐ¼
    // Ð¸Ð·Ð¼ÐµÐ½ÐµÐ½Ð¸Ñ�
    // Ð²Ð½ÐµÑˆÐ½Ð¾Ñ�Ñ‚Ð¸
    // -
    // Ñ‚Ð¸Ð¿
    // Ð’?
    // Ð­Ñ„Ñ„ÐµÐºÑ‚
    // Ð¾Ñ‚
    // Ñ�Ñ‚Ð¾Ð³Ð¾
    // Ð·ÐµÐ»ÑŒÑ�
    // Ð¾Ñ�Ñ‚Ð°Ð½ÐµÑ‚Ñ�Ñ�
    // Ð½Ð°Ð²Ñ�ÐµÐ³Ð´Ð°.
    public static final int XDO_YOU_WISH_TO_USE_THE_FACELIFTING_POTION_X_TYPE_C_IT_IS_PERMANENT = 1091; // Ð’Ñ‹
    // Ñ…Ð¾Ñ‚Ð¸Ñ‚Ðµ
    // Ð²Ð¾Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒÑ�Ñ�
    // Ð·ÐµÐ»ÑŒÐµÐ¼
    // Ð¸Ð·Ð¼ÐµÐ½ÐµÐ½Ð¸Ñ�
    // Ð²Ð½ÐµÑˆÐ½Ð¾Ñ�Ñ‚Ð¸
    // -
    // Ñ‚Ð¸Ð¿
    // Ð¡?
    // Ð­Ñ„Ñ„ÐµÐºÑ‚
    // Ð¾Ñ‚
    // Ñ�Ñ‚Ð¾Ð³Ð¾
    // Ð·ÐµÐ»ÑŒÑ�
    // Ð¾Ñ�Ñ‚Ð°Ð½ÐµÑ‚Ñ�Ñ�
    // Ð½Ð°Ð²Ñ�ÐµÐ³Ð´Ð°.
    public static final int XDO_YOU_WISH_TO_USE_THE_DYE_POTION_X_TYPE_B_IT_IS_PERMANENT = 1092; // Ð’Ñ‹
    // Ñ…Ð¾Ñ‚Ð¸Ñ‚Ðµ
    // Ð²Ð¾Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒÑ�Ñ�
    // Ð·ÐµÐ»ÑŒÐµÐ¼
    // Ð¸Ð·Ð¼ÐµÐ½ÐµÐ½Ð¸Ñ�
    // Ñ†Ð²ÐµÑ‚Ð°
    // Ð²Ð¾Ð»Ð¾Ñ�
    // -
    // Ñ‚Ð¸Ð¿
    // Ð’?
    // Ð­Ñ„Ñ„ÐµÐºÑ‚
    // Ð¾Ñ‚
    // Ñ�Ñ‚Ð¾Ð³Ð¾
    // Ð·ÐµÐ»ÑŒÑ�
    // Ð¾Ñ�Ñ‚Ð°Ð½ÐµÑ‚Ñ�Ñ�
    // Ð½Ð°Ð²Ñ�ÐµÐ³Ð´Ð°.
    public static final int XDO_YOU_WISH_TO_USE_THE_DYE_POTION_X_TYPE_C_IT_IS_PERMANENT = 1093; // Ð’Ñ‹
    // Ñ…Ð¾Ñ‚Ð¸Ñ‚Ðµ
    // Ð²Ð¾Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒÑ�Ñ�
    // Ð·ÐµÐ»ÑŒÐµÐ¼
    // Ð¸Ð·Ð¼ÐµÐ½ÐµÐ½Ð¸Ñ�
    // Ñ†Ð²ÐµÑ‚Ð°
    // Ð²Ð¾Ð»Ð¾Ñ�
    // -
    // Ñ‚Ð¸Ð¿
    // Ð¡?
    // Ð­Ñ„Ñ„ÐµÐºÑ‚
    // Ð¾Ñ‚
    // Ñ�Ñ‚Ð¾Ð³Ð¾
    // Ð·ÐµÐ»ÑŒÑ�
    // Ð¾Ñ�Ñ‚Ð°Ð½ÐµÑ‚Ñ�Ñ�
    // Ð½Ð°Ð²Ñ�ÐµÐ³Ð´Ð°.
    public static final int XDO_YOU_WISH_TO_USE_THE_DYE_POTION_X_TYPE_D_IT_IS_PERMANENT = 1094; // Ð’Ñ‹
    // Ñ…Ð¾Ñ‚Ð¸Ñ‚Ðµ
    // Ð²Ð¾Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒÑ�Ñ�
    // Ð·ÐµÐ»ÑŒÐµÐ¼
    // Ð¸Ð·Ð¼ÐµÐ½ÐµÐ½Ð¸Ñ�
    // Ñ†Ð²ÐµÑ‚Ð°
    // Ð²Ð¾Ð»Ð¾Ñ�
    // -
    // Ñ‚Ð¸Ð¿
    // D?
    // Ð­Ñ„Ñ„ÐµÐºÑ‚
    // Ð¾Ñ‚
    // Ñ�Ñ‚Ð¾Ð³Ð¾
    // Ð·ÐµÐ»ÑŒÑ�
    // Ð¾Ñ�Ñ‚Ð°Ð½ÐµÑ‚Ñ�Ñ�
    // Ð½Ð°Ð²Ñ�ÐµÐ³Ð´Ð°.
    public static final int XDO_YOU_WISH_TO_USE_THE_HAIR_STYLE_CHANGE_POTION_X_TYPE_B_IT_IS_PERMANENT = 1095; // Ð’Ñ‹
    // Ñ…Ð¾Ñ‚Ð¸Ñ‚Ðµ
    // Ð²Ð¾Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒÑ�Ñ�
    // Ð·ÐµÐ»ÑŒÐµÐ¼
    // Ð¸Ð·Ð¼ÐµÐ½ÐµÐ½Ð¸Ñ�
    // Ð¿Ñ€Ð¸Ñ‡ÐµÑ�ÐºÐ¸
    // -
    // Ñ‚Ð¸Ð¿
    // B?
    // Ð­Ñ„Ñ„ÐµÐºÑ‚
    // Ð¾Ñ‚
    // Ñ�Ñ‚Ð¾Ð³Ð¾
    // Ð·ÐµÐ»ÑŒÑ�
    // Ð¾Ñ�Ñ‚Ð°Ð½ÐµÑ‚Ñ�Ñ�
    // Ð½Ð°Ð²Ñ�ÐµÐ³Ð´Ð°.
    public static final int XDO_YOU_WISH_TO_USE_THE_HAIR_STYLE_CHANGE_POTION_X_TYPE_C_IT_IS_PERMANENT = 1096; // Ð’Ñ‹
    // Ñ…Ð¾Ñ‚Ð¸Ñ‚Ðµ
    // Ð²Ð¾Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒÑ�Ñ�
    // Ð·ÐµÐ»ÑŒÐµÐ¼
    // Ð¸Ð·Ð¼ÐµÐ½ÐµÐ½Ð¸Ñ�
    // Ð¿Ñ€Ð¸Ñ‡ÐµÑ�ÐºÐ¸
    // -
    // Ñ‚Ð¸Ð¿
    // C?
    // Ð­Ñ„Ñ„ÐµÐºÑ‚
    // Ð¾Ñ‚
    // Ñ�Ñ‚Ð¾Ð³Ð¾
    // Ð·ÐµÐ»ÑŒÑ�
    // Ð¾Ñ�Ñ‚Ð°Ð½ÐµÑ‚Ñ�Ñ�
    // Ð½Ð°Ð²Ñ�ÐµÐ³Ð´Ð°.
    public static final int XDO_YOU_WISH_TO_USE_THE_HAIR_STYLE_CHANGE_POTION_X_TYPE_D_IT_IS_PERMANENT = 1097; // Ð’Ñ‹
    // Ñ…Ð¾Ñ‚Ð¸Ñ‚Ðµ
    // Ð²Ð¾Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒÑ�Ñ�
    // Ð·ÐµÐ»ÑŒÐµÐ¼
    // Ð¸Ð·Ð¼ÐµÐ½ÐµÐ½Ð¸Ñ�
    // Ð¿Ñ€Ð¸Ñ‡ÐµÑ�ÐºÐ¸
    // -
    // Ñ‚Ð¸Ð¿
    // D?
    // Ð­Ñ„Ñ„ÐµÐºÑ‚
    // Ð¾Ñ‚
    // Ñ�Ñ‚Ð¾Ð³Ð¾
    // Ð·ÐµÐ»ÑŒÑ�
    // Ð¾Ñ�Ñ‚Ð°Ð½ÐµÑ‚Ñ�Ñ�
    // Ð½Ð°Ð²Ñ�ÐµÐ³Ð´Ð°.
    public static final int XDO_YOU_WISH_TO_USE_THE_HAIR_STYLE_CHANGE_POTION_X_TYPE_E_IT_IS_PERMANENT = 1098; // Ð’Ñ‹
    // Ñ…Ð¾Ñ‚Ð¸Ñ‚Ðµ
    // Ð²Ð¾Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒÑ�Ñ�
    // Ð·ÐµÐ»ÑŒÐµÐ¼
    // Ð¸Ð·Ð¼ÐµÐ½ÐµÐ½Ð¸Ñ�
    // Ð¿Ñ€Ð¸Ñ‡ÐµÑ�ÐºÐ¸
    // -
    // Ñ‚Ð¸Ð¿
    // E?
    // Ð­Ñ„Ñ„ÐµÐºÑ‚
    // Ð¾Ñ‚
    // Ñ�Ñ‚Ð¾Ð³Ð¾
    // Ð·ÐµÐ»ÑŒÑ�
    // Ð¾Ñ�Ñ‚Ð°Ð½ÐµÑ‚Ñ�Ñ�
    // Ð½Ð°Ð²Ñ�ÐµÐ³Ð´Ð°.
    public static final int XDO_YOU_WISH_TO_USE_THE_HAIR_STYLE_CHANGE_POTION_X_TYPE_F_IT_IS_PERMANENT = 1099; // Ð’Ñ‹
    // Ñ…Ð¾Ñ‚Ð¸Ñ‚Ðµ
    // Ð²Ð¾Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒÑ�Ñ�
    // Ð·ÐµÐ»ÑŒÐµÐ¼
    // Ð¸Ð·Ð¼ÐµÐ½ÐµÐ½Ð¸Ñ�
    // Ð¿Ñ€Ð¸Ñ‡ÐµÑ�ÐºÐ¸
    // -
    // Ñ‚Ð¸Ð¿
    // F?
    // Ð­Ñ„Ñ„ÐµÐºÑ‚
    // Ð¾Ñ‚
    // Ñ�Ñ‚Ð¾Ð³Ð¾
    // Ð·ÐµÐ»ÑŒÑ�
    // Ð¾Ñ�Ñ‚Ð°Ð½ÐµÑ‚Ñ�Ñ�
    // Ð½Ð°Ð²Ñ�ÐµÐ³Ð´Ð°.
    public static final int XDO_YOU_WISH_TO_USE_THE_HAIR_STYLE_CHANGE_POTION_X_TYPE_G_IT_IS_PERMANENT = 1100; // Ð’Ñ‹
    // Ñ…Ð¾Ñ‚Ð¸Ñ‚Ðµ
    // Ð²Ð¾Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒÑ�Ñ�
    // Ð·ÐµÐ»ÑŒÐµÐ¼
    // Ð¸Ð·Ð¼ÐµÐ½ÐµÐ½Ð¸Ñ�
    // Ð¿Ñ€Ð¸Ñ‡ÐµÑ�ÐºÐ¸
    // -
    // Ñ‚Ð¸Ð¿
    // G?
    // Ð­Ñ„Ñ„ÐµÐºÑ‚
    // Ð¾Ñ‚
    // Ñ�Ñ‚Ð¾Ð³Ð¾
    // Ð·ÐµÐ»ÑŒÑ�
    // Ð¾Ñ�Ñ‚Ð°Ð½ÐµÑ‚Ñ�Ñ�
    // Ð½Ð°Ð²Ñ�ÐµÐ³Ð´Ð°.
    public static final int THE_FACELIFTING_POTION__TYPE_B_IS_BEING_USED = 1101; // ÐŸÑ€Ð¸Ð¼ÐµÐ½ÐµÐ½Ð¾
    // Ð·ÐµÐ»ÑŒÐµ
    // Ð¸Ð·Ð¼ÐµÐ½ÐµÐ½Ð¸Ñ�
    // Ð²Ð½ÐµÑˆÐ½Ð¾Ñ�Ñ‚Ð¸
    // -
    // Ñ‚Ð¸Ð¿
    // B.
    public static final int THE_FACELIFTING_POTION__TYPE_C_IS_BEING_USED = 1102; // ÐŸÑ€Ð¸Ð¼ÐµÐ½ÐµÐ½Ð¾
    // Ð·ÐµÐ»ÑŒÐµ
    // Ð¸Ð·Ð¼ÐµÐ½ÐµÐ½Ð¸Ñ�
    // Ð²Ð½ÐµÑˆÐ½Ð¾Ñ�Ñ‚Ð¸
    // -
    // Ñ‚Ð¸Ð¿
    // C.
    public static final int THE_DYE_POTION__TYPE_B_IS_BEING_USED = 1103; // ÐŸÑ€Ð¸Ð¼ÐµÐ½ÐµÐ½Ð¾
    // Ð·ÐµÐ»ÑŒÐµ
    // Ð¸Ð·Ð¼ÐµÐ½ÐµÐ½Ð¸Ñ�
    // Ñ†Ð²ÐµÑ‚Ð°
    // Ð²Ð¾Ð»Ð¾Ñ�
    // -
    // Ñ‚Ð¸Ð¿
    // B.
    public static final int THE_DYE_POTION__TYPE_C_IS_BEING_USED = 1104; // ÐŸÑ€Ð¸Ð¼ÐµÐ½ÐµÐ½Ð¾
    // Ð·ÐµÐ»ÑŒÐµ
    // Ð¸Ð·Ð¼ÐµÐ½ÐµÐ½Ð¸Ñ�
    // Ñ†Ð²ÐµÑ‚Ð°
    // Ð²Ð¾Ð»Ð¾Ñ�
    // -
    // Ñ‚Ð¸Ð¿
    // C.
    public static final int THE_DYE_POTION__TYPE_D_IS_BEING_USED = 1105; // ÐŸÑ€Ð¸Ð¼ÐµÐ½ÐµÐ½Ð¾
    // Ð·ÐµÐ»ÑŒÐµ
    // Ð¸Ð·Ð¼ÐµÐ½ÐµÐ½Ð¸Ñ�
    // Ñ†Ð²ÐµÑ‚Ð°
    // Ð²Ð¾Ð»Ð¾Ñ�
    // -
    // Ñ‚Ð¸Ð¿
    // D.
    public static final int THE_HAIR_STYLE_CHANGE_POTION__TYPE_B_IS_BEING_USED = 1106; // ÐŸÑ€Ð¸Ð¼ÐµÐ½ÐµÐ½Ð¾
    // Ð·ÐµÐ»ÑŒÐµ
    // Ð¸Ð·Ð¼ÐµÐ½ÐµÐ½Ð¸Ñ�
    // Ð¿Ñ€Ð¸Ñ‡ÐµÑ�ÐºÐ¸
    // -
    // Ñ‚Ð¸Ð¿
    // B.
    public static final int THE_HAIR_STYLE_CHANGE_POTION__TYPE_C_IS_BEING_USED = 1107; // ÐŸÑ€Ð¸Ð¼ÐµÐ½ÐµÐ½Ð¾
    // Ð·ÐµÐ»ÑŒÐµ
    // Ð¸Ð·Ð¼ÐµÐ½ÐµÐ½Ð¸Ñ�
    // Ð¿Ñ€Ð¸Ñ‡ÐµÑ�ÐºÐ¸
    // -
    // Ñ‚Ð¸Ð¿
    // C.
    public static final int THE_HAIR_STYLE_CHANGE_POTION__TYPE_D_IS_BEING_USED = 1108; // ÐŸÑ€Ð¸Ð¼ÐµÐ½ÐµÐ½Ð¾
    // Ð·ÐµÐ»ÑŒÐµ
    // Ð¸Ð·Ð¼ÐµÐ½ÐµÐ½Ð¸Ñ�
    // Ð¿Ñ€Ð¸Ñ‡ÐµÑ�ÐºÐ¸
    // -
    // Ñ‚Ð¸Ð¿
    // D.
    public static final int THE_HAIR_STYLE_CHANGE_POTION__TYPE_E_IS_BEING_USED = 1109; // ÐŸÑ€Ð¸Ð¼ÐµÐ½ÐµÐ½Ð¾
    // Ð·ÐµÐ»ÑŒÐµ
    // Ð¸Ð·Ð¼ÐµÐ½ÐµÐ½Ð¸Ñ�
    // Ð¿Ñ€Ð¸Ñ‡ÐµÑ�ÐºÐ¸
    // -
    // Ñ‚Ð¸Ð¿
    // E.
    public static final int THE_HAIR_STYLE_CHANGE_POTION__TYPE_F_IS_BEING_USED = 1110; // ÐŸÑ€Ð¸Ð¼ÐµÐ½ÐµÐ½Ð¾
    // Ð·ÐµÐ»ÑŒÐµ
    // Ð¸Ð·Ð¼ÐµÐ½ÐµÐ½Ð¸Ñ�
    // Ð¿Ñ€Ð¸Ñ‡ÐµÑ�ÐºÐ¸
    // -
    // Ñ‚Ð¸Ð¿
    // F.
    public static final int THE_HAIR_STYLE_CHANGE_POTION__TYPE_G_IS_BEING_USED = 1111; // ÐŸÑ€Ð¸Ð¼ÐµÐ½ÐµÐ½Ð¾
    // Ð·ÐµÐ»ÑŒÐµ
    // Ð¸Ð·Ð¼ÐµÐ½ÐµÐ½Ð¸Ñ�
    // Ð¿Ñ€Ð¸Ñ‡ÐµÑ�ÐºÐ¸
    // -
    // Ñ‚Ð¸Ð¿
    // G.
    public static final int THE_PRIZE_AMOUNT_FOR_THE_WINNER_OF_LOTTERY__S1__IS_S2_ADENA_WE_HAVE_S3_FIRST_PRIZE_WINNERS = 1112; // Ð”Ð¶ÐµÐºÐ¿Ð¾Ñ‚
    // Ð»Ð¾Ñ‚ÐµÑ€ÐµÐ¸
    // $s1
    // Ñ�Ð¾Ñ�Ñ‚Ð°Ð²Ð¸Ð»
    // $s2
    // Ð°Ð´ÐµÐ½.
    // ÐšÐ¾Ð»Ð¸Ñ‡ÐµÑ�Ñ‚Ð²Ð¾
    // Ð¿Ð¾Ð±ÐµÐ´Ð¸Ñ‚ÐµÐ»ÐµÐ¹:
    // $s3
    // Ñ‡ÐµÐ».
    public static final int THE_PRIZE_AMOUNT_FOR_LUCKY_LOTTERY__S1__IS_S2_ADENA_THERE_WAS_NO_FIRST_PRIZE_WINNER_IN_THIS_DRAWING_THEREFORE_THE_JACKPOT_WILL_BE_ADDED_TO_THE_NEXT_DRAWING = 1113; // Ð”Ð¶ÐµÐºÐ¿Ð¾Ñ‚
    // Ð»Ð¾Ñ‚ÐµÑ€ÐµÐ¸
    // $s1
    // Ñ�Ð¾Ñ�Ñ‚Ð°Ð²Ð¸Ð»
    // $s2
    // Ð°Ð´ÐµÐ½.
    // ÐŸÐµÑ€Ð²Ð¾Ðµ
    // Ð¼ÐµÑ�Ñ‚Ð¾
    // Ð½Ðµ
    // Ð·Ð°Ð½Ñ�Ð»
    // Ð½Ð¸ÐºÑ‚Ð¾.
    // Ð”Ð°Ð½Ð½Ñ‹Ð¹
    // Ð´Ð¶ÐµÐºÐ¿Ð¾Ñ‚
    // Ð±ÑƒÐ´ÐµÑ‚
    // Ñ€Ð°Ð·Ñ‹Ð³Ñ€Ð°Ð½
    // Ð²
    // Ñ�Ð»ÐµÐ´ÑƒÑŽÑ‰ÐµÐ¹
    // Ð»Ð¾Ñ‚ÐµÑ€ÐµÐµ.
    public static final int YOUR_CLAN_MAY_NOT_REGISTER_TO_PARTICIPATE_IN_A_SIEGE_WHILE_UNDER_A_GRACE_PERIOD_OF_THE_CLANS_DISSOLUTION = 1114; // Ð’Ð¾
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ñ€Ð¾Ñ�Ð¿ÑƒÑ�ÐºÐ°
    // ÐºÐ»Ð°Ð½Ð°
    // Ð·Ð°Ñ€ÐµÐ³Ð¸Ñ�Ñ‚Ñ€Ð¸Ñ€Ð¾Ð²Ð°Ñ‚ÑŒÑ�Ñ�
    // Ð²
    // Ð¾Ñ�Ð°Ð´Ðµ
    // Ð·Ð°Ð¼ÐºÐ°
    // Ð½ÐµÐ»ÑŒÐ·Ñ�.
    public static final int INDIVIDUALS_MAY_NOT_SURRENDER_DURING_COMBAT = 1115; // Ð’Ð¾
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ð¸Ð½Ð´Ð¸Ð²Ð¸Ð´ÑƒÐ°Ð»ÑŒÐ½Ð¾Ð¹
    // Ð±Ð¸Ñ‚Ð²Ñ‹
    // Ñ�Ð´Ð°Ñ‚ÑŒÑ�Ñ�
    // Ð½ÐµÐ»ÑŒÐ·Ñ�.
    public static final int ONE_CANNOT_LEAVE_ONES_CLAN_DURING_COMBAT = 1116; // Ð’Ð¾
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ð±Ð¸Ñ‚Ð²Ñ‹
    // ÑƒÐ¹Ñ‚Ð¸
    // Ð¸Ð·
    // ÐºÐ»Ð°Ð½Ð°
    // Ð½ÐµÐ»ÑŒÐ·Ñ�.
    public static final int A_CLAN_MEMBER_MAY_NOT_BE_DISMISSED_DURING_COMBAT = 1117; // Ð’Ð¾
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ð±Ð¸Ñ‚Ð²Ñ‹
    // Ð¸Ð·Ð³Ð½Ð°Ñ‚ÑŒ
    // Ñ‡Ð»ÐµÐ½Ð°
    // ÐºÐ»Ð°Ð½Ð°
    // Ð½ÐµÐ»ÑŒÐ·Ñ�.
    public static final int PROGRESS_IN_A_QUEST_IS_POSSIBLE_ONLY_WHEN_YOUR_INVENTORYS_WEIGHT_AND_VOLUME_ARE_LESS_THAN_80_PERCENT_OF_CAPACITY = 1118; // Ð§Ñ‚Ð¾Ð±Ñ‹
    // Ð¿Ñ€Ð¾Ð´Ð¾Ð»Ð¶Ð¸Ñ‚ÑŒ
    // ÐºÐ²ÐµÑ�Ñ‚,
    // Ñ€Ð°Ð·Ð³Ñ€ÑƒÐ·Ð¸Ñ‚Ðµ
    // Ð¸Ð½Ð²ÐµÐ½Ñ‚Ð°Ñ€ÑŒ
    // (Ð¼ÐµÐ½ÐµÐµ
    // 80%).
    public static final int QUEST_WAS_AUTOMATICALLY_CANCELED_WHEN_YOU_ATTEMPTED_TO_SETTLE_THE_ACCOUNTS_OF_YOUR_QUEST_WHILE_YOUR_INVENTORY_EXCEEDED_80_PERCENT_OF_CAPACITY = 1119; // Ð˜Ð·-Ð·Ð°
    // Ð½ÐµÐ²ÐµÑ€Ð½Ð¾Ð¹
    // Ð¿Ð¾Ð¿Ñ‹Ñ‚ÐºÐ¸
    // Ð²Ñ‹Ð¿Ð¾Ð»Ð½ÐµÐ½Ð¸Ðµ
    // ÐºÐ²ÐµÑ�Ñ‚Ð°
    // Ð¾Ñ‚Ð¼ÐµÐ½ÐµÐ½Ð¾.
    public static final int YOU_ARE_STILL_IN_THE_CLAN = 1120; // ÐžÑˆÐ¸Ð±ÐºÐ° Ð¿Ñ€Ð¸
    // Ð¿Ð¾Ð¿Ñ‹Ñ‚ÐºÐµ
    // Ð¿Ð¾ÐºÐ¸Ð½ÑƒÑ‚ÑŒ ÐºÐ»Ð°Ð½.
    public static final int YOU_DO_NOT_HAVE_THE_RIGHT_TO_VOTE = 1121; // Ð£ Ð’Ð°Ñ�
    // Ð½ÐµÑ‚
    // Ð¿Ñ€Ð°Ð²Ð°
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð²Ð¾Ð²Ð°Ñ‚ÑŒ
    // Ð²
    // Ð²Ñ‹Ð±Ð¾Ñ€Ð°Ñ….
    public static final int THERE_IS_NO_CANDIDATE = 1122; // Ð�ÐµÑ‚ ÐºÐ°Ð½Ð´Ð¸Ð´Ð°Ñ‚Ð¾Ð².
    public static final int WEIGHT_AND_VOLUME_LIMIT_HAS_BEEN_EXCEEDED_THAT_SKILL_IS_CURRENTLY_UNAVAILABLE = 1123; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒ
    // ÑƒÐ¼ÐµÐ½Ð¸Ðµ
    // Ð¸Ð·-Ð·Ð°
    // Ð¿ÐµÑ€ÐµÐ³Ñ€ÑƒÐ·Ð°.
    public static final int A_RECIPE_BOOK_MAY_NOT_BE_USED_WHILE_USING_A_SKILL = 1124; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒ
    // ÐºÐ½Ð¸Ð³Ñƒ
    // Ñ€ÐµÑ†ÐµÐ¿Ñ‚Ð¾Ð²
    // Ð¾Ð´Ð½Ð¾Ð²Ñ€ÐµÐ¼ÐµÐ½Ð½Ð¾
    // Ñ�
    // ÑƒÐ¼ÐµÐ½Ð¸ÐµÐ¼.
    public static final int AN_ITEM_MAY_NOT_BE_CREATED_WHILE_ENGAGED_IN_TRADING = 1125; // Ð’
    // Ñ€ÐµÐ¶Ð¸Ð¼Ðµ
    // Ð¾Ð±Ð¼ÐµÐ½Ð°
    // Ñ„ÑƒÐ½ÐºÑ†Ð¸Ñ�
    // Ð¸Ð·Ð³Ð¾Ñ‚Ð¾Ð²Ð»ÐµÐ½Ð¸Ñ�
    // Ð½ÐµÐ´Ð¾Ñ�Ñ‚ÑƒÐ¿Ð½Ð°.
    public static final int YOU_MAY_NOT_ENTER_A_NEGATIVE_NUMBER = 1126; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ð²Ð²ÐµÑ�Ñ‚Ð¸
    // Ð¾Ñ‚Ñ€Ð¸Ñ†Ð°Ñ‚ÐµÐ»ÑŒÐ½Ð¾Ðµ
    // Ñ‡Ð¸Ñ�Ð»Ð¾.
    public static final int THE_REWARD_MUST_BE_LESS_THAN_10_TIMES_THE_STANDARD_PRICE = 1127; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // ÑƒÑ�Ñ‚Ð°Ð½Ð¾Ð²Ð¸Ñ‚ÑŒ
    // Ð½Ð°Ð³Ñ€Ð°Ð´Ñƒ,
    // Ð²
    // 10
    // Ñ€Ð°Ð·
    // Ð¿Ñ€ÐµÐ²Ñ‹ÑˆÐ°ÑŽÑ‰ÑƒÑŽ
    // Ð¾Ð±Ñ‹Ñ‡Ð½ÑƒÑŽ
    // Ñ†ÐµÐ½Ñƒ.
    public static final int A_PRIVATE_STORE_MAY_NOT_BE_OPENED_WHILE_USING_A_SKILL = 1128; // Ð’Ð¾
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ð½Ð¸Ñ�
    // ÑƒÐ¼ÐµÐ½Ð¸Ñ�
    // Ñ„ÑƒÐ½ÐºÑ†Ð¸Ð¸
    // Ñ‚Ð¾Ñ€Ð³Ð¾Ð²Ð»Ð¸
    // Ð¸
    // Ð¸Ð·Ð³Ð¾Ñ‚Ð¾Ð²Ð»ÐµÐ½Ð¸Ñ�
    // Ð½ÐµÐ´Ð¾Ñ�Ñ‚ÑƒÐ¿Ð½Ñ‹.
    public static final int THIS_IS_NOT_ALLOWED_WHILE_USING_A_FERRY = 1129; // Ð”Ð°Ð½Ð½Ð°Ñ�
    // Ñ„ÑƒÐ½ÐºÑ†Ð¸Ñ�
    // Ð½ÐµÐ´Ð¾Ñ�Ñ‚ÑƒÐ¿Ð½Ð°
    // Ð²Ð¾
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ð¿Ð»Ð°Ð²Ð°Ð½Ð¸Ñ�.
    public static final int YOU_HAVE_GIVEN_S1_DAMAGE_TO_YOUR_TARGET_AND_S2_DAMAGE_TO_THE_SERVITOR = 1130; // Ð’Ñ‹
    // Ð½Ð°Ð½ÐµÑ�Ð»Ð¸
    // $s1
    // ÑƒÑ€Ð¾Ð½Ð°
    // Ð²Ñ€Ð°Ð³Ñƒ
    // Ð¸
    // $s2
    // -
    // ÐµÐ³Ð¾
    // Ñ�Ð»ÑƒÐ³Ðµ.
    public static final int IT_IS_NOW_MIDNIGHT_AND_THE_EFFECT_OF_S1_CAN_BE_FELT = 1131; // Ð¡ÐµÐ¹Ñ‡Ð°Ñ�
    // ÑƒÐ¶Ðµ
    // Ð¿Ð¾Ð»Ð½Ð¾Ñ‡ÑŒ,
    // Ð¸
    // Ð¼Ð¾Ð¶Ð½Ð¾
    // Ð¾Ñ‰ÑƒÑ‚Ð¸Ñ‚ÑŒ
    // Ñ�Ñ„Ñ„ÐµÐºÑ‚
    // $s1.
    public static final int IT_IS_DAWN_AND_THE_EFFECT_OF_S1_WILL_NOW_DISAPPEAR = 1132; // Ð�Ð°Ñ�Ñ‚Ð°Ð»Ð¾
    // ÑƒÑ‚Ñ€Ð¾,
    // Ð¸
    // Ñ�Ñ„Ñ„ÐµÐºÑ‚
    // $s1
    // Ð¿Ñ€Ð¾Ð¿Ð°Ð».
    public static final int SINCE_HP_HAS_DECREASED_THE_EFFECT_OF_S1_CAN_BE_FELT = 1133; // Ð£
    // Ð’Ð°Ñ�
    // Ð¼Ð°Ð»Ð¾
    // HP.
    // Ð’Ñ‹
    // Ð¾Ñ‰ÑƒÑ‰Ð°ÐµÑ‚Ðµ
    // Ñ�Ñ„Ñ„ÐµÐºÑ‚
    // ÑƒÐ¼ÐµÐ½Ð¸Ñ�
    // $s1.
    public static final int SINCE_HP_HAS_INCREASED_THE_EFFECT_OF_S1_WILL_DISAPPEAR = 1134; // ÐšÐ¾Ð»Ð¸Ñ‡ÐµÑ�Ñ‚Ð²Ð¾
    // HP
    // ÑƒÐ²ÐµÐ»Ð¸Ñ‡Ð¸Ð»Ð¾Ñ�ÑŒ.
    // Ð”ÐµÐ¹Ñ�Ñ‚Ð²Ð¸Ðµ
    // ÑƒÐ¼ÐµÐ½Ð¸Ñ�
    // $s1
    // Ð¿Ñ€ÐµÐºÑ€Ð°Ñ‰Ð°ÐµÑ‚Ñ�Ñ�.
    public static final int WHILE_YOU_ARE_ENGAGED_IN_COMBAT_YOU_CANNOT_OPERATE_A_PRIVATE_STORE_OR_PRIVATE_WORKSHOP = 1135; // Ð’Ð¾
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ð±Ð¸Ñ‚Ð²Ñ‹
    // Ñ„ÑƒÐ½ÐºÑ†Ð¸Ñ�Ð¼Ð¸
    // Ñ‚Ð¾Ñ€Ð³Ð¾Ð²Ð»Ð¸
    // Ð¸Ð»Ð¸
    // Ð¸Ð·Ð³Ð¾Ñ‚Ð¾Ð²Ð»ÐµÐ½Ð¸Ñ�
    // Ð²Ð¾Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒÑ�Ñ�
    // Ð½ÐµÐ»ÑŒÐ·Ñ�.
    public static final int SINCE_THERE_WAS_AN_ACCOUNT_THAT_USED_THIS_IP_AND_ATTEMPTED_TO_LOG_IN_ILLEGALLY_THIS_ACCOUNT_IS_NOT_ALLOWED_TO_CONNECT_TO_THE_GAME_SERVER_FOR_S1_MINUTES_PLEASE_USE_ANOTHER_GAME_SERVER = 1136; // Ð‘Ñ‹Ð»Ð°
    // Ð¿Ð¾Ð¿Ñ‹Ñ‚ÐºÐ°
    // Ð½ÐµÐ»ÐµÐ³Ð°Ð»ÑŒÐ½Ð¾Ð³Ð¾
    // Ð²Ñ…Ð¾Ð´Ð°
    // Ñ�
    // Ñ�Ñ‚Ð¾Ð³Ð¾
    // IP,
    // Ð¿Ð¾Ñ�Ñ‚Ð¾Ð¼Ñƒ
    // Ð¿Ð¾Ð´ÐºÐ»ÑŽÑ‡Ð¸Ñ‚ÑŒÑ�Ñ�
    // Ðº
    // Ñ�ÐµÑ€Ð²ÐµÑ€Ñƒ
    // Ð½ÐµÐ»ÑŒÐ·Ñ�.
    // ÐŸÐ¾Ð¿Ñ€Ð¾Ð±ÑƒÐ¹Ñ‚Ðµ
    // Ð²Ð¾Ð¹Ñ‚Ð¸
    // Ð½Ð°
    // Ð´Ñ€ÑƒÐ³Ð¾Ð¹
    // Ñ�ÐµÑ€Ð²ÐµÑ€.
    public static final int S1_HARVESTED_S3_S2_S = 1137; // $c1 Ð¿Ð¾Ð»ÑƒÑ‡Ð°ÐµÑ‚: $s2
    // $s3 ÑˆÑ‚ÑƒÐº.
    public static final int S1_HARVESTED_S2_S = 1138; // $c1 Ð¿Ð¾Ð»ÑƒÑ‡Ð°ÐµÑ‚: $s2.
    public static final int THE_WEIGHT_AND_VOLUME_LIMIT_OF_INVENTORY_MUST_NOT_BE_EXCEEDED = 1139; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ð¿Ñ€ÐµÐ²Ñ‹Ñ�Ð¸Ñ‚ÑŒ
    // Ð»Ð¸Ð¼Ð¸Ñ‚
    // Ð²ÐµÑ�Ð°.
    public static final int WOULD_YOU_LIKE_TO_OPEN_THE_GATE = 1140; // Ð’Ñ‹ Ñ…Ð¾Ñ‚Ð¸Ñ‚Ðµ
    // Ð¾Ñ‚ÐºÑ€Ñ‹Ñ‚ÑŒ
    // Ð²Ñ€Ð°Ñ‚Ð°?
    public static final int WOULD_YOU_LIKE_TO_CLOSE_THE_GATE = 1141; // Ð’Ñ‹
    // Ñ…Ð¾Ñ‚Ð¸Ñ‚Ðµ
    // Ð·Ð°ÐºÑ€Ñ‹Ñ‚ÑŒ
    // Ð²Ñ€Ð°Ñ‚Ð°?
    public static final int SINCE_S1_ALREADY_EXISTS_NEARBY_YOU_CANNOT_SUMMON_IT_AGAIN = 1142; // $s1
    // ÑƒÐ¶Ðµ
    // Ñ€Ñ�Ð´Ð¾Ð¼
    // Ñ�
    // Ð’Ð°Ð¼Ð¸,
    // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¿Ñ€Ð¸Ð·Ð²Ð°Ñ‚ÑŒ
    // ÐµÐ³Ð¾
    // Ñ�Ð½Ð¾Ð²Ð°.
    public static final int SINCE_YOU_DO_NOT_HAVE_ENOUGH_ITEMS_TO_MAINTAIN_THE_SERVITORS_STAY_THE_SERVITOR_WILL_DISAPPEAR = 1143; // Ð¡Ð»ÑƒÐ³Ð°
    // Ð¸Ñ�Ñ‡ÐµÐ·Ð½ÐµÑ‚,
    // Ñ‚Ð°Ðº
    // ÐºÐ°Ðº
    // Ñƒ
    // Ð’Ð°Ñ�
    // Ð½Ðµ
    // Ñ…Ð²Ð°Ñ‚Ð°ÐµÑ‚
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ð¾Ð²
    // Ð´Ð»Ñ�
    // ÐµÐ³Ð¾
    // Ñ�Ð¾Ð´ÐµÑ€Ð¶Ð°Ð½Ð¸Ñ�.
    public static final int CURRENTLY_YOU_DONT_HAVE_ANYBODY_TO_CHAT_WITH_IN_THE_GAME = 1144; // Ð�Ð¸
    // Ð¾Ð´Ð½Ð¾Ð³Ð¾
    // Ð¸Ð·
    // Ð’Ð°ÑˆÐ¸Ñ…
    // Ð´Ñ€ÑƒÐ·ÐµÐ¹
    // Ð½ÐµÑ‚
    // Ð²
    // Ð¸Ð³Ñ€Ðµ.
    public static final int S2_HAS_BEEN_CREATED_FOR_S1_AFTER_THE_PAYMENT_OF_S3_ADENA_IS_RECEIVED = 1145; // Ð˜Ð·Ð³Ð¾Ñ‚Ð¾Ð²Ð»ÐµÐ½Ð¾
    // Ð´Ð»Ñ�
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°
    // $c1:
    // $s2
    // Ð·Ð°
    // $s3
    // Ð°Ð´ÐµÐ½.
    public static final int S1_CREATED_S2_AFTER_RECEIVING_S3_ADENA = 1146; // $c1
    // Ñ�Ð¾Ð·Ð´Ð°ÐµÑ‚
    // Ð´Ð»Ñ�
    // Ð’Ð°Ñ�
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚
    // $s2
    // Ð·Ð°
    // $s3
    // Ð°Ð´ÐµÐ½.
    public static final int S2_S3_HAVE_BEEN_CREATED_FOR_S1_AT_THE_PRICE_OF_S4_ADENA = 1147; // Ð˜Ð·Ð³Ð¾Ñ‚Ð¾Ð²Ð»ÐµÐ½Ð¾
    // Ð´Ð»Ñ�
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°
    // $c1:
    // $s2
    // ($s3
    // ÑˆÑ‚.)
    // Ð·Ð°
    // $s4
    // Ð°Ð´ÐµÐ½.
    public static final int S1_CREATED_S2_S3_AT_THE_PRICE_OF_S4_ADENA = 1148; // $c1
    // Ñ�Ð¾Ð·Ð´Ð°ÐµÑ‚
    // Ð´Ð»Ñ�
    // Ð’Ð°Ñ�
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚
    // $s2
    // ($s3
    // ÑˆÑ‚.)
    // Ð·Ð°
    // $s4
    // Ð°Ð´ÐµÐ½.
    public static final int THE_ATTEMPT_TO_CREATE_S2_FOR_S1_AT_THE_PRICE_OF_S3_ADENA_HAS_FAILED = 1149; // Ð�Ðµ
    // ÑƒÐ´Ð°Ð»Ð¾Ñ�ÑŒ
    // Ð¸Ð·Ð³Ð¾Ñ‚Ð¾Ð²Ð¸Ñ‚ÑŒ
    // Ð´Ð»Ñ�
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°
    // $c1:
    // $s2
    // Ð·Ð°
    // $s3
    // Ð°Ð´ÐµÐ½.
    public static final int S1_HAS_FAILED_TO_CREATE_S2_AT_THE_PRICE_OF_S3_ADENA = 1150; // $c1
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð¸Ð·Ð³Ð¾Ñ‚Ð¾Ð²Ð¸Ñ‚ÑŒ
    // Ð´Ð»Ñ�
    // Ð’Ð°Ñ�
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚
    // $s2
    // Ð·Ð°
    // $s3
    // Ð°Ð´ÐµÐ½.
    public static final int S2_IS_SOLD_TO_S1_AT_THE_PRICE_OF_S3_ADENA = 1151; // $c1
    // Ð¿Ð¾ÐºÑƒÐ¿Ð°ÐµÑ‚
    // Ñƒ
    // Ð’Ð°Ñ�
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚
    // $s2
    // Ð·Ð°
    // $s3
    // Ð°Ð´ÐµÐ½.
    public static final int S2_S3_HAVE_BEEN_SOLD_TO_S1_FOR_S4_ADENA = 1152; // $c1
    // Ð¿Ð¾ÐºÑƒÐ¿Ð°ÐµÑ‚
    // Ñƒ
    // Ð’Ð°Ñ�
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚
    // $s2
    // ($s3
    // ÑˆÑ‚.)
    // Ð·Ð°
    // $s4
    // Ð°Ð´ÐµÐ½.
    public static final int S2_HAS_BEEN_PURCHASED_FROM_S1_AT_THE_PRICE_OF_S3_ADENA = 1153; // ÐšÑƒÐ¿Ð»ÐµÐ½Ð¾
    // Ñƒ
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°
    // $c1:
    // $s2
    // Ð·Ð°
    // $s3
    // Ð°Ð´ÐµÐ½.
    public static final int S3_S2_HAS_BEEN_PURCHASED_FROM_S1_FOR_S4_ADENA = 1154; // ÐšÑƒÐ¿Ð»ÐµÐ½Ð¾
    // Ñƒ
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°
    // $c1:
    // $s2
    // ($s3
    // ÑˆÑ‚.)
    // Ð·Ð°
    // $s4
    // Ð°Ð´ÐµÐ½.
    public static final int _S2S3_HAS_BEEN_SOLD_TO_S1_AT_THE_PRICE_OF_S4_ADENA = 1155; // ÐŸÑ€Ð¾Ð´Ð°Ð½Ð¾
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ñƒ
    // $c1:
    // +$s2
    // $s3
    // Ð·Ð°
    // $s4
    // Ð°Ð´ÐµÐ½.
    public static final int _S2S3_HAS_BEEN_PURCHASED_FROM_S1_AT_THE_PRICE_OF_S4_ADENA = 1156; // ÐšÑƒÐ¿Ð»ÐµÐ½Ð¾
    // Ñƒ
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°
    // $c1:
    // +$s2
    // $s3
    // Ð·Ð°
    // $s4
    // Ð°Ð´ÐµÐ½.
    public static final int TRYING_ON_STATE_LASTS_FOR_ONLY_5_SECONDS_WHEN_A_CHARACTERS_STATE_CHANGES_IT_CAN_BE_CANCELLED = 1157; // ÐŸÑ€Ð¸Ð¼ÐµÑ€ÐºÐ°
    // Ñ�Ð½Ð°Ñ€Ñ�Ð¶ÐµÐ½Ð¸Ñ�
    // Ð´Ð»Ð¸Ñ‚Ñ�Ñ�
    // 10
    // Ñ�ÐµÐºÑƒÐ½Ð´.
    // Ð’
    // Ñ�Ð»ÑƒÑ‡Ð°Ðµ
    // Ð¸Ð·Ð¼ÐµÐ½ÐµÐ½Ð¸Ñ�
    // Ð’Ð°ÑˆÐµÐ³Ð¾
    // Ñ�Ð¾Ñ�Ñ‚Ð¾Ñ�Ð½Ð¸Ñ�
    // Ñ€ÐµÐ¶Ð¸Ð¼
    // Ð¿Ñ€Ð¸Ð¼ÐµÑ€ÐºÐ¸
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð¾Ñ‚Ð¼ÐµÐ½ÐµÐ½.
    public static final int YOU_CANNOT_GET_DOWN_FROM_A_PLACE_THAT_IS_TOO_HIGH = 1158; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ñ�Ð¿ÐµÑˆÐ¸Ñ‚ÑŒÑ�Ñ�
    // Ñ�
    // Ñ‚Ð°ÐºÐ¾Ð¹
    // Ð²Ñ‹Ñ�Ð¾Ñ‚Ñ‹.
    public static final int THE_FERRY_FROM_TALKING_ISLAND_WILL_ARRIVE_AT_GLUDIN_HARBOR_IN_APPROXIMATELY_10_MINUTES = 1159; // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ
    // Ñ�
    // Ð“Ð¾Ð²Ð¾Ñ€Ñ�Ñ‰ÐµÐ³Ð¾
    // ÐžÑ�Ñ‚Ñ€Ð¾Ð²Ð°
    // Ð¿Ñ€Ð¸Ð±ÑƒÐ´ÐµÑ‚
    // Ð²
    // Ð³Ð°Ð²Ð°Ð½ÑŒ
    // Ð“Ð»ÑƒÐ´Ð¸Ð½Ð°
    // Ñ‡ÐµÑ€ÐµÐ·
    // 10
    // Ð¼Ð¸Ð½.
    public static final int THE_FERRY_FROM_TALKING_ISLAND_WILL_BE_ARRIVING_AT_GLUDIN_HARBOR_IN_APPROXIMATELY_5_MINUTES = 1160; // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ
    // Ñ�
    // Ð“Ð¾Ð²Ð¾Ñ€Ñ�Ñ‰ÐµÐ³Ð¾
    // ÐžÑ�Ñ‚Ñ€Ð¾Ð²Ð°
    // Ð¿Ñ€Ð¸Ð±ÑƒÐ´ÐµÑ‚
    // Ð²
    // Ð³Ð°Ð²Ð°Ð½ÑŒ
    // Ð“Ð»ÑƒÐ´Ð¸Ð½Ð°
    // Ñ‡ÐµÑ€ÐµÐ·
    // 5
    // Ð¼Ð¸Ð½.
    public static final int THE_FERRY_FROM_TALKING_ISLAND_WILL_BE_ARRIVING_AT_GLUDIN_HARBOR_IN_APPROXIMATELY_1_MINUTE = 1161; // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ
    // Ñ�
    // Ð“Ð¾Ð²Ð¾Ñ€Ñ�Ñ‰ÐµÐ³Ð¾
    // ÐžÑ�Ñ‚Ñ€Ð¾Ð²Ð°
    // Ð¿Ñ€Ð¸Ð±ÑƒÐ´ÐµÑ‚
    // Ð²
    // Ð³Ð°Ð²Ð°Ð½ÑŒ
    // Ð“Ð»ÑƒÐ´Ð¸Ð½Ð°
    // Ñ‡ÐµÑ€ÐµÐ·
    // 1
    // Ð¼Ð¸Ð½.
    public static final int THE_FERRY_FROM_GIRAN_HARBOR_WILL_BE_ARRIVING_AT_TALKING_ISLAND_IN_APPROXIMATELY_15_MINUTES = 1162; // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ
    // Ð¸Ð·
    // Ð³Ð°Ð²Ð°Ð½Ð¸
    // Ð“Ð¸Ñ€Ð°Ð½Ð°
    // Ð¿Ñ€Ð¸Ð±ÑƒÐ´ÐµÑ‚
    // Ð½Ð°
    // Ð“Ð¾Ð²Ð¾Ñ€Ñ�Ñ‰Ð¸Ð¹
    // ÐžÑ�Ñ‚Ñ€Ð¾Ð²
    // Ñ‡ÐµÑ€ÐµÐ·
    // 15
    // Ð¼Ð¸Ð½.
    public static final int THE_FERRY_FROM_GIRAN_HARBOR_WILL_BE_ARRIVING_AT_TALKING_ISLAND_IN_APPROXIMATELY_10_MINUTES = 1163; // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ
    // Ð¸Ð·
    // Ð³Ð°Ð²Ð°Ð½Ð¸
    // Ð“Ð¸Ñ€Ð°Ð½Ð°
    // Ð¿Ñ€Ð¸Ð±ÑƒÐ´ÐµÑ‚
    // Ð½Ð°
    // Ð“Ð¾Ð²Ð¾Ñ€Ñ�Ñ‰Ð¸Ð¹
    // ÐžÑ�Ñ‚Ñ€Ð¾Ð²
    // Ñ‡ÐµÑ€ÐµÐ·
    // 10
    // Ð¼Ð¸Ð½.
    public static final int THE_FERRY_FROM_GIRAN_HARBOR_WILL_BE_ARRIVING_AT_TALKING_ISLAND_IN_APPROXIMATELY_5_MINUTES = 1164; // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ
    // Ð¸Ð·
    // Ð³Ð°Ð²Ð°Ð½Ð¸
    // Ð“Ð¸Ñ€Ð°Ð½Ð°
    // Ð¿Ñ€Ð¸Ð±ÑƒÐ´ÐµÑ‚
    // Ð½Ð°
    // Ð“Ð¾Ð²Ð¾Ñ€Ñ�Ñ‰Ð¸Ð¹
    // ÐžÑ�Ñ‚Ñ€Ð¾Ð²
    // Ñ‡ÐµÑ€ÐµÐ·
    // 5
    // Ð¼Ð¸Ð½.
    public static final int THE_FERRY_FROM_GIRAN_HARBOR_WILL_BE_ARRIVING_AT_TALKING_ISLAND_IN_APPROXIMATELY_1_MINUTE = 1165; // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ
    // Ð¸Ð·
    // Ð³Ð°Ð²Ð°Ð½Ð¸
    // Ð“Ð¸Ñ€Ð°Ð½Ð°
    // Ð¿Ñ€Ð¸Ð±ÑƒÐ´ÐµÑ‚
    // Ð½Ð°
    // Ð“Ð¾Ð²Ð¾Ñ€Ñ�Ñ‰Ð¸Ð¹
    // ÐžÑ�Ñ‚Ñ€Ð¾Ð²
    // Ñ‡ÐµÑ€ÐµÐ·
    // 1
    // Ð¼Ð¸Ð½.
    public static final int THE_FERRY_FROM_TALKING_ISLAND_WILL_BE_ARRIVING_AT_GIRAN_HARBOR_IN_APPROXIMATELY_20_MINUTES = 1166; // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ
    // Ñ�
    // Ð“Ð¾Ð²Ð¾Ñ€Ñ�Ñ‰ÐµÐ³Ð¾
    // ÐžÑ�Ñ‚Ñ€Ð¾Ð²Ð°
    // Ð¿Ñ€Ð¸Ð±ÑƒÐ´ÐµÑ‚
    // Ð²
    // Ð³Ð°Ð²Ð°Ð½ÑŒ
    // Ð“Ð¸Ñ€Ð°Ð½Ð°
    // Ñ‡ÐµÑ€ÐµÐ·
    // 20
    // Ð¼Ð¸Ð½.
    public static final int THE_FERRY_FROM_TALKING_ISLAND_WILL_BE_ARRIVING_AT_GIRAN_HARBOR_IN_APPROXIMATELY_15_MINUTES = 1167; // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ
    // Ñ�
    // Ð“Ð¾Ð²Ð¾Ñ€Ñ�Ñ‰ÐµÐ³Ð¾
    // ÐžÑ�Ñ‚Ñ€Ð¾Ð²Ð°
    // Ð¿Ñ€Ð¸Ð±ÑƒÐ´ÐµÑ‚
    // Ð²
    // Ð³Ð°Ð²Ð°Ð½ÑŒ
    // Ð“Ð¸Ñ€Ð°Ð½Ð°
    // Ñ‡ÐµÑ€ÐµÐ·
    // 15
    // Ð¼Ð¸Ð½.
    public static final int THE_FERRY_FROM_TALKING_ISLAND_WILL_BE_ARRIVING_AT_GIRAN_HARBOR_IN_APPROXIMATELY_10_MINUTES = 1168; // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ
    // Ñ�
    // Ð“Ð¾Ð²Ð¾Ñ€Ñ�Ñ‰ÐµÐ³Ð¾
    // ÐžÑ�Ñ‚Ñ€Ð¾Ð²Ð°
    // Ð¿Ñ€Ð¸Ð±ÑƒÐ´ÐµÑ‚
    // Ð²
    // Ð³Ð°Ð²Ð°Ð½ÑŒ
    // Ð“Ð¸Ñ€Ð°Ð½Ð°
    // Ñ‡ÐµÑ€ÐµÐ·
    // 10
    // Ð¼Ð¸Ð½.
    public static final int THE_FERRY_FROM_TALKING_ISLAND_WILL_BE_ARRIVING_AT_GIRAN_HARBOR_IN_APPROXIMATELY_5_MINUTES = 1169; // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ
    // Ñ�
    // Ð“Ð¾Ð²Ð¾Ñ€Ñ�Ñ‰ÐµÐ³Ð¾
    // ÐžÑ�Ñ‚Ñ€Ð¾Ð²Ð°
    // Ð¿Ñ€Ð¸Ð±ÑƒÐ´ÐµÑ‚
    // Ð²
    // Ð³Ð°Ð²Ð°Ð½ÑŒ
    // Ð“Ð¸Ñ€Ð°Ð½Ð°
    // Ñ‡ÐµÑ€ÐµÐ·
    // 5
    // Ð¼Ð¸Ð½.
    public static final int THE_FERRY_FROM_TALKING_ISLAND_WILL_BE_ARRIVING_AT_GIRAN_HARBOR_IN_APPROXIMATELY_1_MINUTE = 1170; // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ
    // Ñ�
    // Ð“Ð¾Ð²Ð¾Ñ€Ñ�Ñ‰ÐµÐ³Ð¾
    // ÐžÑ�Ñ‚Ñ€Ð¾Ð²Ð°
    // Ð¿Ñ€Ð¸Ð±ÑƒÐ´ÐµÑ‚
    // Ð²
    // Ð³Ð°Ð²Ð°Ð½ÑŒ
    // Ð“Ð¸Ñ€Ð°Ð½Ð°
    // Ñ‡ÐµÑ€ÐµÐ·
    // 1
    // Ð¼Ð¸Ð½.
    public static final int THE_INNADRIL_PLEASURE_BOAT_WILL_ARRIVE_IN_APPROXIMATELY_20_MINUTES = 1171; // Ð�Ð°Ñˆ
    // ÐºÐ¾Ñ€Ð°Ð±Ð»ÑŒ
    // Ð¿Ñ€Ð¸Ð±ÑƒÐ´ÐµÑ‚
    // Ð²
    // Ð¿ÑƒÐ½ÐºÑ‚
    // Ð½Ð°Ð·Ð½Ð°Ñ‡ÐµÐ½Ð¸Ñ�
    // Ñ‡ÐµÑ€ÐµÐ·
    // 20
    // Ð¼Ð¸Ð½.
    public static final int THE_INNADRIL_PLEASURE_BOAT_WILL_ARRIVE_IN_APPROXIMATELY_15_MINUTES = 1172; // Ð�Ð°Ñˆ
    // ÐºÐ¾Ñ€Ð°Ð±Ð»ÑŒ
    // Ð¿Ñ€Ð¸Ð±ÑƒÐ´ÐµÑ‚
    // Ð²
    // Ð¿ÑƒÐ½ÐºÑ‚
    // Ð½Ð°Ð·Ð½Ð°Ñ‡ÐµÐ½Ð¸Ñ�
    // Ñ‡ÐµÑ€ÐµÐ·
    // 15
    // Ð¼Ð¸Ð½.
    public static final int THE_INNADRIL_PLEASURE_BOAT_WILL_ARRIVE_IN_APPROXIMATELY_10_MINUTES = 1173; // Ð�Ð°Ñˆ
    // ÐºÐ¾Ñ€Ð°Ð±Ð»ÑŒ
    // Ð¿Ñ€Ð¸Ð±ÑƒÐ´ÐµÑ‚
    // Ð²
    // Ð¿ÑƒÐ½ÐºÑ‚
    // Ð½Ð°Ð·Ð½Ð°Ñ‡ÐµÐ½Ð¸Ñ�
    // Ñ‡ÐµÑ€ÐµÐ·
    // 10
    // Ð¼Ð¸Ð½.
    public static final int THE_INNADRIL_PLEASURE_BOAT_WILL_ARRIVE_IN_APPROXIMATELY_5_MINUTES = 1174; // Ð�Ð°Ñˆ
    // ÐºÐ¾Ñ€Ð°Ð±Ð»ÑŒ
    // Ð¿Ñ€Ð¸Ð±ÑƒÐ´ÐµÑ‚
    // Ð²
    // Ð¿ÑƒÐ½ÐºÑ‚
    // Ð½Ð°Ð·Ð½Ð°Ñ‡ÐµÐ½Ð¸Ñ�
    // Ñ‡ÐµÑ€ÐµÐ·
    // 5
    // Ð¼Ð¸Ð½.
    public static final int THE_INNADRIL_PLEASURE_BOAT_WILL_ARRIVE_IN_APPROXIMATELY_1_MINUTE = 1175; // Ð�Ð°Ñˆ
    // ÐºÐ¾Ñ€Ð°Ð±Ð»ÑŒ
    // Ð¿Ñ€Ð¸Ð±ÑƒÐ´ÐµÑ‚
    // Ð²
    // Ð¿ÑƒÐ½ÐºÑ‚
    // Ð½Ð°Ð·Ð½Ð°Ñ‡ÐµÐ½Ð¸Ñ�
    // Ñ‡ÐµÑ€ÐµÐ·
    // 1
    // Ð¼Ð¸Ð½.
    public static final int THIS_IS_A_QUEST_EVENT_PERIOD = 1176; // ÐŸÑ€Ð¾Ð´Ð¾Ð»Ð¶Ð°ÐµÑ‚Ñ�Ñ�
    // Ð¿Ñ€Ð¾Ð²ÐµÐ´ÐµÐ½Ð¸Ðµ
    // Ð¸Ð²ÐµÐ½Ñ‚Ð°.
    public static final int THIS_IS_THE_SEAL_VALIDATION_PERIOD = 1177; // ÐŸÑ€Ð¾Ð´Ð¾Ð»Ð¶Ð°ÐµÑ‚Ñ�Ñ�
    // Ð¿ÐµÑ€Ð¸Ð¾Ð´
    // Ð¿Ñ€Ð¾Ð²ÐµÑ€ÐºÐ¸
    // ÐŸÐµÑ‡Ð°Ñ‚Ð¸.
    public static final int THIS_SEAL_PERMITS_THE_GROUP_THAT_HOLDS_IT_TO_EXCLUSIVELY_ENTER_THE_DUNGEON_OPENED_BY_THE_SEAL_OF_AVARICE_DURING_THE_SEAL_VALIDATION_PERIOD__IT_ALSO_PERMITS_TRADING_WITH_THE_MERCHANT_OF_MAMMON_WHO_APPEARS_IN_SPECIAL_DUNGEONS_AND_PERMITS_MEETINGS_WITH_ANAKIM_OR_LILITH_IN_THE_DISCIPLES_NECROPOLIS = 1178; // Ð­Ñ‚Ð°
    // ÐŸÐµÑ‡Ð°Ñ‚ÑŒ
    // Ð´Ð°ÐµÑ‚
    // Ð³Ñ€ÑƒÐ¿Ð¿Ðµ,
    // Ð²Ð»Ð°Ð´ÐµÑŽÑ‰ÐµÐ¹
    // ÐµÑŽ,
    // Ñ�ÐºÑ�ÐºÐ»ÑŽÐ·Ð¸Ð²Ð½Ñ‹Ð¹
    // Ð²Ñ…Ð¾Ð´
    // Ð²
    // Ð¿Ð¾Ð´Ð·ÐµÐ¼ÐµÐ»ÑŒÐµ,
    // Ð¾Ñ‚ÐºÑ€Ñ‹Ð²Ð°ÑŽÑ‰ÐµÐµÑ�Ñ�
    // ÐŸÐµÑ‡Ð°Ñ‚ÑŒÑŽ
    // Ð�Ð»Ñ‡Ð½Ð¾Ñ�Ñ‚Ð¸
    // Ð²
    // Ð¿ÐµÑ€Ð¸Ð¾Ð´
    // Ð´ÐµÐ¹Ñ�Ñ‚Ð²Ð¸Ñ�
    // ÐŸÐµÑ‡Ð°Ñ‚ÐµÐ¹.
    // Ð¢Ð°ÐºÐ¶Ðµ
    // Ð¾Ð½Ð°
    // Ð¿Ð¾Ð·Ð²Ð¾Ð»Ñ�ÐµÑ‚
    // Ð²ÐµÑ�Ñ‚Ð¸
    // Ð´ÐµÐ»Ð°
    // Ñ�
    // Ð¢Ð¾Ñ€Ð³Ð¾Ð²Ñ†Ð°Ð¼Ð¸
    // ÐœÐ°Ð¼Ð¼Ð¾Ð½Ð°,
    // ÐºÐ¾Ñ‚Ð¾Ñ€Ñ‹Ðµ
    // Ð½Ð°Ñ…Ð¾Ð´Ñ�Ñ‚Ñ�Ñ�
    // Ð²
    // Ð¾Ñ�Ð¾Ð±Ñ‹Ñ…
    // Ð¿Ð¾Ð´Ð·ÐµÐ¼ÐµÐ»ÑŒÑ�Ñ….
    // Ð¢Ð°ÐºÐ¶Ðµ
    // Ð¾Ð½Ð°
    // Ð¿Ð¾Ð·Ð²Ð¾Ð»Ñ�ÐµÑ‚
    // Ð²Ñ�Ñ‚Ñ€ÐµÑ‚Ð¸Ñ‚ÑŒÑ�Ñ�
    // Ñ�
    // Ð�Ð½Ð°ÐºÐ¸Ð¼Ð¾Ð¼
    // Ð¸Ð»Ð¸
    // Ð›Ð¸Ð»Ð¸Ñ‚
    // Ð²
    // Ð�ÐµÐºÑ€Ð¾Ð¿Ð¾Ð»Ðµ
    // Ð�Ð¿Ð¾Ñ�Ñ‚Ð¾Ð»Ð¾Ð².
    public static final int THIS_SEAL_PERMITS_THE_GROUP_THAT_HOLDS_IT_TO_ENTER_THE_DUNGEON_OPENED_BY_THE_SEAL_OF_GNOSIS_USE_THE_TELEPORTATION_SERVICE_OFFERED_BY_THE_PRIEST_IN_THE_VILLAGE_AND_DO_BUSINESS_WITH_THE_MERCHANT_OF_MAMMON_THE_ORATOR_OF_REVELATIONS_APPEARS_AND_CASTS_GOOD_MAGIC_ON_THE_WINNERS_AND_THE_PREACHER_OF_DOOM_APPEARS_AND_CASTS_BAD_MAGIC_ON_THE_LOSERS = 1179; // Ð­Ñ‚Ð°
    // ÐŸÐµÑ‡Ð°Ñ‚ÑŒ
    // Ð¿Ð¾Ð·Ð²Ð¾Ð»Ñ�ÐµÑ‚
    // Ð³Ñ€ÑƒÐ¿Ð¿Ðµ,
    // Ð²Ð»Ð°Ð´ÐµÑŽÑ‰ÐµÐ¹
    // ÐµÑŽ,
    // Ð¿Ñ€Ð¾Ð½Ð¸ÐºÐ½ÑƒÑ‚ÑŒ
    // Ð²
    // Ð¿Ð¾Ð´Ð·ÐµÐ¼ÐµÐ»ÑŒÐµ,
    // Ð¾Ñ‚ÐºÑ€Ñ‹Ð²Ð°ÑŽÑ‰ÐµÐµÑ�Ñ�
    // ÐŸÐµÑ‡Ð°Ñ‚ÑŒÑŽ
    // ÐŸÐ¾Ð·Ð½Ð°Ð½Ð¸Ñ�,
    // Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒÑ�Ñ�
    // ÑƒÑ�Ð»ÑƒÐ³Ð°Ð¼Ð¸
    // Ñ‚ÐµÐ»ÐµÐ¿Ð¾Ñ€Ñ‚Ð°Ñ†Ð¸Ð¸,
    // ÐºÐ¾Ñ‚Ð¾Ñ€Ñ‹Ðµ
    // Ð¿Ñ€ÐµÐ´Ð¾Ñ�Ñ‚Ð°Ð²Ð»Ñ�ÐµÑ‚
    // Ð¶Ñ€ÐµÑ†
    // Ð²
    // Ð´ÐµÑ€ÐµÐ²Ð½Ðµ,
    // Ð¸
    // Ð²ÐµÑ�Ñ‚Ð¸
    // Ð´ÐµÐ»Ð°
    // Ñ�
    // ÐšÑƒÐ·Ð½ÐµÑ†Ð°Ð¼Ð¸
    // ÐœÐ°Ð¼Ð¼Ð¾Ð½Ð°.
    // ÐŸÐ¾Ñ�Ð²Ð»Ñ�ÐµÑ‚Ñ�Ñ�
    // ÐžÑ€Ð°Ñ‚Ð¾Ñ€
    // ÐžÑ‚ÐºÑ€Ð¾Ð²ÐµÐ½Ð¸Ð¹
    // Ð¸
    // Ð·Ð°ÐºÐ»Ð¸Ð½Ð°ÐµÑ‚
    // Ð¿Ð¾Ð»Ð¾Ð¶Ð¸Ñ‚ÐµÐ»ÑŒÐ½Ñ‹Ð¼Ð¸
    // Ñ�Ñ„Ñ„ÐµÐºÑ‚Ð°Ð¼Ð¸
    // Ð¿Ð¾Ð±ÐµÐ´Ð¸Ñ‚ÐµÐ»ÐµÐ¹.
    // ÐŸÐ¾Ñ�Ð²Ð»Ñ�ÐµÑ‚Ñ�Ñ�
    // ÐŸÑ€Ð¾Ð¿Ð¾Ð²ÐµÐ´Ð½Ð¸Ðº
    // Ð¡ÑƒÐ´ÑŒÐ±Ñ‹
    // Ð¸
    // Ð·Ð°ÐºÐ»Ð¸Ð½Ð°ÐµÑ‚
    // Ð¾Ñ‚Ñ€Ð¸Ñ†Ð°Ñ‚ÐµÐ»ÑŒÐ½Ñ‹Ð¼Ð¸
    // Ñ�Ñ„Ñ„ÐµÐºÑ‚Ð°Ð¼Ð¸
    // Ð¿Ñ€Ð¾Ð¸Ð³Ñ€Ð°Ð²ÑˆÐ¸Ñ….
    public static final int DURING_THE_SEAL_VALIDATION_PERIOD_THE_COSTS_OF_CASTLE_DEFENSE_MERCENARIES_AND_RENOVATIONS_BASIC_P_DEF_OF_CASTLE_GATES_AND_CASTLE_WALLS_AND_MAXIMUM_TAX_RATES_WILL_ALL_CHANGE_TO_FAVOR_THE_GROUP_OF_FIGHTERS_THAT_POSSESSES_THIS_SEAL = 1180; // Ð’
    // Ð¿ÐµÑ€Ð¸Ð¾Ð´
    // Ð´ÐµÐ¹Ñ�Ñ‚Ð²Ð¸Ñ�
    // ÐŸÐµÑ‡Ð°Ñ‚ÐµÐ¹
    // Ð¼Ð°ÐºÑ�Ð¸Ð¼ÑƒÐ¼
    // CP
    // Ð³Ñ€ÑƒÐ¿Ð¿Ñ‹
    // ÑƒÐ²ÐµÐ»Ð¸Ñ‡Ð¸Ð²Ð°ÐµÑ‚Ñ�Ñ�.
    // ÐšÑ€Ð¾Ð¼Ðµ
    // Ñ‚Ð¾Ð³Ð¾,
    // Ð³Ñ€ÑƒÐ¿Ð¿Ð°,
    // Ð²Ð»Ð°Ð´ÐµÑŽÑ‰Ð°Ñ�
    // ÐŸÐµÑ‡Ð°Ñ‚ÑŒÑŽ,
    // Ð±ÑƒÐ´ÐµÑ‚
    // Ð¿Ð¾Ð»ÑƒÑ‡Ð°Ñ‚ÑŒ
    // Ñ�ÐºÐ¸Ð´ÐºÐ¸
    // Ð¿Ñ€Ð¸
    // ÑƒÐ»ÑƒÑ‡ÑˆÐµÐ½Ð¸Ð¸
    // Ð·Ð°Ñ‰Ð¸Ñ‚Ð½Ñ‹Ñ…
    // Ð½Ð°ÐµÐ¼Ð½Ð¸ÐºÐ¾Ð²
    // Ð·Ð°Ð¼ÐºÐ°,
    // Ð²Ñ€Ð°Ñ‚
    // Ð·Ð°Ð¼ÐºÐ°
    // Ð¸
    // Ñ�Ñ‚ÐµÐ½
    // Ð·Ð°Ð¼ÐºÐ°.
    // Ð£Ð»ÑƒÑ‡ÑˆÐ°ÐµÑ‚Ñ�Ñ�
    // Ð±Ð°Ð·Ð¾Ð²Ð°Ñ�
    // Ð¤.
    // Ð—Ð°Ñ‰.
    // Ñ�Ñ‚ÐµÐ½
    // Ð¸
    // Ð²Ð¾Ñ€Ð¾Ñ‚
    // Ð·Ð°Ð¼ÐºÐ°
    // Ð¸
    // ÑƒÑ�Ñ‚Ð°Ð½Ð¾Ð²Ð»ÐµÐ½Ð½Ñ‹Ð¹
    // Ð»Ð¸Ð¼Ð¸Ñ‚
    // Ð½Ð°Ð»Ð¾Ð³Ð¾Ð².
    // Ð˜Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ð½Ð¸Ðµ
    // Ð¾Ñ�Ð°Ð´Ð½Ñ‹Ñ…
    // Ð¾Ñ€ÑƒÐ´Ð¸Ð¹
    // Ñ‚Ð°ÐºÐ¶Ðµ
    // Ð±ÑƒÐ´ÐµÑ‚
    // Ð¾Ð³Ñ€Ð°Ð½Ð¸Ñ‡ÐµÐ½Ð¾.
    // Ð•Ñ�Ð»Ð¸
    // ÐœÑ�Ñ‚ÐµÐ¶Ð½Ð¸ÐºÐ¸
    // Ð—Ð°ÐºÐ°Ñ‚Ð°
    // Ð¿Ð¾Ð»ÑƒÑ‡Ð°Ñ‚
    // Ð¿Ñ€Ð°Ð²Ð¾
    // Ð½Ð°
    // Ð¾Ð±Ð»Ð°Ð´Ð°Ð½Ð¸Ðµ
    // Ñ�Ñ‚Ð¾Ð¹
    // ÐŸÐµÑ‡Ð°Ñ‚ÑŒÑŽ
    // Ð²
    // Ð¾Ñ�Ð°Ð´Ðµ
    // Ð·Ð°Ð¼ÐºÐ°,
    // Ñ‚Ð¾
    // Ñ‚Ð¾Ð»ÑŒÐºÐ¾
    // ÐºÐ»Ð°Ð½,
    // Ð²Ð»Ð°Ð´ÐµÑŽÑ‰Ð¸Ð¹
    // Ð·Ð°Ð¼ÐºÐ¾Ð¼,
    // Ñ�Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð²Ñ�Ñ‚Ð°Ñ‚ÑŒ
    // Ð½Ð°
    // ÐµÐ³Ð¾
    // Ð·Ð°Ñ‰Ð¸Ñ‚Ñƒ.
    public static final int DO_YOU_REALLY_WISH_TO_CHANGE_THE_TITLE = 1181; // Ð’Ñ‹
    // Ñ…Ð¾Ñ‚Ð¸Ñ‚Ðµ
    // Ð¸Ð·Ð¼ÐµÐ½Ð¸Ñ‚ÑŒ
    // Ð·Ð°Ð³Ð¾Ð»Ð¾Ð²Ð¾Ðº?
    public static final int DO_YOU_REALLY_WISH_TO_DELETE_THE_CLAN_CREST = 1182; // Ð’Ñ‹
    // Ñ…Ð¾Ñ‚Ð¸Ñ‚Ðµ
    // ÑƒÐ´Ð°Ð»Ð¸Ñ‚ÑŒ
    // Ñ�Ð¼Ð±Ð»ÐµÐ¼Ñƒ
    // ÐºÐ»Ð°Ð½Ð°?
    public static final int THIS_IS_THE_INITIAL_PERIOD = 1183; // Ð˜Ð´ÐµÑ‚
    // Ð¿Ð¾Ð´Ð³Ð¾Ñ‚Ð¾Ð²ÐºÐ°.
    public static final int THIS_IS_A_PERIOD_OF_CALCULATIING_STATISTICS_IN_THE_SERVER = 1184; // Ð˜Ð´ÐµÑ‚
    // Ñ�Ð±Ð¾Ñ€
    // Ð¸Ð½Ñ„Ð¾Ñ€Ð¼Ð°Ñ†Ð¸Ð¸
    // Ð¾
    // Ñ�ÐµÑ€Ð²ÐµÑ€Ðµ.
    public static final int DAYS_LEFT_UNTIL_DELETION = 1185; // Ð´Ð½ÐµÐ¹ Ð´Ð¾
    // ÑƒÐ´Ð°Ð»ÐµÐ½Ð¸Ñ�.
    public static final int TO_CREATE_A_NEW_ACCOUNT_PLEASE_VISIT_THE_PLAYNC_WEBSITE_HTTP___WWWPLAYNCCOM_US_SUPPORT = 1186; // Ð�Ð¾Ð²Ñ‹Ð¹
    // Ð°ÐºÐºÐ°ÑƒÐ½Ñ‚
    // Ð¼Ð¾Ð¶Ð½Ð¾
    // Ñ�Ð¾Ð·Ð´Ð°Ñ‚ÑŒ
    // Ð½Ð°
    // Ð½Ð°ÑˆÐµÐ¼
    // Ñ�Ð°Ð¹Ñ‚Ðµ.
    public static final int IF_YOU_HAVE_LOST_YOUR_ACCOUNT_INFORMATION_PLEASE_VISIT_THE_OFFICIAL_LINEAGE_II_SUPPORT_WEBSITE_AT_HTTP__SUPPORTPLAYNCCOM = 1187; // Ð•Ñ�Ð»Ð¸
    // Ð’Ñ‹
    // Ð·Ð°Ð±Ñ‹Ð»Ð¸
    // Ñ�Ð²Ð¾Ð¹
    // Ð»Ð¾Ð³Ð¸Ð½
    // Ð¸Ð»Ð¸
    // Ð¿Ð°Ñ€Ð¾Ð»ÑŒ,
    // Ð¾Ð±Ñ€Ð°Ñ‚Ð¸Ñ‚ÐµÑ�ÑŒ
    // Ð²
    // Ñ�Ð»ÑƒÐ¶Ð±Ñƒ
    // Ð¿Ð¾Ð´Ð´ÐµÑ€Ð¶ÐºÐ¸
    // Ð½Ð°
    // Ñ�Ð°Ð¹Ñ‚Ðµ.
    public static final int THE_TEMPORARY_ALLIANCE_OF_THE_CASTLE_ATTACKER_TEAM_IS_IN_EFFECT_IT_WILL_BE_DISSOLVED_WHEN_THE_CASTLE_LORD_IS_REPLACED = 1189; // Ð¡Ð¾Ð·Ð´Ð°Ð½
    // Ð²Ñ€ÐµÐ¼ÐµÐ½Ð½Ñ‹Ð¹
    // Ð°Ð»ÑŒÑ�Ð½Ñ�
    // Ð½Ð°
    // Ð¿ÐµÑ€Ð¸Ð¾Ð´
    // Ð¾Ñ�Ð°Ð´Ñ‹
    // Ð·Ð°Ð¼ÐºÐ°.
    // ÐŸÑ€Ð¸
    // Ñ�Ð¼ÐµÐ½Ðµ
    // Ñ…Ð¾Ð·Ñ�Ð¸Ð½Ð°
    // Ð·Ð°Ð¼ÐºÐ°
    // Ð°Ð»ÑŒÑ�Ð½Ñ�
    // Ð±ÑƒÐ´ÐµÑ‚
    // Ñ€Ð°Ñ�Ð¿ÑƒÑ‰ÐµÐ½.
    public static final int THE_TEMPORARY_ALLIANCE_OF_THE_CASTLE_ATTACKER_TEAM_HAS_BEEN_DISSOLVED = 1190; // Ð’Ñ€ÐµÐ¼ÐµÐ½Ð½Ñ‹Ð¹
    // Ð°Ð»ÑŒÑ�Ð½Ñ�,
    // Ñ�Ð¾Ð·Ð´Ð°Ð½Ð½Ñ‹Ð¹
    // Ð½Ð°
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ð¾Ñ�Ð°Ð´Ñ‹
    // Ð·Ð°Ð¼ÐºÐ°,
    // Ñ€Ð°Ñ�Ð¿ÑƒÑ‰ÐµÐ½.
    public static final int THE_FERRY_FROM_GLUDIN_HARBOR_WILL_BE_ARRIVING_AT_TALKING_ISLAND_IN_APPROXIMATELY_10_MINUTES = 1191; // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ
    // Ð¸Ð·
    // Ð³Ð°Ð²Ð°Ð½Ð¸
    // Ð“Ð»ÑƒÐ´Ð¸Ð½Ð°
    // Ð´Ð¾
    // Ð“Ð¾Ð²Ð¾Ñ€Ñ�Ñ‰ÐµÐ³Ð¾
    // ÐžÑ�Ñ‚Ñ€Ð¾Ð²Ð°
    // Ð¾Ñ‚Ð¿Ñ€Ð°Ð²Ð»Ñ�ÐµÑ‚Ñ�Ñ�
    // Ñ‡ÐµÑ€ÐµÐ·
    // 10
    // Ð¼Ð¸Ð½.
    public static final int THE_FERRY_FROM_GLUDIN_HARBOR_WILL_BE_ARRIVING_AT_TALKING_ISLAND_IN_APPROXIMATELY_5_MINUTES = 1192; // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ
    // Ð¸Ð·
    // Ð³Ð°Ð²Ð°Ð½Ð¸
    // Ð“Ð»ÑƒÐ´Ð¸Ð½Ð°
    // Ð´Ð¾
    // Ð“Ð¾Ð²Ð¾Ñ€Ñ�Ñ‰ÐµÐ³Ð¾
    // ÐžÑ�Ñ‚Ñ€Ð¾Ð²Ð°
    // Ð¾Ñ‚Ð¿Ñ€Ð°Ð²Ð»Ñ�ÐµÑ‚Ñ�Ñ�
    // Ñ‡ÐµÑ€ÐµÐ·
    // 5
    // Ð¼Ð¸Ð½.
    public static final int THE_FERRY_FROM_GLUDIN_HARBOR_WILL_BE_ARRIVING_AT_TALKING_ISLAND_IN_APPROXIMATELY_1_MINUTE = 1193; // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ
    // Ð¸Ð·
    // Ð³Ð°Ð²Ð°Ð½Ð¸
    // Ð“Ð»ÑƒÐ´Ð¸Ð½Ð°
    // Ð´Ð¾
    // Ð“Ð¾Ð²Ð¾Ñ€Ñ�Ñ‰ÐµÐ³Ð¾
    // ÐžÑ�Ñ‚Ñ€Ð¾Ð²Ð°
    // Ð¾Ñ‚Ð¿Ñ€Ð°Ð²Ð»Ñ�ÐµÑ‚Ñ�Ñ�
    // Ñ‡ÐµÑ€ÐµÐ·
    // 1
    // Ð¼Ð¸Ð½.
    public static final int A_MERCENARY_CAN_BE_ASSIGNED_TO_A_POSITION_FROM_THE_BEGINNING_OF_THE_SEAL_VALIDATION_PERIOD_UNTIL_THE_TIME_WHEN_A_SIEGE_STARTS = 1194; // Ð�Ð°ÐµÐ¼Ð½Ð¸ÐºÐ¾Ð²
    // Ð¼Ð¾Ð¶Ð½Ð¾
    // Ñ€Ð°Ñ�Ñ�Ñ‚Ð°Ð²Ð»Ñ�Ñ‚ÑŒ
    // Ñ�
    // Ð½Ð°Ñ‡Ð°Ð»Ð°
    // Ð¿ÐµÑ€Ð¸Ð¾Ð´Ð°
    // Ð´ÐµÐ¹Ñ�Ñ‚Ð²Ð¸Ñ�
    // ÐŸÐµÑ‡Ð°Ñ‚Ð¸
    // Ð´Ð¾
    // Ð½Ð°Ñ‡Ð°Ð»Ð°
    // Ð¾Ñ�Ð°Ð´Ñ‹
    // Ð·Ð°Ð¼ÐºÐ°.
    public static final int THIS_MERCENARY_CANNOT_BE_ASSIGNED_TO_A_POSITION_BY_USING_THE_SEAL_OF_STRIFE = 1195; // Ð’Ð¾
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ð´ÐµÐ¹Ñ�Ñ‚Ð²Ð¸Ñ�
    // ÐŸÐµÑ‡Ð°Ñ‚Ð¸
    // Ð Ð°Ð·Ð´Ð¾Ñ€Ð°
    // Ð´Ð°Ð½Ð½Ð¾Ð³Ð¾
    // Ð½Ð°ÐµÐ¼Ð½Ð¸ÐºÐ°
    // Ñ€Ð°Ð·Ð¼ÐµÑ�Ñ‚Ð¸Ñ‚ÑŒ
    // Ð½ÐµÐ»ÑŒÐ·Ñ�.
    public static final int YOUR_FORCE_HAS_REACHED_MAXIMUM_CAPACITY = 1196; // Ð’Ñ‹
    // ÑƒÐ²ÐµÐ»Ð¸Ñ‡Ð¸Ð»Ð¸
    // Ñ�Ð²Ð¾ÑŽ
    // Ñ�Ð¸Ð»Ñƒ
    // Ð´Ð¾
    // Ð¼Ð°ÐºÑ�Ð¸Ð¼Ð°Ð»ÑŒÐ½Ð¾Ð³Ð¾
    // Ð¿Ñ€ÐµÐ´ÐµÐ»Ð°.
    public static final int SUMMONING_A_SERVITOR_COSTS_S2_S1 = 1197; // Ð’
    // ÐºÐ°Ñ‡ÐµÑ�Ñ‚Ð²Ðµ
    // Ð¿Ð»Ð°Ñ‚Ñ‹ Ð·Ð°
    // Ð²Ñ‹Ð·Ð¾Ð²
    // Ð²Ð·Ð¸Ð¼Ð°ÐµÑ‚Ñ�Ñ�
    // $s1 Ð²
    // ÐºÐ¾Ð»Ð¸Ñ‡ÐµÑ�Ñ‚Ð²Ðµ
    // $s2 ÑˆÑ‚.
    public static final int THE_ITEM_HAS_BEEN_SUCCESSFULLY_CRYSTALLIZED = 1198; // Ð’Ð°Ñˆ
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚
    // ÑƒÑ�Ð¿ÐµÑˆÐ½Ð¾
    // ÐºÑ€Ð¸Ñ�Ñ‚Ð°Ð»Ð»Ð¸Ð·Ð¾Ð²Ð°Ð½.
    public static final int _CLAN_WAR_TARGET_ = 1199; // =======<CLAN_WAR_TARGET>=======
    public static final int S1_S2_ALLIANCE = 1200; // = $s1 (Ð�Ð»ÑŒÑ�Ð½Ñ� $s2)
    public static final int PLEASE_SELECT_THE_QUEST_YOU_WISH_TO_QUIT = 1201; // Ð’Ñ‹Ð±ÐµÑ€Ð¸Ñ‚Ðµ
    // ÐºÐ²ÐµÑ�Ñ‚,
    // Ð¾Ñ‚
    // ÐºÐ¾Ñ‚Ð¾Ñ€Ð¾Ð³Ð¾
    // Ñ…Ð¾Ñ‚Ð¸Ñ‚Ðµ
    // Ð¾Ñ‚ÐºÐ°Ð·Ð°Ñ‚ÑŒÑ�Ñ�.
    public static final int S1_NO_ALLIANCE_EXISTS = 1202; // = $s1 (Ð�Ð»ÑŒÑ�Ð½Ñ� $s2)
    public static final int THERE_IS_NO_CLAN_WAR_IN_PROGRESS = 1203; // Ð’ Ð´Ð°Ð½Ð½Ñ‹Ð¹
    // Ð¼Ð¾Ð¼ÐµÐ½Ñ‚
    // Ð²Ñ‹ Ð½Ðµ
    // Ð²Ð¾ÑŽÐµÑ‚Ðµ Ñ�
    // Ð´Ñ€ÑƒÐ³Ð¸Ð¼Ð¸
    // ÐºÐ»Ð°Ð½Ð°Ð¼Ð¸.
    public static final int THE_SCREENSHOT_HAS_BEEN_SAVED_S1_S2XS3 = 1204; // Ð¡ÐºÑ€Ð¸Ð½ÑˆÐ¾Ñ‚
    // Ñ�Ð¾Ñ…Ñ€Ð°Ð½ÐµÐ½.
    // ($s1
    // $s2x$s3)
    public static final int MAILBOX_IS_FULL100_MESSAGE_MAXIMUM = 1205; // Ð’Ð°Ñˆ
    // Ð¿Ð¾Ñ‡Ñ‚Ð¾Ð²Ñ‹Ð¹
    // Ñ�Ñ‰Ð¸Ðº
    // Ð¿ÐµÑ€ÐµÐ¿Ð¾Ð»Ð½ÐµÐ½.
    // Ð’Ñ‹
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ñ…Ñ€Ð°Ð½Ð¸Ñ‚ÑŒ
    // Ð² Ð½ÐµÐ¼
    // Ð½Ðµ
    // Ð±Ð¾Ð»ÐµÐµ
    // 100
    // Ñ�Ð¾Ð¾Ð±Ñ‰ÐµÐ½Ð¸Ð¹.
    public static final int MEMO_BOX_IS_FULL_100_MEMO_MAXIMUM = 1206; // Ð’Ð°Ñˆ
    // Ð´Ð½ÐµÐ²Ð½Ð¸Ðº
    // Ð¿Ð¾Ð»Ð¾Ð½.
    // Ð’Ñ‹
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ñ…Ñ€Ð°Ð½Ð¸Ñ‚ÑŒ
    // Ð² Ð½ÐµÐ¼
    // Ð½Ðµ
    // Ð±Ð¾Ð»ÐµÐµ
    // 100
    // Ð·Ð°Ð¿Ð¸Ñ�ÐµÐ¹.
    public static final int PLEASE_MAKE_AN_ENTRY_IN_THE_FIELD = 1207; // Ð’Ð²ÐµÐ´Ð¸Ñ‚Ðµ
    // Ñ�Ð¾Ð´ÐµÑ€Ð¶Ð°Ð½Ð¸Ðµ.
    public static final int S1_DIED_AND_DROPPED_S3_S2 = 1208; // ÐŸÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶ $c1
    // Ð¿Ð¾Ð³Ð¸Ð± Ð¸ Ð¿Ð¾Ñ‚ÐµÑ€Ñ�Ð»
    // $s2 Ð²
    // ÐºÐ¾Ð»Ð¸Ñ‡ÐµÑ�Ñ‚Ð²Ðµ $s3
    // ÑˆÑ‚.
    public static final int CONGRATULATIONS_YOUR_RAID_WAS_SUCCESSFUL = 1209; // ÐŸÐ¾Ð·Ð´Ñ€Ð°Ð²Ð»Ñ�ÐµÐ¼
    // Ñ�
    // ÑƒÑ�Ð¿ÐµÑˆÐ½Ñ‹Ð¼
    // Ñ€ÐµÐ¹Ð´Ð¾Ð¼!
    public static final int SEVEN_SIGNS_THE_QUEST_EVENT_PERIOD_HAS_BEGUN_VISIT_A_PRIEST_OF_DAWN_OR_DUSK_TO_PARTICIPATE_IN_THE_EVENT = 1210; // Ð¡ÐµÐ¼ÑŒ
    // ÐŸÐµÑ‡Ð°Ñ‚ÐµÐ¹:
    // ÐšÐ²ÐµÑ�Ñ‚Ð¾Ð²Ñ‹Ð¹
    // Ð¸Ð²ÐµÐ½Ñ‚
    // Ð½Ð°Ñ‡Ð°Ð»Ñ�Ñ�.
    // Ð’Ñ‹
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð²Ð¾Ð²Ð°Ñ‚ÑŒ
    // Ð²
    // Ð½ÐµÐ¼,
    // Ð¿Ð¾Ð¾Ð±Ñ‰Ð°Ð²ÑˆÐ¸Ñ�ÑŒ
    // Ñ�Ð¾
    // Ð–Ñ€ÐµÑ†Ð¾Ð¼
    // Ð Ð°Ñ�Ñ�Ð²ÐµÑ‚Ð°
    // Ð¸Ð»Ð¸
    // Ð–Ñ€Ð¸Ñ†ÐµÐ¹
    // Ð—Ð°ÐºÐ°Ñ‚Ð°.
    public static final int SEVEN_SIGNS_THE_QUEST_EVENT_PERIOD_HAS_ENDED_THE_NEXT_QUEST_EVENT_WILL_START_IN_ONE_WEEK = 1211; // Ð¡ÐµÐ¼ÑŒ
    // ÐŸÐµÑ‡Ð°Ñ‚ÐµÐ¹:
    // ÐšÐ²ÐµÑ�Ñ‚Ð¾Ð²Ñ‹Ð¹
    // Ð¸Ð²ÐµÐ½Ñ‚
    // Ð·Ð°Ð²ÐµÑ€ÑˆÐµÐ½.
    // Ð¡Ð»ÐµÐ´ÑƒÑŽÑ‰Ð¸Ð¹
    // Ð¿Ñ€Ð¾Ð¹Ð´ÐµÑ‚
    // Ñ‡ÐµÑ€ÐµÐ·
    // Ð½ÐµÐ´ÐµÐ»ÑŽ.
    public static final int SEVEN_SIGNS_THE_LORDS_OF_DAWN_HAVE_OBTAINED_THE_SEAL_OF_AVARICE = 1212; // Ð¡ÐµÐ¼ÑŒ
    // ÐŸÐµÑ‡Ð°Ñ‚ÐµÐ¹:
    // Ð›Ð¾Ñ€Ð´Ñ‹
    // Ð Ð°Ñ�Ñ�Ð²ÐµÑ‚Ð°
    // Ð·Ð°Ð²Ð¾ÐµÐ²Ð°Ð»Ð¸
    // ÐŸÐµÑ‡Ð°Ñ‚ÑŒ
    // Ð�Ð»Ñ‡Ð½Ð¾Ñ�Ñ‚Ð¸.
    public static final int SEVEN_SIGNS_THE_LORDS_OF_DAWN_HAVE_OBTAINED_THE_SEAL_OF_GNOSIS = 1213; // Ð¡ÐµÐ¼ÑŒ
    // ÐŸÐµÑ‡Ð°Ñ‚ÐµÐ¹:
    // Ð›Ð¾Ñ€Ð´Ñ‹
    // Ð Ð°Ñ�Ñ�Ð²ÐµÑ‚Ð°
    // Ð·Ð°Ð²Ð¾ÐµÐ²Ð°Ð»Ð¸
    // ÐŸÐµÑ‡Ð°Ñ‚ÑŒ
    // ÐŸÐ¾Ð·Ð½Ð°Ð½Ð¸Ñ�.
    public static final int SEVEN_SIGNS_THE_LORDS_OF_DAWN_HAVE_OBTAINED_THE_SEAL_OF_STRIFE = 1214; // Ð¡ÐµÐ¼ÑŒ
    // ÐŸÐµÑ‡Ð°Ñ‚ÐµÐ¹:
    // Ð›Ð¾Ñ€Ð´Ñ‹
    // Ð Ð°Ñ�Ñ�Ð²ÐµÑ‚Ð°
    // Ð·Ð°Ð²Ð¾ÐµÐ²Ð°Ð»Ð¸
    // ÐŸÐµÑ‡Ð°Ñ‚ÑŒ
    // Ð Ð°Ð·Ð´Ð¾Ñ€Ð°.
    public static final int SEVEN_SIGNS_THE_REVOLUTIONARIES_OF_DUSK_HAVE_OBTAINED_THE_SEAL_OF_AVARICE = 1215; // Ð¡ÐµÐ¼ÑŒ
    // ÐŸÐµÑ‡Ð°Ñ‚ÐµÐ¹:
    // ÐœÑ�Ñ‚ÐµÐ¶Ð½Ð¸ÐºÐ¸
    // Ð—Ð°ÐºÐ°Ñ‚Ð°
    // Ð·Ð°Ð²Ð¾ÐµÐ²Ð°Ð»Ð¸
    // ÐŸÐµÑ‡Ð°Ñ‚ÑŒ
    // Ð�Ð»Ñ‡Ð½Ð¾Ñ�Ñ‚Ð¸.
    public static final int SEVEN_SIGNS_THE_REVOLUTIONARIES_OF_DUSK_HAVE_OBTAINED_THE_SEAL_OF_GNOSIS = 1216; // Ð¡ÐµÐ¼ÑŒ
    // ÐŸÐµÑ‡Ð°Ñ‚ÐµÐ¹:
    // ÐœÑ�Ñ‚ÐµÐ¶Ð½Ð¸ÐºÐ¸
    // Ð—Ð°ÐºÐ°Ñ‚Ð°
    // Ð·Ð°Ð²Ð¾ÐµÐ²Ð°Ð»Ð¸
    // ÐŸÐµÑ‡Ð°Ñ‚ÑŒ
    // ÐŸÐ¾Ð·Ð½Ð°Ð½Ð¸Ñ�.
    public static final int SEVEN_SIGNS_THE_REVOLUTIONARIES_OF_DUSK_HAVE_OBTAINED_THE_SEAL_OF_STRIFE = 1217; // Ð¡ÐµÐ¼ÑŒ
    // ÐŸÐµÑ‡Ð°Ñ‚ÐµÐ¹:
    // ÐœÑ�Ñ‚ÐµÐ¶Ð½Ð¸ÐºÐ¸
    // Ð—Ð°ÐºÐ°Ñ‚Ð°
    // Ð·Ð°Ð²Ð¾ÐµÐ²Ð°Ð»Ð¸
    // ÐŸÐµÑ‡Ð°Ñ‚ÑŒ
    // Ð Ð°Ð·Ð´Ð¾Ñ€Ð°.
    public static final int SEVEN_SIGNS_THE_SEAL_VALIDATION_PERIOD_HAS_BEGUN = 1218; // Ð¡ÐµÐ¼ÑŒ
    // ÐŸÐµÑ‡Ð°Ñ‚ÐµÐ¹:
    // ÐŸÐµÑ‡Ð°Ñ‚ÑŒ
    // Ð°ÐºÑ‚Ð¸Ð²Ð¸Ñ€Ð¾Ð²Ð°Ð½Ð°.
    public static final int SEVEN_SIGNS_THE_SEAL_VALIDATION_PERIOD_HAS_ENDED = 1219; // Ð¡ÐµÐ¼ÑŒ
    // ÐŸÐµÑ‡Ð°Ñ‚ÐµÐ¹:
    // Ð’Ñ€ÐµÐ¼Ñ�
    // Ð´ÐµÐ¹Ñ�Ñ‚Ð²Ð¸Ñ�
    // ÐŸÐµÑ‡Ð°Ñ‚Ð¸
    // Ð·Ð°ÐºÐ¾Ð½Ñ‡Ð¸Ð»Ð¾Ñ�ÑŒ.
    public static final int ARE_YOU_SURE_YOU_WISH_TO_SUMMON_IT = 1220; // Ð’Ñ‹
    // Ñ…Ð¾Ñ‚Ð¸Ñ‚Ðµ
    // Ð²Ñ‹Ð·Ð²Ð°Ñ‚ÑŒ
    // Ñ�Ñ‚Ð¾
    // Ñ�ÑƒÑ‰ÐµÑ�Ñ‚Ð²Ð¾?
    public static final int DO_YOU_REALLY_WISH_TO_RETURN_IT = 1221; // Ð’Ñ‹ Ñ…Ð¾Ñ‚Ð¸Ñ‚Ðµ
    // Ð²ÐµÑ€Ð½ÑƒÑ‚ÑŒ
    // Ñ�Ñ‚Ð¾?
    public static final int CURRENT_LOCATION_S1_S2_S3_GM_CONSULTATION_SERVICE = 1222; // Ð¢ÐµÐºÑƒÑ‰ÐµÐµ
    // Ð¼ÐµÑ�Ñ‚Ð¾Ð¿Ð¾Ð»Ð¾Ð¶ÐµÐ½Ð¸Ðµ:
    // $s1,
    // $s2,
    // $s3
    // (Ñ�Ð»ÑƒÐ¶Ð±Ð°
    // Ð¿Ð¾Ð´Ð´ÐµÑ€Ð¶ÐºÐ¸)
    public static final int WE_DEPART_FOR_TALKING_ISLAND_IN_FIVE_MINUTES = 1223; // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ
    // Ð´Ð¾
    // Ð“Ð¾Ð²Ð¾Ñ€Ñ�Ñ‰ÐµÐ³Ð¾
    // ÐžÑ�Ñ‚Ñ€Ð¾Ð²Ð°
    // Ð¾Ñ‚Ð¿Ñ€Ð°Ð²Ð»Ñ�ÐµÑ‚Ñ�Ñ�
    // Ñ‡ÐµÑ€ÐµÐ·
    // 5
    // Ð¼Ð¸Ð½.
    public static final int WE_DEPART_FOR_TALKING_ISLAND_IN_ONE_MINUTE = 1224; // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ
    // Ð´Ð¾
    // Ð“Ð¾Ð²Ð¾Ñ€Ñ�Ñ‰ÐµÐ³Ð¾
    // ÐžÑ�Ñ‚Ñ€Ð¾Ð²Ð°
    // Ð¾Ñ‚Ð¿Ñ€Ð°Ð²Ð»Ñ�ÐµÑ‚Ñ�Ñ�
    // Ñ‡ÐµÑ€ÐµÐ·
    // 1
    // Ð¼Ð¸Ð½.
    public static final int ALL_ABOARD_FOR_TALKING_ISLAND = 1225; // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ Ð´Ð¾
    // Ð“Ð¾Ð²Ð¾Ñ€Ñ�Ñ‰ÐµÐ³Ð¾
    // ÐžÑ�Ñ‚Ñ€Ð¾Ð²Ð°
    // Ð¾Ñ‚Ð¿Ñ€Ð°Ð²Ð»Ñ�ÐµÑ‚Ñ�Ñ�!
    // Ð’Ñ�Ðµ Ð½Ð°
    // Ð±Ð¾Ñ€Ñ‚!
    public static final int WE_ARE_NOW_LEAVING_FOR_TALKING_ISLAND = 1226; // ÐœÑ‹
    // Ð¾Ñ‚Ð¿Ñ€Ð°Ð²Ð»Ñ�ÐµÐ¼Ñ�Ñ�
    // Ðº
    // Ð“Ð¾Ð²Ð¾Ñ€Ñ�Ñ‰ÐµÐ¼Ñƒ
    // ÐžÑ�Ñ‚Ñ€Ð¾Ð²Ñƒ.
    public static final int YOU_HAVE_S1_UNREAD_MESSAGES = 1227; // Ð�Ð¾Ð²Ñ‹Ðµ
    // Ñ�Ð¾Ð¾Ð±Ñ‰ÐµÐ½Ð¸Ñ�:
    // $c1.
    public static final int S1_HAS_BLOCKED_YOU_YOU_CANNOT_SEND_MAIL_TO_S1_ = 1228; // $c1
    // Ð±Ð»Ð¾ÐºÐ¸Ñ€ÑƒÐµÑ‚
    // Ð’Ð°Ñ�.
    // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¿Ð¾Ñ�Ð»Ð°Ñ‚ÑŒ
    // Ñ�Ð¾Ð¾Ð±Ñ‰ÐµÐ½Ð¸Ðµ
    // Ñ�Ñ‚Ð¾Ð¼Ñƒ
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ñƒ.
    public static final int NO_MORE_MESSAGES_MAY_BE_SENT_AT_THIS_TIME_EACH_ACCOUNT_IS_ALLOWED_10_MESSAGES_PER_DAY = 1229; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¿Ð¾Ñ�Ð»Ð°Ñ‚ÑŒ
    // Ð¿Ð¸Ñ�ÑŒÐ¼Ð¾.
    // Ð’
    // Ð´ÐµÐ½ÑŒ
    // Ð¼Ð¾Ð¶Ð½Ð¾
    // Ð¿Ð¾Ñ�Ð»Ð°Ñ‚ÑŒ
    // Ð½Ðµ
    // Ð±Ð¾Ð»ÐµÐµ
    // 10
    // Ð¿Ð¸Ñ�ÐµÐ¼.
    public static final int YOU_ARE_LIMITED_TO_FIVE_RECIPIENTS_AT_A_TIME = 1230; // ÐŸÐ¾Ñ�Ð»Ð°Ñ‚ÑŒ
    // Ð¿Ð¸Ñ�ÑŒÐ¼Ð¾
    // Ð¼Ð¾Ð¶Ð½Ð¾
    // Ð¼Ð°ÐºÑ�Ð¸Ð¼ÑƒÐ¼
    // 5
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°Ð¼.
    public static final int YOUVE_SENT_MAIL = 1231; // ÐŸÐ¸Ñ�ÑŒÐ¼Ð¾ Ð¾Ñ‚Ð¿Ñ€Ð°Ð²Ð»ÐµÐ½Ð¾.
    public static final int THE_MESSAGE_WAS_NOT_SENT = 1232; // ÐžÑˆÐ¸Ð±ÐºÐ° Ð¿Ñ€Ð¸
    // Ð¾Ñ‚Ð¿Ñ€Ð°Ð²ÐºÐµ Ð¿Ð¸Ñ�ÑŒÐ¼Ð°.
    public static final int YOUVE_GOT_MAIL = 1233; // ÐŸÐ¸Ñ�ÑŒÐ¼Ð¾ Ð´Ð¾Ñ�Ñ‚Ð°Ð²Ð»ÐµÐ½Ð¾.
    public static final int THE_MAIL_HAS_BEEN_STORED_IN_YOUR_TEMPORARY_MAILBOX = 1234; // ÐŸÐ¸Ñ�ÑŒÐ¼Ð¾
    // Ñ�Ð¾Ñ…Ñ€Ð°Ð½ÐµÐ½Ð¾
    // Ð²
    // Ñ‡ÐµÑ€Ð½Ð¾Ð²Ð¸ÐºÐ°Ñ….
    public static final int DO_YOU_WISH_TO_DELETE_ALL_YOUR_FRIENDS = 1235; // Ð’Ñ‹
    // Ñ…Ð¾Ñ‚Ð¸Ñ‚Ðµ
    // ÑƒÐ´Ð°Ð»Ð¸Ñ‚ÑŒ
    // Ð²Ñ�ÐµÑ…
    // Ð´Ñ€ÑƒÐ·ÐµÐ¹?
    public static final int PLEASE_ENTER_SECURITY_CARD_NUMBER = 1236; // Ð’Ð²ÐµÐ´Ð¸Ñ‚Ðµ
    // Ð½Ð¾Ð¼ÐµÑ€
    // ÐºÐ°Ñ€Ñ‚Ñ‹
    // Ð±ÐµÐ·Ð¾Ð¿Ð°Ñ�Ð½Ð¾Ñ�Ñ‚Ð¸.
    public static final int PLEASE_ENTER_THE_CARD_NUMBER_FOR_NUMBER_S1 = 1237; // Ð’Ð²ÐµÐ´Ð¸Ñ‚Ðµ
    // Ð½Ð¾Ð¼ÐµÑ€
    // ÐºÐ°Ñ€Ñ‚Ñ‹
    // Ð´Ð»Ñ�
    // $s1.
    public static final int YOUR_TEMPORARY_MAILBOX_IS_FULL_NO_MORE_MAIL_CAN_BE_STORED_10_MESSAGE_LIMIT = 1238; // Ð’Ð°Ñˆ
    // Ñ‡ÐµÑ€Ð½Ð¾Ð²Ð¸Ðº
    // Ð¿Ð¾Ð»Ð¾Ð½.
    // Ð’Ñ‹
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ñ…Ñ€Ð°Ð½Ð¸Ñ‚ÑŒ
    // Ð²
    // Ñ‡ÐµÑ€Ð½Ð¾Ð²Ð¸ÐºÐ°Ñ…
    // Ð½Ðµ
    // Ð±Ð¾Ð»ÐµÐµ
    // 10
    // Ñ�Ð¾Ð¾Ð±Ñ‰ÐµÐ½Ð¸Ð¹.
    public static final int LOADING_OF_THE_KEYBOARD_SECURITY_MODULE_HAS_FAILED_PLEASE_EXIT_THE_GAME_AND_RELOAD = 1239; // ÐžÑˆÐ¸Ð±ÐºÐ°
    // Ð¿Ñ€Ð¸
    // Ð·Ð°Ð³Ñ€ÑƒÐ·ÐºÐµ
    // Ð¼Ð¾Ð´ÑƒÐ»Ñ�
    // Ð±ÐµÐ·Ð¾Ð¿Ð°Ñ�Ð½Ð¾Ñ�Ñ‚Ð¸
    // Ð²Ð²Ð¾Ð´Ð°
    // Ñ‡ÐµÑ€ÐµÐ·
    // ÐºÐ»Ð°Ð²Ð¸Ð°Ñ‚ÑƒÑ€Ñƒ.
    // Ð”Ð»Ñ�
    // Ð¿Ð¾Ð²Ñ‚Ð¾Ñ€Ð½Ð¾Ð¹
    // Ð¿Ð¾Ð¿Ñ‹Ñ‚ÐºÐ¸
    // Ð¿ÐµÑ€ÐµÐ·Ð°Ð¹Ð´Ð¸Ñ‚Ðµ
    // Ð²
    // Ð¸Ð³Ñ€Ñƒ.
    public static final int SEVEN_SIGNS_THE_REVOLUTIONARIES_OF_DUSK_HAVE_WON = 1240; // Ð¡ÐµÐ¼ÑŒ
    // ÐŸÐµÑ‡Ð°Ñ‚ÐµÐ¹:
    // ÐŸÐ¾Ð±ÐµÐ´Ð¸Ð»Ð¸
    // ÐœÑ�Ñ‚ÐµÐ¶Ð½Ð¸ÐºÐ¸
    // Ð—Ð°ÐºÐ°Ñ‚Ð°.
    public static final int SEVEN_SIGNS_THE_LORDS_OF_DAWN_HAVE_WON = 1241; // Ð¡ÐµÐ¼ÑŒ
    // ÐŸÐµÑ‡Ð°Ñ‚ÐµÐ¹:
    // ÐŸÐ¾Ð±ÐµÐ´Ð¸Ð»Ð¸
    // Ð›Ð¾Ñ€Ð´Ñ‹
    // Ð Ð°Ñ�Ñ�Ð²ÐµÑ‚Ð°.
    public static final int USERS_WHO_HAVE_NOT_VERIFIED_THEIR_AGE_CANNOT_LOG_IN_BETWEEN_1000_PM_AND_600_AM = 1242; // ÐŸÐ¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÐµÐ»Ð¸,
    // Ð½Ðµ
    // Ð¿Ð¾Ð´Ñ‚Ð²ÐµÑ€Ð´Ð¸Ð²ÑˆÐ¸Ðµ
    // Ð´Ð¾Ñ�Ñ‚Ð¸Ð¶ÐµÐ½Ð¸Ðµ
    // 18
    // Ð»ÐµÑ‚,
    // Ð½Ðµ
    // Ð¼Ð¾Ð³ÑƒÑ‚
    // Ð²Ð¾Ð¹Ñ‚Ð¸
    // Ð²
    // Ð¸Ð³Ñ€Ñƒ
    // Ñ�
    // 22:00
    // Ð´Ð¾
    // 6:00.
    public static final int THE_SECURITY_CARD_NUMBER_IS_INVALID = 1243; // Ð�Ð¾Ð¼ÐµÑ€
    // ÐºÐ°Ñ€Ñ‚Ñ‹
    // Ð½ÐµÐ´ÐµÐ¹Ñ�Ñ‚Ð²Ð¸Ñ‚ÐµÐ»ÐµÐ½.
    public static final int USERS_WHO_HAVE_NOT_VERIFIED_THEIR_AGE_CANNOT_LOG_IN_BETWEEN_1000_PM_AND_600_AM_LOGGING_OFF = 1244; // ÐŸÐ¾Ð´ÐºÐ»ÑŽÑ‡ÐµÐ½Ð¸Ðµ
    // Ð´Ð»Ñ�
    // Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÐµÐ»ÐµÐ¹,
    // Ð½Ðµ
    // Ð¿Ð¾Ð´Ñ‚Ð²ÐµÑ€Ð´Ð¸Ð²ÑˆÐ¸Ñ…
    // Ð´Ð¾Ñ�Ñ‚Ð¸Ð¶ÐµÐ½Ð¸Ðµ
    // 18
    // Ð»ÐµÑ‚,
    // Ð½ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾
    // Ñ�
    // 22:00
    // Ð´Ð¾
    // 6:00.
    // Ð˜Ð³Ñ€Ð°
    // Ð±ÑƒÐ´ÐµÑ‚
    // Ð¾Ñ‚ÐºÐ»ÑŽÑ‡ÐµÐ½Ð°.
    public static final int YOU_WILL_BE_LOGGED_OUT_IN_S1_MINUTES = 1245; // Ð”Ð¾
    // Ð²Ñ‹Ñ…Ð¾Ð´Ð°
    // Ð¸Ð·
    // Ð¸Ð³Ñ€Ñ‹:
    // $s1
    // Ð¼Ð¸Ð½.
    public static final int S1_DIED_AND_HAS_DROPPED_S2_ADENA = 1246; // $c1
    // Ð¿Ð¾Ð³Ð¸Ð±Ð°ÐµÑ‚
    // Ð¸ Ñ‚ÐµÑ€Ñ�ÐµÑ‚
    // $s2
    // Ð°Ð´ÐµÐ½.
    public static final int THE_CORPSE_IS_TOO_OLD_THE_SKILL_CANNOT_BE_USED = 1247; // ÐœÐ¾Ð½Ñ�Ñ‚Ñ€
    // Ð¿Ð¾Ð³Ð¸Ð±
    // Ñ�Ð»Ð¸ÑˆÐºÐ¾Ð¼
    // Ð´Ð°Ð²Ð½Ð¾,
    // Ð¿Ð¾Ñ�Ñ‚Ð¾Ð¼Ñƒ
    // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð²Ð¾Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒÑ�Ñ�
    // Ð´Ð°Ð½Ð½Ñ‹Ð¼
    // ÑƒÐ¼ÐµÐ½Ð¸ÐµÐ¼.
    public static final int YOU_ARE_OUT_OF_FEED_MOUNT_STATUS_CANCELED = 1248; // Ð£
    // Ð’Ð°Ñ�
    // Ð·Ð°ÐºÐ¾Ð½Ñ‡Ð¸Ð»Ñ�Ñ�
    // ÐºÐ¾Ñ€Ð¼,
    // Ð¿Ð¾Ñ�Ñ‚Ð¾Ð¼Ñƒ
    // ÐµÐ·Ð´Ð°
    // Ð²ÐµÑ€Ñ…Ð¾Ð¼
    // Ð±ÑƒÐ´ÐµÑ‚
    // Ð¿Ñ€ÐµÐºÑ€Ð°Ñ‰ÐµÐ½Ð°.
    public static final int YOU_MAY_ONLY_RIDE_A_WYVERN_WHILE_YOURE_RIDING_A_STRIDER = 1249; // Ð’Ñ‹
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¾Ñ�ÐµÐ´Ð»Ð°Ñ‚ÑŒ
    // Ð²Ð¸Ð²ÐµÑ€Ð½Ñƒ
    // Ñ‚Ð¾Ð»ÑŒÐºÐ¾
    // Ð²Ð¾
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ð²ÐµÑ€Ñ…Ð¾Ð²Ð¾Ð¹
    // ÐµÐ·Ð´Ñ‹
    // Ð½Ð°
    // Ð´Ñ€Ð°ÐºÐ¾Ð½Ðµ.
    public static final int DO_YOU_REALLY_WANT_TO_SURRENDER_IF_YOU_SURRENDER_DURING_AN_ALLIANCE_WAR_YOUR_EXP_WILL_DROP_AS_MUCH_AS_WHEN_YOUR_CHARACTER_DIES_ONCE = 1250; // Ð’Ñ‹
    // Ñ…Ð¾Ñ‚Ð¸Ñ‚Ðµ
    // Ñ�Ð´Ð°Ñ‚ÑŒÑ�Ñ�?
    // ÐŸÑ€Ð¸
    // ÐºÐ°Ð¿Ð¸Ñ‚ÑƒÐ»Ñ�Ñ†Ð¸Ð¸
    // Ð°Ð»ÑŒÑ�Ð½Ñ�Ð°
    // Ð±ÑƒÐ´ÐµÑ‚
    // Ñ�Ð½Ñ�Ñ‚Ð¾
    // ÐºÐ¾Ð»Ð¸Ñ‡ÐµÑ�Ñ‚Ð²Ð¾
    // Ð¾Ð¿Ñ‹Ñ‚Ð°,
    // Ñ€Ð°Ð²Ð½Ð¾Ðµ
    // Ð¾Ð´Ð½Ð¾Ð¹
    // Ñ�Ð¼ÐµÑ€Ñ‚Ð¸.
    public static final int ARE_YOU_SURE_YOU_WANT_TO_DISMISS_THE_ALLIANCE_IF_YOU_USE_THE__ALLYDISMISS_COMMAND_YOU_WILL_NOT_BE_ABLE_TO_ACCEPT_ANOTHER_CLAN_TO_YOUR_ALLIANCE_FOR_ONE_DAY = 1251; // Ð’Ñ‹
    // Ñ…Ð¾Ñ‚Ð¸Ñ‚Ðµ
    // Ñ€Ð°Ñ�Ð¿ÑƒÑ�Ñ‚Ð¸Ñ‚ÑŒ
    // Ð°Ð»ÑŒÑ�Ð½Ñ�?
    // ÐŸÐ¾Ñ�Ð»Ðµ
    // Ñ€Ð¾Ñ�Ð¿ÑƒÑ�ÐºÐ°
    // Ð°Ð»ÑŒÑ�Ð½Ñ�Ð°
    // Ð²
    // Ñ‚ÐµÑ‡ÐµÐ½Ð¸Ðµ
    // 1
    // Ð´Ð½Ñ�
    // Ð’Ñ‹
    // Ð½Ðµ
    // Ñ�Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ñ�Ð¾Ð·Ð´Ð°Ñ‚ÑŒ
    // Ð°Ð»ÑŒÑ�Ð½Ñ�
    // Ñ�
    // Ð´Ñ€ÑƒÐ³Ð¸Ð¼
    // ÐºÐ»Ð°Ð½Ð¾Ð¼.
    public static final int ARE_YOU_SURE_YOU_WANT_TO_SURRENDER_EXP_PENALTY_WILL_BE_THE_SAME_AS_DEATH = 1252; // Ð’Ñ‹
    // Ñ…Ð¾Ñ‚Ð¸Ñ‚Ðµ
    // Ñ�Ð´Ð°Ñ‚ÑŒÑ�Ñ�?
    // ÐŸÑ€Ð¸
    // ÐºÐ°Ð¿Ð¸Ñ‚ÑƒÐ»Ñ�Ñ†Ð¸Ð¸
    // Ð±ÑƒÐ´ÐµÑ‚
    // Ñ�Ð½Ñ�Ñ‚Ð¾
    // ÐºÐ¾Ð»Ð¸Ñ‡ÐµÑ�Ñ‚Ð²Ð¾
    // Ð¾Ð¿Ñ‹Ñ‚Ð°,
    // Ñ€Ð°Ð²Ð½Ð¾Ðµ
    // Ð¾Ð´Ð½Ð¾Ð¹
    // Ñ�Ð¼ÐµÑ€Ñ‚Ð¸.
    public static final int ARE_YOU_SURE_YOU_WANT_TO_SURRENDER_EXP_PENALTY_WILL_BE_THE_SAME_AS_DEATH_AND_YOU_WILL_NOT_BE_ALLOWED_TO_PARTICIPATE_IN_CLAN_WAR = 1253; // Ð’Ñ‹
    // Ñ…Ð¾Ñ‚Ð¸Ñ‚Ðµ
    // Ñ�Ð´Ð°Ñ‚ÑŒÑ�Ñ�?
    // ÐŸÑ€Ð¸
    // ÐºÐ°Ð¿Ð¸Ñ‚ÑƒÐ»Ñ�Ñ†Ð¸Ð¸
    // Ð±ÑƒÐ´ÐµÑ‚
    // Ñ�Ð½Ñ�Ñ‚Ð¾
    // ÐºÐ¾Ð»Ð¸Ñ‡ÐµÑ�Ñ‚Ð²Ð¾
    // Ð¾Ð¿Ñ‹Ñ‚Ð°,
    // Ñ€Ð°Ð²Ð½Ð¾Ðµ
    // Ð¾Ð´Ð½Ð¾Ð¹
    // Ñ�Ð¼ÐµÑ€Ñ‚Ð¸,
    // Ð¸
    // Ð’Ñ‹
    // Ð½Ðµ
    // Ñ�Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð²Ð¾Ð²Ð°Ñ‚ÑŒ
    // Ð²
    // Ð²Ð¾Ð¹Ð½Ðµ
    // ÐºÐ»Ð°Ð½Ð¾Ð².
    public static final int THANK_YOU_FOR_SUBMITTING_FEEDBACK = 1254; // Ð¡Ð¿Ð°Ñ�Ð¸Ð±Ð¾
    // Ð·Ð° Ð’Ð°Ñˆ
    // Ð¾Ñ‚Ð²ÐµÑ‚.
    public static final int GM_CONSULTATION_HAS_BEGUN = 1255; // Ð�Ð°Ñ‡Ð°Ð»Ð¾Ñ�ÑŒ
    // ÐºÐ¾Ð½Ñ�ÑƒÐ»ÑŒÑ‚Ð°Ñ†Ð¸Ñ� Ñ�Ð¾
    // Ñ�Ð»ÑƒÐ¶Ð±Ð¾Ð¹
    // Ð¿Ð¾Ð´Ð´ÐµÑ€Ð¶ÐºÐ¸.
    public static final int PLEASE_WRITE_THE_NAME_AFTER_THE_COMMAND = 1256; // Ð�Ð°Ð¿Ð¸ÑˆÐ¸Ñ‚Ðµ
    // Ð¸Ð¼Ñ�
    // Ð¿Ð¾Ñ�Ð»Ðµ
    // ÐºÐ¾Ð¼Ð°Ð½Ð´Ñ‹.
    public static final int THE_SPECIAL_SKILL_OF_A_SERVITOR_OR_PET_CANNOT_BE_REGISTERED_AS_A_MACRO = 1257; // ÐžÑ�Ð¾Ð±Ñ‹Ðµ
    // ÑƒÐ¼ÐµÐ½Ð¸Ñ�
    // Ð¿Ð¸Ñ‚Ð¾Ð¼Ñ†ÐµÐ²
    // Ð¸
    // Ð²Ñ‹Ð·Ð²Ð°Ð½Ð½Ñ‹Ñ…
    // Ñ�ÑƒÑ‰ÐµÑ�Ñ‚Ð²
    // Ð½Ðµ
    // Ð¼Ð¾Ð³ÑƒÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ñ�Ð¾Ñ…Ñ€Ð°Ð½ÐµÐ½Ñ‹
    // ÐºÐ°Ðº
    // Ð¼Ð°ÐºÑ€Ð¾Ñ�Ñ‹.
    public static final int S1_HAS_BEEN_CRYSTALLIZED = 1258; // $s1:
    // ÐºÑ€Ð¸Ñ�Ñ‚Ð°Ð»Ð»Ð¸Ð·Ð°Ñ†Ð¸Ñ�
    // Ð¿Ñ€Ð¾ÑˆÐ»Ð° ÑƒÐ´Ð°Ñ‡Ð½Ð¾.
    public static final int _ALLIANCE_TARGET_ = 1259; // =======<ALLIANCE_TARGET>=======
    public static final int SEVEN_SIGNS_PREPARATIONS_HAVE_BEGUN_FOR_THE_NEXT_QUEST_EVENT = 1260; // Ð¡ÐµÐ¼ÑŒ
    // ÐŸÐµÑ‡Ð°Ñ‚ÐµÐ¹:
    // Ð˜Ð´ÐµÑ‚
    // Ð¿Ð¾Ð´Ð³Ð¾Ñ‚Ð¾Ð²ÐºÐ°
    // Ðº
    // Ð½Ð¾Ð²Ð¾Ð¼Ñƒ
    // ÐºÑ€ÑƒÐ³Ñƒ.
    public static final int SEVEN_SIGNS_THE_QUEST_EVENT_PERIOD_HAS_BEGUN_SPEAK_WITH_A_PRIEST_OF_DAWN_OR_DUSK_PRIESTESS_IF_YOU_WISH_TO_PARTICIPATE_IN_THE_EVENT = 1261; // Ð¡ÐµÐ¼ÑŒ
    // ÐŸÐµÑ‡Ð°Ñ‚ÐµÐ¹:
    // Ð˜Ð²ÐµÐ½Ñ‚
    // Ð½Ð°Ñ‡Ð°Ð»Ñ�Ñ�.
    // ÐŸÐ¾Ð³Ð¾Ð²Ð¾Ñ€Ð¸Ñ‚Ðµ
    // Ñ�Ð¾
    // Ð–Ñ€ÐµÑ†Ð¾Ð¼
    // Ð Ð°Ñ�Ñ�Ð²ÐµÑ‚Ð°
    // Ð¸Ð»Ð¸
    // Ð–Ñ€Ð¸Ñ†ÐµÐ¹
    // Ð—Ð°ÐºÐ°Ñ‚Ð°.
    public static final int SEVEN_SIGNS_QUEST_EVENT_HAS_ENDED_RESULTS_ARE_BEING_TALLIED = 1262; // Ð¡ÐµÐ¼ÑŒ
    // ÐŸÐµÑ‡Ð°Ñ‚ÐµÐ¹:
    // Ð˜Ð²ÐµÐ½Ñ‚
    // Ð·Ð°Ð²ÐµÑ€ÑˆÐµÐ½.
    // Ð¡Ð¾Ð±Ð¸Ñ€Ð°ÑŽÑ‚Ñ�Ñ�
    // Ð´Ð°Ð½Ð½Ñ‹Ðµ
    // Ñ�Ñ‚Ð°Ñ‚Ð¸Ñ�Ñ‚Ð¸ÐºÐ¸.
    public static final int SEVEN_SIGNS_THIS_IS_THE_SEAL_VALIDATION_PERIOD_A_NEW_QUEST_EVENT_PERIOD_BEGINS_NEXT_MONDAY = 1263; // Ð¡ÐµÐ¼ÑŒ
    // ÐŸÐµÑ‡Ð°Ñ‚ÐµÐ¹:
    // ÐŸÐµÑ€Ð¸Ð¾Ð´
    // Ð´ÐµÐ¹Ñ�Ñ‚Ð²Ð¸Ñ�
    // ÐŸÐµÑ‡Ð°Ñ‚Ð¸.
    // Ð¡Ð»ÐµÐ´ÑƒÑŽÑ‰Ð¸Ð¹
    // Ð¸Ð²ÐµÐ½Ñ‚
    // Ð±ÑƒÐ´ÐµÑ‚
    // Ð¿Ñ€Ð¾Ð²ÐµÐ´ÐµÐ½
    // Ð²
    // Ñ�Ð»ÐµÐ´ÑƒÑŽÑ‰Ð¸Ð¹
    // Ð¿Ð¾Ð½ÐµÐ´ÐµÐ»ÑŒÐ½Ð¸Ðº.
    public static final int THIS_SOUL_STONE_CANNOT_CURRENTLY_ABSORB_SOULS_ABSORPTION_HAS_FAILED = 1264; // Ð­Ñ‚Ð¾Ñ‚
    // ÐºÑ€Ð¸Ñ�Ñ‚Ð°Ð»Ð»
    // Ð´ÑƒÑˆÐ¸
    // Ð½Ðµ
    // Ñ�Ð¼Ð¾Ð³
    // Ð¿Ð¾Ð³Ð»Ð¾Ñ‚Ð¸Ñ‚ÑŒ
    // Ð´ÑƒÑˆÑƒ.
    public static final int YOU_CANT_ABSORB_SOULS_WITHOUT_A_SOUL_STONE = 1265; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¿Ð¾Ð³Ð»Ð¾Ñ‚Ð¸Ñ‚ÑŒ
    // Ð´ÑƒÑˆÑƒ,
    // ÐµÑ�Ð»Ð¸
    // Ð½ÐµÑ‚
    // ÐºÑ€Ð¸Ñ�Ñ‚Ð°Ð»Ð»Ð°
    // Ð´ÑƒÑˆÐ¸.
    public static final int THE_EXCHANGE_HAS_ENDED = 1266; // ÐžÐ±Ð¼ÐµÐ½ Ð·Ð°Ð²ÐµÑ€ÑˆÐµÐ½.
    public static final int YOUR_CONTRIBUTION_SCORE_IS_INCREASED_BY_S1 = 1267; // Ð Ð°Ð·Ð¼ÐµÑ€
    // Ð²ÐºÐ»Ð°Ð´Ð°
    // ÑƒÐ²ÐµÐ»Ð¸Ñ‡ÐµÐ½
    // Ð½Ð°
    // $s1.
    public static final int DO_YOU_WISH_TO_ADD_S1_CLASS_AS_YOUR_SUB_CLASS = 1268; // $s1:
    // Ð¿Ñ€Ð¸Ð½Ñ�Ñ‚ÑŒ
    // ÐºÐ°Ðº
    // Ð¿Ð¾Ð´ÐºÐ»Ð°Ñ�Ñ�?
    public static final int THE_NEW_SUB_CLASS_HAS_BEEN_ADDED = 1269; // Ð’Ñ‹
    // Ð´Ð¾Ð±Ð°Ð²Ð¸Ð»Ð¸
    // Ð½Ð¾Ð²Ñ‹Ð¹
    // Ð¿Ð¾Ð´ÐºÐ»Ð°Ñ�Ñ�.
    public static final int THE_TRANSFER_OF_SUB_CLASS_HAS_BEEN_COMPLETED = 1270; // Ð’Ñ‹
    // Ñ�Ð¼ÐµÐ½Ð¸Ð»Ð¸
    // Ð¿Ð¾Ð´ÐºÐ»Ð°Ñ�Ñ�.
    public static final int DO_YOU_WISH_TO_PARTICIPATE_UNTIL_THE_NEXT_SEAL_VALIDATION_PERIOD_YOU_ARE_A_MEMBER_OF_THE_LORDS_OF_DAWN = 1271; // Ð’Ñ‹
    // Ñ…Ð¾Ñ‚Ð¸Ñ‚Ðµ
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð²Ð¾Ð²Ð°Ñ‚ÑŒ?
    // Ð”Ð¾
    // Ñ�Ð»ÐµÐ´ÑƒÑŽÑ‰ÐµÐ³Ð¾
    // Ð¿ÐµÑ€Ð¸Ð¾Ð´Ð°
    // Ð´ÐµÐ¹Ñ�Ñ‚Ð²Ð¸Ñ�
    // ÐŸÐµÑ‡Ð°Ñ‚Ð¸
    // Ð’Ñ‹
    // Ð±ÑƒÐ´ÐµÑ‚Ðµ
    // Ñ�Ð¾Ñ�Ñ‚Ð¾Ñ�Ñ‚ÑŒ
    // Ð²
    // Ñ€Ñ�Ð´Ð°Ñ…
    // Ð›Ð¾Ñ€Ð´Ð¾Ð²
    // Ð Ð°Ñ�Ñ�Ð²ÐµÑ‚Ð°.
    public static final int DO_YOU_WISH_TO_PARTICIPATE_UNTIL_THE_NEXT_SEAL_VALIDATION_PERIOD_YOU_ARE_A_MEMBER_OF_THE_REVOLUTIONARIES_OF_DUSK = 1272; // Ð’Ñ‹
    // Ñ…Ð¾Ñ‚Ð¸Ñ‚Ðµ
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð²Ð¾Ð²Ð°Ñ‚ÑŒ?
    // Ð”Ð¾
    // Ñ�Ð»ÐµÐ´ÑƒÑŽÑ‰ÐµÐ³Ð¾
    // Ð´ÐµÐ¹Ñ�Ñ‚Ð²Ð¸Ñ�
    // ÐŸÐµÑ‡Ð°Ñ‚Ð¸
    // Ð’Ñ‹
    // Ð±ÑƒÐ´ÐµÑ‚Ðµ
    // Ñ�Ð¾Ñ�Ñ‚Ð¾Ñ�Ñ‚ÑŒ
    // Ð²
    // Ñ€Ñ�Ð´Ð°Ñ…
    // ÐœÑ�Ñ‚ÐµÐ¶Ð½Ð¸ÐºÐ¾Ð²
    // Ð—Ð°ÐºÐ°Ñ‚Ð°.
    public static final int YOU_WILL_PARTICIPATE_IN_THE_SEVEN_SIGNS_AS_A_MEMBER_OF_THE_LORDS_OF_DAWN = 1273; // Ð’Ñ‹
    // Ð±ÑƒÐ´ÐµÑ‚Ðµ
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð²Ð¾Ð²Ð°Ñ‚ÑŒ
    // Ð²
    // Ð¡ÐµÐ¼Ð¸
    // ÐŸÐµÑ‡Ð°Ñ‚Ñ�Ñ…
    // Ð½Ð°
    // Ñ�Ñ‚Ð¾Ñ€Ð¾Ð½Ðµ
    // Ð›Ð¾Ñ€Ð´Ð¾Ð²
    // Ð Ð°Ñ�Ñ�Ð²ÐµÑ‚Ð°.
    public static final int YOU_WILL_PARTICIPATE_IN_THE_SEVEN_SIGNS_AS_A_MEMBER_OF_THE_REVOLUTIONARIES_OF_DUSK = 1274; // Ð’Ñ‹
    // Ð±ÑƒÐ´ÐµÑ‚Ðµ
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð²Ð¾Ð²Ð°Ñ‚ÑŒ
    // Ð²
    // Ð¡ÐµÐ¼Ð¸
    // ÐŸÐµÑ‡Ð°Ñ‚Ñ�Ñ…
    // Ð½Ð°
    // Ñ�Ñ‚Ð¾Ñ€Ð¾Ð½Ðµ
    // ÐœÑ�Ñ‚ÐµÐ¶Ð½Ð¸ÐºÐ¾Ð²
    // Ð—Ð°ÐºÐ°Ñ‚Ð°.
    public static final int YOUVE_CHOSEN_TO_FIGHT_FOR_THE_SEAL_OF_AVARICE_DURING_THIS_QUEST_EVENT_PERIOD = 1275; // Ð’Ñ‹
    // Ð²Ñ‹Ð±Ñ€Ð°Ð»Ð¸
    // ÐŸÐµÑ‡Ð°Ñ‚ÑŒ
    // Ð�Ð»Ñ‡Ð½Ð¾Ñ�Ñ‚Ð¸.
    public static final int YOUVE_CHOSEN_TO_FIGHT_FOR_THE_SEAL_OF_GNOSIS_DURING_THIS_QUEST_EVENT_PERIOD = 1276; // Ð’Ñ‹
    // Ð²Ñ‹Ð±Ñ€Ð°Ð»Ð¸
    // ÐŸÐµÑ‡Ð°Ñ‚ÑŒ
    // ÐŸÐ¾Ð·Ð½Ð°Ð½Ð¸Ñ�.
    public static final int YOUVE_CHOSEN_TO_FIGHT_FOR_THE_SEAL_OF_STRIFE_DURING_THIS_QUEST_EVENT_PERIOD = 1277; // Ð’Ñ‹
    // Ð²Ñ‹Ð±Ñ€Ð°Ð»Ð¸
    // ÐŸÐµÑ‡Ð°Ñ‚ÑŒ
    // Ð Ð°Ð·Ð´Ð¾Ñ€Ð°.
    public static final int THE_NPC_SERVER_IS_NOT_OPERATING = 1278; // Ð¡ÐµÑ€Ð²ÐµÑ€
    // NPC
    // Ð¾Ñ�Ñ‚Ð°Ð½Ð¾Ð²Ð»ÐµÐ½.
    public static final int CONTRIBUTION_LEVEL_HAS_EXCEEDED_THE_LIMIT_YOU_MAY_NOT_CONTINUE = 1279; // Ð‘Ñ‹Ð»
    // Ð¿Ñ€ÐµÐ²Ñ‹ÑˆÐµÐ½
    // Ð»Ð¸Ð¼Ð¸Ñ‚
    // Ð²ÐºÐ»Ð°Ð´Ð°.
    public static final int MAGIC_CRITICAL_HIT = 1280; // ÐšÑ€Ð¸Ñ‚Ð¸Ñ‡ÐµÑ�ÐºÐ¸Ð¹ ÑƒÐ´Ð°Ñ€
    // Ð¼Ð°Ð³Ð¸ÐµÐ¹!
    public static final int YOUR_EXCELLENT_SHIELD_DEFENSE_WAS_A_SUCCESS = 1281; // Ð’Ñ‹
    // ÑƒÑ�Ð¿ÐµÑˆÐ½Ð¾
    // Ð·Ð°Ñ‰Ð¸Ñ‚Ð¸Ð»Ð¸Ñ�ÑŒ
    // Ñ‰Ð¸Ñ‚Ð¾Ð¼.
    public static final int YOUR_KARMA_HAS_BEEN_CHANGED_TO_S1 = 1282; // Ð’Ð°ÑˆÐ°
    // ÐºÐ°Ñ€Ð¼Ð°
    // Ð¸Ð·Ð¼ÐµÐ½ÐµÐ½Ð°
    // Ð½Ð° $s1.
    public static final int THE_MINIMUM_FRAME_OPTION_HAS_BEEN_ACTIVATED = 1283; // Ð‘Ñ‹Ð»Ð°
    // Ð°ÐºÑ‚Ð¸Ð²Ð¸Ñ€Ð¾Ð²Ð°Ð½Ð°
    // Ð½Ð°Ñ�Ñ‚Ñ€Ð¾Ð¹ÐºÐ°
    // Ð¼Ð¸Ð½Ð¸Ð¼Ð°Ð»ÑŒÐ½Ð¾Ð¹
    // Ð´ÐµÑ‚Ð°Ð»Ð¸Ð·Ð°Ñ†Ð¸Ð¸.
    public static final int THE_MINIMUM_FRAME_OPTION_HAS_BEEN_DEACTIVATED = 1284; // Ð�Ð°Ñ�Ñ‚Ñ€Ð¾Ð¹ÐºÐ°
    // Ð¼Ð¸Ð½Ð¸Ð¼Ð°Ð»ÑŒÐ½Ð¾Ð¹
    // Ð´ÐµÑ‚Ð°Ð»Ð¸Ð·Ð°Ñ†Ð¸Ð¸
    // Ð¾Ñ‚ÐºÐ»ÑŽÑ‡ÐµÐ½Ð°.
    public static final int NO_INVENTORY_EXISTS_YOU_CANNOT_PURCHASE_AN_ITEM = 1285; // Ð�ÐµÑ‚
    // Ð¸Ð½Ð²ÐµÐ½Ñ‚Ð°Ñ€Ñ�,
    // Ð¿Ð¾Ñ�Ñ‚Ð¾Ð¼Ñƒ
    // Ð½ÐµÐ»ÑŒÐ·Ñ�
    // Ð¿Ñ€Ð¸Ð¾Ð±Ñ€ÐµÑ�Ñ‚Ð¸
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚.
    public static final int UNTIL_NEXT_MONDAY_AT_120_AM = 1286; // (Ð”Ð¾ 18:00
    // Ñ�Ð»ÐµÐ´ÑƒÑŽÑ‰ÐµÐ³Ð¾
    // Ð¿Ð¾Ð½ÐµÐ´ÐµÐ»ÑŒÐ½Ð¸ÐºÐ°)
    public static final int UNTIL_TODAY_AT_120_AM = 1287; // (Ð”Ð¾ 18:00)
    public static final int IF_TRENDS_CONTINUE_S1_WILL_WIN_AND_THE_SEAL_WILL_BELONG_TO = 1288; // Ð•Ñ�Ð»Ð¸
    // Ð±Ð¸Ñ‚Ð²Ð°
    // Ð·Ð°
    // ÐŸÐµÑ‡Ð°Ñ‚Ð¸
    // Ð·Ð°Ð²ÐµÑ€ÑˆÐ¸Ñ‚Ñ�Ñ�
    // Ñ�ÐµÐ¹Ñ‡Ð°Ñ�,
    // Ð¿Ð¾Ð±ÐµÐ´Ð¸Ñ‚
    // $s1,
    // Ð¸
    // ÐŸÐµÑ‡Ð°Ñ‚ÑŒÑŽ
    // Ð·Ð°Ð²Ð»Ð°Ð´ÐµÑŽÑ‚:
    public static final int SINCE_THE_SEAL_WAS_OWNED_DURING_THE_PREVIOUS_PERIOD_AND_10_PERCENT_OR_MORE_PEOPLE_HAVE_VOTED = 1289; // Ð¢Ð°Ðº
    // ÐºÐ°Ðº
    // Ð’Ñ‹
    // Ð¾Ð±Ð»Ð°Ð´Ð°Ð»Ð¸
    // ÐŸÐµÑ‡Ð°Ñ‚ÑŒÑŽ
    // Ð¸
    // Ð¿Ñ€Ð¾Ð³Ð¾Ð»Ð¾Ñ�Ð¾Ð²Ð°Ð»Ð¾
    // Ð±Ð¾Ð»ÐµÐµ
    // 10%
    // Ð»ÑŽÐ´ÐµÐ¹
    public static final int ALTHOUGH_THE_SEAL_WAS_NOT_OWNED_SINCE_35_PERCENT_OR_MORE_PEOPLE_HAVE_VOTED = 1290; // Ð¥Ð¾Ñ‚Ñ�
    // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¾Ð±Ð»Ð°Ð´Ð°Ð»Ð¸
    // ÐŸÐµÑ‡Ð°Ñ‚ÑŒÑŽ
    // Ð²
    // Ð¿Ñ€Ð¾ÑˆÐ»Ñ‹Ð¹
    // Ñ€Ð°Ð·,
    // Ð½Ð¾
    // Ð¿Ñ€Ð¾Ð³Ð¾Ð»Ð¾Ñ�Ð¾Ð²Ð°Ð»Ð¾
    // Ð±Ð¾Ð»ÐµÐµ
    // 35%
    // Ð»ÑŽÐ´ÐµÐ¹
    public static final int ALTHOUGH_THE_SEAL_WAS_OWNED_DURING_THE_PREVIOUS_PERIOD_BECAUSE_LESS_THAN_10_PERCENT_OF_PEOPLE_HAVE_VOTED = 1291; // Ð¢Ð°Ðº
    // ÐºÐ°Ðº
    // Ð’Ñ‹
    // Ð¾Ð±Ð»Ð°Ð´Ð°Ð»Ð¸
    // ÐŸÐµÑ‡Ð°Ñ‚ÑŒÑŽ
    // Ð¸
    // Ð¿Ñ€Ð¾Ð³Ð¾Ð»Ð¾Ñ�Ð¾Ð²Ð°Ð»Ð¾
    // Ð¼ÐµÐ½ÐµÐµ
    // 10%
    // Ð»ÑŽÐ´ÐµÐ¹
    public static final int SINCE_THE_SEAL_WAS_NOT_OWNED_DURING_THE_PREVIOUS_PERIOD_AND_SINCE_LESS_THAN_35_PERCENT_OF_PEOPLE_HAVE_VOTED = 1292; // Ð¥Ð¾Ñ‚Ñ�
    // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¾Ð±Ð»Ð°Ð´Ð°Ð»Ð¸
    // Ð¿ÐµÑ‡Ð°Ñ‚ÑŒÑŽ
    // Ð²
    // Ð¿Ñ€Ð¾ÑˆÐ»Ñ‹Ð¹
    // Ñ€Ð°Ð·,
    // Ð½Ð¾
    // Ð¿Ñ€Ð¾Ð³Ð¾Ð»Ð¾Ñ�Ð¾Ð²Ð°Ð»Ð¾
    // Ð¼ÐµÐ½ÐµÐµ
    // 35%
    // Ð»ÑŽÐ´ÐµÐ¹
    public static final int IF_CURRENT_TRENDS_CONTINUE_IT_WILL_END_IN_A_TIE = 1293; // Ð•Ñ�Ð»Ð¸
    // Ð±Ð¸Ñ‚Ð²Ð°
    // Ð·Ð°
    // ÐŸÐµÑ‡Ð°Ñ‚Ð¸
    // Ð·Ð°Ð²ÐµÑ€ÑˆÐ¸Ñ‚Ñ�Ñ�
    // Ñ�ÐµÐ¹Ñ‡Ð°Ñ�,
    // Ð±ÑƒÐ´ÐµÑ‚
    // Ð½Ð¸Ñ‡ÑŒÑ�.
    public static final int SINCE_THE_COMPETITION_HAS_ENDED_IN_A_TIE_THE_SEAL_WILL_NOT_BE_AWARDED = 1294; // Ð¡Ð¾Ñ€ÐµÐ²Ð½Ð¾Ð²Ð°Ð½Ð¸Ðµ
    // Ð·Ð°ÐºÐ¾Ð½Ñ‡Ð¸Ð»Ð¾Ñ�ÑŒ
    // Ð²
    // Ð½Ð¸Ñ‡ÑŒÑŽ,
    // Ð¿Ð¾Ñ�Ñ‚Ð¾Ð¼Ñƒ
    // ÐŸÐµÑ‡Ð°Ñ‚ÑŒ
    // Ð½Ðµ
    // Ð¿Ð¾Ð»ÑƒÑ‡Ð¸Ñ‚
    // Ð½Ð¸ÐºÑ‚Ð¾.
    public static final int SUB_CLASSES_MAY_NOT_BE_CREATED_OR_CHANGED_WHILE_A_SKILL_IS_IN_USE = 1295; // Ð’Ð¾
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ð½Ð¸Ñ�
    // ÑƒÐ¼ÐµÐ½Ð¸Ñ�
    // Ñ�Ð¾Ð·Ð´Ð°Ñ‚ÑŒ
    // Ð¸Ð»Ð¸
    // Ð¸Ð·Ð¼ÐµÐ½Ð¸Ñ‚ÑŒ
    // Ð¿Ð¾Ð´ÐºÐ»Ð°Ñ�Ñ�
    // Ð½ÐµÐ»ÑŒÐ·Ñ�.
    public static final int A_PRIVATE_STORE_MAY_NOT_BE_OPENED_IN_THIS_AREA = 1296; // Ð’
    // Ñ�Ñ‚Ð¾Ð¼
    // Ð¼ÐµÑ�Ñ‚Ðµ
    // Ð¾Ñ‚ÐºÑ€Ñ‹Ñ‚ÑŒ
    // Ð»Ð¸Ñ‡Ð½ÑƒÑŽ
    // Ñ‚Ð¾Ñ€Ð³Ð¾Ð²ÑƒÑŽ
    // Ð»Ð°Ð²ÐºÑƒ
    // Ð½ÐµÐ»ÑŒÐ·Ñ�.
    public static final int A_PRIVATE_WORKSHOP_MAY_NOT_BE_OPENED_IN_THIS_AREA = 1297; // Ð’
    // Ñ�Ñ‚Ð¾Ð¼
    // Ð¼ÐµÑ�Ñ‚Ðµ
    // Ð¾Ñ‚ÐºÑ€Ñ‹Ñ‚ÑŒ
    // Ð»Ð¸Ñ‡Ð½ÑƒÑŽ
    // Ð¼Ð°Ñ�Ñ‚ÐµÑ€Ñ�ÐºÑƒÑŽ
    // Ð½ÐµÐ»ÑŒÐ·Ñ�.
    public static final int EXITING_THE_MONSTER_RACE_TRACK = 1298; // ÐŸÐ¾Ð´Ñ‚Ð²ÐµÑ€Ð´Ð¸Ñ‚Ðµ
    // Ð¶ÐµÐ»Ð°Ð½Ð¸Ðµ
    // ÑƒÐ¹Ñ‚Ð¸ Ñ�
    // Ð¸Ð¿Ð¿Ð¾Ð´Ñ€Ð¾Ð¼Ð°
    // Ð¼Ð¾Ð½Ñ�Ñ‚Ñ€Ð¾Ð².
    public static final int S1S_CASTING_HAS_BEEN_INTERRUPTED = 1299; // Ð§Ñ‚ÐµÐ½Ð¸Ðµ
    // Ð·Ð°ÐºÐ»Ð¸Ð½Ð°Ð½Ð¸Ñ�
    // $c1 Ð±Ñ‹Ð»Ð¾
    // Ð¿Ñ€ÐµÑ€Ð²Ð°Ð½Ð¾.
    public static final int TRYING_ON_MODE_CANCELED = 1300; // Ð ÐµÐ¶Ð¸Ð¼ Ð¿Ñ€Ð¸Ð¼ÐµÑ€ÐºÐ¸
    // Ð¾Ñ‚Ð¼ÐµÐ½ÐµÐ½.
    public static final int CAN_BE_USED_ONLY_BY_THE_LORDS_OF_DAWN = 1301; // Ð’Ñ‹
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð²Ð¾Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒÑ�Ñ�
    // Ñ�Ñ‚Ð¸Ð¼
    // Ñ‚Ð¾Ð»ÑŒÐºÐ¾
    // Ð¿Ð¾Ñ�Ð»Ðµ
    // Ð¿Ñ€Ð¸Ñ�Ð¾ÐµÐ´Ð¸Ð½ÐµÐ½Ð¸Ñ�
    // Ðº
    // Ð›Ð¾Ñ€Ð´Ð°Ð¼
    // Ð Ð°Ñ�Ñ�Ð²ÐµÑ‚Ð°.
    public static final int CAN_BE_USED_ONLY_BY_THE_REVOLUTIONARIES_OF_DUSK = 1302; // Ð’Ñ‹
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð²Ð¾Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒÑ�Ñ�
    // Ñ�Ñ‚Ð¸Ð¼
    // Ñ‚Ð¾Ð»ÑŒÐºÐ¾
    // Ð¿Ð¾Ñ�Ð»Ðµ
    // Ð¿Ñ€Ð¸Ñ�Ð¾ÐµÐ´Ð¸Ð½ÐµÐ½Ð¸Ñ�
    // Ðº
    // ÐœÑ�Ñ‚ÐµÐ¶Ð½Ð¸ÐºÐ°Ð¼
    // Ð—Ð°ÐºÐ°Ñ‚Ð°.
    public static final int USED_ONLY_DURING_A_QUEST_EVENT_PERIOD = 1303; // Ð’Ñ‹
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð²Ð¾Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒÑ�Ñ�
    // Ñ�Ñ‚Ð¸Ð¼
    // Ñ‚Ð¾Ð»ÑŒÐºÐ¾
    // Ð²Ð¾
    // Ð²Ñ€ÐµÐ¼Ñ�
    // ÐºÐ²ÐµÑ�Ñ‚Ð¾Ð²Ð¾Ð³Ð¾
    // Ð¸Ð²ÐµÐ½Ñ‚Ð°.
    public static final int DUE_TO_THE_INFLUENCE_OF_THE_SEAL_OF_STRIFE_ALL_DEFENSIVE_REGISTRATION_HAS_BEEN_CANCELED_EXCEPT_BY_ALLIANCES_OF_CASTLE_OWNING_CLANS = 1304; // ÐŸÐ¾Ð´
    // Ð²Ð»Ð¸Ñ�Ð½Ð¸ÐµÐ¼
    // ÐŸÐµÑ‡Ð°Ñ‚Ð¸
    // Ð Ð°Ð·Ð´Ð¾Ñ€Ð°
    // Ð±Ñ‹Ð»Ð°
    // Ð¾Ñ‚Ð¼ÐµÐ½ÐµÐ½Ð°
    // Ñ€ÐµÐ³Ð¸Ñ�Ñ‚Ñ€Ð°Ñ†Ð¸Ñ�
    // ÐºÐ»Ð°Ð½Ð¾Ð²-Ð·Ð°Ñ‰Ð¸Ñ‚Ð½Ð¸ÐºÐ¾Ð²
    // ÐŸÐµÑ‡Ð°Ñ‚Ð¸.
    public static final int YOU_MAY_GIVE_SOMEONE_ELSE_A_SEAL_STONE_FOR_SAFEKEEPING_ONLY_DURING_A_QUEST_EVENT_PERIOD = 1305; // ÐšÐ°Ð¼Ð½Ð¸
    // ÐŸÐµÑ‡Ð°Ñ‚ÐµÐ¹
    // Ð¼Ð¾Ð³ÑƒÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð¿ÐµÑ€ÐµÐ´Ð°Ð½Ñ‹
    // Ñ‚Ð¾Ð»ÑŒÐºÐ¾
    // Ð²Ð¾
    // Ð²Ñ€ÐµÐ¼Ñ�
    // ÐºÐ²ÐµÑ�Ñ‚Ð¾Ð²Ð¾Ð³Ð¾
    // Ð¸Ð²ÐµÐ½Ñ‚Ð°.
    public static final int TRYING_ON_MODE_HAS_ENDED = 1306; // ÐŸÑ€Ð¸Ð¼ÐµÑ€ÐºÐ°
    // Ð·Ð°Ð²ÐµÑ€ÑˆÐµÐ½Ð°.
    public static final int ACCOUNTS_MAY_ONLY_BE_SETTLED_DURING_THE_SEAL_VALIDATION_PERIOD = 1307; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // ÑƒÐ¿Ñ€Ð°Ð²Ð»Ñ�Ñ‚ÑŒ
    // Ñ�Ð²Ð¾Ð¸Ð¼
    // Ð°ÐºÐºÐ°ÑƒÐ½Ñ‚Ð¾Ð¼
    // Ð²Ð¾
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ð´ÐµÐ¹Ñ�Ñ‚Ð²Ð¸Ñ�
    // ÐŸÐµÑ‡Ð°Ñ‚Ð¸.
    public static final int CONGRATULATIONS_YOU_HAVE_TRANSFERRED_TO_A_NEW_CLASS = 1308; // ÐŸÐ¾Ð·Ð´Ñ€Ð°Ð²Ð»Ñ�ÐµÐ¼!
    // Ð’Ñ‹
    // Ñ�Ð¼ÐµÐ½Ð¸Ð»Ð¸
    // ÐºÐ»Ð°Ñ�Ñ�.
    public static final int THIS_OPTION_REQUIRES_THAT_THE_LATEST_VERSION_OF_MSN_MESSENGER_CLIENT_BE_INSTALLED_ON_YOUR_COMPUTER = 1309; // Ð”Ð»Ñ�
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ð½Ð¸Ñ�
    // Ð´Ð°Ð½Ð½Ð¾Ð¹
    // Ñ„ÑƒÐ½ÐºÑ†Ð¸Ð¸
    // Ð²Ð°Ð¼
    // Ð½ÐµÐ¾Ð±Ñ…Ð¾Ð´Ð¸Ð¼Ð¾
    // ÑƒÑ�Ñ‚Ð°Ð½Ð¾Ð²Ð¸Ñ‚ÑŒ
    // Ð¿Ð¾Ñ�Ð»ÐµÐ´Ð½ÑŽÑŽ
    // Ð²ÐµÑ€Ñ�Ð¸ÑŽ
    // Ñ�Ð»ÑƒÐ¶Ð±Ñ‹
    // Ñ�Ð¾Ð¾Ð±Ñ‰ÐµÐ½Ð¸Ð¹
    // MSN.
    public static final int FOR_FULL_FUNCTIONALITY_THE_LATEST_VERSION_OF_MSN_MESSENGER_CLIENT_MUST_BE_INSTALLED_ON_THE_USERS_COMPUTER = 1310; // Ð§Ñ‚Ð¾Ð±Ñ‹
    // Ð²Ð¾Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒÑ�Ñ�
    // Ñ„ÑƒÐ½ÐºÑ†Ð¸Ñ�Ð¼Ð¸
    // Ñ�Ð»ÑƒÐ¶Ð±Ñ‹
    // Ñ�Ð¾Ð¾Ð±Ñ‰ÐµÐ½Ð¸Ð¹
    // MSN
    // Ð²
    // Ð¸Ð³Ñ€Ðµ,
    // Ð½ÐµÐ¾Ð±Ñ…Ð¾Ð´Ð¸Ð¼Ð¾
    // ÑƒÑ�Ñ‚Ð°Ð½Ð¾Ð²Ð¸Ñ‚ÑŒ
    // Ð¿Ð¾Ñ�Ð»ÐµÐ´Ð½ÑŽÑŽ
    // Ð²ÐµÑ€Ñ�Ð¸ÑŽ
    // MSN
    // Messenger.
    public static final int PREVIOUS_VERSIONS_OF_MSN_MESSENGER_ONLY_PROVIDE_THE_BASIC_FEATURES_TO_CHAT_IN_THE_GAME_ADD_DELETE_CONTACTS_AND_OTHER_OPTIONS_ARENT_AVAILABLE = 1311; // Ð•Ñ�Ð»Ð¸
    // Ð’Ñ‹
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·ÑƒÐµÑ‚Ðµ
    // Ñ€Ð°Ð½Ð½Ð¸Ðµ
    // Ð²ÐµÑ€Ñ�Ð¸Ð¸
    // Ñ�Ð»ÑƒÐ¶Ð±Ñ‹
    // Ñ�Ð¾Ð¾Ð±Ñ‰ÐµÐ½Ð¸Ð¹
    // MSN,
    // Ñ‚Ð¾
    // Ñƒ
    // Ð’Ð°Ñ�
    // Ð±ÑƒÐ´ÐµÑ‚
    // Ð²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾Ñ�Ñ‚ÑŒ
    // Ð±ÐµÑ�ÐµÐ´Ð¾Ð²Ð°Ñ‚ÑŒ,
    // Ð½Ð¾
    // Ð´Ð¾Ð±Ð°Ð²Ð¸Ñ‚ÑŒ/ÑƒÐ´Ð°Ð»Ð¸Ñ‚ÑŒ
    // Ñ�Ð¾Ð±ÐµÑ�ÐµÐ´Ð½Ð¸ÐºÐ°
    // Ð’Ñ‹
    // Ð½Ðµ
    // Ñ�Ð¼Ð¾Ð¶ÐµÑ‚Ðµ.
    public static final int THE_LATEST_VERSION_OF_MSN_MESSENGER_MAY_BE_OBTAINED_FROM_THE_MSN_WEB_SITE_ = 1312; // Ð”Ð»Ñ�
    // ÑƒÑ�Ñ‚Ð°Ð½Ð¾Ð²ÐºÐ¸
    // Ð¿Ð¾Ñ�Ð»ÐµÐ´Ð½ÐµÐ¹
    // Ð²ÐµÑ€Ñ�Ð¸Ð¸
    // Ñ�Ð»ÑƒÐ¶Ð±Ñ‹
    // Ñ�Ð¾Ð¾Ð±Ñ‰ÐµÐ½Ð¸Ð¹
    // MSN
    // Ð¿Ð¾Ñ�ÐµÑ‚Ð¸Ñ‚Ðµ
    // Ñ�Ð°Ð¹Ñ‚
    // http://messenger.msn.com.
    public static final int S1_TO_BETTER_SERVE_OUR_CUSTOMERS_ALL_CHAT_HISTORIES_ARE_STORED_AND_MAINTAINED_BY_NCSOFT_IF_YOU_DO_NOT_AGREE_TO_HAVE_YOUR_CHAT_RECORDS_STORED_CLOSE_THE_CHAT_WINDOW_NOW_FOR_MORE_INFORMATION_REGARDING_THIS_ISSUE_PLEASE_VISIT_OUR_HOME_PAGE_AT_WWWNCSOFTCOM = 1313; // $s1,
    // Ð´Ð»Ñ�
    // ÑƒÐ»ÑƒÑ‡ÑˆÐµÐ½Ð¸Ñ�
    // Ð¿Ñ€ÐµÐ´Ð¾Ñ�Ñ‚Ð°Ð²Ð»Ñ�ÐµÐ¼Ñ‹Ñ…
    // ÑƒÑ�Ð»ÑƒÐ³
    // NCSoft
    // Ñ�Ð¾Ñ…Ñ€Ð°Ð½Ñ�ÐµÑ‚
    // Ð·Ð°Ð¿Ð¸Ñ�Ð¸
    // Ð’Ð°ÑˆÐ¸Ñ…
    // Ñ€Ð°Ð·Ð³Ð¾Ð²Ð¾Ñ€Ð¾Ð²
    // Ñ�
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶ÐµÐ¼
    // $s2.
    // Ð•Ñ�Ð»Ð¸
    // Ð’Ñ‹
    // Ñ…Ð¾Ñ‚Ð¸Ñ‚Ðµ
    // Ð¾Ñ‚Ð¼ÐµÐ½Ð¸Ñ‚ÑŒ
    // Ñ�Ð¾Ñ…Ñ€Ð°Ð½ÐµÐ½Ð¸Ðµ
    // Ð´Ð¸Ð°Ð»Ð¾Ð³Ð¾Ð²,
    // Ð·Ð°ÐºÑ€Ð¾Ð¹Ñ‚Ðµ
    // Ð¾ÐºÐ½Ð¾
    // Ñ‡Ð°Ñ‚Ð°.
    // Ð”Ð»Ñ�
    // Ð¿Ð¾Ð»ÑƒÑ‡ÐµÐ½Ð¸Ñ�
    // Ð±Ð¾Ð»ÐµÐµ
    // Ð¿Ð¾Ð´Ñ€Ð¾Ð±Ð½Ð¾Ð¹
    // Ð¸Ð½Ñ„Ð¾Ñ€Ð¼Ð°Ñ†Ð¸Ð¸
    // Ð¿Ð¾Ñ�ÐµÑ‚Ð¸Ñ‚Ðµ
    // Ñ�Ð°Ð¹Ñ‚
    // Ð½Ð°ÑˆÐµÐ¹
    // ÐºÐ¾Ð¼Ð¿Ð°Ð½Ð¸Ð¸.
    public static final int PLEASE_ENTER_THE_PASSPORT_ID_OF_THE_PERSON_YOU_WISH_TO_ADD_TO_YOUR_CONTACT_LIST = 1314; // Ð’Ð²ÐµÐ´Ð¸Ñ‚Ðµ
    // Ð¸Ð¼Ñ�
    // Ñ‡ÐµÐ»Ð¾Ð²ÐµÐºÐ°,
    // ÐºÐ¾Ñ‚Ð¾Ñ€Ð¾Ð³Ð¾
    // Ñ…Ð¾Ñ‚Ð¸Ñ‚Ðµ
    // Ð´Ð¾Ð±Ð°Ð²Ð¸Ñ‚ÑŒ
    // Ð²
    // Ð´Ñ€ÑƒÐ·ÑŒÑ�.
    public static final int DELETING_A_CONTACT_WILL_REMOVE_THAT_CONTACT_FROM_MSN_MESSENGER_AS_WELL_THE_CONTACT_CAN_STILL_CHECK_YOUR_ONLINE_STATUS_AND_WILL_NOT_BE_BLOCKED_FROM_SENDING_YOU_A_MESSAGE = 1315; // Ð•Ñ�Ð»Ð¸
    // Ð’Ñ‹
    // ÑƒÐ´Ð°Ð»Ð¸Ñ‚Ðµ
    // Ð´Ð°Ð½Ð½Ð¾Ð³Ð¾
    // Ñ�Ð¾Ð±ÐµÑ�ÐµÐ´Ð½Ð¸ÐºÐ°,
    // Ð¾Ð½
    // Ð±ÑƒÐ´ÐµÑ‚
    // Ñ‚Ð°ÐºÐ¶Ðµ
    // ÑƒÐ´Ð°Ð»ÐµÐ½
    // Ð¸Ð·
    // Ñ�Ð¿Ð¸Ñ�ÐºÐ°
    // ÐºÐ¾Ð½Ñ‚Ð°ÐºÑ‚Ð¾Ð²
    // Ð²
    // Ñ�Ð»ÑƒÐ¶Ð±Ðµ
    // Ñ�Ð¾Ð¾Ð±Ñ‰ÐµÐ½Ð¸Ð¹
    // MSN.
    // Ð�Ð¾
    // Ñ�Ñ‚Ð¾
    // Ð½Ðµ
    // Ð·Ð°Ð±Ð»Ð¾ÐºÐ¸Ñ€ÑƒÐµÑ‚
    // ÐµÐ³Ð¾
    // Ð´Ð¾Ñ�Ñ‚ÑƒÐ¿
    // Ðº
    // Ð¸Ð½Ñ„Ð¾Ñ€Ð¼Ð°Ñ†Ð¸Ð¸
    // Ð¾
    // Ð’Ð°ÑˆÐµÐ¼
    // Ñ�Ñ‚Ð°Ñ‚ÑƒÑ�Ðµ,
    // Ð¸
    // Ð¾Ð½
    // Ñ�Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð¿Ð¾Ñ�Ñ‹Ð»Ð°Ñ‚ÑŒ
    // Ð’Ð°Ð¼
    // Ñ�Ð¾Ð¾Ð±Ñ‰ÐµÐ½Ð¸Ñ�.
    public static final int THE_CONTACT_WILL_BE_DELETED_AND_BLOCKED_FROM_YOUR_CONTACT_LIST = 1316; // Ð­Ñ‚Ð¾Ñ‚
    // ÐºÐ¾Ð½Ñ‚Ð°ÐºÑ‚
    // Ð±ÑƒÐ´ÐµÑ‚
    // ÑƒÐ´Ð°Ð»ÐµÐ½
    // Ð¸
    // Ð·Ð°Ð±Ð»Ð¾ÐºÐ¸Ñ€Ð¾Ð²Ð°Ð½.
    public static final int WOULD_YOU_LIKE_TO_DELETE_THIS_CONTACT = 1317; // Ð’Ñ‹
    // Ð´ÐµÐ¹Ñ�Ñ‚Ð²Ð¸Ñ‚ÐµÐ»ÑŒÐ½Ð¾
    // Ñ…Ð¾Ñ‚Ð¸Ñ‚Ðµ
    // ÑƒÐ´Ð°Ð»Ð¸Ñ‚ÑŒ
    // Ð´Ð°Ð½Ð½Ñ‹Ð¹
    // ÐºÐ¾Ð½Ñ‚Ð°ÐºÑ‚?
    public static final int PLEASE_SELECT_THE_CONTACT_YOU_WANT_TO_BLOCK_OR_UNBLOCK = 1318; // Ð’Ñ‹Ð±ÐµÑ€Ð¸Ñ‚Ðµ
    // Ð´Ñ€ÑƒÐ³Ð°,
    // ÐºÐ¾Ñ‚Ð¾Ñ€Ð¾Ð³Ð¾
    // Ñ…Ð¾Ñ‚Ð¸Ñ‚Ðµ
    // Ð·Ð°Ð±Ð»Ð¾ÐºÐ¸Ñ€Ð¾Ð²Ð°Ñ‚ÑŒ/Ñ€Ð°Ð·Ð±Ð»Ð¾ÐºÐ¸Ñ€Ð¾Ð²Ð°Ñ‚ÑŒ.
    public static final int PLEASE_SELECT_THE_NAME_OF_THE_CONTACT_YOU_WISH_TO_CHANGE_TO_ANOTHER_GROUP = 1319; // Ð’Ñ‹Ð±ÐµÑ€Ð¸Ñ‚Ðµ
    // Ð´Ñ€ÑƒÐ³Ð°,
    // Ð³Ñ€ÑƒÐ¿Ð¿Ñƒ
    // ÐºÐ¾Ñ‚Ð¾Ñ€Ð¾Ð³Ð¾
    // Ñ…Ð¾Ñ‚Ð¸Ñ‚Ðµ
    // Ð¸Ð·Ð¼ÐµÐ½Ð¸Ñ‚ÑŒ.
    public static final int AFTER_SELECTING_THE_GROUP_YOU_WISH_TO_MOVE_YOUR_CONTACT_TO_PRESS_THE_OK_BUTTON = 1320; // Ð’Ñ‹Ð±Ñ€Ð°Ð²
    // Ð³Ñ€ÑƒÐ¿Ð¿Ñƒ,
    // Ð²
    // ÐºÐ¾Ñ‚Ð¾Ñ€ÑƒÑŽ
    // Ñ…Ð¾Ñ‚Ð¸Ñ‚Ðµ
    // Ð¿Ð¾Ð¼ÐµÑ�Ñ‚Ð¸Ñ‚ÑŒ
    // ÐºÐ¾Ð½Ñ‚Ð°ÐºÑ‚,
    // Ð½Ð°Ð¶Ð¼Ð¸Ñ‚Ðµ
    // ÐºÐ½Ð¾Ð¿ÐºÑƒ
    // "ÐžÐš".
    public static final int ENTER_THE_NAME_OF_THE_GROUP_YOU_WISH_TO_ADD = 1321; // Ð’Ð²ÐµÐ´Ð¸Ñ‚Ðµ
    // Ð½Ð°Ð·Ð²Ð°Ð½Ð¸Ðµ
    // Ð³Ñ€ÑƒÐ¿Ð¿Ñ‹,
    // ÐºÐ¾Ñ‚Ð¾Ñ€ÑƒÑŽ
    // Ñ…Ð¾Ñ‚Ð¸Ñ‚Ðµ
    // Ð´Ð¾Ð±Ð°Ð²Ð¸Ñ‚ÑŒ.
    public static final int SELECT_THE_GROUP_AND_ENTER_THE_NEW_NAME = 1322; // Ð’Ñ‹Ð±Ñ€Ð°Ð²
    // Ð³Ñ€ÑƒÐ¿Ð¿Ñƒ,
    // ÐºÐ¾Ñ‚Ð¾Ñ€ÑƒÑŽ
    // Ñ…Ð¾Ñ‚Ð¸Ñ‚Ðµ
    // Ð¸Ð·Ð¼ÐµÐ½Ð¸Ñ‚ÑŒ,
    // Ð²Ð²ÐµÐ´Ð¸Ñ‚Ðµ
    // Ð½Ð¾Ð²Ð¾Ðµ
    // Ð½Ð°Ð·Ð²Ð°Ð½Ð¸Ðµ.
    public static final int SELECT_THE_GROUP_YOU_WISH_TO_DELETE_AND_CLICK_THE_OK_BUTTON = 1323; // Ð’Ñ‹Ð±ÐµÑ€Ð¸Ñ‚Ðµ
    // Ð³Ñ€ÑƒÐ¿Ð¿Ñƒ,
    // ÐºÐ¾Ñ‚Ð¾Ñ€ÑƒÑŽ
    // Ñ…Ð¾Ñ‚Ð¸Ñ‚Ðµ
    // ÑƒÐ´Ð°Ð»Ð¸Ñ‚ÑŒ,
    // Ð¸
    // Ð½Ð°Ð¶Ð¼Ð¸Ñ‚Ðµ
    // ÐºÐ½Ð¾Ð¿ÐºÑƒ
    // "ÐžÐš".
    public static final int SIGNING_IN = 1324; // Ð’Ñ…Ð¾Ð´ Ð² Ñ�Ð¸Ñ�Ñ‚ÐµÐ¼Ñƒâ€¦
    public static final int YOUVE_LOGGED_INTO_ANOTHER_COMPUTER_AND_BEEN_LOGGED_OUT_OF_THE_NET_MESSENGER_SERVICE_ON_THIS_COMPUTER = 1325; // Ð’Ñ‹
    // Ð²Ð¾ÑˆÐ»Ð¸
    // Ð½Ð°
    // Ð´Ñ€ÑƒÐ³Ð¾Ð¼
    // ÐºÐ¾Ð¼Ð¿ÑŒÑŽÑ‚ÐµÑ€Ðµ,
    // Ð¿Ð¾Ñ�Ñ‚Ð¾Ð¼Ñƒ
    // Ð¿Ñ€Ð¾Ð¸Ð·Ð¾ÑˆÐµÐ»
    // Ð²Ñ‹Ñ…Ð¾Ð´
    // Ð¸Ð·
    // .NET
    // Messenger
    // Service.
    public static final int S1_ = 1326; // $s1:
    public static final int THE_FOLLOWING_MESSAGE_COULD_NOT_BE_DELIVERED = 1327; // Ð­Ñ‚Ð¾
    // Ñ�Ð¾Ð¾Ð±Ñ‰ÐµÐ½Ð¸Ðµ
    // Ð½Ðµ
    // Ð±Ñ‹Ð»Ð¾
    // Ð´Ð¾Ñ�Ñ‚Ð°Ð²Ð»ÐµÐ½Ð¾:
    public static final int MEMBERS_OF_THE_REVOLUTIONARIES_OF_DUSK_WILL_NOT_BE_RESURRECTED = 1328; // ÐœÑ�Ñ‚ÐµÐ¶Ð½Ð¸ÐºÐ¸
    // Ð—Ð°ÐºÐ°Ñ‚Ð°
    // Ð½Ðµ
    // Ð¼Ð¾Ð³ÑƒÑ‚
    // Ð²Ð¾Ð·Ñ€Ð¾Ð´Ð¸Ñ‚ÑŒÑ�Ñ�.
    public static final int YOU_ARE_CURRENTLY_BANNED_FROM_ACTIVITIES_RELATED_TO_THE_PRIVATE_STORE_AND_PRIVATE_WORKSHOP = 1329; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð²Ð¾Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒÑ�Ñ�
    // Ñ„ÑƒÐ½ÐºÑ†Ð¸Ñ�Ð¼Ð¸
    // Ð»Ð¸Ñ‡Ð½Ð¾Ð¹
    // Ñ‚Ð¾Ñ€Ð³Ð¾Ð²Ð¾Ð¹
    // Ð»Ð°Ð²ÐºÐ¸
    // Ð¸
    // Ð¼Ð°Ñ�Ñ‚ÐµÑ€Ñ�ÐºÐ¾Ð¹.
    public static final int NO_PRIVATE_STORE_OR_PRIVATE_WORKSHOP_MAY_BE_OPENED_FOR_S1_MINUTES = 1330; // Ð›Ð¸Ñ‡Ð½Ð°Ñ�
    // Ñ‚Ð¾Ñ€Ð³Ð¾Ð²Ð°Ñ�
    // Ð¸
    // Ð¼Ð°Ñ�Ñ‚ÐµÑ€Ñ�ÐºÐ°Ñ�
    // Ð·Ð°Ð±Ð»Ð¾ÐºÐ¸Ñ€Ð¾Ð²Ð°Ð½Ñ‹
    // Ð½Ð°
    // $s1
    // Ð¼Ð¸Ð½.
    public static final int ACTIVITIES_RELATED_TO_THE_PRIVATE_STORE_AND_PRIVATE_WORKSHOP_ARE_NOW_PERMITTED = 1331; // Ð‘Ð»Ð¾ÐºÐ¸Ñ€Ð¾Ð²ÐºÐ°
    // Ð½Ð°
    // Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ð½Ð¸Ðµ
    // Ð»Ð¸Ñ‡Ð½Ð¾Ð¹
    // Ñ‚Ð¾Ñ€Ð³Ð¾Ð²Ð¾Ð¹
    // Ð»Ð°Ð²ÐºÐ¾Ð¹
    // Ð¸
    // Ð¼Ð°Ñ�Ñ‚ÐµÑ€Ñ�ÐºÐ¾Ð¹
    // Ñ�Ð½Ñ�Ñ‚Ð°.
    public static final int ITEMS_MAY_NOT_BE_USED_AFTER_YOUR_CHARACTER_OR_PET_DIES = 1332; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒ
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚,
    // ÐµÑ�Ð»Ð¸
    // Ð’Ñ‹
    // Ð¼ÐµÑ€Ñ‚Ð²Ñ‹.
    public static final int REPLAY_FILE_ISNT_ACCESSIBLE_VERIFY_THAT_REPLAYINI_FILE_EXISTS = 1333; // Ð�ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾
    // Ñ�Ñ‡Ð¸Ñ‚Ð°Ñ‚ÑŒ
    // Ñ„Ð°Ð¹Ð»
    // Replay.
    // ÐŸÑ€Ð¾Ð²ÐµÑ€ÑŒÑ‚Ðµ
    // Ñ„Ð°Ð¹Ð»
    // Replay.ini.
    public static final int THE_NEW_CAMERA_DATA_HAS_BEEN_STORED = 1334; // Ð¡Ð¾Ñ…Ñ€Ð°Ð½ÐµÐ½Ð°
    // Ð½Ð¾Ð²Ð°Ñ�
    // Ð¸Ð½Ñ„Ð¾Ñ€Ð¼Ð°Ñ†Ð¸Ñ�
    // Ñ�
    // ÐºÐ°Ð¼ÐµÑ€Ñ‹.
    public static final int THE_ATTEMPT_TO_STORE_THE_NEW_CAMERA_DATA_HAS_FAILED = 1335; // ÐžÑˆÐ¸Ð±ÐºÐ°
    // Ð¿Ñ€Ð¸
    // Ñ�Ð¾Ñ…Ñ€Ð°Ð½ÐµÐ½Ð¸Ð¸
    // Ð¸Ð½Ñ„Ð¾Ñ€Ð¼Ð°Ñ†Ð¸Ð¸
    // Ñ�
    // ÐºÐ°Ð¼ÐµÑ€Ñ‹.
    public static final int THE_REPLAY_FILE_HAS_BEEN_CORRUPTED_PLEASE_CHECK_THE_S1S2_FILE = 1336; // Ð¤Ð°Ð¹Ð»
    // Ð¿Ð¾Ð²Ñ‚Ð¾Ñ€Ð°,
    // $s1.$s2,
    // Ð±Ñ‹Ð»
    // Ð¿Ð¾Ð²Ñ€ÐµÐ¶Ð´ÐµÐ½.
    // ÐŸÐ¾Ð¶Ð°Ð»ÑƒÐ¹Ñ�Ñ‚Ð°,
    // Ð¿Ñ€Ð¾Ð²ÐµÑ€ÑŒÑ‚Ðµ
    // ÐµÐ³Ð¾.
    public static final int REPLAY_MODE_WILL_BE_TERMINATED_DO_YOU_WISH_TO_CONTINUE = 1337; // ÐŸÐ¾Ð²Ñ‚Ð¾Ñ€
    // Ð±ÑƒÐ´ÐµÑ‚
    // Ð·Ð°Ð²ÐµÑ€ÑˆÐµÐ½.
    // ÐŸÑ€Ð¾Ð´Ð¾Ð»Ð¶Ð¸Ñ‚ÑŒ?
    public static final int YOU_HAVE_EXCEEDED_THE_QUANTITY_THAT_CAN_BE_TRANSFERRED_AT_ONE_TIME = 1338; // Ð’Ñ‹
    // Ð¿Ñ€ÐµÐ²Ñ‹Ñ�Ð¸Ð»Ð¸
    // ÐºÐ¾Ð»Ð¸Ñ‡ÐµÑ�Ñ‚Ð²Ð¾,
    // ÐºÐ¾Ñ‚Ð¾Ñ€Ð¾Ðµ
    // Ð¼Ð¾Ð¶Ð½Ð¾
    // Ð¿ÐµÑ€ÐµÐ¼ÐµÑ�Ñ‚Ð¸Ñ‚ÑŒ
    // Ð·Ð°
    // Ð¾Ð´Ð¸Ð½
    // Ñ€Ð°Ð·.
    public static final int ONCE_A_MACRO_IS_ASSIGNED_TO_A_SHORTCUT_IT_CANNOT_BE_RUN_AS_A_MACRO_AGAIN = 1339; // Ð•Ñ�Ð»Ð¸
    // Ð¼Ð°ÐºÑ€Ð¾Ñ�
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·ÑƒÐµÑ‚Ñ�Ñ�
    // ÐºÐ°Ðº
    // ÐºÐ»Ð°Ð²Ð¸ÑˆÐ°
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð²,
    // Ð²Ð¾Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒÑ�Ñ�
    // Ð¸Ð¼
    // Ñ�Ð½Ð¾Ð²Ð°
    // Ð½ÐµÐ»ÑŒÐ·Ñ�.
    public static final int THIS_SERVER_CANNOT_BE_ACCESSED_BY_THE_COUPON_YOU_ARE_USING = 1340; // Ð•Ñ�Ð»Ð¸
    // ÐºÑƒÐ¿Ð¾Ð½
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·ÑƒÐµÑ‚Ñ�Ñ�,
    // Ð¿Ð¾Ð´ÐºÐ»ÑŽÑ‡Ð¸Ñ‚ÑŒÑ�Ñ�
    // Ðº
    // Ñ�Ñ‚Ð¾Ð¼Ñƒ
    // Ñ�ÐµÑ€Ð²ÐµÑ€Ñƒ
    // Ð½ÐµÐ»ÑŒÐ·Ñ�.
    public static final int THE_NAME_OR_E_MAIL_ADDRESS_YOU_ENTERED_IS_INCORRECT = 1341; // Ð�ÐµÐ²ÐµÑ€Ð½Ð¾Ðµ
    // Ð¸Ð¼Ñ�
    // Ð¸Ð»Ð¸
    // Ñ�Ð»ÐµÐºÑ‚Ñ€Ð¾Ð½Ð½Ñ‹Ð¹
    // Ð°Ð´Ñ€ÐµÑ�.
    public static final int YOU_ARE_ALREADY_LOGGED_IN = 1342; // Ð’Ñ‹ ÑƒÐ¶Ðµ Ð² Ð¸Ð³Ñ€Ðµ.
    public static final int THE_PASSWORD_OR_E_MAIL_ADDRESS_YOU_ENTERED_IS_INCORRECT__YOUR_ATTEMPT_TO_LOG_INTO_NET_MESSENGER_SERVICE_HAS_FAILED = 1343; // Ð’Ñ‹
    // Ð²Ð²ÐµÐ»Ð¸
    // Ð½ÐµÐ²ÐµÑ€Ð½Ñ‹Ð¹
    // Ð¿Ð°Ñ€Ð¾Ð»ÑŒ
    // Ð¸Ð»Ð¸
    // Ñ�Ð»ÐµÐºÑ‚Ñ€Ð¾Ð½Ð½Ñ‹Ð¹
    // Ð°Ð´Ñ€ÐµÑ�,
    // Ð¿Ð¾Ñ�Ñ‚Ð¾Ð¼Ñƒ
    // Ð¿Ð¾Ð´ÐºÐ»ÑŽÑ‡Ð¸Ñ‚ÑŒÑ�Ñ�
    // Ðº
    // .NET
    // Messenger
    // Service
    // Ð½ÐµÐ»ÑŒÐ·Ñ�.
    public static final int THE_SERVICE_YOU_REQUESTED_COULD_NOT_BE_LOCATED_AND_THEREFORE_YOUR_ATTEMPT_TO_LOG_INTO_THE_NET_MESSENGER_SERVICE_HAS_FAILED_PLEASE_VERIFY_THAT_YOU_ARE_CURRENTLY_CONNECTED_TO_THE_INTERNET = 1344; // Ð’Ñ‹
    // Ð½Ðµ
    // Ñ�Ð¼Ð¾Ð³Ð»Ð¸
    // Ð²Ð¾Ð¹Ñ‚Ð¸
    // Ð²
    // Ñ�Ð¸Ñ�Ñ‚ÐµÐ¼Ñƒ
    // .NET
    // Messenger
    // Service.
    // ÐŸÑ€Ð¾Ð²ÐµÑ€ÑŒÑ‚Ðµ
    // Ñ�Ð¾ÐµÐ´Ð¸Ð½ÐµÐ½Ð¸Ðµ
    // Ñ�
    // Ð¸Ð½Ñ‚ÐµÑ€Ð½ÐµÑ‚Ð¾Ð¼.
    public static final int AFTER_SELECTING_A_CONTACT_NAME_CLICK_ON_THE_OK_BUTTON = 1345; // Ð’Ñ‹Ð±ÐµÑ€Ð¸Ñ‚Ðµ
    // Ñ�Ð¾Ð±ÐµÑ�ÐµÐ´Ð½Ð¸ÐºÐ°
    // Ð¸
    // Ð½Ð°Ð¶Ð¼Ð¸Ñ‚Ðµ
    // "ÐžÐš".
    public static final int YOU_ARE_CURRENTLY_ENTERING_A_CHAT_MESSAGE = 1346; // Ð’Ñ‹
    // Ð²Ð²Ð¾Ð´Ð¸Ñ‚Ðµ
    // Ð²
    // Ñ‡Ð°Ñ‚
    // Ñ�Ð¾Ð¾Ð±Ñ‰ÐµÐ½Ð¸Ðµ.
    public static final int THE_LINEAGE_II_MESSENGER_COULD_NOT_CARRY_OUT_THE_TASK_YOU_REQUESTED = 1347; // Lineage
    // II
    // Messenger
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð²Ñ‹Ð¿Ð¾Ð»Ð½Ð¸Ñ‚ÑŒ
    // Ð·Ð°Ð¿Ñ€Ð¾Ñ�.
    public static final int S1_HAS_ENTERED_THE_CHAT_ROOM = 1348; // $s1 Ð²Ñ…Ð¾Ð´Ð¸Ñ‚ Ð²
    // Ñ‡Ð°Ñ‚.
    public static final int S1_HAS_LEFT_THE_CHAT_ROOM = 1349; // $s1 Ð²Ñ‹Ñ…Ð¾Ð´Ð¸Ñ‚ Ð¸Ð·
    // Ñ‡Ð°Ñ‚Ð°.
    public static final int THE_STATUS_WILL_BE_CHANGED_TO_INDICATE__OFF_LINE__ALL_THE_CHAT_WINDOWS_CURRENTLY_OPENED_WILL_BE = 1350; // Ð’Ñ‹
    // Ð¸Ð·Ð¼ÐµÐ½Ð¸Ð»Ð¸
    // Ñ�Ñ‚Ð°Ñ‚ÑƒÑ�
    // Ð½Ð°
    // "ÐžÑ‚ÐºÐ»ÑŽÑ‡ÐµÐ½".
    // Ð’Ñ�Ðµ
    // Ð¾ÐºÐ½Ð°
    // Ð±ÑƒÐ´ÑƒÑ‚
    // Ð·Ð°ÐºÑ€Ñ‹Ñ‚Ñ‹.
    public static final int AFTER_SELECTING_THE_CONTACT_YOU_WANT_TO_DELETE_CLICK_THE_DELETE_BUTTON = 1351; // Ð’Ñ‹Ð±ÐµÑ€Ð¸Ñ‚Ðµ
    // ÐºÐ¾Ð½Ñ‚Ð°ÐºÑ‚
    // Ð¸
    // Ð½Ð°Ð¶Ð¼Ð¸Ñ‚Ðµ
    // ÐºÐ½Ð¾Ð¿ÐºÑƒ
    // "Ð£Ð´Ð°Ð»Ð¸Ñ‚ÑŒ".
    public static final int YOU_HAVE_BEEN_ADDED_TO_THE_CONTACT_LIST_OF_S1_S2 = 1352; // $s1
    // ($s2)
    // Ð´Ð¾Ð±Ð°Ð²Ð»Ñ�ÐµÑ‚
    // Ð’Ð°Ñ�
    // Ð²
    // Ñ�Ð¿Ð¸Ñ�Ð¾Ðº
    // ÐºÐ¾Ð½Ñ‚Ð°ÐºÑ‚Ð¾Ð².
    public static final int YOU_CAN_SET_THE_OPTION_TO_SHOW_YOUR_STATUS_AS_ALWAYS_BEING_OFF_LINE_TO_ALL_OF_YOUR_CONTACTS = 1353; // Ð’Ñ‹
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ñ�Ð¼ÐµÐ½Ð¸Ñ‚ÑŒ
    // Ñ�Ñ‚Ð°Ñ‚ÑƒÑ�
    // Ð½Ð°
    // "ÐžÑ‚ÐºÐ»ÑŽÑ‡ÐµÐ½"
    // Ð´Ð»Ñ�
    // Ð²Ñ�ÐµÑ…
    // ÐºÐ¾Ð½Ñ‚Ð°ÐºÑ‚Ð¾Ð²
    // Ð²
    // Ñ�Ð¿Ð¸Ñ�ÐºÐµ.
    public static final int YOU_ARE_NOT_ALLOWED_TO_CHAT_WITH_YOUR_CONTACT_WHILE_YOU_ARE_BLOCKED_FROM_CHATTING = 1354; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¾Ð±Ñ‰Ð°Ñ‚ÑŒÑ�Ñ�
    // Ð²Ð¾
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ð±Ð»Ð¾ÐºÐ¸Ñ€Ð¾Ð²ÐºÐ¸
    // Ñ‡Ð°Ñ‚Ð°.
    public static final int THE_CONTACT_YOU_CHOSE_TO_CHAT_WITH_IS_CURRENTLY_BLOCKED_FROM_CHATTING = 1355; // Ð£
    // Ñ�Ñ‚Ð¾Ð³Ð¾
    // ÐºÐ¾Ð½Ñ‚Ð°ÐºÑ‚Ð°
    // Ð·Ð°Ð±Ð»Ð¾ÐºÐ¸Ñ€Ð¾Ð²Ð°Ð½Ð°
    // Ñ„ÑƒÐ½ÐºÑ†Ð¸Ñ�
    // Ñ‡Ð°Ñ‚Ð°.
    public static final int THE_CONTACT_YOU_CHOSE_TO_CHAT_WITH_IS_NOT_CURRENTLY_LOGGED_IN = 1356; // Ð’Ñ‹Ð±Ñ€Ð°Ð½Ð½Ñ‹Ð¹
    // ÐºÐ¾Ð½Ñ‚Ð°ÐºÑ‚
    // Ð¾Ñ‚ÐºÐ»ÑŽÑ‡ÐµÐ½.
    public static final int YOU_HAVE_BEEN_BLOCKED_FROM_THE_CONTACT_YOU_SELECTED = 1357; // Ð­Ñ‚Ð¾Ñ‚
    // ÐºÐ¾Ð½Ñ‚Ð°ÐºÑ‚
    // Ð·Ð°Ð±Ð»Ð¾ÐºÐ¸Ñ€Ð¾Ð²Ð°Ð»
    // Ð²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾Ñ�Ñ‚ÑŒ
    // Ñ‡Ð°Ñ‚Ð°
    // Ñ�
    // Ð½Ð¸Ð¼.
    public static final int YOU_ARE_BEING_LOGGED_OUT = 1358; // Ð’Ñ‹Ñ…Ð¾Ð´ Ð¸Ð·
    // Ñ�Ð¸Ñ�Ñ‚ÐµÐ¼Ñ‹â€¦
    public static final int S1_HAS_LOGGED_IN_1 = 1359; // $s1 Ð²Ñ…Ð¾Ð´Ð¸Ñ‚ Ð² Ð¸Ð³Ñ€Ñƒ.
    public static final int YOU_HAVE_RECEIVED_A_MESSAGE_FROM_S1 = 1360; // $s1:
    // Ð¿Ð¾Ð»ÑƒÑ‡ÐµÐ½Ð¾
    // Ñ�Ð¾Ð¾Ð±Ñ‰ÐµÐ½Ð¸Ðµ.
    public static final int DUE_TO_A_SYSTEM_ERROR_YOU_HAVE_BEEN_LOGGED_OUT_OF_THE_NET_MESSENGER_SERVICE = 1361; // Ð˜Ð·-Ð·Ð°
    // Ñ�Ð¸Ñ�Ñ‚ÐµÐ¼Ð½Ð¾Ð¹
    // Ð¾ÑˆÐ¸Ð±ÐºÐ¸
    // Ð¿Ñ€Ð¾Ð¸Ð·Ð¾ÑˆÐµÐ»
    // Ð²Ñ‹Ñ…Ð¾Ð´
    // Ð¸Ð·
    // .NET
    // Messenger
    // Service.
    public static final int PLEASE_SELECT_THE_CONTACT_YOU_WISH_TO_DELETE__IF_YOU_WOULD_LIKE_TO_DELETE_A_GROUP_CLICK_THE_BUTTON_NEXT_TO_MY_STATUS_AND_THEN_USE_THE_OPTIONS_MENU = 1362; // Ð’Ñ‹Ð±ÐµÑ€Ð¸Ñ‚Ðµ
    // ÐºÐ¾Ð½Ñ‚Ð°ÐºÑ‚,
    // ÐºÐ¾Ñ‚Ð¾Ñ€Ñ‹Ð¹
    // Ñ…Ð¾Ñ‚Ð¸Ñ‚Ðµ
    // ÑƒÐ´Ð°Ð»Ð¸Ñ‚ÑŒ.
    // Ð•Ñ�Ð»Ð¸
    // Ð’Ñ‹
    // Ñ…Ð¾Ñ‚Ð¸Ñ‚Ðµ
    // ÑƒÐ´Ð°Ð»Ð¸Ñ‚ÑŒ
    // Ð³Ñ€ÑƒÐ¿Ð¿Ñƒ,
    // Ð½Ð°Ð¶Ð¼Ð¸Ñ‚Ðµ
    // Ð½Ð°
    // ÐºÐ½Ð¾Ð¿ÐºÑƒ
    // Ñ€Ñ�Ð´Ð¾Ð¼
    // Ñ�
    // Ð’Ð°ÑˆÐ¸Ð¼
    // Ñ�Ñ‚Ð°Ñ‚ÑƒÑ�Ð¾Ð¼
    // Ð¸
    // Ð²Ð¾Ñ�Ð¿Ð¾Ð»ÑŒÐ·ÑƒÐ¹Ñ‚ÐµÑ�ÑŒ
    // Ð¼ÐµÐ½ÑŽ.
    public static final int YOUR_REQUEST_TO_PARTICIPATE_IN_THE_ALLIANCE_WAR_HAS_BEEN_DENIED = 1363; // ÐŸÑ€Ð¸ÑˆÐµÐ»
    // Ð¾Ñ‚ÐºÐ°Ð·
    // Ð½Ð°
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð¸Ðµ
    // Ð²
    // Ð²Ð¾Ð¹Ð½Ðµ
    // Ð°Ð»ÑŒÑ�Ð½Ñ�Ð¾Ð².
    public static final int THE_REQUEST_FOR_AN_ALLIANCE_WAR_HAS_BEEN_REJECTED = 1364; // Ð’Ñ‹
    // Ð¾Ñ‚ÐºÐ°Ð·Ð°Ð»Ð¸Ñ�ÑŒ
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð²Ð¾Ð²Ð°Ñ‚ÑŒ
    // Ð²
    // Ð²Ð¾Ð¹Ð½Ðµ
    // Ð°Ð»ÑŒÑ�Ð½Ñ�Ð¾Ð².
    public static final int S2_OF_S1_CLAN_HAS_SURRENDERED_AS_AN_INDIVIDUAL = 1365; // $s2
    // Ð¸Ð·
    // ÐºÐ»Ð°Ð½Ð°
    // $s1
    // Ñ�Ð´Ð°ÐµÑ‚Ñ�Ñ�.
    public static final int YOU_CAN_DELETE_A_GROUP_ONLY_WHEN_YOU_DO_NOT_HAVE_ANY_CONTACT_IN_THAT_GROUP__IN_ORDER_TO_DELETE_A_GROUP_FIRST_TRANSFER_YOUR_CONTACT_S_IN_THAT_GROUP_TO_ANOTHER_GROUP = 1366; // Ð“Ñ€ÑƒÐ¿Ð¿Ñƒ
    // Ð¼Ð¾Ð¶Ð½Ð¾
    // ÑƒÐ´Ð°Ð»Ð¸Ñ‚ÑŒ
    // Ñ‚Ð¾Ð»ÑŒÐºÐ¾
    // Ð¿Ñ€Ð¸
    // Ð¾Ñ‚Ñ�ÑƒÑ‚Ñ�Ñ‚Ð²Ð¸Ð¸
    // Ð²
    // Ð½ÐµÐ¹
    // ÐºÐ¾Ð½Ñ‚Ð°ÐºÑ‚Ð¾Ð².
    // ÐŸÐµÑ€ÐµÐ´
    // ÑƒÐ´Ð°Ð»ÐµÐ½Ð¸ÐµÐ¼
    // Ð³Ñ€ÑƒÐ¿Ð¿Ñ‹
    // Ð¿ÐµÑ€ÐµÐ¼ÐµÑ�Ñ‚Ð¸Ñ‚Ðµ
    // Ð¸Ð¼ÐµÑŽÑ‰Ð¸ÐµÑ�Ñ�
    // Ð²
    // Ð½ÐµÐ¹
    // ÐºÐ¾Ð½Ñ‚Ð°ÐºÑ‚Ñ‹
    // Ð²
    // Ð´Ñ€ÑƒÐ³ÑƒÑŽ.
    public static final int ONLY_MEMBERS_OF_THE_GROUP_ARE_ALLOWED_TO_ADD_RECORDS = 1367; // Ð¢Ð¾Ð»ÑŒÐºÐ¾
    // Ñ‡Ð»ÐµÐ½Ñ‹
    // Ð³Ñ€ÑƒÐ¿Ð¿Ñ‹
    // Ð¼Ð¾Ð³ÑƒÑ‚
    // Ð´Ð¾Ð±Ð°Ð²Ð¸Ñ‚ÑŒ
    // Ð·Ð°Ð¿Ð¸Ñ�Ð¸.
    public static final int THOSE_ITEMS_MAY_NOT_BE_TRIED_ON_SIMULTANEOUSLY = 1368; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¿Ñ€Ð¸Ð¼ÐµÑ€Ð¸Ñ‚ÑŒ
    // Ñ�Ñ‚Ð¸
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ñ‹
    // Ð¾Ð´Ð½Ð¾Ð²Ñ€ÐµÐ¼ÐµÐ½Ð½Ð¾.
    public static final int YOUVE_EXCEEDED_THE_MAXIMUM = 1369; // Ð’Ñ‹ Ð¿Ñ€ÐµÐ²Ñ‹Ñ�Ð¸Ð»Ð¸
    // Ð¼Ð°ÐºÑ�Ð¸Ð¼Ð°Ð»ÑŒÐ½Ð¾
    // Ð²Ð¾Ð·Ð¼Ð¾Ð¶Ð½ÑƒÑŽ
    // Ñ�ÑƒÐ¼Ð¼Ñƒ.
    public static final int YOU_CANNOT_SEND_MAIL_TO_A_GM_SUCH_AS_S1 = 1370; // $c1
    // -
    // Ð˜Ð³Ñ€Ð¾Ð²Ð¾Ð¹
    // Ð¼Ð°Ñ�Ñ‚ÐµÑ€.
    // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ð¾Ñ‚Ð¿Ñ€Ð°Ð²Ð¸Ñ‚ÑŒ
    // ÐµÐ¼Ñƒ
    // Ð¿Ð¸Ñ�ÑŒÐ¼Ð¾.
    public static final int IT_HAS_BEEN_DETERMINED_THAT_YOURE_NOT_ENGAGED_IN_NORMAL_GAMEPLAY_AND_A_RESTRICTION_HAS_BEEN_IMPOSED_UPON_YOU_YOU_MAY_NOT_MOVE_FOR_S1_MINUTES = 1371; // Ð’Ñ‹
    // Ð¿Ð¾Ð´Ð¾Ð·Ñ€ÐµÐ²Ð°ÐµÑ‚ÐµÑ�ÑŒ
    // Ð²
    // Ñ�Ð¾Ð²ÐµÑ€ÑˆÐµÐ½Ð¸Ð¸
    // Ð½ÐµÐ»ÐµÐ³Ð°Ð»ÑŒÐ½Ñ‹Ñ…
    // Ð´ÐµÐ¹Ñ�Ñ‚Ð²Ð¸Ð¹.
    // Ð’
    // Ñ‚ÐµÑ‡ÐµÐ½Ð¸Ðµ
    // $s1
    // Ð¼Ð¸Ð½
    // Ð¿ÐµÑ€ÐµÐ´Ð²Ð¸Ð¶ÐµÐ½Ð¸Ðµ
    // Ð½ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾.
    public static final int YOUR_PUNISHMENT_WILL_CONTINUE_FOR_S1_MINUTES = 1372; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¿ÐµÑ€ÐµÐ´Ð²Ð¸Ð³Ð°Ñ‚ÑŒÑ�Ñ�.
    // ÐžÐ³Ñ€Ð°Ð½Ð¸Ñ‡ÐµÐ½Ð¸Ðµ
    // Ð¿Ñ€Ð¾Ð´Ð»Ð¸Ñ‚Ñ�Ñ�
    // ÐµÑ‰Ðµ
    // $s1
    // Ð¼Ð¸Ð½.
    public static final int S1_HAS_PICKED_UP_S2_THAT_WAS_DROPPED_BY_A_RAID_BOSS = 1373; // $s1
    // Ð¿Ð¾Ð´Ð±Ð¸Ñ€Ð°ÐµÑ‚
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚
    // $s2,
    // Ð²Ñ‹Ð¿Ð°Ð²ÑˆÐ¸Ð¹
    // Ð¸Ð·
    // Ð‘Ð¾Ñ�Ñ�Ð°
    // Ñ€ÐµÐ¹Ð´Ð°.
    public static final int S1_HAS_PICKED_UP_S3_S2_S_THAT_WAS_DROPPED_BY_A_RAID_BOSS = 1374; // $s1
    // Ð¿Ð¾Ð´Ð±Ð¸Ñ€Ð°ÐµÑ‚
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚
    // $s2
    // ($s3
    // ÑˆÑ‚.),
    // Ð²Ñ‹Ð¿Ð°Ð²ÑˆÐ¸Ð¹
    // Ð¸Ð·
    // Ð‘Ð¾Ñ�Ñ�Ð°
    // Ñ€ÐµÐ¹Ð´Ð°.
    public static final int S1_HAS_PICKED_UP__S2_ADENA_THAT_WAS_DROPPED_BY_A_RAID_BOSS = 1375; // $s1
    // Ð¿Ð¾Ð´Ð±Ð¸Ñ€Ð°ÐµÑ‚
    // $s2
    // Ð°Ð´ÐµÐ½,
    // Ð²Ñ‹Ð¿Ð°Ð²ÑˆÐ¸Ðµ
    // Ð¸Ð·
    // Ð‘Ð¾Ñ�Ñ�Ð°
    // Ñ€ÐµÐ¹Ð´Ð°.
    public static final int S1_HAS_PICKED_UP_S2_THAT_WAS_DROPPED_BY_ANOTHER_CHARACTER = 1376; // $c1
    // Ð¿Ð¾Ð´Ð±Ð¸Ñ€Ð°ÐµÑ‚
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚
    // $s2,
    // Ð²Ñ‹Ð¿Ð°Ð²ÑˆÐ¸Ð¹
    // Ð¸Ð·
    // Ð´Ñ€ÑƒÐ³Ð¾Ð³Ð¾
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°.
    public static final int S1_HAS_PICKED_UP_S3_S2_S_THAT_WAS_DROPPED_BY_ANOTHER_CHARACTER = 1377; // $c1
    // Ð¿Ð¾Ð´Ð±Ð¸Ñ€Ð°ÐµÑ‚
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚
    // $s2
    // ($s3ÑˆÑ‚.),
    // Ð²Ñ‹Ð¿Ð°Ð²ÑˆÐ¸Ð¹
    // Ð¸Ð·
    // Ð´Ñ€ÑƒÐ³Ð¾Ð³Ð¾
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°.
    public static final int S1_HAS_PICKED_UP__S3S2_THAT_WAS_DROPPED_BY_ANOTHER_CHARACTER = 1378; // $c1
    // Ð¿Ð¾Ð´Ð±Ð¸Ñ€Ð°ÐµÑ‚
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚
    // +$s3
    // $s2,
    // Ð²Ñ‹Ð¿Ð°Ð²ÑˆÐ¸Ð¹
    // Ð¸Ð·
    // Ð´Ñ€ÑƒÐ³Ð¾Ð³Ð¾
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°.
    public static final int S1_HAS_OBTAINED_S2_ADENA = 1379; // $c1 Ð¿Ð¾Ð»ÑƒÑ‡Ð°ÐµÑ‚ $s2
    // Ð°Ð´ÐµÐ½.
    public static final int YOU_CANT_SUMMON_A_S1_WHILE_ON_THE_BATTLEGROUND = 1380; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð²Ñ‹Ð·Ð²Ð°Ñ‚ÑŒ
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°
    // $s1
    // Ð½Ð°
    // Ð¿Ð¾Ð»Ðµ
    // Ð±Ð¾Ñ�.
    public static final int THE_PARTY_LEADER_HAS_OBTAINED_S2_OF_S1 = 1381; // Ð›Ð¸Ð´ÐµÑ€
    // Ð³Ñ€ÑƒÐ¿Ð¿Ñ‹
    // Ð¿Ð¾Ð»ÑƒÑ‡Ð°ÐµÑ‚:
    // $s1
    // ($s2
    // ÑˆÑ‚.)
    public static final int ARE_YOU_SURE_YOU_WANT_TO_CHOOSE_THIS_WEAPON_TO_FULFILL_THE_QUEST_YOU_MUST_BRING_THE_CHOSEN_WEAPON = 1382; // Ð’Ñ‹
    // Ñ…Ð¾Ñ‚Ð¸Ñ‚Ðµ
    // Ð²Ñ‹Ð±Ñ€Ð°Ñ‚ÑŒ
    // Ñ�Ñ‚Ð¾
    // Ð¾Ñ€ÑƒÐ¶Ð¸Ðµ?
    // Ð§Ñ‚Ð¾Ð±Ñ‹
    // Ð·Ð°Ð²ÐµÑ€ÑˆÐ¸Ñ‚ÑŒ
    // ÐºÐ²ÐµÑ�Ñ‚,
    // Ð²Ñ‹
    // Ð´Ð¾Ð»Ð¶Ð½Ñ‹
    // Ð¿Ñ€Ð¸Ð½ÐµÑ�Ñ‚Ð¸
    // Ð²Ñ‹Ð±Ñ€Ð°Ð½Ð½Ð¾Ðµ
    // Ð¾Ñ€ÑƒÐ¶Ð¸Ðµ.
    public static final int ARE_YOU_SURE_YOU_WANT_TO_EXCHANGE = 1383; // Ð’Ñ‹
    // Ñ…Ð¾Ñ‚Ð¸Ñ‚Ðµ
    // Ð¿Ñ€Ð¾Ð¸Ð·Ð²ÐµÑ�Ñ‚Ð¸
    // Ð¾Ð±Ð¼ÐµÐ½?
    public static final int S1_HAS_BECOME_A_PARTY_LEADER = 1384; // $c1 Ñ‚ÐµÐ¿ÐµÑ€ÑŒ
    // Ð»Ð¸Ð´ÐµÑ€
    // Ð³Ñ€ÑƒÐ¿Ð¿Ñ‹.
    public static final int YOU_ARE_NOT_ALLOWED_TO_DISMOUNT_AT_THIS_LOCATION = 1385; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ñ�Ð¿ÐµÑˆÐ¸Ñ‚ÑŒÑ�Ñ�
    // Ð²
    // Ñ�Ñ‚Ð¾Ð¼
    // Ð¼ÐµÑ�Ñ‚Ðµ.
    public static final int HOLD_STATE_HAS_BEEN_LIFTED = 1386; // ÐžÐ³Ñ€Ð°Ð½Ð¸Ñ‡ÐµÐ½Ð¸Ðµ Ð½Ð°
    // Ð¿ÐµÑ€ÐµÐ´Ð²Ð¸Ð¶ÐµÐ½Ð¸Ðµ
    // Ñ�Ð½Ñ�Ñ‚Ð¾.
    public static final int PLEASE_SELECT_THE_ITEM_YOU_WOULD_LIKE_TO_TRY_ON = 1387; // Ð’Ñ‹Ð±ÐµÑ€Ð¸Ñ‚Ðµ
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚,
    // ÐºÐ¾Ñ‚Ð¾Ñ€Ñ‹Ð¹
    // Ñ…Ð¾Ñ‚Ð¸Ñ‚Ðµ
    // Ð¿Ñ€Ð¸Ð¼ÐµÑ€Ð¸Ñ‚ÑŒ.
    public static final int A_PARTY_ROOM_HAS_BEEN_CREATED = 1388; // Ð¡Ð¾Ð·Ð´Ð°Ð½Ð°
    // ÐºÐ¾Ð¼Ð½Ð°Ñ‚Ð°
    // Ð³Ñ€ÑƒÐ¿Ð¿Ñ‹.
    public static final int THE_PARTY_ROOMS_INFORMATION_HAS_BEEN_REVISED = 1389; // Ð˜Ð·Ð¼ÐµÐ½ÐµÐ½Ð°
    // Ð¸Ð½Ñ„Ð¾Ñ€Ð¼Ð°Ñ†Ð¸Ñ�
    // Ð¾
    // ÐºÐ¾Ð¼Ð½Ð°Ñ‚Ðµ
    // Ð³Ñ€ÑƒÐ¿Ð¿Ñ‹.
    public static final int YOU_ARE_NOT_ALLOWED_TO_ENTER_THE_PARTY_ROOM = 1390; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð²Ð¾Ð¹Ñ‚Ð¸
    // Ð²
    // ÐºÐ¾Ð¼Ð½Ð°Ñ‚Ñƒ
    // Ð³Ñ€ÑƒÐ¿Ð¿Ñ‹.
    public static final int YOU_HAVE_EXITED_FROM_THE_PARTY_ROOM = 1391; // Ð’Ñ‹
    // Ð²Ñ‹ÑˆÐ»Ð¸
    // Ð¸Ð·
    // ÐºÐ¾Ð¼Ð½Ð°Ñ‚Ñ‹
    // Ð³Ñ€ÑƒÐ¿Ð¿Ñ‹.
    public static final int S1_HAS_LEFT_THE_PARTY_ROOM = 1392; // $c1 Ð²Ñ‹Ñ…Ð¾Ð´Ð¸Ñ‚ Ð¸Ð·
    // ÐºÐ¾Ð¼Ð½Ð°Ñ‚Ñ‹
    // Ð³Ñ€ÑƒÐ¿Ð¿Ñ‹.
    public static final int YOU_HAVE_BEEN_OUSTED_FROM_THE_PARTY_ROOM = 1393; // Ð’Ñ‹
    // Ð±Ñ‹Ð»Ð¸
    // Ð¸Ð·Ð³Ð½Ð°Ð½Ñ‹
    // Ð¸Ð·
    // ÐºÐ¾Ð¼Ð½Ð°Ñ‚Ñ‹
    // Ð³Ñ€ÑƒÐ¿Ð¿Ñ‹.
    public static final int S1_HAS_BEEN_OUSTED_FROM_THE_PARTY_ROOM = 1394; // $c1
    // Ð¸Ð·Ð³Ð¾Ð½Ñ�ÐµÑ‚Ñ�Ñ�
    // Ð¸Ð·
    // ÐºÐ¾Ð¼Ð½Ð°Ñ‚Ñ‹
    // Ð³Ñ€ÑƒÐ¿Ð¿Ñ‹.
    public static final int THE_PARTY_ROOM_HAS_BEEN_DISBANDED = 1395; // ÐšÐ¾Ð¼Ð½Ð°Ñ‚Ð°
    // Ð³Ñ€ÑƒÐ¿Ð¿Ñ‹
    // Ð±Ñ‹Ð»Ð°
    // Ð·Ð°ÐºÑ€Ñ‹Ñ‚Ð°.
    public static final int THE_LIST_OF_PARTY_ROOMS_CAN_BE_VIEWED_BY_A_PERSON_WHO_HAS_NOT_JOINED_A_PARTY_OR_WHO_IS_A_PARTY_LEADER = 1396; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð²Ñ…Ð¾Ð´Ð¸Ñ‚Ðµ
    // Ð²
    // Ð³Ñ€ÑƒÐ¿Ð¿Ñƒ,
    // Ð¸Ð»Ð¸
    // Ð´Ð¾Ñ�Ñ‚ÑƒÐ¿Ð¾Ð¼
    // Ðº
    // Ñ�Ð¿Ð¸Ñ�ÐºÑƒ
    // ÐºÐ¾Ð¼Ð½Ð°Ñ‚Ñ‹
    // Ð³Ñ€ÑƒÐ¿Ð¿Ñ‹
    // Ð¾Ð±Ð»Ð°Ð´Ð°ÐµÑ‚
    // Ñ‚Ð¾Ð»ÑŒÐºÐ¾
    // ÐµÐµ
    // Ð»Ð¸Ð´ÐµÑ€.
    public static final int THE_LEADER_OF_THE_PARTY_ROOM_HAS_CHANGED = 1397; // Ð“Ð»Ð°Ð²Ð°
    // ÐºÐ¾Ð¼Ð½Ð°Ñ‚Ñ‹
    // Ð³Ñ€ÑƒÐ¿Ð¿Ñ‹
    // Ð±Ñ‹Ð»
    // Ñ�Ð¼ÐµÐ½ÐµÐ½.
    public static final int WE_ARE_RECRUITING_PARTY_MEMBERS = 1398; // ÐœÑ‹ Ð¸Ñ‰ÐµÐ¼
    // Ñ‡Ð»ÐµÐ½Ð¾Ð²
    // Ð³Ñ€ÑƒÐ¿Ð¿Ñ‹.
    public static final int ONLY_A_PARTY_LEADER_CAN_TRANSFER_ONES_RIGHTS_TO_ANOTHER_PLAYER = 1399; // Ð¢Ð¾Ð»ÑŒÐºÐ¾
    // Ð»Ð¸Ð´ÐµÑ€
    // Ð³Ñ€ÑƒÐ¿Ð¿Ñ‹
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð¿ÐµÑ€ÐµÐ´Ð°Ñ‚ÑŒ
    // Ð¿Ñ€Ð°Ð²Ð¾
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð¸Ð¼
    // Ð´Ñ€ÑƒÐ³Ð¾Ð¼Ñƒ
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ñƒ.
    public static final int PLEASE_SELECT_THE_PERSON_YOU_WISH_TO_MAKE_THE_PARTY_LEADER = 1400; // Ð’Ñ‹Ð±ÐµÑ€Ð¸Ñ‚Ðµ
    // Ñ‡Ð»ÐµÐ½Ð°
    // Ð³Ñ€ÑƒÐ¿Ð¿Ñ‹,
    // ÐºÐ¾Ñ‚Ð¾Ñ€Ð¾Ð¼Ñƒ
    // Ñ…Ð¾Ñ‚Ð¸Ñ‚Ðµ
    // Ð¿ÐµÑ€ÐµÐ´Ð°Ñ‚ÑŒ
    // Ð·Ð²Ð°Ð½Ð¸Ðµ
    // Ð»Ð¸Ð´ÐµÑ€Ð°
    // Ð³Ñ€ÑƒÐ¿Ð¿Ñ‹.
    public static final int YOU_CANNOT_TRANSFER_RIGHTS_TO_YOURSELF = 1401; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¿ÐµÑ€ÐµÐ´Ð°Ñ‚ÑŒ
    // Ð·Ð²Ð°Ð½Ð¸Ðµ
    // Ð»Ð¸Ð´ÐµÑ€Ð°
    // Ð³Ñ€ÑƒÐ¿Ð¿Ñ‹
    // Ñ�Ð°Ð¼Ð¾Ð¼Ñƒ
    // Ñ�ÐµÐ±Ðµ.
    public static final int YOU_CAN_TRANSFER_RIGHTS_ONLY_TO_ANOTHER_PARTY_MEMBER = 1402; // Ð’Ñ‹
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¿ÐµÑ€ÐµÐ´Ð°Ñ‚ÑŒ
    // Ð·Ð²Ð°Ð½Ð¸Ðµ
    // Ð»Ð¸Ð´ÐµÑ€Ð°
    // Ð³Ñ€ÑƒÐ¿Ð¿Ñ‹
    // Ñ‚Ð¾Ð»ÑŒÐºÐ¾
    // Ñ‡Ð»ÐµÐ½Ñƒ
    // Ñ�Ñ‚Ð¾Ð¹
    // Ð³Ñ€ÑƒÐ¿Ð¿Ñ‹.
    public static final int YOU_HAVE_FAILED_TO_TRANSFER_THE_PARTY_LEADER_RIGHTS = 1403; // ÐžÑˆÐ¸Ð±ÐºÐ°
    // Ð¿Ñ€Ð¸
    // Ð¿ÐµÑ€ÐµÐ´Ð°Ñ‡Ðµ
    // Ð·Ð²Ð°Ð½Ð¸Ñ�
    // Ð»Ð¸Ð´ÐµÑ€Ð°
    // Ð³Ñ€ÑƒÐ¿Ð¿Ñ‹.
    public static final int THE_OWNER_OF_THE_PRIVATE_MANUFACTURING_STORE_HAS_CHANGED_THE_PRICE_FOR_CREATING_THIS_ITEM__PLEASE_CHECK_THE_NEW_PRICE_BEFORE_TRYING_AGAIN = 1404; // Ð¥Ð¾Ð·Ñ�Ð¸Ð½
    // Ð¼Ð°Ñ�Ñ‚ÐµÑ€Ñ�ÐºÐ¾Ð¹
    // Ð¸Ð·Ð¼ÐµÐ½Ð¸Ð»
    // Ñ†ÐµÐ½Ñƒ
    // Ð¸Ð·Ð³Ð¾Ñ‚Ð¾Ð²Ð»ÐµÐ½Ð¸Ñ�.
    // ÐŸÑ€Ð¾Ð²ÐµÑ€ÑŒÑ‚Ðµ
    // Ñ†ÐµÐ½Ñƒ
    // Ð¸
    // Ð¿Ð¾Ð²Ñ‚Ð¾Ñ€Ð¸Ñ‚Ðµ
    // Ð¿Ð¾Ð¿Ñ‹Ñ‚ÐºÑƒ.
    public static final int S1_CPS_WILL_BE_RESTORED = 1405; // $s1: CP
    // Ð²Ð¾Ñ�Ñ�Ñ‚Ð°Ð½Ð¾Ð²Ð»ÐµÐ½Ñ‹.
    public static final int S1_WILL_RESTORE_S2S_CP = 1406; // $s2: CP
    // Ð²Ð¾Ñ�Ñ�Ñ‚Ð°Ð½Ð¾Ð²Ð»ÐµÐ½Ñ‹
    // ($c1).
    public static final int YOU_ARE_USING_A_COMPUTER_THAT_DOES_NOT_ALLOW_YOU_TO_LOG_IN_WITH_TWO_ACCOUNTS_AT_THE_SAME_TIME = 1407; // Ð’Ñ‹
    // Ð¿Ð¾Ð»ÑŒÐ·ÑƒÐµÑ‚ÐµÑ�ÑŒ
    // ÐºÐ¾Ð¼Ð¿ÑŒÑŽÑ‚ÐµÑ€Ð¾Ð¼,
    // Ð´Ð²Ð¾Ð¹Ð½Ð¾Ð¹
    // Ð²Ñ…Ð¾Ð´
    // Ñ�
    // ÐºÐ¾Ñ‚Ð¾Ñ€Ð¾Ð³Ð¾
    // Ð·Ð°Ð¿Ñ€ÐµÑ‰ÐµÐ½.
    public static final int YOUR_PREPAID_REMAINING_USAGE_TIME_IS_S1_HOURS_AND_S2_MINUTES__YOU_HAVE_S3_PAID_RESERVATIONS_LEFT = 1408; // ÐŸÑ€ÐµÐ´Ð¾Ð¿Ð»Ð°Ñ‡ÐµÐ½Ð½Ð¾Ðµ
    // Ð²Ñ€ÐµÐ¼Ñ�:
    // $s1Ñ‡
    // $s2Ð¼Ð¸Ð½.
    // ÐžÑ�Ñ‚Ð°Ð»Ð¾Ñ�ÑŒ
    // Ð¾Ð¿Ð»Ð°Ñ‡ÐµÐ½Ð½Ñ‹Ñ…
    // Ñ‡ÐµÐºÐ¾Ð²:
    // $s3.
    public static final int YOUR_PREPAID_USAGE_TIME_HAS_EXPIRED_YOUR_NEW_PREPAID_RESERVATION_WILL_BE_USED_THE_REMAINING_USAGE_TIME_IS_S1_HOURS_AND_S2_MINUTES = 1409; // ÐŸÑ€ÐµÐ´Ð¾Ð¿Ð»Ð°Ñ‡ÐµÐ½Ð½Ð¾Ðµ
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ð·Ð°ÐºÐ¾Ð½Ñ‡Ð¸Ð»Ð¾Ñ�ÑŒ,
    // Ð¸
    // Ð½Ð°Ñ‡Ð¸Ð½Ð°ÐµÑ‚Ñ�Ñ�
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ð½Ð¸Ðµ
    // Ð¿Ñ€ÐµÐ´Ð¾Ð¿Ð»Ð°Ñ‡ÐµÐ½Ð½Ñ‹Ñ…
    // Ñ‡ÐµÐºÐ¾Ð².
    // ÐžÑ�Ñ‚Ð°Ð²ÑˆÐµÐµÑ�Ñ�
    // Ð²Ñ€ÐµÐ¼Ñ�:
    // $s1
    // Ñ‡
    // $s2
    // Ð¼Ð¸Ð½.
    public static final int YOUR_PREPAID_USAGE_TIME_HAS_EXPIRED_YOU_DO_NOT_HAVE_ANY_MORE_PREPAID_RESERVATIONS_LEFT = 1410; // ÐŸÑ€ÐµÐ´Ð¾Ð¿Ð»Ð°Ñ‡ÐµÐ½Ð½Ð¾Ðµ
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ð·Ð°ÐºÐ¾Ð½Ñ‡Ð¸Ð»Ð¾Ñ�ÑŒ.
    // ÐŸÑ€ÐµÐ´Ð¾Ð¿Ð»Ð°Ñ‡ÐµÐ½Ð½Ñ‹Ñ…
    // Ñ‡ÐµÐºÐ¾Ð²
    // Ð½Ðµ
    // Ð¾Ñ�Ñ‚Ð°Ð»Ð¾Ñ�ÑŒ.
    public static final int THE_NUMBER_OF_YOUR_PREPAID_RESERVATIONS_HAS_CHANGED = 1411; // Ð˜Ð·Ð¼ÐµÐ½ÐµÐ½Ð¾
    // ÐºÐ¾Ð»Ð¸Ñ‡ÐµÑ�Ñ‚Ð²Ð¾
    // Ð¿Ñ€ÐµÐ´Ð¾Ð¿Ð»Ð°Ñ‡ÐµÐ½Ð½Ñ‹Ñ…
    // Ñ‡ÐµÐºÐ¾Ð².
    public static final int YOUR_PREPAID_USAGE_TIME_HAS_S1_MINUTES_LEFT = 1412; // ÐžÑ�Ñ‚Ð°Ð»Ð¾Ñ�ÑŒ
    // Ð¿Ñ€ÐµÐ´Ð¾Ð¿Ð»Ð°Ñ‡ÐµÐ½Ð½Ð¾Ð³Ð¾
    // Ð²Ñ€ÐµÐ¼ÐµÐ½Ð¸:
    // $s1
    // Ð¼Ð¸Ð½.
    public static final int SINCE_YOU_DO_NOT_MEET_THE_REQUIREMENTS_YOU_ARE_NOT_ALLOWED_TO_ENTER_THE_PARTY_ROOM = 1413; // Ð’Ñ‹
    // Ð½Ðµ
    // Ñ�Ð¾Ð¾Ñ‚Ð²ÐµÑ‚Ñ�Ñ‚Ð²ÑƒÐµÑ‚Ðµ
    // Ñ‚Ñ€ÐµÐ±Ð¾Ð²Ð°Ð½Ð¸Ñ�Ð¼
    // Ð´Ð»Ñ�
    // Ð²Ñ…Ð¾Ð´Ð°
    // Ð²
    // ÐºÐ¾Ð¼Ð½Ð°Ñ‚Ñƒ
    // Ð³Ñ€ÑƒÐ¿Ð¿Ñ‹.
    public static final int THE_WIDTH_AND_LENGTH_SHOULD_BE_100_OR_MORE_GRIDS_AND_LESS_THAN_5000_GRIDS_RESPECTIVELY = 1414; // Ð¨Ð¸Ñ€Ð¸Ð½Ð°
    // Ð¸
    // Ð´Ð»Ð¸Ð½Ð°
    // Ð´Ð¾Ð»Ð¶Ð½Ð°
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð±Ð¾Ð»ÐµÐµ
    // 100,
    // Ð½Ð¾
    // Ð¼ÐµÐ½ÐµÐµ
    // 5000.
    public static final int THE_COMMAND_FILE_IS_NOT_SET = 1415; // ÐšÐ¾Ð¼Ð°Ð½Ð´Ð½Ñ‹Ð¹
    // Ñ„Ð°Ð¹Ð» Ð½Ðµ Ð±Ñ‹Ð»
    // Ð½Ð°Ñ�Ñ‚Ñ€Ð¾ÐµÐ½.
    public static final int THE_PARTY_REPRESENTATIVE_OF_TEAM_1_HAS_NOT_BEEN_SELECTED = 1416; // ÐŸÑ€ÐµÐ´Ñ�Ñ‚Ð°Ð²Ð¸Ñ‚ÐµÐ»ÑŒ
    // 1-Ð¹
    // ÐºÐ¾Ð¼Ð°Ð½Ð´Ñ‹
    // Ð²
    // Ð³Ñ€ÑƒÐ¿Ð¿Ðµ
    // Ð½Ðµ
    // Ð²Ñ‹Ð±Ñ€Ð°Ð½.
    public static final int THE_PARTY_REPRESENTATIVE_OF_TEAM_2_HAS_NOT_BEEN_SELECTED = 1417; // ÐŸÑ€ÐµÐ´Ñ�Ñ‚Ð°Ð²Ð¸Ñ‚ÐµÐ»ÑŒ
    // 2-Ð¹
    // ÐºÐ¾Ð¼Ð°Ð½Ð´Ñ‹
    // Ð²
    // Ð³Ñ€ÑƒÐ¿Ð¿Ðµ
    // Ð½Ðµ
    // Ð²Ñ‹Ð±Ñ€Ð°Ð½.
    public static final int THE_NAME_OF_TEAM_1_HAS_NOT_YET_BEEN_CHOSEN = 1418; // Ð�Ð°Ð·Ð²Ð°Ð½Ð¸Ðµ
    // 1-Ð¹
    // ÐºÐ¾Ð¼Ð°Ð½Ð´Ñ‹
    // Ð½Ðµ
    // Ð²Ñ‹Ð±Ñ€Ð°Ð½Ð¾.
    public static final int THE_NAME_OF_TEAM_2_HAS_NOT_YET_BEEN_CHOSEN = 1419; // Ð�Ð°Ð·Ð²Ð°Ð½Ð¸Ðµ
    // 2-Ð¹
    // ÐºÐ¾Ð¼Ð°Ð½Ð´Ñ‹
    // Ð½Ðµ
    // Ð²Ñ‹Ð±Ñ€Ð°Ð½Ð¾.
    public static final int THE_NAME_OF_TEAM_1_AND_THE_NAME_OF_TEAM_2_ARE_IDENTICAL = 1420; // Ð�Ð°Ð·Ð²Ð°Ð½Ð¸Ñ�
    // 1-Ð¹
    // Ð¸
    // 2-Ð¹
    // ÐºÐ¾Ð¼Ð°Ð½Ð´
    // Ð¾Ð´Ð¸Ð½Ð°ÐºÐ¾Ð²Ñ‹.
    public static final int THE_RACE_SETUP_FILE_HAS_NOT_BEEN_DESIGNATED = 1421; // Ð�Ðµ
    // Ð²Ñ‹Ð±Ñ€Ð°Ð½
    // Ñ„Ð°Ð¹Ð»
    // Ð½Ð°Ñ�Ñ‚Ñ€Ð¾Ð¹ÐºÐ¸
    // Ñ�Ð¾Ñ€ÐµÐ²Ð½Ð¾Ð²Ð°Ð½Ð¸Ñ�.
    public static final int RACE_SETUP_FILE_ERROR__BUFFCNT_IS_NOT_SPECIFIED = 1422; // ÐžÑˆÐ¸Ð±ÐºÐ°
    // Ð²
    // Ñ„Ð°Ð¹Ð»Ðµ
    // Ð½Ð°Ñ�Ñ‚Ñ€Ð¾Ð¹ÐºÐ¸
    // Ñ�Ð¾Ñ€ÐµÐ²Ð½Ð¾Ð²Ð°Ð½Ð¸Ñ�
    // -
    // Ð½Ðµ
    // Ð²Ñ‹Ð±Ñ€Ð°Ð½
    // BuffCnt.
    public static final int RACE_SETUP_FILE_ERROR__BUFFIDS1_IS_NOT_SPECIFIED = 1423; // ÐžÑˆÐ¸Ð±ÐºÐ°
    // Ð²
    // Ñ„Ð°Ð¹Ð»Ðµ
    // Ð½Ð°Ñ�Ñ‚Ñ€Ð¾Ð¹ÐºÐ¸
    // Ñ�Ð¾Ñ€ÐµÐ²Ð½Ð¾Ð²Ð°Ð½Ð¸Ñ�
    // -
    // Ð½Ðµ
    // Ð²Ñ‹Ð±Ñ€Ð°Ð½
    // BuffID$s1.
    public static final int RACE_SETUP_FILE_ERROR__BUFFLVS1_IS_NOT_SPECIFIED = 1424; // ÐžÑˆÐ¸Ð±ÐºÐ°
    // Ð²
    // Ñ„Ð°Ð¹Ð»Ðµ
    // Ð½Ð°Ñ�Ñ‚Ñ€Ð¾Ð¹ÐºÐ¸
    // Ñ�Ð¾Ñ€ÐµÐ²Ð½Ð¾Ð²Ð°Ð½Ð¸Ñ�
    // -
    // Ð½Ðµ
    // Ð²Ñ‹Ð±Ñ€Ð°Ð½
    // BuffLv$s1.
    public static final int RACE_SETUP_FILE_ERROR__DEFAULTALLOW_IS_NOT_SPECIFIED = 1425; // ÐžÑˆÐ¸Ð±ÐºÐ°
    // Ð²
    // Ñ„Ð°Ð¹Ð»Ðµ
    // Ð½Ð°Ñ�Ñ‚Ñ€Ð¾Ð¹ÐºÐ¸
    // Ñ�Ð¾Ñ€ÐµÐ²Ð½Ð¾Ð²Ð°Ð½Ð¸Ñ�
    // -
    // Ð½Ðµ
    // Ð²Ñ‹Ð±Ñ€Ð°Ð½
    // DefaultAllow.
    public static final int RACE_SETUP_FILE_ERROR__EXPSKILLCNT_IS_NOT_SPECIFIED = 1426; // ÐžÑˆÐ¸Ð±ÐºÐ°
    // Ð²
    // Ñ„Ð°Ð¹Ð»Ðµ
    // Ð½Ð°Ñ�Ñ‚Ñ€Ð¾Ð¹ÐºÐ¸
    // Ñ�Ð¾Ñ€ÐµÐ²Ð½Ð¾Ð²Ð°Ð½Ð¸Ñ�
    // -
    // Ð½Ðµ
    // Ð²Ñ‹Ð±Ñ€Ð°Ð½
    // ExpSkillCnt.
    public static final int RACE_SETUP_FILE_ERROR__EXPSKILLIDS1_IS_NOT_SPECIFIED = 1427; // ÐžÑˆÐ¸Ð±ÐºÐ°
    // Ð²
    // Ñ„Ð°Ð¹Ð»Ðµ
    // Ð½Ð°Ñ�Ñ‚Ñ€Ð¾Ð¹ÐºÐ¸
    // Ñ�Ð¾Ñ€ÐµÐ²Ð½Ð¾Ð²Ð°Ð½Ð¸Ñ�
    // -
    // Ð½Ðµ
    // Ð²Ñ‹Ð±Ñ€Ð°Ð½
    // ExpSkillID$s1.
    public static final int RACE_SETUP_FILE_ERROR__EXPITEMCNT_IS_NOT_SPECIFIED = 1428; // ÐžÑˆÐ¸Ð±ÐºÐ°
    // Ð²
    // Ñ„Ð°Ð¹Ð»Ðµ
    // Ð½Ð°Ñ�Ñ‚Ñ€Ð¾Ð¹ÐºÐ¸
    // Ñ�Ð¾Ñ€ÐµÐ²Ð½Ð¾Ð²Ð°Ð½Ð¸Ñ�
    // -
    // Ð½Ðµ
    // Ð²Ñ‹Ð±Ñ€Ð°Ð½
    // ExpItemCnt.
    public static final int RACE_SETUP_FILE_ERROR__EXPITEMIDS1_IS_NOT_SPECIFIED = 1429; // ÐžÑˆÐ¸Ð±ÐºÐ°
    // Ð²
    // Ñ„Ð°Ð¹Ð»Ðµ
    // Ð½Ð°Ñ�Ñ‚Ñ€Ð¾Ð¹ÐºÐ¸
    // Ñ�Ð¾Ñ€ÐµÐ²Ð½Ð¾Ð²Ð°Ð½Ð¸Ñ�
    // -
    // Ð½Ðµ
    // Ð²Ñ‹Ð±Ñ€Ð°Ð½
    // ExpItemId$s1.
    public static final int RACE_SETUP_FILE_ERROR__TELEPORTDELAY_IS_NOT_SPECIFIED = 1430; // ÐžÑˆÐ¸Ð±ÐºÐ°
    // Ð²
    // Ñ„Ð°Ð¹Ð»Ðµ
    // Ð½Ð°Ñ�Ñ‚Ñ€Ð¾Ð¹ÐºÐ¸
    // Ñ�Ð¾Ñ€ÐµÐ²Ð½Ð¾Ð²Ð°Ð½Ð¸Ñ�
    // -
    // Ð½Ðµ
    // Ð²Ñ‹Ð±Ñ€Ð°Ð½
    // TeleportDelay.
    public static final int THE_RACE_WILL_BE_STOPPED_TEMPORARILY = 1431; // Ð¡Ð¾Ñ€ÐµÐ²Ð½Ð¾Ð²Ð°Ð½Ð¸Ñ�
    // Ð±ÑƒÐ´ÑƒÑ‚
    // Ð²Ñ€ÐµÐ¼ÐµÐ½Ð½Ð¾
    // Ð¾Ñ�Ñ‚Ð°Ð½Ð¾Ð²Ð»ÐµÐ½Ñ‹.
    public static final int YOUR_OPPONENT_IS_CURRENTLY_IN_A_PETRIFIED_STATE = 1432; // Ð’Ð°Ñˆ
    // Ð¿Ñ€Ð¾Ñ‚Ð¸Ð²Ð½Ð¸Ðº
    // Ð¾ÐºÐ°Ð¼ÐµÐ½ÐµÐ».
    public static final int THE_USE_OF_S1_WILL_NOW_BE_AUTOMATED = 1433; // $s1
    // Ð±ÑƒÐ´ÐµÑ‚
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒÑ�Ñ�
    // Ð°Ð²Ñ‚Ð¾Ð¼Ð°Ñ‚Ð¸Ñ‡ÐµÑ�ÐºÐ¸
    public static final int THE_AUTOMATIC_USE_OF_S1_WILL_NOW_BE_CANCELLED = 1434; // $s1:
    // Ð°Ð²Ñ‚Ð¾Ð¼Ð°Ñ‚Ð¸Ñ‡ÐµÑ�ÐºÐ¾Ðµ
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ð½Ð¸Ðµ
    // Ð¾Ñ‚Ð¼ÐµÐ½ÐµÐ½Ð¾.
    public static final int DUE_TO_INSUFFICIENT_S1_THE_AUTOMATIC_USE_FUNCTION_HAS_BEEN_CANCELLED = 1435; // $s1:
    // Ð½ÐµÑ…Ð²Ð°Ñ‚ÐºÐ°
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ð°.
    // Ð�Ð²Ñ‚Ð¾Ð¼Ð°Ñ‚Ð¸Ñ‡ÐµÑ�ÐºÐ¾Ðµ
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ð½Ð¸Ðµ
    // Ð¾Ñ‚Ð¼ÐµÐ½ÐµÐ½Ð¾.
    public static final int DUE_TO_INSUFFICIENT_S1_THE_AUTOMATIC_USE_FUNCTION_CANNOT_BE_ACTIVATED = 1436; // $s1:
    // Ð½ÐµÑ…Ð²Ð°Ñ‚ÐºÐ°
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ð°.
    // Ð�Ð²Ñ‚Ð¾Ð¼Ð°Ñ‚Ð¸Ñ‡ÐµÑ�ÐºÐ¾Ðµ
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ð½Ð¸Ðµ
    // Ð½ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾.
    public static final int PLAYERS_ARE_NO_LONGER_ALLOWED_TO_PLACE_DICE_DICE_CANNOT_BE_PURCHASED_FROM_A_VILLAGE_STORE_ANY_MORE_HOWEVER_YOU_CAN_STILL_SELL_THEM_TO_A_STORE_IN_A_VILLAGE = 1437; // Ð’Ñ‹
    // Ð±Ð¾Ð»ÑŒÑˆÐµ
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¸Ð³Ñ€Ð°Ñ‚ÑŒ
    // Ð²
    // ÐºÐ¾Ñ�Ñ‚Ð¸,
    // Ð°
    // Ñ‚Ð°ÐºÐ¶Ðµ
    // Ð¿Ñ€Ð¸Ð¾Ð±Ñ€ÐµÑ‚Ð°Ñ‚ÑŒ
    // Ð¸Ñ…
    // Ð²
    // Ð¼Ð°Ð³Ð°Ð·Ð¸Ð½Ðµ
    // Ð´ÐµÑ€ÐµÐ²Ð½Ð¸.
    // Ð�Ð¾
    // Ð¿Ñ€Ð¾Ð´Ð°Ð¶Ð°
    // Ð²
    // Ð¼Ð°Ð³Ð°Ð·Ð¸Ð½
    // Ð²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð°.

    public static final int THERE_IS_NO_SKILL_THAT_ENABLES_ENCHANT = 1438; // ÐžÑ‚Ñ�ÑƒÑ‚Ñ�Ñ‚Ð²ÑƒÐµÑ‚
    // ÑƒÐ¼ÐµÐ½Ð¸Ðµ,
    // Ð¿Ð¾Ð·Ð²Ð¾Ð»Ñ�ÑŽÑ‰ÐµÐµ
    // ÑƒÐ»ÑƒÑ‡ÑˆÐµÐ½Ð¸Ðµ.
    public static final int ITEMS_REQUIRED_FOR_SKILL_ENCHANT_ARE_INSUFFICIENT = 1439; // Ð£
    // Ð’Ð°Ñ�
    // Ð½ÐµÑ‚
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ð¾Ð²,
    // Ð½ÐµÐ¾Ð±Ñ…Ð¾Ð´Ð¸Ð¼Ñ‹Ñ…
    // Ð´Ð»Ñ�
    // ÑƒÐ»ÑƒÑ‡ÑˆÐµÐ½Ð¸Ñ�
    // ÑƒÐ¼ÐµÐ½Ð¸Ñ�.
    public static final int SUCCEEDED_IN_ENCHANTING_SKILL_S1 = 1440; // Ð£Ð¼ÐµÐ½Ð¸Ðµ
    // $s1 Ð±Ñ‹Ð»Ð¾
    // ÑƒÐ»ÑƒÑ‡ÑˆÐµÐ½Ð¾.
    public static final int FAILED_IN_ENCHANTING_SKILL_S1 = 1441; // ÐžÑˆÐ¸Ð±ÐºÐ° Ð¿Ñ€Ð¸
    // ÑƒÐ»ÑƒÑ‡ÑˆÐµÐ½Ð¸Ð¸
    // ÑƒÐ¼ÐµÐ½Ð¸Ñ�.
    // Ð£Ð»ÑƒÑ‡ÑˆÐ°ÐµÐ¼Ð¾Ðµ
    // ÑƒÐ¼ÐµÐ½Ð¸Ðµ
    // Ð±ÑƒÐ´ÐµÑ‚
    // Ð¾Ð±Ð½Ð¾Ð²Ð»ÐµÐ½Ð¾.
    public static final int SP_REQUIRED_FOR_SKILL_ENCHANT_IS_INSUFFICIENT = 1443; // Ð�ÐµÐ´Ð¾Ñ�Ñ‚Ð°Ñ‚Ð¾Ñ‡Ð½Ð¾
    // SP
    // Ð´Ð»Ñ�
    // ÑƒÐ»ÑƒÑ‡ÑˆÐµÐ½Ð¸Ñ�
    // ÑƒÐ¼ÐµÐ½Ð¸Ñ�.
    public static final int EXP_REQUIRED_FOR_SKILL_ENCHANT_IS_INSUFFICIENT = 1444; // Ð�ÐµÐ´Ð¾Ñ�Ñ‚Ð°Ñ‚Ð¾Ñ‡Ð½Ð¾
    // Ð¾Ð¿Ñ‹Ñ‚Ð°
    // Ð´Ð»Ñ�
    // ÑƒÐ»ÑƒÑ‡ÑˆÐµÐ½Ð¸Ñ�
    // ÑƒÐ¼ÐµÐ½Ð¸Ñ�.
    public static final int YOU_DO_NOT_HAVE_ALL_OF_THE_ITEMS_NEEDED_TO_UNTRAIN_THE_ENCHANT_SKILL = 2068; // Ð£
    // Ð²Ð°Ñ�
    // Ð½ÐµÑ‚
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ð¾Ð²,
    // Ð½ÐµÐ¾Ð±Ñ…Ð¾Ð´Ð¸Ð¼Ñ‹Ñ…
    // Ð´Ð»Ñ�
    // ÑƒÐ»ÑƒÑ‡ÑˆÐµÐ½Ð¸Ñ�
    // ÑƒÐ¼ÐµÐ½Ð¸Ñ�.

    public static final int Untrain_of_enchant_skill_was_successful_Current_level_of_enchant_skill_S1_has_been_decreased_by_1 = 2069; // Ð Ð°Ð·ÑƒÑ‡Ð¸Ð²Ð°Ð½Ð¸Ðµ
    // ÑƒÐ¼ÐµÐ½Ð¸Ñ�
    // ÑƒÐ»ÑƒÑ‡ÑˆÐµÐ½Ð¸Ñ�
    // ÑƒÑ�Ð¿ÐµÑˆÐ½Ð¾
    // Ð·Ð°Ð²ÐµÑ€ÑˆÐµÐ½Ð¾.
    // Ð¢ÐµÐºÑƒÑ‰Ð¸Ð¹
    // ÑƒÑ€Ð¾Ð²ÐµÐ½ÑŒ
    // ÑƒÐ¼ÐµÐ½Ð¸Ñ�
    // $s1
    // Ð±Ñ‹Ð»
    // Ð¿Ð¾Ð½Ð¸Ð¶ÐµÐ½
    // Ð½Ð°
    // 1.
    public static final int Untrain_of_enchant_skill_was_successful_Current_level_of_enchant_skill_S1_became_0_and_enchant_skill_will_be_initialized = 2070; // Ð¡Ð±Ñ€Ð¾Ñ�
    // ÑƒÐ»ÑƒÑ‡ÑˆÐµÐ½Ð¸Ð¹
    // ÑƒÐ¼ÐµÐ½Ð¸Ñ�
    // Ð·Ð°Ð²ÐµÑ€ÑˆÐµÐ½.
    // $s1:
    // Ñ‚ÐµÐºÑƒÑ‰Ð¸Ð¹
    // ÑƒÑ€Ð¾Ð²ÐµÐ½ÑŒ
    // -
    // 0,
    // ÑƒÐ¼ÐµÐ½Ð¸Ðµ
    // Ð²Ð¾Ð·Ð²Ñ€Ð°Ñ‰ÐµÐ½Ð¾
    // Ð²
    // Ð¸Ñ�Ñ…Ð¾Ð´Ð½Ð¾Ðµ
    // Ñ�Ð¾Ñ�Ñ‚Ð¾Ñ�Ð½Ð¸Ðµ.
    public static final int You_do_not_have_all_of_the_items_needed_to_enchant_skill_route_change = 2071; // Ð£
    // Ð’Ð°Ñ�
    // Ð½ÐµÑ‚
    // Ð½ÐµÐ¾Ð±Ñ…Ð¾Ð´Ð¸Ð¼Ñ‹Ñ…
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ð¾Ð²,
    // Ñ‡Ñ‚Ð¾Ð±Ñ‹
    // Ð¸Ð·Ð¼ÐµÐ½Ð¸Ñ‚ÑŒ
    // Ð¿ÑƒÑ‚ÑŒ
    // ÑƒÐ»ÑƒÑ‡ÑˆÐµÐ½Ð¸Ñ�
    // ÑƒÐ¼ÐµÐ½Ð¸Ñ�.
    public static final int Enchant_skill_route_change_was_successful_Lv_of_enchant_skill_S1_has_been_decreased_by_S2 = 2072; // ÐŸÑƒÑ‚ÑŒ
    // ÑƒÐ»ÑƒÑ‡ÑˆÐµÐ½Ð¸Ñ�
    // ÑƒÐ¼ÐµÐ½Ð¸Ñ�
    // Ð±Ñ‹Ð»
    // Ð¸Ð·Ð¼ÐµÐ½ÐµÐ½.
    // Ð£Ñ€Ð¾Ð²ÐµÐ½ÑŒ
    // ÑƒÐ¼ÐµÐ½Ð¸Ñ�
    // $s1
    // Ð±Ñ‹Ð»
    // Ð¿Ð¾Ð½Ð¸Ð¶ÐµÐ½
    // Ð½Ð°
    // $s2.
    public static final int Enchant_skill_route_change_was_successful_Lv_of_enchant_skill_S1_will_remain = 2073; // ÐŸÑƒÑ‚ÑŒ
    // ÑƒÐ»ÑƒÑ‡ÑˆÐµÐ½Ð¸Ñ�
    // ÑƒÐ¼ÐµÐ½Ð¸Ñ�
    // Ð±Ñ‹Ð»
    // Ð¸Ð·Ð¼ÐµÐ½ÐµÐ½.
    // Ð£Ñ€Ð¾Ð²ÐµÐ½ÑŒ
    // ÑƒÐ¼ÐµÐ½Ð¸Ñ�
    // $s1
    // Ð±Ñ‹Ð»
    // Ñ�Ð¾Ñ…Ñ€Ð°Ð½ÐµÐ½.
    public static final int Skill_enchant_failed_Current_level_of_enchant_skill_S1_will_remain_unchanged = 2074; // Ð£Ð»ÑƒÑ‡ÑˆÐµÐ½Ð¸Ðµ
    // Ð½Ðµ
    // ÑƒÐ´Ð°Ð»Ð¾Ñ�ÑŒ.
    // Ð£Ñ€Ð¾Ð²ÐµÐ½ÑŒ
    // ÑƒÐ¼ÐµÐ½Ð¸Ñ�
    // $s1
    // Ð¾Ñ�Ñ‚Ð°Ð»Ñ�Ñ�
    // Ð±ÐµÐ·
    // Ð¸Ð·Ð¼ÐµÐ½ÐµÐ½Ð¸Ð¹.

    public static final int REMAINING_TIME_S1_SECOND = 1442; // ÐžÑ�Ñ‚Ð°Ð»Ð¾Ñ�ÑŒ: $s1
    // Ñ�ÐµÐº.
    public static final int YOUR_PREVIOUS_SUB_CLASS_WILL_BE_DELETED_AND_YOUR_NEW_SUB_CLASS_WILL_START_AT_LEVEL_40__DO_YOU_WISH_TO_PROCEED = 1445; // ÐŸÑ€ÐµÐ´Ñ‹Ð´ÑƒÑ‰Ð¸Ð¹
    // Ð¿Ð¾Ð´ÐºÐ»Ð°Ñ�Ñ�
    // Ð±ÑƒÐ´ÐµÑ‚
    // Ð·Ð°Ð¼ÐµÐ½ÐµÐ½
    // Ð½Ð¾Ð²Ñ‹Ð¼
    // Ð¿Ð¾
    // Ð´Ð¾Ñ�Ñ‚Ð¸Ð¶ÐµÐ½Ð¸Ð¸
    // 40-Ð³Ð¾
    // ÑƒÑ€Ð¾Ð²Ð½Ñ�.
    // ÐŸÑ€Ð¾Ð´Ð¾Ð»Ð¶Ð¸Ñ‚ÑŒ?
    public static final int THE_FERRY_FROM_S1_TO_S2_HAS_BEEN_DELAYED = 1446; // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ,
    // Ð¾Ñ‚Ð±Ñ‹Ð²Ð°ÑŽÑ‰Ð¸Ð¹
    // Ð¸Ð·
    // Ð¿Ð¾Ñ€Ñ‚Ð°
    // $s1
    // Ð²
    // Ð¿Ð¾Ñ€Ñ‚
    // $s2,
    // Ð·Ð°Ð´ÐµÑ€Ð¶Ð¸Ð²Ð°ÐµÑ‚Ñ�Ñ�.
    public static final int OTHER_SKILLS_ARE_NOT_AVAILABLE_WHILE_FISHING = 1447; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð²Ð¾Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒÑ�Ñ�
    // Ð´Ñ€ÑƒÐ³Ð¸Ð¼
    // ÑƒÐ¼ÐµÐ½Ð¸ÐµÐ¼
    // Ð²Ð¾
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ñ€Ñ‹Ð±Ð°Ð»ÐºÐ¸.
    public static final int ONLY_FISHING_SKILLS_ARE_AVAILABLE = 1448; // Ð’Ñ‹
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð²Ð¾Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒÑ�Ñ�
    // Ñ‚Ð¾Ð»ÑŒÐºÐ¾
    // ÑƒÐ¼ÐµÐ½Ð¸ÐµÐ¼
    // Ñ€Ñ‹Ð±Ð°Ð»ÐºÐ¸.
    public static final int SUCCEEDED_IN_GETTING_A_BITE = 1449; // ÐšÐ»ÑŽÐµÑ‚!
    public static final int TIME_IS_UP_SO_THAT_FISH_GOT_AWAY = 1450; // Ð’Ñ€ÐµÐ¼Ñ�
    // Ð¿Ñ€Ð¾ÑˆÐ»Ð¾,
    // Ð¸ Ð’Ñ‹
    // ÑƒÐ¿ÑƒÑ�Ñ‚Ð¸Ð»Ð¸
    // Ñ€Ñ‹Ð±Ñƒ.
    public static final int THE_FISH_GOT_AWAY = 1451; // Ð’Ñ‹ ÑƒÐ¿ÑƒÑ�Ñ‚Ð¸Ð»Ð¸ Ñ€Ñ‹Ð±Ñƒ.
    public static final int BAITS_HAVE_BEEN_LOST_BECAUSE_THE_FISH_GOT_AWAY = 1452; // Ð’Ñ‹
    // ÑƒÐ¿ÑƒÑ�Ñ‚Ð¸Ð»Ð¸
    // Ñ€Ñ‹Ð±Ñƒ
    // Ð¸
    // Ð¿Ð¾Ñ‚ÐµÑ€Ñ�Ð»Ð¸
    // Ð½Ð°Ð¶Ð¸Ð²ÐºÑƒ.
    public static final int FISHING_POLES_ARE_NOT_INSTALLED = 1453; // Ð£ Ð’Ð°Ñ� Ð½ÐµÑ‚
    // ÑƒÐ´Ð¾Ñ‡ÐºÐ¸.
    public static final int BAITS_ARE_NOT_PUT_ON_A_HOOK = 1454; // Ð�Ð°Ð¶Ð¸Ð²ÐºÐ° Ð½Ðµ
    // Ð¿Ñ€Ð¸ÐºÑ€ÐµÐ¿Ð»ÐµÐ½Ð° Ðº
    // ÐºÑ€ÑŽÑ‡ÐºÑƒ.
    public static final int YOU_CANT_FISH_IN_WATER = 1455; // Ð’Ñ‹ Ð½Ðµ Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ñ€Ñ‹Ð±Ð°Ñ‡Ð¸Ñ‚ÑŒ, Ð½Ð°Ñ…Ð¾Ð´Ñ�Ñ�ÑŒ
    // Ð² Ð²Ð¾Ð´Ðµ.
    public static final int YOU_CANT_FISH_WHILE_YOU_ARE_ON_BOARD = 1456; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ñ€Ñ‹Ð±Ð°Ñ‡Ð¸Ñ‚ÑŒ,
    // Ð½Ð°Ñ…Ð¾Ð´Ñ�Ñ�ÑŒ
    // Ð½Ð°
    // ÐºÐ¾Ñ€Ð°Ð±Ð»Ðµ.
    public static final int YOU_CANT_FISH_HERE = 1457; // Ð—Ð´ÐµÑ�ÑŒ Ñ€Ñ‹Ð±Ð°Ñ‡Ð¸Ñ‚ÑŒ Ð½ÐµÐ»ÑŒÐ·Ñ�.
    public static final int CANCELS_FISHING = 1458; // Ð Ñ‹Ð±Ð°Ð»ÐºÐ° Ð¾Ñ‚Ð¼ÐµÐ½ÐµÐ½Ð°.
    public static final int NOT_ENOUGH_BAIT = 1459; // Ð£ Ð’Ð°Ñ� Ð½Ðµ Ñ…Ð²Ð°Ñ‚Ð°ÐµÑ‚ Ð½Ð°Ð¶Ð¸Ð²ÐºÐ¸.
    public static final int ENDS_FISHING = 1460; // Ð Ñ‹Ð±Ð°Ð»ÐºÐ° Ð·Ð°Ð²ÐµÑ€ÑˆÐµÐ½Ð°.
    public static final int STARTS_FISHING = 1461; // Ð Ñ‹Ð±Ð°Ð»ÐºÐ° Ð½Ð°Ñ‡Ð°Ð»Ð°Ñ�ÑŒ.
    public static final int PUMPING_SKILL_IS_AVAILABLE_ONLY_WHILE_FISHING = 1462; // ÐŸÐ¾Ð´Ñ‚Ñ�Ð³Ð¸Ð²Ð°Ñ‚ÑŒ
    // Ð¼Ð¾Ð¶Ð½Ð¾
    // Ñ‚Ð¾Ð»ÑŒÐºÐ¾
    // Ð²Ð¾
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ñ€Ñ‹Ð±Ð°Ð»ÐºÐ¸.
    public static final int REELING_SKILL_IS_AVAILABLE_ONLY_WHILE_FISHING = 1463; // ÐŸÐ¾Ð´Ñ�ÐµÐºÐ°Ñ‚ÑŒ
    // Ð¼Ð¾Ð¶Ð½Ð¾
    // Ñ‚Ð¾Ð»ÑŒÐºÐ¾
    // Ð²Ð¾
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ñ€Ñ‹Ð±Ð°Ð»ÐºÐ¸.
    public static final int FISH_HAS_RESISTED = 1464; // Ð Ñ‹Ð±Ð° Ñ�Ð¾Ñ€Ð²Ð°Ð»Ð°Ñ�ÑŒ Ñ�
    // ÐºÑ€ÑŽÑ‡ÐºÐ°, Ð¸ Ð²Ñ‹ Ð½Ðµ Ñ�Ð¼Ð¾Ð³Ð»Ð¸
    // ÐµÐµ Ð²Ñ‹Ñ‚Ð°Ñ‰Ð¸Ñ‚ÑŒ.
    public static final int PUMPING_IS_SUCCESSFUL_DAMAGE_S1 = 1465; // Ð’Ñ‹
    // ÑƒÑ�Ð¿ÐµÑˆÐ½Ð¾
    // Ð¿Ð¾Ð´Ñ‚Ñ�Ð½ÑƒÐ»Ð¸
    // Ñ€Ñ‹Ð±Ñƒ Ð¸
    // Ð½Ð°Ð½ÐµÑ�Ð»Ð¸
    // ÐµÐ¹ ÑƒÑ€Ð¾Ð½
    // $s1 HP
    public static final int PUMPING_FAILED_DAMAGE_S1 = 1466; // Ð’Ñ‹ Ð½Ðµ Ñ�Ð¼Ð¾Ð³Ð»Ð¸
    // Ð¿Ð¾Ð´Ñ‚Ñ�Ð½ÑƒÑ‚ÑŒ Ñ€Ñ‹Ð±Ñƒ,
    // Ð¸ Ð¾Ð½Ð°
    // Ð²Ð¾Ñ�Ñ�Ñ‚Ð°Ð½Ð¾Ð²Ð¸Ð»Ð° $s1
    // HP
    public static final int REELING_IS_SUCCESSFUL_DAMAGE_S1 = 1467; // Ð’Ñ‹
    // ÑƒÑ�Ð¿ÐµÑˆÐ½Ð¾
    // Ð¿Ð¾Ð´Ñ�ÐµÐºÐ»Ð¸
    // Ñ€Ñ‹Ð±Ñƒ Ð¸
    // Ð½Ð°Ð½ÐµÑ�Ð»Ð¸
    // ÐµÐ¹ ÑƒÑ€Ð¾Ð½
    // $s1 HP
    public static final int REELING_FAILED_DAMAGE_S1 = 1468; // Ð’Ñ‹ Ð½Ðµ Ñ�Ð¼Ð¾Ð³Ð»Ð¸
    // Ð¿Ð¾Ð´Ñ�ÐµÑ‡ÑŒ Ñ€Ñ‹Ð±Ñƒ, Ð¸
    // Ð¾Ð½Ð° Ð²Ð¾Ñ�Ñ�Ñ‚Ð°Ð½Ð¾Ð²Ð¸Ð»Ð°
    // $s1 HP
    public static final int SUCCEEDED_IN_FISHING = 1469; // Ð’Ñ‹ Ñ‡Ñ‚Ð¾-Ñ‚Ð¾ Ð¿Ð¾Ð¹Ð¼Ð°Ð»Ð¸.
    public static final int YOU_CANNOT_DO_THAT_WHILE_FISHING = 1470; // Ð’Ñ‹ Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ñ�Ð´ÐµÐ»Ð°Ñ‚ÑŒ
    // Ñ�Ñ‚Ð¾ Ð²Ð¾
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ñ€Ñ‹Ð±Ð°Ð»ÐºÐ¸.
    public static final int YOU_CANNOT_DO_ANYTHING_ELSE_WHILE_FISHING = 1471; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ñ�Ð´ÐµÐ»Ð°Ñ‚ÑŒ
    // Ñ�Ñ‚Ð¾
    // Ð²Ð¾
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ñ€Ñ‹Ð±Ð°Ð»ÐºÐ¸.
    public static final int YOU_CANT_MAKE_AN_ATTACK_WITH_A_FISHING_POLE = 1472; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð°Ñ‚Ð°ÐºÐ¾Ð²Ð°Ñ‚ÑŒ
    // Ð²Ð¾
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ñ€Ñ‹Ð±Ð°Ð»ÐºÐ¸.
    public static final int S1_IS_NOT_SUFFICIENT = 1473; // Ð�ÐµÐ´Ð¾Ñ�Ñ‚Ð°Ñ‚Ð¾Ñ‡Ð½Ð¾ $s1.
    public static final int S1_IS_NOT_AVAILABLE = 1474; // Ð�ÐµÐ»ÑŒÐ·Ñ� Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒ:
    // $s1.
    public static final int PET_HAS_DROPPED_S1 = 1475; // ÐŸÐ¸Ñ‚Ð¾Ð¼ÐµÑ† Ð¿Ð¾Ñ‚ÐµÑ€Ñ�Ð»: $s1.
    public static final int PET_HAS_DROPPED__S1S2 = 1476; // ÐŸÐ¸Ñ‚Ð¾Ð¼ÐµÑ† Ð¿Ð¾Ñ‚ÐµÑ€Ñ�Ð»:
    // +$s1 $s2.
    public static final int PET_HAS_DROPPED_S2_OF_S1 = 1477; // ÐŸÐ¸Ñ‚Ð¾Ð¼ÐµÑ† Ð¿Ð¾Ñ‚ÐµÑ€Ñ�Ð»:
    // $s1 ($s2 ÑˆÑ‚.)
    public static final int YOU_CAN_REGISTER_ONLY_256_COLOR_BMP_FILES_WITH_A_SIZE_OF_64X64 = 1478; // Ð¤Ð°Ð¹Ð»
    // -
    // Ñ„Ð¾Ñ€Ð¼Ð°Ñ‚
    // bmp,
    // 256
    // Ñ†Ð²ÐµÑ‚Ð¾Ð²,
    // Ñ€Ð°Ð·Ð¼ÐµÑ€
    // 64*64.
    public static final int THIS_FISHING_SHOT_IS_NOT_FIT_FOR_THE_FISHING_POLE_CRYSTAL = 1479; // Ð�ÐµÐ²ÐµÑ€Ð½Ñ‹Ð¹
    // Ñ€Ð°Ð½Ð³
    // Ð·Ð°Ñ€Ñ�Ð´Ð¾Ð²
    // Ð´ÑƒÑˆÐ¸
    // Ð´Ð»Ñ�
    // ÑƒÐ´Ð¾Ñ‡ÐºÐ¸.
    public static final int DO_YOU_WANT_TO_CANCEL_YOUR_APPLICATION_FOR_JOINING_THE_GRAND_OLYMPIAD = 1480; // ÐžÑ‚ÐºÐ°Ð·Ð°Ñ‚ÑŒÑ�Ñ�
    // Ð¾Ñ‚
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð¸Ñ�
    // Ð²
    // ÐžÐ»Ð¸Ð¼Ð¿Ð¸Ð°Ð´Ðµ?
    public static final int YOU_HAVE_BEEN_SELECTED_FOR_NO_CLASS_GAME_DO_YOU_WANT_TO_JOIN = 1481; // Ð’Ñ‹
    // Ð²Ñ‹Ð±Ñ€Ð°Ð»Ð¸
    // Ð¸Ð³Ñ€Ñƒ
    // Ñ�
    // Ð¾Ñ‚Ñ�ÑƒÑ‚Ñ�Ñ‚Ð²Ð¸ÐµÐ¼
    // Ð¾Ð³Ñ€Ð°Ð½Ð¸Ñ‡ÐµÐ½Ð¸Ð¹
    // Ð¿Ð¾
    // ÐºÐ»Ð°Ñ�Ñ�Ð°Ð¼.
    // ÐŸÑ€Ð¾Ð´Ð¾Ð»Ð¶Ð¸Ñ‚ÑŒ?
    public static final int YOU_HAVE_BEEN_SELECTED_FOR_CLASSIFIED_GAME_DO_YOU_WANT_TO_JOIN = 1482; // Ð’Ñ‹
    // Ð²Ñ‹Ð±Ñ€Ð°Ð»Ð¸
    // Ð¸Ð³Ñ€Ñƒ
    // Ñ�
    // Ð¾Ð³Ñ€Ð°Ð½Ð¸Ñ‡ÐµÐ½Ð¸ÐµÐ¼
    // Ð¿Ð¾
    // ÐºÐ»Ð°Ñ�Ñ�Ð°Ð¼.
    // ÐŸÑ€Ð¾Ð´Ð¾Ð»Ð¶Ð¸Ñ‚ÑŒ?
    public static final int DO_YOU_WANT_TO_BECOME_A_HERO_NOW = 1483; // Ð’Ñ‹
    // Ð³Ð¾Ñ‚Ð¾Ð²Ñ‹
    // Ñ�Ñ‚Ð°Ñ‚ÑŒ
    // Ð³ÐµÑ€Ð¾ÐµÐ¼?
    public static final int DO_YOU_WANT_TO_USE_THE_HEROES_WEAPON_THAT_YOU_CHOSE = 1484; // Ð’Ð¾Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒÑ�Ñ�
    // Ð²Ñ‹Ð±Ñ€Ð°Ð½Ð½Ñ‹Ð¼
    // Ð¾Ñ€ÑƒÐ¶Ð¸ÐµÐ¼
    // Ð“ÐµÑ€Ð¾Ñ�?
    // Ð”Ð¾Ñ�Ñ‚ÑƒÐ¿Ð½Ð¾
    // Ð²Ñ�ÐµÐ¼
    // Ñ€Ð°Ñ�Ð°Ð¼,
    // ÐºÑ€Ð¾Ð¼Ðµ
    // ÐšÐ°Ð¼Ð°Ñ�Ð»ÐµÐ¹.
    public static final int THE_FERRY_FROM_TALKING_ISLAND_TO_GLUDIN_HARBOR_HAS_BEEN_DELAYED = 1485; // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ,
    // Ð¾Ñ‚Ð¿Ñ€Ð°Ð²Ð»Ñ�ÑŽÑ‰Ð¸Ð¹Ñ�Ñ�
    // Ñ�
    // Ð“Ð¾Ð²Ð¾Ñ€Ñ�Ñ‰ÐµÐ³Ð¾
    // ÐžÑ�Ñ‚Ñ€Ð¾Ð²Ð°
    // Ð²
    // Ð³Ð°Ð²Ð°Ð½ÑŒ
    // Ð“Ð»ÑƒÐ´Ð¸Ð½,
    // Ð·Ð°Ð´ÐµÑ€Ð¶Ð¸Ð²Ð°ÐµÑ‚Ñ�Ñ�.
    public static final int THE_FERRY_FROM_GLUDIN_HARBOR_TO_TALKING_ISLAND_HAS_BEEN_DELAYED = 1486; // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ,
    // Ð¾Ñ‚Ð¿Ñ€Ð°Ð²Ð»Ñ�ÑŽÑ‰Ð¸Ð¹Ñ�Ñ�
    // Ñ�
    // Ð³Ð°Ð²Ð°Ð½Ð¸
    // Ð“Ð»ÑƒÐ´Ð¸Ð½
    // Ð½Ð°
    // Ð“Ð¾Ð²Ð¾Ñ€Ñ�Ñ‰Ð¸Ð¹
    // ÐžÑ�Ñ‚Ñ€Ð¾Ð²,
    // Ð·Ð°Ð´ÐµÑ€Ð¶Ð¸Ð²Ð°ÐµÑ‚Ñ�Ñ�.
    public static final int THE_FERRY_FROM_GIRAN_HARBOR_TO_TALKING_ISLAND_HAS_BEEN_DELAYED = 1487; // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ,
    // Ð¾Ñ‚Ð¿Ñ€Ð°Ð²Ð»Ñ�ÑŽÑ‰Ð¸Ð¹Ñ�Ñ�
    // Ð¸Ð·
    // Ð³Ð°Ð²Ð°Ð½Ð¸
    // Ð“Ð»ÑƒÐ´Ð¸Ð½
    // Ð½Ð°
    // Ð“Ð¾Ð²Ð¾Ñ€Ñ�Ñ‰Ð¸Ð¹
    // ÐžÑ�Ñ‚Ñ€Ð¾Ð²,
    // Ð·Ð°Ð´ÐµÑ€Ð¶Ð¸Ð²Ð°ÐµÑ‚Ñ�Ñ�.
    public static final int THE_FERRY_FROM_TALKING_ISLAND_TO_GIRAN_HARBOR_HAS_BEEN_DELAYED = 1488; // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ,
    // Ð¾Ñ‚Ð¿Ñ€Ð°Ð²Ð»Ñ�ÑŽÑ‰Ð¸Ð¹Ñ�Ñ�
    // Ñ�
    // Ð“Ð¾Ð²Ð¾Ñ€Ñ�Ñ‰ÐµÐ³Ð¾
    // ÐžÑ�Ñ‚Ñ€Ð¾Ð²Ð°
    // Ð²
    // Ð³Ð°Ð²Ð°Ð½ÑŒ
    // Ð“Ð»ÑƒÐ´Ð¸Ð½,
    // Ð·Ð°Ð´ÐµÑ€Ð¶Ð¸Ð²Ð°ÐµÑ‚Ñ�Ñ�.
    public static final int INNADRIL_CRUISE_SERVICE_HAS_BEEN_DELAYED = 1489; // ÐŸÐµÑ€ÐµÐ¼ÐµÑ‰ÐµÐ½Ð¸Ðµ
    // ÐºÐ¾Ñ€Ð°Ð±Ð»Ñ�
    // Ð²
    // Ð˜Ð½Ð½Ð°Ð´Ñ€Ð¸Ð»
    // Ð·Ð°Ð´ÐµÑ€Ð¶Ð¸Ð²Ð°ÐµÑ‚Ñ�Ñ�.
    public static final int TRADED_S2_OF_CROP_S1 = 1490; // ÐŸÑ€Ð¾Ð´Ð°Ð½Ð¾ Ð¿Ð»Ð¾Ð´Ð¾Ð²: $s1
    // ($s2 ÑˆÑ‚.)
    public static final int FAILED_IN_TRADING_S2_OF_CROP_S1 = 1491; // Ð�Ðµ
    // ÑƒÐ´Ð°Ð»Ð¾Ñ�ÑŒ
    // Ð¿Ñ€Ð¾Ð´Ð°Ñ‚ÑŒ
    // Ð¿Ð»Ð¾Ð´Ð¾Ð²:
    // $s1 ($s2
    // ÑˆÑ‚.)
    public static final int YOU_WILL_ENTER_THE_OLYMPIAD_STADIUM_IN_S1_SECOND_S = 1492; // Ð’Ñ‹
    // Ð¿ÐµÑ€ÐµÐ¼ÐµÑ�Ñ‚Ð¸Ñ‚ÐµÑ�ÑŒ
    // Ð½Ð°
    // ÐžÐ»Ð¸Ð¼Ð¿Ð¸Ð¹Ñ�ÐºÐ¸Ð¹
    // Ñ�Ñ‚Ð°Ð´Ð¸Ð¾Ð½
    // Ñ‡ÐµÑ€ÐµÐ·
    // $s1
    // Ñ�ÐµÐº.
    public static final int THE_GAME_HAS_BEEN_CANCELLED_BECAUSE_THE_OTHER_PARTY_ENDS_THE_GAME = 1493; // ÐŸÑ€Ð¾Ñ‚Ð¸Ð²Ð½Ð¸Ðº
    // Ð²Ñ‹ÑˆÐµÐ»
    // Ð¸Ð·
    // Ð¸Ð³Ñ€Ñ‹,
    // Ð¾Ñ‚ÐºÐ°Ð·Ð°Ð²ÑˆÐ¸Ñ�ÑŒ
    // Ñ‚ÐµÐ¼
    // Ñ�Ð°Ð¼Ñ‹Ð¼
    // Ð¾Ñ‚
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð¸Ñ�
    // Ð²
    // Ñ�Ð¾Ñ€ÐµÐ²Ð½Ð¾Ð²Ð°Ð½Ð¸Ð¸.
    public static final int THE_GAME_HAS_BEEN_CANCELLED_BECAUSE_THE_OTHER_PARTY_DOES_NOT_MEET_THE_REQUIREMENTS_FOR_JOINING_THE_GAME = 1494; // ÐŸÑ€Ð¾Ñ‚Ð¸Ð²Ð½Ð¸Ðº
    // Ð½Ðµ
    // Ñ�Ð¾Ð¾Ñ‚Ð²ÐµÑ‚Ñ�Ñ‚Ð²Ð¾Ð²Ð°Ð»
    // ÑƒÑ�Ð»Ð¾Ð²Ð¸Ñ�Ð¼
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð¸Ñ�
    // Ð²
    // Ñ�Ð¾Ñ€ÐµÐ²Ð½Ð¾Ð²Ð°Ð½Ð¸Ð¸,
    // Ð¿Ð¾Ñ�Ñ‚Ð¾Ð¼Ñƒ
    // Ñ�Ð¾Ñ€ÐµÐ²Ð½Ð¾Ð²Ð°Ð½Ð¸Ðµ
    // Ð¾Ñ‚Ð¼ÐµÐ½ÐµÐ½Ð¾.
    public static final int THE_GAME_WILL_START_IN_S1_SECOND_S = 1495; // Ð¡Ð¾Ñ€ÐµÐ²Ð½Ð¾Ð²Ð°Ð½Ð¸Ñ�
    // Ð½Ð°Ñ‡Ð½ÑƒÑ‚Ñ�Ñ�
    // Ñ‡ÐµÑ€ÐµÐ·
    // $s1
    // Ñ�ÐµÐº.
    public static final int STARTS_THE_GAME = 1496; // Ð¡Ð¾Ñ€ÐµÐ²Ð½Ð¾Ð²Ð°Ð½Ð¸Ñ� Ð½Ð°Ñ‡Ð°Ð»Ð¸Ñ�ÑŒ.
    public static final int S1_HAS_WON_THE_GAME = 1497; // ÐŸÐ¾Ð±ÐµÐ´Ð¸Ñ‚ÐµÐ»ÑŒ
    // Ñ�Ð¾Ñ€ÐµÐ²Ð½Ð¾Ð²Ð°Ð½Ð¸Ð¹ - $c1.
    public static final int THE_GAME_ENDED_IN_A_TIE = 1498; // Ð�Ð¸Ñ‡ÑŒÑ�.
    public static final int YOU_WILL_GO_BACK_TO_THE_VILLAGE_IN_S1_SECOND_S = 1499; // Ð§ÐµÑ€ÐµÐ·
    // $s1
    // Ñ�ÐµÐº
    // Ð’Ñ‹
    // Ð¿ÐµÑ€ÐµÐ¼ÐµÑ�Ñ‚Ð¸Ñ‚ÐµÑ�ÑŒ
    // Ð²
    // Ð³Ð¾Ñ€Ð¾Ð´.
    public static final int YOU_CANT_JOIN_THE_OLYMPIAD_WITH_A_SUB_JOB_CHARACTER = 1500; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð²Ð¾Ð²Ð°Ñ‚ÑŒ
    // Ð²
    // ÐžÐ»Ð¸Ð¼Ð¿Ð¸Ð°Ð´Ðµ
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶ÐµÐ¼
    // Ñ�
    // Ð°ÐºÑ‚Ð¸Ð²Ð¸Ñ€Ð¾Ð²Ð°Ð½Ð½Ñ‹Ð¼
    // Ð¿Ð¾Ð´ÐºÐ»Ð°Ñ�Ñ�Ð¾Ð¼.
    public static final int ONLY_NOBLESS_CAN_PARTICIPATE_IN_THE_OLYMPIAD = 1501; // Ð’
    // Ð¾Ð»Ð¸Ð¼Ð¿Ð¸Ð°Ð´Ðµ
    // Ð¼Ð¾Ð³ÑƒÑ‚
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð²Ð¾Ð²Ð°Ñ‚ÑŒ
    // Ñ‚Ð¾Ð»ÑŒÐºÐ¾
    // Ð´Ð²Ð¾Ñ€Ñ�Ð½Ðµ.
    public static final int YOU_HAVE_ALREADY_BEEN_REGISTERED_IN_A_WAITING_LIST_OF_AN_EVENT = 1502; // Ð’Ñ‹
    // ÑƒÐ¶Ðµ
    // Ñ�Ð¾Ñ�Ñ‚Ð¾Ð¸Ñ‚Ðµ
    // Ð²
    // Ñ�Ð¿Ð¸Ñ�ÐºÐµ
    // Ð¾Ð¶Ð¸Ð´Ð°ÑŽÑ‰Ð¸Ñ…
    // Ñ�Ð¾Ñ€ÐµÐ²Ð½Ð¾Ð²Ð°Ð½Ð¸Ñ�.
    public static final int YOU_HAVE_BEEN_REGISTERED_IN_A_WAITING_LIST_OF_CLASSIFIED_GAMES = 1503; // Ð’Ñ‹
    // Ð·Ð°Ñ€ÐµÐ³Ð¸Ñ�Ñ‚Ñ€Ð¸Ñ€Ð¾Ð²Ð°Ð»Ð¸Ñ�ÑŒ
    // Ð½Ð°
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð¸Ðµ
    // Ð²
    // ÐºÐ»Ð°Ñ�Ñ�Ð¾Ð²Ñ‹Ñ…
    // Ñ�Ð¾Ñ€ÐµÐ²Ð½Ð¾Ð²Ð°Ð½Ð¸Ñ�Ñ….
    public static final int YOU_HAVE_BEEN_REGISTERED_IN_A_WAITING_LIST_OF_NO_CLASS_GAMES = 1504; // Ð’Ñ‹
    // Ð·Ð°Ñ€ÐµÐ³Ð¸Ñ�Ñ‚Ñ€Ð¸Ñ€Ð¾Ð²Ð°Ð»Ð¸Ñ�ÑŒ
    // Ð½Ð°
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð¸Ðµ
    // Ð²Ð¾
    // Ð²Ð½ÐµÐºÐ»Ð°Ñ�Ñ�Ð¾Ð²Ñ‹Ñ…
    // Ñ�Ð¾Ñ€ÐµÐ²Ð½Ð¾Ð²Ð°Ð½Ð¸Ñ�Ñ….
    public static final int YOU_HAVE_BEEN_DELETED_FROM_THE_WAITING_LIST_OF_A_GAME = 1505; // Ð’Ñ‹
    // ÑƒÐ´Ð°Ð»Ð¸Ð»Ð¸
    // Ñ�Ð²Ð¾ÑŽ
    // Ð·Ð°Ñ�Ð²ÐºÑƒ
    // Ð¸Ð·
    // Ñ�Ð¿Ð¸Ñ�ÐºÐ°
    // Ð¾Ð¶Ð¸Ð´Ð°ÑŽÑ‰Ð¸Ñ…
    // Ñ�Ð¾Ñ€ÐµÐ²Ð½Ð¾Ð²Ð°Ð½Ð¸Ð¹.
    public static final int YOU_HAVE_NOT_BEEN_REGISTERED_IN_A_WAITING_LIST_OF_A_GAME = 1506; // Ð’Ñ‹
    // Ð½Ðµ
    // Ñ�Ð¾Ñ�Ñ‚Ð¾Ð¸Ñ‚Ðµ
    // Ð²
    // Ñ�Ð¿Ð¸Ñ�ÐºÐµ
    // Ð¾Ð¶Ð¸Ð´Ð°ÑŽÑ‰Ð¸Ñ…
    // Ñ�Ð¾Ñ€ÐµÐ²Ð½Ð¾Ð²Ð°Ð½Ð¸Ñ�.
    public static final int THIS_ITEM_CANT_BE_EQUIPPED_FOR_THE_OLYMPIAD_EVENT = 1507; // Ð�ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾
    // Ð½Ð°Ð´ÐµÑ‚ÑŒ
    // Ñ�Ñ‚Ð¾Ñ‚
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚
    // Ð½Ð°
    // Ð¾Ð»Ð¸Ð¼Ð¿Ð¸Ð°Ð´Ðµ.
    public static final int THIS_ITEM_IS_NOT_AVAILABLE_FOR_THE_OLYMPIAD_EVENT = 1508; // Ð�ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒ
    // Ñ�Ñ‚Ð¾Ñ‚
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚
    // Ð½Ð°
    // Ð¾Ð»Ð¸Ð¼Ð¿Ð¸Ð°Ð´Ðµ.
    public static final int THIS_SKILL_IS_NOT_AVAILABLE_FOR_THE_OLYMPIAD_EVENT = 1509; // Ð�ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒ
    // Ñ�Ñ‚Ð¾
    // ÑƒÐ¼ÐµÐ½Ð¸Ðµ
    // Ð½Ð°
    // Ð¾Ð»Ð¸Ð¼Ð¿Ð¸Ð°Ð´Ðµ.
    public static final int S1_IS_MAKING_AN_ATTEMPT_AT_RESURRECTION_WITH_$S2_EXPERIENCE_POINTS_DO_YOU_WANT_TO_CONTINUE_WITH_THIS_RESURRECTION = 1510; // $c1
    // Ð¿Ñ€Ð¾Ð±ÑƒÐµÑ‚
    // Ð²Ð¾Ñ�Ñ�Ñ‚Ð°Ð½Ð¾Ð²Ð¸Ñ‚ÑŒ
    // ÑƒÑ‚ÐµÑ€Ñ�Ð½Ð½Ñ‹Ð¹
    // Ð¾Ð¿Ñ‹Ñ‚
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°
    // $s2.
    // Ð’Ñ‹
    // Ñ�Ð¾Ð³Ð»Ð°Ñ�Ð½Ñ‹?
    public static final int WHILE_A_PET_IS_ATTEMPTING_TO_RESURRECT_IT_CANNOT_HELP_IN_RESURRECTING_ITS_MASTER = 1511; // Ð’Ð¾
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ð²Ð¾Ñ�ÐºÑ€ÐµÑˆÐµÐ½Ð¸Ñ�
    // Ð¿Ð¸Ñ‚Ð¾Ð¼ÐµÑ†
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð¿Ð¾Ð¼Ð¾Ñ‡ÑŒ
    // Ñ…Ð¾Ð·Ñ�Ð¸Ð½Ñƒ.
    public static final int WHILE_A_PETS_MASTER_IS_ATTEMPTING_TO_RESURRECT_THE_PET_CANNOT_BE_RESURRECTED_AT_THE_SAME_TIME = 1512; // Ð’Ð¾
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ð²Ð¾Ñ�ÐºÑ€ÐµÑˆÐµÐ½Ð¸Ñ�
    // Ñ…Ð¾Ð·Ñ�Ð¸Ð½
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð¿Ð¾Ð¼Ð¾Ñ‡ÑŒ
    // Ð¿Ð¸Ñ‚Ð¾Ð¼Ñ†Ñƒ.
    public static final int BETTER_RESURRECTION_HAS_BEEN_ALREADY_PROPOSED = 1513; // ÐŸÑ€ÐµÐ´Ð»Ð¾Ð¶ÐµÐ½Ð¸Ðµ
    // Ð½Ð°
    // Ð²Ð¾Ñ�ÐºÑ€ÐµÑˆÐµÐ½Ð¸Ðµ
    // ÑƒÐ¶Ðµ
    // Ð¿Ñ€Ð¸ÑˆÐ»Ð¾.
    public static final int SINCE_THE_PET_WAS_IN_THE_PROCESS_OF_BEING_RESURRECTED_THE_ATTEMPT_TO_RESURRECT_ITS_MASTER_HAS_BEEN_CANCELLED = 1514; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ð²Ð¾Ñ�ÐºÑ€ÐµÑ�Ð¸Ñ‚ÑŒ
    // Ñ…Ð¾Ð·Ñ�Ð¸Ð½Ð°,
    // Ñ‚Ð°Ðº
    // ÐºÐ°Ðº
    // Ð²
    // Ð´Ð°Ð½Ð½Ñ‹Ð¹
    // Ð¼Ð¾Ð¼ÐµÐ½Ñ‚
    // Ð²Ð¾Ñ�ÐºÑ€ÐµÑˆÐ°ÐµÑ‚Ñ�Ñ�
    // Ð¿Ð¸Ñ‚Ð¾Ð¼ÐµÑ†.
    public static final int SINCE_THE_MASTER_WAS_IN_THE_PROCESS_OF_BEING_RESURRECTED_THE_ATTEMPT_TO_RESURRECT_THE_PET_HAS_BEEN_CANCELLED = 1515; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ð²Ð¾Ñ�ÐºÑ€ÐµÑ�Ð¸Ñ‚ÑŒ
    // Ð¿Ð¸Ñ‚Ð¾Ð¼Ñ†Ð°,
    // Ñ‚Ð°Ðº
    // ÐºÐ°Ðº
    // Ð²
    // Ð´Ð°Ð½Ð½Ñ‹Ð¹
    // Ð¼Ð¾Ð¼ÐµÐ½Ñ‚
    // Ð²Ð¾Ñ�ÐºÑ€ÐµÑˆÐ°ÐµÑ‚Ñ�Ñ�
    // Ñ…Ð¾Ð·Ñ�Ð¸Ð½.
    public static final int THE_TARGET_IS_UNAVAILABLE_FOR_SEEDING = 1516; // Ð�Ð°
    // Ñ†ÐµÐ»ÑŒ
    // Ð½ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒ
    // Ñ�ÐµÐ¼Ñ�.
    public static final int FAILED_IN_BLESSED_ENCHANT_THE_ENCHANT_VALUE_OF_THE_ITEM_BECAME_0 = 1517; // Ð‘Ð»Ð°Ð³Ð¾Ñ�Ð»Ð¾Ð²ÐµÐ½Ð½Ð¾Ðµ
    // ÑƒÐ»ÑƒÑ‡ÑˆÐµÐ½Ð¸Ðµ
    // Ð½Ðµ
    // ÑƒÐ´Ð°Ð»Ð¾Ñ�ÑŒ.
    // Ð—Ð°Ñ‚Ð¾Ñ‡ÐºÐ°
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ð°
    // Ñ�Ñ‚Ð°Ð»Ð°
    // 0.
    public static final int YOU_DO_NOT_MEET_THE_REQUIRED_CONDITION_TO_EQUIP_THAT_ITEM = 1518; // Ð�ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾
    // Ð½Ð°Ð´ÐµÑ‚ÑŒ
    // Ñ�Ñ‚Ð¾Ñ‚
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚
    // Ð¸Ð·-Ð·Ð°
    // Ñ‚Ð¾Ð³Ð¾,
    // Ñ‡Ñ‚Ð¾
    // ÑƒÑ�Ð»Ð¾Ð²Ð¸Ñ�
    // Ð½Ðµ
    // Ñ�Ð¾Ð±Ð»ÑŽÐ´ÐµÐ½Ñ‹.
    public static final int THE_PET_HAS_BEEN_KILLED_IF_YOU_DO_NOT_RESURRECT_IT_WITHIN_24_HOURS_THE_PETS_BODY_WILL_DISAPPEAR_ALONG_WITH_ALL_THE_PETS_ITEMS = 1519; // ÐŸÐ¸Ñ‚Ð¾Ð¼ÐµÑ†
    // ÑƒÐ¼ÐµÑ€.
    // Ð•Ñ�Ð»Ð¸
    // Ð½Ðµ
    // Ð²Ð¾Ð·Ñ€Ð¾Ð´Ð¸Ñ‚ÑŒ
    // Ð¿Ð¸Ñ‚Ð¾Ð¼Ñ†Ð°
    // Ð²
    // Ñ‚ÐµÑ‡ÐµÐ½Ð¸Ðµ
    // 24
    // Ñ‡,
    // Ñ‚Ð¾
    // Ñ‚Ñ€ÑƒÐ¿
    // Ð¸
    // Ð²Ñ�Ðµ
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ñ‹
    // Ð¸Ñ�Ñ‡ÐµÐ·Ð½ÑƒÑ‚.
    public static final int SERVITOR_PASSED_AWAY = 1520; // Ð¡Ð»ÑƒÐ³Ð° ÑƒÐ¼ÐµÑ€.
    public static final int SERVITOR_DISAPPEASR_BECAUSE_THE_SUMMONING_TIME_IS_OVER = 1521; // Ð’Ñ€ÐµÐ¼Ñ�
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ð½Ð¸Ñ�
    // Ñ�Ð»ÑƒÐ³Ð¸
    // Ð¿Ñ€Ð¾ÑˆÐ»Ð¾.
    // Ð’Ð°Ð¼
    // Ð½ÑƒÐ¶Ð½Ð¾
    // Ð¿Ñ€Ð¸Ð·Ð²Ð°Ñ‚ÑŒ
    // ÐµÑ‰Ðµ
    // Ð¾Ð´Ð½Ð¾Ð³Ð¾.
    public static final int THE_CORPSE_DISAPPEARED_BECAUSE_MUCH_TIME_PASSED_AFTER_PET_DIED = 1522; // ÐŸÐ¾
    // Ð¿Ñ€Ð¾ÑˆÐµÑ�Ñ‚Ð²Ð¸Ð¸
    // Ð´Ð¾Ð»Ð³Ð¾Ð³Ð¾
    // Ð²Ñ€ÐµÐ¼ÐµÐ½Ð¸
    // Ñ‚Ñ€ÑƒÐ¿
    // Ð¿Ð¸Ñ‚Ð¾Ð¼Ñ†Ð°
    // Ð¸Ñ�Ñ‡ÐµÐ·Ð°ÐµÑ‚.
    public static final int BECAUSE_PET_OR_SERVITOR_MAY_BE_DROWNED_WHILE_THE_BOAT_MOVES_PLEASE_RELEASE_THE_SUMMON_BEFORE_DEPARTURE = 1523; // Ð’Ð¾
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ð´Ð²Ð¸Ð¶ÐµÐ½Ð¸Ñ�
    // ÐºÐ¾Ñ€Ð°Ð±Ð»Ñ�
    // Ð¿Ð¸Ñ‚Ð¾Ð¼ÐµÑ†
    // Ð¸Ð»Ð¸
    // Ñ�Ð»ÑƒÐ³Ð°
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // ÑƒÐ¿Ð°Ñ�Ñ‚ÑŒ
    // Ð·Ð°
    // Ð±Ð¾Ñ€Ñ‚
    // Ð¸
    // ÑƒÐ¼ÐµÑ€ÐµÑ‚ÑŒ.
    // ÐŸÐ¾Ð¶Ð°Ð»ÑƒÐ¹Ñ�Ñ‚Ð°,
    // ÑƒÐ±ÐµÑ€Ð¸Ñ‚Ðµ
    // Ð²Ð°ÑˆÐ¸Ñ…
    // Ð¿Ð¸Ñ‚Ð¾Ð¼Ñ†ÐµÐ².
    public static final int PET_OF_S1_GAINED_S2 = 1524; // ÐŸÐ¸Ñ‚Ð¾Ð¼ÐµÑ† Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð° $c1
    // Ð¿Ð¾Ð»ÑƒÑ‡Ð¸Ð»: $s2.
    public static final int PET_OF_S1_GAINED_S3_OF_S2 = 1525; // ÐŸÐ¸Ñ‚Ð¾Ð¼ÐµÑ†
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð° $c1
    // Ð¿Ð¾Ð»ÑƒÑ‡Ð¸Ð»: $s2
    // ($s3 ÑˆÑ‚.)
    public static final int PET_OF_S1_GAINED__S2S3 = 1526; // ÐŸÐ¸Ñ‚Ð¾Ð¼ÐµÑ† Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°
    // $c1 Ð¿Ð¾Ð»ÑƒÑ‡Ð¸Ð»: +$s2
    // $s3.
    public static final int PET_TOOK_S1_BECAUSE_HE_WAS_HUNGRY = 1527; // Ð’Ð°Ñˆ
    // Ð¿Ð¸Ñ‚Ð¾Ð¼ÐµÑ†
    // Ð¿Ñ€Ð¾Ð³Ð¾Ð»Ð¾Ð´Ð°Ð»Ñ�Ñ�
    // Ð¸ Ñ�ÑŠÐµÐ»:
    // $s1.
    public static final int A_FORCIBLE_PETITION_FROM_GM_HAS_BEEN_RECEIVED = 1528; // Ð’Ñ‹
    // Ð¿Ð¾Ñ�Ð»Ð°Ð»Ð¸
    // Ð˜Ð³Ñ€Ð¾Ð²Ð¾Ð¼Ñƒ
    // Ð¼Ð°Ñ�Ñ‚ÐµÑ€Ñƒ
    // Ð·Ð°Ñ�Ð²ÐºÑƒ
    // Ð½Ð°
    // Ð±ÐµÑ�ÐµÐ´Ñƒ.
    public static final int S1_HAS_INVITED_YOU_TO_THE_COMMAND_CHANNEL_DO_YOU_WANT_TO_JOIN = 1529; // $c1
    // Ð¿Ñ€Ð¸Ð³Ð»Ð°ÑˆÐ°ÐµÑ‚
    // Ð’Ð°Ñ�
    // Ð²
    // ÐºÐ°Ð½Ð°Ð»
    // ÐºÐ¾Ð¼Ð°Ð½Ð´Ñ‹.
    // Ð¡Ð¾Ð³Ð»Ð°Ñ�Ð¸Ñ‚ÑŒÑ�Ñ�?
    public static final int SELECT_A_TARGET_OR_ENTER_THE_NAME = 1530; // Ð’Ñ‹Ð±ÐµÑ€Ð¸Ñ‚Ðµ
    // Ñ†ÐµÐ»ÑŒ
    // Ð¸Ð»Ð¸
    // Ð²Ð²ÐµÐ´Ð¸Ñ‚Ðµ
    // Ð¸Ð¼Ñ�.
    public static final int ENTER_THE_NAME_OF_CLAN_AGAINST_WHICH_YOU_WANT_TO_MAKE_AN_ATTACK = 1531; // Ð’Ð²ÐµÐ´Ð¸Ñ‚Ðµ
    // Ð½Ð°Ð·Ð²Ð°Ð½Ð¸Ðµ
    // ÐºÐ»Ð°Ð½Ð°,
    // Ñ�
    // ÐºÐ¾Ñ‚Ð¾Ñ€Ñ‹Ð¼
    // Ð²Ñ‹
    // Ñ…Ð¾Ñ‚Ð¸Ñ‚Ðµ
    // Ð½Ð°Ñ‡Ð°Ñ‚ÑŒ
    // Ð²Ð¾Ð¹Ð½Ñƒ.
    public static final int ENTER_THE_NAME_OF_CLAN_AGAINST_WHICH_YOU_WANT_TO_STOP_THE_WAR = 1532; // Ð’Ð²ÐµÐ´Ð¸Ñ‚Ðµ
    // Ð½Ð°Ð·Ð²Ð°Ð½Ð¸Ðµ
    // ÐºÐ»Ð°Ð½Ð°,
    // Ñ�
    // ÐºÐ¾Ñ‚Ð¾Ñ€Ñ‹Ð¼
    // Ð²Ñ‹
    // Ñ…Ð¾Ñ‚Ð¸Ñ‚Ðµ
    // Ð¿Ñ€ÐµÐºÑ€Ð°Ñ‚Ð¸Ñ‚ÑŒ
    // Ð²Ð¾Ð¹Ð½Ñƒ.
    public static final int ATTENTION_S1_PICKED_UP_S2 = 1533; // $c1 Ð¿Ð¾Ð´Ð½Ð¸Ð¼Ð°ÐµÑ‚:
    // $s2.
    public static final int ATTENTION_S1_PICKED_UP__S2_S3 = 1534; // $c1
    // Ð¿Ð¾Ð´Ð½Ð¸Ð¼Ð°ÐµÑ‚:
    // +$s2 $s3.
    public static final int ATTENTION_S1_PET_PICKED_UP_S2 = 1535; // ÐŸÐ¸Ñ‚Ð¾Ð¼ÐµÑ†
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°
    // $c1 Ð¿Ð¾Ð´Ð½Ñ�Ð»:
    // $s2.
    public static final int ATTENTION_S1_PET_PICKED_UP__S2_S3 = 1536; // ÐŸÐ¸Ñ‚Ð¾Ð¼ÐµÑ†
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°
    // $c1
    // Ð¿Ð¾Ð´Ð½Ñ�Ð»:
    // +$s2
    // $s3.
    public static final int CURRENT_LOCATION__S1_S2_S3_NEAR_RUNE_VILLAGE = 1537; // Ð¢ÐµÐºÑƒÑ‰ÐµÐµ
    // Ð¼ÐµÑ�Ñ‚Ð¾Ð¿Ð¾Ð»Ð¾Ð¶ÐµÐ½Ð¸Ðµ:
    // $s1,
    // $s2,
    // $s3
    // (Ð¾ÐºÑ€ÐµÑ�Ñ‚Ð½Ð¾Ñ�Ñ‚Ð¸
    // Ð ÑƒÐ½Ñ‹)
    public static final int CURRENT_LOCATION__S1_S2_S3_NEAR_GODDARD_CASTLE_TOWN = 1538; // Ð¢ÐµÐºÑƒÑ‰ÐµÐµ
    // Ð¼ÐµÑ�Ñ‚Ð¾Ð¿Ð¾Ð»Ð¾Ð¶ÐµÐ½Ð¸Ðµ:
    // $s1,
    // $s2,
    // $s3
    // (Ð¾ÐºÑ€ÐµÑ�Ñ‚Ð½Ð¾Ñ�Ñ‚Ð¸
    // Ð“Ð¾Ð´Ð´Ð°Ñ€Ð´Ð°)
    public static final int CARGO_HAS_ARRIVED_AT_TALKING_ISLAND_VILLAGE = 1539; // Ð“Ñ€ÑƒÐ·
    // Ð´Ð¾Ñ�Ñ‚Ð°Ð²Ð»ÐµÐ½
    // Ð²
    // Ð”ÐµÑ€ÐµÐ²Ð½ÑŽ
    // Ð“Ð¾Ð²Ð¾Ñ€Ñ�Ñ‰ÐµÐ³Ð¾
    // ÐžÑ�Ñ‚Ñ€Ð¾Ð²Ð°.
    public static final int CARGO_HAS_ARRIVED_AT_DARK_ELVEN_VILLAGE = 1540; // Ð“Ñ€ÑƒÐ·
    // Ð´Ð¾Ñ�Ñ‚Ð°Ð²Ð»ÐµÐ½
    // Ð²
    // Ð”ÐµÑ€ÐµÐ²Ð½ÑŽ
    // Ð¢ÐµÐ¼Ð½Ñ‹Ñ…
    // Ð­Ð»ÑŒÑ„Ð¾Ð².
    public static final int CARGO_HAS_ARRIVED_AT_ELVEN_VILLAGE = 1541; // Ð“Ñ€ÑƒÐ·
    // Ð´Ð¾Ñ�Ñ‚Ð°Ð²Ð»ÐµÐ½
    // Ð²
    // Ð”ÐµÑ€ÐµÐ²Ð½ÑŽ
    // Ð­Ð»ÑŒÑ„Ð¾Ð².
    public static final int CARGO_HAS_ARRIVED_AT_ORC_VILLAGE = 1542; // Ð“Ñ€ÑƒÐ·
    // Ð´Ð¾Ñ�Ñ‚Ð°Ð²Ð»ÐµÐ½
    // Ð²
    // Ð”ÐµÑ€ÐµÐ²Ð½ÑŽ
    // ÐžÑ€ÐºÐ¾Ð².
    public static final int CARGO_HAS_ARRIVED_AT_DWARVEN_VILLAGE = 1543; // Ð“Ñ€ÑƒÐ·
    // Ð´Ð¾Ñ�Ñ‚Ð°Ð²Ð»ÐµÐ½
    // Ð²
    // Ð”ÐµÑ€ÐµÐ²Ð½ÑŽ
    // Ð“Ð½Ð¾Ð¼Ð¾Ð².
    public static final int CARGO_HAS_ARRIVED_AT_ADEN_CASTLE_TOWN = 1544; // Ð“Ñ€ÑƒÐ·
    // Ð´Ð¾Ñ�Ñ‚Ð°Ð²Ð»ÐµÐ½
    // Ð²
    // Ð�Ð´ÐµÐ½.
    public static final int CARGO_HAS_ARRIVED_AT_OREN_CASTLE_TOWN = 1545; // Ð“Ñ€ÑƒÐ·
    // Ð´Ð¾Ñ�Ñ‚Ð°Ð²Ð»ÐµÐ½
    // Ð²
    // ÐžÑ€ÐµÐ½.
    public static final int CARGO_HAS_ARRIVED_AT_HUNTERS_VILLAGE = 1546; // Ð“Ñ€ÑƒÐ·
    // Ð´Ð¾Ñ�Ñ‚Ð°Ð²Ð»ÐµÐ½
    // Ð²
    // Ð”ÐµÑ€ÐµÐ²Ð½ÑŽ
    // ÐžÑ…Ð¾Ñ‚Ð½Ð¸ÐºÐ¾Ð².
    public static final int CARGO_HAS_ARRIVED_AT_DION_CASTLE_TOWN = 1547; // Ð“Ñ€ÑƒÐ·
    // Ð´Ð¾Ñ�Ñ‚Ð°Ð²Ð»ÐµÐ½
    // Ð²
    // Ð”Ð¸Ð¾Ð½.
    public static final int CARGO_HAS_ARRIVED_AT_FLORAN_VILLAGE = 1548; // Ð“Ñ€ÑƒÐ·
    // Ð´Ð¾Ñ�Ñ‚Ð°Ð²Ð»ÐµÐ½
    // Ð²
    // Ð¤Ð»Ð¾Ñ€Ð°Ð½.
    public static final int CARGO_HAS_ARRIVED_AT_GLUDIN_VILLAGE = 1549; // Ð“Ñ€ÑƒÐ·
    // Ð´Ð¾Ñ�Ñ‚Ð°Ð²Ð»ÐµÐ½
    // Ð²
    // Ð“Ð»ÑƒÐ´Ð¸Ð½.
    public static final int CARGO_HAS_ARRIVED_AT_GLUDIO_CASTLE_TOWN = 1550; // Ð“Ñ€ÑƒÐ·
    // Ð´Ð¾Ñ�Ñ‚Ð°Ð²Ð»ÐµÐ½
    // Ð²
    // Ð“Ð»ÑƒÐ´Ð¸Ð¾.
    public static final int CARGO_HAS_ARRIVED_AT_GIRAN_CASTLE_TOWN = 1551; // Ð“Ñ€ÑƒÐ·
    // Ð´Ð¾Ñ�Ñ‚Ð°Ð²Ð»ÐµÐ½
    // Ð²
    // Ð“Ð¸Ñ€Ð°Ð½.
    public static final int CARGO_HAS_ARRIVED_AT_HEINE = 1552; // Ð“Ñ€ÑƒÐ· Ð´Ð¾Ñ�Ñ‚Ð°Ð²Ð»ÐµÐ½
    // Ð² Ð¥ÐµÐ¹Ð½.
    public static final int CARGO_HAS_ARRIVED_AT_RUNE_VILLAGE = 1553; // Ð“Ñ€ÑƒÐ·
    // Ð´Ð¾Ñ�Ñ‚Ð°Ð²Ð»ÐµÐ½
    // Ð² Ð ÑƒÐ½Ñƒ.
    public static final int CARGO_HAS_ARRIVED_AT_GODDARD_CASTLE_TOWN = 1554; // Ð“Ñ€ÑƒÐ·
    // Ð´Ð¾Ñ�Ñ‚Ð°Ð²Ð»ÐµÐ½
    // Ð²
    // Ð“Ð¾Ð´Ð´Ð°Ñ€Ð´.
    public static final int DO_YOU_WANT_TO_CANCEL_CHARACTER_DELETION = 1555; // Ð’Ñ‹
    // Ñ…Ð¾Ñ‚Ð¸Ñ‚Ðµ
    // Ð¾Ñ‚Ð¼ÐµÐ½Ð¸Ñ‚ÑŒ
    // ÑƒÐ´Ð°Ð»ÐµÐ½Ð¸Ðµ
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°?
    public static final int NOTICE_HAS_BEEN_SAVED = 1556; // Ð’Ð°ÑˆÐµ ÐºÐ»Ð°Ð½Ð¾Ð²Ð¾Ðµ
    // Ð¿Ñ€Ð¸Ð²ÐµÑ‚Ñ�Ñ‚Ð²Ð¸Ðµ Ð±Ñ‹Ð»Ð¾
    // Ñ�Ð¾Ñ…Ñ€Ð°Ð½ÐµÐ½Ð¾.
    public static final int SEED_PRICE_SHOULD_BE_MORE_THAN_S1_AND_LESS_THAN_S2 = 1557; // Ð¦ÐµÐ½Ð°
    // Ñ�ÐµÐ¼ÐµÐ½Ð¸:
    // $s1
    // -
    // $s2.
    public static final int THE_QUANTITY_OF_SEED_SHOULD_BE_MORE_THAN_S1_AND_LESS_THAN_S2 = 1558; // ÐšÐ¾Ð»Ð¸Ñ‡ÐµÑ�Ñ‚Ð²Ð¾
    // Ñ�ÐµÐ¼Ñ�Ð½:
    // $s1
    // -
    // $s2.
    public static final int CROP_PRICE_SHOULD_BE_MORE_THAN_S1_AND_LESS_THAN_S2 = 1559; // Ð¦ÐµÐ½Ð°
    // Ð¿Ð»Ð¾Ð´Ð¾Ð²:
    // $s1
    // -
    // $s2.
    public static final int THE_QUANTITY_OF_CROP_SHOULD_BE_MORE_THAN_S1_AND_LESS_THAN_S2_ = 1560; // ÐšÐ¾Ð»Ð¸Ñ‡ÐµÑ�Ñ‚Ð²Ð¾
    // Ð¿Ð»Ð¾Ð´Ð¾Ð²:
    // $s1
    // -
    // $s2.
    public static final int S1_CLAN_HAS_DECLARED_CLAN_WAR = 1561; // ÐšÐ»Ð°Ð½ $s1
    // Ð¾Ð±ÑŠÑ�Ð²Ð¸Ð»
    // Ð²Ð¾Ð¹Ð½Ñƒ.
    public static final int CLAN_WAR_HAS_BEEN_DECLARED_AGAINST_S1_CLAN_IF_YOU_ARE_KILLED_DURING_THE_CLAN_WAR_BY_MEMBERS_OF_THE_OPPOSING_CLAN_THE_EXPERIENCE_PENALTY_WILL_BE_REDUCED_TO_1_4_OF_NORMAL = 1562; // Ð’Ñ‹
    // Ð¾Ð±ÑŠÑ�Ð²Ð¸Ð»Ð¸
    // Ð²Ð¾Ð¹Ð½Ñƒ
    // ÐºÐ»Ð°Ð½Ñƒ
    // $s1.
    // Ð¡
    // Ñ�Ñ‚Ð¾Ð³Ð¾
    // Ð¼Ð¾Ð¼ÐµÐ½Ñ‚Ð°
    // Ð¿Ñ€Ð¸
    // Ñ�Ð¼ÐµÑ€Ñ‚Ð¸
    // Ð¾Ñ‚
    // Ð¿Ñ€Ð¾Ñ‚Ð¸Ð²Ð½Ð¸ÐºÐ¾Ð²
    // Ð¸Ð·
    // Ð´Ñ€ÑƒÐ³Ð¾Ð³Ð¾
    // ÐºÐ»Ð°Ð½Ð°
    // Ñ�Ð½Ð¸Ð¼Ð°ÐµÐ¼Ñ‹Ð¹
    // Ð¾Ð¿Ñ‹Ñ‚
    // Ñ�Ð¾Ñ�Ñ‚Ð°Ð²Ð¸Ñ‚
    // 1/4.
    public static final int S1_CLAN_CANT_MAKE_A_DECLARATION_OF_CLAN_WAR_SINCE_IT_HASNT_REACHED_THE_CLAN_LEVEL_OR_DOESNT_HAVE_ENOUGH_CLAN_MEMBERS = 1563; // Ð£
    // ÐºÐ»Ð°Ð½Ð°
    // $s1
    // Ð½ÐµÐ¿Ð¾Ð´Ñ…Ð¾Ð´Ñ�Ñ‰Ð¸Ð¹
    // ÑƒÑ€Ð¾Ð²ÐµÐ½ÑŒ
    // Ð¸Ð»Ð¸
    // Ñ�Ð»Ð¸ÑˆÐºÐ¾Ð¼
    // Ð¼Ð°Ð»Ð¾Ðµ
    // ÐºÐ¾Ð»Ð¸Ñ‡ÐµÑ�Ñ‚Ð²Ð¾
    // Ñ‡ÐµÐ»Ð¾Ð²ÐµÐº
    // Ð²
    // ÐºÐ»Ð°Ð½Ðµ.
    // ÐžÐ±ÑŠÑ�Ð²Ð¸Ñ‚ÑŒ
    // ÐµÐ¼Ñƒ
    // Ð²Ð¾Ð¹Ð½Ñƒ
    // Ð½ÐµÐ»ÑŒÐ·Ñ�.
    public static final int A_CLAN_WAR_CAN_BE_DECLARED_ONLY_IF_THE_CLAN_IS_LEVEL_THREE_OR_ABOVE_AND_THE_NUMBER_OF_CLAN_MEMBERS_IS_FIFTEEN_OR_GREATER = 1564; // Ð’Ñ‹
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¾Ð±ÑŠÑ�Ð²Ð»Ñ�Ñ‚ÑŒ
    // Ð²Ð¾Ð¹Ð½Ñƒ
    // Ñ‚Ð¾Ð»ÑŒÐºÐ¾
    // Ð²
    // Ñ‚Ð¾Ð¼
    // Ñ�Ð»ÑƒÑ‡Ð°Ðµ,
    // ÐµÑ�Ð»Ð¸
    // ÑƒÑ€Ð¾Ð²ÐµÐ½ÑŒ
    // Ð²Ð°ÑˆÐµÐ³Ð¾
    // ÐºÐ»Ð°Ð½Ð°
    // Ð±Ð¾Ð»ÑŒÑˆÐµ
    // 3
    // Ð¸
    // Ð²
    // Ð¸Ð³Ñ€Ðµ
    // Ð½Ð°Ñ…Ð¾Ð´Ñ�Ñ‚Ñ�Ñ�
    // Ð±Ð¾Ð»ÐµÐµ
    // 15
    // Ñ‡ÐµÐ»Ð¾Ð²ÐµÐº.
    public static final int THE_DECLARATION_OF_WAR_CANT_BE_MADE_BECAUSE_THE_CLAN_DOES_NOT_EXIST_OR_ACT_FOR_A_LONG_PERIOD = 1565; // Ð�ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾
    // Ð¾Ð±ÑŠÑ�Ð²Ð¸Ñ‚ÑŒ
    // Ð²Ð¾Ð¹Ð½Ñƒ
    // Ñ�Ñ‚Ð¾Ð¼Ñƒ
    // ÐºÐ»Ð°Ð½Ñƒ.
    // Ð­Ñ‚Ð¾Ð³Ð¾
    // ÐºÐ»Ð°Ð½Ð°
    // Ð½Ðµ
    // Ñ�ÑƒÑ‰ÐµÑ�Ñ‚Ð²ÑƒÐµÑ‚
    // Ð¸Ð»Ð¸
    // Ð¾Ð½
    // Ð´Ð¾Ð»Ð³Ð¾Ðµ
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ð½Ðµ
    // Ð¿Ñ€Ð¸Ð½Ð¸Ð¼Ð°ÐµÑ‚
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð¸Ñ�
    // Ð²
    // Ð¸Ð³Ñ€Ðµ.
    public static final int S1_CLAN_HAS_STOPPED_THE_WAR = 1566; // ÐšÐ»Ð°Ð½ $s1
    // Ð¿Ñ€Ð¸Ð½Ñ�Ð»
    // Ñ€ÐµÑˆÐµÐ½Ð¸Ðµ
    // Ð¾Ñ�Ñ‚Ð°Ð½Ð¾Ð²Ð¸Ñ‚ÑŒ
    // Ð²Ð¾Ð¹Ð½Ñƒ.
    public static final int THE_WAR_AGAINST_S1_CLAN_HAS_BEEN_STOPPED = 1567; // Ð’Ð¾Ð¹Ð½Ð°
    // Ñ�
    // ÐºÐ»Ð°Ð½Ð¾Ð¼
    // $s1
    // Ð±Ñ‹Ð»Ð°
    // Ð¾Ñ�Ñ‚Ð°Ð½Ð¾Ð²Ð»ÐµÐ½Ð°.
    public static final int THE_TARGET_FOR_DECLARATION_IS_WRONG = 1568; // Ð�ÐµÐ²ÐµÑ€Ð½Ð°Ñ�
    // Ñ†ÐµÐ»ÑŒ
    // Ð·Ð°Ñ�Ð²ÐºÐ¸.
    public static final int A_DECLARATION_OF_CLAN_WAR_AGAINST_AN_ALLIED_CLAN_CANT_BE_MADE = 1569; // Ð�ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾
    // Ð¾Ð±ÑŠÑ�Ð²Ð¸Ñ‚ÑŒ
    // Ð²Ð¾Ð¹Ð½Ñƒ
    // ÐºÐ»Ð°Ð½Ñƒ,
    // Ñ�Ð¾Ñ�Ñ‚Ð¾Ñ�Ñ‰ÐµÐ¼Ñƒ
    // Ñ�
    // Ð²Ð°Ð¼Ð¸
    // Ð²
    // Ð°Ð»ÑŒÑ�Ð½Ñ�Ðµ.
    public static final int A_DECLARATION_OF_WAR_AGAINST_MORE_THAN_30_CLANS_CANT_BE_MADE_AT_THE_SAME_TIME = 1570; // Ð�ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾
    // Ð¾Ð±ÑŠÑ�Ð²Ð¸Ñ‚ÑŒ
    // Ð²Ð¾Ð¹Ð½Ñƒ
    // Ð±Ð¾Ð»ÐµÐµ
    // Ñ‡ÐµÐ¼
    // 30
    // ÐºÐ»Ð°Ð½Ð°Ð¼
    // Ð¾Ð´Ð½Ð¾Ð²Ñ€ÐµÐ¼ÐµÐ½Ð½Ð¾.
    public static final int _ATTACK_LIST_ = 1571; // =======<ATTACK_LIST>=======
    public static final int _UNDER_ATTACK_LIST_ = 1572; // ======<UNDER_ATTACK_LIST>======
    public static final int THERE_IS_NO_ATTACK_CLAN = 1573; // Ð�ÐµÑ‚ ÐºÐ»Ð°Ð½Ð¾Ð²,
    // ÐºÐ¾Ñ‚Ð¾Ñ€Ñ‹Ð¼ Ð’Ñ‹
    // Ð¾Ð±ÑŠÑ�Ð²Ð¸Ð»Ð¸ Ð²Ð¾Ð¹Ð½Ñƒ.
    public static final int THERE_IS_NO_UNDER_ATTACK_CLAN = 1574; // Ð�ÐµÑ‚ ÐºÐ»Ð°Ð½Ð¾Ð²,
    // ÐºÐ¾Ñ‚Ð¾Ñ€Ñ‹Ðµ
    // Ð¾Ð±ÑŠÑ�Ð²Ð¸Ð»Ð¸
    // Ð²Ð¾Ð¹Ð½Ñƒ Ð’Ð°Ð¼.
    public static final int COMMAND_CHANNELS_CAN_ONLY_BE_FORMED_BY_A_PARTY_LEADER_WHO_IS_ALSO_THE_LEADER_OF_A_LEVEL_5_CLAN = 1575; // ÐšÐ°Ð½Ð°Ð»
    // ÐºÐ¾Ð¼Ð°Ð½Ð´Ñ‹
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ñ�Ð¾Ð·Ð´Ð°Ð½
    // Ñ‚Ð¾Ð»ÑŒÐºÐ¾
    // Ð»Ð¸Ð´ÐµÑ€Ð¾Ð¼
    // Ð³Ñ€ÑƒÐ¿Ð¿Ñ‹,
    // ÐºÐ¾Ñ‚Ð¾Ñ€Ñ‹Ð¹
    // Ñ‚Ð°ÐºÐ¶Ðµ
    // Ñ�Ð²Ð»Ñ�ÐµÑ‚Ñ�Ñ�
    // Ð³Ð»Ð°Ð²Ð¾Ð¹
    // ÐºÐ»Ð°Ð½Ð°
    // ÑƒÑ€Ð¾Ð²Ð½ÐµÐ¼
    // Ð½Ðµ
    // Ð¼ÐµÐ½ÐµÐµ
    // 5.
    public static final int PET_USES_THE_POWER_OF_SPIRIT = 1576; // ÐŸÐ¸Ñ‚Ð¾Ð¼ÐµÑ†
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·ÑƒÐµÑ‚
    // Ñ�Ð¸Ð»Ñƒ Ð´ÑƒÑ…Ð¾Ð².
    public static final int SERVITOR_USES_THE_POWER_OF_SPIRIT = 1577; // Ð¡Ð»ÑƒÐ³Ð°
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·ÑƒÐµÑ‚
    // Ñ�Ð¸Ð»Ñƒ
    // Ð´ÑƒÑ…Ð¾Ð².
    public static final int ITEMS_ARE_NOT_AVAILABLE_FOR_A_PRIVATE_STORE_OR_PRIVATE_MANUFACTURE = 1578; // Ð’
    // Ð»Ð¸Ñ‡Ð½Ð¾Ð¹
    // Ñ‚Ð¾Ñ€Ð³Ð¾Ð²Ð¾Ð¹
    // Ð»Ð°Ð²ÐºÐµ
    // Ð¸
    // Ð¼Ð°Ñ�Ñ‚ÐµÑ€Ñ�ÐºÐ¾Ð¹
    // Ð½ÐµÐ»ÑŒÐ·Ñ�
    // Ð½Ð°Ð´ÐµÐ²Ð°Ñ‚ÑŒ
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ñ‹.
    public static final int S1_PET_GAINED_S2_ADENA = 1579; // ÐŸÐ¸Ñ‚Ð¾Ð¼ÐµÑ† Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°
    // $c1 Ð¿Ð¾Ð»ÑƒÑ‡Ð¸Ð» $s2
    // Ð°Ð´ÐµÐ½.
    public static final int THE_COMMAND_CHANNEL_HAS_BEEN_FORMED = 1580; // ÐšÐ°Ð½Ð°Ð»
    // ÐºÐ¾Ð¼Ð°Ð½Ð´Ñ‹
    // Ñ�Ð¾Ð·Ð´Ð°Ð½.
    public static final int THE_COMMAND_CHANNEL_HAS_BEEN_DISBANDED = 1581; // ÐšÐ°Ð½Ð°Ð»
    // ÐºÐ¾Ð¼Ð°Ð½Ð´Ñ‹
    // Ñ€Ð°Ñ�Ñ„Ð¾Ñ€Ð¼Ð¸Ñ€Ð¾Ð²Ð°Ð½.
    public static final int YOU_HAVE_PARTICIPATED_IN_THE_COMMAND_CHANNEL = 1582; // Ð’Ñ‹
    // Ð¿Ñ€Ð¸Ñ�Ð¾ÐµÐ´Ð¸Ð½Ð¸Ð»Ð¸Ñ�ÑŒ
    // Ðº
    // ÐºÐ°Ð½Ð°Ð»Ñƒ
    // ÐºÐ¾Ð¼Ð°Ð½Ð´Ñ‹.
    public static final int YOU_WERE_DISMISSED_FROM_THE_COMMAND_CHANNEL = 1583; // Ð’Ð°Ñ�
    // Ð¸Ñ�ÐºÐ»ÑŽÑ‡Ð¸Ð»Ð¸
    // Ð¸Ð·
    // ÐºÐ°Ð½Ð°Ð»Ð°
    // ÐºÐ¾Ð¼Ð°Ð½Ð´Ñ‹.
    public static final int S1_PARTY_HAS_BEEN_DISMISSED_FROM_THE_COMMAND_CHANNEL = 1584; // Ð“Ñ€ÑƒÐ¿Ð¿Ð°
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°
    // $c1
    // Ð±Ñ‹Ð»Ð°
    // Ð¸Ñ�ÐºÐ»ÑŽÑ‡ÐµÐ½Ð°
    // Ð¸Ð·
    // ÐºÐ°Ð½Ð°Ð»Ð°
    // ÐºÐ¾Ð¼Ð°Ð½Ð´Ñ‹.
    public static final int THE_COMMAND_CHANNEL_HAS_BEEN_DEACTIVATED = 1585; // ÐšÐ°Ð½Ð°Ð»
    // ÐºÐ¾Ð¼Ð°Ð½Ð´Ñ‹
    // Ñ€Ð°Ñ�Ñ„Ð¾Ñ€Ð¼Ð¸Ñ€Ð¾Ð²Ð°Ð½.
    public static final int YOU_HAVE_QUIT_THE_COMMAND_CHANNEL = 1586; // Ð’Ñ‹
    // Ð¿Ð¾ÐºÐ¸Ð½ÑƒÐ»Ð¸
    // ÐºÐ°Ð½Ð°Ð»
    // ÐºÐ¾Ð¼Ð°Ð½Ð´Ñ‹.
    public static final int S1_PARTY_HAS_LEFT_THE_COMMAND_CHANNEL = 1587; // Ð“Ñ€ÑƒÐ¿Ð¿Ð°
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°
    // $c1
    // Ð¿Ð¾ÐºÐ¸Ð½ÑƒÐ»Ð°
    // ÐºÐ°Ð½Ð°Ð»
    // ÐºÐ¾Ð¼Ð°Ð½Ð´Ñ‹.
    public static final int THE_COMMAND_CHANNEL_IS_ACTIVATED_ONLY_IF_AT_LEAST_FIVE_PARTIES_PARTICIPATE_IN = 1588; // ÐšÐ°Ð½Ð°Ð»
    // ÐºÐ¾Ð¼Ð°Ð½Ð´Ñ‹
    // Ð°ÐºÑ‚Ð¸Ð²Ð¸Ñ€ÑƒÐµÑ‚Ñ�Ñ�
    // Ñ‚Ð¾Ð»ÑŒÐºÐ¾
    // Ð²
    // Ñ‚Ð¾Ð¼
    // Ñ�Ð»ÑƒÑ‡Ð°Ðµ,
    // ÐµÑ�Ð»Ð¸
    // Ð²
    // Ð½ÐµÐ¼
    // Ð½Ð°Ñ…Ð¾Ð´Ð¸Ñ‚Ñ�Ñ�
    // Ð±Ð¾Ð»ÐµÐµ
    // 5
    // Ð³Ñ€ÑƒÐ¿Ð¿.
    public static final int COMMAND_CHANNEL_AUTHORITY_HAS_BEEN_TRANSFERRED_TO_S1 = 1589; // ÐŸÐ¾Ð»Ð½Ð¾Ð¼Ð¾Ñ‡Ð¸Ñ�
    // Ð¿Ð¾
    // ÐºÐ°Ð½Ð°Ð»Ñƒ
    // ÐºÐ¾Ð¼Ð°Ð½Ð´Ñ‹
    // Ð¿ÐµÑ€ÐµÑˆÐ»Ð¸
    // Ðº
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ñƒ
    // $c1.
    public static final int _COMMAND_CHANNEL_INFO_TOTAL_PARTIES_S1_ = 1590; // ===<COMMAND_CHANNEL_INFO(TOTAL_PARTIES_S1)>===
    public static final int NO_USER_HAS_BEEN_INVITED_TO_THE_COMMAND_CHANNEL = 1591; // Ð�ÐµÑ‚
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶ÐµÐ¹,
    // Ð¿Ñ€Ð¸Ð³Ð»Ð°ÑˆÐµÐ½Ð½Ñ‹Ñ…
    // Ð²
    // ÐºÐ°Ð½Ð°Ð»
    // ÐºÐ¾Ð¼Ð°Ð½Ð´Ñ‹.
    public static final int YOU_CANT_OPEN_COMMAND_CHANNELS_ANY_MORE = 1592; // Ð’Ñ‹
    // Ð±Ð¾Ð»ÑŒÑˆÐµ
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ñ�Ð¾Ð·Ð´Ð°Ð²Ð°Ñ‚ÑŒ
    // ÐºÐ°Ð½Ð°Ð»
    // ÐºÐ¾Ð¼Ð°Ð½Ð´Ñ‹.
    public static final int YOU_DO_NOT_HAVE_AUTHORITY_TO_INVITE_SOMEONE_TO_THE_COMMAND_CHANNEL = 1593; // Ð£
    // Ð’Ð°Ñ�
    // Ð½ÐµÑ‚
    // Ð¿Ñ€Ð°Ð²
    // Ð¿Ñ€Ð¸Ð½Ð¸Ð¼Ð°Ñ‚ÑŒ
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶ÐµÐ¹
    // Ð²
    // ÐºÐ°Ð½Ð°Ð»
    // ÐºÐ¾Ð¼Ð°Ð½Ð´Ñ‹.
    public static final int S1_PARTY_IS_ALREADY_A_MEMBER_OF_THE_COMMAND_CHANNEL = 1594; // Ð“Ñ€ÑƒÐ¿Ð¿Ð°
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°
    // $c1
    // ÑƒÐ¶Ðµ
    // Ñ�Ð¾Ñ�Ñ‚Ð¾Ð¸Ñ‚
    // Ð²
    // ÐºÐ°Ð½Ð°Ð»Ðµ
    // ÐºÐ¾Ð¼Ð°Ð½Ð´Ñ‹.
    public static final int S1_HAS_SUCCEEDED = 1595; // Ð’Ñ‹ ÑƒÑ�Ð¿ÐµÑˆÐ½Ð¾ Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ð»Ð¸
    // ÑƒÐ¼ÐµÐ½Ð¸Ðµ $1s.
    public static final int HIT_BY_S1 = 1596; // Ð�Ð° Ð’Ð°Ñ� Ð²Ð¾Ð·Ð´ÐµÐ¹Ñ�Ñ‚Ð²Ð¾Ð²Ð°Ð»Ð¾ ÑƒÐ¼ÐµÐ½Ð¸Ðµ
    // $s1.
    public static final int S1_HAS_FAILED = 1597; // Ð’Ð°Ð¼ Ð½Ðµ ÑƒÐ´Ð°Ð»Ð¾Ñ�ÑŒ Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒ
    // ÑƒÐ¼ÐµÐ½Ð¸Ðµ $s1.
    public static final int WHEN_PET_OR_SERVITOR_IS_DEAD_SOULSHOTS_OR_SPIRITSHOTS_FOR_PET_OR_SERVITOR_ARE_NOT_AVAILABLE = 1598; // Ð•Ñ�Ð»Ð¸
    // Ð¿Ð¸Ñ‚Ð¾Ð¼ÐµÑ†/Ñ�Ð»ÑƒÐ³Ð°
    // ÑƒÐ¼ÐµÑ€,
    // Ð½ÐµÐ»ÑŒÐ·Ñ�
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒ
    // Ð—Ð°Ñ€Ñ�Ð´Ñ‹
    // Ð”ÑƒÑˆÐ¸
    // Ð¸
    // Ð”ÑƒÑ…Ð°.
    public static final int WATCHING_IS_IMPOSSIBLE_DURING_COMBAT = 1599; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ð¿ÐµÑ€ÐµÐ¹Ñ‚Ð¸
    // Ð²
    // Ñ€ÐµÐ¶Ð¸Ð¼
    // Ð·Ñ€Ð¸Ñ‚ÐµÐ»Ñ�
    // Ð²Ð¾
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ð±Ð¾Ñ�.
    public static final int TOMORROWS_ITEMS_WILL_ALL_BE_SET_TO_0__DO_YOU_WISH_TO_CONTINUE = 1600; // Ð—Ð°Ð²Ñ‚Ñ€Ð°
    // Ð²Ñ�Ðµ
    // Ñ†ÐµÐ½Ñ‹
    // Ð±ÑƒÐ´ÑƒÑ‚
    // Ð¸Ð·Ð¼ÐµÐ½ÐµÐ½Ñ‹
    // Ð½Ð°
    // 0.
    // ÐŸÑ€Ð¾Ð´Ð¾Ð»Ð¶Ð¸Ñ‚ÑŒ?
    public static final int TOMORROWS_ITEMS_WILL_ALL_BE_SET_TO_THE_SAME_VALUE_AS_TODAYS_ITEMS__DO_YOU_WISH_TO_CONTINUE = 1601; // Ð—Ð°Ð²Ñ‚Ñ€Ð°
    // Ð²Ñ�Ðµ
    // Ñ†ÐµÐ½Ñ‹
    // Ð±ÑƒÐ´ÑƒÑ‚
    // Ñ€Ð°Ð²Ð½Ñ‹
    // Ñ�ÐµÐ³Ð¾Ð´Ð½Ñ�ÑˆÐ½Ð¸Ð¼.
    // ÐŸÑ€Ð¾Ð´Ð¾Ð»Ð¶Ð¸Ñ‚ÑŒ?
    public static final int ONLY_A_PARTY_LEADER_CAN_ACCESS_THE_COMMAND_CHANNEL = 1602; // ÐžÐ±Ñ‰Ð°Ñ‚ÑŒÑ�Ñ�
    // Ð²
    // ÐºÐ°Ð½Ð°Ð»Ðµ
    // ÐºÐ¾Ð¼Ð°Ð½Ð´Ñ‹
    // Ð¼Ð¾Ð³ÑƒÑ‚
    // Ñ‚Ð¾Ð»ÑŒÐºÐ¾
    // Ð»Ð¸Ð´ÐµÑ€Ñ‹
    // Ð³Ñ€ÑƒÐ¿Ð¿.
    public static final int ONLY_CHANNEL_OPENER_CAN_GIVE_ALL_COMMAND = 1603; // Ð’Ñ�Ðµ
    // ÐºÐ¾Ð¼Ð°Ð½Ð´Ñ‹
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒ
    // Ñ‚Ð¾Ð»ÑŒÐºÐ¾
    // Ñ�Ð¾Ð·Ð´Ð°Ñ‚ÐµÐ»ÑŒ
    // ÐºÐ°Ð½Ð°Ð»Ð°.
    public static final int WHILE_DRESSED_IN_FORMAL_WEAR_YOU_CANT_USE_ITEMS_THAT_REQUIRE_ALL_SKILLS_AND_CASTING_OPERATIONS = 1604; // Ð’
    // Ñ�Ð²Ð°Ð´ÐµÐ±Ð½Ñ‹Ñ…
    // Ð½Ð°Ñ€Ñ�Ð´Ð°Ñ…
    // Ð½ÐµÐ»ÑŒÐ·Ñ�
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒ
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ñ‹,
    // ÐºÐ¾Ñ‚Ð¾Ñ€Ñ‹Ðµ
    // Ð·Ð°Ð´ÐµÐ¹Ñ�Ñ‚Ð²ÑƒÑŽÑ‚
    // Ð²Ñ�Ðµ
    // ÑƒÐ¼ÐµÐ½Ð¸Ñ�
    // Ð¸
    // Ð¼Ð°Ð³Ð¸ÑŽ.
    public static final int _HERE_YOU_CAN_BUY_ONLY_SEEDS_OF_S1_MANOR = 1605; // *Ð—Ð´ÐµÑ�ÑŒ
    // Ð¼Ð¾Ð¶Ð½Ð¾
    // ÐºÑƒÐ¿Ð¸Ñ‚ÑŒ
    // Ñ‚Ð¾Ð»ÑŒÐºÐ¾
    // Ñ�ÐµÐ¼ÐµÐ½Ð°
    // Ð²Ð»Ð°Ð´ÐµÐ½Ð¸Ñ�
    // $s1.
    public static final int YOU_HAVE_COMPLETED_THE_QUEST_FOR_3RD_OCCUPATION_CHANGE_AND_MOVED_TO_ANOTHER_CLASS_CONGRATULATIONS = 1606; // ÐŸÐ¾Ð·Ð´Ñ€Ð°Ð²Ð»Ñ�ÐµÐ¼!
    // Ð’Ñ‹
    // Ð·Ð°Ð²ÐµÑ€ÑˆÐ¸Ð»Ð¸
    // ÐºÐ²ÐµÑ�Ñ‚
    // Ð½Ð°
    // Ñ‚Ñ€ÐµÑ‚ÑŒÑŽ
    // Ñ�Ð¼ÐµÐ½Ñƒ
    // Ð¿Ñ€Ð¾Ñ„ÐµÑ�Ñ�Ð¸Ð¸.
    public static final int S1_ADENA_HAS_BEEN_PAID_FOR_PURCHASING_FEES = 1607; // Ð�Ð°Ð»Ð¾Ð³
    // Ð°Ð´ÐµÐ½
    // Ñ�
    // Ð¿Ð¾ÐºÑƒÐ¿ÐºÐ¸:
    // $s1.
    public static final int YOU_CANT_BUY_ANOTHER_CASTLE_SINCE_ADENA_IS_NOT_SUFFICIENT = 1608; // Ð�Ðµ
    // Ñ…Ð²Ð°Ñ‚Ð°ÐµÑ‚
    // Ð°Ð´ÐµÐ½,
    // Ñ‡Ñ‚Ð¾Ð±Ñ‹
    // ÐºÑƒÐ¿Ð¸Ñ‚ÑŒ
    // Ð´Ñ€ÑƒÐ³Ð¾Ð¹
    // Ð·Ð°Ð¼Ð¾Ðº.
    public static final int THE_DECLARATION_OF_WAR_HAS_BEEN_ALREADY_MADE_TO_THE_CLAN = 1609; // Ð­Ñ‚Ð¾Ñ‚
    // ÐºÐ»Ð°Ð½
    // ÑƒÐ¶Ðµ
    // Ð½Ð°Ñ…Ð¾Ð´Ð¸Ñ‚Ñ�Ñ�
    // Ð²
    // Ñ�Ð¾Ñ�Ñ‚Ð¾Ñ�Ð½Ð¸Ð¸
    // Ð²Ð¾Ð¹Ð½Ñ‹.
    public static final int FOOL_YOU_CANNOT_DECLARE_WAR_AGAINST_YOUR_OWN_CLAN = 1610; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¾Ð±ÑŠÑ�Ð²Ð¸Ñ‚ÑŒ
    // Ð²Ð¾Ð¹Ð½Ñƒ
    // Ñ�Ð²Ð¾ÐµÐ¼Ñƒ
    // Ð¶Ðµ
    // ÐºÐ»Ð°Ð½Ñƒ.
    public static final int PARTY_LEADER_S1 = 1611; // Ð›Ð¸Ð´ÐµÑ€ Ð³Ñ€ÑƒÐ¿Ð¿Ñ‹: $c1
    public static final int _WAR_LIST_ = 1612; // =====<WAR_LIST>=====
    public static final int THERE_IS_NO_CLAN_LISTED_ON_WAR_LIST = 1613; // Ð�ÐµÑ‚
    // ÐºÐ»Ð°Ð½Ð¾Ð²,
    // Ñ�Ð¾Ñ�Ñ‚Ð¾Ñ�Ñ‰Ð¸Ñ…
    // Ð²
    // Ñ�Ð¿Ð¸Ñ�ÐºÐµ
    // Ð²Ð¾Ð¹Ð½.
    public static final int YOU_ARE_PARTICIPATING_IN_THE_CHANNEL_WHICH_HAS_BEEN_ALREADY_OPENED = 1614; // Ð’Ñ‹
    // Ð¿Ñ€Ð¸Ñ�Ð¾ÐµÐ´Ð¸Ð½ÐµÐ½Ñ‹
    // Ðº
    // ÐºÐ°Ð½Ð°Ð»Ñƒ.
    public static final int THE_NUMBER_OF_REMAINING_PARTIES_IS_S1_UNTIL_A_CHANNEL_IS_ACTIVATED = 1615; // ÐžÑ�Ñ‚Ð°Ð»Ð¾Ñ�ÑŒ
    // Ð³Ñ€ÑƒÐ¿Ð¿
    // Ð´Ð¾
    // Ð°ÐºÑ‚Ð¸Ð²Ð°Ñ†Ð¸Ð¸
    // ÐºÐ°Ð½Ð°Ð»Ð°:
    // $s1.
    public static final int THE_COMMAND_CHANNEL_HAS_BEEN_ACTIVATED = 1616; // ÐšÐ°Ð½Ð°Ð»
    // ÐºÐ¾Ð¼Ð°Ð½Ð´Ñ‹
    // Ð°ÐºÑ‚Ð¸Ð²Ð¸Ñ€Ð¾Ð²Ð°Ð½.
    public static final int YOU_DO_NOT_HAVE_AUTHORITY_TO_USE_THE_COMMAND_CHANNEL = 1617; // Ð£
    // Ð²Ð°Ñ�
    // Ð½ÐµÑ‚
    // Ð¿Ñ€Ð°Ð²
    // Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ð½Ð¸Ñ�
    // ÐºÐ°Ð½Ð°Ð»Ð¾Ð¼
    // ÐºÐ¾Ð¼Ð°Ð½Ð´Ñ‹.
    public static final int THE_FERRY_FROM_RUNE_HARBOR_TO_GLUDIN_HARBOR_HAS_BEEN_DELAYED = 1618; // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ,
    // Ð¾Ñ‚Ð¿Ñ€Ð°Ð²Ð»Ñ�ÑŽÑ‰Ð¸Ð¹Ñ�Ñ�
    // Ð¸Ð·
    // Ð³Ð°Ð²Ð°Ð½Ð¸
    // Ð ÑƒÐ½Ñ‹
    // Ð²
    // Ð³Ð°Ð²Ð°Ð½ÑŒ
    // Ð“Ð»ÑƒÐ´Ð¸Ð½Ð°,
    // Ð·Ð°Ð´ÐµÑ€Ð¶Ð¸Ð²Ð°ÐµÑ‚Ñ�Ñ�.
    public static final int THE_FERRY_FROM_GLUDIN_HARBOR_TO_RUNE_HARBOR_HAS_BEEN_DELAYED = 1619; // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ,
    // Ð¾Ñ‚Ð¿Ñ€Ð°Ð²Ð»Ñ�ÑŽÑ‰Ð¸Ð¹Ñ�Ñ�
    // Ð¸Ð·
    // Ð³Ð°Ð²Ð°Ð½Ð¸
    // Ð“Ð»ÑƒÐ´Ð¸Ð½Ð°
    // Ð²
    // Ð³Ð°Ð²Ð°Ð½ÑŒ
    // Ð ÑƒÐ½Ñ‹,
    // Ð·Ð°Ð´ÐµÑ€Ð¶Ð¸Ð²Ð°ÐµÑ‚Ñ�Ñ�.
    public static final int ARRIVED_AT_RUNE_HARBOR = 1620; // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ Ð¿Ñ€Ð¸Ð±Ñ‹Ð» Ð²
    // Ð³Ð°Ð²Ð°Ð½ÑŒ Ð ÑƒÐ½Ñ‹.
    public static final int DEPARTURE_FOR_GLUDIN_HARBOR_WILL_TAKE_PLACE_IN_FIVE_MINUTES = 1621; // ÐžÑ‚Ð¿Ð»Ñ‹Ñ‚Ð¸Ðµ
    // Ð²
    // Ð ÑƒÐ½Ñƒ
    // Ñ�Ð¾Ñ�Ñ‚Ð¾Ð¸Ñ‚Ñ�Ñ�
    // Ñ‡ÐµÑ€ÐµÐ·
    // 5
    // Ð¼Ð¸Ð½ÑƒÑ‚.
    public static final int DEPARTURE_FOR_GLUDIN_HARBOR_WILL_TAKE_PLACE_IN_ONE_MINUTE = 1622; // ÐžÑ‚Ð¿Ð»Ñ‹Ñ‚Ð¸Ðµ
    // Ð²
    // Ð ÑƒÐ½Ñƒ
    // Ñ�Ð¾Ñ�Ñ‚Ð¾Ð¸Ñ‚Ñ�Ñ�
    // Ñ‡ÐµÑ€ÐµÐ·
    // 1
    // Ð¼Ð¸Ð½ÑƒÑ‚Ñƒ.
    public static final int MAKE_HASTE__WE_WILL_BE_DEPARTING_FOR_GLUDIN_HARBOR_SHORTLY = 1623; // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ
    // Ð¸Ð·
    // Ð ÑƒÐ½Ñ‹
    // Ð²
    // Ð“Ð»ÑƒÐ´Ð¸Ð½
    // Ñ�ÐºÐ¾Ñ€Ð¾
    // Ð¾Ñ‚Ð¿Ð»Ñ‹Ð²Ð°ÐµÑ‚.
    public static final int WE_ARE_NOW_DEPARTING_FOR_GLUDIN_HARBOR__HOLD_ON_AND_ENJOY_THE_RIDE = 1624; // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ
    // Ð¸Ð·
    // Ð ÑƒÐ½Ñ‹
    // Ð²
    // Ð“Ð»ÑƒÐ´Ð¸Ð½
    // Ð¾Ñ‚Ð¿Ð»Ñ‹Ð²Ð°ÐµÑ‚.
    public static final int WILL_LEAVE_FOR_RUNE_HARBOR_AFTER_ANCHORING_FOR_TEN_MINUTES = 1625; // Ð§ÐµÑ€ÐµÐ·
    // 10
    // Ð¼Ð¸Ð½ÑƒÑ‚
    // ÐºÐ¾Ñ€Ð°Ð±Ð»ÑŒ
    // Ð¾Ñ‚Ð¿Ð»Ñ‹Ð²Ð°ÐµÑ‚
    // Ð²
    // Ð³Ð°Ð²Ð°Ð½ÑŒ
    // Ð ÑƒÐ½Ñ‹.
    public static final int WILL_LEAVE_FOR_RUNE_HARBOR_IN_FIVE_MINUTES = 1626; // ÐžÑ‚Ð¿Ð»Ñ‹Ñ‚Ð¸Ðµ
    // Ð²
    // Ð³Ð°Ð²Ð°Ð½ÑŒ
    // Ð ÑƒÐ½Ñ‹
    // Ñ�Ð¾Ñ�Ñ‚Ð¾Ð¸Ñ‚Ñ�Ñ�
    // Ñ‡ÐµÑ€ÐµÐ·
    // 5
    // Ð¼Ð¸Ð½ÑƒÑ‚.
    public static final int WILL_LEAVE_FOR_RUNE_HARBOR_IN_ONE_MINUTE = 1627; // ÐžÑ‚Ð¿Ð»Ñ‹Ñ‚Ð¸Ðµ
    // Ð²
    // Ð³Ð°Ð²Ð°Ð½ÑŒ
    // Ð ÑƒÐ½Ñ‹
    // Ñ�Ð¾Ñ�Ñ‚Ð¾Ð¸Ñ‚Ñ�Ñ�
    // Ñ‡ÐµÑ€ÐµÐ·
    // 1
    // Ð¼Ð¸Ð½ÑƒÑ‚Ñƒ.
    public static final int LEAVING_SOON_FOR_RUNE_HARBOR = 1628; // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ Ð¸Ð·
    // Ð³Ð°Ð²Ð°Ð½Ð¸ Ð ÑƒÐ½Ñ‹
    // Ð² Ð³Ð°Ð²Ð°Ð½ÑŒ
    // Ð“Ð»ÑƒÐ´Ð¸Ð½Ð°
    // Ñ�ÐºÐ¾Ñ€Ð¾
    // Ð¾Ñ‚Ð¿Ð»Ñ‹Ð²Ð°ÐµÑ‚.
    public static final int LEAVING_FOR_RUNE_HARBOR = 1629; // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ Ð¸Ð· Ð³Ð°Ð²Ð°Ð½Ð¸
    // Ð ÑƒÐ½Ñ‹ Ð² Ð³Ð°Ð²Ð°Ð½ÑŒ
    // Ð“Ð»ÑƒÐ´Ð¸Ð½Ð°
    // Ð¾Ñ‚Ð¿Ð»Ñ‹Ð²Ð°ÐµÑ‚.
    public static final int THE_FERRY_FROM_RUNE_HARBOR_WILL_BE_ARRIVING_AT_GLUDIN_HARBOR_IN_APPROXIMATELY_15_MINUTES = 1630; // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ,
    // Ð¾Ñ‚Ð¿Ð»Ñ‹Ð²ÑˆÐ¸Ð¹
    // Ð¸Ð·
    // Ð³Ð°Ð²Ð°Ð½Ð¸
    // Ð ÑƒÐ½Ñ‹,
    // Ð¿Ñ€Ð¸Ð±ÑƒÐ´ÐµÑ‚
    // Ð²
    // Ð³Ð°Ð²Ð°Ð½ÑŒ
    // Ð“Ð»ÑƒÐ´Ð¸Ð½Ð°
    // Ñ‡ÐµÑ€ÐµÐ·
    // 15
    // Ð¼Ð¸Ð½ÑƒÑ‚.
    public static final int THE_FERRY_FROM_RUNE_HARBOR_WILL_BE_ARRIVING_AT_GLUDIN_HARBOR_IN_APPROXIMATELY_10_MINUTES = 1631; // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ,
    // Ð¾Ñ‚Ð¿Ð»Ñ‹Ð²ÑˆÐ¸Ð¹
    // Ð¸Ð·
    // Ð³Ð°Ð²Ð°Ð½Ð¸
    // Ð ÑƒÐ½Ñ‹,
    // Ð¿Ñ€Ð¸Ð±ÑƒÐ´ÐµÑ‚
    // Ð²
    // Ð³Ð°Ð²Ð°Ð½ÑŒ
    // Ð“Ð»ÑƒÐ´Ð¸Ð½Ð°
    // Ñ‡ÐµÑ€ÐµÐ·
    // 10
    // Ð¼Ð¸Ð½ÑƒÑ‚.
    public static final int THE_FERRY_FROM_RUNE_HARBOR_WILL_BE_ARRIVING_AT_GLUDIN_HARBOR_IN_APPROXIMATELY_5_MINUTES = 1632; // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ,
    // Ð¾Ñ‚Ð¿Ð»Ñ‹Ð²ÑˆÐ¸Ð¹
    // Ð¸Ð·
    // Ð³Ð°Ð²Ð°Ð½Ð¸
    // Ð ÑƒÐ½Ñ‹,
    // Ð¿Ñ€Ð¸Ð±ÑƒÐ´ÐµÑ‚
    // Ð²
    // Ð³Ð°Ð²Ð°Ð½ÑŒ
    // Ð“Ð»ÑƒÐ´Ð¸Ð½Ð°
    // Ñ‡ÐµÑ€ÐµÐ·
    // 5
    // Ð¼Ð¸Ð½ÑƒÑ‚.
    public static final int THE_FERRY_FROM_RUNE_HARBOR_WILL_BE_ARRIVING_AT_GLUDIN_HARBOR_IN_APPROXIMATELY_1_MINUTE = 1633; // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ,
    // Ð¾Ñ‚Ð¿Ð»Ñ‹Ð²ÑˆÐ¸Ð¹
    // Ð¸Ð·
    // Ð³Ð°Ð²Ð°Ð½Ð¸
    // Ð ÑƒÐ½Ñ‹,
    // Ð¿Ñ€Ð¸Ð±ÑƒÐ´ÐµÑ‚
    // Ð²
    // Ð³Ð°Ð²Ð°Ð½ÑŒ
    // Ð“Ð»ÑƒÐ´Ð¸Ð½Ð°
    // Ñ‡ÐµÑ€ÐµÐ·
    // 1
    // Ð¼Ð¸Ð½ÑƒÑ‚Ñƒ.
    public static final int THE_FERRY_FROM_GLUDIN_HARBOR_WILL_BE_ARRIVING_AT_RUNE_HARBOR_IN_APPROXIMATELY_15_MINUTES = 1634; // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ,
    // Ð¾Ñ‚Ð¿Ð»Ñ‹Ð²ÑˆÐ¸Ð¹
    // Ð¸Ð·
    // Ð³Ð°Ð²Ð°Ð½Ð¸
    // Ð ÑƒÐ½Ñ‹,
    // Ð¿Ñ€Ð¸Ð±ÑƒÐ´ÐµÑ‚
    // Ð²
    // Ð³Ð°Ð²Ð°Ð½ÑŒ
    // Ð“Ð»ÑƒÐ´Ð¸Ð½Ð°
    // Ñ‡ÐµÑ€ÐµÐ·
    // 15
    // Ð¼Ð¸Ð½ÑƒÑ‚.
    public static final int THE_FERRY_FROM_GLUDIN_HARBOR_WILL_BE_ARRIVING_AT_RUNE_HARBOR_IN_APPROXIMATELY_10_MINUTES = 1635; // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ,
    // Ð¾Ñ‚Ð¿Ð»Ñ‹Ð²ÑˆÐ¸Ð¹
    // Ð¸Ð·
    // Ð³Ð°Ð²Ð°Ð½Ð¸
    // Ð ÑƒÐ½Ñ‹,
    // Ð¿Ñ€Ð¸Ð±ÑƒÐ´ÐµÑ‚
    // Ð²
    // Ð³Ð°Ð²Ð°Ð½ÑŒ
    // Ð“Ð»ÑƒÐ´Ð¸Ð½Ð°
    // Ñ‡ÐµÑ€ÐµÐ·
    // 10
    // Ð¼Ð¸Ð½ÑƒÑ‚.
    public static final int THE_FERRY_FROM_GLUDIN_HARBOR_WILL_BE_ARRIVING_AT_RUNE_HARBOR_IN_APPROXIMATELY_5_MINUTES = 1636; // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ,
    // Ð¾Ñ‚Ð¿Ð»Ñ‹Ð²ÑˆÐ¸Ð¹
    // Ð¸Ð·
    // Ð³Ð°Ð²Ð°Ð½Ð¸
    // Ð ÑƒÐ½Ñ‹,
    // Ð¿Ñ€Ð¸Ð±ÑƒÐ´ÐµÑ‚
    // Ð²
    // Ð³Ð°Ð²Ð°Ð½ÑŒ
    // Ð“Ð»ÑƒÐ´Ð¸Ð½Ð°
    // Ñ‡ÐµÑ€ÐµÐ·
    // 5
    // Ð¼Ð¸Ð½ÑƒÑ‚.
    public static final int THE_FERRY_FROM_GLUDIN_HARBOR_WILL_BE_ARRIVING_AT_RUNE_HARBOR_IN_APPROXIMATELY_1_MINUTE = 1637; // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ,
    // Ð¾Ñ‚Ð¿Ð»Ñ‹Ð²ÑˆÐ¸Ð¹
    // Ð¸Ð·
    // Ð³Ð°Ð²Ð°Ð½Ð¸
    // Ð ÑƒÐ½Ñ‹,
    // Ð¿Ñ€Ð¸Ð±ÑƒÐ´ÐµÑ‚
    // Ð²
    // Ð³Ð°Ð²Ð°Ð½ÑŒ
    // Ð“Ð»ÑƒÐ´Ð¸Ð½Ð°
    // Ñ‡ÐµÑ€ÐµÐ·
    // 1
    // Ð¼Ð¸Ð½ÑƒÑ‚Ñƒ.
    public static final int YOU_CANNOT_FISH_WHILE_USING_A_RECIPE_BOOK_PRIVATE_MANUFACTURE_OR_PRIVATE_STORE = 1638; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ñ€Ñ‹Ð±Ð°Ñ‡Ð¸Ñ‚ÑŒ
    // Ð²Ð¾
    // Ñ€ÐµÐ¶Ð¸Ð¼Ð°Ñ…
    // Ð»Ð¸Ñ‡Ð½Ð¾Ð¹
    // Ñ‚Ð¾Ñ€Ð³Ð¾Ð²Ð¾Ð¹
    // Ð»Ð°Ð²ÐºÐ¸,
    // Ð¼Ð°Ñ�Ñ‚ÐµÑ€Ñ�ÐºÐ¾Ð¹
    // Ð¸
    // ÐºÐ½Ð¸Ð³Ð¸
    // Ñ€ÐµÑ†ÐµÐ¿Ñ‚Ð¾Ð².
    public static final int OLYMPIAD_PERIOD_S1_HAS_STARTED = 1639; // $s1 Ð¿ÐµÑ€Ð¸Ð¾Ð´
    // ÐžÐ»Ð¸Ð¼Ð¿Ð¸Ð°Ð´Ñ‹
    // Ð½Ð°Ñ‡Ð°Ð»Ñ�Ñ�.
    public static final int OLYMPIAD_PERIOD_S1_HAS_ENDED = 1640; // $s1 Ð¿ÐµÑ€Ð¸Ð¾Ð´
    // ÐžÐ»Ð¸Ð¼Ð¿Ð¸Ð°Ð´Ñ‹
    // Ð·Ð°ÐºÐ¾Ð½Ñ‡Ð¸Ð»Ñ�Ñ�.
    public static final int THE_OLYMPIAD_GAME_HAS_STARTED = 1641; // ÐžÐ»Ð¸Ð¼Ð¿Ð¸Ð°Ð´Ð°
    // Ð½Ð°Ñ‡Ð°Ð»Ð°Ñ�ÑŒ.
    public static final int THE_OLYMPIAD_GAME_HAS_ENDED = 1642; // ÐžÐ»Ð¸Ð¼Ð¿Ð¸Ð°Ð´Ð°
    // Ð¾ÐºÐ¾Ð½Ñ‡ÐµÐ½Ð°.
    public static final int CURRENT_LOCATION_S1_S2_S3_DIMENSION_GAP = 1643; // Ð¢ÐµÐºÑƒÑ‰ÐµÐµ
    // Ð¼ÐµÑ�Ñ‚Ð¾Ð¿Ð¾Ð»Ð¾Ð¶ÐµÐ½Ð¸Ðµ:
    // $s1,
    // $s2,
    // $s3
    // (Ð Ð°Ð·Ð»Ð¾Ð¼
    // ÐœÐµÐ¶Ð´Ñƒ
    // ÐœÐ¸Ñ€Ð°Ð¼Ð¸)
    public static final int NONE_1644 = 1644; // Ð’Ñ‹ Ð¸Ð³Ñ€Ð°ÐµÑ‚Ðµ: $s1 Ñ‡ $s2 Ð¼Ð¸Ð½.
    // Ð�ÐµÐ¾Ð±Ñ…Ð¾Ð´Ð¸Ð¼Ð¾ Ð¾Ñ‚Ð´Ð¾Ñ…Ð½ÑƒÑ‚ÑŒ: $s3 Ñ‡ $s4
    // Ð¼Ð¸Ð½.
    public static final int NONE_1645 = 1645; // Ð•Ñ�Ð»Ð¸ Ð’Ñ‹ Ð½Ð°Ñ…Ð¾Ð´Ð¸Ñ‚ÐµÑ�ÑŒ Ð² Ð¸Ð³Ñ€Ðµ
    // Ð±Ð¾Ð»ÑŒÑˆÐµ 3 Ñ‡, Ð’Ð°Ñ� Ð¾Ð¶Ð¸Ð´Ð°ÐµÑ‚
    // Ð½Ð°ÐºÐ°Ð·Ð°Ð½Ð¸Ðµ, Ð¿Ð¾Ñ�Ñ‚Ð¾Ð¼Ñƒ Ð²Ñ‹Ð¹Ð´Ð¸Ñ‚Ðµ Ð¸Ð·
    // Ð¸Ð³Ñ€Ñ‹ Ð¸ Ð¾Ñ‚Ð´Ð¾Ñ…Ð½Ð¸Ñ‚Ðµ Ð½ÐµÐ¼Ð½Ð¾Ð³Ð¾.
    public static final int NONE_1646 = 1646; // Ð•Ñ�Ð»Ð¸ Ð’Ñ‹ Ð² Ð¸Ð³Ñ€Ðµ Ð±Ð¾Ð»ÑŒÑˆÐµ 3 Ñ‡,
    // Ð¿Ð¾Ð»ÑƒÑ‡Ð°ÐµÐ¼Ñ‹Ð¹ Ð¾Ð¿Ñ‹Ñ‚ Ð¸ ÑˆÐ°Ð½Ñ�
    // Ð²Ñ‹Ð¿Ð°Ð´ÐµÐ½Ð¸Ñ� Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ð¾Ð² Ñ�Ð¾ÐºÑ€Ð°Ñ‰Ð°ÑŽÑ‚Ñ�Ñ�
    // Ð²Ð´Ð²Ð¾Ðµ, Ð¿Ð¾Ñ�Ñ‚Ð¾Ð¼Ñƒ Ð²Ñ‹Ð¹Ð´Ð¸Ñ‚Ðµ Ð¸Ð· Ð¸Ð³Ñ€Ñ‹
    // Ð¸ Ð¾Ñ‚Ð´Ð¾Ñ…Ð½Ð¸Ñ‚Ðµ Ð½ÐµÐ¼Ð½Ð¾Ð³Ð¾.
    public static final int NONE_1647 = 1647; // Ð•Ñ�Ð»Ð¸ Ð’Ñ‹ Ð² Ð¸Ð³Ñ€Ðµ Ð±Ð¾Ð»ÑŒÑˆÐµ 5 Ñ‡, Ñ‚Ð¾
    // Ð¿ÐµÑ€ÐµÑ�Ñ‚Ð°ÐµÑ‚Ðµ Ð¿Ð¾Ð»ÑƒÑ‡Ð°Ñ‚ÑŒ Ð¾Ð¿Ñ‹Ñ‚ Ð¸
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ñ‹, Ð¿Ð¾Ñ�Ñ‚Ð¾Ð¼Ñƒ Ð²Ñ‹Ð¹Ð´Ð¸Ñ‚Ðµ Ð¸Ð·
    // Ð¸Ð³Ñ€Ñ‹ Ð¸ Ð¾Ñ‚Ð´Ð¾Ñ…Ð½Ð¸Ñ‚Ðµ Ð½ÐµÐ¼Ð½Ð¾Ð³Ð¾.
    public static final int NONE_1648 = 1648; // ÐŸÑ€Ð¸ Ð½Ð°Ñ…Ð¾Ð¶Ð´ÐµÐ½Ð¸Ð¸ Ð² Ð¼Ð¸Ñ€Ð½Ð¾Ð¹ Ð·Ð¾Ð½Ðµ
    // Ð²Ñ€ÐµÐ¼Ñ� Ð²Ñ�Ðµ Ñ€Ð°Ð²Ð½Ð¾ Ð¸Ð´ÐµÑ‚.
    public static final int PLAY_TIME_IS_NOW_ACCUMULATING = 1649; // Ð˜Ð´ÐµÑ‚
    // Ð¿Ð¾Ð´Ñ�Ñ‡ÐµÑ‚
    // Ð¸Ð³Ñ€Ð¾Ð²Ð¾Ð³Ð¾
    // Ð²Ñ€ÐµÐ¼ÐµÐ½Ð¸.
    public static final int DUE_TO_A_LARGE_NUMBER_OF_USERS_CURRENTLY_ACCESSING_OUR_SERVER_YOUR_LOGIN_ATTEMPT_HAS_FAILED_PLEASE_WAIT_A_LITTLE_WHILE_AND_ATTEMPT_TO_LOG_IN_AGAIN = 1650; // Ð¡ÐµÑ€Ð²ÐµÑ€
    // Ð¿ÐµÑ€ÐµÐ¿Ð¾Ð»Ð½ÐµÐ½.
    // ÐŸÐ¾Ð¶Ð°Ð»ÑƒÐ¹Ñ�Ñ‚Ð°,
    // Ð¿Ð¾Ð¿Ñ€Ð¾Ð±ÑƒÐ¹Ñ‚Ðµ
    // Ð·Ð°Ð¹Ñ‚Ð¸
    // Ð¿Ð¾Ð·Ð¶Ðµ.
    public static final int THE_OLYMPIAD_GAME_IS_NOT_CURRENTLY_IN_PROGRESS = 1651; // Ð�Ð°
    // Ð´Ð°Ð½Ð½Ñ‹Ð¹
    // Ð¼Ð¾Ð¼ÐµÐ½Ñ‚
    // ÐžÐ»Ð¸Ð¼Ð¿Ð¸Ð°Ð´Ð°
    // Ð½Ðµ
    // Ð²ÐµÐ´ÐµÑ‚Ñ�Ñ�.
    public static final int THE_VIDEO_RECORDING_OF_THE_REPLAY_WILL_NOW_BEGIN = 1652; // Ð’Ñ‹
    // Ð½Ð°Ñ‡Ð°Ð»Ð¸
    // Ð·Ð°Ð¿Ð¸Ñ�ÑŒ
    // Ð¸Ð³Ñ€Ñ‹.
    public static final int THE_REPLAY_FILE_HAS_BEEN_STORED_SUCCESSFULLY_S1 = 1653; // Ð—Ð°Ð¿Ð¸Ñ�Ð°Ð½Ð½Ñ‹Ð¹
    // Ñ„Ð°Ð¹Ð»
    // ÑƒÑ�Ð¿ÐµÑˆÐ½Ð¾
    // Ñ�Ð¾Ñ…Ñ€Ð°Ð½ÐµÐ½
    // ($s1).
    public static final int THE_ATTEMPT_TO_RECORD_THE_REPLAY_FILE_HAS_FAILED = 1654; // Ð�Ðµ
    // ÑƒÐ´Ð°Ð»Ð¾Ñ�ÑŒ
    // Ð·Ð°Ð¿Ð¸Ñ�Ð°Ñ‚ÑŒ
    // Ð¸Ð³Ñ€Ñƒ.
    public static final int YOU_HAVE_CAUGHT_A_MONSTER = 1655; // Ð’Ñ‹ Ð¿Ð¾Ð¹Ð¼Ð°Ð»Ð¸
    // Ð¼Ð¾Ð½Ñ�Ñ‚Ñ€Ð°!
    public static final int YOU_HAVE_SUCCESSFULLY_TRADED_THE_ITEM_WITH_THE_NPC = 1656; // ÐžÐ±Ð¼ÐµÐ½
    // Ñ�
    // NPC
    // ÑƒÑ�Ð¿ÐµÑˆÐ½Ð¾
    // Ð·Ð°Ð²ÐµÑ€ÑˆÐµÐ½.
    public static final int C1_HAS_EARNED_S2_POINTS_IN_THE_GRAND_OLYMPIAD_GAMES = 1657; // $c1
    // -
    // Ð¿Ð¾Ð»ÑƒÑ‡ÐµÐ½Ð¾
    // Ð±Ð°Ð»Ð»Ð¾Ð²
    // ÐžÐ»Ð¸Ð¼Ð¿Ð¸Ð°Ð´Ñ‹:
    // $s2.
    public static final int C1_HAS_LOST_S2_POINTS_IN_THE_GRAND_OLYMPIAD_GAMES = 1658; // $c1
    // -
    // Ð¿Ð¾Ñ‚ÐµÑ€Ñ�Ð½Ð¾
    // Ð±Ð°Ð»Ð»Ð¾Ð²
    // ÐžÐ»Ð¸Ð¼Ð¿Ð¸Ð°Ð´Ñ‹:
    // $s2.
    public static final int CURRENT_LOCATION_S1_S2_S3_CEMETERY_OF_THE_EMPIRE = 1659; // Ð¢ÐµÐºÑƒÑ‰ÐµÐµ
    // Ð¼ÐµÑ�Ñ‚Ð¾Ð¿Ð¾Ð»Ð¾Ð¶ÐµÐ½Ð¸Ðµ:
    // $s1,
    // $s2,
    // $s3
    // (ÐšÐ»Ð°Ð´Ð±Ð¸Ñ‰Ðµ
    // Ð˜Ð¼Ð¿ÐµÑ€Ð¸Ð¸)
    public static final int THE_CHANNEL_WAS_OPENED_BY_S1 = 1660; // Ð¡Ð¾Ð·Ð´Ð°Ñ‚ÐµÐ»ÑŒ
    // ÐºÐ°Ð½Ð°Ð»Ð°: $c1.
    public static final int S1_HAS_OBTAINED_S3_S2S = 1661; // $c1 Ð¿Ð¾Ð»ÑƒÑ‡Ð°ÐµÑ‚ $s2
    // ($s3 ÑˆÑ‚.)
    public static final int IF_YOU_FISH_IN_ONE_SPOT_FOR_A_LONG_TIME_THE_SUCCESS_RATE_OF_A_FISH_TAKING_THE_BAIT_BECOMES_LOWER__PLEASE_MOVE_TO_ANOTHER_PLACE_AND_CONTINUE_YOUR_FISHING_THERE = 1662; // Ð¨Ð°Ð½Ñ�
    // Ð¿Ð¾Ð¹Ð¼Ð°Ñ‚ÑŒ
    // Ñ€Ñ‹Ð±Ñƒ
    // ÑƒÐ¼ÐµÐ½ÑŒÑˆÐ°ÐµÑ‚Ñ�Ñ�,
    // ÐµÑ�Ð»Ð¸
    // Ð¾Ñ�Ñ‚Ð°Ð²Ð°Ñ‚ÑŒÑ�Ñ�
    // Ð½Ð°
    // Ð¾Ð´Ð½Ð¾Ð¼
    // Ð¼ÐµÑ�Ñ‚Ðµ.
    // ÐŸÐ¾Ð¿Ñ€Ð¾Ð±ÑƒÐ¹Ñ‚Ðµ
    // Ð¿Ð¾Ñ€Ñ‹Ð±Ð°Ñ‡Ð¸Ñ‚ÑŒ
    // Ð²
    // Ð´Ñ€ÑƒÐ³Ð¾Ð¼
    // Ð¼ÐµÑ�Ñ‚Ðµ.
    public static final int THE_CLANS_EMBLEM_WAS_SUCCESSFULLY_REGISTERED__ONLY_A_CLAN_THAT_OWNS_A_CLAN_HALL_OR_A_CASTLE_CAN_GET_THEIR_EMBLEM_DISPLAYED_ON_CLAN_RELATED_ITEMS = 1663; // Ð­Ð¼Ð±Ð»ÐµÐ¼Ð°
    // ÐºÐ»Ð°Ð½Ð°
    // Ð±Ñ‹Ð»Ð°
    // ÑƒÑ�Ð¿ÐµÑˆÐ½Ð¾
    // Ð·Ð°Ñ€ÐµÐ³Ð¸Ñ�Ñ‚Ñ€Ð¸Ñ€Ð¾Ð²Ð°Ð½Ð°.
    public static final int BECAUSE_THE_FISH_IS_RESISTING_THE_FLOAT_IS_BOBBING_UP_AND_DOWN_A_LOT = 1664; // Ð Ñ‹Ð±Ð°
    // Ñ�Ð¾Ð¿Ñ€Ð¾Ñ‚Ð¸Ð²Ð»Ñ�ÐµÑ‚Ñ�Ñ�,
    // Ð¸
    // Ð¿Ð¾Ð¿Ð»Ð°Ð²Ð¾Ðº
    // Ñ�Ð¸Ð»ÑŒÐ½Ð¾
    // Ð´ÐµÑ€Ð³Ð°ÐµÑ‚Ñ�Ñ�.
    public static final int SINCE_THE_FISH_IS_EXHAUSTED_THE_FLOAT_IS_MOVING_ONLY_SLIGHTLY = 1665; // Ð Ñ‹Ð±Ð°
    // ÑƒÑ�Ñ‚Ð°Ð»Ð°
    // Ð¸
    // Ð¿Ð¾Ñ‡Ñ‚Ð¸
    // Ð½Ðµ
    // Ñ�Ð¾Ð¿Ñ€Ð¾Ñ‚Ð¸Ð²Ð»Ñ�ÐµÑ‚Ñ�Ñ�.
    public static final int YOU_HAVE_OBTAINED__S1_S2 = 1666; // Ð’Ñ‹ Ð¿Ð¾Ð»ÑƒÑ‡Ð¸Ð»Ð¸ +$s1
    // $s2.
    public static final int LETHAL_STRIKE = 1667; // Ð¡Ð¼ÐµÑ€Ñ‚ÐµÐ»ÑŒÐ½Ñ‹Ð¹ ÑƒÐ´Ð°Ñ€!
    public static final int YOUR_LETHAL_STRIKE_WAS_SUCCESSFUL = 1668; // Ð’Ð°Ñˆ
    // Ñ�Ð¼ÐµÑ€Ñ‚ÐµÐ»ÑŒÐ½Ñ‹Ð¹
    // ÑƒÐ´Ð°Ñ€
    // Ð¿Ñ€Ð¾ÑˆÐµÐ»
    // ÑƒÑ�Ð¿ÐµÑˆÐ½Ð¾!
    public static final int THERE_WAS_NOTHING_FOUND_INSIDE_OF_THAT = 1669; // Ð˜Ð·Ð¼ÐµÐ½ÐµÐ½Ð¸Ðµ
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ð°
    // Ð½Ðµ
    // ÑƒÐ´Ð°Ð»Ð¾Ñ�ÑŒ.
    public static final int SINCE_THE_SKILL_LEVEL_OF_REELING_PUMPING_IS_HIGHER_THAN_THE_LEVEL_OF_YOUR_FISHING_MASTERY_A_PENALTY_OF_S1_WILL_BE_APPLIED = 1670; // Ð£Ð¼ÐµÐ½Ð¸Ðµ
    // Â«ÐŸÐ¾Ð´Ñ�ÐµÑ‡ÑŒÂ»
    // (Â«ÐŸÐ¾Ð´Ñ‚Ñ�Ð½ÑƒÑ‚ÑŒÂ»)
    // Ð½Ð°
    // 3
    // Ð¸Ð»Ð¸
    // Ð±Ð¾Ð»ÐµÐµ
    // ÑƒÑ€Ð¾Ð²Ð½Ñ�
    // Ð²Ñ‹ÑˆÐµ
    // ÑƒÐ¼ÐµÐ½Ð¸Ñ�
    // Ñ€Ñ‹Ð±Ð½Ð¾Ð¹
    // Ð»Ð¾Ð²Ð»Ð¸.
    public static final int YOUR_REELING_WAS_SUCCESSFUL_MASTERY_PENALTYS1_ = 1671; // Ð’Ð°ÑˆÐµ
    // Ð¿Ð¾Ð´Ñ�ÐµÐºÐ°Ð½Ð¸Ðµ
    // Ð¿Ñ€Ð¾ÑˆÐ»Ð¾
    // ÑƒÑ�Ð¿ÐµÑˆÐ½Ð¾!
    // (ÑˆÑ‚Ñ€Ð°Ñ„:
    // $
    // s1)
    public static final int YOUR_PUMPING_WAS_SUCCESSFUL_MASTERY_PENALTYS1_ = 1672; // Ð’Ð°ÑˆÐµ
    // Ð¿Ð¾Ð´Ñ‚Ñ�Ð³Ð¸Ð²Ð°Ð½Ð¸Ðµ
    // Ð¿Ñ€Ð¾ÑˆÐ»Ð¾
    // ÑƒÑ�Ð¿ÐµÑˆÐ½Ð¾!
    // (ÑˆÑ‚Ñ€Ð°Ñ„:
    // $
    // s1)
    public static final int THE_CURRENT_FOR_THIS_OLYMPIAD_IS_S1_WINS_S2_DEFEATS_S3_YOU_HAVE_EARNED_S4_OLYMPIAD_POINTS = 1673; // Ð’Ð°Ñˆ
    // Ñ€ÐµÐ·ÑƒÐ»ÑŒÑ‚Ð°Ñ‚
    // Ð²
    // ÐžÐ»Ð¸Ð¼Ð¿Ð¸Ð°Ð´Ðµ:
    // Ð¼Ð°Ñ‚Ñ‡ÐµÐ¹
    // -
    // $s1,
    // Ð¿Ð¾Ð±ÐµÐ´
    // -
    // $s2,
    // Ð¿Ð¾Ñ€Ð°Ð¶ÐµÐ½Ð¸Ð¹
    // -
    // $s3.
    // ÐŸÐ¾Ð»ÑƒÑ‡ÐµÐ½Ð¾
    // Ð±Ð°Ð»Ð»Ð¾Ð²
    // ÐžÐ»Ð¸Ð¼Ð¿Ð¸Ð°Ð´Ñ‹
    // -
    // $s4.
    public static final int THIS_COMMAND_CAN_ONLY_BE_USED_BY_A_NOBLESSE = 1674; // Ð­Ñ‚Ñƒ
    // ÐºÐ¾Ð¼Ð°Ð½Ð´Ñƒ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒ
    // Ñ‚Ð¾Ð»ÑŒÐºÐ¾
    // Ð´Ð²Ð¾Ñ€Ñ�Ð½Ð¸Ð½.
    public static final int A_MANOR_CANNOT_BE_SET_UP_BETWEEN_6_AM_AND_8_PM = 1675; // Ð’Ð»Ð°Ð´ÐµÐ½Ð¸Ðµ
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // ÑƒÑ�Ñ‚Ð°Ð½Ð¾Ð²Ð»ÐµÐ½Ð¾
    // c
    // 6:00
    // Ð´Ð¾
    // 20:00.
    public static final int SINCE_A_SERVITOR_OR_A_PET_DOES_NOT_EXIST_AUTOMATIC_USE_IS_NOT_APPLICABLE = 1676; // Ð£
    // Ð’Ð°Ñ�
    // Ð½ÐµÑ‚
    // Ñ�Ð»ÑƒÐ³Ð¸
    // Ð¸Ð»Ð¸
    // Ð¿Ð¸Ñ‚Ð¾Ð¼Ñ†Ð°.
    // Ð�Ð²Ñ‚Ð¾Ð¼Ð°Ñ‚Ð¸Ñ‡ÐµÑ�ÐºÐ¸Ðµ
    // Ñ„ÑƒÐ½ÐºÑ†Ð¸Ð¸
    // Ð½ÐµÐ´Ð¾Ñ�Ñ‚ÑƒÐ¿Ð½Ñ‹.
    public static final int A_CEASE_FIRE_DURING_A_CLAN_WAR_CAN_NOT_BE_CALLED_WHILE_MEMBERS_OF_YOUR_CLAN_ARE_ENGAGED_IN_BATTLE = 1677; // Ð�ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾
    // Ð¾Ñ‚Ð¼ÐµÐ½Ð¸Ñ‚ÑŒ
    // Ð²Ð¾Ð¹Ð½Ñƒ
    // Ð¸Ð·-Ð·Ð°
    // Ñ‚Ð¾Ð³Ð¾,
    // Ñ‡Ñ‚Ð¾
    // Ð²
    // Ð´Ð°Ð½Ð½Ñ‹Ð¹
    // Ð¼Ð¾Ð¼ÐµÐ½Ñ‚
    // Ñ‡Ð»ÐµÐ½Ñ‹
    // ÐºÐ»Ð°Ð½Ð°
    // Ð²Ð¾Ð²Ð»ÐµÑ‡ÐµÐ½Ñ‹
    // Ð²
    // Ð±Ð¸Ñ‚Ð²Ñƒ.
    public static final int YOU_HAVE_NOT_DECLARED_A_CLAN_WAR_TO_S1_CLAN = 1678; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¾Ð±ÑŠÑ�Ð²Ð»Ñ�Ð»Ð¸
    // Ð²Ð¾Ð¹Ð½Ñƒ
    // ÐºÐ»Ð°Ð½Ñƒ
    // $s1.
    public static final int ONLY_THE_CREATOR_OF_A_CHANNEL_CAN_ISSUE_A_GLOBAL_COMMAND = 1679; // Ð’Ñ�Ðµ
    // ÐºÐ¾Ð¼Ð°Ð½Ð´Ñ‹
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒ
    // Ñ‚Ð¾Ð»ÑŒÐºÐ¾
    // Ñ�Ð¾Ð·Ð´Ð°Ñ‚ÐµÐ»ÑŒ
    // ÐºÐ°Ð½Ð°Ð»Ð°.
    public static final int S1_HAS_DECLINED_THE_CHANNEL_INVITATION = 1680; // $c1
    // Ð¾Ñ‚ÐºÐ»Ð¾Ð½Ñ�ÐµÑ‚
    // Ð¿Ñ€Ð¸Ð³Ð»Ð°ÑˆÐµÐ½Ð¸Ðµ
    // Ð²
    // ÐºÐ°Ð½Ð°Ð».
    public static final int SINCE_S1_DID_NOT_RESPOND_YOUR_CHANNEL_INVITATION_HAS_FAILED = 1681; // $c1
    // Ð½Ðµ
    // Ð¾Ñ‚Ð²ÐµÑ‡Ð°ÐµÑ‚.
    // ÐŸÑ€Ð¸Ð³Ð»Ð°ÑˆÐµÐ½Ð¸Ðµ
    // Ð²
    // ÐºÐ°Ð½Ð°Ð»
    // Ð½Ðµ
    // ÑƒÐ´Ð°Ð»Ð¾Ñ�ÑŒ.
    public static final int ONLY_THE_CREATOR_OF_A_CHANNEL_CAN_USE_THE_CHANNEL_DISMISS_COMMAND = 1682; // Ð˜Ñ�ÐºÐ»ÑŽÑ‡Ð¸Ñ‚ÑŒ
    // Ð¸Ð·
    // ÐºÐ°Ð½Ð°Ð»Ð°
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ñ‚Ð¾Ð»ÑŒÐºÐ¾
    // Ñ�Ð¾Ð·Ð´Ð°Ñ‚ÐµÐ»ÑŒ
    // ÐºÐ°Ð½Ð°Ð»Ð°.
    public static final int ONLY_A_PARTY_LEADER_CAN_CHOOSE_THE_OPTION_TO_LEAVE_A_CHANNEL = 1683; // ÐŸÐ¾ÐºÐ¸Ð½ÑƒÑ‚ÑŒ
    // ÐºÐ°Ð½Ð°Ð»
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ñ‚Ð¾Ð»ÑŒÐºÐ¾
    // Ð»Ð¸Ð´ÐµÑ€
    // Ð³Ñ€ÑƒÐ¿Ð¿Ñ‹.
    public static final int WHILE_A_CLAN_IS_BEING_DISSOLVED_IT_IS_IMPOSSIBLE_TO_DECLARE_A_CLAN_WAR_AGAINST_IT = 1684; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ð¾Ð±ÑŠÑ�Ð²Ð»Ñ�Ñ‚ÑŒ
    // Ð²Ð¾Ð¹Ð½Ñƒ,
    // ÐµÑ�Ð»Ð¸
    // Ð²Ð°Ñˆ
    // ÐºÐ»Ð°Ð½
    // Ð½Ð°Ñ…Ð¾Ð´Ð¸Ñ‚Ñ�Ñ�
    // Ð²
    // Ñ�Ñ‚Ð°Ð´Ð¸Ð¸
    // Ñ€Ð°Ñ�Ñ„Ð¾Ñ€Ð¼Ð¸Ñ€Ð¾Ð²Ð°Ð½Ð¸Ñ�.
    public static final int IF_YOUR_PK_COUNT_IS_1_OR_MORE_YOU_ARE_NOT_ALLOWED_TO_WEAR_THIS_ITEM = 1685; // Ð�ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾
    // Ð½Ð°Ð´ÐµÑ‚ÑŒ
    // Ñ�Ñ‚Ð¾Ñ‚
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚,
    // ÐµÑ�Ð»Ð¸
    // PK
    // >
    // 0.
    public static final int THE_CASTLE_WALL_HAS_SUSTAINED_DAMAGE = 1686; // Ð¡Ñ‚ÐµÐ½Ñ‹
    // Ð·Ð°Ð¼ÐºÐ°
    // Ñ€Ð°Ð·Ñ€ÑƒÑˆÐµÐ½Ñ‹.
    public static final int THIS_AREA_CANNOT_BE_ENTERED_WHILE_MOUNTED_ATOP_OF_A_WYVERN_YOU_WILL_BE_DISMOUNTED_FROM_YOUR_WYVERN_IF_YOU_DO_NOT_LEAVE = 1687; // Ð’
    // Ñ�Ñ‚Ð¾Ð¹
    // Ð·Ð¾Ð½Ðµ
    // Ð½ÐµÐ»ÑŒÐ·Ñ�
    // Ð»ÐµÑ‚Ð°Ñ‚ÑŒ
    // Ð½Ð°
    // Ð²Ð¸Ð²ÐµÑ€Ð½Ðµ.
    // Ð•Ñ�Ð»Ð¸
    // Ð²Ñ‹
    // Ð¾Ñ�Ñ‚Ð°Ð½ÐµÑ‚ÐµÑ�ÑŒ
    // Ð½Ð°
    // Ñ�Ñ‚Ð¾Ð¹
    // Ñ‚ÐµÑ€Ñ€Ð¸Ñ‚Ð¾Ñ€Ð¸Ð¸,
    // Ð²Ð°Ð¼
    // Ð½ÐµÐ¾Ð±Ñ…Ð¾Ð´Ð¸Ð¼Ð¾
    // Ñ�Ð¿ÐµÑˆÐ¸Ñ‚ÑŒÑ�Ñ�
    // Ñ�
    // Ð²Ð¸Ð²ÐµÑ€Ð½Ñ‹.
    public static final int YOU_CANNOT_PRACTICE_ENCHANTING_WHILE_OPERATING_A_PRIVATE_STORE_OR_PRIVATE_MANUFACTURING_WORKSHOP = 1688; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // ÑƒÐ»ÑƒÑ‡ÑˆÐ°Ñ‚ÑŒ
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ñ‹
    // Ð²Ð¾
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ñ‚Ð¾Ñ€Ð³Ð¾Ð²Ð»Ð¸.
    public static final int YOU_ARE_ALREADY_ON_THE_WAITING_LIST_TO_PARTICIPATE_IN_THE_GAME_FOR_YOUR_CLASS = 1689; // $c1
    // ÑƒÐ¶Ðµ
    // Ñ�Ð¾Ñ�Ñ‚Ð¾Ð¸Ñ‚
    // Ð²
    // Ñ�Ð¿Ð¸Ñ�ÐºÐµ
    // Ð¾Ð¶Ð¸Ð´Ð°ÑŽÑ‰Ð¸Ñ…
    // Ñ�Ð¾Ñ€ÐµÐ²Ð½Ð¾Ð²Ð°Ð½Ð¸Ñ�
    // Ð¼ÐµÐ¶Ð´Ñƒ
    // Ð¿Ñ€ÐµÐ´Ñ�Ñ‚Ð°Ð²Ð¸Ñ‚ÐµÐ»Ñ�Ð¼Ð¸
    // Ð¾Ð´Ð½Ð¾Ð¹
    // Ð¿Ñ€Ð¾Ñ„ÐµÑ�Ñ�Ð¸Ð¸.
    public static final int YOU_ARE_ALREADY_ON_THE_WAITING_LIST_FOR_ALL_CLASSES_WAITING_TO_PARTICIPATE_IN_THE_GAME = 1690; // $c1
    // ÑƒÐ¶Ðµ
    // Ñ�Ð¾Ñ�Ñ‚Ð¾Ð¸Ñ‚
    // Ð²
    // Ñ�Ð¿Ð¸Ñ�ÐºÐµ
    // Ð¾Ð¶Ð¸Ð´Ð°ÑŽÑ‰Ð¸Ñ…
    // Ð²Ð½ÐµÐºÐ»Ð°Ñ�Ñ�Ð¾Ð²Ñ‹Ðµ
    // Ñ�Ð¾Ñ€ÐµÐ²Ð½Ð¾Ð²Ð°Ð½Ð¸Ñ�.
    public static final int SINCE_80_PERCENT_OR_MORE_OF_YOUR_INVENTORY_SLOTS_ARE_FULL_YOU_CANNOT_PARTICIPATE_IN_THE_OLYMPIAD = 1691; // $c1
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð²Ð¾Ð²Ð°Ñ‚ÑŒ
    // Ð²
    // ÐžÐ»Ð¸Ð¼Ð¿Ð¸Ð°Ð´Ðµ,
    // Ñ‚Ð°Ðº
    // ÐºÐ°Ðº
    // Ñ�Ñ‡ÐµÐ¹ÐºÐ¸
    // Ð¸Ð½Ð²ÐµÐ½Ñ‚Ð°Ñ€Ñ�
    // Ð·Ð°Ð¿Ð¾Ð»Ð½ÐµÐ½Ñ‹
    // Ð½Ð°
    // 80%.
    public static final int SINCE_YOU_HAVE_CHANGED_YOUR_CLASS_INTO_A_SUB_JOB_YOU_CANNOT_PARTICIPATE_IN_THE_OLYMPIAD = 1692; // $c1
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð²Ð¾Ð²Ð°Ñ‚ÑŒ
    // Ð²
    // ÐžÐ»Ð¸Ð¼Ð¿Ð¸Ð°Ð´Ðµ,
    // Ñ‚Ð°Ðº
    // ÐºÐ°Ðº
    // ÐºÐ»Ð°Ñ�Ñ�
    // Ð¸Ð·Ð¼ÐµÐ½ÐµÐ½
    // Ð½Ð°
    // Ð¿Ð¾Ð´ÐºÐ»Ð°Ñ�Ñ�.
    public static final int WHILE_YOU_ARE_ON_THE_WAITING_LIST_YOU_ARE_NOT_ALLOWED_TO_WATCH_THE_GAME = 1693; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð½Ð°Ð±Ð»ÑŽÐ´Ð°Ñ‚ÑŒ
    // Ð·Ð°
    // ÐžÐ»Ð¸Ð¼Ð¿Ð¸Ð°Ð´Ð¾Ð¹,
    // ÐºÐ¾Ð³Ð´Ð°
    // Ð²Ñ‹
    // Ñ�Ð¾Ñ�Ñ‚Ð¾Ð¸Ñ‚Ðµ
    // Ð²
    // Ñ�Ð¿Ð¸Ñ�ÐºÐµ
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð½Ð¸ÐºÐ¾Ð².
    public static final int ONLY_A_CLAN_LEADER_THAT_IS_A_NOBLESSE_CAN_VIEW_THE_SIEGE_WAR_STATUS_WINDOW_DURING_A_SIEGE_WAR = 1694; // Ð’Ð¾
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ð²Ð¾Ð¹Ð½Ñ‹
    // Ñ�Ñ‚Ð¾
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒ
    // Ñ‚Ð¾Ð»ÑŒÐºÐ¾
    // Ð³Ð»Ð°Ð²Ð°
    // ÐºÐ»Ð°Ð½Ð°
    // Ð¸
    // Ñ‚Ð¾Ð»ÑŒÐºÐ¾
    // ÐµÑ�Ð»Ð¸
    // Ð¾Ð½
    // Ñ�Ð²Ð»Ñ�ÐµÑ‚Ñ�Ñ�
    // Ð´Ð²Ð¾Ñ€Ñ�Ð½Ð¸Ð½Ð¾Ð¼.
    public static final int IT_CAN_BE_USED_ONLY_WHILE_A_SIEGE_WAR_IS_TAKING_PLACE = 1695; // Ð’Ñ‹
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒ
    // Ñ�Ñ‚Ð¾
    // Ñ‚Ð¾Ð»ÑŒÐºÐ¾
    // Ð¿Ñ€Ð¸
    // Ð¾Ñ�Ð°Ð´Ðµ
    // Ð·Ð°Ð¼ÐºÐ°.
    public static final int IF_THE_ACCUMULATED_ONLINE_ACCESS_TIME_IS_S1_OR_MORE_A_PENALTY_WILL_BE_IMPOSED__PLEASE_TERMINATE_YOUR_SESSION_AND_TAKE_A_BREAK = 1696; // none
    public static final int SINCE_YOUR_CUMULATIVE_ACCESS_TIME_HAS_EXCEEDED_S1_YOUR_EXP_AND_ITEM_DROP_RATE_WERE_REDUCED_BY_HALF_PLEASE_TERMINATE_YOUR_SESSION_AND_TAKE_A_BREAK = 1697; // none
    public static final int SINCE_YOUR_CUMULATIVE_ACCESS_TIME_HAS_EXCEEDED_S1_YOU_NO_LONGER_HAVE_EXP_OR_ITEM_DROP_PRIVILEGE__PLEASE_TERMINATE_YOUR_SESSION_AND_TAKE_A_BREAK = 1698; // none
    public static final int YOU_CANNOT_DISMISS_A_PARTY_MEMBER_BY_FORCE = 1699; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¸Ñ�ÐºÐ»ÑŽÑ‡Ð¸Ñ‚ÑŒ
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°
    // Ð¸Ð·
    // Ð³Ñ€ÑƒÐ¿Ð¿Ñ‹.
    public static final int YOU_DONT_HAVE_ENOUGH_SPIRITSHOTS_NEEDED_FOR_A_PET_SERVITOR = 1700; // Ð�Ðµ
    // Ñ…Ð²Ð°Ñ‚Ð°ÐµÑ‚
    // Ð·Ð°Ñ€Ñ�Ð´Ð¾Ð²
    // Ð´ÑƒÑ…Ð°
    // Ð´Ð»Ñ�
    // Ð¿Ð¸Ñ‚Ð¾Ð¼Ñ†Ð°/Ñ�Ð»ÑƒÐ³Ð¸.
    public static final int YOU_DONT_HAVE_ENOUGH_SOULSHOTS_NEEDED_FOR_A_PET_SERVITOR = 1701; // Ð�Ðµ
    // Ñ…Ð²Ð°Ñ‚Ð°ÐµÑ‚
    // Ð·Ð°Ñ€Ñ�Ð´Ð¾Ð²
    // Ð´ÑƒÑˆÐ¸
    // Ð´Ð»Ñ�
    // Ð¿Ð¸Ñ‚Ð¾Ð¼Ñ†Ð°/Ñ�Ð»ÑƒÐ³Ð¸.
    public static final int THE_USER_WHO_CONDUCTED_A_SEARCH_A_MOMENT_AGO_HAS_BEEN_CONFIRMED_TO_BE_A_BOT_USER = 1702; // ÐŸÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶
    // $s1
    // Ð¿Ñ€Ð¾Ð²ÐµÑ€ÐµÐ½
    // Ð¸
    // Ð¿Ñ€Ð¸Ð·Ð½Ð°Ð½
    // Ð±Ð¾Ñ‚Ð¾Ð¼.
    public static final int THE_USER_WHO_CONDUCTED_A_SEARCH_A_MOMENT_AGO_HAS_BEEN_CONFIRMED_TO_BE_A_NONBOT_USER = 1703; // ÐŸÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶
    // $s1
    // Ð¿Ñ€Ð¾Ð²ÐµÑ€ÐµÐ½
    // -
    // Ð±Ð¾Ñ‚
    // Ð½Ðµ
    // Ð²Ñ‹Ñ�Ð²Ð»ÐµÐ½.
    public static final int PLEASE_CLOSE_THE_SETUP_WINDOW_FOR_A_PRIVATE_MANUFACTURING_STORE_OR_THE_SETUP_WINDOW_FOR_A_PRIVATE_STORE_AND_TRY_AGAIN = 1704; // ÐŸÐ¾Ð¶Ð°Ð»ÑƒÐ¹Ñ�Ñ‚Ð°,
    // Ð·Ð°ÐºÑ€Ð¾Ð¹Ñ‚Ðµ
    // Ð¾ÐºÐ½Ð¾
    // Ð½Ð°Ñ�Ñ‚Ñ€Ð¾ÐµÐº
    // Ñ‡Ð°Ñ�Ñ‚Ð½Ð¾Ð¹
    // Ñ‚Ð¾Ñ€Ð³Ð¾Ð²Ð¾Ð¹
    // Ð»Ð°Ð²ÐºÐ¸/Ð¼Ð°Ñ�Ñ‚ÐµÑ€Ñ�ÐºÐ¾Ð¹
    // Ð¸
    // Ð¿Ð¾Ð²Ñ‚Ð¾Ñ€Ð¸Ñ‚Ðµ
    // Ð¿Ð¾Ð¿Ñ‹Ñ‚ÐºÑƒ.
    // Pc Bang Points
    public static final int PC_BANG_POINTS_ACQUISITION_PERIOD_PONTS_ACQUISITION_PERIOD_LEFT_S1_HOUR = 1705; // ÐŸÐµÑ€Ð¸Ð¾Ð´
    // Ð¿Ñ€Ð¸Ð¾Ð±Ñ€ÐµÑ‚ÐµÐ½Ð¸Ñ�
    // Ð¾Ñ‡ÐºÐ¾Ð²
    // PC
    // Bang.
    // ÐžÐ½
    // Ð¿Ñ€Ð¾Ð´Ð»Ð¸Ñ‚Ñ�Ñ�
    // $s1
    // Ñ‡.
    public static final int PC_BANG_POINTS_USE_PERIOD_POINTS_USE_PERIOD_LEFT_S1_HOUR = 1706; // ÐŸÐµÑ€Ð¸Ð¾Ð´
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ð½Ð¸Ñ�
    // Ð¾Ñ‡ÐºÐ¾Ð²
    // PC
    // Bang.
    // ÐžÐ½
    // Ð¿Ñ€Ð¾Ð´Ð»Ð¸Ñ‚Ñ�Ñ�
    // $s1
    // Ñ‡.
    public static final int YOU_ACQUIRED_S1_PC_BANG_POINT = 1707; // ÐŸÐ¾Ð»ÑƒÑ‡ÐµÐ½Ð¾
    // Ð¾Ñ‡ÐºÐ¾Ð² PC
    // Bang: $s1.
    public static final int DOUBLE_POINTS_YOU_AQUIRED_S1_PC_BANG_POINT = 1708; // ÐžÑ‡ÐºÐ¸
    // ÑƒÐ´Ð²Ð°Ð¸Ð²Ð°ÑŽÑ‚Ñ�Ñ�!
    // ÐŸÐ¾Ð»ÑƒÑ‡ÐµÐ½Ð¾
    // Ð¾Ñ‡ÐºÐ¾Ð²
    // PC
    // Bang:
    // $s1.
    public static final int YOU_ARE_USING_S1_POINT = 1709; // Ð˜Ñ�Ð¿Ð¾Ð»ÑŒÐ·ÑƒÐµÑ‚Ñ�Ñ�
    // Ð¾Ñ‡ÐºÐ¾Ð²: $s1.
    public static final int YOU_ARE_SHORT_OF_ACCUMULATED_POINTS = 1710; // Ð£ Ð’Ð°Ñ�
    // Ð½ÐµÐ´Ð¾Ñ�Ñ‚Ð°Ñ‚Ð¾Ñ‡Ð½Ð¾
    // Ð¾Ñ‡ÐºÐ¾Ð².
    public static final int PC_BANG_POINTS_USE_PERIOD_HAS_EXPIRED = 1711; // ÐŸÐµÑ€Ð¸Ð¾Ð´
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ð½Ð¸Ñ�
    // Ð¾Ñ‡ÐºÐ¾Ð²
    // PC
    // Bang
    // Ð¸Ñ�Ñ‚ÐµÐº.
    public static final int THE_PC_BANG_POINTS_ACCUMULATION_PERIOD_HAS_EXPIRED = 1712; // ÐŸÐµÑ€Ð¸Ð¾Ð´
    // Ð¿Ñ€Ð¸Ð¾Ð±Ñ€ÐµÑ‚ÐµÐ½Ð¸Ñ�
    // Ð¾Ñ‡ÐºÐ¾Ð²
    // PC
    // Bang
    // Ð¸Ñ�Ñ‚ÐµÐº.
    public static final int THE_MATCH_MAY_BE_DELAYED_DUE_TO_NOT_ENOUGH_COMBATANTS = 1713; // Ð˜Ð·-Ð·Ð°
    // Ð½ÐµÑ…Ð²Ð°Ñ‚ÐºÐ¸
    // Ð¿Ñ€Ð¾Ñ‚Ð¸Ð²Ð½Ð¸ÐºÐ¾Ð²
    // Ñ�Ð¾Ñ€ÐµÐ²Ð½Ð¾Ð²Ð°Ð½Ð¸Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð½Ð°Ñ‡Ð°Ñ‚ÑŒÑ�Ñ�
    // Ð¿Ð¾Ð·Ð´Ð½ÐµÐµ.
    public static final int THIS_IS_A_PEACEFUL_ZONE__N__PVP_IS_NOT_ALLOWED_IN_THIS_AREA = 1715; // ÐœÐ¸Ñ€Ð½Ð°Ñ�
    // Ð—Ð¾Ð½Ð°
    // \\n-
    // PvP
    // Ð·Ð°Ð¿Ñ€ÐµÑ‰ÐµÐ½Ð¾.
    public static final int ALTERED_ZONE = 1716; // Ð˜Ð·Ð¼ÐµÐ½ÐµÐ½Ð½Ð°Ñ� Ð—Ð¾Ð½Ð°
    public static final int SIEGE_WAR_ZONE___N__A_SIEGE_IS_CURRENTLY_IN_PROGRESS_IN_THIS_AREA____N_IF_A_CHARACTER_DIES_IN = 1717; // Ð—Ð¾Ð½Ð°
    // ÐžÑ�Ð°Ð´Ñ‹
    // Ð—Ð°Ð¼ÐºÐ°
    // \\n-
    // Ð¡ÐµÐ¹Ñ‡Ð°Ñ�
    // Ð¿Ñ€Ð¾Ñ…Ð¾Ð´Ð¸Ñ‚
    // Ð¾Ñ�Ð°Ð´Ð°
    // Ð·Ð°Ð¼ÐºÐ°.
    // \\n-
    // Ð˜Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ð½Ð¸Ðµ
    // ÑƒÐ¼ÐµÐ½Ð¸Ñ�
    // Ð²Ð¾Ñ�ÐºÑ€ÐµÑˆÐµÐ½Ð¸Ñ�
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð¾Ð³Ñ€Ð°Ð½Ð¸Ñ‡ÐµÐ½Ð¾.
    public static final int GENERAL_FIELD = 1718; // ÐžÐ±Ñ‹Ñ‡Ð½Ð°Ñ� Ñ‚ÐµÑ€Ñ€Ð¸Ñ‚Ð¾Ñ€Ð¸Ñ�
    public static final int SEVEN_SIGNS_ZONE___N__ALTHOUGH_A_CHARACTER_S_LEVEL_MAY_INCREASE_WHILE_IN_THIS_AREA_HP_AND_MP___N = 1719; // Ð—Ð¾Ð½Ð°
    // Ð¡ÐµÐ¼Ð¸
    // ÐŸÐµÑ‡Ð°Ñ‚ÐµÐ¹\\n-
    // Ð’Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾
    // Ð¿Ð¾Ð´Ð½Ñ�Ñ‚ÑŒ
    // ÑƒÑ€Ð¾Ð²ÐµÐ½ÑŒ,
    // Ð½Ð¾
    // HP
    // Ð¸
    // MP\\n
    // Ð½Ðµ
    // Ð²Ð¾Ñ�Ñ�Ñ‚Ð°Ð½Ð°Ð²Ð»Ð¸Ð²Ð°ÑŽÑ‚Ñ�Ñ�.
    public static final int ___ = 1720; // ---
    public static final int COMBAT_ZONE = 1721; // Ð‘Ð¾ÐµÐ²Ð°Ñ� Ð—Ð¾Ð½Ð°
    public static final int PLEASE_ENTER_THE_NAME_OF_THE_ITEM_YOU_WISH_TO_SEARCH_FOR = 1722; // Ð’Ð²ÐµÐ´Ð¸Ñ‚Ðµ
    // Ð½Ð°Ð·Ð²Ð°Ð½Ð¸Ðµ
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ð°,
    // ÐºÐ¾Ñ‚Ð¾Ñ€Ñ‹Ð¹
    // Ð²Ñ‹
    // Ñ…Ð¾Ñ‚Ð¸Ñ‚Ðµ
    // Ð½Ð°Ð¹Ñ‚Ð¸.
    public static final int PLEASE_TAKE_A_MOMENT_TO_PROVIDE_FEEDBACK_ABOUT_THE_PETITION_SERVICE = 1723; // ÐŸÐ¾Ð¶Ð°Ð»ÑƒÐ¹Ñ�Ñ‚Ð°,
    // Ð¿Ñ€Ð¾ÐºÐ¾Ð¼Ð¼ÐµÐ½Ñ‚Ð¸Ñ€ÑƒÐ¹Ñ‚Ðµ
    // ÐºÐ°Ñ‡ÐµÑ�Ñ‚Ð²Ð¾
    // Ð¾Ð±Ñ�Ð»ÑƒÐ¶Ð¸Ð²Ð°Ð½Ð¸Ñ�
    // Ñ�Ð»ÑƒÐ¶Ð±Ñ‹
    // Ð¿ÐµÑ‚Ð¸Ñ†Ð¸Ð¹.
    public static final int A_SERVITOR_WHOM_IS_ENGAGED_IN_BATTLE_CANNOT_BE_DE_ACTIVATED = 1724; // Ð•Ñ�Ð»Ð¸
    // Ñ�Ð»ÑƒÐ³Ð°
    // Ð²Ð¾Ð²Ð»ÐµÑ‡ÐµÐ½
    // Ð²
    // Ð±Ð¸Ñ‚Ð²Ñƒ,
    // Ñ‚Ð¾
    // ÐµÐ³Ð¾
    // Ð½ÐµÐ»ÑŒÐ·Ñ�
    // Ð¾Ñ‚Ð¾Ð·Ð²Ð°Ñ‚ÑŒ.
    public static final int YOU_HAVE_EARNED_S1_RAID_POINTS = 1725; // ÐŸÐ¾Ð»ÑƒÑ‡ÐµÐ½Ð¾
    // Ñ€ÐµÐ¹Ð´Ð¾Ð²Ñ‹Ñ…
    // Ð¾Ñ‡ÐºÐ¾Ð²:
    // $s1.
    public static final int S1_HAS_DISAPPEARED_BECAUSE_ITS_TIME_PERIOD_HAS_EXPIRED = 1726; // $s1
    // Ð¸Ñ�Ñ‡ÐµÐ·Ð°ÐµÑ‚
    // Ð¸Ð·-Ð·Ð°
    // Ð¾ÐºÐ¾Ð½Ñ‡Ð°Ð½Ð¸Ñ�
    // Ñ�Ñ€Ð¾ÐºÐ°
    // Ð´ÐµÐ¹Ñ�Ñ‚Ð²Ð¸Ñ�.
    public static final int C1_HAS_INVITED_YOU_TO_A_PARTY_ROOM_DO_YOU_ACCEPT = 1727; // $c1
    // Ð¿Ñ€Ð¸Ð³Ð»Ð°ÑˆÐ°ÐµÑ‚
    // Ð’Ð°Ñ�
    // Ð²
    // ÐºÐ¾Ð¼Ð½Ð°Ñ‚Ñƒ
    // Ð³Ñ€ÑƒÐ¿Ð¿Ñ‹.
    // ÐŸÑ€Ð¸Ð½Ñ�Ñ‚ÑŒ?
    public static final int THE_RECIPIENT_OF_YOUR_INVITATION_DID_NOT_ACCEPT_THE_PARTY_MATCHING_INVITATION = 1728; // Ð�ÐµÑ‚
    // Ð¾Ñ‚Ð²ÐµÑ‚Ð°.
    // Ð’Ð°ÑˆÐµ
    // Ð¿Ñ€Ð¸Ð³Ð»Ð°ÑˆÐµÐ½Ð¸Ðµ
    // Ð²
    // ÐºÐ°Ð½Ð°Ð»
    // Ð¿Ð¾Ð¸Ñ�ÐºÐ°
    // Ð³Ñ€ÑƒÐ¿Ð¿Ñ‹
    // Ð±Ñ‹Ð»Ð¾
    // Ð¾Ñ‚Ð¼ÐµÐ½ÐµÐ½Ð¾.
    public static final int YOU_CANNOT_JOIN_A_COMMAND_CHANNEL_WHILE_TELEPORTING = 1729; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¿Ñ€Ð¸Ñ�Ð¾ÐµÐ´Ð¸Ð½Ð¸Ñ‚ÑŒÑ�Ñ�
    // Ðº
    // ÐºÐ°Ð½Ð°Ð»Ñƒ
    // ÐºÐ¾Ð¼Ð°Ð½Ð´Ñ‹
    // Ð²Ð¾
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ñ‚ÐµÐ»ÐµÐ¿Ð¾Ñ€Ñ‚Ð°Ñ†Ð¸Ð¸.
    public static final int TO_ESTABLISH_A_CLAN_ACADEMY_YOUR_CLAN_MUST_BE_LEVEL_5_OR_HIGHER = 1730; // Ð”Ð»Ñ�
    // Ñ�Ð¾Ð·Ð´Ð°Ð½Ð¸Ñ�
    // Ð°ÐºÐ°Ð´ÐµÐ¼Ð¸Ð¸
    // ÐºÐ»Ð°Ð½Ð°
    // Ð½ÑƒÐ¶Ð½Ð¾,
    // Ñ‡Ñ‚Ð¾Ð±Ñ‹
    // ÑƒÑ€Ð¾Ð²ÐµÐ½ÑŒ
    // ÐºÐ»Ð°Ð½Ð°
    // Ð±Ñ‹Ð»
    // Ð½Ðµ
    // Ð¼ÐµÐ½ÑŒÑˆÐµ
    // 5.
    public static final int ONLY_THE_CLAN_LEADER_CAN_CREATE_A_CLAN_ACADEMY = 1731; // Ð�ÐºÐ°Ð´ÐµÐ¼Ð¸ÑŽ
    // ÐºÐ»Ð°Ð½Ð°
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ñ�Ð¾Ð·Ð´Ð°Ñ‚ÑŒ
    // Ñ‚Ð¾Ð»ÑŒÐºÐ¾
    // Ð³Ð»Ð°Ð²Ð°
    // ÐºÐ»Ð°Ð½Ð°.
    public static final int TO_CREATE_A_CLAN_ACADEMY_A_BLOOD_MARK_IS_NEEDED = 1732; // Ð”Ð»Ñ�
    // Ñ�Ð¾Ð·Ð´Ð°Ð½Ð¸Ñ�
    // Ð°ÐºÐ°Ð´ÐµÐ¼Ð¸Ð¸
    // Ð½ÑƒÐ¶ÐµÐ½
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚
    // ÐœÐµÑ‚ÐºÐ°
    // ÐšÑ€Ð¾Ð²Ð¸.
    public static final int YOU_DO_NOT_HAVE_ENOUGH_ADENA_TO_CREATE_A_CLAN_ACADEMY = 1733; // Ð�Ðµ
    // Ñ…Ð²Ð°Ñ‚Ð°ÐµÑ‚
    // Ð°Ð´ÐµÐ½
    // Ð´Ð»Ñ�
    // Ñ�Ð¾Ð·Ð´Ð°Ð½Ð¸Ñ�
    // Ð°ÐºÐ°Ð´ÐµÐ¼Ð¸Ð¸
    // ÐºÐ»Ð°Ð½Ð°.
    public static final int TO_JOIN_A_CLAN_ACADEMY_CHARACTERS_MUST_BE_LEVEL_40_OR_BELOW_NOT_BELONG_ANOTHER_CLAN_AND_NOT_YET_COMPLETED_THEIR_2ND_CLASS_TRANSFER = 1734; // Ð”Ð»Ñ�
    // Ð²Ñ�Ñ‚ÑƒÐ¿Ð»ÐµÐ½Ð¸Ñ�
    // Ð²
    // Ð°ÐºÐ°Ð´ÐµÐ¼Ð¸ÑŽ
    // ÐºÐ»Ð°Ð½Ð°
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð¸
    // Ð´Ð¾Ð»Ð¶Ð½Ñ‹
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð¼ÐµÐ½ÐµÐµ
    // 40
    // ÑƒÑ€Ð¾Ð²Ð½Ñ�
    // Ð¸
    // Ð½Ðµ
    // Ð¸Ð¼ÐµÑ‚ÑŒ
    // Ð²Ñ‚Ð¾Ñ€Ð¾Ð¹
    // Ð¿Ñ€Ð¾Ñ„ÐµÑ�Ñ�Ð¸Ð¸.
    public static final int S1_DOES_NOT_MEET_THE_REQUIREMENTS_TO_JOIN_A_CLAN_ACADEMY = 1735; // $s1
    // Ð½Ðµ
    // Ñ�Ð¾Ð¾Ñ‚Ð²ÐµÑ‚Ñ�Ñ‚Ð²ÑƒÐµÑ‚
    // ÑƒÑ�Ð»Ð¾Ð²Ð¸Ñ�Ð¼
    // Ð²Ñ�Ñ‚ÑƒÐ¿Ð»ÐµÐ½Ð¸Ñ�
    // Ð²
    // Ð�ÐºÐ°Ð´ÐµÐ¼Ð¸ÑŽ
    // ÐºÐ»Ð°Ð½Ð°.
    public static final int THE_CLAN_ACADEMY_HAS_REACHED_ITS_MAXIMUM_ENROLLMENT = 1736; // ÐœÐ°ÐºÑ�Ð¸Ð¼Ð°Ð»ÑŒÐ½Ð¾
    // Ð´Ð¾Ð¿ÑƒÑ�Ñ‚Ð¸Ð¼Ð¾Ðµ
    // ÐºÐ¾Ð»-Ð²Ð¾
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶ÐµÐ¹
    // Ð²
    // Ð°ÐºÐ°Ð´ÐµÐ¼Ð¸Ð¸
    // Ð´Ð¾Ñ�Ñ‚Ð¸Ð³Ð½ÑƒÑ‚Ð¾.
    // Ð‘Ð¾Ð»ÑŒÑˆÐµ
    // Ð½ÐµÐ»ÑŒÐ·Ñ�
    // Ð¿Ñ€Ð¸Ð½Ñ�Ñ‚ÑŒ
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶ÐµÐ¹
    // Ð²
    // Ð°ÐºÐ°Ð´ÐµÐ¼Ð¸ÑŽ.
    public static final int YOUR_CLAN_HAS_NOT_ESTABLISHED_A_CLAN_ACADEMY_BUT_IS_ELIGIBLE_TO_DO_SO = 1737; // Ð’
    // Ð’Ð°ÑˆÐµÐ¼
    // ÐºÐ»Ð°Ð½Ðµ
    // Ð½ÐµÑ‚
    // Ð°ÐºÐ°Ð´ÐµÐ¼Ð¸Ð¸
    // ÐºÐ»Ð°Ð½Ð°.
    // Ð’Ñ‹
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ñ�Ð¾Ð·Ð´Ð°Ñ‚ÑŒ
    // Ð°ÐºÐ°Ð´ÐµÐ¼Ð¸ÑŽ.
    public static final int YOUR_CLAN_HAS_ALREADY_ESTABLISHED_A_CLAN_ACADEMY = 1738; // Ð’
    // Ð’Ð°ÑˆÐµÐ¼
    // ÐºÐ»Ð°Ð½Ðµ
    // ÑƒÐ¶Ðµ
    // Ð¸Ð¼ÐµÐµÑ‚Ñ�Ñ�
    // Ð°ÐºÐ°Ð´ÐµÐ¼Ð¸Ñ�
    // ÐºÐ»Ð°Ð½Ð°.
    public static final int WOULD_YOU_LIKE_TO_CREATE_A_CLAN_ACADEMY = 1739; // Ð’Ñ‹
    // Ð¶ÐµÐ»Ð°ÐµÑ‚Ðµ
    // Ñ�Ð¾Ð·Ð´Ð°Ñ‚ÑŒ
    // Ð°ÐºÐ°Ð´ÐµÐ¼Ð¸ÑŽ
    // ÐºÐ»Ð°Ð½Ð°?
    public static final int PLEASE_ENTER_THE_NAME_OF_THE_CLAN_ACADEMY = 1740; // Ð’Ð²ÐµÐ´Ð¸Ñ‚Ðµ
    // Ð½Ð°Ð·Ð²Ð°Ð½Ð¸Ðµ
    // Ð°ÐºÐ°Ð´ÐµÐ¼Ð¸Ð¸
    // ÐºÐ»Ð°Ð½Ð°.
    public static final int CONGRATULATIONS_THE_S1S_CLAN_ACADEMY_HAS_BEEN_CREATED = 1741; // ÐŸÐ¾Ð·Ð´Ñ€Ð°Ð²Ð»Ñ�ÐµÐ¼!
    // Ð�ÐºÐ°Ð´ÐµÐ¼Ð¸Ñ�
    // ÐºÐ»Ð°Ð½Ð°
    // $s1
    // Ñ�Ð¾Ð·Ð´Ð°Ð½Ð°.
    public static final int A_MESSAGE_INVITING_S1_TO_JOIN_THE_CLAN_ACADEMY_IS_BEING_SENT = 1742; // $s1:
    // Ð¿Ñ€Ð¸Ð³Ð»Ð°ÑˆÐµÐ½Ð¸Ðµ
    // Ð½Ð°
    // Ð²Ñ�Ñ‚ÑƒÐ¿Ð»ÐµÐ½Ð¸Ðµ
    // Ð²
    // Ð°ÐºÐ°Ð´ÐµÐ¼Ð¸ÑŽ
    // ÐºÐ»Ð°Ð½Ð°
    // Ð¾Ñ‚Ð¿Ñ€Ð°Ð²Ð»ÐµÐ½Ð¾.
    public static final int TO_OPEN_A_CLAN_ACADEMY_THE_LEADER_OF_A_LEVEL_5_CLAN_OR_ABOVE_MUST_PAY_XX_PROOFS_OF_BLOOD_OR_A_CERTAIN_AMOUNT_OF_ADENA = 1743; // Ð”Ð»Ñ�
    // Ñ�Ð¾Ð·Ð´Ð°Ð½Ð¸Ñ�
    // Ð°ÐºÐ°Ð´ÐµÐ¼Ð¸Ð¸
    // ÐºÐ»Ð°Ð½Ð°
    // Ð½ÑƒÐ¶Ð½Ð¾,
    // Ñ‡Ñ‚Ð¾Ð±Ñ‹
    // ÑƒÑ€Ð¾Ð²ÐµÐ½ÑŒ
    // ÐºÐ»Ð°Ð½Ð°
    // Ð±Ñ‹Ð»
    // Ð½Ðµ
    // Ð¼ÐµÐ½ÑŒÑˆÐµ
    // 5
    // Ð¸
    // Ð³Ð»Ð°Ð²Ð°
    // ÐºÐ»Ð°Ð½Ð°
    // Ð´Ð¾Ð»Ð¶ÐµÐ½
    // Ð·Ð°Ð¿Ð»Ð°Ñ‚Ð¸Ñ‚ÑŒ
    // XX
    // ÐœÐµÑ‚Ð¾Ðº
    // ÐšÑ€Ð¾Ð²Ð¸
    // Ð¸Ð»Ð¸
    // Ð¾Ð¿Ñ€ÐµÐ´ÐµÐ»ÐµÐ½Ð½ÑƒÑŽ
    // Ñ�ÑƒÐ¼Ð¼Ñƒ
    // Ð°Ð´ÐµÐ½.
    public static final int THERE_WAS_NO_RESPONSE_TO_YOUR_INVITATION_TO_JOIN_THE_CLAN_ACADEMY_SO_THE_INVITATION_HAS_BEEN_RESCINDED = 1744; // Ð�ÐµÑ‚
    // Ð¾Ñ‚Ð²ÐµÑ‚Ð°.
    // Ð’Ð°ÑˆÐµ
    // Ð¿Ñ€Ð¸Ð³Ð»Ð°ÑˆÐµÐ½Ð¸Ðµ
    // Ð½Ð°
    // Ð²Ñ�Ñ‚ÑƒÐ¿Ð»ÐµÐ½Ð¸Ðµ
    // Ð²
    // Ð°ÐºÐ°Ð´ÐµÐ¼Ð¸ÑŽ
    // ÐºÐ»Ð°Ð½Ð°
    // Ð±Ñ‹Ð»Ð¾
    // Ð¾Ñ‚Ð¼ÐµÐ½ÐµÐ½Ð¾.
    public static final int THE_RECIPIENT_OF_YOUR_INVITATION_TO_JOIN_THE_CLAN_ACADEMY_HAS_DECLINED = 1745; // Ð’Ð°ÑˆÐµ
    // Ð¿Ñ€Ð¸Ð³Ð»Ð°ÑˆÐµÐ½Ð¸Ðµ
    // Ð½Ð°
    // Ð²Ñ�Ñ‚ÑƒÐ¿Ð»ÐµÐ½Ð¸ÑŽ
    // Ð²
    // Ð°ÐºÐ°Ð´ÐµÐ¼Ð¸ÑŽ
    // Ð±Ñ‹Ð»Ð¾
    // Ð¾Ñ‚ÐºÐ»Ð¾Ð½ÐµÐ½Ð¾.
    public static final int YOU_HAVE_ALREADY_JOINED_A_CLAN_ACADEMY = 1746; // Ð’Ñ‹
    // ÑƒÐ¶Ðµ
    // Ñ�Ð¾Ñ�Ñ‚Ð¾Ð¸Ñ‚Ðµ
    // Ð²
    // Ð°ÐºÐ°Ð´ÐµÐ¼Ð¸Ð¸
    // ÐºÐ»Ð°Ð½Ð°.
    public static final int S1_HAS_SENT_YOU_AN_INVITATION_TO_JOIN_THE_CLAN_ACADEMY_BELONGING_TO_THE_S2_CLAN_DO_YOU_ACCEPT = 1747; // $s1
    // Ð¿Ñ€Ð¸Ñ�Ñ‹Ð»Ð°ÐµÑ‚
    // Ð’Ð°Ð¼
    // Ð¿Ñ€Ð¸Ð³Ð»Ð°ÑˆÐµÐ½Ð¸Ðµ
    // Ð½Ð°
    // Ð²Ñ�Ñ‚ÑƒÐ¿Ð»ÐµÐ½Ð¸Ðµ
    // Ð²
    // Ð�ÐºÐ°Ð´ÐµÐ¼Ð¸ÑŽ
    // ÐºÐ»Ð°Ð½Ð°
    // $s2.
    // ÐŸÑ€Ð¸Ð½Ñ�Ñ‚ÑŒ?
    public static final int CLAN_ACADEMY_MEMBER_S1_HAS_SUCCESSFULLY_COMPLETED_THE_2ND_CLASS_TRANSFER_AND_OBTAINED_S2_CLAN_REPUTATION_POINTS = 1748; // $s1
    // Ð¸Ð·
    // Ð�ÐºÐ°Ð´ÐµÐ¼Ð¸Ð¸
    // ÐºÐ»Ð°Ð½Ð°
    // ÑƒÑ�Ð¿ÐµÑˆÐ½Ð¾
    // Ð¿Ð¾Ð»ÑƒÑ‡Ð¸Ð»
    // 2-ÑŽ
    // Ð¿Ñ€Ð¾Ñ„ÐµÑ�Ñ�Ð¸ÑŽ.
    // ÐšÐ»Ð°Ð½
    // Ð¿Ð¾Ð»ÑƒÑ‡Ð¸Ð»
    // Ð¾Ñ‡ÐºÐ¸
    // Ñ€ÐµÐ¿ÑƒÑ‚Ð°Ñ†Ð¸Ð¸
    // ($s2).
    public static final int CONGRATULATIONS_YOU_WILL_NOW_GRADUATE_FROM_THE_CLAN_ACADEMY_AND_LEAVE_YOUR_CURRENT_CLAN_AS_A_GRADUATE_OF_THE_ACADEMY_YOU_CAN_IMMEDIATELY_JOIN_A_CLAN_AS_A_REGULAR_MEMBER_WITHOUT_BEING_SUBJECT_TO_ANY_PENALTIES = 1749; // ÐŸÐ¾Ð·Ð´Ñ€Ð°Ð²Ð»Ñ�ÐµÐ¼!
    // Ð’Ñ‹
    // Ð·Ð°ÐºÐ¾Ð½Ñ‡Ð¸Ð»Ð¸
    // Ð°ÐºÐ°Ð´ÐµÐ¼Ð¸ÑŽ
    // Ð¸
    // Ð±ÑƒÐ´ÐµÑ‚Ðµ
    // Ð²Ñ‹Ð¿ÑƒÑ‰ÐµÐ½Ñ‹
    // Ð¸Ð·
    // ÐºÐ»Ð°Ð½Ð°.
    // Ð’Ñ‹Ð¿ÑƒÑ�ÐºÐ½Ð¸ÐºÐ¸
    // Ð°ÐºÐ°Ð´ÐµÐ¼Ð¸Ð¸
    // Ð¼Ð¾Ð³ÑƒÑ‚
    // Ñ�Ñ€Ð°Ð·Ñƒ
    // Ð¶Ðµ
    // Ð²Ñ�Ñ‚ÑƒÐ¿Ð¸Ñ‚ÑŒ
    // Ð²
    // ÐºÐ»Ð°Ð½.
    public static final int C1_DOES_NOT_MEET_THE_PARTICIPATION_REQUIREMENTS_THE_OWNER_OF_S2_CANNOT_PARTICIPATE_IN_THE = 1750; // $c1
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð²Ð¾Ð²Ð°Ñ‚ÑŒ
    // Ð²
    // Ñ�Ð¾Ñ€ÐµÐ²Ð½Ð¾Ð²Ð°Ð½Ð¸Ñ�Ñ…,
    // Ñ‚Ð°Ðº
    // ÐºÐ°Ðº
    // Ð¾Ð±Ð»Ð°Ð´Ð°Ñ‚ÐµÐ»ÑŒ
    // Ñ‚ÐµÑ€Ñ€Ð¸Ñ‚Ð¾Ñ€Ð¸ÐµÐ¹
    // $s2
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð²Ð¾Ð²Ð°Ñ‚ÑŒ
    // Ð²
    // ÐžÐ»Ð¸Ð¼Ð¿Ð¸Ð°Ð´Ðµ.
    public static final int THE_GRAND_MASTER_HAS_GIVEN_YOU_A_COMMEMORATIVE_ITEM = 1751; // Ð’ÐµÐ»Ð¸ÐºÐ¸Ð¹
    // ÐœÐ°Ñ�Ñ‚ÐµÑ€
    // Ð¿Ð¾Ð´Ð°Ñ€Ð¸Ð»
    // Ð’Ð°Ð¼
    // Ð¿Ð°Ð¼Ñ�Ñ‚Ð½Ñ‹Ð¹
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚.
    public static final int SINCE_THE_CLAN_HAS_RECEIVED_A_GRADUATE_OF_THE_CLAN_ACADEMY_IT_HAS_EARNED_S1_POINTS_TOWARD_ITS_REPUTATION_SCORE = 1752; // ÐŸÑ€Ð¸Ð½Ñ�Ð²
    // Ð²Ñ‹Ð¿ÑƒÑ�ÐºÐ½Ð¸ÐºÐ°
    // Ð°ÐºÐ°Ð´ÐµÐ¼Ð¸Ð¸
    // Ð²
    // ÐºÐ»Ð°Ð½,
    // Ð’Ñ‹
    // Ð¿Ð¾Ð»ÑƒÑ‡Ð°ÐµÑ‚Ðµ
    // $s1
    // Ð¾Ñ‡ÐºÐ¾Ð²
    // Ñ€ÐµÐ¿ÑƒÑ‚Ð°Ñ†Ð¸Ð¸
    // ÐºÐ»Ð°Ð½Ð°.
    public static final int THE_CLAN_LEADER_HAS_DECREED_THAT_THAT_PARTICULAR_PRIVILEGE_CANNOT_BE_GRANTED_TO_A_CLAN_ACADEMY = 1753; // Ð�ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾
    // ÑƒÐ¿Ð¾Ð»Ð½Ð¾Ð¼Ð¾Ñ‡Ð¸Ñ‚ÑŒ
    // Ñ�Ñ‚Ð¸Ð¼Ð¸
    // Ð¿Ñ€Ð°Ð²Ð°Ð¼Ð¸
    // ÑƒÑ‡ÐµÐ½Ð¸ÐºÐ°
    // Ð°ÐºÐ°Ð´ÐµÐ¼Ð¸Ð¸,
    // Ñ‚Ð°Ðº
    // ÐºÐ°Ðº
    // Ð³Ð»Ð°Ð²Ð°
    // ÐºÐ»Ð°Ð½Ð°
    // Ð¾Ð³Ñ€Ð°Ð½Ð¸Ñ‡Ð¸Ð»
    // Ð¿ÐµÑ€ÐµÐ´Ð°Ñ‡Ñƒ
    // Ñ�Ñ‚Ð¸Ñ…
    // Ð¿Ñ€Ð°Ð².
    public static final int THAT_PRIVILEGE_CANNOT_BE_GRANTED_TO_A_CLAN_ACADEMY_MEMBER = 1754; // Ð�ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾
    // ÑƒÐ¿Ð¾Ð»Ð½Ð¾Ð¼Ð¾Ñ‡Ð¸Ñ‚ÑŒ
    // Ñ�Ñ‚Ð¸Ð¼Ð¸
    // Ð¿Ñ€Ð°Ð²Ð°Ð¼Ð¸
    // ÑƒÑ‡ÐµÐ½Ð¸ÐºÐ°
    // Ð°ÐºÐ°Ð´ÐµÐ¼Ð¸Ð¸.
    public static final int S2_HAS_BEEN_DESIGNATED_AS_THE_APPRENTICE_OF_CLAN_MEMBER_S1 = 1755; // $s1
    // Ð±ÐµÑ€ÐµÑ‚
    // Ð²
    // ÑƒÑ‡ÐµÐ½Ð¸ÐºÐ¸
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°
    // $s2.
    public static final int S1_YOUR_CLAN_ACADEMYS_APPRENTICE_HAS_LOGGED_IN = 1756; // Ð£Ñ‡ÐµÐ½Ð¸Ðº
    // Ð°ÐºÐ°Ð´ÐµÐ¼Ð¸Ð¸
    // $c1
    // Ð·Ð°ÑˆÐµÐ»
    // Ð²
    // Ð¸Ð³Ñ€Ñƒ.
    public static final int S1_YOUR_CLAN_ACADEMYS_APPRENTICE_HAS_LOGGED_OUT = 1757; // Ð£Ñ‡ÐµÐ½Ð¸Ðº
    // Ð°ÐºÐ°Ð´ÐµÐ¼Ð¸Ð¸
    // $c1
    // Ð²Ñ‹ÑˆÐµÐ»
    // Ð¸Ð·
    // Ð¸Ð³Ñ€Ñ‹.
    public static final int S1_YOUR_CLAN_ACADEMYS_SPONSOR_HAS_LOGGED_IN = 1758; // Ð�Ð°Ñ�Ñ‚Ð°Ð²Ð½Ð¸Ðº
    // Ð°ÐºÐ°Ð´ÐµÐ¼Ð¸Ð¸
    // $c1
    // Ð·Ð°ÑˆÐµÐ»
    // Ð²
    // Ð¸Ð³Ñ€Ñƒ.
    public static final int S1_YOUR_CLAN_ACADEMYS_SPONSOR_HAS_LOGGED_OUT = 1759; // Ð�Ð°Ñ�Ñ‚Ð°Ð²Ð½Ð¸Ðº
    // Ð°ÐºÐ°Ð´ÐµÐ¼Ð¸Ð¸
    // $c1
    // Ð²Ñ‹ÑˆÐµÐ»
    // Ð¸Ð·
    // Ð¸Ð³Ñ€Ñ‹.
    public static final int CLAN_MEMBER_S1S_TITLE_HAS_BEEN_CHANGED_TO_S2 = 1760; // $c1:
    // Ñ‚Ð¸Ñ‚ÑƒÐ»
    // Ð¸Ð·Ð¼ÐµÐ½ÐµÐ½
    // Ð½Ð°
    // $s2.
    public static final int CLAN_MEMBER_S1S_PRIVILEGE_LEVEL_HAS_BEEN_CHANGED_TO_S2 = 1761; // $c1:
    // Ð¿Ð¾Ð»Ð½Ð¾Ð¼Ð¾Ñ‡Ð¸Ñ�
    // Ð¸Ð·Ð¼ÐµÐ½ÐµÐ½Ñ‹
    // Ð½Ð°
    // $s2.
    public static final int YOU_DO_NOT_HAVE_THE_RIGHT_TO_DISMISS_AN_APPRENTICE = 1762; // Ð£
    // Ð’Ð°Ñ�
    // Ð½ÐµÑ‚
    // Ð¿Ñ€Ð°Ð²
    // Ð¸Ñ�ÐºÐ»ÑŽÑ‡Ð°Ñ‚ÑŒ
    // ÑƒÑ‡ÐµÐ½Ð¸ÐºÐ¾Ð².
    public static final int S2_CLAN_MEMBER_S1S_APPRENTICE_HAS_BEEN_REMOVED = 1763; // $s2,
    // ÑƒÑ‡ÐµÐ½Ð¸Ðº
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°
    // $c1,
    // ÑƒÐ´Ð°Ð»Ñ�ÐµÑ‚Ñ�Ñ�.
    public static final int THIS_ITEM_CAN_ONLY_BE_WORN_BY_A_MEMBER_OF_THE_CLAN_ACADEMY = 1764; // Ð­Ñ‚Ð¾Ñ‚
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð½Ð°Ð´ÐµÑ‚ÑŒ
    // Ñ‚Ð¾Ð»ÑŒÐºÐ¾
    // ÑƒÑ‡ÐµÐ½Ð¸Ðº
    // Ð°ÐºÐ°Ð´ÐµÐ¼Ð¸Ð¸.
    public static final int AS_A_GRADUATE_OF_THE_CLAN_ACADEMY_YOU_CAN_NO_LONGER_WEAR_THIS_ITEM = 1765; // Ð’Ñ‹Ð¿ÑƒÑ�ÐºÐ½Ð¸ÐºÐ¸
    // Ð°ÐºÐ°Ð´ÐµÐ¼Ð¸Ð¸
    // Ð½Ðµ
    // Ð¼Ð¾Ð³ÑƒÑ‚
    // Ð½Ð°Ð´ÐµÐ²Ð°Ñ‚ÑŒ
    // Ñ�Ñ‚Ð¾Ñ‚
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚.
    public static final int AN_APPLICATION_TO_JOIN_THE_CLAN_HAS_BEEN_SENT_TO_S1_IN_S2 = 1766; // Ð—Ð°Ð¿Ñ€Ð¾Ñ�
    // Ð½Ð°
    // Ð²Ñ�Ñ‚ÑƒÐ¿Ð»ÐµÐ½Ð¸Ðµ
    // Ð²
    // ÐºÐ»Ð°Ð½
    // Ð±Ñ‹Ð»
    // Ð¾Ñ‚Ð¿Ñ€Ð°Ð²Ð»ÐµÐ½
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ñƒ
    // $c1
    // Ð²
    // $s2.
    public static final int AN_APPLICATION_TO_JOIN_THE_CLAN_ACADEMY_HAS_BEEN_SENT_TO_S1 = 1767; // $c1:
    // Ð¾Ñ‚Ð¿Ñ€Ð°Ð²Ð»ÐµÐ½
    // Ð·Ð°Ð¿Ñ€Ð¾Ñ�
    // Ð½Ð°
    // Ð²Ñ�Ñ‚ÑƒÐ¿Ð»ÐµÐ½Ð¸Ðµ
    // Ð²
    // Ð�ÐºÐ°Ð´ÐµÐ¼Ð¸ÑŽ
    // ÐºÐ»Ð°Ð½Ð°.
    public static final int C1_HAS_INVITED_YOU_TO_JOIN_THE_CLAN_ACADEMY_OF_S2_CLAN_WOULD_YOU_LIKE_TO_JOIN = 1768; // $c1
    // Ð¿Ñ€Ð¸Ð³Ð»Ð°ÑˆÐ°ÐµÑ‚
    // Ð’Ð°Ñ�
    // Ð²Ñ�Ñ‚ÑƒÐ¿Ð¸Ñ‚ÑŒ
    // Ð²
    // Ð�ÐºÐ°Ð´ÐµÐ¼Ð¸ÑŽ
    // ÐºÐ»Ð°Ð½Ð°
    // $s2.
    // ÐŸÑ€Ð¸Ð½Ñ�Ñ‚ÑŒ?
    public static final int C1_HAS_SENT_YOU_AN_INVITATION_TO_JOIN_THE_S3_ORDER_OF_KNIGHTS_UNDER_THE_S2_CLAN_WOULD_YOU_LIKE = 1769; // $c1
    // Ð¸Ð·
    // ÐºÐ»Ð°Ð½Ð°
    // $s2
    // Ð¿Ñ€Ð¸Ð³Ð»Ð°ÑˆÐ°ÐµÑ‚
    // Ð’Ð°Ñ�
    // Ð²Ñ�Ñ‚ÑƒÐ¿Ð¸Ñ‚ÑŒ
    // Ð²
    // ÐžÑ€Ð´ÐµÐ½
    // Ð Ñ‹Ñ†Ð°Ñ€ÐµÐ¹
    // $s3.
    public static final int THE_CLAN_REPUTATION_SCORE_HAS_DROPPED_BELOW_0_THE_CLAN_MAY_FACE_CERTAIN_PENALTIES_AS_A_RESULT = 1770; // ÐŸÑ€Ð¸
    // Ñ�Ð½Ð¸Ð¶ÐµÐ½Ð¸Ð¸
    // Ð¾Ñ‡ÐºÐ¾Ð²
    // Ñ€ÐµÐ¿ÑƒÑ‚Ð°Ñ†Ð¸Ð¸
    // Ð½Ð¸Ð¶Ðµ
    // 0
    // Ð½Ð°
    // ÐºÐ»Ð°Ð½
    // Ð¼Ð¾Ð³ÑƒÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð½Ð°Ð»Ð¾Ð¶ÐµÐ½Ñ‹
    // Ð½ÐµÐºÐ¾Ñ‚Ð¾Ñ€Ñ‹Ðµ
    // Ñ�Ð°Ð½ÐºÑ†Ð¸Ð¸.
    public static final int NOW_THAT_YOUR_CLAN_LEVEL_IS_ABOVE_LEVEL_5_IT_CAN_ACCUMULATE_CLAN_REPUTATION_POINTS = 1771; // ÐžÑ‡ÐºÐ¸
    // Ñ€ÐµÐ¿ÑƒÑ‚Ð°Ñ†Ð¸Ð¸
    // ÐºÐ»Ð°Ð½Ð°
    // Ð¼Ð¾Ð¶Ð½Ð¾
    // ÐºÐ¾Ð¿Ð¸Ñ‚ÑŒ
    // Ñ‚Ð¾Ð»ÑŒÐºÐ¾
    // Ñ�
    // 5
    // ÑƒÑ€Ð¾Ð²Ð½Ñ�
    // ÐºÐ»Ð°Ð½Ð°.
    public static final int SINCE_YOUR_CLAN_WAS_DEFEATED_IN_A_SIEGE_S1_POINTS_HAVE_BEEN_DEDUCTED_FROM_YOUR_CLAN_REPUTATION_SCORE_AND_GIVEN_TO_THE_OPPOSING_CLAN = 1772; // Ð’Ð°Ñˆ
    // ÐºÐ»Ð°Ð½
    // Ð¿Ñ€Ð¾Ð¸Ð³Ñ€Ð°Ð»
    // Ð¾Ñ�Ð°Ð´Ñƒ
    // Ð·Ð°Ð¼ÐºÐ°.
    // ÐŸÐ¾Ñ‚ÐµÑ€Ñ�Ð½Ð¾
    // Ð¾Ñ‡ÐºÐ¾Ð²
    // ÐºÐ»Ð°Ð½Ð°:
    // $s1.
    public static final int SINCE_YOUR_CLAN_EMERGED_VICTORIOUS_FROM_THE_SIEGE_S1_POINTS_HAVE_BEEN_ADDED_TO_YOUR_CLAN_REPUTATION_SCORE = 1773; // Ð’Ð°Ñˆ
    // ÐºÐ»Ð°Ð½
    // Ð²Ñ‹Ð¸Ð³Ñ€Ð°Ð»
    // Ð¾Ñ�Ð°Ð´Ñƒ
    // Ð·Ð°Ð¼ÐºÐ°.
    // ÐŸÐ¾Ð»ÑƒÑ‡ÐµÐ½Ð¾
    // Ð¾Ñ‡ÐºÐ¾Ð²
    // ÐºÐ»Ð°Ð½Ð°:
    // $s1.
    public static final int YOUR_CLAN_NEWLY_ACQUIRED_CONTESTED_CLAN_HALL_HAS_ADDED_S1_POINTS_TO_YOUR_CLAN_REPUTATION_SCORE = 1774; // Ð’Ð°Ñˆ
    // ÐºÐ»Ð°Ð½
    // Ð·Ð°Ð½Ð¾Ð²Ð¾
    // Ð·Ð°Ð²Ð¾ÐµÐ²Ð°Ð»
    // Ñ…Ð¾Ð»Ð»
    // ÐºÐ»Ð°Ð½Ð°.
    // ÐŸÐ¾Ð»ÑƒÑ‡ÐµÐ½Ð¾
    // Ð¾Ñ‡ÐºÐ¾Ð²
    // ÐºÐ»Ð°Ð½Ð°:
    // $s1.
    public static final int CLAN_MEMBER_S1_WAS_AN_ACTIVE_MEMBER_OF_THE_HIGHEST_RANKED_PARTY_IN_THE_FESTIVAL_OF_DARKNESS_S2_POINTS_HAVE_BEEN_ADDED_TO_YOUR_CLAN_REPUTATION_SCORE = 1775; // Ð§Ð»ÐµÐ½
    // ÐºÐ»Ð°Ð½Ð°
    // $c1Ð±Ñ‹Ð»
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð½Ð¸ÐºÐ¾Ð¼
    // Ð³Ñ€ÑƒÐ¿Ð¿Ñ‹
    // Ð²Ñ‹Ñ�ÑˆÐµÐ³Ð¾
    // Ñ€Ð°Ð½Ð³Ð°
    // Ð¤ÐµÑ�Ñ‚Ð¸Ð²Ð°Ð»Ñ�
    // Ð¢ÑŒÐ¼Ñ‹.
    // ÐŸÐ¾Ð»ÑƒÑ‡ÐµÐ½Ð¾
    // Ð¾Ñ‡ÐºÐ¾Ð²
    // ÐºÐ»Ð°Ð½Ð°:
    // $s2.
    public static final int CLAN_MEMBER_S1_WAS_NAMED_A_HERO_2S_POINTS_HAVE_BEEN_ADDED_TO_YOUR_CLAN_REPUTATION_SCORE = 1776; // Ð§Ð»ÐµÐ½
    // ÐºÐ»Ð°Ð½Ð°
    // Ð¿Ð¾Ð»ÑƒÑ‡Ð¸Ð»
    // Ñ�Ñ‚Ð°Ñ‚ÑƒÑ�
    // Ð³ÐµÑ€Ð¾Ñ�.
    // ÐŸÐ¾Ð»ÑƒÑ‡ÐµÐ½Ð¾
    // Ð¾Ñ‡ÐºÐ¾Ð²
    // ÐºÐ»Ð°Ð½Ð°:
    // $2s.
    public static final int YOU_HAVE_SUCCESSFULLY_COMPLETED_A_CLAN_QUEST_S1_POINTS_HAVE_BEEN_ADDED_TO_YOUR_CLAN_REPUTATION_SCORE = 1777; // Ð’Ñ‹
    // ÑƒÑ�Ð¿ÐµÑˆÐ½Ð¾
    // Ð²Ñ‹Ð¿Ð¾Ð»Ð½Ð¸Ð»Ð¸
    // ÐºÐ»Ð°Ð½Ð¾Ð²Ñ‹Ð¹
    // ÐºÐ²ÐµÑ�Ñ‚.
    // ÐŸÐ¾Ð»ÑƒÑ‡ÐµÐ½Ð¾
    // Ð¾Ñ‡ÐºÐ¾Ð²
    // ÐºÐ»Ð°Ð½Ð°:
    // $s1.
    public static final int AN_OPPOSING_CLAN_HAS_CAPTURED_YOUR_CLAN_CONTESTED_CLAN_HALL_S1_POINTS_HAVE_BEEN_DEDUCTED_FROM_YOUR_CLAN_REPUTATION_SCORE = 1778; // Ð’Ñ€Ð°Ð¶Ð´ÐµÐ±Ð½Ñ‹Ð¹
    // ÐºÐ»Ð°Ð½
    // Ð·Ð°Ñ…Ð²Ð°Ñ‚Ð¸Ð»
    // Ð²Ð°Ñˆ
    // Ñ�Ð¿Ð¾Ñ€Ð½Ñ‹Ð¹
    // Ñ…Ð¾Ð»Ð».
    // ÐŸÐ¾Ñ‚ÐµÑ€Ñ�Ð½Ð¾
    // Ð¾Ñ‡ÐºÐ¾Ð²
    // ÐºÐ»Ð°Ð½Ð°:
    // $s1.
    public static final int AFTER_LOSING_THE_CONTESTED_CLAN_HALL_300_POINTS_HAVE_BEEN_DEDUCTED_FROM_YOUR_CLAN_REPUTATION_SCORE = 1779; // ÐŸÐ¾Ñ�Ð»Ðµ
    // Ð¿Ð¾Ñ‚ÐµÑ€Ð¸
    // Ñ�Ð¿Ð¾Ñ€Ð½Ð¾Ð³Ð¾
    // Ñ…Ð¾Ð»Ð»Ð°
    // ÐºÐ»Ð°Ð½Ð°
    // 300
    // Ð¾Ñ‡ÐºÐ¾Ð²
    // Ð±ÑƒÐ´ÐµÑ‚
    // Ð²Ñ‹Ñ‡Ñ‚ÐµÐ½Ð¾
    // Ð¸Ð·
    // Ñ€ÐµÐ¿ÑƒÑ‚Ð°Ñ†Ð¸Ð¸
    // Ð²Ð°ÑˆÐµÐ³Ð¾
    // ÐºÐ»Ð°Ð½Ð°.
    public static final int YOUR_CLAN_HAS_CAPTURED_YOUR_OPPONENT_CONTESTED_CLAN_HALL_S1_POINTS_HAVE_BEEN_DEDUCTED_FROM_YOUR_OPPONENT_CLAN_REPUTATION_SCORE = 1780; // Ð’Ð°Ñˆ
    // ÐºÐ»Ð°Ð½
    // Ð·Ð°Ñ…Ð²Ð°Ñ‚Ð¸Ð»
    // Ñ�Ð¿Ð¾Ñ€Ð½Ñ‹Ð¹
    // Ñ…Ð¾Ð»Ð»
    // Ð²Ñ€Ð°Ð¶Ð´ÐµÐ±Ð½Ð¾Ð³Ð¾
    // ÐºÐ»Ð°Ð½Ð°.
    // ÐŸÐ¾Ñ‚ÐµÑ€Ñ�Ð½Ð¾
    // Ð¾Ñ‡ÐºÐ¾Ð²
    // ÐºÐ»Ð°Ð½Ð°
    // Ð¿Ñ€Ð¾Ñ‚Ð¸Ð²Ð½Ð¸ÐºÐ°:
    // $s1.
    public static final int YOUR_CLAN_HAS_ADDED_1S_POINTS_TO_ITS_CLAN_REPUTATION_SCORE = 1781; // ÐŸÐ¾Ð»ÑƒÑ‡ÐµÐ½Ð¾
    // Ð¾Ñ‡ÐºÐ¾Ð²
    // ÐºÐ»Ð°Ð½Ð°:
    // $1s.
    public static final int YOUR_CLAN_MEMBER_S1_WAS_KILLED_S2_POINTS_HAVE_BEEN_DEDUCTED_FROM_YOUR_CLAN_REPUTATION_SCORE_AND_ADDED_TO_YOUR_OPPONENT_CLAN_REPUTATION_SCORE = 1782; // Ð§Ð»ÐµÐ½
    // Ð’Ð°ÑˆÐµÐ³Ð¾
    // ÐºÐ»Ð°Ð½Ð°
    // $c1
    // Ð±Ñ‹Ð»
    // ÑƒÐ±Ð¸Ñ‚.
    // ÐŸÐ¾Ñ‚ÐµÑ€Ñ�Ð½Ð¾
    // Ð¾Ñ‡ÐºÐ¾Ð²
    // ÐºÐ»Ð°Ð½Ð°
    // Ð¸
    // Ð´Ð¾Ð±Ð°Ð²Ð»ÐµÐ½Ð¾
    // Ð¿Ñ€Ð¾Ñ‚Ð¸Ð²Ð½Ð¸ÐºÑƒ:
    // $s2.
    public static final int FOR_KILLING_AN_OPPOSING_CLAN_MEMBER_S1_POINTS_HAVE_BEEN_DEDUCTED_FROM_YOUR_OPPONENTS_CLAN_REPUTATION_SCORE = 1783; // Ð—Ð°
    // ÑƒÐ±Ð¸Ð¹Ñ�Ñ‚Ð²Ð¾
    // Ñ‡Ð»ÐµÐ½Ð°
    // Ð²Ñ€Ð°Ð¶Ð´ÐµÐ±Ð½Ð¾Ð³Ð¾
    // ÐºÐ»Ð°Ð½Ð°
    // Ð¸Ð·
    // ÐºÐ»Ð°Ð½Ð¾Ð²Ð¾Ð¹
    // Ñ€ÐµÐ¿ÑƒÑ‚Ð°Ñ†Ð¸Ð¸
    // Ð¿Ñ€Ð¾Ñ‚Ð¸Ð²Ð½Ð¸ÐºÐ°
    // Ð²Ñ‹Ñ‡Ñ‚ÐµÐ½Ð¾
    // Ð¾Ñ‡ÐºÐ¾Ð²
    // ÐºÐ»Ð°Ð½Ð°:
    // $s1.
    public static final int YOUR_CLAN_HAS_FAILED_TO_DEFEND_THE_CASTLE_S1_POINTS_HAVE_BEEN_DEDUCTED_FROM_YOUR_CLAN_REPUTATION_SCORE_AND_ADDED_TO_YOUR_OPPONENTS = 1784; // Ð’Ð°Ñˆ
    // ÐºÐ»Ð°Ð½
    // Ð½Ðµ
    // Ñ�Ð¼Ð¾Ð³
    // Ð·Ð°Ñ‰Ð¸Ñ‚Ð¸Ñ‚ÑŒ
    // Ð·Ð°Ð¼Ð¾Ðº.
    // ÐŸÐ¾Ñ‚ÐµÑ€Ñ�Ð½Ð¾
    // Ð¾Ñ‡ÐºÐ¾Ð²
    // ÐºÐ»Ð°Ð½Ð°
    // Ð¸
    // Ð´Ð¾Ð±Ð°Ð²Ð»ÐµÐ½Ð¾
    // Ð¿Ñ€Ð¾Ñ‚Ð¸Ð²Ð½Ð¸ÐºÑƒ:
    // $s1.
    public static final int THE_CLAN_YOU_BELONG_TO_HAS_BEEN_INITIALIZED_S1_POINTS_HAVE_BEEN_DEDUCTED_FROM_YOUR_CLAN_REPUTATION_SCORE = 1785; // Ð’Ð°Ñˆ
    // ÐºÐ»Ð°Ð½
    // Ð±Ñ‹Ð»
    // Ð²Ð¾Ð·Ð²Ñ€Ð°Ñ‰ÐµÐ½
    // Ð²
    // Ð¸Ñ�Ñ…Ð¾Ð´Ð½Ð¾Ðµ
    // Ñ�Ð¾Ñ�Ñ‚Ð¾Ñ�Ð½Ð¸Ðµ.
    // ÐŸÐ¾Ñ‚ÐµÑ€Ñ�Ð½Ð¾
    // Ð¾Ñ‡ÐºÐ¾Ð²
    // ÐºÐ»Ð°Ð½Ð°:
    // $s1.
    public static final int YOUR_CLAN_HAS_FAILED_TO_DEFEND_THE_CASTLE_S1_POINTS_HAVE_BEEN_DEDUCTED_FROM_YOUR_CLAN_REPUTATION_SCORE = 1786; // Ð’Ð°Ñˆ
    // ÐºÐ»Ð°Ð½
    // Ð½Ðµ
    // Ñ�Ð¼Ð¾Ð³
    // Ð·Ð°Ñ‰Ð¸Ñ‚Ð¸Ñ‚ÑŒ
    // Ð·Ð°Ð¼Ð¾Ðº.
    // ÐŸÐ¾Ñ‚ÐµÑ€Ñ�Ð½Ð¾
    // Ð¾Ñ‡ÐºÐ¾Ð²
    // ÐºÐ»Ð°Ð½Ð°:
    // $s1.
    public static final int S1_POINTS_HAVE_BEEN_DEDUCTED_FROM_THE_CLAN_REPUTATION_SCORE = 1787; // ÐŸÐ¾Ñ‚ÐµÑ€Ñ�Ð½Ð¾
    // Ð¾Ñ‡ÐºÐ¾Ð²
    // ÐºÐ»Ð°Ð½Ð°:
    // $1s.
    public static final int THE_CLAN_SKILL_S1_HAS_BEEN_ADDED = 1788; // Ð”Ð¾Ð±Ð°Ð²Ð»ÐµÐ½Ð¾
    // ÐºÐ»Ð°Ð½Ð¾Ð²Ð¾Ðµ
    // ÑƒÐ¼ÐµÐ½Ð¸Ðµ:
    // $s1.
    public static final int SINCE_THE_CLAN_REPUTATION_SCORE_HAS_DROPPED_TO_0_OR_LOWER_YOUR_CLAN_SKILLS_WILL_BE_DE_ACTIVATED = 1789; // Ð¡
    // Ð¼Ð¾Ð¼ÐµÐ½Ñ‚Ð°
    // Ð¿Ð°Ð´ÐµÐ½Ð¸Ñ�
    // ÐºÐ»Ð°Ð½Ð¾Ð²Ð¾Ð¹
    // Ñ€ÐµÐ¿ÑƒÑ‚Ð°Ñ†Ð¸Ð¸
    // Ð´Ð¾
    // 0
    // Ð¸
    // Ð½Ð¸Ð¶Ðµ
    // Ð²Ð°ÑˆÐ¸
    // ÐºÐ»Ð°Ð½Ð¾Ð²Ñ‹Ðµ
    // ÑƒÐ¼ÐµÐ½Ð¸Ñ�
    // Ð±ÑƒÐ´ÑƒÑ‚
    // Ð·Ð°Ð±Ð»Ð¾ÐºÐ¸Ñ€Ð¾Ð²Ð°Ð½Ñ‹.
    public static final int THE_CONDITIONS_NECESSARY_TO_INCREASE_THE_CLAN_LEVEL_HAVE_NOT_BEEN_MET = 1790; // Ð£Ñ�Ð»Ð¾Ð²Ð¸Ñ�,
    // Ð½ÐµÐ¾Ð±Ñ…Ð¾Ð´Ð¸Ð¼Ñ‹Ðµ
    // Ð´Ð»Ñ�
    // Ð¿Ð¾Ð´Ð½Ñ�Ñ‚Ð¸Ñ�
    // ÐºÐ»Ð°Ð½Ð¾Ð²Ð¾Ð³Ð¾
    // ÑƒÑ€Ð¾Ð²Ð½Ñ�,
    // Ð½Ðµ
    // Ñ�Ð¾Ð±Ð»ÑŽÐ´ÐµÐ½Ñ‹.
    public static final int THE_CONDITIONS_NECESSARY_TO_CREATE_A_MILITARY_UNIT_HAVE_NOT_BEEN_MET = 1791; // Ð£Ñ�Ð»Ð¾Ð²Ð¸Ñ�,
    // Ð½ÐµÐ¾Ð±Ñ…Ð¾Ð´Ð¸Ð¼Ñ‹Ðµ
    // Ð´Ð»Ñ�
    // Ñ�Ð¾Ð·Ð´Ð°Ð½Ð¸Ñ�
    // Ð±Ð¾ÐµÐ²Ð¾Ð³Ð¾
    // Ð¿Ð¾Ð´Ñ€Ð°Ð·Ð´ÐµÐ»ÐµÐ½Ð¸Ñ�,
    // Ð½Ðµ
    // Ñ�Ð¾Ð±Ð»ÑŽÐ´ÐµÐ½Ñ‹.
    public static final int PLEASE_ASSIGN_A_MANAGER_FOR_YOUR_NEW_ORDER_OF_KNIGHTS = 1792; // ÐŸÐ¾Ð¶Ð°Ð»ÑƒÐ¹Ñ�Ñ‚Ð°,
    // Ð½Ð°Ð·Ð½Ð°Ñ‡ÑŒÑ‚Ðµ
    // Ð¼Ð°Ð³Ð¸Ñ�Ñ‚Ñ€Ð°
    // Ð´Ð»Ñ�
    // Ð²Ð°ÑˆÐµÐ³Ð¾
    // Ð½Ð¾Ð²Ð¾Ð³Ð¾
    // Ð Ñ‹Ñ†Ð°Ñ€Ñ�ÐºÐ¾Ð³Ð¾
    // ÐžÑ€Ð´ÐµÐ½Ð°.
    public static final int S1_HAS_BEEN_SELECTED_AS_THE_CAPTAIN_OF_S2 = 1793; // $c1
    // Ñ‚ÐµÐ¿ÐµÑ€ÑŒ
    // ÐºÐ°Ð¿Ð¸Ñ‚Ð°Ð½
    // $s2.
    public static final int THE_KNIGHTS_OF_S1_HAVE_BEEN_CREATED = 1794; // Ð¡Ñ„Ð¾Ñ€Ð¼Ð¸Ñ€Ð¾Ð²Ð°Ð½
    // Ð¾Ñ‚Ñ€Ñ�Ð´
    // Ð Ñ‹Ñ†Ð°Ñ€ÐµÐ¹
    // $s1.
    public static final int THE_ROYAL_GUARD_OF_S1_HAVE_BEEN_CREATED = 1795; // Ð¡Ñ„Ð¾Ñ€Ð¼Ð¸Ñ€Ð¾Ð²Ð°Ð½
    // Ð¾Ñ‚Ñ€Ñ�Ð´
    // ÐšÐ¾Ñ€Ð¾Ð»ÐµÐ²Ñ�ÐºÐ¾Ð¹
    // Ð¡Ñ‚Ñ€Ð°Ð¶Ð¸
    // $s1.
    public static final int FOR_KOREA_ONLY = 1796; // Ð’Ð°Ñˆ Ð°ÐºÐºÐ°ÑƒÐ½Ñ‚ Ð²Ñ€ÐµÐ¼ÐµÐ½Ð½Ð¾
    // Ð·Ð°Ð±Ð»Ð¾ÐºÐ¸Ñ€Ð¾Ð²Ð°Ð½ Ð·Ð° Ð²Ð¾Ñ€Ð¾Ð²Ñ�Ñ‚Ð²Ð¾
    // Ð°ÐºÐºÐ°ÑƒÐ½Ñ‚Ð¾Ð² Ð¸Ð»Ð¸ Ð´Ñ€ÑƒÐ³ÑƒÑŽ
    // Ð²Ñ€ÐµÐ´Ð¾Ð½Ð¾Ñ�Ð½ÑƒÑŽ Ð´ÐµÑ�Ñ‚ÐµÐ»ÑŒÐ½Ð¾Ñ�Ñ‚ÑŒ.
    // Ð—Ð° Ð´Ð¾Ð¿Ð¾Ð»Ð½Ð¸Ñ‚ÐµÐ»ÑŒÐ½Ð¾Ð¹
    // Ð¸Ð½Ñ„Ð¾Ñ€Ð¼Ð°Ñ†Ð¸ÐµÐ¹ Ð¾Ð±Ñ€Ð°Ñ‰Ð°Ð¹Ñ‚ÐµÑ�ÑŒ Ð²
    // Ð¦ÐµÐ½Ñ‚Ñ€
    // ÐŸÐ¾Ð´Ð´ÐµÑ€Ð¶ÐºÐ¸.
    public static final int C1_HAS_BEEN_PROMOTED_TO_S2 = 1797; // $c1 Ð¿Ð¾Ð²Ñ‹ÑˆÐ°ÐµÑ‚Ñ�Ñ�
    // Ð´Ð¾ $s2.
    public static final int CLAN_LORD_PRIVILEGES_HAVE_BEEN_TRANSFERRED_TO_C1 = 1798; // $c1
    // Ð¿Ð¾Ð»ÑƒÑ‡Ð°ÐµÑ‚
    // Ð¿Ð¾Ð»Ð½Ð¾Ð¼Ð¾Ñ‡Ð¸Ñ�
    // Ð»Ð¸Ð´ÐµÑ€Ð°
    // ÐºÐ»Ð°Ð½Ð°.
    public static final int CURRENTLY_UNDER_INVESTIGATION_PLEASE_WAIT = 1799; // Ð˜Ð´ÐµÑ‚
    // Ð¿Ð¾Ð¸Ñ�Ðº
    // Ð±Ð¾Ñ‚Ð¾Ð².
    // ÐŸÐ¾Ð¶Ð°Ð»ÑƒÐ¹Ñ�Ñ‚Ð°,
    // Ð¿Ð¾Ð²Ñ‚Ð¾Ñ€Ð¸Ñ‚Ðµ
    // Ð¿Ð¾Ð¿Ñ‹Ñ‚ÐºÑƒ
    // Ð¿Ð¾Ð·Ð¶Ðµ.
    public static final int THE_USER_NAME_S1_HAS_A_HISTORY_OF_USING_THIRD_PARTY_PROGRAMS = 1800; // $c1
    // Ð¾Ð±Ð²Ð¸Ð½Ñ�ÐµÑ‚Ñ�Ñ�
    // Ð²
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ð½Ð¸Ð¸
    // Ð±Ð¾Ñ‚Ð°.
    public static final int THE_ATTEMPT_TO_SELL_HAS_FAILED = 1801; // ÐŸÐ¾Ð¿Ñ‹Ñ‚ÐºÐ°
    // Ð¿Ñ€Ð¾Ð´Ð°Ñ‚ÑŒ Ð½Ðµ
    // ÑƒÐ´Ð°Ð»Ð°Ñ�ÑŒ.
    public static final int THE_ATTEMPT_TO_TRADE_HAS_FAILED = 1802; // ÐŸÐ¾Ð¿Ñ‹Ñ‚ÐºÐ°
    // Ñ‚Ð¾Ñ€Ð³Ð¾Ð²Ð»Ð¸
    // Ð½Ðµ
    // ÑƒÐ´Ð°Ð»Ð°Ñ�ÑŒ.
    public static final int YOU_CANNOT_REGISTER_FOR_A_MATCH = 1803; // Ð—Ð°Ð¿Ñ€Ð¾Ñ�
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð¸Ñ� Ð²
    // Ð¸Ð³Ñ€Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ñ�Ð´ÐµÐ»Ð°Ð½ Ð½Ðµ
    // Ñ€Ð°Ð½ÐµÐµ,
    // Ñ‡ÐµÐ¼ Ñ‡ÐµÑ€ÐµÐ·
    // 10 Ð¼Ð¸Ð½
    // Ð¿Ð¾Ñ�Ð»Ðµ
    // Ð¾ÐºÐ¾Ð½Ñ‡Ð°Ð½Ð¸Ñ�
    // Ð¸Ð³Ñ€Ñ‹.
    public static final int THIS_ACCOUNT_HAS_BEEN_SUSPENDED_FOR_7_DAYS = 1804; // Ð’Ð°Ñˆ
    // Ð°ÐºÐºÐ°ÑƒÐ½Ñ‚
    // Ð·Ð°Ð±Ð»Ð¾ÐºÐ¸Ñ€Ð¾Ð²Ð°Ð½
    // Ð½Ð°
    // 7
    // Ð´Ð½ÐµÐ¹,
    // Ñ‚Ð°Ðº
    // ÐºÐ°Ðº
    // Ð¾Ð½
    // Ð±Ñ‹Ð»
    // Ð·Ð°Ð¼ÐµÑ‡ÐµÐ½
    // Ð²
    // Ð½ÐµÐ»ÐµÐ³Ð°Ð»ÑŒÐ½Ñ‹Ñ…
    // Ð´ÐµÐ½ÐµÐ¶Ð½Ñ‹Ñ…
    // Ñ�Ð´ÐµÐ»ÐºÐ°Ñ….
    // Ð—Ð°
    // Ð´Ð¾Ð¿Ð¾Ð»Ð½Ð¸Ñ‚ÐµÐ»ÑŒÐ½Ð¾Ð¹
    // Ð¸Ð½Ñ„Ð¾Ñ€Ð¼Ð°Ñ†Ð¸ÐµÐ¹
    // Ð¾Ð±Ñ€Ð°Ñ‚Ð¸Ñ‚ÐµÑ�ÑŒ
    // Ð²
    // Ð¦ÐµÐ½Ñ‚Ñ€
    // ÐŸÐ¾Ð´Ð´ÐµÑ€Ð¶ÐºÐ¸.
    public static final int THIS_ACCOUNT_HAS_BEEN_SUSPENDED_FOR_30_DAYS_1 = 1805; // Ð’Ð°Ñˆ
    // Ð°ÐºÐºÐ°ÑƒÐ½Ñ‚
    // Ð·Ð°Ð±Ð»Ð¾ÐºÐ¸Ñ€Ð¾Ð²Ð°Ð½
    // Ð½Ð°
    // 30
    // Ð´Ð½ÐµÐ¹,
    // Ñ‚Ð°Ðº
    // ÐºÐ°Ðº
    // Ð¾Ð½
    // Ð±Ñ‹Ð»
    // Ð¿Ð¾Ð²Ñ‚Ð¾Ñ€Ð½Ð¾
    // Ð·Ð°Ð¼ÐµÑ‡ÐµÐ½
    // Ð²
    // Ð½ÐµÐ»ÐµÐ³Ð°Ð»ÑŒÐ½Ñ‹Ñ…
    // Ð´ÐµÐ½ÐµÐ¶Ð½Ñ‹Ñ…
    // Ñ�Ð´ÐµÐ»ÐºÐ°Ñ….
    // Ð—Ð°
    // Ð´Ð¾Ð¿Ð¾Ð»Ð½Ð¸Ñ‚ÐµÐ»ÑŒÐ½Ð¾Ð¹
    // Ð¸Ð½Ñ„Ð¾Ñ€Ð¼Ð°Ñ†Ð¸ÐµÐ¹
    // Ð¾Ð±Ñ€Ð°Ñ‚Ð¸Ñ‚ÐµÑ�ÑŒ
    // Ð²
    // Ð¦ÐµÐ½Ñ‚Ñ€
    // ÐŸÐ¾Ð´Ð´ÐµÑ€Ð¶ÐºÐ¸.
    public static final int THIS_ACCOUNT_HAS_BEEN_PERMANENTLY_BANNED_1 = 1806; // Ð’Ð°Ñˆ
    // Ð°ÐºÐºÐ°ÑƒÐ½Ñ‚
    // Ð±ÐµÑ�Ñ�Ñ€Ð¾Ñ‡Ð½Ð¾
    // Ð·Ð°Ð±Ð»Ð¾ÐºÐ¸Ñ€Ð¾Ð²Ð°Ð½,
    // Ñ‚Ð°Ðº
    // ÐºÐ°Ðº
    // Ð¾Ð½
    // Ð±Ñ‹Ð»
    // Ð²
    // Ñ‚Ñ€ÐµÑ‚Ð¸Ð¹
    // Ñ€Ð°Ð·
    // Ð·Ð°Ð¼ÐµÑ‡ÐµÐ½
    // Ð²
    // Ð½ÐµÐ»ÐµÐ³Ð°Ð»ÑŒÐ½Ñ‹Ñ…
    // Ð´ÐµÐ½ÐµÐ¶Ð½Ñ‹Ñ…
    // Ñ�Ð´ÐµÐ»ÐºÐ°Ñ….
    // Ð—Ð°
    // Ð´Ð¾Ð¿Ð¾Ð»Ð½Ð¸Ñ‚ÐµÐ»ÑŒÐ½Ð¾Ð¹
    // Ð¸Ð½Ñ„Ð¾Ñ€Ð¼Ð°Ñ†Ð¸ÐµÐ¹
    // Ð¾Ð±Ñ€Ð°Ñ‚Ð¸Ñ‚ÐµÑ�ÑŒ
    // Ð²
    // Ð¦ÐµÐ½Ñ‚Ñ€
    // ÐŸÐ¾Ð´Ð´ÐµÑ€Ð¶ÐºÐ¸.
    public static final int THIS_ACCOUNT_HAS_BEEN_SUSPENDED_FOR_30_DAYS_2 = 1807; // Ð’Ð°Ñˆ
    // Ð°ÐºÐºÐ°ÑƒÐ½Ñ‚
    // Ð·Ð°Ð±Ð»Ð¾ÐºÐ¸Ñ€Ð¾Ð²Ð°Ð½
    // Ð½Ð°
    // 30
    // Ð´Ð½ÐµÐ¹,
    // Ñ‚Ð°Ðº
    // ÐºÐ°Ðº
    // Ð²Ñ‹
    // Ð·Ð°Ð¼ÐµÑˆÐ°Ð½Ñ‹
    // Ð²
    // Ð½ÐµÐ»ÐµÐ³Ð°Ð»ÑŒÐ½Ñ‹Ñ…
    // Ð´ÐµÐ½ÐµÐ¶Ð½Ñ‹Ñ…
    // Ð¾Ð¿ÐµÑ€Ð°Ñ†Ð¸Ñ�Ñ….
    // Ð—Ð°
    // Ð´Ð¾Ð¿Ð¾Ð»Ð½Ð¸Ñ‚ÐµÐ»ÑŒÐ½Ð¾Ð¹
    // Ð¸Ð½Ñ„Ð¾Ñ€Ð¼Ð°Ñ†Ð¸ÐµÐ¹
    // Ð¾Ð±Ñ€Ð°Ñ‚Ð¸Ñ‚ÐµÑ�ÑŒ
    // Ð²
    // Ð¦ÐµÐ½Ñ‚Ñ€
    // ÐŸÐ¾Ð´Ð´ÐµÑ€Ð¶ÐºÐ¸.
    public static final int THIS_ACCOUNT_HAS_BEEN_PERMANENTLY_BANNED_2 = 1808; // Ð’Ð°Ñˆ
    // Ð°ÐºÐºÐ°ÑƒÐ½Ñ‚
    // Ð±ÐµÑ�Ñ�Ñ€Ð¾Ñ‡Ð½Ð¾
    // Ð·Ð°Ð±Ð»Ð¾ÐºÐ¸Ñ€Ð¾Ð²Ð°Ð½,
    // Ñ‚Ð°Ðº
    // ÐºÐ°Ðº
    // Ð²Ñ‹
    // Ð·Ð°Ð¼ÐµÑˆÐ°Ð½Ñ‹
    // Ð²
    // Ð½ÐµÐ»ÐµÐ³Ð°Ð»ÑŒÐ½Ñ‹Ñ…
    // Ð´ÐµÐ½ÐµÐ¶Ð½Ñ‹Ñ…
    // Ð¾Ð¿ÐµÑ€Ð°Ñ†Ð¸Ñ�Ñ….
    // Ð—Ð°
    // Ð´Ð¾Ð¿Ð¾Ð»Ð½Ð¸Ñ‚ÐµÐ»ÑŒÐ½Ð¾Ð¹
    // Ð¸Ð½Ñ„Ð¾Ñ€Ð¼Ð°Ñ†Ð¸ÐµÐ¹
    // Ð¾Ð±Ñ€Ð°Ñ‚Ð¸Ñ‚ÐµÑ�ÑŒ
    // Ð²
    // Ð¦ÐµÐ½Ñ‚Ñ€
    // ÐŸÐ¾Ð´Ð´ÐµÑ€Ð¶ÐºÐ¸.
    public static final int ACCOUNT_OWNER_MUST_BE_VERIFIED_IN_ORDER_TO_USE_THIS_ACCOUNT_AGAIN = 1809; // Ð’Ð°Ñˆ
    // Ð°ÐºÐºÐ°ÑƒÐ½Ñ‚,
    // Ð²ÐµÑ€Ð¾Ñ�Ñ‚Ð½Ð¾,
    // Ð¿Ñ€Ð¾Ð²ÐµÑ€Ñ�ÐµÑ‚Ñ�Ñ�.
    // Ð—Ð°
    // Ð¸Ð½Ñ„Ð¾Ñ€Ð¼Ð°Ñ†Ð¸ÐµÐ¹
    // Ð¾
    // Ð¿Ñ€Ð¾Ñ†ÐµÐ´ÑƒÑ€Ðµ
    // Ð¿Ñ€Ð¾Ð²ÐµÑ€ÐºÐ¸
    // Ð°ÐºÐºÐ°ÑƒÐ½Ñ‚Ð¾Ð²
    // Ð¾Ð±Ñ€Ð°Ñ‰Ð°Ð¹Ñ‚ÐµÑ�ÑŒ
    // Ð²
    // Ð¦ÐµÐ½Ñ‚Ñ€
    // ÐŸÐ¾Ð´Ð´ÐµÑ€Ð¶ÐºÐ¸.
    public static final int THE_REFUSE_INVITATION_STATE_HAS_BEEN_ACTIVATED = 1810; // Ð¡Ñ‚Ð°Ñ‚ÑƒÑ�
    // Ð¾Ñ‚ÐºÐ°Ð·Ð°
    // Ð¾Ñ‚
    // Ð¿Ñ€Ð¸Ð³Ð»Ð°ÑˆÐµÐ½Ð¸Ð¹
    // Ð°ÐºÑ‚Ð¸Ð²Ð¸Ñ€Ð¾Ð²Ð°Ð½.
    public static final int THE_REFUSE_INVITATION_STATE_HAS_BEEN_REMOVED = 1811; // Ð¡Ñ‚Ð°Ñ‚ÑƒÑ�
    // Ð¾Ñ‚ÐºÐ°Ð·Ð°
    // Ð¾Ñ‚
    // Ð¿Ñ€Ð¸Ð³Ð»Ð°ÑˆÐµÐ½Ð¸Ð¹
    // Ð´ÐµÐ·Ð°ÐºÑ‚Ð¸Ð²Ð¸Ñ€Ð¾Ð²Ð°Ð½.
    public static final int SINCE_THE_REFUSE_INVITATION_STATE_IS_CURRENTLY_ACTIVATED_NO_INVITATION_CAN_BE_MADE = 1812; // Ð¡
    // Ð¼Ð¾Ð¼ÐµÐ½Ñ‚Ð°
    // Ð°ÐºÑ‚Ð¸Ð²Ð°Ñ†Ð¸Ð¸
    // Ñ�Ñ‚Ð°Ñ‚ÑƒÑ�Ð°
    // Ð¾Ñ‚ÐºÐ°Ð·Ð°
    // Ð¾Ñ‚
    // Ð¿Ñ€Ð¸Ð³Ð»Ð°ÑˆÐµÐ½Ð¸Ð¹
    // Ð½Ðµ
    // Ð¼Ð¾Ð³ÑƒÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ñ�Ð´ÐµÐ»Ð°Ð½Ñ‹
    // Ð¿Ñ€Ð¸Ð³Ð»Ð°ÑˆÐµÐ½Ð¸Ñ�.
    public static final int THERE_IS_S1_HOUR_AND_S2_MINUTE_LEFT_OF_THE_FIXED_USAGE_TIME = 1813; // $s1:
    // Ð¾Ñ�Ñ‚Ð°Ð»Ð¾Ñ�ÑŒ
    // $s2
    // Ñ‡
    // Ð²Ñ€ÐµÐ¼ÐµÐ½Ð¸
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ð½Ð¸Ñ�.
    public static final int S2_MINUTE_OF_USAGE_TIME_ARE_LEFT_FOR_S1 = 1814; // $s1:
    // Ð¾Ñ�Ñ‚Ð°Ð»Ð¾Ñ�ÑŒ
    // $s2
    // Ð¼Ð¸Ð½
    // Ð²Ñ€ÐµÐ¼ÐµÐ½Ð¸
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ð½Ð¸Ñ�.
    public static final int S2_WAS_DROPPED_IN_THE_S1_REGION = 1815; // $s2
    // Ð¿Ð¾Ñ�Ð²Ð¸Ð»Ñ�Ñ�
    // Ð² Ñ€Ð°Ð¹Ð¾Ð½Ðµ
    // $s1.
    public static final int THE_OWNER_OF_S2_HAS_APPEARED_IN_THE_S1_REGION = 1816; // ÐžÐ±Ð»Ð°Ð´Ð°Ñ‚ÐµÐ»ÑŒ
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ð°
    // $s2
    // Ð¿Ð¾Ñ�Ð²Ð¸Ð»Ñ�Ñ�
    // Ð²
    // Ñ€Ð°Ð¹Ð¾Ð½Ðµ
    // $s1.
    public static final int S2_OWNER_HAS_LOGGED_INTO_THE_S1_REGION = 1817; // ÐžÐ±Ð»Ð°Ð´Ð°Ñ‚ÐµÐ»ÑŒ
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ð°
    // $s2
    // Ð½Ð°Ñ…Ð¾Ð´Ð¸Ñ‚Ñ�Ñ�
    // Ð²
    // Ñ€Ð°Ð¹Ð¾Ð½Ðµ
    // $s1.
    public static final int S1_HAS_DISAPPEARED_CW = 1818; // $s1 Ð¸Ñ�Ñ‡ÐµÐ·.
    public static final int AN_EVIL_IS_PULSATING_FROM_S2_IN_S1 = 1819; // Ð—Ð»Ð¾
    // Ð¿ÑƒÐ»ÑŒÑ�Ð¸Ñ€ÑƒÐµÑ‚
    // Ð¸Ð· $s2
    // Ð² $s1.
    public static final int S1_IS_CURRENTLY_ASLEEP = 1820; // $s1 Ð² Ð½Ð°Ñ�Ñ‚Ð¾Ñ�Ñ‰ÐµÐµ
    // Ð²Ñ€ÐµÐ¼Ñ� Ñ�Ð¿Ð¸Ñ‚.
    public static final int S2_S_EVIL_PRESENCE_IS_FELT_IN_S1 = 1821; // Ð�ÑƒÑ€Ð° Ð·Ð»Ð°
    // $s2
    // Ñ‡ÑƒÐ²Ñ�Ñ‚Ð²ÑƒÐµÑ‚Ñ�Ñ�
    // Ð² $s1.
    public static final int S1_HAS_BEEN_SEALED = 1822; // $s1 Ð·Ð°Ð¿ÐµÑ‡Ð°Ñ‚Ð°Ð½.
    public static final int THE_REGISTRATION_PERIOD_FOR_A_CLAN_HALL_WAR_HAS_ENDED = 1823; // ÐŸÐµÑ€Ð¸Ð¾Ð´
    // Ñ€ÐµÐ³Ð¸Ñ�Ñ‚Ñ€Ð°Ñ†Ð¸Ð¸
    // Ð²Ð¾Ð¹Ð½Ñ‹
    // Ð·Ð°
    // Ñ…Ð¾Ð»Ð»
    // ÐºÐ»Ð°Ð½Ð°
    // Ð·Ð°ÐºÐ¾Ð½Ñ‡ÐµÐ½.
    public static final int YOU_HAVE_BEEN_REGISTERED_FOR_A_CLAN_HALL_WAR__PLEASE_MOVE_TO_THE_LEFT_SIDE_OF_THE_CLAN_HALL_S = 1824; // Ð’Ñ‹
    // Ð·Ð°Ñ€ÐµÐ³Ð¸Ñ�Ñ‚Ñ€Ð¸Ñ€Ð¾Ð²Ð°Ð»Ð¸Ñ�ÑŒ
    // Ð´Ð»Ñ�
    // Ð²Ð¾Ð¹Ð½Ñ‹
    // Ð·Ð°
    // Ñ…Ð¾Ð»Ð»
    // ÐºÐ»Ð°Ð½Ð°.
    // ÐŸÐ¾Ð¶Ð°Ð»ÑƒÐ¹Ñ�Ñ‚Ð°,
    // Ð¿Ñ€Ð¾Ñ�Ð»ÐµÐ´ÑƒÐ¹Ñ‚Ðµ
    // Ð½Ð°
    // Ð»ÐµÐ²Ñ‹Ð¹
    // ÐºÑ€Ð°Ð¹
    // Ð°Ñ€ÐµÐ½Ñ‹
    // Ñ…Ð¾Ð»Ð»Ð°
    // ÐºÐ»Ð°Ð½Ð°
    // Ð¸
    // Ð¿Ñ€Ð¸Ð³Ð¾Ñ‚Ð¾Ð²ÑŒÑ‚ÐµÑ�ÑŒ.
    public static final int YOU_HAVE_FAILED_IN_YOUR_ATTEMPT_TO_REGISTER_FOR_THE_CLAN_HALL_WAR_PLEASE_TRY_AGAIN = 1825; // Ð’Ð°ÑˆÐ°
    // Ð¿Ð¾Ð¿Ñ‹Ñ‚ÐºÐ°
    // Ð·Ð°Ñ€ÐµÐ³Ð¸Ñ�Ñ‚Ñ€Ð¸Ñ€Ð¾Ð²Ð°Ñ‚ÑŒÑ�Ñ�
    // Ð´Ð»Ñ�
    // Ð²Ð¾Ð¹Ð½Ñ‹
    // Ð·Ð°
    // Ñ…Ð¾Ð»Ð»
    // ÐºÐ»Ð°Ð½Ð°
    // Ð½Ðµ
    // ÑƒÐ´Ð°Ð»Ð°Ñ�ÑŒ.
    // ÐŸÐ¾Ð¶Ð°Ð»ÑƒÐ¹Ñ�Ñ‚Ð°,
    // Ð¿Ð¾Ð¿Ñ‹Ñ‚Ð°Ð¹Ñ‚ÐµÑ�ÑŒ
    // Ñ�Ð½Ð¾Ð²Ð°.
    public static final int IN_S1_MINUTES_THE_GAME_WILL_BEGIN_ALL_PLAYERS_MUST_HURRY_AND_MOVE_TO_THE_LEFT_SIDE_OF_THE_CLAN = 1826; // Ð˜Ð³Ñ€Ð°
    // Ð½Ð°Ñ‡Ð½ÐµÑ‚Ñ�Ñ�
    // Ñ‡ÐµÑ€ÐµÐ·
    // $s1
    // Ð¼Ð¸Ð½.
    // Ð’Ñ�Ðµ
    // Ð¸Ð³Ñ€Ð¾ÐºÐ¸
    // Ð´Ð¾Ð»Ð¶Ð½Ñ‹
    // Ð¿Ð¾Ñ�Ð¿ÐµÑˆÐ¸Ñ‚ÑŒ
    // Ð¸
    // Ð¾Ñ‚Ð¾Ð¹Ñ‚Ð¸
    // Ð½Ð°
    // Ð»ÐµÐ²Ñ‹Ð¹
    // ÐºÑ€Ð°Ð¹
    // Ð°Ñ€ÐµÐ½Ñ‹
    // Ñ…Ð¾Ð»Ð»Ð°
    // ÐºÐ»Ð°Ð½Ð°.
    public static final int IN_S1_MINUTES_THE_GAME_WILL_BEGIN_ALL_PLAYERS_PLEASE_ENTER_THE_ARENA_NOW = 1827; // Ð˜Ð³Ñ€Ð°
    // Ð½Ð°Ñ‡Ð½ÐµÑ‚Ñ�Ñ�
    // Ñ‡ÐµÑ€ÐµÐ·
    // $s1
    // Ð¼Ð¸Ð½.
    // Ð˜Ð³Ñ€Ð¾ÐºÐ¸,
    // Ð¿Ð¾Ð¶Ð°Ð»ÑƒÐ¹Ñ�Ñ‚Ð°,
    // Ð²Ð¾Ð¹Ð´Ð¸Ñ‚Ðµ
    // Ð½Ð°
    // Ð°Ñ€ÐµÐ½Ñƒ.
    public static final int IN_S1_SECONDS_THE_GAME_WILL_BEGIN = 1828; // Ð˜Ð³Ñ€Ð°
    // Ð½Ð°Ñ‡Ð½ÐµÑ‚Ñ�Ñ�
    // Ñ‡ÐµÑ€ÐµÐ·
    // $s1
    // Ñ�ÐµÐº.
    public static final int THE_COMMAND_CHANNEL_IS_FULL = 1829; // ÐšÐ°Ð½Ð°Ð» ÐšÐ¾Ð¼Ð°Ð½Ð´Ñ‹
    // Ð·Ð°Ð¿Ð¾Ð»Ð½ÐµÐ½.
    public static final int C1_IS_NOT_ALLOWED_TO_USE_THE_PARTY_ROOM_INVITE_COMMAND_PLEASE_UPDATE_THE_WAITING_LIST = 1830; // $c1
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒ
    // ÐºÐ¾Ð¼Ð°Ð½Ð´Ñƒ
    // Ð¿Ñ€Ð¸Ð³Ð»Ð°ÑˆÐµÐ½Ð¸Ñ�
    // Ð²
    // ÐºÐ¾Ð¼Ð½Ð°Ñ‚Ñƒ
    // Ð³Ñ€ÑƒÐ¿Ð¿Ñ‹.
    // ÐŸÐ¾Ð¶Ð°Ð»ÑƒÐ¹Ñ�Ñ‚Ð°,
    // Ð¾Ð±Ð½Ð¾Ð²Ð¸Ñ‚Ðµ
    // Ð»Ð¸Ñ�Ñ‚
    // Ð¾Ð¶Ð¸Ð´Ð°Ð½Ð¸Ñ�.
    public static final int C1_DOES_NOT_MEET_THE_CONDITIONS_OF_THE_PARTY_ROOM_PLEASE_UPDATE_THE_WAITING_LIST = 1831; // $c1
    // Ð½Ðµ
    // Ñ�Ð¾Ð¾Ñ‚Ð²ÐµÑ‚Ñ�Ñ‚Ð²ÑƒÐµÑ‚
    // ÑƒÑ�Ð»Ð¾Ð²Ð¸Ñ�Ð¼
    // ÐºÐ¾Ð¼Ð½Ð°Ñ‚Ñ‹
    // Ð³Ñ€ÑƒÐ¿Ð¿Ñ‹.
    // ÐŸÐ¾Ð¶Ð°Ð»ÑƒÐ¹Ñ�Ñ‚Ð°,
    // Ð¾Ð±Ð½Ð¾Ð²Ð¸Ñ‚Ðµ
    // Ð»Ð¸Ñ�Ñ‚
    // Ð¾Ð¶Ð¸Ð´Ð°Ð½Ð¸Ñ�.
    public static final int ONLY_A_ROOM_LEADER_MAY_INVITE_OTHERS_TO_A_PARTY_ROOM = 1832; // Ð¢Ð¾Ð»ÑŒÐºÐ¾
    // Ñ…Ð¾Ð·Ñ�Ð¸Ð½
    // ÐºÐ¾Ð¼Ð½Ð°Ñ‚Ñ‹
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð¿Ñ€Ð¸Ð³Ð»Ð°ÑˆÐ°Ñ‚ÑŒ
    // Ð¾Ñ�Ñ‚Ð°Ð»ÑŒÐ½Ñ‹Ñ…
    // Ð²
    // ÐºÐ¾Ð¼Ð½Ð°Ñ‚Ñƒ
    // Ð³Ñ€ÑƒÐ¿Ð¿Ñ‹.
    public static final int ALL_OF_S1_WILL_BE_DROPPED_WOULD_YOU_LIKE_TO_CONTINUE = 1833; // Ð’Ñ�Ðµ
    // $s1
    // Ð±ÑƒÐ´ÑƒÑ‚
    // Ñ�Ð±Ñ€Ð¾ÑˆÐµÐ½Ñ‹.
    // ÐŸÑ€Ð¾Ð´Ð¾Ð»Ð¶Ð¸Ñ‚ÑŒ?
    public static final int THE_PARTY_ROOM_IS_FULL_NO_MORE_CHARACTERS_CAN_BE_INVITED_IN = 1834; // ÐšÐ¾Ð¼Ð½Ð°Ñ‚Ð°
    // Ð³Ñ€ÑƒÐ¿Ð¿Ñ‹
    // Ð·Ð°Ð¿Ð¾Ð»Ð½ÐµÐ½Ð°.
    // Ð‘Ð¾Ð»ÑŒÑˆÐµ
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶ÐµÐ¹
    // Ð¿Ñ€Ð¸Ð³Ð»Ð°Ñ�Ð¸Ñ‚ÑŒ
    // Ð½ÐµÐ»ÑŒÐ·Ñ�.
    public static final int S1_IS_FULL_AND_CANNOT_ACCEPT_ADDITIONAL_CLAN_MEMBERS_AT_THIS_TIME = 1835; // $s
    // Ð½Ð°
    // Ð´Ð°Ð½Ð½Ñ‹Ð¹
    // Ð¼Ð¾Ð¼ÐµÐ½Ñ‚
    // Ð·Ð°Ð¿Ð¾Ð»Ð½ÐµÐ½Ð°
    // Ð¸
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð¿Ñ€Ð¸Ð½Ñ�Ñ‚ÑŒ
    // Ð´Ð¾Ð¿Ð¾Ð»Ð½Ð¸Ñ‚ÐµÐ»ÑŒÐ½Ñ‹Ñ…
    // Ñ‡Ð»ÐµÐ½Ð¾Ð².
    public static final int YOU_CANNOT_JOIN_A_CLAN_ACADEMY_BECAUSE_YOU_HAVE_SUCCESSFULLY_COMPLETED_YOUR_2ND_CLASS_TRANSFER = 1836; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¿Ñ€Ð¸Ñ�Ð¾ÐµÐ´Ð¸Ð½Ð¸Ñ‚ÑŒÑ�Ñ�
    // Ðº
    // Ð�ÐºÐ°Ð´ÐµÐ¼Ð¸Ð¸
    // ÐšÐ»Ð°Ð½Ð°,
    // Ñ‚Ð°Ðº
    // ÐºÐ°Ðº
    // ÑƒÐ¶Ðµ
    // Ð²Ñ‹Ð¿Ð¾Ð»Ð½Ð¸Ð»Ð¸
    // ÐºÐ²ÐµÑ�Ñ‚
    // Ð½Ð°
    // Ð²Ñ‚Ð¾Ñ€ÑƒÑŽ
    // Ñ�Ð¼ÐµÐ½Ñƒ
    // Ð¿Ñ€Ð¾Ñ„ÐµÑ�Ñ�Ð¸Ð¸.
    public static final int C1_HAS_SENT_YOU_AN_INVITATION_TO_JOIN_THE_S3_ROYAL_GUARD_UNDER_THE_S2_CLAN_WOULD_YOU_LIKE_TO = 1837; // $c1
    // Ð¿Ñ€Ð¸Ð³Ð»Ð°ÑˆÐ°ÐµÑ‚
    // Ð’Ð°Ñ�
    // Ð²Ñ�Ñ‚ÑƒÐ¿Ð¸Ñ‚ÑŒ
    // Ð²
    // Ð¾Ñ‚Ñ€Ñ�Ð´
    // ÐšÐ¾Ñ€Ð¾Ð»ÐµÐ²Ñ�ÐºÐ¾Ð¹
    // Ð¡Ñ‚Ñ€Ð°Ð¶Ð¸
    // $s3
    // ÐºÐ»Ð°Ð½Ð°
    // $s2.
    // ÐŸÑ€Ð¸Ð½Ñ�Ñ‚ÑŒ?
    public static final int _1_THE_COUPON_CAN_BE_USED_ONCE_PER_CHARACTER = 1838; // ÐšÑƒÐ¿Ð¾Ð½
    // Ð¼Ð¾Ð¶Ð½Ð¾
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒ
    // Ñ‚Ð¾Ð»ÑŒÐºÐ¾
    // 1
    // Ñ€Ð°Ð·.
    public static final int _2_A_USED_SERIAL_NUMBER_MAY_NOT_BE_USED_AGAIN = 1839; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒ
    // Ð°ÐºÑ‚Ð¸Ð²Ð¸Ñ€Ð¾Ð²Ð°Ð½Ð½Ñ‹Ð¹
    // Ñ�ÐµÑ€Ð¸Ð¹Ð½Ñ‹Ð¹
    // Ð½Ð¾Ð¼ÐµÑ€.
    public static final int _3_IF_YOU_ENTER_THE_INCORRECT_SERIAL_NUMBER_MORE_THAN_5_TIMES__N___YOU_MAY_USE_IT_AGAIN_AFTER_A = 1840; // Ð•Ñ�Ð»Ð¸
    // Ð’Ñ‹
    // Ð²Ð²ÐµÐ»Ð¸
    // Ð½ÐµÐ²ÐµÑ€Ð½Ñ‹Ð¹
    // Ñ�ÐµÑ€Ð¸Ð¹Ð½Ñ‹Ð¹
    // Ð½Ð¾Ð¼ÐµÑ€
    // Ð±Ð¾Ð»ÐµÐµ
    // 5-Ñ‚Ð¸
    // Ñ€Ð°Ð·,
    // \\n
    // Ñ‚Ð¾
    // Ñ�Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð²Ð²ÐµÑ�Ñ‚Ð¸
    // ÐµÐ³Ð¾
    // Ñ�Ð½Ð¾Ð²Ð°
    // Ñ‚Ð¾Ð»ÑŒÐºÐ¾
    // Ñ�Ð¿ÑƒÑ�Ñ‚Ñ�
    // Ð½ÐµÐºÐ¾Ñ‚Ð¾Ñ€Ð¾Ðµ
    // Ð²Ñ€ÐµÐ¼Ñ�.
    public static final int THIS_CLAN_HALL_WAR_HAS_BEEN_CANCELLED__NOT_ENOUGH_CLANS_HAVE_REGISTERED = 1841; // Ð­Ñ‚Ð°
    // Ð²Ð¾Ð¹Ð½Ð°
    // Ð·Ð°
    // Ñ…Ð¾Ð»Ð»
    // ÐºÐ»Ð°Ð½Ð°
    // Ð±Ñ‹Ð»Ð°
    // Ð¾Ñ‚Ð¼ÐµÐ½ÐµÐ½Ð°
    // Ð¸Ð·-Ð·Ð°
    // Ð½ÐµÐ´Ð¾Ñ�Ñ‚Ð°Ñ‚Ð¾Ñ‡Ð½Ð¾Ð³Ð¾
    // ÐºÐ¾Ð»Ð¸Ñ‡ÐµÑ�Ñ‚Ð²Ð°
    // Ð·Ð°Ñ€ÐµÐ³Ð¸Ñ�Ñ‚Ñ€Ð¸Ñ€Ð¾Ð²Ð°Ð½Ð½Ñ‹Ñ…
    // ÐºÐ»Ð°Ð½Ð¾Ð².
    public static final int S1_WISHES_TO_SUMMON_YOU_FROM_S2_DO_YOU_ACCEPT = 1842; // $c1
    // Ñ…Ð¾Ñ‡ÐµÑ‚
    // Ð¿Ñ€Ð¸Ð·Ð²Ð°Ñ‚ÑŒ
    // Ð’Ð°Ñ�
    // Ð¸Ð·
    // $s2.
    // Ð’Ñ‹
    // Ñ�Ð¾Ð³Ð»Ð°Ñ�Ð½Ñ‹?
    public static final int S1_IS_ENGAGED_IN_COMBAT_AND_CANNOT_BE_SUMMONED = 1843; // $c1
    // Ð²
    // Ð±Ð¾ÑŽ.
    public static final int S1_IS_DEAD_AT_THE_MOMENT_AND_CANNOT_BE_SUMMONED = 1844; // $c1
    // Ñ�ÐµÐ¹Ñ‡Ð°Ñ�
    // Ð¼ÐµÑ€Ñ‚Ð²
    // Ð¸
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð¿Ñ€Ð¸Ð·Ð²Ð°Ð½.
    public static final int HERO_WEAPONS_CANNOT_BE_DESTROYED = 1845; // ÐžÑ€ÑƒÐ¶Ð¸Ðµ
    // Ð“ÐµÑ€Ð¾Ñ� Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // ÑƒÐ½Ð¸Ñ‡Ñ‚Ð¾Ð¶ÐµÐ½Ð¾.
    public static final int YOU_ARE_TOO_FAR_AWAY_FROM_THE_FENRIR_TO_MOUNT_IT = 1846; // Ð’Ñ‹
    // Ñ�Ð»Ð¸ÑˆÐºÐ¾Ð¼
    // Ð´Ð°Ð»ÐµÐºÐ¾
    // Ð¾Ñ‚
    // Ð¿Ð¸Ñ‚Ð¾Ð¼Ñ†Ð°,
    // Ñ‡Ñ‚Ð¾Ð±Ñ‹
    // Ð¾Ñ�ÐµÐ´Ð»Ð°Ñ‚ÑŒ
    // ÐµÐ³Ð¾.
    public static final int YOU_CAUGHT_A_FISH_S1_IN_LENGTH = 1847; // Ð’Ñ‹ Ð¿Ð¾Ð¹Ð¼Ð°Ð»Ð¸
    // Ñ€Ñ‹Ð±Ñƒ
    // Ð´Ð»Ð¸Ð½Ð¾Ð¹
    // $s1.
    public static final int BECAUSE_OF_THE_SIZE_OF_FISH_CAUGHT_YOU_WILL_BE_REGISTERED_IN_THE_RANKING = 1848; // Ð”Ð»Ð¸Ð½Ð°
    // Ð¿Ð¾Ð¹Ð¼Ð°Ð½Ð½Ð¾Ð¹
    // Ð²Ð°Ð¼Ð¸
    // Ñ€Ñ‹Ð±Ñ‹
    // Ð²Ð¿ÐµÑ‡Ð°Ñ‚Ð»Ñ�ÐµÑ‚.
    // Ð’Ñ‹
    // Ð±ÑƒÐ´ÐµÑ‚Ðµ
    // Ð·Ð°Ð½ÐµÑ�ÐµÐ½Ñ‹
    // Ð²
    // Ñ€ÐµÐ¹Ñ‚Ð¸Ð½Ð³.
    public static final int ALL_OF_S1_WILL_BE_DISCARDED_WOULD_YOU_LIKE_TO_CONTINUE = 1849; // Ð’Ñ�Ðµ
    // $s1
    // Ð±ÑƒÐ´ÑƒÑ‚
    // Ð¾Ñ‚ÐºÐ»Ð¾Ð½ÐµÐ½Ñ‹.
    // ÐŸÑ€Ð¾Ð´Ð¾Ð»Ð¶Ð¸Ñ‚ÑŒ?
    public static final int THE_CAPTAIN_OF_THE_ORDER_OF_KNIGHTS_CANNOT_BE_APPOINTED = 1850; // ÐšÐ°Ð¿Ð¸Ñ‚Ð°Ð½
    // Ð Ñ‹Ñ†Ð°Ñ€Ñ�ÐºÐ¾Ð³Ð¾
    // ÐžÑ€Ð´ÐµÐ½Ð°
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð½Ð°Ð·Ð½Ð°Ñ‡ÐµÐ½.
    public static final int THE_CAPTAIN_OF_THE_ROYAL_GUARD_CANNOT_BE_APPOINTED = 1851; // ÐšÐ°Ð¿Ð¸Ñ‚Ð°Ð½
    // ÐšÐ¾Ñ€Ð¾Ð»ÐµÐ²Ñ�ÐºÐ¾Ð¹
    // Ñ�Ñ‚Ñ€Ð°Ð¶Ð¸
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð½Ð°Ð·Ð½Ð°Ñ‡ÐµÐ½.
    public static final int THE_ATTEMPT_TO_ACQUIRE_THE_SKILL_HAS_FAILED_BECAUSE_OF_AN_INSUFFICIENT_CLAN_REPUTATION_SCORE = 1852; // ÐŸÐ¾Ð¿Ñ‹Ñ‚ÐºÐ°
    // Ð¿Ð¾Ð»ÑƒÑ‡Ð¸Ñ‚ÑŒ
    // ÑƒÐ¼ÐµÐ½Ð¸Ðµ
    // Ð½Ðµ
    // ÑƒÐ´Ð°Ð»Ð°Ñ�ÑŒ
    // Ð¸Ð·-Ð·Ð°
    // Ð½ÐµÐ´Ð¾Ñ�Ñ‚Ð°Ñ‚Ð¾Ñ‡Ð½Ð¾Ð³Ð¾
    // ÐºÐ¾Ð»Ð¸Ñ‡ÐµÑ�Ñ‚Ð²Ð°
    // ÐžÑ‡ÐºÐ¾Ð²
    // Ð ÐµÐ¿ÑƒÑ‚Ð°Ñ†Ð¸Ð¸
    // ÐšÐ»Ð°Ð½Ð°.
    public static final int QUANTITY_ITEMS_OF_THE_SAME_TYPE_CANNOT_BE_EXCHANGED_AT_THE_SAME_TIME = 1853; // ÐšÐ¾Ð»Ð¸Ñ‡ÐµÑ�Ñ‚Ð²ÐµÐ½Ð½Ñ‹Ðµ
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ñ‹
    // Ð¾Ð´Ð½Ð¾Ð³Ð¾
    // Ñ‚Ð¸Ð¿Ð°
    // Ð½Ðµ
    // Ð¼Ð¾Ð³ÑƒÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð¾Ð±Ð¼ÐµÐ½ÐµÐ½Ñ‹
    // Ð¾Ð´Ð½Ð¾Ð²Ñ€ÐµÐ¼ÐµÐ½Ð½Ð¾.
    public static final int THE_ITEM_WAS_CONVERTED_SUCCESSFULLY = 1854; // ÐŸÑ€ÐµÐ´Ð¼ÐµÑ‚
    // ÑƒÑ�Ð¿ÐµÑˆÐ½Ð¾
    // Ð¿Ñ€ÐµÐ¾Ð±Ñ€Ð°Ð·Ð¾Ð²Ð°Ð½.
    public static final int ANOTHER_MILITARY_UNIT_IS_ALREADY_USING_THAT_NAME_PLEASE_ENTER_A_DIFFERENT_NAME = 1855; // Ð­Ñ‚Ð¾
    // Ð¸Ð¼Ñ�
    // ÑƒÐ¶Ðµ
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·ÑƒÐµÑ‚Ñ�Ñ�
    // Ð±Ð¾ÐµÐ²Ñ‹Ð¼
    // Ð¿Ð¾Ð´Ñ€Ð°Ð·Ð´ÐµÐ»ÐµÐ½Ð¸ÐµÐ¼.
    // Ð�Ð°Ð·Ð½Ð°Ñ‡ÑŒÑ‚Ðµ
    // Ð´Ñ€ÑƒÐ³Ð¾Ðµ
    // Ð¸Ð¼Ñ�.
    public static final int SINCE_YOUR_OPPONENT_IS_NOW_THE_OWNER_OF_S1_THE_OLYMPIAD_HAS_BEEN_CANCELLED = 1856; // Ð¢Ð°Ðº
    // ÐºÐ°Ðº
    // Ð²Ð°Ñˆ
    // Ð¿Ñ€Ð¾Ñ‚Ð¸Ð²Ð½Ð¸Ðº
    // Ð·Ð°Ð²Ð»Ð°Ð´ÐµÐ»
    // $s1,
    // ÐžÐ»Ð¸Ð¼Ð¿Ð¸Ð°Ð´Ð°
    // Ð¾Ñ‚Ð¼ÐµÐ½ÐµÐ½Ð°.
    public static final int SINCE_YOU_NOW_OWN_S1_YOU_CANNOT_PARTICIPATE_IN_THE_OLYMPIAD = 1857; // $c1
    // Ð·Ð°Ð²Ð¾ÐµÐ²Ð°Ð»
    // $s2
    // Ð¸
    // Ð¿Ð¾Ñ‚ÐµÑ€Ñ�Ð»
    // Ð²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾Ñ�Ñ‚ÑŒ
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð¸Ñ�
    // Ð²
    // ÐžÐ»Ð¸Ð¼Ð¿Ð¸Ð°Ð´Ðµ.
    public static final int YOU_CANNOT_PARTICIPATE_IN_THE_OLYMPIAD_WHILE_DEAD = 1858; // $c1
    // Ð¿Ð¾Ð³Ð¸Ð±
    // Ð¸
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð¿Ñ€Ð¸Ð½Ñ�Ñ‚ÑŒ
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð¸Ðµ
    // Ð²
    // ÐžÐ»Ð¸Ð¼Ð¿Ð¸Ð°Ð´Ðµ.
    public static final int YOU_HAVE_EXCEEDED_THE_QUANTITY_THAT_CAN_BE_MOVED_AT_ONE_TIME = 1859; // Ð’Ñ‹
    // Ð¿Ñ€ÐµÐ²Ñ‹Ñ�Ð¸Ð»Ð¸
    // Ð¼Ð°ÐºÑ�Ð¸Ð¼Ð°Ð»ÑŒÐ½Ð¾Ðµ
    // ÐºÐ¾Ð»Ð¸Ñ‡ÐµÑ�Ñ‚Ð²Ð¾
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ð¾Ð²,
    // ÐºÐ¾Ñ‚Ð¾Ñ€Ð¾Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð¿ÐµÑ€ÐµÐ¼ÐµÑ‰ÐµÐ½Ð¾
    // Ð·Ð°
    // Ð¾Ð´Ð¸Ð½
    // Ñ€Ð°Ð·.
    public static final int THE_CLAN_REPUTATION_SCORE_IS_TOO_LOW = 1860; // Ð£
    // Ð²Ð°Ñ�
    // Ñ�Ð»Ð¸ÑˆÐºÐ¾Ð¼
    // Ð¼Ð°Ð»Ð¾
    // Ð¾Ñ‡ÐºÐ¾Ð²
    // ÐºÐ»Ð°Ð½Ð°.
    public static final int THE_CLANS_CREST_HAS_BEEN_DELETED = 1861; // Ð­Ð¼Ð±Ð»ÐµÐ¼Ð°
    // ÐºÐ»Ð°Ð½Ð°
    // ÑƒÐ´Ð°Ð»ÐµÐ½Ð°.
    public static final int THE_CLAN_SKILL_WILL_BE_ACTIVATED_BECAUSE_THE_CLANS_REPUTATION_SCORE_HAS_REACHED_TO_0_OR_HIGHER = 1862; // Ð¡
    // Ñ‚Ð¾Ð³Ð¾
    // Ð¼Ð¾Ð¼ÐµÐ½Ñ‚Ð°,
    // ÐºÐ°Ðº
    // ÐºÐ¾Ð»Ð¸Ñ‡ÐµÑ�Ñ‚Ð²Ð¾
    // Ð¾Ñ‡ÐºÐ¾Ð²
    // Ñ€ÐµÐ¿ÑƒÑ‚Ð°Ñ†Ð¸Ð¸
    // Ñ�Ñ‚Ð°Ð»Ð¾
    // 0
    // Ð¸Ð»Ð¸
    // Ð²Ñ‹ÑˆÐµ,
    // ÐºÐ»Ð°Ð½Ð¾Ð²Ñ‹Ðµ
    // ÑƒÐ¼ÐµÐ½Ð¸Ðµ
    // Ð°ÐºÑ‚Ð¸Ð²Ð½Ñ‹.
    public static final int S1_PURCHASED_A_CLAN_ITEM_REDUCING_THE_CLAN_REPUTATION_BY_S2_POINTS = 1863; // $c1
    // Ð¿Ñ€Ð¸Ð¾Ð±Ñ€ÐµÐ»
    // ÐºÐ»Ð°Ð½Ð¾Ð²Ñ‹Ð¹
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚,
    // Ð¿Ð¾Ð½Ð¸Ð¶Ð°ÑŽÑ‰Ð¸Ð¹
    // Ñ€ÐµÐ¿ÑƒÑ‚Ð°Ñ†Ð¸ÑŽ
    // ÐºÐ»Ð°Ð½Ð°
    // Ð½Ð°
    // $s2.
    public static final int THE_PET_SERVITOR_IS_UNRESPONSIVE_AND_WILL_NOT_OBEY_ANY_ORDERS = 1864; // Ð’Ð°Ñˆ
    // Ð¿Ð¸Ñ‚Ð¾Ð¼ÐµÑ†/Ñ�Ð»ÑƒÐ³Ð°
    // Ð½Ðµ
    // Ñ€ÐµÐ°Ð³Ð¸Ñ€ÑƒÐµÑ‚
    // Ð¸
    // Ð½Ðµ
    // Ð±ÑƒÐ´ÐµÑ‚
    // Ð¿Ð¾Ð´Ñ‡Ð¸Ð½Ñ�Ñ‚ÑŒÑ�Ñ�
    // Ð¿Ñ€Ð¸ÐºÐ°Ð·Ð°Ð¼.
    public static final int THE_PET_SERVITOR_IS_CURRENTLY_IN_A_STATE_OF_DISTRESS = 1865; // Ð’Ð°Ñˆ
    // Ñ�Ð»ÑƒÐ³Ð°/Ð¿Ð¸Ñ‚Ð¾Ð¼ÐµÑ†
    // ÑƒÐ³Ð½ÐµÑ‚ÐµÐ½.
    public static final int MP_WAS_REDUCED_BY_S1 = 1866; // MP Ð±Ñ‹Ð»Ð¸ Ð¿Ð¾Ð½Ð¸Ð¶ÐµÐ½Ñ‹ Ð½Ð°
    // $s1.
    public static final int YOUR_OPPONENTS_MP_WAS_REDUCED_BY_S1 = 1867; // MP
    // Ð’Ð°ÑˆÐµÐ³Ð¾
    // Ð¿Ñ€Ð¾Ñ‚Ð¸Ð²Ð½Ð¸ÐºÐ°
    // Ð±Ñ‹Ð»Ð¸
    // Ð¿Ð¾Ð½Ð¸Ð¶ÐµÐ½Ñ‹
    // Ð½Ð°
    // $s1.
    public static final int YOU_CANNOT_EXCHANGE_AN_ITEM_WHILE_IT_IS_BEING_USED = 1868; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¾Ð±Ð¼ÐµÐ½Ñ�Ñ‚ÑŒ
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚,
    // Ð¿Ð¾ÐºÐ°
    // Ð¾Ð½
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·ÑƒÐµÑ‚Ñ�Ñ�.
    public static final int S1_HAS_GRANTED_THE_CHANNELS_MASTER_PARTY_THE_PRIVILEGE_OF_ITEM_LOOTING = 1869; // $c1
    // Ð½Ð°Ð´ÐµÐ»Ñ�ÐµÑ‚Ñ�Ñ�
    // Ð¿Ð¾Ð»Ð½Ð¾Ð¼Ð¾Ñ‡Ð¸Ñ�Ð¼Ð¸
    // Ñ�Ð±Ð¾Ñ€Ð°
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ð¾Ð²
    // Ð³Ð»Ð°Ð²Ð½Ð¾Ð¹
    // Ð³Ñ€ÑƒÐ¿Ð¿Ð¾Ð¹
    // ÐšÐ¾Ð¼Ð°Ð½Ð´Ð½Ð¾Ð³Ð¾
    // ÐšÐ°Ð½Ð°Ð»Ð°.
    public static final int A_COMMAND_CHANNEL_WITH_THE_ITEM_LOOTING_PRIVILEGE_ALREADY_EXISTS = 1870; // ÐšÐ°Ð½Ð°Ð»
    // ÐšÐ¾Ð¼Ð°Ð½Ð´Ñ‹
    // Ñ�
    // Ð¿Ñ€Ð°Ð²Ð°Ð¼Ð¸
    // Ñ�Ð±Ð¾Ñ€Ð°
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ð¾Ð²
    // ÑƒÐ¶Ðµ
    // Ñ�ÑƒÑ‰ÐµÑ�Ñ‚Ð²ÑƒÐµÑ‚.
    public static final int DO_YOU_WANT_TO_DISMISS_S1_FROM_THE_CLAN = 1871; // $c1
    // -
    // Ð¸Ñ�ÐºÐ»ÑŽÑ‡Ð¸Ñ‚ÑŒ
    // Ð¸Ð·
    // ÐºÐ»Ð°Ð½Ð°?
    public static final int YOU_HAVE_S1_HOURS_AND_S2_MINUTES_LEFT = 1872; // ÐžÑ�Ñ‚Ð°Ð»Ð¾Ñ�ÑŒ:
    // $s1Ñ‡
    // $s2
    // Ð¼Ð¸Ð½.
    public static final int THERE_ARE_S1_HOURS_AND_S2_MINUTES_LEFT_IN_THE_FIXED_USE_TIME_FOR_THIS_PC_CARD = 1873; // ÐžÑ�Ñ‚Ð°Ð»Ð¾Ñ�ÑŒ
    // Ñ„Ð¸ÐºÑ�Ð¸Ñ€Ð¾Ð²Ð°Ð½Ð½Ð¾Ð³Ð¾
    // Ð²Ñ€ÐµÐ¼ÐµÐ½Ð¸
    // Ð²
    // Ð¸Ð³Ñ€Ð¾Ð²Ð¾Ð¼
    // ÐºÐ»ÑƒÐ±Ðµ:
    // $s1Ñ‡
    // $s2
    // Ð¼Ð¸Ð½.
    public static final int THERE_ARE_S1_MINUTES_LEFT_FOR_THIS_INDIVIDUAL_USER = 1874; // Ð£
    // Ñ�Ñ‚Ð¾Ð³Ð¾
    // Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÐµÐ»Ñ�
    // Ð¾Ñ�Ñ‚Ð°Ð»Ð¾Ñ�ÑŒ:
    // $s1Ñ‡
    // $s2
    // Ð¼Ð¸Ð½.
    public static final int THERE_ARE_S1_MINUTES_LEFT_IN_THE_FIXED_USE_TIME_FOR_THIS_PC_CARD = 1875; // ÐžÑ�Ñ‚Ð°Ð»Ð¾Ñ�ÑŒ
    // Ñ„Ð¸ÐºÑ�Ð¸Ñ€Ð¾Ð²Ð°Ð½Ð½Ð¾Ð³Ð¾
    // Ð²Ñ€ÐµÐ¼ÐµÐ½Ð¸
    // Ð²
    // Ð¸Ð³Ñ€Ð¾Ð²Ð¾Ð¼
    // ÐºÐ»ÑƒÐ±Ðµ:
    // $s1
    // Ð¼Ð¸Ð½.
    public static final int DO_YOU_WANT_TO_LEAVE_S1_CLAN = 1876; // ÐŸÐ¾ÐºÐ¸Ð½ÑƒÑ‚ÑŒ
    // ÐºÐ»Ð°Ð½ $s1?
    public static final int THE_GAME_WILL_END_IN_S1_MINUTES = 1877; // Ð˜Ð³Ñ€Ð°
    // Ð·Ð°ÐºÐ¾Ð½Ñ‡Ð¸Ñ‚Ñ�Ñ�
    // Ñ‡ÐµÑ€ÐµÐ· $s1
    // Ð¼Ð¸Ð½.
    public static final int THE_GAME_WILL_END_IN_S1_SECONDS = 1878; // Ð˜Ð³Ñ€Ð°
    // Ð·Ð°ÐºÐ¾Ð½Ñ‡Ð¸Ñ‚Ñ�Ñ�
    // Ñ‡ÐµÑ€ÐµÐ· $s1
    // Ñ�ÐµÐº.
    public static final int IN_S1_MINUTES_YOU_WILL_BE_TELEPORTED_OUTSIDE_OF_THE_GAME_ARENA = 1879; // Ð§ÐµÑ€ÐµÐ·
    // $s1
    // Ð¼Ð¸Ð½
    // Ð’Ñ‹
    // Ð±ÑƒÐ´ÐµÑ‚Ðµ
    // Ð²Ñ‹Ð±Ñ€Ð¾ÑˆÐµÐ½Ñ‹
    // Ð¸Ð·
    // Ð¸Ð³Ñ€Ð¾Ð²Ð¾Ð¹
    // Ð°Ñ€ÐµÐ½Ñ‹.
    public static final int IN_S1_SECONDS_YOU_WILL_BE_TELEPORTED_OUTSIDE_OF_THE_GAME_ARENA = 1880; // Ð§ÐµÑ€ÐµÐ·
    // $s1
    // Ñ�ÐµÐº
    // Ð’Ñ‹
    // Ð±ÑƒÐ´ÐµÑ‚Ðµ
    // Ð²Ñ‹Ð±Ñ€Ð¾ÑˆÐµÐ½Ñ‹
    // Ð¸Ð·
    // Ð¸Ð³Ñ€Ð¾Ð²Ð¾Ð¹
    // Ð°Ñ€ÐµÐ½Ñ‹.
    public static final int THE_PRELIMINARY_MATCH_WILL_BEGIN_IN_S1_SECONDS_PREPARE_YOURSELF = 1881; // ÐžÑ‚Ð±Ð¾Ñ€Ð¾Ñ‡Ð½Ñ‹Ð¹
    // Ñ‚ÑƒÑ€
    // Ð½Ð°Ñ‡Ð½ÐµÑ‚Ñ�Ñ�
    // Ñ‡ÐµÑ€ÐµÐ·
    // $s1
    // Ñ�ÐµÐº.
    // ÐŸÑ€Ð¸Ð³Ð¾Ñ‚Ð¾Ð²ÑŒÑ‚ÐµÑ�ÑŒ.
    public static final int CHARACTERS_CANNOT_BE_CREATED_FROM_THIS_SERVER = 1882; // ÐŸÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð¸
    // Ð½Ðµ
    // Ð¼Ð¾Ð³ÑƒÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ñ�Ð¾Ð·Ð´Ð°Ð½Ñ‹
    // Ñ�
    // Ñ�Ñ‚Ð¾Ð³Ð¾
    // Ñ�ÐµÑ€Ð²ÐµÑ€Ð°.
    public static final int THERE_ARE_NO_OFFERINGS_I_OWN_OR_I_MADE_A_BID_FOR = 1883; // ÐœÐ½Ðµ
    // Ð½ÐµÑ‡ÐµÐ³Ð¾
    // Ð¿Ñ€ÐµÐ´Ð»Ð¾Ð¶Ð¸Ñ‚ÑŒ.
    public static final int ENTER_THE_PC_ROOM_COUPON_SERIAL_NUMBER = 1884; // Ð’Ð²ÐµÐ´Ð¸Ñ‚Ðµ
    // Ñ�ÐµÑ€Ð¸Ð¹Ð½Ñ‹Ð¹
    // Ð½Ð¾Ð¼ÐµÑ€
    // ÐºÑƒÐ¿Ð¾Ð½Ð°:
    public static final int THIS_SERIAL_NUMBER_CANNOT_BE_ENTERED_PLEASE_TRY_AGAIN_IN_S1_MINUTES = 1885; // Ð¡ÐµÑ€Ð¸Ð¹Ð½Ñ‹Ð¹
    // Ð½Ð¾Ð¼ÐµÑ€
    // Ñ�ÐµÐ¹Ñ‡Ð°Ñ�
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð²Ð²ÐµÐ´ÐµÐ½.
    // ÐŸÐ¾Ð¶Ð°Ð»ÑƒÐ¹Ñ�Ñ‚Ð°,
    // Ð¿Ð¾Ð²Ñ‚Ð¾Ñ€Ð¸Ñ‚Ðµ
    // Ð¿Ð¾Ð¿Ñ‹Ñ‚ÐºÑƒ
    // Ñ‡ÐµÑ€ÐµÐ·
    // $s1
    // Ð¼Ð¸Ð½.
    public static final int THIS_SERIAL_NUMBER_HAS_ALREADY_BEEN_USED = 1886; // Ð­Ñ‚Ð¾Ñ‚
    // Ñ�ÐµÑ€Ð¸Ð¹Ð½Ñ‹Ð¹
    // Ð½Ð¾Ð¼ÐµÑ€
    // ÑƒÐ¶Ðµ
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ð½.
    public static final int INVALID_SERIAL_NUMBER_YOUR_ATTEMPT_TO_ENTER_THE_NUMBER_HAS_FAILED_S1_TIMES_YOU_WILL_BE_ALLOWED_TO_MAKE_S2_MORE_ATTEMPTS = 1887; // Ð�ÐµÐ²ÐµÑ€Ð½Ñ‹Ð¹
    // Ñ�ÐµÑ€Ð¸Ð¹Ð½Ñ‹Ð¹
    // Ð½Ð¾Ð¼ÐµÑ€.
    // Ð’Ð°ÑˆÐ°
    // Ð¿Ð¾Ð¿Ñ‹Ñ‚ÐºÐ°
    // Ð²Ð²ÐµÑ�Ñ‚Ð¸
    // Ð½Ð¾Ð¼ÐµÑ€
    // Ð½Ðµ
    // ÑƒÐ´Ð°Ð»Ð°Ñ�ÑŒ
    // $s1
    // Ñ€Ð°Ð·(-Ð°).
    // Ð’Ñ‹
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¿Ð¾Ð¿Ñ‹Ñ‚Ð°Ñ‚ÑŒÑ�Ñ�
    // ÐµÑ‰Ðµ
    // $s2
    // Ñ€Ð°Ð·(-Ð°).
    public static final int INVALID_SERIAL_NUMBER_YOUR_ATTEMPT_TO_ENTER_THE_NUMBER_HAS_FAILED_5_TIMES_PLEASE_TRY_AGAIN_IN_4_HOURS = 1888; // Ð�ÐµÐ²ÐµÑ€Ð½Ñ‹Ð¹
    // Ñ�ÐµÑ€Ð¸Ð¹Ð½Ñ‹Ð¹
    // Ð½Ð¾Ð¼ÐµÑ€.
    // Ð’Ð°ÑˆÐ°
    // Ð¿Ð¾Ð¿Ñ‹Ñ‚ÐºÐ°
    // Ð²Ð²ÐµÑ�Ñ‚Ð¸
    // Ð½Ð¾Ð¼ÐµÑ€
    // Ð½Ðµ
    // ÑƒÐ´Ð°Ð»Ð°Ñ�ÑŒ
    // 5
    // Ñ€Ð°Ð·.
    // ÐŸÐ¾Ð¶Ð°Ð»ÑƒÐ¹Ñ�Ñ‚Ð°,
    // Ð¿Ð¾Ð²Ñ‚Ð¾Ñ€Ð¸Ñ‚Ðµ
    // Ð¿Ð¾Ð¿Ñ‹Ñ‚ÐºÑƒ
    // Ñ‡ÐµÑ€ÐµÐ·
    // 4
    // Ñ‡Ð°Ñ�Ð°.
    public static final int CONGRATULATIONS_YOU_HAVE_RECEIVED_S1 = 1889; // ÐŸÐ¾Ð·Ð´Ñ€Ð°Ð²Ð»Ñ�ÐµÐ¼!
    // Ð’Ñ‹
    // Ð¿Ð¾Ð»ÑƒÑ‡Ð¸Ð»Ð¸:
    // $s1.
    public static final int SINCE_YOU_HAVE_ALREADY_USED_THIS_COUPON_YOU_MAY_NOT_USE_THIS_SERIAL_NUMBER = 1890; // Ð’Ñ‹
    // ÑƒÐ¶Ðµ
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ð»Ð¸
    // Ñ�Ñ‚Ð¾Ñ‚
    // ÐºÑƒÐ¿Ð¾Ð½
    // Ð¸
    // Ð±Ð¾Ð»ÑŒÑˆÐµ
    // Ð½ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾
    // Ð²Ð²Ð¾Ð´Ð¸Ñ‚ÑŒ
    // Ñ�Ñ‚Ð¾Ñ‚
    // Ð½Ð¾Ð¼ÐµÑ€.
    public static final int YOU_MAY_NOT_USE_ITEMS_IN_A_PRIVATE_STORE_OR_PRIVATE_WORK_SHOP = 1891; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒ
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ñ‹
    // Ð²
    // Ð»Ð¸Ñ‡Ð½Ð¾Ð¹
    // Ñ‚Ð¾Ñ€Ð³Ð¾Ð²Ð¾Ð¹
    // Ð»Ð°Ð²ÐºÐµ
    // Ð¸Ð»Ð¸
    // Ð¼Ð°Ñ�Ñ‚ÐµÑ€Ñ�ÐºÐ¾Ð¹.
    public static final int THE_REPLAY_FILE_FOR_THE_PREVIOUS_VERSION_CANNOT_BE_PLAYED = 1892; // Ð¤Ð°Ð¹Ð»
    // Replay
    // Ð¿Ñ€ÐµÐ´Ñ‹Ð´ÑƒÑ‰ÐµÐ¹
    // Ð²ÐµÑ€Ñ�Ð¸Ð¸
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð¿Ñ€Ð¾Ð¸Ð³Ñ€Ð°Ð½.
    public static final int THIS_FILE_CANNOT_BE_REPLAYED = 1893; // Ð­Ñ‚Ð¾ Ñ„Ð°Ð¹Ð» Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚ Ð±Ñ‹Ñ‚ÑŒ
    // Ð²Ð¾Ñ�Ð¿Ñ€Ð¾Ð¸Ð·Ð²ÐµÐ´ÐµÐ½.
    public static final int A_SUB_CLASS_CANNOT_BE_CREATED_OR_CHANGED_WHILE_YOU_ARE_OVER_YOUR_WEIGHT_LIMIT = 1894; // ÐŸÐ¾Ð´ÐºÐ»Ð°Ñ�Ñ�
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ñ�Ð¾Ð·Ð´Ð°Ð½
    // Ð¸Ð»Ð¸
    // Ð¸Ð·Ð¼ÐµÐ½ÐµÐ½,
    // ÐµÑ�Ð»Ð¸
    // Ð²Ñ‹
    // Ð¿Ñ€ÐµÐ²Ñ‹ÑˆÐ°ÐµÑ‚Ðµ
    // Ð»Ð¸Ð¼Ð¸Ñ‚
    // Ð²ÐµÑ�Ð°.
    public static final int S1_IS_IN_AN_AREA_WHICH_BLOCKS_SUMMONING = 1895; // $c1
    // Ð½Ð°Ñ…Ð¾Ð´Ð¸Ñ‚Ñ�Ñ�
    // Ð²
    // Ð·Ð¾Ð½Ðµ
    // Ð±Ð»Ð¾ÐºÐ¸Ñ€Ð¾Ð²ÐºÐ¸
    // Ð¿Ñ€Ð¸Ð·Ñ‹Ð²Ð°.
    public static final int S1_HAS_ALREADY_BEEN_SUMMONED = 1896; // ÐŸÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶ $c1
    // ÑƒÐ¶Ðµ Ð¿Ñ€Ð¸Ð·Ð²Ð°Ð½.
    public static final int S1_IS_REQUIRED_FOR_SUMMONING = 1897; // ÐŸÑ€Ð¸Ð·Ñ‹Ð²
    // Ñ‚Ñ€ÐµÐ±ÑƒÐµÑ‚:
    // $s1.
    public static final int S1_IS_CURRENTLY_TRADING_OR_OPERATING_A_PRIVATE_STORE_AND_CANNOT_BE_SUMMONED = 1898; // ÐŸÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶
    // $c1
    // Ñ�ÐµÐ¹Ñ‡Ð°Ñ�
    // Ð½Ð°Ñ…Ð¾Ð´Ð¸Ñ‚Ñ�Ñ�
    // Ð²
    // Ð»Ð¸Ñ‡Ð½Ð¾Ð¹
    // Ñ‚Ð¾Ñ€Ð³Ð¾Ð²Ð¾Ð¹
    // Ð»Ð°Ð²ÐºÐµ
    // Ð¸
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð¿Ñ€Ð¸Ð·Ð²Ð°Ð½.
    public static final int YOUR_TARGET_IS_IN_AN_AREA_WHICH_BLOCKS_SUMMONING = 1899; // Ð’Ð°ÑˆÐ°
    // Ñ†ÐµÐ»ÑŒ
    // Ð½Ð°Ñ…Ð¾Ð´Ð¸Ñ‚Ñ�Ñ�
    // Ð²
    // Ð·Ð¾Ð½Ðµ,
    // Ð³Ð´Ðµ
    // Ð¿Ñ€Ð¸Ð·Ñ‹Ð²
    // Ð±Ð»Ð¾ÐºÐ¸Ñ€Ð¾Ð²Ð°Ð½.
    public static final int S1_HAS_ENTERED_THE_PARTY_ROOM = 1900; // $c1 Ð²Ñ…Ð¾Ð´Ð¸Ñ‚
    // Ð² ÐºÐ¾Ð¼Ð½Ð°Ñ‚Ñƒ
    // Ð³Ñ€ÑƒÐ¿Ð¿Ñ‹.
    public static final int S1_HAS_INVITED_YOU_TO_ENTER_THE_PARTY_ROOM = 1901; // $c1
    // Ð¿Ñ€Ð¸Ð³Ð»Ð°ÑˆÐ°ÐµÑ‚
    // Ð’Ð°Ñ�
    // Ð²
    // ÐºÐ¾Ð¼Ð½Ð°Ñ‚Ñƒ
    // Ð³Ñ€ÑƒÐ¿Ð¿Ñ‹.
    public static final int INCOMPATIBLE_ITEM_GRADE_THIS_ITEM_CANNOT_BE_USED = 1902; // Ð�ÐµÑ�Ð¾Ð²Ð¼ÐµÑ�Ñ‚Ð¸Ð¼Ñ‹Ð¹
    // ÐºÐ»Ð°Ñ�Ñ�
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ð°.
    // ÐŸÑ€ÐµÐ´Ð¼ÐµÑ‚
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ð½.
    public static final int REQUESTED_NCOTP = 1903; // Ð¢Ðµ Ð¸Ð· Ð’Ð°Ñ�, ÐºÐ¾Ð¼Ñƒ Ñ‚Ñ€ÐµÐ±ÑƒÐµÑ‚Ñ�Ñ�
    // NCOTP, Ð¼Ð¾Ð³ÑƒÑ‚ Ð·Ð°Ð¿ÑƒÑ�Ñ‚Ð¸Ñ‚ÑŒ
    // ÐµÐ³Ð¾,\\n Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·ÑƒÑ� Ñ�Ð²Ð¾Ð¹
    // Ñ‚ÐµÐ»ÐµÑ„Ð¾Ð½, Ñ‡Ñ‚Ð¾Ð±Ñ‹ Ð¿Ð¾Ð»ÑƒÑ‡Ð¸Ñ‚ÑŒ
    // NCOTP
    public static final int A_SUB_CLASS_MAY_NOT_BE_CREATED_OR_CHANGED_WHILE_A_SERVITOR_OR_PET_IS_SUMMONED = 1904; // ÐŸÐ¾Ð´ÐºÐ»Ð°Ñ�Ñ�
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ñ�Ð¾Ð·Ð´Ð°Ð½
    // Ð¸Ð»Ð¸
    // Ð¸Ð·Ð¼ÐµÐ½ÐµÐ½,
    // ÐµÑ�Ð»Ð¸
    // Ð²Ñ‹Ð·Ð²Ð°Ð½
    // Ñ�Ð»ÑƒÐ³Ð°
    // Ð¸Ð»Ð¸
    // Ð¿Ð¸Ñ‚Ð¾Ð¼ÐµÑ†.
    public static final int S2_OF_S1_WILL_BE_REPLACED_WITH_S4_OF_S3 = 1905; // $s2
    // $s1
    // Ð·Ð°Ð¼ÐµÐ½ÐµÐ½
    // Ð½Ð°
    // $c4
    // $s3.
    public static final int SELECT_THE_COMBAT_UNIT_YOU_WISH_TO_TRANSFER_TO = 1906; // Ð’Ñ‹Ð±ÐµÑ€Ð¸Ñ‚Ðµ
    // Ð±Ð¾ÐµÐ²Ð¾Ð¹
    // Ð¾Ñ‚Ñ€Ñ�Ð´\\n
    // Ð´Ð»Ñ�
    // Ñ‚Ñ€Ð°Ð½Ñ�Ñ„ÐµÑ€Ð°.
    public static final int SELECT_THE_THE_CHARACTER_WHO_WILL_REPLACE_THE_CURRENT_CHARACTER = 1907; // Ð’Ñ‹Ð±ÐµÑ€Ð¸Ñ‚Ðµ
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°,
    // ÐºÐ¾Ñ‚Ð¾Ñ€Ñ‹Ð¹
    // \\n
    // Ð·Ð°Ð¼ÐµÐ½Ð¸Ñ‚
    // Ñ‚ÐµÐºÑƒÑ‰ÐµÐ³Ð¾
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°.
    public static final int S1_IS_IN_A_STATE_WHICH_PREVENTS_SUMMONING = 1908; // $c1
    // Ð¸Ð¼ÐµÐµÑ‚
    // Ñ�Ñ‚Ð°Ñ‚ÑƒÑ�,
    // Ð½Ðµ
    // Ð´Ð¾Ð¿ÑƒÑ�ÐºÐ°ÑŽÑ‰Ð¸Ð¹
    // ÐµÐ³Ð¾
    // Ð¿Ñ€Ð¸Ð·Ñ‹Ð².
    public static final int LIST_OF_CLAN_ACADEMY_GRADUATES_DURING_THE_PAST_WEEK = 1909; // ==
    // <Ð¡Ð¿Ð¸Ñ�Ð¾Ðº
    // Ð²Ñ‹Ð¿ÑƒÑ�ÐºÐ½Ð¸ÐºÐ¾Ð²
    // ÐºÐ»Ð°Ð½Ð°
    // Ð·Ð°
    // Ð¿Ð¾Ñ�Ð»ÐµÐ´Ð½ÑŽÑŽ
    // Ð½ÐµÐ´ÐµÐ»ÑŽ>
    // ==
    public static final int GRADUATES = 1910; // Ð’Ñ‹Ð¿ÑƒÑ�ÐºÐ½Ð¸ÐºÐ¸: $c1.
    public static final int YOU_CANNOT_SUMMON_PLAYERS_WHO_ARE_CURRENTLY_PARTICIPATING_IN_THE_GRAND_OLYMPIAD = 1911; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¿Ñ€Ð¸Ð·Ñ‹Ð²Ð°Ñ‚ÑŒ
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶ÐµÐ¹,
    // Ð²
    // Ð´Ð°Ð½Ð½Ð¾Ðµ
    // Ð²Ñ€ÐµÐ¼Ñ�
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð²ÑƒÑŽÑ‰Ð¸Ñ…
    // Ð²
    // Ð’ÐµÐ»Ð¸ÐºÐ¾Ð¹
    // ÐžÐ»Ð¸Ð¼Ð¿Ð¸Ð°Ð´Ðµ.
    public static final int ONLY_THOSE_REQUESTING_NCOTP_SHOULD_MAKE_AN_ENTRY_INTO_THIS_FIELD = 1912; // Ð¢Ð¾Ð»ÑŒÐºÐ¾
    // Ñ‚Ðµ,
    // ÐºÑ‚Ð¾
    // Ð·Ð°Ð¿Ñ€Ð°ÑˆÐ¸Ð²Ð°Ð»
    // NCOTP,
    // Ð¼Ð¾Ð³ÑƒÑ‚
    // Ð²Ð¾Ð¹Ñ‚Ð¸
    // Ñ�ÑŽÐ´Ð°.
    public static final int THE_REMAINING_RECYCLE_TIME_FOR_S1_IS_S2_MINUTES = 1913; // Ð’Ñ€ÐµÐ¼Ñ�
    // Ð¿Ð¾Ð²Ñ‚Ð¾Ñ€Ð°
    // Ð´Ð»Ñ�
    // $s1:
    // $s2
    // Ð¼Ð¸Ð½.
    public static final int THE_REMAINING_RECYCLE_TIME_FOR_S1_IS_S2_SECONDS = 1914; // Ð’Ñ€ÐµÐ¼Ñ�
    // Ð¿Ð¾Ð²Ñ‚Ð¾Ñ€Ð°
    // Ð´Ð»Ñ�
    // $s1:
    // $s2
    // Ñ�ÐµÐº.
    public static final int THE_GAME_WILL_END_IN_S1_SECONDS_2 = 1915; // Ð˜Ð³Ñ€Ð°
    // Ð·Ð°ÐºÐ¾Ð½Ñ‡Ð¸Ñ‚Ñ�Ñ�
    // Ñ‡ÐµÑ€ÐµÐ·
    // $s1
    // Ñ�ÐµÐº.
    public static final int THE_LEVEL_S1_SHILENS_BREATH_WILL_BE_ASSESSED = 1916; // Ð£Ñ€Ð¾Ð²ÐµÐ½ÑŒ
    // ÑˆÑ‚Ñ€Ð°Ñ„Ð°
    // Ð·Ð°
    // Ñ�Ð¼ÐµÑ€Ñ‚ÑŒ:
    // $s1.
    public static final int THE_SHILENS_BREATH_HAS_BEEN_LIFTED = 1917; // Ð¨Ñ‚Ñ€Ð°Ñ„
    // Ð·Ð°
    // Ñ�Ð¼ÐµÑ€Ñ‚ÑŒ
    // Ð¾Ñ‚Ð¼ÐµÐ½ÐµÐ½.
    public static final int THE_PET_IS_TOO_HIGH_LEVEL_TO_CONTROL = 1918; // Ð’Ð°Ñˆ
    // Ð¿Ð¸Ñ‚Ð¾Ð¼ÐµÑ†
    // Ñ�Ð»Ð¸ÑˆÐºÐ¾Ð¼
    // Ð²Ñ‹Ñ�Ð¾ÐºÐ¾Ð³Ð¾
    // ÑƒÑ€Ð¾Ð²Ð½Ñ�,
    // Ð²Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // ÐµÐ³Ð¾
    // ÐºÐ¾Ð½Ñ‚Ñ€Ð¾Ð»Ð¸Ñ€Ð¾Ð²Ð°Ñ‚ÑŒ
    public static final int THE_GRAND_OLYMPIAD_REGISTRATION_PERIOD_HAS_ENDED = 1919; // Ð ÐµÐ³Ð¸Ñ�Ñ‚Ñ€Ð°Ñ†Ð¸Ñ�
    // Ð½Ð°
    // Ð’ÐµÐ»Ð¸ÐºÑƒÑŽ
    // ÐžÐ»Ð¸Ð¼Ð¿Ð¸Ð°Ð´Ñƒ
    // Ð¾ÐºÐ¾Ð½Ñ‡ÐµÐ½Ð°.
    public static final int YOUR_ACCOUNT_IS_CURRENTLY_INACTIVE_BECAUSE_YOU_HAVE_NOT_LOGGED_INTO_THE_GAME_FOR_SOME_TIME_YOU = 1920; // Ð’Ð°Ñˆ
    // Ð°ÐºÐºÐ°ÑƒÐ½Ñ‚
    // Ñ�ÐµÐ¹Ñ‡Ð°Ñ�
    // Ð½ÐµÐ°ÐºÑ‚Ð¸Ð²ÐµÐ½,
    // Ð¿Ð¾Ñ�ÐºÐ¾Ð»ÑŒÐºÑƒ
    // Ð²Ñ‹
    // Ð´Ð¾Ð»Ð³Ð¾Ðµ
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ð½Ðµ
    // Ð¿Ð¾Ñ�ÐµÑ‰Ð°Ð»Ð¸
    // Ð¸Ð³Ñ€Ñƒ.
    // ÐœÐ¾Ð¶Ð½Ð¾
    // Ð°ÐºÑ‚Ð¸Ð²Ð¸Ñ€Ð¾Ð²Ð°Ñ‚ÑŒ
    // Ð½Ð°
    // Ð½Ð°ÑˆÐµÐ¼
    // Ñ�Ð°Ð¹Ñ‚Ðµ.
    public static final int S2_HOURS_AND_S3_MINUTES_HAVE_PASSED_SINCE_S1_HAS_KILLED = 1921; // Ð¡
    // Ð¼Ð¾Ð¼ÐµÐ½Ñ‚Ð°
    // ÑƒÐ±Ð¸Ð¹Ñ�Ñ‚Ð²Ð°
    // $s1
    // Ð¿Ñ€Ð¾ÑˆÐ»Ð¾
    // $s2
    // Ñ‡
    // $s3
    // Ð¼Ð¸Ð½.
    public static final int BECAUSE_S1_FAILED_TO_KILL_FOR_ONE_FULL_DAY_IT_HAS_EXPIRED = 1922; // Ð˜Ð·-Ð·Ð°
    // Ñ‚Ð¾Ð³Ð¾,
    // Ñ‡Ñ‚Ð¾
    // $s1
    // Ð±Ñ‹Ð»
    public static final int COURT_MAGICIAN__THE_PORTAL_HAS_BEEN_CREATED = 1923; // ÐŸÑ€Ð¸Ð´Ð²Ð¾Ñ€Ð½Ñ‹Ð¹
    // ÐœÐ°Ð³:
    // ÐŸÐ¾Ñ€Ñ‚Ð°Ð»
    // Ñ�Ð¾Ð·Ð´Ð°Ð½!
    public static final int DUE_TO_THE_AFFECTS_OF_THE_SEAL_OF_STRIFE_IT_IS_NOT_POSSIBLE_TO_SUMMON_AT_THIS_TIME = 1925; // Ð˜Ð·-Ð·Ð°
    // Ñ�Ñ„Ñ„ÐµÐºÑ‚Ð°
    // ÐŸÐµÑ‡Ð°Ñ‚Ð¸
    // Ð Ð°Ð·Ð´Ð¾Ñ€Ð°
    // Ð¿Ñ€Ð¸Ð·Ñ‹Ð²
    // Ñ�ÐµÐ¹Ñ‡Ð°Ñ�
    // Ð½ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶ÐµÐ½.
    public static final int C1_HAS_DECLINED_YOUR_CHALLENGE_TO_A_DUEL = 1932; // $c1
    // Ð¾Ñ‚ÐºÐ°Ð·Ñ‹Ð²Ð°ÐµÑ‚Ñ�Ñ�
    // Ð¾Ñ‚
    // Ð´ÑƒÑ�Ð»Ð¸.
    public static final int C1_HAS_DECLINED_YOUR_CHALLENGE_TO_A_PARTY_DUEL = 1935; // $c1
    // Ð¾Ñ‚ÐºÐ°Ð·Ñ‹Ð²Ð°ÐµÑ‚Ñ�Ñ�
    // Ð¾Ñ‚
    // Ð³Ñ€ÑƒÐ¿Ð¿Ð¾Ð²Ð¾Ð¹
    // Ð´ÑƒÑ�Ð»Ð¸
    // Ñ�
    // Ð’Ð°Ð¼Ð¸.
    public static final int THIS_IS_NOT_A_SUITABLE_PLACE_TO_CHALLENGE_ANYONE_OR_PARTY_TO_A_DUEL = 1941; // Ð­Ñ‚Ð¾
    // Ð½Ðµ
    // Ð¿Ð¾Ð´Ñ…Ð¾Ð´Ñ�Ñ‰ÐµÐµ
    // Ð¼ÐµÑ�Ñ‚Ð¾
    // Ð´Ð»Ñ�
    // Ð¿Ñ€Ð¸Ð³Ð»Ð°ÑˆÐµÐ½Ð¸Ñ�
    // Ð²
    // Ð³Ñ€ÑƒÐ¿Ð¿Ñƒ
    // Ð¸Ð»Ð¸
    // Ð²Ñ‹Ð·Ð¾Ð²Ð°
    // Ð½Ð°
    // Ð´ÑƒÑ�Ð»ÑŒ.
    public static final int THE_OPPOSING_PARTY_IS_CURRENTLY_NOT_IN_A_SUITABLE_LOCATION_FOR_A_DUEL = 1943; // Ð“Ñ€ÑƒÐ¿Ð¿Ð°
    // Ð¿Ñ€Ð¾Ñ‚Ð¸Ð²Ð½Ð¸ÐºÐ°
    // Ð½Ð°Ñ…Ð¾Ð´Ð¸Ñ‚Ñ�Ñ�
    // Ð²
    // Ð½Ðµ
    // Ð¿Ð¾Ð´Ñ…Ð¾Ð´Ñ�Ñ‰ÐµÐ¹
    // Ð´Ð»Ñ�
    // Ð´ÑƒÑ�Ð»Ð¸
    // Ð»Ð¾ÐºÐ°Ñ†Ð¸Ð¸
    // .
    public static final int C1_HAS_CHALLENGED_YOU_TO_A_DUEL_WILL_YOU_ACCEPT = 1946; // $c1
    // Ð²Ñ‹Ð·Ñ‹Ð²Ð°ÐµÑ‚
    // Ð’Ð°Ñ�
    // Ð½Ð°
    // Ð´ÑƒÑ�Ð»ÑŒ.
    // ÐŸÑ€Ð¸Ð½Ñ�Ñ‚ÑŒ?
    public static final int C1_S_PARTY_HAS_CHALLENGED_YOUR_PARTY_TO_A_DUEL_WILL_YOU_ACCEPT = 1947; // Ð“Ñ€ÑƒÐ¿Ð¿Ð°
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°
    // $c1
    // Ð²Ñ‹Ð·Ñ‹Ð²Ð°ÐµÑ‚
    // Ð’Ð°ÑˆÑƒ
    // Ð³Ñ€ÑƒÐ¿Ð¿Ñƒ
    // Ð½Ð°
    // Ð´ÑƒÑ�Ð»ÑŒ.
    // ÐŸÑ€Ð¸Ð½Ñ�Ñ‚ÑŒ?
    public static final int THE_DUEL_WILL_BEGIN_IN_S1_SECONDS_1 = 1948; // Ð”ÑƒÑ�Ð»ÑŒ
    // Ð½Ð°Ñ‡Ð½ÐµÑ‚Ñ�Ñ�
    // Ñ‡ÐµÑ€ÐµÐ·
    // $s1
    // Ñ�ÐµÐº.
    public static final int SINCE_C1_WAS_DISQUALIFIED_S2_HAS_WON = 1953; // Ð˜Ð·-Ð·Ð°
    // Ð´Ð¸Ñ�ÐºÐ²Ð°Ð»Ð¸Ñ„Ð¸ÐºÐ°Ñ†Ð¸Ð¸
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°
    // $c1
    // Ð´ÑƒÑ�Ð»ÑŒ
    // Ð²Ñ‹Ð¸Ð³Ñ€Ñ‹Ð²Ð°ÐµÑ‚
    // $s2.
    public static final int SINCE_C1_S_PARTY_WAS_DISQUALIFIED_S2_S_PARTY_HAS_WON = 1954; // Ð˜Ð·-Ð·Ð°
    // Ð´Ð¸Ñ�ÐºÐ²Ð°Ð»Ð¸Ñ„Ð¸ÐºÐ°Ñ†Ð¸Ð¸
    // Ð³Ñ€ÑƒÐ¿Ð¿Ñ‹
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°
    // $c1
    // Ð´ÑƒÑ�Ð»ÑŒ
    // Ð²Ñ‹Ð¸Ð³Ñ€Ñ‹Ð²Ð°ÐµÑ‚
    // Ð³Ñ€ÑƒÐ¿Ð¿Ð°
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°
    // $s2.
    public static final int ONLY_THE_CLAN_LEADER_MAY_ISSUE_COMMANDS = 1966; // Ð¢Ð¾Ð»ÑŒÐºÐ¾
    // Ð»Ð¸Ð´ÐµÑ€
    // ÐºÐ»Ð°Ð½Ð°
    // Ð¸Ð¼ÐµÐµÑ‚
    // Ð¿Ñ€Ð°Ð²Ð¾
    // ÐºÐ¾Ð¼Ð°Ð½Ð´Ð¾Ð²Ð°Ñ‚ÑŒ.
    public static final int THE_GATE_IS_FIRMLY_LOCKED_PLEASE_TRY_AGAIN_LATER = 1967; // Ð’Ñ€Ð°Ñ‚Ð°
    // Ð·Ð°Ð¿ÐµÑ€Ñ‚Ñ‹.
    // ÐŸÐ¾Ð¶Ð°Ð»ÑƒÐ¹Ñ�Ñ‚Ð°,
    // Ð¿Ð¾Ð²Ñ‚Ð¾Ñ€Ð¸Ñ‚Ðµ
    // Ð¿Ð¾Ð¿Ñ‹Ñ‚ÐºÑƒ
    // Ð¿Ð¾Ð·Ð¶Ðµ.
    public static final int S1_S_OWNER = 1968; // Ð’Ð»Ð°Ð´ÐµÐ»ÐµÑ† $s1.
    public static final int AREA_WHERE_S1_APPEARS = 1969; // ÐœÐµÑ�Ñ‚Ð¾ Ð¿Ð¾Ñ�Ð²Ð»ÐµÐ½Ð¸Ñ�
    // $s1.
    public static final int THE_LEVEL_OF_THE_HARDENER_IS_TOO_HIGH_TO_BE_USED = 1971; // Ð£Ñ€Ð¾Ð²ÐµÐ½ÑŒ
    // Ð·Ð°ÐºÑ€ÐµÐ¿Ð¸Ñ‚ÐµÐ»Ñ�
    // Ñ�Ð»Ð¸ÑˆÐºÐ¾Ð¼
    // Ð²Ñ‹Ñ�Ð¾Ðº.
    public static final int YOU_CANNOT_AUGMENT_ITEMS_WHILE_FROZEN = 1973; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð·Ð°Ñ‡Ð°Ñ€Ð¾Ð²Ð°Ñ‚ÑŒ
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ñ‹,
    // Ð±ÑƒÐ´ÑƒÑ‡Ð¸
    // Ð·Ð°Ð¼Ð¾Ñ€Ð¾Ð¶ÐµÐ½Ð½Ñ‹Ð¼.
    public static final int YOU_CANNOT_AUGMENT_ITEMS_WHILE_ENGAGED_IN_TRADE_ACTIVITIES = 1975; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð·Ð°Ñ‡Ð°Ñ€Ð¾Ð²Ð°Ñ‚ÑŒ
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ñ‹
    // Ð²Ð¾
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ñ‚Ð¾Ñ€Ð³Ð¾Ð²Ð»Ð¸.
    public static final int S1_S_DROP_AREA_S2 = 1985; // Ð—Ð¾Ð½Ð° Ð²Ñ‹Ð¿Ð°Ð´ÐµÐ½Ð¸Ñ� Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ð°
    // $s1
    public static final int S1_S_OWNER_S2 = 1986; // Ð’Ð»Ð°Ð´ÐµÐ»ÐµÑ† Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ð° $s1 ($s2)
    public static final int S1_1 = 1987; // $s1
    public static final int THE_FERRY_HAS_ARRIVED_AT_PRIMEVAL_ISLE = 1988; // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ
    // Ð¿Ñ€Ð¸Ð±Ñ‹Ð»
    // Ð½Ð°
    // ÐŸÐµÑ€Ð²Ð¾Ð±Ñ‹Ñ‚Ð½Ñ‹Ð¹
    // ÐžÑ�Ñ‚Ñ€Ð¾Ð².
    public static final int THE_FERRY_WILL_LEAVE_FOR_RUNE_HARBOR_AFTER_ANCHORING_FOR_THREE_MINUTES = 1989; // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ
    // Ð¿Ð¾ÐºÐ¸Ð½ÐµÑ‚
    // Ð³Ð°Ð²Ð°Ð½ÑŒ
    // Ð ÑƒÐ½Ñ‹
    // Ñ‡ÐµÑ€ÐµÐ·
    // 3
    // Ð¼Ð¸Ð½ÑƒÑ‚Ñ‹
    // Ð¿Ð¾Ñ�Ð»Ðµ
    // Ð¿Ñ€Ð¸Ð±Ñ‹Ñ‚Ð¸Ñ�.
    public static final int THE_FERRY_IS_NOW_DEPARTING_PRIMEVAL_ISLE_FOR_RUNE_HARBOR = 1990; // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ
    // Ð¾Ñ‚
    // Ð³Ð°Ð²Ð°Ð½Ð¸
    // Ð ÑƒÐ½Ñ‹
    // Ðº
    // ÐŸÐµÑ€Ð²Ð¾Ð±Ñ‹Ñ‚Ð½Ð¾Ð¼Ñƒ
    // ÐžÑ�Ñ‚Ñ€Ð¾Ð²Ñƒ
    // Ð¾Ñ‚Ñ‡Ð°Ð»Ð¸Ð²Ð°ÐµÑ‚.
    public static final int THE_FERRY_WILL_LEAVE_FOR_PRIMEVAL_ISLE_AFTER_ANCHORING_FOR_THREE_MINUTES = 1991; // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ
    // Ð¿Ð¾ÐºÐ¸Ð½ÐµÑ‚
    // Ð³Ð°Ð²Ð°Ð½ÑŒ
    // Ð ÑƒÐ½Ñ‹
    // Ñ‡ÐµÑ€ÐµÐ·
    // 3
    // Ð¼Ð¸Ð½ÑƒÑ‚Ñ‹
    // Ð¿Ð¾Ñ�Ð»Ðµ
    // Ð¿Ñ€Ð¸Ð±Ñ‹Ñ‚Ð¸Ñ�.
    public static final int THE_FERRY_IS_NOW_DEPARTING_RUNE_HARBOR_FOR_PRIMEVAL_ISLE = 1992; // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ
    // Ð¾Ñ‚
    // ÐŸÐµÑ€Ð²Ð¾Ð±Ñ‹Ñ‚Ð½Ð¾Ð³Ð¾
    // ÐžÑ�Ñ‚Ñ€Ð¾Ð²Ð°
    // Ðº
    // Ð³Ð°Ð²Ð°Ð½Ð¸
    // Ð ÑƒÐ½Ñ‹
    // Ð¾Ñ‚Ñ‡Ð°Ð»Ð¸Ð²Ð°ÐµÑ‚.
    public static final int THE_FERRY_FROM_PRIMEVAL_ISLE_TO_RUNE_HARBOR_HAS_BEEN_DELAYED = 1993; // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ
    // Ñ�
    // ÐŸÐµÑ€Ð²Ð¾Ð±Ñ‹Ñ‚Ð½Ð¾Ð³Ð¾
    // ÐžÑ�Ñ‚Ñ€Ð¾Ð²Ð°
    // Ðº
    // Ð³Ð°Ð²Ð°Ð½Ð¸
    // Ð ÑƒÐ½Ñ‹
    // Ð·Ð°Ð´ÐµÑ€Ð¶Ð¸Ð²Ð°ÐµÑ‚Ñ�Ñ�.
    public static final int THE_FERRY_FROM_RUNE_HARBOR_TO_PRIMEVAL_ISLE_HAS_BEEN_DELAYED = 1994; // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ
    // Ð¸Ð·
    // Ð³Ð°Ð²Ð°Ð½Ð¸
    // Ð ÑƒÐ½Ñ‹
    // Ðº
    // ÐŸÐµÑ€Ð²Ð¾Ð±Ñ‹Ñ‚Ð½Ð¾Ð¼Ñƒ
    // ÐžÑ�Ñ‚Ñ€Ð¾Ð²Ñƒ
    // Ð·Ð°Ð´ÐµÑ€Ð¶Ð¸Ð²Ð°ÐµÑ‚Ñ�Ñ�.
    public static final int S1_CHANNEL_FILTERING_OPTION = 1995; // Ð¤Ð¸Ð»ÑŒÑ‚Ñ€
    // ÐºÐ°Ð½Ð°Ð»Ð°: $s1.
    public static final int THE_ATTACK_HAS_BEEN_BLOCKED = 1996; // Ð�Ñ‚Ð°ÐºÐ° Ð±Ñ‹Ð»Ð°
    // Ð·Ð°Ð±Ð»Ð¾ÐºÐ¸Ñ€Ð¾Ð²Ð°Ð½Ð°.
    public static final int YOU_HAVE_AVOIDED_C1_S_ATTACK = 2000; // Ð’Ñ‹
    // ÑƒÐ²ÐµÑ€Ð½ÑƒÐ»Ð¸Ñ�ÑŒ
    // Ð¾Ñ‚ Ð°Ñ‚Ð°ÐºÐ¸
    // Ñ†ÐµÐ»Ð¸ $c1.
    public static final int TRAP_FAILED = 2002; // Ð—Ð°Ñ…Ð²Ð°Ñ‚ Ð½Ðµ ÑƒÐ´Ð°Ð»Ñ�Ñ�.
    public static final int YOU_OBTAINED_AN_ORDINARY_MATERIAL = 2003; // Ð’Ñ‹
    // Ð¿Ð¾Ð»ÑƒÑ‡Ð¸Ð»Ð¸
    // Ð¾Ð±Ñ‹Ñ‡Ð½Ñ‹Ð¹
    // Ð¼Ð°Ñ‚ÐµÑ€Ð¸Ð°Ð».
    public static final int YOU_OBTAINED_A_RARE_MATERIAL = 2004; // Ð’Ñ‹ Ð¿Ð¾Ð»ÑƒÑ‡Ð¸Ð»Ð¸
    // Ñ€ÐµÐ´ÐºÐ¸Ð¹
    // Ð¼Ð°Ñ‚ÐµÑ€Ð¸Ð°Ð».
    public static final int YOU_OBTAINED_A_UNIQUE_MATERIAL = 2005; // Ð’Ñ‹
    // Ð¿Ð¾Ð»ÑƒÑ‡Ð¸Ð»Ð¸
    // ÑƒÐ½Ð¸ÐºÐ°Ð»ÑŒÐ½Ñ‹Ð¹
    // Ð¼Ð°Ñ‚ÐµÑ€Ð¸Ð°Ð».
    public static final int YOU_OBTAINED_THE_ONLY_MATERIAL_OF_THIS_KIND = 2006; // Ð’Ñ‹
    // Ð¿Ð¾Ð»ÑƒÑ‡Ð¸Ð»Ð¸
    // ÐµÐ´Ð¸Ð½Ñ�Ñ‚Ð²ÐµÐ½Ð½Ñ‹Ð¹
    // Ð²
    // Ñ�Ð²Ð¾ÐµÐ¼
    // Ñ€Ð¾Ð´Ðµ
    // Ð¼Ð°Ñ‚ÐµÑ€Ð¸Ð°Ð».
    public static final int PLEASE_ENTER_THE_RECIPIENT_S_NAME = 2007; // ÐŸÐ¾Ð¶Ð°Ð»ÑƒÐ¹Ñ�Ñ‚Ð°,
    // Ð²Ð²ÐµÐ´Ð¸Ñ‚Ðµ
    // Ð¸Ð¼Ñ�
    // Ð°Ð´Ñ€ÐµÑ�Ð°Ñ‚Ð°.
    public static final int PLEASE_ENTER_THE_TEXT = 2008; // ÐŸÐ¾Ð¶Ð°Ð»ÑƒÐ¹Ñ�Ñ‚Ð°, Ð²Ð²ÐµÐ´Ð¸Ñ‚Ðµ
    // Ñ‚ÐµÐºÑ�Ñ‚.
    public static final int YOU_CANNOT_EXCEED_1500_CHARACTERS = 2009; // Ð�Ðµ
    // Ð±Ð¾Ð»ÑŒÑˆÐµ
    // 1500
    // Ñ�Ð¸Ð¼Ð²Ð¾Ð»Ð¾Ð².
    public static final int S2_S1 = 2010; // $s2 $s1
    public static final int THE_AUGMENTED_ITEM_CANNOT_BE_DISCARDED = 2011; // Ð—Ð°Ñ‡Ð°Ñ€Ð¾Ð²Ð°Ð½Ð¸Ðµ
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð¾Ñ‚Ð¼ÐµÐ½ÐµÐ½Ð¾.
    public static final int S1_HAS_BEEN_ACTIVATED = 2012; // $s1 Ð°ÐºÑ‚Ð¸Ð²Ð¸Ñ€Ð¾Ð²Ð°Ð½.
    public static final int YOUR_SEED_OR_REMAINING_PURCHASE_AMOUNT_IS_INADEQUATE = 2013; // Ð¢Ð¸Ð¿
    // Ñ�ÐµÐ¼Ñ�Ð½
    // Ð¸Ð»Ð¸
    // Ð¾Ñ�Ñ‚Ð°Ñ‚Ð¾Ðº
    // Ñ�ÑƒÐ¼Ð¼Ñ‹
    // Ð·Ð°ÐºÑƒÐ¿ÐºÐ¸
    // Ð½Ðµ
    // Ñ�Ð¾Ð¾Ñ‚Ð²ÐµÑ‚Ñ�Ñ‚Ð²ÑƒÐµÑ‚
    // Ð²Ð°ÑˆÐµÐ¼Ñƒ
    // Ð·Ð°Ð¿Ñ€Ð¾Ñ�Ñƒ.
    public static final int YOU_CANNOT_PROCEED_BECAUSE_THE_MANOR_CANNOT_ACCEPT_ANY_MORE_CROPS__ALL_CROPS_HAVE_BEEN_RETURNED = 2014; // Ð�ÐµÐ¾Ñ�ÑƒÑ‰ÐµÑ�Ñ‚Ð²Ð¸Ð¼Ð¾.
    // Ð’Ð»Ð°Ð´ÐµÐ½Ð¸Ðµ
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð¿Ñ€Ð¸Ð½Ñ�Ñ‚ÑŒ
    // Ð±Ð¾Ð»ÑŒÑˆÐµ
    // Ñ€Ð¾Ñ�Ñ‚ÐºÐ¾Ð².
    // Ð’Ñ�Ðµ
    // Ñ€Ð¾Ñ�Ñ‚ÐºÐ¸
    // Ð±Ñ‹Ð»Ð¸
    // Ð²Ð¾Ð·Ð²Ñ€Ð°Ñ‰ÐµÐ½Ñ‹
    // Ð¸
    // Ð²
    // Ñ€ÐµÐ·ÐµÑ€Ð²Ðµ
    // Ð½Ðµ
    // Ð¾Ñ�Ñ‚Ð°Ð»Ð¾Ñ�ÑŒ
    // Ð°Ð´ÐµÐ½.
    public static final int A_SKILL_IS_READY_TO_BE_USED_AGAIN = 2015; // Ð£Ð¼ÐµÐ½Ð¸Ðµ
    // Ð³Ð¾Ñ‚Ð¾Ð²Ð¾
    // Ðº
    // Ð¿Ð¾Ð²Ñ‚Ð¾Ñ€Ð½Ð¾Ð¼Ñƒ
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ð½Ð¸ÑŽ.
    public static final int A_SKILL_IS_READY_TO_BE_USED_AGAIN_BUT_ITS_RE_USE_COUNTER_TIME_HAS_INCREASED = 2016; // Ð£Ð¼ÐµÐ½Ð¸Ðµ
    // Ð³Ð¾Ñ‚Ð¾Ð²Ð¾
    // Ðº
    // Ð¿Ð¾Ð²Ñ‚Ð¾Ñ€Ð½Ð¾Ð¼Ñƒ
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ð½Ð¸ÑŽ,
    // Ð½Ð¾
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ð¾Ñ‚ÐºÐ°Ñ‚Ð°
    // Ð²Ð¾Ð·Ñ€Ð¾Ñ�Ð»Ð¾.
    public static final int C1_IS_CURRENTLY_TELEPORTING_AND_CANNOT_PARTICIPATE_IN_THE_OLYMPIAD = 2029; // $c1
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð²Ð¾Ð²Ð°Ñ‚ÑŒ
    // Ð²
    // ÐžÐ»Ð¸Ð¼Ð¿Ð¸Ð°Ð´Ðµ
    // Ð²Ð¾
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ñ‚ÐµÐ»ÐµÐ¿Ð¾Ñ€Ñ‚Ð°Ñ†Ð¸Ð¸.
    public static final int YOU_ARE_CURRENTLY_LOGGING_IN = 2030; // Ð’Ñ‹ Ð²Ñ…Ð¾Ð´Ð¸Ñ‚Ðµ Ð²
    // Ð¸Ð³Ñ€Ñƒ.
    public static final int PLEASE_WAIT_A_MOMENT = 2031; // ÐŸÐ¾Ð¶Ð°Ð»ÑƒÐ¹Ñ�Ñ‚Ð°,
    // Ð¿Ð¾Ð´Ð¾Ð¶Ð´Ð¸Ñ‚Ðµ.
    public static final int IT_IS_NOT_THE_RIGHT_TIME_FOR_PURCHASING_THE_ITEM = 2032; // Ð�ÐµÐ¿Ð¾Ð´Ñ…Ð¾Ð´Ñ�Ñ‰ÐµÐµ
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ð´Ð»Ñ�
    // Ð¿Ð¾ÐºÑƒÐ¿Ð¾Ðº.
    public static final int A_SUB_CLASS_CANNOT_BE_CREATED_OR_CHANGED_BECAUSE_YOU_HAVE_EXCEEDED_YOUR_INVENTORY_LIMIT = 2033; // ÐŸÐ¾Ð´ÐºÐ»Ð°Ñ�Ñ�
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ñ�Ð¾Ð·Ð´Ð°Ð½
    // Ð¸Ð»Ð¸
    // Ð¸Ð·Ð¼ÐµÐ½ÐµÐ½,
    // Ð¿Ð¾Ñ�ÐºÐ¾Ð»ÑŒÐºÑƒ
    // Ð’Ñ‹
    // Ð¿Ñ€ÐµÐ²Ñ‹Ñ�Ð¸Ð»Ð¸
    // Ð²Ð°Ñˆ
    // Ð»Ð¸Ð¼Ð¸Ñ‚
    // Ð¸Ð½Ð²ÐµÐ½Ñ‚Ð°Ñ€Ñ�.
    public static final int THERE_ARE_S1_HOURSS_AND_S2_MINUTES_REMAINING_UNTIL_THE_TIME_WHEN_THE_ITEM_CAN_BE_PURCHASED = 2034; // ÐŸÑ€ÐµÐ´Ð¼ÐµÑ‚
    // Ð¼Ð¾Ð¶Ð½Ð¾
    // ÐºÑƒÐ¿Ð¸Ñ‚ÑŒ
    // Ñ‡ÐµÑ€ÐµÐ·
    // $s1
    // Ñ‡
    // $s2
    // Ð¼Ð¸Ð½.
    public static final int THERE_ARE_S1_MINUTES_REMAINING_UNTIL_THE_TIME_WHEN_THE_ITEM_CAN_BE_PURCHASED = 2035; // ÐŸÑ€ÐµÐ´Ð¼ÐµÑ‚
    // Ð¼Ð¾Ð¶Ð½Ð¾
    // ÐºÑƒÐ¿Ð¸Ñ‚ÑŒ
    // Ñ‡ÐµÑ€ÐµÐ·
    // $s1
    // Ð¼Ð¸Ð½.
    public static final int UNABLE_TO_INVITE_BECAUSE_THE_PARTY_IS_LOCKED = 2036; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ð¿Ñ€Ð¸Ð³Ð»Ð°Ñ�Ð¸Ñ‚ÑŒ,
    // Ð¿Ð¾Ñ�ÐºÐ¾Ð»ÑŒÐºÑƒ
    // Ð³Ñ€ÑƒÐ¿Ð¿Ð°
    // Ð·Ð°ÐºÑ€Ñ‹Ñ‚Ð°.
    public static final int UNABLE_TO_CREATE_CHARACTER_YOU_ARE_UNABLE_TO_CREATE_A_NEW_CHARACTER_ON_THE_SELECTED_SERVER_A = 2037; // Ð�ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾
    // Ñ�Ð¾Ð·Ð´Ð°Ñ‚ÑŒ
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°
    // Ð½Ð°
    // Ð²Ñ‹Ð±Ñ€Ð°Ð½Ð½Ð¾Ð¼
    // Ñ�ÐµÑ€Ð²ÐµÑ€Ðµ.
    // Ð”ÐµÐ¹Ñ�Ñ‚Ð²ÑƒÐµÑ‚
    // Ð¾Ð³Ñ€Ð°Ð½Ð¸Ñ‡ÐµÐ½Ð¸Ðµ,
    // Ð½Ðµ
    // Ð¿Ð¾Ð·Ð²Ð¾Ð»Ñ�ÑŽÑ‰ÐµÐµ
    // Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÐµÐ»Ñ�Ð¼
    // Ñ�Ð¾Ð·Ð´Ð°Ð²Ð°Ñ‚ÑŒ
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶ÐµÐ¹
    // Ð½Ð°
    // Ñ€Ð°Ð·Ð»Ð¸Ñ‡Ð½Ñ‹Ñ…
    // Ñ�ÐµÑ€Ð²ÐµÑ€Ð°Ñ…,
    // Ð³Ð´Ðµ
    // Ð´Ð¾
    // Ñ�Ñ‚Ð¾Ð³Ð¾
    // Ð½Ðµ
    // Ð±Ñ‹Ð»Ð¾
    // Ñ�Ð¾Ð·Ð´Ð°Ð½Ð¾
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶ÐµÐ¹.
    // ÐŸÐ¾Ð¶Ð°Ð»ÑƒÐ¹Ñ�Ñ‚Ð°,
    // Ð²Ñ‹Ð±ÐµÑ€Ð¸Ñ‚Ðµ
    // Ð´Ñ€ÑƒÐ³Ð¾Ð¹
    // Ñ�ÐµÑ€Ð²ÐµÑ€.
    public static final int SOME_LINEAGE_II_FEATURES_HAVE_BEEN_LIMITED_FOR_FREE_TRIALS_TRIAL_ACCOUNTS_ARENT_ALLOWED_TO_DROP = 2038; // Ð­Ñ‚Ð¾Ñ‚
    // Ð°ÐºÐºÐ°ÑƒÐ½Ñ‚
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ñ�Ð±Ñ€Ð°Ñ�Ñ‹Ð²Ð°Ñ‚ÑŒ
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ñ‹.
    public static final int THIS_ACCOUNT_CANOT_TRADE_ITEMS = 2039; // Ð­Ñ‚Ð¾Ñ‚
    // Ð°ÐºÐºÐ°ÑƒÐ½Ñ‚ Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð¿Ñ€Ð¾Ð´Ð°Ð²Ð°Ñ‚ÑŒ
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ñ‹.
    public static final int CANNOT_TRADE_ITEMS_WITH_THE_TARGETED_USER = 2040; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ñ‚Ð¾Ñ€Ð³Ð¾Ð²Ð°Ñ‚ÑŒ
    // Ñ�
    // Ð²Ñ‹Ð±Ñ€Ð°Ð½Ð½Ñ‹Ð¼
    // Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÐµÐ»ÐµÐ¼.
    public static final int SOME_LINEAGE_II_FEATURES_HAVE_BEEN_LIMITED_FOR_FREE_TRIALS_TRIAL_ACCOUNTS_ARENT_ALLOWED_TO = 2041; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ð²Ð¾Ð¹Ñ‚Ð¸
    // Ð²
    // Ð»Ð¸Ñ‡Ð½ÑƒÑŽ
    // Ñ‚Ð¾Ñ€Ð³Ð¾Ð²ÑƒÑŽ
    // Ð»Ð°Ð²ÐºÑƒ.
    public static final int THIS_ACCOUNT_HAS_BEEN_SUSPENDED_FOR_NON_PAYMENT_BASED_ON_THE_CELL_PHONE_PAYMENT_AGREEMENT__N = 2042; // Ð­Ñ‚Ð¾Ñ‚
    // Ð°ÐºÐºÐ°ÑƒÐ½Ñ‚
    // Ð±Ñ‹Ð»
    // Ð·Ð°Ð±Ð»Ð¾ÐºÐ¸Ñ€Ð¾Ð²Ð°Ð½
    // Ð·Ð°
    // Ð½ÐµÑƒÐ¿Ð»Ð°Ñ‚Ñƒ
    // Ð²
    // Ñ�Ð¾Ð¾Ñ‚Ð²ÐµÑ‚Ñ�Ñ‚Ð²Ð¸Ð¸
    // Ñ�
    // Ñ�Ð¾Ð³Ð»Ð°ÑˆÐµÐ½Ð¸ÐµÐ¼
    // Ð¾Ð±
    // Ð¾Ð¿Ð»Ð°Ñ‚Ðµ
    // Ñ‡ÐµÑ€ÐµÐ·
    // Ñ‚ÐµÐ»ÐµÑ„Ð¾Ð½.
    // \\n
    // ÐŸÐ¾Ð¶Ð°Ð»ÑƒÐ¹Ñ�Ñ‚Ð°,
    // Ð¿Ñ€Ð¸ÑˆÐ»Ð¸Ñ‚Ðµ
    // Ð¿Ð¾Ð´Ñ‚Ð²ÐµÑ€Ð¶Ð´ÐµÐ½Ð¸Ðµ
    // Ð¾Ð¿Ð»Ð°Ñ‚Ñ‹
    // Ð¸
    // Ñ�Ð²Ñ�Ð¶Ð¸Ñ‚ÐµÑ�ÑŒ
    // Ñ�
    // Ñ†ÐµÐ½Ñ‚Ñ€Ð¾Ð¼
    // Ð¾Ð±Ñ�Ð»ÑƒÐ¶Ð¸Ð²Ð°Ð½Ð¸Ñ�
    // ÐºÐ»Ð¸ÐµÐ½Ñ‚Ð¾Ð².
    public static final int YOU_HAVE_EXCEEDED_YOUR_INVENTORY_VOLUME_LIMIT_AND_MAY_NOT_TAKE_THIS_QUEST_ITEM_PLEASE_MAKE_ROOM = 2043; // Ð’Ñ‹
    // Ð¿Ñ€ÐµÐ²Ñ‹Ñ�Ð¸Ð»Ð¸
    // Ð»Ð¸Ð¼Ð¸Ñ‚
    // Ð¾Ð±ÑŠÐµÐ¼Ð°
    // Ð²Ð°ÑˆÐµÐ³Ð¾
    // Ð¸Ð½Ð²ÐµÐ½Ñ‚Ð°Ñ€Ñ�
    // Ð¸
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð²Ð·Ñ�Ñ‚ÑŒ
    // ÐºÐ²ÐµÑ�Ñ‚Ð¾Ð²Ñ‹Ð¹
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚.
    // ÐžÑ�Ð²Ð¾Ð±Ð¾Ð´Ð¸Ñ‚Ðµ
    // Ð¼ÐµÑ�Ñ‚Ð¾
    // Ð²
    // Ð¸Ð½Ð²ÐµÐ½Ñ‚Ð°Ñ€Ðµ
    // Ð¸
    // Ð²Ð¾Ð·Ð²Ñ€Ð°Ñ‰Ð°Ð¹Ñ‚ÐµÑ�ÑŒ.
    public static final int SOME_LINEAGE_II_FEATURES_HAVE_BEEN_LIMITED_FOR_FREE_TRIALS_TRIAL_ACCOUNTS_ARENT_ALLOWED_TO_SET = 2044; // Ð­Ñ‚Ð¾Ñ‚
    // Ð°ÐºÐºÐ°ÑƒÐ½Ñ‚
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // ÑƒÑ�Ñ‚Ð°Ð½Ð¾Ð²Ð¸Ñ‚ÑŒ
    // Ð»Ð¸Ñ‡Ð½ÑƒÑŽ
    // Ð¼Ð°Ñ�Ñ‚ÐµÑ€Ñ�ÐºÑƒÑŽ.
    public static final int SOME_LINEAGE_II_FEATURES_HAVE_BEEN_LIMITED_FOR_FREE_TRIALS_TRIAL_ACCOUNTS_ARENT_ALLOWED_TO_USE = 2045; // Ð­Ñ‚Ð¾Ñ‚
    // Ð°ÐºÐºÐ°ÑƒÐ½Ñ‚
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒ
    // Ð»Ð¸Ñ‡Ð½ÑƒÑŽ
    // Ð¼Ð°Ñ�Ñ‚ÐµÑ€Ñ�ÐºÑƒÑŽ.
    public static final int THIS_ACCOUNT_CANOT_USE_PRIVATE_STORES = 2046; // Ð­Ñ‚Ð¾Ñ‚
    // Ð°ÐºÐºÐ°ÑƒÐ½Ñ‚
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒ
    // Ð»Ð¸Ñ‡Ð½ÑƒÑŽ
    // Ñ‚Ð¾Ñ€Ð³Ð¾Ð²ÑƒÑŽ
    // Ð»Ð°Ð²ÐºÑƒ.
    public static final int SOME_LINEAGE_II_FEATURES_HAVE_BEEN_LIMITED_FOR_FREE_TRIALS_TRIAL_ACCOUNTS_ARENT_ALLOWED_TO_ = 2047; // Ð­Ñ‚Ð¾Ñ‚
    // Ð°ÐºÐºÐ°ÑƒÐ½Ñ‚
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒ
    // ÐºÐ»Ð°Ð½Ð¾Ð²Ð¾Ðµ
    // Ñ…Ñ€Ð°Ð½Ð¸Ð»Ð¸Ñ‰Ðµ.
    public static final int THE_SHORTCUT_IN_USE_CONFLICTS_WITH_S1_DO_YOU_WISH_TO_RESET_THE_CONFLICTING_SHORTCUTS_AND_USE_THE = 2048; // Ð¯Ñ€Ð»Ñ‹Ðº
    // ÐºÐ¾Ð½Ñ„Ð»Ð¸ÐºÑ‚ÑƒÐµÑ‚
    // Ñ�
    // $s1.
    // Ð¥Ð¾Ñ‚Ð¸Ñ‚Ðµ
    // Ñ�Ð±Ñ€Ð¾Ñ�Ð¸Ñ‚ÑŒ
    // ÐºÐ¾Ð½Ñ„Ð»Ð¸ÐºÑ‚ÑƒÑŽÑ‰Ð¸Ð¹
    // Ñ�Ñ€Ð»Ñ‹Ðº
    // Ð¸
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒ
    // Ñ�Ð¾Ñ…Ñ€Ð°Ð½ÐµÐ½Ð½Ñ‹Ð¹?
    public static final int THE_SHORTCUT_WILL_BE_APPLIED_AND_SAVED_IN_THE_SERVER_WILL_YOU_CONTINUE = 2049; // Ð¯Ñ€Ð»Ñ‹Ðº
    // Ð¿Ñ€Ð¸Ð½Ñ�Ñ‚
    // Ð¸
    // Ñ�Ð¾Ñ…Ñ€Ð°Ð½ÐµÐ½
    // Ð½Ð°
    // Ñ�ÐµÑ€Ð²ÐµÑ€Ðµ.
    // ÐŸÑ€Ð¾Ð´Ð¾Ð»Ð¶Ð¸Ñ‚ÑŒ?
    public static final int S1_CLAN_IS_TRYING_TO_DISPLAY_A_FLAG = 2050; // ÐšÐ»Ð°Ð½
    // $s1
    // Ð¿Ñ‹Ñ‚Ð°ÐµÑ‚Ñ�Ñ�
    // Ð¿Ð¾Ð´Ð½Ñ�Ñ‚ÑŒ
    // Ñ„Ð»Ð°Ð³.
    public static final int YOU_MUST_ACCEPT_THE_USER_AGREEMENT_BEFORE_THIS_ACCOUNT_CAN_ACCESS_LINEAGE_II__N_PLEASE_TRY_AGAIN = 2051; // Ð§Ñ‚Ð¾Ð±Ñ‹
    // Ð¿Ð¾Ð»ÑƒÑ‡Ð¸Ñ‚ÑŒ
    // Ð´Ð¾Ñ�Ñ‚ÑƒÐ¿
    // Ð²
    // Lineage
    // II,
    // Ð’Ñ‹
    // Ð´Ð¾Ð»Ð¶Ð½Ñ‹
    // Ð¿Ñ€Ð¸Ð½Ñ�Ñ‚ÑŒ
    // Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÐµÐ»ÑŒÑ�ÐºÐ¾Ðµ
    // Ñ�Ð¾Ð³Ð»Ð°ÑˆÐµÐ½Ð¸Ðµ.
    // \\n
    // ÐŸÐ¾Ð¶Ð°Ð»ÑƒÐ¹Ñ�Ñ‚Ð°,
    // Ð¿Ð¾Ð¿Ñ€Ð¾Ð±ÑƒÐ¹Ñ‚Ðµ
    // ÐµÑ‰Ðµ
    // Ñ€Ð°Ð·.
    public static final int A_GUARDIAN_S_CONSENT_IS_REQUIRED_BEFORE_THIS_ACCOUNT_CAN_BE_USED_TO_PLAY_LINEAGE_II__NPLEASE_TRY = 2052; // Ð�ÐºÐºÐ°ÑƒÐ½Ñ‚
    // Ð±ÑƒÐ´ÐµÑ‚
    // Ð´Ð¾Ð¿ÑƒÑ‰ÐµÐ½
    // Ðº
    // Ð¸Ð³Ñ€Ðµ
    // Ð²
    // Lineage
    // II
    // Ñ‚Ð¾Ð»ÑŒÐºÐ¾
    // Ð¿Ð¾Ñ�Ð»Ðµ
    // Ð¿Ñ€Ð¸Ð½Ñ�Ñ‚Ð¸Ñ�
    // Ñ�Ð¾Ð³Ð»Ð°ÑˆÐµÐ½Ð¸Ñ�
    // Ð¾
    // Ð±ÐµÐ·Ð¾Ð¿Ð°Ñ�Ð½Ð¾Ñ�Ñ‚Ð¸.
    // \\n
    // ÐŸÐ¾Ð²Ñ‚Ð¾Ñ€Ð¸Ñ‚Ðµ
    // Ð¿Ð¾Ð¿Ñ‹Ñ‚ÐºÑƒ
    // Ð¿Ð¾Ñ�Ð»Ðµ
    // Ð¿Ñ€Ð¸Ð½Ñ�Ñ‚Ð¸Ñ�
    // Ñ�Ð¾Ð³Ð»Ð°ÑˆÐµÐ½Ð¸Ñ�.
    public static final int THIS_ACCOUNT_HAS_DECLINED_THE_USER_AGREEMENT_OR_IS_PENDING_A_WITHDRAWL_REQUEST___NPLEASE_TRY = 2053; // Ð­Ñ‚Ð¾Ñ‚
    // Ð°ÐºÐºÐ°ÑƒÐ½Ñ‚
    // Ð¾Ñ‚ÐºÐ»Ð¾Ð½Ð¸Ð»
    // Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÐµÐ»ÑŒÑ�ÐºÐ¾Ðµ
    // Ñ�Ð¾Ð³Ð»Ð°ÑˆÐµÐ½Ð¸Ðµ
    // Ð¸Ð»Ð¸
    // Ð·Ð°Ð¿Ñ€Ð¾Ñ�Ð¸Ð»
    // Ð¾Ñ‚ÐºÐ»Ð¾Ð½ÐµÐ½Ð¸Ðµ.\\n
    // ÐŸÐ¾Ð¶Ð°Ð»ÑƒÐ¹Ñ�Ñ‚Ð°,
    // Ð¿Ð¾Ð²Ñ‚Ð¾Ñ€Ð¸Ñ‚Ðµ
    // Ð¿Ð¾Ð¿Ñ‹Ñ‚ÐºÑƒ
    // Ð¿Ð¾Ñ�Ð»Ðµ
    // Ð¾Ñ‚ÐºÐ»Ð¾Ð½ÐµÐ½Ð¸Ñ�
    // Ð·Ð°Ð¿Ñ€Ð¾Ñ�Ð°.
    public static final int THIS_ACCOUNT_HAS_BEEN_SUSPENDED___NFOR_MORE_INFORMATION_PLEASE_CALL_THE_CUSTOMER_S_CENTER_TEL = 2054; // Ð”ÐµÐ¹Ñ�Ñ‚Ð²Ð¸Ðµ
    // Ñ�Ñ‚Ð¾Ð³Ð¾
    // Ð°ÐºÐºÐ°ÑƒÐ½Ñ‚Ð°
    // Ð¿Ñ€Ð¸Ð¾Ñ�Ñ‚Ð°Ð½Ð¾Ð²Ð»ÐµÐ½Ð¾.
    // \\n
    // Ð—Ð°
    // Ð´Ð¾Ð¿Ð¾Ð»Ð½Ð¸Ñ‚ÐµÐ»ÑŒÐ½Ð¾Ð¹
    // Ð¸Ð½Ñ„Ð¾Ñ€Ð¼Ð°Ñ†Ð¸ÐµÐ¹
    // Ð¾Ð±Ñ€Ð°Ñ‰Ð°Ð¹Ñ‚ÐµÑ�ÑŒ
    // Ð²
    // Ð¡Ð»ÑƒÐ¶Ð±Ñƒ
    // ÐŸÐ¾Ð´Ð´ÐµÑ€Ð¶ÐºÐ¸
    // ÐŸÐ¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÐµÐ»ÐµÐ¹.
    public static final int YOUR_ACCOUNT_HAS_BEEN_SUSPENDED_FROM_ALL_GAME_SERVICES__NFOR_MORE_INFORMATION_PLEASE_VISIT_THE = 2055; // Ð’Ð°Ñˆ
    // Ð°ÐºÐºÐ°ÑƒÐ½Ñ‚
    // Ð¾Ñ‚ÐºÐ»ÑŽÑ‡ÐµÐ½
    // Ð¾Ñ‚
    // Ð²Ñ�ÐµÑ…
    // Ð¸Ð³Ñ€Ð¾Ð²Ñ‹Ñ…
    // ÑƒÑ�Ð»ÑƒÐ³.
    // \\n
    // Ð—Ð°
    // Ð´Ð¾Ð¿Ð¾Ð»Ð½Ð¸Ñ‚ÐµÐ»ÑŒÐ½Ð¾Ð¹
    // Ð¸Ð½Ñ„Ð¾Ñ€Ð¼Ð°Ñ†Ð¸ÐµÐ¹
    // Ð¾Ð±Ñ€Ð°Ñ‰Ð°Ð¹Ñ‚ÐµÑ�ÑŒ
    // Ð²
    // Ð¡Ð»ÑƒÐ¶Ð±Ñƒ
    // ÐŸÐ¾Ð´Ð´ÐµÑ€Ð¶ÐºÐ¸
    // ÐŸÐ¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÐµÐ»ÐµÐ¹.
    public static final int YOUR_ACCOUNT_HAS_BEEN_CONVERTED_TO_AN_INTEGRATED_ACCOUNT_AND_IS_UNABLE_TO_BE_ACCESSED___NPLEASE = 2056; // Ð’Ð°Ñˆ
    // Ð°ÐºÐºÐ°ÑƒÐ½Ñ‚
    // Ð±Ñ‹Ð»
    // Ð¿Ñ€ÐµÐ¾Ð±Ñ€Ð°Ð·Ð¾Ð²Ð°Ð½
    // Ð²
    // Ð¾Ð±ÑŠÐµÐ´Ð¸Ð½ÐµÐ½Ð½Ñ‹Ð¹
    // Ð°ÐºÐºÐ°ÑƒÐ½Ñ‚
    // Ð¸
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð²Ñ‹Ð±Ñ€Ð°Ð½.
    // \\n
    // ÐŸÐ¾Ð¶Ð°Ð»ÑƒÐ¹Ñ�Ñ‚Ð°,
    // Ð¿Ñ€Ð¾Ð¹Ð´Ð¸Ñ‚Ðµ
    // Ñ€ÐµÐ³Ð¸Ñ�Ñ‚Ñ€Ð°Ñ†Ð¸ÑŽ
    // Ð¾Ð±ÑŠÐµÐ´Ð¸Ð½ÐµÐ½Ð½Ñ‹Ð¼
    // Ð°ÐºÐºÐ°ÑƒÐ½Ñ‚Ð¾Ð¼.
    public static final int YOU_HAVE_BLOCKED_C1 = 2057; // Ð’Ñ‹ Ð·Ð°Ð±Ð»Ð¾ÐºÐ¸Ñ€Ð¾Ð²Ð°Ð»Ð¸ $c1.
    public static final int THAT_ITEM_CANNOT_BE_TAKEN_OFF = 2065; // ÐŸÑ€ÐµÐ´Ð¼ÐµÑ‚ Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚ Ð±Ñ‹Ñ‚ÑŒ
    // Ð²Ñ‹Ð±Ñ€Ð¾ÑˆÐµÐ½.
    public static final int THAT_WEAPON_CANNOT_PERFORM_ANY_ATTACKS = 2066; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð°Ñ‚Ð°ÐºÐ¾Ð²Ð°Ñ‚ÑŒ
    // Ñ�Ñ‚Ð¸Ð¼
    // Ð¾Ñ€ÑƒÐ¶Ð¸ÐµÐ¼.
    public static final int THAT_WEAPON_CANNOT_USE_ANY_OTHER_SKILL_EXCEPT_THE_WEAPON_S_SKILL = 2067; // ÐžÑ€ÑƒÐ¶Ð¸Ðµ
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒ
    // ÑƒÐ¼ÐµÐ½Ð¸Ñ�,
    // ÐºÑ€Ð¾Ð¼Ðµ
    // ÑƒÐ¼ÐµÐ½Ð¸Ð¹
    // Ð¾Ñ€ÑƒÐ¶Ð¸Ñ�.
    public static final int THERE_ARE_NO_FUNDS_PRESENTLY_DUE_TO_YOU = 2081; // Ð�ÐµÑ‚
    // Ñ€ÐµÐ·ÐµÑ€Ð²Ð°.
    public static final int YOU_HAVE_EXCEEDED_THE_TOTAL_AMOUT_OF_ADENA_ALLOWED_IN_INVENTORY = 2082; // Ð’Ñ‹
    // Ð¿Ñ€ÐµÐ²Ñ‹Ñ�Ð¸Ð»Ð¸
    // Ð¿Ñ€ÐµÐ´ÐµÐ»ÑŒÐ½Ð¾
    // Ð´Ð¾Ð¿ÑƒÑ�Ñ‚Ð¸Ð¼ÑƒÑŽ
    // Ñ�ÑƒÐ¼Ð¼Ñƒ
    // Ð²
    // Ð¸Ð½Ð²ÐµÐ½Ñ‚Ð°Ñ€Ðµ.
    public static final int SEARCH_ON_USER_C2_FOR_THIRD_PARTY_PROGRAM_USE_WILL_BE_COMPLETED_IN_S1_MINUTES = 2086; // ÐŸÐ¾Ð¸Ñ�Ðº
    // Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÐµÐ»Ñ�
    // $c2,
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·ÑƒÑŽÑ‰ÐµÐ³Ð¾
    // Ñ�Ñ‚Ð¾Ñ€Ð¾Ð½Ð½Ð¸Ðµ
    // Ð¿Ñ€Ð¾Ð³Ñ€Ð°Ð¼Ð¼Ñ‹,
    // Ð·Ð°Ð²ÐµÑ€ÑˆÐ¸Ñ‚Ñ�Ñ�
    // Ñ‡ÐµÑ€ÐµÐ·
    // $s1
    // Ð¼Ð¸Ð½.
    public static final int YOUR_ACCOUNT_CAN_ONLY_BE_USED_AFTER_CHANGING_YOUR_PASSWORD_AND_QUIZ___N_SERVICES_WILL_BE = 2091; // Ð’Ð°Ñˆ
    // Ð°ÐºÐºÐ°ÑƒÐ½Ñ‚
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒÑ�Ñ�
    // Ñ‚Ð¾Ð»ÑŒÐºÐ¾
    // Ð¿Ð¾Ñ�Ð»Ðµ
    // Ñ�Ð¼ÐµÐ½Ñ‹
    // Ð¿Ð°Ñ€Ð¾Ð»Ñ�
    // Ð¸
    // ÐºÐ¾Ð½Ñ‚Ñ€Ð¾Ð»ÑŒÐ½Ð¾Ð³Ð¾
    // Ð²Ð¾Ð¿Ñ€Ð¾Ñ�Ð°.
    public static final int YOU_CANNOT_BID_DUE_TO_A_PASSED_IN_PRICE = 2092; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ñ�Ð´ÐµÐ»Ð°Ñ‚ÑŒ
    // Ñ�Ñ‚Ñƒ
    // Ñ�Ñ‚Ð°Ð²ÐºÑƒ
    // Ð¸Ð·-Ð·Ð°
    // Ñ‚Ð¾Ð³Ð¾,
    // Ñ‡Ñ‚Ð¾
    // Ñ†ÐµÐ½Ð°
    // Ð¿Ñ€Ð¾Ñ�Ñ€Ð¾Ñ‡ÐµÐ½Ð°.
    public static final int THE_BID_AMOUNT_WAS_S1_ADENA_WOULD_YOU_LIKE_TO_RETRIEVE_THE_BID_AMOUNT = 2093; // Ð¡ÑƒÐ¼Ð¼Ð°
    // Ñ�Ñ‚Ð°Ð²ÐºÐ¸
    // -
    // $s1
    // Ð°Ð´ÐµÐ½.
    // Ð–ÐµÐ»Ð°ÐµÑ‚Ðµ
    // Ð·Ð°Ð±Ñ€Ð°Ñ‚ÑŒ
    // Ð’Ð°ÑˆÑƒ
    // Ñ�Ñ‚Ð°Ð²ÐºÑƒ?
    public static final int ANOTHER_USER_IS_PURCHASING_PLEASE_TRY_AGAIN_LATER = 2094; // ÐŸÐ¾ÐºÑƒÐ¿Ð°ÐµÑ‚
    // Ð´Ñ€ÑƒÐ³Ð¾Ð¹
    // Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÐµÐ»ÑŒ.
    // ÐŸÐ¾Ð¶Ð°Ð»ÑƒÐ¹Ñ�Ñ‚Ð°,
    // Ð¿Ð¾Ð²Ñ‚Ð¾Ñ€Ð¸Ñ‚Ðµ
    // Ð¿Ð¾Ð¿Ñ‹Ñ‚ÐºÑƒ
    // Ð¿Ð¾Ð·Ð¶Ðµ.
    public static final int SOME_LINEAGE_II_FEATURES_HAVE_BEEN_LIMITED_FOR_FREE_TRIALS_TRIAL_ACCOUNTS_HAVE_LIMITED_CHATTING = 2095; // Ð­Ñ‚Ð¾Ñ‚
    // Ð°ÐºÐºÐ°ÑƒÐ½Ñ‚
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒ
    // ÐºÑ€Ð¸Ðº.
    public static final int YOU_CANNOT_ENTER_BECAUSE_YOU_ARE_NOT_ASSOCIATED_WITH_THE_CURRENT_COMMAND_CHANNEL = 2103; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð²Ð¾Ð¹Ñ‚Ð¸,
    // Ñ‚Ð°Ðº
    // ÐºÐ°Ðº
    // Ð’Ñ‹
    // Ð½Ðµ
    // Ð²
    // Ñ‚ÐµÐºÑƒÑ‰ÐµÐ¼
    // ÐšÐ°Ð½Ð°Ð»Ðµ
    // Ð�Ð»ÑŒÑ�Ð½Ñ�Ð°.
    public static final int THE_MAXIMUM_NUMBER_OF_INSTANCE_ZONES_HAS_BEEN_EXCEEDED_YOU_CANNOT_ENTER = 2104; // ÐŸÑ€ÐµÐ²Ñ‹ÑˆÐµÐ½Ð¾
    // Ð¼Ð°ÐºÑ�Ð¸Ð¼Ð°Ð»ÑŒÐ½Ð¾Ðµ
    // ÐºÐ¾Ð»Ð¸Ñ‡ÐµÑ�Ñ‚Ð²Ð¾
    // Ð²Ñ€ÐµÐ¼ÐµÐ½Ð½Ñ‹Ñ…
    // Ð·Ð¾Ð½.
    // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð²Ð¾Ð¹Ñ‚Ð¸.
    public static final int YOUR_ACCOUNT_HAS_BEEN_SUSPENDED_FOR_10_DAYS_FOR_USE_OF_ILLEGAL_SOFTWARE_AND_MAY_BE_PERMANENTLY = 2108; // Ð’Ð°Ñˆ
    // Ð°ÐºÐºÐ°ÑƒÐ½Ñ‚
    // Ð·Ð°Ð±Ð»Ð¾ÐºÐ¸Ñ€Ð¾Ð²Ð°Ð½
    // Ð½Ð°
    // 10
    // Ð´Ð½ÐµÐ¹
    // Ð·Ð°
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ð½Ð¸Ðµ
    // Ð½ÐµÐ»Ð¸Ñ†ÐµÐ½Ð·Ð¸Ð¾Ð½Ð½Ð¾Ð³Ð¾
    // Ð¿Ñ€Ð¾Ð³Ñ€Ð°Ð¼Ð¼Ð½Ð¾Ð³Ð¾
    // Ð¾Ð±ÐµÑ�Ð¿ÐµÑ‡ÐµÐ½Ð¸Ñ�
    // Ð¸
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð·Ð°Ð±Ð»Ð¾ÐºÐ¸Ñ€Ð¾Ð²Ð°Ð½
    // Ð±ÐµÑ�Ñ�Ñ€Ð¾Ñ‡Ð½Ð¾.
    // Ð—Ð°
    // Ð´Ð¾Ð¿Ð¾Ð»Ð½Ð¸Ñ‚ÐµÐ»ÑŒÐ½Ð¾Ð¹
    // Ð¸Ð½Ñ„Ð¾Ñ€Ð¼Ð°Ñ†Ð¸ÐµÐ¹
    // Ð¾Ð±Ñ€Ð°Ñ‚Ð¸Ñ‚ÐµÑ�ÑŒ
    // Ð²
    // Ñ†ÐµÐ½Ñ‚Ñ€
    // Ð¿Ð¾Ð´Ð´ÐµÑ€Ð¶ÐºÐ¸.
    public static final int THE_SERVER_HAS_BEEN_INTEGRATED_AND_YOUR_CHARACTER_S1_HAS_BEEN_OVERLAPPED_WITH_ANOTHER_NAME = 2109; // Ð¡ÐµÑ€Ð²ÐµÑ€
    // Ð±Ñ‹Ð»
    // Ð¾Ð±ÑŠÐµÐ´Ð¸Ð½ÐµÐ½
    // Ð¸
    // Ð¸Ð¼Ñ�
    // Ð²Ð°ÑˆÐµÐ³Ð¾
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°
    // Ð±Ñ‹Ð»Ð¾
    // Ð¿ÐµÑ€ÐµÐºÑ€Ñ‹Ñ‚Ð¾
    // Ð´Ñ€ÑƒÐ³Ð¸Ð¼
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶ÐµÐ¼.
    // ÐŸÐ¾Ð¶Ð°Ð»ÑƒÐ¹Ñ�Ñ‚Ð°,
    // Ð²Ð²ÐµÐ´Ð¸Ñ‚Ðµ
    // Ð½Ð¾Ð²Ð¾Ðµ
    // Ð¸Ð¼Ñ�
    // Ð²Ð°ÑˆÐµÐ³Ð¾
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°.
    public static final int THIS_CHARACTER_NAME_ALREADY_EXISTS_OR_IS_AN_INVALID_NAME_PLEASE_ENTER_A_NEW_NAME = 2110; // Ð˜Ð¼Ñ�
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°
    // ÑƒÐ¶Ðµ
    // Ñ�ÑƒÑ‰ÐµÑ�Ñ‚Ð²ÑƒÐµÑ‚
    // Ð»Ð¸Ð±Ð¾
    // Ñ�Ñ‚Ð¾
    // Ð½ÐµÐºÐ¾Ñ€Ñ€ÐµÐºÑ‚Ð½Ð¾Ðµ
    // Ð¸Ð¼Ñ�.
    // ÐŸÐ¾Ð¶Ð°Ð»ÑƒÐ¹Ñ�Ñ‚Ð°,
    // Ð²Ð²ÐµÐ´Ð¸Ñ‚Ðµ
    // Ð´Ñ€ÑƒÐ³Ð¾Ðµ
    // Ð¸Ð¼Ñ�.
    public static final int ENTER_A_SHORTCUT_TO_ASSIGN = 2111; // Ð�Ð°Ð·Ð½Ð°Ñ‡ÑŒÑ‚Ðµ
    // Ñ�Ñ€Ð»Ñ‹Ðº.
    public static final int SUB_KEY_CAN_BE_CTRL_ALT_SHIFT_AND_YOU_MAY_ENTER_TWO_SUB_KEYS_AT_A_TIME___N_EXAMPLE__CTRL___ALT__ = 2112; // Ð’Ñ�Ð¿Ð¾Ð¼Ð¾Ð³Ð°Ñ‚ÐµÐ»ÑŒÐ½Ñ‹Ð¼Ð¸
    // ÐºÐ»Ð°Ð²Ð¸ÑˆÐ°Ð¼Ð¸
    // Ð¼Ð¾Ð³ÑƒÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // CTRL,
    // ALT,
    // SHIFT.
    // Ð’Ñ‹
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð½Ð°Ð¶Ð¸Ð¼Ð°Ñ‚ÑŒ
    // Ð´Ð²Ðµ
    // Ð¾Ð´Ð½Ð¾Ð²Ñ€ÐµÐ¼ÐµÐ½Ð½Ð¾.
    // Ð�Ð°Ð¿Ñ€Ð¸Ð¼ÐµÑ€,
    // "CTRL + ALT + A"
    public static final int CTRL_ALT_SHIFT_KEYS_MAY_BE_USED_AS_SUB_KEY_IN_EXPANDED_SUB_KEY_MODE_AND_ONLY_ALT_MAY_BE_USED_AS = 2113; // ÐšÐ»Ð°Ð²Ð¸ÑˆÐ¸
    // CTRL,
    // ALT,
    // SHIFT
    // Ð¼Ð¾Ð³ÑƒÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ð½Ñ‹
    // Ð²
    // Ñ€Ð°Ñ�ÑˆÐ¸Ñ€ÐµÐ½Ð½Ð¾Ð¼
    // Ñ€ÐµÐ¶Ð¸Ð¼Ðµ
    // Ð²Ñ�Ð¿Ð¾Ð¼Ð¾Ð³Ð°Ñ‚ÐµÐ»ÑŒÐ½Ñ‹Ñ…
    // ÐºÐ»Ð°Ð²Ð¸Ñˆ,
    // Ð¸
    // Ñ‚Ð¾Ð»ÑŒÐºÐ¾
    // ALT
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ð½
    // ÐºÐ°Ðº
    // Ð²Ñ�Ð¿Ð¾Ð¼Ð¾Ð³Ð°Ñ‚ÐµÐ»ÑŒÐ½Ð°Ñ�
    // ÐºÐ»Ð°Ð²Ð¸ÑˆÐ°
    // Ð²
    // Ñ�Ñ‚Ð°Ð½Ð´Ð°Ñ€Ñ‚Ð½Ð¾Ð¼
    // Ñ€ÐµÐ¶Ð¸Ð¼Ðµ.
    public static final int FORCED_ATTACK_AND_STAND_IN_PLACE_ATTACKS_ASSIGNED_PREVIOUSLY_TO_CTRL_AND_SHIFT_WILL_BE_CHANGED = 2114; // Ð¡Ð¸Ð»Ð¾Ð²Ð°Ñ�
    // Ð¸
    // Ð¾Ñ�Ñ‚Ð°Ð½Ð°Ð²Ð»Ð¸Ð²Ð°ÑŽÑ‰Ð°Ñ�
    // Ð°Ñ‚Ð°ÐºÐ°,
    // Ð¿Ñ€ÐµÐ¶Ð´Ðµ
    // Ð½Ð°Ð·Ð½Ð°Ñ‡ÐµÐ½Ð½Ð°Ñ�
    // Ð½Ð°
    // Ctrl
    // Ð¸
    // Shift,
    // Ð¿Ñ€Ð¸
    // ÑƒÑ�Ñ‚Ð°Ð½Ð¾Ð²ÐºÐµ
    // Ñ€Ð°Ñ�ÑˆÐ¸Ñ€ÐµÐ½Ð½Ð¾Ð³Ð¾
    // Ñ€ÐµÐ¶Ð¸Ð¼Ð°
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð²
    // Ð±ÑƒÐ´ÐµÑ‚
    // Ð¿ÐµÑ€ÐµÐ½Ð°Ð·Ð½Ð°Ñ‡ÐµÐ½Ð°
    // Ð½Ð°
    // Alt
    // +
    // Q
    // Ð¸
    // Alt
    // +
    // E.
    // Ctrl
    // Ð¸
    // Shift
    // Ð¾Ñ�Ð²Ð¾Ð±Ð¾Ð´Ñ�Ñ‚Ñ�Ñ�
    // Ð´Ð»Ñ�
    // ÑƒÑ�Ñ‚Ð°Ð½Ð¾Ð²ÐºÐ¸
    // Ð´Ñ€ÑƒÐ³Ð¸Ñ…
    // Ð±Ñ‹Ñ�Ñ‚Ñ€Ñ‹Ñ…
    // Ð´Ð¾Ñ�Ñ‚ÑƒÐ¿Ð¾Ð².
    // ÐŸÑ€Ð¾Ð´Ð¾Ð»Ð¶Ð¸Ñ‚ÑŒ?
    public static final int YOUR_ACCOUNT_HAS_BEEN_SUSPENDED_FOR_ABUSING_A_BUG_RELATED_TO_THE_NCCOIN_FOR_MORE_INFORMATION = 2115; // Ð’Ð°Ñˆ
    // Ð°ÐºÐºÐ°ÑƒÐ½Ñ‚
    // Ð·Ð°Ð±Ð»Ð¾ÐºÐ¸Ñ€Ð¾Ð²Ð°Ð½
    // Ð·Ð°
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ð½Ð¸Ðµ
    // Ð¾ÑˆÐ¸Ð±ÐºÐ¸,
    // Ð¾Ñ‚Ð½Ð¾Ñ�Ñ�Ñ‰ÐµÐ¹Ñ�Ñ�
    // Ðº
    // Nccoin.
    // Ð—Ð°
    // Ð´Ð¾Ð¿Ð¾Ð»Ð½Ð¸Ñ‚ÐµÐ»ÑŒÐ½Ð¾Ð¹
    // Ð¸Ð½Ñ„Ð¾Ñ€Ð¼Ð°Ñ†Ð¸ÐµÐ¹
    // Ð¾Ð±Ñ€Ð°Ñ‰Ð°Ð¹Ñ‚ÐµÑ�ÑŒ
    // Ð²
    // Ð¦ÐµÐ½Ñ‚Ñ€
    // ÐŸÐ¾Ð´Ð´ÐµÑ€Ð¶ÐºÐ¸.
    public static final int YOUR_ACCOUNT_HAS_BEEN_SUSPENDED_FOR_ABUSING_A_FREE_NCCOIN_FOR_MORE_INFORMATION_PLEASE_VISIT_THE = 2116; // Ð’Ð°ÑˆÐ°
    // ÑƒÑ‡ÐµÑ‚Ð½Ð°Ñ�
    // Ð·Ð°Ð¿Ð¸Ñ�ÑŒ
    // Ð±ÐµÑ�Ð¿Ð»Ð°Ñ‚Ð½Ð¾
    // jigeupdoen
    // Ð¼Ð¾Ð½ÐµÑ‚Ð°
    // Ð±ÑƒÐ´ÐµÑ‚
    // Ð¾Ð³Ñ€Ð°Ð½Ð¸Ñ‡ÐµÐ½Ð¾,
    // Ð¸
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒÑ�Ñ�
    // Ð´Ð»Ñ�
    // Ð¾Ð¿Ñ€ÐµÐ´ÐµÐ»ÐµÐ½Ð¸Ñ�
    // akyonghan
    // Ð¸Ñ�Ñ‚Ð¾Ñ€Ð¸Ð¸.
    // Ð”Ð»Ñ�
    // Ð¿Ð¾Ð»ÑƒÑ‡ÐµÐ½Ð¸Ñ�
    // Ð´Ð¾Ð¿Ð¾Ð»Ð½Ð¸Ñ‚ÐµÐ»ÑŒÐ½Ð¾Ð¹
    // Ð¸Ð½Ñ„Ð¾Ñ€Ð¼Ð°Ñ†Ð¸Ð¸,
    // ÐºÐ»Ð¸ÐµÐ½Ñ‚Ð¾Ð²
    // Ñ†ÐµÐ½Ñ‚Ñ€Ð°
    // (Ñ‚ÐµÐ».
    // 1600-0020)
    // Ð½Ð°
    // Ð¾Ñ�Ð½Ð¾Ð²Ðµ
    // Ð·Ð°Ð¿Ñ€Ð¾Ñ�Ð°,
    // Ð¿Ð¾Ð¶Ð°Ð»ÑƒÐ¹Ñ�Ñ‚Ð°
    public static final int YOUR_ACCOUNT_HAS_BEEN_SUSPENDED_FOR_USING_ANOTHER_PERSON_S_IDENTIFICATION_IF_YOU_WERE_NOT = 2117; // Ð’Ð°ÑˆÐ°
    // ÑƒÑ‡ÐµÑ‚Ð½Ð°Ñ�
    // Ð·Ð°Ð¿Ð¸Ñ�ÑŒ
    // Ð·Ð°Ð±Ð»Ð¾ÐºÐ¸Ñ€Ð¾Ð²Ð°Ð½Ð°
    // Ð¸Ð·-Ð·Ð°
    // ÐºÑ€Ð°Ð¶Ð¸
    // Ð¾Ð¿Ð»Ð°Ñ‚Ñ‹
    // ÑƒÑ�Ð»ÑƒÐ³
    // Ð½Ðµ
    // Ð¾Ð³Ñ€Ð°Ð½Ð¸Ñ‡Ð¸Ð²Ð°ÐµÑ‚Ñ�Ñ�
    // Ð³Ð¾Ñ�ÑƒÐ´Ð°Ñ€Ñ�Ñ‚Ð²Ð¾Ð¼.
    // Ð”Ð»Ñ�
    // Ð¿Ð¾Ð»ÑƒÑ‡ÐµÐ½Ð¸Ñ�
    // Ð´Ð¾Ð¿Ð¾Ð»Ð½Ð¸Ñ‚ÐµÐ»ÑŒÐ½Ð¾Ð¹
    // Ð¸Ð½Ñ„Ð¾Ñ€Ð¼Ð°Ñ†Ð¸Ð¸,
    // PlayNC
    // Ñ�Ð°Ð¹Ñ‚Ðµ
    // (www.plaync.co.kr)
    // Ñ‡ÐµÑ€ÐµÐ·
    // ÐºÐ»Ð¸ÐµÐ½Ñ‚-Ñ†ÐµÐ½Ñ‚Ñ€
    // Ñ€Ð°Ñ�Ñ�Ð»ÐµÐ´Ð¾Ð²Ð°Ð½Ð¸Ðµ,
    // Ð¿Ð¾Ð¶Ð°Ð»ÑƒÐ¹Ñ�Ñ‚Ð°.
    public static final int YOUR_ACCOUNT_HAS_BEEN_SUSPENDED_FOR_MISAPPROPRIATING_PAYMENT_UNDER_ANOTHER_PLAYER_S_ACCOUNT_FOR = 2118; // Ð’Ð°Ñˆ
    // Ð°ÐºÐºÐ°ÑƒÐ½Ñ‚
    // Ð·Ð°Ð±Ð»Ð¾ÐºÐ¸Ñ€Ð¾Ð²Ð°Ð½
    // Ð·Ð°
    // Ð¿Ñ€Ð¸Ñ�Ð²Ð¾ÐµÐ½Ð¸Ðµ
    // Ð¿Ð»Ð°Ñ‚ÐµÐ¶ÐµÐ¹
    // Ð´Ñ€ÑƒÐ³Ð¾Ð³Ð¾
    // Ð°ÐºÐºÐ°ÑƒÐ½Ñ‚Ð°.
    // Ð—Ð°
    // Ð´Ð¾Ð¿Ð¾Ð»Ð½Ð¸Ñ‚ÐµÐ»ÑŒÐ½Ð¾Ð¹
    // Ð¸Ð½Ñ„Ð¾Ñ€Ð¼Ð°Ñ†Ð¸ÐµÐ¹
    // Ð¾Ð±Ñ€Ð°Ñ‰Ð°Ð¹Ñ‚ÐµÑ�ÑŒ
    // Ð²
    // Ð¡Ð»ÑƒÐ¶Ð±Ñƒ
    // ÐŸÐ¾Ð´Ð´ÐµÑ€Ð¶ÐºÐ¸
    // ÐŸÐ¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÐµÐ»ÐµÐ¹.
    public static final int YOUR_ACCOUNT_HAS_BEEN_SUSPENDED_FROM_ALL_GAME_SERVICES_AFTER_BEING_DETECTED_WITH_DEALING_AN = 2119; // Ð’Ð°Ñˆ
    // Ð°ÐºÐºÐ°ÑƒÐ½Ñ‚
    // Ð¾Ñ‚ÐºÐ»ÑŽÑ‡ÐµÐ½
    // Ð¾Ñ‚
    // Ð²Ñ�ÐµÑ…
    // Ð¸Ð³Ñ€Ð¾Ð²Ñ‹Ñ…
    // ÑƒÑ�Ð»ÑƒÐ³
    // Ð·Ð°
    // Ñ‚Ð¾Ñ€Ð³Ð¾Ð²Ð»ÑŽ
    // Ð°ÐºÐºÐ°ÑƒÐ½Ñ‚Ð°Ð¼Ð¸.
    // \\n
    // Ð—Ð°
    // Ð´Ð¾Ð¿Ð¾Ð»Ð½Ð¸Ñ‚ÐµÐ»ÑŒÐ½Ð¾Ð¹
    // Ð¸Ð½Ñ„Ð¾Ñ€Ð¼Ð°Ñ†Ð¸ÐµÐ¹
    // Ð¾Ð±Ñ€Ð°Ñ‰Ð°Ð¹Ñ‚ÐµÑ�ÑŒ
    // Ð²
    // Ð¡Ð»ÑƒÐ¶Ð±Ñƒ
    // ÐŸÐ¾Ð´Ð´ÐµÑ€Ð¶ÐºÐ¸
    // ÐŸÐ¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÐµÐ»ÐµÐ¹.
    public static final int YOUR_ACCOUNT_HAS_BEEN_SUSPENDED_FOR_10_DAYS_FOR_USING_ILLEGAL_SOFTWARE_YOUR_ACCOUNT_MAY_BE = 2120; // Ð’Ð°Ñˆ
    // Ð°ÐºÐºÐ°ÑƒÐ½Ñ‚
    // Ð·Ð°Ð±Ð»Ð¾ÐºÐ¸Ñ€Ð¾Ð²Ð°Ð½
    // Ð½Ð°
    // 10
    // Ð´Ð½ÐµÐ¹
    // Ð·Ð°
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ð½Ð¸Ðµ
    // Ð½ÐµÐ»ÐµÐ³Ð°Ð»ÑŒÐ½Ð¾Ð³Ð¾
    // Ð¿Ñ€Ð¾Ð³Ñ€Ð°Ð¼Ð¼Ð½Ð¾Ð³Ð¾
    // Ð¾Ð±ÐµÑ�Ð¿ÐµÑ‡ÐµÐ½Ð¸Ñ�.
    // ÐžÐ½
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð·Ð°Ð±Ð»Ð¾ÐºÐ¸Ñ€Ð¾Ð²Ð°Ð½
    // Ð±ÐµÑ�Ñ�Ñ€Ð¾Ñ‡Ð½Ð¾,
    // ÐµÑ�Ð»Ð¸
    // Ð´Ð°Ð½Ð½Ð¾Ðµ
    // Ð½Ð°Ñ€ÑƒÑˆÐµÐ½Ð¸Ðµ
    // Ð±ÑƒÐ´ÐµÑ‚
    // Ð²Ñ‹Ñ�Ð²Ð»ÐµÐ½Ð¾
    // Ð¿Ð¾Ð²Ñ‚Ð¾Ñ€Ð½Ð¾.
    // Ð—Ð°
    // Ð´Ð¾Ð¿Ð¾Ð»Ð½Ð¸Ñ‚ÐµÐ»ÑŒÐ½Ð¾Ð¹
    // Ð¸Ð½Ñ„Ð¾Ñ€Ð¼Ð°Ñ†Ð¸ÐµÐ¹
    // Ð¾Ð±Ñ€Ð°Ñ‚Ð¸Ñ‚ÐµÑ�ÑŒ
    // Ð²
    // Ñ†ÐµÐ½Ñ‚Ñ€
    // Ð¿Ð¾Ð´Ð´ÐµÑ€Ð¶ÐºÐ¸.
    public static final int YOUR_ACCOUNT_HAS_BEEN_SUSPENDED_FROM_ALL_GAME_SERVICES_FOR_USE_OF_ILLEGAL_SOFTWARE_FOR_MORE = 2121; // Ð’Ð°Ñˆ
    // Ð°ÐºÐºÐ°ÑƒÐ½Ñ‚
    // Ð¾Ñ‚ÐºÐ»ÑŽÑ‡ÐµÐ½
    // Ð¾Ñ‚
    // Ð²Ñ�ÐµÑ…
    // Ð¸Ð³Ñ€Ð¾Ð²Ñ‹Ñ…
    // ÑƒÑ�Ð»ÑƒÐ³
    // Ð·Ð°
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ð½Ð¸Ðµ
    // Ð½ÐµÐ»ÐµÐ³Ð°Ð»ÑŒÐ½Ð¾Ð³Ð¾
    // Ð¿Ñ€Ð¾Ð³Ñ€Ð°Ð¼Ð¼Ð½Ð¾Ð³Ð¾
    // Ð¾Ð±ÐµÑ�Ð¿ÐµÑ‡ÐµÐ½Ð¸Ñ�.
    // \\n
    // Ð—Ð°
    // Ð´Ð¾Ð¿Ð¾Ð»Ð½Ð¸Ñ‚ÐµÐ»ÑŒÐ½Ð¾Ð¹
    // Ð¸Ð½Ñ„Ð¾Ñ€Ð¼Ð°Ñ†Ð¸ÐµÐ¹
    // Ð¾Ð±Ñ€Ð°Ñ‰Ð°Ð¹Ñ‚ÐµÑ�ÑŒ
    // Ð²
    // Ð¡Ð»ÑƒÐ¶Ð±Ñƒ
    // ÐŸÐ¾Ð´Ð´ÐµÑ€Ð¶ÐºÐ¸
    // ÐŸÐ¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÐµÐ»ÐµÐ¹.
    public static final int YOUR_ACCOUNT_HAS_BEEN_SUSPENDED_FROM_ALL_GAME_SERVICES_FOR_USE_OF_ILLEGAL_SOFTWARE_FOR_MORE_ = 2122; // Ð’Ð°Ñˆ
    // Ð°ÐºÐºÐ°ÑƒÐ½Ñ‚
    // Ð¾Ñ‚ÐºÐ»ÑŽÑ‡ÐµÐ½
    // Ð¾Ñ‚
    // Ð²Ñ�ÐµÑ…
    // Ð¸Ð³Ñ€Ð¾Ð²Ñ‹Ñ…
    // ÑƒÑ�Ð»ÑƒÐ³
    // Ð·Ð°
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ð½Ð¸Ðµ
    // Ð½ÐµÐ»ÐµÐ³Ð°Ð»ÑŒÐ½Ð¾Ð³Ð¾
    // Ð¿Ñ€Ð¾Ð³Ñ€Ð°Ð¼Ð¼Ð½Ð¾Ð³Ð¾
    // Ð¾Ð±ÐµÑ�Ð¿ÐµÑ‡ÐµÐ½Ð¸Ñ�.
    // \\n
    // Ð—Ð°
    // Ð´Ð¾Ð¿Ð¾Ð»Ð½Ð¸Ñ‚ÐµÐ»ÑŒÐ½Ð¾Ð¹
    // Ð¸Ð½Ñ„Ð¾Ñ€Ð¼Ð°Ñ†Ð¸ÐµÐ¹
    // Ð¾Ð±Ñ€Ð°Ñ‰Ð°Ð¹Ñ‚ÐµÑ�ÑŒ
    // Ð²
    // Ð¡Ð»ÑƒÐ¶Ð±Ñƒ
    // ÐŸÐ¾Ð´Ð´ÐµÑ€Ð¶ÐºÐ¸
    // ÐŸÐ¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÐµÐ»ÐµÐ¹.
    public static final int YOUR_ACCOUNT_HAS_BEEN_SUSPENDED_AT_YOUR_OWN_REQUEST_FOR_MORE_INFORMATION_PLEASE_VISIT_THE = 2123; // Ð’Ð°Ñˆ
    // Ð°ÐºÐºÐ°ÑƒÐ½Ñ‚
    // Ð·Ð°Ð±Ð»Ð¾ÐºÐ¸Ñ€Ð¾Ð²Ð°Ð½
    // Ð¿Ð¾
    // Ð’Ð°ÑˆÐµÐ¼Ñƒ
    // Ñ‚Ñ€ÐµÐ±Ð¾Ð²Ð°Ð½Ð¸ÑŽ.
    // Ð—Ð°
    // Ð´Ð¾Ð¿Ð¾Ð»Ð½Ð¸Ñ‚ÐµÐ»ÑŒÐ½Ð¾Ð¹
    // Ð¸Ð½Ñ„Ð¾Ñ€Ð¼Ð°Ñ†Ð¸ÐµÐ¹
    // Ð¾Ð±Ñ€Ð°Ñ‚Ð¸Ñ‚ÐµÑ�ÑŒ
    // Ð²
    // Ð¡Ð»ÑƒÐ¶Ð±Ñƒ
    // ÐŸÐ¾Ð´Ð´ÐµÑ€Ð¶ÐºÐ¸
    // ÐŸÐ¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÐµÐ»ÐµÐ¹.
    public static final int THE_SERVER_HAS_BEEN_INTEGRATED_AND_YOUR_CLAN_NAME_S1_HAS_BEEN_OVERLAPPED_WITH_ANOTHER_NAME = 2124; // Ð¡ÐµÑ€Ð²ÐµÑ€
    // Ð±Ñ‹Ð»
    // Ð¾Ð±ÑŠÐµÐ´Ð¸Ð½ÐµÐ½,
    // Ð¸
    // Ð¸Ð¼Ñ�
    // Ð²Ð°ÑˆÐµÐ³Ð¾
    // ÐºÐ»Ð°Ð½Ð°,
    // $s1,
    // Ð±Ñ‹Ð»Ð¾
    // Ð·Ð°Ð½Ñ�Ñ‚Ð¾
    // Ð´Ñ€ÑƒÐ³Ð¸Ð¼
    // ÐºÐ»Ð°Ð½Ð¾Ð¼.
    // ÐŸÐ¾Ð¶Ð°Ð»ÑƒÐ¹Ñ�Ñ‚Ð°,
    // Ð²Ð²ÐµÐ´Ð¸Ñ‚Ðµ
    // Ð½Ð¾Ð²Ð¾Ðµ
    // Ð¸Ð¼Ñ�
    // ÐºÐ»Ð°Ð½Ð°.
    public static final int THE_NAME_ALREADY_EXISTS_OR_IS_AN_INVALID_NAME_PLEASE_ENTER_THE_CLAN_NAME_TO_BE_CHANGED = 2125; // Ð˜Ð¼Ñ�
    // ÑƒÐ¶Ðµ
    // Ñ�ÑƒÑ‰ÐµÑ�Ñ‚Ð²ÑƒÐµÑ‚
    // Ð»Ð¸Ð±Ð¾
    // Ð¾Ð½Ð¾
    // Ð½ÐµÐºÐ¾Ñ€Ñ€ÐµÐºÑ‚Ð½Ð¾.
    // ÐŸÐ¾Ð¶Ð°Ð»ÑƒÐ¹Ñ�Ñ‚Ð°,
    // Ð²Ð²ÐµÐ´Ð¸Ñ‚Ðµ
    // Ð´Ñ€ÑƒÐ³Ð¾Ðµ
    // Ð¸Ð¼Ñ�.
    public static final int YOUR_ACCOUNT_HAS_BEEN_SUSPENDED_FOR_REGULARLY_POSTING_ILLEGAL_MESSAGES_FOR_MORE_INFORMATION = 2126; // Ð’Ð°Ñˆ
    // Ð°ÐºÐºÐ°ÑƒÐ½Ñ‚
    // Ð±Ñ‹Ð»
    // Ð·Ð°Ð±Ð»Ð¾ÐºÐ¸Ñ€Ð¾Ð²Ð°Ð½
    // Ð·Ð°
    // Ñ�Ð¸Ñ�Ñ‚ÐµÐ¼Ð°Ñ‚Ð¸Ñ‡ÐµÑ�ÐºÐ¸Ðµ
    // Ð½ÐµÐ»ÐµÐ³Ð°Ð»ÑŒÐ½Ñ‹Ðµ
    // Ñ�Ð¾Ð¾Ð±Ñ‰ÐµÐ½Ð¸Ñ�.
    // Ð—Ð°
    // Ð´Ð¾Ð¿Ð¾Ð»Ð½Ð¸Ñ‚ÐµÐ»ÑŒÐ½Ð¾Ð¹
    // Ð¸Ð½Ñ„Ð¾Ñ€Ð¼Ð°Ñ†Ð¸ÐµÐ¹
    // Ð¾Ð±Ñ€Ð°Ñ‚Ð¸Ñ‚ÐµÑ�ÑŒ
    // Ð²
    // Ð¦ÐµÐ½Ñ‚Ñ€
    // ÐŸÐ¾Ð´Ð´ÐµÑ€Ð¶ÐºÐ¸.
    public static final int YOUR_ACCOUNT_HAS_BEEN_SUSPENDED_AFTER_BEING_DETECTED_WITH_AN_ILLEGAL_MESSAGE_FOR_MORE = 2127; // Ð’Ð°Ñˆ
    // Ð°ÐºÐºÐ°ÑƒÐ½Ñ‚
    // Ð±Ñ‹Ð»
    // Ð·Ð°Ð±Ð»Ð¾ÐºÐ¸Ñ€Ð¾Ð²Ð°Ð½
    // Ð¿Ð¾Ñ�Ð»Ðµ
    // Ð¾Ð±Ð½Ð°Ñ€ÑƒÐ¶ÐµÐ½Ð¸Ñ�
    // Ð½ÐµÐ»ÐµÐ³Ð°Ð»ÑŒÐ½Ð¾Ð³Ð¾
    // Ñ�Ð¾Ð¾Ð±Ñ‰ÐµÐ½Ð¸Ñ�.
    // Ð—Ð°
    // Ð´Ð¾Ð¿Ð¾Ð»Ð½Ð¸Ñ‚ÐµÐ»ÑŒÐ½Ð¾Ð¹
    // Ð¸Ð½Ñ„Ð¾Ñ€Ð¼Ð°Ñ†Ð¸ÐµÐ¹
    // Ð¾Ð±Ñ€Ð°Ñ‰Ð°Ð¹Ñ‚ÐµÑ�ÑŒ
    // Ð²
    // Ð¦ÐµÐ½Ñ‚Ñ€
    // ÐŸÐ¾Ð´Ð´ÐµÑ€Ð¶ÐºÐ¸.
    public static final int YOUR_ACCOUNT_HAS_BEEN_SUSPENDED_FROM_ALL_GAME_SERVICES_FOR_USING_THE_GAME_FOR_COMMERCIAL = 2128; // Ð’Ð°Ñˆ
    // Ð°ÐºÐºÐ°ÑƒÐ½Ñ‚
    // Ð±Ñ‹Ð»
    // Ð¾Ñ‚ÐºÐ»ÑŽÑ‡ÐµÐ½
    // Ð¾Ñ‚
    // Ð²Ñ�ÐµÑ…
    // Ð¸Ð³Ñ€Ð¾Ð²Ñ‹Ñ…
    // ÑƒÑ�Ð»ÑƒÐ³
    // Ð·Ð°
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ð½Ð¸Ðµ
    // Ð¸Ð³Ñ€Ñ‹
    // Ð²
    // ÐºÐ¾Ð¼Ð¼ÐµÑ€Ñ‡ÐµÑ�ÐºÐ¸Ñ…
    // Ñ†ÐµÐ»Ñ�Ñ….
    // Ð—Ð°
    // Ð´Ð¾Ð¿Ð¾Ð»Ð½Ð¸Ñ‚ÐµÐ»ÑŒÐ½Ð¾Ð¹
    // Ð¸Ð½Ñ„Ð¾Ñ€Ð¼Ð°Ñ†Ð¸ÐµÐ¹
    // Ð¾Ð±Ñ€Ð°Ñ‚Ð¸Ñ‚ÐµÑ�ÑŒ
    // Ð²
    // Ñ†ÐµÐ½Ñ‚Ñ€
    // Ð¿Ð¾Ð´Ð´ÐµÑ€Ð¶ÐºÐ¸.
    public static final int THE_AUGMENTED_ITEM_CANNOT_BE_CONVERTED_PLEASE_CONVERT_AFTER_THE_AUGMENTATION_HAS_BEEN_REMOVED = 2129; // Ð£Ð»ÑƒÑ‡ÑˆÐµÐ½Ð½Ñ‹Ð¹
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð¿Ñ€ÐµÐ¾Ð±Ñ€Ð°Ð·Ð¾Ð²Ð°Ð½.
    // ÐŸÐ¾Ð¶Ð°Ð»ÑƒÐ¹Ñ�Ñ‚Ð°,
    // Ð¿Ñ€ÐµÐ¾Ð±Ñ€Ð°Ð·ÑƒÐ¹Ñ‚Ðµ
    // Ð¿Ð¾Ñ�Ð»Ðµ
    // Ñ‚Ð¾Ð³Ð¾,
    // ÐºÐ°Ðº
    // ÑƒÐ»ÑƒÑ‡ÑˆÐµÐ½Ð¸Ðµ
    // Ð±ÑƒÐ´ÐµÑ‚
    // Ñ�Ð½Ñ�Ñ‚Ð¾.
    public static final int YOU_CANNOT_CONVERT_THIS_ITEM = 2130; // Ð’Ñ‹ Ð½Ðµ Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¿Ñ€ÐµÐ¾Ð±Ñ€Ð°Ð·Ð¾Ð²Ð°Ñ‚ÑŒ
    // Ñ�Ñ‚Ð¾Ñ‚
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚.
    public static final int YOU_HAVE_BID_THE_HIGHEST_PRICE_AND_HAVE_WON_THE_ITEM_THE_ITEM_CAN_BE_FOUND_IN_YOUR_PERSONAL = 2131; // Ð’Ñ‹
    // Ð¿Ñ€ÐµÐ´Ð»Ð¾Ð¶Ð¸Ð»Ð¸
    // Ñ�Ð°Ð¼ÑƒÑŽ
    // Ð²Ñ‹Ñ�Ð¾ÐºÑƒÑŽ
    // Ñ�Ñ‚Ð°Ð²ÐºÑƒ
    // Ð¸
    // ÐºÑƒÐ¿Ð¸Ð»Ð¸
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚.
    // ÐžÐ½
    // Ð½Ð°Ñ…Ð¾Ð´Ð¸Ñ‚Ñ�Ñ�
    // Ð²
    // Ð²Ð°ÑˆÐµÐ¹
    // Ð»Ð¸Ñ‡Ð½Ð¾Ð¹
    // Ð¼Ð°Ñ�Ñ‚ÐµÑ€Ñ�ÐºÐ¾Ð¹.
    public static final int YOU_HAVE_ENTERED_A_COMMON_SEVER = 2132; // Ð’Ñ‹ Ð·Ð°ÑˆÐ»Ð¸
    // Ð½Ð° Ð¾Ð±Ñ‰Ð¸Ð¹
    // Ñ�ÐµÑ€Ð²ÐµÑ€.
    public static final int YOU_HAVE_ENTERED_AN_ADULTS_ONLY_SEVER = 2133; // Ð’Ñ‹
    // Ð·Ð°ÑˆÐ»Ð¸
    // Ð½Ð°
    // Ñ�ÐµÑ€Ð²ÐµÑ€
    // Ñ‚Ð¾Ð»ÑŒÐºÐ¾
    // Ð´Ð»Ñ�
    // Ð²Ð·Ñ€Ð¾Ñ�Ð»Ñ‹Ñ….
    public static final int YOU_HAVE_ENTERED_A_SERVER_FOR_JUVENILES = 2134; // Ð’Ñ‹
    // Ð·Ð°ÑˆÐ»Ð¸
    // Ð½Ð°
    // Ð¿Ð¾Ð´Ñ€Ð¾Ñ�Ñ‚ÐºÐ¾Ð²Ñ‹Ð¹
    // Ñ�ÐµÑ€Ð²ÐµÑ€.
    public static final int BECAUSE_OF_YOUR_FATIGUE_LEVEL_THIS_IS_NOT_ALLOWED = 2135; // Ð’Ñ‹
    // Ñ�Ð»Ð¸ÑˆÐºÐ¾Ð¼
    // ÑƒÑ‚Ð¾Ð¼Ð»ÐµÐ½Ñ‹,
    // Ð´ÐµÐ¹Ñ�Ñ‚Ð²Ð¸Ðµ
    // Ð½ÐµÐ´Ð¾Ñ�Ñ‚ÑƒÐ¿Ð½Ð¾.
    public static final int A_CLAN_NAME_CHANGE_APPLICATION_HAS_BEEN_SUBMITTED = 2136; // Ð—Ð°Ñ�Ð²ÐºÐ°
    // Ð½Ð°
    // Ñ�Ð¼ÐµÐ½Ñƒ
    // Ð¸Ð¼ÐµÐ½Ð¸
    // ÐºÐ»Ð°Ð½Ð°
    // Ð¿Ð¾Ð´Ð°Ð½Ð°.
    public static final int YOU_ARE_ABOUT_TO_BID_S1_ITEM_WITH_S2_ADENA_WILL_YOU_CONTINUE = 2137; // Ð’Ñ‹
    // Ð½Ð°Ð¼ÐµÑ€ÐµÐ²Ð°ÐµÑ‚ÐµÑ�ÑŒ
    // Ð¿Ñ€ÐµÐ´Ð»Ð¾Ð¶Ð¸Ñ‚ÑŒ
    // Ð·Ð°
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚
    // $s1
    // Ñ�Ñ‚Ð°Ð²ÐºÑƒ
    // Ð²
    // $s2
    // Ð°Ð´ÐµÐ½.
    // ÐŸÑ€Ð¾Ð´Ð¾Ð»Ð¶Ð¸Ñ‚ÑŒ?
    public static final int PLEASE_ENTER_A_BID_PRICE = 2138; // ÐŸÐ¾Ð¶Ð°Ð»ÑƒÐ¹Ñ�Ñ‚Ð°,
    // Ð²Ð²ÐµÐ´Ð¸Ñ‚Ðµ Ñ�Ð²Ð¾ÑŽ
    // Ñ�Ñ‚Ð°Ð²ÐºÑƒ.
    public static final int C1_S_PET = 2139; // ÐŸÐ¸Ñ‚Ð¾Ð¼ÐµÑ† Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð° $c1.
    public static final int C1_S_SERVITOR = 2140; // Ð¡Ð»ÑƒÐ³Ð° Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð° $c1.
    public static final int YOU_SLIGHTLY_RESISTED_C1_S_MAGIC = 2141; // Ð’Ñ‹
    // Ñ‡Ð°Ñ�Ñ‚Ð¸Ñ‡Ð½Ð¾
    // Ð¾Ñ‚Ñ€Ð°Ð·Ð¸Ð»Ð¸
    // Ð¼Ð°Ð³Ð¸ÑŽ
    // Ñ†ÐµÐ»Ð¸
    // $c1.
    public static final int YOU_CANNOT_EXPEL_C1_BECAUSE_C1_IS_NOT_A_PARTY_MEMBER = 2142; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¸Ñ�ÐºÐ»ÑŽÑ‡Ð¸Ñ‚ÑŒ
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°
    // $c1,
    // Ñ‚Ð°Ðº
    // ÐºÐ°Ðº
    // Ð¾Ð½
    // Ð½Ðµ
    // Ñ�Ð¾Ñ�Ñ‚Ð¾Ð¸Ñ‚
    // Ð²
    // Ð³Ñ€ÑƒÐ¿Ð¿Ðµ.
    public static final int YOUR_OPPONENT_HAS_RESISTANCE_TO_MAGIC_THE_DAMAGE_WAS_DECREASED = 2151; // Ð£
    // Ð’Ð°ÑˆÐµÐ³Ð¾
    // Ð¿Ñ€Ð¾Ñ‚Ð¸Ð²Ð½Ð¸ÐºÐ°
    // ÐµÑ�Ñ‚ÑŒ
    // Ñ�Ð¾Ð¿Ñ€Ð¾Ñ‚Ð¸Ð²Ð»ÐµÐ½Ð¸Ðµ
    // Ðº
    // Ð¼Ð°Ð³Ð¸Ð¸,
    // ÑƒÑ€Ð¾Ð½
    // Ð±Ñ‹Ð»
    // ÑƒÐ¼ÐµÐ½ÑŒÑˆÐµÐ½.
    public static final int THE_ASSIGNED_SHORTCUT_WILL_BE_DELETED_AND_THE_INITIAL_SHORTCUT_SETTING_RESTORED_WILL_YOU = 2152; // Ð�Ð°Ð·Ð½Ð°Ñ‡ÐµÐ½Ð½Ñ‹Ðµ
    // ÑƒÑ�Ñ‚Ð°Ð½Ð¾Ð²ÐºÐ¸
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð²
    // Ð±ÑƒÐ´ÑƒÑ‚
    // ÑƒÐ´Ð°Ð»ÐµÐ½Ñ‹,
    // Ð°
    // Ð¸Ñ�Ñ…Ð¾Ð´Ð½Ñ‹Ðµ
    // Ð²Ð¾Ñ�Ñ�Ñ‚Ð°Ð½Ð¾Ð²Ð»ÐµÐ½Ñ‹.
    // ÐŸÑ€Ð¾Ð´Ð¾Ð»Ð¶Ð¸Ñ‚ÑŒ?
    public static final int YOU_ARE_CURRENTLY_LOGGED_INTO_10_OF_YOUR_ACCOUNTS_AND_CAN_NO_LONGER_ACCESS_YOUR_OTHER_ACCOUNTS = 2153; // Ð’Ñ‹
    // ÑƒÐ¶Ðµ
    // Ð·Ð°Ñ€ÐµÐ³Ð¸Ñ�Ñ‚Ñ€Ð¸Ñ€Ð¾Ð²Ð°Ð»Ð¸
    // 10
    // Ð°ÐºÐºÐ°ÑƒÐ½Ñ‚Ð¾Ð²
    // Ð¸
    // Ð±Ð¾Ð»ÑŒÑˆÐµ
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ñ€ÐµÐ³Ð¸Ñ�Ñ‚Ñ€Ð¸Ñ€Ð¾Ð²Ð°Ñ‚ÑŒ
    // Ð°ÐºÐºÐ°ÑƒÐ½Ñ‚Ñ‹.
    public static final int THE_TARGET_IS_NOT_A_FLAGPOLE_SO_A_FLAG_CANNOT_BE_DISPLAYED = 2154; // Ð¦ÐµÐ»ÑŒ
    // Ð½Ðµ
    // Ñ�Ð²Ð»Ñ�ÐµÑ‚Ñ�Ñ�
    // Ñ„Ð»Ð°Ð³ÑˆÑ‚Ð¾ÐºÐ¾Ð¼
    // Ð¸
    // Ñ„Ð»Ð°Ð³
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð¿Ð¾Ð´Ð½Ñ�Ñ‚.
    public static final int A_FLAG_IS_ALREADY_BEING_DISPLAYED_ANOTHER_FLAG_CANNOT_BE_DISPLAYED = 2155; // Ð¤Ð»Ð°Ð³
    // ÑƒÐ¶Ðµ
    // Ð±Ñ‹Ð»
    // Ð¿Ð¾Ð´Ð½Ñ�Ñ‚.
    // Ð”Ñ€ÑƒÐ³Ð¾Ð¹
    // Ñ„Ð»Ð°Ð³
    // Ð¿Ð¾Ð´Ð½Ñ�Ñ‚ÑŒ
    // Ð½ÐµÐ»ÑŒÐ·Ñ�.
    public static final int THERE_ARE_NOT_ENOUGH_NECESSARY_ITEMS_TO_USE_THE_SKILL = 2156; // Ð�ÐµÑ‚
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ð¾Ð²,
    // Ð½ÐµÐ¾Ð±Ñ…Ð¾Ð´Ð¸Ð¼Ñ‹Ñ…
    // Ð´Ð»Ñ�
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ð½Ð¸Ñ�
    // Ñ�Ñ‚Ð¾Ð³Ð¾
    // Ð½Ð°Ð²Ñ‹ÐºÐ°.
    public static final int BID_WILL_BE_ATTEMPTED_WITH_S1_ADENA = 2157; // Ð£Ñ�Ñ‚Ð°Ð½Ð¾Ð²Ð»ÐµÐ½Ð°
    // Ñ†ÐµÐ½Ð°:
    // $s1
    // Ð°Ð´ÐµÐ½.
    public static final int FORCE_ATTACK_IS_IMPOSSIBLE_AGAINST_A_TEMPORARY_ALLIED_MEMBER_DURING_A_SIEGE = 2158; // Ð¡Ð¸Ð»Ð¾Ð²Ð°Ñ�
    // Ð°Ñ‚Ð°ÐºÐ°
    // Ð½ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð°
    // Ð¿Ñ€Ð¾Ñ‚Ð¸Ð²
    // Ð²Ñ€ÐµÐ¼ÐµÐ½Ð½Ñ‹Ñ…
    // Ñ�Ð¾ÑŽÐ·Ð½Ð¸ÐºÐ¾Ð²
    // Ð¾Ñ�Ð°Ð¶Ð´Ð°ÑŽÑ‰ÐµÐ¹
    // Ñ�Ñ‚Ð¾Ñ€Ð¾Ð½Ñ‹.
    public static final int THE_BARRACKS_HAVE_BEEN_SEIZED = 2164; // ÐšÐ°Ð·Ð°Ñ€Ð¼Ñ‹
    // Ð±Ñ‹Ð»Ð¸
    // Ð·Ð°Ñ…Ð²Ð°Ñ‡ÐµÐ½Ñ‹.
    public static final int THE_BARRACKS_FUNCTION_HAS_BEEN_RESTORED = 2165; // Ð¤ÑƒÐ½ÐºÑ†Ð¸Ð¸
    // ÐºÐ°Ð·Ð°Ñ€Ð¼
    // Ð²Ð¾Ñ�Ñ�Ñ‚Ð°Ð½Ð¾Ð²Ð»ÐµÐ½Ñ‹.
    public static final int ALL_BARRACKS_ARE_OCCUPIED = 2166; // ÐšÐ°Ð·Ð°Ñ€Ð¼Ñ‹ Ð±Ñ‹Ð»Ð¸
    // Ð¾ÐºÐºÑƒÐ¿Ð¸Ñ€Ð¾Ð²Ð°Ð½Ñ‹.
    public static final int C1_HAS_ACQUIRED_THE_FLAG = 2168; // $c1 Ð·Ð°Ñ…Ð²Ð°Ñ‚Ñ‹Ð²Ð°ÐµÑ‚
    // Ñ„Ð»Ð°Ð³.
    public static final int A_MALICIOUS_SKILL_CANNOT_BE_USED_WHEN_AN_OPPONENT_IS_IN_THE_PEACE_ZONE = 2170; // Ð’Ñ€ÐµÐ´Ð¾Ð½Ð¾Ñ�Ð½Ð¾Ðµ
    // ÑƒÐ¼ÐµÐ½Ð¸Ðµ
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ð½Ð¾,
    // ÐºÐ¾Ð³Ð´Ð°
    // Ð¿Ñ€Ð¾Ñ‚Ð¸Ð²Ð½Ð¸Ðº
    // Ð½Ð°Ñ…Ð¾Ð´Ð¸Ñ‚Ñ�Ñ�
    // Ð²
    // Ð¼Ð¸Ñ€Ð½Ð¾Ð¹
    // Ð·Ð¾Ð½Ðµ.
    public static final int THIS_ITEM_CANNOT_BE_CRYSTALIZED = 2171; // Ð­Ñ‚Ð¾Ñ‚
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚
    // Ð½Ðµ Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // ÐºÑ€Ð¸Ñ�Ñ‚Ð°Ð»Ð»Ð¸Ð·Ð¾Ð²Ð°Ð½.
    public static final int S1_S_ELEMENTAL_POWER_HAS_BEEN_REMOVED = 2176; // $s1:
    // Ñ�Ð¸Ð»Ð°
    // Ñ�Ñ‚Ð¸Ñ…Ð¸Ð¸
    // Ð´ÐµÐ°ÐºÑ‚Ð¸Ð²Ð¸Ñ€Ð¾Ð²Ð°Ð½Ð°.
    public static final int _S1S2_S_ELEMENTAL_POWER_HAS_BEEN_REMOVED = 2177; // +$s1
    // $s2:
    // Ñ�Ð¸Ð»Ð°
    // Ñ�Ñ‚Ð¸Ñ…Ð¸Ð¸
    // Ð´ÐµÐ°ÐºÑ‚Ð¸Ð²Ð¸Ñ€Ð¾Ð²Ð°Ð½Ð°.
    public static final int YOU_FAILED_TO_REMOVE_THE_ELEMENTAL_POWER = 2178; // Ð£Ð±Ñ€Ð°Ñ‚ÑŒ
    // Ñ�Ð¸Ð»Ñƒ
    // Ñ�Ñ‚Ð¸Ñ…Ð¸Ð¸
    // Ð½Ðµ
    // ÑƒÐ´Ð°Ð»Ð¾Ñ�ÑŒ.
    public static final int YOU_HAVE_THE_HIGHEST_BID_SUBMITTED_IN_A_GIRAN_CASTLE_AUCTION = 2179; // Ð’Ñ‹
    // Ð¿Ñ€ÐµÐ´Ð»Ð¾Ð¶Ð¸Ð»Ð¸
    // Ñ�Ð°Ð¼ÑƒÑŽ
    // Ð²Ñ‹Ñ�Ð¾ÐºÑƒÑŽ
    // Ñ�Ñ‚Ð°Ð²ÐºÑƒ
    // Ð½Ð°
    // Ð°ÑƒÐºÑ†Ð¸Ð¾Ð½Ðµ
    // Ð·Ð°Ð¼ÐºÐ°
    // Ð“Ð¸Ñ€Ð°Ð½.
    public static final int YOU_HAVE_THE_HIGHEST_BID_SUBMITTED_IN_AN_ADEN_CASTLE_AUCTION = 2180; // Ð’Ñ‹
    // Ð¿Ñ€ÐµÐ´Ð»Ð¾Ð¶Ð¸Ð»Ð¸
    // Ñ�Ð°Ð¼ÑƒÑŽ
    // Ð²Ñ‹Ñ�Ð¾ÐºÑƒÑŽ
    // Ñ�Ñ‚Ð°Ð²ÐºÑƒ
    // Ð½Ð°
    // Ð°ÑƒÐºÑ†Ð¸Ð¾Ð½Ðµ
    // Ð·Ð°Ð¼ÐºÐ°
    // Ð�Ð´ÐµÐ½.
    public static final int YOU_HAVE_HIGHEST_THE_BID_SUBMITTED_IN_A_RUNE_CASTLE_AUCTION = 2181; // Ð’Ñ‹
    // Ð¿Ñ€ÐµÐ´Ð»Ð¾Ð¶Ð¸Ð»Ð¸
    // Ñ�Ð°Ð¼ÑƒÑŽ
    // Ð²Ñ‹Ñ�Ð¾ÐºÑƒÑŽ
    // Ñ�Ñ‚Ð°Ð²ÐºÑƒ
    // Ð½Ð°
    // Ð°ÑƒÐºÑ†Ð¸Ð¾Ð½Ðµ
    // Ð·Ð°Ð¼ÐºÐ°
    // Ð ÑƒÐ½Ð°.
    public static final int THE_TARGET_IS_LOCATED_WHERE_YOU_CANNOT_CHARGE = 2187; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð°Ñ‚Ð°ÐºÐ¾Ð²Ð°Ñ‚ÑŒ
    // Ñ†ÐµÐ»ÑŒ.
    public static final int ANOTHER_ENCHANTMENT_IS_IN_PROGRESS_PLEASE_COMPLETE_PREVIOUS_TASK_AND_TRY_AGAIN = 2188; // Ð”Ñ€ÑƒÐ³Ð¾Ðµ
    // ÑƒÐ»ÑƒÑ‡ÑˆÐµÐ½Ð¸Ðµ
    // Ð²
    // Ð¿Ñ€Ð¾Ñ†ÐµÑ�Ñ�Ðµ.
    // ÐŸÐ¾Ð¶Ð°Ð»ÑƒÐ¹Ñ�Ñ‚Ð°,
    // Ð·Ð°Ð²ÐµÑ€ÑˆÐ¸Ñ‚Ðµ
    // Ñ‚ÐµÐºÑƒÑ‰ÑƒÑŽ
    // Ð·Ð°Ð´Ð°Ñ‡Ñƒ
    // Ð¸
    // Ð¿Ð¾Ð¿Ñ€Ð¾Ð±ÑƒÐ¹Ñ‚Ðµ
    // Ñ�Ð½Ð¾Ð²Ð°.
    public static final int TO_APPLY_SELECTED_OPTIONS_THE_GAME_NEEDS_TO_BE_RELOADED_IF_YOU_DON_T_APPLY_NOW_IT_WILL_BE = 2191; // Ð”Ð»Ñ�
    // Ð¿Ñ€Ð¸Ð¼ÐµÐ½ÐµÐ½Ð¸Ñ�
    // Ð²Ñ‹Ð±Ñ€Ð°Ð½Ð½Ñ‹Ñ…
    // Ð½Ð°Ñ�Ñ‚Ñ€Ð¾ÐµÐº
    // Ð¸Ð³Ñ€Ð°
    // Ð´Ð¾Ð»Ð¶Ð½Ð°
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð¿ÐµÑ€ÐµÐ·Ð°Ð³Ñ€ÑƒÐ¶ÐµÐ½Ð°.
    // Ð•Ñ�Ð»Ð¸
    // Ð½Ðµ
    // Ð¿Ñ€Ð¸Ð¼ÐµÐ½Ð¸Ñ‚ÑŒ
    // Ñ�ÐµÐ¹Ñ‡Ð°Ñ�,
    // Ñ‚Ð¾
    // Ð½Ð°Ñ�Ñ‚Ñ€Ð¾Ð¹ÐºÐ¸
    // Ð¿Ñ€Ð¸Ð¼ÐµÐ½Ñ�Ñ‚Ñ�Ñ�
    // Ð¿Ñ€Ð¸
    // Ñ�Ð»ÐµÐ´ÑƒÑŽÑ‰ÐµÐ¼
    // Ð²Ñ…Ð¾Ð´Ðµ
    // Ð²
    // Ð¸Ð³Ñ€Ñƒ.
    // ÐŸÑ€Ð¸Ð¼ÐµÐ½Ð¸Ñ‚ÑŒ
    // Ñ�ÐµÐ¹Ñ‡Ð°Ñ�?
    public static final int YOU_HAVE_BID_ON_AN_ITEM_AUCTION = 2192; // Ð’Ñ‹
    // Ñ�Ð´ÐµÐ»Ð°Ð»Ð¸
    // Ñ�Ñ‚Ð°Ð²ÐºÑƒ Ð½Ð°
    // Ð°ÑƒÐºÑ†Ð¸Ð¾Ð½Ðµ
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ð¾Ð².
    public static final int NO_OWNED_CLAN = 2196; // Ð�ÐµÑ‚ ÐºÐ»Ð°Ð½Ð°.
    public static final int OWNED_BY_CLAN_S1 = 2197; // Ð•Ñ�Ñ‚ÑŒ ÐºÐ»Ð°Ð½ $s1.
    public static final int YOU_HAVE_THE_HIGHEST_BID_IN_AN_ITEM_AUCTION = 2198; // Ð£
    // Ð²Ð°Ñ�
    // Ñ�Ð°Ð¼Ð°Ñ�
    // Ð²Ñ‹Ñ�Ð¾ÐºÐ°Ñ�
    // Ñ�Ñ‚Ð°Ð²ÐºÐ°
    // Ð½Ð°
    // Ð°ÑƒÐºÑ†Ð¸Ð¾Ð½Ðµ
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ð¾Ð².
    public static final int YOU_CANNOT_ENTER_THIS_INSTANCE_ZONE_WHILE_THE_NPC_SERVER_IS_UNAVAILABLE = 2199; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð²Ð¾Ð¹Ñ‚Ð¸
    // Ð²Ð¾
    // Ð²Ñ€ÐµÐ¼ÐµÐ½Ð½ÑƒÑŽ
    // Ð·Ð¾Ð½Ñƒ,
    // Ð¿Ð¾ÐºÐ°
    // ÑƒÑ�Ð»ÑƒÐ³Ð°
    // NPC
    // Ð½ÐµÐ´Ð¾Ñ�Ñ‚ÑƒÐ¿Ð½Ð°.
    public static final int THIS_INSTANCE_ZONE_WILL_BE_TERMINATED_BECAUSE_THE_NPC_SERVER_IS_UNAVAILABLE_YOU_WILL_BE_FORCIBLY = 2200; // Ð­Ñ‚Ð°
    // Ð²Ñ€ÐµÐ¼ÐµÐ½Ð½Ð°Ñ�
    // Ð·Ð¾Ð½Ð°
    // Ð·Ð°ÐºÑ€Ñ‹Ñ‚Ð°,
    // Ð¿Ð¾Ñ‚Ð¾Ð¼Ñƒ
    // Ñ‡Ñ‚Ð¾
    // Ñ�ÐµÑ€Ð²ÐµÑ€
    // NPC
    // Ð½Ðµ
    // Ñ€Ð°Ð±Ð¾Ñ‚Ð°ÐµÑ‚.
    // Ð¡ÐµÐ¹Ñ‡Ð°Ñ�
    // Ð²Ñ‹
    // Ð±ÑƒÐ´ÐµÑ‚Ðµ
    // Ð¿Ñ€Ð¸Ð½ÑƒÐ´Ð¸Ñ‚ÐµÐ»ÑŒÐ½Ð¾
    // Ð²Ñ‹Ð±Ñ€Ð¾ÑˆÐµÐ½Ñ‹
    // Ð¸Ð·
    // Ð¿Ð¾Ð´Ð·ÐµÐ¼ÐµÐ»ÑŒÑ�.
    public static final int S1YEARS_S2MONTHS_S3DAYS = 2201; // $s1 Ð³. $s2 Ð¼ÐµÑ�.
    // $s3 Ð´Ð½.
    public static final int S1HOURS_S2MINUTES_S3_SECONDS = 2202; // $s1 Ñ‡ $s2
    // Ð¼Ð¸Ð½ $s3 Ñ�ÐµÐº
    public static final int S1_M_S2_D = 2203; // $s1 Ð¼ÐµÑ�. $s2 Ð´Ð½.
    public static final int S1HOURS = 2204; // $s1 Ñ‡
    public static final int YOU_HAVE_ENTERED_AN_AREA_WHERE_THE_MINI_MAP_CANNOT_BE_USED_THE_MINI_MAP_WILL_BE_CLOSED = 2205; // Ð’Ñ‹
    // Ð²Ð¾ÑˆÐ»Ð¸
    // Ð²
    // Ð·Ð¾Ð½Ñƒ,
    // Ð³Ð´Ðµ
    // Ð¼Ð¸Ð½Ð¸ÐºÐ°Ñ€Ñ‚Ð°
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ð½Ð°.
    // ÐœÐ¸Ð½Ð¸ÐºÐ°Ñ€Ñ‚Ð°
    // Ð±ÑƒÐ´ÐµÑ‚
    // Ð·Ð°ÐºÑ€Ñ‹Ñ‚Ð°.
    public static final int YOU_HAVE_ENTERED_AN_AREA_WHERE_THE_MINI_MAP_CAN_BE_USED = 2206; // Ð’Ñ‹
    // Ð²Ð¾ÑˆÐ»Ð¸
    // Ð²
    // Ð·Ð¾Ð½Ñƒ,
    // Ð³Ð´Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ð½Ð°
    // Ð¼Ð¸Ð½Ð¸ÐºÐ°Ñ€Ñ‚Ð°.
    public static final int THIS_IS_AN_AREA_WHERE_YOU_CANNOT_USE_THE_MINI_MAP_THE_MINI_MAP_WILL_NOT_BE_OPENED = 2207; // Ð’
    // Ñ�Ñ‚Ð¾Ð¹
    // Ð·Ð¾Ð½Ðµ
    // Ð¼Ð¸Ð½Ð¸ÐºÐ°Ñ€Ñ‚Ð°
    // Ð½Ðµ
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·ÑƒÐµÑ‚Ñ�Ñ�
    // Ð¸
    // Ð½Ðµ
    // Ð¾Ñ‚ÐºÑ€Ñ‹Ð²Ð°ÐµÑ‚Ñ�Ñ�.
    public static final int YOU_DO_NOT_MEET_THE_SKILL_LEVEL_REQUIREMENTS = 2208; // Ð’Ð°Ñˆ
    // ÑƒÑ€Ð¾Ð²ÐµÐ½ÑŒ
    // Ð½Ðµ
    // Ñ�Ð¾Ð¾Ñ‚Ð²ÐµÑ‚Ñ�Ñ‚Ð²ÑƒÐµÑ‚
    // Ñ‚Ñ€ÐµÐ±Ð¾Ð²Ð°Ð½Ð¸Ñ�Ð¼.
    public static final int THIS_IS_AN_AREA_WHERE_RADAR_CANNOT_BE_USED = 2209; // Ð’
    // Ñ�Ñ‚Ð¾Ð¹
    // Ð·Ð¾Ð½Ðµ
    // Ñ€Ð°Ð´Ð°Ñ€
    // Ð½Ðµ
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·ÑƒÐµÑ‚Ñ�Ñ�.
    public static final int IT_WILL_RETURN_TO_AN_UNENCHANTED_CONDITION = 2210; // Ð’Ñ‹
    // Ð²ÐµÑ€Ð½ÑƒÐ»Ð¸Ñ�ÑŒ
    // Ð²
    // Ð½ÐµÑƒÐ»ÑƒÑ‡ÑˆÐµÐ½Ð½Ð¾Ðµ
    // Ñ�Ð¾Ñ�Ñ‚Ð¾Ñ�Ð½Ð¸Ðµ.
    public static final int YOU_MUST_LEARN_A_GOOD_DEED_SKILL_BEFORE_YOU_CAN_ACQUIRE_NEW_SKILLS = 2211; // Ð’Ñ‹
    // Ð´Ð¾Ð»Ð¶Ð½Ñ‹
    // Ð²Ñ‹ÑƒÑ‡Ð¸Ñ‚ÑŒ
    // Ð±Ð»Ð°Ð³Ð¾Ñ‚Ð²Ð¾Ñ€Ð½Ð¾Ðµ
    // ÑƒÐ¼ÐµÐ½Ð¸Ðµ,
    // Ð¿Ñ€ÐµÐ¶Ð´Ðµ
    // Ñ‡ÐµÐ¼
    // Ñ�Ñ‚Ð°Ð½ÑƒÑ‚
    // Ð´Ð¾Ñ�Ñ‚ÑƒÐ¿Ð½Ñ‹
    // Ð´Ñ€ÑƒÐ³Ð¸Ðµ
    // ÑƒÐ¼ÐµÐ½Ð¸Ñ�.
    public static final int YOU_HAVE_NOT_COMPLETED_THE_NECESSARY_QUEST_FOR_SKILL_ACQUISITION = 2212; // Ð£Ð¼ÐµÐ½Ð¸Ðµ
    // Ð½ÐµÐ´Ð¾Ñ�Ñ‚ÑƒÐ¿Ð½Ð¾,
    // Ð²Ñ‹
    // Ð½Ðµ
    // Ð²Ñ‹Ð¿Ð¾Ð»Ð½Ð¸Ð»Ð¸
    // Ð½ÐµÐ¾Ð±Ñ…Ð¾Ð´Ð¸Ð¼Ñ‹Ð¹
    // ÐºÐ²ÐµÑ�Ñ‚.
    public static final int A_NEW_CHARACTER_WILL_BE_CREATED_WITH_THE_CURRENT_SETTINGS_CONTINUE = 2214; // Ð�Ð¾Ð²Ñ‹Ð¹
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶
    // Ð±ÑƒÐ´ÐµÑ‚
    // Ñ�Ð¾Ð·Ð´Ð°Ð½
    // Ñ�
    // Ñ‚ÐµÐºÑƒÑ‰Ð¸Ð¼Ð¸
    // Ð½Ð°Ñ�Ñ‚Ñ€Ð¾Ð¹ÐºÐ°Ð¼Ð¸.
    // ÐŸÑ€Ð¾Ð´Ð¾Ð»Ð¶Ð¸Ñ‚ÑŒ?
    public static final int S1P_DEF = 2215; // Ð¤Ð¸Ð·. Ð—Ð°Ñ‰.: $s1
    public static final int THE_CPU_DRIVER_IS_NOT_UP_TO_DATE_PLEASE_INSTALL_AN_UP_TO_DATE_CPU_DRIVER = 2216; // Ð�Ðµ
    // Ð¾Ð±Ð½Ð¾Ð²Ð»ÐµÐ½
    // CPU
    // Ð´Ñ€Ð°Ð¹Ð²ÐµÑ€.
    // ÐŸÐ¾Ð¶Ð°Ð»ÑƒÐ¹Ñ�Ñ‚Ð°,
    // ÑƒÑ�Ñ‚Ð°Ð½Ð¾Ð²Ð¸Ñ‚Ðµ
    // Ð¾Ð±Ð½Ð¾Ð²Ð»ÐµÐ½Ð¸Ðµ.
    public static final int THE_BALLISTA_HAS_BEEN_SUCCESSFULLY_DESTROYED_AND_THE_CLAN_S_REPUTATION_WILL_BE_INCREASED = 2217; // Ð‘Ð°Ð»Ð»Ð¸Ñ�Ñ‚Ð°
    // Ð±Ñ‹Ð»Ð°
    // ÑƒÑ�Ð¿ÐµÑˆÐ½Ð¾
    // ÑƒÐ½Ð¸Ñ‡Ñ‚Ð¾Ð¶ÐµÐ½Ð°.
    // ÐšÐ»Ð°Ð½Ð¾Ð²Ð°Ñ�
    // Ñ€ÐµÐ¿ÑƒÑ‚Ð°Ñ†Ð¸Ñ�
    // Ð²Ð¾Ð·Ñ€Ð¾Ñ�Ð»Ð°.
    public static final int THIS_IS_A_MAIN_CLASS_SKILL_ONLY = 2218; // Ð­Ñ‚Ð¾
    // ÑƒÐ¼ÐµÐ½Ð¸Ðµ
    // Ñ‚Ð¾Ð»ÑŒÐºÐ¾
    // Ð´Ð»Ñ�
    // Ð¾Ñ�Ð½Ð¾Ð²Ð½Ð¾Ð³Ð¾
    // ÐºÐ»Ð°Ñ�Ñ�Ð°.
    public static final int THIS_LOWER_CLAN_SKILL_HAS_ALREADY_BEEN_ACQUIRED = 2219; // Ð�Ð¸Ð·ÑˆÐ¸Ðµ
    // ÐºÐ»Ð°Ð½Ð¾Ð²Ñ‹Ðµ
    // ÑƒÐ¼ÐµÐ½Ð¸Ñ�
    // ÑƒÐ¶Ðµ
    // Ð´Ð¾Ñ�Ñ‚ÑƒÐ¿Ð½Ñ‹.
    public static final int THE_PREVIOUS_LEVEL_SKILL_HAS_NOT_BEEN_LEARNED = 2220; // ÐŸÑ€ÐµÐ´Ñ‹Ð´ÑƒÑ‰ÐµÐµ
    // ÑƒÐ¼ÐµÐ½Ð¸Ðµ
    // Ð½Ðµ
    // Ð±Ñ‹Ð»Ð¾
    // Ð²Ñ‹ÑƒÑ‡ÐµÐ½Ð¾.
    public static final int WILL_YOU_ACTIVATE_THE_SELECTED_FUNCTIONS = 2221; // Ð¥Ð¾Ñ‚Ð¸Ñ‚Ðµ
    // Ð°ÐºÑ‚Ð¸Ð²Ð¸Ñ€Ð¾Ð²Ð°Ñ‚ÑŒ
    // Ð²Ñ‹Ð±Ñ€Ð°Ð½Ð½Ñ‹Ðµ
    // Ñ„ÑƒÐ½ÐºÑ†Ð¸Ð¸?
    public static final int IT_WILL_COST_150000_ADENA_TO_PLACE_SCOUTS_WILL_YOU_PLACE_THEM = 2222; // Ð Ð°Ð·Ð¼ÐµÑ�Ñ‚Ð¸Ñ‚ÑŒ
    // Ñ€Ð°Ð·Ð²ÐµÐ´Ñ‡Ð¸ÐºÐ¾Ð²
    // Ñ�Ñ‚Ð¾Ð¸Ñ‚
    // 150
    // 000
    // Ð°Ð´ÐµÐ½.
    // Ð Ð°Ð·Ð¼ÐµÑ�Ñ‚Ð¸Ñ‚ÑŒ?
    public static final int IT_WILL_COST_200000_ADENA_FOR_A_FORTRESS_GATE_ENHANCEMENT_WILL_YOU_ENHANCE_IT = 2223; // Ð£Ð»ÑƒÑ‡ÑˆÐµÐ½Ð¸Ðµ
    // Ð²Ñ€Ð°Ñ‚
    // ÐºÑ€ÐµÐ¿Ð¾Ñ�Ñ‚Ð¸
    // Ð±ÑƒÐ´ÐµÑ‚
    // Ñ�Ñ‚Ð¾Ð¸Ñ‚ÑŒ
    // 200
    // 000
    // Ð°Ð´ÐµÐ½.
    // Ð£Ð»ÑƒÑ‡ÑˆÐ¸Ñ‚ÑŒ?
    public static final int CROSSBOW_IS_PREPARING_TO_FIRE = 2224; // Ð�Ñ€Ð±Ð°Ð»ÐµÑ‚
    // Ð³Ð¾Ñ‚Ð¾Ð²Ð¸Ñ‚Ñ�Ñ� Ðº
    // Ñ�Ñ‚Ñ€ÐµÐ»ÑŒÐ±Ðµ.
    public static final int THERE_ARE_NO_OTHER_SKILLS_TO_LEARN_PLEASE_COME_BACK_AFTER_S1ND_CLASS_CHANGE = 2225; // Ð�ÐµÑ‚
    // Ð´Ñ€ÑƒÐ³Ð¸Ñ…
    // ÑƒÐ¼ÐµÐ½Ð¸Ð¹
    // Ð´Ð»Ñ�
    // Ð¸Ð·ÑƒÑ‡ÐµÐ½Ð¸Ñ�.
    // Ð’Ð¾Ð·Ð²Ñ€Ð°Ñ‰Ð°Ð¹Ñ‚ÐµÑ�ÑŒ
    // Ð¿Ð¾Ñ�Ð»Ðµ
    // $s1-Ð¹
    // Ñ�Ð¼ÐµÐ½Ñ‹
    // ÐºÐ»Ð°Ñ�Ñ�Ð°.
    public static final int IT_IS_NOT_POSSIBLE_TO_REGISTER_FOR_THE_CASTLE_SIEGE_SIDE_OR_CASTLE_SIEGE_OF_A_HIGHER_CASTLE_IN = 2227; // Ð�ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾
    // Ð·Ð°Ñ€ÐµÐ³Ð¸Ñ�Ñ‚Ñ€Ð¸Ñ€Ð¾Ð²Ð°Ñ‚ÑŒÑ�Ñ�
    // Ð½Ð°
    // Ð¾Ñ�Ð°Ð´Ñƒ
    // Ð·Ð°Ð¼ÐºÐ°
    // Ð¸
    // Ð¾Ñ�Ð°Ð´Ñƒ
    // Ð²Ñ‹Ñ�ÑˆÐµÐ³Ð¾
    // Ð·Ð°Ð¼ÐºÐ°
    // Ð²
    // ÐºÐ¾Ð½Ñ‚Ñ€Ð°ÐºÑ‚Ðµ.
    public static final int THE_SUPPLY_ITEMS_HAVE_NOT_NOT_BEEN_PROVIDED_BECAUSE_THE_HIGHER_CASTLE_IN_CONTRACT_DOESN_T_HAVE = 2231; // ÐŸÑ€ÐµÐ´Ð¼ÐµÑ‚Ñ‹
    // Ð¿Ð¾Ð´Ð´ÐµÑ€Ð¶ÐºÐ¸
    // Ð½Ðµ
    // Ð´Ð¾Ð¿ÑƒÑ�ÐºÐ°ÑŽÑ‚Ñ�Ñ�,
    // Ð¿Ð¾Ñ‚Ð¾Ð¼Ñƒ
    // Ñ‡Ñ‚Ð¾
    // Ñƒ
    // Ð²Ñ‹Ñ�ÑˆÐµÐ³Ð¾
    // Ð·Ð°Ð¼ÐºÐ°
    // Ð²
    // ÐºÐ¾Ð½Ñ‚Ñ€Ð°ÐºÑ‚Ðµ
    // Ð½ÐµÐ´Ð¾Ñ�Ñ‚Ð°Ñ‚Ð¾Ñ‡Ð½Ð¾
    // Ð¾Ñ‡ÐºÐ¾Ð²
    // Ñ€ÐµÐ¿ÑƒÑ‚Ð°Ñ†Ð¸Ð¸.
    public static final int S1_WILL_BE_CRYSTALIZED_BEFORE_DESTRUCTION_WILL_YOU_CONTINUE = 2232; // $s1
    // Ð±ÑƒÐ´ÐµÑ‚
    // ÐºÑ€Ð¸Ñ�Ñ‚Ð°Ð»Ð»Ð¸Ð·Ð¾Ð²Ð°Ð½
    // Ð¿ÐµÑ€ÐµÐ´
    // Ñ€Ð°Ð·Ñ€ÑƒÑˆÐµÐ½Ð¸ÐµÐ¼.
    // ÐŸÑ€Ð¾Ð´Ð¾Ð»Ð¶Ð¸Ñ‚ÑŒ?
    public static final int SIEGE_REGISTRATION_IS_NOT_POSSIBLE_DUE_TO_A_CONTRACT_WITH_A_HIGHER_CASTLE = 2233; // Ð ÐµÐ³Ð¸Ñ�Ñ‚Ñ€Ð°Ñ†Ð¸Ñ�
    // Ð½Ð°
    // Ð¾Ñ�Ð°Ð´Ñƒ
    // Ð½ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð°
    // Ð¸Ð·-Ð·Ð°
    // ÐºÐ¾Ð½Ñ‚Ñ€Ð°ÐºÑ‚Ð°
    // Ñ�
    // Ð²Ñ‹Ñ�ÑˆÐ¸Ð¼
    // Ð·Ð°Ð¼ÐºÐ¾Ð¼.
    public static final int WILL_YOU_USE_THE_SELECTED_KAMAEL_RACE_ONLY_HERO_WEAPON = 2234; // Ð’Ñ‹
    // Ð±ÑƒÐ´ÐµÑ‚Ðµ
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒ
    // Ð²Ñ‹Ð±Ñ€Ð°Ð½Ð½Ð¾Ðµ
    // Ð“ÐµÑ€Ð¾Ð¸Ñ‡ÐµÑ�ÐºÐ¾Ðµ
    // ÐžÑ€ÑƒÐ¶Ð¸Ðµ,
    // Ð¿Ð¾Ð´Ñ…Ð¾Ð´Ñ�Ñ‰ÐµÐµ
    // Ñ‚Ð¾Ð»ÑŒÐºÐ¾
    // Ñ€Ð°Ñ�Ðµ
    // ÐšÐ°Ð¼Ð°Ñ�Ð»ÑŒ?
    public static final int THE_INSTANCE_ZONE_IN_USE_HAS_BEEN_DELETED_AND_CANNOT_BE_ACCESSED = 2235; // Ð’Ñ€ÐµÐ¼ÐµÐ½Ð½Ð°Ñ�
    // Ð·Ð¾Ð½Ð°
    // Ð±Ñ‹Ð»Ð°
    // ÑƒÐ´Ð°Ð»ÐµÐ½Ð°
    // Ð¸
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð²Ñ‹Ð±Ñ€Ð°Ð½Ð°.
    public static final int S1_MINUTES_LEFT_FOR_WYVERN_RIDING = 2236; // Ð”Ð¾
    // Ð³Ð¾Ð½ÐºÐ¸
    // Ð½Ð°
    // Ð²Ð¸Ð²ÐµÑ€Ð½Ð°Ñ…
    // Ð¾Ñ�Ñ‚Ð°Ð»Ð¾Ñ�ÑŒ:
    // $s1
    // Ð¼Ð¸Ð½.
    public static final int S1_SECONDS_LEFT_FOR_WYVERN_RIDING = 2237; // Ð”Ð¾
    // Ð³Ð¾Ð½ÐºÐ¸
    // Ð½Ð°
    // Ð²Ð¸Ð²ÐµÑ€Ð½Ð°Ñ…
    // Ð¾Ñ�Ñ‚Ð°Ð»Ð¾Ñ�ÑŒ:
    // $s1
    // Ð¼Ð¸Ð½.
    public static final int YOU_HAVE_PARTICIPATED_IN_THE_SIEGE_OF_S1_THIS_SIEGE_WILL_CONTINUE_FOR_2_HOURS = 2238; // Ð’Ñ‹
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð²ÑƒÐµÑ‚Ðµ
    // Ð²
    // Ð¾Ñ�Ð°Ð´Ðµ
    // $s1.
    // ÐžÑ�Ð°Ð´Ð°
    // Ð¿Ñ€Ð¾Ð´Ð»Ð¸Ñ‚Ñ�Ñ�
    // 2
    // Ñ‡Ð°Ñ�Ð°.
    public static final int THE_SIEGE_OF_S1_IN_WHICH_YOU_ARE_PARTICIPATING_HAS_FINISHED = 2239; // ÐžÑ�Ð°Ð´Ð°
    // $s1,
    // Ð²
    // ÐºÐ¾Ñ‚Ð¾Ñ€Ð¾Ð¹
    // Ð’Ñ‹
    // Ð¿Ñ€Ð¸Ð½Ð¸Ð¼Ð°Ð»Ð¸
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð¸Ðµ,
    // Ð·Ð°ÐºÐ¾Ð½Ñ‡ÐµÐ½Ð°.
    public static final int YOU_CANNOT_REGISTER_FOR_THE_TEAM_BATTLE_CLAN_HALL_WAR_WHEN_YOUR_CLAN_LORD_IS_ON_THE_WAITING_LIST = 2240; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð·Ð°Ñ€ÐµÐ³Ð¸Ñ�Ñ‚Ñ€Ð¸Ñ€Ð¾Ð²Ð°Ñ‚ÑŒÑ�Ñ�
    // Ð½Ð°
    // ÐºÐ¾Ð¼Ð°Ð½Ð´Ð½ÑƒÑŽ
    // Ð±Ð¸Ñ‚Ð²Ñƒ
    // Ð·Ð°
    // Ñ…Ð¾Ð»Ð»
    // ÐºÐ»Ð°Ð½Ð°,
    // ÐºÐ¾Ð³Ð´Ð°
    // Ð³Ð»Ð°Ð²Ð°
    // Ð²Ð°ÑˆÐµÐ³Ð¾
    // ÐºÐ»Ð°Ð½Ð°
    // Ð½Ð°Ñ…Ð¾Ð´Ð¸Ñ‚Ñ�Ñ�
    // Ð²
    // Ð»Ð¸Ñ�Ñ‚Ðµ
    // Ð¾Ð¶Ð¸Ð´Ð°Ð½Ð¸Ñ�
    // Ð½Ð°
    // Ñ‚Ñ€Ð°Ð½Ð·Ð°ÐºÑ†Ð¸ÑŽ.
    public static final int YOU_CANNOT_APPLY_FOR_A_CLAN_LORD_TRANSACTION_IF_YOUR_CLAN_HAS_REGISTERED_FOR_THE_TEAM_BATTLE = 2241; // Ð•Ñ�Ð»Ð¸
    // Ð²Ð°Ñˆ
    // ÐºÐ»Ð°Ð½
    // Ð·Ð°Ñ€ÐµÐ³Ð¸Ñ�Ñ‚Ñ€Ð¸Ñ€Ð¾Ð²Ð°Ð½
    // Ð½Ð°
    // ÐºÐ¾Ð¼Ð°Ð½Ð´Ð½ÑƒÑŽ
    // Ð±Ð¸Ñ‚Ð²Ñƒ
    // Ð·Ð°
    // Ñ…Ð¾Ð»Ð»
    // ÐºÐ»Ð°Ð½Ð°,
    // Ñ‚Ð¾
    // Ñ‚Ñ€Ð°Ð½Ð·Ð°ÐºÑ†Ð¸Ñ�
    // Ð³Ð»Ð°Ð²Ñ‹
    // ÐºÐ»Ð°Ð½Ð°
    // Ð½ÐµÐ´Ð¾Ñ�Ñ‚ÑƒÐ¿Ð½Ð°.
    public static final int CLAN_MEMBERS_CANNOT_LEAVE_OR_BE_EXPELLED_WHEN_THEY_ARE_REGISTERED_FOR_THE_TEAM_BATTLE_CLAN_HALL = 2242; // Ð§Ð»ÐµÐ½
    // ÐºÐ»Ð°Ð½Ð°
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð¿Ð¾ÐºÐ¸Ð½ÑƒÑ‚ÑŒ
    // ÐºÐ»Ð°Ð½,
    // ÐµÑ�Ð»Ð¸
    // ÐºÐ»Ð°Ð½
    // Ð·Ð°Ñ�Ð²Ð»ÐµÐ½
    // Ð½Ð°
    // ÐºÐ¾Ð¼Ð°Ð½Ð´Ð½ÑƒÑŽ
    // Ð±Ð¸Ñ‚Ð²Ñƒ
    // Ð·Ð°
    // Ñ…Ð¾Ð»Ð»
    // ÐºÐ»Ð°Ð½Ð°.
    public static final int WHEN_A_CLAN_LORD_OCCUPYING_THE_BANDIT_STRONGHOLD_OR_WILD_BEAST_RESERVE_CLAN_HALL_IS_IN_DANGER = 2243; // ÐšÐ¾Ð³Ð´Ð°
    // Ð³Ð»Ð°Ð²Ð°
    // ÐºÐ»Ð°Ð½Ð°
    // Ð¾ÐºÐºÑƒÐ¿Ð¸Ñ€ÑƒÐµÑ‚
    // Ð¢Ð²ÐµÑ€Ð´Ñ‹Ð½ÑŽ
    // Ð Ð°Ð·Ð±Ð¾Ð¹Ð½Ð¸ÐºÐ¾Ð²
    // Ð¸Ð»Ð¸
    // Ñ…Ð¾Ð»Ð»
    // ÐºÐ»Ð°Ð½Ð°
    // Ð²
    // Ð—Ð°Ð³Ð¾Ð½Ðµ
    // Ð”Ð¸ÐºÐ¸Ñ…
    // Ð—Ð²ÐµÑ€ÐµÐ¹
    // Ð²
    // Ð¾Ð¿Ð°Ñ�Ð½Ð¾Ñ�Ñ‚Ð¸,
    // Ð¿Ñ€ÐµÐ´Ñ‹Ð´ÑƒÑ‰Ð¸Ð¹
    // Ð³Ð»Ð°Ð²Ð°
    // ÐºÐ»Ð°Ð½Ð°
    // Ð¿Ñ€Ð¸Ð½Ð¸Ð¼Ð°ÐµÑ‚
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð¸Ðµ
    // Ð²
    // Ð±Ð¸Ñ‚Ð²Ðµ
    // Ð·Ð°
    // Ñ…Ð¾Ð»Ð»
    // ÐºÐ»Ð°Ð½Ð°.
    public static final int S1_MINUTES_REMAINING = 2244; // ÐžÑ�Ñ‚Ð°Ð»Ð¾Ñ�ÑŒ: $s1 Ð¼Ð¸Ð½.
    public static final int S1_SECONDS_REMAINING = 2245; // ÐžÑ�Ñ‚Ð°Ð»Ð¾Ñ�ÑŒ: $s1 Ñ�ÐµÐº.
    public static final int THE_CONTEST_WILL_BEGIN_IN_S1_MINUTES = 2246; // Ð¡Ð¾Ñ€ÐµÐ²Ð½Ð¾Ð²Ð°Ð½Ð¸Ðµ
    // Ð½Ð°Ñ‡Ð½ÐµÑ‚Ñ�Ñ�
    // Ñ‡ÐµÑ€ÐµÐ·
    // $s1
    // Ð¼Ð¸Ð½.
    public static final int YOU_CANNOT_BOARD_AN_AIRSHIP_WHILE_TRANSFORMED = 2247; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¾Ñ�ÐµÐ´Ð»Ð°Ñ‚ÑŒ
    // Ð¿Ð¸Ñ‚Ð¾Ð¼Ñ†Ð°,
    // Ð½Ð°Ñ…Ð¾Ð´Ñ�Ñ�ÑŒ
    // Ð²
    // Ð¸Ð·Ð¼ÐµÐ½ÐµÐ½Ð½Ð¾Ð¹
    // Ñ„Ð¾Ñ€Ð¼Ðµ.
    public static final int YOU_CANNOT_BOARD_AN_AIRSHIP_WHILE_PETRIFIED = 2248; // Ð’Ñ‹
    // Ð¾ÐºÐ°Ð¼ÐµÐ½ÐµÐ»Ð¸,
    // Ð¸
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¾Ñ�ÐµÐ´Ð»Ð°Ñ‚ÑŒ
    // Ð¿Ð¸Ñ‚Ð¾Ð¼Ñ†Ð°.
    public static final int YOU_CANNOT_BOARD_AN_AIRSHIP_WHILE_DEAD = 2249; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¾Ñ�ÐµÐ´Ð»Ð°Ñ‚ÑŒ
    // Ð¿Ð¸Ñ‚Ð¾Ð¼Ñ†Ð°
    // Ð±ÑƒÐ´ÑƒÑ‡Ð¸
    // Ð¼ÐµÑ€Ñ‚Ð²Ñ‹Ð¼.
    public static final int YOU_CANNOT_BOARD_AN_AIRSHIP_WHILE_FISHING = 2250; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¾Ñ�ÐµÐ´Ð»Ð°Ñ‚ÑŒ
    // Ð¿Ð¸Ñ‚Ð¾Ð¼Ñ†Ð°
    // Ð²Ð¾
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ñ€Ñ‹Ð±Ð°Ð»ÐºÐ¸.
    public static final int YOU_CANNOT_BOARD_AN_AIRSHIP_WHILE_IN_BATTLE = 2251; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¾Ñ�ÐµÐ´Ð»Ð°Ñ‚ÑŒ
    // Ð¿Ð¸Ñ‚Ð¾Ð¼Ñ†Ð°
    // Ð²Ð¾
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ð±Ð¸Ñ‚Ð²Ñ‹.
    public static final int YOU_CANNOT_BOARD_AN_AIRSHIP_WHILE_IN_A_DUEL = 2252; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¾Ñ�ÐµÐ´Ð»Ð°Ñ‚ÑŒ
    // Ð¿Ð¸Ñ‚Ð¾Ð¼Ñ†Ð°
    // Ð²Ð¾
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ð´ÑƒÑ�Ð»Ð¸.
    public static final int YOU_CANNOT_BOARD_AN_AIRSHIP_WHILE_SITTING = 2253; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¾Ñ�ÐµÐ´Ð»Ð°Ñ‚ÑŒ
    // Ð¿Ð¸Ñ‚Ð¾Ð¼Ñ†Ð°,
    // ÐºÐ¾Ð³Ð´Ð°
    // Ñ�Ð¸Ð´Ð¸Ñ‚Ðµ.
    public static final int YOU_CANNOT_BOARD_AN_AIRSHIP_WHILE_SKILL_CASTING = 2254; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¾Ñ�ÐµÐ´Ð»Ð°Ñ‚ÑŒ
    // Ð¿Ð¸Ñ‚Ð¾Ð¼Ñ†Ð°
    // Ð²Ð¾
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ð²Ñ‹Ð±Ð¾Ñ€Ð°
    // ÑƒÐ¼ÐµÐ½Ð¸Ð¹.
    public static final int YOU_CANNOT_BOARD_AN_AIRSHIP_WHILE_A_CURSED_WEAPON_IS_EQUPPED = 2255; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¾Ñ�ÐµÐ´Ð»Ð°Ñ‚ÑŒ
    // Ð¿Ð¸Ñ‚Ð¾Ð¼Ñ†Ð°,
    // ÐµÑ�Ð»Ð¸
    // Ð’Ñ‹
    // Ñ�ÐºÐ¸Ð¿Ð¸Ñ€Ð¾Ð²Ð°Ð½Ñ‹
    // Ð¿Ñ€Ð¾ÐºÐ»Ñ�Ñ‚Ñ‹Ð¼
    // Ð¾Ñ€ÑƒÐ¶Ð¸ÐµÐ¼.
    public static final int YOU_CANNOT_BOARD_AN_AIRSHIP_WHILE_HOLDING_A_FLAG = 2256; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¾Ñ�ÐµÐ´Ð»Ð°Ñ‚ÑŒ
    // Ð¿Ð¸Ñ‚Ð¾Ð¼Ñ†Ð°,
    // ÐµÑ�Ð»Ð¸
    // Ð´ÐµÑ€Ð¶Ð¸Ñ‚Ðµ
    // Ñ„Ð»Ð°Ð³.
    public static final int YOU_CANNOT_BOARD_AN_AIRSHIP_WHILE_A_PET_OR_A_SERVITOR_IS_SUMMONED = 2257; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¾Ñ�ÐµÐ´Ð»Ð°Ñ‚ÑŒ
    // Ð¿Ð¸Ñ‚Ð¾Ð¼Ñ†Ð°,
    // ÐµÑ�Ð»Ð¸
    // Ð¿Ñ€Ð¸Ð·Ð²Ð°Ð½
    // Ñ�Ð»ÑƒÐ³Ð°
    // Ð¸Ð»Ð¸
    // Ð¿Ð¸Ñ‚Ð¾Ð¼ÐµÑ†.
    public static final int YOU_HAVE_ALREADY_BOARDED_ANOTHER_AIRSHIP = 2258; // Ð’Ñ‹
    // ÑƒÐ¶Ðµ
    // Ð¾Ñ�ÐµÐ´Ð»Ð°Ð»Ð¸
    // Ð´Ñ€ÑƒÐ³Ð¾Ð³Ð¾
    // Ð¿Ð¸Ñ‚Ð¾Ð¼Ñ†Ð°.

    public static final int THE_PET_CAN_RUN_AWAY_IF_THE_HUNGER_GAUGE_IS_BELOW_10 = 2260; // ÐŸÐ¸Ñ‚Ð¾Ð¼ÐµÑ†
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // ÑƒÐ±ÐµÐ¶Ð°Ñ‚ÑŒ,
    // ÐµÑ�Ð»Ð¸
    // ÐµÐ³Ð¾
    // ÑˆÐºÐ°Ð»Ð°
    // Ð³Ð¾Ð»Ð¾Ð´Ð°
    // Ð¾Ð¿ÑƒÑ�Ñ‚Ð¸Ñ‚Ñ�Ñ�
    // Ð½Ð¸Ð¶Ðµ
    // 10%.
    public static final int THE_KEY_YOU_ASSIGNED_AS_A_SHORTCUT_KEY_IS_NOT_AVAILABLE_IN_THE_REGULAR_CHATTING_MODE = 2272; // ÐšÐ»Ð°Ð²Ð¸ÑˆÐ°,
    // Ð½Ð°Ð·Ð½Ð°Ñ‡ÐµÐ½Ð½Ð°Ñ�
    // Ð²Ð°Ð¼Ð¸
    // ÐºÐ°Ðº
    // Ñ�Ñ€Ð»Ñ‹Ðº,
    // Ð½ÐµÐ´Ð¾Ñ�Ñ‚ÑƒÐ¿Ð½Ð°
    // Ð²
    // Ð¾Ð±Ñ‹Ñ‡Ð½Ð¾Ð¼
    // Ñ€ÐµÐ¶Ð¸Ð¼Ðµ
    // Ñ‡Ð°Ñ‚Ð°.
    public static final int THIS_SKILL_CANNOT_BE_LEARNED_WHILE_IN_THE_SUB_CLASS_STATE_PLEASE_TRY_AGAIN_AFTER_CHANGING_TO_THE = 2273; // Ð­Ñ‚Ð¾
    // ÑƒÐ¼ÐµÐ½Ð¸Ðµ
    // Ð½ÐµÐ»ÑŒÐ·Ñ�
    // Ð²Ñ‹ÑƒÑ‡Ð¸Ñ‚ÑŒ
    // Ð¿Ð¾Ð´ÐºÐ»Ð°Ñ�Ñ�Ð¾Ð¼.
    // Ð’ÐµÑ€Ð½Ð¸Ñ‚ÐµÑ�ÑŒ
    // Ðº
    // Ð¾Ñ�Ð½Ð¾Ð²Ð½Ð¾Ð¼Ñƒ
    // ÐºÐ»Ð°Ñ�Ñ�Ñƒ
    // Ð¸
    // Ð¿Ð¾Ð²Ñ‚Ð¾Ñ€Ð¸Ñ‚Ðµ
    // Ð¿Ð¾Ð¿Ñ‹Ñ‚ÐºÑƒ.
    public static final int YOU_ENTERED_AN_AREA_WHERE_YOU_CANNOT_THROW_AWAY_ITEMS = 2274; // Ð’Ñ‹
    // Ð²Ð¾ÑˆÐ»Ð¸
    // Ð²
    // Ð·Ð¾Ð½Ñƒ,
    // Ð³Ð´Ðµ
    // Ð½ÐµÐ»ÑŒÐ·Ñ�
    // Ð²Ñ‹Ð±Ñ€Ð°Ñ�Ñ‹Ð²Ð°Ñ‚ÑŒ
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ñ‹.
    public static final int YOU_ARE_IN_AN_AREA_WHERE_YOU_CANNOT_CANCEL_PET_SUMMONING = 2275; // Ð’Ñ‹
    // Ð²
    // Ð·Ð¾Ð½Ðµ,
    // Ð³Ð´Ðµ
    // Ð½ÐµÐ»ÑŒÐ·Ñ�
    // Ð¾Ñ‚Ð¼ÐµÐ½Ð¸Ñ‚ÑŒ
    // Ð¿Ñ€Ð¸Ð·Ñ‹Ð²
    // Ð¿Ð¸Ñ‚Ð¾Ð¼Ñ†Ð°.
    public static final int PARTY_OF_S1 = 2277; // Ð“Ñ€ÑƒÐ¿Ð¿Ð° Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð° $s1
    public static final int REMAINING_TIME_S1_S2 = 2278; // ÐžÑ�Ñ‚Ð°Ð»Ð¾Ñ�ÑŒ Ð²Ñ€ÐµÐ¼ÐµÐ½Ð¸:
    // $s1:$s2
    public static final int YOU_CAN_NO_LONGER_ADD_A_QUEST_TO_THE_QUEST_ALERTS = 2279; // Ð’Ñ‹
    // Ð±Ð¾Ð»ÑŒÑˆÐµ
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð´Ð¾Ð±Ð°Ð²Ð»Ñ�Ñ‚ÑŒ
    // ÐºÐ²ÐµÑ�Ñ‚Ñ‹
    // Ð²
    // Ð¶ÑƒÑ€Ð½Ð°Ð»
    // ÐºÐ²ÐµÑ�Ñ‚Ð¾Ð².
    public static final int C1_HIT_YOU_FOR_S3_DAMAGE_AND_HIT_YOUR_SERVITOR_FOR_S4 = 2281; // $c1
    // Ð½Ð°Ð½Ð¾Ñ�Ð¸Ñ‚
    // ÑƒÑ€Ð¾Ð½
    // $s3
    // Ð²Ñ€Ð°Ð³Ñƒ
    // $c2,
    // Ñ‚Ð°ÐºÐ¶Ðµ
    // Ð½Ð°Ð½ÐµÑ�
    // ÑƒÑ€Ð¾Ð½
    // $s4
    // Ð¾Ð±ÑŠÐµÐºÑ‚Ñƒ.
    public static final int LEAVE_FANTASY_ISLE = 2282; // ÐŸÐ¾ÐºÐ¸Ð½ÑƒÑ‚ÑŒ ÐžÑ�Ñ‚Ñ€Ð¾Ð² Ð“Ñ€ÐµÐ·?
    public static final int YOU_HAVE_OBTAINED_ALL_THE_POINTS_YOU_CAN_GET_TODAY_IN_A_PLACE_OTHER_THAN_INTERNET_CAF = 2284; // Ð¡ÐµÐ³Ð¾Ð´Ð½Ñ�
    // Ð²Ñ‹
    // Ð´Ð¾Ð±Ñ‹Ð»Ð¸
    // Ð²Ñ�Ðµ
    // Ð²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ñ‹Ðµ
    // Ð¾Ñ‡ÐºÐ¸
    // Ð²
    // Ð¼ÐµÑ�Ñ‚Ðµ,
    // Ð¾Ñ‚Ð»Ð¸Ñ‡Ð½Ð¾Ð¼
    // Ð¾Ñ‚
    // Ð¸Ð½Ñ‚ÐµÑ€Ð½ÐµÑ‚
    // ÐºÐ°Ñ„Ðµ.
    public static final int THIS_SKILL_CANNOT_REMOVE_THIS_TRAP = 2285; // Ð­Ñ‚Ð¾
    // ÑƒÐ¼ÐµÐ½Ð¸Ðµ
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð¾Ð±ÐµÐ·Ð²Ñ€ÐµÐ´Ð¸Ñ‚ÑŒ
    // Ñ�Ñ‚Ñƒ
    // Ð»Ð¾Ð²ÑƒÑˆÐºÑƒ.
    public static final int YOU_CANNOT_WEAR_S1_BECAUSE_YOU_ARE_NOT_WEARING_THE_BRACELET = 2286; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¾Ð´ÐµÑ‚ÑŒ
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚
    // $s1,
    // Ñ‚Ð°Ðº
    // ÐºÐ°Ðº
    // Ð½Ðµ
    // Ð½Ð¾Ñ�Ð¸Ñ‚Ðµ
    // Ð±Ñ€Ð°Ñ�Ð»ÐµÑ‚Ñ‹.
    public static final int THERE_IS_NO_SPACE_TO_WEAR_S1 = 2287; // Ð�ÐµÐ»ÑŒÐ·Ñ� Ð¾Ð´ÐµÑ‚ÑŒ
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚ $s1,
    // Ð½ÐµÑ‚
    // Ñ�Ð²Ð¾Ð±Ð¾Ð´Ð½Ð¾Ð³Ð¾
    // Ð¼ÐµÑ�Ñ‚Ð°.
    public static final int RESURRECTION_WILL_OCCUR_IN_S1_SECONDS = 2288; // Ð’Ð¾Ñ�ÐºÑ€ÐµÑˆÐµÐ½Ð¸Ðµ
    // Ð¿Ñ€Ð¾Ð¸Ð·Ð¾Ð¹Ð´ÐµÑ‚
    // Ñ‡ÐµÑ€ÐµÐ·
    // $s1
    // Ñ�ÐµÐº.
    public static final int THE_MATCH_BETWEEN_THE_PARTIES_IS_NOT_AVAILABLE_BECAUSE_ONE_OF_THE_PARTY_MEMBERS_IS_BEING = 2289; // Ð“Ñ€ÑƒÐ¿Ð¿Ð¾Ð²Ð¾Ðµ
    // Ñ�Ð¾Ñ€ÐµÐ²Ð½Ð¾Ð²Ð°Ð½Ð¸Ðµ
    // Ð½ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾,
    // Ñ‚Ð°Ðº
    // ÐºÐ°Ðº
    // Ð¾Ð´Ð¸Ð½
    // Ð¸Ð·
    // Ñ‡Ð»ÐµÐ½Ð¾Ð²
    // Ð³Ñ€ÑƒÐ¿Ð¿Ñ‹
    // Ñ‚ÐµÐ»ÐµÐ¿Ð¾Ñ€Ñ‚Ð¸Ñ€Ð¾Ð²Ð°Ð½.
    public static final int YOU_CANNOT_ASSIGN_SHORTCUT_KEYS_BEFORE_YOU_LOG_IN = 2290; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð½Ð°Ð·Ð½Ð°Ñ‡Ð¸Ñ‚ÑŒ
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¸
    // Ð´Ð¾
    // Ð·Ð°Ñ…Ð¾Ð´Ð°
    // Ð²
    // Ð¸Ð³Ñ€Ñƒ.
    public static final int YOU_CAN_OPERATE_THE_MACHINE_WHEN_YOU_PARTICIPATE_IN_THE_PARTY = 2291; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // ÑƒÐ¿Ñ€Ð°Ð²Ð»Ñ�Ñ‚ÑŒ
    // Ñ�Ñ‚Ð¸Ð¼
    // Ð¼ÐµÑ…Ð°Ð½Ð¸Ð·Ð¼Ð¾Ð¼,
    // ÐµÑ�Ð»Ð¸
    // Ñ�Ð¾Ñ�Ñ‚Ð¾Ð¸Ñ‚Ðµ
    // Ð²
    // Ð³Ñ€ÑƒÐ¿Ð¿Ðµ.
    public static final int CURRENT_LOCATION__S1_S2_S3_INSIDE_THE_STEEL_CITADEL = 2293; // Ð¢ÐµÐºÑƒÑ‰Ð°Ñ�
    // Ð»Ð¾ÐºÐ°Ñ†Ð¸Ñ�:
    // $s1,
    // $s2,
    // $s3
    // (Ð²Ð½ÑƒÑ‚Ñ€Ð¸
    // Ð¡Ñ‚Ð°Ð»ÑŒÐ½Ð¾Ð¹
    // Ð¦Ð¸Ñ‚Ð°Ð´ÐµÐ»Ð¸)
    public static final int THE_WIDTH_OF_THE_UPLOADED_BADGE_OR_INSIGNIA_DOES_NOT_MEET_THE_STANDARD_REQUIREMENTS = 2294; // Ð¨Ð¸Ñ€Ð¸Ð½Ð°
    // Ð·Ð°Ð³Ñ€ÑƒÐ¶ÐµÐ½Ð½Ð¾Ð³Ð¾
    // Ð·Ð½Ð°Ñ‡ÐºÐ°
    // Ð¸Ð»Ð¸
    // Ñ�Ð¼Ð±Ð»ÐµÐ¼Ñ‹
    // Ð½Ðµ
    // Ñ�Ð¾Ð¾Ñ‚Ð²ÐµÑ‚Ñ�Ñ‚Ð²ÑƒÐµÑ‚
    // Ñ�Ñ‚Ð°Ð½Ð´Ð°Ñ€Ñ‚Ð°Ð¼.
    public static final int THE_LENGTH_OF_THE_UPLOADED_BADGE_OR_INSIGNIA_DOES_NOT_MEET_THE_STANDARD_REQUIREMENTS = 2295; // Ð”Ð»Ð¸Ð½Ð°
    // Ð·Ð°Ð³Ñ€ÑƒÐ¶ÐµÐ½Ð½Ð¾Ð³Ð¾
    // Ð·Ð½Ð°Ñ‡ÐºÐ°
    // Ð¸Ð»Ð¸
    // Ñ�Ð¼Ð±Ð»ÐµÐ¼Ñ‹
    // Ð½Ðµ
    // Ñ�Ð¾Ð¾Ñ‚Ð²ÐµÑ‚Ñ�Ñ‚Ð²ÑƒÐµÑ‚
    // Ñ�Ñ‚Ð°Ð½Ð´Ð°Ñ€Ñ‚Ð°Ð¼.
    public static final int ROUND_S1 = 2297; // Ð Ð°ÑƒÐ½Ð´ $s1
    public static final int THE_COLOR_OF_THE_BADGE_OR_INSIGNIA_THAT_YOU_WANT_TO_REGISTER_DOES_NOT_MEET_THE_STANDARD = 2298; // Ð¦Ð²ÐµÑ‚
    // Ð·Ð½Ð°Ñ‡ÐºÐ°
    // Ð¸Ð»Ð¸
    // Ñ�Ð¼Ð±Ð»ÐµÐ¼Ñ‹,
    // ÐºÐ¾Ñ‚Ð¾Ñ€ÑƒÑŽ
    // Ð’Ñ‹
    // Ñ…Ð¾Ñ‚Ð¸Ñ‚Ðµ
    // Ð·Ð°Ñ€ÐµÐ³Ð¸Ñ�Ñ‚Ñ€Ð¸Ñ€Ð¾Ð²Ð°Ñ‚ÑŒ,
    // Ð½Ðµ
    // Ñ�Ð¾Ð¾Ñ‚Ð²ÐµÑ‚Ñ�Ñ‚Ð²ÑƒÐµÑ‚
    // Ñ�Ñ‚Ð°Ð½Ð´Ð°Ñ€Ñ‚Ð°Ð¼.
    public static final int THE_FILE_FORMAT_OF_THE_BADGE_OR_INSIGNIA_THAT_YOU_WANT_TO_REGISTER_DOES_NOT_MEET_THE_STANDARD = 2299; // Ð¤Ð¾Ñ€Ð¼Ð°Ñ‚
    // Ñ„Ð°Ð¹Ð»Ð°
    // Ð·Ð½Ð°Ñ‡ÐºÐ°
    // Ð¸Ð»Ð¸
    // Ñ�Ð¼Ð±Ð»ÐµÐ¼Ñ‹,
    // ÐºÐ¾Ñ‚Ð¾Ñ€ÑƒÑŽ
    // Ð²Ñ‹
    // Ñ…Ð¾Ñ‚Ð¸Ñ‚Ðµ
    // Ð·Ð°Ñ€ÐµÐ³Ð¸Ñ�Ñ‚Ñ€Ð¸Ñ€Ð¾Ð²Ð°Ñ‚ÑŒ,
    // Ð½Ðµ
    // Ñ�Ð¾Ð¾Ñ‚Ð²ÐµÑ‚Ñ�Ñ‚Ð²ÑƒÐµÑ‚
    // Ñ‚Ñ€ÐµÐ±Ð¾Ð²Ð°Ð½Ð¸Ñ�Ð¼.
    public static final int FAILED_TO_LOAD_KEYBOARD_SECURITY_MODULE_FOR_EFFECTIVE_GAMING_FUNCTIONALITY_WHEN_THE_GAME_IS_OVER = 2300; // Ð�ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾
    // Ð·Ð°Ð³Ñ€ÑƒÐ·Ð¸Ñ‚ÑŒ
    // Ð¼Ð¾Ð´ÑƒÐ»ÑŒ
    // Ð·Ð°Ñ‰Ð¸Ñ‚Ñ‹
    // ÐºÐ»Ð°Ð²Ð¸Ð°Ñ‚ÑƒÑ€Ñ‹.
    // ÐšÐ¾Ð³Ð´Ð°
    // Ð²Ñ‹
    // Ð·Ð°ÐºÐ¾Ð½Ñ‡Ð¸Ñ‚Ðµ
    // Ð¸Ð³Ñ€Ñƒ,
    // Ð¿Ð¾Ð¶Ð°Ð»ÑƒÐ¹Ñ�Ñ‚Ð°,
    // Ð¿Ñ€Ð¾Ð²ÐµÑ€ÑŒÑ‚Ðµ
    // Ð²Ñ�Ðµ
    // Ñ„Ð°Ð¹Ð»Ñ‹
    // Ð²
    // Ñ€ÐµÐ¶Ð¸Ð¼Ðµ
    // Ð°Ð²Ñ‚Ð¾Ð¼Ð°Ñ‚Ð¸Ñ‡ÐµÑ�ÐºÐ¾Ð³Ð¾
    // Ð¾Ð±Ð½Ð¾Ð²Ð»ÐµÐ½Ð¸Ñ�
    // Lineage
    // II.
    public static final int CURRENT_LOCATION__STEEL_CITADEL_RESISTANCE = 2301; // Ð¢ÐµÐºÑƒÑ‰Ð°Ñ�
    // Ð»Ð¾ÐºÐ°Ñ†Ð¸Ñ�:
    // Ð²Ð½ÑƒÑ‚Ñ€Ð¸
    // Ð¡Ñ‚Ð°Ð»ÑŒÐ½Ð¾Ð¹
    // Ð¦Ð¸Ñ‚Ð°Ð´ÐµÐ»Ð¸.
    public static final int YOUR_VITAMIN_ITEM_HAS_ARRIVED_VISIT_THE_VITAMIN_MANAGER_IN_ANY_VILLAGE_TO_OBTAIN_IT = 2302; // Ð’Ð¸Ñ‚Ð°Ð¼Ð¸Ð½
    // Ð´Ð¾Ñ�Ñ‚Ð°Ð²Ð»ÐµÐ½.
    // Ð§Ñ‚Ð¾Ð±Ñ‹
    // Ð¿Ð¾Ð»ÑƒÑ‡Ð¸Ñ‚ÑŒ
    // ÐµÐ³Ð¾,
    // Ð½Ð°Ð¹Ð´Ð¸Ñ‚Ðµ
    // Ð¼ÐµÐ½ÐµÐ´Ð¶ÐµÑ€Ð°
    // Ð²Ð¸Ñ‚Ð°Ð¼Ð¸Ð½Ð¾Ð²
    // Ð²
    // Ð´ÐµÑ€ÐµÐ²Ð½Ðµ.
    public static final int RESURRECTION_IS_POSSIBLE_BECAUSE_OF_THE_COURAGE_CHARM_S_EFFECT_WOULD_YOU_LIKE_TO_RESURRECT_NOW = 2306; // Ð’Ð¾Ñ�ÐºÑ€ÐµÑˆÐµÐ½Ð¸Ðµ
    // Ð²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾
    // Ð±Ð»Ð°Ð³Ð¾Ð´Ð°Ñ€Ñ�
    // Ñ�Ñ„Ñ„ÐµÐºÑ‚Ñƒ
    // ÐžÑ‚Ð²Ð°Ð³Ð°.
    // Ð¥Ð¾Ñ‚Ð¸Ñ‚Ðµ
    // Ð²Ð¾Ñ�ÐºÑ€ÐµÑ�Ð½ÑƒÑ‚ÑŒ?
    public static final int THE_TARGET_IS_RECEIVING_THE_COURAGE_CHARM_S_EFFECT = 2307; // Ð¦ÐµÐ»ÑŒ
    // Ð¿Ð¾Ð»ÑƒÑ‡Ð°ÐµÑ‚
    // Ñ�Ñ„Ñ„ÐµÐºÑ‚
    // ÐžÑ‚Ð²Ð°Ð³Ð°.
    public static final int REMAINING_TIME__S1_DAYS = 2308; // ÐžÑ�Ñ‚Ð°Ð»Ð¾Ñ�ÑŒ Ð²Ñ€ÐµÐ¼ÐµÐ½Ð¸:
    // $s1 Ð´Ð½.
    public static final int REMAINING_TIME__S1_HOURS = 2309; // ÐžÑ�Ñ‚Ð°Ð»Ð¾Ñ�ÑŒ
    // Ð²Ñ€ÐµÐ¼ÐµÐ½Ð¸: $s1 Ñ‡.
    public static final int REMAINING_TIME__S1_MINUTES = 2310; // ÐžÑ�Ñ‚Ð°Ð»Ð¾Ñ�ÑŒ
    // Ð²Ñ€ÐµÐ¼ÐµÐ½Ð¸: $s1
    // Ð¼Ð¸Ð½.
    public static final int YOU_DO_NOT_HAVE_A_SERVITOR = 2311; // Ð£ Ð’Ð°Ñ� Ð½ÐµÑ‚
    // Ñ�Ð»ÑƒÐ³Ð¸.
    public static final int YOU_DO_NOT_HAVE_A_PET = 2312; // Ð£ Ð’Ð°Ñ� Ð½ÐµÑ‚ Ð¿Ð¸Ñ‚Ð¾Ð¼Ñ†Ð°.
    public static final int THE_VITAMIN_ITEM_HAS_ARRIVED = 2313; // Ð’Ð¸Ñ‚Ð°Ð¼Ð¸Ð½
    // Ð´Ð¾Ñ�Ñ‚Ð°Ð²Ð»ÐµÐ½.
    public static final int ONLY_AN_ENHANCED_SKILL_CAN_BE_CANCELLED = 2318; // Ð¢Ð¾Ð»ÑŒÐºÐ¾
    // ÑƒÐ»ÑƒÑ‡ÑˆÐµÐ½Ð½Ð¾Ðµ
    // ÑƒÐ¼ÐµÐ½Ð¸Ðµ
    // Ð¼Ð¾Ð¶Ð½Ð¾
    // Ð¾Ñ‚Ð¼ÐµÐ½Ð¸Ñ‚ÑŒ.
    public static final int MASTERWORK_POSSIBLE = 2320; // ÐœÐ¾Ð¶Ð½Ð¾ Ñ�Ð¾Ð·Ð´Ð°Ñ‚ÑŒ ÑˆÐµÐ´ÐµÐ²Ñ€.
    public static final int CURRENT_LOCATION__INSIDE_KAMALOKA = 2321; // Ð¢ÐµÐºÑƒÑ‰Ð°Ñ�
    // Ð»Ð¾ÐºÐ°Ñ†Ð¸Ñ�:
    // Ð²Ð½ÑƒÑ‚Ñ€Ð¸
    // ÐšÐ°Ð¼Ð°Ð»Ð¾ÐºÐ¸
    public static final int CURRENT_LOCATION__INSIDE_NIA_KAMALOKA = 2322; // Ð¢ÐµÐºÑƒÑ‰Ð°Ñ�
    // Ð»Ð¾ÐºÐ°Ñ†Ð¸Ñ�:
    // Ð²Ð½ÑƒÑ‚Ñ€Ð¸
    // Ð—ÐµÐ¼ÐµÐ»ÑŒ
    // ÐšÐ°Ð¼Ð°Ð»Ð¾ÐºÐ¸
    public static final int CURRENT_LOCATION__INSIDE_RIM_KAMALOKA = 2323; // Ð¢ÐµÐºÑƒÑ‰Ð°Ñ�
    // Ð»Ð¾ÐºÐ°Ñ†Ð¸Ñ�:
    // Ð²Ð½ÑƒÑ‚Ñ€Ð¸
    // ÐžÐºÑ€ÐµÑ�Ñ‚Ð½Ð¾Ñ�Ñ‚ÐµÐ¹
    // ÐšÐ°Ð¼Ð°Ð»Ð¾ÐºÐ¸
    public static final int C1_YOU_CANNOT_ENTER_BECAUSE_YOU_HAVE_INSUFFICIENT_PC_CAFE_POINTS = 2324; // $c1,
    // Ð²Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð²Ð¾Ð¹Ñ‚Ð¸,
    // Ñƒ
    // Ð²Ð°Ñ�
    // Ð½ÐµÐ´Ð¾Ñ�Ñ‚Ð°Ñ‚Ð¾Ñ‡Ð½Ð¾
    // Ð¾Ñ‡ÐºÐ¾Ð²
    // Ð¸Ð³Ñ€Ð¾Ð²Ð¾Ð³Ð¾
    // ÐºÐ»ÑƒÐ±Ð°.
    public static final int ANOTHER_TELEPORT_IS_TAKING_PLACE_PLEASE_TRY_AGAIN_ONCE_THE_TELEPORT_IN_PROCESS_ENDS = 2325; // ÐŸÑ€Ð¾Ð¸Ñ�Ñ…Ð¾Ð´Ð¸Ñ‚
    // Ñ‚ÐµÐ»ÐµÐ¿Ð¾Ñ€Ñ‚Ð°Ñ†Ð¸Ñ�.
    // ÐŸÐ¾Ð²Ñ‚Ð¾Ñ€Ð¸Ñ‚Ðµ
    // Ð¿Ð¾Ð¿Ñ‹Ñ‚ÐºÑƒ,
    // ÐºÐ¾Ð³Ð´Ð°
    // Ð¿Ñ€Ð¾Ñ†ÐµÑ�Ñ�
    // Ñ‚ÐµÐ»ÐµÐ¿Ð¾Ñ€Ñ‚Ð°Ñ†Ð¸Ð¸
    // Ð·Ð°Ð²ÐµÑ€ÑˆÐ¸Ñ‚Ñ�Ñ�.
    public static final int CLANS_OF_LEVEL_4_OR_ABOVE_CAN_REGISTER_FOR_HIDEAWAY_WARS_FOR_DEVASTATED_CASTLE_AND_FORTRESS_OF = 2328; // ÐšÐ»Ð°Ð½Ñ‹
    // 4
    // ÑƒÑ€Ð¾Ð²Ð½Ñ�
    // Ð¸
    // Ð²Ñ‹ÑˆÐµ
    // Ð¼Ð¾Ð³ÑƒÑ‚
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð²Ð¾Ð²Ð°Ñ‚ÑŒ
    // Ð²
    // Ð±Ð¸Ñ‚Ð²Ðµ
    // ÑƒÐºÑ€ÐµÐ¿Ð»ÐµÐ½Ð¸Ð¹
    // Ð²
    // Ð Ð°Ð·Ð¾Ñ€ÐµÐ½Ð½Ð¾Ð¼
    // Ð—Ð°Ð¼ÐºÐµ
    // Ð¸
    // ÐšÑ€ÐµÐ¿Ð¾Ñ�Ñ‚Ð¸
    // Ð�ÐµÑƒÐ¿Ð¾ÐºÐ¾ÐµÐ½Ð½Ñ‹Ñ….
    public static final int VITALITY_LEVEL_S1_S2 = 2329; // Ð£Ñ€Ð¾Ð²ÐµÐ½ÑŒ Ñ�Ð½ÐµÑ€Ð³Ð¸Ð¸ $s1
    // $s2
    public static final int __EXPERIENCE_POINTS_BOOSTED_BY_S1 = 2330; // -
    // ÐšÐ¾Ð»Ð¸Ñ‡ÐµÑ�Ñ‚Ð²Ð¾
    // Ð¿Ð¾Ð»ÑƒÑ‡Ð°ÐµÐ¼Ð¾Ð³Ð¾
    // Ð¾Ð¿Ñ‹Ñ‚Ð°
    // Ñ�Ð¾Ñ�Ñ‚Ð°Ð²Ð»Ñ�ÐµÑ‚
    // $s1% Ð¾Ñ‚
    // Ð¾Ð±Ñ‹Ñ‡Ð½Ð¾Ð³Ð¾.
    public static final int RARE_S1 = 2331; // <Ð ÐµÐ´ÐºÐ¸Ð¹> $s1
    public static final int SUPPLY_S1 = 2332; // <Ð ÐµÑ�ÑƒÑ€Ñ�> $s1
    public static final int YOU_CANNOT_RECEIVE_THE_VITAMIN_ITEM_BECAUSE_YOU_HAVE_EXCEED_YOUR_INVENTORY_WEIGHT_QUANTITY_LIMIT = 2333; // Ð�Ð°Ð³Ñ€Ð°Ð´Ð°
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð¿Ð¾Ð»ÑƒÑ‡ÐµÐ½Ð°,
    // Ñ‚Ð°Ðº
    // ÐºÐ°Ðº
    // Ð²
    // Ð¸Ð½Ð²ÐµÐ½Ñ‚Ð°Ñ€Ðµ
    // Ð½ÐµÑ‚
    // Ñ�Ð²Ð¾Ð±Ð¾Ð´Ð½Ð¾Ð³Ð¾
    // Ð¼ÐµÑ�Ñ‚Ð°.
    public static final int SCORE_THAT_SHOWS_A_PLAYER_S_INDIVIDUAL_FAME_FAME_CAN_BE_OBTAINED_BY_PARTICIPATING_IN_A_TERRITORY = 2334; // ÐžÑ‡ÐºÐ¸,
    // Ð¿Ð¾ÐºÐ°Ð·Ñ‹Ð²Ð°ÑŽÑ‰Ð¸Ðµ
    // Ð»Ð¸Ñ‡Ð½ÑƒÑŽ
    // Ñ€ÐµÐ¿ÑƒÑ‚Ð°Ñ†Ð¸ÑŽ.
    // ÐœÐ¾Ð³ÑƒÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð´Ð¾Ð±Ñ‹Ñ‚Ñ‹
    // Ð²
    // Ð¾Ñ�Ð°Ð´Ðµ
    // Ñ‚ÐµÑ€Ñ€Ð¸Ñ‚Ð¾Ñ€Ð¸Ð¸,
    // Ð·Ð°Ð¼ÐºÐ°,
    // ÐºÑ€ÐµÐ¿Ð¾Ñ�Ñ‚Ð¸,
    // ÑƒÐºÑ€ÐµÐ¿Ð»ÐµÐ½Ð¸Ñ�,
    // Ð²
    // ÐŸÐ¾Ð´Ð·ÐµÐ¼Ð½Ð¾Ð¼
    // ÐšÐ¾Ð»Ð¸Ð·ÐµÐµ,
    // Ð½Ð°
    // Ð¤ÐµÑ�Ñ‚Ð¸Ð²Ð°Ð»Ðµ
    // Ð¢ÑŒÐ¼Ñ‹
    // Ð¸
    // ÐžÐ»Ð¸Ð¼Ð¿Ð¸Ð°Ð´Ðµ.
    public static final int THERE_ARE_NO_MORE_VITAMIN_ITEMS_TO_BE_FOUND = 2335; // Ð’Ð¸Ñ‚Ð°Ð¼Ð¸Ð½Ð¾Ð²
    // Ð±Ð¾Ð»ÑŒÑˆÐµ
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð½Ð°Ð¹Ð´ÐµÐ½Ð¾.
    public static final int IF_IT_S_A_DRAW_THE_PLAYER_WHO_FIRST_ENTERED_IS_FIRST = 2338; // ÐŸÑ€Ð¸
    // Ð¾Ð´Ð¸Ð½Ð°ÐºÐ¾Ð²Ð¾Ð¼
    // Ñ€ÐµÐ·ÑƒÐ»ÑŒÑ‚Ð°Ñ‚Ðµ
    // Ð¿ÐµÑ€Ð²Ñ‹Ð¹
    // Ð¸Ð³Ñ€Ð¾Ðº
    public static final int PLEASE_PLACE_THE_ITEM_TO_BE_ENCHANTED = 2339; // ÐŸÐµÑ€ÐµÑ‚Ð°Ñ‰Ð¸Ñ‚Ðµ
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚,
    // ÐºÐ¾Ñ‚Ð¾Ñ€Ñ‹Ð¹
    // Ð’Ñ‹
    // Ñ…Ð¾Ñ‚Ð¸Ñ‚Ðµ
    // Ð·Ð°Ñ‡Ð°Ñ€Ð¾Ð²Ð°Ñ‚ÑŒ.
    public static final int PLEASE_PLACE_THE_ITEM_FOR_RATE_INCREASE = 2340; // ÐŸÐµÑ€ÐµÑ‚Ð°Ñ‰Ð¸Ñ‚Ðµ
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚,
    // ÑˆÐ°Ð½Ñ�
    // ÐºÐ¾Ñ‚Ð¾Ñ€Ð¾Ð³Ð¾
    // Ð’Ñ‹
    // Ñ…Ð¾Ñ‚Ð¸Ñ‚Ðµ
    // ÑƒÐ²ÐµÐ»Ð¸Ñ‡Ð¸Ñ‚ÑŒ.
    public static final int THE_ENCHANT_WILL_BEGIN_ONCE_YOU_PRESS_THE_START_BUTTON_BELOW = 2341; // Ð�Ð°Ð¶Ð¼Ð¸Ñ‚Ðµ
    // ÐºÐ½Ð¾Ð¿ÐºÑƒ
    // "Ð�Ð°Ñ‡Ð°Ñ‚ÑŒ",
    // Ñ‡Ñ‚Ð¾Ð±Ñ‹
    // Ð·Ð°Ñ‡Ð°Ñ€Ð¾Ð²Ð°Ñ‚ÑŒ
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚.
    public static final int SUCCESS_THE_ITEM_IS_NOW_A_S1 = 2342; // ÐŸÐ¾Ð·Ð´Ñ€Ð°Ð²Ð»Ñ�ÐµÐ¼.
    // ÐŸÑ€ÐµÐ´Ð¼ÐµÑ‚
    // ÑƒÑ�Ð¿ÐµÑˆÐ½Ð¾
    // Ð·Ð°Ñ‡Ð°Ñ€Ð¾Ð²Ð°Ð½ Ð¸
    // Ñ�Ñ‚Ð°Ð» $s1.
    public static final int FAILED_YOU_HAVE_OBTAINED_S2_OF_S1 = 2343; // Ð—Ð°Ñ‡Ð°Ñ€Ð¾Ð²Ð°Ð½Ð¸Ðµ
    // Ð½Ðµ
    // ÑƒÐ´Ð°Ð»Ð¾Ñ�ÑŒ.
    // Ð’Ñ‹
    // Ð¿Ð¾Ð»ÑƒÑ‡Ð¸Ð»Ð¸
    // $s1
    // $s2ÐµÐ´.
    public static final int YOU_HAVE_BEEN_KILLED_BY_AN_ATTACK_FROM_C1 = 2344; // Ð’Ñ‹
    // Ð±Ñ‹Ð»Ð¸
    // Ð°Ñ‚Ð°ÐºÐ¾Ð²Ð°Ð½Ñ‹
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶ÐµÐ¼
    // $c1
    // Ð¸
    // Ð¿Ð¾Ð³Ð¸Ð±Ð»Ð¸.
    public static final int YOU_HAVE_ATTACKED_AND_KILLED_C1 = 2345; // Ð’Ñ‹ ÑƒÐ±Ð¸Ð»Ð¸
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°
    // $c1.
    public static final int YOUR_ACCOUNT_IS_TEMPORARILY_LIMITED_BECAUSE_YOUR_GAME_USE_GOAL_IS_PRESUMED_TO_BE_EMBEZZLEMENT_OF = 2346; // Ð’Ñ‹Ñ�Ð²Ð»ÐµÐ½Ð°
    // Ð¿Ð¾Ð¿Ñ‹Ñ‚ÐºÐ°
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ð½Ð¸Ñ�
    // Ð°ÐºÐºÐ°ÑƒÐ½Ñ‚Ð°
    // Ñ�
    // Ñ†ÐµÐ»ÑŒÑŽ
    // Ñ‚Ð¾Ñ€Ð³Ð¾Ð²Ð»Ð¸
    // Ð¸Ð³Ñ€Ð¾Ð²Ñ‹Ð¼Ð¸
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ð°Ð¼Ð¸
    // Ð·Ð°
    // Ñ€ÐµÐ°Ð»ÑŒÐ½Ñ‹Ðµ
    // Ð´ÐµÐ½ÑŒÐ³Ð¸.
    // Ð”Ð»Ñ�
    // Ð±Ð¾Ð»ÐµÐµ
    // Ð¿Ð¾Ð´Ñ€Ð¾Ð±Ð½Ð¾Ð¹
    // Ð¸Ð½Ñ„Ð¾Ñ€Ð¼Ð°Ñ†Ð¸Ð¸
    // Ð¾Ð±Ñ€Ð°Ñ‚Ð¸Ñ‚ÐµÑ�ÑŒ
    // Ð²
    // Ñ�Ð»ÑƒÐ¶Ð±Ñƒ
    // Ð¿Ð¾Ð´Ð´ÐµÑ€Ð¶ÐºÐ¸
    // Ð½Ð°
    // Ñ�Ð°Ð¹Ñ‚Ðµ
    // Ð¸Ð³Ñ€Ñ‹.
    public static final int S1_SECONDS_TO_GAME_END = 2347; // Ð”Ð¾ Ð·Ð°Ð²ÐµÑ€ÑˆÐµÐ½Ð¸Ñ� Ð¸Ð³Ñ€Ñ‹
    // $s1 Ñ�ÐµÐº!
    public static final int YOU_CANNOT_USE_MY_TELEPORTS_DURING_A_BATTLE = 2348; // Ð’Ð¾
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ð±Ð¾Ñ�
    // ÑƒÑ�Ñ‚Ð°Ð½Ð¾Ð²Ð¸Ñ‚ÑŒ
    // Ñ„Ð»Ð°Ð³
    // Ð´Ð»Ñ�
    // Ð¿Ð¾Ñ�Ð»ÐµÐ´ÑƒÑŽÑ‰ÐµÐ³Ð¾
    // Ð²Ð¾Ð·Ð²Ñ€Ð°Ñ‚Ð°
    // Ðº
    // Ð½ÐµÐ¼Ñƒ
    // Ð½ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾.
    public static final int YOU_CANNOT_USE_MY_TELEPORTS_WHILE_PARTICIPATING_A_LARGE_SCALE_BATTLE_SUCH_AS_A_CASTLE_SIEGE = 2349; // Ð’Ð¾
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ð¿Ð¾Ð»Ð½Ð¾Ð¼Ð°Ñ�ÑˆÑ‚Ð°Ð±Ð½Ñ‹Ñ…
    // Ñ�Ñ€Ð°Ð¶ÐµÐ½Ð¸Ð¹
    // -
    // Ð¾Ñ�Ð°Ð´
    // ÐºÑ€ÐµÐ¿Ð¾Ñ�Ñ‚ÐµÐ¹,
    // Ð·Ð°Ð¼ÐºÐ¾Ð²,
    // Ñ…Ð¾Ð»Ð»Ð¾Ð²
    // ÐºÐ»Ð°Ð½Ð°
    // -
    // ÑƒÑ�Ñ‚Ð°Ð½Ð¾Ð²Ð¸Ñ‚ÑŒ
    // Ñ„Ð»Ð°Ð³
    // Ð´Ð»Ñ�
    // Ð¿Ð¾Ñ�Ð»ÐµÐ´ÑƒÑŽÑ‰ÐµÐ³Ð¾
    // Ð²Ð¾Ð·Ð²Ñ€Ð°Ñ‚Ð°
    // Ðº
    // Ð½ÐµÐ¼Ñƒ
    // Ð½ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾.
    public static final int YOU_CANNOT_USE_MY_TELEPORTS_DURING_A_DUEL = 2350; // Ð’Ð¾
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ð´ÑƒÑ�Ð»Ð¸
    // ÑƒÑ�Ñ‚Ð°Ð½Ð¾Ð²Ð¸Ñ‚ÑŒ
    // Ñ„Ð»Ð°Ð³
    // Ð´Ð»Ñ�
    // Ð¿Ð¾Ñ�Ð»ÐµÐ´ÑƒÑŽÑ‰ÐµÐ³Ð¾
    // Ð²Ð¾Ð·Ð²Ñ€Ð°Ñ‚Ð°
    // Ðº
    // Ð½ÐµÐ¼Ñƒ
    // Ð½ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾.
    public static final int YOU_CANNOT_USE_MY_TELEPORTS_WHILE_FLYING = 2351; // Ð’Ð¾
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ð¿Ð¾Ð»ÐµÑ‚Ð°
    // Ð²Ð¾Ð·Ð²Ñ€Ð°Ñ‚
    // Ðº
    // Ñ„Ð»Ð°Ð³Ñƒ
    // Ð½ÐµÐ´Ð¾Ñ�Ñ‚ÑƒÐ¿ÐµÐ½.
    public static final int YOU_CANNOT_USE_MY_TELEPORTS_WHILE_PARTICIPATING_IN_AN_OLYMPIAD_MATCH = 2352; // Ð’Ð¾
    // Ð²Ñ€ÐµÐ¼Ñ�
    // ÐžÐ»Ð¸Ð¼Ð¿Ð¸Ð°Ð´Ñ‹
    // Ð²Ð¾Ð·Ð²Ñ€Ð°Ñ‚
    // Ðº
    // Ñ„Ð»Ð°Ð³Ñƒ
    // Ð½ÐµÐ´Ð¾Ñ�Ñ‚ÑƒÐ¿ÐµÐ½.
    public static final int YOU_CANNOT_USE_MY_TELEPORTS_WHILE_YOU_ARE_IN_A_FLINT_OR_PARALYZED_STATE = 2353; // Ð’
    // Ñ�Ð¾Ñ�Ñ‚Ð¾Ñ�Ð½Ð¸Ð¸
    // Ð¿Ð°Ñ€Ð°Ð»Ð¸Ñ‡Ð°
    // Ð¸Ð»Ð¸
    // Ð¾ÐºÐ°Ð¼ÐµÐ½ÐµÐ½Ð¸Ñ�
    // Ð²Ð¾Ð·Ð²Ñ€Ð°Ñ‚
    // Ðº
    // Ñ„Ð»Ð°Ð³Ñƒ
    // Ð½ÐµÐ´Ð¾Ñ�Ñ‚ÑƒÐ¿ÐµÐ½.
    public static final int YOU_CANNOT_USE_MY_TELEPORTS_WHILE_YOU_ARE_DEAD = 2354; // Ð•Ñ�Ð»Ð¸
    // Ð’Ð°Ñˆ
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶
    // ÑƒÐ¼ÐµÑ€,
    // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð²ÐµÑ€Ð½ÑƒÑ‚ÑŒÑ�Ñ�
    // Ðº
    // Ñ„Ð»Ð°Ð³Ñƒ.
    public static final int YOU_CANNOT_USE_MY_TELEPORTS_IN_THIS_AREA = 2355; // Ð’Ñ‹
    // Ð½Ð°Ñ…Ð¾Ð´Ð¸Ñ‚ÐµÑ�ÑŒ
    // Ð²
    // Ð»Ð¾ÐºÐ°Ñ†Ð¸Ð¸,
    // Ð½Ð°
    // ÐºÐ¾Ñ‚Ð¾Ñ€Ð¾Ð¹
    // Ð²Ð¾Ð·Ð²Ñ€Ð°Ñ‚
    // Ðº
    // Ñ„Ð»Ð°Ð³Ñƒ
    // Ð½ÐµÐ´Ð¾Ñ�Ñ‚ÑƒÐ¿ÐµÐ½.
    public static final int YOU_CANNOT_USE_MY_TELEPORTS_UNDERWATER = 2356; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð²ÐµÑ€Ð½ÑƒÑ‚ÑŒÑ�Ñ�
    // Ðº
    // Ñ„Ð»Ð°Ð³Ñƒ,
    // Ð½Ð°Ñ…Ð¾Ð´Ñ�Ñ�ÑŒ
    // Ð²
    // Ð²Ð¾Ð´Ðµ.
    public static final int YOU_CANNOT_USE_MY_TELEPORTS_IN_AN_INSTANT_ZONE = 2357; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð²ÐµÑ€Ð½ÑƒÑ‚ÑŒÑ�Ñ�
    // Ðº
    // Ñ„Ð»Ð°Ð³Ñƒ,
    // Ð½Ð°Ñ…Ð¾Ð´Ñ�Ñ�ÑŒ
    // Ð²Ð¾
    // Ð²Ñ€ÐµÐ¼ÐµÐ½Ð½Ð¾Ð¹
    // Ð·Ð¾Ð½Ðµ.
    public static final int YOU_HAVE_NO_SPACE_TO_SAVE_THE_TELEPORT_LOCATION = 2358; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // ÑƒÑ�Ñ‚Ð°Ð½Ð¾Ð²Ð¸Ñ‚ÑŒ
    // ÐµÑ‰Ðµ
    // Ð¾Ð´Ð¸Ð½
    // Ñ„Ð»Ð°Ð³
    // Ð´Ð»Ñ�
    // Ð²Ð¾Ð·Ð²Ñ€Ð°Ñ‚Ð°
    // Ðº
    // Ð½ÐµÐ¼Ñƒ.
    public static final int YOU_CANNOT_TELEPORT_BECAUSE_YOU_DO_NOT_HAVE_A_TELEPORT_ITEM = 2359; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð²ÐµÑ€Ð½ÑƒÑ‚ÑŒÑ�Ñ�
    // Ðº
    // Ñ„Ð»Ð°Ð³Ñƒ
    // Ð±ÐµÐ·
    // Ñ�Ð¾Ð¾Ñ‚Ð²ÐµÑ‚Ñ�Ñ‚Ð²ÑƒÑŽÑ‰ÐµÐ³Ð¾
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ð°.
    public static final int MY_TELEPORTS_SPELLBK__S1 = 2360; // Ð¡Ð²Ð¸Ñ‚Ð¾Ðº Ð’Ð¾Ð·Ð²Ñ€Ð°Ñ‚Ð°
    // Ðº Ð¤Ð»Ð°Ð³Ñƒ: $s1ÐµÐ´.
    public static final int CURRENT_LOCATION__S1 = 2361; // Ð¢ÐµÐºÑƒÑ‰Ð¸Ðµ ÐºÐ¾Ð¾Ñ€Ð´Ð¸Ð½Ð°Ñ‚Ñ‹:
    // $s1
    public static final int THE_SAVED_TELEPORT_LOCATION_WILL_BE_DELETED_DO_YOU_WISH_TO_CONTINUE = 2362; // Ð£Ð´Ð°Ð»ÐµÐ½Ð¸Ðµ
    // Ñ�Ð¾Ñ…Ñ€Ð°Ð½ÐµÐ½Ð½Ð¾Ð³Ð¾
    // Ð´Ð»Ñ�
    // Ñ‚ÐµÐ»ÐµÐ¿Ð¾Ñ€Ñ‚Ð°
    // Ð¼ÐµÑ�Ñ‚Ð°.
    // ÐŸÑ€Ð¾Ð´Ð¾Ð»Ð¶Ð¸Ñ‚ÑŒ?
    public static final int YOUR_ACCOUNT_HAS_BEEN_CONFIRMED_AS_USING_ANOTHER_PERSON_S_NAME_ALL_GAME_SERVICES_HAVE_BEEN = 2363; // Ð’Ð°Ñˆ
    // Ð°ÐºÐºÐ°ÑƒÐ½Ñ‚
    // Ð·Ð°Ð±Ð»Ð¾ÐºÐ¸Ñ€Ð¾Ð²Ð°Ð½,
    // Ñ‚Ð°Ðº
    // ÐºÐ°Ðº
    // Ð¾Ð½
    // Ð·Ð°Ñ€ÐµÐ³Ð¸Ñ�Ñ‚Ñ€Ð¸Ñ€Ð¾Ð²Ð°Ð½
    // Ð¿Ð¾Ð´
    // Ð´Ñ€ÑƒÐ³Ð¸Ð¼
    // Ð¸Ð¼ÐµÐ½ÐµÐ¼.
    // Ð—Ð°
    // Ð´Ð¾Ð¿Ð¾Ð»Ð½Ð¸Ñ‚ÐµÐ»ÑŒÐ½Ð¾Ð¹
    // Ð¸Ð½Ñ„Ð¾Ñ€Ð¼Ð°Ñ†Ð¸ÐµÐ¹
    // Ð¾Ð±Ñ€Ð°Ñ‰Ð°Ð¹Ñ‚ÐµÑ�ÑŒ
    // Ð²
    // Ñ�Ð»ÑƒÐ¶Ð±Ñƒ
    // Ð¿Ð¾Ð´Ð´ÐµÑ€Ð¶ÐºÐ¸.
    public static final int THE_ITEM_HAS_EXPIRED_AFTER_ITS_S1_PERIOD = 2364; // $s1:
    // Ð¿ÐµÑ€Ð¸Ð¾Ð´
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ð½Ð¸Ñ�
    // Ð¸Ñ�Ñ‚ÐµÐº.
    // ÐŸÑ€ÐµÐ´Ð¼ÐµÑ‚
    // Ð¸Ñ�Ñ‡ÐµÐ·.
    public static final int THE_DESIGNATED_ITEM_HAS_EXPIRED_AFTER_ITS_S1_PERIOD = 2365; // ÐŸÐµÑ€Ð¸Ð¾Ð´
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ð½Ð¸Ñ�
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ð°
    // Ð¸Ñ�Ñ‚ÐµÐº,
    // Ð¸
    // Ð¾Ð½
    // Ð¸Ñ�Ñ‡ÐµÐ·.
    public static final int S1_S_BLESSING_HAS_RECOVERED_HP_BY_S2 = 2367; // HP
    // ÑƒÐ²ÐµÐ»Ð¸Ñ‡Ð¸Ð²Ð°ÑŽÑ‚Ñ�Ñ�
    // Ð½Ð°
    // $s2
    // (Ð±Ð»Ð°Ð³Ð¾Ñ�Ð»Ð¾Ð²ÐµÐ½Ð¸Ðµ
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°
    // $s1).
    public static final int S1_S_BLESSING_HAS_RECOVERED_MP_BY_S2 = 2368; // MP
    // ÑƒÐ²ÐµÐ»Ð¸Ñ‡Ð¸Ð²Ð°ÑŽÑ‚Ñ�Ñ�
    // Ð½Ð°
    // $s2
    // (Ð±Ð»Ð°Ð³Ð¾Ñ�Ð»Ð¾Ð²ÐµÐ½Ð¸Ðµ
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°
    // $s1).
    public static final int S1_S_BLESSING_HAS_FULLY_RECOVERED_HP_AND_MP = 2369; // HP
    // Ð¸
    // MP
    // Ð¿Ð¾Ð»Ð½Ð¾Ñ�Ñ‚ÑŒÑŽ
    // Ð²Ð¾Ñ�Ñ�Ñ‚Ð°Ð½Ð¾Ð²Ð»ÐµÐ½Ñ‹
    // (Ð±Ð»Ð°Ð³Ð¾Ñ�Ð»Ð¾Ð²ÐµÐ½Ð¸Ðµ
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°
    // $s1).
    public static final int RESURRECTION_WILL_TAKE_PLACE_IN_THE_WAITING_ROOM_AFTER_S1_SECONDS = 2370; // Ð§ÐµÑ€ÐµÐ·
    // $s1
    // Ñ�ÐµÐº
    // Ð’Ñ‹
    // Ð±ÑƒÐ´ÐµÑ‚Ðµ
    // Ð²Ð¾Ñ�ÐºÑ€ÐµÑˆÐµÐ½Ñ‹
    // Ð²
    // ÐºÐ¾Ð¼Ð½Ð°Ñ‚Ðµ
    // Ð¾Ð¶Ð¸Ð´Ð°Ð½Ð¸Ñ�.
    public static final int C1_WAS_REPORTED_AS_A_BOT = 2371; // ÐŸÐ¾Ð´Ð°Ð½Ð° Ð¶Ð°Ð»Ð¾Ð±Ð°,
    // Ñ‡Ñ‚Ð¾ $c1
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·ÑƒÐµÑ‚ Ð±Ð¾Ñ‚Ð°.
    public static final int THERE_IS_NOT_MUCH_TIME_REMAINING_UNTIL_THE_HUNTING_HELPER_PET_LEAVES = 2372; // ÐŸÐ¸Ñ‚Ð¾Ð¼ÐµÑ†
    // Ñ�ÐºÐ¾Ñ€Ð¾
    // Ñ�Ð±ÐµÐ¶Ð¸Ñ‚.
    public static final int THE_HUNTING_HELPER_PET_IS_NOW_LEAVING = 2373; // ÐŸÐ¸Ñ‚Ð¾Ð¼ÐµÑ†
    // Ñ�Ð±ÐµÐ¶Ð°Ð»
    // Ð¾Ñ‚
    // Ð’Ð°Ñ�.
    public static final int END_MATCH = 2374; // Ð¡Ð¾Ñ€ÐµÐ²Ð½Ð¾Ð²Ð°Ð½Ð¸Ðµ Ð¾ÐºÐ¾Ð½Ñ‡ÐµÐ½Ð¾!
    public static final int THE_HUNTING_HELPER_PET_CANNOT_BE_RETURNED_BECAUSE_THERE_IS_NOT_MUCH_TIME_REMAINING_UNTIL_IT = 2375; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ð²ÐµÑ€Ð½ÑƒÑ‚ÑŒ
    // Ð¿Ð¸Ñ‚Ð¾Ð¼Ñ†Ð°.
    public static final int YOU_CANNOT_RECEIVE_A_VITAMIN_ITEM_DURING_AN_EXCHANGE = 2376; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ð¿Ð¾Ð»ÑƒÑ‡Ð¸Ñ‚ÑŒ
    // Ð’Ð¸Ñ‚Ð°Ð¼Ð¸Ð½
    // Ð²Ð¾
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ð¾Ð±Ð¼ÐµÐ½Ð°.
    public static final int YOU_CANNOT_REPORT_A_CHARACTER_WHO_IS_IN_A_PEACE_ZONE_OR_A_BATTLEFIELD = 2377; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ð¿Ð¾Ð´Ð°Ñ‚ÑŒ
    // Ð¶Ð°Ð»Ð¾Ð±Ñƒ
    // Ð½Ð°
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°,
    // Ð½Ð°Ñ…Ð¾Ð´Ñ�Ñ‰ÐµÐ³Ð¾Ñ�Ñ�
    // Ð²
    // Ð¼Ð¸Ñ€Ð½Ð¾Ð¹
    // Ð·Ð¾Ð½Ðµ
    // Ð¸Ð»Ð¸
    // Ð½Ð°
    // Ð¿Ð¾Ð»Ðµ
    // Ð±Ð¾Ñ�.
    public static final int YOU_CANNOT_REPORT_WHEN_A_CLAN_WAR_HAS_BEEN_DECLARED = 2378; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ð¿Ð¾Ð´Ð°Ð²Ð°Ñ‚ÑŒ
    // Ð¶Ð°Ð»Ð¾Ð±Ñƒ,
    // ÐµÑ�Ð»Ð¸
    // ÐºÐ»Ð°Ð½
    // Ð¾Ð±ÑŠÑ�Ð²Ð¸Ð»
    // Ð²Ð¾Ð¹Ð½Ñƒ.
    public static final int YOU_CANNOT_REPORT_A_CHARACTER_WHO_HAS_NOT_ACQUIRED_ANY_EXP_AFTER_CONNECTING = 2379; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ð¿Ð¾Ð´Ð°Ð²Ð°Ñ‚ÑŒ
    // Ð¶Ð°Ð»Ð¾Ð±Ñƒ
    // Ð½Ð°
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°,
    // ÐºÐ¾Ñ‚Ð¾Ñ€Ñ‹Ð¹
    // ÐµÑ‰Ðµ
    // Ð½Ðµ
    // Ð¿Ð¾Ð»ÑƒÑ‡Ð¸Ð»
    // Ð¾Ð¿Ñ‹Ñ‚
    // Ð¿Ð¾Ñ�Ð»Ðµ
    // Ð·Ð°Ñ…Ð¾Ð´Ð°
    // Ð²
    // Ð¸Ð³Ñ€Ñƒ.
    public static final int YOU_CANNOT_REPORT_THIS_PERSON_AGAIN_AT_THIS_TIME___CHARACTER = 2380; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ð¾Ñ‚Ð¿Ñ€Ð°Ð²Ð¸Ñ‚ÑŒ
    // Ð±Ð¾Ð»ÑŒÑˆÐµ
    // Ð¿Ð¾Ð»Ð¾Ð¶ÐµÐ½Ð½Ð¾Ð³Ð¾
    // ÐºÐ¾Ð»Ð¸Ñ‡ÐµÑ�Ñ‚Ð²Ð°
    // Ð¶Ð°Ð»Ð¾Ð±
    // Ñ�
    // Ð¾Ð´Ð½Ð¾Ð³Ð¾
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°.
    public static final int YOU_CANNOT_REPORT_THIS_PERSON_AGAIN_AT_THIS_TIME___ACCOUNT = 2381; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ð¾Ñ‚Ð¿Ñ€Ð°Ð²Ð¸Ñ‚ÑŒ
    // Ð±Ð¾Ð»ÑŒÑˆÐµ
    // Ð¿Ð¾Ð»Ð¾Ð¶ÐµÐ½Ð½Ð¾Ð³Ð¾
    // ÐºÐ¾Ð»Ð¸Ñ‡ÐµÑ�Ñ‚Ð²Ð°
    // Ð¶Ð°Ð»Ð¾Ð±
    // Ñ�
    // Ð¾Ð´Ð½Ð¾Ð³Ð¾
    // Ð°ÐºÐºÐ°ÑƒÐ½Ñ‚Ð°.
    public static final int YOU_CANNOT_REPORT_THIS_PERSON_AGAIN_AT_THIS_TIME___CLAN = 2382; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ð¾Ñ‚Ð¿Ñ€Ð°Ð²Ð¸Ñ‚ÑŒ
    // Ð±Ð¾Ð»ÑŒÑˆÐµ
    // Ð¿Ð¾Ð»Ð¾Ð¶ÐµÐ½Ð½Ð¾Ð³Ð¾
    // ÐºÐ¾Ð»Ð¸Ñ‡ÐµÑ�Ñ‚Ð²Ð°
    // Ð¶Ð°Ð»Ð¾Ð±
    // Ñ�
    // Ð¾Ð´Ð½Ð¾Ð³Ð¾
    // ÐºÐ»Ð°Ð½Ð°.
    public static final int YOU_CANNOT_REPORT_THIS_PERSON_AGAIN_AT_THIS_TIME___IP = 2383; // Ð�ÐµÐ»ÑŒÐ·Ñ�
    // Ð¾Ñ‚Ð¿Ñ€Ð°Ð²Ð¸Ñ‚ÑŒ
    // Ð±Ð¾Ð»ÑŒÑˆÐµ
    // Ð¿Ð¾Ð»Ð¾Ð¶ÐµÐ½Ð½Ð¾Ð³Ð¾
    // ÐºÐ¾Ð»Ð¸Ñ‡ÐµÑ�Ñ‚Ð²Ð°
    // Ð¶Ð°Ð»Ð¾Ð±
    // Ñ�
    // Ð¾Ð´Ð½Ð¾Ð³Ð¾
    // IP.
    public static final int THIS_ITEM_DOES_NOT_MEET_THE_REQUIREMENTS_FOR_THE_ENHANCEMENT_SPELLBOOK = 2384; // Ð­Ñ‚Ð¾Ñ‚
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚
    // Ð½Ðµ
    // Ð¿Ð¾Ð´Ñ…Ð¾Ð´Ð¸Ñ‚
    // Ð¿Ð¾
    // Ð¿Ñ€Ð°Ð²Ð¸Ð»Ð°Ð¼
    // Ð¡Ð²Ð¸Ñ‚ÐºÐ°
    // Ð—Ð°Ñ‡Ð°Ñ€Ð¾Ð²Ð°Ð½Ð¸Ñ�.
    public static final int THIS_IS_AN_INCORRECT_SUPPORT_ENHANCEMENT_SPELLBOOK = 2385; // Ð�ÐµÐ²ÐµÑ€Ð½Ñ‹Ð¹
    // Ð¡Ð²Ð¸Ñ‚Ð¾Ðº
    // Ð”Ð¾Ð¿Ð¾Ð»Ð½Ð¸Ñ‚ÐµÐ»ÑŒÐ½Ð¾Ð³Ð¾
    // Ð—Ð°Ñ‡Ð°Ñ€Ð¾Ð²Ð°Ð½Ð¸Ñ�.
    public static final int THIS_ITEM_DOES_NOT_MEET_THE_REQUIREMENTS_FOR_THE_SUPPORT_ENHANCEMENT_SPELLBOOK = 2386; // Ð­Ñ‚Ð¾Ñ‚
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚
    // Ð½Ðµ
    // Ð¿Ð¾Ð´Ñ…Ð¾Ð´Ð¸Ñ‚
    // Ð¿Ð¾
    // Ð¿Ñ€Ð°Ð²Ð¸Ð»Ð°Ð¼
    // Ð¡Ð²Ð¸Ñ‚ÐºÐ°
    // Ð”Ð¾Ð¿Ð¾Ð»Ð½Ð¸Ñ‚ÐµÐ»ÑŒÐ½Ð¾Ð³Ð¾
    // Ð—Ð°Ñ‡Ð°Ñ€Ð¾Ð²Ð°Ð½Ð¸Ñ�.
    public static final int REGISTRATION_OF_THE_SUPPORT_ENHANCEMENT_SPELLBOOK_HAS_FAILED = 2387; // Ð�Ðµ
    // ÑƒÐ´Ð°Ð»Ð¾Ñ�ÑŒ
    // Ð·Ð°Ñ€ÐµÐ³Ð¸Ñ�Ñ‚Ñ€Ð¸Ñ€Ð¾Ð²Ð°Ñ‚ÑŒ
    // Ð¡Ð²Ð¸Ñ‚Ð¾Ðº
    // Ð”Ð¾Ð¿Ð¾Ð»Ð½Ð¸Ñ‚ÐµÐ»ÑŒÐ½Ð¾Ð³Ð¾
    // Ð—Ð°Ñ‡Ð°Ñ€Ð¾Ð²Ð°Ð½Ð¸Ñ�.
    public static final int THE_MAXIMUM_ACCUMULATION_ALLOWED_OF_PC_CAFE_POINTS_HAS_BEEN_EXCEEDED_YOU_CAN_NO_LONGER_ACQUIRE = 2389; // ÐšÐ¾Ð»Ð¸Ñ‡ÐµÑ�Ñ‚Ð²Ð¾
    // Ð¾Ñ‡ÐºÐ¾Ð²
    // Ð˜Ð½Ñ‚ÐµÑ€Ð½ÐµÑ‚-ÐºÐ°Ñ„Ðµ
    // Ð¿Ñ€ÐµÐ²Ñ‹Ñ�Ð¸Ð»Ð¾
    // Ð»Ð¸Ð¼Ð¸Ñ‚.
    // Ð‘Ð¾Ð»ÑŒÑˆÐµ
    // Ð½ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾
    // Ð¿Ð¾Ð»ÑƒÑ‡Ð¸Ñ‚ÑŒ
    // Ð¾Ñ‡ÐºÐ¸.
    public static final int YOUR_NUMBER_OF_MY_TELEPORTS_SLOTS_HAS_REACHED_ITS_MAXIMUM_LIMIT = 2390; // Ð”Ð¾Ñ�Ñ‚Ð¸Ð³Ð½ÑƒÑ‚
    // Ð»Ð¸Ð¼Ð¸Ñ‚
    // Ñ�Ñ‡ÐµÐµÐº
    // Ð´Ð»Ñ�
    // Ð¤Ð»Ð°Ð³Ð¾Ð².
    // Ð£Ð²ÐµÐ»Ð¸Ñ‡Ð¸Ñ‚ÑŒ
    // Ñ�Ñ‚Ð¾
    // ÐºÐ¾Ð»Ð¸Ñ‡ÐµÑ�Ñ‚Ð²Ð¾
    // Ð½ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾.
    public static final int YOU_HAVE_USED_THE_FEATHER_OF_BLESSING_TO_RESURRECT = 2391; // Ð’Ñ‹
    // Ð²Ð¾Ñ�ÐºÑ€ÐµÑ�Ð»Ð¸
    // Ð±Ð»Ð°Ð³Ð¾Ð´Ð°Ñ€Ñ�
    // Ð‘Ð»Ð°Ð³Ð¾Ñ�Ð»Ð¾Ð²ÐµÐ½Ð½Ð¾Ð¼Ñƒ
    // ÐŸÐµÑ€Ñƒ.
    public static final int THE_VITAMIN_ITEM_CANNOT_BE_LOCATED_BECAUSE_OF_A_TEMPORARY_CONNECTION_ERROR = 2392; // Ð˜Ð·-Ð·Ð°
    // Ð²Ñ€ÐµÐ¼ÐµÐ½Ð½Ð¾Ð¹
    // Ð¾ÑˆÐ¸Ð±ÐºÐ¸
    // Ð¾Ð±Ð½Ð°Ñ€ÑƒÐ¶ÐµÐ½Ð¸Ðµ
    // Ð’Ð¸Ñ‚Ð°Ð¼Ð¸Ð½Ð¾Ð²
    // Ð½ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾.
    public static final int YOU_HAVE_ACQUIRED_S1_PC_CAFE_POINTS = 2393; // Ð’Ñ‹
    // Ð¿Ð¾Ð»ÑƒÑ‡Ð¸Ð»Ð¸
    // ÐµÐ¶ÐµÐ´Ð½ÐµÐ²Ð½Ñ‹Ðµ
    // Ð¾Ñ‡ÐºÐ¸
    // Ð·Ð°
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ð½Ð¸Ðµ
    // Ð Ð¡-ÐºÐ»ÑƒÐ±Ð°
    // Ð²
    // Ñ€Ð°Ð·Ð¼ÐµÑ€Ðµ
    // $s1
    // Ð¾Ñ‡ÐºÐ¾Ð².
    public static final int THAT_SKILL_CANNOT_BE_USED_BECAUSE_YOUR_PET_SERVITOR_LACKS_SUFFICIENT_MP = 2394; // Ð£
    // Ð¿Ð¸Ñ‚Ð¾Ð¼Ñ†Ð°/Ñ�Ð»ÑƒÐ³Ð¸
    // Ð½Ðµ
    // Ñ…Ð²Ð°Ñ‚Ð°ÐµÑ‚
    // ÐœÐ 
    // Ð´Ð»Ñ�
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ð½Ð¸Ðµ
    // Ð´Ð°Ð½Ð½Ð¾Ð³Ð¾
    // ÑƒÐ¼ÐµÐ½Ð¸Ñ�.
    public static final int THAT_SKILL_CANNOT_BE_USED_BECAUSE_YOUR_PET_SERVITOR_LACKS_SUFFICIENT_HP = 2395; // Ð£
    // Ð¿Ð¸Ñ‚Ð¾Ð¼Ñ†Ð°/Ñ�Ð»ÑƒÐ³Ð¸
    // Ð½Ðµ
    // Ñ…Ð²Ð°Ñ‚Ð°ÐµÑ‚
    // Ð�Ð 
    // Ð´Ð»Ñ�
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ð½Ð¸Ðµ
    // Ð´Ð°Ð½Ð½Ð¾Ð³Ð¾
    // ÑƒÐ¼ÐµÐ½Ð¸Ñ�.
    public static final int THAT_PET_SERVITOR_SKILL_CANNOT_BE_USED_BECAUSE_IT_IS_RECHARGING = 2396; // ÐŸÐ¸Ñ‚Ð¾Ð¼ÐµÑ†/Ñ�Ð»ÑƒÐ³Ð°
    // ÐµÑ‰Ðµ
    // Ð½Ðµ
    // Ð³Ð¾Ñ‚Ð¾Ð²
    // Ð¿Ð¾Ð²Ñ‚Ð¾Ñ€Ð¸Ñ‚ÑŒ
    // ÑƒÐ¼ÐµÐ½Ð¸Ðµ.
    public static final int PLEASE_USE_A_MY_TELEPORT_SCROLL = 2397; // Ð’Ð¾Ñ�Ð¿Ð¾Ð»ÑŒÐ·ÑƒÐ¹Ñ‚ÐµÑ�ÑŒ
    // ÐšÐ½Ð¸Ð³Ð¾Ð¹
    // Ð’Ð¾Ð·Ð²Ñ€Ð°Ñ‚Ð°
    // Ðº Ð¤Ð»Ð°Ð³Ñƒ.
    public static final int YOU_HAVE_NO_OPEN_MY_TELEPORTS_SLOTS = 2398; // Ð”Ð°Ð½Ð½Ð°Ñ�
    // Ñ�Ñ‡ÐµÐ¹ÐºÐ°
    // Ð½Ðµ
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·ÑƒÐµÑ‚Ñ�Ñ�.
    public static final int S1_S_OWNERSHIP_EXPIRES_IN_S2_MINUTES = 2399; // Ð’Ñ€ÐµÐ¼Ñ�
    // Ð´ÐµÐ¹Ñ�Ñ‚Ð²Ð¸Ñ�
    // $s1
    // Ð·Ð°Ð²ÐµÑ€ÑˆÐ¸Ñ‚Ñ�Ñ�
    // Ñ‡ÐµÑ€ÐµÐ·
    // $s2
    // Ð¼Ð¸Ð½.
    public static final int INSTANT_ZONE_CURRENTLY_IN_USE__S1 = 2400; // Ð¢ÐµÐºÑƒÑ‰Ð°Ñ�
    // Ð²Ñ€ÐµÐ¼ÐµÐ½Ð½Ð°Ñ�
    // Ð·Ð¾Ð½Ð°:
    // $s1
    public static final int CLAN_LORD_C2_WHO_LEADS_CLAN_S1_HAS_BEEN_DECLARED_THE_LORD_OF_THE_S3_TERRITORY = 2401; // Ð“Ð»Ð°Ð²Ð°
    // ÐºÐ»Ð°Ð½Ð°
    // $s1,
    // $c2,
    // Ð±Ñ‹Ð»
    // Ð¸Ð·Ð±Ñ€Ð°Ð½
    // ÐŸÑ€Ð°Ð²Ð¸Ñ‚ÐµÐ»ÐµÐ¼
    // Ð—ÐµÐ¼ÐµÐ»ÑŒ
    // $s3Ð°.
    public static final int THE_TERRITORY_WAR_REQUEST_PERIOD_HAS_ENDED = 2402; // Ð’Ñ€ÐµÐ¼Ñ�
    // Ð¿Ð¾Ð´Ð°Ñ‡Ð¸
    // Ð·Ð°Ñ�Ð²Ð¾Ðº
    // Ð½Ð°
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð¸Ðµ
    // Ð²
    // Ð‘Ð¸Ñ‚Ð²Ðµ
    // Ð·Ð°
    // Ð—ÐµÐ¼Ð»Ð¸
    // Ð¸Ñ�Ñ‚ÐµÐºÐ»Ð¾.
    public static final int TERRITORY_WAR_BEGINS_IN_10_MINUTES = 2403; // Ð”Ð¾
    // Ð½Ð°Ñ‡Ð°Ð»Ð°
    // Ð‘Ð¸Ñ‚Ð²Ñ‹
    // Ð·Ð°
    // Ð—ÐµÐ¼Ð»Ð¸
    // Ð¾Ñ�Ñ‚Ð°Ð»Ð¾Ñ�ÑŒ
    // 10Ð¼Ð¸Ð½!
    public static final int TERRITORY_WAR_BEGINS_IN_5_MINUTES = 2404; // Ð”Ð¾
    // Ð½Ð°Ñ‡Ð°Ð»Ð°
    // Ð‘Ð¸Ñ‚Ð²Ñ‹
    // Ð·Ð°
    // Ð—ÐµÐ¼Ð»Ð¸
    // Ð¾Ñ�Ñ‚Ð°Ð»Ð¾Ñ�ÑŒ
    // 5Ð¼Ð¸Ð½!
    public static final int TERRITORY_WAR_BEGINS_IN_1_MINUTE = 2405; // Ð”Ð¾
    // Ð½Ð°Ñ‡Ð°Ð»Ð°
    // Ð‘Ð¸Ñ‚Ð²Ñ‹ Ð·Ð°
    // Ð—ÐµÐ¼Ð»Ð¸
    // Ð¾Ñ�Ñ‚Ð°Ð»Ð¾Ñ�ÑŒ
    // 1Ð¼Ð¸Ð½!
    public static final int S1_S_TERRITORY_WAR_HAS_BEGUN = 2406; // Ð‘Ð¸Ñ‚Ð²Ð° Ð·Ð°
    // Ð—ÐµÐ¼Ð»Ð¸ $s1
    // Ð½Ð°Ñ‡Ð°Ð»Ð°Ñ�ÑŒ.
    public static final int S1_S_TERRITORY_WAR_HAS_ENDED = 2407; // Ð‘Ð¸Ñ‚Ð²Ð° Ð·Ð°
    // Ð—ÐµÐ¼Ð»Ð¸ $s1
    // Ð·Ð°Ð²ÐµÑ€ÑˆÐµÐ½Ð°.
    public static final int YOU_HAVE_REGISTERED_ON_THE_WAITING_LIST_FOR_THE_NON_CLASS_LIMITED_TEAM_MATCH_EVENT = 2408; // Ð’Ñ‹
    // Ð´Ð¾Ð±Ð°Ð²Ð»ÐµÐ½Ñ‹
    // Ð²
    // Ñ�Ð¿Ð¸Ñ�Ð¾Ðº
    // Ð¾Ð¶Ð¸Ð´Ð°Ð½Ð¸Ñ�
    // Ð¸Ð³Ñ€Ñ‹
    // Ð±ÐµÐ·
    // Ð¾Ð³Ñ€Ð°Ð½Ð¸Ñ‡ÐµÐ½Ð¸Ñ�
    // Ð¿Ð¾
    // ÐºÐ»Ð°Ñ�Ñ�Ð°Ð¼.
    public static final int THE_NUMBER_OF_MY_TELEPORTS_SLOTS_HAS_BEEN_INCREASED = 2409; // ÐšÐ¾Ð»Ð¸Ñ‡ÐµÑ�Ñ‚Ð²Ð¾
    // Ñ�Ñ‡ÐµÐµÐº
    // Ð´Ð»Ñ�
    // Ð²Ð¾Ð·Ð²Ñ€Ð°Ñ‚Ð°
    // Ðº
    // Ñ„Ð»Ð°Ð³Ñƒ
    // ÑƒÐ²ÐµÐ»Ð¸Ñ‡ÐµÐ½Ð¾.
    public static final int YOU_CANNOT_USE_MY_TELEPORTS_TO_REACH_THIS_AREA = 2410; // ÐŸÐµÑ€ÐµÐ¼ÐµÑ�Ñ‚Ð¸Ñ‚ÑŒÑ�Ñ�
    // Ð½Ð°
    // Ð²Ñ‹Ð±Ñ€Ð°Ð½Ð½ÑƒÑŽ
    // Ð¼ÐµÑ�Ñ‚Ð½Ð¾Ñ�Ñ‚ÑŒ
    // Ñ�
    // Ð¿Ð¾Ð¼Ð¾Ñ‰ÑŒÑŽ
    // Ð²Ð¾Ð·Ð²Ñ€Ð°Ñ‚Ð°
    // Ðº
    // Ñ„Ð»Ð°Ð³Ñƒ
    // Ð½ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾.
    public static final int C1_HAS_ISSUED_A_PARTY_INVITATION_WHICH_YOU_AUTOMATICALLY_REJECTED_TO_RECEIVE_PARTY_INVITATIONS = 2411; // Ð’Ñ‹
    // Ð¿Ð¾Ð»ÑƒÑ‡Ð¸Ð»Ð¸
    // Ð¿Ñ€Ð¸Ð³Ð»Ð°ÑˆÐµÐ½Ð¸Ðµ
    // Ð²
    // Ñ�Ñ‚ÑƒÐ¿Ð¸Ñ‚ÑŒ
    // Ð²
    // Ð³Ñ€ÑƒÐ¿Ð¿Ñƒ
    // Ð¾Ñ‚
    // $c1,
    // Ð½Ð¾
    // Ñƒ
    // Ð’Ð°Ñ�
    // ÑƒÑ�Ñ‚Ð°Ð½Ð¾Ð²Ð»ÐµÐ½Ð°
    // Ð±Ð»Ð¾ÐºÐ¸Ñ€Ð¾Ð²ÐºÐ°
    // Ð¿Ð¾Ð»ÑƒÑ‡ÐµÐ½Ð¸Ñ�
    // Ð¿Ñ€Ð¸Ð³Ð»Ð°ÑˆÐµÐ½Ð¸Ð¹
    // Ð²
    // Ð³Ñ€ÑƒÐ¿Ð¿Ñƒ.
    // Ð§Ñ‚Ð¾Ð±Ñ‹
    // Ð¿Ð¾Ð»ÑƒÑ‡Ð¸Ñ‚ÑŒ
    // Ð¿Ñ€Ð¸Ð³Ð»Ð°ÑˆÐµÐ½Ð¸Ðµ,
    // Ð¸Ð·Ð¼ÐµÐ½Ð¸Ñ‚Ðµ
    // Ð½Ð°Ñ�Ñ‚Ñ€Ð¾Ð¹ÐºÐ¸.
    public static final int THE_BIRTHDAY_GIFT_HAS_BEEN_DELIVERED_VISIT_THE_VITAMIN_MANAGER_IN_ANY_VILLAGE_TO_OBTAIN_IT = 2412; // Ð’Ñ‹
    // Ð¿Ð¾Ð»ÑƒÑ‡Ð¸Ð»Ð¸
    // Ð¿Ð¾Ð´Ð°Ñ€Ð¾Ðº
    // Ð½Ð°
    // Ð”ÐµÐ½ÑŒ
    // Ð Ð¾Ð¶Ð´ÐµÐ½Ð¸Ñ�.
    // ÐŸÐ¾Ð»ÑƒÑ‡Ð¸Ñ‚ÑŒ
    // ÐµÐ³Ð¾
    // Ð¼Ð¾Ð¶Ð½Ð¾
    // Ñƒ
    // Ð¼ÐµÐ½ÐµÐ´Ð¶ÐµÑ€Ð°
    // Ð²Ð¸Ñ‚Ð°Ð¼Ð¸Ð½Ð¾Ð².
    public static final int YOU_ARE_REGISTERING_AS_A_RESERVE_ON_THE_AERIAL_CLEFT_RED_TEAM_S_BATTLEFIELD_DO_YOU_WISH_TO = 2413; // Ð ÐµÐ³Ð¸Ñ�Ñ‚Ñ€Ð°Ñ†Ð¸Ñ�
    // Ð½Ð°
    // Ð±Ð¸Ñ‚Ð²Ñƒ
    // Ð·Ð°
    // Ð£Ñ‰ÐµÐ»ÑŒÐµ
    // Ð½Ð°
    // Ñ�Ñ‚Ð¾Ñ€Ð¾Ð½Ðµ
    // ÐºÑ€Ð°Ñ�Ð½Ð¾Ð¹
    // ÐºÐ¾Ð¼Ð°Ð½Ð´Ñ‹.
    // ÐŸÑ€Ð¾Ð´Ð¾Ð»Ð¶Ð¸Ñ‚ÑŒ?
    public static final int YOU_ARE_REGISTERING_AS_A_RESERVE_ON_THE_AERIAL_CLEFT_BLUE_TEAM_S_BATTLEFIELD_DO_YOU_WISH_TO = 2414; // Ð ÐµÐ³Ð¸Ñ�Ñ‚Ñ€Ð°Ñ†Ð¸Ñ�
    // Ð½Ð°
    // Ð±Ð¸Ñ‚Ð²Ñƒ
    // Ð·Ð°
    // Ð£Ñ‰ÐµÐ»ÑŒÐµ
    // Ð½Ð°
    // Ñ�Ñ‚Ð¾Ñ€Ð¾Ð½Ðµ
    // Ñ�Ð¸Ð½ÐµÐ¹
    // ÐºÐ¾Ð¼Ð°Ð½Ð´Ñ‹.
    // ÐŸÑ€Ð¾Ð´Ð¾Ð»Ð¶Ð¸Ñ‚ÑŒ?
    public static final int YOU_HAVE_REGISTERED_AS_A_RESERVE_ON_THE_AERIAL_CLEFT_RED_TEAM_S_BATTLEFIELD_WHEN_IN_BATTLE_THE = 2415; // Ð’Ñ‹
    // Ð²Ñ�Ñ‚ÑƒÐ¿Ð¸Ð»Ð¸
    // Ð²
    // ÐºÑ€Ð°Ñ�Ð½ÑƒÑŽ
    // ÐºÐ¾Ð¼Ð°Ð½Ð´Ñƒ.
    // Ð’
    // Ñ‚ÐµÑ‡ÐµÐ½Ð¸Ðµ
    // Ð±Ð¸Ñ‚Ð²Ñ‹
    // ÐºÐ¾Ð¼Ð°Ð½Ð´Ð°
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð¸Ð·Ð¼ÐµÐ½ÐµÐ½Ð°
    // Ð´Ð»Ñ�
    // Ð¿Ð¾Ð´Ð´ÐµÑ€Ð¶Ð°Ð½Ð¸Ñ�
    // Ð±Ð°Ð»Ð°Ð½Ñ�Ð°
    // Ð¸Ð³Ñ€Ñ‹.
    public static final int YOU_HAVE_REGISTERED_AS_A_RESERVE_ON_THE_AERIAL_CLEFT_BLUE_TEAM_S_BATTLEFIELD_WHEN_IN_BATTLE_THE = 2416; // Ð’Ñ‹
    // Ð²Ñ�Ñ‚ÑƒÐ¿Ð¸Ð»Ð¸
    // Ð²
    // Ñ�Ð¸Ð½ÑŽÑŽ
    // ÐºÐ¾Ð¼Ð°Ð½Ð´Ñƒ.
    // Ð’
    // Ñ‚ÐµÑ‡ÐµÐ½Ð¸Ðµ
    // Ð±Ð¸Ñ‚Ð²Ñ‹
    // ÐºÐ¾Ð¼Ð°Ð½Ð´Ð°
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð¸Ð·Ð¼ÐµÐ½ÐµÐ½Ð°
    // Ð´Ð»Ñ�
    // Ð¿Ð¾Ð´Ð´ÐµÑ€Ð¶Ð°Ð½Ð¸Ñ�
    // Ð±Ð°Ð»Ð°Ð½Ñ�Ð°
    // Ð¸Ð³Ñ€Ñ‹.
    public static final int YOU_ARE_CANCELING_THE_AERIAL_CLEFT_BATTLEFIELD_REGISTRATION_DO_YOU_WISH_TO_CONTINUE = 2417; // ÐžÑ‚Ð¼ÐµÐ½Ð°
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð¸Ñ�
    // Ð²
    // Ð±Ð¸Ñ‚Ð²Ðµ
    // Ð·Ð°
    // Ð£Ñ‰ÐµÐ»ÑŒÐµ.
    // ÐŸÑ€Ð¾Ð´Ð¾Ð»Ð¶Ð¸Ñ‚ÑŒ?
    public static final int THE_AERIAL_CLEFT_BATTLEFIELD_REGISTRATION_HAS_BEEN_CANCELED = 2418; // Ð’Ñ‹
    // Ð¾Ñ‚ÐºÐ°Ð·Ð°Ð»Ð¸Ñ�ÑŒ
    // Ð¾Ñ‚
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð¸Ñ�
    // Ð²
    // Ð±Ð¸Ñ‚Ð²Ðµ
    // Ð·Ð°
    // Ð£Ñ‰ÐµÐ»ÑŒÐµ.
    public static final int THE_AERIAL_CLEFT_BATTLEFIELD_HAS_BEEN_ACTIVATED_FLIGHT_TRANSFORMATION_WILL_BE_POSSIBLE_IN = 2419; // Ð�ÐºÑ‚Ð¸Ð²Ð¸Ñ€Ð¾Ð²Ð°Ð½Ð¾
    // Ð¿Ð¾Ð»Ðµ
    // Ð±Ð¸Ñ‚Ð²Ñ‹
    // Ð·Ð°
    // Ð£Ñ‰ÐµÐ»ÑŒÐµ.
    // Ð§ÐµÑ€ÐµÐ·
    // 40
    // Ñ�ÐµÐº.
    // Ñ�Ñ‚Ð°Ð½ÐµÑ‚
    // Ð²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ñ‹Ð¼
    // Ð¿ÐµÑ€ÐµÐ²Ð¾Ð¿Ð»Ð¾Ñ‰ÐµÐ½Ð¸Ðµ
    // Ð²
    // Ð»ÐµÑ‚Ð°ÑŽÑ‰ÐµÐµ
    // Ñ�ÑƒÑ‰ÐµÑ�Ñ‚Ð²Ð¾.
    public static final int THE_BATTLEFIELD_CLOSES_IN_1_MINUTE = 2420; // Ð‘Ð¸Ñ‚Ð²Ð°
    // Ð·Ð°Ð²ÐµÑ€ÑˆÐ¸Ñ‚Ñ�Ñ�
    // Ñ‡ÐµÑ€ÐµÐ·
    // 1Ð¼Ð¸Ð½.
    public static final int THE_BATTLEFIELD_CLOSES_IN_10_SECONDS = 2421; // Ð‘Ð¸Ñ‚Ð²Ð°
    // Ð·Ð°Ð²ÐµÑ€ÑˆÐ¸Ñ‚Ñ�Ñ�
    // Ñ‡ÐµÑ€ÐµÐ·
    // 10Ð¼Ð¸Ð½.
    public static final int EP_OR_ENERGY_POINTS_REFERS_TO_FUEL = 2422; // EP
    // Ð¾Ð·Ð½Ð°Ñ‡Ð°ÐµÑ‚
    // Ð¾Ñ‡ÐºÐ¸
    // Ñ�Ð½ÐµÑ€Ð³Ð¸Ð¸
    // -
    // Ð·Ð°Ð¿Ð°Ñ�
    // Ñ‚Ð¾Ð¿Ð»Ð¸Ð²Ð°.
    public static final int EP_CAN_BE_REFILLED_BY_USING_A_S1_WHILE_SAILING_ON_AN_AIRSHIP = 2423; // Ð•Ð 
    // Ð¼Ð¾Ð¶Ð½Ð¾
    // Ð²Ð¾Ñ�Ñ�Ñ‚Ð°Ð½Ð¾Ð²Ð¸Ñ‚ÑŒ
    // Ñ�
    // Ð¿Ð¾Ð¼Ð¾Ñ‰ÑŒÑŽ
    // $s1,
    // Ð½Ð°Ñ…Ð¾Ð´Ñ�Ñ�ÑŒ
    // Ð²Ð½ÑƒÑ‚Ñ€Ð¸
    // Ð»ÐµÑ‚Ð°ÑŽÑ‰ÐµÐ³Ð¾
    // Ð¾Ð±ÑŠÐµÐºÑ‚Ð°.
    public static final int THE_COLLECTION_HAS_FAILED = 2424; // Ð’Ñ‹ Ð½Ðµ Ñ�Ð¼Ð¾Ð³Ð»Ð¸
    // Ð¿Ð¾Ð»ÑƒÑ‡Ð¸Ñ‚ÑŒ
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚.
    public static final int THE_AERIAL_CLEFT_BATTLEFIELD_HAS_BEEN_CLOSED = 2425; // Ð‘Ð¸Ñ‚Ð²Ð°
    // Ð·Ð°
    // Ð£Ñ‰ÐµÐ»ÑŒÐµ
    // Ð·Ð°Ð²ÐµÑ€ÑˆÐµÐ½Ð°.
    public static final int C1_HAS_BEEN_EXPELLED_FROM_THE_TEAM = 2426; // ÐŸÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶
    // $c1
    // Ð¿Ð¾ÐºÐ¸Ð½ÑƒÐ»
    // ÐºÐ¾Ð¼Ð°Ð½Ð´Ñƒ.
    public static final int THE_RED_TEAM_IS_VICTORIOUS = 2427; // ÐŸÐ¾Ð±ÐµÐ´Ð¸Ð»Ð°
    // ÐºÑ€Ð°Ñ�Ð½Ð°Ñ�
    // ÐºÐ¾Ð¼Ð°Ð½Ð´Ð°.
    public static final int THE_BLUE_TEAM_IS_VICTORIOUS = 2428; // ÐŸÐ¾Ð±ÐµÐ´Ð¸Ð»Ð°
    // Ñ�Ð¸Ð½Ñ�Ñ�
    // ÐºÐ¾Ð¼Ð°Ð½Ð´Ð°.
    public static final int C1_HAS_BEEN_DESIGNATED_AS_THE_TARGET = 2429; // ÐŸÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶
    // $c1
    // Ð²Ñ‹Ð±Ñ€Ð°Ð½
    // Ð³Ð»Ð°Ð²Ð½Ð¾Ð¹
    // Ñ†ÐµÐ»ÑŒÑŽ
    // Ð°Ñ‚Ð°ÐºÐ¸.
    public static final int C1_HAS_FALLEN_THE_RED_TEAM_S_POINTS_HAVE_INCREASED = 2430; // ÐŸÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶
    // $c1
    // Ð¿Ð¾Ñ‚ÐµÑ€Ð¿ÐµÐ»
    // ÐºÑ€ÑƒÑˆÐµÐ½Ð¸Ðµ.
    // ÐšÑ€Ð°Ñ�Ð½Ð°Ñ�
    // ÐºÐ¾Ð¼Ð°Ð½Ð´Ð°
    // Ð¿Ð¾Ð»ÑƒÑ‡Ð°ÐµÑ‚
    // Ð¾Ñ‡ÐºÐ¸.
    public static final int C2_HAS_FALLEN_THE_BLUE_TEAM_S_POINTS_HAVE_INCREASED = 2431; // ÐŸÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶
    // $c2
    // Ð¿Ð¾Ñ‚ÐµÑ€Ð¿ÐµÐ»
    // ÐºÑ€ÑƒÑˆÐµÐ½Ð¸Ðµ.
    // Ð¡Ð¸Ð½Ñ�Ñ�
    // ÐºÐ¾Ð¼Ð°Ð½Ð´Ð°
    // Ð¿Ð¾Ð»ÑƒÑ‡Ð°ÐµÑ‚
    // Ð¾Ñ‡ÐºÐ¸.
    public static final int THE_CENTRAL_STRONGHOLD_S_COMPRESSOR_HAS_BEEN_DESTROYED = 2432; // Ð¡Ñ€ÐµÐ´Ð¸Ð½Ð½Ñ‹Ð¹
    // Ð˜Ñ�Ð¿Ð°Ñ€Ð¸Ñ‚ÐµÐ»ÑŒ
    // Ñ€Ð°Ð·Ñ€ÑƒÑˆÐµÐ½.
    public static final int THE_FIRST_STRONGHOLD_S_COMPRESSOR_HAS_BEEN_DESTROYED = 2433; // 1-Ð¹
    // Ð˜Ñ�Ð¿Ð°Ñ€Ð¸Ñ‚ÐµÐ»ÑŒ
    // Ñ€Ð°Ð·Ñ€ÑƒÑˆÐµÐ½.
    public static final int THE_SECOND_STRONGHOLD_S_COMPRESSOR_HAS_BEEN_DESTROYED = 2434; // 2-Ð¹
    // Ð˜Ñ�Ð¿Ð°Ñ€Ð¸Ñ‚ÐµÐ»ÑŒ
    // Ñ€Ð°Ð·Ñ€ÑƒÑˆÐµÐ½.
    public static final int THE_THIRD_STRONGHOLD_S_COMPRESSOR_HAS_BEEN_DESTROYED = 2435; // 3-Ð¹
    // Ð˜Ñ�Ð¿Ð°Ñ€Ð¸Ñ‚ÐµÐ»ÑŒ
    // Ñ€Ð°Ð·Ñ€ÑƒÑˆÐµÐ½.
    public static final int THE_CENTRAL_STRONGHOLD_S_COMPRESSOR_IS_WORKING = 2436; // Ð¡Ñ€ÐµÐ´Ð¸Ð½Ð½Ñ‹Ð¹
    // Ð˜Ñ�Ð¿Ð°Ñ€Ð¸Ñ‚ÐµÐ»ÑŒ
    // Ð·Ð°Ð¿ÑƒÑ‰ÐµÐ½.
    public static final int THE_FIRST_STRONGHOLD_S_COMPRESSOR_IS_WORKING = 2437; // 1-Ð¹
    // Ð˜Ñ�Ð¿Ð°Ñ€Ð¸Ñ‚ÐµÐ»ÑŒ
    // Ð·Ð°Ð¿ÑƒÑ‰ÐµÐ½
    public static final int THE_SECOND_STRONGHOLD_S_COMPRESSOR_IS_WORKING = 2438; // 2-Ð¹
    // Ð˜Ñ�Ð¿Ð°Ñ€Ð¸Ñ‚ÐµÐ»ÑŒ
    // Ð·Ð°Ð¿ÑƒÑ‰ÐµÐ½
    public static final int THE_THIRD_STRONGHOLD_S_COMPRESSOR_IS_WORKING = 2439; // 3-Ð¹
    // Ð˜Ñ�Ð¿Ð°Ñ€Ð¸Ñ‚ÐµÐ»ÑŒ
    // Ð·Ð°Ð¿ÑƒÑ‰ÐµÐ½
    public static final int C1_IS_ALREADY_REGISTERED_ON_THE_WAITING_LIST_FOR_THE_NON_CLASS_LIMITED_MATCH_EVENT = 2440; // $c1
    // Ð½Ð°Ñ…Ð¾Ð´Ð¸Ñ‚Ñ�Ñ�
    // Ð²
    // Ñ�Ð¿Ð¸Ñ�ÐºÐµ
    // Ð¾Ð¶Ð¸Ð´Ð°ÑŽÑ‰Ð¸Ñ…
    // Ð½Ð°
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð¸Ðµ
    // Ð²
    // Ð¸Ð³Ñ€Ðµ
    // Ð±ÐµÐ·
    // Ð¾Ð³Ñ€Ð°Ð½Ð¸Ñ‡ÐµÐ½Ð¸Ñ�
    // Ð¿Ð¾
    // ÐºÐ»Ð°Ñ�Ñ�Ð°Ð¼.
    public static final int ONLY_A_PARTY_LEADER_CAN_REQUEST_A_TEAM_MATCH = 2441; // ÐŸÐ¾Ð´Ð°Ñ‚ÑŒ
    // Ð·Ð°Ñ�Ð²ÐºÑƒ
    // Ð½Ð°
    // ÐºÐ¾Ð¼Ð°Ð½Ð´Ð½Ñ‹Ðµ
    // Ñ�Ð¾Ñ€ÐµÐ²Ð½Ð¾Ð²Ð°Ð½Ð¸Ñ�
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ñ‚Ð¾Ð»ÑŒÐºÐ¾
    // Ð›Ð¸Ð´ÐµÑ€
    // Ð“Ñ€ÑƒÐ¿Ð¿Ñ‹.
    public static final int THE_REQUEST_CANNOT_BE_MADE_BECAUSE_THE_REQUIREMENTS_HAVE_NOT_BEEN_MET_TO_PARTICIPATE_IN_A_TEAM = 2442; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¿Ð¾Ð´Ñ…Ð¾Ð´Ð¸Ñ‚Ðµ
    // Ð¿Ð¾Ð´
    // ÑƒÑ�Ð»Ð¾Ð²Ð¸Ñ�
    // Ñ�Ð¾Ñ€ÐµÐ²Ð½Ð¾Ð²Ð°Ð½Ð¸Ð¹.
    // Ð”Ð»Ñ�
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð¸Ñ�
    // Ð²
    // Ñ�Ð¾Ñ€ÐµÐ²Ð½Ð¾Ð²Ð°Ð½Ð¸Ñ�Ñ…
    // Ð²
    // Ð³Ñ€ÑƒÐ¿Ð¿Ðµ
    // Ð´Ð¾Ð»Ð¶Ð½Ð¾
    // Ñ�Ð¾Ñ�Ñ‚Ð¾Ñ�Ñ‚ÑŒ
    // 3
    // Ñ‡ÐµÐ»Ð¾Ð²ÐµÐºÐ°.
    public static final int FLAMES_FILLED_WITH_THE_WRATH_OF_VALAKAS_ARE_ENGULFING_YOU = 2443; // Ð“Ð½ÐµÐ²
    // Ð’Ð°Ð»Ð°ÐºÐ°Ñ�Ð°
    // Ð½Ð°Ð¿Ñ€Ð°Ð²Ð»ÐµÐ½
    // Ð½Ð°
    // Ð’Ð°Ñ�.
    public static final int FLAMES_FILLED_WITH_THE_AUTHORITY_OF_VALAKAS_ARE_BINDING_YOUR_MIND = 2444; // ÐŸÐ»Ð°Ð¼Ñ�
    // Ð’Ð°Ð»Ð°ÐºÐ°Ñ�Ð°
    // Ñ�Ð¾ÐºÑ€ÑƒÑˆÐ¸Ð»Ð¾
    // Ð’Ð°ÑˆÐµ
    // Ñ�Ð¾Ð·Ð½Ð°Ð½Ð¸Ðµ.
    public static final int THE_BATTLEFIELD_CHANNEL_HAS_BEEN_ACTIVATED = 2445; // ÐšÐ°Ð½Ð°Ð»
    // Ð±Ð¸Ñ‚Ð²Ñ‹
    // Ð°ÐºÑ‚Ð¸Ð²Ð¸Ñ€Ð¾Ð²Ð°Ð½.
    public static final int THE_BATTLEFIELD_CHANNEL_HAS_BEEN_DEACTIVATED = 2446; // ÐšÐ°Ð½Ð°Ð»
    // Ð±Ð¸Ñ‚Ð²Ñ‹
    // Ð´ÐµÐ°ÐºÑ‚Ð¸Ð²Ð¸Ñ€Ð¾Ð²Ð°Ð½.
    public static final int THE_CLOAK_EQUIP_HAS_BEEN_REMOVED_BECAUSE_THE_ARMOR_SET_EQUIP_HAS_BEEN_REMOVED = 2451; // Ð¢Ð°Ðº
    // ÐºÐ°Ðº
    // Ð’Ñ‹
    // Ñ�Ð½Ñ�Ð»Ð¸
    // ÐºÐ¾Ð¼Ð¿Ð»ÐµÐºÑ‚
    // Ð´Ð¾Ñ�Ð¿ÐµÑ…Ð¾Ð²,
    // Ð¿Ð»Ð°Ñ‰
    // Ñ‚Ð°ÐºÐ¶Ðµ
    // Ñ�Ð½Ñ�Ñ‚.
    public static final int THE_INVENTORY_IS_FULL_SO_IT_CANNOT_BE_EQUIPPED_OR_REMOVED_ON_THE_BELT = 2452; // Ð”Ð¾Ð¿Ð¾Ð»Ð½Ð¸Ñ‚ÐµÐ»ÑŒÐ½Ñ‹Ðµ
    // Ñ�Ñ‡ÐµÐ¹ÐºÐ¸
    // Ð¸Ð½Ð²ÐµÐ½Ñ‚Ð°Ñ€Ñ�
    // Ð¿ÐµÑ€ÐµÐ¿Ð¾Ð»Ð½ÐµÐ½Ñ‹,
    // Ð¿Ð¾Ñ�Ñ‚Ð¾Ð¼Ñƒ
    // Ñ�Ð½Ñ�Ñ‚ÑŒ/Ð½Ð°Ð´ÐµÑ‚ÑŒ
    // Ð¿Ð¾Ñ�Ñ�
    // Ð½ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾.
    public static final int THE_CLOAK_CANNOT_BE_EQUIPPED_BECAUSE_A_NECESSARY_ITEM_IS_NOT_EQUIPPED = 2453; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð½Ð°Ð´ÐµÐ»Ð¸
    // Ð²Ñ�Ðµ
    // Ð½ÐµÐ¾Ð±Ñ…Ð¾Ð´Ð¸Ð¼Ñ‹Ðµ
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ñ‹,
    // Ð¿Ð¾Ñ�Ñ‚Ð¾Ð¼Ñƒ
    // Ð¿Ð»Ð°Ñ‰
    // Ð½Ð°Ð´ÐµÑ‚ÑŒ
    // Ð½ÐµÐ»ÑŒÐ·Ñ�.
    public static final int KRESNIK_CLASS_AIRSHIP = 2454; // Ð›ÐµÑ‚Ð°ÑŽÑ‰Ð¸Ð¹ ÐºÐ¾Ñ€Ð°Ð±Ð»ÑŒ
    // Ñ‚Ð¸Ð¿Ð° ÐšÑ€ÐµÐ¹Ñ�ÐµÑ€Ð°
    public static final int THE_AIRSHIP_MUST_BE_SUMMONED_IN_ORDER_FOR_YOU_TO_BOARD = 2455; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð²Ñ‹Ð·Ð²Ð°Ð»Ð¸
    // Ð›ÐµÑ‚Ð°ÑŽÑ‰Ð¸Ð¹
    // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ,
    // Ð¿Ð¾Ñ�Ñ‚Ð¾Ð¼Ñƒ
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð²Ð¾Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒÑ�Ñ�
    // Ð¸Ð¼.
    public static final int IN_ORDER_TO_ACQUIRE_AN_AIRSHIP_THE_CLAN_S_LEVEL_MUST_BE_LEVEL_5_OR_HIGHER = 2456; // Ð”Ð»Ñ�
    // Ñ‚Ð¾Ð³Ð¾
    // Ñ‡Ñ‚Ð¾Ð±Ñ‹
    // Ð¿Ð¾Ð»ÑƒÑ‡Ð¸Ñ‚ÑŒ
    // Ð›ÐµÑ‚Ð°ÑŽÑ‰Ð¸Ð¹
    // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ,
    // ÐºÐ»Ð°Ð½
    // Ð´Ð¾Ð»Ð¶ÐµÐ½
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð²Ñ‹ÑˆÐµ
    // 5-Ð³Ð¾
    // ÑƒÑ€Ð¾Ð²Ð½Ñ�.
    public static final int AN_AIRSHIP_CANNOT_BE_SUMMONED_BECAUSE_EITHER_YOU_HAVE_NOT_REGISTERED_YOUR_AIRSHIP_LICENSE_OR_THE = 2457; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð²Ð²ÐµÐ»Ð¸
    // Ñ€Ð°Ð·Ñ€ÐµÑˆÐµÐ½Ð¸Ðµ
    // Ð½Ð°
    // Ð²Ñ‹Ð·Ð¾Ð²
    // Ð›ÐµÑ‚Ð°ÑŽÑ‰ÐµÐ³Ð¾
    // ÐšÐ¾Ñ€Ð°Ð±Ð»Ñ�,
    // Ð¸Ð»Ð¸
    // Ð’Ð°Ñˆ
    // ÐºÐ»Ð°Ð½
    // Ð½Ðµ
    // Ð¾Ð±Ð»Ð°Ð´Ð°ÐµÑ‚
    // Ð¸Ð¼.
    public static final int THE_AIRSHIP_OWNED_BY_THE_CLAN_IS_ALREADY_BEING_USED_BY_ANOTHER_CLAN_MEMBER = 2458; // Ð›ÐµÑ‚Ð°ÑŽÑ‰Ð¸Ð¹
    // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ
    // Ð’Ð°ÑˆÐµÐ³Ð¾
    // ÐºÐ»Ð°Ð½Ð°
    // ÑƒÐ¶Ðµ
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·ÑƒÐµÑ‚Ñ�Ñ�.
    public static final int THE_AIRSHIP_SUMMON_LICENSE_HAS_ALREADY_BEEN_ACQUIRED = 2459; // Ð’Ñ‹
    // ÑƒÐ¶Ðµ
    // Ð¿Ð¾Ð»ÑƒÑ‡Ð¸Ð»Ð¸
    // Ñ€Ð°Ð·Ñ€ÐµÑˆÐµÐ½Ð¸Ðµ
    // Ð½Ð°
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ð½Ð¸Ðµ
    // Ð›ÐµÑ‚Ð°ÑŽÑ‰ÐµÐ³Ð¾
    // ÐšÐ¾Ñ€Ð°Ð±Ð»Ñ�.
    public static final int THE_CLAN_OWNED_AIRSHIP_ALREADY_EXISTS = 2460; // Ð£
    // Ð²Ð°ÑˆÐµÐ³Ð¾
    // ÐºÐ»Ð°Ð½Ð°
    // ÑƒÐ¶Ðµ
    // ÐµÑ�Ñ‚ÑŒ
    // Ð›ÐµÑ‚Ð°ÑŽÑ‰Ð¸Ð¹
    // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ.
    public static final int THE_AIRSHIP_OWNED_BY_THE_CLAN_CAN_ONLY_BE_PURCHASED_BY_THE_CLAN_LORD = 2461; // Ð›ÐµÑ‚Ð°ÑŽÑ‰Ð¸Ð¹
    // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ
    // ÐºÐ»Ð°Ð½Ð°
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð¿Ñ€Ð¸Ð¾Ð±Ñ€ÐµÑ‚ÐµÐ½
    // Ð¸Ñ�ÐºÐ»ÑŽÑ‡Ð¸Ñ‚ÐµÐ»ÑŒÐ½Ð¾
    // Ð“Ð»Ð°Ð²Ð¾Ð¹
    // ÐšÐ»Ð°Ð½Ð°.
    public static final int THE_AIRSHIP_CANNOT_BE_SUMMONED_BECAUSE_YOU_DON_T_HAVE_ENOUGH_S1 = 2462; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð²Ñ‹Ð·Ð²Ð°Ñ‚ÑŒ
    // Ð›ÐµÑ‚Ð°ÑŽÑ‰Ð¸Ð¹
    // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ,
    // Ñ‚Ð°Ðº
    // ÐºÐ°Ðº
    // Ð½Ðµ
    // Ñ…Ð²Ð°Ñ‚Ð°ÐµÑ‚
    // $s1.
    public static final int THE_AIRSHIP_S_FUEL_EP_WILL_SOON_RUN_OUT = 2463; // Ð¢Ð¾Ð¿Ð»Ð¸Ð²Ð¾
    // Ð›ÐµÑ‚Ð°ÑŽÑ‰ÐµÐ³Ð¾
    // ÐšÐ¾Ñ€Ð°Ð±Ð»Ñ�
    // (EP)
    // Ð½Ð°
    // Ð¸Ñ�Ñ…Ð¾Ð´Ðµ.
    public static final int THE_AIRSHIP_S_FUEL_EP_HAS_RUN_OUT_THE_AIRSHIP_S_SPEED_WILL_BE_GREATLY_DECREASED_IN_THIS = 2464; // Ð—Ð°ÐºÐ¾Ð½Ñ‡Ð¸Ð»Ð¾Ñ�ÑŒ
    // Ð¢Ð¾Ð¿Ð»Ð¸Ð²Ð¾
    // Ð›ÐµÑ‚Ð°ÑŽÑ‰ÐµÐ³Ð¾
    // ÐšÐ¾Ñ€Ð°Ð±Ð»Ñ�
    // (EP).
    // Ð¡ÐºÐ¾Ñ€Ð¾Ñ�Ñ‚ÑŒ
    // ÐºÐ¾Ñ€Ð°Ð±Ð»Ñ�
    // Ð±ÑƒÐ´ÐµÑ‚
    // Ð¼Ð¸Ð½Ð¸Ð¼Ð°Ð»ÑŒÐ½Ð¾Ð¹.
    public static final int YOU_HAVE_SELECTED_A_NON_CLASS_LIMITED_TEAM_MATCH_DO_YOU_WISH_TO_PARTICIPATE = 2465; // Ð’Ñ‹
    // Ð²Ñ‹Ð±Ñ€Ð°Ð»Ð¸
    // Ð¸Ð³Ñ€Ñƒ
    // Ð±ÐµÐ·
    // Ð¾Ð³Ñ€Ð°Ð½Ð¸Ñ‡ÐµÐ½Ð¸Ñ�
    // Ð¿Ð¾
    // ÐºÐ»Ð°Ñ�Ñ�Ð°Ð¼.
    // Ð’Ñ‹
    // Ð¿Ñ€Ð°Ð²Ð´Ð°
    // Ñ…Ð¾Ñ‚Ð¸Ñ‚Ðµ
    // Ð¿Ñ€Ð¸Ð½Ñ�Ñ‚ÑŒ
    // Ð²
    // Ð½ÐµÐ¹
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð¸Ðµ?
    public static final int A_PET_ON_AUXILIARY_MODE_CANNOT_USE_SKILLS = 2466; // ÐŸÐ¸Ñ‚Ð¾Ð¼ÐµÑ†,
    // Ð²Ñ‹Ð·Ð²Ð°Ð½Ð½Ñ‹Ð¹
    // Ð²
    // Ñ€ÐµÐ¶Ð¸Ð¼Ðµ
    // Ð¿Ð¾Ð¼Ð¾Ñ‰Ð½Ð¸ÐºÐ°,
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒ
    // ÑƒÐ¼ÐµÐ½Ð¸Ñ�.
    public static final int DO_YOU_WISH_TO_BEGIN_THE_GAME_NOW = 2467; // Ð’Ñ‹
    // Ñ…Ð¾Ñ‚Ð¸Ñ‚Ðµ
    // Ð½Ð°Ñ‡Ð°Ñ‚ÑŒ
    // Ð¸Ð³Ñ€Ñƒ
    // Ð¿Ñ€Ñ�Ð¼Ð¾
    // Ñ�ÐµÐ¹Ñ‡Ð°Ñ�?
    public static final int YOU_HAVE_USED_A_REPORT_POINT_ON_C1_YOU_HAVE_S2_POINTS_REMAINING_ON_THIS_ACCOUNT = 2468; // Ð’Ñ‹
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ð»Ð¸
    // Ð¾Ñ‡ÐºÐ¸
    // Ð´Ð»Ñ�
    // Ñ‚Ð¾Ð³Ð¾,
    // Ñ‡Ñ‚Ð¾Ð±Ñ‹
    // Ñ�Ð¾Ð¾Ð±Ñ‰Ð¸Ñ‚ÑŒ
    // Ð¾
    // $c1.
    // Ð£
    // Ð’Ð°Ñ�
    // Ð¾Ñ�Ñ‚Ð°Ð»Ð¾Ñ�ÑŒ
    // $s2
    // Ð¾Ñ‡ÐºÐ¾Ð².
    public static final int YOU_HAVE_USED_ALL_AVAILABLE_POINTS_POINTS_ARE_RESET_EVERYDAY_AT_NOON = 2469; // Ð’Ñ‹
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ð»Ð¸
    // Ð²Ñ�Ðµ
    // Ð¸Ð¼ÐµÐ²ÑˆÐ¸ÐµÑ�Ñ�
    // Ð¾Ñ‡ÐºÐ¸.
    // ÐžÑ‡ÐºÐ¸
    // Ð±ÑƒÐ´ÑƒÑ‚
    // Ð²Ð¾Ñ�Ñ�Ñ‚Ð°Ð½Ð¾Ð²Ð»ÐµÐ½Ñ‹
    // Ð¿Ð¾Ñ�Ð»Ðµ
    // Ð¿Ð¾Ð»ÑƒÐ´Ð½Ñ�.
    public static final int THIS_CHARACTER_CANNOT_MAKE_A_REPORT_YOU_CANNOT_MAKE_A_REPORT_WHILE_LOCATED_INSIDE_A_PEACE_ZONE = 2470; // Ð¡Ð¾Ð¾Ð±Ñ‰Ð¸Ñ‚ÑŒ
    // Ð¾
    // Ð´Ð°Ð½Ð½Ð¾Ð¼
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ðµ
    // Ð½ÐµÐ»ÑŒÐ·Ñ�.
    // ÐžÐ½
    // Ð½Ð°Ñ…Ð¾Ð´Ð¸Ñ‚Ñ�Ñ�
    // Ð²
    // ÐœÐ¸Ñ€Ð½Ð¾Ð¹
    // Ð—Ð¾Ð½Ðµ,
    // Ð½Ð°
    // ÐŸÐ¾Ð»Ðµ
    // Ð‘Ð¾Ñ�
    // Ð¸Ð»Ð¸
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð²ÑƒÐµÑ‚
    // Ð²
    // Ð’Ð¾Ð¹Ð½Ðµ
    // ÐšÐ»Ð°Ð½Ð¾Ð²
    // Ð¸Ð»Ð¸
    // ÐžÐ»Ð¸Ð¼Ð¿Ð¸Ð°Ð´Ðµ.
    public static final int THIS_CHARACTER_CANNOT_MAKE_A_REPORT_THE_TARGET_HAS_ALREADY_BEEN_REPORTED_BY_EITHER_YOUR_CLAN_OR = 2471; // Ð¡Ð¾Ð¾Ð±Ñ‰Ð¸Ñ‚ÑŒ
    // Ð¾
    // Ð´Ð°Ð½Ð½Ð¾Ð¼
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ðµ
    // Ð½ÐµÐ»ÑŒÐ·Ñ�.
    // Ðž
    // Ð´Ð°Ð½Ð½Ð¾Ð¼
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ðµ
    // ÑƒÐ¶Ðµ
    // Ñ�Ð¾Ð¾Ð±Ñ‰Ð¸Ð»
    // Ñ‡Ð»ÐµÐ½
    // Ð’Ð°ÑˆÐµÐ³Ð¾
    // ÐºÐ»Ð°Ð½Ð°,
    // Ð°Ð»ÑŒÑ�Ð½Ñ�Ð°,
    // Ð¸Ð»Ð¸
    // Ñ�Ð¾Ð¾Ð±Ñ‰ÐµÐ½Ð¸Ðµ
    // Ð±Ñ‹Ð»Ð¾
    // Ð¿Ð¾Ð´Ð°Ð½Ð¾
    // Ñ�
    // Ð’Ð°ÑˆÐµÐ³Ð¾
    // IP.
    public static final int THIS_CHARACTER_CANNOT_MAKE_A_REPORT_BECAUSE_ANOTHER_CHARACTER_FROM_THIS_ACCOUNT_HAS_ALREADY_DONE = 2472; // Ð¡Ð¾Ð¾Ð±Ñ‰Ð¸Ñ‚ÑŒ
    // Ð¾
    // Ð´Ð°Ð½Ð½Ð¾Ð¼
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ðµ
    // Ð½ÐµÐ»ÑŒÐ·Ñ�.
    // Ð£Ð¶Ðµ
    // Ð¿Ð¾Ð»ÑƒÑ‡ÐµÐ½Ð¾
    // Ñ�Ð¾Ð¾Ð±Ñ‰ÐµÐ½Ð¸Ðµ
    // Ð¾
    // Ð½ÐµÐ¼
    // Ñ�
    // Ð´Ñ€ÑƒÐ³Ð¾Ð³Ð¾
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°
    // Ð’Ð°ÑˆÐµÐ³Ð¾
    // Ð°ÐºÐºÐ°ÑƒÐ½Ñ‚Ð°.
    public static final int YOU_HAVE_BEEN_REPORTED_AS_AN_ILLEGAL_PROGRAM_USER_AND_YOUR_CHATTING_WILL_BE_BLOCKED_FOR_10 = 2473; // ÐŸÐ¾Ð»ÑƒÑ‡ÐµÐ½Ð¾
    // Ñ�Ð¾Ð¾Ð±Ñ‰ÐµÐ½Ð¸Ðµ
    // Ð¾
    // Ñ‚Ð¾Ð¼,
    // Ñ‡Ñ‚Ð¾
    // Ð’Ñ‹
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·ÑƒÐµÑ‚Ðµ
    // Ð½ÐµÐ»ÐµÐ³Ð°Ð»ÑŒÐ½ÑƒÑŽ
    // Ð¿Ñ€Ð¾Ð³Ñ€Ð°Ð¼Ð¼Ñƒ.
    // Ð’Ñ‹
    // Ð½Ðµ
    // Ñ�Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒ
    // Ñ‡Ð°Ñ‚
    // Ð²
    // Ñ‚ÐµÑ‡ÐµÐ½Ð¸Ðµ
    // 10-Ñ‚Ð¸
    // Ð¼Ð¸Ð½.
    public static final int YOU_HAVE_BEEN_REPORTED_AS_AN_ILLEGAL_PROGRAM_USER_AND_YOUR_PARTY_PARTICIPATION_WILL_BE_BLOCKED_60 = 2474; // ÐŸÐ¾Ð»ÑƒÑ‡ÐµÐ½Ð¾
    // Ñ�Ð¾Ð¾Ð±Ñ‰ÐµÐ½Ð¸Ðµ
    // Ð¾
    // Ñ‚Ð¾Ð¼,
    // Ñ‡Ñ‚Ð¾
    // Ð’Ñ‹
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·ÑƒÐµÑ‚Ðµ
    // Ð½ÐµÐ»ÐµÐ³Ð°Ð»ÑŒÐ½ÑƒÑŽ
    // Ð¿Ñ€Ð¾Ð³Ñ€Ð°Ð¼Ð¼Ñƒ.
    // Ð’Ñ‹
    // Ð½Ðµ
    // Ñ�Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð²Ñ�Ñ‚ÑƒÐ¿Ð¸Ñ‚ÑŒ
    // Ð²
    // Ð³Ñ€ÑƒÐ¿Ð¿Ñƒ
    // Ð²
    // Ñ‚ÐµÑ‡ÐµÐ½Ð¸Ðµ
    // 60-Ñ‚Ð¸
    // Ð¼Ð¸Ð½.
    public static final int YOU_HAVE_BEEN_REPORTED_AS_AN_ILLEGAL_PROGRAM_USER_AND_YOUR_PARTY_PARTICIPATION_WILL_BE_BLOCKED_120 = 2475; // ÐŸÐ¾Ð»ÑƒÑ‡ÐµÐ½Ð¾
    // Ñ�Ð¾Ð¾Ð±Ñ‰ÐµÐ½Ð¸Ðµ
    // Ð¾
    // Ñ‚Ð¾Ð¼,
    // Ñ‡Ñ‚Ð¾
    // Ð’Ñ‹
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·ÑƒÐµÑ‚Ðµ
    // Ð½ÐµÐ»ÐµÐ³Ð°Ð»ÑŒÐ½ÑƒÑŽ
    // Ð¿Ñ€Ð¾Ð³Ñ€Ð°Ð¼Ð¼Ñƒ.
    // Ð’Ñ‹
    // Ð½Ðµ
    // Ñ�Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð²Ñ�Ñ‚ÑƒÐ¿Ð¸Ñ‚ÑŒ
    // Ð²
    // Ð³Ñ€ÑƒÐ¿Ð¿Ñƒ
    // Ð²
    // Ñ‚ÐµÑ‡ÐµÐ½Ð¸Ðµ
    // 120-Ñ‚Ð¸
    // Ð¼Ð¸Ð½.
    public static final int YOU_HAVE_BEEN_REPORTED_AS_AN_ILLEGAL_PROGRAM_USER_AND_YOUR_PARTY_PARTICIPATION_WILL_BE_BLOCKED_180 = 2476; // ÐŸÐ¾Ð»ÑƒÑ‡ÐµÐ½Ð¾
    // Ñ�Ð¾Ð¾Ð±Ñ‰ÐµÐ½Ð¸Ðµ
    // Ð¾
    // Ñ‚Ð¾Ð¼,
    // Ñ‡Ñ‚Ð¾
    // Ð’Ñ‹
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·ÑƒÐµÑ‚Ðµ
    // Ð½ÐµÐ»ÐµÐ³Ð°Ð»ÑŒÐ½ÑƒÑŽ
    // Ð¿Ñ€Ð¾Ð³Ñ€Ð°Ð¼Ð¼Ñƒ.
    // Ð’Ñ‹
    // Ð½Ðµ
    // Ñ�Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð²Ñ�Ñ‚ÑƒÐ¿Ð¸Ñ‚ÑŒ
    // Ð²
    // Ð³Ñ€ÑƒÐ¿Ð¿Ñƒ
    // Ð²
    // Ñ‚ÐµÑ‡ÐµÐ½Ð¸Ðµ
    // 180-Ñ‚Ð¸
    // Ð¼Ð¸Ð½.
    public static final int YOU_HAVE_BEEN_REPORTED_AS_AN_ILLEGAL_PROGRAM_USER_AND_YOUR_ACTIONS_WILL_BE_RESTRICTED_FOR_120 = 2477; // ÐŸÐ¾Ð»ÑƒÑ‡ÐµÐ½Ð¾
    // Ñ�Ð¾Ð¾Ð±Ñ‰ÐµÐ½Ð¸Ðµ
    // Ð¾
    // Ñ‚Ð¾Ð¼,
    // Ñ‡Ñ‚Ð¾
    // Ð’Ñ‹
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·ÑƒÐµÑ‚Ðµ
    // Ð½ÐµÐ»ÐµÐ³Ð°Ð»ÑŒÐ½ÑƒÑŽ
    // Ð¿Ñ€Ð¾Ð³Ñ€Ð°Ð¼Ð¼Ñƒ.
    // Ð’Ñ‹
    // Ð±ÑƒÐ´ÐµÑ‚Ðµ
    // Ð¾Ð³Ñ€Ð°Ð½Ð¸Ñ‡ÐµÐ½Ñ‹
    // Ð²
    // Ð´Ð²Ð¸Ð¶ÐµÐ½Ð¸Ñ�Ñ…
    // Ð²
    // Ñ‚ÐµÑ‡ÐµÐ½Ð¸Ðµ
    // 120-Ñ‚Ð¸
    // Ð¼Ð¸Ð½.
    public static final int YOU_HAVE_BEEN_REPORTED_AS_AN_ILLEGAL_PROGRAM_USER_AND_YOUR_ACTIONS_WILL_BE_RESTRICTED_FOR_180 = 2478; // ÐŸÐ¾Ð»ÑƒÑ‡ÐµÐ½Ð¾
    // Ñ�Ð¾Ð¾Ð±Ñ‰ÐµÐ½Ð¸Ðµ
    // Ð¾
    // Ñ‚Ð¾Ð¼,
    // Ñ‡Ñ‚Ð¾
    // Ð’Ñ‹
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·ÑƒÐµÑ‚Ðµ
    // Ð½ÐµÐ»ÐµÐ³Ð°Ð»ÑŒÐ½ÑƒÑŽ
    // Ð¿Ñ€Ð¾Ð³Ñ€Ð°Ð¼Ð¼Ñƒ.
    // Ð’Ñ‹
    // Ð±ÑƒÐ´ÐµÑ‚Ðµ
    // Ð¾Ð³Ñ€Ð°Ð½Ð¸Ñ‡ÐµÐ½Ñ‹
    // Ð²
    // Ð´Ð²Ð¸Ð¶ÐµÐ½Ð¸Ñ�Ñ…
    // Ð²
    // Ñ‚ÐµÑ‡ÐµÐ½Ð¸Ðµ
    // 180-Ñ‚Ð¸
    // Ð¼Ð¸Ð½.
    public static final int YOU_HAVE_BEEN_REPORTED_AS_AN_ILLEGAL_PROGRAM_USER_AND_YOUR_ACTIONS_WILL_BE_RESTRICTED_FOR_180_1 = 2479; // ÐŸÐ¾Ð»ÑƒÑ‡ÐµÐ½Ð¾
    // Ñ�Ð¾Ð¾Ð±Ñ‰ÐµÐ½Ð¸Ðµ
    // Ð¾
    // Ñ‚Ð¾Ð¼,
    // Ñ‡Ñ‚Ð¾
    // Ð’Ñ‹
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·ÑƒÐµÑ‚Ðµ
    // Ð½ÐµÐ»ÐµÐ³Ð°Ð»ÑŒÐ½ÑƒÑŽ
    // Ð¿Ñ€Ð¾Ð³Ñ€Ð°Ð¼Ð¼Ñƒ.
    // Ð’Ñ‹
    // Ð±ÑƒÐ´ÐµÑ‚Ðµ
    // Ð¾Ð³Ñ€Ð°Ð½Ð¸Ñ‡ÐµÐ½Ñ‹
    // Ð²
    // Ð´Ð²Ð¸Ð¶ÐµÐ½Ð¸Ñ�Ñ…
    // Ð²
    // Ñ‚ÐµÑ‡ÐµÐ½Ð¸Ðµ
    // 180-Ñ‚Ð¸
    // Ð¼Ð¸Ð½.
    public static final int YOU_HAVE_BEEN_REPORTED_AS_AN_ILLEGAL_PROGRAM_USER_AND_MOVING_WILL_BE_BLOCKED_FOR_120_MINUTES = 2480; // ÐŸÐ¾Ð»ÑƒÑ‡ÐµÐ½Ð¾
    // Ñ�Ð¾Ð¾Ð±Ñ‰ÐµÐ½Ð¸Ðµ
    // Ð¾
    // Ñ‚Ð¾Ð¼,
    // Ñ‡Ñ‚Ð¾
    // Ð’Ñ‹
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·ÑƒÐµÑ‚Ðµ
    // Ð½ÐµÐ»ÐµÐ³Ð°Ð»ÑŒÐ½ÑƒÑŽ
    // Ð¿Ñ€Ð¾Ð³Ñ€Ð°Ð¼Ð¼Ñƒ.
    // Ð’Ñ‹
    // Ð±ÑƒÐ´ÐµÑ‚Ðµ
    // Ð½Ðµ
    // Ñ�Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¿ÐµÑ€ÐµÐ¼ÐµÑ‰Ð°Ñ‚ÑŒÑ�Ñ�
    // Ð²
    // Ñ‚ÐµÑ‡ÐµÐ½Ð¸Ðµ
    // 120-Ñ‚Ð¸
    // Ð¼Ð¸Ð½.
    public static final int C1_HAS_BEEN_REPORTED_AS_AN_ILLEGAL_PROGRAM_USER_AND_HAS_BEEN_INVESTIGATED = 2481; // ÐŸÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶
    // $c1
    // Ñ�Ð¾Ð¾Ð±Ñ‰Ð¸Ð»
    // Ð¾
    // Ñ‚Ð¾Ð¼,
    // Ñ‡Ñ‚Ð¾
    // Ð’Ñ‹
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·ÑƒÐµÑ‚Ðµ
    // Ð½ÐµÐ»ÐµÐ³Ð°Ð»ÑŒÐ½ÑƒÑŽ
    // Ð¿Ñ€Ð¾Ð³Ñ€Ð°Ð¼Ð¼Ñƒ,
    // Ð¿Ð¾Ñ�Ñ‚Ð¾Ð¼Ñƒ
    // Ð’Ñ‹
    // Ð½Ðµ
    // Ñ�Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¿Ð¾Ð»ÑƒÑ‡Ð¸Ñ‚ÑŒ
    // Ð½Ð°Ð³Ñ€Ð°Ð´Ñƒ.
    public static final int C1_HAS_BEEN_REPORTED_AS_AN_ILLEGAL_PROGRAM_USER_AND_CANNOT_JOIN_A_PARTY = 2482; // ÐŸÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶
    // $c1
    // Ñ�Ð¾Ð¾Ð±Ñ‰Ð¸Ð»
    // Ð¾
    // Ñ‚Ð¾Ð¼,
    // Ñ‡Ñ‚Ð¾
    // Ð’Ñ‹
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·ÑƒÐµÑ‚Ðµ
    // Ð½ÐµÐ»ÐµÐ³Ð°Ð»ÑŒÐ½ÑƒÑŽ
    // Ð¿Ñ€Ð¾Ð³Ñ€Ð°Ð¼Ð¼Ñƒ,
    // Ð¿Ð¾Ñ�Ñ‚Ð¾Ð¼Ñƒ
    // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð²Ñ�Ñ‚ÑƒÐ¿Ð¸Ñ‚ÑŒ
    // Ð²
    // Ð³Ñ€ÑƒÐ¿Ð¿Ñƒ.
    public static final int YOU_HAVE_BEEN_REPORTED_AS_AN_ILLEGAL_PROGRAM_USER_AND_CHATTING_IS_NOT_ALLOWED = 2483; // ÐŸÐ¾Ð»ÑƒÑ‡ÐµÐ½Ð¾
    // Ñ�Ð¾Ð¾Ð±Ñ‰ÐµÐ½Ð¸Ðµ
    // Ð¾
    // Ñ‚Ð¾Ð¼,
    // Ñ‡Ñ‚Ð¾
    // Ð’Ñ‹
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·ÑƒÐµÑ‚Ðµ
    // Ð½ÐµÐ»ÐµÐ³Ð°Ð»ÑŒÐ½ÑƒÑŽ
    // Ð¿Ñ€Ð¾Ð³Ñ€Ð°Ð¼Ð¼Ñƒ,
    // Ð¿Ð¾Ñ�Ñ‚Ð¾Ð¼Ñƒ
    // Ñ‡Ð°Ñ‚
    // Ð·Ð°Ð¿Ñ€ÐµÑ‰ÐµÐ½.
    public static final int YOU_HAVE_BEEN_REPORTED_AS_AN_ILLEGAL_PROGRAM_USER_AND_PARTICIPATING_IN_A_PARTY_IS_NOT_ALLOWED = 2484; // ÐŸÐ¾Ð»ÑƒÑ‡ÐµÐ½Ð¾
    // Ñ�Ð¾Ð¾Ð±Ñ‰ÐµÐ½Ð¸Ðµ
    // Ð¾
    // Ñ‚Ð¾Ð¼,
    // Ñ‡Ñ‚Ð¾
    // Ð’Ñ‹
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·ÑƒÐµÑ‚Ðµ
    // Ð½ÐµÐ»ÐµÐ³Ð°Ð»ÑŒÐ½ÑƒÑŽ
    // Ð¿Ñ€Ð¾Ð³Ñ€Ð°Ð¼Ð¼Ñƒ,
    // Ð¿Ð¾Ñ�Ñ‚Ð¾Ð¼Ñƒ
    // Ð’Ñ‹
    // Ð½Ðµ
    // Ñ�Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð²Ñ�Ñ‚ÑƒÐ¿Ð¸Ñ‚ÑŒ
    // Ð²
    // Ð³Ñ€ÑƒÐ¿Ð¿Ñƒ.
    public static final int YOU_HAVE_BEEN_REPORTED_AS_AN_ILLEGAL_PROGRAM_USER_AND_YOUR_ACTIVITIES_ARE_ONLY_ALLOWED_WITHIN = 2485; // ÐŸÐ¾Ð»ÑƒÑ‡ÐµÐ½Ð¾
    // Ñ�Ð¾Ð¾Ð±Ñ‰ÐµÐ½Ð¸Ðµ
    // Ð¾
    // Ñ‚Ð¾Ð¼,
    // Ñ‡Ñ‚Ð¾
    // Ð’Ñ‹
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·ÑƒÐµÑ‚Ðµ
    // Ð½ÐµÐ»ÐµÐ³Ð°Ð»ÑŒÐ½ÑƒÑŽ
    // Ð¿Ñ€Ð¾Ð³Ñ€Ð°Ð¼Ð¼Ñƒ,
    // Ð¿Ð¾Ñ�Ñ‚Ð¾Ð¼Ñƒ
    // Ð’Ñ‹
    // Ð±ÑƒÐ´ÐµÑ‚Ðµ
    // Ð¾Ð³Ñ€Ð°Ð½Ð¸Ñ‡ÐµÐ½Ñ‹
    // Ð²
    // Ð´Ð²Ð¸Ð¶ÐµÐ½Ð¸Ñ�Ñ….
    public static final int YOU_HAVE_BEEN_BLOCKED_DUE_TO_VERIFICATION_THAT_YOU_ARE_USING_A_THIRD_PARTY_PROGRAM_SUBSEQUENT = 2486; // Ð”Ð¾
    // Ñ�Ð¸Ñ…
    // Ð¿Ð¾Ñ€
    // Ð’Ñ‹
    // Ð¿Ð¾Ð»ÑƒÑ‡Ð°Ð»Ð¸
    // ÑˆÑ‚Ñ€Ð°Ñ„Ñ‹
    // Ð·Ð°
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ð½Ð¸Ðµ
    // Ð½ÐµÐ»ÐµÐ³Ð°Ð»ÑŒÐ½Ñ‹Ñ…
    // Ð¿Ñ€Ð¾Ð³Ñ€Ð°Ð¼Ð¼
    // Ð²
    // Ñ�Ð¾Ð¾Ñ‚Ð²ÐµÑ‚Ñ�Ñ‚Ð²Ð¸Ð¸
    // Ñ�
    // ÐºÐ¾Ð»Ð¸Ñ‡ÐµÑ�Ñ‚Ð²Ð¾Ð¼
    // Ð¸Ð¼ÐµÑŽÑ‰Ð¸Ñ…Ñ�Ñ�
    // Ñƒ
    // Ð’Ð°Ñ�
    // Ð¾Ñ‡ÐºÐ¾Ð².
    // Ð¡Ð¾
    // Ñ�Ð»ÐµÐ´ÑƒÑŽÑ‰ÐµÐ³Ð¾
    // Ñ€Ð°Ð·Ð°
    // ÑˆÑ‚Ñ€Ð°Ñ„
    // Ð±ÑƒÐ´ÐµÑ‚
    // Ð¿Ñ€ÐµÐ´ÑƒÑ�Ð¼Ð°Ñ‚Ñ€Ð¸Ð²Ð°Ñ‚ÑŒ
    // Ð½Ðµ
    // Ñ‚Ð¾Ð»ÑŒÐºÐ¾
    // Ð¸Ð³Ñ€Ð¾Ð²Ñ‹Ðµ
    // ÑˆÑ‚Ñ€Ð°Ñ„Ñ‹,
    // Ð½Ð¾
    // Ð¸
    // Ð½ÐµÑƒÐ´Ð¾Ð±Ñ�Ñ‚Ð²Ð°
    // Ð²
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ð½Ð¸Ð¸
    // Ð°ÐºÐºÐ°ÑƒÐ½Ñ‚Ð°.
    public static final int YOU_HAVE_BEEN_REPORTED_AS_AN_ILLEGAL_PROGRAM_USER_AND_YOUR_CONNECTION_HAS_BEEN_ENDED_PLEASE = 2487; // ÐŸÐ¾Ð»ÑƒÑ‡ÐµÐ½Ð¾
    // Ñ�Ð¾Ð¾Ð±Ñ‰ÐµÐ½Ð¸Ðµ
    // Ð¾
    // Ñ‚Ð¾Ð¼,
    // Ñ‡Ñ‚Ð¾
    // Ð’Ñ‹
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·ÑƒÐµÑ‚Ðµ
    // Ð½ÐµÐ»ÐµÐ³Ð°Ð»ÑŒÐ½ÑƒÑŽ
    // Ð¿Ñ€Ð¾Ð³Ñ€Ð°Ð¼Ð¼Ñƒ,
    // Ð¿Ð¾Ñ�Ñ‚Ð¾Ð¼Ñƒ
    // Ð¸Ð³Ñ€Ð°
    // Ð·Ð°Ð²ÐµÑ€ÑˆÐµÐ½Ð°.
    // Ð’Ñ‹
    // Ñ�Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ñ�Ð½Ð¾Ð²Ð°
    // Ð²Ð¾Ð¹Ñ‚Ð¸
    // Ð²
    // Ð¸Ð³Ñ€Ñƒ
    // Ð¿Ð¾Ñ�Ð»Ðµ
    // Ð¸Ð´ÐµÐ½Ñ‚Ð¸Ñ„Ð¸ÐºÐ°Ñ†Ð¸Ð¸
    // Ð»Ð¸Ñ‡Ð½Ð¾Ñ�Ñ‚Ð¸.
    public static final int YOU_CANNOT_ENTER_AERIAL_CLEFT_BECAUSE_YOU_ARE_NOT_AT_THE_RIGHT_LEVEL_ENTRY_IS_POSSIBLE_ONLY = 2488; // Ð’Ð°Ñˆ
    // ÑƒÑ€Ð¾Ð²ÐµÐ½ÑŒ
    // Ð½Ðµ
    // Ñ�Ð¾Ð¾Ñ‚Ð²ÐµÑ‚Ñ�Ñ‚Ð²ÑƒÐµÑ‚
    // Ñ‚Ñ€ÐµÐ±Ð¾Ð²Ð°Ð½Ð¸Ñ�Ð¼.
    // Ð§Ñ‚Ð¾Ð±Ñ‹
    // Ð¿Ð¾Ð¿Ð°Ñ�Ñ‚ÑŒ
    // Ð²
    // Ð£Ñ‰ÐµÐ»ÑŒÐµ,
    // Ð½ÐµÐ¾Ð±Ñ…Ð¾Ð´Ð¸Ð¼Ð¾
    // Ð´Ð¾Ñ�Ñ‚Ð¸Ñ‡ÑŒ
    // 75-Ð³Ð¾
    // ÑƒÑ€Ð¾Ð²Ð½Ñ�.
    public static final int YOU_MUST_TARGET_A_CONTROL_DEVICE_IN_ORDER_TO_PERFORM_THIS_ACTION = 2489; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð²Ñ‹Ð±Ñ€Ð°Ð»Ð¸
    // Ð²
    // ÐºÐ°Ñ‡ÐµÑ�Ñ‚Ð²Ðµ
    // Ñ†ÐµÐ»Ð¸
    // Ñ�ÑƒÑ‰ÐµÑ�Ñ‚Ð²Ð¾,
    // ÐºÐ¾Ñ‚Ð¾Ñ€Ñ‹Ð¼
    // Ð¼Ð¾Ð¶Ð½Ð¾
    // ÑƒÐ¿Ñ€Ð°Ð²Ð»Ñ�Ñ‚ÑŒ,
    // Ð¿Ð¾Ñ�Ñ‚Ð¾Ð¼Ñƒ
    // Ð´Ð°Ð½Ð½Ð°Ñ�
    // Ñ„ÑƒÐ½ÐºÑ†Ð¸Ñ�
    // Ð½ÐµÐ´Ð¾Ñ�Ñ‚ÑƒÐ¿Ð½Ð°.
    public static final int YOU_CANNOT_PERFORM_THIS_ACTION_BECAUSE_YOU_ARE_TOO_FAR_AWAY_FROM_THE_CONTROL_DEVICE = 2490; // Ð’Ñ‹
    // Ð½Ð°Ñ…Ð¾Ð´Ð¸Ñ‚ÐµÑ�ÑŒ
    // Ñ�Ð»Ð¸ÑˆÐºÐ¾Ð¼
    // Ð´Ð°Ð»ÐµÐºÐ¾
    // Ð¾Ñ‚
    // Ñ�ÑƒÑ‰ÐµÑ�Ñ‚Ð²Ð°,
    // Ð¿Ð¾Ñ�Ñ‚Ð¾Ð¼Ñƒ
    // ÐºÐ¾Ð½Ñ‚Ñ€Ð¾Ð»ÑŒ
    // Ð½Ð°Ð´
    // Ð½Ð¸Ð¼
    // Ð½ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶ÐµÐ½.
    public static final int YOUR_SHIP_CANNOT_TELEPORT_BECAUSE_IT_DOES_NOT_HAVE_ENOUGH_FUEL_FOR_THE_TRIP = 2491; // Ð�Ðµ
    // Ñ…Ð²Ð°Ñ‚Ð°ÐµÑ‚
    // Ñ‚Ð¾Ð¿Ð»Ð¸Ð²Ð°
    // Ð›ÐµÑ‚Ð°ÑŽÑ‰ÐµÐ³Ð¾
    // ÐšÐ¾Ñ€Ð°Ð±Ð»Ñ�,
    // Ñ‡Ñ‚Ð¾Ð±Ñ‹
    // Ñ‚ÐµÐ»ÐµÐ¿Ð¾Ñ€Ñ‚Ð¸Ñ€Ð¾Ð²Ð°Ñ‚ÑŒÑ�Ñ�.
    public static final int THE_AIRSHIP_HAS_BEEN_SUMMONED_IT_WILL_AUTOMATICALLY_DEPART_IN_S_MINUTES = 2492; // Ð’Ñ‹
    // Ð²Ñ‹Ð·Ð²Ð°Ð»Ð¸
    // Ð›ÐµÑ‚Ð°ÑŽÑ‰Ð¸Ð¹
    // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ.
    // ÐžÑ‚Ð¿Ñ€Ð°Ð²Ð»ÐµÐ½Ð¸Ðµ
    // Ñ‡ÐµÑ€ÐµÐ·
    // %s.
    public static final int ENTER_CHAT_MODE_IS_AUTOMATICALLY_ENABLED_WHEN_YOU_ARE_IN_A_FLYING_TRANSFORMATION_STATE = 2493; // ÐŸÐµÑ€ÐµÐ²Ð¾Ð¿Ð»Ð¾Ñ‚Ð¸Ð²ÑˆÐ¸Ñ�ÑŒ
    // Ð²
    // Ð»ÐµÑ‚Ð°ÑŽÑ‰ÐµÐµ
    // Ñ�ÑƒÑ‰ÐµÑ�Ñ‚Ð²Ð¾,
    // Ð’Ñ‹
    // Ð²Ñ…Ð¾Ð´Ð¸Ñ‚Ðµ
    // Ð²
    // Ñ€ÐµÐ¶Ð¸Ð¼
    // Ð²Ð½ÑƒÑ‚Ñ€ÐµÐ½Ð½ÐµÐ³Ð¾
    // Ñ‡Ð°Ñ‚Ð°.
    public static final int ENTER_CHAT_MODE_IS_AUTOMATICALLY_ENABLED_WHEN_YOU_ARE_IN_AIRSHIP_CONTROL_STATE = 2494; // Ð�Ð°Ñ‡Ð°Ð²
    // ÑƒÐ¿Ñ€Ð°Ð²Ð»ÐµÐ½Ð¸Ðµ
    // Ð›ÐµÑ‚Ð°ÑŽÑ‰Ð¸Ð¼
    // ÐšÐ¾Ñ€Ð°Ð±Ð»ÐµÐ¼,
    // Ð’Ñ‹
    // Ð²Ñ…Ð¾Ð´Ð¸Ñ‚Ðµ
    // Ð²
    // Ñ€ÐµÐ¶Ð¸Ð¼
    // Ð²Ð½ÑƒÑ‚Ñ€ÐµÐ½Ð½ÐµÐ³Ð¾
    // Ñ‡Ð°Ñ‚Ð°.
    public static final int W_GO_FORWARD_S_STOP_A_TURN_LEFT_D_TURN_RIGHT_E_INCREASE_ALTITUDE_AND_Q_DECREASE_ALTITUDE = 2495; // W(Ð’Ð¿ÐµÑ€ÐµÐ´),
    // S(ÐžÑ�Ñ‚Ð°Ð½Ð¾Ð²Ð¸Ñ‚ÑŒÑ�Ñ�),
    // A(Ð’Ð»ÐµÐ²Ð¾),
    // D(Ð’Ð¿Ñ€Ð°Ð²Ð¾),
    // E(Ð�Ð°Ð±Ñ€Ð°Ñ‚ÑŒ
    // Ð’Ñ‹Ñ�Ð¾Ñ‚Ñƒ),
    // Q(Ð¡Ð½Ð¸Ð·Ð¸Ñ‚ÑŒÑ�Ñ�).
    public static final int IF_YOU_CLICK_ON_A_SKILL_DESIGNATED_ON_YOUR_SHORTCUT_BAR_THAT_SLOT_IS_ACTIVATED_ONCE_ACTIVATED = 2496; // Ð�Ð°Ð¶Ð°Ð²
    // ÐºÐ»Ð°Ð²Ð¸ÑˆÑƒ,
    // Ñ�Ð¾Ð¾Ñ‚Ð²ÐµÑ‚Ñ�Ñ‚Ð²ÑƒÑŽÑ‰ÑƒÑŽ
    // Ñ�Ñ€Ð»Ñ‹ÐºÑƒ,
    // Ð’Ñ‹
    // Ð°ÐºÑ‚Ð¸Ð²Ð¸Ñ€ÑƒÐµÑ‚Ðµ
    // Ð¿Ð°Ð½ÐµÐ»ÑŒ
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð²,
    // Ð°
    // Ð¿Ð¾Ð²Ñ‚Ð¾Ñ€Ð½Ð¾
    // Ð½Ð°Ð¶Ð°Ð²
    // Ð½Ð°
    // Ð½ÐµÐµ
    // Ð¸Ð»Ð¸
    // Ð½Ð°
    // Ð¿Ñ€Ð¾Ð±ÐµÐ»,
    // Ð’Ñ‹
    // Ð°ÐºÑ‚Ð¸Ð²Ð¸Ñ€ÑƒÐµÑ‚Ðµ
    // Ñ�Ñ€Ð»Ñ‹Ðº.
    public static final int TO_CLOSE_THE_CURRENTLY_OPEN_TIP_PLEASE_CANCEL_THE_CHECKED_BOX__SYSTEM_TUTORIAL__IN_OPTIONS = 2497; // Ð§Ñ‚Ð¾Ð±Ñ‹
    // Ð¾Ñ‚ÐºÐ»ÑŽÑ‡Ð¸Ñ‚ÑŒ
    // Ð¿Ð¾Ð´Ñ�ÐºÐ°Ð·ÐºÐ¸,
    // Ð²Ñ‹ÐºÐ»ÑŽÑ‡Ð¸Ñ‚Ðµ
    // Ð¾Ñ‚Ð¼ÐµÑ‚ÐºÑƒ
    // "Ð¡Ð¸Ñ�Ñ‚ÐµÐ¼Ð½Ð¾Ðµ ÐžÐ±ÑƒÑ‡ÐµÐ½Ð¸Ðµ"
    // Ð²
    // Ð½Ð°Ñ�Ñ‚Ñ€Ð¾Ð¹ÐºÐ°Ñ…
    // Ð¸Ð³Ñ€Ñ‹.
    public static final int DURING_THE_AIRSHIP_CONTROL_STATE_YOU_CAN_ALSO_CHANGE_ALTITUDE_USING_THE_BUTTON_AT_THE_CENTER_OF = 2498; // Ð˜Ð·Ð¼ÐµÐ½Ð¸Ñ‚ÑŒ
    // Ð²Ñ‹Ñ�Ð¾Ñ‚Ñƒ
    // Ð›ÐµÑ‚Ð°ÑŽÑ‰ÐµÐ³Ð¾
    // ÐšÐ¾Ñ€Ð°Ð±Ð»Ñ�
    // Ð¼Ð¾Ð¶Ð½Ð¾
    // Ñ‚Ð°ÐºÐ¶Ðµ
    // Ñ�
    // Ð¿Ð¾Ð¼Ð¾Ñ‰ÑŒÑŽ
    // Ð¸ÐºÐ¾Ð½Ð¾Ðº
    // Ð¿Ð°Ð½ÐµÐ»Ð¸
    // ÑƒÐ¿Ñ€Ð°Ð²Ð»ÐµÐ½Ð¸Ñ�.
    public static final int YOU_CANNOT_COLLECT_BECAUSE_SOMEONE_ELSE_IS_ALREADY_COLLECTING = 2499; // ÐšÑ‚Ð¾-Ñ‚Ð¾
    // ÑƒÐ¶Ðµ
    // Ñ�Ð¾Ð±Ð¸Ñ€Ð°ÐµÑ‚
    // Ñ€ÑƒÐ´Ñƒ,
    // Ð¿Ð¾Ñ�Ñ‚Ð¾Ð¼Ñƒ
    // Ð’Ñ‹
    // Ð½Ðµ
    // Ñ�Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ñ�Ð¾Ð±Ñ€Ð°Ñ‚ÑŒ
    // Ñ€ÑƒÐ´Ñƒ
    // Ñ�ÐµÐ¹Ñ‡Ð°Ñ�.
    public static final int THE_COLLECTION_HAS_SUCCEEDED = 2500; // Ð£ Ð’Ð°Ñ�
    // Ð¿Ð¾Ð»ÑƒÑ‡Ð¸Ð»Ð¾Ñ�ÑŒ
    // Ð´Ð¾Ð±Ñ‹Ñ‚ÑŒ Ñ€ÑƒÐ´Ñƒ.
    public static final int YOU_WILL_BE_MOVED_TO_THE_PREVIOUS_CHATTING_CHANNEL_TAB = 2501; // Ð’Ñ‹
    // Ð±ÑƒÐ´ÐµÑ‚Ðµ
    // Ð¿ÐµÑ€ÐµÐ¼ÐµÑ‰ÐµÐ½Ñ‹
    // Ð²
    // Ð¿Ñ€ÐµÐ´Ñ‹Ð´ÑƒÑ‰ÑƒÑŽ
    // Ñ‚Ð°Ð±Ð»Ð¸Ñ†Ñƒ
    // ÐºÐ°Ð½Ð°Ð»Ð°
    // Ñ‡Ð°Ñ‚Ð°.
    public static final int YOU_WILL_BE_MOVED_TO_THE_NEXT_CHATTING_CHANNEL_TAB = 2502; // Ð’Ñ‹
    // Ð±ÑƒÐ´ÐµÑ‚Ðµ
    // Ð¿ÐµÑ€ÐµÐ¼ÐµÑ‰ÐµÐ½Ñ‹
    // Ð²
    // Ñ�Ð»ÐµÐ´ÑƒÑŽÑ‰ÑƒÑŽ
    // Ñ‚Ð°Ð±Ð»Ð¸Ñ†Ñƒ
    // ÐºÐ°Ð½Ð°Ð»Ð°
    // Ñ‡Ð°Ñ‚Ð°.
    public static final int THE_CURRENTLY_SELECTED_TARGET_WILL_BE_CANCELLED = 2503; // Ð’Ñ‹Ð±Ñ€Ð°Ð½Ð½Ð°Ñ�
    // Ñ†ÐµÐ»ÑŒ
    // Ð±ÑƒÐ´ÐµÑ‚
    // Ð¾Ñ‚Ð¼ÐµÐ½ÐµÐ½Ð°.
    public static final int FOCUS_WILL_BE_MOVED_TO_CHAT_WINDOW = 2504; // Ð�ÐºÑ†ÐµÐ½Ñ‚
    // Ð¿ÐµÑ€ÐµÐ¼ÐµÑ�Ñ‚Ð¸Ñ‚Ñ�Ñ�
    // Ð½Ð°
    // Ð¾ÐºÐ½Ð¾
    // Ñ‡Ð°Ñ‚Ð°.
    public static final int OPENS_OR_CLOSES_THE_INVENTORY_WINDOW = 2505; // ÐžÑ‚ÐºÑ€Ñ‹Ð²Ð°ÐµÑ‚
    // Ð¸
    // Ð·Ð°ÐºÑ€Ñ‹Ð²Ð°ÐµÑ‚
    // Ð¾ÐºÐ½Ð¾
    // Ð¸Ð½Ð²ÐµÐ½Ñ‚Ð°Ñ€Ñ�.
    public static final int TEMPORARILY_HIDES_ALL_OPEN_WINDOWS = 2506; // Ð’Ñ€ÐµÐ¼ÐµÐ½Ð½Ð¾
    // Ð¿Ñ€Ñ�Ñ‡ÐµÑ‚
    // Ð²Ñ�Ðµ
    // Ð¾Ñ‚ÐºÑ€Ñ‹Ñ‚Ñ‹Ðµ
    // Ð¾ÐºÐ½Ð°.
    public static final int CLOSES_ALL_OPEN_WINDOWS = 2507; // Ð—Ð°ÐºÑ€Ñ‹Ð²Ð°ÐµÑ‚ Ð²Ñ�Ðµ
    // Ð¾ÐºÐ½Ð°.
    public static final int OPENS_THE_GM_MANAGER_WINDOW = 2508; // ÐžÑ‚ÐºÑ€Ñ‹Ð²Ð°ÐµÑ‚
    // Ð¾ÐºÐ½Ð¾
    // ÑƒÐ¿Ñ€Ð°Ð²Ð»ÐµÐ½Ð¸Ñ�
    // GM.
    public static final int OPENS_THE_GM_PETITION_WINDOW = 2509; // ÐžÑ‚ÐºÑ€Ñ‹Ð²Ð°ÐµÑ‚
    // Ð¾ÐºÐ½Ð¾ Ð¿ÐµÑ‚Ð¸Ñ†Ð¸Ð¹
    // GM.
    public static final int THE_BUFF_IN_THE_PARTY_WINDOW_IS_TOGGLED_BUFF_FOR_ONE_INPUT_DEBUFF_FOR_TWO_INPUTS_A_SONG_AND = 2510; // ÐŸÐ¾Ð»Ð¾Ð¶Ð¸Ñ‚ÐµÐ»ÑŒÐ½Ñ‹Ð¹
    // Ñ�Ñ„Ñ„ÐµÐºÑ‚
    // Ð²
    // Ð¾ÐºÐ½Ðµ
    // Ð³Ñ€ÑƒÐ¿Ð¿Ñ‹
    // Ð²ÐºÐ»ÑŽÑ‡ÐµÐ½.
    // ÐŸÐ¾Ð»Ð¾Ð¶Ð¸Ñ‚ÐµÐ»ÑŒÐ½Ñ‹Ð¹
    // Ñ�Ñ„Ñ„ÐµÐºÑ‚
    // Ð´Ð»Ñ�
    // Ð¾Ð´Ð½Ð¾Ð³Ð¾
    // Ð²Ð²Ð¾Ð´Ð°,
    // Ð¾Ñ‚Ñ€Ð¸Ñ†Ð°Ñ‚ÐµÐ»ÑŒÐ½Ñ‹Ð¹
    // Ñ�Ñ„Ñ„ÐµÐºÑ‚
    // Ð´Ð»Ñ�
    // Ð´Ð²ÑƒÑ…,
    // Ñ‚Ð°Ð½ÐµÑ†
    // Ð¸
    // Ð¿ÐµÐ½Ð¸Ðµ
    // Ð´Ð»Ñ�
    // Ñ‚Ñ€ÐµÑ…
    // Ð¸
    // Ð²Ñ‹ÐºÐ»ÑŽÑ‡ÐµÐ½Ð¸Ðµ
    // Ð´Ð»Ñ�
    // Ñ‡ÐµÑ‚Ñ‹Ñ€ÐµÑ….
    public static final int ACTIVATES_OR_DEACTIVATES_MINIMUM_FRAME_FUNCTION = 2511; // Ð�ÐºÑ‚Ð¸Ð²Ð¸Ñ€ÑƒÐµÑ‚
    // Ð¸
    // Ð´ÐµÐ·Ð°ÐºÑ‚Ð¸Ð²Ð¸Ñ€ÑƒÐµÑ‚
    // Ð¼Ð¸Ð½Ð¸Ð¼ÑƒÐ¼
    // Ð±Ð»Ð¾ÐºÐ¸Ñ€Ð¾Ð²Ð¾Ñ‡Ð½Ñ‹Ñ…
    // Ñ„ÑƒÐ½ÐºÑ†Ð¸Ð¹.
    public static final int RUNS_OR_CLOSES_THE_MSN_MESSENGER_WINDOW = 2512; // ÐžÑ‚ÐºÑ€Ñ‹Ð²Ð°ÐµÑ‚
    // Ð¸
    // Ð·Ð°ÐºÑ€Ñ‹Ð²Ð°ÐµÑ‚
    // Ð¾ÐºÐ½Ð¾
    // Ñ�Ð¾Ð¾Ð±Ñ‰ÐµÐ½Ð¸Ð¹
    // MSN.

    public static final int ASSIGN_1ST_SLOT_SHORTCUT_IN_THE_SHORTCUT_BASE_WINDOW_COMBINATION_OF_CTRL_AND_SHIFT_CANNOT_BE = 2513; // Ð�Ð°Ð·Ð½Ð°Ñ‡ÑŒÑ‚Ðµ
    // Ñ�Ñ€Ð»Ñ‹Ðº
    // Ðº
    // 1-Ð¼Ñƒ
    // Ñ�Ð»Ð¾Ñ‚Ñƒ
    // Ð²
    // Ð¾ÐºÐ½Ðµ
    // Ð±Ð°Ð·Ð¾Ð²Ñ‹Ñ…
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð².
    // ÐšÐ¾Ð¼Ð±Ð¸Ð½Ð°Ñ†Ð¸Ñ�
    // Ctrl+Shift
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð½Ð°Ð·Ð½Ð°Ñ‡ÐµÐ½Ð°.
    public static final int ASSIGN_2ND_SLOT_SHORTCUT_IN_THE_SHORTCUT_BASE_WINDOW_COMBINATION_OF_CTRL_AND_SHIFT_CANNOT_BE = 2514; // Ð�Ð°Ð·Ð½Ð°Ñ‡ÑŒÑ‚Ðµ
    // Ñ�Ñ€Ð»Ñ‹Ðº
    // Ðº
    // 2-Ð¼Ñƒ
    // Ñ�Ð»Ð¾Ñ‚Ñƒ
    // Ð²
    // Ð¾ÐºÐ½Ðµ
    // Ð±Ð°Ð·Ð¾Ð²Ñ‹Ñ…
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð².
    // ÐšÐ¾Ð¼Ð±Ð¸Ð½Ð°Ñ†Ð¸Ñ�
    // Ctrl+Shift
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð½Ð°Ð·Ð½Ð°Ñ‡ÐµÐ½Ð°.
    public static final int ASSIGN_3RD_SLOT_SHORTCUT_IN_THE_SHORTCUT_BASE_WINDOW_COMBINATION_OF_CTRL_AND_SHIFT_CANNOT_BE = 2515; // Ð�Ð°Ð·Ð½Ð°Ñ‡ÑŒÑ‚Ðµ
    // Ñ�Ñ€Ð»Ñ‹Ðº
    // Ðº
    // 3-Ð¼Ñƒ
    // Ñ�Ð»Ð¾Ñ‚Ñƒ
    // Ð²
    // Ð¾ÐºÐ½Ðµ
    // Ð±Ð°Ð·Ð¾Ð²Ñ‹Ñ…
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð².
    // ÐšÐ¾Ð¼Ð±Ð¸Ð½Ð°Ñ†Ð¸Ñ�
    // Ctrl+Shift
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð½Ð°Ð·Ð½Ð°Ñ‡ÐµÐ½Ð°.
    public static final int ASSIGN_4TH_SLOT_SHORTCUT_IN_THE_SHORTCUT_BASE_WINDOW_COMBINATION_OF_CTRL_AND_SHIFT_CANNOT_BE = 2516; // Ð�Ð°Ð·Ð½Ð°Ñ‡ÑŒÑ‚Ðµ
    // Ñ�Ñ€Ð»Ñ‹Ðº
    // Ðº
    // 4-Ð¼Ñƒ
    // Ñ�Ð»Ð¾Ñ‚Ñƒ
    // Ð²
    // Ð¾ÐºÐ½Ðµ
    // Ð±Ð°Ð·Ð¾Ð²Ñ‹Ñ…
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð².
    // ÐšÐ¾Ð¼Ð±Ð¸Ð½Ð°Ñ†Ð¸Ñ�
    // Ctrl+Shift
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð½Ð°Ð·Ð½Ð°Ñ‡ÐµÐ½Ð°.
    public static final int ASSIGN_5TH_SLOT_SHORTCUT_IN_THE_SHORTCUT_BASE_WINDOW_COMBINATION_OF_CTRL_AND_SHIFT_CANNOT_BE = 2517; // Ð�Ð°Ð·Ð½Ð°Ñ‡ÑŒÑ‚Ðµ
    // Ñ�Ñ€Ð»Ñ‹Ðº
    // Ðº
    // 5-Ð¼Ñƒ
    // Ñ�Ð»Ð¾Ñ‚Ñƒ
    // Ð²
    // Ð¾ÐºÐ½Ðµ
    // Ð±Ð°Ð·Ð¾Ð²Ñ‹Ñ…
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð².
    // ÐšÐ¾Ð¼Ð±Ð¸Ð½Ð°Ñ†Ð¸Ñ�
    // Ctrl+Shift
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð½Ð°Ð·Ð½Ð°Ñ‡ÐµÐ½Ð°.
    public static final int ASSIGN_6TH_SLOT_SHORTCUT_IN_THE_SHORTCUT_BASE_WINDOW_COMBINATION_OF_CTRL_AND_SHIFT_CANNOT_BE = 2518; // Ð�Ð°Ð·Ð½Ð°Ñ‡ÑŒÑ‚Ðµ
    // Ñ�Ñ€Ð»Ñ‹Ðº
    // Ðº
    // 6-Ð¼Ñƒ
    // Ñ�Ð»Ð¾Ñ‚Ñƒ
    // Ð²
    // Ð¾ÐºÐ½Ðµ
    // Ð±Ð°Ð·Ð¾Ð²Ñ‹Ñ…
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð².
    // ÐšÐ¾Ð¼Ð±Ð¸Ð½Ð°Ñ†Ð¸Ñ�
    // Ctrl+Shift
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð½Ð°Ð·Ð½Ð°Ñ‡ÐµÐ½Ð°.
    public static final int ASSIGN_7TH_SLOT_SHORTCUT_IN_THE_SHORTCUT_BASE_WINDOW_COMBINATION_OF_CTRL_AND_SHIFT_CANNOT_BE = 2519; // Ð�Ð°Ð·Ð½Ð°Ñ‡ÑŒÑ‚Ðµ
    // Ñ�Ñ€Ð»Ñ‹Ðº
    // Ðº
    // 7-Ð¼Ñƒ
    // Ñ�Ð»Ð¾Ñ‚Ñƒ
    // Ð²
    // Ð¾ÐºÐ½Ðµ
    // Ð±Ð°Ð·Ð¾Ð²Ñ‹Ñ…
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð².
    // ÐšÐ¾Ð¼Ð±Ð¸Ð½Ð°Ñ†Ð¸Ñ�
    // Ctrl+Shift
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð½Ð°Ð·Ð½Ð°Ñ‡ÐµÐ½Ð°.
    public static final int ASSIGN_8TH_SLOT_SHORTCUT_IN_THE_SHORTCUT_BASE_WINDOW_COMBINATION_OF_CTRL_AND_SHIFT_CANNOT_BE = 2520; // Ð�Ð°Ð·Ð½Ð°Ñ‡ÑŒÑ‚Ðµ
    // Ñ�Ñ€Ð»Ñ‹Ðº
    // Ðº
    // 8-Ð¼Ñƒ
    // Ñ�Ð»Ð¾Ñ‚Ñƒ
    // Ð²
    // Ð¾ÐºÐ½Ðµ
    // Ð±Ð°Ð·Ð¾Ð²Ñ‹Ñ…
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð².
    // ÐšÐ¾Ð¼Ð±Ð¸Ð½Ð°Ñ†Ð¸Ñ�
    // Ctrl+Shift
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð½Ð°Ð·Ð½Ð°Ñ‡ÐµÐ½Ð°.
    public static final int ASSIGN_9TH_SLOT_SHORTCUT_IN_THE_SHORTCUT_BASE_WINDOW_COMBINATION_OF_CTRL_AND_SHIFT_CANNOT_BE = 2521; // Ð�Ð°Ð·Ð½Ð°Ñ‡ÑŒÑ‚Ðµ
    // Ñ�Ñ€Ð»Ñ‹Ðº
    // Ðº
    // 9-Ð¼Ñƒ
    // Ñ�Ð»Ð¾Ñ‚Ñƒ
    // Ð²
    // Ð¾ÐºÐ½Ðµ
    // Ð±Ð°Ð·Ð¾Ð²Ñ‹Ñ…
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð².
    // ÐšÐ¾Ð¼Ð±Ð¸Ð½Ð°Ñ†Ð¸Ñ�
    // Ctrl+Shift
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð½Ð°Ð·Ð½Ð°Ñ‡ÐµÐ½Ð°.
    public static final int ASSIGN_10TH_SLOT_SHORTCUT_IN_THE_SHORTCUT_BASE_WINDOW_COMBINATION_OF_CTRL_AND_SHIFT_CANNOT_BE = 2522; // Ð�Ð°Ð·Ð½Ð°Ñ‡ÑŒÑ‚Ðµ
    // Ñ�Ñ€Ð»Ñ‹Ðº
    // Ðº
    // 10-Ð¼Ñƒ
    // Ñ�Ð»Ð¾Ñ‚Ñƒ
    // Ð²
    // Ð¾ÐºÐ½Ðµ
    // Ð±Ð°Ð·Ð¾Ð²Ñ‹Ñ…
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð².
    // ÐšÐ¾Ð¼Ð±Ð¸Ð½Ð°Ñ†Ð¸Ñ�
    // Ctrl+Shift
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð½Ð°Ð·Ð½Ð°Ñ‡ÐµÐ½Ð°.
    public static final int ASSIGN_11TH_SLOT_SHORTCUT_IN_THE_SHORTCUT_BASE_WINDOW_COMBINATION_OF_CTRL_AND_SHIFT_CANNOT_BE = 2523; // Ð�Ð°Ð·Ð½Ð°Ñ‡ÑŒÑ‚Ðµ
    // Ñ�Ñ€Ð»Ñ‹Ðº
    // Ðº
    // 11-Ð¼Ñƒ
    // Ñ�Ð»Ð¾Ñ‚Ñƒ
    // Ð²
    // Ð¾ÐºÐ½Ðµ
    // Ð±Ð°Ð·Ð¾Ð²Ñ‹Ñ…
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð².
    // ÐšÐ¾Ð¼Ð±Ð¸Ð½Ð°Ñ†Ð¸Ñ�
    // Ctrl+Shift
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð½Ð°Ð·Ð½Ð°Ñ‡ÐµÐ½Ð°.
    public static final int ASSIGN_12TH_SLOT_SHORTCUT_IN_THE_SHORTCUT_BASE_WINDOW_COMBINATION_OF_CTRL_AND_SHIFT_CANNOT_BE = 2524; // Ð�Ð°Ð·Ð½Ð°Ñ‡ÑŒÑ‚Ðµ
    // Ñ�Ñ€Ð»Ñ‹Ðº
    // Ðº
    // 12-Ð¼Ñƒ
    // Ñ�Ð»Ð¾Ñ‚Ñƒ
    // Ð²
    // Ð¾ÐºÐ½Ðµ
    // Ð±Ð°Ð·Ð¾Ð²Ñ‹Ñ…
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð².
    // ÐšÐ¾Ð¼Ð±Ð¸Ð½Ð°Ñ†Ð¸Ñ�
    // Ctrl+Shift
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð½Ð°Ð·Ð½Ð°Ñ‡ÐµÐ½Ð°.
    public static final int ASSIGN_1ST_SLOT_SHORTCUT_IN_THE_1ST_SHORTCUT_EXPANDED_WINDOW_COMBINATION_OF_CTRL_AND_SHIFT = 2525; // Ð�Ð°Ð·Ð½Ð°Ñ‡ÑŒÑ‚Ðµ
    // Ñ�Ñ€Ð»Ñ‹Ðº
    // Ðº
    // 1-Ð¼Ñƒ
    // Ñ�Ð»Ð¾Ñ‚Ñƒ
    // Ð²
    // 1-Ð¼
    // Ð¾ÐºÐ½Ðµ
    // Ñ€Ð°Ñ�ÑˆÐ¸Ñ€ÐµÐ½Ð½Ñ‹Ñ…
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð².
    // ÐšÐ¾Ð¼Ð±Ð¸Ð½Ð°Ñ†Ð¸Ñ�
    // Ctrl+Shift
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð½Ð°Ð·Ð½Ð°Ñ‡ÐµÐ½Ð°.
    public static final int ASSIGN_2ND_SLOT_SHORTCUT_IN_THE_1ST_SHORTCUT_EXPANDED_WINDOW_COMBINATION_OF_CTRL_AND_SHIFT = 2526; // Ð�Ð°Ð·Ð½Ð°Ñ‡ÑŒÑ‚Ðµ
    // Ñ�Ñ€Ð»Ñ‹Ðº
    // Ðº
    // 2-Ð¼Ñƒ
    // Ñ�Ð»Ð¾Ñ‚Ñƒ
    // Ð²
    // 1-Ð¼
    // Ð¾ÐºÐ½Ðµ
    // Ñ€Ð°Ñ�ÑˆÐ¸Ñ€ÐµÐ½Ð½Ñ‹Ñ…
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð².
    // ÐšÐ¾Ð¼Ð±Ð¸Ð½Ð°Ñ†Ð¸Ñ�
    // Ctrl+Shift
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð½Ð°Ð·Ð½Ð°Ñ‡ÐµÐ½Ð°.
    public static final int ASSIGN_3RD_SLOT_SHORTCUT_IN_THE_1ST_SHORTCUT_EXPANDED_WINDOW_COMBINATION_OF_CTRL_AND_SHIFT = 2527; // Ð�Ð°Ð·Ð½Ð°Ñ‡ÑŒÑ‚Ðµ
    // Ñ�Ñ€Ð»Ñ‹Ðº
    // Ðº
    // 3-Ð¼Ñƒ
    // Ñ�Ð»Ð¾Ñ‚Ñƒ
    // Ð²
    // 1-Ð¼
    // Ð¾ÐºÐ½Ðµ
    // Ñ€Ð°Ñ�ÑˆÐ¸Ñ€ÐµÐ½Ð½Ñ‹Ñ…
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð².
    // ÐšÐ¾Ð¼Ð±Ð¸Ð½Ð°Ñ†Ð¸Ñ�
    // Ctrl+Shift
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð½Ð°Ð·Ð½Ð°Ñ‡ÐµÐ½Ð°.
    public static final int ASSIGN_4TH_SLOT_SHORTCUT_IN_THE_1ST_SHORTCUT_EXPANDED_WINDOW_COMBINATION_OF_CTRL_AND_SHIFT = 2528; // Ð�Ð°Ð·Ð½Ð°Ñ‡ÑŒÑ‚Ðµ
    // Ñ�Ñ€Ð»Ñ‹Ðº
    // Ðº
    // 4-Ð¼Ñƒ
    // Ñ�Ð»Ð¾Ñ‚Ñƒ
    // Ð²
    // 1-Ð¼
    // Ð¾ÐºÐ½Ðµ
    // Ñ€Ð°Ñ�ÑˆÐ¸Ñ€ÐµÐ½Ð½Ñ‹Ñ…
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð².
    // ÐšÐ¾Ð¼Ð±Ð¸Ð½Ð°Ñ†Ð¸Ñ�
    // Ctrl+Shift
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð½Ð°Ð·Ð½Ð°Ñ‡ÐµÐ½Ð°.
    public static final int ASSIGN_5TH_SLOT_SHORTCUT_IN_THE_1ST_SHORTCUT_EXPANDED_WINDOW_COMBINATION_OF_CTRL_AND_SHIFT = 2529; // Ð�Ð°Ð·Ð½Ð°Ñ‡ÑŒÑ‚Ðµ
    // Ñ�Ñ€Ð»Ñ‹Ðº
    // Ðº
    // 5-Ð¼Ñƒ
    // Ñ�Ð»Ð¾Ñ‚Ñƒ
    // Ð²
    // 1-Ð¼
    // Ð¾ÐºÐ½Ðµ
    // Ñ€Ð°Ñ�ÑˆÐ¸Ñ€ÐµÐ½Ð½Ñ‹Ñ…
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð².
    // ÐšÐ¾Ð¼Ð±Ð¸Ð½Ð°Ñ†Ð¸Ñ�
    // Ctrl+Shift
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð½Ð°Ð·Ð½Ð°Ñ‡ÐµÐ½Ð°.
    public static final int ASSIGN_6TH_SLOT_SHORTCUT_IN_THE_1ST_SHORTCUT_EXPANDED_WINDOW_COMBINATION_OF_CTRL_AND_SHIFT = 2530; // Ð�Ð°Ð·Ð½Ð°Ñ‡ÑŒÑ‚Ðµ
    // Ñ�Ñ€Ð»Ñ‹Ðº
    // Ðº
    // 6-Ð¼Ñƒ
    // Ñ�Ð»Ð¾Ñ‚Ñƒ
    // Ð²
    // 1-Ð¼
    // Ð¾ÐºÐ½Ðµ
    // Ñ€Ð°Ñ�ÑˆÐ¸Ñ€ÐµÐ½Ð½Ñ‹Ñ…
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð².
    // ÐšÐ¾Ð¼Ð±Ð¸Ð½Ð°Ñ†Ð¸Ñ�
    // Ctrl+Shift
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð½Ð°Ð·Ð½Ð°Ñ‡ÐµÐ½Ð°.
    public static final int ASSIGN_7TH_SLOT_SHORTCUT_IN_THE_1ST_SHORTCUT_EXPANDED_WINDOW_COMBINATION_OF_CTRL_AND_SHIFT = 2531; // Ð�Ð°Ð·Ð½Ð°Ñ‡ÑŒÑ‚Ðµ
    // Ñ�Ñ€Ð»Ñ‹Ðº
    // Ðº
    // 7-Ð¼Ñƒ
    // Ñ�Ð»Ð¾Ñ‚Ñƒ
    // Ð²
    // 1-Ð¼
    // Ð¾ÐºÐ½Ðµ
    // Ñ€Ð°Ñ�ÑˆÐ¸Ñ€ÐµÐ½Ð½Ñ‹Ñ…
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð².
    // ÐšÐ¾Ð¼Ð±Ð¸Ð½Ð°Ñ†Ð¸Ñ�
    // Ctrl+Shift
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð½Ð°Ð·Ð½Ð°Ñ‡ÐµÐ½Ð°.
    public static final int ASSIGN_8TH_SLOT_SHORTCUT_IN_THE_1ST_SHORTCUT_EXPANDED_WINDOW_COMBINATION_OF_CTRL_AND_SHIFT = 2532; // Ð�Ð°Ð·Ð½Ð°Ñ‡ÑŒÑ‚Ðµ
    // Ñ�Ñ€Ð»Ñ‹Ðº
    // Ðº
    // 8-Ð¼Ñƒ
    // Ñ�Ð»Ð¾Ñ‚Ñƒ
    // Ð²
    // 1-Ð¼
    // Ð¾ÐºÐ½Ðµ
    // Ñ€Ð°Ñ�ÑˆÐ¸Ñ€ÐµÐ½Ð½Ñ‹Ñ…
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð².
    // ÐšÐ¾Ð¼Ð±Ð¸Ð½Ð°Ñ†Ð¸Ñ�
    // Ctrl+Shift
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð½Ð°Ð·Ð½Ð°Ñ‡ÐµÐ½Ð°.
    public static final int ASSIGN_9TH_SLOT_SHORTCUT_IN_THE_1ST_SHORTCUT_EXPANDED_WINDOW_COMBINATION_OF_CTRL_AND_SHIFT = 2533; // Ð�Ð°Ð·Ð½Ð°Ñ‡ÑŒÑ‚Ðµ
    // Ñ�Ñ€Ð»Ñ‹Ðº
    // Ðº
    // 9-Ð¼Ñƒ
    // Ñ�Ð»Ð¾Ñ‚Ñƒ
    // Ð²
    // 1-Ð¼
    // Ð¾ÐºÐ½Ðµ
    // Ñ€Ð°Ñ�ÑˆÐ¸Ñ€ÐµÐ½Ð½Ñ‹Ñ…
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð².
    // ÐšÐ¾Ð¼Ð±Ð¸Ð½Ð°Ñ†Ð¸Ñ�
    // Ctrl+Shift
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð½Ð°Ð·Ð½Ð°Ñ‡ÐµÐ½Ð°.
    public static final int ASSIGN_10TH_SLOT_SHORTCUT_IN_THE_1ST_SHORTCUT_EXPANDED_WINDOW_COMBINATION_OF_CTRL_AND_SHIFT = 2534; // Ð�Ð°Ð·Ð½Ð°Ñ‡ÑŒÑ‚Ðµ
    // Ñ�Ñ€Ð»Ñ‹Ðº
    // Ðº
    // 10-Ð¼Ñƒ
    // Ñ�Ð»Ð¾Ñ‚Ñƒ
    // Ð²
    // 1-Ð¼
    // Ð¾ÐºÐ½Ðµ
    // Ñ€Ð°Ñ�ÑˆÐ¸Ñ€ÐµÐ½Ð½Ñ‹Ñ…
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð².
    // ÐšÐ¾Ð¼Ð±Ð¸Ð½Ð°Ñ†Ð¸Ñ�
    // Ctrl+Shift
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð½Ð°Ð·Ð½Ð°Ñ‡ÐµÐ½Ð°.
    public static final int ASSIGN_11TH_SLOT_SHORTCUT_IN_THE_1ST_SHORTCUT_EXPANDED_WINDOW_COMBINATION_OF_CTRL_AND_SHIFT = 2535; // Ð�Ð°Ð·Ð½Ð°Ñ‡ÑŒÑ‚Ðµ
    // Ñ�Ñ€Ð»Ñ‹Ðº
    // Ðº
    // 11-Ð¼Ñƒ
    // Ñ�Ð»Ð¾Ñ‚Ñƒ
    // Ð²
    // 1-Ð¼
    // Ð¾ÐºÐ½Ðµ
    // Ñ€Ð°Ñ�ÑˆÐ¸Ñ€ÐµÐ½Ð½Ñ‹Ñ…
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð².
    // ÐšÐ¾Ð¼Ð±Ð¸Ð½Ð°Ñ†Ð¸Ñ�
    // Ctrl+Shift
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð½Ð°Ð·Ð½Ð°Ñ‡ÐµÐ½Ð°.
    public static final int ASSIGN_12TH_SLOT_SHORTCUT_IN_THE_1ST_SHORTCUT_EXPANDED_WINDOW_COMBINATION_OF_CTRL_AND_SHIFT = 2536; // Ð�Ð°Ð·Ð½Ð°Ñ‡ÑŒÑ‚Ðµ
    // Ñ�Ñ€Ð»Ñ‹Ðº
    // Ðº
    // 12-Ð¼Ñƒ
    // Ñ�Ð»Ð¾Ñ‚Ñƒ
    // Ð²
    // 1-Ð¼
    // Ð¾ÐºÐ½Ðµ
    // Ñ€Ð°Ñ�ÑˆÐ¸Ñ€ÐµÐ½Ð½Ñ‹Ñ…
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð².
    // ÐšÐ¾Ð¼Ð±Ð¸Ð½Ð°Ñ†Ð¸Ñ�
    // Ctrl+Shift
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð½Ð°Ð·Ð½Ð°Ñ‡ÐµÐ½Ð°.
    public static final int ASSIGN_1ST_SLOT_SHORTCUT_IN_THE_2ND_SHORTCUT_EXPANDED_WINDOW_COMBINATION_OF_CTRL_AND_SHIFT = 2537; // Ð�Ð°Ð·Ð½Ð°Ñ‡ÑŒÑ‚Ðµ
    // Ñ�Ñ€Ð»Ñ‹Ðº
    // Ðº
    // 1-Ð¼Ñƒ
    // Ñ�Ð»Ð¾Ñ‚Ñƒ
    // Ð²
    // 2-Ð¼
    // Ð¾ÐºÐ½Ðµ
    // Ñ€Ð°Ñ�ÑˆÐ¸Ñ€ÐµÐ½Ð½Ñ‹Ñ…
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð².
    // ÐšÐ¾Ð¼Ð±Ð¸Ð½Ð°Ñ†Ð¸Ñ�
    // Ctrl+Shift
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð½Ð°Ð·Ð½Ð°Ñ‡ÐµÐ½Ð°.
    public static final int ASSIGN_2ND_SLOT_SHORTCUT_IN_THE_2ND_SHORTCUT_EXPANDED_WINDOW_COMBINATION_OF_CTRL_AND_SHIFT = 2538; // Ð�Ð°Ð·Ð½Ð°Ñ‡ÑŒÑ‚Ðµ
    // Ñ�Ñ€Ð»Ñ‹Ðº
    // Ðº
    // 2-Ð¼Ñƒ
    // Ñ�Ð»Ð¾Ñ‚Ñƒ
    // Ð²
    // 2-Ð¼
    // Ð¾ÐºÐ½Ðµ
    // Ñ€Ð°Ñ�ÑˆÐ¸Ñ€ÐµÐ½Ð½Ñ‹Ñ…
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð².
    // ÐšÐ¾Ð¼Ð±Ð¸Ð½Ð°Ñ†Ð¸Ñ�
    // Ctrl+Shift
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð½Ð°Ð·Ð½Ð°Ñ‡ÐµÐ½Ð°.
    public static final int ASSIGN_3RD_SLOT_SHORTCUT_IN_THE_2ND_SHORTCUT_EXPANDED_WINDOW_COMBINATION_OF_CTRL_AND_SHIFT = 2539; // Ð�Ð°Ð·Ð½Ð°Ñ‡ÑŒÑ‚Ðµ
    // Ñ�Ñ€Ð»Ñ‹Ðº
    // Ðº
    // 3-Ð¼Ñƒ
    // Ñ�Ð»Ð¾Ñ‚Ñƒ
    // Ð²
    // 2-Ð¼
    // Ð¾ÐºÐ½Ðµ
    // Ñ€Ð°Ñ�ÑˆÐ¸Ñ€ÐµÐ½Ð½Ñ‹Ñ…
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð².
    // ÐšÐ¾Ð¼Ð±Ð¸Ð½Ð°Ñ†Ð¸Ñ�
    // Ctrl+Shift
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð½Ð°Ð·Ð½Ð°Ñ‡ÐµÐ½Ð°.
    public static final int ASSIGN_4TH_SLOT_SHORTCUT_IN_THE_2ND_SHORTCUT_EXPANDED_WINDOW_COMBINATION_OF_CTRL_AND_SHIFT = 2540; // Ð�Ð°Ð·Ð½Ð°Ñ‡ÑŒÑ‚Ðµ
    // Ñ�Ñ€Ð»Ñ‹Ðº
    // Ðº
    // 4-Ð¼Ñƒ
    // Ñ�Ð»Ð¾Ñ‚Ñƒ
    // Ð²
    // 2-Ð¼
    // Ð¾ÐºÐ½Ðµ
    // Ñ€Ð°Ñ�ÑˆÐ¸Ñ€ÐµÐ½Ð½Ñ‹Ñ…
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð².
    // ÐšÐ¾Ð¼Ð±Ð¸Ð½Ð°Ñ†Ð¸Ñ�
    // Ctrl+Shift
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð½Ð°Ð·Ð½Ð°Ñ‡ÐµÐ½Ð°.
    public static final int ASSIGN_5TH_SLOT_SHORTCUT_IN_THE_2ND_SHORTCUT_EXPANDED_WINDOW_COMBINATION_OF_CTRL_AND_SHIFT = 2541; // Ð�Ð°Ð·Ð½Ð°Ñ‡ÑŒÑ‚Ðµ
    // Ñ�Ñ€Ð»Ñ‹Ðº
    // Ðº
    // 5-Ð¼Ñƒ
    // Ñ�Ð»Ð¾Ñ‚Ñƒ
    // Ð²
    // 2-Ð¼
    // Ð¾ÐºÐ½Ðµ
    // Ñ€Ð°Ñ�ÑˆÐ¸Ñ€ÐµÐ½Ð½Ñ‹Ñ…
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð².
    // ÐšÐ¾Ð¼Ð±Ð¸Ð½Ð°Ñ†Ð¸Ñ�
    // Ctrl+Shift
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð½Ð°Ð·Ð½Ð°Ñ‡ÐµÐ½Ð°.
    public static final int ASSIGN_6TH_SLOT_SHORTCUT_IN_THE_2ND_SHORTCUT_EXPANDED_WINDOW_COMBINATION_OF_CTRL_AND_SHIFT = 2542; // Ð�Ð°Ð·Ð½Ð°Ñ‡ÑŒÑ‚Ðµ
    // Ñ�Ñ€Ð»Ñ‹Ðº
    // Ðº
    // 6-Ð¼Ñƒ
    // Ñ�Ð»Ð¾Ñ‚Ñƒ
    // Ð²
    // 2-Ð¼
    // Ð¾ÐºÐ½Ðµ
    // Ñ€Ð°Ñ�ÑˆÐ¸Ñ€ÐµÐ½Ð½Ñ‹Ñ…
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð².
    // ÐšÐ¾Ð¼Ð±Ð¸Ð½Ð°Ñ†Ð¸Ñ�
    // Ctrl+Shift
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð½Ð°Ð·Ð½Ð°Ñ‡ÐµÐ½Ð°.
    public static final int ASSIGN_7TH_SLOT_SHORTCUT_IN_THE_2ND_SHORTCUT_EXPANDED_WINDOW_COMBINATION_OF_CTRL_AND_SHIFT = 2543; // Ð�Ð°Ð·Ð½Ð°Ñ‡ÑŒÑ‚Ðµ
    // Ñ�Ñ€Ð»Ñ‹Ðº
    // Ðº
    // 7-Ð¼Ñƒ
    // Ñ�Ð»Ð¾Ñ‚Ñƒ
    // Ð²
    // 2-Ð¼
    // Ð¾ÐºÐ½Ðµ
    // Ñ€Ð°Ñ�ÑˆÐ¸Ñ€ÐµÐ½Ð½Ñ‹Ñ…
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð².
    // ÐšÐ¾Ð¼Ð±Ð¸Ð½Ð°Ñ†Ð¸Ñ�
    // Ctrl+Shift
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð½Ð°Ð·Ð½Ð°Ñ‡ÐµÐ½Ð°.
    public static final int ASSIGN_8TH_SLOT_SHORTCUT_IN_THE_2ND_SHORTCUT_EXPANDED_WINDOW_COMBINATION_OF_CTRL_AND_SHIFT = 2544; // Ð�Ð°Ð·Ð½Ð°Ñ‡ÑŒÑ‚Ðµ
    // Ñ�Ñ€Ð»Ñ‹Ðº
    // Ðº
    // 8-Ð¼Ñƒ
    // Ñ�Ð»Ð¾Ñ‚Ñƒ
    // Ð²
    // 2-Ð¼
    // Ð¾ÐºÐ½Ðµ
    // Ñ€Ð°Ñ�ÑˆÐ¸Ñ€ÐµÐ½Ð½Ñ‹Ñ…
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð².
    // ÐšÐ¾Ð¼Ð±Ð¸Ð½Ð°Ñ†Ð¸Ñ�
    // Ctrl+Shift
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð½Ð°Ð·Ð½Ð°Ñ‡ÐµÐ½Ð°.
    public static final int ASSIGN_9TH_SLOT_SHORTCUT_IN_THE_2ND_SHORTCUT_EXPANDED_WINDOW_COMBINATION_OF_CTRL_AND_SHIFT = 2545; // Ð�Ð°Ð·Ð½Ð°Ñ‡ÑŒÑ‚Ðµ
    // Ñ�Ñ€Ð»Ñ‹Ðº
    // Ðº
    // 9-Ð¼Ñƒ
    // Ñ�Ð»Ð¾Ñ‚Ñƒ
    // Ð²
    // 2-Ð¼
    // Ð¾ÐºÐ½Ðµ
    // Ñ€Ð°Ñ�ÑˆÐ¸Ñ€ÐµÐ½Ð½Ñ‹Ñ…
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð².
    // ÐšÐ¾Ð¼Ð±Ð¸Ð½Ð°Ñ†Ð¸Ñ�
    // Ctrl+Shift
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð½Ð°Ð·Ð½Ð°Ñ‡ÐµÐ½Ð°.
    public static final int ASSIGN_10TH_SLOT_SHORTCUT_IN_THE_2ND_SHORTCUT_EXPANDED_WINDOW_COMBINATION_OF_CTRL_AND_SHIFT = 2546; // Ð�Ð°Ð·Ð½Ð°Ñ‡ÑŒÑ‚Ðµ
    // Ñ�Ñ€Ð»Ñ‹Ðº
    // Ðº
    // 10-Ð¼Ñƒ
    // Ñ�Ð»Ð¾Ñ‚Ñƒ
    // Ð²
    // 2-Ð¼
    // Ð¾ÐºÐ½Ðµ
    // Ñ€Ð°Ñ�ÑˆÐ¸Ñ€ÐµÐ½Ð½Ñ‹Ñ…
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð².
    // ÐšÐ¾Ð¼Ð±Ð¸Ð½Ð°Ñ†Ð¸Ñ�
    // Ctrl+Shift
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð½Ð°Ð·Ð½Ð°Ñ‡ÐµÐ½Ð°.
    public static final int ASSIGN_11TH_SLOT_SHORTCUT_IN_THE_2ND_SHORTCUT_EXPANDED_WINDOW_COMBINATION_OF_CTRL_AND_SHIFT = 2547; // Ð�Ð°Ð·Ð½Ð°Ñ‡ÑŒÑ‚Ðµ
    // Ñ�Ñ€Ð»Ñ‹Ðº
    // Ðº
    // 11-Ð¼Ñƒ
    // Ñ�Ð»Ð¾Ñ‚Ñƒ
    // Ð²
    // 2-Ð¼
    // Ð¾ÐºÐ½Ðµ
    // Ñ€Ð°Ñ�ÑˆÐ¸Ñ€ÐµÐ½Ð½Ñ‹Ñ…
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð².
    // ÐšÐ¾Ð¼Ð±Ð¸Ð½Ð°Ñ†Ð¸Ñ�
    // Ctrl+Shift
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð½Ð°Ð·Ð½Ð°Ñ‡ÐµÐ½Ð°.
    public static final int ASSIGN_12TH_SLOT_SHORTCUT_IN_THE_2ND_SHORTCUT_EXPANDED_WINDOW_COMBINATION_OF_CTRL_AND_SHIFT = 2548; // Ð�Ð°Ð·Ð½Ð°Ñ‡ÑŒÑ‚Ðµ
    // Ñ�Ñ€Ð»Ñ‹Ðº
    // Ðº
    // 12-Ð¼Ñƒ
    // Ñ�Ð»Ð¾Ñ‚Ñƒ
    // Ð²
    // 2-Ð¼
    // Ð¾ÐºÐ½Ðµ
    // Ñ€Ð°Ñ�ÑˆÐ¸Ñ€ÐµÐ½Ð½Ñ‹Ñ…
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð².
    // ÐšÐ¾Ð¼Ð±Ð¸Ð½Ð°Ñ†Ð¸Ñ�
    // Ctrl+Shift
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð½Ð°Ð·Ð½Ð°Ñ‡ÐµÐ½Ð°.

    public static final int MOVE_THE_SHORTCUT_PAGE_IN_THE_SHORTCUT_BASE_WINDOW_TO_PAGE_1 = 2549; // ÐŸÐµÑ€ÐµÐ¼ÐµÑ�Ñ‚Ð¸Ñ‚Ðµ
    // Ð»Ð¸Ñ�Ñ‚
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð²
    // Ð²
    // Ð¾ÐºÐ½Ð¾
    // Ð±Ð°Ð·Ð¾Ð²Ñ‹Ñ…
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð²
    // Ð½Ð°
    // Ñ�Ñ‚Ñ€.
    // 1.
    public static final int MOVE_THE_SHORTCUT_PAGE_IN_THE_SHORTCUT_BASE_WINDOW_TO_PAGE_2 = 2550; // ÐŸÐµÑ€ÐµÐ¼ÐµÑ�Ñ‚Ð¸Ñ‚Ðµ
    // Ð»Ð¸Ñ�Ñ‚
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð²
    // Ð²
    // Ð¾ÐºÐ½Ð¾
    // Ð±Ð°Ð·Ð¾Ð²Ñ‹Ñ…
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð²
    // Ð½Ð°
    // Ñ�Ñ‚Ñ€.
    // 2.
    public static final int MOVE_THE_SHORTCUT_PAGE_IN_THE_SHORTCUT_BASE_WINDOW_TO_PAGE_3 = 2551; // ÐŸÐµÑ€ÐµÐ¼ÐµÑ�Ñ‚Ð¸Ñ‚Ðµ
    // Ð»Ð¸Ñ�Ñ‚
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð²
    // Ð²
    // Ð¾ÐºÐ½Ð¾
    // Ð±Ð°Ð·Ð¾Ð²Ñ‹Ñ…
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð²
    // Ð½Ð°
    // Ñ�Ñ‚Ñ€.
    // 3.
    public static final int MOVE_THE_SHORTCUT_PAGE_IN_THE_SHORTCUT_BASE_WINDOW_TO_PAGE_4 = 2552; // ÐŸÐµÑ€ÐµÐ¼ÐµÑ�Ñ‚Ð¸Ñ‚Ðµ
    // Ð»Ð¸Ñ�Ñ‚
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð²
    // Ð²
    // Ð¾ÐºÐ½Ð¾
    // Ð±Ð°Ð·Ð¾Ð²Ñ‹Ñ…
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð²
    // Ð½Ð°
    // Ñ�Ñ‚Ñ€.
    // 4.
    public static final int MOVE_THE_SHORTCUT_PAGE_IN_THE_SHORTCUT_BASE_WINDOW_TO_PAGE_5 = 2553; // ÐŸÐµÑ€ÐµÐ¼ÐµÑ�Ñ‚Ð¸Ñ‚Ðµ
    // Ð»Ð¸Ñ�Ñ‚
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð²
    // Ð²
    // Ð¾ÐºÐ½Ð¾
    // Ð±Ð°Ð·Ð¾Ð²Ñ‹Ñ…
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð²
    // Ð½Ð°
    // Ñ�Ñ‚Ñ€.
    // 5.
    public static final int MOVE_THE_SHORTCUT_PAGE_IN_THE_SHORTCUT_BASE_WINDOW_TO_PAGE_6 = 2554; // ÐŸÐµÑ€ÐµÐ¼ÐµÑ�Ñ‚Ð¸Ñ‚Ðµ
    // Ð»Ð¸Ñ�Ñ‚
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð²
    // Ð²
    // Ð¾ÐºÐ½Ð¾
    // Ð±Ð°Ð·Ð¾Ð²Ñ‹Ñ…
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð²
    // Ð½Ð°
    // Ñ�Ñ‚Ñ€.
    // 6.
    public static final int MOVE_THE_SHORTCUT_PAGE_IN_THE_SHORTCUT_BASE_WINDOW_TO_PAGE_7 = 2555; // ÐŸÐµÑ€ÐµÐ¼ÐµÑ�Ñ‚Ð¸Ñ‚Ðµ
    // Ð»Ð¸Ñ�Ñ‚
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð²
    // Ð²
    // Ð¾ÐºÐ½Ð¾
    // Ð±Ð°Ð·Ð¾Ð²Ñ‹Ñ…
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð²
    // Ð½Ð°
    // Ñ�Ñ‚Ñ€.
    // 7.
    public static final int MOVE_THE_SHORTCUT_PAGE_IN_THE_SHORTCUT_BASE_WINDOW_TO_PAGE_8 = 2556; // ÐŸÐµÑ€ÐµÐ¼ÐµÑ�Ñ‚Ð¸Ñ‚Ðµ
    // Ð»Ð¸Ñ�Ñ‚
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð²
    // Ð²
    // Ð¾ÐºÐ½Ð¾
    // Ð±Ð°Ð·Ð¾Ð²Ñ‹Ñ…
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð²
    // Ð½Ð°
    // Ñ�Ñ‚Ñ€.
    // 8.
    public static final int MOVE_THE_SHORTCUT_PAGE_IN_THE_SHORTCUT_BASE_WINDOW_TO_PAGE_9 = 2557; // ÐŸÐµÑ€ÐµÐ¼ÐµÑ�Ñ‚Ð¸Ñ‚Ðµ
    // Ð»Ð¸Ñ�Ñ‚
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð²
    // Ð²
    // Ð¾ÐºÐ½Ð¾
    // Ð±Ð°Ð·Ð¾Ð²Ñ‹Ñ…
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð²
    // Ð½Ð°
    // Ñ�Ñ‚Ñ€.
    // 9.
    public static final int MOVE_THE_SHORTCUT_PAGE_IN_THE_SHORTCUT_BASE_WINDOW_TO_PAGE_10 = 2558; // ÐŸÐµÑ€ÐµÐ¼ÐµÑ�Ñ‚Ð¸Ñ‚Ðµ
    // Ð»Ð¸Ñ�Ñ‚
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð²
    // Ð²
    // Ð¾ÐºÐ½Ð¾
    // Ð±Ð°Ð·Ð¾Ð²Ñ‹Ñ…
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð²
    // Ð½Ð°
    // Ñ�Ñ‚Ñ€.
    // 10.

    public static final int OPENS_AND_CLOSES_THE_ACTION_WINDOW_EXECUTING_CHARACTER_ACTIONS_AND_GAME_COMMANDS = 2559; // ÐžÑ‚ÐºÑ€Ñ‹Ð²Ð°ÐµÑ‚
    // Ð¸
    // Ð·Ð°ÐºÑ€Ñ‹Ð²Ð°ÐµÑ‚
    // Ð¾ÐºÐ½Ð¾
    // Ð´ÐµÐ¹Ñ�Ñ‚Ð²Ð¸Ð¹,
    // Ð¸Ñ�Ð¿Ð¾Ð»Ð½Ñ�ÑŽÑ‰ÐµÐµ
    // Ð´ÐµÐ¹Ñ�Ñ‚Ð²Ð¸Ñ�
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°
    // Ð¸
    // Ð¸Ð³Ñ€Ð¾Ð²Ñ‹Ðµ
    // ÐºÐ¾Ð¼Ð°Ð½Ð´Ñ‹.
    public static final int OPENS_AND_CLOSES_THE_GAME_BULLETIN_BOARD = 2560; // ÐžÑ‚ÐºÑ€Ñ‹Ð²Ð°ÐµÑ‚
    // Ð¸
    // Ð·Ð°ÐºÑ€Ñ‹Ð²Ð°ÐµÑ‚
    // Ð´Ð¾Ñ�ÐºÑƒ
    // Ð¾Ð±ÑŠÑ�Ð²Ð»ÐµÐ½Ð¸Ð¹.
    public static final int OPENS_AND_CLOSES_THE_CALCULATOR = 2561; // ÐžÑ‚ÐºÑ€Ñ‹Ð²Ð°ÐµÑ‚
    // Ð¸
    // Ð·Ð°ÐºÑ€Ñ‹Ð²Ð°ÐµÑ‚
    // ÐºÐ°Ð»ÑŒÐºÑƒÐ»Ñ�Ñ‚Ð¾Ñ€.
    public static final int HIDES_OR_SHOWS_THE_CHAT_WINDOW_THE_WINDOW_ALWAYS_SHOWS_BY_DEFAULT = 2562; // ÐŸÑ€Ñ�Ñ‡ÐµÑ‚
    // Ð¸
    // Ð¿Ð¾ÐºÐ°Ð·Ñ‹Ð²Ð°ÐµÑ‚
    // Ð¾ÐºÐ½Ð¾
    // Ñ‡Ð°Ñ‚Ð°.
    // ÐŸÐ¾
    // ÑƒÐ¼Ð¾Ð»Ñ‡Ð°Ð½Ð¸ÑŽ
    // Ð¾Ð½Ð¾
    // Ð²Ñ�ÐµÐ³Ð´Ð°
    // Ð¿Ð¾ÐºÐ°Ð·Ñ‹Ð²Ð°ÐµÑ‚Ñ�Ñ�.
    public static final int OPENS_AND_CLOSES_THE_CLAN_WINDOW_CONFIRMING_INFORMATION_OF_THE_INCLUDED_CLAN_AND_PERFORMS_THE = 2563; // ÐžÑ‚ÐºÑ€Ñ‹Ð²Ð°ÐµÑ‚
    // Ð¸
    // Ð·Ð°ÐºÑ€Ñ‹Ð²Ð°ÐµÑ‚
    // Ð¾ÐºÐ½Ð¾
    // ÐºÐ»Ð°Ð½Ð°,
    // Ð¿Ñ€ÐµÐ´Ð¾Ñ�Ñ‚Ð°Ð²Ð»Ñ�ÑŽÑ‰ÐµÐµ
    // Ð¸Ð½Ñ„Ð¾Ñ€Ð¼Ð°Ñ†Ð¸ÑŽ
    // Ð¾
    // ÐºÐ»Ð°Ð½Ðµ
    // Ð¸
    // Ñ€Ð°Ð·Ð½Ð¾Ð¾Ð±Ñ€Ð°Ð·Ð½Ñ‹Ðµ
    // ÑƒÑ�Ñ‚Ð°Ð½Ð¾Ð²ÐºÐ¸
    // ÐºÐ°Ñ�Ð°Ñ‚ÐµÐ»ÑŒÐ½Ð¾
    // ÐºÐ»Ð°Ð½Ð°.
    public static final int OPENS_AND_CLOSES_THE_STATUS_WINDOW_SHOWING_THE_DETAILED_STATUS_OF_A_CHARACTER_THAT_YOU_CREATED = 2564; // ÐžÑ‚ÐºÑ€Ñ‹Ð²Ð°ÐµÑ‚
    // Ð¸
    // Ð·Ð°ÐºÑ€Ñ‹Ð²Ð°ÐµÑ‚
    // Ð¾ÐºÐ½Ð¾
    // Ñ�Ñ‚Ð°Ñ‚ÑƒÑ�Ð°,
    // Ð¿Ð¾ÐºÐ°Ð·Ñ‹Ð²Ð°ÑŽÑ‰ÐµÐµ
    // Ð´ÐµÑ‚Ð°Ð»Ð¸
    // Ñ�Ð¾Ñ�Ñ‚Ð¾Ñ�Ð½Ð¸Ñ�
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°.
    public static final int OPENS_AND_CLOSES_THE_HELP_WINDOW = 2565; // ÐžÑ‚ÐºÑ€Ñ‹Ð²Ð°ÐµÑ‚
    // Ð¸
    // Ð·Ð°ÐºÑ€Ñ‹Ð²Ð°ÐµÑ‚
    // Ð¾ÐºÐ½Ð¾
    // Ð¿Ð¾Ð¼Ð¾Ñ‰Ð¸.
    public static final int OPENS_OR_CLOSES_THE_INVENTORY_WINDOW_ = 2566; // ÐžÑ‚ÐºÑ€Ñ‹Ð²Ð°ÐµÑ‚
    // Ð¸
    // Ð·Ð°ÐºÑ€Ñ‹Ð²Ð°ÐµÑ‚
    // Ð¾ÐºÐ½Ð¾
    // Ð¸Ð½Ð²ÐµÐ½Ñ‚Ð°Ñ€Ñ�.
    public static final int OPENS_AND_CLOSES_THE_MACRO_WINDOW_FOR_MACRO_SETTINGS = 2567; // ÐžÑ‚ÐºÑ€Ñ‹Ð²Ð°ÐµÑ‚
    // Ð¸
    // Ð·Ð°ÐºÑ€Ñ‹Ð²Ð°ÐµÑ‚
    // Ð¾ÐºÐ½Ð¾
    // Ð½Ð°Ñ�Ñ‚Ñ€Ð¾ÐµÐº
    // Ð¼Ð°ÐºÑ€Ð¾Ñ�Ð¾Ð².
    public static final int OPENS_AND_CLOSES_THE_SKILL_WINDOW_DISPLAYING_THE_LIST_OF_SKILLS_THAT_YOU_CAN_USE = 2568; // ÐžÑ‚ÐºÑ€Ñ‹Ð²Ð°ÐµÑ‚
    // Ð¸
    // Ð·Ð°ÐºÑ€Ñ‹Ð²Ð°ÐµÑ‚
    // Ð¾ÐºÐ½Ð¾
    // ÑƒÐ¼ÐµÐ½Ð¸Ð¹,
    // Ð¾Ñ‚Ð¾Ð±Ñ€Ð°Ð¶Ð°ÑŽÑ‰ÐµÐµ
    // Ð¿ÐµÑ€ÐµÑ‡ÐµÐ½ÑŒ
    // Ð´Ð¾Ñ�Ñ‚ÑƒÐ¿Ð½Ñ‹Ñ…
    // Ðº
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ð½Ð¸ÑŽ
    // ÑƒÐ¼ÐµÐ½Ð¸Ð¹.
    public static final int HIDES_OR_SHOWS_THE_MENU_WINDOW_THE_WINDOW_SHOWS_BY_DEFAULT = 2569; // ÐŸÑ€Ñ�Ñ‡ÐµÑ‚
    // Ð¸
    // Ð¿Ð¾ÐºÐ°Ð·Ñ‹Ð²Ð°ÐµÑ‚
    // Ð¾ÐºÐ½Ð¾
    // Ð¼ÐµÐ½ÑŽ.
    // ÐŸÐ¾
    // ÑƒÐ¼Ð¾Ð»Ñ‡Ð°Ð½Ð¸ÑŽ
    // Ð¿Ð¾ÐºÐ°Ð·Ð°Ð½Ð¾.
    public static final int OPENS_AND_CLOSES_THE_MINI_MAP_SHOWING_DETAILED_INFORMATION_ABOUT_THE_GAME_WORLD = 2570; // ÐžÑ‚ÐºÑ€Ñ‹Ð²Ð°ÐµÑ‚
    // Ð¸
    // Ð·Ð°ÐºÑ€Ñ‹Ð²Ð°ÐµÑ‚
    // Ð¼Ð¸Ð½Ð¸ÐºÐ°Ñ€Ñ‚Ñƒ,
    // Ð¿Ð¾ÐºÐ°Ð·Ñ‹Ð²Ð°ÑŽÑ‰ÑƒÑŽ
    // Ð´ÐµÑ‚Ð°Ð»Ð¸Ð·Ð¸Ñ€Ð¾Ð²Ð°Ð½Ð½ÑƒÑŽ
    // Ð¸Ð½Ñ„Ð¾Ñ€Ð¼Ð°Ñ†Ð¸ÑŽ
    // Ð¾Ð±
    // Ð¸Ð³Ñ€Ð¾Ð²Ð¾Ð¼
    // Ð¼Ð¸Ñ€Ðµ.
    public static final int OPENS_AND_CLOSES_THE_OPTION_WINDOW = 2571; // ÐžÑ‚ÐºÑ€Ñ‹Ð²Ð°ÐµÑ‚
    // Ð¸
    // Ð·Ð°ÐºÑ€Ñ‹Ð²Ð°ÐµÑ‚
    // Ð¾ÐºÐ½Ð¾
    // Ð¾Ð¿Ñ†Ð¸Ð¹.
    public static final int OPEN_AND_CLOSE_THE_PARTY_MATCHING_WINDOW_USEFUL_IN_ORGANIZING_A_PARTY_BY_HELPING_TO_EASILY_FIND = 2572; // ÐžÑ‚ÐºÑ€Ñ‹Ð²Ð°ÐµÑ‚
    // Ð¸
    // Ð·Ð°ÐºÑ€Ñ‹Ð²Ð°ÐµÑ‚
    // Ð¾ÐºÐ½Ð¾
    // Ð¿Ð¾Ð´Ð±Ð¾Ñ€Ð°
    // Ð³Ñ€ÑƒÐ¿Ð¿Ñ‹,
    // Ð¿Ð¾Ð¼Ð¾Ð³Ð°ÑŽÑ‰ÐµÐµ
    // Ð½Ð°Ð¹Ñ‚Ð¸
    // Ð´Ñ€ÑƒÐ³Ð¸Ñ…
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶ÐµÐ¹,
    // Ð¶ÐµÐ»Ð°ÑŽÑ‰Ð¸Ñ…
    // Ñ�Ñ„Ð¾Ñ€Ð¼Ð¸Ñ€Ð¾Ð²Ð°Ñ‚ÑŒ
    // Ð³Ñ€ÑƒÐ¿Ð¿Ñƒ.
    public static final int OPEN_AND_CLOSE_THE_QUEST_JOURNAL_DISPLAYING_THE_PROGRESS_OF_QUESTS = 2573; // ÐžÑ‚ÐºÑ€Ñ‹Ð²Ð°ÐµÑ‚
    // Ð¸
    // Ð·Ð°ÐºÑ€Ñ‹Ð²Ð°ÐµÑ‚
    // Ð¶ÑƒÑ€Ð½Ð°Ð»
    // ÐºÐ²ÐµÑ�Ñ‚Ð¾Ð²,
    // Ð¾Ñ‚Ð¾Ð±Ñ€Ð°Ð¶Ð°ÑŽÑ‰Ð¸Ð¹
    // Ð¿Ñ€Ð¾Ð³Ñ€ÐµÑ�Ñ�
    // Ð²
    // Ð¿Ñ€Ð¾Ñ…Ð¾Ð¶Ð´ÐµÐ½Ð¸Ð¸
    // ÐºÐ²ÐµÑ�Ñ‚Ð¾Ð².
    public static final int HIDES_OR_RE_OPENS_THE_RADAR_MAP_WHICH_ALWAYS_APPEARS_BY_DEFAULT = 2574; // ÐŸÑ€Ñ�Ñ‡ÐµÑ‚
    // Ð¸
    // Ð¿Ð¾ÐºÐ°Ð·Ñ‹Ð²Ð°ÐµÑ‚
    // Ñ€Ð°Ð´Ð°Ñ€.
    // ÐŸÐ¾
    // ÑƒÐ¼Ð¾Ð»Ñ‡Ð°Ð½Ð¸ÑŽ
    // Ð¿Ð¾ÐºÐ°Ð·Ñ‹Ð²Ð°ÐµÑ‚.
    public static final int HIDE_OR_SHOW_THE_STATUS_WINDOW_THE_WINDOW_WILL_SHOW_BY_DEFAULT = 2575; // ÐŸÑ€Ñ�Ñ‡ÐµÑ‚
    // Ð¸
    // Ð¿Ð¾ÐºÐ°Ð·Ñ‹Ð²Ð°ÐµÑ‚
    // Ð¾ÐºÐ½Ð¾
    // Ñ�Ñ‚Ð°Ñ‚ÑƒÑ�Ð°.
    // ÐŸÐ¾
    // ÑƒÐ¼Ð¾Ð»Ñ‡Ð°Ð½Ð¸ÑŽ
    // Ð¿Ð¾ÐºÐ°Ð·Ñ‹Ð²Ð°ÐµÑ‚.
    public static final int OPENS_AND_CLOSES_THE_SYSTEM_MENU_WINDOW_ENABLES_DETAILED_MENU_SELECTION = 2576; // ÐžÑ‚ÐºÑ€Ñ‹Ð²Ð°ÐµÑ‚
    // Ð¸
    // Ð·Ð°ÐºÑ€Ñ‹Ð²Ð°ÐµÑ‚
    // Ð¾ÐºÐ½Ð¾
    // Ñ�Ð¸Ñ�Ñ‚ÐµÐ¼Ð½Ñ‹Ñ…
    // Ð¾Ð¿Ñ†Ð¸Ð¹,
    // Ñ€Ð°Ð·Ñ€ÐµÑˆÐ°ÑŽÑ‰ÐµÐµ
    // Ð´ÐµÑ‚Ð°Ð»ÑŒÐ½Ñ‹Ðµ
    // Ð½Ð°Ñ�Ñ‚Ñ€Ð¾Ð¹ÐºÐ¸
    // Ð¼ÐµÐ½ÑŽ.
    public static final int DO_NOT_SHOW_DROP_ITEMS_DROPPED_IN_THE_WORLD_GAME_PERFORMANCE_SPEED_CAN_BE_ENHANCED_BY_USING_THIS = 2577; // Ð�Ðµ
    // Ð¿Ð¾ÐºÐ°Ð·Ñ‹Ð²Ð°Ñ‚ÑŒ
    // Ð²Ñ‹Ð¿Ð°Ð²ÑˆÐ¸Ðµ
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ñ‹.
    // Ð­Ñ‚Ð°
    // Ð¾Ð¿Ñ†Ð¸Ñ�
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // ÑƒÐ»ÑƒÑ‡ÑˆÐ¸Ñ‚ÑŒ
    // Ñ�ÐºÐ¾Ñ€Ð¾Ñ�Ñ‚ÑŒ
    // Ð¸Ð³Ñ€Ñ‹.
    public static final int A_KEY_TO_AUTOMATICALLY_SEND_WHISPERS_TO_A_TARGETED_CHARACTER = 2578; // Ð�Ð²Ñ‚Ð¾Ð¼Ð°Ñ‚Ð¸Ñ‡ÐµÑ�ÐºÐ¸
    // Ð¿Ð¾Ñ�Ñ‹Ð»Ð°ÐµÑ‚
    // Ð¿Ñ€Ð¸Ð²Ð°Ñ‚Ð½Ð¾Ðµ
    // Ñ�Ð¾Ð¾Ð±Ñ‰ÐµÐ½Ð¸Ðµ
    // Ð²Ñ‹Ð±Ñ€Ð°Ð½Ð½Ð¾Ð¼Ñƒ
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ñƒ.
    public static final int TURNS_OFF_ALL_GAME_SOUNDS = 2579; // Ð’Ñ‹ÐºÐ»ÑŽÑ‡Ð°ÐµÑ‚ Ð²Ñ�Ðµ
    // Ð¸Ð³Ñ€Ð¾Ð²Ñ‹Ðµ Ð·Ð²ÑƒÐºÐ¸.
    public static final int EXPANDS_EACH_SHORTCUT_WINDOW = 2580; // Ð£Ð²ÐµÐ»Ð¸Ñ‡Ð¸Ð²Ð°ÐµÑ‚
    // Ð¾ÐºÐ½Ð°
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð².
    public static final int INITIALIZE_USER_INTERFACE_LOCATION_TO_A_DEFAULT_LOCATION = 2581; // Ð£Ñ�Ñ‚Ð°Ð½Ð°Ð²Ð»Ð¸Ð²Ð°ÐµÑ‚
    // Ð¿Ð¾Ð·Ð¸Ñ†Ð¸Ð¸
    // Ð¸Ð½Ñ‚ÐµÑ€Ñ„ÐµÐ¹Ñ�Ð°
    // Ð¿Ð¾
    // ÑƒÐ¼Ð¾Ð»Ñ‡Ð°Ð½Ð¸ÑŽ.
    public static final int SPIN_MY_CHARACTER_OR_MOUNTABLE_TO_THE_LEFT = 2582; // ÐŸÐ¾Ð·Ð²Ð¾Ð»Ñ�ÐµÑ‚
    // Ð¿Ð¾Ð²ÐµÑ€Ð½ÑƒÑ‚ÑŒÑ�Ñ�
    // Ð½Ð°Ð»ÐµÐ²Ð¾.
    public static final int SPIN_MY_CHARACTER_OR_MOUNTABLE_TO_THE_RIGHT = 2583; // ÐŸÐ¾Ð·Ð²Ð¾Ð»Ñ�ÐµÑ‚
    // Ð¿Ð¾Ð²ÐµÑ€Ð½ÑƒÑ‚ÑŒÑ�Ñ�
    // Ð½Ð°Ð¿Ñ€Ð°Ð²Ð¾.
    public static final int SPIN_MY_CHARACTER_OR_MOUNTABLE_FORWARD = 2584; // ÐŸÐ¾Ð·Ð²Ð¾Ð»Ñ�ÐµÑ‚
    // Ð´Ð²Ð¸Ð³Ð°Ñ‚ÑŒÑ�Ñ�
    // Ð²Ð¿ÐµÑ€ÐµÐ´.
    public static final int SPIN_MY_CHARACTER_OR_MOUNTABLE_TO_THE_REAR = 2585; // ÐŸÐ¾Ð·Ð²Ð¾Ð»Ñ�ÐµÑ‚
    // Ð´Ð²Ð¸Ð³Ð°Ñ‚ÑŒÑ�Ñ�
    // Ð½Ð°Ð·Ð°Ð´.
    public static final int CONTINUE_MOVING_IN_THE_PRESENT_DIRECTION = 2586; // ÐŸÐ¾Ð·Ð²Ð¾Ð»Ñ�ÐµÑ‚
    // Ð´Ð²Ð¸Ð³Ð°Ñ‚ÑŒÑ�Ñ�
    // Ð²Ð¿ÐµÑ€ÐµÐ´
    // Ð°Ð²Ñ‚Ð¾Ð¼Ð°Ñ‚Ð¸Ñ‡ÐµÑ�ÐºÐ¸.
    public static final int REDUCE_THE_VIEWING_POINT_OF_MY_CHARACTER_OR_MOUNTABLE = 2587; // Ð£Ð¼ÐµÐ½ÑŒÑˆÐ°ÐµÑ‚
    // Ð²Ð¸Ð´
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°.
    public static final int ENLARGE_THE_VIEWING_POINT_OF_MY_CHARACTER_OR_MOUNTABLE = 2588; // Ð£Ð²ÐµÐ»Ð¸Ñ‡Ð¸Ð²Ð°ÐµÑ‚
    // Ð²Ð¸Ð´
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°.
    public static final int QUICKLY_SPIN_IN_ALL_DIRECTIONS_THE_VIEWING_POINT_OF_MY_CHARACTER_OR_MOUNTABLE = 2589; // ÐœÐ³Ð½Ð¾Ð²ÐµÐ½Ð½Ð¾
    // Ñ€Ð°Ð·Ð²Ð¾Ñ€Ð°Ñ‡Ð¸Ð²Ð°ÐµÑ‚
    // Ð²Ð¸Ð´
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°
    // Ð½Ð°
    // 180%.
    public static final int OPENS_THE_GM_MANAGER_WINDOW_ = 2590; // ÐžÑ‚ÐºÑ€Ñ‹Ð²Ð°ÐµÑ‚
    // Ð¾ÐºÐ½Ð¾
    // ÑƒÐ¿Ñ€Ð°Ð²Ð»ÐµÐ½Ð¸Ñ�
    // GM.
    public static final int OPENS_THE_GM_PETITION_WINDOW_ = 2591; // ÐžÑ‚ÐºÑ€Ñ‹Ð²Ð°ÐµÑ‚
    // Ð¾ÐºÐ½Ð¾
    // Ð¿ÐµÑ‚Ð¸Ñ†Ð¸Ð¹ GM.
    public static final int QUICKLY_SWITCH_THE_CONTENT_OF_THE_EXPANDED_SHORTCUT_WINDOW = 2592; // Ð‘Ñ‹Ñ�Ñ‚Ñ€Ð¾
    // Ð¿Ñ€ÐµÐ¾Ð±Ñ€Ð°Ð·Ð¾Ð²Ñ‹Ð²Ð°ÐµÑ‚
    // Ñ�Ð¾Ð´ÐµÑ€Ð¶Ð¸Ð¼Ð¾Ðµ
    // Ñ€Ð°Ñ�ÐºÑ€Ñ‹Ñ‚Ð¾Ð³Ð¾
    // Ð¾ÐºÐ½Ð°
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð².
    // Ð­Ñ‚Ð°
    // Ñ„ÑƒÐ½ÐºÑ†Ð¸Ñ�
    // Ñ�ÐµÐ¹Ñ‡Ð°Ñ�
    // Ð½ÐµÐ´Ð¾Ñ�Ñ‚ÑƒÐ¿Ð½Ð°.
    public static final int ADVANCE_BY_A_SET_DISTANCE_THE_VIEWING_POINT_OF_MY_CHARACTER_OR_MOUNTABLE = 2593; // Ð¡Ð¼ÐµÑ‰Ð°ÐµÑ‚
    // Ð²Ð¸Ð´
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°
    // Ð²Ð¿ÐµÑ€ÐµÐ´
    // Ð½Ð°
    // Ð¾Ð¿Ñ€ÐµÐ´ÐµÐ»ÐµÐ½Ð½ÑƒÑŽ
    // Ð´Ð¸Ñ�Ñ‚Ð°Ð½Ñ†Ð¸ÑŽ.
    public static final int RETREAT_BY_A_SET_DISTANCE_THE_VIEWING_POINT_OF_MY_CHARACTER_OR_MOUNTABLE = 2594; // Ð¡Ð¼ÐµÑ‰Ð°ÐµÑ‚
    // Ð²Ð¸Ð´
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°
    // Ð½Ð°Ð·Ð°Ð´
    // Ð½Ð°
    // Ð¾Ð¿Ñ€ÐµÐ´ÐµÐ»ÐµÐ½Ð½ÑƒÑŽ
    // Ð´Ð¸Ñ�Ñ‚Ð°Ð½Ñ†Ð¸ÑŽ.
    public static final int RESET_THE_VIEWING_POINT_OF_MY_CHARACTER_OR_MOUNTABLE = 2595; // Ð’Ð¾Ñ�Ñ�Ñ‚Ð°Ð½Ð°Ð²Ð»Ð¸Ð²Ð°ÐµÑ‚
    // Ð²Ð¸Ð´
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°
    // Ð¿Ð¾
    // ÑƒÐ¼Ð¾Ð»Ñ‡Ð°Ð½Ð¸ÑŽ.
    public static final int NO_TRANSLATION_REQUIRED_2596 = 2596; // Ð�Ðµ
    // Ð¿ÐµÑ€ÐµÐ²Ð¾Ð´Ð¸Ñ‚ÑŒ
    public static final int NO_TRANSLATION_REQUIRED_2597 = 2597; // Ð�Ðµ
    // Ð¿ÐµÑ€ÐµÐ²Ð¾Ð´Ð¸Ñ‚ÑŒ
    public static final int NO_TRANSLATION_REQUIRED_2598 = 2598; // Ð�Ðµ
    // Ð¿ÐµÑ€ÐµÐ²Ð¾Ð´Ð¸Ñ‚ÑŒ
    public static final int NO_TRANSLATION_REQUIRED_2599 = 2599; // Ð�Ðµ
    // Ð¿ÐµÑ€ÐµÐ²Ð¾Ð´Ð¸Ñ‚ÑŒ
    public static final int THE_MATCH_IS_BEING_PREPARED_PLEASE_TRY_AGAIN_LATER = 2701; // Ð˜Ð´ÐµÑ‚
    // Ð¿Ð¾Ð´Ð³Ð¾Ñ‚Ð¾Ð²ÐºÐ°
    // Ðº
    // Ñ�Ð¾Ñ€ÐµÐ²Ð½Ð¾Ð²Ð°Ð½Ð¸Ñ�Ð¼.
    // ÐŸÐ¾Ð´Ð¾Ð¶Ð´Ð¸Ñ‚Ðµ
    // Ð½ÐµÐ¼Ð½Ð¾Ð³Ð¾.
    public static final int YOU_WERE_EXCLUDED_FROM_THE_MATCH_BECAUSE_THE_REGISTRATION_COUNT_WAS_NOT_CORRECT = 2702; // ÐšÐ¾Ð»Ð¸Ñ‡ÐµÑ�Ñ‚Ð²Ð¾
    // Ð¸Ð³Ñ€Ð¾ÐºÐ¾Ð²
    // Ð½Ðµ
    // Ñ�Ð¾Ð¾Ñ‚Ð²ÐµÑ‚Ñ�Ñ‚Ð²Ð¾Ð²Ð°Ð»Ð¾
    // Ñ‚Ñ€ÐµÐ±Ð¾Ð²Ð°Ð½Ð¸Ñ�Ð¼,
    // Ð¿Ð¾Ñ�Ñ‚Ð¾Ð¼Ñƒ
    // Ð’Ñ‹
    // Ð±Ñ‹Ð»Ð¸
    // Ð¸Ñ�ÐºÐ»ÑŽÑ‡ÐµÐ½Ñ‹
    // Ð¸Ð·
    // ÐºÐ¾Ð¼Ð°Ð½Ð´Ñ‹.
    public static final int THE_TEAM_WAS_ADJUSTED_BECAUSE_THE_POPULATION_RATIO_WAS_NOT_CORRECT = 2703; // Ð”Ð»Ñ�
    // Ð¿Ð¾Ð´Ð´ÐµÑ€Ð¶Ð°Ð½Ð¸Ñ�
    // Ð±Ð°Ð»Ð°Ð½Ñ�Ð°
    // Ð²
    // Ð¸Ð³Ñ€Ðµ
    // Ñ�Ð¾Ñ�Ñ‚Ð°Ð²
    // ÐºÐ¾Ð¼Ð°Ð½Ð´Ñ‹
    // Ð±Ñ‹Ð»
    // Ð¸Ð·Ð¼ÐµÐ½ÐµÐ½.
    public static final int YOU_CANNOT_REGISTER_BECAUSE_CAPACITY_HAS_BEEN_EXCEEDED = 2704; // ÐšÐ¾Ð»Ð¸Ñ‡ÐµÑ�Ñ‚Ð²Ð¾
    // Ð¸Ð³Ñ€Ð¾ÐºÐ¾Ð²
    // Ð¿Ñ€ÐµÐ²Ñ‹Ñ�Ð¸Ð»Ð¾
    // Ð»Ð¸Ð¼Ð¸Ñ‚,
    // Ð¿Ð¾Ñ�Ñ‚Ð¾Ð¼Ñƒ
    // Ð’Ñ‹
    // Ð½Ðµ
    // Ñ�Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¿Ñ€Ð¸Ð½Ñ�Ñ‚ÑŒ
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð¸Ðµ
    // Ð²
    // Ñ�Ð¾Ñ€ÐµÐ²Ð½Ð¾Ð²Ð°Ð½Ð¸Ð¸.
    public static final int THE_MATCH_WAITING_TIME_WAS_EXTENDED_BY_1_MINUTE = 2705; // Ð’Ñ€ÐµÐ¼Ñ�
    // Ð¾Ð¶Ð¸Ð´Ð°Ð½Ð¸Ñ�
    // Ñ�Ð¾Ñ€ÐµÐ²Ð½Ð¾Ð²Ð°Ð½Ð¸Ð¹
    // Ð¿Ñ€Ð¾Ð´Ð»ÐµÐ½Ð¾
    // Ð½Ð°
    // 1Ð¼Ð¸Ð½.
    public static final int YOU_CANNOT_ENTER_BECAUSE_YOU_DO_NOT_MEET_THE_REQUIREMENTS = 2706; // Ð’Ñ‹
    // Ð½Ðµ
    // Ñ�Ð¾Ð¾Ñ‚Ð²ÐµÑ‚Ñ�Ñ‚Ð²ÑƒÐµÑ‚Ðµ
    // Ñ‚Ñ€ÐµÐ±Ð¾Ð²Ð°Ð½Ð¸Ñ�Ð¼
    // Ñ�Ð¾Ñ€ÐµÐ²Ð½Ð¾Ð²Ð°Ð½Ð¸Ð¹,
    // Ð¿Ð¾Ñ�Ñ‚Ð¾Ð¼Ñƒ
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð²Ð¾Ð¹Ñ‚Ð¸.
    public static final int YOU_CANNOT_MAKE_ANOTHER_REQUEST_FOR_10_SECONDS_AFTER_CANCELLING_A_MATCH_REGISTRATION = 2707; // Ð’
    // Ñ‚ÐµÑ‡ÐµÐ½Ð¸Ðµ
    // 10-Ñ‚Ð¸
    // Ñ�ÐµÐº.
    // Ð¿Ð¾Ñ�Ð»Ðµ
    // Ð¾Ñ‚Ð¼ÐµÐ½Ñ‹
    // Ñ€ÐµÐ³Ð¸Ñ�Ñ‚Ñ€Ð°Ñ†Ð¸Ð¸
    // Ð²
    // Ñ�Ð¾Ñ€ÐµÐ²Ð½Ð¾Ð²Ð°Ð½Ð¸Ñ�Ñ…
    // Ð’Ñ‹
    // Ð½Ðµ
    // Ñ�Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¿Ð¾Ð²Ñ‚Ð¾Ñ€Ð¸Ñ‚ÑŒ
    // Ð¿Ð¾Ð¿Ñ‹Ñ‚ÐºÑƒ.
    public static final int YOU_CANNOT_REGISTER_WHILE_POSSESSING_A_CURSED_WEAPON = 2708; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð·Ð°Ñ€ÐµÐ³Ð¸Ñ�Ñ‚Ñ€Ð¸Ñ€Ð¾Ð²Ð°Ñ‚ÑŒÑ�Ñ�,
    // Ð¿Ð¾ÐºÐ°
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·ÑƒÐµÑ‚Ðµ
    // Ð¿Ñ€Ð¾ÐºÐ»Ñ�Ñ‚Ð¾Ðµ
    // Ð¾Ñ€ÑƒÐ¶Ð¸Ðµ.
    public static final int APPLICANTS_FOR_THE_OLYMPIAD_UNDERGROUND_COLISEUM_OR_KRATEI_S_CUBE_MATCHES_CANNOT_REGISTER = 2709; // ÐŸÐ¾Ð´Ð°Ð²ÑˆÐ¸Ð¹
    // Ð·Ð°Ñ�Ð²ÐºÑƒ
    // Ð½Ð°
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð¸Ðµ
    // Ð²
    // ÐžÐ»Ð¸Ð¼Ð¿Ð¸Ð°Ð´Ðµ,
    // Ð‘Ð¸Ñ‚Ð²Ðµ
    // Ð·Ð°
    // ÐŸÐ¾Ð´Ð·ÐµÐ¼Ð½Ñ‹Ð¹
    // Ð—Ð°Ð»
    // Ð‘Ð¾ÐµÐ²
    // Ð¸Ð»Ð¸
    // ÐšÑƒÐ±
    // ÐšÑ€Ð°Ñ‚ÐµÐ¸
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð·Ð°Ñ€ÐµÐ³Ð¸Ñ�Ñ‚Ñ€Ð¸Ñ€Ð¾Ð²Ð°Ñ‚ÑŒÑ�Ñ�.
    public static final int CURRENT_LOCATION__S1_S2_S3_NEAR_THE_KEUCEREUS_CLAN_ASSOCIATION_LOCATION = 2710; // ÐšÐ¾Ð¾Ñ€Ð´Ð¸Ð½Ð°Ñ‚Ñ‹:
    // $s1,
    // $s2,
    // $s3
    // (ÐžÐºÑ€ÐµÑ�Ñ‚Ð½Ð¾Ñ�Ñ‚Ð¸
    // Ð‘Ð°Ð·Ñ‹
    // Ð�Ð»ÑŒÑ�Ð½Ñ�Ð°
    // ÐšÐµÑ†ÐµÑ€ÑƒÑ�Ð°)
    public static final int CURRENT_LOCATION__S1_S2_S3_INSIDE_THE_SEED_OF_INFINITY = 2711; // ÐšÐ¾Ð¾Ñ€Ð´Ð¸Ð½Ð°Ñ‚Ñ‹:
    // $s1,
    // $s2,
    // $s3
    // (Ð’Ð½ÑƒÑ‚Ñ€Ð¸
    // Ð¡ÐµÐ¼ÐµÐ½Ð¸
    // Ð‘ÐµÑ�Ñ�Ð¼ÐµÑ€Ñ‚Ð¸Ñ�)
    public static final int CURRENT_LOCATION__S1_S2_S3_OUTSIDE_THE_SEED_OF_INFINITY = 2712; // ÐšÐ¾Ð¾Ñ€Ð´Ð¸Ð½Ð°Ñ‚Ñ‹:
    // $s1,
    // $s2,
    // $s3
    // (Ð’Ð½ÑƒÑ‚Ñ€Ð¸
    // Ð¡ÐµÐ¼ÐµÐ½Ð¸
    // Ð Ð°Ð·Ñ€ÑƒÑˆÐµÐ½Ð¸Ñ�)
    public static final int ______________________________________________________ = 2713; // ------------------------------------------------------
    public static final int ______________________________________________________________________ = 2714; // ----------------------------------------------------------------------
    public static final int AIRSHIPS_CANNOT_BE_BOARDED_IN_THE_CURRENT_AREA = 2715; // Ð’
    // Ð´Ð°Ð½Ð½Ð¾Ð¹
    // Ð»Ð¾ÐºÐ°Ñ†Ð¸Ð¸
    // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð²Ð¾Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒÑ�Ñ�
    // Ð›ÐµÑ‚Ð°ÑŽÑ‰Ð¸Ð¼
    // ÐšÐ¾Ñ€Ð°Ð±Ð»ÐµÐ¼.
    public static final int CURRENT_LOCATION__S1_S2_S3_INSIDE_AERIAL_CLEFT = 2716; // ÐšÐ¾Ð¾Ñ€Ð´Ð¸Ð½Ð°Ñ‚Ñ‹:
    // $s1,
    // $s2,
    // $s3
    // (Ð’Ð½ÑƒÑ‚Ñ€Ð¸
    // Ð£Ñ‰ÐµÐ»ÑŒÑ�)
    public static final int THE_AIRSHIP_WILL_LAND_AT_THE_WHARF_SHORTLY = 2717; // Ð›ÐµÑ‚Ð°ÑŽÑ‰Ð¸Ð¹
    // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ
    // Ð¿Ñ€Ð¸Ð±Ñ‹Ð²Ð°ÐµÑ‚
    // Ð²
    // Ð’Ð¾Ð·Ð´ÑƒÑˆÐ½ÑƒÑŽ
    // Ð“Ð°Ð²Ð°Ð½ÑŒ.
    public static final int THE_SKILL_CANNOT_BE_USED_BECAUSE_THE_TARGET_S_LOCATION_IS_TOO_HIGH_OR_LOW = 2718; // Ð¦ÐµÐ»ÑŒ
    // Ð½Ð°Ñ…Ð¾Ð´Ð¸Ñ‚Ñ�Ñ�
    // Ñ�Ð»Ð¸ÑˆÐºÐ¾Ð¼
    // Ð²Ñ‹Ñ�Ð¾ÐºÐ¾
    // Ð¸Ð»Ð¸
    // Ð½Ð¸Ð·ÐºÐ¾,
    // Ð¿Ð¾Ñ�Ñ‚Ð¾Ð¼Ñƒ
    // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð²Ð¾Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒÑ�Ñ�
    // Ñ�Ñ‚Ð¸Ð¼
    // ÑƒÐ¼ÐµÐ½Ð¸ÐµÐ¼.
    public static final int ONLY_NON_COMPRESSED_256_COLOR_BMP_BITMAP_FILES_CAN_BE_REGISTERED = 2719; // Ð’Ñ‹
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒ
    // Ð½Ðµ
    // Ñ�Ð¶Ð°Ñ‚Ñ‹Ð¹
    // bmp
    // Ñ„Ð°Ð¹Ð»
    // Ñ�
    // Ñ†Ð²ÐµÑ‚Ð¾Ð²Ð¾Ð¹
    // Ð³Ð°Ð¼Ð¼Ð¾Ð¹
    // 256.
    public static final int INSTANT_ZONE_FROM_HERE__S1_S_ENTRY_HAS_BEEN_RESTRICTED_YOU_CAN_CHECK_THE_NEXT_ENTRY_POSSIBLE = 2720; // Ð’Ñ…Ð¾Ð´
    // Ð²Ð¾
    // Ð²Ñ€ÐµÐ¼ÐµÐ½Ð½ÑƒÑŽ
    // Ð·Ð¾Ð½Ñƒ
    // $s1
    // Ð·Ð°ÐºÑ€Ñ‹Ñ‚.
    // Ð’Ñ€ÐµÐ¼Ñ�
    // Ñ�Ð»ÐµÐ´ÑƒÑŽÑ‰ÐµÐ³Ð¾
    // Ð²Ñ…Ð¾Ð´Ð°
    // Ð¼Ð¾Ð¶Ð½Ð¾
    // Ð¿Ñ€Ð¾Ð²ÐµÑ€Ð¸Ñ‚ÑŒ
    // Ñ�
    // Ð¿Ð¾Ð¼Ð¾Ñ‰ÑŒÑŽ
    // ÐºÐ¾Ð¼Ð°Ð½Ð´Ñ‹
    // "/Ð’Ñ€ÐµÐ¼ÐµÐ½Ð½Ð°Ñ� Ð—Ð¾Ð½Ð°".
    public static final int BOARDING_OR_CANCELLATION_OF_BOARDING_ON_AIRSHIPS_IS_NOT_ALLOWED_IN_THE_CURRENT_AREA = 2721; // Ð’
    // Ð´Ð°Ð½Ð½Ð¾Ð¹
    // Ð»Ð¾ÐºÐ°Ñ†Ð¸Ð¸
    // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð²Ð·Ð¾Ð¹Ñ‚Ð¸
    // Ð½Ð°
    // Ð›ÐµÑ‚Ð°ÑŽÑ‰Ð¸Ð¹
    // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ/Ñ�Ð¾Ð¹Ñ‚Ð¸
    // Ñ�
    // Ð›ÐµÑ‚Ð°ÑŽÑ‰ÐµÐ³Ð¾
    // ÐšÐ¾Ñ€Ð°Ð±Ð»Ñ�.
    public static final int ANOTHER_AIRSHIP_HAS_ALREADY_BEEN_SUMMONED_AT_THE_WHARF_PLEASE_TRY_AGAIN_LATER = 2722; // Ð’
    // Ð’Ð¾Ð·Ð´ÑƒÑˆÐ½Ð¾Ð¹
    // Ð“Ð°Ð²Ð°Ð½Ð¸
    // Ð½Ð°Ñ…Ð¾Ð´Ð¸Ñ‚Ñ�Ñ�
    // Ð´Ñ€ÑƒÐ³Ð¾Ð¹
    // Ð›ÐµÑ‚Ð°ÑŽÑ‰Ð¸Ð¹
    // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ.
    // ÐŸÐ¾Ð²Ñ‚Ð¾Ñ€Ð¸Ñ‚Ðµ
    // Ð¿Ð¾Ð¿Ñ‹Ñ‚ÐºÑƒ
    // Ð¿Ð¾Ð·Ð¶Ðµ.
    public static final int THE_AIRSHIP_CANNOT_BE_SUMMONED_BECAUSE_YOU_DONT_HAVE_ENOUGH_S1 = 2723; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð²Ñ‹Ð·Ð²Ð°Ñ‚ÑŒ
    // Ð›ÐµÑ‚Ð°ÑŽÑ‰Ð¸Ð¹
    // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ,
    // Ñ‚Ð°Ðº
    // ÐºÐ°Ðº
    // Ð½Ðµ
    // Ñ…Ð²Ð°Ñ‚Ð°ÐµÑ‚
    // $s1.
    public static final int THE_AIRSHIP_CANNOT_BE_PURCHASED_BECAUSE_YOU_DONT_HAVE_ENOUGH_S1 = 2724; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // ÐºÑƒÐ¿Ð¸Ñ‚ÑŒ
    // Ð›ÐµÑ‚Ð°ÑŽÑ‰Ð¸Ð¹
    // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ,
    // Ñ‚Ð°Ðº
    // ÐºÐ°Ðº
    // Ð½Ðµ
    // Ñ…Ð²Ð°Ñ‚Ð°ÐµÑ‚
    // $s1.
    public static final int YOU_CANNOT_SUMMON_THE_AIRSHIP_BECAUSE_YOU_DO_NOT_MEET_THE_REQUIREMENTS = 2725; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð²Ñ‹Ð·Ð²Ð°Ñ‚ÑŒ
    // Ð›ÐµÑ‚Ð°ÑŽÑ‰Ð¸Ð¹
    // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ,
    // Ñ‚Ð°Ðº
    // ÐºÐ°Ðº
    // Ð½Ðµ
    // Ñ�Ð¾Ð¾Ñ‚Ð²ÐµÑ‚Ñ�Ñ‚Ð²ÑƒÐµÑ‚Ðµ
    // Ñ‚Ñ€ÐµÐ±Ð¾Ð²Ð°Ð½Ð¸Ñ�Ð¼.
    public static final int YOU_CANNOT_PURCHASE_THE_AIRSHIP_BECAUSE_YOU_DO_NOT_MEET_THE_REQUIREMENTS = 2726; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // ÐºÑƒÐ¿Ð¸Ñ‚ÑŒ
    // Ð›ÐµÑ‚Ð°ÑŽÑ‰Ð¸Ð¹
    // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ,
    // Ñ‚Ð°Ðº
    // ÐºÐ°Ðº
    // Ð½Ðµ
    // Ñ�Ð¾Ð¾Ñ‚Ð²ÐµÑ‚Ñ�Ñ‚Ð²ÑƒÐµÑ‚Ðµ
    // Ñ‚Ñ€ÐµÐ±Ð¾Ð²Ð°Ð½Ð¸Ñ�Ð¼.
    public static final int YOU_CANNOT_MOUNT_BECAUSE_YOU_DO_NOT_MEET_THE_REQUIREMENTS = 2727; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ñ�ÐµÑ�Ñ‚ÑŒ
    // Ð½Ð°
    // Ð›ÐµÑ‚Ð°ÑŽÑ‰Ð¸Ð¹
    // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ,
    // Ñ‚Ð°Ðº
    // ÐºÐ°Ðº
    // Ð½Ðµ
    // Ñ�Ð¾Ð¾Ñ‚Ð²ÐµÑ‚Ñ�Ñ‚Ð²ÑƒÐµÑ‚Ðµ
    // Ñ‚Ñ€ÐµÐ±Ð¾Ð²Ð°Ð½Ð¸Ñ�Ð¼.
    public static final int THIS_ACTION_IS_PROHIBITED_WHILE_MOUNTED = 2728; // Ð­Ñ‚Ð¾
    // Ð´ÐµÐ¹Ñ�Ñ‚Ð²Ð¸Ðµ
    // Ð½ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾
    // Ð²Ð¾
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ð¿Ð¾Ð»ÐµÑ‚Ð°
    // Ð½Ð°
    // ÐšÐ¾Ñ€Ð°Ð±Ð»Ðµ.
    public static final int YOU_CANNOT_CONTROL_THE_TARGET_WHILE_TRANSFORMED = 2729; // Ð’Ð¾
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ð¿ÐµÑ€ÐµÐ²Ð¾Ð¿Ð»Ð¾Ñ‰ÐµÐ½Ð¸Ñ�
    // ÑƒÐ¿Ñ€Ð°Ð²Ð»Ñ�Ñ‚ÑŒ
    // Ñ†ÐµÐ»ÑŒÑŽ
    // Ð½ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾.
    public static final int YOU_CANNOT_CONTROL_THE_TARGET_WHILE_YOU_ARE_PETRIFIED = 2730; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // ÑƒÐ¿Ñ€Ð°Ð²Ð»Ñ�Ñ‚ÑŒ
    // Ñ†ÐµÐ»ÑŒÑŽ
    // Ð²
    // Ð¾ÐºÐ°Ð¼ÐµÐ½ÐµÐ»Ð¾Ð¼
    // Ñ�Ð¾Ñ�Ñ‚Ð¾Ñ�Ð½Ð¸Ð¸.
    public static final int YOU_CANNOT_CONTROL_THE_TARGET_WHEN_YOU_ARE_DEAD = 2731; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // ÑƒÐ¿Ñ€Ð°Ð²Ð»Ñ�Ñ‚ÑŒ
    // Ñ†ÐµÐ»ÑŒÑŽ
    // Ð±ÑƒÐ´ÑƒÑ‡Ð¸
    // Ð¼ÐµÑ€Ñ‚Ð²Ñ‹Ð¼.
    public static final int YOU_CANNOT_CONTROL_THE_TARGET_WHILE_FISHING = 2732; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // ÑƒÐ¿Ñ€Ð°Ð²Ð»Ñ�Ñ‚ÑŒ
    // Ñ†ÐµÐ»ÑŒÑŽ
    // Ð²Ð¾
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ñ€Ñ‹Ð±Ð°Ð»ÐºÐ¸.
    public static final int YOU_CANNOT_CONTROL_THE_TARGET_WHILE_IN_A_BATTLE = 2733; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // ÑƒÐ¿Ñ€Ð°Ð²Ð»Ñ�Ñ‚ÑŒ
    // Ñ†ÐµÐ»ÑŒÑŽ
    // Ð²Ð¾
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ð±Ð¸Ñ‚Ð²Ñ‹.
    public static final int YOU_CANNOT_CONTROL_THE_TARGET_WHILE_IN_A_DUEL = 2734; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // ÑƒÐ¿Ñ€Ð°Ð²Ð»Ñ�Ñ‚ÑŒ
    // Ñ†ÐµÐ»ÑŒÑŽ
    // Ð²Ð¾
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ð´ÑƒÑ�Ð»Ð¸.
    public static final int YOU_CANNOT_CONTROL_THE_TARGET_WHILE_IN_A_SITTING_POSITION = 2735; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // ÑƒÐ¿Ñ€Ð°Ð²Ð»Ñ�Ñ‚ÑŒ
    // Ñ†ÐµÐ»ÑŒÑŽ
    // Ñ�Ð¸Ð´Ñ�.
    public static final int YOU_CANNOT_CONTROL_THE_TARGET_WHILE_USING_A_SKILL = 2736; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // ÑƒÐ¿Ñ€Ð°Ð²Ð»Ñ�Ñ‚ÑŒ
    // Ñ†ÐµÐ»ÑŒÑŽ
    // Ð²Ð¾
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ð¿Ñ€Ð¾Ñ‡Ñ‚ÐµÐ½Ð¸Ñ�
    // Ð·Ð°ÐºÐ»Ð¸Ð½Ð°Ð½Ð¸Ñ�.
    public static final int YOU_CANNOT_CONTROL_THE_TARGET_WHILE_A_CURSED_WEAPON_IS_EQUIPPED = 2737; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // ÑƒÐ¿Ñ€Ð°Ð²Ð»Ñ�Ñ‚ÑŒ
    // Ñ†ÐµÐ»ÑŒÑŽ,
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·ÑƒÑ�
    // Ð¿Ñ€Ð¾ÐºÐ»Ñ�Ñ‚Ð¾Ðµ
    // Ð¾Ñ€ÑƒÐ¶Ð¸Ðµ.
    public static final int YOU_CANNOT_CONTROL_THE_TARGET_WHILE_HOLDING_A_FLAG = 2738; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // ÑƒÐ¿Ñ€Ð°Ð²Ð»Ñ�Ñ‚ÑŒ
    // Ñ†ÐµÐ»ÑŒÑŽ,
    // Ð¿Ð¾Ð´Ð½Ñ�Ð²
    // Ñ„Ð»Ð°Ð³.
    public static final int YOU_CANNOT_CONTROL_BECAUSE_YOU_DO_NOT_MEET_THE_REQUIREMENTS = 2739; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // ÑƒÐ¿Ñ€Ð°Ð²Ð»Ñ�Ñ‚ÑŒ
    // Ñ†ÐµÐ»ÑŒÑŽ,
    // Ñ‚Ð°Ðº
    // ÐºÐ°Ðº
    // Ð½Ðµ
    // Ð²Ñ‹Ð¿Ð¾Ð»Ð½Ð¸Ð»Ð¸
    // Ð²Ñ�Ðµ
    // ÑƒÑ�Ð»Ð¾Ð²Ð¸Ñ�.
    public static final int THIS_ACTION_IS_PROHIBITED_WHILE_CONTROLLING = 2740; // Ð­Ñ‚Ð¾
    // Ð´ÐµÐ¹Ñ�Ñ‚Ð²Ð¸Ðµ
    // Ð½ÐµÐ´Ð¾Ñ�Ñ‚ÑƒÐ¿Ð½Ð¾
    // Ð²Ð¾
    // Ð²Ñ€ÐµÐ¼Ñ�
    // ÑƒÐ¿Ñ€Ð°Ð²Ð»ÐµÐ½Ð¸Ñ�
    // Ñ†ÐµÐ»ÑŒÑŽ.
    public static final int YOU_CAN_CONTROL_THE_AIRSHIP_BY_TARGETING_THE_AIRSHIP_S_CONTROL_KEY_AND_PRESSING_THE__CONTROL_ = 2741; // Ð£Ð¿Ñ€Ð°Ð²Ð»ÐµÐ½Ð¸Ðµ
    // Ð›ÐµÑ‚Ð°ÑŽÑ‰Ð¸Ð¼
    // ÐšÐ¾Ñ€Ð°Ð±Ð»ÐµÐ¼
    // Ð²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾,
    // ÐµÑ�Ð»Ð¸
    // Ð²Ñ‹Ð±Ñ€Ð°Ñ‚ÑŒ
    // Ð²
    // ÐºÐ°Ñ‡ÐµÑ�Ñ‚Ð²Ðµ
    // Ñ†ÐµÐ»Ð¸
    // ÐŸÐ°Ð½ÐµÐ»ÑŒ
    // Ð£Ð¿Ñ€Ð°Ð²Ð»ÐµÐ½Ð¸Ñ�
    // Ð¸
    // Ð²Ñ‹Ð±Ñ€Ð°Ñ‚ÑŒ
    // Ð´ÐµÐ¹Ñ�Ñ‚Ð²Ð¸Ðµ
    // "Ð£Ð¿Ñ€Ð°Ð²Ð»Ñ�Ñ‚ÑŒ"
    public static final int ANY_CHARACTER_RIDING_THE_AIRSHIP_CAN_CONTROL_IT = 2742; // Ð›ÑŽÐ±Ð¾Ð¹
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶,
    // Ð½Ð°Ñ…Ð¾Ð´Ñ�Ñ‰Ð¸Ð¹Ñ�Ñ�
    // Ð½Ð°
    // Ð›ÐµÑ‚Ð°ÑŽÑ‰ÐµÐ¼
    // ÐšÐ¾Ñ€Ð°Ð±Ð»Ðµ,
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð¿Ñ€Ð¸Ð½Ñ�Ñ‚ÑŒ
    // ÑƒÐ¿Ñ€Ð°Ð²Ð»ÐµÐ½Ð¸Ðµ
    // Ð½Ð°
    // Ñ�ÐµÐ±Ñ�.
    public static final int IF_YOU_RESTART_WHILE_ON_AN_AIRSHIP_YOU_WILL_RETURN_TO_THE_DEPARTURE_LOCATION = 2743; // Ð’Ñ‹Ð¿Ð¾Ð»Ð½Ð¸Ð²
    // Ð¿ÐµÑ€ÐµÐ·Ð°Ð¿ÑƒÑ�Ðº
    // Ð¸Ð³Ñ€Ñ‹,
    // Ð½Ð°Ñ…Ð¾Ð´Ñ�Ñ�ÑŒ
    // Ð²
    // Ð›ÐµÑ‚Ð°ÑŽÑ‰ÐµÐ¼
    // ÐšÐ¾Ñ€Ð°Ð±Ð»Ðµ,
    // Ð’Ñ‹
    // Ð¾ÐºÐ°Ð¶ÐµÑ‚ÐµÑ�ÑŒ
    // Ð²
    // Ñ‚Ð¾Ñ‡ÐºÐµ
    // Ð¾Ñ‚Ð¿Ñ€Ð°Ð²ÐºÐ¸
    // ÐšÐ¾Ñ€Ð°Ð±Ð»Ñ�.
    public static final int IF_YOU_PRESS_THE__CONTROL_CANCEL__ACTION_BUTTON_YOU_CAN_EXIT_THE_CONTROL_STATE_AT_ANY_TIME = 2744; // Ð¡
    // Ð¿Ð¾Ð¼Ð¾Ñ‰ÑŒÑŽ
    // Ð´ÐµÐ¹Ñ�Ñ‚Ð²Ð¸Ñ�
    // "Ð’Ñ‹Ð¹Ñ‚Ð¸"
    // Ð’Ñ‹
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¾Ñ‚Ð¼ÐµÐ½Ð¸Ñ‚ÑŒ
    // ÑƒÐ¿Ñ€Ð°Ð²Ð»ÐµÐ½Ð¸Ðµ
    // ÐºÐ¾Ñ€Ð°Ð±Ð»ÐµÐ¼.
    public static final int THE__MOUNT_CANCEL__ACTION_BUTTON_ALLOWS_YOU_TO_DISMOUNT_BEFORE_THE_AIRSHIP_DEPARTS = 2745; // Ð¡
    // Ð¿Ð¾Ð¼Ð¾Ñ‰ÑŒÑŽ
    // Ñ„ÑƒÐ½ÐºÑ†Ð¸Ð¸
    // "Ð¡Ð¾Ð¹Ñ‚Ð¸"
    // Ð’Ñ‹
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ñ�Ð¾Ð¹Ñ‚Ð¸
    // Ñ�
    // Ð›ÐµÑ‚Ð°ÑŽÑ‰ÐµÐ³Ð¾
    // ÐšÐ¾Ñ€Ð°Ð±Ð»Ñ�.
    public static final int USE_THE__DEPART__ACTION_TO_MAKE_THE_AIRSHIP_DEPART = 2746; // Ð¡
    // Ð¿Ð¾Ð¼Ð¾Ñ‰ÑŒÑŽ
    // Ð´ÐµÐ¹Ñ�Ñ‚Ð²Ð¸Ñ�
    // "Ð’Ð·Ð»ÐµÑ‚"
    // Ð’Ñ‹
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð½Ð°Ñ‡Ð°Ñ‚ÑŒ
    // Ð¿Ð¾Ð»ÐµÑ‚
    // Ð½Ð°
    // Ð›ÐµÑ‚Ð°ÑŽÑ‰ÐµÐ¼
    // ÐšÐ¾Ñ€Ð°Ð±Ð»Ðµ.
    public static final int AIRSHIP_TELEPORT_IS_POSSIBLE_THROUGH_THE__DEPART__ACTION_AND_IN_THAT_CASE_FUEL_EP_IS_CONSUMED = 2747; // Ð¡
    // Ð¿Ð¾Ð¼Ð¾Ñ‰ÑŒÑŽ
    // Ð´ÐµÐ¹Ñ�Ñ‚Ð²Ð¸Ñ�
    // "Ð’Ð·Ð»ÐµÑ‚"
    // Ð’Ñ‹
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¿ÐµÑ€ÐµÐ¼ÐµÑ�Ñ‚Ð¸Ñ‚ÑŒÑ�Ñ�,
    // Ð±ÑƒÐ´ÑƒÑ‡Ð¸
    // Ð²
    // Ð›ÐµÑ‚Ð°ÑŽÑ‰ÐµÐ¼
    // ÐšÐ¾Ñ€Ð°Ð±Ð»Ðµ,
    // Ð½Ð¾
    // Ð¿Ñ€Ð¸
    // Ñ�Ñ‚Ð¾Ð¼
    // Ð·Ð°Ñ‚Ñ€Ð°Ñ‡Ð¸Ð²Ð°ÐµÑ‚Ñ�Ñ�
    // Ñ‚Ð¾Ð¿Ð»Ð¸Ð²Ð¾(EP).
    public static final int YOU_HAVE_BEEN_REPORTED_AS_AN_ILLEGAL_PROGRAM_USER_AND_CANNOT_REPORT_OTHER_USERS = 2748; // ÐŸÐ¾Ð»ÑƒÑ‡ÐµÐ½Ð¾
    // Ñ�Ð¾Ð¾Ð±Ñ‰ÐµÐ½Ð¸Ðµ
    // Ð¾
    // Ñ‚Ð¾Ð¼,
    // Ñ‡Ñ‚Ð¾
    // Ð’Ñ‹
    // Ð¿Ð¾Ð»ÑŒÐ·ÑƒÐµÑ‚ÐµÑ�ÑŒ
    // Ð½ÐµÐ»ÐµÐ³Ð°Ð»ÑŒÐ½Ð¾Ð¹
    // Ð¿Ñ€Ð¾Ð³Ñ€Ð°Ð¼Ð¼Ð¾Ð¹,
    // Ð¿Ð¾Ñ�Ñ‚Ð¾Ð¼Ñƒ
    // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ñ�Ð¾Ð¾Ð±Ñ‰Ð¸Ñ‚ÑŒ
    // Ð¾
    // Ð´Ñ€ÑƒÐ³Ð¾Ð¼
    // Ð¸Ð³Ñ€Ð¾ÐºÐµ.
    public static final int YOU_HAVE_REACHED_YOUR_CRYSTALLIZATION_LIMIT_AND_CANNOT_CRYSTALLIZE_ANY_MORE = 2749; // Ð’Ñ‹
    // Ð¿Ñ€ÐµÐ²Ñ‹Ñ�Ð¸Ð»Ð¸
    // Ñ‡Ð¸Ñ�Ð»Ð¾
    // Ð²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ñ‹Ñ…
    // ÐºÑ€Ð¸Ñ�Ñ‚Ð°Ð»Ð»Ð¸Ð·Ð°Ñ†Ð¸Ð¹.
    public static final int THE_S1_WARD_HAS_BEEN_DESTROYED_C2_NOW_HAS_THE_TERRITORY_WARD = 2750; // Ð—Ð½Ð°Ð¼Ñ�
    // $s1
    // Ð¿Ð¾Ñ‚ÐµÑ€Ñ�Ð½Ð¾!
    // $c2
    // Ð·Ð°Ñ…Ð²Ð°Ñ‚Ð¸Ð»
    // Ð—Ð½Ð°Ð¼Ñ�.
    public static final int THE_CHARACTER_THAT_ACQUIRED_S1_WARD_HAS_BEEN_KILLED = 2751; // ÐŸÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶,
    // Ð·Ð°Ñ…Ð²Ð°Ñ‚Ð¸Ð²ÑˆÐ¸Ð¹
    // Ð—Ð½Ð°Ð¼Ñ�,
    // $s1
    // Ð¿Ð¾Ð³Ð¸Ð±.
    public static final int THE_WAR_FOR_S1_HAS_BEEN_DECLARED = 2752; // ÐžÐ±ÑŠÑ�Ð²Ð»ÐµÐ½Ð°
    // Ð²Ð¾Ð¹Ð½Ð° Ð·Ð°
    // Ð—ÐµÐ¼Ð»Ð¸
    // $s1.
    public static final int A_POWERFUL_ATTACK_IS_PROHIBITED_WHEN_ALLIED_TROOPS_ARE_THE_TARGET = 2753; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð°Ñ‚Ð°ÐºÐ¾Ð²Ð°Ñ‚ÑŒ
    // Ñ�Ð¾Ñ€Ð°Ñ‚Ð½Ð¸ÐºÐ°.
    public static final int PVP_MATCHES_SUCH_AS_OLYMPIAD_UNDERGROUND_COLISEUM_AERIAL_CLEFT_KRATEI_S_CUBE_AND_HANDY_S_BLOCK = 2754; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð²Ð¾Ð²Ð°Ñ‚ÑŒ
    // Ð¾Ð´Ð½Ð¾Ð²Ñ€ÐµÐ¼ÐµÐ½Ð½Ð¾
    // Ð²
    // Ð½ÐµÑ�ÐºÐ¾Ð»ÑŒÐºÐ¸Ñ…
    // PVP
    // Ñ�Ð¾Ñ€ÐµÐ²Ð½Ð¾Ð²Ð°Ð½Ð¸Ñ�Ñ…
    // (ÐžÐ»Ð¸Ð¼Ð¿Ð¸Ð°Ð´Ð°,
    // ÐŸÐ¾Ð´Ð·ÐµÐ¼Ð½Ñ‹Ð¹
    // Ð—Ð°Ð»
    // Ð‘Ð¾ÐµÐ²,
    // Ð£Ñ‰ÐµÐ»ÑŒÐµ,
    // ÐšÑƒÐ±
    // ÐšÑ€Ð°Ñ‚ÐµÐ¸
    // Ð¸
    // Ð�Ñ€ÐµÐ½Ð°).
    public static final int C1_HAS_BEEN_DESIGNATED_AS_CAT = 2755; // $c1 Ñ�Ñ‚Ð°Ð»
    // CAT.
    public static final int ANOTHER_PLAYER_IS_PROBABLY_CONTROLLING_THE_TARGET = 2756; // Ð¦ÐµÐ»ÑŒ
    // Ð½Ð°Ñ…Ð¾Ð´Ð¸Ñ‚Ñ�Ñ�
    // Ð¿Ð¾Ð´
    // ÐºÐ¾Ð½Ñ‚Ñ€Ð¾Ð»ÐµÐ¼
    // Ð´Ñ€ÑƒÐ³Ð¾Ð³Ð¾
    // Ð¸Ð³Ñ€Ð¾ÐºÐ°.
    public static final int THE_TARGET_IS_MOVING_SO_YOU_HAVE_FAILED_TO_MOUNT = 2757; // Ð¦ÐµÐ»ÑŒ
    // Ð¿ÐµÑ€ÐµÐ¼ÐµÑ‰Ð°ÐµÑ‚Ñ�Ñ�,
    // Ð¿Ð¾Ñ�Ñ‚Ð¾Ð¼Ñƒ
    // Ð¾Ñ�ÐµÐ´Ð»Ð°Ñ‚ÑŒ
    // ÐµÐµ
    // Ð½Ðµ
    // Ð¿Ð¾Ð»ÑƒÑ‡Ð¸Ð»Ð¾Ñ�ÑŒ.
    public static final int YOU_CANNOT_CONTROL_THE_TARGET_WHILE_A_PET_OR_SERVITOR_IS_SUMMONED = 2758; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // ÑƒÐ¿Ñ€Ð°Ð²Ð»Ñ�Ñ‚ÑŒ
    // Ñ†ÐµÐ»ÑŒÑŽ,
    // ÐµÑ�Ð»Ð¸
    // Ð¿Ñ€Ð¸Ð·Ð²Ð°Ð»Ð¸
    // ÐŸÐ¸Ñ‚Ð¾Ð¼Ñ†Ð°
    // Ð¸Ð»Ð¸
    // Ð¡Ð»ÑƒÐ³Ñƒ.
    public static final int WHEN_ACTIONS_ARE_PROHIBITED_YOU_CANNOT_MOUNT_A_MOUNTABLE = 2759; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¾Ñ�ÐµÐ´Ð»Ð°Ñ‚ÑŒ
    // ÐŸÐ¸Ñ‚Ð¾Ð¼Ñ†Ð°,
    // Ð±ÑƒÐ´ÑƒÑ‡Ð¸
    // Ð¾Ð³Ñ€Ð°Ð½Ð¸Ñ‡ÐµÐ½Ñ‹
    // Ð²
    // Ð´ÐµÐ¹Ñ�Ñ‚Ð²Ð¸Ñ�Ñ….
    public static final int WHEN_ACTIONS_ARE_PROHIBITED_YOU_CANNOT_CONTROL_THE_TARGET = 2760; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // ÑƒÐ¿Ñ€Ð°Ð²Ð»Ñ�Ñ‚ÑŒ
    // Ñ†ÐµÐ»ÑŒÑŽ,
    // Ð±ÑƒÐ´ÑƒÑ‡Ð¸
    // Ð¾Ð³Ñ€Ð°Ð½Ð¸Ñ‡ÐµÐ½Ñ‹
    // Ð²
    // Ð´ÐµÐ¹Ñ�Ñ‚Ð²Ð¸Ñ�Ñ….
    public static final int YOU_MUST_TARGET_THE_ONE_YOU_WISH_TO_CONTROL = 2761; // Ð¡Ð½Ð°Ñ‡Ð°Ð»Ð°
    // Ð’Ñ‹
    // Ð´Ð¾Ð»Ð¶Ð½Ñ‹
    // Ð²Ñ‹Ð±Ñ€Ð°Ñ‚ÑŒ
    // Ñ†ÐµÐ»ÑŒ,
    // ÐºÐ¾Ñ‚Ð¾Ñ€Ð¾Ð¹
    // Ñ…Ð¾Ñ‚Ð¸Ñ‚Ðµ
    // ÑƒÐ¿Ñ€Ð°Ð²Ð»Ñ�Ñ‚ÑŒ.
    public static final int YOU_CANNOT_CONTROL_BECAUSE_YOU_ARE_TOO_FAR = 2762; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // ÑƒÐ¿Ñ€Ð°Ð²Ð»Ñ�Ñ‚ÑŒ
    // Ñ†ÐµÐ»ÑŒÑŽ,
    // Ñ‚Ð°Ðº
    // ÐºÐ°Ðº
    // Ð¾Ð½Ð°
    // Ð½Ð°Ñ…Ð¾Ð´Ð¸Ñ‚Ñ�Ñ�
    // Ñ�Ð»Ð¸ÑˆÐºÐ¾Ð¼
    // Ð´Ð°Ð»ÐµÐºÐ¾.
    public static final int YOU_CANNOT_ENTER_THE_BATTLEFIELD_WHILE_IN_A_PARTY_STATE = 2763; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¿Ð¾Ð¿Ð°Ñ�Ñ‚ÑŒ
    // Ð½Ð°
    // ÐŸÐ¾Ð»Ðµ
    // Ð‘Ð¸Ñ‚Ð²Ñ‹,
    // Ñ�Ð¾Ñ�Ñ‚Ð¾Ñ�
    // Ð²
    // Ð³Ñ€ÑƒÐ¿Ð¿Ðµ.
    public static final int YOU_CANNOT_ENTER_BECAUSE_THE_CORRESPONDING_ALLIANCE_CHANNEL_S_MAXIMUM_NUMBER_OF_ENTRANTS_HAS = 2764; // ÐšÐ¾Ð»Ð¸Ñ‡ÐµÑ�Ñ‚Ð²Ð¾
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð½Ð¸ÐºÐ¾Ð²
    // Ð¡Ð¾ÑŽÐ·Ð½Ð¾Ð³Ð¾
    // ÐšÐ°Ð½Ð°Ð»Ð°
    // Ð´Ð¾Ñ�Ñ‚Ð¸Ð³Ð»Ð¾
    // Ð¼Ð°ÐºÑ�Ð¸Ð¼ÑƒÐ¼Ð°,
    // Ð¿Ð¾Ñ�Ñ‚Ð¾Ð¼Ñƒ
    // Ð²Ñ…Ð¾Ð´
    // Ð²Ð¾Ñ�Ð¿Ñ€ÐµÑ‰ÐµÐ½.
    public static final int ONLY_THE_ALLIANCE_CHANNEL_LEADER_CAN_ATTEMPT_ENTRY = 2765; // Ð’Ð¾Ð¹Ñ‚Ð¸
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ñ‚Ð¾Ð»ÑŒÐºÐ¾
    // Ð“Ð»Ð°Ð²Ð°
    // Ð¡Ð¾ÑŽÐ·Ð½Ð¾Ð³Ð¾
    // ÐšÐ°Ð½Ð°Ð»Ð°.
    public static final int SEED_OF_INFINITY_STAGE_1_ATTACK_IN_PROGRESS = 2766; // Ð˜Ð´ÐµÑ‚
    // ÐžÑ�Ð°Ð´Ð°
    // 1-Ð³Ð¾
    // ÑƒÑ€Ð¾Ð²Ð½Ñ�
    // Ð¡ÐµÐ¼ÐµÐ½Ð¸
    // Ð‘ÐµÑ�Ñ�Ð¼ÐµÑ€Ñ‚Ð¸Ñ�
    public static final int SEED_OF_INFINITY_STAGE_2_ATTACK_IN_PROGRESS = 2767; // Ð˜Ð´ÐµÑ‚
    // ÐžÑ�Ð°Ð´Ð°
    // 2-Ð³Ð¾
    // ÑƒÑ€Ð¾Ð²Ð½Ñ�
    // Ð¡ÐµÐ¼ÐµÐ½Ð¸
    // Ð‘ÐµÑ�Ñ�Ð¼ÐµÑ€Ñ‚Ð¸Ñ�
    public static final int SEED_OF_INFINITY_CONQUEST_COMPLETE = 2768; // Ð¡ÐµÐ¼Ñ�
    // Ð‘ÐµÑ�Ñ�Ð¼ÐµÑ€Ñ‚Ð¸Ñ�
    // Ð·Ð°Ñ…Ð²Ð°Ñ‡ÐµÐ½Ð¾
    public static final int SEED_OF_INFINITY_STAGE_1_DEFENSE_IN_PROGRESS = 2769; // Ð˜Ð´ÐµÑ‚
    // ÐžÐ±Ð¾Ñ€Ð¾Ð½Ð°
    // 1-Ð³Ð¾
    // ÑƒÑ€Ð¾Ð²Ð½Ñ�
    // Ð¡ÐµÐ¼ÐµÐ½Ð¸
    // Ð‘ÐµÑ�Ñ�Ð¼ÐµÑ€Ñ‚Ð¸Ñ�
    public static final int SEED_OF_INFINITY_STAGE_2_DEFENSE_IN_PROGRESS = 2770; // Ð˜Ð´ÐµÑ‚
    // ÐžÐ±Ð¾Ñ€Ð¾Ð½Ð°
    // 2-Ð³Ð¾
    // ÑƒÑ€Ð¾Ð²Ð½Ñ�
    // Ð¡ÐµÐ¼ÐµÐ½Ð¸
    // Ð‘ÐµÑ�Ñ�Ð¼ÐµÑ€Ñ‚Ð¸Ñ�
    public static final int SEED_OF_DESTRUCTION_ATTACK_IN_PROGRESS = 2771; // ÐŸÑ€Ð¾Ñ…Ð¾Ð´Ð¸Ñ‚
    // ÐžÑ�Ð°Ð´Ð°
    // Ð¡ÐµÐ¼ÐµÐ½Ð¸
    // Ð Ð°Ð·Ñ€ÑƒÑˆÐµÐ½Ð¸Ñ�
    public static final int SEED_OF_DESTRUCTION_CONQUEST_COMPLETE = 2772; // Ð¡ÐµÐ¼Ñ�
    // Ð Ð°Ð·Ñ€ÑƒÑˆÐµÐ½Ð¸Ñ�
    // Ð·Ð°Ñ…Ð²Ð°Ñ‡ÐµÐ½Ð¾
    public static final int SEED_OF_DESTRUCTION_DEFENSE_IN_PROGRESS = 2773; // Ð˜Ð´ÐµÑ‚
    // ÐžÐ±Ð¾Ñ€Ð¾Ð½Ð°
    // Ð¡ÐµÐ¼ÐµÐ½Ð¸
    // Ð Ð°Ð·Ñ€ÑƒÑˆÐµÐ½Ð¸Ñ�
    public static final int YOU_CAN_MAKE_ANOTHER_REPORT_IN_S1_MINUTES_YOU_HAVE_S2_POINTS_REMAINING_ON_THIS_ACCOUNT = 2774; // Ð§ÐµÑ€ÐµÐ·
    // $s1
    // Ð’Ñ‹
    // Ñ�Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¿Ð¾Ð´Ð°Ñ‚ÑŒ
    // Ð¿ÐµÑ‚Ð¸Ñ†Ð¸ÑŽ.
    // Ð£
    // Ð’Ð°Ñ�
    // Ð¾Ñ�Ñ‚Ð°Ð»Ð¾Ñ�ÑŒ
    // ÐµÑ‰Ðµ
    // $s2
    // Ð¾Ñ‡ÐºÐ¾Ð².
    public static final int THE_MATCH_CANNOT_TAKE_PLACE_BECAUSE_A_PARTY_MEMBER_IS_IN_THE_PROCESS_OF_BOARDING = 2775; // Ð§Ð»ÐµÐ½
    // Ð³Ñ€ÑƒÐ¿Ð¿Ñ‹
    // Ñ�ÐµÐ¹Ñ‡Ð°Ñ�
    // Ð½Ð°Ñ…Ð¾Ð´Ð¸Ñ‚Ñ�Ñ�
    // Ð²ÐµÑ€Ñ…Ð¾Ð¼
    // Ð½Ð°
    // ÐŸÐ¸Ñ‚Ð¾Ð¼Ñ†Ðµ
    // Ð¸
    // Ð¿Ð¾Ñ�Ñ‚Ð¾Ð¼Ñƒ
    // Ð”ÑƒÑ�Ð»ÑŒ
    // Ð¼ÐµÐ¶Ð´Ñƒ
    // Ð“Ñ€ÑƒÐ¿Ð¿Ð°Ð¼Ð¸
    // Ð½ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð°.
    public static final int THE_EFFECT_OF_TERRITORY_WARD_IS_DISAPPEARING = 2776; // Ð­Ñ„Ñ„ÐµÐºÑ‚
    // Ð—Ð½Ð°Ð¼ÐµÐ½Ð¸
    // Ð¿Ñ€Ð¾Ð¿Ð°Ð´Ð°ÐµÑ‚.
    public static final int THE_AIRSHIP_SUMMON_LICENSE_HAS_BEEN_ENTERED_YOUR_CLAN_CAN_NOW_SUMMON_THE_AIRSHIP = 2777; // Ð’Ñ‹
    // ÑƒÑ�Ð¿ÐµÑˆÐ½Ð¾
    // Ð¿ÐµÑ€ÐµÐ´Ð°Ð»Ð¸
    // Ñ€Ð°Ð·Ñ€ÐµÑˆÐµÐ½Ð¸Ðµ
    // Ð½Ð°
    // Ð²Ñ‹Ð·Ð¾Ð²
    // Ð›ÐµÑ‚Ð°ÑŽÑ‰ÐµÐ³Ð¾
    // ÐšÐ¾Ñ€Ð°Ð±Ð»Ñ�.
    // Ð¢ÐµÐ¿ÐµÑ€ÑŒ
    // Ð’Ð°Ñˆ
    // ÐºÐ»Ð°Ð½
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð²Ñ‹Ð·Ð²Ð°Ñ‚ÑŒ
    // Ð›ÐµÑ‚Ð°ÑŽÑ‰Ð¸Ð¹
    // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ.
    public static final int YOU_CANNOT_TELEPORT_WHILE_IN_POSSESSION_OF_A_WARD = 2778; // Ð¡Ð¾
    // Ð—Ð½Ð°Ð¼ÐµÐ½ÐµÐ¼
    // Ñ‚ÐµÐ»ÐµÐ¿Ð¾Ñ€Ñ‚
    // Ð½ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶ÐµÐ½.
    public static final int FURTHER_INCREASE_IN_ALTITUDE_IS_NOT_ALLOWED = 2779; // Ð’Ñ‹
    // Ð´Ð¾Ñ�Ñ‚Ð¸Ð³Ð»Ð¸
    // Ð¼Ð°ÐºÑ�Ð¸Ð¼Ð°Ð»ÑŒÐ½Ð¾Ð¹
    // Ð²Ñ‹Ñ�Ð¾Ñ‚Ñ‹.
    public static final int FURTHER_DECREASE_IN_ALTITUDE_IS_NOT_ALLOWED = 2780; // Ð’Ñ‹
    // Ð½Ð°Ñ…Ð¾Ð´Ð¸Ñ‚ÐµÑ�ÑŒ
    // Ð½Ð°
    // Ð¼Ð¸Ð½Ð¸Ð¼Ð°Ð»ÑŒÐ½Ð¾Ð¹
    // Ð²Ñ‹Ñ�Ð¾Ñ‚Ðµ.
    public static final int NUMBER_OF_UNITS__S1 = 2781; // $s1 ÐµÐ´.
    public static final int NUMBER_OF_PEOPLE__S1 = 2782; // $s1 Ñ‡ÐµÐ».
    public static final int NO_ONE_IS_LEFT_FROM_THE_OPPOSING_TEAM_THUS_VICTORY_IS_YOURS = 2783; // ÐŸÑ€Ð¾Ñ‚Ð¸Ð²Ð½Ð¸Ðº
    // Ð¿Ð¾ÐºÐ¸Ð½ÑƒÐ»
    // Ð�Ñ€ÐµÐ½Ñƒ.
    // Ð’Ñ‹
    // Ð¿Ð¾Ð±ÐµÐ´Ð¸Ð»Ð¸.
    public static final int THE_BATTLEFIELD_HAS_BEEN_CLOSED_THE_MATCH_HAS_ENDED_IN_A_TIE_BECAUSE_THE_MATCH_LASTED_FOR_S1 = 2784; // ÐŸÑ€Ð¾Ñ‚Ð¸Ð²Ð½Ð¸Ðº
    // Ð¿Ð¾ÐºÐ¸Ð½ÑƒÐ»
    // Ð�Ñ€ÐµÐ½Ñƒ.
    // Ð’Ñ€ÐµÐ¼Ñ�
    // Ð±Ð¸Ñ‚Ð²Ñ‹
    // Ñ�Ð¾Ñ�Ñ‚Ð°Ð²Ð¸Ð»Ð¾
    // $s1
    // Ð¼Ð¸Ð½.
    // $s2
    // Ñ�ÐµÐº.
    // Ð½Ðµ
    // Ð´Ð¾Ñ�Ñ‚Ð¸Ð³Ð½ÑƒÐ²
    // Ð¼Ð¸Ð½Ð¸Ð¼Ð°Ð»ÑŒÐ½Ñ‹Ñ…
    // 15-Ñ‚Ð¸
    // Ð¼Ð¸Ð½.,
    // Ð¿Ð¾Ñ�Ñ‚Ð¾Ð¼Ñƒ
    // Ð·Ð°Ñ‡Ð¸Ñ�Ð»ÐµÐ½Ð°
    // Ð½Ð¸Ñ‡ÑŒÑ�.
    public static final int IT_S_A_LARGE_SCALED_AIRSHIP_FOR_TRANSPORTATIONS_AND_BATTLES_AND_CAN_BE_OWNED_BY_THE_UNIT_OF_CLAN = 2785; // Ð”Ð°Ð½Ð½Ñ‹Ð¹
    // Ð›ÐµÑ‚Ð°ÑŽÑ‰Ð¸Ð¹
    // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð¿Ñ€Ð¸Ð½Ð°Ð´Ð»ÐµÐ¶Ð°Ñ‚ÑŒ
    // Ð¸Ñ�ÐºÐ»ÑŽÑ‡Ð¸Ñ‚ÐµÐ»ÑŒÐ½Ð¾
    // ÐºÐ»Ð°Ð½Ð°Ð¼,
    // Ð¾Ð½
    // Ð¾Ñ‚Ð»Ð¸Ñ‡Ð½Ð¾
    // Ð¿Ð¾Ð´Ñ…Ð¾Ð´Ñ�Ñ‚
    // Ð´Ð»Ñ�
    // Ð¿ÐµÑ€ÐµÐ²Ð¾Ð·Ð¾Ðº
    // Ð¸
    // Ð±Ð¸Ñ‚Ð².
    public static final int START_ACTION_IS_AVAILABLE_ONLY_WHEN_CONTROLLING_THE_AIRSHIP = 2786; // Ð’Ð·Ð»ÐµÑ‚
    // Ð²Ð¾Ð·Ð¼Ð¾Ð¶ÐµÐ½
    // Ñ‚Ð¾Ð»ÑŒÐºÐ¾
    // Ð¿Ñ€Ð¸
    // ÑƒÐ¿Ñ€Ð°Ð²Ð»ÐµÐ½Ð¸Ð¸
    // Ð›ÐµÑ‚Ð°ÑŽÑ‰Ð¸Ð¼
    // ÐšÐ¾Ñ€Ð°Ð±Ð»ÐµÐ¼.
    public static final int C1_HAS_DRAINED_YOU_OF_S2_HP = 2787; // $c1 Ð¿Ð¾Ð³Ð»Ð¾Ñ‚Ð¸Ð»
    // Ñƒ Ð’Ð°Ñ� $s2 HP.
    public static final int MERCENARY_PARTICIPATION_IS_REQUESTED_IN_S1_TERRITORY = 2788; // Ð’Ñ‹
    // Ð¿Ð¾Ð´Ð°Ð»Ð¸
    // Ð·Ð°Ñ�Ð²ÐºÑƒ
    // Ð½Ð°
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð¸Ðµ
    // Ð²
    // Ð‘Ð¸Ñ‚Ð²Ðµ
    // Ð·Ð°
    // Ð—ÐµÐ¼Ð»Ð¸
    // $s1
    // Ð½Ð°
    // Ñ�Ñ‚Ð¾Ñ€Ð¾Ð½Ðµ
    // Ð½Ð°ÐµÐ¼Ð½Ð¸ÐºÐ¾Ð².
    public static final int MERCENARY_PARTICIPATION_REQUEST_IS_CANCELLED_IN_S1_TERRITORY = 2789; // Ð’Ñ‹
    // Ð¾Ñ‚Ð¼ÐµÐ½Ð¸Ð»Ð¸
    // Ð·Ð°Ñ�Ð²ÐºÑƒ
    // Ð½Ð°
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð¸Ðµ
    // Ð²
    // Ð‘Ð¸Ñ‚Ð²Ðµ
    // Ð·Ð°
    // Ð—ÐµÐ¼Ð»Ð¸
    // $s1
    // Ð½Ð°
    // Ñ�Ñ‚Ð¾Ñ€Ð¾Ð½Ðµ
    // Ð½Ð°ÐµÐ¼Ð½Ð¸ÐºÐ¾Ð².
    public static final int CLAN_PARTICIPATION_IS_REQUESTED_IN_S1_TERRITORY = 2790; // Ð’Ñ‹
    // Ð¿Ð¾Ð´Ð°Ð»Ð¸
    // Ð·Ð°Ñ�Ð²ÐºÑƒ
    // Ð½Ð°
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð¸Ðµ
    // Ð²
    // Ð‘Ð¸Ñ‚Ð²Ðµ
    // Ð·Ð°
    // Ð—ÐµÐ¼Ð»Ð¸
    // $s1
    // Ð½Ð°
    // Ñ�Ñ‚Ð¾Ñ€Ð¾Ð½Ðµ
    // ÐºÐ»Ð°Ð½Ð°.
    public static final int CLAN_PARTICIPATION_REQUEST_IS_CANCELLED_IN_S1_TERRITORY = 2791; // Ð’Ñ‹
    // Ð¾Ñ‚Ð¼ÐµÐ½Ð¸Ð»Ð¸
    // Ð·Ð°Ñ�Ð²ÐºÑƒ
    // Ð½Ð°
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð¸Ðµ
    // Ð²
    // Ð‘Ð¸Ñ‚Ð²Ðµ
    // Ð·Ð°
    // Ð—ÐµÐ¼Ð»Ð¸
    // $s1
    // Ð½Ð°
    // Ñ�Ñ‚Ð¾Ñ€Ð¾Ð½Ðµ
    // ÐºÐ»Ð°Ð½Ð°.
    public static final int _50_CLAN_REPUTATION_POINTS_WILL_BE_AWARDED_DO_YOU_WISH_TO_CONTINUE = 2792; // Ð£Ð²ÐµÐ»Ð¸Ñ‡ÐµÐ½Ð¸Ðµ
    // Ñ€ÐµÐ¿ÑƒÑ‚Ð°Ñ†Ð¸Ð¸
    // ÐºÐ»Ð°Ð½Ð°
    // Ð½Ð°
    // 50
    // Ð·Ð°
    // Ñ�Ñ‡ÐµÑ‚
    // Ñ�Ð²Ð¾ÐµÐ¹
    // Ñ€ÐµÐ¿ÑƒÑ‚Ð°Ñ†Ð¸Ð¸.
    // ÐŸÑ€Ð¾Ð´Ð¾Ð»Ð¶Ð¸Ñ‚ÑŒ?
    public static final int THE_MINIMUM_NUMBER_S1_OF_PEOPLE_TO_ENTER_INSTANT_ZONE_IS_NOT_MET_AND_ENTRY_IS_NOT_AVAILABLE = 2793; // ÐœÐ¸Ð½Ð¸Ð¼Ð°Ð»ÑŒÐ½Ð¾Ðµ
    // ÐºÐ¾Ð»Ð¸Ñ‡ÐµÑ�Ñ‚Ð²Ð¾
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð½Ð¸ÐºÐ¾Ð²,
    // Ð½ÐµÐ¾Ð±Ñ…Ð¾Ð´Ð¸Ð¼Ð¾Ðµ
    // Ð´Ð»Ñ�
    // Ð¿Ð¾Ð¿Ð°Ð´Ð°Ð½Ð¸Ñ�
    // Ð²Ð¾
    // Ð²Ñ€ÐµÐ¼ÐµÐ½Ð½ÑƒÑŽ
    // Ð·Ð¾Ð½Ñƒ($s1
    // Ñ‡ÐµÐ».)
    // Ð½Ðµ
    // Ð´Ð¾Ñ�Ñ‚Ð¸Ð³Ð½ÑƒÑ‚Ð¾,
    // Ð¿Ð¾Ñ�Ñ‚Ð¾Ð¼Ñƒ
    // Ð²Ñ…Ð¾Ð´
    // Ð²
    // Ð½ÐµÐµ
    // Ð½ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶ÐµÐ½.
    public static final int THE_TERRITORY_WAR_CHANNEL_AND_FUNCTIONS_WILL_NOW_BE_DEACTIVATED = 2794; // ÐšÐ°Ð½Ð°Ð»
    // Ð‘Ð¸Ñ‚Ð²Ñ‹
    // Ð¸
    // ÐµÐ³Ð¾
    // Ñ„ÑƒÐ½ÐºÑ†Ð¸Ð¸
    // Ð´ÐµÐ°ÐºÑ‚Ð¸Ð²Ð¸Ñ€Ð¾Ð²Ð°Ð½Ñ‹.
    public static final int YOU_VE_ALREADY_REQUESTED_A_TERRITORY_WAR_IN_ANOTHER_TERRITORY_ELSEWHERE = 2795; // Ð’Ñ‹
    // ÑƒÐ¶Ðµ
    // Ð¿Ð¾Ð´Ð°Ð»Ð¸
    // Ð·Ð°Ñ�Ð²ÐºÑƒ
    // Ð½Ð°
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð¸Ðµ
    // Ð²
    // Ð‘Ð¸Ñ‚Ð²Ðµ
    // Ð·Ð°
    // Ð´Ñ€ÑƒÐ³Ð¸Ðµ
    // Ð—ÐµÐ¼Ð»Ð¸.
    public static final int THE_CLAN_WHO_OWNS_THE_TERRITORY_CANNOT_PARTICIPATE_IN_THE_TERRITORY_WAR_AS_MERCENARIES = 2796; // Ð§Ð»ÐµÐ½Ñ‹
    // ÐšÐ»Ð°Ð½Ð°,
    // Ð²Ð»Ð°Ð´ÐµÑŽÑ‰ÐµÐ³Ð¾
    // Ð—ÐµÐ¼Ð»Ñ�Ð¼Ð¸,
    // Ð½Ðµ
    // Ð¼Ð¾Ð³ÑƒÑ‚
    // Ð¿Ñ€Ð¸Ð½Ñ�Ñ‚ÑŒ
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð¸Ðµ
    // Ð²
    // Ð‘Ð¸Ñ‚Ð²Ðµ
    // Ð·Ð°
    // Ð—ÐµÐ¼Ð»Ð¸
    // Ð²
    // ÐºÐ°Ñ‡ÐµÑ�Ñ‚Ð²Ðµ
    // Ð½Ð°ÐµÐ¼Ð½Ð¸ÐºÐ¾Ð².
    public static final int IT_IS_NOT_A_TERRITORY_WAR_REGISTRATION_PERIOD_SO_A_REQUEST_CANNOT_BE_MADE_AT_THIS_TIME = 2797; // ÐŸÐµÑ€Ð¸Ð¾Ð´
    // Ñ€ÐµÐ³Ð¸Ñ�Ñ‚Ñ€Ð°Ñ†Ð¸Ð¸
    // Ð·Ð°Ð²ÐµÑ€ÑˆÐµÐ½.
    // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¿Ð¾Ð´Ð°Ñ‚ÑŒ
    // Ð·Ð°Ñ�Ð²ÐºÑƒ
    // Ð½Ð°
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð¸Ðµ
    // Ð²
    // Ð‘Ð¸Ñ‚Ð²Ðµ.
    public static final int THE_TERRITORY_WAR_WILL_END_IN_S1_HOURS = 2798; // Ð”Ð¾
    // Ð·Ð°Ð²ÐµÑ€ÑˆÐµÐ½Ð¸Ñ�
    // Ð‘Ð¸Ñ‚Ð²Ñ‹
    // Ð·Ð°
    // Ð—ÐµÐ¼Ð»Ð¸
    // Ð¾Ñ�Ñ‚Ð°Ð»Ð¾Ñ�ÑŒ
    // $s1
    // Ñ‡!
    public static final int THE_TERRITORY_WAR_WILL_END_IN_S1_MINUTES = 2799; // Ð”Ð¾
    // Ð·Ð°Ð²ÐµÑ€ÑˆÐµÐ½Ð¸Ñ�
    // Ð‘Ð¸Ñ‚Ð²Ñ‹
    // Ð·Ð°
    // Ð—ÐµÐ¼Ð»Ð¸
    // Ð¾Ñ�Ñ‚Ð°Ð»Ð¾Ñ�ÑŒ
    // $s1
    // Ð¼Ð¸Ð½!

    public static final int NO_TRANSLATION_REQUIRED_2800 = 2800; // Ð�Ðµ
    // Ð¿ÐµÑ€ÐµÐ²Ð¾Ð´Ð¸Ñ‚ÑŒ
    public static final int NO_TRANSLATION_REQUIRED_2801 = 2801; // Ð�Ðµ
    // Ð¿ÐµÑ€ÐµÐ²Ð¾Ð´Ð¸Ñ‚ÑŒ
    public static final int NO_TRANSLATION_REQUIRED_2802 = 2802; // Ð�Ðµ
    // Ð¿ÐµÑ€ÐµÐ²Ð¾Ð´Ð¸Ñ‚ÑŒ
    public static final int NO_TRANSLATION_REQUIRED_2803 = 2803; // Ð�Ðµ
    // Ð¿ÐµÑ€ÐµÐ²Ð¾Ð´Ð¸Ñ‚ÑŒ
    public static final int NO_TRANSLATION_REQUIRED_2804 = 2804; // Ð�Ðµ
    // Ð¿ÐµÑ€ÐµÐ²Ð¾Ð´Ð¸Ñ‚ÑŒ
    public static final int NO_TRANSLATION_REQUIRED_2805 = 2805; // Ð�Ðµ
    // Ð¿ÐµÑ€ÐµÐ²Ð¾Ð´Ð¸Ñ‚ÑŒ
    public static final int NO_TRANSLATION_REQUIRED_2806 = 2806; // Ð�Ðµ
    // Ð¿ÐµÑ€ÐµÐ²Ð¾Ð´Ð¸Ñ‚ÑŒ
    public static final int NO_TRANSLATION_REQUIRED_2807 = 2807; // Ð�Ðµ
    // Ð¿ÐµÑ€ÐµÐ²Ð¾Ð´Ð¸Ñ‚ÑŒ
    public static final int NO_TRANSLATION_REQUIRED_2808 = 2808; // Ð�Ðµ
    // Ð¿ÐµÑ€ÐµÐ²Ð¾Ð´Ð¸Ñ‚ÑŒ
    public static final int NO_TRANSLATION_REQUIRED_2809 = 2809; // Ð�Ðµ
    // Ð¿ÐµÑ€ÐµÐ²Ð¾Ð´Ð¸Ñ‚ÑŒ
    public static final int NO_TRANSLATION_REQUIRED_2810 = 2810; // Ð�Ðµ
    // Ð¿ÐµÑ€ÐµÐ²Ð¾Ð´Ð¸Ñ‚ÑŒ
    public static final int NO_TRANSLATION_REQUIRED_2811 = 2811; // Ð�Ðµ
    // Ð¿ÐµÑ€ÐµÐ²Ð¾Ð´Ð¸Ñ‚ÑŒ
    public static final int NO_TRANSLATION_REQUIRED_2812 = 2812; // Ð�Ðµ
    // Ð¿ÐµÑ€ÐµÐ²Ð¾Ð´Ð¸Ñ‚ÑŒ
    public static final int NO_TRANSLATION_REQUIRED_2813 = 2813; // Ð�Ðµ
    // Ð¿ÐµÑ€ÐµÐ²Ð¾Ð´Ð¸Ñ‚ÑŒ
    public static final int NO_TRANSLATION_REQUIRED_2814 = 2814; // Ð�Ðµ
    // Ð¿ÐµÑ€ÐµÐ²Ð¾Ð´Ð¸Ñ‚ÑŒ
    public static final int NO_TRANSLATION_REQUIRED_2815 = 2815; // Ð�Ðµ
    // Ð¿ÐµÑ€ÐµÐ²Ð¾Ð´Ð¸Ñ‚ÑŒ

    public static final int DESIGNATE_A_SHORTCUT_KEY_FOR_THE_FLYING_TRANSFORMED_OBJECT_EXCLUSIVE_USE_SHORTCUT_WINDOW_S_NO_1 = 2816; // Ð�Ð°Ð·Ð½Ð°Ñ‡ÑŒÑ‚Ðµ
    // Ñ�Ñ€Ð»Ñ‹Ðº
    // Ðº
    // 1-Ð¼Ñƒ
    // Ñ�Ð»Ð¾Ñ‚Ñƒ
    // Ð²
    // Ð¾ÐºÐ½Ðµ
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð²
    // Ð»ÐµÑ‚Ð°ÑŽÑ‰ÐµÐ³Ð¾
    // Ñ�ÑƒÑ‰ÐµÑ�Ñ‚Ð²Ð°.
    // ÐšÐ¾Ð¼Ð±Ð¸Ð½Ð°Ñ†Ð¸Ñ�
    // Ctrl+Shift
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð½Ð°Ð·Ð½Ð°Ñ‡ÐµÐ½Ð°.
    public static final int DESIGNATE_A_SHORTCUT_KEY_FOR_THE_FLYING_TRANSFORMED_OBJECT_EXCLUSIVE_USE_SHORTCUT_WINDOW_S_NO_2 = 2817; // Ð�Ð°Ð·Ð½Ð°Ñ‡ÑŒÑ‚Ðµ
    // Ñ�Ñ€Ð»Ñ‹Ðº
    // Ðº
    // 2-Ð¼Ñƒ
    // Ñ�Ð»Ð¾Ñ‚Ñƒ
    // Ð²
    // Ð¾ÐºÐ½Ðµ
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð²
    // Ð»ÐµÑ‚Ð°ÑŽÑ‰ÐµÐ³Ð¾
    // Ñ�ÑƒÑ‰ÐµÑ�Ñ‚Ð²Ð°.
    // ÐšÐ¾Ð¼Ð±Ð¸Ð½Ð°Ñ†Ð¸Ñ�
    // Ctrl+Shift
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð½Ð°Ð·Ð½Ð°Ñ‡ÐµÐ½Ð°.
    public static final int DESIGNATE_A_SHORTCUT_KEY_FOR_THE_FLYING_TRANSFORMED_OBJECT_EXCLUSIVE_USE_SHORTCUT_WINDOW_S_NO_3 = 2818; // Ð�Ð°Ð·Ð½Ð°Ñ‡ÑŒÑ‚Ðµ
    // Ñ�Ñ€Ð»Ñ‹Ðº
    // Ðº
    // 3-Ð¼Ñƒ
    // Ñ�Ð»Ð¾Ñ‚Ñƒ
    // Ð²
    // Ð¾ÐºÐ½Ðµ
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð²
    // Ð»ÐµÑ‚Ð°ÑŽÑ‰ÐµÐ³Ð¾
    // Ñ�ÑƒÑ‰ÐµÑ�Ñ‚Ð²Ð°.
    // ÐšÐ¾Ð¼Ð±Ð¸Ð½Ð°Ñ†Ð¸Ñ�
    // Ctrl+Shift
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð½Ð°Ð·Ð½Ð°Ñ‡ÐµÐ½Ð°.
    public static final int DESIGNATE_A_SHORTCUT_KEY_FOR_THE_FLYING_TRANSFORMED_OBJECT_EXCLUSIVE_USE_SHORTCUT_WINDOW_S_NO_4 = 2819; // Ð�Ð°Ð·Ð½Ð°Ñ‡ÑŒÑ‚Ðµ
    // Ñ�Ñ€Ð»Ñ‹Ðº
    // Ðº
    // 4-Ð¼Ñƒ
    // Ñ�Ð»Ð¾Ñ‚Ñƒ
    // Ð²
    // Ð¾ÐºÐ½Ðµ
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð²
    // Ð»ÐµÑ‚Ð°ÑŽÑ‰ÐµÐ³Ð¾
    // Ñ�ÑƒÑ‰ÐµÑ�Ñ‚Ð²Ð°.
    // ÐšÐ¾Ð¼Ð±Ð¸Ð½Ð°Ñ†Ð¸Ñ�
    // Ctrl+Shift
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð½Ð°Ð·Ð½Ð°Ñ‡ÐµÐ½Ð°.
    public static final int DESIGNATE_A_SHORTCUT_KEY_FOR_THE_FLYING_TRANSFORMED_OBJECT_EXCLUSIVE_USE_SHORTCUT_WINDOW_S_NO_5 = 2820; // Ð�Ð°Ð·Ð½Ð°Ñ‡ÑŒÑ‚Ðµ
    // Ñ�Ñ€Ð»Ñ‹Ðº
    // Ðº
    // 5-Ð¼Ñƒ
    // Ñ�Ð»Ð¾Ñ‚Ñƒ
    // Ð²
    // Ð¾ÐºÐ½Ðµ
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð²
    // Ð»ÐµÑ‚Ð°ÑŽÑ‰ÐµÐ³Ð¾
    // Ñ�ÑƒÑ‰ÐµÑ�Ñ‚Ð²Ð°.
    // ÐšÐ¾Ð¼Ð±Ð¸Ð½Ð°Ñ†Ð¸Ñ�
    // Ctrl+Shift
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð½Ð°Ð·Ð½Ð°Ñ‡ÐµÐ½Ð°.
    public static final int DESIGNATE_A_SHORTCUT_KEY_FOR_THE_FLYING_TRANSFORMED_OBJECT_EXCLUSIVE_USE_SHORTCUT_WINDOW_S_NO_6 = 2821; // Ð�Ð°Ð·Ð½Ð°Ñ‡ÑŒÑ‚Ðµ
    // Ñ�Ñ€Ð»Ñ‹Ðº
    // Ðº
    // 6-Ð¼Ñƒ
    // Ñ�Ð»Ð¾Ñ‚Ñƒ
    // Ð²
    // Ð¾ÐºÐ½Ðµ
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð²
    // Ð»ÐµÑ‚Ð°ÑŽÑ‰ÐµÐ³Ð¾
    // Ñ�ÑƒÑ‰ÐµÑ�Ñ‚Ð²Ð°.
    // ÐšÐ¾Ð¼Ð±Ð¸Ð½Ð°Ñ†Ð¸Ñ�
    // Ctrl+Shift
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð½Ð°Ð·Ð½Ð°Ñ‡ÐµÐ½Ð°.
    public static final int DESIGNATE_A_SHORTCUT_KEY_FOR_THE_FLYING_TRANSFORMED_OBJECT_EXCLUSIVE_USE_SHORTCUT_WINDOW_S_NO_7 = 2822; // Ð�Ð°Ð·Ð½Ð°Ñ‡ÑŒÑ‚Ðµ
    // Ñ�Ñ€Ð»Ñ‹Ðº
    // Ðº
    // 7-Ð¼Ñƒ
    // Ñ�Ð»Ð¾Ñ‚Ñƒ
    // Ð²
    // Ð¾ÐºÐ½Ðµ
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð²
    // Ð»ÐµÑ‚Ð°ÑŽÑ‰ÐµÐ³Ð¾
    // Ñ�ÑƒÑ‰ÐµÑ�Ñ‚Ð²Ð°.
    // ÐšÐ¾Ð¼Ð±Ð¸Ð½Ð°Ñ†Ð¸Ñ�
    // Ctrl+Shift
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð½Ð°Ð·Ð½Ð°Ñ‡ÐµÐ½Ð°.
    public static final int DESIGNATE_A_SHORTCUT_KEY_FOR_THE_FLYING_TRANSFORMED_OBJECT_EXCLUSIVE_USE_SHORTCUT_WINDOW_S_NO_8 = 2823; // Ð�Ð°Ð·Ð½Ð°Ñ‡ÑŒÑ‚Ðµ
    // Ñ�Ñ€Ð»Ñ‹Ðº
    // Ðº
    // 8-Ð¼Ñƒ
    // Ñ�Ð»Ð¾Ñ‚Ñƒ
    // Ð²
    // Ð¾ÐºÐ½Ðµ
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð²
    // Ð»ÐµÑ‚Ð°ÑŽÑ‰ÐµÐ³Ð¾
    // Ñ�ÑƒÑ‰ÐµÑ�Ñ‚Ð²Ð°.
    // ÐšÐ¾Ð¼Ð±Ð¸Ð½Ð°Ñ†Ð¸Ñ�
    // Ctrl+Shift
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð½Ð°Ð·Ð½Ð°Ñ‡ÐµÐ½Ð°.
    public static final int DESIGNATE_A_SHORTCUT_KEY_FOR_THE_FLYING_TRANSFORMED_OBJECT_EXCLUSIVE_USE_SHORTCUT_WINDOW_S_NO_9 = 2824; // Ð�Ð°Ð·Ð½Ð°Ñ‡ÑŒÑ‚Ðµ
    // Ñ�Ñ€Ð»Ñ‹Ðº
    // Ðº
    // 9-Ð¼Ñƒ
    // Ñ�Ð»Ð¾Ñ‚Ñƒ
    // Ð²
    // Ð¾ÐºÐ½Ðµ
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð²
    // Ð»ÐµÑ‚Ð°ÑŽÑ‰ÐµÐ³Ð¾
    // Ñ�ÑƒÑ‰ÐµÑ�Ñ‚Ð²Ð°.
    // ÐšÐ¾Ð¼Ð±Ð¸Ð½Ð°Ñ†Ð¸Ñ�
    // Ctrl+Shift
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð½Ð°Ð·Ð½Ð°Ñ‡ÐµÐ½Ð°.
    public static final int DESIGNATE_A_SHORTCUT_KEY_FOR_THE_FLYING_TRANSFORMED_OBJECT_EXCLUSIVE_USE_SHORTCUT_WINDOW_S_NO_10 = 2825; // Ð�Ð°Ð·Ð½Ð°Ñ‡ÑŒÑ‚Ðµ
    // Ñ�Ñ€Ð»Ñ‹Ðº
    // Ðº
    // 10-Ð¼Ñƒ
    // Ñ�Ð»Ð¾Ñ‚Ñƒ
    // Ð²
    // Ð¾ÐºÐ½Ðµ
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð²
    // Ð»ÐµÑ‚Ð°ÑŽÑ‰ÐµÐ³Ð¾
    // Ñ�ÑƒÑ‰ÐµÑ�Ñ‚Ð²Ð°.
    // ÐšÐ¾Ð¼Ð±Ð¸Ð½Ð°Ñ†Ð¸Ñ�
    // Ctrl+Shift
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð½Ð°Ð·Ð½Ð°Ñ‡ÐµÐ½Ð°.
    public static final int DESIGNATE_A_SHORTCUT_KEY_FOR_THE_FLYING_TRANSFORMED_OBJECT_EXCLUSIVE_USE_SHORTCUT_WINDOW_S_NO_11 = 2826; // Ð�Ð°Ð·Ð½Ð°Ñ‡ÑŒÑ‚Ðµ
    // Ñ�Ñ€Ð»Ñ‹Ðº
    // Ðº
    // 11-Ð¼Ñƒ
    // Ñ�Ð»Ð¾Ñ‚Ñƒ
    // Ð²
    // Ð¾ÐºÐ½Ðµ
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð²
    // Ð»ÐµÑ‚Ð°ÑŽÑ‰ÐµÐ³Ð¾
    // Ñ�ÑƒÑ‰ÐµÑ�Ñ‚Ð²Ð°.
    // ÐšÐ¾Ð¼Ð±Ð¸Ð½Ð°Ñ†Ð¸Ñ�
    // Ctrl+Shift
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð½Ð°Ð·Ð½Ð°Ñ‡ÐµÐ½Ð°.
    public static final int DESIGNATE_A_SHORTCUT_KEY_FOR_THE_FLYING_TRANSFORMED_OBJECT_EXCLUSIVE_USE_SHORTCUT_WINDOW_S_NO_12 = 2827; // Ð�Ð°Ð·Ð½Ð°Ñ‡ÑŒÑ‚Ðµ
    // Ñ�Ñ€Ð»Ñ‹Ðº
    // Ðº
    // 12-Ð¼Ñƒ
    // Ñ�Ð»Ð¾Ñ‚Ñƒ
    // Ð²
    // Ð¾ÐºÐ½Ðµ
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð²
    // Ð»ÐµÑ‚Ð°ÑŽÑ‰ÐµÐ³Ð¾
    // Ñ�ÑƒÑ‰ÐµÑ�Ñ‚Ð²Ð°.
    // ÐšÐ¾Ð¼Ð±Ð¸Ð½Ð°Ñ†Ð¸Ñ�
    // Ctrl+Shift
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð½Ð°Ð·Ð½Ð°Ñ‡ÐµÐ½Ð°.
    public static final int DESIGNATE_A_SHORTCUT_KEY_FOR_THE_MOUNTABLE_EXCLUSIVE_USE_SHORTCUT_WINDOW_S_NO_1_SLOT_THE_CTRL = 2828; // Ð�Ð°Ð·Ð½Ð°Ñ‡ÑŒÑ‚Ðµ
    // Ñ�Ñ€Ð»Ñ‹Ðº
    // Ðº
    // 1-Ð¼Ñƒ
    // Ñ�Ð»Ð¾Ñ‚Ñƒ
    // Ð²
    // Ð¾ÐºÐ½Ðµ
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð²
    // ÐµÐ·Ð´Ð¾Ð²Ð¾Ð³Ð¾
    // Ð¶Ð¸Ð²Ð¾Ñ‚Ð½Ð¾Ð³Ð¾.
    // ÐšÐ¾Ð¼Ð±Ð¸Ð½Ð°Ñ†Ð¸Ñ�
    // Ctrl+Shift
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð½Ð°Ð·Ð½Ð°Ñ‡ÐµÐ½Ð°.
    public static final int DESIGNATE_A_SHORTCUT_KEY_FOR_THE_MOUNTABLE_EXCLUSIVE_USE_SHORTCUT_WINDOW_S_NO_2_SLOT_THE_CTRL = 2829; // Ð�Ð°Ð·Ð½Ð°Ñ‡ÑŒÑ‚Ðµ
    // Ñ�Ñ€Ð»Ñ‹Ðº
    // Ðº
    // 2-Ð¼Ñƒ
    // Ñ�Ð»Ð¾Ñ‚Ñƒ
    // Ð²
    // Ð¾ÐºÐ½Ðµ
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð²
    // ÐµÐ·Ð´Ð¾Ð²Ð¾Ð³Ð¾
    // Ð¶Ð¸Ð²Ð¾Ñ‚Ð½Ð¾Ð³Ð¾.
    // ÐšÐ¾Ð¼Ð±Ð¸Ð½Ð°Ñ†Ð¸Ñ�
    // Ctrl+Shift
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð½Ð°Ð·Ð½Ð°Ñ‡ÐµÐ½Ð°.
    public static final int DESIGNATE_A_SHORTCUT_KEY_FOR_THE_MOUNTABLE_EXCLUSIVE_USE_SHORTCUT_WINDOW_S_NO_3_SLOT_THE_CTRL = 2830; // Ð�Ð°Ð·Ð½Ð°Ñ‡ÑŒÑ‚Ðµ
    // Ñ�Ñ€Ð»Ñ‹Ðº
    // Ðº
    // 3-Ð¼Ñƒ
    // Ñ�Ð»Ð¾Ñ‚Ñƒ
    // Ð²
    // Ð¾ÐºÐ½Ðµ
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð²
    // ÐµÐ·Ð´Ð¾Ð²Ð¾Ð³Ð¾
    // Ð¶Ð¸Ð²Ð¾Ñ‚Ð½Ð¾Ð³Ð¾.
    // ÐšÐ¾Ð¼Ð±Ð¸Ð½Ð°Ñ†Ð¸Ñ�
    // Ctrl+Shift
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð½Ð°Ð·Ð½Ð°Ñ‡ÐµÐ½Ð°.
    public static final int DESIGNATE_A_SHORTCUT_KEY_FOR_THE_MOUNTABLE_EXCLUSIVE_USE_SHORTCUT_WINDOW_S_NO_4_SLOT_THE_CTRL = 2831; // Ð�Ð°Ð·Ð½Ð°Ñ‡ÑŒÑ‚Ðµ
    // Ñ�Ñ€Ð»Ñ‹Ðº
    // Ðº
    // 4-Ð¼Ñƒ
    // Ñ�Ð»Ð¾Ñ‚Ñƒ
    // Ð²
    // Ð¾ÐºÐ½Ðµ
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð²
    // ÐµÐ·Ð´Ð¾Ð²Ð¾Ð³Ð¾
    // Ð¶Ð¸Ð²Ð¾Ñ‚Ð½Ð¾Ð³Ð¾.
    // ÐšÐ¾Ð¼Ð±Ð¸Ð½Ð°Ñ†Ð¸Ñ�
    // Ctrl+Shift
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð½Ð°Ð·Ð½Ð°Ñ‡ÐµÐ½Ð°.
    public static final int DESIGNATE_A_SHORTCUT_KEY_FOR_THE_MOUNTABLE_EXCLUSIVE_USE_SHORTCUT_WINDOW_S_NO_5_SLOT_THE_CTRL = 2832; // Ð�Ð°Ð·Ð½Ð°Ñ‡ÑŒÑ‚Ðµ
    // Ñ�Ñ€Ð»Ñ‹Ðº
    // Ðº
    // 5-Ð¼Ñƒ
    // Ñ�Ð»Ð¾Ñ‚Ñƒ
    // Ð²
    // Ð¾ÐºÐ½Ðµ
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð²
    // ÐµÐ·Ð´Ð¾Ð²Ð¾Ð³Ð¾
    // Ð¶Ð¸Ð²Ð¾Ñ‚Ð½Ð¾Ð³Ð¾.
    // ÐšÐ¾Ð¼Ð±Ð¸Ð½Ð°Ñ†Ð¸Ñ�
    // Ctrl+Shift
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð½Ð°Ð·Ð½Ð°Ñ‡ÐµÐ½Ð°.
    public static final int DESIGNATE_A_SHORTCUT_KEY_FOR_THE_MOUNTABLE_EXCLUSIVE_USE_SHORTCUT_WINDOW_S_NO_6_SLOT_THE_CTRL = 2833; // Ð�Ð°Ð·Ð½Ð°Ñ‡ÑŒÑ‚Ðµ
    // Ñ�Ñ€Ð»Ñ‹Ðº
    // Ðº
    // 6-Ð¼Ñƒ
    // Ñ�Ð»Ð¾Ñ‚Ñƒ
    // Ð²
    // Ð¾ÐºÐ½Ðµ
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð²
    // ÐµÐ·Ð´Ð¾Ð²Ð¾Ð³Ð¾
    // Ð¶Ð¸Ð²Ð¾Ñ‚Ð½Ð¾Ð³Ð¾.
    // ÐšÐ¾Ð¼Ð±Ð¸Ð½Ð°Ñ†Ð¸Ñ�
    // Ctrl+Shift
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð½Ð°Ð·Ð½Ð°Ñ‡ÐµÐ½Ð°.
    public static final int DESIGNATE_A_SHORTCUT_KEY_FOR_THE_MOUNTABLE_EXCLUSIVE_USE_SHORTCUT_WINDOW_S_NO_7_SLOT_THE_CTRL = 2834; // Ð�Ð°Ð·Ð½Ð°Ñ‡ÑŒÑ‚Ðµ
    // Ñ�Ñ€Ð»Ñ‹Ðº
    // Ðº
    // 7-Ð¼Ñƒ
    // Ñ�Ð»Ð¾Ñ‚Ñƒ
    // Ð²
    // Ð¾ÐºÐ½Ðµ
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð²
    // ÐµÐ·Ð´Ð¾Ð²Ð¾Ð³Ð¾
    // Ð¶Ð¸Ð²Ð¾Ñ‚Ð½Ð¾Ð³Ð¾.
    // ÐšÐ¾Ð¼Ð±Ð¸Ð½Ð°Ñ†Ð¸Ñ�
    // Ctrl+Shift
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð½Ð°Ð·Ð½Ð°Ñ‡ÐµÐ½Ð°.
    public static final int DESIGNATE_A_SHORTCUT_KEY_FOR_THE_MOUNTABLE_EXCLUSIVE_USE_SHORTCUT_WINDOW_S_NO_8_SLOT_THE_CTRL = 2835; // Ð�Ð°Ð·Ð½Ð°Ñ‡ÑŒÑ‚Ðµ
    // Ñ�Ñ€Ð»Ñ‹Ðº
    // Ðº
    // 8-Ð¼Ñƒ
    // Ñ�Ð»Ð¾Ñ‚Ñƒ
    // Ð²
    // Ð¾ÐºÐ½Ðµ
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð²
    // ÐµÐ·Ð´Ð¾Ð²Ð¾Ð³Ð¾
    // Ð¶Ð¸Ð²Ð¾Ñ‚Ð½Ð¾Ð³Ð¾.
    // ÐšÐ¾Ð¼Ð±Ð¸Ð½Ð°Ñ†Ð¸Ñ�
    // Ctrl+Shift
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð½Ð°Ð·Ð½Ð°Ñ‡ÐµÐ½Ð°.
    public static final int DESIGNATE_A_SHORTCUT_KEY_FOR_THE_MOUNTABLE_EXCLUSIVE_USE_SHORTCUT_WINDOW_S_NO_9_SLOT_THE_CTRL = 2836; // Ð�Ð°Ð·Ð½Ð°Ñ‡ÑŒÑ‚Ðµ
    // Ñ�Ñ€Ð»Ñ‹Ðº
    // Ðº
    // 9-Ð¼Ñƒ
    // Ñ�Ð»Ð¾Ñ‚Ñƒ
    // Ð²
    // Ð¾ÐºÐ½Ðµ
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð²
    // ÐµÐ·Ð´Ð¾Ð²Ð¾Ð³Ð¾
    // Ð¶Ð¸Ð²Ð¾Ñ‚Ð½Ð¾Ð³Ð¾.
    // ÐšÐ¾Ð¼Ð±Ð¸Ð½Ð°Ñ†Ð¸Ñ�
    // Ctrl+Shift
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð½Ð°Ð·Ð½Ð°Ñ‡ÐµÐ½Ð°.
    public static final int DESIGNATE_A_SHORTCUT_KEY_FOR_THE_MOUNTABLE_EXCLUSIVE_USE_SHORTCUT_WINDOW_S_NO_10_SLOT_THE_CTRL = 2837; // Ð�Ð°Ð·Ð½Ð°Ñ‡ÑŒÑ‚Ðµ
    // Ñ�Ñ€Ð»Ñ‹Ðº
    // Ðº
    // 10-Ð¼Ñƒ
    // Ñ�Ð»Ð¾Ñ‚Ñƒ
    // Ð²
    // Ð¾ÐºÐ½Ðµ
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð²
    // ÐµÐ·Ð´Ð¾Ð²Ð¾Ð³Ð¾
    // Ð¶Ð¸Ð²Ð¾Ñ‚Ð½Ð¾Ð³Ð¾.
    // ÐšÐ¾Ð¼Ð±Ð¸Ð½Ð°Ñ†Ð¸Ñ�
    // Ctrl+Shift
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð½Ð°Ð·Ð½Ð°Ñ‡ÐµÐ½Ð°.
    public static final int DESIGNATE_A_SHORTCUT_KEY_FOR_THE_MOUNTABLE_EXCLUSIVE_USE_SHORTCUT_WINDOW_S_NO_11_SLOT_THE_CTRL = 2838; // Ð�Ð°Ð·Ð½Ð°Ñ‡ÑŒÑ‚Ðµ
    // Ñ�Ñ€Ð»Ñ‹Ðº
    // Ðº
    // 11-Ð¼Ñƒ
    // Ñ�Ð»Ð¾Ñ‚Ñƒ
    // Ð²
    // Ð¾ÐºÐ½Ðµ
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð²
    // ÐµÐ·Ð´Ð¾Ð²Ð¾Ð³Ð¾
    // Ð¶Ð¸Ð²Ð¾Ñ‚Ð½Ð¾Ð³Ð¾.
    // ÐšÐ¾Ð¼Ð±Ð¸Ð½Ð°Ñ†Ð¸Ñ�
    // Ctrl+Shift
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð½Ð°Ð·Ð½Ð°Ñ‡ÐµÐ½Ð°.
    public static final int DESIGNATE_A_SHORTCUT_KEY_FOR_THE_MOUNTABLE_EXCLUSIVE_USE_SHORTCUT_WINDOW_S_NO_12_SLOT_THE_CTRL = 2839; // Ð�Ð°Ð·Ð½Ð°Ñ‡ÑŒÑ‚Ðµ
    // Ñ�Ñ€Ð»Ñ‹Ðº
    // Ðº
    // 12-Ð¼Ñƒ
    // Ñ�Ð»Ð¾Ñ‚Ñƒ
    // Ð²
    // Ð¾ÐºÐ½Ðµ
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð²
    // ÐµÐ·Ð´Ð¾Ð²Ð¾Ð³Ð¾
    // Ð¶Ð¸Ð²Ð¾Ñ‚Ð½Ð¾Ð³Ð¾.
    // ÐšÐ¾Ð¼Ð±Ð¸Ð½Ð°Ñ†Ð¸Ñ�
    // Ctrl+Shift
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð½Ð°Ð·Ð½Ð°Ñ‡ÐµÐ½Ð°.

    public static final int EXECUTE_THE_DESIGNATED_SHORTCUT_S_ACTION_SKILL_MACRO = 2840; // Ð—Ð°Ð¿ÑƒÑ�ÐºÐ°ÐµÑ‚
    // Ð´ÐµÐ¹Ñ�Ñ‚Ð¸Ð²Ñ�,
    // ÑƒÐ¼ÐµÐ½Ð¸Ñ�
    // Ð¸
    // Ð¼Ð°ÐºÑ€Ð¾Ñ�Ñ‹,
    // ÑƒÑ�Ñ‚Ð°Ð½Ð¾Ð²Ð»ÐµÐ½Ð½Ñ‹Ñ…
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð².
    public static final int RAISE_MY_CHARACTER_TO_THE_TOP = 2841; // ÐŸÐ¾Ð·Ð²Ð¾Ð»Ñ�ÐµÑ‚
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ñƒ
    // Ð²Ð·Ð»ÐµÑ‚ÐµÑ‚ÑŒ
    // Ð²Ð²ÐµÑ€Ñ….
    public static final int LOWER_MY_CHARACTER_TO_THE_BOTTOM = 2842; // ÐŸÐ¾Ð·Ð²Ð¾Ð»Ñ�ÐµÑ‚
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ñƒ
    // Ñ�Ð¿ÑƒÑ�Ñ‚Ð¸Ñ‚ÑŒÑ�Ñ�
    // Ð²Ð½Ð¸Ð·.
    public static final int RAISE_THE_CONTROLLED_MOUNTABLE_TO_THE_TOP = 2843; // ÐŸÐ¾Ð·Ð²Ð¾Ð»Ñ�ÐµÑ‚
    // Ð²Ð·Ð»ÐµÑ‚ÐµÑ‚ÑŒ
    // Ð²Ð²ÐµÑ€Ñ…
    // Ð½Ð°
    // ÐµÐ·Ð´Ð¾Ð²Ð¾Ð¼
    // Ð¶Ð¸Ð²Ð¾Ñ‚Ð½Ð¾Ð¼.
    public static final int LOWER_THE_CONTROLLED_MOUNTABLE_TO_THE_BOTTOM = 2844; // ÐŸÐ¾Ð·Ð²Ð¾Ð»Ñ�ÐµÑ‚
    // Ñ�Ð¿ÑƒÑ�Ñ‚Ð¸Ñ‚ÑŒÑ�Ñ�
    // Ð²Ð½Ð¸Ð·
    // Ð½Ð°
    // ÐµÐ·Ð´Ð¾Ð²Ð¾Ð¼
    // Ð¶Ð¸Ð²Ð¾Ñ‚Ð½Ð¾Ð¼.
    public static final int AUTOMATICALLY_SEND_FORWARD_MY_CHARACTER_OR_MOUNTABLE = 2845; // ÐŸÐ¾Ð·Ð²Ð¾Ð»Ñ�ÐµÑ‚
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ñƒ
    // Ð¸Ð»Ð¸
    // ÐµÐ·Ð´Ð¾Ð²Ð¾Ð¼Ñƒ
    // Ð¶Ð¸Ð²Ð¾Ñ‚Ð½Ð¾Ð¼Ñƒ
    // Ð´Ð²Ð¸Ð³Ð°Ñ‚ÑŒÑ�Ñ�
    // Ð²Ð¿ÐµÑ€ÐµÐ´
    // Ð°Ð²Ñ‚Ð¾Ð¼Ð°Ñ‚Ð¸Ñ‡ÐµÑ�ÐºÐ¸.
    public static final int NO_TRANSLATION_REQUIRED_2846 = 2846; // Ð�Ðµ
    // Ð¿ÐµÑ€ÐµÐ²Ð¾Ð´Ð¸Ñ‚ÑŒ
    public static final int NO_TRANSLATION_REQUIRED_2847 = 2847; // Ð�Ðµ
    // Ð¿ÐµÑ€ÐµÐ²Ð¾Ð´Ð¸Ñ‚ÑŒ
    public static final int NO_TRANSLATION_REQUIRED_2848 = 2848; // Ð�Ðµ
    // Ð¿ÐµÑ€ÐµÐ²Ð¾Ð´Ð¸Ñ‚ÑŒ
    public static final int NO_TRANSLATION_REQUIRED_2849 = 2849; // Ð�Ðµ
    // Ð¿ÐµÑ€ÐµÐ²Ð¾Ð´Ð¸Ñ‚ÑŒ
    public static final int NO_TRANSLATION_REQUIRED_2850 = 2850; // Ð�Ðµ
    // Ð¿ÐµÑ€ÐµÐ²Ð¾Ð´Ð¸Ñ‚ÑŒ
    public static final int STOP_ALL_ACTIONS_OF_MY_CHARACTER = 2851; // ÐžÑ�Ñ‚Ð°Ð½Ð°Ð²Ð»Ð¸Ð²Ð°ÐµÑ‚
    // Ð²Ñ�Ðµ
    // Ð¿ÐµÑ€ÐµÐ´Ð²Ð¸Ð¶ÐµÐ½Ð¸Ñ�
    // Ð’Ð°ÑˆÐµÐ³Ð¾
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°.
    public static final int STOP_ALL_ACTIONS_OF_MY_CONTROLLED_MOUNTABLE = 2852; // ÐžÑ�Ñ‚Ð°Ð½Ð°Ð²Ð»Ð¸Ð²Ð°ÐµÑ‚
    // Ð²Ñ�Ðµ
    // Ð¿ÐµÑ€ÐµÐ´Ð²Ð¸Ð¶ÐµÐ½Ð¸Ñ�
    // ÐµÐ·Ð´Ð¾Ð²Ð¾Ð³Ð¾
    // Ð¶Ð¸Ð²Ð¾Ñ‚Ð½Ð¾Ð³Ð¾.
    public static final int IF_YOU_JOIN_THE_CLAN_ACADEMY_YOU_CAN_BECOME_A_CLAN_MEMBER_AND_LEARN_THE_GAME_SYSTEM_UNTIL_YOU = 2853; // ÐŸÐ¾Ñ�Ñ‚ÑƒÐ¿Ð¸Ð²
    // Ð²
    // Ð°ÐºÐ°Ð´ÐµÐ¼Ð¸ÑŽ
    // ÐºÐ»Ð°Ð½Ð°,
    // Ð’Ñ‹
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ñ�Ñ‚Ð°Ñ‚ÑŒ
    // Ñ‡Ð»ÐµÐ½Ð¾Ð¼
    // ÐºÐ»Ð°Ð½Ð°
    // Ð¸
    // Ð¸Ð·ÑƒÑ‡Ð°Ñ‚ÑŒ
    // Ð¸Ð³Ñ€Ð¾Ð²ÑƒÑŽ
    // Ñ�Ð¸Ñ�Ñ‚ÐµÐ¼Ñƒ
    // Ð´Ð¾
    // Ð¿Ð¾Ð»ÑƒÑ‡ÐµÐ½Ð¸Ñ�
    // 40-Ð³Ð¾
    // ÑƒÑ€Ð¾Ð²Ð½Ñ�.
    // Ð•Ñ�Ð»Ð¸
    // Ñ…Ð¾Ñ‚Ð¸Ñ‚Ðµ
    // Ð¿Ð¾Ð»ÑƒÑ‡Ð°Ñ‚ÑŒ
    // Ð¾Ñ‚
    // Ð¸Ð³Ñ€Ñ‹
    // Ð±Ð¾Ð»ÑŒÑˆÐµ
    // ÑƒÐ´Ð¾Ð²Ð¾Ð»ÑŒÑ�Ñ‚Ð²Ð¸Ñ�,
    // Ñ‚Ð¾
    // Ð¼Ñ‹
    // Ñ€ÐµÐºÐ¾Ð¼ÐµÐ½Ð´ÑƒÐµÐ¼
    // Ð’Ð°Ð¼
    // Ð¿Ð¾Ñ�Ñ‚ÑƒÐ¿Ð¸Ñ‚ÑŒ
    // Ð²
    // Ð°ÐºÐ°Ð´ÐµÐ¼Ð¸ÑŽ
    // ÐºÐ»Ð°Ð½Ð°.
    public static final int IF_YOU_BECOME_LEVEL_40_THE_SECOND_CLASS_CHANGE_IS_AVAILABLE_IF_YOU_COMPLETE_THE_SECOND_CLASS = 2854; // ÐŸÐ¾
    // Ð´Ð¾Ñ�Ñ‚Ð¸Ð¶ÐµÐ½Ð¸Ð¸
    // 40-Ð³Ð¾
    // ÑƒÑ€Ð¾Ð²Ð½Ñ�
    // Ð’Ñ‹
    // Ð¿Ð¾Ð»ÑƒÑ‡Ð°ÐµÑ‚Ðµ
    // Ð²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾Ñ�Ñ‚ÑŒ
    // Ð²Ñ‚Ð¾Ñ€Ð¾Ð¹
    // Ñ�Ð¼ÐµÐ½Ñ‹
    // Ð¿Ñ€Ð¾Ñ„ÐµÑ�Ñ�Ð¸Ð¸.
    // ÐŸÐ¾Ñ�Ð»Ðµ
    // Ð²Ñ‚Ð¾Ñ€Ð¾Ð¹
    // Ñ�Ð¼ÐµÐ½Ñ‹
    // Ð¿Ñ€Ð¾Ñ„ÐµÑ�Ñ�Ð¸Ð¸
    // Ð²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾Ñ�Ñ‚Ð¸
    // Ð’Ð°ÑˆÐµÐ³Ð¾
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°
    // Ð²Ð¾Ð·Ñ€Ð°Ñ�Ñ‚ÑƒÑ‚.
    public static final int YOU_CAN_SEE_THE_GAME_HELP = 2855; // You can see the
    // game help.
    public static final int YOU_CAN_ASK_A_QUESTION_ABOUT_YOUR_GAME_PROGRESS_TO_A_GM = 2856; // You
    // can
    // ask
    // a
    // question
    // about
    // your
    // game
    // progress
    // to
    // a
    // GM.
    public static final int YOU_CAN_SELECT_SEVERAL_OPTIONS_RELATED_TO_THE_GAME_INCLUDING_GRAPHIC_SETTINGS_AND_SOUND_SETTINGS = 2857; // You
    // can
    // select
    // several
    // options
    // related
    // to
    // the
    // game,
    // including
    // graphic
    // settings
    // and
    // sound
    // settings.
    public static final int YOU_ARE_RESTARTING_THE_GAME_AS_ANOTHER_CHARACTER = 2858; // You
    // are
    // restarting
    // the
    // game
    // as
    // another
    // character.
    public static final int YOU_ARE_QUITTING_THE_GAME_CLIENT_AND_LOGGING_OUT_FROM_THE_SERVER = 2859; // You
    // are
    // quitting
    // the
    // game
    // client
    // and
    // logging
    // out
    // from
    // the
    // server.
    public static final int THIS_DISPLAYS_A_LIST_OF_MY_CHARACTER_S_SKILL_AND_MAGIC_ABILITIES = 2860; // This
    // displays
    // a
    // list
    // of
    // my
    // character's
    // skill
    // and
    // magic
    // abilities.
    public static final int THIS_CONFIRMS_MY_CHARACTER_S_CLAN_INFORMATION_AND_MANAGES_THE_CLAN = 2861; // This
    // confirms
    // my
    // character's
    // clan
    // information
    // and
    // manages
    // the
    // clan.
    public static final int THIS_DISPLAYS_ALL_THE_ACTIONS_THAT_MY_CHARACTER_CAN_TAKE = 2862; // This
    // displays
    // all
    // the
    // actions
    // that
    // my
    // character
    // can
    // take.
    public static final int THIS_DISPLAYS_THE_LIST_OF_ALL_THE_QUESTS_THAT_MY_CHARACTER_IS_UNDERTAKING_THE_QUEST_PROGRESS = 2863; // This
    // displays
    // the
    // list
    // of
    // all
    // the
    // quests
    // that
    // my
    // character
    // is
    // undertaking.
    // The
    // quest
    // progress
    // status
    // can
    // be
    // easily
    // managed.
    public static final int THIS_DISPLAYS_MY_CHARACTER_S_DETAILED_STATUS_INFORMATION_I_CAN_EASILY_CONFIRM_WHEN_AN_ITEM_IS = 2864; // This
    // displays
    // my
    // character's
    // detailed
    // status
    // information.
    // I
    // can
    // easily
    // confirm
    // when
    // an
    // item
    // is
    // equipped,
    // when
    // a
    // buff
    // is
    // received,
    // and
    // how
    // much
    // stronger
    // my
    // character
    // has
    // become.
    public static final int THIS_OPENS_AN_INVENTORY_WINDOW_WHERE_I_CAN_CHECK_THE_LIST_OF_ALL_MY_CHARACTER_S_ITEMS = 2865; // This
    // opens
    // an
    // inventory
    // window
    // where
    // I
    // can
    // check
    // the
    // list
    // of
    // all
    // my
    // character's
    // items.
    public static final int I_CAN_SEE_INFORMATION_ABOUT_MY_LOCATION_BY_OPENING_A_MINI_MAP_WINDOW_AND_I_CAN_CHECK_CURRENT = 2866; // I
    // can
    // see
    // information
    // about
    // my
    // location
    // by
    // opening
    // a
    // mini-map
    // window,
    // and
    // I
    // can
    // check
    // current
    // information
    // about
    // the
    // entire
    // game
    // world.
    public static final int CLICK_HERE_TO_SEE_A_GAME_SYSTEM_MENU_THAT_CONTAINS_VARIOUS_FUNCTIONS_OF_THE_GAME_YOU_CAN_CHECK = 2867; // Click
    // here
    // to
    // see
    // a
    // game
    // system
    // menu
    // that
    // contains
    // various
    // functions
    // of
    // the
    // game.
    // You
    // can
    // check
    // information
    // about
    // the
    // game
    // bulletin,
    // macro,
    // help,
    // GM
    // suppression
    // button,
    // game
    // option
    // button,
    // game
    // restart
    // button
    // and
    // the
    // game
    // quit
    // button.
    public static final int IF_YOU_CLICK_THE_CHAT_TAB_YOU_CAN_SELECT_AND_VIEW_THE_MESSAGES_OF_YOUR_DESIRED_GROUP_THE_TABS = 2868; // If
    // you
    // click
    // the
    // Chat
    // tab,
    // you
    // can
    // select
    // and
    // view
    // the
    // messages
    // of
    // your
    // desired
    // group.
    // The
    // tabs
    // can
    // be
    // separated
    // by
    // using
    // drag-and-drop.
    public static final int YOU_CAN_CLOSE_OR_OPEN_SPECIFIC_MESSAGES_FROM_THE_CHAT_SCREEN_YOU_CAN_CLOSE_OR_OPEN_THE_SYSTEM = 2869; // You
    // can
    // close
    // or
    // open
    // specific
    // messages
    // from
    // the
    // chat
    // screen.
    // You
    // can
    // close
    // or
    // open
    // the
    // system
    // message
    // exclusive
    // window.
    public static final int YOU_CAN_LOGIN_ONTO_MSN_MESSENGER_YOU_CAN_ALSO_CHAT_WITH_REGISTERED_FRIENDS_WITHIN_THE_GAME = 2870; // You
    // can
    // login
    // onto
    // MSN
    // Messenger.
    // You
    // can
    // also
    // chat
    // with
    // registered
    // friends
    // within
    // the
    // game.
    public static final int YOU_CAN_USE_THE_PARTY_MATCHING_FUNCTION_THAT_ALLOWS_YOU_TO_EASILY_FORM_PARTIES_WITH_OTHER = 2871; // You
    // can
    // use
    // the
    // party
    // matching
    // function
    // that
    // allows
    // you
    // to
    // easily
    // form
    // parties
    // with
    // other
    // players.
    public static final int YOU_CAN_INSTALL_VARIOUS_RAID_OPTIONS_SUCH_AS_MONSTER_OR_PARTY_MEMBER_MARK_AND_RAID_FIX = 2872; // You
    // can
    // install
    // various
    // raid
    // options
    // such
    // as
    // monster
    // or
    // party
    // member
    // mark
    // and
    // raid
    // fix.
    public static final int THIS_ENLARGES_THE_RAIDER_S_VIEW_AND_MARKS_A_MORE_DETAILED_TOPOGRAPHY = 2873; // This
    // enlarges
    // the
    // raider's
    // view
    // and
    // marks
    // a
    // more
    // detailed
    // topography.
    public static final int THIS_DIMINISHES_THE_RAIDER_S_VIEW_AND_MARKS_A_WIDER_TOPOGRAPHY = 2874; // This
    // diminishes
    // the
    // raider's
    // view
    // and
    // marks
    // a
    // wider
    // topography.
    public static final int S1_SECONDS_TO_THE_END_OF_TERRITORY_WAR = 2900; // Ð”Ð¾
    // Ð·Ð°Ð²ÐµÑ€ÑˆÐµÐ½Ð¸Ñ�
    // Ð‘Ð¸Ñ‚Ð²Ñ‹
    // Ð·Ð°
    // Ð—ÐµÐ¼Ð»Ð¸
    // Ð¾Ñ�Ñ‚Ð°Ð»Ð¾Ñ�ÑŒ
    // $s1
    // Ñ�ÐµÐº!
    public static final int YOU_CANNOT_FORCE_ATTACK_A_MEMBER_OF_THE_SAME_TERRITORY = 2901; // Ð¦ÐµÐ»ÑŒ
    // Ñ�
    // Ñ‚ÐµÑ…
    // Ð¶Ðµ
    // Ð·ÐµÐ¼ÐµÐ»ÑŒ,
    // Ñ‡Ñ‚Ð¾
    // Ð¸
    // Ð’Ñ‹,
    // Ð¿Ð¾Ñ�Ñ‚Ð¾Ð¼Ñƒ
    // Ð°Ñ‚Ð°ÐºÐ°
    // Ð½ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð°.
    public static final int YOU_VE_ACQUIRED_THE_WARD_MOVE_QUICKLY_TO_YOUR_FORCES__OUTPOST = 2902; // Ð’Ñ‹
    // Ñ�Ð¼Ð¾Ð³Ð»Ð¸
    // Ð´Ð¾Ð±Ñ‹Ñ‚ÑŒ
    // Ð—Ð½Ð°Ð¼Ñ�.
    // Ð¡ÐºÐ¾Ñ€ÐµÐµ
    // Ð²ÐµÑ€Ð½Ð¸Ñ‚ÐµÑ�ÑŒ
    // Ð²
    // Ñ�Ð²Ð¾Ð¹
    // Ð»Ð°Ð³ÐµÑ€ÑŒ.
    public static final int TERRITORY_WAR_HAS_BEGUN = 2903; // Ð‘Ð¸Ñ‚Ð²Ð° Ð·Ð° Ð—ÐµÐ¼Ð»Ð¸
    // Ð½Ð°Ñ‡Ð°Ð»Ð°Ñ�ÑŒ.
    public static final int TERRITORY_WAR_HAS_ENDED = 2904; // Ð‘Ð¸Ñ‚Ð²Ð° Ð·Ð° Ð—ÐµÐ¼Ð»Ð¸
    // Ð·Ð°Ð²ÐµÑ€ÑˆÐµÐ½Ð°.
    public static final int ALTITUDE_CANNOT_BE_DECREASED_ANY_FURTHER = 2905; // Ð’Ñ‹
    // Ð½Ð°Ñ…Ð¾Ð´Ð¸Ñ‚ÐµÑ�ÑŒ
    // Ð½Ð°
    // Ð¼Ð¸Ð½Ð¸Ð¼Ð°Ð»ÑŒÐ½Ð¾Ð¹
    // Ð²Ñ‹Ñ�Ð¾Ñ‚Ðµ.
    public static final int ALTITUDE_CANNOT_BE_INCREASED_ANY_FURTHER = 2906; // Ð’Ñ‹
    // Ð´Ð¾Ñ�Ñ‚Ð¸Ð³Ð»Ð¸
    // Ð¼Ð°ÐºÑ�Ð¸Ð¼Ð°Ð»ÑŒÐ½Ð¾Ð¹
    // Ð²Ñ‹Ñ�Ð¾Ñ‚Ñ‹.
    public static final int YOU_HAVE_ENTERED_A_POTENTIALLY_HOSTILE_ENVIRONMENT_SO_THE_AIRSHIP_S_SPEED_HAS_BEEN_GREATLY = 2907; // Ð’Ñ‹
    // Ð¿Ð¾Ð¿Ð°Ð»Ð¸
    // Ð²
    // Ñ�ÐºÐ¾Ð¿Ð»ÐµÐ½Ð¸Ðµ
    // Ð±Ð¾Ð»ÑŒÑˆÐ¾Ð³Ð¾
    // ÐºÐ¾Ð»Ð¸Ñ‡ÐµÑ�Ñ‚Ð²Ð°
    // Ð¼Ð¾Ð½Ñ�Ñ‚Ñ€Ð¾Ð².
    // Ð¡ÐºÐ¾Ñ€Ð¾Ñ�Ñ‚ÑŒ
    // Ð›ÐµÑ‚Ð°ÑŽÑ‰ÐµÐ³Ð¾
    // ÐšÐ¾Ñ€Ð°Ð±Ð»Ñ�
    // Ñ�Ð½Ð¸Ð·Ð¸Ñ‚Ñ�Ñ�.
    public static final int AS_YOU_ARE_LEAVING_THE_HOSTILE_ENVIRONMENT_THE_AIRSHIP_S_SPEED_HAS_BEEN_RETURNED_TO_NORMAL = 2908; // Ð’Ñ‹
    // Ð¿Ñ€Ð¾ÑˆÐ»Ð¸
    // Ñ‡ÐµÑ€ÐµÐ·
    // Ñ�ÐºÐ¾Ð¿Ð»ÐµÐ½Ð¸Ðµ
    // Ð±Ð¾Ð»ÑŒÑˆÐ¾Ð³Ð¾
    // ÐºÐ¾Ð»Ð¸Ñ‡ÐµÑ�Ñ‚Ð²Ð°
    // Ð¼Ð¾Ð½Ñ�Ñ‚Ñ€Ð¾Ð².
    // Ð¡ÐºÐ¾Ñ€Ð¾Ñ�Ñ‚ÑŒ
    // Ð›ÐµÑ‚Ð°ÑŽÑ‰ÐµÐ³Ð¾
    // ÐšÐ¾Ñ€Ð°Ð±Ð»Ñ�
    // Ð²Ð¾Ñ�Ñ�Ñ‚Ð°Ð½Ð¾Ð²Ð»ÐµÐ½Ð°.
    public static final int A_SERVITOR_OR_PET_CANNOT_BE_SUMMONED_WHILE_ON_AN_AIRSHIP = 2909; // Ð�Ð°Ñ…Ð¾Ð´Ñ�Ñ�ÑŒ
    // Ð²ÐµÑ€Ñ…Ð¾Ð¼
    // Ð½Ð°
    // ÐŸÐ¸Ñ‚Ð¾Ð¼Ñ†Ðµ,
    // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¿Ñ€Ð¸Ð·Ð²Ð°Ñ‚ÑŒ
    // Ð¡Ð»ÑƒÐ³Ñƒ.
    public static final int YOU_HAVE_ENTERED_AN_INCORRECT_COMMAND = 2910; // Ð�ÐµÐ²ÐµÑ€Ð½Ð°Ñ�
    // ÐºÐ¾Ð¼Ð°Ð½Ð´Ð°.
    public static final int YOU_VE_REQUESTED_C1_TO_BE_ON_YOUR_FRIENDS_LIST = 2911; // Ð’Ñ‹
    // Ð¿Ñ€ÐµÐ´Ð»Ð¾Ð¶Ð¸Ð»Ð¸
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ñƒ
    // $c1
    // Ð´Ð¾Ð±Ð°Ð²Ð¸Ñ‚ÑŒ
    // Ð’Ð°Ñ�
    // Ð²
    // Ñ�Ð¿Ð¸Ñ�Ð¾Ðº
    // Ð´Ñ€ÑƒÐ·ÐµÐ¹.
    public static final int YOU_VE_INVITED_C1_TO_JOIN_YOUR_CLAN = 2912; // Ð’Ñ‹
    // Ð¿Ñ€ÐµÐ´Ð»Ð¾Ð¶Ð¸Ð»Ð¸
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ñƒ
    // $c1
    // Ð²Ñ�Ñ‚ÑƒÐ¿Ð¸Ñ‚ÑŒ
    // Ð² Ð’Ð°Ñˆ
    // ÐºÐ»Ð°Ð½.
    public static final int CLAN_S1_HAS_SUCCEEDED_IN_CAPTURING_S2_S_TERRITORY_WARD = 2913; // ÐšÐ»Ð°Ð½
    // $s1
    // Ñ�Ð¼Ð¾Ð³
    // Ð·Ð°Ð²Ð¾ÐµÐ²Ð°Ñ‚ÑŒ
    // Ð·Ð½Ð°Ð¼Ñ�
    // $s2.
    public static final int THE_TERRITORY_WAR_WILL_BEGIN_IN_20_MINUTES_TERRITORY_RELATED_FUNCTIONS_IE__BATTLEFIELD_CHANNEL = 2914; // Ð”Ð¾
    // Ð½Ð°Ñ‡Ð°Ð»Ð°
    // Ð‘Ð¸Ñ‚Ð²Ñ‹
    // Ð·Ð°
    // Ð—ÐµÐ¼Ð»Ð¸
    // Ð¾Ñ�Ñ‚Ð°Ð»Ð¾Ñ�ÑŒ
    // 20Ð¼Ð¸Ð½!
    // Ð�ÐºÑ‚Ð¸Ð²Ð¸Ñ€ÑƒÐµÑ‚Ñ�Ñ�
    // ÐšÐ°Ð½Ð°Ð»
    // Ð‘Ð¸Ñ‚Ð²Ñ‹,
    // Ð¿Ð¾Ñ�Ð²Ð»Ñ�ÐµÑ‚Ñ�Ñ�
    // Ð²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾Ñ�Ñ‚ÑŒ
    // Ð¼Ð°Ñ�ÐºÐ¸Ñ€Ð¾Ð²ÐºÐ¸
    // Ð¸
    // Ð¿ÐµÑ€ÐµÐ²Ð¾Ð¿Ð»Ð¾Ñ‰ÐµÐ½Ð¸Ñ�.
    public static final int THIS_CLAN_MEMBER_CANNOT_WITHDRAW_OR_BE_EXPELLED_WHILE_PARTICIPATING_IN_A_TERRITORY_WAR = 2915; // Ð§Ð»ÐµÐ½
    // ÐºÐ»Ð°Ð½Ð°,
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð²ÑƒÑŽÑ‰Ð¸Ð¹
    // Ð²
    // Ð‘Ð¸Ñ‚Ð²Ðµ
    // Ð·Ð°
    // Ð—ÐµÐ¼Ð»ÑŽ,
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð¿Ð¾ÐºÐ¸Ð½ÑƒÑ‚ÑŒ
    // ÐºÐ»Ð°Ð½.
    public static final int PARTICIPATING_IN_S1_TERRITORY_WAR = 2916; // ÐŸÑ€Ð¸Ð½Ð¸Ð¼Ð°ÐµÑ‚
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð¸Ðµ
    // Ð² Ð‘Ð¸Ñ‚Ð²Ðµ
    // Ð·Ð°
    // Ð—ÐµÐ¼Ð»Ð¸
    // $s1
    public static final int NOT_PARTICIPATING_IN_A_TERRITORY_WAR = 2917; // Ð�Ðµ
    // Ð¿Ñ€Ð¸Ð½Ð¸Ð¼Ð°ÐµÑ‚
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð¸Ðµ
    // Ð²
    // Ð‘Ð¸Ñ‚Ð²Ðµ
    // Ð·Ð°
    // Ð—ÐµÐ¼Ð»Ð¸
    public static final int ONLY_CHARACTERS_WHO_ARE_LEVEL_40_OR_ABOVE_WHO_HAVE_COMPLETED_THEIR_SECOND_CLASS_TRANSFER_CAN = 2918; // Ð’
    // Ð‘Ð¸Ñ‚Ð²Ðµ
    // Ð·Ð°
    // Ð—ÐµÐ¼Ð»Ð¸
    // Ð¼Ð¾Ð³ÑƒÑ‚
    // Ð¿Ñ€Ð¸Ð½Ñ�Ñ‚ÑŒ
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð¸Ðµ
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð¸,
    // Ð´Ð¾Ñ�Ñ‚Ð¸Ð³ÑˆÐ¸Ðµ
    // 40-Ð³Ð¾
    // ÑƒÑ€Ð¾Ð²Ð½Ñ�
    // Ð¸
    // Ñ�Ð¼ÐµÐ½Ð¸Ð²ÑˆÐ¸Ðµ
    // 2-ÑŽ
    // Ð¿Ñ€Ð¾Ñ„ÐµÑ�Ñ�Ð¸ÑŽ.
    public static final int WHILE_DISGUISED_YOU_CANNOT_OPERATE_A_PRIVATE_OR_MANUFACTURE_STORE = 2919; // Ð’Ð¾
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ð¼Ð°Ñ�ÐºÐ¸Ñ€Ð¾Ð²ÐºÐ¸
    // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¾Ñ‚ÐºÑ€Ñ‹Ñ‚ÑŒ
    // Ð»Ð¸Ñ‡Ð½ÑƒÑŽ
    // Ñ‚Ð¾Ñ€Ð³Ð¾Ð²ÑƒÑŽ
    // Ð»Ð°Ð²ÐºÑƒ
    // Ð¸Ð»Ð¸
    // Ð¼Ð°Ñ�Ñ‚ÐµÑ€Ñ�ÐºÑƒÑŽ.
    public static final int NO_MORE_AIRSHIPS_CAN_BE_SUMMONED_AS_THE_MAXIMUM_AIRSHIP_LIMIT_HAS_BEEN_MET = 2920; // Ð’Ñ‹
    // Ð¿Ñ€ÐµÐ²Ñ‹Ñ�Ð¸Ð»Ð¸
    // ÐºÐ¾Ð»Ð¸Ñ‡ÐµÑ�Ñ‚Ð²Ð¾
    // Ð²Ñ‹Ð·Ð²Ð°Ð½Ð½Ñ‹Ñ…
    // Ð›ÐµÑ‚Ð°ÑŽÑ‰Ð¸Ñ…
    // ÐšÐ¾Ñ€Ð°Ð±Ð»ÐµÐ¹
    // Ð¸
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð²Ñ‹Ð·Ð²Ð°Ñ‚ÑŒ
    // ÐµÑ‰Ðµ
    // Ð¾Ð´Ð¸Ð½.
    public static final int YOU_CANNOT_BOARD_THE_AIRSHIP_BECAUSE_THE_MAXIMUM_NUMBER_FOR_OCCUPANTS_IS_MET = 2921; // Ð›ÐµÑ‚Ð°ÑŽÑ‰Ð¸Ð¹
    // ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ
    // Ð¿ÐµÑ€ÐµÐ¿Ð¾Ð»Ð½ÐµÐ½,
    // Ð¸
    // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð²
    // Ð½ÐµÐ³Ð¾
    // Ð¿Ð¾Ð¿Ð°Ñ�Ñ‚ÑŒ.
    public static final int BLOCK_CHECKER_WILL_END_IN_5_SECONDS = 2922; // Ð�Ñ€ÐµÐ½Ð°
    // Ð·Ð°ÐºÑ€Ð¾ÐµÑ‚Ñ�Ñ�
    // Ñ‡ÐµÑ€ÐµÐ·
    // 5Ñ�ÐµÐº!!!!
    public static final int BLOCK_CHECKER_WILL_END_IN_4_SECONDS = 2923; // Ð�Ñ€ÐµÐ½Ð°
    // Ð·Ð°ÐºÑ€Ð¾ÐµÑ‚Ñ�Ñ�
    // Ñ‡ÐµÑ€ÐµÐ·
    // 4Ñ�ÐµÐº!!!!
    public static final int YOU_CANNOT_ENTER_A_SEED_WHILE_IN_A_FLYING_TRANSFORMATION_STATE = 2924; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¿Ð¾Ð¿Ð°Ñ�Ñ‚ÑŒ
    // Ð²Ð½ÑƒÑ‚Ñ€ÑŒ
    // Ð¡ÐµÐ¼ÐµÐ½Ð¸,
    // Ð¿ÐµÑ€ÐµÐ²Ð¾Ð¿Ð»Ð¾Ñ‚Ð¸Ð²ÑˆÐ¸Ñ�ÑŒ
    // Ð²
    // Ð›ÐµÑ‚Ð°ÑŽÑ‰ÐµÐµ
    // Ð¡ÑƒÑ‰ÐµÑ�Ñ‚Ð²Ð¾.
    public static final int BLOCK_CHECKER_WILL_END_IN_3_SECONDS = 2925; // Ð�Ñ€ÐµÐ½Ð°
    // Ð·Ð°ÐºÑ€Ð¾ÐµÑ‚Ñ�Ñ�
    // Ñ‡ÐµÑ€ÐµÐ·
    // 3Ñ�ÐµÐº!!!!
    public static final int BLOCK_CHECKER_WILL_END_IN_2_SECONDS = 2926; // Ð�Ñ€ÐµÐ½Ð°
    // Ð·Ð°ÐºÑ€Ð¾ÐµÑ‚Ñ�Ñ�
    // Ñ‡ÐµÑ€ÐµÐ·
    // 2Ñ�ÐµÐº!!!!
    public static final int BLOCK_CHECKER_WILL_END_IN_1_SECOND = 2927; // Ð�Ñ€ÐµÐ½Ð°
    // Ð·Ð°ÐºÑ€Ð¾ÐµÑ‚Ñ�Ñ�
    // Ñ‡ÐµÑ€ÐµÐ·
    // 1Ñ�ÐµÐº!!!!
    public static final int THE_C1_TEAM_HAS_WON = 2928; // Ð’ Ñ�Ð¾Ñ€ÐµÐ²Ð½Ð¾Ð²Ð°Ð½Ð¸Ñ�Ñ…
    // Ð¿Ð¾Ð±ÐµÐ´Ð¸Ð»Ð° ÐºÐ¾Ð¼Ð°Ð½Ð´Ð° $c1.
    public static final int YOUR_REQUEST_CANNOT_BE_PROCESSED_BECAUSE_THERE_S_NO_ENOUGH_AVAILABLE_MEMORY_ON_YOUR_GRAPHIC_CARD = 2929; // ÐŸÐ°Ð¼Ñ�Ñ‚ÑŒ
    // Ð²Ð¸Ð´ÐµÐ¾ÐºÐ°Ñ€Ñ‚Ñ‹
    // Ð¼Ð°ÐºÑ�Ð¸Ð¼Ð°Ð»ÑŒÐ½Ð¾
    // Ð·Ð°Ð³Ñ€ÑƒÐ¶ÐµÐ½Ð°,
    // Ð¿Ð¾Ñ�Ñ‚Ð¾Ð¼Ñƒ
    // Ð·Ð°Ð¿ÑƒÑ�Ðº
    // Ð½ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶ÐµÐ½.
    // Ð¡Ð¼ÐµÐ½Ð¸Ñ‚Ðµ
    // Ñ€Ð°Ð·Ñ€ÐµÑˆÐµÐ½Ð¸Ðµ
    // Ð¸
    // Ð¿Ð¾Ð²Ñ‚Ð¾Ñ€Ð¸Ñ‚Ðµ
    // Ð¿Ð¾Ð¿Ñ‹Ñ‚ÐºÑƒ.
    public static final int A_GRAPHIC_CARD_INTERNAL_ERROR_HAS_OCCURRED_PLEASE_INSTALL_THE_LATEST_VERSION_OF_THE_GRAPHIC_CARD = 2930; // ÐŸÑ€Ð¾Ð¸Ð·Ð¾ÑˆÐ»Ð°
    // Ð²Ð½ÑƒÑ‚Ñ€ÐµÐ½Ð½Ñ�Ñ�
    // Ð¾ÑˆÐ¸Ð±ÐºÐ°
    // Ð²Ð¸Ð´ÐµÐ¾ÐºÐ°Ñ€Ñ‚Ñ‹.
    // ÐŸÐ¾Ð¿Ñ€Ð¾Ð±ÑƒÐ¹Ñ‚Ðµ
    // ÑƒÑ�Ñ‚Ð°Ð½Ð¾Ð²Ð¸Ñ‚ÑŒ
    // Ð¿Ð¾Ñ�Ð»ÐµÐ´Ð½ÑŽÑŽ
    // Ð²ÐµÑ€Ñ�Ð¸ÑŽ
    // Ð´Ñ€Ð°Ð¹Ð²ÐµÑ€Ð¾Ð²
    // Ð´Ð»Ñ�
    // Ð²Ð¸Ð´ÐµÐ¾ÐºÐ°Ñ€Ñ‚Ñ‹.
    public static final int THE_SYSTEM_FILE_MAY_HAVE_BEEN_DAMAGED_AFTER_ENDING_THE_GAME_PLEASE_CHECK_THE_FILE_USING_THE = 2931; // Ð•Ñ�Ñ‚ÑŒ
    // Ð²ÐµÑ€Ð¾Ñ�Ñ‚Ð½Ð¾Ñ�Ñ‚ÑŒ,
    // Ñ‡Ñ‚Ð¾
    // Ð±Ñ‹Ð»
    // Ð¿Ð¾Ð²Ñ€ÐµÐ¶Ð´ÐµÐ½
    // Ñ�Ð¸Ñ�Ñ‚ÐµÐ¼Ð½Ñ‹Ð¹
    // Ñ„Ð°Ð¹Ð».
    // Ð—Ð°Ð²ÐµÑ€ÑˆÐ¸Ñ‚Ðµ
    // Ð¸Ð³Ñƒ
    // Ð¸
    // Ñ�Ð¾Ð²ÐµÑ€ÑˆÐ¸Ñ‚Ðµ
    // Ð¿Ð¾Ð»Ð½ÑƒÑŽ
    // Ð¿Ñ€Ð¾Ð²ÐµÑ€ÐºÑƒ
    // Ñ„Ð°Ð¹Ð»Ð¾Ð²
    // Ð¸Ð³Ñ€Ñ‹.
    public static final int S1_ADENA = 2932; // $s1 Ð°Ð´ÐµÐ½.
    public static final int THOMAS_D_TURKEY_HAS_APPEARED_PLEASE_SAVE_SANTA = 2933; // ÐŸÐ¾Ñ�Ð²Ð¸Ð»Ñ�Ñ�
    // Ð‘ÐµÑˆÐµÐ½Ñ‹Ð¹
    // Ð˜Ð½Ð´ÑŽÐº.
    // Ð¡Ð¿Ð°Ñ�Ð¸Ñ‚Ðµ
    // Ð”ÐµÐ´Ð°
    // ÐœÐ¾Ñ€Ð¾Ð·Ð°!
    public static final int YOU_HAVE_DEFEATED_THOMAS_D_TURKEY_AND_RESCUED_SANTA = 2934; // Ð’Ñ‹
    // Ð²Ñ‹Ð¸Ð³Ñ€Ð°Ð»Ð¸
    // Ð¿Ð¾ÐµÐ´Ð¸Ð½Ð¾Ðº
    // Ñ�
    // Ð‘ÐµÑˆÐµÐ½Ñ‹Ð¼
    // Ð˜Ð½Ð´ÑŽÐºÐ¾Ð¼
    // Ð¸
    // Ñ�Ð¿Ð°Ñ�Ð»Ð¸
    // Ð”ÐµÐ´Ð°
    // ÐœÐ¾Ñ€Ð¾Ð·Ð°!
    public static final int YOU_FAILED_TO_RESCUE_SANTA_AND_THOMAS_D_TURKEY_HAS_DISAPPEARED = 2935; // Ð’Ñ‹
    // Ð½Ðµ
    // Ñ�Ð¼Ð¾Ð³Ð»Ð¸
    // Ñ�Ð¿Ð°Ñ�Ñ‚Ð¸
    // Ð”ÐµÐ´Ð°
    // ÐœÐ¾Ñ€Ð¾Ð·Ð°,
    // Ð¸
    // Ð‘ÐµÑˆÐµÐ½Ñ‹Ð¹
    // Ð˜Ð½Ð´ÑŽÐº
    // Ð¸Ñ�Ñ‡ÐµÐ·.
    public static final int THE_DISGUISE_SCROLL_CANNOT_BE_USED_BECAUSE_IT_IS_MEANT_FOR_USE_IN_A_DIFFERENT_TERRITORY = 2936; // Ð¡Ð²Ð¸Ñ‚Ð¾Ðº
    // Ð½Ðµ
    // Ð¿Ð¾Ð´Ñ…Ð¾Ð´Ð¸Ñ‚
    // Ðº
    // Ð—Ð¼ÐµÐ»Ñ�Ð¼,
    // Ð²
    // ÐºÐ¾Ñ‚Ð¾Ñ€Ñ‹Ñ…
    // Ð’Ñ‹
    // Ñ�Ð¾Ñ�Ñ‚Ð¾Ð¸Ñ‚Ðµ,
    // Ð¿Ð¾Ñ�Ñ‚Ð¾Ð¼Ñƒ
    // Ð½ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾
    // Ð·Ð°Ð¼Ð°Ñ�ÐºÐ¸Ñ€Ð¾Ð²Ð°Ñ‚ÑŒÑ�Ñ�.
    public static final int A_TERRITORY_OWNING_CLAN_MEMBER_CANNOT_USE_A_DISGUISE_SCROLL = 2937; // Ð§Ð»ÐµÐ½
    // ÐºÐ»Ð°Ð½Ð°,
    // Ñ‡ÐµÐ¹
    // ÐºÐ»Ð°Ð½
    // Ð²Ð»Ð°Ð´ÐµÐµÑ‚
    // Ð—ÐµÐ¼Ð»Ñ�Ð¼Ð¸,
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð·Ð°Ð¼Ð°Ñ�ÐºÐ¸Ñ€Ð¾Ð²Ð°Ñ‚ÑŒÑ�Ñ�.
    public static final int THE_DISGUISE_SCROLL_CANNOT_BE_USED_WHILE_YOU_ARE_ENGAGED_IN_A_PRIVATE_STORE_OR_MANUFACTURE = 2938; // Ð�ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾
    // Ð·Ð°Ð¼Ð°Ñ�ÐºÐ¸Ñ€Ð¾Ð²Ð°Ñ‚ÑŒÑ�Ñ�,
    // ÐºÐ¾Ð³Ð´Ð°
    // Ð’Ñ‹
    // Ð¾Ñ‚ÐºÑ€Ñ‹Ð»Ð¸
    // Ð»Ð¸Ñ‡Ð½ÑƒÑŽ
    // Ñ‚Ð¾Ñ€Ð³Ð¾Ð²ÑƒÑŽ
    // Ð»Ð°Ð²ÐºÑƒ
    // Ð¸Ð»Ð¸
    // Ð»Ð¸Ñ‡Ð½ÑƒÑŽ
    // Ð¼Ð°Ñ�Ñ‚ÐµÑ€Ñ�ÐºÑƒÑŽ
    public static final int A_DISGUISE_CANNOT_BE_USED_WHEN_YOU_ARE_IN_A_CHAOTIC_STATE = 2939; // Ð¥Ð°Ð¾Ñ‚Ð¸Ñ‡ÐµÑ�ÐºÐ¸Ðµ
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð³ÑƒÑ‚
    // Ð¼Ð°Ñ�ÐºÐ¸Ñ€Ð¾Ð²Ð°Ñ‚ÑŒÑ�Ñ�.
    public static final int YOU_CAN_INCREASE_THE_CHANCE_TO_ENCHANT_THE_ITEM_PRESS_THE_START_BUTTON_BELOW_TO_BEGIN = 2940; // Ð’Ñ‹
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒ
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚,
    // Ð¿Ð¾Ð²Ñ‹ÑˆÐ°ÑŽÑ‰Ð¸Ð¹
    // ÑˆÐ°Ð½Ñ�
    // ÑƒÑ�Ð¿ÐµÑˆÐ½Ð¾Ð³Ð¾
    // Ð·Ð°Ñ‡Ð°Ñ€Ð¾Ð²Ð°Ð½Ð¸Ñ�.
    // Ð—Ð°Ñ‡Ð°Ñ€Ð¾Ð²Ð°Ð½Ð¸Ðµ
    // Ð½Ð°Ñ‡Ð½ÐµÑ‚Ñ�Ñ�,
    // ÐµÑ�Ð»Ð¸
    // Ð½Ð°Ð¶Ð°Ñ‚ÑŒ
    // Ð½Ð°
    // ÐºÐ½Ð¾Ð¿ÐºÑƒ
    // 'Ð�Ð°Ñ‡Ð°Ñ‚ÑŒ'.
    public static final int THE_REQUEST_CANNOT_BE_COMPLETED_BECAUSE_THE_REQUIREMENTS_ARE_NOT_MET_IN_ORDER_TO_PARTICIPATE_IN = 2941; // Ð�Ðµ
    // Ð²Ñ‹Ð¿Ð¾Ð»Ð½ÐµÐ½Ñ‹
    // Ñ‚Ñ€ÐµÐ±Ð¾Ð²Ð°Ð½Ð¸Ñ�,
    // Ð¿Ð¾Ñ�Ñ‚Ð¾Ð¼Ñƒ
    // Ð½ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾
    // Ð¿Ð¾Ð´Ð°Ñ‚ÑŒ
    // Ð·Ð°Ñ�Ð²ÐºÑƒ.
    // Ð§Ñ‚Ð¾Ð±Ñ‹
    // Ð¿Ñ€Ð¸Ð½Ñ�Ñ‚ÑŒ
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð¸Ðµ
    // Ð²
    // ÐºÐ¾Ð¼Ð°Ð½Ð´Ð½Ð¾Ð¼
    // Ñ�Ð¾Ñ€ÐµÐ²Ð½Ð¾Ð²Ð°Ð½Ð¸Ð¸,
    // Ð½ÐµÐ¾Ð±Ñ…Ð¾Ð´Ð¸Ð¼Ð¾,
    // Ñ‡Ñ‚Ð¾Ð±Ñ‹
    // Ñƒ
    // Ð²Ñ�ÐµÑ…
    // Ñ‡Ð»ÐµÐ½Ð¾Ð²
    // ÐºÐ¾Ð¼Ð°Ð½Ð´Ñ‹
    // Ð¾Ñ‡ÐºÐ¸
    // ÐžÐ»Ð¸Ð¼Ð¿Ð¸Ð°Ð´Ñ‹
    // Ð±Ñ‹Ð»Ð¸
    // Ð²Ñ‹ÑˆÐµ
    // 1.
    public static final int THE_FIRST_GIFT_S_REMAINING_RESUPPLY_TIME_IS_S1_HOURS_S2_MINUTES_S3_SECONDS__IF_YOU_RESUMMON_THE = 2942; // Ð”Ð¾
    // Ð¿Ð¾Ð»ÑƒÑ‡ÐµÐ½Ð¸Ñ�
    // Ð¿ÐµÑ€Ð²Ð¾Ð³Ð¾
    // Ð¿Ð¾Ð´Ð°Ñ€ÐºÐ°
    // Ð¾Ñ�Ñ‚Ð°Ð»Ð¾Ñ�ÑŒ:
    // $s1
    // Ñ‡.
    // $s2
    // Ð¼Ð¸Ð½.
    // $s3
    // Ñ�ÐµÐº.
    // (Ð•Ñ�Ð»Ð¸
    // Ð¿Ñ€Ð¸Ð·Ð²Ð°Ñ‚ÑŒ
    // Ð�Ð³Ð°Ñ‚Ð¸Ð¾Ð½Ð°
    // ÐµÑ‰Ðµ
    // Ñ€Ð°Ð·,
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ð¿Ð¾Ð»ÑƒÑ‡ÐµÐ½Ð¸Ñ�
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð·Ð°Ð½Ñ�Ñ‚ÑŒ
    // Ð½Ð°
    // 10
    // Ð¼Ð¸Ð½ÑƒÑ‚
    // Ð±Ð¾Ð»ÑŒÑˆÐµ.)
    public static final int THE_FIRST_GIFT_S_REMAINING_RESUPPLY_TIME_IS_S1_MINUTES_S2_SECONDS_IF_YOU_RESUMMON_THE_AGATHION = 2943; // Ð”Ð¾
    // Ð¿Ð¾Ð»ÑƒÑ‡ÐµÐ½Ð¸Ñ�
    // Ð¿ÐµÑ€Ð²Ð¾Ð³Ð¾
    // Ð¿Ð¾Ð´Ð°Ñ€ÐºÐ°
    // Ð¾Ñ�Ñ‚Ð°Ð»Ð¾Ñ�ÑŒ:
    // $s1
    // Ð¼Ð¸Ð½.
    // $s2
    // Ñ�ÐµÐº.
    // (Ð•Ñ�Ð»Ð¸
    // Ð¿Ñ€Ð¸Ð·Ð²Ð°Ñ‚ÑŒ
    // Ð�Ð³Ð°Ñ‚Ð¸Ð¾Ð½Ð°
    // ÐµÑ‰Ðµ
    // Ñ€Ð°Ð·,
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ð¿Ð¾Ð»ÑƒÑ‡ÐµÐ½Ð¸Ñ�
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð·Ð°Ð½Ñ�Ñ‚ÑŒ
    // Ð½Ð°
    // 10
    // Ð¼Ð¸Ð½ÑƒÑ‚
    // Ð±Ð¾Ð»ÑŒÑˆÐµ.)
    public static final int THE_FIRST_GIFT_S_REMAINING_RESUPPLY_TIME_IS_S1_SECONDS__IF_YOU_RESUMMON_THE_AGATHION_AT_THE_GIFT = 2944; // Ð”Ð¾
    // Ð¿Ð¾Ð»ÑƒÑ‡ÐµÐ½Ð¸Ñ�
    // Ð¿ÐµÑ€Ð²Ð¾Ð³Ð¾
    // Ð¿Ð¾Ð´Ð°Ñ€ÐºÐ°
    // Ð¾Ñ�Ñ‚Ð°Ð»Ð¾Ñ�ÑŒ:
    // $s1
    // Ñ�ÐµÐº.
    // (Ð•Ñ�Ð»Ð¸
    // Ð¿Ñ€Ð¸Ð·Ð²Ð°Ñ‚ÑŒ
    // Ð�Ð³Ð°Ñ‚Ð¸Ð¾Ð½Ð°
    // ÐµÑ‰Ðµ
    // Ñ€Ð°Ð·,
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ð¿Ð¾Ð»ÑƒÑ‡ÐµÐ½Ð¸Ñ�
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð·Ð°Ð½Ñ�Ñ‚ÑŒ
    // Ð½Ð°
    // 10
    // Ð¼Ð¸Ð½ÑƒÑ‚
    // Ð±Ð¾Ð»ÑŒÑˆÐµ.)
    public static final int THE_SECOND_GIFT_S_REMAINING_RESUPPLY_TIME_IS_S1_HOURS_S2_MINUTES_S3_SECONDS__IF_YOU_RESUMMON_THE = 2945; // Ð”Ð¾
    // Ð¿Ð¾Ð»ÑƒÑ‡ÐµÐ½Ð¸Ñ�
    // Ð²Ñ‚Ð¾Ñ€Ð¾Ð³Ð¾
    // Ð¿Ð¾Ð´Ð°Ñ€ÐºÐ°
    // Ð¾Ñ�Ñ‚Ð°Ð»Ð¾Ñ�ÑŒ:
    // $s1
    // Ñ‡.
    // $s2
    // Ð¼Ð¸Ð½.
    // $s3
    // Ñ�ÐµÐº.
    // (Ð•Ñ�Ð»Ð¸
    // Ð¿Ñ€Ð¸Ð·Ð²Ð°Ñ‚ÑŒ
    // Ð�Ð³Ð°Ñ‚Ð¸Ð¾Ð½Ð°
    // ÐµÑ‰Ðµ
    // Ñ€Ð°Ð·,
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ð¿Ð¾Ð»ÑƒÑ‡ÐµÐ½Ð¸Ñ�
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð·Ð°Ð½Ñ�Ñ‚ÑŒ
    // Ð½Ð°
    // 1
    // Ñ‡.
    // 10
    // Ð¼Ð¸Ð½ÑƒÑ‚
    // Ð±Ð¾Ð»ÑŒÑˆÐµ.)
    public static final int THE_SECOND_GIFT_S_REMAINING_RESUPPLY_TIME_IS_S1_MINUTES_S2_SECONDS_IF_YOU_RESUMMON_THE_AGATHION = 2946; // Ð”Ð¾
    // Ð¿Ð¾Ð»ÑƒÑ‡ÐµÐ½Ð¸Ñ�
    // Ð²Ñ‚Ð¾Ñ€Ð¾Ð³Ð¾
    // Ð¿Ð¾Ð´Ð°Ñ€ÐºÐ°
    // Ð¾Ñ�Ñ‚Ð°Ð»Ð¾Ñ�ÑŒ:
    // $s1
    // Ð¼Ð¸Ð½.
    // $s2
    // Ñ�ÐµÐº.
    // (Ð•Ñ�Ð»Ð¸
    // Ð¿Ñ€Ð¸Ð·Ð²Ð°Ñ‚ÑŒ
    // Ð�Ð³Ð°Ñ‚Ð¸Ð¾Ð½Ð°
    // ÐµÑ‰Ðµ
    // Ñ€Ð°Ð·,
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ð¿Ð¾Ð»ÑƒÑ‡ÐµÐ½Ð¸Ñ�
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð·Ð°Ð½Ñ�Ñ‚ÑŒ
    // Ð½Ð°
    // 1
    // Ñ‡.
    // 10
    // Ð¼Ð¸Ð½ÑƒÑ‚
    // Ð±Ð¾Ð»ÑŒÑˆÐµ.)
    public static final int THE_SECOND_GIFT_S_REMAINING_RESUPPLY_TIME_IS_S1_SECONDS_IF_YOU_RESUMMON_THE_AGATHION_AT_THE_GIFT = 2947; // Ð”Ð¾
    // Ð¿Ð¾Ð»ÑƒÑ‡ÐµÐ½Ð¸Ñ�
    // Ð²Ñ‚Ð¾Ñ€Ð¾Ð³Ð¾
    // Ð¿Ð¾Ð´Ð°Ñ€ÐºÐ°
    // Ð¾Ñ�Ñ‚Ð°Ð»Ð¾Ñ�ÑŒ:
    // $s1
    // Ñ�ÐµÐº.
    // (Ð•Ñ�Ð»Ð¸
    // Ð¿Ñ€Ð¸Ð·Ð²Ð°Ñ‚ÑŒ
    // Ð�Ð³Ð°Ñ‚Ð¸Ð¾Ð½Ð°
    // ÐµÑ‰Ðµ
    // Ñ€Ð°Ð·,
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ð¿Ð¾Ð»ÑƒÑ‡ÐµÐ½Ð¸Ñ�
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð·Ð°Ð½Ñ�Ñ‚ÑŒ
    // Ð½Ð°
    // 1
    // Ñ‡.
    // 10
    // Ð¼Ð¸Ð½ÑƒÑ‚
    // Ð±Ð¾Ð»ÑŒÑˆÐµ.)
    public static final int THE_TERRITORY_WAR_EXCLUSIVE_DISGUISE_AND_TRANSFORMATION_CAN_BE_USED_20_MINUTES_BEFORE_THE_START = 2955; // ÐœÐ°Ñ�ÐºÐ¸Ñ€Ð¾Ð²ÐºÐ°
    // Ð¸
    // ÐŸÐµÑ€ÐµÐ²Ð¾Ð¿Ð»Ð¾Ñ‰ÐµÐ½Ð¸Ðµ,
    // ÐºÐ¾Ñ‚Ð¾Ñ€Ð¾Ñ‹Ðµ
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·ÑƒÑŽÑ‚Ñ�Ñ�
    // Ð²Ð¾
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ð‘Ð¸Ñ‚Ð²Ñ‹
    // Ð·Ð°
    // Ð—ÐµÐ¼Ð»Ð¸,
    // Ð´Ð¾Ñ�Ñ‚ÑƒÐ¿Ð½Ñ‹
    // Ð´Ð»Ñ�
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ð½Ð¸Ñ�
    // Ð·Ð°
    // 20
    // Ð¼Ð¸Ð½.
    // Ð´Ð¾
    // Ð‘Ð¸Ñ‚Ð²Ñ‹
    // Ð¸
    // 10
    // Ð¼Ð¸Ð½.
    // Ð¿Ð¾Ñ�Ð»Ðµ
    // ÐµÐµ
    // Ð¾ÐºÐ¾Ð½Ñ‡Ð°Ð½Ð¸Ñ�.
    public static final int A_USER_PARTICIPATING_IN_THE_OLYMPIAD_CANNOT_WITNESS_THE_BATTLE = 2956; // Ð�ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾
    // Ð½Ð°Ð±Ð»ÑŽÐ´Ð°Ñ‚ÑŒ
    // Ð·Ð°
    // Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÐµÐ»ÐµÐ¼,
    // ÐºÐ¾Ñ‚Ð¾Ñ€Ñ‹Ð¹
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð²ÑƒÐµÑ‚
    // Ð²
    // ÐžÐ»Ð¸Ð¼Ð¿Ð¸Ð°Ð´Ðµ.
    public static final int CHARACTERS_WITH_A_FEBRUARY_29_CREATION_DATE_WILL_RECEIVE_A_GIFT_ON_FEBRUARY_28 = 2957; // ÐŸÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð¸,
    // Ñ�Ð¾Ð·Ð´Ð°Ð½Ð½Ñ‹Ðµ
    // 29-Ð³Ð¾
    // Ñ„ÐµÐ²Ñ€Ð°Ð»Ñ�,
    // Ñ�Ð¼Ð¾Ð³ÑƒÑ‚
    // Ð¿Ð¾Ð»ÑƒÑ‡Ð¸Ñ‚ÑŒ
    // Ð¿Ð¾Ð´Ð°Ñ€Ð¾Ðº
    // 28-Ð³Ð¾
    // Ñ„ÐµÐ²Ñ€Ð°Ð»Ñ�.
    public static final int AN_AGATHION_HAS_ALREADY_BEEN_SUMMONED = 2958; // Ð’Ñ‹
    // ÑƒÐ¶Ðµ
    // Ð¿Ñ€Ð¸Ð·Ð²Ð°Ð»Ð¸
    // Ð�Ð³Ð°Ñ‚Ð¸Ð¾Ð½Ð°.
    public static final int YOUR_ACCOUNT_HAS_BEEN_TEMPORARILY_RESTRICTED_BECAUSE_IT_IS_SUSPECTED_OF_BEING_USED_ABNORMALLY = 2959; // Ð’Ð°Ñˆ
    // Ð°ÐºÐºÐ°ÑƒÐ½Ñ‚
    // Ð±Ñ‹Ð»
    // Ð·Ð°Ð±Ð»Ð¾ÐºÐ¸Ñ€Ð¾Ð²Ð°Ð½
    // Ð·Ð°
    // Ð½ÐµÐ¿Ñ€Ð°Ð²Ð¾Ð¼ÐµÑ€Ð½Ñ‹Ðµ
    // Ð´ÐµÐ¹Ñ�Ñ‚Ð²Ð¸Ñ�
    // Ð²Ð½ÑƒÑ‚Ñ€Ð¸
    // Ð¸Ð³Ñ€Ñ‹.
    // Ð•Ñ�Ð»Ð¸
    // Ð²Ñ‹
    // Ð½Ðµ
    // Ñ�Ð¾Ð²ÐµÑ€ÑˆÐ°Ð»Ð¸
    // Ñ‚Ð°ÐºÐ¸Ñ…
    // Ð´ÐµÐ¹Ñ�Ñ‚Ð²Ð¸Ð¹,
    // Ð²Ð°Ð¼
    // Ð½ÐµÐ¾Ð±Ñ…Ð¾Ð´Ð¸Ð¼Ð¾
    // Ð·Ð°Ð¹Ñ‚Ð¸
    // Ð½Ð°
    // Ð²ÐµÐ±-Ñ�Ð°Ð¹Ñ‚
    // Ð¸Ð³Ñ€Ñ‹
    // Ð¸
    // Ð¿Ñ€Ð¾Ð¹Ñ‚Ð¸
    // Ð¸Ð´ÐµÐ½Ñ‚Ð¸Ñ„Ð¸ÐºÐ°Ñ†Ð¸ÑŽ
    // Ð»Ð¸Ñ‡Ð½Ð¾Ñ�Ñ‚Ð¸.
    // Ð‘Ð¾Ð»ÐµÐµ
    // Ð¿Ð¾Ð´Ñ€Ð¾Ð±Ð½ÑƒÑŽ
    // Ð¸Ð½Ñ„Ð¾Ñ€Ð¼Ð°Ñ†Ð¸ÑŽ
    // Ð²Ñ‹
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¿Ð¾Ð»ÑƒÑ‡Ð¸Ñ‚ÑŒ,
    // Ð¾Ð±Ñ€Ð°Ñ‚Ð¸Ð²ÑˆÐ¸Ñ�ÑŒ
    // Ð²
    // Ñ�Ð»ÑƒÐ¶Ð±Ñƒ
    // Ð¿Ð¾Ð´Ð´ÐµÑ€Ð¶ÐºÐ¸.
    public static final int THE_ITEM_S1_IS_REQUIRED = 2960; // Ð’Ð°Ð¼ Ð½ÐµÐ¾Ð±Ñ…Ð¾Ð´Ð¸Ð¼
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚ $s1%
    public static final int _2_UNITS_OF_THE_ITEM_S1_IS_REQUIRED = 2961; // Ð’Ð°Ð¼
    // Ð½ÐµÐ¾Ð±Ñ…Ð¾Ð´Ð¸Ð¼
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚
    // $s1%
    // Ð²
    // ÐºÐ¾Ð»Ð¸Ñ‡ÐµÑ�Ñ‚Ð²Ðµ
    // $2
    // ÑˆÑ‚.
    public static final int THE_ITEM_HAS_BEEN_SUCCESSFULLY_PURCHASED = 6001; // ÐŸÑ€ÐµÐ´Ð¼ÐµÑ‚
    // ÑƒÑ�Ð¿ÐµÑˆÐ½Ð¾
    // Ð¿Ñ€Ð¸Ð¾Ð±Ñ€ÐµÑ‚ÐµÐ½.
    public static final int THE_ITEM_HAS_FAILED_TO_BE_PURCHASED = 6002; // ÐžÑˆÐ¸Ð±ÐºÐ°
    // Ð¿Ñ€Ð¸
    // Ð¿Ð¾ÐºÑƒÐ¿ÐºÐµ
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ð°.
    public static final int THE_ITEM_YOU_SELECTED_CANNOT_BE_PURCHASED_UNFORTUNATELY_THE_SALE_PERIOD_ENDED = 6003; // Ð�ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾
    // Ð¿Ñ€Ð¸Ð¾Ð±Ñ€ÐµÑ�Ñ‚Ð¸
    // Ð²Ñ‹Ð±Ñ€Ð°Ð½Ð½Ñ‹Ð¹
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚.
    public static final int ENCHANT_FAILED_THE_ENCHANT_SKILL_FOR_THE_CORRESPONDING_ITEM_WILL_BE_EXACTLY_RETAINED = 6004; // Ð£Ð»ÑƒÑ‡ÑˆÐµÐ½Ð¸Ðµ
    // Ð½Ðµ
    // ÑƒÐ´Ð°Ð»Ð¾Ñ�ÑŒ.
    public static final int GAME_POINTS_ARE_NOT_ENOUGH = 6005; // Ð£ Ð’Ð°Ñ� Ð½Ðµ
    // Ñ…Ð²Ð°Ñ‚Ð°ÐµÑ‚ Ð¼Ð¾Ð½ÐµÑ‚
    // 4Game.
    public static final int THE_ITEM_CANNOT_BE_RECEIVED_BECAUSE_THE_INVENTORY_WEIGHT_QUANTITY_LIMIT_HAS_BEEN_EXCEEDED = 6006; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¿Ñ€Ð¸Ð¾Ð±Ñ€ÐµÑ�Ñ‚Ð¸
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚,
    // Ñ‚Ð°Ðº
    // ÐºÐ°Ðº
    // Ð’Ð°Ñˆ
    // Ð¸Ð½Ð²ÐµÐ½Ñ‚Ð°Ñ€ÑŒ
    // Ð¿ÐµÑ€ÐµÐ¿Ð¾Ð»Ð½ÐµÐ½.
    public static final int PRODUCT_PURCHASE_ERROR___THE_USER_STATE_IS_NOT_RIGHT = 6007; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ñ�ÐµÐ¹Ñ‡Ð°Ñ�
    // Ð¿Ñ€Ð¸Ð¾Ð±Ñ€ÐµÑ�Ñ‚Ð¸
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚.
    public static final int PRODUCT_PURCHASE_ERROR___THE_PRODUCT_IS_NOT_RIGHT = 6008; // Ð�ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾
    // Ð¿Ñ€Ð¸Ð¾Ð±Ñ€ÐµÑ�Ñ‚Ð¸
    // Ð´Ð°Ð½Ð½Ñ‹Ð¹
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚.
    public static final int PRODUCT_PURCHASE_ERROR___THE_ITEM_WITHIN_THE_PRODUCT_IS_NOT_RIGHT = 6009; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¿Ñ€Ð¸Ð¾Ð±Ñ€ÐµÑ�Ñ‚Ð¸
    // ÑƒÐ¿Ð°ÐºÐ¾Ð²ÐºÑƒ
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ð¾Ð².
    public static final int THE_MASTER_ACCOUNT_OF_YOUR_ACCOUNT_HAS_BEEN_RESTRICTED = 6010; // Ð’Ð°Ñˆ
    // Ð¼Ð°Ñ�Ñ‚ÐµÑ€-Ð°ÐºÐºÐ°ÑƒÐ½Ñ‚
    // Ð±Ñ‹Ð»
    // Ð·Ð°Ð±Ð»Ð¾ÐºÐ¸Ñ€Ð¾Ð²Ð°Ð½.
    public static final int YOU_CANNOT_BOOKMARK_THIS_LOCATION_BECAUSE_YOU_DO_NOT_HAVE_A_MY_TELEPORT_FLAG = 6501; // Ð�ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾
    // Ñ�Ð¾Ñ…Ñ€Ð°Ð½Ð¸Ñ‚ÑŒ
    // Ð´Ð°Ð½Ð½Ð¾Ðµ
    // Ð¼ÐµÑ�Ñ‚Ð¾Ð¿Ð¾Ð»Ð¾Ð¶ÐµÐ½Ð¸Ðµ,
    // Ñ‚Ð°Ðº
    // ÐºÐ°Ðº
    // Ð¾Ñ‚Ñ�ÑƒÑ‚Ñ�Ñ‚Ð²ÑƒÐµÑ‚
    // Ð¤Ð»Ð°Ð³.
    public static final int MY_TELEPORT_FLAG__S1 = 6502; // Ð¤Ð»Ð°Ð³: $s1 ÐµÐ´.
    public static final int THE_EVIL_THOMAS_D_TURKEY_HAS_APPEARED_PLEASE_SAVE_SANTA = 6503; // ÐŸÐ¾Ñ�Ð²Ð¸Ð»Ñ�Ñ�
    // Ð‘ÐµÑˆÐµÐ½Ñ‹Ð¹
    // Ð˜Ð½Ð´ÑŽÐº.
    // Ð¡Ð¿Ð°Ñ�Ð¸Ñ‚Ðµ
    // Ð¡Ð°Ð½Ñ‚Ð°
    // ÐšÐ»Ð°ÑƒÑ�Ð°.
    public static final int YOU_WON_THE_BATTLE_AGAINST_THOMAS_D_TURKEY_SANTA_HAS_BEEN_RESCUED = 6504; // Ð’Ñ‹
    // Ð¿Ð¾Ð±ÐµÐ´Ð¸Ð»Ð¸
    // Ð‘ÐµÑˆÐµÐ½Ð¾Ð³Ð¾
    // Ð˜Ð½Ð´ÑŽÐºÐ°
    // Ð¸
    // Ñ�Ð¿Ð°Ñ�Ð»Ð¸
    // Ð¡Ð°Ð½Ñ‚Ð°
    // ÐšÐ»Ð°ÑƒÑ�Ð°.
    public static final int YOU_DID_NOT_RESCUE_SANTA_AND_THOMAS_D_TURKEY_HAS_DISAPPEARED = 6505; // Ð’Ñ‹
    // Ð½Ðµ
    // ÑƒÑ�Ð¿ÐµÐ»Ð¸
    // Ñ�Ð¿Ð°Ñ�Ñ‚Ð¸
    // Ð¡Ð°Ð½Ñ‚Ð°
    // ÐšÐ»Ð°ÑƒÑ�Ð°,
    // Ð¸
    // Ð˜Ð½Ð´ÑŽÐº
    // Ð¸Ñ�Ñ‡ÐµÐ·.
    public static final int ALTHOUGH_YOU_CAN_T_BE_CERTAIN_THE_AIR_SEEMS_LADEN_WITH_THE_SCENT_OF_FRESHLY_BAKED_BREAD = 6506; // ÐžÑ‚ÐºÑƒÐ´Ð°-Ñ‚Ð¾
    // Ð¸Ñ�Ñ…Ð¾Ð´Ð¸Ñ‚
    // Ð·Ð°Ð¿Ð°Ñ…
    // Ñ…Ð»ÐµÐ±Ð°.
    // ÐŸÐµÑ€ÐµÐ´
    // Ð³Ð»Ð°Ð·Ð°Ð¼Ð¸
    // Ð²Ñ�Ðµ
    // Ð¿Ð»Ñ‹Ð²ÐµÑ‚.
    public static final int YOU_FEEL_REFRESHED_EVERYTHING_APPEARS_CLEAR = 6507; // Ð�Ð°Ñ�Ñ‚Ñ€Ð¾ÐµÐ½Ð¸Ðµ
    // ÑƒÐ»ÑƒÑ‡ÑˆÐ°ÐµÑ‚Ñ�Ñ�.
    // Ð¡Ñ‚Ð°Ð½Ð¾Ð²Ð¸Ñ‚Ñ�Ñ�
    // Ð»ÑƒÑ‡ÑˆÐµ
    // Ð²Ð¸Ð´Ð½Ð¾.

    // Recommedations
    public static final int YOU_CANNOT_RECOMMEND_YOURSELF = 829; // Ð’Ñ‹ Ð½Ðµ Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ñ€ÐµÐºÐ¾Ð¼ÐµÐ½Ð´Ð¾Ð²Ð°Ñ‚ÑŒ
    // Ñ�Ð°Ð¼Ð¾Ð³Ð¾ Ñ�ÐµÐ±Ñ�.
    public static final int YOU_HAVE_BEEN_RECOMMENDED_BY_C1 = 831; // $c1 Ð´Ð°ÐµÑ‚
    // Ð’Ð°Ð¼
    // Ñ€ÐµÐºÐ¾Ð¼ÐµÐ½Ð´Ð°Ñ†Ð¸ÑŽ.
    public static final int YOU_HAVE_RECOMMENDED_C1_YOU_HAVE_S2_RECOMMENDATIONS_LEFT = 830; // $c1
    // Ð¿Ð¾Ð»ÑƒÑ‡Ð°ÐµÑ‚
    // Ð¾Ñ‚
    // Ð’Ð°Ñ�
    // Ñ€ÐµÐºÐ¾Ð¼ÐµÐ½Ð´Ð°Ñ†Ð¸ÑŽ.
    // ÐžÑ�Ñ‚Ð°Ð»Ð¾Ñ�ÑŒ
    // Ñ€ÐµÐºÐ¾Ð¼ÐµÐ½Ð´Ð°Ñ†Ð¸Ð¹:
    // $s2.
    public static final int THAT_CHARACTER_HAS_ALREADY_BEEN_RECOMMENDED = 832; // Ð’Ñ‹
    // ÑƒÐ¶Ðµ
    // Ð´Ð°Ð²Ð°Ð»Ð¸
    // Ñ€ÐµÐºÐ¾Ð¼ÐµÐ½Ð´Ð°Ñ†Ð¸ÑŽ
    // Ñ�Ñ‚Ð¾Ð¼Ñƒ
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ñƒ.
    public static final int YOU_ARE_NOT_AUTHORIZED_TO_MAKE_FURTHER_RECOMMENDATIONS_AT_THIS_TIME_YOU_WILL_RECEIVE_MORE_RECOMMENDATION_CREDITS_EACH_DAY_AT_1_PM = 833; // Ð’Ñ‹
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ð»Ð¸
    // Ð²Ñ�Ðµ
    // Ñ€ÐµÐºÐ¾Ð¼ÐµÐ½Ð´Ð°Ñ†Ð¸Ð¸.
    // ÐšÐ¾Ð»Ð¸Ñ‡ÐµÑ�Ñ‚Ð²Ð¾
    // Ñ€ÐµÐºÐ¾Ð¼ÐµÐ½Ð´Ð°Ñ†Ð¸Ð¹
    // Ð¾Ð±Ð½Ð¾Ð²Ð»Ñ�ÐµÑ‚Ñ�Ñ�
    // ÐºÐ°Ð¶Ð´Ñ‹Ð¹
    // Ð´ÐµÐ½ÑŒ
    // Ð²
    // Ñ‡Ð°Ñ�
    // Ð´Ð½Ñ�.
    public static final int ONLY_CHARACTERS_OF_LEVEL_10_OR_ABOVE_ARE_AUTHORIZED_TO_MAKE_RECOMMENDATIONS = 898; // Ð’Ñ‹
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð´Ð°Ð²Ð°Ñ‚ÑŒ
    // Ñ€ÐµÐºÐ¾Ð¼ÐµÐ½Ð´Ð°Ñ†Ð¸Ð¸
    // Ñ‚Ð¾Ð»ÑŒÐºÐ¾
    // Ð¿Ð¾
    // Ð´Ð¾Ñ�Ñ‚Ð¸Ð¶ÐµÐ½Ð¸Ð¸
    // 10-Ð³Ð¾
    // ÑƒÑ€Ð¾Ð²Ð½Ñ�.
    public static final int YOUR_SELECTED_TARGET_CAN_NO_LONGER_RECEIVE_A_RECOMMENDATION = 1188; // Ð¦ÐµÐ»ÑŒ
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ð¾Ð»ÑŒÑˆÐµ
    // Ð¿Ñ€Ð¸Ð½Ð¸Ð¼Ð°Ñ‚ÑŒ
    // Ð¾Ñ‚
    // Ð’Ð°Ñ�
    // Ñ€ÐµÐºÐ¾Ð¼ÐµÐ½Ð´Ð°Ñ†Ð¸Ð¸.

    // Duelling
    public static final int THERE_IS_NO_OPPONENT_TO_RECEIVE_YOUR_CHALLENGE_FOR_A_DUEL = 1926; // Ð�Ð¸
    // Ð¾Ð´Ð¸Ð½
    // Ð¾Ð¿Ð¿Ð¾Ð½ÐµÐ½Ñ‚
    // Ð½Ðµ
    // Ð¿Ñ€Ð¸Ð½Ñ�Ð»
    // Ð²Ð°Ñˆ
    // Ð²Ñ‹Ð·Ð¾Ð²
    // Ð½Ð°
    // Ð´ÑƒÑ�Ð»ÑŒ.
    public static final int S1_HAS_BEEN_CHALLENGED_TO_A_DUEL = 1927; // $c1
    // Ð²Ñ‹Ð·Ñ‹Ð²Ð°ÐµÑ‚Ñ�Ñ�
    // Ð½Ð°
    // Ð´ÑƒÑ�Ð»ÑŒ.
    public static final int S1S_PARTY_HAS_BEEN_CHALLENGED_TO_A_DUEL = 1928; // Ð“Ñ€ÑƒÐ¿Ð¿Ð°
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°
    // $c1
    // Ð±Ñ‹Ð»Ð°
    // Ð²Ñ‹Ð·Ð²Ð°Ð½Ð°
    // Ð½Ð°
    // Ð´ÑƒÑ�Ð»ÑŒ.
    public static final int S1_HAS_ACCEPTED_YOUR_CHALLENGE_TO_A_DUEL_THE_DUEL_WILL_BEGIN_IN_A_FEW_MOMENTS = 1929; // $c1
    // Ð¿Ñ€Ð¸Ð½Ð¸Ð¼Ð°ÐµÑ‚
    // Ð’Ð°Ñˆ
    // Ð²Ñ‹Ð·Ð¾Ð²
    // Ð½Ð°
    // Ð´ÑƒÑ�Ð»ÑŒ.
    // Ð”ÑƒÑ�Ð»ÑŒ
    // Ñ�ÐµÐ¹Ñ‡Ð°Ñ�
    // Ð½Ð°Ñ‡Ð½ÐµÑ‚Ñ�Ñ�.
    public static final int YOU_HAVE_ACCEPTED_S1S_CHALLENGE_TO_A_DUEL_THE_DUEL_WILL_BEGIN_IN_A_FEW_MOMENTS = 1930; // Ð’Ñ‹
    // Ð¿Ñ€Ð¸Ð½Ñ�Ð»Ð¸
    // Ð²Ñ‹Ð·Ð¾Ð²
    // Ð½Ð°
    // Ð´ÑƒÑ�Ð»ÑŒ,
    // Ð±Ñ€Ð¾ÑˆÐµÐ½Ð½Ñ‹Ð¹
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶ÐµÐ¼
    // $c1.
    // Ð”ÑƒÑ�Ð»ÑŒ
    // Ñ�ÐµÐ¹Ñ‡Ð°Ñ�
    // Ð½Ð°Ñ‡Ð½ÐµÑ‚Ñ�Ñ�.
    public static final int S1_HAS_DECLINED_YOUR_CHALLENGE_TO_A_DUEL = 1931; // $c1
    // Ð¾Ñ‚ÐºÐ°Ð·Ñ‹Ð²Ð°ÐµÑ‚Ñ�Ñ�
    // Ð¾Ñ‚
    // Ð´ÑƒÑ�Ð»Ð¸
    // Ñ�
    // Ð’Ð°Ð¼Ð¸.
    public static final int YOU_HAVE_ACCEPTED_S1S_CHALLENGE_TO_A_PARTY_DUEL_THE_DUEL_WILL_BEGIN_IN_A_FEW_MOMENTS = 1933; // Ð’Ñ‹
    // Ð¿Ñ€Ð¸Ð½Ñ�Ð»Ð¸
    // Ð²Ñ‹Ð·Ð¾Ð²
    // Ð½Ð°
    // Ð³Ñ€ÑƒÐ¿Ð¿Ð¾Ð²ÑƒÑŽ
    // Ð´ÑƒÑ�Ð»ÑŒ,
    // Ð±Ñ€Ð¾ÑˆÐµÐ½Ð½Ñ‹Ð¹
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶ÐµÐ¼
    // $c1.
    // Ð”ÑƒÑ�Ð»ÑŒ
    // Ñ�ÐµÐ¹Ñ‡Ð°Ñ�
    // Ð½Ð°Ñ‡Ð½ÐµÑ‚Ñ�Ñ�.
    public static final int S1_HAS_ACCEPTED_YOUR_CHALLENGE_TO_DUEL_AGAINST_THEIR_PARTY_THE_DUEL_WILL_BEGIN_IN_A_FEW_MOMENTS = 1934; // $c1
    // Ð¿Ñ€Ð¸Ð½Ð¸Ð¼Ð°ÐµÑ‚
    // Ð²Ñ‹Ð·Ð¾Ð²
    // Ð½Ð°
    // Ð³Ñ€ÑƒÐ¿Ð¿Ð¾Ð²ÑƒÑŽ
    // Ð´ÑƒÑ�Ð»ÑŒ.
    // Ð”ÑƒÑ�Ð»ÑŒ
    // Ñ�ÐµÐ¹Ñ‡Ð°Ñ�
    // Ð½Ð°Ñ‡Ð½ÐµÑ‚Ñ�Ñ�.
    public static final int THE_OPPOSING_PARTY_HAS_DECLINED_YOUR_CHALLENGE_TO_A_DUEL = 1936; // Ð“Ñ€ÑƒÐ¿Ð¿Ð°
    // Ð¿Ñ€Ð¾Ñ‚Ð¸Ð²Ð½Ð¸ÐºÐ°
    // Ð¾Ñ‚Ð²ÐµÑ€Ð³Ð»Ð°
    // Ð’Ð°Ñˆ
    // Ð²Ñ‹Ð·Ð¾Ð²
    // Ð½Ð°
    // Ð´ÑƒÑ�Ð»ÑŒ.
    public static final int SINCE_THE_PERSON_YOU_CHALLENGED_IS_NOT_CURRENTLY_IN_A_PARTY_THEY_CANNOT_DUEL_AGAINST_YOUR_PARTY = 1937; // ÐŸÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶,
    // Ð²Ñ‹Ð·Ð²Ð°Ð½Ð½Ñ‹Ð¹
    // Ð½Ð°
    // Ð´ÑƒÑ�Ð»ÑŒ,
    // Ð½Ðµ
    // Ñ�Ð¾Ñ�Ñ‚Ð¾Ð¸Ñ‚
    // Ð²
    // Ð³Ñ€ÑƒÐ¿Ð¿Ðµ
    // Ð¸
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ñ�Ñ€Ð°Ð¶Ð°Ñ‚ÑŒÑ�Ñ�
    // Ð¿Ñ€Ð¾Ñ‚Ð¸Ð²
    // Ð’Ð°ÑˆÐµÐ¹
    // Ð³Ñ€ÑƒÐ¿Ð¿Ñ‹.
    public static final int S1_HAS_CHALLENGED_YOU_TO_A_DUEL = 1938; // $c1
    // Ð²Ñ‹Ð·Ñ‹Ð²Ð°ÐµÑ‚
    // Ð’Ð°Ñ� Ð½Ð°
    // Ð´ÑƒÑ�Ð»ÑŒ.
    public static final int S1S_PARTY_HAS_CHALLENGED_YOUR_PARTY_TO_A_DUEL = 1939; // Ð“Ñ€ÑƒÐ¿Ð¿Ð°
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°
    // $c1
    // Ð²Ñ‹Ð·Ð²Ð°Ð»Ð°
    // Ð’Ð°Ñ�
    // Ð½Ð°
    // Ð³Ñ€ÑƒÐ¿Ð¿Ð¾Ð²ÑƒÑŽ
    // Ð´ÑƒÑ�Ð»ÑŒ.
    public static final int YOU_ARE_UNABLE_TO_REQUEST_A_DUEL_AT_THIS_TIME = 1940; // Ð’Ñ‹
    // Ñ�ÐµÐ¹Ñ‡Ð°Ñ�
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð²Ñ‹Ð·Ñ‹Ð²Ð°Ñ‚ÑŒ
    // Ð½Ð°
    // Ð´ÑƒÑ�Ð»ÑŒ.
    public static final int THE_OPPOSING_PARTY_IS_CURRENTLY_UNABLE_TO_ACCEPT_A_CHALLENGE_TO_A_DUEL = 1942; // Ð“Ñ€ÑƒÐ¿Ð¿Ð°
    // Ð¿Ñ€Ð¾Ñ‚Ð¸Ð²Ð½Ð¸ÐºÐ°
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ñ�ÐµÐ¹Ñ‡Ð°Ñ�
    // Ð¿Ñ€Ð¸Ð½Ñ�Ñ‚ÑŒ
    // Ð²Ð°Ñˆ
    // Ð²Ñ‹Ð·Ð¾Ð²
    // Ð½Ð°
    // Ð´ÑƒÑ�Ð»ÑŒ.
    public static final int IN_A_MOMENT_YOU_WILL_BE_TRANSPORTED_TO_THE_SITE_WHERE_THE_DUEL_WILL_TAKE_PLACE = 1944; // Ð¡ÐµÐ¹Ñ‡Ð°Ñ�
    // Ð²Ñ‹
    // Ð±ÑƒÐ´ÐµÑ‚Ðµ
    // Ð¿ÐµÑ€ÐµÐ¼ÐµÑ‰ÐµÐ½Ñ‹
    // Ð½Ð°
    // Ð¼ÐµÑ�Ñ‚Ð¾,
    // Ð³Ð´Ðµ
    // Ð¿Ñ€Ð¾Ð¸Ð·Ð¾Ð¹Ð´ÐµÑ‚
    // Ð´ÑƒÑ�Ð»ÑŒ.
    public static final int THE_DUEL_WILL_BEGIN_IN_S1_SECONDS = 1945; // Ð”ÑƒÑ�Ð»ÑŒ
    // Ð½Ð°Ñ‡Ð½ÐµÑ‚Ñ�Ñ�
    // Ñ‡ÐµÑ€ÐµÐ·
    // $s1
    // Ñ�ÐµÐº.
    public static final int LET_THE_DUEL_BEGIN = 1949; // Ð”ÑƒÑ�Ð»ÑŒ Ð½Ð°Ñ‡Ð°Ð»Ð°Ñ�ÑŒ!
    public static final int S1_HAS_WON_THE_DUEL = 1950; // ÐŸÐ¾Ð±ÐµÐ´Ð¸Ñ‚ÐµÐ»ÑŒ Ð´ÑƒÑ�Ð»Ð¸ -
    // $c1.
    public static final int S1S_PARTY_HAS_WON_THE_DUEL = 1951; // ÐŸÐ¾Ð±ÐµÐ´Ð¸Ñ‚ÐµÐ»ÑŒ
    // Ð´ÑƒÑ�Ð»Ð¸ - Ð³Ñ€ÑƒÐ¿Ð¿Ð°
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð° $c1.
    public static final int THE_DUEL_HAS_ENDED_IN_A_TIE = 1952; // Ð”ÑƒÑ�Ð»ÑŒ
    // Ð·Ð°ÐºÐ¾Ð½Ñ‡Ð¸Ð»Ð°Ñ�ÑŒ Ð²
    // Ð½Ð¸Ñ‡ÑŒÑŽ.
    public static final int SINCE_S1_WITHDREW_FROM_THE_DUEL_S2_HAS_WON = 1955; // ÐŸÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶
    // $c1
    // Ð¾Ñ‚Ð¼ÐµÐ½Ð¸Ð»
    // Ð´ÑƒÑ�Ð»ÑŒ.
    // ÐŸÐ¾Ð±ÐµÐ´Ð¸Ñ‚ÐµÐ»ÑŒ
    // -
    // $s2.
    public static final int SINCE_S1S_PARTY_WITHDREW_FROM_THE_DUEL_S1S_PARTY_HAS_WON = 1956; // Ð“Ñ€ÑƒÐ¿Ð¿Ð°
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°
    // $c1
    // Ð¾Ñ‚Ð¼ÐµÐ½Ð¸Ð»Ð°
    // Ð´ÑƒÑ�Ð»ÑŒ.
    // ÐŸÐ¾Ð±ÐµÐ´Ð¸Ñ‚ÐµÐ»ÑŒ
    // -
    // Ð³Ñ€ÑƒÐ¿Ð¿Ð°
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°
    // $s2.
    public static final int S1_CANNOT_DUEL_BECAUSE_S1_IS_CURRENTLY_ENGAGED_IN_A_PRIVATE_STORE_OR_MANUFACTURE = 2017; // $c1
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð²Ñ�Ñ‚ÑƒÐ¿Ð¸Ñ‚ÑŒ
    // Ð²
    // Ð´ÑƒÑ�Ð»ÑŒ,
    // Ð¿Ð¾Ñ�ÐºÐ¾Ð»ÑŒÐºÑƒ
    // Ð²
    // Ð´Ð°Ð½Ð½Ñ‹Ð¹
    // Ð¼Ð¾Ð¼ÐµÐ½Ñ‚
    // Ð½Ð°Ñ…Ð¾Ð´Ð¸Ñ‚Ñ�Ñ�
    // Ð²
    // Ð»Ð¸Ñ‡Ð½Ð¾Ð¹
    // Ñ‚Ð¾Ñ€Ð³Ð¾Ð²Ð¾Ð¹
    // Ð»Ð°Ð²ÐºÐµ
    // Ð¸Ð»Ð¸
    // Ð¼Ð°Ñ�Ñ‚ÐµÑ€Ñ�ÐºÐ¾Ð¹.
    public static final int S1_CANNOT_DUEL_BECAUSE_S1_IS_CURRENTLY_FISHING = 2018; // $c1
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð²Ñ�Ñ‚ÑƒÐ¿Ð¸Ñ‚ÑŒ
    // Ð²
    // Ð´ÑƒÑ�Ð»ÑŒ,
    // Ð¿Ð¾Ñ�ÐºÐ¾Ð»ÑŒÐºÑƒ
    // Ð²
    // Ð´Ð°Ð½Ð½Ñ‹Ð¹
    // Ð¼Ð¾Ð¼ÐµÐ½Ñ‚
    // Ñ€Ñ‹Ð±Ð°Ñ‡Ð¸Ñ‚.
    public static final int S1_CANNOT_DUEL_BECAUSE_S1S_HP_OR_MP_IS_BELOW_50_PERCENT = 2019; // $c1
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð²Ñ�Ñ‚ÑƒÐ¿Ð¸Ñ‚ÑŒ
    // Ð²
    // Ð´ÑƒÑ�Ð»ÑŒ,
    // Ð¿Ð¾Ñ�ÐºÐ¾Ð»ÑŒÐºÑƒ
    // HP
    // Ð¸Ð»Ð¸
    // MP
    // Ð¼ÐµÐ½ÑŒÑˆÐµ
    // 50%.
    public static final int S1_CANNOT_MAKE_A_CHALLANGE_TO_A_DUEL_BECAUSE_S1_IS_CURRENTLY_IN_A_DUEL_PROHIBITED_AREA = 2020; // $c1
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð¿Ñ€Ð¸Ð½Ñ�Ñ‚ÑŒ
    // Ð²Ñ‹Ð·Ð¾Ð²
    // Ð½Ð°
    // Ð´ÑƒÑ�Ð»ÑŒ,
    // Ð¿Ð¾Ñ�ÐºÐ¾Ð»ÑŒÐºÑƒ
    // Ð½Ð°Ñ…Ð¾Ð´Ð¸Ñ‚Ñ�Ñ�
    // Ð²
    // Ð·Ð¾Ð½Ðµ,
    // Ð³Ð´Ðµ
    // Ð´ÑƒÑ�Ð»Ð¸
    // Ð·Ð°Ð¿Ñ€ÐµÑ‰ÐµÐ½Ñ‹.
    public static final int S1_CANNOT_DUEL_BECAUSE_S1_IS_CURRENTLY_ENGAGED_IN_BATTLE = 2021; // $c1
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð²Ñ�Ñ‚ÑƒÐ¿Ð¸Ñ‚ÑŒ
    // Ð²
    // Ð´ÑƒÑ�Ð»ÑŒ,
    // Ð¿Ð¾Ñ�ÐºÐ¾Ð»ÑŒÐºÑƒ
    // Ð½Ð°Ñ…Ð¾Ð´Ð¸Ñ‚Ñ�Ñ�
    // Ð²
    // Ð±Ð¾ÑŽ.
    public static final int S1_CANNOT_DUEL_BECAUSE_S1_IS_ALREADY_ENGAGED_IN_A_DUEL = 2022; // $c1
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð²Ñ�Ñ‚ÑƒÐ¿Ð¸Ñ‚ÑŒ
    // Ð²
    // Ð´ÑƒÑ�Ð»ÑŒ,
    // Ð¿Ð¾Ñ�ÐºÐ¾Ð»ÑŒÐºÑƒ
    // ÑƒÐ¶Ðµ
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð²ÑƒÐµÑ‚
    // Ð²
    // Ð´ÑƒÑ�Ð»Ð¸.
    public static final int S1_CANNOT_DUEL_BECAUSE_S1_IS_IN_A_CHAOTIC_STATE = 2023; // $c1
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð²Ñ�Ñ‚ÑƒÐ¿Ð¸Ñ‚ÑŒ
    // Ð²
    // Ð´ÑƒÑ�Ð»ÑŒ,
    // Ð¿Ð¾Ñ�ÐºÐ¾Ð»ÑŒÐºÑƒ
    // Ð½Ð°Ñ…Ð¾Ð´Ð¸Ñ‚Ñ�Ñ�
    // Ð²
    // Ñ�Ð¾Ñ�Ñ‚Ð¾Ñ�Ð½Ð¸Ð¸
    // Ð¥Ð°Ð¾Ñ�Ð°.
    public static final int S1_CANNOT_DUEL_BECAUSE_S1_IS_PARTICIPATING_IN_THE_OLYMPIAD = 2024; // $c1
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð²Ñ�Ñ‚ÑƒÐ¿Ð¸Ñ‚ÑŒ
    // Ð²
    // Ð´ÑƒÑ�Ð»ÑŒ,
    // Ð¿Ð¾Ñ�ÐºÐ¾Ð»ÑŒÐºÑƒ
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð²ÑƒÐµÑ‚
    // Ð²
    // ÐžÐ»Ð¸Ð¼Ð¿Ð¸Ð°Ð´Ðµ.
    public static final int S1_CANNOT_DUEL_BECAUSE_S1_IS_PARTICIPATING_IN_A_CLAN_HALL_WAR = 2025; // $c1
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð²Ñ�Ñ‚ÑƒÐ¿Ð¸Ñ‚ÑŒ
    // Ð²
    // Ð´ÑƒÑ�Ð»ÑŒ,
    // Ð¿Ð¾Ñ�ÐºÐ¾Ð»ÑŒÐºÑƒ
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð²ÑƒÐµÑ‚
    // Ð²
    // Ð²Ð¾Ð¹Ð½Ðµ
    // Ð·Ð°
    // Ñ…Ð¾Ð»Ð»
    // ÐºÐ»Ð°Ð½Ð°.
    public static final int S1_CANNOT_DUEL_BECAUSE_S1_IS_PARTICIPATING_IN_A_SIEGE_WAR = 2026; // $c1
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð²Ñ�Ñ‚ÑƒÐ¿Ð¸Ñ‚ÑŒ
    // Ð²
    // Ð´ÑƒÑ�Ð»ÑŒ,
    // Ð¿Ð¾Ñ�ÐºÐ¾Ð»ÑŒÐºÑƒ
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð²ÑƒÐµÑ‚
    // Ð²
    // Ð¾Ñ�Ð°Ð´Ðµ.
    public static final int S1_CANNOT_DUEL_BECAUSE_S1_IS_CURRENTLY_RIDING_A_BOAT_WYVERN_OR_STRIDER = 2027; // $c1
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð²Ñ�Ñ‚ÑƒÐ¿Ð¸Ñ‚ÑŒ
    // Ð²
    // Ð´ÑƒÑ�Ð»ÑŒ,
    // Ð¿Ð¾Ñ�ÐºÐ¾Ð»ÑŒÐºÑƒ
    // Ð²
    // Ð´Ð°Ð½Ð½Ñ‹Ð¹
    // Ð¼Ð¾Ð¼ÐµÐ½Ñ‚
    // Ð½Ð°Ñ…Ð¾Ð´Ð¸Ñ‚Ñ�Ñ�
    // Ð²
    // Ð»Ð¸Ñ‡Ð½Ð¾Ð¹
    // Ñ‚Ð¾Ñ€Ð³Ð¾Ð²Ð¾Ð¹
    // Ð»Ð°Ð²ÐºÐµ
    // Ð¸Ð»Ð¸
    // Ð¼Ð°Ñ�Ñ‚ÐµÑ€Ñ�ÐºÐ¾Ð¹.
    public static final int S1_CANNOT_RECEIVE_A_DUEL_CHALLENGE_BECAUSE_S1_IS_TOO_FAR_AWAY = 2028; // $c1
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð¿Ñ€Ð¸Ð½Ñ�Ñ‚ÑŒ
    // Ð²Ñ‹Ð·Ð¾Ð²
    // Ð½Ð°
    // Ð´ÑƒÑ�Ð»ÑŒ,
    // Ð¿Ð¾Ñ‚Ð¾Ð¼Ñƒ
    // Ñ‡Ñ‚Ð¾
    // Ð½Ð°Ñ…Ð¾Ð´Ð¸Ñ‚Ñ�Ñ�
    // Ñ�Ð»Ð¸ÑˆÐºÐ¾Ð¼
    // Ð´Ð°Ð»ÐµÐºÐ¾.
    public static final int YOU_CANNOT_ADD_ELEMENTAL_POWER_WHILE_OPERATING_A_PRIVATE_STORE_OR_PRIVATE_WORKSHOP = 2143; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð´Ð¾Ð±Ð°Ð²Ð¸Ñ‚ÑŒ
    // Ñ�Ð¸Ð»Ñƒ
    // Ñ�Ñ‚Ð¸Ñ…Ð¸Ð¹,
    // Ð¿Ð¾ÐºÐ°
    // Ð½Ð°Ñ…Ð¾Ð´Ð¸Ñ‚ÐµÑ�ÑŒ
    // Ð²
    // Ð»Ð¸Ñ‡Ð½Ð¾Ð¹
    // Ñ‚Ð¾Ñ€Ð³Ð¾Ð²Ð¾Ð¹
    // Ð»Ð°Ð²ÐºÐµ
    // Ð¸Ð»Ð¸
    // Ð¼Ð°Ñ�Ñ‚ÐµÑ€Ñ�ÐºÐ¾Ð¹.
    public static final int PLEASE_SELECT_ITEM_TO_ADD_ELEMENTAL_POWER = 2144; // ÐŸÐ¾Ð¶Ð°Ð»ÑƒÐ¹Ñ�Ñ‚Ð°,
    // Ð²Ñ‹Ð±ÐµÑ€Ð¸Ñ‚Ðµ
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚,
    // Ðº
    // ÐºÐ¾Ñ‚Ð¾Ñ€Ð¾Ð¼Ñƒ
    // Ð²Ñ‹
    // Ñ…Ð¾Ñ‚Ð¸Ñ‚Ðµ
    // Ð´Ð¾Ð±Ð°Ð²Ð¸Ñ‚ÑŒ
    // Ñ�Ð¸Ð»Ñƒ
    // Ñ�Ñ‚Ð¸Ñ…Ð¸Ð¹.
    public static final int ELEMENTAL_POWER_ENCHANCER_USAGE_HAS_BEEN_CANCELLED = 2145; // Ð˜Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ð½Ð¸Ðµ
    // ÑƒÑ�Ð¸Ð»Ð¸Ñ‚ÐµÐ»Ñ�
    // Ñ�Ñ‚Ð¸Ñ…Ð¸Ð¹
    // Ð±Ñ‹Ð»Ð¾
    // Ð¾Ñ‚Ð¼ÐµÐ½ÐµÐ½Ð¾.
    public static final int ELEMENTAL_POWER_ENCHANCER_USAGE_REQUIREMENT_IS_NOT_SUFFICIENT = 2146; // Ð£Ñ�Ð»Ð¾Ð²Ð¸Ñ�
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ð½Ð¸Ñ�
    // ÑƒÑ�Ð¸Ð»Ð¸Ñ‚ÐµÐ»Ñ�
    // Ñ�Ñ‚Ð¸Ñ…Ð¸Ð¹
    // Ð½Ðµ
    // Ñ�Ð¾Ð±Ð»ÑŽÐ´ÐµÐ½Ñ‹.
    public static final int S2_ELEMENTAL_POWER_HAS_BEEN_ADDED_SUCCESSFULLY_TO_S1 = 2147; // $s1:
    // Ñ�Ð¸Ð»Ð°
    // Ñ�Ñ‚Ð¸Ñ…Ð¸Ð¸
    // $s2
    // Ð°ÐºÑ‚Ð¸Ð²Ð¸Ñ€Ð¾Ð²Ð°Ð½Ð°.
    public static final int S3_ELEMENTAL_POWER_HAS_BEEN_ADDED_SUCCESSFULLY_TO__S1S2 = 2148; // +$s1
    // $s2:
    // Ñ�Ð¸Ð»Ð°
    // Ñ�Ñ‚Ð¸Ñ…Ð¸Ð¸
    // $s3
    // Ð°ÐºÑ‚Ð¸Ð²Ð¸Ñ€Ð¾Ð²Ð°Ð½Ð°.
    public static final int YOU_HAVE_FAILED_TO_ADD_ELEMENTAL_POWER = 2149; // Ð”Ð¾Ð±Ð°Ð²Ð»ÐµÐ½Ð¸Ðµ
    // Ñ�Ð¸Ð»Ñ‹
    // Ñ�Ñ‚Ð¸Ñ…Ð¸Ð¹
    // Ð½Ðµ
    // ÑƒÐ´Ð°Ð»Ð¾Ñ�ÑŒ.
    public static final int ANOTHER_ELEMENTAL_POWER_HAS_ALREADY_BEEN_ADDED_THIS_ELEMENTAL_POWER_CANNOT_BE_ADDED = 2150; // Ð”Ñ€ÑƒÐ³Ð°Ñ�
    // Ñ�Ð¸Ð»Ð°
    // Ñ�Ñ‚Ð¸Ñ…Ð¸Ð¸
    // ÑƒÐ¶Ðµ
    // Ð±Ñ‹Ð»Ð°
    // Ð´Ð¾Ð±Ð°Ð²Ð»ÐµÐ½Ð°.
    // Ð­Ñ‚Ð°
    // Ñ�Ð¸Ð»Ð°
    // Ñ�Ñ‚Ð¸Ñ…Ð¸Ð¸
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð´Ð¾Ð±Ð°Ð²Ð»ÐµÐ½Ð°.
    public static final int A_PARTY_CANNOT_BE_FORMED_IN_THIS_AREA = 2388; // Ð—Ð´ÐµÑ�ÑŒ
    // Ð½ÐµÐ»ÑŒÐ·Ñ�
    // Ñ�Ð¾Ð·Ð´Ð°Ñ‚ÑŒ
    // Ð³Ñ€ÑƒÐ¿Ð¿Ñƒ.

    public static final int A_MALICIOUS_SKILL_CANNOT_BE_USED_IN_A_PEACE_ZONE = 2167; // Ð’Ñ€ÐµÐ´Ð¾Ð½Ð¾Ñ�Ð½Ð¾Ðµ
    // ÑƒÐ¼ÐµÐ½Ð¸Ðµ
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ð½Ð¾
    // Ð²
    // Ð¼Ð¸Ñ€Ð½Ð¾Ð¹
    // Ð·Ð¾Ð½Ðµ.
    public static final int C1_CANNOT_DUEL_BECAUSE_C1_IS_CURRENTLY_POLYMORPHED = 2174; // $c1
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð²Ð¾Ð²Ð°Ñ‚ÑŒ
    // Ð²
    // Ð´ÑƒÑ�Ð»Ð¸,
    // Ð¿Ð¾Ñ�ÐºÐ¾Ð»ÑŒÐºÑƒ
    // Ð¾Ð½
    // Ð¸Ð·Ð¼ÐµÐ½Ð¸Ð»
    // Ñ„Ð¾Ñ€Ð¼Ñƒ.
    public static final int PARTY_DUEL_CANNOT_BE_INITIATED_DUEL_TO_A_POLYMORPHED_PARTY_MEMBER = 2175; // Ð“Ñ€ÑƒÐ¿Ð¿Ð¾Ð²Ð°Ñ�
    // Ð´ÑƒÑ�Ð»ÑŒ
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ñ�Ð¾Ñ�Ñ‚Ð¾Ñ�Ñ‚ÑŒÑ�Ñ�
    // Ð¸Ð·-Ð·Ð°
    // Ð¸Ð·Ð¼ÐµÐ½ÐµÐ½Ð½Ð¾Ð¹
    // Ñ„Ð¾Ñ€Ð¼Ñ‹
    // Ð¾Ð´Ð½Ð¾Ð³Ð¾
    // Ð¸Ð·
    // Ñ‡Ð»ÐµÐ½Ð¾Ð²
    // Ð³Ñ€ÑƒÐ¿Ð¿Ñ‹.
    public static final int HALF_KILL = 2336; // Ð’Ð°Ñˆ Ð²Ñ€Ð°Ð³ Ð½Ð°Ð¿Ð¾Ð»Ð¾Ð²Ð¸Ð½Ñƒ Ð¼ÐµÑ€Ñ‚Ð²!
    public static final int CP_DISAPPEARS_WHEN_HIT_WITH_A_HALF_KILL_SKILL = 2337; // CP
    // ÑƒÐ½Ð¸Ñ‡Ñ‚Ð¾Ð¶ÐµÐ½Ñ‹
    // Ñ�Ð¼ÐµÑ€Ñ‚ÐµÐ»ÑŒÐ½Ñ‹Ð¼
    // ÑƒÐ¼ÐµÐ½Ð¸ÐµÐ¼,
    // ÑƒÑ�Ð¿ÐµÑˆÐ½Ñ‹Ð¼
    // Ð½Ð°Ð¿Ð¾Ð»Ð¾Ð²Ð¸Ð½Ñƒ.

    public static final int NOT_ENOUGH_BOLTS = 2226; // Ð�ÐµÐ´Ð¾Ñ�Ñ‚Ð°Ñ‚Ð¾Ñ‡Ð½Ð¾ Ð±Ð¾Ð»Ñ‚Ð¾Ð².

    // ClanHall Auction messages
    public static final int ONLY_A_CLAN_LEADER_WHOSE_CLAN_IS_OF_LEVEL_2_OR_HIGHER_IS_ALLOWED_TO_PARTICIPATE_IN_A_CLAN_HALL_AUCTION = 673; // Ð£Ñ‡Ð°Ñ�Ñ‚Ð²Ð¾Ð²Ð°Ñ‚ÑŒ
    // Ð²
    // Ð°ÑƒÐºÑ†Ð¸Ð¾Ð½Ðµ
    // Ñ…Ð¾Ð»Ð»Ð¾Ð²
    // ÐºÐ»Ð°Ð½Ð¾Ð²
    // Ð¼Ð¾Ð³ÑƒÑ‚
    // Ñ‚Ð¾Ð»ÑŒÐºÐ¾
    // ÐºÐ»Ð°Ð½Ñ‹
    // 2-Ð³Ð¾
    // ÑƒÑ€Ð¾Ð²Ð½Ñ�
    // Ð¸
    // Ð²Ñ‹ÑˆÐµ.
    public static final int IT_HAS_NOT_YET_BEEN_SEVEN_DAYS_SINCE_CANCELING_AN_AUCTION = 674; // Ð�Ðµ
    // Ð¿Ñ€Ð¾ÑˆÐ»Ð¾
    // 7
    // Ð´Ð½ÐµÐ¹
    // Ñ�Ð¾
    // Ð´Ð½Ñ�
    // Ð¾Ñ‚Ð¼ÐµÐ½Ñ‹
    // Ð°ÑƒÐºÑ†Ð¸Ð¾Ð½Ð°.
    public static final int THERE_ARE_NO_CLAN_HALLS_UP_FOR_AUCTION = 675; // Ð”Ð°Ð½Ð½Ñ‹Ð¹
    // Ñ…Ð¾Ð»Ð»
    // ÐºÐ»Ð°Ð½Ð°
    // Ð½Ðµ
    // Ð±ÑƒÐ´ÐµÑ‚
    // Ð²Ñ‹Ñ�Ñ‚Ð°Ð²Ð»ÐµÐ½
    // Ð½Ð°
    // Ð°ÑƒÐºÑ†Ð¸Ð¾Ð½Ðµ.
    public static final int SINCE_YOU_HAVE_ALREADY_SUBMITTED_A_BID_YOU_ARE_NOT_ALLOWED_TO_PARTICIPATE_IN_ANOTHER_AUCTION_AT_THIS_TIME = 676; // Ð’Ñ‹
    // ÑƒÐ¶Ðµ
    // Ð²Ð½ÐµÑ�Ð»Ð¸
    // Ñ�Ð²Ð¾ÑŽ
    // Ñ�Ñ‚Ð°Ð²ÐºÑƒ,
    // Ð¿Ð¾Ñ�Ñ‚Ð¾Ð¼Ñƒ
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð¸Ðµ
    // Ð²
    // Ð´Ñ€ÑƒÐ³Ð¾Ð¼
    // Ð°ÑƒÐºÑ†Ð¸Ð¾Ð½Ðµ
    // Ð½ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾.
    public static final int YOUR_BID_PRICE_MUST_BE_HIGHER_THAN_THE_MINIMUM_PRICE_THAT_CAN_BE_BID = 677; // Ð¡Ñ‚Ð°Ð²ÐºÐ°
    // Ð´Ð¾Ð»Ð¶Ð½Ð°
    // Ð¿Ñ€ÐµÐ²Ñ‹ÑˆÐ°Ñ‚ÑŒ
    // Ð¼Ð¸Ð½Ð¸Ð¼Ð°Ð»ÑŒÐ½Ð¾
    // Ð²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ñ‹Ð¹
    // Ñ€Ð°Ð·Ð¼ÐµÑ€
    // Ð¿Ð»Ð°Ñ‚ÐµÐ¶Ð°.
    public static final int YOU_HAVE_SUBMITTED_A_BID_IN_THE_AUCTION_OF_S1 = 678; // Ð’Ñ‹
    // Ñ�Ð´ÐµÐ»Ð°Ð»Ð¸
    // Ñ�Ñ‚Ð°Ð²ÐºÑƒ
    // Ð²
    // Ð°ÑƒÐºÑ†Ð¸Ð¾Ð½Ðµ
    // $s1.
    public static final int YOU_HAVE_CANCELED_YOUR_BID = 679; // Ð¡Ñ‚Ð°Ð²ÐºÐ°
    // Ð¾Ñ‚Ð¼ÐµÐ½ÐµÐ½Ð°.
    public static final int YOU_CANNOT_PARTICIPATE_IN_AN_AUCTION = 680; // Ð’Ñ‹ Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð²Ð¾Ð²Ð°Ñ‚ÑŒ
    // Ð²
    // Ð°ÑƒÐºÑ†Ð¸Ð¾Ð½Ðµ.
    public static final int THE_CLAN_DOES_NOT_OWN_A_CLAN_HALL = 681; // Ð’Ð°Ñˆ ÐºÐ»Ð°Ð½
    // Ð½Ðµ
    // Ð²Ð»Ð°Ð´ÐµÐµÑ‚
    // Ñ…Ð¾Ð»Ð»Ð¾Ð¼.
    public static final int THE_CLAN_HALL_WHICH_WAS_PUT_UP_FOR_AUCTION_HAS_BEEN_AWARDED_TO_S1_CLAN = 776; // Ð¥Ð¾Ð»Ð»,
    // Ð²Ñ‹Ñ�Ñ‚Ð°Ð²Ð»ÐµÐ½Ð½Ñ‹Ð¹
    // Ð½Ð°
    // Ð°ÑƒÐºÑ†Ð¸Ð¾Ð½Ðµ,
    // Ð¿ÐµÑ€ÐµÑˆÐµÐ»
    // Ð²
    // Ñ�Ð¾Ð±Ñ�Ñ‚Ð²ÐµÐ½Ð½Ð¾Ñ�Ñ‚ÑŒ
    // ÐºÐ»Ð°Ð½Ð°
    // $s1.
    public static final int THE_CLAN_HALL_WHICH_HAD_BEEN_PUT_UP_FOR_AUCTION_WAS_NOT_SOLD_AND_THEREFORE_HAS_BEEN_RELISTED = 777; // Ð¥Ð¾Ð»Ð»,
    // Ð²Ñ‹Ñ�Ñ‚Ð°Ð²Ð»ÐµÐ½Ð½Ñ‹Ð¹
    // Ð½Ð°
    // Ð°ÑƒÐºÑ†Ð¸Ð¾Ð½Ðµ,
    // Ð½Ðµ
    // Ð±Ñ‹Ð»
    // Ð¿Ñ€Ð¾Ð´Ð°Ð½
    // Ð¸
    // Ð²Ð¾Ð·Ð²Ñ€Ð°Ñ‰Ð°ÐµÑ‚Ñ�Ñ�
    // Ð²
    // Ñ�Ð¿Ð¸Ñ�Ð¾Ðº
    // Ñ…Ð¾Ð»Ð»Ð¾Ð²,
    // Ð²Ñ‹Ñ�Ñ‚Ð°Ð²Ð»ÐµÐ½Ð½Ñ‹Ñ…
    // Ð½Ð°
    // Ð¿Ñ€Ð¾Ð´Ð°Ð¶Ñƒ.
    public static final int YOU_HAVE_REGISTERED_FOR_A_CLAN_HALL_AUCTION = 1004; // Ð’Ñ‹
    // Ð´Ð¾Ð±Ð°Ð²Ð»ÐµÐ½Ñ‹
    // Ð²
    // Ñ�Ð¿Ð¸Ñ�Ð¾Ðº
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð½Ð¸ÐºÐ¾Ð²
    // Ð²
    // Ð°ÑƒÐºÑ†Ð¸Ð¾Ð½Ðµ
    // Ñ…Ð¾Ð»Ð»Ð¾Ð²
    // ÐºÐ»Ð°Ð½Ð¾Ð².
    public static final int THERE_IS_NOT_ENOUGH_ADENA_IN_THE_CLAN_HALL_WAREHOUSE = 1005; // Ð�Ðµ
    // Ñ…Ð²Ð°Ñ‚Ð°ÐµÑ‚
    // Ð°Ð´ÐµÐ½
    // Ð²
    // Ñ…Ñ€Ð°Ð½Ð¸Ð»Ð¸Ñ‰Ðµ
    // ÐºÐ»Ð°Ð½Ð°.
    public static final int YOU_HAVE_BID_IN_A_CLAN_HALL_AUCTION = 1006; // Ð’Ñ‹
    // Ñ�Ð´ÐµÐ»Ð°Ð»Ð¸
    // Ñ�Ð²Ð¾ÑŽ
    // Ñ�Ñ‚Ð°Ð²ÐºÑƒ
    // Ð²
    // Ð°ÑƒÐºÑ†Ð¸Ð¾Ð½Ðµ
    // Ñ…Ð¾Ð»Ð»Ð¾Ð²
    // ÐºÐ»Ð°Ð½Ð¾Ð².
    public static final int THE_SECOND_BID_AMOUNT_MUST_BE_HIGHER_THAN_THE_ORIGINAL = 1075; // Ð¡ÑƒÐ¼Ð¼Ð°
    // Ð´Ð¾Ð»Ð¶Ð½Ð°
    // Ð¿Ñ€ÐµÐ²Ñ‹Ñ�Ð¸Ñ‚ÑŒ
    // Ð¿Ñ€ÐµÐ´Ñ‹Ð´ÑƒÑ‰ÑƒÑŽ
    // Ñ�Ñ‚Ð°Ð²ÐºÑƒ.
    public static final int IT_IS_NOT_AN_AUCTION_PERIOD = 2075; // Ð�ÑƒÐºÑ†Ð¸Ð¾Ð½
    // Ñ�ÐµÐ¹Ñ‡Ð°Ñ� Ð½Ðµ
    // Ð¿Ñ€Ð¾Ð²Ð¾Ð´Ð¸Ñ‚Ñ�Ñ�.
    public static final int YOUR_BID_CANNOT_EXCEED_100_BILLION = 2076; // Ð’Ð°ÑˆÐ°
    // Ñ�Ñ‚Ð°Ð²ÐºÐ°
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð¿Ñ€ÐµÐ²Ñ‹ÑˆÐ°Ñ‚ÑŒ
    // 2,1
    // Ð¼Ð¸Ð»Ð»Ð¸Ð°Ñ€Ð´Ð°.
    public static final int YOUR_BID_MUST_BE_HIGHER_THAN_THE_CURRENT_HIGHEST_BID = 2077; // Ð’Ð°ÑˆÐ°
    // Ñ�Ñ‚Ð°Ð²ÐºÐ°
    // Ð´Ð¾Ð»Ð¶Ð½Ð°
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð²Ñ‹ÑˆÐµ
    // Ñ‚ÐµÐºÑƒÑ‰ÐµÐ¹
    // Ñ�Ñ‚Ð°Ð²ÐºÐ¸.
    public static final int YOU_DO_NOT_HAVE_ENOUGH_ADENA_FOR_THIS_BID = 2078; // Ð�ÐµÐ´Ð¾Ñ�Ñ‚Ð°Ñ‚Ð¾Ñ‡Ð½Ð¾
    // Ð°Ð´ÐµÐ½
    // Ð´Ð»Ñ�
    // Ñ�Ñ‚Ð¾Ð¹
    // Ñ�Ñ‚Ð°Ð²ÐºÐ¸.
    public static final int YOU_CURRENTLY_HAVE_THE_HIGHEST_BID = 2079; // Ð£ Ð’Ð°Ñ�
    // Ñ�Ð°Ð¼Ð°Ñ�
    // Ð²Ñ‹Ñ�Ð¾ÐºÐ°Ñ�
    // Ñ�Ñ‚Ð°Ð²ÐºÐ°.
    public static final int YOU_HAVE_BEEN_OUTBID = 2080; // Ð’Ð°ÑˆÐ° Ñ�Ñ‚Ð°Ð²ÐºÐ° Ð±Ñ‹Ð»Ð°
    // Ð¿ÐµÑ€ÐµÐ±Ð¸Ñ‚Ð°.
    public static final int THE_AUCTION_HAS_BEGUN = 2083; // Ð�ÑƒÐºÑ†Ð¸Ð¾Ð½ Ð½Ð°Ñ‡Ð°Ð»Ñ�Ñ�.
    public static final int BIDDER_EXISTS__THE_AUCTION_TIME_HAS_BEEN_EXTENDED_BY_5_MINUTES = 2159; // Ð�Ð¾Ð²Ð°Ñ�
    // Ñ�Ñ‚Ð°Ð²ÐºÐ°!
    // Ð�ÑƒÐºÑ†Ð¸Ð¾Ð½
    // Ð¿Ñ€Ð¾Ð´Ð»ÐµÐ½
    // Ð½Ð°
    // 5
    // Ð¼Ð¸Ð½ÑƒÑ‚.
    public static final int BIDDER_EXISTS__AUCTION_TIME_HAS_BEEN_EXTENDED_BY_3_MINUTES = 2160; // Ð�Ð¾Ð²Ð°Ñ�
    // Ñ�Ñ‚Ð°Ð²ÐºÐ°!
    // Ð�ÑƒÐºÑ†Ð¸Ð¾Ð½
    // Ð¿Ñ€Ð¾Ð´Ð»ÐµÐ½
    // Ð½Ð°
    // 3
    // Ð¼Ð¸Ð½ÑƒÑ‚Ñ‹.
    public static final int TRADE_S1_S2_S_AUCTION_HAS_ENDED = 2172; // +$s1 $s2:
    // Ð°ÑƒÐºÑ†Ð¸Ð¾Ð½
    // Ð·Ð°ÐºÐ¾Ð½Ñ‡ÐµÐ½.
    public static final int S1_S_AUCTION_HAS_ENDED = 2173; // $s1: Ð°ÑƒÐºÑ†Ð¸Ð¾Ð½
    // Ð·Ð°ÐºÐ¾Ð½Ñ‡ÐµÐ½.

    // Combat messages
    public static final int C1S_IS_PERFORMING_A_COUNTERATTACK = 1997; // $c1
    // Ð¿Ñ€Ð¾Ð²Ð¾Ð´Ð¸Ñ‚
    // ÐºÐ¾Ð½Ñ‚Ñ€Ð°Ñ‚Ð°ÐºÑƒ.
    public static final int YOU_COUNTERED_C1S_ATTACK = 1998; // Ð’Ñ‹
    // ÐºÐ¾Ð½Ñ‚Ñ€Ð°Ñ‚Ð°ÐºÐ¾Ð²Ð°Ð»Ð¸
    // Ñ†ÐµÐ»ÑŒ $c1.
    public static final int C1_DODGES_THE_ATTACK = 1999; // $c1 ÑƒÐ²Ð¾Ñ€Ð°Ñ‡Ð¸Ð²Ð°ÐµÑ‚Ñ�Ñ� Ð¾Ñ‚
    // Ð°Ñ‚Ð°ÐºÐ¸.
    public static final int C1_HAS_GIVEN_C2_DAMAGE_OF_S3 = 2261; // $c1 Ð½Ð°Ð½Ð¾Ñ�Ð¸Ñ‚
    // Ñ†ÐµÐ»Ð¸ $c2 $s3
    // ÑƒÑ€Ð¾Ð½Ð°.
    public static final int C1_HAS_RECEIVED_DAMAGE_OF_S3_FROM_C2 = 2262; // $c1
    // Ð¿Ð¾Ð»ÑƒÑ‡Ð°ÐµÑ‚
    // Ð¾Ñ‚
    // Ñ†ÐµÐ»Ð¸
    // $c2
    // $s3
    // ÑƒÑ€Ð¾Ð½Ð°.
    public static final int C1_HAS_RECEIVED_DAMAGE_OF_S3_THROUGH_C2 = 2263; // $c1
    // Ð¿Ð¾Ð»ÑƒÑ‡Ð°ÐµÑ‚
    // $s3
    // ÑƒÑ€Ð¾Ð½Ð°
    // ($c2).
    public static final int C1_HAS_EVADED_C2S_ATTACK = 2264; // $c1 ÑƒÐºÐ»Ð¾Ð½Ñ�ÐµÑ‚Ñ�Ñ�
    // Ð¾Ñ‚ Ð°Ñ‚Ð°ÐºÐ¸ Ñ†ÐµÐ»Ð¸
    // $c2.
    public static final int C1S_ATTACK_WENT_ASTRAY = 2265; // $c1 Ð½Ð°Ð½Ð¾Ñ�Ð¸Ñ‚ ÑƒÐ´Ð°Ñ€
    // Ð¼Ð¸Ð¼Ð¾ Ñ†ÐµÐ»Ð¸.
    public static final int C1_HAD_A_CRITICAL_HIT = 2266; // $c1 Ð½Ð°Ð½Ð¾Ñ�Ð¸Ñ‚
    // ÐºÑ€Ð¸Ñ‚Ð¸Ñ‡ÐµÑ�ÐºÐ¸Ð¹ ÑƒÐ´Ð°Ñ€!
    public static final int C1_RESISTED_C2S_DRAIN = 2267; // $c1 Ð¾Ñ‚Ñ€Ð°Ð¶Ð°ÐµÑ‚
    // ÐŸÐ¾Ð³Ð»Ð¾Ñ‰ÐµÐ½Ð¸Ðµ Ñ†ÐµÐ»Ð¸
    // $c2.
    public static final int C1S_ATTACK_FAILED = 2268; // $c1: Ð°Ñ‚Ð°ÐºÐ° Ð½Ðµ ÑƒÐ´Ð°Ð»Ð°Ñ�ÑŒ.
    public static final int C1_RESISTED_C2S_MAGIC = 2269; // $c1 Ð¾Ñ‚Ñ€Ð°Ð¶Ð°ÐµÑ‚ Ð¼Ð°Ð³Ð¸ÑŽ
    // Ð²Ñ€Ð°Ð³Ð° $c2.
    public static final int C1_HAS_RECEIVED_DAMAGE_FROM_S2_THROUGH_THE_FIRE_OF_MAGIC = 2270; // $c1
    // Ð¿Ð¾Ð»ÑƒÑ‡Ð°ÐµÑ‚
    // ÑƒÑ€Ð¾Ð½
    // Ð¾Ñ‚
    // Ð¼Ð°Ð³Ð¸Ð¸
    // Ð²Ñ€Ð°Ð³Ð°
    // $s2.
    public static final int C1_WEAKLY_RESISTED_C2S_MAGIC = 2271; // $c1 Ñ�Ð»Ð°Ð±Ð¾
    // Ñ�Ð¾Ð¿Ñ€Ð¾Ñ‚Ð¸Ð²Ð»Ñ�ÐµÑ‚Ñ�Ñ�
    // Ð¼Ð°Ð³Ð¸Ð¸ Ð²Ñ€Ð°Ð³Ð°
    // $c2.
    public static final int DAMAGE_IS_DECREASED_BECAUSE_C1_RESISTED_AGAINST_C2S_MAGIC = 2280; // $c1
    // Ñ�Ð¾Ð¿Ñ€Ð¾Ñ‚Ð¸Ð²Ð»Ñ�ÐµÑ‚Ñ�Ñ�
    // Ð¼Ð°Ð³Ð¸Ð¸
    // Ð²Ñ€Ð°Ð³Ð°
    // $c2,
    // ÑƒÑ€Ð¾Ð½
    // ÑƒÐ¼ÐµÐ½ÑŒÑˆÐµÐ½.

    public static final int THERE_ARE_S2_SECONDS_REMAINING_IN_S1S_REUSE_TIME = 2303; // $s1:
    // Ð´Ð¾
    // Ð¿Ð¾Ð²Ñ‚Ð¾Ñ€Ð½Ð¾Ð³Ð¾
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ð½Ð¸Ñ�
    // $s2
    // Ñ�ÐµÐº.
    public static final int THERE_ARE_S2_MINUTES_S3_SECONDS_REMAINING_IN_S1S_REUSE_TIME = 2304; // $s1:
    // Ð´Ð¾
    // Ð¿Ð¾Ð²Ñ‚Ð¾Ñ€Ð½Ð¾Ð³Ð¾
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ð½Ð¸Ñ�
    // $s2
    // Ð¼Ð¸Ð½
    // $s3
    // Ñ�ÐµÐº.
    public static final int THERE_ARE_S2_HOURS_S3_MINUTES_AND_S4_SECONDS_REMAINING_IN_S1S_REUSE_TIME = 2305; // $s1:
    // Ð´Ð¾
    // Ð¿Ð¾Ð²Ñ‚Ð¾Ñ€Ð½Ð¾Ð³Ð¾
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ð½Ð¸Ñ�
    // $s2
    // Ñ‡
    // $s3
    // Ð¼Ð¸Ð½
    // $s4
    // Ñ�ÐµÐº.

    // Augmentation
    public static final int SELECT_THE_ITEM_TO_BE_AUGMENTED = 1957; // Ð’Ñ‹Ð±ÐµÑ€Ð¸Ñ‚Ðµ
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚
    // Ð´Ð»Ñ�
    // Ð·Ð°Ñ‡Ð°Ñ€Ð¾Ð²Ð°Ð½Ð¸Ñ�.
    public static final int SELECT_THE_CATALYST_FOR_AUGMENTATION = 1958; // Ð’Ñ‹Ð±ÐµÑ€Ð¸Ñ‚Ðµ
    // ÐºÐ°Ñ‚Ð°Ð»Ð¸Ð·Ð°Ñ‚Ð¾Ñ€
    // Ð´Ð»Ñ�
    // Ð·Ð°Ñ‡Ð°Ñ€Ð¾Ð²Ð°Ð½Ð¸Ñ�.
    public static final int REQUIRES_S1_S2 = 1959; // Ð¢Ñ€ÐµÐ±ÑƒÐµÑ‚Ñ�Ñ�: $s2 $s1.
    public static final int THIS_IS_NOT_A_SUITABLE_ITEM = 1960; // Ð­Ñ‚Ð¾
    // Ð½ÐµÐ¿Ð¾Ð´Ñ…Ð¾Ð´Ñ�Ñ‰Ð¸Ð¹
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚.
    public static final int GEMSTONE_QUANTITY_IS_INCORRECT = 1961; // ÐŸÐ°Ñ€Ð°Ð¼ÐµÑ‚Ñ€Ñ‹
    // Ñ�Ð°Ð¼Ð¾Ñ†Ð²ÐµÑ‚Ð°
    // Ð½Ðµ
    // Ð¿Ð¾Ð´Ñ…Ð¾Ð´Ñ�Ñ‚.
    public static final int THE_ITEM_WAS_SUCCESSFULLY_AUGMENTED = 1962; // ÐŸÑ€ÐµÐ´Ð¼ÐµÑ‚
    // ÑƒÑ�Ð¿ÐµÑˆÐ½Ð¾
    // Ð·Ð°Ñ‡Ð°Ñ€Ð¾Ð²Ð°Ð½!
    public static final int SELECT_THE_ITEM_FROM_WHICH_YOU_WISH_TO_REMOVE_AUGMENTATION = 1963; // Ð’Ñ‹Ð±ÐµÑ€Ð¸Ñ‚Ðµ
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚,
    // Ñ�
    // ÐºÐ¾Ñ‚Ð¾Ñ€Ð¾Ð³Ð¾
    // Ð²Ñ‹
    // Ñ…Ð¾Ñ‚Ð¸Ñ‚Ðµ
    // Ñ�Ð½Ñ�Ñ‚ÑŒ
    // Ð·Ð°Ñ‡Ð°Ñ€Ð¾Ð²Ð°Ð½Ð¸Ðµ.
    public static final int AUGMENTATION_REMOVAL_CAN_ONLY_BE_DONE_ON_AN_AUGMENTED_ITEM = 1964; // Ð¡Ð½Ñ�Ñ‚ÑŒ
    // Ð·Ð°Ñ‡Ð°Ñ€Ð¾Ð²Ð°Ð½Ð¸Ðµ
    // Ð¼Ð¾Ð¶Ð½Ð¾
    // Ñ‚Ð¾Ð»ÑŒÐºÐ¾
    // Ñ�
    // Ð·Ð°Ñ‡Ð°Ñ€Ð¾Ð²Ð°Ð½Ð½Ð¾Ð³Ð¾
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ð°.
    public static final int AUGMENTATION_HAS_BEEN_SUCCESSFULLY_REMOVED_FROM_YOUR_S1 = 1965; // Ð—Ð°Ñ‡Ð°Ñ€Ð¾Ð²Ð°Ð½Ð¸Ðµ
    // Ð±Ñ‹Ð»Ð¾
    // ÑƒÑ�Ð¿ÐµÑˆÐ½Ð¾
    // Ñ�Ð½Ñ�Ñ‚Ð¾
    // Ñ�
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ð°
    // $s1.
    public static final int ONCE_AN_ITEM_IS_AUGMENTED_IT_CANNOT_BE_AUGMENTED_AGAIN = 1970; // Ð—Ð°Ñ‡Ð°Ñ€Ð¾Ð²Ð°Ð½Ð½Ñ‹Ð¹
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð·Ð°Ñ‡Ð°Ñ€Ð¾Ð²Ð°Ð½
    // Ñ�Ð½Ð¾Ð²Ð°.

    public static final int YOU_CANNOT_AUGMENT_ITEMS_WHILE_A_PRIVATE_STORE_OR_PRIVATE_WORKSHOP_IS_IN_OPERATION = 1972; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð·Ð°Ñ‡Ð°Ñ€Ð¾Ð²Ð°Ñ‚ÑŒ
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ñ‹,
    // Ð¿Ð¾ÐºÐ°
    // Ð´ÐµÐ¹Ñ�Ñ‚Ð²ÑƒÐµÑ‚
    // Ñ€ÐµÐ¶Ð¸Ð¼
    // Ñ‚Ð¾Ñ€Ð³Ð¾Ð²Ð¾Ð¹
    // Ð»Ð°Ð²ÐºÐ¸
    // Ð¸Ð»Ð¸
    // Ð¼Ð°Ñ�Ñ‚ÐµÑ€Ñ�ÐºÐ¾Ð¹.
    public static final int YOU_CANNOT_AUGMENT_ITEMS_WHILE_DEAD = 1974; // Ð’Ñ‹ Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð·Ð°Ñ‡Ð°Ñ€Ð¾Ð²Ð°Ñ‚ÑŒ
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ñ‹,
    // Ð±ÑƒÐ´ÑƒÑ‡Ð¸
    // Ð¼ÐµÑ€Ñ‚Ð²Ñ‹Ð¼.
    public static final int YOU_CANNOT_AUGMENT_ITEMS_WHILE_PARALYZED = 1976; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð·Ð°Ñ‡Ð°Ñ€Ð¾Ð²Ð°Ñ‚ÑŒ
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ñ‹,
    // Ð±ÑƒÐ´ÑƒÑ‡Ð¸
    // Ð¿Ð°Ñ€Ð°Ð»Ð¸Ð·Ð¾Ð²Ð°Ð½Ð½Ñ‹Ð¼.
    public static final int YOU_CANNOT_AUGMENT_ITEMS_WHILE_FISHING = 1977; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð·Ð°Ñ‡Ð°Ñ€Ð¾Ð²Ð°Ñ‚ÑŒ
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ñ‹
    // Ð²Ð¾
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ñ€Ñ‹Ð±Ð°Ð»ÐºÐ¸.
    public static final int YOU_CANNOT_AUGMENT_ITEMS_WHILE_SITTING_DOWN = 1978; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð·Ð°Ñ‡Ð°Ñ€Ð¾Ð²Ð°Ñ‚ÑŒ
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ñ‹
    // Ñ�Ð¸Ð´Ñ�.

    public static final int PRESS_THE_AUGMENT_BUTTON_TO_BEGIN = 1984; // Ð”Ð»Ñ�
    // Ð½Ð°Ñ‡Ð°Ð»Ð°
    // Ð½Ð°Ð¶Ð¼Ð¸Ñ‚Ðµ
    // ÐºÐ½Ð¾Ð¿ÐºÑƒ
    // "Ð—Ð°Ñ‡Ð°Ñ€Ð¾Ð²Ð°Ñ‚ÑŒ".
    public static final int AUGMENTATION_FAILED_DUE_TO_INAPPROPRIATE_CONDITIONS = 2001; // Ð£Ñ�Ð»Ð¾Ð²Ð¸Ñ�
    // Ð½Ðµ
    // Ñ�Ð¾Ð±Ð»ÑŽÐ´ÐµÐ½Ñ‹,
    // Ð·Ð°Ñ‡Ð°Ñ€Ð¾Ð²Ð°Ð½Ð¸Ðµ
    // Ð½Ðµ
    // ÑƒÐ´Ð°Ð»Ð¾Ñ�ÑŒ.

    // Shadow items
    public static final int S1S_REMAINING_MANA_IS_NOW_10 = 1979; // $s1:
    // Ð¾Ñ�Ñ‚Ð°Ð»Ð¾Ñ�ÑŒ 10
    // Ð¼Ð°Ð½Ñ‹.
    public static final int S1S_REMAINING_MANA_IS_NOW_5 = 1980; // $s1: Ð¾Ñ�Ñ‚Ð°Ð»Ð¾Ñ�ÑŒ
    // 5 Ð¼Ð°Ð½Ñ‹.
    public static final int S1S_REMAINING_MANA_IS_NOW_1_IT_WILL_DISAPPEAR_SOON = 1981; // $s1:
    // Ð¾Ñ�Ñ‚Ð°Ð»Ð°Ñ�ÑŒ
    // 1
    // Ð¼Ð°Ð½Ð°.
    // ÐŸÑ€ÐµÐ´Ð¼ÐµÑ‚
    // Ñ�ÐºÐ¾Ñ€Ð¾
    // Ð¸Ñ�Ñ‡ÐµÐ·Ð½ÐµÑ‚.
    public static final int S1S_REMAINING_MANA_IS_NOW_0_AND_THE_ITEM_HAS_DISAPPEARED = 1982; // $s1:
    // Ð¼Ð°Ð½Ð°
    // Ð·Ð°ÐºÐ¾Ð½Ñ‡Ð¸Ð»Ð°Ñ�ÑŒ.
    // ÐŸÑ€ÐµÐ´Ð¼ÐµÑ‚
    // Ð¸Ñ�Ñ‡ÐµÐ·.

    // Limited-items
    public static final int THE_LIMITED_TIME_ITEM_HAS_BEEN_DELETED = 2366; // Ð’Ñ‹
    // ÑƒÐ´Ð°Ð»Ð¸Ð»Ð¸
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚
    // Ñ�
    // Ð¾Ð³Ñ€Ð°Ð½Ð¸Ñ‡ÐµÐ½Ð½Ñ‹Ð¼
    // Ð²Ñ€ÐµÐ¼ÐµÐ½ÐµÐ¼
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ð½Ð¸Ñ�.

    // Ð¢Ñ€Ð°Ð½Ñ�Ñ„Ð¾Ñ€Ð¼Ð°Ñ†Ð¸Ñ�
    public static final int YOU_ALREADY_POLYMORPHED_AND_CANNOT_POLYMORPH_AGAIN = 2058; // Ð’Ñ‹
    // ÑƒÐ¶Ðµ
    // Ð¿Ñ€ÐµÐ²Ñ€Ð°Ñ‚Ð¸Ð»Ð¸Ñ�ÑŒ
    // Ð¸
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ñ�Ð´ÐµÐ»Ð°Ñ‚ÑŒ
    // Ñ�Ñ‚Ð¾Ð³Ð¾
    // Ñ�Ð½Ð¾Ð²Ð°.
    public static final int THE_NEARBLY_AREA_IS_TOO_NARROW_FOR_YOU_TO_POLYMORPH_PLEASE_MOVE_TO_ANOTHER_AREA_AND_TRY_TO_POLYMORPH_AGAIN = 2059; // ÐžÐºÑ€ÑƒÐ¶Ð°ÑŽÑ‰ÐµÐµ
    // Ð¿Ñ€Ð¾Ñ�Ñ‚Ñ€Ð°Ð½Ñ�Ñ‚Ð²Ð¾
    // Ñ�Ð»Ð¸ÑˆÐºÐ¾Ð¼
    // Ñ‚ÐµÑ�Ð½Ð¾
    // Ð´Ð»Ñ�
    // Ð²Ð°ÑˆÐµÐ³Ð¾
    // Ð¿Ñ€ÐµÐ²Ñ€Ð°Ñ‰ÐµÐ½Ð¸Ñ�.
    // ÐŸÐ¾Ð¶Ð°Ð»ÑƒÐ¹Ñ�Ñ‚Ð°,
    // Ð¾Ñ‚Ð¾Ð¹Ð´Ð¸Ñ‚Ðµ
    // Ð²
    // Ð´Ñ€ÑƒÐ³ÑƒÑŽ
    // Ð·Ð¾Ð½Ñƒ
    // Ð¸
    // Ð¿Ð¾Ð¿Ñ€Ð¾Ð±ÑƒÐ¹Ñ‚Ðµ
    // Ð¿Ñ€ÐµÐ²Ñ€Ð°Ñ‚Ð¸Ñ‚ÑŒÑ�Ñ�
    // Ñ�Ð½Ð¾Ð²Ð°.
    public static final int YOU_CANNOT_POLYMORPH_INTO_THE_DESIRED_FORM_IN_WATER = 2060; // Ð’Ñ‹
    // Ð½Ðµ
    // Ñ�Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¿Ñ€ÐµÐ²Ñ€Ð°Ñ‚Ð¸Ñ‚ÑŒÑ�Ñ�
    // Ð²
    // Ð²Ð¾Ð´Ðµ.
    public static final int YOU_ARE_STILL_UNDER_TRANSFORM_PENALTY_AND_CANNOT_BE_POLYMORPHED = 2061; // Ð’Ñ‹
    // Ð½Ð°Ñ…Ð¾Ð´Ð¸Ñ‚ÐµÑ�ÑŒ
    // Ð¿Ð¾Ð´
    // Ð´ÐµÐ¹Ñ�Ñ‚Ð²Ð¸ÐµÐ¼
    // ÑˆÑ‚Ñ€Ð°Ñ„Ð°
    // Ð¿Ñ€ÐµÐ²Ñ€Ð°Ñ‰ÐµÐ½Ð¸Ñ�
    // Ð¸
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¸Ð·Ð¼ÐµÐ½Ð¸Ñ‚ÑŒ
    // Ñ„Ð¾Ñ€Ð¼Ñƒ.
    public static final int YOU_CANNOT_POLYMORPH_WHEN_YOU_HAVE_SUMMONED_A_SERVITOR_PET = 2062; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¿Ñ€ÐµÐ²Ñ€Ð°Ñ‚Ð¸Ñ‚ÑŒÑ�Ñ�,
    // ÐºÐ¾Ð³Ð´Ð°
    // Ð¿Ñ€Ð¸Ð·Ð²Ð°Ð½
    // Ñ�Ð»ÑƒÐ³Ð°/Ð¿Ð¸Ñ‚Ð¾Ð¼ÐµÑ†.
    public static final int YOU_CANNOT_POLYMORPH_WHILE_RIDING_A_PET = 2063; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¿Ñ€ÐµÐ²Ñ€Ð°Ñ‚Ð¸Ñ‚ÑŒÑ�Ñ�
    // Ð²ÐµÑ€Ñ…Ð¾Ð¼
    // Ð½Ð°
    // Ð¿Ð¸Ñ‚Ð¾Ð¼Ñ†Ðµ.
    public static final int YOU_CANNOT_POLYMORPH_WHILE_UNDER_THE_EFFECT_OF_A_SPECIAL_SKILL = 2064; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¿Ñ€ÐµÐ²Ñ€Ð°Ñ‚Ð¸Ñ‚ÑŒÑ�Ñ�,
    // Ð½Ð°Ñ…Ð¾Ð´Ñ�Ñ�ÑŒ
    // Ð¿Ð¾Ð´
    // Ð´ÐµÐ¹Ñ�Ñ‚Ð²Ð¸ÐµÐ¼
    // Ð¾Ñ�Ð¾Ð±Ð¾Ð³Ð¾
    // ÑƒÐ¼ÐµÐ½Ð¸Ñ�.
    public static final int YOU_CANNOT_POLYMORPH_WHILE_RIDING_A_BOAT = 2182; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¿ÐµÑ€ÐµÐ²Ð¾Ð¿Ð»Ð¾Ñ‰Ð°Ñ‚ÑŒÑ�Ñ�
    // Ð½Ð°
    // ÐºÐ¾Ñ€Ð°Ð±Ð»Ðµ.
    public static final int YOU_CANNOT_BOARD_A_SHIP_WHILE_YOU_ARE_POLYMORPHED = 2213; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð²Ð·Ð¾Ð¹Ñ‚Ð¸
    // Ð½Ð°
    // Ð±Ð¾Ñ€Ñ‚
    // ÐºÐ¾Ñ€Ð°Ð±Ð»Ñ�
    // Ð¿Ñ€ÐµÐ²Ñ€Ð°Ñ‰ÐµÐ½Ð½Ñ‹Ð¼.
    public static final int CURRENT_POLYMORPH_FORM_CANNOT_BE_APPLIED_WITH_CORRESPONDING_EFFECTS = 2194; // ÐŸÑ€ÐµÐ²Ñ€Ð°Ñ‰ÐµÐ½Ð¸Ðµ
    // Ð½ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾
    // Ñ�
    // Ñ�Ñ‚Ð¸Ð¼Ð¸
    // Ñ�Ñ„Ñ„ÐµÐºÑ‚Ð°Ð¼Ð¸.
    public static final int SHOUT_AND_TRADE_CHATING_CANNOT_BE_USED_SHILE_POSSESSING_A_CURSED_WEAPON = 2085; // ÐšÑ€Ð¸Ðº
    // Ð¸
    // Ñ‚Ð¾Ñ€Ð³Ð¾Ð²Ð»Ñ�
    // Ð²
    // Ñ‡Ð°Ñ‚Ðµ
    // Ð½ÐµÐ´Ð¾Ñ�Ñ‚ÑƒÐ¿Ð½Ñ‹,
    // Ð¿Ð¾ÐºÐ°
    // Ñƒ
    // Ð²Ð°Ñ�
    // ÐµÑ�Ñ‚ÑŒ
    // Ð¿Ñ€Ð¾ÐºÐ»Ñ�Ñ‚Ð¾Ðµ
    // Ð¾Ñ€ÑƒÐ¶Ð¸Ðµ.
    public static final int YOU_CANNOT_TRANSFORM_WHILE_SITTING = 2283; // Ð’Ñ‹ Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¿Ñ€ÐµÐ²Ñ€Ð°Ñ‚Ð¸Ñ‚ÑŒÑ�Ñ�
    // Ñ�Ð¸Ð´Ñ�.

    public static final int THERE_IS_NOT_ENOUGH_SPACE_TO_MOVE_THE_SKILL_CANNOT_BE_USED = 2161; // Ð�ÐµÐ´Ð¾Ñ�Ñ‚Ð°Ñ‚Ð¾Ñ‡Ð½Ð¾
    // Ñ�Ð²Ð¾Ð±Ð¾Ð´Ð½Ð¾Ð³Ð¾
    // Ð¿Ñ€Ð¾Ñ�Ñ‚Ñ€Ð°Ð½Ñ�Ñ‚Ð²Ð°,
    // ÑƒÐ¼ÐµÐ½Ð¸Ðµ
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ð½Ð¾.

    // Ð�Ð±Ñ�Ð¾Ñ€Ð±Ð°Ñ†Ð¸Ñ� Ð´ÑƒÑˆ
    public static final int YOUR_SOUL_HAS_INCREASED_BY_S1_SO_IT_IS_NOW_AT_S2 = 2162; // Ð’Ð°ÑˆÐµ
    // ÐºÐ¾Ð»Ð¸Ñ‡ÐµÑ�Ñ‚Ð²Ð¾
    // Ð´ÑƒÑˆ
    // Ð¿Ð¾Ð²Ñ‹Ñ�Ð¸Ð»Ð¾Ñ�ÑŒ
    // Ð½Ð°
    // $s1
    // Ð¸
    // Ñ�ÐµÐ¹Ñ‡Ð°Ñ�
    // Ñ�Ð¾Ñ�Ñ‚Ð°Ð²Ð»Ñ�ÐµÑ‚
    // $s2.
    public static final int SOUL_CANNOT_BE_INCREASED_ANY_MORE = 2163; // ÐšÐ¾Ð»Ð¸Ñ‡ÐµÑ�Ñ‚Ð²Ð¾
    // Ð´ÑƒÑˆ
    // Ð±Ð¾Ð»ÑŒÑˆÐµ
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð±Ñ‹Ñ‚ÑŒ
    // Ð¿Ð¾Ð²Ñ‹ÑˆÐµÐ½Ð¾.
    public static final int SOUL_CANNOT_BE_ABSORBED_ANY_MORE = 2186; // Ð‘Ð¾Ð»ÑŒÑˆÐµ
    // Ð´ÑƒÑˆ
    // Ð¿Ð¾Ð³Ð»Ð¾Ñ‚Ð¸Ñ‚ÑŒ
    // Ð½ÐµÐ»ÑŒÐ·Ñ�.
    public static final int THERE_IS_NOT_ENOUGHT_SOUL = 2195; // Ð�ÐµÐ´Ð¾Ñ�Ñ‚Ð°Ñ‚Ð¾Ñ‡Ð½Ð¾
    // Ð´ÑƒÑˆ.

    public static final int AGATHION_SKILLS_CAN_BE_USED_ONLY_WHEN_AGATHION_IS_SUMMONED = 2292; // Ð£Ð¼ÐµÐ½Ð¸Ñ�
    // Ð�Ð³Ð°Ñ‚Ð¸Ð¾Ð½Ð°
    // Ð¼Ð¾Ð¶Ð½Ð¾
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒ
    // Ñ‚Ð¾Ð»ÑŒÐºÐ¾
    // Ð¿Ð¾Ñ�Ð»Ðµ
    // Ñ‚Ð¾Ð³Ð¾,
    // ÐºÐ°Ðº
    // Ð�Ð³Ð°Ñ‚Ð¸Ð¾Ð½
    // Ð¿Ñ€Ð¸Ð·Ð²Ð°Ð½.

    public static final int YOU_HAVE_GAINED_VITALITY_POINTS = 2296; // Ð¢Ñ€ÐµÐ±ÑƒÑŽÑ‚Ñ�Ñ�
    // Ð¾Ñ‡ÐºÐ¸
    // Ñ�Ð½ÐµÑ€Ð³Ð¸Ð¸.
    public static final int YOUR_VITALITY_IS_AT_MAXIMUM = 2314; // Ð­Ð½ÐµÑ€Ð³Ð¸Ñ�
    // Ð¿Ð¾Ð»Ð½Ð°.
    public static final int VITALITY_HAS_INCREASED = 2315; // Ð­Ð½ÐµÑ€Ð³Ð¸Ñ� ÑƒÐ²ÐµÐ»Ð¸Ñ‡ÐµÐ½Ð°.
    public static final int VITALITY_HAS_DECREASED = 2316; // Ð­Ð½ÐµÑ€Ð³Ð¸Ñ� ÑƒÐ¼ÐµÐ½ÑŒÑˆÐµÐ½Ð°.
    public static final int VITALITY_IS_FULLY_EXHAUSTED = 2317; // Ð’Ñ�Ñ� Ñ�Ð½ÐµÑ€Ð³Ð¸Ñ�
    // Ð¸Ð·Ñ€Ð°Ñ�Ñ…Ð¾Ð´Ð¾Ð²Ð°Ð½Ð°.

    public static final int ACQUIRED_50_CLAN_FAME_POINTS = 2326; // Ð’Ñ‹ Ð¿Ð¾Ð»ÑƒÑ‡Ð¸Ð»Ð¸
    // 50 Ð¾Ñ‡ÐºÐ¾Ð²
    // ÐºÐ»Ð°Ð½Ð¾Ð²Ð¾Ð¹
    // Ñ€ÐµÐ¿ÑƒÑ‚Ð°Ñ†Ð¸Ð¸.
    public static final int NOT_ENOUGH_FAME_POINTS = 2327; // Ð£ Ð²Ð°Ñ� Ð½ÐµÐ´Ð¾Ñ�Ñ‚Ð°Ñ‚Ð¾Ñ‡Ð½Ð¾
    // Ð¾Ñ‡ÐºÐ¾Ð² Ñ€ÐµÐ¿ÑƒÑ‚Ð°Ñ†Ð¸Ð¸.
    public static final int YOU_HAVE_ACQUIRED_S1_REPUTATION_SCORE = 2319; // Ð’Ñ‹
    // Ð¿Ð¾Ð»ÑƒÑ‡Ð¸Ð»Ð¸
    // $s1
    // Ð¾Ñ‡ÐºÐ¾Ð²
    // Ñ€ÐµÐ¿ÑƒÑ‚Ð°Ñ†Ð¸Ð¸.

    public static final int C1_IS_IN_A_LOCATION_WHICH_CANNOT_BE_ENTERED_THEREFORE_IT_CANNOT_BE_PROCESSED = 2096; // $c1
    // Ð½Ð°Ñ…Ð¾Ð´Ð¸Ñ‚Ñ�Ñ�
    // Ð²
    // Ð»Ð¾ÐºÐ°Ñ†Ð¸Ð¸,
    // Ð²
    // ÐºÐ¾Ñ‚Ð¾Ñ€ÑƒÑŽ
    // Ð½ÐµÐ»ÑŒÐ·Ñ�
    // Ð²Ð¾Ð¹Ñ‚Ð¸,
    // Ð¿Ð¾Ñ�Ñ‚Ð¾Ð¼Ñƒ
    // Ð´ÐµÐ¹Ñ�Ñ‚Ð²Ð¸Ðµ
    // Ð½ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾.
    public static final int C1S_LEVEL_REQUIREMENT_IS_NOT_SUFFICIENT_AND_CANNOT_BE_ENTERED = 2097; // $c1
    // Ð½Ðµ
    // Ñ�Ð¾Ð¾Ñ‚Ð²ÐµÑ‚Ñ�Ñ‚Ð²ÑƒÐµÑ‚
    // Ñ‚Ñ€ÐµÐ±Ð¾Ð²Ð°Ð½Ð¸Ñ�Ð¼
    // ÑƒÑ€Ð¾Ð²Ð½Ñ�
    // Ð¸
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð²Ð¾Ð¹Ñ‚Ð¸.
    public static final int C1S_QUEST_REQUIREMENT_IS_NOT_SUFFICIENT_AND_CANNOT_BE_ENTERED = 2098; // $c1
    // Ð½Ðµ
    // Ñ�Ð¾Ð¾Ñ‚Ð²ÐµÑ‚Ñ�Ñ‚Ð²ÑƒÐµÑ‚
    // Ñ‚Ñ€ÐµÐ±Ð¾Ð²Ð°Ð½Ð¸Ñ�Ð¼
    // ÐºÐ²ÐµÑ�Ñ‚Ð°
    // Ð¸
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð²Ð¾Ð¹Ñ‚Ð¸.
    public static final int C1S_ITEM_REQUIREMENT_IS_NOT_SUFFICIENT_AND_CANNOT_BE_ENTERED = 2099; // $c1
    // Ð½Ðµ
    // Ñ�Ð¾Ð¾Ñ‚Ð²ÐµÑ‚Ñ�Ñ‚Ð²ÑƒÐµÑ‚
    // Ñ‚Ñ€ÐµÐ±Ð¾Ð²Ð°Ð½Ð¸Ñ�Ð¼
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ð°
    // Ð¸
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð²Ð¾Ð¹Ñ‚Ð¸.
    public static final int C1_MAY_NOT_RE_ENTER_YET = 2100; // Ð’Ñ€ÐµÐ¼Ñ� Ð¿Ð¾Ð²Ñ‚Ð¾Ñ€Ð½Ð¾Ð³Ð¾
    // Ð²Ñ…Ð¾Ð´Ð° Ð´Ð»Ñ�
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð° $c1 ÐµÑ‰Ðµ
    // Ð½Ðµ Ð¿Ñ€Ð¸ÑˆÐ»Ð¾, $c1 Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚ Ð²Ð¾Ð¹Ñ‚Ð¸.
    public static final int YOU_ARE_NOT_CURRENTLY_IN_A_PARTY_SO_YOU_CANNOT_ENTER = 2101; // Ð’Ñ‹
    // Ð½Ðµ
    // Ñ�Ð¾Ñ�Ñ‚Ð¾Ð¸Ñ‚Ðµ
    // Ð²
    // Ð³Ñ€ÑƒÐ¿Ð¿Ðµ
    // Ð¸
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð²Ð¾Ð¹Ñ‚Ð¸.
    public static final int YOU_CANNOT_ENTER_DUE_TO_THE_PARTY_HAVING_EXCEEDED_THE_LIMIT = 2102; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð²Ð¾Ð¹Ñ‚Ð¸,
    // Ñ‚Ð°Ðº
    // ÐºÐ°Ðº
    // Ð³Ñ€ÑƒÐ¿Ð¿Ð°
    // Ð¿Ñ€ÐµÐ²Ñ‹Ñ�Ð¸Ð»Ð°
    // Ð»Ð¸Ð¼Ð¸Ñ‚.
    public static final int YOU_CANNOT_ENTER_BECAUSE_YOU_ARE_NOT_IN_A_CURRENT_COMMAND_CHANNEL = 2103; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð²Ð¾Ð¹Ñ‚Ð¸,
    // Ñ‚Ð°Ðº
    // ÐºÐ°Ðº
    // Ð²Ñ‹
    // Ð½Ðµ
    // Ð²
    // Ñ‚ÐµÐºÑƒÑ‰ÐµÐ¼
    // ÐšÐ°Ð½Ð°Ð»Ðµ
    // ÐšÐ¾Ð¼Ð°Ð½Ð´Ñ‹.
    // 2104 1 The maximum number of instance zones has been exceeded. You cannot
    // enter
    public static final int YOU_HAVE_ENTERED_ANOTHER_INSTANCE_ZONE_THEREFORE_YOU_CANNOT_ENTER_CORRESPONDING_DUNGEON = 2105; // Ð’Ñ‹
    // ÑƒÐ¶Ðµ
    // Ð²Ð¾ÑˆÐ»Ð¸
    // Ð²
    // Ð´Ñ€ÑƒÐ³ÑƒÑŽ
    // Ð²Ñ€ÐµÐ¼ÐµÐ½Ð½ÑƒÑŽ
    // Ð·Ð¾Ð½Ñƒ,
    // Ð¿Ð¾Ñ�Ñ‚Ð¾Ð¼Ñƒ
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð²Ð¾Ð¹Ñ‚Ð¸
    // Ð²
    // Ð´Ð°Ð½Ð½ÑƒÑŽ.
    public static final int THIS_DUNGEON_WILL_EXPIRE_IN_S1_MINUTES = 2106; // ÐŸÐ¾Ð´Ð·ÐµÐ¼ÐµÐ»ÑŒÐµ
    // Ð·Ð°ÐºÑ€Ð¾ÐµÑ‚Ñ�Ñ�
    // Ñ‡ÐµÑ€ÐµÐ·
    // $s1
    // Ð¼Ð¸Ð½.
    // ÐŸÐ¾
    // Ð¸Ñ�Ñ‚ÐµÑ‡ÐµÐ½Ð¸Ð¸
    // Ð²Ñ€ÐµÐ¼ÐµÐ½Ð¸
    // Ð²Ñ‹
    // Ð±ÑƒÐ´ÐµÑ‚Ðµ
    // Ð²Ñ‹Ð±Ñ€Ð¾ÑˆÐµÐ½Ñ‹
    // Ð¸Ð·
    // Ð¿Ð¾Ð´Ð·ÐµÐ¼ÐµÐ»ÑŒÑ�.
    public static final int THIS_INSTANCE_ZONE_WILL_BE_TERMINATED_IN_S1_MINUTES_YOU_WILL_BE_FORCED_OUT_OF_THE_DANGEON_THEN_TIME_EXPIRES = 2107; // Ð’Ñ€ÐµÐ¼ÐµÐ½Ð½Ð°Ñ�
    // Ð·Ð¾Ð½Ð°
    // Ð·Ð°ÐºÑ€Ð¾ÐµÑ‚Ñ�Ñ�
    // Ñ‡ÐµÑ€ÐµÐ·
    // $s1
    // Ð¼Ð¸Ð½.
    // ÐŸÐ¾
    // Ð¸Ñ�Ñ‚ÐµÑ‡ÐµÐ½Ð¸Ð¸
    // Ð²Ñ€ÐµÐ¼ÐµÐ½Ð¸
    // Ð²Ñ‹
    // Ð±ÑƒÐ´ÐµÑ‚Ðµ
    // Ð²Ñ‹Ð±Ñ€Ð¾ÑˆÐµÐ½Ñ‹
    // Ð¸Ð·
    // Ð¿Ð¾Ð´Ð·ÐµÐ¼ÐµÐ»ÑŒÑ�.
    public static final int ONLY_A_PARTY_LEADER_CAN_TRY_TO_ENTER = 2185; // Ð¢Ð¾Ð»ÑŒÐºÐ¾
    // Ð»Ð¸Ð´ÐµÑ€
    // Ð³Ñ€ÑƒÐ¿Ð¿Ñ‹
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð¿Ð¾Ð¿Ñ€Ð¾Ð±Ð¾Ð²Ð°Ñ‚ÑŒ
    // Ð²Ð¾Ð¹Ñ‚Ð¸.
    public static final int ITS_TOO_FAR_FROM_THE_NPC_TO_WORK = 2193; // NPC
    // Ñ�Ð»Ð¸ÑˆÐºÐ¾Ð¼
    // Ð´Ð°Ð»ÐµÐºÐ¾.

    public static final int INSTANCE_ZONE_TIME_LIMIT = 2228; // Ð›Ð¸Ð¼Ð¸Ñ‚ Ð²Ñ€ÐµÐ¼ÐµÐ½Ð¸
    // Ð·Ð¾Ð½Ñ‹:
    public static final int THERE_IS_NO_INSTANCE_ZONE_UNDER_A_TIME_LIMIT = 2229; // Ð’Ñ�Ðµ
    // Ð²Ñ€ÐµÐ¼ÐµÐ½Ð½Ñ‹Ðµ
    // Ð·Ð¾Ð½Ñ‹
    // Ð´Ð¾Ñ�Ñ‚ÑƒÐ¿Ð½Ñ‹.
    public static final int S1_WILL_BE_AVAILABLE_FOR_RE_USE_AFTER_S2_HOURS_S3_MINUTES = 2230; // $s1:
    // Ð·Ð¾Ð½Ð°
    // Ð±ÑƒÐ´ÐµÑ‚
    // Ð´Ð¾Ñ�Ñ‚ÑƒÐ¿Ð½Ð°
    // Ñ‡ÐµÑ€ÐµÐ·
    // $s2
    // Ñ‡
    // $s3
    // Ð¼Ð¸Ð½.

    // TODO Ð Ð°Ñ�Ñ�Ñ‚Ð°Ð²Ð¸Ñ‚ÑŒ Ñ�Ð¾Ð¾Ð±Ñ‰ÐµÐ½Ð¸Ñ� Ð´Ð»Ñ� Ñ„Ð¾Ñ€Ñ‚Ð¾Ð²
    public static final int ENEMY_BLOOD_PLEDGES_HAVE_INTRUDED_INTO_THE_FORTRESS = 2084; // Ð’Ñ€Ð°Ð¶ÐµÑ�ÐºÐ¸Ðµ
    // Ð—Ð°Ð»Ð¾Ð¶Ð½Ð¸ÐºÐ¸
    // ÐšÑ€Ð¾Ð²Ð¸
    // Ð²Ð¾Ñ€Ð²Ð°Ð»Ð¸Ñ�ÑŒ
    // Ð²
    // ÐºÑ€ÐµÐ¿Ð¾Ñ�Ñ‚ÑŒ
    public static final int A_FORTRESS_IS_UNDER_ATTACK = 2087; // ÐšÑ€ÐµÐ¿Ð¾Ñ�Ñ‚ÑŒ
    // Ð°Ñ‚Ð°ÐºÐ¾Ð²Ð°Ð½Ð°!
    public static final int S1_MINUTE_UNTIL_THE_FORTRESS_BATTLE_STARTS = 2088; // Ð‘Ð¸Ñ‚Ð²Ð°
    // Ð·Ð°
    // ÐºÑ€ÐµÐ¿Ð¾Ñ�Ñ‚ÑŒ
    // Ð½Ð°Ñ‡Ð¸Ð½Ð°ÐµÑ‚Ñ�Ñ�
    // Ñ‡ÐµÑ€ÐµÐ·
    // $s1
    // Ð¼Ð¸Ð½.
    public static final int S1_SECOND_UNTIL_THE_FORTRESS_BATTLE_STARTS = 2089; // Ð‘Ð¸Ñ‚Ð²Ð°
    // Ð·Ð°
    // ÐºÑ€ÐµÐ¿Ð¾Ñ�Ñ‚ÑŒ
    // Ð½Ð°Ñ‡Ð¸Ð½Ð°ÐµÑ‚Ñ�Ñ�
    // Ñ‡ÐµÑ€ÐµÐ·
    // $s1
    // Ñ�ÐµÐº.
    public static final int THE_FORTRESS_BATTLE_S1_HAS_BEGAN = 2090; // Ð‘Ð¸Ñ‚Ð²Ð° Ð·Ð°
    // ÐºÑ€ÐµÐ¿Ð¾Ñ�Ñ‚ÑŒ
    // Ð½Ð°Ñ‡Ð°Ð»Ð°Ñ�ÑŒ.
    public static final int YOUR_CLAN_HAS_BEEN_REGISTERED_TO_S1_FORTRESS_BATTLE = 2169; // Ð’Ð°Ñˆ
    // ÐºÐ»Ð°Ð½
    // Ð±Ñ‹Ð»
    // Ð·Ð°Ñ�Ð²Ð»ÐµÐ½
    // Ð½Ð°
    // Ð±Ð¸Ñ‚Ð²Ñƒ
    // Ð·Ð°
    // ÐºÑ€ÐµÐ¿Ð¾Ñ�Ñ‚ÑŒ
    // $s1.
    public static final int THE_FORTRESS_BATTLE_OF_S1_HAS_FINISHED = 2183; // Ð‘Ð¸Ñ‚Ð²Ð°
    // Ð·Ð°
    // ÐºÑ€ÐµÐ¿Ð¾Ñ�Ñ‚ÑŒ
    // $s1
    // Ð·Ð°ÐºÐ¾Ð½Ñ‡ÐµÐ½Ð°.
    public static final int S1_CLAN_IS_VICTORIOUS_IN_THE_FORTRESS_BATLE_OF_S2 = 2184; // Ð’
    // Ð±Ð¸Ñ‚Ð²Ðµ
    // Ð·Ð°
    // ÐºÑ€ÐµÐ¿Ð¾Ñ�Ñ‚ÑŒ
    // $s2
    // Ð¿Ð¾Ð±ÐµÐ´Ð¸Ð»
    // ÐºÐ»Ð°Ð½
    // $s1.
    public static final int THE_REBEL_ARMY_RECAPTURED_THE_FORTRESS = 2276; // Ð�Ñ€Ð¼Ð¸Ñ�
    // Ð¿Ð¾Ð²Ñ�Ñ‚Ð°Ð½Ñ†ÐµÐ²
    // Ð¾Ñ‚Ð²Ð¾ÐµÐ²Ð°Ð»Ð°
    // ÐºÑ€ÐµÐ¿Ð¾Ñ�Ñ‚ÑŒ
    // Ð¾Ð±Ñ€Ð°Ñ‚Ð½Ð¾.

    public static final int FIVE_YEARS_HAVE_PASSED_SINCE_THIS_CHARACTERS_CREATION = 2447; // Ð¡Ð¾
    // Ð´Ð½Ñ�
    // Ñ�Ð¾Ð·Ð´Ð°Ð½Ð¸Ñ�
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°
    // Ð¿Ñ€Ð¾ÑˆÐ»Ð¾
    // 5
    // Ð»ÐµÑ‚.
    public static final int YOUR_BIRTHDAY_GIFT_HAS_ARRIVED_YOU_CAN_OBTAIN_IT_FROM_THE_GATEKEEPER_IN_ANY_VILLAGE = 2448; // Ð”Ð¾Ñ�Ñ‚Ð°Ð²Ð»ÐµÐ½
    // Ð¿Ð¾Ð´Ð°Ñ€Ð¾Ðº
    // Ð²
    // Ñ‡ÐµÑ�Ñ‚ÑŒ
    // Ð´Ð½Ñ�
    // Ñ�Ð¾Ð·Ð´Ð°Ð½Ð¸Ñ�
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°.
    // Ð’Ñ‹
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¿Ð¾Ð»ÑƒÑ‡Ð¸Ñ‚ÑŒ
    // ÐµÐ³Ð¾
    // Ñƒ
    // Ð¥Ñ€Ð°Ð½Ð¸Ñ‚ÐµÐ»Ñ�
    // ÐŸÐ¾Ñ€Ñ‚Ð°Ð»Ð°
    // Ð»ÑŽÐ±Ð¾Ð¹
    // Ð¸Ð·
    // Ð´ÐµÑ€ÐµÐ²ÐµÐ½ÑŒ.
    public static final int THERE_ARE_S1_DAYS_UNTIL_YOUR_CHARACTERS_BIRTHDAY_ON_THAT_DAY_YOU_CAN_OBTAIN_A_SPECIAL_GIFT_FROM_THE_GATEKEEPER_IN_ANY_VILLAGE = 2449; // Ð”Ð¾
    // Ð´Ð½Ñ�
    // Ñ�Ð¾Ð·Ð´Ð°Ð½Ð¸Ñ�
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°
    // Ð¾Ñ�Ñ‚Ð°Ð»Ð¾Ñ�ÑŒ
    // $s1
    // Ð´Ð½.
    // Ð’Ñ‹
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¿Ð¾Ð»ÑƒÑ‡Ð¸Ñ‚ÑŒ
    // Ð¿Ð¾Ð´Ð°Ñ€Ð¾Ðº
    // Ñƒ
    // Ð¥Ñ€Ð°Ð½Ð¸Ñ‚ÐµÐ»Ñ�
    // ÐŸÐ¾Ñ€Ñ‚Ð°Ð»Ð°
    // Ð»ÑŽÐ±Ð¾Ð¹
    // Ð¸Ð·
    // Ð´ÐµÑ€ÐµÐ²ÐµÐ½ÑŒ.
    public static final int C1S_CHARACTER_BIRTHDAY_IS_S3S4S2 = 2450; // Ð”Ð°Ñ‚Ð°
    // Ñ�Ð¾Ð·Ð´Ð°Ð½Ð¸Ñ�
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°
    // $c1: $s2
    // Ð³. $s3
    // Ð¼. $s4
    // Ñ‡.

    public static final int _IF_YOU_JOIN_THE_CLAN_ACADEMY_YOU_CAN_BECOME_A_CLAN_MEMBER_AND_LEARN_THE_GAME_SYSTEM_UNTIL_YOU = 2875; // ÐŸÐ¾Ñ�Ñ‚ÑƒÐ¿Ð¸Ð²
    // Ð²
    // Ð�ÐºÐ°Ð´ÐµÐ¼Ð¸ÑŽ
    // ÐºÐ»Ð°Ð½Ð°,
    // Ð’Ñ‹
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ñ�Ñ‚Ð°Ñ‚ÑŒ
    // ÐµÐ³Ð¾
    // Ñ‡Ð»ÐµÐ½Ð¾Ð¼
    // Ð¸
    // Ð¸Ð·ÑƒÑ‡Ð°Ñ‚ÑŒ
    // Ð¸Ð³Ñ€Ð¾Ð²ÑƒÑŽ
    // Ñ�Ð¸Ñ�Ñ‚ÐµÐ¼Ñƒ
    // Ð´Ð¾
    // Ð¿Ð¾Ð»ÑƒÑ‡ÐµÐ½Ð¸Ñ�
    // 40-Ð³Ð¾
    // ÑƒÑ€Ð¾Ð²Ð½Ñ�.
    // Ð•Ñ�Ð»Ð¸
    // Ñ…Ð¾Ñ‚Ð¸Ñ‚Ðµ
    // Ð¿Ð¾Ð»ÑƒÑ‡Ð°Ñ‚ÑŒ
    // Ð¾Ñ‚
    // Ð¸Ð³Ñ€Ñ‹
    // Ð±Ð¾Ð»ÑŒÑˆÐµ
    // ÑƒÐ´Ð¾Ð²Ð¾Ð»ÑŒÑ�Ñ‚Ð²Ð¸Ñ�,
    // Ñ‚Ð¾
    // Ð½Ð°Ñ�Ñ‚Ð¾Ñ�Ñ‚ÐµÐ»ÑŒÐ½Ð¾
    // Ñ€ÐµÐºÐ¾Ð¼ÐµÐ½Ð´ÑƒÐµÐ¼
    // Ð’Ð°Ð¼
    // Ð¿Ð¾Ñ�Ñ‚ÑƒÐ¿Ð¸Ñ‚ÑŒ
    // Ð²
    // Ð�ÐºÐ°Ð´ÐµÐ¼Ð¸ÑŽ.
    public static final int _IF_YOU_BECOME_LEVEL_40_THE_SECOND_CLASS_CHANGE_IS_AVAILABLE_IF_YOU_COMPLETE_THE_SECOND_CLASS = 2876; // ÐŸÐ¾
    // Ð´Ð¾Ñ�Ñ‚Ð¸Ð¶ÐµÐ½Ð¸Ð¸
    // 40-Ð³Ð¾
    // ÑƒÑ€Ð¾Ð²Ð½Ñ�
    // Ð’Ñ‹
    // Ð¿Ð¾Ð»ÑƒÑ‡Ð°ÐµÑ‚Ðµ
    // Ð²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾Ñ�Ñ‚ÑŒ
    // Ð²Ñ‚Ð¾Ñ€Ð¾Ð¹
    // Ñ�Ð¼ÐµÐ½Ñ‹
    // Ð¿Ñ€Ð¾Ñ„ÐµÑ�Ñ�Ð¸Ð¸.
    // ÐŸÐ¾Ñ�Ð»Ðµ
    // Ð²Ñ‹Ð¿Ð¾Ð»Ð½ÐµÐ½Ð¸Ñ�
    // Ñ�Ñ‚Ð¾Ð³Ð¾
    // Ð´ÐµÐ¹Ñ�Ñ‚Ð²Ð¸Ñ�
    // Ð²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾Ñ�Ñ‚Ð¸
    // Ð’Ð°ÑˆÐµÐ³Ð¾
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°
    // Ð²Ð¾Ð·Ñ€Ð°Ñ�Ñ‚ÑƒÑ‚.
    public static final int THIS_ITEM_CANNOT_BE_USED_IN_THE_CURRENT_TRANSFORMATION_STATTE = 2962; // Ð”Ð°Ð½Ð½Ñ‹Ð¹
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚
    // Ð½ÐµÐ´Ð¾Ñ�Ñ‚ÑƒÐ¿ÐµÐ½
    // Ð²
    // Ñ€ÐµÐ¶Ð¸Ð¼Ðµ
    // Ð¿ÐµÑ€ÐµÐ²Ð¾Ð¿Ð»Ð¾Ñ‰ÐµÐ½Ð¸Ñ�.
    public static final int THE_OPPONENT_HAS_NOT_EQUIPPED_S1_SO_S2_CANNOT_BE_USED = 2963; // ÐŸÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶
    // Ð½Ðµ
    // Ð½Ð°Ð´ÐµÐ»
    // $s1%,
    // Ð¿Ð¾Ñ�Ñ‚Ð¾Ð¼Ñƒ
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒ
    // $s2%.
    public static final int BEING_APPOINTED_AS_A_NOBLESSE_WILL_CANCEL_ALL_RELATED_QUESTS_DO_YOU_WISH_TO_CONTINUE = 2964; // Ð•Ñ�Ð»Ð¸
    // Ð’Ñ‹
    // Ñ�Ñ‚Ð°Ð½ÐµÑ‚Ðµ
    // Ð”Ð²Ð¾Ñ€Ñ�Ð½Ð¸Ð½Ð¾Ð¼,
    // Ð²Ñ�Ðµ
    // Ð²Ñ‹Ð¿Ð¾Ð»Ð½Ñ�ÐµÐ¼Ñ‹Ðµ
    // ÐºÐ²ÐµÑ�Ñ‚Ñ‹
    // Ð±ÑƒÐ´ÑƒÑ‚
    // ÑƒÐ´Ð°Ð»ÐµÐ½Ñ‹.
    // ÐŸÑ€Ð¾Ð´Ð¾Ð»Ð¶Ð¸Ñ‚ÑŒ?
    public static final int YOU_CANNOT_PURCHASE_AND_RE_PURCHASE_THE_SAME_TYPE_OF_ITEM_AT_THE_SAME_TIME = 2965; // ÐŸÐµÑ€Ð²Ð¸Ñ‡Ð½Ð°Ñ�
    // Ð¸
    // Ð¿Ð¾Ð²Ñ‚Ð¾Ñ€Ð½Ð°Ñ�
    // Ð¿Ð¾ÐºÑƒÐ¿ÐºÐ°
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ð¾Ð²
    // Ð¾Ð´Ð¸Ð½Ð°ÐºÐ¾Ð²Ð¾Ð³Ð¾
    // Ñ‚Ð¸Ð¿Ð°
    // Ð¾Ð´Ð½Ð¾Ð²Ñ€ÐµÐ¼ÐµÐ½Ð½Ð¾
    // Ð½ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð°.
    public static final int IT_S_A_PAYMENT_REQUEST_TRANSACTION_PLEASE_ATTACH_THE_ITEM = 2966; // Ð‘ÐµÐ·Ð¾Ð¿Ð°Ñ�Ð½Ð°Ñ�
    // Ñ�Ð´ÐµÐ»ÐºÐ°.
    // ÐŸÑ€Ð¸ÐºÑ€ÐµÐ¿Ð¸Ñ‚Ðµ
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚.
    public static final int YOU_ARE_ATTEMPTING_TO_SEND_MAIL_DO_YOU_WISH_TO_PROCEED = 2967; // Ð’Ñ‹
    // Ð´ÐµÐ¹Ñ�Ñ‚Ð²Ð¸Ñ‚ÐµÐ»ÑŒÐ½Ð¾
    // Ñ…Ð¾Ñ‚Ð¸Ñ‚Ðµ
    // Ñ�Ð¾Ð²ÐµÑ€ÑˆÐ¸Ñ‚ÑŒ
    // Ð¾Ñ‚Ð¿Ñ€Ð°Ð²ÐºÑƒ?
    public static final int THE_MAIL_LIMIT_240_HAS_BEEN_EXCEEDED_AND_THIS_CANNOT_BE_FORWARDED = 2968; // Ð’Ñ‹
    // Ð¿Ñ€ÐµÐ²Ñ‹Ñ�Ð¸Ð»Ð¸
    // Ð»Ð¸Ð¼Ð¸Ñ‚
    // Ð¿Ð¾Ñ‡Ñ‚Ñ‹
    // (240
    // ÑˆÑ‚.),
    // Ð¿Ð¾Ñ�Ñ‚Ð¾Ð¼Ñƒ
    // Ð¾Ñ‚Ð¿Ñ€Ð°Ð²ÐºÐ°
    // Ð½ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð°.
    public static final int THE_PREVIOUS_MAIL_WAS_FORWARDED_LESS_THAN_1_MINUTE_AGO_AND_THIS_CANNOT_BE_FORWARDED = 2969; // Ð¡
    // Ð¼Ð¾Ð¼ÐµÐ½Ñ‚Ð°
    // Ð¿ÐµÑ€ÐµÑ�Ñ‹Ð»ÐºÐ¸
    // Ð¿Ñ€ÐµÐ´Ñ‹Ð´ÑƒÑ‰ÐµÐ³Ð¾
    // Ð¿Ð¸Ñ�ÑŒÐ¼Ð°
    // Ð½Ðµ
    // Ð¿Ñ€Ð¾ÑˆÐ»Ð¾
    // Ð¾Ð´Ð½Ð¾Ð¹
    // Ð¼Ð¸Ð½ÑƒÑ‚Ñ‹,
    // Ð¿Ð¾Ñ�Ñ‚Ð¾Ð¼Ñƒ
    // Ð¾Ñ‚Ð¿Ñ€Ð°Ð²ÐºÐ°
    // Ð½ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð°.
    public static final int YOU_CANNOT_FORWARD_IN_A_NON_PEACE_ZONE_LOCATION = 2970; // ÐžÑ‚Ð¿Ñ€Ð°Ð²ÐºÐ°
    // Ð²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð°
    // Ñ‚Ð¾Ð»ÑŒÐºÐ¾
    // Ð¸Ð·
    // Ð¼Ð¸Ñ€Ð½Ñ‹Ñ…
    // Ð·Ð¾Ð½.
    public static final int YOU_CANNOT_FORWARD_DURING_AN_EXCHANGE = 2971; // Ð’Ð¾
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ð¾Ð±Ð¼ÐµÐ½Ð°
    // Ð¾Ñ‚Ð¿Ñ€Ð°Ð²ÐºÐ°
    // Ð½ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð°.
    public static final int YOU_CANNOT_FORWARD_BECAUSE_THE_PRIVATE_SHOP_OR_WORKSHOP_IS_IN_PROGRESS = 2972; // ÐžÑ‚Ð¿Ñ€Ð°Ð²ÐºÐ°
    // Ð½ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð°
    // Ð¿Ñ€Ð¸
    // Ð¾Ñ‚ÐºÑ€Ñ‹Ñ‚Ð¾Ð¹
    // Ñ‚Ð¾Ñ€Ð³Ð¾Ð²Ð¾Ð¹
    // Ð»Ð°Ð²ÐºÐµ
    // Ð¸Ð»Ð¸
    // Ð¼Ð°Ñ�Ñ‚ÐµÑ€Ñ�ÐºÐ¾Ð¹.
    public static final int YOU_CANNOT_FORWARD_DURING_AN_ITEM_ENHANCEMENT_OR_ATTRIBUTE_ENHANCEMENT = 2973; // Ð’Ð¾
    // Ð²Ñ€ÐµÐ¼Ñ�
    // ÑƒÐ»ÑƒÑ‡ÑˆÐµÐ½Ð¸Ñ�
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ð°
    // Ð¾Ñ‚Ð¿Ñ€Ð°Ð²ÐºÐ°
    // Ð½ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð°.
    public static final int THE_ITEM_THAT_YOU_RE_TRYING_TO_SEND_CANNOT_BE_FORWARDED_BECAUSE_IT_ISN_T_PROPER = 2974; // ÐžÑ‚Ð¿Ñ€Ð°Ð²Ð»Ñ�ÐµÐ¼Ñ‹Ð¹
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚
    // Ð½Ðµ
    // Ð¿Ð¾Ð´Ñ…Ð¾Ð´Ð¸Ñ‚.
    public static final int YOU_CANNOT_FORWARD_BECAUSE_YOU_DON_T_HAVE_ENOUGH_ADENA = 2975; // Ð£
    // Ð’Ð°Ñ�
    // Ð½Ðµ
    // Ñ…Ð²Ð°Ñ‚Ð°ÐµÑ‚
    // Ð´ÐµÐ½ÐµÐ³
    // Ð´Ð»Ñ�
    // Ð¾Ñ‚Ð¿Ñ€Ð°Ð²ÐºÐ¸.
    public static final int YOU_CANNOT_RECEIVE_IN_A_NON_PEACE_ZONE_LOCATION = 2976; // ÐŸÐ¾Ð»ÑƒÑ‡ÐµÐ½Ð¸Ðµ
    // Ð²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾
    // Ñ‚Ð¾Ð»ÑŒÐºÐ¾
    // Ð²
    // Ð¼Ð¸Ñ€Ð½Ð¾Ð¹
    // Ð·Ð¾Ð½Ðµ.
    public static final int YOU_CANNOT_RECEIVE_DURING_AN_EXCHANGE = 2977; // Ð’Ð¾
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ð¾Ð±Ð¼ÐµÐ½Ð°
    // Ð¿Ð¾Ð»ÑƒÑ‡ÐµÐ½Ð¸Ðµ
    // Ð¿Ð¸Ñ�ÐµÐ¼
    // Ð½ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾.
    public static final int YOU_CANNOT_RECEIVE_BECAUSE_THE_PRIVATE_SHOP_OR_WORKSHOP_IS_IN_PROGRESS = 2978; // ÐŸÑ€Ð¸
    // Ð¾Ñ‚ÐºÑ€Ñ‹Ñ‚Ð¾Ð¹
    // Ñ‚Ð¾Ñ€Ð³Ð¾Ð²Ð¾Ð¹
    // Ð»Ð°Ð²ÐºÐµ
    // Ð¸Ð»Ð¸
    // Ð¼Ð°Ñ�Ñ‚ÐµÑ€Ñ�ÐºÐ¾Ð¹
    // Ð¿Ð¾Ð»ÑƒÑ‡ÐµÐ½Ð¸Ðµ
    // Ð½ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾.
    public static final int YOU_CANNOT_RECEIVE_DURING_AN_ITEM_ENHANCEMENT_OR_ATTRIBUTE_ENHANCEMENT = 2979; // Ð’Ð¾
    // Ð²Ñ€ÐµÐ¼Ñ�
    // ÑƒÐ»ÑƒÑ‡ÑˆÐµÐ½Ð¸Ñ�
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ð°
    // Ð¿Ð¾Ð»ÑƒÑ‡ÐµÐ½Ð¸Ðµ
    // Ð½ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾.
    public static final int YOU_CANNOT_RECEIVE_BECAUSE_YOU_DON_T_HAVE_ENOUGH_ADENA = 2980; // Ð£
    // Ð²Ð°Ñ�
    // Ð½Ðµ
    // Ñ…Ð²Ð°Ñ‚Ð°ÐµÑ‚
    // Ð´ÐµÐ½ÐµÐ³
    // Ð´Ð»Ñ�
    // Ð¿Ð¾Ð»ÑƒÑ‡ÐµÐ½Ð¸Ñ�.
    public static final int YOU_COULD_NOT_RECEIVE_BECAUSE_YOUR_INVENTORY_IS_FULL = 2981; // Ð˜Ð·-Ð·Ð°
    // Ð¾ÑˆÐ¸Ð±ÐºÐ¸
    // Ð¸Ð½Ð²ÐµÐ½Ñ‚Ð°Ñ€Ñ�
    // Ð’Ð°Ð¼
    // Ð½Ðµ
    // ÑƒÐ´Ð°Ð»Ð¾Ñ�ÑŒ
    // Ð¿Ð¾Ð»ÑƒÑ‡Ð¸Ñ‚ÑŒ
    // Ð¿Ð¾Ñ�Ñ‹Ð»ÐºÑƒ.
    public static final int YOU_CANNOT_CANCEL_IN_A_NON_PEACE_ZONE_LOCATION = 2982; // ÐžÑ‚Ð¼ÐµÐ½Ð°
    // Ð´Ð¾Ñ�Ñ‚ÑƒÐ¿Ð½Ð°
    // Ñ‚Ð¾Ð»ÑŒÐºÐ¾
    // Ð²
    // Ð¼Ð¸Ñ€Ð½Ð¾Ð¹
    // Ð·Ð¾Ð½Ðµ.
    public static final int YOU_CANNOT_CANCEL_DURING_AN_EXCHANGE = 2983; // Ð’Ð¾
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ð¾Ð±Ð¼ÐµÐ½Ð°
    // Ð¾Ñ‚Ð¼ÐµÐ½Ð°
    // Ð½ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð°.
    public static final int YOU_CANNOT_CANCEL_BECAUSE_THE_PRIVATE_SHOP_OR_WORKSHOP_IS_IN_PROGRESS = 2984; // ÐŸÑ€Ð¸
    // Ð¾Ñ‚ÐºÑ€Ñ‹Ñ‚Ð¾Ð¹
    // Ñ‚Ð¾Ñ€Ð³Ð¾Ð²Ð¾Ð¹
    // Ð»Ð°Ð²ÐºÐµ
    // Ð¸Ð»Ð¸
    // Ð¼Ð°Ñ�Ñ‚ÐµÑ€Ñ�ÐºÐ¾Ð¹
    // Ð¾Ñ‚Ð¼ÐµÐ½Ð°
    // Ð½ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð°.
    public static final int YOU_CANNOT_CANCEL_DURING_AN_ITEM_ENHANCEMENT_OR_ATTRIBUTE_ENHANCEMENT = 2985; // Ð’Ð¾
    // Ð²Ñ€ÐµÐ¼Ñ�
    // ÑƒÐ»ÑƒÑ‡ÑˆÐµÐ½Ð¸Ñ�
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ð°
    // Ð¾Ñ‚Ð¼ÐµÐ½Ð°
    // Ð½ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð°.
    public static final int PLEASE_SET_THE_AMOUNT_OF_ADENA_TO_SEND = 2986; // Ð’Ñ‹Ð±ÐµÑ€Ð¸Ñ‚Ðµ
    // ÐºÐ¾Ð»Ð¸Ñ‡ÐµÑ�Ñ‚Ð²Ð¾
    // Ð¾Ñ‚Ð¿Ñ€Ð°Ð²Ð»Ñ�ÐµÐ¼Ñ‹Ñ…
    // Ð°Ð´ÐµÐ½.
    public static final int PLEASE_SET_THE_AMOUNT_OF_ADENA_TO_RECEIVE = 2987; // Ð’Ñ‹Ð±ÐµÑ€Ð¸Ñ‚Ðµ
    // ÐºÐ¾Ð»Ð¸Ñ‡ÐµÑ�Ñ‚Ð²Ð¾
    // Ð¿Ð¾Ð»ÑƒÑ‡Ð°ÐµÐ¼Ñ‹Ñ…
    // Ð°Ð´ÐµÐ½.
    public static final int YOU_COULD_NOT_CANCEL_RECEIPT_BECAUSE_YOUR_INVENTORY_IS_FULL = 2988; // Ð˜Ð·-Ð·Ð°
    // Ð¾ÑˆÐ¸Ð±ÐºÐ¸
    // Ð²
    // Ð¸Ð½Ð²ÐµÐ½Ñ‚Ð°Ñ€Ðµ
    // Ð¾Ñ‚Ð¼ÐµÐ½Ð¸Ñ‚ÑŒ
    // Ð¿Ð¾Ð»ÑƒÑ‡ÐµÐ½Ð¸Ðµ
    // Ð½Ðµ
    // ÑƒÐ´Ð°Ð»Ð¾Ñ�ÑŒ.
    public static final int VITAMIN_ITEM_S1_IS_BEING_USED = 2989; // Ð’Ñ‹
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ð»Ð¸
    // Ð’Ð¸Ñ‚Ð°Ð¼Ð¸Ð½
    // $s1%.
    public static final int _2_UNITS_OF_VITAMIN_ITEM_S1_WAS_CONSUMED = 2990; // Ð˜Ñ�Ñ‚Ñ€Ð°Ñ‡ÐµÐ½
    // Ð’Ð¸Ñ‚Ð°Ð¼Ð¸Ð½
    // $s1%
    // Ð²
    // ÐºÐ¾Ð»Ð¸Ñ‡ÐµÑ�Ñ‚Ð²Ðµ
    // $2
    // ÐµÐ´.
    public static final int TRUE_INPUT_MUST_BE_ENTERED_BY_SOMEONE_OVER_15_YEARS_OLD = 2991; // ÐŸÐµÑ‚Ð¸Ñ†Ð¸Ñ�
    // Ð´Ð¾Ð»Ð¶Ð½Ð°
    // Ñ�Ð¾Ð´ÐµÑ€Ð¶Ð°Ñ‚ÑŒ
    // Ð½Ðµ
    // Ð±Ð¾Ð»ÐµÐµ
    // 15
    // Ñ�Ð¸Ð¼Ð²Ð¾Ð»Ð¾Ð².
    public static final int PLEASE_CHOOSE_THE_2ND_STAGE_TYPE = 2992; // Ð’Ñ‹Ð±ÐµÑ€Ð¸Ñ‚Ðµ
    // Ñ‚Ð¸Ð¿ 2-Ð³Ð¾
    // Ñ�Ñ‚Ð°Ð¿Ð°.
    public static final int WHEN_AN_COMMAND_CHANNEL_LEADER_GOES_OUT_OF_THE_COMMAND_CHANNEL_MATCHING_ROOM_THE_CURRENTLY_OPEN = 2993; // ÐŸÑ€Ð¸
    // Ð²Ñ‹Ñ…Ð¾Ð´Ðµ
    // ÐšÐ¾Ð¼Ð°Ð½Ð´Ð¸Ñ€Ð°
    // Ð�Ð»ÑŒÑ�Ð½Ñ�Ð°
    // ÐšÐ¾Ð¼Ð½Ð°Ñ‚Ð°
    // ÐŸÐ¾Ð¸Ñ�ÐºÐ°
    // Ð¡Ð¾ÑŽÐ·Ð°
    // Ð·Ð°ÐºÑ€Ð¾ÐµÑ‚Ñ�Ñ�.
    // Ð¥Ð¾Ñ‚Ð¸Ñ‚Ðµ
    // Ð²Ñ‹Ð¹Ñ‚Ð¸?
    public static final int THE_COMMAND_CHANNEL_MATCHING_ROOM_WAS_CANCELLED = 2994; // ÐšÐ¾Ð¼Ð½Ð°Ñ‚Ð°
    // ÐŸÐ¾Ð¸Ñ�ÐºÐ°
    // Ð¡Ð¾ÑŽÐ·Ð°
    // Ð±Ñ‹Ð»Ð°
    // Ð·Ð°ÐºÑ€Ñ‹Ñ‚Ð°.
    public static final int THIS_COMMAND_CHANNEL_MATCHING_ROOM_IS_ALREADY_CANCELLED = 2995; // Ð­Ñ‚Ð°
    // ÐºÐ¾Ð¼Ð½Ð°Ñ‚Ð°
    // ÑƒÐ¶Ðµ
    // Ð·Ð°ÐºÑ€Ñ‹Ñ‚Ð°.
    public static final int YOU_CANNOT_ENTER_THE_COMMAND_CHANNEL_MATCHING_ROOM_BECAUSE_YOU_DO_NOT_MEET_THE_REQUIREMENTS = 2996; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¿Ð¾Ð´Ñ…Ð¾Ð´Ð¸Ñ‚Ðµ
    // Ð¿Ð¾Ð´
    // ÑƒÑ�Ð»Ð¾Ð²Ð¸Ñ�
    // ÐšÐ¾Ð¼Ð½Ð°Ñ‚Ñ‹
    // ÐŸÐ¾Ð¸Ñ�ÐºÐ°
    // Ð¡Ð¾ÑŽÐ·Ð°.
    public static final int YOU_EXITED_FROM_THE_COMMAND_CHANNEL_MATCHING_ROOM = 2997; // Ð’Ñ‹
    // Ð²Ñ‹ÑˆÐ»Ð¸
    // Ð¸Ð·
    // ÐšÐ¾Ð¼Ð½Ð°Ñ‚Ñ‹
    // ÐŸÐ¾Ð¸Ñ�ÐºÐ°
    // Ð¡Ð¾ÑŽÐ·Ð°.
    public static final int YOU_WERE_EXPELLED_FROM_THE_COMMAND_CHANNEL_MATCHING_ROOM = 2998; // Ð’Ð°Ñ�
    // Ð²Ñ‹Ð³Ð½Ð°Ð»Ð¸
    // Ð¸Ð·
    // ÐšÐ¾Ð¼Ð½Ð°Ñ‚Ñ‹
    // ÐŸÐ¾Ð¸Ñ�ÐºÐ°
    // Ð¡Ð¾ÑŽÐ·Ð°.
    public static final int THE_COMMAND_CHANNEL_AFFILIATED_PARTY_S_PARTY_MEMBER_CANNOT_USE_THE_MATCHING_SCREEN = 2999; // Ð§Ð»ÐµÐ½
    // Ð³Ñ€ÑƒÐ¿Ð¿Ñ‹,
    // Ð²Ñ…Ð¾Ð´Ñ�Ñ‰ÐµÐ¹
    // Ð²
    // ÐšÐ°Ð½Ð°Ð»
    // Ð¡Ð¾ÑŽÐ·Ð°,
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒ
    // Ð¾ÐºÐ½Ð¾
    // Ð¿Ð¾Ð¸Ñ�ÐºÐ°.
    public static final int THE_COMMAND_CHANNEL_MATCHING_ROOM_WAS_CREATED = 3000; // Ð¡Ð¾Ð·Ð´Ð°Ð½Ð°
    // ÐšÐ¾Ð¼Ð½Ð°Ñ‚Ð°
    // ÐŸÐ¾Ð¸Ñ�ÐºÐ°
    // Ð¡Ð¾ÑŽÐ·Ð°.
    public static final int THE_COMMAND_CHANNEL_MATCHING_ROOM_INFORMATION_WAS_EDITED = 3001; // Ð˜Ð·Ð¼ÐµÐ½ÐµÐ½Ð°
    // Ð¸Ð½Ñ„Ð¾Ñ€Ð¼Ð°Ñ†Ð¸Ñ�
    // ÐšÐ¾Ð¼Ð½Ð°Ñ‚Ñ‹
    // ÐŸÐ¾Ð¸Ñ�ÐºÐ°
    // Ð¡Ð¾ÑŽÐ·Ð°.
    public static final int WHEN_THE_RECIPIENT_DOESN_T_EXIST_OR_THE_CHARACTER_HAS_BEEN_DELETED_SENDING_MAIL_IS_NOT_POSSIBLE = 3002; // Ð�ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾
    // Ð¾Ñ‚Ð¿Ñ€Ð°Ð²Ð¸Ñ‚ÑŒ
    // Ð¿Ð¸Ñ�ÑŒÐ¼Ð¾,
    // ÐµÑ�Ð»Ð¸
    // Ð¿Ð¾Ð»ÑƒÑ‡Ð°Ñ‚ÐµÐ»ÑŒ
    // Ð½Ðµ
    // Ñ�ÑƒÑ‰ÐµÑ�Ñ‚Ð²ÑƒÐµÑ‚
    // Ð¸Ð»Ð¸
    // Ð´Ð°Ð½Ð½Ñ‹Ð¹
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶
    // ÑƒÐ´Ð°Ð»ÐµÐ½.
    public static final int C1_ENTERED_THE_COMMAND_CHANNEL_MATCHING_ROOM = 3003; // $c1
    // Ð²Ð¾ÑˆÐµÐ»
    // Ð²
    // ÐšÐ¾Ð¼Ð½Ð°Ñ‚Ñƒ
    // ÐŸÐ¾Ð¸Ñ�ÐºÐ°
    // Ð¡Ð¾ÑŽÐ·Ð°.
    public static final int I_M_SORRY_TO_GIVE_YOU_A_SATISFACTORY_RESPONSE__N__NIF_YOU_SEND_YOUR_COMMENTS_REGARDING_THE = 3004; // Ð˜Ð·Ð²Ð¸Ð½Ð¸Ñ‚Ðµ,
    // Ñ‡Ñ‚Ð¾
    // Ð½Ðµ
    // Ñ�Ð¼Ð¾Ð³Ð»Ð¸
    // Ð´Ð°Ñ‚ÑŒ
    // ÑƒÐ´Ð¾Ð²Ð»ÐµÑ‚Ð²Ð¾Ñ€Ñ�ÑŽÑ‰Ð¸Ð¹
    // Ð’Ð°Ñ�
    // Ð¾Ñ‚Ð²ÐµÑ‚.\\n\\nÐ•Ñ�Ð»Ð¸
    // Ð’Ñ‹
    // Ñ�ÐºÐ°Ð¶ÐµÑ‚Ðµ,
    // Ñ‡Ñ‚Ð¾
    // Ð¸Ð¼ÐµÐ½Ð½Ð¾
    // Ð²Ñ‹Ð·Ð²Ð°Ð»Ð¾
    // Ð½ÐµÐ´Ð¾Ð²Ð¾Ð»ÑŒÑ�Ñ‚Ð²Ð¾,
    // Ð¼Ñ‹
    // Ñ�Ð´ÐµÐ»Ð°ÐµÐ¼
    // Ð²Ñ�Ðµ,
    // Ñ‡Ñ‚Ð¾Ð±Ñ‹
    // Ñ�Ñ‚Ð¾
    // Ð±Ð¾Ð»ÑŒÑˆÐµ
    // Ð½Ðµ
    // Ð¿Ð¾Ð²Ñ‚Ð¾Ñ€Ð¸Ð»Ð¾Ñ�ÑŒ.\\n\\nÐ‘ÑƒÐ´ÐµÐ¼
    // Ñ€Ð°Ð´Ñ‹
    // Ð’Ð°ÑˆÐµÐ¼Ñƒ
    // Ñ�Ð¾Ð¾Ð±Ñ‰ÐµÐ½Ð¸ÑŽ.
    public static final int THIS_SKILL_CANNOT_BE_ENHANCED = 3005; // Ð­Ñ‚Ð¾ ÑƒÐ¼ÐµÐ½Ð¸Ðµ
    // ÑƒÐ»ÑƒÑ‡ÑˆÐ¸Ñ‚ÑŒ
    // Ð½ÐµÐ»ÑŒÐ·Ñ�.
    public static final int NEWLY_USED_PC_CAFE_S1_POINTS_WERE_WITHDRAWN = 3006; // Ð�ÐµÐ¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ð½Ð½Ñ‹Ðµ
    // Ð¾Ñ‡ÐºÐ¸
    // Ð Ð¡,
    // $s1,
    // Ð²ÐµÑ€Ð½ÑƒÐ»Ð¸Ñ�ÑŒ.
    public static final int SHYEED_S_ROAR_FILLED_WITH_WRATH_RINGS_THROUGHOUT_THE_STAKATO_NEST = 3007; // Ð“Ð½ÐµÐ·Ð´Ð¾
    // Ð¡Ñ‚Ð°ÐºÐ°Ñ‚Ð¾
    // Ð½Ð°Ð¿Ð¾Ð»Ð½Ñ�ÐµÑ‚Ñ�Ñ�
    // Ð¿Ð»Ð°Ñ‡ÐµÐ¼
    // Ñ€Ð°Ð·Ð³Ð½ÐµÐ²Ð°Ð½Ð½Ð¾Ð¹
    // Ð¨Ð¸Ð¸Ð´.
    public static final int THE_MAIL_HAS_ARRIVED = 3008; // Ð”Ð¾Ñ�Ñ‚Ð°Ð²Ð»ÐµÐ½Ð° Ð¿Ð¾Ñ�Ñ‹Ð»ÐºÐ°.
    public static final int MAIL_SUCCESSFULLY_SENT = 3009; // Ð’Ñ‹ ÑƒÑ�Ð¿ÐµÑˆÐ½Ð¾
    // Ð¾Ñ‚Ð¿Ñ€Ð°Ð²Ð¸Ð»Ð¸ Ð¿Ð¾Ñ�Ñ‹Ð»ÐºÑƒ.
    public static final int MAIL_SUCCESSFULLY_RETURNED = 3010; // ÐŸÐ¾Ñ�Ñ‹Ð»ÐºÐ° Ð±Ñ‹Ð»Ð°
    // ÑƒÑ�Ð¿ÐµÑˆÐ½Ð¾
    // Ð´Ð¾Ñ�Ñ‚Ð°Ð²Ð»ÐµÐ½Ð°
    // Ð¾Ð±Ñ€Ð°Ñ‚Ð½Ð¾.
    public static final int MAIL_SUCCESSFULLY_CANCELLED = 3011; // Ð’Ñ‹ ÑƒÑ�Ð¿ÐµÑˆÐ½Ð¾
    // Ð¾Ñ‚Ð¼ÐµÐ½Ð¸Ð»Ð¸
    // Ð¾Ñ‚Ð¿Ñ€Ð°Ð²ÐºÑƒ.
    public static final int MAIL_SUCCESSFULLY_RECEIVED = 3012; // ÐŸÐ¾Ñ�Ñ‹Ð»ÐºÐ°
    // ÑƒÑ�Ð¿ÐµÑˆÐ½Ð¾
    // Ð¿Ð¾Ð»ÑƒÑ‡ÐµÐ½Ð°.
    public static final int C1_HAS_SUCCESSFULY_ENCHANTED_A__S2_S3 = 3013; // $c1
    // ÑƒÑ�Ð¿ÐµÑˆÐ½Ð¾
    // Ñ�Ð¾Ð²ÐµÑ€ÑˆÐ¸Ð»
    // ÑƒÐ»ÑƒÑ‡ÑˆÐµÐ½Ð¸Ðµ
    // Ð´Ð¾
    // +$s2$s3.
    public static final int DO_YOU_WISH_TO_ERASE_THE_SELECTED_MAIL = 3014; // Ð’Ñ‹
    // Ñ…Ð¾Ñ‚Ð¸Ñ‚Ðµ
    // ÑƒÐ´Ð°Ð»Ð¸Ñ‚ÑŒ
    // Ð²Ñ‹Ð±Ñ€Ð°Ð½Ð½Ñ‹Ðµ
    // Ð¿Ð¸Ñ�ÑŒÐ¼Ð°?
    public static final int PLEASE_SELECT_THE_MAIL_TO_BE_DELETED = 3015; // Ð’Ñ‹Ð±ÐµÑ€Ð¸Ñ‚Ðµ
    // Ð¿Ð¸Ñ�ÑŒÐ¼Ð°,
    // ÐºÐ¾Ñ‚Ð¾Ñ€Ñ‹Ðµ
    // Ñ…Ð¾Ñ‚Ð¸Ñ‚Ðµ
    // Ñ�Ñ‚ÐµÑ€ÐµÑ‚ÑŒ.
    public static final int ITEM_SELECTION_IS_POSSIBLE_UP_TO_8 = 3016; // ÐœÐ¾Ð¶Ð½Ð¾
    // Ð¿Ñ€Ð¸ÐºÑ€ÐµÐ¿Ð¸Ñ‚ÑŒ
    // Ð´Ð¾ 8
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ð¾Ð²
    // Ð·Ð°
    // Ñ€Ð°Ð·.
    public static final int YOU_CANNOT_USE_ANY_SKILL_ENHANCING_SYSTEM_UNDER_YOUR_STATUS_CHECK_OUT_THE_PC_S_CURRENT_STATUS = 3017; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒ
    // Ñ�Ð¸Ñ�Ñ‚ÐµÐ¼Ñƒ
    // ÑƒÐ»ÑƒÑ‡ÑˆÐµÐ½Ð¸Ñ�
    // ÑƒÐ¼ÐµÐ½Ð¸Ñ�.
    // ÐŸÑ€Ð¾Ð²ÐµÑ€ÑŒÑ‚Ðµ
    // Ñ�Ð¾Ñ�Ñ‚Ð¾Ñ�Ð½Ð¸Ðµ
    // Ð²Ð°ÑˆÐµÐ³Ð¾
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°.
    public static final int YOU_CANNOT_USE_SKILL_ENHANCING_SYSTEM_FUNCTIONS_FOR_THE_SKILLS_CURRENTLY_NOT_ACQUIRED = 3018; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // ÑƒÐ»ÑƒÑ‡ÑˆÐ¸Ñ‚ÑŒ
    // ÑƒÐ¼ÐµÐ½Ð¸Ðµ,
    // ÐºÐ¾Ñ‚Ð¾Ñ€Ñ‹Ð¼
    // Ð½Ðµ
    // Ð¾Ð±Ð»Ð°Ð´Ð°ÐµÑ‚Ðµ.
    public static final int YOU_CANNOT_SEND_A_MAIL_TO_YOURSELF = 3019; // Ð’Ñ‹ Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ð¾Ñ‚Ð¿Ñ€Ð°Ð²Ð¸Ñ‚ÑŒ
    // Ð¿Ð¾Ñ�Ñ‹Ð»ÐºÑƒ
    // Ñ�Ð°Ð¼Ð¾Ð¼Ñƒ
    // Ñ�ÐµÐ±Ðµ.
    public static final int WHEN_NOT_ENTERING_THE_AMOUNT_FOR_THE_PAYMENT_REQUEST_YOU_CANNOT_SEND_ANY_MAIL = 3020; // Ð’Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // Ñ�Ð¾Ð²ÐµÑ€ÑˆÐ¸Ñ‚ÑŒ
    // Ð¾Ñ‚Ð¿Ñ€Ð°Ð²ÐºÑƒ,
    // ÐµÑ�Ð»Ð¸
    // Ð½Ðµ
    // Ð²Ð²ÐµÐ´ÐµÑ‚Ðµ
    // Ñ�ÑƒÐ¼Ð¼Ñƒ
    // Ð¾Ð¿Ð»Ð°Ñ‚Ñ‹.
    public static final int STAND_BY_FOR_THE_GAME_TO_BEGIN = 3021; // Ð¡ÐµÐ¹Ñ‡Ð°Ñ� Ð’Ñ‹
    // Ð¾Ð¶Ð¸Ð´Ð°ÐµÑ‚Ðµ
    // Ð½Ð°Ñ‡Ð°Ð»Ð°
    // Ñ�Ð¾Ñ€ÐµÐ²Ð½Ð¾Ð²Ð°Ð½Ð¸Ð¹.
    public static final int THE_KASHA_S_EYE_GIVES_YOU_A_STRANGE_FEELING = 3022; // Ð§ÑƒÐ²Ñ�Ñ‚Ð²ÑƒÐµÑ‚Ñ�Ñ�
    // Ð¼Ð¾Ñ‰Ð½Ð°Ñ�
    // Ñ�Ð½ÐµÑ€Ð³Ð¸Ñ�,
    // Ð¸Ñ�Ñ‚Ð¾Ñ‡Ð°ÐµÐ¼Ð°Ñ�
    // ÐœÐ¾Ð½Ñ�Ñ‚Ñ€Ð¾Ð³Ð»Ð°Ð·Ð¾Ð¼
    // ÐšÑ…Ð°ÑˆÐ¸.
    public static final int I_CAN_FEEL_THAT_THE_ENERGY_BEING_FLOWN_IN_THE_KASHA_S_EYE_IS_GETTING_STRONGER_RAPIDLY = 3023; // ÐœÐ¾Ð½Ñ�Ñ‚Ñ€Ð¾Ð³Ð»Ð°Ð·
    // ÐšÑ…Ð°ÑˆÐ¸
    // Ð½Ð°Ð±Ð¸Ñ€Ð°ÐµÑ‚Ñ�Ñ�
    // Ñ�Ð¸Ð»
    // Ñ�
    // ÑƒÐ¶Ð°Ñ�Ð°ÑŽÑ‰ÐµÐ¹
    // Ñ�ÐºÐ¾Ñ€Ð¾Ñ�Ñ‚ÑŒÑŽ.
    public static final int KASHA_S_EYE_PITCHES_AND_TOSSES_LIKE_IT_S_ABOUT_TO_EXPLODE = 3024; // ÐœÐ¾Ð½Ñ�Ñ‚Ñ€Ð¾Ð³Ð»Ð°Ð·
    // ÐšÑ…Ð°ÑˆÐ¸
    // Ð²Ð¾Ñ‚-Ð²Ð¾Ñ‚
    // Ð²Ð·Ð¾Ñ€Ð²ÐµÑ‚Ñ�Ñ�.
    public static final int S2_HAS_MADE_A_PAYMENT_OF_S1_ADENA_PER_YOUR_PAYMENT_REQUEST_MAIL = 3025; // $s2
    // Ð·Ð°Ð²ÐµÑ€ÑˆÐ¸Ð»
    // Ð¾Ð¿Ð»Ð°Ñ‚Ñƒ
    // Ð¸
    // Ð²Ñ‹
    // Ð¿Ð¾Ð»ÑƒÑ‡Ð¸Ð»Ð¸
    // $s1
    // Ð�Ð´ÐµÐ½
    public static final int YOU_CANNOT_USE_THE_SKILL_ENHANCING_FUNCTION_ON_THIS_LEVEL_YOU_CAN_USE_THE_CORRESPONDING_FUNCTION = 3026; // Ð�Ð°
    // Ð´Ð°Ð½Ð½Ð¾Ð¼
    // ÑƒÑ€Ð¾Ð²Ð½Ðµ
    // Ð²Ñ‹
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚Ðµ
    // ÑƒÐ»ÑƒÑ‡ÑˆÐ¸Ñ‚ÑŒ
    // ÑƒÐ¼ÐµÐ½Ð¸Ðµ.
    // Ð­Ñ‚Ð°
    // Ñ„ÑƒÐ½ÐºÑ†Ð¸Ñ�
    // Ð´Ð¾Ñ�Ñ‚ÑƒÐ¿Ð½Ð°
    // Ð¸Ð³Ñ€Ð¾ÐºÐ°Ð¼,
    // Ð´Ð¾Ñ�Ñ‚Ð¸Ð³ÑˆÐ¸Ð¼
    // 76-Ð³Ð¾
    // ÑƒÑ€Ð¾Ð²Ð½Ñ�.
    public static final int YOU_CANNOT_USE_THE_SKILL_ENHANCING_FUNCTION_IN_THIS_CLASS_YOU_CAN_USE_CORRESPONDING_FUNCTION = 3027; // Ð’Ð°ÑˆÐ°
    // Ð¿Ñ€Ð¾Ñ„ÐµÑ�Ñ�Ð¸Ñ�
    // Ð½Ðµ
    // Ð¿Ð¾Ð·Ð²Ð¾Ð»Ñ�ÐµÑ‚
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒ
    // Ñ„ÑƒÐ½ÐºÑ†Ð¸ÑŽ
    // ÑƒÐ»ÑƒÑ‡ÑˆÐµÐ½Ð¸Ñ�
    // ÑƒÐ¼ÐµÐ½Ð¸Ñ�.
    // ÐžÐ½Ð°
    // Ð´Ð¾Ñ�Ñ‚ÑƒÐ¿Ð½Ð°
    // Ð¿Ð¾Ñ�Ð»Ðµ
    // 3-Ð¹
    // Ñ�Ð¼ÐµÐ½Ñ‹
    // Ð¿Ñ€Ð¾Ñ„ÐµÑ�Ñ�Ð¸Ð¸.
    public static final int YOU_CANNOT_USE_THE_SKILL_ENHANCING_FUNCTION_IN_THIS_CLASS_YOU_CAN_USE_THE_SKILL_ENHANCING = 3028; // Ð’Ð°ÑˆÐµ
    // Ñ�Ð¾Ñ�Ñ‚Ð¾Ñ�Ð½Ð¸Ðµ
    // Ð½Ðµ
    // Ð¿Ð¾Ð·Ð²Ð¾Ð»Ñ�ÐµÑ‚
    // ÑƒÐ»ÑƒÑ‡ÑˆÐ¸Ñ‚ÑŒ
    // ÑƒÐ¼ÐµÐ½Ð¸Ðµ.
    // Ð­Ñ‚Ð°
    // Ñ„ÑƒÐ½ÐºÑ†Ð¸Ñ�
    // Ð´Ð¾Ñ�Ñ‚ÑƒÐ¿Ð½Ð°,
    // ÐµÑ�Ð»Ð¸
    // Ð²Ñ‹
    // Ð½Ðµ
    // Ð¿ÐµÑ€ÐµÐ²Ð¾Ð¿Ð»Ð¾Ñ‰ÐµÐ½Ñ‹,
    // Ð½Ðµ
    // Ñ�Ñ€Ð°Ð¶Ð°ÐµÑ‚ÐµÑ�ÑŒ
    // Ð¸
    // Ð½Ðµ
    // Ñ�Ð¸Ð´Ð¸Ñ‚Ðµ
    // Ð²ÐµÑ€Ñ…Ð¾Ð¼
    // Ð½Ð°
    // ÐµÐ·Ð´Ð¾Ð²Ð¾Ð¼
    // Ñ�ÑƒÑ‰ÐµÑ�Ñ‚Ð²Ðµ.
    public static final int S1_RETURNED_THE_MAIL = 3029; // $s1 Ð¾Ñ‚Ð¿Ñ€Ð°Ð²Ð¸Ð»
    // Ð¿Ð¾Ñ�Ñ‹Ð»ÐºÑƒ.
    public static final int YOU_CANNOT_CANCEL_SENT_MAIL_SINCE_THE_RECIPIENT_RECEIVED_IT = 3030; // ÐŸÐ¾Ð»ÑƒÑ‡Ð°Ñ‚ÐµÐ»ÑŒ
    // ÑƒÐ¶Ðµ
    // Ð¾Ñ‚ÐºÑ€Ñ‹Ð»
    // Ð¿Ð¾Ñ�Ñ‹Ð»ÐºÑƒ,
    // Ð¿Ð¾Ñ�Ñ‚Ð¾Ð¼Ñƒ
    // Ð¾Ñ‚Ð¼ÐµÐ½Ð¸Ñ‚ÑŒ
    // Ð¾Ñ‚Ð¿Ñ€Ð°Ð²ÐºÑƒ
    // Ð½ÐµÐ»ÑŒÐ·Ñ�.
    public static final int BY_USING_THE_SKILL_OF_EINHASAD_S_HOLY_SWORD_DEFEAT_THE_EVIL_LILIMS = 3031; // Ð’Ð¾Ñ�Ð¿Ð¾Ð»ÑŒÐ·ÑƒÐ¹Ñ‚ÐµÑ�ÑŒ
    // Ñ�Ð²Ñ�Ñ‚Ñ‹Ð¼
    // Ð¼ÐµÑ‡Ð¾Ð¼
    // Ð­Ð¹Ð½Ñ…Ð°Ñ�Ð°Ð´,
    // Ñ‡Ñ‚Ð¾Ð±Ñ‹
    // Ñ€Ð°Ñ�Ð¿Ñ€Ð°Ð²Ð¸Ñ‚ÑŒÑ�Ñ�
    // Ñ�
    // Ð¿Ñ€Ð¸Ñ�Ð¿ÐµÑˆÐ½Ð¸ÐºÐ°Ð¼Ð¸
    // Ð›Ð¸Ð»Ð¸Ð¼!
    public static final int IN_ORDER_TO_HELP_ANAKIM_ACTIVATE_THE_SEALING_DEVICE_OF_THE_EMPEROR_WHO_IS_POSSESED_BY_THE_EVIL = 3032; // Ð§Ñ‚Ð¾Ð±Ñ‹
    // Ð¿Ð¾Ð¼Ð¾Ñ‡ÑŒ
    // Ð�Ð½Ð°ÐºÐ¸Ð¼Ñƒ,
    // Ð°ÐºÑ‚Ð¸Ð²Ð¸Ñ€ÑƒÐ¹Ñ‚Ðµ
    // Ð£Ñ�Ñ‚Ñ€Ð¾Ð¹Ñ�Ñ‚Ð²Ð¾
    // ÐŸÐµÑ‡Ð°Ñ‚Ð¸
    // Ð˜Ð¼Ð¿ÐµÑ€Ð°Ñ‚Ð¾Ñ€Ð°,
    // Ð¿Ñ€Ð¾ÐºÐ»Ñ�Ñ‚Ð¾Ðµ
    // Ð›Ð¸Ð»Ð¸Ñ‚!
    // Ð¡Ð¸Ð»Ð°
    // Ð¿Ñ€Ð¾ÐºÐ»Ñ�Ñ‚Ð¸Ñ�
    // ÑƒÐ¶Ð°Ñ�Ð°ÑŽÑ‰Ð°,
    // Ð¾Ñ�Ñ‚ÐµÑ€ÐµÐ³Ð°Ð¹Ñ‚ÐµÑ�ÑŒ
    // ÐµÐµ!
    public static final int BY_USING_THE_INVISIBLE_SKILL_SNEAK_INTO_THE_DAWN_S_DOCUMENT_STORAGE = 3033; // Ð’Ð¾Ñ�Ð¿Ð¾Ð»ÑŒÐ·ÑƒÐ¹Ñ‚ÐµÑ�ÑŒ
    // ÑƒÐ¼ÐµÐ½Ð¸ÐµÐ¼
    // Ñ�Ñ‚Ñ€Ð°Ð¶Ð°
    // "Ð¡ÐºÑ€Ñ‹Ñ‚ÑŒÑ�Ñ�",
    // Ñ‡Ñ‚Ð¾Ð±Ñ‹
    // Ð¿Ð¾Ð¿Ð°Ñ�Ñ‚ÑŒ
    // Ð²
    // Ð¥Ñ€Ð°Ð½Ð¸Ð»Ð¸Ñ‰Ðµ
    // Ð”Ð¾ÐºÑƒÐ¼ÐµÐ½Ñ‚Ð¾Ð²
    // Ð Ð°Ñ�Ñ�Ð²ÐµÑ‚Ð°!
    public static final int THE_DOOR_IN_FRONT_OF_US_IS_THE_ENTRANCE_TO_THE_DAWN_S_DOCUMENT_STORAGE_APPROACH_TO_THE_CODE = 3034; // ÐŸÑ€Ñ�Ð¼Ð¾
    // Ð¿ÐµÑ€ÐµÐ´
    // Ð²Ð°Ð¼Ð¸
    // Ð‘Ð¸Ð±Ð»Ð¸Ð¾Ñ‚ÐµÐºÐ°
    // Ð Ð°Ñ�Ñ�Ð²ÐµÑ‚Ð°!
    // ÐŸÐ¾Ð´Ð¾Ð¹Ð´Ð¸Ñ‚Ðµ
    // Ðº
    // Ð£Ñ�Ñ‚Ñ€Ð¾Ð¹Ñ�Ñ‚Ð²Ñƒ
    // Ð’Ð²Ð¾Ð´Ð°
    // ÐŸÐ°Ñ€Ð¾Ð»Ñ�!
    public static final int MY_POWER_S_WEAKENING_PLEASE_ACTIVATE_THE_SEALING_DEVICE_POSSESSED_BY_LILITH_S_MAGICAL_CURSE = 3035; // Ð�Ð°ÑˆÐ°
    // Ñ�Ð¸Ð»Ð°
    // Ð¸Ñ�Ñ�Ñ�ÐºÐ°ÐµÑ‚.
    // Ð�ÐºÑ‚Ð¸Ð²Ð¸Ñ€ÑƒÐ¹Ñ‚Ðµ
    // Ð¿Ñ€Ð¾ÐºÐ»Ñ�Ñ‚Ð¾Ðµ
    // Ð›Ð¸Ð»Ð¸Ñ‚
    // Ð£Ñ�Ñ‚Ñ€Ð¾Ð¹Ñ�Ñ‚Ð²Ð¾
    // ÐŸÐµÑ‡Ð°Ñ‚Ð¸!
    public static final int YOU_SUCH_A_FOOL_THE_VICTORY_OVER_THIS_WAR_BELONGS_TO_SHILIEN = 3036; // Ð“Ð»ÑƒÐ¿Ñ†Ñ‹!
    // ÐŸÐ¾Ð±ÐµÐ´Ð°
    // Ð²
    // Ñ�Ñ‚Ð¾Ð¹
    // Ð²Ð¾Ð¹Ð½Ðµ
    // Ð±ÑƒÐ´ÐµÑ‚
    // Ð·Ð°
    // Ð¨Ð¸Ð»ÐµÐ½!
    public static final int MALE_GUARDS_CAN_DETECT_THE_CONCEALMENT_BUT_THE_FEMALE_GUARDS_CANNOT = 3037; // Ð¡Ñ‚Ñ€Ð°Ð¶Ð½Ð¸ÐºÐ¸
    // Ð¼Ð¾Ð³ÑƒÑ‚
    // Ð¾Ð±Ð½Ð°Ñ€ÑƒÐ¶Ð¸Ñ‚ÑŒ
    // Ñ�ÐºÑ€Ñ‹Ð²ÑˆÐ¸Ñ…Ñ�Ñ�
    // Ð¸Ð³Ñ€Ð¾ÐºÐ¾Ð²,
    // Ð°
    // Ñ�Ñ‚Ñ€Ð°Ð¶Ð½Ð¸Ñ†Ñ‹
    // -
    // Ð½ÐµÑ‚.
    public static final int FEMALE_GUARDS_NOTICE_THE_DISGUISES_FROM_FAR_AWAY_BETTER_THAN_THE_MALE_GUARDS_DO_SO_BEWARE = 3038; // Ð¡Ñ‚Ñ€Ð°Ð¶Ð½Ð¸Ñ†Ñ‹
    // Ð¾Ð±Ð½Ð°Ñ€ÑƒÐ¶Ð¸Ð²Ð°ÑŽÑ‚
    // Ð¿ÐµÑ€ÐµÐ²Ð¾Ð¿Ð»Ð¾Ñ‰ÐµÐ½Ð½Ñ‹Ñ…
    // Ð¸Ð³Ñ€Ð¾ÐºÐ¾Ð²
    // Ð½Ð°
    // Ð±Ð¾Ð»ÑŒÑˆÐµÐ¹
    // Ð´Ð¸Ñ�Ñ‚Ð°Ð½Ñ†Ð¸Ð¸,
    // Ñ‡ÐµÐ¼
    // Ñ�Ñ‚Ñ€Ð°Ð¶Ð½Ð¸ÐºÐ¸,
    // Ñ‚Ð°Ðº
    // Ñ‡Ñ‚Ð¾
    // Ð¾Ñ�Ñ‚ÐµÑ€ÐµÐ³Ð°Ð¹Ñ‚ÐµÑ�ÑŒ
    // Ð¸Ñ….
    public static final int BY_USING_THE_HOLY_WATER_OF_EINHASAD_OPEN_THE_DOOR_POSSESSED_BY_THE_CURSE_OF_FLAMES = 3039; // Ð’Ð¾Ñ�Ð¿Ð¾Ð»ÑŒÐ·ÑƒÐ¹Ñ‚ÐµÑ�ÑŒ
    // Ð¡Ð²Ñ�Ñ‚Ð¾Ð¹
    // Ð’Ð¾Ð´Ð¾Ð¹
    // Ð­Ð¹Ð½Ñ…Ð°Ñ�Ð°Ð´,
    // Ñ‡Ñ‚Ð¾Ð±Ñ‹
    // Ð¾Ñ‚ÐºÑ€Ñ‹Ñ‚ÑŒ
    // Ð”Ð²ÐµÑ€ÑŒ
    // ÐžÐ³Ð½ÐµÐ½Ð½Ð¾Ð³Ð¾
    // ÐŸÑ€Ð¾ÐºÐ»Ñ�Ñ‚Ð¸Ñ�.
    public static final int BY_USING_THE_COURT_MAGICIAN_S_MAGIC_STAFF_OPEN_THE_DOOR_ON_WHICH_THE_MAGICIAN_S_BARRIER_IS = 3040; // Ð’Ð¾Ñ�Ð¿Ð¾Ð»ÑŒÐ·ÑƒÐ¹Ñ‚ÐµÑ�ÑŒ
    // Ð¡ÐºÐ¸Ð¿ÐµÑ‚Ñ€Ð¾Ð¼
    // ÐšÐ¾Ñ€Ð¾Ð»ÐµÐ²Ñ�ÐºÐ¾Ð³Ð¾
    // Ð’Ð¾Ð»ÑˆÐµÐ±Ð½Ð¸ÐºÐ°,
    // Ñ‡Ñ‚Ð¾Ð±Ñ‹
    // Ð¾Ñ‚ÐºÑ€Ñ‹Ñ‚ÑŒ
    // Ð“Ñ€Ð°Ð½ÑŒ
    // Ð ÐµÐ°Ð»ÑŒÐ½Ð¾Ñ�Ñ‚Ð¸
    // Ð’Ð¾Ð»ÑˆÐµÐ±Ð½Ð¸ÐºÐ°.
    public static final int AROUND_FIFTEEN_HUNDRED_YEARS_AGO_THE_LANDS_WERE_RIDDLED_WITH_HERETICS = 3041; // ÐŸÐ¾Ð»Ñ‚Ð¾Ñ€Ñ‹
    // Ñ‚Ñ‹Ñ�Ñ�Ñ‡Ð¸
    // Ð»ÐµÑ‚
    // Ð½Ð°Ð·Ð°Ð´
    // Ð±Ñ‹Ð»Ð¾
    // Ð²Ñ€ÐµÐ¼Ñ�,
    // ÐºÐ¾Ð³Ð´Ð°
    // Ð¿Ð¾Ñ�Ð»ÐµÐ´Ð¾Ð²Ð°Ñ‚ÐµÐ»Ð¸\\nÐ±Ð¾Ð³Ð¸Ð½Ð¸
    // Ð¨Ð¸Ð»ÐµÐ½
    public static final int WORSHIPPERS_OF_SHILEN_THE_GODDESS_OF_DEATH = 3042; // Ð½Ð°Ð·Ñ‹Ð²Ð°Ð»Ð¸
    // Ñ�ÐµÐ±Ñ�
    // Ð”ÐµÑ‚ÑŒÐ¼Ð¸
    // Ð¨Ð¸Ð»ÐµÐ½.
    // Ð˜Ñ…
    // Ñ�Ð¸Ð»Ð°
    // Ð±Ñ‹Ð»Ð°
    // Ñ‡Ñ€ÐµÐ·Ð²Ñ‹Ñ‡Ð°Ð¹Ð½Ð¾\\nÐ²ÐµÐ»Ð¸ÐºÐ°.
    public static final int BUT_A_MIRACLE_HAPPENED_AT_THE_ENTHRONEMENT_OF_SHUNAIMAN_THE_FIRST_EMPEROR = 3043; // Ð’
    // Ñ�Ñ‚Ð¾
    // Ñ�Ð¼ÑƒÑ‚Ð½Ð¾Ðµ
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ð¿Ñ€Ð¾Ñ…Ð¾Ð´Ð¸Ð»Ð°
    // ÐºÐ¾Ñ€Ð¾Ð½Ð°Ñ†Ð¸Ñ�
    // Ð¿ÐµÑ€Ð²Ð¾Ð³Ð¾
    // Ð¸Ð¼Ð¿ÐµÑ€Ð°Ñ‚Ð¾Ñ€Ð°\\nÐ­Ð»ÑŒÐ¼Ð¾Ñ€ÐµÐ´ÐµÐ½Ð°
    // Ð¨ÑƒÐ½Ð°Ð¹Ð¼Ð°Ð½Ð°.
    public static final int ANAKIM_AN_ANGEL_OF_EINHASAD_CAME_DOWN_FROM_THE_SKIES = 3044; // Ð’Ð¾
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ñ�Ñ‚Ð¾Ð¹
    // ÐºÐ¾Ñ€Ð¾Ð½Ð°Ñ†Ð¸Ð¸
    // Ð¿Ñ€Ð¾Ð¸Ð·Ð¾ÑˆÐ»Ð¾
    // Ñ‡ÑƒÐ´Ð¾.
    // Ð¡
    // Ð½ÐµÐ±Ð°
    // Ñ�Ð¿ÑƒÑ�Ñ‚Ð¸Ð»Ð°Ñ�ÑŒ
    public static final int SURROUNDED_BY_SACRED_FLAMES_AND_THREE_PAIRS_OF_WINGS = 3045; // ÑˆÐµÑ�Ñ‚Ð¸ÐºÑ€Ñ‹Ð»Ð°Ñ�
    // Ð¿Ð¾Ñ�Ð»Ð°Ð½Ð½Ð¸Ñ†Ð°
    // Ð­Ð¹Ð½Ñ…Ð°Ñ�Ð°Ð´,
    // Ð�Ð½Ð°ÐºÐ¸Ð¼.
    public static final int THUS_EMPOWERED_THE_EMPEROR_LAUNCHED_A_WAR_AGAINST__SHILEN_S_PEOPLE_ = 3046; // Ð˜Ð¼Ð¿ÐµÑ€Ð°Ñ‚Ð¾Ñ€
    // Ð¾Ð±Ñ€ÐµÐ»
    // Ð²ÐµÐ»Ð¸ÐºÑƒÑŽ
    // Ñ�Ð¸Ð»Ñƒ
    // Ð¸
    // Ð½Ð°Ñ‡Ð°Ð»
    // Ñ�Ð²Ñ�Ñ‚ÑƒÑŽ
    // Ð²Ð¾Ð¹Ð½Ñƒ
    // Ð¿Ñ€Ð¾Ñ‚Ð¸Ð²\\nÐ”ÐµÑ‚ÐµÐ¹
    // Ð¨Ð¸Ð»ÐµÐ½.
    public static final int THE_EMPEROR_S_ARMY_LED_BY_ANAKIM_ATTACKED__SHILEN_S_PEOPLE__RELENTLESSLY = 3047; // Ð¥Ð¾Ñ‚Ñ�
    // Ð°Ñ€Ð¼Ð¸Ñ�
    // Ð¸Ð¼Ð¿ÐµÑ€Ð°Ñ‚Ð¾Ñ€Ð°,
    // Ð¿Ñ€ÐµÐ¸Ñ�Ð¿Ð¾Ð»Ð½ÐµÐ½Ð½Ð°Ñ�
    // Ð´ÑƒÑ…Ð¾Ð¼
    // Ð�Ð½Ð°ÐºÐ¸Ð¼Ð°,\\nÑ�
    // Ð»ÐµÐ³ÐºÐ¾Ñ�Ñ‚ÑŒÑŽ
    // Ð¿Ð¾Ð±ÐµÐ´Ð¸Ð»Ð°
    // Ð”ÐµÑ‚ÐµÐ¹
    // Ð¨Ð¸Ð»ÐµÐ½,
    public static final int BUT_IN_THE_END_SOME_SURVIVORS_MANAGED_TO_HIDE_IN_UNDERGROUND_CATACOMBS = 3048; // Ð¼Ð½Ð¾Ð³Ð¸Ðµ
    // Ð¸Ð·
    // Ð½Ð¸Ñ…
    // Ð²Ñ‹Ð¶Ð¸Ð»Ð¸
    // Ð¸
    // Ñ�Ð¿Ñ€Ñ�Ñ‚Ð°Ð»Ð¸Ñ�ÑŒ
    // Ð²
    // ÐºÐ°Ñ‚Ð°ÐºÐ¾Ð¼Ð±Ð°Ñ…
    // Ñ…Ñ€Ð°Ð¼Ð°.
    public static final int A_NEW_LEADER_EMERGED_LILITH_WHO_SEEKED_TO_SUMMON_SHILEN_FROM_THE_AFTERLIFE = 3049; // Ð’Ñ‹Ð¶Ð¸Ð²ÑˆÐ¸Ðµ
    // Ð”ÐµÑ‚Ð¸
    // Ð¨Ð¸Ð»ÐµÐ½
    // Ð¾Ð±Ñ€ÐµÐ»Ð¸
    // Ñ�Ð²Ð¾ÐµÐ³Ð¾
    // Ð½Ð¾Ð²Ð¾Ð³Ð¾
    // Ð»Ð¸Ð´ÐµÑ€Ð°,
    // Ð›Ð¸Ð»Ð¸Ñ‚,\\nÐ²Ð¾Ð·Ð³Ð»Ð°Ð²Ð¸Ð²ÑˆÑƒÑŽ
    // Ð¸Ñ…
    // Ñ�
    // Ñ†ÐµÐ»ÑŒÑŽ
    // Ð²Ð¾Ñ�ÐºÑ€ÐµÑ�Ð¸Ñ‚ÑŒ
    // Ð¨Ð¸Ð»ÐµÐ½
    public static final int AND_TO_REBUILD_THE_LILIM_ARMY_WITHIN_THE_EIGHT_NECROPOLISES = 3050; // Ð¸
    // Ñ�Ð¾Ð·Ð´Ð°Ð²ÑˆÑƒÑŽ
    // Ñ�Ð²Ð¾ÑŽ
    // Ð°Ñ€Ð¼Ð¸ÑŽ
    // Ð²
    // Ð²Ð¾Ñ�ÑŒÐ¼Ð¸
    // Ð½ÐµÐºÑ€Ð¾Ð¿Ð¾Ð»Ñ�Ñ….
    public static final int NOW_IN_THE_MIDST_OF_IMPENDING_WAR_THE_MERCHANT_OF_MAMMON_STRUCK_A_DEAL = 3051; // Ð’
    // Ñ�Ñ‚Ð¾
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ð¢Ð¾Ñ€Ð³Ð¾Ð²Ñ†Ñ‹
    // ÐœÐ°Ð¼Ð¼Ð¾Ð½Ð°,
    // Ñ€Ð°Ð´Ð¸
    // Ð´ÐµÐ½ÐµÐ³
    // Ð³Ð¾Ñ‚Ð¾Ð²Ñ‹Ðµ
    // Ð½Ð°
    // Ð²Ñ�Ðµ,
    public static final int HE_SUPPLIES_SHUNAIMAN_WITH_WAR_FUNDS_IN_EXCHANGE_FOR_PROTECTION = 3052; // Ð·Ð°ÐºÐ»ÑŽÑ‡Ð¸Ð»Ð¸
    // Ñ�
    // Ð¸Ð¼Ð¿ÐµÑ€Ð°Ñ‚Ð¾Ñ€Ð¾Ð¼
    // Ñ�ÐµÐºÑ€ÐµÑ‚Ð½Ñ‹Ð¹
    // Ð´Ð¾Ð³Ð¾Ð²Ð¾Ñ€.
    public static final int AND_RIGHT_NOW_THE_DOCUMENT_WE_RE_LOOKING_FOR_IS_THAT_CONTRACT = 3053; // Ð˜
    // Ð¸Ð¼ÐµÐ½Ð½Ð¾
    // Ñ�Ñ‚Ð¾Ñ‚
    // Ð´Ð¾Ð³Ð¾Ð²Ð¾Ñ€
    // Ð¼Ñ‹
    // Ð¸
    // Ð´Ð¾Ð»Ð¶Ð½Ñ‹
    // Ñ€Ð°Ð·Ñ‹Ñ�ÐºÐ°Ñ‚ÑŒ.
    public static final int FINALLY_YOU_RE_HERE_I_M_ANAKIM_I_NEED_YOUR_HELP = 3054; // Ð�Ð°ÐºÐ¾Ð½ÐµÑ†-Ñ‚Ð¾
    // Ð²Ñ‹
    // Ð¿Ñ€Ð¸ÑˆÐ»Ð¸!
    // Ð¯
    // Ð¶Ð´Ð°Ð»Ð°
    // Ð²Ð°Ñ�!
    public static final int IT_S_THE_SEAL_DEVICES_I_NEED_YOU_TO_DESTROY_THEM_WHILE_I_DISTRACT_LILITH = 3055; // ÐŸÐ¾ÐºÐ°
    // Ñ�
    // Ð±ÑƒÐ´Ñƒ
    // Ñ�Ð´ÐµÑ€Ð¶Ð¸Ð²Ð°Ñ‚ÑŒ
    // Ð›Ð¸Ð»Ð¸Ñ‚,
    // Ð°ÐºÑ‚Ð¸Ð²Ð¸Ð·Ð¸Ñ€ÑƒÐ¹Ñ‚Ðµ
    // Ð¼ÐµÑ…Ð°Ð½Ð¸Ð·Ð¼\\nÐŸÐµÑ‡Ð°Ñ‚Ð¸!
    public static final int PLEASE_HURRY_I_DON_T_HAVE_MUCH_TIME_LEFT = 3056; // Ð¯
    // Ð½Ðµ
    // Ñ�Ð¼Ð¾Ð³Ñƒ
    // Ð´Ð¾Ð»Ð³Ð¾
    // ÐµÐµ
    // Ñ�Ð´ÐµÑ€Ð¶Ð¸Ð²Ð°Ñ‚ÑŒ.
    // Ð¢Ð¾Ñ€Ð¾Ð¿Ð¸Ñ‚ÐµÑ�ÑŒ!
    public static final int FOR_EINHASAD = 3057; // Ð—Ð° Ð­Ð¹Ð½Ñ…Ð°Ñ�Ð°Ð´!
    public static final int EMBRYO = 3058; // Ð­Ð¼Ð±... Ñ€Ð¸Ð¾â€¦
    public static final int S1_DID_NOT_RECEIVE_IT_DURING_THE_WAITING_TIME_SO_IT_WAS_RETURNED_AUTOMATICALLY_TNTLS = 3059; // $s1
    // Ð½Ðµ
    // Ð¿Ð¾Ð»ÑƒÑ‡Ð¸Ð»
    // Ð¿Ð¾Ñ�Ñ‹Ð»ÐºÑƒ
    // Ð²
    // ÑƒÑ�Ñ‚Ð°Ð½Ð¾Ð²Ð»ÐµÐ½Ð½Ð¾Ðµ
    // Ð²Ñ€ÐµÐ¼Ñ�,
    // Ð¿Ð¾Ñ�Ñ‚Ð¾Ð¼Ñƒ
    // Ð¾Ð½Ð°
    // Ð²Ð¾Ð·Ð²Ñ€Ð°Ñ‰ÐµÐ½Ð°.
    public static final int THE_SEALING_DEVICE_GLITTERS_AND_MOVES_ACTIVATION_COMPLETE_NORMALLY = 3060; // Ð£Ñ�Ñ‚Ñ€Ð¾Ð¹Ñ�Ñ‚Ð²Ð¾
    // ÐŸÐµÑ‡Ð°Ñ‚Ð¸
    // Ð·Ð°Ð¿ÑƒÑ‰ÐµÐ½Ð¾.
    // ÐœÐµÑ…Ð°Ð½Ð¸Ð·Ð¼
    // Ð²
    // Ð¿Ð¾Ñ€Ñ�Ð´ÐºÐµ!
    public static final int THERE_COMES_A_SOUND_OF_OPENING_THE_HEAVY_DOOR_FROM_SOMEWHERE = 3061; // Ð”Ð¾
    // Ð’Ð°Ñ�
    // Ð´Ð¾Ð½Ð¾Ñ�Ð¸Ñ‚Ñ�Ñ�
    // Ð·Ð²ÑƒÐº
    // Ð¾Ñ‚ÐºÑ€Ñ‹Ð²Ð°ÑŽÑ‰Ð¸Ñ…Ñ�Ñ�
    // Ð´Ð²ÐµÑ€ÐµÐ¹.
    public static final int DO_YOU_WANT_TO_PAY_S1_ADENA = 3062; // Ð—Ð°Ð¿Ð»Ð°Ñ‚Ð¸Ñ‚ÑŒ $s1
    // Ð°Ð´ÐµÐ½?
    public static final int DO_YOU_REALLY_WANT_TO_FORWARD = 3063; // Ð’ÐµÑ€Ð½ÑƒÑ‚ÑŒ
    // Ð¿Ð¾Ñ�Ñ‹Ð»ÐºÑƒ?
    public static final int THERE_IS_AN_UNREAD_MAIL = 3064; // Ð£ Ð²Ð°Ñ� ÐµÑ�Ñ‚ÑŒ
    // Ð½ÐµÐ¿Ñ€Ð¾Ñ‡Ñ‚ÐµÐ½Ð½Ð¾Ðµ
    // Ð¿Ð¸Ñ�ÑŒÐ¼Ð¾.
    public static final int CURRENT_LOCATION__INSIDE_THE_CHAMBER_OF_DELUSION = 3065; // ÐšÐ¾Ð¾Ñ€Ð´Ð¸Ð½Ð°Ñ‚Ñ‹:
    // Ð’Ð½ÑƒÑ‚Ñ€Ð¸
    // Ð“Ñ€Ð°Ð½Ð¸
    // Ð ÐµÐ°Ð»ÑŒÐ½Ð¾Ñ�Ñ‚Ð¸
    public static final int YOU_CANNOT_USE_THE_MAIL_FUNCTION_OUTSIDE_THE_PEACE_ZONE = 3066; // Ð¤ÑƒÐ½ÐºÑ†Ð¸Ð¸
    // Ð¿Ð¾Ñ‡Ñ‚Ñ‹
    // Ð¼Ð¾Ð¶Ð½Ð¾
    // Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒ
    // Ñ‚Ð¾Ð»ÑŒÐºÐ¾
    // Ð²
    // Ð¼Ð¸Ñ€Ð½Ð¾Ð¹
    // Ð·Ð¾Ð½Ðµ.
    public static final int S1_CANCELED_THE_SENT_MAIL = 3067; // $s1 Ð¾Ñ‚Ð¼ÐµÐ½Ð¸Ð»
    // Ð¾Ñ‚Ð¿Ñ€Ð°Ð²ÐºÑƒ
    // Ð¿Ð¾Ñ�Ñ‹Ð»ÐºÐ¸.
    public static final int THE_MAIL_WAS_RETURNED_DUE_TO_THE_EXCEEDED_WAITING_TIME = 3068; // Ð’Ñ€ÐµÐ¼Ñ�
    // Ð¾Ð¶Ð¸Ð´Ð°Ð½Ð¸Ñ�
    // Ð¿Ñ€ÐµÐ²Ñ‹ÑˆÐµÐ½Ð¾,
    // Ð¿Ð¾Ñ�Ñ‚Ð¾Ð¼Ñƒ
    // Ð¿Ð¾Ñ�Ñ‹Ð»ÐºÐ°
    // Ð¾Ñ‚Ð¿Ñ€Ð°Ð²Ð»ÐµÐ½Ð°
    // Ð¾Ð±Ñ€Ð°Ñ‚Ð½Ð¾.
    public static final int DO_YOU_REALLY_WANT_TO_CANCEL_THE_TRANSACTION = 3069; // ÐžÑ‚Ð¼ÐµÐ½Ð¸Ñ‚ÑŒ
    // Ñ�Ð´ÐµÐ»ÐºÑƒ?
    public static final int SKILL_NOT_AVAILABLE_TO_BE_ENHANCED_CHECK_SKILL_S_LV_AND_CURRENT_PC_STATUS = 3070; // Ð­Ñ‚Ð¾
    // ÑƒÐ¼ÐµÐ½Ð¸Ðµ
    // ÑƒÐ»ÑƒÑ‡ÑˆÐ¸Ñ‚ÑŒ
    // Ð½ÐµÐ»ÑŒÐ·Ñ�.
    // ÐŸÑ€Ð¾Ð²ÐµÑ€ÑŒÑ‚Ðµ
    // ÑƒÑ€Ð¾Ð²ÐµÐ½ÑŒ
    // ÑƒÐ¼ÐµÐ½Ð¸Ñ�
    // Ð¸
    // Ñ�Ð¾Ñ�Ñ‚Ð¾Ñ�Ð½Ð¸Ðµ
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°.
    public static final int DO_YOU_REALLY_WANT_TO_RESET_1000000010_MILLION_ADENA_WILL_BE_CONSUMED = 3071; // ÐŸÐµÑ€ÐµÐ·Ð°Ð¿ÑƒÑ�Ñ‚Ð¸Ñ‚ÑŒ?
    // Ð‘ÑƒÐ´ÐµÑ‚
    // Ð¸Ñ�Ñ‚Ñ€Ð°Ñ‡ÐµÐ½Ð¾
    // 10,000,000
    // Ð°Ð´ÐµÐ½.
    public static final int S1_ACQUIRED_THE_ATTACHED_ITEM_TO_YOUR_MAIL = 3072; // $s1
    // Ð¿Ð¾Ð»ÑƒÑ‡Ð¸Ð»
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚,
    // Ð¿Ñ€Ð¸ÐºÑ€ÐµÐ¿Ð»ÐµÐ½Ð½Ñ‹Ð¹
    // Ðº
    // Ð¿Ð¸Ñ�ÑŒÐ¼Ñƒ.
    public static final int YOU_HAVE_ACQUIRED_S2_S1 = 3073; // Ð’Ñ‹ Ð¿Ð¾Ð»ÑƒÑ‡Ð¸Ð»Ð¸ $s1,
    // $s2 ÐµÐ´.
    public static final int THE_ALLOWED_LENGTH_FOR_RECIPIENT_EXCEEDED = 3074; // Ð’Ñ‹
    // Ð¿Ñ€ÐµÐ²Ñ‹Ñ�Ð¸Ð»Ð¸
    // ÑƒÑ�Ñ‚Ð°Ð½Ð¾Ð²Ð»ÐµÐ½Ð½ÑƒÑŽ
    // Ð´Ð»Ð¸Ð½Ñƒ
    // Ð¸Ð¼ÐµÐ½Ð¸
    // Ð¿Ð¾Ð»ÑƒÑ‡Ð°Ñ‚ÐµÐ»Ñ�.
    public static final int THE_ALLOWED_LENGTH_FOR_A_TITLE_EXCEEDED = 3075; // Ð’Ñ‹
    // Ð¿Ñ€ÐµÐ²Ñ‹Ñ�Ð¸Ð»Ð¸
    // ÑƒÑ�Ñ‚Ð°Ð½Ð¾Ð²Ð»ÐµÐ½Ð½ÑƒÑŽ
    // Ð´Ð»Ð¸Ð½Ñƒ
    // Ð½Ð°Ð·Ð²Ð°Ð½Ð¸Ñ�.
    public static final int _THE_ALLOWED_LENGTH_FOR_A_TITLE_EXCEEDED = 3076; // Ð’Ñ‹
    // Ð¿Ñ€ÐµÐ²Ñ‹Ñ�Ð¸Ð»Ð¸
    // Ñ€Ð°Ð·Ñ€ÐµÑˆÐµÐ½Ð½Ñ‹Ð¹
    // Ð¾Ð±ÑŠÐµÐ¼
    // Ñ�Ð¾Ð´ÐµÑ€Ð¶Ð°Ð½Ð¸Ñ�.
    public static final int THE_MAIL_LIMIT_240_OF_THE_OPPONENT_S_CHARACTER_HAS_BEEN_EXCEEDED_AND_THIS_CANNOT_BE_FORWARDED = 3077; // Ð£
    // Ð¿Ð¾Ð»ÑƒÑ‡Ð°Ñ‚ÐµÐ»Ñ�
    // Ð¿ÐµÑ€ÐµÐ¿Ð¾Ð»Ð½ÐµÐ½
    // Ð¿Ð¾Ñ‡Ñ‚Ð¾Ð²Ñ‹Ð¹
    // Ñ�Ñ‰Ð¸Ðº
    // (240
    // ÐµÐ´.),
    // Ð¿Ð¾Ñ�Ñ‚Ð¾Ð¼Ñƒ
    // Ð¾Ñ‚Ð¿Ñ€Ð°Ð²ÐºÐ°
    // Ð½ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð°.
    public static final int YOU_RE_MAKING_A_REQUEST_FOR_PAYMENT_DO_YOU_WANT_TO_PROCEED = 3078; // ÐžÐ¿Ð»Ð°Ñ‚Ð°.
    // ÐžÑ‚Ð¿Ñ€Ð°Ð²Ð¸Ñ‚ÑŒ
    // Ð´ÐµÐ½ÑŒÐ³Ð¸?
    public static final int THERE_ARE_ITEMS_IN_THE_PET_INVENTORY_SO_YOU_CANNOT_REGISTER_AS_AN_INDIVIDUAL_STORE_OR_DROP_ITEMS = 3079; // Ð’
    // Ð¸Ð½Ð²ÐµÐ½Ñ‚Ð°Ñ€Ðµ
    // ÐŸÐ¸Ñ‚Ð¾Ð¼Ñ†Ð°
    // Ð¸Ð¼ÐµÑŽÑ‚Ñ�Ñ�
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ñ‹,
    // Ð¿Ð¾Ñ�Ñ‚Ð¾Ð¼Ñƒ
    // Ð¾Ð±Ð¼ÐµÐ½Ñ�Ñ‚ÑŒ
    // ÐµÐ³Ð¾,
    // Ð²Ñ‹Ñ�Ñ‚Ð°Ð²Ð¸Ñ‚ÑŒ
    // Ð½Ð°
    // Ð¿Ñ€Ð¾Ð´Ð°Ð¶Ñƒ
    // Ð²
    // Ð»Ð¸Ñ‡Ð½Ð¾Ð¹
    // Ñ‚Ð¾Ñ€Ð³Ð¾Ð²Ð¾Ð¹
    // Ð»Ð°Ð²ÐºÐµ
    // Ð¸Ð»Ð¸
    // Ð¿Ñ€Ð¾Ð³Ð½Ð°Ñ‚ÑŒ
    // Ð½ÐµÐ»ÑŒÐ·Ñ�.
    // Ð¡Ð½Ð°Ñ‡Ð°Ð»Ð°
    // Ð¾Ñ‡Ð¸Ñ�Ñ‚Ð¸Ñ‚Ðµ
    // Ð¸Ð½Ð²ÐµÐ½Ñ‚Ð°Ñ€ÑŒ
    // ÐŸÐ¸Ñ‚Ð¾Ð¼Ñ†Ð°.
    public static final int YOU_CANNOT_RESET_THE_SKILL_LINK_BECAUSE_THERE_IS_NOT_ENOUGH_ADENA = 3080; // Ð£
    // Ð²Ð°Ñ�
    // Ð½Ðµ
    // Ñ…Ð²Ð°Ñ‚Ð°ÐµÑ‚
    // Ð°Ð´ÐµÐ½,
    // Ñ‡Ñ‚Ð¾Ð±Ñ‹
    // Ñ�Ð±Ñ€Ð¾Ñ�Ð¸Ñ‚ÑŒ
    // Ð½Ð°Ñ�Ñ‚Ñ€Ð¾Ð¹ÐºÐ¸
    // Ñ�Ñ€Ð»Ñ‹ÐºÐ¾Ð²
    // ÑƒÐ¼ÐµÐ½Ð¸Ð¹.
    public static final int YOU_CANNOT_RECEIVE_IT_BECAUSE_YOU_ARE_UNDER_THE_CONDITION_THAT_THE_OPPONENT_CANNOT_ACQUIRE_ANY = 3081; // ÐŸÐ°Ñ€Ñ‚Ð½ÐµÑ€
    // Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ð¿Ð¾Ð»ÑƒÑ‡Ð¸Ñ‚ÑŒ
    // Ð´ÐµÐ½ÑŒÐ³Ð¸,
    // Ð¿Ð¾Ñ�Ñ‚Ð¾Ð¼Ñƒ
    // ÐºÑƒÐ¿Ð¸Ñ‚ÑŒ
    // Ð´Ð°Ð½Ð½Ñ‹Ð¹
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚
    // Ð½ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾.
    public static final int YOU_CANNOT_SEND_MAILS_TO_ANY_CHARACTER_THAT_HAS_BLOCKED_YOU = 3082; // ÐžÑ‚Ð¿Ñ€Ð°Ð²Ð¸Ñ‚ÑŒ
    // Ð¿Ð¾Ñ�Ñ‹Ð»ÐºÑƒ
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ñƒ,
    // ÐºÐ¾Ñ‚Ð¾Ñ€Ñ‹Ð¹
    // Ð²Ð°Ñ�
    // Ð·Ð°Ð±Ð»Ð¾ÐºÐ¸Ñ€Ð¾Ð²Ð°Ð»,
    // Ð½ÐµÐ»ÑŒÐ·Ñ�.
    public static final int IN_THE_PROCESS_OF_WORKING_ON_THE_PREVIOUS_CLAN_DECLARATION_RETREAT_PLEASE_TRY_AGAIN_LATER = 3083; // Ð˜Ð´ÐµÑ‚
    // Ð¾Ð±Ñ€Ð°Ð±Ð¾Ñ‚ÐºÐ°
    // Ð¸Ð½Ñ„Ð¾Ñ€Ð¼Ð°Ñ†Ð¸Ð¸
    // Ð¾
    // Ð¿Ñ€Ð¾ÑˆÐ»Ð¾Ð¹
    // Ð²Ð¾Ð¹Ð½Ðµ
    // ÐºÐ»Ð°Ð½Ð¾Ð².
    // ÐŸÐ¾Ð²Ñ‚Ð¾Ñ€Ð¸Ñ‚Ðµ
    // Ð¿Ð¾Ð¿Ñ‹Ñ‚ÐºÑƒ
    // Ð¿Ð¾Ð·Ð¶Ðµ.
    public static final int CURRENTLY_WE_ARE_IN_THE_PROCESS_OF_CHOOSING_A_HERO_PLEASE_TRY_AGAIN_LATER = 3084; // Ð˜Ð´ÐµÑ‚
    // Ð¾Ð±Ñ€Ð°Ð±Ð¾Ñ‚ÐºÐ°
    // Ð¾
    // Ð²Ñ‹Ð±Ð¾Ñ€Ðµ
    // Ð³ÐµÑ€Ð¾Ñ�.
    // ÐŸÐ¾Ð²Ñ‚Ð¾Ñ€Ð¸Ñ‚Ðµ
    // Ð¿Ð¾Ð¿Ñ‹Ñ‚ÐºÑƒ
    // Ð¿Ð¾Ð·Ð¶Ðµ.
    public static final int YOU_CAN_SUMMON_THE_PET_YOU_ARE_TRYING_TO_SUMMON_NOW_ONLY_WHEN_YOU_OWN_AN_AGIT = 3085; // Ð’Ñ‹Ð·Ð²Ð°Ñ‚ÑŒ
    // Ñ�Ñ‚Ð¾Ð³Ð¾
    // ÐŸÐ¸Ñ‚Ð¾Ð¼Ñ†Ð°
    // Ð¼Ð¾Ð¶Ð½Ð¾
    // Ñ‚Ð¾Ð»ÑŒÐºÐ¾
    // Ð¿Ñ€Ð¸
    // Ð½Ð°Ð»Ð¸Ñ‡Ð¸Ð¸
    // Ð¥Ð¾Ð»Ð»Ð°
    // ÐšÐ»Ð°Ð½Ð°.
    public static final int WOULD_YOU_LIKE_TO_GIVE_S2_S1 = 3086; // Ð’Ñ‹ Ñ…Ð¾Ñ‚Ð¸Ñ‚Ðµ
    // Ð¿ÐµÑ€ÐµÐ´Ð°Ñ‚ÑŒ
    // Ð¸Ð³Ñ€Ð¾ÐºÑƒ $s1
    // $s2?
    public static final int THIS_MAIL_IS_BEING_SENT_WITH_A_PAYMENT_REQUEST_WOULD_YOU_LIKE_TO_CONTINUE = 3087; // Ð­Ñ‚Ð¾
    // Ñ„ÑƒÐ½ÐºÑ†Ð¸Ñ�
    // Ð¿Ñ€ÐµÐ´Ð½Ð°Ð·Ð½Ð°Ñ‡ÐµÐ½Ð°
    // Ð´Ð»Ñ�
    // Ð¿ÐµÑ€ÐµÑ‡Ð¸Ñ�Ð»ÐµÐ½Ð¸Ñ�
    // Ð´ÐµÐ½ÐµÐ³.
    // Ð’Ñ‹
    // Ñ�Ð¾Ð³Ð»Ð°Ñ�Ð½Ñ‹
    // Ð¿Ñ€Ð¾Ð´Ð¾Ð»Ð¶Ð¸Ñ‚ÑŒ?
    public static final int YOU_HAVE_S1_HOURS_S2_MINUTES_AND_S3_SECONDS_LEFT_IN_THE_PROOF_OF_SPACE_AND_TIME_IF_AGATHION_IS = 3088; // Ð”Ð¾
    // Ð¿Ð¾Ð²Ñ‚Ð¾Ñ€Ð½Ð¾Ð³Ð¾
    // Ð¿Ñ€ÐµÐ´Ð¾Ñ�Ñ‚Ð°Ð²Ð»ÐµÐ½Ð¸Ñ�
    // Ð¡Ð²Ð¸Ð´ÐµÑ‚ÐµÐ»ÑŒÑ�Ñ‚Ð²Ð°
    // ÐŸÑ€Ð¾Ñ�Ñ‚Ñ€Ð°Ð½Ñ�Ñ‚Ð²Ð°
    // Ð¾Ñ�Ñ‚Ð°Ð»Ð¾Ñ�ÑŒ
    // $s1Ñ‡Ð°Ñ�.
    // $s2Ð¼Ð¸Ð½.
    // $s3Ñ�ÐµÐº.
    // (Ð•Ñ�Ð»Ð¸
    // Ð²
    // Ð´Ð°Ð½Ð½Ñ‹Ð¹
    // Ð¿ÐµÑ€Ð¸Ð¾Ð´
    // Ð¿Ð¾Ð²Ñ‚Ð¾Ñ€Ð½Ð¾
    // Ð¿Ñ€Ð¸Ð·Ð²Ð°Ñ‚ÑŒ
    // Ð�Ð³Ð°Ñ‚Ð¸Ð¾Ð½Ð°,
    // Ñ‚Ð¾
    // Ð¼Ð¾Ð¶Ð½Ð¾
    // Ð¿Ñ€Ð¾Ð´Ð»Ð¸Ñ‚ÑŒ
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ð½Ð°
    // 10
    // Ð¼Ð¸Ð½.)
    public static final int YOU_HAVE_S1_MINUTES_AND_S2_SECONDS_LEFT_IN_THE_PROOF_OF_SPACE_AND_TIME_IF_AGATHION_IS_SUMMONED = 3089; // Ð”Ð¾
    // Ð¿Ð¾Ð²Ñ‚Ð¾Ñ€Ð½Ð¾Ð³Ð¾
    // Ð¿Ñ€ÐµÐ´Ð¾Ñ�Ñ‚Ð°Ð²Ð»ÐµÐ½Ð¸Ñ�
    // Ð¡Ð²Ð¸Ð´ÐµÑ‚ÐµÐ»ÑŒÑ�Ñ‚Ð²Ð°
    // ÐŸÑ€Ð¾Ñ�Ñ‚Ñ€Ð°Ð½Ñ�Ñ‚Ð²Ð°
    // Ð¾Ñ�Ñ‚Ð°Ð»Ð¾Ñ�ÑŒ
    // $s1Ð¼Ð¸Ð½.
    // $s2Ñ�ÐµÐº.
    // (Ð•Ñ�Ð»Ð¸
    // Ð²
    // Ð´Ð°Ð½Ð½Ñ‹Ð¹
    // Ð¿ÐµÑ€Ð¸Ð¾Ð´
    // Ð¿Ð¾Ð²Ñ‚Ð¾Ñ€Ð½Ð¾
    // Ð¿Ñ€Ð¸Ð·Ð²Ð°Ñ‚ÑŒ
    // Ð�Ð³Ð°Ñ‚Ð¸Ð¾Ð½Ð°,
    // Ñ‚Ð¾
    // Ð¼Ð¾Ð¶Ð½Ð¾
    // Ð¿Ñ€Ð¾Ð´Ð»Ð¸Ñ‚ÑŒ
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ð½Ð°
    // 10
    // Ð¼Ð¸Ð½.)
    public static final int YOU_HAVE_S1_SECONDS_LEFT_IN_THE_PROOF_OF_SPACE_AND_TIME_IF_AGATHION_IS_SUMMONED_WITHIN_THIS_TIME = 3090; // Ð”Ð¾
    // Ð¿Ð¾Ð²Ñ‚Ð¾Ñ€Ð½Ð¾Ð³Ð¾
    // Ð¿Ñ€ÐµÐ´Ð¾Ñ�Ñ‚Ð°Ð²Ð»ÐµÐ½Ð¸Ñ�
    // Ð¡Ð²Ð¸Ð´ÐµÑ‚ÐµÐ»ÑŒÑ�Ñ‚Ð²Ð°
    // ÐŸÑ€Ð¾Ñ�Ñ‚Ñ€Ð°Ð½Ñ�Ñ‚Ð²Ð°
    // Ð¾Ñ�Ñ‚Ð°Ð»Ð¾Ñ�ÑŒ
    // $s1Ñ�ÐµÐº.
    // (Ð•Ñ�Ð»Ð¸
    // Ð²
    // Ð´Ð°Ð½Ð½Ñ‹Ð¹
    // Ð¿ÐµÑ€Ð¸Ð¾Ð´
    // Ð¿Ð¾Ð²Ñ‚Ð¾Ñ€Ð½Ð¾
    // Ð¿Ñ€Ð¸Ð·Ð²Ð°Ñ‚ÑŒ
    // Ð�Ð³Ð°Ñ‚Ð¸Ð¾Ð½Ð°,
    // Ñ‚Ð¾
    // Ð¼Ð¾Ð¶Ð½Ð¾
    // Ð¿Ñ€Ð¾Ð´Ð»Ð¸Ñ‚ÑŒ
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ð½Ð°
    // 10
    // Ð¼Ð¸Ð½.)
    public static final int YOU_CANNOT_DELETE_CHARACTERS_ON_THIS_SERVER_RIGHT_NOW = 3091; // Ð’
    // Ð½Ð°Ñ�Ñ‚Ð¾Ñ�Ñ‰ÐµÐµ
    // Ð²Ñ€ÐµÐ¼Ñ�
    // Ð½Ð°
    // Ð´Ð°Ð½Ð½Ð¾Ð¼
    // Ñ�ÐµÑ€Ð²ÐµÑ€Ðµ
    // ÑƒÐ´Ð°Ð»ÐµÐ½Ð¸Ðµ
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶Ð°
    // Ð½ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾.
    public static final int TRANSACTION_COMPLETED = 3092; // Ð¡Ð´ÐµÐ»ÐºÐ° Ð·Ð°Ð²ÐµÑ€ÑˆÐµÐ½Ð°.
    public static final int YOU_ARE_PROTECTED_AGGRESSIVE_MONSTERS = 3108;
    public static final int YOU_ACQUIRED_S1_EXP_AND_S2_SP_AS_A_REWARD_YOU_RECEIVE_S3_MORE_EXP = 6011; // Ð’Ñ‹
    // Ð¿Ð¾Ð»ÑƒÑ‡Ð¸Ð»Ð¸
    // $s1
    // Ð¾Ñ‡ÐºÐ¾Ð²
    // Ð¾Ð¿Ñ‹Ñ‚Ð°
    // Ð¸
    // $s2
    // SP.
    // (Ð’
    // Ð½Ð°Ð³Ñ€Ð°Ð´Ñƒ
    // Ð²Ñ‹
    // Ð´Ð¾Ð¿Ð¾Ð»Ð½Ð¸Ñ‚ÐµÐ»ÑŒÐ½Ð¾
    // Ð¿Ð¾Ð»ÑƒÑ‡Ð°ÐµÑ‚Ðµ
    // $s3
    // Ð¾Ñ‡ÐºÐ¾Ð²
    // Ð¾Ð¿Ñ‹Ñ‚Ð°.)
    public static final int A_BLESSING_THAT_INCREASES_EXP_BY_1_2 = 6012; // ÐšÐ¾Ð»Ð¸Ñ‡ÐµÑ�Ñ‚Ð²Ð¾
    // Ð¾Ñ‡ÐºÐ¾Ð²
    // Ð¾Ð¿Ñ‹Ñ‚Ð°
    // ÑƒÐ²ÐµÐ»Ð¸Ñ‡Ð¸Ð²Ð°ÐµÑ‚Ñ�Ñ�
    // Ð½Ð°
    // $s1
    // $s2
    public static final int IT_IS_NOT_A_BLESSING_PERIOD_WHEN_YOU_REACH_TODAY_S_TARGET_YOU_CAN_RECEIVE_S1 = 6013; // Ð¡ÐµÐ¹Ñ‡Ð°Ñ�
    // Ð˜Ð²ÐµÐ½Ñ‚
    // Ð½Ðµ
    // Ð¿Ñ€Ð¾Ð²Ð¾Ð´Ð¸Ñ‚Ñ�Ñ�.
    // Ð’Ð°Ð¼
    // Ð½ÐµÐ¾Ð±Ñ…Ð¾Ð´Ð¸Ð¼Ð¾
    // Ð´Ð¾Ñ�Ñ‚Ð¸Ñ‡ÑŒ
    // Ñ�ÐµÐ³Ð¾Ð´Ð½Ñ�ÑˆÐ½ÑŽÑŽ
    // Ñ†ÐµÐ»ÑŒ,
    // Ð¸
    // Ñ‚Ð¾Ð³Ð´Ð°
    // Ð²Ñ‹
    // Ð¿Ð¾Ð»ÑƒÑ‡Ð¸Ñ‚Ðµ
    // Ð½Ð°Ð³Ñ€Ð°Ð´Ñƒ:
    // $s1.
    public static final int IT_IS_EVA_S_BLESSING_PERIOD_S1_WILL_BE_EFFECTIVE_UNTIL_S2 = 6014; // Ð”Ð¾Ñ�Ñ‚ÑƒÐ¿Ð½Ð¾
    // Ð±Ð»Ð°Ð³Ð¾Ñ�Ð»Ð¾Ð²ÐµÐ½Ð¸Ðµ
    // Ð•Ð²Ñ‹.
    // Ð’Ñ‹
    // Ð¿Ð¾Ð»ÑƒÑ‡Ð°ÐµÑ‚Ðµ
    // $s1
    // Ð´Ð¾
    // $s2.
    public static final int IT_IS_EVA_S_BLESSING_PERIOD_UNTIL_S1_JACK_SAGE_CAN_GIFT_YOU_WITH_S2 = 6015; // Ð”Ð¾Ñ�Ñ‚ÑƒÐ¿Ð½Ð¾
    // Ð±Ð»Ð°Ð³Ð¾Ñ�Ð»Ð¾Ð²ÐµÐ½Ð¸Ðµ
    // Ð•Ð²Ñ‹.
    // Ð”Ð¾
    // $s1
    // Ð¡Ñ‚Ð¸Ð²
    // Ð²Ñ‹Ð´Ð°Ñ�Ñ‚
    // $s2.
    public static final int PROGRESS__EVENT_STAGE_S1 = 6016; // Ð’Ñ‹Ð¿Ð¾Ð»Ð½ÐµÐ½Ð¸Ðµ
    // ($s1-Ð¹ Ð´ÐµÐ½ÑŒ
    // Ð˜Ð²ÐµÐ½Ñ‚Ð°)
    public static final int EVA_S_BLESSING_STAGE_S1_HAS_BEGUN = 6017; // Ð�Ð°Ñ‡Ð¸Ð½Ð°ÐµÑ‚Ñ�Ñ�
    // $s1-Ð¹
    // Ð´ÐµÐ½ÑŒ
    // Ð¸Ð²ÐµÐ½Ñ‚Ð°
    // Ð‘Ð»Ð°Ð³Ð¾Ñ�Ð»Ð¾Ð²ÐµÐ½Ð¸Ðµ
    // Ð•Ð²Ñ‹.
    public static final int EVA_S_BLESSING_STAGE_S1_HAS_ENDED = 6018; // Ð—Ð°ÐºÐ°Ð½Ñ‡Ð¸Ð²Ð°ÐµÑ‚Ñ�Ñ�
    // $s1-Ð¹
    // Ð´ÐµÐ½ÑŒ
    // Ð¸Ð²ÐµÐ½Ñ‚Ð°
    // Ð‘Ð»Ð°Ð³Ð¾Ñ�Ð»Ð¾Ð²ÐµÐ½Ð¸Ðµ
    // Ð•Ð²Ñ‹.
    public static final int YOU_CANNOT_BUY_THE_ITEM_ON_THIS_DAY_OF_THE_WEEK = 6019; // Ð’
    // Ñ�Ñ‚Ð¾Ñ‚
    // Ð´ÐµÐ½ÑŒ
    // Ð½ÐµÐ´ÐµÐ»Ð¸
    // Ð¿Ð¾ÐºÑƒÐ¿ÐºÐ°
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ð°
    // Ð½ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð°.
    public static final int YOU_CANNOT_BUY_THE_ITEM_AT_THIS_HOUR = 6020; // Ð’
    // Ð´Ð°Ð½Ð½Ñ‹Ð¹
    // Ð¿ÐµÑ€Ð¸Ð¾Ð´
    // Ð¿Ð¾ÐºÑƒÐ¿ÐºÐ°
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ð°
    // Ð½ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð°.
    public static final int S1_REACHED_S2_CONSECUTIVE_WINS_IN_JACK_GAME = 6021; // ÐŸÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶
    // $s1
    // Ð¿Ð¾Ð±ÐµÐ´Ð¸Ð»
    // $s2
    // Ñ€Ð°Ð·
    // Ð²
    // Ð¸Ð³Ñ€Ðµ
    // Ð”Ð¶ÐµÐºÐ°.
    public static final int S1_RECEIVED_S4_S3_AS_REWARD_FOR_S2_CONSECUTIVE_WINS = 6022; // ÐŸÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶
    // $s1
    // Ð¿Ð¾Ð»ÑƒÑ‡Ð¸Ð»
    // Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚
    // $s3
    // ($s4
    // ÑˆÑ‚.)
    // Ð²
    // Ð½Ð°Ð³Ñ€Ð°Ð´Ñƒ
    // Ð·Ð°
    // Ñ‚Ð¾,
    // Ñ‡Ñ‚Ð¾
    // Ð¿Ð¾Ð±ÐµÐ´Ð¸Ð»
    // $s2
    // Ñ€Ð°Ð·.
    public static final int WORLD__S1_CONSECUTIVE_WINS_S2_PPL = 6023; // ÐœÐ¸Ñ€:
    // ÐŸÐ¾Ð±ÐµÐ´ -
    // $s1
    // ($s2
    // Ñ‡ÐµÐ».)
    public static final int MY_RECORD__S1_CONSECUTIVE_WINS = 6024; // Ð’Ñ‹: Ð¿Ð¾Ð±ÐµÐ´:
    // $s1
    public static final int WORLD__UNDER_4_CONSECUTIVE_WINS = 6025; // ÐœÐ¸Ñ€:
    // Ð¼ÐµÐ½ÑŒÑˆÐµ 4
    // Ð¿Ð¾Ð±ÐµÐ´
    public static final int MY_RECORD__UNDER_4_CONSECUTIVE_WINS = 6026; // Ð’Ñ‹:
    // Ð¼ÐµÐ½ÑŒÑˆÐµ
    // 4
    // Ð¿Ð¾Ð±ÐµÐ´
    public static final int IT_S_HALLOWEEN_EVENT_PERIOD = 6027; // ÐŸÐµÑ€Ð¸Ð¾Ð´ Ð˜Ð²ÐµÐ½Ñ‚Ð°
    // Ð¥ÐµÐ»Ð»Ð¾ÑƒÐ¸Ð½Ð°.
    public static final int NO_RECORD_OVER_10_CONSECUTIVE_WINS = 6028; // Ð�ÐµÑ‚
    // Ð·Ð°Ð¿Ð¸Ñ�ÐµÐ¹
    // Ñ�Ð²Ñ‹ÑˆÐµ
    // 10
    // Ð¿Ð¾Ð±ÐµÐ´.
    public static final int C1_IS_SET_TO_REFUSE_COUPLE_ACTIONS = 3164; // ÐŸÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶
    // $c1 Ð½Ðµ
    // Ð¼Ð¾Ð¶ÐµÑ‚
    // Ñ�Ð¾Ð²ÐµÑ€ÑˆÐ°Ñ‚ÑŒ
    // Ð¿Ð°Ñ€Ð½Ñ‹Ñ…
    // Ð´ÐµÐ¹Ñ�Ñ‚Ð²Ð¸Ð¹,
    // Ð·Ð°Ð¿Ñ€Ð¾Ñ�
    // Ð½Ð°
    // ÐŸÐ°Ñ€Ð½Ð¾Ðµ
    // Ð´ÐµÐ¹Ñ�Ñ‚Ð²Ð¸Ðµ
    // Ð½ÐµÐ´Ð¾Ñ�Ñ‚ÑƒÐ¿ÐµÐ½.

    public static final int YOU_HAVE_EXCEEDED_THE_CORRECT_CALCULATION_RANGE_PLEASE_ENTER_AGAIN = 3093; // Ð’Ñ‹
    // Ð¿Ñ€ÐµÐ²Ñ‹Ñ�Ð¸Ð»Ð¸
    // Ð»Ð¸Ð¼Ð¸Ñ‚
    // Ñ†ÐµÐ½Ñ‹.
    // ÐŸÐ¾Ð¶Ð°Ð»ÑƒÐ¹Ñ�Ñ‚Ð°,
    // Ð²Ð²ÐµÐ´Ð¸Ñ‚Ðµ
    // Ñ†ÐµÐ½Ñƒ
    // ÐµÑ‰Ðµ
    // Ñ€Ð°Ð·.
    public static final int PLEASE_HELP_RAISE_REINDEER_FOR_SANTA_S_CHRISTMAS_DELIVERY = 6029; // ÐŸÐ¾Ð·Ð°Ð±Ð¾Ñ‚ÑŒÑ‚ÐµÑ�ÑŒ
    // Ð¾
    // Ð ÑƒÐ´Ð¾Ð»ÑŒÑ„Ðµ
    // Ð¸
    // Ð²Ñ‹Ñ€Ð°Ñ�Ñ‚Ð¸Ñ‚Ðµ
    // ÐµÐ³Ð¾!
    // ÐžÐ½
    // Ð½ÑƒÐ¶ÐµÐ½
    // Ð´Ð»Ñ�
    // Ð´Ð¾Ñ�Ñ‚Ð°Ð²ÐºÐ¸
    // Ð½Ð¾Ð²Ð¾Ð³Ð¾Ð´Ð½Ð¸Ñ…
    // Ð¿Ð¾Ð´Ð°Ñ€ÐºÐ¾Ð².
    public static final int SANTA_HAS_STARTED_DELIVERING_THE_CHRISTMAS_GIFTS_TO_ADEN = 6030; // Ð¡ÐµÐ´Ð¾Ð±Ð¾Ñ€Ð¾Ð´Ñ‹Ð¹
    // Ð”ÐµÐ´
    // ÐœÐ¾Ñ€Ð¾Ð·
    // Ð½Ð°Ñ‡Ð¸Ð½Ð°ÐµÑ‚
    // Ñ€Ð°Ð·Ð²Ð¾Ð·Ð¸Ñ‚ÑŒ
    // Ð½Ð¾Ð²Ð¾Ð³Ð¾Ð´Ð½Ð¸Ðµ
    // Ð¿Ð¾Ð´Ð°Ñ€ÐºÐ¸
    // Ð¿Ð¾
    // Ð¼Ð¸Ñ€Ñƒ
    // Ð�Ð´ÐµÐ½.
    public static final int SANTA_HAS_COMPLETED_THE_DELIVERIES_SEE_YOU_IN_1_HOUR = 6031; // Ð¡ÐµÐ´Ð¾Ð±Ð¾Ñ€Ð¾Ð´Ñ‹Ð¹
    // Ð”ÐµÐ´
    // ÐœÐ¾Ñ€Ð¾Ð·
    // Ñ€Ð°Ð·Ð²ÐµÐ·
    // Ð²Ñ�Ðµ
    // Ð¿Ð¾Ð´Ð°Ñ€ÐºÐ¸.
    // Ð£Ð²Ð¸Ð´ÐµÐ¼Ñ�Ñ�
    // Ñ‡ÐµÑ€ÐµÐ·
    // 1
    // Ñ‡Ð°Ñ�.
    public static final int SANTA_IS_OUT_DELIVERING_THE_GIFTS_MERRY_CHRISTMAS = 6032; // Ð¡ÐµÐ´Ð¾Ð±Ð¾Ñ€Ð¾Ð´Ñ‹Ð¹
    // Ð”ÐµÐ´
    // ÐœÐ¾Ñ€Ð¾Ð·
    // Ñ€Ð°Ð·Ð²Ð¾Ð·Ð¸Ñ‚
    // Ð½Ð¾Ð²Ð¾Ð³Ð¾Ð´Ð½Ð¸Ðµ
    // Ð¿Ð¾Ð´Ð°Ñ€ÐºÐ¸.
    // Ð¡
    // Ð�Ð¾Ð²Ñ‹Ð¼
    // Ð³Ð¾Ð´Ð¾Ð¼!
    public static final int ONLY_THE_TOP_S1_APPEAR_IN_THE_RANKING_AND_ONLY_THE_TOP_S2_ARE_RECORDED_IN_MY_BEST_RANKING = 6033; // Ð’
    // Ñ€ÐµÐ¹Ñ‚Ð¸Ð½Ð³Ðµ
    // Ð¾Ñ‚Ð¾Ð±Ñ€Ð°Ð¶Ð°ÐµÑ‚Ñ�Ñ�
    // Ð´Ð¾
    // $s1
    // Ñ‡ÐµÐ».,
    // Ð°
    // Ñ�Ð°Ð¼Ñ‹Ðµ
    // Ð¿ÐµÑ€Ð²Ñ‹Ðµ
    // $s2
    // Ñ‡ÐµÐ».
    // Ð·Ð°Ð¿Ð¸Ñ�Ñ‹Ð²Ð°ÑŽÑ‚Ñ�Ñ�
    // Ð²
    // ÐœÐ¾Ð¹
    // Ð²Ñ‹Ñ�ÑˆÐ¸Ð¹
    // Ñ€ÐµÐ¹Ñ‚Ð¸Ð½Ð³.
    public static final int S1_HAVE_HAS_BEEN_INITIALIZED = 6034; // $s1
    // Ð¾Ð±Ð½ÑƒÐ»Ð¸Ð»Ð¾Ñ�ÑŒ.
    public static final int WHEN_THERE_ARE_MANY_PLAYERS_WITH_THE_SAME_SCORE_THEY_APPEAR_IN_THE_ORDER_IN_WHICH_THEY_WERE = 6035; // Ð•Ñ�Ð»Ð¸
    // Ð¾Ð´Ð¸Ð½Ð°ÐºÐ¾Ð²Ð¾Ðµ
    // ÐºÐ¾Ð»Ð¸Ñ‡ÐµÑ�Ñ‚Ð²Ð¾
    // Ð¾Ñ‡ÐºÐ¾Ð²
    // Ð½Ð°Ð±Ñ€Ð°Ð»Ð¾
    // Ð½ÐµÑ�ÐºÐ¾Ð»ÑŒÐºÐ¾
    // Ð¿ÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶ÐµÐ¹,
    // Ñ‚Ð¾
    // Ð²Ñ‹ÑˆÐµ
    // Ð¾Ñ‚Ð¾Ð±Ñ€Ð°Ð·Ð¸Ñ‚Ñ�Ñ�
    // Ñ‚Ð¾Ñ‚,
    // ÐºÑ‚Ð¾
    // Ð½Ð°Ð±Ñ€Ð°Ð»
    // Ð¸Ñ…
    // Ñ€Ð°Ð½ÑŒÑˆÐµ.
    public static final int BELOW_S1_POINTS = 6036; // ÐœÐµÐ½ÑŒÑˆÐµ $s1 Ð¾Ñ‡ÐºÐ¾Ð²

    public static final int YOU_CANCEL_FOR_COUPLE_ACTION = 3119; // Ð’Ñ‹
    // Ð¾Ñ‚ÐºÐ°Ð·Ð°Ð»Ð¸Ñ�ÑŒ
    // Ð¾Ñ‚ ÐŸÐ°Ñ€Ð½Ð¾Ð³Ð¾
    // Ð´ÐµÐ¹Ñ�Ñ‚Ð²Ð¸Ñ�.
    public static final int REQUEST_CANNOT_COMPLETED_BECAUSE_TARGET_DOES_NOT_MEET_LOCATION_REQUIREMENTS = 3120; // Ð—Ð°Ð¿Ñ€Ð¾Ñ�
    // Ð½ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶ÐµÐ½.
    // ÐœÐµÑ�Ñ‚Ð¾Ñ€Ð°Ñ�Ð¿Ð¾Ð»Ð¾Ð¶ÐµÐ½Ð¸Ðµ
    // Ñ†ÐµÐ»Ð¸
    // Ð½Ðµ
    // Ñ�Ð¾Ð¾Ñ‚Ð²ÐµÑ‚Ñ�Ñ‚Ð²ÑƒÐµÑ‚
    // ÑƒÑ�Ð»Ð¾Ð²Ð¸Ñ�Ð¼.
    public static final int COUPLE_ACTION_WAS_CANCELED = 3121; // ÐŸÐ°Ñ€Ð½Ð¾Ðµ
    // Ð´ÐµÐ¹Ñ�Ñ‚Ð²Ð¸Ðµ
    // Ð¾Ñ‚Ð¼ÐµÐ½ÐµÐ½Ð¾.
    public static final int COUPLE_ACTION_CANNOT_C1_TARGET_IN_PRIVATE_STORE = 3123; // Ð—Ð°Ð¿Ñ€Ð¾Ñ�
    // Ð¾
    // ÐŸÐ°Ñ€Ð½Ð¾Ð¼
    // Ð´ÐµÐ¹Ñ�Ñ‚Ð²Ð¸Ð¸
    // Ð½ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶ÐµÐ½.
    // $c1
    // Ð·Ð°Ð½Ñ�Ñ‚
    // Ð»Ð¸Ñ‡Ð½Ð¾Ð¹
    // Ð»Ð°Ð²ÐºÐ¾Ð¹
    // Ð¸Ð»Ð¸
    // Ð»Ð¸Ñ‡Ð½Ð¾Ð¹
    // Ð¼Ð°Ñ�Ñ‚ÐµÑ€Ñ�ÐºÐ¾Ð¹.
    public static final int COUPLE_ACTION_CANNOT_C1_TARGET_IS_FISHING = 3124; // Ð—Ð°Ð¿Ñ€Ð¾Ñ�
    // Ð¾
    // ÐŸÐ°Ñ€Ð½Ð¾Ð¼
    // Ð´ÐµÐ¹Ñ�Ñ‚Ð²Ð¸Ð¸
    // Ð½ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶ÐµÐ½.
    // $c1
    // Ð·Ð°Ð½Ñ�Ñ‚
    // Ð»Ð¾Ð²Ð»ÐµÐ¹
    // Ñ€Ñ‹Ð±Ñ‹.
    public static final int COUPLE_ACTION_CANNOT_C1_TARGET_IS_IN_COMBAT = 3125; // Ð—Ð°Ð¿Ñ€Ð¾Ñ�
    // Ð¾
    // ÐŸÐ°Ñ€Ð½Ð¾Ð¼
    // Ð´ÐµÐ¹Ñ�Ñ‚Ð²Ð¸Ð¸
    // Ð½ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶ÐµÐ½.
    // $c1
    // Ð½Ð°Ñ…Ð¾Ð´Ð¸Ñ‚Ñ�Ñ�
    // Ð²
    // Ð±Ð¾ÑŽ.
    public static final int COUPLE_ACTION_CANNOT_C1_TARGET_IN_ANOTHER_COUPLE_ACTION = 3126; // Ð—Ð°Ð¿Ñ€Ð¾Ñ�
    // Ð¾
    // ÐŸÐ°Ñ€Ð½Ð¾Ð¼
    // Ð´ÐµÐ¹Ñ�Ñ‚Ð²Ð¸Ð¸
    // Ð½ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶ÐµÐ½.
    // $c1
    // ÑƒÐ¶Ðµ
    // Ñ�Ð¾Ð³Ð»Ð°Ñ�Ð¸Ð»Ñ�Ñ�
    // Ð½Ð°
    // Ð´Ñ€ÑƒÐ³Ð¾Ðµ
    // ÐŸÐ°Ñ€Ð½Ð¾Ðµ
    // Ð´ÐµÐ¹Ñ�Ñ‚Ð²Ð¸Ðµ.
    public static final int COUPLE_ACTION_CANNOT_C1_TARGET_IS_CURSED_WEAPON_EQUIPED = 3127; // Ð—Ð°Ð¿Ñ€Ð¾Ñ�
    // Ð¾
    // ÐŸÐ°Ñ€Ð½Ð¾Ð¼
    // Ð´ÐµÐ¹Ñ�Ñ‚Ð²Ð¸Ð¸
    // Ð½ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶ÐµÐ½.
    // $c1
    // Ð½Ð°Ñ…Ð¾Ð´Ð¸Ñ‚Ñ�Ñ�
    // Ð²
    // Ñ�Ð¾Ñ�Ñ‚Ð¾Ñ�Ð½Ð¸Ð¸
    // Ð¥Ð°Ð¾Ñ�Ð°.
    public static final int COUPLE_ACTION_CANNOT_C1_TARGET_IS_IN_OLYMPIAD = 3128; // Ð—Ð°Ð¿Ñ€Ð¾Ñ�
    // Ð¾
    // ÐŸÐ°Ñ€Ð½Ð¾Ð¼
    // Ð´ÐµÐ¹Ñ�Ñ‚Ð²Ð¸Ð¸
    // Ð½ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶ÐµÐ½.
    // $c1
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð²ÑƒÐµÑ‚
    // Ð²
    // ÐžÐ»Ð¸Ð¼Ð¿Ð¸Ð°Ð´Ðµ.
    public static final int COUPLE_ACTION_CANNOT_C1_TARGET_IS_IN_HIDEOUT_SIEGE = 3129; // Ð—Ð°Ð¿Ñ€Ð¾Ñ�
    // Ð¾
    // ÐŸÐ°Ñ€Ð½Ð¾Ð¼
    // Ð´ÐµÐ¹Ñ�Ñ‚Ð²Ð¸Ð¸
    // Ð½ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶ÐµÐ½.
    // $c1
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð²ÑƒÐµÑ‚
    // Ð²
    // Ð±Ð¸Ñ‚Ð²Ðµ
    // Ð·Ð°
    // Ñ…Ð¾Ð»Ð»
    // ÐºÐ»Ð°Ð½Ð°.
    public static final int COUPLE_ACTION_CANNOT_C1_TARGET_IS_IN_SIEGE = 3130; // Ð—Ð°Ð¿Ñ€Ð¾Ñ�
    // Ð¾
    // ÐŸÐ°Ñ€Ð½Ð¾Ð¼
    // Ð´ÐµÐ¹Ñ�Ñ‚Ð²Ð¸Ð¸
    // Ð½ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶ÐµÐ½.
    // $c1
    // ÑƒÑ‡Ð°Ñ�Ñ‚Ð²ÑƒÐµÑ‚
    // Ð²
    // Ð¾Ñ�Ð°Ð´Ðµ
    // Ð·Ð°Ð¼ÐºÐ°.
    public static final int COUPLE_ACTION_CANNOT_C1_TARGET_IS_IN_VEHICLE_MOUNT_OTHER = 3131; // Ð—Ð°Ð¿Ñ€Ð¾Ñ�
    // Ð¾
    // ÐŸÐ°Ñ€Ð½Ð¾Ð¼
    // Ð´ÐµÐ¹Ñ�Ñ‚Ð²Ð¸Ð¸
    // Ð½ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶ÐµÐ½.
    // $c1
    // Ñ�ÐµÐ¹Ñ‡Ð°Ñ�
    // Ð½Ð°
    // ÐºÐ¾Ñ€Ð°Ð±Ð»Ðµ,
    // Ð²ÐµÑ€Ñ…Ð¾Ð¼
    // Ð½Ð°
    // Ð¿Ð¸Ñ‚Ð¾Ð¼Ñ†Ðµ
    // Ð¸
    // Ñ‚.Ð¿..
    public static final int COUPLE_ACTION_CANNOT_C1_TARGET_IS_TELEPORTING = 3132; // Ð—Ð°Ð¿Ñ€Ð¾Ñ�
    // Ð¾
    // ÐŸÐ°Ñ€Ð½Ð¾Ð¼
    // Ð´ÐµÐ¹Ñ�Ñ‚Ð²Ð¸Ð¸
    // Ð½ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶ÐµÐ½.
    // $c1
    // Ð²
    // Ñ�Ð¾Ñ�Ñ‚Ð¾Ñ�Ð½Ð¸Ð¸
    // Ñ‚ÐµÐ»ÐµÐ¿Ð¾Ñ€Ñ‚Ð°Ñ†Ð¸Ð¸.
    public static final int COUPLE_ACTION_CANNOT_C1_TARGET_IS_IN_TRANSFORM = 3133; // Ð—Ð°Ð¿Ñ€Ð¾Ñ�
    // Ð¾
    // ÐŸÐ°Ñ€Ð½Ð¾Ð¼
    // Ð´ÐµÐ¹Ñ�Ñ‚Ð²Ð¸Ð¸
    // Ð½ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶ÐµÐ½.
    // $c1
    // Ð²
    // Ñ�Ð¾Ñ�Ñ‚Ð¾Ñ�Ð½Ð¸Ð¸
    // Ð¿ÐµÑ€ÐµÐ²Ð¾Ð¿Ð»Ð¾Ñ‰ÐµÐ½Ð¸Ñ�.
    public static final int COUPLE_ACTION_CANNOT_C1_TARGET_IS_DEAD = 3139; // Ð—Ð°Ð¿Ñ€Ð¾Ñ�
    // Ð¾
    // ÐŸÐ°Ñ€Ð½Ð¾Ð¼
    // Ð´ÐµÐ¹Ñ�Ñ‚Ð²Ð¸Ð¸
    // Ð½ÐµÐ²Ð¾Ð·Ð¼Ð¾Ð¶ÐµÐ½.
    // ÐŸÐµÑ€Ñ�Ð¾Ð½Ð°Ð¶
    // $c1
    // Ð¼ÐµÑ€Ñ‚Ð².
    public static final int YOU_ASK_FOR_COUPLE_ACTION_C1 = 3150; // Ð’Ñ‹ Ð½Ð°Ð¿Ñ€Ð°Ð²Ð¸Ð»Ð¸
    // Ð·Ð°Ð¿Ñ€Ð¾Ñ� Ð½Ð°
    // ÐŸÐ°Ñ€Ð½Ð¾Ðµ
    // Ð´ÐµÐ¹Ñ�Ñ‚Ð²Ð¸Ðµ
    // Ð¸Ð³Ñ€Ð¾ÐºÑƒ $c1.
    public static final int C1_ACCEPTED_COUPLE_ACTION = 3151; // $c1 Ñ�Ð¾Ð³Ð»Ð°Ñ�Ð¸Ð»Ñ�Ñ�
    // Ð½Ð° ÐŸÐ°Ñ€Ð½Ð¾Ðµ
    // Ð´ÐµÐ¹Ñ�Ñ‚Ð²Ð¸Ðµ.
    public static final int REQUESTING_APPROVAL_CHANGE_PARTY_LOOT_S1 = 3135; // Requesting
    // approval
    // for
    // changing
    // party
    // loot
    // to
    // "$s1".
    public static final int PARTY_LOOT_CHANGE_CANCELLED = 3137; // Party loot
    // change was
    // cancelled.
    public static final int PARTY_LOOT_CHANGED_S1 = 3138; // Party loot was
    // changed to "$s1".
    public static final int YOU_OBTAINED_S1_RECOMMENDS = 3207; // ÐŸÐ¾Ð»ÑƒÑ‡ÐµÐ½Ð¾
    // Ñ€ÐµÐºÐ¾Ð¼ÐµÐ½Ð´Ð°Ñ†Ð¸Ð¹:
    // "$s1:.
    public static final int CURRENT_LOCATION__S1_S2_S3_INSIDE_SEED_OF_ANNIHILATION = 3170; // Ð¢ÐµÐºÑƒÑ‰Ð°Ñ�
    // Ð»Ð¾ÐºÐ°Ñ†Ð¸Ñ�:
    // $s1,
    // $s2,
    // $s3
    // (Ð’Ð½ÑƒÑ‚Ñ€Ð¸
    // Ð¡ÐµÐ¼ÐµÐ½Ð¸
    // Ð£Ð½Ð¸Ñ‡Ñ‚Ð¾Ð¶ÐµÐ½Ð¸Ñ�)
    public static final int YOU_HAVE_EARNED_S1_B_S2_EXP_AND_S3_B_S4_SP = 3259; // ÐŸÐ¾Ð»ÑƒÑ‡ÐµÐ½Ð¾

    // Ð·Ð½Ð°Ñ‡ÐµÐ½Ð¸Ðµ
    // Ð¾Ð¿Ñ‹Ñ‚Ð°
    // $s1
    // (Ð‘Ð¾Ð½ÑƒÑ�:
    // $s2)
    // Ð¸
    // SP
    // $s3
    // (Ð‘Ð¾Ð½ÑƒÑ�:
    // $s4).
    
    public static final int YOU_CANNOT_MOVE_WHILE_IN_A_CHAOTIC_STATE = 3404;
    public static final int YOU_CANNOT_MOVE_WHILE_DEAD = 3392;
    public static final int YOU_CANNOT_MOVE_DURING_COMBAT = 3393;
    public static final int SUBCLASS_S1_HAS_BEEN_UPGRADED_TO_DUAL_CLASS_S2_CONGRATULATION = 3279;

    public SystemMessage(SystemMsg msg) {
        _messageId = msg.getId();
    }

    public SystemMessage(int messageId) {
        _messageId = messageId;
    }

    public SystemMessage(String msg) {
        this(S1);
        addString(msg);
    }

    public static SystemMessage obtainItemsByMail(ItemInstance item) {
        return new SystemMessage(SystemMessage.YOU_HAVE_ACQUIRED_S2_S1).addItemName(item.getItemId()).addNumber(
                                                                                                                       item.getCount());
    }

    public SystemMessage addString(String text) {
        args.add(new Arg(TYPE_TEXT, StringUtils.defaultString(text)));
        return this;
    }

    public SystemMessage addNumber(int number) {
        args.add(new Arg(TYPE_NUMBER, number));
        return this;
    }

    public SystemMessage addNumber(long number) {
        args.add(new Arg(TYPE_LONG, number));
        return this;
    }

    /**
     * Ð£Ñ�Ñ‚Ð°Ð½Ð°Ð²Ð»Ð¸Ð²Ð°ÐµÑ‚ Ð¸Ð¼Ñ� ÐµÑ�Ð»Ð¸ Ñ�Ñ‚Ð¾ playable Ð¸Ð»Ð¸ id ÐµÑ�Ð»Ð¸ Ñ�Ñ‚Ð¾ npc
     */
    public SystemMessage addName(Creature cha) {
        if (cha == null)
            return addString(StringUtils.EMPTY);

        if (cha.isDoor())
            return addDoorName(((DoorInstance) cha).getDoorId());

        if (cha.getNpcId() <= 0)
            return addString(cha.getName());

        return addNpcName(cha.getNpcId());
    }

    public SystemMessage addDoorName(int id) {
        args.add(new Arg(TYPE_DOOR_NAME, new Integer(id)));
        return this;
    }

    public SystemMessage addNpcName(int id) {
        args.add(new Arg(TYPE_NPC_NAME, new Integer(1000000 + id)));
        return this;
    }

    public SystemMessage addItemName(int id) {
        args.add(new Arg(TYPE_ITEM_NAME, id));
        return this;
    }

    public SystemMessage addZoneName(Location loc) {
        args.add(new Arg(TYPE_ZONE_NAME, loc));
        return this;
    }

    public SystemMessage addSkillName(int id, int level) {
        args.add(new Arg(TYPE_SKILL_NAME, new int[]{id, level}));
        return this;
    }

    public SystemMessage addClassName(int id) {
        args.add(new Arg(TYPE_CLASS_NAME, id));
        return this;
    }

    /**
     * Elemental name - 0(Fire) ...
     *
     * @param type
     * @return
     */
    public SystemMessage addElemntal(int type) {
        args.add(new Arg(TYPE_ELEMENT_NAME, type));
        return this;
    }

    /**
     * ID from sysstring-e.dat
     *
     * @param type
     * @return
     */
    public SystemMessage addSystemString(int type) {
        args.add(new Arg(TYPE_SYSTEM_STRING, type));
        return this;
    }

    /**
     * Instance name from instantzonedata-e.dat
     *
     * @param type id of instance
     * @return
     */
    public SystemMessage addInstanceName(int type) {
        args.add(new Arg(TYPE_INSTANCE_NAME, type));
        return this;
    }

    /**
     * Castlename-e.dat<br> 0-9 Castle names<br> 21-64 CH names<br> 81-89 Territory names<br> 101-121 Fortress names<br>
     *
     * @param number
     * @return
     */
    public SystemMessage addFortId(int number) {
        args.add(new Arg(TYPE_CASTLE_NAME, number));
        return this;
    }

    public SystemMessage addDamage(Creature targetid, Creature attakerid, int damage) {
        args.add(new Arg(TYPE_DAMAGE, new int[]{targetid.getObjectId(), attakerid.getObjectId(), damage}));
        return this;
    }

    @Override
    protected final void writeImpl() {
        Player activeChar = getClient().getActiveChar();
        if (activeChar == null)
            return;

        writeC(0x62);

        writeD(_messageId);
        writeD(args.size());
        for (Arg e : args) {
            writeD(e.type);

            switch (e.type) {
                case TYPE_TEXT:
                case TYPE_PLAYER_NAME: {
                    writeS((String) e.obj);
                    break;
                }
                case TYPE_NUMBER:
                case TYPE_NPC_NAME:
                case TYPE_ITEM_NAME:
                case TYPE_CASTLE_NAME:
                case TYPE_ELEMENT_NAME:
                case TYPE_SYSTEM_STRING:
                case TYPE_INSTANCE_NAME:
                case TYPE_DOOR_NAME:
                case TYPE_CLASS_NAME: {
                    writeD(((Number) e.obj).intValue());
                    break;
                }
                case TYPE_SKILL_NAME: {
                    int[] skill = (int[]) e.obj;
                    writeD(skill[0]); // id
                    writeD(skill[1]); // level
                    break;
                }
                case TYPE_LONG: {
                    writeQ((Long) e.obj);
                    break;
                }
                case TYPE_ZONE_NAME: {
                    Location coord = (Location) e.obj;
                    writeD(coord.x);
                    writeD(coord.y);
                    writeD(coord.z);
                    break;
                }
                case TYPE_UNKNOWN_8: {
                    writeD(0x00); // ?
                    writeH(0x00); // ?
                    writeH(0x00); // ?
                    break;
                }
                case TYPE_DAMAGE: {
                    int[] attr = (int[]) e.obj;
                    writeD(attr[0]); // target object id
                    writeD(attr[1]); // attaker object id
                    writeD(attr[2]); // damage
                    break;
                }
            }
        }
    }

    private class Arg {
        public final int type;
        public final Object obj;

        private Arg(int _type, Object _obj) {
            type = _type;
            obj = _obj;
        }
    }
}
