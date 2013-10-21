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

import lineage2.commons.collections.MultiValueSet;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.Config;
import lineage2.gameserver.geodata.GeoEngine;
import lineage2.gameserver.instancemanager.ReflectionManager;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.entity.events.EventOwner;
import lineage2.gameserver.model.entity.events.GlobalEvent;
import lineage2.gameserver.model.instances.MinionInstance;
import lineage2.gameserver.model.instances.MonsterInstance;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.instances.PetInstance;
import lineage2.gameserver.taskmanager.SpawnTaskManager;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.templates.spawn.SpawnRange;
import lineage2.gameserver.utils.Location;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public abstract class Spawner extends EventOwner implements Cloneable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field _log.
	 */
	protected static final Logger _log = LoggerFactory.getLogger(Spawner.class);
	/**
	 * Field MIN_RESPAWN_DELAY. (value is 20)
	 */
	protected static final int MIN_RESPAWN_DELAY = 20;
	/**
	 * Field _maximumCount.
	 */
	protected int _maximumCount;
	/**
	 * Field _referenceCount.
	 */
	protected int _referenceCount;
	/**
	 * Field _currentCount.
	 */
	protected int _currentCount;
	/**
	 * Field _scheduledCount.
	 */
	protected int _scheduledCount;
	/**
	 * Field _nativeRespawnDelay. Field _respawnDelayRandom. Field _respawnDelay.
	 */
	protected int _respawnDelay, _respawnDelayRandom, _nativeRespawnDelay;
	/**
	 * Field _respawnTime.
	 */
	protected int _respawnTime;
	/**
	 * Field _doRespawn.
	 */
	protected boolean _doRespawn;
	/**
	 * Field _lastSpawn.
	 */
	protected NpcInstance _lastSpawn;
	/**
	 * Field _spawned.
	 */
	protected List<NpcInstance> _spawned;
	/**
	 * Field _reflection.
	 */
	protected Reflection _reflection = ReflectionManager.DEFAULT;
	
	/**
	 * Method decreaseScheduledCount.
	 */
	public void decreaseScheduledCount()
	{
		if (_scheduledCount > 0)
		{
			_scheduledCount--;
		}
	}
	
	/**
	 * Method isDoRespawn.
	 * @return boolean
	 */
	public boolean isDoRespawn()
	{
		return _doRespawn;
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
	 * Method setReflection.
	 * @param reflection Reflection
	 */
	public void setReflection(Reflection reflection)
	{
		_reflection = reflection;
	}
	
	/**
	 * Method getRespawnDelay.
	 * @return int
	 */
	public int getRespawnDelay()
	{
		return _respawnDelay;
	}
	
	/**
	 * Method getNativeRespawnDelay.
	 * @return int
	 */
	public int getNativeRespawnDelay()
	{
		return _nativeRespawnDelay;
	}
	
	/**
	 * Method getRespawnDelayRandom.
	 * @return int
	 */
	public int getRespawnDelayRandom()
	{
		return _respawnDelayRandom;
	}
	
	/**
	 * Method getRespawnDelayWithRnd.
	 * @return int
	 */
	public int getRespawnDelayWithRnd()
	{
		return _respawnDelayRandom == 0 ? _respawnDelay : Rnd.get(_respawnDelay - _respawnDelayRandom, _respawnDelay);
	}
	
	/**
	 * Method getRespawnTime.
	 * @return int
	 */
	public int getRespawnTime()
	{
		return _respawnTime;
	}
	
	/**
	 * Method getLastSpawn.
	 * @return NpcInstance
	 */
	public NpcInstance getLastSpawn()
	{
		return _lastSpawn;
	}
	
	/**
	 * Method setAmount.
	 * @param amount int
	 */
	public void setAmount(int amount)
	{
		if (_referenceCount == 0)
		{
			_referenceCount = amount;
		}
		_maximumCount = amount;
	}
	
	/**
	 * Method deleteAll.
	 */
	public void deleteAll()
	{
		stopRespawn();
		for (NpcInstance npc : _spawned)
		{
			npc.deleteMe();
		}
		_spawned.clear();
		_respawnTime = 0;
		_scheduledCount = 0;
		_currentCount = 0;
	}
	
	/**
	 * Method decreaseCount.
	 * @param oldNpc NpcInstance
	 */
	public abstract void decreaseCount(NpcInstance oldNpc);
	
	/**
	 * Method doSpawn.
	 * @param spawn boolean
	 * @return NpcInstance
	 */
	public abstract NpcInstance doSpawn(boolean spawn);
	
	/**
	 * Method respawnNpc.
	 * @param oldNpc NpcInstance
	 */
	public abstract void respawnNpc(NpcInstance oldNpc);
	
	/**
	 * Method initNpc.
	 * @param mob NpcInstance
	 * @param spawn boolean
	 * @param set MultiValueSet<String>
	 * @return NpcInstance
	 */
	protected abstract NpcInstance initNpc(NpcInstance mob, boolean spawn, MultiValueSet<String> set);
	
	/**
	 * Method getCurrentNpcId.
	 * @return int
	 */
	public abstract int getCurrentNpcId();
	
	/**
	 * Method getCurrentSpawnRange.
	 * @return SpawnRange
	 */
	public abstract SpawnRange getCurrentSpawnRange();
	
	/**
	 * Method init.
	 * @return int
	 */
	public int init()
	{
		while ((_currentCount + _scheduledCount) < _maximumCount)
		{
			doSpawn(false);
		}
		_doRespawn = true;
		return _currentCount;
	}
	
	/**
	 * Method spawnOne.
	 * @return NpcInstance
	 */
	public NpcInstance spawnOne()
	{
		return doSpawn(false);
	}
	
	/**
	 * Method stopRespawn.
	 */
	public void stopRespawn()
	{
		_doRespawn = false;
	}
	
	/**
	 * Method startRespawn.
	 */
	public void startRespawn()
	{
		_doRespawn = true;
	}
	
	/**
	 * Method getAllSpawned.
	 * @return List<NpcInstance>
	 */
	public List<NpcInstance> getAllSpawned()
	{
		return _spawned;
	}
	
	/**
	 * Method getFirstSpawned.
	 * @return NpcInstance
	 */
	public NpcInstance getFirstSpawned()
	{
		List<NpcInstance> npcs = getAllSpawned();
		return npcs.size() > 0 ? npcs.get(0) : null;
	}
	
	/**
	 * Method setRespawnDelay.
	 * @param respawnDelay int
	 * @param respawnDelayRandom int
	 */
	public void setRespawnDelay(int respawnDelay, int respawnDelayRandom)
	{
		if (respawnDelay < 0)
		{
			_log.warn("respawn delay is negative");
		}
		_nativeRespawnDelay = respawnDelay;
		_respawnDelay = respawnDelay;
		_respawnDelayRandom = respawnDelayRandom;
	}
	
	/**
	 * Method setRespawnDelay.
	 * @param respawnDelay int
	 */
	public void setRespawnDelay(int respawnDelay)
	{
		setRespawnDelay(respawnDelay, 0);
	}
	
	/**
	 * Method setRespawnTime.
	 * @param respawnTime int
	 */
	public void setRespawnTime(int respawnTime)
	{
		_respawnTime = respawnTime;
	}
	
	/**
	 * Method doSpawn0.
	 * @param template NpcTemplate
	 * @param spawn boolean
	 * @param set MultiValueSet<String>
	 * @return NpcInstance
	 */
	protected NpcInstance doSpawn0(NpcTemplate template, boolean spawn, MultiValueSet<String> set)
	{
		if (template.isInstanceOf(PetInstance.class) || template.isInstanceOf(MinionInstance.class))
		{
			_currentCount++;
			return null;
		}
		NpcInstance tmp = template.getNewInstance();
		if (tmp == null)
		{
			return null;
		}
		if (!spawn)
		{
			spawn = _respawnTime <= ((System.currentTimeMillis() / 1000) + MIN_RESPAWN_DELAY);
		}
		return initNpc(tmp, spawn, set);
	}
	
	/**
	 * Method initNpc0.
	 * @param mob NpcInstance
	 * @param newLoc Location
	 * @param spawn boolean
	 * @param set MultiValueSet<String>
	 * @return NpcInstance
	 */
	protected NpcInstance initNpc0(NpcInstance mob, Location newLoc, boolean spawn, MultiValueSet<String> set)
	{
		mob.setParameters(set);
		mob.setCurrentHpMp(mob.getMaxHp(), mob.getMaxMp(), true);
		mob.setSpawn(this);
		mob.setSpawnedLoc(newLoc);
		mob.setUnderground(GeoEngine.getHeight(newLoc, getReflection().getGeoIndex()) < GeoEngine.getHeight(newLoc.clone().changeZ(5000), getReflection().getGeoIndex()));
		for (GlobalEvent e : getEvents())
		{
			mob.addEvent(e);
		}
		if (spawn)
		{
			mob.setReflection(getReflection());
			if (mob.isMonster())
			{
				((MonsterInstance) mob).setChampion();
			}
			mob.spawnMe(newLoc);
			_currentCount++;
		}
		else
		{
			mob.setLoc(newLoc);
			_scheduledCount++;
			SpawnTaskManager.getInstance().addSpawnTask(mob, (_respawnTime * 1000L) - System.currentTimeMillis());
		}
		_spawned.add(mob);
		_lastSpawn = mob;
		return mob;
	}
	
	/**
	 * Method decreaseCount0.
	 * @param template NpcTemplate
	 * @param spawnedNpc NpcInstance
	 * @param deadTime long
	 */
	public void decreaseCount0(NpcTemplate template, NpcInstance spawnedNpc, long deadTime)
	{
		_currentCount--;
		if (_currentCount < 0)
		{
			_currentCount = 0;
		}
		if (_respawnDelay == 0)
		{
			return;
		}
		if (_doRespawn && ((_scheduledCount + _currentCount) < _maximumCount))
		{
			_scheduledCount++;
			long delay = (long) (template.isRaid ? Config.ALT_RAID_RESPAWN_MULTIPLIER * getRespawnDelayWithRnd() : getRespawnDelayWithRnd()) * 1000L;
			delay = Math.max(1000, delay - deadTime);
			_respawnTime = (int) ((System.currentTimeMillis() + delay) / 1000);
			SpawnTaskManager.getInstance().addSpawnTask(spawnedNpc, delay);
		}
	}
}
