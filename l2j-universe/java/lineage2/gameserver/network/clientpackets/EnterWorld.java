/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package lineage2.gameserver.network.clientpackets;

import java.util.Calendar;

import lineage2.gameserver.Announcements;
import lineage2.gameserver.Config;
import lineage2.gameserver.dao.MailDAO;
import lineage2.gameserver.data.StringHolder;
import lineage2.gameserver.data.xml.holder.ResidenceHolder;
import lineage2.gameserver.instancemanager.AwakingManager;
import lineage2.gameserver.instancemanager.CoupleManager;
import lineage2.gameserver.instancemanager.CursedWeaponsManager;
import lineage2.gameserver.instancemanager.PetitionManager;
import lineage2.gameserver.instancemanager.PlayerMessageStack;
import lineage2.gameserver.instancemanager.QuestManager;
import lineage2.gameserver.listener.actor.player.OnAnswerListener;
import lineage2.gameserver.listener.actor.player.impl.ReviveAnswerListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Effect;
import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.SubClass;
import lineage2.gameserver.model.Summon;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.base.InvisibleType;
import lineage2.gameserver.model.entity.events.impl.ClanHallAuctionEvent;
import lineage2.gameserver.model.entity.residence.Castle;
import lineage2.gameserver.model.entity.residence.ClanHall;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.model.mail.Mail;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.model.pledge.SubUnit;
import lineage2.gameserver.model.pledge.UnitMember;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.network.GameClient;
import lineage2.gameserver.network.serverpackets.ChangeWaitType;
import lineage2.gameserver.network.serverpackets.ClientSetTime;
import lineage2.gameserver.network.serverpackets.ConfirmDlg;
import lineage2.gameserver.network.serverpackets.Die;
import lineage2.gameserver.network.serverpackets.EtcStatusUpdate;
import lineage2.gameserver.network.serverpackets.ExAcquirableSkillListByClass;
import lineage2.gameserver.network.serverpackets.ExAutoSoulShot;
import lineage2.gameserver.network.serverpackets.ExBR_PremiumState;
import lineage2.gameserver.network.serverpackets.ExBasicActionList;
import lineage2.gameserver.network.serverpackets.ExCastleState;
import lineage2.gameserver.network.serverpackets.ExChangeMPCost;
import lineage2.gameserver.network.serverpackets.ExGoodsInventoryChangedNotify;
import lineage2.gameserver.network.serverpackets.ExMPCCOpen;
import lineage2.gameserver.network.serverpackets.ExNoticePostArrived;
import lineage2.gameserver.network.serverpackets.ExNotifyPremiumItem;
import lineage2.gameserver.network.serverpackets.ExPCCafePointInfo;
import lineage2.gameserver.network.serverpackets.ExReceiveShowPostFriend;
import lineage2.gameserver.network.serverpackets.ExSetCompassZoneCode;
import lineage2.gameserver.network.serverpackets.ExShowUsmVideo;
import lineage2.gameserver.network.serverpackets.ExStorageMaxCount;
import lineage2.gameserver.network.serverpackets.ExSubjobInfo;
import lineage2.gameserver.network.serverpackets.ExTutorialList;
import lineage2.gameserver.network.serverpackets.ExVitalityEffectInfo;
import lineage2.gameserver.network.serverpackets.ExWaitWaitingSubStituteInfo;
import lineage2.gameserver.network.serverpackets.HennaInfo;
import lineage2.gameserver.network.serverpackets.L2FriendList;
import lineage2.gameserver.network.serverpackets.L2GameServerPacket;
import lineage2.gameserver.network.serverpackets.MagicSkillLaunched;
import lineage2.gameserver.network.serverpackets.MagicSkillUse;
import lineage2.gameserver.network.serverpackets.PartySmallWindowAll;
import lineage2.gameserver.network.serverpackets.PartySpelled;
import lineage2.gameserver.network.serverpackets.PetInfo;
import lineage2.gameserver.network.serverpackets.PledgeShowInfoUpdate;
import lineage2.gameserver.network.serverpackets.PledgeShowMemberListUpdate;
import lineage2.gameserver.network.serverpackets.PledgeSkillList;
import lineage2.gameserver.network.serverpackets.PrivateStoreMsgBuy;
import lineage2.gameserver.network.serverpackets.PrivateStoreMsgSell;
import lineage2.gameserver.network.serverpackets.QuestList;
import lineage2.gameserver.network.serverpackets.RecipeShopMsg;
import lineage2.gameserver.network.serverpackets.RelationChanged;
import lineage2.gameserver.network.serverpackets.Ride;
import lineage2.gameserver.network.serverpackets.SSQInfo;
import lineage2.gameserver.network.serverpackets.ShortCutInit;
import lineage2.gameserver.network.serverpackets.SkillCoolTime;
import lineage2.gameserver.network.serverpackets.SkillList;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.skills.AbnormalEffect;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.templates.item.ItemTemplate;
import lineage2.gameserver.utils.GameStats;
import lineage2.gameserver.utils.ItemFunctions;
import lineage2.gameserver.utils.TradeHelper;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class EnterWorld extends L2GameClientPacket
{
	/**
	 * Field _lock.
	 */
	private static final Object _lock = new Object();
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(EnterWorld.class);
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		GameClient client = getClient();
		Player activeChar = client.getActiveChar();
		if (activeChar == null)
		{
			client.closeNow(false);
			return;
		}
		int MyObjectId = activeChar.getObjectId();
		Long MyStoreId = activeChar.getStoredId();
		for(Castle castle : ResidenceHolder.getInstance().getResidenceList(Castle.class))
			{
				activeChar.sendPacket(new ExCastleState(castle));
			}
		synchronized (_lock)
		{

			for (Player cha : GameObjectsStorage.getAllPlayersForIterate())
			{
				if (MyStoreId.equals(cha.getStoredId()))
				{
					continue;
				}
				try
				{
					if (cha.getObjectId() == MyObjectId)
					{
						_log.warn("Double EnterWorld for char: " + activeChar.getName());
						cha.kick();
					}
				}
				catch (Exception e)
				{
					_log.error("", e);
				}
			}
		}
		GameStats.incrementPlayerEnterGame();
		boolean first = activeChar.entering;
		if (first)
		{
			activeChar.setOnlineStatus(true);
			if (activeChar.getPlayerAccess().GodMode && !Config.SHOW_GM_LOGIN)
			{
				activeChar.setInvisibleType(InvisibleType.NORMAL);
			}
			activeChar.setNonAggroTime(Long.MAX_VALUE);
			activeChar.spawnMe();
			if (activeChar.isInStoreMode())
			{
				if (!TradeHelper.checksIfCanOpenStore(activeChar, activeChar.getPrivateStoreType()))
				{
					activeChar.setPrivateStoreType(Player.STORE_PRIVATE_NONE);
					activeChar.standUp();
					activeChar.broadcastCharInfo();
				}
			}
			activeChar.setRunning();
			activeChar.standUp();
			activeChar.startTimers();
		}
		activeChar.sendPacket(new ExBR_PremiumState(activeChar, activeChar.hasBonus()));
		
		activeChar.getMacroses().sendUpdate(0x01, 0, true);
        activeChar.sendPacket(new SSQInfo(), new HennaInfo(activeChar));
		activeChar.sendItemList(false);
		activeChar.sendPacket(new ShortCutInit(activeChar));
        activeChar.sendPacket(new ShortCutInit(activeChar), new SkillList(activeChar), new SkillCoolTime(activeChar));
		activeChar.sendPacket(new SkillCoolTime(activeChar));
		//activeChar.sendPacket(new ExCastleState(_castle));
        activeChar.sendPacket(new ExVitalityEffectInfo(activeChar));
		for(Castle castle : ResidenceHolder.getInstance().getResidenceList(Castle.class))
		{
			activeChar.sendPacket(new ExCastleState(castle));
		}
		activeChar.sendPacket(SystemMsg.WELCOME_TO_THE_WORLD_OF_LINEAGE_II);
		Announcements.getInstance().showAnnouncements(activeChar);
		if (first)
		{
			activeChar.getListeners().onEnter();
		}
		if (first && (activeChar.getCreateTime() > 0))
		{
			Calendar create = Calendar.getInstance();
			create.setTimeInMillis(activeChar.getCreateTime());
			Calendar now = Calendar.getInstance();
			int day = create.get(Calendar.DAY_OF_MONTH);
			if ((create.get(Calendar.MONTH) == Calendar.FEBRUARY) && (day == 29))
			{
				day = 28;
			}
			int myBirthdayReceiveYear = activeChar.getVarInt(Player.MY_BIRTHDAY_RECEIVE_YEAR, 0);
			if ((create.get(Calendar.MONTH) == now.get(Calendar.MONTH)) && (create.get(Calendar.DAY_OF_MONTH) == day))
			{
				if (((myBirthdayReceiveYear == 0) && (create.get(Calendar.YEAR) != now.get(Calendar.YEAR))) || ((myBirthdayReceiveYear > 0) && (myBirthdayReceiveYear != now.get(Calendar.YEAR))))
				{
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
					mail.setExpireTime((720 * 3600) + (int) (System.currentTimeMillis() / 1000L));
					mail.save();
					activeChar.setVar(Player.MY_BIRTHDAY_RECEIVE_YEAR, String.valueOf(now.get(Calendar.YEAR)), -1);
				}
			}
		}
		if (activeChar.getClan() != null)
		{
			notifyClanMembers(activeChar);
			activeChar.sendPacket(activeChar.getClan().listAll());
			activeChar.sendPacket(new PledgeShowInfoUpdate(activeChar.getClan()), new PledgeSkillList(activeChar.getClan()));
		}
		if (first && Config.ALLOW_WEDDING)
		{
			CoupleManager.getInstance().engage(activeChar);
			CoupleManager.getInstance().notifyPartner(activeChar);
		}
		if (first)
		{
			activeChar.getFriendList().notifyFriends(true);
			loadTutorial(activeChar);
			activeChar.restoreDisableSkills();
			activeChar.mentoringLoginConditions();
		}
		sendPacket(new L2FriendList(activeChar), new ExStorageMaxCount(activeChar), new QuestList(activeChar), new ExBasicActionList(activeChar), new EtcStatusUpdate(activeChar));
		activeChar.checkHpMessages(activeChar.getMaxHp(), activeChar.getCurrentHp());
		activeChar.checkDayNightMessages();
		if (Config.PETITIONING_ALLOWED)
		{
			PetitionManager.getInstance().checkPetitionMessages(activeChar);
		}
		if (!first)
		{
			if (activeChar.isCastingNow())
			{
				Creature castingTarget = activeChar.getCastingTarget();
				Skill castingSkill = activeChar.getCastingSkill();
				long animationEndTime = activeChar.getAnimationEndTime();
				if ((castingSkill != null) && (castingTarget != null) && castingTarget.isCreature() && (activeChar.getAnimationEndTime() > 0))
				{
					sendPacket(new MagicSkillUse(activeChar, castingTarget, castingSkill.getId(), castingSkill.getLevel(), (int) (animationEndTime - System.currentTimeMillis()), 0));
				}
			}
			if (activeChar.isInBoat())
			{
				activeChar.sendPacket(activeChar.getBoat().getOnPacket(activeChar, activeChar.getInBoatPosition()));
			}
			if (activeChar.isMoving || activeChar.isFollow)
			{
				sendPacket(activeChar.movePacket());
			}
			if (activeChar.getMountNpcId() != 0)
			{
				sendPacket(new Ride(activeChar));
			}
			if (activeChar.isFishing())
			{
				activeChar.stopFishing();
			}
		}
		activeChar.entering = false;
		activeChar.sendUserInfo();
		if (activeChar.isSitting())
		{
			activeChar.sendPacket(new ChangeWaitType(activeChar, ChangeWaitType.WT_SITTING));
		}
		if (activeChar.getPrivateStoreType() != Player.STORE_PRIVATE_NONE)
		{
			if (activeChar.getPrivateStoreType() == Player.STORE_PRIVATE_BUY)
			{
				sendPacket(new PrivateStoreMsgBuy(activeChar));
			}
			else if ((activeChar.getPrivateStoreType() == Player.STORE_PRIVATE_SELL) || (activeChar.getPrivateStoreType() == Player.STORE_PRIVATE_SELL_PACKAGE))
			{
				sendPacket(new PrivateStoreMsgSell(activeChar));
			}
			else if (activeChar.getPrivateStoreType() == Player.STORE_PRIVATE_MANUFACTURE)
			{
				sendPacket(new RecipeShopMsg(activeChar));
			}
		}
		if (activeChar.isDead())
		{
			sendPacket(new Die(activeChar));
		}
		activeChar.unsetVar("offline");
		activeChar.sendActionFailed();
		if (first && activeChar.isGM() && Config.SAVE_GM_EFFECTS && activeChar.getPlayerAccess().CanUseGMCommand)
		{
			if (activeChar.getVarB("gm_silence"))
			{
				activeChar.setMessageRefusal(true);
				activeChar.sendPacket(SystemMsg.MESSAGE_REFUSAL_MODE);
			}
			if (activeChar.getVarB("gm_invul"))
			{
				activeChar.setIsInvul(true);
				activeChar.startAbnormalEffect(AbnormalEffect.S_INVINCIBLE);
				activeChar.sendMessage(activeChar.getName() + " is now immortal.");
			}
			try
			{
				int var_gmspeed = Integer.parseInt(activeChar.getVar("gm_gmspeed"));
				if ((var_gmspeed >= 1) && (var_gmspeed <= 4))
				{
					activeChar.doCast(SkillTable.getInstance().getInfo(7029, var_gmspeed), activeChar, true);
				}
			}
			catch (Exception E)
			{
			}
		}
		PlayerMessageStack.getInstance().CheckMessages(activeChar);
		sendPacket(ClientSetTime.STATIC, new ExSetCompassZoneCode(activeChar));
		Pair<Integer, OnAnswerListener> entry = activeChar.getAskListener(false);
		if ((entry != null) && (entry.getValue() instanceof ReviveAnswerListener))
		{
			sendPacket(new ConfirmDlg(SystemMsg.C1_IS_MAKING_AN_ATTEMPT_TO_RESURRECT_YOU_IF_YOU_CHOOSE_THIS_PATH_S2_EXPERIENCE_WILL_BE_RETURNED_FOR_YOU, 0).addString("Other player").addString("some"));
		}
		if (activeChar.isCursedWeaponEquipped())
		{
			CursedWeaponsManager.getInstance().showUsageTime(activeChar, activeChar.getCursedWeaponEquippedId());
		}
		if (!first)
		{
			if (activeChar.isInObserverMode())
			{
				if (activeChar.getObserverMode() == Player.OBSERVER_LEAVING)
				{
					activeChar.returnFromObserverMode();
				}
				else if (activeChar.getOlympiadObserveGame() != null)
				{
					activeChar.leaveOlympiadObserverMode(true);
				}
				else
				{
					activeChar.leaveObserverMode();
				}
			}
			else if (activeChar.isVisible())
			{
				World.showObjectsToPlayer(activeChar);
			}
			for (Summon summon : activeChar.getSummonList())
			{
				sendPacket(new PetInfo(summon));
			}
			if (activeChar.isInParty())
			{
				sendPacket(new PartySmallWindowAll(activeChar.getParty(), activeChar));
				for (Player member : activeChar.getParty().getPartyMembers())
				{
					if (member != activeChar)
					{
						sendPacket(new PartySpelled(member, true));
						for (Summon memberPet : member.getSummonList())
						{
							sendPacket(new PartySpelled(memberPet, true));
						}
						sendPacket(RelationChanged.update(activeChar, member, activeChar));
					}
				}
				if (activeChar.getParty().isInCommandChannel())
				{
					sendPacket(ExMPCCOpen.STATIC);
				}
			}
			for (int shotId : activeChar.getAutoSoulShot())
			{
				sendPacket(new ExAutoSoulShot(shotId, true));
			}
			for (Effect e : activeChar.getEffectList().getAllFirstEffects())
			{
				if (e.getSkill().isToggle())
				{
					sendPacket(new MagicSkillLaunched(activeChar.getObjectId(), e.getSkill().getId(), e.getSkill().getLevel(), activeChar));
				}
			}
			activeChar.broadcastCharInfo();
		}
		else
		{
			activeChar.sendUserInfo();
		}
		activeChar.updateEffectIcons();
		activeChar.setCurrentHpMp(activeChar.getActiveSubClass().getlogOnHp(),activeChar.getActiveSubClass().getlogOnMp());
		activeChar.setCurrentCp(activeChar.getActiveSubClass().getlogOnCp());
		activeChar.updateStats();		
		if (Config.ALT_PCBANG_POINTS_ENABLED)
		{
			activeChar.sendPacket(new ExPCCafePointInfo(activeChar, 0, 1, 2, 12));
		}
		if (!activeChar.getPremiumItemList().isEmpty())
		{
			activeChar.sendPacket(Config.GOODS_INVENTORY_ENABLED ? ExGoodsInventoryChangedNotify.STATIC : ExNotifyPremiumItem.STATIC);
		}
		if (activeChar.getVarB("HeroPeriod") && Config.SERVICES_HERO_SELL_ENABLED)
		{
			activeChar.setHero(activeChar);
		}
		activeChar.sendVoteSystemInfo();
		activeChar.sendPacket(new ExReceiveShowPostFriend(activeChar));
		activeChar.sendPacket(new ExSubjobInfo(activeChar.getPlayer(), false));
		activeChar.sendPacket(new ExVitalityEffectInfo(activeChar));
		activeChar.sendPacket(new ExTutorialList());
		activeChar.sendPacket(new ExWaitWaitingSubStituteInfo(true));
		for (Effect effect : activeChar.getEffectList().getAllEffects())
		{
			if (effect.isInUse())
			{
				if (effect.getSkill().getId() == 10022)
				{
					activeChar.setIsIgnoringDeath(true);
				}
			}
		}
		if(Config.ALT_GAME_REMOVE_PREVIOUS_CERTIFICATES)
		{
			Skill [] allSkill = activeChar.getAllSkillsArray();
			int totalCertificates =  0;
			for(Skill skl : allSkill)
			{
				if(skl.getId() >= 1573 && skl.getId() <= 1581)
				{
					totalCertificates += skl.getLevel();
					activeChar.removeSkill(skl,true);
				}
			}
			if(totalCertificates > 0)
			{
				activeChar.getInventory().addItem(10280, totalCertificates);
				_log.info("EnterWorld: Player - " + activeChar.getName() + " - Has received " + totalCertificates + " by previous skill certificate deletion." );
				for(SubClass sc : activeChar.getSubClassList().values())
				{
					sc.setCertification(0);
					activeChar.store(true);
				}
			}
		}
		activeChar.sendPacket(new ExAcquirableSkillListByClass(activeChar));
		activeChar.setPartySearchStatus(true);
		activeChar.sendPacket(new ExWaitWaitingSubStituteInfo(true));
		checkNewMail(activeChar);
		activeChar.sendPacket(new ExChangeMPCost(1, -3));
		activeChar.sendPacket(new ExChangeMPCost(1, -5));
		activeChar.sendPacket(new ExChangeMPCost(0, 20));
		activeChar.sendPacket(new ExChangeMPCost(1, -10));
		activeChar.sendPacket(new ExChangeMPCost(3, -20));
		activeChar.sendPacket(new ExChangeMPCost(22, -20));
		if (activeChar.getVar("startMovie") == null)
		{
			activeChar.setVar("startMovie", "1", -1);
			activeChar.sendPacket(new ExShowUsmVideo(ExShowUsmVideo.GD1_INTRO));
		}
		if ((activeChar.getLevel() > 84) && !activeChar.isAwaking())
		{
			AwakingManager.getInstance().SendReqToStartQuest(activeChar);
		}
		if(activeChar.isAwaking()) //If the characters returns to Main, or dual Subclass and Delete Skills prof are active, do check of Correct skills
		{
			if(Config.ALT_CHECK_SKILLS_AWAKENING)
			{
				AwakingManager.getInstance().checkAwakenPlayerSkills(activeChar);
			}
		}
	}
	
	/**
	 * Method notifyClanMembers.
	 * @param activeChar Player
	 */
	private static void notifyClanMembers(Player activeChar)
	{
		Clan clan = activeChar.getClan();
		SubUnit subUnit = activeChar.getSubUnit();
		if ((clan == null) || (subUnit == null))
		{
			return;
		}
		UnitMember member = subUnit.getUnitMember(activeChar.getObjectId());
		if (member == null)
		{
			return;
		}
		member.setPlayerInstance(activeChar, false);
		int sponsor = activeChar.getSponsor();
		int apprentice = activeChar.getApprentice();
		L2GameServerPacket msg = new SystemMessage2(SystemMsg.CLAN_MEMBER_S1_HAS_LOGGED_INTO_GAME).addName(activeChar);
		PledgeShowMemberListUpdate memberUpdate = new PledgeShowMemberListUpdate(activeChar);
		for (Player clanMember : clan.getOnlineMembers(activeChar.getObjectId()))
		{
			clanMember.sendPacket(memberUpdate);
			if (clanMember.getObjectId() == sponsor)
			{
				clanMember.sendPacket(new SystemMessage2(SystemMsg.YOUR_APPRENTICE_C1_HAS_LOGGED_OUT).addName(activeChar));
			}
			else if (clanMember.getObjectId() == apprentice)
			{
				clanMember.sendPacket(new SystemMessage2(SystemMsg.YOUR_SPONSOR_C1_HAS_LOGGED_IN).addName(activeChar));
			}
			else
			{
				clanMember.sendPacket(msg);
			}
		}
		activeChar.getClan().startNotifyClanEnterWorld(activeChar);
		if (!activeChar.isClanLeader())
		{
			return;
		}
		ClanHall clanHall = clan.getHasHideout() > 0 ? ResidenceHolder.getInstance().getResidence(ClanHall.class, clan.getHasHideout()) : null;
		if ((clanHall == null) || (clanHall.getAuctionLength() != 0))
		{
			return;
		}
		if (clanHall.getSiegeEvent().getClass() != ClanHallAuctionEvent.class)
		{
			return;
		}
		if (clan.getWarehouse().getCountOf(ItemTemplate.ITEM_ID_ADENA) < clanHall.getRentalFee())
		{
			activeChar.sendPacket(new SystemMessage2(SystemMsg.PAYMENT_FOR_YOUR_CLAN_HALL_HAS_NOT_BEEN_MADE_PLEASE_ME_PAYMENT_TO_YOUR_CLAN_WAREHOUSE_BY_S1_TOMORROW).addLong(clanHall.getRentalFee()));
		}
	}
	
	/**
	 * Method loadTutorial.
	 * @param player Player
	 */
	private void loadTutorial(Player player)
	{
		Quest q = QuestManager.getQuest(255);
		if (q != null)
		{
			player.processQuestEvent(q.getName(), "UC", null);
		}
	}
	
	/**
	 * Method checkNewMail.
	 * @param activeChar Player
	 */
	private void checkNewMail(Player activeChar)
	{
		for (Mail mail : MailDAO.getInstance().getReceivedMailByOwnerId(activeChar.getObjectId()))
		{
			if (mail.isUnread())
			{
				sendPacket(ExNoticePostArrived.STATIC_FALSE);
				break;
			}
		}
	}
}
