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
package lineage2.gameserver.ai;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ScheduledFuture;

import lineage2.commons.collections.CollectionUtils;
import lineage2.commons.collections.LazyArrayList;
import lineage2.commons.lang.reference.HardReference;
import lineage2.commons.math.random.RndSelector;
import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.Config;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.geodata.GeoEngine;
import lineage2.gameserver.model.AggroList.AggroInfo;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.MinionList;
import lineage2.gameserver.model.Playable;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.WorldRegion;
import lineage2.gameserver.model.Zone.ZoneType;
import lineage2.gameserver.model.instances.MinionInstance;
import lineage2.gameserver.model.instances.MonsterInstance;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.QuestEventType;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.MagicSkillUse;
import lineage2.gameserver.network.serverpackets.StatusUpdate;
import lineage2.gameserver.network.serverpackets.StatusUpdate.StatusUpdateField;
import lineage2.gameserver.stats.Stats;
import lineage2.gameserver.taskmanager.AiTaskManager;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.NpcUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class DefaultAI extends CharacterAI
{
	/**
	 * Field _log.
	 */
	protected static final Logger _log = LoggerFactory.getLogger(DefaultAI.class);
	
	/**
	 * @author Mobius
	 */
	public static enum TaskType
	{
		/**
		 * Field MOVE.
		 */
		MOVE,
		/**
		 * Field ATTACK.
		 */
		ATTACK,
		/**
		 * Field CAST.
		 */
		CAST,
		/**
		 * Field BUFF.
		 */
		BUFF
	}
	
	/**
	 * Field TaskDefaultWeight. (value is 10000)
	 */
	public static final int TaskDefaultWeight = 10000;
	
	/**
	 * @author Mobius
	 */
	public static class Task
	{
		/**
		 * Field type.
		 */
		public TaskType type;
		/**
		 * Field skill.
		 */
		public Skill skill;
		/**
		 * Field target.
		 */
		public HardReference<? extends Creature> target;
		/**
		 * Field loc.
		 */
		public Location loc;
		/**
		 * Field pathfind.
		 */
		public boolean pathfind;
		/**
		 * Field weight.
		 */
		public int weight = TaskDefaultWeight;
	}
	
	/**
	 * Method addTaskCast.
	 * @param target Creature
	 * @param skill Skill
	 */
	protected void addTaskCast(Creature target, Skill skill)
	{
		Task task = new Task();
		task.type = TaskType.CAST;
		task.target = target.getRef();
		task.skill = skill;
		_tasks.add(task);
		_def_think = true;
	}
	
	/**
	 * Method addTaskBuff.
	 * @param target Creature
	 * @param skill Skill
	 */
	protected void addTaskBuff(Creature target, Skill skill)
	{
		Task task = new Task();
		task.type = TaskType.BUFF;
		task.target = target.getRef();
		task.skill = skill;
		_tasks.add(task);
		_def_think = true;
	}
	
	/**
	 * Method addTaskAttack.
	 * @param target Creature
	 */
	public void addTaskAttack(Creature target)
	{
		Task task = new Task();
		task.type = TaskType.ATTACK;
		task.target = target.getRef();
		_tasks.add(task);
		_def_think = true;
	}
	
	/**
	 * Method addTaskAttack.
	 * @param target Creature
	 * @param skill Skill
	 * @param weight int
	 */
	protected void addTaskAttack(Creature target, Skill skill, int weight)
	{
		Task task = new Task();
		task.type = skill.isOffensive() ? TaskType.CAST : TaskType.BUFF;
		task.target = target.getRef();
		task.skill = skill;
		task.weight = weight;
		_tasks.add(task);
		_def_think = true;
	}
	
	/**
	 * Method addTaskMove.
	 * @param loc Location
	 * @param pathfind boolean
	 */
	public void addTaskMove(Location loc, boolean pathfind)
	{
		Task task = new Task();
		task.type = TaskType.MOVE;
		task.loc = loc;
		task.pathfind = pathfind;
		_tasks.add(task);
		_def_think = true;
	}
	
	/**
	 * Method addTaskMove.
	 * @param locX int
	 * @param locY int
	 * @param locZ int
	 * @param pathfind boolean
	 */
	protected void addTaskMove(int locX, int locY, int locZ, boolean pathfind)
	{
		addTaskMove(new Location(locX, locY, locZ), pathfind);
	}
	
	/**
	 * @author Mobius
	 */
	private static class TaskComparator implements Comparator<Task>
	{
		/**
		 * Field instance.
		 */
		private static final Comparator<Task> instance = new TaskComparator();
		
		/**
		 * Method getInstance.
		 * @return Comparator<Task>
		 */
		public static Comparator<Task> getInstance()
		{
			return instance;
		}
		
		/**
		 * Method compare.
		 * @param o1 Task
		 * @param o2 Task
		 * @return int
		 */
		@Override
		public int compare(Task o1, Task o2)
		{
			if ((o1 == null) || (o2 == null))
			{
				return 0;
			}
			return o2.weight - o1.weight;
		}
	}
	
	/**
	 * @author Mobius
	 */
	protected class Teleport extends RunnableImpl
	{
		/**
		 * Field _destination.
		 */
		Location _destination;
		
		/**
		 * Constructor for Teleport.
		 * @param destination Location
		 */
		public Teleport(Location destination)
		{
			_destination = destination;
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			NpcInstance actor = getActor();
			if (actor != null)
			{
				actor.teleToLocation(_destination);
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	protected class RunningTask extends RunnableImpl
	{
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			NpcInstance actor = getActor();
			if (actor != null)
			{
				actor.setRunning();
			}
			_runningTask = null;
		}
	}
	
	/**
	 * @author Mobius
	 */
	protected class MadnessTask extends RunnableImpl
	{
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			NpcInstance actor = getActor();
			if (actor != null)
			{
				actor.stopConfused();
			}
			_madnessTask = null;
		}
	}
	
	/**
	 * @author Mobius
	 */
	protected class NearestTargetComparator implements Comparator<Creature>
	{
		/**
		 * Field actor.
		 */
		private final Creature actor;
		
		/**
		 * Constructor for NearestTargetComparator.
		 * @param actor Creature
		 */
		public NearestTargetComparator(Creature actor)
		{
			this.actor = actor;
		}
		
		/**
		 * Method compare.
		 * @param o1 Creature
		 * @param o2 Creature
		 * @return int
		 */
		@Override
		public int compare(Creature o1, Creature o2)
		{
			double diff = actor.getDistance3D(o1) - actor.getDistance3D(o2);
			if (diff < 0)
			{
				return -1;
			}
			return diff > 0 ? 1 : 0;
		}
	}
	
	/**
	 * Field AI_TASK_ATTACK_DELAY.
	 */
	protected long AI_TASK_ATTACK_DELAY = Config.AI_TASK_ATTACK_DELAY;
	/**
	 * Field AI_TASK_ACTIVE_DELAY.
	 */
	protected long AI_TASK_ACTIVE_DELAY = Config.AI_TASK_ACTIVE_DELAY;
	/**
	 * Field AI_TASK_DELAY_CURRENT.
	 */
	protected long AI_TASK_DELAY_CURRENT = AI_TASK_ACTIVE_DELAY;
	/**
	 * Field MAX_PURSUE_RANGE.
	 */
	protected int MAX_PURSUE_RANGE;
	/**
	 * Field _aiTask.
	 */
	protected ScheduledFuture<?> _aiTask;
	/**
	 * Field _runningTask.
	 */
	protected ScheduledFuture<?> _runningTask;
	/**
	 * Field _madnessTask.
	 */
	public ScheduledFuture<?> _madnessTask;
	/**
	 * Field _thinking.
	 */
	private boolean _thinking = false;
	/**
	 * Field _def_think.
	 */
	protected boolean _def_think = false;
	/**
	 * Field _globalAggro.
	 */
	protected long _globalAggro;
	/**
	 * Field _randomAnimationEnd.
	 */
	protected long _randomAnimationEnd;
	/**
	 * Field _pathfindFails.
	 */
	protected int _pathfindFails;
	/**
	 * Field _tasks.
	 */
	protected final NavigableSet<Task> _tasks = new ConcurrentSkipListSet<>(TaskComparator.getInstance());
	/**
	 * Field _stunSkills. Field _buffSkills. Field _healSkills. Field _debuffSkills. Field _dotSkills. Field _damSkills.
	 */
	protected final Skill[] _damSkills, _dotSkills, _debuffSkills, _healSkills, _buffSkills, _stunSkills;
	/**
	 * Field _lastActiveCheck.
	 */
	protected long _lastActiveCheck;
	/**
	 * Field _checkAggroTimestamp.
	 */
	protected long _checkAggroTimestamp = 0;
	/**
	 * Field _attackTimeout.
	 */
	protected long _attackTimeout;
	/**
	 * Field _lastFactionNotifyTime.
	 */
	protected long _lastFactionNotifyTime = 0;
	/**
	 * Field _minFactionNotifyInterval.
	 */
	protected long _minFactionNotifyInterval = 10000;
	/**
	 * Field _nearestTargetComparator.
	 */
	protected final Comparator<Creature> _nearestTargetComparator;
	
	/**
	 * Constructor for DefaultAI.
	 * @param actor NpcInstance
	 */
	public DefaultAI(NpcInstance actor)
	{
		super(actor);
		setAttackTimeout(Long.MAX_VALUE);
		NpcInstance npc = getActor();
		_damSkills = npc.getTemplate().getDamageSkills();
		_dotSkills = npc.getTemplate().getDotSkills();
		_debuffSkills = npc.getTemplate().getDebuffSkills();
		_buffSkills = npc.getTemplate().getBuffSkills();
		_stunSkills = npc.getTemplate().getStunSkills();
		_healSkills = npc.getTemplate().getHealSkills();
		_nearestTargetComparator = new NearestTargetComparator(actor);
		MAX_PURSUE_RANGE = actor.getParameter("MaxPursueRange", actor.isRaid() ? Config.MAX_PURSUE_RANGE_RAID : npc.isUnderground() ? Config.MAX_PURSUE_UNDERGROUND_RANGE : Config.MAX_PURSUE_RANGE);
		_minFactionNotifyInterval = actor.getParameter("FactionNotifyInterval", 10000);
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	public void runImpl()
	{
		if (_aiTask == null)
		{
			return;
		}
		if (!isGlobalAI() && ((System.currentTimeMillis() - _lastActiveCheck) > 60000L))
		{
			_lastActiveCheck = System.currentTimeMillis();
			NpcInstance actor = getActor();
			WorldRegion region = actor == null ? null : actor.getCurrentRegion();
			if ((region == null) || !region.isActive())
			{
				stopAITask();
				return;
			}
		}
		onEvtThink();
	}
	
	/**
	 * Method startAITask.
	 */
	@Override
	public synchronized void startAITask()
	{
		if (_aiTask == null)
		{
			AI_TASK_DELAY_CURRENT = AI_TASK_ACTIVE_DELAY;
			_aiTask = AiTaskManager.getInstance().scheduleAtFixedRate(this, 0L, AI_TASK_DELAY_CURRENT);
		}
	}
	
	/**
	 * Method switchAITask.
	 * @param NEW_DELAY long
	 */
	protected synchronized void switchAITask(long NEW_DELAY)
	{
		if (_aiTask == null)
		{
			return;
		}
		if (AI_TASK_DELAY_CURRENT != NEW_DELAY)
		{
			_aiTask.cancel(false);
			AI_TASK_DELAY_CURRENT = NEW_DELAY;
			_aiTask = AiTaskManager.getInstance().scheduleAtFixedRate(this, 0L, AI_TASK_DELAY_CURRENT);
		}
	}
	
	/**
	 * Method stopAITask.
	 */
	@Override
	public final synchronized void stopAITask()
	{
		if (_aiTask != null)
		{
			_aiTask.cancel(false);
			_aiTask = null;
		}
	}
	
	/**
	 * Method canSeeInSilentMove.
	 * @param target Playable
	 * @return boolean
	 */
	protected boolean canSeeInSilentMove(Playable target)
	{
		if (getActor().getParameter("canSeeInSilentMove", false))
		{
			return true;
		}
		return !target.isSilentMoving();
	}
	
	/**
	 * Method canSeeInHide.
	 * @param target Playable
	 * @return boolean
	 */
	protected boolean canSeeInHide(Playable target)
	{
		if (getActor().getParameter("canSeeInHide", false))
		{
			return true;
		}
		return !target.isInvisible();
	}
	
	/**
	 * Method checkAggression.
	 * @param target Creature
	 * @return boolean
	 */
	protected boolean checkAggression(Creature target)
	{
		NpcInstance actor = getActor();
		if ((getIntention() != CtrlIntention.AI_INTENTION_ACTIVE) || !isGlobalAggro())
		{
			return false;
		}
		if (target.isAlikeDead())
		{
			return false;
		}
		if (target.isNpc() && target.isInvul())
		{
			return false;
		}
		if (target.isPlayable())
		{
			if (!canSeeInSilentMove((Playable) target))
			{
				return false;
			}
			if (!canSeeInHide((Playable) target))
			{
				return false;
			}
			if (actor.getFaction().getName().equalsIgnoreCase("varka_silenos_clan") && (target.getPlayer().getVarka() > 0))
			{
				return false;
			}
			if (actor.getFaction().getName().equalsIgnoreCase("ketra_orc_clan") && (target.getPlayer().getKetra() > 0))
			{
				return false;
			}
			if (target.isPlayer() && ((Player) target).isGM() && target.isInvisible())
			{
				return false;
			}
			if (((Playable) target).getNonAggroTime() > System.currentTimeMillis())
			{
				return false;
			}
			if (target.isPlayer() && !target.getPlayer().isActive())
			{
				return false;
			}
			if (actor.isMonster() && target.isInZonePeace())
			{
				return false;
			}
		}
		AggroInfo ai = actor.getAggroList().get(target);
		if ((ai != null) && (ai.hate > 0))
		{
			if (!target.isInRangeZ(actor.getSpawnedLoc(), MAX_PURSUE_RANGE))
			{
				return false;
			}
		}
		else if (!actor.isAggressive() || !target.isInRangeZ(actor.getSpawnedLoc(), actor.getAggroRange()))
		{
			return false;
		}
		if (!canAttackCharacter(target))
		{
			return false;
		}
		if (!GeoEngine.canSeeTarget(actor, target, false))
		{
			return false;
		}
		actor.getAggroList().addDamageHate(target, 0, 2);
		if ((target.isServitor() || target.isPet()))
		{
			actor.getAggroList().addDamageHate(target.getPlayer(), 0, 1);
		}
		startRunningTask(AI_TASK_ATTACK_DELAY);
		setIntention(CtrlIntention.AI_INTENTION_ATTACK, target);
		return true;
	}
	
	/**
	 * Method setIsInRandomAnimation.
	 * @param time long
	 */
	protected void setIsInRandomAnimation(long time)
	{
		_randomAnimationEnd = System.currentTimeMillis() + time;
	}
	
	/**
	 * Method randomAnimation.
	 * @return boolean
	 */
	protected boolean randomAnimation()
	{
		NpcInstance actor = getActor();
		if (actor.getParameter("noRandomAnimation", false))
		{
			return false;
		}
		if (actor.hasRandomAnimation() && !actor.isActionsDisabled() && !actor.isMoving && !actor.isInCombat() && Rnd.chance(Config.RND_ANIMATION_RATE))
		{
			setIsInRandomAnimation(3000);
			actor.onRandomAnimation();
			return true;
		}
		return false;
	}
	
	/**
	 * Method randomWalk.
	 * @return boolean
	 */
	protected boolean randomWalk()
	{
		NpcInstance actor = getActor();
		if (actor.getParameter("noRandomWalk", false))
		{
			return false;
		}
		return !actor.isMoving && maybeMoveToHome();
	}
	
	/**
	 * Method thinkActive.
	 * @return boolean
	 */
	protected boolean thinkActive()
	{
		NpcInstance actor = getActor();
		if (actor.isActionsDisabled())
		{
			return true;
		}
		if (_randomAnimationEnd > System.currentTimeMillis())
		{
			return true;
		}
		if (_def_think)
		{
			if (doTask())
			{
				clearTasks();
			}
			return true;
		}
		long now = System.currentTimeMillis();
		if ((now - _checkAggroTimestamp) > Config.AGGRO_CHECK_INTERVAL)
		{
			_checkAggroTimestamp = now;
			boolean aggressive = Rnd.chance(actor.getParameter("SelfAggressive", actor.isAggressive() ? 100 : 0));
			if (!actor.getAggroList().isEmpty() || aggressive)
			{
				List<Creature> chars = World.getAroundCharacters(actor);
				CollectionUtils.eqSort(chars, _nearestTargetComparator);
				for (Creature cha : chars)
				{
					if (aggressive || (actor.getAggroList().get(cha) != null))
					{
						if (checkAggression(cha))
						{
							return true;
						}
					}
				}
			}
		}
		if (actor.isMinion())
		{
			MonsterInstance leader = ((MinionInstance) actor).getLeader();
			if (leader != null)
			{
				double distance = actor.getDistance(leader.getX(), leader.getY());
				if (distance > 1000)
				{
					actor.teleToLocation(leader.getMinionPosition());
				}
				else if (distance > 200)
				{
					addTaskMove(leader.getMinionPosition(), false);
				}
				return true;
			}
		}
		if (randomAnimation())
		{
			return true;
		}
		if (randomWalk())
		{
			return true;
		}
		return false;
	}
	
	/**
	 * Method onIntentionIdle.
	 */
	@Override
	protected void onIntentionIdle()
	{
		NpcInstance actor = getActor();
		clearTasks();
		actor.stopMove();
		actor.getAggroList().clear(true);
		setAttackTimeout(Long.MAX_VALUE);
		setAttackTarget(null);
		changeIntention(CtrlIntention.AI_INTENTION_IDLE, null, null);
	}
	
	/**
	 * Method onIntentionActive.
	 */
	@Override
	protected void onIntentionActive()
	{
		NpcInstance actor = getActor();
		actor.stopMove();
		setAttackTimeout(Long.MAX_VALUE);
		if (getIntention() != CtrlIntention.AI_INTENTION_ACTIVE)
		{
			switchAITask(AI_TASK_ACTIVE_DELAY);
			changeIntention(CtrlIntention.AI_INTENTION_ACTIVE, null, null);
		}
		onEvtThink();
	}
	
	/**
	 * Method onIntentionAttack.
	 * @param target Creature
	 */
	@Override
	protected void onIntentionAttack(Creature target)
	{
		NpcInstance actor = getActor();
		clearTasks();
		actor.stopMove();
		setAttackTarget(target);
		setAttackTimeout(getMaxAttackTimeout() + System.currentTimeMillis());
		setGlobalAggro(0);
		if (getIntention() != CtrlIntention.AI_INTENTION_ATTACK)
		{
			changeIntention(CtrlIntention.AI_INTENTION_ATTACK, target, null);
			switchAITask(AI_TASK_ATTACK_DELAY);
		}
		onEvtThink();
	}
	
	/**
	 * Method canAttackCharacter.
	 * @param target Creature
	 * @return boolean
	 */
	protected boolean canAttackCharacter(Creature target)
	{
		return target.isPlayable();
	}
	
	/**
	 * Method checkTarget.
	 * @param target Creature
	 * @param range int
	 * @return boolean
	 */
	protected boolean checkTarget(Creature target, int range)
	{
		NpcInstance actor = getActor();
		if ((target == null) || target.isAlikeDead() || !actor.isInRangeZ(target, range))
		{
			return false;
		}
		final boolean hided = target.isPlayable() && !canSeeInHide((Playable) target);
		if (!hided && actor.isConfused())
		{
			return true;
		}
		if (getIntention() == CtrlIntention.AI_INTENTION_ATTACK)
		{
			AggroInfo ai = actor.getAggroList().get(target);
			if (ai != null)
			{
				if (hided)
				{
					ai.hate = 0;
					return false;
				}
				return ai.hate > 0;
			}
			return false;
		}
		return canAttackCharacter(target);
	}
	
	/**
	 * Method setAttackTimeout.
	 * @param time long
	 */
	public void setAttackTimeout(long time)
	{
		_attackTimeout = time;
	}
	
	/**
	 * Method getAttackTimeout.
	 * @return long
	 */
	protected long getAttackTimeout()
	{
		return _attackTimeout;
	}
	
	/**
	 * Method thinkAttack.
	 * @param returnHome boolean
	 */
	protected void thinkAttack(boolean returnHome)
	{
		NpcInstance actor = getActor();
		if (actor.isDead())
		{
			return;
		}
		if (doTask() && !actor.isAttackingNow() && !actor.isCastingNow())
		{
			if (!createNewTask())
			{
				if (System.currentTimeMillis() > getAttackTimeout())
				{
					returnHome();
				}
			}
		}
	}
	
	/**
	 * Method thinkAttack.
	 */
	protected void thinkAttack()
	{
		NpcInstance actor = getActor();
		if (actor.isDead())
		{
			return;
		}
		Location loc = actor.getSpawnedLoc();
		if (!actor.isInRange(loc, MAX_PURSUE_RANGE))
		{
			teleportHome();
			return;
		}
		if (doTask() && !actor.isAttackingNow() && !actor.isCastingNow())
		{
			if (!createNewTask())
			{
				if (System.currentTimeMillis() > getAttackTimeout())
				{
					returnHome();
				}
			}
		}
	}
	
	/**
	 * Method onEvtSpawn.
	 */
	@Override
	protected void onEvtSpawn()
	{
		setGlobalAggro(System.currentTimeMillis() + getActor().getParameter("globalAggro", 10000L));
		setIntention(CtrlIntention.AI_INTENTION_ACTIVE);
	}
	
	/**
	 * Method onEvtReadyToAct.
	 */
	@Override
	protected void onEvtReadyToAct()
	{
		onEvtThink();
	}
	
	/**
	 * Method onEvtArrivedTarget.
	 */
	@Override
	protected void onEvtArrivedTarget()
	{
		onEvtThink();
	}
	
	/**
	 * Method onEvtArrived.
	 */
	@Override
	protected void onEvtArrived()
	{
		onEvtThink();
	}
	
	/**
	 * Method tryMoveToTarget.
	 * @param target Creature
	 * @return boolean
	 */
	protected boolean tryMoveToTarget(Creature target)
	{
		return tryMoveToTarget(target, 0);
	}
	
	/**
	 * Method tryMoveToTarget.
	 * @param target Creature
	 * @param range int
	 * @return boolean
	 */
	protected boolean tryMoveToTarget(Creature target, int range)
	{
		NpcInstance actor = getActor();
		if (!actor.followToCharacter(target, actor.getPhysicalAttackRange(), true))
		{
			_pathfindFails++;
		}
		if ((_pathfindFails >= getMaxPathfindFails()) && (System.currentTimeMillis() > ((getAttackTimeout() - getMaxAttackTimeout()) + getTeleportTimeout())) && actor.isInRange(target, MAX_PURSUE_RANGE))
		{
			_pathfindFails = 0;
			if (target.isPlayable())
			{
				AggroInfo hate = actor.getAggroList().get(target);
				if ((hate == null) || (hate.hate < 100))
				{
					returnHome();
					return false;
				}
			}
			Location loc = GeoEngine.moveCheckForAI(target.getLoc(), actor.getLoc(), actor.getGeoIndex());
			if (!GeoEngine.canMoveToCoord(actor.getX(), actor.getY(), actor.getZ(), loc.x, loc.y, loc.z, actor.getGeoIndex()))
			{
				loc = target.getLoc();
			}
			actor.teleToLocation(loc);
		}
		return true;
	}
	
	/**
	 * Method maybeNextTask.
	 * @param currentTask Task
	 * @return boolean
	 */
	protected boolean maybeNextTask(Task currentTask)
	{
		_tasks.remove(currentTask);
		if (_tasks.size() == 0)
		{
			return true;
		}
		return false;
	}
	
	/**
	 * Method doTask.
	 * @return boolean
	 */
	protected boolean doTask()
	{
		NpcInstance actor = getActor();
		if (!_def_think)
		{
			return true;
		}
		Task currentTask = _tasks.pollFirst();
		if (currentTask == null)
		{
			clearTasks();
			return true;
		}
		if (actor.isDead() || actor.isAttackingNow() || actor.isCastingNow())
		{
			return false;
		}
		switch (currentTask.type)
		{
			case MOVE:
			{
				if (actor.isMovementDisabled() || !getIsMobile())
				{
					return true;
				}
				if (actor.isInRange(currentTask.loc, 100))
				{
					return maybeNextTask(currentTask);
				}
				if (actor.isMoving)
				{
					return false;
				}
				if (!actor.moveToLocation(currentTask.loc, 0, currentTask.pathfind))
				{
					clientStopMoving();
					_pathfindFails = 0;
					actor.teleToLocation(currentTask.loc);
					return maybeNextTask(currentTask);
				}
			}
				break;
			case ATTACK:
			{
				Creature target = currentTask.target.get();
				if (!checkTarget(target, MAX_PURSUE_RANGE))
				{
					return true;
				}
				setAttackTarget(target);
				if (actor.isMoving)
				{
					return Rnd.chance(25);
				}
				if ((actor.getRealDistance3D(target) <= (actor.getPhysicalAttackRange() + 40)) && GeoEngine.canSeeTarget(actor, target, false))
				{
					clientStopMoving();
					_pathfindFails = 0;
					setAttackTimeout(getMaxAttackTimeout() + System.currentTimeMillis());
					actor.doAttack(target);
					return maybeNextTask(currentTask);
				}
				if (actor.isMovementDisabled() || !getIsMobile())
				{
					return true;
				}
				tryMoveToTarget(target);
			}
				break;
			case CAST:
			{
				Creature target = currentTask.target.get();
				if (actor.isMuted(currentTask.skill) || actor.isSkillDisabled(currentTask.skill) || actor.isUnActiveSkill(currentTask.skill.getId()))
				{
					return true;
				}
				boolean isAoE = currentTask.skill.getTargetType() == Skill.SkillTargetType.TARGET_AURA;
				int castRange = currentTask.skill.getAOECastRange();
				if (!checkTarget(target, MAX_PURSUE_RANGE + castRange))
				{
					return true;
				}
				setAttackTarget(target);
				if ((actor.getRealDistance3D(target) <= (castRange + 60)) && GeoEngine.canSeeTarget(actor, target, false))
				{
					clientStopMoving();
					_pathfindFails = 0;
					setAttackTimeout(getMaxAttackTimeout() + System.currentTimeMillis());
					actor.doCast(currentTask.skill, isAoE ? actor : target, !target.isPlayable());
					return maybeNextTask(currentTask);
				}
				if (actor.isMoving)
				{
					return Rnd.chance(10);
				}
				if (actor.isMovementDisabled() || !getIsMobile())
				{
					return true;
				}
				tryMoveToTarget(target, castRange);
			}
				break;
			case BUFF:
			{
				Creature target = currentTask.target.get();
				if (actor.isMuted(currentTask.skill) || actor.isSkillDisabled(currentTask.skill) || actor.isUnActiveSkill(currentTask.skill.getId()))
				{
					return true;
				}
				if ((target == null) || target.isAlikeDead() || !actor.isInRange(target, 2000))
				{
					return true;
				}
				boolean isAoE = currentTask.skill.getTargetType() == Skill.SkillTargetType.TARGET_AURA;
				int castRange = currentTask.skill.getAOECastRange();
				if (actor.isMoving)
				{
					return Rnd.chance(10);
				}
				if ((actor.getRealDistance3D(target) <= (castRange + 60)) && GeoEngine.canSeeTarget(actor, target, false))
				{
					clientStopMoving();
					_pathfindFails = 0;
					actor.doCast(currentTask.skill, isAoE ? actor : target, !target.isPlayable());
					return maybeNextTask(currentTask);
				}
				if (actor.isMovementDisabled() || !getIsMobile())
				{
					return true;
				}
				tryMoveToTarget(target);
			}
				break;
		}
		return false;
	}
	
	/**
	 * Method createNewTask.
	 * @return boolean
	 */
	protected boolean createNewTask()
	{
		return false;
	}
	
	/**
	 * Method defaultNewTask.
	 * @return boolean
	 */
	protected boolean defaultNewTask()
	{
		clearTasks();
		NpcInstance actor = getActor();
		Creature target;
		if ((actor == null) || ((target = prepareTarget()) == null))
		{
			return false;
		}
		double distance = actor.getDistance(target);
		return chooseTaskAndTargets(null, target, distance);
	}
	
	/**
	 * Method onEvtThink.
	 */
	@Override
	protected void onEvtThink()
	{
		NpcInstance actor = getActor();
		if (_thinking || (actor == null) || actor.isActionsDisabled() || actor.isAfraid())
		{
			return;
		}
		if (_randomAnimationEnd > System.currentTimeMillis())
		{
			return;
		}
		if (actor.isRaid() && (actor.isInZonePeace() || actor.isInZoneBattle() || actor.isInZone(ZoneType.SIEGE)))
		{
			teleportHome();
			return;
		}
		_thinking = true;
		try
		{
			if (!Config.BLOCK_ACTIVE_TASKS && (getIntention() == CtrlIntention.AI_INTENTION_ACTIVE))
			{
				thinkActive();
			}
			else if (getIntention() == CtrlIntention.AI_INTENTION_ATTACK)
			{
				thinkAttack();
			}
		}
		finally
		{
			_thinking = false;
		}
	}
	
	/**
	 * Method onEvtDead.
	 * @param killer Creature
	 */
	@Override
	protected void onEvtDead(Creature killer)
	{
		NpcInstance actor = getActor();
		int transformer = actor.getParameter("transformOnDead", 0);
		int chance = actor.getParameter("transformChance", 100);
		if ((transformer > 0) && Rnd.chance(chance))
		{
			NpcInstance npc = NpcUtils.spawnSingle(transformer, actor.getLoc(), actor.getReflection());
			if ((killer != null) && killer.isPlayable())
			{
				npc.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, killer, 100);
				killer.setTarget(npc);
				killer.sendPacket(new StatusUpdate(npc).addAttribute(StatusUpdateField.CUR_HP, StatusUpdateField.MAX_HP));
			}
		}
		super.onEvtDead(killer);
	}
	
	/**
	 * Method onEvtClanAttacked.
	 * @param attacked Creature
	 * @param attacker Creature
	 * @param damage int
	 */
	@Override
	protected void onEvtClanAttacked(Creature attacked, Creature attacker, int damage)
	{
		if ((getIntention() != CtrlIntention.AI_INTENTION_ACTIVE) || !isGlobalAggro())
		{
			return;
		}
		notifyEvent(CtrlEvent.EVT_AGGRESSION, attacker, 2);
	}
	
	/**
	 * Method onEvtAttacked.
	 * @param attacker Creature
	 * @param damage int
	 */
	@Override
	protected void onEvtAttacked(Creature attacker, int damage)
	{
		NpcInstance actor = getActor();
		if ((attacker == null) || actor.isDead())
		{
			return;
		}
		int transformer = actor.getParameter("transformOnUnderAttack", 0);
		if (transformer > 0)
		{
			int chance = actor.getParameter("transformChance", 5);
			if ((chance == 100) || ((((MonsterInstance) actor).getChampion() == 0) && (actor.getCurrentHpPercents() > 50) && Rnd.chance(chance)))
			{
				MonsterInstance npc = (MonsterInstance) NpcHolder.getInstance().getTemplate(transformer).getNewInstance();
				npc.setSpawnedLoc(actor.getLoc());
				npc.setReflection(actor.getReflection());
				npc.setChampion(((MonsterInstance) actor).getChampion());
				npc.setCurrentHpMp(npc.getMaxHp(), npc.getMaxMp(), true);
				npc.spawnMe(npc.getSpawnedLoc());
				npc.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, attacker, 100);
				actor.doDie(actor);
				actor.decayMe();
				attacker.setTarget(npc);
				attacker.sendPacket(new StatusUpdate(npc).addAttribute(StatusUpdateField.CUR_HP, StatusUpdateField.MAX_HP));
				return;
			}
		}
		Player player = attacker.getPlayer();
		if (player != null)
		{
			List<QuestState> quests = player.getQuestsForEvent(actor, QuestEventType.ATTACKED_WITH_QUEST, false);
			if (quests != null)
			{
				for (QuestState qs : quests)
				{
					qs.getQuest().notifyAttack(actor, qs);
				}
			}
		}
		actor.getAggroList().addDamageHate(attacker, 0, damage);
		if ((damage > 0) && (attacker.isServitor() || attacker.isPet()))
		{
			actor.getAggroList().addDamageHate(attacker.getPlayer(), 0, actor.getParameter("searchingMaster", false) ? damage : 1);
		}
		if (getIntention() != CtrlIntention.AI_INTENTION_ATTACK)
		{
			if (!actor.isRunning())
			{
				startRunningTask(AI_TASK_ATTACK_DELAY);
			}
			setIntention(CtrlIntention.AI_INTENTION_ATTACK, attacker);
		}
		notifyFriends(attacker, damage);
	}
	
	/**
	 * Method onEvtAggression.
	 * @param attacker Creature
	 * @param aggro int
	 */
	@Override
	protected void onEvtAggression(Creature attacker, int aggro)
	{
		NpcInstance actor = getActor();
		if ((attacker == null) || actor.isDead())
		{
			return;
		}
		actor.getAggroList().addDamageHate(attacker, 0, aggro);
		if ((aggro > 0) && (attacker.isServitor() || attacker.isPet()))
		{
			actor.getAggroList().addDamageHate(attacker.getPlayer(), 0, actor.getParameter("searchingMaster", false) ? aggro : 1);
		}
		if (getIntention() != CtrlIntention.AI_INTENTION_ATTACK)
		{
			if (!actor.isRunning())
			{
				startRunningTask(AI_TASK_ATTACK_DELAY);
			}
			setIntention(CtrlIntention.AI_INTENTION_ATTACK, attacker);
		}
	}
	
	/**
	 * Method maybeMoveToHome.
	 * @return boolean
	 */
	protected boolean maybeMoveToHome()
	{
		NpcInstance actor = getActor();
		if (actor.isDead())
		{
			return false;
		}
		boolean randomWalk = actor.hasRandomWalk();
		Location sloc = actor.getSpawnedLoc();
		if (randomWalk && (!Config.RND_WALK || !Rnd.chance(Config.RND_WALK_RATE)))
		{
			return false;
		}
		boolean isInRange = actor.isInRangeZ(sloc, Config.MAX_DRIFT_RANGE);
		if (!randomWalk && isInRange)
		{
			return false;
		}
		Location pos = Location.findPointToStay(actor, sloc, 0, Config.MAX_DRIFT_RANGE);
		actor.setWalking();
		if (!actor.moveToLocation(pos.x, pos.y, pos.z, 0, true) && !isInRange)
		{
			teleportHome();
		}
		return true;
	}
	
	/**
	 * Method returnHome.
	 */
	protected void returnHome()
	{
		returnHome(true, Config.ALWAYS_TELEPORT_HOME);
	}
	
	/**
	 * Method teleportHome.
	 */
	protected void teleportHome()
	{
		returnHome(true, true);
	}
	
	/**
	 * Method returnHome.
	 * @param clearAggro boolean
	 * @param teleport boolean
	 */
	protected void returnHome(boolean clearAggro, boolean teleport)
	{
		NpcInstance actor = getActor();
		Location sloc = actor.getSpawnedLoc();
		clearTasks();
		actor.stopMove();
		if (clearAggro)
		{
			actor.getAggroList().clear(true);
		}
		setAttackTimeout(Long.MAX_VALUE);
		setAttackTarget(null);
		changeIntention(CtrlIntention.AI_INTENTION_ACTIVE, null, null);
		if (teleport)
		{
			actor.broadcastPacketToOthers(new MagicSkillUse(actor, actor, 2036, 1, 500, 0));
			actor.teleToLocation(sloc.x, sloc.y, GeoEngine.getHeight(sloc, actor.getGeoIndex()));
		}
		else
		{
			if (!clearAggro)
			{
				actor.setRunning();
			}
			else
			{
				actor.setWalking();
			}
			addTaskMove(sloc, false);
		}
	}
	
	/**
	 * Method prepareTarget.
	 * @return Creature
	 */
	protected Creature prepareTarget()
	{
		NpcInstance actor = getActor();
		if (actor.isConfused())
		{
			return getAttackTarget();
		}
		if (Rnd.chance(actor.getParameter("isMadness", 0)))
		{
			Creature randomHated = actor.getAggroList().getRandomHated();
			if (randomHated != null)
			{
				setAttackTarget(randomHated);
				if ((_madnessTask == null) && !actor.isConfused())
				{
					actor.startConfused();
					_madnessTask = ThreadPoolManager.getInstance().schedule(new MadnessTask(), 10000);
				}
				return randomHated;
			}
		}
		List<Creature> hateList = actor.getAggroList().getHateList();
		Creature hated = null;
		for (Creature cha : hateList)
		{
			if (!checkTarget(cha, MAX_PURSUE_RANGE))
			{
				actor.getAggroList().remove(cha, true);
				continue;
			}
			hated = cha;
			break;
		}
		if (hated != null)
		{
			setAttackTarget(hated);
			return hated;
		}
		return null;
	}
	
	/**
	 * Method canUseSkill.
	 * @param skill Skill
	 * @param target Creature
	 * @param distance double
	 * @return boolean
	 */
	protected boolean canUseSkill(Skill skill, Creature target, double distance)
	{
		NpcInstance actor = getActor();
		if ((skill == null) || skill.isNotUsedByAI())
		{
			return false;
		}
		if ((skill.getTargetType() == Skill.SkillTargetType.TARGET_SELF) && (target != actor))
		{
			return false;
		}
		int castRange = skill.getAOECastRange();
		if ((castRange <= 200) && (distance > 200))
		{
			return false;
		}
		if (actor.isSkillDisabled(skill) || actor.isMuted(skill) || actor.isUnActiveSkill(skill.getId()))
		{
			return false;
		}
		double mpConsume2 = skill.getMpConsume2();
		if (skill.isMagic())
		{
			mpConsume2 = actor.calcStat(Stats.MP_MAGIC_SKILL_CONSUME, mpConsume2, target, skill);
		}
		else
		{
			mpConsume2 = actor.calcStat(Stats.MP_PHYSICAL_SKILL_CONSUME, mpConsume2, target, skill);
		}
		if (actor.getCurrentMp() < mpConsume2)
		{
			return false;
		}
		if (target.getEffectList().getEffectsCountForSkill(skill.getId()) != 0)
		{
			return false;
		}
		return true;
	}
	
	/**
	 * Method canUseSkill.
	 * @param sk Skill
	 * @param target Creature
	 * @return boolean
	 */
	protected boolean canUseSkill(Skill sk, Creature target)
	{
		return canUseSkill(sk, target, 0);
	}
	
	/**
	 * Method selectUsableSkills.
	 * @param target Creature
	 * @param distance double
	 * @param skills Skill[]
	 * @return Skill[]
	 */
	protected Skill[] selectUsableSkills(Creature target, double distance, Skill[] skills)
	{
		if ((skills == null) || (skills.length == 0) || (target == null))
		{
			return null;
		}
		Skill[] ret = null;
		int usable = 0;
		for (Skill skill : skills)
		{
			if (canUseSkill(skill, target, distance))
			{
				if (ret == null)
				{
					ret = new Skill[skills.length];
				}
				ret[usable++] = skill;
			}
		}
		if ((ret == null) || (usable == skills.length))
		{
			return ret;
		}
		if (usable == 0)
		{
			return null;
		}
		ret = Arrays.copyOf(ret, usable);
		return ret;
	}
	
	/**
	 * Method selectTopSkillByDamage.
	 * @param actor Creature
	 * @param target Creature
	 * @param distance double
	 * @param skills Skill[]
	 * @return Skill
	 */
	protected static Skill selectTopSkillByDamage(Creature actor, Creature target, double distance, Skill[] skills)
	{
		if ((skills == null) || (skills.length == 0))
		{
			return null;
		}
		if (skills.length == 1)
		{
			return skills[0];
		}
		RndSelector<Skill> rnd = new RndSelector<>(skills.length);
		double weight;
		for (Skill skill : skills)
		{
			weight = (skill.getSimpleDamage(actor, target) * skill.getAOECastRange()) / distance;
			if (weight < 1.)
			{
				weight = 1.;
			}
			rnd.add(skill, (int) weight);
		}
		return rnd.select();
	}
	
	/**
	 * Method selectTopSkillByDebuff.
	 * @param actor Creature
	 * @param target Creature
	 * @param distance double
	 * @param skills Skill[]
	 * @return Skill
	 */
	protected static Skill selectTopSkillByDebuff(Creature actor, Creature target, double distance, Skill[] skills)
	{
		if ((skills == null) || (skills.length == 0))
		{
			return null;
		}
		if (skills.length == 1)
		{
			return skills[0];
		}
		RndSelector<Skill> rnd = new RndSelector<>(skills.length);
		double weight;
		for (Skill skill : skills)
		{
			if (skill.getSameByStackType(target) != null)
			{
				continue;
			}
			if ((weight = (100. * skill.getAOECastRange()) / distance) <= 0)
			{
				weight = 1;
			}
			rnd.add(skill, (int) weight);
		}
		return rnd.select();
	}
	
	/**
	 * Method selectTopSkillByBuff.
	 * @param target Creature
	 * @param skills Skill[]
	 * @return Skill
	 */
	protected static Skill selectTopSkillByBuff(Creature target, Skill[] skills)
	{
		if ((skills == null) || (skills.length == 0))
		{
			return null;
		}
		if (skills.length == 1)
		{
			return skills[0];
		}
		RndSelector<Skill> rnd = new RndSelector<>(skills.length);
		double weight;
		for (Skill skill : skills)
		{
			if (skill.getSameByStackType(target) != null)
			{
				continue;
			}
			if ((weight = skill.getPower()) <= 0)
			{
				weight = 1;
			}
			rnd.add(skill, (int) weight);
		}
		return rnd.select();
	}
	
	/**
	 * Method selectTopSkillByHeal.
	 * @param target Creature
	 * @param skills Skill[]
	 * @return Skill
	 */
	protected static Skill selectTopSkillByHeal(Creature target, Skill[] skills)
	{
		if ((skills == null) || (skills.length == 0))
		{
			return null;
		}
		double hpReduced = target.getMaxHp() - target.getCurrentHp();
		if (hpReduced < 1)
		{
			return null;
		}
		if (skills.length == 1)
		{
			return skills[0];
		}
		RndSelector<Skill> rnd = new RndSelector<>(skills.length);
		double weight;
		for (Skill skill : skills)
		{
			if ((weight = Math.abs(skill.getPower() - hpReduced)) <= 0)
			{
				weight = 1;
			}
			rnd.add(skill, (int) weight);
		}
		return rnd.select();
	}
	
	/**
	 * Method addDesiredSkill.
	 * @param skillMap Map<Skill,Integer>
	 * @param target Creature
	 * @param distance double
	 * @param skills Skill[]
	 */
	protected void addDesiredSkill(Map<Skill, Integer> skillMap, Creature target, double distance, Skill[] skills)
	{
		if ((skills == null) || (skills.length == 0) || (target == null))
		{
			return;
		}
		for (Skill sk : skills)
		{
			addDesiredSkill(skillMap, target, distance, sk);
		}
	}
	
	/**
	 * Method addDesiredSkill.
	 * @param skillMap Map<Skill,Integer>
	 * @param target Creature
	 * @param distance double
	 * @param skill Skill
	 */
	protected void addDesiredSkill(Map<Skill, Integer> skillMap, Creature target, double distance, Skill skill)
	{
		if ((skill == null) || (target == null) || !canUseSkill(skill, target))
		{
			return;
		}
		int weight = (int) -Math.abs(skill.getAOECastRange() - distance);
		if (skill.getAOECastRange() >= distance)
		{
			weight += 1000000;
		}
		else if (skill.isNotTargetAoE() && (skill.getTargets(getActor(), target, false).size() == 0))
		{
			return;
		}
		skillMap.put(skill, weight);
	}
	
	/**
	 * Method addDesiredHeal.
	 * @param skillMap Map<Skill,Integer>
	 * @param skills Skill[]
	 */
	protected void addDesiredHeal(Map<Skill, Integer> skillMap, Skill[] skills)
	{
		if ((skills == null) || (skills.length == 0))
		{
			return;
		}
		NpcInstance actor = getActor();
		double hpReduced = actor.getMaxHp() - actor.getCurrentHp();
		double hpPercent = actor.getCurrentHpPercents();
		if (hpReduced < 1)
		{
			return;
		}
		int weight;
		for (Skill sk : skills)
		{
			if (canUseSkill(sk, actor) && (sk.getPower() <= hpReduced))
			{
				weight = (int) sk.getPower();
				if (hpPercent < 50)
				{
					weight += 1000000;
				}
				skillMap.put(sk, weight);
			}
		}
	}
	
	/**
	 * Method addDesiredBuff.
	 * @param skillMap Map<Skill,Integer>
	 * @param skills Skill[]
	 */
	protected void addDesiredBuff(Map<Skill, Integer> skillMap, Skill[] skills)
	{
		if ((skills == null) || (skills.length == 0))
		{
			return;
		}
		NpcInstance actor = getActor();
		for (Skill sk : skills)
		{
			if (canUseSkill(sk, actor))
			{
				skillMap.put(sk, 1000000);
			}
		}
	}
	
	/**
	 * Method selectTopSkill.
	 * @param skillMap Map<Skill,Integer>
	 * @return Skill
	 */
	protected Skill selectTopSkill(Map<Skill, Integer> skillMap)
	{
		if ((skillMap == null) || skillMap.isEmpty())
		{
			return null;
		}
		int nWeight, topWeight = Integer.MIN_VALUE;
		for (Skill next : skillMap.keySet())
		{
			if ((nWeight = skillMap.get(next)) > topWeight)
			{
				topWeight = nWeight;
			}
		}
		if (topWeight == Integer.MIN_VALUE)
		{
			return null;
		}
		Skill[] skills = new Skill[skillMap.size()];
		nWeight = 0;
		for (Map.Entry<Skill, Integer> e : skillMap.entrySet())
		{
			if (e.getValue() < topWeight)
			{
				continue;
			}
			skills[nWeight++] = e.getKey();
		}
		return skills[Rnd.get(nWeight)];
	}
	
	/**
	 * Method chooseTaskAndTargets.
	 * @param skill Skill
	 * @param target Creature
	 * @param distance double
	 * @return boolean
	 */
	protected boolean chooseTaskAndTargets(Skill skill, Creature target, double distance)
	{
		NpcInstance actor = getActor();
		if (skill != null)
		{
			if (actor.isMovementDisabled() && (distance > (skill.getAOECastRange() + 60)))
			{
				target = null;
				if (skill.isOffensive())
				{
					LazyArrayList<Creature> targets = LazyArrayList.newInstance();
					for (Creature cha : actor.getAggroList().getHateList())
					{
						if (!checkTarget(cha, skill.getAOECastRange() + 60) || !canUseSkill(skill, cha))
						{
							continue;
						}
						targets.add(cha);
					}
					if (!targets.isEmpty())
					{
						target = targets.get(Rnd.get(targets.size()));
					}
					LazyArrayList.recycle(targets);
				}
			}
			if (target == null)
			{
				return false;
			}
			if (skill.isOffensive())
			{
				addTaskCast(target, skill);
			}
			else
			{
				addTaskBuff(target, skill);
			}
			return true;
		}
		if (actor.isMovementDisabled() && (distance > (actor.getPhysicalAttackRange() + 40)))
		{
			target = null;
			LazyArrayList<Creature> targets = LazyArrayList.newInstance();
			for (Creature cha : actor.getAggroList().getHateList())
			{
				if (!checkTarget(cha, actor.getPhysicalAttackRange() + 40))
				{
					continue;
				}
				targets.add(cha);
			}
			if (!targets.isEmpty())
			{
				target = targets.get(Rnd.get(targets.size()));
			}
			LazyArrayList.recycle(targets);
		}
		if (target == null)
		{
			return false;
		}
		addTaskAttack(target);
		return true;
	}
	
	/**
	 * Method isActive.
	 * @return boolean
	 */
	@Override
	public boolean isActive()
	{
		return _aiTask != null;
	}
	
	/**
	 * Method clearTasks.
	 */
	protected void clearTasks()
	{
		_def_think = false;
		_tasks.clear();
	}
	
	/**
	 * Method startRunningTask.
	 * @param interval long
	 */
	protected void startRunningTask(long interval)
	{
		NpcInstance actor = getActor();
		if ((actor != null) && (_runningTask == null) && !actor.isRunning())
		{
			_runningTask = ThreadPoolManager.getInstance().schedule(new RunningTask(), interval);
		}
	}
	
	/**
	 * Method isGlobalAggro.
	 * @return boolean
	 */
	protected boolean isGlobalAggro()
	{
		if (_globalAggro == 0)
		{
			return true;
		}
		if (_globalAggro <= System.currentTimeMillis())
		{
			_globalAggro = 0;
			return true;
		}
		return false;
	}
	
	/**
	 * Method setGlobalAggro.
	 * @param value long
	 */
	public void setGlobalAggro(long value)
	{
		_globalAggro = value;
	}
	
	/**
	 * Method getActor.
	 * @return NpcInstance
	 */
	@Override
	public NpcInstance getActor()
	{
		return (NpcInstance) super.getActor();
	}
	
	/**
	 * Method defaultThinkBuff.
	 * @param rateSelf int
	 * @return boolean
	 */
	protected boolean defaultThinkBuff(int rateSelf)
	{
		return defaultThinkBuff(rateSelf, 0);
	}
	
	/**
	 * Method notifyFriends.
	 * @param attacker Creature
	 * @param damage int
	 */
	protected void notifyFriends(Creature attacker, int damage)
	{
		NpcInstance actor = getActor();
		if ((System.currentTimeMillis() - _lastFactionNotifyTime) > _minFactionNotifyInterval)
		{
			_lastFactionNotifyTime = System.currentTimeMillis();
			if (actor.isMinion())
			{
				MonsterInstance master = ((MinionInstance) actor).getLeader();
				if (master != null)
				{
					if (!master.isDead() && master.isVisible())
					{
						master.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, attacker, damage);
					}
					MinionList minionList = master.getMinionList();
					if (minionList != null)
					{
						for (MinionInstance minion : minionList.getAliveMinions())
						{
							if (minion != actor)
							{
								minion.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, attacker, damage);
							}
						}
					}
				}
			}
			MinionList minionList = actor.getMinionList();
			if ((minionList != null) && minionList.hasAliveMinions())
			{
				for (MinionInstance minion : minionList.getAliveMinions())
				{
					minion.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, attacker, damage);
				}
			}
			for (NpcInstance npc : activeFactionTargets())
			{
				npc.getAI().notifyEvent(CtrlEvent.EVT_CLAN_ATTACKED, actor, attacker, damage);
			}
		}
	}
	
	/**
	 * Method activeFactionTargets.
	 * @return List<NpcInstance>
	 */
	protected List<NpcInstance> activeFactionTargets()
	{
		NpcInstance actor = getActor();
		if (actor.getFaction().isNone())
		{
			return Collections.emptyList();
		}
		List<NpcInstance> npcFriends = new LazyArrayList<>();
		for (NpcInstance npc : World.getAroundNpc(actor))
		{
			if (!npc.isDead())
			{
				if (npc.isInFaction(actor))
				{
					if (npc.isInRangeZ(actor, npc.getFaction().getRange()))
					{
						if (GeoEngine.canSeeTarget(npc, actor, false))
						{
							npcFriends.add(npc);
						}
					}
				}
			}
		}
		return npcFriends;
	}
	
	/**
	 * Method defaultThinkBuff.
	 * @param rateSelf int
	 * @param rateFriends int
	 * @return boolean
	 */
	protected boolean defaultThinkBuff(int rateSelf, int rateFriends)
	{
		NpcInstance actor = getActor();
		if (actor.isDead())
		{
			return true;
		}
		if (Rnd.chance(rateSelf))
		{
			double actorHp = actor.getCurrentHpPercents();
			Skill[] skills = actorHp < 50 ? selectUsableSkills(actor, 0, _healSkills) : selectUsableSkills(actor, 0, _buffSkills);
			if ((skills == null) || (skills.length == 0))
			{
				return false;
			}
			Skill skill = skills[Rnd.get(skills.length)];
			addTaskBuff(actor, skill);
			return true;
		}
		if (Rnd.chance(rateFriends))
		{
			for (NpcInstance npc : activeFactionTargets())
			{
				double targetHp = npc.getCurrentHpPercents();
				Skill[] skills = targetHp < 50 ? selectUsableSkills(actor, 0, _healSkills) : selectUsableSkills(actor, 0, _buffSkills);
				if ((skills == null) || (skills.length == 0))
				{
					continue;
				}
				Skill skill = skills[Rnd.get(skills.length)];
				addTaskBuff(actor, skill);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Method defaultFightTask.
	 * @return boolean
	 */
	protected boolean defaultFightTask()
	{
		clearTasks();
		NpcInstance actor = getActor();
		if (actor.isDead() || actor.isAMuted())
		{
			return false;
		}
		Creature target;
		if ((target = prepareTarget()) == null)
		{
			return false;
		}
		double distance = actor.getDistance(target);
		double targetHp = target.getCurrentHpPercents();
		double actorHp = actor.getCurrentHpPercents();
		Skill[] dam = Rnd.chance(getRateDAM()) ? selectUsableSkills(target, distance, _damSkills) : null;
		Skill[] dot = Rnd.chance(getRateDOT()) ? selectUsableSkills(target, distance, _dotSkills) : null;
		Skill[] debuff = targetHp > 10 ? Rnd.chance(getRateDEBUFF()) ? selectUsableSkills(target, distance, _debuffSkills) : null : null;
		Skill[] stun = Rnd.chance(getRateSTUN()) ? selectUsableSkills(target, distance, _stunSkills) : null;
		Skill[] heal = actorHp < 50 ? Rnd.chance(getRateHEAL()) ? selectUsableSkills(actor, 0, _healSkills) : null : null;
		Skill[] buff = Rnd.chance(getRateBUFF()) ? selectUsableSkills(actor, 0, _buffSkills) : null;
		RndSelector<Skill[]> rnd = new RndSelector<>();
		if (!actor.isAMuted())
		{
			rnd.add(null, getRatePHYS());
		}
		rnd.add(dam, getRateDAM());
		rnd.add(dot, getRateDOT());
		rnd.add(debuff, getRateDEBUFF());
		rnd.add(heal, getRateHEAL());
		rnd.add(buff, getRateBUFF());
		rnd.add(stun, getRateSTUN());
		Skill[] selected = rnd.select();
		if (selected != null)
		{
			if ((selected == dam) || (selected == dot))
			{
				return chooseTaskAndTargets(selectTopSkillByDamage(actor, target, distance, selected), target, distance);
			}
			if ((selected == debuff) || (selected == stun))
			{
				return chooseTaskAndTargets(selectTopSkillByDebuff(actor, target, distance, selected), target, distance);
			}
			if (selected == buff)
			{
				return chooseTaskAndTargets(selectTopSkillByBuff(actor, selected), actor, distance);
			}
			if (selected == heal)
			{
				return chooseTaskAndTargets(selectTopSkillByHeal(actor, selected), actor, distance);
			}
		}
		return chooseTaskAndTargets(null, target, distance);
	}
	
	/**
	 * Method getRatePHYS.
	 * @return int
	 */
	public int getRatePHYS()
	{
		return 100;
	}
	
	/**
	 * Method getRateDOT.
	 * @return int
	 */
	public int getRateDOT()
	{
		return 0;
	}
	
	/**
	 * Method getRateDEBUFF.
	 * @return int
	 */
	public int getRateDEBUFF()
	{
		return 0;
	}
	
	/**
	 * Method getRateDAM.
	 * @return int
	 */
	public int getRateDAM()
	{
		return 0;
	}
	
	/**
	 * Method getRateSTUN.
	 * @return int
	 */
	public int getRateSTUN()
	{
		return 0;
	}
	
	/**
	 * Method getRateBUFF.
	 * @return int
	 */
	public int getRateBUFF()
	{
		return 0;
	}
	
	/**
	 * Method getRateHEAL.
	 * @return int
	 */
	public int getRateHEAL()
	{
		return 0;
	}
	
	/**
	 * Method getIsMobile.
	 * @return boolean
	 */
	public boolean getIsMobile()
	{
		return !getActor().getParameter("isImmobilized", false);
	}
	
	/**
	 * Method getMaxPathfindFails.
	 * @return int
	 */
	public int getMaxPathfindFails()
	{
		return 3;
	}
	
	/**
	 * Method getMaxAttackTimeout.
	 * @return int
	 */
	public int getMaxAttackTimeout()
	{
		return 15000;
	}
	
	/**
	 * Method getTeleportTimeout.
	 * @return int
	 */
	public int getTeleportTimeout()
	{
		return 10000;
	}
}
