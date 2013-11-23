/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.clientpackets;

import l2p.gameserver.Announcements;
import l2p.gameserver.Config;
import l2p.gameserver.dao.MailDAO;
import l2p.gameserver.data.StringHolder;
import l2p.gameserver.data.xml.holder.ResidenceHolder;
import l2p.gameserver.instancemanager.*;
import l2p.gameserver.listener.actor.player.OnAnswerListener;
import l2p.gameserver.listener.actor.player.impl.ReviveAnswerListener;
import l2p.gameserver.model.*;
import l2p.gameserver.model.base.InvisibleType;
import l2p.gameserver.model.entity.events.impl.ClanHallAuctionEvent;
import l2p.gameserver.model.entity.residence.Castle;
import l2p.gameserver.model.entity.residence.ClanHall;
import l2p.gameserver.model.items.ItemInstance;
import l2p.gameserver.model.mail.Mail;
import l2p.gameserver.model.pledge.Clan;
import l2p.gameserver.model.pledge.SubUnit;
import l2p.gameserver.model.pledge.UnitMember;
import l2p.gameserver.network.GameClient;
import l2p.gameserver.network.serverpackets.*;
import l2p.gameserver.network.serverpackets.ConfirmDlg;
import l2p.gameserver.network.serverpackets.PledgeRecruit.ExPledgeWaitingListAlarm;
import l2p.gameserver.network.serverpackets.company.MPCC.ExMPCCOpen;
import l2p.gameserver.network.serverpackets.company.party.PartySmallWindowAll;
import l2p.gameserver.network.serverpackets.company.party.PartySpelled;
import l2p.gameserver.network.serverpackets.company.pledge.PledgeShowInfoUpdate;
import l2p.gameserver.network.serverpackets.company.pledge.PledgeShowMemberListUpdate;
import l2p.gameserver.network.serverpackets.company.pledge.PledgeSkillList;
import l2p.gameserver.network.serverpackets.components.SystemMsg;
import l2p.gameserver.scripts.Functions;
import l2p.gameserver.skills.AbnormalEffect;
import l2p.gameserver.tables.SkillTable;
import l2p.gameserver.templates.item.ItemTemplate;
import l2p.gameserver.utils.GameStats;
import l2p.gameserver.utils.ItemFunctions;
import l2p.gameserver.utils.TradeHelper;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;

public class EnterWorld extends L2GameClientPacket {
    private static final Object _lock = new Object();

    private static final Logger _log = LoggerFactory.getLogger(EnterWorld.class);

    @Override
    protected void readImpl() {
        //readS(); - клиент всегда отправляет строку "narcasse"
    }

    @Override
    protected void runImpl() {
        GameClient client = getClient();
        Player activeChar = client.getActiveChar();

        if (activeChar == null) {
            client.closeNow(false);
            return;
        }

        int MyObjectId = activeChar.getObjectId();
        Long MyStoreId = activeChar.getStoredId();

        synchronized (_lock)//TODO [G1ta0] че это за хуйня, и почему она тут
        {
            for (Castle castle : ResidenceHolder.getInstance().getResidenceList(Castle.class)) {
                activeChar.sendPacket(new ExCastleState(castle));
            }
            for (Player cha : GameObjectsStorage.getAllPlayersForIterate()) {
                if (MyStoreId == cha.getStoredId())
                    continue;
                try {
                    if (cha.getObjectId() == MyObjectId) {
                        _log.warn("Double EnterWorld for char: " + activeChar.getName());
                        cha.kick();
                    }
                } catch (Exception e) {
                    _log.error("", e);
                }
            }
        }

        GameStats.incrementPlayerEnterGame();

        boolean first = activeChar.entering;

        if (first) {
            activeChar.setOnlineStatus(true);
            if (activeChar.getPlayerAccess().GodMode && !Config.SHOW_GM_LOGIN)
                activeChar.setInvisibleType(InvisibleType.NORMAL);

            activeChar.setNonAggroTime(Long.MAX_VALUE);
            activeChar.spawnMe();

            if (activeChar.isInStoreMode())
                if (!TradeHelper.checksIfCanOpenStore(activeChar, activeChar.getPrivateStoreType())) {
                    activeChar.setPrivateStoreType(Player.STORE_PRIVATE_NONE);
                    activeChar.standUp();
                    activeChar.broadcastCharInfo();
                }

            activeChar.setRunning();
            activeChar.standUp();
            activeChar.startTimers();
        }

        activeChar.sendPacket(new ExBR_PremiumState(activeChar, activeChar.hasBonus()));

        activeChar.getMacroses().sendUpdate();
        activeChar.sendPacket(new SSQInfo(), new HennaInfo(activeChar));
        activeChar.sendItemList(false);
        activeChar.sendPacket(new ShortCutInit(activeChar), new SkillList(activeChar), new SkillCoolTime(activeChar));

        for (Castle castle : ResidenceHolder.getInstance().getResidenceList(Castle.class)) {
            activeChar.sendPacket(new ExCastleState(castle));
        }

        activeChar.sendPacket(SystemMsg.VITALITY_IS_APPLIED_300);

        activeChar.sendPacket(SystemMsg.WELCOME_TO_THE_WORLD_OF_LINEAGE_II);
        activeChar.sendPacket(new ExBR_NewIConCashBtnWnd(), new ExPledgeWaitingListAlarm());
        Announcements.getInstance().showAnnouncements(activeChar);

        if (first)
            activeChar.getListeners().onEnter();

        if (first && activeChar.getCreateTime() > 0) {
            Calendar create = Calendar.getInstance();
            create.setTimeInMillis(activeChar.getCreateTime());
            Calendar now = Calendar.getInstance();

            int day = create.get(Calendar.DAY_OF_MONTH);
            if (create.get(Calendar.MONTH) == Calendar.FEBRUARY && day == 29)
                day = 28;

            int myBirthdayReceiveYear = activeChar.getVarInt(Player.MY_BIRTHDAY_RECEIVE_YEAR, 0);
            if (create.get(Calendar.MONTH) == now.get(Calendar.MONTH) && create.get(Calendar.DAY_OF_MONTH) == day)
                if (myBirthdayReceiveYear == 0 && create.get(Calendar.YEAR) != now.get(Calendar.YEAR) || myBirthdayReceiveYear > 0 && myBirthdayReceiveYear != now.get(Calendar.YEAR)) {
                    Mail mail = new Mail();
                    mail.setSenderId(1);
                    mail.setSenderName(StringHolder.getInstance().getNotNull(activeChar, "birthday.npc"));
                    mail.setReceiverId(activeChar.getObjectId());
                    mail.setReceiverName(activeChar.getName());
                    mail.setTopic(StringHolder.getInstance().getNotNull(activeChar, "birthday.title"));
                    mail.setBody(StringHolder.getInstance().getNotNull(activeChar, "birthday.text"));

                    ItemInstance item = ItemFunctions.createItem(21169);
                    item.setLocation(ItemInstance.ItemLocation.MAIL);
                    item.setCount(1L);
                    item.save();

                    mail.addAttachment(item);
                    mail.setUnread(true);
                    mail.setType(Mail.SenderType.BIRTHDAY);
                    mail.setExpireTime(720 * 3600 + (int) (System.currentTimeMillis() / 1000L));
                    mail.save();

                    activeChar.setVar(Player.MY_BIRTHDAY_RECEIVE_YEAR, String.valueOf(now.get(Calendar.YEAR)), -1);
                }
        }

        if (activeChar.getClan() != null) {
            notifyClanMembers(activeChar);

            activeChar.sendPacket(activeChar.getClan().listAll());
            activeChar.sendPacket(new PledgeShowInfoUpdate(activeChar.getClan()), new PledgeSkillList(activeChar.getClan()));
        }

        // engage and notify Partner
        if (first && Config.ALLOW_WEDDING) {
            CoupleManager.getInstance().engage(activeChar);
            CoupleManager.getInstance().notifyPartner(activeChar);
        }

        if (first) {
            activeChar.getFriendList().notifyFriends(true);
            activeChar.restoreDisableSkills();
            activeChar.mentoringLoginConditions();
        }

        sendPacket(new L2FriendList(activeChar), new ExStorageMaxCount(activeChar), new QuestList(activeChar), new ExBasicActionList(activeChar), new EtcStatusUpdate(activeChar));

        activeChar.checkHpMessages(activeChar.getMaxHp(), activeChar.getCurrentHp());
        activeChar.checkDayNightMessages();

        if (Config.PETITIONING_ALLOWED)
            PetitionManager.getInstance().checkPetitionMessages(activeChar);

        if (!first) {
            if (activeChar.isCastingNow()) {
                Creature castingTarget = activeChar.getCastingTarget();
                Skill castingSkill = activeChar.getCastingSkill();
                long animationEndTime = activeChar.getAnimationEndTime();
                if (castingSkill != null && castingTarget != null && castingTarget.isCreature() && activeChar.getAnimationEndTime() > 0)
                    sendPacket(new MagicSkillUse(activeChar, castingTarget, castingSkill.getId(), castingSkill.getLevel(), (int) (animationEndTime - System.currentTimeMillis()), 0));
            }

            if (activeChar.isInBoat())
                activeChar.sendPacket(activeChar.getBoat().getOnPacket(activeChar, activeChar.getInBoatPosition()));

            if (activeChar.isMoving || activeChar.isFollow)
                sendPacket(activeChar.movePacket());

            if (activeChar.getMountNpcId() != 0)
                sendPacket(new Ride(activeChar));

            if (activeChar.isFishing())
                activeChar.stopFishing();
        }

        activeChar.entering = false;
        activeChar.sendUserInfo(true);

        if (activeChar.isSitting())
            activeChar.sendPacket(new ChangeWaitType(activeChar, ChangeWaitType.WT_SITTING));
        if (activeChar.getPrivateStoreType() != Player.STORE_PRIVATE_NONE)
            if (activeChar.getPrivateStoreType() == Player.STORE_PRIVATE_BUY)
                sendPacket(new PrivateStoreMsgBuy(activeChar));
            else if (activeChar.getPrivateStoreType() == Player.STORE_PRIVATE_SELL || activeChar.getPrivateStoreType() == Player.STORE_PRIVATE_SELL_PACKAGE)
                sendPacket(new PrivateStoreMsgSell(activeChar));
            else if (activeChar.getPrivateStoreType() == Player.STORE_PRIVATE_MANUFACTURE)
                sendPacket(new RecipeShopMsg(activeChar));

        if (activeChar.isDead())
            sendPacket(new Die(activeChar));

        activeChar.unsetVar("offline");

        // на всякий случай
        activeChar.sendActionFailed();

        if (first && activeChar.isGM() && Config.SAVE_GM_EFFECTS && activeChar.getPlayerAccess().CanUseGMCommand) {
            //silence
            if (activeChar.getVarB("gm_silence")) {
                activeChar.setMessageRefusal(true);
                activeChar.sendPacket(SystemMsg.MESSAGE_REFUSAL_MODE);
            }
            //invul
            if (activeChar.getVarB("gm_invul")) {
                activeChar.setIsInvul(true);
                activeChar.startAbnormalEffect(AbnormalEffect.INVUL2);
                activeChar.sendMessage(activeChar.getName() + " is now immortal.");
            }
            //gmspeed
            try {
                int var_gmspeed = Integer.parseInt(activeChar.getVar("gm_gmspeed"));
                if (var_gmspeed >= 1 && var_gmspeed <= 4)
                    activeChar.doCast(SkillTable.getInstance().getInfo(7029, var_gmspeed), activeChar, true);
            } catch (Exception E) {
            }
        }

        PlayerMessageStack.getInstance().CheckMessages(activeChar);

        sendPacket(ClientSetTime.STATIC, new ExSetCompassZoneCode(activeChar));

        Pair<Integer, OnAnswerListener> entry = activeChar.getAskListener(false);
        if (entry != null && entry.getValue() instanceof ReviveAnswerListener)
            sendPacket(new ConfirmDlg(SystemMsg.C1_IS_MAKING_AN_ATTEMPT_TO_RESURRECT_YOU_IF_YOU_CHOOSE_THIS_PATH_S2_EXPERIENCE_WILL_BE_RETURNED_FOR_YOU, 0).addString("Other player").addString("some"));

        if (activeChar.isCursedWeaponEquipped())
            CursedWeaponsManager.getInstance().showUsageTime(activeChar, activeChar.getCursedWeaponEquippedId());

        if (!first) {
            //Персонаж вылетел во время просмотра
            if (activeChar.isInObserverMode()) {
                if (activeChar.getObserverMode() == Player.OBSERVER_LEAVING)
                    activeChar.returnFromObserverMode();
                else if (activeChar.getOlympiadObserveGame() != null)
                    activeChar.leaveOlympiadObserverMode(true);
                else
                    activeChar.leaveObserverMode();
            } else if (activeChar.isVisible())
                World.showObjectsToPlayer(activeChar);

            for (Summon summon : activeChar.getSummonList())
                sendPacket(new PetInfo(summon));

            if (activeChar.isInParty()) {
                //sends new member party window for all members
                //we do all actions before adding member to a list, this speeds things up a little
                sendPacket(new PartySmallWindowAll(activeChar.getParty(), activeChar));

                for (Player member : activeChar.getParty().getPartyMembers())
                    if (member != activeChar) {
                        sendPacket(new PartySpelled(member, true));
                        for (Summon summon : member.getSummonList())
                            sendPacket(new PartySpelled(summon, true));

                        sendPacket(RelationChanged.update(activeChar, member, activeChar));
                    }

                // Если партия уже в СС, то вновь прибывшем посылаем пакет открытия окна СС
                if (activeChar.getParty().isInCommandChannel())
                    sendPacket(ExMPCCOpen.STATIC);
            }

            for (int shotId : activeChar.getAutoSoulShot())
                sendPacket(new ExAutoSoulShot(shotId, true));

            for (Effect e : activeChar.getEffectList().getAllFirstEffects())
                if (e.getSkill().isToggle())
                    sendPacket(new MagicSkillLaunched(activeChar.getObjectId(), e.getSkill().getId(), e.getSkill().getLevel(), activeChar));

            activeChar.broadcastCharInfo();
        } else
            activeChar.sendUserInfo(); // Отобразит права в клане

        activeChar.updateEffectIcons();
        activeChar.updateStats();

        if (Functions.IsActive("PcCafePointsExchange"))
            activeChar.sendPacket(new ExPCCafePointInfo(activeChar, 0, 1, 2, 12));

        if (Functions.IsActive("PcCafePointsExchange") && activeChar.getVar("pcBangOnStart") == null) {
            activeChar.addPcBangPoints(Config.ALT_PCBANG_POINTS_ON_START, false);
            activeChar.setVar("pcBangOnStart", String.valueOf(Config.ALT_PCBANG_POINTS_ON_START), System.currentTimeMillis() / 1000L + 86400);
        } else if (Functions.IsActive("PcCafePointsExchange"))
            activeChar.sendMessage(activeChar.isLangRus() ? "Вы уже получали очки PC Cafe сегодня" : "You already received PC Cafe points today");

        if (!activeChar.getPremiumItemList().isEmpty())
            activeChar.sendPacket(Config.GOODS_INVENTORY_ENABLED ? ExGoodsInventoryChangedNotify.STATIC : ExNotifyPremiumItem.STATIC);

        activeChar.sendVoteSystemInfo();
        activeChar.sendPacket(new ExReceiveShowPostFriend(activeChar));

        checkNewMail(activeChar);

        // GoD
        activeChar.sendPacket(new ExSubjobInfo(activeChar.getPlayer(), false));

        activeChar.sendPacket(new ExVitalityEffectInfo(activeChar));

        activeChar.sendPacket(new ExTutorialList());

        for (Effect effect : activeChar.getEffectList().getAllEffects())
            if (effect.isInUse())
                if (effect.getSkill().getId() == 10022)
                    activeChar.setIsIgnoringDeath(true);

        activeChar.sendPacket(new ExAcquirableSkillListByClass(activeChar));

        if (activeChar.getVar("startMovie") == null) {
            activeChar.setVar("startMovie", "1", -1);
            activeChar.sendPacket(new ExShowUsmVideo(ExShowUsmVideo.GD1_INTRO));
        }
        //activeChar.sendPacket(new ExDynamicQuest(State.CONFIRMED_IN_PROGRESS));
        activeChar.sendPacket(new ExChangeMPCost());
        if (activeChar.getLevel() >= 85) {
            AwakingManager.getInstance().getRaceSkill(activeChar);
        }

        if (first)
            PartySubstituteManager.getInstance().addPlayerToParty(activeChar);
    }

    private static void notifyClanMembers(Player activeChar) {
        Clan clan = activeChar.getClan();
        SubUnit subUnit = activeChar.getSubUnit();
        if (clan == null || subUnit == null)
            return;

        UnitMember member = subUnit.getUnitMember(activeChar.getObjectId());
        if (member == null)
            return;

        member.setPlayerInstance(activeChar, false);

        int sponsor = activeChar.getSponsor();
        int apprentice = activeChar.getApprentice();
        L2GameServerPacket msg = new SystemMessage2(SystemMsg.CLAN_MEMBER_S1_HAS_LOGGED_INTO_GAME).addName(activeChar);
        PledgeShowMemberListUpdate memberUpdate = new PledgeShowMemberListUpdate(activeChar);
        for (Player clanMember : clan.getOnlineMembers(activeChar.getObjectId())) {
            clanMember.sendPacket(memberUpdate);
            if (clanMember.getObjectId() == sponsor)
                clanMember.sendPacket(new SystemMessage2(SystemMsg.YOUR_APPRENTICE_C1_HAS_LOGGED_OUT).addName(activeChar));
            else if (clanMember.getObjectId() == apprentice)
                clanMember.sendPacket(new SystemMessage2(SystemMsg.YOUR_SPONSOR_C1_HAS_LOGGED_IN).addName(activeChar));
            else
                clanMember.sendPacket(msg);
        }

        if (!activeChar.isClanLeader())
            return;

        ClanHall clanHall = clan.getHasHideout() > 0 ? ResidenceHolder.getInstance().getResidence(ClanHall.class, clan.getHasHideout()) : null;
        if (clanHall == null || clanHall.getAuctionLength() != 0)
            return;

        if (clanHall.getSiegeEvent().getClass() != ClanHallAuctionEvent.class)
            return;

        if (clan.getWarehouse().getCountOf(ItemTemplate.ITEM_ID_ADENA) < clanHall.getRentalFee())
            activeChar.sendPacket(new SystemMessage2(SystemMsg.PAYMENT_FOR_YOUR_CLAN_HALL_HAS_NOT_BEEN_MADE_PLEASE_ME_PAYMENT_TO_YOUR_CLAN_WAREHOUSE_BY_S1_TOMORROW).addLong(clanHall.getRentalFee()));
    }

    private void checkNewMail(Player activeChar) {
        for (Mail mail : MailDAO.getInstance().getReceivedMailByOwnerId(activeChar.getObjectId()))
            if (mail.isUnread()) {
                sendPacket(ExNoticePostArrived.STATIC_FALSE);
                sendPacket(new ExUnReadMailCount(mail));
                break;
            }
    }
}
