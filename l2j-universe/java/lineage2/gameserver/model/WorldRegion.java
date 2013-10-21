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

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import lineage2.commons.lang.ArrayUtils;
import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.ai.CtrlIntention;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.L2GameServerPacket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class WorldRegion implements Iterable<GameObject>
{
	/**
	 * Field EMPTY_L2WORLDREGION_ARRAY.
	 */
	public final static WorldRegion[] EMPTY_L2WORLDREGION_ARRAY = new WorldRegion[0];
	/**
	 * Field _log.
	 */
	@SuppressWarnings("unused")
	private static final Logger _log = LoggerFactory.getLogger(WorldRegion.class);
	
	/**
	 * @author Mobius
	 */
	public class ActivateTask extends RunnableImpl
	{
		/**
		 * Field _isActivating.
		 */
		private final boolean _isActivating;
		
		/**
		 * Constructor for ActivateTask.
		 * @param isActivating boolean
		 */
		public ActivateTask(boolean isActivating)
		{
			_isActivating = isActivating;
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			if (_isActivating)
			{
				World.activate(WorldRegion.this);
			}
			else
			{
				World.deactivate(WorldRegion.this);
			}
		}
	}
	
	/**
	 * Field tileZ. Field tileY. Field tileX.
	 */
	private final int tileX, tileY, tileZ;
	/**
	 * Field _objects.
	 */
	private volatile GameObject[] _objects = GameObject.EMPTY_L2OBJECT_ARRAY;
	/**
	 * Field _objectsCount.
	 */
	private int _objectsCount = 0;
	/**
	 * Field _zones.
	 */
	private volatile Zone[] _zones = Zone.EMPTY_L2ZONE_ARRAY;
	/**
	 * Field _playersCount.
	 */
	private int _playersCount = 0;
	/**
	 * Field _isActive.
	 */
	private final AtomicBoolean _isActive = new AtomicBoolean();
	/**
	 * Field _activateTask.
	 */
	private Future<?> _activateTask;
	/**
	 * Field lock.
	 */
	private final Lock lock = new ReentrantLock();
	
	/**
	 * Constructor for WorldRegion.
	 * @param x int
	 * @param y int
	 * @param z int
	 */
	WorldRegion(int x, int y, int z)
	{
		tileX = x;
		tileY = y;
		tileZ = z;
	}
	
	/**
	 * Method getX.
	 * @return int
	 */
	int getX()
	{
		return tileX;
	}
	
	/**
	 * Method getY.
	 * @return int
	 */
	int getY()
	{
		return tileY;
	}
	
	/**
	 * Method getZ.
	 * @return int
	 */
	int getZ()
	{
		return tileZ;
	}
	
	/**
	 * Method setActive.
	 * @param activate boolean
	 */
	void setActive(boolean activate)
	{
		if (!_isActive.compareAndSet(!activate, activate))
		{
			return;
		}
		NpcInstance npc;
		for (GameObject obj : this)
		{
			if (!obj.isNpc())
			{
				continue;
			}
			npc = (NpcInstance) obj;
			if (npc.getAI().isActive() != isActive())
			{
				if (isActive())
				{
					npc.getAI().startAITask();
					npc.getAI().setIntention(CtrlIntention.AI_INTENTION_ACTIVE);
					npc.startRandomAnimation();
				}
				else if (!npc.getAI().isGlobalAI())
				{
					npc.getAI().stopAITask();
					npc.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE);
					npc.stopRandomAnimation();
				}
			}
		}
	}
	
	/**
	 * Method addToPlayers.
	 * @param object GameObject
	 * @param dropper Creature
	 */
	void addToPlayers(GameObject object, Creature dropper)
	{
		if (object == null)
		{
			return;
		}
		Player player = null;
		if (object.isPlayer())
		{
			player = (Player) object;
		}
		int oid = object.getObjectId();
		int rid = object.getReflectionId();
		Player p;
		for (GameObject obj : this)
		{
			if ((obj.getObjectId() == oid) || (obj.getReflectionId() != rid))
			{
				continue;
			}
			if (player != null)
			{
				player.sendPacket(player.addVisibleObject(obj, null));
			}
			if (obj.isPlayer())
			{
				p = (Player) obj;
				p.sendPacket(p.addVisibleObject(object, dropper));
			}
		}
	}
	
	/**
	 * Method removeFromPlayers.
	 * @param object GameObject
	 */
	void removeFromPlayers(GameObject object)
	{
		if (object == null)
		{
			return;
		}
		Player player = null;
		if (object.isPlayer())
		{
			player = (Player) object;
		}
		int oid = object.getObjectId();
		Reflection rid = object.getReflection();
		Player p;
		List<L2GameServerPacket> d = null;
		for (GameObject obj : this)
		{
			if ((obj.getObjectId() == oid) || (obj.getReflection() != rid))
			{
				continue;
			}
			if (player != null)
			{
				player.sendPacket(player.removeVisibleObject(obj, null));
			}
			if (obj.isPlayer())
			{
				p = (Player) obj;
				p.sendPacket(p.removeVisibleObject(object, d == null ? d = object.deletePacketList() : d));
			}
		}
	}
	
	/**
	 * Method addObject.
	 * @param obj GameObject
	 */
	public void addObject(GameObject obj)
	{
		if (obj == null)
		{
			return;
		}
		lock.lock();
		try
		{
			GameObject[] objects = _objects;
			GameObject[] resizedObjects = new GameObject[_objectsCount + 1];
			System.arraycopy(objects, 0, resizedObjects, 0, _objectsCount);
			objects = resizedObjects;
			objects[_objectsCount++] = obj;
			_objects = resizedObjects;
			if (obj.isPlayer())
			{
				if (_playersCount++ == 0)
				{
					if (_activateTask != null)
					{
						_activateTask.cancel(false);
					}
					_activateTask = ThreadPoolManager.getInstance().schedule(new ActivateTask(true), 1000L);
				}
			}
		}
		finally
		{
			lock.unlock();
		}
	}
	
	/**
	 * Method removeObject.
	 * @param obj GameObject
	 */
	public void removeObject(GameObject obj)
	{
		if (obj == null)
		{
			return;
		}
		lock.lock();
		try
		{
			GameObject[] objects = _objects;
			int index = -1;
			for (int i = 0; i < _objectsCount; i++)
			{
				if (objects[i] == obj)
				{
					index = i;
					break;
				}
			}
			if (index == -1)
			{
				return;
			}
			_objectsCount--;
			GameObject[] resizedObjects = new GameObject[_objectsCount];
			objects[index] = objects[_objectsCount];
			System.arraycopy(objects, 0, resizedObjects, 0, _objectsCount);
			_objects = resizedObjects;
			if (obj.isPlayer())
			{
				if (--_playersCount == 0)
				{
					if (_activateTask != null)
					{
						_activateTask.cancel(false);
					}
					_activateTask = ThreadPoolManager.getInstance().schedule(new ActivateTask(false), 60000L);
				}
			}
		}
		finally
		{
			lock.unlock();
		}
	}
	
	/**
	 * Method getObjectsSize.
	 * @return int
	 */
	public int getObjectsSize()
	{
		return _objectsCount;
	}
	
	/**
	 * Method getPlayersCount.
	 * @return int
	 */
	public int getPlayersCount()
	{
		return _playersCount;
	}
	
	/**
	 * Method isEmpty.
	 * @return boolean
	 */
	public boolean isEmpty()
	{
		return _playersCount == 0;
	}
	
	/**
	 * Method isActive.
	 * @return boolean
	 */
	public boolean isActive()
	{
		return _isActive.get();
	}
	
	/**
	 * Method addZone.
	 * @param zone Zone
	 */
	void addZone(Zone zone)
	{
		lock.lock();
		try
		{
			_zones = ArrayUtils.add(_zones, zone);
		}
		finally
		{
			lock.unlock();
		}
	}
	
	/**
	 * Method removeZone.
	 * @param zone Zone
	 */
	void removeZone(Zone zone)
	{
		lock.lock();
		try
		{
			_zones = ArrayUtils.remove(_zones, zone);
		}
		finally
		{
			lock.unlock();
		}
	}
	
	/**
	 * Method getZones.
	 * @return Zone[]
	 */
	Zone[] getZones()
	{
		return _zones;
	}
	
	/**
	 * Method toString.
	 * @return String
	 */
	@Override
	public String toString()
	{
		return "[" + tileX + ", " + tileY + ", " + tileZ + "]";
	}
	
	/**
	 * Method iterator.
	 * @return Iterator<GameObject> * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<GameObject> iterator()
	{
		return new InternalIterator(_objects);
	}
	
	/**
	 * @author Mobius
	 */
	private class InternalIterator implements Iterator<GameObject>
	{
		/**
		 * Field objects.
		 */
		final GameObject[] objects;
		/**
		 * Field cursor.
		 */
		int cursor = 0;
		
		/**
		 * Constructor for InternalIterator.
		 * @param objects GameObject[]
		 */
		public InternalIterator(final GameObject[] objects)
		{
			this.objects = objects;
		}
		
		/**
		 * Method hasNext.
		 * @return boolean * @see java.util.Iterator#hasNext()
		 */
		@Override
		public boolean hasNext()
		{
			if (cursor < objects.length)
			{
				return objects[cursor] != null;
			}
			return false;
		}
		
		/**
		 * Method next.
		 * @return GameObject * @see java.util.Iterator#next()
		 */
		@Override
		public GameObject next()
		{
			return objects[cursor++];
		}
		
		/**
		 * Method remove.
		 * @see java.util.Iterator#remove()
		 */
		@Override
		public void remove()
		{
			throw new UnsupportedOperationException();
		}
	}
}
