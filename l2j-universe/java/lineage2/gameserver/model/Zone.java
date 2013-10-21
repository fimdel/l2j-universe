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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import lineage2.commons.collections.LazyArrayList;
import lineage2.commons.collections.MultiValueSet;
import lineage2.commons.listener.Listener;
import lineage2.commons.listener.ListenerList;
import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.listener.zone.OnZoneEnterLeaveListener;
import lineage2.gameserver.model.base.Race;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.network.serverpackets.EventTrigger;
import lineage2.gameserver.network.serverpackets.L2GameServerPacket;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.stats.Stats;
import lineage2.gameserver.stats.funcs.FuncAdd;
import lineage2.gameserver.taskmanager.EffectTaskManager;
import lineage2.gameserver.templates.ZoneTemplate;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.PositionUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Zone
{
	/**
	 * Field EMPTY_L2ZONE_ARRAY.
	 */
	public static final Zone[] EMPTY_L2ZONE_ARRAY = new Zone[0];
	
	/**
	 * @author Mobius
	 */
	public static enum ZoneType
	{
		/**
		 * Field AirshipController.
		 */
		AirshipController,
		/**
		 * Field SIEGE.
		 */
		SIEGE,
		/**
		 * Field RESIDENCE.
		 */
		RESIDENCE,
		/**
		 * Field HEADQUARTER.
		 */
		HEADQUARTER,
		/**
		 * Field FISHING.
		 */
		FISHING,
		/**
		 * Field CHANGED_ZONE.
		 */
		CHANGED_ZONE,
		/**
		 * Field water.
		 */
		water,
		/**
		 * Field battle_zone.
		 */
		battle_zone,
		/**
		 * Field damage.
		 */
		damage,
		/**
		 * Field instant_skill.
		 */
		instant_skill,
		/**
		 * Field mother_tree.
		 */
		mother_tree,
		/**
		 * Field peace_zone.
		 */
		peace_zone,
		/**
		 * Field poison.
		 */
		poison,
		/**
		 * Field ssq_zone.
		 */
		ssq_zone,
		/**
		 * Field swamp.
		 */
		swamp,
		/**
		 * Field no_escape.
		 */
		no_escape,
		/**
		 * Field no_landing.
		 */
		no_landing,
		/**
		 * Field no_restart.
		 */
		no_restart,
		/**
		 * Field no_summon.
		 */
		no_summon,
		/**
		 * Field dummy.
		 */
		dummy,
		/**
		 * Field offshore.
		 */
		offshore,
		/**
		 * Field epic.
		 */
		epic,
		/**
		 * Field JUMPING.
		 */
		JUMPING,
	}
	
	/**
	 * @author Mobius
	 */
	public enum ZoneTarget
	{
		/**
		 * Field pc.
		 */
		pc,
		/**
		 * Field npc.
		 */
		npc,
		/**
		 * Field only_pc.
		 */
		only_pc
	}
	
	/**
	 * Field BLOCKED_ACTION_PRIVATE_STORE. (value is ""open_private_store"")
	 */
	public static final String BLOCKED_ACTION_PRIVATE_STORE = "open_private_store";
	/**
	 * Field BLOCKED_ACTION_PRIVATE_WORKSHOP. (value is ""open_private_workshop"")
	 */
	public static final String BLOCKED_ACTION_PRIVATE_WORKSHOP = "open_private_workshop";
	/**
	 * Field BLOCKED_ACTION_DROP_MERCHANT_GUARD. (value is ""drop_merchant_guard"")
	 */
	public static final String BLOCKED_ACTION_DROP_MERCHANT_GUARD = "drop_merchant_guard";
	/**
	 * Field BLOCKED_ACTION_SAVE_BOOKMARK. (value is ""save_bookmark"")
	 */
	public static final String BLOCKED_ACTION_SAVE_BOOKMARK = "save_bookmark";
	/**
	 * Field BLOCKED_ACTION_USE_BOOKMARK. (value is ""use_bookmark"")
	 */
	public static final String BLOCKED_ACTION_USE_BOOKMARK = "use_bookmark";
	/**
	 * Field BLOCKED_ACTION_MINIMAP. (value is ""open_minimap"")
	 */
	public static final String BLOCKED_ACTION_MINIMAP = "open_minimap";
	
	/**
	 * @author Mobius
	 */
	private abstract class ZoneTimer extends RunnableImpl
	{
		/**
		 * Field cha.
		 */
		protected Creature cha;
		/**
		 * Field future.
		 */
		protected Future<?> future;
		/**
		 * Field active.
		 */
		protected boolean active;
		
		/**
		 * Constructor for ZoneTimer.
		 * @param cha Creature
		 */
		public ZoneTimer(Creature cha)
		{
			this.cha = cha;
		}
		
		/**
		 * Method start.
		 */
		public void start()
		{
			active = true;
			future = EffectTaskManager.getInstance().schedule(this, getTemplate().getInitialDelay() * 1000L);
		}
		
		/**
		 * Method stop.
		 */
		public void stop()
		{
			active = false;
			if (future != null)
			{
				future.cancel(false);
				future = null;
			}
		}
		
		/**
		 * Method next.
		 */
		public void next()
		{
			if (!active)
			{
				return;
			}
			if ((getTemplate().getUnitTick() == 0) && (getTemplate().getRandomTick() == 0))
			{
				return;
			}
			future = EffectTaskManager.getInstance().schedule(this, (getTemplate().getUnitTick() + Rnd.get(0, getTemplate().getRandomTick())) * 1000L);
		}
		
		/**
		 * Method runImpl.
		 * @throws Exception
		 */
		@Override
		public abstract void runImpl() throws Exception;
	}
	
	/**
	 * @author Mobius
	 */
	private class SkillTimer extends ZoneTimer
	{
		/**
		 * Constructor for SkillTimer.
		 * @param cha Creature
		 */
		public SkillTimer(Creature cha)
		{
			super(cha);
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			if (!isActive())
			{
				return;
			}
			if (!checkTarget(cha))
			{
				return;
			}
			Skill skill = getZoneSkill();
			if (skill == null)
			{
				return;
			}
			if (Rnd.chance(getTemplate().getSkillProb()) && !cha.isDead())
			{
				skill.getEffects(cha, cha, false, false);
			}
			next();
		}
	}
	
	/**
	 * @author Mobius
	 */
	private class DamageTimer extends ZoneTimer
	{
		/**
		 * Constructor for DamageTimer.
		 * @param cha Creature
		 */
		public DamageTimer(Creature cha)
		{
			super(cha);
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			if (!isActive())
			{
				return;
			}
			if (!checkTarget(cha))
			{
				return;
			}
			int hp = getDamageOnHP();
			int mp = getDamageOnMP();
			int message = getDamageMessageId();
			if ((hp == 0) && (mp == 0))
			{
				return;
			}
			if (hp > 0)
			{
				cha.reduceCurrentHp(hp, 0, cha, null, false, false, true, false, false, false, true);
				if (message > 0)
				{
					cha.sendPacket(new SystemMessage(message).addNumber(hp));
				}
			}
			if (mp > 0)
			{
				cha.reduceCurrentMp(mp, null);
				if (message > 0)
				{
					cha.sendPacket(new SystemMessage(message).addNumber(mp));
				}
			}
			next();
		}
	}
	
	/**
	 * @author Mobius
	 */
	public class ZoneListenerList extends ListenerList<Zone>
	{
		/**
		 * Method onEnter.
		 * @param actor Creature
		 */
		public void onEnter(Creature actor)
		{
			if (!getListeners().isEmpty())
			{
				for (Listener<Zone> listener : getListeners())
				{
					((OnZoneEnterLeaveListener) listener).onZoneEnter(Zone.this, actor);
				}
			}
		}
		
		/**
		 * Method onLeave.
		 * @param actor Creature
		 */
		public void onLeave(Creature actor)
		{
			if (!getListeners().isEmpty())
			{
				for (Listener<Zone> listener : getListeners())
				{
					((OnZoneEnterLeaveListener) listener).onZoneLeave(Zone.this, actor);
				}
			}
		}
	}
	
	/**
	 * Field _type.
	 */
	private ZoneType _type;
	/**
	 * Field _active.
	 */
	private boolean _active;
	/**
	 * Field _params.
	 */
	private final MultiValueSet<String> _params;
	/**
	 * Field _template.
	 */
	private final ZoneTemplate _template;
	/**
	 * Field _reflection.
	 */
	private Reflection _reflection;
	/**
	 * Field listeners.
	 */
	private final ZoneListenerList listeners = new ZoneListenerList();
	/**
	 * Field lock.
	 */
	private final ReadWriteLock lock = new ReentrantReadWriteLock();
	/**
	 * Field readLock.
	 */
	private final Lock readLock = lock.readLock();
	/**
	 * Field writeLock.
	 */
	private final Lock writeLock = lock.writeLock();
	/**
	 * Field _objects.
	 */
	private final List<Creature> _objects = new LazyArrayList<>(32);
	/**
	 * Field _zoneTimers.
	 */
	private final Map<Creature, ZoneTimer> _zoneTimers = new ConcurrentHashMap<>();
	/**
	 * Field ZONE_STATS_ORDER.
	 */
	public final static int ZONE_STATS_ORDER = 0x40;
	
	/**
	 * Constructor for Zone.
	 * @param template ZoneTemplate
	 */
	public Zone(ZoneTemplate template)
	{
		this(template.getType(), template);
	}
	
	/**
	 * Constructor for Zone.
	 * @param type ZoneType
	 * @param template ZoneTemplate
	 */
	public Zone(ZoneType type, ZoneTemplate template)
	{
		_type = type;
		_template = template;
		_params = template.getParams();
	}
	
	/**
	 * Method getTemplate.
	 * @return ZoneTemplate
	 */
	public ZoneTemplate getTemplate()
	{
		return _template;
	}
	
	/**
	 * Method getName.
	 * @return String
	 */
	public final String getName()
	{
		return getTemplate().getName();
	}
	
	/**
	 * Method getType.
	 * @return ZoneType
	 */
	public ZoneType getType()
	{
		return _type;
	}
	
	/**
	 * Method setType.
	 * @param type ZoneType
	 */
	public void setType(ZoneType type)
	{
		_type = type;
	}
	
	/**
	 * Method getTerritory.
	 * @return Territory
	 */
	public Territory getTerritory()
	{
		return getTemplate().getTerritory();
	}
	
	/**
	 * Method getEnteringMessageId.
	 * @return int
	 */
	public final int getEnteringMessageId()
	{
		return getTemplate().getEnteringMessageId();
	}
	
	/**
	 * Method getLeavingMessageId.
	 * @return int
	 */
	public final int getLeavingMessageId()
	{
		return getTemplate().getLeavingMessageId();
	}
	
	/**
	 * Method getZoneSkill.
	 * @return Skill
	 */
	public Skill getZoneSkill()
	{
		return getTemplate().getZoneSkill();
	}
	
	/**
	 * Method getZoneTarget.
	 * @return ZoneTarget
	 */
	public ZoneTarget getZoneTarget()
	{
		return getTemplate().getZoneTarget();
	}
	
	/**
	 * Method getAffectRace.
	 * @return Race
	 */
	public Race getAffectRace()
	{
		return getTemplate().getAffectRace();
	}
	
	/**
	 * Method getDamageMessageId.
	 * @return int
	 */
	public int getDamageMessageId()
	{
		return getTemplate().getDamageMessageId();
	}
	
	/**
	 * Method getDamageOnHP.
	 * @return int
	 */
	public int getDamageOnHP()
	{
		return getTemplate().getDamageOnHP();
	}
	
	/**
	 * Method getDamageOnMP.
	 * @return int
	 */
	public int getDamageOnMP()
	{
		return getTemplate().getDamageOnMP();
	}
	
	/**
	 * Method getMoveBonus.
	 * @return double
	 */
	public double getMoveBonus()
	{
		return getTemplate().getMoveBonus();
	}
	
	/**
	 * Method getRegenBonusHP.
	 * @return double
	 */
	public double getRegenBonusHP()
	{
		return getTemplate().getRegenBonusHP();
	}
	
	/**
	 * Method getRegenBonusMP.
	 * @return double
	 */
	public double getRegenBonusMP()
	{
		return getTemplate().getRegenBonusMP();
	}
	
	/**
	 * Method getRestartTime.
	 * @return long
	 */
	public long getRestartTime()
	{
		return getTemplate().getRestartTime();
	}
	
	/**
	 * Method getRestartPoints.
	 * @return List<Location>
	 */
	public List<Location> getRestartPoints()
	{
		return getTemplate().getRestartPoints();
	}
	
	/**
	 * Method getPKRestartPoints.
	 * @return List<Location>
	 */
	public List<Location> getPKRestartPoints()
	{
		return getTemplate().getPKRestartPoints();
	}
	
	/**
	 * Method getSpawn.
	 * @return Location
	 */
	public Location getSpawn()
	{
		if (getRestartPoints() == null)
		{
			return null;
		}
		Location loc = getRestartPoints().get(Rnd.get(getRestartPoints().size()));
		return loc.clone();
	}
	
	/**
	 * Method getPKSpawn.
	 * @return Location
	 */
	public Location getPKSpawn()
	{
		if (getPKRestartPoints() == null)
		{
			return getSpawn();
		}
		Location loc = getPKRestartPoints().get(Rnd.get(getPKRestartPoints().size()));
		return loc.clone();
	}
	
	/**
	 * Method checkIfInZone.
	 * @param x int
	 * @param y int
	 * @return boolean
	 */
	public boolean checkIfInZone(int x, int y)
	{
		return getTerritory().isInside(x, y);
	}
	
	/**
	 * Method checkIfInZone.
	 * @param x int
	 * @param y int
	 * @param z int
	 * @return boolean
	 */
	public boolean checkIfInZone(int x, int y, int z)
	{
		return checkIfInZone(x, y, z, getReflection());
	}
	
	/**
	 * Method checkIfInZone.
	 * @param x int
	 * @param y int
	 * @param z int
	 * @param reflection Reflection
	 * @return boolean
	 */
	public boolean checkIfInZone(int x, int y, int z, Reflection reflection)
	{
		return isActive() && (_reflection == reflection) && getTerritory().isInside(x, y, z);
	}
	
	/**
	 * Method checkIfInZone.
	 * @param cha Creature
	 * @return boolean
	 */
	public boolean checkIfInZone(Creature cha)
	{
		readLock.lock();
		try
		{
			return _objects.contains(cha);
		}
		finally
		{
			readLock.unlock();
		}
	}
	
	/**
	 * Method findDistanceToZone.
	 * @param obj GameObject
	 * @param includeZAxis boolean
	 * @return double
	 */
	public final double findDistanceToZone(GameObject obj, boolean includeZAxis)
	{
		return findDistanceToZone(obj.getX(), obj.getY(), obj.getZ(), includeZAxis);
	}
	
	/**
	 * Method findDistanceToZone.
	 * @param x int
	 * @param y int
	 * @param z int
	 * @param includeZAxis boolean
	 * @return double
	 */
	public final double findDistanceToZone(int x, int y, int z, boolean includeZAxis)
	{
		return PositionUtils.calculateDistance(x, y, z, (getTerritory().getXmax() + getTerritory().getXmin()) / 2, (getTerritory().getYmax() + getTerritory().getYmin()) / 2, (getTerritory().getZmax() + getTerritory().getZmin()) / 2, includeZAxis);
	}
	
	/**
	 * Method doEnter.
	 * @param cha Creature
	 */
	public void doEnter(Creature cha)
	{
		boolean added = false;
		writeLock.lock();
		try
		{
			if (!_objects.contains(cha))
			{
				added = _objects.add(cha);
			}
		}
		finally
		{
			writeLock.unlock();
		}
		if (added)
		{
			onZoneEnter(cha);
		}
	}
	
	/**
	 * Method onZoneEnter.
	 * @param actor Creature
	 */
	protected void onZoneEnter(Creature actor)
	{
		checkEffects(actor, true);
		addZoneStats(actor);
		if (actor.isPlayer())
		{
			if (getEnteringMessageId() != 0)
			{
				actor.sendPacket(new SystemMessage(getEnteringMessageId()));
			}
			if (getTemplate().getEventId() != 0)
			{
				actor.sendPacket(new EventTrigger(getTemplate().getEventId(), true));
			}
			if (getTemplate().getBlockedActions() != null)
			{
				((Player) actor).blockActions(getTemplate().getBlockedActions());
			}
		}
		listeners.onEnter(actor);
	}
	
	/**
	 * Method doLeave.
	 * @param cha Creature
	 */
	public void doLeave(Creature cha)
	{
		boolean removed = false;
		writeLock.lock();
		try
		{
			removed = _objects.remove(cha);
		}
		finally
		{
			writeLock.unlock();
		}
		if (removed)
		{
			onZoneLeave(cha);
		}
	}
	
	/**
	 * Method onZoneLeave.
	 * @param actor Creature
	 */
	protected void onZoneLeave(Creature actor)
	{
		checkEffects(actor, false);
		removeZoneStats(actor);
		if (actor.isPlayer())
		{
			if ((getLeavingMessageId() != 0) && actor.isPlayer())
			{
				actor.sendPacket(new SystemMessage(getLeavingMessageId()));
			}
			if ((getTemplate().getEventId() != 0) && actor.isPlayer())
			{
				actor.sendPacket(new EventTrigger(getTemplate().getEventId(), false));
			}
			if (getTemplate().getBlockedActions() != null)
			{
				((Player) actor).unblockActions(getTemplate().getBlockedActions());
			}
		}
		listeners.onLeave(actor);
	}
	
	/**
	 * Method addZoneStats.
	 * @param cha Creature
	 */
	private void addZoneStats(Creature cha)
	{
		if (!checkTarget(cha))
		{
			return;
		}
		if (getMoveBonus() != 0)
		{
			if (cha.isPlayable())
			{
				cha.addStatFunc(new FuncAdd(Stats.RUN_SPEED, ZONE_STATS_ORDER, this, getMoveBonus()));
				cha.sendChanges();
			}
		}
		if (getRegenBonusHP() != 0)
		{
			cha.addStatFunc(new FuncAdd(Stats.REGENERATE_HP_RATE, ZONE_STATS_ORDER, this, getRegenBonusHP()));
		}
		if (getRegenBonusMP() != 0)
		{
			cha.addStatFunc(new FuncAdd(Stats.REGENERATE_MP_RATE, ZONE_STATS_ORDER, this, getRegenBonusMP()));
		}
	}
	
	/**
	 * Method removeZoneStats.
	 * @param cha Creature
	 */
	private void removeZoneStats(Creature cha)
	{
		if ((getRegenBonusHP() == 0) && (getRegenBonusMP() == 0) && (getMoveBonus() == 0))
		{
			return;
		}
		cha.removeStatsOwner(this);
		cha.sendChanges();
	}
	
	/**
	 * Method checkEffects.
	 * @param cha Creature
	 * @param enter boolean
	 */
	private void checkEffects(Creature cha, boolean enter)
	{
		if (checkTarget(cha))
		{
			if (enter)
			{
				if (getZoneSkill() != null)
				{
					ZoneTimer timer = new SkillTimer(cha);
					_zoneTimers.put(cha, timer);
					timer.start();
				}
				else if ((getDamageOnHP() > 0) || (getDamageOnHP() > 0))
				{
					ZoneTimer timer = new DamageTimer(cha);
					_zoneTimers.put(cha, timer);
					timer.start();
				}
			}
			else
			{
				ZoneTimer timer = _zoneTimers.remove(cha);
				if (timer != null)
				{
					timer.stop();
				}
				if (getZoneSkill() != null)
				{
					cha.getEffectList().stopEffect(getZoneSkill());
				}
			}
		}
	}
	
	/**
	 * Method checkTarget.
	 * @param cha Creature
	 * @return boolean
	 */
	boolean checkTarget(Creature cha)
	{
		switch (getZoneTarget())
		{
			case pc:
				if (!cha.isPlayable())
				{
					return false;
				}
				break;
			case only_pc:
				if (!cha.isPlayer())
				{
					return false;
				}
				break;
			case npc:
				if (!cha.isNpc())
				{
					return false;
				}
				break;
		}
		if (getAffectRace() != null)
		{
			Player player = cha.getPlayer();
			if (player == null)
			{
				return false;
			}
			if (player.getRace() != getAffectRace())
			{
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Method getObjects.
	 * @return Creature[]
	 */
	public Creature[] getObjects()
	{
		readLock.lock();
		try
		{
			return _objects.toArray(new Creature[_objects.size()]);
		}
		finally
		{
			readLock.unlock();
		}
	}
	
	/**
	 * Method getInsidePlayers.
	 * @return List<Player>
	 */
	public List<Player> getInsidePlayers()
	{
		List<Player> result = new LazyArrayList<>();
		readLock.lock();
		try
		{
			Creature cha;
			for (int i = 0; i < _objects.size(); i++)
			{
				if (((cha = _objects.get(i)) != null) && cha.isPlayer())
				{
					result.add((Player) cha);
				}
			}
		}
		finally
		{
			readLock.unlock();
		}
		return result;
	}
	
	/**
	 * Method getInsidePlayables.
	 * @return List<Playable>
	 */
	public List<Playable> getInsidePlayables()
	{
		List<Playable> result = new LazyArrayList<>();
		readLock.lock();
		try
		{
			Creature cha;
			for (int i = 0; i < _objects.size(); i++)
			{
				if (((cha = _objects.get(i)) != null) && cha.isPlayable())
				{
					result.add((Playable) cha);
				}
			}
		}
		finally
		{
			readLock.unlock();
		}
		return result;
	}
	
	/**
	 * Method setActive.
	 * @param value boolean
	 */
	public void setActive(boolean value)
	{
		writeLock.lock();
		try
		{
			if (_active == value)
			{
				return;
			}
			_active = value;
		}
		finally
		{
			writeLock.unlock();
		}
		if (isActive())
		{
			World.addZone(Zone.this);
		}
		else
		{
			World.removeZone(Zone.this);
		}
	}
	
	/**
	 * Method isActive.
	 * @return boolean
	 */
	public boolean isActive()
	{
		return _active;
	}
	
	/**
	 * Method setReflection.
	 * @param reflection Reflection
	 */
	public void setReflection(Reflection reflection)
	{
		_reflection = reflection;
	}
	
	/**
	 * Method getReflection.
	 * @return Reflection
	 */
	public Reflection getReflection()
	{
		return _reflection;
	}
	
	/**
	 * Method setParam.
	 * @param name String
	 * @param value String
	 */
	public void setParam(String name, String value)
	{
		_params.put(name, value);
	}
	
	/**
	 * Method setParam.
	 * @param name String
	 * @param value Object
	 */
	public void setParam(String name, Object value)
	{
		_params.put(name, value);
	}
	
	/**
	 * Method getParams.
	 * @return MultiValueSet<String>
	 */
	public MultiValueSet<String> getParams()
	{
		return _params;
	}
	
	/**
	 * Method addListener.
	 * @param listener T
	 * @return boolean
	 */
	public <T extends Listener<Zone>> boolean addListener(T listener)
	{
		return listeners.add(listener);
	}
	
	/**
	 * Method removeListener.
	 * @param listener T
	 * @return boolean
	 */
	public <T extends Listener<Zone>> boolean removeListener(T listener)
	{
		return listeners.remove(listener);
	}
	
	/**
	 * Method toString.
	 * @return String
	 */
	@Override
	public final String toString()
	{
		return "[Zone " + getType() + " name: " + getName() + "]";
	}
	
	/**
	 * Method broadcastPacket.
	 * @param packet L2GameServerPacket
	 * @param toAliveOnly boolean
	 */
	public void broadcastPacket(L2GameServerPacket packet, boolean toAliveOnly)
	{
		List<Player> insideZoners = getInsidePlayers();
		if ((insideZoners != null) && !insideZoners.isEmpty())
		{
			for (Player player : insideZoners)
			{
				if (toAliveOnly)
				{
					if (!player.isDead())
					{
						player.broadcastPacket(packet);
					}
				}
				else
				{
					player.broadcastPacket(packet);
				}
			}
		}
	}
}
