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

import gnu.trove.iterator.TIntObjectIterator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;

import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.Config;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.ai.CtrlIntention;
import lineage2.gameserver.ai.SummonAI;
import lineage2.gameserver.dao.EffectsDAO;
import lineage2.gameserver.instancemanager.ReflectionManager;
import lineage2.gameserver.model.Zone.ZoneType;
import lineage2.gameserver.model.actor.recorder.SummonStatsChangeRecorder;
import lineage2.gameserver.model.base.TeamType;
import lineage2.gameserver.model.entity.events.GlobalEvent;
import lineage2.gameserver.model.entity.events.impl.DuelEvent;
import lineage2.gameserver.model.instances.PetInstance;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.model.items.PetInventory;
import lineage2.gameserver.network.serverpackets.AutoAttackStart;
import lineage2.gameserver.network.serverpackets.ExPartyPetWindowAdd;
import lineage2.gameserver.network.serverpackets.ExPartyPetWindowDelete;
import lineage2.gameserver.network.serverpackets.ExPartyPetWindowUpdate;
import lineage2.gameserver.network.serverpackets.L2GameServerPacket;
import lineage2.gameserver.network.serverpackets.NpcInfo;
import lineage2.gameserver.network.serverpackets.PartySpelled;
import lineage2.gameserver.network.serverpackets.PetDelete;
import lineage2.gameserver.network.serverpackets.PetInfo;
import lineage2.gameserver.network.serverpackets.PetItemList;
import lineage2.gameserver.network.serverpackets.PetStatusShow;
import lineage2.gameserver.network.serverpackets.PetStatusUpdate;
import lineage2.gameserver.network.serverpackets.RelationChanged;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.skills.EffectType;
import lineage2.gameserver.skills.effects.EffectTemplate;
import lineage2.gameserver.stats.Env;
import lineage2.gameserver.stats.Stats;
import lineage2.gameserver.taskmanager.DecayTaskManager;
import lineage2.gameserver.templates.item.WeaponTemplate;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public abstract class Summon extends Playable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field SUMMON_DISAPPEAR_RANGE. (value is 2500)
	 */
	private static final int SUMMON_DISAPPEAR_RANGE = 2500;
	/**
	 * Field _owner.
	 */
	private final Player _owner;
	/**
	 * Field _spawnAnimation.
	 */
	private int _spawnAnimation = 2;
	/**
	 * Field _exp.
	 */
	protected long _exp = 0;
	/**
	 * Field _sp.
	 */
	protected int _sp = 0;
	/**
	 * Field _spsCharged. Field _maxLoad.
	 */
	private int _maxLoad, _spsCharged;
	/**
	 * Field _ssCharged. Field _depressed. Field _follow. Field _defend.
	 */
	private boolean _follow = true, _depressed = false, _ssCharged = false, _defend = false;
	/**
	 * Field _decayTask.
	 */
	private Future<?> _decayTask;
	
	/**
	 * Constructor for Summon.
	 * @param objectId int
	 * @param template NpcTemplate
	 * @param owner Player
	 */
	public Summon(int objectId, NpcTemplate template, Player owner)
	{
		super(objectId, template);
		_owner = owner;
		if (template.getSkills().size() > 0)
		{
			for (TIntObjectIterator<Skill> iterator = template.getSkills().iterator(); iterator.hasNext();)
			{
				iterator.advance();
				addSkill(iterator.value());
			}
		}
		setXYZ(owner.getX() + Rnd.get(-100, 100), owner.getY() + Rnd.get(-100, 100), owner.getZ());
	}
	
	/**
	 * Method onSpawn.
	 */
	@Override
	protected void onSpawn()
	{
		super.onSpawn();
		_spawnAnimation = 0;
		Player owner = getPlayer();
		Party party = owner.getParty();
		if (party != null)
		{
			party.broadcastToPartyMembers(owner, new ExPartyPetWindowAdd(this));
		}
		if (owner.getEffectList().getEffectByStackType("ServitorShare") != null)
		{
			final Creature SummonEffect = this;
			ThreadPoolManager.getInstance().execute(new RunnableImpl()
			{
				@Override
				public void runImpl()
				{
					final Player owner = getPlayer();
					final Skill skl = owner.getEffectList().getEffectByStackType("ServitorShare").getSkill();
					long currenttime = owner.getEffectList().getEffectByStackType("ServitorShare").getTime();
					long duration = owner.getEffectList().getEffectByStackType("ServitorShare").getDuration();
					for(EffectTemplate et : skl.getEffectTemplates())
					{
						if(et == null || et.getEffectType() != EffectType.ServitorShare)
						{
							continue;
						}
						Env env = new Env(owner,SummonEffect,skl);
						final Effect effect = et.getEffect(env);
						if(effect == null)
						{
							continue;
						}
						effect.setCount(1);
						effect.setPeriod(duration - currenttime);
						effect.schedule();
					}
				}
			});
		}
		getAI().setIntention(CtrlIntention.AI_INTENTION_ACTIVE);
	}
	
	/**
	 * Method getAI.
	 * @return SummonAI
	 */
	@Override
	public SummonAI getAI()
	{
		if (_ai == null)
		{
			synchronized (this)
			{
				if (_ai == null)
				{
					_ai = new SummonAI(this);
				}
			}
		}
		return (SummonAI) _ai;
	}
	
	/**
	 * Method getTemplate.
	 * @return NpcTemplate
	 */
	@Override
	public NpcTemplate getTemplate()
	{
		return (NpcTemplate) _template;
	}
	
	/**
	 * Method isUndead.
	 * @return boolean
	 */
	@Override
	public boolean isUndead()
	{
		return getTemplate().isUndead();
	}
	
	/**
	 * Method getSummonType.
	 * @return int
	 */
	public abstract int getSummonType();
	
	/**
	 * Method getSummonSkillId.
	 * @return int
	 */
	public abstract int getSummonSkillId();
	
	/**
	 * Method getSummonSkillLvl.
	 * @return int
	 */
	public abstract int getSummonSkillLvl();
	
	/**
	 * Method isMountable.
	 * @return boolean
	 */
	public boolean isMountable()
	{
		return false;
	}
	

	/**
	 * Method getExpForThisLevel.
	 * @return long
	 */
	public long getExpForThisLevel()
	{
		return 0;
	}
	
	/**
	 * Method getExpForNextLevel.
	 * @return long
	 */
	public long getExpForNextLevel()
	{
		return 0;
	}
	
	/**
	 * Method getNpcId.
	 * @return int
	 */
	@Override
	public int getNpcId()
	{
		return getTemplate().npcId;
	}
	
	/**
	 * Method getExp.
	 * @return long
	 */
	public final long getExp()
	{
		return _exp;
	}
	
	/**
	 * Method setExp.
	 * @param exp long
	 */
	public final void setExp(final long exp)
	{
		_exp = exp;
	}
	
	/**
	 * Method getSp.
	 * @return int
	 */
	public final int getSp()
	{
		return _sp;
	}
	
	/**
	 * Method setSp.
	 * @param sp int
	 */
	public void setSp(final int sp)
	{
		_sp = sp;
	}
	
	/**
	 * Method getMaxLoad.
	 * @return int
	 */
	@Override
	public int getMaxLoad()
	{
		return _maxLoad;
	}
	
	/**
	 * Method setMaxLoad.
	 * @param maxLoad int
	 */
	public void setMaxLoad(final int maxLoad)
	{
		_maxLoad = maxLoad;
	}
	
	/**
	 * Method getBuffLimit.
	 * @return int
	 */
	@Override
	public int getBuffLimit()
	{
		Player owner = getPlayer();
		return (int) calcStat(Stats.BUFF_LIMIT, owner.getBuffLimit(), null, null);
	}
	
	/**
	 * Method getCurrentFed.
	 * @return int
	 */
	public abstract int getCurrentFed();
	
	/**
	 * Method getMaxFed.
	 * @return int
	 */
	public abstract int getMaxFed();
	
	/**
	 * Method onDeath.
	 * @param killer Creature
	 */
	@Override
	protected void onDeath(Creature killer)
	{
		super.onDeath(killer);
		startDecay(8500L);
		Player owner = getPlayer();
		if ((killer == null) || (killer == owner) || (killer == this) || isInZoneBattle() || killer.isInZoneBattle())
		{
			return;
		}
		if (killer instanceof Summon)
		{
			killer = killer.getPlayer();
		}
		if (killer == null)
		{
			return;
		}
		if (killer.isPlayer())
		{
			Player pk = (Player) killer;
			if (isInZone(ZoneType.SIEGE))
			{
				return;
			}
			DuelEvent duelEvent = getEvent(DuelEvent.class);
			if ((owner.getPvpFlag() > 0) || owner.atMutualWarWith(pk))
			{
				pk.setPvpKills(pk.getPvpKills() + 1);
			}
			else if (((duelEvent == null) || (duelEvent != pk.getEvent(DuelEvent.class))) && (getKarma() <= 0))
			{
				int pkCountMulti = Math.max(pk.getPkKills() / 2, 1);
				pk.increaseKarma(Config.KARMA_MIN_KARMA * pkCountMulti);
			}
			pk.sendChanges();
		}
	}
	
	/**
	 * Method startDecay.
	 * @param delay long
	 */
	protected void startDecay(long delay)
	{
		stopDecay();
		_decayTask = DecayTaskManager.getInstance().addDecayTask(this, delay);
	}
	
	/**
	 * Method stopDecay.
	 */
	protected void stopDecay()
	{
		if (_decayTask != null)
		{
			_decayTask.cancel(false);
			_decayTask = null;
		}
	}
	
	/**
	 * Method onDecay.
	 */
	@Override
	protected void onDecay()
	{
		deleteMe();
	}
	
	/**
	 * Method endDecayTask.
	 */
	public void endDecayTask()
	{
		stopDecay();
		doDecay();
	}
	
	/**
	 * Method broadcastStatusUpdate.
	 */
	@Override
	public void broadcastStatusUpdate()
	{
		super.broadcastStatusUpdate();

		Player owner = getPlayer();

		sendStatusUpdate();

		Party party = owner.getParty();

		if (party != null)
		{
			party.broadcastToPartyMembers(owner, new ExPartyPetWindowUpdate(this));
		}
	}
	
	/**
	 * Method sendStatusUpdate.
	 */
	public void sendStatusUpdate()
	{
		Player owner = getPlayer();
		owner.sendPacket(new PetStatusUpdate(this));
	}
	
	/**
	 * Method onDelete.
	 */
	@Override
	protected void onDelete()
	{
		Player owner = getPlayer();
		Party party = owner.getParty();
		if (party != null)
		{
			party.broadcastToPartyMembers(owner, new ExPartyPetWindowDelete(this));
		}
		owner.sendPacket(new PetDelete(getSummonType(), getObjectId()));
		stopDecay();
		super.onDelete();
	}
	
	/**
	 * Method unSummon.
	 */
	public void unSummon()
	{
		deleteMe();
	}
	
	/**
	 * Method saveEffects.
	 */
	public void saveEffects()
	{
		Player owner = getPlayer();
		if (owner == null)
		{
			return;
		}
		if (owner.isInOlympiadMode())
		{
			getEffectList().stopAllEffects();
		}
		EffectsDAO.getInstance().insert(this);
	}
	
	/**
	 * Method setFollowMode.
	 * @param state boolean
	 */
	public void setFollowMode(boolean state)
	{
		Player owner = getPlayer();
		_follow = state;
		if (_follow)
		{
			if (getAI().getIntention() == CtrlIntention.AI_INTENTION_ACTIVE)
			{
				getAI().setIntention(CtrlIntention.AI_INTENTION_FOLLOW, owner, Config.FOLLOW_RANGE);
			}
		}
		else if (getAI().getIntention() == CtrlIntention.AI_INTENTION_FOLLOW)
		{
			getAI().setIntention(CtrlIntention.AI_INTENTION_ACTIVE);
		}
	}

	/**
	 * Method setDefendMode.
	 * @param state boolean
	 */
	public void setDefendMode(boolean state)
	{
		_defend = state;
	}

	/**
	 * Method isFollowMode.
	 * @return boolean
	 */
	public boolean isFollowMode()
	{
		return _follow;
	}

	/**
	 * Method isFollowMode.
	 * @return boolean
	 */
	public boolean isDefendMode()
	{
		return _defend;
	}

	/**
	 * Field _updateEffectIconsTask.
	 */
	Future<?> _updateEffectIconsTask;
	
	/**
	 * Method getSummonPoint.
	 * @return int
	 */
	public abstract int getSummonPoint();
	
	/**
	 * Method updateEffectIcons.
	 */
	@Override
	public void updateEffectIcons()
	{
		super.updateEffectIcons();
		updateEffectIconsImpl();
		return;
	}
	
	/**
	 * Method updateEffectIconsImpl.
	 */
	public void updateEffectIconsImpl()
	{
		Player owner = getPlayer();
		PartySpelled ps = new PartySpelled(this, true);
		Party party = owner.getParty();
		if (party != null)
		{
			party.broadCast(ps);
		}
		else
		{
			owner.sendPacket(ps);
		}
	}
	
	/**
	 * Method getControlItemObjId.
	 * @return int
	 */
	public int getControlItemObjId()
	{
		return 0;
	}
	
	/**
	 * Method getInventory.
	 * @return PetInventory
	 */
	@Override
	public PetInventory getInventory()
	{
		return null;
	}
	
	/**
	 * Method doPickupItem.
	 * @param object GameObject
	 */
	@Override
	public void doPickupItem(final GameObject object)
	{
	}
	
	/**
	 * Method doRevive.
	 */
	@Override
	public void doRevive()
	{
		super.doRevive();
		setRunning();
		getAI().setIntention(CtrlIntention.AI_INTENTION_ACTIVE);
		setFollowMode(true);
	}
	
	/**
	 * Method getActiveWeaponInstance.
	 * @return ItemInstance
	 */
	@Override
	public ItemInstance getActiveWeaponInstance()
	{
		return null;
	}
	
	/**
	 * Method getActiveWeaponItem.
	 * @return WeaponTemplate
	 */
	@Override
	public WeaponTemplate getActiveWeaponItem()
	{
		return null;
	}
	
	/**
	 * Method getSecondaryWeaponInstance.
	 * @return ItemInstance
	 */
	@Override
	public ItemInstance getSecondaryWeaponInstance()
	{
		return null;
	}
	
	/**
	 * Method getSecondaryWeaponItem.
	 * @return WeaponTemplate
	 */
	@Override
	public WeaponTemplate getSecondaryWeaponItem()
	{
		return null;
	}
	
	/**
	 * Method displayGiveDamageMessage.
	 * @param target Creature
	 * @param damage int
	 * @param crit boolean
	 * @param miss boolean
	 * @param shld boolean
	 * @param magic boolean
	 */
	@Override
	public abstract void displayGiveDamageMessage(Creature target, int damage, boolean crit, boolean miss, boolean shld, boolean magic);
	
	/**
	 * Method displayReceiveDamageMessage.
	 * @param attacker Creature
	 * @param damage int
	 */
	@Override
	public abstract void displayReceiveDamageMessage(Creature attacker, int damage);
	
	/**
	 * Method unChargeShots.
	 * @param spirit boolean
	 * @return boolean
	 */
	@Override
	public boolean unChargeShots(final boolean spirit)
	{
		Player owner = getPlayer();
		if (spirit)
		{
			if (_spsCharged != 0)
			{
				_spsCharged = 0;
				owner.autoShot();
				return true;
			}
		}
		else if (_ssCharged)
		{
			_ssCharged = false;
			owner.autoShot();
			return true;
		}
		return false;
	}
	
	/**
	 * Method getChargedSoulShot.
	 * @return boolean
	 */
	@Override
	public boolean getChargedSoulShot()
	{
		return _ssCharged;
	}
	
	/**
	 * Method getChargedSpiritShot.
	 * @return int
	 */
	@Override
	public int getChargedSpiritShot()
	{
		return _spsCharged;
	}
	
	/**
	 * Method chargeSoulShot.
	 */
	public void chargeSoulShot()
	{
		_ssCharged = true;
	}
	
	/**
	 * Method chargeSpiritShot.
	 * @param state int
	 */
	public void chargeSpiritShot(final int state)
	{
		_spsCharged = state;
	}
	
	/**
	 * Method getSoulshotConsumeCount.
	 * @return int
	 */
	public int getSoulshotConsumeCount()
	{
		return (getLevel() / 27) + 1;
	}
	
	/**
	 * Method getSpiritshotConsumeCount.
	 * @return int
	 */
	public int getSpiritshotConsumeCount()
	{
		return (getLevel() / 58) + 1;
	}
	
	/**
	 * Method isDepressed.
	 * @return boolean
	 */
	public boolean isDepressed()
	{
		return _depressed;
	}
	
	/**
	 * Method setDepressed.
	 * @param depressed boolean
	 */
	public void setDepressed(final boolean depressed)
	{
		_depressed = depressed;
	}
	
	/**
	 * Method isInRange.
	 * @return boolean
	 */
	public boolean isInRange()
	{
		Player owner = getPlayer();
		return getDistance(owner) < SUMMON_DISAPPEAR_RANGE;
	}
	
	/**
	 * Method teleportToOwner.
	 */
	public void teleportToOwner()
	{
		Player owner = getPlayer();
		setNonAggroTime(System.currentTimeMillis() + Config.NONAGGRO_TIME_ONTELEPORT);
		if (owner.isInOlympiadMode())
		{
			teleToLocation(owner.getLoc(), owner.getReflection());
		}
		else
		{
			teleToLocation(Location.findPointToStay(owner, 50, 150), owner.getReflection());
		}
		if (!isDead() && _follow)
		{
			getAI().setIntention(CtrlIntention.AI_INTENTION_FOLLOW, owner, Config.FOLLOW_RANGE);
		}
	}
	
	/**
	 * Method broadcastCharInfo.
	 */
	public void broadcastCharInfo()
	{
		Player owner = getPlayer();
		for (Player player : World.getAroundPlayers(this))
		{
			if (player == owner)
			{
				player.sendPacket(new PetInfo(this).update());
			}
			else
			{
				player.sendPacket(new NpcInfo(this, player).update());
			}
		}
	}
	
	/**
	 * Method sendPetInfo.
	 */
	public void sendPetInfo()
	{
		Player owner = getPlayer();
		owner.sendPacket(new PetInfo(this).update());
		return;
	}
	
	/**
	 * Method getSpawnAnimation.
	 * @return int
	 */
	public int getSpawnAnimation()
	{
		return _spawnAnimation;
	}
	
	/**
	 * Method startPvPFlag.
	 * @param target Creature
	 */
	@Override
	public void startPvPFlag(Creature target)
	{
		Player owner = getPlayer();
		owner.startPvPFlag(target);
	}
	
	/**
	 * Method getPvpFlag.
	 * @return int
	 */
	@Override
	public int getPvpFlag()
	{
		Player owner = getPlayer();
		return owner.getPvpFlag();
	}
	
	/**
	 * Method getKarma.
	 * @return int
	 */
	@Override
	public int getKarma()
	{
		Player owner = getPlayer();
		return owner.getKarma();
	}
	
	/**
	 * Method getTeam.
	 * @return TeamType
	 */
	@Override
	public TeamType getTeam()
	{
		Player owner = getPlayer();
		return owner.getTeam();
	}
	
	/**
	 * Method getPlayer.
	 * @return Player
	 */
	@Override
	public Player getPlayer()
	{
		return _owner;
	}
	
	/**
	 * Method getExpPenalty.
	 * @return double
	 */
	public abstract double getExpPenalty();
	
	/**
	 * Method getStatsRecorder.
	 * @return SummonStatsChangeRecorder
	 */
	@Override
	public SummonStatsChangeRecorder getStatsRecorder()
	{
		if (_statsRecorder == null)
		{
			synchronized (this)
			{
				if (_statsRecorder == null)
				{
					_statsRecorder = new SummonStatsChangeRecorder(this);
				}
			}
		}
		return (SummonStatsChangeRecorder) _statsRecorder;
	}
	
	/**
	 * Method addPacketList.
	 * @param forPlayer Player
	 * @param dropper Creature
	 * @return List<L2GameServerPacket>
	 */
	@Override
	public List<L2GameServerPacket> addPacketList(Player forPlayer, Creature dropper)
	{
		List<L2GameServerPacket> list = new ArrayList<L2GameServerPacket>();
		Player owner = getPlayer();
		if (owner == forPlayer)
		{
			list.add(new PetInfo(this));
			list.add(new PartySpelled(this, true));
			if (isPet())
			{
				list.add(new PetItemList((PetInstance) this));
			}
		}
		else
		{
			Party party = forPlayer.getParty();
			if ((getReflection() == ReflectionManager.GIRAN_HARBOR) && ((owner == null) || (party == null) || (party != owner.getParty())))
			{
				return list;
			}
			list.add(new NpcInfo(this, forPlayer));
			if ((owner != null) && (party != null) && (party == owner.getParty()))
			{
				list.add(new PartySpelled(this, true));
			}
			list.add(RelationChanged.update(forPlayer, this, forPlayer));
		}
		if (isInCombat())
		{
			list.add(new AutoAttackStart(getObjectId()));
		}
		if (isMoving || isFollow)
		{
			list.add(movePacket());
		}
		return list;
	}
	
	/**
	 * Method startAttackStanceTask.
	 */
	@Override
	public void startAttackStanceTask()
	{
		startAttackStanceTask0();
		Player player = getPlayer();
		if (player != null)
		{
			player.startAttackStanceTask0();
		}
	}
	
	/**
	 * Method getEvent.
	 * @param eventClass Class<E>
	 * @return E
	 */
	@Override
	public <E extends GlobalEvent> E getEvent(Class<E> eventClass)
	{
		Player player = getPlayer();
		if (player != null)
		{
			return player.getEvent(eventClass);
		}
		return super.getEvent(eventClass);
	}
	
	/**
	 * Method getEvents.
	 * @return Set<GlobalEvent>
	 */
	@Override
	public Set<GlobalEvent> getEvents()
	{
		Player player = getPlayer();
		if (player != null)
		{
			return player.getEvents();
		}
		return super.getEvents();
	}
	
	/**
	 * Method sendReuseMessage.
	 * @param skill Skill
	 */
	@Override
	public void sendReuseMessage(Skill skill)
	{
		Player player = getPlayer();
		if ((player != null) && isSkillDisabled(skill))
		{
			player.sendPacket(SystemMsg.THAT_PET_SERVITOR_SKILL_CANNOT_BE_USED_BECAUSE_IT_IS_RECHARGING);
		}
	}
	
	@Override
	public void onActionTarget(final Player player, boolean forced)
	{
		super.onActionTarget(player, forced);

		if(getPlayer() == player)
		{
			player.sendPacket(new PetInfo(this).update());

			if(!player.isActionsDisabled())
			{
				player.sendPacket(new PetStatusShow(this));
			}
		}
	}

	@Override
	public void onActionTargeted(final Player player, boolean forced)
	{
		if(getPlayer() == player)
		{
			player.sendPacket(new PetInfo(this).update());

			if(!player.isActionsDisabled())
			{
				player.sendPacket(new PetStatusShow(this));
			}
		}

		super.onActionTargeted(player, forced);
	}

}
