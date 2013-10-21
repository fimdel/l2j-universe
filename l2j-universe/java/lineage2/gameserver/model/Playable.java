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
package lineage2.gameserver.model;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import lineage2.commons.lang.reference.HardReference;
import lineage2.commons.util.Rnd;
import lineage2.commons.util.concurrent.atomic.AtomicState;
import lineage2.gameserver.Config;
import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.ai.CtrlIntention;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.geodata.GeoEngine;
import lineage2.gameserver.model.AggroList.AggroInfo;
import lineage2.gameserver.model.Skill.SkillTargetType;
import lineage2.gameserver.model.Skill.SkillType;
import lineage2.gameserver.model.Zone.ZoneType;
import lineage2.gameserver.model.entity.boat.Boat;
import lineage2.gameserver.model.entity.events.GlobalEvent;
import lineage2.gameserver.model.entity.events.impl.DuelEvent;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.instances.StaticObjectInstance;
import lineage2.gameserver.model.items.Inventory;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.network.serverpackets.Revive;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.CustomMessage;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.skills.EffectType;
import lineage2.gameserver.stats.Stats;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.templates.CharTemplate;
import lineage2.gameserver.templates.item.EtcItemTemplate;
import lineage2.gameserver.templates.item.WeaponTemplate;
import lineage2.gameserver.templates.item.WeaponTemplate.WeaponType;
import lineage2.gameserver.utils.Location;

public abstract class Playable extends Creature
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field _isSilentMoving.
	 */
	private final AtomicState _isSilentMoving = new AtomicState();
	/**
	 * Field _isPendingRevive.
	 */
	private boolean _isPendingRevive;
	/**
	 * Field questLock.
	 */
	protected final ReadWriteLock questLock = new ReentrantReadWriteLock();
	/**
	 * Field questRead.
	 */
	protected final Lock questRead = questLock.readLock();
	/**
	 * Field questWrite.
	 */
	protected final Lock questWrite = questLock.writeLock();
	
	/**
	 * Constructor for Playable.
	 * @param objectId int
	 * @param template CharTemplate
	 */
	public Playable(int objectId, CharTemplate template)
	{
		super(objectId, template);
	}
	
	/**
	 * Method getRef.
	 * @return HardReference<? extends Playable>
	 */
	@SuppressWarnings("unchecked")
	@Override
	public HardReference<? extends Playable> getRef()
	{
		return (HardReference<? extends Playable>) super.getRef();
	}
	
	/**
	 * Method getInventory.
	 * @return Inventory
	 */
	public abstract Inventory getInventory();
	
	/**
	 * Method getWearedMask.
	 * @return long
	 */
	public abstract long getWearedMask();
	
	/**
	 * Field _boat.
	 */
	private Boat _boat;
	/**
	 * Field _inBoatPosition.
	 */
	private Location _inBoatPosition;
	
	/**
	 * Method checkPvP.
	 * @param target Creature
	 * @param skill Skill
	 * @return boolean
	 */
	@Override
	public boolean checkPvP(final Creature target, Skill skill)
	{
		Player player = getPlayer();
		if (isDead() || (target == null) || (player == null) || (target == this) || (target == player) || player.getSummonList().contains(target) || (player.getKarma() < 0))
		{
			return false;
		}
		if (skill != null)
		{
			if (skill.altUse())
			{
				return false;
			}
			if (skill.getTargetType() == SkillTargetType.TARGET_FEEDABLE_BEAST)
			{
				return false;
			}
			if (skill.getTargetType() == SkillTargetType.TARGET_UNLOCKABLE)
			{
				return false;
			}
			if (skill.getTargetType() == SkillTargetType.TARGET_CHEST)
			{
				return false;
			}
		}
		DuelEvent duelEvent = getEvent(DuelEvent.class);
		if ((duelEvent != null) && (duelEvent == target.getEvent(DuelEvent.class)))
		{
			return false;
		}
		if (isInZonePeace() && target.isInZonePeace())
		{
			return false;
		}
		if (isInZoneBattle() && target.isInZoneBattle())
		{
			return false;
		}
		if (isInZone(ZoneType.SIEGE) && target.isInZone(ZoneType.SIEGE))
		{
			return false;
		}
		if ((skill == null) || skill.isOffensive())
		{
			if (target.getKarma() < 0)
			{
				return false;
			}
			else if (target.isPlayable())
			{
				return true;
			}
		}
		else if ((target.getPvpFlag() > 0) || (target.getKarma() < 0) || target.isMonster())
		{
			return true;
		}
		return false;
	}
	
	/**
	 * Method checkTarget.
	 * @param target Creature
	 * @return boolean
	 */
	public boolean checkTarget(Creature target)
	{
		Player player = getPlayer();
		if (player == null)
		{
			return false;
		}
		if ((target == null) || target.isDead())
		{
			player.sendPacket(Msg.INVALID_TARGET);
			return false;
		}
		if (!isInRange(target, 2000))
		{
			player.sendPacket(Msg.YOUR_TARGET_IS_OUT_OF_RANGE);
			return false;
		}
		if (target.isDoor() && !target.isAttackable(this))
		{
			player.sendPacket(Msg.INVALID_TARGET);
			return false;
		}
		if (target.paralizeOnAttack(this))
		{
			if (Config.PARALIZE_ON_RAID_DIFF)
			{
				paralizeMe(target);
			}
			return false;
		}
		if (target.isInvisible() || (getReflection() != target.getReflection()) || !GeoEngine.canSeeTarget(this, target, false))
		{
			player.sendPacket(SystemMsg.CANNOT_SEE_TARGET);
			return false;
		}
		if (player.isInZone(ZoneType.epic) != target.isInZone(ZoneType.epic))
		{
			player.sendPacket(Msg.INVALID_TARGET);
			return false;
		}
		if (target.isPlayable())
		{
			if (isInZoneBattle() != target.isInZoneBattle())
			{
				player.sendPacket(Msg.INVALID_TARGET);
				return false;
			}
			if (isInZonePeace() || target.isInZonePeace())
			{
				player.sendPacket(Msg.YOU_MAY_NOT_ATTACK_THIS_TARGET_IN_A_PEACEFUL_ZONE);
				return false;
			}
			if (player.isInOlympiadMode() && !player.isOlympiadCompStart())
			{
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Method doAttack.
	 * @param target Creature
	 */
	@Override
	public void doAttack(Creature target)
	{
		Player player = getPlayer();
		if (player == null)
		{
			return;
		}
		if (isAMuted() || isAttackingNow())
		{
			player.sendActionFailed();
			return;
		}
		if (player.isInObserverMode())
		{
			player.sendMessage(new CustomMessage("lineage2.gameserver.model.L2Playable.OutOfControl.ObserverNoAttack", player));
			return;
		}
		if (!checkTarget(target))
		{
			getAI().setIntention(CtrlIntention.AI_INTENTION_ACTIVE, null, null);
			player.sendActionFailed();
			return;
		}
		DuelEvent duelEvent = getEvent(DuelEvent.class);
		if ((duelEvent != null) && (target.getEvent(DuelEvent.class) != duelEvent))
		{
			duelEvent.abortDuel(getPlayer());
		}
		WeaponTemplate weaponItem = getActiveWeaponItem();
		if ((weaponItem != null) && ((weaponItem.getItemType() == WeaponType.BOW) || (weaponItem.getItemType() == WeaponType.CROSSBOW)))
		{
			double bowMpConsume = weaponItem.getMpConsume();
			if (bowMpConsume > 0)
			{
				double chance = calcStat(Stats.MP_USE_BOW_CHANCE, 0., target, null);
				if ((chance > 0) && Rnd.chance(chance))
				{
					bowMpConsume = calcStat(Stats.MP_USE_BOW, bowMpConsume, target, null);
				}
				if (_currentMp < bowMpConsume)
				{
					getAI().setIntention(CtrlIntention.AI_INTENTION_ACTIVE, null, null);
					player.sendPacket(Msg.NOT_ENOUGH_MP);
					player.sendActionFailed();
					return;
				}
				reduceCurrentMp(bowMpConsume, null);
			}
			if (!player.checkAndEquipArrows())
			{
				getAI().setIntention(CtrlIntention.AI_INTENTION_ACTIVE, null, null);
				player.sendPacket(player.getActiveWeaponInstance().getItemType() == WeaponType.BOW ? Msg.YOU_HAVE_RUN_OUT_OF_ARROWS : Msg.NOT_ENOUGH_BOLTS);
				player.sendActionFailed();
				return;
			}
		}
		super.doAttack(target);
	}
	
	/**
	 * Method doCast.
	 * @param skill Skill
	 * @param target Creature
	 * @param forceUse boolean
	 */
	@Override
	public void doCast(final Skill skill, final Creature target, boolean forceUse)
	{
		if (skill == null)
		{
			return;
		}
		DuelEvent duelEvent = getEvent(DuelEvent.class);
		if ((duelEvent != null) && (target.getEvent(DuelEvent.class) != duelEvent))
		{
			duelEvent.abortDuel(getPlayer());
		}
		if (skill.isAoE() && isInPeaceZone())
		{
			getPlayer().sendPacket(Msg.A_MALICIOUS_SKILL_CANNOT_BE_USED_IN_A_PEACE_ZONE);
			return;
		}
		if ((skill.getSkillType() == SkillType.DEBUFF) && target.isNpc() && target.isInvul() && !target.isMonster())
		{
			getPlayer().sendPacket(Msg.INVALID_TARGET);
			return;
		}
		super.doCast(skill, target, forceUse);
	}
	
	/**
	 * Method reduceCurrentHp.
	 * @param damage double
	 * @param reflectableDamage double
	 * @param attacker Creature
	 * @param skill Skill
	 * @param awake boolean
	 * @param standUp boolean
	 * @param directHp boolean
	 * @param canReflect boolean
	 * @param transferDamage boolean
	 * @param isDot boolean
	 * @param sendMessage boolean
	 */
	@Override
	public void reduceCurrentHp(double damage, double reflectableDamage, Creature attacker, Skill skill, boolean awake, boolean standUp, boolean directHp, boolean canReflect, boolean transferDamage, boolean isDot, boolean sendMessage)
	{
		if ((attacker == null) || isDead() || (attacker.isDead() && !isDot))
		{
			return;
		}
		if (isDamageBlocked() && transferDamage)
		{
			return;
		}
		if (isDamageBlocked() && (attacker != this))
		{
			if (sendMessage)
			{
				attacker.sendPacket(Msg.THE_ATTACK_HAS_BEEN_BLOCKED);
			}
			return;
		}
		if ((attacker != this) && attacker.isPlayable())
		{
			Player player = getPlayer();
			Player pcAttacker = attacker.getPlayer();
			if (pcAttacker != player)
			{
				if (player.isInOlympiadMode() && !player.isOlympiadCompStart())
				{
					if (sendMessage)
					{
						pcAttacker.sendPacket(Msg.INVALID_TARGET);
					}
					return;
				}
			}
			if (isInZoneBattle() != attacker.isInZoneBattle())
			{
				if (sendMessage)
				{
					attacker.getPlayer().sendPacket(Msg.INVALID_TARGET);
				}
				return;
			}
			DuelEvent duelEvent = getEvent(DuelEvent.class);
			if ((duelEvent != null) && (attacker.getEvent(DuelEvent.class) != duelEvent))
			{
				duelEvent.abortDuel(player);
			}
		}
		super.reduceCurrentHp(damage, reflectableDamage, attacker, skill, awake, standUp, directHp, canReflect, transferDamage, isDot, sendMessage);
	}
	
	/**
	 * Method getPAtkSpd.
	 * @return int
	 */
	@Override
	public int getPAtkSpd()
	{
		return Math.max((int) calcStat(Stats.POWER_ATTACK_SPEED, calcStat(Stats.ATK_BASE, _template.getBasePAtkSpd(), null, null), null, null), 1);
	}
	
	/**
	 * Method getPAtk.
	 * @param target Creature
	 * @return int
	 */
	@Override
	public int getPAtk(final Creature target)
	{
		double init = getActiveWeaponInstance() == null ? _template.getBasePAtk() : 0;
		return (int) calcStat(Stats.POWER_ATTACK, init, target, null);
	}
	
	/**
	 * Method getMAtk.
	 * @param target Creature
	 * @param skill Skill
	 * @return int
	 */
	@Override
	public int getMAtk(final Creature target, final Skill skill)
	{
		if ((skill != null) && (skill.getMatak() > 0))
		{
			return skill.getMatak();
		}
		final double init = getActiveWeaponInstance() == null ? _template.getBaseMAtk() : 0;
		return (int) calcStat(Stats.MAGIC_ATTACK, init, target, skill);
	}
	
	/**
	 * Method isAttackable.
	 * @param attacker Creature
	 * @return boolean
	 */
	@Override
	public boolean isAttackable(Creature attacker)
	{
		return isCtrlAttackable(attacker, true, false);
	}
	
	/**
	 * Method isAutoAttackable.
	 * @param attacker Creature
	 * @return boolean
	 */
	@Override
	public boolean isAutoAttackable(Creature attacker)
	{
		return isCtrlAttackable(attacker, false, false);
	}
	
	/**
	 * Method isCtrlAttackable.
	 * @param attacker Creature
	 * @param force boolean
	 * @param witchCtrl boolean
	 * @return boolean
	 */
	public boolean isCtrlAttackable(Creature attacker, boolean force, boolean witchCtrl)
	{
		Player player = getPlayer();
		if ((attacker == null) || (player == null) || (attacker == this) || ((attacker == player) && !force) || isAlikeDead() || attacker.isAlikeDead())
		{
			return false;
		}
		if (isInvisible() || (getReflection() != attacker.getReflection()))
		{
			return false;
		}
		if (isInBoat())
		{
			return false;
		}
		for (GlobalEvent e : getEvents())
		{
			if (e.checkForAttack(this, attacker, null, force) != null)
			{
				return false;
			}
		}
		for (GlobalEvent e : player.getEvents())
		{
			if (e.canAttack(this, attacker, null, force))
			{
				return true;
			}
		}
		Player pcAttacker = attacker.getPlayer();
		if ((pcAttacker != null) && (pcAttacker != player))
		{
			if (pcAttacker.isInBoat())
			{
				return false;
			}
			if ((pcAttacker.getBlockCheckerArena() > -1) || (player.getBlockCheckerArena() > -1))
			{
				return false;
			}
			if ((pcAttacker.isCursedWeaponEquipped() && (player.getLevel() < 21)) || (player.isCursedWeaponEquipped() && (pcAttacker.getLevel() < 21)))
			{
				return false;
			}
			if (player.isInZone(ZoneType.epic) != pcAttacker.isInZone(ZoneType.epic))
			{
				return false;
			}
			if ((player.isInOlympiadMode() || pcAttacker.isInOlympiadMode()) && (player.getOlympiadGame() != pcAttacker.getOlympiadGame()))
			{
				return false;
			}
			if (player.isInOlympiadMode() && !player.isOlympiadCompStart())
			{
				return false;
			}
			if (player.isInOlympiadMode() && player.isOlympiadCompStart() && (player.getOlympiadSide() == pcAttacker.getOlympiadSide()) && !force)
			{
				return false;
			}
			if (isInZonePeace())
			{
				return false;
			}
			if (!force && (player.getParty() != null) && (player.getParty() == pcAttacker.getParty()))
			{
				return false;
			}
			if (isInZoneBattle())
			{
				return true;
			}
			if (!force && (player.getClan() != null) && (player.getClan() == pcAttacker.getClan()))
			{
				return false;
			}
			if (isInZone(ZoneType.SIEGE))
			{
				return true;
			}
			if (pcAttacker.atMutualWarWith(player))
			{
				return true;
			}
			if ((player.getKarma() < 0) || (player.getPvpFlag() != 0))
			{
				return true;
			}
			if (witchCtrl && (player.getPvpFlag() > 0))
			{
				return true;
			}
			return force;
		}
		if ((pcAttacker != null) && (pcAttacker == player))
		{
			return force;
		}
		return true;
	}
	
	/**
	 * Method getKarma.
	 * @return int
	 */
	@Override
	public int getKarma()
	{
		Player player = getPlayer();
		return player == null ? 0 : player.getKarma();
	}
	
	/**
	 * Method callSkill.
	 * @param skill Skill
	 * @param targets List<Creature>
	 * @param useActionSkills boolean
	 */
	@Override
	public void callSkill(Skill skill, List<Creature> targets, boolean useActionSkills)
	{
		Player player = getPlayer();
		if (player == null)
		{
			return;
		}
		if (useActionSkills && !skill.altUse() && !skill.getSkillType().equals(SkillType.BEAST_FEED))
		{
			for (Creature target : targets)
			{
				if (target.isNpc())
				{
					if (skill.isOffensive())
					{
						if (target.paralizeOnAttack(player))
						{
							if (Config.PARALIZE_ON_RAID_DIFF)
							{
								paralizeMe(target);
							}
							return;
						}
						if (!skill.isAI())
						{
							int damage = skill.getEffectPoint() != 0 ? skill.getEffectPoint() : 1;
							target.getAI().notifyEvent(CtrlEvent.EVT_ATTACKED, this, damage);
						}
					}
					target.getAI().notifyEvent(CtrlEvent.EVT_SEE_SPELL, skill, this);
				}
				else
				{
					if (target.isPlayable() && !(isPlayer() && player.getSummonList().contains(target)) && !((isServitor() || isPet()) && (target == player)))
					{
						int aggro = skill.getEffectPoint() != 0 ? skill.getEffectPoint() : Math.max(1, (int) skill.getPower());
						List<NpcInstance> npcs = World.getAroundNpc(target);
						for (NpcInstance npc : npcs)
						{
							if (npc.isDead() || !npc.isInRangeZ(this, 2000))
							{
								continue;
							}
							npc.getAI().notifyEvent(CtrlEvent.EVT_SEE_SPELL, skill, this);
							AggroInfo ai = npc.getAggroList().get(target);
							if (ai == null)
							{
								continue;
							}
							if (!skill.isHandler() && npc.paralizeOnAttack(player))
							{
								if (Config.PARALIZE_ON_RAID_DIFF)
								{
									paralizeMe(npc);
								}
								return;
							}
							if (ai.hate < 100)
							{
								continue;
							}
							if (GeoEngine.canSeeTarget(npc, target, false))
							{
								npc.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, this, ai.damage == 0 ? aggro / 2 : aggro);
							}
						}
						//Notify Attack also on skill
						if ((target.isServitor() || target.isPlayer()) && skill.isOffensive())
						{
							if (!skill.isAI())
							{
								int damage = skill.getEffectPoint() != 0 ? skill.getEffectPoint() : 1;
								target.getAI().notifyEvent(CtrlEvent.EVT_ATTACKED, this, damage);
							}
						}
						//Notify Attack on Skill End
					}
				}
				if (checkPvP(target, skill))
				{
					startPvPFlag(target);
				}
			}
		}
		super.callSkill(skill, targets, useActionSkills);
	}
	
	/**
	 * Method broadcastPickUpMsg.
	 * @param item ItemInstance
	 */
	public void broadcastPickUpMsg(ItemInstance item)
	{
		Player player = getPlayer();
		if ((item == null) || (player == null) || player.isInvisible())
		{
			return;
		}
		if (item.isEquipable() && !(item.getTemplate() instanceof EtcItemTemplate))
		{
			SystemMessage msg = null;
			String player_name = player.getName();
			if (item.getEnchantLevel() > 0)
			{
				int msg_id = isPlayer() ? SystemMessage.ATTENTION_S1_PICKED_UP__S2_S3 : SystemMessage.ATTENTION_S1_PET_PICKED_UP__S2_S3;
				msg = new SystemMessage(msg_id).addString(player_name).addNumber(item.getEnchantLevel()).addItemName(item.getItemId());
			}
			else
			{
				int msg_id = isPlayer() ? SystemMessage.ATTENTION_S1_PICKED_UP_S2 : SystemMessage.ATTENTION_S1_PET_PICKED_UP__S2_S3;
				msg = new SystemMessage(msg_id).addString(player_name).addItemName(item.getItemId());
			}
			player.broadcastPacket(msg);
		}
	}
	
	/**
	 * Method paralizeMe.
	 * @param effector Creature
	 */
	public void paralizeMe(Creature effector)
	{
		Skill revengeSkill = SkillTable.getInstance().getInfo(Skill.SKILL_RAID_CURSE, 1);
		revengeSkill.getEffects(effector, this, false, false);
	}
	
	/**
	 * Method setPendingRevive.
	 * @param value boolean
	 */
	public final void setPendingRevive(boolean value)
	{
		_isPendingRevive = value;
	}
	
	/**
	 * Method isPendingRevive.
	 * @return boolean
	 */
	public boolean isPendingRevive()
	{
		return _isPendingRevive;
	}
	
	/**
	 * Method doRevive.
	 */
	public void doRevive()
	{
		if (!isTeleporting())
		{
			setPendingRevive(false);
			setNonAggroTime(System.currentTimeMillis() + Config.NONAGGRO_TIME_ONTELEPORT);
			if (isSalvation())
			{
				for (Effect e : getEffectList().getAllEffects())
				{
					if (e.getEffectType() == EffectType.Salvation)
					{
						e.exit();
						break;
					}
				}
				setCurrentHp(getMaxHp(), true);
				setCurrentMp(getMaxMp());
				setCurrentCp(getMaxCp());
			}
			else
			{
				if (isPlayer() && (Config.RESPAWN_RESTORE_CP >= 0))
				{
					setCurrentCp(getMaxCp() * Config.RESPAWN_RESTORE_CP);
				}
				setCurrentHp(Math.max(1, getMaxHp() * Config.RESPAWN_RESTORE_HP), true);
				if (Config.RESPAWN_RESTORE_MP >= 0)
				{
					setCurrentMp(getMaxMp() * Config.RESPAWN_RESTORE_MP);
				}
			}
			broadcastPacket(new Revive(this));
		}
		else
		{
			setPendingRevive(true);
		}
	}
	
	/**
	 * Method doPickupItem.
	 * @param object GameObject
	 */
	public abstract void doPickupItem(GameObject object);
	
	/**
	 * Method sitDown.
	 * @param throne StaticObjectInstance
	 */
	public void sitDown(StaticObjectInstance throne)
	{
	}
	
	/**
	 * Method standUp.
	 */
	public void standUp()
	{
	}
	
	/**
	 * Field _nonAggroTime.
	 */
	private long _nonAggroTime;
	
	/**
	 * Method getNonAggroTime.
	 * @return long
	 */
	public long getNonAggroTime()
	{
		return _nonAggroTime;
	}
	
	/**
	 * Method setNonAggroTime.
	 * @param time long
	 */
	public void setNonAggroTime(long time)
	{
		_nonAggroTime = time;
	}
	
	/**
	 * Method startSilentMoving.
	 * @return boolean
	 */
	public boolean startSilentMoving()
	{
		return _isSilentMoving.getAndSet(true);
	}
	
	/**
	 * Method stopSilentMoving.
	 * @return boolean
	 */
	public boolean stopSilentMoving()
	{
		return _isSilentMoving.setAndGet(false);
	}
	
	/**
	 * Method isSilentMoving.
	 * @return boolean
	 */
	public boolean isSilentMoving()
	{
		return _isSilentMoving.get();
	}
	
	/**
	 * Method isInCombatZone.
	 * @return boolean
	 */
	public boolean isInCombatZone()
	{
		return isInZoneBattle();
	}
	
	/**
	 * Method isInPeaceZone.
	 * @return boolean
	 */
	public boolean isInPeaceZone()
	{
		return isInZonePeace();
	}
	
	/**
	 * Method isInZoneBattle.
	 * @return boolean
	 */
	@Override
	public boolean isInZoneBattle()
	{
		return super.isInZoneBattle();
	}
	
	/**
	 * Method isOnSiegeField.
	 * @return boolean
	 */
	public boolean isOnSiegeField()
	{
		return isInZone(ZoneType.SIEGE);
	}
	
	/**
	 * Method isInSSQZone.
	 * @return boolean
	 */
	public boolean isInSSQZone()
	{
		return isInZone(ZoneType.ssq_zone);
	}
	
	/**
	 * Method isInDangerArea.
	 * @return boolean
	 */
	public boolean isInDangerArea()
	{
		return isInZone(ZoneType.damage) || isInZone(ZoneType.swamp) || isInZone(ZoneType.poison) || isInZone(ZoneType.instant_skill);
	}
	
	/**
	 * Method getMaxLoad.
	 * @return int
	 */
	public int getMaxLoad()
	{
		return 0;
	}
	
	/**
	 * Method getInventoryLimit.
	 * @return int
	 */
	public int getInventoryLimit()
	{
		return 0;
	}
	
	/**
	 * Method isPlayable.
	 * @return boolean
	 */
	@Override
	public boolean isPlayable()
	{
		return true;
	}
	
	/**
	 * Method isInBoat.
	 * @return boolean
	 */
	@Override
	public boolean isInBoat()
	{
		return _boat != null;
	}
	
	/**
	 * Method isInShuttle.
	 * @return boolean
	 */
	@Override
	public boolean isInShuttle()
	{
		return (_boat != null) && _boat.isShuttle();
	}
	
	/**
	 * Method getBoat.
	 * @return Boat
	 */
	public Boat getBoat()
	{
		return _boat;
	}
	
	/**
	 * Method setBoat.
	 * @param boat Boat
	 */
	public void setBoat(Boat boat)
	{
		_boat = boat;
	}
	
	/**
	 * Method getInBoatPosition.
	 * @return Location
	 */
	public Location getInBoatPosition()
	{
		return _inBoatPosition;
	}
	
	/**
	 * Method setInBoatPosition.
	 * @param loc Location
	 */
	public void setInBoatPosition(Location loc)
	{
		_inBoatPosition = loc;
	}

	@Override
	public void onInteract(final Player player)
	{
		player.getAI().setIntention(CtrlIntention.AI_INTENTION_FOLLOW, this, Config.FOLLOW_RANGE);
	}

	@Override
	public boolean displayHpBar()
	{
		return true;
	}
}
