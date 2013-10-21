package lineage2.gameserver.network.serverpackets.components;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.serverpackets.L2GameServerPacket;
import lineage2.gameserver.network.serverpackets.SystemMessage2;

import java.util.NoSuchElementException;

/**
 * @author VISTALL
 * @date 13:28/01.12.2010
 */
public enum SystemMsg implements IStaticPacket
{
	// Message: Unable to dissolve: your clan has requested to participate in a
	// castle siege.
	UNABLE_TO_DISSOLVE_YOUR_CLAN_HAS_REQUESTED_TO_PARTICIPATE_IN_A_CASTLE_SIEGE(13),
	// Message: You are not in siege.
	YOU_ARE_NOT_IN_SIEGE(16),
	// Message: Your target is out of range.
	YOUR_TARGET_IS_OUT_OF_RANGE(22),
	// Message: Welcome to the World of Lineage II.
	WELCOME_TO_THE_WORLD_OF_LINEAGE_II(34),
	// Message: You carefully nock an arrow.
	YOU_CAREFULLY_NOCK_AN_ARROW(41),
	// Message: You have equipped your $s1.
	YOU_HAVE_EQUIPPED_YOUR_S1(49),
	// Message: You cannot use this on yourself.
	YOU_CANNOT_USE_THIS_ON_YOURSELF(51),
	// Message: You have earned $s1 adena.
	YOU_HAVE_EARNED_S1_ADENA(52),
	// Message: You have earned $s2 $s1(s).
	YOU_HAVE_EARNED_S2_S1S(53),
	// Message: You have earned $s1.
	YOU_HAVE_EARNED_S1(54),
	// Message: Nothing happened.
	NOTHING_HAPPENED(61),
	// Message: This name already exists.
	THIS_NAME_ALREADY_EXISTS(79),
	// Message: You may not attack in a peaceful zone.
	YOU_MAY_NOT_ATTACK_IN_A_PEACEFUL_ZONE(84),
	// Message: You may not attack this target in a peaceful zone.
	YOU_MAY_NOT_ATTACK_THIS_TARGET_IN_A_PEACEFUL_ZONE(85),
	// Message: This item cannot be traded or sold.
	THIS_ITEM_CANNOT_BE_TRADED_OR_SOLD(99),
	// Message: You cannot exit the game while in combat.
	YOU_CANNOT_EXIT_THE_GAME_WHILE_IN_COMBAT(101),
	// Message: You cannot restart while in combat.
	YOU_CANNOT_RESTART_WHILE_IN_COMBAT(102),
	// Message: $c1 has been invited to the party.
	C1_HAS_BEEN_INVITED_TO_THE_PARTY(105),
	// Message: Invalid target.
	INVALID_TARGET(109),
	// Message: Your shield defense has succeeded.
	YOUR_SHIELD_DEFENSE_HAS_SUCCEEDED(111),
	// Message: $s1 cannot be used due to unsuitable terms.
	S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS(113),
	// Message: $c1 has denied your request to trade.
	C1_HAS_DENIED_YOUR_REQUEST_TO_TRADE(119),
	// Message: You begin trading with $c1.
	YOU_BEGIN_TRADING_WITH_C1(120),
	// Message: You may no longer adjust items in the trade because the trade
	// has been confirmed.
	YOU_MAY_NO_LONGER_ADJUST_ITEMS_IN_THE_TRADE_BECAUSE_THE_TRADE_HAS_BEEN_CONFIRMED(122),
	// Message: You inventory is full.
	YOUR_INVENTORY_IS_FULL(129),
	// Message: That is an incorrect target.
	THAT_IS_AN_INCORRECT_TARGET(144),
	// Message: That player is not online.
	THAT_PLAYER_IS_NOT_ONLINE(145),
	// Message: $c1 is on another task. Please try again later.
	C1_IS_ON_ANOTHER_TASK(153),
	// Message: Only the leader can give out invitations.
	ONLY_THE_LEADER_CAN_GIVE_OUT_INVITATIONS(154),
	// Message: The party is full.
	THE_PARTY_IS_FULL(155),
	// Message: Your attack has failed.
	YOUR_ATTACK_HAS_FAILED(158),
	// Message: $c1 is a member of another party and cannot be invited.
	C1_IS_A_MEMBER_OF_ANOTHER_PARTY_AND_CANNOT_BE_INVITED(160),
	// Message: Waiting for another reply.
	WAITING_FOR_ANOTHER_REPLY(164),
	// Message: You cannot add yourself to your own friend list.
	YOU_CANNOT_ADD_YOURSELF_TO_YOUR_OWN_FRIEND_LIST(165),
	// Message: Friend list is not ready yet. Please register again later.
	FRIEND_LIST_IS_NOT_READY_YET(166),
	// Message: $c1 is already on your friend list.
	C1_IS_ALREADY_ON_YOUR_FRIEND_LIST(167),
	// Message: $c1 has sent a friend request.
	C1_HAS_SENT_A_FRIEND_REQUEST(168),
	// Message: Accept friendship 0/1 (1 to accept, 0 to deny)
	ACCEPT_FRIENDSHIP_01_1_TO_ACCEPT_0_TO_DENY(169),
	// Message: The user who requested to become friends is not found in the
	// game.
	THE_USER_WHO_REQUESTED_TO_BECOME_FRIENDS_IS_NOT_FOUND_IN_THE_GAME(170),
	// Message: $c1 is not on your friend list.
	C1_IS_NOT_ON_YOUR_FRIEND_LIST(171),
	// Message: You lack the funds needed to pay for this transaction.
	YOU_LACK_THE_FUNDS_NEEDED_TO_PAY_FOR_THIS_TRANSACTION(172),
	// Message: That person is in message refusal mode.
	THAT_PERSON_IS_IN_MESSAGE_REFUSAL_MODE(176),
	// Message: Message refusal mode.
	MESSAGE_REFUSAL_MODE(177),
	// Message: Cannot see target.
	CANNOT_SEE_TARGET(181),
	// Message: Entered the clan.
	ENTERED_THE_CLAN(195),
	// Message: $s1 declined your clan invitation.
	S1_DECLINED_YOUR_CLAN_INVITATION(196),
	// Message: You have recently been dismissed from a clan. You are not
	// allowed to join another clan for 24-hours.
	YOU_HAVE_RECENTLY_BEEN_DISMISSED_FROM_A_CLAN(199),
	// Message: Incorrect name. Please try again.
	INCORRECT_NAME(204),
	// Message: $s1 has joined the clan.
	S1_HAS_JOINED_THE_CLAN(222),
	// Message: $s1 has withdrawn from the clan.
	S1_HAS_WITHDRAWN_FROM_THE_CLAN(223),
	// Message: After leaving or having been dismissed from a clan, you must
	// wait at least a day before joining another clan.
	AFTER_LEAVING_OR_HAVING_BEEN_DISMISSED_FROM_A_CLAN_YOU_MUST_WAIT_AT_LEAST_A_DAY_BEFORE_JOINING_ANOTHER_CLAN(232),
	// Message: The target must be a clan member.
	THE_TARGET_MUST_BE_A_CLAN_MEMBER(234),
	// Message: Only the clan leader is enabled.
	ONLY_THE_CLAN_LEADER_IS_ENABLED(236),
	// Message: A clan leader cannot withdraw from their own clan.
	A_CLAN_LEADER_CANNOT_WITHDRAW_FROM_THEIR_OWN_CLAN(239),
	// Message: Clan name is invalid.
	CLAN_NAME_IS_INVALID(261),
	// Message: You do not have the necessary materials or prerequisites to
	// learn this skill.
	YOU_DO_NOT_HAVE_THE_NECESSARY_MATERIALS_OR_PREREQUISITES_TO_LEARN_THIS_SKILL(276),
	// Message: You have earned $s1.
	YOU_HAVE_EARNED_S1_SKILL(277),
	// Message: You do not have enough adena.
	YOU_DO_NOT_HAVE_ENOUGH_ADENA(279),
	// Message: You do not have enough SP to learn this skill.
	YOU_DO_NOT_HAVE_ENOUGH_SP_TO_LEARN_THIS_SKILL(278),
	// Message: Clan $s1 has successfully engraved the holy artifact!
	CLAN_S1_HAS_SUCCESSFULLY_ENGRAVED_THE_HOLY_ARTIFACT(285),
	// Message: Your base is being attacked.
	YOUR_BASE_IS_BEING_ATTACKED(286),
	// Message: The opposing clan has started to engrave the holy artifact!
	THE_OPPOSING_CLAN_HAS_STARTED_TO_ENGRAVE_THE_HOLY_ARTIFACT(287),
	// Message: The castle gate has been destroyed.
	THE_CASTLE_GATE_HAS_BEEN_DESTROYED(288),
	// Message: An outpost or headquarters cannot be built because one already
	// exists.
	AN_OUTPOST_OR_HEADQUARTERS_CANNOT_BE_BUILT_BECAUSE_ONE_ALREADY_EXISTS(289),
	// Message: You cannot set up a base here.
	YOU_CANNOT_SET_UP_A_BASE_HERE(290),
	// Message: Clan $s1 is victorious over $s2's castle siege!
	CLAN_S1_IS_VICTORIOUS_OVER_S2S_CASTLE_SIEGE(291),
	// Message: $s1 has announced the next castle siege time.
	S1_HAS_ANNOUNCED_THE_NEXT_CASTLE_SIEGE_TIME(292),
	// Message: The registration term for $s1 has ended.
	THE_REGISTRATION_TERM_FOR_S1_HAS_ENDED(293),
	// Message: You cannot summon the encampment because you are not a member of
	// the siege clan involved in the castle / fortress / hideout siege.
	YOU_CANNOT_SUMMON_THE_ENCAMPMENT_BECAUSE_YOU_ARE_NOT_A_MEMBER_OF_THE_SIEGE_CLAN_INVOLVED_IN_THE_CASTLE__FORTRESS__HIDEOUT_SIEGE(294),
	// Message: $s1's siege was canceled because there were no clans that
	// participated.
	S1S_SIEGE_WAS_CANCELED_BECAUSE_THERE_WERE_NO_CLANS_THAT_PARTICIPATED(295),
	// Message: You have dropped $s1.
	YOU_HAVE_DROPPED_S1(298),
	// Message: $c1 has obtained $s3 $s2.
	C1_HAS_OBTAINED_S3_S2(299),
	// Message: $c1 has obtained $s2.
	C1_HAS_OBTAINED_S2(300),
	// Message: $s2 $s1 has disappeared.
	S2_S1_HAS_DISAPPEARED(301),
	// Message: $s1 has disappeared.
	S1_HAS_DISAPPEARED(302),
	// Message: Clan member $s1 has logged into game.
	CLAN_MEMBER_S1_HAS_LOGGED_INTO_GAME(304),
	// Message: The player declined to join your party.
	THE_PLAYER_DECLINED_TO_JOIN_YOUR_PARTY(305),
	// Message: Incorrect item count.
	INCORRECT_ITEM_COUNT(351),
	// Message: Inappropriate enchant conditions.
	INAPPROPRIATE_ENCHANT_CONDITIONS(355),
	// Message: $s1 hour(s) until castle siege conclusion.
	S1_HOURS_UNTIL_CASTLE_SIEGE_CONCLUSION(358),
	// Message: $s1 minute(s) until castle siege conclusion.
	S1_MINUTES_UNTIL_CASTLE_SIEGE_CONCLUSION(359),
	// Message: This castle siege will end in $s1 second(s)!
	THIS_CASTLE_SIEGE_WILL_END_IN_S1_SECONDS(360),
	// Message: You have obtained a +$s1 $s2.
	YOU_HAVE_OBTAINED_A_S1_S2(369),
	// Message: $c1 has obtained +$s2$s3.
	C1_HAS_OBTAINED_S2S3(376),
	// Message: $S1 $S2 disappeared.
	S1_S2_DISAPPEARED(377),
	// Message: $c1 purchased $s2.
	C1_PURCHASED_S2(378),
	// Message: $c1 purchased +$s2$s3.
	C1_PURCHASED_S2S3(379),
	// Message: $c1 purchased $s3 $s2(s).
	C1_PURCHASED_S3_S2S(380),
	// Message: System error.
	SYSTEM_ERROR(399),
	// Message: You do not possess the correct ticket to board the boat.
	YOU_DO_NOT_POSSESS_THE_CORRECT_TICKET_TO_BOARD_THE_BOAT(402),
	// Message: Does not fit strengthening conditions of the scroll.
	DOES_NOT_FIT_STRENGTHENING_CONDITIONS_OF_THE_SCROLL(424),
	// Message: You cannot summon during a trade or while using a private store.
	YOU_CANNOT_SUMMON_DURING_A_TRADE_OR_WHILE_USING_A_PRIVATE_STORE(577),
	// Message: A pet cannot be unsummoned during battle.
	A_PET_CANNOT_BE_UNSUMMONED_DURING_BATTLE(579),
	// Message: Dead pets cannot be returned to their summoning item.
	DEAD_PETS_CANNOT_BE_RETURNED_TO_THEIR_SUMMONING_ITEM(589),
	// Message: You may not restore a hungry pet.
	YOU_MAY_NOT_RESTORE_A_HUNGRY_PET(594),
	// Message: You may not equip a pet item.
	YOU_MAY_NOT_EQUIP_A_PET_ITEM(600),
	// Message: You can only enter up 128 names in your friends list.
	YOU_CAN_ONLY_ENTER_UP_128_NAMES_IN_YOUR_FRIENDS_LIST(605),
	// Message: The Friend's List of the person you are trying to add is full,
	// so registration is not possible.
	THE_FRIENDS_LIST_OF_THE_PERSON_YOU_ARE_TRYING_TO_ADD_IS_FULL_SO_REGISTRATION_IS_NOT_POSSIBLE(606),
	// Message: You do not have any further skills to learn. Come back when you
	// have reached Level $s1.
	YOU_DO_NOT_HAVE_ANY_FURTHER_SKILLS_TO_LEARN__COME_BACK_WHEN_YOU_HAVE_REACHED_LEVEL_S1(607),
	// Message: You have already requested a Castle Siege.
	YOU_HAVE_ALREADY_REQUESTED_A_CASTLE_SIEGE(638),
	// Message: You are already registered to the attacker side and must cancel
	// your registration before submitting your request.
	YOU_ARE_ALREADY_REGISTERED_TO_THE_ATTACKER_SIDE_AND_MUST_CANCEL_YOUR_REGISTRATION_BEFORE_SUBMITTING_YOUR_REQUEST(642),
	// Message: You have already registered to the defender side and must cancel
	// your registration before submitting your request.
	YOU_HAVE_ALREADY_REGISTERED_TO_THE_DEFENDER_SIDE_AND_MUST_CANCEL_YOUR_REGISTRATION_BEFORE_SUBMITTING_YOUR_REQUEST(643),
	// Message: You are not yet registered for the castle siege.
	YOU_ARE_NOT_YET_REGISTERED_FOR_THE_CASTLE_SIEGE(644),
	// Message: Only clans of level 5 or higher may register for a castle siege.
	ONLY_CLANS_OF_LEVEL_5_OR_HIGHER_MAY_REGISTER_FOR_A_CASTLE_SIEGE(645),
	// Message: You do not have the authority to modify the castle defender
	// list.
	YOU_DO_NOT_HAVE_THE_AUTHORITY_TO_MODIFY_THE_CASTLE_DEFENDER_LIST(646),
	// Message: You do not have the authority to modify the siege time.
	YOU_DO_NOT_HAVE_THE_AUTHORITY_TO_MODIFY_THE_SIEGE_TIME(647),
	// Message: No more registrations may be accepted for the attacker side.
	NO_MORE_REGISTRATIONS_MAY_BE_ACCEPTED_FOR_THE_ATTACKER_SIDE(648),
	// Message: No more registrations may be accepted for the defender side.
	NO_MORE_REGISTRATIONS_MAY_BE_ACCEPTED_FOR_THE_DEFENDER_SIDE(649),
	// Message: You do not have the authority to position mercenaries.
	YOU_DO_NOT_HAVE_THE_AUTHORITY_TO_POSITION_MERCENARIES(653),
	// Message: You do not have the authority to cancel mercenary positioning.
	YOU_DO_NOT_HAVE_THE_AUTHORITY_TO_CANCEL_MERCENARY_POSITIONING(654),
	// Message: Mercenaries cannot be positioned here.
	MERCENARIES_CANNOT_BE_POSITIONED_HERE(655),
	// Message: This mercenary cannot be positioned anymore.
	THIS_MERCENARY_CANNOT_BE_POSITIONED_ANYMORE(656),
	// Message: Positioning cannot be done here because the distance between
	// mercenaries is too short.
	POSITIONING_CANNOT_BE_DONE_HERE_BECAUSE_THE_DISTANCE_BETWEEN_MERCENARIES_IS_TOO_SHORT(657),
	// Message: This is not a mercenary of a castle that you own and so you
	// cannot cancel its positioning.
	THIS_IS_NOT_A_MERCENARY_OF_A_CASTLE_THAT_YOU_OWN_AND_SO_YOU_CANNOT_CANCEL_ITS_POSITIONING(658),
	// Message: This is not the time for siege registration and so registrations
	// cannot be accepted or rejected.
	THIS_IS_NOT_THE_TIME_FOR_SIEGE_REGISTRATION_AND_SO_REGISTRATIONS_CANNOT_BE_ACCEPTED_OR_REJECTED(659),
	// Message: This is not the time for siege registration and so registration
	// and cancellation cannot be done.
	THIS_IS_NOT_THE_TIME_FOR_SIEGE_REGISTRATION_AND_SO_REGISTRATION_AND_CANCELLATION_CANNOT_BE_DONE(660),
	// Message: $s1 adena disappeared.
	S1_ADENA_DISAPPEARED(672),
	// Message: Only a clan leader whose clan is of level 2 or higher is allowed
	// to participate in a clan hall auction.
	ONLY_A_CLAN_LEADER_WHOSE_CLAN_IS_OF_LEVEL_2_OR_HIGHER_IS_ALLOWED_TO_PARTICIPATE_IN_A_CLAN_HALL_AUCTION(673),
	// Message: It has not yet been seven days since canceling an auction.
	IT_HAS_NOT_YET_BEEN_SEVEN_DAYS_SINCE_CANCELING_AN_AUCTION(674),
	// Message: There are no clan halls up for auction.
	THERE_ARE_NO_CLAN_HALLS_UP_FOR_AUCTION(675),
	// Message: Since you have already submitted a bid, you are not allowed to
	// participate in another auction at this time.
	SINCE_YOU_HAVE_ALREADY_SUBMITTED_A_BID_YOU_ARE_NOT_ALLOWED_TO_PARTICIPATE_IN_ANOTHER_AUCTION_AT_THIS_TIME(676),
	// Message: Your bid price must be higher than the minimum price currently
	// being bid.
	YOUR_BID_PRICE_MUST_BE_HIGHER_THAN_THE_MINIMUM_PRICE_CURRENTLY_BEING_BID(677),
	// Message: You have canceled your bid.
	YOU_HAVE_CANCELED_YOUR_BID(679),
	// Field YOU_ARE_NO_PRIORITY_RIGHTS_ON_A_SWEEPER.
	YOU_ARE_NO_PRIORITY_RIGHTS_ON_A_SWEEPER(683),
	// Message: You cannot move while frozen. Please wait.
	YOU_CANNOT_MOVE_WHILE_FROZEN(687),
	// Message: Castle-owning clans are automatically registered on the
	// defending side.
	CASTLE_OWNING_CLANS_ARE_AUTOMATICALLY_REGISTERED_ON_THE_DEFENDING_SIDE(688),
	// Message: A clan that owns a castle cannot participate in another siege.
	A_CLAN_THAT_OWNS_A_CASTLE_CANNOT_PARTICIPATE_IN_ANOTHER_SIEGE(689),
	// Message: You cannot register as an attacker because you are in an
	// alliance with the castle-owning clan.
	YOU_CANNOT_REGISTER_AS_AN_ATTACKER_BECAUSE_YOU_ARE_IN_AN_ALLIANCE_WITH_THE_CASTLE_OWNING_CLAN(690),
	// Message: The other party is frozen. Please wait a moment.
	THE_OTHER_PARTY_IS_FROZEN(692),
	// Message: No packages have arrived.
	NO_PACKAGES_HAVE_ARRIVED(694),
	// Message: You do not have enough required items.
	YOU_DO_NOT_HAVE_ENOUGH_REQUIRED_ITEMS(701),
	// Message: If a base camp does not exist, resurrection is not possible.
	IF_A_BASE_CAMP_DOES_NOT_EXIST_RESURRECTION_IS_NOT_POSSIBLE(716),
	// Message: The guardian tower has been destroyed and resurrection is not
	// possible.
	THE_GUARDIAN_TOWER_HAS_BEEN_DESTROYED_AND_RESURRECTION_IS_NOT_POSSIBLE(717),
	// Message: The effect of $s1 has been removed.
	THE_EFFECT_OF_S1_HAS_BEEN_REMOVED(749),
	// Message: There are no other skills to learn.
	THERE_ARE_NO_OTHER_SKILLS_TO_LEARN(750),
	// Message: You cannot position mercenaries here.
	YOU_CANNOT_POSITION_MERCENARIES_HERE(753),
	// Message: The clan hall which was put up for auction has been awarded to
	// $s1 clan.
	THE_CLAN_HALL_WHICH_WAS_PUT_UP_FOR_AUCTION_HAS_BEEN_AWARDED_TO_S1_CLAN(776),
	// Message: The clan hall which had been put up for auction was not sold and
	// therefore has been re-listed.
	THE_CLAN_HALL_WHICH_HAD_BEEN_PUT_UP_FOR_AUCTION_WAS_NOT_SOLD_AND_THEREFORE_HAS_BEEN_RELISTED(777),
	// Message: Observation is only possible during a siege.
	OBSERVATION_IS_ONLY_POSSIBLE_DURING_A_SIEGE(780),
	// Message: The tryouts are finished.
	THE_TRYOUTS_ARE_FINISHED(787),
	// Message: The finals are finished.
	THE_FINALS_ARE_FINISHED(788),
	// Message: The tryouts have begun.
	THE_TRYOUTS_HAVE_BEGUN(789),
	// Message: The finals have begun.
	THE_FINALS_HAVE_BEGUN(790),
	// Message: The final match is about to begin. Line up!
	THE_FINAL_MATCH_IS_ABOUT_TO_BEGIN(791),
	// Message: You are not authorized to do that.
	YOU_ARE_NOT_AUTHORIZED_TO_DO_THAT(794),
	// Message: You are too late. The registration period is over.
	YOU_ARE_TOO_LATE_THE_REGISTRATION_PERIOD_IS_OVER(800),
	// Message: The tryouts are about to begin. Line up!
	THE_TRYOUTS_ARE_ABOUT_TO_BEGIN(815),
	// Message: The siege of $s1 is finished.
	THE_SIEGE_OF_S1_IS_FINISHED(843),
	// Message: The siege to conquer $s1 has begun.
	THE_SIEGE_TO_CONQUER_S1_HAS_BEGUN(844),
	// Message: The deadline to register for the siege of $s1 has passed.
	THE_DEADLINE_TO_REGISTER_FOR_THE_SIEGE_OF_S1_HAS_PASSED(845),
	// Message: The siege of $s1 has been canceled due to lack of interest.
	THE_SIEGE_OF_S1_HAS_BEEN_CANCELED_DUE_TO_LACK_OF_INTEREST(846),
	// Message: A clan that owns a clan hall may not participate in a clan hall
	// siege.
	A_CLAN_THAT_OWNS_A_CLAN_HALL_MAY_NOT_PARTICIPATE_IN_A_CLAN_HALL_SIEGE(847),
	// Message: $s1 clan has defeated $s2.
	S1_CLAN_HAS_DEFEATED_S2(855),
	// Message: The siege of $s1 has ended in a draw.
	THE_SIEGE_OF_S1_HAS_ENDED_IN_A_DRAW(856),
	// Message: The seed has been sown.
	THE_SEED_HAS_BEEN_SOWN(871),
	// Message: This seed may not be sown here.
	THIS_SEED_MAY_NOT_BE_SOWN_HERE(872),
	// Message: The symbol has been added.
	THE_SYMBOL_HAS_BEEN_ADDED(877),
	// Message: The preliminary match of $s1 has ended in a draw.
	THE_PRELIMINARY_MATCH_OF_S1_HAS_ENDED_IN_A_DRAW(858),
	// Message: The symbol has been deleted.
	THE_SYMBOL_HAS_BEEN_DELETED(878),
	// Message: The manor system is currently under maintenance.
	THE_MANOR_SYSTEM_IS_CURRENTLY_UNDER_MAINTENANCE(879),
	// Message: The transaction is complete.
	THE_TRANSACTION_IS_COMPLETE(880),
	// Message: The symbol cannot be drawn.
	THE_SYMBOL_CANNOT_BE_DRAWN(899),
	// Message: No slot exists to draw the symbol.
	NO_SLOT_EXISTS_TO_DRAW_THE_SYMBOL(900),
	THE_COMMUNITY_SERVER_IS_CURRENTLY_OFFLINE(938),
	// Message: The soul crystal succeeded in absorbing a soul.
	THE_SOUL_CRYSTAL_SUCCEEDED_IN_ABSORBING_A_SOUL(974),
	// Message: The soul crystal was not able to absorb the soul.
	THE_SOUL_CRYSTAL_WAS_NOT_ABLE_TO_ABSORB_THE_SOUL(975),
	// Message: The soul crystal broke because it was not able to endure the
	// soul energy.
	THE_SOUL_CRYSTAL_BROKE_BECAUSE_IT_WAS_NOT_ABLE_TO_ENDURE_THE_SOUL_ENERGY(976),
	// Message: The soul crystal caused resonation and failed at absorbing a
	// soul.
	THE_SOUL_CRYSTAL_CAUSED_RESONATION_AND_FAILED_AT_ABSORBING_A_SOUL(977),
	// Message: The soul crystal is refusing to absorb the soul.
	THE_SOUL_CRYSTAL_IS_REFUSING_TO_ABSORB_THE_SOUL(978),
	// Message: You have registered for a clan hall auction.
	YOU_HAVE_REGISTERED_FOR_A_CLAN_HALL_AUCTION(1004),
	// Message: There is not enough adena in the clan hall warehouse.
	THERE_IS_NOT_ENOUGH_ADENA_IN_THE_CLAN_HALL_WAREHOUSE(1005),
	// Message: Your bid has been successfully placed.
	YOUR_BID_HAS_BEEN_SUCCESSFULLY_PLACED(1006),
	// Message: A hungry strider cannot be mounted or dismounted.
	A_HUNGRY_STRIDER_CANNOT_BE_MOUNTED_OR_DISMOUNTED(1008),
	// Message: A strider cannot be ridden when dead.
	A_STRIDER_CANNOT_BE_RIDDEN_WHEN_DEAD(1009),
	// Message: A dead strider cannot be ridden.
	A_DEAD_STRIDER_CANNOT_BE_RIDDEN(1010),
	// Message: A strider in battle cannot be ridden.
	A_STRIDER_IN_BATTLE_CANNOT_BE_RIDDEN(1011),
	// Message: A strider cannot be ridden while in battle.
	A_STRIDER_CANNOT_BE_RIDDEN_WHILE_IN_BATTLE(1012),
	// Message: A strider can be ridden only when standing.
	A_STRIDER_CAN_BE_RIDDEN_ONLY_WHEN_STANDING(1013),
	// Message: Pet's critical hit!
	PETS_CRITICAL_HIT(1017),
	// Message: Summoned monster's critical hit!
	SUMMONED_MONSTERS_CRITICAL_HIT(1028),
	// Message: You have exceeded the quantity that can be inputted.
	YOU_HAVE_EXCEEDED_THE_QUANTITY_THAT_CAN_BE_INPUTTED(1036),
	// Message: Payment for your clan hall has not been made. Please make
	// payment to your clan warehouse by $s1 tomorrow.
	PAYMENT_FOR_YOUR_CLAN_HALL_HAS_NOT_BEEN_MADE_PLEASE_ME_PAYMENT_TO_YOUR_CLAN_WAREHOUSE_BY_S1_TOMORROW(1051),
	// Message: The clan hall fee is one week overdue; therefore the clan hall
	// ownership has been revoked.
	THE_CLAN_HALL_FEE_IS_ONE_WEEK_OVERDUE_THEREFORE_THE_CLAN_HALL_OWNERSHIP_HAS_BEEN_REVOKED(1052),
	// Message: It is not possible to resurrect in battlefields where a siege
	// war is taking place.
	IT_IS_NOT_POSSIBLE_TO_RESURRECT_IN_BATTLEFIELDS_WHERE_A_SIEGE_WAR_IS_TAKING_PLACE(1053),
	// Message: While operating a private store or workshop, you cannot discard,
	// destroy, or trade an item.
	WHILE_OPERATING_A_PRIVATE_STORE_OR_WORKSHOP_YOU_CANNOT_DISCARD_DESTROY_OR_TRADE_AN_ITEM(1065),
	// Message: $s1 HP has been restored.
	S1_HP_HAS_BEEN_RESTORED(1066),
	// Message: $s2 HP has been restored by $c1.
	S2_HP_HAS_BEEN_RESTORED_BY_C1(1067),
	// Message: $s1 MP has been restored.
	S1_MP_HAS_BEEN_RESTORED(1068),
	// Message: $s2 MP has been restored by $c1.
	S2_MP_HAS_BEEN_RESTORED_BY_C1(1069),
	// Message: The bid amount must be higher than the previous bid.
	THE_BID_AMOUNT_MUST_BE_HIGHER_THAN_THE_PREVIOUS_BID(1075),
	// Message: A clan member may not be dismissed during combat.
	A_CLAN_MEMBER_MAY_NOT_BE_DISMISSED_DURING_COMBAT(1117),
	// Message: Would you like to open the gate?
	WOULD_YOU_LIKE_TO_OPEN_THE_GATE(1140),
	// Message: Would you like to close the gate?
	WOULD_YOU_LIKE_TO_CLOSE_THE_GATE(1141),
	// Message: Since $s1 already exists nearby, you cannot summon it again.
	SINCE_S1_ALREADY_EXISTS_NEARBY_YOU_CANNOT_SUMMON_IT_AGAIN(1142),
	// Message: The temporary alliance of the Castle Attacker team is in effect.
	// It will be dissolved when the Castle Lord is replaced.
	THE_TEMPORARY_ALLIANCE_OF_THE_CASTLE_ATTACKER_TEAM_IS_IN_EFFECT(1189),
	// Message: The temporary alliance of the Castle Attacker team has been
	// dissolved.
	THE_TEMPORARY_ALLIANCE_OF_THE_CASTLE_ATTACKER_TEAM_HAS_BEEN_DISSOLVED(1190),
	// Message: A mercenary can be assigned to a position from the beginning of
	// the Seal Validation period until the time when a siege starts.
	A_MERCENARY_CAN_BE_ASSIGNED_TO_A_POSITION_FROM_THE_BEGINNING_OF_THE_SEAL_VALIDATION_PERIOD_UNTIL_THE_TIME_WHEN_A_SIEGE_STARTS(1194),
	// Message: This mercenary cannot be assigned to a position by using the
	// Seal of Strife.
	THIS_MERCENARY_CANNOT_BE_ASSIGNED_TO_A_POSITION_BY_USING_THE_SEAL_OF_STRIFE(1195),
	// Message: $c1 died and dropped $s3 $s2.
	C1_DIED_AND_DROPPED_S3_S2(1208),
	// Message: $c1 has died and dropped $s2 adena.
	C1_HAS_DIED_AND_DROPPED_S2_ADENA(1246),
	// Message: The new subclass has been added.
	THE_NEW_SUBCLASS_HAS_BEEN_ADDED(1269),
	// Message: You have successfully switched to your subclass.
	YOU_HAVE_SUCCESSFULLY_SWITCHED_TO_YOUR_SUBCLASS(1270),
	// Message: Your excellent shield defense was a success!
	YOUR_EXCELLENT_SHIELD_DEFENSE_WAS_A_SUCCESS(1281),
	// Message: Subclasses may not be created or changed while a skill is in
	// use.
	SUBCLASSES_MAY_NOT_BE_CREATED_OR_CHANGED_WHILE_A_SKILL_IS_IN_USE(1295),
	// Message: You have exited the party room.
	YOU_HAVE_EXITED_THE_PARTY_ROOM(1391),
	// Message: $c1 has left the party room.
	C1_HAS_LEFT_THE_PARTY_ROOM(1392),
	// Message: You have been ousted from the party room.
	YOU_HAVE_BEEN_OUSTED_FROM_THE_PARTY_ROOM(1393),
	// Message: $c1 has been kicked from the party room.
	C1_HAS_BEEN_KICKED_FROM_THE_PARTY_ROOM(1394),
	// Message: The party room has been disbanded.
	THE_PARTY_ROOM_HAS_BEEN_DISBANDED(1395),
	// Message: The list of party rooms can only be viewed by a person who is
	// not part of a party.
	THE_LIST_OF_PARTY_ROOMS_CAN_ONLY_BE_VIEWED_BY_A_PERSON_WHO_IS_NOT_PART_OF_A_PARTY(1396),
	// Message: $s1 CP has been restored.
	S1_CP_HAS_BEEN_RESTORED(1405),
	// Message: $s2 CP has been restored by $c1.
	S2_CP_HAS_BEEN_RESTORED_BY_C1(1406),
	// Message: You do not meet the requirements to enter that party room.
	YOU_DO_NOT_MEET_THE_REQUIREMENTS_TO_ENTER_THAT_PARTY_ROOM(1413),
	// Message: You cannot do that while fishing.
	YOU_CANNOT_DO_THAT_WHILE_FISHING_(1470),
	// Message: You cannot do that while fishing.
	YOU_CANNOT_DO_THAT_WHILE_FISHING_2(1471),
	// Message: Your opponent made haste with their tail between their legs; the
	// match has been cancelled.
	YOUR_OPPONENT_MADE_HASTE_WITH_THEIR_TAIL_BETWEEN_THEIR_LEGS_THE_MATCH_HAS_BEEN_CANCELLED(1493),
	// Message: Your opponent does not meet the requirements to do battle; the
	// match has been cancelled.
	YOUR_OPPONENT_DOES_NOT_MEET_THE_REQUIREMENTS_TO_DO_BATTLE_THE_MATCH_HAS_BEEN_CANCELLED(1494),
	// Message: The match will start in $s1 second(s).
	THE_MATCH_WILL_START_IN_S1_SECONDS(1495),
	// Message: The match has started. Fight!
	THE_MATCH_HAS_STARTED(1496),
	// Message: Congratulations, $c1! You win the match!
	CONGRATULATIONS_C1_YOU_WIN_THE_MATCH(1497),
	// Message: There is no victor; the match ends in a tie.
	THERE_IS_NO_VICTOR_THE_MATCH_ENDS_IN_A_TIE(1498),
	// Message: You will be moved back to town in $s1 second(s).
	YOU_WILL_BE_MOVED_BACK_TO_TOWN_IN_S1_SECONDS(1499),
	// Message: $c1 does not meet the participation requirements. A subclass
	// character cannot participate in the Olympiad.
	C1_DOES_NOT_MEET_THE_PARTICIPATION_REQUIREMENTS_SUBCLASS_CHARACTER_CANNOT_PARTICIPATE_IN_THE_OLYMPIAD(1500),
	// Message: $c1 does not meet the participation requirements. Only Noblesse
	// characters can participate in the Olympiad.
	C1_DOES_NOT_MEET_THE_PARTICIPATION_REQUIREMENTS_ONLY_NOBLESSE_CHARACTERS_CAN_PARTICIPATE_IN_THE_OLYMPIAD(1501),
	// Message: $c1 is already registered on the match waiting list.
	C1_IS_ALREADY_REGISTERED_ON_THE_MATCH_WAITING_LIST(1502),
	// Message: You have been registered for the Grand Olympiad waiting list for
	// a class specific match.
	YOU_HAVE_BEEN_REGISTERED_FOR_THE_GRAND_OLYMPIAD_WAITING_LIST_FOR_A_CLASS_SPECIFIC_MATCH(1503),
	// Message: You are currently registered for a 1v1 class irrelevant match.
	YOU_ARE_CURRENTLY_REGISTERED_FOR_A_1V1_CLASS_IRRELEVANT_MATCH(1504),
	// Message: You have been removed from the Grand Olympiad waiting list.
	YOU_HAVE_BEEN_REMOVED_FROM_THE_GRAND_OLYMPIAD_WAITING_LIST(1505),
	// Message: You are not currently registered for the Grand Olympiad.
	YOU_ARE_NOT_CURRENTLY_REGISTERED_FOR_THE_GRAND_OLYMPIAD(1506),
	// Message: You cannot equip that item in a Grand Olympiad match.
	YOU_CANNOT_EQUIP_THAT_ITEM_IN_A_GRAND_OLYMPIAD_MATCH(1507),
	// Message: You cannot use that item in a Grand Olympiad match.
	YOU_CANNOT_USE_THAT_ITEM_IN_A_GRAND_OLYMPIAD_MATCH(1508),
	// Message: You cannot use that skill in a Grand Olympiad match.
	YOU_CANNOT_USE_THAT_SKILL_IN_A_GRAND_OLYMPIAD_MATCH(1509),
	// Message: $c1 is making an attempt to resurrect you. If you choose this
	// path, $s2 experience points will be returned to you. Do you want to be
	// resurrected?
	C1_IS_MAKING_AN_ATTEMPT_TO_RESURRECT_YOU_IF_YOU_CHOOSE_THIS_PATH_S2_EXPERIENCE_WILL_BE_RETURNED_FOR_YOU(1510),
	// Message: The target is unavailable for seeding.
	THE_TARGET_IS_UNAVAILABLE_FOR_SEEDING(1516),
	// Message: The Blessed Enchant failed. The enchant value of the item became
	// 0.
	THE_BLESSED_ENCHANT_FAILED(1517),
	
	//You do not meet the required condition to equip that item.
	YOU_DO_NOT_MEET_THE_REQUIRED_CONDITION_TO_EQUIP_THAT_ITEM(1518),
	// Message: You should release your pet or servitor so that it does not fall
	// off of the boat and drown!
	YOU_SHOULD_RELEASE_YOUR_PET_OR_SERVITOR_SO_THAT_IT_DOES_NOT_FALL_OFF_OF_THE_BOAT_AND_DROWN(1523),
	// Message: Welcome to Rune Harbor.
	WELCOME_TO_RUNE_HARBOR(1620),
	// Message: The Grand Olympiad Games are not currently in progress.
	THE_GRAND_OLYMPIAD_GAMES_ARE_NOT_CURRENTLY_IN_PROGRESS(1651),
	// Message: $c1 has earned $s2 points in the Grand Olympiad Games.
	C1_HAS_EARNED_S2_POINTS_IN_THE_GRAND_OLYMPIAD_GAMES(1657),
	// Message: $c1 has lost $s2 points in the Grand Olympiad Games.
	C1_HAS_LOST_S2_POINTS_IN_THE_GRAND_OLYMPIAD_GAMES(1658),
	// Message: Lethal Strike!
	LETHAL_STRIKE(1667),
	// Message: Your lethal strike was successful!
	YOUR_LETHAL_STRIKE_WAS_SUCCESSFUL(1668),
	// Message: There was nothing found inside.
	THERE_WAS_NOTHING_FOUND_INSIDE(1669),
	// Message: For the current Grand Olympiad you have participated in $s1
	// match(es). $s2 win(s) and $s3 defeat(s). You currently have $s4 Olympiad
	// Point(s).
	FOR_THE_CURRENT_GRAND_OLYMPIAD_YOU_HAVE_PARTICIPATED_IN_S1_MATCHES_S2_WINS_S3_DEFEATS_YOU_CURRENTLY_HAVE_S4_OLYMPIAD_POINTS(1673),
	// Message: This command can only be used by a Noblesse.
	THIS_COMMAND_CAN_ONLY_BE_USED_BY_A_NOBLESSE(1674),
	// Message: A manor cannot be set up between 4:30 am and 8 pm.
	A_MANOR_CANNOT_BE_SET_UP_BETWEEN_430_AM_AND_8_PM(1675),
	// Message: This area cannot be entered while mounted atop of a Wyvern. You
	// will be dismounted from your Wyvern if you do not leave!
	THIS_AREA_CANNOT_BE_ENTERED_WHILE_MOUNTED_ATOP_OF_A_WYVERN(1687),
	// Message: You cannot enchant while operating a Private Store or Private
	// Workshop.
	YOU_CANNOT_ENCHANT_WHILE_OPERATING_A_PRIVATE_STORE_OR_PRIVATE_WORKSHOP(1688),
	// Message: $c1 is already registered on the class match waiting list.
	C1_IS_ALREADY_REGISTERED_ON_THE_CLASS_MATCH_WAITING_LIST(1689),
	// Message: $c1 is already registered on the waiting list for the class
	// irrelevant individual match.
	C1_IS_ALREADY_REGISTERED_ON_THE_WAITING_LIST_FOR_THE_CLASS_IRRELEVANT_INDIVIDUAL_MATCH(1690),
	// Message: You may not observe a Grand Olympiad Games match while you are
	// on the waiting list.
	YOU_MAY_NOT_OBSERVE_A_GRAND_OLYMPIAD_GAMES_MATCH_WHILE_YOU_ARE_ON_THE_WAITING_LIST(1693),
	// Message: Only a clan leader that is a Noblesse can view the Siege War
	// Status window during a siege war.
	ONLY_A_CLAN_LEADER_THAT_IS_A_NOBLESSE_CAN_VIEW_THE_SIEGE_WAR_STATUS_WINDOW_DURING_A_SIEGE_WAR(1694),
	// Message: Your apprentice, $c1, has logged in.
	YOUR_APPRENTICE_C1_HAS_LOGGED_IN(1756),
	// Message: Your apprentice, $c1, has logged out.
	YOUR_APPRENTICE_C1_HAS_LOGGED_OUT(1757),
	// Message: Your sponsor, $c1, has logged in.
	YOUR_SPONSOR_C1_HAS_LOGGED_IN(1758),
	// Message: Your sponsor, $c1, has logged out.
	YOUR_SPONSOR_C1_HAS_LOGGED_OUT(1759),
	// Message: Since your clan emerged victorious from the siege, $s1 points
	// have been added to your clan's reputation score.
	SINCE_YOUR_CLAN_EMERGED_VICTORIOUS_FROM_THE_SIEGE_S1_POINTS_HAVE_BEEN_ADDED_TO_YOUR_CLANS_REPUTATION_SCORE(1773),
	// Message: Your clan has failed to defend the castle. $s1 points have been
	// deducted from your clan's reputation score and added to your opponents'.
	YOUR_CLAN_HAS_FAILED_TO_DEFEND_THE_CASTLE_S1_POINTS_HAVE_BEEN_DEDUCTED_FROM_YOU_CLAN_REPUTATION_SCORE_AND_ADDED_TO_YOUR_OPPONENTS(1784),
	// Message: The clan skill $s1 has been added.
	THE_CLAN_SKILL_S1_HAS_BEEN_ADDED(1788),
	// Message: The registration period for a clan hall war has ended.
	THE_REGISTRATION_PERIOD_FOR_A_CLAN_HALL_WAR_HAS_ENDED(1823),
	// Message: You have been registered for a clan hall war. Please move to the
	// left side of the clan hall's arena and get ready.
	YOU_HAVE_BEEN_REGISTERED_FOR_A_CLAN_HALL_WAR(1824),
	// Message: You have failed in your attempt to register for the clan hall
	// war. Please try again.
	YOU_HAVE_FAILED_IN_YOUR_ATTEMPT_TO_REGISTER_FOR_THE_CLAN_HALL_WAR(1825),
	// Message: In $s1 minute(s), the game will begin. All players must hurry
	// and move to the left side of the clan hall's arena.
	IN_S1_MINUTES_THE_GAME_WILL_BEGIN_ALL_PLAYERS_MUST_HURRY_AND_MOVE_TO_THE_LEFT_SIDE_OF_THE_CLAN_HALLS_ARENA(1826),
	// Message: In $s1 minute(s), the game will begin. All players, please enter
	// the arena now.
	IN_S1_MINUTES_THE_GAME_WILL_BEGIN_ALL_PLAYERS_PLEASE_ENTER_THE_ARENA_NOW(1827),
	// Message: In $s1 second(s), the game will begin.
	IN_S1_SECONDS_THE_GAME_WILL_BEGIN(1828),
	// Message: $c1 is not allowed to use the party room invite command. Please
	// update the waiting list.
	C1_IS_NOT_ALLOWED_TO_USE_THE_PARTY_ROOM_INVITE_COMMAND(1830),
	// Message: $c1 does not meet the conditions of the party room. Please
	// update the waiting list.
	C1_DOES_NOT_MEET_THE_CONDITIONS_OF_THE_PARTY_ROOM(1831),
	// Message: Only a room leader may invite others to a party room.
	ONLY_A_ROOM_LEADER_MAY_INVITE_OTHERS_TO_A_PARTY_ROOM(1832),
	// Message: The party room is full. No more characters can be invited in.
	THE_PARTY_ROOM_IS_FULL(1834),
	// / Message: This clan hall war has been cancelled. Not enough clans have
	// registered.
	THIS_CLAN_HALL_WAR_HAS_BEEN_CANCELLED(1841),
	// Message: $c1 wishes to summon you from $s2. Do you accept?
	C1_WISHES_TO_SUMMON_YOU_FROM_S2(1842),
	// Message: The Clan Reputation Score is too low.
	THE_CLAN_REPUTATION_SCORE_IS_TOO_LOW(1860),
	// Message: Your pet/servitor is unresponsive and will not obey any orders.
	YOUR_PETSERVITOR_IS_UNRESPONSIVE_AND_WILL_NOT_OBEY_ANY_ORDERS(1864),
	// Message: The preliminary match will begin in $s1 second(s). Prepare
	// yourself.
	THE_PRELIMINARY_MATCH_WILL_BEGIN_IN_S1_SECONDS(1881),
	// Message: There are no offerings I own or I made a bid for.
	THERE_ARE_NO_OFFERINGS_I_OWN_OR_I_MADE_A_BID_FOR(1883),
	// Message: You may not use items in a private store or private work shop.
	YOU_MAY_NOT_USE_ITEMS_IN_A_PRIVATE_STORE_OR_PRIVATE_WORK_SHOP(1891),
	// Message: A sub-class cannot be created or changed while you are over your
	// weight limit.
	A_SUBCLASS_CANNOT_BE_CREATED_OR_CHANGED_WHILE_YOU_ARE_OVER_YOUR_WEIGHT_LIMIT(1894),
	// Message: $c1 has entered the party room.
	C1_HAS_ENTERED_THE_PARTY_ROOM(1900),
	// Message: $s1 has sent an invitation to room <$s2>.
	S1_HAS_SENT_AN_INVITATION_TO_ROOM_S2(1901),
	// Message: Incompatible item grade. This item cannot be used.
	INCOMPATIBLE_ITEM_GRADE(1902),
	// Message: A sub-class may not be created or changed while a servitor or
	// pet is summoned.
	A_SUBCLASS_MAY_NOT_BE_CREATED_OR_CHANGED_WHILE_A_SERVITOR_OR_PET_IS_SUMMONED(1904),
	// Message: Your pet is too high level to control.
	YOUR_PET_IS_TOO_HIGH_LEVEL_TO_CONTROL(1918),
	// Message: There is no opponent to receive your challenge for a duel.
	THERE_IS_NO_OPPONENT_TO_RECEIVE_YOUR_CHALLENGE_FOR_A_DUEL(1926),
	// Message: $c1 has been challenged to a duel.
	C1_HAS_BEEN_CHALLENGED_TO_A_DUEL(1927),
	// Message: $c1's party has been challenged to a duel.
	C1S_PARTY_HAS_BEEN_CHALLENGED_TO_A_DUEL(1928),
	// Message: $c1 has accepted your challenge to a duel. The duel will begin
	// in a few moments.
	C1_HAS_ACCEPTED_YOUR_CHALLENGE_TO_A_DUEL(1929),
	// Message: You have accepted $c1's challenge a duel. The duel will begin in
	// a few moments.
	YOU_HAVE_ACCEPTED_C1S_CHALLENGE_A_DUEL(1930),
	// Message: $c1 has declined your challenge to a duel.
	C1_HAS_DECLINED_YOUR_CHALLENGE_TO_A_DUEL(1931),
	// Message: $c1 has declined your challenge to a duel.
	C1_HAS_DECLINED_YOUR_CHALLENGE_TO_A_DUEL_(1932),
	// Message: You have accepted $c1's challenge to a party duel. The duel will
	// begin in a few moments.
	YOU_HAVE_ACCEPTED_C1S_CHALLENGE_TO_A_PARTY_DUEL(1933),
	// Message: $s1 has accepted your challenge to duel against their party. The
	// duel will begin in a few moments.
	S1_HAS_ACCEPTED_YOUR_CHALLENGE_TO_DUEL_AGAINST_THEIR_PARTY(1934),
	// Message: $c1 has declined your challenge to a party duel.
	C1_HAS_DECLINED_YOUR_CHALLENGE_TO_A_PARTY_DUEL(1935),
	// Message: The opposing party has declined your challenge to a duel.
	THE_OPPOSING_PARTY_HAS_DECLINED_YOUR_CHALLENGE_TO_A_DUEL(1936),
	// Message: Since the person you challenged is not currently in a party,
	// they cannot duel against your party.
	SINCE_THE_PERSON_YOU_CHALLENGED_IS_NOT_CURRENTLY_IN_A_PARTY_THEY_CANNOT_DUEL_AGAINST_YOUR_PARTY(1937),
	// Message: $c1 has challenged you to a duel.
	C1_HAS_CHALLENGED_YOU_TO_A_DUEL(1938),
	// Message: $c1's party has challenged your party to a duel.
	C1S_PARTY_HAS_CHALLENGED_YOUR_PARTY_TO_A_DUEL(1939),
	// Message: You are unable to request a duel at this time.
	YOU_ARE_UNABLE_TO_REQUEST_A_DUEL_AT_THIS_TIME(1940),
	// Message: In a moment, you will be transported to the site where the duel
	// will take place.
	IN_A_MOMENT_YOU_WILL_BE_TRANSPORTED_TO_THE_SITE_WHERE_THE_DUEL_WILL_TAKE_PLACE(1944),
	// Message: The duel will begin in $s1 second(s).
	THE_DUEL_WILL_BEGIN_IN_S1_SECONDS(1945),
	// Message: Let the duel begin!
	LET_THE_DUEL_BEGIN(1949),
	// Message: $c1 has won the duel.
	C1_HAS_WON_THE_DUEL(1950),
	// Message: $c1's party has won the duel.
	C1S_PARTY_HAS_WON_THE_DUEL(1951),
	// Message: The duel has ended in a tie.
	THE_DUEL_HAS_ENDED_IN_A_TIE(1952),
	// Message: Only the clan leader may issue commands.
	ONLY_THE_CLAN_LEADER_MAY_ISSUE_COMMANDS(1966),
	// Message: S1
	S1(1983),
	// Message: The ferry has arrived at Primeval Isle.
	THE_FERRY_HAS_ARRIVED_AT_PRIMEVAL_ISLE(1988),
	// Message: The ferry will leave for Rune Harbor after anchoring for three
	// minutes.
	THE_FERRY_WILL_LEAVE_FOR_RUNE_HARBOR_AFTER_ANCHORING_FOR_THREE_MINUTES(1989),
	// Message: The ferry is now departing Primeval Isle for Rune Harbor.
	THE_FERRY_IS_NOW_DEPARTING_PRIMEVAL_ISLE_FOR_RUNE_HARBOR(1990),
	// Message: The ferry will leave for Primeval Isle after anchoring for three
	// minutes.
	THE_FERRY_WILL_LEAVE_FOR_PRIMEVAL_ISLE_AFTER_ANCHORING_FOR_THREE_MINUTES(1991),
	// Message: The ferry is now departing Rune Harbor for Primeval Isle.
	THE_FERRY_IS_NOW_DEPARTING_RUNE_HARBOR_FOR_PRIMEVAL_ISLE(1992),
	// Message: The ferry from Primeval Isle to Rune Harbor has been delayed.
	THE_FERRY_FROM_PRIMEVAL_ISLE_TO_RUNE_HARBOR_HAS_BEEN_DELAYED(1993),
	// Message: The ferry from Rune Harbor to Primeval Isle has been delayed.
	THE_FERRY_FROM_RUNE_HARBOR_TO_PRIMEVAL_ISLE_HAS_BEEN_DELAYED(1994),
	// Message: $c1 cannot duel because $c1 is currently engaged in a private
	// store or manufacture.
	C1_CANNOT_DUEL_BECAUSE_C1_IS_CURRENTLY_ENGAGED_IN_A_PRIVATE_STORE_OR_MANUFACTURE(2017),
	// Message: $c1 cannot duel because $c1 is currently fishing.
	C1_CANNOT_DUEL_BECAUSE_C1_IS_CURRENTLY_FISHING(2018),
	// Message: $c1 cannot duel because $c1's HP or MP is below 50%.
	C1_CANNOT_DUEL_BECAUSE_C1S_HP_OR_MP_IS_BELOW_50(2019),
	// Message: $c1 cannot make a challenge to a duel because $c1 is currently
	// in a duel-prohibited area (Peaceful Zone / Seven Signs Zone / Near Water
	// / Restart Prohibited Area).
	C1_CANNOT_MAKE_A_CHALLENGE_TO_A_DUEL_BECAUSE_C1_IS_CURRENTLY_IN_A_DUELPROHIBITED_AREA_PEACEFUL_ZONE__SEVEN_SIGNS_ZONE__NEAR_WATER__RESTART_PROHIBITED_AREA(2020),
	// Message: $c1 cannot duel because $c1 is currently engaged in battle.
	C1_CANNOT_DUEL_BECAUSE_C1_IS_CURRENTLY_ENGAGED_IN_BATTLE(2021),
	// Message: $c1 cannot duel because $c1 is already engaged in a duel.
	C1_CANNOT_DUEL_BECAUSE_C1_IS_ALREADY_ENGAGED_IN_A_DUEL(2022),
	// Message: $c1 cannot duel because $c1 is in a chaotic state.
	C1_CANNOT_DUEL_BECAUSE_C1_IS_IN_A_CHAOTIC_STATE(2023),
	// Message: $c1 cannot duel because $c1 is participating in the Olympiad.
	C1_CANNOT_DUEL_BECAUSE_C1_IS_PARTICIPATING_IN_THE_OLYMPIAD(2024),
	// Message: $c1 cannot duel because $c1 is participating in a clan hall war.
	C1_CANNOT_DUEL_BECAUSE_C1_IS_PARTICIPATING_IN_A_CLAN_HALL_WAR(2025),
	// Message: $c1 cannot duel because $c1 is participating in a siege war.
	C1_CANNOT_DUEL_BECAUSE_C1_IS_PARTICIPATING_IN_A_SIEGE_WAR(2026),
	// Message: $c1 cannot duel because $c1 is currently riding a boat, steed,
	// or strider.
	C1_CANNOT_DUEL_BECAUSE_C1_IS_CURRENTLY_RIDING_A_BOAT_STEED_OR_STRIDER(2027),
	// Message: $c1 cannot receive a duel challenge because $c1 is too far away.
	C1_CANNOT_RECEIVE_A_DUEL_CHALLENGE_BECAUSE_C1_IS_TOO_FAR_AWAY(2028),
	// Message: A sub-class cannot be created or changed because you have
	// exceeded your inventory limit.
	A_SUBCLASS_CANNOT_BE_CREATED_OR_CHANGED_BECAUSE_YOU_HAVE_EXCEEDED_YOUR_INVENTORY_LIMIT(2033),
	// Message: $s1 clan is trying to display a flag.
	S1_CLAN_IS_TRYING_TO_DISPLAY_A_FLAG(2050),
	// Message: That weapon cannot perform any attacks.
	THAT_WEAPON_CANNOT_PERFORM_ANY_ATTACKS(2066),
	// Message: That weapon cannot use any other skill except the weapon's
	// skill.
	THAT_WEAPON_CANNOT_USE_ANY_OTHER_SKILL_EXCEPT_THE_WEAPONS_SKILL(2067),
	// Message: Enemy Blood Pledges have intruded into the fortress.
	ENEMY_BLOOD_PLEDGES_HAVE_INTRUDED_INTO_THE_FORTRESS(2084),
	// Message: Shout and trade chatting cannot be used while possessing a
	// cursed weapon.
	SHOUT_AND_TRADE_CHATTING_CANNOT_BE_USED_WHILE_POSSESSING_A_CURSED_WEAPON(2085),
	// Message: Search on user $c2 for third-party program use will be completed
	// in $s1 minute(s).
	SEARCH_ON_USER_C2_FOR_THIRDPARTY_PROGRAM_USE_WILL_BE_COMPLETED_IN_S1_MINUTES(2086),
	// Message: A fortress is under attack!
	A_FORTRESS_IS_UNDER_ATTACK(2087),
	// Message: $s1 minute(s) until the fortress battle starts.
	S1_MINUTES_UNTIL_THE_FORTRESS_BATTLE_STARTS(2088),
	// Message: $s1 second(s) until the fortress battle starts.
	S1_SECONDS_UNTIL_THE_FORTRESS_BATTLE_STARTS(2089),
	// Message: The fortress battle $s1 has begun.
	THE_FORTRESS_BATTLE_S1_HAS_BEGUN(2090),
	// Message: $c1 is in a location which cannot be entered, therefore it
	// cannot be processed.
	C1_IS_IN_A_LOCATION_WHICH_CANNOT_BE_ENTERED_THEREFORE_IT_CANNOT_BE_PROCESSED(2096),
	// Message: $c1's level does not correspond to the requirements for entry.
	C1S_LEVEL_DOES_NOT_CORRESPOND_TO_THE_REQUIREMENTS_FOR_ENTRY(2097),
	// Message: $c1's quest requirement is not sufficient and cannot be entered.
	C1S_QUEST_REQUIREMENT_IS_NOT_SUFFICIENT_AND_CANNOT_BE_ENTERED(2098),
	// Message: $c1's item requirement is not sufficient and cannot be entered.
	C1S_ITEM_REQUIREMENT_IS_NOT_SUFFICIENT_AND_CANNOT_BE_ENTERED(2099),
	// Message: $c1 may not re-enter yet.
	C1_MAY_NOT_REENTER_YET(2100),
	// Message: You are not currently in a party, so you cannot enter.
	YOU_ARE_NOT_CURRENTLY_IN_A_PARTY_SO_YOU_CANNOT_ENTER(2101),
	// Message: You cannot enter due to the party having exceeded the limit.
	YOU_CANNOT_ENTER_DUE_TO_THE_PARTY_HAVING_EXCEEDED_THE_LIMIT(2102),
	// Message: You cannot enter because you are not associated with the current
	// command channel.
	YOU_CANNOT_ENTER_BECAUSE_YOU_ARE_NOT_ASSOCIATED_WITH_THE_CURRENT_COMMAND_CHANNEL(2103),
	// Message: The maximum number of instance zones has been exceeded. You
	// cannot enter.
	THE_MAXIMUM_NUMBER_OF_INSTANCE_ZONES_HAS_BEEN_EXCEEDED(2104),
	// Message: You have entered another instance zone, therefore you cannot
	// enter corresponding dungeon.
	YOU_HAVE_ENTERED_ANOTHER_INSTANCE_ZONE_THEREFORE_YOU_CANNOT_ENTER_CORRESPONDING_DUNGEON(2105),
	// Message: This dungeon will expire in $s1 minute(s). You will be forced
	// out of the dungeon when the time expires.
	THIS_DUNGEON_WILL_EXPIRE_IN_S1_MINUTES(2106),
	// Message: You cannot convert this item.
	YOU_CANNOT_CONVERT_THIS_ITEM(2130),
	// Message: The target is not a flagpole so a flag cannot be displayed.
	THE_TARGET_IS_NOT_A_FLAGPOLE_SO_A_FLAG_CANNOT_BE_DISPLAYED(2154),
	// Message: A flag is already being displayed, another flag cannot be
	// displayed.
	A_FLAG_IS_ALREADY_BEING_DISPLAYED_ANOTHER_FLAG_CANNOT_BE_DISPLAYED(2155),
	// Message: There are not enough necessary items to use the skill.
	THERE_ARE_NOT_ENOUGH_NECESSARY_ITEMS_TO_USE_THE_SKILL(2156),
	// Message: Force attack is impossible against a temporary allied member
	// during a siege.
	FORCE_ATTACK_IS_IMPOSSIBLE_AGAINST_A_TEMPORARY_ALLIED_MEMBER_DURING_A_SIEGE(2158),
	// Message: The barracks have been seized.
	THE_BARRACKS_HAVE_BEEN_SEIZED(2164),
	// Message: The barracks function has been restored.
	THE_BARRACKS_FUNCTION_HAS_BEEN_RESTORED(2165),
	// Message: All barracks are occupied.
	ALL_BARRACKS_ARE_OCCUPIED(2166),
	// Message: $c1 has acquired the flag.
	C1_HAS_ACQUIRED_THE_FLAG(2168),
	// Message: Your clan has been registered to $s1's fortress battle.
	YOUR_CLAN_HAS_BEEN_REGISTERED_TO_S1S_FORTRESS_BATTLE(2169),
	// Message: $c1 cannot duel because $c1 is currently polymorphed.
	C1_CANNOT_DUEL_BECAUSE_C1_IS_CURRENTLY_POLYMORPHED(2174),
	// Message: Party duel cannot be initiated due to a polymorphed party
	// member.
	PARTY_DUEL_CANNOT_BE_INITIATED_DUE_TO_A_POLYMORPHED_PARTY_MEMBER(2175),
	// Message: The fortress battle of $s1 has finished.
	THE_FORTRESS_BATTLE_OF_S1_HAS_FINISHED(2183),
	// Message: $s1 is victorious in the fortress battle of $s2.
	S1_IS_VICTORIOUS_IN_THE_FORTRESS_BATTLE_OF_S2(2184),
	// Message: Only a party leader can make the request to enter.
	ONLY_A_PARTY_LEADER_CAN_MAKE_THE_REQUEST_TO_ENTER(2185),
	// Message: You cannot board a ship while you are polymorphed.
	YOU_CANNOT_BOARD_A_SHIP_WHILE_YOU_ARE_POLYMORPHED(2213),
	// Message: The ballista has been successfully destroyed. The clan's
	// reputation will be increased.
	THE_BALLISTA_HAS_BEEN_SUCCESSFULLY_DESTROYED(2217),
	// Message: This squad skill has already been acquired.
	THIS_SQUAD_SKILL_HAS_ALREADY_BEEN_ACQUIRED(2219),
	// Message: The previous level skill has not been learned.
	THE_PREVIOUS_LEVEL_SKILL_HAS_NOT_BEEN_LEARNED(2220),
	// Message: It is not possible to register for the castle siege side or
	// castle siege of a higher castle in the contract.
	IT_IS_NOT_POSSIBLE_TO_REGISTER_FOR_THE_CASTLE_SIEGE_SIDE_OR_CASTLE_SIEGE_OF_A_HIGHER_CASTLE_IN_THE_CONTRACT(2227),
	// Message: Instance zone time limit:
	INSTANCE_ZONE_TIME_LIMIT(2228),
	// Message: There is no instance zone under a time limit.
	THERE_IS_NO_INSTANCE_ZONE_UNDER_A_TIME_LIMIT(2229),
	// Message: $s1 will be available for re-use after $s2 hour(s) $s3
	// minute(s).
	S1_WILL_BE_AVAILABLE_FOR_REUSE_AFTER_S2_HOURS_S3_MINUTES(2230),
	// Message: Siege registration is not possible due to your castle contract.
	SIEGE_REGISTRATION_IS_NOT_POSSIBLE_DUE_TO_YOUR_CASTLE_CONTRACT(2233),
	// Message: You are participating in the siege of $s1. This siege is
	// scheduled for 2 hours.
	YOU_ARE_PARTICIPATING_IN_THE_SIEGE_OF_S1_THIS_SIEGE_IS_SCHEDULED_FOR_2_HOURS(2238),
	// Message: $s1 minute(s) remaining.
	S1_MINUTES_REMAINING(2244),
	// Message: $s1 second(s) remaining.
	S1_SECONDS_REMAINING(2245),
	// Message: The contest will begin in $s1 minute(s).
	THE_CONTEST_WILL_BEGIN_IN_S1_MINUTES(2246),
	// Message: You cannot board an airship while transformed.
	YOU_CANNOT_BOARD_AN_AIRSHIP_WHILE_TRANSFORMED(2247),
	// Message: You cannot board an airship while petrified.
	YOU_CANNOT_BOARD_AN_AIRSHIP_WHILE_PETRIFIED(2248),
	// Message: You cannot board an airship while dead.
	YOU_CANNOT_BOARD_AN_AIRSHIP_WHILE_DEAD(2249),
	// Message: You cannot board an airship while fishing.
	YOU_CANNOT_BOARD_AN_AIRSHIP_WHILE_FISHING(2250),
	// Message: You cannot board an airship while in battle.
	YOU_CANNOT_BOARD_AN_AIRSHIP_WHILE_IN_BATTLE(2251),
	// Message: You cannot board an airship while in a duel.
	YOU_CANNOT_BOARD_AN_AIRSHIP_WHILE_IN_A_DUEL(2252),
	// Message: You cannot board an airship while sitting.
	YOU_CANNOT_BOARD_AN_AIRSHIP_WHILE_SITTING(2253),
	// Message: You cannot board an airship while casting.
	YOU_CANNOT_BOARD_AN_AIRSHIP_WHILE_CASTING(2254),
	// Message: You cannot board an airship when a cursed weapon is equipped.
	YOU_CANNOT_BOARD_AN_AIRSHIP_WHEN_A_CURSED_WEAPON_IS_EQUIPPED(2255),
	// Message: You cannot board an airship while holding a flag.
	YOU_CANNOT_BOARD_AN_AIRSHIP_WHILE_HOLDING_A_FLAG(2256),
	// Message: You cannot board an airship while a pet or a servitor is
	// summoned.
	YOU_CANNOT_BOARD_AN_AIRSHIP_WHILE_A_PET_OR_A_SERVITOR_IS_SUMMONED(2257),
	// Message: You have already boarded another airship.
	YOU_HAVE_ALREADY_BOARDED_ANOTHER_AIRSHIP(2258),
	// Message: This skill cannot be learned while in the sub-class state.
	// Please try again after changing to the main class.
	THIS_SKILL_CANNOT_BE_LEARNED_WHILE_IN_THE_SUBCLASS_STATE(2273),
	// Message: You cannot wear $s1 because you are not wearing a bracelet.
	YOU_CANNOT_WEAR_S1_BECAUSE_YOU_ARE_NOT_WEARING_A_BRACELET(2286),
	// Message: You cannot equip $s1 because you do not have any available
	// slots.
	YOU_CANNOT_EQUIP_S1_BECAUSE_YOU_DO_NOT_HAVE_ANY_AVAILABLE_SLOTS(2287),
	// Message: Agathion skills can be used only when your Agathion is summoned.
	AGATHION_SKILLS_CAN_BE_USED_ONLY_WHEN_YOUR_AGATHION_IS_SUMMONED(2292),
	// Message: There are $s2 second(s) remaining in $s1's re-use time.
	THERE_ARE_S2_SECONDS_REMAINING_IN_S1S_REUSE_TIME(2303),
	// Message: There are $s2 minute(s), $s3 second(s) remaining in $s1's re-use
	// time.
	THERE_ARE_S2_MINUTES_S3_SECONDS_REMAINING_IN_S1S_REUSE_TIME(2304),
	// Message: There are $s2 hour(s), $s3 minute(s), and $s4 second(s)
	// remaining in $s1's re-use time.
	THERE_ARE_S2_HOURS_S3_MINUTES_AND_S4_SECONDS_REMAINING_IN_S1S_REUSE_TIME(2305),
	// Message: Your Charm of Courage is trying to resurrect you. Would you like
	// to resurrect now?
	YOUR_CHARM_OF_COURAGE_IS_TRYING_TO_RESURRECT_YOU(2306),
	// Message: Only clans who are level 4 or above can register for battle at
	// Devastated Castle and Fortress of the Dead.
	ONLY_CLANS_WHO_ARE_LEVEL_4_OR_ABOVE_CAN_REGISTER_FOR_BATTLE_AT_DEVASTATED_CASTLE_AND_FORTRESS_OF_THE_DEAD(2328),
	// Message: $s1 seconds to game end!
	S1_SECONDS_TO_GAME_END(2347),
	// Message: Resurrection will take place in the waiting room after $s1
	// seconds.
	RESURRECTION_WILL_TAKE_PLACE_IN_THE_WAITING_ROOM_AFTER_S1_SECONDS(2370),
	// Message: End match!
	END_MATCH(2374),
	// Message: A party cannot be formed in this area.
	A_PARTY_CANNOT_BE_FORMED_IN_THIS_AREA(2388),
	// Field YOUR_NUMBER_OF_MY_TELEPORTS_SLOTS_HAS_REACHED_ITS_MAXIMUM_LIMIT.
	YOUR_NUMBER_OF_MY_TELEPORTS_SLOTS_HAS_REACHED_ITS_MAXIMUM_LIMIT(2390),
	// Message: You have used the Feather of Blessing to resurrect.
	YOU_HAVE_USED_THE_FEATHER_OF_BLESSING_TO_RESURRECT(2391),
	// Message: That pet/servitor skill cannot be used because it is recharging.
	THAT_PET_SERVITOR_SKILL_CANNOT_BE_USED_BECAUSE_IT_IS_RECHARGING(2396),
	// Message: Instant Zone currently in use: $s1
	INSTANT_ZONE_CURRENTLY_IN_USE_S1(2400),
	// Message: Clan lord $c2, who leads clan $s1, has been declared the lord of
	// the $s3 territory.
	CLAN_LORD_C2_WHO_LEADS_CLAN_S1_HAS_BEEN_DECLARED_THE_LORD_OF_THE_S3_TERRITORY(2401),
	// Message: The Territory War request period has ended.
	THE_TERRITORY_WAR_REQUEST_PERIOD_HAS_ENDED(2402),
	// Message: The Territory War begins in 10 minutes!
	THE_TERRITORY_WAR_BEGINS_IN_10_MINUTES(2403),
	// Message: The Territory War begins in 5 minutes!
	THE_TERRITORY_WAR_BEGINS_IN_5_MINUTES(2404),
	// Message: The Territory War begins in 1 minute!
	THE_TERRITORY_WAR_BEGINS_IN_1_MINUTE(2405),
	// Message: You are currently registered for a 3 vs. 3 class irrelevant team
	// match.
	YOU_ARE_CURRENTLY_REGISTERED_FOR_A_3_VS_3_CLASS_IRRELEVANT_TEAM_MATCH(2408),
	// Message: $c1 is already registered on the waiting list for the 3 vs. 3
	// class irrelevant team match.
	C1_IS_ALREADY_REGISTERED_ON_THE_WAITING_LIST_FOR_THE_3_VS_3_CLASS_IRRELEVANT_TEAM_MATCH(2440),
	// Message: Only a party leader can request a team match.
	ONLY_A_PARTY_LEADER_CAN_REQUEST_A_TEAM_MATCH(2441),
	// Message: The request cannot be made because the requirements have not
	// been met. To participate in a team match, you must first form a 3-member
	// party.
	THE_REQUEST_CANNOT_BE_MADE_BECAUSE_THE_REQUIREMENTS_HAVE_NOT_BEEN_MET(2442),
	// Message: The battlefield channel has been activated.
	THE_BATTLEFIELD_CHANNEL_HAS_BEEN_ACTIVATED(2445),
	// Message: The battlefield channel has been deactivated.
	THE_BATTLEFIELD_CHANNEL_HAS_BEEN_DEACTIVATED(2446),
	// Message: Five years have passed since this character's creation.
	FIVE_YEARS_HAVE_PASSED_SINCE_THIS_CHARACTERS_CREATION(2447),
	// Message: Your birthday gift has arrived. You can obtain it from the
	// Gatekeeper in any village.
	YOUR_BIRTHDAY_GIFT_HAS_ARRIVED(2448),
	// Message: There are $s1 days until your character's birthday. On that day,
	// you can obtain a special gift from the Gatekeeper in any village.
	THERE_ARE_S1_DAYS_UNTIL_YOUR_CHARACTERS_BIRTHDAY(2449),
	// Message: $c1's birthday is $s3/$s4/$s2.
	C1S_BIRTHDAY_IS_S3S4S2(2450),
	// Message: Your cloak has been unequipped because your armor set is no
	// longer complete.
	YOUR_CLOAK_HAS_BEEN_UNEQUIPPED_BECAUSE_YOUR_ARMOR_SET_IS_NO_LONGER_COMPLETE(2451),
	// Message: In order to acquire an airship, the clan's level must be level 5
	// or higher.
	IN_ORDER_TO_ACQUIRE_AN_AIRSHIP_THE_CLANS_LEVEL_MUST_BE_LEVEL_5_OR_HIGHER(2456),
	// Message: An airship cannot be summoned because either you have not
	// registered your airship license, or the airship has not yet been
	// summoned.
	AN_AIRSHIP_CANNOT_BE_SUMMONED_BECAUSE_EITHER_YOU_HAVE_NOT_REGISTERED_YOUR_AIRSHIP_LICENSE_OR_THE_AIRSHIP_HAS_NOT_YET_BEEN_SUMMONED(2457),
	// Message: Your clan's airship is already being used by another clan
	// member.
	YOUR_CLANS_AIRSHIP_IS_ALREADY_BEING_USED_BY_ANOTHER_CLAN_MEMBER(2458),
	// Message: The Airship Summon License has already been acquired.
	THE_AIRSHIP_SUMMON_LICENSE_HAS_ALREADY_BEEN_ACQUIRED(2459),
	// Message: The clan owned airship already exists.
	THE_CLAN_OWNED_AIRSHIP_ALREADY_EXISTS(2460),
	// Message: An airship cannot be summoned because you don't have enough $s1.
	AN_AIRSHIP_CANNOT_BE_SUMMONED_BECAUSE_YOU_DONT_HAVE_ENOUGH_S1(2462),
	// Message: The airship's fuel (EP) will soon run out.
	THE_AIRSHIPS_FUEL_EP_WILL_SOON_RUN_OUT(2463),
	// Message: The airship's fuel (EP) has run out. The airship's speed will be
	// greatly decreased in this condition.
	THE_AIRSHIPS_FUEL_EP_HAS_RUN_OUT(2464),
	// Message: Your ship cannot teleport because it does not have enough fuel
	// for the trip.
	YOUR_SHIP_CANNOT_TELEPORT_BECAUSE_IT_DOES_NOT_HAVE_ENOUGH_FUEL_FOR_THE_TRIP(2491),
	// Message: The $s1 ward has been destroyed! $c2 now has the territory ward.
	THE_S1_WARD_HAS_BEEN_DESTROYED_C2_NOW_HAS_THE_TERRITORY_WARD(2750),
	// Message: The character that acquired $s1's ward has been killed.
	THE_CHARACTER_THAT_ACQUIRED_S1S_WARD_HAS_BEEN_KILLED(2751),
	// Message: You cannot enter because you do not meet the requirements.
	YOU_CANNOT_ENTER_BECAUSE_YOU_DO_NOT_MEET_THE_REQUIREMENTS(2706),
	// Message: You cannot register while in possession of a cursed weapon.
	YOU_CANNOT_REGISTER_WHILE_IN_POSSESSION_OF_A_CURSED_WEAPON(2708),
	// Message: Applicants for the Olympiad, Underground Coliseum, or Kratei's
	// Cube matches cannot register.
	APPLICANTS_FOR_THE_OLYMPIAD_UNDERGROUND_COLISEUM_OR_KRATEIS_CUBE_MATCHES_CANNOT_REGISTER(2709),
	// Message: You cannot board because you do not meet the requirements.
	YOU_CANNOT_BOARD_BECAUSE_YOU_DO_NOT_MEET_THE_REQUIREMENTS(2727),
	// Message: You cannot control the helm while transformed.
	YOU_CANNOT_CONTROL_THE_HELM_WHILE_TRANSFORMED(2729),
	// Message: You cannot control the helm while you are petrified.
	YOU_CANNOT_CONTROL_THE_HELM_WHILE_YOU_ARE_PETRIFIED(2730),
	// Message: You cannot control the helm when you are dead.
	YOU_CANNOT_CONTROL_THE_HELM_WHEN_YOU_ARE_DEAD(2731),
	// Message: You cannot control the helm while fishing.
	YOU_CANNOT_CONTROL_THE_HELM_WHILE_FISHING(2732),
	// Message: You cannot control the helm while in a battle.
	YOU_CANNOT_CONTROL_THE_HELM_WHILE_IN_A_BATTLE(2733),
	// Message: You cannot control the helm while in a duel.
	YOU_CANNOT_CONTROL_THE_HELM_WHILE_IN_A_DUEL(2734),
	// Message: You cannot control the helm while in a sitting position.
	YOU_CANNOT_CONTROL_THE_HELM_WHILE_IN_A_SITTING_POSITION(2735),
	// Message: You cannot control the helm while using a skill.
	YOU_CANNOT_CONTROL_THE_HELM_WHILE_USING_A_SKILL(2736),
	// Message: You cannot control the helm while a cursed weapon is equipped.
	YOU_CANNOT_CONTROL_THE_HELM_WHILE_A_CURSED_WEAPON_IS_EQUIPPED(2737),
	// Message: You cannot control the helm while holding a flag.
	YOU_CANNOT_CONTROL_THE_HELM_WHILE_HOLDING_A_FLAG(2738),
	// Message: You cannot control the helm because you do not meet the
	// requirements.
	YOU_CANNOT_CONTROL_THE_HELM_BECAUSE_YOU_DO_NOT_MEET_THE_REQUIREMENTS(2739),
	// Message: This action is prohibited while steering.
	THIS_ACTION_IS_PROHIBITED_WHILE_STEERING(2740),
	// Message: This type of attack is prohibited when allied troops are the
	// target.
	THIS_TYPE_OF_ATTACK_IS_PROHIBITED_WHEN_ALLIED_TROOPS_ARE_THE_TARGET(2753),
	// Message: You cannot be simultaneously registered for PVP matches such as
	// the Olympiad, Underground Coliseum, Aerial Cleft, Kratei's Cube, and
	// Handy's Block Checkers.
	YOU_CANNOT_BE_SIMULTANEOUSLY_REGISTERED_FOR_PVP_MATCHES_SUCH_AS_THE_OLYMPIAD_UNDERGROUND_COLISEUM_AERIAL_CLEFT_KRATEIS_CUBE_AND_HANDYS_BLOCK_CHECKERS(2754),
	// Message: Another player is probably controlling the target.
	ANOTHER_PLAYER_IS_PROBABLY_CONTROLLING_THE_TARGET(2756),
	// Message: You must target the one you wish to control.
	YOU_MUST_TARGET_THE_ONE_YOU_WISH_TO_CONTROL(2761),
	// Message: You cannot control because you are too far.
	YOU_CANNOT_CONTROL_BECAUSE_YOU_ARE_TOO_FAR(2762),
	// Message: The effect of territory ward is disappearing.
	THE_EFFECT_OF_TERRITORY_WARD_IS_DISAPPEARING(2776),
	// Message: The airship summon license has been entered. Your clan can now
	// summon the airship.
	THE_AIRSHIP_SUMMON_LICENSE_HAS_BEEN_ENTERED(2777),
	// Message: You cannot teleport while in possession of a ward.
	YOU_CANNOT_TELEPORT_WHILE_IN_POSSESSION_OF_A_WARD(2778),
	// Message: Mercenary participation is requested in $s1 territory.
	MERCENARY_PARTICIPATION_IS_REQUESTED_IN_S1_TERRITORY(2788),
	// Message: Mercenary participation request is cancelled in $s1 territory.
	MERCENARY_PARTICIPATION_REQUEST_IS_CANCELLED_IN_S1_TERRITORY(2789),
	// Message: Clan participation is requested in $s1 territory.
	CLAN_PARTICIPATION_IS_REQUESTED_IN_S1_TERRITORY(2790),
	// Message: Clan participation request is cancelled in $s1 territory.
	CLAN_PARTICIPATION_REQUEST_IS_CANCELLED_IN_S1_TERRITORY(2791),
	// Message: You must have a minimum of ($s1) people to enter this Instant
	// Zone. Your request for entry is denied.
	YOU_MUST_HAVE_A_MINIMUM_OF_S1_PEOPLE_TO_ENTER_THIS_INSTANT_ZONE(2793),
	// Message: The territory war channel and functions will now be deactivated.
	THE_TERRITORY_WAR_CHANNEL_AND_FUNCTIONS_WILL_NOW_BE_DEACTIVATED(2794),
	// Message: You've already requested a territory war in another territory
	// elsewhere.
	YOUVE_ALREADY_REQUESTED_A_TERRITORY_WAR_IN_ANOTHER_TERRITORY_ELSEWHERE(2795),
	// Message: The clan who owns the territory cannot participate in the
	// territory war as mercenaries.
	THE_CLAN_WHO_OWNS_THE_TERRITORY_CANNOT_PARTICIPATE_IN_THE_TERRITORY_WAR_AS_MERCENARIES(2796),
	// Message: It is not a territory war registration period, so a request
	// cannot be made at this time.
	IT_IS_NOT_A_TERRITORY_WAR_REGISTRATION_PERIOD_SO_A_REQUEST_CANNOT_BE_MADE_AT_THIS_TIME(2797),
	// Message: The territory war will end in $s1-hour(s).
	THE_TERRITORY_WAR_WILL_END_IN_S1HOURS(2798),
	// Message: The territory war will end in $s1-minute(s).
	THE_TERRITORY_WAR_WILL_END_IN_S1MINUTES(2799),
	// Message: $s1-second(s) to the end of territory war!
	S1_SECONDS_TO_THE_END_OF_TERRITORY_WAR(2900),
	// Message: You cannot force attack a member of the same territory.
	YOU_CANNOT_FORCE_ATTACK_A_MEMBER_OF_THE_SAME_TERRITORY(2901),
	// Message: You've acquired the ward. Move quickly to your forces' outpost.
	YOUVE_ACQUIRED_THE_WARD(2902),
	// Message: Territory war has begun.
	TERRITORY_WAR_HAS_BEGUN(2903),
	// Message: Territory war has ended.
	TERRITORY_WAR_HAS_ENDED(2904),
	// Message: You've requested $c1 to be on your Friends List.
	YOUVE_REQUESTED_C1_TO_BE_ON_YOUR_FRIENDS_LIST(2911),
	// Message: Clan $s1 has succeeded in capturing $s2's territory ward.
	CLAN_S1_HAS_SUCCEEDED_IN_CAPTURING_S2S_TERRITORY_WARD(2913),
	// Message: The territory war will begin in 20 minutes! Territory related
	// functions (i.e.: battlefield channel, Disguise Scrolls, Transformations,
	// etc...) can now be used.
	THE_TERRITORY_WAR_WILL_BEGIN_IN_20_MINUTES(2914),
	// Message: This clan member cannot withdraw or be expelled while
	// participating in a territory war.
	THIS_CLAN_MEMBER_CANNOT_WITHDRAW_OR_BE_EXPELLED_WHILE_PARTICIPATING_IN_A_TERRITORY_WAR(2915),
	// Message: Only characters who are level 40 or above who have completed
	// their second class transfer can register in a territory war.
	ONLY_CHARACTERS_WHO_ARE_LEVEL_40_OR_ABOVE_WHO_HAVE_COMPLETED_THEIR_SECOND_CLASS_TRANSFER_CAN_REGISTER_IN_A_TERRITORY_WAR(2918),
	// Message: The disguise scroll cannot be used because it is meant for use
	// in a different territory.
	THE_DISGUISE_SCROLL_CANNOT_BE_USED_BECAUSE_IT_IS_MEANT_FOR_USE_IN_A_DIFFERENT_TERRITORY(2936),
	// Message: A territory owning clan member cannot use a disguise scroll.
	A_TERRITORY_OWNING_CLAN_MEMBER_CANNOT_USE_A_DISGUISE_SCROLL(2937),
	// Message: The disguise scroll cannot be used while you are engaged in a
	// private store or manufacture workshop.
	THE_DISGUISE_SCROLL_CANNOT_BE_USED_WHILE_YOU_ARE_ENGAGED_IN_A_PRIVATE_STORE_OR_MANUFACTURE_WORKSHOP(2938),
	// Message: A disguise cannot be used when you are in a chaotic state.
	A_DISGUISE_CANNOT_BE_USED_WHEN_YOU_ARE_IN_A_CHAOTIC_STATE(2939),
	// Message: The territory war exclusive disguise and transformation can be
	// used 20 minutes before the start of the territory war to 10 minutes after
	// its end.
	THE_TERRITORY_WAR_EXCLUSIVE_DISGUISE_AND_TRANSFORMATION_CAN_BE_USED_20_MINUTES_BEFORE_THE_START_OF_THE_TERRITORY_WAR_TO_10_MINUTES_AFTER_ITS_END(2955),
	// Message: A character born on February 29 will receive a gift on February
	// 28.
	A_CHARACTER_BORN_ON_FEBRUARY_29_WILL_RECEIVE_A_GIFT_ON_FEBRUARY_28(2957),
	// Message: An Agathion has already been summoned.
	AN_AGATHION_HAS_ALREADY_BEEN_SUMMONED(2958),
	// Message: The Command Channel matching room was cancelled.
	THE_COMMAND_CHANNEL_MATCHING_ROOM_WAS_CANCELLED(2994),
	// Message: You cannot enter the Command Channel matching room because you
	// do not meet the requirements.
	YOU_CANNOT_ENTER_THE_COMMAND_CHANNEL_MATCHING_ROOM_BECAUSE_YOU_DO_NOT_MEET_THE_REQUIREMENTS(2996),
	// Message: You exited from the Command Channel matching room.
	YOU_EXITED_FROM_THE_COMMAND_CHANNEL_MATCHING_ROOM(2997),
	// Message: You were expelled from the Command Channel matching room.
	YOU_WERE_EXPELLED_FROM_THE_COMMAND_CHANNEL_MATCHING_ROOM(2998),
	// Message: The Command Channel affiliated party's party member cannot use
	// the matching screen.
	THE_COMMAND_CHANNEL_AFFILIATED_PARTYS_PARTY_MEMBER_CANNOT_USE_THE_MATCHING_SCREEN(2999),
	// Message: The Command Channel matching room was created.
	THE_COMMAND_CHANNEL_MATCHING_ROOM_WAS_CREATED(3000),
	// Message: The Command Channel matching room information was edited.
	THE_COMMAND_CHANNEL_MATCHING_ROOM_INFORMATION_WAS_EDITED(3001),
	// Message: $c1 entered the Command Channel matching room.
	C1_ENTERED_THE_COMMAND_CHANNEL_MATCHING_ROOM(3003),
	// Message: A user currently participating in the Olympiad cannot send party
	// and friend invitations.
	A_USER_CURRENTLY_PARTICIPATING_IN_THE_OLYMPIAD_CANNOT_SEND_PARTY_AND_FRIEND_INVITATIONS(3094),
	// Message: The couple action was denied.
	THE_COUPLE_ACTION_WAS_DENIED(3119),
	// Message: The request cannot be completed because the target does not meet
	// location requirements.
	THE_REQUEST_CANNOT_BE_COMPLETED_BECAUSE_THE_TARGET_DOES_NOT_MEET_LOCATION_REQUIREMENTS(3120),
	// Message: The couple action was cancelled.
	THE_COUPLE_ACTION_WAS_CANCELLED(3121),
	// Message: $c1 is already participating in a couple action and cannot be
	// requested for another couple action.
	C1_IS_ALREADY_PARTICIPATING_IN_A_COUPLE_ACTION_AND_CANNOT_BE_REQUESTED_FOR_ANOTHER_COUPLE_ACTION(3126),
	// Message: You have requested a couple action with $c1.
	YOU_HAVE_REQUESTED_A_COUPLE_ACTION_WITH_C1(3150),
	// Message: $c1 is set to refuse duel requests and cannot receive a duel
	// request.
	C1_IS_SET_TO_REFUSE_DUEL_REQUESTS_AND_CANNOT_RECEIVE_A_DUEL_REQUEST(3169),
	// Message: $s1 was successfully added to your Contact List.
	S1_WAS_SUCCESSFULLY_ADDED_TO_YOUR_CONTACT_LIST(3214),
	// Message: The name $s1% doesn't exist. Please try another name.
	THE_NAME_S1__DOESNT_EXIST(3215),
	// Message: The name already exists on the added list.
	THE_NAME_ALREADY_EXISTS_ON_THE_ADDED_LIST(3216),
	// Message: The name is not currently registered.
	THE_NAME_IS_NOT_CURRENTLY_REGISTERED(3217),
	// Message: $s1 was successfully deleted from your Contact List.
	S1_WAS_SUCCESSFULLY_DELETED_FROM_YOUR_CONTACT_LIST(3219),
	// Message: You cannot add your own name.
	YOU_CANNOT_ADD_YOUR_OWN_NAME(3221),
	// Message: The maximum number of names (100) has been reached. You cannot
	// register any more.
	THE_MAXIMUM_NUMBER_OF_NAMES_100_HAS_BEEN_REACHED(3222),
	// Message: The maximum matches you can participate in 1 week is 70.
	THE_MAXIMUM_MATCHES_YOU_CAN_PARTICIPATE_IN_1_WEEK_IS_70(3224),
	// Message: The total number of matches that can be entered in 1 week is 60
	// class irrelevant individual matches, 30 specific matches, and 10 team
	// matches.
	THE_TOTAL_NUMBER_OF_MATCHES_THAT_CAN_BE_ENTERED_IN_1_WEEK_IS_60_CLASS_IRRELEVANT_INDIVIDUAL_MATCHES_30_SPECIFIC_MATCHES_AND_10_TEAM_MATCHES(3225),
	// Message: MP became 0 and the Arcane Shield is disappearing.
	MP_BECAME_0_AND_THE_ARCANE_SHIELD_IS_DISAPPEARING(3256),
	// Message: You have acquired $s1 EXP (Bonus: $s2) and $s3 SP (Bonus: $s4).
	YOU_HAVE_ACQUIRED_S1_EXP_BONUS_S2_AND_S3_SP_BONUS_S4(3259),
	// Message: You have $s1 match(es) remaining that you can participate in
	// this week ($s2 1 vs 1 Class matches, $s3 1 vs 1 matches, & $s4 3 vs 3
	// Team matches).
	YOU_HAVE_S1_MATCHES_REMAINING_THAT_YOU_CAN_PARTICIPATE_IN_THIS_WEEK_S2_1_VS_1_CLASS_MATCHES_S3_1_VS_1_MATCHES__S4_3_VS_3_TEAM_MATCHES(3261),
	// Message: There are $s2 seconds remaining for $s1's re-use time. It is
	// reset every day at 6:30 AM.
	THERE_ARE_S2_SECONDS_REMAINING_FOR_S1S_REUSE_TIME(3263),
	// Message: There are $s2 minutes $s3 seconds remaining for $s1's re-use
	// time. It is reset every day at 6:30 AM.
	THERE_ARE_S2_MINUTES_S3_SECONDS_REMAINING_FOR_S1S_REUSE_TIME(3264),
	// Message: There are $s2 hours $s3 minutes $s4 seconds remaining for $s1's
	// re-use time. It is reset every day at 6:30 AM.
	THERE_ARE_S2_HOURS_S3_MINUTES_S4_SECONDS_REMAINING_FOR_S1S_REUSE_TIME(3265),
	// Message: $c1 is set to refuse couple actions and cannot be requested for
	// a couple action.
	C1_IS_SET_TO_REFUSE_COUPLE_ACTIONS_AND_CANNOT_BE_REQUESTED_FOR_A_COUPLE_ACTION(3164),
	// Message: The angel Nevit has blessed you from above. You are imbued with
	// full Vitality as well as a Vitality Replenishing effect. And should you
	// die, you will not lose Exp!
	THE_ANGEL_NEVIT_HAS_BLESSED_YOU_FROM_ABOVE_YOU_ARE_IMBUED_WITH_FULL_VITALITY_AS_WELL_AS_A_VITALITY_REPLENISHING_EFFECT(3266),
	// Message: You are starting to feel the effects of Nevit's Advent Blessing.
	YOU_ARE_STARTING_TO_FEEL_THE_EFFECTS_OF_NEVITS_BLESSING(3267),
	// Message: You are further infused with the blessings of Nevit! Continue to
	// battle evil wherever it may lurk.
	YOU_ARE_FURTHER_INFUSED_WITH_THE_BLESSINGS_OF_NEVIT_CONTINUE_TO_BATTLE_EVIL_WHEREVER_IT_MAY_LURK(3268),
	// Message: Nevit's Advent Blessing shines strongly from above. You can
	// almost see his divine aura.
	NEVITS_BLESSING_SHINES_STRONGLY_FROM_ABOVE_YOU_CAN_ALMOST_SEE_HIS_DIVINE_AURA(3269),
	// Message: Nevit's Advent Blessing has ended. Continue your journey and you
	// will surely meet his favor again sometime soon.
	NEVITS_BLESSING_HAS_ENDED_CONTINUE_YOUR_JOURNEY_AND_YOU_WILL_SURELY_MEET_HIS_FAVOR_AGAIN_SOMETIME_SOON(3275),
	THE_CORRESPONDING_WORK_CANNOT_BE_PROCEEDED_BECAUSE_THE_INVENTORY_WEIGHTQUANTITY_LIMIT_HAS_BEEN_EXCEEDED(3646), 
	// Message: You cannor change an attribute while using a private shop or workshop.
	YOU_CANNOT_CHANGE_AN_ATTRIBUTE_WHILE_USING_A_PRIVATE_SHOP_OR_WORKSHOP(3659),
	// Message: Enchantment or attribute enchantment is in progress.
	ENCHANTMENT_OR_ATTRIBUTE_ENCHANTMENT_IS_IN_PROGRESS(3660),
	// Message: Changing attribute has been failed.
	CHANGING_ATTRIBUTES_HAS_BEEN_FAILED(3661),
	// Message: You cannot change attributes while exchanging.
	YOU_CANNOT_CHANGE_ATTRIBUTES_WHILE_EXCHANGING(3662),
	// Message: Started searching the party.
	STARTED_SEARCHING_THE_PARTY(3452),
	// Message: Stopped searching the party.
	STOPPED_SEARCHING_THE_PARTY(3453),
	// Message: Vitality is applied, and you are receiving 300% bonus XP while
	// hunting. You can use a maximum of 5 Vitality items per week, including
	// Replenishing and Maintaining items.
	VITALITY_IS_APPLIED_300(3525),
	// Message: The skill has been canceled because you have insufficient
	// Energy.
	THE_SKILL_HAS_BEEN_CANCELED_BECAUSE_YOU_HAVE_INSUFFICIENT_ENERGY(6042),
	// Message: Your energy cannot be replenished because conditions are not
	// met.
	YOUR_ENERGY_CANNOT_BE_REPLENISHED_BECAUSE_CONDITIONS_ARE_NOT_MET(6043),
	// Message: Energy $s1 replenished.
	ENERGY_S1_REPLENISHED(6044),
	// Message: In the item <$s1> attribute <$s2> successfully changed to <$
	// s3>.
	IN_THE_ITEM_S1_ATTRIBUTE_S2_SUCCESSFULLY_CHANGED_TO_S3(3668),
	// Message: The item for changing an attribute does not exist.
	THE_ITEM_FOR_CHANGING_AN_ATTRIBUTE_DOES_NOT_EXIST(3669),
	// Message: You can not change the attribute while operating a private store
	// or private workshop.
	YOU_CAN_NOT_CHANGE_THE_ATTRIBUTE_WHILE_OPERATING_A_PRIVATE_STORE_OR_PRIVATE_WORKSHOP(3659),
	// Message: You cannot bookmark this location because you do not have a My
	// Teleport Flag.
	YOU_CANNOT_BOOKMARK_THIS_LOCATION_BECAUSE_YOU_DO_NOT_HAVE_A_MY_TELEPORT_FLAG(6501),
	// Message: Item Purchase has failed
	ITEM_PURCHASE_HAS_FAILED(3371),
	// Message: Cancellation of Sale for the item is successful.
	CANCELLATION_OF_SALE_FOR_THE_ITEM_IS_SUCCESSFUL(3485),
	// Message: You do not have enough Adena to register the item
	YOU_DO_NOT_HAVE_ENOUGH_ADENA_TO_REGISTER_THE_ITEM(3364),
	// Message: Congratulations - You've completed a class transfer!
	CONGRATULATIONS__YOUVE_COMPLETED_A_CLASS_TRANSFER(1308),
	// Message: Congratulations - You've completed your third-class transfer
	// quest!
	CONGRATULATIONS__YOUVE_COMPLETED_YOUR_THIRDCLASS_TRANSFER_QUEST(1606),
	// Message: You can not change the class in a state of transformation.
	YOU_CAN_NOT_CHANGE_CLASS_IN_TRANSFORMATION(3677),
	// Message: The number of My Teleports slots has been increased.
	THE_NUMBER_OF_MY_TELEPORTS_SLOTS_HAS_BEEN_INCREASED(2409),
	// Message: Will you join <$s1>, <$s2> party as <$s3>?
	WILL_YOU_JOIN_S1_S2_PARTY_AS_S3(3461),
	// Message: You can not change the class due to disruption of the
	// identification.
	YOU_CAN_NOT_CHANGE_CLASS_DUE_TO_DISRUPTION_OF_THE_IDENTIFICATION(3574),
	// Message: Player $c1 will be replaced. Replacement will occur within 3
	// minutes upon approval by the party leader
	PLAYER_C1_WILL_BE_REPLACED(3679),
	// Message: You have accepted to join a party. Replacement will occur within
	// 3 minutes upon approval by the party leader.
	YOU_HAVE_ACCEPTED_TO_JOIN_A_PARTY(3683),
	// Message: This territory can not change class.
	THIS_TERRITORY_CAN_NOT_CHANGE_CLASS(3684),
	$C1_USED_$S3_ON_$C2(3463),
	// Message: Some Lineage II features have been limited for free
	// trials. Trial accounts aren’t allowed buy items from private stores. To
	// unlock all of the features of Lineage II, purchase the full
	// version today.
	SOME_LINEAGE_II_FEATURES_HAVE_BEEN_LIMITED_FOR_FREE_TRIALS_____(2046),
	// Message: The recipe is incorrect.
	THE_RECIPE_IS_INCORRECT(852),
	// Message: You do not have enough materials to perform that action.
	YOU_DO_NOT_HAVE_ENOUGH_MATERIALS_TO_PERFORM_THAT_ACTION(341),
	// Message: Please register a recipe.
	PLEASE_REGISTER_A_RECIPE(859),
	// Message: You cannot do that while fishing.
	YOU_CANNOT_DO_THAT_WHILE_FISHING(1447),
	// Message: The mentoring relationship with $s1 has been canceled. The
	// mentor cannot obtain another mentee for one week
	THE_MENTORING_RELATIONSHIP_WITH_S1_HAS_BEEN_CANCELED(3689),
	// Message: Do you wish to make $s1 your mentor? (Class: $s2 / Level: $s3)
	DO_TOU_WISH_TO_MAKE_S1_YOUR_MENTOR_CLASS_S2_LEVEL_S3(3690),
	// Message: From now on, $s1 will be your mentor
	FROM_NOW_ON_S1_WILL_BE_YOUR_MENTOR(3691),
	// Message: From now on, $s1 will be your mentee.
	FROM_NOW_ON_S1_WILL_BE_YOUR_MENTEE(3692),
	// Message: A mentor can have up to 3 mentees at the same time.
	A_MENTOR_CAN_HAVE_UP_TO_3_MENTEES_AT_THE_SAME_TIME(3693),
	// Message: You must awaken in order to become a mentor.
	YOU_MUST_AWAKEN_IN_ORDER_TO_BECOME_A_MENTOR(3694),
	// Message: Your mentee $s1 has connected.
	YOU_MENTEE_S1_HAS_CONNECTED(3695),
	// Message: Your mentor $s1 has connected
	YOU_MENTOR_S1_HAS_CONNECTED(3696),
	// Message: Your mentee $s1 has disconnected.
	YOU_MENTEE_S1_HAS_DISCONNECTED(3697),
	// Message: Your mentor $s1 has disconnected.
	YOU_MENTOR_S1_HAS_DISCONNECTED(3698),
	// Message: $s1 has declined becoming your mentee
	S1_HAS_DECLINED_BECOMING_YOUR_MENTEE(3699),
	// Message: You cannot become your own mentee.
	YOU_CANNOT_BECOME_YOUR_OWN_MENTEE(3701),
	// Message: $s1 already has a mentor.
	S1_ALREADY_HAS_A_MENTOR(3702),
	// Message: $s1 is above level 86 and cannot become a mentee
	S1_IS_ABOVE_LEVEL_86_AND_CANNOT_BECOME_A_MENTEE(3703),
	// Message: $s1 does not have the item needed to become a mentee.
	S1_DOES_NOT_HAVE_THE_ITEM_NEDEED_TO_BECOME_A_MENTEE(3704),
	// Message: The mentee $s1 reached level 86, so the mentoring relationship
	// was ended. After the mentee's graduation, the mentor cannot obtain
	// another mentee for 5 days.
	THE_MENTEE_S1_HAS_REACHED_LEVEL_86(3705),
	// Message: You reached level 86, so the mentoring relationship with your
	// mentor $s1 came to an end.
	YOU_REACHED_LEVEL_86_RELATIONSHIP_WITH_S1_CAME_TO_AN_END(3706),
	// Message: You have offered to become $s1's mentor
	YOU_HAVE_OFFERED_TO_BECOME_S1_MENTOR(3707),
	// Message: You can bond with a new mentee in $s1 day(s) $s2 hour(s) $s3
	// minute(s).
	YOU_CAN_BOND_WITH_A_NEW_MENTEE_IN_S1_DAYS_S2_HOUR_S3_MINUTE(3713),
	// Message: Not enough MP.
	NOT_ENOUGH_MP(24),
	// Message: You may not use Sayune while pet or summoned pet is out
	YOU_MAY_NOT_USE_SAYUNE_WHILE_PET_OR_SUMMONED_PET_IS_OUT(3625),
	// Message: Invitation can occur only when the mentee is in main class status
	INVITATION_CAN_OCCUR_ONLY_WHEN_THE_MENTEE_IS_IN_MAIN_CLASS_STATUS(3710),
	// Message: A Mark of Adventurer is acquired. This item can be re-acquired after 6:30 a.m. everyday
	A_MARK_OF_ADVENTURER_IS_ACQUIRED_THIS_ITEM_CAN_BE_RE_ACQUIRED_EVERYDAY(3495),
	// Message: This account has already received a gift
	SUBCLASS_S1_HAS_BEEN_UPGRADED_TO_DUEL_CLASS_S2_CONGRATULATIONS(3279),
	THIS_ACCOUNT_HAS_ALREADY_RECEIVED_A_GIFT(3289),
	YOU_CANNOT_EXTRACT_FROM_A_MODIFIED_ITEM(6093),
	THIS_ITEM_DOES_NOT_MEET_REQUIREMENTS(6094),
	YOU_CANNOT_MODIFY_AS_YOU_DO_NOT_HAVE_ENOUGH_ADENA(6099),
	YOU_HAVE_SPENT_S1_ON_A_SUCCESSFUL_APPEARANCE_MODIFICATION(6100),
	ITEM_GRADES_DO_NOT_MATCH(6101),
	YOU_CANNOT_EXTRACT_FROM_ITEMS_THAT_ARE_HIGHERGRADE_THAN_ITEMS_TO_BE_MODIFIED(6102),
	YOU_CANNOT_MODIFY_OR_RESTORE_NOGRADE_ITEMS(6103),
	WEAPONS_ONLY(6104),
	ARMOR_ONLY(6105);

	private final L2GameServerPacket _message;
	private final int _id;
	private final byte _size;

	SystemMsg(int i)
	{
		_id = i;

		if (name().contains("S4") || name().contains("C4"))
		{
			_size = 4;
			_message = null;
		}
		else if (name().contains("S3") || name().contains("C3"))
		{
			_size = 3;
			_message = null;
		}
		else if (name().contains("S2") || name().contains("C2"))
		{
			_size = 2;
			_message = null;
		}
		else if (name().contains("S1") || name().contains("C1"))
		{
			_size = 1;
			_message = null;
		}
		else
		{
			_size = 0;
			_message = new SystemMessage2(this);
		}
	}

	public int getId()
	{
		return _id;
	}

	public byte size()
	{
		return _size;
	}

	public static SystemMsg valueOf(int id)
	{
		for (SystemMsg m : values())
			if (m.getId() == id)
				return m;

		throw new NoSuchElementException("Not find SystemMsg by id: " + id);
	}

	@Override
	public L2GameServerPacket packet(Player player)
	{
		if (_message == null)
			throw new NoSuchElementException("Running SystemMsg.packet(Player), but message require arguments: " + name());

		return _message;
	}
}
