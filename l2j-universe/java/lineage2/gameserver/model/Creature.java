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

import static lineage2.gameserver.ai.CtrlIntention.AI_INTENTION_ACTIVE;
import gnu.trove.set.hash.TIntHashSet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javolution.util.FastList;
import lineage2.commons.collections.LazyArrayList;
import lineage2.commons.lang.reference.HardReference;
import lineage2.commons.lang.reference.HardReferences;
import lineage2.commons.listener.Listener;
import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.commons.util.concurrent.atomic.AtomicState;
import lineage2.gameserver.Config;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.ai.CharacterAI;
import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.ai.CtrlIntention;
import lineage2.gameserver.ai.PlayableAI.nextAction;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.geodata.GeoEngine;
import lineage2.gameserver.geodata.GeoMove;
import lineage2.gameserver.instancemanager.ReflectionManager;
import lineage2.gameserver.instancemanager.WorldStatisticsManager;
import lineage2.gameserver.model.GameObjectTasks.AltMagicUseTask;
import lineage2.gameserver.model.GameObjectTasks.CastEndTimeTask;
import lineage2.gameserver.model.GameObjectTasks.HitTask;
import lineage2.gameserver.model.GameObjectTasks.MagicLaunchedTask;
import lineage2.gameserver.model.GameObjectTasks.MagicUseTask;
import lineage2.gameserver.model.GameObjectTasks.NotifyAITask;
import lineage2.gameserver.model.Skill.SkillTargetType;
import lineage2.gameserver.model.Skill.SkillType;
import lineage2.gameserver.model.Zone.ZoneType;
import lineage2.gameserver.model.actor.listener.CharListenerList;
import lineage2.gameserver.model.actor.recorder.CharStatsChangeRecorder;
import lineage2.gameserver.model.base.InvisibleType;
import lineage2.gameserver.model.base.TeamType;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.MinionInstance;
import lineage2.gameserver.model.instances.MonsterInstance;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.model.quest.QuestEventType;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.model.reference.L2Reference;
import lineage2.gameserver.model.worldstatistics.CategoryType;
import lineage2.gameserver.network.serverpackets.ActionFail;
import lineage2.gameserver.network.serverpackets.Attack;
import lineage2.gameserver.network.serverpackets.AutoAttackStart;
import lineage2.gameserver.network.serverpackets.AutoAttackStop;
import lineage2.gameserver.network.serverpackets.ChangeMoveType;
import lineage2.gameserver.network.serverpackets.CharMoveToLocation;
import lineage2.gameserver.network.serverpackets.ExAbnormalStatusUpdateFromTarget;
import lineage2.gameserver.network.serverpackets.ExTeleportToLocationActivate;
import lineage2.gameserver.network.serverpackets.FlyToLocation;
import lineage2.gameserver.network.serverpackets.FlyToLocation.FlyType;
import lineage2.gameserver.network.serverpackets.L2GameServerPacket;
import lineage2.gameserver.network.serverpackets.MagicSkillCanceled;
import lineage2.gameserver.network.serverpackets.MagicSkillLaunched;
import lineage2.gameserver.network.serverpackets.MagicSkillUse;
import lineage2.gameserver.network.serverpackets.SetupGauge;
import lineage2.gameserver.network.serverpackets.StatusUpdate;
import lineage2.gameserver.network.serverpackets.StatusUpdate.StatusUpdateField;
import lineage2.gameserver.network.serverpackets.StopMove;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.TeleportToLocation;
import lineage2.gameserver.network.serverpackets.ValidateLocation;
import lineage2.gameserver.network.serverpackets.components.CustomMessage;
import lineage2.gameserver.network.serverpackets.components.IStaticPacket;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.skills.AbnormalEffect;
import lineage2.gameserver.skills.EffectType;
import lineage2.gameserver.skills.TimeStamp;
import lineage2.gameserver.skills.effects.EffectTemplate;
import lineage2.gameserver.stats.Calculator;
import lineage2.gameserver.stats.Env;
import lineage2.gameserver.stats.Formulas;
import lineage2.gameserver.stats.Formulas.AttackInfo;
import lineage2.gameserver.stats.StatFunctions;
import lineage2.gameserver.stats.StatTemplate;
import lineage2.gameserver.stats.Stats;
import lineage2.gameserver.stats.funcs.Func;
import lineage2.gameserver.stats.triggers.TriggerInfo;
import lineage2.gameserver.stats.triggers.TriggerType;
import lineage2.gameserver.taskmanager.LazyPrecisionTaskManager;
import lineage2.gameserver.taskmanager.RegenTaskManager;
import lineage2.gameserver.templates.CharTemplate;
import lineage2.gameserver.templates.item.WeaponTemplate;
import lineage2.gameserver.templates.item.WeaponTemplate.WeaponType;
import lineage2.gameserver.templates.spawn.WalkerRouteTemplate;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.Log;
import lineage2.gameserver.utils.PositionUtils;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.napile.primitive.maps.IntObjectMap;
import org.napile.primitive.maps.impl.CHashIntObjectMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public abstract class Creature extends GameObject
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * @author Mobius
	 */
	public class MoveNextTask extends RunnableImpl
	{
		/**
		 * Field donedist.
		 */
		/**
		 * Field alldist.
		 */
		private double alldist, donedist;
		
		/**
		 * Method setDist.
		 * @param dist double
		 * @return MoveNextTask
		 */
		public MoveNextTask setDist(double dist)
		{
			alldist = dist;
			donedist = 0.;
			return this;
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			if (!isMoving)
			{
				return;
			}
			moveLock.lock();
			try
			{
				if (!isMoving)
				{
					return;
				}
				if (isMovementDisabled())
				{
					stopMove();
					return;
				}
				Creature follow = null;
				int speed = getMoveSpeed();
				if (speed <= 0)
				{
					stopMove();
					return;
				}
				long now = System.currentTimeMillis();
				if (isFollow)
				{
					follow = getFollowTarget();
					if (follow == null)
					{
						stopMove();
						return;
					}
					if (isInRangeZ(follow, _offset) && GeoEngine.canSeeTarget(Creature.this, follow, false))
					{
						stopMove();
						ThreadPoolManager.getInstance().execute(new NotifyAITask(Creature.this, CtrlEvent.EVT_ARRIVED_TARGET));
						return;
					}
				}
				if (alldist <= 0)
				{
					moveNext(false);
					return;
				}
				donedist += ((now - _startMoveTime) * _previousSpeed) / 1000.;
				double done = donedist / alldist;
				if (done < 0)
				{
					done = 0;
				}
				if (done >= 1)
				{
					moveNext(false);
					return;
				}
				if (isMovementDisabled())
				{
					stopMove();
					return;
				}
				Location loc = null;
				int index = (int) (moveList.size() * done);
				if (index >= moveList.size())
				{
					index = moveList.size() - 1;
				}
				if (index < 0)
				{
					index = 0;
				}
				loc = moveList.get(index).clone().geo2world();
				if (!isFlying() && !isInBoat() && !isInWater() && !isBoat())
				{
					if ((loc.z - getZ()) > 256)
					{
						String bug_text = "geo bug 1 at: " + getLoc() + " => " + loc.x + "," + loc.y + "," + loc.z + "\tAll path: " + moveList.get(0) + " => " + moveList.get(moveList.size() - 1);
						Log.add(bug_text, "geo");
						stopMove();
						return;
					}
				}
				if ((loc == null) || isMovementDisabled())
				{
					stopMove();
					return;
				}
				setLoc(loc, true);
				if (isMovementDisabled())
				{
					stopMove();
					return;
				}
				if (isFollow && ((now - _followTimestamp) > (_forestalling ? 500 : 1000)) && (follow != null) && !follow.isInRange(movingDestTempPos, Math.max(100, _offset)))
				{
					if ((Math.abs(getZ() - loc.z) > 1000) && !isFlying())
					{
						sendPacket(SystemMsg.CANNOT_SEE_TARGET);
						stopMove();
						return;
					}
					if (buildPathTo(follow.getX(), follow.getY(), follow.getZ(), _offset, follow, true, true))
					{
						movingDestTempPos.set(follow.getX(), follow.getY(), follow.getZ());
					}
					else
					{
						stopMove();
						return;
					}
					moveNext(true);
					return;
				}
				_previousSpeed = speed;
				_startMoveTime = now;
				_moveTask = ThreadPoolManager.getInstance().schedule(this, getMoveTickInterval());
			}
			catch (Exception e)
			{
				_log.error("", e);
			}
			finally
			{
				moveLock.unlock();
			}
		}
	}
	
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(Creature.class);
	/**
	 * Field HEADINGS_IN_PI. (value is 10430.378350470453)
	 */
	public static final double HEADINGS_IN_PI = 10430.378350470452724949566316381;
	/**
	 * Field INTERACTION_DISTANCE. (value is 200)
	 */
	public static final int INTERACTION_DISTANCE = 200;
	/**
	 * Field _castingSkill.
	 */
	private Skill _castingSkill;
	/**
	 * Field _castInterruptTime.
	 */
	private long _castInterruptTime;
	/**
	 * Field _animationEndTime.
	 */
	private long _animationEndTime;
	/**
	 * Field _scheduledCastCount.
	 */
	public int _scheduledCastCount;
	/**
	 * Field _scheduledCastInterval.
	 */
	public int _scheduledCastInterval;
	/**
	 * Field _skillTask.
	 */
	public Future<?> _skillTask;
	/**
	 * Field _skillLaunchedTask.
	 */
	public Future<?> _skillLaunchedTask;
	/**
	 * Field _skillDoubleTask.
	 */
	public Future<?> _skillDoubleTask;
	/**
	 * Field _skillDoubleLaunchedTask.
	 */
	public Future<?> _skillDoubleLaunchedTask;
	/**
	 * Field _stanceTask.
	 */
	private Future<?> _stanceTask;
	/**
	 * Field _stanceTaskRunnable.
	 */
	private Runnable _stanceTaskRunnable;
	/**
	 * Field _stanceEndTime.
	 */
	private long _stanceEndTime;
	/**
	 * Field CLIENT_BAR_SIZE. (value is 352)
	 */
	public final static int CLIENT_BAR_SIZE = 352;
	/**
	 * Field _lastCpBarUpdate.
	 */
	private int _lastCpBarUpdate = -1;
	/**
	 * Field _lastHpBarUpdate.
	 */
	private int _lastHpBarUpdate = -1;
	/**
	 * Field _lastMpBarUpdate.
	 */
	private int _lastMpBarUpdate = -1;
	/**
	 * Field _currentCp.
	 */
	protected double _currentCp = 0;
	/**
	 * Field _currentHp.
	 */
	protected double _currentHp = 1;
	/**
	 * Field _currentMp.
	 */
	protected double _currentMp = 1;
	/**
	 * Field _abnormalEffects.
	 */
	private int _abnormalEffects;
	/**
	 * Field _abnormalEffects2.
	 */
	private int _abnormalEffects2;
	/**
	 * Field _abnormalEffects3.
	 */
	private int _abnormalEffects3;
	/**
	 * Field _isAttackAborted.
	 */
	
	private final FastList<Integer> _aveList = new FastList<Integer>();
	
	protected boolean _isAttackAborted;
	/**
	 * Field _attackEndTime.
	 */
	protected long _attackEndTime;
	/**
	 * Field _attackReuseEndTime.
	 */
	protected long _attackReuseEndTime;
	/**
	 * Field _poleAttackCount.
	 */
	private int _poleAttackCount = 0;
	/**
	 * Field POLE_VAMPIRIC_MOD.
	 */
	private static final double[] POLE_VAMPIRIC_MOD =
	{
		1,
		0.9,
		0,
		7,
		0.2,
		0.01
	};
	/**
	 * Field _skills.
	 */
	protected final Map<Integer, Skill> _skills = new ConcurrentSkipListMap<Integer, Skill>();
	/**
	 * Field _triggers.
	 */
	protected Map<TriggerType, Set<TriggerInfo>> _triggers;
	/**
	 * Field _skillReuses.
	 */
	protected final IntObjectMap<TimeStamp> _skillReuses = new CHashIntObjectMap<TimeStamp>();
	/**
	 * Field _effectList.
	 */
	protected volatile EffectList _effectList;
	/**
	 * Field _statsRecorder.
	 */
	protected volatile CharStatsChangeRecorder<? extends Creature> _statsRecorder;
	/**
	 * Field _blockedStats.
	 */
	private List<Stats> _blockedStats;
	/**
	 * Field isDead.
	 */
	protected AtomicBoolean isDead = new AtomicBoolean();
	/**
	 * Field isTeleporting.
	 */
	protected AtomicBoolean isTeleporting = new AtomicBoolean();
	/**
	 * Field _skillMastery.
	 */
	private Map<Integer, Integer> _skillMastery;
	/**
	 * Field _isInvul.
	 */
	protected boolean _isInvul;
	/**
	 * Field _fakeDeath.
	 */
	private boolean _fakeDeath;
	/**
	 * Field _isBlessedByNoblesse.
	 */
	private boolean _isBlessedByNoblesse;
	/**
	 * Field _isSalvation.
	 */
	private boolean _isSalvation;
	/**
	 * Field _meditated.
	 */
	private boolean _meditated;
	/**
	 * Field _lockedTarget.
	 */
	private boolean _lockedTarget;
	/**
	 * Field _isTargetable.
	 */
	private boolean _isTargetable = true;
	/**
	 * Field _blocked.
	 */
	private boolean _blocked;
	/**
	 * Field _afraid.
	 */
	private final AtomicState _afraid = new AtomicState();
	/**
	 * Field _muted.
	 */
	private final AtomicState _muted = new AtomicState();
	/**
	 * Field _pmuted.
	 */
	private final AtomicState _pmuted = new AtomicState();
	/**
	 * Field _amuted.
	 */
	private final AtomicState _amuted = new AtomicState();
	/**
	 * Field _pulled.
	 */
	private final AtomicState _pulled = new AtomicState();
	/**
	 * Field _airbinded.
	 */
	private final AtomicState _airbinded = new AtomicState();
	/**
	 * Field _knockbacked.
	 */
	private final AtomicState _knockedback = new AtomicState();
	/**
	 * Field _knockdowned.
	 */
	private final AtomicState _knockeddown = new AtomicState();
	/**
	 * Field _paralyzed.
	 */
	private final AtomicState _paralyzed = new AtomicState();
	/**
	 * Field _rooted.
	 */
	private final AtomicState _rooted = new AtomicState();
	/**
	 * Field _sleeping.
	 */
	private final AtomicState _sleeping = new AtomicState();
	/**
	 * Field _stunned.
	 */
	private final AtomicState _stunned = new AtomicState();
	/**
	 * Field _immobilized.
	 */
	private final AtomicState _immobilized = new AtomicState();
	/**
	 * Field _confused.
	 */
	private final AtomicState _confused = new AtomicState();
	/**
	 * Field _frozen.
	 */
	private final AtomicState _frozen = new AtomicState();
	/**
	 * Field _healBlocked.
	 */
	private final AtomicState _healBlocked = new AtomicState();
	/**
	 * Field _damageBlocked.
	 */
	private final AtomicState _damageBlocked = new AtomicState();
	/**
	 * Field _buffImmunity.
	 */
	private final AtomicState _buffImmunity = new AtomicState();
	/**
	 * Field _debuffImmunity.
	 */
	private final AtomicState _debuffImmunity = new AtomicState();
	/**
	 * Field _effectImmunity.
	 */
	private final AtomicState _effectImmunity = new AtomicState();
	/**
	 * Field _weaponEquipBlocked.
	 */
	private final AtomicState _weaponEquipBlocked = new AtomicState();
	/**
	 * Field _flying.
	 */
	private boolean _flying;
	/**
	 * Field _running.
	 */
	private boolean _running;
	/**
	 * Field isMoving.
	 */
	public boolean isMoving;
	/**
	 * Field isFollow.
	 */
	public boolean isFollow;
	/**
	 * Field moveLock.
	 */
	final Lock moveLock = new ReentrantLock();
	/**
	 * Field _moveTask.
	 */
	Future<?> _moveTask;
	/**
	 * Field _moveTaskRunnable.
	 */
	private MoveNextTask _moveTaskRunnable;
	/**
	 * Field moveList.
	 */
	List<Location> moveList;
	/**
	 * Field destination.
	 */
	private Location destination;
	/**
	 * Field movingDestTempPos.
	 */
	final Location movingDestTempPos = new Location();
	/**
	 * Field _offset.
	 */
	int _offset;
	/**
	 * Field _forestalling.
	 */
	boolean _forestalling;
	/**
	 * Field target.
	 */
	private volatile HardReference<? extends GameObject> target = HardReferences.emptyRef();
	/**
	 * Field castingTarget.
	 */
	private volatile HardReference<? extends Creature> castingTarget = HardReferences.emptyRef();
	/**
	 * Field followTarget.
	 */
	private volatile HardReference<? extends Creature> followTarget = HardReferences.emptyRef();
	/**
	 * Field _aggressionTarget.
	 */
	private volatile HardReference<? extends Creature> _aggressionTarget = HardReferences.emptyRef();
	/**
	 * Field _targetRecorder.
	 */
	private final List<List<Location>> _targetRecorder = new ArrayList<List<Location>>();
	/**
	 * Field _followTimestamp.
	 */
	long _followTimestamp;
	/**
	 * Field _startMoveTime.
	 */
	long _startMoveTime;
	/**
	 * Field _previousSpeed.
	 */
	int _previousSpeed = 0;
	/**
	 * Field _heading.
	 */
	private int _heading;
	/**
	 * Field _calculators.
	 */
	private final Calculator[] _calculators;
	/**
	 * Field _template.
	 */
	protected CharTemplate _template;
	/**
	 * Field _ai.
	 */
	protected volatile CharacterAI _ai;
	/**
	 * Field _name.
	 */
	protected String _name;
	/**
	 * Field _title.
	 */
	protected String _title;
	/**
	 * Field _team.
	 */
	protected TeamType _team = TeamType.NONE;
	/**
	 * Field _isRegenerating.
	 */
	private boolean _isRegenerating;
	/**
	 * Field regenLock.
	 */
	final Lock regenLock = new ReentrantLock();
	/**
	 * Field _regenTask.
	 */
	private Future<?> _regenTask;
	/**
	 * Field _regenTaskRunnable.
	 */
	private Runnable _regenTaskRunnable;
	/**
	 * Field _isEnabledDoubleCast.
	 */
	public boolean _isEnabledDoubleCast = false;
	/**
	 * Field _isKnockedDown.
	 */
	public boolean _isKnockedDown = false;
	/**
	 * Field _isKnockedDown.
	 */
	public boolean _isAirBind = false;
	/**
	 * Field _zones.
	 */
	private final List<Zone> _zones = new LazyArrayList<Zone>();
	/**
	 * Field zonesLock.
	 */
	private final ReadWriteLock zonesLock = new ReentrantReadWriteLock();
	/**
	 * Field zonesRead.
	 */
	private final Lock zonesRead = zonesLock.readLock();
	/**
	 * Field zonesWrite.
	 */
	private final Lock zonesWrite = zonesLock.writeLock();
	/**
	 * Field listeners.
	 */
	protected volatile CharListenerList listeners;
	/**
	 * Field _deathImmune.
	 */
	protected boolean _deathImmune = false;

	private List<Player> _statusListeners;
	private final Lock statusListenersLock = new ReentrantLock();

	/**
	 * Field _walkerRoutesTemplate.
	 */
	protected WalkerRouteTemplate _walkerRoutesTemplate = null;
	/**
	 * Field _storedId.
	 */
	protected Long _storedId;
	
	/**
	 * Method getStoredId.
	 * @return Long
	 */
	public final Long getStoredId()
	{
		return _storedId;
	}
	
	/**
	 * Field reference.
	 */
	protected HardReference<? extends Creature> reference;
	/**
	 * Field _transformationId.
	 */
	private int _transformationId;
	/**
	 * Field _transformationTemplate.
	 */
	private int _transformationTemplate;
	/**
	 * Field _transformationName.
	 */
	private String _transformationName;
	
	/**
	 * Constructor for Creature.
	 * @param objectId int
	 * @param template CharTemplate
	 */
	public Creature(int objectId, CharTemplate template)
	{
		super(objectId);
		_template = template;
		_calculators = new Calculator[Stats.NUM_STATS];
		StatFunctions.addPredefinedFuncs(this);
		reference = new L2Reference<>(this);
		_storedId = GameObjectsStorage.put(this);
	}
	
	/**
	 * Method getRef.
	 * @return HardReference<? extends Creature>
	 */
	@Override
	public HardReference<? extends Creature> getRef()
	{
		return reference;
	}
	
	/**
	 * Method isAttackAborted.
	 * @return boolean
	 */
	public boolean isAttackAborted()
	{
		return _isAttackAborted;
	}
	
	/**
	 * Method abortAttack.
	 * @param force boolean
	 * @param message boolean
	 */
	public final void abortAttack(boolean force, boolean message)
	{
		if (isAttackingNow())
		{
			_attackEndTime = 0;
			if (force)
			{
				_isAttackAborted = true;
			}
			getAI().setIntention(AI_INTENTION_ACTIVE);
			if (isPlayer() && message)
			{
				sendActionFailed();
				sendPacket(new SystemMessage(SystemMessage.C1S_ATTACK_FAILED).addName(this));
			}
		}
	}
	
	/**
	 * Method abortCast.
	 * @param force boolean
	 * @param message boolean
	 */
	public final void abortCast(boolean force, boolean message)
	{
		if (isCastingNow() && (force || canAbortCast()))
		{
			final Skill castingSkill = _castingSkill;
			final Future<?> skillTask = _skillTask;
			final Future<?> skillLaunchedTask = _skillLaunchedTask;
			final Future<?> skillDoubleTask = _skillDoubleTask;
			final Future<?> skillDoubleLaunchedTask = _skillDoubleLaunchedTask;
			finishFly();
			clearCastVars();
			if (skillTask != null)
			{
				skillTask.cancel(false);
			}
			if (skillLaunchedTask != null)
			{
				skillLaunchedTask.cancel(false);
			}
			if (skillDoubleTask != null)
			{
				skillDoubleTask.cancel(false);
			}
			if (skillDoubleLaunchedTask != null)
			{
				skillDoubleLaunchedTask.cancel(false);
			}
			if (castingSkill != null)
			{
				if (castingSkill.isUsingWhileCasting())
				{
					Creature target = getAI().getAttackTarget();
					if (target != null)
					{
						target.getEffectList().stopEffect(castingSkill.getId());
					}
				}
				removeSkillMastery(castingSkill.getId());
			}
			broadcastPacket(new MagicSkillCanceled(getObjectId()));
			getAI().setIntention(AI_INTENTION_ACTIVE);
			if (isPlayer() && message)
			{
				sendPacket(Msg.CASTING_HAS_BEEN_INTERRUPTED);
			}
		}
	}
	
	/**
	 * Method canAbortCast.
	 * @return boolean
	 */
	public final boolean canAbortCast()
	{
		return _castInterruptTime > System.currentTimeMillis();
	}
	
	/**
	 * Method absorbAndReflect.
	 * @param target Creature
	 * @param skill Skill
	 * @param damage double
	 * @return boolean
	 */
	public boolean absorbAndReflect(Creature target, Skill skill, double damage)
	{
		if (target.isDead())
		{
			return false;
		}
		boolean bow = (getActiveWeaponItem() != null) && ((getActiveWeaponItem().getItemType() == WeaponType.BOW) || (getActiveWeaponItem().getItemType() == WeaponType.CROSSBOW));
		double value = 0;
		if ((skill != null) && skill.isMagic())
		{
			value = target.calcStat(Stats.REFLECT_AND_BLOCK_MSKILL_DAMAGE_CHANCE, 0, this, skill);
		}
		else if ((skill != null) && (skill.getCastRange() <= 200))
		{
			value = target.calcStat(Stats.REFLECT_AND_BLOCK_PSKILL_DAMAGE_CHANCE, 0, this, skill);
		}
		else if ((skill == null) && !bow)
		{
			value = target.calcStat(Stats.REFLECT_AND_BLOCK_DAMAGE_CHANCE, 0, this, null);
		}
		if ((value > 0) && Rnd.chance(value))
		{
			reduceCurrentHp(damage, 0, target, null, true, true, false, false, false, false, true);
			return true;
		}
		if ((skill != null) && skill.isMagic())
		{
			value = target.calcStat(Stats.REFLECT_MSKILL_DAMAGE_PERCENT, 0, this, skill) - this.calcStat(Stats.REFLECT_RESISTANCE_PERCENT,0.,target, skill);
		}
		else if ((skill != null) && (skill.getCastRange() <= 200))
		{
			value = target.calcStat(Stats.REFLECT_PSKILL_DAMAGE_PERCENT, 0, this, skill) - this.calcStat(Stats.REFLECT_RESISTANCE_PERCENT,0.,target, skill);
		}
		else if ((skill == null) && !bow)
		{
			value = target.calcStat(Stats.REFLECT_DAMAGE_PERCENT, 0, this, null) - this.calcStat(Stats.REFLECT_RESISTANCE_PERCENT,0.,target, null);			
		}
		if (value > 0)
		{
			if ((target.getCurrentHp() + target.getCurrentCp()) > damage)
			{
				reduceCurrentHp((value / 100.) * damage, 0, target, null, true, true, false, false, false, false, true);
			}
		}
		if ((skill != null || bow) && Config.ALT_ABSORB_DAMAGE_ONLY_MEELE)
		{
			return false;
		}
		damage = (int) (damage - target.getCurrentCp());
		if (damage <= 0)
		{
			return false;
		}
		final double poleMod = _poleAttackCount < POLE_VAMPIRIC_MOD.length ? POLE_VAMPIRIC_MOD[_poleAttackCount] : 0;
		double absorb = poleMod * calcStat(Stats.ABSORB_DAMAGE_PERCENT, 0, target, null);
		double limit;
		if ((absorb > 0) && !target.isDamageBlocked())
		{
			limit = (calcStat(Stats.HP_LIMIT, null, null) * getMaxHp()) / 100.;
			if (getCurrentHp() < limit)
			{
				setCurrentHp(Math.min(_currentHp + ((damage * absorb * Config.ALT_ABSORB_DAMAGE_MODIFIER) / 100.), limit), false);
			}
		}
		absorb = poleMod * calcStat(Stats.ABSORB_DAMAGEMP_PERCENT, 0, target, null);
		if ((absorb > 0) && !target.isDamageBlocked())
		{
			limit = (calcStat(Stats.MP_LIMIT, null, null) * getMaxMp()) / 100.;
			if (getCurrentMp() < limit)
			{
				setCurrentMp(Math.min(_currentMp + ((damage * absorb * Config.ALT_ABSORB_DAMAGE_MODIFIER) / 100.), limit));
			}
		}
		return false;
	}
	
	/**
	 * Method absorbToEffector.
	 * @param attacker Creature
	 * @param damage double
	 * @return double
	 */
	public double absorbToEffector(Creature attacker, double damage)
	{
		double transferToEffectorDam = calcStat(Stats.TRANSFER_TO_EFFECTOR_DAMAGE_PERCENT, 0.);
		if (transferToEffectorDam > 0)
		{
			Effect effect = getEffectList().getEffectByType(EffectType.AbsorbDamageToEffector);
			if (effect == null)
			{
				return damage;
			}
			Creature effector = effect.getEffector();
			if ((effector == this) || effector.isDead() || !isInRange(effector, 1200))
			{
				return damage;
			}
			Player thisPlayer = getPlayer();
			Player effectorPlayer = effector.getPlayer();
			if ((thisPlayer != null) && (effectorPlayer != null))
			{
				if ((thisPlayer != effectorPlayer) && (!thisPlayer.isOnline() || !thisPlayer.isInParty() || (thisPlayer.getParty() != effectorPlayer.getParty())))
				{
					return damage;
				}
			}
			else
			{
				return damage;
			}
			double transferDamage = (damage * transferToEffectorDam) * .01;
			damage -= transferDamage;
			effector.reduceCurrentHp(transferDamage, 0, effector, null, false, false, !attacker.isPlayable(), false, true, false, true);
		}
		return damage;
	}
	
	/**
	 * Method absorbToMp.
	 * @param attacker Creature
	 * @param damage double
	 * @return double
	 */
	public double absorbToMp(Creature attacker, double damage)
	{
		double transferToMpDamPercent = calcStat(Stats.TRANSFER_TO_MP_DAMAGE_PERCENT, 0.);
		if (transferToMpDamPercent > 0)
		{
			double transferDamage = (damage * transferToMpDamPercent) * .01;
			double currentMp = getCurrentMp();
			if (currentMp > transferDamage)
			{
				setCurrentMp(getCurrentMp() - transferDamage);
				return 0;
			}
			if (currentMp > 0)
			{
				damage -= currentMp;
				setCurrentMp(0);
				sendPacket(SystemMsg.MP_BECAME_0_AND_THE_ARCANE_SHIELD_IS_DISAPPEARING);
			}
			getEffectList().stopEffects(EffectType.AbsorbDamageToMp);
			return damage;
		}
		return damage;
	}
	
	/**
	 * Method absorbToSummon.
	 * @param attacker Creature
	 * @param damage double
	 * @return double
	 */
	public double absorbToSummon(Creature attacker, double damage)
	{
		if (!isPlayer())
		{
			return damage;
		}
		double transferToSummonDam = calcStat(Stats.TRANSFER_TO_SUMMON_DAMAGE_PERCENT, 0.);
		if (transferToSummonDam > 0)
		{
			//TRANSFER DAMAGE ALWAYS TO THE FIRST SUMMON
			Summon summon = null;
			List<Summon> servitors = ((Player) this).getSummonList().getServitors();
			double transferDamage = (damage * transferToSummonDam) * .01;
			if (servitors.size() > 0)
			{
				summon = servitors.get(0);
			}
			if (summon != null && !summon.isDead() && summon.getCurrentHp()>transferDamage && summon.isInRangeZ(this, 1200))
			{
				damage -= transferDamage;
				summon.reduceCurrentHp(transferDamage, 0, summon, null, false, false, false, false, true, false, true);
			}
			else
			{
				getEffectList().stopEffects(EffectType.AbsorbDamageToSummon);
				return damage;
			}
			/* 
			List<Summon> servitors = ((Player) this).getSummonList().getServitors();
			double transferDamage = (damage * transferToSummonDam) * .01;
			for (Iterator<Summon> it = servitors.iterator(); it.hasNext();)
			{
				Summon summon = it.next();
				if ((summon == null) || summon.isDead() || (summon.getCurrentHp() < (transferDamage / servitors.size())))
				{
					it.remove();
				}
			}
			if (servitors.size() > 0)
			{
				for (Summon summon : servitors)
				{
					if (summon.isInRangeZ(this, 1200))
					{
						damage -= transferDamage;
						summon.reduceCurrentHp(transferDamage, 0, summon, null, false, false, false, false, true, false, true);
					}
				}
			}
			else
			{
				getEffectList().stopEffects(EffectType.AbsorbDamageToSummon);
				return damage;
			}
			*/
		}
		return damage;
	}
	
	/**
	 * Method addBlockStats.
	 * @param stats List<Stats>
	 */
	public void addBlockStats(List<Stats> stats)
	{
		if (_blockedStats == null)
		{
			_blockedStats = new ArrayList<Stats>();
		}
		_blockedStats.addAll(stats);
	}
	
	/**
	 * Method addSkill.
	 * @param newSkill Skill
	 * @return Skill
	 */
	public Skill addSkill(Skill newSkill)
	{
		if (newSkill == null)
		{
			return null;
		}
		Skill oldSkill = _skills.get(newSkill.getId());
		if ((oldSkill != null) && (oldSkill.getLevel() == newSkill.getLevel()))
		{
			return newSkill;
		}
		_skills.put(newSkill.getId(), newSkill);
		if (oldSkill != null)
		{
			removeStatsOwner(oldSkill);
			removeTriggers(oldSkill);
		}
		addTriggers(newSkill);
		addStatFuncs(newSkill.getStatFuncs());
		return oldSkill;
	}
	
	/**
	 * Method getCalculators.
	 * @return Calculator[]
	 */
	public Calculator[] getCalculators()
	{
		return _calculators;
	}
	
	/**
	 * Method addStatFunc.
	 * @param f Func
	 */
	public final void addStatFunc(Func f)
	{
		if (f == null)
		{
			return;
		}
		int stat = f.stat.ordinal();
		synchronized (_calculators)
		{
			if (_calculators[stat] == null)
			{
				_calculators[stat] = new Calculator(f.stat, this);
			}
			_calculators[stat].addFunc(f);
		}
	}
	
	/**
	 * Method addStatFuncs.
	 * @param funcs Func[]
	 */
	public final void addStatFuncs(Func[] funcs)
	{
		for (Func f : funcs)
		{
			addStatFunc(f);
		}
	}
	
	/**
	 * Method removeStatFunc.
	 * @param f Func
	 */
	public final void removeStatFunc(Func f)
	{
		if (f == null)
		{
			return;
		}
		int stat = f.stat.ordinal();
		synchronized (_calculators)
		{
			if (_calculators[stat] != null)
			{
				_calculators[stat].removeFunc(f);
			}
		}
	}
	
	/**
	 * Method removeStatFuncs.
	 * @param funcs Func[]
	 */
	public final void removeStatFuncs(Func[] funcs)
	{
		for (Func f : funcs)
		{
			removeStatFunc(f);
		}
	}
	
	/**
	 * Method removeStatsOwner.
	 * @param owner Object
	 */
	public final void removeStatsOwner(Object owner)
	{
		synchronized (_calculators)
		{
			for (Calculator _calculator : _calculators)
			{
				if (_calculator != null)
				{
					_calculator.removeOwner(owner);
				}
			}
		}
	}
	
	/**
	 * Method altOnMagicUseTimer.
	 * @param aimingTarget Creature
	 * @param skill Skill
	 */
	public void altOnMagicUseTimer(Creature aimingTarget, Skill skill)
	{
		if (isAlikeDead())
		{
			return;
		}
		int magicId = skill.getDisplayId();
		int level = Math.max(1, getSkillDisplayLevel(skill.getId()));
		List<Creature> targets = skill.getTargets(this, aimingTarget, true);
		broadcastPacket(new MagicSkillLaunched(getObjectId(), magicId, level, targets));
		double mpConsume2 = skill.getMpConsume2();
		if (mpConsume2 > 0)
		{
			if (_currentMp < mpConsume2)
			{
				sendPacket(Msg.NOT_ENOUGH_MP);
				return;
			}
			if (skill.isMagic())
			{
				reduceCurrentMp(calcStat(Stats.MP_MAGIC_SKILL_CONSUME, mpConsume2, aimingTarget, skill), null);
			}
			else
			{
				reduceCurrentMp(calcStat(Stats.MP_PHYSICAL_SKILL_CONSUME, mpConsume2, aimingTarget, skill), null);
			}
		}
		callSkill(skill, targets, false);
	}
	
	/**
	 * Method altUseSkill.
	 * @param skill Skill
	 * @param target Creature
	 */
	public void altUseSkill(Skill skill, Creature target)
	{
		if (skill == null)
		{
			return;
		}
		int magicId = skill.getId();
		if (isUnActiveSkill(magicId))
		{
			return;
		}
		if (isSkillDisabled(skill))
		{
			sendReuseMessage(skill);
			return;
		}
		if (target == null)
		{
			target = skill.getAimingTarget(this, getTarget());
			if (target == null)
			{
				return;
			}
		}
		getListeners().onMagicUse(skill, target, true);
		int itemConsume[] = skill.getItemConsume();
		if (itemConsume[0] > 0)
		{
			for (int i = 0; i < itemConsume.length; i++)
			{
				if (!consumeItem(skill.getItemConsumeId()[i], itemConsume[i]))
				{
					sendPacket(skill.isHandler() ? SystemMsg.INCORRECT_ITEM_COUNT : SystemMsg.THERE_ARE_NOT_ENOUGH_NECESSARY_ITEMS_TO_USE_THE_SKILL);
					return;
				}
			}
		}
		if (skill.getReferenceItemId() > 0)
		{
			if (!consumeItemMp(skill.getReferenceItemId(), skill.getReferenceItemMpConsume()))
			{
				return;
			}
		}
		if (skill.getSoulsConsume() > getConsumedSouls())
		{
			sendPacket(Msg.THERE_IS_NOT_ENOUGHT_SOUL);
			return;
		}
		if (skill.getEnergyConsume() > getAgathionEnergy())
		{
			sendPacket(SystemMsg.THE_SKILL_HAS_BEEN_CANCELED_BECAUSE_YOU_HAVE_INSUFFICIENT_ENERGY);
			return;
		}
		if (skill.getSoulsConsume() > 0)
		{
			setConsumedSouls(getConsumedSouls() - skill.getSoulsConsume(), null);
		}
		if (skill.getEnergyConsume() > 0)
		{
			setAgathionEnergy(getAgathionEnergy() - skill.getEnergyConsume());
		}
		int level = Math.max(1, getSkillDisplayLevel(magicId));
		Formulas.calcSkillMastery(skill, this);
		long reuseDelay = Formulas.calcSkillReuseDelay(this, skill);
		if (!skill.isToggle() || skill.isAwakeningToggle())
		{
			broadcastPacket(new MagicSkillUse(this, target, skill.getDisplayId(), level, skill.getHitTime(), reuseDelay));
		}
		if (!skill.isHideUseMessage())
		{
			if (skill.getSkillType() == SkillType.PET_SUMMON)
			{
				sendPacket(new SystemMessage(SystemMessage.SUMMON_A_PET));
			}
			else if (!skill.isHandler())
			{
				sendPacket(new SystemMessage(SystemMessage.YOU_USE_S1).addSkillName(magicId, level));
			}
			else
			{
				sendPacket(new SystemMessage(SystemMessage.YOU_USE_S1).addItemName(skill.getItemConsumeId()[0]));
			}
		}
		if (!skill.isHandler())
		{
			disableSkill(skill, reuseDelay);
		}
		ThreadPoolManager.getInstance().schedule(new AltMagicUseTask(this, target, skill), skill.getHitTime());
	}
	
	/**
	 * Method sendReuseMessage.
	 * @param skill Skill
	 */
	public void sendReuseMessage(Skill skill)
	{
	}
	
	/**
	 * Method broadcastPacket.
	 * @param packets L2GameServerPacket[]
	 */
	public void broadcastPacket(L2GameServerPacket... packets)
	{
		sendPacket(packets);
		broadcastPacketToOthers(packets);
	}
	
	/**
	 * Method broadcastPacket.
	 * @param packets List<L2GameServerPacket>
	 */
	public void broadcastPacket(List<L2GameServerPacket> packets)
	{
		sendPacket(packets);
		broadcastPacketToOthers(packets);
	}
	
	/**
	 * Method broadcastPacketToOthers.
	 * @param packets L2GameServerPacket[]
	 */
	public void broadcastPacketToOthers(L2GameServerPacket... packets)
	{
		if (!isVisible() || (packets.length == 0))
		{
			return;
		}
		List<Player> players = World.getAroundPlayers(this);
		Player target;
		for (int i = 0; i < players.size(); i++)
		{
			target = players.get(i);
			target.sendPacket(packets);
		}
	}
	
	/**
	 * Method broadcastPacketToOthers.
	 * @param packets List<L2GameServerPacket>
	 */
	public void broadcastPacketToOthers(List<L2GameServerPacket> packets)
	{
		if (!isVisible() || packets.isEmpty())
		{
			return;
		}
		List<Player> players = World.getAroundPlayers(this);
		Player target;
		for (int i = 0; i < players.size(); i++)
		{
			target = players.get(i);
			target.sendPacket(packets);
		}
	}

	public void broadcastToStatusListeners(L2GameServerPacket... packets)
	{
		if(!isVisible() || (packets.length == 0))
		{
			return;
		}

		statusListenersLock.lock();

		try
		{
			if((_statusListeners == null) || _statusListeners.isEmpty())
			{
				return;
			}

			Player player;

			for(int i = 0; i < _statusListeners.size(); i++)
			{
				player = _statusListeners.get(i);

				player.sendPacket(packets);
			}
		}
		finally
		{
			statusListenersLock.unlock();
		}
	}

	public void addStatusListener(Player cha)
	{
		if(cha == this)
		{
			return;
		}

		statusListenersLock.lock();

		try
		{
			if(_statusListeners == null)
			{
				_statusListeners = new LazyArrayList<Player>();
			}

			if(!_statusListeners.contains(cha))
			{
				_statusListeners.add(cha);
			}
		}
		finally
		{
			statusListenersLock.unlock();
		}
	}

	public void removeStatusListener(Creature cha)
	{
		statusListenersLock.lock();

		try
		{
			if(_statusListeners == null)
			{
				return;
			}

			_statusListeners.remove(cha);
		}
		finally
		{
			statusListenersLock.unlock();
		}
	}

	public void clearStatusListeners()
	{
		statusListenersLock.lock();

		try
		{
			if(_statusListeners == null)
			{
				return;
			}

			_statusListeners.clear();
		}
		finally
		{
			statusListenersLock.unlock();
		}
	}

	/**
	 * Method broadcastStatusUpdate.
	 */
	public void broadcastStatusUpdate()
	{
		if (!needStatusUpdate())
		{
			return;
		}
		StatusUpdate statusUpdatePacket = new StatusUpdate(this).addAttribute(StatusUpdateField.CUR_HP, StatusUpdateField.MAX_HP);

		for(final Player pla : World.getAroundPlayers(this))
		{
			if(pla == null)
			{
				continue;
			}
			pla.sendPacket(statusUpdatePacket);
		}
	}
	
	/**
	 * Method calcHeading.
	 * @param x_dest int
	 * @param y_dest int
	 * @return int
	 */
	public int calcHeading(int x_dest, int y_dest)
	{
		return (int) (Math.atan2(getY() - y_dest, getX() - x_dest) * HEADINGS_IN_PI) + 32768;
	}
	
	/**
	 * Method calcStat.
	 * @param stat Stats
	 * @param init double
	 * @return double
	 */
	public final double calcStat(Stats stat, double init)
	{
		return calcStat(stat, init, null, null);
	}
	
	/**
	 * Method calcStat.
	 * @param stat Stats
	 * @param init double
	 * @param target Creature
	 * @param skill Skill
	 * @return double
	 */
	public final double calcStat(Stats stat, double init, Creature target, Skill skill)
	{
		int id = stat.ordinal();
		Calculator c = _calculators[id];
		if (c == null)
		{
			return init;
		}
		Env env = new Env();
		env.character = this;
		env.target = target;
		env.skill = skill;
		env.value = init;
		c.calc(env);
		return env.value;
	}
	
	/**
	 * Method calcStat.
	 * @param stat Stats
	 * @param target Creature
	 * @param skill Skill
	 * @return double
	 */
	public final double calcStat(Stats stat, Creature target, Skill skill)
	{
		Env env = new Env(this, target, skill);
		env.value = stat.getInit();
		int id = stat.ordinal();
		Calculator c = _calculators[id];
		if (c != null)
		{
			c.calc(env);
		}
		return env.value;
	}
	
	/**
	 * Method calculateAttackDelay.
	 * @return int
	 */
	public int calculateAttackDelay()
	{
		return Formulas.calcPAtkSpd(getPAtkSpd());
	}
	
	/**
	 * Method callSkill.
	 * @param skill Skill
	 * @param targets List<Creature>
	 * @param useActionSkills boolean
	 */
	public void callSkill(Skill skill, List<Creature> targets, boolean useActionSkills)
	{
		try
		{
			if (useActionSkills && !skill.isUsingWhileCasting() && (_triggers != null))
			{
				if (skill.isOffensive())
				{
					if (skill.isMagic())
					{
						useTriggers(getTarget(), TriggerType.OFFENSIVE_MAGICAL_SKILL_USE, null, skill, 0);
					}
					else
					{
						useTriggers(getTarget(), TriggerType.OFFENSIVE_PHYSICAL_SKILL_USE, null, skill, 0);
					}
				}
				else if (skill.isMagic())
				{
					final boolean targetSelf = skill.isAoE() || skill.isNotTargetAoE() || (skill.getTargetType() == Skill.SkillTargetType.TARGET_SELF);
					useTriggers(targetSelf ? this : getTarget(), TriggerType.SUPPORT_MAGICAL_SKILL_USE, null, skill, 0);
				}
			}
			Player pl = getPlayer();
			Creature target;
			Iterator<Creature> itr = targets.iterator();
			while (itr.hasNext())
			{
				target = itr.next();
				if (skill.isOffensive() && target.isInvul())
				{
					Player pcTarget = target.getPlayer();
					if ((!skill.isIgnoreInvul() || ((pcTarget != null) && pcTarget.isGM())) && !target.isArtefact())
					{
						itr.remove();
						continue;
					}
				}
				Effect ie = target.getEffectList().getEffectByType(EffectType.IgnoreSkill);
				if (ie != null)
				{
					if (ArrayUtils.contains(ie.getTemplate().getParam().getIntegerArray("skillId"), skill.getId()))
					{
						itr.remove();
						continue;
					}
				}
				for(EffectTemplate ef : skill.getEffectTemplates())
				{
					if((target.isAirBinded() ||target.isKnockedDown()) && (ef.getEffectType() == EffectType.KnockDown || ef.getEffectType() == EffectType.HellBinding ))
					{
						itr.remove();
						continue;
					}
				}
				target.getListeners().onMagicHit(skill, this);
				if (pl != null)
				{
					if ((target != null) && target.isNpc())
					{
						NpcInstance npc = (NpcInstance) target;
						List<QuestState> ql = pl.getQuestsForEvent(npc, QuestEventType.MOB_TARGETED_BY_SKILL, false);
						if (ql != null)
						{
							for (QuestState qs : ql)
							{
								qs.getQuest().notifySkillUse(npc, skill, qs);
							}
						}
					}
				}
				if (skill.getNegateSkill() > 0)
				{
					for (Effect e : target.getEffectList().getAllEffects())
					{
						Skill efs = e.getSkill();
						if ((efs.getId() == skill.getNegateSkill()) && e.isCancelable() && ((skill.getNegatePower() <= 0) || (efs.getPower() <= skill.getNegatePower())))
						{
							e.exit();
						}
					}
				}
				if(target.getEffectList().getEffectByType(EffectType.DispelOnHit) != null)
				{
					target.getEffectList().getEffectByType(EffectType.DispelOnHit).onActionTime();
				}
				if (skill.getCancelTarget() > 0)
				{
					if (Rnd.chance(skill.getCancelTarget()))
					{
						if (((target.getCastingSkill() == null) || !((target.getCastingSkill().getSkillType() == SkillType.TAKECASTLE) || (target.getCastingSkill().getSkillType() == SkillType.TAKEFORTRESS))) && !target.isRaid())
						{
							target.abortAttack(true, true);
							target.abortCast(true, true);
							target.setTarget(null);
						}
					}
				}
			}
			if (skill.isOffensive())
			{
				startAttackStanceTask();
			}
			if (!(skill.isNotTargetAoE() && skill.isOffensive() && (targets.size() == 0)))
			{
				skill.getEffects(this, this, false, true);
			}
			skill.useSkill(this, targets);
		}
		catch (Exception e)
		{
			_log.error("", e);
		}
	}
	
	/**
	 * Method useTriggers.
	 * @param target GameObject
	 * @param type TriggerType
	 * @param ex Skill
	 * @param owner Skill
	 * @param damage double
	 */
	public void useTriggers(GameObject target, TriggerType type, Skill ex, Skill owner, double damage)
	{
		if (_triggers == null)
		{
			return;
		}
		Set<TriggerInfo> SkillsOnSkillAttack = _triggers.get(type);
		if (SkillsOnSkillAttack != null)
		{
			for (TriggerInfo t : SkillsOnSkillAttack)
			{
				if (t.getSkill() != ex)
				{
					useTriggerSkill(target == null ? getTarget() : target, null, t, owner, damage);
				}
			}
		}
	}
	
	/**
	 * Method useTriggerSkill.
	 * @param target GameObject
	 * @param targets List<Creature>
	 * @param trigger TriggerInfo
	 * @param owner Skill
	 * @param damage double
	 */
	public void useTriggerSkill(GameObject target, List<Creature> targets, TriggerInfo trigger, Skill owner, double damage)
	{
		Skill skill = trigger.getSkill();
		if ((skill.getReuseDelay() > 0) && isSkillDisabled(skill))
		{
			return;
		}
		Creature aimTarget = skill.getAimingTarget(this, target);
		Creature realTarget = (target != null) && target.isCreature() ? (Creature) target : null;
		if (Rnd.chance(trigger.getChance()) && trigger.checkCondition(this, realTarget, aimTarget, owner, damage) && skill.checkCondition(this, aimTarget, false, true, true))
		{
			if (targets == null)
			{
				targets = skill.getTargets(this, aimTarget, false);
			}
			int displayId = 0, displayLevel = 0;
			if (skill.hasEffects())
			{
				displayId = skill.getEffectTemplates()[0]._displayId;
				displayLevel = skill.getEffectTemplates()[0]._displayLevel;
			}
			if (displayId == 0)
			{
				displayId = skill.getDisplayId();
			}
			if (displayLevel == 0)
			{
				displayLevel = skill.getDisplayLevel();
			}
			if (trigger.getType() != TriggerType.SUPPORT_MAGICAL_SKILL_USE)
			{
				for (Creature cha : targets)
				{
					broadcastPacket(new MagicSkillUse(this, cha, displayId, displayLevel, 0, 0));
				}
			}
			Formulas.calcSkillMastery(skill, this);
			callSkill(skill, targets, false);
			disableSkill(skill, skill.getReuseDelay());
		}
	}
	
	/**
	 * Method checkBlockedStat.
	 * @param stat Stats
	 * @return boolean
	 */
	public boolean checkBlockedStat(Stats stat)
	{
		return (_blockedStats != null) && _blockedStats.contains(stat);
	}
	
	/**
	 * Method checkReflectSkill.
	 * @param attacker Creature
	 * @param skill Skill
	 * @return boolean
	 */
	public boolean checkReflectSkill(Creature attacker, Skill skill)
	{
		if (!skill.isReflectable())
		{
			return false;
		}
		if (isInvul() || attacker.isInvul() || !skill.isOffensive())
		{
			return false;
		}
		if (skill.isMagic() && (skill.getSkillType() != SkillType.MDAM))
		{
			return false;
		}
		if (Rnd.chance(calcStat(skill.isMagic() ? Stats.REFLECT_MAGIC_SKILL : Stats.REFLECT_PHYSIC_SKILL, 0, attacker, skill)))
		{
			sendPacket(new SystemMessage(SystemMessage.YOU_COUNTERED_C1S_ATTACK).addName(attacker));
			attacker.sendPacket(new SystemMessage(SystemMessage.C1_DODGES_THE_ATTACK).addName(this));
			return true;
		}
		return false;
	}
	
	/**
	 * Method doCounterAttack.
	 * @param skill Skill
	 * @param attacker Creature
	 * @param blow boolean
	 */
	public void doCounterAttack(Skill skill, Creature attacker, boolean blow)
	{
		if (isDead())
		{
			return;
		}
		if (isDamageBlocked() || attacker.isDamageBlocked())
		{
			return;
		}
		if ((skill == null) || skill.hasEffects() || skill.isMagic() || !skill.isOffensive() || (skill.getCastRange() > 200))
		{
			return;
		}
		if (Rnd.chance(calcStat(Stats.COUNTER_ATTACK, 0, attacker, skill)))
		{
			double damage = (1189 * getPAtk(attacker)) / Math.max(attacker.getPDef(this), 1);
			attacker.sendPacket(new SystemMessage(SystemMessage.C1S_IS_PERFORMING_A_COUNTERATTACK).addName(this));
			if (blow)
			{
				sendPacket(new SystemMessage(SystemMessage.C1S_IS_PERFORMING_A_COUNTERATTACK).addName(this));
				sendPacket(new SystemMessage(SystemMessage.C1_HAS_GIVEN_C2_DAMAGE_OF_S3).addName(this).addName(attacker).addNumber((long) damage));
				attacker.reduceCurrentHp(damage, damage, this, skill, true, true, false, false, false, false, true);
			}
			else
			{
				sendPacket(new SystemMessage(SystemMessage.C1S_IS_PERFORMING_A_COUNTERATTACK).addName(this));
			}
			sendPacket(new SystemMessage(SystemMessage.C1_HAS_GIVEN_C2_DAMAGE_OF_S3).addName(this).addName(attacker).addNumber((long) damage));
			attacker.reduceCurrentHp(damage, damage, this, skill, true, true, false, false, false, false, true);
		}
	}
	
	/**
	 * Method disableSkill.
	 * @param skill Skill
	 * @param delay long
	 */
	public void disableSkill(Skill skill, long delay)
	{
		_skillReuses.put(skill.hashCode(), new TimeStamp(skill, delay));
	}
	
	/**
	 * Method isAutoAttackable.
	 * @param attacker Creature
	 * @return boolean
	 */
	public abstract boolean isAutoAttackable(Creature attacker);
	
	/**
	 * Method doAttack.
	 * @param target Creature
	 */
	public void doAttack(Creature target)
	{
		if ((target == null) || isAMuted() || isAttackingNow() || isAlikeDead() || target.isAlikeDead() || !isInRange(target, 2000) || (isPlayer() && getPlayer().isInMountTransform()))
		{
			return;
		}
		getListeners().onAttack(target);
		int sAtk = Math.max(calculateAttackDelay(), 333);
		int ssGrade = 0;
		WeaponTemplate weaponItem = getActiveWeaponItem();
		if (weaponItem != null)
		{
			if (isPlayer() && (weaponItem.getAttackReuseDelay() > 0))
			{
				int reuse = (int) ((weaponItem.getAttackReuseDelay() * getReuseModifier(target) * 666 * calcStat(Stats.ATK_BASE, 0, target, null)) / 293. / getPAtkSpd());
				if (reuse > 0)
				{
					sendPacket(new SetupGauge(this, SetupGauge.RED_MINI, reuse));
					_attackReuseEndTime = (reuse + System.currentTimeMillis()) - 75;
					if (reuse > sAtk)
					{
						ThreadPoolManager.getInstance().schedule(new NotifyAITask(this, CtrlEvent.EVT_READY_TO_ACT, null, null), reuse);
					}
				}
			}
			ssGrade = weaponItem.getCrystalType().externalOrdinal;
		}
		_attackEndTime = (sAtk + System.currentTimeMillis()) - 10;
		_isAttackAborted = false;
		Attack attack = new Attack(this, target, getChargedSoulShot(), ssGrade);
		setHeading(PositionUtils.calculateHeadingFrom(this, target));
		if (weaponItem == null)
		{
			doAttackHitSimple(attack, target, 1., !isPlayer(), sAtk, true);
		}
		else
		{
			switch (weaponItem.getItemType())
			{
				case BOW:
				case CROSSBOW:
					doAttackHitByBow(attack, target, sAtk);
					break;
				case POLE:
					doAttackHitByPole(attack, target, sAtk);
					break;
				case DUAL:
				case DUALFIST:
				case DUALDAGGER:
					doAttackHitByDual(attack, target, sAtk);
					break;
				default:
					doAttackHitSimple(attack, target, 1., true, sAtk, true);
			}
		}
		if (attack.hasHits())
		{
			broadcastPacket(attack);
		}
	}
	
	/**
	 * Method doAttackHitSimple.
	 * @param attack Attack
	 * @param target Creature
	 * @param multiplier double
	 * @param unchargeSS boolean
	 * @param sAtk int
	 * @param notify boolean
	 */
	private void doAttackHitSimple(Attack attack, Creature target, double multiplier, boolean unchargeSS, int sAtk, boolean notify)
	{
		int damage1 = 0, reflectableDamage1 = 0;
		boolean shld1 = false;
		boolean crit1 = false;
		boolean miss1 = Formulas.calcHitMiss(this, target);
		if (!miss1)
		{
			AttackInfo info = Formulas.calcPhysDam(this, target, null, false, false, attack._soulshot, false);
			damage1 = (int) (info.damage * multiplier);
			reflectableDamage1 = (int) (info.reflectableDamage * multiplier);
			shld1 = info.shld;
			crit1 = info.crit;
		}
		ThreadPoolManager.getInstance().schedule(new HitTask(this, target, damage1, reflectableDamage1, crit1, miss1, attack._soulshot, shld1, unchargeSS, notify), sAtk);
		attack.addHit(target, damage1, miss1, crit1, shld1);
	}
	
	/**
	 * Method doAttackHitByBow.
	 * @param attack Attack
	 * @param target Creature
	 * @param sAtk int
	 */
	private void doAttackHitByBow(Attack attack, Creature target, int sAtk)
	{
		WeaponTemplate activeWeapon = getActiveWeaponItem();
		if (activeWeapon == null)
		{
			return;
		}
		int damage1 = 0, damage2 = 0;
		boolean shld1 = false;
		boolean crit1 = false;
		boolean miss1 = Formulas.calcHitMiss(this, target);
		reduceArrowCount();
		if (!miss1)
		{
			AttackInfo info = Formulas.calcPhysDam(this, target, null, false, false, attack._soulshot, false);
			damage1 = (int) info.damage;
			damage2 = (int) info.reflectableDamage;
			shld1 = info.shld;
			crit1 = info.crit;
			int range = activeWeapon.getAttackRange();
			damage1 *= ((Math.min(range, getDistance(target)) / range) * .4) + 0.8;
		}
		ThreadPoolManager.getInstance().schedule(new HitTask(this, target, damage1, damage2, crit1, miss1, attack._soulshot, shld1, true, true), sAtk);
		attack.addHit(target, damage2, miss1, crit1, shld1);
	}
	
	/**
	 * Method doAttackHitByDual.
	 * @param attack Attack
	 * @param target Creature
	 * @param sAtk int
	 */
	private void doAttackHitByDual(Attack attack, Creature target, int sAtk)
	{
		int damage1 = 0;
		int damage2 = 0;
		int reflectableDamage1 = 0, reflectableDamage2 = 0;
		boolean shld1 = false;
		boolean shld2 = false;
		boolean crit1 = false;
		boolean crit2 = false;
		boolean miss1 = Formulas.calcHitMiss(this, target);
		boolean miss2 = Formulas.calcHitMiss(this, target);
		if (!miss1)
		{
			AttackInfo info = Formulas.calcPhysDam(this, target, null, true, false, attack._soulshot, false);
			damage1 = (int) info.damage;
			reflectableDamage1 = (int) info.reflectableDamage;
			shld1 = info.shld;
			crit1 = info.crit;
		}
		if (!miss2)
		{
			AttackInfo info = Formulas.calcPhysDam(this, target, null, true, false, attack._soulshot, false);
			damage2 = (int) info.damage;
			reflectableDamage2 = (int) info.reflectableDamage;
			shld2 = info.shld;
			crit2 = info.crit;
		}
		ThreadPoolManager.getInstance().schedule(new HitTask(this, target, damage1, reflectableDamage1, crit1, miss1, attack._soulshot, shld1, true, false), sAtk / 2);
		ThreadPoolManager.getInstance().schedule(new HitTask(this, target, damage2, reflectableDamage2, crit2, miss2, attack._soulshot, shld2, false, true), sAtk);
		attack.addHit(target, damage1, miss1, crit1, shld1);
		attack.addHit(target, damage2, miss2, crit2, shld2);
	}
	
	/**
	 * Method doAttackHitByPole.
	 * @param attack Attack
	 * @param target Creature
	 * @param sAtk int
	 */
	private void doAttackHitByPole(Attack attack, Creature target, int sAtk)
	{
		int angle = (int) calcStat(Stats.POLE_ATTACK_ANGLE, 90, target, null);
		int range = (int) calcStat(Stats.POWER_ATTACK_RANGE, getTemplate().getBaseAtkRange(), target, null);
		int attackcountmax = (int) Math.round(calcStat(Stats.POLE_TARGET_COUNT, 0, target, null));
		if (isBoss())
		{
			attackcountmax += 27;
		}
		else if (isRaid())
		{
			attackcountmax += 12;
		}
		else if (isMonster() && (getLevel() > 0))
		{
			attackcountmax += getLevel() / 7.5;
		}
		double mult = 1.;
		_poleAttackCount = 1;
		if (!isInZonePeace())
		{
			for (Creature t : getAroundCharacters(range, 200))
			{
				if (_poleAttackCount <= attackcountmax)
				{
					if ((t == target) || t.isDead() || !PositionUtils.isFacing(this, t, angle))
					{
						continue;
					}
					if (t.isAutoAttackable(this))
					{
						doAttackHitSimple(attack, t, mult, false, sAtk, false);
						mult *= Config.ALT_POLE_DAMAGE_MODIFIER;
						_poleAttackCount++;
					}
				}
				else
				{
					break;
				}
			}
		}
		_poleAttackCount = 0;
		doAttackHitSimple(attack, target, 1., true, sAtk, true);
	}
	
	/**
	 * Method getAnimationEndTime.
	 * @return long
	 */
	public long getAnimationEndTime()
	{
		return _animationEndTime;
	}
	
	/**
	 * Method doCast.
	 * @param skill Skill
	 * @param target Creature
	 * @param forceUse boolean
	 */
	public void doCast(Skill skill, Creature target, boolean forceUse)
	{
		if (skill == null)
		{
			return;
		}
		int itemConsume[] = skill.getItemConsume();
		if (itemConsume[0] > 0)
		{
			for (int i = 0; i < itemConsume.length; i++)
			{
				if (!consumeItem(skill.getItemConsumeId()[i], itemConsume[i]))
				{
					sendPacket(skill.isHandler() ? SystemMsg.INCORRECT_ITEM_COUNT : SystemMsg.THERE_ARE_NOT_ENOUGH_NECESSARY_ITEMS_TO_USE_THE_SKILL);
					return;
				}
			}
		}
		if (skill.getReferenceItemId() > 0)
		{
			if (!consumeItemMp(skill.getReferenceItemId(), skill.getReferenceItemMpConsume()))
			{
				return;
			}
		}
		int magicId = skill.getId();
		if (target == null)
		{
			target = skill.getAimingTarget(this, getTarget());
		}
		if (target == null)
		{
			return;
		}
		getListeners().onMagicUse(skill, target, false);
		if (this != target)
		{
			setHeading(PositionUtils.calculateHeadingFrom(this, target));
		}
		int level = Math.max(1, getSkillDisplayLevel(magicId));
		int skillTime = skill.isSkillTimePermanent() ? skill.getHitTime() : Formulas.calcMAtkSpd(this, skill, skill.getHitTime());
		int skillInterruptTime = skill.isMagic() ? Formulas.calcMAtkSpd(this, skill, skill.getSkillInterruptTime()) : 0;
		int minCastTime = Math.min(Config.SKILLS_CAST_TIME_MIN, skill.getHitTime());
		if (skillTime < minCastTime)
		{
			skillTime = minCastTime;
			skillInterruptTime = 0;
		}
		_animationEndTime = System.currentTimeMillis() + skillTime;
		if (skill.isMagic() && !skill.isSkillTimePermanent() && (getChargedSpiritShot() > 0))
		{
			skillTime = (int) (0.70 * skillTime);
			skillInterruptTime = (int) (0.70 * skillInterruptTime);
		}
		Formulas.calcSkillMastery(skill, this);
		long reuseDelay = Math.max(0, Formulas.calcSkillReuseDelay(this, skill));
		broadcastPacket(new MagicSkillUse(this, target, skill.getDisplayId(), level, skillTime, reuseDelay, isDoubleCastingNow()));
		if(skill.getFlyType() == FlyType.CHARGE)
		{
			skillTime = minCastTime;
		}
		if (!skill.isHandler())
		{
			disableSkill(skill, reuseDelay);
		}
		if (isPlayer())
		{
			if (skill.getSkillType() == SkillType.PET_SUMMON)
			{
				sendPacket(Msg.SUMMON_A_PET);
			}
			else if (!skill.isHandler())
			{
				if (!skill.isAlterSkill())
					sendPacket(new SystemMessage(SystemMessage.YOU_USE_S1).addSkillName(magicId, level));
			}
			else
			{
				sendPacket(new SystemMessage(SystemMessage.YOU_USE_S1).addItemName(skill.getItemConsumeId()[0]));
			}
		}
		if (skill.getTargetType() == SkillTargetType.TARGET_HOLY)
		{
			target.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, this, 1);
		}
		double mpConsume1 = skill.isUsingWhileCasting() ? skill.getMpConsume() : skill.getMpConsume1();
		if (mpConsume1 > 0)
		{
			if (_currentMp < mpConsume1)
			{
				sendPacket(Msg.NOT_ENOUGH_MP);
				onCastEndTime(false);
				return;
			}
			reduceCurrentMp(mpConsume1, null);
		}
		_castingSkill = skill;
		_castInterruptTime = System.currentTimeMillis() + skillInterruptTime;
		setCastingTarget(target);
		if (skill.isUsingWhileCasting())
		{
			callSkill(skill, skill.getTargets(this, target, forceUse), true);
		}
		if (isPlayer())
		{
			sendPacket(new SetupGauge(this, SetupGauge.BLUE_DUAL, skillTime));
		}
		_scheduledCastCount = skill.getCastCount();
		_scheduledCastInterval = skill.getCastCount() > 0 ? skillTime / _scheduledCastCount : skillTime;
		if (!isDoubleCastingNow() && IsEnabledDoubleCast())
		{
			_skillDoubleLaunchedTask = ThreadPoolManager.getInstance().schedule(new MagicLaunchedTask(this, forceUse), skillInterruptTime);
			_skillDoubleTask = ThreadPoolManager.getInstance().schedule(new MagicUseTask(this, forceUse), skill.getCastCount() > 0 ? skillTime / skill.getCastCount() : skillTime);
		}
		else
		{
			_skillLaunchedTask = ThreadPoolManager.getInstance().schedule(new MagicLaunchedTask(this, forceUse), skillInterruptTime);
			_skillTask = ThreadPoolManager.getInstance().schedule(new MagicUseTask(this, forceUse), skill.getCastCount() > 0 ? skillTime / skill.getCastCount() : skillTime);
		}
	}
	
	/**
	 * Field _flyLoc.
	 */
	private Location _flyLoc;
	
	/**
	 * Method getFlyLocation.
	 * @param target GameObject
	 * @param skill Skill
	 * @return Location
	 */
	public Location getFlyLocation(GameObject target, Skill skill)
	{
		if ((target != null) && (target != this))
		{
			Location loc;
			double radian = PositionUtils.convertHeadingToRadian(target.getHeading());
			if (skill.isFlyToBack())
			{
				loc = new Location(target.getX() + (int) (Math.sin(radian) * 40), target.getY() - (int) (Math.cos(radian) * 40), target.getZ());
			}
			else
			{
				loc = new Location(target.getX() - (int) (Math.sin(radian) * 40), target.getY() + (int) (Math.cos(radian) * 40), target.getZ());
			}
			if (isFlying())
			{
				if (isPlayer() && ((Player) this).isInFlyingTransform() && ((loc.z <= 0) || (loc.z >= 6000)))
				{
					return null;
				}
				if (GeoEngine.moveCheckInAir(getX(), getY(), getZ(), loc.x, loc.y, loc.z, getColRadius(), getGeoIndex()) == null)
				{
					return null;
				}
			}
			else
			{
				loc.correctGeoZ();
				if (!GeoEngine.canMoveToCoord(getX(), getY(), getZ(), loc.x, loc.y, loc.z, getGeoIndex()))
				{
					loc = target.getLoc();
					if (!GeoEngine.canMoveToCoord(getX(), getY(), getZ(), loc.x, loc.y, loc.z, getGeoIndex()))
					{
						return null;
					}
				}
			}
			return loc;
		}
		int x1 = 0;
		int y1 = 0;
		int z1 = 0;
		if (skill.getFlyType() == FlyType.THROW_UP)
		{
			x1 = 0;
			y1 = 0;
			z1 = getZ() + skill.getFlyRadius();
		}
		else
		{
			double radian = PositionUtils.convertHeadingToRadian(getHeading());
			x1 = -(int) (Math.sin(radian) * skill.getFlyRadius());
			y1 = (int) (Math.cos(radian) * skill.getFlyRadius());
		}
		if (isFlying())
		{
			return GeoEngine.moveCheckInAir(getX(), getY(), getZ(), getX() + x1, getY() + y1, getZ() + z1, getColRadius(), getGeoIndex());
		}
		return GeoEngine.moveCheck(getX(), getY(), getZ(), getX() + x1, getY() + y1, getGeoIndex());
	}

	/**
	 * Method doDie.
	 * @param killer Creature
	 */
	public final void doDie(Creature killer)
	{
		if (!isDead.compareAndSet(false, true))
		{
			return;
		}
		onDeath(killer);
	}
	
	/**
	 * Method onDeath.
	 * @param killer Creature
	 */
	protected void onDeath(Creature killer)
	{
		if (killer != null)
		{
			Player killerPlayer = killer.getPlayer();
			if (killerPlayer != null)
			{
				killerPlayer.getListeners().onKillIgnorePetOrSummon(this);
				WorldStatisticsManager.getInstance().updateStat(killerPlayer, CategoryType.MONSTERS_KILLED, 1L);
			}
			killer.getListeners().onKill(this);
			if (isPlayer() && killer.isPlayable())
			{
				_currentCp = 0;
			}
		}
		setTarget(null);
		stopMove();
		stopAttackStanceTask();
		stopRegeneration();
		_currentHp = 0;
		if (isBlessedByNoblesse() || isSalvation())
		{
			if (isSalvation() && isPlayer() && !getPlayer().isInOlympiadMode())
			{
				getPlayer().reviveRequest(getPlayer(), 100, false);
			}
			for (Effect e : getEffectList().getAllEffects())
			{
				if ((e.getEffectType() == EffectType.BlessNoblesse) || (e.getSkill().getId() == Skill.SKILL_FORTUNE_OF_NOBLESSE) || (e.getSkill().getId() == Skill.SKILL_RAID_BLESSING))
				{
					e.exit();
				}
				else if (e.getEffectType() == EffectType.AgathionResurrect)
				{
					if (isPlayer())
					{
						getPlayer().setAgathionRes(true);
					}
					e.exit();
				}
			}
		}
		else
		{
			for (Effect e : getEffectList().getAllEffects())
			{
				if ((e.getEffectType() != EffectType.Transformation) && !e.getSkill().isPreservedOnDeath())
				{
					e.exit();
				}
			}
		}
		ThreadPoolManager.getInstance().execute(new NotifyAITask(this, CtrlEvent.EVT_DEAD, killer, null));
		getListeners().onDeath(killer);
		updateEffectIcons();
		updateStats();
		broadcastStatusUpdate();
	}
	
	/**
	 * Method onRevive.
	 */
	protected void onRevive()
	{
	}
	
	/**
	 * Method enableSkill.
	 * @param skill Skill
	 */
	public void enableSkill(Skill skill)
	{
		_skillReuses.remove(skill.hashCode());
	}
	
	/**
	 * Method getAbnormalEffect.
	 * @return integer
	 */
	public int getAbnormalEffect()
	{
		return _abnormalEffects;
	}
	
	/**
	 * Method getAbnormalEffect2.
	 * @return integer
	 */
	public int getAbnormalEffect2()
	{
		return _abnormalEffects2;
	}
	
	/**
	 * Method getAbnormalEffect3.
	 * @return integer
	 */
	public int getAbnormalEffect3()
	{
		return _abnormalEffects3;
	}
	
	/**
	 * Method getAveList.
	 * @return FastList<Integer>
	 */
	public FastList<Integer> getAveList()
	{
		return _aveList;
	}
	
	public void addToAveList(int aeId)
	{
		if (!_aveList.contains(aeId))
		{
			_aveList.add(aeId);
		}
	}
	
	public void removeFromAveList(int aeId)
	{
		if (_aveList.contains(aeId))
		{
			_aveList.remove(_aveList.indexOf(aeId));
		}
	}
	
	/**
	 * Method getAccuracy.
	 * @return int
	 */
	public int getAccuracy()
	{
		return (int) calcStat(Stats.ACCURACY_COMBAT, 0, null, null);
	}
	
	/**
	 * Method getMAccuracy.
	 * @return int
	 */
	public int getMAccuracy()
	{
		return (int) calcStat(Stats.MACCURACY_COMBAT, 0, null, null);
	}
	
	/**
	 * Method getAllSkills.
	 * @return Collection<Skill>
	 */
	public Collection<Skill> getAllSkills()
	{
		return _skills.values();
	}
	
	/**
	 * Method getAllSkillsArray.
	 * @return Skill[]
	 */
	public final Skill[] getAllSkillsArray()
	{
		Collection<Skill> vals = _skills.values();
		return vals.toArray(new Skill[vals.size()]);
	}
	
	/**
	 * Method getAttackSpeedMultiplier.
	 * @return double
	 */
	public final double getAttackSpeedMultiplier()
	{
		return (1.1 * getPAtkSpd()) / getTemplate().getBasePAtkSpd();
	}
	
	/**
	 * Method getBuffLimit.
	 * @return int
	 */
	public int getBuffLimit()
	{
		return (int) calcStat(Stats.BUFF_LIMIT, Config.ALT_BUFF_LIMIT, null, null);
	}
	
	/**
	 * Method getCastingSkill.
	 * @return Skill
	 */
	public Skill getCastingSkill()
	{
		return _castingSkill;
	}
	
	/**
	 * Method getCriticalHit.
	 * @param target Creature
	 * @param skill Skill
	 * @return int
	 */
	public int getCriticalHit(Creature target, Skill skill)
	{
		return (int) calcStat(Stats.CRITICAL_BASE, _template.getBaseCritRate(), target, skill);
	}

	/**
	 * Method getCriticalDmg.
	 * @param target Creature
	 * @param skill Skill
	 * @return int
	 */
	public int getCriticalDmg(Creature target, Skill skill)
	{
		return (int) calcStat(Stats.CRITICAL_DAMAGE, target, skill);
	}

	/**
	 * Method getMagicCriticalRate.
	 * @param target Creature
	 * @param skill Skill
	 * @return double
	 */
	public double getMagicCriticalRate(Creature target, Skill skill)
	{
		return (int) calcStat(Stats.MCRITICAL_RATE, target, skill);
	}

	/**
	 * Method getMagicCriticalRate.
	 * @param target Creature
	 * @param skill Skill
	 * @return double
	 */
	public double getMagicCriticalDmg(Creature target, Skill skill)
	{
		return calcStat(Stats.MCRITICAL_DAMAGE, target, skill);
	}

	/**
	 * Method getCurrentCp.
	 * @return double
	 */
	public final double getCurrentCp()
	{
		return _currentCp;
	}
	
	/**
	 * Method getCurrentCpRatio.
	 * @return double
	 */
	public final double getCurrentCpRatio()
	{
		return getCurrentCp() / getMaxCp();
	}
	
	/**
	 * Method getCurrentCpPercents.
	 * @return double
	 */
	public final double getCurrentCpPercents()
	{
		return getCurrentCpRatio() * 100.;
	}
	
	/**
	 * Method isCurrentCpFull.
	 * @return boolean
	 */
	public final boolean isCurrentCpFull()
	{
		return getCurrentCp() >= getMaxCp();
	}
	
	/**
	 * Method isCurrentCpZero.
	 * @return boolean
	 */
	public final boolean isCurrentCpZero()
	{
		return getCurrentCp() < 1;
	}
	
	/**
	 * Method getCurrentHp.
	 * @return double
	 */
	public final double getCurrentHp()
	{
		return _currentHp;
	}
	
	/**
	 * Method getCurrentHpRatio.
	 * @return double
	 */
	public final double getCurrentHpRatio()
	{
		return getCurrentHp() / getMaxHp();
	}
	
	/**
	 * Method getCurrentHpPercents.
	 * @return double
	 */
	public final double getCurrentHpPercents()
	{
		return getCurrentHpRatio() * 100.;
	}
	
	/**
	 * Method isCurrentHpFull.
	 * @return boolean
	 */
	public final boolean isCurrentHpFull()
	{
		return getCurrentHp() >= getMaxHp();
	}
	
	/**
	 * Method isCurrentHpZero.
	 * @return boolean
	 */
	public final boolean isCurrentHpZero()
	{
		return getCurrentHp() < 1;
	}
	
	/**
	 * Method getCurrentMp.
	 * @return double
	 */
	public final double getCurrentMp()
	{
		return _currentMp;
	}
	
	/**
	 * Method getCurrentMpRatio.
	 * @return double
	 */
	public final double getCurrentMpRatio()
	{
		return getCurrentMp() / getMaxMp();
	}
	
	/**
	 * Method getCurrentMpPercents.
	 * @return double
	 */
	public final double getCurrentMpPercents()
	{
		return getCurrentMpRatio() * 100.;
	}
	
	/**
	 * Method isCurrentMpFull.
	 * @return boolean
	 */
	public final boolean isCurrentMpFull()
	{
		return getCurrentMp() >= getMaxMp();
	}
	
	/**
	 * Method isCurrentMpZero.
	 * @return boolean
	 */
	public final boolean isCurrentMpZero()
	{
		return getCurrentMp() < 1;
	}
	
	/**
	 * Method getDestination.
	 * @return Location
	 */
	public Location getDestination()
	{
		return destination;
	}
	
	/**
	 * Method getDEX.
	 * @return int
	 */
	public int getDEX()
	{
		return (int) calcStat(Stats.STAT_DEX, _template.getBaseAttr().getDEX(), null, null);
	}
	
	/**
	 * Method getCON.
	 * @return int
	 */
	public int getCON()
	{
		return (int) calcStat(Stats.STAT_CON, _template.getBaseAttr().getCON(), null, null);
	}
	
	/**
	 * Method getINT.
	 * @return int
	 */
	public int getINT()
	{
		return (int) calcStat(Stats.STAT_INT, _template.getBaseAttr().getINT(), null, null);
	}
	
	/**
	 * Method getMEN.
	 * @return int
	 */
	public int getMEN()
	{
		return (int) calcStat(Stats.STAT_MEN, _template.getBaseAttr().getMEN(), null, null);
	}
	
	/**
	 * Method getSTR.
	 * @return int
	 */
	public int getSTR()
	{
		return (int) calcStat(Stats.STAT_STR, _template.getBaseAttr().getSTR(), null, null);
	}
	
	/**
	 * Method getEvasionRate.
	 * @param target Creature
	 * @return int
	 */
	public int getEvasionRate(Creature target)
	{
		return (int) calcStat(Stats.EVASION_RATE, 0, target, null);
	}
	
	/**
	 * Method getMEvasionRate.
	 * @param target Creature
	 * @return int
	 */
	public int getMEvasionRate(Creature target)
	{
		return (int) calcStat(Stats.MEVASION_RATE, 0, target, null);
	}
	
	/**
	 * Method getWIT.
	 * @return int
	 */
	public int getWIT()
	{
		return (int) calcStat(Stats.STAT_WIT, _template.getBaseAttr().getWIT(), null, null);
	}
	
	/**
	 * Method getAroundCharacters.
	 * @param radius int
	 * @param height int
	 * @return List<Creature>
	 */
	public List<Creature> getAroundCharacters(int radius, int height)
	{
		if (!isVisible())
		{
			return Collections.emptyList();
		}
		return World.getAroundCharacters(this, radius, height);
	}
	
	/**
	 * Method getAroundNpc.
	 * @param range int
	 * @param height int
	 * @return List<NpcInstance>
	 */
	public List<NpcInstance> getAroundNpc(int range, int height)
	{
		if (!isVisible())
		{
			return Collections.emptyList();
		}
		return World.getAroundNpc(this, range, height);
	}
	
	/**
	 * Method knowsObject.
	 * @param obj GameObject
	 * @return boolean
	 */
	public boolean knowsObject(GameObject obj)
	{
		return World.getAroundObjectById(this, obj.getObjectId()) != null;
	}
	
	/**
	 * Method getKnownSkill.
	 * @param skillId int
	 * @return Skill
	 */
	public final Skill getKnownSkill(int skillId)
	{
		return _skills.get(skillId);
	}
	
	/**
	 * Method getMagicalAttackRange.
	 * @param skill Skill
	 * @return int
	 */
	public final int getMagicalAttackRange(Skill skill)
	{
		if (skill != null)
		{
			return (int) calcStat(Stats.MAGIC_ATTACK_RANGE, skill.getCastRange(), null, skill);
		}
		return getTemplate().getBaseAtkRange();
	}
	
	/**
	 * Method getMAtk.
	 * @param target Creature
	 * @param skill Skill
	 * @return int
	 */
	public int getMAtk(Creature target, Skill skill)
	{
		if ((skill != null) && (skill.getMatak() > 0))
		{
			return skill.getMatak();
		}
		return (int) calcStat(Stats.MAGIC_ATTACK, _template.getBaseMAtk(), target, skill);
	}
	
	/**
	 * Method getMAtkSpd.
	 * @return int
	 */
	public int getMAtkSpd()
	{
		return (int) (calcStat(Stats.MAGIC_ATTACK_SPEED, _template.getBaseMAtkSpd(), null, null));
	}
	
	/**
	 * Method getMaxCp.
	 * @return int
	 */
	public int getMaxCp()
	{
		return (int) calcStat(Stats.MAX_CP, _template.getBaseCpMax(), null, null);
	}
	
	/**
	 * Method getMaxHp.
	 * @return int
	 */
	public int getMaxHp()
	{
		return (int) calcStat(Stats.MAX_HP, _template.getBaseHpMax(), null, null);
	}
	
	/**
	 * Method getMaxMp.
	 * @return int
	 */
	public int getMaxMp()
	{
		return (int) calcStat(Stats.MAX_MP, _template.getBaseMpMax(), null, null);
	}
	
	/**
	 * Method getMDef.
	 * @param target Creature
	 * @param skill Skill
	 * @return int
	 */
	public int getMDef(Creature target, Skill skill)
	{
		return Math.max((int) calcStat(Stats.MAGIC_DEFENCE, _template.getBaseMDef(), target, skill), 1);
	}
	
	/**
	 * Method getMinDistance.
	 * @param obj GameObject
	 * @return double
	 */
	public double getMinDistance(GameObject obj)
	{
		double distance = getTemplate().getCollisionRadius();
		if ((obj != null) && obj.isCreature())
		{
			distance += ((Creature) obj).getTemplate().getCollisionRadius();
		}
		return distance;
	}
	
	/**
	 * Method getMovementSpeedMultiplier.
	 * @return double
	 */
	public double getMovementSpeedMultiplier()
	{
		return (getRunSpeed() * 1.) / _template.getBaseRunSpd();
	}
	
	/**
	 * Method getMoveSpeed.
	 * @return int
	 */
	@Override
	public int getMoveSpeed()
	{
		if (isRunning())
		{
			return getRunSpeed();
		}
		return getWalkSpeed();
	}
	
	/**
	 * Method getRunSpeed.
	 * @return int
	 */
	public int getRunSpeed()
	{
		if (isInWater())
		{
			return getSwimRunSpeed();
		}
		return getSpeed(_template.getBaseRunSpd());
	}
	
	/**
	 * Method getWalkSpeed.
	 * @return int
	 */
	public int getWalkSpeed()
	{
		if (isInWater())
		{
			return getSwimWalkSpeed();
		}
		return getSpeed(_template.getBaseWalkSpd());
	}
	
	/**
	 * Method getSwimRunSpeed.
	 * @return int
	 */
	public int getSwimRunSpeed()
	{
		return getSpeed(_template.getBaseWaterRunSpd());
	}
	
	/**
	 * Method getSwimWalkSpeed.
	 * @return int
	 */
	public int getSwimWalkSpeed()
	{
		return getSpeed(_template.getBaseWaterWalkSpd());
	}
	
	/**
	 * Method relativeSpeed.
	 * @param target GameObject
	 * @return double
	 */
	public double relativeSpeed(GameObject target)
	{
		return getMoveSpeed() - (target.getMoveSpeed() * Math.cos(headingToRadians(getHeading()) - headingToRadians(target.getHeading())));
	}
	
	/**
	 * Method getSpeed.
	 * @param baseSpeed double
	 * @return int
	 */
	public int getSpeed(double baseSpeed)
	{
		return (int) calcStat(Stats.RUN_SPEED, baseSpeed, null, null);
	}
	
	/**
	 * Method getName.
	 * @return String
	 */
	@Override
	public String getName()
	{
		return StringUtils.defaultString(_name);
	}
	
	/**
	 * Method getPAtk.
	 * @param target Creature
	 * @return int
	 */
	public int getPAtk(Creature target)
	{
		return (int) calcStat(Stats.POWER_ATTACK, _template.getBasePAtk(), target, null);
	}
	
	/**
	 * Method getPAtkSpd.
	 * @return int
	 */
	public int getPAtkSpd()
	{
		return (int) calcStat(Stats.POWER_ATTACK_SPEED, _template.getBasePAtkSpd(), null, null);
	}
	
	/**
	 * Method getPDef.
	 * @param target Creature
	 * @return int
	 */
	public int getPDef(Creature target)
	{
		return (int) calcStat(Stats.POWER_DEFENCE, _template.getBasePDef(), target, null);
	}
	
	/**
	 * Method getPhysicalAttackRange.
	 * @return int
	 */
	public final int getPhysicalAttackRange()
	{
		return (int) calcStat(Stats.POWER_ATTACK_RANGE, getTemplate().getBaseAtkRange(), null, null);
	}
	
	/**
	 * Method getRandomDamage.
	 * @return int
	 */
	public int getRandomDamage()
	{
		WeaponTemplate weaponItem = getActiveWeaponItem();
		if (weaponItem == null)
		{
			return 5 + (int) Math.sqrt(getLevel());
		}
		return weaponItem.getRandomDamage();
	}
	
	/**
	 * Method getReuseModifier.
	 * @param target Creature
	 * @return double
	 */
	public double getReuseModifier(Creature target)
	{
		return calcStat(Stats.ATK_REUSE, 1, target, null);
	}
	
	/**
	 * Method getShldDef.
	 * @return int
	 */
	public final int getShldDef()
	{
		if (isPlayer())
		{
			return (int) calcStat(Stats.SHIELD_DEFENCE, 0, null, null);
		}
		return (int) calcStat(Stats.SHIELD_DEFENCE, _template.getBaseShldDef(), null, null);
	}
	
	/**
	 * Method getSkillDisplayLevel.
	 * @param skillId Integer
	 * @return int
	 */
	public final int getSkillDisplayLevel(Integer skillId)
	{
		Skill skill = _skills.get(skillId);
		if (skill == null)
		{
			return -1;
		}
		return skill.getDisplayLevel();
	}
	
	/**
	 * Method getSkillLevel.
	 * @param skillId Integer
	 * @return int
	 */
	public final int getSkillLevel(Integer skillId)
	{
		switch (skillId)
		{
			case 1566:
			case 1567:
			case 1568:
			case 1569:
				return 1;
		}
		return getSkillLevel(skillId, -1);
	}
	
	/**
	 * Method getSkillLevel.
	 * @param skillId Integer
	 * @param def int
	 * @return int
	 */
	public final int getSkillLevel(Integer skillId, int def)
	{
		Skill skill = _skills.get(skillId);
		if (skill == null)
		{
			return def;
		}
		return skill.getLevel();
	}
	
	/**
	 * Method getSkillMastery.
	 * @param skillId Integer
	 * @return int
	 */
	public int getSkillMastery(Integer skillId)
	{
		if (_skillMastery == null)
		{
			return 0;
		}
		Integer val = _skillMastery.get(skillId);
		return val == null ? 0 : val.intValue();
	}
	
	/**
	 * Method removeSkillMastery.
	 * @param skillId Integer
	 */
	public void removeSkillMastery(Integer skillId)
	{
		if (_skillMastery != null)
		{
			_skillMastery.remove(skillId);
		}
	}
	
	/**
	 * Method getSwimSpeed.
	 * @return int
	 */
	public int getSwimSpeed()
	{
		return (int) calcStat(Stats.RUN_SPEED, Config.SWIMING_SPEED, null, null);
	}
	
	/**
	 * Method getTarget.
	 * @return GameObject
	 */
	public GameObject getTarget()
	{
		return target.get();
	}
	
	/**
	 * Method getTargetId.
	 * @return int
	 */
	public final int getTargetId()
	{
		GameObject target = getTarget();
		return target == null ? -1 : target.getObjectId();
	}
	
	/**
	 * Method getTemplate.
	 * @return CharTemplate
	 */
	public CharTemplate getTemplate()
	{
		return _template;
	}
	
	/**
	 * Method getTitle.
	 * @return String
	 */
	public String getTitle()
	{
		return StringUtils.defaultString(_title);
	}
	
	/**
	 * Method headingToRadians.
	 * @param heading int
	 * @return double
	 */
	public double headingToRadians(int heading)
	{
		return (heading - 32768) / HEADINGS_IN_PI;
	}
	
	/**
	 * Method isAlikeDead.
	 * @return boolean
	 */
	public boolean isAlikeDead()
	{
		return _fakeDeath || isDead();
	}
	
	/**
	 * Method isAttackingNow.
	 * @return boolean
	 */
	public final boolean isAttackingNow()
	{
		return _attackEndTime > System.currentTimeMillis();
	}
	
	/**
	 * Method isBlessedByNoblesse.
	 * @return boolean
	 */
	public final boolean isBlessedByNoblesse()
	{
		return _isBlessedByNoblesse;
	}
	
	/**
	 * Method isSalvation.
	 * @return boolean
	 */
	public final boolean isSalvation()
	{
		return _isSalvation;
	}
	
	/**
	 * Method isEffectImmune.
	 * @return boolean
	 */
	public boolean isEffectImmune()
	{
		return _effectImmunity.get();
	}
	
	/**
	 * Method isBuffImmune.
	 * @return boolean
	 */
	public boolean isBuffImmune()
	{
		return _buffImmunity.get();
	}
	
	/**
	 * Method isDebuffImmune.
	 * @return boolean
	 */
	public boolean isDebuffImmune()
	{
		return _debuffImmunity.get();
	}
	
	/**
	 * Method isDead.
	 * @return boolean
	 */
	public boolean isDead()
	{
		return (_currentHp < 0.5) || isDead.get();
	}
	
	/**
	 * Method isFlying.
	 * @return boolean
	 */
	@Override
	public final boolean isFlying()
	{
		return _flying;
	}
	
	/**
	 * Method isInCombat.
	 * @return boolean
	 */
	public final boolean isInCombat()
	{
		return System.currentTimeMillis() < _stanceEndTime;
	}
	
	/**
	 * Method isInvul.
	 * @return boolean
	 */
	public boolean isInvul()
	{
		return _isInvul;
	}
	
	/**
	 * Method isMageClass.
	 * @return boolean
	 */
	public boolean isMageClass()
	{
		return getTemplate().getBaseMAtk() > 3;
	}
	
	/**
	 * Method isRunning.
	 * @return boolean
	 */
	public final boolean isRunning()
	{
		return _running;
	}
	
	/**
	 * Method isSkillDisabled.
	 * @param skill Skill
	 * @return boolean
	 */
	public boolean isSkillDisabled(Skill skill)
	{
		TimeStamp sts = _skillReuses.get(skill.hashCode());
		if (sts == null)
		{
			return false;
		}
		if (sts.hasNotPassed())
		{
			return true;
		}
		_skillReuses.remove(skill.hashCode());
		return false;
	}
	
	/**
	 * Method isTeleporting.
	 * @return boolean
	 */
	public final boolean isTeleporting()
	{
		return isTeleporting.get();
	}
	
	/**
	 * Method getIntersectionPoint.
	 * @param target Creature
	 * @return Location
	 */
	public Location getIntersectionPoint(Creature target)
	{
		if (!PositionUtils.isFacing(this, target, 90))
		{
			return new Location(target.getX(), target.getY(), target.getZ());
		}
		double angle = PositionUtils.convertHeadingToDegree(target.getHeading());
		double radian = Math.toRadians(angle - 90);
		double range = target.getMoveSpeed() / 2;
		return new Location((int) (target.getX() - (range * Math.sin(radian))), (int) (target.getY() + (range * Math.cos(radian))), target.getZ());
	}
	
	/**
	 * Method applyOffset.
	 * @param point Location
	 * @param offset int
	 * @return Location
	 */
	public Location applyOffset(Location point, int offset)
	{
		if (offset <= 0)
		{
			return point;
		}
		long dx = point.x - getX();
		long dy = point.y - getY();
		long dz = point.z - getZ();
		double distance = Math.sqrt((dx * dx) + (dy * dy) + (dz * dz));
		if (distance <= offset)
		{
			point.set(getX(), getY(), getZ());
			return point;
		}
		if (distance >= 1)
		{
			double cut = offset / distance;
			point.x -= (int) ((dx * cut) + 0.5);
			point.y -= (int) ((dy * cut) + 0.5);
			point.z -= (int) ((dz * cut) + 0.5);
			if (!isFlying() && !isInBoat() && !isInWater() && !isBoat())
			{
				point.correctGeoZ();
			}
		}
		return point;
	}
	
	/**
	 * Method applyOffset.
	 * @param points List<Location>
	 * @param offset int
	 * @return List<Location>
	 */
	public List<Location> applyOffset(List<Location> points, int offset)
	{
		offset = offset >> 4;
		if (offset <= 0)
		{
			return points;
		}
		long dx = points.get(points.size() - 1).x - points.get(0).x;
		long dy = points.get(points.size() - 1).y - points.get(0).y;
		long dz = points.get(points.size() - 1).z - points.get(0).z;
		double distance = Math.sqrt((dx * dx) + (dy * dy) + (dz * dz));
		if (distance <= offset)
		{
			Location point = points.get(0);
			points.clear();
			points.add(point);
			return points;
		}
		if (distance >= 1)
		{
			double cut = offset / distance;
			int num = (int) ((points.size() * cut) + 0.5);
			for (int i = 1; (i <= num) && (points.size() > 0); i++)
			{
				points.remove(points.size() - 1);
			}
		}
		return points;
	}
	
	/**
	 * Method setSimplePath.
	 * @param dest Location
	 * @return boolean
	 */
	private boolean setSimplePath(Location dest)
	{
		List<Location> moveList = GeoMove.constructMoveList(getLoc(), dest);
		if (moveList.isEmpty())
		{
			return false;
		}
		_targetRecorder.clear();
		_targetRecorder.add(moveList);
		return true;
	}
	
	/**
	 * Method buildPathTo.
	 * @param x int
	 * @param y int
	 * @param z int
	 * @param offset int
	 * @param pathFind boolean
	 * @return boolean
	 */
	private boolean buildPathTo(int x, int y, int z, int offset, boolean pathFind)
	{
		return buildPathTo(x, y, z, offset, null, false, pathFind);
	}
	
	/**
	 * Method buildPathTo.
	 * @param x int
	 * @param y int
	 * @param z int
	 * @param offset int
	 * @param follow Creature
	 * @param forestalling boolean
	 * @param pathFind boolean
	 * @return boolean
	 */
	boolean buildPathTo(int x, int y, int z, int offset, Creature follow, boolean forestalling, boolean pathFind)
	{
		int geoIndex = getGeoIndex();
		Location dest;
		if (forestalling && (follow != null) && follow.isMoving)
		{
			dest = getIntersectionPoint(follow);
		}
		else
		{
			dest = new Location(x, y, z);
		}
		if (isInBoat() || isBoat() || !Config.ALLOW_GEODATA)
		{
			applyOffset(dest, offset);
			return setSimplePath(dest);
		}
		if (isFlying() || isInWater())
		{
			applyOffset(dest, offset);
			Location nextloc;
			if (isFlying())
			{
				if (GeoEngine.canSeeCoord(this, dest.x, dest.y, dest.z, true))
				{
					return setSimplePath(dest);
				}
				nextloc = GeoEngine.moveCheckInAir(getX(), getY(), getZ(), dest.x, dest.y, dest.z, getColRadius(), geoIndex);
				if ((nextloc != null) && !nextloc.equals(getX(), getY(), getZ()))
				{
					return setSimplePath(nextloc);
				}
			}
			else
			{
				int waterZ = getWaterZ();
				nextloc = GeoEngine.moveInWaterCheck(getX(), getY(), getZ(), dest.x, dest.y, dest.z, waterZ, geoIndex);
				if (nextloc == null)
				{
					return false;
				}
				List<Location> moveList = GeoMove.constructMoveList(getLoc(), nextloc.clone());
				_targetRecorder.clear();
				if (!moveList.isEmpty())
				{
					_targetRecorder.add(moveList);
				}
				int dz = dest.z - nextloc.z;
				if ((dz > 0) && (dz < 128))
				{
					moveList = GeoEngine.MoveList(nextloc.x, nextloc.y, nextloc.z, dest.x, dest.y, geoIndex, false);
					if (moveList != null)
					{
						if (!moveList.isEmpty())
						{
							_targetRecorder.add(moveList);
						}
					}
				}
				return !_targetRecorder.isEmpty();
			}
			return false;
		}
		List<Location> moveList = GeoEngine.MoveList(getX(), getY(), getZ(), dest.x, dest.y, geoIndex, true);
		if (moveList != null)
		{
			if (moveList.isEmpty())
			{
				return false;
			}
			applyOffset(moveList, offset);
			if (moveList.isEmpty())
			{
				return false;
			}
			_targetRecorder.clear();
			_targetRecorder.add(moveList);
			return true;
		}
		if (pathFind)
		{
			List<List<Location>> targets = GeoMove.findMovePath(getX(), getY(), getZ(), dest.clone(), this, true, geoIndex);
			if (!targets.isEmpty())
			{
				moveList = targets.remove(targets.size() - 1);
				applyOffset(moveList, offset);
				if (!moveList.isEmpty())
				{
					targets.add(moveList);
				}
				if (!targets.isEmpty())
				{
					_targetRecorder.clear();
					_targetRecorder.addAll(targets);
					return true;
				}
			}
		}
		if (follow != null)
		{
			return false;
		}
		applyOffset(dest, offset);
		moveList = GeoEngine.MoveList(getX(), getY(), getZ(), dest.x, dest.y, geoIndex, false);
		if ((moveList != null) && !moveList.isEmpty())
		{
			_targetRecorder.clear();
			_targetRecorder.add(moveList);
			return true;
		}
		return false;
	}
	
	/**
	 * Method getFollowTarget.
	 * @return Creature
	 */
	public Creature getFollowTarget()
	{
		return followTarget.get();
	}
	
	/**
	 * Method setFollowTarget.
	 * @param target Creature
	 */
	public void setFollowTarget(Creature target)
	{
		followTarget = target == null ? HardReferences.<Creature> emptyRef() : target.getRef();
	}
	
	/**
	 * Method followToCharacter.
	 * @param target Creature
	 * @param offset int
	 * @param forestalling boolean
	 * @return boolean
	 */
	public boolean followToCharacter(Creature target, int offset, boolean forestalling)
	{
		return followToCharacter(target.getLoc(), target, offset, forestalling);
	}
	
	/**
	 * Method followToCharacter.
	 * @param loc Location
	 * @param target Creature
	 * @param offset int
	 * @param forestalling boolean
	 * @return boolean
	 */
	public boolean followToCharacter(Location loc, Creature target, int offset, boolean forestalling)
	{
		moveLock.lock();
		try
		{
			if (isMovementDisabled() || (target == null) || (isInBoat() && !isInShuttle()) || isInWater())
			{
				return false;
			}
			offset = Math.max(offset, 10);
			if (isFollow && (target == getFollowTarget()) && (offset == _offset))
			{
				return true;
			}
			if ((Math.abs(getZ() - target.getZ()) > 1000) && !isFlying())
			{
				sendPacket(SystemMsg.CANNOT_SEE_TARGET);
				return false;
			}
			getAI().clearNextAction();
			stopMove(false, false);
			if (buildPathTo(loc.x, loc.y, loc.z, offset, target, forestalling, !target.isDoor()))
			{
				movingDestTempPos.set(loc.x, loc.y, loc.z);
			}
			else
			{
				return false;
			}
			isMoving = true;
			isFollow = true;
			_forestalling = forestalling;
			_offset = offset;
			setFollowTarget(target);
			moveNext(true);
			return true;
		}
		finally
		{
			moveLock.unlock();
		}
	}
	
	/**
	 * Method moveToLocation.
	 * @param loc Location
	 * @param offset int
	 * @param pathfinding boolean
	 * @return boolean
	 */
	public boolean moveToLocation(Location loc, int offset, boolean pathfinding)
	{
		return moveToLocation(loc.x, loc.y, loc.z, offset, pathfinding);
	}
	
	/**
	 * Method moveToLocation.
	 * @param x_dest int
	 * @param y_dest int
	 * @param z_dest int
	 * @param offset int
	 * @param pathfinding boolean
	 * @return boolean
	 */
	public boolean moveToLocation(int x_dest, int y_dest, int z_dest, int offset, boolean pathfinding)
	{
		moveLock.lock();
		try
		{
			offset = Math.max(offset, 0);
			Location dst_geoloc = new Location(x_dest, y_dest, z_dest).world2geo();
			if (isMoving && !isFollow && movingDestTempPos.equals(dst_geoloc))
			{
				sendActionFailed();
				return true;
			}
			if (isMovementDisabled())
			{
				getAI().setNextAction(nextAction.MOVE, new Location(x_dest, y_dest, z_dest), offset, pathfinding, false);
				sendActionFailed();
				return false;
			}
			getAI().clearNextAction();
			if (isPlayer())
			{
				getAI().changeIntention(AI_INTENTION_ACTIVE, null, null);
			}
			stopMove(false, false);
			if (buildPathTo(x_dest, y_dest, z_dest, offset, pathfinding))
			{
				movingDestTempPos.set(dst_geoloc);
			}
			else
			{
				sendActionFailed();
				return false;
			}
			isMoving = true;
			moveNext(true);
			return true;
		}
		finally
		{
			moveLock.unlock();
		}
	}
	
	/**
	 * Method moveNext.
	 * @param firstMove boolean
	 */
	void moveNext(boolean firstMove)
	{
		if (!isMoving || isMovementDisabled())
		{
			stopMove();
			return;
		}
		_previousSpeed = getMoveSpeed();
		if (_previousSpeed <= 0)
		{
			stopMove();
			return;
		}
		if (!firstMove)
		{
			Location dest = destination;
			if (dest != null)
			{
				setLoc(dest, true);
			}
		}
		if (_targetRecorder.isEmpty())
		{
			CtrlEvent ctrlEvent = isFollow ? CtrlEvent.EVT_ARRIVED_TARGET : CtrlEvent.EVT_ARRIVED;
			stopMove(false, true);
			ThreadPoolManager.getInstance().execute(new NotifyAITask(this, ctrlEvent));
			return;
		}
		moveList = _targetRecorder.remove(0);
		Location begin = moveList.get(0).clone().geo2world();
		Location end = moveList.get(moveList.size() - 1).clone().geo2world();
		destination = end;
		double distance = (isFlying() || isInWater()) ? begin.distance3D(end) : begin.distance(end);
		if (distance != 0)
		{
			setHeading(PositionUtils.calculateHeadingFrom(getX(), getY(), destination.x, destination.y));
		}
		broadcastMove();
		_startMoveTime = _followTimestamp = System.currentTimeMillis();
		if (_moveTaskRunnable == null)
		{
			_moveTaskRunnable = new MoveNextTask();
		}
		_moveTask = ThreadPoolManager.getInstance().schedule(_moveTaskRunnable.setDist(distance), getMoveTickInterval());
	}
	
	/**
	 * Method getMoveTickInterval.
	 * @return int
	 */
	public int getMoveTickInterval()
	{
		return (isPlayer() ? 16000 : 32000) / Math.max(getMoveSpeed(), 1);
	}
	
	/**
	 * Method broadcastMove.
	 */
	private void broadcastMove()
	{
		validateLocation(isPlayer() ? 2 : 1);
		broadcastPacket(movePacket());
	}
	
	/**
	 * Method stopMove.
	 */
	public void stopMove()
	{
		stopMove(true, true);
	}
	
	/**
	 * Method stopMove.
	 * @param validate boolean
	 */
	public void stopMove(boolean validate)
	{
		stopMove(true, validate);
	}
	
	/**
	 * Method stopMove.
	 * @param stop boolean
	 * @param validate boolean
	 */
	public void stopMove(boolean stop, boolean validate)
	{
		if (!isMoving)
		{
			return;
		}
		moveLock.lock();
		try
		{
			if (!isMoving)
			{
				return;
			}
			isMoving = false;
			isFollow = false;
			if (_moveTask != null)
			{
				_moveTask.cancel(false);
				_moveTask = null;
			}
			destination = null;
			moveList = null;
			_targetRecorder.clear();
			if (validate)
			{
				validateLocation(isPlayer() ? 2 : 1);
			}
			if (stop)
			{
				broadcastPacket(stopMovePacket());
			}
		}
		finally
		{
			moveLock.unlock();
		}
	}
	
	/**
	 * Method getWaterZ.
	 * @return int
	 */
	public int getWaterZ()
	{
		if (!isInWater())
		{
			return Integer.MIN_VALUE;
		}
		int waterZ = Integer.MIN_VALUE;
		zonesRead.lock();
		try
		{
			Zone zone;
			for (int i = 0; i < _zones.size(); i++)
			{
				zone = _zones.get(i);
				if (zone.getType() == ZoneType.water)
				{
					if ((waterZ == Integer.MIN_VALUE) || (waterZ < zone.getTerritory().getZmax()))
					{
						waterZ = zone.getTerritory().getZmax();
					}
				}
			}
		}
		finally
		{
			zonesRead.unlock();
		}
		return waterZ;
	}
	
	/**
	 * Method stopMovePacket.
	 * @return L2GameServerPacket
	 */
	protected L2GameServerPacket stopMovePacket()
	{
		return new StopMove(this);
	}
	
	/**
	 * Method movePacket.
	 * @return L2GameServerPacket
	 */
	public L2GameServerPacket movePacket()
	{
		return new CharMoveToLocation(this);
	}
	
	/**
	 * Method updateZones.
	 */
	public void updateZones()
	{
		if (isInObserverMode())
		{
			return;
		}
		Zone[] zones = isVisible() ? getCurrentRegion().getZones() : Zone.EMPTY_L2ZONE_ARRAY;
		LazyArrayList<Zone> entering = null;
		LazyArrayList<Zone> leaving = null;
		Zone zone;
		zonesWrite.lock();
		try
		{
			if (!_zones.isEmpty())
			{
				leaving = LazyArrayList.newInstance();
				for (int i = 0; i < _zones.size(); i++)
				{
					zone = _zones.get(i);
					if (!ArrayUtils.contains(zones, zone) || !zone.checkIfInZone(getX(), getY(), getZ(), getReflection()))
					{
						leaving.add(zone);
					}
				}
				if (!leaving.isEmpty())
				{
					for (int i = 0; i < leaving.size(); i++)
					{
						zone = leaving.get(i);
						_zones.remove(zone);
					}
				}
			}
			if (zones.length > 0)
			{
				entering = LazyArrayList.newInstance();
				for (Zone zone2 : zones)
				{
					zone = zone2;
					if (!_zones.contains(zone) && zone.checkIfInZone(getX(), getY(), getZ(), getReflection()))
					{
						entering.add(zone);
					}
				}
				if (!entering.isEmpty())
				{
					for (int i = 0; i < entering.size(); i++)
					{
						zone = entering.get(i);
						_zones.add(zone);
					}
				}
			}
		}
		finally
		{
			zonesWrite.unlock();
		}
		onUpdateZones(leaving, entering);
		if (leaving != null)
		{
			LazyArrayList.recycle(leaving);
		}
		if (entering != null)
		{
			LazyArrayList.recycle(entering);
		}
	}
	
	/**
	 * Method onUpdateZones.
	 * @param leaving List<Zone>
	 * @param entering List<Zone>
	 */
	protected void onUpdateZones(List<Zone> leaving, List<Zone> entering)
	{
		Zone zone;
		if ((leaving != null) && !leaving.isEmpty())
		{
			for (int i = 0; i < leaving.size(); i++)
			{
				zone = leaving.get(i);
				zone.doLeave(this);
			}
		}
		if ((entering != null) && !entering.isEmpty())
		{
			for (int i = 0; i < entering.size(); i++)
			{
				zone = entering.get(i);
				zone.doEnter(this);
			}
		}
	}
	
	/**
	 * Method isInZonePeace.
	 * @return boolean
	 */
	public boolean isInZonePeace()
	{
		return isInZone(ZoneType.peace_zone) && !isInZoneBattle();
	}
	
	/**
	 * Method isInZoneBattle.
	 * @return boolean
	 */
	public boolean isInZoneBattle()
	{
		return isInZone(ZoneType.battle_zone);
	}
	
	/**
	 * Method isInWater.
	 * @return boolean
	 */
	public boolean isInWater()
	{
		return isInZone(ZoneType.water) && !(isInBoat() || isBoat() || isFlying());
	}
	
	/**
	 * Method isInZone.
	 * @param type ZoneType
	 * @return boolean
	 */
	public boolean isInZone(ZoneType type)
	{
		zonesRead.lock();
		try
		{
			Zone zone;
			for (Zone _zone : _zones)
			{
				zone = _zone;
				if (zone.getType() == type)
				{
					return true;
				}
			}
		}
		finally
		{
			zonesRead.unlock();
		}
		return false;
	}
	
	/**
	 * Method isInZone.
	 * @param name String
	 * @return boolean
	 */
	public boolean isInZone(String name)
	{
		zonesRead.lock();
		try
		{
			Zone zone;
			for (int i = 0; i < _zones.size(); i++)
			{
				zone = _zones.get(i);
				if (zone.getName().equals(name))
				{
					return true;
				}
			}
		}
		finally
		{
			zonesRead.unlock();
		}
		return false;
	}
	
	/**
	 * Method isInZone.
	 * @param zone Zone
	 * @return boolean
	 */
	public boolean isInZone(Zone zone)
	{
		zonesRead.lock();
		try
		{
			return _zones.contains(zone);
		}
		finally
		{
			zonesRead.unlock();
		}
	}
	
	/**
	 * Method getZone.
	 * @param type ZoneType
	 * @return Zone
	 */
	public Zone getZone(ZoneType type)
	{
		zonesRead.lock();
		try
		{
			Zone zone;
			for (int i = 0; i < _zones.size(); i++)
			{
				zone = _zones.get(i);
				if (zone.getType() == type)
				{
					return zone;
				}
			}
		}
		finally
		{
			zonesRead.unlock();
		}
		return null;
	}
	
	/**
	 * Method getRestartPoint.
	 * @return Location
	 */
	public Location getRestartPoint()
	{
		zonesRead.lock();
		try
		{
			Zone zone;
			for (int i = 0; i < _zones.size(); i++)
			{
				zone = _zones.get(i);
				if (zone.getRestartPoints() != null)
				{
					ZoneType type = zone.getType();
					if ((type == ZoneType.battle_zone) || (type == ZoneType.peace_zone) || (type == ZoneType.offshore) || (type == ZoneType.dummy))
					{
						return zone.getSpawn();
					}
				}
			}
		}
		finally
		{
			zonesRead.unlock();
		}
		return null;
	}
	
	/**
	 * Method getPKRestartPoint.
	 * @return Location
	 */
	public Location getPKRestartPoint()
	{
		zonesRead.lock();
		try
		{
			Zone zone;
			for (int i = 0; i < _zones.size(); i++)
			{
				zone = _zones.get(i);
				if (zone.getRestartPoints() != null)
				{
					ZoneType type = zone.getType();
					if ((type == ZoneType.battle_zone) || (type == ZoneType.peace_zone) || (type == ZoneType.offshore) || (type == ZoneType.dummy))
					{
						return zone.getPKSpawn();
					}
				}
			}
		}
		finally
		{
			zonesRead.unlock();
		}
		return null;
	}
	
	/**
	 * Method getGeoZ.
	 * @param loc Location
	 * @return int
	 */
	@Override
	public int getGeoZ(Location loc)
	{
		if (isFlying() || isInWater() || isInBoat() || isBoat() || isDoor())
		{
			return loc.z;
		}
		return super.getGeoZ(loc);
	}
	
	/**
	 * Method needStatusUpdate.
	 * @return boolean
	 */
	protected boolean needStatusUpdate()
	{
		if(!isVisible() || !displayHpBar())
		{
			return false;
		}
		
		boolean result = false;
		int bar;
		
		bar = (int) ((getCurrentHp() * CLIENT_BAR_SIZE) / getMaxHp());
		
		if ((bar == 0) || (bar != _lastHpBarUpdate))
		{
			_lastHpBarUpdate = bar;
			result = true;
		}
		
		bar = (int) ((getCurrentMp() * CLIENT_BAR_SIZE) / getMaxMp());
		
		if ((bar == 0) || (bar != _lastMpBarUpdate))
		{
			_lastMpBarUpdate = bar;
			result = true;
		}
		
		if (isPlayer())
		{
			bar = (int) ((getCurrentCp() * CLIENT_BAR_SIZE) / getMaxCp());

			if ((bar == 0) || (bar != _lastCpBarUpdate))
			{
				_lastCpBarUpdate = bar;
				result = true;
			}
		}
		return result;
	}
	
	/**
	 * Method onForcedAttack.
	 * @param player Player
	 * @param shift boolean
	 */
	@Override
	public void onForcedAttack(Player player, boolean shift)
	{
		if(player.getTarget() != this)
		{
			player.setTarget(this);
		}

		if(!isAttackable(player) || player.isConfused() || player.isBlocked())
		{
			player.sendActionFailed();
			return;
		}

		player.getAI().Attack(this, true, shift);
	}
	
	/**
	 * Method onHitTimer.
	 * @param target Creature
	 * @param damage int
	 * @param reflectableDamage int
	 * @param crit boolean
	 * @param miss boolean
	 * @param soulshot boolean
	 * @param shld boolean
	 * @param unchargeSS boolean
	 */
	public void onHitTimer(Creature target, int damage, int reflectableDamage, boolean crit, boolean miss, boolean soulshot, boolean shld, boolean unchargeSS)
	{
		if (isAlikeDead())
		{
			sendActionFailed();
			return;
		}
		if (target.isDead() || !isInRange(target, 2000))
		{
			sendActionFailed();
			return;
		}
		if (isPlayable() && target.isPlayable() && (isInZoneBattle() != target.isInZoneBattle()))
		{
			Player player = getPlayer();
			if (player != null)
			{
				player.sendPacket(Msg.INVALID_TARGET);
				player.sendActionFailed();
			}
			return;
		}
		target.getListeners().onAttackHit(this);
		if (!miss && target.isPlayer() && (isCursedWeaponEquipped() || ((getActiveWeaponInstance() != null) && getActiveWeaponInstance().isHeroWeapon() && target.isCursedWeaponEquipped())))
		{
			target.setCurrentCp(0);
		}
		if (target.isStunned() && Formulas.calcStunBreak(crit))
		{
			target.getEffectList().stopEffects(EffectType.Stun);
		}
		if(target.getEffectList().getEffectByType(EffectType.DispelOnHit) != null && !miss)
		{
			target.getEffectList().getEffectByType(EffectType.DispelOnHit).onActionTime();
		}
		displayGiveDamageMessage(target, damage, crit, miss, shld, false);
		ThreadPoolManager.getInstance().execute(new NotifyAITask(target, CtrlEvent.EVT_ATTACKED, this, damage));
		boolean checkPvP = checkPvP(target, null);
		if (!miss && (damage > 0))
		{
			target.reduceCurrentHp(damage, reflectableDamage, this, null, true, true, false, true, false, false, true);
			if (!target.isDead())
			{
				if (crit)
				{
					useTriggers(target, TriggerType.CRIT, null, null, damage);
				}
				useTriggers(target, TriggerType.ATTACK, null, null, damage);
				if (Formulas.calcCastBreak(target, crit))
				{
					target.abortCast(false, true);
				}
			}
			if (soulshot && unchargeSS)
			{
				unChargeShots(false);
			}
		}
		if (miss)
		{
			target.useTriggers(this, TriggerType.UNDER_MISSED_ATTACK, null, null, damage);
		}
		startAttackStanceTask();
		if (checkPvP)
		{
			startPvPFlag(target);
		}
	}
	
	/**
	 * Method onMagicUseTimer.
	 * @param aimingTarget Creature
	 * @param skill Skill
	 * @param forceUse boolean
	 */
	public void onMagicUseTimer(Creature aimingTarget, Skill skill, boolean forceUse)
	{
		if (skill == null)
		{
			onCastEndTime(false);
			sendPacket(ActionFail.STATIC);
			return;
		}
		_castInterruptTime = 0;
		if (skill.isUsingWhileCasting())
		{
			aimingTarget.getEffectList().stopEffect(skill.getId());
			onCastEndTime(false);
			return;
		}
		if (!skill.isOffensive() && (getAggressionTarget() != null))
		{
			forceUse = true;
		}
		if (!skill.checkCondition(this, aimingTarget, forceUse, false, false))
		{
			if ((skill.getSkillType() == SkillType.PET_SUMMON) && isPlayer())
			{
				getPlayer().setPetControlItem(null);
			}
			onCastEndTime(false);
			return;
		}
		if ((skill.getCastRange() < 32767) && (skill.getSkillType() != SkillType.TAKECASTLE) && (skill.getSkillType() != SkillType.TAKEFORTRESS) && !GeoEngine.canSeeTarget(this, aimingTarget, isFlying()))
		{
			sendPacket(SystemMsg.CANNOT_SEE_TARGET);
			broadcastPacket(new MagicSkillCanceled(getObjectId()));
			onCastEndTime(false);
			return;
		}
		List<Creature> targets = skill.getTargets(this, aimingTarget, forceUse);
		int hpConsume = skill.getHpConsume();
		if (hpConsume > 0)
		{
			setCurrentHp(Math.max(0, _currentHp - hpConsume), false);
		}
		double mpConsume2 = skill.getMpConsume2();
		if (mpConsume2 > 0)
		{
			if (skill.isMusic())
			{
				double inc = mpConsume2 / 2;
				double add = 0;
				for (Effect e : getEffectList().getAllEffects())
				{
					if ((e.getSkill().getId() != skill.getId()) && e.getSkill().isMusic() && (e.getTimeLeft() > 30))
					{
						add += inc;
					}
				}
				mpConsume2 += add;
				mpConsume2 = calcStat(Stats.MP_DANCE_SKILL_CONSUME, mpConsume2, aimingTarget, skill);
			}
			else if (skill.isMagic())
			{
				mpConsume2 = calcStat(Stats.MP_MAGIC_SKILL_CONSUME, mpConsume2, aimingTarget, skill);
			}
			else
			{
				mpConsume2 = calcStat(Stats.MP_PHYSICAL_SKILL_CONSUME, mpConsume2, aimingTarget, skill);
			}
			if ((_currentMp < mpConsume2) && isPlayable())
			{
				sendPacket(Msg.NOT_ENOUGH_MP);
				onCastEndTime(false);
				return;
			}
			reduceCurrentMp(mpConsume2, null);
		}
		callSkill(skill, targets, true);
		if (skill.getNumCharges() > 0)
		{
			setIncreasedForce(getIncreasedForce() - skill.getNumCharges());
		}
		if (skill.getMaxCharges() > 0)
		{
			setIncreasedForce(getIncreasedForce() - skill.getMaxCharges());
		}
		if (skill.isSoulBoost())
		{
			setConsumedSouls(getConsumedSouls() - Math.min(getConsumedSouls(), 5), null);
		}
		else if (skill.getSoulsConsume() > 0)
		{
			setConsumedSouls(getConsumedSouls() - skill.getSoulsConsume(), null);
		}
		switch (skill.getFlyType())
		{
			// TARGETS FLYTYPE
			case THROW_UP:
			case THROW_HORIZONTAL:
			case PUSH_HORIZONTAL:
			case PUSH_DOWN_HORIZONTAL:
				Location flyLoc;
				for (Creature target : targets)
				{
					flyLoc = target.getFlyLocation(null, skill);
					if (flyLoc == null)
						_log.warn(skill.getFlyType() + " have null flyLoc.");
					target.setLoc(flyLoc);
					broadcastPacket(new FlyToLocation(target, flyLoc, skill.getFlyType(), 0));
				}
				break;
			// CASTER FLYTYPE
			case CHARGE:
				Location flyLocCharge;
				for(Creature target : targets)
				{
					double radian = PositionUtils.convertHeadingToRadian(this.getHeading());
					flyLocCharge = target.getLoc();
					flyLocCharge.set(flyLocCharge.getX() + (int) (Math.sin(radian) * 40), flyLocCharge.getY() - (int) (Math.cos(radian) * 40), flyLocCharge.getZ());
					setLoc(flyLocCharge);
					broadcastPacket(new FlyToLocation(this, flyLocCharge, skill.getFlyType(), 0));
				}
				break;
			case DUMMY:
			case WARP_BACK:
			case WARP_FORWARD:
				flyLoc = getFlyLocation(null, skill);
				if (flyLoc != null)
				{
					setLoc(flyLoc);
					broadcastPacket(new FlyToLocation(this, flyLoc, skill.getFlyType(), 0));
				}
				else
				{
					sendPacket(SystemMsg.CANNOT_SEE_TARGET);
				}
				break;
			default:
				break;
		}
		if (_scheduledCastCount > 0)
		{
			_scheduledCastCount--;
			if (!isDoubleCastingNow() && IsEnabledDoubleCast())
			{
				_skillDoubleLaunchedTask = ThreadPoolManager.getInstance().schedule(new MagicLaunchedTask(this, forceUse), _scheduledCastInterval);
				_skillDoubleTask = ThreadPoolManager.getInstance().schedule(new MagicUseTask(this, forceUse), _scheduledCastInterval);
			}
			else
			{
				_skillLaunchedTask = ThreadPoolManager.getInstance().schedule(new MagicLaunchedTask(this, forceUse), _scheduledCastInterval);
				_skillTask = ThreadPoolManager.getInstance().schedule(new MagicUseTask(this, forceUse), _scheduledCastInterval);
			}
			return;
		}
		int skillCoolTime = 0;
		int chargeAddition = 0;
		if(skill.getFlyType() == FlyType.CHARGE)
			chargeAddition = skill.getHitTime();
		if(!skill.isSkillTimePermanent())
			skillCoolTime = Formulas.calcMAtkSpd(this, skill, skill.getCoolTime() + chargeAddition);
		else				
			skillCoolTime = skill.getCoolTime() + chargeAddition;
		if (skillCoolTime > 0)
		{
			ThreadPoolManager.getInstance().schedule(new CastEndTimeTask(this), skillCoolTime);
		}
		else
		{
			onCastEndTime(true);
		}
	}
	
	/**
	 * Method onCastEndTime.
	 * @param success boolean
	 */
	public void onCastEndTime(boolean success)
	{
		finishFly();
		int skillId = 0;
		if (getCastingSkill() != null)
		{
			skillId = getCastingSkill().getId();
			// TODO: CHAIN SKILL MINOR FIX
			if(getCastingSkill().isAlterSkill())
			{
				if(getCastingTarget().isAirBinded())
				{
					getCastingTarget().getEffectList().stopEffects(EffectType.HellBinding);
				}
				else if(getCastingTarget().isKnockedDown())
				{
					getCastingTarget().getEffectList().stopEffects(EffectType.KnockDown);
				}
			}
			//----------------
		}
		clearCastVars();
		getAI().notifyEvent(CtrlEvent.EVT_FINISH_CASTING, skillId, success);
	}
	
	/**
	 * Method clearCastVars.
	 */
	public void clearCastVars()
	{
		_animationEndTime = 0;
		_castInterruptTime = 0;
		_scheduledCastCount = 0;
		_castingSkill = null;
		_skillTask = null;
		_skillLaunchedTask = null;
		_skillDoubleTask = null;
		_skillDoubleLaunchedTask = null;
		_flyLoc = null;
	}
	
	/**
	 * Method finishFly.
	 */
	private void finishFly()
	{
		Location flyLoc = _flyLoc;
		_flyLoc = null;
		if (flyLoc != null)
		{
			setLoc(flyLoc);
			validateLocation(1);
		}
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
		if (isDeathImmune())
		{
			damage = Math.min(damage, Math.floor(getCurrentHp() - 1));
		}
		double damageLimit = calcStat(Stats.RECEIVE_DAMAGE_LIMIT, damage);
		if (damageLimit >= 0.)
		{
			damage = damageLimit;
		}
		if (canReflect)
		{
			if (attacker.absorbAndReflect(this, skill, reflectableDamage))
			{
				return;
			}
			damage = absorbToEffector(attacker, damage);
			damage = absorbToMp(attacker, damage);
			damage = absorbToSummon(attacker, damage);
		}
		getListeners().onCurrentHpDamage(damage, attacker, skill);
		if (attacker != this)
		{
			if (sendMessage)
			{
				displayReceiveDamageMessage(attacker, (int) damage);
			}
			if (!isDot)
			{
				useTriggers(attacker, TriggerType.RECEIVE_DAMAGE, null, null, damage);
			}
		}
		onReduceCurrentHp(damage, attacker, skill, awake, standUp, directHp);

		if (attacker.isPlayer())
		{
			WorldStatisticsManager.getInstance().updateStat(attacker.getPlayer(), CategoryType.DAMAGE_TO_MONSTERS, attacker.getPlayer().getClassId().getId(), (long) damage);
			WorldStatisticsManager.getInstance().updateStat(attacker.getPlayer(), CategoryType.DAMAGE_TO_MONSTERS_MAX, attacker.getPlayer().getClassId().getId(), (long) damage);
		}
	}
	
	/**
	 * Method onReduceCurrentHp.
	 * @param damage double
	 * @param attacker Creature
	 * @param skill Skill
	 * @param awake boolean
	 * @param standUp boolean
	 * @param directHp boolean
	 */
	protected void onReduceCurrentHp(final double damage, Creature attacker, Skill skill, boolean awake, boolean standUp, boolean directHp)
	{
		if (getTransformation() != 0)
		{
			List<Effect> effects = getEffectList().getAllEffects();
			for (Effect effect : effects)
				if (effect.getSkill().isDispelOnDamage())
					getEffectList().stopEffect(effect.getSkill());
		}
		if (awake && isSleeping())
		{
			getEffectList().stopEffects(EffectType.Sleep);
		}
		if ((attacker != this) || ((skill != null) && skill.isOffensive()))
		{
			if (isMeditated())
			{
				Effect effect = getEffectList().getEffectByType(EffectType.Meditation);
				if (effect != null)
				{
					getEffectList().stopEffect(effect.getSkill());
				}
			}
			startAttackStanceTask();
			checkAndRemoveInvisible();
			if ((getCurrentHp() - damage) < 0.5)
			{
				useTriggers(attacker, TriggerType.DIE, null, null, damage);
			}
		}
		if (isPlayer() && getPlayer().isGM() && getPlayer().isUndying() && ((damage + 0.5) >= getCurrentHp()))
		{
			return;
		}
		setCurrentHp(Math.max(getCurrentHp() - damage, 0), false, attacker.getObjectId());
		broadcastStatusUpdate();
		if (getCurrentHp() < 0.5)
		{
			doDie(attacker);
		}
	}
	
	/**
	 * Method reduceCurrentMp.
	 * @param i double
	 * @param attacker Creature
	 */
	public void reduceCurrentMp(double i, Creature attacker)
	{
		if ((attacker != null) && (attacker != this))
		{
			if (isSleeping())
			{
				getEffectList().stopEffects(EffectType.Sleep);
			}
			if (isMeditated())
			{
				Effect effect = getEffectList().getEffectByType(EffectType.Meditation);
				if (effect != null)
				{
					getEffectList().stopEffect(effect.getSkill());
				}
			}
		}
		if (isDamageBlocked() && (attacker != null) && (attacker != this))
		{
			attacker.sendPacket(Msg.THE_ATTACK_HAS_BEEN_BLOCKED);
			return;
		}
		if ((attacker != null) && attacker.isPlayer() && (Math.abs(attacker.getLevel() - getLevel()) > 10))
		{
			if ((attacker.getKarma() < 0) && (getEffectList().getEffectsBySkillId(5182) != null) && !isInZone(ZoneType.SIEGE))
			{
				return;
			}
			if ((getKarma() < 0) && (attacker.getEffectList().getEffectsBySkillId(5182) != null) && !attacker.isInZone(ZoneType.SIEGE))
			{
				return;
			}
		}
		i = _currentMp - i;
		if (i < 0)
		{
			i = 0;
		}
		setCurrentMp(i);
		if ((attacker != null) && (attacker != this))
		{
			startAttackStanceTask();
		}
	}
	
	/**
	 * Method removeAllSkills.
	 */
	public void removeAllSkills()
	{
		for (Skill s : getAllSkillsArray())
		{
			removeSkill(s);
		}
	}
	
	/**
	 * Method removeBlockStats.
	 * @param stats List<Stats>
	 */
	public void removeBlockStats(List<Stats> stats)
	{
		if (_blockedStats != null)
		{
			_blockedStats.removeAll(stats);
			if (_blockedStats.isEmpty())
			{
				_blockedStats = null;
			}
		}
	}
	
	/**
	 * Method removeSkill.
	 * @param skill Skill
	 * @return Skill
	 */
	public Skill removeSkill(Skill skill)
	{
		if (skill == null)
		{
			return null;
		}
		return removeSkillById(skill.getId());
	}
	
	/**
	 * Method removeSkillById.
	 * @param id Integer
	 * @return Skill
	 */
	public Skill removeSkillById(Integer id)
	{
		Skill oldSkill = _skills.remove(id);
		List<Effect> effects;
		if (oldSkill != null)
		{
			removeTriggers(oldSkill);
			removeStatsOwner(oldSkill);
			if (Config.ALT_DELETE_SA_BUFFS && (oldSkill.isItemSkill() || oldSkill.isHandler()))
			{
				effects = getEffectList().getEffectsBySkill(oldSkill);
				if (effects != null)
				{
					for (Effect effect : effects)
					{
						effect.exit();
					}
				}
				if (isPlayer())
				{
					for (Summon summon : ((Player) this).getSummonList())
					{
						if (summon != null)
						{
							effects = summon.getEffectList().getEffectsBySkill(oldSkill);
							if (effects != null)
							{
								for (Effect effect : effects)
								{
									effect.exit();
								}
							}
						}
					}
				}
			}
		}
		return oldSkill;
	}
	
	/**
	 * Method addTriggers.
	 * @param f StatTemplate
	 */
	public void addTriggers(StatTemplate f)
	{
		if (f.getTriggerList().isEmpty())
		{
			return;
		}
		for (TriggerInfo t : f.getTriggerList())
		{
			addTrigger(t);
		}
	}
	
	/**
	 * Method addTrigger.
	 * @param t TriggerInfo
	 */
	public void addTrigger(TriggerInfo t)
	{
		if (_triggers == null)
		{
			_triggers = new ConcurrentHashMap<TriggerType, Set<TriggerInfo>>();
		}
		Set<TriggerInfo> hs = _triggers.get(t.getType());
		if (hs == null)
		{
			hs = new CopyOnWriteArraySet<TriggerInfo>();
			_triggers.put(t.getType(), hs);
		}
		hs.add(t);
		if (t.getType() == TriggerType.ADD)
		{
			useTriggerSkill(this, null, t, null, 0);
		}
	}
	
	/**
	 * Method removeTriggers.
	 * @param f StatTemplate
	 */
	public void removeTriggers(StatTemplate f)
	{
		if ((_triggers == null) || f.getTriggerList().isEmpty())
		{
			return;
		}
		for (TriggerInfo t : f.getTriggerList())
		{
			removeTrigger(t);
		}
	}
	
	/**
	 * Method removeTrigger.
	 * @param t TriggerInfo
	 */
	public void removeTrigger(TriggerInfo t)
	{
		if (_triggers == null)
		{
			return;
		}
		Set<TriggerInfo> hs = _triggers.get(t.getType());
		if (hs == null)
		{
			return;
		}
		hs.remove(t);
	}
	
	/**
	 * Method sendActionFailed.
	 */
	public void sendActionFailed()
	{
		sendPacket(ActionFail.STATIC);
	}
	
	/**
	 * Method hasAI.
	 * @return boolean
	 */
	public boolean hasAI()
	{
		return _ai != null;
	}
	
	/**
	 * Method getAI.
	 * @return CharacterAI
	 */
	public CharacterAI getAI()
	{
		if (_ai == null)
		{
			synchronized (this)
			{
				if (_ai == null)
				{
					_ai = new CharacterAI(this);
				}
			}
		}
		return _ai;
	}
	
	/**
	 * Method setAI.
	 * @param newAI CharacterAI
	 */
	public void setAI(CharacterAI newAI)
	{
		if (newAI == null)
		{
			return;
		}
		CharacterAI oldAI = _ai;
		synchronized (this)
		{
			_ai = newAI;
		}
		if (oldAI != null)
		{
			if (oldAI.isActive())
			{
				oldAI.stopAITask();
				newAI.startAITask();
				newAI.setIntention(CtrlIntention.AI_INTENTION_ACTIVE);
			}
		}
	}
	
	/**
	 * Method setCurrentHp.
	 * @param newHp double
	 * @param canRessurect boolean
	 * @param sendInfo boolean
	 * @param _attakerId int
	 */
	public final void setCurrentHp(double newHp, boolean canRessurect, boolean sendInfo, int _attakerId)
	{
		int maxHp = getMaxHp();
		newHp = Math.min(maxHp, Math.max(0, newHp));
		if (_currentHp == newHp)
		{
			return;
		}
		if ((newHp >= 0.5) && isDead() && !canRessurect)
		{
			return;
		}
		double hpStart = _currentHp;
		_currentHp = newHp;
		if (isDead.compareAndSet(true, false))
		{
			onRevive();
		}
		checkHpMessages(hpStart, _currentHp);
		if (sendInfo)
		{
			broadcastStatusUpdate();
			sendChanges();
		}
		if (_currentHp < maxHp)
		{
			startRegeneration();
		}
	}
	
	/**
	 * Method setCurrentHp.
	 * @param newHp double
	 * @param canRessurect boolean
	 */
	public final void setCurrentHp(double newHp, boolean canRessurect)
	{
		setCurrentHp(newHp, canRessurect, true, 0);
	}
	
	/**
	 * Method setCurrentHp.
	 * @param newHp double
	 * @param canRessurect boolean
	 * @param _attakerObjId int
	 */
	public final void setCurrentHp(double newHp, boolean canRessurect, int _attakerObjId)
	{
		setCurrentHp(newHp, canRessurect, true, _attakerObjId);
	}
	
	/**
	 * Method setCurrentMp.
	 * @param newMp double
	 * @param sendInfo boolean
	 */
	public final void setCurrentMp(double newMp, boolean sendInfo)
	{
		int maxMp = getMaxMp();
		newMp = Math.min(maxMp, Math.max(0, newMp));
		if (_currentMp == newMp)
		{
			return;
		}
		if ((newMp >= 0.5) && isDead())
		{
			return;
		}
		_currentMp = newMp;
		if (sendInfo)
		{
			broadcastStatusUpdate();
			sendChanges();
		}
		if (_currentMp < maxMp)
		{
			startRegeneration();
		}
	}
	
	/**
	 * Method setCurrentMp.
	 * @param newMp double
	 */
	public final void setCurrentMp(double newMp)
	{
		setCurrentMp(newMp, true);
	}
	
	/**
	 * Method setCurrentCp.
	 * @param newCp double
	 * @param sendInfo boolean
	 */
	public final void setCurrentCp(double newCp, boolean sendInfo)
	{
		if (!isPlayer())
		{
			return;
		}
		int maxCp = getMaxCp();
		newCp = Math.min(maxCp, Math.max(0, newCp));
		if (_currentCp == newCp)
		{
			return;
		}
		if ((newCp >= 0.5) && isDead())
		{
			return;
		}
		_currentCp = newCp;
		if (sendInfo)
		{
			broadcastStatusUpdate();
			sendChanges();
		}
		if (_currentCp < maxCp)
		{
			startRegeneration();
		}
	}
	
	/**
	 * Method setCurrentCp.
	 * @param newCp double
	 */
	public final void setCurrentCp(double newCp)
	{
		setCurrentCp(newCp, true);
	}
	
	/**
	 * Method setCurrentHpMp.
	 * @param newHp double
	 * @param newMp double
	 * @param canRessurect boolean
	 */
	public void setCurrentHpMp(double newHp, double newMp, boolean canRessurect)
	{
		int maxHp = getMaxHp();
		int maxMp = getMaxMp();
		newHp = Math.min(maxHp, Math.max(0, newHp));
		newMp = Math.min(maxMp, Math.max(0, newMp));
		if ((_currentHp == newHp) && (_currentMp == newMp))
		{
			return;
		}
		if ((newHp >= 0.5) && isDead() && !canRessurect)
		{
			return;
		}
		double hpStart = _currentHp;
		_currentHp = newHp;
		_currentMp = newMp;
		if (isDead.compareAndSet(true, false))
		{
			onRevive();
		}
		checkHpMessages(hpStart, _currentHp);
		broadcastStatusUpdate();
		sendChanges();
		if ((_currentHp < maxHp) || (_currentMp < maxMp))
		{
			startRegeneration();
		}
	}
	
	/**
	 * Method setCurrentHpMp.
	 * @param newHp double
	 * @param newMp double
	 */
	public void setCurrentHpMp(double newHp, double newMp)
	{
		setCurrentHpMp(newHp, newMp, false);
	}
	
	/**
	 * Method setFlying.
	 * @param mode boolean
	 */
	public final void setFlying(boolean mode)
	{
		_flying = mode;
	}
	
	/**
	 * Method getHeading.
	 * @return int
	 */
	@Override
	public final int getHeading()
	{
		return _heading;
	}
	
	/**
	 * Method setHeading.
	 * @param heading int
	 */
	public void setHeading(int heading)
	{
		_heading = heading;
	}
	
	/**
	 * Method setIsTeleporting.
	 * @param value boolean
	 */
	public final void setIsTeleporting(boolean value)
	{
		isTeleporting.compareAndSet(!value, value);
	}
	
	/**
	 * Method setName.
	 * @param name String
	 */
	public final void setName(String name)
	{
		_name = name;
	}
	
	/**
	 * Method getCastingTarget.
	 * @return Creature
	 */
	public Creature getCastingTarget()
	{
		return castingTarget.get();
	}
	
	/**
	 * Method setCastingTarget.
	 * @param target Creature
	 */
	public void setCastingTarget(Creature target)
	{
		if (target == null)
		{
			castingTarget = HardReferences.emptyRef();
		}
		else
		{
			castingTarget = target.getRef();
		}
	}
	
	/**
	 * Method setRunning.
	 */
	public final void setRunning()
	{
		if (!_running)
		{
			_running = true;
			broadcastPacket(new ChangeMoveType(this));
		}
	}
	
	/**
	 * Method setSkillMastery.
	 * @param skill Integer
	 * @param mastery int
	 */
	public void setSkillMastery(Integer skill, int mastery)
	{
		if (_skillMastery == null)
		{
			_skillMastery = new HashMap<Integer, Integer>();
		}
		_skillMastery.put(skill, mastery);
	}
	
	/**
	 * Method setAggressionTarget.
	 * @param target Creature
	 */
	public void setAggressionTarget(Creature target)
	{
		if (target == null)
		{
			_aggressionTarget = HardReferences.emptyRef();
		}
		else
		{
			_aggressionTarget = target.getRef();
		}
	}
	
	/**
	 * Method getAggressionTarget.
	 * @return Creature
	 */
	public Creature getAggressionTarget()
	{
		return _aggressionTarget.get();
	}
	
	/**
	 * Method setTarget.
	 * @param object GameObject
	 */
	public void setTarget(GameObject object)
	{
		if ((object != null) && !object.isVisible())
		{
			object = null;
		}
		if (object == null)
		{
			target = HardReferences.emptyRef();
		}
		else
		{
			target = object.getRef();
		}
	}
	
	/**
	 * Method setTitle.
	 * @param title String
	 */
	public void setTitle(String title)
	{
		_title = title;
	}
	
	/**
	 * Method setWalking.
	 */
	public void setWalking()
	{
		if (_running)
		{
			_running = false;
			broadcastPacket(new ChangeMoveType(this));
		}
	}
	
	/**
	 * Method startAbnormalEffect.
	 * @param ae AbnormalEffect
	 */
	public void startAbnormalEffect(AbnormalEffect ae)
	{
		if (ae == AbnormalEffect.NULL)
		{
			_abnormalEffects = AbnormalEffect.NULL.getMask();
			_abnormalEffects2 = AbnormalEffect.NULL.getMask();
			_abnormalEffects3 = AbnormalEffect.NULL.getMask();
			_aveList.clear();
		}
		else if (ae.isSpecial())
		{
			_abnormalEffects2 |= ae.getMask();
			addToAveList(ae.getId());
		}
		else if (ae.isEvent())
		{
			_abnormalEffects3 |= ae.getMask();
			addToAveList(ae.getId());
		}
		else
		{
			_abnormalEffects |= ae.getMask();
			addToAveList(ae.getId());
		}
		sendChanges();
		broadcastCharInfo();
	}
	
	/**
	 * Method startAttackStanceTask.
	 */
	public void startAttackStanceTask()
	{
		startAttackStanceTask0();
	}
	
	/**
	 * Method startAttackStanceTask0.
	 */
	protected void startAttackStanceTask0()
	{
		if (isInCombat())
		{
			_stanceEndTime = System.currentTimeMillis() + 15000L;
			return;
		}
		_stanceEndTime = System.currentTimeMillis() + 15000L;
		broadcastPacket(new AutoAttackStart(getObjectId()));
		final Future<?> task = _stanceTask;
		if (task != null)
		{
			task.cancel(false);
		}
		_stanceTask = LazyPrecisionTaskManager.getInstance().scheduleAtFixedRate(_stanceTaskRunnable == null ? _stanceTaskRunnable = new AttackStanceTask() : _stanceTaskRunnable, 1000L, 1000L);
	}
	
	/**
	 * Method stopAttackStanceTask.
	 */
	public void stopAttackStanceTask()
	{
		_stanceEndTime = 0L;
		final Future<?> task = _stanceTask;
		if (task != null)
		{
			task.cancel(false);
			_stanceTask = null;
			broadcastPacket(new AutoAttackStop(getObjectId()));
		}
	}
	
	/**
	 * @author Mobius
	 */
	private class AttackStanceTask extends RunnableImpl
	{
		/**
		 * Constructor for AttackStanceTask.
		 */
		public AttackStanceTask()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			if (!isInCombat())
			{
				stopAttackStanceTask();
			}
		}
	}
	
	/**
	 * Method stopRegeneration.
	 */
	protected void stopRegeneration()
	{
		regenLock.lock();
		try
		{
			if (_isRegenerating)
			{
				_isRegenerating = false;
				if (_regenTask != null)
				{
					_regenTask.cancel(false);
					_regenTask = null;
				}
			}
		}
		finally
		{
			regenLock.unlock();
		}
	}
	
	/**
	 * Method startRegeneration.
	 */
	protected void startRegeneration()
	{
		if (!isVisible() || isDead() || (getRegenTick() == 0L))
		{
			return;
		}
		if (_isRegenerating)
		{
			return;
		}
		regenLock.lock();
		try
		{
			if (!_isRegenerating)
			{
				_isRegenerating = true;
				_regenTask = RegenTaskManager.getInstance().scheduleAtFixedRate(_regenTaskRunnable == null ? _regenTaskRunnable = new RegenTask() : _regenTaskRunnable, 0, getRegenTick());
			}
		}
		finally
		{
			regenLock.unlock();
		}
	}
	
	/**
	 * Method getRegenTick.
	 * @return long
	 */
	public long getRegenTick()
	{
		return 3000L;
	}
	
	/**
	 * @author Mobius
	 */
	private class RegenTask implements Runnable
	{
		/**
		 * Constructor for RegenTask.
		 */
		public RegenTask()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method run.
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run()
		{
			if (isAlikeDead() || (getRegenTick() == 0L))
			{
				return;
			}
			double hpStart = _currentHp;
			int maxHp = getMaxHp();
			int maxMp = getMaxMp();
			int maxCp = isPlayer() ? getMaxCp() : 0;
			double addHp = 0.;
			double addMp = 0.;
			regenLock.lock();
			try
			{
				if (_currentHp < maxHp)
				{
					addHp += Formulas.calcHpRegen(Creature.this);
				}
				if (_currentMp < maxMp)
				{
					addMp += Formulas.calcMpRegen(Creature.this);
				}
				if (isPlayer() && Config.REGEN_SIT_WAIT)
				{
					Player pl = (Player) Creature.this;
					if (pl.isSitting())
					{
						pl.updateWaitSitTime();
						if (pl.getWaitSitTime() > 5)
						{
							addHp += pl.getWaitSitTime();
							addMp += pl.getWaitSitTime();
						}
					}
				}
				else if (isRaid())
				{
					addHp *= Config.RATE_RAID_REGEN;
					addMp *= Config.RATE_RAID_REGEN;
				}
				_currentHp += Math.max(0, Math.min(addHp, ((calcStat(Stats.HP_LIMIT, null, null) * maxHp) / 100.) - _currentHp));
				_currentMp += Math.max(0, Math.min(addMp, ((calcStat(Stats.MP_LIMIT, null, null) * maxMp) / 100.) - _currentMp));
				_currentHp = Math.min(maxHp, _currentHp);
				_currentMp = Math.min(maxMp, _currentMp);
				if (isPlayer())
				{
					_currentCp += Math.max(0, Math.min(Formulas.calcCpRegen(Creature.this), ((calcStat(Stats.CP_LIMIT, null, null) * maxCp) / 100.) - _currentCp));
					_currentCp = Math.min(maxCp, _currentCp);
				}
				if ((_currentHp == maxHp) && (_currentMp == maxMp) && (_currentCp == maxCp))
				{
					stopRegeneration();
				}
			}
			finally
			{
				regenLock.unlock();
			}
			broadcastStatusUpdate();
			sendChanges();
			checkHpMessages(hpStart, _currentHp);
		}
	}
	
	/**
	 * Method stopAbnormalEffect.
	 * @param ae AbnormalEffect
	 */
	public void stopAbnormalEffect(AbnormalEffect ae)
	{
		if (ae.isSpecial())
		{
			_abnormalEffects2 &= ~ae.getMask();
			removeFromAveList(ae.getId());
		}
		else if (ae.isEvent())
		{
			_abnormalEffects3 &= ~ae.getMask();
			removeFromAveList(ae.getId());
		}
		else
		{
			_abnormalEffects &= ~ae.getMask();
			removeFromAveList(ae.getId());
		}
		sendChanges();
		broadcastCharInfo();
	}
	
	/**
	 * Method block.
	 */
	public void block()
	{
		_blocked = true;
	}
	
	/**
	 * Method unblock.
	 */
	public void unblock()
	{
		_blocked = false;
	}
	
	/**
	 * Method startConfused.
	 * @return boolean
	 */
	public boolean startConfused()
	{
		return _confused.getAndSet(true);
	}
	
	/**
	 * Method stopConfused.
	 * @return boolean
	 */
	public boolean stopConfused()
	{
		return _confused.setAndGet(false);
	}
	
	/**
	 * Method startFear.
	 * @return boolean
	 */
	public boolean startFear()
	{
		return _afraid.getAndSet(true);
	}
	
	/**
	 * Method stopFear.
	 * @return boolean
	 */
	public boolean stopFear()
	{
		return _afraid.setAndGet(false);
	}
	
	/**
	 * Method startMuted.
	 * @return boolean
	 */
	public boolean startMuted()
	{
		return _muted.getAndSet(true);
	}
	
	/**
	 * Method stopMuted.
	 * @return boolean
	 */
	public boolean stopMuted()
	{
		return _muted.setAndGet(false);
	}
	
	/**
	 * Method startPMuted.
	 * @return boolean
	 */
	public boolean startPMuted()
	{
		return _pmuted.getAndSet(true);
	}
	
	/**
	 * Method stopPMuted.
	 * @return boolean
	 */
	public boolean stopPMuted()
	{
		return _pmuted.setAndGet(false);
	}
	
	/**
	 * Method startAMuted.
	 * @return boolean
	 */
	public boolean startAMuted()
	{
		return _amuted.getAndSet(true);
	}
	
	/**
	 * Method stopAMuted.
	 * @return boolean
	 */
	public boolean stopAMuted()
	{
		return _amuted.setAndGet(false);
	}
	
	/**
	 * Method startRooted.
	 * @return boolean
	 */
	public boolean startRooted()
	{
		return _rooted.getAndSet(true);
	}
	
	/**
	 * Method stopRooted.
	 * @return boolean
	 */
	public boolean stopRooted()
	{
		return _rooted.setAndGet(false);
	}
	
	/**
	 * Method startSleeping.
	 * @return boolean
	 */
	public boolean startSleeping()
	{
		return _sleeping.getAndSet(true);
	}
	
	/**
	 * Method stopSleeping.
	 * @return boolean
	 */
	public boolean stopSleeping()
	{
		return _sleeping.setAndGet(false);
	}
	
	/**
	 * Method startAirbinding.
	 * @return boolean
	 */
	public boolean startAirbinding()
	{
		return _airbinded.getAndSet(true);
	}
	
	/**
	 * Method stopAirbinding.
	 * @return boolean
	 */
	public boolean stopAirbinding()
	{
		return _airbinded.setAndGet(false);
	}

	/**
	 * Method startKnockingback.
	 * @return boolean
	 */
	public boolean startKnockingback()
	{
		return _knockedback.getAndSet(true);
	}
	
	/**
	 * Method stopKnockingback.
	 * @return boolean
	 */
	public boolean stopKnockingback()
	{
		return _knockedback.setAndGet(false);
	}

	/**
	 * Method startKnockingback.
	 * @return boolean
	 */
	public boolean startKnockingdown()
	{
		return _knockeddown.getAndSet(true);
	}
	
	/**
	 * Method stopKnockingback.
	 * @return boolean
	 */
	public boolean stopKnockingdown()
	{
		return _knockeddown.setAndGet(false);
	}


	/**
	 * Method startPulling For chain skills.
	 * @return boolean
	 */
	public boolean startPulling()
	{
		return _pulled.getAndSet(true);
	}
	
	/**
	 * Method stopKnockingback for chain skill.
	 * @return boolean
	 */
	public boolean stopPulling()
	{
		return _pulled.setAndGet(false);
	}
	
	/**
	 * Method startStunning.
	 * @return boolean
	 */
	public boolean startStunning()
	{
		return _stunned.getAndSet(true);
	}
	
	/**
	 * Method stopStunning.
	 * @return boolean
	 */
	public boolean stopStunning()
	{
		return _stunned.setAndGet(false);
	}
	
	/**
	 * Method startParalyzed.
	 * @return boolean
	 */
	public boolean startParalyzed()
	{
		return _paralyzed.getAndSet(true);
	}
	
	/**
	 * Method stopParalyzed.
	 * @return boolean
	 */
	public boolean stopParalyzed()
	{
		return _paralyzed.setAndGet(false);
	}
	
	/**
	 * Method startImmobilized.
	 * @return boolean
	 */
	public boolean startImmobilized()
	{
		return _immobilized.getAndSet(true);
	}
	
	/**
	 * Method stopImmobilized.
	 * @return boolean
	 */
	public boolean stopImmobilized()
	{
		return _immobilized.setAndGet(false);
	}
	
	/**
	 * Method startHealBlocked.
	 * @return boolean
	 */
	public boolean startHealBlocked()
	{
		return _healBlocked.getAndSet(true);
	}
	
	/**
	 * Method stopHealBlocked.
	 * @return boolean
	 */
	public boolean stopHealBlocked()
	{
		return _healBlocked.setAndGet(false);
	}
	
	/**
	 * Method startDamageBlocked.
	 * @return boolean
	 */
	public boolean startDamageBlocked()
	{
		return _damageBlocked.getAndSet(true);
	}
	
	/**
	 * Method stopDamageBlocked.
	 * @return boolean
	 */
	public boolean stopDamageBlocked()
	{
		return _damageBlocked.setAndGet(false);
	}
	
	/**
	 * Method startBuffImmunity.
	 * @return boolean
	 */
	public boolean startBuffImmunity()
	{
		return _buffImmunity.getAndSet(true);
	}
	
	/**
	 * Method stopBuffImmunity.
	 * @return boolean
	 */
	public boolean stopBuffImmunity()
	{
		return _buffImmunity.setAndGet(false);
	}
	
	/**
	 * Method startDebuffImmunity.
	 * @return boolean
	 */
	public boolean startDebuffImmunity()
	{
		return _debuffImmunity.getAndSet(true);
	}
	
	/**
	 * Method stopDebuffImmunity.
	 * @return boolean
	 */
	public boolean stopDebuffImmunity()
	{
		return _debuffImmunity.setAndGet(false);
	}
	
	/**
	 * Method startEffectImmunity.
	 * @return boolean
	 */
	public boolean startEffectImmunity()
	{
		return _effectImmunity.getAndSet(true);
	}
	
	/**
	 * Method stopEffectImmunity.
	 * @return boolean
	 */
	public boolean stopEffectImmunity()
	{
		return _effectImmunity.setAndGet(false);
	}
	
	/**
	 * Method startWeaponEquipBlocked.
	 * @return boolean
	 */
	public boolean startWeaponEquipBlocked()
	{
		return _weaponEquipBlocked.getAndSet(true);
	}
	
	/**
	 * Method stopWeaponEquipBlocked.
	 * @return boolean
	 */
	public boolean stopWeaponEquipBlocked()
	{
		return _weaponEquipBlocked.getAndSet(false);
	}
	
	/**
	 * Method startFrozen.
	 * @return boolean
	 */
	public boolean startFrozen()
	{
		return _frozen.getAndSet(true);
	}
	
	/**
	 * Method stopFrozen.
	 * @return boolean
	 */
	public boolean stopFrozen()
	{
		return _frozen.setAndGet(false);
	}
	
	/**
	 * Method setFakeDeath.
	 * @param value boolean
	 */
	public void setFakeDeath(boolean value)
	{
		_fakeDeath = value;
	}
	
	/**
	 * Method breakFakeDeath.
	 */
	public void breakFakeDeath()
	{
		getEffectList().stopAllSkillEffects(EffectType.FakeDeath);
	}
	
	/**
	 * Method setMeditated.
	 * @param value boolean
	 */
	public void setMeditated(boolean value)
	{
		_meditated = value;
	}
	
	/**
	 * Method setIsBlessedByNoblesse.
	 * @param value boolean
	 */
	public final void setIsBlessedByNoblesse(boolean value)
	{
		_isBlessedByNoblesse = value;
	}
	
	/**
	 * Method setIsSalvation.
	 * @param value boolean
	 */
	public final void setIsSalvation(boolean value)
	{
		_isSalvation = value;
	}
	
	/**
	 * Method setIsInvul.
	 * @param value boolean
	 */
	public void setIsInvul(boolean value)
	{
		_isInvul = value;
	}
	
	/**
	 * Method setLockedTarget.
	 * @param value boolean
	 */
	public void setLockedTarget(boolean value)
	{
		_lockedTarget = value;
	}
	
	/**
	 * Method setTargetable.
	 * @param value boolean
	 */
	public void setTargetable(boolean value)
	{
		_isTargetable = value;
	}
	
	/**
	 * Method isConfused.
	 * @return boolean
	 */
	public boolean isConfused()
	{
		return _confused.get();
	}
	
	/**
	 * Method isFakeDeath.
	 * @return boolean
	 */
	public boolean isFakeDeath()
	{
		return _fakeDeath;
	}
	
	/**
	 * Method isAfraid.
	 * @return boolean
	 */
	public boolean isAfraid()
	{
		return _afraid.get();
	}
	
	/**
	 * Method isBlocked.
	 * @return boolean
	 */
	public boolean isBlocked()
	{
		return _blocked;
	}
	
	/**
	 * Method isMuted.
	 * @param skill Skill
	 * @return boolean
	 */
	public boolean isMuted(Skill skill)
	{
		if ((skill == null) || skill.isNotAffectedByMute())
		{
			return false;
		}
		return (isMMuted() && skill.isMagic()) || (isPMuted() && !skill.isMagic());
	}
	
	/**
	 * Method isPMuted.
	 * @return boolean
	 */
	public boolean isPMuted()
	{
		return _pmuted.get();
	}
	
	/**
	 * Method isMMuted.
	 * @return boolean
	 */
	public boolean isMMuted()
	{
		return _muted.get();
	}
	
	/**
	 * Method isAMuted.
	 * @return boolean
	 */
	public boolean isAMuted()
	{
		return _amuted.get();
	}
	
	/**
	 * Method isRooted.
	 * @return boolean
	 */
	public boolean isRooted()
	{
		return _rooted.get();
	}
	
	/**
	 * Method isSleeping.
	 * @return boolean
	 */
	public boolean isSleeping()
	{
		return _sleeping.get();
	}
	
	/**
	 * Method isStunned.
	 * @return boolean
	 */
	public boolean isStunned()
	{
		return _stunned.get();
	}
	
	/**
	 * Method isAirBinded.
	 * @return boolean
	 */
	public boolean isAirBinded()
	{
		return _airbinded.get();
	}
	
	/**
	 * Method isKnockedBack.
	 * @return boolean
	 */
	public boolean isKnockedBack()
	{
		return _knockedback.get();
	}
	
	/**
	 * Method isKnockedDown.
	 * @return boolean
	 */
	public boolean isKnockedDown()
	{
		return _knockeddown.get();
	}
	
	/**
	 * Method isPulledNow.
	 * @return boolean
	 */
	public boolean isPulledNow()
	{
		return _pulled.get();
	}
	/**
	 * Method isMeditated.
	 * @return boolean
	 */
	public boolean isMeditated()
	{
		return _meditated;
	}
	
	/**
	 * Method isWeaponEquipBlocked.
	 * @return boolean
	 */
	public boolean isWeaponEquipBlocked()
	{
		return _weaponEquipBlocked.get();
	}
	
	/**
	 * Method isParalyzed.
	 * @return boolean
	 */
	public boolean isParalyzed()
	{
		return _paralyzed.get();
	}
	
	/**
	 * Method isFrozen.
	 * @return boolean
	 */
	public boolean isFrozen()
	{
		return _frozen.get();
	}
	
	/**
	 * Method isImmobilized.
	 * @return boolean
	 */
	public boolean isImmobilized()
	{
		return _immobilized.get() || (getRunSpeed() < 1);
	}
	
	/**
	 * Method isHealBlocked.
	 * @return boolean
	 */
	public boolean isHealBlocked()
	{
		return isAlikeDead() || _healBlocked.get();
	}
	
	/**
	 * Method isDamageBlocked.
	 * @return boolean
	 */
	public boolean isDamageBlocked()
	{
		return isInvul() || _damageBlocked.get();
	}
	
	/**
	 * Method isCastingNow.
	 * @return boolean
	 */
	public boolean isCastingNow()
	{
		return _skillTask != null;
	}
	
	/**
	 * Method isDoubleCastingNow.
	 * @return boolean
	 */
	public boolean isDoubleCastingNow()
	{
		return _skillDoubleTask != null;
	}
	
	/**
	 * Method isLockedTarget.
	 * @return boolean
	 */
	public boolean isLockedTarget()
	{
		return _lockedTarget;
	}
	
	/**
	 * Method isTargetable.
	 * @return boolean
	 */
	public boolean isTargetable()
	{
		return _isTargetable;
	}
	
	/**
	 * Method isMovementDisabled.
	 * @return boolean
	 */
	public boolean isMovementDisabled()
	{
		return isBlocked() || isRooted() || isImmobilized() || isAlikeDead() || isStunned() || isSleeping() || isParalyzed() || isAttackingNow() || isCastingNow() || isFrozen() || isAirBinded() || isKnockedBack() || isKnockedDown() || isPulledNow();
	}
	
	/**
	 * Method isActionsDisabled.
	 * @return boolean
	 */
	public boolean isActionsDisabled()
	{
		return isBlocked() || isAlikeDead() || isStunned() || isSleeping() || isParalyzed() || isAttackingNow() || isCastingNow() || isFrozen() || isAirBinded() || isKnockedBack() || isKnockedDown() || isPulledNow();
	}
	
	/**
	 * Method isAttackingDisabled.
	 * @return boolean
	 */
	public final boolean isAttackingDisabled()
	{
		return _attackReuseEndTime > System.currentTimeMillis();
	}
	
	/**
	 * Method isOutOfControl.
	 * @return boolean
	 */
	public boolean isOutOfControl()
	{
		return isBlocked() || isConfused() || isAfraid();
	}
	
	/**
	 * Method teleToLocation.
	 * @param loc Location
	 */
	public void teleToLocation(Location loc)
	{
		teleToLocation(loc.x, loc.y, loc.z, getReflection());
	}
	
	/**
	 * Method teleToLocation.
	 * @param loc Location
	 * @param refId int
	 */
	public void teleToLocation(Location loc, int refId)
	{
		teleToLocation(loc.x, loc.y, loc.z, refId);
	}
	
	/**
	 * Method teleToLocation.
	 * @param loc Location
	 * @param r Reflection
	 */
	public void teleToLocation(Location loc, Reflection r)
	{
		teleToLocation(loc.x, loc.y, loc.z, r);
	}
	
	/**
	 * Method teleToLocation.
	 * @param x int
	 * @param y int
	 * @param z int
	 */
	public void teleToLocation(int x, int y, int z)
	{
		teleToLocation(x, y, z, getReflection());
	}
	
	/**
	 * Method checkAndRemoveInvisible.
	 */
	public void checkAndRemoveInvisible()
	{
		InvisibleType invisibleType = getInvisibleType();
		if (invisibleType == InvisibleType.EFFECT)
		{
			getEffectList().stopEffects(EffectType.Invisible);
		}
	}
	
	/**
	 * Method teleToLocation.
	 * @param x int
	 * @param y int
	 * @param z int
	 * @param refId int
	 */
	public void teleToLocation(int x, int y, int z, int refId)
	{
		Reflection r = ReflectionManager.getInstance().get(refId);
		if (r == null)
		{
			return;
		}
		teleToLocation(x, y, z, r);
	}
	
	/**
	 * Method teleToLocation.
	 * @param x int
	 * @param y int
	 * @param z int
	 * @param r Reflection
	 */
	public void teleToLocation(int x, int y, int z, Reflection r)
	{
		if (!isTeleporting.compareAndSet(false, true))
		{
			return;
		}
		if (isFakeDeath())
		{
			breakFakeDeath();
		}
		abortCast(true, false);
		if (!isLockedTarget())
		{
			setTarget(null);
		}
		stopMove();
		if (!isBoat() && !isFlying() && !World.isWater(new Location(x, y, z), r))
		{
			z = GeoEngine.getHeight(x, y, z, r.getGeoIndex());
		}
		if (isPlayer())
		{
			Player player = (Player) this;
			player.getListeners().onTeleport(x, y, z, r);
			decayMe();
			setXYZ(x, y, z);
			setReflection(r);
			player.setLastClientPosition(null);
			player.setLastServerPosition(null);
			player.sendPacket(new TeleportToLocation(player, x, y, z));
			player.sendPacket(new ExTeleportToLocationActivate(player, x, y, z));
		}
		else
		{
			setXYZ(x, y, z);
			setReflection(r);
			broadcastPacket(new TeleportToLocation(this, x, y, z));
			onTeleported();
		}
	}
	
	/**
	 * Method onTeleported.
	 * @return boolean
	 */
	public boolean onTeleported()
	{
		return isTeleporting.compareAndSet(true, false);
	}
	
	/**
	 * Method sendMessage.
	 * @param message CustomMessage
	 */
	public void sendMessage(CustomMessage message)
	{
	}
	
	/**
	 * Method toString.
	 * @return String
	 */
	@Override
	public String toString()
	{
		return getClass().getSimpleName() + "[" + getObjectId() + "]";
	}
	
	/**
	 * Method getEffectList.
	 * @return EffectList
	 */
	public EffectList getEffectList()
	{
		if (_effectList == null)
		{
			synchronized (this)
			{
				if (_effectList == null)
				{
					_effectList = new EffectList(this);
				}
			}
		}
		return _effectList;
	}
	
	/**
	 * Method paralizeOnAttack.
	 * @param attacker Creature
	 * @return boolean
	 */
	public boolean paralizeOnAttack(Creature attacker)
	{
		int max_attacker_level = 0xFFFF;
		MonsterInstance leader;
		if (isRaid() || (isMinion() && ((leader = ((MinionInstance) this).getLeader()) != null) && leader.isRaid()))
		{
			max_attacker_level = getLevel() + Config.RAID_MAX_LEVEL_DIFF;
		}
		else if (isNpc())
		{
			int max_level_diff = ((NpcInstance) this).getParameter("ParalizeOnAttack", -1000);
			if (max_level_diff != -1000)
			{
				max_attacker_level = getLevel() + max_level_diff;
			}
		}
		if (attacker.getLevel() > max_attacker_level)
		{
			return true;
		}
		return false;
	}
	
	/**
	 * Method onDelete.
	 */
	@Override
	protected void onDelete()
	{
		GameObjectsStorage.remove(_storedId);
		getEffectList().stopAllEffects();
		super.onDelete();
	}
	
	/**
	 * Method addExpAndSp.
	 * @param exp long
	 * @param sp long
	 */
	public void addExpAndSp(long exp, long sp)
	{
	}
	
	/**
	 * Method broadcastCharInfo.
	 */
	public void broadcastCharInfo()
	{
	}
	
	/**
	 * Method checkHpMessages.
	 * @param currentHp double
	 * @param newHp double
	 */
	public void checkHpMessages(double currentHp, double newHp)
	{
	}
	
	/**
	 * Method checkPvP.
	 * @param target Creature
	 * @param skill Skill
	 * @return boolean
	 */
	public boolean checkPvP(Creature target, Skill skill)
	{
		return false;
	}
	
	/**
	 * Method consumeItem.
	 * @param itemConsumeId int
	 * @param itemCount long
	 * @return boolean
	 */
	public boolean consumeItem(int itemConsumeId, long itemCount)
	{
		return true;
	}
	
	/**
	 * Method consumeItemMp.
	 * @param itemId int
	 * @param mp int
	 * @return boolean
	 */
	public boolean consumeItemMp(int itemId, int mp)
	{
		return true;
	}
	
	/**
	 * Method isFearImmune.
	 * @return boolean
	 */
	public boolean isFearImmune()
	{
		return false;
	}
	
	/**
	 * Method isLethalImmune.
	 * @return boolean
	 */
	public boolean isLethalImmune()
	{
		return getMaxHp() >= 50000;
	}
	
	/**
	 * Method getChargedSoulShot.
	 * @return boolean
	 */
	public boolean getChargedSoulShot()
	{
		return false;
	}
	
	/**
	 * Method getChargedSpiritShot.
	 * @return int
	 */
	public int getChargedSpiritShot()
	{
		return 0;
	}
	
	/**
	 * Method getIncreasedForce.
	 * @return int
	 */
	public int getIncreasedForce()
	{
		return 0;
	}
	
	/**
	 * Method getConsumedSouls.
	 * @return int
	 */
	public int getConsumedSouls()
	{
		return 0;
	}
	
	/**
	 * Method getAgathionEnergy.
	 * @return int
	 */
	public int getAgathionEnergy()
	{
		return 0;
	}
	
	/**
	 * Method setAgathionEnergy.
	 * @param val int
	 */
	public void setAgathionEnergy(int val)
	{
	}
	
	/**
	 * Method getKarma.
	 * @return int
	 */
	public int getKarma()
	{
		return 0;
	}
	
	/**
	 * Method getLevelMod.
	 * @return double
	 */
	public double getLevelMod()
	{
		return 1;
	}
	
	/**
	 * Method getNpcId.
	 * @return int
	 */
	public int getNpcId()
	{
		return 0;
	}
	
	/**
	 * Method getPvpFlag.
	 * @return int
	 */
	public int getPvpFlag()
	{
		return 0;
	}
	
	/**
	 * Method setTeam.
	 * @param t TeamType
	 */
	public void setTeam(TeamType t)
	{
		_team = t;
		sendChanges();
	}
	
	/**
	 * Method getTeam.
	 * @return TeamType
	 */
	public TeamType getTeam()
	{
		return _team;
	}
	
	/**
	 * Method isUndead.
	 * @return boolean
	 */
	public boolean isUndead()
	{
		return false;
	}
	
	/**
	 * Method isParalyzeImmune.
	 * @return boolean
	 */
	public boolean isParalyzeImmune()
	{
		return false;
	}
	
	/**
	 * Method reduceArrowCount.
	 */
	public void reduceArrowCount()
	{
	}
	
	/**
	 * Method sendChanges.
	 */
	public void sendChanges()
	{
		getStatsRecorder().sendChanges();
	}
	
	/**
	 * Method sendMessage.
	 * @param message String
	 */
	public void sendMessage(String message)
	{
	}
	
	/**
	 * Method sendPacket.
	 * @param mov IStaticPacket
	 */
	public void sendPacket(IStaticPacket mov)
	{
	}
	
	/**
	 * Method sendPacket.
	 * @param mov IStaticPacket[]
	 */
	public void sendPacket(IStaticPacket... mov)
	{
	}
	
	/**
	 * Method sendPacket.
	 * @param mov List<? extends IStaticPacket>
	 */
	public void sendPacket(List<? extends IStaticPacket> mov)
	{
	}
	
	/**
	 * Method setIncreasedForce.
	 * @param i int
	 */
	public void setIncreasedForce(int i)
	{
	}
	
	/**
	 * Method setConsumedSouls.
	 * @param i int
	 * @param monster NpcInstance
	 */
	public void setConsumedSouls(int i, NpcInstance monster)
	{
	}
	
	/**
	 * Method startPvPFlag.
	 * @param target Creature
	 */
	public void startPvPFlag(Creature target)
	{
	}
	
	/**
	 * Method unChargeShots.
	 * @param spirit boolean
	 * @return boolean
	 */
	public boolean unChargeShots(boolean spirit)
	{
		return false;
	}
	
	/**
	 * Method updateEffectIcons.
	 */
	public void updateEffectIcons()
	{
	}
	
	/**
	 * Method refreshHpMpCp.
	 */
	protected void refreshHpMpCp()
	{
		final int maxHp = getMaxHp();
		final int maxMp = getMaxMp();
		final int maxCp = isPlayer() ? getMaxCp() : 0;
		if (_currentHp > maxHp)
		{
			setCurrentHp(maxHp, false);
		}
		if (_currentMp > maxMp)
		{
			setCurrentMp(maxMp, false);
		}
		if (_currentCp > maxCp)
		{
			setCurrentCp(maxCp, false);
		}
		if ((_currentHp < maxHp) || (_currentMp < maxMp) || (_currentCp < maxCp))
		{
			startRegeneration();
		}
	}
	
	/**
	 * Method updateStats.
	 */
	public void updateStats()
	{
		refreshHpMpCp();
		sendChanges();
	}
	
	/**
	 * Method setOverhitAttacker.
	 * @param attacker Creature
	 */
	public void setOverhitAttacker(Creature attacker)
	{
	}
	
	/**
	 * Method setOverhitDamage.
	 * @param damage double
	 */
	public void setOverhitDamage(double damage)
	{
	}
	
	/**
	 * Method isCursedWeaponEquipped.
	 * @return boolean
	 */
	public boolean isCursedWeaponEquipped()
	{
		return false;
	}
	
	/**
	 * Method isHero.
	 * @return boolean
	 */
	public boolean isHero()
	{
		return false;
	}
	
	/**
	 * Method getAccessLevel.
	 * @return int
	 */
	public int getAccessLevel()
	{
		return 0;
	}
	
	/**
	 * Method getClan.
	 * @return Clan
	 */
	public Clan getClan()
	{
		return null;
	}
	
	/**
	 * Method getRateAdena.
	 * @return double
	 */
	public double getRateAdena()
	{
		return 1.;
	}
	
	/**
	 * Method getRateItems.
	 * @return double
	 */
	public double getRateItems()
	{
		return 1.;
	}
	
	/**
	 * Method getRateExp.
	 * @return double
	 */
	public double getRateExp()
	{
		return 1.;
	}
	
	/**
	 * Method getRateSp.
	 * @return double
	 */
	public double getRateSp()
	{
		return 1.;
	}
	
	/**
	 * Method getRateSpoil.
	 * @return double
	 */
	public double getRateSpoil()
	{
		return 1.;
	}
	
	/**
	 * Method getFormId.
	 * @return int
	 */
	public int getFormId()
	{
		return 0;
	}
	
	/**
	 * Method isNameAbove.
	 * @return boolean
	 */
	public boolean isNameAbove()
	{
		return true;
	}
	
	/**
	 * Method setLoc.
	 * @param loc Location
	 */
	@Override
	public void setLoc(Location loc)
	{
		setXYZ(loc.x, loc.y, loc.z);
	}
	
	/**
	 * Method setLoc.
	 * @param loc Location
	 * @param MoveTask boolean
	 */
	public void setLoc(Location loc, boolean MoveTask)
	{
		setXYZ(loc.x, loc.y, loc.z, MoveTask);
	}
	
	/**
	 * Method setXYZ.
	 * @param x int
	 * @param y int
	 * @param z int
	 */
	@Override
	public void setXYZ(int x, int y, int z)
	{
		setXYZ(x, y, z, false);
	}
	
	/**
	 * Method setXYZ.
	 * @param x int
	 * @param y int
	 * @param z int
	 * @param MoveTask boolean
	 */
	public void setXYZ(int x, int y, int z, boolean MoveTask)
	{
		if (!MoveTask)
		{
			stopMove();
		}
		moveLock.lock();
		try
		{
			super.setXYZ(x, y, z);
		}
		finally
		{
			moveLock.unlock();
		}
		updateZones();
	}
	
	/**
	 * Method onSpawn.
	 */
	@Override
	protected void onSpawn()
	{
		super.onSpawn();
		updateStats();
		updateZones();
	}
	
	/**
	 * Method spawnMe.
	 * @param loc Location
	 */
	@Override
	public void spawnMe(Location loc)
	{
		if (loc.h > 0)
		{
			setHeading(loc.h);
		}
		super.spawnMe(loc);
	}
	
	/**
	 * Method onDespawn.
	 */
	@Override
	protected void onDespawn()
	{
		if (!isLockedTarget())
		{
			setTarget(null);
		}
		stopMove();
		stopAttackStanceTask();
		stopRegeneration();
		updateZones();
		clearStatusListeners();
		super.onDespawn();
	}
	
	/**
	 * Method doDecay.
	 */
	public final void doDecay()
	{
		if (!isDead())
		{
			return;
		}
		onDecay();
	}
	
	/**
	 * Method onDecay.
	 */
	protected void onDecay()
	{
		decayMe();
	}
	
	/**
	 * Method validateLocation.
	 * @param broadcast int
	 */
	public void validateLocation(int broadcast)
	{
		L2GameServerPacket sp = new ValidateLocation(this);
		if (broadcast == 0)
		{
			sendPacket(sp);
		}
		else if (broadcast == 1)
		{
			broadcastPacket(sp);
		}
		else
		{
			broadcastPacketToOthers(sp);
		}
	}
	
	/**
	 * Field _unActiveSkills.
	 */
	private final TIntHashSet _unActiveSkills = new TIntHashSet();
	
	/**
	 * Method addUnActiveSkill.
	 * @param skill Skill
	 */
	public void addUnActiveSkill(Skill skill)
	{
		if ((skill == null) || isUnActiveSkill(skill.getId()))
		{
			return;
		}
		removeStatsOwner(skill);
		removeTriggers(skill);
		_unActiveSkills.add(skill.getId());
	}
	
	/**
	 * Method removeUnActiveSkill.
	 * @param skill Skill
	 */
	public void removeUnActiveSkill(Skill skill)
	{
		if ((skill == null) || !isUnActiveSkill(skill.getId()))
		{
			return;
		}
		addStatFuncs(skill.getStatFuncs());
		addTriggers(skill);
		_unActiveSkills.remove(skill.getId());
	}
	
	/**
	 * Method isUnActiveSkill.
	 * @param id int
	 * @return boolean
	 */
	public boolean isUnActiveSkill(int id)
	{
		return _unActiveSkills.contains(id);
	}
	
	/**
	 * Method getLevel.
	 * @return int
	 */
	public abstract int getLevel();
	
	/**
	 * Method getActiveWeaponInstance.
	 * @return ItemInstance
	 */
	public abstract ItemInstance getActiveWeaponInstance();
	
	/**
	 * Method getActiveWeaponItem.
	 * @return WeaponTemplate
	 */
	public abstract WeaponTemplate getActiveWeaponItem();
	
	/**
	 * Method getSecondaryWeaponInstance.
	 * @return ItemInstance
	 */
	public abstract ItemInstance getSecondaryWeaponInstance();
	
	/**
	 * Method getSecondaryWeaponItem.
	 * @return WeaponTemplate
	 */
	public abstract WeaponTemplate getSecondaryWeaponItem();
	
	/**
	 * Method getListeners.
	 * @return CharListenerList
	 */
	public CharListenerList getListeners()
	{
		if (listeners == null)
		{
			synchronized (this)
			{
				if (listeners == null)
				{
					listeners = new CharListenerList(this);
				}
			}
		}
		return listeners;
	}
	
	/**
	 * Method addListener.
	 * @param listener T
	 * @return boolean
	 */
	public <T extends Listener<Creature>> boolean addListener(T listener)
	{
		return getListeners().add(listener);
	}
	
	/**
	 * Method removeListener.
	 * @param listener T
	 * @return boolean
	 */
	public <T extends Listener<Creature>> boolean removeListener(T listener)
	{
		return getListeners().remove(listener);
	}
	
	/**
	 * Method getStatsRecorder.
	 * @return CharStatsChangeRecorder<? extends Creature>
	 */
	public CharStatsChangeRecorder<? extends Creature> getStatsRecorder()
	{
		if (_statsRecorder == null)
		{
			synchronized (this)
			{
				if (_statsRecorder == null)
				{
					_statsRecorder = new CharStatsChangeRecorder<>(this);
				}
			}
		}
		return _statsRecorder;
	}
	
	/**
	 * Method isCreature.
	 * @return boolean
	 */
	@Override
	public boolean isCreature()
	{
		return true;
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
	public void displayGiveDamageMessage(Creature target, int damage, boolean crit, boolean miss, boolean shld, boolean magic)
	{
		if (miss && target.isPlayer() && !target.isDamageBlocked())
		{
			target.sendPacket(new SystemMessage(SystemMessage.C1_HAS_EVADED_C2S_ATTACK).addName(target).addName(this));
		}
	}
	
	/**
	 * Method displayReceiveDamageMessage.
	 * @param attacker Creature
	 * @param damage int
	 */
	public void displayReceiveDamageMessage(Creature attacker, int damage)
	{
	}
	
	/**
	 * Method getSkillReuses.
	 * @return Collection<TimeStamp>
	 */
	public Collection<TimeStamp> getSkillReuses()
	{
		return _skillReuses.values();
	}
	
	/**
	 * Method getSkillReuse.
	 * @param skill Skill
	 * @return TimeStamp
	 */
	public TimeStamp getSkillReuse(Skill skill)
	{
		return _skillReuses.get(skill.hashCode());
	}
	
	/**
	 * Method isInFlyingTransform.
	 * @return boolean
	 */
	public boolean isInFlyingTransform()
	{
		return (_transformationId == 8) || (_transformationId == 9) || (_transformationId == 260);
	}

	/**
	 * Method isInMountTransform.
	 * @return boolean
	 */
	public boolean isInMountTransform()
	{
		return (_transformationId == 106) || (_transformationId == 109) || (_transformationId == 110) || (_transformationId == 20001);
	}
	
	/**
	 * Method setTransformation.
	 * @param id int
	 */
	public void setTransformation(int id)
	{
		_transformationId = id;
	}
	
	/**
	 * Method getTransformation.
	 * @return int
	 */
	public int getTransformation()
	{
		return _transformationId;
	}
	
	/**
	 * Method getTransformationName.
	 * @return String
	 */
	public String getTransformationName()
	{
		return _transformationName;
	}
	
	/**
	 * Method setTransformationName.
	 * @param name String
	 */
	public void setTransformationName(String name)
	{
		_transformationName = name;
	}
	
	/**
	 * Method setTransformationTemplate.
	 * @param template int
	 */
	public void setTransformationTemplate(int template)
	{
		_transformationTemplate = template;
	}
	
	/**
	 * Method getTransformationTemplate.
	 * @return int
	 */
	public int getTransformationTemplate()
	{
		return _transformationTemplate;
	}
	
	/**
	 * Method getHpRegen.
	 * @return double
	 */
	public double getHpRegen()
	{
		return calcStat(Stats.REGENERATE_HP_RATE, getTemplate().getBaseHpReg());
	}
	
	/**
	 * Method getMpRegen.
	 * @return double
	 */
	public double getMpRegen()
	{
		return calcStat(Stats.REGENERATE_MP_RATE, getTemplate().getBaseMpReg());
	}
	
	/**
	 * Method getCpRegen.
	 * @return double
	 */
	public double getCpRegen()
	{
		return 0.0D;
	}
	
	/**
	 * Method getColRadius.
	 * @return double
	 */
	@Override
	public double getColRadius()
	{
		return getTemplate().getCollisionRadius();
	}
	
	/**
	 * Method getColHeight.
	 * @return double
	 */
	@Override
	public double getColHeight()
	{
		return getTemplate().getCollisionHeight();
	}
	
	/**
	 * Method addDeathImmunity.
	 */
	public void addDeathImmunity()
	{
		_deathImmune = true;
	}
	
	/**
	 * Method removeDeathImmunity.
	 */
	public void removeDeathImmunity()
	{
		_deathImmune = false;
	}
	
	/**
	 * Method isDeathImmune.
	 * @return boolean
	 */
	public boolean isDeathImmune()
	{
		return _deathImmune;
	}
	
	/**
	 * Method setWalkerRouteTemplate.
	 * @param template WalkerRouteTemplate
	 */
	public void setWalkerRouteTemplate(WalkerRouteTemplate template)
	{
		_walkerRoutesTemplate = template;
	}
	
	/**
	 * Method getWalkerRouteTemplate.
	 * @return WalkerRouteTemplate
	 */
	public WalkerRouteTemplate getWalkerRouteTemplate()
	{
		return _walkerRoutesTemplate;
	}
	
	/**
	 * Method setIsEnabledDoubleCast.
	 * @param doubleCast boolean
	 */
	public void setIsEnabledDoubleCast(boolean doubleCast)
	{
		_isEnabledDoubleCast = doubleCast;
	}
	
	/**
	 * Method IsEnabledDoubleCast.
	 * @return boolean
	 */
	public boolean IsEnabledDoubleCast()
	{
		return _isEnabledDoubleCast;
	}
	
	/**
	 * Method getDefaultMaxHp.
	 * @return double
	 */
	public double getDefaultMaxHp()
	{
		return 0;
	}
	
	/**
	 * Method getDefaultMaxMp.
	 * @return double
	 */
	public double getDefaultMaxMp()
	{
		return 0;
	}

	public Collection<Summon> getPets()
	{
		return new ArrayList<Summon>(0);
	}

	public void broadcastEffectsStatusToListeners()
	{
		if(!isVehicle() && !isDoor())
		{
			final ExAbnormalStatusUpdateFromTarget exb = new ExAbnormalStatusUpdateFromTarget(this);

			if(isPlayer() && (getTarget() == this))
			{
				sendPacket(exb);
			}

			broadcastToStatusListeners(exb);
		}
	}

	@Override
	public void onActionTarget(final Player player, boolean forced)
	{
		super.onActionTarget(player, forced);

		if((player != this) && !isDoor())
		{
			validateLocation(0);
		}
	}

	@Override
	public void onActionTargeted(final Player player, boolean forced)
	{
		if(player == this)
		{
			return;
		}

		if(isAutoAttackable(player))
		{
			player.getAI().Attack(this, false, forced);
			return;
		}

		super.onActionTargeted(player, forced);
	}
}
